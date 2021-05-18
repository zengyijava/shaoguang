package com.montnets.emp.wxmanager.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.common.AllUtil;
import com.montnets.emp.netnews.daoImpl.Wx_getWapDaoImpl;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxmanager.biz.NetCheckBiz;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 网讯审核servlet
 */
@SuppressWarnings("serial")
public class wx_checkSvt extends BaseServlet {
	private static final String PATH = "/ydwx/manageWX/";
	
	private final BaseBiz baseBiz = new BaseBiz();
	
	/**
	 * 模板查询
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		
		//登录用户id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		String tmName = AllUtil.toStringValue(request.getParameter("tmName"), "");
		String conrstate = request.getParameter("conrstate");
		PageInfo pageInfo=new PageInfo();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		orderByMap.put("frId", StaticValue.DESC);
		try {
			//是否第一次打开
			boolean isFirstEnter = false;
			String skip=request.getParameter("skip");
			if("true".equals(skip)){
				pageInfo=(PageInfo)request.getSession(false).getAttribute("wxcheckPageInfo");
				tmName=(String)request.getSession(false).getAttribute("wxchecktmName");
      			conrstate=(String)request.getSession(false).getAttribute("wxcheckconrstate");
      			if (tmName != null && !"".equals(tmName)) {
					conditionMap.put("RContent&like", tmName);
				}
				//审批状态
				if(conrstate != null && !"".equals(conrstate)){
					//审批完成
					if("3".equals(conrstate)){
						conditionMap.put("isComplete", "1");
						conditionMap.put("RState","-1");
					}else{
						conditionMap.put("RState",conrstate);
					}
				}
			}
			isFirstEnter=pageSet(pageInfo, request);
			conditionMap.put("infoType", "6");
			//不是第一次查询
			if (!isFirstEnter) {
				if (tmName != null && !"".equals(tmName)) {
					conditionMap.put("RContent&like", tmName);
				}
				//审批状态
				if(conrstate != null && !"".equals(conrstate)){
					//审批完成
					if("3".equals(conrstate)){
						conditionMap.put("isComplete", "1");
						conditionMap.put("RState","-1");
					}else{
						conditionMap.put("RState",conrstate);
					}
				}
			}
			conditionMap.put("userCode",lguserid);
			
			List<LfFlowRecord> recordList = baseBiz.getByConditionNoCount(LfFlowRecord.class, null, conditionMap, orderByMap, pageInfo);
			StringBuffer sb = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			if(recordList != null && recordList.size()>0){
				for(LfFlowRecord temp:recordList){
					sb2.append(temp.getMtId()).append(",");
					sb.append(temp.getProUserCode()).append(",");
				}
			}
			List<LfSysuser> userList = null;
			LinkedHashMap<Long,String> usernameMap = new LinkedHashMap<Long, String>();
			if(sb != null && sb.toString().length()>0){
				conditionMap.clear();
				conditionMap.put("userId&in", sb.toString());
				userList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
				if(userList != null && userList.size()>0){
					for(LfSysuser user:userList){
						String name =  user.getName();
						if(user.getUserState() != null && user.getUserState() ==2){
							name = name + "<font color='red'>( "+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_yizhuxiao",request)+")</font>";
						}
						usernameMap.put(user.getUserId(),name);
					}
				}
			}
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+pageInfo.getTotalRec();
				Object sysObj=request.getSession(false).getAttribute("loginSysuser");
				if(sysObj!=null){
					LfSysuser lfSysuser=(LfSysuser)sysObj;
					EmpExecutionContext.info("网讯审核", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), opContent, "GET");
				}
			}
			request.getSession(false).setAttribute("wxcheckconrstate", conrstate);
			request.getSession(false).setAttribute("wxcheckPageInfo", pageInfo);
			request.getSession(false).setAttribute("wxchecktmName", tmName);
			request.setAttribute("skip", skip);
			request.setAttribute("usernameMap", usernameMap);
			request.setAttribute("recordList", recordList);
			request.setAttribute("conrstate",conrstate);
			request.setAttribute("tmName",tmName);
			//分页信息传给页面
			request.setAttribute("pageInfo", pageInfo);
			//页面跳转
			request.getRequestDispatcher(PATH+"wx_netCheck.jsp")
			.forward(request, response);
		} catch (Exception e) {
			//异常打印
			EmpExecutionContext.error(e,"模板查询");
			//错误信息传给页面
			request.setAttribute("findresult", "-1");
			//分页信息传给页面
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(PATH+"wx_netCheck.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"跳转异常");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"IO异常");
			}
		}
	}

	/**
	 * 单个修改
	 * 
	 * @param request
	 * @param response
	 */

	@SuppressWarnings("unchecked")
	public void update(HttpServletRequest request, HttpServletResponse response) {
		//String corpCode = request.getParameter("lgcorpcode");
	//	String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);
		try {
			String netid = request.getParameter("netid");
			String type = request.getParameter("type");// 判断要执行的操作是审核通过还是未通过,1为通过，2为未通过
			if ("2".equals(type)) {
				// 审核通过，生成网讯页面
				Wx_getWapDaoImpl wx_getWapDao = new Wx_getWapDaoImpl();
				ResourceBundle bundle = ResourceBundle
						.getBundle("resourceBundle");
				String TsType = bundle.getString("montnets.wx.Phone.type");
				//String dm = request.getParameter("dm");
				String iphORhe = "0"; // 0其它手机，1IPHONE手机
				if ("TS".equals(TsType)) {
					Enumeration headers = request.getHeaderNames();
					while (headers.hasMoreElements()) {
						String head = (String) headers.nextElement();
						if ("user-agent".equals(head)) {
							String headValue = request.getHeader(head);
							if (headValue.indexOf("iPhone") > -1
									|| headValue.indexOf("Mac OS") > -1) {
								iphORhe = "1";
								break;
							}
						}
					}
				}
				/**
				 * 网讯审核生成网讯访问页面
				 */
				wx_getWapDao.getNetByJsp(netid, iphORhe,null,null,request.getContextPath());
			}
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("STATUS", type);
			objectMap.put("AUDITUSERID", userId);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("NETID", netid);
			boolean b = baseBiz.update(LfWXBASEINFO.class, objectMap, conditionMap);
			
			if(b){
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser sysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("网讯审核", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "网讯审核成功。[网讯ID]（"+netid+"）", "OTHER");
				}
			}else{
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser sysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("网讯审核", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "网讯审核失败。[网讯ID]（"+netid+"）", "OTHER");
				}
			}
			response.getWriter().print(b);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网讯单个修改");
		}
	}

	/**
	 * 点击审批/查看操作
	 * @param request
	 * @param response
	 */
	public void getExamineInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		try 
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			// 审核流程id
			String frid = request.getParameter("frid");
			// 审核流程信息
			LfFlowRecord record = baseBiz.getById(LfFlowRecord.class, frid);

			// 模板对象
			LfWXBASEINFO temp = baseBiz.getById(LfWXBASEINFO.class, record.getMtId());

			int level = record.getRLevel();
			conditionMap.put("RLevel&<", String.valueOf(level + 1));
			// 审核通过与不通过都显示
			conditionMap.put("RState&in", "1,2");
			// 相同审核流程
			conditionMap.put("FId", record.getFId().toString());
			// 相同任务
			conditionMap.put("mtId", record.getMtId().toString());
			// 等级逆序排序
			orderbyMap.put("RLevel", StaticValue.DESC);
			//审批历史记录
			List<LfFlowRecord> recordList = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderbyMap);

			StringBuffer sb = new StringBuffer();
			sb.append(record.getProUserCode()).append(",");
			if(recordList != null && recordList.size()>0){
				for(LfFlowRecord re:recordList){
					sb.append(re.getUserCode()).append(",");
				}
			}
			List<LfSysuser> userList = null;
			LinkedHashMap<Long,String> usernameMap = new LinkedHashMap<Long, String>();
			if(sb != null && sb.toString().length()>0){
				conditionMap.clear();
				conditionMap.put("userId&in", sb.toString());
				userList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
				if(userList != null && userList.size()>0){
					for(LfSysuser user:userList){
						String name =  user.getName();
						if(user.getUserState() != null && user.getUserState() ==2){
							name = name + "<font color='red'>("+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_yizhuxiao",request)+")</font>";
						}
						usernameMap.put(user.getUserId(),name);
					}
				}
			}

			request.setAttribute("usernameMap", usernameMap);
			request.setAttribute("curRecord", record);
			request.setAttribute("temp", temp);
			request.setAttribute("recordList", recordList);
			request.getRequestDispatcher(PATH+ "wx_examine.jsp")
					.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"点击审批/查看操作");
		}
	}
	
	/**
	 * 审批操作
	 * @param request
	 * @param response
	 */
	public void review(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//模板id
			String frId = request.getParameter("frId");
			//审批意见
			String cont = request.getParameter("cont");
			//状态
			String rState = request.getParameter("rState");
			if (frId != null && !"".equals(frId))
			{
				//审批，并返回结果
				boolean result = new NetCheckBiz().reviewTemplate(Long.parseLong(frId), Integer.parseInt(rState), cont);
				if(result){
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser sysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("网讯审核", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "网讯模板审核成功。[模板id]（"+frId+"）", "OTHER");
					}
				}else{
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser sysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("网讯审核", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "网讯模板审核失败。[模板id]（"+frId+"）", "OTHER");
					}
				}
				
				response.getWriter().print(result);
			}else
			{
				response.getWriter().print("false");
			}
		} catch (Exception e)
		{
			//异常打印
			EmpExecutionContext.error(e,"审批操作");
		}
	}
}
