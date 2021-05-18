package com.montnets.emp.monnet.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitor.LfMonHnetwarn;
import com.montnets.emp.entity.monitor.LfMonHostnet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
/**
 * WEB主机网络监控servlet
 * @功能概要：
 * @项目名称： p_ptjk
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-23 下午02:46:48
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
@SuppressWarnings("serial")
public class mon_webHostMonNetSvt extends BaseServlet {

	final String empRoot="ptjk";
	final String base="/monnet";
	protected final SuperOpLog spLog = new SuperOpLog();
	private final BaseBiz baseBiz = new BaseBiz();
	public static final String OPMODULE = "WEB主机网络监控";
	
	/**
	 * WEB主机网络监控查询
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-4-23 下午02:47:01
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try {
			pageSet(pageInfo,request);
			//web主机名称
			String webname = request.getParameter("webname");
			//监控主机名称
			String hostname = request.getParameter("hostname");
			//告警级别
			String evttype = request.getParameter("evttype");
			//WEB主机所属节点
			String webnode = request.getParameter("webnode");
			//监控状态
			String monstatus = request.getParameter("monstatus");
			//网络状态
			String netstate = request.getParameter("netstate");
			//从缓存中取map
			Map<String, LfMonHostnet> monhnetmap =MonitorStaticValue.getHostNetMap();
			if(monhnetmap == null || monhnetmap.size() < 1)
			{
				monhnetmap = MonitorStaticValue.getHostNetMapTemp();
			}
			List<LfMonHostnet> monList = new ArrayList<LfMonHostnet>();
			List<LfMonHostnet> pageList = new ArrayList<LfMonHostnet>();
			//遍历程序动态信息map
			Iterator<String> keys = monhnetmap.keySet().iterator();
			//遍历程序动态信息mondbstateList
			while(keys.hasNext()){
				String key = keys.next();
				LfMonHostnet monhnet=monhnetmap.get(key);
				//WEB主机名称过滤 如果为空说明没有限制 
				if(webname!=null &&!"".equals(webname))
				{	//如果为空则不匹配 跳出
					if(monhnet.getWebname()==null||"".equals(monhnet.getWebname())){
						continue;
					}else{//手动输入 模糊匹配
						if(monhnet.getWebname().toLowerCase().indexOf(webname.toLowerCase())<0){
							continue;
						}
					}
				}
				//监控主机名称
				if(hostname!=null &&!"".equals(hostname))
				{	//如果为空则不匹配 跳出
					if(monhnet.getHostname()==null||"".equals(monhnet.getHostname())){
						continue;
					}else{//手动输入 模糊匹配
						if(monhnet.getHostname().toLowerCase().indexOf(hostname.toLowerCase())<0){
							continue;
						}
					}
				}
				//告警级别条件过滤
				if(evttype!=null &&!"".equals(evttype))
				{
					Integer type= monhnet.getEvttype();
					if(type==null){type = -1;}
					if(evttype.equals("0")&&(type==1||type==2)){
						continue;
					}
					if(!evttype.equals("0")&&!evttype.equals(String.valueOf(type))){
						continue;
					}
				}
				
				//WEB主机所属节点
				if(webnode!=null &&!"".equals(webnode))
				{
					if(monhnet.getWebnode()==null) {
						continue;
					}else if(!webnode.equals(String.valueOf(monhnet.getWebnode()))){
						continue;
					}
				}
				
				//监控状态
				if(monstatus!=null &&!"".equals(monstatus))
				{
					if(monhnet.getMonstatus()==null) {
						continue;
					}else if(!monstatus.equals(String.valueOf(monhnet.getMonstatus()))){
						continue;
					}
				}
				
				//网络状态
				if(netstate!=null &&!"".equals(netstate))
				{
					if(monhnet.getNetstate()==null) {
						continue;
					}else if(!netstate.equals(String.valueOf(monhnet.getNetstate()))){
						continue;
					}
				}
				
				
				monList.add(monhnet);
			}
            //排序
            Collections.sort(monList, new Comparator<LfMonHostnet>() {
                public int compare(LfMonHostnet o1, LfMonHostnet o2) {
                    return o1.getWebnode().compareTo(o2.getWebnode());
                }
            });
			request.setAttribute("dbstateList", monList);
			//分页设置
			pageSetList(pageInfo,monList.size());
			for(int i=(pageInfo.getPageIndex()-1)*pageInfo.getPageSize();i<pageInfo.getPageIndex()*pageInfo.getPageSize()&&i<monList.size();i++)
			{
				pageList.add(monList.get(i));
			}
			//分页
			request.setAttribute("pageList", pageList);
			request.setAttribute("pageInfo", pageInfo);
			
			//企业编码
			String corpCode = null;
			//当前操作员ID
			String userId = null;
			//当前操作员登录名
			String userName = null;
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				if(loginSysuser!=null && !"".equals(loginSysuser.getCorpCode()))
				{
					corpCode = loginSysuser.getCorpCode();
				}
				if(loginSysuser!=null && loginSysuser.getUserId() != null)
				{
					userId = String.valueOf(loginSysuser.getUserId());
				}
				if(loginSysuser!=null && !loginSysuser.getUserName().equals("") && loginSysuser.getUserName()!= null)
				{
					userName = loginSysuser.getUserName();
				}
			}

			//查询出的数据的总数量
			int totalCount = pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间："+sdf.format(stratTime)+ " 耗时：" 
			+(System.currentTimeMillis()-stratTime) + " 数量："+totalCount;
			
			EmpExecutionContext.info("WEB主机网络监控", corpCode, userId, userName, opContent, "GET");
			
			request.getRequestDispatcher(this.empRoot+base+"/mon_webHostMonNet.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"WEB主机网络监控查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_webHostMonNet.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"WEB主机网络监控查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"WEB主机网络监控查询！");
			}
		}
	}
	
	/**
	 * 设置分页信息
	 * @description    
	 * @param pageInfo
	 * @param totalCount       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-4-23 下午04:36:59
	 */
	public void pageSetList(PageInfo pageInfo,int totalCount ) {
		//当前页数
		int pageSize = pageInfo.getPageSize();
		//总页数
		int totalPage = totalCount % pageSize == 0 ? totalCount
				/ pageSize : totalCount / pageSize + 1;
		pageInfo.setTotalRec(totalCount);
		pageInfo.setTotalPage(totalPage);
		//如果当前页数大于最大页数，则跳转到第一页
		if (pageInfo.getPageIndex() > totalPage)
		{
			pageInfo.setPageIndex(1);
		}
	}
	
	/**
	 * 点击设置获取告警信息
	 * @description    
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-4-29 下午02:14:58
	 */
	public void getNetWarn(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		//增加操作日志
		Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
		try
		{
				//根据通道号查询
				List<LfMonHnetwarn> list = baseBiz.getByCondition(LfMonHnetwarn.class, null, null);
				if(list!=null&&list.size()>0){
					request.setAttribute("hnetwarn", list.get(0));
				}else{
					request.setAttribute("hnetwarn", null);
				}
				if(loginSysuserObj!=null)
				{
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String opContent=OPMODULE+ " 设置获取告警信息成功";
					EmpExecutionContext.info(OPMODULE, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, "update");
				}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,OPMODULE+ " 设置获取告警信息失败");
		}finally{
			request.getRequestDispatcher(this.empRoot + base + "/mon_setAlrm.jsp").forward(request, response);
		}
	}

	
	
	/**
	 * 点击设置获取告警信息
	 * @description    
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-4-29 下午02:14:58
	 */
	public void setNetWarn(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		String result = null;
		//手机号
		String monphone=request.getParameter("monphone");
		//邮箱地址
		String monemail=request.getParameter("monemail");
		boolean opFlag = false;
		String OpStatus = null;
		try
		{
			//根据通道号查询
			List<LfMonHnetwarn> list = baseBiz.getByCondition(LfMonHnetwarn.class, null, null);
			if(list!=null&&list.size()>0){
				//如果存在则修改
				LfMonHnetwarn hostwarn=list.get(0);
				hostwarn.setUpdatetime(new Timestamp(System.currentTimeMillis()));
				if(monphone!=null&&!"".equals(monphone)){
					hostwarn.setMonphone(monphone);
				}
				if(monemail!=null&&!"".equals(monemail)){
					hostwarn.setMonemail(monemail);
				}else{
					hostwarn.setMonemail(" ");
				}
				new BaseBiz().updateObj(hostwarn);
				
			}else{
				//如果不存在则添加
				LfMonHnetwarn hostwarn=new LfMonHnetwarn();
				hostwarn.setCreatetime(new Timestamp(System.currentTimeMillis()));
				hostwarn.setUpdatetime(new Timestamp(System.currentTimeMillis()));
				if(monphone!=null&&!"".equals(monphone)){
					hostwarn.setMonphone(monphone);
				}
				if(monemail!=null&&!"".equals(monemail)){
					hostwarn.setMonemail(monemail);
				}else{
					hostwarn.setMonemail(" ");
				}
				new BaseBiz().addObj(hostwarn);
			}
			opFlag = true;
			OpStatus = "成功";
		} catch (Exception e)
		{
			opFlag = false;
			OpStatus = "失败";
		}finally{
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String opContent=OPMODULE+ " 设置告警信息"+OpStatus;
					EmpExecutionContext.info(OPMODULE, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, "update");
			}
			if (opFlag){
					result="true";
			}else{
					result="false";
			}
			response.getWriter().print(result);
		}
	}
	
	
	
	/**
	 * 检查告警手机号码是否合法
	 * @description    
	 * @param request
	 * @param response
	 * @throws IOException       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-4-23 下午02:48:12
	 */
	public void checkPhone(HttpServletRequest request, HttpServletResponse response ) throws IOException
	{
		String result="error";
		try
		{
			String monphone = request.getParameter("monphone");
			if(monphone==null||monphone.length()==0)
			{
				result = "success";
				return;
			}
			String [] phone = monphone.split(",");
			if(phone.length==0)
			{
				result = "phoneError";
				return;
			}
			//获取运营商号码段
			String[] haoduan = new WgMsgConfigBiz().getHaoduan();
			PhoneUtil phoneUtil = new PhoneUtil();
			for(int i=0;i<phone.length;i++)
			{
				if(phoneUtil.getPhoneType(phone[i], haoduan)==-1)
				{
					result = "phoneError";
					return;
				}
			}
			result="success";
		}
		catch (Exception e) {
			result="error";
			EmpExecutionContext.error(e,"验证告警手机号码异常！");
		}
		finally
		{
			response.getWriter().write(result) ;
		}
	}
	
	
	
	/**
	 * 修改web主机网络监控的状态
	 * @description    
	 * @param request
	 * @param response
	 * @throws IOException       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-5-5 下午02:19:54
	 */
	public void changeState(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try {
			String monid = request.getParameter("monid");
			if(monid==null||"".equals(monid)){
				EmpExecutionContext.error("修改WEB主机网络监控传入ID异常！");
				response.getWriter().print("false");
				return;
			}
			//1.监控，0.未监控
			String monstatus = request.getParameter("monstatus");
			if(monstatus==null||"".equals(monstatus)){
				EmpExecutionContext.error("修改WEB主机网络监控传入监控状态异常！");
				response.getWriter().print("false");
				return;
			}
			LfMonHostnet hostnet=baseBiz.getById(LfMonHostnet.class, monid);
			if(hostnet==null){
				EmpExecutionContext.error("修改WEB主机网络监控获取对象异常！");
				response.getWriter().print("false");
				return;
			}
			//设置监控状态
			hostnet.setMonstatus(Integer.valueOf(monstatus));
			//改数据库
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			conditionMap.put("webnode", hostnet.getWebnode()+"");
			conditionMap.put("procenode", hostnet.getProcenode()+"");
			conditionMap.put("procetype", hostnet.getProcetype()+"");
			objectMap.put("monstatus", monstatus);
			boolean result=baseBiz.update(LfMonHostnet.class, objectMap, conditionMap);
			//拼取缓存中的key
			String key=hostnet.getWebnode()+""+hostnet.getProcenode()+""+hostnet.getProcetype();
			//设置缓存中的
			if(MonitorStaticValue.getHostNetMap() != null&&MonitorStaticValue.getHostNetMap().get(key)!=null)
			{
				MonitorStaticValue.getHostNetMap().put(key,  hostnet);
			}
			
			if(MonitorStaticValue.getHostNetMapTemp() != null&&MonitorStaticValue.getHostNetMapTemp().get(key)!=null)
			{
				MonitorStaticValue.getHostNetMapTemp().put(key,  hostnet);
			}
			
			response.getWriter().print(result);
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "更改WEB主机网络监控状态"+(result==true?"成功":"失败")+"。[监控状态]("+monstatus+")";
				EmpExecutionContext.info("WEB主机网络监控", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}	
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"修改WEB主机网络监控状态异常！");
			response.getWriter().print("false");
		}
	}
	
	
	
	
}
