package com.montnets.emp.menuParam.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.email.Aes;
import com.montnets.emp.email.MyAuthenticator;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfReviewSwitch;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailmanage.GwMsgtail;
import com.montnets.emp.entity.tailmanage.GwTailctrl;
import com.montnets.emp.entity.tailmanage.GwTdcmd;
import com.montnets.emp.menuParam.biz.MenuParamConfgBiz;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Session;
import javax.mail.Transport;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 模块参数配置
 * 
 * @project 标准版&托管版
 * @author zhihanking@163.com <zhihanking@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2012-8-03 下午05:02:59
 * @description 模块参数配置控制器Servlet
 */
@SuppressWarnings("serial")
public class mep_menuParamConfigSvt extends BaseServlet
{

	

	private final  String	empRoot		= "xtgl";

	private final String	basePath	= "/menuParam";

	private final BaseBiz			baseBiz		= new BaseBiz();
	
	private final CommonBiz       commonBiz   = new CommonBiz();
	

	/**
	 * 加载模块参数配置项
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		List<LfReviewSwitch> swirchList = null;
		List<LfCorpConf> corpConf = null;
		LfGlobalVariable globaV = null;
		
		List<GwTailctrl> tailctrlList = null;
		GwTailctrl tailctrl = null;
		List<GwMsgtail> msgtailList = null;
		GwMsgtail msgtail = null;
		
		String isEditor = "0"; //默认否  0 否,1 是
		String isShow = "0"; //默认不显示, 0不显示，1显示
		int isOpen = 0; //默认不显示, 是否开启上行回复退订，手机号自动添加黑名单功能 (0不启用、1启用)。
		String cmdstr="";
		try
		{
			String corpCode = request.getParameter("lgcorpcode");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", corpCode);
			// 获取企业对应的审核开关配置信息
			swirchList = baseBiz.getByCondition(LfReviewSwitch.class, conditionMap, null);
			
			corpConf = baseBiz.getByCondition(LfCorpConf.class, conditionMap, null);
			
			List<LfCorp> corpTempList = baseBiz.getByCondition(LfCorp.class, conditionMap, null);
			if(corpTempList != null && corpTempList.size() > 0)
			{
				LfCorp corp = corpTempList.get(0);
				isOpen=corp.getIsOpenTD();
				
			}
			
			//密码解密
			for(LfCorpConf lf : corpConf){
				if("email.password".equals(lf.getParamKey())){
					lf.setParamValue(Aes.deString(lf.getParamValue()));
				}
			}
			
			//日志保留时间
			globaV = commonBiz.getGlobalVariable("LOGRETENTIONTIME");
			
			/**EMP标准版6.1 新需求：贴尾全局配置  pengj*/
			// 获取企业对应的贴尾全局配置信息 
			tailctrlList = new MenuParamConfgBiz().getTailctrlList(corpCode);
			
			if(tailctrlList != null && tailctrlList.size() > 0)
			{
				tailctrl = tailctrlList.get(0);
			}
			
			//获得全局贴尾管理对象
			msgtailList = new MenuParamConfgBiz().getMsgtailList(corpCode);
			if(msgtailList != null && msgtailList.size() > 0)
			{
				msgtail = msgtailList.get(0);
			}
			/**end*/
			
			/**签名同步开关*/
			LfGlobalVariable globavActsyncflag = commonBiz.getGlobalVariable("ACTSYNCFLAG");
			request.setAttribute("actsyncflag", globavActsyncflag.getGlobalValue().toString());
			/**end*/
			
			//判断是单企业还是多企业,只有单企业才显示是否可编辑
			if(StaticValue.getCORPTYPE() == 0)
			{
				isShow = "1"; //是否显示
				//是否可编辑
				LfGlobalVariable glVariable = commonBiz.getGlobalVariable("TMPEDITORFLAG");
				isEditor = glVariable.getGlobalValue().toString();
			}
			conditionMap.clear();
			//上行指令加黑名单 显示
//			conditionMap.put("gwType", "4000");
//			conditionMap.put("paramItem", "FILTERB01TDMO");
//			List<AgwParamValue> agw=new BaseBiz().getByCondition(AgwParamValue.class, conditionMap, null);
//			if(agw!=null&&agw.size()>0){
//				String va=agw.get(0).getParamValue();
//				if(va!=null&&!"".equals(va)){
//					isOpen=va;
//				}
//			}
			
			conditionMap.clear();
			conditionMap.put("pbcropcode", corpCode);
			
			//上行加黑名单指令显示
			List<GwTdcmd> cmd=new BaseBiz().getByCondition(GwTdcmd.class, conditionMap, null);
			if(cmd!=null&&cmd.size()>0){
				for(int k=0;k<cmd.size();k++){
					GwTdcmd cm=cmd.get(k);
					if(k==0){
						cmdstr=cmdstr+cm.getTdcmd();
					}else{
						cmdstr=cmdstr+","+cm.getTdcmd();
					}
					
				}
			}
			
			
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询审核开关异常！");
		}
		finally
		{
			request.setAttribute("swirchList", swirchList);
			request.setAttribute("tailctrl", tailctrl);
			request.setAttribute("msgtail", msgtail);
			request.setAttribute("corpConf", corpConf);
			request.setAttribute("globaV", globaV);
			request.setAttribute("isShow",isShow);
			request.setAttribute("isEditor",isEditor);
			request.setAttribute("isOpen",isOpen);
			request.setAttribute("cmdstr",cmdstr);
			request.getRequestDispatcher(this.empRoot + basePath + "/mep_menuParamConfig.jsp").forward(request, response);
		}
	}

	/**
	 * 修改审核配置
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void change(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String lgcorpcode = request.getParameter("lgcorpcode");
			String params = request.getParameter("params");
			String len = request.getParameter("len");
			// 机构计费
			String isBalance = request.getParameter("isBalance");
			Map<String, List<LfPrivilege>> priMap = (Map<String, List<LfPrivilege>>) request.getSession(false).getAttribute("priMap");
			Map<String, String> titleMap = (Map<String, String>) request.getSession(false).getAttribute("titleMap");
			Map<String,String> btnMap = (Map<String,String>)request.getSession(false).getAttribute("btnMap");
			if("0".equals(isBalance)){
				//关闭机构计费则隐藏充值/回收管理菜单项
				for(LfPrivilege lfPrivilege : priMap.get("10")){
					if("充值/回收管理".equals(lfPrivilege.getMenuName())){
						priMap.get("10").remove(lfPrivilege);
						break;
					}
				}
			}else {
				boolean flag = false;
				LfPrivilege lfPrivilege = new LfPrivilege();
				lfPrivilege.setResourceId(10L);
				lfPrivilege.setMenuName("充值/回收管理");
				lfPrivilege.setModName("操作员管理");
				lfPrivilege.setMenuCode("1600-1320");
				lfPrivilege.setMenuSite("/cha_balanceMgr.htm");
				lfPrivilege.setZhHkMenuName("Recharge/Retrieve management");
				lfPrivilege.setZhTwMenuName("充值/回收管理");
				lfPrivilege.setZhHkModName("Operator");
				lfPrivilege.setZhTwModName("操作員管理");
				for(LfPrivilege privilege : priMap.get("10")){
					if("充值/回收管理".equals(privilege.getMenuName())){
						flag = true;
						break;
					}
				}
				if(!flag){
					priMap.get("10").add(3, lfPrivilege);
					if(!titleMap.containsKey("balanceMgr")){
						titleMap.put("balanceMgr", "1600-1320");
						request.getSession(false).setAttribute("titleMap", titleMap);
					}
					if(!btnMap.containsKey("1600-1320-0")){
						btnMap.put("1600-1320-0", "查看");
						request.getSession(false).setAttribute("btnMap", btnMap);
					}
				}
			}
			request.getSession(false).setAttribute("priMap", priMap);

			/**EMP标准版6.1 新需求：贴尾全局配置  pengj*/
			String overtailflag = request.getParameter("overtailflag");
			String content = request.getParameter("content");
			String othertailflag = request.getParameter("othertailflag");
			//当前登录操作员
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//签名同步开关
			String actsyncflag=request.getParameter("actsyncflag");
			
			//所有传入后台的数据都用该对象传递
			LinkedHashMap<String, String> dataCondition = new LinkedHashMap<String, String>();
			
			/**end*/
			
			if(params == null)
			{
				response.getWriter().print("false");
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 根据企业编码获取企业对象
			conditionMap.put("corpCode", lgcorpcode);
			List<LfCorp> corpTempList = baseBiz.getByCondition(LfCorp.class, conditionMap, null);
			LfCorp corp = null;
			if(corpTempList != null && corpTempList.size() > 0)
			{
				corp = corpTempList.get(0);
			}
			else
			{
				throw new Exception("获取企业对象失败！");
			}
			
			
			
			
			/**EMP标准版6.1 新需求：贴尾全局配置  pengj*/
			//根据企业编码获取贴尾控制管理对象
			List<GwTailctrl> tailctrlList = new MenuParamConfgBiz().getTailctrlList(lgcorpcode);
			GwTailctrl tailctrl = null;
			if(tailctrlList != null && tailctrlList.size() > 0)
			{
				tailctrl = tailctrlList.get(0);
			}
			int oldOthertailflag = tailctrl!=null?tailctrl.getOthertailflag():0;
			int oldOvertailflag = tailctrl!=null?tailctrl.getOvertailflag():0;
			String updateTailctrlFlag = "0";
			//如果没有修改就不更新表，修改了才更新表
			if(oldOthertailflag != Integer.valueOf(othertailflag) || oldOvertailflag != Integer.valueOf(overtailflag)){
				tailctrl.setOvertailflag(Integer.valueOf(overtailflag));
				tailctrl.setOthertailflag(Integer.valueOf(othertailflag));
				//tailctrl.setCreatetime(new Timestamp(System.currentTimeMillis()));
				tailctrl.setUpdatetime(new Timestamp(System.currentTimeMillis()));
				//tailctrl.setUserid(Long.parseLong(lguserid));
				updateTailctrlFlag = "1";
				//boolean result = baseBiz.updateObj(tailctrl);
				//response.getWriter().print(result);
			}
			
			
			
			//获得全局贴尾管理对象，用于修改全局贴尾模板
			List<GwMsgtail> msgtailList = new MenuParamConfgBiz().getMsgtailList(lgcorpcode);
			GwMsgtail msgtail = null;
			if(msgtailList != null && msgtailList.size() > 0)
			{
				msgtail = msgtailList.get(0);
			}
			String oldContent = msgtail!=null?msgtail.getContent():"";
			String updateMsgtailFlag = "0";
			//如果没有修改就不更新表，修改了才更新表
			if(content!=null&&!content.equals(oldContent) && msgtail!=null){
				msgtail.setContent(content);
				//msgtail.setCreatetime(new Timestamp(System.currentTimeMillis()));
				msgtail.setUpdatetime(new Timestamp(System.currentTimeMillis()));
				//msgtail.setUserid(Long.parseLong(lguserid));
				//msgtail.setCorpcode(lgcorpcode);
				updateMsgtailFlag = "1";
				//boolean result1 = baseBiz.updateObj(msgtail);
				//response.getWriter().print(result1);
			}
			
			dataCondition.put("updateTailctrlFlag", updateTailctrlFlag);
			dataCondition.put("updateMsgtailFlag", updateMsgtailFlag);
			/**end*/
			
			//邮件参数
			String protocol = request.getParameter("protocol");
			String host = request.getParameter("host");
			String port = request.getParameter("port");
			String username = request.getParameter("username");
			String name = request.getParameter("name");
			String password = request.getParameter("password");	
			//密码加密
			String secretPassword = Aes.enString(password);
			dataCondition.put("email.protocol", protocol);
			dataCondition.put("email.host", host);
			dataCondition.put("email.port", port);
			dataCondition.put("email.username", username);
			dataCondition.put("email.name", name);
			dataCondition.put("email.password", secretPassword);
			dataCondition.put("lgcorpcode", lgcorpcode);
			
			//传入后台的参数与条件
			dataCondition.put("actsyncflag", actsyncflag);
			// 设置尾号最大长度
			corp.setSubnoDigit(Integer.valueOf(len));
			// 设置机构计费
			corp.setIsBalance(Integer.valueOf(isBalance));
			
			//上行指令加黑名单
			String sendcmdflag=request.getParameter("sendcmdflag");
			dataCondition.put("sendcmdflag", sendcmdflag);
			//上行加黑名单指令
			String sendcmdcontent=request.getParameter("sendcmdcontent");
			dataCondition.put("sendcmdcontent", sendcmdcontent);

			boolean isSuccess = new MenuParamConfgBiz().change(corp, params, msgtail, tailctrl, dataCondition);
			if(isSuccess)
			{
				// 保存成功之后 为了修改密码时候，更新密码显示
				String text = valid(request);
				request.getSession(false).setAttribute("loginCorp", corp);

				request.getSession(false).setAttribute("passWordLimt", text);
				// 此处增加text值是为了同步更新密码显示的限制条件
				response.getWriter().print("true;" + text);
			}
			else
			{
				response.getWriter().print("error");
			}
			
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "配置模块参数"+(isSuccess==true?"成功":"失败")+"。[参数值，拓展尾号位数，机构计费开关]" +
						"("+params+"，"+len+"，"+isBalance+")";
				EmpExecutionContext.info("模块参数配置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取企业对象失败！");
			response.getWriter().print("error");
		}
	}

	/***
	 * 用于校验密码是否符合一个规则
	 * 位数要求、组合形式，初始化密码不受此条件约束，修改密码受此限制，当不满足条件时，提示：“密码设置不得少于n位，且至少包含数字、字母、符号”，
	 * 保存失败
	 *
	 * @return
	 */
	public String valid(HttpServletRequest request) throws Exception
	{
		String text = "";
		try
		{
			String bit = "0";
			BaseBiz baseBiz = new BaseBiz();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LfCorp lfcorp = (LfCorp) request.getSession(false).getAttribute("loginCorp");
			if(lfcorp == null){return text;}
			conditionMap.put("corpCode", lfcorp.getCorpCode());
			String number = "";
			String character = "";
			String sign = "";
			List<LfCorpConf> lfCorpConfList = baseBiz.getByCondition(LfCorpConf.class, conditionMap, null);

			for (int i = 0; i < lfCorpConfList.size(); i++)
			{
				LfCorpConf conf = lfCorpConfList.get(i);
				// 位数
				if("pwd.count".equals(conf.getParamKey()))
				{
					bit = conf.getParamValue();
				}
				// 组合形式
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
			}
			String temp = "";
			if(!"".equals(number) && number != null && number != "null")
			{
				temp = "数字、" + temp;
			}
			if(!"".equals(character) && character != null && character != "null")
			{
				temp = "字母、" + temp;
			}
			if(!"".equals(sign) && sign != null && sign != "null")
			{
				temp = "符号、" + temp;
			}

			if(!"".equals(bit) && !"0".equals(bit) && bit != null)
			{
				if(!"".equals(temp))
				{
					text = "密码" + bit + "位以上且含";
				}
				else
				{
					text = "密码" + bit + "位以上";
				}
			}
			else
			{
				if(!"".equals(temp))
				{
					text = "密码包含";
				}
			}
			if(!"".equals(temp))
			{
				temp = temp.substring(0, temp.length() - 1);
				text = text + temp;
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询密码设置规则异常！");
		}
		return text;

	}

	/**
	 * 修改拓展尾号长度
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void changeLen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			// 长度
			String len = request.getParameter("len");
			// 机构计费
			String isBalance = request.getParameter("isBalance");
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 根据企业编码获取企业对象
			conditionMap.put("corpCode", lgcorpcode);
			List<LfCorp> corpTempList = baseBiz.getByCondition(LfCorp.class, conditionMap, null);
			LfCorp corp = null;
			if(corpTempList != null && corpTempList.size() > 0)
			{
				corp = corpTempList.get(0);
			}
			else
			{
				throw new Exception("获取企业对象失败！");
			}

			// LfCorp corp = (LfCorp)
			// request.getSession().getAttribute("loginCorp");
			// 设置尾号最大长度
			corp.setSubnoDigit(Integer.valueOf(len));
			// 设置机构计费
			corp.setIsBalance(Integer.valueOf(isBalance));

			if(baseBiz.updateObj(corp))
			{	
				request.getSession(false).setAttribute("loginCorp", corp);
				response.getWriter().print("true");
			}
			else
			{
				response.getWriter().print("error");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "修改拓展尾号长度异常！");
			response.getWriter().print("error");
		}
	}
	/**
	 * 向邮件服务器验证邮箱身份是否合法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void emailAuthenticator(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//邮件参数
		String protocol = request.getParameter("protocol");
		String host = request.getParameter("host");
		String port = request.getParameter("port");
		String username = request.getParameter("username");
		String password = request.getParameter("password");	
		// 初始化连接邮件服务器的会话信息
		Properties props  = new Properties();
		props.setProperty("mail.transport.protocol", protocol);
		props.setProperty("mail.smtp.host", host);
		props.setProperty("mail.smtp.port", port);
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.debug", "false");
		if(port.equals("465")){
			MailSSLSocketFactory sf;
			try {
				sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);  
				props.put("mail.smtp.ssl.enable", "true");  
				props.put("mail.smtp.ssl.socketFactory", sf);  
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				EmpExecutionContext.error(e, "邮件服务器ssl验证失败");
			}  
		}else {
			props.put("mail.smtp.ssl.enable", "false");  
		}
		
		// 向邮件服务器验证账号密码
		MyAuthenticator authenticator = new MyAuthenticator(username,
				password);
		Session session = Session.getInstance(props, authenticator);
		try {
			Transport ts = session.getTransport();
	        ts.connect();
	        response.getWriter().print("true");
		}catch(javax.mail.AuthenticationFailedException e) {
			EmpExecutionContext.error(e, "邮件账号身份验证失败,请检查账号密码是否有误，或者邮件服务器与端口号不匹配");
			if(e.getMessage().indexOf("535 Error")!=-1){
				response.getWriter().print("code");
			}else{
				response.getWriter().print("false");
			}
		}
	}
}
