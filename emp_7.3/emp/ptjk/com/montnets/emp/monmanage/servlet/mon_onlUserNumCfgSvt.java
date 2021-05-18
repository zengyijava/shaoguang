/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-4 上午10:01:19
 */
package com.montnets.emp.monmanage.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitoronline.LfMonOnlcfg;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.SuperOpLog;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-4 上午10:01:19
 */

public class mon_onlUserNumCfgSvt extends BaseServlet
{
	final String						empRoot				= "ptjk";

	final String						base				= "/monmanage";

	final BaseBiz						baseBiz				= new BaseBiz();
	protected final SuperOpLog spLog = new SuperOpLog();
	public static final String OPMODULE = "在线用户数设置";
	/**
	 * @description 在线用户监控设置
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-4 上午10:01:47
	 */
	private static final long	serialVersionUID	= 1L;

	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			List<LfMonOnlcfg> onlUserNum = baseBiz.getByCondition(LfMonOnlcfg.class, null, null);
			Iterator<LfMonOnlcfg> itr = onlUserNum.iterator();
			LfMonOnlcfg onlUser = null;
			if(itr.hasNext())
			{
				onlUser = itr.next();
			}
			request.setAttribute("onlUser", onlUser);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询用户在线数失败！");
		}
		finally
		{
			try
			{
				request.getRequestDispatcher(this.empRoot + base + "/mon_onlUserNumCfg.jsp").forward(request, response);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "跳转至用户在线监控设置页面失败！");
			}
		}
	}

	/**
	 * @description
	 * @param request
	 * @param response
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-4 上午10:09:30
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response)
	{
		//操作模块
		String opModule = "系统监控";
		String opSper = StaticValue.OPSPER;
		String opType = StaticValue.UPDATE;
		String opContent = "在线用户数设置";
		String opUser = "";
		// 在线用户数告警阀值
		String maxOnlineStr = request.getParameter("maxonline");
		// 刷新时间
		String monFreqStr = request.getParameter("monFreq");
		// 告警手机
		String monphone = request.getParameter("monphone");
		// 告警邮箱
		String monemail = request.getParameter("monemail");
		//监控状态
		String monstatus = request.getParameter("monstatus");
		// 自增ID
		String idStr = request.getParameter("id");
		//配置时间
		Timestamp modifytime = new Timestamp(System.currentTimeMillis());
		// 返回状态
		String result = "faild";
		//修改字段
		String field = "[在线用户数告警阀值，刷新频率，告警手机号，监控状态]";
		LfMonOnlcfg onlineUserCfg = new LfMonOnlcfg();
		LfSysuser sysuser = null;
		try
		{
			sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(sysuser!=null)
			{
				opUser=sysuser.getUserName();
			}
			Long maxOnline = Long.valueOf(maxOnlineStr);
			Long monFreq = Long.valueOf(monFreqStr);
			Long id = Long.valueOf(idStr);
			//更新前字段信息
			LfMonOnlcfg onlineUserCfg2 = baseBiz.getById(LfMonOnlcfg.class, id);
			String editBeforeValueStr = "(";
			editBeforeValueStr += onlineUserCfg2.getMaxonline()+"，"+onlineUserCfg2.getMonfreq()+"，"+onlineUserCfg2.getMonphone()+"，"+onlineUserCfg2.getMonemail()+"，"+onlineUserCfg2.getMonstatus();
			editBeforeValueStr += ")";
			if(monphone==null){monphone="";}
			if(monemail==null || monemail.equals("")){monemail=" ";}
			if(!"0".equals(monstatus)){monstatus="1";}
			onlineUserCfg.setMaxonline(maxOnline);
			onlineUserCfg.setMonfreq(monFreq);
			onlineUserCfg.setId(id);
			onlineUserCfg.setMonphone(monphone);
			onlineUserCfg.setMonemail(monemail);
			onlineUserCfg.setModifytime(modifytime);
			onlineUserCfg.setMonstatus(Integer.parseInt(monstatus));
			// 设置用户在线监控信息
			if(baseBiz.updateObj(onlineUserCfg))
			{
				result = "success";
				spLog.logSuccessString(opUser, opModule, opType, opContent,sysuser.getCorpCode());
			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String flag = "失败";
				if("success".equals(result))
				{
					flag = "成功";
				}
				String editAfterValueStr = "("+maxOnlineStr+"，"+monFreqStr+"，"+monFreqStr+"，"+monphone+"，"+monemail+"，"+monstatus+")";
				String opContent2 = "设置在线用户数"+flag+"。"+field+editBeforeValueStr+"->"+editAfterValueStr;
				EmpExecutionContext.info(OPMODULE, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent2, "update");
			}
			
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e, "获取用户在线监控设置参数异常，maxOnline=" + maxOnlineStr + "，monFreq=" + monFreqStr);
		}
		catch (Exception e)
		{
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, null,sysuser.getCorpCode());
			EmpExecutionContext.error(e, "设置用户在线监控信息失败！");
		}
		finally
		{
			try
			{
				response.getWriter().write(result);
			}
			catch (IOException e)
			{
				EmpExecutionContext.error(e, "设置用户在线监控信息失败！");
			}
		}

	}

}
