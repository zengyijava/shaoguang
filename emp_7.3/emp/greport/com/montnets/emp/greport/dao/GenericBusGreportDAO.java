package com.montnets.emp.greport.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.greport.vo.BusGreportVo;

/**
 * 业务类型发送对比报表
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-20 下午04:40:36
 * @description
 */
public class GenericBusGreportDAO extends SuperDAO
{

	/**
	 * 业务类型发送对比
	 * @param busGreportVo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findBusGreportsByVo(BusGreportVo busGreportVo) throws Exception
	{

		String sql = this.getBusGreportSql(busGreportVo);
		// 加上排序
		String orderbyMonthY = "";
		if(busGreportVo != null && busGreportVo.getReporttype() != null && busGreportVo.getReporttype() == 0)
		{
			orderbyMonthY = " ORDER BY BUS.BUS_NAME,MD.IMONTH ASC ";
		}
		else if(busGreportVo != null && busGreportVo.getReporttype() != null && busGreportVo.getReporttype() == 1)
		{
			orderbyMonthY = " ORDER BY BUS.BUS_NAME,MD.IYMD ASC ";
		}

		String dataSql = sql + orderbyMonthY;
		//List<BusGreportVo> returnList = findVoListBySQL(BusGreportVo.class, dataSql, StaticValue.EMP_POOLNAME);
		List<DynaBean> returnList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(dataSql);
		return returnList;
	}

	/**
	 * 获取短彩业务类型报表sql
	 * @param busgreportvo
	 * @return
	 */
	public String getBusGreportSql(BusGreportVo busgreportvo)
	{
		String tablename = "";
		String leftjoinbindsql = "";
		if(busgreportvo != null && busgreportvo.getMstype() != null && busgreportvo.getMstype() == 0)
		{
			tablename = "MT_DATAREPORT";
			leftjoinbindsql = " LEFT JOIN (SELECT DISTINCT CORP_CODE,SPUSER FROM LF_SP_DEP_BIND) LSDB ON LSDB.SPUSER=MD.USERID ";
		}
		else if(busgreportvo != null && busgreportvo.getMstype() != null && busgreportvo.getMstype() == 1)
		{
			tablename = "MMS_DATAREPORT";
			leftjoinbindsql = " LEFT JOIN (SELECT DISTINCT CORP_CODE,MMS_USER FROM LF_MMSACCBIND) LSDB ON LSDB.MMS_USER =MD.USERID ";
		}
		else
		{
			return "";
		}

		//多企业的条件
		String muticontition="";
		//企业编码
		if(busgreportvo.getCorpCode()!= null&&!"".equals(busgreportvo.getCorpCode())){
			 muticontition=" AND LSDB.CORP_CODE='" + busgreportvo.getCorpCode().trim() + "' ";
		}else{
			leftjoinbindsql="";
		}
		
		
		StringBuffer busreportsql = new StringBuffer("SELECT (SUM(MD.ICOUNT)-SUM(MD.RFAIL1)) ICOUNT,MAX(MD.IMONTH) IMONTH,MAX(MD.IYMD) IYMD,MAX(MD.SVRTYPE) SVRTYPE,MAX(BUS.BUS_NAME) BUS_NAME " + " FROM " + tablename 
				+ " MD  LEFT JOIN LF_BUSMANAGER BUS ON MD.SVRTYPE=BUS.BUS_CODE " + leftjoinbindsql + " WHERE MD.Y = " + busgreportvo.getY() + " ");
		StringBuffer conditionSql = new StringBuffer();

//		// 年份
//		if(busgreportvo.getY() != null && !"".equals(busgreportvo.getY()))
//		{
//			conditionSql.append(" AND MD.Y = " + busgreportvo.getY() + " ");
//		}

		// 月份
		if(busgreportvo.getImonth() != null && !"".equals(busgreportvo.getImonth()))
		{
			conditionSql.append(" AND MD.IMONTH = " + busgreportvo.getImonth() + " ");
		}

		// 企业编码
		conditionSql.append(muticontition);
//		if(busgreportvo.getCorpCode() != null && !"".equals(busgreportvo.getCorpCode()))
//		{
//			conditionSql.append(" AND LSDB.CORP_CODE='" + busgreportvo.getCorpCode().trim() + "'");
//		}

		
		// 业务类型
		if(busgreportvo.getSvrtype() != null && !"".equals(busgreportvo.getSvrtype()))
		{
			conditionSql.append(" AND MD.SVRTYPE in (" + busgreportvo.getSvrtype() + ") ");
		}
		
		String groupby = "";
		if(busgreportvo != null && busgreportvo.getReporttype() != null && busgreportvo.getReporttype() == 0)
		{
			groupby = " GROUP BY BUS.BUS_NAME,MD.IMONTH  HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";
		}
		else if(busgreportvo != null && busgreportvo.getReporttype() != null && busgreportvo.getReporttype() == 1)
		{
			groupby = " GROUP BY BUS.BUS_NAME,MD.IYMD  HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";
		}

		// 总的条件语句
		String sql = busreportsql.append(conditionSql).append(groupby).toString();

		return sql;
	}


}
