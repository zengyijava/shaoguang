package com.montnets.emp.datasource.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.LoginBiz;
import com.montnets.emp.common.constant.LoginInfo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.gateway.AProInfo;
import com.montnets.emp.entity.securectrl.LfDynPhoneWord;
import com.montnets.emp.entity.securectrl.LfMacIp;
import com.montnets.emp.entity.system.LfUser2skin;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.MD5;
import com.montnets.emp.util.SuperOpLog;

public class singleSignOnSvt  extends BaseServlet
{

	/**  
	* @Title: 单点登录处理类
	* @Package com.montnets.emp.datasource.servlet
	* @Description: TODO( 用于单点登录 )
	* @author A18ccms A18ccms_gmail_com  
	* @date 2013-11-26 下午04:48:52
	* @version V1.0  
	*/
	

	//操作模块
	public final String auditModule= "登录";
	//操作用户
	final String opSper = StaticValue.OPSPER;
    private final SuperOpLog spLog=new SuperOpLog();
	private final String LOGO_PATH="file/logo/";
	
	/**
	 * 单点登录主要的进入方法
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{	
		//操作类型
	    String opType = StaticValue.OTHER;
	    //操作内容
		String opContent =null;
		//编码
		response.setContentType("text/html;charset=utf-8");
		HttpSession session = request.getSession();
//		PrintWriter out = response.getWriter();

		BaseBiz baseBiz = new BaseBiz();
		LoginBiz biz = new LoginBiz();

		//获取请求路径 
		String requestUrl = request.getServletPath()
				+ (request.getPathInfo() == null ? "" : request.getPathInfo());
		requestUrl = requestUrl.substring(requestUrl.lastIndexOf("/") + 1);

		//用户登录操作
        //获取用户登录信息 
    	//企业编码
		String enpCode = request.getParameter("enpCode"); 
		//如果不传入公司，则设置默认值

		//登录账号
		String username = request.getParameter("username"); 
		//登录密码
		String pass = request.getParameter("pass");
//		AuthenAtom authen=new AuthenAtom();
//		username=authen.parseCode(username);
//		//********由于是加密所以需要解析
//		pass=authen.parseCode(pass);
		//验证码	
		String tkn = session.getId() + String.valueOf(System.currentTimeMillis());
		String passOverDate="";
		boolean result = false;
		try
		{


			//登录用户
			LfSysuser sysuser = null;
            //获取企业编码
//			String corpcode = request.getParameter("enpCode");

			if(enpCode==null||enpCode.length()<=0)
			{
				//如果是单企业的登录的页面上的企业名称取lf_dep表中为1的那条记录的企业名称.
				LinkedHashMap<String, String> conditionMapbydep = new LinkedHashMap<String, String>();
				conditionMapbydep.put("depId", "1");
				List<LfDep>  depList = baseBiz.getByCondition(LfDep.class, conditionMapbydep, null);
				if(depList!=null && depList.size()>0)
				{
					LfDep dep =(LfDep)depList.get(0);
					session.setAttribute("depName", dep.getDepName());
				}
				enpCode="100001";
			}
            //获取当前登录用户的企业信息
			LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
			conditionMap1.put("corpCode", enpCode);
			List<LfCorp> returnLists = baseBiz.getByCondition(LfCorp.class,
							conditionMap1, null);
			//当前登录用户的企业信息不为空时
			if (returnLists != null && returnLists.size() > 0)
			{
				LfCorp corp = (LfCorp) returnLists.get(0);
				session.setAttribute("loginCorp", corp);
				//获取登录用户
			    sysuser = biz.login(username, pass, enpCode);
			    //用户名或密码不对
				if (sysuser == null) {
					//out.print("false");
					response.sendRedirect(request.getContextPath()+"/common/errorLogin.html");
					return;
				}else if(sysuser.getUserState() - 2 == 0)
				{
					//用户已删除
					//跳转到checkLogin.html
					response.sendRedirect(request.getContextPath()+"/common/errorLogin.html");
				}else if(sysuser.getUserState() - 0 == 0)
				{
//					//用户已注销
					//跳转到checkLogin.html
					response.sendRedirect(request.getContextPath()+"/common/forbidLogin.html");
				}else if(sysuser.getName() == null)
				{
					//跳转到checkLogin.html
					response.sendRedirect(request.getContextPath()+"/common/errorLogin.html");
				}

				//过期提醒 may add
				if(sysuser.getPwdupdatetime() != null){
					String updatetime=getLfCorpConf(sysuser);
					if(!"".equals(updatetime)&&updatetime!=null){
						String[] array=updatetime.split("@");
					if(array.length==2){
					//修改周期 提醒处理	
					if(!"0".equals(array[0])&&!"null".equals(array[0])&&!"".equals(array[0])){						
						Date currDate = new Date();
						Date oldDate = new Date(sysuser.getPwdupdatetime().getTime());
						long days = 0L;
						//计算当前时间与修改时间的值
					    days = currDate.getTime() - oldDate.getTime();
					    days = days / 1000 / 60 / 60 / 24;
					    Integer InUpate=Integer.parseInt(array[0]);
					    if(days >= InUpate){
					    	session.setAttribute("passOverdue", "true");
					    }else{
					    	session.setAttribute("passOverdue", "false");
					    	Long ins=Long.parseLong(array[0])-days;
					    	session.setAttribute("Logindays", ins+"");
					    }
					}
					
					//过期提醒 处理	
					if(!"0".equals(array[1])&&!"".equals(array[1])&&!"null".equals(array[1])){						
						Date currDate = new Date();
						Date oldDate = new Date(sysuser.getPwdupdatetime().getTime());
						long days = 0L;
						//计算当前时间与修改时间的值
					    days = currDate.getTime() - oldDate.getTime();
					    days = days / 1000 / 60 / 60 / 24;
					    Integer max=Integer.parseInt(array[0]);
					    Integer InUpate=Integer.parseInt(array[1]);
					    if(days >= InUpate&&max>days){				    	
							passOverDate=(max-days)+"";
					    }
					}
					
					
					}
				}
				}
				
				//获取当前登录用户mac-ip绑定信息
		    	LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		    	LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		    	//用户guid
				conditionMap.put("guid", sysuser.getGuId()
						.toString());
				List<LfMacIp> MacIPList = baseBiz.getByCondition(
						LfMacIp.class, conditionMap, null);
				//如果绑定了mac-ip
				if (MacIPList != null && MacIPList.size() > 0) {
					LfMacIp macIp = MacIPList.get(0);
					//启用了动态口令
					if(macIp.getDtpwd() == 1){
						// 手机动态口令
						String phoneword = request.getParameter("phoneword");
						//清空map
						conditionMap.clear();
						//用户id
						conditionMap.put("userId", sysuser.getUserId().toString());
						conditionMap.put("phoneWord", phoneword);
						List<LfDynPhoneWord> PhoneWordList = baseBiz.getByCondition(LfDynPhoneWord.class,conditionMap, null);
						//动态口令输入正确
						if (PhoneWordList != null && PhoneWordList.size() > 0) {
							//格式化时间
							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
							//日历
							//当前时间
							Calendar c = Calendar.getInstance();
							String nowDate = format.format(c.getTime());
							LfDynPhoneWord pword = PhoneWordList.get(0);
							//如果输入的动态口令与当前登录时间不在同一天，则此动态口令已过期
							if (!nowDate.equals(format.format(pword.getCreateTime()))) {
								// 手机动态口令已过期
								response.sendRedirect(request.getContextPath()+"/common/errorLogin.html");
								opContent="操作员["+sysuser.getUserName()+"]手机动态口令已过期，登录系统失败！";
								spLog.logFailureString(sysuser.getUserName(), auditModule, opType, opContent+opSper, null,sysuser.getCorpCode());
								return;
							}
						} else {
							// 输入手机动态口令错误密码错误
							response.sendRedirect(request.getContextPath()+"/common/errorLogin.html");
							opContent="操作员["+sysuser.getUserName()+"]手机动态口令错误，登录系统失败！";
							spLog.logFailureString(sysuser.getUserName(), auditModule, opType, opContent+opSper, null,sysuser.getCorpCode());
							return;
						}
						//设置为登录过一次
			    		objectMap.put("isLogin", "1");
			    		baseBiz.update(LfDynPhoneWord.class, objectMap, conditionMap);
					}
				}
			} 
			else
			{
				//获取当前登录用户企业信息错误
				response.sendRedirect(request.getContextPath()+"/common/errorLogin.html");
				return;
			}
			if (sysuser == null)
			{
				sysuser = new LfSysuser();
				EmpExecutionContext.error("操作员对象为null！");
			}
			if (sysuser.getName() != null)
			{
				//有效用户
				if (sysuser.getUserState() - 1 == 0)
				{
					result = true;
					session.setAttribute("loginSysuser", sysuser);
                    //获得当前登录用户所有的按钮权限
					String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
					Map<String, String> btnMap = biz
							.getBtnFuntionMap(langName, sysuser.getUserId()
									.toString());
					//获得当前登录用户所用权限模块的位置
					Map<String, String> titleMap = biz.getTitleMap(sysuser.getUserId()
							.toString());
					List<LfPrivilege> prList = biz
							.getMenuByPrivCodeAsc(sysuser.getUserId()
									.toString());

					Map<String, List<LfPrivilege>> priMap = new HashMap<String, List<LfPrivilege>>();
					for (int i = 0; i < prList.size(); i++)
					{
						LfPrivilege privi = prList.get(i);
						if (priMap.get(privi.getResourceId().toString()) != null)
						{
							List<LfPrivilege> pri = priMap.get(privi
									.getResourceId().toString());
							//配置的模块才添加
							if(StaticValue.getInniMenuMap().get(privi.getMenuSite())!=null)
							{
								
								pri.add(privi);
							}
						} else
						{
							List<LfPrivilege> pri = new ArrayList<LfPrivilege>();
							//配置的模块才添加
							if(StaticValue.getInniMenuMap().get(privi.getMenuSite())!=null)
							{
								
								pri.add(privi);
								priMap.put(privi.getResourceId().toString(),
										pri);
							}
						}
					}
					

					// 查找网关认证系信息
					List<AProInfo> proList = baseBiz.getEntityList(AProInfo.class);

					if (proList != null && proList.size()>0)
					{
						session.setAttribute("AProInfo", proList.get(0));
					}

					session.setAttribute("prList", prList);
					session.setAttribute("priMap", priMap);
					session.setAttribute("btnMap", btnMap);
					session.setAttribute("titleMap", titleMap);					
					
					//读取皮肤cookie,获取主题路径
					String ftameUrl = getSkinConfig(request,response,sysuser);
					
					//登录信息
					LoginInfo loginInfo = new LoginInfo();
					tkn = MD5.getMD5Str(tkn+loginInfo.getUserName()+loginInfo.getCorpCode());
					setLoginInfoMap(loginInfo, sysuser, tkn,session.getId());

					String loginparams = loginInfo.toString()+"&"+tkn;
					//如果当前登录用户的用户名与密码相同．则弹出修改密码页面
					if(MD5.getMD5Str(sysuser.getUserName()).equals(sysuser.getPassword())||MD5.getMD5Str(sysuser.getUserName()+sysuser.getUserName().toLowerCase()).equals(sysuser.getPassword()))
					{
					    //out.print("NameAndPWordSame&"+loginparams+"&"+sysuser.getUserId().toString());
						loginparams +="&NameAndPWordSame";
						session.setAttribute("NameAndPWord", "NameAndPWordSame");
					}
					else
					{
						loginparams +="&NameAndPWordNoSame";
					}
					opContent="操作员["+sysuser.getUserName()+"]登录系统成功！";
					//spLog.logSuccessString(sysuser.getUserName(), auditModule, opType, opContent,sysuser.getCorpCode());
					/*else
					{*/
						//loginparams += "isexistsubno:"+sysuser.getIsExistSubNo()+",";
					String sk="";
					if("".equals(passOverDate)){
						sk=result+"&"+loginparams+"&"+ftameUrl;
					}else{
						sk=result+"&"+loginparams+"&"+ftameUrl+"&"+passOverDate;
					}
					String para="";
		            String[] rArr = sk.split("&");

		            para=para+"?tkn="+rArr[2];
		            para=para+"&loginparams="+rArr[1];
		            para=para+"&isOneLogin="+rArr[3];
		            if(rArr.length>5){
		            	para=para+"&remd="+rArr[5];
		            }

					response.sendRedirect(request.getContextPath()+"/frame/" + rArr[4] + "/main.jsp"+para);
				} else if(sysuser.getUserState() - 2 == 0)
				{
					//out.print("deleteuser");
					response.sendRedirect(request.getContextPath()+"/common/deleteUser.html");
				}
				else
				{
					//out.print("mid");
					response.sendRedirect(request.getContextPath()+"/common/forbidLogin.html");
				}
			} else
			{
				//out.print("false");
				response.sendRedirect(request.getContextPath()+"/common/errorLogin.html");
			}
		} catch (Exception e)
		{
			//异常错误
			//out.print("error");
			//异常打印
			EmpExecutionContext.error(e,"登录异常！");
			response.sendRedirect(request.getContextPath()+"/common/errorLogin.html");

		}
	}
	/***
	 *  获得系统中配置信息
	 * @param sysuser
	 * @return
	 */
	public String getLfCorpConf(LfSysuser sysuser){
		String result="";
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", sysuser.getCorpCode());

		String modiftime="0";
		String remind="0";
		try
		{
			BaseBiz baseBiz = new BaseBiz();
			List<LfCorpConf> lfCorpConfList= baseBiz.getByCondition(LfCorpConf.class, conditionMap, null);

			for(int i=0;i<lfCorpConfList.size();i++){
			LfCorpConf conf=lfCorpConfList.get(i);
			//修改周期
			if("pwd.upcycle".equals(conf.getParamKey())){
				if(!"".equals(conf.getParamValue())){
				 modiftime=conf.getParamValue();;
				}
			}
			//过期提醒
			if("pwd.pastalarm".equals(conf.getParamKey())){
				if(!"".equals(conf.getParamValue())){
					remind=conf.getParamValue();;
					}
			}

		}
			result=modiftime+"@"+remind;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询密码设置规则异常！");
		}
		return result;
	}
	
	
	
	/**
	 * 设置登录信息
	 * @param loginInfo
	 * @param sysuser
	 * @param tkn
	 * @param sessionId
	 */
	private void setLoginInfoMap(LoginInfo loginInfo,LfSysuser sysuser,String tkn,String sessionId)
	{
		//用户id
		loginInfo.setUserId(sysuser.getUserId());
		//用户guid
		loginInfo.setGuId(sysuser.getGuId());
		//用户名
		loginInfo.setUserName(sysuser.getUserName());
		//用户编码
		loginInfo.setUserCode(sysuser.getUserCode());
		//企业编码
		loginInfo.setCorpCode(sysuser.getCorpCode());
		//sessionid
		loginInfo.setSessionId(sessionId);
		
		//判断是否已存在登录对象
		Map<String,LoginInfo> loginMap = StaticValue.getLoginInfoMap();
		Iterator<String> itr = loginMap.keySet().iterator();
		String key;
		String removeKey = null;
		while(itr.hasNext())
		{
			key=itr.next();
			LoginInfo logininfo = loginMap.get(key);
			
			if(sessionId.equals(logininfo.getSessionId()))
			{
				removeKey = key;
				break;
			}
		}
		if(removeKey != null)
		{
			StaticValue.getLoginInfoMap().remove(removeKey);
		}
		StaticValue.getLoginInfoMap().put(tkn, loginInfo);
	}

	/**
	 * 操作皮肤cookie
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	private String getSkinConfig(HttpServletRequest request, HttpServletResponse response,LfSysuser sysuser) throws Exception
	{

		String frameUrl = SystemGlobals.getValue("emp.web.frame");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		conditionMap.put("userid", sysuser.getUserId().toString());
		
		String skin = "";
		BaseBiz baseBiz = new BaseBiz();
		List<LfUser2skin> userskinList = baseBiz.getByCondition(LfUser2skin.class, conditionMap, null);
	    LfUser2skin u2S;
	    if ((userskinList != null) && (userskinList.size() > 0))
	    {
	      u2S = (LfUser2skin)userskinList.get(0);
	      skin = u2S.getSkincode();
	      frameUrl = u2S.getThemecode();
	    }
	    else {
	      u2S = new LfUser2skin();
	      u2S.setSkincode("default");
	      u2S.setUserid(sysuser.getUserId());
	      u2S.setSid(Long.valueOf(1L));
	      u2S.setThemecode(frameUrl);

	      baseBiz.addObj(u2S);
	      skin = u2S.getSkincode();
	    }
	    String path = request.getContextPath();
	    request.getSession().setAttribute("stlyeSkin", path + "/frame/" + frameUrl + "/skin/" + skin);
	    request.getSession().setAttribute("emp.web.frame", frameUrl);
	    return frameUrl;
	}
	
}