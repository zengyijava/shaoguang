package com.montnets.emp.ywgl.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.ydyw.LfBusPackage;
import com.montnets.emp.entity.ydyw.LfBusPkgeTaoCan;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.ywgl.biz.YwglBiz;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务包管理
 * @todo TODO
 * @project	emp
 * @author WANGRUBIN
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-9 下午05:26:57
 * @description
 */
public class ydyw_busPkgMgrSvt extends BaseServlet
{
	private static final long	serialVersionUID	= 1908901989360839320L;

	// 业务包biz
	private final YwglBiz		        ywglBiz		        = new YwglBiz();
	
	private final BaseBiz             baseBiz             = new BaseBiz();

	// 模块名称
	private final String						empRoot				= "ydyw";

	// 基路径
	private final String						base				= "/ywgl";

	/**
	 * 分页使用方法
	 * 
	 * @param param
	 * @param defaultValue
	 * @param request
	 * @return
	 */
	protected int getIntParameter(String param, int defaultValue, HttpServletRequest request)
	{
		try
		{
			return Integer.parseInt(request.getParameter(param));
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e, "获取分页信息异常");
			return defaultValue;
		}
	}

	/**
	 * 业务包管理查询方法
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 是否第一次访问
		boolean isFirstEnter = false;
		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 登录操作员id
		//String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);
		// 分页对象
		PageInfo pageInfo = new PageInfo();
		
		//添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
		
		try
		{
			// 获得session
			HttpSession session = request.getSession(false);
			List<LfBusPackage> lfBusPackageList = null;
			boolean isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			List<DynaBean> recordList = null;
			Map map = null;
			
			if(isError)
			{
				isFirstEnter = true;
			}
			else
			{
				// 设置分页
				pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
				pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
				isFirstEnter = false;
			}
			//if(!isFirstEnter)
			//{
				// 获取当前登录操作员对象
				LfSysuser user = baseBiz.getById(LfSysuser.class, userid);
				//当前操作员所拥有的权限
				Integer permissionType = user.getPermissionType();
				if(permissionType == 1)
				{
					conditionMap.put("permissionType","1");
					conditionMap.put("permissionUserName",user.getUserName().toString());
				}else{
					//机构权限 只能查看本机机构及其子机构数据
					conditionMap.put("opDepId", user.getDepId().toString());
				}
					
				String packageName = request.getParameter("packageName");
				String packageCode = request.getParameter("packageCode");
				String packageState = request.getParameter("packageState");
				String busName = request.getParameter("busName");
				String busCode = request.getParameter("busCode");
				String userName = request.getParameter("userName");
				String deptString = request.getParameter("deptString");
				String begintime = request.getParameter("begintime");
				String endtime = request.getParameter("endtime");
				String isContainsSun = request.getParameter("isContainsSun");
				
				if(!isFirstEnter){
					if(isContainsSun!=null && !"".equals(isContainsSun)){
						conditionMap.put("isContainsSun", "1");
					}else{
						conditionMap.put("isContainsSun", "0");
					}					
				}
				
				if(packageName != null && !"".equals(packageName)){
					conditionMap.put("packageName",packageName);
				}
				if(packageCode != null && !"".equals(packageCode)){
					conditionMap.put("packageCode",packageCode);
				}
				if(packageState != null && !"".equals(packageState)){
					conditionMap.put("packageState",packageState);
				}
				if(busName != null && !"".equals(busName)){
					conditionMap.put("busName",busName);
				}
				if(busCode != null && !"".equals(busCode)){
					conditionMap.put("busCode",busCode);
				}
				if(userName != null && !"".equals(userName)){
					conditionMap.put("userName",userName);
				}
				deptString = (deptString != null && deptString.length()>0)?deptString:"";
				if(!"".equals(deptString)){
					conditionMap.put("deptString",deptString);
				}					
				if(begintime != null && !"".equals(begintime)){
					conditionMap.put("begintime",begintime);
				}
				if(endtime != null && !"".equals(endtime)){
					conditionMap.put("endtime",endtime);
				}
				
				//查询业务包记录
				//lfBusPackageList = baseBiz.getByCondition(LfBusPackage.class, Long.valueOf(userid), conditionMap, orderbyMap, pageInfo);
				recordList = ywglBiz.getPkgList(conditionMap, pageInfo);
				
				map = new HashMap<String, String>();
				
				 //查询业务列表记录并添加到map集合
				 if(recordList!=null&&recordList.size()>0){
					 
					 String pkgCodeStr = "";
					 
					 for(int i=0;i<recordList.size();i++){
						DynaBean bean=recordList.get(i);
						pkgCodeStr += "'"+bean.get("package_code").toString()+"',";
					 }
					 if(pkgCodeStr.indexOf(",")>-1){
						 pkgCodeStr = pkgCodeStr.substring(0,pkgCodeStr.length()-1);
					 }
					 List<DynaBean> busInfoList = ywglBiz.getBusInfoList(pkgCodeStr);
					 for(DynaBean bean : busInfoList){
						 String key = bean.get("package_code").toString();
						 if(key!=null)
						 {
						 	 String bus_name = bean.get("bus_name").toString();
							 bus_name = "默认业务".equals(bus_name)? MessageUtils.extractMessage("common","common_defaultBusiness",request):bus_name;
							 String val = map.get(key)==null?bus_name+"("+bean.get("bus_code")+")":map.get(key)+","+bus_name+"("+bean.get("bus_code")+")";
							 map.put(key, val);
						 }
					 }
				 }
			//}
			request.setAttribute("recordList", recordList);
			request.setAttribute("busMap", map);
			request.setAttribute("pageInfo",pageInfo);
			request.setAttribute("conditionMap",conditionMap);
		}
		catch (Exception e)
		{
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "后台异常");

		}
		finally
		{
			//添加与日志相关 p
			long endTimeByLog = System.currentTimeMillis();  //查询结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时
			
			try {
				//增加操作日志 p
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
					
					EmpExecutionContext.info("业务包管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent, "GET");
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "添加操作日志异常！");
			}
			
			
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.getRequestDispatcher(this.empRoot + base + "/ydyw_busPkgMgr.jsp?lgcorpcode=" + corpCode + "&lguserid=" + userid).forward(request, response);
		}
	}

	/**
	 * 跳转到新增业务包页面
	 * @author WANGRUBIN
	 * @datatime 2015-1-13 下午02:56:16
	 * @description TODO 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toAdd(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 登录操作员id
		//String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);

		
		try
		{
			//查出所有启用的业务列表
			//List<LfBusManager> busList = baseBiz.getEntityList(LfBusManager.class);
			List<DynaBean> busList = ywglBiz.getBusList(null);
			busList = replaceStr(busList,request);
			request.setAttribute("busList", busList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "跳转到新增业务包页面后台异常");
		}
		finally
		{
			request.getRequestDispatcher(this.empRoot + base + "/ydyw_addBusPkg.jsp?lgcorpcode=" + corpCode + "&lguserid=" + userid).forward(request, response);
		}
	}
	
	/**
	 * 跳转到修改业务包页面
	 * @author WANGRUBIN
	 * @datatime 2015-1-15 下午02:51:23
	 * @description TODO 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 登录操作员id
		//String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);
		
		String pkgId = request.getParameter("pkgId");
		try
		{
			//所有启用的业务列表
			List<DynaBean> busList = ywglBiz.getBusList(null);
			busList = replaceStr(busList,request);
			//业务包信息
			LfBusPackage pkgList = baseBiz.getById(LfBusPackage.class, Long.valueOf(pkgId));
			//该业务包所包含的业务记录
			List<DynaBean> haveBusList = null;
			if(null!=pkgList){
				haveBusList = ywglBiz.getBusInfoList("'"+pkgList.getPackageCode()+"'");	
			}
			haveBusList = replaceStr(haveBusList,request);
			request.setAttribute("busList", busList);
			request.setAttribute("pkgList", pkgList);
			request.setAttribute("haveBusList", haveBusList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "跳转到修改业务包页面后台异常");
		}
		finally
		{
			request.getRequestDispatcher(this.empRoot + base + "/ydyw_editBusPkg.jsp?lgcorpcode=" + corpCode + "&lguserid=" + userid).forward(request, response);
		}
	}

	/**
	 * 替換业务名称与描述中的“默认业务”
	 * @param list
	 * @param request
	 */
	private List<DynaBean> replaceStr(List<DynaBean> list,HttpServletRequest request){
		String str = MessageUtils.extractMessage("common","common_defaultBusiness",request);
		for(DynaBean dynaBean : list){
			if("默认业务".equals(dynaBean.get("bus_name"))){
				dynaBean.set("bus_name",str);
				try {
					if(dynaBean.get("bus_description")!= null) dynaBean.set("bus_description",str);
					break;
				}catch(IllegalArgumentException e) {
					EmpExecutionContext.error(e, e.getLocalizedMessage());
					break;
				}
			}
		}
		return list;
	}
	/**
	 * 点击查询按钮，根据名称或编码查询业务
	 * @author WANGRUBIN
	 * @datatime 2015-1-13 下午04:14:10
	 * @description TODO 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void busSearch(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String searStr = request.getParameter("searStr");
		if(searStr==null){
			searStr="";
		}
		List<DynaBean> nameList = ywglBiz.getBusList(searStr);
		StringBuffer sb=new StringBuffer("[");
		if(nameList!=null){
			for( int i=0;i<nameList.size();i++){
				DynaBean db=nameList.get(i);
				String bus_code=db.get("bus_code").toString();
				String bus_name=db.get("bus_name").toString();
				sb.append("{bus_code:'"+bus_code+"',bus_name:'"+bus_name+"'}");
				if(i !=nameList.size()-1){
					sb.append(",");
				}
			}
		}
		sb.append("]");
		response.getWriter().print(sb.toString());
	}
	
	/**
	 * 业务包新增或修改
	 * @author WANGRUBIN
	 * @datatime 2015-1-13 下午04:13:53
	 * @description TODO 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void update(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		String result = "false";
		try{
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String lgcorpcode = request.getParameter("lgcorpcode");
			String busPkgName = null!=request.getParameter("busPkgName")?request.getParameter("busPkgName").trim():"";
			String busPkgCode = request.getParameter("busPkgCode");
			//String busPkgDes = request.getParameter("busPkgDes");
			String bussids = request.getParameter("bussids");
			String doType = request.getParameter("doType");
			
			LfBusPackage pkg = null;
			
			Timestamp time = new Timestamp(System.currentTimeMillis());
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			if("add".equals(doType)){//新增业务包
				
				//查询库记录是否已存在此业务包编码
				conditionMap.put("packageCode", busPkgCode);
				List<LfBusPackage> pkgList = baseBiz.getByCondition(LfBusPackage.class, conditionMap, null);
				if(null!=pkgList && pkgList.size()>0){
					result = "repeat";	
				}else{
					pkg = new LfBusPackage();
					pkg.setPackageCode(busPkgCode);
					pkg.setPackageName(busPkgName);
					pkg.setPackageDes("");
					pkg.setPackageState(0);
					pkg.setCorpCode(lgcorpcode);
					pkg.setUserId(Integer.valueOf(lguserid));
					pkg.setCreateTime(time);
					pkg.setUpdateTime(time);
					boolean isTure = ywglBiz.doAdd(pkg,bussids);
					if(isTure){
						result = "success";	
					}
					//增加操作日志
					Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
						String opContent1 = "新建业务包"+(isTure==true?"成功":"失败")+"。[业务包名称，业务包编号，业务]" +
								"("+busPkgName+"，"+busPkgCode+"，["+bussids+"])";
						EmpExecutionContext.info("业务包管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
								loginSysuser.getUserName(), opContent1, "ADD");
					}					
				}
			}else if("edit".equals(doType)){//修改业务包
				
				String pkgId = request.getParameter("pkgId");
				pkg = baseBiz.getById(LfBusPackage.class, Long.valueOf(pkgId));
				
				//查询操作之前记录
				conditionMap.clear();
				conditionMap.put("packageCode", pkg.getPackageCode());
				conditionMap.put("associateType","1");
				List<LfBusPkgeTaoCan> oldpkgtcList = baseBiz.getByCondition(LfBusPkgeTaoCan.class, conditionMap, null);
				String oldBuss = "";
				if(oldpkgtcList.size()>0){
					for(int i=0;i<oldpkgtcList.size();i++){
						oldBuss += oldpkgtcList.get(i).getBusCode()+",";	
					}
					oldBuss = oldBuss.substring(0,oldBuss.length()-1);
				}
				String befchgCont= pkg.getPackageName()+"，"+pkg.getPackageCode()+"，["+oldBuss+"]";
				
				pkg.setPackageName(busPkgName);
				pkg.setUpdateTime(time);
				boolean isTure = ywglBiz.doEdit(pkg,bussids);
				if(isTure){
					result = "success";	
				}
				//增加操作日志
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent1 = "修改业务包"+(isTure==true?"成功":"失败")+"。[业务包名称，业务包编号，业务编码集合]" +
							"("+befchgCont+")->("+busPkgName+"，"+busPkgCode+"，["+bussids+"])";
					EmpExecutionContext.info("业务包管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent1, "UPDATE");
				}
			}
		}catch(Exception e){
			EmpExecutionContext.error(e, "编辑业务包后台异常");
		}finally{
			response.getWriter().print(result);
		}
	}
	
	/**
	 * 修改业务包状态
	 * @author WANGRUBIN
	 * @datatime 2015-1-15 下午04:21:53
	 * @description TODO 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void changeState(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		String result = "false";
		try{
			String pkgId = request.getParameter("pkgId");
			String pkgState = request.getParameter("pkgState");
			
			LfBusPackage pkg = baseBiz.getById(LfBusPackage.class, pkgId);
			pkg.setPackageState(Integer.valueOf(pkgState));
			boolean sult = baseBiz.updateObj(pkg);
			if(sult){
				result = "success";
			}
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "修改业务包状态"+(sult==true?"成功":"失败")+"。[业务包名称，业务包编号，业务包状态]" +
						"("+pkg.getPackageName()+"，"+pkg.getPackageCode()+"，"+pkgState+")";
				EmpExecutionContext.info("业务包管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}
			
		}catch(Exception e){
			EmpExecutionContext.error(e, "编辑业务包后台异常");
		}finally{
			response.getWriter().print(result);
		}
	}
	
	
	
}
