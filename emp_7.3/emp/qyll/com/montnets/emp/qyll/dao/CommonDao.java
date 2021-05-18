package com.montnets.emp.qyll.dao;

import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.entity.LlProduct;
import com.montnets.emp.qyll.vo.LlCompInfoVo;

public class CommonDao {
	
    LldgDao lldgDao = new LldgDao();
	
	/**
	 * 查询当前可用的套餐
	 * @return
	 */
	public List<LlProduct> findAllProduct(){
		try {
			LlCompInfoVo compInfoVo = new LlCompInfoBiz().getLlCompInfoBean();
			String sql =  "select * from LL_PRODUCT where status = 1 AND ecid = "+compInfoVo.getCorpCode();
			return lldgDao.findLlProductList(sql);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询套餐信息失败！");
			return null;
		}
	}
	

	/**
	 * 通过套餐编码查询套餐
	 * @param pid
	 * @return
	 */
	public List<LlProduct> findProductByPid(String pid){
		if(pid == null || "".equals(pid)){
			return null;
		}
		StringBuffer sql_ = new StringBuffer("Select * From LL_PRODUCT Where Id = ");
		sql_.append(pid);
		sql_.append("  And Status = 1");
		
		return lldgDao.findLlProductList(sql_.toString());
	}
	
	/**
	 * 更新订购详情表状态为已读（status=1）
	 * @param ids
	 */
	public boolean updateReadDetailStatus(String ids,String year){
		if(ids == null || "".equals(ids)){
			return true;
		}
		
		StringBuffer sbSql = new StringBuffer("Update LL_ORDER_DETAIL");
		sbSql.append(year);
		sbSql.append(" SET STATUS = 1 WHERE ID IN(");
		sbSql.append(ids);
		sbSql.append(")");
		
		//更新成功，返回true
		if(lldgDao.updateSql(sbSql.toString()) > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 更新订购详情表batchId
	 * @param phones 手机号字符串
	 * @param batchId 批次号
	 * @param orderNo 订单编号
	 */
	public boolean updateFlowDetailBatchId(String phones,int batchId,String orderNo,String year){
		if(phones == null || "".equals(phones)){
			return true;
		}
		
		StringBuffer sbSql = new StringBuffer("Update LL_ORDER_DETAIL");
		sbSql.append(year)
		.append("  SET BATCHID = ")
		.append(batchId)
		.append("  ,LLRPT = '0'  WHERE MOBILE IN(")
		.append(phones)
		.append(")	AND ORDERNO = '" )
		.append(orderNo)
		.append("'");
		
		if(lldgDao.updateSql(sbSql.toString()) > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 更新订购任务表订购状态（ORDERSTATUS=0）
	 * @param ids
	 */
	public boolean updateReadTaskFlowStat(String orderNo,String status){
		StringBuffer sbSql = new StringBuffer("UPDATE LL_ORDER_TASK SET ORDERSTATUS = '");
		sbSql.append(status);
		sbSql.append("' WHERE ORDERNO = '");
		sbSql.append(orderNo);
		sbSql.append("'");
		
		//更新成功，返回true
		if(lldgDao.updateSql(sbSql.toString()) > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 更新订购任务表短信发送状态（ORDERSTATUS=0）
	 * @param ids
	 */
	public boolean updateReadTaskSMSStat(String orderNo,String status){
		StringBuffer sbSql = new StringBuffer("UPDATE LL_ORDER_TASK SET SMSSTATUS = '");
		sbSql.append(status);
		sbSql.append("' WHERE ORDERNO = '");
		sbSql.append(orderNo);
		sbSql.append("'");
		
		//更新成功，返回true
		if(lldgDao.updateSql(sbSql.toString()) > 0){
			return true;
		}
		return false;
	}
}



