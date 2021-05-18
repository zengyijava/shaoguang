package com.montnets.emp.charging.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.charging.vo.LfDepBalanceDefVo;
import com.montnets.emp.charging.vo.LfDepBalancePriVo;
import com.montnets.emp.charging.vo.LfDepBalanceVo;
import com.montnets.emp.charging.vo.LfDepRechargeLogVo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfBalancePri;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.sysuser.TableLfBalanceDef;
import com.montnets.emp.table.sysuser.TableLfBalancePri;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDepRechargeLog;
import com.montnets.emp.table.sysuser.TableLfDepUserBalance;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

public class LfDepBalanceDAO extends SuperDAO
{
	
	/**
	 *  查询该机构下的子机构的余额情况
	 * @description    
	 * @param lfSysuser
	 * 	操作员
	 * @param depId
	 *  机构ID
	 * @param depName
	 *  机构名称
	 * @param pageInfo
	 *  分页对象
	 * @return
	 * @throws Exception
	 * 	抛异常       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-8 上午09:50:29
	 */
	public List<LfDepBalanceVo> findLfDepBalanceVos(LfSysuser lfSysuser,
			Long depId, String depName, PageInfo pageInfo) throws Exception
	{
		//查询字段
		String sql = new StringBuffer("select dep.").append(TableLfDep.DEP_ID)
				.append(",dep.").append(TableLfDep.DEP_NAME).append(",dep.")
				.append(TableLfDep.DEP_RESP).append(",balance.").append(
						TableLfDepUserBalance.SMS_BALANCE).append(",balance.")
				.append(TableLfDepUserBalance.SMS_COUNT).append(",balance.")
				.append(TableLfDepUserBalance.MMS_BALANCE).append(",balance.")
				.append(TableLfDepUserBalance.MMS_COUNT).append(",balance.")
				.append(TableLfDepUserBalance.SMS_ALARM_VALUE).append(",balance.")
				.append(TableLfDepUserBalance.MMS_ALARM_VALUE).append(",balance.")
				.append(TableLfDepUserBalance.ALARM_NAME).append(",balance.")
				.append(TableLfDepUserBalance.ALARM_PHONE)
				.toString();
		//计数sql
		String countSql = "select count(*) totalcount";
		//表关联
		String baseSql = new StringBuffer(" from ").append(
				TableLfDep.TABLE_NAME).append(" dep left join ").append(
				TableLfDepUserBalance.TABLE_NAME).append(" balance on dep.")
				.append(TableLfDep.DEP_ID).append(" = ").append(" balance.")
				.append(TableLfDepUserBalance.TARGET_ID).append(" and dep.")
				.append(TableLfDep.CORP_CODE).append(" = ").append(" balance.")
				.append(TableLfDepUserBalance.CORP_CODE).append(" where (dep.")
				.append(TableLfDep.DEP_ID).append(" in (select lfdep.").append(
						TableLfDep.DEP_ID).append(" from ").append(
						TableLfDep.TABLE_NAME).append(" lfdep where lfdep.")
				.append(TableLfDep.SUPERIOR_ID).append(" = ").append(depId)
				.append(" and lfdep.dep_state=1 and lfdep.").append(
						TableLfDep.CORP_CODE).append(" = '").append(
						lfSysuser.getCorpCode()).append("')")
				.append(" or dep.").append(TableLfDep.DEP_ID).append("=")
				.append(lfSysuser.getDepId()).append(")").toString();
		//判断机构名称是否为空
		if (depName != null && !"".equals(depName.trim()))
		{
			baseSql += " and dep." + TableLfDep.DEP_NAME + " like '%"
					+ depName.trim() + "%'";
		}
		//sql
		sql += baseSql;
		countSql += baseSql;
		
		StringBuffer conditionSql = new StringBuffer();
		//加条件
		sql += conditionSql.toString();
		//查询总数
		countSql += conditionSql.toString();

		String orderSql = new StringBuffer("  order by dep.").append(
				TableLfDep.DEP_ID).append(" asc ").toString();
		sql += orderSql;
		//查询余额
		List<LfDepBalanceVo> balanceVos = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
				LfDepBalanceVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return balanceVos;
	}

	/**
	 * 查询该机构下的子机构的余额情况
	 * @description    
	 * @param corpCode 企业编码
	 * @param depId    机构id
	 * @param depName  机构名称
	 * @param pageInfo 分页信息
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-8 上午10:00:45
	 */
	public List<LfDepBalanceVo> findLfDepBalanceVosByAdmin(String corpCode,
			String depId, String depName, PageInfo pageInfo) throws Exception
	{
	//	Integer maxChargeLevel = SystemGlobals.getMaxChargeLevel(corpCode);
		//查询字段
		String sql = new StringBuffer("select dep.").append(TableLfDep.DEP_ID)
				.append(",dep.").append(TableLfDep.DEP_NAME).append(",dep.")
				.append(TableLfDep.DEP_RESP).append(",balance.").append(
						TableLfDepUserBalance.SMS_BALANCE).append(",balance.")
				.append(TableLfDepUserBalance.SMS_COUNT).append(",balance.")
				.append(TableLfDepUserBalance.MMS_BALANCE).append(",balance.")
				.append(TableLfDepUserBalance.MMS_COUNT).append(",balance.")
				.append(TableLfDepUserBalance.SMS_ALARM_VALUE).append(",balance.")
				.append(TableLfDepUserBalance.MMS_ALARM_VALUE).append(",balance.")
				.append(TableLfDepUserBalance.ALARM_NAME).append(",balance.")
				.append(TableLfDepUserBalance.ALARM_PHONE)
				.toString();
		String countSql = "select count(*) totalcount";
		//查询的表
		String baseSql = new StringBuffer(" from ").append(
				TableLfDep.TABLE_NAME).append(" dep left join ").append(
				TableLfDepUserBalance.TABLE_NAME).append(" balance on dep.")
				.append(TableLfDep.DEP_ID).append(" = ").append(" balance.")
				.append(TableLfDepUserBalance.TARGET_ID).append(" and dep.")
				.append(TableLfDep.CORP_CODE).append(" = ").append(" balance.")
				.append(TableLfDepUserBalance.CORP_CODE).append(" where (dep.")
				.append(TableLfDep.DEP_ID).append(" in (select lfdep.").append(
						TableLfDep.DEP_ID).append(" from ").append(
						TableLfDep.TABLE_NAME).append(" lfdep where ")
				//.append(" lfdep."+TableLfDep.DEP_LEVEL).append(" <= ").append(maxChargeLevel + " and ")
						.append("  lfdep.dep_state=1 and lfdep.").append(
						TableLfDep.CORP_CODE).append(" = '").append(corpCode)
				.append("' ").toString();
		//查询条件：机构id
		if (depId != null && !"".equals(depId)) {
			baseSql += " and (lfdep." + TableLfDep.DEP_ID + " =" + depId
					+ " or lfdep." + TableLfDep.SUPERIOR_ID + "=" + depId + ")";
		}

		baseSql += " ))";
		//查询条件：机构名称模糊查询
		if (depName != null && !"".equals(depName.trim())) {
			baseSql += " and dep." + TableLfDep.DEP_NAME + " like '%"
					+ depName.trim() + "%'";
		}

		sql += baseSql;
		countSql += baseSql;
		StringBuffer conditionSql = new StringBuffer();
		sql += conditionSql.toString();
		countSql += conditionSql.toString();
		//排序字段
		String orderSql = new StringBuffer("  order by dep.").append(
				TableLfDep.DEP_ID).append(" asc ").toString();
		sql += orderSql;

		List<LfDepBalanceVo> balanceVos = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				LfDepBalanceVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return balanceVos;
	}

	/*
	 * private List<LfDepBalanceVo> findLfDepBalanceVosSqlServer(Long depId)
	 * throws Exception { select dep.* , balance.* from dbo.LF_DEP dep left join
	 * dbo.LF_DEP_USER_BALANCE balance on dep.DEP_ID = balance.target_id where
	 * dep.DEP_ID in (select lfdep.DEP_ID from dbo.LF_DEP lfdep where
	 * lfdep.SUPERIOR_ID='1'); String sql = new
	 * StringBuffer("select dep.*,balance.* from "
	 * ).append(TableLfDep.TABLE_NAME).append(" dep left join ")
	 * .append(TableLfDepUserBalance
	 * .TABLE_NAME).append(" balance on dep.").append
	 * (TableLfDep.DEP_ID).append(" = ")
	 * .append(" balance.").append(TableLfDepUserBalance
	 * .TARGET_ID).append(" where dep."
	 * ).append(TableLfDep.DEP_ID).append(" in (select lfdep.")
	 * .append(TableLfDep
	 * .DEP_ID).append(" from ").append(TableLfDep.TABLE_NAME).
	 * append(" lfdep where lfdep.").append(TableLfDep.SUPERIOR_ID)
	 * .append(" = '").append(depId).append("' ").toString();
	 * List<LfDepBalanceVo> balanceVos = findVoListBySQL(LfDepBalanceVo.class,
	 * sql, StaticValue.EMP_POOLNAME); return balanceVos; }
	 */

	/**
	 * 查询该机构下的子机构的余额情况
	 * @description    
	 * @param lfSysuser 操作员对象
	 * @param lfDepRechargeLogVo 机构余额对象
	 * @param pageInfo  分页对象
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-8 下午05:39:52
	 */
	public List<LfDepRechargeLogVo> findLfDepBalanceLogVos(LfSysuser lfSysuser,
			LfDepRechargeLogVo lfDepRechargeLogVo, PageInfo pageInfo)
			throws Exception
	{
		// String sql =
		// "select log.*,sysuser.NAME userName,srcdep.DEP_NAME srcName,dstdep.DEP_NAME dstName ";
		//查询字段
		String sql = new StringBuffer(" select log.*,sysuser.").append(
				TableLfSysuser.USER_NAME).append(" userName, sysuser.").append(
				TableLfSysuser.USER_STATE).append(" ,srcdep.").append(
				TableLfDep.DEP_NAME).append(" srcName , dstdep.").append(
				TableLfDep.DEP_NAME).append(" dstName ").toString();
		String countSql = "select count(*) totalcount";
		/*
		 * String baseSql = " from LF_DEP_RECHARGE_LOG log "+
		 * " LEFT JOIN LF_DEP srcdep on log.SRC_TARGETID=srcdep.DEP_ID "+
		 * " LEFT JOIN LF_DEP dstdep on log.DST_TARGETID=dstdep.DEP_ID"+
		 * " LEFT JOIN LF_SYSUSER sysuser on log.OPT_ID=sysuser.GUID ";
		 */
		//查询的表
		String baseSql = new StringBuffer(" from ").append(
				TableLfDepRechargeLog.TABLE_NAME).append(" log LEFT JOIN ")
				.append(TableLfDep.TABLE_NAME).append(" srcdep on log.")
				.append(TableLfDepRechargeLog.SRC_TARGETID)
				.append(" = srcdep.").append(TableLfDep.DEP_ID).append(
						" LEFT JOIN ").append(TableLfDep.TABLE_NAME).append(
						" dstdep on log.").append(
						TableLfDepRechargeLog.DST_TARGETID).append(" =dstdep.")
				.append(TableLfDep.DEP_ID).append(" LEFT JOIN ").append(
						TableLfSysuser.TABLE_NAME).append(" sysuser on log.")
				.append(TableLfDepRechargeLog.OPT_ID).append(" =sysuser.")
				.append(TableLfSysuser.GUID).toString();

		sql += baseSql;
		countSql += baseSql;
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(
				lfSysuser.getUserId());

		String dominationSql = new StringBuffer(" where sysuser.").append(
				TableLfSysuser.USER_ID).append("<>1 and ").append(" (sysuser.")
				.append(TableLfSysuser.USER_ID).append("=").append(
						lfSysuser.getUserId()).append(" or sysuser.").append(
						TableLfSysuser.DEP_ID).append(" in (").append(
						domination).append("))").toString();

		sql += dominationSql;
		countSql += dominationSql;

		StringBuffer conditionSql = new StringBuffer();
		//查询条件：起始时间
		if (lfDepRechargeLogVo.getBeginTime() != null
				&& !"".equals(lfDepRechargeLogVo.getBeginTime())) {
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE) {
				//oracle数据库
				conditionSql.append(" and log.").append(
						TableLfDepRechargeLog.OPT_DATE).append(" >=to_date('")
						.append(lfDepRechargeLogVo.getBeginTime()).append(
								"','yyyy-MM-dd HH24:mi:ss')");
			} else {
				//sqlserver、mysql、db2数据库
				conditionSql.append(" and log.").append(
						TableLfDepRechargeLog.OPT_DATE).append(" >='").append(
						lfDepRechargeLogVo.getBeginTime()).append("'");
			}
		}

		if (lfDepRechargeLogVo.getEndTime() != null
				&& !"".equals(lfDepRechargeLogVo.getEndTime())) {
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE) {
				//oracle数据库
				conditionSql.append(" and log.").append(
						TableLfDepRechargeLog.OPT_DATE).append(" <=to_date('")
						.append(lfDepRechargeLogVo.getEndTime()).append(
								"','yyyy-MM-dd HH24:mi:ss')");
			} else {
				//其他类型数据库
				conditionSql.append(" and log.").append(
						TableLfDepRechargeLog.OPT_DATE).append(" <='").append(
						lfDepRechargeLogVo.getEndTime()).append("'");
			}
		}
		//查询条件：操作员id模糊查询
		if (lfDepRechargeLogVo.getUserName() != null
				&& !"".equals(lfDepRechargeLogVo.getUserName())) {
			conditionSql.append(" and sysuser.").append(
					TableLfSysuser.USER_NAME).append(" like '%").append(
					lfDepRechargeLogVo.getUserName()).append("%'");
		}
		//查询条件：信息类型
		if (lfDepRechargeLogVo.getMsgType() != null
				&& lfDepRechargeLogVo.getMsgType() != 0L) {
			conditionSql.append(" and log.").append(
					TableLfDepRechargeLog.MSG_TYPE).append(" =").append(
					lfDepRechargeLogVo.getMsgType());
		}
		//查询条件：操作结果
		if (lfDepRechargeLogVo.getResult() != null) {
			conditionSql.append(" and log.").append(
					TableLfDepRechargeLog.RESULT).append(" =").append(
					lfDepRechargeLogVo.getResult());
		}

		if (lfDepRechargeLogVo.getCount() != null) {
			if (lfDepRechargeLogVo.getCount() >= 0) {
				conditionSql.append(" and log.").append(
						TableLfDepRechargeLog.COUNT).append(" >").append(0);
			} else {
				conditionSql.append(" and log.").append(
						TableLfDepRechargeLog.COUNT).append(" <").append(0);
			}
		}
		//查询条件：充入机构名称  模糊查询
		if (lfDepRechargeLogVo.getDstName() != null) {
			conditionSql.append(" and upper(dstdep.").append(
					TableLfDep.DEP_NAME).append(") like '%").append(
					lfDepRechargeLogVo.getDstName().toUpperCase()).append("%'");
		}

		sql += conditionSql.toString();
		;
		countSql += conditionSql.toString();
		//排序字段
		String orderSql = " order by log." + TableLfDepRechargeLog.OPT_DATE
				+ " desc";
		sql += orderSql;
		List<LfDepRechargeLogVo> returnList = new DataAccessDriver().getGenericDAO()
				.findPageVoListBySQL(LfDepRechargeLogVo.class, sql, countSql,
						pageInfo, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 * 查询此机构以及其子机构信息
	 * @description    
	 * @param depId 机构id
	 * @return
	 * @throws Exception       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-8 下午05:41:53
	 */
	public List<LfDep> findLfDepAndDepSon(Long depId) throws Exception
	{
		/*
		 * select * from dbo.LF_DEP dep where dep.DEP_ID = '1' or dep.DEP_ID in
		 * (select sondep.DEP_ID from dbo.LF_DEP sondep where sondep.SUPERIOR_ID
		 * = '1');
		 */
		//查询sql
		String sql = new StringBuffer(" select * from ").append(
				TableLfDep.TABLE_NAME).append(" dep where dep.").append(
				TableLfDep.DEP_ID).append(" = '").append(depId).append(
				"' or dep.").append(TableLfDep.DEP_ID).append(
				" in (select sondep.").append(TableLfDep.DEP_ID).append(
				" from ").append(TableLfDep.TABLE_NAME).append(
				" sondep where sondep.").append(TableLfDep.SUPERIOR_ID).append(
				" = '").append(depId).append("')").toString();
		List<LfDep> lfdeps = findEntityListBySQL(LfDep.class, sql,
				StaticValue.EMP_POOLNAME);
		return lfdeps;
	}
	/**
	 * 更新余额
	 * @description    
	 * @param depId 机构id
	 * @param lgcorpcode 企业编码
	 * @param smsAlarm 
	 * @param mmsAlarm
	 * @param name 名称
	 * @param phone 手机号
	 * @return       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-8 下午05:43:10
	 */
	public int updateBalance(long depId,String lgcorpcode,Integer smsAlarm,Integer mmsAlarm,String name,String phone){
		StringBuffer sql=new StringBuffer("update ");
		sql.append(TableLfDepUserBalance.TABLE_NAME).append(" set ");
		sql.append(TableLfDepUserBalance.ALARM_PHONE).append(" = '").append(phone).append("'");
		//短信阀值有改动 则更新阀值及提醒次数
        if(smsAlarm!=null){
			sql.append(",").append(TableLfDepUserBalance.SMS_ALARM_VALUE).append(" = ").append(smsAlarm);
            //更新阀值 已提醒次数清零
            sql.append(",").append(TableLfDepUserBalance.ALARMED_COUNT).append(" = 0 ");
		}
		if(mmsAlarm!=null){
			sql.append(",").append(TableLfDepUserBalance.MMS_ALARM_VALUE).append(" = ").append(mmsAlarm);
            //更新阀值 已提醒次数清零
            sql.append(",").append(TableLfDepUserBalance.MMSALARMEDCOUNT).append(" = 0 ");
		}
		if(name!=null){
			sql.append(",").append(TableLfDepUserBalance.ALARM_NAME).append(" = '").append(name).append("'");
		}
		//默认设置阀值短信提醒次数
		sql.append(",").append(TableLfDepUserBalance.ALARM_COUNT).append(" = ").append(StaticValue.ALARM_COUNT);
		sql.append(" where ").append(TableLfDepUserBalance.CORP_CODE).append(" = '").append(lgcorpcode).append("' and ")
		.append(TableLfDepUserBalance.TARGET_ID).append(" = ").append(depId);
		 int count=0;
		try
		{
			count = executeBySQLReturnCount(sql.toString(), StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新设置短彩信阀值出现异常！");
		}
		return count;
	}
	
	// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
	/**
	 * 查询充值回收权限机构树对应机构的余额情况，此方法经过评审后，决定不使用
	 * 
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	/*
	public List<LfDepBalanceVo> findLfDepBalanceVosByOperator2(Long userId, String corpCode,
			String depId, String depName, PageInfo pageInfo) throws Exception
	{
	//	Integer maxChargeLevel = SystemGlobals.getMaxChargeLevel(corpCode);
		//查询字段
		String sql = new StringBuffer("select dep.").append(TableLfDep.DEP_ID)
				.append(",dep.").append(TableLfDep.DEP_NAME).append(",dep.")
				.append(TableLfDep.DEP_RESP).append(",balance.").append(
						TableLfDepUserBalance.SMS_BALANCE).append(",balance.")
				.append(TableLfDepUserBalance.SMS_COUNT).append(",balance.")
				.append(TableLfDepUserBalance.MMS_BALANCE).append(",balance.")
				.append(TableLfDepUserBalance.MMS_COUNT).append(",balance.")
				.append(TableLfDepUserBalance.SMS_ALARM_VALUE).append(",balance.")
				.append(TableLfDepUserBalance.MMS_ALARM_VALUE).append(",balance.")
				.append(TableLfDepUserBalance.ALARM_NAME).append(",balance.")
				.append(TableLfDepUserBalance.ALARM_PHONE)
				.toString();
		String countSql = "select count(*) totalcount";
		//查询的表
		String baseSql = new StringBuffer(" from ").append(
				TableLfDep.TABLE_NAME).append(" dep left join ").append(
				TableLfDepUserBalance.TABLE_NAME).append(" balance on dep.")
				.append(TableLfDep.DEP_ID).append(" = ").append(" balance.")
				.append(TableLfDepUserBalance.TARGET_ID).append(" and dep.")
				.append(TableLfDep.CORP_CODE).append(" = ").append(" balance.")
				.append(TableLfDepUserBalance.CORP_CODE).append(" where dep.").append(TableLfDep.DEP_ID)
				.append(" = ").append(depId).append(" and ").append(" dep.")
				.append(TableLfDep.DEP_ID).append(" in (select c.").append(
						TableLfBalancePri.DEP_ID).append(" from ").append(
						TableLfBalancePri.TABLE_NAME).append(" c where ")
				//.append(" lfdep."+TableLfDep.DEP_LEVEL).append(" <= ").append(maxChargeLevel + " and ")
						.append("  c.").append(
						TableLfBalancePri.USER_ID).append(" = ").append(userId)
				.append(" ) ").toString();
		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//查询条件：机构名称模糊查询
		if (depName != null && !"".equals(depName.trim())) {
			baseSql += " and dep." + TableLfDep.DEP_NAME + " like '%"
					+ depName.trim() + "%'";
		}

		sql += baseSql;
		countSql += baseSql;
		StringBuffer conditionSql = new StringBuffer();
		sql += conditionSql.toString();
		countSql += conditionSql.toString();
		
		//排序字段
		//String orderSql = new StringBuffer("  order by dep.").append(
		//		TableLfDep.DEP_ID).append(" asc ").toString();
		//sql += orderSql;
		
		//System.out.println("aaaaaaaaaaaaaaaaaaaaa"+sql);

		List<LfDepBalanceVo> balanceVos = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				LfDepBalanceVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return balanceVos;
	}
	*/
	
	/**
	 * 查询充值回收权限机构树对应机构的余额情况
	 * 
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	/**
	 * 查询该机构下的子机构的余额情况
	 * 
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<LfDepBalanceVo> findLfDepBalanceVosByOperator(Long userId, String corpCode,
			String depId, String depName, PageInfo pageInfo) throws Exception
	{
	//	Integer maxChargeLevel = SystemGlobals.getMaxChargeLevel(corpCode);
		//查询字段
		String sql = new StringBuffer("select dep.").append(TableLfDep.DEP_ID)
				.append(",dep.").append(TableLfDep.DEP_NAME).append(",dep.")
				.append(TableLfDep.DEP_RESP).append(",balance.").append(
						TableLfDepUserBalance.SMS_BALANCE).append(",balance.")
				.append(TableLfDepUserBalance.SMS_COUNT).append(",balance.")
				.append(TableLfDepUserBalance.MMS_BALANCE).append(",balance.")
				.append(TableLfDepUserBalance.MMS_COUNT).append(",balance.")
				.append(TableLfDepUserBalance.SMS_ALARM_VALUE).append(",balance.")
				.append(TableLfDepUserBalance.MMS_ALARM_VALUE).append(",balance.")
				.append(TableLfDepUserBalance.ALARM_NAME).append(",balance.")
				.append(TableLfDepUserBalance.ALARM_PHONE)
				.toString();
		String countSql = "select count(*) totalcount";
		//查询的表
		String baseSql = new StringBuffer(" from ").append(
				TableLfDep.TABLE_NAME).append(" dep left join ").append(
				TableLfDepUserBalance.TABLE_NAME).append(" balance on dep.")
				.append(TableLfDep.DEP_ID).append(" = ").append(" balance.")
				.append(TableLfDepUserBalance.TARGET_ID).append(" and dep.")
				.append(TableLfDep.CORP_CODE).append(" = ").append(" balance.")
				.append(TableLfDepUserBalance.CORP_CODE).append(" where (dep.")
				.append(TableLfDep.DEP_ID).append(" in (select lfdep.").append(
						TableLfDep.DEP_ID).append(" from ").append(
						TableLfDep.TABLE_NAME).append(" lfdep where ")
				//.append(" lfdep."+TableLfDep.DEP_LEVEL).append(" <= ").append(maxChargeLevel + " and ")
						.append("  lfdep.dep_state=1 and lfdep.").append(
						TableLfDep.CORP_CODE).append(" = '").append(corpCode)
				.append("' ").toString();
		//查询条件：机构id
		if (depId != null && !"".equals(depId)) {
			baseSql += " and (lfdep." + TableLfDep.DEP_ID + " =" + depId
					+ " or lfdep." + TableLfDep.SUPERIOR_ID + "=" + depId + ")";
		}

	
			
		baseSql += " ))";
		
		String baseSql2 = new StringBuffer(" and (dep.").append(TableLfDep.DEP_ID).append(" in ( ")
		.append(getOperatorBalancePriDepsSql(userId.toString(), depId, corpCode)).append(" )) ").toString();
		
		baseSql += baseSql2; 
		//查询条件：机构名称模糊查询
		if (depName != null && !"".equals(depName.trim())) {
			baseSql += " and dep." + TableLfDep.DEP_NAME + " like '%"
					+ depName.trim() + "%'";
		}
		
		

		sql += baseSql;
		countSql += baseSql;
		StringBuffer conditionSql = new StringBuffer();
		sql += conditionSql.toString();
		countSql += conditionSql.toString();
		//排序字段
		String orderSql = new StringBuffer("  order by dep.").append(
				TableLfDep.DEP_ID).append(" asc ").toString();
		sql += orderSql;
		
		

		List<LfDepBalanceVo> balanceVos = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				LfDepBalanceVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return balanceVos;
	}
	
	
	/**
	 * 通过机构权限中的机构，查出其所有父机构，此处为完成此查询的sql语句。这里权限表包括了默认机构，评审后决定不使用
	 * @param userId 当前操作员的ID
	 * @param depId 机构id
	 * @return 机构的集合
	 * @throws Exception  			 
	 * @author pengj
	 * @datetime 2015-12-1 上午15:00:00
	 */
	/*
	public String getOperatorBalancePriDepsSql1(String userId,
			String depId, String corpCode) throws Exception
	{	
		String sql = "";
	
		String sql1 = "";
		//通过userid查出充值回收权限表中指定的机构，通过这些机构，查出LfDep表中对应的机构
		sql1 = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfDep.CORP_CODE).append(" = ").append(corpCode)
		.append(" AND ").append(TableLfDep.DEP_ID).append(" IN (SELECT ")
		.append(TableLfBalancePri.DEP_ID).append(" FROM ").append(TableLfBalancePri.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfBalancePri.CORP_CODE).append(" = ").append(corpCode)
		.append(" AND ").append(TableLfBalancePri.USER_ID).append(" = ").append(userId).append(")").toString();
		System.out.println("--------通过userid查出充值回收权限表中指定的机构，通过这些机构，查出LfDep表中对应的机构---------" + sql1);
		List<LfDep> lfDepList1 = findEntityListBySQL(
					LfDep.class, sql1, StaticValue.EMP_POOLNAME);
		
		String depIdsForDepPath = "";
		if(lfDepList1 != null && lfDepList1.size() > 0){
			String[] depIdArray = null;
			String lfDepDepPath = null;
			StringBuffer lfDepDepPathSb = new StringBuffer();
			for(LfDep lfDep : lfDepList1 ){
				lfDepDepPath = lfDep.getDeppath();
				lfDepDepPathSb.append(lfDepDepPath);
			}
			//去掉最后一个"/"
			lfDepDepPathSb.deleteCharAt(lfDepDepPathSb.length()-1);
			
			depIdArray = lfDepDepPathSb.toString().split("/");
			List<String> depIdList = new ArrayList<String>();
			//过滤重复元素
			for(int i=0; i<depIdArray.length; i++){
				if(!depIdList.contains(depIdArray[i])){
					depIdList.add(depIdArray[i]);
				}
			}
			depIdsForDepPath = depIdList.toString();
			depIdsForDepPath = depIdsForDepPath.replace("[","");
			depIdsForDepPath = depIdsForDepPath.replace("]","");
		}
		sql = new StringBuffer("SELECT DEP_ID FROM ").append(TableLfDep.TABLE_NAME)
		.append(" " + StaticValue.WITHNOLOCK)
		.append(" WHERE DEP_ID IN (").append(depIdsForDepPath).append(")").toString();
		return sql;
			
		}
	*/
	/**
	 * 通过机构权限中的机构，查出其所有父机构，此处为完成此查询的sql语句，存在bug
	 * @param userId 当前操作员的ID
	 * @param depId 机构id
	 * @return 机构的集合
	 * @throws Exception  			 
	 * @author pengj
	 * @datetime 2015-12-1 上午15:00:00
	 */
	/*
	public String getOperatorBalancePriDepsSql1(String userId,
			String depId, String corpCode) throws Exception
	{	
		String sql = "";
	
		String sql1 = "";
		//通过userid查出充值回收权限表中指定的机构以及默认机构，通过这些机构，查出LfDep表中对应的机构
		sql1 = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfDep.DEP_STATE).append(" = 1 ")
		.append(" AND ").append(TableLfDep.DEP_ID).append(" IN (SELECT ")
		.append(TableLfBalancePri.DEP_ID).append(" FROM ").append(TableLfBalancePri.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfBalancePri.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfBalancePri.USER_ID).append(" = ").append(userId).append(")")
		.append(" OR ( ").append(TableLfDep.SUPERIOR_ID).append(" = ( ")
		.append(" SELECT ").append(TableLfSysuser.DEP_ID).append(" FROM ").append(TableLfSysuser.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfSysuser.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfSysuser.USER_ID).append(" = ").append(userId).append(" ))").toString();
		//System.out.println("--------通过userid查出充值回收权限表中指定的机构，通过这些机构，查出LfDep表中对应的机构---------" + sql1);
		List<LfDep> lfDepList1 = findEntityListBySQL(
					LfDep.class, sql1, StaticValue.EMP_POOLNAME);
		
		String depIdsForDepPath = "";
		if(lfDepList1 != null && lfDepList1.size() > 0){
			String[] depIdArray = null;
			String lfDepDepPath = null;
			StringBuffer lfDepDepPathSb = new StringBuffer();
			for(LfDep lfDep : lfDepList1 ){
				lfDepDepPath = lfDep.getDeppath();
				lfDepDepPathSb.append(lfDepDepPath);
			}
			//去掉最后一个"/"
			lfDepDepPathSb.deleteCharAt(lfDepDepPathSb.length()-1);
			
			depIdArray = lfDepDepPathSb.toString().split("/");
			List<String> depIdList = new ArrayList<String>();
			//过滤重复元素
			for(int i=0; i<depIdArray.length; i++){
				if(!depIdList.contains(depIdArray[i])){
					depIdList.add(depIdArray[i]);
				}
			}
			depIdsForDepPath = depIdList.toString();
			depIdsForDepPath = depIdsForDepPath.replace("[","");
			depIdsForDepPath = depIdsForDepPath.replace("]","");
		}
		sql = new StringBuffer("SELECT ").append(TableLfDep.DEP_ID).append(" FROM ").append(TableLfDep.TABLE_NAME)
		.append(" " + StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfDep.CORP_CODE).append(" = ").append(corpCode).append(" AND ")
		//未进行IN拆分  备份
		//.append(TableLfDep.DEP_ID).append(" IN ( ").append(depIdsForDepPath).append(" )")
		//
		//使用IN拆分  新增一行
		.append(" ( ").append(getSqlStr(depIdsForDepPath, TableLfDep.DEP_ID)).append(" ) ")
		//end
		.toString();
		return sql;
			
		}
	*/
	/**
	 * 通过机构权限中的机构，查出其所有父机构，此处为完成此查询的sql语句，修正bug
	 * @param userId 当前操作员的ID
	 * @param depId 机构id
	 * @return 机构的集合
	 * @throws Exception  			 
	 * @author pengj
	 * @datetime 2015-12-1 上午15:00:00
	 */
	public String getOperatorBalancePriDepsSql(String userId,
			String depId, String corpCode) throws Exception
	{	
		String sql = "";
	
		String sql1 = "";
		//通过userid查出充值回收权限表中指定的机构以及默认机构，通过这些机构，查出LfDep表中对应的机构
		sql1 = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
		.append(" ").append(StaticValue.getWITHNOLOCK())
		.append(" WHERE ").append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfDep.DEP_STATE).append(" = 1 ")
		.append(" AND ").append(TableLfDep.DEP_ID).append(" IN (SELECT ")
		.append(TableLfBalancePri.DEP_ID).append(" FROM ").append(TableLfBalancePri.TABLE_NAME)
		.append(" ").append(StaticValue.getWITHNOLOCK())
		.append(" WHERE ").append(TableLfBalancePri.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfBalancePri.USER_ID).append(" = ").append(userId).append(")")
		.append(" OR ( ").append(TableLfDep.SUPERIOR_ID).append(" = ( ")
		.append(" SELECT ").append(TableLfSysuser.DEP_ID).append(" FROM ").append(TableLfSysuser.TABLE_NAME)
		.append(" ").append(StaticValue.getWITHNOLOCK())
		.append(" WHERE ").append(TableLfSysuser.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfSysuser.USER_ID).append(" = ").append(userId).append(" ))")
		.append(" OR ( ").append(TableLfDep.DEP_ID).append(" = ( ")
		.append(" SELECT ").append(TableLfSysuser.DEP_ID).append(" FROM ").append(TableLfSysuser.TABLE_NAME)
		.append(" ").append(StaticValue.getWITHNOLOCK())
		.append(" WHERE ").append(TableLfSysuser.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfSysuser.USER_ID).append(" = ").append(userId).append(" ))").toString();
		//System.out.println("--------通过userid查出充值回收权限表中指定的机构，通过这些机构，查出LfDep表中对应的机构---------" + sql1);
		List<LfDep> lfDepList1 = findEntityListBySQL(
					LfDep.class, sql1, StaticValue.EMP_POOLNAME);
		
		String depIdsForDepPath = "";
		if(lfDepList1 != null && lfDepList1.size() > 0){
			String[] depIdArray = null;
			String lfDepDepPath = null;
			StringBuffer lfDepDepPathSb = new StringBuffer();
			for(LfDep lfDep : lfDepList1 ){
				lfDepDepPath = lfDep.getDeppath();
				lfDepDepPathSb.append(lfDepDepPath);
			}
			//去掉最后一个"/"
			lfDepDepPathSb.deleteCharAt(lfDepDepPathSb.length()-1);
			
			depIdArray = lfDepDepPathSb.toString().split("/");
			List<String> depIdList = new ArrayList<String>();
			//过滤重复元素
			for(int i=0; i<depIdArray.length; i++){
				if(!depIdList.contains(depIdArray[i])){
					depIdList.add(depIdArray[i]);
				}
			}
			depIdsForDepPath = depIdList.toString();
			depIdsForDepPath = depIdsForDepPath.replace("[","");
			depIdsForDepPath = depIdsForDepPath.replace("]","");
		}
		sql = new StringBuffer("SELECT ").append(TableLfDep.DEP_ID).append(" FROM ").append(TableLfDep.TABLE_NAME)
		.append(" " + StaticValue.getWITHNOLOCK())
		.append(" WHERE ").append(TableLfDep.CORP_CODE).append(" = ").append(corpCode).append(" AND ")
		/*未进行IN拆分  备份
		.append(TableLfDep.DEP_ID).append(" IN ( ").append(depIdsForDepPath).append(" )")
		*/
		//使用IN拆分  新增一行
		.append(" ( ").append(getSqlStr(depIdsForDepPath, TableLfDep.DEP_ID)).append(" ) ")
		//end
		.toString();
		return sql;
			
		}
	
	/**
	 * 获取操作员拥有的所有充值回收权限机构id的list（仅包括操作员表在权限表中的机构id），评审后决定不使用
	 * @param userId 当前操作员的ID
	 * @param depId 机构id
	 * @return 机构的集合
	 * @throws Exception  			 
	 * @author pengj
	 * @datetime 2015-12-3 下午15:00:00
	 */
	/*
	public List<Long> getBalancePriDepsIds1(String userId) throws Exception
	{
		String sql = "";
		List<Long> balancePridepIdsList = new ArrayList<Long>();
		//通过userid查出充值回收权限表中指定的机构，通过这些机构，查出LfDep表中对应的机构
		sql = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfDep.DEP_ID).append(" IN (SELECT ")
		.append(TableLfBalancePri.DEP_ID).append(" FROM ").append(TableLfBalancePri.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfBalancePri.USER_ID).append(" = ").append(userId).append(")").toString();
		List<LfDep> lfDepList = findEntityListBySQL(
					LfDep.class, sql, StaticValue.EMP_POOLNAME);
		for(LfDep lfdep : lfDepList){
			balancePridepIdsList.add(lfdep.getDepId());	
		}
		return balancePridepIdsList;
	}	
	*/
	
	/**
	 * 获取操作员拥有的所有充值回收权限机构id的list（包括操作员表在权限表中的机构id和操作员默认的充值回收权限机构id）
	 * @param userId 当前操作员的ID
	 * @param depId 机构id
	 * @return 机构的集合
	 * @throws Exception  			 
	 * @author pengj
	 * @datetime 2015-12-3 下午15:00:00
	 */
	public List<Long> getBalancePriDepsIds(String userId, String depId, String corpCode) throws Exception
	{
		String sql = "";
		List<Long> balancePridepIdsList = new ArrayList<Long>();
		//通过userid查出充值回收权限表中指定的机构，通过这些机构，查出LfDep表中对应的机构
		sql = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
		.append(" ").append(StaticValue.getWITHNOLOCK())
		.append(" WHERE ").append(TableLfDep.CORP_CODE).append(" = ").append(corpCode)
		.append(" AND ").append(TableLfDep.DEP_STATE).append(" = 1 ")
		.append(" AND ").append(TableLfDep.DEP_ID).append(" IN (SELECT ")
		.append(TableLfBalancePri.DEP_ID).append(" FROM ").append(TableLfBalancePri.TABLE_NAME)
		.append(" ").append(StaticValue.getWITHNOLOCK())
		.append(" WHERE ").append(TableLfBalancePri.USER_ID).append(" = ").append(userId).append(")")
		.append(" OR ( ").append(TableLfDep.SUPERIOR_ID).append(" = ").append(depId)
		.append(" AND ").append(TableLfDep.CORP_CODE).append(" = ").append(corpCode)
		.append(" AND ").append(TableLfDep.DEP_STATE).append(" = 1 ").append(" )").toString();
		List<LfDep> lfDepList = findEntityListBySQL(
					LfDep.class, sql, StaticValue.EMP_POOLNAME);
		for(LfDep lfdep : lfDepList){
			balancePridepIdsList.add(lfdep.getDepId());	
		}
		return balancePridepIdsList;
	}	
	
	// EMP5.7新需求：增加对操作员充值和回收权限   by pengj	
	
	/**
	 * 通过机构权限中的机构，查出其所有父机构，用于创建充值回收权限的机构树，本方法是默认权限也在权限表中，评审后不允许默认权限放在权限表。所以此方法不用
	 * @param userId 当前操作员的ID
	 * @param depId 机构id
	 * @return 机构的集合
	 * @throws Exception  			 
	 * @author pengj
	 * @datetime 2015-11-25 上午15:00:00
	 */
	/*
	public List<LfDep> getOperatorBalancePriDeps1(String userId,
			String depId, String corpCode) throws Exception
	{	
		String sql = "";
	
		String sql1 = "";
		//通过userid查出充值回收权限表中指定的机构，通过这些机构，查出LfDep表中对应的机构
		sql1 = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfDep.CORP_CODE).append(" = ").append(corpCode)
		.append(" AND ").append(TableLfDep.DEP_ID).append(" IN (SELECT ")
		.append(TableLfBalancePri.DEP_ID).append(" FROM ").append(TableLfBalancePri.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfBalancePri.CORP_CODE).append(" = ").append(corpCode)
		.append(" AND ").append(TableLfBalancePri.USER_ID).append(" = ").append(userId).append(")").toString();
		List<LfDep> lfDepList1 = findEntityListBySQL(
					LfDep.class, sql1, StaticValue.EMP_POOLNAME);
		
		String depIds = "";
		String depIdsForDepPath = "";
		if(lfDepList1 != null && lfDepList1.size() > 0){
			String[] depIdArray = null;
			String lfDepDepPath = null;
			Long lfDepDepId = null;
			StringBuffer lfDepDepPathSb = new StringBuffer();
			StringBuffer lfDepDepIdSb = new StringBuffer();
			for(LfDep lfDep : lfDepList1 ){
				lfDepDepPath = lfDep.getDeppath();
				lfDepDepId = lfDep.getDepId();
				lfDepDepPathSb.append(lfDepDepPath);
				lfDepDepIdSb.append(lfDepDepId).append(",");
			}
			//去掉最后一个"/"
			lfDepDepPathSb.deleteCharAt(lfDepDepPathSb.length()-1);
			//去掉最后一个","
			lfDepDepIdSb.deleteCharAt(lfDepDepIdSb.length()-1);
			depIds = lfDepDepIdSb.toString();
			
			depIdArray = lfDepDepPathSb.toString().split("/");
			List<String> depIdList = new ArrayList<String>();
			//过滤重复元素
			for(int i=0; i<depIdArray.length; i++){
				if(!depIdList.contains(depIdArray[i])){
					depIdList.add(depIdArray[i]);
				}
			}
			depIdsForDepPath = depIdList.toString();
			depIdsForDepPath = depIdsForDepPath.replace("[","");
			depIdsForDepPath = depIdsForDepPath.replace("]","");
		}
		if (depId == null || "".equals(depId))
			{
			sql = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
			.append(" " + StaticValue.WITHNOLOCK)
			.append(" WHERE DEP_ID IN (").append(depIdsForDepPath).append(")").toString();
				
			
		}else
		{
				sql = new StringBuffer(" select * from ").append(
						TableLfDep.TABLE_NAME).append(
						" " + StaticValue.WITHNOLOCK).append(" where ")
						.append(TableLfDep.CORP_CODE).append(" = ").append(corpCode)
						.append(" AND ").append(
						TableLfDep.SUPERIOR_ID).append(" = ").append(depId)
						.append(" AND ").append(TableLfDep.DEP_ID)
						.append(" IN (").append(depIds).append(")")
						.toString();
				
		}
		List<LfDep> lfDepList = findEntityListBySQL(
				LfDep.class, sql, StaticValue.EMP_POOLNAME);
		return lfDepList;
	}
	*/
	/**
	 * 通过机构权限中的机构，查出其所有父机构，用于创建充值回收权限的机构树，本方法是默认权限不在权限表中，该方法可以优化，此版本暂不使用
	 * @param userId 当前操作员的ID
	 * @param depId 机构id
	 * @return 机构的集合
	 * @throws Exception  			 
	 * @author pengj
	 * @datetime 2015-11-25 上午15:00:00
	 */
	/*
	public List<LfDep> getOperatorBalancePriDeps(String userId,
			String depId, String corpCode) throws Exception
	{	
		String sql = "";
	
		String sql1 = "";
		
		//通过userid查出充值回收权限表中指定的机构，通过这些机构，查出LfDep表中对应的机构
		sql1 = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfDep.CORP_CODE).append(" = ").append(corpCode)
		.append(" AND ").append(TableLfDep.DEP_ID).append(" IN (SELECT ")
		.append(TableLfBalancePri.DEP_ID).append(" FROM ").append(TableLfBalancePri.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfBalancePri.CORP_CODE).append(" = ").append(corpCode)
		.append(" AND ").append(TableLfBalancePri.USER_ID).append(" = ").append(userId).append(")").toString();
		List<LfDep> lfDepList1 = findEntityListBySQL(
					LfDep.class, sql1, StaticValue.EMP_POOLNAME);
		
		//通过UserId查出该操作员所属机构的所有第一级子机构
		List<LfDep> firstLevelChildDeps = getFirstLevelChildDeps(userId, corpCode);
		
		//由depPath构成的depId字符串
		String depIdsForDepPath = "";
		//由充值权限机构id构成的字符串
		String depIdsForBalance = "";
		//由depPath构成的depId数组
		String[] depPathIdArray = null;
		//由充值权限机构id构成的数组
		String[] balanceDepIdArray = null;
		
		//用于拼接由depPath构成的depId
		StringBuffer lfDepPathSb = new StringBuffer();
		//用于拼接充值权限机构id
		StringBuffer lfBalanceDepIdSb = new StringBuffer();
		
		if(lfDepList1 != null && lfDepList1.size() > 0){
			
			String lfDepDepPath = null;
			Long lfBalanceDepId = null;
			
			for(LfDep lfDep : lfDepList1 ){
				lfDepDepPath = lfDep.getDeppath();
				lfBalanceDepId = lfDep.getDepId();
				lfDepPathSb.append(lfDepDepPath);
				lfBalanceDepIdSb.append(lfBalanceDepId).append(",");
			}
		}
		if(firstLevelChildDeps != null && firstLevelChildDeps.size() > 0){
			
			String firstLevelChildDepPath = null;
			Long firstLevelChildDepId = null;
			
			for(LfDep firstLevelChildDep : firstLevelChildDeps ){
				firstLevelChildDepPath = firstLevelChildDep.getDeppath();
				firstLevelChildDepId = firstLevelChildDep.getDepId();
				lfDepPathSb.append(firstLevelChildDepPath);
				lfBalanceDepIdSb.append(firstLevelChildDepId).append(",");
			}
		}
		//去掉最后一个"/"
		lfDepPathSb.deleteCharAt(lfDepPathSb.length()-1);
		//去掉最后一个","
		lfBalanceDepIdSb.deleteCharAt(lfBalanceDepIdSb.length()-1);
		
		//由depPath构成的depId数组
		depPathIdArray = lfDepPathSb.toString().split("/");
		//由充值权限机构id构成的数组
		balanceDepIdArray = lfBalanceDepIdSb.toString().split(",");
		//用于过滤depPath构成depId的list
		List<String> depIdListForDepPath = new ArrayList<String>();
		//用于过滤充值回收权限机构id的list
		List<String> depIdListForBalance = new ArrayList<String>();
			
		//过滤重复的元素（depPath构成depId）
		for(int i=0; i<depPathIdArray.length; i++){
			if(!depIdListForDepPath.contains(depPathIdArray[i])){
				depIdListForDepPath.add(depPathIdArray[i]);
			}
		}
		//过滤重复的元素（充值回收权限机构id）
		for(int i=0; i<balanceDepIdArray.length; i++){
			if(!depIdListForBalance.contains(balanceDepIdArray[i])){
				depIdListForBalance.add(balanceDepIdArray[i]);
			}
		}
		//由depPath构成的depId字符串，并去掉特殊字符
		depIdsForDepPath = depIdListForDepPath.toString();
		depIdsForDepPath = depIdsForDepPath.replace("[","");
		depIdsForDepPath = depIdsForDepPath.replace("]","");
		depIdsForDepPath = depIdsForDepPath.replace(" ","");
		
		//由充值权限机构id构成的字符串，并去掉特殊字符
		depIdsForBalance = depIdListForBalance.toString();
		depIdsForBalance = depIdsForDepPath.replace("[","");
		depIdsForBalance = depIdsForDepPath.replace("]","");
		depIdsForBalance = depIdsForDepPath.replace(" ","");
		
		
		
		if (depId == null || "".equals(depId))
			{
			sql = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
			.append(" " + StaticValue.WITHNOLOCK)
			.append(" WHERE DEP_ID IN (").append(depIdsForDepPath).append(")").toString();
				
			
		}else
		{
				sql = new StringBuffer(" select * from ").append(
						TableLfDep.TABLE_NAME).append(
						" " + StaticValue.WITHNOLOCK).append(" where ")
						.append(TableLfDep.CORP_CODE).append(" = ").append(corpCode)
						.append(" AND ").append(
						TableLfDep.SUPERIOR_ID).append(" = ").append(depId)
						.append(" AND ").append(TableLfDep.DEP_ID)
						.append(" IN (").append(depIdsForBalance).append(")")
						.toString();
				
		}
		//System.out.println("pppppppppppppppppppppppppp-充值回收权限树-------"+sql);
		List<LfDep> lfDepList = findEntityListBySQL(
				LfDep.class, sql, StaticValue.EMP_POOLNAME);
		return lfDepList;
	}
	*/
	
	/**
	 * 通过机构权限中的机构，查出其所有父机构，用于创建充值回收权限的机构树，本方法是默认权限不在权限表中，此方法已优化，存在机构没有子机构，则不显示树的bug，需修改
	 * @param userId 当前操作员的ID
	 * @param depId 机构id
	 * @return 机构的集合
	 * @throws Exception  			 
	 * @author pengj
	 * @datetime 2015-11-25 上午15:00:00
	 */
	/*
	public List<LfDep> getOperatorBalancePriDeps(String userId,
			String depId, String corpCode, String sysDepId) throws Exception
	{	
		String sql = "";
	
		String sql1 = "";
		
		//通过userid查出充值回收权限表中指定的机构，通过这些机构，查出LfDep表中对应的机构
		sql1 = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfDep.DEP_ID).append(" IN (SELECT ")
		.append(TableLfBalancePri.DEP_ID).append(" FROM ").append(TableLfBalancePri.TABLE_NAME)
		.append(" ").append(StaticValue.WITHNOLOCK)
		.append(" WHERE ").append(TableLfBalancePri.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfBalancePri.USER_ID).append(" = ").append(userId).append(")")
		.append(" OR ( ").append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfDep.DEP_STATE).append(" = 1 ")
		.append(" AND ").append(TableLfDep.SUPERIOR_ID).append(" = ").append(sysDepId).append(" )").toString();
		List<LfDep> lfDepList1 = findEntityListBySQL(
					LfDep.class, sql1, StaticValue.EMP_POOLNAME);
		
		//通过UserId查出该操作员所属机构的所有第一级子机构
		//List<LfDep> firstLevelChildDeps = getFirstLevelChildDeps(userId, corpCode);
		
		//由depPath构成的depId字符串
		String depIdsForDepPath = "";
		//由充值权限机构id构成的字符串
		String depIdsForBalance = "";
		//由depPath构成的depId数组
		String[] depPathIdArray = null;
		//由充值权限机构id构成的数组
		String[] balanceDepIdArray = null;
		
		//用于拼接由depPath构成的depId
		StringBuffer lfDepPathSb = new StringBuffer();
		//用于拼接充值权限机构id
		StringBuffer lfBalanceDepIdSb = new StringBuffer();
		
		if(lfDepList1 != null && lfDepList1.size() > 0){
			
			String lfDepDepPath = null;
			Long lfBalanceDepId = null;
			
			for(LfDep lfDep : lfDepList1 ){
				lfDepDepPath = lfDep.getDeppath();
				lfBalanceDepId = lfDep.getDepId();
				lfDepPathSb.append(lfDepDepPath);
				lfBalanceDepIdSb.append(lfBalanceDepId).append(",");
			}
		}
		
//		if(firstLevelChildDeps != null && firstLevelChildDeps.size() > 0){
//			
//			String firstLevelChildDepPath = null;
//			Long firstLevelChildDepId = null;
//			
//			for(LfDep firstLevelChildDep : firstLevelChildDeps ){
//				firstLevelChildDepPath = firstLevelChildDep.getDeppath();
//				firstLevelChildDepId = firstLevelChildDep.getDepId();
//				lfDepPathSb.append(firstLevelChildDepPath);
//				lfBalanceDepIdSb.append(firstLevelChildDepId).append(",");
//			}
//		}
		
		//去掉最后一个"/"
		if(lfDepPathSb.length() > 0 ){
			lfDepPathSb.deleteCharAt(lfDepPathSb.length()-1);
		}
		//去掉最后一个","
		if(lfBalanceDepIdSb.length() > 0 ){
			lfBalanceDepIdSb.deleteCharAt(lfBalanceDepIdSb.length()-1);
		}
		//由depPath构成的depId数组
		depPathIdArray = lfDepPathSb.toString().split("/");
		//由充值权限机构id构成的数组
		balanceDepIdArray = lfBalanceDepIdSb.toString().split(",");
		//用于过滤depPath构成depId的list
		List<String> depIdListForDepPath = new ArrayList<String>();
		//用于过滤充值回收权限机构id的list
		List<String> depIdListForBalance = new ArrayList<String>();
			
		//过滤重复的元素（depPath构成depId）
		for(int i=0; i<depPathIdArray.length; i++){
			if(!depIdListForDepPath.contains(depPathIdArray[i])){
				depIdListForDepPath.add(depPathIdArray[i]);
			}
		}
		//过滤重复的元素（充值回收权限机构id）
		for(int i=0; i<balanceDepIdArray.length; i++){
			if(!depIdListForBalance.contains(balanceDepIdArray[i])){
				depIdListForBalance.add(balanceDepIdArray[i]);
			}
		}
		//由depPath构成的depId字符串，并去掉特殊字符
		depIdsForDepPath = depIdListForDepPath.toString();
		depIdsForDepPath = depIdsForDepPath.replace("[","");
		depIdsForDepPath = depIdsForDepPath.replace("]","");
		depIdsForDepPath = depIdsForDepPath.replace(" ","");
		
		//由充值权限机构id构成的字符串，并去掉特殊字符
		depIdsForBalance = depIdListForBalance.toString();
		depIdsForBalance = depIdsForDepPath.replace("[","");
		depIdsForBalance = depIdsForDepPath.replace("]","");
		depIdsForBalance = depIdsForDepPath.replace(" ","");
		
		
		
		if (depId == null || "".equals(depId))
			{
			sql = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
			.append(" " + StaticValue.WITHNOLOCK).append(" WHERE ")
			.append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
			.append("' AND ")
//			原先没有解决IN拆分   备份
//			.append(TableLfDep.DEP_ID).append(" IN ( ").append(depIdsForDepPath).append(" ) ")
//			
			//解决了IN拆分的 新增一行  IN中超过1000则进行IN拆分
			.append(" ( ").append(getSqlStr(depIdsForDepPath, TableLfDep.DEP_ID)).append(" ) ")
			//end
			.append(" ORDER BY ").append(TableLfDep.DEP_NAME).append(" ASC ")
			.toString();
				
			
		}else
		{
				sql = new StringBuffer(" select * from ").append(
						TableLfDep.TABLE_NAME).append(
						" " + StaticValue.WITHNOLOCK).append(" where ")
						.append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
						.append("' AND ").append(
						TableLfDep.SUPERIOR_ID).append(" = ").append(depId)
						.append(" AND ")
						//原先没有解决IN拆分   备份
						//.append(TableLfDep.DEP_ID).append(" IN (").append(depIdsForBalance).append(")")
						//
						//解决了IN拆分的 新增一行  IN中超过1000则进行IN拆分
						.append(" ( ").append(getSqlStr(depIdsForBalance, TableLfDep.DEP_ID)).append(" ) ")
						//end
						.append(" ORDER BY ").append(TableLfDep.DEP_NAME).append(" ASC ")
						.toString();
				
		}
		//System.out.println("pppppppppppppppppppppppppp-充值回收权限树-------"+sql);
		List<LfDep> lfDepList = findEntityListBySQL(
				LfDep.class, sql, StaticValue.EMP_POOLNAME);
		return lfDepList;
	}
	*/
	
	/**
	 * 通过机构权限中的机构，查出其所有父机构，用于创建充值回收权限的机构树，本方法是默认权限不在权限表中，此方法已优化，bug已修改
	 * @param userId 当前操作员的ID
	 * @param depId 机构id
	 * @return 机构的集合
	 * @throws Exception  			 
	 * @author pengj
	 * @datetime 2015-11-25 上午15:00:00
	 */
	
	public List<LfDep> getOperatorBalancePriDeps(String userId,
			String depId, String corpCode, String sysDepId) throws Exception
	{	
		String sql = "";
	
		String sql1 = "";
		
		//通过userid查出充值回收权限表中指定的机构，通过这些机构，查出LfDep表中对应的机构
		sql1 = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
		.append(" ").append(StaticValue.getWITHNOLOCK())
		.append(" WHERE ").append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND (").append(TableLfDep.DEP_ID).append(" IN (SELECT ")
		.append(TableLfBalancePri.DEP_ID).append(" FROM ").append(TableLfBalancePri.TABLE_NAME)
		.append(" ").append(StaticValue.getWITHNOLOCK())
		.append(" WHERE ").append(TableLfBalancePri.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfBalancePri.USER_ID).append(" = ").append(userId).append(")")
		.append(" OR ( ").append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfDep.DEP_STATE).append(" = 1 ")
		.append(" AND ").append(TableLfDep.SUPERIOR_ID).append(" = ").append(sysDepId).append(" )")
		.append(" OR ( ").append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
		.append("' AND ").append(TableLfDep.DEP_STATE).append(" = 1 ")
		.append(" AND ").append(TableLfDep.DEP_ID).append(" = ").append(sysDepId).append(" )) AND ").append(TableLfDep.DEP_STATE).append(" = 1 ").toString();
		List<LfDep> lfDepList1 = findEntityListBySQL(
					LfDep.class, sql1, StaticValue.EMP_POOLNAME);
		
		//通过UserId查出该操作员所属机构的所有第一级子机构
		//List<LfDep> firstLevelChildDeps = getFirstLevelChildDeps(userId, corpCode);
		
		//由depPath构成的depId字符串
		String depIdsForDepPath = "";
		//由充值权限机构id构成的字符串
		String depIdsForBalance = "";
		//由depPath构成的depId数组
		String[] depPathIdArray = null;
		//由充值权限机构id构成的数组
		String[] balanceDepIdArray = null;
		
		//用于拼接由depPath构成的depId
		StringBuffer lfDepPathSb = new StringBuffer();
		//用于拼接充值权限机构id
		StringBuffer lfBalanceDepIdSb = new StringBuffer();
		
		if(lfDepList1 != null && lfDepList1.size() > 0){
			
			String lfDepDepPath = null;
			Long lfBalanceDepId = null;
			
			for(LfDep lfDep : lfDepList1 ){
				lfDepDepPath = lfDep.getDeppath();
				lfBalanceDepId = lfDep.getDepId();
				lfDepPathSb.append(lfDepDepPath);
				lfBalanceDepIdSb.append(lfBalanceDepId).append(",");
			}
		}
		/*
		if(firstLevelChildDeps != null && firstLevelChildDeps.size() > 0){
			
			String firstLevelChildDepPath = null;
			Long firstLevelChildDepId = null;
			
			for(LfDep firstLevelChildDep : firstLevelChildDeps ){
				firstLevelChildDepPath = firstLevelChildDep.getDeppath();
				firstLevelChildDepId = firstLevelChildDep.getDepId();
				lfDepPathSb.append(firstLevelChildDepPath);
				lfBalanceDepIdSb.append(firstLevelChildDepId).append(",");
			}
		}
		*/
		//去掉最后一个"/"
		if(lfDepPathSb.length() > 0 ){
			lfDepPathSb.deleteCharAt(lfDepPathSb.length()-1);
		}
		//去掉最后一个","
		if(lfBalanceDepIdSb.length() > 0 ){
			lfBalanceDepIdSb.deleteCharAt(lfBalanceDepIdSb.length()-1);
		}
		//由depPath构成的depId数组
		depPathIdArray = lfDepPathSb.toString().split("/");
		//由充值权限机构id构成的数组
		balanceDepIdArray = lfBalanceDepIdSb.toString().split(",");
		//用于过滤depPath构成depId的list
		List<String> depIdListForDepPath = new ArrayList<String>();
		//用于过滤充值回收权限机构id的list
		List<String> depIdListForBalance = new ArrayList<String>();
			
		//过滤重复的元素（depPath构成depId）
		for(int i=0; i<depPathIdArray.length; i++){
			if(!depIdListForDepPath.contains(depPathIdArray[i])){
				depIdListForDepPath.add(depPathIdArray[i]);
			}
		}
		//过滤重复的元素（充值回收权限机构id）
		for(int i=0; i<balanceDepIdArray.length; i++){
			if(!depIdListForBalance.contains(balanceDepIdArray[i])){
				depIdListForBalance.add(balanceDepIdArray[i]);
			}
		}
		//由depPath构成的depId字符串，并去掉特殊字符
		depIdsForDepPath = depIdListForDepPath.toString();
		depIdsForDepPath = depIdsForDepPath.replace("[","");
		depIdsForDepPath = depIdsForDepPath.replace("]","");
		depIdsForDepPath = depIdsForDepPath.replace(" ","");
		
		//由充值权限机构id构成的字符串，并去掉特殊字符
		depIdsForBalance = depIdListForBalance.toString();
		depIdsForBalance = depIdsForBalance.replace("[","");
		depIdsForBalance = depIdsForBalance.replace("]","");
		depIdsForBalance = depIdsForBalance.replace(" ","");
		
		
		
		if (depId == null || "".equals(depId))
			{
			sql = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
			.append(" " + StaticValue.getWITHNOLOCK()).append(" WHERE ")
			.append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
			.append("' AND ")
			/*原先没有解决IN拆分   备份
			.append(TableLfDep.DEP_ID).append(" IN ( ").append(depIdsForDepPath).append(" ) ")
			*/
			//解决了IN拆分的 新增一行  IN中超过1000则进行IN拆分
			.append(" ( ").append(getSqlStr(depIdsForDepPath, TableLfDep.DEP_ID)).append(" ) ")
			//end
			.append(" ORDER BY ").append(TableLfDep.DEP_NAME).append(" ASC ")
			.toString();
				
			
		}else
		{
				sql = new StringBuffer(" select * from ").append(
						TableLfDep.TABLE_NAME).append(
						" " + StaticValue.getWITHNOLOCK()).append(" where ")
						.append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
						.append("' AND ").append(
						TableLfDep.SUPERIOR_ID).append(" = ").append(depId)
						.append(" AND ")
						/*原先没有解决IN拆分   备份
						.append(TableLfDep.DEP_ID).append(" IN (").append(depIdsForBalance).append(")")
						*/
						//解决了IN拆分的 新增一行  IN中超过1000则进行IN拆分
						.append(" ( ").append(getSqlStr(depIdsForBalance, TableLfDep.DEP_ID)).append(" ) ")
						//end
						.append(" ORDER BY ").append(TableLfDep.DEP_NAME).append(" ASC ")
						.toString();
				
		}
		//System.out.println("pppppppppppppppppppppppppp-充值回收权限树-------"+sql);
		List<LfDep> lfDepList = findEntityListBySQL(
				LfDep.class, sql, StaticValue.EMP_POOLNAME);
		return lfDepList;
	}
	
	/**
	 * 获取机构的第一级的子机构，不使用
	 * @param depId
	 * @param pageInfo
	 * @return
	 * @throws Exception 
	 * @throws Exception 
	 */
	/*
	public List<LfDep> getFirstLevelChildDeps1(String depId, String corpCode){
		if(depId == null || corpCode == null){
			return null;
		}
		String sql = "";
		try {
			sql = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
			.append(" WHERE ").append(TableLfDep.SUPERIOR_ID).append(" = ").append(depId)
			.append(" AND ").append(TableLfDep.DEP_STATE).append(" = 1 ")
			.append(" AND ").append(TableLfDep.CORP_CODE).append(" = ").append(corpCode).toString();
			//System.out.println("---------获取机构的第一级子机构SQL---------" + sql);
			List<LfDep> firstLevelChildDeps = findEntityListBySQL(
						LfDep.class, sql, StaticValue.EMP_POOLNAME);
			return firstLevelChildDeps;
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"获取机构的第一级子机构异常!");
			return null;
		}
	}
	*/
	
	/**
	 * 获取机构的第一级的子机构
	 * @param depId
	 * @param pageInfo
	 * @return
	 * @throws Exception 
	 * @throws Exception 
	 */
	
	public List<LfDep> getFirstLevelChildDeps(String userId, String corpCode){
		
		String sql = "";
		try {
			sql = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
			.append(" WHERE ").append(TableLfDep.DEP_STATE).append(" = 1 ")
			.append(" AND ").append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
			.append("' AND ").append(TableLfDep.SUPERIOR_ID).append(" = ( ")
			.append(" SELECT ").append(TableLfSysuser.DEP_ID).append(" FROM ").append(TableLfSysuser.TABLE_NAME)
			.append(" ").append(StaticValue.getWITHNOLOCK())
			.append(" WHERE ").append(TableLfSysuser.CORP_CODE).append(" = '").append(corpCode)
			.append("' AND ").append(TableLfSysuser.USER_ID).append(" = ").append(userId).append(" ) ").toString();
			//System.out.println("---------获取机构的第一级子机构SQL---------" + sql);
			List<LfDep> firstLevelChildDeps = findEntityListBySQL(
						LfDep.class, sql, StaticValue.EMP_POOLNAME);
			return firstLevelChildDeps;
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"获取机构的第一级子机构异常!");
			return null;
		}
	}
	// end

	
	
	
	// end

	
	/**
	 * 查询此机构的余额信息
	 */
	public LfDepBalanceVo getDepBalanceBydepId(Long depId,String lgCorpcode) throws Exception
	{
		//查询sql
		LfDepBalanceVo balance = null;
		try {
			
			//查询字段
			String sql = new StringBuffer("select dep.").append(TableLfDep.DEP_ID)
					.append(",dep.").append(TableLfDep.DEP_NAME).append(",dep.")
					.append(TableLfDep.DEP_RESP).append(",balance.").append(
							TableLfDepUserBalance.SMS_BALANCE).append(",balance.")
					.append(TableLfDepUserBalance.SMS_COUNT).append(",balance.")
					.append(TableLfDepUserBalance.MMS_BALANCE).append(",balance.")
					.append(TableLfDepUserBalance.MMS_COUNT).append(",balance.")
					.append(TableLfDepUserBalance.SMS_ALARM_VALUE).append(",balance.")
					.append(TableLfDepUserBalance.MMS_ALARM_VALUE).append(",balance.")
					.append(TableLfDepUserBalance.ALARM_NAME).append(",balance.")
					.append(TableLfDepUserBalance.ALARM_PHONE)
					.toString();
			
			//查询条件
			String basesql = new StringBuffer(" from  ").append(TableLfDep.TABLE_NAME).append(" dep  left join ")
					.append(TableLfDepUserBalance.TABLE_NAME).append(" balance on  dep.")
					.append(TableLfDep.DEP_ID).append(" = ").append(" balance.")
					.append(TableLfDepUserBalance.TARGET_ID).append(" and  dep.").append(TableLfDep.CORP_CODE)
					.append(" = ").append(" balance.").append(TableLfDepUserBalance.CORP_CODE).append(" where ")
					.append(TableLfDep.DEP_ID).append(" = ").append(depId).append(" AND dep.").append(TableLfDep.CORP_CODE)
					.append(" = '").append(lgCorpcode).append("' ").toString();
			
			//总sql
			String conSql = sql+basesql;
			List<LfDepBalanceVo> lfList = findVoListBySQL(LfDepBalanceVo.class, conSql,StaticValue.EMP_POOLNAME);
			if(lfList != null && lfList.size() > 0)
			{
				balance = lfList.get(0);
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据机构id查询机构余额信息异常!");
		}
		
		return balance;
	}
	
	/**
	 * 根据机构Id查询有充值权限的子机构(不关联权限表)
	 * @Title: getDepSon
	 * @Description: TODO
	 * @return List<LfDep>
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-11-23 下午05:46:26
	 */
	public List<LfDepBalanceDefVo> getDepSon(Long depId,String lgCorpcode) {
		
		List<LfDepBalanceDefVo> LfDepBalanceDefVos = null;
		try {
			String sql = new StringBuffer("SELECT dep.").append(TableLfDep.DEP_ID).append(",dep.").append(TableLfDep.DEP_NAME)
						 .append(",dep.").append(TableLfDep.CORP_CODE).append(",balancedef.").append(TableLfBalanceDef.SMS_COUNT)
						 .append(",balancedef.").append(TableLfBalanceDef.MMS_COUNT).append(" FROM ").append(TableLfDep.TABLE_NAME)
						 .append(" dep LEFT JOIN ").append(TableLfBalanceDef.TABLE_NAME).append(" balancedef ON dep.")
						 .append(TableLfDep.DEP_ID).append(" = balancedef.").append(TableLfBalanceDef.DEP_ID).append(" WHERE dep.")
						 .append(TableLfDep.SUPERIOR_ID).append(" = ").append(depId).append(" AND dep.").append(TableLfDep.CORP_CODE)
						 .append(" = '").append(lgCorpcode).append("' AND dep.").append(TableLfDep.DEP_STATE).append(" = 1 ")
						 .append(" ORDER BY dep.").append(TableLfDep.DEP_NAME).append(" ASC").toString();

			LfDepBalanceDefVos = findVoListBySQL(LfDepBalanceDefVo.class, sql, StaticValue.EMP_POOLNAME);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"批量充值，根据机构Id查询有充值权限的子机构异常！");
		}
		
		return LfDepBalanceDefVos;
		
	}
	
	public List<LfDepBalanceDefVo> getDepSonNoAdmin(Long depId,String lgCorpcode,Long userId) {
		
		List<LfDepBalanceDefVo> LfDepBalanceDefVos = null;
		try {
			//查询字段
			String consql = new StringBuffer("SELECT dep.").append(TableLfDep.DEP_ID).append(",dep.")
							.append(TableLfDep.DEP_NAME).append(",dep.").append(TableLfDep.CORP_CODE)
							.append(",balancedef.").append(TableLfBalanceDef.SMS_COUNT).append(",balancedef.")
							.append(TableLfBalanceDef.MMS_COUNT).toString();
			//查询表
			String tableSql = new StringBuffer(" FROM ").append(TableLfDep.TABLE_NAME).append(" dep LEFT JOIN ")
								.append(TableLfBalancePri.TABLE_NAME).append(" balancepri ON dep.")
								.append(TableLfDep.DEP_ID).append(" = balancepri.").append(TableLfBalancePri.DEP_ID)
								.append(" LEFT JOIN ").append(TableLfBalanceDef.TABLE_NAME).append(" balancedef ON dep.")
								.append(TableLfDep.DEP_ID).append(" = balancedef.").append(TableLfBalanceDef.DEP_ID)
								.toString();
			//查询条件
			String whereSql = new StringBuffer(" WHERE dep.").append(TableLfDep.SUPERIOR_ID).append(" = ").append(depId)
								.append(" AND dep.").append(TableLfDep.CORP_CODE).append(" = '").append(lgCorpcode)
								.append("' AND balancepri.").append(TableLfBalancePri.USER_ID).append(" = ")
								.append(userId).append(" AND dep.").append(TableLfDep.DEP_STATE).append(" = 1").toString();
			//排序
			String orderSql = new StringBuffer(" ORDER BY dep.").append(TableLfDep.DEP_NAME).append(" ASC").toString();
			
			//总sql
			String sql = consql + tableSql +whereSql + orderSql;
			
			LfDepBalanceDefVos = findVoListBySQL(LfDepBalanceDefVo.class, sql,StaticValue.EMP_POOLNAME);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"批量充值，根据机构Id查询有充值权限的子机构异常！");
		}
		
		return LfDepBalanceDefVos;
		
	}
	
	
	/**
	 * 判断该操作员是否有对选中机构的子机构有批量充值的权限
	 * @Title: checkLguser
	 * @Description: TODO
	 * @param depId
	 * @param lguserid
	 * @param lgcorpCode
	 * @return 
	 * @return boolean
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-12-2 上午10:28:23
	 */
	public boolean checkLguser(Long depId,Long lguserid,String lgcorpCode){
		boolean falg = false;
		
		List<LfDepBalancePriVo> lfdepBalancePriVos = null;
		
		try {
			String consql = new StringBuffer("SELECT dep.").append(TableLfDep.DEP_ID)
								.append(",dep.").append(TableLfDep.DEP_NAME).append(",dep.")
								.append(TableLfDep.CORP_CODE).append(",balancepri.")
								.append(TableLfBalancePri.USER_ID).toString();
			
			String tableSql = new StringBuffer(" FROM ").append(TableLfBalancePri.TABLE_NAME).append(" balancepri LEFT JOIN  ")
								.append(TableLfDep.TABLE_NAME).append(" dep ON balancepri.").append(TableLfBalancePri.DEP_ID)
								.append(" = dep.").append(TableLfDep.DEP_ID).toString();
			String whereSql = new StringBuffer(" WHERE dep.").append(TableLfDep.SUPERIOR_ID).append(" = ").append(depId)
								.append(" AND balancepri.").append(TableLfBalancePri.USER_ID).append(" = ").append(lguserid)
								.append("  AND dep.").append(TableLfDep.CORP_CODE).append(" = '").append(Long.valueOf(lgcorpCode))
								.append("'").toString();
			
//			String sql = new StringBuffer("SELECT balancepri.DEP_ID FROM LF_BALANCE_PRI balancepri LEFT JOIN LF_DEP dep ON balancepri.DEP_ID = dep.DEP_ID WHERE dep.SUPERIOR_ID = ")
//			.append(depId).append(" AND balancepri.USER_ID = ").append(lguserid).append(" AND dep.CORP_CODE ='").append(lgcorpCode).append("'").toString();		
//			
			String sql = consql + tableSql  + whereSql;
			
			lfdepBalancePriVos = findVoListBySQL(LfDepBalancePriVo.class, sql, StaticValue.EMP_POOLNAME);
			if(lfdepBalancePriVos != null && lfdepBalancePriVos.size() > 0)
			{
				falg = true;
			}
		} catch (Exception e) {
			falg = false;
			EmpExecutionContext.error(e,"查询该操作员是否有对选中机构的子机构有批量充值的权限异常！");
		}
		return falg;
	}
	
	/**
	 * 检查用户是否有跨机构充值的权限（为了充值时，调用新的存储过程）
	 * @Title: checkUserBalance
	 * @Description: TODO
	 * @param depId
	 * @param lguserid
	 * @param lgcorpCode
	 * @return 
	 * @return boolean
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-12-5 上午11:56:16
	 */
	public boolean checkUserBalance(Long depId,Long lguserid,String lgcorpCode){
		boolean falg = false;
		try {
			String sql = new StringBuffer("SELECT * FROM ").append(TableLfBalancePri.TABLE_NAME)
							.append(" WHERE ").append(TableLfBalancePri.DEP_ID).append(" = ")
							.append(depId).append(" AND ").append(TableLfBalancePri.USER_ID)
							.append(" = ").append(lguserid).append(" AND ").append(TableLfBalancePri.CORP_CODE)
							.append(" = '").append(lgcorpCode).append("'").toString();
			
			List<LfBalancePri> list = findEntityListBySQL(LfBalancePri.class, sql, StaticValue.EMP_POOLNAME);
			if(list != null && list.size() >0)
			{
				falg = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"验证用户是否有跨机构充值的权限异常！");
		}
		
		
		return falg;
	}
	
	/**
	 * 根据机构Id和企业编码获取机构信息
	 * @Title: getDepByDepIdAndCorpCode
	 * @Description: TODO
	 * @param depId
	 * @param lgcorpCode
	 * @return LfDep
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-12-16 下午04:19:26
	 */
	public LfDep getDepByDepIdAndCorpCode(Long depId,String lgcorpCode){
		LfDep lfDep = null;
		try {
			String sql = new StringBuffer("SELECT * FROM ").append(TableLfDep.TABLE_NAME)
							.append(" WHERE ").append(TableLfDep.DEP_ID).append(" = ")
							.append(depId).append(" AND ").append(TableLfDep.CORP_CODE)
							.append(" = '").append(lgcorpCode).append("'").toString();
			
			List<LfDep> list = findEntityListBySQL(LfDep.class, sql, StaticValue.EMP_POOLNAME);
			if(list != null && list.size() > 0)
			{
				lfDep = list.get(0);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"根据机构Id和企业编码获取机构信息异常！");
		}
		
		return lfDep;
		
	}
	
	
	// EMP5.7新需求：增加对操作员充值和回收权限   by pengj	
	/**
	 * sql in查询  拆分方法
	 * @param idstr
	 * @param columnstr
	 * @return
	 */
	public String getSqlStr(String idstr,String columnstr){
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
	// end
	
}
