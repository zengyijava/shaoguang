/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-5 上午10:12:59
 */
package com.montnets.emp.mobilebus.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.ydyw.LfContractTaocan;
import com.montnets.emp.mobilebus.biz.BuckleFeeParams;
import com.montnets.emp.mobilebus.constant.MobileBusStaticValue;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.ydyw.TableLfBusTaoCan;
import com.montnets.emp.table.ydyw.TableLfContract;
import com.montnets.emp.table.ydyw.TableLfContractTaocan;
import com.montnets.emp.table.ydyw.TableLfDeductionsDisp;
import com.montnets.emp.table.ydyw.TableLfDeductionsList;
import com.montnets.emp.table.ydyw.TableLfProCharges;

/**
 * @description 
 * @project p_ydyw
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-5 上午10:12:59
 */

public class BuckleFeeDAO extends SuperDAO
{
	private SmsSpecialDAO specialDao = new SmsSpecialDAO();
	/**
	 * 获取扣费任务执行时间
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-5 下午03:08:15
	 */
	public List<DynaBean> getBillingTaskTime()
	{
		try
		{
			List<DynaBean> monConfigList = null;
			String sql = "SELECT * FROM LF_GLOBAL_VARIABLE WHERE GLOBALID IN(38,39)";
			monConfigList = getListDynaBeanBySql(sql);
			return monConfigList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取扣费任务执行时间异常！");
			return null;
		}
	}
	
	/**
	 * 扣费账号信息
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-5 下午03:14:02
	 */
	public List<DynaBean> getBuckleFeeACCTList()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		String time = genericDao.getTimeCondition(sdf.format(calendar.getTime()));
		String sql = "SELECT ct."+TableLfContractTaocan.DEBITACCOUNT+",ct."+TableLfContractTaocan.TAOCAN_TYPE+",ct."+TableLfContractTaocan.TAOCAN_MONEY+",ct."+TableLfContractTaocan.TAOCAN_NAME+",ct."+TableLfContractTaocan.TAOCAN_CODE
		+",ct."+TableLfContractTaocan.ID+",contract."+TableLfContract.MOBILE+",contract."+TableLfContract.CUSTOM_NAME+",contract."+TableLfContract.IDENT_NO+",contract."+TableLfContract.CONTRACT_ID+",charge."+TableLfProCharges.BUCKUP_MAXTIMER
		+",contract."+TableLfContract.ACCT_NO+",contract."+TableLfContract.CONTRACT_STATE+",contract."+TableLfContract.CONTRACT_SOURCE+",contract."+TableLfContract.CONTRACT_DEP+",ct."+TableLfContractTaocan.BUCKLEFEE_TIME+",tc."+TableLfBusTaoCan.STATE
		+",contract."+TableLfContract.CONTRACT_USER+",contract."+TableLfContract.DEP_ID+",contract."+TableLfContract.USER_ID+",contract."+TableLfContract.CORP_CODE+",dep."+TableLfDep.DEP_NAME+",ct."+TableLfContractTaocan.LAST_BUCKLEFEE
		+" FROM "+TableLfContractTaocan.TABLE_NAME+" ct,"+TableLfContract.TABLE_NAME+" contract,"+TableLfDep.TABLE_NAME+" dep,"+TableLfBusTaoCan.TABLE_NAME+" tc,"+TableLfProCharges.TABLE_NAME+" charge WHERE ct."+TableLfContractTaocan.CONTRACT_ID+" = contract."
		+TableLfContract.CONTRACT_ID+" AND contract."+TableLfContract.CONTRACT_DEP+" = dep."+TableLfDep.DEP_ID+" AND "
		//+specialDao.notNullSql(TableLfContractTaocan.DEBITACCOUNT)+" AND "
		+"ct."+TableLfContractTaocan.TAOCAN_CODE +" = tc."+TableLfBusTaoCan.TAOCAN_CODE+" AND "
		+"ct."+TableLfContractTaocan.TAOCAN_CODE +" = charge."+TableLfProCharges.TAOCAN_CODE+" AND "
		+"contract."+TableLfContractTaocan.IS_VALID+" = '0' AND ct."+TableLfContractTaocan.TAOCAN_MONEY+" >0 AND ct."+TableLfContractTaocan.BUCKLEFEE_TIME +" <= "+time;
		return getListDynaBeanBySql(sql);
	}
	
	/**
	 * @description    查找对应套餐的计费统计规则
	 * @param code
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-1-28 下午02:37:06
	 */
	public DynaBean findCharge(String code){
		String sql = "select * from "+TableLfProCharges.TABLE_NAME+" where "+TableLfProCharges.TAOCAN_CODE+"='"+code+"'";
		List<DynaBean> lists = getListDynaBeanBySql(sql);
		if(lists.size()>0){return lists.get(0);}
		return null;
	}
	
	public String getUpdateSql(LfContractTaocan ct){
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		String sql = "update "+TableLfContractTaocan.TABLE_NAME+" set ";
		StringBuffer sb = new StringBuffer();
		if(ct.getIsvalid()!=null){
			sb.append(","+TableLfContractTaocan.IS_VALID+" = '"+ct.getIsvalid()+"'");
		}
		if(ct.getBucklefeetime()!=null){
			String time = genericDao.getTimeCondition(ct.getBucklefeetime().toString().substring(0, 19));
			sb.append(","+TableLfContractTaocan.BUCKLEFEE_TIME+"= "+time);
		}
		if(ct.getUpdatetime()!=null){
			String time = genericDao.getTimeCondition(ct.getUpdatetime().toString().substring(0, 19));
			sb.append(","+TableLfContractTaocan.UPDATE_TIME+"= "+time);
		}
		if(ct.getLastbucklefee()!=null){
			sb.append(","+TableLfContractTaocan.LAST_BUCKLEFEE+" = '"+ct.getLastbucklefee()+"'");
		}
		sql += sb.toString().substring(1);
		sql +=" where "+TableLfContractTaocan.ID+" = "+ct.getId();
		return sql;
	}
	/**
	 * @description    批量处理签约套餐关系
	 * @param conn
	 * @param cts
	 * @throws SQLException       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-1-28 下午02:56:56
	 */
	public void batchUpdate(Connection conn,List<LfContractTaocan> cts) throws SQLException{
		Statement st = null;
		try {
			st = conn.createStatement();
			int i = 0;
			for (LfContractTaocan ct:cts)
			{
				i++;
				st.addBatch(getUpdateSql(ct));
				if(i>0 && i%10000==0){
					st.executeBatch();
					st.clearBatch();
				}
			}
			st.executeBatch();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"批量处理签约套餐关系异常batchUpdate");
		}finally{
			if(st!=null){
				st.close();
			}
		}
		
	}
	
	/**
	 * @description   批量处理扣费结果 更新状态至计费表及流水表 
	 * @param conn
	 * @param cts
	 * @throws SQLException       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-1-29 上午11:57:59
	 */
	public void batchUpdate(Map<String, BuckleFeeParams> buckleFeeMap) throws SQLException{
		Iterator<String> keys = buckleFeeMap.keySet().iterator();
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Connection conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
		Statement st = null;
		try {
		st = conn.createStatement();
		String time = null;
		String msgId = null;
		String state = null;
		String lastBucleUpFee = null;
		Long lTime = 0L; 
		int i = 0;
		while(keys.hasNext()){
			String key = keys.next();
			BuckleFeeParams params = buckleFeeMap.get(key);
			msgId = params.getMsgId();
			state = params.getState();//返回结果 0 成功 1失败
			lTime = params.getTime();
			lastBucleUpFee = params.getLastBucleUpFee();
			if(StringUtils.isNotBlank(msgId)&&StringUtils.isNotBlank(state)&&lTime != null){
				i++;
				time = genericDao.getTimeCondition(new Timestamp(lTime).toString().substring(0, 19));
				state = String.valueOf(Integer.valueOf(state)+1);//1 为 扣费成功 2为扣费失败
				st.addBatch("update "+TableLfDeductionsDisp.TABLE_NAME+" set "+TableLfDeductionsDisp.OPR_STATE+" = "+state+","+TableLfDeductionsDisp.OPR_TIME
						+" = "+time+" where "+TableLfDeductionsDisp.MSG_ID+" = '"+msgId+"'");
				st.addBatch("update "+TableLfDeductionsList.TABLE_NAME+" set "+TableLfDeductionsList.DEDUCTIONS_TYPE+" = "+state+","+TableLfDeductionsList.BUCKLEFEE_TIME
						+" = "+time+" where "+TableLfDeductionsList.MSG_ID+" = '"+msgId+"'");
				if("2".equals(state)&&"1".equals(lastBucleUpFee)){//扣费失败且不存在补扣
					String timeString = genericDao.getTimeCondition(sdf.format(new Date()));
					st.addBatch("UPDATE "+TableLfContract.TABLE_NAME+" SET "+TableLfContract.CONTRACT_STATE+" = 2,"+TableLfContract.CANCEL_CONTIME+" = "+timeString
					+","+TableLfContract.CANCEL_CONTYPE+" = "+0+" WHERE "+TableLfContract.CONTRACT_ID+
					" IN (SELECT "+TableLfDeductionsList.CONTRACT_ID+" FROM "+TableLfDeductionsList.TABLE_NAME+" WHERE "+TableLfDeductionsList.MSG_ID+" = '"+msgId +"')");
				}
			}
			if(i>0 && i%5000==0){
				st.executeBatch();
				st.clearBatch();
				i = 0;
			}
		}
		st.executeBatch();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"批量处理扣费结果 更新状态至计费表及流水表异常");
		}finally{
			if(st!=null){
				st.close();
			}
		}
	}
	
	public String[] getTimeOutUpdateSql(){
		String[] sqls = new String[2];
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = genericDao.getTimeCondition(sdf.format(new Date()));
		
		sqls[0] = "update "+TableLfDeductionsList.TABLE_NAME+" set "+TableLfDeductionsList.UDPATE_TIME+" = "+time
		+","+TableLfDeductionsList.DEDUCTIONS_TYPE +" = -1  where "+TableLfDeductionsList.DEDUCTIONS_TYPE 
		+" in(0,5) and "+getTimeDiffOver(TableLfDeductionsList.UDPATE_TIME,MobileBusStaticValue.rptTimeOut); 
		
		sqls[1] = "update "+TableLfDeductionsDisp.TABLE_NAME+" set "+TableLfDeductionsDisp.UPDATE_TIME+" = "+time
		+","+TableLfDeductionsDisp.OPR_STATE +" = -1  where "+TableLfDeductionsDisp.OPR_STATE
		+" in(0,5) and "+getTimeDiffOver(TableLfDeductionsDisp.OPR_TIME,MobileBusStaticValue.rptTimeOut);
		return sqls;
	
	}
	
	public String getTimeDiffOver(String field,long hours){
		if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){//sqlserver
			return "DATEDIFF(hh,"+field+",getdate()) >= "+hours;
		}
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){//oracle
			return field+"+"+hours/24+" <= sysdate";
		}
		if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){//db2
			return "timestampdiff(8,char(current timestamp - timestamp("+field+"))) >= "+hours;
		}
		if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){//mysql
			return "DATE_ADD("+field+", INTERVAL "+hours+" DAY_HOUR) <= now()";
		}
		return "1=2";
	}
	public void batchTimeOutUpdate(Connection conn) throws SQLException{
		Statement st = null;
		try {
			st = conn.createStatement();
			String[] sqls = getTimeOutUpdateSql();
			st.addBatch(sqls[0]);
			st.addBatch(sqls[1]);
			st.executeBatch();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"batchTimeOutUpdate");
		}finally{
			if(st!=null){
				st.close();
			}
		}
	}
}
