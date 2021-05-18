package com.montnets.emp.pasgroup.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.*;
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
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.pasgroup.biz.UserDataBiz;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.biz.PageFieldConfigBiz;
import com.montnets.emp.servmodule.txgl.entity.LfPageField;
import com.montnets.emp.servmodule.txgl.entity.LfSpFee;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;



/**
 * 短彩SP账户
 * @project p_txgl
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-3-3 上午11:22:33
 * @description
 */
@SuppressWarnings("serial")
public class pag_userDataSvt extends BaseServlet {
	final ErrorLoger errorLoger = new ErrorLoger();
	//List<Userdata> userdataList = null;
	//private WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
	private final UserDataBiz userDataBiz = new UserDataBiz();
	
	//第5位为1 表示用户不需要状态报告 0x00000010
	private static final int NO_RPT 		= 0x00000010;
	
	//第10位为1 表示用户不需要上行    0x00000200
	private static final int NO_MO 			= 0x00000200;
	
	//第16位为1 表示用户不使用长连接  0x00008000
	private static final int NO_LONG_CONN   =  0x00008000;

	// sp账号具有短信能力
	private static final int DX_ABILITY =  0x00000001;
	// sp账号取消短信能力
	private static final int DX_DEBILITY =  0xFFFFFFFE;
	// sp账号具有彩信能力
	private static final int CX_ABILITY =  0x00000002;
	// sp账号取消彩信能力
	private static final int CX_DEBILITY =  0xFFFFFFFD;
	// sp账号具有富信能力
	private static final int FX_ABILITY =  0x00000004;
	// sp账号取消富信能力
	private static final int FX_DEBILITY =  0xFFFFFFFB;
	
	//操作模块
	final String opModule=StaticValue.GATE_CONFIG;
	//操作用户
	final String opSper = StaticValue.OPSPER;
	
	private final String empRoot = "txgl";
	private final String basePath = "/pasgroup";
	
	/**
	 * 短彩sp账户查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		long stime = System.currentTimeMillis();
		boolean isFirstEnter;
		PageInfo pageInfo = new PageInfo();
		//获取系统定义的短彩类型值
		PageFieldConfigBiz gcBiz = new PageFieldConfigBiz();
		List<LfPageField> pagefileds = gcBiz.getPageFieldById("100002");
		try {
			List<Userdata> userdataList = null;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			isFirstEnter = pageSet(pageInfo,request);
			if(!isFirstEnter){
				//账户名称
				conditionMap.put("staffName&like", request.getParameter("staffName").toUpperCase());
				conditionMap.put("userId&like", request.getParameter("userId").toUpperCase());
				//账户状态
				conditionMap.put("status", request.getParameter("status"));
				String accountType = request.getParameter("accountType")!=null?request.getParameter("accountType"):"";
				if("1".equals(accountType)){
					conditionMap.put("accouttype", "1");
					conditionMap.put("accability&&", "1");
				}else if("2".equals(accountType)){
					conditionMap.put("accouttype", "2");
					conditionMap.put("accability&&", "2");
				}else if("4".equals(accountType)){
					conditionMap.put("accouttype", "1");
					conditionMap.put("accability&&", "4");
				}
				conditionMap.put("sptype", request.getParameter("loginId"));
				//计费类型
				conditionMap.put("feeFlag", request.getParameter("feeflag"));
			}
			
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			//orderbyMap.put("khdate", StaticValue.DESC);
			orderbyMap.put("orderTime", StaticValue.DESC);
			conditionMap.put("uid&>", "100001");
			conditionMap.put("userType", "0");
			
			int corpType = StaticValue.getCORPTYPE();
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//企业编码
			//String corpCode = request.getParameter("lgcorpcode");
			String corpCode = lfSysuser.getCorpCode();
			if("100000".equals(corpCode))
			{
				/*userdataList = wgMsgBiz.findSpUser(conditionMap, orderbyMap, pageInfo);*/
				userdataList = userDataBiz.findSpUser(conditionMap, orderbyMap, pageInfo);
			}else
			{
				if(corpType==0){
					conditionMap.put("userprivilege&<>", "11");
					//单企业
					/*userdataList = wgMsgBiz.findSpUser(conditionMap, orderbyMap, pageInfo);*/
					userdataList = userDataBiz.findSpUser(conditionMap, orderbyMap, pageInfo);
				}else{
					//多企业
					/*userdataList = wgMsgBiz.findSpUserByCorp(corpCode,conditionMap,orderbyMap, pageInfo);*/
					userdataList = userDataBiz.findSpUserByCorp(corpCode,conditionMap,orderbyMap, pageInfo);
				}
			}
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
						EmpExecutionContext.error("查询短彩SP账户列表，参数加密失败。corpCode:"+corpCode);
						throw new Exception("查询短彩SP账户列表，参数加密失败。");
					}
					result = encryptOrDecrypt.batchEncryptToMap(userdataList, "Uid", keyIdUidMap);
					if(!result)
					{
						EmpExecutionContext.error("查询短彩SP账户列表，参数UID加密失败。corpCode:"+corpCode);
						throw new Exception("查询短彩SP账户列表，参数UID加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询短彩SP账户列表，从session中获取加密对象为空！");
					userdataList.clear();
					throw new Exception("查询短彩SP账户列表，获取加密对象失败。");
				}
			}
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("userdataList", userdataList);
			request.setAttribute("keyIdMap", keyIdMap);
			request.setAttribute("keyIdUidMap", keyIdUidMap);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("pagefileds", pagefileds);
			request.getRequestDispatcher(empRoot  + basePath +"/pag_userData.jsp")
				.forward(request, response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"短彩SP账号","("+sDate+")查询",StaticValue.GET);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"短彩sp账户查询异常！"));
			request.setAttribute("pagefileds", pagefileds);
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(empRoot + basePath +"/pag_userData.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"短彩sp账户查询servlet异常！"));
			} catch (IOException e1) {				
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"短彩sp账户查询servlet跳转异常！"));
			}
		}
	}
	
	/**
	 * 跳转到修改短彩sp账户页面
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
					EmpExecutionContext.error("跳转到修改短彩sp账户，参数解密码失败，keyId:"+keyId);
					throw new Exception("跳转到修改短彩sp账户，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("跳转到修改短彩sp账户，从session中获取加密对象为空！");
				throw new Exception("跳转到修改短彩sp账户，获取加密对象失败。");
			}
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//企业编码
			//String corpCode = (String)request.getAttribute("lgcorpcode");
			String corpCode = lfSysuser.getCorpCode();
			Integer accouttype=Integer.parseInt(request.getParameter("accouttype"));
			Userdata userdata =null;
			if(accouttype==1){
				userdata = userDataBiz.getSmsUserdataByUserid(id);
			}else{
				userdata = userDataBiz.getMmsUserdataByUserid(id);
			}
			List<Userdata> userList = userDataBiz.getAgentUserdata();
			
			//多企业情况下查询lfspfee信息
			if(StaticValue.getCORPTYPE() ==1)
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("spUser", id.toLowerCase());
				conditionMap.put("accountType", accouttype.toString());
				List<LfSpFee> spfeeList = new BaseBiz().getByCondition(LfSpFee.class, conditionMap, null);
				if(spfeeList!=null && spfeeList.size()>0)
				{
					LfSpFee lfSpFee = spfeeList.get(0);
					request.setAttribute("lfSpFee", lfSpFee);
				}
				
			}
			request.setAttribute("userList", userList);
			request.setAttribute("keyId", keyId);
			request.setAttribute("userdata", userdata);
			request.getRequestDispatcher(empRoot  + basePath +"/pag_editUserData.jsp?lgcorpcode="+corpCode)
				.forward(request, response);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"跳转到修改短彩sp账户页面异常！"));
		}
	}
	
	/**
	 * 修改短彩sp账户
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response)
	{
		//企业编码
//		String corpCode = request.getParameter("lgcorpcode");
		String corpCode = "";
		//操作类型
		String hidOpType = request.getParameter("hidOpType");

		Long usertype=Long.valueOf(request.getParameter("usertype"));

		if(usertype-1==0)
		{
			try
			{
				userDataBiz.delAllMrSpgateWatch();
			} catch (Exception e)
			{
				EmpExecutionContext.error(errorLoger.getErrorLog(e,"删除MrSpgateWatch表信息异常"));
			}
		}
		String opUser = request.getParameter("lgusername");
		opUser = opUser==null?"":opUser;
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
						EmpExecutionContext.error("修改短彩sp账户，参数解密码失败，keyId:"+keyId);
						throw new Exception("修改短彩sp账户，参数解密码失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("修改短彩sp账户，从session中获取加密对象为空！");
					throw new Exception("修改短彩sp账户，获取加密对象失败。");
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
			Long riselevel=Long.valueOf(request.getParameter("riselevel"));
			Long feeflag=Long.valueOf(request.getParameter("feeflag"));

			int accouttype= 0;//Integer.parseInt(request.getParameter("accouttype"));
			String accabilityStr =  request.getParameter("accability");
			String dxval =  request.getParameter("dxval");
			String cxval =  request.getParameter("cxval");
			String fxval =  request.getParameter("fxval");
			int accability=Integer.parseInt(null == accabilityStr ? "0" : accabilityStr);
			if("1".equals(dxval)){
				accability = accability | DX_ABILITY;
				accouttype = 1;
			}else{
				accability = accability & DX_DEBILITY;
			}
			if("2".equals(cxval)){
				accability = accability | CX_ABILITY;
				accouttype = 2;
			}else{
				accability = accability & CX_DEBILITY;
			}
			if("4".equals(fxval)){
				accability = accability | FX_ABILITY;
				// 富信发送在托管版7.2走的是短信发送方式，所以暂时存成短信
				accouttype = 1;
			}else{
				accability = accability & FX_DEBILITY;
			}
			//短彩Str
			String dcstr="";
			if(accouttype==1){
				dcstr="短信";
			}else if(accouttype==2){
				dcstr="彩信";
			}
			String lxr=request.getParameter("lxr");
			String lxrph=request.getParameter("lxrph");
			//账户id
			String loginid=request.getParameter("loginid");
			//上行URL
			String moUrl=request.getParameter("moUrl");
			//状态报告URL
			String rptUrl=request.getParameter("rptUrl");
			//账户类型
			String uuuutype = request.getParameter("uuuutype");
			//ip地址
			String ips = request.getParameter("ips");
			if(ips==null||ips.length()<1){
				ips=" ";
			}

			if(lxrph==null ||lxrph.length()<1) lxrph=" ";
			if(lxr==null || lxr.length()<1) lxr=" ";

			//上行、状态报告获取方式
			String morptMode = request.getParameter("morptmode");
			//上行、状态报告URL
			String spBindUrl = request.getParameter("spbindurl");
			//开始发送时间
			String startTime = request.getParameter("starttime");
			//结束发送时间
			String endTime = request.getParameter("endtime");
			//当彩信SP账户时,上行、状态报告获取方式为null,设置默认值2



			if(morptMode == null || "".equals(morptMode))
			{
				morptMode = "2";
			}
			Userdata user=new Userdata();
			user.setAccability(accability);
			//网送直接转发、用户主动获取，不设置URL
			if("0".equals(morptMode))
			{
				moUrl = " ";
				rptUrl = " ";
				spBindUrl = " ";
			}
			//SGIP协议网关主动推送，只设置spBindUrl
			else if("1".equals(morptMode))
			{
				moUrl = " ";
				rptUrl = " ";
			}
			//HTTP协议网关主动推送，设置moUrl和rptUrl
			else if("2".equals(morptMode))
			{
				spBindUrl = " ";
				//推送接口版本
				String pushversion = request.getParameter("pushversion");
				if(pushversion!=null&&pushversion.length()>0){
					user.setPushversion(Integer.parseInt(pushversion));
				}
			}

			//连接方式 1、长连接 2、短连接
			String connMethod = request.getParameter("connmethod");

			//需要上行
			String needMo = request.getParameter("needmo");

			//需要状态报告
			String needRpt = request.getParameter("needrpt");
			int userprivilege = user.getUserprivilege();
			if(!"2".equals(connMethod)) {
				userprivilege = userprivilege|NO_LONG_CONN;
			}
			if(!"true".equals(needMo)) {
				userprivilege = userprivilege|NO_MO;
			}
			if(!"true".equals(needRpt)) {
				userprivilege = userprivilege|NO_RPT;
			}

			user.setUserprivilege(userprivilege);

			user.setFeeFlag(feeflag);
			user.setUserId(userid.toUpperCase());
			user.setStaffName(staffname);
			user.setUserType(usertype);
			user.setRiseLevel(riselevel);
			user.setAccouttype(accouttype);
			user.setLoginIp(ips);
			if(usertype-1==0){
				user.setLoginId(userid);
			}else{
				user.setLoginId(loginid);
			}
			user.setSptype(Integer.parseInt(uuuutype));
			//user.setLxr(lxr);
			//user.setLxrph(lxrph);
			if(curPass == null || !curPass.equals(userpassword))
			{
				user.setUserPassword(userpassword);
			}

			user.setMoUrl(moUrl==null || "".equals(moUrl)?" ":moUrl);
			user.setRptUrl(rptUrl==null || "".equals(rptUrl)?" ":rptUrl);
			user.setSpbindurl(spBindUrl==null || "".equals(spBindUrl)?" ":spBindUrl);
			Long morptModeLong = Long.parseLong(morptMode);
			user.setTransmotype(morptModeLong);
			user.setTransrptype(morptModeLong);
			//拼接发送起止时间，其中一个为空则数据库给默认值
			if(startTime != null && !"".equals(startTime) && endTime != null && !"".equals(endTime))
			{
				String sendtmspan = startTime +"-"+endTime;
				user.setSendtmspan(sendtmspan);
			}
			else
			{
				user.setSendtmspan("00:00:00-23:59:59");
			}

			LfSpFee lfSpFee = null;
			//如果是多企业，就存lf_spfee表
			if(StaticValue.getCORPTYPE()==1)
			{
				lfSpFee = new LfSpFee();
				//运营商余额查询url
				//String feeUrl = request.getParameter("spFeeUrl");
				//扣费类型
				String feeflag_spfee = request.getParameter("feeflag_spfee");
				//这里为空给个默认值，是预防没有相关关联数据时，修改报错
				feeflag_spfee = feeflag_spfee!=null?feeflag_spfee:"1";
				//自增id
				String sfId = request.getParameter("sfId");
				//修改时设id值
				if(sfId!=null && !"".equals(sfId))
				{
					lfSpFee.setSfId(Long.parseLong(sfId));
				}
				else
				{
					//运营商余额
					lfSpFee.setBalance(0);
					//运营商余额
					lfSpFee.setBalanceth(0);
					//更新时间
					lfSpFee.setUpdateTime(new Timestamp(System.currentTimeMillis()));
					//余额查询时间(当前时间减5分钟)
					lfSpFee.setQueryTime(new Timestamp(System.currentTimeMillis()-5 * 60 * 1000));
				}
				// SP账号/后端账号
				lfSpFee.setSpUser(userid.toUpperCase());
				//账号密码
				lfSpFee.setSpUserpassword(userpassword);
				//账号类型(1代表后端账号，2代表SP账号)
				lfSpFee.setSpType(2);
				//账号类型（1.短信   2.彩信）
				lfSpFee.setAccountType(accouttype);
				//运营商余额查询URL
				//lfSpFee.setSpFeeUrl(feeUrl!=null&&!"".equals(feeUrl)?feeUrl:" ");
				//扣费类型(1代表后付费，2代表预付费)
				lfSpFee.setSpFeeFlag(Integer.parseInt(feeflag_spfee));
			}

			boolean result = false;
			if(hidOpType.equals("add")){
				opType=StaticValue.ADD;
				int status=Integer.parseInt(request.getParameter("status"));
				user.setStatus(status);
				result = userDataBiz.addUserdata(user,request.getRemoteAddr(),lfSpFee);
				request.setAttribute("w_userdataResult", "1");
				newStr.append(user.getAccouttype()).append("，").append(user.getUserId()).append("，").append(user.getStaffName())
						.append("，").append(user.getUserPassword()).append("，").append(user.getLoginId()).append("，").append(user.getStatus())
						.append("，"+user.getRiseLevel()).append("，").append(user.getTransmotype()).append("，").append(user.getSpbindurl())
						.append("，").append(user.getRptUrl()).append("，").append(user.getMoUrl()).append("，")
						.append(user.getSendtmspan()).append("，").append(user.getLoginIp());
				opContent.append("新建").append(dcstr).append("SP账户[信息类型，SP账号，账户名称，账户密码，应用类型，账户状态，发送级别，上行、状态报告获取方式，" )
						.append("上行、状态报告URL，上行URL，状态报告URL，发送起止时间，绑定IP]（").append(newStr).append("）");

			}
			else if(hidOpType.equals("edit")){
				opType=StaticValue.UPDATE;
				user.setUid(new Long(request.getParameter("uid")));
				//获取修改前短彩SP账户信息
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

				//修改的时候，不会修改loginid
				user.setLoginId(olduser.getLoginId());

				//重新对userprivilege字段赋值
				userprivilege = user.getUserprivilege()|userprivilege;

				user.setUserprivilege(userprivilege);

				result = userDataBiz.updateUserdata(user,lfSpFee);
				request.setAttribute("w_userdataResult", "2");
				newStr.append(user.getAccouttype()).append("，").append(user.getUserId()).append("，")
						.append(user.getStaffName()).append("，").append(user.getUserPassword()).append("，")
						.append(user.getLoginId()).append("，").append(user.getStatus()).append("，").append(user.getRiseLevel())
						.append("，").append(user.getTransmotype()).append("，").append(user.getSpbindurl()).append("，")
						.append(user.getRptUrl()).append("，").append(user.getMoUrl()).append("，").append(user.getSendtmspan())
						.append("，").append(user.getLoginIp());
				opContent.append("修改").append(dcstr).append("SP账户[信息类型，SP账号，账户名称，账户密码，应用类型，账户状态，发送级别，上行、状态报告获取方式，" )
						.append("上行、状态报告URL，上行URL，状态报告URL，发送起止时间，绑定IP]（").append(oldStr).append("-->").append(newStr).append("）");
			}
			//屏蔽代码，监控模块查询数据库获取
			//同步更新监控系统用到的账号map缓存
			/*if(accouttype==1)
			{
				String [] strs = new String[3];
				//账号名称
				strs [0]=staffname;
				//账号类型
				strs [1]=uuuutype.toString();
				//发送级别
				strs [2]=riselevel.toString();
				synchronized (StaticValue.SPACCOUTN_INFO)
				{
					StaticValue.SPACCOUTN_INFO.put(userid, strs);
				}
			}*/
			if(result)
			{
				//添加成功操作日志
				spLog.logSuccessString(opUser, opModule, opType, opContent.toString(),corpCode);
				//增加操作日志
				setLog(request, "短彩SP账号", opContent.toString()+"成功", opType);
			}else
			{
				//添加失败操作日志
				spLog.logFailureString(opUser, opModule, opType, opContent+opSper, null,corpCode);
				//增加操作日志
				setLog(request, "短彩SP账号", opContent.toString()+"失败", opType);
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
			EmpExecutionContext.error(errorLoger.getErrorLog(ex,"企业：" +corpCode+",操作员：" +opUser+"修改短彩sp账户异常！"));
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
					EmpExecutionContext.error("修改短彩sp账户状态，参数解密码失败，keyId:"+keyId);
					throw new Exception("修改短彩sp账户状态，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("修改短彩sp账户状态，从session中获取加密对象为空！");
				throw new Exception("修改短彩sp账户状态，获取加密对象失败。");
			}
			//SP账户状态
			String status = request.getParameter("status");			
			
			Userdata userdata = baseBiz.getById(Userdata.class, Long.parseLong(uidStr));
			 
			userdata.setStatus(Integer.valueOf(status));
			String type=userdata.getAccouttype()==1?"短信":"彩信";
			if(status.equals("0"))
			{
				
				opContent="修改"+type+"SP账户：("+userdata.getUserId()+")的状态为激活";
			}
			else
			{
				opContent="修改"+type+"SP账户：("+userdata.getUserId()+")的状态为失效";
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
				setLog(request, "短彩SP账号", opContent+"成功", StaticValue.UPDATE);
			}else{
				//增加操作日志
				setLog(request, "短彩SP账号", opContent+"失败", StaticValue.UPDATE);
			}
		} catch (Exception e) {
			//异常错误
			if(writer!=null){
				writer.print("error");
			}
			spLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent+opSper, e,corpcode);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"企业：" +corpcode+",操作员：" +opUser+"修改sp账户状态异常！"));
		}
		return;
	}
	
	/**
	 * 删除sp账户
	 * @param request
	 * @param response
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response)
	{
		
		String userId=request.getParameter("userId");
		//企业编码
		String corpCode = request.getParameter("lgcorpcode");
		
		String opContent = "删除账户（账户ID："+userId+"）成功!";
		BaseBiz baseBiz = new BaseBiz();
		String opUser = request.getParameter("lgusername");
		opUser = opUser==null?"":opUser;
		SuperOpLog spLog = new SuperOpLog();
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			if(userDataBiz.getPortByUserId(userId).size()>0)
			{
				response.getWriter().print("mid");
			}else
			{
				int count=userDataBiz.delUserdataBySpUserId(userId);
				if(count>0)
				{
					LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
					map.put("spUser", userId);
					List<LfSubnoAllot> subnoAllots = baseBiz.getByCondition(LfSubnoAllot.class, map, null);
					if(subnoAllots != null && subnoAllots.size() > 0){
						for(int i=0;i<subnoAllots.size();i++){
							LfSubnoAllot lfSubnoAllot = subnoAllots.get(i);
							lfSubnoAllot.setSpUser("");
							baseBiz.updateObj(lfSubnoAllot);
						}
					}
					
					response.getWriter().print("true");
					//添加成功操作日志
					spLog.logSuccessString(opUser, opModule, StaticValue.DELETE, opContent,corpCode);
					//EmpExecutionContext.error("企业：" +corpCode+",操作员：" +opUser+opContent);
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("模块名称：短彩SP账户，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+opContent+"成功。");
					}
				}else
				{
					response.getWriter().print("false");
					//添加成功操作日志
					spLog.logFailureString(opUser, opModule, StaticValue.DELETE, opContent+opSper, null,corpCode);
					EmpExecutionContext.error("企业：" +corpCode+",操作员：" +opUser+"删除账户（账户ID："+userId+"）为删除删除条数为0!");
				}
			}
		} catch (Exception e)
		{
			if(writer!=null){
				writer.print("error");
			}
			//添加失败操作日志
			spLog.logFailureString(opUser, opModule, StaticValue.DELETE, opContent+opSper, e,corpCode);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"企业：" +corpCode+",操作员：" +opUser+"删除sp账户异常！"));
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
//			String accounttype=request.getParameter("accouttype");
//			user = userDataBiz.getUserdataByUseridandActype(userid,accounttype==null?null:Integer.parseInt(accounttype));
			//查询表中是否已存在该账号
			boolean isSameAccount = userDataBiz.getUserdataByUserid(userid);
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
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"检查SP账户是否存在异常！userid:"+userid));
		}
	}
	
	/**
	 * 新建sp账户页面跳转
	 * @param request
	 * @param response
	 */
	public void toAdd(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String corpCode = (String)request.getAttribute("lgcorpcode");
			List<Userdata> userList = userDataBiz.getAgentUserdata();
			//获取系统定义的短彩类型值
			PageFieldConfigBiz gcBiz = new PageFieldConfigBiz();
			List<LfPageField> pagefileds = gcBiz.getPageFieldById("100002");
			
			//查询代理账号
			LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
			conditionMap.put("uid&>", "100001");
			conditionMap.put("userprivilege", "11");
			List<Userdata> agentUserList=new BaseBiz().getByCondition(Userdata.class, conditionMap, null);
			request.setAttribute("agentUserList", agentUserList);
			
			request.setAttribute("pagefileds", pagefileds);
			request.setAttribute("userList", userList);
			request.getRequestDispatcher(empRoot  + basePath +"/pag_addUserData.jsp?lgcorpcode="+corpCode)
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
				EmpExecutionContext.error("短彩SP账户，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "短彩SP账户，从session获取加密对象异常。");
			return null;
		}
	}
}
