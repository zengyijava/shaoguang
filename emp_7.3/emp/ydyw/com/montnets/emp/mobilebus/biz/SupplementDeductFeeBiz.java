package com.montnets.emp.mobilebus.biz;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.mobilebus.dao.SupplementDeductFeeDAO;

public class SupplementDeductFeeBiz extends SuperBiz
{
	private SupplementDeductFeeDAO sdfDAO=new SupplementDeductFeeDAO();
	
	private BuckleFeeBiz buckleFeeBiz=new  BuckleFeeBiz();
	
	private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 补扣费主方法
	 */
	public void supplementDeductFee(){
		EmpExecutionContext.error("补扣费任务开始执行！");
		//查询到需要补扣费的数据
		List<DynaBean> supplementDeductFees=sdfDAO.findSupplementDeductFees();
		
		//查询需要补扣费的数据失败
		if(supplementDeductFees==null){
			EmpExecutionContext.error("查询需要补扣费的数据失败！");
			return;
		}
		
		//没有查询到需要补扣费的数据
		if(supplementDeductFees.size()<1){
			EmpExecutionContext.error("没有需要补扣费的数据！");
			return;
		}
		
		//补扣费
		//补扣费实体类
		DynaBean supplementDeductFee=null;
		//流水号
		String msgId=null;
		//操作类型
		String oprType=null;
		StringBuffer supplementDeductFeeSb=new StringBuffer("");
		String currentTime=simpleDateFormat.format(Calendar.getInstance().getTime());
		boolean isUpdateSuccess=false;
		for (int i = 0; i <supplementDeductFees.size(); i++)
		{
			supplementDeductFee=supplementDeductFees.get(i);
			msgId=buckleFeeBiz.getMsgId(String.valueOf(supplementDeductFee.get("corp_code")));
			//将扣费表更新为扣费中，并且记录扣费流水
			//如果失败会尝试3次操作，3次还没有成功，就放弃，并且记录日志
			isUpdateSuccess=supplementDeductFeeTry3Times(supplementDeductFee,msgId);
			
			//只有更新数据库成功，才调用文件接口进行扣费
			if(isUpdateSuccess){
				if(Integer.parseInt(String.valueOf(supplementDeductFee.get("buckup_maxtimer")))-Integer.parseInt(String.valueOf(supplementDeductFee.get("buckup_timer")))==1){
					oprType="1";
				}else{
					oprType="0";
				}
				//扣费
				supplementDeductFeeSb.append(msgId).append("|").append(String.valueOf(supplementDeductFee.get("mobile"))).append("|")
				.append(String.valueOf(supplementDeductFee.get("debitaccount"))).append("|").append(String.valueOf(supplementDeductFee.get("taocan_code"))).append("|")
				.append(String.valueOf(supplementDeductFee.get("taocan_money"))).append("|").append("1").append("|")
				.append(oprType).append("|").append(currentTime).append("&");
			}else{
				EmpExecutionContext.error("更新扣费表和插入扣费流水表失败，不进行扣费。相关信息为，"+"主键ID："+String.valueOf(supplementDeductFee.get("id"))+",签约ID："+String.valueOf(supplementDeductFee.get("contract_id"))
				+",套餐编码:"+String.valueOf(supplementDeductFee.get("taocan_code"))+"金额："+String.valueOf(supplementDeductFee.get("taocan_money")));
			}
		}
		//扣费操作
		buckleFeeBiz.saveBuckleFeeFile(supplementDeductFeeSb.toString());
		
		EmpExecutionContext.error("补扣费任务结束执行！");
	}
	
	/**
	 * 处理欠费补扣的结果
	 * @param buckleUpFeeMap
	 */
	public void dealSupplementDeductFeeResults(Map<String, BuckleFeeParams> buckleUpFeeMap){
		EmpExecutionContext.error("补扣费返回结果处理任务开始执行！");
		Iterator<Map.Entry<String, BuckleFeeParams>> objIter = buckleUpFeeMap.entrySet()
		.iterator();
		Map.Entry<String, BuckleFeeParams> e= null;
		BuckleFeeParams buckleFeeParams=null;
		while (objIter.hasNext())
		{
			e = objIter.next();
			buckleFeeParams=e.getValue();
			try
			{
				boolean isSuccess=dealSupplementDeductFeeResultTry3Times(buckleFeeParams);
				if(!isSuccess){
					EmpExecutionContext.error("流水号为:"+buckleFeeParams.getMsgId()+"的补扣费结果更新到数据库失败！");
				}
			}
			catch (Exception e2)
			{
				EmpExecutionContext.error(e2, "流水号为:"+buckleFeeParams.getMsgId()+"的补扣费结果更新到数据库失败！");
			}
			
		}
		EmpExecutionContext.error("补扣费返回结果处理任务结束执行！");
	}
	
	/**
	 * 单个欠费补扣方法，如果失败，最多会尝试3次
	 * @param supplementDeductFee
	 * @param msgId
	 * @return
	 */
	private boolean supplementDeductFeeTry3Times(DynaBean supplementDeductFee,String msgId){
		boolean isSuccess=false;
		//最多尝试3次
		for (int i = 0; i < 3; i++)
		{
			try
			{
				isSuccess=false;
				isSuccess=supplementDeductFee(supplementDeductFee, msgId);
				if(isSuccess){
					break;
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "单个欠费补扣失败！");
			}
		}
		return isSuccess;
	}
	/**
	 * 单个欠费补扣的方法
	 * @param supplementDeductFee
	 * @param deductFeeState 扣费结果状态 1扣费成功  2扣费失败
	 * @return
	 */
	private boolean supplementDeductFee(DynaBean supplementDeductFee,String msgId){
		boolean flag=false;
		boolean updateFlag=false;
		boolean insertFlag=false;
		Connection conn = null;
		try {
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			//更新计费表
			updateFlag=sdfDAO.updateLfDeductions(conn,supplementDeductFee,msgId);
			//记录补扣费流水
			insertFlag=sdfDAO.saveLfDeductionsDisp(conn,supplementDeductFee,msgId);
			empTransDao.commitTransaction(conn);
			if(updateFlag&&insertFlag){
				flag=true;
			}
		} catch (Exception e) {
			flag=false;
			EmpExecutionContext.error(e,"单个欠费补扣失败！");
			empTransDao.rollBackTransaction(conn);
		} finally {
			empTransDao.closeConnection(conn);
		}
		return flag;
	}
	
	/**
	 * 处理单个欠费补扣的结果，如果失败，最多会尝试3次
	 * @param buckleFeeParams
	 * @return
	 */
	private  boolean dealSupplementDeductFeeResultTry3Times(BuckleFeeParams buckleFeeParams){
		boolean isSuccess=false;
		//最多尝试3次
		for (int i = 0; i < 3; i++)
		{
			try
			{
				isSuccess=false;
				isSuccess=dealSupplementDeductFeeResult(buckleFeeParams);
				if(isSuccess){
					break;
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "处理扣费结果失败！");
			}
		}
		return isSuccess;
	}
	
	/**
	 * 处理单个欠费补扣的结果
	 * @param buckleFeeParams
	 * @return
	 */
	private  boolean dealSupplementDeductFeeResult(BuckleFeeParams buckleFeeParams){
		boolean flag=false;
		boolean updateFlag1=false;
		boolean updateFlag2=false;
		boolean updateFlag3=false;
		Connection conn = null;
		try {
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			//更新计费表
			updateFlag1=sdfDAO.updateLfDeductionsByMsgID(conn,buckleFeeParams);
			//更新流水表
			updateFlag2=sdfDAO.updateLfDeductionsDispByMsgID(conn,buckleFeeParams);
			//如果最后一次补扣费失败,则冻结账户
			if(("1").equals(buckleFeeParams.getState())&&("1").equals(buckleFeeParams.getLastBucleUpFee())){
				updateFlag3=sdfDAO.updateLfContract(conn, buckleFeeParams);
			}else{
				updateFlag3=true;
			}
			empTransDao.commitTransaction(conn);
			if(updateFlag1&&updateFlag2&&updateFlag3){
				flag=true;
			}
		} catch (Exception e) {
			flag=false;
			EmpExecutionContext.error(e,"处理扣费结果失败！");
			empTransDao.rollBackTransaction(conn);
		} finally {
			empTransDao.closeConnection(conn);
		}
		return flag;
	}
}
