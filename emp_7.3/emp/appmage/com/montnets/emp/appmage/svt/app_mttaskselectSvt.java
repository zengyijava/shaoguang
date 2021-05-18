package com.montnets.emp.appmage.svt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.montnets.emp.appmage.biz.MttaskSelectBiz;
import com.montnets.emp.appmage.biz.MttaskSelectExcelTool;
import com.montnets.emp.appmage.util.FFmpegKit;
import com.montnets.emp.appmage.vo.MttaskDetailVo;
import com.montnets.emp.appmage.vo.MttaskSelectVo;
import com.montnets.emp.appwg.biz.WgMwFileBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.apptask.LfAppMttask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;

/**
 *  APP发送任务查询
 * @project p_appmage
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-6-19 下午02:54:47
 * @description
 */
@SuppressWarnings("serial")
public class app_mttaskselectSvt extends BaseServlet {
	private static MttaskSelectBiz mtsBiz=new MttaskSelectBiz();
	static String empRoot="appmage";
	static String basePath="/morequery";
	static SuperOpLog spLog = new SuperOpLog();
	static BaseBiz baseBiz = new BaseBiz();
	private static String path=new TxtFileUtil().getWebRoot();
	//模板路径
	protected static String  excelPath = path + "appmage/morequery/file/";

	/**
	 * APP发送任务查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		//发送任务查询结果集
		List<MttaskSelectVo> mtsVoList = null;
		//发送任务VO
		MttaskSelectVo msVo = new MttaskSelectVo();
		//分页对象
		PageInfo pageInfo = new PageInfo();
		
		try
		{
			//是否第一次进入
			boolean isFirstEnter = pageSet(pageInfo,request);
			//当前登录用户的企业编码
			String corpCode =request.getParameter("lgcorpcode");
			//当前登录用户操作员ID
			//String  userId =request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userId = SysuserUtil.strLguserid(request);

			if(!isFirstEnter){
				//操作员字符串 逗号隔开
				String userid = request.getParameter("userid");
				//内容类型 
				String msgtype = request.getParameter("msgtype");
				//发送主题
	            String title = request.getParameter("title");
				//开始时间
				String starttime = request.getParameter("sendtime");
				//结束时间
				String endtime = request.getParameter("recvtime");
				//发送状态
				String sendstate=request.getParameter("sendstate");
	            //获取过滤条件
	            userid = (userid != null && userid.length()>0 && !userid.equals("请选择"))?userid.substring(0,userid.lastIndexOf(",")):"";
				//操作员ID
				msVo.setUserids(userid);
				//内容类型
				if(msgtype!=null&&!"".equals(msgtype)){
					msVo.setMsgtype(Integer.parseInt(msgtype));
				}
				//发送主题
				if(title != null && title.length() > 0)
				{
					msVo.setTitle(title);
				}
				
				//发送状态
				if(sendstate!=null&&!"".equals(sendstate)){
					msVo.setSendstate(Integer.parseInt(sendstate));
				}
				
				// 查询创建时间
				if(endtime!=null&&!"".equals(endtime)) msVo.setEndtimestr(endtime);
				if(starttime!=null&&!"".equals(starttime)) msVo.setBigintimestr(starttime);
				msVo.setCorpcode(corpCode);
				
				//设置导出的查询条件
				request.getSession(false).setAttribute("app_sendTime", msVo.getBigintimestr());
				request.getSession(false).setAttribute("app_recvTime", msVo.getEndtimestr());
				request.getSession(false).setAttribute("app_userIds", msVo.getUserids());
				request.getSession(false).setAttribute("app_sendTitle", msVo.getTitle());
				//获取当前登录用户的数据访问权限
				LfSysuser cursys=baseBiz.getById(LfSysuser.class, userId!=null?Long.parseLong(userId):0l);
				if(cursys==null){
					cursys=new LfSysuser();
					cursys.setUserId(0l);
					cursys.setPermissionType(1);
				}
				//查询LF_app_mttask信息
				mtsVoList = mtsBiz.getMttaskSelectVoWithoutDomination(cursys, msVo, pageInfo);
			}
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.setAttribute("mtList", mtsVoList);
			request.setAttribute("pageInfo", pageInfo);
			request.getSession(false).setAttribute("mttask_msvo", msVo);
			request.getSession(false).setAttribute("mttask_pageInfo", pageInfo);
			
			//当前登录用户的登录名
			Object sysuserObj =  request.getSession(false).getAttribute("loginSysuser");
			String userName = null;
			if(sysuserObj != null)
			{
				LfSysuser sysuser = (LfSysuser)sysuserObj;
				userName = (sysuser != null && sysuser.getUserName()!= null 
						&& !"".equals(sysuser.getUserName())) ? sysuser.getUserName() : null;
			}			
			//查询出的数据的总数量
			int totalCount = pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间：" + sdf.format(stratTime) + " 耗时："+
			(System.currentTimeMillis()-stratTime) + "ms  数量：" + totalCount;
			
			EmpExecutionContext.info("APP发送任务查询", corpCode, userId, userName, opContent, "GET");
			
			//页面跳转
			request.getRequestDispatcher(this.empRoot+this.basePath+"/app_mttaskselect.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "app发送任务查询异常!");
			request.setAttribute("findresult", "-1");
			request.setAttribute("isFirstEnter", true);//回到页面第一次加载时的状态
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+this.basePath+"/app_mttaskselect.jsp").forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1, "app发送任务查询异常!");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1, "app发送任务查询异常!");
			}
		}
	}
	

	/**
	 * 发送任务查询返回时
	 * @param request
	 * @param response
	 */
	public void findAllAppMttask(HttpServletRequest request, HttpServletResponse response)
	{
		List<MttaskSelectVo> mtsVoList = null;
		MttaskSelectVo msVo = new MttaskSelectVo();
		PageInfo pageInfo = new PageInfo();
		try
		{
			//企业编码
			String corpCode =request.getParameter("lgcorpcode");
			//登录用户的userid
			//String  userId =request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userId = SysuserUtil.strLguserid(request);
			
			pageSet(pageInfo,request);
			//操作员
			String userid = request.getParameter("userid");
			//内容类型 
			String msgtype = request.getParameter("msgtype");
			//发送主题
            String title = request.getParameter("title");
			//开始时间
			String starttime = request.getParameter("sendtime");
			//结束时间
			String endtime = request.getParameter("recvtime");
			String skip=request.getParameter("skip");
			
			
            userid = (userid != null && userid.length()>0 && !userid.equals("请选择"))?userid.substring(0,userid.lastIndexOf(",")):"";
          //操作员ID
			msVo.setUserids(userid);
			//内容类型
			if(msgtype!=null&&!"".equals(msgtype)){
				msVo.setMsgtype(Integer.parseInt(msgtype));
			}
			//发送主题
			if(title != null && title.length() > 0)
			{
				msVo.setTitle(title);
			}
			// 查询创建时间
			if(!"".equals(endtime)) msVo.setEndtimestr(endtime);
			if(!("").equals(starttime)) msVo.setBigintimestr(starttime);
			msVo.setCorpcode(corpCode);
			if("true".equals(skip)){
				pageInfo=(PageInfo)request.getSession(false).getAttribute("mttask_pageInfo");
				msVo=(MttaskSelectVo)request.getSession(false).getAttribute("mttask_msvo");
			}
			//设置导出的查询条件
			request.getSession(false).setAttribute("app_sendTime", msVo.getBigintimestr());
			request.getSession(false).setAttribute("app_recvTime", msVo.getEndtimestr());
			request.getSession(false).setAttribute("app_userIds", msVo.getUserids());
			request.getSession(false).setAttribute("app_sendTitle", msVo.getTitle());
			LfSysuser curuser=baseBiz.getById(LfSysuser.class, userId!=null?Long.parseLong(userId):0l);
			if(curuser==null){
				curuser=new LfSysuser();
				curuser.setUserId(0l);
				curuser.setPermissionType(1);
			}
			//查询LF_app_mttask信息
			mtsVoList = mtsBiz.getMttaskSelectVoWithoutDomination(curuser, msVo, pageInfo);
			request.setAttribute("isFirstEnter", false);
			request.setAttribute("mtList", mtsVoList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("skip", skip);
			request.setAttribute("msVo", msVo);
			//页面跳转
			request.getRequestDispatcher(this.empRoot+this.basePath+"/app_mttaskselect.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "返回时发送任务查询异常!");
			request.setAttribute("findresult", "-1");
			//回到页面第一次加载时的状态
			request.setAttribute("isFirstEnter", true);
			request.setAttribute("pageInfo", pageInfo);
			try {
				//页面跳转
				request.getRequestDispatcher(this.empRoot+this.basePath+"/app_mttaskselect.jsp")
					.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1, "返回时发送任务查询异常!");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1, "返回时发送任务查询异常!");
			}
		}
	}
	
	/**
	 * APP发送任务查询详情信息查看
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void findAllSendInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{	
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		PageInfo pageInfos = new PageInfo();
		try {	
			pageSet(pageInfos,request);
			//获取页面传过来的lf_mttask表中的taskid
            String taskid  = request.getParameter("taskid");
            //内容类型
            String msgtype  = request.getParameter("msgtype");
            //获取页面传过来的查询条件
            //用户app帐号
            String appacount = request.getParameter("appacount");
            //昵称
            String appacountname = request.getParameter("appacountname");
            //发送状态
            String sendstate = request.getParameter("sendstate");
            //发送状态
            String rptstate = request.getParameter("rptstate");
            //企业编码
            String corpcode=request.getParameter("lgcorpcode");
            //查询对象
            MttaskDetailVo mdv=new MttaskDetailVo();
            //任务ID
            if(taskid!=null&&!"".equals(taskid)){
            	mdv.setTaskid(taskid);
            }
            //用户APP帐号
            if(appacount!=null&&!"".equals(appacount)){
            	mdv.setAppuseraccount(appacount);
            }
            //昵称
            if(appacountname!=null&&!"".equals(appacountname)){
            	mdv.setAppusername(appacountname);
            }
            //发送状态
            if(sendstate!=null&&!"".equals(sendstate)){
            	//成功
            	if("1".equals(sendstate)){
            		mdv.setSendstate("'0'");
            	}else if("2".equals(sendstate)){
            		//失败
            		mdv.setSendstate("'1'");
            	}else if("3".equals(sendstate)){
            		//未返
            		mdv.setSendstate("-2");
            	}
            	
            }
            //回执状态
            if(rptstate!=null&&!"".equals(rptstate)){
            	mdv.setRptstate(rptstate);
            }
            
            mdv.setCorpcode(corpcode);
            //发送详情
    		List<MttaskDetailVo> mtdetailVo= mtsBiz.getMtTaskTwo(mdv, pageInfos);
    		
    		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //如果不是10000用户登录，则需要带上企业编码查询
			if(corpcode !=null && !corpcode.equals("100000"))
			{
				conditionMap.put("corpcode", corpcode);
			}
//          查找微信客户
//			List<LfAppMwClient> clientList = baseBiz.getByCondition(
//					LfAppMwClient.class, conditionMap, null);
//    		
			request.setAttribute("mtList",mtdetailVo);
			//request.setAttribute("clientList",clientList);
			request.getSession(false).setAttribute("appacountname", appacountname);
			//分页信息
			request.setAttribute("pageInfo", pageInfos);	
			
			//当前登录用户的登录名
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String lguserName = (sysuser != null && sysuser.getUserName()!= null 
					&& !"".equals(sysuser.getUserName())) ? sysuser.getUserName() : null;
			long lguserid = (sysuser != null && sysuser.getUserId()!= null) ? sysuser.getUserId() : null;
			//查询出的数据的总数量
			int totalCount = pageInfos.getTotalRec();
			//日志信息
			String opContent = "开始时间：" + sdf.format(stratTime) + " 耗时："+
			(System.currentTimeMillis()-stratTime) + "ms  数量：" + totalCount;
			
			EmpExecutionContext.info("APP发送任务查询详情查看", corpcode, String.valueOf(lguserid), lguserName, opContent, "GET");
			
			//页面跳转
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/app_sendrecord.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "APP发送任务查询详情信息查看信息异常!");
			request.setAttribute("findresult", "-1");
			//分页信息
			request.setAttribute("pageInfo", pageInfos);
			try {		
				//页面跳转
				request.getRequestDispatcher(this.empRoot  + this.basePath+"/app_sendrecord.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1, "APP发送任务查询详情信息查看页面跳转异常!");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1, "APP发送任务查询详情信息查看异常!");
			}
		}
		
	}
	
	
	
	/**
	 * APP发送任务查询详情的excel导出方法
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void smsReportAllExcel(HttpServletRequest request,HttpServletResponse response)throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//导出开始时间
		long startTime = System.currentTimeMillis();
	    //企业编码
		String corpCode = request.getParameter("lgcorpcode");
		//用户id
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);
		try {
			//获取页面传过来的lf_mttask表中的taskid
            String taskid  = request.getParameter("taskid");
            //获取页面传过来的查询条件
            //用户app帐号
            String appacount = request.getParameter("appacount");
            //昵称
            String appacountname =(String) request.getSession(false).getAttribute("appacountname");
            //删除
            //发送状态
            String sendstate = request.getParameter("sendstate");
            //发送状态
            String rptstate = request.getParameter("rptstate");
            //内容类型
            String msgtype=request.getParameter("msgtype");
            if(msgtype==null||"".equals(msgtype)){
            	msgtype="0";
            }
            //查询对象
            MttaskDetailVo mdv=new MttaskDetailVo();
            //任务ID
            if(taskid!=null&&!"".equals(taskid)){
            	mdv.setTaskid(taskid);
            }
            //用户APP帐号
            if(appacount!=null&&!"".equals(appacount)){
            	mdv.setAppuseraccount(appacount);
            }
            //昵称
            if(appacountname!=null&&!"".equals(appacountname)){
            	mdv.setAppusername(appacountname);
            }
            
            //发送状态
            if(sendstate!=null&&!"".equals(sendstate)){
            	//成功
            	if("1".equals(sendstate)){
            		mdv.setSendstate("'0'");
            	}else if("2".equals(sendstate)){
            		//失败
            		mdv.setSendstate("'1'");
            	}else if("3".equals(sendstate)){
            		//未返
            		mdv.setSendstate("-2");
            	}
            }
            	
            //回执状态
            if(rptstate!=null&&!"".equals(rptstate)){
            	mdv.setRptstate(rptstate);
            }
            mdv.setCorpcode(corpCode);
            //发送详情
    		List<MttaskDetailVo> mtdetailList= mtsBiz.getMtTaskTwo(mdv, null);;
    		
    		//查询出来的记录不为空时，去创建需要导出的excel
			if (mtdetailList != null && mtdetailList.size()>0 ) 
			{
				MttaskSelectExcelTool et=new MttaskSelectExcelTool(excelPath);
				String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);	
				Map<String, String> resultMap = et.createSmsMtReportExcel(mtdetailList,msgtype,request);
				
				request.getSession(false).setAttribute("app_mttaskselectDetilMap", resultMap);
				
				//String fileName=(String)resultMap.get("FILE_NAME");
		        //String filePath=(String)resultMap.get("FILE_PATH");
		     // 增加操作日志
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if (loginSysuserObj != null) {
					LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
					String opContent = "导出成功, 开始时间："+ sdf.format(startTime) + " 耗时："+(System.currentTimeMillis()-startTime)+"ms" 
					+"导出数量：" + mtdetailList.size();
					EmpExecutionContext.info("APP发送任务查询详情(导出)", loginSysuser.getCorpCode(),
							loginSysuser.getUserId().toString(), loginSysuser
									.getUserName(), opContent, "OTHER");
				}
				PrintWriter out = response.getWriter();
				out.println("true");
		       // DownloadFile dfs=new DownloadFile();
		       // dfs.downFile(request, response, filePath, fileName);
		        //用于判断是否下载加载完成了
		        request.getSession(false).setAttribute("checkOver"+userId, "true");	       
			}
			else
			{
				response.sendRedirect(request.getContextPath()+"/app_mttaskselect.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
		} catch (Exception e) {		
				EmpExecutionContext.error(e, "APP发送任务查询详情异常!");
			   //异常打印
			   response.sendRedirect(request.getContextPath()+"/app_mttaskselect.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
		} 
	}

	
	
	
	
	
	/**
	 * 发送任务查询的excel导出方法(导出全部)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void ReportCurrPageExcel(HttpServletRequest request,HttpServletResponse response)throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//导出开始时间
		long stratTime = System.currentTimeMillis();
		//下行短信list
		List<MttaskSelectVo> mtVoList = null;
		MttaskSelectVo msVo = new MttaskSelectVo();
		//企业编码
		String corpCode = request.getParameter("lgcorpcode");
		//用户id
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);

        try
        {
			//内容类型 
			String msgtype = request.getParameter("msgtype");
			//发送状态
			String sendstate = request.getParameter("sendstate");
            //操作员
			String userids = (String)request.getSession(false).getAttribute("app_userIds");
			//发送主题
			String title = (String)request.getSession(false).getAttribute("app_sendTitle");
			//开始时间
			String starttime = (String)request.getSession(false).getAttribute("app_sendTime");
			//结束时间
			String endtime = (String)request.getSession(false).getAttribute("app_recvTime");
			//操作员ID
			if(userids!=null&&!"".equals(userids)){
				msVo.setUserids(userids);
			}
			//内容类型
			if(msgtype!=null&&!"".equals(msgtype)){
				msVo.setMsgtype(Integer.parseInt(msgtype));
			}
			//发送主题
			if(title != null && title.length() > 0)
			{
				msVo.setTitle(title);
			}
			//发送状态
			if(sendstate!=null&&!"".equals(sendstate)){
				msVo.setSendstate(Integer.parseInt(sendstate));
			}
			// 查询创建时间
			if(endtime!=null&&!"".equals(endtime)) msVo.setEndtimestr(endtime);
			if(starttime!=null&&!"".equals(starttime)) msVo.setBigintimestr(starttime);
			msVo.setCorpcode(corpCode);
			LfSysuser curuser=baseBiz.getById(LfSysuser.class, userId!=null?Long.parseLong(userId):0l);
			if(curuser==null){
				curuser=new LfSysuser();
				curuser.setUserId(0l);
				curuser.setPermissionType(1);
			}
			mtVoList = mtsBiz.getMttaskSelectVoWithoutDomination(curuser,msVo, null);
			
			if (mtVoList != null && mtVoList.size()>0 ) 
			{
				
				MttaskSelectExcelTool et = new MttaskSelectExcelTool(excelPath);
				String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);	
				Map<String, String> resultMap = et.createMtReportExcel(mtVoList,request);
				
				request.getSession(false).setAttribute("app_mttaskselectResultMap", resultMap);
				
				//String fileName=(String)resultMap.get("FILE_NAME");
		        //String filePath=(String)resultMap.get("FILE_PATH");
		     // 增加操作日志
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if (loginSysuserObj != null) {
					LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
					String opContent = "导出成功,开始时间：" +sdf.format(stratTime)
					+" 耗时："+(System.currentTimeMillis()-stratTime)+"ms  导出数量："+ mtVoList.size() ;
					
					EmpExecutionContext.info("APP发送任务查询(导出)", loginSysuser.getCorpCode(),
							loginSysuser.getUserId().toString(), loginSysuser
									.getUserName(), opContent, "OTHER");
				}
		       // DownloadFile dfs=new DownloadFile();
		       // dfs.downFile(request, response, filePath, fileName);
				PrintWriter out = response.getWriter();
				out.println("true");
			}
			else
			{
				 response.sendRedirect(request.getContextPath()+"/app_mttaskselect.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
			
		} catch (Exception e) {		
			   //异常打印
				response.sendRedirect(request.getContextPath()+"/app_mttaskselect.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			    EmpExecutionContext.error(e, "APP发送任务查询的excel导出异常!");
		} 
	}
	/**
	 * APP发送任务查询的excel导出
	 * @Title: downloadFile
	 * @Description: TODO
	 * @param  
	 * @return void    返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void downloadFile(HttpServletRequest request,HttpServletResponse response)throws Exception{
		try {
			Object resultMapObj1 = request.getSession(false).getAttribute("app_mttaskselectResultMap");
			Object resultMapObj2 = request.getSession(false).getAttribute("app_mttaskselectDetilMap");
			if(resultMapObj1 != null)
			{
				Map<String, String> resultMap = (Map<String, String>) resultMapObj1;
				//文件名
				String fileName=(String)resultMap.get("FILE_NAME");
				//文件路径
			    String filePath=(String)resultMap.get("FILE_PATH");
				
			    DownloadFile dfs=new DownloadFile();
			    dfs.downFile(request, response, filePath, fileName);
			    request.getSession(false).removeAttribute("app_mttaskselectResultMap");
			}
			if(resultMapObj2 != null)
			{
				Map<String, String> resultMap = (Map<String, String>) resultMapObj2;
				//文件名
				String fileName=(String)resultMap.get("FILE_NAME");
				//文件路径
				String filePath=(String)resultMap.get("FILE_PATH");
				
				DownloadFile dfs=new DownloadFile();
				dfs.downFile(request, response, filePath, fileName);
				request.getSession(false).removeAttribute("app_mttaskselectDetilMap");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			EmpExecutionContext.error(e, "APP发送任务查询的excel导出异常!");
		}
		
	}

	/**
	 * 操作员树的加载方法
	 * @param titlePath
	 * @param depId
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String getDeptUserJosnData(String titlePath,Long depId,Long userid,String corpCode, HttpServletRequest request) throws Exception{
		StringBuffer tree = null;
		//根据userid获取当前操作员信息
		LfSysuser currUser = baseBiz.getById(LfSysuser.class, userid);
		if(currUser.getPermissionType()==1)
		{
			tree=new StringBuffer("[]");
		}else
		{
			List<LfDep> lfDeps;
			List<LfSysuser> lfSysusers = null;
			
			DepBiz depBiz = new DepBiz();
			try {
				//如果企业编码是10000的用户登录
				if(currUser.getCorpCode().equals("100000"))
				{
					if(depId == null)
					{
						LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
						LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
						//只查询顶级机构
						conditionMap.put("superiorId", "0");
						//查询未删除的机构
						conditionMap.put("depState", "1");
						//排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);	
						lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
					}
					else
					{
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,corpCode);
					}
					
					
				}else
				{
					if(depId == null){
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userid,corpCode).get(0);
						lfDeps.add(lfDep);						
					}else{
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,corpCode);
					}
				}
				//拼结机构树
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",isParent:").append(true);
					tree.append(",nocheck:").append(true);
					tree.append("}");			
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				
				if(depId != null)
				{
					//如果当前登录用户的企业编码是10000
					if(currUser.getCorpCode().equals("100000"))
					{
						LinkedHashMap<String,String> conMap = new LinkedHashMap<String,String>();
						conMap.put("userId&<>","1" );
	                    conMap.put("depId", depId.toString());
						lfSysusers = baseBiz.getByCondition(LfSysuser.class, conMap, null);
					}else
					{
                        //获取所有状态操作员信息
						lfSysusers = mtsBiz.getAllSysusersOfSmsTaskRecordByDep(userid,depId);
						
					}
				}
				//拼结操作员信息
				LfSysuser lfSysuser = null;
				if(lfSysusers != null && !lfSysusers.isEmpty()){
					if(lfDeps !=null && lfDeps.size()>0)
					{
					  tree.append(",");
					}
					
					for (int i = 0; i < lfSysusers.size(); i++) {
						//操作员信息
						lfSysuser = lfSysusers.get(i);
						tree.append("{");
						tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
						tree.append(",name:'").append(lfSysuser.getName()).append("'");
						if(lfSysuser.getUserState()==2)
						{
							tree.append(",name:'").append(lfSysuser.getName()).append(MessageUtils.extractMessage("appmage", "appmage_smstask_text_36", request)).append("'");;
						}else
						{
							tree.append(",name:'").append(lfSysuser.getName()).append("'");
						}
						tree.append(",pId:").append(lfSysuser.getDepId());
						tree.append(",depId:'").append(lfSysuser.getDepId()+"'");
						tree.append(",isParent:").append(false);
						tree.append("}");
						if(i != lfSysusers.size()-1){
							tree.append(",");
						}
					}
				}
				
				tree.append("]");
			} catch (Exception e) {
				EmpExecutionContext.error(e, "操作员树的加载异常!");
			}
		}
		if(tree == null){
			tree = new StringBuffer("[]");;
		}
		return tree.toString();
	}

	
		

	/**
	 * 群发历史页面获取操作员树的方法 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createUserTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try
		{		
			Long depId = null;
			String titlePath="";
			String depStr = request.getParameter("depId");
			//String userid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userid = SysuserUtil.strLguserid(request);

			String corpCode="";
			LfSysuser user= getLoginUser(request);
			if(user!=null){
				corpCode=user.getCorpCode();
			}
			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			//调用公用创建树的方法
			String departmentTree = getDeptUserJosnData(titlePath,depId,Long.parseLong(userid),corpCode,request);
			response.getWriter().print(departmentTree);
		}
		catch (Exception e) 
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e, "App发送任务获取操作员树异常!");
		}
	}

	
	/**
	 * 预览
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void prev(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, JSONException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String taskid = request.getParameter("taskid");
		LfAppMttask appmttask = null;
		if(StringUtils.isNotBlank(taskid)&&StringUtils.isNumeric(taskid)){
			try
			{
				LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
				conditionMap.put("taskid", taskid);
				List<LfAppMttask> mttasklist=baseBiz.getByCondition(LfAppMttask.class, conditionMap, null);
				if(mttasklist!=null&&mttasklist.size()>0){
					appmttask = mttasklist.get(0);
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "查询对应任务异常！");
			}
		}
		JSONObject json = new JSONObject();
		try
		{
			if(appmttask != null){
				json.put("errcode", 0);
				json.put("title",appmttask.getTitle());
				json.put("msgtype",appmttask.getMsgtype());
				String text="";
				if(appmttask.getMsgtype()==0){
					//text = StringEscapeUtils.escapeHtml(appmttask.getMsg());
					text=appmttask.getMsg();
//					Map<String,String> emos = app_morequeryBiz.getEmoMap();
//					Iterator<String> its = emos.keySet().iterator();
//					while(its.hasNext()){
//						String key = its.next();
//						text = text.replaceAll(key, "<img class=\"emo\" src=\""+this.empRoot+basePath+"/emo/"+emos.get(key)+"\" />");
//					}
					json.put("msgtext",text);
				}else{
					json.put("msgtext",appmttask.getMsg());
				}
				
				if((appmttask.getMsgurl()!=null&&!"".equals(appmttask.getMsgurl()))||(appmttask.getMsglocalurl()!=null&&!"".equals(appmttask.getMsglocalurl()))){
					WgMwFileBiz fileBiz = new WgMwFileBiz();
					//请求地址相对路径
					String path = appmttask.getMsglocalurl();
					if(path==null||"".equals(path)){
						path="file"+File.separator+appmttask.getMsgurl();
					}
					//请求文件真实路径
					String realPath = request.getRealPath(path);
					//String extName = realPath.substring(realPath.lastIndexOf(".")).toLowerCase();
					File f = new File(realPath);
					if(!f.exists()){
							//String allImgExt = ".jpg|.jpeg|.gif|.bmp|.png|";
							//if(allImgExt.indexOf(extName)!=-1){
							fileBiz.downloadFromMwFileSer(realPath, appmttask.getMsgurl());
							File f2 = new File(realPath);
							if(!f2.exists()){
								json.put("msgurl","");
							}else{
								if(appmttask.getMsgtype()==2){
									String flvpatch=FFmpegKit.convertPath(path);
									String flvrealPath = request.getRealPath(flvpatch);
									File flvf = new File(flvrealPath);
									if(!flvf.exists()){
										FFmpegKit.converVideo(realPath);
									}
									path=flvpatch;
								}else if(appmttask.getMsgtype()==3){
									String flvpatch=FFmpegKit.convertPath(path);
									String flvrealPath = request.getRealPath(flvpatch);
									File flvf = new File(flvrealPath);
									if(!flvf.exists()){
										FFmpegKit.converAudio(realPath);
									}
									path=flvpatch;
								}
								json.put("msgurl",path);
							}
							//}else if(){
							//	fileBiz.do
							//}else{
							//	json.put("msgurl","");
							//}
					}else{
						if(appmttask.getMsgtype()==2){
							String flvpatch=FFmpegKit.convertPath(path);
							String flvrealPath = request.getRealPath(flvpatch);
							File flvf = new File(flvrealPath);
							if(!flvf.exists()){
								FFmpegKit.converVideo(realPath);
							}
							path=flvpatch;
						}else if(appmttask.getMsgtype()==3){
							String flvpatch=FFmpegKit.convertPath(path);
							String flvrealPath = request.getRealPath(flvpatch);
							File flvf = new File(flvrealPath);
							if(!flvf.exists()){
								FFmpegKit.converAudio(realPath);
							}
							path=flvpatch;
						}
						json.put("msgurl",path);
					}

				}else{
					json.put("msgurl","");
				}
				
				
			}else{
				json.put("errcode", -1);
				json.put("errmsg", "未找到相应记录！");
			}
		}
		catch (Exception e)
		{
			json.put("errcode", -1);
			json.put("errmsg", "处理数据出现错误！");
		}finally{
			out.print(json.toString());
		}
		
	}
	
	
	
	
	protected int getIntParameter(HttpServletRequest request,String param, int defaultValue)
	{
		try
		{
			return Integer.parseInt(request.getParameter(param));
		} catch (NumberFormatException e)
		{
			//异常处理
			EmpExecutionContext.error(e,"数字转换异常");
			return defaultValue;
		}
	}
	
	
}