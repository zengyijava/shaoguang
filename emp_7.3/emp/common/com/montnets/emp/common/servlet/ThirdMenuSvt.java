package com.montnets.emp.common.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.LoginBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.common.dao.impl.GenericEmpDAO;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.LfPrivilegeAndMenuVo;
import com.montnets.emp.entity.gateway.AProInfo;
import com.montnets.emp.entity.notice.LfNotice;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfSpeUICfg;
import com.montnets.emp.entity.system.LfThiMenuControl;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.TxtFileUtil;
/**
 * 三级菜单控制
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class ThirdMenuSvt extends BaseServlet
{
	
	/**
	 * 获取菜单
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getPriList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Map<String,List<LfPrivilege>> priMap = null;
		String priMenus = null;
		HttpSession session = null;
		try {
			session = request.getSession(false);
			priMap = (Map<String, List<LfPrivilege>>) session.getAttribute("priMap");
			priMenus = request.getParameter("priMenus");
			LfSysuser lfSysuser=((LfSysuser) session.getAttribute("loginSysuser"));
			LfDep lfdep=new BaseBiz().getById(LfDep.class, lfSysuser.getDepId());
			request.setAttribute("lfdep", lfdep);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取菜单，获取操作员对象异常。");
		}
		
		List<LfPrivilege> prList =new ArrayList<LfPrivilege>();
		if(priMenus!=null && priMap!=null)
		{
			String[] menus = priMenus.split(",");
			for(int i=0;i<menus.length;i++)
			{
				if(priMap.get(menus[i])!=null)
				{
					prList.addAll( priMap.get(menus[i]));
				}
			}
		}
		request.getSession(false).setAttribute("prList", prList);
		String frame = "";
		if(session != null){
			frame = (String) session.getAttribute(StaticValue.EMP_WEB_FRAME);
		}
		//跳转页面
		request.getRequestDispatcher("common/frame/"+ frame+ "/left.jsp")
		.forward(request, response);
	}
	
	/**
	 * 获取菜单
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getAllPriList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		orderbyMap.put("priOrder", StaticValue.ASC);
		HttpSession session = null;
		try {
			session = request.getSession(false);
			LfSysuser lfSysuser=((LfSysuser) session.getAttribute("loginSysuser"));
			LfDep lfdep=new BaseBiz().getById(LfDep.class, lfSysuser.getDepId());
			request.setAttribute("lfdep", lfdep);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取菜单，获取操作员对象异常。");
		}
		
		try {
			List<LfThiMenuControl> thirdMenuList = new GenericEmpDAO().findListBySymbolsCondition(LfThiMenuControl.class, conditionMap, orderbyMap);
			LfThiMenuControl tt = new LfThiMenuControl();
			tt.setMenuNum(0);
			thirdMenuList.add(tt);
			request.setAttribute("thirdMenuList", thirdMenuList);

			String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
			Map<Integer, String> thirdMenuMap = (Map<Integer, String>) request.getSession(false).getAttribute("thirdMenuMap");
			if (thirdMenuMap == null) {
				thirdMenuMap = new LinkedHashMap<Integer, String>();
			}
			Set<Integer>  numSet = getLoginUserMenuNums(request, response);
			for (LfThiMenuControl menu : thirdMenuList) {
				
				//不拥有对应权限
				if(numSet != null&&!numSet.contains(menu.getMenuNum())){
					continue;
				}
				
				if (StaticValue.ZH_TW.equals(langName)) {
					thirdMenuMap.put(menu.getMenuNum(), menu.getZhTwTitle());
				} else if (StaticValue.ZH_HK.equals(langName)) {
					thirdMenuMap.put(menu.getMenuNum(), menu.getZhHkTitle());
				} else {
					thirdMenuMap.put(menu.getMenuNum(), menu.getTitle());
				}
			}
			
			request.getSession(false).setAttribute("thirdMenuMap", thirdMenuMap);
			
		}catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取菜单异常。");
		}
		String frame = (String) session.getAttribute(StaticValue.EMP_WEB_FRAME);
		//跳转页面
		request.getRequestDispatcher("/frame/"+frame + "/left.jsp")
			.forward(request, response);
	}
	
	/**
	 * 主页面加载几大模块的方法
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void thirdMenuPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		orderbyMap.put("priOrder", StaticValue.ASC);
		HttpSession session = null;
		try {
			//if(StaticValue.menu_num!=null && !"".equals(StaticValue.menu_num.toString()))
			if(StaticValue.getMenu_num()!=null && !"".equals(StaticValue.getMenu_num().toString()))
			{
				
				//conditionMap.put("menuNum&in", StaticValue.menu_num.toString());
				conditionMap.put("menuNum&in", StaticValue.getMenu_num().toString());
			}
			List<LfThiMenuControl> thirdMenuList = new GenericEmpDAO().findListBySymbolsCondition(LfThiMenuControl.class, conditionMap, orderbyMap);
			request.setAttribute("thirdMenuList", thirdMenuList);
			
			int menuCount = 1;//模块菜单总数
			LfThiMenuControl menu = thirdMenuList.get(0);
			int menuNum = menu.getMenuNum();
			int size = thirdMenuList.size();
			for(int i=1;i<size;i++)
			{
				if(thirdMenuList.get(i).getMenuNum() - menuNum != 0)
				{
					menuNum = thirdMenuList.get(i).getMenuNum();
					menuCount++;
				}
			}
			request.setAttribute("menuCount", menuCount);
			
			//取得session中的认证信息			
			session = request.getSession(false);
			AProInfo proInfo = (AProInfo)session.getAttribute("AProInfo");
			proInfo = proInfo==null?new AProInfo():proInfo;
			//获取关于平台处的认证信息，如果认证天数小于15天，则需要给出提示
			if(proInfo.getValidDays() < 15)
			{
				Integer ValidDay = (Integer)session.getAttribute("ValidDay");
				if(ValidDay == null || ValidDay.toString() == "")
				{
					//返回结果到前台页面
					request.setAttribute("proInfo", "over");
					session.setAttribute("ValidDay", proInfo.getValidDays());
				}
			}
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e, "svt主页面加载几大模块异常。");
		}
		String frame = "";
		if(session != null){
		    frame = (String) session.getAttribute(StaticValue.EMP_WEB_FRAME);
		}
        //跳转到首页面
		request.getRequestDispatcher("/frame/"+frame + "/index.jsp")
			.forward(request, response);
	}
	/**
	 * 跳转到图标列表界面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getMonInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String frame = (String) request.getSession(false).getAttribute(StaticValue.EMP_WEB_FRAME);
		LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);
    	if(null == lfSysuser){
    		request.getRequestDispatcher("/common/logoutEmp.html").forward(request, response);
    	}else{
    		request.getRequestDispatcher("/frame/"+frame + "/index.jsp").forward(request, response);
    	}
	}
	
	/**
	 * 主页面加载几大模块的方法(frame3.0异步调用)
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getPageJson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);
		orderbyMap.put("priOrder", StaticValue.ASC);
		JSONObject json = new JSONObject();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			//if(StaticValue.menu_num!=null && !"".equals(StaticValue.menu_num.toString()))
			if(StaticValue.getMenu_num()!=null && !"".equals(StaticValue.getMenu_num().toString()))
			{
				
				//conditionMap.put("menuNum&in", StaticValue.menu_num.toString());
				conditionMap.put("menuNum&in", StaticValue.getMenu_num().toString());
			}
			List<LfThiMenuControl> thirdMenuList = new GenericEmpDAO().findListBySymbolsCondition(LfThiMenuControl.class, conditionMap, orderbyMap);
//			request.setAttribute("thirdMenuList", thirdMenuList);
			Set<Integer>  numSet = getLoginUserMenuNums(request, response);
			if(numSet != null){
				Iterator<LfThiMenuControl> its = thirdMenuList.iterator();
				while(its.hasNext()){
					if(!numSet.contains(its.next().getMenuNum())){
						its.remove();
					}
				}
			}
			json.put("thirdMenuList", getThirdMenuList(thirdMenuList, langName));
			int menuCount = 1;//模块菜单总数
			LfThiMenuControl menu = thirdMenuList.get(0);
			int menuNum = menu.getMenuNum();
			int size = thirdMenuList.size();
			for(int i=1;i<size;i++)
			{
				if(thirdMenuList.get(i).getMenuNum() - menuNum != 0)
				{
					menuNum = thirdMenuList.get(i).getMenuNum();
					menuCount++;
				}
			}
			json.put("menuCount", menuCount);
//			request.setAttribute("menuCount", menuCount);
			
			//取得session中的认证信息			
			HttpSession session = request.getSession(false);
			AProInfo proInfo = (AProInfo)session.getAttribute("AProInfo");
			proInfo = proInfo==null?new AProInfo():proInfo;
			//获取关于平台处的认证信息，如果认证天数小于15天，则需要给出提示
			if(proInfo.getValidDays() < 15)
			{
				Integer ValidDay = (Integer)session.getAttribute("ValidDay");
				if(ValidDay == null || ValidDay.toString() == "")
				{
					//返回结果到前台页面
					json.put("proInfo", "over");
//					request.setAttribute("proInfo", "over");
					session.setAttribute("ValidDay", proInfo.getValidDays());
				}
			}
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e, "svt主页面加载几大模块异常。");
		}finally{
			out.print(json.toString());
		}		

	}
	/***
	 * 菜单中循环值拼装成JSONArray
	 * @param thirdMenuList 菜单列表
	 * @return JSONArray数组
	 */
	public JSONArray getThirdMenuList(List<LfThiMenuControl>  thirdMenuList){
		JSONArray array = new JSONArray();
		for (int i=0;i<thirdMenuList.size();i++){
			JSONObject json = new JSONObject();
			LfThiMenuControl control=(LfThiMenuControl)thirdMenuList.get(i);
			json.put("title", control.getTitle());
			json.put("menuNum", control.getMenuNum());
			json.put("priMenu", control.getPriMenu());
			json.put("priOrder", control.getPriOrder());
			json.put("tid", control.getTid());
			array.add(json);
		}
		return array;
	}
	
	/***
	 * 菜单中循环值拼装成JSONArray
	 * @param thirdMenuList 菜单列表
	 * @return JSONArray数组
	 */
	public JSONArray getThirdMenuList(List<LfThiMenuControl> thirdMenuList, String langName) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < thirdMenuList.size(); i++) {
			JSONObject json = new JSONObject();
			LfThiMenuControl control = (LfThiMenuControl) thirdMenuList.get(i);
			if (StaticValue.ZH_TW.equals(langName)) {
				json.put("title", control.getZhTwTitle());
			} else if (StaticValue.ZH_HK.equals(langName)) {
				json.put("title", control.getZhHkTitle());
			} else {
				json.put("title", control.getTitle());
			}
			json.put("menuNum", control.getMenuNum());
			json.put("priMenu", control.getPriMenu());
			json.put("priOrder", control.getPriOrder());
			json.put("tid", control.getTid());
			array.add(json);
		}
		return array;
	}
	
	
	
	/**
	 * 获取菜单
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toTopPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		orderbyMap.put("priOrder", StaticValue.ASC);
		String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);
		try {
			//if(StaticValue.menu_num!=null && !"".equals(StaticValue.menu_num.toString()))
			if(StaticValue.getMenu_num()!=null && !"".equals(StaticValue.getMenu_num().toString()))
			{

				//conditionMap.put("menuNum&in", StaticValue.menu_num.toString());
				conditionMap.put("menuNum&in", StaticValue.getMenu_num().toString());
			}

			List<LfThiMenuControl> thirdMenuList = new GenericEmpDAO().findListBySymbolsCondition(LfThiMenuControl.class, conditionMap, orderbyMap);
			Map<Integer,String> thirdMenuMap = new LinkedHashMap<Integer,String>();
			Set<Integer>  numSet = getLoginUserMenuNums(request, response);
			
			if(thirdMenuList != null && thirdMenuList.size() > 0)
			{
				for(LfThiMenuControl menu : thirdMenuList)
				{
					//不拥有对应权限
					if(numSet != null&&!numSet.contains(menu.getMenuNum())){
						continue;
					}
					
					if (StaticValue.ZH_TW.equals(langName)) {
						thirdMenuMap.put(menu.getMenuNum(), menu.getZhTwTitle());
					} else if (StaticValue.ZH_HK.equals(langName)) {
						thirdMenuMap.put(menu.getMenuNum(), menu.getZhHkTitle());
					} else {
						thirdMenuMap.put(menu.getMenuNum(), menu.getTitle());
					}
					
				}
			}
			request.getSession(false).setAttribute("thirdMenuMap", thirdMenuMap);
			getCfg(request, response);
			
		}catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "svt获取菜单异常。");
		}
		String frame="";
		try
		{
			frame = (String) request.getSession(false).getAttribute(StaticValue.EMP_WEB_FRAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取菜单设置session失败！");
		}
		//跳转页面
		request.getRequestDispatcher("/frame/"+frame + "/top.jsp")
			.forward(request, response);
	}
	
	/**
	 * 处理个性化设置
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getCfg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//查找当前登录操作员企业下个性化设置信息
		String corpCode = request.getParameter("corpcode");
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		conditionMap.put("corpCode", corpCode);
		List<LfSpeUICfg> cfgs;
		try
		{
			cfgs = new GenericEmpDAO().findListByCondition(LfSpeUICfg.class, conditionMap, null);
			if(cfgs.size()>0){
				//个性化设置信息
				LfSpeUICfg cfg = cfgs.get(0);
				//多企业时将个性化信息缓存至cookie
			/*2014-05-13 取消多企业的登录页个性化
			 * 	if(StaticValue.CORPTYPE!=0){
					int maxAge=7*24*3600;
					addCookie(response, "displayType", String.valueOf(cfg.getDisplayType()), maxAge);
					addCookie(response, "loginLogo", cfg.getLoginLogo(), maxAge);
					addCookie(response, "bgImg", cfg.getBgImg(), maxAge);
					addCookie(response, "dispContent", cfg.getDispContent(), maxAge);
				}*/
				//内页个性化公司logo处理（图片是否存在 集群等）
				String url = cfg.getCompanyLogo();
				if(url!=null&&!"".equals(url.trim())){
					String servletPath=new TxtFileUtil().getWebRoot();
					File f = new File(servletPath+url);
					//文件本地不存在 且为集群时 则 从文件服务器上下载文件
					if(!f.exists()&& StaticValue.getISCLUSTER() ==1){
							CommonBiz comBiz = new CommonBiz();
							String downMsg = comBiz.downloadFileFromFileCenter(url);
							if("error".equals(downMsg)){
							EmpExecutionContext.error("从文件服务器下载文件"+url+"失败！");
							}
					}
					if(!f.exists()){
						cfg.setCompanyLogo(null);
					}
				}
				request.setAttribute("cfg", cfg);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取个性化设置失败！");
		}
	}
	
	public void addCookie(HttpServletResponse response,String name,String value,int maxAge){
	    Cookie cookie = new Cookie(name,value);
	    cookie.setPath("/");
	    if(maxAge>0)  cookie.setMaxAge(maxAge);
	    response.addCookie(cookie);
	}
	/**
	 * 3.0下 top跳转前查询个性化设置
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toTop(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String frame = (String) request.getSession(false).getAttribute(StaticValue.EMP_WEB_FRAME);
		getCfg(request, response);
		//跳转页面
		request.getRequestDispatcher("/frame/"+frame + "/top.jsp")
		.forward(request, response);
	}
	
	/**
	 * 简约版首页
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toNewIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String frame = (String) request.getSession(false).getAttribute(StaticValue.EMP_WEB_FRAME);
		request.getRequestDispatcher("/frame/"+frame + "/newIndex.jsp").forward(request, response);
	}
	
	/**
	 * 首页信息查询及首页页面跳转
	 * @description    
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException       			 
	 * @author zhagmin 
	 * @datetime 2013-10-15 上午11:54:20
	 */
	public void getWarm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String lgcorpcode=request.getParameter("lgcorpcode");
		//String lguserid=request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		BaseBiz baseBiz = new BaseBiz();
		SpecialDAO spDao = new SpecialDAO();
		String[] resultStr = new String[15];
		String frame = "";
		try {
			frame = (String) request.getSession(false).getAttribute(StaticValue.EMP_WEB_FRAME);
			//系统配置的大模块
			//String [] menu_num=StaticValue.menu_num.toString().split(",");
			String [] menu_num=StaticValue.getMenu_num().toString().split(",");
			LinkedHashMap<String,String> menMap=new LinkedHashMap<String,String>();
			if(menu_num!=null )
			{
				for(int i=0;i<menu_num.length;i++)
				{
					menMap.put(menu_num[i],"");
				}
			}
			//是否有网讯模块
			boolean isHaveWx=true;
			if(!menMap.containsKey("14"))
			{
				isHaveWx=false;
			}
			LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap=new LinkedHashMap<String, String>();
			conditionMap.put("corpcode", lgcorpcode);
			orderbyMap.put("publishTime", "desc");
			List<LfNotice> lfnoticeList=new BaseBiz().getByCondition(LfNotice.class, conditionMap, orderbyMap);
			
			//待处理
			int msrecount = 0, smsrecount = 0, mmsrecount = 0;
			int tmrecount = 0, smstmrecount = 0, mmstmrecount = 0, wxtmrecount = 0;
			//属于自己需要审批的任务
			List<DynaBean> reRecordList = spDao.getFlowRecordWithNoReview(lguserid);
			if(reRecordList != null && reRecordList.size() > 0)
			{
				for(DynaBean dyb : reRecordList)
				{
					Short infotype = Short.parseShort(dyb.get("info_type").toString()) ;
					Integer count = Integer.parseInt( dyb.get("recount").toString());
					
					switch(infotype )
					{
					case 1:
						msrecount += count;
						smsrecount += count;
						break;
					case 2:
						msrecount += count;
						mmsrecount += count;
						break;
					case 3:
						tmrecount += count;
						smstmrecount += count;
						break;
					case 4:
						tmrecount += count;
						mmstmrecount += count;
						break;
					case 6:
						tmrecount += count;
						wxtmrecount += count;
						break;
					}
				}
			}
			resultStr[0] = String.valueOf(msrecount);
			resultStr[1] = String.valueOf(smsrecount);
			resultStr[2] = String.valueOf(mmsrecount);
			resultStr[3] = String.valueOf(tmrecount);
			resultStr[4] = String.valueOf(smstmrecount);
			resultStr[5] = String.valueOf(mmstmrecount);
			resultStr[6] = String.valueOf(wxtmrecount);
			//任务审批进度
			int taskcount = 0, smstaskcount = 0, mmstaskcount = 0, wxtaskcount = 0;
			//待审批的任务
			List<DynaBean> reMttaskList = spDao.getMttaskWithNoReview(lguserid);
			if(reMttaskList != null && reMttaskList.size() > 0)
			{
				for(DynaBean dyb : reMttaskList)
				{
					
					Short infotype = Short.parseShort(dyb.get("ms_type").toString());
					Integer count = Integer.parseInt( dyb.get("mtcount").toString());
					
					taskcount += count;
					switch(infotype )
					{
					case 1:
						smstaskcount += count;
						break;
					case 2:
						mmstaskcount += count;
						break;
					case 5:
						smstaskcount += count;
						break;
					case 6:
						wxtaskcount += count;
						break;
					}
				}
			}
			resultStr[7] = String.valueOf(taskcount);
			resultStr[8] = String.valueOf(smstaskcount);
			resultStr[9] = String.valueOf(mmstaskcount);
			resultStr[10] = String.valueOf(wxtaskcount);
			
			//待审批的模板
			int tmcount = 0, smstmcount = 0, mmstmcount = 0, wxtmcount = 0;
			List<DynaBean> reTempList = spDao.getTempWithNoReview(lguserid);
			if(reTempList != null && reTempList.size() > 0)
			{
				for(DynaBean dyb : reTempList)
				{
					Short infotype = Short.parseShort(dyb.get("tmp_type").toString());
					Integer count = Integer.parseInt( dyb.get("tmcount").toString());
					
					tmcount += count;
					switch(infotype )
					{
					case 3:
						smstmcount += count;
						break;
					case 4:
						mmstmcount += count;
						break;
					}
				}
			}
			
			Integer wxcount = 0;
			//没有网讯模块不查询网讯信息
			if(isHaveWx)
			{
				wxcount = spDao.getWxTempReviewCount(lguserid);
			}
			if(wxcount != null)
			{
				wxtmcount = wxcount.intValue();
				tmcount += wxtmcount;
			}
			resultStr[11] = String.valueOf(tmcount);
			resultStr[12] = String.valueOf(smstmcount);
			resultStr[13] = String.valueOf(mmstmcount);
			resultStr[14] = String.valueOf(wxtmcount);
			
			//定时任务 
			int timercount = 0, todaycount = 0, succcount = 0, failcount = 0, waitcount = 0;
			int timercount2 = 0, todaycount2 = 0, succcount2 = 0, failcount2 = 0, waitcount2 = 0;
			int timercount3 = 0, todaycount3 = 0, succcount3 = 0, failcount3 = 0, waitcount3 = 0;
			
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
			SimpleDateFormat df2=new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
    		Date date1=df.parse(df.format(new Date()));
    		//当天时间
    		Calendar c1=Calendar.getInstance();
    		//隔天时间
    		Calendar c2=Calendar.getInstance();
    		c1.setTime(date1);
    		c2.setTime(date1);
    		c2.add(Calendar.DAY_OF_MONTH, 1);
    		conditionMap.clear();
			conditionMap.put("userId", lguserid);
			conditionMap.put("subState&<>", "3");
			//conditionMap.put("sendstate", "0");		
			conditionMap.put("timerStatus", "1");
			conditionMap.put("timerTime&<", df2.format(c2.getTime()));
			conditionMap.put("timerTime&>=", df2.format(c1.getTime()));
			//定时中的发送 任务
			List<LfMttask> timeMttaskList = baseBiz.getByCondition(LfMttask.class, conditionMap, null);
    		if(timeMttaskList != null && timeMttaskList.size() > 0)
    		{
    			for(LfMttask mt : timeMttaskList)
    			{
    				if(mt.getMsType() == 1)
    				{
    					todaycount += 1;
    					if(mt.getSendstate() == 1 
    							|| mt.getSendstate() == 4 
    							|| mt.getSendstate() == 6 
    							|| mt.getSendstate() == 3)
    					{
    						succcount += 1;
    					}else if(mt.getSendstate() == 0 )
    					{
    						waitcount += 1;
    					}else if(mt.getSendstate() == 2 )
    					{
    						failcount += 1;
    					}
    				}else if(mt.getMsType() == 2)
    				{
    					todaycount2 += 1;
    					if(mt.getSendstate() == 1 
    							|| mt.getSendstate() == 4 
    							|| mt.getSendstate() == 6 
    							|| mt.getSendstate() == 3)
    					{
    						succcount2 += 1;
    					}else if(mt.getSendstate() == 0 )
    					{
    						waitcount2 += 1;
    					}else if(mt.getSendstate() == 2 )
    					{
    						failcount2 += 1;
    					}
    				}else if(mt.getMsType() == 6)
    				{
    					todaycount3 += 1;
    					if(mt.getSendstate() == 1 
    							|| mt.getSendstate() == 4 
    							|| mt.getSendstate() == 6 
    							|| mt.getSendstate() == 3)
    					{
    						succcount3 += 1;
    					}else if(mt.getSendstate() == 0 )
    					{
    						waitcount3 += 1;
    					}else if(mt.getSendstate() == 2 )
    					{
    						failcount3 += 1;
    					}
    				}
    			}
    		}
    		
    		List<DynaBean> timerCountList = spDao.getTimerTaskCount(lguserid);
    		if(timerCountList != null && timerCountList.size() > 0)
			{
				for(DynaBean dyb : timerCountList)
				{
					Short infotype = Short.parseShort(dyb.get("ms_type").toString());
					Integer count = Integer.parseInt( dyb.get("tmcount").toString());
					
					if(infotype == 1)
					{
						timercount = count.intValue();
					}else
					if(infotype == 2)
					{
						timercount2 = count.intValue();
					}else
					if(infotype == 6)
					{
						timercount3 = count.intValue();
					}
				}
			}
    		int smsTimer[] = {todaycount,succcount,failcount,waitcount,timercount};
    		int mmsTimer[] = {todaycount2,succcount2,failcount2,waitcount2,timercount2};
			int wxTimer[] = {todaycount3,succcount3,failcount3,waitcount3,timercount3};
			
			request.setAttribute("smsTimer", smsTimer);
			request.setAttribute("mmsTimer", mmsTimer);
			request.setAttribute("wxTimer", wxTimer);
			
			LfNotice lfNotice = new LfNotice();
			if(lfnoticeList!=null && lfnoticeList.size()>0)
			{
				lfNotice = lfnoticeList.get(0);
			}
			request.setAttribute("lfNotice", lfNotice);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "svt获取审批任务信息异常。");
		}
		request.setAttribute("resultStr", resultStr);
		//跳转页面
		request.getRequestDispatcher("/frame/"+frame + "/warmPrompt.jsp")
			.forward(request, response);
	}
	
	//登录用户拥有的大模块权限
	public Set<Integer> getLoginUserMenuNums(HttpServletRequest request, HttpServletResponse response){
		LoginBiz loginBiz=new LoginBiz();
		List<LfPrivilegeAndMenuVo> prList = null;
		try
		{
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(sysuser == null){
				EmpExecutionContext.error("获取登录用户拥有的大模块权限，session获取失败！");
				return null;
			}
			Long lguserid = sysuser.getUserId();
			String lgcorpcode = sysuser.getCorpCode();
			prList = loginBiz.getAllMenuByUserId(lgcorpcode, lguserid.toString(),"1");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "操作员权限查询失败！");
			return null;
		}
		//循环角色菜单列表，与配置文件里配置的模块匹配，没有配置的模块不显示  
		Set<Integer> numSet=new HashSet<Integer>();
		if(prList!=null && prList.size()>0)
		{
			String menuCode="";
			for(int i=0;i<prList.size();i++)
			{
				LfPrivilegeAndMenuVo priVo=prList.get(i);
				if(StaticValue.getInniMenuMap().get(priVo.getMenuSite())!=null || menuCode.equals(priVo.getMenuCode()))
				{
					menuCode=priVo.getMenuCode();
					numSet.add(priVo.getMenuNum());
				}
				//号码是否可见和运营商查看不是左边栏的模块，做特殊处理
				else if("1600-2000-0".equals(priVo.getPrivCode()) || "1600-1900-0".equals(priVo.getPrivCode()))
				{
					numSet.add(priVo.getMenuNum());
				}
			}
		}
		
		return numSet;
	}
	
	/**
	 * 获取菜单
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getFirstMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		orderbyMap.put("priOrder", StaticValue.ASC);
		String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);
		try {
			//if(StaticValue.menu_num!=null && !"".equals(StaticValue.menu_num.toString()))
			if(StaticValue.getMenu_num()!=null && !"".equals(StaticValue.getMenu_num().toString()))
			{
				
				//conditionMap.put("menuNum&in", StaticValue.menu_num.toString());
				conditionMap.put("menuNum&in", StaticValue.getMenu_num().toString());
			}
			List<LfThiMenuControl> thirdMenuList = new GenericEmpDAO().findListBySymbolsCondition(LfThiMenuControl.class, conditionMap, orderbyMap);
			Map<Integer,String> thirdMenuMap = new LinkedHashMap<Integer,String>();
			Set<Integer>  numSet = getLoginUserMenuNums(request, response);
			
			if(thirdMenuList != null && thirdMenuList.size() > 0)
			{
				for(LfThiMenuControl menu : thirdMenuList)
				{
					//不拥有对应权限
					if(numSet != null&&!numSet.contains(menu.getMenuNum())){
						continue;
					}
					
					if (StaticValue.ZH_TW.equals(langName)) {
						thirdMenuMap.put(menu.getMenuNum(), menu.getZhTwTitle());
					} else if (StaticValue.ZH_HK.equals(langName)) {
						thirdMenuMap.put(menu.getMenuNum(), menu.getZhHkTitle());
					} else {
						thirdMenuMap.put(menu.getMenuNum(), menu.getTitle());
					}
					
				}
			}
			request.getSession(false).setAttribute("thirdMenuMap", thirdMenuMap);
			getCfg(request, response);
			
		}catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "svt获取菜单异常。");
		}
	}
}
