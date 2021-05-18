package com.montnets.emp.shorturl.samesms.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.shorturl.surlmanage.biz.UrlSendBiz;
import com.montnets.emp.shorturl.surlmanage.entity.LfDfadvanced;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomain;
import com.montnets.emp.shorturl.surlmanage.util.TxtFileUtil;

@SuppressWarnings("serial")
public class Surl_sendBatchSMSSvt extends BaseServlet {

	private final BaseBiz baseBiz = new BaseBiz();
	private final SmsBiz smsBiz = new SmsBiz();
	private final CommonBiz commonBiz = new CommonBiz();
	//写文件时候要的换行符
	private final String empRoot="shorturl";
	private final TxtFileUtil txtFileUtil = new TxtFileUtil();
	
	public void find(HttpServletRequest request, HttpServletResponse response) {
		String lgcorpcode = null;
		Long lguserid = null;
		try {
			//用户的请求地址
			String s = request.getRequestURI();
			//是短信客服模块的相同内容还是短信助手模块
			String hjsp = s.substring(s.lastIndexOf("_")+1,s.lastIndexOf("."));
			
			//登录操作员信息
			LfSysuser curSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//当前登录企业
			lgcorpcode = curSysuser.getCorpCode();
			//当前登录操作员id
			lguserid = curSysuser.getUserId();
			//当前登录操作员对象
			//LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

			LinkedHashMap<String, String> conditionbusMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
			conditionbusMap.put("corpCode&in", "0," + lgcorpcode);
			orconp.put("corpCode", "asc");
			
			//设置启用查询条件
			conditionbusMap.put("state", "0");
			//设置查询手动和手动+触发
			conditionbusMap.put("busType&in", "0,2");
			
			//根据企业编码查询业务类型
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionbusMap, orconp);
			request.setAttribute("busList", busList);
			
			List<Userdata> spUserList = smsBiz.getSpUserList(curSysuser);
			request.setAttribute("sendUserList", spUserList);
//			UserdataAtom userdataAtom = new UserdataAtom();
//			//发送账户存放内存Map
//			userdataAtom.setUserdata(spUserList);
			
			//获取高级设置默认信息
			conditionMap.clear();
			conditionMap.put("userid", String.valueOf(lguserid));
			//1:相同内容群发
			conditionMap.put("flag", "1");
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			orderMap.put("id", StaticValue.DESC);
			List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, conditionMap, orderMap);
			LfDfadvanced lfDfadvanced = null;
			if(lfDfadvancedList != null && lfDfadvancedList.size() > 0)
			{
				lfDfadvanced = lfDfadvancedList.get(0);
			}
			request.setAttribute("lfDfadvanced", lfDfadvanced);
			
			conditionMap.clear();
			//短信模板
			conditionMap.put("tmpType", "3"); 
			//有效
			conditionMap.put("tmState", "1");
			//无需审核或审核通过
			conditionMap.put("isPass&in", "0,1");
			//静态模板
			conditionMap.put("dsflag", "0");
			
			//------------------------------end
			request.setAttribute("reTitle", hjsp);
			LfDep dep = baseBiz.getById(LfDep.class, curSysuser.getDepId());
			if(dep!=null)
			{
			    request.setAttribute("depSign", dep.getDepName());	
			}
			//产生taskId
			Long taskId = commonBiz.getAvailableTaskId();
			//操作日志信息
			String opContent = "获取taskid("+taskId+")成功";
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info("相同内容群发", lgcorpcode, String.valueOf(lguserid), lfSysuser.getUserName(), opContent.toString(), "GET");
			List<LfDomain> domainlist = new UrlSendBiz().findDomainByCorpcode(lgcorpcode);
			request.setAttribute("domainlist", domainlist);
			request.setAttribute("taskId",taskId.toString());
			request.getRequestDispatcher(this.empRoot + "/surlsms/ssm_sendBatchSMS.jsp"  ).forward(
					request, response);

		} catch (Exception e) {
			EmpExecutionContext.error(e,lguserid,lgcorpcode);
			request.setAttribute("findresult", "-1");
			try {
				request.getRequestDispatcher(this.empRoot + "/surlsms/ssm_sendBatchSMS.jsp"  ).forward(
						request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,lguserid,lgcorpcode);
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,lguserid,lgcorpcode);
			}
		}
	}

	//预览之后提交
	public void add(HttpServletRequest request, HttpServletResponse response)
	{
		String result = "";
		// 任务主题
		String title = request.getParameter("taskname");
		//任务ID
		String taskId = request.getParameter("taskId");
		//当前登录操作员id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		//草稿箱id
		String draftId = request.getParameter("draftId");
		// 提交类型
		String bmtType = request.getParameter("bmtType");
		// 发送账号
		String spUser = request.getParameter("spUser");
		
		String srcFileUrl = request.getParameter("hidDzUrl");
		
		
		// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
		String sendType = request.getParameter("sendType");
		EmpExecutionContext.info("[OTHER] mod：相同内容群发，发送servlet接收参数，userid:"+lguserid
				+"，corpCode:"+lgcorpcode+"，taskId:"+taskId+"，sendType:"+sendType+"，bmtType:"+bmtType+"，spUser:"+spUser);
		//主题为默认时,直接返回(防止重发)
		if(title != null && "不作为短信内容发送".equals(title.trim()))
		{
			EmpExecutionContext.error("相同内容发送获取参数异常，" + "title:" + title+"，taskId："+taskId+"，errCode："+IErrorCode.V10001);
			result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.error(result);
		}
		else
		{
			//保存短地址相关信息到数据库
			UrlSendBiz  urlSendBiz = new UrlSendBiz();
			SmsSendBiz smsSendBiz = new SmsSendBiz();
			// 提交发送
			result = urlSendBiz.send(request,1);
			
			//删除草稿箱，如果有的话
			smsSendBiz.dealDrafts(draftId, result, lguserid, lgcorpcode);
			
			
		}	
		
		try 
		{
			String s = request.getRequestURI();
			
			request.getSession(false).setAttribute("surl_batchResult", result);
			//重定向
			s = s+"?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&oldTaskId="+taskId+"&t="+System.currentTimeMillis();
			response.sendRedirect(s);
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,lguserid,lgcorpcode);
		}
	}

//	private void createUrlTask(HttpServletRequest request) {
//		try {
//			LfUrlTask urlTask = new LfUrlTask();
//			// 任务主题
//			String title = request.getParameter("taskname");
//			// 信息内容
//			String msg = request.getParameter("msg");
//			// 有效总数
//			String hidSubCount = request.getParameter("hidEffCount");
//			// 任务id
//			String taskId = request.getParameter("taskId");
//			// 当前登录操作员id
//			String strlguserid = request.getParameter("lguserid");
//			// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
//			String sendType = request.getParameter("sendType");
//			//详情文件物理路径
//			String srcFileUrl = request.getParameter("hidDzUrl");
//			//企业编码
//			String lgcorpcode = request.getParameter("lgcorpcode");
//			//原始地址Id
//			String srcId = request.getParameter("srcId");
//			LfSysuser user = baseBiz.getById(LfSysuser.class, strlguserid);
//			
//			urlTask.setTitle(title);
//			urlTask.setMsg(msg==null?"":msg);
//			urlTask.setSubCount(Integer.valueOf(hidSubCount));
////			urlTask.setVisCount(0);
////			urlTask.setVisNum(0);
//			urlTask.setTaskId(Integer.valueOf(taskId));
//			urlTask.setCorpCode(lgcorpcode);
//			urlTask.setCreateUid(Integer.valueOf(strlguserid));
////			urlTask.setDepId(user.getDepId().intValue());
//			urlTask.setUtype(1);
//			urlTask.setSrcFile(srcFileUrl);
//			urlTask.setUrl(srcFileUrl);
//			urlTask.setHandleLine(0);
////			urlTask.setHandleNode(StaticValue.SERVER_NUMBER);
//			urlTask.setSubmittm(new Timestamp(new Date().getTime()));
//			urlTask.setUpdatetm(new Timestamp(new Date().getTime()));
//			urlTask.setStatus(1);
////			urlTask.setNetUrlId(Integer.valueOf(srcId));
//			Long urlTaskID = baseBiz.addObjReturnId(urlTask);
//			urlTask.setId(urlTaskID);
//			boolean re = timerBiz.execute(urlTask);
//			if (!re) {
//				EmpExecutionContext.error("短链接入库任务失败！！！");
//			}
//		} catch (Exception e) {
//			EmpExecutionContext.error(e, "短链接入库任务生成异常");
//		}
//	}
	
	/**
	 * 读取用来存储预览十条信息的文件并显示
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void readSmsContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		BufferedReader reader = null;
		String url = "";
		try
		{
			PrintWriter writer = response.getWriter();
			// 预览文件地址
			url = request.getParameter("url");
			// 地址处理
			url = txtFileUtil.getPhysicsUrl(url);
			// 发送类型
			String sendType = request.getParameter("sendType");
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(url)), "GBK"));
			String tmp = "";
			String phoneStr = "";
			//
			String smsContent = "";
			int index;
			int x = 0;
			CommonVariables cv = new CommonVariables();
			Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
			int ishidephome = 0;
			if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null)
			{
				ishidephome = 1;
			}
			// 动态模板短信发送
//			if("2".equals(sendType))
//			{
//				writer.print("<thead><tr align='center'><th>编号</th><th><center><div style='width:89px'>手机号码</div></center></th>" + "<th>文件内参数</th><th>文件内参数个数</th>" + "<th>短信内容</th></tr></thead><tbody>");
//				// 逐行读取
//				while((tmp = reader.readLine()) != null)
//				{
//					x++;
//					tmp = tmp.trim();
//					// 内容截取
//					String[] snum = tmp.split("MWHS]#");
//					// 手机号特殊处理
//					String phone = snum[2] != null ? snum[2] : "";
//					if(phone != null && !"".equals(phone) && ishidephome == 0)
//					{
//						phone = cv.replacePhoneNumber(phone);
//					}
//					writer.print("<tr align ='center'><td>" + x + "</td><td>" + phone + "</td>" + "<td>" + snum[0] + "</td><td>" + snum[1] + "</td>" + "<td><xmp style='word-break: break-all;white-space:normal;'>" + snum[3] + "</xmp></td></tr>");
//				}
//				writer.print("</tbody>");
//			}
			// 文件内容短信发送
//			else
//			{
				writer.print("<thead><tr align='center'><th>编号</th>" + "<th><center><div style='width:89px'>手机号码</div></center></th>" + "<th>短信内容</th><th>手机预览</th></thead><tbody>");
				while((tmp = reader.readLine()) != null)
				{
					x++;
					tmp = tmp.trim();
					index = tmp.indexOf("MWHS]#");
					phoneStr = tmp.substring(0, index);
					smsContent = tmp.substring(index + 6).trim();
					// 手机号特殊处理
					String phone = phoneStr != null ? phoneStr : "";
					if(phone != null && !"".equals(phone) && ishidephome == 0)
					{
						phone = cv.replacePhoneNumber(phone);
					}
					writer.print("<tr align ='center'><td >" + x + "</td><td>" + phone + "</td>" + "<td ><xmp style='word-break: break-all;white-space:normal;'>" + smsContent + "</xmp></td><td><label><a href='javascript:showUrl();'>预览</a></label></td></tr>");
				}
				writer.print("</tbody>");
			//}
//			reader.close();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "不同内容群发，读取用于存储预览信息的文件失败！url:" + url);
		}finally{
			if(null != reader){
				try{
					reader.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"IO关闭异常");
				}
			}
		}
	}
	/**
	 * 发送后跳转(暂不使用)
	 * @param result
	 * @param lguserid
	 * @param lgcorpcode
	 * @param response
	 */
	private void goFind(String result, String lguserid, String lgcorpcode, HttpServletRequest request, HttpServletResponse response)
	{
		try 
		{
			String s = request.getRequestURI();

			request.getSession(false).setAttribute("mcs_batchResult", result);

			s = s+"?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&t="+System.currentTimeMillis();
			response.sendRedirect(s);
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,lguserid,lgcorpcode);
		}
	}
	
}
