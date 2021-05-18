package com.montnets.emp.charging.servlet;


import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.charging.biz.ChargingBiz;
import com.montnets.emp.charging.vo.LfDepRechargeLogVo;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
/**
 * 
 * @author yjm
 *
 */
public class cha_balanceLogSvr extends BaseServlet {

	private final String empRoot="xtgl";
	private final String base="/charging";
	private final BaseBiz baseBiz=new BaseBiz();
	/**
	 * 
	 */
	private static final long serialVersionUID = 8708430283384136287L;

	/**
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		LfDepRechargeLogVo lfDepRechargeLogVo = new LfDepRechargeLogVo();
		String guid = request.getParameter("lgguid");
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		try {
			//是否第一次打开
			boolean isFirstEnter = false;
			PageInfo pageInfo=new PageInfo();
			isFirstEnter=pageSet(pageInfo, request);
			if(!isFirstEnter){
				//起始操作时间
				String beginTime = request.getParameter("beginTime");
				String endTime = request.getParameter("endTime");
				if(beginTime != null && !"".equals(beginTime)){
					lfDepRechargeLogVo.setBeginTime(beginTime);
				}
				if(endTime != null && !"".equals(endTime)){
					lfDepRechargeLogVo.setEndTime(endTime);
				}
				//操作类型
				String opAction = request.getParameter("opAction");
				if(opAction != null && !"".equals(opAction)){
					lfDepRechargeLogVo.setCount(Long.parseLong(opAction));
				}
				//操作信息类型  短信-彩信
				String opInfoType = request.getParameter("opInfoType");
				if(opInfoType != null && !"".equals(opInfoType)){
					lfDepRechargeLogVo.setMsgType(Integer.parseInt(opInfoType));
				}
				//操作用户
				String opUser = request.getParameter("opUser");
				if(opUser != null && !"".equals(opUser)){
					lfDepRechargeLogVo.setUserName(opUser);
				}
				//执行结构
				String result = request.getParameter("result");
				if(result != null && !"".equals(result)){
					lfDepRechargeLogVo.setResult(Integer.parseInt(result));
				}
				//充入机构
				String dstName = request.getParameter("dstName");
				if(dstName != null && !"".equals(dstName)){
					lfDepRechargeLogVo.setDstName(dstName);
				}
			}
			LfSysuser sysuser = baseBiz.getByGuId(LfSysuser.class, Long.parseLong(guid));
			ChargingBiz balanceLogBiz = new ChargingBiz();
			List<LfDepRechargeLogVo> lfDepRechargeLogVos = balanceLogBiz.getDepBalanceLogVos(sysuser,lfDepRechargeLogVo,pageInfo);
			request.setAttribute("lfDepRechargeLogVo", lfDepRechargeLogVo);
			request.setAttribute("lfDepRechargeLogVos", lfDepRechargeLogVos);
			request.setAttribute("pageInfo", pageInfo);
			//增加查询日志
			long end_time=System.currentTimeMillis();
			if(pageInfo!=null){
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
				opSucLog(request, "充值回收日志", opContent, "GET");
			}
			request.getRequestDispatcher(this.empRoot +base+ "/cha_balanceLog.jsp")
					.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"充值回收日志页面出现异常！");
		}
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
			EmpExecutionContext.error(e, "记录操作日志异常,session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
	}
}



