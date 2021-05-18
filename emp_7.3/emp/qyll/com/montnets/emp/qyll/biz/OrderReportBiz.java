package com.montnets.emp.qyll.biz;

import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.qyll.dao.OrderReportDao;
import com.montnets.emp.qyll.vo.LlOrderReportVo;
import com.montnets.emp.util.PageInfo;

/**
 * 套餐订购统计报表
 * @project p_qyll
 * @author pengyc
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2017-10-17 上午10:44:31
 * @description
 */
public class OrderReportBiz{
	// 套餐报表DAO
	protected OrderReportDao orderReportDao;
	//构造函数
	public OrderReportBiz(){
		orderReportDao = new OrderReportDao();	
	}

	
	/**
	 * 根据条件获取套餐订购统计报表信息
	 * 
	 * @param llOrderReport
	 *        查询信息List
	 * @param pageInfo
	 *        分页对象
	 * @return
	 * @throws Exception
	 */
	public List<LlOrderReportVo> getLlOrderReportList(LlOrderReportVo llOrderReport, PageInfo pageInfo) throws Exception{
		//定义运营商统计报表集合
		List<LlOrderReportVo> returnList;

		try {
			returnList = orderReportDao.getllOrderReportSql(llOrderReport,pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过套餐订购对象获取套餐订购报表数据");
			throw e;
		}
		//返回数据集合
		return returnList;
	}

	/**
	 * 获取表单合计数据
	 * 
	 * @param llOrderReport
	 *        查询信息List
	 * @return
	 * @throws Exception
	 */
	public long[] findSumCount(LlOrderReportVo llOrderReport) throws Exception{
		return orderReportDao.findSumCount(llOrderReport);
	}

}
