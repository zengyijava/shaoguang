package com.montnets.emp.greport.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.greport.vo.TotalGreportVo;

/**
 * 总体发送趋势报表
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-19 上午10:37:18
 * @description
 */
public class GenericTotalGreportDAO extends SuperDAO
{

	/**
	 * 总体发送趋势
	 * @param totalGreportVo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findTotalReportsByVo(TotalGreportVo totalGreportVo) throws Exception
	{

		String sql = this.getTotalGreportSql(totalGreportVo);
		// 加上排序
		String orderbyMonthY = "";
		if(totalGreportVo != null && totalGreportVo.getReporttype() != null && totalGreportVo.getReporttype() == 0)
		{
			orderbyMonthY = " ORDER BY MD.Y,MD.IMONTH ASC ";
		}
		else if(totalGreportVo != null && totalGreportVo.getReporttype() != null && totalGreportVo.getReporttype() == 1)
		{
			orderbyMonthY = " ORDER BY MD.Y,MD.IMONTH,MD.IYMD ASC ";
		}
		else if(totalGreportVo != null && totalGreportVo.getReporttype() != null && totalGreportVo.getReporttype() == 2)
		{
			orderbyMonthY = " ORDER BY MD.Y ASC ";
		}
		String dataSql = sql + orderbyMonthY;
		//List<TotalGreportVo> returnList = findVoListBySQL(TotalGreportVo.class, dataSql, StaticValue.EMP_POOLNAME);
		List<DynaBean> returnList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(dataSql);
		return returnList;
	}

	/**
	 *  获取短彩业务类型报表sql
	 * @param totalgreportvo
	 * @return
	 */
	public String getTotalGreportSql(TotalGreportVo totalgreportvo)
	{
		String tablename = "";
		String leftjoinbindsql = "";
		if(totalgreportvo != null && totalgreportvo.getMstype() != null && totalgreportvo.getMstype() == 0)
		{
			tablename = "MT_DATAREPORT";
			leftjoinbindsql = " LEFT JOIN (SELECT DISTINCT CORP_CODE,SPUSER FROM LF_SP_DEP_BIND) LSDB ON LSDB.SPUSER=MD.USERID ";
		}
		else if(totalgreportvo != null && totalgreportvo.getMstype() != null && totalgreportvo.getMstype() == 1)
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
		if(totalgreportvo.getCorpCode()!= null&&!"".equals(totalgreportvo.getCorpCode())){
			 muticontition=" AND LSDB.CORP_CODE='" + totalgreportvo.getCorpCode().trim() + "' ";
		}else{
			leftjoinbindsql="";
		}
		
		
		StringBuffer busreportsql = new StringBuffer("SELECT (SUM(MD.ICOUNT)-SUM(MD.RFAIL1)) ICOUNT,MAX(MD.IMONTH) IMONTH,MAX(MD.Y) Y,MAX(MD.IYMD) IYMD " + " FROM " + tablename + " MD " 
				+ leftjoinbindsql + " ");
		StringBuffer conditionSql = new StringBuffer();

		// 年份
		if(totalgreportvo.getY() != null && !"".equals(totalgreportvo.getY()))
		{
			if(!totalgreportvo.getY().contains(",")&&!totalgreportvo.getY().contains("a")){
				conditionSql.append(" AND MD.Y = " + totalgreportvo.getY() + " ");
			}else{
				conditionSql.append(" AND (");
				String[] yormontharry=totalgreportvo.getY().split("a");
				for (String yormonth : yormontharry)
				{
					String[] yandmonth=yormonth.split(",");
					if(yandmonth.length==2){
						conditionSql.append(" (MD.Y = " + yandmonth[0] + " ");
						if("null".equals(yandmonth[1])){
							conditionSql.append(" ) or ");
						}else{
							conditionSql.append(" AND MD.IMONTH= "+yandmonth[1]+" ) or ");
						}
					}
				}
				conditionSql.append(" 1=2 ) ");
			}
		}

		// 月份
		if(totalgreportvo.getImonth() != null && !"".equals(totalgreportvo.getImonth()))
		{
			conditionSql.append(" AND MD.IMONTH = " + totalgreportvo.getImonth() + " ");
		}

		// 企业编码
		conditionSql.append(muticontition);
//		if(totalgreportvo.getCorpCode() != null && !"".equals(totalgreportvo.getCorpCode()))
//		{
//			conditionSql.append(" AND LSDB.CORP_CODE='" + totalgreportvo.getCorpCode().trim() + "'");
//		}

		String groupby = "";
		if(totalgreportvo != null && totalgreportvo.getReporttype() != null && totalgreportvo.getReporttype() == 0)
		{
			groupby = " GROUP BY MD.Y,MD.IMONTH  HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";
		}
		else if(totalgreportvo != null && totalgreportvo.getReporttype() != null && totalgreportvo.getReporttype() == 1)
		{
			groupby = " GROUP BY MD.Y,MD.IMONTH,MD.IYMD  HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";
		}
		else if(totalgreportvo != null && totalgreportvo.getReporttype() != null && totalgreportvo.getReporttype() == 2)
		{
			groupby = " GROUP BY MD.Y  HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";
		}

		//sql 条件 and替换成where
		String wherecondsql=new GreportDAO().getConditionSql(conditionSql.toString());
		
		// 总的条件语句
		String sql = busreportsql.append(wherecondsql).append(groupby).toString();

		return sql;
	}


}
