package com.montnets.emp.shorturl.samesms.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sms.LfDrafts;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.shorturl.comm.atom.SendSmsAtom;
import com.montnets.emp.shorturl.comm.constant.PreviewParams;
import com.montnets.emp.shorturl.surlmanage.entity.LfNeturl;
import com.montnets.emp.shorturl.surlmanage.util.TxtFileUtil;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.IOUtils;

@SuppressWarnings("serial")
public class Surl_previewSMSSvt extends BaseServlet
{
	private final BalanceLogBiz balancebiz = new BalanceLogBiz();
	private final SmsBiz smsBiz = new SmsBiz();
	private final WgMsgConfigBiz msgConfigBiz = new WgMsgConfigBiz();
	private final SendSmsAtom smsAtom = new SendSmsAtom();
	
	private final TxtFileUtil txtFileUtil = new TxtFileUtil();
	
	private final String empRoot = "shorturl";
	
	
	private final KeyWordAtom keyWordAtom = new KeyWordAtom();

	/**
	 * 预览方法
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void preview(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		long startTime = System.currentTimeMillis();
		//返回结果
		String result = "";
		//手工输入号码
		String phoneStr = "";
		//发送账号
		String spUser = "";
		//机构id
		String depIdStr = "";
		// 所选群组信息
		String groupStr = "";
		// 内容
		String msg = "";
		// 短信贴尾内容
		String tailcontents = "";
		//操作员id
		String strlguserid = null;
		//操作员id
		Long lguserid = null;
		//企业编码
		String lgcorpcode = "";
		//业务类型
		String busCode = "";
		//草稿箱id
		String draftId = "";
        //是否包含草稿文件
        String containDraft = "";
        //草稿箱临时文件
        String draftFileTemp = "";
		// 获取当前登录操作员id
		//strlguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		strlguserid = SysuserUtil.strLguserid(request);

		//任务ID
		String taskId = request.getParameter("taskId");
		//活动地址
		String netUrlId =request.getParameter("netUrlId"); 
		//域名Id
		String domainId = request.getParameter("domainId");
		String srcUrl = "";
		if (!StringUtils.isBlank(srcUrl)) {
			srcUrl = new BaseBiz().getById(LfNeturl.class, Long.valueOf(netUrlId)).getSrcurl();
		}
		// 预览参数传递变量类
		PreviewParams preParams = new PreviewParams();
		//所有上传文件流集合
		List<BufferedReader> readerList = new ArrayList<BufferedReader>();
		try
		{
			if (strlguserid == null || strlguserid.trim().length() == 0 
				|| "null".equals(strlguserid.trim()) || "undefined".equals(strlguserid.trim()))
			{
				EmpExecutionContext.error("相同内容预览，获取操作员id异常。userid:"+strlguserid+"，errCode："+IErrorCode.V10001);
				EmpExecutionContext.logRequestUrl(request, "后台请求");
				ErrorCodeInfo info = ErrorCodeInfo.getInstance();
				String desc = info.getErrorInfo(IErrorCode.V10001);
				result = desc;
				return;
			}
			lguserid = Long.valueOf(strlguserid);
			
			Map<String, String> btnMap = (Map<String, String>) request.getSession(false)
					.getAttribute("btnMap");
			// 是否有预览号码权限.
			if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null)
			{
				//号码是否可见，0：不可见，1：可见
				preParams.setIshidephone(1);
			}
			
			// 获取运营商号码段
			String[] haoduan = msgConfigBiz.getHaoduan();
			if(haoduan == null){
				EmpExecutionContext.error("相同内容预览，获取运营商号码段异常。userId："+ strlguserid +"，errCode:" + IErrorCode.V10002);
				throw new EMPException(IErrorCode.V10002);
			}
			//设置文件名参数
			String[] fileNameparam = {taskId};
			// 获取号码文件url
			String[] phoneFilePath = txtFileUtil.getSaveFileUrl(lguserid, fileNameparam);
			if (phoneFilePath == null || phoneFilePath.length < 5)
			{
				EmpExecutionContext.error("相同内容预览，获取发送文件路径失败。userId："+ strlguserid +"，errCode:" + IErrorCode.V10013);
				throw new EMPException(IErrorCode.V10013);
			} 
			else
			{
				//判断文件是否存在，存在则返回
				if (new File(phoneFilePath[0]).exists())
				{
					EmpExecutionContext.error("相同内容预览，获取发送文件路径失败，文件路径已存在，文件路径："+phoneFilePath[0]+"，userid:"+strlguserid+"，errCode："+IErrorCode.V10013);
					throw new EMPException(IErrorCode.V10013);
				}
				preParams.setPhoneFilePath(phoneFilePath);
			}
			
			List<FileItem> fileList = null;
			try
			{
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
				upload.setSizeMax(104857600l);
				// 以文件方式解析表单
				fileList = upload.parseRequest(request);
			} 
			catch(SizeLimitExceededException e)
			{
				//捕获到文件超出最大数限制的异常
				EmpExecutionContext.error("相同内容预览，文件上传失败，超出上传文件大小限制。userId："+ strlguserid +"，errCode:" + IErrorCode.V10014);
				throw new EMPException(IErrorCode.V10014, e);
			}
			catch (FileUploadException e)
			{
				StringBuffer logInfo= new StringBuffer();
				logInfo.append("相同内容预览，表单流上传失败。userId:").append(strlguserid)
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
						EmpExecutionContext.error("相同内容预览，文件上传失败，单个zip文件超出上传文件大小限制。userId："+ strlguserid +"，errCode:" + IErrorCode.V10014);
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
			// 获取群组id
			groupStr = fieldInfo.get("groupStr");
			// 获取发送账号
			spUser = fieldInfo.get("spUser1");
			// 获取发送短信内容
			msg = fieldInfo.get("msg");
			// 获取短信贴尾内容
			tailcontents = fieldInfo.get("tailcontents");
			if(tailcontents != null && tailcontents.trim().length() > 0)
			{
//				// 短信内容+贴尾内容
                if ('*' == msg.charAt(msg.length()-1)) {
                    msg += " "+tailcontents;
                }else {
                    msg += tailcontents;
                }

			}
			// 获取企业编码
			lgcorpcode = fieldInfo.get("lgcorpcode");
			// 获取业务编码
			busCode = fieldInfo.get("busCode");
			//任务ID
//			String taskId = fieldInfo.get("taskId");
			//草稿箱id
			draftId = fieldInfo.get("draftId");
			//是否包含草稿文件
			containDraft = fieldInfo.get("containDraft");
			//草稿箱临时文件
			draftFileTemp = fieldInfo.get("draftFileTemp");
			// 清空MAP
			fieldInfo.clear();
			//登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//登录操作员信息为空
			if(lfSysuser == null)
			{
				EmpExecutionContext.error("相同内容预览，从session获取登录操作员信息异常。lfSysuser为null，errCode："+IErrorCode.V10001);
				ErrorCodeInfo info = ErrorCodeInfo.getInstance();
				String desc = info.getErrorInfo(IErrorCode.V10001);
				result = desc;
				return;
			}
			//操作员、企业编码、SP账号检查
			boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, lgcorpcode, spUser, null);
			if(!checkFlag)
			{
				EmpExecutionContext.error("相同内容预览，检查操作员、企业编码、发送账号不通过，，taskid:"+taskId
						+ "，corpCode:"+lgcorpcode
						+ "，userid："+strlguserid
						+ "，spUser："+spUser
						+ "，errCode:"+ IErrorCode.B20007);
						ErrorCodeInfo info = ErrorCodeInfo.getInstance();
						String desc = info.getErrorInfo(IErrorCode.B20007);
						result = desc;
						return;	
			}
			//关键字检查
			if(msg != null && msg.trim().length() > 0)
			{
				if(lgcorpcode != null && lgcorpcode.trim().length() > 0)
				{
					//包含的关键字，为""时内容无关键字
					String words = keyWordAtom.checkText(msg, lgcorpcode);
					//包含关键字
					if(!"".equals(words))
					{
						EmpExecutionContext.error("相同内容预览，关键字检查未通过，发送内容包含违禁词组:'"+words+"'，taskid:"+taskId
												+ "，corpCode:"+lgcorpcode
												+ "，userid："+strlguserid
												+ "，errCode:"+ IErrorCode.V10016);
						ErrorCodeInfo info = ErrorCodeInfo.getInstance();
						String desc = info.getErrorInfo(IErrorCode.V10016);
						result = desc+words;
						return;	
					}
				}
				else
				{
					EmpExecutionContext.error("相同内容预览，获取企业编码异常，lgcorpcode:"+lgcorpcode+"，taskid:"+taskId+"，userid："+strlguserid+ "，errCode:"+ IErrorCode.V10001);
					ErrorCodeInfo info = ErrorCodeInfo.getInstance();
					String desc = info.getErrorInfo(IErrorCode.V10001);
					result = desc;
					return;	
				}
			}
			else
			{
				EmpExecutionContext.error("相同内容预览，获取短信内容异常，短信内容为空。taskid:"+taskId
											+ "，corpCode:"+lgcorpcode
											+ "，userid："+strlguserid
											+ "，errCode:"+ IErrorCode.V10001);
				EmpExecutionContext.logRequestUrl(request, "后台请求");
				ErrorCodeInfo info = ErrorCodeInfo.getInstance();
				String desc = info.getErrorInfo(IErrorCode.V10001);
				result = desc;
				return;
			}
			//上传文件名
			StringBuffer loadFileName= new StringBuffer("'");
			// 循环解析每个上传文件对象，获取文本文件流集合
			for (int i = 0; i < fileItemsList.size(); i++)
			{
				// 上传文件转换为文本文件流
				readerList.addAll(smsAtom.parseFile(fileItemsList.get(i),
						phoneFilePath[0], fileCount, preParams));
				fileCount++;
				loadFileName.append(fileItemsList.get(i).getName()).append("、"); 
			}
			// 获取文件名验证其合法性
			if(fileItemsList != null && fileItemsList.size()>0){
			List<String> loadFileNameSingle = Arrays.asList(loadFileName.toString().split("、"));
				for (int i = 0; i < loadFileNameSingle.size(); i++) {
					// 通过文件名截取到文件类型
					String fileType = loadFileNameSingle.get(i).substring(loadFileNameSingle.get(i).lastIndexOf(".")).toLowerCase();
					// 判断文件类型
					if(!fileType.equals(".rar") && !fileType.equals(".zip") && !fileType.equals(".xls") && !fileType.equals(".csv")
							&& !fileType.equals(".et") && !fileType.equals(".xlsx") && !fileType.equals(".txt")){
						// 文件类型不合法
						EmpExecutionContext.error("相同内容预览，文件上传失败，文件类型不合法。userId："+ strlguserid +"，errCode:" + ErrorCodeInfo.V10003);
						throw new EMPException(ErrorCodeInfo.V10003);
					}
				}
			}
			//有草稿箱文件，则需要加入草稿箱文件
			if(containDraft != null && containDraft.trim().length() > 0)
			{
				//读取草稿箱号码文件，并加入到流对象中，让下面的解析方法来解析
				int draftRes = smsAtom.setDraftReader(readerList, draftId, draftFileTemp, lguserid.toString(), lgcorpcode);
				//成功
				if(draftRes == 1)
				{
					fileCount++;
					loadFileName.append("编号").append(draftId).append("草稿箱").append("、"); 
				}
				//查不到草稿箱对象
				else if(draftRes == -2)
				{
					result = "draftnotex";
					EmpExecutionContext.error("相同内容预览，设置草稿箱文件流失败，获取不到草稿箱对象，draftId="+draftId+"，taskid:"+taskId+"，corpCode:"+lgcorpcode+"，userid："+strlguserid);
					return;
				}
				//草稿箱文件不存在
				else if(draftRes == -4)
				{
					result = "draftnotfile";
					EmpExecutionContext.error("相同内容预览，设置草稿箱文件流失败，获取不到草稿箱文件对象，文件不存在，draftId="+draftId+"，taskid:"+taskId+"，corpCode:"+lgcorpcode+"，userid："+strlguserid);
					return;
				}
				//其他失败情况
				else
				{
					result = "drafterr";
					EmpExecutionContext.error("相同内容预览，设置草稿箱文件流失败，返回状态："+draftRes+"，draftId="+draftId+"，taskid:"+taskId+"，corpCode:"+lgcorpcode+"，userid："+strlguserid);
					return;
				}
			}

			// 解析文本文件流，验证号码合法性、过滤黑名单、过滤重号、生成有效号码文件和无效号码文件
			//TOWR 增加短地址转化逻辑
			boolean re = smsAtom.parsePhone(readerList, preParams, haoduan,
					lguserid.toString(), lgcorpcode, busCode,msg,domainId,taskId);
			if (!re) {
				result = "noShortUrl";
				return;
			}
			//TODO：考虑在前面就处理好范围超出判断
			//判断有效号码数是否超出范围
			if (preParams.getEffCount() > 1000000L)
			{
				//删除前面生成的有效号码和无效号码文件
				for (int i = 0; i < phoneFilePath.length; i++)
				{
					txtFileUtil.deleteFile(phoneFilePath[i]);
				}
				//文件内有效号码大于500万
				result = "overstep";
				EmpExecutionContext.error("相同内容预览，有效号码数超出过最大限制:"+1000000+"，无法进行发送，taskid:"+taskId+"，corpCode:"+lgcorpcode+"，userid："+strlguserid);
				return;
			}
			
			//解析手工输入和通讯录的号码
			//TOWR 增加短地址转化逻辑
			boolean re2 = smsAtom.parseAddrAndInputPhone(preParams, depIdStr, phoneStr, groupStr
					, lgcorpcode, haoduan, busCode,msg,domainId,taskId);
			if (!re2) {
				result = "noShortUrl";
				return;
			}
			//处理预览号码内容，有值则去掉最后一个分号
			String previewPhone = preParams.getPreviewPhone();
			if (previewPhone.lastIndexOf(";") > 0)
			{
				previewPhone = previewPhone.substring(0, previewPhone
						.lastIndexOf(";"));
				preParams.setPreviewPhone(previewPhone);
			}
			
			//没有提交号码，返回页面提示
			if(preParams.getSubCount() == 0){
				result = "noPhone";
				EmpExecutionContext.error("相同内容预览，没有提交号码，无法进行发送。taskid:"+taskId+"，corpCode:"+lgcorpcode+"，userid："+strlguserid);
				return;
			}
			
			// 机构余额
			Long depFeeCount = -1L;
			//预发送条数
			int preSendCount = -1;
			//替换短信内的特殊字符
			msg = smsBiz.smsContentFilter(msg);
			//获取预发送条数
			preSendCount = smsBiz.getCountSmsSend(spUser, msg, preParams.getOprValidPhone());
			if(preSendCount < 0)
			{
				EmpExecutionContext.error("相同内容预览，获取预发送条数异常。errCode：" + IErrorCode.B20006 + "，taskid:"+taskId+"，corpCode:"+lgcorpcode+"，userid："+strlguserid);
				ErrorCodeInfo info = ErrorCodeInfo.getInstance();
				String desc = info.getErrorInfo(IErrorCode.B20006);
				result = desc;
				return;
			}
			
			//判断是否需要机构计费			
			Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
			// 如果启用了计费则为true;未启用则为false;
			boolean isCharge = false;
			if(infoMap == null)
			{
				new CommonBiz().setSendInfo(request, response, lgcorpcode, lguserid);
				infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
			}
			if("true".equals(infoMap.get("feeFlag")))
			{
				isCharge = true;
			}
			
			//机构、SP账号检查余额标识，只要有其一个余额不足，则为false，之后的扣费将不再执行
			boolean isAllCharge = true;
			//启用计费，查询机构余额
			if (isCharge)
			{
				//获取机构余额
				depFeeCount = balancebiz.getAllowSmsAmount(lfSysuser);
				if(depFeeCount == null || depFeeCount < 0){
					EmpExecutionContext.error("相同内容预览，获取机构余额异常。errCode：" + IErrorCode.B20024 
												+ "，taskid:" + taskId
												+ "，corpCode:" + lgcorpcode
												+ "，depFeeCount:"+depFeeCount
												+ "，userid："+strlguserid);
					throw new EMPException(IErrorCode.B20024);
				}
				//机构余额小于发送条数
				if(depFeeCount - preSendCount < 0)
				{
					//设置检查标识为false余额不足
					isAllCharge = false;
					EmpExecutionContext.error("相同内容预览，机构余额小于发送条数，taskid:" + taskId
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
			if(isAllCharge)
			{
				//获取SP账号类型
				feeFlag = balancebiz.getSpUserFeeFlag(spUser, 1);
				if(feeFlag == null || feeFlag < 0)
				{
					EmpExecutionContext.error("相同内容预览，获取SP账号计费类型异常。errCode：" + IErrorCode.B20044 
							+ "，taskid:" + taskId
							+ "，corpCode:" + lgcorpcode
							+ "，userid:"+strlguserid
							+ "，spUser:"+spUser
							+ "，feeFlag:"+feeFlag);
					throw new EMPException(IErrorCode.B20044);	
				}
				//预付费账号，查询SP账号余额
				if(feeFlag == 1)
				{
					//获取SP账号余额
					spUserFeeCount = balancebiz.getSpUserAmount(spUser);
					if(spUserFeeCount == null || spUserFeeCount < 0)
					{
						EmpExecutionContext.error("相同内容预览，获取SP账号余额异常。errCode：" + IErrorCode.B20045 
								+ "，taskid:" + taskId
								+ "，corpCode:" + lgcorpcode
								+ "，userid："+strlguserid
								+ "，spUser:"+spUser
								+ "，spUserFeeCount:"+spUserFeeCount);
						throw new EMPException(IErrorCode.B20045);	
					}
					//SP账号余额小于发送条数
					if(spUserFeeCount - preSendCount < 0)
					{
						//设置检查标识为false余额不足
						isAllCharge = false;
						EmpExecutionContext.error("相同内容预览，SP账号余额小于发送条数。，taskid:" + taskId
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
			if(isAllCharge)
			{
				//检查运营商余额
				spFeeResult = balancebiz.checkGwFee(spUser, preSendCount, lgcorpcode, 1);
			}
			
			Map<String,Object> objMap = new HashMap<String,Object>();
			
			objMap.put("preSendCount", preSendCount);
			objMap.put("depFeeCount", depFeeCount);
			objMap.put("isCharge", isCharge);
			objMap.put("spFeeResult", spFeeResult);
			objMap.put("feeFlag", feeFlag);
			objMap.put("spUserFeeCount", spUserFeeCount);
			
			//预览信息 json
			result = preParams.getJsonStr(objMap);

			//格式化时间
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			//操作日志信息
			StringBuffer opContent = new StringBuffer().append("预览S：").append(sdf.format(startTime)).append("，耗时：")
						.append((System.currentTimeMillis()-startTime)).append("ms，文件数:").append(fileCount).append("，总大小:")
						.append(allFileSize/1024).append("KB");
			//记录上传文件名
			if(fileCount > 0)
			{
				opContent.append("，文件名：")
				.append(loadFileName.deleteCharAt(loadFileName.length()-1)
				.append("'"));
			}
			//选择机构
			if(depIdStr != null && depIdStr.length()>1)
			{
				opContent.append("，DEPID：")
				.append(depIdStr.substring(1, depIdStr.length()-1));
			}
			//选择群组
			if(groupStr != null && groupStr.length() > 1)
			{
				opContent.append("，群组ID：")
				.append(groupStr.substring(1, groupStr.length()-1));
			}
			
			opContent.append("，提交总数：").append(preParams.getSubCount())
					.append("，有效数：").append(preParams.getEffCount())
					.append("，短信条数：").append(preSendCount)
					.append("，taskId：").append(taskId)
					.append("，spUser:").append(spUser);
			//操作员名称
			String userName = " ";
			if(lfSysuser != null)
			{
				userName = lfSysuser.getUserName();
			}
			EmpExecutionContext.info("相同内容群发", lgcorpcode, strlguserid, userName, opContent.toString(), "OTHER");
		} catch (EMPException empex)
		{
			ErrorCodeInfo info = ErrorCodeInfo.getInstance();
			//获取自定义异常编码
			String message = empex.getMessage();
			//获取自定义异常提示信息
			String desc = "";
			//excel文件加密异常
			if(message.indexOf("B20038") == 0)
			{
				desc = String.format(info.getErrorInfo(IErrorCode.B20038), message.substring(6));
			}
			//zip文件为异常格式文件
			else if(message.indexOf("B20039") == 0)
			{
				desc = String.format(info.getErrorInfo(IErrorCode.B20039), message.substring(6));
			}
			//文件不存在
			else if(message.indexOf("B20042") == 0)
			{
				desc = String.format(info.getErrorInfo(IErrorCode.B20042), message.substring(6));
			}
			else
			{
				desc = info.getErrorInfo(message);
			}
			//拼接前台自定义异常标识
			result = desc;
			EmpExecutionContext.error(empex, lguserid, lgcorpcode);
			EmpExecutionContext.logRequestUrl(request, "后台请求");
		} catch (Exception ex)
		{
			result = "error";
			EmpExecutionContext.error(ex, lguserid, lgcorpcode);
			EmpExecutionContext.logRequestUrl(request, "后台请求");
		} finally
		{
//			try{
//				IOUtils.closeReaders(getClass(), readerList);
//			}catch(IOException e){
//				EmpExecutionContext.error(e, "preview()IO异常");
//			}
			readerList.clear();
			readerList = null;
			preParams.getValidPhone().clear();
			preParams.setValidPhone(null);
			preParams = null;
			request.setAttribute("result", result);
			request.getRequestDispatcher(
					this.empRoot + "/surlsms/ssm_preview.jsp").forward(request,
					response);
		}
	}
	
	/**
	 * 
	 * @description 保存草稿箱
	 * @param request
	 * @param response
	 * @throws Exception       			 
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-21 下午07:02:52
	 */
	public void toDraft(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long startTime = System.currentTimeMillis();
		//返回结果
		String result = "";
		//草稿箱id
		String draftId = "";
        //是否包含草稿文件
        String containDraft = "";
        //临时草稿箱文件
        String draftFileTemp = "";
        String taskname = "";
		//手工输入号码
		String phoneStr = "";
		//发送账号
		//String spUser = "";
		//机构id
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
		// 预览参数传递变量类
		PreviewParams preParams = new PreviewParams();
		//所有上传文件流集合
		List<BufferedReader> readerList = new ArrayList<BufferedReader>();
		try
		{
			//登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//登录操作员信息为空
			if(lfSysuser == null)
			{
				EmpExecutionContext.error("相同内容暂存草稿，从session获取登录操作员信息异常。lfSysuser为null，errCode："+IErrorCode.V10001);
				ErrorCodeInfo info = ErrorCodeInfo.getInstance();
				String desc = info.getErrorInfo(IErrorCode.V10001);
				result = desc;
				return;
			}
			strlguserid = lfSysuser.getUserId().toString();
			lguserid = lfSysuser.getUserId();
			
			// 获取号码文件url。0:号码绝对路径；1：号码相对路径
			String[] phoneFilePath = txtFileUtil.getSaveUrl(lguserid, StaticValue.FILEDIRNAME + "drafttxt/ssm_sendbatch");
			if (phoneFilePath == null || phoneFilePath.length < 2)
			{
				EmpExecutionContext.error("相同内容暂存草稿，获取发送文件路径失败。userId："+ strlguserid +"，errCode:" + IErrorCode.V10013);
				throw new EMPException(IErrorCode.V10013);
			} 
			else
			{
				//判断文件是否存在，存在则返回
				if (new File(phoneFilePath[0]).exists())
				{
					EmpExecutionContext.error("相同内容暂存草稿，发送文件路径已存在，文件路径："+phoneFilePath[0]+"，userid:"+strlguserid+"，errCode："+IErrorCode.V10013);
					throw new EMPException(IErrorCode.V10013);
				}
				preParams.setPhoneFilePath(phoneFilePath);
			}
			
			List<FileItem> fileList;
			try
			{
				//上传文件
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1024 * 1024);
				
				//定义上传文件的临时目录
				String temp = phoneFilePath[0].substring(0, phoneFilePath[0].lastIndexOf("/"));
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
				EmpExecutionContext.error("相同内容暂存草稿，超出上传文件大小限制。userId："+ strlguserid +"，errCode:" + IErrorCode.V10014);
				throw new EMPException(IErrorCode.V10014, e);
			}
			catch (FileUploadException e)
			{
				StringBuffer logInfo= new StringBuffer();
				logInfo.append("相同内容暂存草稿，表单流上传失败。userId:").append(strlguserid)
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
						EmpExecutionContext.error("相同内容暂存草稿，单个zip文件超出上传文件大小限制。userId："+ strlguserid +"，errCode:" + IErrorCode.V10014);
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
			//草稿箱id
			draftId = fieldInfo.get("draftId");
			//草稿箱文件相对路径
			//draftFilePath = fieldInfo.get("draftFile");
			//是否包含草稿文件
			containDraft = fieldInfo.get("containDraft");
			//草稿箱临时文件
			draftFileTemp = fieldInfo.get("draftFileTemp");
			taskname = fieldInfo.get("taskname");
			// 获取手工输入号码
			phoneStr = fieldInfo.get("phoneStr");
			// 获取机构id
			depIdStr = fieldInfo.get("depIdStr");
			// 获取群组id
			groupStr = fieldInfo.get("groupStr");
			// 获取发送账号
			//spUser = fieldInfo.get("spUser1");
			// 获取发送短信内容
			msg = fieldInfo.get("msg");
			// 获取企业编码
			lgcorpcode = fieldInfo.get("lgcorpcode");
			// 获取业务编码
			//busCode = fieldInfo.get("busCode");
			//任务ID
			String taskId = fieldInfo.get("taskId");
			// domainId
            String domainId = fieldInfo.get("domainId");
            //srcUrl
            String netUrlId = fieldInfo.get("netUrlId");
            // validays
            String validays = fieldInfo.get("vaildays");

			// 清空MAP
			fieldInfo.clear();
			
			//加载存在的草稿箱对象
            LfDrafts oldDrafts = smsAtom.getDrafts(draftId);
			//有草稿箱id
			if(draftId != null && draftId.trim().length() > 0)
			{
				//获取不到草稿箱对象，而且页面有选择草稿箱文件，则不能用来保存
				if(oldDrafts == null && containDraft != null && containDraft.trim().length() > 0)
				{
					//草稿箱不存在
					result = "draftnotex";
					EmpExecutionContext.error("相同内容暂存草稿，草稿箱不存在，draftId："+draftId+"，taskid:"+taskId+"，corpCode:"+lgcorpcode+"，userid："+strlguserid);
					return;
				}
				//获取不到草稿箱对象，但页面没选择草稿箱文件，则当成新增
				else if(oldDrafts == null)
				{
					//清空掉id，下面判断将作为新增处理
					draftId = null;
				}
			}
			
			//上传文件名
			StringBuffer loadFileName= new StringBuffer("'");
			// 循环解析每个上传文件对象，获取文本文件流集合
			for (int i = 0; i < fileItemsList.size(); i++)
			{
				// 上传文件转换为文本文件流
				readerList.addAll(smsAtom.parseFile(fileItemsList.get(i), phoneFilePath[0], fileCount, preParams));
				fileCount++;
				loadFileName.append(fileItemsList.get(i).getName()).append("、"); 
			}

			// 解析文本文件流
			smsAtom.parseUploadFilePhone(readerList, preParams, lguserid.toString(), lgcorpcode);
			//TODO：考虑在前面就处理好范围超出判断
			//判断有效号码数是否超出范围
			if (preParams.getEffCount() > 1000000l)
			{
				//删除前面生成的有效号码和无效号码文件
				for (int i = 0; i < phoneFilePath.length; i++)
				{
					txtFileUtil.deleteFile(phoneFilePath[i]);
				}
				//文件内有效号码大于500万
				result = "overstep";
				EmpExecutionContext.error("相同内容暂存草稿，有效号码数超过"+1000000+"，taskid:"+taskId+"，corpCode:"+lgcorpcode+"，userid："+strlguserid);
				return;
			}
			
			//解析手工输入和通讯录的号码
			smsAtom.parseAddrAndInputPhone(preParams, depIdStr, phoneStr, groupStr , lgcorpcode);
			
			//没修改前的草稿箱文件相对路径
	        String draftFilePath = null;
			//草稿箱对象
            LfDrafts drafts = new LfDrafts();
			
            //新草稿箱
            if(draftId == null || draftId.trim().length() < 1)
            {
                drafts.setCorpcode(lgcorpcode);
                drafts.setCreatetime(new Timestamp(System.currentTimeMillis()));
                //相同内容
                drafts.setDraftstype(0);
                drafts.setUserid(lguserid);
            }
            else
            {
                drafts.setId(Long.parseLong(draftId));
                //加载存在的草稿箱对象
                //LfDrafts oldDrafts = baseBiz.getById(LfDrafts.class, drafts.getId());
                draftFilePath = oldDrafts.getMobileurl();
            }
            
            //设置草稿箱号码url
            smsAtom.setDraftMobileUrl(drafts, preParams, containDraft, draftFilePath, draftFileTemp, lgcorpcode);
            
            //msg为空或者空字符串，填空格，因为oracle不允许填空
            if(msg == null || msg.length() < 1)
            {
            	msg = " ";
            }
            
            drafts.setMsg(msg);
            //为空，oracle不允许null或空字符串
            if(taskname == null || taskname.length() < 1)
            {
            	//插入空格
            	taskname = " ";
            }
            drafts.setTitle(taskname);
            drafts.setUpdatetime(new Timestamp(System.currentTimeMillis()));

            //判断是否为新增
            boolean addFlag = drafts.getId() == null;

            //保存草稿箱
            boolean saveResult = smsAtom.saveDrafts(drafts, preParams);

            // 同时保存主草稿箱对应数据到子草稿箱
            Map<String, Object> extraParams = new HashMap<String, Object>();
            extraParams.put("addFlag", addFlag);
            extraParams.put("netUrlId", netUrlId);
            extraParams.put("domainId", domainId);
            extraParams.put("validays", validays);

            long saveResult2 = 1l;

            if(StringUtils.isNotEmpty(netUrlId) && StringUtils.isNotEmpty(domainId)) {
                saveResult2 = smsBiz.saveSubDrafts(drafts, extraParams);
            }

            JSONObject resultJson = new JSONObject();
            resultJson.put("draftid", drafts.getId());
            resultJson.put("draftpath", drafts.getMobileurl());
            resultJson.put("ok",saveResult ? saveResult2 > 0 ? "1" : "0" : "0");
            
			//预览信息 json
			result = resultJson.toString();

			//格式化时间
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			//操作日志信息
			StringBuffer opContent = new StringBuffer().append("相同内容暂存草稿S：").append(sdf.format(startTime)).append("，耗时：")
						.append((System.currentTimeMillis()-startTime)).append("ms，文件数:").append(fileCount).append("，总大小:")
						.append(allFileSize/1024).append("KB")
						.append("，草稿箱id："+drafts.getId())
						.append("，草稿箱名称：").append(drafts.getTitle())
						.append("，草稿箱文件url：").append(drafts.getMobileurl());
			//记录上传文件名
			if(fileCount > 0)
			{
				opContent.append("，文件名：")
				.append(loadFileName.deleteCharAt(loadFileName.length()-1)
				.append("'"));
			}
			//选择机构
			if(depIdStr != null && depIdStr.length()>1)
			{
				opContent.append("，DEPID：")
				.append(depIdStr.substring(1, depIdStr.length()-1));
			}
			//选择群组
			if(groupStr != null && groupStr.length() > 1)
			{
				opContent.append("，群组ID：")
				.append(groupStr.substring(1, groupStr.length()-1));
			}
			
			opContent.append("，提交总数：").append(preParams.getSubCount())
					.append("，有效数：").append(preParams.getEffCount())
					.append("，taskId：").append(taskId);
			
			EmpExecutionContext.info("相同内容群发", lgcorpcode, strlguserid, lfSysuser.getUserName(), opContent.toString(), "OTHER");
		} 
		catch (EMPException empex)
		{
			ErrorCodeInfo info = ErrorCodeInfo.getInstance();
			//获取自定义异常编码
			String message = empex.getMessage();
			//获取自定义异常提示信息
			String desc = "";
			//excel文件加密异常
			if(message.indexOf("B20038") == 0)
			{
				desc = String.format(info.getErrorInfo(IErrorCode.B20038), message.substring(6));
			}
			//zip文件为异常格式文件
			else if(message.indexOf("B20039") == 0)
			{
				desc = String.format(info.getErrorInfo(IErrorCode.B20039), message.substring(6));
			}
			else
			{
				desc = info.getErrorInfo(message);
			}
			//拼接前台自定义异常标识
			result = desc;
			EmpExecutionContext.error(empex, lguserid, lgcorpcode);
			EmpExecutionContext.logRequestUrl(request, "后台请求");
		} 
		catch (Exception ex)
		{
			result = "error";
			EmpExecutionContext.error(ex, lguserid, lgcorpcode);
			EmpExecutionContext.logRequestUrl(request, "后台请求");
		} 
		finally
		{
			try{
				IOUtils.closeReaders(getClass(), readerList);
			}catch(IOException e){
				EmpExecutionContext.error(e, "");
			}
			readerList.clear();
			readerList = null;
			preParams.getValidPhone().clear();
			preParams.setValidPhone(null);
			preParams = null;
			request.setAttribute("result", result);
			request.getRequestDispatcher(this.empRoot + "/surlsms/ssm_drafts.jsp").forward(request, response);
		}
	}

}
