package com.montnets.emp.samesms.servlet;

import com.montnets.emp.common.atom.SendSmsAtom;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.sms.LfBigFile;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.query.biz.SysMtRecordBiz;
import com.montnets.emp.samesms.biz.BigFileManagerBiz;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("serial")
public class ssm_bigFileExportSvt extends BaseServlet{
	
	BaseBiz baseBiz = new BaseBiz();

	private WgMsgConfigBiz msgConfigBiz = new WgMsgConfigBiz();

	private TxtFileUtil txtFileUtil = new TxtFileUtil();
	private SendSmsAtom smsAtom = new SendSmsAtom();
	// 模块
	String opModule = "超大文件导入";
	// 操作员
	String opSper = StaticValue.OPSPER;
	// 类型
	String opType = null;
	// 操作内容
	String opContent = null;
	String opUser = "";
	SuperOpLog spLog = new SuperOpLog();

	private String empRoot = "dxzs";

	private String path = txtFileUtil.getWebRoot();
	private String basePath = "/samesms";
	
	// 模板路径
	protected String excelPath = path + empRoot + basePath + "/file/";
	
	public void find(HttpServletRequest request, HttpServletResponse response) {


		String requestPath = request.getRequestURI();
		String actionPath=requestPath.substring(requestPath.lastIndexOf("/")+1, requestPath.length());
		
		//当前登录用户的企业编码
		String corpCode = "";
		String  userId = "";
		String busIds = "";
		//登录操作员信息
		LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		try {
			
			//当前登录用户的企业编码
			corpCode =loginSysuser.getCorpCode();
			userId =String.valueOf(loginSysuser.getUserId());
			if(corpCode==null||"".equals(corpCode.trim())||"undefined".equals(corpCode.trim())||userId==null||"".equals(userId.trim())||"undefined".equals(userId.trim())){
				userId=String.valueOf(loginSysuser.getUserId());
				corpCode=loginSysuser.getCorpCode();
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", "0," + corpCode);
			orconp.put("corpCode", "asc");
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, orconp);
			
			//拼装业务ids
			if(busList!=null||busList.size()>0){
				for (int i = 0; i < busList.size(); i++) {
					if(i==0){
						busIds = String.valueOf(busList.get(i).getBusId());
					}else{
						busIds = busIds +","+String.valueOf(busList.get(i).getBusId());
					}
				}
			}
			
			//System.out.println("busIds:"+busIds);
			
			request.setAttribute("busIds", busIds);
			request.setAttribute("busList", busList);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取企业编码失败,"+e.getMessage());
		}
		
			
		List<LfBigFile> lfBigFiles = new ArrayList<LfBigFile>();

		// 查询条件封装对象
		LfBigFile lfBigFile = new LfBigFile();
		
		//文件批次
		String pcid = request.getParameter("pcid");
		//操作员
		String userName = request.getParameter("userName");
		//处理状态
		String handleStatus = request.getParameter("handleStatus");
		//文件状态
		String fileStatus = request.getParameter("fileStatus");
		//开始时间
		String stime = request.getParameter("sendtime");
		//结束时间
		String etime = request.getParameter("recvtime");
		
		

		// 分页控件
		PageInfo pageInfo = new PageInfo();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		boolean isFirstEnter;
		try {
			// 当前登录操作员对象
//			LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
//			LfDep lfdep = null;
//			lfdep = baseBiz.getById(LfDep.class, curSysuser.getDepId());
//			opUser = curSysuser.getUserName();
			isFirstEnter = pageSet(pageInfo, request);
			
			

			if (!isFirstEnter) {
				//fileName = new String(fileName.getBytes("iso-8859-1"),"utf-8");
				if(pcid!=null&&!"".equals(pcid)){
					conditionMap.put("id", pcid);
				}
				
				if(userName!=null&&!"".equals(userName)){
					conditionMap.put("userName", userName);
				}
				
				if(handleStatus!=null&&!"".equals(handleStatus)){
					conditionMap.put("handleStatus", handleStatus);
				}
				
				if(fileStatus!=null&&!"".equals(fileStatus)){
					conditionMap.put("fileStatus", fileStatus);
				}

				if (stime != null && !"".equals(stime) && !"null".equals(stime)) {
					conditionMap.put("createTime&>=", stime);
				}
				if (etime != null && !"".equals(etime) && !"null".equals(etime)) {
					conditionMap.put("createTime&<=", etime);
				}
			}
			
//			if (!"100000".equals(corpCode)) {
//				conditionMap.put("corpCode", corpCode);
//				if (loginSysuser.getPermissionType() == 1) {
//					conditionMap.put("userId", loginSysuser.getUserId().toString());
//				}else {
//					conditionMap.put("depId", loginSysuser.getDepId().toString());
//				}
//			}
			if (!"100000".equals(corpCode)) {
				conditionMap.put("corpCode", corpCode);
				 if (loginSysuser.getPermissionType() == 1) {
		                conditionMap.put("userId", loginSysuser.getUserId().toString());
		            }else {
		                //设置有权限看的操作员编码。不需要考虑权限则是空字符串，目前只有admin和管辖范围是顶级机构不需要考虑权限
		                String usercode =  new SysMtRecordBiz().getPermissionUserId(loginSysuser);
		                
		                //1000个人以上，或者查询异常
		                if(usercode == null)
		                {
		                    conditionMap.put("userId", loginSysuser.getUserId().toString());
		                }
		                else if("".equals(usercode))
		                {
		                    //admin和管辖范围是顶级机构不需要考虑权限
		                }
		                else
		                {
		                    conditionMap.put("userId&in", usercode.replace("'", ""));
		                }
		            }
			}
			 
			orderbyMap.put("updateTime", "DESC");
			
			//chaxun超大文件
			lfBigFiles = baseBiz.getByCondition(LfBigFile.class, null, conditionMap, orderbyMap, pageInfo);


//			request.setAttribute("lguserid", lguserid);
//			request.setAttribute("depid", curSysuser.getDepId().toString());
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lfBigFiles", lfBigFiles);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("actionPath", actionPath);
						
			request.getRequestDispatcher(this.empRoot + "/samesms/ssm_bigFileExport.jsp").forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e.getMessage());
		}
	}
	
	//changeFileStatus
	public void changeFileStatus(HttpServletRequest request, HttpServletResponse response) {

		// 查询条件封装对象
		LfBigFile lfBigFile = new LfBigFile();			

		String id = request.getParameter("id");
		//文件状态
		String fileStatus = request.getParameter("fileStatus");
		
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try {
			
			if(fileStatus!=null&&!"".equals(fileStatus)){
				conditionMap.put("fileStatus", fileStatus);
			}

			lfBigFile = baseBiz.getById(LfBigFile.class, id);
			if(fileStatus!=null&&"1".equals(fileStatus)){
				response.getWriter().write("已禁用");
				lfBigFile.setFileStatus(2);
			}
			if(fileStatus!=null&&"2".equals(fileStatus)){
				response.getWriter().write("已启用");
				lfBigFile.setFileStatus(1);
			}
			boolean flag = baseBiz.updateObj(lfBigFile);

			

			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"更改文件状态失败,"+e.getMessage());
		}
	}
	
	public void uploadFile(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		String result = "uploadFail";
		// 预览参数传递变量类
		PreviewParams preParams = new PreviewParams();
		//所有上传文件流集合
		List<BufferedReader> readerList = new ArrayList<BufferedReader>();
		//操作员id
		String strlguserid = null;
		LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
		strlguserid = sysuser.getUserId().toString();
		String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
		try {
			String taskId = new CommonBiz().getAvailableTaskId().toString();
			//设置文件名参数
			String[] fileNameparam = {taskId};
			// 获取号码文件url
			String[] phoneFilePath = txtFileUtil.getSaveFileUrl(sysuser.getUserId(), fileNameparam);
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
				upload.setSizeMax(1000 * 1024 * 1024);
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
			while (it.hasNext())
			{
				fileItem = (FileItem) it.next();
				// 获取上传号码文件
				if (!fileItem.isFormField() && fileItem.getName().length() > 0)
				{
					//文件大小累加
					allFileSize += fileItem.getSize();
					
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
			//上传文件名
			StringBuilder loadFileName= new StringBuilder("'");
			if (fileItemsList.isEmpty()){
				result = "noFile";
				return;
			}
			// 循环解析每个上传文件对象，获取文本文件流集合
			for (FileItem fileItem1 : fileItemsList) {
				// 上传文件转换为文本文件流
				readerList.addAll(smsAtom.parseFile(fileItem1,
						phoneFilePath[0], fileCount, preParams));
				fileCount++;
				loadFileName.append(fileItem1.getName()).append("、");
			}
			//查询业务类型
			LfBusManager busManager = baseBiz.getById(LfBusManager.class, fieldInfo.get("busId"));
			if (busManager == null) {
				result = "noBus";
				return;
			}
			Timestamp time = new Timestamp(System.currentTimeMillis());
			//创建超大文件对象
			LfBigFile bigFile = new LfBigFile();
			bigFile.setFileName(fieldInfo.get("creFileName"));
			bigFile.setHandleStatus(1);
			bigFile.setFileStatus(1);
			bigFile.setUploadNum(fileItemsList.size());
			bigFile.setUserId(sysuser.getUserId());
			bigFile.setUserName(sysuser.getUserName());
			bigFile.setDepId(sysuser.getDepId());
			bigFile.setHandleNode(StaticValue.getServerNumber());
			bigFile.setCorpCode(sysuser.getCorpCode());
			bigFile.setCreateTime(time);
			bigFile.setBusCode(busManager.getBusCode());
			bigFile.setBusId(busManager.getBusId().longValue());
			bigFile.setBusName(busManager.getBusName());
			bigFile.setSubCount(0L);
			bigFile.setEffCount(0L);
			bigFile.setBlaCount(0L);
			bigFile.setRepCount(0L);
			bigFile.setErrCount(0L);
			bigFile.setOprNum(" ");
			bigFile.setTaskId(" ");
			bigFile.setEffNum(0);
			bigFile.setUpdateTime(time);
			bigFile.setFileUrl(" ");
			bigFile.setFileUrl2(" ");
			bigFile.setFileUrl3(" ");
			bigFile.setFileUrl4(" ");
			bigFile.setFileUrl5(" ");
			bigFile.setFileUrl6(" ");
			bigFile.setFileUrl7(" ");
			bigFile.setFileUrl8(" ");
			bigFile.setFileUrl9(" ");
			bigFile.setBadUrl(" ");
			bigFile.setViewUrl(" ");
			bigFile.setRemark(" ");
			Long bigFileId = new BaseBiz().addObjReturnId(bigFile);
			
			//判断新建超大文件信息是否成功
			if (bigFileId>0L) {
				result = "fileSuccess";
				bigFile.setId(bigFileId);
				bigFile.setRemark(" ");
			}else {
				return ;
			}
			//创建超大文件拆分任务
			boolean flag = BigFileManagerBiz.getTMInstance().execute(bigFile,readerList,preParams,langName);
			if(flag){
				result = "fileSuccess";
			}else {
				bigFile.setHandleStatus(3);
				bigFile.setRemark("文件拆分线程调用失败");
				baseBiz.updateObj(bigFile);
			}
		} catch (EMPException empex){
			ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
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
			EmpExecutionContext.error(empex, sysuser.getUserId(), sysuser.getCorpCode());
			EmpExecutionContext.logRequestUrl(request, "后台请求");
		}catch (Exception e) {
			EmpExecutionContext.error(e, "超大文件上传异常");
			result = "Fail";
		}finally{
			try {
				request.setAttribute("result", result);
				toExport(request, response);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "超大文件上传跳转异常");
			} 
		}
	}
	
	
	//进入上传文件界面
	public void toExport(HttpServletRequest request, HttpServletResponse response) {
		List<LfBusManager> busList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
				
		try {
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
	            conditionMap.put("corpCode&in", "0," + sysuser.getCorpCode());
	            orconp.put("corpCode", "asc");

	            // 设置启用查询条件
	            conditionMap.put("state", "0");
	            // 设置查询手动和手动+触发
	            conditionMap.put("busType&in", "0,2");

	            // 根据企业编码查询业务类型
	             busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, orconp);
				request.setAttribute("busList", busList);
				request.getRequestDispatcher(this.empRoot + "/samesms/toExport.jsp").forward(request, response);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"进入导出界面跳转失败,"+e.getMessage());
			}
		}
}
