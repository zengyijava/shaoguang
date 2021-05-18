package com.montnets.emp.mobilebus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.mobilebus.biz.BuckleFeeParams;

public class SupplementDeductFeeDAO extends SuperDAO
{
	private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 欠费补扣操作：查询需要补扣费的列表
	 * @return
	 */
	public List<DynaBean> findSupplementDeductFees(){
		List<DynaBean> supplementDeductFees=null;
		String currentDate=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
		try
		{
			//字段
			String fieldSql="select ldl.ID ID,ldl.CONTRACT_ID CONTRACT_ID,ldl.MOBILE MOBILE,ldl.CUSTOM_NAME CUSTOM_NAME,ldl.ACCT_NO ACCT_NO," +
							"ldl.DEBITACCOUNT DEBITACCOUNT,ldl.TAOCAN_CODE TAOCAN_CODE,ldl.TAOCAN_NAME TAOCAN_NAME,ldl.TAOCAN_TYPE TAOCAN_TYPE," +
							"ldl.TAOCAN_MONEY TAOCAN_MONEY,ldl.IYEAR IYEAR,ldl.IMONTH IMONTH,ldl.BUPSUM_MONEY BUPSUM_MONEY,ldl.BUPTIMER BUPTIMER," +
							"ldl.CONTRACT_STATE CONTRACT_STATE,ldl.CONTRACT_TYPE CONTRACT_TYPE,ldl.DEDUCTIONS_TYPE DEDUCTIONS_TYPE," +
							"ldl.BUCKUP_TIMER BUCKUP_TIMER,ldl.UDPATE_TIME UDPATE_TIME,ldl.BUCKLEFEE_TIME BUCKLEFEE_TIME," +
							"ldl.BUCKLEUPFEE_TIME BUCKLEUPFEE_TIME,ldl.CONTRACT_DEP CONTRACT_DEP,ldl.DEP_NAME DEP_NAME," +
							"ldl.CONTRACT_USER CONTRACT_USER,ldl.DEP_ID DEP_ID,ldl.USER_ID USER_ID,ldl.CORP_CODE CORP_CODE,lp.BUCKUP_MAXTIMER BUCKUP_MAXTIMER";
			//表名
			String tableSql=" from LF_DEDUCTIONS_LIST ldl inner join LF_PROCHARGES lp on ldl.TAOCAN_CODE=lp.TAOCAN_CODE ";
			
			//查询条件
			//1.扣费状态DEDUCTIONS_TYPE为2，即扣费失败的
			StringBuffer conditionSql=new StringBuffer(" where ldl.DEDUCTIONS_TYPE=2 "); 
			//2.欠费补扣次数BUCKUP_TIMER小于5
			conditionSql.append(" and ldl.BUCKUP_TIMER<5 ");
			//3.当前扣费次数小于套餐最大扣费次数，即 BUCKUP_TIMER小于BUCKUP_MAXTIMER
			conditionSql.append(" and ldl.BUCKUP_TIMER<lp.BUCKUP_MAXTIMER ");
			//4.最后一次扣费日期+补扣费间隔天数=当前系统日期
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
				conditionSql.append(" and to_char(ldl.BUCKLEFEE_TIME+lp.BUCKUP_INTERVALDAY,'yyyy-mm-dd')='"+currentDate+"' ");
		   	} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
		   		conditionSql.append(" and convert(varchar(10),dateadd(day,lp.BUCKUP_INTERVALDAY,ldl.BUCKLEFEE_TIME),120)='"+currentDate+"' ");
			}else if(StaticValue.DBTYPE==StaticValue.DB2_DBTYPE){
				conditionSql.append(" and CAST((ldl.BUCKLEFEE_TIME +lp.BUCKUP_INTERVALDAY DAYS) AS VARCHAR(10))='"+currentDate+"' ");
			}else if(StaticValue.DBTYPE==StaticValue.MYSQL_DBTYPE){
	
			}
			
			//组合SQL语句
			String sql = new StringBuffer(fieldSql).append(tableSql).append(
					conditionSql).toString();
			//查询数据
			supplementDeductFees=getListDynaBeanBySql(sql);
		}
		catch (Exception e)
		{
			supplementDeductFees=null;
			EmpExecutionContext.error(e, "查询需要补扣费的列表失败！");
		}
		return supplementDeductFees;
	}
	
	/**
	 * 欠费补扣操作：更新扣费表
	 * @param conn 数据库连接
	 * @param supplementDeductFee  扣费对象
	 * @param deductFeeState  扣费结果
	 * @return true成功,false失败
	 * @throws Exception
	 */
	public boolean updateLfDeductions(Connection conn ,DynaBean supplementDeductFee,String msgId) throws Exception{
		boolean isSuccess=false;
		String currentTime=simpleDateFormat.format(Calendar.getInstance().getTime());
		//设置更新时间和扣费时间
		String setSql="";
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			setSql="UDPATE_TIME=to_date('"+currentTime+"','yyyy-MM-dd HH24:mi:ss'),BUCKLEFEE_TIME=to_date('"+currentTime+"','yyyy-MM-dd HH24:mi:ss')";
	   	} else {
	   		setSql="UDPATE_TIME='"+currentTime+"',BUCKLEFEE_TIME='"+currentTime+"'";
	   	}
		String sql="update LF_DEDUCTIONS_LIST set DEDUCTIONS_TYPE=0,BUCKUP_TIMER=BUCKUP_TIMER+1,MSG_ID='"+msgId+"',"+setSql+" where id="+String.valueOf(supplementDeductFee.get("id"));
		PreparedStatement ps=null;
		try
		{
			ps=conn.prepareStatement(sql);
			int count = ps.executeUpdate();
			if (count > 0)
			{
				isSuccess = true;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新扣费表失败！");
			throw e;
		}finally{
			try
			{
				close(null, ps);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源出错");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 欠费补扣操作：保存欠费补扣流水记录
	 * @param conn
	 * @param sdf
	 * @param deductFeeState
	 * @return
	 * @throws Exception
	 */
	public boolean saveLfDeductionsDisp(Connection conn ,DynaBean sdf,String msgId) throws Exception{
		boolean isSuccess = false;
		PreparedStatement ps=null;
		String sql="insert into LF_DEDUCTIONS_DISP(CONTRACT_ID,MOBILE,CUSTOM_NAME,ACCT_NO,DEBITACCOUNT,TAOCAN_CODE," +
				"TAOCAN_MONEY,TAOCAN_NAME,TAOCAN_TYPE,CONTRACT_STATE,DEDUCTIONS_TYPE,DEDUCTIONS_MONEY,OPR_STATE," +
				"CONTRACT_DEP,DEP_NAME,CONTRACT_USER,DEP_ID,USER_ID,CORP_CODE,MSG_ID)" +
				" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try{
			ps=conn.prepareStatement(sql);
			//设置值
			setLong(ps, 1, sdf.get("contract_id"));
			setString(ps, 2, sdf.get("mobile"));
			setString(ps, 3, sdf.get("custom_name"));
			setString(ps, 4, sdf.get("acct_no"));
			setString(ps, 5, sdf.get("debitaccount"));
			setString(ps, 6, sdf.get("taocan_code"));
			setInt(ps, 7, sdf.get("taocan_money"));
			setString(ps, 8, sdf.get("taocan_name"));
			setInt(ps, 9, sdf.get("taocan_type"));
			setInt(ps, 10, sdf.get("contract_state"));
			setInt(ps, 11, 0);
			setInt(ps, 12, sdf.get("taocan_money"));
			setInt(ps, 13, 0);
			setLong(ps, 14, sdf.get("contract_dep"));
			setString(ps, 15, sdf.get("dep_name"));
			setLong(ps, 16, sdf.get("contract_user"));
			setLong(ps, 17, sdf.get("dep_id"));
			setLong(ps, 18, sdf.get("user_id"));
			setString(ps, 19, sdf.get("corp_code"));
			setString(ps, 20, msgId);
			
			int count = ps.executeUpdate();
			if (count == 1)
			{
				isSuccess = true;
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "插入扣费流水表失败！");
			throw e;
		}finally{
			try
			{
				close(null, ps);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源出错");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 欠费补扣返回结果处理操作：更新扣费表
	 * @param conn 数据库连接
	 * @param supplementDeductFee  扣费对象
	 * @param deductFeeState  扣费结果
	 * @return true成功,false失败
	 * @throws Exception
	 */
	public boolean updateLfDeductionsByMsgID(Connection conn ,BuckleFeeParams buckleFeeParam) throws Exception{
		boolean isSuccess=false;
		String currentTime=simpleDateFormat.format(Calendar.getInstance().getTime());
		//设置更新时间和扣费时间
		String setSql="";
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			setSql="UDPATE_TIME=to_date('"+currentTime+"','yyyy-MM-dd HH24:mi:ss')";
	   	} else {
	   		setSql="UDPATE_TIME='"+currentTime+"'";
	   	}
		String deductionsType=null;
		if("0".equals(buckleFeeParam.getState())){
			deductionsType="1";
		}else {
			deductionsType="2";
		}
		String sql="update LF_DEDUCTIONS_LIST set DEDUCTIONS_TYPE="+deductionsType+","+setSql+" where MSG_ID='"+buckleFeeParam.getMsgId()+"'";
		PreparedStatement ps=null;
		try
		{
			ps=conn.prepareStatement(sql);
			int count = ps.executeUpdate();
			if (count > 0)
			{
				isSuccess = true;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新扣费表失败！");
			throw e;
		}finally{
			try
			{
				close(null, ps);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源出错");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 欠费补扣返回结果处理操作：更新流水表
	 * @param conn
	 * @param buckleFeeParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateLfDeductionsDispByMsgID(Connection conn ,BuckleFeeParams buckleFeeParam) throws Exception{
		boolean isSuccess=false;
		String currentTime=simpleDateFormat.format(Calendar.getInstance().getTime());
		//设置更新时间和扣费时间
		String setSql="";
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			setSql="UPDATE_TIME=to_date('"+currentTime+"','yyyy-MM-dd HH24:mi:ss')";
	   	} else {
	   		setSql="UPDATE_TIME='"+currentTime+"'";
	   	}
		String oprState=null;
		if("0".equals(buckleFeeParam.getState())){
			oprState="1";
		}else {
			oprState="2";
		}
		String sql="update LF_DEDUCTIONS_DISP set OPR_STATE="+oprState+","+setSql+" where MSG_ID='"+buckleFeeParam.getMsgId()+"'";
		PreparedStatement ps=null;
		try
		{
			ps=conn.prepareStatement(sql);
			int count = ps.executeUpdate();
			if (count > 0)
			{
				isSuccess = true;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新扣费表流水表失败！");
			throw e;
		}finally{
			try
			{
				close(null, ps);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源出错");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 欠费补扣返回结果处理操作：更新签约表
	 * @param conn
	 * @param buckleFeeParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateLfContract(Connection conn ,BuckleFeeParams buckleFeeParam)  throws Exception{
		boolean isSuccess=false;
		String currentTime=simpleDateFormat.format(Calendar.getInstance().getTime());
		String setSql="";
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			setSql="UPDATE_TIME=to_date('"+currentTime+"','yyyy-MM-dd HH24:mi:ss')";
	   	} else {
	   		setSql="UPDATE_TIME='"+currentTime+"'";
	   	}
		String sql="update LF_CONTRACT set CONTRACT_STATE=2,"+setSql+" where CONTRACT_ID in(select CONTRACT_ID from LF_DEDUCTIONS_LIST where MSG_ID='"+buckleFeeParam.getMsgId()+"')";
		PreparedStatement ps=null;
		try
		{
			ps=conn.prepareStatement(sql);
			int count = ps.executeUpdate();
			if (count > 0)
			{
				isSuccess = true;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新签约表失败！");
			throw e;
		}finally{
			try
			{
				close(null, ps);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭数据库资源出错");
			}
		}
		return isSuccess;
	}
	
	
	
	/**
	 * 设置字符串
	 * @param ps
	 * @param psIndex
	 * @param fieldValue
	 * @throws Exception
	 */
	private void setString(PreparedStatement ps,int psIndex,Object fieldValue) throws Exception{
		if(fieldValue!=null){
			ps.setString(psIndex,String.valueOf(fieldValue));
		}else{
			ps.setNull(psIndex,Types.VARCHAR);
		}
	}
	
	/**
	 * 设置Int
	 * @param ps
	 * @param psIndex
	 * @param fieldValue
	 * @throws Exception
	 */
	private void setInt(PreparedStatement ps,int psIndex,Object fieldValue) throws Exception{
		if(fieldValue!=null){
			ps.setInt(psIndex,Integer.parseInt(String.valueOf(fieldValue)));
		}else{
			ps.setNull(psIndex,Types.VARCHAR);
		}
	}
	
	/**
	 * 设置Long
	 * @param ps
	 * @param psIndex
	 * @param fieldValue
	 * @throws Exception
	 */
	private void setLong(PreparedStatement ps,int psIndex,Object fieldValue) throws Exception{
		if(fieldValue!=null){
			ps.setLong(psIndex,Long.parseLong(String.valueOf(fieldValue)));
		}else{
			ps.setNull(psIndex,Types.VARCHAR);
		}
	}
}
