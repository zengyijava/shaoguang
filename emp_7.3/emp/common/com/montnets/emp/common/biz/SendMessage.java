package com.montnets.emp.common.biz;

import java.io.File;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.mmstask.DBMmsSend;
import com.montnets.emp.mmstask.MmsWGParams;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 
 * @author Administrator
 * 
 */
public class SendMessage{
	private IEmpTransactionDAO empTransDao;

	private IEmpDAO empDao;
	
	public SendMessage() {
		empTransDao = new DataAccessDriver().getEmpTransDAO();
		empDao = new DataAccessDriver().getEmpDAO();
	}

	/**
	 *运营商已扣费的时候调用的发送接口 
	 * @param mtId 任务ID
	 * @return
	 * @throws Exception
	 */
	public String sendSms(LfMttask mt, Map<String,String> infoMap) 
	{
		if(infoMap == null || infoMap.get("userCode") == null || infoMap.get("feeFlag") == null)
		{
			infoMap = new CommonBiz().checkMapNull(infoMap, mt.getUserId(), mt.getCorpCode());
		}
		return sendSmsChild( mt, infoMap);
	}
	
	/**
	 * 定时扣费时调用的接口，发送失败时运营商补费
	 * @param mtId 任务ID
	 * @return
	 * @throws Exception
	 */
	public String sendSmsTimer(LfMttask mt, Map<String,String> infoMap) throws Exception 
	{
		if(infoMap == null || infoMap.get("userCode") == null || infoMap.get("feeFlag") == null)
		{
			new CommonBiz().checkMapNull(infoMap, mt.getUserId(), mt.getCorpCode());
		}
		return sendSmsChild(mt, infoMap );
	}
	/**
	 * 发送短信
	 * @param mtId
	 * @return
	 * @throws Exception
	 */
	private String sendSmsChild(LfMttask lfMttask, Map<String,String> infoMap) {
		BalanceLogBiz balanceBiz = new BalanceLogBiz();
		String resultStr = "sendError";
		//返回网关请求信息
		String result = "";
		//实例化网关发送参数类，组装发送接口所需条件 
		WGParams params = new WGParams();
		//是否为异常错误 :false不是  true是
		boolean isExp = false;
		//任务ID
		String taskid = " ";
		try {
			taskid = lfMttask.getTaskId().toString();
			//设置初值为发送失败，后续发送成功时则更改为3
			lfMttask.setSendstate(2);
			
			HttpSmsSend smsSend = new HttpSmsSend();
			
			//发送账号
			params.setSpid(lfMttask.getSpUser());
			if(lfMttask.getSpPwd() == null )
			{
				EmpExecutionContext.info("发送账户的密码为空，重新获取，taskid:"+taskid+",corpCode:"+lfMttask.getCorpCode()
						+",userId:"+lfMttask.getUserId()+",spUser:"+lfMttask.getSpUser());
                //加载过滤条件
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("userId", lfMttask.getSpUser());
				//SP账户类型   1:短信SP账号;2:彩信SP账号
				conditionMap.put("accouttype", "1");
				//调用查询方法获取发送账号
				List<Userdata> tempList = empDao.findListByCondition(
						Userdata.class, conditionMap, null);
				Userdata userData = null;
				if (tempList != null && tempList.size() == 1) {
					userData = tempList.get(0);
					params.setSppassword(userData.getUserPassword());
				}
				else
				{
					EmpExecutionContext.error("发送账户密码为空，重新获取未获取到，taskid:"+taskid+",corpCode:"+lfMttask.getCorpCode()
							+",userId:"+lfMttask.getUserId()+",spUser:"+lfMttask.getSpUser());
					return ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.B20040);
				}
			}else
			{
				params.setSppassword(lfMttask.getSpPwd());
			}
			//相同内容是1，不同内容是2
			Integer msgType = lfMttask.getMsgType();
			params.setBmttype(msgType > 1 ? "2" : "1");
			//短信且为动态模板发送
			if(msgType == 2 && lfMttask.getMsType() == 1)
			{
				//1：换行
				if(lfMttask.getMsgedcodetype() == 35)
				{
					//设置编码类型，支持短信内容换行
					params.setDc("35");
				}
			}
			//判断是否为短链模块
			if(lfMttask.getMsType() == 31){
				params.setDc("35");
			}
			//任务ID
			params.setTaskid(taskid);
			//短信主题
			params.setTitle(lfMttask.getTitle());
			//短信内容
			//params.setPriority("1");
			if(lfMttask.getSendLevel()!=null && !"".equals(lfMttask.getSendLevel()+""))
			{
				params.setPriority(lfMttask.getSendLevel().toString());
			}else
			{
				params.setPriority("1");  //设置发送优先级
			}
			//p1参数，传操作员的内部用户编码
			params.setParam1(infoMap.get("userCode"));
			//业务类型
			params.setSvrtype(lfMttask.getBusCode());
			//是否需要状态报告
			params.setRptflag("0");
			//设置尾号，1：操作员固定尾号，2：回复本次任务
			if(lfMttask.getIsReply()==1 || lfMttask.getIsReply() == 2)
			{
					params.setSa(lfMttask.getSubNo());
			}
			//相同内容群发时，如果有效号码数小于等于50个时，则直接读取号码拼接发送
			if(lfMttask.getEffCount() - 0 < 51 && lfMttask.getBmtType() == 1)
			{
				//设置发送请求为号码群发，不是文件群发
				params.setCommand("MULTI_MT_REQUEST");
				//设置短信内容
				params.setSm(lfMttask.getMsg());
				
				TxtFileUtil txtUtil = new TxtFileUtil();
				String mbileUrl = txtUtil.getWebRoot() + lfMttask.getMobileUrl();
				File mfile = new File(mbileUrl);
				//先判断本地文件是否存在，如不在则从文件服务器下载
				if(!mfile.exists())
				{
					String downresult = "error";
					if(StaticValue.getISCLUSTER() == 1)
					{
						//从文件服务器下载
						downresult = new CommonBiz().downloadFileFromFileCenter(lfMttask.getMobileUrl());
					}
					if(!"success".equals(downresult))
					{
						EmpExecutionContext.error("发送号码文件不存在，taskid:"+taskid);
						resultStr = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.B20041);
					}
				}
				//读文件获取号码字符串
				String phoneString = txtUtil.readFileByLines(lfMttask.getMobileUrl());
				params.setDas(phoneString);
			}else
			{
				//不是相同内容发送，或是相同内容发送且有效号码数大于50这采用文件发送
				//设置文件地址
				params.setUrl(lfMttask.getFileuri() + lfMttask.getMobileUrl());
				//设置发送内容
				params.setContent(lfMttask.getMsg());
			}
            //调用发送接口 
			result = smsSend.createbatchMtRequest(params);
			
			//将返回状态记录到文件里
			//new TxtFileUtil().writeSendResult(lfMttask.getTaskId(), result);
			EmpExecutionContext.requestInfo(lfMttask.getTaskId(), result);
/*			//如果是集群则删除本地文件
			if(StaticValue.ISCLUSTER == 1)
			{
				//删除本地文件 
				new CommonBiz().deleteFile(lfMttask.getMobileUrl());
			}*/

		}catch(ConnectException connEx)
		{
			EmpExecutionContext.error(connEx,"向网关发送请求失败，taskid:"+taskid);
			resultStr = connEx.getMessage();
			isExp = true;
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"短信任务发送请求失败，taskid:"+taskid);
			resultStr = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.B20021);
			isExp = true;
		} finally {
			//如果不是异常错误
			if(!isExp && result != null && !"".equals(result))
			{
				//截取网关返回状态码
				int index = result.indexOf("mterrcode");
				resultStr = result.substring(index + 10, index + 13);
				//返回状态成功
				if ("000".equals(resultStr)) 
				{
					//下行状态设置为已发送成功 
					lfMttask.setSendstate(1);
					if("MULTI_MT_REQUEST".equals(params.getCommand()))
					{
						//为批量下行的任务直接设为网关处理完成
						lfMttask.setSendstate(3);
					}
					EmpExecutionContext.info("短信任务发送请求提交成功！taskid:"+taskid);
				} 
				else 
				{
					//运营商余额回收
					balanceBiz.huishouFee(Integer.parseInt(lfMttask.getIcount()), lfMttask.getSpUser(), 1);
					//如果开启机构计费，则补回机构费用
					if("true".equals(infoMap.get("feeFlag")))
					{
						int recResult = balanceBiz.sendSmsAmountByUserId(null, lfMttask.getUserId(), Integer.parseInt(lfMttask.getIcount())*-1);
						if(recResult != 1)
						{
							EmpExecutionContext.error("补回机构费用失败，recResult:"+recResult+",taskid:" + lfMttask.getTaskId() + ",corpCode:"
												+lfMttask.getCorpCode()+",userId:"+lfMttask.getUserId());
						}
					}
					if(index > -1){
						//截取网关返回的错误状态码
						resultStr = result.substring(index - 8, index - 1);
						EmpExecutionContext.info("短信任务发送请求提交失败！返回码:"+resultStr+"，taskid:" + taskid);
					}else {
						resultStr = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.B20046);
						EmpExecutionContext.error("短信任务发送请求提交失败！响应报文中没有带mterrcode，返回码:"+resultStr+"，taskid:" + taskid);
					}
				}
			}
			else
			{
				//运营商余额回收
				balanceBiz.huishouFee(Integer.parseInt(lfMttask.getIcount()), lfMttask.getSpUser(), 1);
				//如果开启机构计费，则补回机构费用
				if("true".equals(infoMap.get("feeFlag")))
				{
					int recResult = balanceBiz.sendSmsAmountByUserId(null, lfMttask.getUserId(), Integer.parseInt(lfMttask.getIcount())*-1);
					if(recResult != 1)
					{
						EmpExecutionContext.error("补回机构费用失败，recResult:"+recResult+",taskid:" + lfMttask.getTaskId() + ",corpCode:"
											+lfMttask.getCorpCode()+",userId:"+lfMttask.getUserId());
					}
				}
				EmpExecutionContext.error("网关发送请求失败，result:" + result + "，taskid:"+taskid);
			}
			//成功数
			lfMttask.setSucCount(null);
			//失败数
			lfMttask.setFaiCount(null);
		
			int a = 1;
			//如果多次更新失败，则重复更新
			while (a<4) {
				try {
					if(updateMttaskByTaskid(lfMttask))
					{
						a=4;
					}
					else 
					{
						a++;
					}
				} catch (Exception e2) {
					try {
						Thread.sleep(500L);
					} catch (Exception e3) {
						EmpExecutionContext.error(e3,"更新任务表暂停异常!taskid:"+taskid);
					}finally
					{
						EmpExecutionContext.error(e2,"更新任务表失败：第"+a+"次，taskid:"+taskid);
						a++;
					}
				}
			}
		}
		return resultStr;
	}

	/**
	 * @description 发送完成后更新任务表
	 * @param mt 任务对象
	 * @return true-修改成功,false-修改失败
	 * @throws Exception       			 
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-21 下午06:56:54
	 */
	public boolean updateMttaskByTaskid(LfMttask mt) throws Exception
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		
		conditionMap.put("taskId", mt.getTaskId().toString());
		objectMap.put("sendstate", mt.getSendstate().toString());
		objectMap.put("errorCodes", mt.getErrorCodes());
		objectMap.put("sucCount", mt.getSucCount());
		objectMap.put("faiCount", mt.getFaiCount());
		
		return empDao.update(LfMttask.class, objectMap, conditionMap);
	}
	
	/**老平台彩信发送方法
	 * 发送彩信 分  存储过程 与 WEBSERVICE
	 * @param mtId
	 * @return
	 * @throws Exception 
	 * @throws Exception
	 */
//	public String sendMms(Long mtId) throws Exception {
//		/*if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
//			return this.sendMmsDB(mtId);
//		}else{*/
//			return this.sendMmsWebService(mtId);
//		//}
//	}

	public String sendMms(Long mtId) throws Exception
	{
		return sendMmsChild(mtId, 0);
	}
	/**新的彩信发送方法
	 * 发送彩信
	 * @param mtId
	 * @return
	 * @throws Exception
	 */
	private String sendMmsChild(Long mtId,int type) throws Exception{
	    //定义返回字符串
		String returnStr="提交彩信任务失败！";
		//获取彩信任务对象
		LfMttask lfMttask = empDao.findObjectByID(LfMttask.class, mtId);
		if(type == 1)
		{
			String wgresult = new BalanceLogBiz().checkGwFee(lfMttask.getSpUser(), Integer.parseInt(lfMttask.getIcount()), lfMttask.getCorpCode(), lfMttask.getMsType());
			if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || wgresult.indexOf("lessgwfee") > -1)
			{
				returnStr = wgresult;
				lfMttask.setErrorCodes(returnStr);
			}
		}
		lfMttask.setSendstate(2);
		
		//查找操作员
		LfSysuser lfSysuser=empDao.findObjectByID(LfSysuser.class, lfMttask.getUserId());
		
		//创建发送对象
		MmsWGParams mmsWGParams=new MmsWGParams();
		//设置发送账号
		mmsWGParams.setUserID(lfMttask.getSpUser());
		//LfMttask中bmtType字段 10普通彩信,11静态模板彩信,12动态模板彩信
		mmsWGParams.setMsgType(lfMttask.getBmtType());
		mmsWGParams.setTaskid(Integer.parseInt(String.valueOf(lfMttask.getTaskId())));
		//设置彩信标题
		mmsWGParams.setTitle(lfMttask.getTitle());
		mmsWGParams.setRemoteUrl(lfMttask.getFileuri()+lfMttask.getMobileUrl());
		mmsWGParams.setSendStatus(new Integer(1));
		if(lfMttask.getBmtType().intValue()==11||lfMttask.getBmtType().intValue()==12){
			//如果是模板彩信,则填模板ID
			mmsWGParams.setTmpID(Long.parseLong(lfMttask.getMsg()));
		}else{
		    //如果是普通彩信，则填彩信路径
			mmsWGParams.setMsg(lfMttask.getMsg());
		}
		mmsWGParams.setSvrType(" ");
		mmsWGParams.setP1(lfSysuser.getUserCode());
		mmsWGParams.setP2(" ");
		mmsWGParams.setP3(" ");
		mmsWGParams.setP4(" ");
		mmsWGParams.setModuleID(0);
		//调用发送接口进行发送
		DBMmsSend dbMmsSend=new DBMmsSend();
		returnStr=dbMmsSend.sendMms(mmsWGParams);
		//发送成功，则更新lfMttask的状态
		if(returnStr.equals("000")){
			lfMttask.setSendstate(1);
		}
		lfMttask.setSucCount(null);
		lfMttask.setFaiCount(null);
		lfMttask.setIcount(null);
		if (lfMttask.getTimerStatus().intValue() == 0) {
			lfMttask.setTimerTime(new Timestamp(Calendar.getInstance()
					.getTime().getTime()));
		}
		//更新lfMttask表
		empDao.update(lfMttask);
		return returnStr;
	}

	/**
	 * @description 发送之前处理 先修改发送状态为4，
	 * 即发送中... 修改成功表示该任务以前未发送，否则表示访任务已发送过
	 * @param mtId
	 * @return boolean
	 * @throws Exception
	 */
	public boolean sendpre(Long mtId) throws Exception 
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		objectMap.put("sendstate", "1");
		// 修改条件
		conditionMap.put("sendstate", String.valueOf(0));
		conditionMap.put("mtId", String.valueOf(mtId));
		boolean flag = empDao.update(LfMttask.class, objectMap, conditionMap);

		if(!flag)
		{
			//new TxtFileUtil().writeSendResult(mtId,"this sendstate is no 0");
			EmpExecutionContext.requestInfo(mtId,"this sendstate is no 0");
		}
		return flag;

	}
	
	
	/**
	 * 发送成功后更新状态
	 * @param mtId 
	 * @return 成功返回true
	 */
	public boolean sendAfter(Long mtId) {
		boolean flag = false;
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("sendstate", "3");
			// 修改条件
			conditionMap.put("mtId", String.valueOf(mtId));
			flag = empDao.update(LfMttask.class, objectMap, conditionMap);

			if(!flag)
			{
				//new TxtFileUtil().writeSendResult(mtId,"this sendAfter is fail");
				EmpExecutionContext.requestInfo(mtId,"this sendAfter is fail");
			}
			return flag;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e, "发送成功后更新状态异常。");
			return flag;
		}
		

	}
	
	/**
	 *  彩信WEBSERVICE发送
	 * @param mtId
	 * @return
	 * @throws Exception
	 */
//	private String sendMmsWebService(Long mtId) throws Exception {
//		String resultStr = null;
//		//获取彩信任务对象
//		LfMttask lfMttask = empDao.findObjectByID(LfMttask.class, mtId);
//		lfMttask.setSendstate(2);		
//		//发送失败
//		//设置条件
//		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//		//获取彩信发送账号
//		map.put("mmsUserId", lfMttask.getSpUser());
//		//获取其彩信发送账号信息
//		List<LfMmsAccManagement> accManage = empDao.findListByCondition(LfMmsAccManagement.class, map, null);
//		if(accManage != null && accManage.size() > 0){
//			LfMmsAccManagement accManagement = accManage.get(0);
//			map.clear();
//			//发送账号
//			map.put("mmsUser", lfMttask.getSpUser());
//			//企业编码
//			map.put("corpCode", lfMttask.getCorpCode());
//			//彩信账号绑定信息
//			List<LfMmsAccbind> accBind = empDao.findListByCondition(LfMmsAccbind.class, map, null);
//			if(accBind != null && accBind.size() > 0){
//				LfMmsAccbind acc = accBind.get(0);
//				//判断是否存在已经有效
//				if(acc != null && acc.getIsValidate() == 1){
//					//调用接口发送彩信信息
//					resultStr = this.mmsUserWebService(lfMttask, accManagement.getMmsUserId(), accManagement.getMmsPassword());
//				}else{
//					resultStr = "彩信发送账户绑定失效！";
//				}
//			}else{
//				resultStr = "获取彩信发送账户绑定数据失败！";
//			}
//		}else{
//			resultStr = "获取发送账号信息失败！";
//		}
//		lfMttask.setSucCount(null);
//		lfMttask.setFaiCount(null);
//		lfMttask.setIcount(null);
//		if (lfMttask.getTimerStatus().intValue() == 0) {
//			lfMttask.setTimerTime(new Timestamp(Calendar.getInstance()
//					.getTime().getTime()));
//		}
//		//保存 执行数据库操作
//		empDao.update(lfMttask);
//		return resultStr;
//	}
	/**
	 * 读取DB信息
	 * @param mtId
	 * @return
	 * @throws Exception
	 */
//	private String sendMmsDB(Long mtId) throws Exception {
//		String resultStr = "";
//		try{
//			LfMttask lfMttask = empDao.findObjectByID(LfMttask.class, mtId);
//			lfMttask.setSendstate(2);		//发送失败
//			lfMttask.setSucCount(null);
//			lfMttask.setFaiCount(null);
//			lfMttask.setIcount(null);
//			if (lfMttask.getTimerStatus().intValue() == 0) {
//				lfMttask.setTimerTime(new Timestamp(Calendar.getInstance()
//						.getTime().getTime()));
//			}
//			String base64Content = new TmsFile().tmsToBase64(lfMttask.getMsg());
//			boolean updateMsg = false;
//			if(base64Content == null){
//				lfMttask.setSendstate(0);
//				resultStr = "读取彩信文件出错。";
//			}else{
//				lfMttask.setSendstate(1);
//				updateMsg = true;
//			}
//			empDao.update(lfMttask);
//			if(updateMsg){
//				Db2SendMMSUtil  dbUtil = new Db2SendMMSUtil();
//				resultStr =  dbUtil.sendMmsTask(lfMttask);
//				if(!"000".equals(resultStr)){
//					lfMttask.setSendstate(2);
//					empDao.update(lfMttask);
//				}
//			}
//		}catch (Exception e) {
//			EmpExecutionContext.error(e);
//		}
//		return resultStr;
//	}
	
	/**
	 *   DB2处理
	 * @param mtId
	 * @return
	 * @throws Exception
	 */
//	public String sendMms_db2(Long mtId) throws Exception {
//
//		String resultStr = null;
//		LfMttask lfMttask = empDao.findObjectByID(LfMttask.class, mtId);		
//		//该条彩信任务
//		lfMttask.setSendstate(2);												
//		//发送失败
//		String base64Content = new TmsFile().tmsToBase64(lfMttask.getMsg());	
//		//彩信内容 TMS
//		if (base64Content != null) {
//			//Connection conn = empTransDao.getConnection();
//			Db2SendMMSUtil  dbUtil = new Db2SendMMSUtil();
//			dbUtil.sendMmsTask(lfMttask);
//			int sendResult = 0;
//			TxtFileUtil txtfileutil = new TxtFileUtil();
//			String mobiles = txtfileutil.readFileByLines(txtfileutil.getWebRoot()+ lfMttask.getMobileUrl());
//				
//				try {
//					List<LfMmsMttask> mmsMttaskList = new ArrayList<LfMmsMttask>();
//					Long userId = lfMttask.getUserId();
//					String msg = lfMttask.getMsg();
//					String[] phonesArray = mobiles.split(",");
//					LfMmsMttask mmsMttask = null;
//					for (int i = 0; i < phonesArray.length; i++) {
//						mmsMttask = new LfMmsMttask();
//						mmsMttask.setUserId(userId);
//						mmsMttask.setPhone(phonesArray[i]);
//						mmsMttask.setMmsMsg(msg);
//						mmsMttask.setSendTime(new Timestamp(Calendar
//								.getInstance().getTime().getTime()));
//						mmsMttask.setSendStatus(sendResult);
//						mmsMttaskList.add(mmsMttask);
//
//						if (mmsMttaskList.size() == 5000) {
//							empDao.save(mmsMttaskList, LfMmsMttask.class);
//							mmsMttaskList.clear();
//						}
//
//					}
//					empDao.save(mmsMttaskList, LfMmsMttask.class);
//
//					Calendar cal = Calendar.getInstance();
//					int month = cal.get(Calendar.MONTH) + 1;
//					String monthstr = month > 9 ? String.valueOf(month) : "0"
//							+ String.valueOf(month);
//					int day = cal.get(Calendar.DAY_OF_MONTH);
//					String daystr = day > 9 ? String.valueOf(day) : "0"
//							+ String.valueOf(day);
//					String iymd = String.valueOf(cal.get(Calendar.YEAR))
//							+ monthstr + daystr;
//					LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
//					condition.put("iymd", iymd);
//					List<LfMmsMtReport> list = empDao.findListByCondition(
//							LfMmsMtReport.class, condition, null);
//					if (list != null && list.size() > 0) {
//						LfMmsMtReport mmsMtReport = list.get(0);
//						mmsMtReport.setIcount(mmsMtReport.getIcount()
//								.longValue()
//								+ Long.valueOf(phonesArray.length));
//						if (sendResult == 0) {
//							mmsMtReport.setRfail1(mmsMtReport.getRfail1()
//									+ Long.valueOf(phonesArray.length));
//						} else if (sendResult == 1) {
//							mmsMtReport.setRsucc(mmsMtReport.getRsucc()
//									+ Long.valueOf(phonesArray.length));
//						}
//						empDao.update(mmsMtReport);
//					} else {
//						LfMmsMtReport mmsMtReport = new LfMmsMtReport();
//						mmsMtReport.setIcount(Long.valueOf(phonesArray.length));
//						mmsMtReport.setIymd(Long.valueOf(iymd));
//						mmsMtReport.setY(Long.valueOf(cal.get(Calendar.YEAR)));
//						mmsMtReport.setImonth(Long.valueOf(cal
//								.get(Calendar.MONTH) + 1));
//						if (sendResult == 0) {
//							mmsMtReport.setRfail1(Long
//									.valueOf(phonesArray.length));
//							mmsMtReport.setRfail2(0L);
//							mmsMtReport.setRsucc(0L);
//						} else if (sendResult == 1) {
//							mmsMtReport.setRfail1(0L);
//							mmsMtReport.setRfail2(0L);
//							mmsMtReport.setRsucc(Long
//									.valueOf(phonesArray.length));
//						}
//						empDao.save(mmsMtReport);
//					}
//
//				} catch (Exception e) {
//					throw e;
//				}
//		} else {
//			resultStr = "读取彩信文件出错。";
//		}
//
//		lfMttask.setSucCount(null);
//		lfMttask.setFaiCount(null);
//		lfMttask.setIcount(null);
//
//		if (lfMttask.getTimerStatus().intValue() == 0) {
//			lfMttask.setTimerTime(new Timestamp(Calendar.getInstance()
//					.getTime().getTime()));
//		}
//
//		empDao.update(lfMttask);
//		return resultStr;
//	}

	/**
	 * 如果用户禁用了，那它其下的到时的定时任务则变成未发送，已撤销
	 * @param lfMttask
	 * @return
	 */
	public boolean ChangeSendState(LfMttask lfMttask)
	{
		boolean flag = false;
		BalanceLogBiz b = new BalanceLogBiz();
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		
		try {
			empTransDao.beginTransaction(conn);
			//lfMttask.setSendstate(2);
			//变成已撤销
			lfMttask.setSubState(3);
			lfMttask.setSucCount(null);
			lfMttask.setFaiCount(null);
			if(b.IsChargings(lfMttask.getUserId()))
			{
				//如果发送状态不等于发送成功,则需要在此补回
				if(lfMttask.getMsType()==2)
				{
					b.sendMmsAmountByUserId(conn, lfMttask.getUserId(), lfMttask.getEffCount()*-1);
				}
				else
				{
					 b.sendSmsAmountByUserId(conn, lfMttask.getUserId(), Integer.parseInt(lfMttask.getIcount())*-1);
				}
			}
			flag = empTransDao.update(conn, lfMttask);
			if(flag)
			{
				empTransDao.commitTransaction(conn);
			}
			else
			{
				empTransDao.rollBackTransaction(conn);
			}
		} catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			flag = false;
			EmpExecutionContext.error(e,"用户禁用时，处理该用户未发送的短信任务异常！");
		}finally
		{
			if(conn !=null)
			{
				//关闭连接
				empTransDao.closeConnection(conn);
			}
		}
		return flag;
	}
}
