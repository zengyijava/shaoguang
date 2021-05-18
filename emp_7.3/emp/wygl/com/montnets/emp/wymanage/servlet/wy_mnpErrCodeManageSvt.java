/**
 * Program  : wy_mnpErrCodeManageSvt.java
 * Author   : zousy
 * Create   : 2014-3-26 下午02:13:39
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.wymanage.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wy.AMnperrcode;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wymanage.biz.MnpErrCodeManageBiz;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-3-26 下午02:13:39
 */
public class wy_mnpErrCodeManageSvt extends BaseServlet
{
	private final String empRoot="wygl";
	private final String base="/wymanage";
	private final MnpErrCodeManageBiz mnpErrCodeManageBiz = new MnpErrCodeManageBiz();
	private final BaseBiz	baseBiz	= new BaseBiz();
	private final String opModule ="转网错误代码";
	/**
	 * 查询
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zousy <zousy999@qq.com>
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2014-3-26 下午02:14:10
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		long stime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try {
			String errCodeType = request.getParameter("errCodeType");
			String errCode = request.getParameter("errCode");
			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			//类型
			if(errCodeType!=null&&!"".equals(errCodeType)){
				Integer type = -1;
				Integer mnpType = -1;
				if(errCodeType.length()==4){
					type = Integer.valueOf(errCodeType.substring(0, 2));
					mnpType = Integer.valueOf(errCodeType.substring(2));
					conditionMap.put("type", String.valueOf(type));
					conditionMap.put("mnptype", String.valueOf(mnpType));
				}
			}
			//错误代码
			if(errCode!=null&&!"".equals(errCode)){
				conditionMap.put("errorcode&like", errCode);
			}
			orderbyMap.put("createtime", StaticValue.DESC);
			orderbyMap.put("id", StaticValue.DESC);
			List<AMnperrcode> list = mnpErrCodeManageBiz.getMnpErrCodeList(pageInfo, conditionMap, orderbyMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("list", list);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"携号转网错误代码","("+sDate+")查询", StaticValue.GET);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"携号转网错误码管理查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
		}finally{
			request.getRequestDispatcher(this.empRoot+base+"/wy_mnpErrCodeManage.jsp").forward(request,
					response); 
		}
	}
	
	/**
	 * 错误码修改
	 * @description    
	 * @param request
	 * @param response
	 * @throws IOException       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-3-26 下午04:18:58
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String errCode = request.getParameter("errCode");
		String oldErrCode = request.getParameter("oldErrCode");
		String oldType = null;
		String oldCode = null;
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "UPDATE";
		String OpStatus = "失败";
		Integer type = null;
		Integer mnpType = null;
		Long id = null;
		String result = null;
		try
		{
			id = Long.valueOf(request.getParameter("id"));
			//原类型、错误代码
			AMnperrcode oldMnpErrCode = baseBiz.getById(AMnperrcode.class, id);
			oldType = oldMnpErrCode.getType().toString()+oldMnpErrCode.getMnptype().toString();
			oldCode = oldMnpErrCode.getErrorcode();
			String typeStr = request.getParameter("type");
			if(typeStr==null||"".equals(typeStr.trim())||typeStr.length()!=4){
				//out.print("error");
				result = "error";
				return;
			}
			type = Integer.valueOf(typeStr.substring(0, 2));
			mnpType = Integer.valueOf(typeStr.substring(2));
			if(errCode==null||"".equals(errCode.trim())||errCode.getBytes("UTF-8").length>7){
//				out.print("codeErr");
				result = "codeErr";
				return;
			}
			boolean isAllowed = false;
			if(!errCode.equals(oldErrCode)){
				isAllowed = true;
			}
			AMnperrcode mnpErrCode = new AMnperrcode(id,errCode,type,mnpType);
			int count = mnpErrCodeManageBiz.updateMnpErrCode(mnpErrCode, isAllowed);
			if(count>0)
			{
				OpStatus = "成功";
//				out.print("success");
				result = "success";
				
			}else if(count==0){
//				out.print("repeat");
				result = "repeat";
			}else if(count==-1){
//				out.print("notExist");
				result = "notExist";
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "号码错误码修改发生异常！");
//			out.print("exception");
			result ="exception";
		}finally
		{
			//增加操作日志
			Object obj = null;
			try {
				obj = request.getSession(false).getAttribute("loginSysuser");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"session为null");
			}
			LfSysuser lfSysuser = (LfSysuser)obj;
			if(lfSysuser!=null && lfSysuser.getUserId()!=null){
			userId = lfSysuser.getUserId().toString();
			corpCode = lfSysuser.getCorpCode();
			userName = lfSysuser.getUserName();
			}
		
			opContent = "修改携号转网错误代码"+OpStatus+"。（id："+id+"，类型："+oldType+"，错误代码："+oldCode+")-->（id："+id+"，类型："+type+""+mnpType+"，错误代码："+errCode+")";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			out.print(result);
		}
	}
	
	/**
	 * 删除
	 * @description    
	 * @param request
	 * @param response
	 * @throws IOException       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-4-1 上午08:30:48
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String ids = request.getParameter("id");
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "DELETE";
		String OpStatus = "失败";
		String opStr = null;
		String result = null;
		if(ids==null||ids.length()<1){
//			out.print("error");
			result = "error";
			return;
		}
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("id&in", ids);
			List<AMnperrcode> aMnperrcodes = new BaseBiz().getByCondition(AMnperrcode.class, conditionMap, null);
			opStr = mnpErrCodeManageBiz.getloggerInfoStr(aMnperrcodes);
			new BaseBiz().deleteByIds(AMnperrcode.class, ids);
			OpStatus = "成功";
//			out.print("success");
			result = "success";
		}
		catch (Exception e)
		{
			OpStatus = "失败";
			EmpExecutionContext.error(e, "携号转网错误码删除发生异常！");
//			out.print("exception");
			result = "exception";
		}finally{
			//增加操作日志
			Object obj = null;
			try {
				obj = request.getSession(false).getAttribute("loginSysuser");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"session为null");
			}
			LfSysuser lfSysuser = (LfSysuser)obj;
			if(lfSysuser!=null && lfSysuser.getUserId()!=null){
			userId = lfSysuser.getUserId().toString();
			corpCode = lfSysuser.getCorpCode();
			userName = lfSysuser.getUserName();
			}
			
			opContent = "删除携号转网错误代码"+OpStatus+"。（"+opStr+"）";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			out.print(result);
		}
	}
	
	/**
	 * 手动添加
	 * @description    
	 * @param request
	 * @param response
	 * @throws IOException       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-4-1 上午08:37:42
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String result = null;
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "ADD";
		Integer type = null ,mnpType = null;
		String errCodeStr = null;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String errCodeTypeStr = request.getParameter("errCodeType");
		int size = 0;
		try
		{
			if(!errCodeTypeStr.matches("^[0-2]{4}$")){
//				out.print("error");
				result = "error";
				return;
			}
			type = Integer.valueOf(errCodeTypeStr.substring(0, 2));
			mnpType = Integer.valueOf(errCodeTypeStr.substring(2));
			errCodeStr = request.getParameter("errCodeStr");
			List<AMnperrcode> lists = new ArrayList<AMnperrcode>();
			Pattern pattern = Pattern.compile("(^|[，, 、\\n]+)([^，, 、\\n]{1,7})($|[，, 、\\n]+)"); 
			Matcher matcher = pattern.matcher(errCodeStr); 
			int regionEnd = matcher.regionEnd();
			while(matcher.find()){
				String errcode = matcher.group(2);
				matcher.region(matcher.end(2), regionEnd);
				if(errcode.getBytes("UTF-8").length>7){break;}
				AMnperrcode mnpErr = new AMnperrcode();
				mnpErr.setErrorcode(errcode);
				mnpErr.setType(type);
				mnpErr.setMnptype(mnpType);
				mnpErr.setCreatetime(new Timestamp(System.currentTimeMillis()));
				lists.add(mnpErr);
			}
			if(lists.size()==0){
//				out.print("noErrCode");
				result = "noErrCode";
				return;
			}
			size = mnpErrCodeManageBiz.saveMnpErrCodeList(lists);
			if(size>0){
				result = "success["+size+"]";
			}else{
				result = "noErrCode";
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "手动添加携号转网错误码发生异常！");
//			out.print("exception");
			result = "exception";
		}
		finally
		{
			//增加操作日志
			Object obj = null;
			try {
				obj = request.getSession(false).getAttribute("loginSysuser");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"session为null");
			}
			LfSysuser lfSysuser = (LfSysuser)obj;
			if(lfSysuser!=null && lfSysuser.getUserId()!=null){
			userId = lfSysuser.getUserId().toString();
			corpCode = lfSysuser.getCorpCode();
			userName = lfSysuser.getUserName();
			}
			
			opContent = "新增携号转网错误代码成功。（类型："+ errCodeTypeStr + "错误代码:"+errCodeStr+")";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			out.print(result);
		}
	}

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
			EmpExecutionContext.error(e,opModule+opType+opContent+"日志写入异常");
		}
	}
}

