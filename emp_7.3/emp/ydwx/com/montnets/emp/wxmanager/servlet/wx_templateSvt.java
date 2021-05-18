package com.montnets.emp.wxmanager.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXSORT;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxmanager.biz.WxTemplateBiz;

@SuppressWarnings("serial")
public class wx_templateSvt extends BaseServlet {
	private static final String PATH = "/ydwx/manageWX/";

	private final BaseBiz baseBiz = new BaseBiz();
	
	/**
	 * 查询方法
	 * 
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher(PATH + "templateManger.jsp").forward(
					request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"进入网讯管理页面异常！");
		}
	}
	
	/***
	 * 查询网讯基本信息
	 * @param request
	 * @param response
	 */
	public void getTable(HttpServletRequest request,
			HttpServletResponse response) {
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//Long lguserid = Long.parseLong(request.getParameter("lguserid"));
		String sortId = request.getParameter("sortId");
		String lgcorpcode = request.getParameter("lgcorpcode");
		String mbid = request.getParameter("mbid");
		String mbname = request.getParameter("mbname");
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		
		LfWXBASEINFO baseInfo = new LfWXBASEINFO();
		if(mbid != null && !"".equals(mbid)){
			baseInfo.setNETID(Long.parseLong(mbid));
		}
		if(sortId != null && !"".equals(sortId)){
			baseInfo.setSORT(Long.parseLong(sortId));
		}
		baseInfo.setNAME(mbname);
		PageInfo pageInfo = new PageInfo();
		try {
			
			pageSet(pageInfo,request);
			
			List<DynaBean> beans = new WxTemplateBiz().getTemplates(baseInfo,lgcorpcode,
					pageInfo);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("beans", beans);
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+pageInfo.getTotalRec();
				Object sysObj=request.getSession(false).getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					EmpExecutionContext.info("网讯模板", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), opContent, "GET");
				}
			}
			request.getRequestDispatcher(PATH + "templateTable.jsp").forward(
					request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询Lf_WX_BASEINFO信息");
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			try {
				request.getRequestDispatcher(PATH + "templateTable.jsp")
						.forward(request, response);
			} catch (ServletException e1) {
				EmpExecutionContext.error(e1, "页面跳转异常！");
			} catch (IOException e1) {
				EmpExecutionContext.error(e1, "查询网讯基本信息异常！");
			}
		}
	}

	/**
	 * 得到模板类型
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	public void getSort(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// String loginName = request.getParameter("lgusername");
		String corpCode = request.getParameter("lgcorpcode");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", corpCode);
		conditionMap.put("TYPE", "0");
		StringBuffer tree = null;
		try {

			List<LfWXSORT> list = baseBiz.getByCondition(LfWXSORT.class,
					conditionMap, null);
			if (list == null || list.size() == 0) {
				tree = new StringBuffer("[");
				tree.append("{");
				tree.append("id:0");
				tree.append(",name:'"+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_35",request)+"'");
				tree.append(",isParent:").append(true);
				tree.append("}]");
			} else {
				tree = new StringBuffer("[");
				tree.append("{");
				tree.append("id:0");
				tree.append(",name:'"+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_35",request)+"'");
				tree.append(",isParent:").append(true);
				tree.append("},");
				LfWXSORT sort = null;
				for (int i = 0; i < list.size(); i++) {
					sort = list.get(i);
					tree.append("{");
					tree.append("id:").append(sort.getID());
					tree.append(",pId:0");
					tree.append(",name:'").append(sort.getNAME()).append("'");
					tree.append(",isParent:").append(false);
					tree.append("}");
					if (i != list.size() - 1) {
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());

		} catch (SQLException e) {
			EmpExecutionContext.error(e,"得到模板类型异常！");
			response.getWriter().print("[]");
		}
	}

	/**
	 * 得到模板类型数量
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	public void getSortCount(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String corpCode = request.getParameter("lgcorpcode");
		String sortId = request.getParameter("sortId");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("CORPCODE", corpCode);
		conditionMap.put("SORT", sortId);
		try {
			List<LfWXBASEINFO> list = baseBiz.getByCondition(
					LfWXBASEINFO.class, conditionMap, null);
			response.getWriter().print(list == null ? 0 : list.size());
		} catch (SQLException e) {
			EmpExecutionContext.error(e,"得到模板类型数量异常！公司编码："+corpCode);
		}

	}

	/**
	 * 更新模板 新增 更新名字 删除
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void updMB(HttpServletRequest request, HttpServletResponse response) {
			String name=request.getParameter("name");
			String id=request.getParameter("id");
			String type=request.getParameter("type");
			String corpCode = request.getParameter("lgcorpcode");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("NAME", name);
			conditionMap.put("TYPE", "0");
			try{
				if("0".equals(type)){
					//新增
					List<LfWXSORT> sorts = baseBiz.getByCondition(LfWXSORT.class, conditionMap,null);
					if(sorts != null && sorts.size()>0){
						response.getWriter().print(2);
						return ;
					}else{
						LfWXSORT sort = new LfWXSORT();
						sort.setNAME(name);
						sort.setCorpCode(corpCode);
						sort.setCREATID(Long.parseLong(lguserid));
						sort.setCREATDATE(new Timestamp(System.currentTimeMillis()));
						sort.setParentId(0L);
						Long result = baseBiz.addObjProReturnId(sort);//需要自增id
						if(result>0){
							//增加操作日志
							Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
							if(loginSysuserObj!=null){
								LfSysuser sysuser=(LfSysuser)loginSysuserObj;
								EmpExecutionContext.info("网讯模板", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "新增类型成功。[类型名称]（"+name+"）", "ADD");
							}
							response.getWriter().print(1);
						}else{
							//增加操作日志
							Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
							if(loginSysuserObj!=null){
								LfSysuser sysuser=(LfSysuser)loginSysuserObj;
								EmpExecutionContext.info("网讯模板", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "新增类型失败。[类型名称]（"+name+"）", "ADD");
							}
							response.getWriter().print(0);
						}
						
					}
				}else if("1".equals(type)){
					//修改
					List<LfWXSORT> sorts = baseBiz.getByCondition(LfWXSORT.class, conditionMap,null);
					if(sorts != null && sorts.size()>0){
						response.getWriter().print(2);
						return ;
					}else{
						LfWXSORT sort = baseBiz.getById(LfWXSORT.class, id);
						String beforeName="";
						if(sort!=null){
							beforeName=sort.getNAME();
							sort.setNAME(name);
							sort.setMODIFYDATE(new Timestamp(System.currentTimeMillis()));
						}
						boolean result = baseBiz.updateObj(sort);
						if(result){
							//增加操作日志
							Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
							if(loginSysuserObj!=null){
								LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
								EmpExecutionContext.info("网讯模板", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改类型成功。[类型名称]("+beforeName+"）->("+name+"）", "UPDATE");
							}
							response.getWriter().print(1);
						}else{
							//增加操作日志
							Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
							if(loginSysuserObj!=null){
								LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
								EmpExecutionContext.info("网讯模板", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改类型失败。[类型名称]("+beforeName+"）->("+name+"）", "UPDATE");
							}
							response.getWriter().print(0);
						}
					}
				}else if("2".equals(type)){
					LfWXSORT sort = baseBiz.getById(LfWXSORT.class, id);
					String beforeName="";
					if(sort!=null){
						beforeName=sort.getNAME();
					}
					Integer result = baseBiz.deleteByIds(LfWXSORT.class, id);
					if(result >0){
						//增加操作日志
						Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
						if(loginSysuserObj!=null){
							LfSysuser sysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("网讯模板", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "删除类型成功。[类型名称]（"+beforeName+"）", "DELETE");
						}
						response.getWriter().print(1);
					}
				}else if("3".equals(type)){
					LfWXBASEINFO info = baseBiz.getById(LfWXBASEINFO.class, id);
					String beforeName="";
					if(info!=null){
						beforeName=info.getNAME();
					}
					boolean result = new WxTemplateBiz().deleteTemplate(id);
					if(result){
						//增加操作日志
						Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
						if(loginSysuserObj!=null){
							LfSysuser sysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("网讯模板", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "删除网讯模板成功。[网讯名称]（"+beforeName+"）", "DELETE");
							
						}
						response.getWriter().print(1);
					}else{
						//增加操作日志
						Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
						if(loginSysuserObj!=null){
							LfSysuser sysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("网讯模板", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "删除网讯模板失败。[网讯名称]（"+beforeName+"）", "DELETE");
						}
						response.getWriter().print(0);
					}
				}
			}catch(Exception e){
				EmpExecutionContext.error(e,"更新模板,更新名字, 删除");
				try
				{
					response.getWriter().print(0);
				} catch (IOException e1)
				{
					EmpExecutionContext.error(e1,"更新模板异常！");
				}
			}
	}
	
	/****
	 * 修改网讯模板状态
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void cancelType(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String	netid=request.getParameter("netid");
	    LinkedHashMap<String, String> updateMap = new LinkedHashMap<String, String>();
	    updateMap.put("SORT", "0");
	    updateMap.put("wxTYPE", "1");
	    LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
	    conMap.put("ID", netid);
	     
		try {
		    boolean result = baseBiz.update(LfWXBASEINFO.class, updateMap, conMap);
		    
			if(result){
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("网讯模板", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改网讯模板状态成功。[网讯ID]("+netid+"）", "UPDATE");
				}
			}else{
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("网讯模板", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改网讯模板状态失败。[网讯ID]("+netid+"）", "UPDATE");
				}
			}
		    
		    response.getWriter().print(result);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"更新操作");
			 response.getWriter().print(false);
		} 
	}
}
