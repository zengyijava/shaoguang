package com.montnets.emp.wxmanager.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.netnews.biz.WXUeditorBiz;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxmanger.dao.NetCheckDao;

public class NetCheckBiz extends SuperBiz
{

	public List<DynaBean> getBaseInfos(LinkedHashMap<String, String> conMap, PageInfo pageInfo, String userId)
	{
		return new NetCheckDao().getBaseInfos(conMap, pageInfo, userId);
	}

	public List<DynaBean> getNetByJsp(int netId, String corpcode)
	{
		return new NetCheckDao().getNetByJsp(netId, corpcode);
	}

	/**
	 * 网讯模板审批
	 * 
	 * @param frId
	 * @param rState
	 * @param comments
	 * @return
	 * @throws Exception
	 */
	public boolean reviewTemplate(Long frId, Integer rState, String comments) throws Exception
	{
		boolean isSuccess = true;
		
		// 获取当前审批流信息
		LfFlowRecord flowRecord = empDao.findObjectByID(LfFlowRecord.class, frId);
		// -----此处是针对建议进行修改
		if(flowRecord.getIsComplete() == 1)
		{
			return false;
		}
		// 设置审批状态及审批意见及审批时间
		flowRecord.setRState(rState);
		flowRecord.setComments(comments);
		// 设置为流程结束
		flowRecord.setIsComplete(1);
		flowRecord.setRTime(new Timestamp(System.currentTimeMillis()));

		// 获取一个连接
		Connection conn = empTransDao.getConnection();
		// 是否实例化下一级
		boolean isNewNext = false;
		String approve="";  //作为审核处理标志 0 表示同意，1表示拒绝
		try
		{
			// 开启一个事务
			int level = flowRecord.getRLevel(); // 当前审批级次
			int levelamout = flowRecord.getRLevelAmount(); // 所有审批级次
			int rCondition = flowRecord.getRCondition();

			// 是否最后一级
			boolean isLastLevel = (level == levelamout);

			ReviewBiz review = new ReviewBiz();
			// -----此处是针对建议进行增加 ---------------
			if(isLastLevel && flowRecord.getRType() == 5)
			{
				isLastLevel = review.checkZhujiIsOver(flowRecord.getPreRv().intValue(), flowRecord.getProUserCode());
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			conditionMap.put("FId", flowRecord.getFId().toString());
			conditionMap.put("mtId", flowRecord.getMtId().toString());
			conditionMap.put("infoType", flowRecord.getInfoType().toString());
			conditionMap.put("isComplete", "2");
			conditionMap.put("RLevel", flowRecord.getRLevel().toString());

			List<LfFlowRecord> otherRecords = null;
			// 需全部通过时，查找同级未被通过的数据,因为放在事务中会查询不到数据，所以放在事务外先查询
			if(rCondition == 1 && rState == 1)
			{
				conditionMap.put("RState", "-1");
				conditionMap.put("frId&<>", flowRecord.getFrId().toString());
				otherRecords = empDao.findListBySymbolsCondition(LfFlowRecord.class, conditionMap, null);
				conditionMap.remove("frId&<>");
				conditionMap.remove("RState");
			}
			empTransDao.beginTransaction(conn);

			// 更新所有审核实例为流程结束
			objectMap.put("isComplete", "1");
			// 如果审批拒绝
			if(rState == 2)
			{
				isSuccess = empTransDao.update(conn, LfFlowRecord.class, objectMap, conditionMap);
				// -----此处是针对建议进行修改 增加 if判断 ----------------
				if(isSuccess)
				{
					// 获取当前审批模板信息，并修改其审批状态
					LfWXBASEINFO baseInfo = empDao.findObjectByID(LfWXBASEINFO.class, flowRecord.getMtId());
					baseInfo.setSTATUS(rState + 1);
					isSuccess = empTransDao.update(conn, baseInfo);
					//拒绝是给发送者一个信息告诉他该信息已经被拒绝了
					if(isSuccess){
						approve="1";  
//						new ReviewRemindBiz().ReviewRemindForSender(flowRecord,flowRecord.getRLevel()+"");
					}
				}
			}
			else
				if(rState == 1)
				{
					// 是否模板审批通过
					boolean isTempSure = false;
					// 一人通过，且是最后一级时
					if(rCondition == 2)
					{
						isNewNext = true;
						if(isLastLevel)
						{
							isTempSure = true;
						}
					}
					else
						if(rCondition == 1 && (otherRecords == null || otherRecords.size() == 0))
						{
							isNewNext = true;
							if(isLastLevel)
							{
								isTempSure = true;
							}
						}
					if(isTempSure)
					{
						// 获取当前审批模板信息，并修改其审批状态
						LfWXBASEINFO baseInfo = empDao.findObjectByID(LfWXBASEINFO.class, flowRecord.getMtId());
						baseInfo.setSTATUS(rState + 1);
						isSuccess = empTransDao.update(conn, baseInfo);
					}
					// 非模板通过但需要实例化下一级流程的情况
					else
						if(isNewNext)
						{
							ReviewBiz reviewBiz = new ReviewBiz();
							isSuccess = reviewBiz.addFlowRecordsForNext(conn, flowRecord);
						}
					// 最后一级审批通过或实例化下一级时，需更新同级其他审核为审核完成
					if(isNewNext || isLastLevel)
					{
						if(isSuccess)
						{
							if(flowRecord.getRType() == 5)
							{
								conditionMap.put("preRv", flowRecord.getPreRv().toString());
							}
							// 判断是否更新同级其他流程为完成
							if(rCondition == 2)
							{
								// -----此处是针对建议进行增加 -----------
								// 一人通过时，直接更新
								isSuccess = empTransDao.update(conn, LfFlowRecord.class, objectMap, conditionMap);
								//当一个人审核通过并且为最后一个级别时候，直接发送
								if(isSuccess&&isLastLevel){
									approve="0";
//									new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
								}
							}
							else
								if(rCondition == 1)
								{
									// 多人通过且是最后一个审核时更新
									if(otherRecords == null || otherRecords.size() == 0)
									{
										isSuccess = empTransDao.update(conn, LfFlowRecord.class, objectMap, conditionMap);
										//当时最后一个审核时候发送消息
										if(isSuccess&&isLastLevel){
											approve="0";
//											new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
										}
									}
								}
						}
					}
				}
			if(isSuccess)
			{
				isSuccess = empTransDao.update(conn, flowRecord);
				empTransDao.commitTransaction(conn);

			}
			else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}
		catch (Exception e)
		{
			// 回滚事务
			EmpExecutionContext.error(e,"网讯模板审批异常");
			empTransDao.rollBackTransaction(conn);
			throw e;
		}
		finally
		{
			// 关闭连接
			empTransDao.closeConnection(conn);
			//有可能为空，不做处理 新加的要求，最后发送给第一个发送者信息
			if("0".equals(approve)){//同意
				new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
				new ReviewRemindBiz().sendMailForSender(flowRecord,"");
			}else if("1".equals(approve)){//拒绝
				new ReviewRemindBiz().ReviewRemindForSender(flowRecord,flowRecord.getRLevel()+"");
				new ReviewRemindBiz().sendMailForSender(flowRecord,flowRecord.getRLevel()+"");
			}
			if(isSuccess)
			{
				// 判断是否开启短信审批提醒功能
				String isre = SystemGlobals.getSysParam("isRemind");
				// 如果开启且审批通过
				if("0".equals(isre) && rState == 1 && isNewNext)
				{
					// 调用审批提醒接口
					new WXUeditorBiz().flowReviewRemind(flowRecord);
					new WXUeditorBiz().sendMail(flowRecord);
				}
			}

		}
		return isSuccess;
	}
}
