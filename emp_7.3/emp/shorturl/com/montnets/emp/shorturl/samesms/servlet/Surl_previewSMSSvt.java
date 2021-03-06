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
	 * ????????????
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
		//????????????
		String result = "";
		//??????????????????
		String phoneStr = "";
		//????????????
		String spUser = "";
		//??????id
		String depIdStr = "";
		// ??????????????????
		String groupStr = "";
		// ??????
		String msg = "";
		// ??????????????????
		String tailcontents = "";
		//?????????id
		String strlguserid = null;
		//?????????id
		Long lguserid = null;
		//????????????
		String lgcorpcode = "";
		//????????????
		String busCode = "";
		//?????????id
		String draftId = "";
        //????????????????????????
        String containDraft = "";
        //?????????????????????
        String draftFileTemp = "";
		// ???????????????????????????id
		//strlguserid = request.getParameter("lguserid");
		//???????????? session????????????????????????
		strlguserid = SysuserUtil.strLguserid(request);

		//??????ID
		String taskId = request.getParameter("taskId");
		//????????????
		String netUrlId =request.getParameter("netUrlId"); 
		//??????Id
		String domainId = request.getParameter("domainId");
		String srcUrl = "";
		if (!StringUtils.isBlank(srcUrl)) {
			srcUrl = new BaseBiz().getById(LfNeturl.class, Long.valueOf(netUrlId)).getSrcurl();
		}
		// ???????????????????????????
		PreviewParams preParams = new PreviewParams();
		//???????????????????????????
		List<BufferedReader> readerList = new ArrayList<BufferedReader>();
		try
		{
			if (strlguserid == null || strlguserid.trim().length() == 0 
				|| "null".equals(strlguserid.trim()) || "undefined".equals(strlguserid.trim()))
			{
				EmpExecutionContext.error("????????????????????????????????????id?????????userid:"+strlguserid+"???errCode???"+IErrorCode.V10001);
				EmpExecutionContext.logRequestUrl(request, "????????????");
				ErrorCodeInfo info = ErrorCodeInfo.getInstance();
				String desc = info.getErrorInfo(IErrorCode.V10001);
				result = desc;
				return;
			}
			lguserid = Long.valueOf(strlguserid);
			
			Map<String, String> btnMap = (Map<String, String>) request.getSession(false)
					.getAttribute("btnMap");
			// ???????????????????????????.
			if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null)
			{
				//?????????????????????0???????????????1?????????
				preParams.setIshidephone(1);
			}
			
			// ????????????????????????
			String[] haoduan = msgConfigBiz.getHaoduan();
			if(haoduan == null){
				EmpExecutionContext.error("??????????????????????????????????????????????????????userId???"+ strlguserid +"???errCode:" + IErrorCode.V10002);
				throw new EMPException(IErrorCode.V10002);
			}
			//?????????????????????
			String[] fileNameparam = {taskId};
			// ??????????????????url
			String[] phoneFilePath = txtFileUtil.getSaveFileUrl(lguserid, fileNameparam);
			if (phoneFilePath == null || phoneFilePath.length < 5)
			{
				EmpExecutionContext.error("??????????????????????????????????????????????????????userId???"+ strlguserid +"???errCode:" + IErrorCode.V10013);
				throw new EMPException(IErrorCode.V10013);
			} 
			else
			{
				//??????????????????????????????????????????
				if (new File(phoneFilePath[0]).exists())
				{
					EmpExecutionContext.error("?????????????????????????????????????????????????????????????????????????????????????????????"+phoneFilePath[0]+"???userid:"+strlguserid+"???errCode???"+IErrorCode.V10013);
					throw new EMPException(IErrorCode.V10013);
				}
				preParams.setPhoneFilePath(phoneFilePath);
			}
			
			List<FileItem> fileList = null;
			try
			{
				//????????????
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1024 * 1024);
				
				//?????????????????????????????????
				String temp = phoneFilePath[0].substring(0, phoneFilePath[0]
						.lastIndexOf("/"));
				//?????????????????????????????????
				factory.setRepository(new File(temp));
				ServletFileUpload upload = new ServletFileUpload(factory);
			
				//?????????????????????????????????
				upload.setSizeMax(104857600l);
				// ???????????????????????????
				fileList = upload.parseRequest(request);
			} 
			catch(SizeLimitExceededException e)
			{
				//?????????????????????????????????????????????
				EmpExecutionContext.error("???????????????????????????????????????????????????????????????????????????userId???"+ strlguserid +"???errCode:" + IErrorCode.V10014);
				throw new EMPException(IErrorCode.V10014, e);
			}
			catch (FileUploadException e)
			{
				StringBuffer logInfo= new StringBuffer();
				logInfo.append("?????????????????????????????????????????????userId:").append(strlguserid)
						.append("???errCode:").append(IErrorCode.V10003)
						.append("?????????:").append(System.currentTimeMillis()-startTime).append("ms");
				EmpExecutionContext.error(e, logInfo.toString());
				throw new EMPException(IErrorCode.V10003, e);
			}

			Iterator<FileItem> it = fileList.iterator();
			//????????????????????????
			FileItem fileItem = null;
			//?????????
			int fileCount = 0;
			//????????????????????????
			List<FileItem> fileItemsList = new ArrayList<FileItem>();
			//?????????????????????????????????????????????
			Map<String, String> fieldInfo = new HashMap<String, String>();
			//????????????name
			String fileName = "";
			//???????????????
			String fileValue = "";
			//?????????????????????
			long allFileSize = 0L;
			//?????????????????????????????????
			while (it.hasNext())
			{
				fileItem = (FileItem) it.next();
				// ????????????????????????
				if (!fileItem.isFormField() && fileItem.getName().length() > 0)
				{
					//??????????????????
					allFileSize += fileItem.getSize();
					//????????????zip????????????
					boolean isOverSize = smsAtom.isZipOverSize(fileItem);
					if (isOverSize)
					{
						// ??????????????????
						EmpExecutionContext.error("????????????????????????????????????????????????zip???????????????????????????????????????userId???"+ strlguserid +"???errCode:" + IErrorCode.V10014);
						throw new EMPException(IErrorCode.V10014);
					}
					//?????????????????????????????????
					fileItemsList.add(fileItem);
				}
				else
				{
					//????????????name
					fileName = fileItem.getFieldName();
					//???????????????
					fileValue = fileItem.getString("UTF-8").toString();
					//??????????????????,???????????????????????????MAP
					fieldInfo.put(fileName, fileValue);
				}
			}
			// ????????????????????????
			phoneStr = fieldInfo.get("phoneStr");
			// ????????????id
			depIdStr = fieldInfo.get("depIdStr");
			// ????????????id
			groupStr = fieldInfo.get("groupStr");
			// ??????????????????
			spUser = fieldInfo.get("spUser1");
			// ????????????????????????
			msg = fieldInfo.get("msg");
			// ????????????????????????
			tailcontents = fieldInfo.get("tailcontents");
			if(tailcontents != null && tailcontents.trim().length() > 0)
			{
//				// ????????????+????????????
                if ('*' == msg.charAt(msg.length()-1)) {
                    msg += " "+tailcontents;
                }else {
                    msg += tailcontents;
                }

			}
			// ??????????????????
			lgcorpcode = fieldInfo.get("lgcorpcode");
			// ??????????????????
			busCode = fieldInfo.get("busCode");
			//??????ID
//			String taskId = fieldInfo.get("taskId");
			//?????????id
			draftId = fieldInfo.get("draftId");
			//????????????????????????
			containDraft = fieldInfo.get("containDraft");
			//?????????????????????
			draftFileTemp = fieldInfo.get("draftFileTemp");
			// ??????MAP
			fieldInfo.clear();
			//?????????????????????
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//???????????????????????????
			if(lfSysuser == null)
			{
				EmpExecutionContext.error("????????????????????????session????????????????????????????????????lfSysuser???null???errCode???"+IErrorCode.V10001);
				ErrorCodeInfo info = ErrorCodeInfo.getInstance();
				String desc = info.getErrorInfo(IErrorCode.V10001);
				result = desc;
				return;
			}
			//???????????????????????????SP????????????
			boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, lgcorpcode, spUser, null);
			if(!checkFlag)
			{
				EmpExecutionContext.error("?????????????????????????????????????????????????????????????????????????????????taskid:"+taskId
						+ "???corpCode:"+lgcorpcode
						+ "???userid???"+strlguserid
						+ "???spUser???"+spUser
						+ "???errCode:"+ IErrorCode.B20007);
						ErrorCodeInfo info = ErrorCodeInfo.getInstance();
						String desc = info.getErrorInfo(IErrorCode.B20007);
						result = desc;
						return;	
			}
			//???????????????
			if(msg != null && msg.trim().length() > 0)
			{
				if(lgcorpcode != null && lgcorpcode.trim().length() > 0)
				{
					//????????????????????????""?????????????????????
					String words = keyWordAtom.checkText(msg, lgcorpcode);
					//???????????????
					if(!"".equals(words))
					{
						EmpExecutionContext.error("??????????????????????????????????????????????????????????????????????????????:'"+words+"'???taskid:"+taskId
												+ "???corpCode:"+lgcorpcode
												+ "???userid???"+strlguserid
												+ "???errCode:"+ IErrorCode.V10016);
						ErrorCodeInfo info = ErrorCodeInfo.getInstance();
						String desc = info.getErrorInfo(IErrorCode.V10016);
						result = desc+words;
						return;	
					}
				}
				else
				{
					EmpExecutionContext.error("????????????????????????????????????????????????lgcorpcode:"+lgcorpcode+"???taskid:"+taskId+"???userid???"+strlguserid+ "???errCode:"+ IErrorCode.V10001);
					ErrorCodeInfo info = ErrorCodeInfo.getInstance();
					String desc = info.getErrorInfo(IErrorCode.V10001);
					result = desc;
					return;	
				}
			}
			else
			{
				EmpExecutionContext.error("?????????????????????????????????????????????????????????????????????taskid:"+taskId
											+ "???corpCode:"+lgcorpcode
											+ "???userid???"+strlguserid
											+ "???errCode:"+ IErrorCode.V10001);
				EmpExecutionContext.logRequestUrl(request, "????????????");
				ErrorCodeInfo info = ErrorCodeInfo.getInstance();
				String desc = info.getErrorInfo(IErrorCode.V10001);
				result = desc;
				return;
			}
			//???????????????
			StringBuffer loadFileName= new StringBuffer("'");
			// ??????????????????????????????????????????????????????????????????
			for (int i = 0; i < fileItemsList.size(); i++)
			{
				// ????????????????????????????????????
				readerList.addAll(smsAtom.parseFile(fileItemsList.get(i),
						phoneFilePath[0], fileCount, preParams));
				fileCount++;
				loadFileName.append(fileItemsList.get(i).getName()).append("???"); 
			}
			// ?????????????????????????????????
			if(fileItemsList != null && fileItemsList.size()>0){
			List<String> loadFileNameSingle = Arrays.asList(loadFileName.toString().split("???"));
				for (int i = 0; i < loadFileNameSingle.size(); i++) {
					// ????????????????????????????????????
					String fileType = loadFileNameSingle.get(i).substring(loadFileNameSingle.get(i).lastIndexOf(".")).toLowerCase();
					// ??????????????????
					if(!fileType.equals(".rar") && !fileType.equals(".zip") && !fileType.equals(".xls") && !fileType.equals(".csv")
							&& !fileType.equals(".et") && !fileType.equals(".xlsx") && !fileType.equals(".txt")){
						// ?????????????????????
						EmpExecutionContext.error("??????????????????????????????????????????????????????????????????userId???"+ strlguserid +"???errCode:" + ErrorCodeInfo.V10003);
						throw new EMPException(ErrorCodeInfo.V10003);
					}
				}
			}
			//???????????????????????????????????????????????????
			if(containDraft != null && containDraft.trim().length() > 0)
			{
				//??????????????????????????????????????????????????????????????????????????????????????????
				int draftRes = smsAtom.setDraftReader(readerList, draftId, draftFileTemp, lguserid.toString(), lgcorpcode);
				//??????
				if(draftRes == 1)
				{
					fileCount++;
					loadFileName.append("??????").append(draftId).append("?????????").append("???"); 
				}
				//????????????????????????
				else if(draftRes == -2)
				{
					result = "draftnotex";
					EmpExecutionContext.error("????????????????????????????????????????????????????????????????????????????????????draftId="+draftId+"???taskid:"+taskId+"???corpCode:"+lgcorpcode+"???userid???"+strlguserid);
					return;
				}
				//????????????????????????
				else if(draftRes == -4)
				{
					result = "draftnotfile";
					EmpExecutionContext.error("????????????????????????????????????????????????????????????????????????????????????????????????????????????draftId="+draftId+"???taskid:"+taskId+"???corpCode:"+lgcorpcode+"???userid???"+strlguserid);
					return;
				}
				//??????????????????
				else
				{
					result = "drafterr";
					EmpExecutionContext.error("?????????????????????????????????????????????????????????????????????"+draftRes+"???draftId="+draftId+"???taskid:"+taskId+"???corpCode:"+lgcorpcode+"???userid???"+strlguserid);
					return;
				}
			}

			// ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			//TOWR ???????????????????????????
			boolean re = smsAtom.parsePhone(readerList, preParams, haoduan,
					lguserid.toString(), lgcorpcode, busCode,msg,domainId,taskId);
			if (!re) {
				result = "noShortUrl";
				return;
			}
			//TODO????????????????????????????????????????????????
			//???????????????????????????????????????
			if (preParams.getEffCount() > 1000000L)
			{
				//??????????????????????????????????????????????????????
				for (int i = 0; i < phoneFilePath.length; i++)
				{
					txtFileUtil.deleteFile(phoneFilePath[i]);
				}
				//???????????????????????????500???
				result = "overstep";
				EmpExecutionContext.error("?????????????????????????????????????????????????????????:"+1000000+"????????????????????????taskid:"+taskId+"???corpCode:"+lgcorpcode+"???userid???"+strlguserid);
				return;
			}
			
			//???????????????????????????????????????
			//TOWR ???????????????????????????
			boolean re2 = smsAtom.parseAddrAndInputPhone(preParams, depIdStr, phoneStr, groupStr
					, lgcorpcode, haoduan, busCode,msg,domainId,taskId);
			if (!re2) {
				result = "noShortUrl";
				return;
			}
			//????????????????????????????????????????????????????????????
			String previewPhone = preParams.getPreviewPhone();
			if (previewPhone.lastIndexOf(";") > 0)
			{
				previewPhone = previewPhone.substring(0, previewPhone
						.lastIndexOf(";"));
				preParams.setPreviewPhone(previewPhone);
			}
			
			//???????????????????????????????????????
			if(preParams.getSubCount() == 0){
				result = "noPhone";
				EmpExecutionContext.error("???????????????????????????????????????????????????????????????taskid:"+taskId+"???corpCode:"+lgcorpcode+"???userid???"+strlguserid);
				return;
			}
			
			// ????????????
			Long depFeeCount = -1L;
			//???????????????
			int preSendCount = -1;
			//??????????????????????????????
			msg = smsBiz.smsContentFilter(msg);
			//?????????????????????
			preSendCount = smsBiz.getCountSmsSend(spUser, msg, preParams.getOprValidPhone());
			if(preSendCount < 0)
			{
				EmpExecutionContext.error("???????????????????????????????????????????????????errCode???" + IErrorCode.B20006 + "???taskid:"+taskId+"???corpCode:"+lgcorpcode+"???userid???"+strlguserid);
				ErrorCodeInfo info = ErrorCodeInfo.getInstance();
				String desc = info.getErrorInfo(IErrorCode.B20006);
				result = desc;
				return;
			}
			
			//??????????????????????????????			
			Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
			// ???????????????????????????true;???????????????false;
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
			
			//?????????SP??????????????????????????????????????????????????????????????????false?????????????????????????????????
			boolean isAllCharge = true;
			//?????????????????????????????????
			if (isCharge)
			{
				//??????????????????
				depFeeCount = balancebiz.getAllowSmsAmount(lfSysuser);
				if(depFeeCount == null || depFeeCount < 0){
					EmpExecutionContext.error("????????????????????????????????????????????????errCode???" + IErrorCode.B20024 
												+ "???taskid:" + taskId
												+ "???corpCode:" + lgcorpcode
												+ "???depFeeCount:"+depFeeCount
												+ "???userid???"+strlguserid);
					throw new EMPException(IErrorCode.B20024);
				}
				//??????????????????????????????
				if(depFeeCount - preSendCount < 0)
				{
					//?????????????????????false????????????
					isAllCharge = false;
					EmpExecutionContext.error("??????????????????????????????????????????????????????taskid:" + taskId
							+ "???corpCode:" + lgcorpcode
							+ "???userid???"+strlguserid
							+ "???depFeeCount:"+depFeeCount
							+ "???preSendCount"+preSendCount);
				}
				
			}
			//SP????????????,1:?????????;2:????????? 
			Long feeFlag = 2L;
			// SP????????????
			Long spUserFeeCount = -1L;
			//??????????????????????????????????????????
			if(isAllCharge)
			{
				//??????SP????????????
				feeFlag = balancebiz.getSpUserFeeFlag(spUser, 1);
				if(feeFlag == null || feeFlag < 0)
				{
					EmpExecutionContext.error("???????????????????????????SP???????????????????????????errCode???" + IErrorCode.B20044 
							+ "???taskid:" + taskId
							+ "???corpCode:" + lgcorpcode
							+ "???userid:"+strlguserid
							+ "???spUser:"+spUser
							+ "???feeFlag:"+feeFlag);
					throw new EMPException(IErrorCode.B20044);	
				}
				//????????????????????????SP????????????
				if(feeFlag == 1)
				{
					//??????SP????????????
					spUserFeeCount = balancebiz.getSpUserAmount(spUser);
					if(spUserFeeCount == null || spUserFeeCount < 0)
					{
						EmpExecutionContext.error("???????????????????????????SP?????????????????????errCode???" + IErrorCode.B20045 
								+ "???taskid:" + taskId
								+ "???corpCode:" + lgcorpcode
								+ "???userid???"+strlguserid
								+ "???spUser:"+spUser
								+ "???spUserFeeCount:"+spUserFeeCount);
						throw new EMPException(IErrorCode.B20045);	
					}
					//SP??????????????????????????????
					if(spUserFeeCount - preSendCount < 0)
					{
						//?????????????????????false????????????
						isAllCharge = false;
						EmpExecutionContext.error("?????????????????????SP????????????????????????????????????taskid:" + taskId
								+ "???corpCode:" + lgcorpcode
								+ "???userid???"+strlguserid
								+ "???spUserFeeCount:"+spUserFeeCount
								+ "???preSendCount"+preSendCount
								+ "???spUser:"+spUser);
					}
				}
			}
			
			String spFeeResult = "koufeiSuccess";
			//?????????SP???????????????????????????
			if(isAllCharge)
			{
				//?????????????????????
				spFeeResult = balancebiz.checkGwFee(spUser, preSendCount, lgcorpcode, 1);
			}
			
			Map<String,Object> objMap = new HashMap<String,Object>();
			
			objMap.put("preSendCount", preSendCount);
			objMap.put("depFeeCount", depFeeCount);
			objMap.put("isCharge", isCharge);
			objMap.put("spFeeResult", spFeeResult);
			objMap.put("feeFlag", feeFlag);
			objMap.put("spUserFeeCount", spUserFeeCount);
			
			//???????????? json
			result = preParams.getJsonStr(objMap);

			//???????????????
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			//??????????????????
			StringBuffer opContent = new StringBuffer().append("??????S???").append(sdf.format(startTime)).append("????????????")
						.append((System.currentTimeMillis()-startTime)).append("ms????????????:").append(fileCount).append("????????????:")
						.append(allFileSize/1024).append("KB");
			//?????????????????????
			if(fileCount > 0)
			{
				opContent.append("???????????????")
				.append(loadFileName.deleteCharAt(loadFileName.length()-1)
				.append("'"));
			}
			//????????????
			if(depIdStr != null && depIdStr.length()>1)
			{
				opContent.append("???DEPID???")
				.append(depIdStr.substring(1, depIdStr.length()-1));
			}
			//????????????
			if(groupStr != null && groupStr.length() > 1)
			{
				opContent.append("?????????ID???")
				.append(groupStr.substring(1, groupStr.length()-1));
			}
			
			opContent.append("??????????????????").append(preParams.getSubCount())
					.append("???????????????").append(preParams.getEffCount())
					.append("??????????????????").append(preSendCount)
					.append("???taskId???").append(taskId)
					.append("???spUser:").append(spUser);
			//???????????????
			String userName = " ";
			if(lfSysuser != null)
			{
				userName = lfSysuser.getUserName();
			}
			EmpExecutionContext.info("??????????????????", lgcorpcode, strlguserid, userName, opContent.toString(), "OTHER");
		} catch (EMPException empex)
		{
			ErrorCodeInfo info = ErrorCodeInfo.getInstance();
			//???????????????????????????
			String message = empex.getMessage();
			//?????????????????????????????????
			String desc = "";
			//excel??????????????????
			if(message.indexOf("B20038") == 0)
			{
				desc = String.format(info.getErrorInfo(IErrorCode.B20038), message.substring(6));
			}
			//zip???????????????????????????
			else if(message.indexOf("B20039") == 0)
			{
				desc = String.format(info.getErrorInfo(IErrorCode.B20039), message.substring(6));
			}
			//???????????????
			else if(message.indexOf("B20042") == 0)
			{
				desc = String.format(info.getErrorInfo(IErrorCode.B20042), message.substring(6));
			}
			else
			{
				desc = info.getErrorInfo(message);
			}
			//?????????????????????????????????
			result = desc;
			EmpExecutionContext.error(empex, lguserid, lgcorpcode);
			EmpExecutionContext.logRequestUrl(request, "????????????");
		} catch (Exception ex)
		{
			result = "error";
			EmpExecutionContext.error(ex, lguserid, lgcorpcode);
			EmpExecutionContext.logRequestUrl(request, "????????????");
		} finally
		{
//			try{
//				IOUtils.closeReaders(getClass(), readerList);
//			}catch(IOException e){
//				EmpExecutionContext.error(e, "preview()IO??????");
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
	 * @description ???????????????
	 * @param request
	 * @param response
	 * @throws Exception       			 
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-21 ??????07:02:52
	 */
	public void toDraft(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long startTime = System.currentTimeMillis();
		//????????????
		String result = "";
		//?????????id
		String draftId = "";
        //????????????????????????
        String containDraft = "";
        //?????????????????????
        String draftFileTemp = "";
        String taskname = "";
		//??????????????????
		String phoneStr = "";
		//????????????
		//String spUser = "";
		//??????id
		String depIdStr = "";
		// ??????????????????
		String groupStr = "";
		// ??????
		String msg = "";
		//?????????id
		String strlguserid = null;
		//?????????id
		Long lguserid = null;
		//????????????
		String lgcorpcode = "";
		//????????????
		// ???????????????????????????
		PreviewParams preParams = new PreviewParams();
		//???????????????????????????
		List<BufferedReader> readerList = new ArrayList<BufferedReader>();
		try
		{
			//?????????????????????
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//???????????????????????????
			if(lfSysuser == null)
			{
				EmpExecutionContext.error("??????????????????????????????session????????????????????????????????????lfSysuser???null???errCode???"+IErrorCode.V10001);
				ErrorCodeInfo info = ErrorCodeInfo.getInstance();
				String desc = info.getErrorInfo(IErrorCode.V10001);
				result = desc;
				return;
			}
			strlguserid = lfSysuser.getUserId().toString();
			lguserid = lfSysuser.getUserId();
			
			// ??????????????????url???0:?????????????????????1?????????????????????
			String[] phoneFilePath = txtFileUtil.getSaveUrl(lguserid, StaticValue.FILEDIRNAME + "drafttxt/ssm_sendbatch");
			if (phoneFilePath == null || phoneFilePath.length < 2)
			{
				EmpExecutionContext.error("????????????????????????????????????????????????????????????userId???"+ strlguserid +"???errCode:" + IErrorCode.V10013);
				throw new EMPException(IErrorCode.V10013);
			} 
			else
			{
				//??????????????????????????????????????????
				if (new File(phoneFilePath[0]).exists())
				{
					EmpExecutionContext.error("????????????????????????????????????????????????????????????????????????"+phoneFilePath[0]+"???userid:"+strlguserid+"???errCode???"+IErrorCode.V10013);
					throw new EMPException(IErrorCode.V10013);
				}
				preParams.setPhoneFilePath(phoneFilePath);
			}
			
			List<FileItem> fileList;
			try
			{
				//????????????
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1024 * 1024);
				
				//?????????????????????????????????
				String temp = phoneFilePath[0].substring(0, phoneFilePath[0].lastIndexOf("/"));
				//?????????????????????????????????
				factory.setRepository(new File(temp));
				ServletFileUpload upload = new ServletFileUpload(factory);
			
				//?????????????????????????????????
				upload.setSizeMax(StaticValue.MAX_SIZE);
				// ???????????????????????????
				fileList = upload.parseRequest(request);
			} 
			catch(SizeLimitExceededException e)
			{
				//?????????????????????????????????????????????
				EmpExecutionContext.error("????????????????????????????????????????????????????????????userId???"+ strlguserid +"???errCode:" + IErrorCode.V10014);
				throw new EMPException(IErrorCode.V10014, e);
			}
			catch (FileUploadException e)
			{
				StringBuffer logInfo= new StringBuffer();
				logInfo.append("???????????????????????????????????????????????????userId:").append(strlguserid)
						.append("???errCode:").append(IErrorCode.V10003)
						.append("?????????:").append(System.currentTimeMillis()-startTime).append("ms");
				EmpExecutionContext.error(e, logInfo.toString());
				throw new EMPException(IErrorCode.V10003, e);
			}

			Iterator<FileItem> it = fileList.iterator();
			//????????????????????????
			FileItem fileItem = null;
			//?????????
			int fileCount = 0;
			//????????????????????????
			List<FileItem> fileItemsList = new ArrayList<FileItem>();
			//?????????????????????????????????????????????
			Map<String, String> fieldInfo = new HashMap<String, String>();
			//????????????name
			String fileName = "";
			//???????????????
			String fileValue = "";
			//?????????????????????
			long allFileSize = 0L;
			//?????????????????????????????????
			while (it.hasNext())
			{
				fileItem = (FileItem) it.next();
				// ????????????????????????
				if (!fileItem.isFormField() && fileItem.getName().length() > 0)
				{
					//??????????????????
					allFileSize += fileItem.getSize();
					//????????????zip????????????
					boolean isOverSize = smsAtom.isZipOverSize(fileItem);
					if (isOverSize)
					{
						// ??????????????????
						EmpExecutionContext.error("?????????????????????????????????zip???????????????????????????????????????userId???"+ strlguserid +"???errCode:" + IErrorCode.V10014);
						throw new EMPException(IErrorCode.V10014);
					}
					//?????????????????????????????????
					fileItemsList.add(fileItem);
				}
				else
				{
					//????????????name
					fileName = fileItem.getFieldName();
					//???????????????
					fileValue = fileItem.getString("UTF-8").toString();
					//??????????????????,???????????????????????????MAP
					fieldInfo.put(fileName, fileValue);
				}
			}
			//?????????id
			draftId = fieldInfo.get("draftId");
			//???????????????????????????
			//draftFilePath = fieldInfo.get("draftFile");
			//????????????????????????
			containDraft = fieldInfo.get("containDraft");
			//?????????????????????
			draftFileTemp = fieldInfo.get("draftFileTemp");
			taskname = fieldInfo.get("taskname");
			// ????????????????????????
			phoneStr = fieldInfo.get("phoneStr");
			// ????????????id
			depIdStr = fieldInfo.get("depIdStr");
			// ????????????id
			groupStr = fieldInfo.get("groupStr");
			// ??????????????????
			//spUser = fieldInfo.get("spUser1");
			// ????????????????????????
			msg = fieldInfo.get("msg");
			// ??????????????????
			lgcorpcode = fieldInfo.get("lgcorpcode");
			// ??????????????????
			//busCode = fieldInfo.get("busCode");
			//??????ID
			String taskId = fieldInfo.get("taskId");
			// domainId
            String domainId = fieldInfo.get("domainId");
            //srcUrl
            String netUrlId = fieldInfo.get("netUrlId");
            // validays
            String validays = fieldInfo.get("vaildays");

			// ??????MAP
			fieldInfo.clear();
			
			//??????????????????????????????
            LfDrafts oldDrafts = smsAtom.getDrafts(draftId);
			//????????????id
			if(draftId != null && draftId.trim().length() > 0)
			{
				//??????????????????????????????????????????????????????????????????????????????????????????
				if(oldDrafts == null && containDraft != null && containDraft.trim().length() > 0)
				{
					//??????????????????
					result = "draftnotex";
					EmpExecutionContext.error("????????????????????????????????????????????????draftId???"+draftId+"???taskid:"+taskId+"???corpCode:"+lgcorpcode+"???userid???"+strlguserid);
					return;
				}
				//?????????????????????????????????????????????????????????????????????????????????
				else if(oldDrafts == null)
				{
					//?????????id????????????????????????????????????
					draftId = null;
				}
			}
			
			//???????????????
			StringBuffer loadFileName= new StringBuffer("'");
			// ??????????????????????????????????????????????????????????????????
			for (int i = 0; i < fileItemsList.size(); i++)
			{
				// ????????????????????????????????????
				readerList.addAll(smsAtom.parseFile(fileItemsList.get(i), phoneFilePath[0], fileCount, preParams));
				fileCount++;
				loadFileName.append(fileItemsList.get(i).getName()).append("???"); 
			}

			// ?????????????????????
			smsAtom.parseUploadFilePhone(readerList, preParams, lguserid.toString(), lgcorpcode);
			//TODO????????????????????????????????????????????????
			//???????????????????????????????????????
			if (preParams.getEffCount() > 1000000l)
			{
				//??????????????????????????????????????????????????????
				for (int i = 0; i < phoneFilePath.length; i++)
				{
					txtFileUtil.deleteFile(phoneFilePath[i]);
				}
				//???????????????????????????500???
				result = "overstep";
				EmpExecutionContext.error("????????????????????????????????????????????????"+1000000+"???taskid:"+taskId+"???corpCode:"+lgcorpcode+"???userid???"+strlguserid);
				return;
			}
			
			//???????????????????????????????????????
			smsAtom.parseAddrAndInputPhone(preParams, depIdStr, phoneStr, groupStr , lgcorpcode);
			
			//??????????????????????????????????????????
	        String draftFilePath = null;
			//???????????????
            LfDrafts drafts = new LfDrafts();
			
            //????????????
            if(draftId == null || draftId.trim().length() < 1)
            {
                drafts.setCorpcode(lgcorpcode);
                drafts.setCreatetime(new Timestamp(System.currentTimeMillis()));
                //????????????
                drafts.setDraftstype(0);
                drafts.setUserid(lguserid);
            }
            else
            {
                drafts.setId(Long.parseLong(draftId));
                //??????????????????????????????
                //LfDrafts oldDrafts = baseBiz.getById(LfDrafts.class, drafts.getId());
                draftFilePath = oldDrafts.getMobileurl();
            }
            
            //?????????????????????url
            smsAtom.setDraftMobileUrl(drafts, preParams, containDraft, draftFilePath, draftFileTemp, lgcorpcode);
            
            //msg?????????????????????????????????????????????oracle???????????????
            if(msg == null || msg.length() < 1)
            {
            	msg = " ";
            }
            
            drafts.setMsg(msg);
            //?????????oracle?????????null???????????????
            if(taskname == null || taskname.length() < 1)
            {
            	//????????????
            	taskname = " ";
            }
            drafts.setTitle(taskname);
            drafts.setUpdatetime(new Timestamp(System.currentTimeMillis()));

            //?????????????????????
            boolean addFlag = drafts.getId() == null;

            //???????????????
            boolean saveResult = smsAtom.saveDrafts(drafts, preParams);

            // ???????????????????????????????????????????????????
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
            
			//???????????? json
			result = resultJson.toString();

			//???????????????
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			//??????????????????
			StringBuffer opContent = new StringBuffer().append("????????????????????????S???").append(sdf.format(startTime)).append("????????????")
						.append((System.currentTimeMillis()-startTime)).append("ms????????????:").append(fileCount).append("????????????:")
						.append(allFileSize/1024).append("KB")
						.append("????????????id???"+drafts.getId())
						.append("?????????????????????").append(drafts.getTitle())
						.append("??????????????????url???").append(drafts.getMobileurl());
			//?????????????????????
			if(fileCount > 0)
			{
				opContent.append("???????????????")
				.append(loadFileName.deleteCharAt(loadFileName.length()-1)
				.append("'"));
			}
			//????????????
			if(depIdStr != null && depIdStr.length()>1)
			{
				opContent.append("???DEPID???")
				.append(depIdStr.substring(1, depIdStr.length()-1));
			}
			//????????????
			if(groupStr != null && groupStr.length() > 1)
			{
				opContent.append("?????????ID???")
				.append(groupStr.substring(1, groupStr.length()-1));
			}
			
			opContent.append("??????????????????").append(preParams.getSubCount())
					.append("???????????????").append(preParams.getEffCount())
					.append("???taskId???").append(taskId);
			
			EmpExecutionContext.info("??????????????????", lgcorpcode, strlguserid, lfSysuser.getUserName(), opContent.toString(), "OTHER");
		} 
		catch (EMPException empex)
		{
			ErrorCodeInfo info = ErrorCodeInfo.getInstance();
			//???????????????????????????
			String message = empex.getMessage();
			//?????????????????????????????????
			String desc = "";
			//excel??????????????????
			if(message.indexOf("B20038") == 0)
			{
				desc = String.format(info.getErrorInfo(IErrorCode.B20038), message.substring(6));
			}
			//zip???????????????????????????
			else if(message.indexOf("B20039") == 0)
			{
				desc = String.format(info.getErrorInfo(IErrorCode.B20039), message.substring(6));
			}
			else
			{
				desc = info.getErrorInfo(message);
			}
			//?????????????????????????????????
			result = desc;
			EmpExecutionContext.error(empex, lguserid, lgcorpcode);
			EmpExecutionContext.logRequestUrl(request, "????????????");
		} 
		catch (Exception ex)
		{
			result = "error";
			EmpExecutionContext.error(ex, lguserid, lgcorpcode);
			EmpExecutionContext.logRequestUrl(request, "????????????");
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
