<%@page language="java" import="java.util.*" %>
<%@ page import="com.montnets.emp.netnews.entity.LfWXBASEINFO" %>
<%@ page import="com.montnets.emp.netnews.daoImpl.Wx_ueditorDaoImpl" %>


<%!
Wx_ueditorDaoImpl ueditorDao = new Wx_ueditorDaoImpl();
%>

<%
	String pageId ="";
	if(request.getAttribute("pageId") != null)
	{
		pageId = (String)request.getAttribute("pageId");
	}
	
	String phone = "";
	if(request.getAttribute("phone") != null){
		phone = (String)request.getAttribute("phone");
	}
	
	String taskId = "";
	if(request.getAttribute("taskId") != null){
		taskId = (String)request.getAttribute("taskId");
	}
	
	
	LfWXBASEINFO base =  ueditorDao.getNetInfoByPageId(pageId);
	
	Map<Integer,String> infoMap = new HashMap<Integer,String>();
	if(base != null&&!"".equals(base.getDynTableName())){
		infoMap = ueditorDao.getDynDataInfo(base.getNETID().toString(), taskId, phone, base.getDynTableName(), base.getParams());
	}

%>

						 