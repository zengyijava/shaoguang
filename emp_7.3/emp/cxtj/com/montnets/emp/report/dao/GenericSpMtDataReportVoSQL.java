package com.montnets.emp.report.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.inbox.dao.ReciveBoxDao;
import com.montnets.emp.report.vo.SpMtDataReportVo;
import org.apache.commons.lang.StringUtils;

/**
 * Sp账号统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:43:00
 * @description
 */
public class GenericSpMtDataReportVoSQL {
	

	/**
	 * 以下是月报表用到的方法
	 * @return
	 */
	public static String getMonthFieldSql() {
		String sql =new StringBuffer("select "+ReportDAO.getPublicCountSql("mtdatareport")+",max(mtdatareport.Y) Y,max(mtdatareport.IMONTH) IMONTH,")
		.append("max(mtdatareport.USERID) USERID,MAX(u.STAFFNAME) STAFFNAME,MAX(u.SPTYPE) SPTYPE,MAX(mtdatareport.SENDTYPE) SENDTYPE,MAX(mtdatareport.SPISUNCM) SPISUNCM ").toString();
		return sql;
	}
	
	/**
	 * 以下是月报表用到的方法--国家
	 * @return
	 */
	public static String getMonthFieldNationSql() {
		String sql =new StringBuffer("select "+ReportDAO.getPublicCountSql("mtdatareport")+",max(mtdatareport.Y) Y,max(mtdatareport.IMONTH) IMONTH,")
		.append("max(mtdatareport.USERID) USERID,MAX(u.STAFFNAME) STAFFNAME,MAX(u.SPTYPE) SPTYPE,MAX(mtdatareport.SENDTYPE) SENDTYPE,MAX(mtdatareport.SPISUNCM) SPISUNCM ,gate.SPGATE spgatecode,gate.GATENAME spgatename,area.AREACODE nationcode,area.AREANAME nationname ").toString();
		return sql;
	}
	

	/**
	 * 月报表获取查询表格
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getMonthTableSql(SpMtDataReportVo operatorsMtDataReportVo) { //change by denglj
		
		String conttypestr="";
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getSptypes()!=null
				&&!"".equals(operatorsMtDataReportVo.getSptypes())){
			conttypestr=" AND u.SPTYPE IN ("+operatorsMtDataReportVo.getSptypes()+")";
		}
		
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getStaffname()!=null
				&&!"".equals(operatorsMtDataReportVo.getStaffname())){
            //conttypestr=" AND mtdatareport.USERID='"+operatorsMtDataReportVo.getStaffname().trim()+"' ";
            conttypestr = " AND u.STAFFNAME like '%" + operatorsMtDataReportVo.getStaffname().trim() + "%' ";
		}
		
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		
		String sql= " from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM FROM MT_DATAREPORT " +
				getMonthConditionSql(operatorsMtDataReportVo, false) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA "
				+ ") u ON mtdatareport.USERID=u.USERID AND U.ACCOUNTTYPE=1 ";
		if(operatorsMtDataReportVo.getCorpCode()!= null){
			if("100000".equals(operatorsMtDataReportVo.getCorpCode())){
				sql = " from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM FROM MT_DATAREPORT " +
						getMonthConditionSql(operatorsMtDataReportVo, false) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 ";
			}else{
				String spUsers = operatorsMtDataReportVo.getSpUsers()== null?"''":operatorsMtDataReportVo.getSpUsers();
				//如果是db2 oracle 则大写小写都加一份 兼容小写
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
						||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) )
				{
					spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
				}
				//sql in查询拆分
				if(StringUtils.isNotEmpty(operatorsMtDataReportVo.getSpUserId())){
					//spUsers = spUsers+","+operatorsMtDataReportVo.getSpUserId();
					spUsers = operatorsMtDataReportVo.getSpUserId();
				}
				String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
                //String tempCondition = " AND 1 = 1 ";
                if("2".equals(operatorsMtDataReportVo.getCurrentUserId())){
                    operatorsMtDataReportVo.setPriviligeUserCode("");
                }
				return " from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MT_DATAREPORT where (" + insqlstr + ") " +
						getMonthConditionSql(operatorsMtDataReportVo, true) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 " + conttypestr;

/*				return " from (select report.ICOUNT,RSUCC,RFAIL1,report.RFAIL2,report.RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,report.TASKID from MT_DATAREPORT report" +
						" LEFT JOIN LF_MTTASK task ON report.TASKID = task.TASKID  where (" + insqlstr + ") " +tempCondition+
						getDayConditionSql(operatorsMtDataReportVo, true) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 "+ conttypestr;*/
			}
		}
		sql+=conttypestr;
		return sql;
	}
	

	/**
	 * 月报表获取查询表格---国家
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getMonthTableNationSql(SpMtDataReportVo operatorsMtDataReportVo) { //change by denglj
		
		String conttypestr="";
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getSptypes()!=null
				&&!"".equals(operatorsMtDataReportVo.getSptypes())){
			conttypestr=" AND u.SPTYPE IN ("+operatorsMtDataReportVo.getSptypes()+")";
		}
		
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getStaffname()!=null
				&&!"".equals(operatorsMtDataReportVo.getStaffname())){
            //conttypestr=" AND mtdatareport.USERID= '"+operatorsMtDataReportVo.getStaffname().trim()+"' ";
            conttypestr = " AND u.USERID like '%" + operatorsMtDataReportVo.getStaffname().trim() + "%' ";
		}
		
		//当查询各国发送时候
		if (operatorsMtDataReportVo.getNationcode() != null
				&& !"".equals(operatorsMtDataReportVo.getNationcode())) {
			conttypestr=conttypestr+" and area.AREACODE like '%"+operatorsMtDataReportVo.getNationcode()+"%' ";
		}
		if (operatorsMtDataReportVo.getNationname() != null
				&& !"".equals(operatorsMtDataReportVo.getNationname())) {
			conttypestr=conttypestr+" and area.AREANAME  like '%"+operatorsMtDataReportVo.getNationname()+"%' ";
			
		}
		
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		
		String sql=new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,AREACODE,SPGATE  FROM MT_DATAREPORT ")
		.append(getMonthConditionSql(operatorsMtDataReportVo,false)).append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u ON mtdatareport.USERID=u.USERID AND U.ACCOUNTTYPE=1 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=1 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
		if(operatorsMtDataReportVo.getCorpCode()!= null){
			if("100000".equals(operatorsMtDataReportVo.getCorpCode())){
				sql = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,AREACODE,SPGATE  FROM MT_DATAREPORT ")
						.append(getMonthConditionSql(operatorsMtDataReportVo,false)).append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=1 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
			}else{
				String spUsers = operatorsMtDataReportVo.getSpUsers()== null?"''":operatorsMtDataReportVo.getSpUsers();
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
						||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) )
				{
					spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
				}
				//sql in查询拆分
				String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
				String SQL = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,AREACODE,SPGATE  from MT_DATAREPORT where ("+insqlstr+") ")
					.append(getMonthConditionSql(operatorsMtDataReportVo,true)).append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=1 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
				SQL+=conttypestr;
				return SQL;
			}
		}
		sql+=conttypestr;
		return sql;
	}
	
	
	/**
	 * 彩信月报表获取查询表格
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getMonthMMSTableSql(SpMtDataReportVo operatorsMtDataReportVo) { //change by denglj
		
		String conttypestr="";
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getSptypes()!=null){
			conttypestr=" AND u.SPTYPE IN ("+operatorsMtDataReportVo.getSptypes()+")";
		}
		
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getStaffname()!=null
				&&!"".equals(operatorsMtDataReportVo.getStaffname())){
            //conttypestr=" AND mtdatareport.USERID='"+operatorsMtDataReportVo.getStaffname().trim()+"' ";
            conttypestr = " AND u.STAFFNAME like '%" + operatorsMtDataReportVo.getStaffname().trim() + "%' ";
		}
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		
		String sql= " from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT " + getMonthConditionSql(operatorsMtDataReportVo, false) +
				") mtdatareport left join userdata u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 ";
		if(operatorsMtDataReportVo.getCorpCode()!= null){
			if("100000".equals(operatorsMtDataReportVo.getCorpCode())){
				sql = " from  (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT " + getMonthConditionSql(operatorsMtDataReportVo, false) +
						") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 ";
				sql+=conttypestr;
				return sql;
			}else{
				String spUsers = operatorsMtDataReportVo.getSpUsers()== null?"''":operatorsMtDataReportVo.getSpUsers();
				//如果是db2 oracle 数据库则兼容小写sp账号
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
						||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) )
				{
					spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
				}
				//sql in查询拆分
				String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
                //String tempCondition = " AND 1 = 1 ";
                if("2".equals(operatorsMtDataReportVo.getCurrentUserId())){
                    operatorsMtDataReportVo.setPriviligeUserCode("");
                }
				return " from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT where (" + insqlstr + ") " + getMonthConditionSql(operatorsMtDataReportVo, true) +
						") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 " + conttypestr;
/*				return " from (select report.ICOUNT,RSUCC,RFAIL1,report.RFAIL2,report.RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,report.TASKID from MMS_DATAREPORT report" +
						" LEFT JOIN LF_MTTASK task ON report.TASKID = task.TASKID  where (" + insqlstr + ") " +tempCondition+
						getDayConditionSql(operatorsMtDataReportVo, true) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 "+ conttypestr;*/
			}
		}
			sql+=conttypestr;
			return sql;
	}
	
	/**
	 * 彩信月报表获取查询表格--国家
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getMonthMMSTableNationSql(SpMtDataReportVo operatorsMtDataReportVo) { //change by denglj
		
		String conttypestr="";
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getSptypes()!=null){
			conttypestr=" AND u.SPTYPE IN ("+operatorsMtDataReportVo.getSptypes()+")";
		}
		
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getStaffname()!=null
				&&!"".equals(operatorsMtDataReportVo.getStaffname())){
            //conttypestr=" AND mtdatareport.USERID= '"+operatorsMtDataReportVo.getStaffname().trim()+"' ";
            conttypestr = " AND u.USERID like '%" + operatorsMtDataReportVo.getStaffname().trim() + "%' ";
		}
		//当查询各国发送时候
		if (operatorsMtDataReportVo.getNationcode() != null
				&& !"".equals(operatorsMtDataReportVo.getNationcode())) {
			conttypestr=conttypestr+" and area.AREACODE like '%"+operatorsMtDataReportVo.getNationcode()+"%' ";
		}
		if (operatorsMtDataReportVo.getNationname() != null
				&& !"".equals(operatorsMtDataReportVo.getNationname())) {
			conttypestr=conttypestr+" and area.AREANAME  like '%"+operatorsMtDataReportVo.getNationname()+"%' ";
			
		}
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		
		String sql=new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT ").append(getMonthConditionSql(operatorsMtDataReportVo,false))
				.append(") mtdatareport left join userdata u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=2 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
		if(operatorsMtDataReportVo.getCorpCode()!= null){
			if("100000".equals(operatorsMtDataReportVo.getCorpCode())){
				sql = new StringBuffer(" from  (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT ").append(getMonthConditionSql(operatorsMtDataReportVo,false))
						.append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=2 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
				sql+=conttypestr;
			}else{
				String spUsers = operatorsMtDataReportVo.getSpUsers()== null?"''":operatorsMtDataReportVo.getSpUsers();
				//如果是db2 oracle 数据库则兼容小写sp账号
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
						||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE ))
				{
					spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
				}
				//sql in查询拆分
				String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
				String SQL = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT where ("+insqlstr+") ").append(getMonthConditionSql(operatorsMtDataReportVo,true))
						.append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=2 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
				SQL+=conttypestr;
				return SQL;
			}
		}
			sql+=conttypestr;
			return sql;
	}
	
	

	/**
	 * 月报表获取查询条件
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getMonthConditionSql(SpMtDataReportVo operatorsMtDataReportVo,boolean iswhere) {
		StringBuilder sql = new StringBuilder();

		checkCondition(operatorsMtDataReportVo, sql);

		//如果选择的类型是日报表（后加的），
		if("2".equals(operatorsMtDataReportVo.getReportType())){
			if (operatorsMtDataReportVo.getSendTime() != null
					&& !"".equals(operatorsMtDataReportVo.getSendTime())) {
				String sendtime = operatorsMtDataReportVo.getSendTime().replaceAll("-", "");
				sql.append(" and IYMD>=").append(sendtime).append(" ");
			}
	
			if (operatorsMtDataReportVo.getEndTime() != null
					&& !"".equals(operatorsMtDataReportVo.getEndTime())) {
				String endtime=operatorsMtDataReportVo.getEndTime().replaceAll("-", "");
				sql.append(" and IYMD<=").append(endtime).append(" ");
			}
		}else{
		//如果不是，则按照原来的逻辑处理
				if (operatorsMtDataReportVo.getY() != null
						&& !"".equals(operatorsMtDataReportVo.getY())) {
					sql.append(" and Y=").append(operatorsMtDataReportVo.getY()).append(" ");
				}
				if (operatorsMtDataReportVo.getImonth() != null
						&& !"".equals(operatorsMtDataReportVo.getImonth())) {
					sql.append(" and IMONTH=").append(operatorsMtDataReportVo.getImonth()).append(" ");
				} else if("true".equals(operatorsMtDataReportVo.getIsYearDetail())){
					//如果是年的明细则是查询出所有月份
					sql.append(" and IMONTH in ('1','2','3','4','5','6','7','8','9','10','11','12')");
				}
		}

		String wheresql=sql.toString();
		//如果为false则代表没有where替换
		if(!iswhere){
			//将首个And替换成where
			wheresql=new ReciveBoxDao().getConditionSql(sql.toString());
		}
		return wheresql;
	}

	private static void checkCondition(SpMtDataReportVo vo, StringBuilder sql){
		String sendType = vo.getSendtypes();
		if (!StringUtils.isEmpty(sendType)) {
			sql.append(" and SENDTYPE=").append(sendType);
		}

		String userId = vo.getUserid();
		if (!StringUtils.isEmpty(userId)) {
			//如果是db2 oracle 则大写小写都加一份 兼容小写
			if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR")) &&
					(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE ||
					StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) ) {
				sql.append(" and USERID in ('").append(userId.toUpperCase()).append("','").append(userId.toLowerCase()).append("') ") ;
			}else{
				sql.append(" and USERID='").append(userId.toUpperCase()).append("' ");
			}
		}

		//增加运营商查询条件
		String spisuncm = vo.getSpisuncm();
		if (!StringUtils.isEmpty(spisuncm)) {
			//如果是国内的
			if("100".equals(spisuncm)){
				//是否有具体判断运营商的
				if(!StringUtils.isEmpty(vo.getDomesticOperator())){
					sql.append(" and SPISUNCM = ").append(vo.getDomesticOperator());
				}else {
					sql.append(" and SPISUNCM in (0,1,21) ");
				}
			}else if("5".equals(spisuncm)){
				//如果是国外类型
				sql.append(" and SPISUNCM =5");
			}
		}
		//区域
		String areaCode = vo.getAraCode();
		if (!StringUtils.isEmpty(areaCode)) {
			sql.append(" and MOBILEAREA = '").append(areaCode).append("'");
		}

		//业务类型
		String svrType = vo.getSvrType();
		if (!StringUtils.isEmpty(svrType)) {
			sql.append(" and SVRTYPE = '").append(svrType).append("'");
		}

        //操作员与机构--转化为操作员编码 P1字段
        String priviligeUserCode = vo.getPriviligeUserCode();
        if (!StringUtils.isEmpty(priviligeUserCode)) {
            if(priviligeUserCode.contains("(")){
                sql.append(" and P1 in ").append(priviligeUserCode);
            }else {
                sql.append(" and P1 = ").append(priviligeUserCode);
            }
        }
	}

	/**
	 * 月报表 分组sql
	 * @return
	 */
	public static String getMonthGroupBySql() {
		String sql =" group by mtdatareport.IMONTH,mtdatareport.USERID,mtdatareport.SENDTYPE ";
		return sql;
	}
	
	/**
	 * 月报表 分组sql--国家类型
	 * @return
	 */
	public static String getMonthGroupNationBySql() {
		String sql =" group by mtdatareport.USERID,mtdatareport.SENDTYPE,gate.SPGATE,gate.GATENAME,area.AREACODE,area.AREANAME  ";
		return sql;
	}

	/**
	 * 月报表获取排序sql
	 * @return
	 */
	public static String getMonthOrderBySql() {
		String sql =" order by mtdatareport.SENDTYPE,mtdatareport.USERID desc ";
		return sql;
	}
	
	/**
	 * 月报表获取排序sql
	 * @return
	 */
	public static String getMonthNationOrderBySql() {
		String sql =" order by mtdatareport.USERID desc ";
		return sql;
	}
	
		/**
	 * 日报表 分组sql
	 * @return
	 */
	public static String getDayGroupBySql() {
		String sql =" group by mtdatareport.USERID,mtdatareport.SENDTYPE ";
		return sql;
	}
	/**
	 * 日详细报表 分组sql
	 * @return
	 */
	public static String getDayDetailGroupBySql() {
		String sql =" group by mtdatareport.IYMD,mtdatareport.USERID,mtdatareport.SENDTYPE ";
		return sql;
	}
	
	/**
	 * 日详细报表 分组sql
	 * @return
	 */
	public static String getDayNationGroupBySql() {
		String sql =" group by mtdatareport.USERID,mtdatareport.SENDTYPE,gate.SPGATE,gate.GATENAME,area.AREACODE,area.AREANAME ";
		return sql;
	}
	
	
	
	/**
	 * 日报表获取排序sql
	 * @return
	 */
	public static String getDayOrderBySql() {
		String sql =" order by mtdatareport.USERID,mtdatareport.SENDTYPE desc ";
		return sql;
	}
	
	/**
	 * 日报表获取排序sql(excel导出排序)
	 * @return
	 */
	public static String getDayPageOrderBySql() {
		String sql =" order by mtdatareport.IYMD DESC ";
		return sql;
	}
	
	/**
	 * 以下是日报表用到的方法
	 * @return
	 */
	public static String getDayDetailFieldSql() {
		String sql =new StringBuffer("select "+ReportDAO.getPublicCountSql("mtdatareport")+",mtdatareport.IYMD ,max(mtdatareport.Y) Y,max(mtdatareport.IMONTH) IMONTH,")
		.append("max(mtdatareport.USERID) USERID,MAX(u.STAFFNAME) STAFFNAME,MAX(u.SPTYPE) SPTYPE,MAX(mtdatareport.SENDTYPE) SENDTYPE ").toString();
		return sql;
	}
	
	/**
	 * 以下是日报表(国家)用到的方法
	 * @return
	 */
	public static String getDayNationFieldSql() {
		String sql =new StringBuffer("select "+ReportDAO.getPublicCountSql("mtdatareport")+",max(mtdatareport.IYMD) IYMD ,max(mtdatareport.Y) Y,max(mtdatareport.IMONTH) IMONTH,")
		.append("max(mtdatareport.USERID) USERID,MAX(u.STAFFNAME) STAFFNAME,MAX(u.SPTYPE) SPTYPE,MAX(mtdatareport.SENDTYPE) SENDTYPE,gate.SPGATE spgatecode,gate.GATENAME spgatename,area.AREACODE nationcode,area.AREANAME nationname").toString();
		return sql;
	}
	
	
	/**
	 * 以下是日报表用到的方法
	 * @return
	 */
	public static String getDayFieldSql() {
		String sql =new StringBuffer("select "+ReportDAO.getPublicCountSql("mtdatareport")+",max(mtdatareport.Y) Y,max(mtdatareport.IMONTH) IMONTH,")
		.append("max(mtdatareport.USERID) USERID,MAX(u.STAFFNAME) STAFFNAME,MAX(u.SPTYPE) SPTYPE,MAX(mtdatareport.SENDTYPE) SENDTYPE,MAX(mtdatareport.SPISUNCM) SPISUNCM ").toString();
		return sql;
	}

	/**
	 * 日报表获取查询表格
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getDayTableSql(SpMtDataReportVo operatorsMtDataReportVo) { //change by denglj
		
		String conttypestr="";
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getSptypes()!=null
				&&!"".equals(operatorsMtDataReportVo.getSptypes())){
			conttypestr=" AND u.SPTYPE IN ("+operatorsMtDataReportVo.getSptypes()+")";
		}
		
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getStaffname()!=null
				&&!"".equals(operatorsMtDataReportVo.getStaffname())){
            conttypestr = " AND u.STAFFNAME like '%" + operatorsMtDataReportVo.getStaffname().trim() + "%' ";
		}
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		String sql= " from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM FROM MT_DATAREPORT " +
				getDayConditionSql(operatorsMtDataReportVo, false) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+ ") u ON mtdatareport.USERID=u.USERID AND U.ACCOUNTTYPE=1 ";
		if(operatorsMtDataReportVo != null && operatorsMtDataReportVo.getCorpCode()!= null){
			if("100000".equals(operatorsMtDataReportVo.getCorpCode())){
				sql = " from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM FROM MT_DATAREPORT " +
						getDayConditionSql(operatorsMtDataReportVo, false) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 ";
			}else{
				String spUsers = operatorsMtDataReportVo.getSpUsers()== null?"''":operatorsMtDataReportVo.getSpUsers();
				//如果是db2 oracle 数据库则兼容小写sp账号
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
						||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) )
				{
					spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
				}
				if(StringUtils.isNotEmpty(operatorsMtDataReportVo.getSpUserId())){
					///spUsers = spUsers+","+operatorsMtDataReportVo.getSpUserId();
					spUsers = operatorsMtDataReportVo.getSpUserId();
				}
				//sql in查询拆分
				String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
				//String tempCondition = " AND 1 = 1 ";
				if("2".equals(operatorsMtDataReportVo.getCurrentUserId())){
                    operatorsMtDataReportVo.setPriviligeUserCode("");//系统管理员查全部的
                }
				String SQL = " from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MT_DATAREPORT where (" + insqlstr + ") " +
						getDayConditionSql(operatorsMtDataReportVo, true) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 ";
/*				String SQL = " from (select report.ICOUNT,RSUCC,RFAIL1,report.RFAIL2,report.RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,report.TASKID from MT_DATAREPORT report" +
						" LEFT JOIN LF_MTTASK task ON report.TASKID = task.TASKID  where (" + insqlstr + ") " +tempCondition+
						getDayConditionSql(operatorsMtDataReportVo, true) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 ";*/
				SQL+=conttypestr;
				return SQL;
			}
		}
		sql+=conttypestr;
		return sql;
	}
	
	/**
	 * 日明细报表获取查询表格
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getDayDetailTableSql(SpMtDataReportVo operatorsMtDataReportVo) { //change by denglj

		//日报表详情莫雷雷
		String conttypestr="";
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getSptypes()!=null
				&&!"".equals(operatorsMtDataReportVo.getSptypes())){
			conttypestr=" AND u.SPTYPE IN ("+operatorsMtDataReportVo.getSptypes()+")";
		}
		
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getStaffname()!=null
				&&!"".equals(operatorsMtDataReportVo.getStaffname())){
			conttypestr=" AND  mtdatareport.USERID= '"+operatorsMtDataReportVo.getStaffname().trim()+"' ";
		}
		
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		
		String sql=new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,IYMD,SPISUNCM FROM MT_DATAREPORT ")
		.append(getDayConditionSql(operatorsMtDataReportVo,false)).append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u ON mtdatareport.USERID=u.USERID AND U.ACCOUNTTYPE=1 ").toString();
		if(operatorsMtDataReportVo != null && operatorsMtDataReportVo.getCorpCode()!= null){
			if("100000".equals(operatorsMtDataReportVo.getCorpCode())){
				sql = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,IYMD,SPISUNCM FROM MT_DATAREPORT ")
						.append(getDayConditionSql(operatorsMtDataReportVo,false)).append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 ").toString();
			}else{
				//String spUsers = operatorsMtDataReportVo.getSpUsers()== null?"''":operatorsMtDataReportVo.getSpUsers();
				String spUsers = operatorsMtDataReportVo.getUserid()== null?"''":operatorsMtDataReportVo.getUserid();
				//如果是db2 oracle 数据库则兼容小写sp账号
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
						||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) )
				{
					spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
				}
				//sql in查询拆分
				//String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
                String insqlstr= " 1 = 1";//只查SP账号下发的
                //String tempCondition = " AND 1 = 1 ";
                if("2".equals(operatorsMtDataReportVo.getCurrentUserId())){
                    operatorsMtDataReportVo.setPriviligeUserCode("");
                }
                String SQL = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,IYMD,SPISUNCM from MT_DATAREPORT where ("+insqlstr+") ")
					.append(getDayConditionSql(operatorsMtDataReportVo,true)).append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 ").toString();
				/*String SQL = " from (select report.ICOUNT,RSUCC,RFAIL1,report.RFAIL2,report.RNRET,Y,IMONTH,USERID,SENDTYPE,IYMD,SPISUNCM,report.TASKID from MT_DATAREPORT report" +
						" LEFT JOIN LF_MTTASK task ON report.TASKID = task.TASKID  where (" + insqlstr + ") " +tempCondition+
						getDayConditionSql(operatorsMtDataReportVo, true) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 ";*/
				SQL+=conttypestr;
				return SQL;
			}
		}
		sql+=conttypestr;
		return sql;
	}
	
	
	/**
	 * 日国家报表获取查询表格
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getDayNationTableSql(SpMtDataReportVo operatorsMtDataReportVo) { //change by denglj
		
		String conttypestr="";
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getSptypes()!=null
				&&!"".equals(operatorsMtDataReportVo.getSptypes())){
			conttypestr=" AND u.SPTYPE IN ("+operatorsMtDataReportVo.getSptypes()+")";
		}
		
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getStaffname()!=null
				&&!"".equals(operatorsMtDataReportVo.getStaffname())){
			conttypestr=" AND  mtdatareport.USERID='"+operatorsMtDataReportVo.getStaffname().trim()+"' ";
		}
		
		//当查询各国发送时候
		if (operatorsMtDataReportVo.getNationcode() != null
				&& !"".equals(operatorsMtDataReportVo.getNationcode())) {
			conttypestr=conttypestr+" and area.AREACODE like '%"+operatorsMtDataReportVo.getNationcode()+"%' ";
		}
		if (operatorsMtDataReportVo.getNationname() != null
				&& !"".equals(operatorsMtDataReportVo.getNationname())) {
			conttypestr=conttypestr+" and area.AREANAME  like '%"+operatorsMtDataReportVo.getNationname()+"%' ";
			
		}
		
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		
		String sql=new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,IYMD,SPISUNCM,AREACODE,SPGATE  FROM MT_DATAREPORT ")
		.append(getDayConditionSql(operatorsMtDataReportVo,false)).append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA "
				+") u ON mtdatareport.USERID=u.USERID AND U.ACCOUNTTYPE=1 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=1 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
		if(operatorsMtDataReportVo.getCorpCode()!= null){
			if("100000".equals(operatorsMtDataReportVo.getCorpCode())){
				sql = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,IYMD,SPISUNCM,AREACODE,SPGATE FROM MT_DATAREPORT ")
						.append(getDayConditionSql(operatorsMtDataReportVo,false)).append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=1 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
			}else{
				String spUsers = operatorsMtDataReportVo.getSpUsers()== null?"''":operatorsMtDataReportVo.getSpUsers();
				//如果是db2 oracle 数据库则兼容小写sp账号
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
						||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) )
				{
					spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
				}
				//sql in查询拆分
				String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
				String SQL = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,IYMD,SPISUNCM,AREACODE,SPGATE  from MT_DATAREPORT where ("+insqlstr+") ")
					.append(getDayConditionSql(operatorsMtDataReportVo,true)).append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=1 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
				SQL+=conttypestr;
				return SQL;
			}
		}
		sql+=conttypestr;
		return sql;
	}
	
	/**
	 * 彩信日报表获取查询表格
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getDayMMSTableSql(SpMtDataReportVo operatorsMtDataReportVo) {

		String conttypestr="";
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getSptypes()!=null){
			conttypestr=" AND u.SPTYPE IN ("+operatorsMtDataReportVo.getSptypes()+")";
		}
		
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getStaffname()!=null
				&&!"".equals(operatorsMtDataReportVo.getStaffname())){
			conttypestr=" AND  u.STAFFNAME= '"+operatorsMtDataReportVo.getStaffname().trim()+"' ";
		}
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		
		String sql= " from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT " + getMonthConditionSql(operatorsMtDataReportVo, false) +
				") mtdatareport left join userdata u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 ";
		if(operatorsMtDataReportVo.getCorpCode()!= null){
			if("100000".equals(operatorsMtDataReportVo.getCorpCode())){
				sql = " from  (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT " + getMonthConditionSql(operatorsMtDataReportVo, false) +
						") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 ";
				sql+=conttypestr;
				return sql;
			}else{
				String spUsers = operatorsMtDataReportVo.getSpUsers()== null?"''":operatorsMtDataReportVo.getSpUsers();
				//如果是db2 oracle 数据库则兼容小写sp账号
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
						||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) )
				{
					spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
				}
				//sql in查询拆分
				String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
                //String tempCondition = " AND 1 = 1 ";
                if("2".equals(operatorsMtDataReportVo.getCurrentUserId())){
                    operatorsMtDataReportVo.setPriviligeUserCode("");
                }
				String SQL = " from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT where (" + insqlstr + ") " + getMonthConditionSql(operatorsMtDataReportVo, true) +
						") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 ";
/*				String SQL = " from (select report.ICOUNT,RSUCC,RFAIL1,report.RFAIL2,report.RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,report.TASKID from MMS_DATAREPORT report" +
						" LEFT JOIN LF_MTTASK task ON report.TASKID = task.TASKID  where (" + insqlstr + ") " +tempCondition+
						getDayConditionSql(operatorsMtDataReportVo, true) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
						+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 ";*/
				SQL+=conttypestr;
				return SQL;
			}
		}
			sql+=conttypestr;
			return sql;
	}
	
	/**
	 * 彩信日报表获取查询表格
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getDayDetailMMSTableSql(SpMtDataReportVo operatorsMtDataReportVo) { //change by denglj

		String conttypestr="";
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getSptypes()!=null){
			conttypestr=" AND u.SPTYPE IN ("+operatorsMtDataReportVo.getSptypes()+")";
		}
		
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getStaffname()!=null
				&&!"".equals(operatorsMtDataReportVo.getStaffname())){
			conttypestr=" AND  mtdatareport.USERID='"+operatorsMtDataReportVo.getStaffname().trim()+"' ";
		}
		
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		
		String sql=new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM  from MMS_DATAREPORT ").append(getMonthConditionSql(operatorsMtDataReportVo,false))
				.append(") mtdatareport left join userdata u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 ").toString();
		if(operatorsMtDataReportVo.getCorpCode()!= null){
			if("100000".equals(operatorsMtDataReportVo.getCorpCode())){
				sql = new StringBuffer(" from  (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM ,IYMD from MMS_DATAREPORT ").append(getMonthConditionSql(operatorsMtDataReportVo,false))
						.append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 ").toString();
				sql+=conttypestr;
			}else{
				String spUsers = operatorsMtDataReportVo.getSpUsers()== null?"''":operatorsMtDataReportVo.getSpUsers();
				//如果是db2 oracle 数据库则兼容小写sp账号
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
						||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) )
				{
					spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
				}
				//sql in查询拆分
				//String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
                String insqlstr = " 1 = 1 ";
                //String tempCondition = " AND 1 = 1 ";
                if("2".equals(operatorsMtDataReportVo.getCurrentUserId())){
                    operatorsMtDataReportVo.setPriviligeUserCode("");
                }
				String SQL = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,IYMD  from MMS_DATAREPORT where ("+insqlstr+") ").append(getMonthConditionSql(operatorsMtDataReportVo,true))
						.append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 ").toString();

/*                String SQL = new StringBuffer(" from (select report.ICOUNT,RSUCC,RFAIL1,report.RFAIL2,report.RNRET,Y,IMONTH,USERID,SENDTYPE,IYMD,SPISUNCM,report.TASKID from MT_DATAREPORT report" +
                        " LEFT JOIN LF_MTTASK task ON report.TASKID = task.TASKID  where (" + insqlstr + ") " +tempCondition+
                        getDayConditionSql(operatorsMtDataReportVo, true) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
                        + ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2").toString();*/
				SQL+=conttypestr;
				return SQL;
			}
		}
			sql+=conttypestr;
			return sql;
	}
	
	/**
	 * 彩信日国家报表获取查询表格
	 * @param operatorsMtDataReportVo
	 * @return
	 */
	public static String getDayNationMMSTableSql(SpMtDataReportVo operatorsMtDataReportVo) { //change by denglj

		String conttypestr="";
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getSptypes()!=null){
			conttypestr=" AND u.SPTYPE IN ("+operatorsMtDataReportVo.getSptypes()+")";
		}
		
		if(operatorsMtDataReportVo!=null&&operatorsMtDataReportVo.getStaffname()!=null
				&&!"".equals(operatorsMtDataReportVo.getStaffname())){
			conttypestr=" AND  mtdatareport.USERID= '"+operatorsMtDataReportVo.getStaffname().trim()+"' ";
		}
		
		//当查询各国发送时候
		if (operatorsMtDataReportVo.getNationcode() != null
				&& !"".equals(operatorsMtDataReportVo.getNationcode())) {
			conttypestr=conttypestr+" and area.AREACODE like '%"+operatorsMtDataReportVo.getNationcode()+"%' ";
		}
		if (operatorsMtDataReportVo.getNationname() != null
				&& !"".equals(operatorsMtDataReportVo.getNationname())) {
			conttypestr=conttypestr+" and area.AREANAME  like '%"+operatorsMtDataReportVo.getNationname()+"%' ";
			
		}
		
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		
		String sql=new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,AREACODE,SPGATE   from MMS_DATAREPORT ").append(getMonthConditionSql(operatorsMtDataReportVo,false))
				.append(") mtdatareport left join userdata u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=2 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
		if(operatorsMtDataReportVo.getCorpCode()!= null){
			if("100000".equals(operatorsMtDataReportVo.getCorpCode())){
				sql = new StringBuffer(" from  (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,AREACODE,SPGATE   from MMS_DATAREPORT ").append(getMonthConditionSql(operatorsMtDataReportVo,false))
						.append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID,IYMD FROM USERDATA  "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=2 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
				sql+=conttypestr;
			}else{
				String spUsers = operatorsMtDataReportVo.getSpUsers()== null?"''":operatorsMtDataReportVo.getSpUsers();
				//如果是db2 oracle 数据库则兼容小写sp账号
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
						||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) )
				{
					spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
				}
				//sql in查询拆分
				String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
				String SQL = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,AREACODE,SPGATE   from MMS_DATAREPORT where ("+insqlstr+") ").append(getMonthConditionSql(operatorsMtDataReportVo,true))
						.append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID,IYMD FROM USERDATA "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 left join XT_GATE_QUEUE gate on gate.SPGATE=mtdatareport.SPGATE and gate.GATETYPE=2 left join A_AREACODE area on area.code=mtdatareport.AREACODE ").toString();
				SQL+=conttypestr;
				return SQL;
			}
		}
			sql+=conttypestr;
			return sql;
	}

	/**
	 * 日报表获取查询条件
	 * @param operatorsMtDataReportVo
	 * @param iswhere 外接sql是否包含where true包含   false不包含
	 * @return
	 */
	public static String getDayConditionSql(
			SpMtDataReportVo operatorsMtDataReportVo,boolean iswhere) {
		StringBuilder sql = new StringBuilder();
		if(operatorsMtDataReportVo != null){
			if("0".equals(operatorsMtDataReportVo.getReportType())){
				if (operatorsMtDataReportVo.getY() != null
						&& !"".equals(operatorsMtDataReportVo.getY())) {
					sql.append(" and Y=").append(operatorsMtDataReportVo.getY()).append(" ");
				}

				if (operatorsMtDataReportVo.getImonth() != null
						&& !"".equals(operatorsMtDataReportVo.getImonth())) {
					sql.append(" and IMONTH=").append(operatorsMtDataReportVo.getImonth()).append(" ");
				}
			}else if("1".equals(operatorsMtDataReportVo.getReportType())){
				if (operatorsMtDataReportVo.getY() != null
						&& !"".equals(operatorsMtDataReportVo.getY())) {
					sql.append(" and Y=").append(operatorsMtDataReportVo.getY()).append(" ");
				}
			}else{
				if (operatorsMtDataReportVo.getSendTime() != null
						&& !"".equals(operatorsMtDataReportVo.getSendTime())) {
					String sendtime = operatorsMtDataReportVo.getSendTime().replaceAll("-", "");
					sql.append(" and IYMD>=").append(sendtime).append(" ");
				}

				if (operatorsMtDataReportVo.getEndTime() != null
						&& !"".equals(operatorsMtDataReportVo.getEndTime())) {
					String endtime=operatorsMtDataReportVo.getEndTime().replaceAll("-", "");
					sql.append(" and IYMD<=").append(endtime).append(" ");
				}
			}
			checkCondition(operatorsMtDataReportVo, sql);
		}

		String wheresql=sql.toString();
		//如果为false则代表没有where替换
		if(!iswhere){
			//将首个And替换成where
			wheresql=new ReciveBoxDao().getConditionSql(sql.toString());
		}
		return wheresql;
		//return sql.toString();
	}

	/**
	 * 以下是年报表用到的方法
	 * @return
	 */
	public static String getYearFieldSql() {
		String sql = new StringBuffer("select "+ReportDAO.getPublicCountSql("mtdatareport")+",mtdatareport.Y,max(mtdatareport.IMONTH) IMONTH,")
				.append("mtdatareport.USERID USERID,MAX(u.STAFFNAME) STAFFNAME,MAX(u.SPTYPE) SPTYPE,MAX(mtdatareport.SENDTYPE) SENDTYPE,MAX(mtdatareport.SPISUNCM) SPISUNCM ").toString();
		return sql;
	}

	/**
	 * 年报表 获取查询表格
	 * @param mtDataReportVo
	 * @return
	 */
	public static String getYearTableSql(SpMtDataReportVo mtDataReportVo) { //change by denglj
	
		String conttypestr="";
		//账户类型
		if(mtDataReportVo!=null&&mtDataReportVo.getSptypes()!=null){
			conttypestr=" AND u.SPTYPE IN ("+mtDataReportVo.getSptypes()+")";
		}
		//账户名称
		if(mtDataReportVo!=null&&mtDataReportVo.getStaffname()!=null
				&&!"".equals(mtDataReportVo.getStaffname())){
            conttypestr = " AND u.STAFFNAME like '%" + mtDataReportVo.getStaffname().trim() + "%' ";
		}
		
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		
		String sql = " from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MT_DATAREPORT " + getYearConditionSql(mtDataReportVo, false) +
				") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA "
				+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 ";
		if(mtDataReportVo.getCorpCode()!=null&&"100000".equals(mtDataReportVo.getCorpCode()))//多企业版
		{
			String SQL = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MT_DATAREPORT ").append(getYearConditionSql(mtDataReportVo,false))
					.append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID,SENDTYPE FROM USERDATA "
				+") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 ").toString();
			SQL+=conttypestr;
			return SQL;
		}
		else if(mtDataReportVo.getCorpCode()!=null&&!"100000".equals(mtDataReportVo.getCorpCode())
				&&!"".equals(mtDataReportVo.getSpUsers())&&mtDataReportVo.getSpUsers()!=null)//单企业版
		{ 
			String spUsers=mtDataReportVo.getSpUsers();
			//如果是db2 oracle 数据库则兼容小写sp账号
			if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
					||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) )
			{
				spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
			}
			//sql in查询拆分
			if(StringUtils.isNotEmpty(mtDataReportVo.getSpUserId())){
				//spUsers = spUsers+","+mtDataReportVo.getSpUserId();
				spUsers = mtDataReportVo.getSpUserId();
			}
			String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
			if("2".equals(mtDataReportVo.getCurrentUserId())){
                mtDataReportVo.setPriviligeUserCode("");
            }
			sql = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MT_DATAREPORT where ("+insqlstr+") ").append(getYearConditionSql(mtDataReportVo,true))
					.append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID= u.userid and u.ACCOUNTTYPE=1 ").toString();

/*			sql = new StringBuffer(" from (select report.ICOUNT,RSUCC,RFAIL1,report.RFAIL2,report.RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,report.TASKID from MT_DATAREPORT report" +
					" LEFT JOIN LF_MTTASK task ON report.TASKID = task.TASKID  where (" + insqlstr + ") " +" AND task.USER_ID = "+mtDataReportVo.getCurrentUserId()+
					getDayConditionSql(mtDataReportVo, true) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
					+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=1 ").toString();*/
			sql+=conttypestr;
			return sql;
		}
		else {
			sql+=conttypestr;
			return sql;
		}
		
	}
	
	
	/**
	 * 彩信年报表 获取查询表格
	 * @param mtDataReportVo
	 * @return
	 */
	public static String getYearMMSTableSql(SpMtDataReportVo mtDataReportVo) { //change by denglj
		
		String conttypestr="";
		//账户类型
		if(mtDataReportVo!=null&&mtDataReportVo.getSptypes()!=null){
			conttypestr=" AND u.SPTYPE IN ("+mtDataReportVo.getSptypes()+")";
		}
		
		//账户名称
		if(mtDataReportVo!=null&&mtDataReportVo.getStaffname()!=null
				&&!"".equals(mtDataReportVo.getStaffname())){
            //conttypestr=" AND mtdatareport.USERID='"+mtDataReportVo.getStaffname().trim()+"' ";
            conttypestr = " AND u.STAFFNAME like '%" + mtDataReportVo.getStaffname().trim() + "%' ";
		}
		
		//将首个and改成where
		conttypestr=new ReciveBoxDao().getConditionSql(conttypestr);
		
		String sql = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT ").append(getYearConditionSql(mtDataReportVo,false))
				.append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID= u.USERID and u.ACCOUNTTYPE=2 ").toString();
		if(mtDataReportVo.getCorpCode()!=null&&"100000".equals(mtDataReportVo.getCorpCode()))//SYSADMIN
		{
			String SQL =  new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT ").append(getYearConditionSql(mtDataReportVo,false))
					.append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID= u.USERID and u.ACCOUNTTYPE=2 ").toString();
			SQL+=conttypestr;
			return SQL;
		}
		else if(mtDataReportVo.getCorpCode()!=null&&!"100000".equals(mtDataReportVo.getCorpCode())
				&&!"".equals(mtDataReportVo.getSpUsers())&&mtDataReportVo.getSpUsers()!=null)//单企业版
		{ 
			String spUsers=mtDataReportVo.getSpUsers();
			//如果是db2 oracle 数据库则兼容小写sp账号
			if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
					||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) )
			{
				spUsers=spUsers.toUpperCase()+","+spUsers.toLowerCase();
			}
			//sql in查询拆分
			String insqlstr=new ReciveBoxDao().getSqlStr(spUsers, "USERID");
			if("2".equals(mtDataReportVo.getCurrentUserId())){
                mtDataReportVo.setPriviligeUserCode("");
            }
			sql = new StringBuffer(" from (select ICOUNT,RSUCC,RFAIL1,RFAIL2,RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM from MMS_DATAREPORT where ("+insqlstr+") ").append(getYearConditionSql(mtDataReportVo,true))
					.append(") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
				+") u on mtdatareport.USERID= u.USERID and u.ACCOUNTTYPE=2 ").toString();
/*			sql = new StringBuffer(" from (select report.ICOUNT,RSUCC,RFAIL1,report.RFAIL2,report.RNRET,Y,IMONTH,USERID,SENDTYPE,SPISUNCM,report.TASKID from MMS_DATAREPORT report" +
					" LEFT JOIN LF_MTTASK task ON report.TASKID = task.TASKID  where (" + insqlstr + ") " +" AND task.USER_ID = "+mtDataReportVo.getCurrentUserId()+
					getDayConditionSql(mtDataReportVo, true) + ") mtdatareport left join (SELECT STAFFNAME,SPTYPE,ACCOUNTTYPE,USERID FROM USERDATA  "
					+ ") u on mtdatareport.USERID=u.userid and u.ACCOUNTTYPE=2 ").toString();*/
			sql+=conttypestr;
			return sql;
		}
		else{ 
			sql+=conttypestr;
			return sql;
		}
	}
	
	
	
	/**
	 * 年报表获取查询条件
	 * 
	 * @param operatorsMtDataReportVo
	 * @param iswhere
	 *        外接sql是否包含where true包含 false不包含
	 * @return
	 */
	public static String getYearConditionSql(SpMtDataReportVo operatorsMtDataReportVo,boolean iswhere) {
		StringBuilder sql = new StringBuilder();
		
		checkCondition(operatorsMtDataReportVo, sql);

		if (operatorsMtDataReportVo.getY() != null
				&& !"".equals(operatorsMtDataReportVo.getY())) {
			sql.append(" and Y=").append(operatorsMtDataReportVo.getY()).append(" ");
		}
		
		String wheresql=sql.toString();
		//如果为false则代表没有where替换
		if(!iswhere){
			//将首个And替换成where
			wheresql=new ReciveBoxDao().getConditionSql(sql.toString());
		}
		return wheresql;
	}

	/**
	 * 年报表 获取分组sql
	 * @return
	 */
	public static String getYearGroupBySql() {
		String sql =" group by mtdatareport.Y,mtdatareport.USERID,mtdatareport.SENDTYPE  ";
		return sql;
	}

	/**
	 * 年报表获取排序sql
	 * @return
	 */
	public static String getYearOrderBySql() {
		String sql = " order by mtdatareport.Y desc ";
		return sql;
	}
}
