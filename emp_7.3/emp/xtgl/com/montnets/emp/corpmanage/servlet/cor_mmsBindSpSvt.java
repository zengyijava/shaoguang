package com.montnets.emp.corpmanage.servlet;



import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.corpmanage.biz.CorpSpUserBindBiz;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;


/**
 * 彩信账户绑定
 * @project p_xtgl
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-3-5 上午11:44:37
 * @description
 */
public class cor_mmsBindSpSvt extends BaseServlet {
	
	private final String empRoot="xtgl";
	private final String basePath="/corpmanage";

	private final BaseBiz baseBiz=new BaseBiz();
	//操作模块
	private final String opModule= "彩信SP账户绑定";

	/**
	 * 
	 */
	private static final long serialVersionUID = 8224206407518725383L;
	
		
	public void find(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		List<LfMmsAccbind> accbindList = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			//当前登录用户
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			//操作员企业编码
			String lgCorpCode = sysuser.getCorpCode();
			//不为100000的企业编码不允许修改
			if(lgCorpCode != null && "100000".equals(lgCorpCode))
			{
				//是否第一次打开
				boolean isFirstEnter = false;
				PageInfo pageInfo=new PageInfo();
				isFirstEnter=pageSet(pageInfo, request);
				if(!isFirstEnter){
					String cropNum = request.getParameter("cropNum");
					String mmsUser = request.getParameter("mmsUser");
					if(cropNum != null && !"".equals(cropNum)){
						conditionMap.put("corpCode&like", cropNum);
					}
					if(mmsUser != null && !"".equals(mmsUser)){
						conditionMap.put("mmsUser&like", mmsUser);
					}
				}
				accbindList = baseBiz.getByConditionNoCount(LfMmsAccbind.class, null, conditionMap, null, pageInfo);
				request.setAttribute("accbindList", accbindList);
				request.setAttribute("pageInfo", pageInfo);
				request.setAttribute("conditionMap", conditionMap);
			}
			else
			{
				EmpExecutionContext.error("查询彩信账户企业绑定关系异常，操作员企业编码非管理级企业，操作员企业编码："+lgCorpCode);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询彩信账户企业绑定关系异常！");
		}finally{
				request.getRequestDispatcher(this.empRoot+this.basePath+"/cor_mmsBindSp.jsp")
				.forward(request, response);
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
			//当前登录用户
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			//操作员企业编码
			String lgCorpCode = sysuser.getCorpCode();
			//不为100000的企业编码不允许修改
			if(lgCorpCode != null && "100000".equals(lgCorpCode))
			{
				List<LfCorp> lbList = baseBiz.getByCondition(LfCorp.class, null, null);	//企业
				
				CorpSpUserBindBiz sbs = new CorpSpUserBindBiz();
				//获取未绑定的彩信账号
				List<GtPortUsed> gpList = sbs.getUnBindEmpMMsUserID();
				//返回结果到前台
				request.setAttribute("gpList", gpList);
				request.setAttribute("lbList", lbList);
			}
			else
			{
				EmpExecutionContext.error("新增帐户绑定页面跳转 异常，操作员企业编码非管理级企业，操作员企业编码："+lgCorpCode);
			}
			request.getRequestDispatcher(this.empRoot + this.basePath + "/cor_addMmsBindSp.jsp")
				.forward(request, response);
		}
		catch(Exception e){
			EmpExecutionContext.error(e,"新增帐户绑定页面跳转 异常！");
		}
	}
	
	
	
	/**
	 * 新增彩信帐户绑定
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void add(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		
		Integer addNum = 0;
		String loginCorpCode = "";
		String loginUserName= "";
		String userId = "";
		try
		{
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			loginCorpCode=lfSysuser.getCorpCode();
			loginUserName=lfSysuser.getUserName();
			userId = lfSysuser.getUserId().toString();
			//不为100000的企业编码不允许修改
			if(loginCorpCode != null && "100000".equals(loginCorpCode))
			{
				String corpCode = request.getParameter("corpCode");
				String mmsUser = request.getParameter("mmsUseres");
				List<LfMmsAccbind> accbinds = new ArrayList<LfMmsAccbind>();
				if(!"".equals(mmsUser)){
					mmsUser = mmsUser.substring(0, mmsUser.length()-1);
					String[] arr = mmsUser.split(",");
					LfMmsAccbind mmsAccbind = null;
					for(int i=0;i<arr.length;i++){
						mmsAccbind= new LfMmsAccbind();
						mmsAccbind.setCorpCode(corpCode);
						mmsAccbind.setMmsUser(arr[i]);
						mmsAccbind.setIsValidate(1);
						accbinds.add(mmsAccbind);
					}
					addNum = baseBiz.addList(LfMmsAccbind.class, accbinds);
					
					EmpExecutionContext.info(opModule, loginCorpCode, userId, loginUserName, 
							"新增企业["+corpCode+"]与彩信发送账户("+mmsUser+")绑定成功！", StaticValue.ADD);
				}
			}
			else
			{
				EmpExecutionContext.error("模块：彩信账户绑定，企业编码["+loginCorpCode+"],登录账号["
											+loginUserName+"]的操作员新增企业与彩信发送账户绑定失败！，操作员企业编码非管理级企业，操作员企业编码："
											+loginCorpCode);
			}
			response.getWriter().print(addNum);
		}
		catch(Exception e){
			response.getWriter().print("");
			EmpExecutionContext.error(e,"模块：彩信账户绑定，企业编码["+loginCorpCode+"],登录账号["+loginUserName+"]的操作员新增企业与彩信发送账户绑定失败！");
		}
	}
	
	
	/**
	 * 更新彩信帐户绑定  的激活 失效
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void update(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		
		boolean updateResult = false;
		try
		{
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String loginCorpCode=lfSysuser.getCorpCode();
			String loginUserName=lfSysuser.getUserName();
			//不为100000的企业编码不允许修改
			if(loginCorpCode != null && "100000".equals(loginCorpCode))
			{
				String type = request.getParameter("type");
				String mmsId = request.getParameter("mmsId");
				LfMmsAccbind mmsAccbind = baseBiz.getById(LfMmsAccbind.class, mmsId);
				if(mmsAccbind != null){
					if("1".equals(type)){ 
						//失效
						mmsAccbind.setIsValidate(0);
					}else if("2".equals(type)){	
						//激活
						mmsAccbind.setIsValidate(1);
					}
					updateResult = baseBiz.updateObj(mmsAccbind);
					
					String operatorType="";
					if("1".equals(type)){
						operatorType="失效";
					}else{
						operatorType="激活";
					}
					
					EmpExecutionContext.info(opModule, loginCorpCode, lfSysuser.getUserId().toString(), loginUserName, 
							"修改企业，id["+mmsId+"]与彩信发送账户绑定成功," + operatorType, StaticValue.UPDATE);
				}
				else
				{
					EmpExecutionContext.error("更新彩信帐户绑定异常，操作员企业编码非管理级企业，操作员企业编码："+loginCorpCode);
				}
			}
			response.getWriter().print(updateResult);
		}
		catch(Exception e){
			response.getWriter().print("");
			EmpExecutionContext.error(e,"更新彩信帐户绑定异常！");
		}
	}


}
