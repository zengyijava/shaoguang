package com.montnets.emp.query.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections4.map.HashedMap;

import sun.misc.BASE64Encoder;

import com.montnets.emp.common.atom.UserdataAtom;
import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SendMessage;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.WGStatus;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;
/**
 * 处理下行实时记录补发的业务层
 * @author 
 *
 */
public class ResendMtRecordBiz  extends SuperBiz
{
	private final SmsSpecialDAO smsSpecialDAO = new SmsSpecialDAO();
	
	//格式化时间
	private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	/**
	 * 短信提交发送
	 * @param request
	 * @param response
	 * @return
	 */
    public String send(HttpServletRequest request, HttpServletResponse response,LfMttask oldLfMttask,String newTaskId,PreviewParams preParams,int preSendCount)
	{
		long startTime = System.currentTimeMillis();
		Long lguserid = null;
		String result = "";
		LfMttask mttask = new LfMttask();

		// 操作内容
		String oprInfo = "";
		// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
		String sendType = String.valueOf(oldLfMttask.getMsgType());

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
			result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.logRequestUrl(request, "后台请求");
			return result;
		}

		// 当前登录操作员id
		//String strlguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String strlguserid = SysuserUtil.strLguserid(request);

		// 当前登录企业
		String lgcorpcode = "";
		// 提交类型
		String bmtType = String.valueOf(oldLfMttask.getBmtType());
		// 发送账号
		String spUser = request.getParameter("spUser");
		String oldTitle=oldLfMttask.getTitle();
		if(oldTitle==null)
		{
			oldTitle="";
		}
		// 任务主题
		String title = oldTitle+"["+oldLfMttask.getTaskId()+"]"+"补发";
		// 信息内容
		String msg = oldLfMttask.getMsg();
		//如果是不同内容群发，则需要设置msg为空字符串
		if(msg==null&&"3".equals(sendType))
		{
			msg="";
		}
		// 业务编码
		String busCode = oldLfMttask.getBusCode();
		// 是否定时  补发设置为不定时
		String timerStatus = "0";
		// 定时时间  不定时，定时时间为空字符串
		String timerTime =  "";
		// 提交状态(已提交 2，已撤销3)
		Integer subState = 2;
		// 是否需要回复
		String isReply = "0";
		// 优先级
		String priority = String.valueOf(oldLfMttask.getSendLevel());
		// 尾号
		String subno = " ";
		// 预览结果
		// 提交总数
		String hidSubCount = String.valueOf(preParams.getSubCount());
		// 有效总数
		String hidEffCount = String.valueOf(preParams.getEffCount());
		// 文件绝对路径   
		String hidMobileUrl = preParams.getPhoneFilePath()[1];
		// 预发送条数
		String hidPreSendCount = String.valueOf(preSendCount);
//        //贴尾内容
//        String tailcontents = request.getParameter("tailcontents");
		
		//登录操作员信息
		LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		//登录操作员信息为空
		if(lfSysuser == null)
		{
			EmpExecutionContext.error(oprInfo + "，从session获取登录操作员信息异常。lfSysuser为null，errCode："+IErrorCode.V10001);
			result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.error(result);
			return result;
		}
		
		lgcorpcode=lfSysuser.getCorpCode();
		
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
			result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.error(result);
			return result;
		}
		
		// 任务id
		String taskId = newTaskId;
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
				result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
				EmpExecutionContext.error(result);
				return result;
			}
			//判断任务ID是否合法
			if(!BalanceLogBiz.checkNumber(taskId) || "".equals(taskId))
			{
				EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId格式非法，taskId:" + taskId 
											+ "strlguserid:" + strlguserid 
											+ "，lgcorpcode:" + lgcorpcode
											+ "，errCode："+IErrorCode.V10001);
				result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
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
					result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
					return result;
				}
				//任务ID是否已在网关任务表存在,true:存在；false:不存在
				if(smsSpecialDAO.isExistTask(taskId))
				{
					EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId在网关任务表已存在，taskId:" + taskId 
												+ "，strlguserid:" + strlguserid 
												+ "，lgcorpcode:" + lgcorpcode
												+ "，errCode："+IErrorCode.V10001);
					result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
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
			result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10012);
			EmpExecutionContext.error(result);
			return result;
		}
		
		try
		{
			try
			{
				lguserid = Long.valueOf(strlguserid);
				
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
				if("2".equals(sendType))
				{
					mttask.setMsgedcodetype(35);
				}
				// 信息类型：1－短信 2－彩信
				mttask.setMsType(oldLfMttask.getMsType());
				
				// 7-智能引擎下行业务
				if(oldLfMttask.getMsType().intValue()==7)
				{
					mttask.setTempid(oldLfMttask.getTempid());
				}
				
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
				result = addSmsLfMttask(mttask, infoMap);
				//日志信息
				StringBuffer opContent = new StringBuffer("开始：").append(sdf.format(startTime))
											.append("，耗时：").append((System.currentTimeMillis()-startTime))
											.append("ms，title：").append(title)
											.append("，taskid：").append(taskId)
											.append("，spUser:").append(spUser)
											.append("，提交总数：").append(hidSubCount)
											.append("，有效数：").append(hidEffCount)
											.append("，状态：").append(result);
				
				//操作员名称
				String userName = lfSysuser.getUserName();
				EmpExecutionContext.info(oprInfo, lgcorpcode, strlguserid, userName, opContent.toString(), "OTHER");
				// 结果
				String reultClong = result;
				String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
				// 根据错误编码从网关定义查找错误信息
				result = new WGStatus(langName).getInfoMap().get(result);
				// 如果返回状态网关中未定义，则重置为之前状态
				if(result == null)
				{
					result = reultClong;
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
			result = errInfo.getErrorInfo(e.getMessage());
			// 未知异常
			if(result == null)
			{
				result = errInfo.getErrorInfo(IErrorCode.V10015);
			}
			// 失败日志
			EmpExecutionContext.error(e, strlguserid, lgcorpcode, result, "");
		}
		return result;
	}
    
	/**
	 * 创建短信任务、判断审核流、扣费、判断创建定时任务、调用网关发送
	 * 
	 * @param mt
	 * @return
	 * @throws Exception
	 */
    public String addSmsLfMttask(LfMttask mt, Map<String, String> infoMap)
	{
		// 短信发送
		return addSmsLfMttaskResend(mt, infoMap);
	}
	
	/**
	 * 创建短信任务、判断审核流、扣费、判断创建定时任务、调用网关发送(addSmsLfMttask调用以及短信重发调用)
	 * 
	 * @param mt
	 *        短信对象
	 * @return
	 * @throws Exception
	 */
    public String addSmsLfMttaskResend(LfMttask mt, Map<String, String> infoMap)
	{
		// 返回结果
		String returnStr = "";
		// 拆分后的短信条数，用于计费
		int sendCount = Integer.parseInt(mt.getIcount());

		// 检查发送缓存信息，若为空，则重新获取。
		infoMap = new CommonBiz().checkMapNull(infoMap, mt.getUserId(), mt.getCorpCode());
		if(infoMap == null)
		{
			// 短信任务创建参数获取失败
			return ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.B20022);
		}
		// 默认无需审批:0
		mt.setReState(0);

		Long mtId = mt.getTaskId();

		BalanceLogBiz balanceBiz = new BalanceLogBiz();
		//使用集群，上传文件到文件服务器
		if(StaticValue.getISCLUSTER() == 1)
		{
			CommonBiz comBiz = new CommonBiz();
			// 上传文件到文件服务器
			mt.setFileuri(comBiz.uploadFileToFileServer(mt.getMobileUrl()));
		}
		else
		{
			
			mt.setFileuri(StaticValue.BASEURL);
		}
		Connection conn = null;
		try
		{
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);

			// 检查SP账号费用
			int spFeeResult = balanceBiz.checkSpUserFee(mt.getSpUser(), sendCount, 1);
			//0:成功,2:后附费账号 ,-2:余额不足,-3:不存在账号信息 
			if(spFeeResult < 0)
			{
				EmpExecutionContext.error("SP账号余额小于发送条数，taskid:" + mtId + ",corpCode:"+mt.getCorpCode()+",userId:"+mt.getUserId()
						+",sendCount:"+sendCount+",spUser:"+mt.getSpUser()+",spFeeResult:"+spFeeResult);
				return "spuserfee:" + spFeeResult;
			}
			
			// 机构扣费
			int depResult = balanceBiz.depKoufei(conn, mt, sendCount, infoMap);
			// -3：不存在机构信息 ，-2：余额不足 ，-1:扣费失败，0：成功
			if(depResult < 0)
			{
				EmpExecutionContext.error("机构扣费失败，taskid:" + mtId + ",corpCode:"+mt.getCorpCode()+",userId:"+mt.getUserId()
										+",sendCount:"+sendCount+",spUser:"+mt.getSpUser()+",msType"+mt.getMsType()+",depResult:"+depResult);
				return "depfee:" + depResult;
			}
			
			
			// 运营商扣费
			String wgResult = balanceBiz.wgKoufei(mt);
			if("feefail".equals(wgResult) || "nogwfee".equals(wgResult) || wgResult.indexOf("lessgwfee") > -1)
			{
				//如果开启机构计费，则补回机构费用
				if("true".equals(infoMap.get("feeFlag")))
				{
					// 机构回收
					int recResult = balanceBiz.sendSmsAmountByUserId(null, mt.getUserId(), sendCount * -1);
					if(recResult != 1)
					{
						EmpExecutionContext.error("补回机构费用失败，taskid:" + mtId + ",corpCode:"+mt.getCorpCode()+",userId:"+mt.getUserId());
					}
				}
				EmpExecutionContext.error("运营商扣费失败，返回状态：" + wgResult+"，taskid:" + mtId + ",corpCode:"+mt.getCorpCode()+",userId:"+mt.getUserId());
				return wgResult;
			}
			
			//如果不是定时发送，则发送时间为当前时间
			if (mt.getTimerStatus().intValue() == 0) {
				mt.setTimerTime(new Timestamp(Calendar.getInstance()
						.getTime().getTime()));
			}
			// 保存短信任务
			empTransDao.saveObjectReturnID(conn, mt);
			mt.setMtId(mtId);

			// 立即发送前先提交事务
			empTransDao.commitTransaction(conn);

			SendMessage sendMessage = new SendMessage();
			mt.setMtId(mtId);

			// 向网关发送请求
			returnStr = sendMessage.sendSms(mt, infoMap);

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "短信发送失败，taskid:" + mtId + ",corpCode:"+mt.getCorpCode()+",userId:"+mt.getUserId());
			empTransDao.rollBackTransaction(conn);
			// 运营商余额回收
			balanceBiz.huishouFee(sendCount, mt.getSpUser(), mt.getMsType());
			//如果开启机构计费，则补回机构费用
			if("true".equals(infoMap.get("feeFlag")))
			{
				// 机构回收
				int recResult = balanceBiz.sendSmsAmountByUserId(null, mt.getUserId(), sendCount * -1);
				if(recResult != 1)
				{
					EmpExecutionContext.error("补回机构费用失败，taskid:" + mtId + ",corpCode:"+mt.getCorpCode()+",userId:"+mt.getUserId());
				}
			}
			String desc = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.B20012);
			returnStr = desc;
		}
		finally
		{
			// 关闭连接
			empTransDao.closeConnection(conn);
		}
		return returnStr;
	}
    
    
    private final PhoneUtil phoneUtil = new PhoneUtil();
    
    // 写文件时候要的换行符
    private final String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
    
    private final BlackListAtom blackBiz = new BlackListAtom();
    
    private final CommonVariables cv = new CommonVariables();
    
    /**
     * 解析号码，检验号码合法，过滤黑名单、过滤重号
     *
     * @param readerList
     * @param params
     * @param haoduan
     * @param lguserid
     * @param corpCode
     * @throws Exception
     */
    public void parsePhone(HashSet<String> sendPhoneSet, PreviewParams params, String[] haoduan, String lguserid, String corpCode, String busCode, HttpServletRequest request) throws Exception {
        // 运营商有效号码数
        int[] oprValidPhone = params.getOprValidPhone();
        // 运营商标识。0:移动号码 ;1:联通号码;2:电信号码;3:国际号码
        int index = 0;
        //号码返回状态
        int resultStatus = 0;
        // 解析号码文件
        String mobile;
        // 每批次的有效号码数，在该批号码写入数据库后重置为默认值
        int perEffCount = 0;
        // 每批次的无效号码数，在该批号码写入数据库后重置为默认值
        int perBadCount = 0;

        TxtFileUtil txtFileUtil = new TxtFileUtil();
        //有效号码文件
        File perEffFile = null;
        //无效号码文件
        File perBadFile = null;
        //合法号码文件流
        FileOutputStream perEffOS = null;
        //无效号码文件流
        FileOutputStream perBadOS = null;
        //提交号码总数
        Integer subCount = 0;
        Object mobileObj=null;
        try {
            // 有效号码
            StringBuilder contentSb = new StringBuilder(64);
            // 无效号码
            StringBuilder badContentSb = new StringBuilder(64);
            Iterator iter=sendPhoneSet.iterator();
            while (iter.hasNext()) {
                // 如果上传号码大于设置的最大值就不允许发送
                if (params.getEffCount() > StaticValue.MAX_PHONE_NUM) {
                    EmpExecutionContext.error(MessageUtils.extractMessage("common", "common_sms_code1", request) + StaticValue.MAX_PHONE_NUM + "，corpCode:" + corpCode + "，userid：" + lguserid);
                    throw new EMPException(IErrorCode.A40000);
                }
                mobileObj=iter.next();
                if (mobileObj!= null) {
                    subCount++;
                    mobile=(String)mobileObj;
                    mobile = mobile.trim();
                    // 去掉号码中+86前缀
                    mobile = StringUtils.parseMobile(mobile);
                    // 检查号码合法性和号段
                    if ((index = phoneUtil.getPhoneType(mobile, haoduan)) < 0) {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(mobile).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else if ((resultStatus = phoneUtil.checkRepeat(mobile, params.getValidPhone())) != 0) {
                        // 1为重复号码
                        if (resultStatus == 1) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code3", request)).append(mobile).append(line);
                            params.setRepeatCount(params.getRepeatCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }
                        //-1为非法号码
                        else {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(mobile).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }
                    } else if (blackBiz.checkBlackList(corpCode, mobile, busCode)) {
                        // 检查是否是黑名单
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(mobile).append(line);
                        params.setBlackCount(params.getBlackCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else {
                        perEffCount++;
                        // 有效号码
                        contentSb.append(mobile).append(line);
                        params.setEffCount(params.getEffCount() + 1);
                        // 预览10个号码
                        if (params.getEffCount() < 11) {
                            if (mobile != null && !"".equals(mobile) && params.getIshidephone() == 0) {
                                mobile = cv.replacePhoneNumber(mobile);
                            }
                            params.setPreviewPhone(params.getPreviewPhone() + mobile + StaticValue.MSG_SPLIT_CHAR + index + ";");
                        }
                        // 累加运营商有效号码数(区分运营商)
                        oprValidPhone[index] += 1;
                    }

				
                    // 一千条存贮一次
                    if (perEffCount >= StaticValue.PER_PHONE_NUM) {
                        if (perEffFile == null) {
                            //有效号码文件
                            perEffFile = new File(params.getPhoneFilePath()[0]);
                            //判断文件是否存在，不存在就新建一个
                            if (!perEffFile.exists()) {

                                boolean state = perEffFile.createNewFile();
                                if (!state) {
                                    EmpExecutionContext.error("创建失败");
                                }
                            }
                        }
                        if (perEffOS == null) {
                            //合法号码文件输出流
                            perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                        }
                        //params.setSubCount(params.getSubCount()+perEffCount);
                        //写入有效号码文件
                        //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                        txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                        contentSb.setLength(0);
                        perEffCount = 0;
                    }
                    if (perBadCount >= StaticValue.PER_PHONE_NUM) {
                        if (perBadFile == null) {
                            //非法号码文件
                            perBadFile = new File(params.getPhoneFilePath()[2]);
                            //判断文件是否存在，不存在就新建一个
                            if (!perBadFile.exists()) {
                                boolean state = perBadFile.createNewFile();
                                if (!state) {
                                    EmpExecutionContext.error("创建失败");
                                }
                            }
                        }
                        if (perBadOS == null) {
                            //非法号码文件输出流
                            perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                        }
                        //写入非法号码写文件
                        //params.setSubCount(params.getBadCount() + perBadCount);
                        //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
                        txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                        badContentSb.setLength(0);
                        perBadCount = 0;
                    }
                }
            }
            //设置提交总数
            params.setSubCount(subCount);
            // 设置各运营商有效号码数
            params.setOprValidPhone(oprValidPhone);

            if (contentSb.length() > 0) {
                // 剩余的有效号码写文件
                if (perEffFile == null) {
                    //有效号码文件
                    perEffFile = new File(params.getPhoneFilePath()[0]);
                    //判断文件是否存在，不存在就新建一个
                    if (!perEffFile.exists()) {
                        boolean state = perEffFile.createNewFile();
                        if (!state) {
                            EmpExecutionContext.error("创建失败");
                        }
                    }
                }
                if (perEffOS == null) {
                    //合法号码文件输出流
                    perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                }
                //写入有效号码文件
                //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                contentSb.setLength(0);
            }

            if (badContentSb.length() > 0) {
                // 剩余的非法号码写文件
                if (perBadFile == null) {
                    //非法号码文件
                    perBadFile = new File(params.getPhoneFilePath()[2]);
                    //判断文件是否存在，不存在就新建一个
                    if (!perBadFile.exists()) {
                        boolean state = perBadFile.createNewFile();
                        if (!state) {
                            EmpExecutionContext.error("创建失败");
                        }
                    }
                }
                if (perBadOS == null) {
                    //非法号码文件输出流
                    perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                }
                //写入非法号码写文件
                //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
                txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                badContentSb.setLength(0);
            }

        } catch (EMPException e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw e;
        } catch (Exception e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw new EMPException(IErrorCode.B20005, e);
        } finally {
            if (perEffOS != null) {
                try {
                    perEffOS.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "相同内容发送，解析文本文件流关闭有效号码文件输入流异常，lguserid:" + lguserid + "，corpCode:" + corpCode);
                }
            }
            if (perBadOS != null) {
                try {
                    perBadOS.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "相同内容发送，解析文本文件流关闭无效号码文件输入流异常，lguserid:" + lguserid + "，corpCode:" + corpCode);
                }
            }
            // 执行删除临时文件的操作
            cleanTempFile(params);
        }
    }
    
    /**
     * 删除临时文件
     *
     * @param params 传递参数类
     */
    private void cleanTempFile(PreviewParams params) {
        try {
            // 获取待删除的文件url集合
            List<String> delFileList = params.getDelFilePath();
            if (delFileList != null && delFileList.size() > 0) {
                for (String fileUrl : delFileList) {
                    File file = new File(fileUrl);
                    // 判断文件是否存在
                    if (file.exists()) {
                        boolean state = file.delete();
                        if (!state) {
                            EmpExecutionContext.error("删除失败");
                        }
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "删除临时文件异常");
        }
    }
    
    /**
     * 删除临时文件
     *
     * @param params 传递参数类
     */
    private void cleanTempFileB(PreviewParams params) {
        try {
            // 获取待删除的文件url集合
            List<String> delFileList = params.getDelFilePath();
            if (delFileList != null && delFileList.size() > 0) {
                for (String fileUrl : delFileList) {
                    File file = new File(fileUrl);
                    // 判断文件是否存在
                    if (file.exists()) {
                        boolean state = file.delete();
                        if (!state) {
                            EmpExecutionContext.error("删除失败");
                        }
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "删除临时文件异常");
        }
    }
    
    
    private final SmsBiz smsBiz = new SmsBiz();
    
    private final TxtFileUtil txtFileUtil = new TxtFileUtil();
    
    private final SuperDAO superDAO=new SuperDAO();
    
    /**
     * 不同内容群发时，解析号码，检验号码合法，过滤黑名单、过滤重号、过滤关键字(支持国外通道英文短信)
     *
     * @param readerList
     * @param params
     * @param haoduan
     * @param lguserid
     * @param corpCode
     * @param busCode
     * @param dtMsg
     * @throws Exception
     */
    public void parsePhoneAndContent(List<String> phoneAndContentList, PreviewParams params, String[] haoduan, String lguserid, String corpCode, String busCode, String dtMsg, String spUser, HttpServletRequest request) throws Exception {
        // 解析号码文件
        String tmp;
        // 短信内容
        String smsContent = "";
        // Base64编码后的短信内容
        String smsContentBase64 = "";
        //文件参数内容
        String fileContent = "";
        String mobile = "";
        // 文件内参数格式，用于动态模板发送时计算文件内参数个数与模板参数个数比较
        int paramCount = 0;

        // 每批次的有效号码数，在该批号码写入数据库后重置为默认值
        int perEffCount = 0;
        // 每批次的无效号码数，在该批号码写入数据库后重置为默认值
        int perBadCount = 0;
        KeyWordAtom keyWordAtom = new KeyWordAtom();
        StringBuilder viewContentSb = new StringBuilder(64);
        int sendType = params.getSendType();
        //有效号码文件
        File perEffFile = null;
        //无效号码文件
        File perBadFile = null;
        //合法号码文件流
        FileOutputStream perEffOS = null;
        //无效号码文件流
        FileOutputStream perBadOS = null;
        //国外通道信息
        DynaBean spGate = smsBiz.getInterSpGateInfo(spUser);
        // 获取1到n-1条英文短信内容的长度
        int longSmsFirstLen = 0;
        // 单条短信字数
        int singlelen = 0;
        //英文短信签名长度
        int signLen = 0;
        //是否支持英文短信，1：支持；0：不支持
        int gateprivilege = 0;
        if (spGate != null) {
            // 获取1到n-1条英文短信内容的长度
            longSmsFirstLen = Integer.parseInt(spGate.get("enmultilen1").toString());
            // 单条短信字数
            singlelen = Integer.parseInt(spGate.get("ensinglelen").toString());
            //英文短信签名长度
            signLen = Integer.parseInt(spGate.get("ensignlen").toString());
            // 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
            if (signLen == 0) {
                //国外通道英文短信签名长度
                signLen = smsBiz.getenSmsSignLen(spGate.get("ensignstr").toString().trim());
            }
            //是否支持英文短信，1：支持；0：不支持
            gateprivilege = Integer.parseInt(spGate.get("gateprivilege").toString());
            if ((gateprivilege & 2) == 2) {
                gateprivilege = 1;
            }

            //签名前置
            if ((gateprivilege & 4) == 4) {
                longSmsFirstLen = longSmsFirstLen - signLen;
            }
        }
        //号码类型
        int phoneType = 0;
        //0:英文短信;1:中文短信
        String SmsCharType = "1";
        //短信长度
        int smsLen = 0;
        //下标0(0:英文短信;1:中文短信);下标1(处理后的短信内容);下标2(英文短信长度)
        String[] smsContentInfo = {"1", "", "0"};
        try {
            // 有效号码
            StringBuilder contentSb = new StringBuilder(128);
            // 无效号码
            StringBuilder badContentSb = new StringBuilder(128);
            for (int r = 0; r < phoneAndContentList.size(); r++) {
                // 如果上传号码大于500w就不允许发送
                if (params.getEffCount() > StaticValue.MAX_PHONE_NUM) {
                    EmpExecutionContext.error("不同内容预览，有效号码数超过" + StaticValue.MAX_PHONE_NUM + "，corpCode:" + corpCode + "，userid：" + lguserid);
                    throw new EMPException(IErrorCode.A40000);
                }
                tmp = phoneAndContentList.get(r);
                if (tmp != null) {
                    params.setSubCount(params.getSubCount() + 1);
                    tmp = tmp.trim();
                    int index = tmp.indexOf(",");
                    if (index != -1) {
                        mobile = tmp.substring(0, tmp.indexOf(",")).trim();
                        // 去掉号码中+86前缀
                        mobile = StringUtils.parseMobile(mobile);
                    } else {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code5", request)).append(tmp).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                        continue;
                    }
                    // 检查号码合法性和号段
                    if ((phoneType = phoneUtil.getPhoneType(mobile, haoduan)) < 0) {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code6", request)).append(tmp).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    }
                    else if (blackBiz.checkBlackList(corpCode, mobile, busCode)) {
                        // 检查是否是黑名单
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(tmp).append(line);
                        params.setBlackCount(params.getBlackCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else {
                        // 过滤内容部分
                        smsContent = tmp.substring(index + 1).trim();
                        // 内容长度为零时或为","
                        if (smsContent.length() == 0 || ",".equals(smsContent)) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code8", request)).append(tmp).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                            // 跳转下一循环
                            continue;
                        }
                        //下标0(0:英文短信;1:中文短信);下标1(处理后的短信内容);下标2(短信长度)
                        smsContentInfo = smsBiz.getSmsContentInfo(smsContent, longSmsFirstLen, singlelen, signLen, gateprivilege);
                        //处理后的短信内容
                        smsContent = smsContentInfo[1];
                        if (smsContent.length() > StaticValue.MAX_MSG_LEN || smsContent.length() == 0) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code10", request) + smsContent.length() + "）(" + MessageUtils.extractMessage("common", "common_sms_code11", request) + "1-" + StaticValue.MAX_MSG_LEN + MessageUtils.extractMessage("common", "common_sms_code12", request) + ")：").append(tmp).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                            // 跳转下一循环
                            continue;
                        }

                        //国际号码并且存在国外通道
                        if (phoneType == 3 && spGate != null) {
                            //0:英文短信;1:中文短信
                            SmsCharType = smsContentInfo[0];
                            //英文短信长度
                            smsLen = Integer.valueOf(smsContentInfo[2]);
                            //英文短信大于700字或中文短信大于350字
                            if (("0".equals(SmsCharType) && smsLen > (720 - 20)) || ("1".equals(SmsCharType) && smsContent.length() > (360 - 10))) {
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code10", request) + smsContent.length() + "，" + MessageUtils.extractMessage("common", "common_sms_code15", request) + ")：").append(tmp).append(line);
                                params.setBadModeCount(params.getBadModeCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                perBadCount++;
                                // 跳转下一循环
                                continue;
                            }
                        }

                        int filterKeyRes = keyWordAtom.filterKeyWord(smsContent.toUpperCase(), corpCode);
                        // 过滤内容关键字
                        if (filterKeyRes == 1) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code13", request)).append(tmp).append(line);
                            params.setKwCount(params.getKwCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                            // 跳转下一循环
                            continue;
                        } else if (filterKeyRes == -1) {
                            EmpExecutionContext.error("不同内容预览，过滤关键字异常，corpCode：" + corpCode + "，smsContent：" + smsContent.toUpperCase());
                            throw new EMPException(ErrorCodeInfo.B20014);
                        }

                        // 动态模板发送时，对短信内容进行BASE64编码
                        if (sendType == 2) {
                            // 短信内容BASE64编码
                            smsContentBase64 = contentBase64Encoder(smsContent);
                            // 短信内容BASE64编码异常
                            if (smsContentBase64 == null) {
                                // 动态模板提交时，如果动态内容拼接处理异常
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code16", request)).append(tmp).append(line);
                                params.setBadModeCount(params.getBadModeCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                perBadCount++;
                                continue;
                            }
                            // 有效号码
                            contentSb.append(mobile + "," + smsContentBase64).append(line);
                        } else {
                            // 有效号码
                            contentSb.append(mobile + "," + smsContent).append(line);
                        }
                        params.setEffCount(params.getEffCount() + 1);
                        perEffCount++;
                        // 预览10个号码
                        if (params.getEffCount() < 11) {
                            if (sendType == 2) {
                                // 预览信息
                                viewContentSb.append(String.valueOf(fileContent) + "MWHS]#" + String.valueOf(paramCount) + "MWHS]#");
                                viewContentSb.append(mobile + "MWHS]#" + smsContent.replace("\r\n", " ")).append(line);
                            } else {
                                viewContentSb.append(mobile + "MWHS]#" + smsContent).append(line);
                            }
                        }
                    }

                    // 一千条存贮一次
                    if (perEffCount >= StaticValue.PER_PHONE_NUM) {
                        if (perEffFile == null) {
                            //有效号码文件
                            perEffFile = new File(params.getPhoneFilePath()[0]);
                            //判断文件是否存在，不存在就新建一个
                            if (!perEffFile.exists()) {
                                boolean state = perEffFile.createNewFile();
                                if (!state) {
                                    EmpExecutionContext.error("创建失败");
                                }
                            }
                        }
                        if (perEffOS == null) {
                            //合法号码文件输出流
                            perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                        }
                        //写入有效号码文件
                        txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                        //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                        contentSb.setLength(0);
                        perEffCount = 0;
                    }
                    if (perBadCount >= StaticValue.PER_PHONE_NUM) {
                        if (perBadFile == null) {
                            //非法号码文件
                            perBadFile = new File(params.getPhoneFilePath()[2]);
                            //判断文件是否存在，不存在就新建一个
                            if (!perBadFile.exists()) {
                                boolean state = perBadFile.createNewFile();
                                if (!state) {
                                    EmpExecutionContext.error("创建失败");
                                }
                            }
                        }
                        if (perBadOS == null) {
                            //非法号码文件输出流
                            perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                        }
                        //写入非法号码写文件
                        txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                        //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
                        badContentSb.setLength(0);
                        perBadCount = 0;
                    }
                }
            }

            if (contentSb.length() > 0) {
                if (perEffFile == null) {
                    //有效号码文件
                    perEffFile = new File(params.getPhoneFilePath()[0]);
                    //判断文件是否存在，不存在就新建一个
                    if (!perEffFile.exists()) {
                        boolean state = perEffFile.createNewFile();
                        if (!state) {
                            EmpExecutionContext.error("创建失败");
                        }

                    }
                }
                if (perEffOS == null) {
                    //合法号码文件输出流
                    perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                }
                // 剩余的有效号码写文件
                txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                contentSb.setLength(0);
            }

            if (badContentSb.length() > 0) {
                if (perBadFile == null) {
                    //非法号码文件
                    perBadFile = new File(params.getPhoneFilePath()[2]);
                    //判断文件是否存在，不存在就新建一个
                    if (!perBadFile.exists()) {
                        boolean state = perBadFile.createNewFile();
                        if (!state) {
                            EmpExecutionContext.error("创建失败");
                        }

                    }
                }
                if (perBadOS == null) {
                    //非法号码文件输出流
                    perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                }
                // 剩余的有效号码写文件
                txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
                badContentSb.setLength(0);
            }

            if (viewContentSb.length() > 0) {
                // 将预览信息写入文件
                txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[3], viewContentSb.toString());
                viewContentSb.setLength(0);
            }
        } catch (EMPException e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw e;
        } catch (Exception e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw new EMPException(IErrorCode.B20005, e);
        } finally {
            if (perEffOS != null) {
                try {
                    perEffOS.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "不同内容发送，解析文本文件流关闭无效号码文件输入流异常，lguserid:" + lguserid + "，corpCode:" + corpCode);
                }
            }
            if (perBadOS != null) {
                try {
                    perBadOS.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "不同内容发送，解析文本文件流关闭无效号码文件输入流异常，lguserid:" + lguserid + "，corpCode:" + corpCode);
                }
            }
            // 执行删除临时文件的操作
            cleanTempFile(params);
        }
    }
    
    /**
     * 处理带模板的短信发送(不进行换行处理)
     *
     * @param info 参数内容
     * @param msg  动态模板内容
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-1-18 下午05:41:44
     */
    private String combineContent(String info, String msg) {
        String content = msg;
        try {
            String params[] = info.split(",");
            int size = info.length() - info.replace(",", "").length() + 1;
            StringBuffer mid = null;
            for (int i = 1; i < size + 1; i++) {
                mid = new StringBuffer();
                mid.append("#P_").append(i).append("#");
                if (i < params.length + 1) {
                    content = content.replace(mid.toString(), params[i - 1]);
                    content = content.replace(mid.toString().replace("#P_", "#p_"), params[i - 1]);
                }
            }
            return content.replaceAll(StaticValue.EXECL_SPLID, ",");
        } catch (Exception e) {
            EmpExecutionContext.error(e, "不同内容动态模板发送，处理短信内容异常！info:" + info + "，msg:" + msg);
            return null;
        }
    }
    
    /**
     * 短信内容BASE64编码
     *
     * @param msg 短信内容
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-1-18 下午07:30:15
     */
    private String contentBase64Encoder(String msg) {
        try {
            // 短信内容BASE64编码
            String content = new BASE64Encoder().encode(msg.getBytes("GBK"));
            //windows操作系统
            if (StaticValue.getServerSystemType() == 0) {
                // 去回车换行
                content = content.replace("\r\n", "");
            }
            //其他系统
            else {
                // 去回车换行
                content = content.replace("\n", "");
            }
            return content;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "不同内容动态模板发送，短信内容BASE64编码异常！msg:" + msg);
            return null;
        }
    }
    
    /**
     * 处理平台流水号，滤重
     * @param ptmsgIds
     * @return
     */
    public String handlePtmsgIds(String ptmsgIds)
    {
    	try 
    	{
            if(ptmsgIds==null||"".equals(ptmsgIds.trim()))
            {
            	EmpExecutionContext.error("下行记录补发，传入的平台流水号为空！ptmsgIds="+ptmsgIds);
            	return "error";
            }
            String[] msgIdArr=ptmsgIds.trim().split(",");
            if(msgIdArr==null||msgIdArr.length==0)
            {
            	EmpExecutionContext.error("下行记录补发，传入的平台流水号存在问题！ptmsgIds="+ptmsgIds);
            	return "error";
            }
            HashSet<Long> validMsgId = new HashSet<Long>();
            for (int i = 0; i < msgIdArr.length; i++) 
            {
            	validMsgId.add(Long.parseLong(msgIdArr[i]));
			}
            Object[] msgIdObjArr=validMsgId.toArray();
            ptmsgIds="";
            for (int i = 0; i < msgIdObjArr.length; i++) 
            {
            	ptmsgIds+=String.valueOf(msgIdObjArr[i])+",";
			}
            //去掉最后一个逗号
            ptmsgIds=ptmsgIds.substring(0, ptmsgIds.length()-1);
            return ptmsgIds;
		} catch (Exception e)
    	{
			EmpExecutionContext.error(e, "处理平台流水号失败！");
			return "error";
		}
    }
    
    /**
     * 查询拆分后的第一条短信
     * @param ptmsgids
     * @return
     */
    public List<DynaBean> findMtTaskByPtmsgids(String ptmsgids)
    {
    	try 
    	{
    		String fieldSql = "select mttask.ID,mttask.USERID,mttask.SPGATE,mttask.CPNO,mttask.PHONE," +
    				"mttask.TASKID,mttask.SENDSTATUS,mttask.ERRORCODE,mttask.SENDTIME,mttask.MESSAGE," +
    				"mttask.UNICOM,mttask.RECVTIME,mttask.SVRTYPE,mttask.PKNUMBER,mttask.PKTOTAL," +
    				"mttask.MSGFMT,mttask.P1,mttask.P2,mttask.usermsgid,mttask.ptmsgid,mttask.custid ";
    	    String sql=fieldSql+" from gw_mt_task_bak mttask where mttask.ptmsgid in ("+ptmsgids+") order by mttask.ID desc";
    	    List<DynaBean> mttaskList= new SuperDAO().getListDynaBeanBySql(sql);
    	    return mttaskList;
		} catch (Exception e) 
    	{
			EmpExecutionContext.error(e, "补发时查询短信下行记录失败！");
			return null;
		}
    }
    
    /**
     * 根据长短信第一条查询长短信所有短信拼接在一起
     * @param mttaskList
     * @return List<String>  手机号,短信内容
     */
    public List<String> findPhoneAndContentByFirstContent(List<DynaBean> mttaskList)
    {
    	List<String> phoneAndContentList=new ArrayList<String>();
    	try 
    	{
    		GtPortUsed gtPortUsed=null;
    		String signStr="";
    		String enSignStr="";
    		String finalMessage=null;
    		
    		//获取SP账号绑定的通道路由
    		String userid=mttaskList.get(0).get("userid").toString();
    		//key:运营商  0:移动;1:联通;21:电信;5:国际  value:绑定的路由
    		Map<Integer,GtPortUsed> gtPortUsedMap=getGtPortUsed(userid);
			if(gtPortUsedMap==null||gtPortUsedMap.size()==0)
			{
				EmpExecutionContext.error("下行记录补发，通过SP账号获取SP账号绑定的路由失败！spuser="+userid);
				return null;
			}
    		
			
			long ptmsgid=0L;
    		int pktotal=0;
    		String phone=null;
    		Integer spisuncm=null;
			for (int i = 0; i < mttaskList.size(); i++) 
			{
				ptmsgid=Long.parseLong(mttaskList.get(i).get("ptmsgid").toString());
				pktotal=Integer.parseInt(mttaskList.get(i).get("pktotal").toString());
				phone=mttaskList.get(i).get("phone").toString();
				spisuncm=Integer.parseInt(mttaskList.get(i).get("unicom").toString());
				
				//通过长短信第一条，查询出此长短信拆分后的短信
				String message=getMessageByFirst(ptmsgid, pktotal);
				if(message==null)
				{
					EmpExecutionContext.error("下行记录补发，根据第一条短信查找拼接长短信的所有拆分短信失败！ptmsgid="+ptmsgid+",pktotal="+pktotal);
					return null;
				}
				
				//去掉签名
				//通过运营商类型获取路由
				gtPortUsed=gtPortUsedMap.get(spisuncm);
				//获取中文签名
				signStr=gtPortUsed.getSignstr();
				//不是国际通道
				if(spisuncm.intValue()!=5)
				{
					//去掉短信的签名
					finalMessage=handleMessage(message, signStr);
				}else   //国际通道
				{
					//英文签名
					enSignStr=gtPortUsed.getEnsignstr();
					//去掉国际短信的签名
					finalMessage=handleGJMessage(message, signStr, enSignStr);
				}
				if(finalMessage==null)
				{
					EmpExecutionContext.error("下行记录补发，补发内容为空！finalMessage="+finalMessage);
					return null;
				}
				//将手机号码和短信内容以逗号分隔存放在List中
				phoneAndContentList.add(phone+","+finalMessage);
			}
			return phoneAndContentList;
		} catch (Exception e) 
    	{
			EmpExecutionContext.error(e, "下行记录补发，根据第一条短信查找拼接长短信失败！");
			return null;
		}
    }
    
    
    /**
     * 根据长短信第一条查询长短信所有短信拼接在一起
     * @param mttaskList
     * @return List<String>  手机号,短信内容
     */
    public List<String> findPhoneAndContentByFirstContentOld(List<DynaBean> mttaskList)
    {
    	List<String> phoneAndContentList=new ArrayList<String>();
    	try 
    	{
    		GtPortUsed gtPortUsed=null;
    		String signStr="";
    		String enSignStr="";
    		String finalMessage=null;
    		
    		//获取SP账号绑定的通道路由
    		String userid=mttaskList.get(0).get("userid").toString();
    		//key:运营商  0:移动;1:联通;21:电信;5:国际  value:绑定的路由
    		Map<Integer,GtPortUsed> gtPortUsedMap=getGtPortUsed(userid);
			if(gtPortUsedMap==null||gtPortUsedMap.size()==0)
			{
				EmpExecutionContext.error("下行记录补发，通过SP账号获取SP账号绑定的路由失败！spuser="+userid);
				return null;
			}
    		
			
			long ptmsgid=0L;
    		int pktotal=0;
    		String phone=null;
    		Integer spisuncm=null;
			for (int i = 0; i < mttaskList.size(); i++) 
			{
				ptmsgid=Long.parseLong(mttaskList.get(i).get("ptmsgid").toString());
				pktotal=Integer.parseInt(mttaskList.get(i).get("pktotal").toString());
				phone=mttaskList.get(i).get("phone").toString();
				spisuncm=Integer.parseInt(mttaskList.get(i).get("unicom").toString());
				
				//通过长短信第一条，查询出此长短信拆分后的短信
				String message=getMessageByFirst(ptmsgid, pktotal);
				if(message==null)
				{
					EmpExecutionContext.error("下行记录补发，根据第一条短信查找拼接长短信的所有拆分短信失败！ptmsgid="+ptmsgid+",pktotal="+pktotal);
					return null;
				}
				
				//去掉签名
				//通过运营商类型获取路由
				gtPortUsed=gtPortUsedMap.get(spisuncm);
				//获取中文签名
				signStr=gtPortUsed.getSignstr();
				//不是国际通道
				if(spisuncm.intValue()!=5)
				{
					//去掉短信的签名
					finalMessage=handleMessage(message, signStr);
				}else   //国际通道
				{
					//英文签名
					enSignStr=gtPortUsed.getEnsignstr();
					//去掉国际短信的签名
					finalMessage=handleGJMessage(message, signStr, enSignStr);
				}
				if(finalMessage==null)
				{
					EmpExecutionContext.error("下行记录补发，补发内容为空！finalMessage="+finalMessage);
					return null;
				}
				//将手机号码和短信内容以逗号分隔存放在List中
				phoneAndContentList.add(phone+","+finalMessage);
			}
			return phoneAndContentList;
		} catch (Exception e) 
    	{
			EmpExecutionContext.error(e, "下行记录补发，根据第一条短信查找拼接长短信失败！");
			return null;
		}
    }
    
    
    /**
     * 通过长短信第一条，查询出此长短信拆分后的短信,并且拼接好
     * @param ptmsgid
     * @param pktotal
     * @return
     */
    public String getMessageByFirst(long ptmsgid,int pktotal)
    {
    	String message=null;
    	long ssid=17179869184L;
    	try {
    		String ptmsgidstb = "";
			//拼接SQL
			for(int j=1;j<=pktotal;j++)
			{
				ptmsgidstb+=String.valueOf(ptmsgid+(j-1)*ssid)+",";
			}
			ptmsgidstb=ptmsgidstb.substring(0,ptmsgidstb.lastIndexOf(","));
			
			String sql="SELECT message,svrtype,pknumber FROM GW_MT_TASK_BAK where  ptmsgid in ("+ptmsgidstb+") order by pknumber asc";
			
			List<DynaBean> msgList= superDAO.getListDynaBeanBySql(sql);
			if(msgList==null||msgList.size()==0)
			{
				EmpExecutionContext.error("下行记录补发，根据第一条短信查找此短信所有的拆分短信失败！ptmsgid="+ptmsgid+",pktotal="+pktotal);
				return null;
			}
			
			//拼接短信
			StringBuffer messageSB=new StringBuffer();
			for (int j = 0; j < msgList.size(); j++) 
			{
				messageSB.append(String.valueOf(msgList.get(j).get("message")));
			}
			message=messageSB.toString();
			return message;
		} catch (Exception e) 
    	{
			EmpExecutionContext.error(e, "下行记录补发，根据第一条短信查找并且拼接长短信的所有拆分短信失败！ptmsgid="+ptmsgid+",pktotal="+pktotal);
			return null;
		}
    }
    
    /**
     * 短信去掉签名的方法
     * @param message
     * @param signStr
     * @return
     */
    public String handleMessage(String message,String signStr)
    {
    	//去掉签名的短信
    	String finalMessage=null;
    	try 
    	{
    		if(signStr!=null)
        	{
    			signStr=signStr.trim();
        	}
    		
			//有签名，则去掉签名
			if(signStr!=null&&signStr.length()>0)
			{
				if(message.startsWith(signStr))  //前置签名     将短信内容中短信签名舍去
				{
					finalMessage = message.substring(signStr.length());
				}else if(message.endsWith(signStr)) //后置签名     将短信内容中短信签名舍去
				{
					int messageLen=message.length()-signStr.length();
					finalMessage = message.substring(0, messageLen);
				}else   //短信内容中找不到签名
                {
					finalMessage=null;
					EmpExecutionContext.error("短信去掉签名存在问题，短信内容中找不到签名！message="+message.toString()+",signStr="+signStr);
                }
			}else
			{
				//没有签名，说明签名为空，则不去掉
				finalMessage=message;
			}
		} catch (Exception e) 
    	{
			finalMessage=null;
			EmpExecutionContext.error(e, "短信去掉签名存在异常！message="+message.toString()+",signStr="+signStr);
		}
    	return finalMessage;
    }
    
    /**
     * 国际通道的短信去掉签名的方法
     * @param message
     * @param signStr
     * @param enSignStr
     * @return
     */
    public String handleGJMessage(String message,String signStr,String enSignStr)
    {
    	//去掉签名的短信
    	String finalMessage=null;
    	try{
    		if(signStr!=null)
        	{
    			signStr=signStr.trim();
        	}
    		
    		if(enSignStr!=null)
        	{
    			enSignStr=enSignStr.trim();
        	}
    		
	    	//是否存在中文签名
	    	boolean hasSignStr=(signStr!=null&&signStr.length()>0)?true:false;
	    	//是否存在英文签名
	    	boolean hasEnSignStr=(enSignStr!=null&&enSignStr.length()>0)?true:false;
	    	//中文签名和英文签名都不存在
	    	if(!hasSignStr&&!hasEnSignStr)
	    	{
	    		finalMessage=message;
	    	}else if(hasSignStr&&hasEnSignStr)  //中文签名和英文签名都存在
	    	{
	    		  if(message.startsWith(signStr))  //前置中文签名     将短信内容中短信签名舍去 
				  {
				      finalMessage = message.substring(signStr.length());
				  }else if(message.endsWith(signStr))  //后置中文签名      将短信内容中短信签名舍去
				  {
					  int messageLen=message.length()-signStr.length();
					  finalMessage = message.substring(0, messageLen);
				  }else   //短信中不包含中文签名，肯定存在英文签名
				  {
	    			  if(message.startsWith(enSignStr))  //前置英文签名     将短信内容中短信签名舍去 
					  {
					      finalMessage = message.substring(enSignStr.length());
					  }else if(message.endsWith(enSignStr))  //后置英文签名      将短信内容中短信签名舍去
					  {
						  int messageLen=message.length()-enSignStr.length();
						  finalMessage = message.substring(0, messageLen);
					  }else  //中文签名和英文签名都找不到，则存在问题
					  {
						  finalMessage=null;
						  EmpExecutionContext.error("国际通道短信去掉签名存在问题，短信内容中找不到签名！message="+message.toString()+",signStr="+signStr+",enSignStr="+enSignStr);
					  }
				  }
	    	}else if(!hasSignStr&&hasEnSignStr) //不存在中文签名，存在英文签名
	    	{
	    		 if(message.startsWith(enSignStr))  //前置英文签名     将短信内容中短信签名舍去 
				 {
			         finalMessage = message.substring(enSignStr.length());
				 }else if(message.endsWith(enSignStr))  //后置英文签名      将短信内容中短信签名舍去
				 {
					 int messageLen=message.length()-enSignStr.length();
					 finalMessage = message.substring(0, messageLen);
				 }else   //短信中不包含英文签名，则说明短信中没有签名，不需要去掉签名
				 {
					 finalMessage=message;
				 }
	    	}else if(hasSignStr&&!hasEnSignStr)  //存在中文签名，不存在英文签名
	    	{
	    		  if(message.startsWith(signStr))  //前置中文签名     将短信内容中短信签名舍去 
				  {
				      finalMessage = message.substring(signStr.length());
				  }else if(message.endsWith(signStr))  //后置中文签名      将短信内容中短信签名舍去
				  {
					  int messageLen=message.length()-signStr.length();
					  finalMessage = message.substring(0, messageLen);
				  }else   //短信中不包含中文签名，则说明短信中没有签名，不需要去掉签名
				  {
					  finalMessage=message;
				  }
	    	}else
	    	{
	    		 finalMessage=null;
	    		 EmpExecutionContext.error("国际通道短信去掉签名有问题，短信内容中找不到签名！message="+message.toString()+",signStr="+signStr+",enSignStr="+enSignStr);
	    	}
    	}catch(Exception e)
    	{
    		finalMessage=null;
    		EmpExecutionContext.error(e, "国际通道短信去掉签名存在异常！message="+message.toString()+",signStr="+signStr+",enSignStr="+enSignStr);
    	}
    	return finalMessage;
    }
    
    
	/**
	 * 获取sp账号所绑定的运营商通道，不存在返回null
	 * @param userId
	 * @param spisuncm
	 * @return
	 */
	private Map<Integer,GtPortUsed> getGtPortUsed(String userId){
		Map<Integer,GtPortUsed> gtPortUsedMap=new HashedMap<Integer, GtPortUsed>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("userId", userId);
		conditionMap.put("spisuncm&in", "0,1,21,5");
		//查询上下行路由或者下行路由
		conditionMap.put("routeFlag&in", "0,1");
		try {
			List<GtPortUsed> gtPortUsedList = empDao.findListBySymbolsCondition(GtPortUsed.class, conditionMap, null);
			if(gtPortUsedList != null && gtPortUsedList.size()>0)
			{
				for (int j = 0; j <gtPortUsedList.size(); j++) {
					gtPortUsedMap.put(gtPortUsedList.get(j).getSpisuncm(), gtPortUsedList.get(j));
				}
			}
			return gtPortUsedMap;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取发送账号绑定的所有路由失败：userId="+userId);
			return null;
		}
	}
	
	public GtPortUsed getGtPortUsed(String userid,String spisuncm)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("userId", userid);
		conditionMap.put("spisuncm", spisuncm);
		//查询上下行路由或者下行路由
		conditionMap.put("routeFlag&in", "0,1");
		try 
		{
			List<GtPortUsed> gtPortUsedList = empDao.findListBySymbolsCondition(GtPortUsed.class, conditionMap, null);
			if(gtPortUsedList != null && gtPortUsedList.size()>0)
			{
				return gtPortUsedList.get(0);
			}else 
			{
				EmpExecutionContext.error("获取发送账号绑定的路由失败：userId="+userid+",spisuncm="+spisuncm);
				return null;
			}
		} catch (Exception e) 
		{
			EmpExecutionContext.error(e, "获取发送账号绑定的路由失败：userId="+userid+",spisuncm="+spisuncm);
			return null;
		}
	}
	
	
    /**
     * 国际通道的短信去掉签名的方法
     * @param message
     * @param signStr
     * @param enSignStr
     * @return
     */
    public String handleGJMessageOld(String message,String signStr,String enSignStr)
    {
    	//去掉签名的短信
    	String finalMessage=null;
    	try{
    		if(signStr!=null)
        	{
    			signStr=signStr.trim();
        	}
    		
    		if(enSignStr!=null)
        	{
    			enSignStr=enSignStr.trim();
        	}
    		
	    	//是否存在中文签名
	    	boolean hasSignStr=(signStr!=null&&signStr.length()>0)?true:false;
	    	//是否存在英文签名
	    	boolean hasEnSignStr=(enSignStr!=null&&enSignStr.length()>0)?true:false;
	    	//中文签名和英文签名都不存在
	    	if(!hasSignStr&&!hasEnSignStr)
	    	{
	    		finalMessage=message;
	    	}else if(hasSignStr&&hasEnSignStr)  //中文签名和英文签名都存在
	    	{
	    		  if(message.startsWith(signStr))  //前置中文签名     将短信内容中短信签名舍去 
				  {
				      finalMessage = message.substring(signStr.length());
				  }else if(message.endsWith(signStr))  //后置中文签名      将短信内容中短信签名舍去
				  {
					  int messageLen=message.length()-signStr.length();
					  finalMessage = message.substring(0, messageLen);
				  }else   //短信中不包含中文签名，肯定存在英文签名
				  {
	    			  if(message.startsWith(enSignStr))  //前置英文签名     将短信内容中短信签名舍去 
					  {
					      finalMessage = message.substring(enSignStr.length());
					  }else if(message.endsWith(enSignStr))  //后置英文签名      将短信内容中短信签名舍去
					  {
						  int messageLen=message.length()-enSignStr.length();
						  finalMessage = message.substring(0, messageLen);
					  }else  //中文签名和英文签名都找不到，则存在问题
					  {
						  finalMessage=null;
						  EmpExecutionContext.error("国际通道短信去掉签名存在问题，短信内容中找不到签名！message="+message.toString()+",signStr="+signStr+",enSignStr="+enSignStr);
					  }
				  }
	    	}else if(!hasSignStr&&hasEnSignStr) //不存在中文签名，存在英文签名
	    	{
	    		 if(message.startsWith(enSignStr))  //前置英文签名     将短信内容中短信签名舍去 
				 {
			         finalMessage = message.substring(enSignStr.length());
				 }else if(message.endsWith(enSignStr))  //后置英文签名      将短信内容中短信签名舍去
				 {
					 int messageLen=message.length()-enSignStr.length();
					 finalMessage = message.substring(0, messageLen);
				 }else   //短信中不包含英文签名，则说明短信中没有签名，不需要去掉签名
				 {
					 finalMessage=message;
				 }
	    	}else if(hasSignStr&&!hasEnSignStr)  //存在中文签名，不存在英文签名
	    	{
	    		  if(message.startsWith(signStr))  //前置中文签名     将短信内容中短信签名舍去 
				  {
				      finalMessage = message.substring(signStr.length());
				  }else if(message.endsWith(signStr))  //后置中文签名      将短信内容中短信签名舍去
				  {
					  int messageLen=message.length()-signStr.length();
					  finalMessage = message.substring(0, messageLen);
				  }else   //短信中不包含中文签名，则说明短信中没有签名，不需要去掉签名
				  {
					  finalMessage=message;
				  }
	    	}else
	    	{
	    		 finalMessage=null;
	    		 EmpExecutionContext.error("国际通道短信去掉签名有问题，短信内容中找不到签名！message="+message.toString()+",signStr="+signStr+",enSignStr="+enSignStr);
	    	}
    	}catch(Exception e)
    	{
    		finalMessage=null;
    		EmpExecutionContext.error(e, "国际通道短信去掉签名存在异常！message="+message.toString()+",signStr="+signStr+",enSignStr="+enSignStr);
    	}
    	return finalMessage;
    }
    
    /**
     * 短信去掉签名的方法
     * @param message
     * @param signStr
     * @return
     */
    public String handleMessageB(String message,String signStr)
    {
    	//去掉签名的短信
    	String finalMessage=null;
    	try 
    	{
    		if(signStr!=null)
        	{
    			signStr=signStr.trim();
        	}
    		
			//有签名，则去掉签名
			if(signStr!=null&&signStr.length()>0)
			{
				if(message.startsWith(signStr))  //前置签名     将短信内容中短信签名舍去
				{
					finalMessage = message.substring(signStr.length());
				}else if(message.endsWith(signStr)) //后置签名     将短信内容中短信签名舍去
				{
					int messageLen=message.length()-signStr.length();
					finalMessage = message.substring(0, messageLen);
				}else   //短信内容中找不到签名
                {
					finalMessage=null;
					EmpExecutionContext.error("短信去掉签名存在问题，短信内容中找不到签名！message="+message.toString()+",signStr="+signStr);
                }
			}else
			{
				//没有签名，说明签名为空，则不去掉
				finalMessage=message;
			}
		} catch (Exception e) 
    	{
			finalMessage=null;
			EmpExecutionContext.error(e, "短信去掉签名存在异常！message="+message.toString()+",signStr="+signStr);
		}
    	return finalMessage;
    }
    
    
    /**
     * 处理平台流水号，滤重
     * @param ptmsgIds
     * @return
     */
    public String handlePtmsgIdsAB(String ptmsgIds)
    {
    	try 
    	{
            if(ptmsgIds==null||"".equals(ptmsgIds.trim()))
            {
            	EmpExecutionContext.error("下行记录补发，传入的平台流水号为空！ptmsgIds="+ptmsgIds);
            	return "error";
            }
            String[] msgIdArr=ptmsgIds.trim().split(",");
            if(msgIdArr==null||msgIdArr.length==0)
            {
            	EmpExecutionContext.error("下行记录补发，传入的平台流水号存在问题！ptmsgIds="+ptmsgIds);
            	return "error";
            }
            HashSet<Long> validMsgId = new HashSet<Long>();
            for (int i = 0; i < msgIdArr.length; i++) 
            {
            	validMsgId.add(Long.parseLong(msgIdArr[i]));
			}
            Object[] msgIdObjArr=validMsgId.toArray();
            ptmsgIds="";
            for (int i = 0; i < msgIdObjArr.length; i++) 
            {
            	ptmsgIds+=String.valueOf(msgIdObjArr[i])+",";
			}
            //去掉最后一个逗号
            ptmsgIds=ptmsgIds.substring(0, ptmsgIds.length()-1);
            return ptmsgIds;
		} catch (Exception e)
    	{
			EmpExecutionContext.error(e, "处理平台流水号失败！");
			return "error";
		}
    }
    
    
	public GtPortUsed getGtPortUsedO(String userid,String spisuncm)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("userId", userid);
		conditionMap.put("spisuncm", spisuncm);
		//查询上下行路由或者下行路由
		conditionMap.put("routeFlag&in", "0,1");
		try 
		{
			List<GtPortUsed> gtPortUsedList = empDao.findListBySymbolsCondition(GtPortUsed.class, conditionMap, null);
			if(gtPortUsedList != null && gtPortUsedList.size()>0)
			{
				return gtPortUsedList.get(0);
			}else 
			{
				EmpExecutionContext.error("获取发送账号绑定的路由失败：userId="+userid+",spisuncm="+spisuncm);
				return null;
			}
		} catch (Exception e) 
		{
			EmpExecutionContext.error(e, "获取发送账号绑定的路由失败：userId="+userid+",spisuncm="+spisuncm);
			return null;
		}
	}
}
