<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%
@ SuppressWarnings("unchecked")
HashMap<String,JSONObject> resultMap = (HashMap<String,JSONObject>)request.getAttribute("resultMap");
@ SuppressWarnings("unchecked")
HashMap<String,JSONObject> plantMap = (HashMap<String,JSONObject>)request.getAttribute("plantMap");
String plantType = request.getParameter("plantType");
String isPreview = request.getParameter("isPreview");

//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
%>
<%if("normal_head".equals(plantType)){
    //head
    JSONObject normal_tab_filed_values = resultMap.get("normal_head");
    JSONArray head_items = (JSONArray)normal_tab_filed_values.get("items");
%>
   <%-- 滚动图片 start --%>
   <div id="imgs" class="normal_banner plant" plantType="normal_head" plantId="<%=plantMap.get("normal_head")%>">
		<div class="box_skitter box_skitter_large">
			<ul class="slides">
			 	 <%for(int i=0;i<head_items.size();i++) {
		       		JSONObject head_item = (JSONObject)head_items.get(i);
		   		 %>
		   		 <li class="<%= "0".equals(String.valueOf(i)) ? "first" : "" %>">
		   		 <%--
		   		 System.out.println(  head_item.get("head_imgurl").toString().matches("wzgl/site/img/wzpic_b\\d.[a-zA-Z]{3}")?
				 head_item.get("head_imgurl").toString().replace(".","_" + langName + "."):head_item.get("head_imgurl").toString());
							 
				处理图片路径问题，如果图片格式为wzpic_bn.png,则加上langName并显示
				    --%>
				    <img style="width:100%;height:130px;" src="<%= head_item.get("head_imgurl").toString().matches("wzgl/site/img/wzpic_b\\d.[a-zA-Z]{3}")?
					 head_item.get("head_imgurl").toString().replace(".","_" + langName + "."):head_item.get("head_imgurl").toString()%>" >
				    
				 </li>
		   		 <% }%>		
			</ul>
		</div>	  
	</div>
	<%-- 滚动图片 end --%>
<%}else if("normal_link".equals(plantType)){
   //normal_link
   JSONObject normal_link_filed_values = resultMap.get("normal_link");
   JSONArray link_items = (JSONArray)normal_link_filed_values.get("items");
%>
	<%-- 服务电话 start--%>
	<%for(int i=0;i<link_items.size();i++) {
       		JSONObject link_item = (JSONObject)link_items.get(i);
       		String colorval = "#" + link_item.get("link_bgcolor");
   		 %>
	<div class="normal_link plant" plantType="normal_link" style="background-color:<%= colorval%>" plantId="<%=plantMap.get("normal_link")%>">
		<p id="link-phone"><%=link_item.get("link_note") %>： <%=link_item.get("link_phone") %></p>
	</div>
	<% }%>
	<%-- 服务电话 end --%>
<%} else if("normal_list".equals(plantType)){
    //normal_list
    JSONObject normal_list_filed_values = resultMap.get("normal_list");
    JSONArray list_items = (JSONArray)normal_list_filed_values.get("items");
%>
	<%-- 列表 start --%>
	<div class="normal_list plant" plantType="normal_list" plantId="<%=plantMap.get("normal_list")%>">
	  <ul>
	  <%for(int i=0;i<list_items.size();i++) {
       		JSONObject list_item = (JSONObject)list_items.get(i);
   		 %>
	  	<li>
	  		<div class="info">
	  			<p class="title"><%=list_item.get("list_title") %></p>
	  			<p class="desc"><%=list_item.get("list_note") %></p>
	  		</div>
	  		<img class="avatar"  src='<%=list_item.get("list_imgurl") %>'/>
	  	</li>
		<% }%>
	  </ul>
	</div>
	<%-- 列表 end --%>
<%} else if("normal_menu".equals(plantType)){
  	//normal_menu
    JSONObject normal_menu_filed_values = resultMap.get("normal_menu");
    JSONArray menu_items = (JSONArray)normal_menu_filed_values.get("items");
%>
<div style="height:35px; width:100%;"></div>
	<%-- 菜单 start --%>
	<div class="normal_menu plant" plantType="normal_menu" style="bottom:0;position:<%="1".equals(isPreview)?"fixed": "absolute"%>;width:100%;" plantId="<%=plantMap.get("normal_menu")%>">
	<ul class="menu">
	   <%for(int i=0;i<menu_items.size();i++) {
       		JSONObject menu_item = (JSONObject)menu_items.get(i);
       		String fontcolor = "#" + menu_item.get("menu_fontcolor");
       		String bgcolor = "#" + menu_item.get("menu_bgcolor");
   		 %>
	   <li style="background-color:<%= bgcolor%>"><a style="color:<%= fontcolor%>" href="#1" data-name="t_<%=i%>"><%=menu_item.get("menu_title")%></a></li>
	   <% }%>
	 </ul>
	</div>
	<%-- 菜单 end --%>
<%}else if("normal_tab".equals(plantType)) {
    //normal_tab
    JSONObject normal_tab_filed_values = resultMap.get("normal_tab");
    JSONArray tab_items = (JSONArray)normal_tab_filed_values.get("items");
%>
<%-- 多标签控件  start--%>
<ul class="mytabs">
   <%for(int i=0;i<tab_items.size();i++) {
       JSONObject tab_item = (JSONObject)tab_items.get(i);
   	%>
   	<li <%= "0".equals(String.valueOf(i)) ? "class='active'" : "" %>><a href="#1" data-name="t_<%=i%>"><%=tab_item.get("tab_name")%></a></li>
   <% }%>
 </ul>
<div class="normal_tab plant" plantType="normal_tab" plantId="<%=plantMap.get("normal_tab")%>">
 <ul style="height:30px;" id="mytabs_c"></ul>
 <div class="tabContent">
   	<%for(int i=0;i<tab_items.size();i++) {
       JSONObject tab_item = (JSONObject)tab_items.get(i);
   	%>
     <div id="t_<%=i%>" style="<%= !"0".equals(String.valueOf(i)) ? "display:none;" : "" %>">
      <div style="padding:10px;text-align:center;font-weight:bold;" class="title"><%=tab_item.get("tab_title")%></div>
	 	 <div class="thumbnail">
	 	 
	     	<img style="margin-left:5px;width:250px;height:134px;" src="<%= tab_item.get("tab_imgurl").toString().matches("wzgl/site/img/wzpic_b\\d.[a-zA-Z]{3}")?
			 tab_item.get("tab_imgurl").toString().replace(".","_" + langName + "."):tab_item.get("tab_imgurl").toString()%>" >
	 	 </div>
   	     <div class="note"><%= tab_item.get("tab_content") %></div>
     	</div>
     <%}%>
 </div>
</div>
<%-- 多标签控件 --%>
<%}else if("normal_content".equals(plantType)){
    //normal_content
    JSONObject normal_content_filed_values = resultMap.get("normal_content");
    JSONArray content_items = (JSONArray)normal_content_filed_values.get("items");
%>
<%-- 内容显示控件 start--%>
	<div class="normal_content plant" plantType="normal_content" plantId="<%=plantMap.get("normal_content")%>">
	<div class="content">
		<%for(int i=0;i<content_items.size();i++) {
       		JSONObject content_item = (JSONObject)content_items.get(i);
   		%>
		<p class="title"><%=content_item.get("content_title") %></p>
	     <div id="t_1" style="padding-top:5px;">
		 <div class="thumbnail">
		  
		     <center><img style="width:100%;height:132px" src="<%=content_item.get("content_imgurl").toString().matches("wzgl/site/img/wzpic_b\\d.[a-zA-Z]{3}")?
			 content_item.get("content_imgurl").toString().replace(".","_" + langName + "."):content_item.get("content_imgurl").toString()%>" /></center>
		 </div>
	   	     <div class="note"><%=content_item.get("content_body") %></div>
	     </div>
	 	<%}%>
	 </div>
	 </div>
<%-- 内容显示控件 end--%>
<%}else if("normal_scontent".equals(plantType)){ 
  	//normal_contents
    JSONObject normal_scontent_filed_values = resultMap.get("normal_scontent");
    JSONArray content_items = (JSONArray)normal_scontent_filed_values.get("items");
%>
<%-- 内容显示控件 start--%>
	<div class="normal_content plant" plantType="normal_scontent" plantId="<%=plantMap.get("normal_scontent")%>">
	<div class="content">
		<%for(int i=0;i<content_items.size();i++) {
       		JSONObject content_item = (JSONObject)content_items.get(i);
   		%>
		 <p class="title"><%=content_item.get("content_title") %></p>     
	     <div id="t_1" style="padding-top:5px;">
			 <div class="thumbnail">
			     <img style="width:100%;height:132px" src="<%=content_item.get("content_imgurl").toString().matches("wzgl/site/img/wzpic_b\\d.[a-zA-Z]{3}")?
				 content_item.get("content_imgurl").toString().replace(".","_" + langName + "."):content_item.get("content_imgurl").toString()%>" >
			 </div>
	     </div>
	 	<%}%>
	 	</div>
	 </div>
<%-- 内容显示控件 end--%>
<%} %>