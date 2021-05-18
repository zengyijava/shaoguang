package com.montnets.emp.rms.commontempl.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.google.gson.Gson;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.rms.commontempl.biz.CommonTemplateBiz;
import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.commontempl.entity.LfTemplate;
import com.montnets.emp.rms.rmsapi.util.OOSUtil;
import com.montnets.emp.rms.servmodule.constant.ServerInof;
import com.montnets.emp.rms.templmanage.biz.RmsShortTemplateBiz;
import com.montnets.emp.rms.templmanage.servlet.Mbgl_templateSvt;
import com.montnets.emp.rms.tools.AnalysisRMS;
import com.montnets.emp.rms.tools.ExcelTool;
import com.montnets.emp.rms.tools.ParamTool;
import com.montnets.emp.rms.tools.RmsEntity;
import com.montnets.emp.rms.tools.ZipUtil;
import com.montnets.emp.rms.vo.LfShortTemplateVo;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.rms.vo.Param;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 
 * @ClassName: CommonTemplateSvt
 * @Description: 公共模板管理类
 * @author xuty
 * @date 2018-3-19 下午2:33:33
 * 
 */
public class CommonTemplateSvt extends BaseServlet {
	private final String PATH = "rms/commontempl";
	private final CommonTemplateBiz commonTemplateBiz = new CommonTemplateBiz();
	private final OOSUtil oosUtil = new OOSUtil();
	final CommonBiz commBiz = new CommonBiz();
	/**
	 * 
	 */
	private static final long serialVersionUID = -499920464422057327L;

	/**
	 * 查询
	 * 
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		try {
			LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
					.getAttribute(StaticValue.SESSION_USER_KEY);
			PageInfo pageInfo = new PageInfo();
			LfTemplateVo lfTemplate = new LfTemplateVo();
			Map<String, List<LfIndustryUse>> induUseMap = null;
			List<LfIndustryUse> industryList = null;
			List<LfIndustryUse> useList = null;
			// 当前登录企业编号
			String loginOrgcode = loginSysuser.getCorpCode();
			lfTemplate.setCorpCode(loginOrgcode);
			// 模板类型-11：富信模板
			lfTemplate.setTmpType(11);
			lfTemplate.setIsPublic(1);// 是否为公共模板 0 -不是，1-是
			pageInfo.setPageSize(9);// 100000账号设置每页9条数据
			if (!"100000".equals(loginOrgcode)) {
				//lfTemplate.setAuditstatus(1);// 审核通过
			
				pageInfo.setPageSize(10);// 设置每页10条数据
			}  
			
			// 模板名称/ID
			String key = "";
			if (!StringUtils.IsNullOrEmpty(request.getParameter("templName"))) {
				key = new String(request.getParameter("templName").getBytes("ISO-8859-1"),"UTF-8");
			}
			// 模板审核状态
			String templStatus = request.getParameter("templStatus");
			if (!StringUtils.IsNullOrEmpty(templStatus)
					&& !"-2".equals(templStatus)) {// -2为全部
				lfTemplate.setAuditstatus(Integer.parseInt(templStatus));
			}
			// 行业-用途类型
			String type = request.getParameter("type");
			// 行业ID
			String InduOrUseId = request.getParameter("InduOrUseId");

			if (!StringUtils.IsNullOrEmpty(type)
					&& !StringUtils.IsNullOrEmpty(InduOrUseId)) {
				if (type.equals("0")) {
					// 行业ID
					lfTemplate.setIndustryid(Integer.parseInt(InduOrUseId));
				} else if (type.equals("1")) {
					// //用途
					lfTemplate.setUseid(Integer.parseInt(InduOrUseId));
				}

			}

			//行业div 高度
			String indudvHeight = request.getParameter("indudv_height");
			//用途div 高度
			String usedvHeight = request.getParameter("usedv_height");
			// 是否第一次打开
			boolean isFirstEnter = false;

			isFirstEnter = pageSet(pageInfo, request);

			String templName = request.getParameter("templName");
			if (!StringUtils.IsNullOrEmpty(templName)) {
				lfTemplate.setTmName( new String(templName.getBytes("ISO-8859-1"),"UTF-8"));

			}
			LfShortTemplateVo lv = new LfShortTemplateVo();
			lv.setCorpCode(loginOrgcode);
			//快捷场景集合
			List<LfShortTemplateVo> lfShortTemList = new RmsShortTemplateBiz().getLfShortTemList(lv);
			
			LfTemplateVo lfClone = (LfTemplateVo) BeanUtils.cloneBean(lfTemplate);
			lfClone.setTmName(null);//用于发送页面的查询处理
			// 公共模板列表
			List<LfTemplateVo> commonTemList = commonTemplateBiz
					.getCommonTempalateList(lfClone, pageInfo, key);
			
			//设置快捷场景标识
			for(LfTemplateVo lf : commonTemList){
				for(LfShortTemplateVo sv : lfShortTemList){
					if(sv.getTempId().equals(lf.getTmid())){
						lf.setIsShortTemp(1);//1为快捷场景
					}
				}
			}
			TxtFileUtil txtfileutil = new TxtFileUtil();
			//遍历mmsList 中的每一个对象是否含有第一帧html,没有就从阿里云下载
			txtfileutil = new TxtFileUtil();
			String dirUrl = null;
			dirUrl = txtfileutil.getWebRoot();
			for(LfTemplateVo lf : commonTemList){
				String firstFramePath = dirUrl + lf.getTmMsg().replace("fuxin.rms", "firstframe.jsp");
				File fFrame  = new File(firstFramePath);
				String tmid = lf.getTmid().toString();
				String filePath = dirUrl + lf.getTmMsg().replace("fuxin.rms", "");
				if(!fFrame.exists()){//不存在则从阿里云下载
					long startTM = System.currentTimeMillis();					
					boolean downLoadRmsFromOss = new Mbgl_templateSvt().downLoadRmsFromOss(request,filePath, tmid,"");
					long endTM = System.currentTimeMillis();
					EmpExecutionContext.info("模板ID"+lf.getTmid()+",rms 解析耗时："+(endTM - startTM)+ " ms");
					if(!downLoadRmsFromOss){
						lf.setTmMsg("rms/mbgl/404.jsp");
					}
				}
			}
			
			
			// 行业-用途列表
			induUseMap = getIndustryUseList(request, response);
			industryList = induUseMap.get("industry");
			useList = induUseMap.get("use");

			request.setAttribute("commonTemList", commonTemList);
			request.setAttribute("industryList", industryList);
			request.setAttribute("useList", useList);
			request.setAttribute("pageInfo", pageInfo);
			// 用于页面回显
			request.setAttribute("lfTemplate", lfTemplate);
			request.setAttribute("InduOrUseId", InduOrUseId);
			request.setAttribute("indudvHeight", indudvHeight);
			request.setAttribute("usedvHeight", usedvHeight);
			// 新增审核状态回显
			request.setAttribute("templStatus", templStatus);

			new ServerInof().setServerName(getServletContext().getServerInfo());
			request.getRequestDispatcher(PATH + "/common_template.jsp")
					.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "公共模板查询出现异常");
		}

	}
	public void loadIndustryUse(HttpServletRequest request, HttpServletResponse response){
		PrintWriter pw =null;
		Gson json = new Gson();
		Map<String, List<LfIndustryUse>> induUseMap = null;
		try{
			pw = response.getWriter();
			induUseMap = getIndustryUseList(request, response);
			pw.write(json.toJson(induUseMap));
		}catch (Exception e){
			EmpExecutionContext.error(e,"查询富信模板出现异常！");
		}finally{
			if(null != pw){
				pw.close();
			}
		}
	}

	/**
	 * 行业-用途列表 查询
	 * 
	 * @return
	 */
	public Map<String, List<LfIndustryUse>> getIndustryUseList(
			HttpServletRequest request, HttpServletResponse response) {
		PageInfo pageInfo = new PageInfo();
		List<LfIndustryUse> industryUseList = new ArrayList<LfIndustryUse>();
		// 所有行业集合
		List<LfIndustryUse> industryList = new ArrayList<LfIndustryUse>();
		// 所有用途集合
		List<LfIndustryUse> useList = new ArrayList<LfIndustryUse>();
		LfIndustryUse lfIndustryUse = new LfIndustryUse();

		// 行业-用途名称
		String InDuName = request.getParameter("InDuName");
		if (!StringUtils.IsNullOrEmpty(InDuName)) {
			lfIndustryUse.setName(InDuName);
		}

		// 最终的返回结果集
		Map<String, List<LfIndustryUse>> map = new HashMap<String, List<LfIndustryUse>>();

		industryUseList = commonTemplateBiz.getIndustryUseList(lfIndustryUse,
				pageInfo);
		// 根据type 进行分组
		for (int i = 0; i < industryUseList.size(); i++) {
			LfIndustryUse lf = industryUseList.get(i);
			if (lf.getType().equals(0)) {
				industryList.add(lf);
			} else if (lf.getType().equals(1)) {
				useList.add(lf);
			}
		}
		map.put("industry", industryList);
		map.put("use", useList);
		return map;
	}

	/**
	 * 行业-用途 添加
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) {
		LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
				.getAttribute(StaticValue.SESSION_USER_KEY);

		// 类型 0-行业，1-用途,默认-1 未知
		Integer type = -1;
		if (!StringUtils.IsNullOrEmpty(request.getParameter("type"))) {
			type = Integer.parseInt(request.getParameter("type"));
		}
		// 名称
		String name = "";
		if (!StringUtils.IsNullOrEmpty(request.getParameter("iuName"))) {
			name = request.getParameter("iuName");
		}

		//
		Long userId = loginSysuser.getUserId();

		LfIndustryUse lfIndustryUse = new LfIndustryUse();
		lfIndustryUse.setName(name);
		lfIndustryUse.setOperator(userId);
		lfIndustryUse.setType(type);
		lfIndustryUse.setCreatetm(new Timestamp(System.currentTimeMillis()));
		lfIndustryUse.setUpdatetm(new Timestamp(System.currentTimeMillis()));
		StringBuffer result = new StringBuffer();
		PrintWriter pw = null;
		try {
			Long saveReturnId = 0L;
			//判断添加的行业或用途是否存在
			LfIndustryUse lf = new LfIndustryUse();
			//根据行业-用途名称 查询出唯一的一条记录
			lf.setName(name);
			lf.setType(type);//区分行业还是用途-行业、用途的名称一样时，也可添加
			List<LfIndustryUse> list = commonTemplateBiz.getIndustryUseList(lf,null);
			if(null != list && list.size() > 0){
				//多语言支持 对应mbgl_addTemplate.jsp页面769行判断条件 
//				result.append("添加名称已存在，请重新输入！");
				result.append(org.apache.commons.lang.StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_pubscene_nameexist", request), "添加名称已存在，请重新输入！"));
			}else {
				saveReturnId = commonTemplateBiz.addObjReturnId(lfIndustryUse);
				if (saveReturnId > 0) {
					result.append(saveReturnId);
				} else {
					result.append("fault");
				}
			}
			

			// CommonTemplateBiz commonTemplateBiz = new CommonTemplateBiz();
			// 再进行查询显示在页面
			// getIndustryUseList(request, response);
			pw = response.getWriter();
			pw.write(result.toString());
		} catch (Exception e) {
			EmpExecutionContext.error(e, "行业-用途Svt层添加出现异常！");
		} finally {
			if (null != pw) {
				pw.close();
			}
		}

	}

	/**
	 * 行业-用途 删除
	 * 
	 * @param request
	 * @param response
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		String idStr = "";
		if (!StringUtils.IsNullOrEmpty(request.getParameter("id"))) {
			idStr = request.getParameter("id");
		}
		StringBuffer result = new StringBuffer();
		PrintWriter pw = null;
		try {
			if (commonTemplateBiz.deleteByIds(LfIndustryUse.class, idStr) != null) {
				result.append(idStr);
			} else {
				result.append("falut");
			}
			pw = response.getWriter();
			pw.write(result.toString());
		} catch (Exception e) {
			EmpExecutionContext.error(e, "行业-管理SVT层删除出现异常！");
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}

	/**
	 * 行业-用途 修改
	 * 
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) {
		LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
				.getAttribute(StaticValue.SESSION_USER_KEY);
		
		// 类型 0-行业，1-用途,默认-1 未知
		Integer type = -1;
		if (!StringUtils.IsNullOrEmpty(request.getParameter("type"))) {
			type = Integer.parseInt(request.getParameter("type"));
		}
		// ID
		Long inUseId = 0L;
		if (!StringUtils.IsNullOrEmpty(request.getParameter("id"))) {
			inUseId = Long.parseLong(request.getParameter("id"));
		}
		// 名称
		String name = "";
		if (!StringUtils.IsNullOrEmpty(request.getParameter("name"))) {
			name = request.getParameter("name");
		}
		// 操作员ID
		Long userId = loginSysuser.getUserId();
		LfIndustryUse lfIndustryUse = new LfIndustryUse();
		lfIndustryUse.setName(name);
		lfIndustryUse.setId(inUseId);
		lfIndustryUse.setOperator(userId);
		StringBuffer result = new StringBuffer();
		PrintWriter pw = null;
		try {
			//查询该名称是否存在
			//判断添加的行业或用途是否存在
			LfIndustryUse lf = new LfIndustryUse();
			//根据行业-用途名称 查询出唯一的一条记录
			lf.setName(name);
			lf.setType(type);//区分行业还是用途-行业、用途的名称一样时，也可添加
			List<LfIndustryUse> list = commonTemplateBiz.getIndustryUseList(lf,null);
			if(null != list && list.size() > 0){
				//多语言支持 对应mbgl_editTemplate.jsp页面834行判断条件 
//				result.append("添加名称已存在，请重新输入！");
				result.append(org.apache.commons.lang.StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_pubscene_nameexist", request), "添加名称已存在，请重新输入！"));
			}else{
				// commonTemplateBiz.updateObj(lfIndustryUse);
				if (commonTemplateBiz.updateIndustryOrUse(lfIndustryUse)) {
					result.append(name);
				} else {
					result.append("falut");
				}
			}
			pw = response.getWriter();
			pw.write(result.toString());
		} catch (Exception e) {
			EmpExecutionContext.error(e, "行业-管理SVT层更新出现异常！");
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}

	/**
	 * 预览
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getTmMsg(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		TxtFileUtil txtfileutil = new TxtFileUtil();
		String dirUrl = null;
		dirUrl = txtfileutil.getWebRoot();
		String htmlUrl = request.getParameter("tmUrl");
		String filePath = dirUrl
				+ htmlUrl.replace("fuxin.rms", "fuxinPreview.html");
		String mms = null;
		String tmName = request.getParameter("tmName");
		
		String tmId = request.getParameter("tmId");
		PrintWriter pw = null;
		pw = response.getWriter();

		if (StringUtils.IsNullOrEmpty(tmName)) {
			tmName = "";
		}
		// 解决 + - 等特殊符号丢失的问题
		// tmName = URLEncoder.encode(tmName, "UTF-8");
		// tmName = URLDecoder.decode(tmName, "UTF-8");
		// tmName = URLDecoder.decode(tmName, "UTF-8");
		try {
			//多语言处理
			String noSourcefile = org.apache.commons.lang.StringUtils.defaultIfEmpty(
					MessageUtils.extractMessage("rms", "rms_scenepreview_nosource", request), "源文件不存在");
			if (filePath == null || "".equals(filePath)) {
//				pw.print("源文件不存在");
				pw.write(noSourcefile);
				return;
			} else {
				// 从阿里云下载
				boolean flag = downLoadRmsFromOss(request,filePath.replace("fuxinPreview.html", ""),tmId);
//				boolean flag = new Mbgl_templateSvt().downLoadRmsFromOss(request,filePath, tmId,"needPreview");
				if (!flag) {
//					pw.print("源文件不存在");
					pw.write(noSourcefile);
					return;
				}
				// 根据smil文件生成 预览html
				String smilFileUrl = "";
				String filePath1 = filePath.split("fuxinPreview.html")[0];
				String srcPath = (filePath1 + "src/").replace(dirUrl, "");
				smilFileUrl = filePath1 + "src/00.smil";
				// analysisRMS.createHtml(filePath+"src/", srcPath, rEntity);

				new AnalysisRMS().createPreviewHtml(filePath1 + "src/",
						srcPath, "", smilFileUrl,false);

				// 读取生成的用于预览的html:fuxinPreview.html
				File file = new File(filePath);
				StringBuffer sbuf = new StringBuffer();
				if (file.isFile() && file.exists()) {
					InputStreamReader isr = null;
					BufferedReader br = null;
					FileInputStream fis = null;
					try{
						fis = new FileInputStream(file);
						isr = new InputStreamReader(
								fis, "UTF-8");
						 br = new BufferedReader(isr);
						String lineTxt = null;
						while ((lineTxt = br.readLine()) != null) {
							sbuf.append(lineTxt);
						}
					}finally{
						if(null != fis){
							fis.close();
						}
						if(null != isr){
							isr.close();
						}
						if(null != br){
							br.close();
						}
					}
				} else {
//					pw.print("源文件不存在");
					pw.write(noSourcefile);
					return;

				}

				// 插入标题
				sbuf.insert(0, "<h4 style='margin-bottom: 10px;font-size: 14px;text-align: center;'>" + tmName + "</h4>");
				if (sbuf != null) {
					mms = sbuf.toString().replace("\r\n", "&lt;BR/&gt;");

					// 判断文件是否含有参数进行模板参数替换
					String paramInFile = request.getParameter("paramInFile");
					if (!StringUtils.IsNullOrEmpty(paramInFile)) {
						paramInFile = URLDecoder.decode(paramInFile, "UTF-8");
						mms = parseTemplate(mms, paramInFile);
					}
					mms = mms.replace("\n", "&lt;BR/&gt;");
				}
			}
			response.getWriter().print(mms);
		} catch (Exception e) {
			pw.print("");
			EmpExecutionContext.error(e, "获取富信模板文件信息出现异常！");
		} finally {
			if (null != pw) {
				pw.flush();
				pw.close();
			}

		}
	}

	/**
	 * 参数替换
	 * 
	 * @param tmpContent
	 * @param param
	 * @return
	 */
	private String parseTemplate(String tmpContent, String param) {
		String result = tmpContent;
		String[] paramArr = param.split(",");
		String eg = "#[pP]_[1-9][0-9]*#";
		Matcher m = Pattern.compile(eg,
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(
				tmpContent);
		String paramStr = "";
		String pc = "";
		tmpContent = " " + tmpContent + " ";
		String[] splitTplContent = tmpContent.split(eg);
		int count = 0;
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			sb.append(splitTplContent[count]);
			paramStr = m.group();
			paramStr = paramStr.toUpperCase();
			pc = paramStr.substring(paramStr.indexOf("#P_") + 3,
					paramStr.lastIndexOf("#"));
			int paramPos = Integer.parseInt(pc);
			count++;
			sb.append(paramArr[paramPos - 1].replaceAll("&", "&amp;")
					.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
					.replaceAll("\"", "&quot;").replaceAll("'", "&apos;"));
		}
		sb.append(splitTplContent[count]);
		result = sb.toString();
		return result.trim();
	}

	/**
	 * 从阿里云下载rms文件
	 * 
	 * @param filePath
	 * @return
	 */
	public boolean downLoadRmsFromOss(HttpServletRequest request,String filePath,String tmid) {
		boolean flag = false;
		TxtFileUtil txtfileutil = new TxtFileUtil();
		String dirUrl = null;
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
			if(htmlFile.exists() && rmsFile.exists() && previewFile.exists()){
				flag = true;
				return flag;
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
		String downFileZip = commBiz.downFileFromFileCenWhitZip(downFileZipUrl);
		
		if(!"success".equals(downFileZip)){//文件服务器没有下载成功,则从阿里云下载 zip
			if(oosUtil.downLoadFile(sourcPath,destPath, rmsZipName)){
				//之前是只上传fuxin.rms 文件到阿里云，需要下载下来后再反解析rms 文件为资源文件，现在直接将源文件和fuxin.rms 文件打包上传，直接下载解压即可
				ZipUtil.unZip((filePath+"fuxin.rms").replaceAll(tmid+"/fuxin.rms", "")+rmsZipName); 
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
	 * 记录模板使用次数
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void updateUseCount(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		LfTemplate lf = new LfTemplate();
		// 模板自增ID
		String tmid = request.getParameter("tmid");
		if (!StringUtils.IsNullOrEmpty(tmid)) {
			lf.setTmid(Long.parseLong(tmid));
		}
		// 模板使用次数
		String usecount = request.getParameter("usecount");
		if (!StringUtils.IsNullOrEmpty(tmid)) {
			lf.setUsecount(Long.parseLong(usecount));
		}
		PrintWriter pw = null;
		StringBuffer sbf = new StringBuffer();
		pw = response.getWriter();
		try {
			if (commonTemplateBiz.updateUseCount(lf)) {
				sbf.append("success");
			} else {
				sbf.append("fault");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "更新模板使用次数出现异常！");
		} finally {
			pw.write(sbf.toString());
			if (null != pw) {
				pw.flush();
				pw.close();
			}
		}
	}

	/**
	 * 下载手机号码Excel文件
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("static-access")
	public void downPhoneFile(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String filePath = request.getParameter("tmMsg");
			//模板自增ID
			String tmId = request.getParameter("tmId");
			
			if(!StringUtils.IsNullOrEmpty(filePath) && !StringUtils.IsNullOrEmpty(tmId)){
				String dirUrl = null;
				dirUrl = new TxtFileUtil().getWebRoot();
				filePath = dirUrl + filePath.replace("fuxin.rms", "fuxin.html");
				// 1.根据tmid 读取fuxin.html
				String html = getHtml(filePath);
				// 2.解析fuxin.html
				List<List<Param>> phoneParamList = new ParamTool().convertParam(html);
				// 3.组装 号码文件EXCEL表头
				SimpleDateFormat FMT = new SimpleDateFormat("yyyyMMddHHmmss");
				String excleFileName = FMT.format(new Date()) + "_phone.xls";
				HSSFWorkbook wk = ExcelTool.crateExcel(Long.parseLong(tmId),phoneParamList, false);
				// 4.生成号码EXCEL 文件
				exportExcel(excleFileName, wk, response);
			}else{
				
				
			}
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "下载号码文件出异常！");
		}

	}

	/**
	 * 获取fuxin.html内容
	 * 
	 * @param filePath
	 * @return
	 */
	public String getHtml(String filePath) {
		StringBuffer sbuf = new StringBuffer();
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			sbuf = new StringBuffer();
			// 读取生成的用于预览的html:fuxinPreview.html
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
				br = new BufferedReader(isr);
				String lineTxt = null;
				while ((lineTxt = br.readLine()) != null) {
					sbuf.append(lineTxt);
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取fuxin.html出异常！");
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,
							"获取fuxin.html关闭BufferedReader流出异常！");
				}
			}
			if (null != isr) {
				try {
					isr.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,
							"获取fuxin.html关闭InputStreamReader流出异常！");
				}
			}
		}
		return sbuf.toString();
	}

	// 下载
	public void exportExcel(String excleFileName, HSSFWorkbook wk,
			HttpServletResponse response) throws ServletException, IOException {
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.reset();// 清空输出流
			// 解决中文文件名乱码
			response.setHeader("Content-disposition", "attachment;filename="
					+ new String(excleFileName.getBytes("GBK"), "ISO-8859-1"));
			// 定义输出文件类型为Excel
			response.setContentType("application/ynd.ms-excel;charset=UTF-8");
			// 将wk表个对象写入OutputStream流中并进行输出
			wk.write(out);

		} catch (Exception e) {
			EmpExecutionContext.error(e, "下载手机号码文件出现异常！");
		} finally {
			if (null != out) {
				out.flush();
				out.close();
			}

		}
	}

	/**
	 * 用于新增模板的时候就生成LF_TEMPALTE 表的  EXTJSON 字段参数
	 * @param filePath
	 * @param tmId
	 */
	public void downPhoneFileV1(String filePath,String tmId) {
		try {
			if(!StringUtils.IsNullOrEmpty(filePath) && !StringUtils.IsNullOrEmpty(tmId)){
				String dirUrl = new TxtFileUtil().getWebRoot();
				filePath = dirUrl + filePath.replace("fuxin.rms", "fuxin.html");
				// 1.根据tmid 读取fuxin.html
				String html = getHtml(filePath);
				// 2.解析fuxin.html
				List<List<Param>> phoneParamList = new ParamTool().convertParam(html);
				ExcelTool.crateExcel(Long.parseLong(tmId),phoneParamList,true);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "下载号码文件出异常！");
		}

	}
}
