package com.montnets.emp.pasgroup.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.pasgroup.biz.ProxyMageBiz;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.pasgroup.entity.Userdata;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

public class pag_proxyMageSvt extends BaseServlet{
	final ErrorLoger errorLoger = new ErrorLoger();

	//操作模块
	final String opModule=StaticValue.GATE_CONFIG;
	//操作用户
	final String opSper = StaticValue.OPSPER;
	final ProxyMageBiz proxybiz=new ProxyMageBiz();
	private final String empRoot = "txgl";
	private final String basePath = "/proxymanger";
	
	/**
	 * 代理账号查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{

		long stime = System.currentTimeMillis();
		boolean isFirstEnter;
		PageInfo pageInfo = new PageInfo();

		try {
			List<Userdata> userdataList = null;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			isFirstEnter = pageSet(pageInfo,request);
			if(!isFirstEnter){
				//账户名称
				conditionMap.put("STAFFNAME&like", request.getParameter("staffName"));
				conditionMap.put("USERID&like", request.getParameter("userId").toUpperCase());
				//账户状态
				conditionMap.put("STATUS", request.getParameter("status"));

			}
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//企业编码
			//String corpCode = request.getParameter("lgcorpcode");
			String corpCode = lfSysuser.getCorpCode();

			userdataList = proxybiz.findSpUser(conditionMap, pageInfo);
			//加密后的集合(SP账号)
			Map<String, String> keyIdMap = new HashMap<String, String>();
			//加密后的集合(UID)
			Map<Long, String> keyIdUidMap = new HashMap<Long, String>();
			//ID加密
			if(userdataList != null && userdataList.size() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptToMapKeyStr(userdataList, "UserId", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询代理账户列表，参数加密失败。corpCode:"+corpCode);
						throw new Exception("查询代理账户列表，参数加密失败。");
					}
					result = encryptOrDecrypt.batchEncryptToMap(userdataList, "Uid", keyIdUidMap);
					if(!result)
					{
						EmpExecutionContext.error("查询代理账户列表，参数UID加密失败。corpCode:"+corpCode);
						throw new Exception("查询代理账户列表，参数UID加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询代理账户列表，从session中获取加密对象为空！");
					userdataList.clear();
					throw new Exception("查询代理账户列表，获取加密对象失败。");
				}
			}
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("userdataList", userdataList);
			request.setAttribute("keyIdUidMap", keyIdUidMap);
			request.setAttribute("keyIdMap", keyIdMap);
			request.setAttribute("conditionMap", conditionMap);
			request.getRequestDispatcher(empRoot  + basePath +"/pag_proxyMage.jsp")
				.forward(request, response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"代理账号","("+sDate+")查询",StaticValue.GET);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"代理账户查询异常！"));

			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(empRoot + basePath +"/pag_proxyMage.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"代理账户查询servlet异常！"));
			} catch (IOException e1) {				
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"代理账户查询servlet跳转异常！"));
			}
		}
	
		
	}
	
	/**
	 * 检查账户名称
	 * @param request
	 * @param response
	 */
	public void checkName(HttpServletRequest request, HttpServletResponse response)
	{
//		Userdata user = new Userdata();
		//账号ID
		String userid="";
		try
		{
			userid=request.getParameter("userid").toUpperCase();
			//查询表中是否已存在该账号
			boolean isSameAccount = proxybiz.getUserdataByUserid(userid);
			//不存在在该账号，返回true
			if (!isSameAccount)
			{
				response.getWriter().print(true);
			}
			else
			{
				response.getWriter().print(false);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"检查代理账户是否存在异常！userid:"+userid));
		}
	}
	
	/**
	 * 新建代理账户页面跳转
	 * @param request
	 * @param response
	 */
	public void toAdd(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String corpCode = (String)request.getAttribute("lgcorpcode");
			request.getRequestDispatcher(empRoot  + basePath +"/pag_addProxyMage.jsp?lgcorpcode="+corpCode)
				.forward(request, response);
		}catch(Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"新建sp账户页面跳转异常！"));
		}
	}
	
	/**
	 * 写日志
	 * 
	 * @param request
	 *        请求对象
	 * @param opModule
	 *        菜单名称
	 * @param opContent
	 *        操作内容
	 * @param opType
	 */
	private void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,opModule+opType+opContent+"日志写入异常"));
		}
	}
	/**
	 * 获取加密对象
	 * @description    
	 * @param request
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 下午07:29:28
	 */
	public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt(HttpServletRequest request)
	{
		try
		{
			ParamsEncryptOrDecrypt encryptOrDecrypt = null;
			//加密对象
			Object encrypOrDecrypttobject = request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("代理账户，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "代理账户，从session获取加密对象异常。");
			return null;
		}
	}
	
	/**
	 * 修改代理账户
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response)
	{
		String opUser = request.getParameter("lgusername");
		opUser = opUser==null?"":opUser;
		String corpCode = "";
		//操作类型
		String hidOpType = request.getParameter("hidOpType");
		SuperOpLog spLog = new SuperOpLog();
		String opType = null;
		//记录日志总字符串
		StringBuffer opContent = new StringBuffer("");
		//修改前数据字符串
		StringBuffer oldStr=new StringBuffer("");
		//修改后数据字符串
		StringBuffer newStr=new StringBuffer("");
		try
		{
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpCode = lfSysuser.getCorpCode();
			String userid;
			//如果是修改,账号ID解密
			if(hidOpType.equals("edit"))
			{
				String keyId = request.getParameter("keyId");
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					//解密
					userid = encryptOrDecrypt.decrypt(keyId);
					if(userid == null)
					{
						EmpExecutionContext.error("修改代理账号，参数解密码失败，keyId:"+keyId);
						throw new Exception("修改代理账号，参数解密码失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("修改代理账号，从session中获取加密对象为空！");
					throw new Exception("修改代理账号，获取加密对象失败。");
				}
			}
			//新增,从界面获取
			else
			{
				//账户id
				userid=request.getParameter("userid");
			}
			//账户密码
			String userpassword=request.getParameter("userpassword");
			String curPass = request.getParameter("curPass");
			//账户名称
			String staffname=request.getParameter("staffname");
			//ip地址
			String ips = request.getParameter("ips");
			if(ips==null||ips.length()<1){
				ips=" ";
			}
			Userdata user=new Userdata();
			//扣费类型(1:预付费,2:后付费)
			user.setFeeFlag(2l);
			user.setUserId(userid.toUpperCase());
			user.setStaffName(staffname);
			//
			user.setUserType(0l);
			user.setRiseLevel(0l);
			user.setAccouttype(1);
			user.setLoginIp(ips);
			user.setLoginId(userid.toUpperCase());
			user.setSptype(0);
			if(curPass == null || !curPass.equals(userpassword))
			{
				user.setUserPassword(userpassword);
			}
			user.setMoUrl(" ");
			user.setRptUrl(" ");
			user.setSpbindurl(" ");
			user.setTransmotype(0l);
			user.setTransrptype(0l);
			user.setSendtmspan("00:00:00-23:59:59");

		
			boolean result = false;
			if(hidOpType.equals("add")){
				user.setOrderTime(new Timestamp(System.currentTimeMillis()));
				user.setModitime(new Timestamp(System.currentTimeMillis()));
				opType=StaticValue.ADD;
				user.setStatus(0);
				result = proxybiz.addUserdata(user,request.getRemoteAddr());
				request.setAttribute("w_userdataResult", "1");
				newStr.append(user.getAccouttype()).append("，").append(user.getUserId()).append("，").append(user.getStaffName())
				.append("，").append(user.getUserPassword()).append("，").append(user.getLoginId()).append("，").append(user.getStatus())
				.append("，"+user.getRiseLevel()).append("，").append(user.getTransmotype()).append("，").append(user.getSpbindurl())
				.append("，").append(user.getRptUrl()).append("，").append(user.getMoUrl()).append("，")
				.append(user.getSendtmspan()).append("，").append(user.getLoginIp());
				opContent.append("新建").append("代理账户[信息类型，代理账号，账户名称，账户密码，应用类型，账户状态，发送级别，上行、状态报告获取方式，" )
				.append("上行、状态报告URL，上行URL，状态报告URL，发送起止时间，绑定IP]（").append(newStr).append("）");
				
			}
			else if(hidOpType.equals("edit")){
				opType=StaticValue.UPDATE;
				user.setModitime(new Timestamp(System.currentTimeMillis()));
				user.setUid(new Long(request.getParameter("uid")));
				int status=Integer.parseInt(request.getParameter("status"));
				user.setStatus(status);
				//获取修改前代理账户信息
				Userdata olduser=new BaseBiz().getById(Userdata.class, user.getUid());
				if(olduser!=null){
					oldStr.append(olduser.getAccouttype()).append("，").append(olduser.getUserId()).append("，")
					.append(olduser.getStaffName()).append("，").append(olduser.getUserPassword()).append("，")
					.append(olduser.getLoginId()).append("，").append(olduser.getStatus()).append("，")
					.append(olduser.getRiseLevel()).append("，").append(olduser.getTransmotype()).append("，")
					.append(olduser.getSpbindurl()).append("，").append(olduser.getRptUrl()).append("，")
					.append(olduser.getMoUrl()).append("，").append(olduser.getSendtmspan()).append("，")
					.append(olduser.getLoginIp());
				}
				result = proxybiz.updateUserdata(user);
				request.setAttribute("w_userdataResult", "2");
				newStr.append(user.getAccouttype()).append("，").append(user.getUserId()).append("，")
				.append(user.getStaffName()).append("，").append(user.getUserPassword()).append("，")
				.append(user.getLoginId()).append("，").append(user.getStatus()).append("，").append(user.getRiseLevel())
				.append("，").append(user.getTransmotype()).append("，").append(user.getSpbindurl()).append("，")
				.append(user.getRptUrl()).append("，").append(user.getMoUrl()).append("，").append(user.getSendtmspan())
				.append("，").append(user.getLoginIp());
				opContent.append("修改").append("代理账户[信息类型，SP账号，账户名称，账户密码，应用类型，账户状态，发送级别，上行、状态报告获取方式，" )
				.append("上行、状态报告URL，上行URL，状态报告URL，发送起止时间，绑定IP]（").append(oldStr).append("-->").append(newStr).append("）");
			}
			if(result)
			{
				//添加成功操作日志
				spLog.logSuccessString(opUser, opModule, opType, opContent.toString(),corpCode);
				//增加操作日志
				setLog(request, "代理账号", opContent.toString()+"成功", opType);
			}else
			{
				//添加失败操作日志
				spLog.logFailureString(opUser, opModule, opType, opContent+opSper, null,corpCode);
				//增加操作日志
				setLog(request, "代理账号", opContent.toString()+"失败", opType);
				//EmpExecutionContext.error("企业：" +corpCode+",操作员：" +opUser+opContent+"失败");
			}
			if(hidOpType.equals("add"))
			{
				request.setAttribute("lgcorpcode", corpCode);
				this.toAdd(request, response);
			}else if(hidOpType.equals("edit"))
			{
				request.setAttribute("lgcorpcode", corpCode);
				this.toEdit(request, response);
			}
		}catch(Exception ex){
			//添加失败操作日志
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, ex,corpCode);
			request.setAttribute("w_userdataResult", "0");
			EmpExecutionContext.error(errorLoger.getErrorLog(ex,"企业：" +corpCode+",操作员：" +opUser+"修改代理账户异常！"));
		}
	}
	
	
	/**
	 * 跳转到修改代理账户页面
	 * @param request
	 * @param response
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//String id = request.getParameter("userid");
			String id;
			String keyId = request.getParameter("keyId");
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				id = encryptOrDecrypt.decrypt(keyId);
				if(id == null)
				{
					EmpExecutionContext.error("跳转到修改短彩代理账户，参数解密码失败，keyId:"+keyId);
					throw new Exception("跳转到修改代理账户，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("跳转到修改代理账户，从session中获取加密对象为空！");
				throw new Exception("跳转到修改代理账户，获取加密对象失败。");
			}
//			List<Userdata> userList = proxybiz.getAgentUserdata();
//			request.setAttribute("userList", userList);
			//加密后的集合(SP账号)
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", id);
			conditionMap.put("userprivilege", "11");
			Userdata userdata=proxybiz.getUserdataByMap(conditionMap);
			request.setAttribute("userdata", userdata);
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String corpCode = lfSysuser.getCorpCode();
			request.setAttribute("keyId", keyId);
			request.getRequestDispatcher(empRoot  + basePath +"/pag_editProxyMage.jsp?lgcorpcode="+corpCode)
				.forward(request, response);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"跳转到修改代理账户页面异常！"));
		}
	}
	/**
	 * 修改sp账户状态
	 * @param request
	 * @param response
	 */
	public void ChangeSate(HttpServletRequest request, HttpServletResponse response)
	{
		//企业编码
		String corpcode = request.getParameter("lgcorpcode");
		String opUser = request.getParameter("lgusername");
		opUser = opUser==null?"":opUser;
		SuperOpLog spLog = new SuperOpLog();
		BaseBiz baseBiz = new BaseBiz();
		PrintWriter writer = null;
		String opContent = null;
		try {
			writer = response.getWriter();
			//获取SP账户ID
			String keyId = request.getParameter("keyId");
			String uidStr;
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				uidStr = encryptOrDecrypt.decrypt(keyId);
				if(uidStr == null)
				{
					EmpExecutionContext.error("修改代理账户状态，参数解密码失败，keyId:"+keyId);
					throw new Exception("修改代理账户状态，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("修改代理账户状态，从session中获取加密对象为空！");
				throw new Exception("修改代理账户状态，获取加密对象失败。");
			}
			//SP账户状态
			String status = request.getParameter("status");			
			
			Userdata userdata = baseBiz.getById(Userdata.class, Long.parseLong(uidStr));
			 
			userdata.setStatus(Integer.valueOf(status));
			String type=userdata.getAccouttype()==1?"短信":"彩信";
			if(status.equals("0"))
			{
				
				opContent="修改"+type+"代理账户：("+userdata.getUserId()+")的状态为激活";
			}
			else
			{
				opContent="修改"+type+"代理账户：("+userdata.getUserId()+")的状态为失效";
			}			
           
			//调用修关键字对象的方法，并返回结果
			boolean userresult = baseBiz.updateObj(userdata);
			if(userresult)
			{
				/*writer.print("true");*/
				response.getWriter().print("true");
				//保存日志
				spLog.logSuccessString(opUser, opModule, StaticValue.UPDATE, opContent,corpcode);
				//增加操作日志
				setLog(request, "代理账号", opContent+"成功", StaticValue.UPDATE);
			}else{
				//增加操作日志
				setLog(request, "代理账号", opContent+"失败", StaticValue.UPDATE);
			}
		} catch (Exception e) {
			//异常错误
			if(writer!=null){
				writer.print("error");
			}
			spLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent+opSper, e,corpcode);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"企业：" +corpcode+",操作员：" +opUser+"修改代理账户状态异常！"));
		}
		return;
	}
}
