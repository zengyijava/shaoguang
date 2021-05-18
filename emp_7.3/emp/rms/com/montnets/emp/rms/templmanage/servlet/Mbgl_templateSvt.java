package com.montnets.emp.rms.templmanage.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.template.MmsTemplate;
import com.montnets.emp.rms.commontempl.biz.CommonTemplateBiz;
import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.commontempl.servlet.CommonTemplateSvt;
import com.montnets.emp.rms.dao.UserDataDAO;
import com.montnets.emp.rms.rmsapi.biz.RMSApiBiz;
import com.montnets.emp.rms.rmsapi.biz.impl.IRMSApiBiz;
import com.montnets.emp.rms.rmsapi.model.TempParams;
import com.montnets.emp.rms.rmsapi.util.DegreeCountUtil;
import com.montnets.emp.rms.rmsapi.util.FileNameBytesUtil;
import com.montnets.emp.rms.rmsapi.util.OOSUtil;
import com.montnets.emp.rms.rmsapi.util.ZipCompress;
import com.montnets.emp.rms.servmodule.constant.ServerInof;
import com.montnets.emp.rms.templmanage.biz.MbglTemplateBiz;
import com.montnets.emp.rms.templmanage.biz.RmsShortTemplateBiz;
import com.montnets.emp.rms.tools.AnalysisRMS;
import com.montnets.emp.rms.tools.GetAndReadAllFile;
import com.montnets.emp.rms.tools.HTMLTool;
import com.montnets.emp.rms.tools.RmsEntity;
import com.montnets.emp.rms.tools.ZipTool;
import com.montnets.emp.rms.tools.ZipUtil;
import com.montnets.emp.rms.vo.LfShortTemplateVo;
import com.montnets.emp.rms.vo.LfTemplateChartVo;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.rms.vo.UserDataVO;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;

@SuppressWarnings("serial")
public class Mbgl_templateSvt extends  BaseServlet {
	private final String PATH = "rms/mbgl";
	// 操作模块
	final String opModule = StaticValue.TEMP_MANAGE;
	// 操作用户
	final String opSper = StaticValue.OPSPER;
	private final MbglTemplateBiz mtlBiz = new MbglTemplateBiz();
	public static String dirUrl = null;
	final BaseBiz baseBiz = new BaseBiz();
	final SuperOpLog spLog = new SuperOpLog();
	static boolean  globalFlag = false;
	private final HTMLTool htmlTool =  new HTMLTool();
	private final OOSUtil oosUtil = new OOSUtil();
	//rms 模板路径
	private static final String RMSTEMPLATEPATH = "file/rms/templates/";
	
	private final KeyWordAtom keyWordAtom = new KeyWordAtom();
	private final CommonTemplateBiz commonTemplateBiz = new CommonTemplateBiz();
	static Map<String, String> infoMap = new HashMap<String, String>();
	final CommonBiz commBiz = new CommonBiz();
	static {
		infoMap.put("1", "短信模板");
		infoMap.put("2", "彩信模板");
		infoMap.put("3", "网讯模板");
	}

	
	public void find(HttpServletRequest request, HttpServletResponse response) {
		String lgguid = "";
		Long userGuid = null;
		Long lguserId = null;
		LfSysuser sysUser = null;
		// 操作员的GUID
		lgguid = request.getParameter("lgguid");
		// 企业编码
		String lgcorpcode = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getCorpCode();
		
		try {
			if (lgguid == null || "".equals(lgguid.trim())
					|| "undefined".equals(lgguid.trim())) {
				EmpExecutionContext.error("彩信模板编辑获取lgguid参数异常！lgguid=" + lgguid
						+ "。改成从Session获取。");
				LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
						.getAttribute(StaticValue.SESSION_USER_KEY);
				lgguid = String.valueOf(loginSysuser.getGuId());
			}
			userGuid = Long.valueOf(lgguid);
			// 获取操作员的对象
			sysUser = baseBiz.getByGuId(LfSysuser.class, userGuid);
			// 获取操作员的用户ID
			lguserId = sysUser.getUserId();
		} catch (Exception e) {
			// 进入异常
			EmpExecutionContext.error(e, "彩信模板获取当前操作员出现异常！");
		}
		List<LfTemplateVo> mmsList = null;
		LfTemplateVo mt = new LfTemplateVo();
		TxtFileUtil txtfileutil = new TxtFileUtil();
		dirUrl = txtfileutil.getWebRoot();
		PageInfo pageInfo = new PageInfo();
		// 日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time = System.currentTimeMillis();
		try {
			if(!StringUtils.IsNullOrEmpty(lgcorpcode) && !"100000".equals(lgcorpcode)){
				mt.setCorpCode(lgcorpcode);
			}
			// 是否第一次打开
			boolean isFirstEnter = false;
			isFirstEnter = pageSet(pageInfo, request);
			if (!isFirstEnter) {
				// 主题
				String theme = request.getParameter("theme");
				// 模板ID
				String spTempID = request.getParameter("tmCode");

				// 操作员名称
				String userName = request.getParameter("userName");
				// 审批状态
				String rstate = request.getParameter("rState");
				// 模板状态
				String state = request.getParameter("state");
				// 模板类型
				String dsFlag = request.getParameter("dsFlag");
				// 开始时间
				String startTm = request.getParameter("submitSartTime");
				// 结束时间
				String endTm = request.getParameter("submitEndTime");
				//企业ID
				String corpCode = request.getParameter("corpCode");
				
				if (theme != null && !"".equals(theme)) {
					theme = theme.replace("'", "");
					mt.setTmName(theme);
				}
				if (spTempID != null && !"".equals(spTempID)) {
					spTempID = spTempID.trim();
					mt.setSptemplid(Long.parseLong(spTempID));
				}
				if (userName != null && !"".equals(userName)) {
					mt.setName(userName);
				}
				if (rstate != null && !"".equals(rstate)) {
					mt.setIsPass(Integer.parseInt(rstate));
				}
				if (state != null && !"".equals(state)) {
					mt.setTmState(Long.parseLong(state));
				}
				if (dsFlag != null && !"".equals(dsFlag)) {
					mt.setDsflag(Long.parseLong(dsFlag));
				}
				String auditStatus = request.getParameter("auditStatus");
				if (auditStatus != null && !"".equals(auditStatus)) {
					mt.setAuditstatus(Integer.parseInt(auditStatus));
				}
				String submitstatus = request.getParameter("submitstatus");
				if (submitstatus != null && !"".equals(submitstatus)) {
					mt.setSubmitstatus(Integer.parseInt(submitstatus));
				}
				if (startTm != null && !"".equals(startTm)) {
					mt.setAddStartm(startTm);
				}
				if (endTm != null && !"".equals(endTm)) {
					mt.setAddEndtm(endTm);
				}
				if(!StringUtils.IsNullOrEmpty(corpCode)){
					mt.setCorpCode(corpCode);//指定查询企业ID
				}else if(!"100000".equals(lgcorpcode)){
					mt.setCorpCode(lgcorpcode);//登录企业ID
				}
			}

			// 模板（3-短信模板;4-彩信模板;11-富信模板）
			mt.setTmpType(11);
			//是否公共模板 0-不是，1-是
			mt.setIsPublic(0);
			// 审核流程id
			String flowIdStr = request.getParameter("flowId");
			if (flowIdStr != null && !"".equals(flowIdStr)) {
				mt.setFlowID(Long.valueOf(flowIdStr));
			}
			//设置每页9条数据
			pageInfo.setPageSize(9);
			 
			LfShortTemplateVo lv = new LfShortTemplateVo();
			lv.setCorpCode(lgcorpcode);
			//快捷场景集合
			List<LfShortTemplateVo> lfShortTemList = new RmsShortTemplateBiz().getLfShortTemList(lv);
			
			mmsList = mtlBiz.getTemplateByCondition(lguserId, mt, pageInfo);
			//设置快捷场景标识
			for(LfTemplateVo lf : mmsList){
				for(LfShortTemplateVo sv : lfShortTemList){
					if(sv.getTempId().equals(lf.getTmid())){
						//1为快捷场景
						lf.setIsShortTemp(1);
					}
				}
			}
			//下载第一帧用于预览
			downFirstFrame(request, mmsList);
			// 设置服务器名称
			new ServerInof().setServerName(getServletContext().getServerInfo());
			request.setAttribute("mmsVo", mt);
			request.setAttribute("mmsList", mmsList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("findresult", "");
			// 增加查询日志
			if (pageInfo != null) {
				long end_time = System.currentTimeMillis();
				String opContent = "查询开始时间：" + format.format(begin_time)
						+ ",耗时:" + (end_time - begin_time) + "毫秒，数量："
						+ pageInfo.getTotalRec();
				opSucLog(request, "富信模板", opContent, "GET");
			}
			
			  //设置服务器名称
            new ServerInof().setServerName(getServletContext().getServerInfo());
			request.getRequestDispatcher(PATH + "/myScene.jsp").forward(
					request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "富信模板管理跳转失败！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
		}
	}

	// 跳转到新增页面
	public void doAdd(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 查询有没有彩信账号
			if (StaticValue.getCORPTYPE() == 1) {
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap
						.put("corpCode", request.getParameter("lgcorpcode"));
				List<LfMmsAccbind> mmsaccs = baseBiz.getByCondition(
						LfMmsAccbind.class, conditionMap, null);
				if (mmsaccs != null && mmsaccs.size() > 0) {
					request.setAttribute("mmsacc", "true");
				} else {
					request.setAttribute("mmsacc", "false");
				}
			} else {
				request.setAttribute("mmsacc", "true");
			}
			
			DegreeCountUtil countUtil =  DegreeCountUtil.getInstance();
			Map<String, String> queryDegree = countUtil.queryDegree();
//			Map<String, String> queryDegree = new HashMap<String, String>();
			// 设置服务器名称
			String json = JSON.toJSONString(queryDegree);
			new ServerInof().setServerName(getServletContext().getServerInfo());
			
			//网关参数大小:默认2KB=2048B
			String paramSize =SystemGlobals.getValue("montnets.rms.mw.paramsize");
			if(StringUtils.IsNullOrEmpty(paramSize)){
				paramSize ="2048";
			}
			//rms 文件大小:默认：19M =1992294B
			String maxSize =SystemGlobals.getValue("montnets.rms.maxsize");
			if(StringUtils.IsNullOrEmpty(paramSize)){
				maxSize ="1992294";
			}
			//各资源文件标签大小  total:par:text:img:audio:vedio=355:35:45:45:48:48
			String smilFileSize =SystemGlobals.getValue("montnets.rms.smilfile.size");
			if(StringUtils.IsNullOrEmpty(paramSize)){
				smilFileSize ="355:35:45:45:48:48";
			}
			
			Map<String, List<LfIndustryUse>> induUseMap = null;
			List<LfIndustryUse> industryList = null;
			List<LfIndustryUse> useList = null;
			
			//行业-用途列表
			induUseMap = getIndustryUseList(request,response);
			industryList = induUseMap.get("industry");
			useList = induUseMap.get("use");
			
			String source = request.getParameter("source");
			if(!StringUtils.IsNullOrEmpty(source)){//从公共模板跳转过来才有此参数
				request.setAttribute("source",source);
			}
			
			request.setAttribute("industryList",industryList);
			request.setAttribute("useList",useList);
			
			
			request.setAttribute("deGreeMap", json);
			request.setAttribute("paramSize", paramSize);
			request.setAttribute("maxSize", maxSize);
			request.setAttribute("smilFileSize", smilFileSize);
			
			request.getRequestDispatcher(PATH + "/mbgl_addTemplate.jsp").forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "跳转新增富信模板页面出现异常！");
		}
	}

	public void update(HttpServletRequest request, HttpServletResponse response) {
		//记录执行时间
		long btime = (new Date()).getTime();
		
		//定义页面返回值
		boolean result = true;
		StringBuffer resultMsg =  new StringBuffer();
		//模板自增ID
		long tmId = 0L;
		//操作判断
		String operType  = request.getParameter("opType");
		long optypeL = 0L;
		//更新、新建标识：true：新建，false:编辑、复制
		boolean boolFlag = false;
		TxtFileUtil txtfileutil = new TxtFileUtil();
		dirUrl = txtfileutil.getWebRoot();
		try {
			
			// 获取操作员的用户ID
			Long lguserId = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getUserId();
			String lgcorpcode = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getCorpCode();
			
			//富信主题
			String tmName = request.getParameter("tmThame"); 
			if(null == tmName || "".equals(tmName)){
				tmName = "";
			}
			//模板HTML
			String htmlContent = request.getParameter("fxHtml");
			
			if(!StringUtils.IsNullOrEmpty(operType)){
				optypeL = Long.parseLong(operType);
			}
			String tempPath = request.getParameter("tempPath");
			
			String tmIdStr = request.getParameter("tmId");
			
			//档位
			int degree = 0;
			if(!StringUtils.IsNullOrEmpty(request.getParameter("degree"))){
				degree = Integer.parseInt(request.getParameter("degree"));
			}
			
			//容量
			int degreeSize = 0;
			if(!StringUtils.IsNullOrEmpty(request.getParameter("degreeSize"))){
				degreeSize = Integer.parseInt(request.getParameter("degreeSize"));
			}
			
			//参数个数
			int parmLegth = 0;
			if(!StringUtils.IsNullOrEmpty(htmlContent)){
				parmLegth = getHtmlParamCount(htmlContent);
			}
			
			//行业ID
			int industryID = -1;//默认-1
			if(!StringUtils.IsNullOrEmpty(request.getParameter("industryID"))){
				industryID = Integer.parseInt(request.getParameter("industryID"));
			}
			//用途ID
			int useID = -2; //默认-2
			if(!StringUtils.IsNullOrEmpty(request.getParameter("useID"))){
				useID = Integer.parseInt(request.getParameter("useID"));
			}
			
			//我的场景-公共场景判断 ： source == 'commontemplate'为公共场景  
			String source  = request.getParameter("source");
			//是否是公共模板 0为不是 1为是
			int isPublic = 0;
			if(!StringUtils.IsNullOrEmpty(source)){
				if(source.equals("commontemplate")){
					isPublic = 1;
				}
			}
			
			if(result){
				if(StringUtils.IsNullOrEmpty(tmIdStr)){//新建
					//1.插入一条记录，生成模板id
					tmId= addTemplate(optypeL,tmName, lguserId, lgcorpcode,parmLegth,isPublic);
					boolFlag = true;
					if(tmId<=0){
						result = false;
						resultMsg.append("增加富信模板记录失败！");
						EmpExecutionContext.error("增加富信模板记录失败！");
					}
				}else{//更新操作
					tmId = Long.parseLong(tmIdStr);
				}
			}
			
			SimpleDateFormat smf = new SimpleDateFormat("yyyy/MM/dd/");
			String filePath = "";
			//阿里云路径
			String aliPath  = "";
			
			
			if(StringUtils.IsNullOrEmpty(htmlContent)){
				result = false;
				resultMsg.append("请编辑富信模板内容！");
				EmpExecutionContext.error("请编辑富信模板内容！");
			}
			//2.根据页面获取的html生成smil
			Map<String, Object> htmlMap =new HashMap<String, Object>();
			if(result){
				//组装文件路径
				if(boolFlag){//新建
					filePath  = dirUrl +RMSTEMPLATEPATH +lgcorpcode+"/";
					aliPath = filePath;
					filePath = filePath + tmId+"/";
					htmlMap = htmlTool.htmlHandle(dirUrl,filePath,htmlContent);
				}else{//修改
					filePath  = dirUrl + tempPath.replace("fuxin.rms", "");
					aliPath = filePath.replace(tmId+"/", "");
					htmlMap = htmlTool.htmlHandle(dirUrl,filePath,htmlContent);
				}	
				
				if(!(Boolean)htmlMap.get("result")){
					result = false;
					resultMsg.append("生成rms文件失败！");
					EmpExecutionContext.error("生成rms文件失败！");
				}
			}
			
			//------校验是否存关键字---
			if(null !=htmlMap && (Boolean)htmlMap.get("result")){
				StringBuffer context  = new StringBuffer();
				context.append(tmName);
				context.append(htmlMap.get("text"));
				String keyWords = checkBadWord(context.toString().trim().replace("\r\n", "").replace(" ", ""), lgcorpcode);
				if(!keyWords.equals("")){//内容含有关键字
					result = false;
					resultMsg.append("您编辑的内容触发关键字,["+keyWords+"]");
				}
			}
			
			
			//先判断本地是否存在文件，如不存在，则从阿里云下载，如阿里云也不存在，弹出提示框
//			if(!boolFlag){//编辑
//				result = downLoadRmsFromOss(request,filePath,String.valueOf(tmId));
//				if(!result){
//					result = false;
//					resultMsg.append("rms 文件不存在");
//					EmpExecutionContext.error("rms 文件不存在");
//				}
//				
//			}
//			
			//3.生成rms文件
			byte[] bytes = null;
			if(result){
				 File file  = null;
				 @SuppressWarnings("unchecked")
					List<Map<String,String>> list = (List<Map<String,String>>)htmlMap.get("list");
					 bytes = getRmsFileBytes(filePath+"/src",tmName, list);
				 if(bytes != null){
					  file =  writeFile(filePath+"fuxin.rms", bytes);
				 }
				 
				 if(file==null){
					 result = false;
					 resultMsg.append("解析rms文件，生成二进制流失败！");
					 EmpExecutionContext.error("解析rms文件，生成二进制流失败！");
				 }
			}
			
			//将模板ID下的所有文件压缩
			ZipTool.createZip(filePath, filePath.substring(0, filePath.lastIndexOf("/"))+".zip");
			File remoteUrl = new File(filePath.substring(0, filePath.lastIndexOf("/"))+".zip");
			//新增逻辑--将压缩文件上传文件服务器
			Boolean  upFileCenterFlag  = uploadFileCenter(aliPath.replace(dirUrl, "")+tmId+".zip");
			//4.将rms文件上传阿里云
			//2018-04-11：现在将模板ID下的所有文件打包压缩上传阿里云
			if( upFileCenterFlag  && result &&  null != oosUtil.getOssClient()){
//				ZipCompress zipCom = new ZipCompress(filePath+"/fuxin.zip",filePath+"/fuxin.rms");
//				zipCom.zip();
				if(!oosUtil.uploadFile(aliPath.replace(dirUrl, "")+tmId+"/",remoteUrl)){
					 result = false;
					 resultMsg.append("备份富信文档到远程服务器失败，请稍后再试！");
					 EmpExecutionContext.error("备份富信文档到远程服务器失败，请稍后再试！");
				}
			}
			
			//5.将rms文件上传网关
			String empTmId ="0";//默认0是防止后面Long.parseLong 转换异常
			if(result && optypeL == 1L){
				if(bytes != null && optypeL == 1L){//1为启用,2为草稿的时候不提交网关
					int degreeSize1 = degreeSize * 1024;
					empTmId = submGwCenter(bytes,lgcorpcode,degree,degreeSize1,parmLegth);
				}
				
				if(StringUtils.IsNullOrEmpty(empTmId)){
					 result = false;
					 resultMsg.append("上传富信模板到审核中心失败，请稍后再试！");
					 EmpExecutionContext.error("上传富信模板到审核中心失败，请稍后再试！");
				}
			}
			
			//6.根据模板id更新模板记录
			if(result){
				// 更新 入库 EXTJSON 字段
				new CommonTemplateSvt().downPhoneFileV1(filePath.replace(dirUrl, "")+"fuxin.rms",String.valueOf(tmId));

				boolean updaFlag =  updateTemplate(tmId,filePath.replace(dirUrl, "")+"fuxin.rms",tmName,empTmId,degree,degreeSize,parmLegth,optypeL,industryID,useID);
				if(!updaFlag){
					 result = false;
					 resultMsg.append("富信模板保存数据库失败，请稍后再试！");
					 EmpExecutionContext.error("富信模板保存数据库失败，请稍后再试！");
				}
			}
			 
			
			//8.记录操作日志
//		if(updaFlag){
//			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
//			if (loginSysuserObj != null) {
//				LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
//				String contnet = opContent + "成功。[模板名称，模板类型，模板状态]("
//						+ oldlmt.getTmName() + "，"
//						+ oldlmt.getDsflag() + "，"
//						+ oldlmt.getTmState() + ")" + "-->("
//						+ lmt.getTmName() + "，" + lmt.getDsflag()
//						+ "，" + lmt.getTmState() + ")";
//				EmpExecutionContext.info("模板编辑",
//						loginSysuser.getCorpCode(),
//						String.valueOf(loginSysuser.getUserId()),
//						loginSysuser.getUserName(), contnet,
//						"UPDATE");
//			}
//		}
//		
//		if (updaFlag) {
//			spLog.logSuccessString(opUser, opModule, opType,
//					opContent, lgcorpcode);
//			if (lmt.getTmState() == 2L) {
//				response.getWriter().print("caogaotrue");
//			} else {
//				response.getWriter().print("true");
//			}
//		} else {
//			spLog.logFailureString(opUser, opModule, opType,
//					opContent + opSper, null, lgcorpcode);
//			response.getWriter().print("false");
//		}
//			i
			 
		} catch (Exception e) {
			EmpExecutionContext.error(e,"新建富信模板出现异常：");
			if(result){
				 result = false;
				 resultMsg.append("新建富信模板出现异常，请稍后再试！");
			}
		}finally{
			try {
				//7、如果上述步骤执行失败，删除第一步骤时插入的记录
				if(!result && !StringUtils.IsNullOrEmpty(String.valueOf(tmId)) && boolFlag ==true){//boolFlag ==true 为新建
					String[] ids ={String.valueOf(tmId)};
					mtlBiz.delTempByTmId(ids);
				}
				if(result){
					 response.getWriter().print("true");
				 }else{
					 response.getWriter().print(resultMsg);
				 }
				long etime = (new Date()).getTime();
				EmpExecutionContext.info("本次新增富信模板耗时：" +(etime - btime) +" ms" +",tmId:" +tmId);
			} catch (Exception e) {
				EmpExecutionContext.error("创建富信模板出现异常："+e.toString());
			}
		}
		
	}


	/**
	 * 获取报表标题参数个数
	 * @param tmsMsg
	 * @return
	 */
	private int getChartTitleCount(String tmsMsg) {
		String eg = "\\{#标题#\\}";
		Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(tmsMsg);
		int paramCount = 0;
		while (m.find()) {
			paramCount++;
		}
		return paramCount;
	}

	/**
	 * 从阿里云下载rms文件
	 * @param filePath
	 * @return
	 */
	public boolean downLoadRmsFromOss(HttpServletRequest request,String filePath,String tmid,String preview) {
		boolean flag = false;
		TxtFileUtil txtfileutil = new TxtFileUtil();
		dirUrl = txtfileutil.getWebRoot();
		//拼接文件服务器下载zip文件相对地址
		String downFileZipUrl =filePath.replace(dirUrl, "");
		downFileZipUrl = downFileZipUrl.substring(0, downFileZipUrl.length()-1)+".zip";
		String sourcPath = filePath.replace(dirUrl, "");
		String rmsZipName =tmid+".zip";
		//判断文件夹是否存在，存在直接返回，不存在从阿里云下载
		File fDir = new File(filePath);
		if(!fDir.exists()){
			fDir.mkdirs();
		}else{
			File htmlFile = new File(filePath+"fuxin.html");
			File rmsFile = new File(filePath+"fuxin.rms");
			File previewFile = new File(filePath+"fuxinPreview.html");
			if("needPreview".equals(preview)){
				if(htmlFile.exists() && rmsFile.exists()&&previewFile.exists()){
					return true;
				}
			}else{
				if(htmlFile.exists() && rmsFile.exists()){
					return true;
				}
			}
		}
		/**
		 * //解析生成相关资源文件
	    	RmsEntity rEntity = analysisRMS.Parse(filePath+rmsName, filePath+"src/", rmsFileSize);
	    	//生成fuxin.html-用于预览
	    	String srcPath = (filePath+"src/").replace(dirUrl, "");
	    	analysisRMS.createHtml(filePath+"src/", srcPath, rEntity);
		 */
		String destPath = (filePath+"fuxin.rms").replaceAll(tmid+"/fuxin.rms", "");
		
		
		//从文件服务器下载 zip
		long fileStartTm = System.currentTimeMillis();
		String downFileZip = commBiz.downFileFromFileCenWhitZip(downFileZipUrl);
		long fileEndTm = System.currentTimeMillis();
EmpExecutionContext.info(tmid+"：文件服务器下载耗时：" +(fileEndTm - fileStartTm)+" ms");		
		if(!"success".equals(downFileZip)){//文件服务器没有下载成功,则从阿里云下载 zip
			long aliStartTm = System.currentTimeMillis();
			if(oosUtil.downLoadFile(sourcPath,destPath, rmsZipName)){
				long aliEndTm = System.currentTimeMillis();
EmpExecutionContext.info(tmid+"：阿里云下载耗时：" +(aliEndTm - aliStartTm)+" ms");				
				//之前是只上传fuxin.rms 文件到阿里云，需要下载下来后再反解析rms 文件为资源文件，现在直接将源文件和fuxin.rms 文件打包上传，直接下载解压即可
long uzipStartTm = System.currentTimeMillis();
				ZipUtil.unZip((filePath+"fuxin.rms").replaceAll(tmid+"/fuxin.rms", "")+rmsZipName); 
long uzipEndTm = System.currentTimeMillis();
EmpExecutionContext.info(tmid+"：解压耗时：" +(uzipEndTm - uzipStartTm)+" ms");			
			}else{
				return false;
			}
		} 
		
		//反解析rms
		//解析rms文件
		AnalysisRMS aRms = new AnalysisRMS();
		//获取fuxin.rms 文件大小
		File rmsFile = new File(filePath+"fuxin.rms");
		int fileByteSize =(int)rmsFile.length(); 
		//生成fuxin.html-用于预览
		String srcPath = (filePath+"src/").replace(dirUrl, "");
		RmsEntity rsRmsEntity=aRms.Parse(request,filePath+"fuxin.rms", filePath+"src/", fileByteSize,null);
		//aRms.createHtml(filePath+"src/", srcPath, rsRmsEntity);
		flag = true;
		
	     return flag;
	}

	/**
	 * 更新模板
	 * @param tmid
	 * @param filePath
	 * @param tmName
	 * @param empTmId
	 * @param degree
	 * @param degreeSize
	 * @param parmLegth
	 * @param tmState
	 * @return
	 */
	private boolean updateTemplate(long tmid ,String filePath,String tmName,String empTmId,int degree,long degreeSize,int parmLegth,long tmState,int industryId,int useId){
		LfTemplate lmt = new LfTemplate();
		lmt.setTmid(tmid);
		lmt.setTmMsg(filePath);
		//富信主题
		lmt.setTmName(tmName);
		//网关返回的模板ID
		lmt.setSptemplid(Long.parseLong(empTmId));
		lmt.setDegree(degree);
		lmt.setDegreeSize(degreeSize);
		//tmState:2草稿 
		lmt.setSubmitstatus((tmState==2L)?0:1);//1：提交成功，0：未提交
		lmt.setTmState(tmState);//模板状态
		lmt.setParamcnt(parmLegth);
		int paramsNum = 0;
		if(parmLegth > 0){//动态模板
			lmt.setParamcnt(parmLegth);
			lmt.setDsflag(1L);
            paramsNum = 1;
		}else{//静态模板
			lmt.setParamcnt(0);
			// 静态模板0、动态模板1
			lmt.setDsflag(0L);
		}
		lmt.setParamsnum(paramsNum);
		lmt.setIndustryid(industryId);
		lmt.setUseid(useId);
		return mtlBiz.updateTemplate(lmt);
	}
	
	/**
	 * 上传网关
	 * @return
	 */
	public String submGwCenter(byte[] bytes,String loginOrgCode,int degree,int degreeSize,int parmLegth){
		String tmpid = "";
		 RMSApiBiz rmsBiz = new IRMSApiBiz();
		 List<TempParams>list = new ArrayList<TempParams>();
		 TempParams tp = new TempParams();
		 tp.setType(1);//1表示rms文件
		 tp.setDegree(degree);//档位
//		 tp.setPnum(parmLegth);//pnum:表示参数个数,如果是静态模板填0,动态模板填参数个数
		 //根据参数个数判断传给审核中心的参数写何值
		 if(parmLegth > 0){
			 tp.setPnum(1);//pnum:表示参数个数,如果是静态模板填0,动态模板填参数个数 --V2.0改为1 ,所有参数从P1里面获取
		 }else if(parmLegth == 0){//静态模板
			 tp.setPnum(0);
		 }
		 tp.setSize(degreeSize);//表示档位对应的文件大小,不包含参数 ; 档位*1024
		 tp.setContentByte(bytes);
		 list.add(tp);
		 try {
			 // 通过企业编号去查询数据库中可使用的一个账号密码
			 UserDataDAO userDataDAO = new UserDataDAO();
			UserDataVO userData = userDataDAO.getSPUser(loginOrgCode);
			if(null != userData){
					//  将账账号密码设置成数据库中查询出来的值
					Map<String, String> tmplMap = rmsBiz.subTemplate(userData.getUserId().toUpperCase(), userData.getPassWord(), list);
					if((null != tmplMap) &&  (null != tmplMap.get("result"))&& ("0".equals(tmplMap.get("result")))){
						tmpid = tmplMap.get("tmplid");
					}
					globalFlag = true;
			}else{
				globalFlag = false;
				EmpExecutionContext.error("没有发送网关的SP账号.....");
			}
		} catch (Exception e) {
			globalFlag = false;
			EmpExecutionContext.error("数字转换异常:"+e.toString());
		}
		 return tmpid;
		
	}
	

	// 写文件
	private File writeFile(String filePath, byte[] contents) {
		File file = null;
		FileOutputStream fos = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			} else {
				if (file.isFile()) {
                    boolean flag = file.delete();
                    if (!flag) {
                        EmpExecutionContext.error("删除文件失败！");
                    }
				}
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			}

			fos = new FileOutputStream(filePath);
			fos.write(contents, 0, contents.length);
			
		} catch (Exception e) {
			file = null;
			EmpExecutionContext.error(e, "富信模板写文件失败！");
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "文件关闭失败！");
				}
			}
		}
		return file;
	}
	/**
	 * 插入一条模板记录
	 * @param tmName
	 * @param lguserId
	 * @param lgcorpcode
	 * @return
	 */
	private long addTemplate(long optypeL,String tmName,Long lguserId,String lgcorpcode,int parmLegth,int isPublic){
		long result = 0L;
		 
		try {
			LfTemplate lmt = new LfTemplate();
			//富信主题
			lmt.setTmName(tmName);
			lmt.setTmCode(" ");
			// 是否审核(无需审核-0，未审核-1，同意1，拒绝2)
			lmt.setIsPass(-1);
			// 模板状态（0无效，1有效，2草稿）
			lmt.setTmState(optypeL);
			// 添加时间
			lmt.setAddtime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
			// 模板内容 添加的时候还未生成，直接填空字符串
			lmt.setTmMsg(" ");
			// 操作员ID
			lmt.setUserId(lguserId);
			lmt.setCorpCode(lgcorpcode);
			// 模板（3-短信模板;4-彩信模板；11-富信模板）
			lmt.setTmpType(new Integer("11"));
			int paramsnum = 0;
			if(parmLegth > 0){
				//动态模板
				lmt.setDsflag(1L);
				lmt.setParamcnt(parmLegth);
                paramsnum = 1;
			}else{
				//静态模板
				lmt.setParamcnt(0);
				// 静态模板0、动态模板1
				lmt.setDsflag(0L);
			}
			lmt.setParamsnum(paramsnum);
			lmt.setIndustryid(-1);
			lmt.setUseid(-2);
			lmt.setIsPublic(isPublic);
			lmt.setUsecount(0L);
			lmt.setExlJson(" ");
			// 网关审核状态 -1：未审批，0：未审核，1：同意，2：拒绝，3：审核中
			lmt.setAuditstatus(-1);
			// 网关彩信模板状态
			lmt.setTmplstatus(0);
			//rms文件版本号
			lmt.setVer("V2.0");
			lmt.setEmptemplid(getEmpTemplateLid());
			lmt.setSubmitstatus(0);
			// 网关平台彩信模板对象
			MmsTemplate mmstl = new MmsTemplate();
			// 创建人
			mmstl.setUserId(lguserId.toString());
			// 审核状态
			mmstl.setAuditStatus(0);
			// 参数个数
			mmstl.setParamCnt(0);
			// 模板状态
			mmstl.setTmplStatus(0);
			// 文件路径
			mmstl.setTmplPath(" ");
			// 模板接收时间
			mmstl.setRecvTime(lmt.getAddtime());
			mmstl.setAuditor(" ");
			mmstl.setAuditTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
			mmstl.setRemarks(" ");
			mmstl.setEmptemplid(getEmpTemplateLid());
			mmstl.setTmplId(0L);
			mmstl.setSubmitstatus(0);
			mmstl.setReServe1("0");
			mmstl.setReServe2("0");
			mmstl.setReServe3("0");
			mmstl.setReServe4("0");
			mmstl.setReServe5("0");

		    result = mtlBiz.addTemplate(mmstl, lmt);
		} catch (NumberFormatException e) {
			EmpExecutionContext.error(e, "数字转换异常！");
		} catch (Exception e) {
			EmpExecutionContext.error(e, "富信模板插入出现异常！");
		}
		return result;
	}

	// 删除
	@SuppressWarnings("unused")
	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String opUser = ((LfSysuser) request.getSession(false).getAttribute(
				StaticValue.SESSION_USER_KEY)).getUserName();
		String opType = null;
		String opContent = null;
		opType = StaticValue.DELETE;

		String corpcode = request.getParameter("lgcorpcode");
		try {
			// 1.删除审核中心中的模板（调用接口）-->改为不删除
			String tempIds = "";
			RMSApiBiz rmsBiz = new IRMSApiBiz();
//			Map<String, String> deleteTemplates = rmsBiz
//					.deleteTemplates("SZ8541","SZ8541",tempIds);

			// 2.删除阿里云模板（调用接口）-->改为不删除
			

			// 3.删除数据库中存储的模板路径

			// 获取需要删除的模板ID
			String[] ids = request.getParameter("ids").toString().split(",");

			// 查询删除的彩信模板集合
			String idsStr = request.getParameter("ids").toString();
			LinkedHashMap<String, String> logConditionMap = new LinkedHashMap<String, String>();
			logConditionMap.put("tmid&" + StaticValue.IN, idsStr);
			LinkedHashMap<String, String> logOrderMap = new LinkedHashMap<String, String>();
			logOrderMap.put("tmid", StaticValue.ASC);
			List<LfTemplate> deleteTemplateList = new BaseBiz().getByCondition(
					LfTemplate.class, logConditionMap, logOrderMap);

			int count = mtlBiz.delTempByTmId(ids);
			opContent = "删除" + ids.length + "条富信模板";

			// 增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);
			if (loginSysuserObj != null) {
				LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
				// 增加操作日志
				String deleteMsg = "";
				for (int i = 0; i < deleteTemplateList.size(); i++) {
					deleteMsg += "[" + deleteTemplateList.get(i).getTmid()
							+ "，" + deleteTemplateList.get(i).getTmName() + "，"
							+ deleteTemplateList.get(i).getDsflag() + "]，";
				}
				String contnet = "删除富信模板" + "成功。(总数：" + ids.length
						+ ")[模板ID，模板名称，模板类型](" + deleteMsg + ")";
				EmpExecutionContext.info("模板管理", loginSysuser.getCorpCode(),
						String.valueOf(loginSysuser.getUserId()),
						loginSysuser.getUserName(), contnet, "DELETE");
			}
			
			if(count > 0){//模板表删除成功，删除快捷场景表里面的记录
				@SuppressWarnings("unchecked")
				Map<String, List<LfPrivilege>> priMap = (Map<String, List<LfPrivilege>>) request.getSession(false).getAttribute("priMap");
				long tempId = Long.valueOf(ids[0]);
				String lgcorpcode = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getCorpCode();
				//企业ID
				long userId = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getUserId();
				LfShortTemplateVo bean = new LfShortTemplateVo();
				bean.setCorpCode(lgcorpcode);
				bean.setUserId(userId);
				bean.setTempId(tempId);
				RmsShortTemplateBiz rstlBiz = new RmsShortTemplateBiz();
				boolean flag = rstlBiz.deleteShortTemp(bean);
				if(flag){
					Map<String, List<LfPrivilege>> priMaps = new HashMap<String, List<LfPrivilege>>();
					Set<String> keys = priMap.keySet();   //此行可省略，直接将map.keySet()写在for-each循环的条件中  
			        for(String key:keys){  
			        	for(int i=0;i<priMap.get(key).size();i++){
			        		if(priMap.get(key).get(i).getMenuCode().equals(String.valueOf(tempId))){
			        			priMap.get(key).remove(i);
			        		}
			        		priMaps.put(key, priMap.get(key));
			        	}
			        }  
			        request.getSession(false).setAttribute("priMap", priMaps);
				}
				 
			}
			
			response.getWriter().print(count);
			// 添加操作成功日志
			spLog.logSuccessString(opUser, opModule, opType, opContent,
					corpcode);
		} catch (Exception e) {
			response.getWriter().print(0);
			// 添加操作失败日志
			spLog.logFailureString(opUser, opModule, opType,opContent + opSper, e, corpcode);
			EmpExecutionContext.error(e, "删除富信模板失败！");
		}
	}
	
	public  byte[] getRmsFileBytes(String filePath, String title, List<Map<String,String>> list) {
		byte[] results = null;
		try {
			//用于记录RMS总长度的位置
			int rmsBgIndex = 0;
			int index = 0;
			// 设置一个，每次 装载信息的容器
			byte[] bytes = new byte[1024 * 1024 * 8];
			bytes[index] = (byte) 162;// 文件标识:A2
			index++;

			bytes[index] = (byte) 02;// 版本:01 表示V1.0
			index++;

			bytes[index] = (byte) 00;// 文件编码格式:0:明文
			index++;

			// 直接将整型数4字节以2进制形式存储(下同)
			// 计算所有文件总长度:4byte
			//int contLegth = getContFileLegth(filePath);
			int rmsTotalLen = 0;
			rmsBgIndex= index;
			index += 4;
			
			// 计算包含文件个数：2byte
			int fileCount = list.size() + 1;
			System.arraycopy(int2bytes(fileCount), 2, bytes, index, 2);
			index += 2;
			//累加总长度
			rmsTotalLen=rmsTotalLen+2;
			// 计算标题内容长度:1
			int tileLegth = title.getBytes("UTF-8").length;
			System.arraycopy(int2bytes(tileLegth), 3, bytes, index, 1);
			index++;
			//累加总长度
			rmsTotalLen=rmsTotalLen+1;
			
			// 标题内容
			System.arraycopy(title.getBytes("UTF-8"), 0, bytes, index,tileLegth);
			index += tileLegth;
			//累加总长度
			rmsTotalLen=rmsTotalLen+tileLegth;
			
			// ----遍历文件Map
			Map<String, byte[]> rmsMap = getRmsMap(filePath);
			Iterator<Entry<String, byte[]>> it=rmsMap.entrySet().iterator();
			Entry<String, byte[]> entry = it.next();
			for (int i = -1; i < list.size(); i++) {
				//文件类型 ：1byte
				//int rfileType = FileNameBytesUtil.getFileType(rfileName);
				int rfileType = 1;
				if (i != -1) {
					rfileType = Integer.parseInt(list.get(i).get("fileType"));
				}
				System.arraycopy(int2bytes(rfileType), 3, bytes, index, 1);
				index++;
				//累加总长度
				rmsTotalLen=rmsTotalLen+1;
				
				if(rfileType == 6){
					int messageLength = list.get(i).get("message").getBytes("UTF-8").length;
					System.arraycopy(int2bytes(messageLength), 0, bytes, index, 4);
					index+=4;
					//累加总长度
					rmsTotalLen=rmsTotalLen+4;
				
					byte[] message = list.get(i).get("message").getBytes("UTF-8");
					System.arraycopy(message, 0, bytes, index, messageLength);
					index += messageLength;
					//累加总长度
					rmsTotalLen=rmsTotalLen+messageLength;
				}else if(rfileType == 7){
					int messageLength = list.get(i).get("message").getBytes("UTF-8").length;
					System.arraycopy(int2bytes(messageLength), 0, bytes, index, 4);
					index+=4;
					//累加总长度
					rmsTotalLen=rmsTotalLen+4;
				
					byte[] message = list.get(i).get("message").getBytes("UTF-8");
					System.arraycopy(message, 0, bytes, index, messageLength);
					index += messageLength;
					//累加总长度
					rmsTotalLen=rmsTotalLen+messageLength;
					continue;
				}
				
				String rfileName = entry.getKey();
				
				// 文件文件名的字节长度 :1byte
				int rfileNameLegth = entry.getKey().getBytes("UTF-8").length;
				System.arraycopy(int2bytes(rfileNameLegth), 3, bytes, index, 1);
				index++;
				//累加总长度
				rmsTotalLen=rmsTotalLen+1;
				// 文件文件名 :实际计算长度
				System.arraycopy(rfileName.getBytes("UTF-8"), 0,bytes, index, rfileNameLegth);
				index += rfileNameLegth;
				//累加总长度
				rmsTotalLen=rmsTotalLen+rfileNameLegth;

				// 文件内容长度: 4byte
				byte[] content = entry.getValue();
				int contLenth = content.length;
				System.arraycopy(int2bytes(contLenth), 0, bytes, index, 4);
				index += 4;
				//累加总长度
				rmsTotalLen=rmsTotalLen+4;
				// 文件内容：实际计算长度
				System.arraycopy(content, 0, bytes, index, contLenth);
				index += contLenth;
				//累加总长度
				rmsTotalLen=rmsTotalLen+contLenth;

				if(it.hasNext()){
					entry = it.next();
				}
			}
			
			//最后写入RMS总长度
			System.arraycopy(int2bytes(rmsTotalLen), 0, bytes, rmsBgIndex, 4);
			results = new byte[index];
			System.arraycopy(bytes, 0, results, 0, index);
		} catch (Exception e) {
			EmpExecutionContext.error("封装rms文件字节数组出现异常：" +e.toString());
		}
		return results;
	}

	/**
	 * 获取RMS文件的byte[]
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public  byte[] getRmsFileBytes(String filePath,String title) {
		byte[] results = null;
		try {
			//用于记录RMS总长度的位置
			int rmsBgIndex = 0;
			int index = 0;
			// 设置一个，每次 装载信息的容器
			byte[] bytes = new byte[1024 * 1024 * 8];
			bytes[index] = (byte) 162;// 文件标识:A2
			index++;

			bytes[index] = (byte) 01;// 版本:01 表示V1.0
			index++;

			bytes[index] = (byte) 00;// 文件编码格式:0:明文
			index++;

			// 直接将整型数4字节以2进制形式存储(下同)
			// 计算所有文件总长度:4byte
			//int contLegth = getContFileLegth(filePath);
			int rmsTotalLen = 0;
			rmsBgIndex= index;
			index += 4;
			
			// 计算包含文件个数：2byte
			int fileCount = getFileCount(filePath);
			System.arraycopy(int2bytes(fileCount), 2, bytes, index, 2);
			index += 2;
			//累加总长度
			rmsTotalLen=rmsTotalLen+2;
			// 计算标题内容长度:1
			int tileLegth = title.getBytes("UTF-8").length;
			System.arraycopy(int2bytes(tileLegth), 3, bytes, index, 1);
			index++;
			//累加总长度
			rmsTotalLen=rmsTotalLen+1;
			
			// 标题内容
			System.arraycopy(title.getBytes("UTF-8"), 0, bytes, index,tileLegth);
			index += tileLegth;
			//累加总长度
			rmsTotalLen=rmsTotalLen+tileLegth;
			
			// ----遍历文件Map
			Map<String, byte[]> rmsMap = getRmsMap(filePath);
			for (Entry<String, byte[]> entry : rmsMap.entrySet()) {
				// System.out.println("key= " + entry.getKey() + " and value= " +
				// entry.getValue().length);
				String rfileName = entry.getKey();
				//文件类型 ：1byte
				int rfileType = FileNameBytesUtil.getFileType(rfileName);
				System.arraycopy(int2bytes(rfileType), 3, bytes, index, 1);
				index++;
				//累加总长度
				rmsTotalLen=rmsTotalLen+1;
				
				// 文件文件名的字节长度 :1byte
				int rfileNameLegth = entry.getKey().getBytes("UTF-8").length;
				System.arraycopy(int2bytes(rfileNameLegth), 3, bytes, index, 1);
				index++;
				//累加总长度
				rmsTotalLen=rmsTotalLen+1;
				// 文件文件名 :实际计算长度
				System.arraycopy(rfileName.getBytes("UTF-8"), 0,bytes, index, rfileNameLegth);
				index += rfileNameLegth;
				//累加总长度
				rmsTotalLen=rmsTotalLen+rfileNameLegth;

				// 文件内容长度: 4byte
				byte[] content = entry.getValue();
				int contLenth = content.length;
				System.arraycopy(int2bytes(contLenth), 0, bytes, index, 4);
				index += 4;
				//累加总长度
				rmsTotalLen=rmsTotalLen+4;
				// 文件内容：实际计算长度
				System.arraycopy(content, 0, bytes, index, contLenth);
				index += contLenth;
				//累加总长度
				rmsTotalLen=rmsTotalLen+contLenth;
			}
			//最后写入RMS总长度
			System.arraycopy(int2bytes(rmsTotalLen), 0, bytes, rmsBgIndex, 4);
			results = new byte[index];
			System.arraycopy(bytes, 0, results, 0, index);
		} catch (Exception e) {
			EmpExecutionContext.error("封装rms文件字节数组出现异常：" +e.toString());
		}
		return results;

	}

	private int getContFileLegth(String filePath) {
		Map<String, byte[]> rmsMap = getRmsMap(filePath);
		int totalSize = 0;
		for (Entry<String, byte[]> entry : rmsMap.entrySet()) {
			totalSize += entry.getValue().length;
//System.out.println(entry.getKey()+":" +entry.getValue().length);
		}
//System.out.println("totalSize:" + totalSize);
		return totalSize;
	}

	private int getFileCount(String filePath) {
		File file = new File(filePath);
		return file.listFiles().length;
	}

	private byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

	@SuppressWarnings("unused")
	private byte[] readFileContents(File file) {
		InputStream in = null;
		byte[] contentBytes = null;
		try {
			in = new FileInputStream(file);
			int length = in.available();
			contentBytes = new byte[length];
			int c=in.read(contentBytes);
		} catch (IOException e) {
			EmpExecutionContext.error(e, "获取文件内容失败！");
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "文件关闭失败！");
				}
			}
		}
		return contentBytes;
	}

	// Map<序号 ,Map<文件名, byte[]>>
	public Map<String, byte[]> getRmsMap(String filePath) {
		Map<String, byte[]> map = new LinkedHashMap<String, byte[]>();
		File file = new File(filePath);
		if (!file.isDirectory()) {
			EmpExecutionContext.info(filePath+"路径不是一个文件夹");
			return null;
		}
		File[] tempList1 = file.listFiles();
		List<File> tempList = Arrays.asList(tempList1);
		Collections.sort(tempList, new Comparator<File>(){
		    @Override
            public int compare(File o1, File o2) {
		    if(o1.isDirectory() && o2.isFile()) {
                return -1;
            }
		    if(o1.isFile() && o2.isDirectory()) {
                return 1;
            }
		    return o1.getName().compareTo(o2.getName());
		    }
		});

		
		if(tempList == null || tempList.size() <=0){
			EmpExecutionContext.info(filePath+"src文件夹 目录为空");
			return null;
		}
		for (int i = 0; i < tempList.size(); i++) {
			File tempFile = tempList.get(i);
			if (tempFile.getName().endsWith(".smil")) {
				String finlName = tempFile.getName();
				byte[] tempBytes = getFileBytes(tempFile);
				map.put(finlName, tempBytes);
			}
		}
		for (int i = 0; i < tempList.size(); i++) {
			File tempFile = tempList.get(i);
			if (tempFile.getName().endsWith(".smil")) {
				continue;
			} 
			String finlName = tempFile.getName();
			byte[] tempBytes = getFileBytes(tempFile);
			map.put(finlName, tempBytes);
		}
		return map;
	}
	
	/**
	 * 读取 src 目录下的资源文件 字节
	 * @param tempFile
	 * @return
	 */
	public byte[] getFileBytes(File tempFile) {
		FileInputStream fis = null;
		ByteArrayOutputStream outStream = null;
		try {
			fis = new FileInputStream(tempFile);
			outStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len = -1;
	        while((len=fis.read(buffer))!=-1){
	            outStream.write(buffer,0,len);
	        }
	       
		} catch (Exception e) {
			EmpExecutionContext.error(e,"读取文件字节流出现异常！");
		}finally{
			try {
				if(null != outStream){
					outStream.close();
				}
				if(null != fis){
					fis.close();
				}
			} catch (Exception e) {
				 EmpExecutionContext.error(e,"读取src目录文件关闭流出现异常");
			}
			
		}
		 return outStream==null?null:outStream.toByteArray();
	}
	
	/**
	 * 检测文件是否存在
	 * @param request
	 * @param response
	 */
	public void checkMmsFile(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			TxtFileUtil tfu = new TxtFileUtil();
			String url = request.getParameter("url");
			String tmid = request.getParameter("tmid");
			String filePath =  dirUrl + url.replace(".rms", ".html");
			//如果文件路径下没有富信模板，则从阿里云下载富信模板。
			if(!tfu.checkFile(url)){
				//从阿里云下载
				downLoadRmsFromOss(request,filePath.replace("fuxin.html", ""),tmid,"");
			}
			response.getWriter().print(tfu.checkFile(url));
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"验证富信文件是否存在出现异常！");
		}
	}
	
	
	/**
	 * 导出Rms文件
	 */
	public void exportRms(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		InputStream inStream = null;
		ServletOutputStream servletOS = null;
		try {
			String url = request.getParameter("u");
			File file = new File(dirUrl + url);
			if (file.exists()) {
				String filename = URLEncoder.encode(file.getName(), "utf-8");
				response.reset();
				response.setContentType("application/rms");
				response.addHeader("Content-Disposition",
						"attachment; filename=\"" + filename + "\"");
				int fileLength = (int) file.length();
				response.setContentLength(fileLength);
				if (fileLength != 0) {
					inStream = new FileInputStream(file);
					byte[] buf = new byte[4096];
					servletOS = response.getOutputStream();
					int readLength;
					while (((readLength = inStream.read(buf)) != -1)) {
						servletOS.write(buf, 0, readLength);
					}
					servletOS.flush();
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"导出rms文件出现异常！");
		} finally{
			if(null != inStream) {
                inStream.close();
            }
			if(null != servletOS) {
                servletOS.close();
            }
		}
	}
	 
	/**
	 * 获取模板文件的html
	 */
	public void getTmMsg(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		//根据前端传来的版本号、判断用哪个版本的预览方法
		String version = request.getParameter("ver");
		if(!StringUtils.IsNullOrEmpty(version)&& "V1.0".equals(version)){
			getTmMsgV1(request, response);
		}else{
			TxtFileUtil txtfileutil = new TxtFileUtil();
			dirUrl = txtfileutil.getWebRoot();
			//rms模板文件路径
			String htmlUrl = request.getParameter("tmUrl");
			//模板ID
			String tmid = request.getParameter("tmid");
			String filePath = dirUrl + htmlUrl.replace("fuxin.rms", "fuxinPreview.html");
//		String filePath = dirUrl + htmlUrl.replace("fuxin.rms", "fuxin.html");
			String mms =null;
			String tmName  = request.getParameter("tmName");
			if(StringUtils.IsNullOrEmpty(tmName)){
				tmName ="";
			}
			//解决 + - 等特殊符号丢失的问题
//		tmName =  URLEncoder.encode(tmName, "UTF-8");
//		tmName =  URLDecoder.decode(tmName, "UTF-8");
//		tmName =  URLDecoder.decode(tmName, "UTF-8");
			try {
				if (filePath == null || "".equals(filePath)) {
					response.getWriter().print("源文件不存在");
					return;
				} else {
					long downStartTm = System.currentTimeMillis();
					//从阿里云下载
					boolean flag = downLoadRmsFromOss(request,filePath.replace("fuxinPreview.html", ""),tmid,"needPreview");
					
					long downEndTm = System.currentTimeMillis();
EmpExecutionContext.info(tmid+":下载rms文件总耗时：" +(downEndTm - downStartTm) +" ms");					
//				boolean flag = downLoadRmsFromOss(filePath.replace("fuxin.html", ""),tmid);
					if(!flag){
						response.getWriter().print("源文件不存在");
						return;
					}
					//根据smil文件生成 预览html
					String smilFileUrl ="";
					String  filePath1 = filePath.split("fuxinPreview.html")[0];
//				String  filePath1 = filePath.split("fuxin.html")[0];
					String srcPath = (filePath1+"src/").replace(dirUrl, "");
					smilFileUrl = filePath1+"src/00.smil";
					//analysisRMS.createHtml(filePath+"src/", srcPath, rEntity);
					long previewStartTm = System.currentTimeMillis();
					new AnalysisRMS().createPreviewHtml(filePath1+"src/", srcPath,"", smilFileUrl,false);
					long previewEndTm = System.currentTimeMillis();
EmpExecutionContext.info(tmid+":生成预览文件耗时：" +(previewEndTm - previewStartTm)+" ms");
					//读取生成的用于预览的html:fuxinPreview.html
					File file = new File(filePath);
					StringBuffer sbuf = new StringBuffer();
					if(file.isFile() && file.exists()) {
						InputStreamReader isr = null; 
						BufferedReader br = null; 
						FileInputStream fis = null;
						try{
							fis = new FileInputStream(file);
							isr = new InputStreamReader(fis, "UTF-8");
							br = new BufferedReader(isr);
							String lineTxt = null;
							while ((lineTxt = br.readLine()) != null) {
								sbuf.append(lineTxt);
							}
						}finally{
							try{
								if(null != fis){
									fis.close();
								}
								IOUtils.closeReaders(getClass(), br,isr);
							}catch(Exception e){
								EmpExecutionContext.error(e, "IOerror");
							}
						}
//						br.close();
					}else{
						response.getWriter().print("源文件不存在");
						return;
						
					}
					
					//插入标题
					sbuf.insert(0, "<h4 style='margin-bottom: 10px;font-size: 14px;text-align: center;'>"+tmName+"</h4>");
					if (sbuf != null) {
						mms = sbuf.toString().replace("\r\n", "&lt;BR/&gt;");
						
						//判断文件是否含有参数进行模板参数替换
						String paramInFile = request.getParameter("paramInFile");
						if(!StringUtils.IsNullOrEmpty(paramInFile)){
							paramInFile = URLDecoder.decode(paramInFile, "UTF-8");
							mms = parseTemplate(mms, paramInFile);
						}
						mms =mms.replace("\n", "&lt;BR/&gt;");
					}	
				}
				response.getWriter().print(mms);
			} catch (Exception e) {
				response.getWriter().print("");
				EmpExecutionContext.error(e,"获取富信模板文件信息出现异常！");
			}
		}
		
	}
	
    /**
     * 彩信内容中的参数个数
     * 2018-1-25,caihq
     * @param tmpContent 参数内容
     * @param param 参数，多个参数以逗号隔开
     */
    private String parseTemplate(String tmpContent,String param) {
        String result = tmpContent;
        String[] paramArr = param.split(",");
        String eg = "#[pP]_[1-9][0-9]*#";
        Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(tmpContent);
        String paramStr = "";
        String pc = "";
        tmpContent = " "+ tmpContent + " ";
        String[] splitTplContent = tmpContent.split(eg);
        int count = 0;
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
        	sb.append(splitTplContent[count]);
            paramStr = m.group();
            paramStr = paramStr.toUpperCase();
            pc = paramStr.substring(paramStr.indexOf("#P_") + 3,paramStr.lastIndexOf("#"));
            int paramPos = Integer.parseInt(pc);
            count++;
            sb.append(paramArr[paramPos-1].replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;"));
        }
        sb.append(splitTplContent[count]);
        result = sb.toString();
        return result.trim();
    }
		//编辑、复制
		public void doCopy(HttpServletRequest request, HttpServletResponse response)
		{	//模板ID
			String tmId = request.getParameter("tmId");
			//操作类型
			String opType = request.getParameter("opType");
			//富信模板.rms文件相对路径
			String tmUrl = request.getParameter("tmUrl");
			TxtFileUtil txtfileutil = new TxtFileUtil();
			dirUrl = txtfileutil.getWebRoot();
			String filePath = "";
			String fuxinHtml ="";
			//
			
			try {
				
				//LfTemplate template = baseBiz.getById(LfTemplate.class, tmId);
				LfTemplate template = mtlBiz.getTmplateByTmid(Long.parseLong(tmId));
				String htmlUrl = template.getTmMsg();
				String temTheame = template.getTmName();
				int degree = template.getDegree();
				Long degreeSize = template.getDegreeSize();
				filePath = dirUrl + htmlUrl.replace(".rms", ".html");
				
				File file = new File(filePath);
				StringBuffer sbuf = new StringBuffer();
			    if(!file.exists()) {
			    	//从阿里云下载
					boolean flag = downLoadRmsFromOss(request,filePath.replace("fuxin.html", ""),tmId,"");
					if(!flag){
						response.getWriter().write("<script>alert('文件不存在，请稍后再试！');history.go(-1);</script>");
						return;
					}
					
			    }
			    InputStreamReader isr = null;
			    BufferedReader br = null;
			    try{
				    isr = new InputStreamReader(new FileInputStream(file), "utf-8");
				    br = new BufferedReader(isr);
				    String lineTxt = null;
				    while ((lineTxt = br.readLine()) != null) {
				    	sbuf.append(lineTxt);
				    }
			    }finally{
			    	try{
			    		IOUtils.closeReaders(getClass(), br,isr);
			    	}catch(IOException e){
			    		EmpExecutionContext.error(e, "IOError:");
			    	}
			    }
//			    br.close();
				if (sbuf != null) {
					fuxinHtml = sbuf.toString().replace("\r\n", "&lt;BR/&gt;");
					fuxinHtml =fuxinHtml.replace("\n", "&lt;BR/&gt;").replaceAll("'", "&apos;");
					
				}	
				//request.setAttribute("opType", opType);
				if("edit".equals(opType)){
					request.setAttribute("tmUrl",tmUrl);
					request.setAttribute("tmId", tmId);
				}
				DegreeCountUtil countUtil =  DegreeCountUtil.getInstance();
				//Map<String, String> queryDegree = countUtil.queryDegree();
//				Map<String, String> queryDegree = new HashMap<String, String>();
				Map<String, String> queryDegree =new HashMap<String,String>();
				try{
					queryDegree = countUtil.queryDegree();
				}catch(Exception e){
					EmpExecutionContext.error(e, "档位计算出错");
				}
				
				// 设置服务器名称
				String json = JSON.toJSONString(queryDegree);
				
				
				//网关参数大小:默认2KB=2048B
				String paramSize =SystemGlobals.getValue("montnets.rms.mw.paramsize");
				if(StringUtils.IsNullOrEmpty(paramSize)){
					paramSize ="2048";
				}
				//rms 文件大小:默认：19M =1992294B
				String maxSize =SystemGlobals.getValue("montnets.rms.maxsize");
				if(StringUtils.IsNullOrEmpty(paramSize)){
					maxSize ="1992294";
				}
				//各资源文件标签大小   total:par:text:img:audio:vedio=355:35:45:45:48:48
				String smilFileSize =SystemGlobals.getValue("montnets.rms.smilfile.size");
				if(StringUtils.IsNullOrEmpty(paramSize)){
					smilFileSize ="355:35:45:45:48:48";
				}
				 
				
				Map<String, List<LfIndustryUse>> induUseMap = null;
				List<LfIndustryUse> industryList = null;
				List<LfIndustryUse> useList = null;
				
				//行业-用途列表
				induUseMap = getIndustryUseList(request,response);
				industryList = induUseMap.get("industry");
				useList = induUseMap.get("use");
				
				String source = request.getParameter("source");
				if(!StringUtils.IsNullOrEmpty(source)){//从公共模板跳转过来才有此参数
					request.setAttribute("source",source);
				}
				
				//行业ID
				int industryId = -1;//默认-1
				if(!StringUtils.IsNullOrEmpty(request.getParameter("industryId"))){
					industryId = Integer.parseInt(request.getParameter("industryId"));
				}
				//用途ID
				int useId = -2; //默认-2
				if(!StringUtils.IsNullOrEmpty(request.getParameter("useId"))){
					useId = Integer.parseInt(request.getParameter("useId"));
				}
				
				
				new ServerInof().setServerName(getServletContext().getServerInfo());
				request.setAttribute("deGreeMap", json);
				request.setAttribute("fuxinHtml",fuxinHtml);
				request.setAttribute("temTheame",temTheame);
				request.setAttribute("paramSize", paramSize);
				request.setAttribute("maxSize", maxSize);
				request.setAttribute("smilFileSize", smilFileSize);
				request.setAttribute("degree", degree);
				request.setAttribute("degreeSize", degreeSize);
				request.setAttribute("industryList",industryList);
				request.setAttribute("useList",useList);
				request.setAttribute("industryId",industryId);
				request.setAttribute("useId",useId);
				request.getRequestDispatcher(PATH +"/mbgl_editTemplate.jsp").forward(request, response);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"编辑富信模板出现异常！");
				try {
					response.getWriter().write("<script>alert('文件不存在，请稍后再试！');history.go(-1);</script>");
				} catch (IOException e1) {
					EmpExecutionContext.error(e1,"编辑富信模板出现异常！");
				}
			}
		}
		
	 public List<LfTemplateVo> findTemplateBycontition(long loginUserId,LfTemplateVo ltv,PageInfo pageInfo){
		 List<LfTemplateVo>  list = null;
		 try {
			 list = mtlBiz.getTemplateByCondition(loginUserId, ltv, pageInfo);
			 //-- TODO 第一帧文件是否存在，不存在从阿里云下载生成
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询富信模板出现异常！");
		}
		 return list;
	 }

	/**
	 * 下载第一帧用于预览
	 * @param request request对象
	 * @param list 集合
	 */
	 public void downFirstFrame(HttpServletRequest request, List<LfTemplateVo> list){
		 //遍历mmsList 中的每一个对象是否含有第一帧html,没有就从阿里云下载
		 for(LfTemplateVo lf : list){
			 String firstFramePath = dirUrl+lf.getTmMsg().replace("fuxin.rms", "firstframe.jsp");
			 File fFrame  = new File(firstFramePath);
			 //不存在则从阿里云下载
			 if(!fFrame.exists()){
				 String tmid = lf.getTmid().toString();
				 String filePath = dirUrl + lf.getTmMsg().replace("fuxin.rms", "");
				 long startTM = System.currentTimeMillis();
				 //根据版本号判断rms 是哪个版本
				 String version = "V1.0";
				 version = lf.getVer();
				 boolean downLoadRmsFromOss = false;
				 if("V1.0".equals(version)){
					 downLoadRmsFromOss = downLoadRmsFromOssV1(filePath);
				 }else{
					 downLoadRmsFromOss = downLoadRmsFromOss(request,filePath, tmid,"");
				 }
				 long endTM = System.currentTimeMillis();
				 EmpExecutionContext.info("模板ID"+lf.getTmid()+",rms 解析耗时："+(endTM - startTM)+ " ms");
				 if(!downLoadRmsFromOss){
				 	lf.setPreviewError(1);
				 	lf.setTmMsg("rms/mbgl/404.jsp");
				 }
			 }
		 }
	 }

	public void findTemplatePage(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw =null;
		Gson json = new Gson();
		try{
			pw = response.getWriter();
			LfTemplateVo ltv = null;
			PageInfo pageInfo = null;
			String loginUserId = request.getParameter("loginUserId");
			String lfTemplateVo_ = request.getParameter("lfTemplateVo");
			String pageInfo_ = request.getParameter("pageInfo");
			String lgcorpcode = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getCorpCode();
			ltv = json.fromJson(lfTemplateVo_, LfTemplateVo.class);
			//网关审核通过
			ltv.setAuditstatus(1);
			//富信
			ltv.setTmpType(11);
			//已启用
			ltv.setTmState(1L);
			ltv.setCorpCode(lgcorpcode);
			pageInfo = json.fromJson(pageInfo_, PageInfo.class);
			List<LfTemplateVo> list = new ArrayList<LfTemplateVo>();
			list = findTemplateBycontition(Long.parseLong(loginUserId),ltv,pageInfo);
			//下载第一帧用于预览
			downFirstFrame(request, list);
			Map<String,Object> res = new HashMap<String,Object>();
			res.put("pageInfo", pageInfo);
			res.put("vo", ltv);
			res.put("record", list);
			pw.write(json.toJson(res));
			pw.flush();
		}catch (Exception e) {
			EmpExecutionContext.error(e,"查询富信模板出现异常！");
		}finally{
			if(null != pw){
				pw.close();
			}
		}
	 }
	 	/**
	 	 * 修改模板状态
	 	 */
		public void changeState(HttpServletRequest request, HttpServletResponse response)
		{
			PrintWriter pw = null;
			try
			{
				pw = response.getWriter();
				//模板ID
				String id = request.getParameter("id");

				//模板修改的状态
				String state = request.getParameter("t");
				LfTemplate temp = mtlBiz.getTmplateByTmid(Long.parseLong(id));
				LfTemplate tem = new LfTemplate();
				tem.setTmid(Long.parseLong(id));
				tem.setTmState(Long.parseLong(state));
				tem.setAddtime(temp.getAddtime());
				tem.setIsPublic(temp.getIsPublic());
				tem.setExlJson(temp.getExlJson());
                tem.setVer(temp.getVer());

                tem.setDegree(temp.getDegree());
                tem.setUsecount(temp.getUsecount());
                tem.setDegreeSize(temp.getDegreeSize());
				boolean r = baseBiz.updateObj(tem);
				
				String changeStr="";
				if(Long.parseLong(state)==0L){
					changeStr="状态由启用改为禁用";
				}else{
					changeStr="状态由禁用改为启用";
				}
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String contnet="修改富信信模板状态成功(富信模板ID为："+id+"，富信模板名称为："+tem.getTmName()+")，"+changeStr+"。";
					EmpExecutionContext.info("模板编辑", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "UPDATE");
				}
				
				if (r)
				{
					String lgcorpcode = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getCorpCode();
					//企业ID
					long userId = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getUserId();
					LfShortTemplateVo bean = new LfShortTemplateVo();
					bean.setCorpCode(lgcorpcode);
					bean.setUserId(userId);
					bean.setTempId(Long.valueOf(id));
					RmsShortTemplateBiz rstlBiz = new RmsShortTemplateBiz();
					boolean flag = rstlBiz.deleteShortTemp(bean);
					if(flag){
						//修改tf_template表中isshorttemp字段的值（1表示新增，0表示取消）
						int param = 0;
						flag=rstlBiz.updateLfTemplate(Integer.parseInt(id),param);
						@SuppressWarnings("unchecked")
						Map<String, List<LfPrivilege>> priMap = (Map<String, List<LfPrivilege>>) request.getSession(false).getAttribute("priMap");
						Map<String, List<LfPrivilege>> priMaps = new HashMap<String, List<LfPrivilege>>();
						Set<String> keys = priMap.keySet();   //此行可省略，直接将map.keySet()写在for-each循环的条件中  
				        for(String key:keys){  
				        	for(int i=0;i<priMap.get(key).size();i++){
				        		if(priMap.get(key).get(i).getMenuCode().equals(id)){
				        			priMap.get(key).remove(i);
				        		}
				        		priMaps.put(key, priMap.get(key));
				        	}
				        }  
				        request.getSession(false).setAttribute("priMap", priMaps);
					}
					pw.write("true");
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"修改富信模板状态出现异常！");
			}finally{
				if(null != pw){
					pw.flush();
					pw.close();
					
				}
			}
		}
		
		/**
		 * 导入富信模板文件
		 * @param request
		 * @param response
		 */
		@SuppressWarnings("unchecked")
		public void importRms(HttpServletRequest request, HttpServletResponse response)
		{
			TxtFileUtil txtfileutil = new TxtFileUtil();
			dirUrl = txtfileutil.getWebRoot();
			response.setHeader("Charset","UTF-8");
		    response.setContentType("text/html;charset=UTF-8");
		    PrintWriter out = null;
		    long lguserId = ((LfSysuser) request.getSession(false).getAttribute("loginSysuser")).getUserId();
		    String lgcorpcode = ((LfSysuser) request.getSession(false).getAttribute("loginSysuser")).getCorpCode();
		    long startTime = System.currentTimeMillis();
		    //定义页面返回值
			boolean result = true;
			StringBuffer resultMsg =  new StringBuffer();
			//模板自增ID
			long tmId = 0L;
			try {
				// 获取操作员的用户ID
				String corpcodedir = lgcorpcode + "/";
				out = response.getWriter(); 
				//rms 文件名称
				String rmsName = "";
				String fileName ="fuxin";
				
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(100*1024);          
				factory.setRepository(new File(dirUrl + "file/rms/templates/"+ corpcodedir));
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");
				List<FileItem> items = upload.parseRequest(request); 
				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
				    FileItem fileItem = (FileItem) iter.next();
				    if(!fileItem.isFormField() && fileItem.getName().length() > 0 && "chooseRms".equals(fileItem.getFieldName())){ 
				    	rmsName = fileName+fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
				    	String filePath = dirUrl +"file/rms/templates/"+lgcorpcode+"/";
				    	//String rmsFilePath = dirUrl + "file/rms/templates/" + rmsName;
				    	File fileDir = new File(filePath);
				    	File file = new File(filePath+rmsName);
				    	if(!fileDir.exists()){
				    		fileDir.mkdirs();
				    	}
				    	if(!file.exists()){
                            boolean flag = file.createNewFile();
                            if (!flag) {
                                EmpExecutionContext.error("创建文件失败！");
                            }
				    	}
				    	
				    	//操作类型 1：有效,2草稿
				    	long optypeL  = 2L;
				    	
				    	//模板主题
				    	String tmName = "";
				    	//参数个数
				    	int parmLegth = 0;
				    	
				    	tmId = addTemplate(optypeL, tmName, lguserId, lgcorpcode, parmLegth,0);
				    	
				    	filePath = filePath+tmId+"/";
				    	File f = new File(filePath);
				    	if(!f.exists()){
				    		f.mkdirs();
				    	}
				    	
				    	fileItem.write(new File(filePath+rmsName));//将页面获取的rms文件生成到指定目录下
				    	//rms文件大小
				    	int rmsFileSize =(int)fileItem.getSize();
				    	
				    	AnalysisRMS analysisRMS = new AnalysisRMS();
				    	
				    	//解析生成相关资源文件
				    	RmsEntity rEntity = analysisRMS.Parse(request,filePath+rmsName, filePath+"src/", rmsFileSize,null);
				    	//生成fuxin.html-用于预览
				    	String srcPath = (filePath+"src/").replace(dirUrl, "");
				    	analysisRMS.createHtml(filePath+"src/", srcPath, rEntity);
				    	
				    	tmName =  rEntity.getTitle();
				    	//3.生成rms文件
						byte[] bytes = null;
						if(result){
							 File file1  = null;
							 bytes = getRmsFileBytes(filePath+"/src",tmName);
							 if(bytes != null){
								  file1 =  writeFile(filePath+"fuxin.rms", bytes);
							 }
							 
							 if(file1==null){
								 result = false;
								 resultMsg.append("解析rms文件，生成二进制流失败！");
								 EmpExecutionContext.error("解析rms文件，生成二进制流失败！");
							 }
						}
				    	
				    	
						//4.将rms文件上传阿里云
						
						if(result &&  null != oosUtil.getOssClient()){
							ZipCompress zipCom = new ZipCompress(filePath+"/fuxin.zip",filePath+"/fuxin.rms");
							zipCom.zip();
							File aliFile = new File(filePath+"/fuxin.zip");
							if(!oosUtil.uploadFile(filePath.replace(dirUrl, ""),aliFile)){
								 result = false;
								 resultMsg.append("备份富信模板到远程服务器失败，请稍后再试！");
								 EmpExecutionContext.error("备份富信模板到远程服务器失败，请稍后再试！");
							}
						}
						
						DegreeCountUtil util = DegreeCountUtil.getInstance();
						int degreeSize = rmsFileSize;
						
						//读取生成的txt文件，判断是否有参数来 
				    	File txtFile = new File(filePath+"src/");
				    	List<String> fileList = GetAndReadAllFile.getFileList(txtFile);
				    	String fileContent = null;  
			    		//网关参数大小:默认2KB=2048B
						int paramSize = 0;
						if(!StringUtils.IsNullOrEmpty(SystemGlobals.getValue("montnets.rms.mw.paramsize"))){
							paramSize =Integer.parseInt(SystemGlobals.getValue("montnets.rms.mw.paramsize"));
						}
				        for (String s : fileList) {  
				             // 文件内容  
				             fileContent = GetAndReadAllFile.getContentByLocalFile(new File(s));  
				             // 文件内容  
				             int temParmLegth = getTemplateCount(0,fileContent);
				             if(temParmLegth > parmLegth){
				            	 parmLegth = temParmLegth;
				             }
				             
				         } 
				       
				        if(parmLegth > 0){ //说明有参数
				        	degreeSize +=paramSize;
				        }
				        
				        degreeSize = (int)Math.ceil((double)degreeSize/1024);
						int degree = Integer.parseInt(util.countDegree(degreeSize));
						
						//5.将rms文件上传网关
						String empTmId ="0";//默认0是防止后面Long.parseLong 转换异常
						if(result && optypeL == 1L){
							if(bytes != null && optypeL == 1L){//1为启用,2为草稿的时候不提交网关
								empTmId = submGwCenter(bytes,lgcorpcode,degree,degreeSize,parmLegth);
							}
							
							if(StringUtils.IsNullOrEmpty(empTmId)){
								 result = false;
								 resultMsg.append("上传富信文档到文件服务器失败，请稍后再试！");
								 EmpExecutionContext.error("上传富信文档到文件服务器失败，请稍后再试！");
							}
						}
						
						//6.根据模板id更新模板记录
						if(result){
							boolean updaFlag =  updateTemplate(tmId,filePath.replace(dirUrl, "")+"fuxin.rms",tmName,empTmId,degree,degreeSize,parmLegth,optypeL,0,0);
//							boolean updaFlag =  updateTemplate(tmId,filePath.replace(dirUrl, "")+"fuxin.rms",tmName,empTmId,degree,degreeSize,parmLegth,optypeL);
							if(!updaFlag){
								 result = false;
								 resultMsg.append("富信文档模板保存数据库失败，请稍后再试！");
								 EmpExecutionContext.error("富信文档模板保存数据库失败，请稍后再试！");
							}
						}
						 
				    }
				}
				 
				if(result){
					out.print("true");
				}else{
					out.print(resultMsg);
				}
				
			}catch (FileUploadException e)
			{
				StringBuffer logInfo= new StringBuffer();
				logInfo.append("富信模板文件上传表单流上传失败。userId:").append(lguserId).append("，lgcorpcode:").append(lgcorpcode)
						.append("，耗时:").append(System.currentTimeMillis()-startTime).append("ms");
				EmpExecutionContext.error(e, logInfo.toString());
			} catch (Exception e) {
				EmpExecutionContext.error(e,"上传富信模板文件失败！");
			}finally{
				//7、如果上述步骤执行失败，删除第一步骤时插入的记录
				if(!result && !StringUtils.IsNullOrEmpty(String.valueOf(tmId))){
					String[] ids ={String.valueOf(tmId)};
					try {
						mtlBiz.delTempByTmId(ids);
					} catch (Exception e) {
						EmpExecutionContext.error(e,"上传富信模板删除失败数据出现异常！");
					}
				}
			}
		}
 
		/**
		 * 获取模板参数个数
		 * @param templateCount
		 * @param tmsMsg
		 * @return
		 */
		private int getTemplateCount(int templateCount, String tmsMsg) {
			String eg = "#[pP]_[1-9][0-9]*#";
			Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(tmsMsg);
			String paramStr = "";
			String pc = "";
			int paramCount = 0;
			while (m.find()) {
				paramStr = m.group();
				paramStr = paramStr.toUpperCase();
				pc = paramStr.substring(paramStr.indexOf("#P_") + 3,
						paramStr.lastIndexOf("#"));
				paramCount = Integer.parseInt(pc);
				if (paramCount > templateCount) {
					templateCount = paramCount;
				}
			}
			return templateCount;
		}
		/**
		 * 含有#P_N#就算一个参数
		 * @param tmsMsg
		 * @return
		 */
		private int getTemplateCount(String tmsMsg) {
			String eg = "\\{#参数[1-9][0-9]*#\\}";
			Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(tmsMsg);
			int paramCount = 0;
			while (m.find()) {
				paramCount++;
			}
			return paramCount;
		}
		/**
		 * 获取参数个数
		 * @param html
		 * @return
		 */
		private int getChartParamCount(String html) {
			String eg = "\\{#[1-9]?\\d行[1-9]?\\d列#}";
			Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(html);
			int paramCount = 0;
			while (m.find()) {
				paramCount ++;
			 
			}
			return paramCount;
		}
		/**
		 * 获取图参个数
		 * @param html
		 * @return
		 */
		public int getImgParamCount(String html) {
			String eg = "\\{#图参[1-9][1-9]*#\\}";
			Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(html);
			int paramCount = 0;
			while (m.find()) {
				paramCount ++;
				
			}
			return paramCount;
		}
		
		/*
		 * 获取模板id的方法
		 */
		public String getEmpTemplateLid()
		{
			String code = "";
			try {
				Calendar nowCal = Calendar.getInstance();
				Integer year = nowCal.get(Calendar.YEAR);
				Integer month = nowCal.get(Calendar.MONTH)+1;
				Integer day = nowCal.get(Calendar.DATE); 
				Integer hour = nowCal.get(Calendar.HOUR_OF_DAY);
				Integer minute = nowCal.get(Calendar.MINUTE);
				Integer ss = nowCal.get(Calendar.SECOND);			

	            String time =year+buP(month)+buP(day)+buP(hour)+buP(minute)+buP(ss);
	            
	    		//生成一个4位的随机数做为动态口令
	    		/*int count =4;
	    		StringBuffer sb = new StringBuffer();
	            String str = "0123456789";
	            Random r = new Random();
	            for(int i=0;i<count;i++){
	                int num = r.nextInt(str.length());
	                sb.append(str.charAt(num));
	                str = str.replace((str.charAt(num)+""), "");
	            }*/
	            //GetSxCount sx = GetSxCount.getInstance();
	            
	            String count = GetSxCount.getInstance().getCount().toString();
	    		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
	    		//彩信模板
	    		conditionMap.put("tmpType", "4");
	     		while(true){
	     			conditionMap.put("emptemplid", time+count);
	    			List<LfTemplate> lfTemplates = baseBiz.getByCondition(LfTemplate.class, conditionMap, null);
	    			if(lfTemplates != null && lfTemplates.size()>0){
	    				count = GetSxCount.getInstance().getCount().toString();
	    			}else{
	    				break;
	    			}
	     		}
	            code =time+count;  
	            
	            //code =time+sx.getCount()+sb.toString();            
				
			} catch (Exception e) {
				EmpExecutionContext.error(e,"获取彩信模板id失败！");
			}
			return code;
		}
		
	    public String buP(Integer s) { 
	        return s < 10 ? '0' + s.toString(): s.toString(); 
	    } 
	    
	    /**
		 * @description  记录操作成功日志  
		 * @param request
		 * @param modName 模块名称
		 * @param opContent 操作详情    	
		 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
		 * @author zousy <zousy999@qq.com>
		 * @datetime 2015-3-3 上午11:29:50
		 */
		public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
			LfSysuser lfSysuser = null;
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) {
                return;
            }
			lfSysuser = (LfSysuser)obj;
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}
		
		/**
		 * 验证关键字及参数格式是否正确的方法
		 * @param context
		 * @param corpCode
		 * @throws IOException
		 */
		public String checkBadWord (String context,String corpCode){
			String words = "";
			try {
				return words = keyWordAtom.checkText(context,corpCode);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"关键字校验出现异常");
			}
			return words;
		}
		/**
		 * createPicture：根据请求参数生成python的饼状图
		 */
		public void createPicture(HttpServletRequest request, HttpServletResponse response){
			PrintWriter pw =null;
			Gson json = new Gson();
			try {
				//获取年月以及随机数
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		        Date date = new Date();
		        String YandM = sdf.format(date);
		        SimpleDateFormat sdfs = new SimpleDateFormat("ddHHmmssSSS");
		        String nameVariable = sdfs.format(date);
				pw = response.getWriter();
				//图形类型 （1：饼状图，2：柱状图，3：折线图，4：工资条，5：表格，默认是1）
				String  chartType  = request.getParameter("chartType");
				//图形标题
				String  chartTitle = request.getParameter("chartTitle");
				//数据类型（1：静态，2：数值动态,3：全动态，默认是1）
				String  ptType     = request.getParameter("ptType");
				//饼状图颜色
				String  color      = request.getParameter("color");
				//饼状图第二列数值
				String  rowValue   = request.getParameter("rowValue");
				//柱状图折线图行名
				String barRowName = request.getParameter("barRowName");
				//饼状图、柱状图、折线图列名
				String barColName = request.getParameter("barColName");
				//柱状图折线图数值(以行为单位，用“,”隔开，换行则用“@”隔开)
				String barValue = request.getParameter("barValue");
				//柱状图折线图表格所有数据，用于回显(以行为单位，用“,”隔开，换行则用“@”隔开)
				String barTableVal = request.getParameter("barTableVal");
				//动态参数的值
				String parmValue = request.getParameter("parmValue");
				//行数
				String rowNum = request.getParameter("rowNum");
				//列数
				String colNum = request.getParameter("colNum");
				
				//创建图片生成位置
				String deletePath = request.getSession().getServletContext()
						.getRealPath("/pythonPicture/"+YandM).replaceAll("%20", " ");
				File f = new File(deletePath);
				if(!f.exists()){
					f.mkdirs();
					EmpExecutionContext.info("程序在"+deletePath+"目录下新建了pythonPicture的文件夹用于存放生成的图片");
				}
				
				//图片保存的位置
				String  pictureUrl = null;
				String colors = null;
				String rowNameVal = null;
				String [] args1 = null;
				String barRowNames = null;
				String barColNames = null;
				String barValues = null;
				String pictureUrls =null;
//System.out.println("--------------");					
//System.out.println(barRowNames);
//System.out.println(barValues);
//System.out.println(barColNames);
//System.out.println(chartTitle);
//System.out.println(nameVariable);
//System.out.println(deletePath);				
				if(("1").equals(chartType)){
					pictureUrl =request.getSession().getServletContext()
							.getRealPath("/pythonPicture/"+YandM).replaceAll("%20", " ")+"/"+nameVariable+".png";
					//拼装python脚本所需变量
					colors = AssembleStr(color); 
					//拼装rowName
					barRowNames = AssembleRowName(barRowName,rowValue,ptType);
					rowNameVal = AssembleStr(barRowNames); 
					args1 = new String[] { "python",request.getSession().getServletContext().getRealPath("/rms/mbgl/pythonScript/pie.py").replaceAll("%20", " "),rowNameVal,rowValue,colors,chartTitle,nameVariable,deletePath};
					pictureUrls = "pythonPicture/"+YandM+"/"+nameVariable+".png";
				}else if(("2").equals(chartType)){
					//柱状图折线图表列
					barRowNames = AssembleStr(barRowName); 
					//柱状图表行
					barColNames = AssembleStr(barColName); 
					//柱状图数值(以行为单位，用“,”隔开，换行则用“@”隔开)
					barValues = AssembleBarStr(barValue); 
					pictureUrl =request.getSession().getServletContext()
							.getRealPath("/pythonPicture/"+YandM).replaceAll("%20", " ")+"/"+nameVariable+".png";
					args1 = new String[] { "python",request.getSession().getServletContext().getRealPath("/rms/mbgl/pythonScript/bar.py").replaceAll("%20", " "),barRowNames,barValues,barColNames,chartTitle,nameVariable,deletePath};
					pictureUrls = "pythonPicture/"+YandM+"/"+nameVariable+".png";
				}else if(("3").equals(chartType)){
					//柱状图折线图表列
					barRowNames = AssembleStr(barRowName); 
					//柱状图表行
					barColNames = AssembleStr(barColName); 
					//柱状图数值(以行为单位，用“,”隔开，换行则用“@”隔开)
					barValues = AssembleBarStr(barValue); 
					pictureUrl =request.getSession().getServletContext()
							.getRealPath("/pythonPicture/"+YandM).replaceAll("%20", " ")+"/"+nameVariable+".png";
					args1 = new String[] { "python",request.getSession().getServletContext().getRealPath("/rms/mbgl/pythonScript/line.py").replaceAll("%20", " "),barRowNames,barValues,barColNames,chartTitle,nameVariable,deletePath};
					pictureUrls = "pythonPicture/"+YandM+"/"+nameVariable+".png";
				}
				Process pr= Runtime.getRuntime().exec(args1);
				BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				in.close();
				pr.waitFor();
				//组装需要的bean数据并转换为json返回
				File file = new File(pictureUrl);
				if(!file.exists()){
					pw.write("false");
					pw.flush();
				}else{
					//图片大小
					long pictureSize = file.length();
					LfTemplateChartVo bean = new LfTemplateChartVo();
					bean.setChartType(chartType);
					bean.setChartTitle(chartTitle);
					bean.setColor(color);
					bean.setPtType(ptType);
					bean.setRowValue(rowValue);
					bean.setBarColName(barColName);
					bean.setBarRowName(barRowName);
					bean.setBarTableVal(barTableVal);
					bean.setBarValue(barValue);
					bean.setPictureUrl(pictureUrls);
					bean.setPictureSize(pictureSize);
					bean.setParmValue(parmValue);
					bean.setRowNum(rowNum);
					bean.setColNum(colNum);
					pw.write(json.toJson(bean));
					pw.flush();
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "根据请求参数生成python的饼状图");
			}finally{
				if(pw!= null){
					pw.close();
				}
			}
		}
		private String AssembleBarStr(String barValue) {
			String [] arrBar = barValue.split("@");
			String returnStr = "[";
			for(int i=0;i<arrBar.length;i++){
				returnStr = returnStr+"["+arrBar[i]+"]"+",";
			}
			String str="";
			if(barValue.contains("@")){
				str = returnStr.substring(0, returnStr.length()-1)+"]";
			}else{
				String barValues [] = barValue.split(",");
				for(int i=0;i<barValues.length;i++){
					str =str+ "'"+barValues[i]+"',";
				}
				str =str.substring(0, str.length()-1);
			}
			return str;
		}
		/**
		 * @param str1:饼状图块名称
		 * @param str2:饼状图块数据
		 * @param str3:饼状图数据类型
		 * @return
		 */
		public String AssembleRowName(String str1,String str2,String str3){
			String resultStr = "";
			String [] arr1 = str1.split(",");
			String [] arr2 = str2.split(",");
			for(int i=0;i<arr1.length;i++){
				if(("1").equals(str3)){
					if(Integer.parseInt(arr2[i])>1000000){
						arr2[i] = BigDecimal.valueOf((float)Integer.parseInt(arr2[i])/10000).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()+"万";
					}
					resultStr += arr1[i]+"("+arr2[i]+")"+",";
				}else{
					resultStr += arr1[i]+"(n)"+",";
				}
			}
			return resultStr.substring(0,resultStr.length()-1);
		}
		
		public String AssembleStr(String str){
			String resultStr = "";
			String [] arr = str.split(",");
			for(int i=0;i<arr.length;i++){
				if(!str.contains(",")){
						resultStr += arr[i]+",";
					}else{
						resultStr += "'"+arr[i]+"',";
				}
			}
			return resultStr.substring(0,resultStr.length()-1);
		}
		// 删除文件夹中的图片
	 	public void deleteTmp(String path) {
	 		// 获得该路径
	 		File f = new File(path);
	 		// 获得该文件夹下的所有文件
	 		String[] list = f.list();
	 		File temp;
	 		// 删除结果
	 		boolean b = true;
	 		// 循环删除
	 		for (String s : list) {
	 			temp = new File(path + "/" + s);
	 			b = temp.delete();
	 			if (!b) {
	 				EmpExecutionContext.error("存在的图片删除不成功");	
	 			}else{
	 				EmpExecutionContext.info("存在的图片删除成功");	
	 			}
	 		}
	 	}
	 	
	 	/**
		 * 行业-用途列表 查询
		 * @return
		 */
		public Map<String,List<LfIndustryUse>> getIndustryUseList(HttpServletRequest request, HttpServletResponse response){
			PageInfo pageInfo = new PageInfo();
			List<LfIndustryUse> industryUseList = new ArrayList<LfIndustryUse>();
			//所有行业集合
			List<LfIndustryUse> industryList = new ArrayList<LfIndustryUse>();
			//所有用途集合
			List<LfIndustryUse> useList = new ArrayList<LfIndustryUse>();
			LfIndustryUse lfIndustryUse = new LfIndustryUse();
			
			//行业-用途名称
			String InDuName = request.getParameter("InDuName");
			if(!StringUtils.IsNullOrEmpty(InDuName)){
				lfIndustryUse.setName(InDuName);
			}
			
			//最终的返回结果集
			Map<String,List<LfIndustryUse>> map = new HashMap<String, List<LfIndustryUse>>();
			
			
			industryUseList = commonTemplateBiz.getIndustryUseList(lfIndustryUse, pageInfo);
			//根据type 进行分组
			for(int i=0;i<industryUseList.size();i++){
				LfIndustryUse lf = industryUseList.get(i);
				if(lf.getType().equals(0)){
					industryList.add(lf);
				}else if(lf.getType().equals(1)){
					useList.add(lf);
				}
			}
			map.put("industry", industryList);
			map.put("use", useList);
			return map;
		}
		/**
		 * 上传文件服务器
		 * @param fileUrL zip文件相对路径
		 * @return
		 */
		private boolean uploadFileCenter(String fileUrL){
			boolean flag = false;
			String uploadFileFlag= commBiz.uploadFileToFileCenter(fileUrL);
			if("success".equals(uploadFileFlag)){
				flag =  true;
			} 
			return flag;
		}
		
		/**
		 * V1.0从阿里云下载-解析rms文件
		 * @param filePath
		 * @return
		 */
		public boolean downLoadRmsFromOssV1(String filePath) {
			boolean flag = false;
			TxtFileUtil txtfileutil = new TxtFileUtil();
			dirUrl = txtfileutil.getWebRoot();
			String sourcPath =filePath.replace(dirUrl, "");
			String rmsZipName ="fuxin.zip";
			//判断文件夹是否存在，存在直接返回，不存在从阿里云下载
			File fDir = new File(filePath);
			if(!fDir.exists()){
				fDir.mkdirs();
			}else{
				File htmlFile = new File(filePath+"fuxin.html");
				File rmsFile = new File(filePath+"fuxin.rms");
				if(htmlFile.exists() && rmsFile.exists()){
					return true;
				}
			}
			/**
			 * //解析生成相关资源文件
		    	RmsEntity rEntity = analysisRMS.Parse(filePath+rmsName, filePath+"src/", rmsFileSize);
		    	//生成fuxin.html-用于预览
		    	String srcPath = (filePath+"src/").replace(dirUrl, "");
		    	analysisRMS.createHtml(filePath+"src/", srcPath, rEntity);
			 */
			if(oosUtil.downLoadFile(sourcPath, filePath, rmsZipName)){
				ZipUtil.unZip(filePath+rmsZipName); 
				//解析rms文件
				AnalysisRMS aRms = new AnalysisRMS();
				//获取fuxin.rms 文件大小
				File rmsFile = new File(filePath+"fuxin.rms");
				int fileByteSize =(int)rmsFile.length(); 
				//生成fuxin.html-用于预览
		    	String srcPath = (filePath+"src/").replace(dirUrl, "");
				RmsEntity rsRmsEntity=aRms.Parse(filePath+"fuxin.rms", filePath+"src/", fileByteSize);
				aRms.createHtml(filePath+"src/", srcPath, rsRmsEntity);
				flag = true;
				
			}
		     return flag;
		}
		
		/**
		 * 
		 * @param request
		 * @param response
		 * @throws IOException
		 */
		public void getTmMsgV1(HttpServletRequest request,
				HttpServletResponse response) throws IOException {
			response.setCharacterEncoding("UTF-8");
			TxtFileUtil txtfileutil = new TxtFileUtil();
			dirUrl = txtfileutil.getWebRoot();
			String htmlUrl = request.getParameter("tmUrl");
			String filePath = dirUrl + htmlUrl.replace("fuxin.rms", "fuxinPreview.html");
			String mms =null;
			String tmName  = request.getParameter("tmName");
			if(StringUtils.IsNullOrEmpty(tmName)){
				tmName ="";
			}
			//解决 + - 等特殊符号丢失的问题
//			tmName =  URLEncoder.encode(tmName, "UTF-8");
//			tmName =  URLDecoder.decode(tmName, "UTF-8");
//			tmName =  URLDecoder.decode(tmName, "UTF-8");
			try {
				if (filePath == null || "".equals(filePath)) {
					response.getWriter().print("源文件不存在");
					return;
				} else {
					//从阿里云下载
					boolean flag = downLoadRmsFromOssV1(filePath.replace("fuxinPreview.html", ""));
					if(!flag){
						response.getWriter().print("源文件不存在");
						return;
					}
					//根据smil文件生成 预览html
					String smilFileUrl ="";
					String  filePath1 = filePath.split("fuxinPreview.html")[0];
					String srcPath = (filePath1+"src/").replace(dirUrl, "");
					smilFileUrl = filePath1+"src/00.smil";
			    	//analysisRMS.createHtml(filePath+"src/", srcPath, rEntity);
					
					new AnalysisRMS().createPreviewHtml(filePath1+"src/", srcPath,"", smilFileUrl);
					
					//读取生成的用于预览的html:fuxinPreview.html
					File file = new File(filePath);
					StringBuffer sbuf = new StringBuffer();
				    if(file.isFile() && file.exists()) {
				      InputStreamReader isr = null;
				      BufferedReader br = null;
				      FileInputStream fis = null;
				      try{
				    	  fis = new FileInputStream(file);
					      isr = new InputStreamReader(fis, "UTF-8");
					      br = new BufferedReader(isr);
					      String lineTxt = null;
					      while ((lineTxt = br.readLine()) != null) {
					    	  sbuf.append(lineTxt);
					      }
				      }finally{
				    	  if(null != fis){
				    		  fis.close();
				    	  }
				    	  IOUtils.closeReaders(getClass(), br,isr);
				      }
				    }else{
				    	response.getWriter().print("源文件不存在");
						return;
				    	
				    }
				    
				    //插入标题
				    sbuf.insert(0, "<h4 style='margin-bottom: 10px;font-size: 14px;text-align: center;'>"+tmName+"</h4>");
					if (sbuf != null) {
						mms = sbuf.toString().replace("\r\n", "&lt;BR/&gt;");
						
						//判断文件是否含有参数进行模板参数替换
						String paramInFile = request.getParameter("paramInFile");
					    if(!StringUtils.IsNullOrEmpty(paramInFile)){
					    	paramInFile = URLDecoder.decode(paramInFile, "UTF-8");
					    	mms = parseTemplate(mms, paramInFile);
					    }
						mms =mms.replace("\n", "&lt;BR/&gt;");
					}	
				}
				response.getWriter().print(mms);
			} catch (Exception e) {
				response.getWriter().print("");
				EmpExecutionContext.error(e,"获取富信模板文件信息出现异常！");
			}
		}
		
		/**
		 * 获取参数所有个数
		 * @param html
		 * @return
		 */
		private int getHtmlParamCount(String html) {
//System.out.println(html);			
			Document doc = Jsoup.parseBodyFragment(html);
			Elements eles = doc.body().getElementsByClass("J-keyframe");
			int paramCount = 0;
			for (Element ele : eles) {
				//文参个数
				paramCount = paramCount + getTemplateCount(ele.html());
				//图参个数
				paramCount  = paramCount + getImgParamCount(ele.html());
				//报表标题个数
				Integer reportTitleCount = 0;
				//行标题个数
				Integer rowTitleCount = 0;
				//列标题个数
				Integer colTitleCount = 0;
				//数据个数
				Integer dataCount = 0;
				//报表参数个数
				 if (!ele.getElementsByAttributeValue("data-type", "chart").isEmpty()) {
						JSONObject jsonObj = JSONObject.parseObject(ele.getElementsByClass("J-chart-data").text());
						String chartParamValue = jsonObj.getString("parmValue");
						//假如需要对Unicode字符进行大小不明感的匹配，加上(?iu) eg:(?iu){#行标题[1-9]?\d#}
						for(String str : chartParamValue.split(",")){
							if(str.matches("\\{#标题#}")){
								reportTitleCount++;
							}else if(str.matches("\\{#行标题[1-9]?\\d#}")){
								rowTitleCount++;
							}else if(str.matches("\\{#列标题[1-9]?\\d#}")){
								colTitleCount++;
							}else if(str.matches("\\{#[1-9]?\\d行[1-9]?\\d列#}")){
								dataCount++;
							}
						}
						/*
						paramCount = paramCount+getChartTitleCount(chartParamValue);
						paramCount = paramCount+getRowTitleCount(chartParamValue);
						paramCount = paramCount+getColTitleCount(chartParamValue);
						paramCount  = paramCount + getChartParamCount(chartParamValue);
						*/
					 paramCount += reportTitleCount + rowTitleCount + colTitleCount + dataCount;
				 }
			}
			
			return paramCount;
		}
		/**
		 * 报表列标题参数个数
		 */
		private int getColTitleCount(String tmsMsg) {
			String eg = "\\{#列标题[1-9][0-9]*#\\}";
			Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(tmsMsg);
			int paramCount = 0;
			while (m.find()) {
				paramCount++;
			}
			return paramCount;
		}
		/**
		 * 报表行标题参数个数
		 */
		private int getRowTitleCount(String tmsMsg) {
			String eg = "\\{#行标题[1-9][0-9]*#\\}";
			Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(tmsMsg);
			int paramCount = 0;
			while (m.find()) {
				paramCount++;
			}
			return paramCount;
		}
}


