package com.montnets.emp.common.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.Message;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.email.Aes;
import com.montnets.emp.email.MailInfo;
import com.montnets.emp.email.SendMail;
import com.montnets.emp.entity.approveflow.LfExamineSms;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.netnews.LfWXBASEINFO;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;



/**
 * 审批提醒biz
 * @author 
 *
 */
public class ReviewRemindBiz extends SuperBiz {

	//private GenericLfFlowRecordVoDAO lfFlowRecordVoDAO;
	//private GenericLfFlowRecordTemplateVoDAO lfFlowRecordTemplateVoDAO;
	private PhoneUtil phoneUtil = new PhoneUtil();

	/**
	 * 单发短信
	 * @param lfExamineSms    null
	 * @param msgInfo   发送内容
	 * @param corpCode   企业编码
	 * @param sp     发送账户
	 * @param subno   尾号
	 * @param balanceBiz    
	 * @param count    发送条数
	 * @param senderUser      发送者
	 * @param flag    是否计费
	 * @param Rptflag  是否需要返回状态报告
	 * @return
	 */
	public String imSendMsgOneByOne(LfExamineSms lfExamineSms,Message msgInfo, String corpCode,String sp ,String subno,BalanceLogBiz balanceBiz,Integer count,Long senderId,boolean flag,String Rptflag) {
		String result = "2";
		LinkedHashMap< String, String> conMap=new LinkedHashMap<String, String>();
		//企业编码
		conMap.put("corpCode", corpCode);
		conMap.put("userName","admin");
		LfSysuser lfSysuser=new LfSysuser();
		try {
			//admin的用户信息
			lfSysuser = empDao.findListByCondition(LfSysuser.class, conMap,null ).get(0);
		} catch (Exception e1) {
			EmpExecutionContext.error(e1, "查询操作员失败！");
		}
		
		//检查sp余额是否足够发送
		int spResult = balanceBiz.checkSpUserFee(sp, count, 1);
		if(spResult<0){
			if(spResult == -3)
			{
				//没有账号信息
				EmpExecutionContext.error("查询不到sp账号信息。spuser:"+sp);
				return "noSpInfo";
			}
			else if(spResult == -2)
			{
				//余额不足
				EmpExecutionContext.error("sp账号余额不足。spuser:"+sp);
				return "noSuffiSpFee";
			}else{
				EmpExecutionContext.error("查询sp账号信息异常。spuser:"+sp);
				return "spFail";
			}
		}
		
		//如果扣费开启
		if(flag){
			//获得数据库连接
			Connection conn = empTransDao.getConnection();
			try {
				//开启事物
				empTransDao.beginTransaction(conn);
				//String resultMsg = balanceBiz.sendSmsAmount(conn, senderId, Long.valueOf(count));
				int resultMsg = balanceBiz.sendSmsAmountByGuid(conn, lfSysuser.getGuId(),count);
				//0:短信扣费成功；
				if(resultMsg == 0){
					String[] wegGateResult = this.imSendMsg(null, msgInfo.getMenuCode(), msgInfo.getDialogId(), msgInfo.getTaskId(), lfSysuser.getUserCode(), msgInfo.getDepId(), msgInfo.getName(), msgInfo.getTelPhoneNo(), msgInfo.getContent(), null, corpCode,sp,subno,Rptflag);
					if(wegGateResult == null || wegGateResult.length == 0){
						empTransDao.rollBackTransaction(conn);
						//未发送成功
						result = "2";				
					}
					else if("000".equals(wegGateResult[0]) ){
						//发送到网关
						result = "1";			
						if(lfExamineSms!=null)
						{
							empTransDao.save(conn, lfExamineSms);
						}
						empTransDao.commitTransaction(conn);
					}else{
						empTransDao.rollBackTransaction(conn);
					}
				}else if(resultMsg == -2){
					//短信余额不足
					result = "6";				
					empTransDao.rollBackTransaction(conn);
				}else if(resultMsg == -1){
					//短信扣费失败
					result = "7";				
					empTransDao.rollBackTransaction(conn);
				}else if(resultMsg == -4){
					//用户所属机构没有充值
					result = "9";				
					empTransDao.rollBackTransaction(conn);
				}else{
					empTransDao.rollBackTransaction(conn);
				}
			} catch (Exception e) {
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"单发短信失败！");
			}finally{
				empTransDao.closeConnection(conn);
			}
		}else{
			//获得数据库连接
			//Connection conn = empTransDao.getConnection();
			try {
				String[] wegGateResult = this.imSendMsg(null, msgInfo.getMenuCode(), msgInfo.getDialogId(), msgInfo.getTaskId(), lfSysuser.getUserCode(), msgInfo.getDepId(), msgInfo.getName(), msgInfo.getTelPhoneNo(), msgInfo.getContent(), null, corpCode,sp,subno,Rptflag);
				if(wegGateResult == null || wegGateResult.length == 0){
					//未发送成功
					result = "2";				
				}
				if("000".equals(wegGateResult == null ? null : wegGateResult[0]) ){
					//发送到网关
					result = "1";			
					if(lfExamineSms!=null)
					{
						empDao.save(lfExamineSms);
					}
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"单发短信失败！");
			}
		}
		return result;
	}
	
	/**
	 * 登录时发送动态口令的方法
	 * @param lfExamineSms    null
	 * @param msgInfo   发送内容
	 * @param corpCode   企业编码
	 * @param sp     发送账户
	 * @param subno   尾号
	 * @param balanceBiz    
	 * @param count    发送条数
	 * @param senderUser      发送者
	 * @param flag    是否计费
	 * @param Rptflag  是否需要返回状态报告
	 * @return
	 */
	public String LoginSendMsgOneByOne(LfExamineSms lfExamineSms,Message msgInfo, String corpCode,String sp ,String subno,BalanceLogBiz balanceBiz,Integer count,LfSysuser senderUser,boolean flag,String Rptflag) {
		String result = "2";
        //如果启用了计费
		if(flag){
			Connection conn = empTransDao.getConnection();
			try {
				empTransDao.beginTransaction(conn);
				//先进行扣费操作
				int resultMsg = balanceBiz.sendSmsAmountByGuid(conn, senderUser.getGuId(), count);
				//0:短信扣费成功；
				if(resultMsg == 0){
					//扣费成功，则进行发送操作
					String[] wegGateResult = this.imSendMsg(null, msgInfo.getMenuCode(), msgInfo.getDialogId(), msgInfo.getTaskId(), senderUser.getUserCode(), msgInfo.getDepId(), msgInfo.getName(), msgInfo.getTelPhoneNo(), msgInfo.getContent(), null, corpCode,sp,subno,Rptflag);
					if(wegGateResult == null || wegGateResult.length == 0){
						empTransDao.rollBackTransaction(conn);
						//未发送成功
						result = "2";				
					}
					else if("000".equals(wegGateResult[0]) ){
						//发送到网关成功
						result = "1,"+wegGateResult[1];			
						if(lfExamineSms!=null)
						{
							empTransDao.save(conn, lfExamineSms);
						}
						empTransDao.commitTransaction(conn);
					}else{
						empTransDao.rollBackTransaction(conn);
					}
				}else if(resultMsg == -2){
					//短信余额不足
					result = "6";				
					empTransDao.rollBackTransaction(conn);
				}else if(resultMsg == -1){
					//短信扣费失败
					result = "7";				
					empTransDao.rollBackTransaction(conn);
				}else if(resultMsg == -4){
					//用户所属机构没有充值
					result = "9";				
					empTransDao.rollBackTransaction(conn);
				}else{
					empTransDao.rollBackTransaction(conn);
				}
			} catch (Exception e) {
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"登录时发送动态口令出现异常！");
			}finally{
				empTransDao.closeConnection(conn);
			}
		}else{
			//Connection conn = empTransDao.getConnection();
			try {
				//调用发送接口
				String[] wegGateResult = this.imSendMsg(null, msgInfo.getMenuCode(), msgInfo.getDialogId(), msgInfo.getTaskId(), senderUser.getUserCode(), msgInfo.getDepId(), msgInfo.getName(), msgInfo.getTelPhoneNo(), msgInfo.getContent(), null, corpCode,sp,subno,Rptflag);
				if(wegGateResult == null || wegGateResult.length == 0){
					//未发送成功
					result = "2";				
				}
				if("000".equals(wegGateResult == null ? null : wegGateResult[0]) ){
					//发送到网关成功
					result = "1,"+wegGateResult[1];			
					if(lfExamineSms!=null)
					{
						empDao.save(lfExamineSms);
					}
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"登录时发送动态口令出现异常！");
			}
		}
		return result;
	}

	/**
	 *    对起发送配置判断
	 * @param url
	 * @param menuCode		MENUCODE
	 * @param dialogId		
	 * @param taskId		任务ID
	 * @param userCode		用户编码
	 * @param depId			
	 * @param userLoginName	
	 * @param phone			手机号码
	 * @param msg			短信内容
	 * @param phones		
	 * @param corpCode		企业编码
	 * @param subno			子号详情类
	 * @return
	 * @throws Exception
	 */
	private String[] imSendMsg(String url, String menuCode, String dialogId, Long taskId, String userCode, Long depId, String userLoginName, String phone, String msg, String phones, String corpCode,String sp,String subno,String Rptflag) throws Exception {
		int messageCount = 0;
		try
		{
			//运营商号段
			String[] haoduans = new WgMsgConfigBiz().getHaoduan();
			//List<GtPortUsed> portsList = new SmsBiz().getPortByUserId(sp);
			messageCount = new SmsBiz().countAllOprSmsNumber(msg, phone, haoduans, sp);
			//先执行运营商扣费
			boolean kf = this.wgKoufei(messageCount, sp, corpCode);
			if(!kf){
				return null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "运营商扣费过程失败！");
			return null;
		}
		if( (url == null || "".equals(url.trim())) && (phone == null || "".equals(phone.trim())) && (phones == null || "".equals(phones.trim())) ){
			EmpExecutionContext.error("发送号码或文件为空！");
			return null;
		}
		WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
		HttpSmsSend jobBiz = new HttpSmsSend();
		String[] resultReceive = new String[2];
		if(subno == null){
			EmpExecutionContext.error("子号对象为空！");
			return null;
		}
		Userdata userdata = wgMsgBiz.getSmsUserdataByUserid(sp);
		String webGateResult;
			WGParams wgParams = new WGParams();
			wgParams.setCommand("MT_REQUEST");
			//spuser帐户
			wgParams.setSpid(sp);	
			//密码
			wgParams.setSppassword(userdata.getUserPassword());	
			//用户编码
			wgParams.setParam1(String.valueOf(userCode));	
			//拓展子号
			wgParams.setSa(subno);		
			//手机
			wgParams.setDa(phone);	
			//任务ID
			//wgParams.setTaskid(taskId.toString());
			//优先级（越小优先级越高）
			wgParams.setPriority("1");   
			//短信内容
			wgParams.setSm(msg);   
			//默认业务编码      
			wgParams.setSvrtype("M00000");	
			//模块ID
			wgParams.setModuleid(menuCode);	
			//设值为0的不需要状态报告，其他值是需要状态报告
			wgParams.setRptflag(Rptflag);		
			//wgParams.setMsgid(taskId.toString());
			webGateResult = jobBiz.createbatchMtRequest(wgParams);
			resultReceive[1] = this.parseWebgateResult(webGateResult, "mtmsgid");
		int index = webGateResult.indexOf("mterrcode");
		resultReceive[0] = webGateResult.substring(index+10,index+13);
		if(!resultReceive[0].equals("000")){
			resultReceive[0] = webGateResult.substring(index-8,index-1);
			EmpExecutionContext.debug("审批提醒或发送动态口令到短信网关返回错误代码："+resultReceive[0]);
			//运营商扣费回收
			new BalanceLogBiz().huishouFee(messageCount,sp,1);
		}
		return resultReceive;
	}
	
	/**
	 * 发送接口，下行手机审批的结果
	 * @param sp sp账号
	 * @param subno 子号
	 * @param msg 内容
	 * @param phone 手机号
	 * @param userCord 用户编码
	 * @return
	 * @throws Exception
	 */
	public String[] sendmsg(String sp,String subno,String msg,String phone,String userCord) throws Exception {
		WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
		HttpSmsSend jobBiz = new HttpSmsSend();
		String[] resultReceive = new String[2];
		Userdata userdata = wgMsgBiz.getSmsUserdataByUserid(sp);
		String webGateResult;
		WGParams wgParams = new WGParams();
		wgParams.setCommand("MT_REQUEST");
		//spuser帐户
		wgParams.setSpid(sp);
		//密码
		wgParams.setSppassword(userdata.getUserPassword());	
		//用户编码
		wgParams.setParam1(userCord);	
		//拓展子号
		wgParams.setSa(subno);		
		//手机
		wgParams.setDa(phone);			
		//任务ID
		//wgParams.setTaskid(taskId.toString());	
		wgParams.setPriority("1");
		wgParams.setSm(msg);
		//默认业务编码
		wgParams.setSvrtype("M00000");	
		//模块ID
		wgParams.setModuleid(StaticValue.VERIFYREMIND_MENUCODE);	
		//设值为0的不需要状态报告，其他值是需要状态报告
		wgParams.setRptflag("0");		
		//wgParams.setMsgid(taskId.toString());
		webGateResult = jobBiz.createbatchMtRequest(wgParams);
		resultReceive[1] = this.parseWebgateResult(webGateResult, "mtmsgid");
		int index = webGateResult.indexOf("mterrcode");
		resultReceive[0] = webGateResult.substring(index+10,index+13);
		if(!resultReceive[0].equals("000")
		){
			resultReceive[0] = webGateResult.substring(index-8,index-1);
			EmpExecutionContext.debug("审批提醒到短信网关返回错误代码："+resultReceive[0]);
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
	
	/*
	 * 短彩信审批提醒接口方法
	 */
	public void reviewRemind(LfFlowRecord lfFlowRecord,Long mtId,int state)
	{
		try
			{
			SmsBiz smsbiz=new SmsBiz();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//短信提醒内容
			String content="";
			//短信任务
			LfMttask mt=empDao.findObjectByID(LfMttask.class, mtId);
			//当前操作员的信息
			LfSysuser currUser=null;
	    	LinkedHashMap<String, String> conMap=new LinkedHashMap<String, String>();
	    	conMap.put("mtId",mt.getMtId().toString());
	    	//如果是任务创建人，当前用户通过任务表里的创建人id查出当前操作员信息，如果是审批人，则通过审批流程表里的审批人id查出当前操作员
	    	//state =1 :审批人  2：任务创建人
	    	if(state==1)
	    	{
	    		currUser=empDao.findObjectByID(LfSysuser.class, lfFlowRecord.getReviewer());
	    		conMap.put("preRv", lfFlowRecord.getReviewer().toString());
	    	}else if(state==2)
	    	{
	    		currUser=empDao.findObjectByID(LfSysuser.class, mt.getUserId());
	    		conMap.put("RLevel", "1");
	    	}
	    	conMap.put("reviewType", mt.getMsType().toString());
	    	
	    	Message lfImMessage = new Message();
	    	WgMsgConfigBiz wgMsgConfigBiz=new WgMsgConfigBiz();
	    	SmsBiz smsBiz=new SmsBiz();
	    	BalanceLogBiz balanceBiz = new BalanceLogBiz();
	    	SubnoManagerBiz subnoManagerBiz = new SubnoManagerBiz();
	    	
	    	//创建人信息
	    	LfSysuser lfSysuserCreate=empDao.findObjectByID(LfSysuser.class, mt.getUserId());
	    	
	    	//手机号码
			String moblie="";
			//批次号(同意)
			String batchNumber="";
			//批次号(不同意)
			String disagreeNumber="";
	    	LfSysuser lfSysuser=null;
	    	LfFlowRecord nextlfFlowRecord=null;
	    	if(lfFlowRecord!=null&&lfFlowRecord.getRLevel() == lfFlowRecord.getRLevelAmount())
	    	{
	    		//审批最后一级，电话号码为创建者的号码
	    		moblie=lfSysuserCreate.getMobile();
	    	}else
	    	{
	    		
	    		//下一级审批流程
	    		nextlfFlowRecord= empDao.findListByCondition(LfFlowRecord.class, conMap, null).get(0);
	    		//下一级审批人信息
	    		lfSysuser=empDao.findObjectByID(LfSysuser.class, nextlfFlowRecord.getReviewer());
	    		batchNumber=nextlfFlowRecord.getBatchNumber().toString().toUpperCase();
	    		disagreeNumber=nextlfFlowRecord.getDisagreeNumber().toUpperCase();
	    		moblie=lfSysuser.getMobile();
	    	}
	    	
	    	//企业编码
			String corpCode = currUser.getCorpCode();
			LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
			conditionMap1.put("userName", "admin");//admin用户
			conditionMap1.put("corpCode", corpCode);//当前登录用户的企业编码
			List<LfSysuser> AdminLfSysuerList = empDao.findListByCondition(LfSysuser.class, conditionMap1, null) ;
			LfSysuser AdminUser = null; 
			if(AdminLfSysuerList!=null && AdminLfSysuerList.size()>0)
			{
				AdminUser = AdminLfSysuerList.get(0);
			}
			else
			{
				AdminUser = currUser;
			}
			//sp账号
			String sp="";
			String isUsable="";
			ErrorCodeParam errorCode =new ErrorCodeParam();
			//扩展尾号
			String subno=GlobalVariableBiz.getInstance().getValidSubno(StaticValue.VERIFYREMIND_MENUCODE, 0, corpCode,errorCode);
	    	//获取sp账号
			List<Userdata> spUserList=smsbiz.getSpUserListForReviewRemind(AdminUser);
			//spuserlist为空说明没有可用的账号
			//验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由
			//	发送账号
			//isUsable	1通过 	2失败  没有绑定一条上下行路由/下行路由	3失败 	发送账号没有绑定路由	 4 该发送账号的全通道号有一个超过20位
			if(spUserList.size()>0)
			{
				for(Userdata spUser : spUserList)
				{
					if(subno!=null)
					{
						isUsable=subnoManagerBiz.checkPortUsed(spUser.getUserId(), subno);
						if("1".equals(isUsable))
						{
							sp=spUser.getUserId();
							break;
						}
					}
					else
					{
						throw new Exception("子号已使用完！");
					}
				}
			}
			
			if(!"".equals(sp)&&sp!=null)
			{
				
				//拿到当前系统配置的号段
				String[] haoduans=wgMsgConfigBiz.getHaoduan();	
				//过滤号段
				if(phoneUtil.checkMobile(moblie,haoduans) == 1)
				{
					//短信内容
					Integer bmtType=mt.getBmtType();
					//预发送条数
					String sendCount = "0";
					String bmt="相同内容群发";
					String msgContents=mt.getMsg();
					if(msgContents!=null&&msgContents.length()>797)
					{
						msgContents=msgContents.substring(0,797)+"...";
					}
					if(mt.getMsType()==1){
						
						if(bmtType==1)
						{
							bmt="相同内容群发";
						}else if(bmtType==2)
						{
							bmt="动态模板短信群发";
						}else if(bmtType==3)
						{
							bmt="文件内容短信群发";
							msgContents="详见文件";
						}
						else if(bmtType==4)
						{
							bmt="客户群组群发";
							bmtType=1;
							
						}
						sendCount=smsBiz.countAllOprSmsNumber(mt.getSpUser(), mt.getMsg(), bmtType, mt.getMobileUrl(), null)+"";
						
					}
					else if(mt.getMsType()==2)
					{
						bmt="彩信群发";
						sendCount=mt.getEffCount().toString();
						msgContents=mt.getTaskName();
					}
					if(lfFlowRecord!=null&&lfFlowRecord.getRLevel().equals(lfFlowRecord.getRLevelAmount()))
					{
						//content =this.getLastMsgContent(null, sdf.format(mt.getSubmitTime()), bmt, mt.getTitle(), null);
					}else
					{
						//TODO
						content=this.getMsgContent(lfSysuserCreate.getName(), sdf.format(mt.getSubmitTime()), bmt, mt.getTitle(), msgContents,sendCount, mt.getEffCount(),batchNumber,disagreeNumber);
					}
//				content="您有信息需要审批：提交人："+lfSyssuser1.getUserName()+"，"+mt.getSubmitTime()+bmt+"审核任务，主题："+mt.getTitle()+"[内容]："+mt.getMsg()+"[总条数/号码数]："+mt.getIcount()
//				+"/"+mt.getEffCount()+"，批次号"+lfFlowRecord.getFrId()+"，短信审核当天回复有效，通过审核回复Y#，不通过审核回复N#+意见";
					if(content!=null&&!"".equals(content))
					{
						lfImMessage.setContent(content);
						lfImMessage.setMenuCode(StaticValue.VERIFYREMIND_MENUCODE );
						lfImMessage.setTelPhoneNo(moblie);
						//lfImMessage.setTaskId(lfFlowRecord.getFrId());
						//早期拿子号信息的方法
						//LfSubnoAllot subno = subnoManagerBiz.getSubnoAllotByCodes(lfImMessage.getMenuCode(), 0, corpCode);
						//Map<Integer, LfSubnoAllotDetail> map = subnoManagerBiz.getSubnoDetailMap(spId, lfImMessage.getMenuCode(), 0, corpCode);
						//Integer spisuncm = wgMsgConfigBiz.getPhoneSpisuncm(moblie);
						//LfSubnoAllotDetail detail = map.get(spisuncm);
						//是否计费
						boolean flag = SystemGlobals.isDepBilling(corpCode);		
						//List<GtPortUsed> portsList = null;
						//portsList = smsbiz.getPortByUserId(sp);
						//计算条数
						int messageCount = smsbiz.countAllOprSmsNumber(content, moblie, haoduans, sp);
						if(messageCount>0)
						{
							LfExamineSms lfExamineSms=new LfExamineSms();
							if(nextlfFlowRecord!=null)
							{
								lfExamineSms.setBatchNumber(batchNumber);
								lfExamineSms.setDisagreeNumber(disagreeNumber);
								lfExamineSms.setFrId(nextlfFlowRecord.getFrId());
								lfExamineSms.setMsgContent(content);
								lfExamineSms.setPhone(moblie);
								lfExamineSms.setSpNumber(sp+subno);
								lfExamineSms.setSpUser(sp);
								lfExamineSms.setEsType(1);
							}
							//是需要返回状态报告。
							String Rptflag ="0";
							this.imSendMsgOneByOne(lfExamineSms,lfImMessage,corpCode,sp,subno,balanceBiz,messageCount,currUser.getGuId(),flag,Rptflag);
						}
					}
					else
					{
						//内容为空
					}
				}
				else
				{
					EmpExecutionContext.error("审批提醒的手机号码错误！");
				}
			}
			else
			{
				EmpExecutionContext.error("审批提醒没有可用的发送账号！");
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"短彩信审批提醒接口调用出现异常！");
		}
	}
	
	/**
	 *  模板审批提醒接口方法
	 * @param tempId 模板id
	 * @param state 短信审批级别总共是否1级(1：大于1级，2：等于一级)
	 */
	public void reviewRemindTmp(LfFlowRecord lfFlowRecord,Long tempId,int state)
	{
		try
			{
			SmsBiz smsbiz=new SmsBiz();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String content="";
			//同意批次号
			String batchNumber="";
			//不同意批次号
			String disagreeNumber="";
			//模板内容
			String tempMsg="";
			LfTemplate lfTemplate=empDao.findObjectByID(LfTemplate.class, tempId);
			//当前操作员的信息
			LfSysuser currUser=null;
	    	LinkedHashMap<String, String> conMap=new LinkedHashMap<String, String>();
	    	conMap.put("mtId",lfTemplate.getTmid().toString());
	    	if(state==1)
	    	{
	    		currUser=empDao.findObjectByID(LfSysuser.class, lfFlowRecord.getReviewer());
	    		conMap.put("preRv", lfFlowRecord.getReviewer().toString());
	    	}else if(state==2)
	    	{
	    		currUser=empDao.findObjectByID(LfSysuser.class,lfTemplate.getUserId());
	    		conMap.put("RLevel", "1");
	    	}
	    	conMap.put("reviewType", lfTemplate.getTmpType().toString());
	    	
	    	Message lfImMessage = new Message();
	    	WgMsgConfigBiz wgMsgConfigBiz=new WgMsgConfigBiz();
	    	BalanceLogBiz balanceBiz = new BalanceLogBiz();
	    	SubnoManagerBiz subnoManagerBiz = new SubnoManagerBiz();
	    	//手机号码
			String moblie="";
			//下一级审批人信息
	    	LfSysuser lfSysuser=null;
	    	LfFlowRecord nextlfFlowRecord=null;
	    	//创建人信息
	    	LfSysuser lfSysuserCreate=empDao.findObjectByID(LfSysuser.class, lfTemplate.getUserId());
	    	if(lfFlowRecord!=null&&lfFlowRecord.getRLevel() == lfFlowRecord.getRLevelAmount())
	    	{
	    		//审批最后一级，电话号码为创建者的号码
	    		moblie=lfSysuserCreate.getMobile();
	    	}else
	    	{
	    		
	    		//下一级审批流程
	    		nextlfFlowRecord= empDao.findListByCondition(LfFlowRecord.class, conMap, null).get(0);
	    		//下一级审批人信息
	    		lfSysuser=empDao.findObjectByID(LfSysuser.class, nextlfFlowRecord.getReviewer());
	    		batchNumber=nextlfFlowRecord.getBatchNumber().toUpperCase();
	    		disagreeNumber=nextlfFlowRecord.getDisagreeNumber().toUpperCase();
	    		moblie=lfSysuser.getMobile();
	    	}
	    	if(moblie==null||!"".equals(moblie))
	    	{
	    		String corpCode = lfSysuserCreate.getCorpCode();
	    		
				//sp账号
				String sp="";
				String isUsable="";
				ErrorCodeParam errorCode =new ErrorCodeParam();
				//扩展尾号
				String subno=GlobalVariableBiz.getInstance().getValidSubno(StaticValue.VERIFYREMIND_MENUCODE, 0, corpCode,errorCode);
		    	//获取sp账号
				List<Userdata> spUserList=smsbiz.getSpUserListForReviewRemind(currUser);
				//spuserlist为空说明没有可用的账号
				//验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由
				//	发送账号
				//isUsable	1通过 	2失败  没有绑定一条上下行路由/下行路由	3失败 	发送账号没有绑定路由	 4 该发送账号的全通道号有一个超过20位
				if(spUserList.size()>0)
				{
					for(Userdata spUser : spUserList)
					{
						if(subno!=null)
						{
							isUsable=subnoManagerBiz.checkPortUsed(spUser.getUserId(), subno);
							if("1".equals(isUsable))
							{
								sp=spUser.getUserId();
								break;
							}
						}
						else
						{
							throw new Exception("子号已使用完！");
						}
					}
				}
				
				if(!"".equals(sp)&&sp!=null)
				{
		    		String[] haoduans=wgMsgConfigBiz.getHaoduan();	
		    		//过滤号段
		    		if(phoneUtil.checkMobile(moblie,haoduans) == 1)
		    		{
		    			//短信内容
		    			Integer bmtType=lfTemplate.getTmpType();
		    			String bmt="短信模板";
		    			if(bmtType==4)
		    			{
		    				bmt="彩信模板";
		    			}
		    			tempMsg=lfTemplate.getTmMsg();
		    			if(lfTemplate.getTmMsg()!=null)
		    			{
		    				
		    				if(lfTemplate.getTmMsg().length()>797)
		    				{
		    					tempMsg=lfTemplate.getTmMsg().substring(0,797)+"...";
		    				}
		    				
		    			}
		    			if(lfFlowRecord!=null&&lfFlowRecord.getRLevel().equals(lfFlowRecord.getRLevelAmount()))
		    			{
		    				//content =this.getLastMsgContent(null, sdf.format(lfTemplate.getAddtime()), bmt, lfTemplate.getTmName(), null);
		    			}else
		    			{
		    				content=this.getMsgTempsContent(lfSysuserCreate.getName(), sdf.format(lfTemplate.getAddtime()), bmt,lfTemplate.getTmName(),tempMsg,batchNumber,disagreeNumber);
		    			}
	//				content="您有短信模板需要审批：提交人："+lfSyssuser1.getUserName()+" "+mt.getSubmitTime()+bmt+"审核任务，主题："+mt.getTitle()+"[内容]："+mt.getMsg()+"[总条数/号码数]："+mt.getIcount()
	//				+"/"+mt.getEffCount()+"。短信审核当天回复有效，通过审核回复Y，不通过审核回复N#+意见";
		    			if(content!=null&&!"".equals(content))
		    			{
		    				lfImMessage.setContent(content);
		    				lfImMessage.setMenuCode(StaticValue.VERIFYREMIND_MENUCODE );
		    				lfImMessage.setTelPhoneNo(moblie);
		    				
	    					//是否计费
	    					boolean flag = SystemGlobals.isDepBilling(corpCode);		
	    					//List<GtPortUsed> portsList = null;
	    					//portsList = smsbiz.getPortByUserId(sp);
	    					int messageCount = smsbiz.countAllOprSmsNumber(content, moblie, haoduans, sp);
	    					if(messageCount>0)
	    					{
	    						LfExamineSms lfExamineSms=new LfExamineSms();
	    						if(nextlfFlowRecord!=null)
	    						{
	    							lfExamineSms.setBatchNumber(batchNumber);
	    							lfExamineSms.setDisagreeNumber(disagreeNumber);
	    							lfExamineSms.setFrId(nextlfFlowRecord.getFrId());
	    							lfExamineSms.setMsgContent(content);
	    							lfExamineSms.setPhone(moblie);
	    							lfExamineSms.setSpNumber(sp+subno);
									lfExamineSms.setSpUser(sp);
									lfExamineSms.setEsType(1);
	    						}
	    						String Rptflag = "0";//是否需要返回状态报告
	    						this.imSendMsgOneByOne(lfExamineSms,lfImMessage,corpCode,sp,subno,balanceBiz,messageCount,currUser.getGuId(),flag,Rptflag);
	    					}
		    			}
		    		}
		    		else
		    		{
		    			EmpExecutionContext.info("["+sdf.format(new Date())+"] 手机号错误！");
		    		}
				}
				else
				{
					throw new Exception("没有可用的发送账号！");
				}
	    	}
	    	else
	    	{
	    		EmpExecutionContext.error("电话号码为空！");
	    	}
				
		}catch (Exception e) {
			EmpExecutionContext.error(e,"模板审批提醒接口调用出现异常！");
		}
	}
	
	/**
	 * 短彩信审批
	 * @param userName
	 * @param date
	 * @param revSate
	 * @param
	 * @return
	 */
	public String getMsgContent(String userName,String date,String revSate,String title,String msg,String count,long effCount,String batchNumber,String disagreeNumber)
	{
		String content="";
		if("彩信群发".equals(revSate))
		{
			content="您有信息需要审批：提交人："+userName+"，预发送条数："+count+"，号码数:"+effCount+"，发送类型："+"彩信"+"，主题："+msg+"，标题："+title
			+"，彩信审批48小时之内回复有效，通过审批回复:"+batchNumber+"，不通过审批回复:"+disagreeNumber+"+意见";
		}
		else
		{
			if(title==null||"".equals(title))
			{
				title="-";
			}
			content="您有信息需要审批：提交人："+userName+"，预发送条数："+count+"，号码数:"+effCount+"，发送类型："+revSate+"，主题："+title+"，内容："+msg
			+"，短信审批48小时之内回复有效，通过审批回复:"+batchNumber+"，不通过审批回复:"+disagreeNumber+"+意见";
		}
		//String content=您有信息需要审批：提交人：**，预计发送条数：**，号码数：**，发送类型：不同内容群发（文件内容），主题：**，内容：详见《文件内容》，批次号：**，审核通过回复Y+批次
		return content;
	}
	
	/**
	 * 模板审批
	 * @param userName
	 * @param date
	 * @param revSate
	 * @param title
	 * @param msg
	 * @return
	 */
	public String getMsgTempsContent(String userName,String date,String revSate,String title,String msg,String batchNumber,String disagreeNumber)
	{
		String content="";
		if(title==null||"".equals(title))
		{
			title="-";
		}
		if("短信模板".equals(revSate))
		{
			content="您有"+revSate+"需要审批：提交人："+userName+"，主题："+title+"，模板内容："+msg+"，短信模板审批48小时之内回复有效，通过审批回复:"+batchNumber+"，不通过审批回复:"+disagreeNumber+"+意见";
		}
		else
		{
			content="您有"+revSate+"需要审批：提交人："+userName+"，主题："+title+"，彩信模板审批48小时之内回复有效，通过审批回复:"+batchNumber+"，不通过审批回复:"+disagreeNumber+"+意见";
		}
		//您有短信模板需要审批：提交人：**，主题：**，模板内容：**，批次号：**，审核通过回复Y+批次号，拒绝通过回复N+批次号。
		return content;
	}
	
	/**
	 *   处理短信彩信信息审批下发
	 * @param lfFlowRecord 审核实时记录对象
	 */
	public void flowReviewRemind(LfFlowRecord lfFlowRecord)
	{
		
		LfSysuser AdminUser = null; 
		StringBuffer buffer = new StringBuffer();
		try{
			
			if(lfFlowRecord.getFId() != null){
				buffer.append(" 审核流程ID ：").append(lfFlowRecord.getFId()).append(";");
			}else{
				buffer.append(" 审核流程ID ：无;");
			}
			SmsBiz smsbiz=new SmsBiz();
			//短信提醒内容
			String content="";
			//创建的任务
			LfMttask mt  = null;
			LfTemplate lfTemplate= null ;
			//根据类型判断是否 1短信发送2 彩信发送  3短信模板4彩信摸板
			if(lfFlowRecord.getInfoType() == 1 || lfFlowRecord.getInfoType() == 2|| lfFlowRecord.getInfoType() == 5)
			{
				//mt=empDao.findObjectByID(LfMttask.class, lfFlowRecord.getMtId());
				mt= new CommonBiz().getLfMttaskbyTaskId(lfFlowRecord.getMtId());
				if(mt == null){
					EmpExecutionContext.error("任务ID为："+lfFlowRecord.getMtId()+"短信提醒下发失败，获取任务失败！");
					return;
				}
			}else if(lfFlowRecord.getInfoType() == 3 || lfFlowRecord.getInfoType() == 4|| lfFlowRecord.getInfoType() == 6){
				lfTemplate = empDao.findObjectByID(LfTemplate.class,lfFlowRecord.getMtId());
			}
	    	LinkedHashMap<String, String> conditionMap =new LinkedHashMap<String, String>();
	    	//任务创建人
	    	LfSysuser lfSysuserCreate=empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getProUserCode());
	    	conditionMap.put("userName", "admin");//admin用户
			conditionMap.put("corpCode", lfSysuserCreate.getCorpCode());//当前登录用户的企业编码
			List<LfSysuser> AdminLfSysuerList = empDao.findListByCondition(LfSysuser.class, conditionMap, null) ;
			
			if(AdminLfSysuerList!=null && AdminLfSysuerList.size()>0)
			{
				//系统admin
				AdminUser = AdminLfSysuerList.get(0);
			}else{
				//当前审批人
				//AdminUser = empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getUserCode());
				String str = buffer.toString() + " 获取该企业的admin失败。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
	    	//获取SPUSER 以及 尾号
			String[] arr = this.getSpuserSubnoByCorpCode(lfSysuserCreate.getCorpCode(),AdminUser, lfFlowRecord);
			String spUser = "";
			String subno = "";
			//记录该 短信提醒的信息
			
			if(arr != null && "success".equals(arr[0])){
				subno = arr[1];
				spUser = arr[2];
				buffer.append("子号 ：").append(subno).append(";sp账号 ").append(spUser).append(";");
			}else{
				if(arr != null && "fail".equals(arr[0])){
					EmpExecutionContext.error("审批提醒获取发送账号失败！该企业没有可用sp账号，或者没有绑定上行url。CorpCode:"+lfSysuserCreate.getCorpCode()+"。userId:"+AdminUser.getUserId());
					String str = buffer.toString() + " 获取发送账号失败。";
					this.dailyFlowRecord(AdminUser,str, "fail");
				}else if(arr != null && "nosubno".equals(arr[0])){
					EmpExecutionContext.error("审批提醒没有可用的尾号！CorpCode:"+lfSysuserCreate.getCorpCode()+"。userId:"+AdminUser.getUserId());
					String str = buffer.toString() + " 获取没有可用的尾号。";
					this.dailyFlowRecord(AdminUser,str, "fail");
				}
				return;
			}
			//批次号(同意)
			String batchNumber= GlobalVariableBiz.getInstance().getNewNodeCode();
			//批次号(不同意)
			String disagreeNumber= GlobalVariableBiz.getInstance().getNewNodeCode();
			if(batchNumber == null || "".equals(batchNumber)){
				EmpExecutionContext.error("审批提醒获取同意指令失败！");
				String str = buffer.toString() + " 获取同意指令失败。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
			if(disagreeNumber == null || "".equals(disagreeNumber)){
				EmpExecutionContext.error("审批提醒获取不同意指令失败！");
				String str = buffer.toString() + " 获取不同意指令失败。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
			buffer.append(" 同意指令：").append(batchNumber).append(";不同意指令: ").append(disagreeNumber).append(";");
	    	//下一级审批记录
	    	List<LfFlowRecord> nextFlowRecordList = null;
	    	//存放   USERID  KEY   FRID VALUE
	    	LinkedHashMap<Long,Long> frIdMap = new LinkedHashMap<Long, Long>();
	    	//存放   MOBLIE  KEY   FRID VALUE
	    	LinkedHashMap<String,Long> userIdMap = new LinkedHashMap<String, Long>();
	    	//记录下一批审批人的手机号码
	    	String[] phoneArr = null;
	    	
    		int rlevel = lfFlowRecord.getRLevel()+1;
    		//如果是逐级审核
    		if(lfFlowRecord.getRType() != null && lfFlowRecord.getRType() == 5 )
    		{
    			rlevel = 1;
    			boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(lfFlowRecord.getPreRv().intValue(), lfFlowRecord.getProUserCode());
    			if(isLastLevel){
    				rlevel = 2;
    			}
    		}
    		buffer.append("下一级审批级别：").append(rlevel).append(";");
    		if(lfFlowRecord.getRType() != null){
    			buffer.append("审批类型：").append(lfFlowRecord.getRType()).append(";");
    		}else{
    			buffer.append("审批类型：未知;");
    		}
    		//下一级审批流程
    		conditionMap.clear();
    		
    		conditionMap.put("mtId",String.valueOf(lfFlowRecord.getMtId()));
    		int mstype=0;
    		if(mt!=null){
    			mstype=mt.getMsType();
    		}
    		if(mstype==6){//6是网讯 
    			conditionMap.put("infoType", "5");//网讯发送的type是5
    		}else{
    			conditionMap.put("infoType", String.valueOf(lfFlowRecord.getInfoType()));
    		}
	    	
	    	conditionMap.put("isComplete","2");
	    	conditionMap.put("RLevel", String.valueOf(rlevel));
    		nextFlowRecordList = empDao.findListByCondition(LfFlowRecord.class, conditionMap, null);
    		StringBuffer idStr = new StringBuffer();
    		if(nextFlowRecordList != null && nextFlowRecordList.size()>0){
    			for(LfFlowRecord record:nextFlowRecordList){
    				idStr.append(record.getUserCode()).append(",");
    				frIdMap.put(record.getUserCode(), record.getFrId());
    			}
    			String str = idStr.toString().substring(0, idStr.toString().length()-1);
    			conditionMap.clear();
				conditionMap.put("userId&in", str);
				conditionMap.put("corpCode", lfSysuserCreate.getCorpCode());
				//List<LfSysuser> sysuserList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
				List<LfSysuser> sysuserList = empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);;
				if(sysuserList != null && sysuserList.size()>0){
					phoneArr = new String[sysuserList.size()];
					for(int i=0;i<sysuserList.size();i++){
						LfSysuser sysuser = sysuserList.get(i);
						phoneArr[i] = sysuser.getMobile();
						//此处判断下是否需要有审核提醒【根据操作员中是否勾选审核提醒处理】 有四位
						String cf=sysuser.getControlFlag();
						if(cf!=null&&cf.length()>1){
							String bit=cf.substring(0, 1);//判断是否勾选，如果没勾选则不发送信息
							if("0".equals(bit)){
								continue;
							}
						}
						Long frId = frIdMap.get(sysuser.getUserId());
						userIdMap.put(sysuser.getMobile(), frId);
					}
				}
    		}else{
    			//没有下一级审核就不显示
//    			EmpExecutionContext.error("审批提醒没有获取到下一级审批信息记录！");
//    			String str = buffer.toString() + " 没有获取到下一级审批信息记录。";
//				this.dailyFlowRecord(AdminUser,str, "fail");
    			return;
    		}
    		
	    	if(phoneArr == null || phoneArr.length == 0){
				EmpExecutionContext.error("审批提醒没有可用的手机号码！");
				String str = buffer.toString() + " 没有可用的手机号码。";
				this.dailyFlowRecord(AdminUser,str, "fail");
	    		return;
	    	}
	    	
			if(lfFlowRecord.getInfoType() == 1 || lfFlowRecord.getInfoType() == 2|| lfFlowRecord.getInfoType() == 5)
			{
				content = this.getRemindContent(mt, batchNumber, disagreeNumber, lfSysuserCreate);
			}else if(lfFlowRecord.getInfoType() == 3 || lfFlowRecord.getInfoType() == 4|| lfFlowRecord.getInfoType() == 6){
				content = this.getRemindTmpContent(lfTemplate, batchNumber, disagreeNumber, lfSysuserCreate);
			}
			if(content == null || "".equals(content)){
				EmpExecutionContext.error("审批提醒内容为空！");
				String str = buffer.toString() + " 审批提醒内容为空。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
			if(!"".equals(spUser) &&  spUser != null)
			{
		    	WgMsgConfigBiz wgMsgConfigBiz=new WgMsgConfigBiz();
				String[] haoduans= wgMsgConfigBiz.getHaoduan();	
				//拿到当前系统配置的号段
				List<GtPortUsed> portsList = smsbiz.getPortByUserId(spUser);
				//计算条数
				StringBuffer sendBuffer = new StringBuffer("");
				int messageCount = 0;
				//是否计费
				boolean flag = SystemGlobals.isDepBilling(lfSysuserCreate.getCorpCode());
				LfExamineSms lfExamineSms = null;
				List<LfExamineSms> examineSmsList = new ArrayList<LfExamineSms>();
				
				//Integer arrCount = (phoneArr.length/100==0?0: phoneArr.length/100) + 1;
				Integer num = 0;
				Map<Integer, String> map = new SubnoManagerBiz().getPortUserBySpuser(portsList, spUser, subno);
				try{
					for(int a=0;a<phoneArr.length;a++){
						String temp = phoneArr[a];
						if(temp != null && !"".equals(temp) && phoneUtil.checkMobile(temp,haoduans) == 1){
							temp = temp.trim();
							Long frId = userIdMap.get(temp);
							if(frId == null){
								continue;
							}
							sendBuffer.append(temp).append(",");
							int count = smsbiz.countAllOprSmsNumber(content, temp, haoduans, spUser);
							messageCount = messageCount + count;
							lfExamineSms = new LfExamineSms();
							lfExamineSms.setBatchNumber(batchNumber);
							lfExamineSms.setDisagreeNumber(disagreeNumber);
							lfExamineSms.setFrId(frId);
							lfExamineSms.setMsgContent(content);
							lfExamineSms.setPhone(temp);
							Integer spisuncm = 0;
							if("+".equals(temp.substring(0, 1)) || "00".equals(temp.substring(0, 2)))
							{
								spisuncm = 5;
							}
							else
							{
								spisuncm = wgMsgConfigBiz.getPhoneSpisuncm(temp);
							}
							String spNumber = map.get(spisuncm);
							if(spNumber == null || "".equals(spNumber)){
								continue;
							}
							lfExamineSms.setSpNumber(spNumber);
	
							lfExamineSms.setSpUser(spUser);
							lfExamineSms.setEsType(1);
							examineSmsList.add(lfExamineSms);
							num = num + 1;
						}
						if(num >= 100 && num%100 == 0 && messageCount >0 && examineSmsList.size()>0){
							this.sendMsgByMoblieStr(examineSmsList, spUser, subno, messageCount, 
									flag,"0", StaticValue.VERIFYREMIND_MENUCODE, sendBuffer.toString(), content, AdminUser);
							sendBuffer = new StringBuffer();
							examineSmsList = new ArrayList<LfExamineSms>();
							num = 0;
							messageCount = 0;
						}
					}
					if(num < 100 && messageCount >0 && examineSmsList.size()>0){
						this.sendMsgByMoblieStr(examineSmsList, spUser, subno, messageCount, 
									flag,"0", StaticValue.VERIFYREMIND_MENUCODE, sendBuffer.toString(), content, AdminUser);
					}
					//其他逻辑不变，如果增加了最后一个不管是拒绝，或者审批通过最后一级，都要给创建人发送消息
					//在全部审核生效的情况下      标识的是  该级下是否是最后一个审批人
					

					
					
					buffer.append(" 下发条数：").append(phoneArr.length).append(";");
					if(lfFlowRecord.getFId() != null&&lfFlowRecord.getRType() != null){
						this.dailyFlowRecord(AdminUser,buffer.toString(), "success");
					}
				}catch (Exception e) {
					EmpExecutionContext.error(e, "短信提醒下发记录保存失败！");
					String str = buffer.toString() + " 短信提醒下发记录保存失败。";
					this.dailyFlowRecord(AdminUser,str, "fail");
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"短信提醒下发失败！");
		}
	}
	
	/**
	 *   处理邮件发送审批下发 (网讯单独做处理，模板不是存在同一个位置)
	 * @param lfFlowRecord 审核实时记录对象
	 */
	public void sendMail(LfFlowRecord lfFlowRecord){

		LfSysuser AdminUser = null; 
		LfSysuser sendUser = null; 
		StringBuffer buffer = new StringBuffer();
		String msg="";
		try{
			
			if(lfFlowRecord!=null){
				msg=getType(lfFlowRecord.getInfoType());
			}
			if(lfFlowRecord.getFId() != null){
				buffer.append(" 邮件发送中审核流程ID ：").append(lfFlowRecord.getFId()).append(";");
			}else{
				buffer.append(" 邮件发送中审核流程ID ：无;");
			}
			//短信提醒内容
			String content="";
			//创建的任务
			LfMttask mt  = null;
			LfTemplate lfTemplate= null ;
			//根据类型判断是否 1短信发送2 彩信发送  3短信模板4彩信摸板
			if(lfFlowRecord.getInfoType() == 1 || lfFlowRecord.getInfoType() == 2|| lfFlowRecord.getInfoType() == 5)
			{
				mt= new CommonBiz().getLfMttaskbyTaskId(lfFlowRecord.getMtId());
				if(mt == null){
					EmpExecutionContext.error("任务ID为："+lfFlowRecord.getMtId()+"邮件提醒下发失败，获取任务失败！");
					return;
				}
			}else if(lfFlowRecord.getInfoType() == 3 || lfFlowRecord.getInfoType() == 4|| lfFlowRecord.getInfoType() == 6){
				lfTemplate = empDao.findObjectByID(LfTemplate.class,lfFlowRecord.getMtId());
			}
	    	LinkedHashMap<String, String> conditionMap =new LinkedHashMap<String, String>();
	    	//任务创建人
	    	sendUser=empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getProUserCode());
			conditionMap.put("userName", "admin");//admin用户
			conditionMap.put("corpCode", sendUser.getCorpCode());//当前登录用户的企业编码
			List<LfSysuser> AdminLfSysuerList = empDao.findListByCondition(LfSysuser.class, conditionMap, null) ;
			
			if(AdminLfSysuerList!=null && AdminLfSysuerList.size()>0)
			{
				//系统admin
				AdminUser = AdminLfSysuerList.get(0);
			}else{
				//当前审批人
				//AdminUser = empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getUserCode());
				String str = buffer.toString() + " 获取该企业的admin失败。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
			if(sendUser!=null )
			{
				if(sendUser.getEMail()==null||"".equals(sendUser.getEMail())){
					//发送人的邮件为空 则返回
					String str = buffer.toString() + " 发送人的邮件为空。";
					this.dailyFlowRecord(AdminUser,str, "fail");
					return;
				}
			}else{
				//当前审批人
				//AdminUser = empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getUserCode());
				String str = buffer.toString() + " 获得发送人失败。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
	    	//下一级审批记录
	    	List<LfFlowRecord> nextFlowRecordList = null;

	    	//记录下一批审批人的手机号码
	    	String[] phoneArr = null;
	    	
    		int rlevel = lfFlowRecord.getRLevel()+1;
    		//如果是逐级审核
    		if(lfFlowRecord.getRType() != null && lfFlowRecord.getRType() == 5 )
    		{
    			rlevel = 1;
    			boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(lfFlowRecord.getPreRv().intValue(), lfFlowRecord.getProUserCode());
    			if(isLastLevel){
    				rlevel = 2;
    			}
    		}
    		buffer.append("下一级审批级别：").append(rlevel).append(";");
    		if(lfFlowRecord.getRType() != null){
    			buffer.append("审批类型：").append(lfFlowRecord.getRType()).append(";");
    		}else{
    			buffer.append("审批类型：未知;");
    		}
    		//下一级审批流程
    		conditionMap.clear();
    		
    		conditionMap.put("mtId",String.valueOf(lfFlowRecord.getMtId()));
    		int mstype=0;
    		if(mt!=null){
    			mstype=mt.getMsType();
    		}
    		//其中网讯的模板审核走的是自己单独写的，短信发送走的是公共的这个方法
    		if(mstype==6){//6是网讯 
    			conditionMap.put("infoType", "5");//网讯发送的type是5
    		}else{
    			conditionMap.put("infoType", String.valueOf(lfFlowRecord.getInfoType()));
    		}

	    	
	    	conditionMap.put("isComplete","2");
	    	conditionMap.put("RLevel", String.valueOf(rlevel));
    		nextFlowRecordList = empDao.findListByCondition(LfFlowRecord.class, conditionMap, null);
    		//用户code
    		StringBuffer idStr = new StringBuffer();
    		//email
    		StringBuffer emailStr = new StringBuffer();
    		if(nextFlowRecordList != null && nextFlowRecordList.size()>0){
    			for(LfFlowRecord record:nextFlowRecordList){
    				idStr.append(record.getUserCode()).append(",");
//    				frIdMap.put(record.getUserCode(), record.getFrId());
    			}
    			String str = idStr.toString().substring(0, idStr.toString().length()-1);
    			conditionMap.clear();
				conditionMap.put("userId&in", str);
				conditionMap.put("corpCode", sendUser.getCorpCode());
				List<LfSysuser> sysuserList = empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);;
				if(sysuserList != null && sysuserList.size()>0){
					phoneArr = new String[sysuserList.size()];
					for(int i=0;i<sysuserList.size();i++){
						LfSysuser sysuser = sysuserList.get(i);
						phoneArr[i] = sysuser.getMobile();
						//此处判断下是否需要有审核提醒【根据操作员中是否勾选审核提醒处理】 有四位
						String cf=sysuser.getControlFlag();
						if(cf!=null&&cf.length()>1){
							String bit=cf.substring(1, 2);//判断是否勾选，如果没勾选则不发送信息
							if("0".equals(bit)){
								continue;
							}
						}
						emailStr.append(sysuser.getEMail()).append(",");
//						userIdMap.put(sysuser.getEMail(), frId);
					}
				}
    		}else{
    			return;
    		}
			if(lfFlowRecord.getInfoType() == 1 || lfFlowRecord.getInfoType() == 2|| lfFlowRecord.getInfoType() == 5)
			{
				content = this.getEmailContent(mt, sendUser);
			}else if(lfFlowRecord.getInfoType() == 3 || lfFlowRecord.getInfoType() == 4|| lfFlowRecord.getInfoType() == 6){
				content = this.getRemindEmailContent(lfTemplate, sendUser);
			}
			if(content == null || "".equals(content)){
				EmpExecutionContext.error("邮件审批提醒内容为空！");
				String str = buffer.toString() + " 邮件审批提醒内容为空。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
			content="您有信息需要审批，请及时处理，谢谢 "+"</br></br>"+content;
    		
    		if(emailStr.toString()!=null&&!"".equals(emailStr.toString())){
    			//给下级发送的邮箱地址列表
        		String email = emailStr.toString().substring(0, emailStr.toString().length()-1);
    			LinkedHashMap<String, String> condition  = new LinkedHashMap<String, String>();
    			condition.put("corpCode", sendUser.getCorpCode());
    			BaseBiz	baseBiz	= new BaseBiz();
				List<LfCorpConf> lfCorpConfList  =baseBiz.getByCondition(LfCorpConf.class,condition,null);
				String protocol="";
				String host="";
				String port="";
				String username="";
				String password=null;
				if(lfCorpConfList != null){
					for(int i=0;i<lfCorpConfList.size();i++){
						LfCorpConf conf=lfCorpConfList.get(i);{
							if("email.protocol".equals(conf.getParamKey())){
								protocol=conf.getParamValue();
							}
							if("email.host".equals(conf.getParamKey())){
								host=conf.getParamValue();
							}
							if("email.port".equals(conf.getParamKey())){
								port=conf.getParamValue();
							}							
							if("email.username".equals(conf.getParamKey())){
								username=conf.getParamValue();
							}							
							if("email.password".equals(conf.getParamKey())){
								password=conf.getParamValue();
							}
						}
					}
				}else{
						String str =   "模块参数配置中相应的邮件配置不存在！";
						EmpExecutionContext.error(str);
						this.dailyFlowRecord(AdminUser,str, "fail");
			    		return;
					}
        		MailInfo mi = new MailInfo();
        		Aes aes=new Aes();
        		mi.setFrom(username);
        		mi.setUsername(username);
        		//解密
        		mi.setPassword(aes.deString(password));
        		mi.setHost(host);
        		mi.setPort(port);
        		mi.setProtocol(protocol);
        		mi.setTo(email);
        		mi.setSubject("【审批提醒】");
        		mi.setName("");
        		mi.setPriority("1");
        		mi.setContent(content);
        		
        		SendMail sm = new SendMail(mi);
				
        		try{
        			sm.sendMultipleEmail();
        		}catch (Exception e) {
        			String str =  " 网络原因，邮箱提醒失败！";
        			String msessage=e.getMessage();
        			if(msessage!=null&&msessage.indexOf("authentication failed")>-1){
        				str="地址无效，邮箱提醒失败！";
        			}
        			EmpExecutionContext.error(str);
    				this.dailyFlowRecord(AdminUser,str, "fail");	
        		}

    		}else{
				String str =   "模块类型："+msg+"，类型：邮件提醒，发送人： "+sendUser.getUserName()+"，没有可用的E-mail。";
				EmpExecutionContext.error(str);
				this.dailyFlowRecord(AdminUser,str, "fail");
	    		return;
	    	}
	    	

		}catch (Exception e) {
			EmpExecutionContext.error(e,"邮件提醒下发失败！");
			
		}
	
		
	}
	
	/**
	 *   处理邮件发送审批下发 (网讯单独做处理，模板不是存在同一个位置)
	 * @param lfFlowRecord 审核实时记录对象
	 * @param number 不通过的级别，传入当前级别，如果最后一级（都通过），则传入空字符串
	 * 当拒绝时候，不产生批次号
	 */
	public void sendMailForSender(LfFlowRecord lfFlowRecord,String number){

		LfSysuser AdminUser = null; 
		LfSysuser sendUser = null; 
		StringBuffer buffer = new StringBuffer();
		String msg="";
		LfWXBASEINFO lfWXBasinfo=null;
		try{
			
			if(lfFlowRecord!=null){
				msg=getType(lfFlowRecord.getInfoType());
			}
			if(lfFlowRecord.getFId() != null){
				buffer.append(" 邮件发送中审核流程ID ：").append(lfFlowRecord.getFId()).append(";");
			}else{
				buffer.append(" 邮件发送中审核流程ID ：无;");
			}
			//短信提醒内容
			String content="";
			//创建的任务
			LfMttask mt  = null;
			LfTemplate lfTemplate= null ;
			//根据类型判断是否 1短信发送2 彩信发送  3短信模板4彩信摸板
			if(lfFlowRecord.getInfoType() == 1 || lfFlowRecord.getInfoType() == 2|| lfFlowRecord.getInfoType() == 5)
			{
				mt= new CommonBiz().getLfMttaskbyTaskId(lfFlowRecord.getMtId());
				if(mt == null){
					EmpExecutionContext.error("任务ID为："+lfFlowRecord.getMtId()+"邮件提醒下发失败，获取任务失败！");
					return;
				}
			}else if(lfFlowRecord.getInfoType() == 3 || lfFlowRecord.getInfoType() == 4){
				lfTemplate = empDao.findObjectByID(LfTemplate.class,lfFlowRecord.getMtId());
			}else if(lfFlowRecord.getInfoType() == 6){
				 lfWXBasinfo = empDao.findObjectByID(LfWXBASEINFO.class, lfFlowRecord.getMtId());
			}
			
	    	LinkedHashMap<String, String> conditionMap =new LinkedHashMap<String, String>();
	    	//任务创建人
	    	 sendUser=empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getProUserCode());
			conditionMap.put("userName", "admin");//admin用户
			conditionMap.put("corpCode", sendUser.getCorpCode());//当前登录用户的企业编码
			List<LfSysuser> AdminLfSysuerList = empDao.findListByCondition(LfSysuser.class, conditionMap, null) ;
			
			if(AdminLfSysuerList!=null && AdminLfSysuerList.size()>0)
			{
				//系统admin
				AdminUser = AdminLfSysuerList.get(0);
			}else{
				//当前审批人
				//AdminUser = empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getUserCode());
				String str = buffer.toString() + " 获取该企业的admin失败。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
			if(sendUser!=null )
			{
				if(sendUser.getEMail()==null||"".equals(sendUser.getEMail())){
					//发送人的邮件为空 则返回
					String str = buffer.toString() + " 发送人的邮件为空。";
					this.dailyFlowRecord(AdminUser,str, "fail");
					return;
				}
			}else{
				//当前审批人
				//AdminUser = empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getUserCode());
				String str = buffer.toString() + " 获得发送人失败。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
			String cf=sendUser.getControlFlag();//给创建者发信息
			//此处判断下是否需要有审核提醒【根据操作员中是否勾选审核提醒处理】 有四位
			if(cf!=null&&cf.length()>1){
				String bit=cf.substring(1, 2);//判断是否勾选，如果没勾选则不发送信息
				if("0".equals(bit)){
					return;
				}
			}
	    	//下一级审批记录
	    	List<LfFlowRecord> nextFlowRecordList = null;

	    	//记录下一批审批人的手机号码
	    	String[] phoneArr = null;
	    	
    		int rlevel = lfFlowRecord.getRLevel()+1;
    		//如果是逐级审核
    		if(lfFlowRecord.getRType() != null && lfFlowRecord.getRType() == 5 )
    		{
    			rlevel = 1;
    			boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(lfFlowRecord.getPreRv().intValue(), lfFlowRecord.getProUserCode());
    			if(isLastLevel){
    				rlevel = 2;
    			}
    		}
    		buffer.append("下一级审批级别：").append(rlevel).append(";");
    		if(lfFlowRecord.getRType() != null){
    			buffer.append("审批类型：").append(lfFlowRecord.getRType()).append(";");
    		}else{
    			buffer.append("审批类型：未知;");
    		}
    		//下一级审批流程
    		conditionMap.clear();
    		
    		conditionMap.put("mtId",String.valueOf(lfFlowRecord.getMtId()));
    		int mstype=0;
    		if(mt!=null){
    			mstype=mt.getMsType();
    		}
    		//其中网讯的模板审核走的是自己单独写的，短信发送走的是公共的这个方法
    		if(mstype==6){//6是网讯 
    			conditionMap.put("infoType", "5");//网讯发送的type是5
    		}else{
    			conditionMap.put("infoType", String.valueOf(lfFlowRecord.getInfoType()));
    		}

	    	
	    	conditionMap.put("isComplete","2");
	    	conditionMap.put("RLevel", String.valueOf(rlevel));
    		nextFlowRecordList = empDao.findListByCondition(LfFlowRecord.class, conditionMap, null);
    		//用户code

			if(lfFlowRecord.getInfoType() == 1 || lfFlowRecord.getInfoType() == 2|| lfFlowRecord.getInfoType() == 5)
			{
				content = this.getMailContent(mt,sendUser,number,lfFlowRecord);
			}else if(lfFlowRecord.getInfoType() == 3 || lfFlowRecord.getInfoType() == 4){
				content = this.getTempMailContent(lfTemplate, sendUser,number,lfFlowRecord);
			}else if(lfFlowRecord.getInfoType() == 6){
				if(lfWXBasinfo!=null){
					content = this.getWXMailContent(lfWXBasinfo, "", "", sendUser,number,lfFlowRecord);
				}
			}
			
			if(content == null || "".equals(content)){
				EmpExecutionContext.error("邮件审批提醒内容为空！");
				String str = buffer.toString() + " 邮件审批提醒内容为空。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
			content ="您的信息已经完成审批，请及时确认，谢谢</br></br>"+content;
    		String email = sendUser.getEMail();
    		if(email!=null&&!"".equals(email)){
    			//给下级发送的邮箱地址列表
    			LinkedHashMap<String, String> condition  = new LinkedHashMap<String, String>();
				conditionMap.put("corpCode", sendUser.getCorpCode());
    			BaseBiz	baseBiz	= new BaseBiz();
				List<LfCorpConf> lfCorpConfList  =baseBiz.getByCondition(LfCorpConf.class,condition,null);
				String protocol="";
				String host="";
				String port="";
				String username="";
				String password=null;
				if(lfCorpConfList != null){
					for(int i=0;i<lfCorpConfList.size();i++){
						LfCorpConf conf=lfCorpConfList.get(i);{
							if("email.protocol".equals(conf.getParamKey())){
								protocol=conf.getParamValue();
							}
							if("email.host".equals(conf.getParamKey())){
								host=conf.getParamValue();
							}
							if("email.port".equals(conf.getParamKey())){
								port=conf.getParamValue();
							}							
							if("email.username".equals(conf.getParamKey())){
								username=conf.getParamValue();
							}							
							if("email.password".equals(conf.getParamKey())){
								password=conf.getParamValue();
							}
						}
					}
				}else{
						String str =   "模块参数配置中相应的邮件配置不存在！";
						EmpExecutionContext.error(str);
						this.dailyFlowRecord(AdminUser,str, "fail");
			    		return;
					}
        		MailInfo mi = new MailInfo();
        		Aes aes=new Aes();
        		mi.setFrom(username);
        		mi.setUsername(username);
        		//解密
        		mi.setPassword(aes.deString(password));
        		//取的是邮件后面的部分
        		mi.setHost(host);
        		mi.setPort(port);
        		mi.setProtocol(protocol);
        		mi.setTo(email);
        		mi.setSubject("【审批完成提醒】");
        		mi.setName("");
        		mi.setPriority("1");
        		mi.setContent(content);
        		
        		SendMail sm = new SendMail(mi);
				
        		try{
        			sm.sendMultipleEmail();
        		}catch (Exception e) {
        			String str =  " 网络原因，邮箱提醒失败！";
        			String msessage=e.getMessage();
        			if(msessage!=null&&msessage.indexOf("authentication failed")>-1){
        				str="地址无效，邮箱提醒失败！";
        			}
        			EmpExecutionContext.error(str);
    				this.dailyFlowRecord(AdminUser,str, "fail");	
        		}

    		}else{
				String str =   "模块类型："+msg+"，类型：邮件提醒，发送人： "+sendUser.getUserName()+"，没有可用的E-mail。";
				EmpExecutionContext.error(str);
				this.dailyFlowRecord(AdminUser,str, "fail");
	    		return;
	    	}
	    	

		}catch (Exception e) {
			EmpExecutionContext.error(e,"邮件提醒下发失败！");
			
		}
	
		
	}
	
	/**
	 * 审核的类型
	 * @description    
	 * @param infoType
	 * @return       			 
	 * @datetime 2016-3-26 上午11:42:39
	 */
	private String getType(int infoType){	
		String title="";
		if(infoType==1){
			title="短信发送";
		}else if(infoType==2){
			title="彩信发送";
		}else if(infoType==3){
			title="短信模板";
		}else if(infoType==4){
			title="彩信模板";
		}else if(infoType==5){
			title="网讯发送";
		}else if(infoType==6){
			title="网讯模板";
		}
		return title;
	}
	
	
	/**
	 *   处理短信彩信信息审批下发给创建人
	 * @param lfFlowRecord 审核实时记录对象
	 * @param number 不通过的级别，传入当前级别，如果最后一级（都通过），则传入空字符串
	 * 当拒绝时候，不产生批次号
	 */
	public void ReviewRemindForSender(LfFlowRecord lfFlowRecord,String number)
	{
		LfSysuser lfSysuser= null;
    	try
		{
			 lfSysuser=empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getUserCode());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询被审批人失败！");
		}
		LfSysuser AdminUser = null; 
		StringBuffer buffer = new StringBuffer();
		try{
			
			if(lfFlowRecord.getFId() != null){
				buffer.append(" 审核流程ID ：").append(lfFlowRecord.getFId()).append(";");
			}else{
				buffer.append(" 审核流程ID ：无;");
			}
			SmsBiz smsbiz=new SmsBiz();
			//短信提醒内容
			String content="";
			//创建的任务
			LfMttask mt  = null;
			LfTemplate lfTemplate= null ;
			LfWXBASEINFO lfWXBasinfo=null;
			//根据类型判断是否 1短信发送2 彩信发送  3短信模板4彩信摸板
			if(lfFlowRecord.getInfoType() == 1 || lfFlowRecord.getInfoType() == 2||lfFlowRecord.getInfoType() == 5)
			{
				mt= new CommonBiz().getLfMttaskbyTaskId(lfFlowRecord.getMtId());
				if(mt == null){
					EmpExecutionContext.error("任务ID为："+lfFlowRecord.getMtId()+"短信提醒下发失败，获取任务失败！");
					return;
				}
			}else if(lfFlowRecord.getInfoType() == 3 || lfFlowRecord.getInfoType() == 4){
				lfTemplate = empDao.findObjectByID(LfTemplate.class,lfFlowRecord.getMtId());
			}else if(lfFlowRecord.getInfoType() == 6){
				 lfWXBasinfo = empDao.findObjectByID(LfWXBASEINFO.class, lfFlowRecord.getMtId());
			}
	    	//任务创建人
	    	LfSysuser lfSysuserCreate=empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getProUserCode());
			String cf=lfSysuserCreate.getControlFlag();//给创建者发信息
			//此处判断下是否需要有审核提醒【根据操作员中是否勾选审核提醒处理】 有四位
			if(cf!=null&&cf.length()>1){
				String bit=cf.substring(0, 1);//判断是否勾选，如果没勾选则不发送信息
				if("0".equals(bit)){
					return;
				}
			}
			//AdminUser=lfSysuserCreate;
	    	//获取SPUSER 以及 尾号
			String[] arr = this.getSpuserSubnoByCorpCode(lfSysuserCreate.getCorpCode(),lfSysuserCreate, lfFlowRecord);
			String spUser="";
			String subno = "";
			//记录该 短信提醒的信息
			
			//*************扣admin所在机构的费用***********
			LinkedHashMap<String, String> conditionMap =new LinkedHashMap<String, String>();
	    	conditionMap.put("userName", "admin");//admin用户
			conditionMap.put("corpCode", lfSysuserCreate.getCorpCode());//当前登录用户的企业编码
			List<LfSysuser> AdminLfSysuerList = empDao.findListByCondition(LfSysuser.class, conditionMap, null) ;
			
			if(AdminLfSysuerList!=null && AdminLfSysuerList.size()>0)
			{
				//系统admin
				AdminUser = AdminLfSysuerList.get(0);
			}else{
				//当前审批人
				//AdminUser = empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getUserCode());
				String str = buffer.toString() + " 获取该企业的admin失败。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
			
			if(arr != null && "success".equals(arr[0])){
				subno = arr[1];
				spUser = arr[2];
				buffer.append("子号 ：").append(subno).append(";sp账号 ").append(spUser).append(";");
			}else{
				if(arr != null && "fail".equals(arr[0])){
					EmpExecutionContext.error("审批提醒获取发送账号失败！");
					String str = buffer.toString() + " 获取发送账号失败。";
					this.dailyFlowRecord(AdminUser,str, "fail");
				}else if(arr != null && "nosubno".equals(arr[0])){
					EmpExecutionContext.error("审批提醒没有可用的尾号！");
					String str = buffer.toString() + " 获取没有可用的尾号。";
					this.dailyFlowRecord(AdminUser,str, "fail");
				}
				return;
			}
			//批次号(同意)  批次号(不同意) 同意取得的是taskid
			String batchNumber=lfFlowRecord.getMtId()+"";
			//批次号(不同意)
			String disagreeNumber=lfFlowRecord.getMtId()+"";
			if(lfFlowRecord.getInfoType() == 1 || lfFlowRecord.getInfoType() == 2|| lfFlowRecord.getInfoType() == 5)
			{
				content = this.getContent(mt, batchNumber, disagreeNumber, lfSysuser,number,lfFlowRecord);
			}else if(lfFlowRecord.getInfoType() == 3 || lfFlowRecord.getInfoType() == 4){
				content = this.getTempContent(lfTemplate, batchNumber, disagreeNumber, lfSysuser,number,lfFlowRecord);
			}else if(lfFlowRecord.getInfoType() == 6){
				if(lfWXBasinfo!=null){
					content = this.getWXContent(lfWXBasinfo, batchNumber, disagreeNumber, lfSysuser,number,lfFlowRecord);
				}
			}
			if(content == null || "".equals(content)){
				EmpExecutionContext.error("审批提醒内容为空！");
				String str = buffer.toString() + " 审批提醒内容为空。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				return;
			}
			if(!"".equals(spUser) &&  spUser != null)
			{
		    	WgMsgConfigBiz wgMsgConfigBiz=new WgMsgConfigBiz();
				String[] haoduans= wgMsgConfigBiz.getHaoduan();	
				//拿到当前系统配置的号段
				List<GtPortUsed> portsList = smsbiz.getPortByUserId(spUser);
				//计算条数
				StringBuffer sendBuffer = new StringBuffer("");
				int messageCount = 0;
				//是否计费
				boolean flag = SystemGlobals.isDepBilling(lfSysuserCreate.getCorpCode());
				LfExamineSms lfExamineSms = null;
				List<LfExamineSms> examineSmsList = new ArrayList<LfExamineSms>();
				

				Map<Integer, String> map = new SubnoManagerBiz().getPortUserBySpuser(portsList, spUser, subno);
				try{
						String temp = lfSysuserCreate.getMobile();
						if(temp != null && !"".equals(temp) && phoneUtil.checkMobile(temp,haoduans) == 1){
							temp = temp.trim();
							sendBuffer.append(temp).append(",");
							int count = smsbiz.countAllOprSmsNumber(content, temp, haoduans, spUser);
							messageCount = messageCount + count;
							lfExamineSms = new LfExamineSms();
							lfExamineSms.setBatchNumber(batchNumber);
							lfExamineSms.setDisagreeNumber(disagreeNumber);
							lfExamineSms.setFrId(lfFlowRecord.getFrId());
							lfExamineSms.setMsgContent(content);
							lfExamineSms.setPhone(temp);
							Integer spisuncm = 0;
							if("+".equals(temp.substring(0, 1)) || "00".equals(temp.substring(0, 2)))
							{
								spisuncm = 5;
							}
							else
							{
								spisuncm = wgMsgConfigBiz.getPhoneSpisuncm(temp);
							}
							String spNumber = map.get(spisuncm);
							lfExamineSms.setSpNumber(spNumber);
							lfExamineSms.setSpUser(spUser);
							lfExamineSms.setEsType(1);
							examineSmsList.add(lfExamineSms);
						}
							String res = this.sendMsgByMoblieStr(examineSmsList, spUser, subno, messageCount, 
									flag,"0", StaticValue.VERIFYREMIND_MENUCODE, sendBuffer.toString(), content, AdminUser);
						
							//其他逻辑不变，如果增加了最后一个不管是拒绝，或者审批通过最后一级，都要给创建人发送消息
							//在全部审核生效的情况下      标识的是  该级下是否是最后一个审批人
							//发送网关成功
							if("1".equals(res))
							{
								this.dailyFlowRecord(AdminUser,buffer.toString(), "success");
							}
				}catch (Exception e) {
					EmpExecutionContext.error(e, "短信提醒下发记录保存失败！");
					String str = buffer.toString() + " 短信提醒下发记录保存失败。";
					this.dailyFlowRecord(AdminUser,str, "fail");
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"短信提醒下发失败！");
		}
	}

	/**
	 * 保存审批提醒的记录
	 * @param
	 * @return
	 */
	private void dailyFlowRecord(LfSysuser sysuser,String buffer,String type){
		try{
			if("fail".equals(type)){
			 	new SuperOpLog().logFailureString(sysuser.getUserName(),"审批提醒","其他",buffer,null,sysuser.getCorpCode());
			}else if("success".equals(type)){
				new SuperOpLog().logSuccessString(sysuser.getUserName(),"审批提醒","其他",buffer,sysuser.getCorpCode());
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "审批提醒日志保存失败！");
		}
	}
	
	
	/**
	 *  通过企业编码查询出所对应的SPUSER以及尾号
	 * @param corpCode 企业编码
	 * @param
	 * @return
	 */
	public String[] getSpuserSubnoByCorpCode(String corpCode,LfSysuser AdminUser, LfFlowRecord lfFlowRecord){
			String[] arr = new String[3];
		try{
			arr[0] = "fail";
			//sp账号
			String sp="";
			String isUsable="";
			ErrorCodeParam errorCode =new ErrorCodeParam();
			//扩展尾号
			String menuCode = StaticValue.VERIFYREMIND_MENUCODE;
			if(lfFlowRecord.getInfoType()==6){
				//网讯模板用网讯模板定义的模块编码
				menuCode = StaticValue.WXREMIND_MENUCODE;
			}
			String subno=GlobalVariableBiz.getInstance().getValidSubno(menuCode, 0, corpCode,errorCode);
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
			EmpExecutionContext.error(e,"查询出所对应的SPUSER以及尾号出现异常！");
			arr[0] = "fail";
		}
		return arr;
	}
	
	
	/**
	 *  通过企业编码查询出所对应的SPUSER以及尾号
	 * @param corpCode 企业编码
	 * @param
	 * @return
	 */
	public String[] getSpuserSubnoByCorpCodeCuiban(String corpCode,LfSysuser AdminUser, LfFlowRecord lfFlowRecord,int spisuncm){
			String[] arr = new String[3];
		try{
			arr[0] = "fail";
			//sp账号
			String sp="";
			String isUsable="";
			ErrorCodeParam errorCode =new ErrorCodeParam();
			//扩展尾号
			String menuCode = StaticValue.VERIFYREMIND_MENUCODE;
			if(lfFlowRecord.getInfoType()==6){
				//网讯模板用网讯模板定义的模块编码
				menuCode = StaticValue.WXREMIND_MENUCODE;
			}
			String subno=GlobalVariableBiz.getInstance().getValidSubno(menuCode, 0, corpCode,errorCode);
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
						isUsable= new SubnoManagerBiz().checkPortUsed(spUser.getUserId(), subno,spisuncm);
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
			EmpExecutionContext.error(e,"查询出所对应的SPUSER以及尾号出现异常！");
			arr[0] = "fail";
		}
		return arr;
	}
	
	/**
	 *   增加的网讯模板 审核，增加审核拒绝，最后一级审核的提醒
	 * @param
	 * @param batchNumber	同意指令
	 * @param disagreeNumber	不同意指令
	 * @param createTaskUser	提交任务人的对象
	 * @param number	第几级
	 * @return
	 */
	public String getWXMailContent(LfWXBASEINFO lfWXBasinfo,String batchNumber,String disagreeNumber,LfSysuser createTaskUser,String number,LfFlowRecord lfFlowRecord){
			String content="";
			String title="网讯模板";

			if(!"".equals(number)){
				content="模板类型："+title+"</br>名称："+lfWXBasinfo.getNAME()+"</br> ID："+lfWXBasinfo.getNETID()+"</br>第"+number+"级审批不通过，审批意见："+lfFlowRecord.getComments()+"，审批人："+createTaskUser.getName();
			}else{
				content="模板类型："+title+"</br>名称："+lfWXBasinfo.getNAME()+"</br> ID："+lfWXBasinfo.getNETID()+" </br>最后一级审批通过。";
				
			}
		return content;
	}
	
	/**
	 *   增加的网讯模板 审核，增加审核拒绝，最后一级审核的提醒
	 * @param batchNumber	同意指令
	 * @param disagreeNumber	不同意指令
	 * @param createTaskUser	提交任务人的对象
	 * @param number	第几级
	 * @return
	 */
	public String getWXContent(LfWXBASEINFO lfWXBasinfo,String batchNumber,String disagreeNumber,LfSysuser createTaskUser,String number,LfFlowRecord lfFlowRecord){
			String content="";
			String title="网讯模板";

			if(!"".equals(number)){
				content=title+"[名称："+lfWXBasinfo.getNAME()+"，ID："+lfWXBasinfo.getNETID()+"]第"+number+"级审批不通过，审批意见："+lfFlowRecord.getComments()+"，审批人："+createTaskUser.getName();
			}else{
				content=title+"[名称："+lfWXBasinfo.getNAME()+"，ID："+lfWXBasinfo.getNETID()+"]最后一级审批通过。可供发送界面选用。";
				
			}
		return content;
	}
	/**
	 *   2013-11-15增加的短信审核，增加审核拒绝，最后一级审核的提醒
	 * @param mttask	该任务对象
	 * @param batchNumber	同意指令
	 * @param disagreeNumber	不同意指令
	 * @param createTaskUser	提交任务人的对象
	 * @param number	第几级
	 * @return
	 */
	public String getMailContent(LfMttask mttask,LfSysuser createTaskUser, String number,LfFlowRecord lfFlowRecord){
		String content = "";
			String msgContents = mttask.getMsg();
			if(msgContents != null && msgContents.length()>797)
			{
				msgContents=msgContents.substring(0,797)+"...";
			}
			String title="短信发送";
			if(lfFlowRecord.getInfoType()==1){
				title="短信发送";
			}else if(lfFlowRecord.getInfoType()==2){
				title="彩信发送";
			}else if(lfFlowRecord.getInfoType()==5){
				title="网讯发送";
			}
			if(!"".equals(number)){
				content="发送类型："+title+"</br>主题："+mttask.getTitle()+" </br> 第"+number+"级审批不通过，审批意见："+lfFlowRecord.getComments()+"，审批人："+createTaskUser.getName();
			}else{
				content="发送类型："+title+"</br>主题："+mttask.getTitle()+" </br> 最后一级审批通过。";
				
			}
		return content;
	}
	
	
	
	/**
	 *   2013-11-15增加的短信审核，增加审核拒绝，最后一级审核的提醒
	 * @param mttask	该任务对象
	 * @param batchNumber	同意指令
	 * @param disagreeNumber	不同意指令
	 * @param createTaskUser	提交任务人的对象
	 * @param number	第几级
	 * @return
	 */
	public String getContent(LfMttask mttask,String batchNumber,String disagreeNumber,LfSysuser createTaskUser, String number,LfFlowRecord lfFlowRecord){
		String content = "";
			String msgContents = mttask.getMsg();
			if(msgContents != null && msgContents.length()>797)
			{
				msgContents=msgContents.substring(0,797)+"...";
			}
			String title="短信发送";
			if(lfFlowRecord.getInfoType()==1){
				title="短信发送";
			}else if(lfFlowRecord.getInfoType()==2){
				title="彩信发送";
			}else if(lfFlowRecord.getInfoType()==5){
				title="网讯发送";
			}
			if(!"".equals(number)){
				content=title+"[主题："+mttask.getTitle()+"，任务批次："+disagreeNumber+"]第"+number+"级审批不通过，审批意见："+lfFlowRecord.getComments()+"，审批人："+createTaskUser.getName();
			}else{
				content=title+"[主题："+mttask.getTitle()+"，任务批次："+batchNumber+"] 最后一级审批通过。";
				
			}
		return content;
	}
	/**
	 *   增加的模板 审核，增加审核拒绝，最后一级审核的提醒
	 * @param createTaskUser	提交任务人的对象
	 * @param number	第几级
	 * @return
	 */
	public String getTempMailContent(LfTemplate template,LfSysuser createTaskUser,String number,LfFlowRecord lfFlowRecord){
		String content = "";
			String msgContents = template.getTmMsg();
			if(msgContents != null && msgContents.length()>797)
			{
				msgContents=msgContents.substring(0,797)+"...";
			}
			String title="短信模板";
			if(lfFlowRecord.getInfoType()==3){
				title="短信模板";
			}else if(lfFlowRecord.getInfoType()==4){
				title="彩信模板";
			}else if(lfFlowRecord.getInfoType()==6){
				title="网讯模板";
			}
			if(!"".equals(number)){
				content="模板类型："+title+"</br>名称："+template.getTmName()+" </br>  ID："+template.getTmid()+"]第"+number+"级审批不通过，审批意见："+lfFlowRecord.getComments()+"，审批人："+createTaskUser.getName();
			}else{
				content="模板类型："+title+"</br>名称："+template.getTmName()+" </br>  ID："+template.getTmid()+"]最后一级审批通过。";
				
			}
		return content;
	}
	
	/**
	 *   2013-11-15增加的模板 审核，增加审核拒绝，最后一级审核的提醒
	 * @param mttask	该任务对象
	 * @param batchNumber	同意指令
	 * @param disagreeNumber	不同意指令
	 * @param createTaskUser	提交任务人的对象
	 * @param number	第几级
	 * @return
	 */
	public String getTempContent(LfTemplate template,String batchNumber,String disagreeNumber,LfSysuser createTaskUser,String number,LfFlowRecord lfFlowRecord){
		String content = "";
			String msgContents = template.getTmMsg();
			if(msgContents != null && msgContents.length()>797)
			{
				msgContents=msgContents.substring(0,797)+"...";
			}
			String title="短信模板";
			if(lfFlowRecord.getInfoType()==3){
				title="短信模板";
			}else if(lfFlowRecord.getInfoType()==4){
				title="彩信模板";
			}else if(lfFlowRecord.getInfoType()==6){
				title="网讯模板";
			}
			if(!"".equals(number)){
				content=title+"[名称："+template.getTmName()+"，ID："+template.getTmid()+"]第"+number+"级审批不通过，审批意见："+lfFlowRecord.getComments()+"，审批人："+createTaskUser.getName();
			}else{
				content=title+"[名称："+template.getTmName()+"，ID："+template.getTmid()+"]最后一级审批通过。可供发送界面选用。";
				
			}
		return content;
	}
	
	
	/**
	 *   获取下发内容
	 * @param mttask	该任务对象
	 * @param batchNumber	同意指令
	 * @param disagreeNumber	不同意指令
	 * @param createTaskUser	提交任务人的对象
	 * @return
	 */
	public String getRemindContent(LfMttask mttask,String batchNumber,String disagreeNumber,LfSysuser createTaskUser){
		String content = "";
		try{
			//短信内容
			Integer bmtType= mttask.getBmtType();
			//预发送条数
			String sendCount = "0";
			String bmt="相同内容群发";
			String msgContents = mttask.getMsg();
			if(msgContents != null && msgContents.length()>797)
			{
				msgContents=msgContents.substring(0,797)+"...";
			}
			if(mttask.getMsType()==1){
				if(bmtType==1)
				{
					bmt="相同内容群发";
				}else if(bmtType==2)
				{
					bmt="动态模板短信群发";
				}else if(bmtType==3)
				{
					bmt="文件内容短信群发";
					msgContents="详见文件";
				}
				else if(bmtType==4)
				{
					bmt="客户群组群发";
					bmtType=1;
				}
				sendCount=new SmsBiz().countAllOprSmsNumber(mttask.getSpUser(), mttask.getMsg(), bmtType, mttask.getMobileUrl(), null)+"";
			}
			else if(mttask.getMsType()==2)
			{
				bmt="彩信群发";
				sendCount= mttask.getEffCount().toString();
				msgContents= mttask.getTaskName();
			}else if(mttask.getMsType()==6){
				sendCount=new SmsBiz().countAllOprSmsNumber(mttask.getSpUser(), mttask.getMsg(), bmtType, mttask.getMobileUrl(), null)+"";
			}else if(mttask.getMsType()==31){
				if(bmtType==1)
				{
					bmt="相同内容群发";
				}else if(bmtType==2)
				{
					bmt="动态模板短信群发";
				}else if(bmtType==3)
				{
					bmt="文件内容短信群发";
					msgContents="详见文件";
				}
				else if(bmtType==4)
				{
					bmt="客户群组群发";
					bmtType=1;
				}
				sendCount=new SmsBiz().countAllOprSmsNumber(mttask.getSpUser(), mttask.getMsg(), bmtType, mttask.getMobileUrl(), null)+"";
			}
			
			if(mttask.getMsType() == 2)
			{
				content="您有信息需要审批：提交人："+createTaskUser.getName()+"，预发送条数："+mttask.getEffCount()+"，号码数:"
				+mttask.getEffCount()+"，发送类型："+"彩信"+"，主题："+mttask.getTaskName()+"，标题："+mttask.getTitle()
				+"，彩信审批48小时之内回复有效，通过审批回复:"+batchNumber+"，不通过审批回复:"+disagreeNumber+"+意见";
			}
			else if(mttask.getMsType() == 1)
			{
				if(mttask.getTitle()==null ||"".equals(mttask.getTitle()))
				{
					mttask.setTitle("-");
				}
				content="您有信息需要审批：提交人："+createTaskUser.getName()+"，预发送条数："+mttask.getIcount()+"，号码数:"
				+mttask.getEffCount()+"，发送类型："+bmt+"，主题："+mttask.getTitle()+"，内容："+msgContents
				+"，短信审批48小时之内回复有效，通过审批回复:"+batchNumber+"，不通过审批回复:"+disagreeNumber+"+意见";
			}else if(mttask.getMsType() == 6)
			{
				if(mttask.getTitle()==null ||"".equals(mttask.getTitle()))
				{
					mttask.setTitle("-");
				}
				content="您有信息需要审批：提交人："+createTaskUser.getName()+"，预发送条数："+mttask.getIcount()+"，号码数:"
				+mttask.getEffCount()+"，主题："+mttask.getTitle()+"，内容："+msgContents
				+"，短信审批48小时之内回复有效，通过审批回复:"+batchNumber+"，不通过审批回复:"+disagreeNumber+"+意见";
			}else if(mttask.getMsType() == 31){
				if(mttask.getTitle()==null ||"".equals(mttask.getTitle()))
				{
					mttask.setTitle("-");
				}
				content="您有信息需要审批：提交人："+createTaskUser.getName()+"，预发送条数："+mttask.getIcount()+"，号码数:"
				+mttask.getEffCount()+"，发送类型："+bmt+"，主题："+mttask.getTitle()+"，内容："+msgContents
				+"，短信审批48小时之内回复有效，通过审批回复:"+batchNumber+"，不通过审批回复:"+disagreeNumber+"+意见";
			}
		}catch (Exception e) {
			content = "";
			EmpExecutionContext.error(e,"获取下发内容出现异常！");
		}
		return content;
	}
	
	/**
	 *   获取下发内容
	 * @param mttask	该任务对象
	 * @param createTaskUser	提交任务人的对象
	 * @return
	 */
	public String getEmailContent(LfMttask mttask,LfSysuser createTaskUser){
		String content = "";
		try{
			//短信内容
			Integer bmtType= mttask.getBmtType();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//预发送条数
			String bmt="相同内容群发";
			String msgContents = mttask.getMsg();
			if(msgContents != null && msgContents.length()>797)
			{
				msgContents=msgContents.substring(0,797)+"...";
			}
			if(mttask.getMsType()==1){
				if(bmtType==1)
				{
					bmt="相同内容群发";
				}else if(bmtType==2)
				{
					bmt="动态模板短信群发";
				}else if(bmtType==3)
				{
					bmt="文件内容短信群发";
					msgContents="详见文件";
				}
				else if(bmtType==4)
				{
					bmt="客户群组群发";
					bmtType=1;
				}
			}
			else if(mttask.getMsType()==2)
			{
				bmt="彩信群发";
				msgContents= mttask.getTaskName();
			}
			
			if(mttask.getMsType() == 2)
			{
				content="提交人："+createTaskUser.getName()+"</br>预发送条数："+mttask.getEffCount()+"</br>号码数:"
				+mttask.getEffCount()+"</br>发送类型："+"彩信"+"</br>主题："+mttask.getTaskName()+" </br>标题："+mttask.getTitle();
			}
			else if(mttask.getMsType() == 1)
			{
				if(mttask.getTitle()==null ||"".equals(mttask.getTitle()))
				{
					mttask.setTitle("-");
				}
				content="提交人："+createTaskUser.getName()+"</br>预发送条数："+mttask.getIcount()+"</br>号码数:"
				+mttask.getEffCount()+"</br>发送类型："+bmt+" </br>主题："+mttask.getTitle()+"</br>内容："+msgContents;
			}else if(mttask.getMsType() == 6)
			{
				if(mttask.getTitle()==null ||"".equals(mttask.getTitle()))
				{
					mttask.setTitle("-");
				}
				content="提交人："+createTaskUser.getName()+"</br>预发送条数："+mttask.getIcount()+"</br>号码数:"
				+mttask.getEffCount()+"</br>主题："+mttask.getTitle()+" </br>内容："+msgContents;
			}
		}catch (Exception e) {
			content = "";
			EmpExecutionContext.error(e,"获取下发内容出现异常！");
		}
		return content;
	}
	
	/**
	 *   返回下发的模板审批信息短信
	 * @param lfTemplate  摸板对象 
	 * @param batchNumber	同意指令
	 * @param disagreeNumber	不同意指令
	 * @param createTaskUser	提交人对象
	 * @return
	 */
	public String getRemindTmpContent(LfTemplate lfTemplate,String batchNumber,String disagreeNumber,LfSysuser createTaskUser){
			String content = "";
		try{
			String tempMsg=lfTemplate.getTmMsg();
			if(lfTemplate.getTmMsg()!=null && lfTemplate.getTmpType() == 3)
			{
				if(lfTemplate.getTmMsg().length()>797)
				{
					tempMsg=lfTemplate.getTmMsg().substring(0,797)+"...";
				}
			}
			if(lfTemplate.getTmpType() == 3)
			{
				content="您有短信模板需要审批：提交人："+createTaskUser.getName()+"，主题："+lfTemplate.getTmName()+",模板内容："
				+tempMsg+"，短信模板审批48小时之内回复有效，通过审批回复:"+batchNumber+"，不通过审批回复:"+disagreeNumber+"+意见";
			}
			else if(lfTemplate.getTmpType() == 4)
			{
				content="您有彩信模板需要审批：提交人："+createTaskUser.getName()+"，主题："+lfTemplate.getTmName()
				+"，彩信模板审批48小时之内回复有效，通过审批回复:"+batchNumber+"，不通过审批回复:"+disagreeNumber+"+意见";
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e," 返回下发的模板审批信息短信出现异常！");
			content = "";
		}
		return content;
	}
	
	/**
	 *   返回下发的模板审批信息短信
	 * @param lfTemplate  摸板对象 
	 * @param batchNumber	同意指令
	 * @param disagreeNumber	不同意指令
	 * @param createTaskUser	提交人对象
	 * @return
	 */
	public String getRemindEmailContent(LfTemplate lfTemplate,LfSysuser createTaskUser){
			String content = "";
		try{
			String tempMsg=lfTemplate.getTmMsg();
			if(lfTemplate.getTmMsg()!=null && lfTemplate.getTmpType() == 3)
			{
				if(lfTemplate.getTmMsg().length()>797)
				{
					tempMsg=lfTemplate.getTmMsg().substring(0,797)+"...";
				}
			}
			if(lfTemplate.getTmpType() == 3)
			{
				content=" 提交人："+createTaskUser.getName()+"，</br>主题："+lfTemplate.getTmName()+",</br>模板内容："
				+tempMsg+"。";
			}
			else if(lfTemplate.getTmpType() == 4)
			{
				content=" 提交人："+createTaskUser.getName()+"，</br>主题："+lfTemplate.getTmName()
				+"。";
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e," 返回下发的模板审批信息邮件出现异常！");
			content = "";
		}
		return content;
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
	 * @return "2" 提交网关失败 “1” 发送网关成功
	 */
	public String sendMsgByMoblieStr(List<LfExamineSms> lfExamineSms,String sp ,
			String subno,Integer count,boolean flag,String Rptflag,String menuCode,String phone,String msg,LfSysuser lfSysuser) {
		String result = "2";
		int resultMsg = -1;
		//检查sp余额是否足够发送
		int spResult = new BalanceLogBiz().checkSpUserFee(sp, count, 1);
		if (spResult < 0) {
			if (spResult == -3) {
				// 没有账号信息
				EmpExecutionContext.error("查询不到sp账号信息。spuser:" + sp);
				return "noSpInfo";
			} else if (spResult == -2) {
				// 余额不足
				EmpExecutionContext.error("sp账号余额不足。spuser:" + sp);
				return "noSuffiSpFee";
			} else {
				EmpExecutionContext.error("查询sp账号信息异常。spuser:" + sp);
				return "spFail";
			}
		}
		
		//如果扣费开启
		if(flag){
			//获得数据库连接
			Connection conn = empTransDao.getConnection();
			try {
				//开启事物
				empTransDao.beginTransaction(conn);
				//String resultMsg = new BalanceLogBiz().sendSmsAmountByGuid(conn, lfSysuser.getGuId(), count);
				resultMsg = new BalanceLogBiz().sendSmsAmountByUserId(conn, lfSysuser.getUserId(), count);
				//0:短信扣费成功；
				if(resultMsg == 0){
					//先执行运营商扣费
					boolean kf = this.wgKoufei(count, sp, lfSysuser.getCorpCode());
					if(!kf){
						EmpExecutionContext.error("运营商扣费失败！");
						return result;
					}
					String[] wegGateResult= this.flowSendMsg(menuCode, lfSysuser.getUserCode(), phone, msg, sp, subno, Rptflag);
					if(wegGateResult == null || wegGateResult.length == 0){
						empTransDao.rollBackTransaction(conn);
						//未发送成功
						result = "2";	
						//运营商扣费回收
						new BalanceLogBiz().huishouFee(count,sp,1);
					}else if("000".equals(wegGateResult[0]) ){
						//发送到网关
						result = "1";			
						if(lfExamineSms!=null)
						{
							empTransDao.save(conn, lfExamineSms, LfExamineSms.class);
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
				EmpExecutionContext.error(e,"发送短信失败！");
			}finally{
				empTransDao.closeConnection(conn);
				try{
					if(result != null && "1".equals(result)){
						String buffer = "短信审批提醒扣费成功，总费用："+count;
						this.dailyFlowRecord(lfSysuser, buffer,"success");
					}else if(result != null && resultMsg ==  0 && !"1".equals(result)){
						String buffer = "短信审批提醒提交网关失败！";
						this.dailyFlowRecord(lfSysuser, buffer,"fail");
					}else{
						String buffer = "短信审批提醒扣费失败，错误返回值："+resultMsg;
						this.dailyFlowRecord(lfSysuser, buffer,"fail");
					}
				}catch (Exception e) {
					EmpExecutionContext.error(e,"短信审批提醒扣费日志异常！");
				}
			}
		}else{
			try {
				//先执行运营商扣费
				boolean kf = this.wgKoufei(count, sp, lfSysuser.getCorpCode());
				if(!kf){
					EmpExecutionContext.error("运营商扣费失败！");
					return result;
				}
				String[] wegGateResult= this.flowSendMsg(menuCode,
						 lfSysuser.getUserCode(), phone, msg, sp, subno, Rptflag);
				if(wegGateResult == null || wegGateResult.length == 0){
					//未发送成功
					result = "2";	
					//运营商扣费回收
					new BalanceLogBiz().huishouFee(count,sp,1);
				}
				if("000".equals(wegGateResult[0]) ){
					//发送到网关
					result = "1";			
					if(lfExamineSms!=null)
					{
						empDao.save(lfExamineSms, LfExamineSms.class);
					}
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"发送短信失败！");
			}
		}
		return result;
	}
	/**
	 *   发送文件
	 * @param menuCode	模块编码
	 * @param userCode	用户编码
	 * @param phone	手机号码 
	 * @param msg	短信内容 
	 * @param sp	SPUSER
	 * @param subno 尾号
	 * @param Rptflag 	是否需要状态报告
	 * @return
	 * @throws Exception
	 */
	private String[] flowSendMsg(String menuCode, String userCode,
			 String phone, String msg,String sp,String subno,String Rptflag) throws Exception {
		if(phone == null || "".equals(phone.trim()) ){
			EmpExecutionContext.error("发送号码或文件为空！");
			return null;
		}else{
			if(phone.lastIndexOf(",")==phone.length()-1){
				phone = phone.substring(0, phone.length()-1);
			}
		}
		HttpSmsSend jobBiz = new HttpSmsSend();
		String[] resultReceive = new String[2];
		if(subno == null){
			EmpExecutionContext.error("子号对象为空！");
			return null;
		}
		WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
		Userdata userdata = wgMsgBiz.getSmsUserdataByUserid(sp);
		String webGateResult;
		WGParams wgParams = new WGParams();
		wgParams.setSpid(sp);
		wgParams.setSppassword(userdata.getUserPassword());
		wgParams.setDas(phone);
		wgParams.setSm(msg);
		wgParams.setCommand("MULTI_MT_REQUEST");
		wgParams.setRptflag("0");
		wgParams.setSvrtype("M00000");	
		wgParams.setSa(subno);	
		wgParams.setParam1(String.valueOf(userCode));
		wgParams.setModuleid(StaticValue.VERIFYREMIND_MENUCODE);	
		webGateResult = jobBiz.createbatchMtRequest(wgParams);
		resultReceive[1] = this.parseWebgateResult(webGateResult, "mtmsgid");
		int index = webGateResult.indexOf("mterrcode");
		resultReceive[0] = webGateResult.substring(index+10,index+13);
		if(!resultReceive[0].equals("000")){
			resultReceive[0] = webGateResult.substring(index-8,index-1);
			EmpExecutionContext.debug("审批提醒或发送动态口令到短信网关返回错误代码："+resultReceive[0]);
		}
		return resultReceive;
	}
	/**
	 *   发送登录密码下行到手机上去
	 * @param sp	发送账号
	 * @param msg	密码内容
	 * @param phone	手机号码
	 * @param userCode	用户编码
	 * @return
	 */
	public String sendAutoPassMsgByOne(String sp ,String msg,String phone,String userCode,Long taskId) {
		String result = "2";
		try {
			WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
			HttpSmsSend jobBiz = new HttpSmsSend();
			String[] resultReceive = new String[2];
			Userdata userdata = wgMsgBiz.getSmsUserdataByUserid(sp);
			String webGateResult;
			WGParams wgParams = new WGParams();
			wgParams.setCommand("MT_REQUEST");
			//spuser帐户
			wgParams.setSpid(sp);
			//密码
			wgParams.setSppassword(userdata.getUserPassword());	
			//用户编码
			wgParams.setParam1(userCode);	
			//拓展子号
			wgParams.setSa("");		
			//手机
			wgParams.setDa(phone);			
			//任务ID
			wgParams.setTaskid(String.valueOf(taskId));	
			wgParams.setPriority("20");
			wgParams.setSm(msg);
			//默认业务编码
			wgParams.setSvrtype("M00000");	
			//模块ID
			wgParams.setModuleid("");	
			//设值为0的不需要状态报告，其他值是需要状态报告
			wgParams.setRptflag("0");		
			//wgParams.setMsgid(taskId.toString());
			webGateResult = jobBiz.createbatchMtRequest(wgParams);
			resultReceive[1] = this.parseWebgateResult(webGateResult, "mtmsgid");
			int index = webGateResult.indexOf("mterrcode");
			resultReceive[0] = webGateResult.substring(index+10,index+13);
			if(!resultReceive[0].equals("000")){
				resultReceive[0] = webGateResult.substring(index-8,index-1);
				EmpExecutionContext.debug("网关返回错误代码："+resultReceive[0]);
			}else{
				//成功
				result = "1";
			}
		} catch (Exception e) {
			EmpExecutionContext.debug("发送登录密码下行到手机失败！");
		}
		return result;
	}
	
	public boolean wgKoufei(int count,String sp,String corpCode){
		LfMttask mTask = new LfMttask();
		mTask.setIcount(String.valueOf(count));
		mTask.setSpUser(sp);
		mTask.setCorpCode(corpCode);
		String wgresult = "";
		try
		{
			wgresult = new BalanceLogBiz().wgKoufei(mTask);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "审批提醒短信运营商扣费失败！");
			return false;
		}
		if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || "lessgwfee".equals(wgresult.substring(0, 9)))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 处理短信，彩信，短信模板，彩信模板，网讯模板审批催办
	 * @param lfFlowRecord 审核实时对象
	 * @return  success:发送成功
	 * getTaskFail:获取发送任务信息失败,getDcTempFail:获取模板信息失败,getWxTempFail:获取网讯模板信息失败,
	 * noPhone：审批人员没有设置手机号码,noContent:生成催办短信内容失败,noAdmin:获取admin失败,noAgree:获取同意指令失败,
	 * noDisAgree:获取不同意指令失败,noSP:没有可用的SP账号,noSubNo:没有可用的尾号,noSpNumber:没有可用的通道号
	 * validPhone:手机号码格式非法,sendFail:发送催办短信失败,wgkoufeiFail:运营商扣费失败
	 */
	public String cuibanSms(LfFlowRecord lfFlowRecord)
	{
		String resultStr=null;
		LfSysuser AdminUser = null; 
		StringBuffer buffer = new StringBuffer();
		try{
			//审核流程ID
			if(lfFlowRecord.getFId() != null){
				buffer.append(" 审核流程ID ：").append(lfFlowRecord.getFId()).append(";");
			}else{
				buffer.append(" 审核流程ID ：无;");
			}
			SmsBiz smsbiz=new SmsBiz();
			//短信提醒内容
			String content="";
			//创建的任务
			LfMttask mt  = null;
			LfTemplate lfTemplate= null ;
			LfWXBASEINFO baseInfo=null;
			//根据类型判断是否 1短信发送2 彩信发送  3短信模板4彩信摸板 5网讯发送 6网讯模板
			if(lfFlowRecord.getInfoType() == 1 || lfFlowRecord.getInfoType() == 2|| lfFlowRecord.getInfoType() == 5)
			{
				//根据mt_id获取lf_mttask短信任务对象
				mt= new CommonBiz().getLfMttaskbyTaskId(lfFlowRecord.getMtId());
				if(mt == null){
					EmpExecutionContext.error("任务ID为："+lfFlowRecord.getMtId()+"催办短信下发失败，获取任务失败！");
					resultStr="getTaskFail";
					return resultStr;
				}
			}else if(lfFlowRecord.getInfoType() == 3 || lfFlowRecord.getInfoType() == 4){
				//根据短信和彩信模板ID，获取模板对象
				lfTemplate = empDao.findObjectByID(LfTemplate.class,lfFlowRecord.getMtId());
				if(lfTemplate == null){
					EmpExecutionContext.error("短信或者彩信模板ID为："+lfFlowRecord.getMtId()+"催办短信下发失败，获取任务失败！");
					resultStr="getDcTempFail";
					return resultStr;
				}
			}else if(lfFlowRecord.getInfoType() == 6){
				//根据网讯模板ID，获取网讯模板对象
				baseInfo = empDao.findObjectByID(LfWXBASEINFO.class, lfFlowRecord.getMtId());
				if(baseInfo == null){
					EmpExecutionContext.error("网讯模板ID为："+lfFlowRecord.getMtId()+"催办短信下发失败，获取任务失败！");
					resultStr="getWxTempFail";
					return resultStr;
				}
			}
	    	LinkedHashMap<String, String> conditionMap =new LinkedHashMap<String, String>();
	    	//任务创建人
	    	LfSysuser lfSysuserCreate=empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getProUserCode());
	    	//admin用户
	    	conditionMap.put("userName", "admin");
	    	//当前登录用户的企业编码
			conditionMap.put("corpCode", lfSysuserCreate.getCorpCode());
			//查找admin对象
			List<LfSysuser> AdminLfSysuerList = empDao.findListByCondition(LfSysuser.class, conditionMap, null) ;
			
			if(AdminLfSysuerList!=null && AdminLfSysuerList.size()>0)
			{
				//系统admin
				AdminUser = AdminLfSysuerList.get(0);
			}else{
				//当前审批人
				String str = buffer.toString() + " 获取该企业的admin失败。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				resultStr="noAdmin";
				return resultStr;
			}
			
			String phone = null;
			//查找审批人员的手机号码
			conditionMap.clear();
			conditionMap.put("userId&in", String.valueOf(lfFlowRecord.getUserCode()));
			conditionMap.put("corpCode", lfSysuserCreate.getCorpCode());
			List<LfSysuser> sysuserList = empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);;
			if(sysuserList != null && sysuserList.size()>0){
				phone=sysuserList.get(0).getMobile();
			}
			//手机号码为空，则不发催办短信
	    	if(phone == null || phone.length() == 0){
				EmpExecutionContext.error("催办短信没有可用的手机号码！");
				String str = buffer.toString() + " 没有可用的手机号码。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				resultStr="noPhone";
				return resultStr;
	    	}
	    	WgMsgConfigBiz wgMsgConfigBiz=new WgMsgConfigBiz();
	    	//拿到当前系统配置的号段
			String[] haoduans= wgMsgConfigBiz.getHaoduan();	
			//-1: 非法号码  0:移动号码  1:联通号码  2:电信号码 3:国际号码
			int phoneSpisuncm=phoneUtil.getPhoneType(phone, haoduans);
			if(phoneSpisuncm==-1){
				resultStr="validPhone";
				return resultStr;
			}else{
				//2是电信号码
				if(phoneSpisuncm==2){
					//电信运营商在网关数据库存储的是21
					phoneSpisuncm=21;
				}
				//3是国际号码
				if(phoneSpisuncm==3){
					//国外运营商在网关数据库存储的是5
					phoneSpisuncm=5;
				}
			}
			
	    	//获取SP账号以及尾号 arr[1]子号  arr[2]sp账号
			String[] arr = this.getSpuserSubnoByCorpCodeCuiban(lfSysuserCreate.getCorpCode(),AdminUser, lfFlowRecord,phoneSpisuncm);
			String spUser = "";
			String subno = "";
			//记录该 催办短信的信息
			if(arr != null && "success".equals(arr[0])){
				subno = arr[1];
				spUser = arr[2];
				buffer.append("子号 ：").append(subno).append(";sp账号 ").append(spUser).append(";");
			}else{
				//获取失败记录日志
				if(arr != null && "fail".equals(arr[0])){
					EmpExecutionContext.error("催办短信获取发送账号失败！");
					String str = buffer.toString() + " 获取发送账号失败。";
					this.dailyFlowRecord(AdminUser,str, "fail");
				}else if(arr != null && "nosubno".equals(arr[0])){
					EmpExecutionContext.error("催办短信没有可用的尾号！");
					String str = buffer.toString() + " 获取没有可用的尾号。";
					this.dailyFlowRecord(AdminUser,str, "fail");
					resultStr="noSubNo";
					return resultStr;
				}
				resultStr="noSP";
				return resultStr;
			}
			//批次号(同意)
			String batchNumber= GlobalVariableBiz.getInstance().getNewNodeCode();
			//批次号(不同意)
			String disagreeNumber= GlobalVariableBiz.getInstance().getNewNodeCode();
			//获取同意失败
			if(batchNumber == null || "".equals(batchNumber)){
				EmpExecutionContext.error("催办短信获取同意指令失败！");
				String str = buffer.toString() + " 获取同意指令失败。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				resultStr="noAgree";
				return resultStr;
			}
			//获取不同意失败
			if(disagreeNumber == null || "".equals(disagreeNumber)){
				EmpExecutionContext.error("催办短信获取不同意指令失败！");
				String str = buffer.toString() + " 获取不同意指令失败。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				resultStr="noDisAgree";
				return resultStr;
			}
			buffer.append(" 同意指令：").append(batchNumber).append(";不同意指令: ").append(disagreeNumber).append(";");
    		if(lfFlowRecord.getRType() != null){
    			buffer.append("审批类型：").append(lfFlowRecord.getRType()).append(";");
    		}else{
    			buffer.append("审批类型：未知;");
    		}
    		    
	    	//获取催办的模板
			if(lfFlowRecord.getInfoType() == 1 || lfFlowRecord.getInfoType() == 2|| lfFlowRecord.getInfoType() == 5)
			{
				//获取短信，彩信，网讯的催办模板
				content = this.getRemindContent(mt, batchNumber, disagreeNumber, lfSysuserCreate);
			}else if(lfFlowRecord.getInfoType() == 3 || lfFlowRecord.getInfoType() == 4){
				//获取短信模板，彩信模板催办模板
				content = this.getRemindTmpContent(lfTemplate, batchNumber, disagreeNumber, lfSysuserCreate);
			}else if(lfFlowRecord.getInfoType() == 6){
				 //获取网讯模板的催办模板
				content=this.getRemindTmpContent(baseInfo, batchNumber, disagreeNumber, lfSysuserCreate);
			}
			//催办模板获取为空
			if(content == null || "".equals(content)){
				EmpExecutionContext.error("催办短信内容为空！");
				String str = buffer.toString() + " 催办短信内容为空。";
				this.dailyFlowRecord(AdminUser,str, "fail");
				resultStr="noContent";
				return resultStr;
			}
			//SP账号不为空
			if(!"".equals(spUser) &&  spUser != null)
			{
				List<GtPortUsed> portsList = smsbiz.getPortByUserId(spUser);
				//计算条数
				StringBuffer sendBuffer = new StringBuffer("");
				int messageCount = 0;
				//是否计费
				boolean flag = SystemGlobals.isDepBilling(lfSysuserCreate.getCorpCode());
				LfExamineSms lfExamineSms = null;
				List<LfExamineSms> examineSmsList = new ArrayList<LfExamineSms>();
				//获取运营商和通道号码的MAP集合
				Map<Integer, String> map = new SubnoManagerBiz().getPortUserBySpuser(portsList, spUser, subno);
				try{
					    //验证手机号码
						if(phone != null && !"".equals(phone) && phoneUtil.getPhoneType(phone, haoduans)>= 0){
							phone = phone.trim();
							sendBuffer.append(phone).append(",");
							//计算预发送条数
							int count = smsbiz.countAllOprSmsNumber(content, phone, haoduans, spUser);
							messageCount = messageCount + count;
							//生成短信审批对象
							lfExamineSms = new LfExamineSms();
							//同意指令
							lfExamineSms.setBatchNumber(batchNumber);
							//不同意指令
							lfExamineSms.setDisagreeNumber(disagreeNumber);
							//审批流程记录ID
							lfExamineSms.setFrId(lfFlowRecord.getFrId());
							//催办提醒内容
							lfExamineSms.setMsgContent(content);
							//手机号码
							lfExamineSms.setPhone(phone);
							Integer spisuncm = 0;
							//根据号码获取运营商
							if("+".equals(phone.substring(0, 1)) || "00".equals(phone.substring(0, 2)))
							{
								//国际号码
								spisuncm = 5;
							}
							else
							{
								//非国际号码
								spisuncm = wgMsgConfigBiz.getPhoneSpisuncm(phone);
							}
							//根据运营商获取通道号码
							String spNumber = map.get(spisuncm);
							if(spNumber == null || "".equals(spNumber)){
								resultStr="noSpNumber";
								return resultStr;
							}
							//设置通道号码
							lfExamineSms.setSpNumber(spNumber);	
							//设置SP账号
							lfExamineSms.setSpUser(spUser);
							//1代表短信提醒 2代表是否成功回复
							lfExamineSms.setEsType(1);
							examineSmsList.add(lfExamineSms);
							//发送催办短信
							if(examineSmsList!=null&&examineSmsList.size()>0){
								resultStr=sendMsgByMoblieStr(examineSmsList, spUser, subno, messageCount, 
											flag,"0", StaticValue.VERIFYREMIND_MENUCODE, sendBuffer.toString(), content, AdminUser,lfFlowRecord.getFrId());
								buffer.append(" 下发条数：1").append(";");
								if(lfFlowRecord.getFId() != null&&lfFlowRecord.getRType() != null){
									this.dailyFlowRecord(AdminUser,buffer.toString(), "success");
								}
								return resultStr;
							}else{
								resultStr="sendFail";
								return resultStr;
							}
						}else
						{
							resultStr="validPhone";
							return resultStr;
						}
					
				}catch (Exception e) {
					EmpExecutionContext.error(e, "催办短信下发记录保存失败！");
					String str = buffer.toString() + " 催办短信下发记录保存失败。";
					this.dailyFlowRecord(AdminUser,str, "fail");
					resultStr="sendFail";
					return resultStr;
				}
			}else{
				resultStr="noSubNo";
				return resultStr;
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"催办短信下发失败！");
			resultStr="sendFail";
			return resultStr;
		}
	}
	/**
	 *   催办短信的发送方法
	 * @param lfExamineSms  保存的催办提醒信息
	 * @param sp	发送帐号
	 * @param subno	尾号
	 * @param count	条数
	 * @param flag	是否计费
	 * @param Rptflag	是否状态报告 
	 * @param menuCode	模块ID
	 * @param phone	手机号码串
	 * @param msg	内容 
	 * @param lfSysuser	admin对象
	 * @param frId 审核流程ID
	 * @return wgkoufeiFail:运营商扣费失败,sendFail:发送失败,success:发送成功
	 */
	public String sendMsgByMoblieStr(List<LfExamineSms> lfExamineSms,String sp ,
			String subno,Integer count,boolean flag,String Rptflag,String menuCode,String phone,String msg,LfSysuser lfSysuser,Long frId) {
		String result = "sendFail";
		int resultMsg = -1;
		
		//检查sp余额是否足够发送
		int spResult = new BalanceLogBiz().checkSpUserFee(sp, count, 1);
		if (spResult < 0) {
			if (spResult == -3) {
				// 没有账号信息
				EmpExecutionContext.error("查询不到sp账号信息。spuser:" + sp);
				return "noSpInfo";
			} else if (spResult == -2) {
				// 余额不足
				EmpExecutionContext.error("sp账号余额不足。spuser:" + sp);
				return "noSuffiSpFee";
			} else {
				EmpExecutionContext.error("查询sp账号信息异常。spuser:" + sp);
				return "spFail";
			}
		}
		
		//如果扣费开启
		if(flag){
			//获得数据库连接
			Connection conn =null;
			try {
				conn = empTransDao.getConnection();
				//开启事务
				empTransDao.beginTransaction(conn);
				//短信机构扣费
				resultMsg = new BalanceLogBiz().sendSmsAmountByUserId(conn, lfSysuser.getUserId(), count);
				//0:短信扣费成功；
				if(resultMsg == 0){
					//先执行运营商扣费
					boolean kf = this.wgKoufei(count, sp, lfSysuser.getCorpCode());
					if(!kf){
						//运营商扣费失败回滚
						empTransDao.rollBackTransaction(conn);
						EmpExecutionContext.error("运营商扣费失败！");
						result="wgkoufeiFail";
						return result;
					}
					//调用发送方法，发送短信
					String[] wegGateResult= this.flowSendMsg(menuCode, lfSysuser.getUserCode(), phone, msg, sp, subno, Rptflag);
					if(wegGateResult == null || wegGateResult.length == 0){
						empTransDao.rollBackTransaction(conn);
						//未发送成功
						result = "sendFail";	
						//运营商扣费回收
						new BalanceLogBiz().huishouFee(count,sp,1);
					}else if("000".equals(wegGateResult[0]) ){
						empTransDao.save(conn, lfExamineSms, LfExamineSms.class);
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							
						//更新催办时间和催办状态
						LinkedHashMap<String, Object> objectMap =new LinkedHashMap<String, Object>();
						//催办状态设置为已经催办
						objectMap.put("isremind", "1");
						objectMap.put("remindTime", df.format(new Timestamp(System.currentTimeMillis())));
						//更新审核记录
						empTransDao.update(conn, LfFlowRecord.class, objectMap, String.valueOf(frId));
						//提交事务
						empTransDao.commitTransaction(conn);
						//发送到网关
						result = "success";	
					}else{
						empTransDao.rollBackTransaction(conn);
					}
				}else{
					//机构扣费失败回滚
					empTransDao.rollBackTransaction(conn);
				}
			} catch (Exception e) {
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"发送短信失败！");
			}finally{
				empTransDao.closeConnection(conn);
				try{
					if(result != null && "success".equals(result)){
						//短信审批提醒扣费成功
						String buffer = "催办短信扣费成功，总费用："+count;
						this.dailyFlowRecord(lfSysuser, buffer,"success");
					}else if(result != null && resultMsg ==  0 && !"success".equals(result)){
						//短信审批提醒提交网关失败
						String buffer = "催办短信提交网关失败！";
						this.dailyFlowRecord(lfSysuser, buffer,"fail");
					}else{
						//短信审批提醒扣费失败
						String buffer = "催办短信扣费失败，错误返回值："+resultMsg;
						this.dailyFlowRecord(lfSysuser, buffer,"fail");
					}
				}catch (Exception e) {
					EmpExecutionContext.error(e,"催办短信扣费日志异常！");
				}
			}
		}else{
			try {
				//先执行运营商扣费
				boolean kf = this.wgKoufei(count, sp, lfSysuser.getCorpCode());
				if(!kf){
					EmpExecutionContext.error("运营商扣费失败！");
					result="wgkoufeiFail";
					return result;
				}
				//调用发送方法，发送短信
				String[] wegGateResult= this.flowSendMsg(menuCode,
						 lfSysuser.getUserCode(), phone, msg, sp, subno, Rptflag);
				if(wegGateResult == null || wegGateResult.length == 0){
					//未发送成功
					result = "sendFail";	
					//运营商扣费回收
					new BalanceLogBiz().huishouFee(count,sp,1);
				}
				else if("000".equals(wegGateResult[0]) ){
					Connection conn = null;
					try{
						conn = empTransDao.getConnection();
						//开启事物
						empTransDao.beginTransaction(conn);
						empTransDao.save(conn,lfExamineSms, LfExamineSms.class);
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							
						//更新催办时间和催办状态
						LinkedHashMap<String, Object> objectMap =new LinkedHashMap<String, Object>();
						//催办状态设置为已经催办
						objectMap.put("isremind", "1");
						objectMap.put("remindTime", df.format(new Timestamp(System.currentTimeMillis())));
						//更新审核记录
						empTransDao.update(conn, LfFlowRecord.class, objectMap, String.valueOf(frId));
						empTransDao.commitTransaction(conn);
						//发送到网关
						result = "success";	
					}catch(Exception ex){
						empTransDao.rollBackTransaction(conn);
						EmpExecutionContext.error(ex,"保存催办指令到数据库失败！");
					}finally{
						empTransDao.closeConnection(conn);
					}
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"催办短信发送失败！");
			}
		}
		return result;
	}
	
	/**
	 *   返回下发的模板审批信息短信
	 * @param lfTemplate  摸板对象 
	 * @param batchNumber	同意指令
	 * @param disagreeNumber	不同意指令
	 * @param createTaskUser	提交人对象
	 * @return
	 */
	public String getRemindTmpContent(LfWXBASEINFO baseInfo,String batchNumber,String disagreeNumber,LfSysuser createTaskUser){
		String content = "";
		try{
			content="您有网讯模板需要审批：提交人："+createTaskUser.getName()+"，主题："+baseInfo.getNAME()
				+"，网讯模板审批48小时之内回复有效，通过审批回复:"+batchNumber+"，不通过审批回复:"+disagreeNumber+"+意见";
		}catch (Exception e) {
			EmpExecutionContext.error(e, "模板审批信息短信异常!");
			content = "";
		}
		return content;
	}

}