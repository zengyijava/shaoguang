package com.montnets.emp.msgflow.vo.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.table.approveflow.TableLfFlowRecord;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.util.PageInfo;

public class GenericLfFlowRecordVoDAO  extends SuperDAO
{
	/**
	 * @param GL_UserID
	 * @param lfFlowRecordVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordVo> findLfFlowRecordVo(Long GL_UserID,
			LfFlowRecordVo lfFlowRecordVo, PageInfo pageInfo) throws Exception
	{
		//字段sql
		String fieldSql = GenericLfFlowRecordVoSQL.getFieldSql();
		//表名sql
		String tableSql = GenericLfFlowRecordVoSQL.getTableSql();
		tableSql=tableSql+" and mttask."+TableLfMttask.MS_TYPE+"=userdata."+TableUserdata.ACCOUNTTYPE;
		//管辖范围
		String dominationSql = GenericLfFlowRecordVoSQL.getDominationSql(String
				.valueOf(GL_UserID));
		String conditionSql = GenericLfFlowRecordVoSQL
				.getConditionSql(lfFlowRecordVo);
		//时间
		List<String> timeList = GenericLfFlowRecordVoSQL
				.getTimeCondition(lfFlowRecordVo);
		String orderBySql = GenericLfFlowRecordVoSQL.getOrderBySql();
		//查询sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//分页sql
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(dominationSql).append(conditionSql)
				.toString();
		//调用vo的list公共查询方法，并返回查询数据
		List<LfFlowRecordVo> lfFlowRecordVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
						LfFlowRecordVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		//返回数据
		return lfFlowRecordVoList;
	}

	/**
	 * 获取当前登录用户的审批流信息
	 * @param loginUserId
	 * @param lfFlowRecordVo
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordVo> findLfFlowRecordVo(Long loginUserId,
			LfFlowRecordVo lfFlowRecordVo) throws Exception
	{
		//字段sql
		String fieldSql = GenericLfFlowRecordVoSQL.getFieldSql();
		//表名sql
		String tableSql = GenericLfFlowRecordVoSQL.getTableSql();
		//管辖范围
		String dominationSql = GenericLfFlowRecordVoSQL.getDominationSql(String
				.valueOf(loginUserId));
		//条件sql
		String conditionSql = GenericLfFlowRecordVoSQL
				.getConditionSql(lfFlowRecordVo);
		//时间
		List<String> timeList = GenericLfFlowRecordVoSQL
				.getTimeCondition(lfFlowRecordVo);
		//排序sql
		String orderBySql = GenericLfFlowRecordVoSQL.getOrderBySql();
		//查询sql拼接
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//调用vo的list公共查询方法，并返回查询数据
		List<LfFlowRecordVo> returnVoList = findVoListBySQL(
				LfFlowRecordVo.class, sql, StaticValue.EMP_POOLNAME, timeList);
		//返回数据
		return returnVoList;
	}
	
	/**
	 * @param mtID
	 * @param rLevel
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordVo> findLfFlowRecordVo(String mtID, String rLevel,String reviewType)
			throws Exception
	{
		//字段
		String fieldSql = GenericLfFlowRecordVoSQL.getFieldSql();
		//表名
		String tableSql = GenericLfFlowRecordVoSQL.getTableSql();
		
		tableSql = tableSql + " and userdata."+TableUserdata.ACCOUNTTYPE + " = " + reviewType;
		//条件
		StringBuffer conditionSql = new StringBuffer(" where flowrecord.").append(
				TableLfFlowRecord.MT_ID).append("=").append(mtID);
		//审批级别
		if(rLevel != null)
		{
			conditionSql = conditionSql.append(" and flowrecord.").append(TableLfFlowRecord.R_LEVEL).append(
					"<=").append(rLevel);
		}
		//类型
		if(reviewType != null)
		{
			conditionSql = conditionSql.append(" and flowrecord.").append(TableLfFlowRecord.REVIEW_TYPE).append(
					"=").append(reviewType);
		}
		//排序
		String orderBySql = new StringBuffer(" order by flowrecord.").append(
				TableLfFlowRecord.R_LEVEL).append(" asc").toString();
		//查询sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		//调用公共方法查询
		List<LfFlowRecordVo> returnVoList = findVoListBySQL(
				LfFlowRecordVo.class, sql, StaticValue.EMP_POOLNAME);
		//返回查询的数据
		return returnVoList;
	}
	
	/**
	 *   获取网讯时间
	 * @param netid
	 * @return
	 * @throws Exception
	 */
	public Long getLfWxTemplateCreateTime(Long netid) throws Exception{
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select creatdate from LF_WX_BASEINFO  where netid=").append(netid);
		Long time = null;
		List<DynaBean> beanList = this.getListDynaBeanBySql(buffer.toString());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(beanList != null && beanList.size()>0){
			DynaBean bean = beanList.get(0);
			time = format.parse(bean.get("creatdate")+"").getTime();
		}
		return time;
	}
	
	
	
	
}
