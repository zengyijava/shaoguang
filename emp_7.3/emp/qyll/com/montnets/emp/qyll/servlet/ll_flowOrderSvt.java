package com.montnets.emp.qyll.servlet;

import com.montnets.emp.common.atom.SendSmsAtom;
import com.montnets.emp.common.atom.UserdataAtom;
import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.biz.QyllOrderBiz;
import com.montnets.emp.qyll.dao.CommonDao;
import com.montnets.emp.qyll.entity.LLOrderTask;
import com.montnets.emp.qyll.entity.LlOrderDetail;
import com.montnets.emp.qyll.entity.LlPreviewParam;
import com.montnets.emp.qyll.entity.LlProduct;
import com.montnets.emp.qyll.utils.LldgUtil;
import com.montnets.emp.qyll.utils.LltextFileUtil;
import com.montnets.emp.qyll.utils.StringUtil;
import com.montnets.emp.qyll.vo.LlCompInfoVo;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.IOUtils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 企业流量订购servlet
 * @author xiebk
 *
 */
@SuppressWarnings("serial")
public class ll_flowOrderSvt extends BaseServlet {
	private final BalanceLogBiz balancebiz = new BalanceLogBiz();
	private final SmsBiz smsBiz = new SmsBiz();
	private final CommonBiz commonBiz = new CommonBiz();
	private final SendSmsAtom smsAtom = new SendSmsAtom();
	private final KeyWordAtom keyWordAtom = new KeyWordAtom();
	private final LldgUtil lldgUtil = new LldgUtil();
	private final LltextFileUtil fileUtil = new LltextFileUtil();
	private final QyllOrderBiz orderBiz = new QyllOrderBiz();
	private final CommonDao comonDao = new CommonDao();

	public void find(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		try {
			//登录操作员信息
			LfSysuser curSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			curSysuser.getDepId();
			List<Userdata> spUserList = smsBiz.getSpUserList(curSysuser);
			request.setAttribute("sendUserList", spUserList);
			
			List<LlProduct> productList = comonDao.findAllProduct();
			request.setAttribute("productList",productList);
//			request.setAttribute("result",request.getSession(false).getAttribute("result"));
			request.getRequestDispatcher("qyll/lldg/ll_flowOrder.jsp").forward(request, response);
		
		} catch (Exception e) {
			EmpExecutionContext.error(e, "发现异常！");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void preview(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		long startTime = System.currentTimeMillis();
		//返回结果
		String result = "";
		//手工输入号码
		String phoneStr = "";
		//发送账号
		String spUser = "";
		String depIdStr = "";
		// 所选群组信息
		String groupStr = "";
		// 内容
		String msg = "";
		//操作员id
		String strlguserid = null;
		//操作员id
		Long lguserid = null;
		//企业编码
		String lgcorpcode = "";
		//业务类型
		String busCode = "";
		// 获取当前登录操作员id
		//strlguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		strlguserid = SysuserUtil.strLguserid(request);

		//任务ID
		// 预览参数传递变量类
		LlPreviewParam param = new LlPreviewParam();
		//所有上传文件流集合
		List<BufferedReader> readerList = new ArrayList<BufferedReader>();
		try {
			//第一步：判断操作员
			if (strlguserid == null || strlguserid.trim().length() == 0 
					|| "null".equals(strlguserid.trim()) || "undefined".equals(strlguserid.trim()))
				{
					EmpExecutionContext.error("企业流量订购，获取操作员id异常。userid:"+strlguserid+"，errCode："+IErrorCode.V10001);
					EmpExecutionContext.logRequestUrl(request, "后台请求");
					ErrorCodeInfo info = ErrorCodeInfo.getInstance();
					String desc = info.getErrorInfo(IErrorCode.V10001);
					result = desc;
					return;
				}
				lguserid = Long.valueOf(strlguserid);
			
				//产生taskId
				Long taskId = commonBiz.getAvailableTaskId();
				param.setTaskId(taskId);
				//设置文件名参数
				String[] fileNameparam = {String.valueOf(taskId)};
				// 获取号码文件url
				String[] phoneFilePath = fileUtil.getSaveFileUrl(lguserid, fileNameparam);
//				if (phoneFilePath == null || phoneFilePath.length < 5)
				if (phoneFilePath == null)
				{
					EmpExecutionContext.error("流量订购预览，获取发送文件路径失败。userId："+ strlguserid +"，errCode:" + IErrorCode.V10013);
					throw new EMPException(IErrorCode.V10013);
				} 
				else
				{
					//判断文件是否存在，存在则返回
					if (new File(phoneFilePath[0]).exists())
					{
						EmpExecutionContext.error("流量订购预览，获取发送文件路径失败，文件路径已存在，文件路径："+phoneFilePath[0]+"，userid:"+strlguserid+"，errCode："+IErrorCode.V10013);
						throw new EMPException(IErrorCode.V10013);
					}
					param.setPhoneFilePath(phoneFilePath);
				}
				
				List<FileItem> fileList = null;
				try{
					//上传文件
					DiskFileItemFactory factory = new DiskFileItemFactory();
					factory.setSizeThreshold(1024 * 1024);
					
					//定义上传文件的临时目录
					String temp = phoneFilePath[0].substring(0, phoneFilePath[0]
							.lastIndexOf("/"));
					//创建上传文件的临时目录
					factory.setRepository(new File(temp));
					ServletFileUpload upload = new ServletFileUpload(factory);
				
					//判断所有上传文件最大数
					upload.setSizeMax(StaticValue.MAX_SIZE);
					// 以文件方式解析表单
					fileList = upload.parseRequest(request);
				} 
				catch(SizeLimitExceededException e)
				{
					//捕获到文件超出最大数限制的异常
					EmpExecutionContext.error("流量订购预览，文件上传失败，超出上传文件大小限制。userId："+ strlguserid +"，errCode:" + IErrorCode.V10014);
					throw new EMPException(IErrorCode.V10014, e);
				}
				catch (FileUploadException e)
				{
					StringBuffer logInfo= new StringBuffer();
					logInfo.append("流量订购预览，表单流上传失败。userId:").append(strlguserid)
							.append("，errCode:").append(IErrorCode.V10003)
							.append("，耗时:").append(System.currentTimeMillis()-startTime).append("ms");
					EmpExecutionContext.error(e, logInfo.toString());
					throw new EMPException(IErrorCode.V10003, e);
				}	
				Iterator<FileItem> it = fileList.iterator();
				//临时表单控件对象
				FileItem fileItem = null;
				//文件数
				int fileCount = 0;
				//上传文件对象集合
				List<FileItem> fileItemsList = new ArrayList<FileItem>();
				//存放表单控件信息，除了文件格式
				Map<String, String> fieldInfo = new HashMap<String, String>();
				//表单控件name
				String fileName = "";
				//表单控件值
				String fileValue = "";
				//文件大小累加值
				long allFileSize = 0L;
				//循环获取页面表单控件值
				while (it.hasNext())
				{
					fileItem = (FileItem) it.next();
					// 获取上传号码文件
					if (!fileItem.isFormField() && fileItem.getName().length() > 0)
					{
						//文件大小累加
						allFileSize += fileItem.getSize();
						//判断单个zip文件大小
						boolean isOverSize = smsAtom.isZipOverSize(fileItem);
						if (isOverSize)
						{
							// 大小超出限制
							EmpExecutionContext.error("流量订购预览，文件上传失败，单个zip文件超出上传文件大小限制。userId："+ strlguserid +"，errCode:" + IErrorCode.V10014);
							throw new EMPException(IErrorCode.V10014);
						}
						//有效文件对象存放到集合
						fileItemsList.add(fileItem);
					}
					else
					{
						//表单控件name
						fileName = fileItem.getFieldName();
						//表单控件值
						fileValue = fileItem.getString("UTF-8").toString();
						//控件名不为空,将表单控件信息放入MAP
						fieldInfo.put(fileName, fileValue);
					}
				}
				// 获取手工输入号码
				phoneStr = fieldInfo.get("phoneStr");
				// 获取机构id
				depIdStr = fieldInfo.get("depIdStr");
				// 获取企业编码
				lgcorpcode = fieldInfo.get("lgcorpcode");
				// 获取业务编码
				busCode = fieldInfo.get("busCode");
				// 获取群组id
				groupStr = fieldInfo.get("groupStr");
				
				//获取发送的sp账号
				spUser = fieldInfo.get("sp_User");
				
				//获取短信内容
				msg = fieldInfo.get("smsContent");
				
				//获取定时时间
				String timerTime = fieldInfo.get("timerTime");
				
				String []  tcStrs = new String[]{"-1","-1","-1"};
				String[] proIds = new String[]{"-1","-1","-1"};
				
				//获取选择的套餐
				tcStrs[0] = fieldInfo.get("yd_select").split(":")[0];
				proIds[0] = fieldInfo.get("yd_select").split(":")[1];
				tcStrs[1] = fieldInfo.get("lt_select").split(":")[0];
				proIds[1] = fieldInfo.get("lt_select").split(":")[1];
				tcStrs[2] = fieldInfo.get("dx_select").split(":")[0];
				proIds[2] = fieldInfo.get("dx_select").split(":")[1];
				param.setProductIdStrs(tcStrs);
				param.setProIds(proIds);
				
				//登录操作员信息
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				//是否需要短信提醒
				boolean needSMS = false;
				//判断是否需要短信提醒
				if(msg != null && !"".equals(msg) && !"请输入流量赠送短信提醒内容，系统将与流量套餐订购同步发送给手机用户，为空则不发送短信提醒。".equals(msg.trim())){
					needSMS = true;
					//需要短信提醒，检查sp账号，短信内容
					//操作员、企业编码、SP账号检查
					boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, lgcorpcode, spUser, null);
					if(!checkFlag){
						EmpExecutionContext.error("流量订购预览，检查操作员、企业编码、发送账号不通过，，taskid:"+taskId
								+ "，corpCode:"+lgcorpcode
								+ "，userid："+strlguserid
								+ "，spUser："+spUser
								+ "，errCode:"+ IErrorCode.B20007);
								ErrorCodeInfo info = ErrorCodeInfo.getInstance();
								String desc = info.getErrorInfo(IErrorCode.B20007);
								result = desc;
								return;	
					}
					if(msg != null && msg.trim().length() > 0){
						//包含的关键字，为""时内容无关键字
						String words = keyWordAtom.checkText(msg, lgcorpcode);
						//包含关键字
						if(!"".equals(words)){
							EmpExecutionContext.error("流量订购预览，关键字检查未通过，发送内容包含违禁词组:'"+words+"'，taskid:"+taskId
													+ "，corpCode:"+lgcorpcode
													+ "，userid："+strlguserid
													+ "，errCode:"+ IErrorCode.V10016);
							ErrorCodeInfo info = ErrorCodeInfo.getInstance();
							String desc = info.getErrorInfo(IErrorCode.V10016);
							result = desc+words;
							return;	
						}
					}
				}
				
				//解析文本文件流
				//上传文件名
				StringBuffer loadFileName= new StringBuffer("'");
				// 循环解析每个上传文件对象，获取文本文件流集合
				for (int i = 0; i < fileItemsList.size(); i++){
					// 上传文件转换为文本文件流
					readerList.addAll(smsAtom.parseFile(fileItemsList.get(i),phoneFilePath[0], fileCount, param));
					fileCount++;
					loadFileName.append(fileItemsList.get(i).getName()).append("、"); 
				}
				if(readerList.size() == 0 && StringUtil.isNullOrEmpty(phoneStr) && 
						(StringUtil.isNullOrEmpty(groupStr) || ",".equals(groupStr)) && 
							(StringUtil.isNullOrEmpty(depIdStr) || ",".equals(depIdStr))){
					result = "noPhoneOrFile";
					return;
				}
				
				// 解析文本文件流，验证号码合法性、过滤黑名单、过滤重号、生成有效号码文件和无效号码文件
				lldgUtil.parsePhone(readerList,param,lgcorpcode,"M00000",needSMS,request);
				//解析手工输入和通讯录的号码
				lldgUtil.parseAddrAndInputPhone(param, phoneStr, depIdStr, lgcorpcode, groupStr,"M00000",needSMS,request);
				//生成预览数据
				lldgUtil.packParams(param, fieldInfo);
				//没有提交号码，返回页面提示
				if(param.getEffCount() == 0){
					result = "noPhone";
					EmpExecutionContext.error("相同内容预览，没有有效号码，无法进行发送。taskid:"+taskId+"，corpCode:"+lgcorpcode+"，userid："+strlguserid);
					return;
				}
				
				Map<String,Object> objMap = new HashMap<String,Object>();
				
				//需要短信提醒，判断短信余额
				if(needSMS){
					// 机构余额
					Long depFeeCount = -1L;
					//预发送条数
					int preSendCount = -1;
					//替换短信内的特殊字符
					msg = smsBiz.smsContentFilter(msg);
					//获取预发送条数
					preSendCount = smsBiz.getCountSmsSend(spUser, msg, param.getOprValidPhone());
					if(preSendCount < 0){
						EmpExecutionContext.error("流量订购预览，获取预发送条数异常。errCode：" + IErrorCode.B20006 + "，taskid:"+taskId+"，corpCode:"+lgcorpcode+"，userid："+strlguserid);
						ErrorCodeInfo info = ErrorCodeInfo.getInstance();
						String desc = info.getErrorInfo(IErrorCode.B20006);
						result = desc;
						return;
					}
					
					//判断是否需要机构计费			
					Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
					// 如果启用了计费则为true;未启用则为false;
					boolean isCharge = false;
					if(infoMap == null){
						new CommonBiz().setSendInfo(request, response, lgcorpcode, lguserid);
						infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
					}
					if("true".equals(infoMap.get("feeFlag"))){
						isCharge = true;
					}
					
					//机构、SP账号检查余额标识，只要有其一个余额不足，则为false，之后的扣费将不再执行
					boolean isAllCharge = true;
					//启用计费，查询机构余额
					if (isCharge){
						//获取机构余额
						depFeeCount = balancebiz.getAllowSmsAmount(lfSysuser);
						if(depFeeCount == null || depFeeCount < 0){
							EmpExecutionContext.error("流量订购预览，获取机构余额异常。errCode：" + IErrorCode.B20024 
														+ "，taskid:" + taskId
														+ "，corpCode:" + lgcorpcode
														+ "，depFeeCount:"+depFeeCount
														+ "，userid："+strlguserid);
							throw new EMPException(IErrorCode.B20024);
						}
						//机构余额小于发送条数
						if(depFeeCount - preSendCount < 0){
							//设置检查标识为false余额不足
							isAllCharge = false;
							EmpExecutionContext.error("流量订购预览，机构余额小于发送条数，taskid:" + taskId
									+ "，corpCode:" + lgcorpcode
									+ "，userid："+strlguserid
									+ "，depFeeCount:"+depFeeCount
									+ "，preSendCount"+preSendCount);
						}
					}
					
					//SP账号类型,1:预付费;2:后付费 
					Long feeFlag = 2L;
					// SP账号余额
					Long spUserFeeCount = -1L;
					//机构余额检查正常或无机构扣费
					if(isAllCharge){
						//获取SP账号类型
						feeFlag = balancebiz.getSpUserFeeFlag(spUser, 1);
						if(feeFlag == null || feeFlag < 0){
							EmpExecutionContext.error("流量订购预览，获取SP账号计费类型异常。errCode：" + IErrorCode.B20044 
									+ "，taskid:" + taskId
									+ "，corpCode:" + lgcorpcode
									+ "，userid:"+strlguserid
									+ "，spUser:"+spUser
									+ "，feeFlag:"+feeFlag);
							throw new EMPException(IErrorCode.B20044);	
						}
						//预付费账号，查询SP账号余额
						if(feeFlag == 1){
							//获取SP账号余额
							spUserFeeCount = balancebiz.getSpUserAmount(spUser);
							if(spUserFeeCount == null || spUserFeeCount < 0){
								EmpExecutionContext.error("流量订购预览，获取SP账号余额异常。errCode：" + IErrorCode.B20045 
										+ "，taskid:" + taskId
										+ "，corpCode:" + lgcorpcode
										+ "，userid："+strlguserid
										+ "，spUser:"+spUser
										+ "，spUserFeeCount:"+spUserFeeCount);
								throw new EMPException(IErrorCode.B20045);	
							}
							//SP账号余额小于发送条数
							if(spUserFeeCount - preSendCount < 0){
								//设置检查标识为false余额不足
								isAllCharge = false;
								EmpExecutionContext.error("流量订购预览，SP账号余额小于发送条数。，taskid:" + taskId
										+ "，corpCode:" + lgcorpcode
										+ "，userid："+strlguserid
										+ "，spUserFeeCount:"+spUserFeeCount
										+ "，preSendCount"+preSendCount
										+ "，spUser:"+spUser);
							}
						}
					}
					
					String spFeeResult = "koufeiSuccess";
					//机构和SP账号余额检查都通过
					if(isAllCharge){
						//检查运营商余额
						spFeeResult = balancebiz.checkGwFee(spUser, preSendCount, lgcorpcode, 1);
					}
					
					objMap.put("preSendCount", preSendCount);
					objMap.put("depFeeCount", depFeeCount);//机构余额
					objMap.put("isCharge", isCharge);
					objMap.put("spFeeResult", spFeeResult);
					objMap.put("feeFlag", feeFlag);
					objMap.put("spUserFeeCount", spUserFeeCount);//短信余额
				}//判断短信余额结束
				
				//查询流量余额 T
				String balance = lldgUtil.findBalance();
				objMap.put("llBalance", balance);
				objMap.put("timerTime", timerTime);
				objMap.put("needSMS", needSMS);
				
				result = param.getJsonStr(objMap);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "发现异常！");
		}finally{
			try{
				IOUtils.closeReaders(getClass(), readerList);
			}catch(IOException e){
				EmpExecutionContext.error(e, "");
			}
			readerList.clear();
			readerList = null;
			param.getValidPhone().clear();
			param.setValidPhone(null);
			param = null;
			request.setAttribute("result", result);
			request.getRequestDispatcher(
					  "qyll/lldg/ll_preview.jsp").forward(request,response);
		}
	}
	
	public  void add(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String s = request.getRequestURI();
		String webRoot = fileUtil.getWebRoot();
		String lguserid = "";
		String result = "";
		String lgcorpcode = "";
		String taskId = "";
		LfSysuser curSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		try {
			Map<String,String> paraMap = new HashMap<String, String>();
			// 任务主题
			String title = request.getParameter("taskname");
			//任务ID
			taskId = request.getParameter("taskId");
			//当前登录操作员id
			//lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			lguserid = SysuserUtil.strLguserid(request);

			//当前登录企业
			lgcorpcode = request.getParameter("lgcorpcode");
			// 发送账号
			String spUser = request.getParameter("sp_User");
			// 信息内容
			String msg = request.getParameter("hidemsg");
			//企业ID
			String ecid = request.getParameter("ECID");
			//定时时间
			String timerTime = request.getParameter("timerTime");
			//选择的套餐编号
			String ydtc = request.getParameter("yd_select").split(":")[0];
			String lttc = request.getParameter("lt_select").split(":")[0];
			String dxtc = request.getParameter("dx_select").split(":")[0];
			String  proIds[] = {ydtc,lttc,dxtc};
			//有效号码数
			String hidSubCount = request.getParameter("hidSubCount");
			//有效号码数
			String hidEffCount = request.getParameter("hidEffCount");
			
			String filePath = request.getParameter("hidMobileUrl");
			//总价格
			String sumPrice = request.getParameter("hideSumPrice");
			//选择的产品ids
			String productIds = request.getParameter("hideProIds");
			
			if(taskId == null || lguserid == null){
				EmpExecutionContext.error("，发送获取参数异常，" + "strlguserid:" + lguserid 
						+ ";lgcorpcode:" + lgcorpcode 
						+"，errCode："+IErrorCode.V10001);
						result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
						EmpExecutionContext.error(result);
						return;
			}
			paraMap.put("filePath", webRoot + filePath);
			
			UserdataAtom userdataAtom = new UserdataAtom();
			
			//订购详情参数封装bean
			LlOrderDetail orderDetail = new LlOrderDetail();
			//订购任务参数封装bean
			LLOrderTask orderTask = new LLOrderTask();
			//设置所选套餐的主键id
			orderTask.setP_ids(productIds);
			//默认短信发送状态
			orderTask.setSmsstatus("0");
			//默认流量订购状态
			orderTask.setOrderstatus("-1");
			//默认不定时
			orderTask.setTimer_status("0");
			//默认rpt状态
			orderDetail.setLlrpt("-1");
			
			//是否定时发送
			Date date = new Date();
			boolean needTimer = false;
			if(!StringUtil.isNullOrEmpty(timerTime)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				date = sdf.parse(timerTime);
				//是否定时
				orderTask.setTimer_status("1");
				orderTask.setSmsstatus("2");
				orderTask.setOrderstatus("2");
				needTimer = true;
			}
			//设置定时时间
			orderTask.setTimer_time(new Timestamp(date.getTime()));
			orderTask.setOrdertm(new Timestamp(date.getTime()));
			orderDetail.setOrdertm(new Timestamp(date.getTime()));
			
			//企业编码
			LlCompInfoVo compInfoVo = new LlCompInfoBiz().getLlCompInfoBean();
			orderTask.setEcid(Integer.parseInt(compInfoVo.getCorpCode() == null ? "0" : compInfoVo.getCorpCode()));
			
			//任务批次号
			orderTask.setTaskid(Integer.parseInt(taskId));
			//订购产品ID
			orderTask.setPro_ids(ydtc+","+lttc+","+dxtc);
			orderDetail.setProIds(proIds);
			
			//提交号码数
			orderTask.setSubcount(Integer.valueOf(hidEffCount));
			//有效号码数
			orderTask.setEffcount(Integer.valueOf(hidEffCount));
			
			//订单编号
			String orderNum = lldgUtil.createOrderNumber(taskId);
			orderTask.setOrderNo(orderNum);
			orderDetail.setOrderno(orderNum);
			
			//操作员ID
			orderTask.setUser_id(Integer.parseInt(lguserid));
			orderDetail.setUser_id(Integer.parseInt(lguserid));
			
			//机构id
			orderDetail.setOrg_id(curSysuser.getDepId());
			orderTask.setOrg_id(curSysuser.getDepId());
			
			//短信内容
			orderDetail.setMsg(msg);
			orderTask.setMsg(msg);
			boolean needSms = false;
			if(null == msg || "".equals( msg.trim())){
				// 无需发送短信
				orderTask.setSp_user(" ");
				orderTask.setSp_pwd(" ");
				orderTask.setSmsstatus("-1");
			}else{
				// 设置发送账户密码
				orderTask.setSp_user(spUser);
				orderTask.setSp_pwd(userdataAtom.getUserPassWord(spUser));
				needSms = true;
			}
			
			//设置订购的总金额
			orderTask.setSummoney(sumPrice == null ? 0 : Double.parseDouble(sumPrice));
			//二次检查余额是否足够
			String balance = lldgUtil.findBalance();
			if(Double.parseDouble(balance) < Double.parseDouble(sumPrice)){
				//余额不足
				result = MessageUtils.extractMessage("qyll","qyll_lldg_msg1",request);
				return;
			}
			
			
			//设置主题
			if(null != title && !"".equals(title)){
				orderTask.setTopic(title);	
			}else{
				orderTask.setTopic(" ");	
			}
			//设置提交时间
			orderTask.setSubmittm(new Timestamp(date.getTime()));
			
			//审核状态  无需审核:0,未审核:1,同意:2,拒绝:3
			String checkResult = lldgUtil.checkFlow(lguserid, lgcorpcode, "7", Long.parseLong(hidSubCount),orderTask);
			if("createSuccess".equals(checkResult)){
				orderTask.setRe_status("1");
				orderTask.setOrderstatus("1");
				orderTask.setSmsstatus("1");
				result = orderBiz.order(orderTask, orderDetail,paraMap,request);
				EmpExecutionContext.info("taskId:"+taskId+"流量订购已添加到审核流程！");
				if(!"".equals(result)){
					return;
				}
				result = MessageUtils.extractMessage("qyll","qyll_lldg_msg2",request);
				return;
				
			}else if("noFlow".equals(checkResult)){
				orderTask.setRe_status("0");
				result = orderBiz.order(orderTask, orderDetail,paraMap,request);
				if(!"".equals(result)){
					return;
				}else if(needTimer){
					result = MessageUtils.extractMessage("qyll","qyll_lldg_msg3",request);//"流量订购添加到定时任务成功！";
					return;
				}else if(needSms){
					result = MessageUtils.extractMessage("qyll","qyll_lldg_msg4",request);//"订购套餐及发送短信提交成功！";
					return;
				}else{
					result = MessageUtils.extractMessage("qyll","qyll_lldg_msg5",request);//"订购套餐提交成功！";
				}
				
			}else if("createFail".equals(checkResult)){
				EmpExecutionContext.error("流量订购：添加到审核流程失败！");
				result = MessageUtils.extractMessage("qyll","qyll_lldg_msg6",request);//"流量订购添加到审核流程失败！";
				return;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"流量订购失败！");
			result = MessageUtils.extractMessage("qyll","qyll_lldg_msg7",request);//"流量订购失败！";
		}finally{
			request.getSession(false).setAttribute("result", result);
			//重定向
			s = s+"?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&oldTaskId="+taskId+"&t="+System.currentTimeMillis();
			response.sendRedirect(s);
		}
	}
	
}



