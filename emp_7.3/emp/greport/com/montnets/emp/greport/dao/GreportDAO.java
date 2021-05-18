package com.montnets.emp.greport.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
/**
 * 图形报表公共类
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-12-1 上午10:45:17
 * @description
 */
public class GreportDAO extends SuperDAO
{
	/**
	 * 获取年份
	 * @param mstype
	 * @return
	 * @throws Exception
	 */
	public List<String> findYears(Integer mstype) throws Exception
	{
		String fieldSql = "SELECT Y FROM ";
		String tablename = "";
		if(mstype != null && mstype == 0)
		{
			tablename = " MT_DATAREPORT ";
		}
		else if(mstype != null && mstype == 1)
		{
			tablename = " MMS_DATAREPORT ";
		}
		else
		{
			return null;
		}
		String sql = fieldSql + tablename + " GROUP BY Y ORDER BY Y DESC ";
		List<String> years = new ArrayList<String>();
		List<DynaBean> returnList=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		if(returnList!=null&&returnList.size()>0){
			for (int i = 0; i < returnList.size(); i++)
			{
				DynaBean dyb=returnList.get(i);
				if(dyb.get("y")!=null){
					years.add(dyb.get("y").toString());
				}
			}
		}
		Calendar c=Calendar.getInstance();
		int currenty=c.get(Calendar.YEAR);
		if(!years.contains(currenty+"")){
			years.add(0,currenty+"");
		}
		
		
//		Connection conn = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try
//		{
//			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
//			ps = conn.prepareStatement(sql);
//			rs = ps.executeQuery();
//			while(rs.next())
//			{
//				years.add(rs.getLong("Y") + "");
//			}
//		}
//		catch (Exception e)
//		{
//			EmpExecutionContext.error(e, "输出人员代码数据异常");
//			throw e;
//		}
//		finally
//		{
//			close(rs, ps, conn);
//		}
		return years;
	}

	/**
	 * 处理SQL条件,不允许使用1 =1方式
	 * @param conSql 条件
	 * @return 处理后的条件
	 */
	public String getConditionSql(String conSql)
	{
		String conditionSql = "";
		try {
			//存在查询条件
			if(conSql != null && conSql.length() > 0)
			{
				//将条件字符串首个and替换为where,不允许1 =1方式
				conditionSql = conSql.replaceFirst("^(\\s*)(?i)and", "$1where");
			}
			return conditionSql;
		} catch (Exception e) {
			EmpExecutionContext.error("处理SQL条件异常，conSql:" + conSql);
			return null;
		}
	}
}
