package com.montnets.emp.sysuser.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.util.*;
import org.apache.commons.beanutils.BeanUtils;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.LoginBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.SMParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.employee.biz.EmployeeAddrBookBiz;
import com.montnets.emp.employee.vo.LfEmployeeTypeVo;
import com.montnets.emp.entity.birthwish.LfBirthdaySetup;
import com.montnets.emp.entity.client.LfCliDepConn;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.engine.LfService;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfBalancePri;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfReviewSwitch;
import com.montnets.emp.entity.sysuser.LfReviewer2level;
import com.montnets.emp.entity.sysuser.LfRoles;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.sysuser.biz.DepPriBiz;
import com.montnets.emp.sysuser.biz.RoleBiz;
import com.montnets.emp.sysuser.biz.SysuserPriBiz;
import com.montnets.emp.sysuser.biz.UserBiz;
import com.montnets.emp.sysuser.util.UpWSTools;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.MD5;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 
 * @description 
 */
@SuppressWarnings("serial")
public class opt_sysuserSvt extends BaseServlet {
	
	private final SysuserBiz userBiz = new SysuserBiz();
	private final SysuserPriBiz sysuserPriBiz = new SysuserPriBiz();
	private final String opModule=StaticValue.USERS_MANAGER;
	private final String opSper = StaticValue.OPSPER;
	private static GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
	private final SubnoManagerBiz biz = new SubnoManagerBiz();
	private final EmployeeAddrBookBiz employeeBiz = new EmployeeAddrBookBiz();
	private static final String PATH = "/user/operator";
	final BaseBiz baseBiz=new BaseBiz();
	final SuperOpLog spLog=new SuperOpLog();
	/**
	 * 此方法为有两个参数这样做 当新增件参数时  若参数有值加依次将count加2、加1，count为字符串按二进制表示的10进制数字
	 * @param isMesage 提示是否发送短信
	 * @param isEmail 提示是否发送邮件
	 * @return 以0和1表示的四位字符串
	 */
	public String getControlFlag(boolean isMesage,boolean isEmail){
		int count=0;
		if(isMesage==true){
			count=count+8;
		}else{
			count=count+0;
		}
		if(isEmail==true){
			count=count+4;
		}else{
			count=count+0;
		}
		String toBinary=Integer.toBinaryString(count);
		int addStrCount=toBinary.length();
		for(int i=0;i<4-addStrCount;i++){
			toBinary="0"+toBinary;
		}
		return toBinary;
	} 
	@SuppressWarnings("unchecked")
	public void find(HttpServletRequest request, HttpServletResponse response) 
	{
		List<LfSysuserVo> userList = null;
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		LinkedHashMap<String, String> conditionMap = null;
		PageInfo pageInfo=null;
		try {
			long begin_time=System.currentTimeMillis();
	 		LfSysuserVo lsv = new LfSysuserVo();
			conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
			//Long userId = Long.parseLong(request.getParameter("lguserid"));
			//漏洞修复 session里获取操作员信息
			Long userId = SysuserUtil.longLguserid(request);



			String corpCode = request.getParameter("lgcorpcode");
			String depNameStr = request.getParameter("depNameStr");
			String isEmployee = request.getParameter("isEmployee");
			String isAll = request.getParameter("isAll");
			String returnBySubPage  = request.getParameter("returnBySubPage");
			pageInfo=new PageInfo();
			if(depNameStr != null && !"".equals(depNameStr)){
				depNameStr = URLDecoder.decode(depNameStr, "UTF-8");
			}
			conditionMap.put("depNameStr", depNameStr);
			//是否第一次打开
			boolean isFirstEnter = false;
			isFirstEnter=pageSet(pageInfo, request);
			Long depId = null;
			Long roleId = null;
			String sOprId = null;
			String sOpName = null;
			if("true".equals(returnBySubPage))
			{
				conditionMap = (LinkedHashMap<String, String>)request.getSession(false).getAttribute("sysuserCondition");
				pageInfo = (PageInfo) request.getSession(false).getAttribute("sysuserPageInfo");
				lsv = (LfSysuserVo)  request.getSession(false).getAttribute("sysuserlsv");
			}
			else
			{
				if (!isFirstEnter) {
					//操作员id
				    sOprId = request.getParameter("sOprId");
				    //操作员名称
					sOpName = request.getParameter("sOpName");
					//机构
					String depName = request.getParameter("depName");
					//角色
					String roleName = request.getParameter("roleName");
					conditionMap.put("depName", depName);
					conditionMap.put("roleName", roleName);
					if(sOprId != null && !"".equals(sOprId)){
						sOprId = sOprId.replace("'", "");
					}
					if(sOpName != null && !"".equals(sOpName)){
						sOpName = sOpName.replace("'", "");
						sOpName = URLDecoder.decode(sOpName, "UTF-8");
					}
					conditionMap.put("sOprId", sOprId);
					conditionMap.put("sOpName", sOpName);
					
					if (depName != null && !depName.equals("")) {
						depId = Long.valueOf(depName);
					}
					if (roleName != null && !roleName.equals("")) {
						roleId = Long.valueOf(roleName);
					}
					
					//创建人名字
					String conname = request.getParameter("conname");
					//创建时间起始
					String submitSartTime = request.getParameter("submitSartTime");
					//创建时间结束 
					String submitEndTime = request.getParameter("submitEndTime");
					//手机
					String conphone = request.getParameter("conphone");
					if(conname != null && !"".equals(conname)){
						lsv.setHolder(conname);
						conditionMap.put("conname", conname);
					}
					if(submitSartTime != null && !"".equals(submitSartTime)){
						lsv.setSubmitSartTime(submitSartTime);
						conditionMap.put("submitSartTime", submitSartTime);
					}
					if(submitEndTime != null && !"".equals(submitEndTime)){
						lsv.setSubmitEndTime(submitEndTime);
						conditionMap.put("submitEndTime", submitEndTime);
					}
					if(conphone != null && !"".equals(conphone)){
						lsv.setMobile(conphone);
						conditionMap.put("conphone", conphone);
					}
					//包含子机构
					if(isAll!=null){
						lsv.setIsAll(1);
						conditionMap.put("isAll", "1");
					}
					String isExistSubno = "";
					//判断它是否存在尾号进行处理
					isExistSubno = request.getParameter("subno");
					if(!"".equals(isExistSubno) && isExistSubno != null){
						lsv.setIsExistSubno(Integer.valueOf(isExistSubno));
						conditionMap.put("isSubno", isExistSubno);
					}
					String userState = request.getParameter("userState");
					if(userState != null && !"".equals(userState)){
						lsv.setUserState(Integer.valueOf(userState));
					}
					if(isEmployee != null && !"".equals(isEmployee)){
						lsv.setUserType(Integer.parseInt(isEmployee));
						conditionMap.put("isEmployee", isEmployee);
					}
					conditionMap.put("userState", userState);
				} else {
					//默认第一次登录进去只查询已激活的操作员用户
					lsv.setUserState(1);
					conditionMap.put("userState", "1");	
				}
				lsv.setName(sOpName);
				lsv.setUserName(sOprId);
			}
			conMap.put("corpCode", corpCode);
			List<LfRoles> roleList= baseBiz.getByCondition(LfRoles.class, conMap, null);
			request.setAttribute("roleList", roleList);
			try{
				userList = sysuserPriBiz.getSysuserVo(lsv, userId, depId, roleId,pageInfo);
				if(userList != null && userList.size() > 0)
				{
					//加密对象
					ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
					//加密对象不为空
					if(encryptOrDecrypt != null)
					{
						encryptOrDecrypt.batchEncrypt(userList, "UserId", "KeyId");
					}
					else
					{
						EmpExecutionContext.error("查询操作员列表，从session中获取加密对象为空！");
						userList.clear();
					}
				}
			}catch (Exception e) {
				EmpExecutionContext.error(e,"操作员管理查询操作员失败！");
			}
			
			//作为导出数据的查询条件�?
			request.getSession(false).setAttribute("u_currentUser", lsv);
			request.getSession(false).setAttribute("u_sysDepId", depId);
			request.getSession(false).setAttribute("u_sysRoleID", roleId);
			//二级页面回跳时记录之前查询条件
			request.getSession(false).setAttribute("sysuserCondition", conditionMap);
			request.getSession(false).setAttribute("sysuserPageInfo", pageInfo);
			request.getSession(false).setAttribute("sysuserlsv", lsv);
			
			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("userList", userList);
			request.setAttribute("conditionMap", conditionMap);
			
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
				opSucLog(request, "操作员管理", opContent, "GET");
			}
			request.getRequestDispatcher(PATH +"/opt_sysuser.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "操作员管理find方法异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			try {
				request.getRequestDispatcher(PATH +"/opt_sysuser.jsp")
				.forward(request, response);
			} catch (Exception e1) {				
				EmpExecutionContext.error(e1, "操作员管理页面跳转异常！");
			}
		}
		
	}
	/**
	 *   新建操作�?
	 * @param request
	 * @param response
	 */
	public void add(HttpServletRequest request, HttpServletResponse response)
	{
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		Long opUserId = getOpUserId(request);
		String corpCode = request.getParameter("lgcorpcode");
		//是否接收审批提醒
		String msgRemind = request.getParameter("msgRemind");
		//是否接受邮件提醒
		String emailRemind=request.getParameter("emailRemind");
		String isCustome = request.getParameter("isCustome");
		oppType=StaticValue.ADD;
		Long guid = 0l;
		String haveSubno = request.getParameter("haveSubno");
		String isRepeatSubno = "1";
		LfSysuser sysuser2=new LfSysuser();

		
		if(!"1".equals(haveSubno)){
			sysuser2.setIsExistSubNo(2);
		}
		
		String username = request.getParameter("username");
		String name = request.getParameter("name");
		opContent = "新建操作员（操作员名称："+name+"）";
		String userCode = null;
		if(StaticValue.getCORPTYPE() == 1){
			userCode = corpCode+request.getParameter("userCode");
		}else{
			userCode = request.getParameter("userCode");
		}
		//登录密码等于用户账号
		String password = username;
		Long  depId= Long.valueOf(request.getParameter("depId"));
		Integer sex = Integer.valueOf(request.getParameter("sex"));
		String birthday = request.getParameter("birthday");
		String mobile = request.getParameter("mobile");
		String job = request.getParameter("job");
		String[] roleId = request.getParameter("cheRoles").split(",");
		Integer userState = Integer.valueOf(request.getParameter("userState"));
		String isReviewer = request.getParameter("isReviewer");

		try {
		
			//是否需要被审核
			String isaudited = request.getParameter("isaudited");
			Long domDepId = null;
			String[] employDepId = null;
			String[] clientDepId = null ;
			String[] balanceDepId = null ;
			
			//操作员数据权限
			if("2".equals(request.getParameter("userPerType"))){
				domDepId = Long.valueOf(request.getParameter("domDepId"));
			}
			//员工通讯录权限
			if("2".equals(request.getParameter("employPerType"))){
				String domDepId_employ = request.getParameter("domDepId_employ");
				//employDepId = domDepId_employ.split(",");
				employDepId = getEmployeeDepIds(domDepId_employ);
			}
			//客户通讯录权限
			if("2".equals(request.getParameter("clientPerType"))){
				String domDepId_client = request.getParameter("domDepId_client");
				//clientDepId = domDepId_client.split(",");
				clientDepId = getClientDepIds(domDepId_client);
			}
			
			// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
			// 充值回收权限
			if("2".equals(request.getParameter("depPerType"))){
				//获取所有选中的机构id
				String domDepId_dep = request.getParameter("domDepId_dep");
				//获得操作员所属机构的第一级子机构id
				//String firstLevelChildDepIds = new SysuserPriBiz().getFirstLevelChildDepIds(Long.valueOf(depId));
				//过滤重复id
				balanceDepId = getBalanceDepIds(domDepId_dep);
				//balanceDepId = domDepId_dep.split(",");
			}
			/*
			else{
				//如果为1，则为操作员所属机构的所有第一级子机构
				String domDepId_dep = "";
				String firstLevelChildDepIds = new SysuserPriBiz().getFirstLevelChildDepIds(Long.valueOf(depId));
				balanceDepId = getBalanceDepIds(domDepId_dep, firstLevelChildDepIds);
			}
			*/
			// end
			
			
			//是否加入员工通讯录
			String isEmploy = request.getParameter("isEmploy");
			//备注
			String comments = request.getParameter("comments");
		
			
			if (request.getParameter("eguid") == null
					|| "".equals(request.getParameter("eguid"))) {
				guid = Long.parseLong(request.getParameter("guid"));
			} else {
				guid = Long.parseLong(request.getParameter("eguid"));
			}
			//这里是判断是否需要绑定操作员固定尾号    1是需要，2是不需要
			
			String oph = request.getParameter("oph");//座机
			String qq = request.getParameter("qq");
			String EMail = request.getParameter("EMail");
			String fax = request.getParameter("fax");
			String msn = request.getParameter("msn");
			boolean isMesage=msgRemind!=null&&"on".equals(msgRemind);
			boolean isEmail=emailRemind!=null&&"on".equals(emailRemind);
			String controlFlag=getControlFlag(isMesage, isEmail);
			sysuser2.setControlFlag(controlFlag);
			//是否客服人员
			if(new SmsBiz().isWyModule("20")){
				if("on".equals(isCustome)){
					sysuser2.setIsCustome(1);
				}else{
					sysuser2.setIsCustome(0);
				}
			}
			//是否显示数字
			String showNum = request.getParameter("showNum");
			if(StringUtils.IsNullOrEmpty(showNum)){
				sysuser2.setShowNum(0);
			}else{
				sysuser2.setShowNum(Integer.parseInt(showNum));
			}
			sysuser2.setUserName(username);
			sysuser2.setName(name);
			sysuser2.setDepId(depId);
			sysuser2.setSex(sex);
			sysuser2.setOph(oph);
			sysuser2.setMobile(mobile);
			
			sysuser2.setQq(qq);
			sysuser2.setEMail(EMail);
			sysuser2.setFax(fax);
			sysuser2.setGuId(guid);
			sysuser2.setUserState(userState);
			String guidstr = request.getParameter("lgguid");
			LfSysuser sysuser = baseBiz.getByGuId(LfSysuser.class, Long.parseLong(guidstr));
			sysuser2.setHolder(sysuser.getName());
            sysuser2.setCorpCode(corpCode);
            sysuser2.setPermissionType(Integer.parseInt(request.getParameter("userPerType")));
            sysuser2.setUserCode(userCode);
            sysuser2.setMsn(msn);
            sysuser2.setDuties(job);
            sysuser2.setComments(comments);
           
            //判断是否该下拉框被设置无效
    		if(isReviewer != null && !"".equals(isReviewer)){
    			sysuser2.setIsReviewer(Integer.parseInt(isReviewer));
			}else{
				sysuser2.setIsReviewer(2);
			}

            if("1".equals(isaudited)){
                //设置为必审
                sysuser2.setIsAudited(1);
            }else{//值不为1 默认为免审
                sysuser2.setIsAudited(2);
            }

            sysuser2.setClientState(0);
            sysuser2.setEmployeeId(0L);
            sysuser2.setManualInput(0);
            sysuser2.setOnLine(0);
            sysuser2.setPid(0);
            sysuser2.setPostId(0L);
			sysuser2.setUdgId(0);
			sysuser2.setUpGuId(0L);
			sysuser2.setUserType(0);
			sysuser2.setWorkState(0);
            if(birthday != null && !"".equals(birthday)){
            	sysuser2.setBirthday(Timestamp.valueOf(birthday+" 00:00:00"));
            }
			
			sysuser2.setPassword(MD5.getMD5Str(password+username.toLowerCase()));
			LfEmployee lc = null;
			if("1".equals(isEmploy)){
				lc = new LfEmployee();
				
				sysuser2.setUserType(2);
				sysuser2.setUserState(1);
				lc.setGuId(guid);
				lc.setIsOperator(1);
				String employeeNo = request.getParameter("employeeNo");
				String employDep = request.getParameter("employDepId");
				lc.setDepId(Long.parseLong(employDep));
				
				if (birthday != null && !"".equals(birthday)){
					lc.setBirthday(Timestamp.valueOf(birthday+" 00:00:00"));
				}
				lc.setEmployeeNo(employeeNo.toUpperCase());
				lc.setName(name);
				lc.setSex(sex);
				lc.setMobile(mobile);
				lc.setQq(qq);
				lc.setMsn(msn);
				lc.setEmail(EMail);
				lc.setOph(oph);
				lc.setFax(fax);
				lc.setDuties(job);
				lc.setCommnets(comments);
				lc.setCorpCode(corpCode);
			}
			sysuser2.setPwdupdatetime(new Timestamp(System.currentTimeMillis()));
			Long[] roleIds=new Long[roleId.length];
			for (int r = 0; r < roleIds.length; r++) {
				roleIds[r] = Long.valueOf(roleId[r]);
			}
			boolean result = new SysuserPriBiz().add(domDepId, roleIds, sysuser2,lc,employDepId,clientDepId,balanceDepId,opUserId);//
			if(result){
				//modify by tanglili 20181227----------开始---
				//新建操作员成功，则移除newUserCode
				HttpSession session=request.getSession(false);
				if(session.getAttribute("newUserCode")!=null)
				{
					session.removeAttribute("newUserCode");
				}
				//modify by tanglili 20181227----------结束---

				if("1".equals(haveSubno)){
					//修改的尾号
					String addSubno = request.getParameter("addSubno");  
					//新增时赋予的尾号
					String subno2 = request.getParameter("subno2");		
					sysuser2.setIsExistSubNo(1);
					if(!subno2.equals(addSubno)){
						isRepeatSubno = sysuserPriBiz.updateUserSubno(guid,subno2,addSubno, corpCode);
						if("2".equals(isRepeatSubno)){
							sysuser2.setIsExistSubNo(2);
						}
					}else{
						biz.updateSubnoStat(addSubno,corpCode, null);
					}
					baseBiz.updateObj(sysuser2);
				}
				String info = "(操作员名称："+sysuser2.getName()+"，登录帐号："+sysuser2.getUserName()+"，所属机构："+sysuser2.getDepId()+"，手机："
				+sysuser2.getMobile()+"，guid:"+guid+")";
				opSucLog(request, opModule, "新建操作员"+info+"成功。", "ADD");
				spLog.logSuccessString(opUser, opModule, oppType, opContent,corpCode);
			}else {	
				//失败 
				EmpExecutionContext.error("企业："+corpCode+",操作员："+opUser+","+opContent+"失败");
				spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, null,corpCode);
			}
			request.setAttribute("result", String.valueOf(result));
			//判断修改的尾号是否重�?
			if(!"1".equals(isRepeatSubno)){
				request.setAttribute("result", isRepeatSubno);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"新建操作员出现异常！");
 			spLog.logFailureString(opUser, opModule, oppType, opContent + opSper, e, corpCode);
 			request.setAttribute("result", "errer");
		} finally {
            toAdd(request, response);
        }
	}
	
	/**
	 * 修改操作员
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response)
	{
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		Long opUserId = getOpUserId(request);
		String optype = request.getParameter("optype");
		String username = request.getParameter("username");
		//Long userId = Long.valueOf(request.getParameter("userId"));
		String name = request.getParameter("name");
		String keyId = request.getParameter("keyId");
		oppType=StaticValue.UPDATE;
		if("update".equals(optype)){
			 opContent="修改操作员（操作员名称："+name+"）";
		}else{
			 opContent="恢复操作员（操作员名称："+name+"）";
		}
	   
		//如果是正常的话就继续�?
		 String msg = "1";
		String lgcorpcode = request.getParameter("lgcorpcode");
		try{
			String userIdStr = "";
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				userIdStr = encryptOrDecrypt.decrypt(keyId);
				if(userIdStr == null)
				{
					EmpExecutionContext.error("修改操作员信息，参数解密码失败，keyId:"+keyId);
					request.setAttribute("result", "errer");
					request.setAttribute("keyId", keyId);
					throw new Exception("修改操作员信息，参数解密码失败，keyId:"+keyId);
				}
			}
			else
			{
				EmpExecutionContext.error("修改操作员信息，从session中获取加密对象为空！");
				request.setAttribute("result", "errer");
				request.setAttribute("keyId", keyId);
				throw new Exception("修改操作员信息，从session中获取加密对象为空！");
			}
		Long userId = Long.valueOf(userIdStr);
		LfSysuser sysuser2=baseBiz.getById(LfSysuser.class, userId);
		LfSysuser _sysuser2=(LfSysuser) BeanUtils.cloneBean(sysuser2);
		//这里是判断是否是修改还是回收
		String haveSubno = request.getParameter("haveSubno");
		//判断该操作员已分配了还是没有分配
		String isGiveSubno = request.getParameter("isGiveSubno");
		String corpCode = sysuser2.getCorpCode();
		//判断进去修改操作员，确定该操作员是已经有尾号  还是没有尾号
		if("1".equals(isGiveSubno)){
			//判断是否该操作员有固定尾号，取到值就有，没有就没有分配固定尾�?
			String subno = request.getParameter("subno");	
			//这里是获取修改的尾号
			String updateSubno = request.getParameter("updateSubno");
			
			//这里判断是对尾号进行修改还是回收
			if("1".equals(haveSubno)){
				sysuser2.setIsExistSubNo(1);
				 if(!subno.trim().equals(updateSubno)){	
					 msg = sysuserPriBiz.updateUserSubno(sysuser2.getGuId(),subno,updateSubno, corpCode);
				 }else{
					biz.updateSubnoStat(subno,corpCode, null);
				}
			}else if("2".equals(haveSubno)){
				//这里做回�?
					sysuser2.setIsExistSubNo(2);
					sysuserPriBiz.delUserSubno(sysuser2);
			}
		}else if("2".equals(isGiveSubno)){
			//判断是否该操作员有固定尾号，取到值就有，没有就没有分配固定尾�?
			String subno2 = request.getParameter("subno2");	
			//这里是分配的尾号
			String addSubno = request.getParameter("addSubno");
			//这里判断是对尾号进行分配还是不分�?
			if("1".equals(haveSubno)){
				//这里是做分配尾号
				sysuser2.setIsExistSubNo(1);
				 if(!subno2.trim().equals(addSubno)){	
						//做更�?
						 msg = sysuserPriBiz.updateUserSubno(sysuser2.getGuId(),subno2,addSubno,corpCode);
						if("2".equals(msg)){
							sysuser2.setIsExistSubNo(2);
						}
				 }else{
					 	//对有效状�?   以及 永久的更�?
						biz.updateSubnoStat(subno2,corpCode, null);
				 }
			}else if("2".equals(haveSubno)){
				//这里是不做操�?
				sysuser2.setIsExistSubNo(2);
			}
		}
		if("1".equals(msg)){
			Long  depId= Long.valueOf(request.getParameter("depId"));
			Integer sex = Integer.valueOf(request.getParameter("sex"));
			String oph = request.getParameter("oph");
			String mobile = request.getParameter("mobile");
			String qq = request.getParameter("qq");
			String EMail = request.getParameter("EMail");
			String birthday = request.getParameter("birthday");
			String job = request.getParameter("job");
			Integer userState = Integer.valueOf(request.getParameter("userState"));
			String isReviewer = request.getParameter("isReviewer");
			String isaudited = request.getParameter("isaudited");
			
			Integer permissionType = Integer.valueOf(request.getParameter("userPerType"));

			String[] roleId = request.getParameter("cheRoles").split(",");
			Long domDepId = null;
			String[] employDepId = null;
			String[] clientDepId = null ;
			String[] balanceDepId = null;
				
				//机构权限
				if("2".equals(request.getParameter("userPerType"))){
					domDepId = Long.valueOf(request.getParameter("domDepId"));
				}
				if("2".equals(request.getParameter("employPerType"))){
					String domDepId_employ = request.getParameter("domDepId_employ");
//					employDepId = domDepId_employ.split(",");
					employDepId = getEmployeeDepIds(domDepId_employ);
				}
				if("2".equals(request.getParameter("clientPerType"))){
					String domDepId_client = request.getParameter("domDepId_client");
//					clientDepId = domDepId_client.split(",");
					clientDepId = getClientDepIds(domDepId_client);
				}
				// EMP5.7新需求：增加对操作员充值和回收权限   by pengj				
				if("2".equals(request.getParameter("depPerType"))){
					String domDepId_dep = request.getParameter("domDepId_dep");
					//获得操作员所属机构的一级子机构
					//String firstLevelChildDepIds = new SysuserPriBiz().getFirstLevelChildDepIds(depId);
					//过滤重复id
					balanceDepId = getBalanceDepIds(domDepId_dep);
					//balanceDepId = domDepId_dep.split(",");
				}
				/*
				else{
					//如果为1，则为操作员所属机构的所有第一级子机构
					String domDepId_dep = "";
					String firstLevelChildDepIds = new SysuserPriBiz().getFirstLevelChildDepIds(depId);
					balanceDepId = getBalanceDepIds(domDepId_dep, firstLevelChildDepIds);
				}
				*/
				// end				
				String isEmploy = request.getParameter("isEmploy");
				String comments = request.getParameter("comments");
				String fax = request.getParameter("fax");
				String msn = request.getParameter("msn");
				
				sysuser2.setUserName(username);
				sysuser2.setName(name);
				sysuser2.setDepId(depId);
				sysuser2.setSex(sex);
				sysuser2.setOph(oph);
				sysuser2.setMobile(mobile);
				if(isReviewer != null && !"".equals(isReviewer)){
					sysuser2.setIsReviewer(Integer.parseInt(isReviewer));
				}
				if(isaudited != null && !"".equals(isaudited)){
					sysuser2.setIsAudited(Integer.valueOf(isaudited));
				}
				sysuser2.setQq(qq);
				sysuser2.setFax(fax);
				sysuser2.setEMail(EMail);
				sysuser2.setUserState(userState);
				sysuser2.setDuties(job);
				sysuser2.setPermissionType(permissionType);
				sysuser2.setComments(comments);
				sysuser2.setMsn(msn);
				String msgRemind = request.getParameter("msgRemind");
				String emailRemind=request.getParameter("emailRemind");
				String isCustome = request.getParameter("isCustome");
				boolean isMesage=msgRemind!=null&&"on".equals(msgRemind);
				boolean isEmail=emailRemind!=null&&"on".equals(emailRemind);
				//这里有两个参数时这样设置 如果增加多余的参数请修改getControlFlag方法
				String controlFlag=getControlFlag(isMesage, isEmail);
				sysuser2.setControlFlag(controlFlag);
				
				//是否客服人员
				//存在客服模块则处理该字段值
				if(new SmsBiz().isWyModule("20")){
					if("on".equals(isCustome)){
						sysuser2.setIsCustome(1);
					}else{
						sysuser2.setIsCustome(0);
					}
				}
			    if(birthday != null && !"".equals(birthday)){
	            	sysuser2.setBirthday(Timestamp.valueOf(birthday+" 00:00:00"));
	            }
				Long[] roleIds=new Long[roleId.length];
				for (int r = 0; r < roleIds.length; r++) {
					roleIds[r] = Long.valueOf(roleId[r]);
				}
				sysuser2.setUserId(userId);
				
				LfEmployee employee = baseBiz.getByGuId(LfEmployee.class, sysuser2.getGuId());
			if("1".equals(isEmploy)){
				if(employee == null){
					employee = new LfEmployee();
				}
				//调整可以对加入员工的记录的空工号进行修改
				String employeeNo = request.getParameter("employeeNo");
				if(employeeNo!=null){
					employeeNo=employeeNo.trim();
					employee.setEmployeeNo(employeeNo.toUpperCase());
				}
				sysuser2.setUserType(2);
				sysuser2.setUserState(userState);
				employee.setGuId(sysuser2.getGuId());
				employee.setIsOperator(1);
				
				String employDep = request.getParameter("employDepId");
				employee.setDepId(Long.parseLong(employDep));
				
				if (birthday != null && !"".equals(birthday)){
					employee.setBirthday(Timestamp.valueOf(birthday+" 00:00:00"));
				}
				
				employee.setName(name);
				employee.setSex(sex);
				employee.setMobile(mobile);
				employee.setQq(qq);
				employee.setMsn(msn);
				employee.setEmail(EMail);
				employee.setOph(oph);
				employee.setFax(fax);
				employee.setDuties(job);
				employee.setCommnets(comments);
				employee.setCorpCode(corpCode);
			}else {
				sysuser2.setUserType(0);
				if(employee != null){
					employee.setIsOperator(0);
				}
			}
			//不管是禁用还是启用该pwderrortimes状态都要设置为0	
				sysuser2.setPwderrortimes(0);

				//是否显示数字
				String showNum = request.getParameter("showNum");
				if(StringUtils.IsNullOrEmpty(showNum)){
					sysuser2.setShowNum(0);
				}else{
					sysuser2.setShowNum(Integer.parseInt(showNum));
				}
				
				boolean result= sysuserPriBiz.update(domDepId, roleIds, sysuser2,employee, employDepId, clientDepId, balanceDepId, opUserId);
				if(result){	
					String info = "[登录帐号，所属机构，手机，账号状态，设置审核流程] （"+
					_sysuser2.getUserName()+","+_sysuser2.getDepId()+","+_sysuser2.getMobile()+","+_sysuser2.getUserState()+","+_sysuser2.getIsAudited()+
					"）-->（"+sysuser2.getUserName()+","+sysuser2.getDepId()+","+sysuser2.getMobile()+","+sysuser2.getUserState()+","+_sysuser2.getIsAudited()+"）";
					opSucLog(request, opModule, "修改操作员"+info+"成功。", "ADD");
					spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
				}else{	//失败 
					EmpExecutionContext.error("企业："+lgcorpcode+",操作员："+opUser+","+opContent+"失败 ");
					spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, null,lgcorpcode);
				}
				request.setAttribute("result", String.valueOf(result));
	            toEdit(request,response);
			}else{
				request.setAttribute("result", "subnoRepeat");
				request.setAttribute("userId", userId);
				toEdit(request, response);
			}
		}  catch (Exception e)
		{
			EmpExecutionContext.error(e,"修改操作员出现异常！");
			spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,lgcorpcode);
			request.setAttribute("result", "errer");
			request.setAttribute("keyId", keyId);
			toEdit(request, response);
		}
		  
	}
	
	//操作员转员工
	/*public void toEmployee(HttpServletRequest request, HttpServletResponse response)
	{
		String id = request.getParameter("id");
		Long userId = Long.parseLong(request.getParameter("lguserid"));
		String corpCode = request.getParameter("lgcorpcode");
		try {
 			LfSysuser user = null;
			if (id != null && !"".equals(id)) {
				user = baseBiz.getById(LfSysuser.class, id);
			}
			if (user == null) {
				user = new LfSysuser();
			}
 			List<LfEmployeeTypeVo> bookInfoList = employeeBiz.getAllEmployeeTypeVo(userId,corpCode);
			request.setAttribute("zwList", bookInfoList);
 			request.setAttribute("user", user);
 			request.getRequestDispatcher(PATH+"/opt_editEmployee.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e);
		}
	}
	*/
	//验证操作员id是否已经存在
	public void checkName(HttpServletRequest request, HttpServletResponse response)
	{
		String username = request.getParameter("username");
		String corpCode = request.getParameter("lgcorpcode");
		try {
 			if(username.equals("")){
 				response.getWriter().print(false);
			} else {
                String exists = "false";
 	 			LfSysuser user = new LoginBiz().login(username, null,corpCode);
 	 			if(user!=null ) {
					if (user.getUserState() == 2) {
	 	 				LfDep dep = baseBiz.getById(LfDep.class, user.getDepId());
						exists = "true," + user.getUserId() + ","
								+ user.getName() + "," + dep.getDepName();
					} else {
 	 					exists="dotrue";
 	 				}
 	 			}
 	 			response.getWriter().print(exists);
 				//this.writer.print(userBiz.isUserNameExists(username));
 			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"检测操作员用户名是否重复出现异常！");
		}
	}
	
	public void checkUserCode(HttpServletRequest request, HttpServletResponse response)
	{
		String userCode = request.getParameter("userCode");
		String isEmploy = request.getParameter("isEmploy");
		String employeeNo = request.getParameter("employeeNo");
		String orCode = request.getParameter("orCode");
		String optype = request.getParameter("optype");
 		try
		{
 			LfSysuser lfSysuser=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
 			if(userCode.equals("")){
 				response.getWriter().print(false);
 				return;
 			}else{
 				String corpCode = "";
 				if(StaticValue.getCORPTYPE() == 1 ){
 					corpCode = lfSysuser.getCorpCode();
 				}
 				boolean exists = false;
 				//由于optype可能取值 use 此种情况同样不需要进行重复检查 
 				//修改在更新时不做操作员编号的重复检查
 				if(!"update".equals(optype)&&!"use".equals(optype)){
 					exists =  sysuserPriBiz.isUserCodeExists(corpCode,userCode);
 				}
                if(employeeNo!=null&&!"".equals(employeeNo.trim())){
                	employeeNo=employeeNo.trim();
                }else{
                	employeeNo=null;
                }
                if(employeeNo!=null&&!exists && "1".equals(isEmploy)&&!employeeNo.equals(orCode)){
                	//验证员工号
                	LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                	conditionMap.put("employeeNo", employeeNo.toUpperCase());
    				//将员工号转换成大写
    				conditionMap.put("corpCode", corpCode);
    				//将员工号转换成大写
    				List<LfEmployee> le = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
    				if(le != null && le.size()>0){
    					response.getWriter().print("existEmploy");
    					return;
    				}
                }
                response.getWriter().print(exists);
 			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"判断操作员用户编码重复出现异常！");
		}
	}
	
	public void checkUserNameAndCode(HttpServletRequest request, HttpServletResponse response)
	{
		
		String username = request.getParameter("username");
		String userCode = request.getParameter("userCode");
		//String isEmploy = request.getParameter("isEmploy");
		//String employeeNo = request.getParameter("employeeNo");
		
 		String result = "";
 		PrintWriter writer = null;
		try
		{
			LfSysuser lfSysuser=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			writer = response.getWriter();
 			if(username == null || "".equals(username)){
 				result = "emptyName";
			}else if(userCode == null || "".equals(userCode)){
				result = "emptyCode";
			} else {
				String corpCode = request.getParameter("lgcorpcode");
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("userName", username);
				conditionMap.put("corpCode", corpCode);
				List<LfSysuser> lfSysusers = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
				//验证username
				if (lfSysusers != null && lfSysusers.size()>0) {
					result = "nameExist";
				}else{
					//验证usercode
					corpCode = "";
	 				if(StaticValue.getCORPTYPE() == 1 ){
	 					corpCode = lfSysuser.getCorpCode();
	 				}
	                boolean exists =  sysuserPriBiz.isUserCodeExists(corpCode,userCode);
	                result = exists?"codeExist":"true";
				}
 			}
 			writer.print(result);
		} catch (Exception e) {
			if(writer!=null){
				writer.print("error");
			}
			EmpExecutionContext.error(e,"检测操作员用户名以及用户编码出现异常！");
		}
	}
	
	/**
	 *   更新操作员状�?
	 * @param request
	 * @param response
	 */
	public void changeState(HttpServletRequest request, HttpServletResponse response)
	{
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		String lgcorpcode = request.getParameter("lgcorpcode");
		oppType=StaticValue.UPDATE;
		String name = request.getParameter("name");
		String us = request.getParameter("us");
		opContent = "修改操作员（操作员名称："+name+"）";
//		Long userId= Long.valueOf(request.getParameter("userId"));
		Integer state = null;
		try {
			String keyId = request.getParameter("keyId");
			String userIdStr = "";
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				userIdStr = encryptOrDecrypt.decrypt(keyId);
				if(userIdStr == null)
				{
					EmpExecutionContext.error("修改操作员状态，参数解密码失败，keyId:"+keyId);
					response.getWriter().print("error");
					return;
				}
			}
			else
			{
				EmpExecutionContext.error("修改操作员状态，从session中获取加密对象为空！");
				response.getWriter().print("error");
				return;
			}
			Long userId= Long.valueOf(userIdStr);
			String info = "";
			if ("1".equals(us)) {
				//info = userBiz.getFlowInfoByOperId(userId);
				info = sysuserPriBiz.judgeIsExFlowRecord(userId);
			}
			if (info != null && !"".equals(info)) {
				response.getWriter().print("no");
				return;
			}
			state = sysuserPriBiz.changeState(userId);
			if( state!=null)
			{
				String stateName = state==1?"启用":"禁用";
				opContent+=stateName+"、";
				spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
				opSucLog(request, opModule, "修改操作员（ID："+userId+",名称："+name+"）状态为"+stateName+"成功。", "UPDATE");
				response.getWriter().print(state);
			}else{
				spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, null,lgcorpcode);
				EmpExecutionContext.error("企业："+lgcorpcode+",操作员："+opUser+","+opContent+"失败");
				response.getWriter().print("error");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"修改操作员状态出现异常！");
			spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,lgcorpcode);
		}
	
	}

	public void toEdit(HttpServletRequest request,HttpServletResponse response)
	{
//		String userId = request.getParameter("userId");
		String keyId = request.getParameter("keyId");
		String userId = "";
		//加密对象
		ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
		//加密对象不为空
		if(encryptOrDecrypt != null)
		{
			//解密
			userId = encryptOrDecrypt.decrypt(keyId);
			if(userId == null)
			{
				EmpExecutionContext.error("修改操作员信息，参数解密码失败，keyId:"+keyId);
				return;
			}
		}
		else
		{
			EmpExecutionContext.error("修改操作员信息，从session中获取加密对象为空！");
			return;
		}
		
		
		LfSysuserVo sysuserVo = new LfSysuserVo();
		Long lguserid = Long.parseLong(request.getParameter("lguserid"));
		String lgcorpcode = request.getParameter("lgcorpcode");
		try
	    {
			HttpSession session = request.getSession(false);
			RoleBiz roleBiz=new RoleBiz();
			List<LfRoles> roleList=roleBiz.getAllRoles(lguserid);
			session.setAttribute("roleList", roleList);
			if(null != userId && 0 != userId.length())
			{
				LfSysuser suser = baseBiz.getById(LfSysuser.class, userId);
				request.setAttribute("lfsysuser",suser);
				LfSysuser sysuser = (LfSysuser)  request.getSession(false).getAttribute("loginSysuser");
				sysuserVo = userBiz.getSysuserVoByUserIdAndCorpCode(Long.valueOf(userId), sysuser.getCorpCode());
				sysuserVo.setPid(suser.getPid());
				sysuserVo.setPostId(suser.getPostId());
				//设置加密ID
				sysuserVo.setKeyId(keyId);
				List<LfReviewer2level> lrl = sysuserPriBiz.getFlowInfosByOperId(Long.parseLong(userId));
				request.setAttribute("lrl", lrl);
				request.setAttribute("isExistSubno", suser.getIsExistSubNo());
				//判断是否分配操作员固定尾�?  分配了就进去 尾号规则表去查询该操作员的尾�?
				if(suser.getIsExistSubNo() == 1){
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("corpCode", lgcorpcode);
					conditionMap.put("loginId", String.valueOf(suser.getGuId()));
					conditionMap.put("menuCode&is null", "isnull");
					conditionMap.put("busCode&is null", "isnull");
					conditionMap.put("isValid", "1");
					List<LfSubnoAllot> subnoAllotList = baseBiz.getByCondition(LfSubnoAllot.class, conditionMap, null);
					if(subnoAllotList != null && subnoAllotList.size()>0){
						LfSubnoAllot subnoAllot = subnoAllotList.get(0);
						request.setAttribute("usedSubno", subnoAllot.getUsedExtendSubno());
					}
				}else{
					//删除数据库中在操作员没有分配尾号的状态时候， 去数据库删除
					sysuserPriBiz.delUserSubno(suser);
				}
			}
			//if(sysuserVo.getUserType()==2){
				
				LfEmployee employee = baseBiz.getByGuId(LfEmployee.class, sysuserVo.getGuId());
				if(employee != null){
					LfEmployeeDep dep = baseBiz.getById(LfEmployeeDep.class, employee.getDepId());
					request.setAttribute("employee", employee);
					if(dep != null){
						request.setAttribute("employeeDep", dep.getDepName());
					}
					
				}
				
			//}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId",sysuserVo.getUserId().toString());
			List<LfEmpDepConn> empDepConn = baseBiz.getByCondition(LfEmpDepConn.class, conditionMap, null);
			List<LfCliDepConn> clientDepConn = baseBiz.getByCondition(LfCliDepConn.class, conditionMap, null);
			//新增 充值回收权限
			//根据userid查出所有充值回收权限
			List<LfBalancePri> balancePri = baseBiz.getByCondition(LfBalancePri.class, conditionMap, null);
			if(empDepConn != null && empDepConn.size()>0){
				String depIds = "";
				for (LfEmpDepConn lfEmpDepConn : empDepConn) {
					depIds +=lfEmpDepConn.getDepId()+",";
				}
				depIds = depIds.substring(0,depIds.lastIndexOf(","));
				conditionMap.clear();
				conditionMap.put("depId&in", depIds);
				List<LfEmployeeDep> employeeDeps = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
				String depNames = "";
				for (LfEmployeeDep lfEmployeeDep : employeeDeps) {
					depNames += lfEmployeeDep.getDepName()+";";
				}
				request.setAttribute("employeeIds", depIds);
				request.setAttribute("employeeNames", depNames);
			}
			
			if(clientDepConn != null && clientDepConn.size()>0){
				String depIds = "";
				for (LfCliDepConn lfCliDepConn : clientDepConn) {
					depIds +=lfCliDepConn.getDepId()+",";
				}
				depIds = depIds.substring(0,depIds.lastIndexOf(","));
				conditionMap.clear();
				conditionMap.put("depId&in", depIds);
				List<LfClientDep> clientDeps = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
				String depNames = "";
				for (LfClientDep lfClientDep : clientDeps) {
					depNames += lfClientDep.getDepName()+";";
				}
				request.setAttribute("clientIds", depIds);
				request.setAttribute("clientNames", depNames);
			}
			
			// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
			//用于充值回收权限修改页面的数据回显
			if( balancePri != null && balancePri.size()>0){
				String depIds = "";
				for (LfBalancePri lfBalancePri : balancePri) {
					depIds +=lfBalancePri.getDepId()+",";
				}
				depIds = depIds.substring(0,depIds.lastIndexOf(","));
				conditionMap.clear();
				conditionMap.put("depId&in", depIds);
				conditionMap.put("depState", "1");
				List<LfDep> balancePriDeps = baseBiz.getByCondition(LfDep.class, conditionMap, null);
				String depNames = "";
				for (LfDep balancePriDep : balancePriDeps) {
					depNames += balancePriDep.getDepName()+";";
				}
				request.setAttribute("balancePriIds", depIds);
				request.setAttribute("balancePriNames", depNames);
			}
			// end
			
			List<LfEmployeeTypeVo> bookInfoList = employeeBiz.getAllEmployeeTypeVo( lguserid,lgcorpcode);
			request.setAttribute("zwList", bookInfoList);
   			Integer subnoDigit = userBiz.getSubnoDidit(lgcorpcode);
			//获取该企业的默认子号长度
   			request.setAttribute("subnoDigit",subnoDigit);
   			
   			conditionMap.clear();
			conditionMap.put("corpCode",lgcorpcode);
			//控制审核范围的开关
			conditionMap.put("switchType","1");
			List<LfReviewSwitch> switchList = baseBiz.getByCondition(LfReviewSwitch.class, conditionMap, null);
			String switchFlag = "2";
			//1是开启审核范围开关    2是一个都没有开启
			if(switchList != null && switchList.size()>0){
				switchFlag = "1";
			}
			
			request.setAttribute("switchFlag",switchFlag);
			request.setAttribute("sysuser", sysuserVo);
			request.setAttribute("optype", request.getParameter("optype"));
			request.getRequestDispatcher(PATH +"/opt_editSysuser.jsp")
				.forward(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"跳转修改操作员界面出现异常！");
		}
	}
	/**
	 * 跳转到新建操作员页面
	 * @Title: toAdd
	 * @Description: TODO
	 * @param request
	 * @param response 
	 * @return void
	 */
	public void toAdd(HttpServletRequest request,HttpServletResponse response)
	{
		String lgguid = request.getParameter("lgguid");
		try
		{
			HttpSession session = request.getSession(false);
			LfSysuser sysuser = null;
			if(lgguid != null && !"".equals(lgguid) && !"undefined".equals(lgguid))
			{
			 sysuser = baseBiz.getByGuId(LfSysuser.class, Long.parseLong(lgguid));
			}else{
				EmpExecutionContext.error("跳转到操作员新增页面异常，lgguid为空！lgguid:"+lgguid);
				return;
			}
			int permissionType = sysuser.getPermissionType();
			
			if(permissionType != 2){
				request.getRequestDispatcher("/common/no_dep_permission.jsp").forward(
						request, response);
				return;
			}
			
			RoleBiz roleBiz=new RoleBiz();
			List<LfRoles> roleList=roleBiz.getAllRoles(sysuser.getUserId());
			session.setAttribute("roleList", roleList);
		
			WgMsgConfigBiz roBiz = new WgMsgConfigBiz();
   			List<Userdata> userList=roBiz.getAllRoutUserdata();
			session.setAttribute("routUserdatas", userList);
			List<LfEmployeeTypeVo> bookInfoList = employeeBiz.getAllEmployeeTypeVo( sysuser.getUserId(),sysuser.getCorpCode());
			request.setAttribute("zwList", bookInfoList);
			//员工的id，用于员工转为操作员
			String eid = request.getParameter("eid");
			
			Long guid = null;
			if (eid != null && !"".equals(eid))
			{
				LfEmployee le =  baseBiz.getById(LfEmployee.class, eid);
				request.setAttribute("le", le);
				guid = le.getGuId();
			}else{
				//modify by tanglili 20181227----------开始---
				//如果Session中不存在newUserCode，则重新获取，如果存在，则从session中取。
				if(session.getAttribute("newUserCode")==null)
				{
					guid = globalBiz.getValueByKey("guid", 1L);
					session.setAttribute("newUserCode",String.valueOf(guid));
				}else
				{
					guid=Long.parseLong(String.valueOf(session.getAttribute("newUserCode")));
				}
				//modify by tanglili 20181227----------结束---
			}
			
			//List<LfPosition> positionList = baseBiz.getEntityList(LfPosition.class);只显示有效的职务
//			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//			conditionMap.put("isdel", "0");
//			List<LfPosition> positionList = baseBiz.getByCondition(LfPosition.class, conditionMap, null);
//   			request.setAttribute("positionList", positionList);
   			
   			//List<OaPost> postList =baseBiz.getEntityList(OaPost.class);只显示有效的岗位
//   			conditionMap.clear();
//   			conditionMap.put("isdel", "0");
//   			List<OaPost> postList = baseBiz.getByCondition(OaPost.class, conditionMap, null);
//   			request.setAttribute("postList", postList);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode",sysuser.getCorpCode());
			//控制审核范围的开关
			conditionMap.put("switchType","1");
			List<LfReviewSwitch> switchList = baseBiz.getByCondition(LfReviewSwitch.class, conditionMap, null);
			String switchFlag = "2";
			//1是开启审核范围开关    2是一个都没有开启
			if(switchList != null && switchList.size()>0){
				switchFlag = "1";
			}
			request.setAttribute("switchFlag",switchFlag);
			
			Integer subnoDigit = userBiz.getSubnoDidit(sysuser.getCorpCode());
			//获取该企业的默认子号长度
   			request.setAttribute("subnoDigit",subnoDigit);
   			request.setAttribute("guid", guid);
   			request.setAttribute("corpCode", sysuser.getCorpCode());
			request.getRequestDispatcher(PATH +"/opt_addSysuser.jsp")
				.forward(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"操作员新增页面跳转出现异常！");
		}
	}
	
	/**
	 * 跳转到操作员导入页面
	 * @Title: toImport
	 * @Description: TODO
	 * @param request
	 * @param response 
	 * @return void
	 */
	public void toImport(HttpServletRequest request,HttpServletResponse response)
	{
		String lgguid = request.getParameter("lgguid");
		try
		{
			HttpSession session = request.getSession(false);
			LfSysuser sysuser = null;
			if(lgguid != null && !"".equals(lgguid) && !"undefined".equals(lgguid))
			{
			 sysuser = baseBiz.getByGuId(LfSysuser.class, Long.parseLong(lgguid));
			}else{
				EmpExecutionContext.error("跳转到操作员导入页面异常，lgguid为空！lgguid:"+lgguid);
				return;
			}
			int permissionType = sysuser.getPermissionType();
			
			if(permissionType != 2){
				request.getRequestDispatcher("/common/no_dep_permission.jsp").forward(
						request, response);
				return;
			}
			
			RoleBiz roleBiz=new RoleBiz();
			List<LfRoles> roleList=roleBiz.getAllRoles(sysuser.getUserId());
			session.setAttribute("roleList", roleList);
		
			//员工的id，用于员工转为操作员
			String eid = request.getParameter("eid");
			
			Long guid = null;
			if (eid != null && !"".equals(eid))
			{
				LfEmployee le =  baseBiz.getById(LfEmployee.class, eid);
				request.setAttribute("le", le);
				guid = le.getGuId();
			}else{
				guid = globalBiz.getValueByKey("guid", 1L);
			}
			
   			request.setAttribute("guid", guid);
   			request.setAttribute("corpCode", sysuser.getCorpCode());
			request.getRequestDispatcher(PATH +"/opt_importSysuser.jsp")
				.forward(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"操作员导入页面跳转出现异常！");
		}
	}
	
	public void createTree1(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		Long depId = null;
		String userIdStr = request.getParameter("lguserid");
		Long userId = null;
		LfSysuser lfSysuser = null;
		try{
			//从session中获取操作员
			lfSysuser = getLoginUser(request);
			userId = Long.parseLong(userIdStr);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"操作员管理当前操作员lguserid转化失败！");
		}
		String depStr = request.getParameter("id");
		if(depStr != null && !"".equals(depStr.trim())){
			depId = Long.parseLong(depStr);
		}
		String corpCode = lfSysuser==null?"":lfSysuser.getCorpCode();
		String sysuserTree = getDepartmentAndSysuserJosnData1(depId,userId,corpCode);
		try {
			response.getWriter().print(sysuserTree);
		} catch (IOException e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e, "获取操作员机构树失败！");
		}
	}
	
	public void createTree(HttpServletRequest request,HttpServletResponse response)
	{
		DepBiz depBiz = new DepBiz();
		List<LfDep> lfDeps;
		List<LfSysuser> lfSysusers;
		StringBuffer tree = null;
		try {
			LfSysuser user=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			//lfDeps = depBiz.getAllDeps(user.getUserId());
			lfDeps = depBiz.getAllDepByUserIdAndCorpCode(user.getUserId(), user.getCorpCode());
			LfDep lfDep = null;
			tree = new StringBuffer("[");
			for (int i = 0; i < lfDeps.size(); i++) {
				lfDep = lfDeps.get(i);
				tree.append("{");
				tree.append("id:").append(lfDep.getDepId());
				tree.append(",name:'").append(lfDep.getDepName()).append("'");
				tree.append(",pId:").append(lfDep.getSuperiorId());
				tree.append(",isParent:").append(true);
				tree.append("}");
				if(i != lfDeps.size()-1){
					tree.append(",");
				}
			}
			
			lfSysusers = sysuserPriBiz.getAllUsedSysusers(user.getUserId());
			LfSysuser lfSysuser = null;
			if(!lfSysusers.isEmpty()){
				tree.append(",");
			}
			for (int i = 0; i < lfSysusers.size(); i++) {
				lfSysuser = lfSysusers.get(i);
				tree.append("{");
				tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
				tree.append(",name:'").append(lfSysuser.getName()).append("'");
				tree.append(",pId:").append(lfSysuser.getDepId());
				tree.append(",isParent:").append(false);
				tree.append("}");
				if(i != lfSysusers.size()-1){
					tree.append(",");
				}
			}
			
			tree.append("]");
			response.getWriter().print(tree.toString());
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取操作员管理界面机构树出现异常！");
		}
	}
	
	public void getFlowInfo(HttpServletRequest request,HttpServletResponse response)
	{
		try
		{
			String operId = request.getParameter("operId");
			String info = "";
			if (operId != null && !"".equals(operId))
			{
				Long id = Long.parseLong(operId);
				info  = userBiz.getFlowInfoByOperId(id);
				if (!"".equals(info))
				{
					info = info.substring(0, info.length()-1);
				}
			}
			response.getWriter().print(info);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取该操作员审核流程信息失败！");
		}
	}
	
	
	/**
	 *     新增操作员的时�?，验证手机号是否合法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void checkPhone(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=UTF-8");
		//手机号码
		String phone = request.getParameter("mobile");		
		boolean checkOk = false;
		 try{
			WgMsgConfigBiz wgMsgConfigBiz=new WgMsgConfigBiz();
			String[] haoduans=wgMsgConfigBiz.getHaoduan();	
			if(new PhoneUtil().getPhoneType(phone, haoduans)>=0){
				checkOk =true;
			}
			response.getWriter().print(checkOk);
		 }catch(Exception e){
			 response.getWriter().print("");
			 EmpExecutionContext.error(e,"操作员管理检验手机号合法出现异常！");
		 }
		
	
	}
	
	
	/**
	 *     判断是否操作员所在机构人数大�?000
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void checkDepCount(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=UTF-8");
		//机构ID
		String depId = request.getParameter("depId");		
		String corpCode = request.getParameter("lgcorpcode");
		boolean checkOk = false;
		 try{
			DepPriBiz depBiz = new DepPriBiz();
			checkOk = depBiz.isDepCount(Long.valueOf(depId), corpCode);
			response.getWriter().print(checkOk);
		 }catch(Exception e){
			 response.getWriter().print("");
			 EmpExecutionContext.error(e,"判断操作员所在机构人数出现异常！");
		 }
		
	
	}
	
	
	/**
	 *   将操作员的密码进行初始化
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void InitPassWord(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		String lgcorpcode = request.getParameter("lgcorpcode");
		boolean flag = false;
		 try{
			String keyId = request.getParameter("keyId");
			String userId = "";
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				userId = encryptOrDecrypt.decrypt(keyId);
				if(userId == null)
				{
					EmpExecutionContext.error("初始化操作员密码，参数解密码失败，keyId:"+keyId);
					response.getWriter().print("error");
					return;
				}
			}
			else
			{
				EmpExecutionContext.error("初始化操作员密码，从session中获取加密对象为空！");
				response.getWriter().print("error");
				return;
			}
			response.setContentType("text/html;charset=UTF-8");
			//GUID
			String guId = request.getParameter("guId");				
			//用户名
			String userName = request.getParameter("userName");		
			String name = request.getParameter("name");
			oppType= StaticValue.UPDATE;
		    opContent="初始化操作员（操作员名称"+name+"）";
		    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId",userId);
			LfSysuser sysuser = (LfSysuser)  request.getSession(false).getAttribute("loginSysuser");
			conditionMap.put("corpCode",sysuser.getCorpCode());
			List<LfSysuser> userList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
			if(userList != null && userList.size() > 0)
			{
				LfSysuser user = userList.get(0);
				//LfSysuser user = baseBiz.getByGuId(LfSysuser.class, Long.valueOf(guId));
				user.setPassword(MD5.getMD5Str(userName+userName.toLowerCase()));
				//对锁定状态的操作员进行初始化密码还需改变其状态为启用 以及 密码错误次数清零
				if(user.getUserState()==3){
					user.setUserState(1);
					//密码连续错误次数清零
					user.setPwderrortimes(0);
				}
				flag = baseBiz.updateObj(user);
			}
			if(flag)
			{
				EmpExecutionContext.info("企业："+lgcorpcode+",操作员："+opUser+","+opContent+"成功");
				spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
			}else{
				EmpExecutionContext.error("企业："+lgcorpcode+",操作员："+opUser+","+opContent+"失败");
				spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, null,lgcorpcode);
			}
			response.getWriter().print(flag);
		 }catch(Exception e){
			EmpExecutionContext.error(e,"初始化操作员密码失败！");
			spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, null,lgcorpcode);
			response.getWriter().print("error");
		 }
	}
	
	
	/**
	 *   点击CHECKBOX�?获取�?�� 子号�?
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getSingleSubno(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String subno = "";
		 try{
			String guid = request.getParameter("guid");				//GUID
			String corpCode = request.getParameter("lgcorpcode"); //getCorpCode();
			LfSubnoAllotDetail detail = null;
			ErrorCodeParam errorCode = new ErrorCodeParam();
		//	detail = globalBiz.getSubnoDetail(null, String.valueOf(guid),4,corpCode, null,null,false);
			
			//detail = globalBiz.getSubnoDetail(null, String.valueOf(guid),4,corpCode, null,null,false);
			SMParams smParams = new SMParams ();
			smParams.setCodes(String.valueOf(guid));
			smParams.setCodeType(4);
			smParams.setCorpCode(corpCode);
			smParams.setAllotType(null);
			smParams.setSubnoVali(false);
			smParams.setLoginId(guid);
			detail = globalBiz.getSubnoDetail(smParams,errorCode);
			if(detail != null){
				subno = detail.getUsedExtendSubno();
			}else{
				subno = errorCode.getErrorCode();
				if("EZHB237".equals(subno)){	
					//尾号不够
					subno = "enough";
				}else{			
					//获取尾号失败
					subno = "notsubno";
				}
			}
			response.getWriter().print(subno);
		 }catch(Exception e){
			 EmpExecutionContext.error(e,"获取操作员尾号失败！");
			 response.getWriter().print("");
		 }
	}
	
	/**
	 *   在新建操作员的时候点击CHECKBOX�?删除�?�� 尾号
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delSingleSubno(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String corpCode = request.getParameter("lgcorpcode");
		 try{
			String guid = request.getParameter("guid");	
			if(guid==null||"".equals(guid)){
				response.getWriter().print("");
				return;
			}
			//GUID
			String msg= sysuserPriBiz.delUserSubno(Long.valueOf(guid),corpCode);
			EmpExecutionContext.info("企业："+corpCode+",删除操作员尾号成功");
			response.getWriter().print(msg);
		 }catch(Exception e){
			 EmpExecutionContext.error(e,"删除操作员尾号失败！");
			 response.getWriter().print("");
		 }
	}
	
	/**
	 * 删除操作
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	
	public void delete_old(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try{
			String userId = request.getParameter("userId");
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, userId);
			LfSysuser lfSysuser=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			//判断是否存在审核流
			conditionMap.put("userId", sysuser.getUserName());
			List<LfFlow> flowList = baseBiz.getByCondition(LfFlow.class, conditionMap, null);
			conditionMap.clear();
			conditionMap.put("userId", userId);
			List<LfReviewer2level> r2lList = baseBiz.getByCondition(LfReviewer2level.class, conditionMap, null);
			if((flowList != null && flowList.size() > 0) || (r2lList != null && r2lList.size() > 0))
			{
				response.getWriter().print("flowExist");
				return;
			}
			
			//判断是否存在定时未发送的短信任务
			conditionMap.clear();
			//未发
			conditionMap.put("sendstate", "0");
			//定时
			conditionMap.put("timerStatus", "1");
			conditionMap.put("subState", "2");
			conditionMap.put("userId", userId);
			List<LfMttask> mttaskList = baseBiz.getByCondition(LfMttask.class, conditionMap, null);
			if(mttaskList != null && mttaskList.size() > 0)
			{
				response.getWriter().print("mttaskExist");
				return;
			}
			
			//生日祝福判断
			conditionMap.clear();
			conditionMap.put("userId", userId);
			List<LfBirthdaySetup> birList = baseBiz.getByCondition(LfBirthdaySetup.class, conditionMap, null);
			if(birList != null && birList.size() > 0)
			{
				response.getWriter().print("birExist");
				return;
			}
			
			//下行业务判断
			conditionMap.clear();
			conditionMap.put("userId", userId);
			List<LfService> serviceList = baseBiz.getByCondition(LfService.class, conditionMap, null);
			if(serviceList != null && serviceList.size() > 0)
			{
				response.getWriter().print("serExist");
				return;
			}
			
			//群组，自定义通讯
			conditionMap.clear();
			conditionMap.put("userId", userId);
			List<LfUdgroup> udgList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, null);
			List<LfMalist> maList = baseBiz.getByCondition(LfMalist.class, conditionMap, null);
			
			if((udgList != null && udgList.size() > 0) || (maList != null && maList.size() > 0))
			{
				response.getWriter().print("bookExist");
				return;
			}
			
			//删除客户权限，员工权�?
			//baseBiz.deleteByCondition(LfCliDepConn.class, conditionMap);
			//baseBiz.deleteByCondition(LfEmpDepConn.class, conditionMap);
			
			//删除子号分配记录
			conditionMap.clear();
			conditionMap.put("loginId", sysuser.getGuId().toString());
			conditionMap.put("corpCode", lfSysuser.getCorpCode());
			conditionMap.put("menuCode&is null", "isnull");
			conditionMap.put("busCode&is null", "isnull");
			
			baseBiz.deleteByCondition(LfSubnoAllotDetail.class, conditionMap);
			baseBiz.deleteByCondition(LfSubnoAllot.class, conditionMap);
			
			//更新状�?
			conditionMap.clear();
			conditionMap.put("userId", userId);
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("userState", "2");
			//设置成无尾号
			objectMap.put("isExistSubNo","2");
			EmpExecutionContext.info("企业："+sysuser.getCorpCode()+",操作员："+userId+",删除操作员成功");
			response.getWriter().print(baseBiz.update(LfSysuser.class, objectMap, conditionMap));
			
		}catch(Exception e){
			EmpExecutionContext.error(e,"删除操作员失败！");
			response.getWriter().print("false");
		}
	}

	/**
	 * 注销操作
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		try{
//			String userId = request.getParameter("userId");
			String keyId = request.getParameter("keyId");
			String userId = "";
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				userId = encryptOrDecrypt.decrypt(keyId);
				if(userId == null)
				{
					EmpExecutionContext.error("注销操作员，参数解密码失败，keyId:"+keyId);
					response.getWriter().print("false");
					return;
				}
			}
			else
			{
				EmpExecutionContext.error("注销操作员，从session中获取加密对象为空！");
				response.getWriter().print("false");
				return;
			}
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, userId);
			oppType=StaticValue.UPDATE;
			opContent = "注销操作员（操作员名称："+sysuser.getName()+"）";
			boolean flag = sysuserPriBiz.deleteUser(sysuser);
			if(flag)
			{
				opSucLog(request, opModule, "注销操作员（ID："+userId+"，名称："+sysuser.getName()+"）成功。", "UPDATE");
				spLog.logSuccessString(opUser, opModule, oppType, opContent,sysuser.getCorpCode());
			}else{
				EmpExecutionContext.error("企业："+sysuser.getCorpCode()+",操作员："+opUser+","+opContent+"失败");
				spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, null,sysuser.getCorpCode());
			}
			response.getWriter().print(flag);
		}catch(Exception e){
			EmpExecutionContext.error(e,"注销操作员失败！");
			response.getWriter().print("false");
		}
	}
	
	/**
	 *   处理统一平台同步数据请求�?
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void syncInfo(HttpServletRequest request , HttpServletResponse response) throws IOException {
		String type = request.getParameter("type");
		UserBiz userBiz = new UserBiz();
		String returnMsg = "";
		try {
			LfSysuser lfSysuser=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			Long guid = lfSysuser.getGuId();
			LfSysuser user = null;
			String name = "";
			if(UpWSTools.isNotNull(guid)){
				user = baseBiz.getByGuId(LfSysuser.class, guid);
			}
			if(user != null){
				name = user.getName();
			}else{
				name= "[系统]";
			}
			returnMsg = userBiz.syncUserDep(type, name);
			response.getWriter().print(returnMsg);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"处理统一平台同步数据请求失败！");
		}
	}
	
	
	/**
	 * 验证操作员id是否已经存在
	 * @param request
	 * @param response
	 */
	public void checkUserName(HttpServletRequest request, HttpServletResponse response)
	{
		String username = request.getParameter("username");
		String corpCode = request.getParameter("lgcorpcode");
		String orname = request.getParameter("orname");
		try {
                String exists = "false";
                if(username==null||"".equals(username.trim())){
                	return;
                }
                if(!username.equals(orname)){
	                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
	                conditionMap.put("userName",username);
	                conditionMap.put("corpCode",corpCode);
	 	 			List<LfSysuser> userList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
	 	 			if(userList != null && userList.size()>0){
	 	 				exists = "dotrue";
	 	 			}
                }
 	 			response.getWriter().print(exists);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"判断操作员用户名是否存在出现异常！");
		}
	}
	
	/**
	 * 机构树
	 * @param depId
	 * @param userId
	 * @return
	 */
	protected String getDepartmentAndSysuserJosnData1(Long depId,Long userId){
		//机构biz
		DepBiz depBiz = new DepBiz();
		//操作员biz
		SysuserBiz sysuserBiz = new SysuserBiz();
		List<LfDep> lfDeps;
		List<LfSysuser> lfSysusers;
		StringBuffer tree = null;
		try {
			lfDeps = null;
			
			//如果部门id等于空，查所有
			if(depId == null){
				lfDeps = new ArrayList<LfDep>();
				LfDep lfDep = depBiz.getAllDeps(userId).get(0);
				lfDeps.add(lfDep);
				//lfDeps.addAll(new DepBiz().getDepsByDepSuperId(lfDep.getDepId()));
			}else{
				//查询机构id下所有的机构
				lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			}
			
			LfDep lfDep = null;
			
			//json数据拼写
			tree = new StringBuffer("[");
			for (int i = 0; i < lfDeps.size(); i++) {
				lfDep = lfDeps.get(i);
				tree.append("{");
				tree.append("id:").append(lfDep.getDepId());
				tree.append(",name:'").append(lfDep.getDepName()).append("'");
				tree.append(",pId:").append(lfDep.getSuperiorId());
				tree.append(",isParent:").append(true);
				tree.append("}");
				if(i != lfDeps.size()-1){
					tree.append(",");
				}
			}
			
			//获取操作员
			lfSysusers = sysuserBiz.getAllSysusers1(depId);
			LfSysuser lfSysuser = null;
			if(!lfDeps.isEmpty() && !lfSysusers.isEmpty()){
				tree.append(",");
			}
			for (int i = 0; i < lfSysusers.size(); i++) {
				lfSysuser = lfSysusers.get(i);
				tree.append("{");
				tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
				tree.append(",userName:'").append(lfSysuser.getUserName()).append("'");
				tree.append(",name:'").append(lfSysuser.getName()).append("'");
				tree.append(",pId:").append(lfSysuser.getDepId());
				tree.append(",isParent:").append(false);
				tree.append("}");
				if(i != lfSysusers.size()-1){
					tree.append(",");
				}
			}
			
			tree.append("]");
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"获取操作员机构树出现异常！");
		}
		return tree.toString();
	}
	
	/**
	 * 机构树
	 * @param depId
	 * @param userId
	 * @return
	 */
	protected String getDepartmentAndSysuserJosnData1(Long depId,Long userId,String corpCode){
		//机构biz
		DepBiz depBiz = new DepBiz();
		//操作员biz
		SysuserBiz sysuserBiz = new SysuserBiz();
		List<LfDep> lfDeps;
		List<LfSysuser> lfSysusers;
		StringBuffer tree = null;
		try {
			lfDeps = null;
			
			//如果部门id等于空，查所有
			if(depId == null){
				lfDeps = new ArrayList<LfDep>();
				//LfDep lfDep = depBiz.getAllDeps(userId).get(0);
				LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userId, corpCode).get(0);
				lfDeps.add(lfDep);
				//lfDeps.addAll(new DepBiz().getDepsByDepSuperId(lfDep.getDepId()));
			}else{
				//查询机构id下所有的机构
				//lfDeps = new DepBiz().getDepsByDepSuperId(depId);
				lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId, corpCode);
			}
			
			LfDep lfDep = null;
			
			//json数据拼写
			tree = new StringBuffer("[");
			for (int i = 0; i < lfDeps.size(); i++) {
				lfDep = lfDeps.get(i);
				tree.append("{");
				tree.append("id:").append(lfDep.getDepId());
				tree.append(",name:'").append(lfDep.getDepName()).append("'");
				tree.append(",pId:").append(lfDep.getSuperiorId());
				tree.append(",isParent:").append(true);
				tree.append("}");
				if(i != lfDeps.size()-1){
					tree.append(",");
				}
			}
			
			//获取操作员
			lfSysusers = sysuserBiz.getAllSysusers1(depId);
			LfSysuser lfSysuser = null;
			if(!lfDeps.isEmpty() && !lfSysusers.isEmpty()){
				tree.append(",");
			}
			for (int i = 0; i < lfSysusers.size(); i++) {
				lfSysuser = lfSysusers.get(i);
				tree.append("{");
				tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
				tree.append(",userName:'").append(lfSysuser.getUserName()).append("'");
				tree.append(",name:'").append(lfSysuser.getName()).append("'");
				tree.append(",pId:").append(lfSysuser.getDepId());
				tree.append(",isParent:").append(false);
				tree.append("}");
				if(i != lfSysusers.size()-1){
					tree.append(",");
				}
			}
			
			tree.append("]");
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"获取操作员机构树出现异常！");
		}
		return tree.toString();
	}
	

	public void updateRole(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		SuperOpLog spLog = new SuperOpLog();
		String opUser = getOpUser(request);
		String opModule=StaticValue.USERS_MANAGER;
		String opSper = StaticValue.OPSPER;
	    String oppType = null;
		String opContent =null;
 	
		//String userId = request.getParameter("userId");
		String userId = "";
		String userName = request.getParameter("userName");
		String corpCode = request.getParameter("lgcorpcode");
		String keyId = request.getParameter("keyId");
		try
		{
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				userId = encryptOrDecrypt.decrypt(keyId);
				if(userId == null)
				{
					EmpExecutionContext.error("注销操作员，参数解密码失败，keyId:"+keyId);
					response.getWriter().print("-1");
					return;
				}
			}
			else
			{
				EmpExecutionContext.error("修改操作员角色信息，从session中获取加密对象为空！");
				response.getWriter().print("-1");
				return;
			}
			oppType=StaticValue.UPDATE;
			opContent="修改操作员（操作员名称:"+userName+"）角色";
			
			Long id = Long.valueOf(userId);
			if(id != null)
			{
				
				String rolesId=request.getParameter("rolesId");
				String[] roleIds=rolesId.split(",");
				Long[] roleId=new Long[roleIds.length];
				for(int i=0;i<roleIds.length;i++)
				{
					if(roleIds[i]!= null && !roleIds[i].equals(""))
					{
						roleId[i]=Long.valueOf(roleIds[i]);
					}
				}
				int count= sysuserPriBiz.updateOperRole(id, roleId);
				//添加操作成功日志
				spLog.logSuccessString(opUser, opModule, oppType, opContent,corpCode);
				opSucLog(request, opModule, "修改操作员角色（ID："+userId+"，名称："+userName+"，修改后角色："+rolesId+"）成功。", "UPDATE");
				response.getWriter().print(count);
			}else
			{
				EmpExecutionContext.error("企业："+corpCode+",操作员："+opUser+","+opContent+"失败");
				response.getWriter().print("-1");
			}
			
		} catch (Exception e)
		{
			//添加操作失败日志
			spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,corpCode);
			EmpExecutionContext.error("企业："+corpCode+",操作员："+opUser+","+opContent+"异常");
			response.getWriter().print("-1");
			EmpExecutionContext.error(e,"修改操作员角色失败！");
		}	
	}
	
	/**
	 *   获取机构的 数，只 限查子级
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			LfSysuser lfSysuser=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			String depId = request.getParameter("depId");
			List<LfEmployeeDep> empDepList = sysuserPriBiz.getEmpSecondDepTreeByUserIdorDepId(lfSysuser.getUserId().toString(),depId);
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
			EmpExecutionContext.error(e,"获取操作员机构失败！");
		}
	}
	
	/**
	 * 操作员导出
	 * @param request
	 * @param response
	 */
	public void exportToExcel(HttpServletRequest request , HttpServletResponse response) {
		
		//获取模块路径
		//String context=request.getSession().getServletContext().getRealPath("/fileUpload/excelDownload");
		String excelPath = new TxtFileUtil().getWebRoot()+"user/operator/file";
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		try{
			PrintWriter out = response.getWriter();
			//是否第一次打开
			PageInfo pageInfo=new PageInfo();
			pageSet(pageInfo, request);
	 		//获取当前操作员的userID
			//Long userId = Long.parseLong(request.getParameter("lguserid"));
			//漏洞修复 session里获取操作员信息
			Long userId = SysuserUtil.longLguserid(request);
			//获取当前企业编码 
			String corpCode = request.getParameter("lgcorpcode");
			//获取导出数据的查询条件。
			LfSysuserVo lsv = (LfSysuserVo)request.getSession(false).getAttribute("u_currentUser");
			Long depId = (Long) request.getSession(false).getAttribute("u_sysDepId");
			Long roleId = (Long) request.getSession(false).getAttribute("u_sysRoleID");
			
			Map<String, String> resultMap = new SysuserPriBiz().createSysUserExcel(excelPath,lsv,userId,depId,roleId,pageInfo,request);

			if (resultMap != null && resultMap.size() > 0) {
				String fileName=resultMap.get("FILE_NAME");
		        String filePath=resultMap.get("FILE_PATH");
		        
		        //写日志
		        if(pageInfo!=null){
					long end_time=System.currentTimeMillis();
					String opContent = "导出操作员开始时间："+format.format(System.currentTimeMillis())+",耗时:"+(end_time-begin_time)+"ms,导出总数："+pageInfo.getTotalRec() + "条 ";
			        opSucLog(request, "操作员管理", opContent, "OTHER");
		        }
		        
		      request.getSession(false).setAttribute("exportToExcel", fileName+"@@"+filePath);	
		      out.print("true");
	
			} else {
				out.print("false");
				
				
//				//操作员id
//				String  sOprId = request.getParameter("sOprId");
//				//操作员名称
//				String	sOpName = request.getParameter("sOpName");
//				//机构
//				String depName = request.getParameter("depName");
//				//角色
//				String roleName = request.getParameter("roleName");
//				response.sendRedirect(request.getContextPath()+"/opt_sysuser.htm?lguserid="+userId+"&lgcorpcode="+corpCode+"&sOprId="+sOprId+"&sOpName="+sOpName+"&depName="+depName+"&roleName="+roleName);
			
			}
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e, "操作员导出异常！");
		}
		
	}
	
	/**
	 * excel文件导出
	 * @param request
	 * @param response
	 */
	   public void downloadFile(HttpServletRequest request, HttpServletResponse response)   {
		   String down_session=request.getParameter("down_session");
		    try {
				HttpSession session = request.getSession(false);
				Object obj = session.getAttribute(down_session);
				if(obj != null){
				    String result = (String) obj;
				    if(result.indexOf("@@")>-1){
				    	String[] file=result.split("@@");
				        // 弹出下载页面。
				        DownloadFile dfs = new DownloadFile();
				        dfs.downFile(request, response, file[1], file[0]);
				        session.removeAttribute(down_session);
				    }
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "excel文件导出异常！");
			}
	    }

	/**
	 *   获取客户机构的 数，只 限查子级
	 * @param request
	 * @param response
	 */
	public void getClientDepCheck(HttpServletRequest request, HttpServletResponse response)
	{
		String checkedIds = request.getParameter("checkedIds");
		List<String> ids = null;
		if(checkedIds != null && !"".equals(checkedIds)){
			ids = new ArrayList<String>();
			String[] chId = checkedIds.split(",");
			for (String str : chId) {
				ids.add(str);
			}
		}
		AddrBookSpecialDAO specialDAO = new AddrBookSpecialDAO();
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			String depId = request.getParameter("id");
			LfSysuser lfSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//此方法只查询两级机构
			if(lfSysuser == null){return;}
			List<LfClientDep> clientDepList = specialDAO.getCliSecondDepTreeByUserIdorDepId(lfSysuser.getUserId().toString(), depId);
			LfClientDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(clientDepList != null && clientDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < clientDepList.size(); i++) {
					dep = clientDepList.get(i);
					tree.append("{");
					tree.append("id:").append(dep.getDepId()+"");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird()).append("'");
					//树数据中加入父机构id
					if(dep.getParentId()-0==0){
						tree.append(",pId:").append(0);
					}else{
						tree.append(",pId:").append(dep.getParentId());
					}
					tree.append(",isParent:").append(true);
					if(ids != null && ids.contains(dep.getDepId().toString())){
						tree.append(",checked:").append(true);
					}
					tree.append("}");
					if(i != clientDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			writer.print(tree.toString());
		} catch (Exception e)
		{
			if(writer!=null){
				writer.print("");
			}
			EmpExecutionContext.error(e,"获取客户机构树失败！");
		}
	}

	//过滤员工机构
	private String[] getEmployeeDepIds(String depIds)
	{
		String[] ids = depIds.split(",");
		LfEmployeeDep employeeDep = null;
		List<LfEmployeeDep> employeeDeps = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			for (int i=0;i<ids.length-1;i++) {
				if(!"".equals(ids[i])){
					employeeDep = baseBiz.getById(LfEmployeeDep.class, ids[i]);
					conditionMap.put("deppath&like2",employeeDep.getDeppath());
					employeeDeps = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
					if(employeeDeps!=null && employeeDeps.size()>0){
						List<String> list = new ArrayList<String>();
						for (LfEmployeeDep lfEmployeeDep : employeeDeps) {
							list.add(lfEmployeeDep.getDepId().toString());
						}
						for (int j=i+1;j<ids.length;j++) {
							if(list.contains(ids[j])){
								ids[j] = "";
							}
						}
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "过滤员工机构发生异常！");
		}
		return ids;
	}
	/**
	 * 过滤客户机构
	 * @param depIds
	 * @return
	 */
	private String[] getClientDepIds(String depIds)
	{
		String[] ids = depIds.split(",");
		LfClientDep clientDep = null;
		List<LfClientDep> clientDeps = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			for (int i=0;i<ids.length-1;i++) {
				if(!"".equals(ids[i])){
					clientDep = baseBiz.getById(LfClientDep.class, ids[i]);
					conditionMap.put("deppath&like2",clientDep.getDeppath());
					clientDeps = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
					if(clientDeps!=null && clientDeps.size()>0){
						List<String> list = new ArrayList<String>();
						for (LfClientDep lfclientDep : clientDeps) {
							list.add(lfclientDep.getDepId().toString());
						}
						for (int j=i+1;j<ids.length;j++) {
							if(list.contains(ids[j])){
								ids[j] = "";
							}
						}
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"过滤客户机构发生异常！");
		}
		return ids;
	}
	
	

	
	/*
	@SuppressWarnings("unchecked")
	public void getEmployee(HttpServletRequest request,
			HttpServletResponse response) {
		String guid = request.getParameter("guid");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			if(guid == null || "".equals(guid)){
				writer.print("error");
			}else{
				
				LfEmployee employee = baseBiz.getByGuId(LfEmployee.class, Long.parseLong(guid));
				if(employee != null){
					JSONObject object = new JSONObject();
					LfEmployeeDep dep = baseBiz.getById(LfEmployeeDep.class, employee.getDepId());
					object.put("employeeNo", employee.getEmployeeNo());
					object.put("depId", employee.getDepId());
					object.put("depName", dep.getDepName());
					writer.print(object.toString());
				}else{
					writer.print("");
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e);
			writer.print("error");
		}
	}*/
	
	/**
	 * 获取操作员名称
	 * @description    
	 * @param request
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-4-2 上午09:17:49
	 */
	public String getOpUser(HttpServletRequest request){
		String opUser = "";
		try {
			LfSysuser sysuser = (LfSysuser)  request.getSession(false).getAttribute("loginSysuser");
			if(sysuser != null){
				opUser = sysuser.getUserName();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "session获取操作员名称异常，session为空！");
		}
		return opUser;
	}
	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		LfSysuser lfSysuser = null;
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "记录操作日志失败！session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}
	}	
	
	// EMP5.7新需求：增加对操作员充值和回收权限   by pengj

	/**
	 *   获取机构树
	 * @param request
	 * @param response
	 */
	/*	
	public void getDepCheck(HttpServletRequest request, HttpServletResponse response)
	{
		String checkedIds = request.getParameter("checkedIds");
		List<String> ids = null;
		if(checkedIds != null && !"".equals(checkedIds)){
			ids = new ArrayList<String>();
			String[] chId = checkedIds.split(",");
			for (String str : chId) {
				ids.add(str);
			}
		}
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			LfSysuser lfSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(lfSysuser == null){return;}
			
			//机构id
			Long depId = null;
			String depStr = request.getParameter("depId");
			//获取当前登录账号的userid
			String userIdStr = request.getParameter("lguserid");
			Long userId = Long.parseLong(userIdStr);
			
			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			
			
			StringBuffer tree = null;
			LfSysuser sysuser = null;
			
			sysuser = baseBiz.getById(LfSysuser.class, userId);
			if(sysuser != null && sysuser.getPermissionType()!=1)
			{
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps = new ArrayList<LfDep>();
				//第一次进入depId(机构Id)为空则获取当前操作员所在机构下的所有子机构
				if(depId == null){
					List<LfDep> list = depBiz.getAllDeps(userId);
					if(list!=null && list.size()>0)
					{
						lfDeps.add(list.get(0));
					}
					//LfDep lfDep = depBiz.getAllDeps(userId).get(0);//这里需要优化
					//lfDeps.add(lfDep);
					//lfDeps.addAll(new DepBiz().getDepsByDepSuperId(lfDep.getDepId()));
				//非首次进入，根据depId(机构Id)获取本机构下的所有子机构
				}else{
					lfDeps = depBiz.getDepsByDepSuperId(depId);
				}
				
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				if(lfDeps != null)
				{
					for (int i = 0; i < lfDeps.size(); i++) {
						lfDep = lfDeps.get(i);
						String path = lfDep.getDeppath();
						if(path == null){path = "";}
						if(!path.startsWith("/")){path = "/"+path;}
						tree.append("{");
						tree.append("id:").append(lfDep.getDepId());
						tree.append(",name:'").append(lfDep.getDepName()).append("'");
						tree.append(",pId:").append(lfDep.getSuperiorId());
						tree.append(",depId:'").append(lfDep.getDepId()+"'");
						tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
						tree.append(",dpath:'").append(path+"'");
						tree.append(",isParent:").append(true);
						if(ids != null && ids.contains(lfDep.getDepId().toString())){
							tree.append(",checked:").append(true);
						}
						tree.append("}");
						tree.append(",");
					}
					if(tree.length() >1)
					{
						tree = tree.deleteCharAt(tree.length()-1);
					}
				}
				tree.append("]");
				
		
			}else{
				tree = new StringBuffer("[]");
			}
			writer.print(tree.toString());
		} catch (Exception e) {
			//EmpExecutionContext.error(e,"操作员机构获取机构出现异常！userId:"+userId+" depId:"+depId);
			writer.print("");
			EmpExecutionContext.error(e,"获取机构树失败！");
		}
			
		
	}
*/	

	// end
	
	
	// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
	/**
	 * 获取操作员userid
	 * @description    
	 * @param request
	 * @return       			 
	 * @author pengj
	 * @datetime 2015-11-22 上午09:00:00
	 */
	public Long getOpUserId(HttpServletRequest request){
		Long opUserId = 0L;
		try {
			LfSysuser sysuser = (LfSysuser)  request.getSession(false).getAttribute("loginSysuser");
			if(sysuser != null){
				opUserId = sysuser.getUserId();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "session获取操作员userid异常，session为空！");
		}
		return opUserId;
	}
	
	
	/**
	 * 过滤充值回收权限机构，过滤掉重复机构id的机构（该id包含自定义机构的id和默认权限的id），评审后决定不使用
	 * @param depIds
	 * @return      			 
	 * @author pengj
	 * @datetime 2015-11-25 上午15:00:00
	 */
	/*
	private String[] getBalanceDepIds1(String depIds, String firstLevelChildDepIds)
	{
		String balanceDepIds = null;
		String[] balanceDepIdsArray = null;
		String[] ids = null;
		String[] firstLevelDepIds = null;
		if(depIds != null && !"".equals(depIds.trim())){
			ids = depIds.split(",");
		}
		if(firstLevelChildDepIds != null && !"".equals(firstLevelChildDepIds.trim())){
			firstLevelDepIds = firstLevelChildDepIds.split(",");
		}
		
		//用于过滤的list容器
		List<String> depIdList = new ArrayList<String>();
		try{
			//过滤重复元素
			//过滤机构树中选取的机构id
			if(ids != null){
				for(int i=0; i<ids.length; i++){
					if(!depIdList.contains(ids[i])){
						depIdList.add(ids[i]);
					}
				}
			}
			
			//过滤所属机构第一级子机构的机构id
			if(firstLevelDepIds != null){
				for(int i=0; i<firstLevelDepIds.length; i++){
					if(!depIdList.contains(firstLevelDepIds[i])){
						depIdList.add(firstLevelDepIds[i]);
					}
				}
			}
			
			if(depIdList != null && depIdList.size()>0){
				balanceDepIds = depIdList.toString();
				balanceDepIds = balanceDepIds.replace("[","");
				balanceDepIds = balanceDepIds.replace("]","");
				balanceDepIds = balanceDepIds.replace(" ","");
				//System.out.println("---------过滤后的充值权限机构字符串----------"+balanceDepIds);
				balanceDepIdsArray = balanceDepIds.split(",");
			}
			
			
			
		}catch (Exception e) {
			EmpExecutionContext.error(e,"过滤充值回收机构发生异常！");
		}
		return balanceDepIdsArray;
	}
	*/

	/**
	 * 过滤充值回收权限机构，过滤掉重复机构id的机构（该id仅为自定义充值回收权限的机构id）
	 * @param depIds
	 * @return      			 
	 * @author pengj
	 * @datetime 2015-11-25 上午15:00:00
	 */
	
	private String[] getBalanceDepIds(String depIds)
	{
		String balanceDepIds = null;
		String[] balanceDepIdsArray = null;
		String[] ids = null;
		if(depIds != null && !"".equals(depIds.trim())){
			ids = depIds.split(",");
		}
		
		//用于过滤的list容器
		List<String> depIdList = new ArrayList<String>();
		try{
			//过滤重复元素
			//过滤机构树中选取的机构id
			if(ids != null){
				for(int i=0; i<ids.length; i++){
					if(!depIdList.contains(ids[i])){
						depIdList.add(ids[i]);
					}
				}
			}
			
			
			if(depIdList != null && depIdList.size()>0){
				balanceDepIds = depIdList.toString();
				balanceDepIds = balanceDepIds.replace("[","");
				balanceDepIds = balanceDepIds.replace("]","");
				balanceDepIds = balanceDepIds.replace(" ","");
				//System.out.println("---------过滤后的充值权限机构字符串----------"+balanceDepIds);
				balanceDepIdsArray = balanceDepIds.split(",");
			}
			
			
			
		}catch (Exception e) {
			EmpExecutionContext.error(e,"过滤充值回收机构发生异常！");
		}
		return balanceDepIdsArray;
	}
	
	
	
	/**
	 *   获取充值回收权限机构的树，只限查子级
	 * @param request
	 * @param response
	 */
	public void getBalanceDepCheck(HttpServletRequest request, HttpServletResponse response)
	{
		
		//新增 所属机构默认子机构全部不可编辑 pengj
		//获取添加操作员页面，选择的操作员所属机构id
		String depIdByOperator = request.getParameter("depIdByOperator");
		//通过操作员所属机构id查出该机构所有第一级子机构ids
		List<String> firstLevelChildDepIdsList = null;
		if(depIdByOperator != null){
			String firstLevelChildDepIds = new SysuserPriBiz().getFirstLevelChildDepIds(Long.valueOf(depIdByOperator));
			if(firstLevelChildDepIds != null && !"".equals(firstLevelChildDepIds)){
				firstLevelChildDepIdsList = new ArrayList<String>();
				String[] firstLevelChildDepIdsArray = firstLevelChildDepIds.split(",");
				for (String firstChildDepId : firstLevelChildDepIdsArray) {
					firstLevelChildDepIdsList.add(firstChildDepId);
				}
			}
		}
		
		//---------------------------------------
		
		String checkedIds = request.getParameter("checkedIds");
		List<String> ids = null;
		if(checkedIds != null && !"".equals(checkedIds)){
			ids = new ArrayList<String>();
			String[] chId = checkedIds.split(",");
			for (String str : chId) {
				ids.add(str);
			}
		}
		StringBuffer tree = null;
		PrintWriter writer = null;
		try
		{
			Long depId = null;
			writer = response.getWriter();
			String depStr = request.getParameter("depId");
			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			LfSysuser lfSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(lfSysuser == null){
				return;
			}
			if(lfSysuser != null && lfSysuser.getPermissionType()!=1)
			{
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps = new ArrayList<LfDep>();
				//第一次进入depId(机构Id)为空则获取当前操作员所在机构下的所有子机构
				if(depId == null){
					//List<LfDep> list = depBiz.getAllDeps(lfSysuser.getUserId());
					List<LfDep> list = depBiz.getAllDepByUserIdAndCorpCode(lfSysuser.getUserId(),lfSysuser.getCorpCode());
					if(list!=null && list.size()>0)
					{
						lfDeps.add(list.get(0));
					}
					//LfDep lfDep = depBiz.getAllDeps(userId).get(0);//这里需要优化
					//lfDeps.add(lfDep);
					//lfDeps.addAll(new DepBiz().getDepsByDepSuperId(lfDep.getDepId()));
				//非首次进入，根据depId(机构Id)获取本机构下的所有子机构
				}else{
					//lfDeps = depBiz.getDepsByDepSuperId(depId);
					lfDeps = depBiz.getDepsByDepSuperIdAndCorpCode(depId,lfSysuser.getCorpCode());
				}
			
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				if(lfDeps != null)
				{
					for (int i = 0; i < lfDeps.size(); i++) {
						lfDep = lfDeps.get(i);
						String path = lfDep.getDeppath();
						if(path == null){path = "";}
						if(!path.startsWith("/")){path = "/"+path;}
						tree.append("{");
						tree.append("id:").append(lfDep.getDepId());
						tree.append(",name:'").append(lfDep.getDepName()).append("'");
						tree.append(",pId:").append(lfDep.getSuperiorId());
						tree.append(",depId:'").append(lfDep.getDepId()+"'");
						tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
						tree.append(",dpath:'").append(path+"'");
						tree.append(",isParent:").append(true);
						if((ids != null && ids.contains(lfDep.getDepId().toString())) || (firstLevelChildDepIdsList != null && firstLevelChildDepIdsList.contains(lfDep.getDepId().toString()))){
							tree.append(",checked:").append(true);
						}
						/*
						if((firstLevelChildDepIdsList != null && firstLevelChildDepIdsList.contains(lfDep.getDepId().toString()))){
							tree.append(",canEdit:").append(false);
						}else{
							tree.append(",canEdit:").append(true);
						}
						*/
						if((firstLevelChildDepIdsList != null && firstLevelChildDepIdsList.contains(lfDep.getDepId().toString()))){
							tree.append(",nocheck:").append(true);
						}
						tree.append("}");
						tree.append(",");
					}
					if(tree.length() >1)
					{
						tree = tree.deleteCharAt(tree.length()-1);
					}
				}
				tree.append("]");
		
			}else{
				tree = new StringBuffer("[]");
			}
			writer.print(tree.toString());
		}catch (Exception e)
		{
			writer.print("");
			EmpExecutionContext.error(e,"获取充值回收权限机构树失败！");
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
				EmpExecutionContext.error("从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "从session获取加密对象异常。");
			return null;
		}
	}
	
}
