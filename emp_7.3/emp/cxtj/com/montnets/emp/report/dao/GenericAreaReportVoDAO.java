package com.montnets.emp.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.report.vo.AreaReportVo;
import com.montnets.emp.util.PageInfo;

/**
 * 区域统计报表
 * @project sinolife
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-17 下午02:40:18
 * @description
 */
public class GenericAreaReportVoDAO extends SuperDAO {

	/**
	 * 区域统计报表 月/年 报表 无分页
	 * @param arearptVo
	 * @return
	 * @throws Exception
	 */
	public List<AreaReportVo> findAreaReportsByVo(AreaReportVo arearptVo)throws Exception {

		String sql = this.getAreaReportSql(arearptVo);
		String orderbyMonthY="";
		if(arearptVo!=null&&arearptVo.getReporttype()!=null){
			int reportType = arearptVo.getReporttype();
			if(arearptVo.isDes()){
				if(reportType==0){
					orderbyMonthY=" ORDER BY MD.IYMD DESC ";
				}else if(reportType==1){
					orderbyMonthY=" ORDER BY MD.IMONTH DESC ";
				}else if(reportType==2){
					orderbyMonthY=" ORDER BY MD.IYMD DESC ";
				}
			}
		}

		//加上排序
		String dataSql = sql + orderbyMonthY;

		//返回集合
		List<AreaReportVo> returnList = findVoListBySQL(AreaReportVo.class, dataSql, StaticValue.EMP_POOLNAME);
		return returnList;
	}


	/**
	 * 区域统计报表   月/年 报表 分页
	 * @param arearptvo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<AreaReportVo> findAreaReportsByVo(AreaReportVo arearptvo, PageInfo pageInfo) throws Exception {
		String sql = this.getAreaReportSql(arearptvo);
		String orderbyMonthY="";
		if(arearptvo!=null&&arearptvo.getReporttype()!=null){
			int reportType = arearptvo.getReporttype();
			if(arearptvo.isDes()){
				if(reportType==0){
					orderbyMonthY=" ORDER BY MD.IYMD DESC ";
				}else if(reportType==1){
					orderbyMonthY=" ORDER BY MD.IMONTH DESC ";
				}else if(reportType==2){
					orderbyMonthY=" ORDER BY MD.IYMD DESC ";
				}
			}
		}


		//加上排序
		String dataSql = sql + orderbyMonthY;
		//总条数语句
		String countSql = "select count(*) totalcount FROM (";
		countSql += sql;
		countSql += " ) A";
		//查询结果集
		List<AreaReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				AreaReportVo.class, dataSql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}



	/**
	 * 获取区域统计报表sql语句
	 * @param arearptvo
	 * @return
	 */
	public String getAreaReportSql(AreaReportVo arearptvo){
		String tablename="";
		String leftjoinbindsql="";
		if(arearptvo!=null&&arearptvo.getMstype()!=null&&arearptvo.getMstype()==0){
			tablename="MT_DATAREPORT";
			leftjoinbindsql=" LEFT JOIN (SELECT DISTINCT CORP_CODE,SPUSER FROM LF_SP_DEP_BIND) LSDB ON LSDB.SPUSER=MD.USERID ";
		}else if(arearptvo!=null&&arearptvo.getMstype()!=null&&arearptvo.getMstype()==1){
			tablename="MMS_DATAREPORT";
			leftjoinbindsql=" LEFT JOIN (SELECT DISTINCT CORP_CODE,MMS_USER FROM LF_MMSACCBIND) LSDB ON LSDB.MMS_USER =MD.USERID ";
		}else{
			return "";
		}

		if(StaticValue.getCORPTYPE() ==0){
			leftjoinbindsql="";
		}

		String fieldMonthY="";
		String groupbyMonthY="";
		if(arearptvo==null||arearptvo.getReporttype()==null){
			return "";
		}
		int reportType = arearptvo.getReporttype();
		boolean isDes = arearptvo.isDes();
		if(!isDes){
			if(reportType==0){
				fieldMonthY="MD.Y,MD.IMONTH,MAX(MD.IYMD) IYMD,";
				groupbyMonthY=" GROUP BY MD.Y,MD.IMONTH,APCITY.PROVINCE HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";
			}else if(reportType==1){
				arearptvo.setImonth(null);
				fieldMonthY="MD.Y,MAX(MD.IMONTH) IMONTH,MAX(MD.IYMD) IYMD,";
				groupbyMonthY=" GROUP BY MD.Y,APCITY.PROVINCE  HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";
			}else if(reportType==2){//日报表类型
				arearptvo.setImonth(null);arearptvo.setY(null);
				fieldMonthY="MAX(MD.Y) Y,MAX(MD.IMONTH) IMONTH,MAX(MD.IYMD) IYMD,";
				groupbyMonthY=" GROUP BY APCITY.PROVINCE  HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";
			}else{
				return "";
			}
		}else{//详情查看
			if(reportType==0){
				fieldMonthY="MAX(MD.Y) Y,MAX(MD.IMONTH) IMONTH,MD.IYMD,";
				groupbyMonthY=" GROUP BY MD.IYMD,APCITY.PROVINCE HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";
			}else if(reportType==1){
				arearptvo.setImonth(null);
				fieldMonthY="MD.Y,MD.IMONTH,MAX(MD.IYMD) IYMD,";
				groupbyMonthY=" GROUP BY MD.Y,MD.IMONTH,APCITY.PROVINCE  HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";
			}else if(reportType==2){//日报表类型
				arearptvo.setImonth(null);arearptvo.setY(null);
				fieldMonthY="MAX(MD.Y) Y,MAX(MD.IMONTH) IMONTH,MD.IYMD,";
				groupbyMonthY=" GROUP BY MD.IYMD, APCITY.PROVINCE  HAVING SUM(MD.ICOUNT)-SUM(MD.RFAIL1)>0 ";
			}else{
				return "";
			}
		}


		StringBuffer busreportsql=new StringBuffer("");
		boolean iswhere =false;
		if(StaticValue.getCORPTYPE() ==1){
			busreportsql.append("SELECT "+fieldMonthY+"MAX(APCITY.PROVINCE) PROVINCE,SUM(ICOUNT) ICOUNT,SUM(RSUCC) RSUCC,SUM(RFAIL1) RFAIL1,SUM(RFAIL2) RFAIL2,SUM(RNRET) RNRET " +
					"FROM "+tablename+" MD LEFT JOIN (SELECT DISTINCT AREACODE,PROVINCE FROM A_PROVINCECITY) APCITY ON MD.MOBILEAREA=APCITY.AREACODE "+leftjoinbindsql
					+" WHERE LSDB.CORP_CODE='"+arearptvo.getCorpcode().trim()+"' ");
			iswhere=true;
		}else{
			busreportsql.append("SELECT "+fieldMonthY+"MAX(APCITY.PROVINCE) PROVINCE,SUM(ICOUNT) ICOUNT,SUM(RSUCC) RSUCC,SUM(RFAIL1) RFAIL1,SUM(RFAIL2) RFAIL2,SUM(RNRET) RNRET " +
					"FROM "+tablename+" MD LEFT JOIN (SELECT DISTINCT AREACODE,PROVINCE FROM A_PROVINCECITY) APCITY ON MD.MOBILEAREA=APCITY.AREACODE "+leftjoinbindsql+" ");
		}

		StringBuffer conditionSql = new StringBuffer();

		//区域
		if (arearptvo.getProvince() != null	&& !"".equals(arearptvo.getProvince())) {
			if(iswhere){
				if(!"未知".equals(arearptvo.getProvince())){
					conditionSql.append(" AND APCITY.PROVINCE like '%"+arearptvo.getProvince().trim()+"%' ");
				}else{
					conditionSql.append(" AND APCITY.PROVINCE IS NULL ");
				}
			}else{
				if(!"未知".equals(arearptvo.getProvince())){
					conditionSql.append(" WHERE APCITY.PROVINCE like '%"+arearptvo.getProvince().trim()+"%' ");
				}else{
					conditionSql.append(" WHERE APCITY.PROVINCE IS NULL ");
				}
				iswhere=true;
			}

		}

		// 年份
		if(arearptvo.getY() != null && !"".equals(arearptvo.getY()))
		{
			if(iswhere){
				conditionSql.append(" AND MD.Y = "+arearptvo.getY()+" ");
			}else{
				conditionSql.append(" WHERE MD.Y = "+arearptvo.getY()+" ");
				iswhere=true;
			}
		}

		// 月份
		if(arearptvo.getImonth() != null && !"".equals(arearptvo.getImonth()))
		{
			if(iswhere){
				conditionSql.append(" AND MD.IMONTH = "+arearptvo.getImonth()+" ");
			}else{
				conditionSql.append(" WHERE MD.IMONTH = "+arearptvo.getImonth()+" ");
				iswhere=true;
			}

		}

//		//企业编码
//		if(arearptvo.getCorpcode()!= null&&!"".equals(arearptvo.getCorpcode())){
//			conditionSql.append(" AND LSDB.CORP_CODE='"+arearptvo.getCorpcode().trim()+"'");
//		}

		if(reportType==2){//日报表类型
			String startDate = arearptvo.getStartdate();
			String endDate = arearptvo.getEnddate();
			if(iswhere){
				if(startDate!=null&&startDate.length()>1){
					startDate = startDate.replaceAll("-", "");
					conditionSql.append(" AND MD.IYMD >= "+startDate+" ");
				}
			}else{
				if(startDate!=null&&startDate.length()>1){
					startDate = startDate.replaceAll("-", "");
					conditionSql.append(" WHERE MD.IYMD >= "+startDate+" ");
				}
				iswhere=true;
			}
			if(iswhere){
				if(endDate!=null&&endDate.length()>1){
					endDate = endDate.replaceAll("-", "");
					conditionSql.append(" AND MD.IYMD <= "+endDate+" ");
				}
			}else{
				if(endDate!=null&&endDate.length()>1){
					endDate = endDate.replaceAll("-", "");
					conditionSql.append(" WHERE MD.IYMD <= "+endDate+" ");
				}
				iswhere=true;
			}
		}


		//总的条件语句
		String sql = busreportsql.append(conditionSql).append(groupbyMonthY).toString();

		return sql;
	}




	/**
	 * 查询合计
	 * @param arearptvo
	 * @param corpcode
	 * @return
	 * @throws Exception
	 */
	public long[] findSumCount(AreaReportVo arearptvo) throws Exception {
		long icount=0L;
		long rsucc=0L;
		long rfail1=0L;
		long rfail2=0L;
		long rnret=0L;
		//总sql
		String sql = "SELECT SUM(ICOUNT) ICOUNT,SUM(RSUCC) RSUCC,SUM(RFAIL1) RFAIL1,SUM(RFAIL2) RFAIL2,SUM(RNRET) RNRET  FROM ("+this.getAreaReportSql(arearptvo)+") A";

		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try {
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//获取操作对象
			ps = conn.prepareStatement(sql);
			//获取结果集
			rs = ps.executeQuery();
			//判断是否有值
			if (rs.next()) {
				icount = rs.getLong("ICOUNT");
				rsucc = rs.getLong("RSUCC");
				rfail1 = rs.getLong("RFAIL1");
				rfail2 = rs.getLong("RFAIL2");
				rnret = rs.getLong("RNRET");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询合计异常");
			throw e;
		} finally {
			//关闭连接
			close(rs, ps, conn);
		}
		long[] returnLong = new long[5];
		returnLong[0] = icount;
		returnLong[1] = rsucc;
		returnLong[2] = rfail1;
		returnLong[3] = rfail2;
		returnLong[4] = rnret;
		return returnLong;
	}

}
