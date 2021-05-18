<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import= "java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount" %>
<%@page import="com.montnets.emp.wxgl.base.util.TitleImgBean" %>
<%@page import="java.util.ArrayList"%>
<%@page import="com.montnets.emp.ottbase.constant.WXStaticValue"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

String msgType = (String)request.getAttribute("msgType");
String time = (String)request.getAttribute("time");
String tempId = "";
if(request.getAttribute("tempId")!=null){
	tempId=request.getAttribute("tempId").toString();
}else if(request.getAttribute("tetId")!=null){
	tempId = request.getAttribute("tetId").toString();
}
else if(request.getAttribute("evtId")!=null){
	tempId = request.getAttribute("evtId").toString();
}else{
	out.write(MessageUtils.extractMessage("wxgl","wxgl_java_title_23",request));
}
List<TitleImgBean> imgbeans = (List<TitleImgBean>)request.getAttribute("imgbeans");
//单图和多图的第一个标题
String title ="";
if(imgbeans!=null){
	title=imgbeans.get(0).getTitle(); 
	title = title.length()>6?title.substring(0,6)+"...":title;
}
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

%>
<%!
public static String turn(String str){
//下面的代码将字符串以正确方式显示（包括回车，换行，空格）
	 if(str==null) return "";       
     String html = str;
     html = html.replaceAll("(<)(/?+[^aA/][^>]*)(>)","&lt$2&gt");
     return html;
 
}

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/reset.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/chat.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
	</head>
	<body>
		 <div id="container" style="rgba(0,0,0,0.5);">
		 <input type="hidden" id="tempId" value="<%=tempId %>"/>
	<div class="chatMainPanel">
	 
	  <div id="chat_chatmsglist">
	  <%if("0".equals(msgType)){%>
	    <div class="chatItem me">
		   <div class="time">
		     <span class="bd"></span>
			 <span class="text"><%=time.replaceAll("[ :]\\d+", "") %></span>
		   </div>
		   <div class="chatItemContent">
		     <img class="avatar" src="<%=iPath%>/img/me.jpg" />
			 <div class="cloud cloudText">
			   <div class="cloudPannel"> 
			      
				 <div class="cloudBody">
				   <div class="cloudContent">
				     <pre style="white-space:pre-wrap"><%= turn(request.getAttribute("textRely").toString()) %></pre>  
				   </div>  
				 </div> 
				 <div class="cloudArrow "></div>  
			   </div>
			 </div>
		   </div>
		</div>
	   <%}else if("1".equals(msgType)) {%>
	     <% 
	     	String link = "";
	     	if(link.indexOf("weix_imgDetail.hts")!=-1){
				link = basePath + imgbeans.get(0).getLink();
			}else{
				link = imgbeans.get(0).getLink();
			}
	     %>
	    <div class="chatItem  third pic" onclick="showContent('<%= link %>')">
		   
		   <div class="chatItemContent">
			 <div class="cloud cloudText">
			   <div class="cloudPannel"> 
			      
				 <div class="cloudBody">
				   <div class="cloudContent">                                  
				    <h4 class="i-title"> <%=turn(title) %></h4>
				    <div class="single-time"><%=time.replaceAll("[ :]\\d+", "") %></div>
				    <img width="100%" height="84" class="i-img" src="<%= imgbeans.get(0).getUrl()%>">
				    <p><%=request.getAttribute("summary")==null?"":request.getAttribute("summary") %></p>
				   </div>  
				 </div> 
				 
			   </div>
			 </div>
		   </div>
		</div>
	   
	   <%}else if("2".equals(msgType)) { %>
	    <% 
	     	String link0 = "";
	     	if(link0.indexOf("weix_imgDetail.hts")!=-1){
	     		link0 = basePath + imgbeans.get(0).getLink();
			}else{
				link0 = imgbeans.get(0).getLink();
			}
	     %>
	     <div id="appmsg" class="msg-item-wrapper img-border">
					<div class="msg-item multi-msg">
						<div class="appmsgItem chatItemContent" id="appmsgItem1" onclick="showContent('<%= link0%>')">
						   <p class="msg-meta">
        					  <span class="msg-date"><%=time.replaceAll("[ :]\\d+", "") %></span> 
        				   </p>
        				   <div class="cover">                
        				     <h4 class="msg-tt">                 
        				        <span class="i-title"><%=turn(title)  %></span>      
        				     </h4>        
                              <img width="170px" class="i-img" src="<%=imgbeans.get(0).getUrl()%>">            
                            </div>         
					    </div>
					    <%String moreTitle = "";
					   	 for(int i=1;i<imgbeans.size();i++){ 
					   		moreTitle = imgbeans.get(i).getTitle();
					   		String link1 = "";
					     	if(link1.indexOf("weix_imgDetail.hts")!=-1){
					     		link1 = basePath + imgbeans.get(i).getLink();
							}else{
								link1 = imgbeans.get(i).getLink();
							}
					    %>
					    <div id="appmsgItem2" class="rel sub-msg-item appmsgItem chatItemContent" onclick="showContent('<%= link1 %>')">    
					    	
					    		<div style="height:43px;"><span class="thumb" >
					             <img width="40px" class="i-img" src="<%=imgbeans.get(i).getUrl()%>"></span>
					              <h4 class="msg-tt"><span class="i-title"><%=turn(moreTitle)  %></span></h4>
					         	</div> 
					    	 
					             
					    </div>
					    <%}%> 
					      
					</div>   
				</div>
	     
	   <%} %>
	   </div>
	</div>
</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=iPath %>/js/preview.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>