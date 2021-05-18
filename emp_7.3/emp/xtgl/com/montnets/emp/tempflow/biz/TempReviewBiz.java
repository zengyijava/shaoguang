package com.montnets.emp.tempflow.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.constant.WebgatePropInfo;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.template.MmsTemplate;
import com.montnets.emp.tempflow.dao.GenericLfFlowRecordTemplateVoDAO;
import com.montnets.emp.tempflow.vo.LfFlowRecordTemplateVo;
import com.montnets.emp.util.PageInfo;

/**
 * 模板审批biz
 * @author zm
 *
 */
public class TempReviewBiz extends SuperBiz{

	private GenericLfFlowRecordTemplateVoDAO lfFlowRecordTemplateVoDAO;
	
	public TempReviewBiz()
	{
		lfFlowRecordTemplateVoDAO=new GenericLfFlowRecordTemplateVoDAO();
	}
	
	/**
	 * 重载获取模板审批记录的方法。获取无需分页的模板审批记录
	 * @param curLoginedUserId
	 * @param lfFlowRecordTemplateVo
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordTemplateVo> getFlowRecordTemplateVos(
			Long curLoginedUserId, LfFlowRecordTemplateVo lfFlowRecordTemplateVo)
			throws Exception
	{
		return this.getFlowRecordTemplateVos(curLoginedUserId, lfFlowRecordTemplateVo, null);
	}
	
	/**
	 * 获取模板审批记录
	 * @param curLoginedUserId
	 * @param lfFlowRecordTemplateVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfFlowRecordTemplateVo> getFlowRecordTemplateVos(
			Long curLoginedUserId,
			LfFlowRecordTemplateVo lfFlowRecordTemplateVo, PageInfo pageInfo)
			throws Exception
	{
		//短信模板审批信息集合
		List<LfFlowRecordTemplateVo> frVosList;
		try
		{
			//没有分页信息
			if (pageInfo == null)
			{
			
				frVosList = lfFlowRecordTemplateVoDAO
						.findLfFlowRecordTemplateVo(curLoginedUserId,
								lfFlowRecordTemplateVo);
			}
			else
			{
				//带分页查询
				frVosList = lfFlowRecordTemplateVoDAO
						.findLfFlowRecordTemplateVo(curLoginedUserId,
								lfFlowRecordTemplateVo, pageInfo);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取模板审批记录失败！");
			//抛出异常
			throw e;
		}
		return frVosList;
	}
	
	/**
	 * 短信模板审批
	 * @param frId
	 * @param rState
	 * @param comments
	 * @return 
	 * @throws Exception
	 */
	public boolean reviewTemplate(Long frId, Integer rState, String comments)
			throws Exception
	{
		boolean isSuccess = true;
		//获取当前审批流信息
		LfFlowRecord flowRecord = empDao.findObjectByID(LfFlowRecord.class,
				frId);
		if(flowRecord.getIsComplete() == 1)
		{
			return false;
		}
		//设置审批状态及审批意见及审批时间
		flowRecord.setRState(rState);
		flowRecord.setComments(comments);
		//设置为流程结束
		flowRecord.setIsComplete(1);
		flowRecord.setRTime(new Timestamp(System.currentTimeMillis()));
		String approve="";  //作为审核处理标志 0 表示同意，1表示拒绝
		//是否实例化下一级
		boolean isNewNext = false;
		ReviewBiz reviewBiz = new ReviewBiz();
		//获取一个连接
		Connection conn = empTransDao.getConnection();
		try
		{
			//开启一个事务
			int level = flowRecord.getRLevel();  //当前审批级次
			int levelamout = flowRecord.getRLevelAmount(); //所有审批级次
			int rCondition = flowRecord.getRCondition();
			
			//是否最后一级
			boolean isLastLevel = (level == levelamout);
			if(isLastLevel && flowRecord.getRType() == 5)
			{
				isLastLevel = reviewBiz.checkZhujiIsOver(flowRecord.getPreRv().intValue(), flowRecord.getProUserCode());
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			conditionMap.put("FId", flowRecord.getFId().toString());
			conditionMap.put("mtId", flowRecord.getMtId().toString());
			conditionMap.put("infoType", flowRecord.getInfoType().toString());
			conditionMap.put("isComplete", "2");
			conditionMap.put("RLevel", flowRecord.getRLevel().toString());
			
			List<LfFlowRecord> otherRecords = null;
			//需全部通过时，查找同级未被通过的数据,因为放在事务中会查询不到数据，所以放在事务外先查询
			if(rCondition == 1 && rState == 1)
			{
				conditionMap.put("RState", "-1");
				conditionMap.put("frId&<>", flowRecord.getFrId().toString());
				otherRecords = empDao.findListBySymbolsCondition(LfFlowRecord.class, conditionMap, null);
				conditionMap.remove("frId&<>");
				conditionMap.remove("RState");
			}
			empTransDao.beginTransaction(conn);
			
			//更新所有审核实例为流程结束
			objectMap.put("isComplete", "1");
			//如果审批拒绝
			if(rState == 2)
			{
				isSuccess = empTransDao.update(conn, LfFlowRecord.class, objectMap, conditionMap);
				if(isSuccess)
				{
					//获取当前审批模板信息，并修改其审批状态
					LfTemplate template = empDao.findObjectByID(LfTemplate.class,
							flowRecord.getMtId());
					template.setIsPass(2);
					isSuccess = empTransDao.update(conn, template);
					//拒绝是给发送者一个信息告诉他该信息已经被拒绝了
					approve="1";

				}
			}else if(rState == 1)
			{
				//是否模板审批通过
				boolean isTempSure = false;
				//一人通过，且是最后一级时
				if(rCondition == 2 )
				{
					if(isLastLevel)
					{
						isTempSure = true;
					}else
					{
						isNewNext = true;
					}
				}else
				if(rCondition == 1 && (otherRecords == null || otherRecords.size() == 0))
				{
					if(isLastLevel)
					{
						isTempSure = true;
					}else
					{
						isNewNext = true;
					}
				}
				if(isTempSure)	
				{
					//获取当前审批模板信息，并修改其审批状态
					LfTemplate template = empDao.findObjectByID(LfTemplate.class,
							flowRecord.getMtId());
					LinkedHashMap<String, String> mmsConditionMap = new LinkedHashMap<String, String>();
					//如果是彩信,则需要保存到网关平台表中去
					if(template.getTmpType()==4)
					{
						//获取http路径
						Map<String, String[]> prop = WebgatePropInfo.getProp();
						String httpUrl = prop.get("webgateProp")[0];
						
						MmsTemplate mmstl = new MmsTemplate();//网关平台彩信模板对象
						mmstl.setUserId(template.getUserId().toString()); //创建人
						if(StaticValue.getCORPTYPE() ==1)
						{
							mmsConditionMap.clear();
							//因为lf_template老数据可能没企业编码所有这里通过操作员来取企业编码
							if(template.getCorpCode() == null || "".equals(template.getCorpCode()))
							{
								LfSysuser userTemp = empDao.findObjectByID(LfSysuser.class, template.getUserId());
								mmsConditionMap.put("corpCode", userTemp.getCorpCode());
							}else
							{
								mmsConditionMap.put("corpCode", template.getCorpCode());
							}
							//状态为起效
							mmsConditionMap.put("isValidate", "1");
							List<LfMmsAccbind> mmsaccs = empDao.findListByCondition(LfMmsAccbind.class, mmsConditionMap, null);
							if(mmsaccs!=null && mmsaccs.size()>0)
							{
								mmstl.setUserId(mmsaccs.get(0).getMmsUser());
							}
						}
						mmstl.setAuditStatus(0);//审核状态
						mmstl.setTmplStatus(0);//模板状态
						mmstl.setParamCnt(template.getParamcnt());//参数个数
						mmstl.setTmplPath(httpUrl + template.getTmMsg());//文件路径	
						mmstl.setRecvTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));//模板接收时间
						mmstl.setAuditor("0");
						mmstl.setAuditTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
						mmstl.setRemarks("0");
						mmstl.setEmptemplid(template.getEmptemplid());
						mmstl.setTmplId(0L);
						mmstl.setSubmitstatus(0);
						mmstl.setReServe1("0");
						mmstl.setReServe2("0");
						mmstl.setReServe3("0");
						mmstl.setReServe4("0");
						mmstl.setReServe5("0");
						//保存对象
						Long mmsId = empTransDao.saveObjectReturnID(conn, mmstl);
						template.setMmstmplid(mmsId);
					}
					template.setIsPass(rState);
					isSuccess = empTransDao.update(conn, template);
				}
				//非模板通过但需要实例化下一级流程的情况 
				else if(isNewNext)
				{
					isSuccess = reviewBiz.addFlowRecordsForNext(conn,flowRecord);
				}
				//最后一级审批通过或实例化下一级时，需更新同级其他审核为审核完成
				if(isNewNext || isLastLevel)
				{
					if(isSuccess)
					{
						if(flowRecord.getRType() == 5)
						{
							conditionMap.put("preRv", flowRecord.getPreRv().toString());
						}
						//判断是否更新同级其他流程为完成
						if(rCondition == 2)
						{
							
							//一人通过时，直接更新
							isSuccess = empTransDao.update(conn, LfFlowRecord.class, objectMap, conditionMap);
							//当一个人审核通过并且为最后一个级别时候，直接发送
							if(isSuccess&&isLastLevel){
								approve="0";
//								new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
							}
						}else if(rCondition == 1) 
						{
							//多人通过且是最后一个审核时更新
							if(otherRecords == null || otherRecords.size() == 0)
							{
								
								isSuccess = empTransDao.update(conn, LfFlowRecord.class, objectMap, conditionMap);

								//当时最后一个审核时候发送消息
								if(isSuccess&&isLastLevel){
									approve="0";
//									new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
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
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		} catch (Exception e)
		{
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "模板审批失败！");
			throw e;
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
			//执行审批提醒
			if(isSuccess)
			{

				//判断是否开启短信审批提醒功能
				String isre=SystemGlobals.getSysParam("isRemind");
				//如果开启且审批通过
				if("0".equals(isre) && rState==1 && isNewNext)
	        	{
					//调用审批提醒接口
	        		new ReviewRemindBiz().flowReviewRemind(flowRecord);
	        		new ReviewRemindBiz().sendMail(flowRecord);
	        	}
			}
		}
		//有可能为空，不做处理 最后发送给第一个发送者信息
		if("0".equals(approve)){//同意
			new ReviewRemindBiz().ReviewRemindForSender(flowRecord,"");
			new ReviewRemindBiz().sendMailForSender(flowRecord,"");
		}else if("1".equals(approve)){//拒绝
			new ReviewRemindBiz().ReviewRemindForSender(flowRecord,flowRecord.getRLevel()+"");
			new ReviewRemindBiz().sendMailForSender(flowRecord,flowRecord.getRLevel()+"");
		}
		return isSuccess;
	}
}

