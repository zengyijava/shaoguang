<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
/**
备注：
1.控件表单编辑页面
2.wzgl_normal_page1(模块名+微站风格类型+当前页面类型)
3.异步请求getPageInfo返回该页面
5.改页面需要的数据
	1.otSitPlantList
	2.resultMap的格式为:
    	resultMap = {"控件类型":"控件值"，["控件类型":"控件的值"],...}
	例如图片滚动类型:
		控件类型： normal_head
		控件值: {"plantId":"620","plantType":"normal_head","count":2,"items":[{"head_link":"http:\/\/www.baidu.com","head_imgurl":"http:\/\/www.baidu.com","head_title":"我的世界"},{"head_link":"http:\/\/www.baidu.com","head_imgurl":"http:\/\/www.baidu.com","head_title":"你的世界1"}]}
    3.plantMap格式为：
    	plantMap = {"控件类型"："控件ID"，["控件类型"："控件ID"]，...}**/
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
//当前页面的类型
String ptype = request.getParameter("ptype");
//当前页面的控件类型
String[] plants = (String[])request.getAttribute("plants");

//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

//页面数据信息
HashMap<String,JSONObject> resultMap = (HashMap<String,JSONObject>)request.getAttribute("resultMap");
HashMap<String,JSONObject> plantMap = (HashMap<String,JSONObject>)request.getAttribute("plantMap");
%>
<style type="text/css">
	.normal_list li a:hover,.normal_menu li a:hover{
		text-decoration:none;
	}
</style>
<div id="page_item_<%=ptype %>" ptype="<%=ptype%>" class="page_item_view">
		<%--页面控件 start--%>
		<%if(null!=plants&&plants.length>0) {%>
			<%for(int n=0;n<plants.length;n++){%>
			  <%
			  if("normal_head".equals(plants[n])){
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
										 <img style="width:100%;height:130px;" 
	 src="<%= head_item.get("head_imgurl").toString().matches("wzgl/site/img/wzpic_b\\d.[a-zA-Z]{3}")?
			 head_item.get("head_imgurl").toString().replace(".","_" + langName + "."):head_item.get("head_imgurl").toString() %>"
											 alt="image" 
											 data-href="<%=head_item.get("head_tpvalue") %>">						 
								 </li>
						   		 <% }%>		
							</ul>
						</div>	  
					</div>
					<%-- 滚动图片 end --%>
				<%}else if("normal_link".equals(plants[n])){
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
				<%} else if("normal_list".equals(plants[n])){
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
					  	<li data-href="<%=list_item.get("list_tpvalue") %>">
					  		<div class="info">
					  			<p class="title"><%=list_item.get("list_title") %></p>
					  			<p class="desc"><%=list_item.get("list_note") %></p>
					  		</div>
					  		<img class="avatar"  src="<%= list_item.get("list_imgurl") %>">
					  	</li>
						<% }%>
					  </ul>
					</div>
					<%-- 列表 end --%>
				<%} else if("normal_menu".equals(plants[n])){
				  	//normal_menu
				    JSONObject normal_menu_filed_values = resultMap.get("normal_menu");
				    JSONArray menu_items = (JSONArray)normal_menu_filed_values.get("items");
				%>
				<div style="height:35px; width:100%;"></div>
					<%-- 菜单 start --%>
					<div class="normal_menu plant" plantType="normal_menu" style="bottom:0;position:absolute;width:100%;" plantId="<%=plantMap.get("normal_menu")%>">
					<ul class="menu">
					   <%for(int i=0;i<menu_items.size();i++) {
				       		JSONObject menu_item = (JSONObject)menu_items.get(i);
				       		String fontcolor = "#" + menu_item.get("menu_fontcolor");
				       		String bgcolor = "#" + menu_item.get("menu_bgcolor");
				   		 %>
					   <li style="background-color:<%= bgcolor%>" data-href="<%=menu_item.get("menu_tpvalue") %>"><a style="color:<%= fontcolor%>" data-name="t_<%=i%>"><%=menu_item.get("menu_title")%></a></li>
					   <% }%>
					 </ul>
					</div>
					<%-- 菜单 end --%>
				<%}else if("normal_tab".equals(plants[n])) {
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
					     			tab_item.get("tab_imgurl").toString().replace(".","_" + langName + "."):tab_item.get("tab_imgurl").toString()%>">
					 	 </div>
				   	     <div class="note"><%= tab_item.get("tab_content") %></div>
				     	</div>
				     <%}%>
				 </div>
				</div>
				<%-- 多标签控件 --%>
				<%}else if("normal_content".equals(plants[n])){
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
						     <center><img style="width:100%;height:132px" src="<%= content_item.get("content_imgurl").toString().matches("wzgl/site/img/wzpic_b\\d.[a-zA-Z]{3}")?content_item.get("content_imgurl").toString().replace(".","_" + langName + "."):content_item.get("content_imgurl").toString()%>" '/></center>
						 </div>
					   	     <div class="note"><%=content_item.get("content_body") %></div>
					     </div>
					 	<%}%>
					 </div>
					 </div>
				<%-- 内容显示控件 end--%>
				<%}else if("normal_scontent".equals(plants[n])){ 
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
							     <img style="width:100%;height:132px" src="<%= content_item.get("content_imgurl").toString().matches("wzgl/site/img/wzpic_b\\d.[a-zA-Z]{3}")?content_item.get("content_imgurl").toString().replace(".","_" + langName + "."):content_item.get("content_imgurl").toString()%>">
							 </div>
					     </div>
					 	<%}%>
					 	</div>
					 </div>
				<%-- 内容显示控件 end--%>
				<%} %>
	  		<%} %>
		<%} %>
		<%--页面控件  end--%>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		var h = $("#imgs .slides li:first img").css("height");
		 $("#imgs .slides").css("height",h);
		 $("#imgs .slides").css("overflow","hidden");
		
		$('.box_skitter_large').skitter({
			theme: 'default',
			//numbers_align: 'center',
			progressbar: false, 
			dots: false, 
			preview: true,
			animation:'fade',
			hideTools:true,
			numbers:false,
			navigation:false,
			label:false		
		});
		
		$(".normal_list li,.normal_menu li").live("click",function(){
			var value= $.trim($(this).attr("data-href"));
			if(value!=undefined&&""!=value){
				var pattern1=/http(s)?:\/\/([\w-]+\.)+[\w-]+([\w-]*)?/;
				var pattern2=/^[a-zA-Z\d]+$/;
				 if(pattern1.test(value)){
					window.location.href=value;
				 }else if(pattern2.test(value)){
					window.location.href= "<%=path%>" + "/wzgl_sitePreview.hts?urlToken="+value; 
				 }
			}
		});
		
		$(".container_skitter img").live("click",function(){
			var value = "";
			var img = this;
			
			$(".normal_banner img").each(function(i){
				var src = $(img).attr("src");
				var currentsrc= $(this).attr("src");
				if(src!=undefined&&""!=src&&src==currentsrc){
					value = $.trim($(this).attr("data-href"));
				}
				if(value!=""){
					var pattern1=/http(s)?:\/\/([\w-]+\.)+[\w-]+([\w-]*)?/;
					var pattern2=/^[a-zA-Z\d]+$/;
					if(pattern1.test(value)){
						window.location.href=value;
					}else if(pattern2.test(value)){
						window.location.href= "<%=path%>" + "/wzgl_sitePreview.hts?urlToken="+value; 
					}
				}
			});
			
		});
	});	
</script>