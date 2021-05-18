package com.montnets.emp.shorturl.surlmanage.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.util.StringUtils;
import org.apache.log4j.chainsaw.Main;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Data;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.shorturl.surlmanage.biz.UrlDomianBindBiz;
import com.montnets.emp.shorturl.surlmanage.biz.UrlDomianBiz;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomain;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomainCorp;
import com.montnets.emp.shorturl.surlmanage.vo.LfDomainCorpVo;
import com.montnets.emp.shorturl.surlmanage.vo.view.ViewLfDomainCorpVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

@SuppressWarnings("serial")
public class Surl_domainBindSvt  extends BaseServlet{
	//公共biz类实例化
	final BaseBiz baseBiz=new BaseBiz();
	final UrlDomianBindBiz urlbiz = new UrlDomianBindBiz();
	
	//页面模块文件名称
	private final String empRoot = "shorturl";
	//页面功能文件名称
	private final String basePath ="/urldomainBind";
	
	final ErrorLoger errorLoger = new ErrorLoger();
	//操作模块
	final String opModule="短域名绑定管理";
	//操作用户
	final String opSper = StaticValue.OPSPER;
	/**
	 * 查看
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {

		//查询结果模板volist
		List<LfDomainCorpVo> urlList = null;
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(10);
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			//初始化分页信息
			pageSet(pageInfo, request);
			//企业编号
			String corpCode = request.getParameter("corpCode");
			//企业名称
			String corpName = request.getParameter("corpName");
            //状态
			String flag = request.getParameter("flag");
			//短域名
			String domain = request.getParameter("domain");
			//创建开始时间
			String startTime = request.getParameter("startTime");
			//创建结束时间
			String recvtime = request.getParameter("recvtime");
			
			
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("corpName", corpName);
			conditionMap.put("flag", flag);
			conditionMap.put("domain", domain);
			conditionMap.put("startTime", startTime);
			conditionMap.put("recvtime", recvtime);
			

			
			urlList = urlbiz.getDomainCorpVos(conditionMap, pageInfo);
			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("urlList", urlList);
			request.setAttribute("conditionMap", conditionMap);
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/url_domianBind.jsp")
			.forward(request, response);
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"短链报备查询异常！");
			// 异常错误
			request.getSession(false).setAttribute("error", e);
			request.setAttribute("findresult", "-1");
			// 分页信息
			request.setAttribute("pageInfo", pageInfo);
			try
			{
				request.getRequestDispatcher(this.empRoot + this.basePath + "/url_domianBind.jsp").forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(e1, "短链报备查询后跳转异常");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1, "短链报备查询后跳转IO异常");
			}
		}
	}
	
	/**
	 * 进入新建短域名绑定界面
	 * @param request
	 * @param response
	 */
	public void toAdd(HttpServletRequest request, HttpServletResponse response)
	{
		List<LfDomain> urlListadd = null;
		List<LfCorp> lfCorpList = null;
		List<String> corpCodeList = new ArrayList<String>();
		List<String> corpNameList = new ArrayList<String>();

		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(10);
		String pageIndex="";
		String pageSize = "";
		String strDomains = "";
		String addcorpCode = "";
		try
		{	
			//初始化分页信息
			pageSet(pageInfo, request);
			//获取短域名全部信息
			//urlListadd = baseBiz.getByCondition(LfDomain.class, null, null);
			addcorpCode = request.getParameter("add");
			pageIndex = request.getParameter("pageIndex");
			pageSize = request.getParameter("pageSize");
			if(pageIndex!=null&&!"".equals(pageIndex)){
				pageInfo.setPageIndex(Integer.valueOf(pageIndex));
			}
			if(pageSize!=null&&!"".equals(pageSize)){
				pageInfo.setPageSize(Integer.valueOf(pageSize));
			}
			
			//查询出短信域名(被绑定的独享专用的短信域名不显示)
			strDomains = getUniqDomains(request, response); 
			//根据域名id查找域名对象
			if(strDomains!=null&&!"".equals(strDomains)){
				urlListadd = urlbiz.getDomainsPageinfo(strDomains, pageInfo);
				
			}
			//获取所有的企业编号
			lfCorpList = baseBiz.getByCondition(LfCorp.class, null, null);
			if(lfCorpList!=null&&lfCorpList.size()>0){
				for (int i = 0; i < lfCorpList.size(); i++) {
					corpCodeList.add(lfCorpList.get(i).getCorpCode());
					corpNameList.add(lfCorpList.get(i).getCorpName());
				}
			}
			
			request.setAttribute("corpCodeList", corpCodeList);
			request.setAttribute("addcorpCode", addcorpCode);
			request.setAttribute("corpNameList", corpNameList);
			//urlListadd = baseBiz.getByCondition(LfDomain.class, null, null, null, pageInfo);
			request.setAttribute("urlListadd", urlListadd);
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(empRoot  + basePath +"/url_addDomianBind.jsp")
				.forward(request, response);
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"新建短域名页面跳转异常！");
		}
	}
	
	/**
	 * 显示短信域名（被绑定的独享专用的短信域名除外）
	 */
	public String getUniqDomains(HttpServletRequest request, HttpServletResponse response)
	{
		List<LfDomain> urlList = null;
		List<LfDomain> domains = null;
		List<String> idList = new ArrayList<String>();
		List<String> idList2 = new ArrayList<String>();
		List<LfDomainCorp> urlDomainCorpList = null;

		List<String> idList02 = new ArrayList<String>();
		List<String> idList3 = new ArrayList<String>();
		List<String> idList4 = new ArrayList<String>();
		String userStr= "";
		
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{	
			//查询所有短域名中应用类型为独享专用的id
			conditionMap.clear();
			conditionMap.put("dtype", "1");
			urlList = baseBiz.getByCondition(LfDomain.class, conditionMap, null);
			if(urlList!=null&&urlList.size()>0){
				for (int i = 0; i < urlList.size(); i++) {
					idList.add(String.valueOf(urlList.get(i).getId()));
				}
			}
			
			//查看所有被绑定的域名id
			urlDomainCorpList = baseBiz.getByCondition(LfDomainCorp.class, null, null);
			if(urlDomainCorpList!=null&&urlDomainCorpList.size()>0){
				for (int i = 0; i < urlDomainCorpList.size(); i++) {
					idList02.add(String.valueOf(urlDomainCorpList.get(i).getDomainId()));
				}
			}
			
			if(idList02!=null&&idList02.size()>0){
				//对被绑定的域名id进行去重 idList2
				for (int i = 0; i < idList02.size(); i++) {
					if(!idList2.contains(idList02.get(i))){
						idList2.add(idList02.get(i));
					}
				}
			}
			
				
			//查看所有独享专用被绑定的域名id
			if(idList!=null&&idList.size()>0&&idList2!=null&&idList2.size()>0){
				for (int i = 0; i < idList.size(); i++) {
					for (int j = 0; j < idList2.size(); j++) {
						if(idList2.get(j).equals(idList.get(i))){
							idList3.add(idList.get(i));
						}
					}
				}
			}				
			
			//获取短域名全部信息
			domains = baseBiz.getByCondition(LfDomain.class, null, null);
			if(domains!=null&&domains.size()>0){
				for (int i = 0; i < domains.size(); i++) {
					idList4.add(String.valueOf(domains.get(i).getId()));
				}
			}
			
			if(idList3!=null&&idList3.size()>0){
				for (int i = 0; i < idList4.size(); i++) {
					for (int j = 0; j < idList3.size(); j++) {
						if((idList4.get(i)).equals(idList3.get(j))){
							break;
						}else if(!(idList4.get(i)).equals(idList3.get(j))&&j==(idList3.size()-1)){
							userStr = userStr + idList4.get(i) + ",";
						}
					}
				}
			}else{
				for (int i = 0; i < idList4.size(); i++) {
					userStr = userStr + idList4.get(i) + ",";
				}
			}
			
			if(userStr!=null&&!"".equals(userStr)){
				userStr = userStr.substring(0, userStr.length()-1);
			}
			
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"获取短域名中应用类型为独享专用的id异常！");
		}
		return userStr;
	}
	
	/**
	 * 新建短域名
	 * @param request
	 * @param response
	 */
	public void check(HttpServletRequest request, HttpServletResponse response)
	{
		List<LfDomain> urlList = null;
		List<LfDomainCorp> urlDomainCorpList = null;
		List<LfDomainCorp> urlDomainCorpList1 = null;
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		List<String> idList = new ArrayList<String>();
		List<String> idList2 = new ArrayList<String>();
		List<String> idList02 = new ArrayList<String>();
		List<String> idList3 = new ArrayList<String>();
		List<String> idList4 = new ArrayList<String>();
		String idsYes = "";
		String idsNo = "";
		String idsCf = "";
		
		try{
			//获取企业编号
			String addcorpCode = request.getParameter("addcorpCode");
			//获取企业名称
			String addcorpName = request.getParameter("addcorpName");
			//获取选中的域名
			String ids = request.getParameter("ids");
			String[] strIds = ids.split(",");
			
			//获取当前时间
			Timestamp updateTm = new Timestamp(System.currentTimeMillis());
			
			
			//查询短域名中应用类型为独享专用的id
			conditionMap.put("dtype", "1");
			urlList = baseBiz.getByCondition(LfDomain.class, conditionMap, null);
			if(urlList!=null&&urlList.size()>0){
				for (int i = 0; i < urlList.size(); i++) {
					idList.add(String.valueOf(urlList.get(i).getId()));
				}
				
				//查看被绑定的域名id
				urlDomainCorpList = baseBiz.getByCondition(LfDomainCorp.class, null, null);
				if(urlDomainCorpList!=null&&urlDomainCorpList.size()>0){
					for (int i = 0; i < urlDomainCorpList.size(); i++) {
						idList02.add(String.valueOf(urlDomainCorpList.get(i).getDomainId()));
					}
				}
				//对被绑定的域名id进行去重 idList2
				for (int i = 0; i < idList02.size(); i++) {
					if(!idList2.contains(idList02.get(i))){
						idList2.add(idList02.get(i));
					}
				}
				
				
				//查看所有独享专用的域名id是否被绑定
				if(idList!=null&&idList.size()>0&&idList2!=null&&idList2.size()>0){
					for (int i = 0; i < idList.size(); i++) {
						for (int j = 0; j < idList2.size(); j++) {
							if(idList2.get(j).equals(idList.get(i))){
								idList3.add(idList.get(i));
							}
						}
					}
				}
				
			}
			
			//根据企业编号查看已经绑定的域名id  idList4
			conditionMap.clear();
			conditionMap.put("corpCode", addcorpCode);
			urlDomainCorpList1 = baseBiz.getByCondition(LfDomainCorp.class, conditionMap, null);
			if(urlDomainCorpList1!=null&&urlDomainCorpList1.size()>0){
				for (int i = 0; i < urlDomainCorpList1.size(); i++) {
					idList4.add(String.valueOf(urlDomainCorpList1.get(i).getDomainId()));
				}
			}
			

			//检查选中的域名id是否含有独享专用的绑定的域名id
			if(strIds!=null&&idList3!=null&&idList3.size()>0){
				for (int i = 0; i < strIds.length; i++) {
					for (int j = 0; j < idList3.size(); j++) {
						if(strIds[i].equals(idList3.get(j))){
							idsNo = idsNo + strIds[i] + ",";
							break;
						}else if(!strIds[i].equals(idList3.get(j))&&j==(idList3.size()-1)){
							idsYes = idsYes + strIds[i] + ",";
						}
					}
				}
			}else{
				for (int i = 0; i < strIds.length; i++) {
					idsYes = idsYes + strIds[i] + ",";
				}
			}
			
			if (idsNo.length()>0) {
				idsNo = idsNo.substring(0, idsNo.length()-1);
			}
			if (idsYes.length()>0) {
				idsYes = idsYes.substring(0, idsYes.length()-1);
				
				if(idList4==null||idList4.size()==0){
					idsYes = idsYes.substring(0, idsYes.length());	
				}else{
					String[] idsYesStr = idsYes.split(",");
					idsYes = "";
					for (int i = 0; i < idsYesStr.length; i++) {
						for (int j = 0; j < idList4.size(); j++) {
							if(idsYesStr[i].equals(idList4.get(j))){
								idsCf = idsCf + idsYesStr[i] + ",";
							}else if(j==(idList4.size()-1)){
								idsYes = idsYes + idsYesStr[i] + ",";
							}
						}
					}
					if(idsYes.endsWith(",")){
						idsYes = idsYes.substring(0, idsYes.length()-1);
					}
				}
			}
			
			PrintWriter writer = response.getWriter();
			
			if(idsNo!=null&&!"".equals(idsNo)){
				writer.print(idsNo+"n");
			}else if(idsCf!=null&&!"".equals(idsCf)){
				writer.print(idsCf+"c");
			}else {
				writer.print(idsYes+"y");
			}
				
			writer.flush();

		}catch(Exception e){
			//添加失败操作日志
			EmpExecutionContext.error(e,"新建短域名异常");
		}
	}
	
	public void add(HttpServletRequest request, HttpServletResponse response){
		LfDomainCorp lfDomainCorp = null;
		boolean flag = false;
		
		String opUser = "";
		String corpcode = "";
		//日志内容
		String opContent = null;
		//修改前数据
		StringBuffer oldStr=new StringBuffer("");
		//修改后数据
		StringBuffer newStr=new StringBuffer("");
		//修改字段字符串
		String updateContent="";
		SuperOpLog opLog = new SuperOpLog();		
		
		
		try
		{	
			String result = request.getParameter("result");
			//符合要求的被绑定的域名id
			String[] ids = result.substring(0,result.length()-1).split(",");
			//企业编号
			String addcorpCode = request.getParameter("addcorpCode");
			
			//添加成功操作日志
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员id
			Long lguserid = sysuser.getUserId();
			corpcode = sysuser.getCorpCode();
			opUser = sysuser.getUserName();
			
			//创建人
			Long createUid = sysuser.getUserId();
			//修改人
			Long updateUid = sysuser.getUserId();
			
			//将企业编号和域名id插入映射表
			for (int i = 0; i < ids.length; i++) {
				flag=urlbiz.insert(addcorpCode, ids[i],createUid,updateUid,opUser);
			}
			
			
			//获取当前时间
			Date now = new Date(); 
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
			String currentTime = dateFormat.format( now ); 
			
			
			for (int i = 0; i < ids.length; i++) {
				newStr.append(ids[i]).append(",").append(addcorpCode)
				.append(",").append(currentTime).append(",");
			}
			
			if(flag){
				opContent = "短域名id，企业编码,增加时间"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
				setLog(request, "短域名绑定管理", opContent, StaticValue.ADD);
				opLog.logSuccessString(opUser, opModule, StaticValue.ADD, opContent,corpcode);
			}else{
				opContent = "短域名id，企业编码,增加时间"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
				setLog(request, "短域名绑定管理", opContent, StaticValue.ADD);
				opLog.logFailureString(opUser, opModule, StaticValue.ADD, opContent,null,corpcode);
			}
			
			
			response.getWriter().print(flag);
			
			
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"新建短域名页面跳转异常！");
			opContent = "短域名id，企业编码,增加时间"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
			setLog(request, "短域名绑定管理", opContent, StaticValue.ADD);
			opLog.logFailureString(opUser, opModule, StaticValue.ADD, opContent,null,corpcode);
	
		}
	}
	
	/**
	 * 跳转到修改短域名管理页面
	 * @param request
	 * @param response
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response)
	{

		//查询结果模板volist
		LfDomainCorpVo lfDomainCorpVo = null;
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(10);
		String pageIndex="";
		String pageSize = "";
//		String strDomains = "";
//		List<LfDomain> urlList = null;
//		List<String> idList = new ArrayList<String>();
//		List<String> idList5= new ArrayList<String>();
//		List<String> idList6= new ArrayList<String>();
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			//初始化分页信息
			pageSet(pageInfo, request);
			pageIndex = request.getParameter("pageIndex");
			pageSize = request.getParameter("pageSize");
			
			
			String id = request.getParameter("corpid");
			conditionMap.put("corpID", id);
			lfDomainCorpVo = urlbiz.getDomainCorpVos(conditionMap, pageInfo).get(0);
			conditionMap.clear();
			//根据id查找企业编码
			String corpCode = lfDomainCorpVo.getCorpCode();
			//根据企业编码corpCode查找域名id,根据域名id查找域名集合
			conditionMap.put("corpCode", corpCode);

/*
			List<Long> domainBindIds = urlbiz.getDomainBindIds(conditionMap);
			String ids = "";
			if(domainBindIds!=null&&domainBindIds.size()>0){
				for (int i = 0; i < domainBindIds.size(); i++) {
					ids = ids + String.valueOf(domainBindIds.get(i))+",";
				}
			}
			ids = ids.substring(0,ids.length()-1);
			
			//根据域名id查找域名对象
			List<LfDomain> domains = urlbiz.getDomains(ids);
			//获取短域名全部信息
			List<LfDomain> urlListadd = null;
			*/
			if(pageIndex!=null&&!"".equals(pageIndex)){
				pageInfo.setPageIndex(Integer.valueOf(pageIndex));
			}
			if(pageSize!=null&&!"".equals(pageSize)){
				pageInfo.setPageSize(Integer.valueOf(pageSize));
			}
			
			/*
			//查询出短信域名(被绑定的独享专用的短信域名不显示)
			strDomains = getUniqDomains(request, response); 
			//包含被企业被绑定的域名id
			
			
			//查询所有短域名中应用类型为独享专用的id
			conditionMap.clear();
			conditionMap.put("dtype", "1");
			urlList = baseBiz.getByCondition(LfDomain.class, conditionMap, null);
			if(urlList!=null&&urlList.size()>0){
				for (int i = 0; i < urlList.size(); i++) {
					idList.add(String.valueOf(urlList.get(i).getId()));
				}
			}
			//查看该企业中绑定的域名id是否存在独享专用的域名id
			if(idList!=null&&idList.size()>0&&domainBindIds!=null&&domainBindIds.size()>0){
				for (int i = 0; i < idList.size(); i++) {
					for (int j = 0; j < domainBindIds.size(); j++) {
						if(String.valueOf(domainBindIds.get(j)).equals(idList.get(i))){
							idList6.add(idList.get(i));
						}
					}
				}
			}
			
			if(idList6!=null&&idList6.size()>0){
				if(strDomains==null||"".equals(strDomains)){
					for (int i = 0; i < idList6.size(); i++) {
						strDomains = strDomains + idList6.get(i)+",";
					}
				}else{
					strDomains = strDomains + ",";
					for (int i = 0; i < idList6.size(); i++) {
						strDomains = strDomains+ idList6.get(i)+",";
					}
				}
				strDomains = strDomains.substring(0, strDomains.length()-1);
			}

			//根据域名id查找域名对象
			if(strDomains!=null&&!"".equals(strDomains)){
				urlListadd = urlbiz.getDomainsPageinfo(strDomains, pageInfo);
			}
*/
            //1.根据企业编码查询出所有可用的(独享域名未绑定 + 所有公有域名)域名信息
            List<LfDomain> availableDomains = urlbiz.getAvailableDomains(corpCode, pageInfo);

            //2.根据可用域名列表查询出已经绑定的域名信息
            List<LfDomain> bindedDomains = urlbiz.getBindedDomains(corpCode, availableDomains);

			//urlListadd = baseBiz.getByCondition(LfDomain.class, null, null, null, pageInfo);


//			request.setAttribute("urlListadd", urlListadd);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lfDomainCorpVo", lfDomainCorpVo);
//			request.setAttribute("domains", domains);
			request.setAttribute("corpid", id);

            request.setAttribute("urlListadd", availableDomains);
            request.setAttribute("domains", bindedDomains);
            request.setAttribute("bindedIdStr", urlbiz.getBindedIdStrFromList(bindedDomains));

			request.getRequestDispatcher(this.empRoot  + this.basePath+"/url_editDomianCorp.jsp")
			.forward(request, response);
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"短链报备查询异常！");
			// 异常错误
			request.getSession(false).setAttribute("error", e);
			request.setAttribute("findresult", "-1");
			// 分页信息
			request.setAttribute("pageInfo", pageInfo);
			try
			{
				request.getRequestDispatcher(this.empRoot + this.basePath + "/url_editDomianCorp.jsp").forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(e1, "短链报备查询后跳转异常");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1, "短链报备查询后跳转IO异常");
			}
		}
	}
	
	/**
	 * 新建短域名
	 * @param request
	 * @param response
	 */
	public void checkUpdate(HttpServletRequest request, HttpServletResponse response)
	{
		List<LfDomain> urlList = null;
		List<LfDomainCorp> urlDomainCorpList = null;
		List<LfDomainCorp> urlDomainCorpList1 = null;
		List<LfDomainCorp> urlDomainCorpList2 = null;
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		List<String> idList = new ArrayList<String>();
		List<String> idList2 = new ArrayList<String>();
		List<String> idList02 = new ArrayList<String>();
		List<String> idList3 = new ArrayList<String>();
		List<String> idList4 = new ArrayList<String>();
		List<String> idList5= new ArrayList<String>();
		List<String> idList6= new ArrayList<String>();
		List<String> idList06= new ArrayList<String>();
		List<String> idList7= new ArrayList<String>();
		String idsYes = "";
		String idsNo = "";
		
		try{
			//获取企业编号
			String addcorpCode = request.getParameter("addcorpCode");
			//获取企业名称
			String addcorpName = request.getParameter("addcorpName");
			//获取选中的域名
			String ids = request.getParameter("ids");
			String[] strIds = ids.split(",");
			
			//获取当前时间
			Timestamp updateTm = new Timestamp(System.currentTimeMillis());
			
			//TODO查询企业编码已经绑定的短域名中应用类型为独享专用的id，这个要加到可以绑定的域名id里面
			//查找已经被该企业绑定的域名id
			conditionMap.put("corpCode", addcorpCode);
			urlDomainCorpList2 = baseBiz.getByCondition(LfDomainCorp.class, conditionMap, null);
			if(urlDomainCorpList2!=null&&urlDomainCorpList2.size()>0){
				for (int i = 0; i < urlDomainCorpList2.size(); i++) {
					idList5.add(String.valueOf(urlDomainCorpList2.get(i).getDomainId()));
				}
			}
			
			
			
			
			//查询所有短域名中应用类型为独享专用的id
			conditionMap.clear();
			conditionMap.put("dtype", "1");
			urlList = baseBiz.getByCondition(LfDomain.class, conditionMap, null);
			if(urlList!=null&&urlList.size()>0){
				for (int i = 0; i < urlList.size(); i++) {
					idList.add(String.valueOf(urlList.get(i).getId()));
				}
				
				//查看所有被绑定的域名id
				urlDomainCorpList = baseBiz.getByCondition(LfDomainCorp.class, null, null);
				if(urlDomainCorpList!=null&&urlDomainCorpList.size()>0){
					for (int i = 0; i < urlDomainCorpList.size(); i++) {
						idList02.add(String.valueOf(urlDomainCorpList.get(i).getDomainId()));
					}
				}
				//对被绑定的域名id进行去重 idList2
				for (int i = 0; i < idList02.size(); i++) {
					if(!idList2.contains(idList02.get(i))){
						idList2.add(idList02.get(i));
					}
				}
				
				//查看所有独享专用被绑定的域名id
				if(idList!=null&&idList.size()>0&&idList2!=null&&idList2.size()>0){
					for (int i = 0; i < idList.size(); i++) {
						for (int j = 0; j < idList2.size(); j++) {
							if(idList2.get(j).equals(idList.get(i))){
								idList3.add(idList.get(i));
							}
						}
					}
				}
				
				//查看该企业中绑定的域名id是否存在独享专用的域名id
				if(idList!=null&&idList.size()>0&&idList5!=null&&idList5.size()>0){
					for (int i = 0; i < idList.size(); i++) {
						for (int j = 0; j < idList5.size(); j++) {
							if(idList5.get(j).equals(idList.get(i))){
								idList6.add(idList.get(i));
							}
						}
					}
				}
			}
			
			//根据企业编号查看已经绑定的域名id  idList4
			conditionMap.clear();
			conditionMap.put("corpCode", addcorpCode);
			urlDomainCorpList1 = baseBiz.getByCondition(LfDomainCorp.class, conditionMap, null);
			if(urlDomainCorpList1!=null&&urlDomainCorpList1.size()>0){
				for (int i = 0; i < urlDomainCorpList1.size(); i++) {
					idList4.add(String.valueOf(urlDomainCorpList1.get(i).getDomainId()));
				}
			}
			

			//检查选中的域名id是否含有独享专用的绑定的域名id
			if(strIds!=null&&idList3!=null&&idList3.size()>0){
				for (int i = 0; i < strIds.length; i++) {
					for (int j = 0; j < idList3.size(); j++) {
						if(strIds[i].equals(idList3.get(j))){
							idsNo = idsNo + strIds[i] + ",";
							break;
						}else if(!strIds[i].equals(idList3.get(j))&&j==(idList3.size()-1)){
							idsYes = idsYes + strIds[i] + ",";
						}
					}
				}
			}else{
				for (int i = 0; i < strIds.length; i++) {
					idsYes = idsYes + strIds[i] + ",";
				}
			}
			
			//去掉idsNo中该企业绑定的独享专用的域名id
			if (idsNo.length()>0) {
				idsNo = idsNo.substring(0, idsNo.length()-1);
				String[] idsNoStr = idsNo.split(",");
				
				for (int i = 0; i < idsNoStr.length; i++) {
					idList7.add(idsNoStr[i]);
				}
				
				for (int  i= 0;  i< idsNoStr.length; i++) {
					for (int j = 0; j < idList6.size(); j++) {
						if(idsNoStr[i].equals(idList6.get(j))){
							idList7.remove(idsNoStr[i]);
						}
					}
				}
				
				idsNo = "";
				if(idList7!=null&&idList7.size()>0){
					for (int i = 0; i < idList7.size(); i++) {
						idsNo = idsNo + idList7.get(i)+",";
					}
					idsNo = idsNo.substring(0, idsNo.length()-1);
				}
				
			}
			
			//如果独享专用的域名id在现在的域名id中存在，则加进去，否则不加进去 idList6 ids
			if (idsYes.length()>0) {
				if(idList6!=null&&idList6.size()>0){
					for (int i = 0; i < idList6.size(); i++) {
						for (int j = 0; j < strIds.length; j++) {
							if(idList6.get(i).equals(strIds[j])){
								idList06.add(idList6.get(i));
								break;
							}
						}
					}
					
				
				}
				
				if(idList06!=null&&idList06.size()>0){
					for (int i = 0; i < idList06.size(); i++) {
						idsYes = idsYes + idList06.get(i)+",";
					}
				}
				idsYes = idsYes.substring(0, idsYes.length()-1);
				
			}
			
			
			PrintWriter writer = response.getWriter();
			if(idsNo!=null&&!"".equals(idsNo)){
				writer.print(idsNo+"n");
			}else {
				writer.print(idsYes+"y");
			}
			writer.flush();
		}catch(Exception e){
			//添加失败操作日志
			EmpExecutionContext.error(e,"新建短域名异常");
		}
	}

	public void update1(HttpServletRequest request, HttpServletResponse response) {

	    String oldList = request.getParameter("bindedIdStr");
	    String newList = request.getParameter("ids");
	    String corpCode = request.getParameter("addcorpCode");;
        LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
        Long userid = sysuser != null ? sysuser.getUserId() : 0L;
        String username = sysuser==null?"":sysuser.getUserName();
        if(org.apache.commons.lang.StringUtils.equalsIgnoreCase(oldList, "null")) {
            oldList = null;
        }
        int count = urlbiz.updateBinds(corpCode, userid, username, oldList, newList);
        try {
            PrintWriter writer = response.getWriter();
            if(StringUtils.isBlank(oldList) && StringUtils.isBlank(newList) && count == 0) {
                writer.print("success");
            } else if(count > 0) {
                writer.print("success");
            } else {
                writer.print("failure");
            }
            writer.flush();
        } catch (IOException e) {
            EmpExecutionContext.error(e,"修改企业绑定域名异常！");
        }

    }


	
	public void update(HttpServletRequest request, HttpServletResponse response){
		LfDomainCorp lfDomainCorp = null;
		boolean flag = true;
		boolean flagold = true;
		
		String opUser = "";
		String corpcode = "";
		//日志内容
		String opContent = null;
		//修改前数据
		StringBuffer oldStr=new StringBuffer("");
		//修改后数据
		StringBuffer newStr=new StringBuffer("");
		//修改字段字符串
		String updateContent="";
		SuperOpLog opLog = new SuperOpLog();
		
		
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{	
			String result = request.getParameter("result");
			//最新的符合要求的被绑定的域名id
			String[] ids = result.substring(0,result.length()-1).split(",");
			String idsEquals = "";
			//企业编号
			String addcorpCode = request.getParameter("addcorpCode");
			
			//根据企业编号查看映射表LF_DOMAIN_CORP
			conditionMap.put("corpCode", addcorpCode);
			//原本的域名编号
			List<Long> domainBindIds = urlbiz.getDomainBindIds(conditionMap);
			//取出原来的域名编号与现在的域名编号进行比较
			for (int i = 0; i < ids.length ; i++) {
				for (int j = 0; j < domainBindIds.size(); j++) {
		
					if(ids[i].equals(String.valueOf(domainBindIds.get(j)))){
						idsEquals = idsEquals + ids[i]+",";
					}
				}
			}
			
			String newIds = "";
			String oldIds = "";
			if(idsEquals!=null&&idsEquals.length()>0){
				idsEquals = idsEquals.substring(0,idsEquals.length()-1);
				//原来的域名编号与现在的域名编号共同的部分
				String[] idsEqualsStr = idsEquals.split(",");
				//原来的编号比共同部分多的id则删除
				
				for (int i = 0; i < domainBindIds.size() ; i++) {
					for (int j = 0; j < idsEqualsStr.length; j++) {
						if(String.valueOf(domainBindIds.get(i)).equals(idsEqualsStr[j])){
							break;
						}else if(!String.valueOf(domainBindIds.get(i)).equals(idsEqualsStr[j])&&j== (idsEqualsStr.length-1)){
							oldIds = oldIds + String.valueOf(domainBindIds.get(i)) + ",";
						}
					}
				}
				
				
				
				//现在的编号比共同部分多的id则插入映射表
				
				for (int i = 0; i < ids.length ; i++) {
					for (int j = 0; j < idsEqualsStr.length; j++) {
						if(ids[i].equals(idsEqualsStr[j])){
							break;
						}else if(!ids[i].equals(idsEqualsStr[j])&&j== (idsEqualsStr.length-1)){
							newIds = newIds + ids[i] + ",";
						}
					}
				}
				
			}else{
				//原来的编号全部删除
				for (int i = 0; i < domainBindIds.size() ; i++) {
					oldIds = oldIds + String.valueOf(domainBindIds.get(i)) + ",";
				}
				
				//现在的编号全部插入
				for (int i = 0; i < ids.length ; i++) {
					newIds = newIds + ids[i] + ",";
				}
				
			}
			
			//获取当前时间
			Date now = new Date(); 
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
			String currentTime = dateFormat.format( now ); 
			
			//添加成功操作日志
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员id
			Long lguserid = sysuser.getUserId();
			corpcode = sysuser.getCorpCode();
			opUser = sysuser.getUserName();
			//创建人
			Long createUid = sysuser.getUserId();
			//修改人
			Long updateUid = sysuser.getUserId();
			
			if(oldIds.length()>0){
				oldIds =  oldIds.substring(0,oldIds.length()-1);
				flagold=urlbiz.delete(oldIds,addcorpCode);
			}
			
			if(newIds!=null&&!"".equals(newIds)){
				newIds =  newIds.substring(0,newIds.length()-1);
				String[] newIdsStr = newIds.split(",");
				
				if(flagold){
					if(newIdsStr.length>0){
						for (int i = 0; i < newIdsStr.length; i++) {
							flag=urlbiz.insert(addcorpCode, newIdsStr[i],createUid,updateUid,opUser);
						}
					}
				}
			}
			

			//查询企业编号修改绑定修改时间和修改人
			if(addcorpCode!=null&&!"".equals(addcorpCode)){
				urlbiz.updateTime(addcorpCode,updateUid);
			}

			String olds = "";
			for (int i = 0; i < domainBindIds.size(); i++) {
				olds = olds + domainBindIds.get(i) + ",";
			}
			
			oldStr.append(olds).append(addcorpCode)
			.append(",").append(currentTime).append(",");
			
			newStr.append(ids).append(",").append(addcorpCode)
			.append(",").append(currentTime).append(",");
			
			if(flag){
				opContent = "短域名id，企业编码,增加时间"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
				setLog(request, "短域名绑定管理", opContent, StaticValue.UPDATE);
				opLog.logSuccessString(opUser, opModule, StaticValue.UPDATE, opContent,corpcode);
			}else{
				opContent = "短域名id，企业编码,增加时间"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
				setLog(request, "短域名绑定管理", opContent, StaticValue.UPDATE);
				opLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent,null,corpcode);
			}

			
			
			response.getWriter().print(flag);
			
			
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"新建短域名页面跳转异常！");
			opContent = "短域名id，企业编码,增加时间"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
			setLog(request, "短域名绑定管理", opContent, StaticValue.UPDATE);
			opLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent,null,corpcode);

		}
	}
	
	public void see(HttpServletRequest request, HttpServletResponse response)
	{

		//查询结果模板volist
		LfDomainCorpVo lfDomainCorpVo = null;
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(10);
		String pageIndex="";
		String pageSize = "";
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			//初始化分页信息
			pageSet(pageInfo, request);
			pageIndex = request.getParameter("pageIndex");
			pageSize = request.getParameter("pageSize");
			
			String  id = request.getParameter("id");
			conditionMap.put("corpID", id);
			lfDomainCorpVo = urlbiz.getDomainCorpVos(conditionMap, pageInfo).get(0);
			conditionMap.clear();
			//根据id查找企业编码
			String corpCode = lfDomainCorpVo.getCorpCode();
			//根据企业编码corpCode查找域名id,根据域名id查找域名集合
			conditionMap.put("corpCode", corpCode);
			List<Long> domainBindIds = urlbiz.getDomainBindIds(conditionMap);
			String ids = "";
			if(domainBindIds!=null&&domainBindIds.size()>0){
				for (int i = 0; i < domainBindIds.size(); i++) {
					ids = ids + String.valueOf(domainBindIds.get(i))+",";
				}
			}
			ids = ids.substring(0,ids.length()-1);
			
			//根据域名id查找域名对象
			List<LfDomain> domains = urlbiz.getDomains(ids);
			//获取短域名全部信息
			List<LfDomain> urlListadd = null;
			if(pageIndex!=null&&!"".equals(pageIndex)){
				pageInfo.setPageIndex(Integer.valueOf(pageIndex));
			}
			if(pageSize!=null&&!"".equals(pageSize)){
				pageInfo.setPageSize(Integer.valueOf(pageSize));
			}
			urlListadd = baseBiz.getByCondition(LfDomain.class, null, null, null, pageInfo);
			
			request.setAttribute("urlListadd", urlListadd);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lfDomainCorpVo", lfDomainCorpVo);
			request.setAttribute("domains", domains);
			request.setAttribute("corpid", id);
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/url_findDomianCorp.jsp")
			.forward(request, response);
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"短链报备查询异常！");
			// 异常错误
			request.getSession(false).setAttribute("error", e);
			request.setAttribute("findresult", "-1");
			// 分页信息
			request.setAttribute("pageInfo", pageInfo);
			try
			{
				request.getRequestDispatcher(this.empRoot + this.basePath + "/url_findDomianCorp.jsp").forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(e1, "短链报备查询后跳转异常");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1, "短链报备查询后跳转IO异常");
			}
		}
	}
	
	
	//根据企业编号获取企业名称
	public void getAddcorpName(HttpServletRequest request, HttpServletResponse response){
		LfCorp lfCorp = null;
		List<LfCorp> lfCorpList = null;
		String addcorpName = "";
		String flag = "";
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{	
			//企业编号
			String addcorpCode = request.getParameter("addcorpCode");
			
			conditionMap.put("corpCode", addcorpCode);
			lfCorpList = baseBiz.getByCondition(LfCorp.class, conditionMap, null);
			if(lfCorpList!=null&&lfCorpList.size()>0){
				lfCorp = lfCorpList.get(0);
				addcorpName = lfCorp.getCorpName();
			}
			
			if(addcorpName==null||"".equals(addcorpName)){
				flag = "n";
			}else{
				flag = addcorpName;
			}
			
			response.getWriter().print(flag);
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"根据企业编号获取企业名称异常！");
		}
	}
	
	/**
	 * 修改通道状
	 * @param request
	 * @param response
	 */
	public void changeState(HttpServletRequest request, HttpServletResponse response)
	{
		String opUser = "";
		String corpcode = "";
		//日志内容
		String opContent = null;
		//修改前数据
		StringBuffer oldStr=new StringBuffer("");
		//修改后数据
		StringBuffer newStr=new StringBuffer("");
		//修改字段字符串
		String updateContent="";
		SuperOpLog opLog = new SuperOpLog();
		
		boolean statuFlag;
		PrintWriter writer = null;
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LfDomainCorp domainCorp = null;
		try {
			writer = response.getWriter();
			String id = request.getParameter("id");
			String corpCode = request.getParameter("corpCode");
			String state = request.getParameter("state");
			//修改绑定管理界面的状态
			conditionMap.put("domainId", String.valueOf(id));
			conditionMap.put("corpCode", corpCode);
			domainCorp = baseBiz.getByCondition(LfDomainCorp.class, conditionMap, null).get(0);
			
			statuFlag = urlbiz.changeState(Long.parseLong(id),corpCode,Integer.parseInt(state));
			

			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员id
			Long lguserid = sysuser.getUserId();
			corpcode = sysuser.getCorpCode();
			opUser = sysuser.getUserName();
			
			oldStr.append(domainCorp.getDomainId()).append(",").append(domainCorp.getCorpCode())
			.append(",").append(domainCorp.getFlag()).append(",").append(domainCorp.getCreateTm())
			.append(",").append(domainCorp.getCreateUid()).append(",").append(domainCorp.getUpdateTm());
			
			domainCorp = baseBiz.getByCondition(LfDomainCorp.class, conditionMap, null).get(0);
			
			newStr.append(domainCorp.getDomainId()).append(",").append(domainCorp.getCorpCode())
			.append(",").append(domainCorp.getFlag()).append(",").append(domainCorp.getCreateTm())
			.append(",").append(domainCorp.getCreateUid()).append(",").append(domainCorp.getUpdateTm());
			
			if(statuFlag){
				opContent = "短域名，全局扩展位数，访问时效上限，应用类型,状态"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
				setLog(request, "短域名管理", opContent, StaticValue.UPDATE);
				opLog.logSuccessString(opUser, opModule, StaticValue.UPDATE, opContent,corpcode);
				
			}else{
				opContent = "短域名，全局扩展位数，访问时效上限，应用类型,状态"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
				setLog(request, "短域名管理", opContent, StaticValue.UPDATE);
				opLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent,null,corpcode);
				
			}
			
			writer.print(statuFlag);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"修改状态异常！");
			EmpExecutionContext.error(e,"修改状态异常！");
			opContent = "短域名，全局扩展位数，访问时效上限，应用类型,状态"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
			setLog(request, "短域名管理", opContent, StaticValue.UPDATE);
			opLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent,null,corpcode);

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
}
