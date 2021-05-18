package com.montnets.emp.charging.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.montnets.emp.charging.biz.ChargingBiz;
import com.montnets.emp.charging.dao.LfDepBalanceDAO;
import com.montnets.emp.charging.vo.LfDepBalanceVo;
import com.montnets.emp.charging.vo.LfDepBalanceDefVo;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDepRechargeLog;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
/**
 * 充值回收管理
 * @Projevt: p_xtgl
 * @ClassName: cha_balanceMgrSvt
 * @Description: TODO
 * @Company ShenZhen Montnets Technology CO.,LTD.
 */
public class cha_balanceMgrSvt extends BaseServlet {
	
	private final String empRoot="xtgl";
	private final String base="/charging";
	private static final long serialVersionUID = 9206855342339116239L;
	private final BaseBiz baseBiz=new BaseBiz();
	private final SuperOpLog spLog=new SuperOpLog();
	
	public void find(HttpServletRequest request, HttpServletResponse response) {
		String guidstr;
		LfSysuser sysuser = null;
		try {
			EmpExecutionContext.logRequestUrl(request, "后台请求");
			guidstr = request.getParameter("lgguid");
			if(guidstr != null && !"".equals(guidstr.trim())){
				sysuser = baseBiz.getByGuId(LfSysuser.class, Long.parseLong(guidstr));
			}else{
				Object lfsysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(lfsysuserObj != null){
					sysuser = (LfSysuser) lfsysuserObj;
				}else{
					EmpExecutionContext.error("跳转充值回收管理页面异常，guidstr为空！");
					return;
				}
			}
			Long depId = sysuser.getDepId();
			String corpCode = sysuser.getCorpCode();
			PageInfo pageInfo=new PageInfo();
			pageSet(pageInfo, request);
			//Long depId = lfSysuser.getDepId();
			//根据当前机构获取当前机构的级别
			Integer level = new DepDAO().getUserDepLevel(depId);
			//操作页面跳转
			String operatePageReturn = request.getParameter("operatePageReturn");
			String operatorBalancePri = request.getParameter("operatorBalancePri");
			request.setAttribute("operatePageReturn", operatePageReturn);
			request.setAttribute("operatorBalancePri", operatorBalancePri);
			if(level >= SystemGlobals.getMaxChargeLevel(corpCode)){
				request.getRequestDispatcher(this.empRoot  + base  + "/cha_no_dep_level_permission.jsp").forward(
						request, response);
				return;
			}
			request.getRequestDispatcher(this.empRoot  + base  + "/cha_balanceMgr.jsp").forward(
					request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"充值回收管理页面跳转出现异常！");
			request.setAttribute("findresult", "-1");
			try {
				request.getRequestDispatcher(this.empRoot  + base  + "/cha_balanceMgr.jsp").forward(
						request, response);
			} catch (ServletException e1) {
				EmpExecutionContext.error(e,"充值回收管理页面跳转出现异常！");
			} catch (IOException e1) {
				EmpExecutionContext.error(e,"充值回收管理页面跳转出现异常！");
			}
		}
	}
	
	/***
	 * 查询充值/回收管理列表
	 * @param request
	 * @param response
	 */
	public void getTable(HttpServletRequest request, HttpServletResponse response) {
		Long guid = Long.parseLong(request.getParameter("lgguid"));
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		//新增 机构充值回收权限是否是默认的
		String operatorBalancePri = request.getParameter("operatorBalancePri");
		long begin_time=System.currentTimeMillis();
		try {
			PageInfo pageInfo=new PageInfo();
			String operatePageReturn = request.getParameter("operatePageReturn");
			String depIdStr ="";
			String depName = "";
			String balanceAllDepId ="";
			if("true".equals(operatePageReturn))
			{
				depName = (String) request.getSession(false).getAttribute("blanceDepName");
				depIdStr = (String)request.getSession(false).getAttribute("blanceDepId");
				pageInfo = (PageInfo) request.getSession(false).getAttribute("blancePageInfo");
			}
			else
			{
				depName = request.getParameter("depName");
				depIdStr = request.getParameter("depTreeId");
			}
			pageSet(pageInfo, request);
			LfSysuser sysuser = baseBiz.getByGuId(LfSysuser.class, guid);
			ChargingBiz balanceLogBiz = new ChargingBiz();
			LfDep lfDep = null;
			Long depId = null;
			boolean isAdmin = "admin".equals(sysuser.getUserName());
			List<LfDepBalanceVo> lfDepBalanceVos = null;
			
			//admin可以对前五级机构进行充值/回收操作，其他用户只能对所在机构的下级进行充值/回收操作
			if(isAdmin){
				request.getSession(false).setAttribute("blanceDepId", depIdStr);
//				String depIdStr = "6";
				depId = new DepBiz().getDepById(sysuser.getDepId()).getSuperiorId();
			    lfDepBalanceVos = balanceLogBiz.getDepBalanceVoByAdmin(sysuser.getCorpCode(),depIdStr,depName,pageInfo);
			}else{
				// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
				// 判断是否是默认的机构权限    defaultBalancePri表示为默认机构权限
				if("defaultBalancePri".equals(operatorBalancePri)){
					// 原始的默认机构权限方法 ----------------------------------------------------------------
					lfDep = new DepBiz().getDepById(sysuser.getDepId());
					depId = sysuser.getDepId();
					lfDepBalanceVos = balanceLogBiz.getDepBalanceVoByDepId(sysuser,depId,depName,pageInfo);
					// --------------------------------------------------------------------------------------
				} else{
					request.getSession(false).setAttribute("blanceDepId", depIdStr);
//					String depIdStr = "6";
					depId = new DepBiz().getDepById(sysuser.getDepId()).getSuperiorId();
				    lfDepBalanceVos = balanceLogBiz.getDepBalanceVoByOperator(sysuser.getUserId(),sysuser.getCorpCode(),depIdStr,depName,pageInfo);
				}
				//end
			}
			balanceAllDepId = depIdStr;
			//根据操作员userid获得对应的充值回收权限机构id
			List<Long> balancePriDepsIds = balanceLogBiz.getBalancePriDepsIds(sysuser.getUserId().toString(), sysuser.getDepId().toString(), sysuser.getCorpCode());

			//根据当前机构获取当前机构的级别
			Integer level = new DepDAO().getUserDepLevel(depId);
			if(lfDep != null && level >= SystemGlobals.getMaxChargeLevel(sysuser.getCorpCode())){
			    lfDepBalanceVos = new ArrayList<LfDepBalanceVo>();
			}
			request.getSession(false).setAttribute("blanceDepName", depName);
			request.getSession(false).setAttribute("blancePageInfo", pageInfo);
			
			String corpcode=sysuser.getCorpCode()==null?"":sysuser.getCorpCode()+"";
			//获得机构总余额数
			HashMap<String,Long> hashMap=balanceLogBiz.getMoney(corpcode);
			request.setAttribute("hashMap",hashMap);
			request.setAttribute("depName",depName);
			request.setAttribute("lfDep",lfDep);
			request.setAttribute("balanceAllDepId", balanceAllDepId);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("depBalanceVos", lfDepBalanceVos);
			request.setAttribute("balancePriDepsIdsList",balancePriDepsIds);
			
			//增加查询日志
			long end_time=System.currentTimeMillis();
			if(pageInfo!=null){
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
				opSucLog(request, "充值回收列表信息", opContent, "GET");
			}
			
			request.getRequestDispatcher(this.empRoot  + this.base  + "/cha_depBalanceTable.jsp").forward(
					request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"充值回收管理获取列表出现异常！");
		}
	}
	
	/**
	 * 刷新获得金额
	 * @param request
	 * @param response
	 */
	public void getMoney(HttpServletRequest request, HttpServletResponse response) {

		try {
			ChargingBiz balanceLogBiz = new ChargingBiz();
			HashMap<String,Long> hashMap=balanceLogBiz.getMoney(request.getParameter("lgcorpcode"));
			Long mms=hashMap.get("mms");
			Long sms=hashMap.get("sms");
			response.getWriter().write(sms+","+mms);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取机构总余额出现异常！");
		}
		
	}
	
	
	/**
	 * 充值
	 * @Title: addBalance
	 * @Description: TODO
	 * @param request
	 * @param response 
	 * @return void
	 */
	public void addBalance(HttpServletRequest request, HttpServletResponse response) {
		try {
			String countStr = request.getParameter("count");
			String depIdStr = request.getParameter("depId");
			Long count = null;
			Long depId = null;
			if(countStr != null && !"".equals(countStr.trim()))
			{
				count = Long.parseLong(countStr);
			}else{
				EmpExecutionContext.error("充值失败，count："+count);
				response.getWriter().write("0");
				return;
			}
			if(depIdStr != null && !"".equals(depIdStr.trim()))
			{
				depId = Long.parseLong(depIdStr);
			}else{
				EmpExecutionContext.error("充值失败，depId："+depId);
				response.getWriter().write("0");
				return;
			}
			//充值信息类型 1=短信  2=彩信
			String msgType = request.getParameter("msgType");
			String addMark = request.getParameter("addMark");
			if(addMark == null || "".equals(addMark)){
				addMark = " ";
			}
			ChargingBiz balanceLogBiz = new ChargingBiz();
			// 充值类型（100为总公司充值/回收；101机构为机构充值/回收； 102机构为操作员充值/回收）
			Integer optType = 0; ; 
			//String result = null;
			Integer returnmsg = 0;
			String lgguidStr = request.getParameter("lgguid");
			Long lgguid = null;
			if(lgguidStr != null && !"".equals(lgguidStr.trim()))
			{
				lgguid = Long.parseLong(lgguidStr);
			}else{
				response.getWriter().write("0");
				EmpExecutionContext.error("充值失败，lgguid:"+lgguid);
				return;
			}
			LfSysuser  sysuser = null;
			try {
				sysuser = baseBiz.getByGuId(LfSysuser.class, lgguid);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"充值回收管理获取当前操作员出现异常！");
			}
			if("1".equals(msgType)){
				returnmsg = balanceLogBiz.addSmsBalance(sysuser, depId, count);
				optType = 100;
			}else if("2".equals(msgType)){
				returnmsg = balanceLogBiz.addMmsBalance(sysuser, depId, count);
				optType = 101;
			}
			LfDepRechargeLog lfDepRechargeLog = new LfDepRechargeLog();
			if(sysuser!=null){
			lfDepRechargeLog.setCorpCode(sysuser.getCorpCode());
			//充值源id 上级机构
			lfDepRechargeLog.setSrcTargetId(sysuser.getDepId());
			lfDepRechargeLog.setOptId(sysuser.getGuId());
			}
			//充值数量
			lfDepRechargeLog.setCount(count);
			
			// 充值目的id 充值机构
			lfDepRechargeLog.setDstTargetId(depId);
			// 充值类型（100为总公司充值/回收；101机构为机构充值/回收； 102机构为操作员充值/回收）
			lfDepRechargeLog.setOptType(optType);
			lfDepRechargeLog.setMsgType(Integer.parseInt(msgType));
			
			
			//充值源名称/描述--->充值目的名称/描述
			lfDepRechargeLog.setOptInfo(returnmsg + " ");
			
			lfDepRechargeLog.setMemo(addMark);
			if(returnmsg == 0){
				lfDepRechargeLog.setResult(0);
			}else{
				lfDepRechargeLog.setResult(1);
			}
			try {
				balanceLogBiz.addBalanceLog(lfDepRechargeLog);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"充值记录保存失败！");
			}
			response.getWriter().write(String.valueOf(returnmsg));
		} catch (Exception e) {
			EmpExecutionContext.error(e,"充值失败！");
		}
	}
	/**
	 * 回收
	 * @Title: recBalance
	 * @Description: TODO
	 * @param request
	 * @param response 
	 * @return void
	 */
	public void recBalance(HttpServletRequest request, HttpServletResponse response) {
		try {
			String countStr = request.getParameter("count");
			String depIdStr = request.getParameter("depId");
			Long count = null;
			Long depId = null;
			if(countStr != null && !"".equals(countStr.trim()))
			{
				count = Long.parseLong(countStr);
			}else{
				response.getWriter().write("0");
				EmpExecutionContext.error("回收记录保存失败,count:"+count);
				return;
			}
			if(depIdStr != null && !"".equals(depIdStr.trim()))
			{
				depId = Long.parseLong(depIdStr);
			}else{
				response.getWriter().write("0");
				EmpExecutionContext.error("回收记录保存失败,depId:"+depId);
				return;
			}
			//1=短信  2=彩信
			String msgType = request.getParameter("msgType");
			String recMark = request.getParameter("recMark");
			if(recMark == null || "".equals(recMark)){
				recMark = " ";
			}
			
			ChargingBiz balanceLogBiz = new ChargingBiz();
			// 充值类型（100为总公司充值/回收；101机构为机构充值/回收； 102机构为操作员充值/回收）
			Integer optType = 0;
			String lgguidStr = request.getParameter("lgguid");
			Long lgguid = null;
			if(lgguidStr != null && !"".equals(lgguidStr.trim()))
			{
				lgguid = Long.parseLong(lgguidStr);
			}else{
				response.getWriter().write("0");
				EmpExecutionContext.error("回收记录保存失败，lgguid:"+lgguid);
				return;
			}
			LfSysuser  sysuser = null;
			try {
				sysuser = baseBiz.getByGuId(LfSysuser.class, lgguid);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"充值回收管理获取当前操作员对象出现异常！");
			}
			Integer returnmsg = 0;
			if("1".equals(msgType)){
				returnmsg = balanceLogBiz.recSmsBalance(sysuser, depId, count);
				optType = 100;
			}else if("2".equals(msgType)){
				returnmsg = balanceLogBiz.recMmsBalance(sysuser, depId, count);
				optType = 101;
			}
			
			LfDepRechargeLog lfDepRechargeLog = new LfDepRechargeLog();
			if(sysuser!=null){
				lfDepRechargeLog.setCorpCode(sysuser.getCorpCode());
				//充值源id 上级机构
				lfDepRechargeLog.setSrcTargetId(sysuser.getDepId());
				lfDepRechargeLog.setOptId(sysuser.getGuId());
			}
			//充值数量
			lfDepRechargeLog.setCount(count * -1);
			
			// 充值目的id 充值机构
			lfDepRechargeLog.setDstTargetId(depId);
			lfDepRechargeLog.setOptType(optType);
			lfDepRechargeLog.setMsgType(Integer.parseInt(msgType));
			//充值源名称/描述--->充值目的名称/描述
			lfDepRechargeLog.setOptInfo(returnmsg+" ");
			lfDepRechargeLog.setMemo(recMark);
			if(returnmsg == 0){
				lfDepRechargeLog.setResult(0);
			}else{
				lfDepRechargeLog.setResult(1);
			}
			try {
				balanceLogBiz.addBalanceLog(lfDepRechargeLog);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"回收失败！");
			}
			response.getWriter().write(String.valueOf(returnmsg));
		} catch (Exception e) {
			EmpExecutionContext.error(e,"回收记录保存失败！");
		}
	}
	/**
	 * 判断当前操作员是否有对选中机构的批量充值权限
	 * @Title: checkLguser
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @return void 返回-1表示无权限，0表示有权限
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-12-2 上午09:56:14
	 */
	public void checkLguser(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String depIdStr = request.getParameter("depId");
		String lgguidStr = request.getParameter("lgguid");
		ChargingBiz chargingBiz = new ChargingBiz();
		try {
			Long depId = null;
			LfSysuser sysuser = null;
			if(depIdStr != null && !"".equals(depIdStr.trim()))
			{
				depId = Long.valueOf(depIdStr.trim());
			}
			else
			{
				response.getWriter().print("-1");
				EmpExecutionContext.error("查询当前操作员是否对选中的机构有批量充值权限异常！depId:"+depIdStr);
				return;
			}
			if(lgguidStr != null && !"".equals(lgguidStr))
			{
				sysuser = baseBiz.getByGuId(LfSysuser.class, Long.valueOf(lgguidStr));
			}
			else
			{
				response.getWriter().print("-1");
				EmpExecutionContext.error("查询当前操作员是否对选中的机构有批量充值权限异常！lgguid:"+lgguidStr);
				return;
			}
			boolean falg = false;
			//admin有所有机构的充值权限
			if(sysuser != null && sysuser.getUserName().equals("admin"))
			{
				response.getWriter().print("0");
			}
			//当前操作员有所属机构的充值权限
			else if(sysuser != null && depId.equals(sysuser.getDepId()))
			{
				response.getWriter().print("0");
			}
			else
			{
				if(sysuser!=null){
				//验证当前操作员是否有选中机构的批量充值权限
				falg = chargingBiz.checkLguser(depId, sysuser.getUserId(), sysuser.getCorpCode());
				}
				if(falg)
				{
					response.getWriter().print("0");
				}
				else
				{
					response.getWriter().print("-1");
				}
			}
		} catch (Exception e) {
			response.getWriter().print("-1");
			EmpExecutionContext.error(e,"判断当前操作员是否有对选中机构的批量充值权限");
		}
		
		
	}
	
	/**
	 * 跳转到批量充值页面
	 * @Title: toAddBalanceAll
	 * @Description: TODO
	 * @param request
	 * @param response 
	 * @return void
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-11-20 上午09:28:12
	 */
	public void toAddBalanceAll(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String depId = request.getParameter("depId");
		String lgguid = request.getParameter("lgguid");
		LfDepBalanceVo balance = null;
		List<LfDepBalanceDefVo> LfDepBalanceDefVos = null;
		ChargingBiz chargingBiz = new ChargingBiz();
		try {
			LfSysuser lfSysuser = null;
			if(depId == null || "".equals(depId.trim()))
			{
				EmpExecutionContext.error("批量充值机构id为空! depId="+depId);
				return;
			}
			if(lgguid != null && !"".equals(lgguid.trim()))
			{
				lfSysuser = baseBiz.getByGuId(LfSysuser.class, Long.valueOf(lgguid));
			}
			else
			{
				EmpExecutionContext.error("批量充值异常，lgguid为空！lgguid:"+lgguid);
				return;
			}
			String lgCorpcode = "";
			Long lguserid = null;
			String lgusername = "";
			Long lgDepId = null;
			if(lfSysuser != null)
			{
				lgCorpcode = lfSysuser.getCorpCode();
				lguserid = lfSysuser.getUserId();
				lgusername = lfSysuser.getUserName();
				lgDepId  = lfSysuser.getDepId();
			}
			else
			{
				EmpExecutionContext.error("机构批量充值失败，lfSysuser为空。");
				return;
			}
			//根据机构编码获取机构信息及其余额信息
			balance = chargingBiz.getDepBalanceBydepId(depId,lgCorpcode);
			//根据机构编码获取子机构的信息
			if(lgusername.equals("admin") || depId.equals(String.valueOf(lgDepId)))
			{
				//获取admin和选中机构是当前操作员所属机构的子机构信息
				LfDepBalanceDefVos = chargingBiz.getDepSon(Long.valueOf(depId),lgCorpcode);
			}
			else 
			{
				//获取有跨机构充值权限的操作员，选中机构的子机构信息，不关联充值权限表
				LfDepBalanceDefVos = chargingBiz.getDepSonNoAdmin(Long.valueOf(depId), lgCorpcode, lguserid);
			}
			
			request.setAttribute("balance",balance);
			request.setAttribute("lfDeplist", LfDepBalanceDefVos);
			
			request.getRequestDispatcher(this.empRoot  + base  + "/cha_depBalanceAll.jsp").forward(
					request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "跳转到批量充值页面异常");
		}
	}
	/**
	 * 批量充值
	 * @Title: AddBalanceAll
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @return void
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-11-24 上午11:02:43
	 */
	public void addBalanceAll(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//机构id
		String supDepId = request.getParameter("supDepId");
		//当前操作员
		//String lguserId = request.getParameter("lguserId");
		//漏洞修复 session里获取操作员信息
		String lguserId = SysuserUtil.strLguserid(request);

		String lgguid = request.getParameter("lgguid");
		//需要充值的子机构的ID
		String sonDepId = request.getParameter("balanceDepIds");
		//短信充值数量
		String smsCount = request.getParameter("smsCounts");
		//彩信充值数量
		String mmsCount = request.getParameter("mmsCounts");
		//是否设置默认
		boolean isDefault = "1".equals(request.getParameter("isDefault"));
		try {
			LfSysuser  sysuser = null;
			//获取当前操作员信息
			if(lgguid != null && !"".equals(lgguid)){
				sysuser = baseBiz.getByGuId(LfSysuser.class, Long.valueOf(lgguid));				
			}
			//获取不到再从session中获取
			else
			{
				sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			}
			if(sysuser == null)
			{
				response.getWriter().print("-1");
				EmpExecutionContext.error("机构批量充值失败，获取当前操作员信息失败，lfSysuser为空");
				return;
			}
			if(supDepId == null || "".equals(supDepId))
			{
				response.getWriter().print("-1");
				EmpExecutionContext.error("机构批量充值失败，父机构id为空。supDepId:"+supDepId);
				return;
			}
			String[] sonDepIds = sonDepId.split(",");
			String[] smsCounts = smsCount.split(",");
			String[] mmsCounts = mmsCount.split(",");
			//所有子机构短信总条数
			Long sonDepBalanceAllsms = 0l;
			//所有子机构彩信总条数
			Long sonDepBalanceAllmms = 0l;
			for (String string : smsCounts) {
				sonDepBalanceAllsms +=Integer.valueOf(string);
			}
			for (String string : mmsCounts) {
				sonDepBalanceAllmms +=Integer.valueOf(string);
			}
			//充值对象做值
			List<LfDepBalanceVo> balanceVos = new ArrayList<LfDepBalanceVo>();
			for (int i = 0; i < sonDepIds.length; i++) {
				LfDepBalanceVo balanceVo = new LfDepBalanceVo();
				balanceVo.setDepId(Long.valueOf(sonDepIds[i]));
				balanceVo.setSmsBalance(Long.valueOf(smsCounts[i]));
				balanceVo.setMmsBalance(Long.valueOf(mmsCounts[i]));
				balanceVos.add(balanceVo);
			}
			String opContent = "";
			if(balanceVos == null || balanceVos.size() ==0)
			{
				EmpExecutionContext.error("批量充值失败，balances为空.");
				response.getWriter().print("-1");
				return;
			}
			//充值
			int returnNum = new ChargingBiz().addBalanceAll(sysuser,balanceVos,
					sonDepBalanceAllsms,sonDepBalanceAllmms,Long.valueOf(supDepId));
			if(returnNum < 0)
			{
				opContent = "批量充值失败，短信"+sonDepBalanceAllsms+"条,彩信"+sonDepBalanceAllmms+"条。";
				//添加操作日志
				spLog.logFailureString(sysuser.getName(), "充值回收管理", StaticValue.OTHER, opContent, null, sysuser.getCorpCode());
				EmpExecutionContext.error("批量短信充值失败！总条数:"+sonDepBalanceAllsms+"操作员:"+lguserId+","+sysuser.getUserName()+"机构:"+supDepId+",企业编码："+sysuser.getCorpCode());
			}else{
				//判断是否需要设置默认值
				if(isDefault)
				{
					//添加默认值
					int mu = new ChargingBiz().addDefault(sysuser, balanceVos);
					if(mu < 0)
					{
						EmpExecutionContext.error("设置默认值失败！");
						returnNum = -2;
					}
					else
					{
						EmpExecutionContext.info("设置默认值成功!");
						returnNum = 0;
					}
				}
				//添加操作日志
				opContent = "批量充值成功，短信"+sonDepBalanceAllsms+"条,彩信"+sonDepBalanceAllmms+"条。";
				spLog.logSuccessString(sysuser.getName(), "充值回收管理", StaticValue.OTHER, opContent, sysuser.getCorpCode());
				EmpExecutionContext.info("批量短信充值成功,总条数:"+sonDepBalanceAllsms+" 操作员："+lguserId+","+sysuser.getUserName() +"机构:"+supDepId+"企业编码："+sysuser.getCorpCode());
			}
			response.getWriter().print(returnNum);
		} catch (Exception e) {
			response.getWriter().print("-1");
			EmpExecutionContext.error(e,"批量充值失败！");
		}
		
	}
	
	
	/**
	 *   创建树
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void createTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String lgguid = request.getParameter("lgguid");
		Long guid = null;
		if(lgguid != null && !"".equals(lgguid.trim()))
		{
			guid = Long.parseLong(lgguid);
		}else{
			response.getWriter().print("");
			EmpExecutionContext.error("充值回收管理获取机构树出现异常，guid："+guid);
			return;
		}
		try {
			//获取当前登录的操作员
			LfSysuser sysuser = baseBiz.getByGuId(LfSysuser.class, guid);
			String depId = sysuser.getDepId().toString();
			String userName =sysuser.getUserName();
			String corpCode = sysuser.getCorpCode();
			//String depId = String.valueOf(lfSysuser.getDepId());
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();

			List<LfDep> lfDeps = null;
			conditionMap.put("corpCode", corpCode);
			orderbyMap.put("depLevel", StaticValue.ASC);
			if("admin".equals(userName)){
				//如果是admin登录，则从顶级机构开始查询，superiorid=0表示顶级机构
				conditionMap.put("superiorId", "0");
				lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
			}else{
				ChargingBiz balanceLogBiz = new ChargingBiz();
				lfDeps = balanceLogBiz.getLfDepAndDepSon(Long.valueOf(depId));
			}
			if(lfDeps != null && lfDeps.size()>0){
				LfDep lfDep = null;
				StringBuffer tree = new StringBuffer("[");
				for(int i=0;i<lfDeps.size();i++){
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					//tree.append(",level:").append(lfDep.getDepLevel());
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
				response.getWriter().write(tree.toString());
			}
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"充值回收管理获取机构树出现异常！");
		}
	}
	
	
	
	//获取机构�?
	public void createTree2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long depId = null;
		//机构id
		String depStr = request.getParameter("depId");
		//String userIdStr = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userIdStr = SysuserUtil.strLguserid(request);

		Long userId = null;
		try{
			//从session获取企业编码
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			String corpCode = sysuser != null?sysuser.getCorpCode():"";
			//获取当前登录账号的userid
			try{
				if(userIdStr != null && !"".equals(userIdStr)&&!"undefined".equals(userIdStr)){
					userId = Long.parseLong(userIdStr);
				}else{
					response.getWriter().print("");
					EmpExecutionContext.error("充值回收管理页面获取当前登录操作员ID异常，将从session中获取！userId:"+userId);
					//return;
					userId=sysuser!=null?sysuser.getUserId():0;
				}
			}catch (Exception e) {
				EmpExecutionContext.error("充值回收管理当前操作员lguserid转化出现异常！将从session中获取！userId:"+userId);
				userId=sysuser!=null?sysuser.getUserId():0;
			}
			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}

			//获取机构树字符串
			String departmentTree = getDepartmentJosnData2(depId,userId,corpCode);
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"充值回收管理页面获取机构树出现异常！depidstr:"+depStr+",userIdStr:"+userIdStr);
		}
	}
	
	
	
	/**
	 * 树
	 * @return
	 */
	protected String getDepartmentJosnData2(Long depId,Long userId){
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			sysuser = baseBiz.getById(LfSysuser.class, userId);
			if( sysuser!=null && sysuser.getPermissionType()!=1)
			{
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps = new ArrayList<LfDep>();
				if(depId == null){
					List<LfDep> list = depBiz.getAllDeps(userId);
					if(!list.isEmpty())
					{
						lfDeps.add(list.get(0));
					}
					//LfDep lfDep = .get(0);//这里需要优化
					//lfDeps.add(lfDep);
					//lfDeps.addAll(new DepBiz().getDepsByDepSuperId(lfDep.getDepId()));
				}else{
					lfDeps = new DepBiz().getDepsByDepSuperId(depId);
				}
				
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					tree.append(",");
				}
				if(tree.length() >1)
				{
					tree = tree.deleteCharAt(tree.length()-1);
				}
				tree.append("]");
			
			}else{
				tree = new StringBuffer("[]");
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"充值回收管理获取机构树出现异常！");
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}
	
	/**
	 * 树
	 * @return
	 */
	protected String getDepartmentJosnData2(Long depId,Long userId,String corpCode){
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			sysuser = baseBiz.getById(LfSysuser.class, userId);
			if( sysuser!=null && sysuser.getPermissionType()!=1)
			{
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps = new ArrayList<LfDep>();
				if(depId == null){
					//List<LfDep> list = depBiz.getAllDeps(userId);
					List<LfDep> list = depBiz.getAllDepByUserIdAndCorpCode(userId, corpCode);
					if(!list.isEmpty())
					{
						lfDeps.add(list.get(0));
					}
					//LfDep lfDep = .get(0);//这里需要优化
					//lfDeps.add(lfDep);
					//lfDeps.addAll(new DepBiz().getDepsByDepSuperId(lfDep.getDepId()));
				}else{
					//lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,corpCode);
				}
				
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					tree.append(",");
				}
				if(tree.length() >1)
				{
					tree = tree.deleteCharAt(tree.length()-1);
				}
				tree.append("]");
			
			}else{
				tree = new StringBuffer("[]");
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"充值回收管理获取机构树出现异常！");
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}
	/**
	 * 设置短彩告警阀值
	 * @Title: setAlarm
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @return void
	 */
	public void setAlarm(HttpServletRequest request, HttpServletResponse response) throws IOException {
			String depIdStr =request.getParameter("depId");
			String smsAlarmStr =request.getParameter("smsAlarm");
			String mmsAlarmStr =request.getParameter("mmsAlarm");
			String name =request.getParameter("name");
			String phone =request.getParameter("phone");
			String lgcorpcode =request.getParameter("lgcorpcode");
			int ucount=0;
			try
			{
				if(lgcorpcode==null||"".equals(lgcorpcode.trim())){
					EmpExecutionContext.error("设置短彩信阀值时获取机构编码发生异常！");
					return;
				}
				if(depIdStr==null||"".equals(depIdStr.trim())){
					EmpExecutionContext.error("设置短彩信阀值时获取机构id发生异常！");
					return;
				}
				if(phone==null||"".equals(phone.trim())){
					EmpExecutionContext.error("设置短彩信阀值时获取通知人手机号发生异常！");
					return;
				}
				Integer smsAlarm,mmsAlarm;
				if(smsAlarmStr==null||"".equals(smsAlarmStr.trim())){
					smsAlarm=null;
				}else{
					smsAlarm=Integer.parseInt(smsAlarmStr);
				}
				if(mmsAlarmStr==null||"".equals(mmsAlarmStr.trim())){
					mmsAlarm=null;
				}else{
					mmsAlarm=Integer.parseInt(mmsAlarmStr);
				}
				Long depId = Long.parseLong(depIdStr);
				LfDepBalanceDAO balanceDao =new LfDepBalanceDAO();
				ucount=balanceDao.updateBalance(depId, lgcorpcode, smsAlarm, mmsAlarm, name, phone);
				if(ucount>0){
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser lfSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("充值/回收管理", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "设置短彩信阀值成功[通知人姓名，手机号码]（"+name+"，"+phone+"）", "OTHER");
					}
				}
			}
			catch (Exception e)
			{	
				EmpExecutionContext.error(e,"设置短彩信阀值出现异常！");
				ucount=0;
			}finally{
				response.getWriter().print(ucount);
			}
			
	}
	/**
	 * 撤销短彩告警阀值
	 * @Title: deleteAlarm
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @return void -1失败，0成功
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-12-15 下午01:53:16
	 */
	public void deleteAlarm(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
		try {
			//机构Id
			String depId = request.getParameter("depId");
			//当前登录操作员对象
			LfSysuser sysuser = getLoginUser(request);
			//企业编码
			String corpCode = sysuser != null?sysuser.getCorpCode():"";
			ChargingBiz chargingBiz = new ChargingBiz();
			
			if(depId == null || "".equals(depId.trim()))
			{
				EmpExecutionContext.error("撤销短彩告警阀值失败，机构id为空！depId:"+depId+",corpCode:"+corpCode);
				response.getWriter().print("-1");
				return;
			}
			
			if(corpCode == null || "".equals(corpCode.trim()))
			{
				EmpExecutionContext.error("撤销短彩告警阀值失败，企业编码为空！depId:"+depId+",corpCode:"+corpCode);
				response.getWriter().print("-1");
				return;
			}
			boolean falg = false;
			//撤销短彩告警阀值
			falg = chargingBiz.deleteAlarm(Long.valueOf(depId), corpCode);
			if(falg)
			{
				response.getWriter().print("0");
				EmpExecutionContext.info("撤销短彩告警阀值成功！");
			}
			else
			{
				response.getWriter().print("-1");
				EmpExecutionContext.error("撤销短彩告警阀值失败,depId:"+depId+",corpCode:"+corpCode);
			}
			
		} catch (Exception e) {
			response.getWriter().print("-1");
			EmpExecutionContext.error(e, "撤销短彩告警阀值异常！");
		}
	}
	
	
	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		LfSysuser lfSysuser = null;
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "记录操作日志异常,session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
	}
	
	
	
	
	// EMP5.7新需求：增加对操作员充值和回收权限   by pengj
	/**
	 *   获取操作员的充值回收权限机构的树
	 * @param request
	 * @param response
	 */
	public void getOperatorBalancePriDepJson(HttpServletRequest request, HttpServletResponse response)
	{
		LfSysuser sysuser = null;
		ChargingBiz chargingBiz = new ChargingBiz();
		try
		{
			String depId = request.getParameter("depId");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			Object lfsysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(lfsysuserObj != null){
				sysuser = (LfSysuser) lfsysuserObj;
			}
			String corpCode = sysuser!=null?sysuser.getCorpCode():"";
			//通过机构权限中的机构，查出其所有父机构，用于创建充值回收权限的机构树
			List<LfDep> balanceDepList = chargingBiz.getOperatorBalancePriDeps(lguserid, depId, corpCode,  sysuser!=null?sysuser.getDepId().toString():"");
			LfDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(balanceDepList != null && balanceDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < balanceDepList.size(); i++) {
					dep = balanceDepList.get(i);
					tree.append("{");
					tree.append("id:").append(dep.getDepId()+"");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",depcodethird:'").append(dep.getDepCodeThird()).append("'");
					//树数据中加入父机构id
					if(dep.getSuperiorId()-0==0){
						tree.append(",pId:").append(0);
					}else{
						tree.append(",pId:").append(dep.getSuperiorId());
					}
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != balanceDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			try
			{
				response.getWriter().print("");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1,"JSON传值到页面异常！");
			}
			EmpExecutionContext.error(e,"获取充值回收权限机构信息出现异常！");
		}
	}

	// end
}
