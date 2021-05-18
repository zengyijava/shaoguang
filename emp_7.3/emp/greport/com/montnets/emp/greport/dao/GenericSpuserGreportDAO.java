package com.montnets.emp.greport.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.greport.vo.SpuserGreportVo;

/**
 * Sp账号对比报表
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-20 下午04:49:43
 * @description
 */
public class GenericSpuserGreportDAO extends SuperDAO
{

	/**
	 * Sp账号对比
	 * @param spuserGreportVo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findSpuserGreportsByVo(SpuserGreportVo spuserGreportVo) throws Exception
	{
		String sql = this.getSpuserGreportSql(spuserGreportVo);
		// 加上排序
		String orderby = "";
		if(spuserGreportVo!=null&&spuserGreportVo.getOrderby()!=null&&"1".equals(spuserGreportVo.getOrderby())){
			orderby = " ORDER BY SUM(MD.ICOUNT-MD.RFAIL1) DESC ";
		}else if(spuserGreportVo!=null&&spuserGreportVo.getOrderby()!=null&&"2".equals(spuserGreportVo.getOrderby())){
			orderby = " ORDER BY SUM(MD.ICOUNT-MD.RFAIL1) ASC ";
		}else if(spuserGreportVo!=null&&spuserGreportVo.getOrderby()!=null&&"3".equals(spuserGreportVo.getOrderby())){
			orderby = " ORDER BY MD.USERID ASC ";
		}
		String dataSql = sql + orderby;
		//List<SpuserGreportVo> returnList = findVoListBySQL(SpuserGreportVo.class, dataSql, StaticValue.EMP_POOLNAME);
		List<DynaBean> returnList= new DataAccessDriver().getGenericDAO().findDynaBeanBySql(dataSql);
		return returnList;
	}

	/**
	 * 获取Sp账号图形报表sql
	 * @param areagreportvo
	 * @return
	 */
	public String getSpuserGreportSql(SpuserGreportVo areagreportvo)
	{
		String tablename = "";
		String leftjoinbindsql = "";
		if(areagreportvo != null && areagreportvo.getMstype() != null && areagreportvo.getMstype() == 0)
		{
			tablename = "MT_DATAREPORT";
			leftjoinbindsql = " LEFT JOIN (SELECT DISTINCT CORP_CODE,SPUSER FROM LF_SP_DEP_BIND) LSDB ON LSDB.SPUSER=MD.USERID ";
		}
		else if(areagreportvo != null && areagreportvo.getMstype() != null && areagreportvo.getMstype() == 1)
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
		if(areagreportvo.getCorpCode()!= null&&!"".equals(areagreportvo.getCorpCode())){
			 muticontition=" AND LSDB.CORP_CODE='" + areagreportvo.getCorpCode().trim() + "' ";
		}else{
			leftjoinbindsql="";
		}
		
		StringBuffer busreportsql = new StringBuffer("SELECT (SUM(MD.ICOUNT)-SUM(MD.RFAIL1)) ICOUNT,MAX(MD.USERID) USERID " 
				+ " FROM " + tablename + " MD  " + leftjoinbindsql + " WHERE MD.Y = " + areagreportvo.getY() + " ");
		StringBuffer conditionSql = new StringBuffer();
		
		

//		// 年份
//		if(areagreportvo.getY() != null && !"".equals(areagreportvo.getY()))
//		{
//			conditionSql.append(" AND MD.Y = " + areagreportvo.getY() + " ");
//		}

		// 月份
		if(areagreportvo.getImonth() != null && !"".equals(areagreportvo.getImonth()))
		{
			conditionSql.append(" AND MD.IMONTH = " + areagreportvo.getImonth() + " ");
		}

		// 企业编码
		conditionSql.append(muticontition);
//		if(areagreportvo.getCorpCode() != null && !"".equals(areagreportvo.getCorpCode()))
//		{
//			conditionSql.append(" AND LSDB.CORP_CODE='" + areagreportvo.getCorpCode().trim() + "'");
//		}

		String groupby = " GROUP BY MD.USERID HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";

		// 总的条件语句
		String sql = busreportsql.append(conditionSql).append(groupby).toString();

		return sql;
	}


}
