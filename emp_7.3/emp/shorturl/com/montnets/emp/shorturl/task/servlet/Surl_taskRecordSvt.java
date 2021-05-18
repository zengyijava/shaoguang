package com.montnets.emp.shorturl.task.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.JsonReceviceUtil;
import com.montnets.emp.common.tools.JsonReturnUtil;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.shorturl.surlmanage.entity.LfUrlTask;
import com.montnets.emp.shorturl.surlmanage.vo.LfUrlTaskVo;
import com.montnets.emp.shorturl.task.biz.SurlTaskRecordBiz;
import com.montnets.emp.util.PageInfo;

public class Surl_taskRecordSvt extends BaseServlet{

	private static final long serialVersionUID = -7813447250937056973L;
	// 模块名称
	final String				empRoot	= "shorturl";

	// 功能文件夹名
	final String				base	= "/urltaskRecord";
	
	final BaseBiz baseBiz = new BaseBiz();
	final SurlTaskRecordBiz mtBiz = new SurlTaskRecordBiz();
	
	
	/**
	 * 查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		long startTime = System.currentTimeMillis();
		List<LfUrlTaskVo> mtVoList = null;
		LfUrlTaskVo mtVo = new LfUrlTaskVo();
		PageInfo pageInfo = new PageInfo();
		//当前登录用户的企业编码
		String corpCode = "";
		String  userId = "";
		Map<String, String> usersMap = new HashMap<String, String>();
		try {
			//初始化分页信息
			pageSet(pageInfo, request);
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, null);
			
			//登录操作员信息
			LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//当前登录用户的企业编码
			corpCode =loginSysuser.getCorpCode();
			userId =String.valueOf(loginSysuser.getUserId());
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			//如果不是10000用户登录，则需要带上企业编码查询
			if(corpCode !=null && !corpCode.equals("100000"))
			{
				conditionMap.put("corpCode", corpCode);
			}
			conditionMap.put("isValidate", "1");
			 //查找绑定的sp账号  
			//查询界面使用
			List<LfSpDepBind> userList = baseBiz.getByCondition(LfSpDepBind.class, conditionMap, null);
			
			//sp账号
			String spUser = request.getParameter("spUser");
			//机构
			String depid = request.getParameter("depid");
			 //机构名称
			String depNam = request.getParameter("depNam");
			//是否包含子机构
            String isContainsSun=request.getParameter("isContainsSun");
			//操作员
			String userid = request.getParameter("userid");
			String origUserid = userid;
			//操作员名字
			String userName = request.getParameter("userName");
			//创建开始时间
			String startSubmitTime = request.getParameter("startSubmitTime");
			//创建结束时间
			String endSubmitTime = request.getParameter("endSubmitTime");
			//任务状态
            String taskState= request.getParameter("taskState");
            //发送类型
            String taskType= request.getParameter("taskType");
            //任务批次查询条件
            String taskID=request.getParameter("taskID");
            //信息内容
            String msg=request.getParameter("msg");
           
			conditionMap.clear();
			conditionMap.put("spUser", spUser);
			conditionMap.put("depid", depid);
			conditionMap.put("depNam", depNam);
			conditionMap.put("userid", userid);
			conditionMap.put("userName", userName);
			conditionMap.put("startSubmitTime", startSubmitTime);
			conditionMap.put("endSubmitTime", endSubmitTime);
			conditionMap.put("taskState", taskState);
			conditionMap.put("taskType", taskType);
			conditionMap.put("taskID", taskID);
			conditionMap.put("msg", msg);
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("corpUserId", userId);
			conditionMap.put("isContainsSun", isContainsSun);

			mtVoList = mtBiz.find(conditionMap,pageInfo);
			//可查询的用户id 用户名
			usersMap = mtBiz.getUsers(userId);
			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("mtVoList", mtVoList);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("usersMap",usersMap);
			request.setAttribute("userList",userList);
			request.setAttribute("userId",origUserid);
			request.setAttribute("userName",userName);

			
			request.getRequestDispatcher(this.empRoot  + this.base+"/url_taskRecord.jsp")
			.forward(request, response);
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "企业短链群发任务查看查询异常");
		}
		
		
		
		
		
	}
	
	public void isOverTimerTime(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		try {
			String  id = request.getParameter("id");
			LfUrlTask task = baseBiz.getById(LfUrlTask.class,
					id); 
			JSONObject jsonObject = JSON.parseObject(task.getRemark());
			// 是否定时
			String timerStatus = jsonObject.getString("timerStatus");
			// 定时时间
			String timerTime = jsonObject.getString("timerTime");
			Integer timerStatus1 = (timerStatus == null || "".equals(timerStatus)) ? 0 : Integer.valueOf(timerStatus);
			//如果为定时
			if (timerStatus1 == 1) {
				//获取当前时间
				Calendar curCalendar =Calendar.getInstance();				
				String cur = sdf.format(curCalendar.getTime());
				Calendar timerCalendar = Calendar.getInstance();
				timerCalendar.setTime(sdf.parse(timerTime));
				timerCalendar.add(Calendar.MINUTE, -10);
				String ti = sdf.format(timerCalendar.getTime());
				if(timerCalendar.after(curCalendar)){
					response.getWriter().print("true");
				}else {
					response.getWriter().print("timeOver");
				}
			}else {
				response.getWriter().print("true");
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "短链接重发失败");
			response.getWriter().print("false");
		}
		
	}
	
	
	
	public void resend(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		
		try {
			String  id = request.getParameter("id");
			String  type = request.getParameter("type");
			LfUrlTask task = baseBiz.getById(LfUrlTask.class,
					id); 
			
			Integer urlStatus =task.getStatus();
			if (urlStatus == 13 || urlStatus == 23) {
				
				boolean re = mtBiz.reSend(id,urlStatus,type,task);
				if(re){
					response.getWriter().print("true");
				}else {
					response.getWriter().print("false");
				}
			}else {
				response.getWriter().print("repeat");
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "短链接重发失败");
			response.getWriter().print("false");
		}
		
	}

	public void getFailDetails(HttpServletRequest request, HttpServletResponse response) {
        String id = null;
	    JSONObject jsonObject = JsonReceviceUtil.parseToJson(request);
        if(null != jsonObject) {
            id = jsonObject.getString("id");
        }
        Map<String, Object> result = mtBiz.getFailDetails(id);
        JsonReturnUtil.success(result, "", request, response);

    }

}
