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
 * @Description: ?????????????????????
 * @author xuty
 * @date 2018-3-19 ??????2:33:33
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
	 * ??????
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
			// ????????????????????????
			String loginOrgcode = loginSysuser.getCorpCode();
			lfTemplate.setCorpCode(loginOrgcode);
			// ????????????-11???????????????
			lfTemplate.setTmpType(11);
			lfTemplate.setIsPublic(1);// ????????????????????? 0 -?????????1-???
			pageInfo.setPageSize(9);// 100000??????????????????9?????????
			if (!"100000".equals(loginOrgcode)) {
				//lfTemplate.setAuditstatus(1);// ????????????
			
				pageInfo.setPageSize(10);// ????????????10?????????
			}  
			
			// ????????????/ID
			String key = "";
			if (!StringUtils.IsNullOrEmpty(request.getParameter("templName"))) {
				key = new String(request.getParameter("templName").getBytes("ISO-8859-1"),"UTF-8");
			}
			// ??????????????????
			String templStatus = request.getParameter("templStatus");
			if (!StringUtils.IsNullOrEmpty(templStatus)
					&& !"-2".equals(templStatus)) {// -2?????????
				lfTemplate.setAuditstatus(Integer.parseInt(templStatus));
			}
			// ??????-????????????
			String type = request.getParameter("type");
			// ??????ID
			String InduOrUseId = request.getParameter("InduOrUseId");

			if (!StringUtils.IsNullOrEmpty(type)
					&& !StringUtils.IsNullOrEmpty(InduOrUseId)) {
				if (type.equals("0")) {
					// ??????ID
					lfTemplate.setIndustryid(Integer.parseInt(InduOrUseId));
				} else if (type.equals("1")) {
					// //??????
					lfTemplate.setUseid(Integer.parseInt(InduOrUseId));
				}

			}

			//??????div ??????
			String indudvHeight = request.getParameter("indudv_height");
			//??????div ??????
			String usedvHeight = request.getParameter("usedv_height");
			// ?????????????????????
			boolean isFirstEnter = false;

			isFirstEnter = pageSet(pageInfo, request);

			String templName = request.getParameter("templName");
			if (!StringUtils.IsNullOrEmpty(templName)) {
				lfTemplate.setTmName( new String(templName.getBytes("ISO-8859-1"),"UTF-8"));

			}
			LfShortTemplateVo lv = new LfShortTemplateVo();
			lv.setCorpCode(loginOrgcode);
			//??????????????????
			List<LfShortTemplateVo> lfShortTemList = new RmsShortTemplateBiz().getLfShortTemList(lv);
			
			LfTemplateVo lfClone = (LfTemplateVo) BeanUtils.cloneBean(lfTemplate);
			lfClone.setTmName(null);//?????????????????????????????????
			// ??????????????????
			List<LfTemplateVo> commonTemList = commonTemplateBiz
					.getCommonTempalateList(lfClone, pageInfo, key);
			
			//????????????????????????
			for(LfTemplateVo lf : commonTemList){
				for(LfShortTemplateVo sv : lfShortTemList){
					if(sv.getTempId().equals(lf.getTmid())){
						lf.setIsShortTemp(1);//1???????????????
					}
				}
			}
			TxtFileUtil txtfileutil = new TxtFileUtil();
			//??????mmsList ??????????????????????????????????????????html,???????????????????????????
			txtfileutil = new TxtFileUtil();
			String dirUrl = null;
			dirUrl = txtfileutil.getWebRoot();
			for(LfTemplateVo lf : commonTemList){
				String firstFramePath = dirUrl + lf.getTmMsg().replace("fuxin.rms", "firstframe.jsp");
				File fFrame  = new File(firstFramePath);
				String tmid = lf.getTmid().toString();
				String filePath = dirUrl + lf.getTmMsg().replace("fuxin.rms", "");
				if(!fFrame.exists()){//??????????????????????????????
					long startTM = System.currentTimeMillis();					
					boolean downLoadRmsFromOss = new Mbgl_templateSvt().downLoadRmsFromOss(request,filePath, tmid,"");
					long endTM = System.currentTimeMillis();
					EmpExecutionContext.info("??????ID"+lf.getTmid()+",rms ???????????????"+(endTM - startTM)+ " ms");
					if(!downLoadRmsFromOss){
						lf.setTmMsg("rms/mbgl/404.jsp");
					}
				}
			}
			
			
			// ??????-????????????
			induUseMap = getIndustryUseList(request, response);
			industryList = induUseMap.get("industry");
			useList = induUseMap.get("use");

			request.setAttribute("commonTemList", commonTemList);
			request.setAttribute("industryList", industryList);
			request.setAttribute("useList", useList);
			request.setAttribute("pageInfo", pageInfo);
			// ??????????????????
			request.setAttribute("lfTemplate", lfTemplate);
			request.setAttribute("InduOrUseId", InduOrUseId);
			request.setAttribute("indudvHeight", indudvHeight);
			request.setAttribute("usedvHeight", usedvHeight);
			// ????????????????????????
			request.setAttribute("templStatus", templStatus);

			new ServerInof().setServerName(getServletContext().getServerInfo());
			request.getRequestDispatcher(PATH + "/common_template.jsp")
					.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "??????????????????????????????");
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
			EmpExecutionContext.error(e,"?????????????????????????????????");
		}finally{
			if(null != pw){
				pw.close();
			}
		}
	}

	/**
	 * ??????-???????????? ??????
	 * 
	 * @return
	 */
	public Map<String, List<LfIndustryUse>> getIndustryUseList(
			HttpServletRequest request, HttpServletResponse response) {
		PageInfo pageInfo = new PageInfo();
		List<LfIndustryUse> industryUseList = new ArrayList<LfIndustryUse>();
		// ??????????????????
		List<LfIndustryUse> industryList = new ArrayList<LfIndustryUse>();
		// ??????????????????
		List<LfIndustryUse> useList = new ArrayList<LfIndustryUse>();
		LfIndustryUse lfIndustryUse = new LfIndustryUse();

		// ??????-????????????
		String InDuName = request.getParameter("InDuName");
		if (!StringUtils.IsNullOrEmpty(InDuName)) {
			lfIndustryUse.setName(InDuName);
		}

		// ????????????????????????
		Map<String, List<LfIndustryUse>> map = new HashMap<String, List<LfIndustryUse>>();

		industryUseList = commonTemplateBiz.getIndustryUseList(lfIndustryUse,
				pageInfo);
		// ??????type ????????????
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
	 * ??????-?????? ??????
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) {
		LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
				.getAttribute(StaticValue.SESSION_USER_KEY);

		// ?????? 0-?????????1-??????,??????-1 ??????
		Integer type = -1;
		if (!StringUtils.IsNullOrEmpty(request.getParameter("type"))) {
			type = Integer.parseInt(request.getParameter("type"));
		}
		// ??????
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
			//??????????????????????????????????????????
			LfIndustryUse lf = new LfIndustryUse();
			//????????????-???????????? ??????????????????????????????
			lf.setName(name);
			lf.setType(type);//????????????????????????-????????????????????????????????????????????????
			List<LfIndustryUse> list = commonTemplateBiz.getIndustryUseList(lf,null);
			if(null != list && list.size() > 0){
				//??????????????? ??????mbgl_addTemplate.jsp??????769??????????????? 
//				result.append("??????????????????????????????????????????");
				result.append(org.apache.commons.lang.StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_pubscene_nameexist", request), "??????????????????????????????????????????"));
			}else {
				saveReturnId = commonTemplateBiz.addObjReturnId(lfIndustryUse);
				if (saveReturnId > 0) {
					result.append(saveReturnId);
				} else {
					result.append("fault");
				}
			}
			

			// CommonTemplateBiz commonTemplateBiz = new CommonTemplateBiz();
			// ??????????????????????????????
			// getIndustryUseList(request, response);
			pw = response.getWriter();
			pw.write(result.toString());
		} catch (Exception e) {
			EmpExecutionContext.error(e, "??????-??????Svt????????????????????????");
		} finally {
			if (null != pw) {
				pw.close();
			}
		}

	}

	/**
	 * ??????-?????? ??????
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
			EmpExecutionContext.error(e, "??????-??????SVT????????????????????????");
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}

	/**
	 * ??????-?????? ??????
	 * 
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) {
		LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
				.getAttribute(StaticValue.SESSION_USER_KEY);
		
		// ?????? 0-?????????1-??????,??????-1 ??????
		Integer type = -1;
		if (!StringUtils.IsNullOrEmpty(request.getParameter("type"))) {
			type = Integer.parseInt(request.getParameter("type"));
		}
		// ID
		Long inUseId = 0L;
		if (!StringUtils.IsNullOrEmpty(request.getParameter("id"))) {
			inUseId = Long.parseLong(request.getParameter("id"));
		}
		// ??????
		String name = "";
		if (!StringUtils.IsNullOrEmpty(request.getParameter("name"))) {
			name = request.getParameter("name");
		}
		// ?????????ID
		Long userId = loginSysuser.getUserId();
		LfIndustryUse lfIndustryUse = new LfIndustryUse();
		lfIndustryUse.setName(name);
		lfIndustryUse.setId(inUseId);
		lfIndustryUse.setOperator(userId);
		StringBuffer result = new StringBuffer();
		PrintWriter pw = null;
		try {
			//???????????????????????????
			//??????????????????????????????????????????
			LfIndustryUse lf = new LfIndustryUse();
			//????????????-???????????? ??????????????????????????????
			lf.setName(name);
			lf.setType(type);//????????????????????????-????????????????????????????????????????????????
			List<LfIndustryUse> list = commonTemplateBiz.getIndustryUseList(lf,null);
			if(null != list && list.size() > 0){
				//??????????????? ??????mbgl_editTemplate.jsp??????834??????????????? 
//				result.append("??????????????????????????????????????????");
				result.append(org.apache.commons.lang.StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_pubscene_nameexist", request), "??????????????????????????????????????????"));
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
			EmpExecutionContext.error(e, "??????-??????SVT????????????????????????");
		} finally {
			if (null != pw) {
				pw.close();
			}
		}
	}

	/**
	 * ??????
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
		// ?????? + - ??????????????????????????????
		// tmName = URLEncoder.encode(tmName, "UTF-8");
		// tmName = URLDecoder.decode(tmName, "UTF-8");
		// tmName = URLDecoder.decode(tmName, "UTF-8");
		try {
			//???????????????
			String noSourcefile = org.apache.commons.lang.StringUtils.defaultIfEmpty(
					MessageUtils.extractMessage("rms", "rms_scenepreview_nosource", request), "??????????????????");
			if (filePath == null || "".equals(filePath)) {
//				pw.print("??????????????????");
				pw.write(noSourcefile);
				return;
			} else {
				// ??????????????????
				boolean flag = downLoadRmsFromOss(request,filePath.replace("fuxinPreview.html", ""),tmId);
//				boolean flag = new Mbgl_templateSvt().downLoadRmsFromOss(request,filePath, tmId,"needPreview");
				if (!flag) {
//					pw.print("??????????????????");
					pw.write(noSourcefile);
					return;
				}
				// ??????smil???????????? ??????html
				String smilFileUrl = "";
				String filePath1 = filePath.split("fuxinPreview.html")[0];
				String srcPath = (filePath1 + "src/").replace(dirUrl, "");
				smilFileUrl = filePath1 + "src/00.smil";
				// analysisRMS.createHtml(filePath+"src/", srcPath, rEntity);

				new AnalysisRMS().createPreviewHtml(filePath1 + "src/",
						srcPath, "", smilFileUrl,false);

				// ??????????????????????????????html:fuxinPreview.html
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
//					pw.print("??????????????????");
					pw.write(noSourcefile);
					return;

				}

				// ????????????
				sbuf.insert(0, "<h4 style='margin-bottom: 10px;font-size: 14px;text-align: center;'>" + tmName + "</h4>");
				if (sbuf != null) {
					mms = sbuf.toString().replace("\r\n", "&lt;BR/&gt;");

					// ??????????????????????????????????????????????????????
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
			EmpExecutionContext.error(e, "?????????????????????????????????????????????");
		} finally {
			if (null != pw) {
				pw.flush();
				pw.close();
			}

		}
	}

	/**
	 * ????????????
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
	 * ??????????????????rms??????
	 * 
	 * @param filePath
	 * @return
	 */
	public boolean downLoadRmsFromOss(HttpServletRequest request,String filePath,String tmid) {
		boolean flag = false;
		TxtFileUtil txtfileutil = new TxtFileUtil();
		String dirUrl = null;
		dirUrl = txtfileutil.getWebRoot();
		//???????????????????????????zip??????????????????
		String downFileZipUrl =filePath.replace(dirUrl, "");
		downFileZipUrl = downFileZipUrl.substring(0, downFileZipUrl.length()-1)+".zip";
		String sourcPath = filePath.replace(dirUrl, "");
		String rmsZipName =tmid+".zip";
		//??????????????????????????????????????????????????????????????????????????????
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
		 * //??????????????????????????????
	    	RmsEntity rEntity = analysisRMS.Parse(filePath+rmsName, filePath+"src/", rmsFileSize);
	    	//??????fuxin.html-????????????
	    	String srcPath = (filePath+"src/").replace(dirUrl, "");
	    	analysisRMS.createHtml(filePath+"src/", srcPath, rEntity);
		 */
		String destPath = (filePath+"fuxin.rms").replaceAll(tmid+"/fuxin.rms", "");
		
		
		//???????????????????????? zip
		String downFileZip = commBiz.downFileFromFileCenWhitZip(downFileZipUrl);
		
		if(!"success".equals(downFileZip)){//?????????????????????????????????,????????????????????? zip
			if(oosUtil.downLoadFile(sourcPath,destPath, rmsZipName)){
				//??????????????????fuxin.rms ??????????????????????????????????????????????????????rms ???????????????????????????????????????????????????fuxin.rms ?????????????????????????????????????????????
				ZipUtil.unZip((filePath+"fuxin.rms").replaceAll(tmid+"/fuxin.rms", "")+rmsZipName); 
			}
		}
		
		//?????????rms
		//??????rms??????
		AnalysisRMS aRms = new AnalysisRMS();
		//??????fuxin.rms ????????????
		File rmsFile = new File(filePath+"fuxin.rms");
		int fileByteSize =(int)rmsFile.length(); 
		//??????fuxin.html-????????????
		String srcPath = (filePath+"src/").replace(dirUrl, "");
		RmsEntity rsRmsEntity=aRms.Parse(request,filePath+"fuxin.rms", filePath+"src/", fileByteSize,null);
		//aRms.createHtml(filePath+"src/", srcPath, rsRmsEntity);
		flag = true;
		
	    return flag;
	}

	/**
	 * ????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void updateUseCount(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		LfTemplate lf = new LfTemplate();
		// ????????????ID
		String tmid = request.getParameter("tmid");
		if (!StringUtils.IsNullOrEmpty(tmid)) {
			lf.setTmid(Long.parseLong(tmid));
		}
		// ??????????????????
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
			EmpExecutionContext.error(e, "???????????????????????????????????????");
		} finally {
			pw.write(sbf.toString());
			if (null != pw) {
				pw.flush();
				pw.close();
			}
		}
	}

	/**
	 * ??????????????????Excel??????
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("static-access")
	public void downPhoneFile(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String filePath = request.getParameter("tmMsg");
			//????????????ID
			String tmId = request.getParameter("tmId");
			
			if(!StringUtils.IsNullOrEmpty(filePath) && !StringUtils.IsNullOrEmpty(tmId)){
				String dirUrl = null;
				dirUrl = new TxtFileUtil().getWebRoot();
				filePath = dirUrl + filePath.replace("fuxin.rms", "fuxin.html");
				// 1.??????tmid ??????fuxin.html
				String html = getHtml(filePath);
				// 2.??????fuxin.html
				List<List<Param>> phoneParamList = new ParamTool().convertParam(html);
				// 3.?????? ????????????EXCEL??????
				SimpleDateFormat FMT = new SimpleDateFormat("yyyyMMddHHmmss");
				String excleFileName = FMT.format(new Date()) + "_phone.xls";
				HSSFWorkbook wk = ExcelTool.crateExcel(Long.parseLong(tmId),phoneParamList, false);
				// 4.????????????EXCEL ??????
				exportExcel(excleFileName, wk, response);
			}else{
				
				
			}
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "??????????????????????????????");
		}

	}

	/**
	 * ??????fuxin.html??????
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
			// ??????????????????????????????html:fuxinPreview.html
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
			EmpExecutionContext.error(e, "??????fuxin.html????????????");
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,
							"??????fuxin.html??????BufferedReader???????????????");
				}
			}
			if (null != isr) {
				try {
					isr.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,
							"??????fuxin.html??????InputStreamReader???????????????");
				}
			}
		}
		return sbuf.toString();
	}

	// ??????
	public void exportExcel(String excleFileName, HSSFWorkbook wk,
			HttpServletResponse response) throws ServletException, IOException {
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.reset();// ???????????????
			// ???????????????????????????
			response.setHeader("Content-disposition", "attachment;filename="
					+ new String(excleFileName.getBytes("GBK"), "ISO-8859-1"));
			// ???????????????????????????Excel
			response.setContentType("application/ynd.ms-excel;charset=UTF-8");
			// ???wk??????????????????OutputStream?????????????????????
			wk.write(out);

		} catch (Exception e) {
			EmpExecutionContext.error(e, "???????????????????????????????????????");
		} finally {
			if (null != out) {
				out.flush();
				out.close();
			}

		}
	}

	/**
	 * ????????????????????????????????????LF_TEMPALTE ??????  EXTJSON ????????????
	 * @param filePath
	 * @param tmId
	 */
	public void downPhoneFileV1(String filePath,String tmId) {
		try {
			if(!StringUtils.IsNullOrEmpty(filePath) && !StringUtils.IsNullOrEmpty(tmId)){
				String dirUrl = new TxtFileUtil().getWebRoot();
				filePath = dirUrl + filePath.replace("fuxin.rms", "fuxin.html");
				// 1.??????tmid ??????fuxin.html
				String html = getHtml(filePath);
				// 2.??????fuxin.html
				List<List<Param>> phoneParamList = new ParamTool().convertParam(html);
				ExcelTool.crateExcel(Long.parseLong(tmId),phoneParamList,true);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "??????????????????????????????");
		}

	}
}
