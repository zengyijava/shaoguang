package com.montnets.emp.rms.templmanage.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.templmanage.biz.RmsShortTemplateBiz;
import com.montnets.emp.rms.vo.LfShortTemplateVo;
import com.montnets.emp.util.PageInfo;

public class RmsShortTemplate extends BaseServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String PATH = "rms/mbgl";
	private final RmsShortTemplateBiz rstlBiz = new RmsShortTemplateBiz();
	
	public void find(HttpServletRequest request, HttpServletResponse response) {
		PageInfo pageInfo = new PageInfo();
		pageSet(pageInfo, request);
		String corpCode = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getCorpCode();
		//企业ID
		long userId = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getUserId();
		LfShortTemplateVo bean = new LfShortTemplateVo();
		bean.setUserId(userId);
		bean.setCorpCode(corpCode);
		try {
			List<LfShortTemplateVo> shortTemplateList = new ArrayList<LfShortTemplateVo>();
			shortTemplateList = rstlBiz.getLfShortTemplate(pageInfo,bean);
			request.setAttribute("shortTemplateList", shortTemplateList);
		
			request.getRequestDispatcher(PATH + "/shoutTemplate.jsp").forward(
					request, response);
		} catch (Exception e) {
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "快捷模板管理跳转失败！");
		}
		
	}
	public void deleteShortTemp(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter pw = null;
		try {
			String tempId = request.getParameter("tempId");
			String lgcorpcode = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getCorpCode();
			//企业ID
			long userId = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getUserId();
			LfShortTemplateVo bean = new LfShortTemplateVo();
			bean.setCorpCode(lgcorpcode);
			bean.setUserId(userId);
			bean.setTempId(Long.valueOf(tempId));
			RmsShortTemplateBiz rstlBiz = new RmsShortTemplateBiz();
			boolean flag = rstlBiz.deleteShortTemp(bean);
			pw = response.getWriter();
			if(flag){
				//修改tf_template表中isshorttemp字段的值（1表示新增，0表示取消）
				int param = 0;
				flag=rstlBiz.updateLfTemplate(Integer.parseInt(tempId),param);
				@SuppressWarnings("unchecked")
				Map<String, List<LfPrivilege>> priMap = (Map<String, List<LfPrivilege>>) request.getSession(false).getAttribute("priMap");
				Map<String, List<LfPrivilege>> priMaps = new HashMap<String, List<LfPrivilege>>();
				Set<String> keys = priMap.keySet();   //此行可省略，直接将map.keySet()写在for-each循环的条件中  
		        for(String key:keys){  
		        	for(int i=0;i<priMap.get(key).size();i++){
		        		if(priMap.get(key).get(i).getMenuCode().equals(tempId)){
		        			priMap.get(key).remove(i);
		        		}
		        		priMaps.put(key, priMap.get(key));
		        	}
		        }  
		        request.getSession(false).setAttribute("priMap", priMaps);
		        pw.write("true");
			}else{
				pw.write("false");
			}
		} catch (IOException e) {
			EmpExecutionContext.error(e, "快捷模板管理删除失败！");
		}finally{
			if(pw!= null){
				pw.close();
			}
		}
		
	}
	
	public void addShortTemp(HttpServletRequest request, HttpServletResponse response) {
		
		PrintWriter pw = null;
		try {
			// 企业编码
			pw = response.getWriter();
			String lgcorpcode = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getCorpCode();
			//企业ID
			long userId = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getUserId();
			String tempId = request.getParameter("tempId");
			String tempName = request.getParameter("tempName");
			if (tempName != null && !tempName.equals("")) {
				try {
					tempName = URLDecoder.decode(tempName, "utf-8");
				} catch (Exception e) {
					EmpExecutionContext.error(e, "快捷模板名称乱码处理失败！");
				}
			}
			LfShortTemplateVo bean = new LfShortTemplateVo();
			bean.setCorpCode(lgcorpcode);
			bean.setUserId(userId);
			bean.setTempId(Long.valueOf(tempId));
			bean.setTempName(tempName);
			int number = rstlBiz.getNumber(bean);
			if(number>14){
				EmpExecutionContext.info("快捷模板新增超过十五个！");
				pw.write("num");
				return;
			}
			//根据页面请求信息获取数据信息
			LfShortTemplateVo lfShortTemplateBean = rstlBiz.getLfShortTemplate(bean);
			if(lfShortTemplateBean.getId() != null){
				EmpExecutionContext.info("快捷模板新增模板已存在！");
				pw.write("exist");
				return;
			}
			boolean flag = rstlBiz.addShortTemp(bean);
			if(flag){
				//修改tf_template表中isshorttemp字段的值（1表示新增，0表示取消）
				int param = 1;
//				flag=rstlBiz.updateLfTemplate(Long.valueOf(tempId),param);
				//根据menusite = '/rms_templateMana.htm'获取Lf_Privilege表中的PRIVILEGE_ID用于对比
				String menusite = "/rms_templateMana.htm";
				String resourceId = rstlBiz.getPrivilegeId(menusite,bean);
				long resourceIds= resourceId == null ? 93L:Long.valueOf(resourceId);
				List<LfPrivilege> list = new ArrayList<LfPrivilege>();
				LfPrivilege lfPrivilege = new LfPrivilege();
				/*LfShortTemplateVo lfShortTemplate = rstlBiz.getLfShortTemplate(bean);
				lfPrivilege.setMenuCode(String.valueOf(lfShortTemplate.getId()));*/
				lfPrivilege.setMenuCode(tempId);
				lfPrivilege.setMenuName(tempName);
				lfPrivilege.setMenuSite("/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tempId);
				lfPrivilege.setModName("我的快捷场景");
                lfPrivilege.setZhHkModName("My shortcut");
                lfPrivilege.setZhTwModName("我的快捷場景");
				lfPrivilege.setResourceId(resourceIds);
				list.add(lfPrivilege);
				@SuppressWarnings("unchecked")
				Map<String, List<LfPrivilege>> priMap = (Map<String, List<LfPrivilege>>) request.getSession(false).getAttribute("priMap");
				Set<String> keys = priMap.keySet(); 
		        for(String key:keys){ 
		        	if(key.equals(String.valueOf(resourceIds))){
		        		priMap.get(key).addAll(list);
		        	}
		        }  
		        request.getSession(false).setAttribute("priMap", priMap);
		        pw.write("true");
			}else{
				pw.write("false");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "快捷模板新增失败！");
		}finally{
			if(pw!= null){
				pw.close();
			}
		}
		
	}
	
}
