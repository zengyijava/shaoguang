package com.montnets.emp.report.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.report.vo.BusNationtVo;
import com.montnets.emp.report.vo.BusReportVo;
import com.montnets.emp.util.PageInfo;
/**
 * 业务类型报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 下午07:21:21
 * @description
 */
public class GenericBusReportVoDAO extends SuperDAO {

	/**
	 * 业务类型报表
	 * @param busReportVo
	 * @return
	 * @throws Exception
	 */
	public List<BusReportVo> findBusReportsPageByVo(BusReportVo busReportVo,PageInfo pageInfo)throws Exception {
		
		String sql = this.getBusReportSql(busReportVo);
		String orderbysql=" ";
		//排序
		String reportType=busReportVo.getReportType();
		if(busReportVo.getDetailFlag()!=null&&"detail".equals(busReportVo.getDetailFlag())){
			if("0".equals(reportType)){
				orderbysql=" ORDER BY MD.IYMD DESC, BUS.BUS_NAME DESC";
			}else if("1".equals(reportType)){
				orderbysql=" ORDER BY MD.Y DESC,MD.IMONTH DESC, BUS.BUS_NAME DESC";
			}else if("2".equals(reportType)){//日报表类型
				orderbysql=" ORDER BY MD.IYMD DESC,  BUS.BUS_NAME DESC";
			}else{
				return null;
			}
		}else{
			if("0".equals(reportType)){
				orderbysql=" ORDER BY MD.Y DESC,MD.IMONTH DESC,BUS.BUS_NAME ";
			}else if("1".equals(reportType)){
				orderbysql=" ORDER BY MD.Y DESC, BUS.BUS_NAME  ";
			}else if("2".equals(reportType)){//日报表类型
				orderbysql =" ORDER BY BUS.BUS_NAME DESC ";
			}else{
				return null;
			}
			
		}
		//加上排序
		String dataSql = sql + orderbysql;
		//总条数语句
		String countSql = "select count(*) totalcount FROM (";
		countSql += sql;
		countSql += " ) A";
		List<BusReportVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(BusReportVo.class, dataSql, countSql, pageInfo,StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 * 业务类型报表(国家)
	 * @param busReportVo
	 * @return
	 * @throws Exception
	 */
	public List<BusNationtVo> findBusNationReportsPageByVo(BusReportVo busReportVo,PageInfo pageInfo)throws Exception {
		
		String sql = this.getBusNationReportSql(busReportVo);

		//加上排序
		String dataSql = sql + " order by BUS.BUS_NAME desc ";
		//总条数语句
		String countSql = "select count(*) totalcount FROM (";
		countSql += sql;
		countSql += " ) A";
		List<BusNationtVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(BusNationtVo.class, dataSql, countSql, pageInfo,StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	/**
	 * 业务类型报表
	 * @param busReportVo
	 * @return
	 * @throws Exception
	 */
	public List<BusReportVo> findBusReportsByVo(BusReportVo busReportVo)throws Exception {
		
		String sql = this.getBusReportSql(busReportVo);
		String orderbysql=" ";
		//排序
		String reportType=busReportVo.getReportType();
		if(busReportVo.getDetailFlag()!=null&&"detail".equals(busReportVo.getDetailFlag())){
			if("0".equals(reportType)){
				orderbysql=" ORDER BY MD.IYMD DESC, BUS.BUS_NAME DESC";
			}else if("1".equals(reportType)){
				orderbysql=" ORDER BY MD.Y DESC,MD.IMONTH DESC, BUS.BUS_NAME DESC";
			}else if("2".equals(reportType)){//日报表类型
				orderbysql=" ORDER BY MD.IYMD DESC,  BUS.BUS_NAME DESC";
			}else{
				return null;
			}
		}else{
			if("0".equals(reportType)){
				orderbysql=" ORDER BY MD.Y DESC,MD.IMONTH DESC,BUS.BUS_NAME ";
			}else if("1".equals(reportType)){
				orderbysql=" ORDER BY MD.Y DESC, BUS.BUS_NAME  ";
			}else if("2".equals(reportType)){//日报表类型
				orderbysql =" ORDER BY BUS.BUS_NAME DESC ";
			}else{
				return null;
			}
			
		}
		
		//加上排序
		String dataSql = sql + orderbysql;
		List<BusReportVo> returnList = findVoListBySQL(BusReportVo.class, dataSql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 * 业务类型报表(国家)
	 * @param busReportVo
	 * @return
	 * @throws Exception
	 */
	public List<BusNationtVo> findBusNationReportsByVo(BusReportVo busReportVo)throws Exception {
		
		String sql = this.getBusNationReportSql(busReportVo);
		//加上排序
		String dataSql = sql + " order by BUS.BUS_NAME desc ";
		List<BusNationtVo> returnList = findVoListBySQL(BusNationtVo.class, dataSql, StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	
	/**
	 * 获取短彩业务类型报表sql
	 * @param busReportVo
	 * @return
	 */
	public String getBusReportSql(BusReportVo busReportVo){
		
		String tablename="";
		String leftjoinbindsql="";
		if(busReportVo!=null&&busReportVo.getMstype()!=null&&busReportVo.getMstype()==0){
			tablename="MT_DATAREPORT";
			leftjoinbindsql=" LEFT JOIN (SELECT DISTINCT CORP_CODE,SPUSER FROM LF_SP_DEP_BIND) LSDB ON LSDB.SPUSER=MD.USERID ";
		}else if(busReportVo!=null&&busReportVo.getMstype()!=null&&busReportVo.getMstype()==1){
			tablename="MMS_DATAREPORT";
			leftjoinbindsql=" LEFT JOIN (SELECT DISTINCT CORP_CODE,MMS_USER FROM LF_MMSACCBIND) LSDB ON LSDB.MMS_USER =MD.USERID ";
		}else{
			return "";
		}
//		//多企业的条件
//		String muticontition="";
//		//企业编码
//		if(busReportVo.getCorpCode()!= null&&!"".equals(busReportVo.getCorpCode())){
//			 muticontition=" AND LSDB.CORP_CODE='"+busReportVo.getCorpCode().trim()+"' ";
//		}else{
//			leftjoinbindsql="";
//		}
		
		
		String groupbyMonthY ="";
		String fieldMonthY="";
		//如果是详细情况
		String reportType=busReportVo.getReportType();
		if(busReportVo.getDetailFlag()!=null&&"detail".equals(busReportVo.getDetailFlag())){
			if("0".equals(reportType)){
				fieldMonthY="MAX(MD.Y) Y,MAX(MD.IMONTH) IMONTH,MD.IYMD,";
				groupbyMonthY=" GROUP BY MD.IYMD, BUS.BUS_NAME ";
			}else if("1".equals(reportType)){
				fieldMonthY="MD.Y,MD.IMONTH,MAX(MD.IYMD) IYMD,";
				groupbyMonthY=" GROUP BY MD.Y,MD.IMONTH, BUS.BUS_NAME ";
			}else if("2".equals(reportType)){//日报表类型
				fieldMonthY="MAX(MD.Y) Y,MAX(MD.IMONTH) IMONTH,MD.IYMD,";
				groupbyMonthY=" GROUP BY MD.IYMD,  BUS.BUS_NAME ";
			}else{
				return "";
			}
		}else{
			if("0".equals(reportType)){
				fieldMonthY="MD.Y,MD.IMONTH,MAX(MD.IYMD) IYMD,";
				groupbyMonthY=" GROUP BY MD.Y,MD.IMONTH,BUS.BUS_NAME ";
			}else if("1".equals(reportType)){
				fieldMonthY="MD.Y,MAX(MD.IMONTH) IMONTH,MAX(MD.IYMD) IYMD,";
				groupbyMonthY=" GROUP BY MD.Y, BUS.BUS_NAME  ";
			}else if("2".equals(reportType)){//日报表类型
				fieldMonthY="MAX(MD.Y) Y,MAX(MD.IMONTH) IMONTH,MAX(MD.IYMD) IYMD,";
				groupbyMonthY =" GROUP BY BUS.BUS_NAME  ";
			}else{
				return "";
			}
			
		}
		
		StringBuffer busreportsql=new StringBuffer("");
		boolean iswhere =false;
		//多企业
		if(StaticValue.getCORPTYPE() ==1){
			busreportsql.append("SELECT "+fieldMonthY+"MAX(MD.SVRTYPE) SVRTYPE,MAX(MD.SPISUNCM) SPISUNCM  ,MAX(BUS.BUS_NAME) BUS_NAME,"+ sumFields()+
					"FROM "+tablename+" MD LEFT JOIN (SELECT BUS_CODE,BUS_NAME FROM LF_BUSMANAGER WHERE CORP_CODE IN ('0','"+busReportVo.getCorpCode().trim()+"')) BUS ON MD.SVRTYPE=BUS.BUS_CODE"+leftjoinbindsql+" WHERE LSDB.CORP_CODE='"+busReportVo.getCorpCode().trim()+"' ");
			iswhere=true;
		}else{
		//单企业
			busreportsql.append("SELECT "+fieldMonthY+"MAX(MD.SVRTYPE) SVRTYPE,MAX(MD.SPISUNCM) SPISUNCM  ,MAX(BUS.BUS_NAME) BUS_NAME,"+ sumFields()+
					"FROM "+tablename+" MD LEFT JOIN LF_BUSMANAGER BUS ON MD.SVRTYPE=BUS.BUS_CODE ");
		}

		StringBuffer conditionSql = new StringBuffer();

		//业务类型
		if (busReportVo.getBusCode() != null && !"".equals(busReportVo.getBusCode().trim())) {
			if(iswhere){
				if("-1".equals(busReportVo.getBusCode())){
					conditionSql.append(" AND BUS.BUS_NAME IS NULL ");
				}else{
					conditionSql.append(" AND MD.SVRTYPE='"+busReportVo.getBusCode().trim()+"' ");
				}
			}else{
				if("-1".equals(busReportVo.getBusCode())){
					conditionSql.append(" WHERE BUS.BUS_NAME IS NULL ");
				}else{
					conditionSql.append(" WHERE MD.SVRTYPE='"+busReportVo.getBusCode().trim()+"' ");
				}
				iswhere=true;
			}
		}
		
		//判断如果彩信而数据源又选择了接入类 则让其查不到数据
		if(busReportVo.getMstype()!=null&&busReportVo.getMstype()==1&&busReportVo.getDatasourcetype()!=null&&busReportVo.getDatasourcetype()==2){
			if(iswhere){
				conditionSql.append(" AND 1=2 ");
			}else{
				conditionSql.append(" WHERE 1=2 ");
				iswhere=true;
			}
		}
		
		
		//数据源
		if (busReportVo.getDatasourcetype() != null) {
			if(iswhere){
				if(busReportVo.getDatasourcetype()==1){
					conditionSql.append(" AND MD.SENDTYPE=1 ");
				}else if(busReportVo.getDatasourcetype()==2){
					conditionSql.append(" AND MD.SENDTYPE=2 ");
				}else if(busReportVo.getDatasourcetype()==3){
					conditionSql.append(" AND MD.SENDTYPE=3 ");
				}else if(busReportVo.getDatasourcetype()==4){
					conditionSql.append(" AND MD.SENDTYPE=4 ");
				}
			}else{
				if(busReportVo.getDatasourcetype()==1){
					conditionSql.append(" WHERE MD.SENDTYPE=1 ");
					iswhere=true;
				}else if(busReportVo.getDatasourcetype()==2){
					conditionSql.append(" WHERE MD.SENDTYPE=2 ");
					iswhere=true;
				}else if(busReportVo.getDatasourcetype()==3){
					conditionSql.append(" WHERE MD.SENDTYPE=3 ");
					iswhere=true;
				}else if(busReportVo.getDatasourcetype()==4){
					conditionSql.append(" WHERE MD.SENDTYPE=4 ");
					iswhere=true;
				}
				
			}
			
		}
		
		//企业编码
		//conditionSql.append(muticontition);
//		if(busReportVo.getCorpCode()!= null&&!"".equals(busReportVo.getCorpCode())){
//			conditionSql.append(" AND LSDB.CORP_CODE='"+busReportVo.getCorpCode().trim()+"'");
//		}
		if("2".equals(busReportVo.getReportType())){
			//开始时间
			if(busReportVo.getBegintime()!=null&&!"".equals(busReportVo.getBegintime())){
				String sendtime=busReportVo.getBegintime().replaceAll("-", "");
				if(iswhere){
					conditionSql.append(" AND MD.IYMD>="+ sendtime+ " ");
				}else{
					conditionSql.append(" WHERE MD.IYMD>="+ sendtime+ " ");
					iswhere=true;
				}
			}
			
			if(busReportVo.getEndtime()!=null&&!"".equals(busReportVo.getEndtime())){
				String endtime=busReportVo.getEndtime().replaceAll("-", "");
				if(iswhere){
					conditionSql.append(" AND MD.IYMD<="+ endtime+ " ");
				}else{
					conditionSql.append(" WHERE MD.IYMD<="+ endtime+ " ");
					iswhere=true;
				}
			}
			//月报表
		}else if("0".equals(busReportVo.getReportType())){
			if(busReportVo.getImonth()!=null&&!"".equals(busReportVo.getImonth())){
				if(iswhere){
					conditionSql.append(" AND MD.IMONTH="+ busReportVo.getImonth()+ " ");
				}else{
					conditionSql.append(" WHERE MD.IMONTH="+ busReportVo.getImonth()+ " ");
					iswhere=true;
				}
			}
			
			if(busReportVo.getY()!=null&&!"".equals(busReportVo.getY())){
				if(iswhere){
					conditionSql.append(" AND MD.Y="+ busReportVo.getY()+ " ");
				}else{
					conditionSql.append(" WHERE MD.Y="+ busReportVo.getY()+ " ");
					iswhere=true;
				}
			}
			
		//年报表
		}else if("1".equals(busReportVo.getReportType())){
			if(busReportVo.getY()!=null&&!"".equals(busReportVo.getY())){
				if(iswhere){
					conditionSql.append(" AND MD.Y="+ busReportVo.getY()+ " ");
				}else{
					conditionSql.append(" WHERE MD.Y="+ busReportVo.getY()+ " ");
					iswhere=true;
				}
			}
			
		}
		
		//增加运营商查询条件
		if (busReportVo.getSpisuncm() != null
				&& !"".equals(busReportVo.getSpisuncm())) {
			//如果是国内的
			if("100".equals(busReportVo.getSpisuncm())||"0".equals(busReportVo.getSpisuncm())||"1".equals(busReportVo.getSpisuncm())||"21".equals(busReportVo.getSpisuncm())){
				if(iswhere){
					conditionSql.append(" and MD.SPISUNCM in (0,1,21) ");
				}else{
					conditionSql.append(" WHERE MD.SPISUNCM in (0,1,21) ");
					iswhere=true;
				}
			}else if("5".equals(busReportVo.getSpisuncm())){//如果是国外类型
				if(iswhere){
					conditionSql.append(" and MD.SPISUNCM =5");
				}else{
					conditionSql.append(" WHERE MD.SPISUNCM =5");
					iswhere=true;
				}
			}
		}
		
		if((busReportVo.getBegintime()==null||"".equals(busReportVo.getBegintime()))&&(busReportVo.getEndtime()==null||"".equals(busReportVo.getEndtime()))){
			// 如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();
			if (null != yearAndMonth) {
				if(iswhere){
					conditionSql.append(" AND MD.Y="+ yearAndMonth[0] + " " + " AND MD.IMONTH="+ yearAndMonth[1] + " ");
				}else{
					conditionSql.append(" WHERE MD.Y="+ yearAndMonth[0] + " " + " AND MD.IMONTH="+ yearAndMonth[1] + " ");
					iswhere=true;
				}
			}
		}
		
		//如果是国际类型详细信息
//		if("nation".equals(busReportVo.getNationFlag())){
//			leftjoinbindsql=leftjoinbindsql+" left join XT_GATE_QUEUE gate on gate.SPGATE=MD.SPGATE left join A_AREACODE area on area.code=MD.AREACODE ";
//		}
		
		//总的条件语句
		String sql = busreportsql.append(conditionSql).append(groupbyMonthY).toString();
		
	    return sql;
	}
	
	
	
	/**
	 * 获取短彩业务类型报表sql(国家类型)
	 * @param busReportVo
	 * @return
	 */
	public String getBusNationReportSql(BusReportVo busReportVo){
		
		String tablename="";
		String leftjoinbindsql="";
		if(busReportVo!=null&&busReportVo.getMstype()!=null&&busReportVo.getMstype()==0){
			tablename="MT_DATAREPORT  MD";
			tablename=tablename+" left join (SELECT SPTYPE,USERID,ACCOUNTTYPE FROM USERDATA) u on  MD.USERID=u.userid and u.ACCOUNTTYPE=1 ";
			leftjoinbindsql=" LEFT JOIN (SELECT DISTINCT CORP_CODE,SPUSER FROM LF_SP_DEP_BIND) LSDB ON LSDB.SPUSER=MD.USERID ";
		}else if(busReportVo!=null&&busReportVo.getMstype()!=null&&busReportVo.getMstype()==1){
			tablename="MMS_DATAREPORT MD";
			tablename=tablename+" left join (SELECT SPTYPE,USERID,ACCOUNTTYPE FROM USERDATA) u on  MD.USERID=u.userid and u.ACCOUNTTYPE=2 ";
			leftjoinbindsql=" LEFT JOIN (SELECT DISTINCT CORP_CODE,MMS_USER FROM LF_MMSACCBIND) LSDB ON LSDB.MMS_USER =MD.USERID ";
		}else{
			return "";
		}
//		//多企业的条件
//		String muticontition="";
//		//企业编码
//		if(busReportVo.getCorpCode()!= null&&!"".equals(busReportVo.getCorpCode())){
//			 muticontition=" AND LSDB.CORP_CODE='"+busReportVo.getCorpCode().trim()+"' ";
//		}else{
//			leftjoinbindsql="";
//		}
		
		String groupbyMonthY ="";
		String fieldMonthY="";
		//如果是详细情况
		String reportType=busReportVo.getReportType();//由于国际详情，不需要按照日期分类
		if(busReportVo.getNationFlag()!=null&&"nation".equals(busReportVo.getNationFlag())){
			if("0".equals(reportType)){
				fieldMonthY="MAX(MD.Y) Y,MAX(MD.IMONTH) IMONTH,MAX(MD.IYMD) IYMD,u.SPTYPE sptype,gate.SPGATE spgatecode,gate.GATENAME spgatename,area.AREACODE nationcode,area.AREANAME nationname,";
				groupbyMonthY=" GROUP BY MD.SPISUNCM,u.SPTYPE,gate.GATENAME,MD.SENDTYPE,gate.SPGATE,area.AREACODE,area.AREANAME,BUS.BUS_NAME ";
			}else if("1".equals(reportType)){
				fieldMonthY="MAX(MD.Y) Y,MAX(MD.IMONTH) IMONTH,MAX(MD.IYMD) IYMD,u.SPTYPE sptype,gate.SPGATE spgatecode,gate.GATENAME spgatename,area.AREACODE nationcode,area.AREANAME nationname,";
				groupbyMonthY=" GROUP BY u.SPTYPE,MD.SPISUNCM,MD.SENDTYPE,gate.GATENAME,gate.SPGATE,area.AREACODE,area.AREANAME,BUS.BUS_NAME ";
			}else if("2".equals(reportType)){//日报表类型
				fieldMonthY="MAX(MD.Y) Y,MAX(MD.IMONTH) IMONTH,u.SPTYPE sptype,MAX(MD.IYMD) IYMD ,gate.SPGATE spgatecode,gate.GATENAME spgatename,area.AREACODE nationcode,area.AREANAME nationname,";
				groupbyMonthY=" GROUP BY MD.SPISUNCM,MD.SENDTYPE,gate.GATENAME,u.SPTYPE,gate.SPGATE,area.AREACODE,area.AREANAME, BUS.BUS_NAME ";
			}else{
				return "";
			}
		}
		
		StringBuffer busreportsql=new StringBuffer("");
		boolean iswhere=false;
		//多企业
		if(StaticValue.getCORPTYPE() ==1){
			busreportsql.append("SELECT "+fieldMonthY+"MAX(MD.SVRTYPE) SVRTYPE,MD.SPISUNCM ,MD.SENDTYPE,MAX(BUS.BUS_NAME) BUS_NAME," + sumFields() +
			"FROM "+tablename+"  LEFT JOIN (SELECT BUS_CODE,BUS_NAME FROM LF_BUSMANAGER WHERE CORP_CODE IN ('0','"+busReportVo.getCorpCode().trim()+"')) BUS ON MD.SVRTYPE=BUS.BUS_CODE "+leftjoinbindsql+" left join XT_GATE_QUEUE gate on gate.SPGATE=MD.SPGATE left join A_AREACODE area on area.code=MD.AREACODE WHERE LSDB.CORP_CODE='"+busReportVo.getCorpCode().trim()+"' ");
			iswhere=true;
		}else{
			busreportsql.append("SELECT "+fieldMonthY+"MAX(MD.SVRTYPE) SVRTYPE,MD.SPISUNCM ,MD.SENDTYPE,MAX(BUS.BUS_NAME) BUS_NAME," + sumFields() +
			"FROM "+tablename+"  LEFT JOIN LF_BUSMANAGER BUS ON MD.SVRTYPE=BUS.BUS_CODE left join XT_GATE_QUEUE gate on gate.SPGATE=MD.SPGATE left join A_AREACODE area on area.code=MD.AREACODE ");
		}
		

		StringBuffer conditionSql = new StringBuffer();

		//业务类型
		if (busReportVo.getBusCode() != null && !"".equals(busReportVo.getBusCode())) {
			if(iswhere){
				if("-1".equals(busReportVo.getBusCode())){
					conditionSql.append(" AND BUS.BUS_NAME IS NULL ");
				}else{
					conditionSql.append(" AND MD.SVRTYPE='"+busReportVo.getBusCode().trim()+"' ");
				}
			}else{
				if("-1".equals(busReportVo.getBusCode())){
					conditionSql.append(" WHERE BUS.BUS_NAME IS NULL ");
				}else{
					conditionSql.append(" WHERE MD.SVRTYPE='"+busReportVo.getBusCode().trim()+"' ");
				}
				iswhere=true;
			}
		}
		
		//判断如果彩信而数据源又选择了接入类 则让其查不到数据
		if(busReportVo.getMstype()!=null&&busReportVo.getMstype()==1&&busReportVo.getDatasourcetype()!=null&&busReportVo.getDatasourcetype()==2){
			if(iswhere){
				conditionSql.append(" AND 1=2 ");
			}else{
				conditionSql.append(" WHERE 1=2 ");
				iswhere=true;
			}
		}
		
		
		//数据源
		if (busReportVo.getDatasourcetype() != null) {
			if(iswhere){
				if(busReportVo.getDatasourcetype()==1){
					conditionSql.append(" AND MD.SENDTYPE=1 ");
				}else if(busReportVo.getDatasourcetype()==2){
					conditionSql.append(" AND MD.SENDTYPE=2 ");
				}else if(busReportVo.getDatasourcetype()==3){
					conditionSql.append(" AND MD.SENDTYPE=3 ");
				}else if(busReportVo.getDatasourcetype()==4){
					conditionSql.append(" AND MD.SENDTYPE=4 ");
				}
			}else{
				if(busReportVo.getDatasourcetype()==1){
					conditionSql.append(" WHERE MD.SENDTYPE=1 ");
					iswhere=true;
				}else if(busReportVo.getDatasourcetype()==2){
					conditionSql.append(" WHERE MD.SENDTYPE=2 ");
					iswhere=true;
				}else if(busReportVo.getDatasourcetype()==3){
					conditionSql.append(" WHERE MD.SENDTYPE=3 ");
					iswhere=true;
				}else if(busReportVo.getDatasourcetype()==4){
					conditionSql.append(" WHERE MD.SENDTYPE=4 ");
					iswhere=true;
				}
			}
			
		}
		
		//企业编码
		//conditionSql.append(muticontition);

		
		if("2".equals(busReportVo.getReportType())){
		//开始时间
		if(busReportVo.getBegintime()!=null&&!"".equals(busReportVo.getBegintime())){
			String sendtime=busReportVo.getBegintime().replaceAll("-", "");
			if(iswhere){
				conditionSql.append(" AND MD.IYMD>="+ sendtime+ " ");
			}else{
				conditionSql.append(" WHERE MD.IYMD>="+ sendtime+ " ");
				iswhere=true;
			}
			
		}
		
		
		if(busReportVo.getEndtime()!=null&&!"".equals(busReportVo.getEndtime())){
			String endtime=busReportVo.getEndtime().replaceAll("-", "");
			if(iswhere){
				conditionSql.append(" AND MD.IYMD<="+ endtime+ " ");
			}else{
				conditionSql.append(" WHERE MD.IYMD<="+ endtime+ " ");
				iswhere=true;
			}
		}
			//月报表
		}else if("0".equals(busReportVo.getReportType())){
			if(busReportVo.getImonth()!=null&&!"".equals(busReportVo.getImonth())){
				if(iswhere){
					conditionSql.append(" AND MD.IMONTH="+ busReportVo.getImonth()+ " ");
				}else{
					conditionSql.append(" WHERE MD.IMONTH="+ busReportVo.getImonth()+ " ");
					iswhere=true;
				}
			}
			
			if(busReportVo.getY()!=null&&!"".equals(busReportVo.getY())){
				if(iswhere){
					conditionSql.append(" AND MD.Y="+ busReportVo.getY()+ " ");
				}else{
					conditionSql.append(" WHERE MD.Y="+ busReportVo.getY()+ " ");
					iswhere=true;
				}
			}
			
		//年报表
		}else if("1".equals(busReportVo.getReportType())){
			if(busReportVo.getY()!=null&&!"".equals(busReportVo.getY())){
				if(iswhere){
					conditionSql.append(" AND MD.Y="+ busReportVo.getY()+ " ");
				}else{
					conditionSql.append(" WHERE MD.Y="+ busReportVo.getY()+ " ");
					iswhere=true;
				}
			}
		}
		
		//增加运营商查询条件
		if (busReportVo.getSpisuncm() != null
				&& !"".equals(busReportVo.getSpisuncm())) {
			//如果是国内的
			if("100".equals(busReportVo.getSpisuncm())||"0".equals(busReportVo.getSpisuncm())||"1".equals(busReportVo.getSpisuncm())||"21".equals(busReportVo.getSpisuncm())){
				if(iswhere){
					conditionSql.append(" and MD.SPISUNCM in (0,1,21) ");//刚进入页面
				}else{
					conditionSql.append(" WHERE MD.SPISUNCM in (0,1,21) ");//刚进入页面
					iswhere=true;
				}
			}else if("5".equals(busReportVo.getSpisuncm())){//如果是国外类型
				if(iswhere){
					conditionSql.append(" and MD.SPISUNCM =5");
				}else{
					conditionSql.append(" WHERE MD.SPISUNCM =5");
					iswhere=true;
				}
				
			}
		}
		
		//当查询各国发送时候
		if (busReportVo.getNationcode() != null
				&& !"".equals(busReportVo.getNationcode())) {
			if(iswhere){
				conditionSql.append(" and area.AREACODE like '%"+busReportVo.getNationcode()+"%'" );
			}else{
				conditionSql.append(" WHERE area.AREACODE like '%"+busReportVo.getNationcode()+"%'" );
				iswhere=true;
			}
		}
		if (busReportVo.getNationname() != null
				&& !"".equals(busReportVo.getNationname())) {
			if(iswhere){
				conditionSql.append(" and area.AREANAME  like '%"+busReportVo.getNationname()+"%' ");
			}else{
				conditionSql.append(" WHERE area.AREANAME  like '%"+busReportVo.getNationname()+"%' ");
				iswhere=true;
			}
			
		}
		
		if((busReportVo.getBegintime()==null||"".equals(busReportVo.getBegintime()))&&(busReportVo.getEndtime()==null||"".equals(busReportVo.getEndtime()))){
			// 如果没有时间查询条件，则默认查询当前年月份的
			String yearAndMonth[] = this.getYearAndMonth();
			if (null != yearAndMonth) {
				if(iswhere){
					conditionSql.append(" AND MD.Y="+ yearAndMonth[0] + " " + " AND MD.IMONTH="+ yearAndMonth[1] + " ");
				}else{
					conditionSql.append(" WHERE MD.Y="+ yearAndMonth[0] + " " + " AND MD.IMONTH="+ yearAndMonth[1] + " ");
					iswhere=true;
				}
			}
		}
		
		
		//总的条件语句
		String sql = busreportsql.append(conditionSql).append(groupbyMonthY).toString();
		
	    return sql;
	}
	
	
	/**
	 * 获取当前的年份和月份的数组，用于限制查询当前月份的业务类型报表
	 * @return
	 */
	public String[] getYearAndMonth() {
		//时间和月份的数组
		String[] datetime = new String[2];

		try {
			//定义一个系统时间对象
			Calendar cal = Calendar.getInstance();
			//获取月份
			Integer month = cal.get(Calendar.MONTH);
			month += 1;
			datetime[0] = String.valueOf(cal.get(Calendar.YEAR));
			datetime[1] = String.valueOf(month);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务类型报表获取当前年份和月份数组异常");
		}

		return datetime;
	}


    /**
     * 统计字段
     * @return
     */
	public String sumFields()
    {
        return " SUM(ICOUNT) ICOUNT,SUM(RSUCC) RSUCC,SUM(RFAIL1) RFAIL1,SUM(RFAIL2) RFAIL2,SUM(RNRET) RNRET ";
    }


    public int sumFieldsCount()
    {
        return sumFields().split(",").length;
    }
    /**
     * 统计字段返回数组
     * @return
     */
    public long[] sumFieldsArrays(ResultSet rs) throws SQLException {
        int count = sumFieldsCount();
        long[] arrays = new long[count];
        for(int i = 0;i<arrays.length;i++)
        {
            arrays[i] = rs.getLong(i+1);
        }
        return arrays;
    }


	/**
	 *业务类型报表 合计
	 * @param busReportVo
	 * @return
	 * @throws Exception
	 */
	public long[] findSumCount(BusReportVo busReportVo) throws Exception {
        int count = sumFieldsCount();
		long[] arrays = new long[count];
		//总sql
		String sql="";
		if("nation".equals(busReportVo.getNationFlag())){			
			sql = "SELECT "+sumFields()+" FROM ("+this.getBusNationReportSql(busReportVo)+") A";
		}else{
			sql = "SELECT "+sumFields()+" FROM ("+this.getBusReportSql(busReportVo)+") A";
		}
		
		//定义连接
		Connection conn = null;
		PreparedStatement ps = null;
		java.sql.ResultSet rs = null;
		try {
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//获取操作类
			ps = conn.prepareStatement(sql);
			//获取结果集
			rs = ps.executeQuery();
			//遍历
			if (rs.next()) {
                arrays = sumFieldsArrays(rs);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务类型报表 合计异常");
			throw e;
		} finally {
			//关闭连接
			close(rs, ps, conn);
		}

		return arrays;
	}

}
