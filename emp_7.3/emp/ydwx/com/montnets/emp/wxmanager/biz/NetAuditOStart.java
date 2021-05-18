package com.montnets.emp.wxmanager.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.biz.receive.IBusinessStart;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.approveflow.LfExamineSms;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
/**
 * 审批提醒手机上行审批
 * @author Administrator
 *
 */
public class NetAuditOStart extends SuperBiz implements IBusinessStart{
	/**
	 * 上行启动该方法
	 */
	@Override
    public boolean start(LfMotask moTask) throws Exception {
		//结果
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BaseBiz baseBize=new BaseBiz();
		//下发内容
		String content="";
		LfExamineSms examineSms = null;
		//判断上行的信息是否正确  如果返回false则过五秒钟系统会重新推送上行信箱
		String resultStr = judgeMotask(moTask);
			try{
				//查询是否开启了审批提醒功能
				String isre=SystemGlobals.getSysParam("isRemind");
				//是否开启审批提醒功能  0开启 1不开启
				if("0".equals(isre) && "success".equals(resultStr))
				{
					//上行信息内容
					String msg = moTask.getMsgContent();
					if(msg != null && !"".equals(msg))
					{
						//上行回复内容长度是否小于4
						if(msg.length()<4)
						{
							content= " 对不起，您发送的指令有误！";
							resultStr = "errer";
						}else
						{
							LinkedHashMap<String,LfExamineSms> examineSmsMap = analysisMsg(msg, moTask);

							//判断是同意还是拒绝
							Integer isExamineFlag = -1;
							if(examineSmsMap == null){
								content = " 对不起，您发送的指令有误！";
								resultStr = "errer";
								EmpExecutionContext.error("["+sdf.format(new Date())+"] 对不起，您发送的指令有误！");
							}else if(examineSmsMap.get("batchNumKey") != null){
								examineSms = examineSmsMap.get("batchNumKey");
								isExamineFlag = 1;
							}else if(examineSmsMap.get("disagreeNumKey") != null){
								examineSms = examineSmsMap.get("disagreeNumKey");
								isExamineFlag = 2;
							}
							if(examineSms == null){
								resultStr = "fail";
								EmpExecutionContext.error("["+sdf.format(new Date())+"] 获取短信提醒信息失败！");
							}
							//审批意见
							String comments = "";
							if(msg.length()>4)
							{
								comments=msg.substring(4);
								if(comments.indexOf("+")==0)
								{
									comments=comments.substring(1);
								}
							}
							if("success".equals(resultStr)){
								//获取该条短信提醒记录
								LfFlowRecord flowRecord = null;

								if(examineSms != null){
									flowRecord = baseBize.getById(LfFlowRecord.class,examineSms.getFrId());
								}

								if(flowRecord == null){
									EmpExecutionContext.error("["+sdf.format(new Date())+"] 获取审批流程实时记录失败！");
									resultStr = "fail";
								}else{
									flowRecord.setComments(comments);
									flowRecord.setRState(isExamineFlag);
									flowRecord.setReviewer(flowRecord.getUserCode());
									flowRecord.setRTime(new Timestamp(System.currentTimeMillis()));
									flowRecord.setPreRv(flowRecord.getUserCode());
									if(flowRecord.getIsComplete() == 1)
									{
										content = "该任务已被处理不能重复审批！";
										resultStr = "errer";
										EmpExecutionContext.error("["+sdf.format(new Date())+"] 该任务已被处理不能重复审批！");
									}else{
										Long addtime = getAddTime(flowRecord);
										if(addtime == null){
											EmpExecutionContext.error("["+sdf.format(new Date())+"] 获取任务时间失败！");
											resultStr = "fail";
										}else{
											if(System.currentTimeMillis()-addtime<48*60*60*1000)
											{
												content = examineMsgByType(flowRecord);
											}else{
												content = "对不起，您的审批指令已过期，系统不再审批！";
												resultStr = "errer";
												EmpExecutionContext.error("["+sdf.format(new Date())+"] 对不起，您的审批指令已过期，系统不再审批！");
											}
										}
									}
								}
							}
						}
					}else{
						EmpExecutionContext.error("["+sdf.format(new Date())+"] 上行记录没有内容");
						resultStr = "fail";
					}
				}
				return true;
			}catch (Exception e) {
				EmpExecutionContext.error(e,"上行记录");
				return true;
			}finally{
				 if(resultStr != null && null != examineSms && !"fail".equals(resultStr) && !"fail".equals(content) && !"".equals(content)){
                     sendDownResultMsg(moTask, examineSms, content, resultStr);
				 }
			}
	}


	/**
	 *  判断上行记录是否合法
	 * @param moTask
	 * @return
	 */
	private String judgeMotask(LfMotask moTask){
		String returnmsg = "success";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(moTask.getPhone()==null||"".equals(moTask.getPhone()))
			{
				//不执行审批操作
				returnmsg="fail";
				EmpExecutionContext.error("["+sdf.format(new Date())+"] 上行电话号码为空");
			}
			else if(moTask.getCorpCode()==null||"".equals(moTask.getCorpCode()))
			{
				returnmsg = "fail";
				EmpExecutionContext.error("["+sdf.format(new Date())+"] 上行信息企业编码为空");
			}
			else if(moTask.getSpUser()==null||"".equals(moTask.getSpUser()))
			{
				returnmsg = "fail";
				EmpExecutionContext.error("["+sdf.format(new Date())+"] 上行账号为空");
			}
			else if(moTask.getDeliverTime()==null)
			{
				returnmsg = "fail";
				EmpExecutionContext.error("["+sdf.format(new Date())+"] 上行时间为空");
			}
		}catch (Exception e) {
			returnmsg = "fail";
			EmpExecutionContext.error(e,"判断上行记录是否合法");
		}
		return returnmsg;
	}


	/**
	 *  解析内容
	 * @param content	内容
	 * @param moTask	上行对象
	 * @return
	 */
	private LinkedHashMap<String,LfExamineSms> analysisMsg(String content,LfMotask moTask){
		try{
			BaseBiz baseBiz = new BaseBiz();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String,LfExamineSms> examineSmsMap = new LinkedHashMap<String, LfExamineSms>();
			List<LfExamineSms>  examineSmsList = null;
			LfExamineSms examineSms = null;
			//获取指令
			String instruction= content.substring(0,4).toUpperCase();
			conditionMap.put("batchNumber",instruction);
			conditionMap.put("phone",moTask.getPhone());
			conditionMap.put("spNumber",moTask.getSpnumber());
			orderbyMap.put("frId", StaticValue.DESC);
			examineSmsList = baseBiz.getByCondition(LfExamineSms.class, conditionMap, orderbyMap);
			if(examineSmsList != null && examineSmsList.size()>0){
				examineSms = examineSmsList.get(0);
				examineSmsMap.put("batchNumKey", examineSms);
				return examineSmsMap;
			}else{
				conditionMap.remove("batchNumber");
				conditionMap.put("disagreeNumber", instruction);
				examineSmsList = baseBiz.getByCondition(LfExamineSms.class, conditionMap, orderbyMap);
				if(examineSmsList != null && examineSmsList.size()>0){
					examineSms = examineSmsList.get(0);
					examineSmsMap.put("disagreeNumKey", examineSms);
					return examineSmsMap;
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"解析内容");
		}
		return null;
	}


	/**
	 *   获取该任务 该模板 的创建时间
	 * @param record
	 * @return
	 */
	private Long getAddTime(LfFlowRecord record){
		Long addtime = null;
		try{
			//短彩信模板任务
			BaseBiz baseBiz = new BaseBiz();
			if(record.getRLevel() == 1){
//				//短信 彩信
//				if(record.getInfoType() == 1 || record.getInfoType() == 2){
//					LfMttask lfMttask = baseBiz.getById(LfMttask.class, record.getMtId());
//					if(lfMttask != null){
//						addtime = lfMttask.getSubmitTime().getTime();
//					}
//				//短彩模板
//				}else if(record.getInfoType() == 3 || record.getInfoType() == 4){
//					LfTemplate lfTemplate = baseBiz.getById(LfTemplate.class, record.getMtId());
//					if(lfTemplate != null){
//						addtime = lfTemplate.getAddtime().getTime();
//					}				
//				}else 
					if(record.getInfoType() == 6){// 通过网讯模板ID找到网讯的创建时间
						LfWXBASEINFO LfWXBASEINFO = baseBiz.getById(LfWXBASEINFO.class, record.getMtId());
						if(LfWXBASEINFO != null){
							addtime = LfWXBASEINFO.getCREATDATE().getTime();
						}
				}
			}else if(record.getRLevel() > 1){
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("RState&in", "1,2");
				conditionMap.put("FId", String.valueOf(record.getFId()));
				conditionMap.put("mtId", String.valueOf(record.getMtId()));
				conditionMap.put("RLevel", String.valueOf(record.getRLevel()-1));
				conditionMap.put("infoType", String.valueOf(record.getInfoType()));
				conditionMap.put("isComplete", "1");
				List<LfFlowRecord> records = empDao.findListBySymbolsCondition(LfFlowRecord.class, conditionMap, null);
				if(records != null && records.size()>0){
					addtime = records.get(0).getRTime().getTime();
				}
			}
		}catch (Exception e) {
			addtime = null;
			EmpExecutionContext.error(e,"获取该任务 该模板 的创建时间");
		}
		return addtime;
	}



	/**
	 *  对任务进行审批
	 * @param record
	 * @return
	 */
	private String examineMsgByType(LfFlowRecord record){
		String returnmsg = "";
		try{
			BaseBiz baseBiz = new BaseBiz();
			LfSysuser createteakuser = baseBiz.getById(LfSysuser.class, record.getProUserCode());
			if(record.getInfoType() == 6){
				//审核方法 返回状态，
				boolean isflag = new NetCheckBiz().reviewTemplate(record.getFrId(),record.getRState(),record.getComments());
				if(isflag){
					LfTemplate template = baseBiz.getById(LfTemplate.class, record.getMtId());
					returnmsg = getDownReviewContent(createteakuser, record.getInfoType(), record.getRState(), null, template);
				}else{
					returnmsg = "审批失败！";
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"对任务进行审批");
		}
		return returnmsg;
	}



	/**
	 *  获取下行的内容
	 * @param createuser 任务提交人
	 * @param type	发送类型
	 * @param isAuditFlag	是否同意
	 * @param lfMttask	任务
	 * @param template	模板对象
	 * @return
	 */
	private String getDownReviewContent(LfSysuser createuser,Integer type,Integer isAuditFlag,
			LfMttask lfMttask,LfTemplate template){
		String msgContent = "";
		try{
			String bmt = "";
			String date = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(type == 1){
				Integer bmtType=lfMttask.getBmtType();
				if(bmtType==1)
				{
					bmt="相同内容群发";
				}else if(bmtType==2)
				{
					bmt="动态模板短信群发";
				}else if(bmtType==3)
				{
					bmt="文件内容短信群发";
				}
				else if(bmtType==4)
				{
					bmt="客户群组群发";
				}
				date = lfMttask.getSubmitTime()==null?"":sdf.format(lfMttask.getSubmitTime());
			}else if(type == 2){
				bmt="彩信群发";
				date = lfMttask.getSubmitTime()==null?"":sdf.format(lfMttask.getSubmitTime());
			}else if(type == 3){
				bmt="短信模板";
				date = template.getAddtime()==null?"":sdf.format(template.getAddtime());
			}else if(type == 4){
				bmt="彩信模板";
				date = template.getAddtime()==null?"":sdf.format(template.getAddtime());
			}else if(type == 6){
				bmt="网讯模板";
			}
			String a = "";
			if(isAuditFlag == 1){
				a = "审批通过";
			}else if(isAuditFlag == 2){
				a = "审批不通过";
			}
			if(type == 6){
				msgContent = "["+createuser.getName()+" 提交的"+bmt+"审批任务]"+a+"处理意见已被系统成功接收！";
			}else{
				msgContent = "["+createuser.getName()+" "+date+"提交的"+bmt+"审批任务]"+a+"处理意见已被系统成功接收！";
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取下行的内容");
			msgContent = "";
		}
		return msgContent;
	}

	/**
	 *   下发信息
	 * @param moTask	上行对象
	 * @param examineSms	短醒提醒对象
	 * @param content	内容
	 * @param errerStr	错误串
	 */
	private void sendDownResultMsg(LfMotask moTask,
			LfExamineSms examineSms,String content,String errerStr){
		try{
			boolean flag = SystemGlobals.isDepBilling(moTask.getCorpCode());
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		 	conditionMap.put("userName", "admin");//admin用户
			conditionMap.put("corpCode", moTask.getCorpCode());//当前登录用户的企业编码
			List<LfSysuser> adminLfSysuerList = empDao.findListByCondition(LfSysuser.class, conditionMap, null) ;
			LfSysuser adminUser = null;
			if(adminLfSysuerList!=null && adminLfSysuerList.size()>0)
			{
				//系统admin
				adminUser = adminLfSysuerList.get(0);
			}else{
				return ;
			}
	    	//获取SPUSER 以及 尾号
			String[] arr = getSpuserSubnoByCorpCode(moTask.getCorpCode(),adminUser);
			String spUser = "";
			String subno = "";
			if(arr != null && "success".equals(arr[0])){
				subno = arr[1];
				spUser = arr[2];
			}else{
				if(arr != null && "fail".equals(arr[0])){
					EmpExecutionContext.error("审批提醒获取发送账号失败！");
				}else if(arr != null && "nosubno".equals(arr[0])){
					EmpExecutionContext.error("审批提醒没有可用的尾号！");
				}
				return;
			}
			if(!"".equals(spUser) &&  spUser != null)
			{
				WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
				String[] haoduans= wgMsgBiz.getHaoduan();
				SmsBiz smsbiz=new SmsBiz();
				//拿到当前系统配置的号段
				//List<GtPortUsed> portsList = smsbiz.getPortByUserId(spUser);
				//计算条数
				int messageCount = smsbiz.countAllOprSmsNumber(content, moTask.getPhone(), haoduans, spUser);
				WGParams wgParams = new WGParams();
				wgParams.setCommand("MT_REQUEST");
				wgParams.setSpid(spUser);
				Userdata userdata = wgMsgBiz.getSmsUserdataByUserid(spUser);
				wgParams.setSppassword(userdata.getUserPassword());
				wgParams.setParam1(adminUser.getUserCode());
				wgParams.setSa(subno);
				wgParams.setDa(moTask.getPhone());
				wgParams.setPriority("1");
				wgParams.setSm(content);
				wgParams.setSvrtype("M00000");
				wgParams.setModuleid(StaticValue.VERIFYREMIND_MENUCODE);
				wgParams.setRptflag("0");
				if("success".equals(errerStr)){
					if(examineSms!=null&&messageCount >0){
                            examineSms.setReciveContent(moTask.getMsgContent());
                            examineSms.setReciveTime(moTask.getDeliverTime());
                            sendMsgToOne(examineSms,wgParams, messageCount, adminUser,flag);
					}
				}else if("errer".equals(errerStr)){
                    sendMsgToOne(null,wgParams, messageCount, adminUser,flag);
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"下发信息");
		}
	}



	/**
	 *   发送方法
	 * @param lfExamineSms  保存的短信提醒信息
	 * @param sp	发送帐号
	 * @param subno	尾号
	 * @param count	条数
	 * @param flag	是否计费
	 * @param Rptflag	是否状态报告
	 * @param menuCode	模块ID
	 * @param phone	手机号码串
	 * @param msg	内容
	 * @param lfSysuser	admin对象
	 * @return
	 */
	private String sendMsgToOne(LfExamineSms lfExamineSms,WGParams wgParams,Integer count,LfSysuser lfSysuser,boolean flag) {
		String result = "2";
		//如果扣费开启
		if(flag){
			//获得数据库连接
			Connection conn = empTransDao.getConnection();
			try {
				//开启事物
				empTransDao.beginTransaction(conn);
				int resultMsg = new BalanceLogBiz().sendSmsAmountByGuid(conn, lfSysuser.getGuId(), count);
				if(resultMsg==1){
					String[] wegGateResult= examineSendMsg(wgParams);
					if(wegGateResult == null || wegGateResult.length == 0){
						empTransDao.rollBackTransaction(conn);
						//未发送成功
						result = "2";
					}else if("000".equals(wegGateResult[0]) ){
						//发送到网关
						result = "1";
						if(lfExamineSms != null)
						{
							empTransDao.save(conn, lfExamineSms);
						}
						empTransDao.commitTransaction(conn);
					}else{
						empTransDao.rollBackTransaction(conn);
					}
				}else{
					empTransDao.rollBackTransaction(conn);
				}
			} catch (Exception e) {
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"网讯发送");
			}finally{
				empTransDao.closeConnection(conn);
			}
		}else{
			try {
				String[] wegGateResult= examineSendMsg(wgParams);
				if(wegGateResult == null || wegGateResult.length == 0){
					//未发送成功
					result = "2";
				}
				if("000".equals(wegGateResult[0]) ){
					//发送到网关
					result = "1";
					if(lfExamineSms!=null)
					{
						empDao.save(lfExamineSms);
					}
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"网讯保存");
			}
		}
		return result;
	}

	/**
	 *   单发
	 * @param wgParams
	 * @return
	 * @throws Exception
	 */
	private String[] examineSendMsg(WGParams wgParams) throws Exception {
		HttpSmsSend jobBiz = new HttpSmsSend();
		String[] resultReceive = new String[2];
		String webGateResult = jobBiz.createbatchMtRequest(wgParams);
		resultReceive[1] = parseWebgateResult(webGateResult, "mtmsgid");
		int index = webGateResult.indexOf("mterrcode");
		resultReceive[0] = webGateResult.substring(index+10,index+13);
		if(!resultReceive[0].equals("000")){
			resultReceive[0] = webGateResult.substring(index-8,index-1);
			EmpExecutionContext.debug("审批提醒或发送动态口令到短信网关返回错误代码："+resultReceive[0]);
		}
		return resultReceive;
	}


	private String parseWebgateResult(String webGateResult, String param) {
		String[] testArray = webGateResult.split("&");
		String strParam;
		int index = -1;
		String value ="";
		for(int i = 0; i < testArray.length; i++){
			strParam = testArray[i];
			index = strParam.indexOf(param+"=");
			if(index == -1){
				continue;
			}

			value = strParam.substring(index+8);
			break;
		}

		return value;
	}



	/**
	 *  通过企业编码查询出所对应的SPUSER以及尾号
	 * @param corpCode 企业编码
	 * @param lguserid	审批人用户ID
	 * @return
	 */
	private String[] getSpuserSubnoByCorpCode(String corpCode,LfSysuser AdminUser){
			String[] arr = new String[3];
		try{
			arr[0] = "fail";
			//sp账号
			String sp="";
			String isUsable="";
			ErrorCodeParam errorCode =new ErrorCodeParam();
			//扩展尾号
			String subno=GlobalVariableBiz.getInstance().getValidSubno(StaticValue.VERIFYREMIND_MENUCODE, 0, corpCode,errorCode);
	    	//获取sp账号
			List<Userdata> spUserList= new SmsBiz().getSpUserListForReviewRemind(AdminUser);
			//spuserlist为空说明没有可用的账号
			//验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由
			//	发送账号
			//isUsable	1通过 	2失败  没有绑定一条上下行路由/下行路由	3失败 	发送账号没有绑定路由	 4 该发送账号的全通道号有一个超过20位
			if(spUserList.size()>0)
			{
				for(Userdata spUser : spUserList)
				{
					if(subno != null)
					{
						isUsable= new SubnoManagerBiz().checkPortUsed(spUser.getUserId(), subno);
						if("1".equals(isUsable)){
							sp=spUser.getUserId();
							arr[0] = "success";
							arr[1] = subno;
							arr[2] = sp;
							break;
						}
					}else{
						arr[0] = "nosubno";
						throw new Exception("子号已使用完！");
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"通过企业编码查询出所对应的SPUSER以及尾号");
			arr[0] = "fail";
		}
		return arr;
	}















}
