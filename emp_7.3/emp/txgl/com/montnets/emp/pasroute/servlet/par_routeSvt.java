package com.montnets.emp.pasroute.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.pasgroup.biz.UserDataBiz;
import com.montnets.emp.pasroute.biz.PasRouteBiz;
import com.montnets.emp.pasroute.dao.PasRouteDao;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.LfSpFee;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.servmodule.txgl.entity.XtGateQueue;
import com.montnets.emp.servmodule.txgl.table.TableAgwAccount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 账户通道配置
 * @project p_txgl
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-3-3 上午11:22:14
 * @description
 */
@SuppressWarnings("serial")
public class par_routeSvt extends BaseServlet {
	final ErrorLoger errorLoger = new ErrorLoger();
	//List<GtPortUsed> routeList = null;

	//操作模块
	final String opModule = StaticValue.GATE_CONFIG;
	//操作用户
	final String opSper = StaticValue.OPSPER;

	//private WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
	private final PasRouteBiz pasRouteBiz = new PasRouteBiz();
	private final UserDataBiz userDataBiz = new UserDataBiz();

	final int corpType = StaticValue.getCORPTYPE();
	
	private final String empRoot = "txgl";
	private final String basePath = "/pasroute";

	/**
	 * 账户通道配置查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		long stime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter;
		BaseBiz baseBiz = new BaseBiz();
		try {
			List<GtPortUsed> routeList = null;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap1 = new LinkedHashMap<String, String>();
			orderbyMap1.put("userId", StaticValue.ASC);
			orderbyMap1.put("spisuncm", StaticValue.ASC);
			orderbyMap1.put("id", StaticValue.DESC);
			String spgatetype=null;
			String accouttype=null;
			String userId = null;
			//状态
			String status= null;
			//通道号
			String spgete = null;
			//运营商类型
			String spisuncm = null;
			//路由类型
			String routeFlag = null;
			String cpno = null;
			boolean isBack = request.getParameter("isback")==null?false:true;//是否返回操作
			HttpSession session = request.getSession(false);
			if(isBack){
				pageInfo = (PageInfo) session.getAttribute("par_pageinfo");
				spgatetype = session.getAttribute("par_spgatetype")==null?"":String.valueOf(session.getAttribute("par_spgatetype"));
				accouttype = session.getAttribute("par_accouttype")==null?"":String.valueOf(session.getAttribute("par_accouttype"));
				userId = session.getAttribute("par_userId")==null?"":String.valueOf(session.getAttribute("par_userId"));
				status = session.getAttribute("par_status")==null?"":String.valueOf(session.getAttribute("par_status"));
				spgete = session.getAttribute("par_spgete")==null?"":String.valueOf(session.getAttribute("par_spgete"));
				spisuncm = session.getAttribute("par_spisuncm")==null?"":String.valueOf(session.getAttribute("par_spisuncm"));
				routeFlag = session.getAttribute("par_routeFlag")==null?"":String.valueOf(session.getAttribute("par_routeFlag"));
				cpno = session.getAttribute("par_cpno")==null?"":String.valueOf(session.getAttribute("par_cpno"));
				request.setAttribute("spgatetype", spgatetype);
				request.setAttribute("accouttype", accouttype);
				request.setAttribute("userId", userId);
				request.setAttribute("status", status);
				request.setAttribute("spgate", spgete);
				request.setAttribute("spisuncm", spisuncm);
				request.setAttribute("routeFlag", routeFlag);
				request.setAttribute("cpno", cpno);
			}else{
				isFirstEnter = pageSet(pageInfo,request);
				if (!isFirstEnter) {
					spgatetype=request.getParameter("spgatetype");
					accouttype=request.getParameter("accouttype");
					userId = request.getParameter("userId");
					status=request.getParameter("status");
					spgete = request.getParameter("spgate");
					spisuncm = request.getParameter("spisuncm");
					routeFlag = request.getParameter("routeFlag");
					cpno = request.getParameter("cpno");
				}
			}
			session.setAttribute("par_pageinfo", pageInfo);
			session.setAttribute("par_spgatetype", spgatetype);
			session.setAttribute("par_accouttype", accouttype);
			session.setAttribute("par_userId", userId);
			session.setAttribute("par_status", status);
			session.setAttribute("par_spgete", spgete);
			session.setAttribute("par_spisuncm", spisuncm);
			session.setAttribute("par_routeFlag", routeFlag);
			session.setAttribute("par_cpno", cpno);
			if(!isInvalidString(userId)){
				if(userId.matches("\\w+")){
					conditionMap.put("userId&like", userId);
				}else if(userId.matches("\\w+\\(.*\\)")){
					userId = userId.replaceAll("\\(.*\\)", "");
					conditionMap.put("userId", userId);
				}else{
					
				}
				
			}
			if(!isInvalidString(status)){
				conditionMap.put("status", status);
			}
			if(!isInvalidString(spgete)){
				if(spgete.matches("\\d+")){
					conditionMap.put("spgate&like", spgete);
				}else if(spgete.matches("\\d+\\(.*\\)")){
					spgete = spgete.replaceAll("\\(.*\\)", "");
					conditionMap.put("spgate", spgete);
				}
				
			}
			if(!isInvalidString(spisuncm)){
				conditionMap.put("spisuncm", spisuncm);
			}
			if(!isInvalidString(routeFlag)){
				conditionMap.put("routeFlag", routeFlag);
			}
			if(!isInvalidString(cpno)){
				conditionMap.put("cpno&like", cpno);
			}
			if(spgatetype!=null&&!"".equals(spgatetype)&&(accouttype==null||"".equals(accouttype))){
				conditionMap.put("gateType", spgatetype);
			}else if(accouttype!=null&&!"".equals(accouttype)&&(spgatetype==null||"".equals(spgatetype))){
				conditionMap.put("gateType", accouttype);
			}else if(spgatetype!=null&&!"".equals(spgatetype)&&accouttype!=null&&!"".equals(accouttype)&&accouttype.equals(spgatetype)){
				conditionMap.put("gateType", spgatetype);
			}else if(spgatetype!=null&&!"".equals(spgatetype)&&accouttype!=null&&!"".equals(accouttype)&&!accouttype.equals(spgatetype)){
				conditionMap.put("gateType", "-1");
			}
			//企业编码
			//String corpCode=request.getParameter("lgcorpcode");
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String corpCode = lfSysuser.getCorpCode();
			Long userid=null;
			//String userStr=request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userStr = SysuserUtil.strLguserid(request);



			if(userStr != null && !"".equals(userStr.trim())){
				userid = Long.parseLong(userStr);
			}
			
			if ("100000".equals(corpCode) || corpType == 0) {
//				routeList = baseBiz.getByCondition(GtPortUsed.class, null,
//						conditionMap, orderbyMap1, pageInfo);
				routeList = baseBiz.getByConditionNoCount(GtPortUsed.class, null,
						conditionMap, orderbyMap1, pageInfo);
			} else {
				/*routeList = new GenericEmpSpecialDAO().findSpRouteByCorp(
						corpCode, conditionMap, orderbyMap1, pageInfo);*/
				routeList = new PasRouteDao().findSpRouteByCorp(
						corpCode, conditionMap, orderbyMap1, pageInfo);
			}

			List<XtGateQueue> xtList = null;
			if ("100000".equals(corpCode) || corpType == 0) {
				LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
				orderbyMap.put("spisuncm", StaticValue.ASC);
				xtList = baseBiz.getByCondition(XtGateQueue.class, null, null,
						orderbyMap);

			} else {
				/*xtList = new GenericEmpSpecialDAO().findGateInfoByCorp(
						corpCode, null, null);*/
				xtList = new PasRouteDao().findGateInfoByCorp(
						corpCode, null, null);
			}
			request.setAttribute("mrXtList", xtList);

			conditionMap.clear();
			conditionMap.put("userType", "0");
			conditionMap.put("uid&>", "100001");
			List<Userdata> userdataList = null;
			
			//多企业的，就赋值为1，如果是单企业字符串，就赋值为0
			if(StaticValue.getCORPTYPE() ==0)
			{
				//单企业
				userdataList = baseBiz.getByCondition(Userdata.class,
						conditionMap, null);
			}else{
				//托管版
				if ("100000".equals(corpCode)) {
					userdataList = baseBiz.getByCondition(Userdata.class,
							conditionMap, null);
				} else {
					/*userdataList = new GenericEmpSpecialDAO().findSpUserByCorp(
							corpCode, conditionMap, null, null);*/
					userdataList = new PasRouteDao().findSpUserByCorp(
							corpCode, conditionMap, null, null);
				}
			}
			//加密后的集合
			Map<Long, String> keyIdMap = new HashMap<Long, String>();
			//ID加密
			if(routeList != null && routeList.size() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptToMap(routeList, "Id", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询账户通道配置列表，参数加密失败。corpCode:"+corpCode);
						throw new Exception("查询账户通道配置列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询账户通道配置列表，从session中获取加密对象为空！");
					routeList.clear();
					throw new Exception("查询账户通道配置列表，获取加密对象失败。");
				}
			}
			request.setAttribute("userdataList", userdataList);
			request.setAttribute("keyIdMap", keyIdMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("routeList", routeList);
			request.getRequestDispatcher(
					empRoot + basePath + "/par_route.jsp").forward(
					request, response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"账户通道配置","("+sDate+")查询",StaticValue.GET);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"账户通道配置查询异常！"));
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getSession(false).setAttribute("error", e);
				request.getRequestDispatcher(
						empRoot + basePath + "/par_route.jsp").forward(
						request, response);
			} catch (ServletException e1) {
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"账户通道配置查询异常！"));
			} catch (IOException e1) {
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"账户通道配置查询异常！"));
			}
		}
	}

	/**
	 * 账户通道配新建页面跳转
	 * @param request
	 * @param response
	 */
	public void toAdd(HttpServletRequest request, HttpServletResponse response) {
		BaseBiz baseBiz = new BaseBiz();
		try {
			List<XtGateQueue> xtList;
			// xtList = wgMsgBiz.getAllGates();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("status", "0");
			String corpCode=request.getParameter("lgcorpcode");
			//String userStr=request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userStr = SysuserUtil.strLguserid(request);

			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			if ("100000".equals(corpCode) || corpType == 0) {
				
				orderbyMap.put("spisuncm", StaticValue.ASC);
				xtList = baseBiz.getByCondition(XtGateQueue.class,
						conditionMap, orderbyMap);

			} else {
				/*xtList = new GenericEmpSpecialDAO().findGateInfoByCorp(
						getCorpCode(), conditionMap, null);*/
				xtList = new PasRouteDao().findGateInfoByCorp(
						corpCode, conditionMap, null);
			}

			request.setAttribute("allXtList", xtList);
			List<Userdata> userdataList = pasRouteBiz.getAllUserdata(0);
			request.setAttribute("userdataList", userdataList);

			request.getRequestDispatcher(
					empRoot + basePath + "/par_addRoute.jsp?lgcorpcode="+corpCode+"&lguserid"+userStr).forward(
					request, response);

		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"跳转页面异常"));
		}
	}
	
	/**
	 * 通道配置显示跳转
	 * @param request
	 * @param response
	 */
	public void toAddshow(HttpServletRequest request, HttpServletResponse response) {
		BaseBiz baseBiz = new BaseBiz();
		try {
			List<XtGateQueue> xtList;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("status", "0");
			String corpCode=request.getParameter("lgcorpcode");
			String keyword=request.getParameter("keyword");
			if(keyword!=null && !"".equals(keyword)){
				conditionMap.put("spgate&like", keyword);
			}
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			if ("100000".equals(corpCode) || corpType == 0) {
				
				orderbyMap.put("spisuncm", StaticValue.ASC);
				xtList = baseBiz.getByCondition(XtGateQueue.class,
						conditionMap, orderbyMap);

			} else {
				xtList = new PasRouteDao().findGateInfoByCorp(
						corpCode, conditionMap, null);
			}
			if(xtList!=null && xtList.size()>0){
				StringBuilder sb = getresult(xtList);
				response.getWriter().print(sb.toString());
			}else{
				response.getWriter().print("");
			}
			

		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通道配置显示跳转异常！"));
		}
	}
		
	private static StringBuilder getresult(List<XtGateQueue> xtList){
		StringBuilder sb = new StringBuilder();
		for(XtGateQueue xt : xtList){
			sb.append(xt.getSpgate()+"-"+xt.getId()+",");
		}
		sb.substring(0, sb.length()-1);
		return sb;
	}
	
	/**
	 * 账户通道配修改页面跳转
	 * @param request
	 * @param response
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response) {
		BaseBiz baseBiz = new BaseBiz();
		try {
			//String id = request.getParameter("id");
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
					EmpExecutionContext.error("账户通道配修改页面跳转，参数解密码失败，keyId:"+keyId);
					throw new Exception("账户通道配修改页面跳转，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("账户通道配修改页面跳转，从session中获取加密对象为空！");
				throw new Exception("账户通道配修改页面跳转，获取加密对象失败。");
			}
			//企业编码
			//String corpCode=request.getParameter("lgcorpcode");
			LfSysuser lfSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String corpCode=lfSysuser.getCorpCode();
			//当前登录操作员id
			//String userStr=request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userStr = SysuserUtil.strLguserid(request);
			GtPortUsed gtPortUsed = baseBiz.getById(GtPortUsed.class, id);
			//XtGateQueue xtGateQueue = pasRouteBiz.getGateByGateIsum(gtPortUsed
			//		.getSpgate(), gtPortUsed.getSpisuncm());
			XtGateQueue xtGateQueue = pasRouteBiz.getGateByGateIsumAndTyep(gtPortUsed.getSpgate(), gtPortUsed.getSpisuncm(),gtPortUsed.getGateType());

			request.setAttribute("gtPort", gtPortUsed);
			request.setAttribute("xtGate", xtGateQueue);
			request.getRequestDispatcher(
					empRoot + basePath + "/par_editRoute.jsp?lgcorpcode="+corpCode+"&lguserid"+userStr).forward(
					request, response);

		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"账户通道配修改页面跳转异常！"));
		}
	}

	/**
	 * 账户通道配修改
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) {
		//操作类型
		String hidOpType = request.getParameter("hidOpType");
		String gatetype = request.getParameter("gatetype");
		if(gatetype==null){
			gatetype="1";
		}
		String userid = request.getParameter("userid");
		//路由描述
		String memo = request.getParameter("memo");
		//指令代码
		String usercode = request.getParameter("usercode");
		//拓展子号
		String cpno = request.getParameter("cpno");
		GtPortUsed gt = new GtPortUsed();
		//路由标志
		gt.setRouteFlag(Integer.parseInt(request.getParameter("routeflag")));
		//企业编码
		String corpCode =request.getParameter("lgcorpcode");
		//当前登录操作员id
		//String userStr=request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userStr = SysuserUtil.strLguserid(request);
		//如果拓展子号为空，则设置默认为空格
		if (cpno == null || cpno.length() < 1) {
			cpno = " ";
		}
		gt.setCpno(cpno);
		//路由描述为空时，默认为空格
		if (memo == null || memo.length() < 1)
			memo = " ";
		gt.setMemo(memo);
		gt.setUserId(userid);
        String sign = request.getParameter("signstr");
        String ensign = request.getParameter("ensignstr");
		//新增时,时间为空,赋默认值
		if ("add".equals(hidOpType)) {
			String sendTimeBegin = request.getParameter("sendtimebegin");
			String sendTimeEnd = request.getParameter("sendtimeend");
			if(sendTimeBegin == null || "".equals(sendTimeBegin))
			{
				sendTimeBegin = "00:00:00";
			}
			if(sendTimeEnd == null || "".equals(sendTimeEnd))
			{
				sendTimeEnd = "23:59:59";
			}
			gt.setSendTimeBegin(sendTimeBegin);
			gt.setSendTimeEnd(sendTimeEnd);
		}
		//指令代码为空时，默认为空格
		if (usercode == null || usercode.length() < 1)
			usercode = " ";
		gt.setUserCode(usercode);
		BaseBiz baseBiz = new BaseBiz();
		SuperOpLog spLog = new SuperOpLog();
		String opUser = "";
		String opType = null;
		//记录日志总字符串
		StringBuffer opContent = new StringBuffer("");
		//修改前数据字符串
		StringBuffer oldStr=new StringBuffer("");
		//修改后数据字符串
		StringBuffer newStr=new StringBuffer("");
		try {
			//由于sp绑定通道账号 通道账户信息没有任何改变保存到绑定关系表中 故新增修改时英文短信信息直接从原始表获取
			String gtId = request.getParameter("gtId");
			if(gtId!=null&&!"".equals(gtId)){
				setGtPortUsed(gt, gtId,sign,ensign);
			}
			String signstr = gt.getSignstr().trim();
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, userStr);
			opUser = sysuser==null?"":sysuser.getUserName();
			String type="1".equals(gatetype)?"短信":"彩信";
			if ("add".equals(hidOpType)) {
				opType = StaticValue.ADD;
				String signstrLog= signstr!=null&&!"".equals(signstr)?signstr:"无";
				//路由状态
				gt.setStatus(Integer.parseInt(request.getParameter("status")));
				Userdata user =null;
				if("1".equals(gatetype)){
					user=userDataBiz.getSmsUserdataByUserid(gt.getUserId());
				}else{
					user=userDataBiz.getMmsUserdataByUserid(gt.getUserId());
				}
				gt.setFeeFlag(Integer.valueOf(user.getFeeFlag().toString())); // /??
				String loginid = user.getLoginId();
				gt.setLoginId(loginid);
				newStr.append(gt.getUserId()).append("，").append(gt.getStatus()).append("，").append(gt.getRouteFlag()).append("，")
				.append(gt.getSpgate()).append("，").append(gt.getCpno()).append("，").append(gt.getMemo())
				.append("，").append(gt.getUserCode());
				if (baseBiz.addObj(gt)) {
					
					//短信更新指令
					if("1".equals(gatetype)){
						pasRouteBiz.updateCmd(gt.getUserId());
					}
					
					//对应账号类型
					String platFormType = user.getSptype().toString();
					
					if (corpType == 0&& pasRouteBiz.checkRouteBysmsSpUser(userid, platFormType)&&"1".equals(gatetype)) {
						LfSpDepBind lsdb = new LfSpDepBind();
						lsdb = new LfSpDepBind();
						lsdb.setCorpCode(corpCode);
						lsdb.setBindType(0);
						lsdb.setPlatFormType(Integer.parseInt(platFormType));
						lsdb.setSpUser(userid);
						lsdb.setIsValidate(1);

						baseBiz.addObj(lsdb);
					}
					if (corpType == 0&& pasRouteBiz.checkRouteByMmsSpUser(userid)&&"2".equals(gatetype)) {
						LfMmsAccbind lsdb = new LfMmsAccbind();
						lsdb = new LfMmsAccbind();
						lsdb.setCorpCode(corpCode);
						lsdb.setMmsUser(userid);
						lsdb.setCreateTime(new Timestamp(System.currentTimeMillis()));
						lsdb.setIsValidate(1);
						lsdb.setUserId(Long.parseLong(userStr));
						baseBiz.addObj(lsdb);
					}
					
					//托管版设置运营商余额地址
					if(corpType==1){
						List<DynaBean> feeUrlBeans=new PasRouteDao().getFeeUrlByGate(gt.getSpgate(), gt.getSpisuncm(), gatetype);
						if(feeUrlBeans != null && feeUrlBeans.size() > 0)
						{
							for(DynaBean feeUrlBean : feeUrlBeans)
							{
								String feeUrlStr=null;
								Object feeUrl=feeUrlBean.get(TableAgwAccount.FEEURL.toLowerCase());
								if(feeUrl!=null){
									feeUrlStr=(String)feeUrl;
									LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
									conditionMap.put("spUser", userid);
									conditionMap.put("accountType", gatetype);
									List<LfSpFee> spfeeList = new BaseBiz().getByCondition(LfSpFee.class, conditionMap, null);
									if(spfeeList!=null && spfeeList.size()>0)
									{
										LfSpFee lfSpFee = spfeeList.get(0);
										lfSpFee.setSpFeeUrl(feeUrlStr);
										 new BaseBiz().updateObj(lfSpFee);
									}
								}
							}
						}
					}
					
					request.setAttribute("w_routeResult", "1");
					opContent.append("新增"+type+"账户通道配置成功[SP账号，路由状态，路由类型，通道号码，子号设定，路由描述，指令代码]")
					.append("（").append(newStr).append("）");
					//增加操作日志
					setLog(request, "账户通道配置", opContent.toString(), opType);
					spLog.logSuccessString(opUser, opModule, opType, opContent.toString(),
							corpCode);

				} else {
					request.setAttribute("w_routeResult", "0");
					opContent.append("新增"+type+"账户通道配置失败[SP账号，路由状态，路由类型，通道号码，子号设定，路由描述，指令代码]")
					.append("（").append(newStr).append("）");
					//增加操作日志
					setLog(request, "账户通道配置", opContent.toString(), opType);
					spLog.logFailureString(opUser, opModule, opType, opContent
							+ opSper, null, corpCode);
//					EmpExecutionContext.error("企业：" +corpCode+",操作员：" +opUser+"新增账户通道配置（账户：" + gt.getUserId() + ",通道号："
//							+ gt.getSpgate()+",短信签名："+ signstrLog +"）绑定失败");
				}

			} else if ("edit".equals(hidOpType)) {
				String oldSignstr=request.getParameter("oldSignstr");
				opType = StaticValue.UPDATE;
				String userId = request.getParameter("userId");
				String signstrLog=",原短信签名："+oldSignstr+"修改后短信签名："+(signstr!=null&&!"".equals(signstr)?signstr:"无")+"）";
				//Long id = Long.valueOf(request.getParameter("id"));
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
						EmpExecutionContext.error("修改短彩sp账户，参数解密码失败，keyId:"+keyId);
						throw new Exception("修改短彩sp账户，参数解密码失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("修改短彩sp账户，从session中获取加密对象为空！");
					throw new Exception("修改短彩sp账户，获取加密对象失败。");
				}
				String loginId = request.getParameter("loginId");
				//路由状态
				String status=request.getParameter("status");
				// Integer multilen1 =
				// Integer.valueOf(request.getParameter("multilen1"));
				Integer feeFlag = Integer.valueOf(request
						.getParameter("feeFlag"));

				gt.setId(Long.valueOf(id));
				gt.setLoginId(loginId);
				
				gt.setStatus(status!=null?Integer.parseInt(status):0);

				gt.setFeeFlag(feeFlag);
				//增加操作日志
				//修改前
				GtPortUsed oldgpu=baseBiz.getById(GtPortUsed.class, gt.getId());
				if(oldgpu!=null){
					oldStr.append(oldgpu.getUserId()).append("，").append(oldgpu.getStatus()).append("，").append(oldgpu.getRouteFlag()).append("，")
					.append(oldgpu.getSpgate()).append("，").append(oldgpu.getCpno()).append("，").append(oldgpu.getMemo())
					.append("，").append(oldgpu.getUserCode());
				}
				//修改后
				newStr.append(oldgpu.getUserId()).append("，").append(gt.getStatus()).append("，").append(gt.getRouteFlag()).append("，")
				.append(gt.getSpgate()).append("，").append(gt.getCpno()).append("，").append(gt.getMemo())
				.append("，").append(gt.getUserCode());
				if (baseBiz.updateObj(gt)) {
					request.setAttribute("w_routeResult", "2");
					//短信更新指令
					if("1".equals(gatetype)){
						pasRouteBiz.updateCmd(userId);
					}
					opContent.append("修改"+type+"账户通道配置成功[SP账号，路由状态，路由类型，通道号码，子号设定，路由描述，指令代码]（")
					.append(oldStr).append("-->").append(newStr).append("）");
					setLog(request, "账户通道配置", opContent.toString(), opType);
					spLog.logSuccessString(opUser, opModule, opType, opContent.toString(),corpCode);
				} else {
					request.setAttribute("w_routeResult", "0");
					opContent.append("修改"+type+"账户通道配置失败[SP账号，路由状态，路由类型，通道号码，子号设定，路由描述，指令代码]（")
					.append(oldStr).append("-->").append(newStr).append("）");
					setLog(request, "账户通道配置", opContent.toString(), opType);
					spLog.logFailureString(opUser, opModule, opType, opContent
							+ opSper, null, corpCode);
					//EmpExecutionContext.error("企业：" +corpCode+",操作员：" +opUser+"修改账户通道配置（账户：" + userId + ",通道号：" + gt.getSpgate()+"失败");
				}
			}
			if ("edit".equals(hidOpType)) {
				toEdit(request, response);
			} else {
				toAdd(request, response);
			}
		} catch (Exception ex) {
			EmpExecutionContext.error(errorLoger.getErrorLog(ex,"企业：" +corpCode+",操作员：" +opUser+"账户通道配修改异常！"));
			try
			{
				request.getSession(false).setAttribute("w_routeResult", "0");
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "账户通道配置，设置session失败！企业：" +corpCode+",操作员：" +opUser);
			}
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, ex, corpCode);
		}

	}

	/**
	 * 删除账户通道配
	 * @param request
	 * @param response
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("ids");
		String gatetype = request.getParameter("gatetype");
		//企业编码
		String corpCode=request.getParameter("lgcorpcode");
		//当前登录操作员id
		//String userStr=request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userStr = SysuserUtil.strLguserid(request);
		String opContent = "删除账户通道配置";
		SuperOpLog spLog = new SuperOpLog();

		BaseBiz baseBiz = new BaseBiz();
		String opUser = "";
		PrintWriter writer = null;
		try {
			String message="";
			List<GtPortUsed> gpus = null;
			if(ids!=null&&ids.length()>0){
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt == null)
				{
					EmpExecutionContext.error("删除彩短黑名单，从session中获取加密对象为空！");
					throw new Exception("删除彩信黑名单信息，获取加密对象失败。");
				}
				//批量删除
				if(ids.indexOf(",") >= 0)
				{
					//拆分出来进行单个解密再拼接查询条件
					String[]kwIdClump = ids.split(",");
					StringBuffer wdSb = new StringBuffer();
					for(int i=0; i<kwIdClump.length; i++)
					{
						wdSb.append(encryptOrDecrypt.decrypt(kwIdClump[i])).append(",");
					}
					wdSb.deleteCharAt(wdSb.lastIndexOf(","));
					ids = wdSb.toString();
				}
				//单个删除
				else
				{
					//解密
					ids = encryptOrDecrypt.decrypt(ids);
				}
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("id&"+StaticValue.IN, ids);
				gpus=baseBiz.getByCondition(GtPortUsed.class, conditionMap, null);
				GtPortUsed gpu=null;
				String yys="";
				for (int i = 0; i < gpus.size(); i++)
				{
					gpu=gpus.get(i);
					if(gpu.getSpisuncm()==0){
						yys="移动";
					}else if(gpu.getSpisuncm()==1){
						yys="联通";
					}else if(gpu.getSpisuncm()==21){
						yys="电信";
					}else{
						yys="国外";
					}
					message+="账户："+gpu.getUserId()+"，通道号："+gpu.getSpgate()+"，运营商："+yys+"；";
				}
				if(gpus.size()>0){
					message=message.substring(0, message.length()-1);
					opContent+="（"+message+"）";
				}
			}
			
			
			writer = response.getWriter();
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, userStr);
			opUser = sysuser==null?"":sysuser.getUserName();
			Integer count = 0;
			if (corpType == 0) {
				//单企业
				count = pasRouteBiz.deleteRoute(ids,gatetype);
			} else {
				//多企业
				count = baseBiz.deleteByIds(GtPortUsed.class, ids);
			}

			if (count != null && count - 0 > 0) {
				//短信更新指令
				pasRouteBiz.updateCmd(gpus);
				
				//添加成功操作日志
				spLog.logSuccessString(opUser, opModule, StaticValue.DELETE, opContent,
						corpCode);
				//EmpExecutionContext.error("企业：" +corpCode+",操作员：" +opUser+opContent+"成功");
				//增加操作日志
				setLog(request, "账户通道配置", opContent+"成功", StaticValue.DELETE);
				writer.print(count);
			} else {
				//添加失败操作日志
				spLog.logFailureString(opUser, opModule, StaticValue.DELETE, opContent
						+ opSper, null, corpCode);
				//EmpExecutionContext.error("企业：" +corpCode+",操作员：" +opUser+opContent+"失败");
				setLog(request, "账户通道配置", opContent+"失败", StaticValue.DELETE);
				writer.print(0);
			}
		} catch (Exception e) {
			if(writer!=null){
				writer.print(0);
			}
			//添加失败操作日志
			spLog.logFailureString(opUser, opModule, StaticValue.DELETE,
					opContent + opSper, e, corpCode);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"企业：" +corpCode+",操作员：" +opUser+"删除账户通道配异常！"));
		}
	}

	/**
	 * 检查通道配置
	 * @param request
	 * @param response
	 */
	public void checkRoute(HttpServletRequest request,
			HttpServletResponse response) {
		String userid = request.getParameter("userid");
		//操作类型
		String opType = request.getParameter("opType");
		//通道号
		String spgate = request.getParameter("spgate");
		//运营商类型
		String spisuncm = request.getParameter("spisuncm");
		//路由标志
		String routeflag = request.getParameter("routeflag");
		//通道号id
		String spisuncmId = request.getParameter("spisuncmId");
		
		String gatetype = request.getParameter("gatetype");
		if(gatetype==null){
			gatetype="1";
		}
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			if ("checkRoute".equals(opType)) {
				writer.print(pasRouteBiz.isRoutExists(userid, spgate, spisuncm,
						routeflag,gatetype));
			} else if ("check".equals(opType)) {
				String cpno = request.getParameter("cpno");
				//如果拓展子号为空，则默认为空格
				if (cpno == null || cpno.length() < 1) {
					cpno = " ";
				}
				String result = "";

				String userType = request.getParameter("userType");
				if ("1".equals(userType)) {
					result = "spUserBindExists";
				}
				// 新增下行路由
				if (routeflag.equals("1")) {
					if (pasRouteBiz.isDownRoutExists(userid, spisuncm, "1",gatetype)) {
						result = "downExists";
					} else if (pasRouteBiz.isDownRoutExists(userid, spisuncm, "0",gatetype)) {
						result = "downInUpExists";
					}
					// 新增上下行路由
				} else if (routeflag.equals("0")) {
					if (pasRouteBiz.isDownRoutExists(userid, spisuncm, "0",gatetype)) {
						result = "upDownSpisuncmExists";
					} else if (pasRouteBiz.isUpRoutExists(userid, spgate,
							spisuncm, "2",gatetype)) {
						result = "allUpExists";
					} else if (pasRouteBiz.isDownRoutExists(userid, spisuncm, "1",gatetype)) {
						result = "allDownExists";
					} else if (pasRouteBiz.isGateUpRoutExists(spgate, spisuncm,
							cpno, "0",gatetype)) {
						result = "allUpDownExists";
					} else if (pasRouteBiz.isGateUpRoutExists(spgate, spisuncm,
							cpno, "2",gatetype)) {
						result = "allupGateExists1";
					} else if (pasRouteBiz.isCpnoContains(spgate, Integer
							.valueOf(spisuncm), cpno,gatetype)) {
						result = "cpnoContain";
					}
					// 新增上行路由
				} else if (routeflag.equals("2")) {
					if (pasRouteBiz.isUpRoutExists(userid, spgate, spisuncm, "0",gatetype)) {
						result = "upGateExists1";
					} else if (pasRouteBiz.isUpRoutExists(userid, spgate,
							spisuncm, "2",gatetype)) {
						result = "upExists";
					} else if (pasRouteBiz.isGateUpRoutExists(spgate, spisuncm,
							cpno, "0",gatetype)) {
						result = "allupGateExists";
					} else if (pasRouteBiz.isGateUpRoutExists(spgate, spisuncm,
							cpno, "2",gatetype)) {
						result = "upGateExists";
					} else if (pasRouteBiz.isCpnoContains(spgate, Integer
							.valueOf(spisuncm), cpno,gatetype)) {
						result = "cpnoContain";
					}
				}
				
				// 如果是单企业就判断能通道号+子号+尾号是否大于20
				if (StaticValue.getCORPTYPE() == 0) {
					// 获取当前企业的尾号位数
					LfCorp lfCorp = (LfCorp) request.getSession(false).getAttribute(
							"loginCorp");
					if (cpno.length() + spgate.length() + lfCorp.getSubnoDigit() > 20) {
						result += ",OutOfLength";
						result +=","+lfCorp.getSubnoDigit();
					}
				}
				writer.print(result);
			} else if ("checkDownExists".equals(opType)) {
				writer.print(pasRouteBiz.isDownRoutExists(userid, spisuncm,
						routeflag,gatetype));
			} else if ("isBindSp".equals(opType)) {
				String res = null;
				if (spisuncmId != null && !"".equals(spisuncmId)
						&& spgate != null && !"".equals(spgate)) {
					res = pasRouteBiz.isSpgateBandExists(spgate, Integer
							.parseInt(spisuncmId),gatetype);
				}
				writer.print(res);
				return;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"检查通道配置异常！"));
			writer.print("error");
		}
	}

	/**
	 * 检查路由
	 * @param request
	 * @param response
	 */
	public void checkUpdateRoute(HttpServletRequest request,
			HttpServletResponse response) {
		String userid = request.getParameter("userid");
		//操作类型
		String opType = request.getParameter("opType");
		//通道号
		String spgate = request.getParameter("spgate");
		//运营商类型
		String spisuncm = request.getParameter("spisuncm");
		//路由标志
		String routeflag = request.getParameter("routeflag");
		//通道号id
		String spisuncmId = request.getParameter("spisuncmId");

		String gatetype = request.getParameter("gatetype");
		if(gatetype==null){
			gatetype="1";
		}
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			if ("checkRoute".equals(opType)) {
				//检查该路由是否存在
				writer.print(pasRouteBiz.isRoutExists(userid, spgate, spisuncm,
						routeflag,gatetype));
			} else if ("check".equals(opType)) {
				String cpno = request.getParameter("cpno");
				//如果拓展子号为空，则默认为空格
				if (cpno == null || cpno.length() < 1) {
					cpno = " ";
				}
				String result = "";
				String userType = request.getParameter("userType");
				if ("1".equals(userType)) {
					result = "spUserBindExists";
				}

				// 修改上下行路由
				if (routeflag.equals("0")) {
					if (pasRouteBiz.isUpdateUpRoutExists(userid, spgate, spisuncm,
							cpno, routeflag,gatetype)) {
						result = "allUpDownExists";
					} else if (pasRouteBiz.isUpdateUpRoutExists(userid, spgate,
							spisuncm, cpno, "2",gatetype)) {
						result = "allupGateExists1";
					} else if (pasRouteBiz.isCpnoContains(spgate, Integer
							.valueOf(spisuncm), cpno, userid,gatetype)) {
						result = "cpnoContain";
					}
					// 修改上行路由
				} else if (routeflag.equals("2")) {
					if (pasRouteBiz.isUpdateUpRoutExists(userid, spgate, spisuncm,
							cpno, "0",gatetype)) {
						result = "allupGateExists";
					} else if (pasRouteBiz.isUpdateUpRoutExists(userid, spgate,
							spisuncm, cpno, routeflag,gatetype)) {
						result = "upGateExists";
					} else if (pasRouteBiz.isCpnoContains(spgate, Integer
							.valueOf(spisuncm), cpno, userid,gatetype)) {
						result = "cpnoContain";
					}
				}
				// 如果是单企业就判断能通道号+子号+尾号是否大于20
				if (StaticValue.getCORPTYPE() == 0) {
					// 获取当前企业的尾号位数
					LfCorp lfCorp = (LfCorp) request.getSession(false).getAttribute(
							"loginCorp");
					if (cpno.length() + spgate.length() + lfCorp.getSubnoDigit() > 20) {
						result += ",OutOfLength";
						result +=","+lfCorp.getSubnoDigit();
					}
				}
				response.getWriter().print(result);
			} else if ("checkDownExists".equals(opType)) {
				//检查是否存在下行路由
				response.getWriter().print(pasRouteBiz.isDownRoutExists(userid, spisuncm,routeflag,gatetype));
			} else if ("isBindSp".equals(opType)) {
				String res = null;
				if (spisuncmId != null && !"".equals(spisuncmId)
						&& spgate != null && !"".equals(spgate)) {
					res = pasRouteBiz.isSpgateBandExists(spgate, Integer
							.parseInt(spisuncmId),gatetype);
				}
				response.getWriter().print(res);
				return;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"检查路由异常！"));
			writer.print("error");
		}
	}
	
	
	
	/**
	 * 路由状态修改
	 * @param request
	 * @param response
	 */
	public void ChangeSate(HttpServletRequest request, HttpServletResponse response)
	{
		//企业编码
		String corpcode = request.getParameter("lgcorpcode");
		String lgusername = request.getParameter("lgusername");
		BaseBiz baseBiz = new BaseBiz();
		SuperOpLog spLog = new SuperOpLog();
		String opUser =  lgusername==null?"":lgusername;
		PrintWriter writer = null;
		String opContent = null;
		try {
			writer = response.getWriter();
			//获取SP账户ID
			//Long id=Long.valueOf(request.getParameter("id"));
			String id;
			//加密ID
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
					EmpExecutionContext.error("修改账户通道配状态，参数解密码失败，keyId:"+keyId);
					throw new Exception("修改账户通道配状态，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("修改账户通道配状态，从session中获取加密对象为空！");
				throw new Exception("修改账户通道配状态，获取加密对象失败。");
			}
			//SP账户状态
			String status = request.getParameter("status");			
			GtPortUsed gtu = baseBiz.getById(GtPortUsed.class, Long.parseLong(id));
			gtu.setStatus(Integer.valueOf(status));
			String type=gtu.getGateType()==1?"短信":"彩信";
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			if(status.equals("0"))
			{
				if (corpType == 0) {
					//单企业

						if("2".equals(gtu.getGateType().toString())){
							conditionMap.put("mmsUser", gtu.getUserId());
							objectMap.put("isValidate", "1");
							baseBiz.update(LfMmsAccbind.class, objectMap,	conditionMap);
							conditionMap.clear();
							objectMap.clear();
					}
				}
				opContent="修改"+type+"SP账户：("+gtu.getUserId()+")与通道号：("+gtu.getSpgate()+")的路由状态为启用";
			}
			else
			{
					if (corpType == 0) {
						//单企业
						conditionMap.put("userId", gtu.getUserId());
						conditionMap.put("gateType", gtu.getGateType().toString());
						conditionMap.put("status", "0");
						List<GtPortUsed> gtportuserds = baseBiz.getByCondition(GtPortUsed.class, conditionMap, null);
						conditionMap.clear();
						if (gtportuserds.size() == 1)
						{
							if("2".equals(gtu.getGateType().toString())){
								conditionMap.put("mmsUser", gtu.getUserId());
								objectMap.put("isValidate", "0");
								baseBiz.update(LfMmsAccbind.class, objectMap,	conditionMap);
								conditionMap.clear();
								objectMap.clear();
							}
						}
					}

				opContent="修改"+type+"SP账户：("+gtu.getUserId()+")与通道号：("+gtu.getSpgate()+")的路由状态为禁用";
			}			
           
			//调用修改路由对象的方法，并返回结果
			boolean gtresult = baseBiz.updateObj(gtu);
			if(gtresult)
			{
				response.getWriter().print("true");
				//保存日志
				spLog.logSuccessString(opUser, opModule, StaticValue.UPDATE, opContent,corpcode);
				//EmpExecutionContext.error("企业：" +corpcode+",操作员：" +opUser+opContent+"成功");
				//增加操作日志
				setLog(request, "账户通道配置", opContent+"成功", StaticValue.UPDATE);
				
			}else{
				//增加操作日志
				setLog(request, "账户通道配置", opContent+"失败", StaticValue.UPDATE);
			}
		} catch (Exception e) {
			//异常错误
			if(writer!=null){
				writer.print("error");
			}
			spLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent+opSper, e,corpcode);
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"企业：" +corpcode+",操作员：" +opUser+"路由状态修改异常！"));
		}
		return;
	}
	
	
	
	/**
	 * 获取最大SP通道扩展位数
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void getSublen(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		JSONObject json = new JSONObject();
		try {
			XtGateQueue xt = new BaseBiz().getById(XtGateQueue.class, Long
					.valueOf(id));
			int privilege = xt.getGateprivilege();
			boolean isSupportEn = (privilege&2)==2;
			int signMode = xt.getSignDropType()-0==0?1:0;
			int signlen = signMode==1?xt.getSignlen():xt.getSignstr().trim().length();
			int ensignlen = signMode==1?xt.getEnsignlen():xt.getEnsignstr().trim().replaceAll("[\\[\\]\\|\\^\\{\\}\\~\\\\]", "**").length();
			String maxWordStr = isSupportEn?getContent(xt.getMaxWords()+"",xt.getEnmaxwords()+""):xt.getMaxWords()+"";
			String singleLenStr = isSupportEn?getContent(xt.getSingleLen()+"",xt.getEnsinglelen()+""):xt.getSingleLen()+"";
			String multilen1Str = isSupportEn?getContent(xt.getMultilen1()+"",xt.getEnmultilen1()+""):xt.getMultilen1()+"";
			String multilen2Str = isSupportEn?getContent(xt.getMultilen2()+"",xt.getEnmultilen2()+""):xt.getMultilen2()+"";
			String signlenStr = isSupportEn?getContent(signlen+"",ensignlen+""):signlen+"";
//			String signStr = isSupportEn?getContent(xt.getSignstr()+"",xt.getEnsignstr()+""):xt.getSignstr()+"";
            String signStr = xt.getSignstr().trim();
            String enSignStr = xt.getEnsignstr().trim();
            int maxSignLen = signMode==1? xt.getSignlen():20;
            int maxEnSignLen = signMode == 1 ? xt.getEnsignlen() : 20;
			json.put("errcode", "0");
			json.put("gateName", xt.getGateName());
			json.put("isSupportEn", isSupportEn?1:0);
			json.put("maxWords", maxWordStr);
			json.put("singleLen", singleLenStr);
			json.put("multilen1", multilen1Str);
			json.put("multilen2", multilen2Str);
			json.put("sign", signStr);
            json.put("ensign", enSignStr);
            json.put("maxsignlen", maxSignLen);
            json.put("maxensignlen", maxEnSignLen);
			json.put("signlen", signlenStr);
			json.put("signMode", signMode);
			json.put("sublen", xt.getSublen());
			json.put("gatetype", xt.getGateType());
			json.put("spisuncm", xt.getSpisuncm());
		} catch (Exception e) {
			json.put("errcode", "-1");
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"获取最大SP通道扩展位数异常！"));
		}finally{
			response.getWriter().print(json.toString());
		}
	}
	public String getContent(String c1,String c2){
		return "中文："+StringUtils.rightPad(c1, 10," ")+"英文："+c2;
	}
	public static boolean isInvalidString(String arg){
			
			return (arg==null || "".equals(arg)
					|| "undefined".equals(arg))?true:false;
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
	 * 拷贝数据到GtPortUsed
	 * @throws Exception 
	 */
	public GtPortUsed setGtPortUsed(GtPortUsed gt,String gtId,String sign,String enSign) throws Exception{
		XtGateQueue xgq = new BaseBiz().getById(XtGateQueue.class, gtId);
		if(xgq != null){
			gt.setMaxwords(xgq.getMaxWords());
            gt.setMultilen1(xgq.getMultilen1());
            gt.setEnmultilen1(xgq.getEnmultilen1());
            if(StaticValue.getCORPTYPE() == 0){
                gt.setSignstr(xgq.getSignstr());
                gt.setEnsignstr(xgq.getEnsignstr());
                gt.setMultilen2(xgq.getMultilen2());
                gt.setEnmultilen2(xgq.getEnmultilen2());
            }else{
               String signdroptype = xgq.getSignDropType() != null ? xgq.getSignDropType().toString() : "";
                boolean isAuto = !"0".equals(signdroptype);//自动计算 还需重新计算拆分
                sign = StringUtils.defaultIfEmpty(sign,"").trim();
                enSign = StringUtils.defaultIfEmpty(enSign,"").trim();
                gt.setSignstr(sign.length()>0?sign:" ");
                gt.setEnsignstr(enSign.length()>0?enSign : " ");
                if (xgq.getGateType()-1==0&&isAuto) {//自动计算 还需重新计算拆分
                    gt.setMultilen2(gt.getMultilen1() - sign.length());
                    gt.setEnmultilen2(gt.getEnmultilen1() - enSign.replaceAll("[\\[\\]\\|\\^\\{\\}\\~\\\\]", "**").length());
                } else{
                    gt.setMultilen2(xgq.getMultilen2());
                    gt.setEnmultilen2(xgq.getEnmultilen2());
                }
            }
			gt.setSignlen(xgq.getSignlen());
			gt.setSinglelen(xgq.getSingleLen());
			gt.setEnmaxwords(xgq.getEnmaxwords());
			gt.setEnsignlen(xgq.getEnsignlen());
			gt.setEnsinglelen(xgq.getEnsinglelen());
			gt.setSpgate(xgq.getSpgate());
			gt.setSpisuncm(xgq.getSpisuncm());
			gt.setGateType(xgq.getGateType());
		}
		return gt;
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
				EmpExecutionContext.error("账户通道配置，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "账户通道配置，从session获取加密对象异常。");
			return null;
		}
	}
}
