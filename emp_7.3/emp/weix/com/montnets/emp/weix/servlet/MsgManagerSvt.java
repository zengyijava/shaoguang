package com.montnets.emp.weix.servlet;

import java.io.IOException;
import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.weix.biz.MsgBiz;
import com.montnets.emp.weix.biz.WeixBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.entity.weix.LfWcMsg;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

@SuppressWarnings("serial")
public class MsgManagerSvt extends BaseServlet
{
	// 上行消息逻辑层
	private final MsgBiz					msgBiz		= new MsgBiz();
	// 资源路径
	private static final String		PATH		= "/weix/msg";
	
	private final String opModule = "上行消息";
	
	/**
	 * 公众帐号管理页面
	 * 
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 分页信息
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter = pageSet(pageInfo, request);
		try
		{
			// 当前企业编号
			String lgcorpcode = request.getParameter("lgcorpcode");
			
			// 上行消息记录信息
			LinkedHashMap<String, String>	msgtpList	= new LinkedHashMap<String, String>();
			msgtpList = getMsgtpList();
			List<DynaBean> msgbeans = new ArrayList<DynaBean>();
			
			// 查询所有的公众帐号
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);
			List<LfWcAccount> lfWcAccList = new ArrayList<LfWcAccount>();
			lfWcAccList = new BaseBiz().getByCondition(LfWcAccount.class, conditionMap, null);
			
			if(!isFirstEnter)
			{
				// 获取查询条件
				// 公众帐号
				String aId = request.getParameter("aId");
				// 消息类型
				String msgtp = request.getParameter("msgtp");
				// 微信名称
				String wcname = request.getParameter("wcname").trim();
				// 微信openId
				String openid = wcname;
				// 发送开始时间
				String beginTime = request.getParameter("beginTime");
				String endTime = request.getParameter("endTime");
				if(aId != null && !"".equals(aId.trim()))
				{
					conditionMap.put("aId", aId);
				}
				if(msgtp != null && !"".equals(msgtp.trim()))
				{
					conditionMap.put("msgtp", msgtp);
				}
				if(wcname != null && !"".equals(wcname.trim()))
				{
					conditionMap.put("wcname", wcname);
				}
				if(openid != null && !"".equals(openid.trim()))
				{
					conditionMap.put("openid", openid);
				}
				if(beginTime != null && !"".equals(beginTime.trim()))
				{
					conditionMap.put("beginTime", beginTime);
				}
				if(endTime != null && !"".equals(endTime.trim()))
				{
					conditionMap.put("endTime", endTime);
				}
			}
			//type为“0”，表示上行消息
			conditionMap.put("type", "0");
			msgbeans = msgBiz.findListMsgByCondition(lgcorpcode, conditionMap, pageInfo);
			
			request.setAttribute("msgtpList", msgtpList);
			request.setAttribute("acctList", lfWcAccList);
			request.setAttribute("msgbeans", msgbeans);
			
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, opModule + "-获取上行消息列表报错！");
		}finally{
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(PATH + "/msgList.jsp").forward(request, response);
		}
	}
	
	/**
	 * 微信上行消息类型
	 * 
	 * @return
	 */
	private LinkedHashMap<String, String> getMsgtpList()
	{
		LinkedHashMap<String, String>	msgtpList = new LinkedHashMap<String, String>();
		msgtpList.put("0", "文本");
		msgtpList.put("1", "图片");
		msgtpList.put("2", "地理位置");
		// this.msgtpList.put("3","链接");
		msgtpList.put("4", "事件");
		return msgtpList;
	}
}
