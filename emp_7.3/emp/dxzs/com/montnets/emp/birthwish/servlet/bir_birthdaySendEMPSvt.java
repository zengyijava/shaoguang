package com.montnets.emp.birthwish.servlet;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.birthwish.biz.BirthWishBiz;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.common.vo.LfBirthdaySetupVo;
import com.montnets.emp.entity.birthwish.LfBirthdayMember;
import com.montnets.emp.entity.birthwish.LfBirthdaySetup;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.dxzs.LfDfadvanced;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author guodawei
 *生日祝福
 */
@SuppressWarnings("serial")
public class bir_birthdaySendEMPSvt extends BaseServlet{
	final String empRoot = "dxzs";
	final SmsBiz smsBiz = new SmsBiz();
	final IEmpTransactionDAO empTransactionDAO = new DataAccessDriver().getEmpTransDAO();
	final SendBirthDateBiz sendBirthDateBiz = new SendBirthDateBiz();
	final SpecialDAO smsSpecialDAO = new SpecialDAO();
	final IEmpDAO smsDao=new DataAccessDriver().getEmpDAO();
	final BaseBiz baseBiz = new BaseBiz();
	final BirthWishBiz birthWishBiz=new BirthWishBiz();
	//格式化时间
	private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		long startTime = System.currentTimeMillis();
		try {
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, null);
			//员工生日祝福
			//查询条件对象，执行完查询要返回到页面上
			LfBirthdaySetupVo vo=new LfBirthdaySetupVo();
			List<LfBirthdaySetupVo> setupVoList = null;
			
			
			LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//当前登录操作员id
			String lguserid=String.valueOf(loginSysuser.getUserId());
			//企业编码
			String lgcorpcode=loginSysuser.getCorpCode();
			
			
//			//当前登录操作员id
//			String lguserid = request.getParameter("lguserid");
//			//当前登录操作员id
//			String lgcorpcode = request.getParameter("lgcorpcode");
//			if(lgcorpcode==null||"".equals(lgcorpcode.trim())||"undefined".equals(lgcorpcode.trim())||lguserid==null||"".equals(lguserid.trim())||"undefined".equals(lguserid.trim())){
//				EmpExecutionContext.error("跳转到员工生日祝福查询页面获取登录操作员ID和登录操作员企业编码异常！lguserid="+lguserid+",lgcorpcode="+lgcorpcode+"。改成从Session获取。");
//				LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
//				lguserid=String.valueOf(loginSysuser.getUserId());
//				lgcorpcode=loginSysuser.getCorpCode();
//			}
			//当前登录操作员对象
			LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			
			vo.setCorpCode(lgcorpcode);
			PageInfo pageInfo = new PageInfo();
			boolean isBack = request.getParameter("isback")==null?false:true;//是否返回操作
			String username = null;
			String status = null;
			String spUser = null;
			String title = null;
			HttpSession session = request.getSession(false);
			if(isBack){
				pageInfo = (PageInfo) session.getAttribute("bir_pageinfo");
				username = session.getAttribute("bir_username")==null?"":String.valueOf(session.getAttribute("bir_username"));
				status = session.getAttribute("bir_status")==null?"":String.valueOf(session.getAttribute("bir_status"));
				spUser = session.getAttribute("bir_spUser")==null?"":String.valueOf(session.getAttribute("bir_spUser"));
				title = session.getAttribute("bir_title")==null?"":String.valueOf(session.getAttribute("bir_title"));
			}else{
				boolean isFirstEnter = pageSet(pageInfo,request);
				if(!isFirstEnter)
				{
					//查询条件
					username = request.getParameter("username");
					status = request.getParameter("status");
					spUser = request.getParameter("spUser");
					title = request.getParameter("title");
				}
			}
			session.setAttribute("bir_pageinfo", pageInfo);
			session.setAttribute("bir_username", username);
			session.setAttribute("bir_status", status);
			session.setAttribute("bir_spUser", spUser);
			session.setAttribute("bir_title", title);
			//操作员姓名
			if(username!=null && username.length()>0)
			{
				vo.setUsername(username);
			}
			//是否启用
			if(status!=null && status.length()>0)
			{
				vo.setIsUse(Integer.parseInt(status));
			}
			//发送账号
			if(spUser!=null && spUser.length()>0)
			{
				vo.setSpUser(spUser);
			}
			//发送主题
			if(title!=null && title.length()>0)
			{
				vo.setTitle(title);
			}
			vo.setType(1);
			request.setAttribute("reTitle", "bir_birthdaySendEMP");
			request.setAttribute("titlePath", "birthdaySendEMP");
			
		
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			conditionMap.put("platFormType", "1");
			conditionMap.put("corpCode", lgcorpcode);
			conditionMap.put("isValidate", "1");
			orderbyMap.put("spUser", StaticValue.ASC);
			List<LfSpDepBind> smsBindSp = birthWishBiz.getSpDepBindWhichUserdataIsOk(conditionMap, orderbyMap, null);
			//List<Userdata> spUserList = smsBiz.getSpUserList(curSysuser);
			request.setAttribute("smsBindSp", smsBindSp);			
			//查询生日祝福设置列表
			setupVoList = sendBirthDateBiz.getBirthdaySetupVoList(curSysuser.getUserId(),vo, pageInfo);
			
			//生日祝福加密
			//获取加密类
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//加密mtId
			encryptOrDecrypt.batchEncrypt(setupVoList, "Id", "IdCipher");
			
			//查询发送账号列表
			//List<Userdata> spUserList = smsBiz.getSpUserList(curSysuser);
			//request.setAttribute("sendUserList", spUserList);
			String updateResult = request.getParameter("updateResult");
			if(updateResult!=null && !"".equals(updateResult))
			{
				request.setAttribute("updateResult", updateResult);
			}
			request.setAttribute("curSysuser", curSysuser);
			request.setAttribute("setupVoList", setupVoList);
			request.setAttribute("vo", vo);
			request.setAttribute("pageInfo", pageInfo);
			if(!isBack)
			{
				//操作日志信息
				String opContent = "查询："+sdf.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + pageInfo.getTotalRec();
				EmpExecutionContext.info("员工生日祝福管理", lgcorpcode, lguserid, curSysuser.getUserName(), opContent, "GET");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"跳转到员工生日祝福查询页面失败！");
		}finally{
			try
			{
				request.getRequestDispatcher(this.empRoot+"/birthwish/bir_birthdaySendManageEMP.jsp").forward(request,response);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"跳转到员工生日祝福查询页面失败！");
			}
		}
	}
	//请求新增或修改页面时调用
	public void toUpdate(HttpServletRequest request, HttpServletResponse response)
	{
		try {
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, null);
			//获取生日祝福任务id
			String setupid = request.getParameter("setupid");
			
			//生日祝福解密
		    //获取加密的ID字符串
			String setupidChpher=request.getParameter("setupidChpher");
			 //获取加密类
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//解密setupidChpher
			setupidChpher=encryptOrDecrypt.decrypt(setupidChpher);
			
			
			//是员工还是客户
			LfBirthdaySetup setup = new LfBirthdaySetup();
			
			//当前登录操作员id
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			if(lguserid==null||"".equals(lguserid.trim())||"undefined".equals(lguserid.trim())){
				EmpExecutionContext.error("跳转到员工生日祝福新增或者修改页面获取登录操作员ID异常！lguserid="+lguserid+"，改成从Session获取。");
				LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				lguserid=String.valueOf(loginSysuser.getUserId());
			}
			//当前登录操作员对象
			//LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			//任务的所有者
			String userid = request.getParameter("userid");
			LfSysuser taskOwner=null;
			//根据userid找到操作员对象，这个操作员是任务的所有者不一定是当前登录操作员
			if(userid!=null && userid.length()>0)
			{
				taskOwner = baseBiz.getById(LfSysuser.class, Long.parseLong(userid));
			}else {
				//如果页面传递的userid为空则取当前登录操作员
				taskOwner = baseBiz.getById(LfSysuser.class, Long.parseLong(lguserid));
			}
			//页面设置签名时，设置任务创建人的姓名
			request.setAttribute("taskOwner", taskOwner);
			
			request.setAttribute("reTitle", "bir_birthdaySendEMP");
			request.setAttribute("titlePath", "birthdaySendEMP");
			
			
			//发送账号
			List<Userdata> spUserList = smsBiz.getSpUserList(taskOwner);
			//我在页面上做了一个处理，如果从管理页面跳过来的时候是新增那么任务id为-1，否则为修改
			List<Userdata> spUserListTemp=null;
			if(setupidChpher!=null && !setupidChpher.equals("-1"))
			{
				//如果是修改则找出任务对象传递给页面，填充各个字段
				setup = baseBiz.getById(LfBirthdaySetup.class, Long.parseLong(setupidChpher));
//				//处理生日祝福语，方便textarea显示
//				String msg=handleText(setup.getMsg());
//				setup.setMsg(msg);
//				//处理标题，方便textarea显示
//				String title=handleText(setup.getTitle());
//				setup.setTitle(title);
				request.setAttribute("birthdaySetup", setup);
				LinkedHashMap<String, String> conditionMap= new LinkedHashMap<String, String>();
				conditionMap.put("userId", setup.getSpUser());
				spUserListTemp = baseBiz.getByCondition(Userdata.class, conditionMap, null);
				//任务不是当前操作员的，是别人的
				if(!lguserid.equals(setup.getUserId().toString()))
				{
					spUserList = spUserListTemp;
				}
				
			}

			request.setAttribute("spUserList", spUserList);
			request.setAttribute("lguserid", lguserid);
			//获取高级设置默认信息
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			conditionMap.put("userid", lguserid);
			//5:员工生日祝福
			conditionMap.put("flag", "5");
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			orderMap.put("id", StaticValue.DESC);
			List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, conditionMap, orderMap);
			LfDfadvanced lfDfadvanced = null;
			if(lfDfadvancedList != null && lfDfadvancedList.size() > 0)
			{
				lfDfadvanced = lfDfadvancedList.get(0);
			}
			request.setAttribute("lfDfadvanced", lfDfadvanced);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"跳转到员工生日祝福新增或者修改页面失败！");
		}finally{
			try
			{
				request.getRequestDispatcher(this.empRoot+"/birthwish/bir_birthdaySendEMP.jsp").forward(request,response);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"跳转到员工生日祝福新增或者修改页面失败！");
			}
			
		}
	}

	/**
	 * 处理文本信息，方便textarea显示
	 * @param text
	 * @return
	 */
	private String handleText(String text){
		String returnMsg=text.replace("&amp","&amp;amp")
				.replace("&nbsp","&amp;nbsp")
				.replace("&lt","&amp;lt")
				.replace("&gt","&amp;gt")
				.replace("&apos","&amp;apos")
				.replace("&quot","&amp;quot");
		return returnMsg;
	}
	
	/**
	 * 改变任务状态启用或禁用（暂不用，因为管理页面上取消了这个功能）
	 * 2012-11-10
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 */
	public void changeState(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		TaskManagerBiz tm = new TaskManagerBiz();
		boolean result =false;
		try {
			String setupid = request.getParameter("setupid");
			LfBirthdaySetup setup =baseBiz.getById(LfBirthdaySetup.class, Long.parseLong(setupid));
			String isUse = request.getParameter("isUse");
			String corpCode = request.getParameter("lgcorpcode");
			if(isUse.equals("1"))
			{
				//将状态改为启用
				objectMap.put("isUse", "1");
				SendBirthDateTimerTask sendSmsTimerTask = new SendBirthDateTimerTask(setup.getTitle(), 1, setup.getSendTime(), 1, "BD|"+setup.getId());
				//设置定时任务
				tm.setJobAndDelRepeat(sendSmsTimerTask);
			}else {
				//将状态改为禁用
				objectMap.put("isUse", "2");
				//停止定时器
				tm.stopTask("BD|"+setupid);
			}
			conditionMap.put("id", setupid);
			conditionMap.put("corpCode", corpCode);
			result = baseBiz.update(LfBirthdaySetup.class, objectMap, conditionMap);
			
			String changeStr="";
			if("1".equals(isUse)){
				changeStr="状态由停用改为启用";
			}else{
				changeStr="状态由启用改为停用";
			}
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String contnet="修改员工生日祝福状态成功(生日祝福ID为："+setupid+"，"+changeStr+")。";
				EmpExecutionContext.info("员工生日祝福管理", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "UPDATE");
			}
			
			response.getWriter().print(result);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"员工生日祝福改变任务状态启用或禁用失败！");
			response.getWriter().print("false");
		}
	}
	/**
	 * 新增或修改保存入库，此方法兼容员工和客户，修改和新建，并且成功后跳转到管理页面就是调用find方法
	 * 2012-11-10
	 * @param request
	 * @param response
	 * @throws Exception
	 * void
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Connection conn =null;
		String lgcorpcode="";
		Long lguserid=0L;
		String updateResult = "true";
		try {
			//当前登录操作员id
			//lguserid = Long.valueOf(request.getParameter("taskOwnerId"));
			//漏洞修复 session里获取操作员信息
			lguserid = SysuserUtil.longLguserid(request);

			//当前登录企业
			lgcorpcode = request.getParameter("lgcorpcode");
			//任务id，不为-1就表示是修改
			String setupid = request.getParameter("setupid");
			//生日祝福修改前信息
			String oldSetUpMessage="";
			LfBirthdaySetup setup = null;
			//判断是新增记录还是更新记录
			if("-1".equals(setupid)) {
				//-1表示新增任务
				setup = new LfBirthdaySetup();
				setup.setUserId(lguserid);
			}else {
				setup = baseBiz.getById(LfBirthdaySetup.class, setupid);
				oldSetUpMessage = setup.getId()+"，"+setup.getTitle()+"，"+setup.getSendTime()+"，"
				+setup.getIsAddName()+"，"+setup.getAddName()+"，"+setup.getIsSignName()+"，"+setup.getSignName()+")"+"，"
				+setup.getMsg()+"，"+setup.getSpUser()+"，"+setup.getIsUse();
			}
			String temp = "zh_HK".equals(MessageUtils.extractMessage("common","common_empLangName",request))?" ":"";
			//类型 员工
			setup.setType(1);
			//是否加尊称
			String isAddName = request.getParameter("isAddName");
			//尊称
			String addName = MessageUtils.extractMessage("dxzs","dxzs_ssend_alert_141",request)+temp+"#P_1#";
			//祝福语
			String msg = request.getParameter("msg");
			//sp账号
			String spUser = request.getParameter("spUser");
			//发送时间
			String sendTime = request.getParameter("sendTime");
			//是否启用
			String isUse = request.getParameter("isUse");
			//是否启用
			String title = request.getParameter("title");
			//是否要签名
			String isSignName = StringUtils.defaultIfEmpty(request.getParameter("isSignName"),"2");

			//签名内容
			String signName = request.getParameter("signName");
			Long bsId;
			
			if("1".equals(isAddName)){
				setup.setIsAddName(1);
				setup.setAddName(addName);
			}else {
				setup.setIsAddName(2);
				setup.setAddName("");
			}
			//替换短信内的特殊字符
			msg = smsBiz.smsContentFilter(msg);
			//祝福语内容
			setup.setMsg(msg);
			//是否需要签名
			setup.setIsSignName(Integer.parseInt(isSignName));
			if(Integer.parseInt(isSignName)==1) {
				setup.setSignName(signName);
			}
			setup.setTitle(StringUtils.defaultIfEmpty(title,""));
			setup.setBuscode("M00000");
			setup.setSpUser(spUser);
			//页面传过来的时间是不带秒的，所以要把ss拼上去
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			sendTime=sendTime+":00";
			java.util.Date date1 = sdf.parse(sendTime);
			//用这个对象设置年月日
			java.util.Date nowDate = new java.util.Date(System.currentTimeMillis());
			//将时间定在明天
			Timestamp sendTime1 = new Timestamp(nowDate.getYear(),nowDate.getMonth(),nowDate.getDate()+1,date1.getHours(),date1.getMinutes(),0,0);
			setup.setSendTime(sendTime1);
			
			setup.setCorpCode(lgcorpcode);
			setup.setIsUse(Integer.valueOf(isUse));
			
			//事务开始
			conn = empTransactionDAO.getConnection();
			empTransactionDAO.beginTransaction(conn);
			if(!"-1".equals(setupid)) {
				//Long型任务id
				bsId = setup.getId();
				empTransactionDAO.update(conn, setup);
			}else {
				bsId = empTransactionDAO.saveObjectReturnID(conn, setup);
				setup.setId(bsId);
			}

			//选择的机构Id集合
			String[] empDepIds = StringUtils.defaultIfEmpty(request.getParameter("empDepIds"),"").split(",");
			//选择的员工Id集合
			String[] empIds = StringUtils.defaultIfEmpty(request.getParameter("empIds"),"").split(",");
			String[] empDepName = StringUtils.defaultIfEmpty(request.getParameter("empDepName"),"").split(",");
			String[] empName = StringUtils.defaultIfEmpty(request.getParameter("empName"),"").split(",");

			//采取先删后增，事务控制
			//1.删除
			LinkedHashMap<String,String> map = new LinkedHashMap<String, String>(16);
			map.put("bsId",setup.getId().toString());
			empTransactionDAO.delete(conn, LfBirthdayMember.class, map);
			//2.新增绑定关系
			//根据Id组装LfBirthdayMember对象
			List<LfBirthdayMember> birMembers = ids2LfBirthdayMember(empDepIds, empIds, lgcorpcode, lguserid, setup.getId(), empDepName, empName);
			empTransactionDAO.save(conn, birMembers, LfBirthdayMember.class);
			empTransactionDAO.commitTransaction(conn);
			//操作定时器
			TaskManagerBiz tm = new TaskManagerBiz();
			if(setup.getIsUse()==1) {
				SendBirthDateTimerTask sendSmsTimerTask = new SendBirthDateTimerTask(setup.getTitle(), 1, setup.getSendTime(), 1, "BD|"+setup.getId());
				boolean flagTemp = tm.setJobAndDelRepeat(sendSmsTimerTask);
				if(flagTemp) {
					updateResult = "true";
				}else {
					updateResult = "false";
				}
			}else {
				//如果停用
				tm.stopTask("BD|"+setup.getId());
			}
			if(!"-1".equals(setupid)) {
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String contnet="修改员工生日祝福"+"成功。[任务ID，任务主题，发送时间，是否带尊称，尊称，是否带签名，签名，祝福内容，SP账号，是否启用]" +
							"("+oldSetUpMessage+")"
					    +"-->("+setup.getId()+"，"+setup.getTitle()+"，"+setup.getSendTime()+"，"
						+setup.getIsAddName()+"，"+setup.getAddName()+"，"+setup.getIsSignName()+"，"+setup.getSignName()+")"+"，"
						+setup.getMsg()+"，"+setup.getSpUser()+"，"+setup.getIsUse()+")";
					EmpExecutionContext.info("员工生日祝福管理", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "UPDATE");
				}
			}else{
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String contnet="新建员工生日祝福"+"成功。[任务ID，任务主题，发送时间，是否带尊称，尊称，是否带签名，签名，祝福内容，SP账号，是否启用]("
					+setup.getId()+"，"+setup.getTitle()+"，"+setup.getSendTime()+"，"
					+setup.getIsAddName()+"，"+setup.getAddName()+"，"+setup.getIsSignName()+"，"+setup.getSignName()+")"+"，"
					+setup.getMsg()+"，"+setup.getSpUser()+"，"+setup.getIsUse()+")";
					EmpExecutionContext.info("员工生日祝福管理", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "ADD");
				}
			}
		} catch (Exception e) {
			empTransactionDAO.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"新增或者修改员工生日祝福失败！");
		} finally{
			empTransactionDAO.closeConnection(conn);
			
			String url = "bir_birthdaySendEMP.htm?method=find&isback=1&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&updateResult="+updateResult;
			try{
				response.sendRedirect(url);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"新增或者修改员工生日祝福跳转页面失败！");
			}
		}
	}

	/**
	 * 根据Id转化为LfBirthdayMember集合
	 * @param empDepIds 机构id
	 * @param empIds 员工id
	 * @param corpCode 企业Id
	 * @param userId 操作员Id
	 * @param bsId 对应的lf_birthdaysetup Id
	 * @param empDepName 对应的lf_birthdaysetup Id
	 * @param empName 对应的lf_birthdaysetup Id
	 * @return LfBirthdayMember集合
	 */
	private List<LfBirthdayMember> ids2LfBirthdayMember(String[] empDepIds, String[] empIds, String corpCode, Long userId, Long bsId, String[] empDepName, String[] empName) {

		List<LfBirthdayMember> birMembers = new ArrayList<LfBirthdayMember>();

		for(int i = 0;i < empDepIds.length;i++){
			String str = empDepIds[i];
			if(StringUtils.isBlank(str)) continue;
			LfBirthdayMember member = new LfBirthdayMember();
			member.setCorpCode(corpCode);
			member.setType(1);
			member.setBsId(bsId);
			//如果包含子机构则为e开头
			int type = str.startsWith("e") ? 3 : 2;
			member.setMembertype(type);
			member.setMemberId(Long.parseLong(str.startsWith("e") ? str.substring(1):str));
			member.setUserId(userId);
			member.setAddName(empDepName[i]);
			birMembers.add(member);
		}
		for(int i = 0;i < empIds.length;i++){
			String str = empIds[i];
			if(StringUtils.isBlank(str)) continue;
			LfBirthdayMember member = new LfBirthdayMember();
			member.setCorpCode(corpCode);
			member.setType(1);
			member.setBsId(bsId);
			member.setMembertype(1);
			member.setMemberId(Long.parseLong(str));
			member.setUserId(userId);
			member.setAddName(empName[i]);
			birMembers.add(member);
		}
		return birMembers;
	}

	public void getDepAndEmpTree1(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		//获取每个机构下的员工列表，拼成option对象打印到页面
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//获取机构id 异步查询用
			String depId = request.getParameter("depId");
			//保存生成的html代码
			StringBuffer sb = new StringBuffer();
			//页码
			String pageIndex1 = request.getParameter("pageIndex1");
			//操作是上一页还是下一页
			String opType = request.getParameter("opType");
			//类型是员工
			//当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");

			PageInfo pageInfo = new PageInfo();

			if(opType!=null && opType.equals("goNext"))
			{
				pageInfo.setPageIndex(Integer.parseInt(pageIndex1)+1);
			}else if( opType!=null && opType.equals("goLast")){
				pageInfo.setPageIndex(Integer.parseInt(pageIndex1)-1);
			}else {
				pageInfo.setPageIndex(1);
			}
			//每页显示50条记录
			pageInfo.setPageSize(50);

			List<LfEmployee> lfEmployeeList=null;
			List<LfClient> lfClientList = null;
			//查询机构下员工列表
			conditionMap.put("depId", depId);
			conditionMap.put("corpCode", lgcorpcode);
			//判断如果已经被别人绑定了成员显示灰色
			StringBuffer guids = new StringBuffer();

			//查询出点击机构下的50条员工记录
			lfEmployeeList = baseBiz.getByCondition(LfEmployee.class, null, conditionMap, null, pageInfo);
			for (int i = 0; i < lfEmployeeList.size(); i++)
			{
				guids.append(lfEmployeeList.get(i).getGuId()).append(",");
			}


			//查询所有已经被绑定的操作员对象
			List<LfBirthdayMember> memberList =null ;
			conditionMap.clear();
			if(guids.length()>0)
			{
				conditionMap.put("corpCode", lgcorpcode);
				conditionMap.put("memberId&in", guids.deleteCharAt(guids.lastIndexOf(",")).toString());
				memberList = baseBiz.getByCondition(LfBirthdayMember.class, conditionMap, null);
			}
			//memberGuidList用以验证一个机构下面的员工客户是否已经被其它操作员所绑定
			List<Long> memberGuidList = new ArrayList<Long>();
			if(memberList!=null && memberList.size()>0)
			{
				for(LfBirthdayMember member : memberList)
				{
					if(member.getMembertype()==1)
					    memberGuidList.add(member.getMemberId());
				}
			}
			//遍历员工集合
			if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
				for (LfEmployee user : lfEmployeeList) {
					//判断如果已经被别人绑定了成员显示灰色
					if(memberGuidList.contains(user.getGuId()))
					{
						sb.append("<option  style='color:#808080' value='").append(user.getGuId()).append("'>");
						sb.append(user.getName().trim()).append("</option>");
					}
					else
					{
						sb.append("<option value='").append(user.getGuId()).append("'>");
						sb.append(user.getName().trim()).append("</option>");
					}
				}
			}
			//遍历客户集合
			if (lfClientList != null && lfClientList.size() > 0) {
				for (LfClient user : lfClientList) {
					//判断如果已经被别人绑定了成员显示灰色
					if(memberGuidList.contains(user.getGuId()))
					{
						sb.append("<option  style='color:#808080' value='").append(user.getGuId()).append("'>");
						sb.append(user.getName().trim()).append("</option>");
					}
					else
					{
						sb.append("<option value='").append(user.getGuId()).append("'>");
						sb.append(user.getName().trim()).append("</option>");
					}
				}
			}
			//成功异步打印总记录数@总页数@select控件的html代码
			response.getWriter().print(pageInfo.getTotalRec()+"@"+pageInfo.getTotalPage()+"@"+sb.toString());

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"生日祝福获取员工机构下的员工失败！");
		}
	}

	public void getBirthMembers(HttpServletRequest request,HttpServletResponse response){
		try {
			//当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			//当前登录企业
			String lguserid = request.getParameter("ownerId");
			//漏洞修复 session里获取操作员信息
			//String lguserid = SysuserUtil.strLguserid(request);

			//当前设置id
			String setupid = request.getParameter("setupid");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("type", "1");
			conditionMap.put("bsId", setupid);
			conditionMap.put("userId", lguserid);
			conditionMap.put("corpCode", lgcorpcode);
			List<LfBirthdayMember> existMembersEMP = baseBiz.getByCondition(LfBirthdayMember.class, conditionMap, null);
			StringBuilder ids = new StringBuilder();
			//获取手机号
			for(LfBirthdayMember vo: existMembersEMP){
				if(vo.getMembertype() == 1 && vo.getType() == 1){
					ids.append(vo.getMemberId()).append(",");
				}
			}
			if(ids.length() > 0){
				ids = ids.deleteCharAt(ids.length() - 1);
			}
			StringBuilder finalJson = new StringBuilder("[");
			HashMap<Long,String> map =  birthWishBiz.getBirthMembersPhone(ids.toString());
			for(LfBirthdayMember vo: existMembersEMP){
				String jsonStr = JSONObject.toJSONString(vo);
				if(map.containsKey(vo.getMemberId())){
					jsonStr = jsonStr.replaceAll("}$",",\"phone\":\""+ map.get(vo.getMemberId()) +"\"}");
					finalJson.append(jsonStr).append(",");
                }else {
					finalJson.append(jsonStr).append(",");
				}
			}
			if(finalJson.toString().endsWith(",")){
                finalJson = finalJson.deleteCharAt(finalJson.length() - 1);
            }
			response.getWriter().print(finalJson.append("]"));
		}catch (Exception e){
			EmpExecutionContext.error(e,"员工生日祝福获取选择的成员失败！");
		}
	}

	/**判断一个机构是否被包含在其它机构
	 * type=1表示员工机构 type=2表示客户机构
	 */
	private boolean isDepAcontainsDepB(String depIdAsTemp,String depIdB,String corpCode)
	{
		boolean result=false;
		LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			List<LfEmployeeDep> lfEmployeeDepList=new ArrayList<LfEmployeeDep>();
			if(depIdAsTemp!=null && !"".equals(depIdAsTemp))
			{
				LfEmployeeDep dep = baseBiz.getById(LfEmployeeDep.class, depIdAsTemp);
				conditionMap.put("deppath&like2", dep.getDeppath());
				conditionMap.put("corpCode", corpCode);
				lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
				for(int i=0;i<lfEmployeeDepList.size();i++)
				{
					depIdSet.add(lfEmployeeDepList.get(i).getDepId());
				}
			}
			result= depIdSet.contains(Long.valueOf(depIdB));
		}catch (Exception e) {
			EmpExecutionContext.error(e,"员工生日祝福判断一个机构是否包含在其他机构内失败！");
		}
		return result;
	}
	
	//判断选择的机构是否把其它已经选择的机构包含了
	public void isDepsContainedByDepB(String depId,String corpCode,List<LfBirthdayMember> existMembersEMP) throws Exception
	{
		LfEmployeeDep depTemp = baseBiz.getById(LfEmployeeDep.class, depId);
		List<LfEmployeeDep> lfEmployeeDepList=null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		if(depTemp!=null)
		{
			conditionMap.put("deppath&like2", depTemp.getDeppath());
		}
		conditionMap.put("corpCode", corpCode);
		lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
		HashSet<Long> depIdSet = new HashSet<Long>();
		if(lfEmployeeDepList!=null && lfEmployeeDepList.size()>0)
		{
			for (int i = 0; i < lfEmployeeDepList.size(); i++) 
			{
				depIdSet.add(lfEmployeeDepList.get(i).getDepId());
			}
		}
		List<LfBirthdayMember> tempList = new  ArrayList<LfBirthdayMember>();
		if(existMembersEMP!=null && existMembersEMP.size()>0)
		{
			for(LfBirthdayMember member : existMembersEMP) 
			{
				if(depIdSet.contains(member.getMemberId()))
				{
					//existMembersEMP.remove(member);对当前操作的list进行删除操作会抛出java.util.ConcurrentModificationException异常
					tempList.add(member);
				}
			}
		}
		if(tempList!=null && tempList.size()>0)
		{
			if(existMembersEMP != null){
				existMembersEMP.removeAll(tempList);
			}
		}
	}
	//选择机构
	public void selectDepMemeber(HttpServletRequest request,HttpServletResponse response)
	{
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			//0表示不包含子机构 1表示包含子机构
			int ismut = Integer.parseInt(request.getParameter("ismut"));
			//当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			//漏洞修复 session里获取操作员信息
			Long lguserid = SysuserUtil.longLguserid(request);

			//当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			//选择机构名称
			String depname=request.getParameter("depname");
			@SuppressWarnings("unchecked")
			List<LfBirthdayMember> existMembersEMP=(List<LfBirthdayMember>)request.getSession(false).getAttribute("existMembersEMP");
			//选择机构的id
			String depId = request.getParameter("depId");
			//这个标识符判断是否重复了 ，如果重复就不往下执行了
			boolean existFlag= false;
			//做重复过滤
			if(existMembersEMP != null && existMembersEMP.size()>0) {
				for (LfBirthdayMember member:existMembersEMP) {
					if (member.getMembertype()==2 && member.getMemberId().toString().equals(depId)) {
						existFlag=true;
						break;
					}else if( member.getMembertype()==3 && isDepAcontainsDepB(member.getMemberId().toString(),depId,lgcorpcode)) {
						//用户选择包含子机构时判断该机构是否被其他机构包含
						existFlag=true;
						break;
					}
				}
			}
			
			if(existFlag) {
				response.getWriter().print("repeat");
				return;
			}
			//没有被包含，接着往下走
			if(ismut==1)
			{
				isDepsContainedByDepB(depId,lgcorpcode,existMembersEMP);
			}
			
			//下面要验证如果他选择的是包含子机构时，要删掉该机构包含的其他子机构
			
			
			LfBirthdayMember member = new LfBirthdayMember();
			//机构人数
			int count=0;
			int membertype=(ismut==1?3:2);
			
			member.setType(1);
			count = getCountByDepId(1,Long.parseLong(depId),lgcorpcode,membertype);
			
			//如果count为0则禁止添加
			if(count == 0) {
				response.getWriter().print("nomember");
				return;
			}
			//成功异步返回true
			response.getWriter().print("true");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"员工生日祝福将机构选择到后边的区域失败！");
			writer.print("false");
		}
	}

	public void deleteSetup(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		//这个方法的目的是删除一条设置时调用，并且会删除成员，采用了事务
		Connection conn =null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//lfSysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
		try {
			String setupId = request.getParameter("setupId");
			
			//生日祝福解密
			 //获取加密类
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//解密setupidChpher
			setupId=encryptOrDecrypt.decrypt(setupId);
			
			String type = request.getParameter("type");
			LfBirthdaySetup setup = baseBiz.getById(LfBirthdaySetup.class, setupId);
			//当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			
			conn = empTransactionDAO.getConnection();
			//开启事务
			empTransactionDAO.beginTransaction(conn);
			
			conditionMap.put("bsId", setupId);
			conditionMap.put("type", type);
			conditionMap.put("corpCode",lgcorpcode);
			//删除成员
			empTransactionDAO.delete(conn, LfBirthdayMember.class, conditionMap);
			//删除设置
			empTransactionDAO.delete(conn, LfBirthdaySetup.class, setupId);
			
			//删除员工生日祝福定时任务
			TaskManagerBiz tm = new TaskManagerBiz();
			tm.delTimerTask(conn, "BD|"+setupId);
			
			//提交事务
			empTransactionDAO.commitTransaction(conn);
			
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String deleteMessage=setup.getId()+"，"+setup.getTitle()+"，"+setup.getSendTime()+"，"
				+setup.getIsAddName()+"，"+setup.getAddName()+"，"+setup.getIsSignName()+"，"+setup.getSignName()+")"+"，"
				+setup.getMsg()+"，"+setup.getSpUser()+"，"+setup.getIsUse();
				String content="删除员工生日祝福成功。"+"[任务ID，任务主题，发送时间，是否带尊称，尊称，是否带签名，签名，祝福内容，SP账号，是否启用]"+"("+deleteMessage+")";
				EmpExecutionContext.info("员工生日祝福管理", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), content, "DELETE");
			}
			
			//成功异步返回true
			response.getWriter().print("true");
		} catch (Exception e) {
			//失败回滚事务
			empTransactionDAO.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"删除员工生日祝福失败！");
		}finally{
			conditionMap.clear();
			//关闭事务
			empTransactionDAO.closeConnection(conn);
		}
	}
	//根据机构id获取机构成员数量
	public int getCountByDepId(Integer type ,Long depId,String corpCode,int memberType) throws Exception
	{
		return smsSpecialDAO.getCountByDepId(type, depId, corpCode,memberType);
	}
	//获取总人数，此处解析是写死的，如果right的select格式要改，此处一定要相应修改
	public Long getSelectCount(List<LfBirthdayMember> existMembersEMP)
	{
		Long selectCount=0L;
		LfBirthdayMember member =null;
		String memberAddname="";
		for(int i=0;i<existMembersEMP.size();i++)
		{
			member = existMembersEMP.get(i);
			if(member.getMembertype()==1)
			{
				selectCount++;
			}else {
				memberAddname = member.getAddName();
				if(memberAddname.lastIndexOf("]")==memberAddname.length()-1)
				{
					selectCount=selectCount+Long.parseLong(memberAddname.substring(memberAddname.lastIndexOf("[")+1, memberAddname.lastIndexOf("人")));
				}else {
					selectCount++;
				}
			}	
		}
		return selectCount;
	}
	

	//带颜色的树生日祝福选择员工通讯录用
	public void getEmpSecondDepJson1(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		try
		{
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, null);
			writer = response.getWriter();
			String depId = request.getParameter("depId");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//企业编码不用页面传过来的值
			//String lgcorpcode = request.getParameter("lgcorpcode");
			//从Session中获取当前登录操作员
			LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//从Session中获取当前登录操作员企业编码
			String lgcorpcode=loginSysuser.getCorpCode();
			//如果操作员ID为空，则返回
			if(lguserid==null||"".equals(lguserid.trim())||"undefined".equals(lguserid.trim())){
				EmpExecutionContext.error("员工生日祝福获取机构树，传的操作员ID有异常,userid:"+lguserid);
				writer.print("");
				return;
			}
			//此方法只查询两级机构
			/*List<LfEmployeeDep> empDepList = enterBiz.getEmpSecondDepTreeByUserIdorDepId(lguserid,depId);*/
			List<LfEmployeeDep> empDepList = new DepBiz().getEmpSecondDepTreeByUserIdorDepId(lguserid,depId,lgcorpcode);
			LfEmployeeDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(empDepList != null && empDepList.size()>0){
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				StringBuffer guids = new StringBuffer();
				List<LfBirthdayMember> memberList=null;
				for(LfEmployeeDep empDep:empDepList)
				{
					guids.append(empDep.getDepId()).append(",");
				}
				if(guids.length()>0)
				{
					conditionMap.put("corpCode", lgcorpcode);
					conditionMap.put("memberId&in", guids.deleteCharAt(guids.lastIndexOf(",")).toString());
					conditionMap.put("type", "1");
					conditionMap.put("membertype", "2");
					memberList = new BaseBiz().getByCondition(LfBirthdayMember.class, conditionMap, null);
				}
				List<Long> memberGuidList = new ArrayList<Long>();
				if(memberList!=null && memberList.size()>0)
				{
					for(LfBirthdayMember member : memberList)
					{
						memberGuidList.add(member.getMemberId());
					}
				}
				tree.append("[");
				if (empDepList != null && empDepList.size() > 0) {
					for (int i = 0; i < empDepList.size(); i++) {
						dep = empDepList.get(i);
						tree.append("{");
						tree.append("id:").append(dep.getDepId() + "");
						tree.append(",name:'").append(dep.getDepName()).append(
								"'");
						tree.append(",depcodethird:'").append(
								dep.getDepcodethird()).append("'");
						// 树数据中加入父机构id
						if (dep.getParentId() - 0 == 0) {
							tree.append(",pId:").append(0);
						} else {
							tree.append(",pId:").append(dep.getParentId());
						}
						tree.append(",isParent:").append(true);
	
						if (memberGuidList.contains(dep.getDepId())) {
							tree.append(",isBind:").append(true);
						} else {
							tree.append(",isBind:").append(false);
						}
	
						tree.append("}");
						if (i != empDepList.size() - 1) {
							tree.append(",");
						}
	
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			writer.print("");
			EmpExecutionContext.error(e,"员工生日祝福获取员工机构树失败！");
		}
	}

	public void checkBadWord1(HttpServletRequest request, HttpServletResponse response)
	{
		String tmMsg=request.getParameter("tmMsg");
		String corpCode = request.getParameter("corpCode");
		String words=new String();
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			KeyWordAtom keyWordAtom=new KeyWordAtom();
			words=keyWordAtom.checkText(tmMsg,corpCode);
		} catch (Exception e)
		{
			words="error";
			EmpExecutionContext.error(e,"员工生日祝福检查关键字失败！");
		}finally
		{
			if(writer != null){
				writer.print("@"+words);
			}
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
			String spUser = request.getParameter("spUser");
			String flag = request.getParameter("flag");
			if(flag == null || "".equals(flag))
			{
				EmpExecutionContext.error("员工生日祝福高级设置存为默认参数异常！");
				response.getWriter().print(result);
				return;
			}
			if(lguserid == null || "".equals(lguserid))
			{
				EmpExecutionContext.error("员工生日祝福高级设置存为默认参数异常！lguserid："+lguserid);
				response.getWriter().print(result);
				return;
			}
			if(spUser == null || "".equals(spUser))
			{
				EmpExecutionContext.error("员工生日祝福高级设置存为默认参数异常！spUser："+spUser);
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
			lfDfadvanced.setSpuserid(spUser);
			lfDfadvanced.setFlag(Integer.parseInt(flag));
			lfDfadvanced.setCreatetime(new Timestamp(System.currentTimeMillis()));

			result = birthWishBiz.setDefault(conditionMap, lfDfadvanced);
			
			//操作结果
			String opResult ="员工生日祝福高级设置存为默认失败。";
			if(result != null && "seccuss".equals(result))
			{
				opResult = "员工生日祝福高级设置存为默认成功。";
			}
			//操作员信息
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			//操作员姓名
			String opUser = sysuser==null?"":sysuser.getUserName();
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[SP账号](").append(spUser).append(")");
			
			//操作日志
			EmpExecutionContext.info("员工生日祝福管理", lgcorpcode, lguserid, opUser, opResult + content.toString(), "OTHER");
			
			response.getWriter().print(result);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "员工生日祝福高级设置存为默认异常！");
			response.getWriter().print(result);
		}
	}
}
