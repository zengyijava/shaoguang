<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
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
//页面数据信息
HashMap<String,JSONObject> resultMap = (HashMap<String,JSONObject>)request.getAttribute("resultMap");
HashMap<String,JSONObject> plantMap = (HashMap<String,JSONObject>)request.getAttribute("plantMap");
//使用集群，文件服务器的地址
String filePath = GlobalMethods.getWeixFilePath();
%>
<div id="page_item_<%=ptype %>" ptype="<%=ptype%>" class="page_item_view">
		<%--页面控件 start--%>
		<%if(null!=plants&&plants.length>0) {%>
			<%for(int i=0;i<plants.length;i++){%>
			<jsp:include page="wzgl_normal_page_plants.jsp">
	       		<jsp:param value="<%=filePath%>" name="filePath" />
	       		<jsp:param value="<%=resultMap%>" name="resultMap" />
	       		<jsp:param value="<%=plantMap%>" name="plantMap" />
	       		<jsp:param value="<%=plants[i] %>" name="plantType" />
	  		</jsp:include>
	  		<%} %>
		<%} %>
		<%--页面控件  end--%>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		/*$('.flexslider').flexslider({
		    animation: "slide",
		    controlNav: false
	  	});*/

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
	});	
</script>