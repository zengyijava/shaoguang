package com.montnets.emp.qyll.servlet;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.GroupBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PageInfo;

@SuppressWarnings("serial")
public class ll_commSvt extends BaseServlet {

	private final DepBiz depBiz = new DepBiz();
	private final BaseBiz baseBiz = new BaseBiz();
	private final SmsBiz smsBiz = new SmsBiz();
	
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		EmpExecutionContext.error("进入ll_commSvt的find方法:"+request.getRequestURL()+"?"+request.getQueryString()+";"+request.getParameter("method"));
	}
	
	public void getGtInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String spUser = request.getParameter("spUser"); 
		String[] gtInfos = new String[]{"","",""};
		int index = 0;
		int maxLen;
		int totalLen;
		int lastLen;
		int signLen;
		
		String info = "infos:";
		try{
			List<GtPortUsed> gtPortsList = new WgMsgConfigBiz().getPortByUserId(spUser);
			for(GtPortUsed gtPort : gtPortsList)
			{
				index = gtPort.getSpisuncm() - 2 > 0 ? 2 : gtPort.getSpisuncm();

				maxLen = gtPort.getMaxwords();
				totalLen = gtPort.getMultilen1();
				lastLen = gtPort.getMultilen2();
				signLen = gtPort.getSignlen() == 0?gtPort.getSignstr().trim().length():gtPort.getSignlen();
				
				gtInfos[index] =  String.valueOf(maxLen)+","+String.valueOf(totalLen) +
						","+String.valueOf(lastLen)+","+String.valueOf(signLen);
			}
			info += gtInfos[0]+"&"+gtInfos[1]+"&"+gtInfos[2]+"&";
		}catch(Exception e)
		{
			info = "error";
			EmpExecutionContext.error(e,"获取发送账户绑定的通道信息异常！");
		}finally
		{
			response.getWriter().print(info);
			//writer.print(info);
		}
	}
	
	/**
	 * 获取通道信息,包括英文短信配置参数
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getSpGateConfig(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String spUser = request.getParameter("spUser"); 
		String[] gtInfos = new String[]{"","","",""};
		int index = 0;
		int maxLen;
		int totalLen;
		int lastLen;
		int signLen;
		int enmaxLen;
		int entotalLen;
		int enlastLen;
		int ensignLen;
		int gateprivilege = 0;
		int gatepri;
		//签名位置,1:前置;0:后置
		int signLocation = 0;
		int ensinglelen;
		int msgLen = 990;
		
		String info = "infos:";
		try{
			// 根据发送账号获取路由信息
			List<DynaBean> spGateList = smsBiz.getSpGateInfo(spUser);
			for (DynaBean spGate : spGateList)
			{
				gateprivilege = 0;
				//中文短信配置参数
				maxLen = Integer.parseInt(spGate.get("maxwords").toString());
				totalLen = Integer.parseInt(spGate.get("multilen1").toString());
				lastLen = Integer.parseInt(spGate.get("multilen2").toString());
				signLen = Integer.parseInt(spGate.get("signlen").toString());
				// 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
				if(signLen == 0){
					signLen = spGate.get("signstr").toString().trim().length();
				}
				//英文短信配置参数
				enmaxLen = Integer.parseInt(spGate.get("enmaxwords").toString());
				entotalLen = Integer.parseInt(spGate.get("enmultilen1").toString());
				enlastLen = Integer.parseInt(spGate.get("enmultilen2").toString());
				ensinglelen = Integer.parseInt(spGate.get("ensinglelen").toString());
				
				//是否支持英文短信，1：支持；0：不支持
				gatepri = Integer.parseInt(spGate.get("gateprivilege").toString());
				//运营商标识
				index =Integer.parseInt(spGate.get("spisuncm").toString());
				//电信
				if(index == 21){
					index = 2;
				//国外通道
				}else if(index == 5){
					index = 3;
					//是否支持英文短信，1：支持；0：不支持
					if((gatepri&2)==2)
					{
						gateprivilege = 1;
						//国外通道英文短信最大长度
						msgLen = enmaxLen - 20;
					}
					else
					{
						gateprivilege = 0;
						//国外通道中文短信最大长度
						msgLen = maxLen - 10;
					}
				}
				//签名位置,1:前置;0:后置
				if((gatepri&4)==4)
				{
					signLocation = 1;
				}
				else
				{
					signLocation = 0;
				}
				//英文短信签名长度
				ensignLen = Integer.parseInt(spGate.get("ensignlen").toString());
				// 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
				if(ensignLen == 0){
					if(index == 3)
					{
						//国外通道英文短信签名长度
						ensignLen = smsBiz.getenSmsSignLen(spGate.get("ensignstr").toString().trim());
					}
					else
					{
						//国内通道英文短信签名长度
						ensignLen = spGate.get("ensignstr").toString().trim().length();
					}
				}
				
				gtInfos[index] = new StringBuffer().append(maxLen).append(",").append(totalLen).append(",")
								.append(lastLen).append(",").append(signLen).append(",").append(enmaxLen).append(",")
								.append(entotalLen).append(",").append(enlastLen).append(",").append(ensignLen)
								.append(",").append(gateprivilege).append(",").append(ensinglelen).append(",").append(signLocation).toString();
			}
			info += gtInfos[0]+"&"+gtInfos[1]+"&"+gtInfos[2]+"&"+gtInfos[3]+"&"+msgLen+"&";
		}catch(Exception e)
		{
			info = "error";
			EmpExecutionContext.error(e,"获取发送账户绑定的通道信息异常！");
		}finally
		{
			response.getWriter().print(info);
			//writer.print(info);
		}
	}

	/**
	 * 获取群组信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getGroupList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		//此方法获取群组列表
		StringBuffer buffer = new StringBuffer("");
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderByMap = new LinkedHashMap<String,String>();
		String corpCode = "";
		try {
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpCode = lfSysuser.getCorpCode();
			
			//操作员userid
			conditionMap.put("receiver", request.getParameter("lguserid"));
			conditionMap.put("gpAttribute", "0");
			orderByMap.put("udgName", StaticValue.ASC);
			//根据条件查询所有群组
			List<LfUdgroup> udgList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderByMap);
			if (udgList != null && udgList.size() > 0) 
			{
				
				String udgIds = "";
				for(LfUdgroup udg : udgList)
				{
					//udgIds += udg.getUdgId().toString()+",";
					udgIds+=udg.getGroupid().toString()+",";
				}
				//获取群组id字符串
				udgIds = udgIds.substring(0,udgIds.length()-1);
				//Map<String,String> countMap = new AddrBookAtom().getEmployeeCount(udgIds);
				Map<String,String> countMap = new GroupBiz().getGroupMemberCount(udgIds, 1, corpCode);
				//拼成html代码返回
				buffer.append("<select select-one name='groupList' id='groupList' " +
						"size='15' style='height: 240px; width: 240px; border: 0;color: black;font-size: 12px;'");
				buffer.append(" onclick='grouponChange()'>");
				for(LfUdgroup udg : udgList)
				{
					String mcount = countMap.get(udg.getGroupid().toString());
					mcount = mcount == null ? "0" : mcount;
					buffer.append("<option mcount='").append(mcount).append("' value='").append(udg.getUdgId()).append("'>");
					buffer.append(udg.getUdgName().replace("<","&lt;").replace(">","&gt;")).append(udg.getSharetype()==0?"[员工/个人]":"[员工/共享]").append("</option>");
				}
				buffer.append("</select>");
			}else
			{
				buffer.append("");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取群组信息异常！");
			request.getSession(false).setAttribute("error", e);
		}
		response.getWriter().print(buffer.toString());
	}
	

	/** 获取群组员工
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getGroupMember(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try
		{
			//搜索名称
			String epname = request.getParameter("epname");
			//群组id
			String udgId = request.getParameter("udgId");
			//如果是选择多个群组就不查询员工
			if("".equals(udgId) || udgId.split(",").length>1)
			{
				response.getWriter().print("");
				return;
			}
			//当前操作员userid
			//String userId = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userId = SysuserUtil.strLguserid(request);


			udgId = (udgId != null && udgId.length() > 0) ? udgId.substring(0,udgId.length()-1) : "";
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("name&like", epname);
			//查询出符合条件的人
			LfUdgroup group = baseBiz.getById(LfUdgroup.class, udgId);
			List<LfEmployee> lfEmployeeList = null;
			PageInfo pageInfo = new PageInfo();
			if(group!=null)
			{
				// 页码
				String pageIndex1 = request.getParameter("pageIndex1");
				// 区分上一页还是下一页
				String opType = request.getParameter("opType");
				if(opType != null && opType.equals("goNext"))
				{
					pageInfo.setPageIndex(Integer.parseInt(pageIndex1) + 1);
				}
				else
				{
					if(opType != null && opType.equals("goLast"))
					{
						pageInfo.setPageIndex(Integer.parseInt(pageIndex1) - 1);
					}
					else
					{
						pageInfo.setPageIndex(1);
					}
				}
				// 每页显示50条记录
				pageInfo.setPageSize(50);
				/*lfEmployeeList=new AddrBookBiz().getEmployeeListByUdgId(group.getGroupid().toString(), userId, conditionMap);*/
				//lfEmployeeList=depBiz.getEmployeeListByUdgId(group.getGroupid().toString(), userId, conditionMap, pageInfo);
				// 当前登录对象
				LfSysuser lfSysuser = getLoginUser(request);
				// 当前登录操作员企业，从会话中获取
				String lgcorpcode = lfSysuser.getCorpCode();
				lfEmployeeList=depBiz.getEmployeeListByUdgIdAndCorpCode(group.getGroupid().toString(), userId, conditionMap, pageInfo, lgcorpcode);

			}
			
			StringBuffer sb = new StringBuffer();
			//拼成html代码返回
			if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
				for (LfEmployee user : lfEmployeeList) {
					if(user.getRecState()==2)
					{
						sb.append("<option value='").append("m_"+user.getGuId())
						.append("' mobile='").append(user.getMobile()).append("'>");
						sb.append(user.getName().trim()).append("</option>");
						
					}else {
						sb.append("<option value='").append("e_"+user.getEmployeeId())
						.append("' mobile='").append(user.getMobile()).append("'>");
						sb.append(user.getName().trim()).append("</option>");
					}
				}
			}
			String pageStr = pageInfo.getTotalRec() + "@" + pageInfo.getTotalPage() + "@";
			response.getWriter().print(pageStr + sb.toString());
	
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取群组中的员工信息异常！");
		}
	}
	
	public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			String depId = request.getParameter("depId");
			if(lguserid == null && depId == null)
			{
				response.getWriter().print("");
				return;
			}
			/*List<LfEmployeeDep> empDepList = enterBiz.getEmpSecondDepTreeByUserIdorDepId(lfSysuser.getUserId().toString(),depId);*/
			List<LfEmployeeDep> empDepList = depBiz.getEmpSecondDepTreeByUserIdorDepId(lguserid,depId);
			LfEmployeeDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(empDepList != null && empDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < empDepList.size(); i++) {
					dep = empDepList.get(i);
					tree.append("{");
					tree.append("id:'").append(dep.getDepId()+"'");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",pId:'").append(dep.getParentId()+"'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != empDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"机构数获取子级机构异常！");
		}
	}
	
}
