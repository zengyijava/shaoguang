package com.montnets.emp.shorturl.report.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.shorturl.report.vo.SendDetailMttaskVo;
import com.montnets.emp.table.sms.TableMtTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MtRecordDAOSql {
	/**
	 * 构造查询条件sql
	 * @param conditionMap 查询条件
	 * @param tableName 表名
	 * @return 返回查询条件sql，包含where
	 */
	public static String getConditionSql(LinkedHashMap<String, String> conditionMap, String tableName) {
		try {
			boolean hasWhere = false;

			StringBuffer conditionSql = new StringBuffer();
			
			//多企业，且未绑定sp账号，则查询结果为空
			if(StaticValue.getCORPTYPE() == 1 && (conditionMap.get("spUsers") == null || conditionMap.get("spUsers").length() < 1)) {
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append("1=2 ");
			}
			//手机号
			if (conditionMap.get("phone") != null && conditionMap.get("phone").trim().length() > 0) {
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append(tableName).append(".PHONE='").append(conditionMap.get("phone").trim()).append("'");
			}
			//任务批次
			if (conditionMap.get("taskid") != null && conditionMap.get("taskid").trim().length() > 0) {
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append(tableName).append(".TASKID=").append(conditionMap.get("taskid").trim());
			}
			//自定义流水号
			if (conditionMap.get("usermsgid") != null && conditionMap.get("usermsgid").trim().length() > 0) {
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append(tableName).append(".usermsgid=").append(conditionMap.get("usermsgid").trim());
			}
			//指定时间段，开始时间
			if (conditionMap.get("sendtime") != null && conditionMap.get("sendtime").trim().length() > 0) {
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append(tableName).append(".SENDTIME>=?");
			}
			//指定时间段，结束时间
			if (conditionMap.get("recvtime") != null && conditionMap.get("recvtime").trim().length() > 0) {
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append(tableName).append(".SENDTIME<=?");
			}
			// sp账号
			if (conditionMap.get("userid") != null && conditionMap.get("userid").trim().length() > 0) {
				//如果是DB2或者oracle 则做兼容小写处理
				if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE== StaticValue.ORACLE_DBTYPE || StaticValue.DBTYPE== StaticValue.DB2_DBTYPE)){
					String useridstr = conditionMap.get("userid");
					hasWhere = addWhereOrAnd(conditionSql, hasWhere);
					conditionSql.append(tableName).append(".USERID").append(" in ('").append(useridstr.toUpperCase().trim()).append("','")
					.append(useridstr.toLowerCase().trim()).append("') ");
				}else{
					hasWhere = addWhereOrAnd(conditionSql, hasWhere);
					conditionSql.append(tableName).append(".USERID").append("='").append(conditionMap.get("userid").trim()).append("'");
				}
			}
			//通道号
			if (conditionMap.get("spgate") != null && conditionMap.get("spgate").trim().length() > 0) {
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append(tableName).append(".SPGATE='").append(conditionMap.get("spgate").trim()).append("'");
			}
			//运营商
			if (conditionMap.get("spisuncm") != null && conditionMap.get("spisuncm").trim().length() > 0) {
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append(tableName).append(".UNICOM=").append(conditionMap.get("spisuncm").trim());
			}
			//业务类型
			if (conditionMap.get("buscode") != null && conditionMap.get("buscode").trim().length() > 0) {
				//无业务类型
				if("-1".equals(conditionMap.get("buscode").trim())) {
					hasWhere = addWhereOrAnd(conditionSql, hasWhere);
					conditionSql.append(" not exists (select BUS_CODE from LF_BUSMANAGER ").append(StaticValue.getWITHNOLOCK()).append(" where BUS_CODE=").append(tableName).append(".SVRTYPE) ");
				} else {
					hasWhere = addWhereOrAnd(conditionSql, hasWhere);
					conditionSql.append(tableName).append(".SVRTYPE='").append(conditionMap.get("buscode").trim()).append("'");
				}
			}
			//为多企业，且不是100000企业，且查询条件没选sp账号的情况下，则拼入绑定的发送账号
			if (StaticValue.getCORPTYPE() == 1 && !"100000".equals(conditionMap.get("lgcorpcode")) && (conditionMap.get("userid") == null || conditionMap.get("userid").trim().length() < 1)) {
				String spusers = conditionMap.get("spUsers");
				if(spusers!=null){
					//sp账号个数
					int length = 0;
					if(spusers.contains(",")) {
						String[] useriday = spusers.split(",");
						length = useriday.length;
					}
					hasWhere = addWhereOrAnd(conditionSql, hasWhere);
					//如果个数小于50则用in
					if(length<50) {
						//如果是DB2或者oracle 则做兼容小写处理
						if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE== StaticValue.ORACLE_DBTYPE
								|| StaticValue.DBTYPE== StaticValue.DB2_DBTYPE)){
							spusers = spusers.toUpperCase() + "," + spusers.toLowerCase();
						}
						String insqlstr = getSqlStr(spusers, tableName + ".USERID");
						conditionSql.append(" (").append(insqlstr).append(") ");
					} else {
						//如果是DB2或者oracle 则做兼容小写处理
						if("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR"))&&(StaticValue.DBTYPE== StaticValue.ORACLE_DBTYPE
								|| StaticValue.DBTYPE== StaticValue.DB2_DBTYPE)){
							conditionSql.append(" exists (select SPUSER from lf_sp_dep_bind ").append(StaticValue.getWITHNOLOCK())
							.append(" where upper(SPUSER)=upper(").append(tableName).append(".USERID) ");
						}else{
							conditionSql.append(" exists (select SPUSER from lf_sp_dep_bind ").append(StaticValue.getWITHNOLOCK())
										.append(" where SPUSER=").append(tableName).append(".USERID ");
						}
						if(conditionMap.get("lgcorpcode") != null && conditionMap.get("lgcorpcode").length() > 0) {
							conditionSql.append(" and CORP_CODE= '").append(conditionMap.get("lgcorpcode").trim()).append("' ");
						}
						conditionSql.append(") ");
					}
				}
			}
			
			//错误码查询
			if (conditionMap.get("mterrorcode") != null && conditionMap.get("mterrorcode").trim().length() > 0) {
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append(tableName).append(".ERRORCODE='").append(conditionMap.get("mterrorcode")).append("'");
			}

			//多企业需要加入企业编码进行筛选
			if (StaticValue.getCORPTYPE() == 1 && !"100000".equals(conditionMap.get("lgcorpcode"))){
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
//				conditionSql.append("lfmttask.").append(TableLfMttask.CORP_CODE).append("='").append(conditionMap.get("lgcorpcode")).append("'");
                conditionSql.append(tableName).append(".ECID").append("='").append(conditionMap.get("lgcorpcode")).append("'");
			}

			//控制数据权限
			StringBuffer domusercodesql=new StringBuffer("");
			if(conditionMap.get("domUsercode") != null && conditionMap.get("domUsercode").length() < 1) {
				//有权限查看的操作员编码为空字符串，则是admin或者是管辖顶级机构，不需要拼接p1条件
				//
			} else if(conditionMap.get("domUsercode") != null && conditionMap.get("domUsercode").length() > 0) {
				//有权限查看的操作员编码有值，则需要拼入p1条件，以控制查询权限
				//hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				//获取是否开启p2参数配置
				String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
				//获取不到配置文件值
				if(depcodethird2p2 == null || depcodethird2p2.trim().length() == 0) {
					depcodethird2p2 = "false";
				}
				if(!"true".equals(depcodethird2p2)){
					domusercodesql.append(tableName).append(".p1 in (").append(conditionMap.get("domUsercode")).append(")");
				}else{
					domusercodesql.append(" (").append(tableName).append(".p2 in (select DEP_CODE_THIRD from LF_DEP where DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=").append(conditionMap.get("curUserId")).append("))")
					.append(" or ").append(tableName).append(".p1=").append(conditionMap.get("curUsercode")).append(")");
				}
			} else {
				//不能用已知权限，则使用语句，在操作员编码超过1000时使用，性能差
				//hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				//获取是否开启p2参数配置
				String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
				//获取不到配置文件值
				if(depcodethird2p2 == null || depcodethird2p2.trim().length() == 0)
				{
					depcodethird2p2 = "false";
				}
				if("true".equals(depcodethird2p2)){
					domusercodesql.append(" (").append(tableName).append(".p2 in (select DEP_CODE_THIRD from LF_DEP where DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=").append(conditionMap.get("curUserId")).append("))")
					.append(" or ").append(tableName).append(".p1=").append(conditionMap.get("curUsercode")).append(")");
				}else{
					domusercodesql.append(" (").append(tableName).append(".p1 in (select USER_CODE from LF_SYSUSER where DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=").append(conditionMap.get("curUserId")).append("))")
					.append(" or ").append(tableName).append(".p1=").append(conditionMap.get("curUsercode")).append(")");
				}
				
				
				//conditionSql.append(" (").append(tableName).append(".p1 in (select USER_CODE from LF_SYSUSER where DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=").append(conditionMap.get("curUserId")).append("))")
				//.append(" or ").append(tableName).append(".p1=").append(conditionMap.get("curUsercode")).append(")");
			}
			
			StringBuffer spuserprisql=new StringBuffer("");
			if(conditionMap.get("spuserpri") != null && conditionMap.get("spuserpri").length() < 1) {
				//有权限查看的操作员编码为空字符串，则是admin或者是管辖顶级机构，不需要拼接p1条件
			}
			else if(conditionMap.get("spuserpri") != null && conditionMap.get("spuserpri").length() > 0) {
				//有权限查看的操作员编码有值，则需要拼入p1条件，以控制查询权限
				spuserprisql.append(tableName).append(".USERID in (").append(conditionMap.get("spuserpri")).append(")");
			} else {
				//不能用已知权限，则使用语句，在操作员编码超过1000时使用，性能差
				spuserprisql.append(" ").append(tableName).append(".USERID in (select SPUSERID FROM LF_MT_PRI WHERE USER_ID=").append(conditionMap.get("spcurUserId")).append(" AND CORP_CODE=").append(conditionMap.get("spcurcorpcode")).append(")");
				//conditionSql.append(" (").append(tableName).append(".p1 in (select USER_CODE from LF_SYSUSER where DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=").append(conditionMap.get("curUserId")).append("))")
				//.append(" or ").append(tableName).append(".p1=").append(conditionMap.get("curUsercode")).append(")");
			}
			
			if( domusercodesql.length() !=0 && spuserprisql.length() != 0){
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append(" ( ").append(domusercodesql).append(" or ").append(spuserprisql).append(" ) ");
			}else if(domusercodesql.length()!=0&&spuserprisql.length()==0){
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append(domusercodesql);
			}else if(domusercodesql.length()==0&&spuserprisql.length()!=0){
				hasWhere = addWhereOrAnd(conditionSql, hasWhere);
				conditionSql.append(spuserprisql);
			}

			return conditionSql.toString();
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "DAO查询下行记录，获取查询条件sql，异常。");
			return " where 1=2 ";
		}
	}
	
	/**
	 * 获取查询时间对象
	 * @param conditionMap 查询条件
	 * @return 返回查询时间对象集合
	 */
	public static List<String> getTimeCondition(LinkedHashMap<String, String> conditionMap) {
		try {
		    int times = 1;
		    // 跨月查询联合了两张表 条件加进去 需要多传入时间一次
		    if(null != conditionMap.get("singleTable") && "false".equals(conditionMap.get("singleTable"))) {
		        times = 3;
            }

			List<String> timeList = new ArrayList<String>();
			for (int i = 0; i < times; i++) {
			if (conditionMap.get("sendtime") != null && conditionMap.get("sendtime").trim().length() > 0) {
				timeList.add(conditionMap.get("sendtime").trim());
			}
			if (conditionMap.get("recvtime") != null && conditionMap.get("recvtime").trim().length() > 0) {
				timeList.add(conditionMap.get("recvtime").trim());
			}
			}
			return timeList;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "DAO查询，获取查询时间对象，异常。");
			return null;
		}
	}

	/**
	 * sql in查询  拆分方法
	 * @param idstr
	 * @param columnstr
	 * @return
	 */
	private static String getSqlStr(String idstr,String columnstr){
		String sql=" 1=2 ";
		if(idstr!=null&&!"".equals(idstr)&&columnstr!=null&&!"".equals(columnstr)){
			if(idstr.contains(",")){
				String[] useriday=idstr.split(",");
				if(useriday.length<900){
					sql=" "+columnstr+" IN ("+idstr+") ";
				}else{
					String zidstr="";
					sql="";
					for(int i=0;i<useriday.length;i++){
						if((i+1)%900==0){
							zidstr=zidstr+useriday[i];
							sql=sql+" "+columnstr +" IN ("+zidstr+") OR ";
							zidstr="";

						}else{
							zidstr=zidstr+useriday[i]+",";
						}
					}
					if(!"".equals(sql)&&"".equals(zidstr)){
						sql=sql.substring(0,sql.lastIndexOf("OR"));
					}else if(!"".equals(sql)&&!"".equals(zidstr)){
						zidstr=zidstr.substring(0, zidstr.length()-1);
						sql=sql+" "+columnstr +" IN ("+zidstr+") ";
					}else{
						sql=" 1=2 ";
					}
				}
			}else{
				sql=" "+columnstr+" = "+idstr;
			}
		}
		return sql;
	}

	public static String getAllSendMttaskFieldSql() {
		StringBuffer sql = new StringBuffer();
		//UNICOM,PHONE,MESSAGE,ERRORCODE,PKNUMBER,PKTOTAL
		sql.append("select mttask.").append(TableMtTask.UNICOM).
			append(",mttask.").append(TableMtTask.PHONE).
			append(",mttask.").append(TableMtTask.MESSAGE).
			append(",mttask.").append(TableMtTask.ERROR_CODE).
			append(",mttask.").append(TableMtTask.PK_NUMBER).
			append(",mttask.").append(TableMtTask.PK_TOTAL);
		return sql.toString();
	}

	public static String getAllSendMttaskTableSql(SendDetailMttaskVo detailMttaskVo) {
		if(detailMttaskVo == null || detailMttaskVo.getTaskId() == null
			|| detailMttaskVo.getSendTime() == null || "".equals(detailMttaskVo.getTaskId())){
			EmpExecutionContext.error("企业短链报表查询-批次发送统计-发送详情查询异常，传入参数有误，关键参数为null");
			return "";
		}
		StringBuffer sql = new StringBuffer();
		//查询全部联合查询
		//根据发送时间查询相应的年月表与实时表(gw_mt_task_bak)
		String yyyyMM = new SimpleDateFormat("yyyyMM").format(detailMttaskVo.getSendTime().getTime());
		String mttaskTableName = "MTTASK" + yyyyMM;
		String fieldSql = "select "+ TableMtTask.UNICOM +","+ TableMtTask.PHONE +
							","+TableMtTask.MESSAGE +","+TableMtTask.ERROR_CODE +
							","+TableMtTask.PK_NUMBER + "," + TableMtTask.PK_TOTAL;
		sql.append(" from (").append(fieldSql).append(" from ").
				append("gw_mt_task_bak").append(StaticValue.getWITHNOLOCK())
				.append(" where ").append(TableMtTask.TASK_ID).
				append("=").append(detailMttaskVo.getTaskId()).
				append(" union all ").append(fieldSql).
				append(" from ").append(mttaskTableName).
				append(StaticValue.getWITHNOLOCK()).append(" where ").
				append(TableMtTask.TASK_ID).append("=").
				append(detailMttaskVo.getTaskId()).append(") mttask");
		return sql.toString();
	}

	public static String getAllSendMttaskConnditionSql(SendDetailMttaskVo detailMttaskVo) {
		if(detailMttaskVo == null || detailMttaskVo.getTaskId() == null
				|| detailMttaskVo.getSendTime() == null || "".equals(detailMttaskVo.getTaskId())){
			EmpExecutionContext.error("企业短链报表查询-批次发送统计-发送详情查询异常，传入参数有误，关键参数为null");
			return "";
		}
		boolean hasWhere = false;
		StringBuffer sql = new StringBuffer();
		//手机号
		if(detailMttaskVo.getPhone() != null && !"".equals(detailMttaskVo.getPhone())){
			hasWhere = addWhereOrAnd(sql,hasWhere);
			sql.append(TableMtTask.PHONE).append("='").append(detailMttaskVo.getPhone()).append("'");
		}
		//运营商
		if(detailMttaskVo.getUnicom() != null){
			hasWhere = addWhereOrAnd(sql,hasWhere);
			sql.append(TableMtTask.UNICOM).append("=").append(detailMttaskVo.getUnicom());
		}
		//错误码
		if(detailMttaskVo.getErrorcode() != null){
			hasWhere = addWhereOrAnd(sql,hasWhere);
			sql.append(TableMtTask.ERROR_CODE).append(detailMttaskVo.getErrorcode());
		}
		return sql.toString();
	}

	private static boolean addWhereOrAnd(StringBuffer sql, boolean hasWhere) {
		if(hasWhere) {
			//有where则加and
			sql.append(" AND ");
		} else {
			//没where则加where
			sql.append(" WHERE ");
		}
		return true;
	}
}
