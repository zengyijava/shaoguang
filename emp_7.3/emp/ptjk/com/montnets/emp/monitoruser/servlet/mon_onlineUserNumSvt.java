/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-2 上午11:06:28
 */
package com.montnets.emp.monitoruser.servlet;

import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitoronline.LfMonOnlcfg;
import com.montnets.emp.entity.monitoronline.LfMonOnluser;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.mondetails.biz.PrcMonBiz;
import com.montnets.emp.util.PageInfo;

/**
 * @description 在线用户查询
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-2 上午11:06:28
 */

public class mon_onlineUserNumSvt extends BaseServlet
{
	final String						empRoot				= "ptjk";

	final String						base				= "/monitor";

	final BaseBiz						baseBiz				= new BaseBiz();

	/**
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-2 下午12:02:06
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * @description 
	 * @param request
	 * @param response
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-2 下午12:37:08
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		//从内存获取数据
		if(StaticValue.getMonOnlinecfg().getId() != null)
		{
			request.setAttribute("onlUser", StaticValue.getMonOnlinecfg());
			try
			{
				request.getRequestDispatcher(this.empRoot + base + "/mon_onlineUserNumMonitor.jsp").forward(request, response);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "跳转至用户在线数页面失败！");
			}
		}
		//内存获取数据异常,查询数据库
		else
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
					request.getRequestDispatcher(this.empRoot + base + "/mon_onlineUserNumMonitor.jsp").forward(request, response);
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "跳转至用户在线数页面失败！");
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @description  在线用户数详情  
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-12-3 下午04:33:09
	 */
	public void getOnlineUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		//设置分页
		PageInfo pageInfo = new PageInfo();
		try
		{
			pageSet(pageInfo, request);
			//String corpCode = request.getParameter("corpCode");
			String userName = request.getParameter("userName");
			String name = request.getParameter("name");
			String depName = request.getParameter("depName");
			//从缓存中获取登录用户信息
			Collection<LfMonOnluser> onlUserDetail = StaticValue.mon_OnlineUserMap.values();
			if(onlUserDetail == null || onlUserDetail.size() < 1)
			{
				onlUserDetail = StaticValue.mon_OnlineUserMapTemp.values();
			}
			List<LfMonOnluser> userDetails = new ArrayList<LfMonOnluser>();
			List<LfMonOnluser> pageList = new ArrayList<LfMonOnluser>();
			//获取系统所有的操作员名称及机构信息
			List<DynaBean> beanList = new PrcMonBiz().getUserAndDep();
			LinkedHashMap<String, String> userMap = new LinkedHashMap<String, String>();
			LinkedHashMap<Long, String> depMap = new LinkedHashMap<Long, String>();
			if(beanList!=null&& beanList.size()>0)
			{
				for(int i=0;i<beanList.size();i++)
				{
					DynaBean bean = beanList.get(i);
					userMap.put(bean.get("user_name").toString(), bean.get("name").toString());
					depMap.put(Long.parseLong(bean.get("dep_id").toString()), bean.get("dep_name").toString());
				}
			}
			for(LfMonOnluser user:onlUserDetail){
	/*			if(corpCode!=null&&!"".equals(corpCode)&&!corpCode.equals(user.getCorpcode())){
					continue;
				}*/
				if(userName!=null&&!"".equals(userName)){
					if(user.getUsername()==null||"".equals(user.getUsername())){
						continue;
					}
					else if(user.getUsername().toLowerCase().indexOf(userName.toLowerCase())<0)
					{
						continue;
					}
				}
				if(name!=null&&!"".equals(name)){
					if(userMap.get(user.getUsername())==null||"".equals(userMap.get(user.getUsername()))){
						continue;
					}
					else if(userMap.get(user.getUsername()).toLowerCase().indexOf(name.toLowerCase())<0)
					{
						continue;
					}
				}
				if(depName!=null&&!"".equals(depName)){
					if(depMap.get(user.getDepid())==null||"".equals(depMap.get(user.getDepid()))){
						continue;
					}
					else if(depMap.get(user.getDepid()).toLowerCase().indexOf(depName.toLowerCase())<0)
					{
						continue;
					}
				}
				userDetails.add(user);
			}
			//分页设置
			pageSetList(pageInfo,userDetails.size());
			for(int i=(pageInfo.getPageIndex()-1)*pageInfo.getPageSize();i<pageInfo.getPageIndex()*pageInfo.getPageSize()&&i<userDetails.size();i++)
			{
				pageList.add(userDetails.get(i));
			}
			request.setAttribute("pageList", pageList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("userMap", userMap);
			request.setAttribute("depMap", depMap);
			
			//企业编码
			String corpCode = null;
			//当前操作员ID
			String userId = null;
			//当前操作员登录名
			String lgUserName = null;
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
					lgUserName = loginSysuser.getUserName();
				}
			}

			//查询出的数据的总数量
			int totalCount = pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间："+sdf.format(stratTime)+"耗时："+
			(System.currentTimeMillis()-stratTime)+ "ms  数量："+totalCount;
			
			EmpExecutionContext.info("在线用户数详情", corpCode, userId, lgUserName, opContent, "GET");
			
			
			request.getRequestDispatcher(this.empRoot + "/mondetails/mon_onlineUserDetail.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "跳转至用户在线数详情页面失败！");
			request.getRequestDispatcher(this.empRoot + "/mondetails/mon_onlineUserDetail.jsp").forward(request, response);
		}
	}
	/**
	 *  * 设置分页信息
	 * @description    
	 * @param pageInfo
	 * @param totalCount       			 
	 * @author zhangmin
	 * @datetime 2014-1-8 上午11:49:09
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
	 * 用户在线时长
	 * @description    
	 * @param loginTime
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-2-14 下午05:32:54
	 */
	public String getOnlineTime(String loginTime)
	{
		//在线时长
		String onlineTime = "";
		Format format;
		//时间格式
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try
		{
			d = (Date) format.parseObject(loginTime);
		}
		catch (ParseException e)
		{
			EmpExecutionContext.error(e, "转换用户登录时间异常，loginTime:" + loginTime);
			return onlineTime; 
		}
		//在线时长(毫秒)
		Long time = System.currentTimeMillis() - d.getTime();
		//时间转换
	    long minseconds = time % 1000;  
	      
	    time = time /1000;  
	    long seconds = time % 60;  
	      
	    time = time / 60;  
	    long mins = time % 60;  
	      
	    time = time / 60;  
	    long hours = time % 24;  

	    time = time / 24;  
	    long days = time;
	    String timeFormat;
	    //小于1天
	    if(days < 1)
	    {
	    	timeFormat = "%2$02d小时%3$02d分钟%4$02d秒";
	    }
	    else
	    {
	    	timeFormat = "%1$d天%2$02d小时%3$02d分钟%4$02d秒";
	    }
	    //处理后的在线时长
//	    onlineTime = (new Formatter()).format(timeFormat, days,hours,mins,seconds,minseconds).toString();
	    Formatter formatter = null;
	    try{
	    	formatter = new Formatter();
	    	onlineTime = formatter.format(timeFormat, days,hours,mins,seconds,minseconds).toString();
	    }finally{
	    	if(formatter != null){
	    		formatter.close();
	    	}
	    }
	    return onlineTime; 
	}

	
}
