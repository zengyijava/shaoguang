package com.montnets.emp.netnews.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.biz.TrustDataBiz;
import com.montnets.emp.netnews.common.AllUtil;
import com.montnets.emp.netnews.entity.LfWXData;
import com.montnets.emp.netnews.entity.LfWXDataBind;
import com.montnets.emp.netnews.entity.LfWXDataChos;
import com.montnets.emp.netnews.entity.LfWXDataType;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;
/**
 * @project netmsg
 * @author Vincent <vincent1219@21cn.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-12-1
 * @description 业务数据
 */

@SuppressWarnings("serial")
public class wx_trustDataSvt extends BaseServlet {
	
	private static final String PATH = "/ydwx/trustdata";
	private final BaseBiz baseBiz = new BaseBiz();
	public void find(HttpServletRequest request, HttpServletResponse response) 
	{
		String code = request.getParameter("code");
		String trustName = request.getParameter("name");
		String dataType = request.getParameter("dataType");
		String chUser = request.getParameter("chUser");
		String chDate = request.getParameter("chDate");
		String chEndDate = request.getParameter("chEndDate");
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> condiMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		//修改公司取值 may add
		try {
			HttpSession session= request.getSession(false);
			LfCorp corp=(LfCorp)session.getAttribute("loginCorp");
			String corpCode = corp.getCorpCode();
			conditionMap.put("corpCode", corpCode);
			isFirstEnter = pageSet(pageInfo, request);
			if (!isFirstEnter) {
				conditionMap.put("code", code);
				conditionMap.put("name", trustName);
				conditionMap.put("dataType", dataType);
				//创建人
				conditionMap.put("chUser", chUser);
				conditionMap.put("chDate", chDate);
				conditionMap.put("chEndDate", chEndDate);
			} 
			
			condiMap.put("corpCode", corpCode);
			orderMap.put("id", "desc");
			String skip=request.getParameter("skip");
			if(skip==null){
				skip=(String)session.getAttribute("ydwxSkip");
			}
			if("1".equals(skip)){
				conditionMap=(LinkedHashMap)request.getSession(false).getAttribute("trustDataMap");
				pageInfo=(PageInfo)request.getSession(false).getAttribute("trustDatpageInfo");
				session.setAttribute("ydwxSkip", "false");
			}
			
			List<LfWXDataType> dataTypes = baseBiz.getByCondition(LfWXDataType.class, condiMap, orderMap);
			List<DynaBean> wxDatas =  new TrustDataBiz().getData(conditionMap,pageInfo);
			request.setAttribute("wxDatas", wxDatas);
			request.setAttribute("conditionMap", conditionMap);
			request.getSession(false).setAttribute("trustDataMap", conditionMap);
			request.getSession(false).setAttribute("trustDatpageInfo", pageInfo);
			request.setAttribute("dataTypes", dataTypes);
			request.setAttribute("pageInfo", pageInfo);
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+pageInfo.getTotalRec();
				Object sysObj=request.getSession(false).getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					EmpExecutionContext.info("互动项管理", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), opContent, "GET");
				}
			}
			request.getRequestDispatcher(PATH +"/trustData.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "业务数据查询异常!");
			try {
				request.getRequestDispatcher(PATH +"/trustData.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1, "业务数据跳转异常!");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1, "业务数据IO加载异常!");
			}
		}
		
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response) {
		String dId = AllUtil.toStringValue(request.getParameter("dId"), "");
		String optype = request.getParameter("optype");
		String lgcorpcode = request.getParameter("lgcorpcode");
		String sPath = "/wx_addData.jsp";
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("dId", dId);
		try {
			if(dId != null && !"".equals(dId)&&!"null".equals(dId)){
				sPath = "/wx_editData.jsp";
				LfWXData wxData = baseBiz.getById(LfWXData.class,Long.parseLong(dId));
				
				List<LfWXDataChos> colsList = baseBiz.getByCondition(LfWXDataChos.class, conditionMap, null); 
				StringBuffer sb = new StringBuffer();
				LfWXDataChos dataChos = null;
				if(wxData.getQuesType() == 2){
					for (int i=0;i<colsList.size();i++ ) {
						dataChos = colsList.get(i);
						sb.append("<tr><td><input type='radio' disabled name='check'>")
						.append("<input type='text' style='width:160px;margin-left:10px;' maxlength='20' class='input_bd' name='displayName_")
						.append(i).append("' value='")
						.append(dataChos.getName()).append("'>")
						.append("<font style='color: red;'>&nbsp;&nbsp;*</font><a style='margin-left:10px;cursor:hand;' onclick='deleteRow(this)'>"+MessageUtils.extractMessage("ydwx","ydwx_common_btn_shanqu",request)+"</a></td></tr>");
					}
				}else if(wxData.getQuesType() == 3){
					for (int i=0;i<colsList.size();i++ ) {
						dataChos = colsList.get(i);
						sb.append("<tr><td><input type='checkbox' disabled name='check'>")
						.append("<input type='text' style='width:160px;margin-left:10px;' maxlength='20' class='input_bd' name='displayName_")
						.append(i).append("' value='")
						.append(dataChos.getName()).append("'>")
						.append("<font style='color: red;'>&nbsp;&nbsp;*</font><a style='margin-left:10px;cursor:hand;' onclick='deleteRow(this)'>"+MessageUtils.extractMessage("ydwx","ydwx_common_btn_shanqu",request)+"</a></td></tr>");
					}
				}else if(wxData.getQuesType() == 4){
					for (int i=0;i<colsList.size();i++ ) {
						dataChos = colsList.get(i);
						sb.append("<tr><td>")
						.append("<input type='text' style='width:180px;' maxlength='20' class='input_bd' name='displayName_")
						.append(i).append("' value='")
						.append(dataChos.getName()).append("'>")
						.append("<font style='color: red;'>&nbsp;&nbsp;*</font><a style='margin-left:10px;cursor:hand;' onclick='deleteRow(this)'>"+MessageUtils.extractMessage("ydwx","ydwx_common_btn_shanqu",request)+"</a></td></tr>");
					}
				}
				
				if ("copy".equalsIgnoreCase(optype)){
					wxData.setDId(null);
					wxData.setCode(generateWxCode());
				}
				
				int colNum = colsList!= null?colsList.size():0;
				request.setAttribute("wxData", wxData);
				request.setAttribute("dataChos", sb.toString());
				request.setAttribute("colNum", colNum);
				request.setAttribute("optype", optype);
			}else{
				HttpSession session = request.getSession(false);
				String wxCode=(String) session.getAttribute("wxCode");
				if(wxCode==null||"".equals(wxCode)){
					wxCode=generateWxCode();
					session.setAttribute("wxCode", wxCode);
				}
				request.setAttribute("code", wxCode);
			}
			conditionMap.clear();
			conditionMap.put("corpCode", lgcorpcode);
			
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			orderMap.put("id", "desc");
			List<LfWXDataType> dataTypes = baseBiz.getByCondition(LfWXDataType.class, conditionMap, orderMap);
			request.setAttribute("dataTypes", dataTypes);
			request.getRequestDispatcher(PATH+sPath).forward(request, response);

		} catch (Exception e) {
			EmpExecutionContext.error(e, "修改互动项异常!");
		}
	}
	
	/**
	 * DESC:网讯编辑-->新增互动项
	 * @param request 
	 * @param response
	 */
	public void toAddInteraction(HttpServletRequest request, HttpServletResponse response){
		
		String lgcorpcode = request.getParameter("lgcorpcode");
		String sPath = "/wx_addInteraction.jsp";
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			HttpSession session = request.getSession(false);
			String wxCode=(String) session.getAttribute("wxCode");
			if(wxCode==null||"".equals(wxCode)){
				wxCode=generateWxCode();
				session.setAttribute("wxCode", wxCode);
			}
			request.setAttribute("code", wxCode);
			conditionMap.clear();
			conditionMap.put("corpCode", lgcorpcode);
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			orderMap.put("id", "desc");
			List<LfWXDataType> dataTypes = baseBiz.getByCondition(LfWXDataType.class, conditionMap, orderMap);
			request.setAttribute("dataTypes", dataTypes);
			request.getRequestDispatcher(PATH+sPath).forward(request, response);

		} catch (Exception e) {
			EmpExecutionContext.error(e, "新增互动项异常!");
		}
		
		
	}
	
	/**
	 * 删除互动项
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void trustDel(HttpServletRequest request, HttpServletResponse response) {
		
		PrintWriter writer = null ;
		String trustID = AllUtil.toStringValue(request.getParameter("trustID"), "");
		boolean result = false;
		try {
			writer = response.getWriter();
			if(!"".equals(trustID)){
				
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("dId", trustID);
				List<LfWXDataBind> dataBinds = baseBiz.getByCondition(LfWXDataBind.class, conditionMap, null);
				if(dataBinds != null && dataBinds.size()>0){
					//该互动项已绑定数据，不能删除
					writer.print("dataBind");
				}else{
					
				//查询一遍
					String name="";
					String code="";
					LfWXData data=baseBiz.getById(LfWXData.class, trustID);
					if(data!=null){
						name=data.getName();
						code=data.getCode();
					}
					result = new TrustDataBiz().delete(trustID);
					if(result){
						//增加操作日志
						Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
						if(loginSysuserObj!=null){
							LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("互动项管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "删除互动项成功。[编号，名称]("+code+"，"+name+"）", "DELETE");
						}
					}else{
						//增加操作日志
						Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
						if(loginSysuserObj!=null){
							LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("互动项管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "删除互动项失败。[编号，名称]("+code+"，"+name+"）", "DELETE");
						}
					}
					request.getSession(false).setAttribute("ydwxSkip", "1");
					writer.print(result);
				}
			}else{
				writer.print("error");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除互动项异常!");
			if(writer!=null){
			writer.print("error");
			}
		}
	}
	
	/**
	 * 新增互动项
	 * @param request
	 * @param response
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) {
		LfWXData wxData = new LfWXData();
		String code = request.getParameter("code");
		String trustName = AllUtil.toStringValue(request.getParameter("name"), "");
		String dataTypeId = request.getParameter("dataTypeId");
//		String replyType = AllUtil.toStringValue(request.getParameter("replySetType"), "");
		String replyType = "1";//默认多次回复有效
		String quesType = request.getParameter("quesType");
		String quesContent = request.getParameter("quesContent");
		if(quesContent != null){
			quesContent = quesContent.trim();
		}
		int colNum = Integer.parseInt(request.getParameter("colNum"));
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);


		String corpCode = request.getParameter("lgcorpcode");
		String strRefpage = request.getParameter("refpage"); //引用来源页面
		
		wxData.setCode(code);
		wxData.setDataTypeId(Long.parseLong(dataTypeId));
		wxData.setReplySetType(Integer.parseInt(replyType));
		wxData.setQuesType(Integer.parseInt(quesType));
		wxData.setQuesContent(quesContent);
		wxData.setCreatDate(new Timestamp(System.currentTimeMillis()));
		wxData.setModifyDate(new Timestamp(System.currentTimeMillis()));
		wxData.setName(trustName);
		wxData.setColName(code);
		wxData.setCorpCode(corpCode);
		wxData.setUserId(Long.parseLong(userId));
		wxData.setColType(0);
		wxData.setColSize(0);
		wxData.setStatus(0);
		boolean result = false;
		try {
			List<LfWXDataChos> colList = null;
			if(!"1".equals(quesType)){
				//如果不是问答类型
				colList = new ArrayList<LfWXDataChos>();
				for(int i = 0 ; i < colNum ; i++){
					String displayName = AllUtil.toStringValue(request.getParameter("displayName_" + i),"");
					if(!"".equals(displayName)){
						LfWXDataChos dataChos = new LfWXDataChos();
						dataChos.setName(displayName);
						colList.add(dataChos);
					}
				}
			}
			result = new TrustDataBiz().add(wxData, colList);
			wxData.setColName("D"+wxData.getDId());
			if(result){
				//更新colName
				boolean ret=baseBiz.updateObj(wxData);
				String log="失败";
				if(ret){
					log="成功";
				}
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("互动项管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "新增互动项"+log+"。[互动项名称，编码]("+trustName+"，"+code+"）", "ADD");
				}
				
				request.getSession(false).removeAttribute("wxCode");
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "新增互动项异常!");
		}finally{
			try {
				//如果值“addhdx”则表示调用新增互动项由插入互动页面触发
				if ("addhdx".equalsIgnoreCase(strRefpage)) {
					PrintWriter writer = response.getWriter();
					writer.print(String.valueOf(result));
				}else {
					try{
						request.getSession(false).setAttribute("trustdataResult",String.valueOf(result));
					}catch (Exception e) {
						EmpExecutionContext.error(e,"从Session取出信息出现异常！");
					}
					response.sendRedirect(request.getRequestURI());
				}
				
			} catch (IOException e) {
				EmpExecutionContext.error(e, "新增互动项异常!");
			}
		}
	}
	
	/**
	 * 修改互动项
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) {
		LfWXData wxData = new LfWXData();
		
		String dId = request.getParameter("dId");
		
		String code = request.getParameter("code");
		String trustName = AllUtil.toStringValue(request.getParameter("name"), "");
		String dataTypeId = request.getParameter("dataTypeId");
//		String replyType = AllUtil.toStringValue(request.getParameter("replySetType"), "");
		String replyType = "1";//默认多次回复有效
		String quesType = request.getParameter("quesType");
		String quesContent = request.getParameter("quesContent");
		if(quesContent != null){
			quesContent = quesContent.trim();
		}
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);

		String corpCode = request.getParameter("lgcorpcode");
		wxData.setDId(Long.parseLong(dId));
		wxData.setCode(code);
		wxData.setDataTypeId(Long.parseLong(dataTypeId));
		wxData.setReplySetType(Integer.parseInt(replyType));
		wxData.setQuesType(Integer.parseInt(quesType));
		wxData.setQuesContent(quesContent);
		wxData.setCreatDate(new Timestamp(System.currentTimeMillis()));
		wxData.setModifyDate(new Timestamp(System.currentTimeMillis()));
		wxData.setName(trustName);
		//wxData.setColName(code);
		wxData.setCorpCode(corpCode);
		wxData.setUserId(Long.parseLong(userId));
		try {
			LfWXData beforeData=baseBiz.getById(LfWXData.class, Long.parseLong(dId));
			String preName="";
			String preCode="";
			if(beforeData!=null){
				preName=beforeData.getName();
				preCode=beforeData.getCode();
			}
			
			List<LfWXDataChos> colList = null;
			if(!"1".equals(quesType)){
				//如果不是问答类型
				int colNum = Integer.parseInt(request.getParameter("colNum"));
				colList = new ArrayList<LfWXDataChos>();
				for(int i = 0 ; i < colNum ; i++){
					String displayName = AllUtil.toStringValue(request.getParameter("displayName_" + i),"");
					if(!"".equals(displayName)){
						LfWXDataChos dataChos = new LfWXDataChos();
						dataChos.setDId(wxData.getDId());
						dataChos.setName(displayName);
						colList.add(dataChos);
					}
				}
			}
			boolean result = new TrustDataBiz().update(wxData, colList);
			if(result){
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;	
					EmpExecutionContext.info("互动项管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改互动项成功。[互动项名称，编码]("+preName+"，"+preCode+"）->("+trustName+"，"+code+"）", "UPDATE");
				}
			}else{
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("互动项管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改互动项失败。[互动项名称，编码]("+preName+"，"+preCode+"）->("+trustName+"，"+code+"）", "UPDATE");
				}
			}
			
			request.getSession(false).setAttribute("trustdataResult",String.valueOf(result));
			

		} catch (Exception e) {
			try{
				request.getSession(false).setAttribute("trustdataResult", "false");
			}catch (Exception e1) {
				EmpExecutionContext.error(e1,"从Session取出信息出现异常！");
			}
			EmpExecutionContext.error(e, "修改互动项异常!");
		}finally{
			try {
				//为了处理修改之后，保持进来之后的状态
				response.sendRedirect(request.getRequestURI()+"?skip=1");
			} catch (IOException e) {
				EmpExecutionContext.error(e, "修改互动项异常!");
			}
		}
	}
	/**
	 * 添加/修改互动项类别
	 * @param request
	 * @param response
	 */
	public void saveDataType(HttpServletRequest request, HttpServletResponse response) {
		String typeId = request.getParameter("typeId");
		String name = request.getParameter("name");
		String corpCode = request.getParameter("lgcorpcode");
		LfWXDataType dataType = new LfWXDataType();
		dataType.setName(name);
		PrintWriter writer = null;
		
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("name", name);
		conditionMap.put("corpCode", corpCode);
		try{
			writer = response.getWriter();
			List<LfWXDataType> dataTypes = baseBiz.getByCondition(LfWXDataType.class, conditionMap, null);
			if(dataTypes != null && dataTypes.size()>0){
				writer.print("exist");
			}else if(typeId != null && !"".equals(typeId)){
				//修改
				LfWXDataType beforeData=baseBiz.getById(LfWXDataType.class, Long.parseLong(typeId));
				String preName="";
				if(beforeData!=null){
					preName=beforeData.getName();
				}
				
				dataType.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				dataType.setId(Long.parseLong(typeId));
				boolean result = baseBiz.updateObj(dataType);
				
				if(result){
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("互动项管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改互动项类别成功。[互动项名称，编码]("+preName+"，"+typeId+"）->("+name+"，"+typeId+"）", "UPDATE");
					}
				}else{
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("互动项管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改互动项类别失败。[互动项名称，编码]("+preName+"，"+typeId+"）->("+name+"，"+typeId+"）", "UPDATE");
					}
				}
				writer.print(String.valueOf(result));
			}else{
				//添加
				dataType.setCreatDate(new Timestamp(System.currentTimeMillis()));
				dataType.setCorpCode(corpCode);
				boolean result = baseBiz.addObj(dataType);
				
				if(result){
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("互动项管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "新增互动项类别成功。[名称]("+name+"）", "ADD");
					}
				}else{
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("互动项管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "新增互动项类别失败。[名称]("+name+"）", "ADD");
					}
				}
				
				writer.print(String.valueOf(result));
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "修改互动项类别异常!");
			if(writer!=null){
				writer.print("error");
			}
		}
	}
	
	/**
	 * 删除互动项类别
	 * @param request
	 * @param response
	 */
	public void delDataType(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		PrintWriter writer = null;
		//增加校验，首先判断该类型是否被使用
		String flag="true";
		try{
			writer = response.getWriter();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("dataTypeId", id);
			List<LfWXData> dataTypes =baseBiz.getByCondition(LfWXData.class, conditionMap, null);
			if(dataTypes!=null&&dataTypes.size()>0){
				flag="forbidden";//已经关联不允许删除
				writer.print(flag);
				return;
			}
			String name="";
			if(id!=null){
				String[] ids=id.split(",");
				for(int i=0;i<ids.length;i++){
					if("".equals(ids[i])){
						continue;
					}
					LfWXDataType info = baseBiz.getById(LfWXDataType.class, ids[i]);
					if(info!=null){
						name=info.getName()+","+name;
					}
				}
			}
			
			int result = baseBiz.deleteByIds(LfWXDataType.class, id);
			if(result==0){
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("互动项管理", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "删除互动项类别失败。[类别名称]("+name+"）", "DELETE");
				}
				flag="false";
			}else{
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("互动项管理", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "删除互动项类别成功。[类别名称]("+name+"）", "DELETE");
				}
			}
			writer.print(flag);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "删除互动项类别异常!");
			if(writer!=null){
				writer.print("error");
			}
		}
	}
	
	/**
	 *  获取网讯互动类别
	 * @param request
	 * @param response
	 */
	public void getDataTypes(HttpServletRequest request, HttpServletResponse response)
	{
		
		String lgcorpcode = request.getParameter("lgcorpcode");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", lgcorpcode);
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		orderMap.put("id", "desc");
		try {
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo,request);
			pageInfo.setPageSize(5);
			List<LfWXDataType> dataTypes = baseBiz.getByCondition(LfWXDataType.class, null, conditionMap, orderMap, pageInfo);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("dataTypes", dataTypes);
			request.setAttribute("lgcorpcode", lgcorpcode);
			request.getRequestDispatcher(PATH +"/wx_dataType.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取网讯互动类别异常!");
		}
	}
	
	/**
	 * 验证编码是否存在
	 */
	public void validateCode(HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		String corpCode = request.getParameter("lgcorpcode");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("code", code);
		conditionMap.put("corpCode", corpCode);
		PrintWriter writer = null;
		try{
			writer = response.getWriter();
			List<LfWXData> wxDatas = baseBiz.getByCondition(LfWXData.class, conditionMap,null);
			if(wxDatas != null && wxDatas.size()>0){
				writer.print("exist");
			}else{
				writer.print("true");
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "验证编码是否存在异常!");
			if(writer!=null){
				writer.print("error");
			}
		}
	}
	
	/**
	 * 预览互动项内容
	 * @param request
	 * @param response
	 */
	public void previewContent(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("dId");
		StringBuffer sb = new StringBuffer("<p>");
		PrintWriter writer = null;
		try{
			writer = response.getWriter();
			LfWXData wxData = baseBiz.getById(LfWXData.class, id);
			if(wxData != null){
				//----预览时候处理显示标签字段---
				String content=wxData.getQuesContent();
				content=StringUtils.escapeString(content);
				
				sb.append(content).append("<br/>");
				if(wxData.getQuesType() == 1){
					//回答类
					sb.append("<input type='text' />");
				}else{
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("dId", id);
					List<LfWXDataChos> dataChos = baseBiz.getByCondition(LfWXDataChos.class, conditionMap, null);
					if(wxData.getQuesType() == 2){
						//单选类
						for (LfWXDataChos lfWXDataChos : dataChos) {
							sb.append("<input type='radio' name='ra' />").append(lfWXDataChos.getName()).append("<br/>");
						}
					}else if(wxData.getQuesType() == 3){
						//多选类
						for (LfWXDataChos lfWXDataChos : dataChos) {
							sb.append("<input type='checkbox' name='ra' />").append(lfWXDataChos.getName()).append("<br/>");
						}
					}else {
						//下拉框类
						sb.append("<select name='se' style='width:150px;'>");
						for (LfWXDataChos lfWXDataChos : dataChos) {
							sb.append("<option>").append(lfWXDataChos.getName()).append("</option>");
						}
						sb.append("</select>");
					}
				}
			}
			sb.append("</p>");
			writer.print(sb.toString());
		}catch (Exception e) {
			EmpExecutionContext.error(e, "预览互动项内容异常!");
			if(writer!=null){
				writer.print("");
			}
		}
		
	}
	
	/**
	 * DESC: 生成互动项编号
	 * @return 编号
	 */
	private String generateWxCode(){
		
		Long value = new TrustDataBiz().getWxCode();
		String code = value !=null?value.toString():"10000";
		
		return code;
	}
	
}
