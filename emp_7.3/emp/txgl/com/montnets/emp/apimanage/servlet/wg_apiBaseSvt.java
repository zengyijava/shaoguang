package com.montnets.emp.apimanage.servlet;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.montnets.emp.apimanage.biz.wg_apiBaseBiz;
import com.montnets.emp.apimanage.util.FormatUtil;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.servmodule.txgl.entity.GwBaseprotocol;
import com.montnets.emp.servmodule.txgl.entity.GwMultiEnterp;
import com.montnets.emp.servmodule.txgl.entity.GwProtomtch;
import com.montnets.emp.servmodule.txgl.entity.GwPushprotomtch;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class wg_apiBaseSvt extends BaseServlet{
	
	private final BaseBiz baseBiz = new BaseBiz();
	private final String empRoot = "txgl";
	private final String basePath = "/apimanage";
	final wg_apiBaseBiz biz=new wg_apiBaseBiz();
	final FormatUtil util =new FormatUtil();
	public void find(HttpServletRequest request, HttpServletResponse response) 
	{

		PageInfo pageInfo = new PageInfo();
		LfSysuser curUser = null;
		boolean isFirstEnter;
		try {
			 curUser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			isFirstEnter = pageSet(pageInfo, request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			if(!isFirstEnter)
			{
				//????????????
				String funtype =request.getParameter("funtype");
				//????????????
				String reqfmt=request.getParameter("reqfmt");
				String respfmt=request.getParameter("respfmt");
				String startdate=request.getParameter("startdate");
				String enddate=request.getParameter("enddate");
				//????????????
				String etccode=request.getParameter("etccode");
				//????????????
				String etcname=request.getParameter("etcname");
				
				if(funtype!=null && !"".equals(funtype.trim()))
				{
					conditionMap.put("funtype", funtype.trim());

				}
				if(reqfmt!=null && !"".equals(reqfmt))
				{
					conditionMap.put("reqfmt", reqfmt);
				}
				
				if(respfmt!=null && !"".equals(respfmt))
				{
					conditionMap.put("respfmt", respfmt);
				}
				if(startdate!=null && !"".equals(startdate))
				{
					conditionMap.put("startdate", startdate);
				}
				if(enddate!=null && !"".equals(enddate))
				{
					conditionMap.put("enddate", enddate);
				}
				
				if(etccode!=null && !"".equals(etccode.trim()))
				{
					conditionMap.put("etccode", etccode.trim());

				}
				if(etcname!=null && !"".equals(etcname.trim()))
				{
					conditionMap.put("etcname", etcname.trim());

				}
				
			}
			List<DynaBean> enterpList=biz.getApiList(conditionMap, pageInfo);
			request.setAttribute("enterpList",enterpList);
			request.setAttribute("pageInfo",pageInfo);	
			request.setAttribute("conditionMap",conditionMap);

			int corptype = StaticValue.getCORPTYPE();
			if(corptype == 0)
			{
				request.getRequestDispatcher(
						empRoot + basePath + "/wg_apiBase.jsp").forward(
						request, response);
			}else{
				request.getRequestDispatcher(
						empRoot + basePath + "/wg_apiBaseMulit.jsp").forward(
						request, response);
			}

			
		} catch (Exception e) {
			String str="";
			if(curUser!=null){
				str="(corpCode???"+curUser.getCorpCode()+"???opt???"+curUser.getUserId()+"["+curUser.getUserName()+"])";
			}
			EmpExecutionContext.error(e, "??????API???????????????"+str+"??????????????????!");
		}
	}
	/**
	 * ????????????
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		//?????????single_send,batch_send,multi_send???
		String methodNames  =request.getParameter("methodNames");
		String clientIntName=request.getParameter("clientIntName");
		String InterfaceName=request.getParameter("InterfaceName");
		//??????????????????
		String req_type=request.getParameter("req_type");
		//??????????????????
		String resp_type=request.getParameter("resp_type");
		//??????ID
		String ecid=request.getParameter("ecid");
		
		String funtype=request.getParameter("funtype");
		
		LinkedHashMap<String, String> baseCondition = new LinkedHashMap<String,String>();
		baseCondition.put("ecid", ecid);
		baseCondition.put("funtype", funtype);
		//??????????????????????????????????????????
		
		

		
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("ecid", ecid);
		conditionMap.put("funtype", funtype);
		conditionMap.put("funname", methodNames);
		
		//???????????????????????????????????????????????????
		List<GwBaseprotocol> proList=biz.isExist(conditionMap);
		if(proList!=null&&proList.size()>0){
			response.getWriter().print("isExist");
			return;
		}
		List<GwBaseprotocol> bslist=new ArrayList<GwBaseprotocol>();
		for(int i=1;i<=5;i++){
			GwBaseprotocol reqbp=new GwBaseprotocol();
			//??????ID
			if(ecid!=null){
				reqbp.setEcid(Integer.parseInt(ecid));
			}
			//?????????
			if(methodNames!=null){
				reqbp.setFunname(methodNames);
			}
			//???????????????
			if(InterfaceName!=null){
				reqbp.setCfunname(InterfaceName);
			}
			//????????????1??????  2??????????????? 3??????????????? 4?????????????????????????????? 5??????????????????
			reqbp.setCmdtype(i);
			if(i==1){
				if(req_type!=null){
					reqbp.setRettype(Integer.parseInt(req_type));
				}
			}else{
				if(resp_type!=null){
					reqbp.setRettype(Integer.parseInt(resp_type));
				}
			}
			//???????????????
			reqbp.setStatus(0);
			reqbp.setCustintfname(clientIntName);
			reqbp.setCreatetm(new Timestamp(System.currentTimeMillis()));
			reqbp.setModiytm(new Timestamp(System.currentTimeMillis()));
			reqbp.setFuntype(funtype);
			reqbp.setFmtmsg(" ");
			bslist.add(reqbp);
		}
		int result=biz.add(bslist);
		
		//???????????????(??????)
		List<GwBaseprotocol> basepro=biz.getfunNum(ecid,funtype,"1");
		int number=0;
		String restotal="";
		if(basepro!=null&&basepro.size()>0){
			number=basepro.size();
			for(int k=0;k<basepro.size();k++){
				restotal=restotal+","+basepro.get(k).getRettype();
			}
		}
		LinkedHashMap<String, String> enternMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> enternContMap = new LinkedHashMap<String, String>();
		enternContMap.put("funtype", funtype);
		enternMap.put("bookcnt", number+"");
		//xml--1--json--2--urlencode--4-
		if(restotal.indexOf("1")>-1&&restotal.indexOf("2")>-1&&restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "7");
		}else if(restotal.indexOf("2")>-1&&restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "6");
		}else if(restotal.indexOf("1")>-1&&restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "5");
		}else if(restotal.indexOf("1")>-1&&restotal.indexOf("2")>-1){
			enternMap.put("reqfmt", "3");
		}else if(restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "4");
		}else if(restotal.indexOf("2")>-1){
			enternMap.put("reqfmt", "2");
		}else if(restotal.indexOf("1")>-1){
			enternMap.put("reqfmt", "1");
		}else {
			enternMap.put("reqfmt", "0");
		}
		List<GwBaseprotocol> respone=biz.getfunNum(ecid,funtype,"2");
		String resptotal="";
		if(respone!=null&&respone.size()>0){
			for(int k=0;k<respone.size();k++){
				resptotal=resptotal+","+respone.get(k).getRettype();
			}
		}
		if(resptotal.indexOf("1")>-1&&resptotal.indexOf("2")>-1&&resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "7");
		}else if(resptotal.indexOf("2")>-1&&resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "6");
		}else if(resptotal.indexOf("1")>-1&&resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "5");
		}else if(resptotal.indexOf("1")>-1&&resptotal.indexOf("2")>-1){
			enternMap.put("respfmt", "3");
		}else if(resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "4");
		}else if(resptotal.indexOf("2")>-1){
			enternMap.put("respfmt", "2");
		}else if(resptotal.indexOf("1")>-1){
			enternMap.put("respfmt", "1");
		}else{
			enternMap.put("respfmt", "0");
		}
		
		biz.setStatus(enternMap,enternContMap);
		String res="false";
		if(result>0){
			res="true";
		}
		response.getWriter().print(res);
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void setStatus(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String ecid=request.getParameter("ecid");
		String enStatus=request.getParameter("enStatus");
		LinkedHashMap<String, String> valuemap = new LinkedHashMap<String,String>();
		LinkedHashMap<String, String> conmap = new LinkedHashMap<String,String>();
		conmap.put("ecid", ecid);
		if(enStatus!=null){
			if("0".equals(enStatus)){
				valuemap.put("status", "1");
			}else if("1".equals(enStatus)){
				valuemap.put("status", "0");
			}
		}
		boolean result=biz.setStatus(valuemap,conmap);
		response.getWriter().print(result);
	}
	
	/**
	 * ?????????????????? 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void addFuntype(HttpServletRequest request, HttpServletResponse response) throws IOException{
		boolean result=false;
		String functiontype=request.getParameter("functiontype");
		String corp_code=request.getParameter("code");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("ecid", corp_code);
		//????????????
		try {
			List<GwMultiEnterp> ge = baseBiz.getByCondition(GwMultiEnterp.class, conditionMap, null);
			if(ge!=null&&ge.size()>0){
				response.getWriter().print("existed");
				return;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "????????????????????????????????????!");
		}
		//????????????????????????????????????
		GwMultiEnterp ent=new GwMultiEnterp();
		ent.setFuntype("cstd/"+functiontype);
		ent.setReqfmt(0);
		ent.setRespfmt(0);
		ent.setStatus(0);
		ent.setBookcnt(0);
		ent.setMatchcnt(0);
		ent.setCreatetm(new Timestamp(System.currentTimeMillis()));
		ent.setModiytm(new Timestamp(System.currentTimeMillis()));
		ent.setEcid(Integer.parseInt(corp_code));
		result=biz.addFuntype(ent);
		response.getWriter().print(result);
	}
	
	
	/**
	 * ??????????????????
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void funList(HttpServletRequest request, HttpServletResponse response) throws IOException{
		PageInfo pageInfo = new PageInfo();
		LfSysuser curUser = null;
		try {
			 curUser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			 pageSet(pageInfo, request);
			 LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			 //??????ID
			String ecid =request.getParameter("ecid");
			//????????????????????????cstd/jd0001
			String funtype =request.getParameter("funtype");
			request.setAttribute("funtype",funtype);	
			conditionMap.put("ecid", ecid);	
			conditionMap.put("funtype", funtype);	
			List<DynaBean> enterpList=biz.getFunList(conditionMap, pageInfo);
			request.setAttribute("enterpProList",enterpList);
			request.setAttribute("pageInfo",pageInfo);		
			request.setAttribute("ecid", ecid);
			if(StaticValue.getCORPTYPE() ==0){
				request.getRequestDispatcher(empRoot + basePath + "/wg_funList.jsp").forward(request, response);
			}else{
				request.getRequestDispatcher(empRoot + basePath + "/wg_funListMulit.jsp").forward(request, response);
			}

			
		} catch (Exception e) {
			String str="";
			if(curUser!=null){
				str="(corpCode???"+curUser.getCorpCode()+"???opt???"+curUser.getUserId()+"["+curUser.getUserName()+"])";
			}
			EmpExecutionContext.error(e, "??????????????????"+str+"??????????????????!");
		}
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void edit(HttpServletRequest request, HttpServletResponse response) throws Exception{

		String mwInter  =request.getParameter("mwInter");
		String clientInterface=request.getParameter("clientInterface");
		String interfaceName=request.getParameter("interfaceName");
		String req_type=request.getParameter("req_type");
		String resp_type=request.getParameter("resp_type");
		String ecid=request.getParameter("ecid");
		String funtype=request.getParameter("funtype");
		LinkedHashMap<String, String> condition = new LinkedHashMap<String,String>();
		condition.put("funname", mwInter);
		condition.put("ecid", ecid);
		LinkedHashMap<String, String> obj = new LinkedHashMap<String,String>();
		obj.put("cfunname", clientInterface);
		obj.put("custintfname", interfaceName);
		
		boolean result=biz.updateFun(condition,obj,req_type,resp_type);
		
		
		//
		//???????????? ???????????????????????????,??????????????????????????????????????????
		List<GwBaseprotocol> basepro=biz.getfunNum(ecid,funtype,"1");
		int number=0;
		String restotal="";
		if(basepro!=null&&basepro.size()>0){
			number=basepro.size();
			for(int k=0;k<basepro.size();k++){
				restotal=restotal+","+basepro.get(k).getRettype();
			}
		}
		LinkedHashMap<String, String> enternMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> enternContMap = new LinkedHashMap<String, String>();
		enternContMap.put("funtype", funtype);
		enternMap.put("bookcnt", number+"");
		//xml--1--json--2--urlencode--4-
		if(restotal.indexOf("1")>-1&&restotal.indexOf("2")>-1&&restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "7");
		}else if(restotal.indexOf("2")>-1&&restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "6");
		}else if(restotal.indexOf("1")>-1&&restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "5");
		}else if(restotal.indexOf("1")>-1&&restotal.indexOf("2")>-1){
			enternMap.put("reqfmt", "3");
		}else if(restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "4");
		}else if(restotal.indexOf("2")>-1){
			enternMap.put("reqfmt", "2");
		}else if(restotal.indexOf("1")>-1){
			enternMap.put("reqfmt", "1");
		}else{
			enternMap.put("reqfmt", "0");
		}
		List<GwBaseprotocol> respone=biz.getfunNum(ecid,funtype,"2");
		String resptotal="";
		if(respone!=null&&respone.size()>0){
			for(int k=0;k<respone.size();k++){
				resptotal=resptotal+","+respone.get(k).getRettype();
			}
		}
		if(resptotal.indexOf("1")>-1&&resptotal.indexOf("2")>-1&&resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "7");
		}else if(resptotal.indexOf("2")>-1&&resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "6");
		}else if(resptotal.indexOf("1")>-1&&resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "5");
		}else if(resptotal.indexOf("1")>-1&&resptotal.indexOf("2")>-1){
			enternMap.put("respfmt", "3");
		}else if(resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "4");
		}else if(resptotal.indexOf("2")>-1){
			enternMap.put("respfmt", "2");
		}else if(resptotal.indexOf("1")>-1){
			enternMap.put("respfmt", "1");
		}else{
			enternMap.put("respfmt", "0");
		}
		
		

		//????????????????????????????????????????????????????????????
		biz.setStatus(enternMap,enternContMap);
		response.getWriter().print(result);
	
	}
	
	/**
	 * ??????????????????
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void changeStaute(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String ecid=request.getParameter("ecid");
		String enStatus=request.getParameter("enStatus");
		String funname=request.getParameter("funname");
		LinkedHashMap<String, String> valuemap = new LinkedHashMap<String,String>();
		LinkedHashMap<String, String> conmap = new LinkedHashMap<String,String>();
		conmap.put("ecid", ecid);
		conmap.put("funname", funname);
		if(enStatus!=null){
			if("0".equals(enStatus)){
				valuemap.put("status", "1");
			}else if("1".equals(enStatus)){
				valuemap.put("status", "0");
			}
		}
		boolean result=biz.changeStaute(valuemap,conmap);
		response.getWriter().print(result);
	}
	
	/**
	 * ????????????
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String ecid=request.getParameter("ecid");
		String funname=request.getParameter("funname");
		String funtype=request.getParameter("funtype");
		LinkedHashMap<String, String> conmap = new LinkedHashMap<String,String>();
		conmap.put("ecid", ecid);
		conmap.put("funname", funname);
		conmap.put("funtype", funtype);
		
		//???????????????(??????)
		List<GwBaseprotocol> basepro=biz.getfunNum(ecid,funtype,"1");
		int number=0;
		String restotal="";
		if(basepro!=null&&basepro.size()>0){
			number=basepro.size();
			for(int k=0;k<basepro.size();k++){
				restotal=restotal+","+basepro.get(k).getRettype();
			}
		}
		LinkedHashMap<String, String> enternMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> enternContMap = new LinkedHashMap<String, String>();
		enternContMap.put("funtype", funtype);
		if(number>0){
			number=number-1;
		}
		enternMap.put("bookcnt", number+"");
		//xml--1--json--2--urlencode--4-
		if(restotal.indexOf("1")>-1&&restotal.indexOf("2")>-1&&restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "7");
		}else if(restotal.indexOf("2")>-1&&restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "6");
		}else if(restotal.indexOf("1")>-1&&restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "5");
		}else if(restotal.indexOf("1")>-1&&restotal.indexOf("2")>-1){
			enternMap.put("reqfmt", "3");
		}else if(restotal.indexOf("4")>-1){
			enternMap.put("reqfmt", "4");
		}else if(restotal.indexOf("2")>-1){
			enternMap.put("reqfmt", "2");
		}else if(restotal.indexOf("1")>-1){
			enternMap.put("reqfmt", "1");
		}else{
			enternMap.put("reqfmt", "0");
		}
		List<GwBaseprotocol> respone=biz.getfunNum(ecid,funtype,"2");
		String resptotal="";
		if(respone!=null&&respone.size()>0){
			for(int k=0;k<respone.size();k++){
				resptotal=resptotal+","+respone.get(k).getRettype();
			}
		}
		if(resptotal.indexOf("1")>-1&&resptotal.indexOf("2")>-1&&resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "7");
		}else if(resptotal.indexOf("2")>-1&&resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "6");
		}else if(resptotal.indexOf("1")>-1&&resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "5");
		}else if(resptotal.indexOf("1")>-1&&resptotal.indexOf("2")>-1){
			enternMap.put("respfmt", "3");
		}else if(resptotal.indexOf("4")>-1){
			enternMap.put("respfmt", "4");
		}else if(resptotal.indexOf("2")>-1){
			enternMap.put("respfmt", "2");
		}else if(resptotal.indexOf("1")>-1){
			enternMap.put("respfmt", "1");
		}else{
			enternMap.put("respfmt", "0");
		}
		
		int result=biz.del(conmap,enternMap,enternContMap);
		if(result>0){
			response.getWriter().print("true");
		}else{
			response.getWriter().print("false");
		}
		
	}
	
	/**
	 * ????????????
	 */
	public void mapping(HttpServletRequest request, HttpServletResponse response)throws IOException{
		String ecid=request.getParameter("ecid");
		//
		String funname=request.getParameter("funname");
		//?????????????????????????????????json???xml???
		String req=request.getParameter("req");
		//?????????????????????????????????json???xml???
		String resp=request.getParameter("resp");
		String funtype=request.getParameter("funtype");
		String cmdtype=request.getParameter("cmdtype");
		//???????????????1-?????? 2-??????
		
		try {
			//????????????????????????
			List<DynaBean> list=biz.getMWCode(funname,cmdtype);
			request.setAttribute("mwCodeList", list);
			//??????????????????????????????
			List<GwBaseprotocol> contentList=biz.getParseContext(funname, cmdtype);
			String content="";
			if(contentList!=null&&contentList.size()>0){
				content=contentList.get(0).getFmtmsg();
			}
			List<GwBaseprotocol> baseinfo=biz.getbase(funname,funtype,cmdtype);
			if(baseinfo!=null&&baseinfo.size()>0){
				String mtmsg=baseinfo.get(0).getFmtmsg();
				request.setAttribute("mtmsg", mtmsg);
			}
			String retMsg="";
			//????????????????????????????????????????????????GW_PUSHPROTOMTCH
			//???FUNNAME???CMDTYPE???CARGNAME???BELONG??????????????????,1-MO ,2-RPT
			if("MO".equals(funname.toUpperCase())||"RPT".equals(funname.toUpperCase())){
				String Str="1";
				if("MO".equals(funname)){
					Str="1";
				}else{
					Str="2";
				}
				List<GwPushprotomtch> morptList=biz.getMORPT(Str,cmdtype);
				request.setAttribute("morptList", morptList);
				retMsg=getPushList(morptList,req,resp,list);
			}else{
				//??????????????????????????????GW_PROTOMTCH
				List<GwProtomtch> protoList=biz.getInter(funname,cmdtype,funtype);
				retMsg=getProtoList(protoList,req,resp,list);
				request.setAttribute("protoList", protoList);
			}
			request.setAttribute("retMsg", retMsg);
			//????????????
			request.setAttribute("content", content);
			request.setAttribute("funtype", request.getParameter("funtype"));
			request.setAttribute("ecid", ecid);
			request.setAttribute("funname", funname);
			request.setAttribute("req", req);
			request.setAttribute("resp", resp);
			request.setAttribute("cmdtype", cmdtype);
			request.setAttribute("status", request.getParameter("status"));
			//?????????????????????????????????
			request.setAttribute("saveValue", request.getParameter("saveValue"));

			request.getRequestDispatcher(
					empRoot + basePath + "/wg_mapping.jsp").forward(
					request, response);
		} catch (Exception e) {
            EmpExecutionContext.error(e, "???????????????");
		}
	}
	
	/***
	 * ???????????????????????????????????????
	 * @param mwCodeList
	 * @param margname
	 * @param cargvalue
	 * @return
	 */
	public String getStr(List<DynaBean> mwCodeList,String margname,String cargvalue){
		String ret="";
		if(cargvalue!=null&&!"".equals(cargvalue.trim())&&(margname==null||"".equals(margname.trim()))){
			ret=ret+"<option value='0' selected>?????????</option>";
			ret=ret+"<option value='-1'>?????????</option>";
		}else if((cargvalue==null||"".equals(cargvalue.trim()))&&(margname==null||"".equals(margname.trim()))){
			ret=ret+"<option value='0' >?????????</option>";
			ret=ret+"<option value='-1' selected>?????????</option>";
		}else{
			ret=ret+"<option value='0'>?????????</option>";
			ret=ret+"<option value='-1'>?????????</option>";
		}
		
		if(mwCodeList!=null){
			List<DynaBean> beanlist=(List<DynaBean>)mwCodeList;
			for(int k=0;k<beanlist.size();k++){
				DynaBean db=beanlist.get(k);
				String argname=db.get("argname").toString();
				if(margname!=null&&margname.equals(argname)){
					ret=ret+"<option value='"+argname+"' selected>"+argname+"</option>";
				}else{
					ret=ret+"<option value='"+argname+"'>"+argname+"</option>";
				}

			}
		}
		
		return ret;
	}
	
	/***
	 * ???????????????????????????
	 * @param type
	 * @return
	 */
	public String getselecttype(Integer type){
		String selecttype="";
		if("8".equals(type.toString())){
			selecttype=selecttype+"<option value='8' selected>string</option>";
		}else{
			selecttype=selecttype+"<option value='8'>string</option>";
		}
		if("1".equals(type.toString())){
			selecttype=selecttype+"<option value='1' selected>xml</option>";
		}else{
			selecttype=selecttype+"<option value='1'>xml</option>";
		}
		if("2".equals(type.toString())){
			selecttype=selecttype+"<option value='2' selected>json</option>";
		}else{
			selecttype=selecttype+"<option value='2'>json</option>";
		}
		
		if("4".equals(type.toString())){
			selecttype=selecttype+"<option value='4' selected>urlencode</option>";
		}else{
			selecttype=selecttype+"<option value='4'>urlencode</option>";
		}

		if("16".equals(type.toString())){
			selecttype=selecttype+"<option value='16' selected>int</option>";
		}else{
			selecttype=selecttype+"<option value='16'>int</option>";
		}
		if("32".equals(type.toString())){
			selecttype=selecttype+"<option value='32' selected>time</option>";
		}else{
			selecttype=selecttype+"<option value='32'>time</option>";
		}
		
		if("64".equals(type.toString())){
			selecttype=selecttype+"<option value='64' selected>other</option>";
		}else{
			selecttype=selecttype+"<option value='64'>other</option>";
		}
		return selecttype;
	}
	
	
	/**
	 * ??????MO,RPT????????????
	 * @param protoList
	 * @param req
	 * @param resp
	 * @param mwCodeList
	 * @return
	 */
	public String getPushList(List<GwPushprotomtch> protoList,String req,String resp,List<DynaBean> mwCodeList){
		StringBuffer reStr= new StringBuffer();
		if(protoList!=null){
			if(("2".equals(req))||"2".equals(resp)){
				for(int i=0;i<protoList.size();i++){
					GwPushprotomtch mtch=protoList.get(i);
					String selectOp=getStr(mwCodeList,mtch.getMargname(),mtch.getCargvalue());
					String json_select="";
					//????????????????????????json??????????????????
					if(mtch.getCargtype()==2){
						json_select="disabled='disabled'";
					}
					
					//???????????? ??????
					String gu="";
					if(!"".equals(mtch.getCargvalue().trim())){
						gu="<input type='text' name='fixed"+i+"' id='fixed"+i+"' value='"+mtch.getCargvalue()+"' style='width: 30px;' maxlength='30'>";
					}else{
						gu="<input type='text' name='fixed"+i+"' id='fixed"+i+"' value='' disabled='disabled' style='width: 30px;' maxlength='30'>";
					}
				reStr.append("<tr style='background:#dfe9f9' class='listAll'><td>"+mtch.getCargname()+"<input name='cargname"+i+"' value='"+mtch.getCargname()+"' type='hidden' /><input name='mainNode"+i+"' value='"+mtch.getBelongtype()+";"+mtch.getBelong()+"' type='hidden' /></td>" +
				"<td> <select name='mwfield"+i+"' id='mwfield"+i+"' onchange='change(this)'>" +
				selectOp+
				"</select></td><td>" +
				"<select name='mwattr"+i+"' onchange='propertySelect(this,"+mtch.getCargtype()+")' "+json_select+"  onclick='beforeSelect(this)'>" +
				getselecttype(mtch.getCargtype())
				+"</select>" +
				"</td>" +
				"<td>" +gu+"</td></tr>");
			}
			}else{
				for(int i=0;i<protoList.size();i++){
					GwPushprotomtch mtch=protoList.get(i);
				String selectOp=getStr(mwCodeList,mtch.getMargname(),mtch.getCargvalue());
				String json_select="";
				//????????????????????????json??????????????????
				if(mtch.getCargtype()==2){
					json_select="disabled='disabled'";
				}
				//???????????? ??????
				String gu="";
				if(!"".equals(mtch.getCargvalue().trim())){
					gu="<input type='text' name='fixed"+i+"' id='fixed"+i+"' value='"+mtch.getCargvalue()+"' style='width: 30px;' maxlength='30'>";
				}else{
					gu="<input type='text' name='fixed"+i+"' id='fixed"+i+"' value='' disabled='disabled' style='width: 30px;' maxlength='30'>";
				}
				reStr.append("<tr style='background:#dfe9f9' class='listAll'><td>"+mtch.getCargname()+"<input name='cargname"+i+"' value='"+mtch.getCargname()+"' type='hidden' /></td>" +
				"<td> <select name='mwfield"+i+"' id='mwfield"+i+"' onchange='change(this)'>" +selectOp+
				"</select></td><td>" +
				"<select name='mwattr"+i+"' onchange='propertySelect(this,"+mtch.getCargtype()+")' "+json_select+" onfocus='beforeSelect(this)'>" +getselecttype(mtch.getCargtype()) +
				"</select>" +
				"</td><td>"+gu+"</td></tr>");
			}
			}
		}
		return reStr.toString();
	}
	
	/**
	 * ??????MO,RPT???????????????
	 * @param protoList
	 * @param req
	 * @param resp
	 * @param mwCodeList
	 * @return
	 */
	public String getProtoList(List<GwProtomtch> protoList,String req,String resp,List<DynaBean> mwCodeList){
		StringBuffer reStr= new StringBuffer();
		if(protoList!=null){
			if(("2".equals(req))||"2".equals(resp)){
				for(int i=0;i<protoList.size();i++){
					GwProtomtch mtch=protoList.get(i);
					String selectOp=getStr(mwCodeList,mtch.getMargname(),mtch.getCargvalue());
					String json_select="";
					//????????????????????????json??????????????????
					if(mtch.getCargtype()==2){
						json_select="disabled='disabled'";
					}
					//???????????? ??????
					String gu="";
					if(!"".equals(mtch.getCargvalue().trim())){
						gu="<input type='text' name='fixed"+i+"' id='fixed"+i+"' value='"+mtch.getCargvalue()+"' style='width: 30px;' maxlength='30'>";
					}else{
						gu="<input type='text' name='fixed"+i+"' id='fixed"+i+"' value='' disabled='disabled' style='width: 30px;' maxlength='30'>";
					}
				reStr.append("<tr style='background:#dfe9f9' class='listAll'><td>"+mtch.getCargname()+"<input name='cargname"+i+"' value='"+mtch.getCargname()+"' type='hidden' /><input name='mainNode"+i+"' value='"+mtch.getBelongtype()+";"+mtch.getBelong()+"' type='hidden' /></td>" +
				"<td> <select name='mwfield"+i+"'  id='mwfield"+i+"'  onchange='change(this)'>" +selectOp+
				"</select></td><td>" +
				"<select name='mwattr"+i+"' onchange='propertySelect(this,"+mtch.getCargtype()+")' "+json_select+" onfocus='beforeSelect(this)'>" +
				getselecttype(mtch.getCargtype())
				+"</select>" +
				"</td>" +
				"<td>" +gu+"</td></tr>");
			}
			}else{
				for(int i=0;i<protoList.size();i++){
					GwProtomtch mtch=protoList.get(i);
				String selectOp=getStr(mwCodeList,mtch.getMargname(),mtch.getCargvalue());
				String json_select="";
				//????????????????????????json??????????????????
				if(mtch.getCargtype()==2){
					json_select="disabled='disabled'";
				}
				//???????????? ??????
				String gu="";
				if(!"".equals(mtch.getCargvalue().trim())){
					gu="<input type='text' name='fixed"+i+"' id='fixed"+i+"' value='"+mtch.getCargvalue()+"' style='width: 30px;' maxlength='30'>";
				}else{
					gu="<input type='text' name='fixed"+i+"' id='fixed"+i+"' value='' disabled='disabled' style='width: 30px;' maxlength='30'>";
				}
				reStr.append("<tr style='background:#dfe9f9' class='listAll'><td>"+mtch.getCargname()+"<input name='cargname"+i+"' value='"+mtch.getCargname()+"' type='hidden' /></td>" +
				"<td> <select name='mwfield"+i+"' id='mwfield"+i+"' onchange='change(this)'>" +selectOp+
				"</select></td><td>" +
				"<select name='mwattr"+i+"' onchange='propertySelect(this,"+mtch.getCargtype()+")' "+json_select+" onfocus='beforeSelect(this)'>" +getselecttype(mtch.getCargtype()) +
				"</select>" +
				"</td><td>"+gu+"</td></tr>");
			}
			}
		}
		return reStr.toString();
		
	}
	
	
	/**
	 * ????????????????????????????????????????????????
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void checkStyle(HttpServletRequest request, HttpServletResponse response)throws Exception{
		//???????????????
		String req=request.getParameter("req");
		//???????????????
		String resp=request.getParameter("resp");
		String oldStyle=request.getParameter("oldStyle");
		oldStyle=oldStyle.replaceAll("\n","");
		//?????????????????????????????????????????????
		String cmdtype=request.getParameter("cmdtype");
        JSONObject json=new JSONObject();
		json.put("style","");
		json.put("nodeList","");
		//???????????????1.??????(?????????????????????????????????????????????)
		if("1".equals(cmdtype)){
			json=newString(json,req,oldStyle);
		}else if("2".equals(cmdtype)||"3".equals(cmdtype)||"4".equals(cmdtype)||"5".equals(cmdtype)){
			json=newString(json,resp,oldStyle);
		}
		response.getWriter().print(json);
	}
	
	/**
	 * ????????????????????????????????????json???xml?????????
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void pashURLCode(HttpServletRequest request, HttpServletResponse response)throws Exception{
		//???????????????
		String req=request.getParameter("req");
		//???????????????
		String resp=request.getParameter("resp");
		String oldStyle=request.getParameter("oldStyle");
		//??????json???xml
		String cmdtype=request.getParameter("cmdtype");
		//????????????
		String selected=request.getParameter("jsonselect");
		//???????????????????????????
        JSONObject json=new JSONObject();
		json.put("style","");
		json.put("nodeList","");
		if(oldStyle.indexOf(selected)>-1){
			String usercode="";
			usercode=oldStyle.substring(oldStyle.indexOf(selected),oldStyle.indexOf("}]")+2);
			usercode=usercode.substring(usercode.indexOf("=")+1);
			//???????????????1.??????(?????????????????????????????????????????????)
			json=newString(json,cmdtype,usercode);
		}
		
		response.getWriter().print(json);
		
	}
	
	
	/***
	 * ??????????????????
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void saveContext(HttpServletRequest request, HttpServletResponse response)throws Exception{
		//??????ID
		String ecid=request.getParameter("ecid");
		//????????????
		String funname=request.getParameter("funname");
		//????????????
		String funtype=request.getParameter("funtype");
		//????????????
		String cmdtype=request.getParameter("cmdtype");
		
		String req=request.getParameter("req");
		String resp=request.getParameter("resp");
		LinkedHashMap<String, String> baseCondition = new LinkedHashMap<String,String>();
		baseCondition.put("ecid", ecid);
		baseCondition.put("funname", funname);
		baseCondition.put("funtype", funtype);
		baseCondition.put("cmdtype", cmdtype);
		LinkedHashMap<String, String> baseUpdatMap = new LinkedHashMap<String,String>();
		baseUpdatMap.put("fmtmsg", request.getParameter("newStyle"));
		String number=request.getParameter("number");
		int ret=0;
		//????????????????????????
		//????????????????????????????????????????????????GW_PUSHPROTOMTCH
		//???FUNNAME???CMDTYPE???CARGNAME???BELONG??????????????????
		if(!"MO".equals(funname)&&!"RPT".equals(funname)){
			List<GwProtomtch> morptList=biz.getInter(funname,cmdtype,funtype);
			if(number!=null&&!"".equals(number)){
				//?????????????????????????????????
				if(isNumeric(number)){
					List<GwProtomtch> add_list =new ArrayList<GwProtomtch>();
					int num=Integer.parseInt(number);
						for(int k=0;k<num;k++){
							String cargname=request.getParameter("cargname"+k);
							String mwfield=request.getParameter("mwfield"+k);
							String fixed=request.getParameter("fixed"+k);
							String mwattr=request.getParameter("mwattr"+k);
							String mainNode=request.getParameter("mainNode"+k);
							GwProtomtch add_mtch=new GwProtomtch();
							if(mwattr!=null){
								add_mtch.setCargtype(Integer.parseInt(mwattr));	
							}else{//??????json?????????????????????????????????????????????2
								add_mtch.setCargtype(2);	
							}
							if(cargname!=null){
								add_mtch.setCargname(cargname.trim());
							}
							//?????????
							if("0".equals(mwfield)){
								add_mtch.setMargname(" ");
								if(fixed!=null&&!"".equals(fixed)){
									add_mtch.setCargvalue(fixed);
								}else{
									add_mtch.setCargvalue(" ");
								}
								
							}else if("-1".equals(mwfield)){
								add_mtch.setMargname(" ");
								add_mtch.setCargvalue(" ");
							}else{
								add_mtch.setMargname(mwfield);
								add_mtch.setCargvalue(" ");
							}
							add_mtch.setEcid(Integer.parseInt(ecid));
							add_mtch.setFuntype(funtype);
							add_mtch.setFunname(funname);
							add_mtch.setCmdtype(Integer.parseInt(cmdtype));
							if(mainNode==null||"".equals(mainNode)){
								add_mtch.setBelongtype(0);
								add_mtch.setBelong(" ");	
							}else{
								String[] type=mainNode.split(";");
								if(type.length>=2){//????????????????????????????????????????????????????????????????????????????????????????????????????????????
									add_mtch.setBelongtype(Integer.parseInt(type[0]));
									add_mtch.setBelong(type[1]);	
								}else if(type.length==1){//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
									add_mtch.setBelongtype(Integer.parseInt(type[0]));
									add_mtch.setBelong(" ");	
								}else{
									add_mtch.setBelongtype(0);
									add_mtch.setBelong(" ");	
								}

							}
							add_mtch.setCreatetime(new Timestamp(System.currentTimeMillis()));
							add_mtch.setModiftime(new Timestamp(System.currentTimeMillis()));
							add_mtch.setReserve(" ");
//							add_mtch.setBelongtype();
							add_list.add(add_mtch);
						}
				
						ret= biz.saveMapping(baseCondition,baseUpdatMap,add_list,morptList);
				}
			}
		}else{
			//??????????????????????????????GW_PROTOMTCH

			List<GwPushprotomtch> add_list =new ArrayList<GwPushprotomtch>();
			if(number!=null&&!"".equals(number)){
				String flag="1";
				if("MO".equals(funname)){
					flag="1";
				}else{
					flag="2";
				}
				List<GwPushprotomtch> protoList=biz.getMORPT(flag,cmdtype);
				//?????????????????????????????????
				if(isNumeric(number)){
					int num=Integer.parseInt(number);
						for(int k=0;k<num;k++){
							String cargname=request.getParameter("cargname"+k);
							String mwfield=request.getParameter("mwfield"+k);
							String fixed=request.getParameter("fixed"+k);
							String mwattr=request.getParameter("mwattr"+k);
							String mainNode=request.getParameter("mainNode"+k);
							GwPushprotomtch add_mtch=new GwPushprotomtch();
							if(cargname!=null){
								add_mtch.setCargname(cargname.trim());
							}
							//?????????
							if("0".equals(mwfield)){
								add_mtch.setMargname(" ");
								if(fixed!=null&&!"".equals(fixed)){
									add_mtch.setCargvalue(fixed);
								}else{
									add_mtch.setCargvalue(" ");
								}
							}else if("-1".equals(mwfield)){
								add_mtch.setMargname(" ");
								add_mtch.setCargvalue(" ");
							}else{
								add_mtch.setMargname(mwfield);
								add_mtch.setCargvalue(" ");
							}
							add_mtch.setUserid("000000");
							if("MO".equals(funname)){
								add_mtch.setPushflag(1);
							}else{
								add_mtch.setPushflag(2);
							}
							if(mainNode==null||"".equals(mainNode)){
								add_mtch.setBelongtype(0);
								add_mtch.setBelong(" ");	
							}else{
								String[] type=mainNode.split(";");
								if(type.length>=2){
									add_mtch.setBelongtype(Integer.parseInt(type[0]));
									add_mtch.setBelong(type[1]);	
								}else if(type.length==1){//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
									add_mtch.setBelongtype(Integer.parseInt(type[0]));
									add_mtch.setBelong(" ");	
								}else{
									add_mtch.setBelongtype(0);
									add_mtch.setBelong(" ");	
								}
							}
							add_mtch.setEcid(Integer.parseInt(ecid));
							add_mtch.setCmdtype(Integer.parseInt(cmdtype));
							if(mwattr!=null){
								add_mtch.setCargtype(Integer.parseInt(mwattr));	
							}else{
								//?????????????????????????????????json????????????????????????????????????
								add_mtch.setCargtype(2);	
							}
							add_mtch.setCmdtype(Integer.parseInt(cmdtype));
							add_mtch.setReserve(" ");
							add_list.add(add_mtch);
						}
					}
				ret= biz.saveMORPTMapping(baseCondition,baseUpdatMap,add_list,protoList);
				
			}
			
		}
		
		request.getRequestDispatcher("/wg_apiBaseMage.htm?method=mapping&ecid="+ecid+"&funname="+funname+"&req="+req+"&resp="+resp+"&funtype="+funtype+"&saveValue="+ret).forward(request, response);


	}
	//?????????????????????
	public boolean isNumeric(String str){   
		   Pattern pattern = Pattern.compile("[0-9]*");   
		   Matcher isNum = pattern.matcher(str);  
		   if( !isNum.matches() ){  
		       return false;   
		   }   
		   return true;   
		}
	
	//???????????????????????????
	public JSONObject newString(JSONObject json,String type,String old) {

		try{
			DuXMLDoc xml=new DuXMLDoc();
			//??????xml??????
			if("1".equals(type)){
				if(isXML(old)){
					old=old.replace("<?xml version=1.0 encoding=utf-8?>", "<?xml version=\"1.0\" encoding=\"utf-8\"?>");
					old=util.XMLformat(old);
					old=old.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
					old=old.trim();
					json.put("style",old);
					json.put("nodeList",xml.xmlElements(old));
				}else{
					json.put("style","");
					json.put("nodeList","");
					return json;
				}
			}else if("2".equals(type)){
				if(isJson(old)){
					old=util.formatJson(old);
					
					json.put("style",old);
					json.put("nodeList",util.getJsonNode(old));
				}else{
					json.put("style","");
					json.put("nodeList","");
					return json;
				}
			}else if("4".equals(type)){//?????????
				if(isURLencode(old)){
					String ur=util.getURLencodeNode(old);
					json.put("style",old);
					json.put("nodeList",ur);
				}else{
					json.put("style","");
					json.put("nodeList","");
					return json;
				}
			}
		} catch (Exception e) {
            EmpExecutionContext.error(e, "???????????????");
		}
		return json;
	}
	
	/** 
	* ???????????????Json?????? 
	*/ 
	public static boolean isJson(String value) {
		if (StringUtils.isBlank(value)) {
			return false;
		}
		try {
			new JsonParser().parse(value);
			return true;
		} catch (JsonParseException e) {
			return false;
		}
	}
	
	/** 
	* ???????????????xml?????? 
	*/ 
	public static boolean isXML(String value) { 
	try { 
		DocumentHelper.parseText(value.replace("<?xml version=1.0 encoding=utf-8?>", "")); 
		} catch (DocumentException e) { 
			return false; 
	} 
		return true; 
	}
	
	/** 
	* ???????????????URLencode?????? 
	*/ 
	public  static boolean isURLencode(String value) { 
		if(value==null){
			return false;
		}
		String[]  array=value.split("&");
		if(array==null||array.length==0){
			return false;
		}
		for(int k=0;k<array.length;k++){
			String every=array[k];
			if("".equals(every.trim())){
				return false;
			}
			String[] keyvalue= every.split("=");
			if(keyvalue==null||keyvalue.length==0){
				return false;
			}
			if(keyvalue.length!=2){
				return false;
			}
			
		}
		
		return true; 
	}
	
	/**
	 * ??????????????????
	 * @param request
	 * @param response
	 */
	public void customersList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		PageInfo pageInfo = new PageInfo();
		//?????????????????????
		LfSysuser loginSysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
		//?????????????????? 
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		//????????????
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//?????????????????????
		String corpCode = loginSysuser.getCorpCode();
		 List<LfCorp> corpList = null;
		//??????100000??????????????????????????????
//		if(corpCode != null && "100000".equals(corpCode))
//		{
			//???????????????????????????????????????
			String code = request.getParameter("code");
			if(code != null && !"".equals(code))
			{
				code = URLDecoder.decode(code, "UTF-8");
				conditionMap.put("corpCode&like", code);
			}
			String name = request.getParameter("condcorpname");
			if(name != null && !"".equals(name))
			{
				name = URLDecoder.decode(name, "UTF-8");
				conditionMap.put("corpName&like", name);
			}
			//???10?????????
			conditionMap.put("corpCode&<>", "100000");
			orderbyMap.put("corpCode", "desc");
		try {
			pageSet(pageInfo, request);
			corpList = baseBiz.getByConditionNoCount(LfCorp.class, null, conditionMap,
					orderbyMap, pageInfo);
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "??????????????????????????????!");
		}
		
//		}else
//		{
//			EmpExecutionContext.error("?????????????????????????????????????????????????????????????????????????????????????????????"+corpCode);
//		}
		request.setAttribute("conditionMap", conditionMap);
		request.setAttribute("corpList", corpList);
		request.setAttribute("pageInfo",pageInfo);
		if(StaticValue.getCORPTYPE() ==0){
			request.getRequestDispatcher(empRoot + basePath + "/wg_customersList.jsp").forward(request, response);
		}else{
			request.getRequestDispatcher(empRoot + basePath + "/wg_customersListMulit.jsp").forward(request, response);
		}
	}
	

}
