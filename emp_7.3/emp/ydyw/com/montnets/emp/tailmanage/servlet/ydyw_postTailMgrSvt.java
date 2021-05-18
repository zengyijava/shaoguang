package com.montnets.emp.tailmanage.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.ydyw.LfBusTail;
import com.montnets.emp.entity.ydyw.LfBusTailTmp;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.tailmanage.biz.TailMgrBiz;
import com.montnets.emp.util.PageInfo;

public class ydyw_postTailMgrSvt extends BaseServlet {

	private static final String PATH = "/ydyw/postTailMgr";
	private final BaseBiz baseBiz=new BaseBiz();
	private final TailMgrBiz tailMgrBiz = new TailMgrBiz();
	
	/**
	 *  查询业务贴尾
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {

		try {
			
			//添加与日志相关 p
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
			long startTimeByLog = System.currentTimeMillis();  //开始时间
			
			//分页对象
			PageInfo pageInfo=new PageInfo();
			//是否第一次打�?
			boolean isFirstEnter = false;
			isFirstEnter=pageSet(pageInfo, request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			List<DynaBean> recordList =null;
			//String lguserid=request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String lgcorpcode=request.getParameter("lgcorpcode");
			 LinkedHashMap<String, LinkedHashMap> tailList=new LinkedHashMap<String, LinkedHashMap>();
	        //是否包含子机构
	        String isContainsSun=request.getParameter("isContainsSun");
				//贴尾名称
				String name = request.getParameter("tailname");
				if(name != null && !"".equals(name)){
					conditionMap.put("tailname",name.trim());
				}
				//业务名称
				String buss = request.getParameter("buss");
				if(buss != null && !"".equals(buss)){
					conditionMap.put("buss",buss.trim());
				}
				//部门
				String deptid = request.getParameter("deptid");
				deptid = (deptid != null && deptid.length()>0)?deptid:"";
				if(!"".equals(deptid)){
					conditionMap.put("deptid",deptid.trim());
				}
				
				if(isFirstEnter){
					//默认是选择的
						conditionMap.put("isContainsSun", "1");
				}else{
					if(isContainsSun==null||"".equals(isContainsSun)){
						conditionMap.put("isContainsSun", "0");
					}else{
						conditionMap.put("isContainsSun", "1");
					}
				}
				//创建人
				String userName = request.getParameter("userName");
				if(userName != null && !"".equals(userName)){
					conditionMap.put("userName",userName.trim());
				}
				//提交起始时间
				String submitSartTime = request.getParameter("startSubmitTime");
				if(submitSartTime != null && !"".equals(submitSartTime)){
					conditionMap.put("startSubmitTime",submitSartTime);
				}
				//提交结束时间
				String submitEndTime = request.getParameter("endSubmitTime");
				if(submitEndTime != null && !"".equals(submitEndTime)){
					conditionMap.put("endSubmitTime",submitEndTime);
				}
				// 获取当前登录操作员对象
				LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
				//当前操作员所拥有的权限
				Integer permissionType = (sysuser != null && sysuser.getPermissionType() != null) ? sysuser.getPermissionType():null;
				if(permissionType != null && permissionType == 1)
				{
					conditionMap.put("permissionType","1");
					conditionMap.put("permissionUserName",sysuser.getUserName().toString());
				}
	        	conditionMap.put("userId", sysuser==null?null:String.valueOf(sysuser.getUserId()));
				 recordList =tailMgrBiz.getTailRecord(conditionMap,pageInfo);
				 

				 //业务列表记录
				 if(recordList!=null&&recordList.size()>0){
					 for(int i=0;i<recordList.size();i++){
						 DynaBean db=recordList.get(i);
						String bustail_id =db.get("bustail_id").toString();
						LinkedHashMap<String, String> list= tailMgrBiz.busList(bustail_id);
						tailList.put(bustail_id, list);
					 }
					
				 }
			if(isFirstEnter){
				//默认是选择的
				request.setAttribute("isContainsSun", "1");
			}else{
				if(isContainsSun==null||"".equals(isContainsSun)){
					request.setAttribute("isContainsSun", "0");
				}else{
					request.setAttribute("isContainsSun", "1");
				}
			}
			List<DynaBean> busList = tailMgrBiz.getBusiness(null);
			request.setAttribute("lguserid",lguserid);
			request.setAttribute("lgcorpcode",lgcorpcode);
			request.setAttribute("tailList",tailList);
			request.setAttribute("busList",busList);
			request.setAttribute("recordList",recordList);
			request.setAttribute("conditionMap",conditionMap);
			request.setAttribute("pageInfo",pageInfo);
			
			
			//添加与日志相关 p
			long endTimeByLog = System.currentTimeMillis();  //查询结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时
			
			//增加操作日志 p
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
				
				EmpExecutionContext.info("业务贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent, "GET");
			}
			
			
			
			request.getRequestDispatcher(PATH+ "/smsTailManage.jsp?lguserid="+lguserid).forward(request,response);
		
		
		} catch (Exception e1) {
			EmpExecutionContext.error(e1,"业务贴尾管理页面跳转出现异常");
		}
	}
	
	/**
	 * 点击查询按钮，根据名称查询业务
	 * @param request
	 * @param response
	 */

	public void search(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String name = request.getParameter("epname");
		if(name==null){
			name="";
		}
		List<DynaBean> nameList = tailMgrBiz.getBusiness(name);
		StringBuffer sb=new StringBuffer("[");
		if(nameList!=null){
			for( int i=0;i<nameList.size();i++){
				DynaBean db=nameList.get(i);
				String bus_code=db.get("bus_code").toString();
				String bus_name=db.get("bus_name").toString();
				String bus_id=db.get("bus_id").toString();
				String bustailname="";
				Object bustail_name=db.get("bustail_name");
				if(bustail_name!=null){
					bustailname=bustail_name.toString();
				}
				String tablelink="";
				if(db.get("tablelink")!=null){
					tablelink=db.get("tablelink").toString();
				}
				
				sb.append("{bus_code:'"+bus_code+"',bus_name:'"+bus_name+"',bus_id:'"+bus_id+"',bustail_name:'"+bustailname+"', tablelink:'"+tablelink+"'}");
				if(i !=nameList.size()-1){
					sb.append(",");
				}
			}
		}
		sb.append("]");
		response.getWriter().print(sb.toString());
	}
	
	/***
	 * 保存短息贴尾信息
	 * @param request
	 * @param response
	 */
	public void save(HttpServletRequest request, HttpServletResponse response) {
		boolean add=false;
		String tmMsg = request.getParameter("tmMsg");
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		String lgcorpcode=request.getParameter("lgcorpcode");
		String tmTitle=request.getParameter("tmTitle");
		String bussids=request.getParameter("bussids");
		
		//判断是否包含关键字
		String keyWord=checkBadWord(tmMsg);
		if(keyWord!=null&&!"".equals(keyWord)){
			try {
				response.getWriter().print(keyWord);
			} catch (IOException e) {
				EmpExecutionContext.error(e,"判断是否包含关键字异常");
			}
			return;
		}
		
		LfBusTail tail=new LfBusTail();
		tail.setContent(tmMsg);
		tail.setBustail_name(tmTitle);
		tail.setCreate_time(new Timestamp(System.currentTimeMillis()));
		if(lguserid!=null&&!"".equals(lguserid)){
			tail.setUser_id(Integer.parseInt(lguserid));
		}
		tail.setCorp_code(lgcorpcode);
		try {
			add=tailMgrBiz.save(tail, bussids);
//			 add=new BaseBiz().addObj(tail);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"保存贴尾信息异常");
		}
		try {
				if(add){
					response.getWriter().print("success");
				}else{
					response.getWriter().print("fail");
				}
				//增加操作日志
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent1 = "新建业务贴尾内容"+(add==true?"成功":"失败")+"。[贴尾名称，适用业务]" +
							"("+tmTitle+"，["+bussids+"])";
					EmpExecutionContext.info("业务贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent1, "ADD");
				}
			} catch (IOException e) {
				EmpExecutionContext.error(e,"跳转贴尾信息异常");
			}
	}
	/***
	 *  根据ID查询出单个记录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getTailByID(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
	  String tailid= request.getParameter("tailid");
	  LfBusTail tail= baseBiz.getById(LfBusTail.class, tailid);
	  String updateSelect= tailMgrBiz.busStr(tail.getBustail_id()+"");
	  
	  String show="{bustail_name: '"+tail.getBustail_name()+"', content: '"+string2json(tail.getContent())+"',tailid: '"+tailid+"',updateSelect:"+updateSelect+"}";
	  response.getWriter().print(show);
	}
	
	public String string2json(String s){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<s.length();i++){
			char c=s.charAt(i);
			switch(c){
				case '\"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;	
				case '/':
					sb.append("\\/");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				default:
					sb.append(c);
			
			}
			
		}
		return sb.toString();
	}
	
	/**
	 * 根据ID删除单条记录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void deleteByID(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String tailid= request.getParameter("id");
		
		try {
			//查询操作之前记录
			LfBusTail befchgEntity = baseBiz.getById(LfBusTail.class, tailid);
			String befchgCont = befchgEntity.getBustail_id()+"，"+befchgEntity.getBustail_name();
			
			String retvalue= tailMgrBiz.deleteSingle(tailid);
			response.getWriter().print(retvalue);
			 
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "删除业务贴尾内容"+("success".equals(retvalue)?"成功":"失败")+"。[贴尾Id，贴尾名称]" +
						"("+befchgCont+")";
				EmpExecutionContext.info("业务贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "DELETE");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据Id删除单条记录异常！");
		}			
	}
	
	/**
	 * 根据ID删除多条记录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void deleteSelect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String ids = request.getParameter("id");
		
		try {
			//查询操作之前记录
			String idss = "";
			String befchgCont= "";
			if(ids.indexOf(",")>-1){
				if(",".equals(ids.substring(0,1).toString())){
					idss = ids.substring(1, ids.length()-1);
				}else{
					idss = ids.substring(0, ids.length()-1);
				}
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("bustail_id&in", idss);
				List<LfBusTail> lfBusTailList = baseBiz.getByCondition(LfBusTail.class, conditionMap, null);
				if(lfBusTailList.size()>0){
					for(int i=0;i<lfBusTailList.size();i++){
						befchgCont += "("+lfBusTailList.get(i).getBustail_id()+"，"+lfBusTailList.get(i).getBustail_name()+")";
					}
				}
			}

			String ret=tailMgrBiz.deleteSelect(ids);
			response.getWriter().print(ret);
			
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "删除业务贴尾内容"+("success".equals(ret)?"成功":"失败")+"。[贴尾Id，贴尾名称]" +befchgCont;
				EmpExecutionContext.info("业务贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "DELETE");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据Id删除多条记录异常！");
		}	
	}
	
	//修改贴尾的内容
	public void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean add=false;
		String tmMsg = request.getParameter("tmMsg");
		String tmTitle=request.getParameter("tmTitle");
		String bussids=request.getParameter("bussids");
		
		//判断是否包含关键字
		String keyWord=checkBadWord(tmMsg);
		if(keyWord!=null&&!"".equals(keyWord)){
			try {
				response.getWriter().print(keyWord);
			} catch (IOException e) {
				EmpExecutionContext.error(e,"判断是否包含关键字异常");
			}
			return;
		}
		
		String modfid=request.getParameter("modfid");
		LfBusTail tail=baseBiz.getById(LfBusTail.class, modfid);
		
		//查询操作之前记录
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("smstail_id", modfid);
		List<LfBusTailTmp> tmpList = baseBiz.getByCondition(LfBusTailTmp.class, conditionMap, null);
		String busIds = "";
		if(tmpList.size()>0){
			for(int i=0;i<tmpList.size();i++){
				busIds += tmpList.get(i).getBus_id()+",";
			}
		}
		String befchgCont= tail.getBustail_name()+"，["+busIds+"]";
		
		tail.setContent(tmMsg);
		tail.setBustail_name(tmTitle);
		tail.setUpdate_time(new Timestamp(System.currentTimeMillis()));

		if(modfid==null||"".equals(modfid)){
			try {
				response.getWriter().print("fail");
				return;
			} catch (IOException e) {
				EmpExecutionContext.error(e,"贴尾信息异常");
			}
			
		}
		try {
			add=tailMgrBiz.update(tail, bussids,modfid);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"贴尾信息异常");
		}
		try {
				if(add){
					response.getWriter().print("success");
				}else{
					response.getWriter().print("fail");
				}
				//增加操作日志
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent1 = "修改业务贴尾内容"+(add==true?"成功":"失败")+"。[贴尾名称，适用业务]" +
							"("+befchgCont+")->("+tmTitle+"，["+bussids+"])";
					EmpExecutionContext.info("业务贴尾管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent1, "UPDATE");
				}				
			} catch (IOException e) {
				EmpExecutionContext.error(e,"跳转贴尾信息异常");
			}
	}

	/**
	 * 查询条件中的机构树加载方法
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	//输出机构代码数据	
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try
		{
			Long depId = null;
			Long userid=null;
			//部门iD
			String depStr = request.getParameter("depId");
			//操作员账号
			//String userStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userStr = SysuserUtil.strLguserid(request);
			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			if(userStr != null && !"".equals(userStr.trim())){
				userid = Long.parseLong(userStr);
			}
			//从session中获取当前操作员对象
			LfSysuser lfSysuser = getLoginUser(request);
			String corpCode = lfSysuser.getCorpCode();
			String departmentTree = tailMgrBiz.getDepartmentJosnData2(depId, userid,corpCode);			
			response.getWriter().print(departmentTree);		
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"网优群发历史或群发任务查询条件中的机构树加载方法异常");
		}
	}
	
	/**
	 * 检查关键字
	 */
	public String  checkBadWord(String tmMsg) {
		 KeyWordAtom keyWordAtom = new KeyWordAtom();
		//内容
		String words = new String();
		try {
			//调用检查关键字的方法，并返回结果

			words = keyWordAtom.checkText(tmMsg);
		} catch (Exception e) {
			words = "error";
			EmpExecutionContext.error(e, "检查发送内容关键字异常!");
		} 
		return words;
	}
	
}
