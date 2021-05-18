package com.montnets.emp.spbalance.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.servmodule.txgl.table.TableUserdata;
import com.montnets.emp.spbalance.vo.UserfeeVo;
import com.montnets.emp.util.PageInfo;

public class SpBalanceDAO extends SuperDAO
{
	

	/**
	 * 查询预付费的sp账号的余额情况
	 * @description    
	 * @param ufvo  查询条件
	 * @param pageInfo 分页对象
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-11 下午07:06:42
	 */
	public List<DynaBean> findSpBalancesByYff(UserfeeVo ufvo, PageInfo pageInfo) throws Exception
	{
		//判断传入参数合法性
		if(ufvo==null||pageInfo==null){
			EmpExecutionContext.error("查询预付费的sp账号的余额情况DAO，传入参数为空");
			return null;
		}
		//查询sql
		StringBuffer sql = null;
		if("100000".equals(ufvo.getCorpcode()))
		{
			sql=new StringBuffer("SELECT UD.USERID,UD.STAFFNAME,UD.SPTYPE,UD.ACCOUNTTYPE,UF.SENDNUM,UF.THRESHOLD FROM USERDATA UD " +
					"LEFT JOIN USERFEE UF ON UD.USERID=UF.USERID WHERE UD."+TableUserdata.UID
					+">100001 AND UD.USERTYPE=0 AND UD.FEEFLAG=1 AND UD.ACCOUNTTYPE=1 ");
		}else
		{
			if(StaticValue.getCORPTYPE() ==0){
				//单企业
				sql=new StringBuffer("SELECT UD.USERID,UD.STAFFNAME,UD.SPTYPE,UD.ACCOUNTTYPE,UF.SENDNUM,UF.THRESHOLD FROM USERDATA UD " +
						"LEFT JOIN USERFEE UF ON UD.USERID=UF.USERID WHERE UD."+TableUserdata.UID
						+">100001 AND UD.USERTYPE=0 AND UD.FEEFLAG=1 AND UD.ACCOUNTTYPE=1 ");
			}else{
				//多企业
				sql=new StringBuffer("SELECT UD.USERID,UD.STAFFNAME,UD.SPTYPE,UD.ACCOUNTTYPE,UF.SENDNUM,UF.THRESHOLD " +
						"FROM (SELECT SPUSER FROM LF_SP_DEP_BIND WHERE CORP_CODE='"+ufvo.getCorpcode()+"' AND IS_VALIDATE=1) LSDB " +
								"LEFT JOIN  USERDATA UD ON LSDB.SPUSER=UD.USERID LEFT JOIN USERFEE UF ON UD.USERID=UF.USERID " +
								"WHERE UD."+TableUserdata.UID+">100001 AND UD.USERTYPE=0 AND UD.FEEFLAG=1 AND UD.ACCOUNTTYPE=1 ");
			}
		}
		
		//查询条件：SPUSER
		if (ufvo.getSpuser() != null && !"".equals(ufvo.getSpuser().trim())) {
			sql.append(" AND UD.USERID LIKE '%"+ufvo.getSpuser().trim()+"%' ");
		}

		//查询条件：信息类型
		if (ufvo.getAccounttype() != null && !"".equals(ufvo.getAccounttype().trim())) {
			sql.append(" AND UD.ACCOUNTTYPE ="+ufvo.getAccounttype().trim()+" ");
		}
		
		//查询条件：应用类型
		if (ufvo.getSptype() != null && !"".equals(ufvo.getSptype().trim())) {
			sql.append(" AND UD.SPTYPE ="+ufvo.getSptype().trim()+" ");
		}

		String countSql = "select count(*) totalcount from ("+sql+" ) A ";
		List<DynaBean> balanceVos = new DataAccessDriver().getGenericDAO()
		.findPageDynaBeanBySQL(sql.toString(), countSql,pageInfo, StaticValue.EMP_POOLNAME,null);
		return balanceVos;
	}
	
	

	
	/**
	 * 通过账号查找通知人信息列表
	 * @description    
	 * @param ufvo
	 * @param pageInfo
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-13 下午08:53:47
	 */
	public List<DynaBean> getSpfeealearmBySpuser(String useridstr) throws Exception
	{
		//判断传入参数合法性
		if(useridstr==null||"".equals(useridstr)){
			EmpExecutionContext.error("查询sp账号相关通知人信息列表信息传入参数为空 useridstr："+useridstr);
			return null;
		}

		//查询字段
		StringBuffer sql=new StringBuffer("select NOTICENAME,ALARMPHONE from LF_SPFEEALARM WHERE SPUSER='"+useridstr
				+"' ");

		List<DynaBean> balanceVos = new DataAccessDriver().getGenericDAO()
		.findDynaBeanBySql(sql.toString());
		return balanceVos;
	}

	
	
	
	
	/**
	 * 获取账号对应的  告警人以及手机号码
	 * @description    
	 * @param corpcode
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-12 下午08:01:26
	 */
	public List<DynaBean> findSpFeeAlarmUserMap(String corpcode) throws Exception
	{

		//查询字段
		StringBuffer sql = new StringBuffer("SELECT SPUSER,NOTICENAME,ALARMPHONE from LF_SPFEEALARM ");
		//企业编码
		sql.append(" WHERE CORP_CODE ='"+corpcode+"' ");
		
		List<DynaBean> alarms = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql.toString());
		return alarms;
	}

	
	public List<DynaBean> findSPBalanceLogBean(
			LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception {
		//从条件中获取输入的值
		//企业编码
		String corpCode = conditionMap.get("corpCode");
		String spUser = conditionMap.get("spUser");
		String msgType = conditionMap.get("msgType");
		String opType = conditionMap.get("opType");
		String result = conditionMap.get("result");
		String opUser = conditionMap.get("opUser");
		String beginTime = conditionMap.get("beginTime");
		String endTime = conditionMap.get("endTime");
		//新增操作员ID条件
		String userId = conditionMap.get("userId");
		//查询条件
		StringBuffer conditionSql = new StringBuffer(" WHERE LF_SPFEE_LOG.CORP_CODE = '").append(corpCode).append("'");

		if(userId != null && !"".equals(userId)){
			conditionSql.append(" AND LF_SPFEE_LOG.USERID = "+userId);
		}

		//过滤掉第一次进入或者无条件查询的语句
		if (spUser != null && !"".equals(spUser)) {
			conditionSql.append(" and LF_SPFEE_LOG.SPUSER like '%").append(spUser).append("%' ").toString();
		}
		//过滤掉第一次进入或者无条件查询的语句
		if (msgType != null && !"".equals(msgType)) {
			conditionSql.append(" and USERDATA.ACCOUNTTYPE =").append(msgType).toString();
		}
		//过滤掉第一次进入或者无条件查询的语句
		if (result != null && !"".equals(result)) {
			conditionSql.append(" and LF_SPFEE_LOG.RESULT =").append(result).toString();
		}
		//过滤掉第一次进入或者无条件查询的语句
		if (opUser != null && !"".equals(opUser)) {
			conditionSql.append(" and LF_SYSUSER.USER_NAME like '%").append(opUser).append("%' ").toString();
		}
		
		if (beginTime != null && !"".equals(beginTime)) {
			String beginTimeFormat = new DataAccessDriver().getGenericDAO()
					.getTimeCondition(beginTime);
			conditionSql.append(" and LF_SPFEE_LOG.OPR_TIME>=").append(beginTimeFormat)  ;
		}
		if (endTime != null && !"".equals(endTime)) {
			String endTimeFormat = new DataAccessDriver().getGenericDAO()
					.getTimeCondition(endTime);
			conditionSql.append(" and LF_SPFEE_LOG.OPR_TIME<=").append(endTimeFormat);
		}
		if (opType != null && Integer.parseInt(opType) == 1) {
			// 充值
			conditionSql.append(" and LF_SPFEE_LOG.ICOUNT>0");
		} else if (opType != null && Integer.parseInt(opType) == -1) {
			// 回收
			conditionSql.append(" and LF_SPFEE_LOG.ICOUNT<0");
		}
		
		String sql = "SELECT LF_SPFEE_LOG.SPUSER, LF_SPFEE_LOG.ICOUNT, LF_SPFEE_LOG.RESULT, LF_SPFEE_LOG.MEMO, LF_SYSUSER.USER_NAME, " +
				" LF_SPFEE_LOG.OPR_TIME, USERDATA.STAFFNAME, USERDATA.SPTYPE, USERDATA.ACCOUNTTYPE, " +
				"LF_DEP.DEP_NAME, LF_SYSUSER.USER_STATE ";
		
		String baseSql = new StringBuffer("FROM LF_SPFEE_LOG" +
				" LEFT JOIN USERDATA ON LF_SPFEE_LOG.SPUSER=USERDATA.USERID" +
				" LEFT JOIN LF_SYSUSER ON LF_SPFEE_LOG.USERID=LF_SYSUSER.USER_ID" +
				" LEFT JOIN LF_DEP ON LF_SYSUSER.DEP_ID=LF_DEP.DEP_ID ").toString();
		
		//String conditionSqlstr = conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		
		String orderSql = " order by LF_SPFEE_LOG.OPR_TIME desc";
		//查询的sql
		sql += baseSql + conditionSql.toString() + orderSql;
		String countSql = "select count(*) totalcount ";
		//查询数量的
		countSql += baseSql + conditionSql.toString();
		//获取动态bean
		List<DynaBean> beanList =  new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql,countSql, pageInfo, StaticValue.EMP_POOLNAME,null);
		//输入日志
		EmpExecutionContext.info("输入的sp账号：" + spUser);
		EmpExecutionContext.info("选择的操作类型：" + opType);
		EmpExecutionContext.info("输入的操作员：" + opUser);
		return beanList;
	}
	
	
	
	
	
	
}
