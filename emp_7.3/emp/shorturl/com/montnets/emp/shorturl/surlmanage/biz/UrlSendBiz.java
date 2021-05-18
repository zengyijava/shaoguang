package com.montnets.emp.shorturl.surlmanage.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.atom.UserdataAtom;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfReviewer2level;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.shorturl.comm.constant.Result;
import com.montnets.emp.shorturl.comm.constant.UrlGlobals;
import com.montnets.emp.shorturl.surlmanage.dao.UrlSendDao;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomain;
import com.montnets.emp.shorturl.surlmanage.entity.LfNeturl;
import com.montnets.emp.shorturl.surlmanage.entity.LfUrlTask;
import com.montnets.emp.shorturl.surlmanage.entity.Message;
import com.montnets.emp.shorturl.surlmanage.util.HttpClientHandler;
import com.montnets.emp.shorturl.surlmanage.util.TxtFileUtil;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.MD5;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class UrlSendBiz extends SuperBiz {

	private final UrlSendDao dao = new UrlSendDao();

	private final TxtFileUtil txtFileUtil = new TxtFileUtil();
	
	private final BaseBiz baseBiz = new BaseBiz();

	private final SmsSpecialDAO smsSpecialDAO = new SmsSpecialDAO();
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");

	// 写文件时候要的换行符
	String line = StaticValue.systemProperty
			.getProperty(StaticValue.LINE_SEPARATOR);

	public boolean runUrlTask(LfUrlTask task) {
		if (task == null) {
			EmpExecutionContext.error("短链接任务执行失败，任务对象为空");
			return false;
		}
		// 先获取任务状态，如果已经是处理中就结束任务
		try {
			Integer urlStatus = new BaseBiz().getById(LfUrlTask.class,
					task.getId()).getStatus();
			if (urlStatus == 11 || urlStatus == 12 || urlStatus == 21
					|| urlStatus == 22 || urlStatus == 13 || urlStatus == 23) {
				EmpExecutionContext.error("短链接任务已经在处理或处理完成，任务Id"
						+ task.getTaskId());
				return false;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取任务状态异常");
			UrlTimerManagerBiz.getTaskmap().remove(task.getId());
			return false;
		}
		// 更改短链接发送任务状态为处理中
		boolean updateBeforRes = updateStatusBeforeRun(task);
		if (updateBeforRes) {
			EmpExecutionContext.info("短链接发送任务处理，处理前更新数据库成功。任务Id："
					+ task.getTaskId());
		} else {
			EmpExecutionContext.error("短链接发送任务处理，处理前更新数据库失败。任务Id:"
					+ task.getTaskId());
			return false;
		}
		Result runRes = new Result();
		// writeTaskDetailToDb(task);
		runRes = manageUrlTask(task);
		boolean updateAfterRes = updateStatusAfterRun(task,runRes);
		if (updateAfterRes) {
			EmpExecutionContext.info("短链接发送任务处理，处理后更新数据库成功。任务Id："
					+ task.getTaskId());
		} else {
			EmpExecutionContext.error("短链接发送任务处理，处理后更新数据库失败。任务Id:"
					+ task.getTaskId());
			return false;
		}
		//任务执行完以后可以移除缓存
		UrlTimerManagerBiz.getTaskmap().remove(task.getId());
		
		return true;
	}

	private Result manageUrlTask(LfUrlTask task) {
		Result result = new Result();
		try {
			EmpExecutionContext.info("短链接发送任务处理，开始从文件服务器读取短链文件。任务Id："
					+ task.getTaskId());
			// 1.通过任务对象获取url文件地址
			String url = task.getUrlfile().replace("%20", " ");
			URL urls = new URL(url);
			// 获取文件输入流
			BufferedReader br = new BufferedReader(new InputStreamReader(urls
					.openConnection().getInputStream(), "GBK"));
			String temp = "";
			Map<String, String> smsMap = new HashMap<String, String>();
			int a = 0;
			Integer handleLine = task.getHandleLine();
			int index = 0;
			// 读取文件流
			while ((temp = br.readLine()) != null) {
				index++;
				if (index<=handleLine) {
					continue;
				}
				a++;
				// 获取手机号
				String phone = temp.substring(0, temp.indexOf(","));
				// 获取短信内容
				EmpExecutionContext.error("短链，读取_url文件解密前内容" + temp);
				String content = contentBase64Decoder(temp.substring(temp.indexOf(",")+1),"GBK");
				EmpExecutionContext.error("短链，读取_url文件解密后内容" + content);
				smsMap.put(phone, content);
				if (a >= 10000) {
					Result res = writeSmsFile(smsMap, task);
					if (!res.isSucc()) {
						result.setErrordesc(res.getErrordesc());
						return result;
					}
					a = 0;
					smsMap.clear();
				}
				
			}
			if (a > 0) {
				Result res = writeSmsFile(smsMap, task);
				if (!res.isSucc()) {
					result.setErrordesc(res.getErrordesc());
					return result;
				}
				a = 0;
				smsMap.clear();
			}
			//发送短信
			EmpExecutionContext.info("短链接发送任务处理，开始发送短链短信。任务Id："
					+ task.getTaskId());
			Result res2 = sendMessage(task);
			if (res2.isSucc()) {
				result.setSucc(true);
			} else {
			    result.setErrordesc(res2.getErrordesc());
            }
			EmpExecutionContext.info("短链接发送任务处理，结束发送短链短信。任务Id："
					+ task.getTaskId());
			smsMap =null;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "短链任务处理失败,任务Id:"+task.getTaskId());
		}
		return result;
	}

	// private boolean writeTaskDetailToDb(LfUrlTask task) {
	// if (task == null) {
	// EmpExecutionContext.error("短链接发送详情入库任务执行失败，任务对象为空");
	// return false;
	// }
	// // 1.通过任务对象获取文件地址
	// String filePath = task.getSrcFile();
	// if (StringUtils.isBlank(filePath)) {
	// EmpExecutionContext.error("短链接发送详情入库任务处理，文件路径为空。任务Id:"
	// + task.getTaskId());
	// return false;
	// }
	//
	// // 2.判断文件是否存在
	// File file = new File(filePath);
	// if (!file.exists()) {
	// EmpExecutionContext.error("短链接发送详情入库任务处理，文件不存在。任务Id:"
	// + task.getTaskId());
	// return false;
	// }
	// // 3.获取读取文件流
	// BufferedReader buffer = null;
	// try {
	// buffer = new BufferedReader(new InputStreamReader(
	// new FileInputStream(file)));
	//
	// // 4.通过任务对象获取读取的起始位置
	// Integer lineNum = task.getHandleLine();
	// if (lineNum == null || lineNum < 0) {
	// EmpExecutionContext.error("短链接发送详情入库任务处理，获取已读取行数异常。任务Id:"
	// + task.getTaskId());
	// }
	//
	//			
	// String str = null;
	// Integer count = 0;
	// List<LfUrlVisit> detailList = new ArrayList<LfUrlVisit>();
	// while ((str = buffer.readLine()) != null
	// && !("").equals(str.trim())) {
	// if (count < lineNum) {
	// count++;
	// continue;
	// }
	// //分割字符串
	// String[] details = str.split("\\|");
	// LfUrlVisit dtl = new LfUrlVisit();
	//				
	// if (StringUtils.isBlank(details[0])
	// || StringUtils.isBlank(details[1])
	// || StringUtils.isBlank(details[2])
	// || StringUtils.isBlank(details[3])
	// ) {
	// EmpExecutionContext.error("短链接发送详情入库任务处理，存在详情元素为空。任务Id:"
	// + task.getTaskId()+"详情："+str);
	// continue;
	// }
	// dtl.setPhone(details[0]);
	// dtl.setTaskId(Long.valueOf(details[1]));
	// dtl.setShortUrl(details[2]);
	// dtl.setSrcUrl(details[3]);
	// dtl.setCreateTm(task.getSubmittm());
	// dtl.setCorpCode(task.getCorpCode());
	// detailList.add(dtl);
	// count++;
	// if (count % 5000==0) {
	// // 5.每读固定行数做一次批处理
	// boolean res = dao.addDetailBatch(detailList,task);
	// //失败了修改状态为失败，并返回
	// if (!res) {
	// return false;
	// }
	// // 6.成功后修改handle_line字段
	// dao.updateHandleLine(task,lineNum+5000);
	// detailList.clear();
	// }
	// }
	//			
	// //处理剩余不足5000的详情数据
	// if (!detailList.isEmpty()) {
	// boolean res = dao.addDetailBatch(detailList,task);
	// //失败了修改状态为失败，并返回
	// if (!res) {
	// return false;
	// }
	// dao.updateHandleLine(task,detailList.size());
	// detailList.clear();
	// }
	// return true;
	// } catch (Exception e) {
	// EmpExecutionContext.error(e, "短链接发送详情入库任务处理，异常。任务Id:"
	// + task.getTaskId());
	// return false;
	// } finally {
	// if(null != buffer){
	// try {
	// buffer.close();
	// } catch (IOException e) {
	// EmpExecutionContext.error(e,"短链接发送详情入库任务处理，关闭输入流失败。");
	// }
	// }
	// }
	//		
	//
	// }

	private Result sendMessage(LfUrlTask task) {
		Result result = new Result();
		JSONObject jsonObject = JSON.parseObject(task.getRemark());
		long startTime = System.currentTimeMillis();
		Long lguserid = null;
		String errorDesc = "";
		LfMttask mttask = new LfMttask();

		// 操作内容
		String oprInfo = "";
		// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
		String sendType = jsonObject.getString("sendType");

		// 日志内容
		if(sendType != null && sendType.trim().length() > 0)
		{
			if("1".equals(sendType))
			{
				oprInfo = "相同内容群发";
			}
			else if("4".equals(sendType))
			{
				oprInfo = "客户群组群发";
				// 创建客户群组短信任务时,sendType都按1来处理
				sendType = "1";
			}
			else
			{
				oprInfo = "不同内容群发";
			}
		}
		else
		{
			EmpExecutionContext.error("短信任务发送获取参数异常，sendType:" + sendType+"，errCode："+IErrorCode.V10001);
			errorDesc = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
			result.setErrordesc(errorDesc);
			
			return result;
		}

		// 当前登录操作员id
		String strlguserid = task.getCreateUid().toString();
		// 当前登录企业
		String lgcorpcode = task.getCorpCode();
		// 提交类型
		String bmtType = jsonObject.getString("bmtType");
		// 发送账号
		String spUser = task.getSpUser();
		// 任务主题
		String title = task.getTitle();
		// 信息内容
		String msg = task.getMsg();
		// 业务编码
		String busCode = jsonObject.getString("busCode");
		// 是否定时
		String timerStatus = jsonObject.getString("timerStatus");
		// 定时时间
		String timerTime = jsonObject.getString("timerTime");
		// 提交状态(已提交 2，已撤销3)
		Integer subState = 2;
		// 是否需要回复
		String isReply = jsonObject.getString("isReply");
		// 优先级
		String priority = jsonObject.getString("priority");
		// 尾号
		String subno = jsonObject.getString("subNo");
		// 预览结果
		// 提交总数
		String hidSubCount = jsonObject.getString("hidSubCount");
		// 有效总数
		String hidEffCount = jsonObject.getString("hidEffCount");
		// 文件绝对路径
		String hidMobileUrl = task.getSrcfile().replace("_url", "");
		// 预发送条数
		String hidPreSendCount = jsonObject.getString("hidPreSendCount");
        //贴尾内容
        String tailcontents = jsonObject.getString("tailcontents");
       
        
        
		// 判断页面参数是否为空
		if(strlguserid == null || lgcorpcode == null || spUser == null 
				|| msg == null || timerStatus == null || hidSubCount == null 
				|| hidEffCount == null || hidMobileUrl == null || hidPreSendCount == null)
		{
			EmpExecutionContext.error(oprInfo + "，发送获取参数异常，" + "strlguserid:" + strlguserid 
										+ ";lgcorpcode:" + lgcorpcode 
										+ ";spUser:" + spUser 
										+ ";msg:" + msg 
										+ ";timerStatus:" + timerStatus 
										+ ";hidSubCount:" + hidSubCount 
										+ ";hidEffCount:" + hidEffCount 
										+ ";hidMobileUrl:" + hidMobileUrl 
										+ ";hidPreSendCount:" + hidPreSendCount
										+"，errCode："+IErrorCode.V10001);
			errorDesc = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.error(errorDesc);
			
			result.setErrordesc(errorDesc);
			return result;	
		}
		
		//登录操作员信息
		LfSysuser lfSysuser = null;
		try {
			lfSysuser = baseBiz.getLfSysuserByUserId(task.getCreateUid().longValue());
		} catch (Exception e1) {
			EmpExecutionContext.error(e1, "短链接短信发送，获取操作员信息异常");
		}
		//登录操作员信息为空
		if(lfSysuser == null)
		{
			EmpExecutionContext.error(oprInfo + "，从session获取登录操作员信息异常。lfSysuser为null，errCode："+IErrorCode.V10001);
			errorDesc = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.error(errorDesc);
			result.setErrordesc(errorDesc);
			return result;	
		}
		// 任务id
		String taskId =task.getTaskId().toString();
		try
		{
			//操作员、企业编码、SP账号检查
			boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, lgcorpcode, spUser, null);
			if(!checkFlag)
			{
				EmpExecutionContext.error(oprInfo + "，检查操作员、企业编码、发送账号不通过，，taskid:"+taskId
						+ "，corpCode:"+lgcorpcode
						+ "，userid："+strlguserid
						+ "，spUser："+spUser
						+ "，errCode:"+ IErrorCode.V10001);
				errorDesc = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
				EmpExecutionContext.error(errorDesc);
				result.setErrordesc(errorDesc);
				return result;	
			}
			//判断任务ID是否合法
			if(!BalanceLogBiz.checkNumber(taskId) || "".equals(taskId))
			{
				EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId格式非法，taskId:" + taskId 
											+ "strlguserid:" + strlguserid 
											+ "，lgcorpcode:" + lgcorpcode
											+ "，errCode："+IErrorCode.V10001);
				errorDesc = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
				result.setErrordesc(errorDesc);
				return result;	
			}
			else
			{
				//查询任务ID在是否在lf_mttask表已使用,false:存在；true:不存在
				if(!smsSpecialDAO.checkTaskIdNotUse(Long.parseLong(taskId.trim())))
				{
					EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId已使用:" + taskId 
												+ "strlguserid:" + strlguserid 
												+ "，lgcorpcode:" + lgcorpcode
												+ "，errCode："+IErrorCode.V10001);
					errorDesc = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
					result.setErrordesc(errorDesc);
					return result;	
				}
				//任务ID是否已在网关任务表存在,true:存在；false:不存在
				if(smsSpecialDAO.isExistTask(taskId))
				{
					EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId在网关任务表已存在，taskId:" + taskId 
												+ "，strlguserid:" + strlguserid 
												+ "，lgcorpcode:" + lgcorpcode
												+ "，errCode："+IErrorCode.V10001);
					errorDesc = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
					result.setErrordesc(errorDesc);
					return result;	
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, oprInfo + "taskId合法性校验异常 ，taskId:" + taskId 
												+ "strlguserid:" + strlguserid 
												+ ";lgcorpcode:" + lgcorpcode
												+ "，errCode："+IErrorCode.V10012);
			errorDesc = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10012);
			EmpExecutionContext.error(errorDesc);
			result.setErrordesc(errorDesc);
			return result;	
		}
		
		try
		{
			try
			{
				lguserid = Long.valueOf(strlguserid);
				
				// 非不同内容文件发送
				if(!"3".equals(sendType))
				{
					if(tailcontents != null){
						msg += tailcontents;
					}
				}
				// 初始化任务对象
				// 提交总数
				mttask.setSubCount(Long.valueOf(hidSubCount));
				// 有效总数
				mttask.setEffCount(Long.valueOf(hidEffCount));
				// 文件绝对路径
				mttask.setMobileUrl(hidMobileUrl);
				// 预发送条数
				mttask.setIcount(hidPreSendCount);
				// 任务主题
				mttask.setTitle(title);
				// sp账号
				mttask.setSpUser(spUser);
				// 提交类型
				mttask.setBmtType(Integer.valueOf(bmtType));
				// 是否定时
				Integer timerStatus1 = (timerStatus == null || "".equals(timerStatus)) ? 0 : Integer.valueOf(timerStatus);
				// 定时时间
				mttask.setTimerStatus(timerStatus1);
				// 根据发送类型判断 短信类型
				mttask.setMsgType(Integer.valueOf(sendType));
				//不同内容动态模块，设置消息编码为35
				
				mttask.setMsgedcodetype(35);
				
				// 信息类型：1－短信 2－彩信 31-短链
				mttask.setMsType(31);
				// 提交类型
				mttask.setSubState(subState);
				// 业务类型
				mttask.setBusCode(busCode);
				//中文•不能识别,转为英文字符
				msg = msg.replaceAll("•", "·").replace("¥", "￥");
				mttask.setMsg(msg);
				// 发送状态
				mttask.setSendstate(0);
				// 企业编码
				mttask.setCorpCode(lgcorpcode);
				// 发送优先级
				mttask.setSendLevel(Integer.valueOf(priority));
				// 是否需要回复
				mttask.setIsReply(Integer.valueOf(isReply));
				// 尾号
				mttask.setSubNo(subno);
				// 操作员id
				mttask.setUserId(lguserid);
				
				mttask.setTaskId(Long.valueOf(taskId));
				
				// 定时任务
				if(timerStatus1 == 1)
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					timerTime = timerTime + ":00";
					mttask.setTimerTime(new Timestamp(sdf.parse(timerTime).getTime()));
				}
				else
				{
					// 非定时任务
					mttask.setTimerTime(mttask.getSubmitTime());
				}

				UserdataAtom userdataAtom = new UserdataAtom();
				// 设置发送账户密码
				mttask.setSpPwd(userdataAtom.getUserPassWord(spUser));
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, strlguserid, lgcorpcode, "初始化任务对象失败", IErrorCode.V10011);
				throw new EMPException(IErrorCode.V10011, e);
			}

			try
			{
				// 获取发送信息等缓存数据（是否计费、是否审核、用户编码）
				Map<String, String> infoMap =null;
				infoMap=new CommonBiz().checkMapNull(infoMap, lguserid, lgcorpcode);
				// 创建短信任务、判断审核流、扣费、判断创建定时任务、调用网关发送
				errorDesc = new  SmsSendBiz().addSmsLfMttask(mttask, infoMap);
				if ("000".equals(errorDesc) || "timerSuccess".equals(errorDesc) || "createSuccess".equals(errorDesc)) {
					result.setSucc(true);
					result.setErrordesc(errorDesc);
				} else {
                    result.setSucc(false);
                    result.setErrordesc(errorDesc);
                }
				//日志信息
				StringBuffer opContent = new StringBuffer("开始：").append(sdf.format(startTime))
											.append("，耗时：").append((System.currentTimeMillis()-startTime))
											.append("ms，title：").append(title)
											.append("，taskid：").append(taskId)
											.append("，spUser:").append(spUser)
											.append("，提交总数：").append(hidSubCount)
											.append("，有效数：").append(hidEffCount)
											.append("，状态：").append(errorDesc);
				
				//操作员名称
				String userName = lfSysuser.getUserName();
				EmpExecutionContext.info(oprInfo, lgcorpcode, strlguserid, userName, opContent.toString(), "OTHER");
				// 结果
				String reultClong = errorDesc;
				// 根据错误编码从网关定义查找错误信息
				String langName = jsonObject.getString("langName");
				errorDesc = new WGStatus(langName).infoMap.get(errorDesc);
				// 如果返回状态网关中未定义，则重置为之前状态
				if(errorDesc == null)
				{
					errorDesc = reultClong;
				}
				
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, strlguserid, lgcorpcode, "创建短信任务失败", IErrorCode.V10012);
				throw new EMPException(IErrorCode.V10012, e);
			}
		}
		catch (Exception e)
		{
			ErrorCodeInfo errInfo = ErrorCodeInfo.getInstance();
			errorDesc = errInfo.getErrorInfo(e.getMessage());
			// 未知异常
			if(errorDesc == null)
			{
				errorDesc = errInfo.getErrorInfo(IErrorCode.V10015);
			}
			// 失败日志
			EmpExecutionContext.error(e, strlguserid, lgcorpcode, errorDesc, "");
		}
		result.setErrordesc(errorDesc);
		return result;	
	
	}

	private Result writeSmsFile(Map<String, String> smsMap, LfUrlTask task) {
		Result result = new Result();
		try {
			EmpExecutionContext.info("短链接发送任务处理，准备从短链服务中获取短链。任务Id："
					+ task.getTaskId());
			
			// 获取对应参数
			Message message = new Message();
			message.setUserid(task.getSpUser());
			UserdataAtom userdataAtom = new UserdataAtom();
			String pwd = userdataAtom.getUserPassWord(task.getSpUser());
			String timestramp = sdf.format(new Date());
			//密码加密
			pwd = MD5.getMD5Str(task.getSpUser().toUpperCase()+"00000000"+pwd+timestramp);
			message.setPwd(pwd);
			message.setTimestamp(timestramp);
			message.setLongaddr(task.getNetUrl());
			message.setSadomain(task.getDomainUrl());
			message.setCustid(task.getTaskId().toString());
			// 手机号
			message.setMobile(smsMap.keySet().toString().replace("[", "")
					.replace("]", ""));
			message.setValidday(task.getValidDays());
			//扩展字段 empnum,lid,utaskid
			String extra =contentBase64Encoder(contentBase64Encoder(UrlGlobals.getEMP_NUM())
					+","+contentBase64Encoder(task.getNetUrlId().toString())
					+","+contentBase64Encoder(task.getId().toString())
					);
				
				/* contentBase64Encoder(UrlGlobals.EMP_NUM)
			                 +","+contentBase64Encoder(task.getNetUrlId().toString())
			                 +","+contentBase64Encoder(task.getId().toString());*/
			
			message.setExdata(extra);
			// 业务类型
			JSONObject jo = JSON.parseObject(task.getRemark());
			message.setSvrtype(jo.getString("busCode"));

			// 调用网关api获取返回结果
			HttpClientHandler handler = HttpClientHandler.getInstance();
			String body = message.toString();
			// 报文头
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Connection", "keep-alive");
			headers.put("Content-Type", "text/json");
			// 短地址中心url地址
			String url = UrlGlobals.getSHORT_CENTER_URL()+"get_shorturl";
			EmpExecutionContext.info("短链接发送任务处理，开始调用短链服务中获取短链。任务Id："
					+ task.getTaskId()+"，发送报文为： "+body);
			
			String reJson = handler.execute(url, body, headers, null, null);
			EmpExecutionContext.info("短链接发送任务处理，结束调用短链服务中获取短链。任务Id："
					+ task.getTaskId());
			EmpExecutionContext.info("短链接发送任务处理，开始解析短链，进行短信内容拼接。任务Id："
					+ task.getTaskId());
			if ("-310099".equals(reJson)) {
				result.setSucc(false);
				result.setErrordesc("调用短地址接口失败");
				EmpExecutionContext.error("调用短地址接口失败,传递参数："+message.toString());
				return result;
			}
			JSONObject reObj = JSON.parseObject(reJson);
			Integer reStatus = reObj.getInteger("result");
			if (reStatus != 0) {
				result.setSucc(false);
				result.setErrordesc("调用短地址接口返回错误码：" + reStatus);
				EmpExecutionContext.error("调用短地址接口返回错误码：" + reStatus+"，传递参数："+message.toString());
				return result;
			}
			// 拼装发送文件内容
			//JSONObject sa = reObj.getJSONObject("sa");
			JSONArray data = reObj.getJSONArray("data");
			StringBuffer effContent = new StringBuffer();
			// 短信内容替换标识
			String rStr = task.getDomainUrl()
					+ getAsterist(task.getDomainExten().longValue());
			for (Object object : data) {
				JSONObject json = (JSONObject) object;
				// 获取手机号码
				String phone = json.getString("mobile");
				// 获取短链
				String surl = json.getString("surl");
				// 替换短信内容
				String content = smsMap.get(phone);
				if (StringUtils.isBlank(content)) {

					result.setSucc(false);
					result.setErrordesc("根据手机号码：" + phone + "获取短信内容为空，无法进行替换");
					return result;
				}
				content = content.replace(rStr, surl);
				// 获取短链流水号
				EmpExecutionContext.error("短链，提交短链中心.txt文件加密内容：" + contentBase64Encoder(content));
				String msgid = json.getString("msgid");
				effContent.append(phone).append(",").append(
						contentBase64Encoder(content)).append(",")
						.append(msgid).append(",").append(task.getNetUrlId())
						.append(line);

			}
			// 写入发送文件
			if (effContent.length()>0) {
				String filePath = txtFileUtil.getWebRoot()+task.getSrcfile().replace("_url", "");
				String fileDirPath = filePath.substring(0, filePath.lastIndexOf("/"));
				File fileDir = new File(fileDirPath);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				txtFileUtil.writeToTxtFile(filePath, effContent.toString());
				effContent.setLength(0);
			}
			EmpExecutionContext.info("短链接发送任务处理，结束解析短链，短信内容拼接。任务Id："
					+ task.getTaskId());
			// 更新lf_urltask的handleline
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objMap = new LinkedHashMap<String, String>();
			conditionMap.put("id", task.getId().toString());
			Integer newLine = task.getHandleLine()+smsMap.size();
			objMap.put("handleLine", newLine.toString());
			task.setHandleLine(newLine);
			baseBiz.update(LfUrlTask.class, objMap, conditionMap);
			result.setSucc(true);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取短地址写入文件异常");
			result.setErrordesc("获取短地址写入文件异常");
		}
		return result;
	}

	private String contentBase64Encoder(String msg) {
		try {
			// 短信内容BASE64编码
			String content = new BASE64Encoder().encode(msg.getBytes("GBK"));
			//String content = new BASE64Encoder().encode(msg.getBytes());
			// windows操作系统
			if (StaticValue.getServerSystemType() == 0) {
				// 去回车换行
				content = content.replace("\r\n", "");
			}
			// 其他系统
			else {
				// 去回车换行
				content = content.replace("\n", "");
			}
			return content;
		} catch (Exception e) {
			EmpExecutionContext
					.error(e, "不同内容动态模板发送，短信内容BASE64编码异常！msg:" + msg);
			return null;
		}
	}

	public String contentBase64Decoder(String msg)
	{
		String content = "";
		try
		{
			if(msg != null && msg.trim().length() > 0)
			{
				content = new String(new BASE64Decoder().decodeBuffer(msg));
			}
			return content;
		}
		catch (IOException e)
		{
			EmpExecutionContext.error(e, "短信内容BASE64解码异常！msg:"+msg);
			return null;
		}
	}

	public String contentBase64Decoder(String msg, String charsetName)
	{
		String content = "";
		try
		{
			if(msg != null && msg.trim().length() > 0)
			{
				content = new String(new BASE64Decoder().decodeBuffer(msg),charsetName);
			}
			return content;
		}
		catch (IOException e)
		{
			EmpExecutionContext.error(e, "短信内容BASE64解码异常！msg:"+msg);
			return null;
		}
	}

	private boolean updateStatusAfterRun(LfUrlTask task,Result res) {
		if (task == null) {
			EmpExecutionContext.error("短链接发送详情入库任务执行失败，任务对象为空");
			return false;
		}
		Connection conn = empTransDao.getConnection();
		try {
			if (conn == null || conn.isClosed()) {
				EmpExecutionContext.error("执行短链接发送详情入库任务，执行前更新，获取数据库连接异常");
				return false;
			}
			empTransDao.beginTransaction(conn);
			// 更新对象
			LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
			// LinkedHashMap<String, String> conditionMap = new
			// LinkedHashMap<String, String>();
			String suffix = res.isSucc()?"2":"3";
			String status = task.getStatus().toString().substring(0, 1) + suffix;
			objectMap.put("status", Integer.valueOf(status));
			Timestamp time = new Timestamp(new Date().getTime());
			objectMap.put("updatetm", time);
			objectMap.put("remark1", StringUtils.isBlank(res.getErrordesc())?" ":res.getErrordesc());
			if (res.isSucc()) {
				if("1".equals(task.getStatus().toString().substring(0, 1))){
					objectMap.put("sendtm", time);
				}else {
					objectMap.put("sendtm", task.getPlantime());
				}
				
			}
			boolean result = empTransDao.update(conn, LfUrlTask.class,
					objectMap, task.getId().toString(), null);
			if (!result) {
				EmpExecutionContext.error("执行短链接发送详情入库任务，执行前更新，操作失败");
				empTransDao.rollBackTransaction(conn);
				return false;
			}
			
			empTransDao.commitTransaction(conn);
			return true;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "执行短链接发送详情入库任务，执行前更新，操作失败");
			empTransDao.rollBackTransaction(conn);
			return false;
		}finally{
			empTransDao.closeConnection(conn);
		}
	}

	private boolean updateStatusBeforeRun(LfUrlTask task) {
		if (task == null) {
			EmpExecutionContext.error("短链接发送详情入库任务执行失败，任务对象为空");
			return false;
		}
		Connection conn = empTransDao.getConnection();
		try {
			if (conn == null || conn.isClosed()) {
				EmpExecutionContext.error("执行短链接发送详情入库任务，执行前更新，获取数据库连接异常");
				return false;
			}
			empTransDao.beginTransaction(conn);
			// 更新对象
			LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
			// LinkedHashMap<String, String> conditionMap = new
			// LinkedHashMap<String, String>();
 			String status = task.getStatus().toString().substring(0, 1) + "1";
			objectMap.put("status", Integer.valueOf(status));
			objectMap.put("updatetm", new Timestamp(new Date().getTime()));
			boolean result = empTransDao.update(conn, LfUrlTask.class,
					objectMap, task.getId().toString(), null);
			if (!result) {
				EmpExecutionContext.error("执行短链接发送详情入库任务，执行前更新，操作失败");
				empTransDao.rollBackTransaction(conn);
				return false;
			}
			empTransDao.commitTransaction(conn);
			return true;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "执行短链接发送详情入库任务，执行前更新，操作失败");
			empTransDao.rollBackTransaction(conn);
			return false;
		}finally{
			empTransDao.closeConnection(conn);
		}
	}

	public List<LfDomain> findDomainByCorpcode(String corp_code) {
		if (StringUtils.isBlank(corp_code)) {
			EmpExecutionContext.error("获取发送用的域名-企业编码为空");
			return null;
		}

		List<LfDomain> domainlist = new ArrayList<LfDomain>();
		try {
			domainlist = dao.findDomainByCorpcode(corp_code);
			if (domainlist != null && !domainlist.isEmpty()) {
				for (LfDomain lfDomain : domainlist) {
					lfDomain.setDomain(lfDomain.getDomain()
							+ getAsterist(lfDomain.getLenExten()));
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取发送用的域名异常");
		}
		return domainlist;
	}

	private String getAsterist(Long length) {

		if (length == null) {
			return "";
		}
		StringBuffer asterisk = new StringBuffer("/");
		for (long i = 0; i < length; i++) {
			asterisk.append("*");
		}
		return asterisk.toString();
	}

	public String send(HttpServletRequest request,Integer utype) {
		String result = "error";
		try {
			// 1.解析参数
			LfUrlTask urlTask = new LfUrlTask();
			// 任务主题
			String title = request.getParameter("taskname");
			// 信息内容
			String msg = request.getParameter("msg");
			// 有效总数
			String hidSubCount = request.getParameter("hidEffCount");
			// 任务id
			String taskId = request.getParameter("taskId");
			// 当前登录操作员id
			//String strlguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String strlguserid = SysuserUtil.strLguserid(request);

			// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
			String sendType = request.getParameter("sendType");
			// 详情文件相对路径
			String srcFileUrl = request.getParameter("hidDzUrl");
			// 企业编码
			String lgcorpcode = request.getParameter("lgcorpcode");
			// 原始地址Id
			String netUrlId = request.getParameter("netUrlId");
			// 原始地址
			String netUrl = request.getParameter("netUrl");
			// 域名ID
			String domainId = request.getParameter("domainId");
			// 有效期
			String vaildays = request.getParameter("vaildays");
			// 发送账号
			String spUser = request.getParameter("spUser");
			// 定时时间
			String timerTime = request.getParameter("timerTime");
			// 是否定时
			String timerStatus = request.getParameter("timerStatus");
			Timestamp time = new Timestamp(new Date().getTime());
			// 是否定时
			Integer timerStatus1 = (timerStatus == null || ""
					.equals(timerStatus)) ? 0 : Integer.valueOf(timerStatus);
			// 2.保存到LF_URLTASK对象中
			urlTask.setTitle(title);
			urlTask.setMsg(msg == null ? "" : msg);
			urlTask.setSubCount(Integer.valueOf(hidSubCount));
			urlTask.setSpUser(spUser);
			urlTask.setTaskId(Integer.valueOf(taskId));
			urlTask.setStrTaskId(taskId);
			urlTask.setCorpCode(lgcorpcode);
			urlTask.setCreateUid(Integer.valueOf(strlguserid));
			urlTask.setCreatetm(time);
			urlTask.setUpdateUid(Integer.valueOf(strlguserid));
			urlTask.setUpdatetm(time);
			urlTask.setUtype(utype);
			urlTask.setSrcfile(srcFileUrl);

			urlTask.setHandleLine(0);
			urlTask.setSubmittm(time);
			urlTask.setSendtm(time);
			urlTask.setUpdatetm(time);

			urlTask.setNetUrlId(Long.valueOf(netUrlId));
			netUrl = new BaseBiz().getById(LfNeturl.class,
					Long.valueOf(netUrlId)).getSrcurl();
			urlTask.setNetUrl(netUrl);
			LfDomain lfDomain = new BaseBiz().getById(LfDomain.class, domainId);
			urlTask.setDomainId(lfDomain.getId());
			urlTask.setDomainUrl(lfDomain.getDomain());
			urlTask.setDomainLen(lfDomain.getLenAll());
			urlTask.setDomainExten(lfDomain.getLenExten().intValue());
			urlTask.setValidDays(Integer.valueOf(vaildays));
			urlTask.setHandleNode(StaticValue.getServerNumber());
			// 定时任务
			if (timerStatus1 == 1) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				timerTime = timerTime + ":00";
				urlTask.setPlantime(new Timestamp(sdf.parse(timerTime)
						.getTime()));
				urlTask.setSendtm(urlTask.getPlantime());
				urlTask.setStatus(20);
			} else {
				// 非定时任务
				urlTask.setPlantime(urlTask.getSubmittm());
				urlTask.setStatus(10);
			}

			// json序列化LF_MTTASK所需数据
			JSONObject remark = new JSONObject();
			remark.put("sendType", request.getParameter("sendType"));
			// 提交类型
			remark.put("bmtType", request.getParameter("bmtType"));
			// 业务编码
			remark.put("busCode", request.getParameter("busCode"));
			// 是否定时
			remark.put("timerStatus", request.getParameter("timerStatus"));
			// 定时时间
			remark.put("timerTime", request.getParameter("timerTime"));
			remark.put("isReply", request.getParameter("isReply"));
			remark.put("priority", request.getParameter("priority"));
			remark.put("subNo", request.getParameter("subNo"));
			remark.put("hidSubCount", request.getParameter("hidSubCount"));
			remark.put("hidEffCount", request.getParameter("hidEffCount"));
			remark.put("hidPreSendCount", request
					.getParameter("hidPreSendCount"));
			remark.put("tailcontents", request.getParameter("tailcontents"));
			String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
			remark.put("langName", langName);
			urlTask.setRemark(remark.toJSONString());
			urlTask.setRemark1("");
			// 3.向文件服务器上传url.txt文件

			CommonBiz comBiz = new CommonBiz();
			// 上传文件到文件服务器
			urlTask.setUrlfile(comBiz.uploadFileToFileServer(srcFileUrl)
					+ srcFileUrl);

			// 4.保存LF_URLTASK对象到数据库，返回结果
			Long urlTaskID = new BaseBiz().addObjReturnId(urlTask);
			//change by denglj 20181102
			//String userId =request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userId = SysuserUtil.strLguserid(request);

			String count = request.getParameter("hidPreSendCount");
			ReviewBiz revBiz = new ReviewBiz();
			LfFlow flow=   revBiz.checkUserFlow(userId==null?0:Long.parseLong(userId), lgcorpcode, "1", count==null?0:Long.parseLong(count));
	        if(flow!=null){
                result = "createSuccess";
                //查找该审批流程是否为逐级审批
                if(isByStepReview(flow)) {

                    result = "waitingResult";
//                    //判断该userid对应机构是否有审核员
//                    LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
//                    boolean flag = isExistReviewer(lgcorpcode, lfSysuser.getDepId().toString());
//                    if(!flag) {
//                        result = "noReviewer";
//                    }
                }
	        }else{
	            if (timerStatus1 == 1) {
	                result = "timerSuccess";
	            }else{
	                result = "000";
	            }
	        }
		} catch (Exception e) {
			EmpExecutionContext.error(e, "短链接任务生成异常");
		}
		return result;
	}

    private boolean isExistReviewer(String corpCode, String deptId) {
        //获取该机构下是否有审核员
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("corpCode", corpCode);
        conditionMap.put("depId", deptId);
        conditionMap.put("isReviewer", "1"); //1表示是审核人
        List<LfSysuser> auditList = null;
        try {
            auditList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, e.getMessage());
            return false;
        }
        if(auditList != null && auditList.size() > 0) {
            return true;
        }
        return false;
    }


    private boolean isByStepReview(LfFlow lfFlow) {
	    if(lfFlow != null) {
	        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
	        conditionMap.put("FId", lfFlow.getFId().toString());
	        //直接判断一级审核
	        conditionMap.put("RLevel", "1");
	        LfReviewer2level lfReviewer2level = dao.checkFlowType(conditionMap);
	        if(lfReviewer2level != null) {
	            int rType = lfReviewer2level.getRType();
	            //5表示逐级审批
	            if(5 == rType) {
                    return true;
                }
            }
        }
        return false;
    }





	public static void main(String[] args) throws Exception {
		String content = new String(new BASE64Decoder().decodeBuffer("bWFvbWFvLmNvbS95NGRtR9/13/Xf9d/13/Xf9df2zvvO+877zvvO+877zvvO+9Gw"),"GBK");
		System.out.println(content);

	}
}
