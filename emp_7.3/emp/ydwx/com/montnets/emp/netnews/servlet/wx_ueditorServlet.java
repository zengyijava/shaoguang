package com.montnets.emp.netnews.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.i18n.util.MessageUtils;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.netnews.biz.TrustDataBiz;
import com.montnets.emp.netnews.biz.WXUeditorBiz;
import com.montnets.emp.netnews.common.AllUtil;
import com.montnets.emp.netnews.common.CompressEncodeing;
import com.montnets.emp.netnews.common.FileJsp;
import com.montnets.emp.netnews.daoImpl.Wx_getWapDaoImpl;
import com.montnets.emp.netnews.daoImpl.Wx_ueditorDaoImpl;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXData;
import com.montnets.emp.netnews.entity.LfWXDataBind;
import com.montnets.emp.netnews.entity.LfWXDataChos;
import com.montnets.emp.netnews.entity.LfWXDataType;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.netnews.entity.LfWXSORT;
import com.montnets.emp.netnews.entity.LfWXTrustCols;
import com.montnets.emp.netnews.entity.LfWXUploadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;

public class wx_ueditorServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8492302872240846377L;

	private final BaseBiz baseBiz = new BaseBiz();
	
	private final Wx_ueditorDaoImpl ueditorDao = new Wx_ueditorDaoImpl();
	private final Wx_getWapDaoImpl wx_getWapDao = new Wx_getWapDaoImpl();
	private final ResourceBundle bundle = ResourceBundle.getBundle("resourceBundle");
	private final String MovePhoneHead = bundle
			.getString("montnets.wx.MovePhoneHead"); // ??????
	private final String MovePhoneHeadURL = bundle
			.getString("montnets.wx.MovePhoneHead.url"); // ??????

	private final String UnionPhoneHead = bundle
			.getString("montnets.wx.UnionPhoneHead"); // ???????????????
	private final String UnionPhoneHeadURL = bundle
			.getString("montnets.wx.UnionPhoneHead.url"); // ???????????????

	private final String TelPhoneHead1 = bundle
			.getString("montnets.wx.TelPhoneHead1"); // ??????
	private final String TelPhoneHead1URL = bundle
			.getString("montnets.wx.TelPhoneHead1.url"); // ??????

	public void find(HttpServletRequest request, HttpServletResponse response) {
		try {
			PageInfo pageInfo =new PageInfo();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String lfcorpcode = request.getParameter("lgcorpcode");
			conditionMap.put("corpCode", lfcorpcode);
			//?????????0????????????1?????????
			conditionMap.put("TYPE", "0");
			//??????????????????
			List<LfWXSORT> wxSortList = baseBiz.getByCondition(LfWXSORT.class,
					conditionMap, null);
			
			if(wxSortList!=null && wxSortList.size()>0)
			{
				LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
				orderByMap.put("ID", "desc");
				pageInfo.setPageIndex(1);
				pageInfo.setPageSize(6);
				LfWXSORT wxsort = wxSortList.get(0);
				conditionMap.clear();
				//???????????????0????????????1?????????????????????2???????????????
				conditionMap.put("wxTYPE","0");
				conditionMap.put("CORPCODE", lfcorpcode);
				conditionMap.put("SORT", wxsort.getID().toString());
				List<LfWXBASEINFO> wxbaseinfos = baseBiz.getByConditionNoCount(LfWXBASEINFO.class, null, conditionMap, orderByMap, pageInfo);

				request.setAttribute("wxbaseinfos", wxbaseinfos);
			}
			//???????????????wxpageid?????????
			request.setAttribute("wxSortList", wxSortList);
			request.setAttribute("pageInfo", pageInfo);
			request.getSession(false).setAttribute("topMonu", "11");
			request.getSession(false).removeAttribute("netid");
			request.getSession(false).removeValue("netid");
			//response.sendRedirect(request.getContextPath()+ "/ydwx/createWX/wx_editor.jsp");
			request.getRequestDispatcher("/ydwx/createWX/wx_editor.jsp").forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"????????????????????????");
		}

	}
	
	/**
	 * ??????????????????
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getWxBaseinfoByPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String result="";
		String div="";
		try {
			String sortId = request.getParameter("sortId");
			String pageIndex=request.getParameter("pageIndex");
			String lgcorpcode = request.getParameter("lgcorpcode");
			PageInfo pageInfo=new PageInfo();
			LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			orderByMap.put("ID", "desc");
			
			String viewurl="";
			CommonBiz biz=new CommonBiz();
			String[] filePath=biz.getALiveFileServer();
			if(filePath!=null&&filePath.length>1){
				viewurl=filePath[1];
			}
			pageInfo.setPageIndex(Integer.parseInt(pageIndex));
			pageInfo.setPageSize(6);
			//??????
			conditionMap.put("wxTYPE","0");
			conditionMap.put("CORPCODE", lgcorpcode);
			conditionMap.put("SORT", sortId);
			List<LfWXBASEINFO> wxbaseinfos = baseBiz.getByCondition(LfWXBASEINFO.class, null, conditionMap, orderByMap, pageInfo);
			if(wxbaseinfos!=null && wxbaseinfos.size()>0)
			{
				for(LfWXBASEINFO wxbaseinfo : wxbaseinfos)
				{
					//<!--  ????????????????????????????????????-->
					String imgSrc="";
					if(StaticValue.getISCLUSTER() ==1){
						imgSrc=viewurl+wxbaseinfo.getIMAGE();
					}else{
						imgSrc=request.getContextPath()+wxbaseinfo.getIMAGE();
					}
					div=div+"<div style=\" width: 100px;float: left;  padding-left: 5px;\"><div >"
					+"<img onclick=\"templateShow('"+wxbaseinfo.getNETID()+"')\"" + "alt=\"" + wxbaseinfo.getNAME() + "\" title=\"" + wxbaseinfo.getNAME() + "\" style=\" width: 100px;height: 100px;cursor: pointer;\" src=\""+imgSrc+"\"/>"
					+"</div><div style=\"width: 100px;text-align: center;\">"
					+"<a onclick=\"templateShow('"+wxbaseinfo.getNETID()+"')\" style=\"cursor: pointer;\">"+ MessageUtils.extractMessage("ydwx","ydwx_common_yulan",request)+"</a>"
					+"<span style=\"width: 10px;\">&nbsp;</span>"
					+"<a onclick=\"templateApplyConfirm('"+wxbaseinfo.getNETID()+"')\" style=\"cursor: pointer;\">"+MessageUtils.extractMessage("ydwx","ydwx_common_yingyong",request)+"</a>"
					+"</div>"
					+"</div>";
				}
			}
			result=div+"@"+pageIndex+"@"+pageInfo.getTotalPage();
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"??????????????????");
			result="error";
		}
		response.getWriter().write(result);

	}

	/**
	 * ??????txt??????
	 * 
	 * @param data
	 * @param destFile
	 * @return
	 * @throws IOException
	 */
	public boolean sortAndSave(List data, String destFile) {
		BufferedWriter writer = null;
		try {
			StringBuffer sb = new StringBuffer();
			writer = new BufferedWriter(new FileWriter(destFile));
			int dSize = data.size();
			for (int i = 0; i < dSize; i = i + 5) {
				int c = i;
				if (c + 5 <= dSize) {
					c = c + 5;
				} else {
					c = dSize;
				}

				for (int j = i; j < c; j++) {
					sb.append(data.get(j));
					sb.append("\n");
				}
				writer.write(sb.toString());
				sb.delete(0, sb.length());
			}

			writer.flush();
			
			//System.out.println(" ????????????????????????????????????:" + destFile);
			return true;
		} catch (IOException e) {
			EmpExecutionContext.error(e, "??????txt???????????????");
			return false;
		}finally{
			if(writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "??????txt????????????????????????????????????");
				}
			}
		}
	}

	/**
	 * ????????? ?????????URL?????????URL ????????????URL
	 */
	public String UrlRep(String phone, String url, String repUrl) {
		String str = "";
		if (MovePhoneHead.indexOf(phone.substring(0, 3)) > -1) {
			str = url.replace(repUrl, MovePhoneHeadURL);
		} else if (UnionPhoneHead.indexOf(phone.substring(0, 3)) > -1) {
			str = url.replace(repUrl, UnionPhoneHeadURL);
		} else if (TelPhoneHead1.indexOf(phone.substring(0, 3)) > -1) {
			str = url.replace(repUrl, TelPhoneHead1URL);
		} else {
			str = url;
		}
		return str;
	}
	/**
	 *wap????????????
	 * 
	 * @param request
	 * @param response
	 */
	public void interaction(HttpServletRequest request,
			HttpServletResponse response) {
		
		try {
			String msg ="";
			if(request.getSession(false).getAttribute("pageId") == null)
			{
				EmpExecutionContext.error("????????????pageId");
				return;
			}
			String pageId = (String)request.getSession(false).getAttribute("pageId");
			
			String phone = null;
			if(request.getSession(false).getAttribute("phone") != null){
				phone = (String)request.getSession(false).getAttribute("phone");
			}
			
			String taskId = null;
			if(request.getSession(false).getAttribute("taskId") != null){
				taskId = (String)request.getSession(false).getAttribute("taskId");
			}
			
			
			//??????????????????
			LfWXBASEINFO base =  ueditorDao.getNetInfoByPageId(pageId);
			
			List<LfWXData> dataList = ueditorDao.getDataByNetId(base.getNETID().toString());
			if(dataList == null || dataList.size() == 0){
				EmpExecutionContext.error("????????????????????????");
				return;
			}
			
			Map<String,String[]> answerMap = new HashMap<String,String[]>();
			String[] answer = null;
			for(int i=0;i<dataList.size();i++)
			{
				String a = request.getParameter(dataList.get(i).getColName());
				answer = request.getParameterValues(dataList.get(i).getColName());
				answerMap.put(dataList.get(i).getColName(), answer);
			}
			boolean resultInsert=ueditorDao.insertData(base.getNETID().toString(), pageId, phone, answerMap, base.getDataTableName(), taskId);
			
			if(resultInsert){
				//??????????????????
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser sysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("????????????", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "????????????????????????????????????[?????????????????????ID????????????????????????????????????ID,pageId]???"+phone+"???"+base.getNETID().toString()+"???"+base.getDataTableName()+"???"+taskId+"???"+pageId+"???", "ADD");
				}
				
				msg="???????????????";
			}else{
				//??????????????????
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser sysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("????????????", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "????????????????????????????????????[?????????????????????ID????????????????????????????????????ID,pageId]???"+phone+"???"+base.getNETID().toString()+"???"+base.getDataTableName()+"???"+taskId+"???"+pageId+"???", "ADD");
				}
			}
			
			request.setAttribute("msg", msg);
			request.setAttribute("w", CompressEncodeing.CompressNumber(Long.valueOf(pageId),6)+"-"+CompressEncodeing.CompressNumber(Long.valueOf(taskId), 6)+CompressEncodeing.JieMPhone(phone));
			
			request.getRequestDispatcher("/ydwx/wap/wx_info.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"wap????????????");
		}

	}

	/**
	 * ????????????
	 * 
	 * @param request
	 * @param response
	 */
	public void Ueditor(HttpServletRequest request, HttpServletResponse response) {
		
		//String lguserid = request.getParameter("lguserid");
		//???????????? session????????????????????????
		String lguserid = SysuserUtil.strLguserid(request);



		String lgcorpcode = request.getParameter("lgcorpcode");
		String path=request.getContextPath();
	
		try {
			String viewurl="";
			CommonBiz biz=new CommonBiz();
			String[] filePath=biz.getALiveFileServer();
			if(filePath!=null&&filePath.length>1){
				viewurl=filePath[1];
			}
			
			//????????????????????????????????????
			String saveAndList = request.getParameter("saveAndList");
			String wx_netid = request.getParameter("wx_netid");
			// ?????????????????????????????????id????????????  ???","????????? ??????????????? LF_WX_DATA_BIND
			String dataId = request.getParameter("dataId");
			
			if (null != wx_netid && !"".equals(wx_netid)) {
				//??????????????????
				getupdateByUeditor(request, response);
			} else {
				wx_netid = "0";
				//js????????????????????????
				String str[] = request.getParameterValues("checkboxzym");
				String wx_name = request.getParameter("wx_name");
				//???????????????0????????????1?????????
				String wx_timeType = request.getParameter("wx_timeType");
				request.setAttribute("wx_timeType", wx_timeType);
				//????????????
				String wx_timeOut = request.getParameter("wx_timeOut");
				
				List<LfWXPAGE> pageList = new ArrayList<LfWXPAGE>();
				LfWXBASEINFO base = new LfWXBASEINFO();
				base.setCORPCODE(lgcorpcode);
				if (wx_timeType == null)
					wx_timeType = "0";
				base.setTIMETYPE(Integer.parseInt(wx_timeType));
				if ("0".equals(wx_timeType)) { // ???????????????????????????
					SimpleDateFormat sdf = AllUtil.getFormatDateTime();
					Calendar cal = Calendar.getInstance();
					Date date = new Date();
					cal.setTime(date);
					cal.add(cal.YEAR, 1); // ???????????????1???
					wx_timeOut = sdf.format(cal.getTime());

				}
				if (wx_timeOut == null)
				{
					wx_timeOut = AllUtil.datetoString(new Date());
				}
				// 0?????????1??????
				if(saveAndList!=null&&!"".equals(saveAndList))
				{
					base.setSTATUS(1);
				}else {
					base.setSTATUS(0);
				}
				//String wx_sms = request.getParameter("wx_sms");
				//???????????????1??????????????????????????????html?????????2???????????????????????????????????????????????????????????????jsp???????????????
				String wx_tempType = request.getParameter("wx_tempType");
				base.setTempType(Integer.valueOf(wx_tempType));
				base.setTIMEOUT(AllUtil.getTimeStamp(wx_timeOut));
				base.setCREATID(Long.parseLong(lguserid));
				base.setWxSHARE(0);//??????????????????
				base.setNAME(wx_name);
				//base.setSMS("");
				base.setCREATDATE(new Timestamp(System.currentTimeMillis()));
				//???????????????0????????????1?????????????????????2???????????????
				base.setWxTYPE(1);
				base.setSORT(0L);
				//?????????????????????
				if(base.getSTATUS()==1 || base.getSTATUS() == 4){
					SimpleDateFormat formatDateTime = new SimpleDateFormat("yyMMddHHmmss");
					String dtableName = "D"+ lguserid +formatDateTime.format(new Date());
					String ytableName = "Y"+ lguserid +formatDateTime.format(new Date());
					base.setDataTableName(dtableName);
					base.setDynTableName(ytableName);
				}
				String hasParams = request.getParameter("params");
				base.setHasParams(hasParams);
				String useParam = request.getParameter("useParam");
				base.setParams(useParam);
				base.setOperAppStatus(0);//??????????????? 0 2014-1-7??????
				request.setAttribute("useParam", useParam);
				request.setAttribute("params", hasParams);
				
				//???????????????????????????
				if(base.getSTATUS()==1 || base.getSTATUS() == 4){
					//????????????????????????
					boolean addDtableResult = this.addDataTable(base.getDataTableName(), dataId);
					if(!addDtableResult){
						//???????????????????????????
						base.setDataTableName("");
						//base.setSTATUS(0);
					}
					//?????????????????????
					boolean addYtableREsult = false;
					//??????????????????????????????
					if("2".equals(wx_tempType)){
					 addYtableREsult = new TrustDataBiz().addDynTable(base.getDynTableName(), base.getParams(),lgcorpcode,lguserid);
					}
					if(!addYtableREsult){
						//???????????????????????????
						base.setDynTableName("");
						//base.setSTATUS(0);
					}
				}
				
				//???????????????????????????????????????????????????????????????
				//  ????????????????????????pageid
				String appendForNetID=request.getParameter("appendForNetID");
				if(appendForNetID!=null&&appendForNetID.length()>0){
					
					
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					LinkedHashMap<String, String> tempMap = new LinkedHashMap<String, String>();
					conditionMap.put("NETID", appendForNetID);
					List<LfWXBASEINFO> baseInfo = baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);
					tempMap.put("netId", appendForNetID);
					List<LfWXDataBind> bindList = baseBiz.getByCondition(LfWXDataBind.class, tempMap, null);

						
					if(baseInfo!=null&&baseInfo.size()>0){
						LfWXBASEINFO before=baseInfo.get(0);
						//????????????????????????????????????????????????????????????????????????
						if(dataId==null|| dataId.length() == 0){
						if(before.getDataTableName()!=null&&!"".equals(before.getDataTableName())){
							base.setDataTableName(before.getDataTableName());
						}
						
						}
						if(base.getParams()==null|| base.getParams().length() == 0){
							if(before.getDynTableName()!=null&&!"".equals(before.getDynTableName())){
								base.setParams(before.getParams());
								base.setHasParams(before.getHasParams());
								base.setDynTableName(before.getDynTableName());
							}
						}
					}
					
					//????????????????????????
					String StrDID="";
					for(int k=0;k<bindList.size();k++){
						StrDID=bindList.get(k).getDId()+","+StrDID;
					}
						if(!"".equals(StrDID)){
							StrDID=StrDID.substring(0, StrDID.length()-1);
							dataId=StrDID;
						}
				}
				
				LfWXPAGE pagetemp = null;
				//???????????????????????????
				if (str != null && str.length > 0) {
					for (int i = 0; i < str.length; i++) {
						String tempContent = str[i];
						//????????????????????????????????? ?????????????????????
						tempContent = tempContent.replaceAll("??", "&nbsp;");
						//???????????????????????????????????????????????????
						if(saveAndList!=null&&!"".equals(saveAndList))
						{	//???????????????????????????????????????????????????????????????????????????????????????????????????
							if(StaticValue.getISCLUSTER() ==1){
								tempContent = tempContent.replaceAll(viewurl, ""); //?????????????????????
							}else{
								tempContent = tempContent.replaceAll(getServerUrl(request), ""); //?????????????????????
							}

						}
						String[] temp = tempContent.split("<#!!#>");
						LfWXPAGE page = new LfWXPAGE();
						page.setNAME(temp[0]);
						page.setPARENTID(Long.parseLong(temp[1]));
						
						// ?????? ?????????????????????
						String cont="";
						//???????????????????????????????????????????????????
						if(saveAndList!=null&&!"".equals(saveAndList))
						{
						 cont=dealWithSrc(path,temp[2]);
						}
						if(!"".equals(cont)){
							page.setCONTENT(cont);
						}else{
							page.setCONTENT(temp[2]);	
						}
						
						
						page.setLink(temp[4]);
						page.setCREATDATE(base.getCREATDATE());
						//???????????????????????????????????????????????????????????????????????????
						if (temp[1].equals("0")) {
							// ????????????-1 ???????????????0
							pagetemp = page;
						} else {
							pageList.add(page);
						}
					}
					//??????????????????????????????????????????????????????????????????????????????????????????
					wx_netid = new WXUeditorBiz().SaveUdeitor(base, pagetemp, pageList)+"";
					// ????????????????????????????????????????????????
					if(wx_netid != null && wx_netid.trim().length() > 0){
						this.addDataBind(Long.valueOf(wx_netid), dataId);
					}
					
					wx_getWapDao.getNetByJsp(wx_netid, "0",base.getSTATUS()+"",getServerUrl(request),request.getContextPath());
					
					//??????????????????
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("????????????", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "?????????????????????[????????????]("+wx_name+"???", "ADD");
					}
				}
				request.getSession(false).setAttribute("net_show_state",base.getSTATUS());
				request.getSession(false).setAttribute("callbackResult","true");
			}
			

			//??????
			this.goFind(saveAndList, wx_netid, lguserid, lgcorpcode, request, response);
			
		}catch (EMPException ex) {
			
			try {
				String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
				ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);

				String desc = info.getErrorInfo(ex.getMessage());
				if(!"".equals(desc)&&desc!=null){
					request.getSession(false).setAttribute("errormsg",desc);
					response.sendRedirect(request.getContextPath() + "/wx_ueditor.htm?method=find&lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode);
				}else{
					request.getSession(false).setAttribute("errormsg", ex.getMessage());
					response.sendRedirect(request.getContextPath() + "/wx_ueditor.htm?method=find&lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode);
				}
				
			} catch (IOException e) {
				EmpExecutionContext.error(e,"??????????????????");
			}
			
		}catch (Exception e) {
			EmpExecutionContext.error(e,"????????????");
		}
		
	}
	
	/**
	 * ???????????????????????????????????????????????????????????????
	 * ?????????????????????????????????????????????????????????????????????url ?????????http:192.168.1.127:8082(??????)
	 * 2013-09-11 18:38 modify: LIUW
	 **/
	public  String dealWithSrc(String path,String Content){
		//??????????????????????????????????????? ??????IP???????????????
		   String regExdown="src=\"(.{50})ydwx/images/downfile.jpg";
           Pattern pt=Pattern.compile(regExdown);
           Matcher ma=pt.matcher(Content);
	   		String viewurl="";
			CommonBiz biz=new CommonBiz();
			String[] filePath=biz.getALiveFileServer();
			if(filePath!=null&&filePath.length>1){
				viewurl=filePath[1];
			}
           if(ma.find()){
				if(StaticValue.getISCLUSTER() ==1){
				Content =  Content.replaceAll(regExdown,"src=\""+viewurl+"/ydwx/images/downfile.jpg");
				}else{
        	   Content =  Content.replaceAll(regExdown,"src=\"<%=wxPath%>/ydwx/images/downfile.jpg");
				}
           }
           
		//????????????????????????
		   String regEx1="<img style=\"FLOAT: none\"(.{50}) src=\"(.{50})/wx/mater/";
           Pattern p1=Pattern.compile(regEx1);
           Matcher m1=p1.matcher(Content);
           if(m1.find()){
        	   Content =  Content.replaceAll(regEx1,"<img src=\"file/wx/mater/");
  }
		return Content;
		
		
		
	}
	
	private void goFind(String saveAndList, String wx_netid, String lguserid, String lgcorpcode, HttpServletRequest request, HttpServletResponse response){
		try{
			if (saveAndList != null&&!"".equals(saveAndList)) 
			{
				// ????????????????????????
				response.sendRedirect(request.getContextPath() + "/wx_manger.htm?method=find&lgcorpcode=" + lgcorpcode + "&lguserid=" + lguserid+"&skip=true");
			}else { 
				// ??????
				response.sendRedirect(request.getContextPath() + "/wx_ueditor.htm?method=findBYid&netid=" + wx_netid + "&lgcorpcode=" + lgcorpcode + "&lguserid=" + lguserid);
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"??????????????????");
		}
		
	}
	
	/**
	 * ?????????????????????????????????
	 * @param
	 * @param dataId
	 * @return
	 */
	private boolean addDataTable(String tableName, String dataId)
	{
		try
		{
			if(dataId == null || dataId.length() == 0)
			{
				return false;
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("dId&in", dataId);
			List<LfWXData> dataList = baseBiz.getByCondition(LfWXData.class, conditionMap, null);
			List<LfWXTrustCols> colList = new ArrayList<LfWXTrustCols>();
			
			LfWXTrustCols trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("NETID");
			trustDataCol.setColType(1);
			trustDataCol.setColSize(38);
			colList.add(trustDataCol);
			
			trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("PAGEID");
			trustDataCol.setColType(1);
			trustDataCol.setColSize(38);
			colList.add(trustDataCol);
			
			trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("PHONE");
			trustDataCol.setColType(0);
			trustDataCol.setColSize(15);
			colList.add(trustDataCol);
			
			trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("ISSIMPLE");
			trustDataCol.setColType(1);
			trustDataCol.setColSize(1);
			colList.add(trustDataCol);
			
			trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("DATE_TIME");
			trustDataCol.setColType(2);
			trustDataCol.setColSize(8);
			colList.add(trustDataCol);
			
			trustDataCol = new LfWXTrustCols();
			trustDataCol.setColName("TASKID");
			trustDataCol.setColType(1);
			trustDataCol.setColSize(38);
			colList.add(trustDataCol);
			
			for(int i = 0; i < dataList.size(); i++)
			{
				trustDataCol = new LfWXTrustCols();
				trustDataCol.setColName(dataList.get(i).getColName());
				trustDataCol.setColType(0);
				trustDataCol.setColSize(256);
				colList.add(trustDataCol);
			}
			
			boolean optColTable = new TrustDataBiz().trustDataTable(tableName, colList, "2");
			return optColTable;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"?????????????????????????????????");
			return false;
		}
		
	}
	
	
	/**
	 * ???????????????????????????????????????
	 * @param netId
	 * @return
	 */
	private String getDataId(String netId)
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("netId", netId);
			List<LfWXDataBind> dataBindsList= baseBiz.getByCondition(LfWXDataBind.class, conditionMap, null);
			if(dataBindsList == null || dataBindsList.size() == 0)
			{
				return "";
			}
			String tempDId = dataBindsList.toString();
			String dataIds = tempDId.substring(1,tempDId.length()-1);
			return dataIds;
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"???????????????????????????????????????");
			return null;
		}
		
	}
	
	/**
	 * ????????????????????????????????????
	 * @param netId
	 * @param dataId
	 * @return
	 */
	private boolean addDataBind(Long netId, String dataId)
	{
		try
		{
			if(netId == null || netId ==0 || dataId == null || dataId.length() == 0)
			{
				return false;
			}
			List<LfWXDataBind> dataBindList = new ArrayList<LfWXDataBind>();
			LfWXDataBind dataBind = null;
			String[] dataIdArray = dataId.split(",");
			for(int i=0;i<dataIdArray.length;i++)
			{
				dataBind = new LfWXDataBind();
				dataBind.setNetId(netId);
				dataBind.setDId(Long.valueOf(dataIdArray[i]));
				dataBindList.add(dataBind);
				
			}
			if(dataBindList == null || dataBindList.size() == 0)
			{
				return false;
			}
			Integer result = baseBiz.addList(LfWXDataBind.class, dataBindList);
			if(result > 0)
			{
				return true;
			}else{
				return false;
			}
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"????????????????????????????????????");
			return false;
		}
		
	}

	// ????????????
	public void findBYid(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			LfSysuser sysuser=((LfSysuser) request.getSession(false).getAttribute("loginSysuser"));
			String pageI=request.getParameter("pageI");   //????????????????????????  ????????????
			String pageS=request.getParameter("pageS");	 //????????????		
			PageInfo pageInfo =new PageInfo();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String lfcorpcode = request.getParameter("lgcorpcode");
			conditionMap.put("corpCode", sysuser.getCorpCode());
			conditionMap.put("TYPE", "0");
			//??????????????????
			List<LfWXSORT> wxSortList = baseBiz.getByCondition(LfWXSORT.class,
					conditionMap, null);
			
			if(wxSortList!=null && wxSortList.size()>0)
			{
				LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
				orderByMap.put("ID", "desc");
				pageInfo.setPageIndex(1);
				pageInfo.setPageSize(6);
				LfWXSORT wxsort = wxSortList.get(0);
				conditionMap.clear();
				//??????
				conditionMap.put("wxTYPE","0");
				conditionMap.put("CORPCODE", sysuser.getCorpCode());
				conditionMap.put("SORT", wxsort.getID().toString());
				List<LfWXBASEINFO> wxbaseinfos = baseBiz.getByCondition(LfWXBASEINFO.class, null, conditionMap, orderByMap, pageInfo);
				request.setAttribute("wxbaseinfos", wxbaseinfos);
			}
			
			request.getSession(false).setAttribute("pageI", pageI);  //   ???????????????
			request.getSession(false).setAttribute("pageS", pageS);  //  ?????????SESSION ????????????????????????
			request.setAttribute("wxSortList", wxSortList);
			request.setAttribute("pageInfo", pageInfo);
			String editNetid = request.getParameter("netid");
			//System.out.println("????????????id:" + editNetid);
			request.setAttribute("netid",editNetid);
			String dataId = this.getDataId(editNetid);
			request.setAttribute("dataId",dataId);
			request.getRequestDispatcher("/ydwx/createWX/wx_editor.jsp").forward(request, response);
			//response.sendRedirect(request.getContextPath() + "/ydwx/createWX/wx_editor.jsp");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"??????????????????????????????");
		}

	}

	/**
	 * ??????ID????????????????????????
	 * 
	 * @param
	 * @return
	 */
	public void getParentUdeitor(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			//String lguserid = request.getParameter("lguserid");
			//???????????? session????????????????????????
			String lguserid = SysuserUtil.strLguserid(request);

			String netid = request.getParameter("netid");
			//System.out.println("??????ID????????????????????????--->" + netid);
			String cid = request.getParameter("creteid");
			//???????????????
			String showcount = request.getParameter("showcount");
			String wxpageid = "";//request.getParameter("wxpageid");
			//???????????????????????????
			if(showcount==null||"".equals(showcount)){
				showcount="-1";
			}
			int intshowcount=Integer.parseInt(showcount);
			LfWXBASEINFO baseInfo = null;
			if (netid != null) {
				if (cid != null && !"".equals(cid)) {
					baseInfo = ueditorDao.getParentUdeitor(netid, Integer.parseInt(cid));
				} else {
					baseInfo = ueditorDao.getParentUdeitor(netid, Integer.parseInt(lguserid));
				}
			}
			List<DynaBean> pages = ueditorDao.getPages(netid, String.valueOf(baseInfo.getCREATID()));
			StringBuffer msgParent = new StringBuffer();
			StringBuffer base = new StringBuffer();
			msgParent.append("{root:[");
			base.append("{root:[");
			if (pages != null && pages.size() > 0) {
				Map<String, String> wxrplc=new HashMap<String, String>();
				for (int i = 0; i < pages.size(); i++) {
					DynaBean page = pages.get(i);
					String pageidstr=page.get("id")!=null?page.get("id").toString():"";
					String linkstr=page.get("link")!=null?page.get("link").toString():"";
					intshowcount=intshowcount+1;
					String wxpagecode=wxpageid+"_"+intshowcount;
					wxrplc.put("w="+pageidstr, "w=#link#01" + wxpagecode + "#link#");
					wxrplc.put("&#39;"+linkstr+"&#39;", "&#39;01" + wxpagecode + "&#39;");
				}
				
				for (int i = 0; i < pages.size(); i++) {
					DynaBean page = pages.get(i);
					String content=FileJsp.getContent(page.get("id")+"");
					
					for(String key:wxrplc.keySet()){
						content=content.replaceAll(key, wxrplc.get(key));
					}
					
					if ((page.get("parentid").toString()).equals("0")) {
						
						base.append("{netid:'");
						base.append(baseInfo.getNETID());

						base.append("',wx_name:'");
						base.append(baseInfo.getNAME());

						base.append("',wx_sms:'");
						base.append(baseInfo.getSMS());

						base.append("',timetype:'");
						base.append(baseInfo.getTIMETYPE());
						
						base.append("',temptype:'");
						base.append(baseInfo.getTempType());
						
						base.append("',useparams:'");
						base.append(baseInfo.getParams());
						
						base.append("',hasparams:'");
						base.append(baseInfo.getHasParams());

						base.append("',timeout:'");
						if (baseInfo.getTIMEOUT() != null)
						{
							base.append(baseInfo.getTIMEOUT().toString().substring(0, 19));
						}  
						else
						{
							base.append(" ");
						}

						base.append("',share:'");
						base.append(baseInfo.getWxSHARE());

						base.append("',name:'");
						base.append((String)page.get("name"));

						base.append("',page:'");
						base.append((String)page.get("name") + "<#!!#>"
								+ page.get("parentid") + "<#!!#>"
								+ (content).replaceAll(">???</td", "></td").replaceAll(">\\?</td>", "></td>").replaceAll("'", "&#39;") + "<#!!#>"
								+ page.get("id"));

						base.append("'}]}");
					} else {
						msgParent.append("{name:'");
						msgParent.append(page.get("name"));

						msgParent.append("',page:'");
						msgParent.append(page.get("name") + "<#!!#>"
								+ page.get("parentid") + "<#!!#>"
								+ (content).replaceAll(">???</td", "></td").replaceAll(">\\?</td>", "></td>").replaceAll("'", "&#39;") + "<#!!#>"
								+ page.get("id") + "<#!!#>").append(page.get("link"));
						msgParent.append("'},");
					}
				}
			}
			String msg = msgParent.toString().substring(0,msgParent.length()-1)+"]}";
			//String msg = msgParent+ "]}";
			String baseContent=base.toString();
			//???????????????????????????????????????????????????????????????????????????????????????
			baseContent=addWithSrc( request,baseContent);
			if(!msg.trim().equals("{root:]}")){
				response.getWriter().write("{success:true,msg:"+msg+",base:"+baseContent+"}");
			}else{
				response.getWriter().write("{success:true,base:"+baseContent+"}");
			}
	
		} catch (Exception e) {
			EmpExecutionContext.error(e,"??????ID????????????????????????");
		}
	}
	

	
	
	/**
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????
	 * 
	 * 2013-09-11 18:38 modify: LIUW
	 **/
	public  String addWithSrc(HttpServletRequest request,String Content){

		String ipport=getServerUrl(request);
		//??????????????????????????????????????? ??????IP???????????????
		   String regExdown="src=\"(.{50})ydwx/images/downfile.jpg";
           Pattern pt=Pattern.compile(regExdown);
           Matcher ma=pt.matcher(Content);
           if(ma.find()){
        	   Content =  Content.replaceAll(regExdown,"src=\""+ipport+"ydwx/images/downfile.jpg");
           }
           //????????????????????????????????????????????????????????????  ---????????????????????????????????????????????????
           if(StaticValue.getISCLUSTER() ==1){
          		String viewurl="";
        		CommonBiz biz=new CommonBiz();
        		String[] filePath=biz.getALiveFileServer();
        		if(filePath!=null&&filePath.length>1){
        			viewurl=filePath[1];
        		}
        	   ipport=viewurl;
           }
		//????????????????????????
		   String regEx1="src=\"(.{50})/wx/mater/";
           Pattern p1=Pattern.compile(regEx1);
           Matcher m1=p1.matcher(Content);
           if(m1.find()){
        	   Content =  Content.replaceAll(regEx1,"src=\""+ipport+"file/wx/mater/");
  }
		return Content;
		
		
		
	}

	/**
	 * ????????????
	 * 
	 * @param request
	 * @param response
	 */

	public void getupdateByUeditor(HttpServletRequest request,
			HttpServletResponse response) {
		//String lguserid = request.getParameter("lguserid");
		//???????????? session????????????????????????
		String lguserid = SysuserUtil.strLguserid(request);


		String lgcorpcode = request.getParameter("lgcorpcode");
		try {
			String path=request.getContextPath();
			String str[] = request.getParameterValues("checkboxzym");
			String wx_name = request.getParameter("wx_name");
			String wx_timeType = request.getParameter("wx_timeType");
			String wx_timeOut = request.getParameter("wx_timeOut");
			String wx_sms = request.getParameter("wx_sms");
			String dataId = request.getParameter("dataId");//?????????ID
			List<LfWXPAGE> pageList = new ArrayList<LfWXPAGE>();
			LfWXBASEINFO base = new LfWXBASEINFO();
			String wx_netid = request.getParameter("wx_netid");
			Long newtid = Long.parseLong(wx_netid);
			base.setID(newtid);
			base.setNETID(newtid);
			base.setTIMETYPE(Integer.parseInt(wx_timeType));
			if ("0".equals(wx_timeType)) { // ???????????????????????????
				SimpleDateFormat sdf = AllUtil.getFormatDateTime();
				Calendar cal = Calendar.getInstance();
				Date date = new Date();
				cal.setTime(date);
				cal.add(cal.YEAR, 1); // ???????????????1???
				wx_timeOut = sdf.format(cal.getTime());
			}
			Long coorid = Long.parseLong(lguserid);
			base.setWxTYPE(1);
			base.setCREATID(coorid);
			base.setCORPCODE(lgcorpcode);
			base.setTIMEOUT(AllUtil.getTimeStamp(wx_timeOut));
			base.setMODIFYID(coorid);
			base.setNAME(wx_name);
			base.setSMS(wx_sms);
			String saveAndList = request.getParameter("saveAndList");
			if(saveAndList!=null&&!"".equals(saveAndList))
			{
				base.setCREATDATE(new Timestamp(System.currentTimeMillis()));
				base.setSTATUS(1);
			}else {
				base.setSTATUS(0);
			}
			if(base.getSTATUS()==1 || base.getSTATUS() == 4){
				SimpleDateFormat formatDateTime = new SimpleDateFormat("yyMMddHHmmss");
				String dtableName = "D"+ lguserid +formatDateTime.format(new Date());
				String ytableName = "Y"+ lguserid +formatDateTime.format(new Date());
				base.setDataTableName(dtableName);
				base.setDynTableName(ytableName);
			}
			String wx_tempType = request.getParameter("wx_tempType");
			request.setAttribute("wx_tempType", wx_tempType);
			base.setTempType(Integer.valueOf(wx_tempType));
			String hasParams = request.getParameter("params");
			base.setHasParams(hasParams);
			String useParam = request.getParameter("useParam");
			base.setParams(useParam);
			request.setAttribute("useParam", useParam);
			request.setAttribute("params", hasParams);
			

			base.setWxSHARE(0);
			// ?????????
			
			// ?????? ?????????????????????

			String rmovePage = "";
			LfWXPAGE paget = null;
			if (str != null && str.length > 0) {
				for (int i = 0; i < str.length; i++) {
					LfWXPAGE page = new LfWXPAGE();
					str[i] = str[i].replaceAll("??", "&nbsp;");
					page.setNAME(str[i].split("<#!!#>")[0]);

					Long parentid = Long.parseLong(str[i].split("<#!!#>")[1]);

					page.setPARENTID(parentid);
					String cont="";
					//???????????????????????????????????????????????????
					if(saveAndList!=null&&!"".equals(saveAndList))
					{
						str[i] = str[i].replaceAll(getServerUrl(request), ""); //?????????????????????
					}
					//?????????????????????
					if(saveAndList!=null&&!"".equals(saveAndList))
					{
					 cont=dealWithSrc(path,str[i].split("<#!!#>")[2]);
					}
					
					if(!"".equals(cont)){
						page.setCONTENT(cont);
					}else{
						page.setCONTENT(str[i].split("<#!!#>")[2]);	
					}
//					page.setCONTENT(str[i].split("<#!!#>")[2]);
					
					page.setID(Long.parseLong(str[i].split("<#!!#>")[3]));
					page.setLink(str[i].split("<#!!#>")[4]);
					rmovePage = rmovePage + Integer.parseInt(str[i].split("<#!!#>")[3]) + ",";
					if (parentid.equals(0L))
					{
						paget = page;
					}
					pageList.add(page);
				}

				String netid = request.getParameter("wx_netid");
				// ??????PAGE??????
				String PageID = "";
				List<DynaBean> pages = ueditorDao.getPages(netid, lguserid); // ????????????????????????????????????
				if (pages != null && pages.size() > 0) {
					DynaBean bean = null;
					for (int j = 0; j < pages.size(); j++) {
						bean = pages.get(j);
						Long id =  Long.parseLong(bean.get("id").toString());
						if (rmovePage.indexOf(id + ",") < 0) {
							PageID = PageID + id + ",";
						}
					}
				}
				if (!"".equals(PageID)) {

					PageID = PageID.substring(0, PageID.length() - 1);
					baseBiz.deleteByIds(LfWXPAGE.class, PageID); // ??????
				}

				//???????????????????????????
				if(base.getSTATUS()==1 || base.getSTATUS() == 4){
					//????????????????????????
					boolean addDtableResult = this.addDataTable(base.getDataTableName(), dataId);
					if(!addDtableResult){
						//???????????????????????????
						base.setDataTableName("");
						//base.setSTATUS(0);
					}
					//?????????????????????
					boolean addYtableREsult = new TrustDataBiz().addDynTable(base.getDynTableName(), base.getParams());
					if(!addYtableREsult){
						//???????????????????????????
						base.setDynTableName("");
						//base.setSTATUS(0);
					}
				}
				//???????????????????????????????????????????????????????????????
				//  ????????????????????????pageid
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("NETID", wx_netid);
					List<LfWXBASEINFO> baseInfo = baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);
					if(baseInfo!=null&&baseInfo.size()>0){
						LfWXBASEINFO before=baseInfo.get(0);
						//????????????????????????????????????????????????????????????????????????
						if(dataId==null|| dataId.length() == 0){
							if(before.getDataTableName()!=null&&!"".equals(before.getDataTableName())){
								base.setDataTableName(before.getDataTableName());
							}
						}
						if(base.getParams()==null|| base.getParams().length() == 0){
							if(before.getDynTableName()!=null&&!"".equals(before.getDynTableName())){
								base.setParams(before.getParams());
								base.setHasParams(before.getHasParams());
								base.setDynTableName(before.getDynTableName());
							}
						}
					}

				
				
				WXUeditorBiz objWxUeditorBiz = new WXUeditorBiz();
				//?????????????????????
				if(wx_netid != null && wx_netid.trim().length() > 0 ){
					
					objWxUeditorBiz.updateDataBind(Long.valueOf(wx_netid), dataId);
				}
				
				int i = objWxUeditorBiz.updateUeditor(base, paget, pageList);
				
				//ueditorDao.getupdateByUeditor(base, paget, pageList);
				
				//String dataId = request.getParameter("dataId");
				//getDataId(netid);
				
				//????????????
				wx_getWapDao.getNetByJsp(wx_netid, "0",base.getSTATUS()+"",getServerUrl(request),request.getContextPath());
				
				if (i > 0){
					//??????????????????
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("????????????", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "?????????????????????[?????????????????????NETID]("+wx_name+"???"+wx_netid+"???", "UPDATE");
					}
					request.setAttribute("panduan", "true");
				}else{
					//??????????????????
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("????????????", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "?????????????????????[?????????????????????NETID]("+wx_name+"???"+wx_netid+"???", "UPDATE");
					}
					request.setAttribute("panduan", "false");
				}
				//??????????????????????????????
				request.getSession(false).setAttribute("callbackResult","true");
			}

		} catch (Exception ex) {
			try {
				String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
				ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
				String desc = info.getErrorInfo(ex.getMessage());
				if(!"".equals(desc)&&desc!=null){
					request.getSession(false).setAttribute("errormsg",desc);
					response.sendRedirect(request.getContextPath() + "/wx_ueditor.htm?method=find&lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode);
				}else{
					request.getSession(false).setAttribute("errormsg", ex.getMessage());
					response.sendRedirect(request.getContextPath() + "/wx_ueditor.htm?method=find&lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode);
				}
				
			} catch (IOException e) {
				EmpExecutionContext.error(e,"???????????????????????????");
			}
			EmpExecutionContext.error(ex,"??????????????????");
		}
	}

	/**
	 * ???????????? ????????????
	 * 
	 * @param request
	 * @param response
	 */

	public void getVideoUeditor(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			//String lguserid = request.getParameter("lguserid");
			//???????????? session????????????????????????
			String lguserid = SysuserUtil.strLguserid(request);


			String filetype = request.getParameter("filetype");
			String lgcorpcode = request.getParameter("lgcorpcode");
			List<LfWXSORT> sorts=null;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("FILETYPE", filetype);
			List<LfWXSORT> list = baseBiz.getByCondition(LfWXSORT.class, conditionMap, null);
			if(list.size()>0){
				LfWXSORT wxs=list.get(0);
				LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
				//????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
				condition.put("CREATID&in", 0+","+lguserid);
				condition.put("corpCode&in",0+","+lgcorpcode);
				condition.put("sort_path&like",wxs.getSort_path());
				 sorts = baseBiz.getByCondition(LfWXSORT.class, condition, null);
			}
			
			StringBuffer sortids = new StringBuffer();
			if(sorts!=null && sorts.size()>0)
			{
				LfWXSORT sort = null;
				for(int i = 0;i<sorts.size();i++){
					sort = sorts.get(i);
/*					// ??? DB2???????????????????????????????????????
					if("4".equals(type)){
						sortids.append("'"+sort.getID()+"'");	
					}else{
						sortids.append(sort.getID());
					}*/
					//SORTID??????????????????????????????,????????????LONG?????????STRING?????????DB2????????????????????????????????????????????????
					sortids.append(sort.getID());
					if(i<sorts.size()-1)
					{
						sortids.append(",");
					}
				}
			}
			
			List<LfWXUploadFile> li = null;
			if(sortids!=null && sortids.length()>0)
			{
				conditionMap.clear();
				conditionMap.put("SORTID&in", sortids.toString());
				conditionMap.put("CORP_CODE", lgcorpcode);
				li = baseBiz.getByCondition(LfWXUploadFile.class, conditionMap, null);
			}
			
			StringBuffer msgParent = new StringBuffer();
			msgParent.append("{root:[");

			if (li != null && li.size() > 0) {
				LfWXUploadFile upLoad = null;
				for (int i = 0; i < li.size(); i++) {
					upLoad =li.get(i);
					msgParent.append("{file_id:'");
					msgParent.append(upLoad.getID());

					msgParent.append("',file_name:'");
					msgParent.append(upLoad.getFILENAME());

					msgParent.append("',file_url:'");
					msgParent.append(upLoad.getWEBURL());

					msgParent.append("',type:'0'}");
					if (i != li.size() - 1) {
						msgParent.append(",");
					}
				}
				msgParent.append("]}");
				response.getWriter().write("{success:true,msg:" + msgParent.toString() + "}");
			}
			return;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"??????????????????");
		}
	}

	/**
	 * ????????????
	 * 
	 * @param request
	 * @param response
	 */

	public void getImgUeditor(HttpServletRequest request,
			HttpServletResponse response) {
		PageInfo pageInfo = new PageInfo();
		try {
			String pageIndex = request.getParameter("pageIndex");
			HttpSession session= request.getSession(false);
			LfCorp corp=(LfCorp)session.getAttribute("loginCorp");
			String lgcorpcode = corp.getCorpCode();
			pageSet(pageInfo,request);
			pageInfo.setPageIndex(Integer.parseInt(pageIndex));
			pageInfo.setPageSize(8);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("TYPE", "1");
			conditionMap.put("FILETYPE", "3");
			conditionMap.put("corpCode&in",0+","+lgcorpcode);
			List<LfWXSORT> sorts = baseBiz.getByCondition(LfWXSORT.class, conditionMap, null);
			StringBuffer buffer=null;
			if(sorts!=null && sorts.size()>0)
			{
				buffer = new StringBuffer();
				LfWXSORT sort = null;
				for (int i = 0; i <sorts.size(); i++) 
				{
					sort = sorts.get(i);
					
/*					// ??? DB2???????????????????????????????????????
					if("4".equals(type)){
						buffer.append("'"+sort.getID()+"'");	
					}else{
						buffer.append(sort.getID());
					}*/
					//SORTID??????????????????????????????,????????????LONG?????????STRING?????????DB2????????????????????????????????????????????????
					buffer.append(sort.getID());
					if(i<sorts.size()-1)
					{
						buffer.append(",");
					}
				}
			}
			
			List<LfWXUploadFile> files =null;
			if(buffer!=null && buffer.length()>0)
			{
				conditionMap.clear();
				conditionMap.put("SORTID&in", buffer.toString());
				conditionMap.put("CORP_CODE", lgcorpcode);
				//conditionMap.put("CREATID",lguserid);
				
				LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
				orderbyMap.put("CREATDATE", "desc");
				files = baseBiz.getByCondition(LfWXUploadFile.class, null, conditionMap, orderbyMap, pageInfo);
			}
			StringBuffer msgParent = new StringBuffer();
			msgParent.append("{root:[");

			if (files != null && files.size() > 0) {
				LfWXUploadFile file = null;
				for (int i = 0; i < files.size(); i++) {
					file = files.get(i);
					msgParent.append("{mg_id:'");
					msgParent.append(file.getID());

					msgParent.append("',mg_name:'");
					msgParent.append(file.getFILENAME());

					msgParent.append("',mg_url:'");
					msgParent.append(file.getWEBURL());

					msgParent.append("',type:'0'}");
					if (i != files.size() - 1) {
						msgParent.append(",");
					}
				}
				msgParent.append("]}");
				response.getWriter().write("{success:true,pageno:" + pageInfo.getPageIndex()
						+ ",totalpages:" + pageInfo.getTotalPage()+ ",msg:"
						+ msgParent.toString() + "}");
			} else {
				response.getWriter().write("{success:false}");
			}
			return;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"??????????????????????????????");
		}
	}

	public void getTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		String output = new String();
		try {
			int sort = Integer.parseInt(request.getParameter("section"));
			int id = Integer.parseInt(request.getParameter("id"));
			output = ueditorDao.getTemplate(sort, id);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"??????????????????");
		}
		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter pw = response.getWriter();
			pw.println(output);
		} catch (IOException e1) {
			EmpExecutionContext.error(e1,"????????????????????????");
		}
	}

	// --------------------------------------------------------------

	/**
	 * ?????????????????? id name tablename
	 * 
	 */
	public void getActiviTables(HttpServletRequest request, HttpServletResponse response) {
		String lgcorpcode = request.getParameter("lgcorpcode");

		List<LfWXDataType> list = new ArrayList<LfWXDataType>();
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			orderMap.put("id", "desc");
			
			list = baseBiz.getByCondition(LfWXDataType.class, conditionMap, orderMap);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"?????????????????? ");
		}
		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter pw = response.getWriter();
			pw.print(list);
		} catch (IOException e1) {
			EmpExecutionContext.error(e1,"????????????????????????");
		}
	}

	/**
	 * ???????????? id name tablename
	 * 
	 */
	public void getActiviCols(HttpServletRequest request,
			HttpServletResponse response) {
		//??????id
		String dataTypeId = request.getParameter("dataTypeId");
		List<LfWXData> list = new ArrayList<LfWXData>();
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			conditionMap.put("dataTypeId", dataTypeId);
			
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			orderMap.put("dId", "asc");
			
			list = baseBiz.getByCondition(LfWXData.class, conditionMap, orderMap);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"????????????");
		}
		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter pw = response.getWriter();
			//??????????????????????????????????????????????????????????????????????????????
			for(int i=0;i<list.size();i++){
				LfWXData data=list.get(i);
				String content=data.getQuesContent();
				content=StringUtils.escapeString(content);
				if(content.indexOf("\r\n")>-1){
					content=content.replace("\r\n", "<br>");
				}
				data.setQuesContent(content);
				
			}
			pw.print(list);
		} catch (IOException e1) {
			EmpExecutionContext.error(e1,"????????????????????????");
		}
	}
	/**
	 * ??????????????????
	 * 
	 */
	public void getDataHtml(HttpServletRequest request,
			HttpServletResponse response) {
		//??????id
		String dId = request.getParameter("dId");
		List<LfWXDataChos> list = new ArrayList<LfWXDataChos>();
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			conditionMap.put("dId&in", dId);
			list = baseBiz.getByCondition(LfWXDataChos.class, conditionMap, null);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"??????Lf_WX_Data_Chos?????????");
		}
		response.setCharacterEncoding("utf-8");
		try {
			PrintWriter pw = response.getWriter();
			pw.print(list);
		} catch (IOException e1) {
			EmpExecutionContext.error(e1,"??????????????????");
		}
	}

	public void setImgTypeSession(HttpServletRequest request,
			HttpServletResponse response) {
		String _imgType = request.getParameter("_imgType");
		String del = request.getParameter("del");// ????????? 1??????_imgType 0??????_imgType
		try {
			if ("0".equals(del)) {
				if (_imgType != null) {
					request.getSession(false).setAttribute("_imgType", _imgType);
					response.getWriter().write("{success:true}");
				} else {
					response.getWriter().write("{success:false}");
				}
			} else {
				request.getSession(false).removeAttribute("_imgType");
				response.getWriter().write("{success:true}");

			}
		} catch (IOException e) {
			EmpExecutionContext.error(e, "?????????????????????");
		}
	}


	/**
	 * DESC: ???????????????????????????
	 * @param request
	 * @return ????????????
	 */
	public String getServerUrl(HttpServletRequest request){
		
		//String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName();
		String port = String.valueOf(request.getServerPort());
		if(null != port && port.length() > 0){
		  basePath = basePath + ":" + request.getServerPort()+request.getContextPath()+"/";
		}
		/*if(null != path && path.length() > 0){
		  basePath = basePath + path;
		}*/
		return basePath;
	}
	
	/**
	 * ????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public void isExist(HttpServletRequest request,
			HttpServletResponse response) {
		String flag="false";
		String src = request.getParameter("url");
		String temp=request.getParameter("url");
		src=new TxtFileUtil().getWebRoot()+src;
		File file=new File(src);
		if(!file.exists()){
			flag="true";
		}
		//????????????????????????
		if("true".equals(flag)&& StaticValue.getISCLUSTER() ==1){
			CommonBiz com=new CommonBiz();
			long before=System.currentTimeMillis();
			String value=com.downloadFileFromFileCenter(temp);
			long after=System.currentTimeMillis();
			EmpExecutionContext.info("?????????????????????"+value+",???????????????"+(after-before)+"ms");
			if("error".equals(value)){
				flag="true";
			}else{
				flag="false";
			}
		}
		try
		{
			response.getWriter().write(flag);
		}
		catch (IOException e)
		{
			EmpExecutionContext.error(e,"????????????????????????");
		}
	}
}
