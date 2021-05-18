package com.montnets.emp.weix.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.entity.weix.LfWcMenu;
import com.montnets.emp.entity.weix.LfWcTemplate;
import com.montnets.emp.weix.biz.AccountBiz;
import com.montnets.emp.weix.biz.MenuBiz;
import com.montnets.emp.weix.biz.WeixBiz;
import com.montnets.emp.weix.common.util.CommonUtils;
import com.montnets.emp.weix.common.util.GlobalMethods;
import com.montnets.emp.weix.common.util.PropertiesUtil;
import com.montnets.emp.weix.common.util.TrustSSL;

/**
 * 自定义图文模块servlet
 * 
 * @project p_weix
 * @author linzhihan <zhihanking@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-13 下午05:44:32
 * @description
 */
@SuppressWarnings("serial")
public class DefineMenuSvt extends BaseServlet
{

	// 资源路径
	private static final String		PATH		= "/weix/definemenu";
	// 基础逻辑层
	private final BaseBiz	baseBiz	= new BaseBiz();
	// 菜单逻辑层
	private final MenuBiz	menuBiz	= new MenuBiz();
	// 公众帐号逻辑层
	private final AccountBiz accountBiz	= new AccountBiz();
	// 微信菜单发布逻辑层
	private final TrustSSL	trustSSL	= new TrustSSL();
	
	private final String opModule = "自定义菜单";

	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		// EMP企业公众帐号
		// 查询所有的公众帐号
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", lgcorpcode);
		List<LfWcAccount> lfWcAccList = new ArrayList<LfWcAccount>();
		try
		{
			lfWcAccList = new BaseBiz().getByCondition(LfWcAccount.class, conditionMap, null);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, opModule + "-获取公众帐号记录失败！");
		}finally{
			request.setAttribute("acctList", lfWcAccList);
			request.getRequestDispatcher(PATH + "/dfm_definemenu.jsp").forward(request, response);
		}
	}

	/**
	 * 通过公众号ID获取菜单信息
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getMenuByAId(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		// 公众号ID
		String aid = request.getParameter("aid");

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try
		{
			// appid信息
			String appStr = "";
			// 公众号信息
			LfWcAccount account = baseBiz.getById(LfWcAccount.class, aid);
			if(account != null)
			{
				// appid信息赋值
				appStr = (account.getAppId() == null ? "" : account.getAppId()) + "&" + (account.getSecret() == null ? "" : account.getSecret());
			}
			conditionMap.put("AId", aid);
			// 先按一级菜单排序
			orderbyMap.put("PId", StaticValue.ASC);
			orderbyMap.put("morder", StaticValue.ASC);
			// 获得该公众号对应的菜单信息
			List<LfWcMenu> menuList = baseBiz.getByCondition(LfWcMenu.class, conditionMap, orderbyMap);
			// 返回结果的Map<key：一级菜单Id，value：一级菜单对应的菜单信息>
			LinkedHashMap<Long, JSONArray> resultJsonMap = new LinkedHashMap<Long, JSONArray>();

			if(menuList != null && menuList.size() > 0)
			{
				for (LfWcMenu menu : menuList)
				{
					JSONArray resultArray = new JSONArray();
					Long key = menu.getMId();
					// 如果不是一级菜单，是一级菜单时pid=0
					if(menu.getPId().intValue() != 0)
					{
						// 获取对应的一级菜单信息
						resultArray = resultJsonMap.get(menu.getPId());
						if(resultArray == null)
						{
							continue;
						}
						key = menu.getPId();
					}
					// 设置菜单属性
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("mname", menu.getMname());
					jsonObj.put("mid", menu.getMId());
					jsonObj.put("pid", menu.getPId());
					jsonObj.put("murl", menu.getMurl());
					jsonObj.put("tid", menu.getTId());
					jsonObj.put("mtype", menu.getMtype());

					resultArray.add(jsonObj);
					resultJsonMap.put(key, resultArray);
				}
			}
			// 将分组后的信息添加到一个json数组中
			JSONArray resultArray = new JSONArray();
			Iterator<Long> iter = resultJsonMap.keySet().iterator();
			while(iter.hasNext())
			{
				Long key = iter.next();
				resultArray.add(resultJsonMap.get(key));
			}
			// 返回结果
			response.getWriter().print(appStr + "@" + resultArray.toString());
		}
		catch (Exception e)
		{
			response.getWriter().print("@");
			EmpExecutionContext.error(e, "通过公众号ID获取菜单信息异常！");
		}

	}

	/**
	 * 新增菜单
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void addMenu(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		// 已存在的菜单个数
		String size = request.getParameter("size");
		// 菜单名称
		String mname = request.getParameter("mname");
		// 父级菜单ID，一级菜单时为0
		String pid = request.getParameter("pid");
		// 公众号Id
		String aid = request.getParameter("aid");
		// 企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		// 一级菜单添加时字节长度判断为8
		if("0".equals(pid) && mname.getBytes("GBK").length > 8)
		{
			response.getWriter().print("overlength");
			return;
		}
		// 二级菜单添加时字节长度判断为16
		else
			if(mname.getBytes("GBK").length > 16)
			{
				response.getWriter().print("overlength");
				return;
			}
		// 设置属性
		LfWcMenu menu = new LfWcMenu();
		menu.setPId(Long.valueOf(pid));
		menu.setMname(mname);
		menu.setMorder(Integer.parseInt(size) + 1);
		menu.setCorpCode(lgcorpcode);
		menu.setAId(Long.valueOf(aid));
		menu.setMkey(menuBiz.getKeyId(pid, aid));
		try
		{
			// 如果不是一级菜单，则需判断是否是第一个2级菜单创建，是则需重置一级菜单的动作
			if("0".equals(size) && pid != null && !"0".equals(pid))
			{
				LfWcMenu pmenu = baseBiz.getById(LfWcMenu.class, Long.valueOf(pid));
				if(pmenu.getMtype() > 0)
				{
					pmenu.setMurl("");
					pmenu.setMsgText("");
					pmenu.setMsgXml("");
					pmenu.setMtype(0);
					pmenu.setTId(0l);
					// 属性值设为null时该方法无法将值更新至数据库
					boolean res = baseBiz.updateObj(pmenu);
					if(!res)
					{
						throw new Exception();
					}
				}
			}
			Long result = baseBiz.addObjProReturnId(menu);
			if(result != null && result > 0)
			{
			Object sysObj=request.getSession().getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					String corpCode =lfSysuser.getCorpCode();
					
					EmpExecutionContext.info(opModule, lfSysuser.getCorpCode(), 
							lfSysuser.getUserId().toString(), lfSysuser.getUserName(), 
							"新增菜单（菜单名称:"+mname+"，企业："+lgcorpcode+"，公众号Id："+aid+"，已存在的菜单个数："+size+"）成功", 
							StaticValue.ADD);
					//EmpExecutionContext.info("模块名称：" + opModule + "，企业："+corpCode+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，新增菜单（菜单名称:"+mname+"，企业："+lgcorpcode+"，公众号Id："+aid+"，已存在的菜单个数："+size+"）成功");
				}
				response.getWriter().print("true" + result.toString());
			}else{
			Object sysObj=request.getSession().getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					String corpCode =lfSysuser.getCorpCode();
					EmpExecutionContext.error("模块名称：" + opModule + "，企业："+corpCode+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，新增菜单（菜单名称:"+mname+"，企业："+lgcorpcode+"，公众号Id："+aid+"，已存在的菜单个数："+size+"）失败");
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "添加微信" + opModule + "（菜单名称:"+mname+"，企业："+lgcorpcode+"，公众号Id："+aid+"，已存在的菜单个数："+size+"）失败！");
			response.getWriter().print(false);
		}

	}

	/**
	 * 删除菜单
	 * 
	 * @param request
	 * @param response
	 */
	public void delMenu(HttpServletRequest request, HttpServletResponse response)
	{
		response.setContentType("text/html");
		String mid = request.getParameter("mid");
		try
		{
			// 查找待删除的菜单信息
			LfWcMenu menu = baseBiz.getById(LfWcMenu.class, mid);
			if(menu == null)
			{
				// 查找不到则返回找不到菜单信息
				response.getWriter().print("nomenu");
			}
			else
			{
				Object sysObj=request.getSession().getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					String corpCode =lfSysuser.getCorpCode();
					
					EmpExecutionContext.info(opModule, corpCode, 
							lfSysuser.getUserId().toString(), lfSysuser.getUserName(), 
							"删除菜单(id:"+mid+")", StaticValue.DELETE);
					//EmpExecutionContext.info("模块名称：" + opModule + "，企业："+corpCode+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，删除菜单(id:"+mid+")");
				}
				response.getWriter().print(menuBiz.delMenu(menu));
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "调用删除微信" + opModule + "异常！");
		}
	}

	/**
	 * 修改菜单名称
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void updateMenuName(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		// 父级菜单ID，为0则表示为一级菜单
		String pid = request.getParameter("pid");
		// 菜单id
		String mid = request.getParameter("mid");
		// 修改后的名称
		String mname = request.getParameter("mname");
		// 一级菜单添加时字节长度判断为8
		if("0".equals(pid) && mname.getBytes("GBK").length > 8)
		{
			response.getWriter().print("overlength");
			return;
		}
		// 二级菜单添加时字节长度判断为16
		else
			if(mname.getBytes("GBK").length > 16)
			{
				response.getWriter().print("overlength");
				return;
			}
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();

		conditionMap.put("MId", mid);
		objectMap.put("mname", mname);

		try
		{
			Object sysObj=request.getSession().getAttribute("loginSysuser");
			if(sysObj!=null){
				LfSysuser lfSysuser=(LfSysuser)sysObj;
				String corpCode =lfSysuser.getCorpCode();
				
				EmpExecutionContext.info(opModule, corpCode, 
						lfSysuser.getUserId().toString(), lfSysuser.getUserName(), 
						"修改菜单名称(id:"+mid+"，修改后的名称:"+mname+")成功", StaticValue.UPDATE);
				//EmpExecutionContext.info("模块名称：" + opModule + "，企业："+corpCode+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，修改菜单名称(id:"+mid+"，修改后的名称:"+mname+")成功");
			}
			
			// 返回更新结果
			response.getWriter().print(baseBiz.update(LfWcMenu.class, objectMap, conditionMap));
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "微信修改" + opModule + "名称失败！");
		}

	}

	/**
	 * 调整顺序
	 * 
	 * @param request
	 * @param response
	 */
	public void updateOrder(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = null;
		response.setContentType("text/html");
		try
		{
			out = response.getWriter();
			// 原调整的菜单顺序1
			String mId1 = request.getParameter("mid1");
			// 原调整的菜单顺序2
			String mId2 = request.getParameter("mid2");
			// 判断是否为无效的字符串
			if(!GlobalMethods.isInvalidString(mId1) && !GlobalMethods.isInvalidString(mId2))
			{
				boolean suc = menuBiz.updateOrder(mId1, mId2);
				if(suc)
				{
					Object sysObj=request.getSession().getAttribute("loginSysuser");
					if(sysObj!=null){
						LfSysuser lfSysuser=(LfSysuser)sysObj;
						String corpCode =lfSysuser.getCorpCode();
						
						EmpExecutionContext.info(opModule, corpCode, 
								lfSysuser.getUserId().toString(), lfSysuser.getUserName(), 
								"调整顺序(原调整的菜单顺序1:"+mId1+"，原调整的菜单顺序2:"+mId2+")成功", StaticValue.UPDATE);
						//EmpExecutionContext.info("模块名称：" + opModule + "，企业："+corpCode+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，调整顺序(原调整的菜单顺序1:"+mId1+"，原调整的菜单顺序2:"+mId2+")成功");
					}
					out.print("@菜单顺序更新成功！");
				}
				else
				{
					Object sysObj=request.getSession().getAttribute("loginSysuser");
					if(sysObj!=null){
						LfSysuser lfSysuser=(LfSysuser)sysObj;
						String corpCode =lfSysuser.getCorpCode();
						EmpExecutionContext.error("模块名称：" + opModule + "，企业："+corpCode+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，调整顺序(原调整的菜单顺序1:"+mId1+"，原调整的菜单顺序2:"+mId2+")失败");
					}
					out.print("#菜单顺序更新失败！");
				}
			}
		}
		catch (IOException e)
		{
			EmpExecutionContext.error(e, "菜单顺序更新发生异常！");
		}
	}

	/**
	 * 保存公众号APPid信息
	 * 
	 * @param request
	 * @param response
	 */
	public void saveAppId(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = null;
		response.setContentType("text/html");
		try
		{
			out = response.getWriter();
			// 公众帐号id
			String aId = request.getParameter("aid");
			if(!GlobalMethods.isInvalidString(aId))
			{
				String appId = request.getParameter("appid");
				String secret = request.getParameter("secret");
				// 判断合法性以及长度
				String judge1 = CommonUtils.judgeRuleAndLength("Appid", appId, 1, 64);
				if(judge1 != null)
				{
					out.print(judge1);
					return;
				}
				String judge2 = CommonUtils.judgeRuleAndLength("AppSecret", secret, 1, 64);
				if(judge2 != null)
				{
					out.print(judge2);
					return;
				}
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
				objectMap.put("appId", appId);
				objectMap.put("secret", secret);
				conMap.put("AId", aId);
				// 更新APPID信息
				boolean suc = accountBiz.updateAccount(objectMap, conMap);
				if(suc)
				{
					Object sysObj=request.getSession().getAttribute("loginSysuser");
					if(sysObj!=null){
						LfSysuser lfSysuser=(LfSysuser)sysObj;
						String corpCode =lfSysuser.getCorpCode();
						
						EmpExecutionContext.info(opModule, corpCode, 
								lfSysuser.getUserId().toString(), lfSysuser.getUserName(), 
								"保存公众号APPid信息(公众帐号id:"+aId+")成功", StaticValue.ADD);
						//EmpExecutionContext.info("模块名称：" + opModule + "，企业："+corpCode+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，保存公众号APPid信息(公众帐号id:"+aId+")成功");
					}
					out.print("信息保存成功！");
				}
				else
				{
					Object sysObj=request.getSession().getAttribute("loginSysuser");
					if(sysObj!=null){
						LfSysuser lfSysuser=(LfSysuser)sysObj;
						String corpCode =lfSysuser.getCorpCode();
						EmpExecutionContext.error("模块名称：" + opModule + "，企业："+corpCode+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，保存公众号APPid信息(公众帐号id:"+aId+")失败");
					}
					out.print("信息保存失败！");
				}
			}
			else
			{	
				Object sysObj=request.getSession().getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					String corpCode =lfSysuser.getCorpCode();
					EmpExecutionContext.error("模块名称：" + opModule + "，企业："+corpCode+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，未获取到公众帐号");
				}
				out.print("未获取到公众帐号！");
			}
		}
		catch (IOException e)
		{
			EmpExecutionContext.error(e, "公众帐号更新失败！");
		}

	}

	/**
	 * 设置动作或修改动作
	 * 
	 * @param request
	 * @param response
	 */
	public void setAction(HttpServletRequest request, HttpServletResponse response)
	{
		// 菜单id
		String menuId = request.getParameter("mid");
		// 动作类型
		String mType = request.getParameter("mtype");
		// 动作跳转url
		String url = request.getParameter("url");
		// 关联模板id
		String tId = request.getParameter("tid");
		// 关联模板事件
		response.setContentType("text/html");
		try
		{
			PrintWriter out = response.getWriter();
			if(!GlobalMethods.isInvalidString(menuId))
			{
				LfWcMenu menu = baseBiz.getById(LfWcMenu.class, Long.valueOf(menuId));
				if(!GlobalMethods.isInvalidString(mType))
				{
					menu.setMtype(Integer.parseInt(mType));
					if("2".equalsIgnoreCase(mType))
					{
						menu.setMurl(url);
						menu.setTId(0L);
						menu.setMsgXml("");
						menu.setMsgText("");
					}
					if("1".equals(mType))
					{
						// 没选模板，模板id设为0
						menu.setTId(0l);
						// 模板id为空或为0,表示没有关联模板
						if(tId != null && !"".equals(tId) && !"0".equals(tId))
						{
							// 模板类型的回复直接去模板表取msgxml
							LfWcTemplate wcTemplate;
							try
							{
								// 同步更新模板信息
								wcTemplate = baseBiz.getById(LfWcTemplate.class, tId);
								if(wcTemplate != null)
								{
									String msgXml = wcTemplate.getMsgXml();
									String msgText = wcTemplate.getMsgText();
									menu.setMsgText(msgText);
									menu.setMsgXml(msgXml);
								}
								menu.setTId(Long.valueOf(tId));
								menu.setMurl("");
							}
							catch (Exception e)
							{
								EmpExecutionContext.error(e, "模板没有找到！");
							}
						}
					}
					boolean result = baseBiz.updateObj(menu);
					if(result)
					{
						Object sysObj=request.getSession().getAttribute("loginSysuser");
						if(sysObj!=null){
							LfSysuser lfSysuser=(LfSysuser)sysObj;
							String corpCode =lfSysuser.getCorpCode();
							
							EmpExecutionContext.info(opModule, corpCode, 
									lfSysuser.getUserId().toString(), lfSysuser.getUserName(), 
									"设置动作(菜单id:"+menuId+")成功", StaticValue.OTHER);
							//EmpExecutionContext.info("模块名称：" + opModule + "，企业："+corpCode+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，设置动作(菜单id:"+menuId+")成功");
						}
						out.print("@设置成功！");
					}
					else
					{
						Object sysObj=request.getSession().getAttribute("loginSysuser");
						if(sysObj!=null){
							LfSysuser lfSysuser=(LfSysuser)sysObj;
							String corpCode =lfSysuser.getCorpCode();
							EmpExecutionContext.error("模块名称：" + opModule + "，企业："+corpCode+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，设置动作(菜单id:"+menuId+")失败");
						}
						out.print("设置失败！");
					}
				}
				else
				{
					// 未知的动作类型
					out.print("未知的动作类型！");
				}
			}
			else
			{
				// 未获取到menuid
				out.print("未知的菜单！");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置动作失败！");
		}

	}

	/**
	 * 获取图文模板名称
	 * 
	 * @param request
	 * @param response
	 */
	public void getTempName(HttpServletRequest request, HttpServletResponse response)
	{
		// 图文模板ID
		String tid = request.getParameter("tid");
		response.setContentType("text/html");
		try
		{
			String tempName = "";
			// 查找对应的图文模板信息
			LfWcTemplate template = baseBiz.getById(LfWcTemplate.class, tid);
			if(template != null)
			{
				tempName = template.getName();
			}
			// 返回名称
			response.getWriter().print(tempName);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取图文模板名称失败！");
		}
	}

	/**
	 * 发布菜单
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void release(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// 公众帐号
		String aid = request.getParameter("aid");
		// Appid
		String appid = request.getParameter("appid");
		// AppSecret
		String secret = request.getParameter("secret");
		// 获取token请求地址
		String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
		// 发布菜单请求地址
		String postUrl = "https://api.weixin.qq.com/cgi-bin/menu/create";
		// 输出对象
		PrintWriter out = null;
		response.setContentType("text/html");
		try
		{
			out = response.getWriter();
			// 查询公众帐号对应的菜单
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("AId", aid);
			LfWcAccount account = baseBiz.getById(LfWcAccount.class, Long.valueOf(aid));
			
			// 获取token和AppSecret读数据库中的值
			String token = account.getAccessToken();
			// 公众帐appId
			appid = account.getAppId();
			// 公众帐appSecret
			secret = account.getSecret();
			// 上次获取的时间
			Timestamp accessTime = account.getAccessTime();
			// 错误编码
			String errcode = null;
			// 错误消息
			String errmsg = null;
			
			// 第一步：判断token是否有效，如果无效，重新发送请求获取token的值
			// 当前token不能使用 不存在或已失效
			if(!trustSSL.isAlive(token, accessTime))
			{ // 重新 获取token
				JSONObject json = trustSSL.getRemoteAccessToken(accessTokenUrl, appid, secret);
				token = (String) json.get("access_token");
				// 成功取到token
				if(token != null)
				{
					account.setAccessToken(token);
					account.setAccessTime(new Timestamp(new Date().getTime()));
					// 保存信息到库
					baseBiz.updateObj(account);
				}
				else
				{
					// 获取token失败
					errcode = String.valueOf(json.get("errcode"));
					errmsg = (String) json.get("errmsg");
				}
			}
			
			// 第二步：保证token有效的情况下，提交发送菜单请求，负责不用调用发布菜单接口
			//token有效和不为空的时候才执行
			if(token != null)
			{
				// 公众帐号对应的菜单json格式
				JSONObject jsonMenu = menuBiz.getMenuJsonByAccount(aid);
				
				// 执行发布请求
				JSONObject result = trustSSL.createRemoteMenu(postUrl, jsonMenu.toString(), token);
				errcode = String.valueOf(result.get("errcode"));
				errmsg = (String) result.get("errmsg");
			}
			
			// 第三部：对errcode进行判断，如果为“0”，表明发布成功；
			if("0".equals(errcode))
			{
				Object sysObj=request.getSession().getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					
					EmpExecutionContext.info(opModule, lfSysuser.getCorpCode(), 
							lfSysuser.getUserId().toString(), lfSysuser.getUserName(), 
							"发布菜单(公众帐号:"+aid+")成功", StaticValue.OTHER);
					//EmpExecutionContext.info("模块名称：" + opModule + "，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，发布菜单(公众帐号:"+aid+")成功");
				}
				out.print("发布成功！");
			}
			else
			{	
				Object sysObj=request.getSession().getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					EmpExecutionContext.error("模块名称：" + opModule + "，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，发布菜单(公众帐号:"+aid+")失败");
				}
				//根据返回错误码 若因为token的原因导致发布失败 则置空token 使其下次重新获取
				if(token!=null&&(errcode.matches("4[0-2]001")||"40014".equals(errcode)))
				{
					account.setAccessToken("");
					baseBiz.updateObj(account);
				}
				try
				{
					String message = PropertiesUtil.getValue(errcode);
					if(!GlobalMethods.isInvalidString(message))
					{
						out.print(message + "！");
					}
					else
					{
						out.print(errmsg + "！");
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "加载message配置信息失败！");
					out.print(errmsg + "！");
				}
			}
		}
		catch (Exception e)
		{
			if(out != null){
			out.print("发布失败！");
			}
			EmpExecutionContext.error(e, "公众帐号发布失败！");
		}
	}
}
