package com.montnets.emp.clientsms.servlet;

import com.montnets.emp.clientsms.biz.ClientSmsBiz;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.common.vo.LfCustFieldValueVo;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.client.LfCustField;
import com.montnets.emp.entity.client.LfCustFieldValue;
import com.montnets.emp.entity.clientsms.LfBusTaoCan;
import com.montnets.emp.entity.clientsms.LfDfadvanced;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfDrafts;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("serial")
public class kfs_sendClientSMSSvt extends BaseServlet
{

	private final SmsBiz				smsBiz			= new SmsBiz();

	private final ClientSmsBiz		clientSmsBiz	= new ClientSmsBiz();

	private static final String	PATH			= "/dxkf/clientsms";

	final BaseBiz						baseBiz			= new BaseBiz();

	final CommonBiz					commonBiz		= new CommonBiz();


	/**跳转到客户群组群发发送页面
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		EmpExecutionContext.logRequestUrl(request,"<客户群组群发页面跳转>");
		// 当前登录账号的企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 当前登录账号的guid
		String guidstr = request.getParameter("lgguid");
        //请求来源
        String referer = request.getHeader("Referer");
		try
		{
			LfSysuser sysuser=null;
			if(guidstr!=null&&!"".equals(guidstr)&&corpCode!=null&&!"".equals(corpCode)){
				sysuser = baseBiz.getByGuId(LfSysuser.class,  Long.parseLong(guidstr));
			}else{
				EmpExecutionContext.error("客户群组群发find方法参数为空！guid="+guidstr+",corpCode="+corpCode+",referer:"+referer);
                HttpSession session = request.getSession(false);
                if(session == null){
                    EmpExecutionContext.error("客户群组群发find方法会话为null！"+",referer:"+referer);
                    return;
                }
				sysuser = (LfSysuser) session.getAttribute("loginSysuser");
				corpCode=sysuser.getCorpCode();
                guidstr = String.valueOf(sysuser.getGuId());
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> conp = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
			// 查询是否有上级审核人
			conditionMap.put("userId", sysuser.getUserName());
			List<LfFlow> flowList = baseBiz.getByCondition(LfFlow.class, conditionMap, null);
			if(flowList != null && flowList.size() > 0)
			{
				// 有审批流
				request.setAttribute("isFlow", "true");
			}
			else
			{
				// 没有审批流
				request.setAttribute("isFlow", "false");
			}
			conp.put("corpCode&in", "0," + corpCode);
			orconp.put("corpCode", "asc");
			
			//设置启用查询条件
			conp.put("state", "0");
			//设置查询手动和手动+触发
			conp.put("busType&in", "0,2");
			
			// 查询所有的业务类型
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conp, orconp);
			request.setAttribute("busList", busList);
			// 查询SP账号
			List<Userdata> spUserList = smsBiz.getSpUserList(sysuser);
			request.setAttribute("spUserList", spUserList);
			
			//发送账户存放内存Map
//			UserdataAtom userdataAtom = new UserdataAtom();
//			userdataAtom.setUserdata(spUserList);
			
			// 进入页面就产生taskId
			Long taskId = commonBiz.getAvailableTaskId();
			//操作日志信息
			String opContent = "获取taskid("+taskId+")成功";
			EmpExecutionContext.info("客户群组群发", sysuser.getCorpCode(), String.valueOf(sysuser.getUserId()), sysuser.getUserName(), opContent, "GET");
			request.setAttribute("taskId", taskId.toString());
			request.setAttribute("lfSysuser", sysuser);
			request.setAttribute("company", corpCode);
			//获取高级设置默认信息
			conditionMap.clear();
			conditionMap.put("userid", String.valueOf(sysuser.getUserId()));
			//11:客户群组群发
			conditionMap.put("flag", "11");
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			orderMap.put("id", StaticValue.DESC);
			List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, conditionMap, orderMap);
			LfDfadvanced lfDfadvanced = null;
			if(lfDfadvancedList != null && lfDfadvancedList.size() > 0)
			{
				lfDfadvanced = lfDfadvancedList.get(0);
			}
			request.setAttribute("lfDfadvanced", lfDfadvanced);
			request.getRequestDispatcher(PATH + "/kfs_sendClientSMS.jsp").forward(request, response);
		}
		catch (Exception e) {
            EmpExecutionContext.error(e, "客户群组群发加载发送页面失败！guid="+guidstr+"; corpCode="+corpCode+",referer:"+referer);
			request.setAttribute("findresult", "-1");
			try
			{
				request.getRequestDispatcher(PATH + "/kfs_sendClientSMS.jsp").forward(request, response);
			}
			catch (Exception ex)
			{
				EmpExecutionContext.error(ex,"客户群组群发跳转发送页面失败！"+",referer:"+referer);
			}
		}
	}

	// 判断选择的机构是否把其它已经选择的机构包含了
	public void isDepsContainedByDepB(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String ismut = request.getParameter("ismut");
		String depId = request.getParameter("depId");

		if("0".equals(ismut))
		{
			LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
			if(clientDep == null)
			{
				response.getWriter().print("0");
				return;
			}
			String countttt = clientSmsBiz.getDepClientCount(clientDep, 2).toString();
			response.getWriter().print(countttt);
			return;
		}
		List<LfClientDep> lfClientDepList = new ArrayList<LfClientDep>();
		LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
		String depIdsExist = request.getParameter("depIdsExist");
		String[] depIds = depIdsExist.split(",");
		// 将已经存在的机构id放在一个list里面(如果前缀有e就去掉e放在depIdExistList里面)
		List<Long> depIdExistList = new ArrayList<Long>();
		for (int j = 0; j < depIds.length; j++)
		{
			if(depIds[j] != null && !"".equals(depIds[j]))
			{
				if(depIds[j].indexOf("e") > -1)
				{
					if(!"".equals(depIds[j].substring(1)))
					{
						depIdExistList.add(Long.valueOf(depIds[j].substring(1)));
					}
				}
				else
				{
					depIdExistList.add(Long.valueOf(depIds[j]));
				}

			}
		}
		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");

		LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
		String deppath = clientDep.getDeppath();
		// 查找出要添加的机构的所有子机构，放在一个set里面
		lfClientDepList = clientSmsBiz.findClientDepsByDeppath(lgcorpcode, deppath);
		List<Long> depIdListTemp = new ArrayList<Long>();
		for (int i = 0; i < lfClientDepList.size(); i++)
		{
			depIdSet.add(lfClientDepList.get(i).getDepId());
		}
		// 遍历这个set，看看已经存在的机构是否包含在这个机构的子机构里面，如果包含的话，就重新生成一个option列表的字符串给select控件
		for (int a = 0; a < depIdExistList.size(); a++)
		{
			if(depIdSet.contains(depIdExistList.get(a)))
			{
				depIdListTemp.add(depIdExistList.get(a));
			}
		}
		// 如果确实有几个已经存在的机构是要添加机构的子机构，那么就从新生成select的html
		String depids = depIdSet.toString();
		depids = depids.substring(1, depids.length() - 1);
		// 计算机构人数
		String countttt = clientSmsBiz.getDepClientCount(clientDep, 1).toString();
		if(depIdListTemp.size() > 0)
		{
			String tempDeps = depIdListTemp.toString();
			tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
			response.getWriter().print(countttt + "," + tempDeps);
			return;
		}
		// 如果没有包含关系
		else
		{
			response.getWriter().print("notContains" + "&" + countttt);
			return;
		}
	}

	/**
	 * 短信客服群组群发的发送方法
	 * @param request
	 * @param response
	 */
	public void add(HttpServletRequest request, HttpServletResponse response)
	{
		String lguserid="";
		String lgcorpcode="";
		String lgguid="";
		String taskId="";
		String result = "";
		try 
		{
			// 任务主题
			String title = request.getParameter("taskname");
			//任务ID
			taskId = request.getParameter("taskId");
			//当前登录操作员id
			//lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			lguserid = SysuserUtil.strLguserid(request);


			//当前登录企业
			lgcorpcode = request.getParameter("lgcorpcode");
			//当前操作员GUID
			lgguid = request.getParameter("lgguid");
			if(lguserid==null||"".equals(lguserid)||lgcorpcode==null||"".equals(lgcorpcode)||lgguid==null||"".equals(lgguid)){
				EmpExecutionContext.error("客户群组群发获取参数异常，"+"lguserid:"+lguserid+"，lgcorpcode:"+lgcorpcode+",lgguid:"+lgguid);
			}
			//主题为默认时,直接返回(防止重发)
			if(title != null && "不作为短信内容发送".equals(title.trim()))
			{
				EmpExecutionContext.error("客户群组发送获取参数异常，" + "title:" + title+"，taskId："+taskId);
				String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
				result = ErrorCodeInfo.getInstance(langName).getErrorInfo(IErrorCode.V10001);
				EmpExecutionContext.error(result);
			}
			else
			{
				// 提交发送
				result = new SmsSendBiz().send(request, response);
			}
            //提交成功 删除当前草稿
            String draftId = request.getParameter("draftId");
            if(StringUtils.isNotBlank(draftId) && ("timerSuccess".equals(result) || "createSuccess".equals(result) || "000".equals(result))){
                baseBiz.deleteByIds(LfDrafts.class,draftId);
            }
			request.getSession(false).setAttribute("mcs_clientResult", result);
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,lguserid,lgcorpcode);
		}finally{
			try
			{
				String s = request.getRequestURI();
				//重定向
				s = s + "?method=find&lgguid=" + lgguid + "&lgcorpcode=" + lgcorpcode +"&lguserid=" +lguserid+"&oldTaskId="+taskId+"&t="+System.currentTimeMillis();
				response.sendRedirect(s);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"跳转到客户群组群发页面失败！");
			}
		}
	}
	
	
	/**
	 * 获取群组客户
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getGroupMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String result = "";
		try
		{
			// 搜索名称
			String epname = request.getParameter("epname");
			// 群组id
			String udgId = request.getParameter("udgId");
            if(udgId == null || "".equals(udgId.trim())){
                EmpExecutionContext.error("客户群组群发选择群组人员获取群组id为"+udgId);
                return;
            }
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("name&like", epname);
			StringBuffer sb = new StringBuffer();
			List<GroupInfoVo> groupInfoList = null;
			PageInfo pageInfo = new PageInfo();

            pageSet(pageInfo,request);
            // 每页显示50条记录
            pageInfo.setPageSize(50);

			groupInfoList = clientSmsBiz.getCliGroupUser(Long.valueOf(udgId), epname, pageInfo);
			if(groupInfoList != null && groupInfoList.size() > 0)
			{
				for (GroupInfoVo user : groupInfoList)
				{
					sb.append("<option value='").append("m_" + user.getGuId()).append("' mobile='").append(user.getMobile()).append("'>");
					sb.append(StringUtils.defaultIfEmpty(user.getName(),"").trim()).append("</option>");
				}
			}
            result = pageInfo.getTotalRec() + "@" + pageInfo.getTotalPage() + "@" + sb.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询客户群组成员失败！");
		}finally {
            out.print(result);
        }
    }

	// 点击选择机构按钮的时候如果包含子机构则获取子机构集合
	public void getDep(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			String depId = request.getParameter("depId");
			// String depName= request.getParameter("depName");
			String depIdsExist = request.getParameter("depIdsExist");
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
            if(depId == null || depIdsExist == null || lgcorpcode == null){
                EmpExecutionContext.error("客户群组群发选择机构处理时获取参数为null! depId:"+depId+",depIdsExist:"+depIdsExist+",lgcorpcode:"+lgcorpcode);
                response.getWriter().print("illegal");
                return;
            }
			String[] depIds = depIdsExist.split(",");
			StringBuffer depIdsTemp = new StringBuffer();
			for (int i = 0; i < depIds.length; i++)
			{
				if(depIds[i].indexOf("e") > -1)
				{
					depIdsTemp.append(depIds[i].substring(1) + ",");
				}
				else
					if(depIds[i].equals(depId))
					{
						response.getWriter().print("depExist");
						return;
					}
			}
			if(!"".equals(depIdsTemp.toString()))
			{
				// 判断新添加的机构是不是已经添加的机构的子机构
				boolean result = clientSmsBiz.isDepAcontainsDepB(depIdsTemp.toString(), depId, lgcorpcode);
				if(result)
				{
					response.getWriter().print("depExist");
					return;
				}
				else
				{
					response.getWriter().print("notExist");
					return;
				}
			}
			else
			{
				response.getWriter().print("notExist");
				return;
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"客户群组群发获取客户机构的子机构失败！");
		}
	}

	/**
	 * 获取群组信息
	 * 
	 * @param request
	 * @param response
	 */
	public void getGroupList(HttpServletRequest request, HttpServletResponse response)
	{
		// 当前登录账号的userid
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);


		StringBuffer buffer = new StringBuffer("");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		PrintWriter writer = null;
		String corpCode = "";
		String individual = MessageUtils.extractMessage("common","common_individual",request);
		String shared = MessageUtils.extractMessage("common","common_shared",request);
		String client = MessageUtils.extractMessage("common","common_client",request);
		try
		{
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpCode = lfSysuser.getCorpCode();
			writer = response.getWriter();
			// 操作员userid
			conditionMap.put("gpAttribute", "1");
			// conditionMap.put("userId", userId);
			// 查询出并且是共享的群组
			conditionMap.put("receiver", userId);

			orderByMap.put("udgName", StaticValue.ASC);
			// 根据条件查询所有群组
			List<LfUdgroup> udgList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderByMap);
			if(udgList != null && udgList.size() > 0)
			{
				String udgIds = "";
				for (LfUdgroup udg : udgList)
				{
					// udgIds += udg.getUdgId().toString() + ",";
					udgIds += udg.getGroupid().toString() + ",";
				}
				// 获取群组id字符串
				udgIds = udgIds.substring(0, udgIds.length() - 1);
				//Map<String, String> countMap = clientSmsBiz.getGroupCount(udgIds, "2");
				Map<String,String> countMap = new GroupBiz().getGroupMemberCount(udgIds, 2, corpCode);
				// 拼成html代码返回
				buffer.append("<select select-one name='groupList' id='groupList' " + "size='15' style='height: 240px; width: 240px; border: 0;color: black;font-size: 12px;'");
				buffer.append(" onclick='grouponChange()'>");
				for (LfUdgroup udg : udgList)
				{
					String mcount = countMap.get(udg.getGroupid().toString());
					mcount = mcount == null ? "0" : mcount;
					/*
					 * buffer.append("<option mcount='").append(mcount).append("' value='"
					 * ).append(udg.getUdgId()).append("'>");
					 * 
					 * buffer.append(udg.getUdgName().replace("<","&lt;").replace
					 * (">","&gt;")).append("</option>");
					 */
					buffer.append("<option  mcount='" + mcount + "'   value='" + udg.getGroupid() + "'   style='padding-left: 5px;'>").append(udg.getUdgName().replace("<", "&lt;").replace(">", "&gt;"));
					if(udg.getSharetype() == 0)
					{
						buffer.append(" ["+client+"/"+individual+"]");
					}
					else
						if(udg.getSharetype() == 1)
						{
							buffer.append(" ["+client+"/"+shared+"]");
						}

				}
				buffer.append("</select>");
			}
			else
			{
				buffer.append("");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询客户群组信息失败！");
		}
		writer.print(buffer.toString());
	}

	// 获取客户属性列表
	public void getProsList(HttpServletRequest request, HttpServletResponse response)
	{
		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		StringBuffer buffer = new StringBuffer();
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			// 查询所有的客户属性
			conditionMap.put("corp_code", corpCode);
			orderbyMap.put("id", "asc");
			List<LfCustField> custField = baseBiz.getByCondition(LfCustField.class, conditionMap, orderbyMap);

			// 拼成html代码返回
			buffer.append("<select select-one name='custfieldList' id='custfieldList' " + "size='15' style='height: 240px; width: 240px; border: 0;color: black;font-size: 12px;'");
			buffer.append(" onclick='custfieldonChange()'>");
			String udgIds = "";
			for (LfCustField udg : custField)
			{
				udgIds += udg.getId().toString() + ",";
			}
			for (LfCustField custField2 : custField)
			{
				int mcount = clientSmsBiz.getClientCountByFieldRef(corpCode, custField2.getField_Ref());
				buffer.append("<option mcount='").append(mcount).append("' fieldId='").append(custField2.getId()).append("' value='").append(custField2.getField_Ref()).append("'>");
				buffer.append(custField2.getField_Name().replace("<", "&lt;").replace(">", "&gt;")).append("</option>");
			}
			buffer.append("</select>");
			writer.print(buffer.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取客户属性人员列表失败！");
		}
	}

	/**
	 * 获取群组客户
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getCustFieldMember(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
		String custfieldid = request.getParameter("fieldId");
		// 搜索名称
		String epname = request.getParameter("epname");
		StringBuffer sb = new StringBuffer();
		try
		{
			// 查询所有的客户属性值
			LfCustFieldValueVo cfvo = new LfCustFieldValueVo();

			// 根据客户属性查找属性值
			cfvo.setCorp_code(corpCode);
			cfvo.setField_ID(custfieldid);
			cfvo.setField_Value(epname);
			List<LfCustFieldValueVo> proList = clientSmsBiz.getCustVos(cfvo);
			// 拼成html代码返回
			if(proList != null && proList.size() > 0)
			{
				for (LfCustFieldValueVo user : proList)
				{
					sb.append("<option value='").append(user.getField_Ref() + "&" + user.getId()).append("' mobile='").append("'>");
					sb.append(user.getField_Value().trim()).append("</option>");
				}
			}
			response.getWriter().print(sb.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取客户属性成员失败！");
		}
	}

	/**
	 * 根据属性值获取客户数
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getCustFieldMemberCount(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
        // 字符转义回来
        String fieldValue = StringEscapeUtils.unescapeHtml(request.getParameter("fieldValue"));
        String[] split = fieldValue.split("&");
		String fieldRef = split[0];
		String fieldValueId = split[1];
		try
		{
			Long mcount = 0L;
			LfCustFieldValueVo fieldValueVo = new LfCustFieldValueVo();
			fieldValueVo.setField_Ref(fieldRef);
			fieldValueVo.setId(Long.valueOf(fieldValueId));
			mcount = clientSmsBiz.getClientCountByCusField(corpCode, fieldValueVo);
			mcount = mcount == null ? 0 : mcount;
			response.getWriter().print(mcount);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"根据属性值获取客户数失败！");
		}
	}

	/**
	 * 获取群组群发中客户机构下的客户 ， 兼容点查询方法
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getDepAndClientTree1(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PageInfo pageinof = new PageInfo();
		// 页码
		String pageIndex1 = request.getParameter("pageIndex1");
		// 操作是上一页还是下一页
		String opType = request.getParameter("opType");
		if(opType != null && opType.equals("goNext"))
		{
			pageinof.setPageIndex(Integer.parseInt(pageIndex1) + 1);
		}
		else
			if(opType != null && opType.equals("goLast"))
			{
				pageinof.setPageIndex(Integer.parseInt(pageIndex1) - 1);
			}
			else
			{
				pageinof.setPageIndex(1);
			}
		// 每页显示50条记录
		pageinof.setPageSize(50);
		try
		{
			String epname = request.getParameter("epname");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String depId = request.getParameter("depId");
			StringBuffer sb = new StringBuffer();
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			LfClientDep clientDep = null;
			// 有机构则带上机构id
			if(depId != null && !"".equals(depId.trim()))
			{
				clientDep = baseBiz.getById(LfClientDep.class, depId);
			}

			LfClient client = new LfClient();
			client.setCorpCode(lgcorpcode);
			if(epname != null && !"".equals(epname.trim()))
			{
				client.setName(epname);
			}
			// 查询客户列表
			List<DynaBean> lfClients = clientSmsBiz.getClientsByDepId(clientDep, client, 2, pageinof);
			// 生成html
			if(lfClients != null && lfClients.size() > 0)
			{
				LinkedHashMap<Long, String> depMap = new LinkedHashMap<Long, String>();
				// 查询出机构对象
				conditionMap.clear();
				conditionMap.put("corpCode", lgcorpcode);
				List<LfClientDep> empDepsList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
				if(empDepsList != null && empDepsList.size() > 0)
				{
					for (LfClientDep dep : empDepsList)
					{
						depMap.put(dep.getDepId(), dep.getDepName());
					}
				}
				for (DynaBean bean : lfClients)
				{
					Long id = Long.valueOf(bean.get("dep_id") + "");
					sb.append("<option value='").append("e_" + bean.get("client_id")).append("' mobile='").append(String.valueOf(bean.get("mobile"))).append("'>").append(String.valueOf(bean.get("name")));
					if(depMap.containsKey(id))
					{
						sb.append(" [").append(depMap.get(id)).append("]");
					}
					sb.append("</option>");
				}
			}
			String pageStr = pageinof.getTotalRec() + "@" + pageinof.getTotalPage() + "@";
			// 异步返回处理
			response.getWriter().print(pageStr + sb.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取群组群发中客户机构下的客户失败！");
		}
	}

	/**
	 * 检测文件
	 * 
	 * @param request
	 * @param response
	 */
	public void checkFiles(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			// 获取文件路径
			String fileUrl = request.getParameter("url");
			// 检测文件是否存在
			boolean r = new TxtFileUtil().checkFile(fileUrl);
			if(r)
			{
				writer.print("true");
			}
		}
		catch (Exception e)
		{
			// 异常
			EmpExecutionContext.error(e,"检查文件是否存在失败！");
		}
	}

	/**
	 * 过滤短信内容关键字
	 * 
	 * @param request
	 * @param response
	 */
	public void checkBadWord(HttpServletRequest request, HttpServletResponse response)
	{
		String tmMsg = request.getParameter("tmMsg");
		String corpCode = request.getParameter("corpCode");
		String words = new String();
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			KeyWordAtom keyWordAtom = new KeyWordAtom();
			words = keyWordAtom.checkText(tmMsg, corpCode);
		}
		catch (Exception e)
		{
			words = "error";
			EmpExecutionContext.error(e,"客户群组群发过滤关键字失败！");
		}
		finally
		{
			if(writer != null){
				writer.print("@" + words);
			}
		}
	}

	/**
	 * 查询短信模板
	 * @param request
	 * @param response
	 */
	public void getTmAsOption(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 只能引擎模板
			conditionMap.put("tmpType", "3");
			// 状态为启用
			conditionMap.put("tmState", "1");
			// 无需审核或者通过审核
			conditionMap.put("isPass&in", "0,1");
			// conditionMap.put("userId", getUserId().toString());
			conditionMap.put("dsflag", request.getParameter("dsflag"));

			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("tmid", StaticValue.ASC);
			// 当前登录操作员userid
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));// 当前登录操作员id
			//漏洞修复 session里获取操作员信息
			Long lguserid = SysuserUtil.longLguserid(request);


			List<LfTemplate> tmpList = baseBiz.getByCondition(LfTemplate.class, lguserid, conditionMap, orderbyMap);
			writer.print("<option value='' class='optionFirst'>请选择短信模板</option>");
			if(tmpList != null)
			{
				for (LfTemplate temp : tmpList)
				{
					if(temp.getIsPass() == 0 || temp.getIsPass() == 1)
					{
						// 生成select控件的html代码
						writer.print("<option value='" + temp.getTmid() + "'>" + temp.getTmName().replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "(ID:" + temp.getTmid() + ")</option>");

					}
				}
			}
			else
			{
				writer.print("");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询短信模板失败！");
			if(writer != null){
				writer.print("");
			}
		}
	}

	/**
	 * 获取客户机构的 数，只 限查子级
	 * 
	 * @param request
	 * @param response
	 */
	public void getClientSecondDepJson(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			String depId = request.getParameter("depId");
            LfSysuser sysuser = getLoginUser(request);
			// 此方法只查询两级机构
			List<LfClientDep> clientDepList = clientSmsBiz.getCliSecondDepTreeBySysUser(sysuser, depId);
			LfClientDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(clientDepList != null && clientDepList.size() > 0)
			{
				tree.append("[");
				for (int i = 0; i < clientDepList.size(); i++)
				{
					dep = clientDepList.get(i);
					tree.append("{");
					tree.append("id:").append(dep.getDepId() + "");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird()).append("'");
					// 树数据中加入父机构id
					if(dep.getParentId() - 0 == 0)
					{
						tree.append(",pId:").append(0);
					}
					else
					{
						tree.append(",pId:").append(dep.getParentId());
					}
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != clientDepList.size() - 1)
					{
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		}
		catch (Exception e)
		{
			if(writer != null){
				writer.print("");
			}
			EmpExecutionContext.error(e,"获取客户机构数目失败！");
		}
		finally
		{
			if(writer != null){
				writer.flush();
				writer.close();
			}

		}
	}

	/**
	 * 获取短信模板内容
	 * 
	 * @param request
	 * @param response
	 */
	public void getTmMsg1(HttpServletRequest request, HttpServletResponse response)
	{
		// 发送模块获取模板（解决断网断库session超时用）

		String result = null;
		// 模板id
		String tmId = request.getParameter("tmId");
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			if("".equals(tmId))
			{
				result = "";
			}
			else
			{
				// 根据id查询模板记录
				LfTemplate template = baseBiz.getById(LfTemplate.class, tmId);
				result = template.getTmMsg();
			}
		}
		catch (Exception e)
		{
			result = "error";
			EmpExecutionContext.error(e,"获取短信模板内容失败！");
		}
		finally
		{
			// 异步返回操作结果
			if(writer != null){
				writer.print("@" + result);
			}
			if(writer != null){
				writer.flush();
				writer.close();
			}
		}
	}

	/**
	 * 检测文件是否存在
	 * 
	 * @param request
	 * @param response
	 */
	public void goToFile(HttpServletRequest request, HttpServletResponse response)
	{
		String url = request.getParameter("url");
		TxtFileUtil tfu = new TxtFileUtil();
		try
		{
			response.getWriter().print(tfu.checkFile(url));
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e,"检查文件是否存在失败！");
		}
	}

	/**
	 * @description 高级搜索页面
	 *              1.支持客户基本信息查询和客户搜索查询；
	 *              2.选择人员支持“全选”和“部分选择”；
	 *              3.“全选”将搜索结果全部选择；
	 *              4.实现思想为：在父frame隐藏域中隐藏一些值，然后将这些值提交到服务器端，经过处理，
	 *              然后获取高级搜索的“手机号”字串。
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void advancedSearch(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			// 获取自定义属性
			List<LfCustField> list1 = (List<LfCustField>) request.getAttribute("list1");
			if(list1 == null || list1.size() == 0)
			{
				list1 = clientSmsBiz.getdatalist(lgcorpcode);
			}
			Map<LfCustField, List<LfCustFieldValue>> map1 = (Map<LfCustField, List<LfCustFieldValue>>) request.getAttribute("map1");
			Map<LfCustField, List<LfCustFieldValue>> map2 = (Map<LfCustField, List<LfCustFieldValue>>) request.getAttribute("map2");
			if(map1 == null || map1.size() == 0 || (map2 == null || map2.size() == 0))
			{
				map1 = new LinkedHashMap<LfCustField, List<LfCustFieldValue>>();
				map2 = new LinkedHashMap<LfCustField, List<LfCustFieldValue>>();
				// 把单选和多选分开
				for (int i = 0; i < list1.size(); i++)
				{
					LfCustField lf = list1.get(i);
					// 单选
					if(lf.getV_type() != null && "0".equals(lf.getV_type()))
					{
						List<LfCustFieldValue> list2 = clientSmsBiz.getValueVo(lf.getId());
						map1.put(lf, list2);
					}// 多选
					else
						if(lf.getV_type() != null && "1".equals(lf.getV_type()))
						{
							List<LfCustFieldValue> list3 = clientSmsBiz.getValueVo(lf.getId());
							map2.put(lf, list3);
						}
				}
			}

			List<DynaBean> areaBeans = (List<DynaBean>) request.getAttribute("areaBeans");
			if(areaBeans == null || areaBeans.size() == 0)
			{
				areaBeans = clientSmsBiz.getAreas(lgcorpcode);
			}
			List<DynaBean> beans = new ArrayList<DynaBean>();
			LfClient client = new LfClient();
			PageInfo pageInfo = new PageInfo();
			boolean isFirstEnter = pageSet(pageInfo, request);
			// 客户ID对应客户机构
			LinkedHashMap<Long, String> clientDepNameMap = new LinkedHashMap<Long, String>();
			// 客户属性对应关系（客户Id => 客户属性字符）
			LinkedHashMap<Long, String> clientValueMap = new LinkedHashMap<Long, String>();
			// 查询条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 客户属性
			List<LfCustFieldValueVo> custFieldList = new ArrayList<LfCustFieldValueVo>();
			if(!isFirstEnter)
			{
				// 客户基本信息
				String depId = request.getParameter("depId");
				String clientCode = request.getParameter("clientCode");
				String cName = request.getParameter("cName");
				String sex = request.getParameter("sex");
				String mobile = request.getParameter("mobile");
				String birth1 = request.getParameter("birth1");
				String birth2 = request.getParameter("birth2");
				String area = request.getParameter("area");

				// 客户属性字段名,"FIELD01,FIELD02"
				String nameStr = request.getParameter("fieldNames");

				if(nameStr.length() > 0)
				{
					String[] filedNames = nameStr.split(",");
					for (String fieldName : filedNames)
					{
						// 客户属性对应的ID
						String[] ids = request.getParameterValues(fieldName);
						for (String id : ids)
						{
							LfCustFieldValueVo custField = new LfCustFieldValueVo();
							custField.setField_Ref(fieldName);
							custField.setId(Long.parseLong(id));
							custFieldList.add(custField);
						}
					}
				}

				client.setCorpCode(lgcorpcode);
				// 客户基本资料判断
				if(depId != null && !"".equals(depId.trim()))
				{
					client.setDepId(Long.valueOf(depId));
					// 等到当前机构和其子机构的Id集合
					List<LfClientDep> clientDepList = clientSmsBiz.findDepIdsAndSelf(depId, lgcorpcode);

					// 将depIds拼接成一个字符串用于查询
					String depIds = "";
					for (LfClientDep item : clientDepList)
					{
						if("".equals(depIds))
						{
							depIds = String.valueOf(item.getDepId());
						}
						else
						{
							depIds = depIds + "," + String.valueOf(item.getDepId());
						}
					}

					conditionMap.put("depIds", depIds);
				}
				// 构造查询对象
				if(clientCode != null && !"".equals(clientCode.trim()))
				{
					client.setClientCode(clientCode);
				}
				if(cName != null && !"".equals(cName.trim()))
				{
					client.setName(cName);
				}
				if(sex != null && !"".equals(sex.trim()))
				{
					client.setSex(Integer.valueOf(sex));
				}
				if(mobile != null && !"".equals(mobile.trim()))
				{
					client.setMobile(mobile);
				}
				if(area != null && !"".equals(area.trim()))
				{
					client.setArea(area);
				}
				if(birth1 != null && !"".equals(birth1.trim()))
				{
					conditionMap.put("birth1", birth1);
				}
				if(birth2 != null && !"".equals(birth2.trim()))
				{
					conditionMap.put("birth2", birth2);
				}

				// 执行查询逻辑
				beans = clientSmsBiz.findAdvancedSearchClientNew(client, conditionMap, custFieldList, pageInfo);
				if(beans != null && beans.size() > 0)
				{
					LinkedHashMap<String, String> valuemap = new LinkedHashMap<String, String>();
					List<LfCustFieldValue> fields = new ArrayList<LfCustFieldValue>();
					// 迭代客户记录，获取部门的名称和客户属性值
					for (int i = 0; i < beans.size(); i++)
					{
						DynaBean bean = beans.get(i);
						// 提取部门的名称
						Long lfclientId = Long.valueOf(bean.get("client_id") + "");
						String depName = clientSmsBiz.getDepNameById(lfclientId);
						clientDepNameMap.put(lfclientId, depName);
						// 字段的名称
						String fieldN = "";
						// 属性值字符串
						String custFieldIdStr = "";

						// 下面等到一个custFieldIdStr的字符串","分开,如 "29,31,32,33,34"
						for (int m = 1; m < 51; m++)
						{
							if(m < 10)
							{
								fieldN = "field0" + m;
							}
							else
							{
								fieldN = "field" + m;
							}
							Object object = bean.get(fieldN);
							if(object != null && !"".equals(object))
							{
								String id = String.valueOf(object);
								if(id.contains(";"))
								{
									String[] arr = id.split(";");
									if(arr != null && arr.length > 0)
									{
										for (String temp : arr)
										{
											if("".equals(custFieldIdStr))
											{
												custFieldIdStr = temp;
											}
											else
											{
												custFieldIdStr = custFieldIdStr + "," + temp;
											}
										}
									}
								}
								else
								{
									if("".equals(custFieldIdStr))
									{
										custFieldIdStr = id;
									}
									else
									{
										custFieldIdStr = custFieldIdStr + "," + id;
									}
								}

							}
						}

						// 通过custFieldIdStr，来获取客户{属性=>"属性值",'...'}
						if(custFieldIdStr != null && !"".equals(custFieldIdStr))
						{
							valuemap.clear();
							fields.clear();
							valuemap.put("id&in", custFieldIdStr);
							fields = baseBiz.getByCondition(LfCustFieldValue.class, valuemap, null);
							String filedvalue = "";
							if(fields != null && fields.size() > 0)
							{
								for (LfCustFieldValue value : fields)
								{
									if("".equals(filedvalue))
									{
										filedvalue = value.getField_Value();
									}
									else
									{
										filedvalue = filedvalue + "," + value.getField_Value();
									}
								}
								clientValueMap.put(lfclientId, filedvalue);
							}
						}
					}
				}
			}

			String conditionsql = "";
			if(conditionMap.get("conditionsql") != null && !"".equals(conditionMap.get("conditionsql")))
			{
				conditionsql = conditionMap.get("conditionsql");
			}
			request.setAttribute("conditionsql", conditionsql.replaceAll("%","#&M"));

			request.setAttribute("areaBeans", areaBeans);
			request.setAttribute("lgcorpcode", lgcorpcode);
			request.setAttribute("lguserid", lguserid);
			request.setAttribute("map1", map1);
			request.setAttribute("map2", map2);
			request.setAttribute("client", client);
			request.setAttribute("beans", beans);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("clientDepNameMap", clientDepNameMap);
			request.setAttribute("clientValueMap", clientValueMap);
			request.setAttribute("custFieldList", custFieldList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"客户群组群发高级搜索失败！");
		}finally{
			try
			{
				request.getRequestDispatcher(PATH + "/kfs_searchPage.jsp").forward(request, response);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"客户群组群发高级搜索页面跳转失败！");
			}
		}
	}

	/**
	 * 获取微信用户
	 * 
	 * @param request
	 * @param response
	 */
	public void getwxuser(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String lgcorpcode = request.getParameter("lgcorpcode");
			String ename = request.getParameter("ename");
			StringBuffer sb = new StringBuffer();
			List<DynaBean> beansList = clientSmsBiz.getWxuserByCorpCode(lgcorpcode, ename);
			if(beansList != null && beansList.size() > 0)
			{
				for (DynaBean bean : beansList)
				{
					Object mobileobj = bean.get("phone");
					String mobile = "";
					if(mobileobj != null && !"".equals(mobileobj))
					{
						mobile = String.valueOf(mobileobj);
					}
					else
					{
						continue;
					}
					Object nameobj = bean.get("uname");
					String name = "";
					if(nameobj != null && !"".equals(nameobj))
					{
						name = String.valueOf(nameobj);
					}
					else
					{
						name = "-";
					}
					sb.append("<option value='u").append(mobile).append("' mobile='").append(mobile).append("'>");
					sb.append(name + " [").append(mobile + "]").append("</option>");
				}
			}
			response.getWriter().print(sb.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取微信用户失败！");
		}

	}
	
	/**
	 * 获取移动业务群组信息
	 * 
	 * @param request
	 * @param response
	 */
	public void getYdywGroupList(HttpServletRequest request, HttpServletResponse response)
	{
		// 当前登录账号的userid
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);


		StringBuffer buffer = new StringBuffer("");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		PrintWriter writer = null;
		String corpCode = "";
		try
		{
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpCode = lfSysuser.getCorpCode();
			writer = response.getWriter();
			
			conditionMap.put("state", "0");
			conditionMap.put("corp_code", corpCode);
			orderByMap.put("taocan_name", StaticValue.ASC);
			// 根据条件查询所有套餐
			List<LfBusTaoCan> busTaoCanList = baseBiz.getByCondition(LfBusTaoCan.class, conditionMap, orderByMap);
			if(busTaoCanList != null && busTaoCanList.size() > 0)
			{
				String tcCodes = "";
				for (LfBusTaoCan busTaoCan : busTaoCanList)
				{
					tcCodes +="'"+busTaoCan.getTaocan_code() + "',";
				}
				// 获取群组id字符串
				tcCodes = tcCodes.substring(0, tcCodes.length() - 1);
				Map<String,String> countMap = clientSmsBiz.getYdywGroupMemberCount(tcCodes,corpCode);
				// 拼成html代码返回
				buffer.append("<select select-one name='ydywGroupList' id='ydywGroupList' " + "size='15' style='height: 240px; width: 240px; border: 0;color: black;font-size: 12px;'");
				buffer.append(" onclick='ydywGrouponChange()'>");
				for (LfBusTaoCan busTaoCan : busTaoCanList)
				{
					String mcount = countMap.get(busTaoCan.getTaocan_code().toString());
					mcount = mcount == null ? "0" : mcount;
					buffer.append("<option  mcount='" +mcount + "'   value='" + busTaoCan.getTaocan_code() + "'   style='padding-left: 5px;'>").append(busTaoCan.getTaocan_name().replace("<", "&lt;").replace(">", "&gt;"))
					.append("(").append(busTaoCan.getTaocan_code().replace("<", "&lt;").replace(">", "&gt;")).append(")");
				}
				buffer.append("</select>");
			}
			else
			{
				buffer.append("");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询套餐信息失败！");
		}
		writer.print(buffer.toString());
	}

	/** 获取套餐用户
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getYdywGroupMember(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try
		{
			//搜索名称
			String epname = request.getParameter("epname");
			//套餐编码
			String tccode = request.getParameter("tccode");
            if(tccode==null || "".equals(tccode.trim())){
                EmpExecutionContext.error("客户群组群发选择签约人员获取套餐编码为"+tccode);
                response.getWriter().print("");
                return;
            }

			PageInfo pageInfo = new PageInfo();

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
            StringBuffer sbSql=new StringBuffer("");
            sbSql.append("select client.guid guid,client.mobile mobile,client.name name,ctc.contract_id contract_id from lf_client client inner join lf_contract_taocan ctc on client.guid=ctc.guid where ctc.taocan_code='"+tccode+"' and ctc.is_valid='0' ");
            if(epname!=null&&!"".equals(epname)){
                sbSql.append(" and client.name like '%"+epname+"%'");
            }
            String countSql="select count(*) totalcount from ("+sbSql.toString()+") tmp" ;
            sbSql.append(" order by client.name asc");
            String sql= sbSql.toString();

            List<DynaBean> clientBeanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql.toString(),
                    countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
			StringBuffer sb = new StringBuffer();
			
			//签约ID集合
			StringBuffer contractIds=new StringBuffer("");
			if (clientBeanList != null && clientBeanList.size() > 0) {
				for (DynaBean clientBean : clientBeanList) {
					contractIds.append(String.valueOf(clientBean.get("contract_id"))).append(",");
				}
				contractIds.deleteCharAt(contractIds.lastIndexOf(","));
			}
			
			//拼成html代码返回
			if (clientBeanList != null && clientBeanList.size() > 0) {
				String accountNoStr=null;
				Map<String, String> contractMap=clientSmsBiz.getAccountNoByContractIds(contractIds.toString());
				for (DynaBean clientBean : clientBeanList) {
						accountNoStr=null;
						accountNoStr=contractMap.get(String.valueOf(clientBean.get("contract_id")));
					    if(accountNoStr==null||"".equals(accountNoStr.trim())||"null".equals(accountNoStr.trim())){
					    	accountNoStr="";
					    }else{
					    	if(accountNoStr.length()>4){
					    		accountNoStr="(***"+accountNoStr.substring(accountNoStr.length()-4,accountNoStr.length())+")";
					    	}else{
					    		accountNoStr="("+accountNoStr+")";
					    	}
					    }
						sb.append("<option value='").append("c_"+clientBean.get("guid").toString())
						.append("' mobile='").append(clientBean.get("mobile").toString()).append("'>");
						sb.append(clientBean.get("name").toString()+accountNoStr).append("</option>");
				}
			}
			String pageStr = pageInfo.getTotalRec() + "@" + pageInfo.getTotalPage() + "@";
			response.getWriter().print(pageStr + sb.toString());
	
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取套餐人员信息异常！");
		}
	}
	
	/**
	 * 高级设置存为默认
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void setDefault(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		//返回信息
		String result = "fail";
		try {
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String lgcorpcode = request.getParameter("lgcorpcode");
			String busCode = request.getParameter("busCode");
			String priority = request.getParameter("priority");
			String spUser = request.getParameter("spUser");
			String isReply = request.getParameter("isReply");
			String flag = request.getParameter("flag");
			if(flag == null || "".equals(flag))
			{
				EmpExecutionContext.error("客户群组群发高级设置存为默认参数异常！FLAG:"+flag);
				response.getWriter().print(result);
				return;
			}
			if(lguserid == null || "".equals(lguserid))
			{
				EmpExecutionContext.error("客户群组群发高级设置存为默认参数异常！lguserid："+lguserid);
				response.getWriter().print(result);
				return;
			}
			if(busCode == null || "".equals(busCode))
			{
				EmpExecutionContext.error("客户群组群发高级设置存为默认参数异常！busCode："+busCode);
				response.getWriter().print(result);
				return;
			}
			if(priority == null || "".equals(priority))
			{
				EmpExecutionContext.error("客户群组群发高级设置存为默认参数异常！priority："+priority);
				response.getWriter().print(result);
				return;
			}
			if(spUser == null || "".equals(spUser))
			{
				EmpExecutionContext.error("客户群组群发高级设置存为默认参数异常！spUser："+spUser);
				response.getWriter().print(result);
				return;
			}
			if(isReply == null || "".equals(isReply))
			{
				EmpExecutionContext.error("客户群组群发高级设置存为默认参数异常！isReply："+isReply);
				response.getWriter().print(result);
				return;
			}

			//原记录删除条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userid", lguserid);
			conditionMap.put("flag", flag);
			
			//更新对象
			LfDfadvanced lfDfadvanced = new LfDfadvanced();
			lfDfadvanced.setUserid(Long.parseLong(lguserid));
			lfDfadvanced.setBuscode(busCode);
			lfDfadvanced.setPriority(priority);
			lfDfadvanced.setSpuserid(spUser);
			lfDfadvanced.setReplyset(Integer.parseInt(isReply));
			lfDfadvanced.setFlag(Integer.parseInt(flag));
			lfDfadvanced.setCreatetime(new Timestamp(System.currentTimeMillis()));
			//高级设置存为默认
			result = clientSmsBiz.setDefault(conditionMap, lfDfadvanced);
			//操作结果
			String opResult = "客户群组群发高级设置存为默认失败。";
			if(result != null && "seccuss".equals(result))
			{
				opResult = "客户群组群发高级设置存为默认成功。";
			}
			
			//操作员信息
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			//操作员姓名
			String opUser = sysuser==null?"":sysuser.getUserName();
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务编码，发送级别，SP账号，回复设置](").append(busCode).append("，").append(priority).append("，")
			.append(spUser).append("，").append(isReply).append(")");
			
			//操作日志
			EmpExecutionContext.info("客户群组群发", lgcorpcode, lguserid, opUser, opResult + content.toString(), "OTHER");
			
			response.getWriter().print(result);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "客户群组群发高级设置存为默认异常！");
			response.getWriter().print(result);
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
		int msgLen = 980;
		
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
			EmpExecutionContext.error(e,"获取发送账户"+spUser+"绑定的通道信息异常！");
		}finally
		{
			response.getWriter().print(info);
		}
	}

    /**
     * 保存草稿箱
     * @param request
     * @param response
     */
    public void toDraft(HttpServletRequest request, HttpServletResponse response)
    {

        //草稿箱id
        String draftid=request.getParameter("draftId");
        //发送主题
        String taskname=request.getParameter("taskname");
        //操作员ID
        //String lguserid=request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		// 企业编码
        String corpCode = request.getParameter("lgcorpcode");
        // 客户群组id集合
        String groupStr = request.getParameter("groupStr");
        //签约套餐id集合
        String ydywGroupStr=request.getParameter("ydywGroupStr");
        // 客户机构id集合
        String depIdStr = request.getParameter("depIdStr");
        // 客户属性id集合
        String proIdStr = request.getParameter("proIdStr");
        // 客户属性值id集合
        String proValueIdStr = request.getParameter("proValueIdStr");

        // 高级搜索参数
        // 选择状态,是否全选,true是全选，false是未全选
        String selectAllStatus = request.getParameter("selectAllStatus");
        // 未全选时的手机号码
        String phoneStr12 = request.getParameter("phoneStr12");
        // 全选时的未选中客户ID
        String unChioceUserIds = request.getParameter("unChioceUserIds");
        // 全选时的查询条件
        String conditionsqlTemp = request.getParameter("conditionsqlTemp");
        // 发送内容
        String msg = request.getParameter("msg");

        //选取的草稿箱 文件路径
        String selDraftFilePath = request.getParameter("containDraft");
        //处理返回结果
        JSONObject json = new JSONObject();
        json.put("ok","0");

        try
        {
            //发送的手机号码的集合
            List<String> phoneList=new ArrayList<String>();
            // 0.处理草稿箱文件
            if(StringUtils.isNotBlank(selDraftFilePath)){
                TxtFileUtil txtFileUtil = new TxtFileUtil();
                String webRoot = txtFileUtil.getWebRoot();
                File draftFile = new File(webRoot, selDraftFilePath);
                if (!draftFile.exists() && StaticValue.getISCLUSTER() == 1) {
                    CommonBiz comBiz = new CommonBiz();
                    String downloadRes = "error";
                    //最大尝试次数
                    int retryTime = 3;
                    while (!"success".equals(downloadRes) && retryTime-- > 0) {
                        downloadRes = comBiz.downloadFileFromFileCenter(selDraftFilePath);
                    }
                    if (!"success".equals(downloadRes)) {
                        EmpExecutionContext.error("客户群组群发草稿箱文件从文件服务器下载失败。");
                    }
                }
                if (!draftFile.exists()) {
                    EmpExecutionContext.error("客户群组群发未找到草稿箱发送文件！");
                    //草稿箱号码文件不存在
                    json.put("ok", "-1");
                    return;
                }

                BufferedReader bufferedReader = null;
                try {
                    //读取时编码 保持与写入时一致
                    bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(draftFile),"gbk"));
                    String tmp = null;
                    while ((tmp = bufferedReader.readLine()) != null){
                        phoneList.add(tmp.trim());
                    }
                } catch (Exception e){
                    EmpExecutionContext.error("客户群组群发读取草稿箱发送文件异常！");
                    return;
                }finally {
                    if(bufferedReader != null){
                        bufferedReader.close();
                    }
                }
            }
            // 1.界面传过来的手机号码
            String phoneViewStr = request.getParameter("phoneStr1");
            if(phoneViewStr != null && !"".equals(phoneViewStr))
            {
                clientSmsBiz.phoneStrAddList(phoneList, phoneViewStr);
            }

            // 2.处理微信用户手机号码
            String wxphoneStr = request.getParameter("wxphoneStr");
            if(wxphoneStr != null && !"".equals(wxphoneStr))
            {
                clientSmsBiz.phoneStrAddList(phoneList, wxphoneStr);
            }

            //8.处理签约用户手机号码
            String ydywPhoneStr=request.getParameter("ydywPhoneStr");
            if(ydywPhoneStr!=null&&!"".equals(ydywPhoneStr)){
                clientSmsBiz.phoneStrAddList(phoneList, ydywPhoneStr);
            }

            // 3.获取客户机构的手机号码
            if(depIdStr!=null&&depIdStr.length() > 1&&!",".equals(depIdStr))
            {
                // 通过机构id查找电话
                clientSmsBiz.getClientPhoneStrByDepId(phoneList,depIdStr,corpCode);
            }
            // 4.获取客户群组的手机号码
            if(groupStr!=null&&groupStr.length() > 0&&!",".equals(groupStr))
            {
                // 通过群组查找电话
                clientSmsBiz.getClientByGroupStr(phoneList,groupStr,corpCode);
            }
            // 5.获取客户属性的手机号码
            if(proIdStr!=null&&proIdStr.length() > 0&&!",".equals(proIdStr))
            {
                clientSmsBiz.getClientByProIdStr(phoneList,corpCode, proIdStr);
            }
            // 6.获取客户属性值的手机号码
            if(proValueIdStr!=null&&proValueIdStr.length() > 0&&!",".equals(proValueIdStr))
            {
                clientSmsBiz.getClientByProValueIdStr(phoneList,corpCode, proValueIdStr);
            }

            //9.获取签约套餐的手机号码
            if(ydywGroupStr!=null&&ydywGroupStr.length() > 0&&!",".equals(ydywGroupStr))
            {
                // 通过套餐编码查找电话
                clientSmsBiz.getClientByYdywGroupStr(phoneList,ydywGroupStr,corpCode);
            }

            // 7.高级搜索的手机号码
            // 7.(1)高级搜索全选的手机号码
            if("true".equals(selectAllStatus))
            {
                // 高级搜索全选时，通过传递过来的查询条件，找到符合条件的客户，然后将手机号拼接成字符串
                clientSmsBiz.getClientByConditionSqlNew(phoneList,corpCode, conditionsqlTemp, unChioceUserIds);
            }
            //7.(2)高级搜索未全选的手机号码
            else
            {
                // 高级搜索未全选，直接拼接手机号码
                if(phoneStr12 != null && !"".equals(phoneStr12))
                {
                    clientSmsBiz.phoneStrAddList(phoneList, phoneStr12);
                }
            }
            //草稿箱对象
            LfDrafts drafts = new LfDrafts();

            //写入草稿箱文件
            TxtFileUtil txtFileUtil = new TxtFileUtil();
            String physicsUrl = "";
            String webRoot = txtFileUtil.getWebRoot();

            String uploadPath = StaticValue.FILEDIRNAME + "drafttxt/";
            //构建年月日目录结构
            String dirPath = txtFileUtil.createDir(webRoot + uploadPath, Calendar.getInstance());
            GetSxCount sx = GetSxCount.getInstance();
            //保存文件名
            String saveName = "draft" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(System.currentTimeMillis()) + sx.getCount() + ".txt";
            //每次保存生成新的草稿文件路径
            String newDraftFilePath = uploadPath + dirPath + saveName;
            //全路径
            physicsUrl = webRoot + newDraftFilePath;
            //还没有保存过草稿
            if(draftid == null || "".equals(draftid.trim())) {
                drafts.setCorpcode(corpCode);
                drafts.setCreatetime(new Timestamp(System.currentTimeMillis()));
                drafts.setDraftstype(3);
                drafts.setUserid(Long.parseLong(lguserid));
                drafts.setMobileurl(newDraftFilePath);
            }else{
                drafts.setId(Long.parseLong(draftid));
                drafts.setMobileurl(newDraftFilePath);
            }

            //若文件存在 则清空内容 否则 生成新文件
            txtFileUtil.emptyTxtFile(physicsUrl);

            //号码文件全新写入
            String line = System.getProperties().getProperty(StaticValue.LINE_SEPARATOR);
            StringBuffer contentSb = new StringBuffer("");
            //统计号码数
            int count = 0;
            for (String phone : phoneList) {
                contentSb.append(phone+line);
                count++;
                //达到5000 写一次文件
                if(count % 5000 == 0){
                    txtFileUtil.writeToTxtFile(physicsUrl,contentSb.toString());
                    contentSb.setLength(0);
                    count = 0;
                }
            }
            if(count > 0)
            {
                // 剩余的号码写文件
                txtFileUtil.writeToTxtFile(physicsUrl, contentSb.toString());
                contentSb.setLength(0);
            }

            //使用集群，将文件上传到文件服务器
            if(StaticValue.getISCLUSTER() ==1){
                //上传文件到文件服务器
                CommonBiz comBiz = new CommonBiz();
                boolean upFileRes = false;
                //最大尝试次数
                int retryTime = 3;
                while (!upFileRes && retryTime-- >0)
                {
                    upFileRes = comBiz.upFileToFileServer(newDraftFilePath);

                }
                if(!upFileRes){
                    EmpExecutionContext.error("客户群组群发草稿箱文件上传文件到服务器失败。错误码：" + IErrorCode.B20023);
                    return;
                }
            }

            boolean result = false;
            drafts.setUpdatetime(new Timestamp(System.currentTimeMillis()));
            //若参数为空 则默认赋值一个空格 （oracle数据库兼容）
            drafts.setMsg(StringUtils.defaultIfEmpty(msg," "));
            drafts.setTitle(StringUtils.defaultIfEmpty(taskname," "));
            // 主题：taskname 内容：msg 手机号文件路径：physicsUrl 时间
            if(draftid == null || "".equals(draftid.trim())){
                Long id = baseBiz.addObjReturnId(drafts);
                if(id != null && id > 0){
                    drafts.setId(id);
                    result = true;
                }
            }else{
                result = baseBiz.updateObj(drafts);
                if(!result){
                    //草稿箱对应记录已删除
                    json.put("ok","-2");
                    return;
                }
            }

            json.put("draftid",drafts.getId());
            json.put("ok",result?"1":"0");
        }
        catch (EMPException empex)
        {
            EmpExecutionContext.error(empex, lguserid, corpCode);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, lguserid, corpCode);
        }
        finally
        {
            request.setAttribute("result", json.toString());
            try
            {
                request.getRequestDispatcher(PATH + "/kfs_todraft.jsp").forward(request, response);
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e,"客户群组群发暂存草稿处理异常！");
            }
        }
    }
}
