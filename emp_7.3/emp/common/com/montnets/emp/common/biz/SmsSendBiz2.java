/**
 * 
 */
package com.montnets.emp.common.biz;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.atom.ReviewAtom;
import com.montnets.emp.common.atom.UserdataAtom;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.SMParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.WGStatus;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sms.LfDrafts;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.TxtFileUtil;

/**
 * @project p_comm
 * @author chentingsheng <cts314@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午03:11:39
 * @description
 */

public class SmsSendBiz2 extends SuperBiz {
	private final SubnoManagerBiz subnoManagerBiz = new SubnoManagerBiz();

	private final SmsSpecialDAO smsSpecialDAO = new SmsSpecialDAO();

	/**
	 * 格式化时间
	 */
	private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 短信提交发送
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String send(HttpServletRequest request, HttpServletResponse response,List<String> batchNodeList) {
		long startTime = System.currentTimeMillis();
		Long lguserid = null;
		String result = "";

		// 操作内容
		String oprInfo = "";
		// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
		String sendType = request.getParameter("sendType");

		// 日志内容
		if (sendType != null && sendType.trim().length() > 0) {
			if ("1".equals(sendType)) {
				oprInfo = "相同内容群发";
			} else if ("4".equals(sendType)) {
				oprInfo = "客户群组群发";
				// 创建客户群组短信任务时,sendType都按1来处理
				sendType = "1";
			} else {
				oprInfo = "不同内容群发";
			}

			// 漏洞修复 session里获取操作员信息
			String strlguserid = SysuserUtil.strLguserid(request);
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			// 发送账号
			String spUser = request.getParameter("spUser");
			// 任务主题
			String title = request.getParameter("taskname");
			// 信息内容
			String msg = request.getParameter("msg");
			// 是否定时
			String timerStatus = request.getParameter("timerStatus");

			// 预览结果
			// 提交总数
			String hidSubCount = request.getParameter("hidSubCount");
			// 有效总数
			String hidEffCount = request.getParameter("hidEffCount");
			// 文件绝对路径
			String hidMobileUrl = request.getParameter("hidMobileUrl");
			// 预发送条数
			String hidPreSendCount = request.getParameter("hidPreSendCount");

			// 判断页面参数是否为空
			if (strlguserid == null || lgcorpcode == null || spUser == null || msg == null || timerStatus == null || hidSubCount == null || hidEffCount == null || hidMobileUrl == null || hidPreSendCount == null) {
				EmpExecutionContext.error(oprInfo + "，发送获取参数异常，" + "strlguserid:" + strlguserid + ";lgcorpcode:" + lgcorpcode + ";spUser:" + spUser + ";msg:" + msg + ";timerStatus:" + timerStatus + ";hidSubCount:" + hidSubCount + ";hidEffCount:"
						+ hidEffCount + ";hidMobileUrl:" + hidMobileUrl + ";hidPreSendCount:" + hidPreSendCount + "，errCode：" + IErrorCode.V10001);
				result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
				EmpExecutionContext.error(result);
			} else {
				// 登录操作员信息
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				// 登录操作员信息为空
				if (lfSysuser == null) {
					EmpExecutionContext.error(oprInfo + "，从session获取登录操作员信息异常。lfSysuser为null，errCode：" + IErrorCode.V10001);
					result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
					EmpExecutionContext.error(result);
				} else {
					lguserid = lfSysuser.getUserId();
					// 任务id
					String taskId = request.getParameter("taskId");
					try {
						// 操作员、企业编码、SP账号检查
						boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, lgcorpcode, spUser, null);
						if (!checkFlag) {
							EmpExecutionContext.error(oprInfo + "，检查操作员、企业编码、发送账号不通过，，taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + strlguserid + "，spUser：" + spUser + "，errCode:" + IErrorCode.V10001);
							result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
							EmpExecutionContext.error(result);
						} else
						// 判断任务ID是否合法
						if (!BalanceLogBiz.checkNumber(taskId) || "".equals(taskId)) {
							EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId格式非法，taskId:" + taskId + "strlguserid:" + strlguserid + "，lgcorpcode:" + lgcorpcode + "，errCode：" + IErrorCode.V10001);
							result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
						} else
						// 查询任务ID在是否在lf_mttask表已使用,false:存在；true:不存在
						if (!smsSpecialDAO.checkTaskIdNotUse(Long.parseLong(taskId.trim()))) {
							EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId已使用:" + taskId + "strlguserid:" + strlguserid + "，lgcorpcode:" + lgcorpcode + "，errCode：" + IErrorCode.V10001);
							result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
						} else
						// 任务ID是否已在网关任务表存在,true:存在；false:不存在
						if (smsSpecialDAO.isExistTask(taskId)) {
							EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId在网关任务表已存在，taskId:" + taskId + "，strlguserid:" + strlguserid + "，lgcorpcode:" + lgcorpcode + "，errCode：" + IErrorCode.V10001);
							result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
						} else {
							BufferedReader br = null;
							Reader r = null;
							InputStream is = null;
							FileOutputStream fos = null;
							// 初始化任务对象
							try {
								// 获取审核流，如抛出自定义异常则，流程终止，直接返回上层调用
								LfFlow flow = null;
								try {
									flow = checkUserFlow(lguserid, lgcorpcode, "1", Long.valueOf(hidPreSendCount));
								} catch (EMPException e) {
									// 不是异常，不需要记录日志，在抛出前已记录INFO日志
									String desc = ErrorCodeInfo.getInstance().getErrorInfo(e.getMessage());
									return desc;
								}

								TxtFileUtil txtUtil = new TxtFileUtil();
								
								is = new FileInputStream(txtUtil.getWebRoot()+hidMobileUrl);
								r = new InputStreamReader(is, "GBK");
								br = new BufferedReader(r);
								
								// 读源文件行数
								int lineNumber = 1;
								// 已处理批次总行数
								int sumLineNumber = 0;
								// 批次，当前处理到第几批
								int batch = 1;
								String batchId = "";
								String line;
								String lineSeparator = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
								CommonBiz commBiz = new CommonBiz();
								for (String batchNodeValueStr : batchNodeList) {
									Integer batchNodeValue = Integer.parseInt(batchNodeValueStr.split(",")[0]);
									String batchNodeTimeValue = batchNodeValueStr.split(",")[1];
									sumLineNumber += batchNodeValue;
									String newTaskId = commBiz.getAvailableTaskId().toString();
									if(1 == batch){
										batchId = newTaskId;
									}
									//设置文件名参数
									String[] fileNameparam = {newTaskId};
									// 获取号码文件url
									String[] phoneFilePath = txtUtil.getSaveFileUrl(((LfSysuser) request.getSession(false).getAttribute("loginSysuser")).getUserId(), fileNameparam);
									// 写新批次文件
									fos = new FileOutputStream(phoneFilePath[0], true);
									while(lineNumber <= sumLineNumber && null != (line = br.readLine())){
										txtUtil.repeatWriteToTxtFile(fos, line + lineSeparator);
										fos.flush();
										lineNumber++;
									}
									fos.close();

									request.setAttribute("taskname", "["+batchId+"]-"+batch);
									request.setAttribute("hidEffCount", ""+batchNodeValue);
									request.setAttribute("hidMobileUrl", phoneFilePath[1]);
									request.setAttribute("taskId", newTaskId);
									request.setAttribute("batchNodeTimeValue", batchNodeTimeValue);
									// 日志信息
									StringBuffer opContent = new StringBuffer("开始：").append(sdf.format(startTime)).append("，耗时：").append((System.currentTimeMillis() - startTime)).append("ms，title：").append(title).append("，taskid：").append(taskId)
											.append("，spUser:").append(spUser).append("，提交总数：").append(hidSubCount).append("，有效数：").append(hidEffCount).append("，状态：").append(result);
									
									// 提交发送
									result = doSend(request, flow, result, oprInfo, sendType, lgcorpcode, lfSysuser, opContent);
									batch++;
								}
							} catch (Exception e) {
								ErrorCodeInfo errInfo = ErrorCodeInfo.getInstance();
								result = errInfo.getErrorInfo(e.getMessage());
								// 未知异常
								if (result == null) {
									result = errInfo.getErrorInfo(IErrorCode.V10015);
								}
								// 失败日志
								EmpExecutionContext.error(e, strlguserid, lgcorpcode, result, "");
							}finally{
								if(null != br){
									br.close();
								}
								if(null != r){
									r.close();
								}
								if(null != is){
									is.close();
								}
								if(null != fos){
									fos.close();
								}
							}

						}

					} catch (Exception e) {
						EmpExecutionContext.error(e, oprInfo + "taskId合法性校验异常 ，taskId:" + taskId + "strlguserid:" + strlguserid + ";lgcorpcode:" + lgcorpcode + "，errCode：" + IErrorCode.V10012);
						result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10012);
						EmpExecutionContext.error(result);
						return result;
					}
				}
			}
			return result;
		} else {
			EmpExecutionContext.error("短信任务发送获取参数异常，sendType:" + sendType + "，errCode：" + IErrorCode.V10001);
			result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.logRequestUrl(request, "后台请求");
		}
		return result;
	}

	/**
	 * 
	 * @param request
	 * @param flow 审核流
	 * @param result 之前的处理状态
	 * @param oprInfo 模块名称(相同内容群发，不同内容群发...)
	 * @param sendType 发送类型
	 * @param lgcorpcode 企业编码
	 * @param lfSysuser 登陆人信息
	 * @param opContent 日志信息
	 * @return
	 * @throws EMPException
	 */
	private String doSend(HttpServletRequest request, LfFlow flow, String result, String oprInfo, String sendType, String lgcorpcode, LfSysuser lfSysuser, StringBuffer opContent) throws EMPException {
		// 创建群发任务对象
		LfMttask mttask = createLfMtTask(request, sendType);
		
		try {
			if("1".equals(mttask.getIsReply()+"")){
				// 创建短信任务时会验证尾号，所以这里为分批的每个任务创建对应批次的尾号
				SMParams smParams = new SMParams();
				smParams.setCodes(mttask.getTaskId()+"");
				// 编码类别（0模块编码1业务编码2产品编码3机构id4操作员guid,5任务id）
				smParams.setCodeType(5);
				smParams.setCorpCode(lgcorpcode);
				// (分配类型0固定1自动有效期7天，null表是不设有效期)
				smParams.setAllotType(1);
				// 尾号是否确定插入表
				smParams.setSubnoVali(false);
				smParams.setTaskId(mttask.getTaskId());
				smParams.setLoginId(""+lfSysuser.getGuId());
				ErrorCodeParam errorCodeParam = new ErrorCodeParam();
				LfSubnoAllotDetail subnoAllotDetail = GlobalVariableBiz.getInstance().getSubnoDetail(smParams, errorCodeParam);
				if(errorCodeParam.getErrorCode() != null && "EZHB237".equals(errorCodeParam.getErrorCode())){
					// 没有可用的尾号（尾号已经用完）
					result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10021);
				}else if(errorCodeParam.getErrorCode() != null && "EZHB238".equals(errorCodeParam.getErrorCode())){
					// 获取尾号失败
					result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10022);
				}
				String subNo = subnoAllotDetail != null ? subnoAllotDetail.getUsedExtendSubno() : null;
				if(subNo == null || "".equals(subNo))
				{
					return result;
				}else{
					mttask.setSubNo(subNo);
				}
			}
			// 获取发送信息等缓存数据（是否计费、是否审核、用户编码）
			Map<String, String> infoMap = null;
			infoMap = new CommonBiz().checkMapNull(infoMap, lfSysuser.getUserId(), lgcorpcode);
			// 创建短信任务、判断审核流、扣费、判断创建定时任务、调用网关发送
			result = addSmsLfMttask(mttask, infoMap, flow);

			// 操作员名称
			String userName = lfSysuser.getUserName();
			EmpExecutionContext.info(oprInfo, lgcorpcode, ""+lfSysuser.getUserId(), userName, opContent.toString(), "OTHER");
			// 结果
			String reultClong = result;
			String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
			// 根据错误编码从网关定义查找错误信息
			result = new WGStatus(langName).getInfoMap().get(result);
			// 如果返回状态网关中未定义，则重置为之前状态
			if (result == null) {
				result = reultClong;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, ""+lfSysuser.getUserId(), lgcorpcode, "创建短信任务失败", IErrorCode.V10012);
			throw new EMPException(IErrorCode.V10012, e);
		}
		return result;
	}


	/**
	 * 创建群发任务对象
	 * @param request
	 * @param sendType
	 * @return
	 * @throws EMPException
	 */
	private LfMttask createLfMtTask(HttpServletRequest request, String sendType) throws EMPException {
		LfMttask mttask = new LfMttask(); 
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		Long lguserid = ((LfSysuser) request.getSession(false).getAttribute("loginSysuser")).getUserId();
		// 发送账号
		String spUser = request.getParameter("spUser");
		try {
			// 提交类型
			String bmtType = request.getParameter("bmtType");
			// 任务主题
//			String title = request.getParameter("taskname");
			String title = request.getParameter("taskname") + (String) request.getAttribute("taskname");
			// 信息内容
			String msg = request.getParameter("msg");
			// 业务编码
			String busCode = request.getParameter("busCode");
			// 是否定时
//			String timerStatus = request.getParameter("timerStatus");
			// 定时时间
//			String timerTime = request.getParameter("timerTime");
			String timerTime = (String) request.getAttribute("batchNodeTimeValue");
			// 提交状态(已提交 2，已撤销3)
			Integer subState = 2;
			// 是否需要回复
			String isReply = request.getParameter("isReply");
			// 优先级
			String priority = request.getParameter("priority");
			// 尾号
			String subno = request.getParameter("subNo");
			// 提交总数
			String hidSubCount = request.getParameter("hidSubCount");
			// 有效总数
			String oldHidEffCount = request.getParameter("hidEffCount");
			String hidEffCount = (String) request.getAttribute("hidEffCount");
			// 文件绝对路径
//			String hidMobileUrl = request.getParameter("hidMobileUrl");
			String hidMobileUrl = (String) request.getAttribute("hidMobileUrl");
			// 预发送条数
			String hidPreSendCount = request.getParameter("hidPreSendCount");
			// 长短信条数
			Long longSMSNum = Long.parseLong(hidPreSendCount)/Long.parseLong(oldHidEffCount);
			// 贴尾内容
			String tailcontents = request.getParameter("tailcontents");
			// 任务id
//			String taskId = request.getParameter("taskId");
			String taskId = (String) request.getAttribute("taskId");
			// 非不同内容文件发送
			if (!"3".equals(sendType)) {
				if (tailcontents != null) {
					msg += tailcontents;
				}
			}
			// 提交总数
			mttask.setSubCount(Long.valueOf(hidSubCount));
			// 有效总数
			mttask.setEffCount(Long.valueOf(hidEffCount));
			// 文件绝对路径
			mttask.setMobileUrl(hidMobileUrl);
			// 预发送条数
			mttask.setIcount(""+longSMSNum*mttask.getEffCount());
			// 任务主题
			mttask.setTitle(title);
			// sp账号
			mttask.setSpUser(spUser);
			// 提交类型
			mttask.setBmtType(Integer.valueOf(bmtType));
			// 是否定时
//			Integer timerStatus1 = (timerStatus == null || "".equals(timerStatus)) ? 0 : Integer.valueOf(timerStatus);
			Integer timerStatus1 = 1;
			// 定时时间
			mttask.setTimerStatus(timerStatus1);
			// 根据发送类型判断 短信类型
			mttask.setMsgType(Integer.valueOf(sendType));
			// 不同内容动态模块，设置消息编码为35
			if ("2".equals(sendType)) {
				mttask.setMsgedcodetype(35);
			}
			// 信息类型：1－短信 2－彩信
			mttask.setMsType(1);
			// 提交类型
			mttask.setSubState(subState);
			// 业务类型
			mttask.setBusCode(busCode);
			// 中文•不能识别,转为英文字符
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
			if (timerStatus1 == 1) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timerTime = timerTime + ":00";
				mttask.setTimerTime(new Timestamp(sdf.parse(timerTime).getTime()));
			} else {
				// 非定时任务
				mttask.setTimerTime(mttask.getSubmitTime());
			}

			UserdataAtom userdataAtom = new UserdataAtom();
			// 设置发送账户密码
			mttask.setSpPwd(userdataAtom.getUserPassWord(spUser));
		} catch (Exception e) {
			EmpExecutionContext.error(e, lguserid + "", lgcorpcode, "初始化任务对象失败", IErrorCode.V10011);
			throw new EMPException(IErrorCode.V10011, e);
		}
		return mttask;
	}

	/**
	 * 创建短信任务、判断审核流、扣费、判断创建定时任务、调用网关发送
	 * 
	 * @param mt
	 * @return
	 * @throws Exception
	 */
	
	public String addSmsLfMttask(LfMttask mt, Map<String, String> infoMap, LfFlow flow) {
		// TODO:尾号同一个登录用户用一个，生成一个放session
		// 如果为回复到本次任务，设置尾号的有效期为7天
		if (mt.getIsReply() == 1) {
			boolean isSuccess = subnoManagerBiz.updateSubnoStat(mt.getSubNo(), mt.getCorpCode(), StaticValue.VALIDITY);
			if (!isSuccess) {
				return "subnoFailed";
			}
		}
		// 短信发送
		return addSmsLfMttaskResend(mt, infoMap, flow);
	}

	/**
	 * 创建短信任务、判断审核流、扣费、判断创建定时任务、调用网关发送(addSmsLfMttask调用以及短信重发调用)
	 * 
	 * @param mt
	 *            短信对象
	 * @return
	 * @throws Exception
	 */
	
	public String addSmsLfMttaskResend(LfMttask mt, Map<String, String> infoMap, LfFlow flow) {
		// 返回结果
		String returnStr = "";
		// 拆分后的短信条数，用于计费
		int sendCount = Integer.parseInt(mt.getIcount());

		// 检查发送缓存信息，若为空，则重新获取。
		infoMap = new CommonBiz().checkMapNull(infoMap, mt.getUserId(), mt.getCorpCode());
		if (infoMap == null) {
			// 短信任务创建参数获取失败
			return ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.B20022);
		}
		// 默认无需审批:0
		mt.setReState(0);
		
		if (flow != null) {
			// 设置审核状态为未审核
			mt.setReState(-1);
		}
		
		Long mtId = mt.getTaskId();
		// 无需审核时，判断是否为定时
		if (flow == null && mt.getTimerStatus().intValue() == 1) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
			String dateStr = sdf.format(new Date());
			// 定时发送不考虑秒，定时时间如果小于当前时间，则返回失败
			if (mt.getTimerTime().getTime() < Timestamp.valueOf(dateStr).getTime()) {
				EmpExecutionContext.error("定时时间小于当前时间，定时发送失败。taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
				return "timeError";
			}
		}

		BalanceLogBiz balanceBiz = new BalanceLogBiz();
		// 使用集群，上传文件到文件服务器
		if (StaticValue.getISCLUSTER() == 1) {
			CommonBiz comBiz = new CommonBiz();
			// 上传文件到文件服务器
			mt.setFileuri(comBiz.uploadFileToFileServer(mt.getMobileUrl()));
		} else {

			mt.setFileuri(StaticValue.BASEURL);
		}
		Connection conn = null;
		try {
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);

			// 检查SP账号费用
			int spFeeResult = balanceBiz.checkSpUserFee(mt.getSpUser(), sendCount, 1);
			// 0:成功,2:后附费账号 ,-2:余额不足,-3:不存在账号信息
			if (spFeeResult < 0) {
				EmpExecutionContext.error("SP账号余额小于发送条数，taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId() + ",sendCount:" + sendCount + ",spUser:" + mt.getSpUser() + ",spFeeResult:" + spFeeResult);
				return "spuserfee:" + spFeeResult;
			}

			// 机构扣费
			int depResult = balanceBiz.depKoufei(conn, mt, sendCount, infoMap);
			// -3：不存在机构信息 ，-2：余额不足 ，-1:扣费失败，0：成功
			if (depResult < 0) {
				EmpExecutionContext.error("机构扣费失败，taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId() + ",sendCount:" + sendCount + ",spUser:" + mt.getSpUser() + ",msType" + mt.getMsType() + ",depResult:" + depResult);
				return "depfee:" + depResult;
			}

			// 运营商扣费
			String wgResult = balanceBiz.wgKoufei(mt);
			if ("feefail".equals(wgResult) || "nogwfee".equals(wgResult) || wgResult.indexOf("lessgwfee") > -1) {
				// 如果开启机构计费，则补回机构费用
				if ("true".equals(infoMap.get("feeFlag"))) {
					// 机构回收
					int recResult = balanceBiz.sendSmsAmountByUserId(null, mt.getUserId(), sendCount * -1);
					if (recResult != 1) {
						EmpExecutionContext.error("补回机构费用失败，taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
					}
				}
				EmpExecutionContext.error("运营商扣费失败，返回状态：" + wgResult + "，taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
				return wgResult;
			}

			// 如果不是定时发送，则发送时间为当前时间
			if (mt.getTimerStatus().intValue() == 0) {
				mt.setTimerTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
			}
			// 保存短信任务
			empTransDao.saveObjectReturnID(conn, mt);
			mt.setMtId(mtId);
			// 需要审核
			if (flow != null) {
				ReviewBiz revBiz = new ReviewBiz();
				// 保存短信审批记录
				// 判断是否是网讯发送，修改类型，为了处理发送短信后，短信提示中的字 2014-1-7 add
				int infotype = 1;
				if (mt != null && mt.getMsType() == 6) {// 网讯发送
					infotype = 5;
				}

				boolean saveReviewResult = revBiz.addFlowRecords(conn, mtId, mt.getTitle(), mt.getSubmitTime(), infotype, flow, mt.getUserId(), "1");
				if (saveReviewResult) {
					empTransDao.commitTransaction(conn);
					// 提交事务
					returnStr = "createSuccess";
				} else {
					empTransDao.rollBackTransaction(conn);
					// 运营商余额回收
					balanceBiz.huishouFee(sendCount, mt.getSpUser(), mt.getMsType());
					// 如果开启机构计费，则补回机构费用
					if ("true".equals(infoMap.get("feeFlag"))) {
						// 机构回收
						int recResult = balanceBiz.sendSmsAmountByUserId(null, mt.getUserId(), sendCount * -1);
						if (recResult != 1) {
							EmpExecutionContext.error("补回机构费用失败，taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
						}
					}
					String desc = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.B20020);
					returnStr = desc;
					EmpExecutionContext.error("添加审核任务失败，taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
				}
			} else {
				// 如果是定时
				if (mt.getTimerStatus().intValue() == 1) {
					// 添加定时器
					TaskManagerBiz tm = new TaskManagerBiz();
					SendSmsTimerTask sendSmsTimerTask = new SendSmsTimerTask(mt.getTitle(), 1, new Date(mt.getTimerTime().getTime()), 0, String.valueOf(mtId));
					sendSmsTimerTask.setMtId(mtId);
					// 返回添加结果
					boolean flag = tm.setJob(sendSmsTimerTask);
					if (flag) {
						empTransDao.commitTransaction(conn);
						returnStr = "timerSuccess";
						EmpExecutionContext.info("创建定时任务成功，taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
					} else {
						empTransDao.rollBackTransaction(conn);
						// 运营商余额回收
						balanceBiz.huishouFee(sendCount, mt.getSpUser(), mt.getMsType());
						// 如果开启机构计费，则补回机构费用
						if ("true".equals(infoMap.get("feeFlag"))) {
							// 机构回收
							int recResult = balanceBiz.sendSmsAmountByUserId(null, mt.getUserId(), sendCount * -1);
							if (recResult != 1) {
								EmpExecutionContext.error("补回机构费用失败，recResult:" + recResult + ",taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
							}
						}
						returnStr = "timerFail";
						EmpExecutionContext.error("创建定时任务失败，taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
					}
				} else {
					// 立即发送前先提交事务
					empTransDao.commitTransaction(conn);

					SendMessage sendMessage = new SendMessage();
					mt.setMtId(mtId);

					// 向网关发送请求
					returnStr = sendMessage.sendSms(mt, infoMap);
				}
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "短信发送失败，taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
			empTransDao.rollBackTransaction(conn);
			// 运营商余额回收
			balanceBiz.huishouFee(sendCount, mt.getSpUser(), mt.getMsType());
			// 如果开启机构计费，则补回机构费用
			if ("true".equals(infoMap.get("feeFlag"))) {
				// 机构回收
				int recResult = balanceBiz.sendSmsAmountByUserId(null, mt.getUserId(), sendCount * -1);
				if (recResult != 1) {
					EmpExecutionContext.error("补回机构费用失败，taskid:" + mtId + ",corpCode:" + mt.getCorpCode() + ",userId:" + mt.getUserId());
				}
			}
			String desc = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.B20012);
			returnStr = desc;
		} finally {
			// 关闭连接
			empTransDao.closeConnection(conn);
			// 审批提醒置于事务提交后
			if (flow != null && mtId != null && "createSuccess".equals(returnStr)) {
				// 审批提醒
				new ReviewAtom().doRevRemain(mtId, mt.getUserId());
			}
		}
		return returnStr;
	}

	/**
	 * 检查操作是否绑定了审核流程
	 * 
	 * @param userId
	 *            操作员ID
	 * @param corpCode
	 *            企业编码
	 * @param infoType
	 *            信息类型
	 * @return
	 * @throws EMPException
	 */
	
	public LfFlow checkUserFlow(Long userId, String corpCode, String infoType, Long count) throws EMPException {
		ReviewBiz revBiz = new ReviewBiz();
		return revBiz.checkUserFlow(userId, corpCode, "1", count);
	}

	/**
	 * 根据草稿箱id删除草稿箱
	 * 
	 * @description
	 * @param draftId
	 *            草稿箱id
	 * @param lguserid
	 *            当前登录操作员id
	 * @param lgcorpcode
	 *            当前登录操作员企业编码
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-21 下午06:24:04
	 */
	public void dealDrafts(String draftId, String result, String lguserid, String lgcorpcode) {
		try {
			// 没有id则不需要删除
			if (draftId == null || draftId.trim().length() < 1) {
				return;
			}
			// 发送成功才删除
			if (!"000".equals(result) && !"timerSuccess".equals(result) && !"createSuccess".equals(result)) {
				return;
			}
			int delResult = empDao.delete(LfDrafts.class, draftId);
			if (delResult > 0) {
				EmpExecutionContext.info("删除草稿箱，成功" + "。draftId=" + draftId + ",lguserid=" + lguserid + ",lgcorpcode=" + lgcorpcode);
			} else {
				EmpExecutionContext.error("删除草稿箱，失败。" + "。draftId=" + draftId + ",lguserid=" + lguserid + ",lgcorpcode=" + lgcorpcode);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除草稿箱，异常。" + "。draftId=" + draftId + ",lguserid=" + lguserid + ",lgcorpcode=" + lgcorpcode);
		}
	}
}
