package com.montnets.emp.query.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;

/**
 * 
 * @project emp_std_192.169.1.81_new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-12-1 下午04:06:10
 * @description
 */
public class SysMtRecordDAO extends SuperDAO
{
	/**
	 * 获取通道号集合
	 * @param corpCode 企业编码
	 * @return 返回通道号动态bean集合
	 */
	public List<DynaBean> findSpgateList(String corpCode)
	{
		//获取查询sql
		String sql = getSpgateSql(corpCode);
		if(sql == null)
		{
			return null;
		}
		//返回集合
		List<DynaBean> returnList = getListDynaBeanBySql(sql);
		return returnList;
	}
	
	/**
	 * 获取通道号查询sql
	 * @param corpCode 企业编码
	 * @return 返回查询sql
	 */
	private String getSpgateSql(String corpCode)
	{
		try
		{
			String sql;
			//单企业
			if(StaticValue.getCORPTYPE() == 0)
			{
				sql = "select SPGATE from XT_GATE_QUEUE "+StaticValue.getWITHNOLOCK()+" where  SPGATE in (select SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" where USERID in (select SPUSER from LF_SP_DEP_BIND "+StaticValue.getWITHNOLOCK()+" where CORP_CODE='" + corpCode + "')) or SPGATE like '200%'";
				return sql;
			}
			
			//多企业
			//100000企业则查全部
			if("100000".equals(corpCode))
			{
				sql = "select distinct(SPGATE) SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK();
			}
			//其他企业按企业编码查
			else
			{
				sql = "select distinct(SPGATE) SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" where USERID in (select SPUSER from LF_SP_DEP_BIND "+StaticValue.getWITHNOLOCK()+" where CORP_CODE='" + corpCode + "')";
			}
			return sql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，下行记录，获取通道号查询sql，异常。");
			return null;
		}
	}
	
	/**
	 * 
	 * @description 系统下行记录获取错误代码说明
	 * @param errCodes 错误码，格式为：code1,code2,code3.....
	 * @param corpCode 企业编码
	 * @return 错误代码说明动态bean集合
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-19 上午09:02:50
	 */
	public List<DynaBean> getErrCodeDis(String errCodes, String corpCode)
	{
		if(errCodes == null || errCodes.length() < 1)
		{
			EmpExecutionContext.error("DAO查询，系统下行记录获取错误代码说明，传入的错误代码为空。");
			return null;
		}
		
		String sql = "select state_code, state_des from lf_statecode where state_code in (" + errCodes + ") and CORP_CODE='"+corpCode+"'";
		
		List<DynaBean> beanList = getListDynaBeanBySql(sql);
		return beanList;
	}
	
}
