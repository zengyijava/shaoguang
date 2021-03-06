package com.montnets.emp.common.servlet;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.FrameControlBiz;
import com.montnets.emp.common.biz.LoginBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.LoginInfo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.constant.ViewParams;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.entity.LfChangeLog;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.gateway.AProInfo;
import com.montnets.emp.entity.system.LfSkin;
import com.montnets.emp.entity.system.LfTheme;
import com.montnets.emp.entity.system.LfUser2skin;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.reportform.cache.DepAndUserTreeInfoCache;
import com.montnets.emp.reportform.cache.InitDataCache;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.MD5;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.beanutils.DynaBean;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ??????????????????Servlet
 * 
 * @author Administrator
 */
@SuppressWarnings("serial")
public class FrameControlSvt extends HttpServlet
{

	String	empRoot	= "frame";

	BaseBiz	baseBiz	= new BaseBiz();

	FrameControlBiz frameBiz = new FrameControlBiz();
	
	private LoginBiz biz = new LoginBiz();

	/**
	 * doget??????
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * dopost??????
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String method = request.getParameter("method");
		String requestUrl = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
		requestUrl = requestUrl.substring(requestUrl.lastIndexOf("/") + 1);
		try
		{
			//?????????404????????????????????????finda??????
			if("error404".equals(requestUrl))
			{
				method = "find";
			}
			else if(request.getParameter("method") != null)
			{
				method = request.getParameter("method");
			}
			else
			{
				method = "find";
			}

			// ??????????????????
			String tkn = request.getParameter("tkn");
			int ISCLUSTER = StaticValue.getISCLUSTER();
			if(request.getSession() != null)
			{
				if(tkn != null)
				{

					// ??????token????????????????????????????????????session????????????????????????
					if(StaticValue.getLoginInfoMap().get(tkn) != null)
					{
						// ??????loginInfoMap??????sessionid????????????sessionId??????
						if(!StaticValue.getLoginInfoMap().get(tkn).checkSessionId(request.getSession().getId()))
						{
							response.sendRedirect(request.getContextPath() + "/common/logoutEmp.jsp");
							return;
						}
					}
					// ????????????????????????
					else
					{
						response.sendRedirect(request.getContextPath() + "/common/logoutEmp.jsp");
						return;
					}
				}
			}
			else
			{
				// ????????????session????????????????????????
				response.sendRedirect(request.getContextPath() + "/common/logoutEmp.jsp");
				return;
			}
			//????????????
			DepAndUserTreeInfoCache.clear();
			InitDataCache.INSTANCE.clear();

			Class c = this.getClass();
			Class[] parameterTypes = {HttpServletRequest.class,HttpServletResponse.class};
			Method clazzMethod = c.getMethod(method, parameterTypes);
			Object[] Objparams = {request,response};
			clazzMethod.invoke(this, Objparams);
		}
		catch (SecurityException e)
		{
			EmpExecutionContext.error(e,"svt??????/???????????????");
		}
		catch (NoSuchMethodException e)
		{
			EmpExecutionContext.error(e,"svt??????/???????????????????????????????????????");
		}
		catch (IllegalArgumentException e)
		{
			EmpExecutionContext.error(e,"svt??????/???????????????");
		}
		catch (IllegalAccessException e)
		{
			EmpExecutionContext.error(e,"svt??????/???????????????");
		}
		catch (InvocationTargetException e)
		{
			EmpExecutionContext.error(e,"svt??????/???????????????");
		}
	}

	/**
	 * ????????????????????????????????????
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		// ??????????????????
		String requestUrl = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
		requestUrl = requestUrl.substring(requestUrl.lastIndexOf("/") + 1);
		// ??????????????????
		if("logout".equals(requestUrl))
		{
			// LfSysuser lfSysuser = (LfSysuser)
			// session.getAttribute("loginSysuser");
			@SuppressWarnings("unchecked")
			java.util.Enumeration<String> e = session.getAttributeNames();
			String sessionName = null;
			// ??????????????????session??????????????????
			while(e.hasMoreElements())
			{
				sessionName = e.nextElement();
				session.removeAttribute(sessionName);
			}
			session.invalidate();
			// ??????????????????
			response.sendRedirect(request.getContextPath());

		}
		else
		{
			// ????????? ?????????????????????????????????
			final String loginUrl = "emp_tz.hts";
			if(loginUrl.equals(requestUrl)){
				session = SysuserUtil.regenerateSessionId(request);
			}

			// ?????????????????????????????????
			int corpType = StaticValue.getCORPTYPE();
			String isMulti = corpType == 1 ? "true" : "false";


			String tkn = session.getId() + String.valueOf(System.currentTimeMillis());

			// ?????????????????????????????????
			//String isYanzhengma = SystemGlobals.getValue(StaticValue.EMP_LOGIN_YANZHENGMA);
			//???????????????????????????????????????????????? modify by tanglili 20210201
			String isYanzhengma = SystemGlobals.getSysParam("loginYanZhengMa");
			// ??????????????????session???
			if(isYanzhengma != null)
			{
				session.setAttribute("yanzhengma", isYanzhengma);
			}
			else
			{
				session.setAttribute("yanzhengma", "true");
			}

			request.setAttribute("yanzhengma", isYanzhengma == null ? "true" : isYanzhengma);
			request.setAttribute("isMulti", isMulti);
			request.setAttribute("tkn", tkn);
			// ??????????????????
			request.getRequestDispatcher("/frame/frame3.0/login.jsp").forward(request, response);
		}
	}

	/**
	 * ?????????????????????logo
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void picTz(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter writer = response.getWriter();
		String dirUrl = this.empRoot + "/" + SystemGlobals.getValue(StaticValue.EMP_WEB_FRAME) + StaticValue.LOGOPATH;
		String imgUrl = request.getParameter("imgUrl");
		//??????????????????
		if(imgUrl != null && !"".equals(imgUrl))
		{
			//??????logo????????????????????????????????????????????????logo
			File dirFile = new File(new TxtFileUtil().getWebRoot() + dirUrl + imgUrl);
			if(!dirFile.exists())
			{
				imgUrl = "logo_1.png";
			}
		}
		else
		{
			imgUrl = "logo_1.png";
		}
		writer.print(request.getContextPath() + "/" + dirUrl + imgUrl);
	}

	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getLoginInfoMap(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Map<String, LoginInfo> loginMap = StaticValue.getLoginInfoMap();
		Iterator<String> itr = loginMap.keySet().iterator();
		String key;

		// ??????html?????????
		StringBuffer sb = new StringBuffer("<table id='content'><thead><tr align='center'><th border=1>TOKEN</th>" +
				"<th border=1>SessionId</th>" +
				"<th border=1>UserId</th>" +
				"<th border=1>UserName</th>" +
				"<th border=1>GuId</th>" +
				"<th border=1>CorpCode</th></tr></thead>");
		// ??????????????????
		while(itr.hasNext())
		{
			sb.append("<tr align='center'>");
			key = itr.next();
			LoginInfo logininfo = loginMap.get(key);
			sb.append("<td border=1>" + key + "</td>");
			sb.append("<td border=1>" + logininfo.getSessionId() + "</td>");
			sb.append("<td border=1>" + logininfo.getUserId() + "</td>");
			sb.append("<td border=1>" + logininfo.getUserName() + "</td>");
			sb.append("<td border=1>" + logininfo.getGuId() + "</td>");
			sb.append("<td border=1>" + logininfo.getCorpCode() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		request.setAttribute("tableStr", sb.toString());
		request.getRequestDispatcher(this.empRoot + "/frame3.0/loginInfo.jsp?timee=" + System.currentTimeMillis()).forward(request, response);
	}

	/**
	 * ????????????
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void setSkin(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//???????????????
		String skin = request.getParameter("skin");
		//?????????id
		String userid = request.getParameter("userid");
		//??????????????????
		String themeCode = request.getParameter("themeCode");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		conditionMap.put("userid", userid);

		BaseBiz baseBiz = new BaseBiz();
		//?????????????????????????????????
		List<LfUser2skin> userskinList = baseBiz.getByCondition(LfUser2skin.class, conditionMap, null);
		if("frame2.5".equalsIgnoreCase(themeCode) || "frame3.0".equalsIgnoreCase(themeCode) || "frame4.0".equalsIgnoreCase(themeCode)) {
            if(userskinList != null && userskinList.size() > 0)
            {
                //??????????????????????????????????????????????????????
                LfUser2skin u2S = userskinList.get(0);
                u2S.setSkincode(skin);
                u2S.setThemecode(themeCode);
                baseBiz.updateObj(u2S);
            }
            else
            {
                //????????????????????????????????????
                LfUser2skin u2S = new LfUser2skin();
                u2S.setSkincode("default");
                u2S.setUserid(Long.valueOf(userid));
                u2S.setThemecode(themeCode);
                u2S.setSid(1l);

                baseBiz.addObj(u2S);
            }
        }

		//????????????
		request.getSession().setAttribute(StaticValue.EMP_WEB_FRAME, themeCode);
		request.getSession().setAttribute("stlyeSkin", request.getContextPath() + "/frame/" + themeCode + "/skin/" + skin);
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getSkinList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		String frame = (String) request.getSession().getAttribute(StaticValue.EMP_WEB_FRAME);
		//???????????????
		String themecode = request.getParameter("themecode");
		//???????????????????????????????????????????????????
		if(themecode != null && !"".equals(themecode))
		{
			frame = themecode;
		}

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		conditionMap.put("themecode", frame);
		//?????????????????????????????????
		List<LfSkin> skinList = baseBiz.getByCondition(LfSkin.class, conditionMap, null);
		StringBuffer sb = new StringBuffer("<div class='themeSkin' framecode='" + frame + "' style='width:100%' >");
		if(skinList != null && skinList.size() > 0)
		{
			for (LfSkin sk : skinList)
			{
				String skcode = sk.getSkincode();
				String skinName = sk.getSkinname();
				if("?????????????????????".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_1",request);
				}else if("????????????".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_2",request);
				}else if("????????????".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_3",request);
				}else if("????????????".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_4",request);
				}else if("????????????".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_5",request);
				}else if("????????????".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_6",request);
				}else if("????????????".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_7",request);
				}else if("?????????????????????".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_8",request);
				}else if("Wood???".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_9",request);
				}else if("?????????".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_10",request);
				}else if("?????????".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_11",request);
				}else if("?????????".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_12",request);
				}
				if(!"?????????????????????".equals(skinName)&&!"?????????".equals(skinName)) {
                //???????????????????????????					
				sb.append("<div style='padding:5px;float:left'><div class='perSkin skin_").append(skcode).append("'  onclick='changeSkin2(\"").append(sk.getSkincode()).append("\",\"").append(frame).append("\")'>").append("<div class='skinPic' style='background:url(").append(request.getContextPath()).append("/frame/").append(frame).append("/skin/").append(skcode).append("/images/skinpreview.jpg) no-repeat;'></div>").append("<div class='uselb'>").append(skinName).append("</div></div></div>");
				}
			}
		}
		sb.append("</div>");
		writer.print(sb.toString());
		writer.flush();
		writer.close();
	}

	/**
	 * ??????????????????
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getTheme(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		//??????????????????
		List<LfTheme> themeList = baseBiz.getEntityList(LfTheme.class);
		String patch = request.getParameter("patch");
		StringBuffer sb = new StringBuffer();
		String userFrame = (String) request.getSession().getAttribute(StaticValue.EMP_WEB_FRAME);
		if(themeList != null && themeList.size() > 0)
		{
			int themeSize = themeList.size();
			int index = 0;
			for (LfTheme theme : themeList)
			{
				index++;
				String themecode = theme.getThemecode();
				sb.append("<span style='height:16px;float:left;line-height:16px;background:url(" + patch + "/img/icon_" + theme.getThemesrc() + (userFrame.equals(themecode) ? "_sel" : "") + ".png) left top no-repeat;text-indent:22px;cursor:pointer'" + " class='themeSpan " + (userFrame.equals(themecode) ? "fontsel" : "") + "' " + " themeCode='" + themecode + "' onclick='getThemeSkin(\"" + themecode + "\")'>" + theme.getThemename() + "</span>");
				if(index < themeSize)
				{
					sb.append("<span  style='height:14px;float:left;line-height:14px;border-left:0;border-top:0;border-bottom:0;width:10px;' class='div_bd'></span>");
					sb.append("<span  style='height:14px;float:left;width:10px;'></span>");
				}
			}
		}
		writer.print(sb.toString());
		writer.flush();
		writer.close();
	}

	/**
	 * ?????????????????????
	 * 
	 * @param request
	 * @param response
	 */
	public void getSysUserInfo(HttpServletRequest request, HttpServletResponse response){
		PrintWriter out = null;
		String result = "error";
		try
		{
			response.setContentType("text/html");
			out = response.getWriter();
			// ????????????id
			//String lguserid = request.getParameter("lguserid");
			//???????????? session????????????????????????
			String lguserid = SysuserUtil.strLguserid(request);

			if(lguserid == null || "".equals(lguserid.trim())){
				return;
			}
			Long userId = Long.parseLong(lguserid);

			LfSysuserVo sysuserVo = new LfSysuserVo();
			sysuserVo = new SysuserBiz().getSysuserVoByUserId(userId);
			result = sysuserVo.getUserName() + "&" + sysuserVo.getName() + "&" + sysuserVo.getSex() + "&" + sysuserVo.getMobile() + "&" + sysuserVo.getOph() + "&" + sysuserVo.getEMail() + "&" + sysuserVo.getQq();
		}
		catch (Exception e)
		{
			// ????????????
			EmpExecutionContext.error(e, "svt??????????????????????????????");
		}finally {
			if(out != null){
				out.print(result);
			}
		}
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		String opModule = StaticValue.USERS_MANAGER;
		String oppType = "";
		String opContent = "";
		SuperOpLog spLog = new SuperOpLog();
		HttpSession session = request.getSession();
		// ?????????
		String username = request.getParameter("username");
		SysuserBiz sysuserBiz = new SysuserBiz();
		// ??????
		String type = request.getParameter("type");
		Long lgguid = Long.parseLong(request.getParameter("lgguid"));
		LfSysuser sysuser = null;
		try
		{
			// ??????????????????
			sysuser = baseBiz.getByGuId(LfSysuser.class, lgguid);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "????????????????????????????????????????????????");
			writer.print("false");
		}
		String opUser = sysuser != null? sysuser.getUserName() : null;
		// (LfSysuser) session.getAttribute("loginSysuser");
		boolean result = false;

		if(type.equals("phone"))
		{
			// ??????????????????
			oppType = StaticValue.UPDATE;
			opContent = "???????????????" + username + "????????????";
			String userId = sysuser !=null? sysuser.getUserId().toString():"";
			// ??????
			String mobile = request.getParameter("mobile");
			// email
			String EMail = request.getParameter("EMail");
			// ??????
			String oph = request.getParameter("oph");
			// qq
			String qq = request.getParameter("qq");
			try
			{
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				objectMap.put("mobile", mobile);
				objectMap.put("EMail", EMail);
				objectMap.put("oph", oph);
				objectMap.put("qq", qq);

				// ??????id
				conditionMap.put("userId", userId);

				LfEmployee employee = baseBiz.getByGuId(LfEmployee.class, sysuser!=null? sysuser.getGuId():null);
				if(employee != null)
				{
					employee.setEmail(EMail);
					employee.setQq(qq);
					employee.setMobile(mobile);
					employee.setOph(oph);
					baseBiz.updateObj(employee);
				}
				result = baseBiz.update(LfSysuser.class, objectMap, conditionMap);
				session.setAttribute("loginSysuser", sysuser);
				response.getWriter().print(result);
			}
			catch (Exception e)
			{
				// ????????????
				EmpExecutionContext.error(e, "???????????????????????????");
				writer.print("false");
			}
			finally
			{
				if(result)
				{
					// ??????
					spLog.logSuccessString(opUser, opModule, oppType, opContent, sysuser == null ? "" :sysuser.getCorpCode());
				}
				else
				{
					spLog.logFailureString(opUser, opModule, oppType, opContent + StaticValue.OPSPER, null, sysuser !=null?sysuser.getCorpCode():null);
				}
				//??????????????????
				if(result){
					Object loginSysuserObj=request.getSession().getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("???????????????????????????????????????"+loginSysuser.getCorpCode()+"???"
						+"????????????"+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]???"
						+"??????????????????(????????????????????????"+mobile+")?????????");
					}
				}
			}
		}
		else
			if(type.equals("pass"))
			{
				// ????????????
				oppType = StaticValue.UPDATE;
				opContent = "???????????????" + username + "??????";
				String pass = request.getParameter("pass");
				String agoPass = request.getParameter("agoPass");
				if(sysuser==null){
					sysuser=new LfSysuser();
				}
				
				if(!MD5.getMD5Str(agoPass).equals(sysuser.getPassword()) && !MD5.getMD5Str(agoPass + sysuser.getUserName().toLowerCase()).equals(sysuser.getPassword()))
				{
					writer.print("mid");
				}
				else
				{
					try
					{	
						//?????????????????????????????????????????????
						
						result = sysuserBiz.modifyPassword(sysuser, pass);
						sysuser.setPassword(MD5.getMD5Str(pass + sysuser.getUserName().toLowerCase()));
						session.setAttribute("loginSysuser", sysuser);
						session.removeAttribute("NameAndPWord");
						response.getWriter().print(result);
					}
					catch (Exception e)
					{
						// spLog.logFailureString(opUser, opModule, oppType,
						// opContent+opSper, e,getCorpCode());
						EmpExecutionContext.error(e, "svt?????????????????????");
						writer.print("false");
					}
					finally
					{
						if(result)
						{
							spLog.logSuccessString(opUser, opModule, oppType, opContent, sysuser.getCorpCode());

						}
						else
						{
							spLog.logFailureString(opUser, opModule, oppType, opContent + StaticValue.OPSPER, null, sysuser.getCorpCode());
						}
						//??????????????????
						if(result){
							Object loginSysuserObj=request.getSession().getAttribute("loginSysuser");
							if(loginSysuserObj!=null){
								LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
								EmpExecutionContext.info("???????????????????????????????????????"+loginSysuser.getCorpCode()+"???"
								+"????????????"+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]???"
								+opContent+"?????????");
							}
						}
					}
				}
			}
	}

	/***
	 *  ??????????????????????????????????????????
	 *  ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????n???????????????????????????????????????????????????????????????
	 * @return
	 */
	public void  valid(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String result = "";
		PrintWriter writer = response.getWriter();
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LfCorp lfcorp=(LfCorp)request.getSession().getAttribute("loginCorp");
			conditionMap.put("corpCode", lfcorp.getCorpCode());
			String bit="0"; 
			String number="";
			String character ="";
			String sign="";
			String modiftime="";
			String remind="";
			String errorlimt="";
			List<LfCorpConf> lfCorpConfList= baseBiz.getByCondition(LfCorpConf.class, conditionMap, null);

			for (int i = 0; i < lfCorpConfList.size(); i++)
			{
				LfCorpConf conf = lfCorpConfList.get(i);
				// ??????

				if("pwd.count".equals(conf.getParamKey()))
				{
					bit = conf.getParamValue();
				}
				// ????????????
				if("pwd.combtype".equals(conf.getParamKey()))
				{
					String cunt = conf.getParamValue();
					if(cunt != null)
					{
						String[] per = cunt.split(",");
						if(per.length == 1)
						{
							number = per[0];
						}
						else if(per.length == 2)
						{
							number = per[0];
							character = per[1];
						}
						else if(per.length == 3)
						{
							number = per[0];
							character = per[1];
							sign = per[2];
						}
					}
				}
				// ????????????
				if("pwd.upcycle".equals(conf.getParamKey()))
				{
					modiftime = conf.getParamValue();
				}
				// ????????????
				if("pwd.pastalarm".equals(conf.getParamKey()))
				{
					remind = conf.getParamValue();
				}
				// ????????????
				if("pwd.errlimit".equals(conf.getParamKey()))
				{
					errorlimt = conf.getParamValue();
				}
			}
			result=bit+"@"+number+"@"+character+"@"+sign+"@"+modiftime+"@"+remind+"@"+errorlimt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "?????????????????????????????????");
		}
		finally
		{
			writer.print(result);
		}
	}
	
	
	
	/**
	 * ??????????????????
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void checkMobile(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		// ????????????
		String mobile = request.getParameter("mobile");
		// ?????????
		String separator = request.getParameter("separator");
		// ???????????????
		String spisuncm = request.getParameter("spisuncm");

		WgMsgConfigBiz mtBiz = new WgMsgConfigBiz();

		int result = 0;
		try
		{
			Pattern pa = Pattern.compile("( {2,})|\n|\r");
			Matcher m = pa.matcher(mobile);
			mobile = m.replaceAll("");
			String[] numbers = mobile.split(separator);
			String[] haoduan = mtBiz.getHaoduan();
			if(spisuncm == null)
			{
				for (int i = 0; i < numbers.length; i++)
				{
					if(!numbers[i].equals(""))
					{
						result = mtBiz.checkMobile(numbers[i], haoduan);
						if(result == 0)
						{
							break;
						}
					}
				}
			}
			else
			{
				for (int i = 0; i < numbers.length; i++)
				{
					if(!numbers[i].equals(""))
					{
						result = mtBiz.checkMobile(numbers[i], Integer.valueOf(spisuncm), haoduan);
						if(result == 0)
						{
							break;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			result = 2;
			EmpExecutionContext.error(e, "svt???????????????????????????");
		}
		finally
		{
			writer.print(result);
		}
	}
	
	/**
	 * ??????????????????????????????
	 * @description    
	 * @param request
	 * @param response
	 * @throws Exception       			 
	 * @author zhangmin
	 * @datetime 2014-4-16 ??????05:28:22
	 */
	public void checkMobileByInfo(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		// ????????????
		String mobile = request.getParameter("mobile");

		WgMsgConfigBiz mtBiz = new WgMsgConfigBiz();
		
		PhoneUtil phoneUtil = new PhoneUtil();

		int result = 3;
		try
		{
			String[] haoduan = mtBiz.getHaoduan();
			result = phoneUtil.getPhoneType(mobile, haoduan);
			if(result == -1)
			{
				result=0;
			}
			else if(result==0)
			{
				result = 3;
			}else if(result==2){
				result = 4;
			}
		}
		catch (Exception e)
		{
			result = 2;
			EmpExecutionContext.error(e, "svt???????????????????????????");
		}
		finally
		{
			writer.print(result);
		}
	}

	/**
	 * ????????????
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void checkPass(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		HttpSession session = request.getSession();
		// ?????????
		String agoPass = request.getParameter("agoPass");

		try
		{
			// ?????????????????????????????????
			LfSysuser sysuser = (LfSysuser) session.getAttribute("loginSysuser");

			// ????????????
			if(MD5.getMD5Str(agoPass).equals(sysuser.getPassword()) || MD5.getMD5Str(agoPass + sysuser.getUserName().toLowerCase()).equals(sysuser.getPassword()))
			{

				writer.print("true");
			}
			else
			{
				writer.print("false");
			}

		}
		catch (Exception e)
		{
			// ????????????
			EmpExecutionContext.error(e, "svt?????????????????????");
		}

	}

	/**
	 * ????????????????????????
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getCt(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		BalanceLogBiz biz = new BalanceLogBiz();

		PrintWriter out = response.getWriter();
		Long maxcount = null;
		Long maxMmsConunt = null;
		// ????????????
		String result = "";
		//String id = request.getParameter("lguserid");
		//???????????? session????????????????????????
		String id = SysuserUtil.strLguserid(request);


		long userId;
		// ????????????id
		if(id == null || "".equals(id) || "0".equals(id))
		{
			EmpExecutionContext.error("svt??????????????????ID??????");
			userId = ((LfSysuser) request.getSession().getAttribute("loginSysuser")).getUserId();
		}
		else
		{
			userId = Long.parseLong(id);
		}
		LfSysuser syuser = null;
		try
		{
			syuser = baseBiz.getLfSysuserByUserId(userId);
			if(syuser == null){
				EmpExecutionContext.error("????????????????????????????????????????????????");
				return;
			}
			if(!biz.IsChargings(syuser.getUserId()))
			{
				out.print("nojifei");
			}
			else
			{
				maxcount = biz.getAllowSmsAmount(syuser);
				maxMmsConunt = biz.getAllowMmsAmount(syuser);
				if(maxcount == null && maxMmsConunt == null)
				{
					result = "^";
				}
                maxcount = (maxcount == null?0:maxcount);
                maxMmsConunt = (maxMmsConunt == null?0:maxMmsConunt);
				out.print(result + "ye" + maxcount + "," + maxMmsConunt);
			}
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "svt?????????????????????????????????");
		}
	}

	/**
	 * frame2.5?????????????????????
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void toChangeSkin(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//String lguserid = request.getParameter("lguserid");
		//???????????? session????????????????????????
		String lguserid = SysuserUtil.strLguserid(request);

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		conditionMap.put("userid", lguserid);
		//??????????????????????????????????????????
		List<LfUser2skin> userskinList = baseBiz.getByCondition(LfUser2skin.class, conditionMap, null);
		LfUser2skin u2S = null;
		if((userskinList != null) && (userskinList.size() > 0))
		{
			u2S = (LfUser2skin) userskinList.get(0);
		}
		//??????????????????
		List<LfTheme> themeList = baseBiz.getEntityList(LfTheme.class);
		//??????????????????
		List<LfSkin> skinList = baseBiz.getEntityList(LfSkin.class);
		//????????????????????????Map<key-?????????value-????????????>
		Map<String, List<LfSkin>> themeSkinMap = new HashMap<String, List<LfSkin>>();
		if(themeList != null && themeList.size() > 0)
		{
			for (LfTheme theme : themeList)
			{
				themeSkinMap.put(theme.getThemecode(), new ArrayList<LfSkin>());
			}
		}
		if(skinList != null && skinList.size() > 0)
		{
			for (LfSkin skin : skinList)
			{
				if(themeSkinMap.get(skin.getThemecode()) != null)
				{
					themeSkinMap.get(skin.getThemecode()).add(skin);
				}
			}
		}
		request.setAttribute("themeList", themeList);
		request.setAttribute("themeSkinMap", themeSkinMap);
		request.setAttribute("u2S", u2S);
		request.getRequestDispatcher("/frame/" + request.getSession().getAttribute(StaticValue.EMP_WEB_FRAME) + "/skin.jsp").forward(request, response);
	}

	/**
	 * ????????????????????????
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void showAbout(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AProInfo proInfo = null;
		Map<String,String[]> versions = new HashMap<String, String[]>();
        List<LfChangeLog> changeLogs = new ArrayList<LfChangeLog>();
		String dbVersion = null;
		try
		{
			// ???????????????????????????
			List<AProInfo> proList = baseBiz.getByCondition(AProInfo.class, null, null);
			if(proList != null && proList.size() > 0)
			{
				proInfo = proList.get(0);
			}
			//?????????????????????
			//processid 1000:EMP-WEB,2000:E??????WBS,3000:??????SPGATE
//			String processIds = "1000,2000,3000";
			versions = frameBiz.getVersionInfos(null);

			//???????????????????????????
			dbVersion = frameBiz.getDbVersion();

            Map<String,String> changeLogMaps = getChangeLogs();
            List<DynaBean> changes = frameBiz.getVersionHis();
            LfChangeLog changeLog = null;
            if(changes != null)
            {
                String ver = null;
                String updatetime = null;
                String content = null;
                for (DynaBean change : changes) {
                    ver = change.get("version").toString();
                    updatetime = change.get("updatetime").toString();
//                    if(changeLogMaps.containsKey(ver))
//                    {
                        content = change.get("versioninfo").toString();//changeLogMaps.get(ver);
                        if(content!=null&&!"".equals(content.trim())){
	                        changeLog = new LfChangeLog();
	                        changeLog.setReleaseNote(content);
	                        changeLog.setReleasetime(updatetime.substring(0,10));
	                        changeLog.setMajorversion("V"+ver);
	                        changeLogs.add(changeLog);
                        }
//                    }
                }
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "?????????????????????????????????");
		}
		finally
		{
			request.setAttribute("proInfo", proInfo);
			request.setAttribute("versions",versions);
			request.setAttribute("dbVersion",dbVersion);
			request.setAttribute("changeLogs",changeLogs);
			request.getRequestDispatcher("/common/aboutEmp.jsp").forward(request, response);
		}

	}

    public Map<String,String> getChangeLogs(){
        Map<String,String> changeLogs = new HashMap<String, String>();
        String webRoot = new TxtFileUtil().getWebRoot();
        //????????????
        File file = new File(webRoot,"common/file/changelog.xml");
        SAXReader reader = new SAXReader();
        Document document = null;
        try
        {
            document = reader.read(new FileInputStream(file));
            // ???????????????
            Element root = document.getRootElement();
            //????????????
            List<Element> rootElementList = root.elements();
            String ver = null;
            StringBuffer stringBuffer = new StringBuffer();
            List<Element> lis = null;
            for (Element version : rootElementList)
            {
                ver = version.attributeValue("ver");
                lis = version.elements();
                for (int i = 0; i < lis.size(); i++) {
                    stringBuffer.append((i+1)+"???"+lis.get(i).getTextTrim());
                    stringBuffer.append("<br/>");
                }
                changeLogs.put(ver,stringBuffer.toString());
                stringBuffer.setLength(0);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"?????????????????????????????????");
        }

        return changeLogs;
    }

    /**
     *??????????????????????????? ????????????EMP????????????
     * @param request
     * @param response
     * @throws IOException
     */
    public void notice(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String url = request.getParameter("url");
        String result = "error";
        if(url != null)
        {
            result = new CommonBiz().uploadFileToOtherNodes(url);
        }
        OutputStream os = response.getOutputStream();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/octet-stream");
        response.setContentLength(result.getBytes().length);
        os.write(result.getBytes());
        os.close();
    }

    /**
     * ???????????????????????????
     * @param request
     * @param response
     */
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getParameter("url");
        String result = new CommonBiz().downloadFileFromFileCenter(url);
        OutputStream os = response.getOutputStream();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/octet-stream");
        response.setContentLength(result.getBytes().length);
        os.write(result.getBytes());
        os.close();
    }

    public void checkTkn(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
    	// ????????????
    	String requestUrl = request.getServletPath();
		EmpExecutionContext.info("tkn????????????"+"url:"+requestUrl);
		response.getWriter().print("true");
	}
    
    
    /**
     * ????????????
     *@anthor qiyin<15112605627@163.com>
     *@param request
     *@param response
     *@throws Exception
     */
    public void refreshLanguage(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	String temp="";
    	try
		{
//			LfSysuser user = (LfSysuser) request.getSession().getAttribute("loginSysuser");
//			MessageUtils.extractMessage("comm", "", this.getp)
			temp = " <ul id=\"uChildren\" class=\"\">";
			List<String> langs = (List)request.getSession().getAttribute("multiLanguage");
			if(null != langs && !langs.isEmpty()){
				for (String str : langs) {
					String langName = "zh_HK".equals(str)?"English":"zh_TW".equals(str)?"??????":"??????";
					temp += "<li><a href=\"#\" onclick=\"javascript:changeLanguage('"+str+"')\">"+langName+"</a></li>";
				}
			}
			temp += "</ul>";
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"??????????????????");
		}finally{
			response.getWriter().print(temp);
		}
    }
    
    /**
     * ????????????
     *@anthor qiyin<15112605627@163.com>
     *@param request
     *@param response
     *@throws Exception
     */
    public void refreshPersonSet(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	String temp="";
    	try
    	{
    		LfSysuser user = (LfSysuser) request.getSession().getAttribute("loginSysuser");
    		String common_personalSetting = MessageUtils.extractMessage("common", "common_personalSetting", request);
    		String common_helpManual = MessageUtils.extractMessage("common", "common_helpManual", request);
    		String common_aboutPlatform = MessageUtils.extractMessage("common", "common_aboutPlatform", request);
    		temp = " <ul class=\"uChildren\" id=\"\">";
    		temp+="<li><a href=\"#\" onclick=\"doUpdatePass()\">"+common_personalSetting+"</a></li>";
   			temp+="<li><a href=\"#\" onclick=\"parent.frames['topFrame'].window.upLoad()\">"+common_helpManual +"</a></li>";
   			temp+="<li><a href=\"#\" onclick=\"showAbout()\">"+common_aboutPlatform+"</a></li>";
   			temp+="</ul>";
   		
    	} catch (Exception e)
    	{
    		EmpExecutionContext.error(e,"??????????????????");
    	}finally{
    		response.getWriter().print(temp);
    	}
    }
    
    /**
     * ??????????????????
     *@anthor qiyin<15112605627@163.com>
     *@param request
     *@param response
     *@throws Exception
     */
    public void refreshBalance(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	String temp="";
    	try
    	{
    		LfSysuser user = (LfSysuser) request.getSession().getAttribute("loginSysuser");
    		String common_frame2_main_8 = MessageUtils.extractMessage("common", "common_frame2_main_8", request);
    		String common_frame2_main_9 = MessageUtils.extractMessage("common", "common_frame2_main_9", request);
    		String common_frame2_main_10 = MessageUtils.extractMessage("common", "common_frame2_main_10", request);
    		
    		String corpcode=user.getCorpCode();
    		//????????????????????????
    		boolean isCharging = SystemGlobals.isDepBilling((corpcode == null || "".equals(corpcode.trim())) ? "100001": corpcode);
    		Map<String, String> btnMap = (Map<String, String>) request.getSession().getAttribute("btnMap");
			//???????????????????????????
			boolean isShowYe = btnMap.get(ViewParams.YECODE+"-0") == null ? false : true;
    		temp+="<ul class=\"uChildren\" id=\"\">";
      		if (isCharging) 
              {
      			temp+="	<li id=\"smsYe\"><a href=\"#\" >"+common_frame2_main_8 +"???<span id=\"ctSms\" style=\"color:red;\">0</span></a></li>";
      			temp+="<li id=\"mmsYe\"><a href=\"#\" >"+common_frame2_main_9 +"???<span id=\"ctMms\" style=\"color:red\">0</span></a></li>";
      		} 
      		if(isShowYe){ 
      			temp+="	<li><a href=\"#\" id=\"checkfee\" onclick=\"parent.frames['topFrame'].window.checkFee()\">"+common_frame2_main_10 +"</a></li>";
    		} 
      		temp+="</ul>";
    		
    	} catch (Exception e)
    	{
    		EmpExecutionContext.error(e,"??????????????????");
    	}finally{
    		response.getWriter().print(temp);
    	}
    }
    
    /**
	 * ????????????
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void setLangeuage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//???????????????1????????????2????????????3?????????
		String lang = request.getParameter("lang");
		//?????????id
		String userid = request.getParameter("userid");
		String result="false";
		PrintWriter writer = response.getWriter();
		try
		{
			String value = StaticValue.ZH_CN;
			if (StaticValue.ZH_CN.equals(lang))
			{
				value =StaticValue.ZH_CN;
			} else if (StaticValue.ZH_TW.equals(lang))
			{
				value = StaticValue.ZH_TW;
			} else if (StaticValue.ZH_HK.equals(lang))
			{
				value = StaticValue.ZH_HK;
			}
			if(null==userid){
				Cookie cookie=new Cookie(StaticValue.LANG_KEY, value);
				cookie.setMaxAge(365*24*60*60);
				response.addCookie(cookie);
				request.getSession().setAttribute(StaticValue.LANG_KEY, value);
				result="true";
			}else{
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				objectMap.put("languageCode", value);
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("userId", userid);
				BaseBiz baseBiz = new BaseBiz();
				Boolean flag = baseBiz.update(LfSysuser.class, objectMap, conditionMap);
				if (flag)
				{
					//??????session???languageMap
					HttpSession session= request.getSession();
//					LfSysuser user = (LfSysuser) session.getAttribute("loginSysuser");
//					user.setLanguageCode(value);
//					chooseLanguage(user);
					Cookie cookie=new Cookie(StaticValue.LANG_KEY, value);
					cookie.setMaxAge(365*24*60*60);
					response.addCookie(cookie);
					request.getSession().setAttribute(StaticValue.LANG_KEY, value);
					
					//????????????
					/*List<LfPrivilege> prList = biz.getMenuByPrivCodeAsc(userid,request);
					request.getSession().setAttribute("prList", prList);
					
					// ??????????????????????????????
				    Map<String, String> titleMap = biz.getTitleMap(user.getUserId().toString(),request);
				    request.getSession().setAttribute("titleMap", titleMap);
				  
				    Map<String, String> btnMap = biz.getBtnFuntionMap(user.getUserId().toString(),request);
				    request.getSession().setAttribute("btnMap", btnMap);
				    Map<String, List<LfPrivilege>> priMap = new HashMap<String, List<LfPrivilege>>();
					for (LfPrivilege privi : prList)
					{
						// ?????????????????????
						if (StaticValue.inniMenuMap.get(privi.getMenuSite()) == null)
						{
							continue;
						}
						String resourceId = privi.getResourceId().toString();
						if (priMap.get(resourceId) != null)
						{
							priMap.get(resourceId).add(privi);
						} else
						{
							List<LfPrivilege> pri = new ArrayList<LfPrivilege>();
							pri.add(privi);
							priMap.put(resourceId, pri);
						}
					}
					
					request.getSession().setAttribute("priMap", priMap);*/
					result="true";
				}
				//????????????
				//		request.getSession().setAttribute(StaticValue.EMP_WEB_FRAME, themeCode);
				//		request.getSession().setAttribute("stlyeSkin", request.getContextPath() + "/frame/" + themeCode + "/skin/" + skin);
			}
		} catch (Exception e)
		{
			// ????????????
			EmpExecutionContext.error(e, "??????????????????");
		}finally{
			writer.print(result);
			writer.flush();
			writer.close();
		}
	}
}
