package com.montnets.emp.wymanage.servlet;

import java.io.IOException;
import java.text.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wy.AGwroute;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.wymanage.biz.RouteManageBiz;

/**
 * 网优路由绑定
 * @description 
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2014-3-24 上午11:48:38
 */
public class wy_routeManageSvt extends BaseServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1937276757187297993L;
	
	private final String empRoot="wygl";
	private final String base="/wymanage";
	private final RouteManageBiz routeBiz = new RouteManageBiz();
	protected final SuperOpLog spLog = new SuperOpLog();
	
	/**
	 * 网优路由绑定查询
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @datetime 2014-3-24 上午11:50:21
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		long stime = System.currentTimeMillis();
		try {
			List<DynaBean> routeList = null;
			List<DynaBean> nobindList = null;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//默认携号转网
			conditionMap.put("type", "0");
			routeList =  routeBiz.getRoute(null, conditionMap);
			nobindList =  routeBiz.getRouteNotBind(conditionMap);
			JSONArray array = getJson(routeList, nobindList);
			request.setAttribute("json", array.toString());
			request.getRequestDispatcher(this.empRoot+base+"/wy_routeManage.jsp").forward(request,
					response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"网优路由绑定","("+sDate+")查询", StaticValue.GET);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网优路由绑定查询异常！");
			request.setAttribute("json", "error");
			try {
				request.getRequestDispatcher(this.empRoot+base+"/wy_routeManage.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"网优路由绑定查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"网优路由绑定查询异常！");
			}
		}
	}
	
	/**
	 * 网优路由绑定查询(根据路由类型查询)
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @datetime 2014-3-24 上午11:50:21
	 */
	public void getJsonByType(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String result = "error";
		try {
			List<DynaBean> routeList = null;
			List<DynaBean> nobindList = null;
			//路由类型
			String type = request.getParameter("type");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//默认携号转网
			conditionMap.put("type", type);
			routeList =  routeBiz.getRoute(null, conditionMap);
			nobindList =  routeBiz.getRouteNotBind(conditionMap);
			JSONArray array = getJson(routeList, nobindList);
			result = array.toString();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网优路由绑定查询(根据路由类型查询)异常！");
		}
		finally
		{
			response.getWriter().print(result);
		}
	}
	
	/**
	 * json数据处理
	 * @description    
	 * @param routeList
	 * @param nobindList
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-3-27 上午10:48:00
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getJson(List<DynaBean> routeList,List<DynaBean> nobindList)
	{
		JSONArray array = new JSONArray();
		for(int i=0;i<routeList.size();i++)
		{
			JSONObject jsObject = new JSONObject();
			DynaBean bean = routeList.get(i);
			String gatename=(bean.get("gatename")!=null?bean.get("gatename").toString().replaceAll("<","&lt").replaceAll(">","&gt").replaceAll("'","&acute;"):"");
			String corpsign=(bean.get("corpsign")!=null?bean.get("corpsign").toString().replaceAll("<","&lt").replaceAll(">","&gt").replaceAll("'","&acute;"):"");
			//是否绑定
			jsObject.put("is_bind", "yes");
			//id
			jsObject.put("id", bean.get("id"));
			//名称
			jsObject.put("ip_id", bean.get("ip_id"));
			//通道id
			jsObject.put("ip_gateid",  bean.get("ip_gateid"));
			//通道名称
			jsObject.put("gatename", gatename );
			//企业签名
			jsObject.put("corpsign",  corpsign);
			//是否绑定
			jsObject.put("spgate", bean.get("spgate"));
			array.add(jsObject);
			
		}
		for(int i=0;i<nobindList.size();i++)
		{
			
			JSONObject jsObject = new JSONObject();
			DynaBean bean = nobindList.get(i);
			String gatename=(bean.get("gatename")!=null?bean.get("gatename").toString().replaceAll("<","&lt").replaceAll(">","&gt").replaceAll("'","&acute;"):"").replaceAll("\"", "&quot;");
			String corpsign=(bean.get("corpsign")!=null?bean.get("corpsign").toString().replaceAll("<","&lt").replaceAll(">","&gt").replaceAll("'","&acute;"):"").replaceAll("\"", "&quot;");
			//是否绑定
			jsObject.put("is_bind", "no");
			//id
			jsObject.put("id","-1");
			//名称
			jsObject.put("ip_id", bean.get("ip_id"));
			//通道id
			jsObject.put("ip_gateid",  bean.get("ip_gateid"));
			//通道名称
			jsObject.put("gatename",  gatename);
			//企业签名
			jsObject.put("corpsign",  corpsign);
			//是否绑定
			jsObject.put("spgate", bean.get("spgate"));
			array.add(jsObject);
			
		}
		return array;
	}
	
	/**
	 * 绑定路由通道
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @datetime 2014-3-24 上午11:50:21
	 */
	public void bind(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String opModule = "网优路由绑定";
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "OTHER";
		String OpStatus = null;
		//操作模块
		//String opModule = "网优路由绑定";
		String res = "error";
		String opSper = StaticValue.OPSPER;
		String opTypeSp=StaticValue.OTHER;
		String opContentSp = "网优绑定路由通道";
		String lgusername = request.getParameter("lgusername");
		String lgcorpcode = request.getParameter("lgcorpcode");
		//路由类型
		String type = request.getParameter("type");
		String oldBindStr = null;
		String BindStr = null;
		try {
			//通道id字符串拼接
			String id = request.getParameter("id");
			//先查出原绑定信息
			oldBindStr = getBindByType(type);
			routeBiz.bind(id,type);
			BindStr = getBindByType(type);
			res = "success";
			spLog.logSuccessString(lgusername, opModule, opTypeSp, opContentSp,lgcorpcode);
			OpStatus = "成功";
		} catch (Exception e) {
			OpStatus = "失败";
			spLog.logFailureString(lgusername, opModule, opTypeSp, opContentSp+opSper, null,lgcorpcode);
		}
		finally{
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
			
			opContent = "绑定网优路由通道"+OpStatus+"。（路由类型："+type+"，绑定路由通道信息："+oldBindStr+")-->（路由类型："+type+"，绑定路由通道信息："+BindStr+")";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			response.getWriter().write(res);
		}
	}

	/**
	 * 网优路由绑定查询(根据路由类型查询)
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @datetime 2014-3-24 上午11:50:21
	 */
	public String getBindByType(String type) throws IOException
	{
		String result = "";
		try {
			List<DynaBean> routeList = null;
			List<DynaBean> nobindList = null;
			//路由类型
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//默认携号转网
			conditionMap.put("type", type);
			routeList =  routeBiz.getRoute(null, conditionMap);
			nobindList =  routeBiz.getRouteNotBind(conditionMap);
			JSONArray array = getJson(routeList, nobindList);
			JSONArray arrayNew = null;
			//去除未绑定的通道
			int num = array.size();
			if (array!=null &&array.size()>0)
			{
				arrayNew = new JSONArray();
				for(int i=0;i< num;i++){
					Map o=(Map)array.get(i);
					if(o.get("is_bind").equals("yes")){
						//array.remove(o);
						arrayNew.add(o);
					}
				}
				result = arrayNew.toString();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网优路由绑定查询(根据路由类型查询)异常！");
		}
		return result;
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
