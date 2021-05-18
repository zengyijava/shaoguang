package com.montnets.emp.corpmanage.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.corpmanage.biz.CorpSpUserBindBiz;
import com.montnets.emp.corpmanage.vo.LfSpCorpBindVo;
import com.montnets.emp.entity.corp.GwUserproperty;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

@SuppressWarnings("serial")
public class cor_appSvt extends BaseServlet
{
	private final CorpSpUserBindBiz sbs = new CorpSpUserBindBiz();
	private final WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
	
	private final String empRoot="xtgl";
	private final String basePath="/corpmanage";
	private final BaseBiz baseBiz=new BaseBiz();
	//验证SP账号类
	private final SmsBiz						smsBiz				= new SmsBiz();
	//操作模块
	private final String opModule= "短信SP账户绑定";

	
	
    /**
     * 查询方法
     * @param request
     * @param response
     * @throws IOException 
     * @throws ServletException 
     */
	public void find(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		List<LfSpCorpBindVo> corpList = null;
		PageInfo pageInfo=new PageInfo();
		try {
			//当前登录用户
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			//操作员企业编码
			String lgCorpCode = sysuser.getCorpCode();
			//不为100000的企业编码不允许查询
			if(lgCorpCode != null && "100000".equals(lgCorpCode))
			{
				//是否第一次打开
				boolean isFirstEnter = false;
				//判断是否首次进入并设置分页
				isFirstEnter=pageSet(pageInfo, request);
				LfSpCorpBindVo lbb = new LfSpCorpBindVo();
				//获取页面查询条件信息
				String corpName = request.getParameter("cropName");
				String corpCode = request.getParameter("cropCode");
				String atype = request.getParameter("atype");
				String aid = request.getParameter("aid3");
				String aid1 = request.getParameter("aid1");
				String aid2 = request.getParameter("aid2");
				//非第一次进入，根据查询条件过滤信息
				if(!isFirstEnter)
				{
					if (corpName != null && !"".equals(corpName))
					{
						lbb.setCorpName(corpName);
					}
					if (corpCode != null && !"".equals(corpCode))
					{
						lbb.setCorpCode(corpCode);
					}
					if (atype != null && !"3".equals(atype))
					{
						lbb.setPlatFormType(Integer.parseInt(atype));
					}
					if ("1".equals(atype))
					{
						if (aid1 != null && !"".equals(aid1))
						{
							lbb.setSpUser(aid1);
						}
					}
					else if ("2".equals(atype))
					{
						if (aid2 != null && !"".equals(aid2))
						{
							lbb.setSpUser(aid2);
						}
					}
					else if ("3".equals(atype))
					{
						if (aid != null && !"".equals(aid))
						{
							lbb.setSpUser(aid);
						}
					}
				}
				//根据过滤条件获取数据
				corpList = sbs.getSpCorpBindVos(lbb, pageInfo);
				//获取sp账号
				List<Userdata> udataList = wgMsgBiz.getAllUserdata("0","1");
				//获取企业信息
				List<LfCorp> lbList = baseBiz.getByCondition(LfCorp.class, null, null);
				//获取所有的企业账户
				List<GtPortUsed> gpList = sbs.findAllEmpUserID();
				//获取
				List<GtPortUsed> dbList = sbs.findAllDBServerUserID();
				//将结果返回前台
				request.setAttribute("lbList", lbList);
				request.setAttribute("gpList", gpList);
				request.setAttribute("dbList", dbList);
				request.setAttribute("lsbv", lbb);
				request.setAttribute("udataList", udataList);
			}
			else
			{
				EmpExecutionContext.error("查询短信SP账号绑定异常，操作员企业编码非管理级企业，操作员企业编码："+lgCorpCode+",操作员ID："+sysuser.getUserId());
				//不是100000企业请求，跳转到登录页面。
				response.sendRedirect(request.getContextPath() + "/common/logoutEmp.html");
				return;
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询企业与发送账户绑定关系异常！");
		}
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("corpList", corpList);
		request.getRequestDispatcher(this.empRoot+this.basePath+"/cor_BindSp.jsp")
			.forward(request, response);
	}
	
	/**
	 * 新增方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void add(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		
		//获取前台页面控件内容
		String busCode = request.getParameter("busCode");
		String bustype = request.getParameter("bustype");
		String ids = request.getParameter("ids");
		List<LfSpDepBind> idList = new ArrayList<LfSpDepBind>();
		LfSpDepBind lsdb = null;
		String loginCorpCode= "";
		String loginUserName= "";
		//是否允许绑定
		boolean isAllowAdd=true;
		//未绑定状态报告URL的SP账号
		String spUsers="";
		try
		{
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			
			loginCorpCode=lfSysuser.getCorpCode();
			loginUserName=lfSysuser.getUserName();
			//不为100000的企业编码不允许查询
			if(loginCorpCode != null && "100000".equals(loginCorpCode))
			{
				if (ids != null && !"".equals(ids))
				{
					for (String s : ids.split(","))
					{
						lsdb = new LfSpDepBind();
						lsdb.setCorpCode(busCode);
						lsdb.setBindType(0);
						lsdb.setPlatFormType(Integer.parseInt(bustype));
						lsdb.setSpUser(s);
						lsdb.setIsValidate(1);
						//100000企业绑定SP账号需要配置状态报告推送URL
						if("100000".equals(busCode)){
							// 判断spuser的url是否可用，不需要上行URL,需要状态报告URL
							String result=smsBiz.checkSpUser(s, 2, 1);
							//验证不成功
							if(!"checksuccess".equals(result)){
								isAllowAdd=false;
								spUsers=spUsers+s+",";
							}
						}
						idList.add(lsdb);
					}
				}
				int r = 0;
				//允许绑定
				if(isAllowAdd)
				{
					//单企业
					if(StaticValue.getCORPTYPE() ==0)
					{
					r=baseBiz.addList(LfSpDepBind.class, idList);
					}
					else
					{
						//多企业
						List<GwUserproperty> gwUserpropertyList=new ArrayList<GwUserproperty>();
						for (int i = 0; i < idList.size(); i++) 
						{
							GwUserproperty gwUserproperty=new GwUserproperty();
							//设置默认值
							gwUserproperty.setEcid(Integer.parseInt(idList.get(i).getCorpCode()));
							gwUserproperty.setUserid(idList.get(i).getSpUser());
							gwUserproperty.setPwdencode(1);
							gwUserproperty.setPwdencodestr("00000000");
							gwUserproperty.setMsgcode(1);
							gwUserproperty.setMsgencode(1);
							gwUserproperty.setPushmofmt(2);
							gwUserproperty.setPushrptfmt(2);
							gwUserproperty.setPushpwdencode(0);
							gwUserproperty.setPushpwdencodestr("00000000");
							gwUserproperty.setPushmsgcode(1);
							gwUserproperty.setPushmsgencode(1);
							gwUserproperty.setPushfailcnt(3);
							gwUserproperty.setPushslidewnd(5);
							gwUserproperty.setPushmomaxcnt(100);
							gwUserproperty.setPushrptmaxcnt(100);
							gwUserproperty.setGetmomaxcnt(100);
							gwUserproperty.setGetrptmaxcnt(100);
							gwUserproperty.setReserve("");
							gwUserpropertyList.add(gwUserproperty);
						}
						//托管版EMP短信SP账号绑定，保存绑定关系和GwUserproperty
						r=sbs.addLfSpDepBindList(idList, gwUserpropertyList);
					}
				}else{
					//截取到最后一个逗号
					spUsers=spUsers.substring(0, spUsers.length()-1);
				}
				if (r > 0)
				{
					EmpExecutionContext.info(opModule, loginCorpCode, lfSysuser.getUserId().toString(), 
							loginUserName, "新增企业绑定短信SP帐号，企业编号：" + busCode + "短信发送账户：" + ids, StaticValue.ADD);
					//EmpExecutionContext.info("模块:短信SP账户绑定,企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员新增企业["+busCode+"]与短信发送账户("+ids+")绑定成功！");
					response.getWriter().print("true");
				} else {
					//EmpExecutionContext.error("模块:短信SP账户绑定,企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员新增企业["+busCode+"]与短信发送账户("+ids+")绑定成功！");
					if(!isAllowAdd){
						EmpExecutionContext.info(opModule, loginCorpCode, lfSysuser.getUserId().toString(), 
								loginUserName, "新增企业绑定短信SP帐号，企业编号：" + busCode + "短信发送账户：(" +ids+")绑定失败！"+spUsers+"这些SP账号没有绑定状态报告URL", StaticValue.ADD);
						response.getWriter().print(spUsers);
					}
				}
			}
			else
			{
				EmpExecutionContext.error("模块:短信SP账户绑定,企业编码["+loginCorpCode+"],登录账号["
											+loginUserName+"]的操作员新增企业["+busCode+"]与短信发送账户("
											+ids+")绑定失败！操作员企业编码非管理级企业，操作员企业编码："+loginCorpCode);
			}
		}catch(Exception e){
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"模块:短信SP账户绑定,企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员新增企业["+busCode+"]与短信发送账户("+ids+")绑定失败！");
		}
	}
	/**
	 * 删除方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delete(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		
		//删除前台页面控件信息
		String busCode = request.getParameter("busCode");
		String spUser = request.getParameter("SpUser");
		String op = request.getParameter("op");
		
		try
		{
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			
			String loginCorpCode=lfSysuser.getCorpCode();
			String loginUserName=lfSysuser.getUserName();
			//不为100000的企业编码不允许查询
			if(loginCorpCode != null && "100000".equals(loginCorpCode))
			{
				//加载过滤条件
				LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
				conditionMap.put("corpCode", busCode);
				conditionMap.put("spUser", spUser);
				LfSpDepBind lsdb = null;
				//根据过滤条件获取绑定sp账号
				lsdb = baseBiz.getByCondition(LfSpDepBind.class, conditionMap, null).get(0);
				lsdb.setIsValidate(Integer.parseInt(op));
				//修改
				boolean r = baseBiz.updateObj(lsdb);
				
				String operatorType="";
				if("1".equals(op)){
					operatorType="激活";
				}else{
					operatorType="失效";
				}
				
				EmpExecutionContext.info(opModule, loginCorpCode, lfSysuser.getUserId().toString(), 
						loginUserName, "删除企业绑定短信SP帐号，企业编号：" + busCode + "短信发送账户：" + spUser + "," + operatorType, 
						StaticValue.DELETE);
				//EmpExecutionContext.info("模块:短信SP账户绑定,企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员"+operatorType+"短信SP账号"+spUser+"绑定成功！");
				
				response.getWriter().print(r?"true":"false");
			}
			else
			{
				EmpExecutionContext.error("删除企业与发送账户绑定关系异常，操作员企业编码非管理级企业，操作员企业编码："+loginCorpCode);
			}
		}catch(Exception e){
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"删除企业与发送账户绑定关系！");
		}
	}
	/**
	 * 修改方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void update(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//获取前台页面控件值
		String bustype = request.getParameter("bustype");
		String busName = request.getParameter("busName");
		String busId = request.getParameter("busId");
		String ids = request.getParameter("ids");
		
		LfSpDepBind lsdb = null;
		int r = 0;
		
		try
		{
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//操作员企业编码
			String loginCorpCode = lfSysuser.getCorpCode();
			//不为100000的企业编码不允许查询
			if(loginCorpCode != null && "100000".equals(loginCorpCode))
			{
				if (ids != null && !"".equals(ids))
				{
					for (String s : ids.split(","))
					{
						lsdb = new LfSpDepBind();
						lsdb.setBusId(Long.parseLong(busId));
						lsdb.setBusId(Long.parseLong(busName.substring(0, busName.indexOf("&"))));
						lsdb.setBusCode(busName.substring(busName.indexOf("&")+1));
						lsdb.setPlatFormType(Integer.parseInt(bustype));
						lsdb.setSpUser(s);
						boolean re = baseBiz.updateObj(lsdb);
						if (re)
						{
							r += 1;
						}
					}
					if (r == ids.split(",").length)
					{
						response.getWriter().print("true");
					}
				}
				
				EmpExecutionContext.info(opModule, lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(), 
						lfSysuser.getUserName(), "修改企业与发送账户绑定关系，短信发送账户：" + ids,
						StaticValue.UPDATE);
			}
			else
			{
				EmpExecutionContext.error("修改企业与发送账户绑定关系异常，操作员企业编码非管理级企业，操作员企业编码："+loginCorpCode);
			}
		}
		catch(Exception e){
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"修改企业与发送账户绑定关系！");
		}
	}
	
	/**
	 * 新建页面跳转
	 * @param request
	 * @param response
	 */
	public void goAdd(HttpServletRequest request,HttpServletResponse response)
	{
		try
		{
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//操作员企业编码
			String loginCorpCode = lfSysuser.getCorpCode();
			//不为100000的企业编码不允许查询
			if(loginCorpCode != null && "100000".equals(loginCorpCode))
			{
				//获取所有企业信息
				List<LfCorp> lbList = baseBiz.getByCondition(LfCorp.class, null, null);
				//获取企业账户信息
				List<GtPortUsed> gpList = sbs.getUnBindEmpUserID(1);
//				List<GtPortUsed> dbList = sbs.getUnBindDBUserID();
				List<GtPortUsed> dbList = sbs.getUnBindEmpUserID(2);
				//返回结果到前台
				request.setAttribute("gpList", gpList);
				request.setAttribute("dbList", dbList);
				request.setAttribute("lbList", lbList);
			}
			else
			{
				EmpExecutionContext.error("新建企业与发送账户绑定关系异常，操作员企业编码非管理级企业，操作员企业编码："+loginCorpCode);
			}
			//获取跳转页面
			request.getRequestDispatcher(this.empRoot + this.basePath + "/cor_addBindSp.jsp")
				.forward(request, response);
		}
		catch(Exception e){
			EmpExecutionContext.error(e,"跳转企业与账户绑定页面异常！");
		}
	}
	
}
