<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.entity.site.LfSitPage"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
/**
备注：
1.控件表单编辑页面
2.wzgl_normal_page1(模块名+微站风格类型+当前页面类型)
3.异步请求copyPageInfo返回该页面
5.改页面需要的数据
	1.otSitPlantList
	2.resultMap的格式为:
    	resultMap = {"控件类型":"控件值"，["控件类型":"控件的值"],...}
	例如图片滚动类型:
		控件类型： normal_head
		控件值: {"plantId":"620","plantType":"normal_head","count":2,"items":[{"head_link":"http:\/\/www.baidu.com","head_imgurl":"http:\/\/www.baidu.com","head_title":"我的世界"},{"head_link":"http:\/\/www.baidu.com","head_imgurl":"http:\/\/www.baidu.com","head_title":"你的世界1"}]}
    3.plantMap格式为：
    	plantMap = {"控件类型"："控件ID"，["控件类型"："控件ID"]，...}
**/
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
HashMap<String,JSONObject> resultMap = (HashMap<String,JSONObject>)request.getAttribute("resultMap");
HashMap<String,JSONObject> plantMap = (HashMap<String,JSONObject>)request.getAttribute("plantMap");

//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

//使用集群，文件服务器的地址
String filePath = GlobalMethods.getWeixFilePath();
LfSitPage otSitpage = (LfSitPage)request.getAttribute("otSitPage");
%>
<div style="" class="nav_item_wrapper l2son nav_item_level2_wrapper" id="level0_2_<%=otSitpage.getPageId()%>">
  <div sid="<%=otSitpage.getSId()%>" pageid="<%=otSitpage.getPageId()%>" ptype="<%= otSitpage.getPageType() %>" class="nav_item level2" url="<%=otSitpage.getUrl()%></div>">       
     <div class="nav_bd">         
	     <span class="nav_icon">
		     <i class="add_on_icon10 circle"></i>
		 </span>         
		 <ul class="nav_opr_list">
		   <li class="nav_opr_item">
				<a title="<emp:message key="wzgl_qywx_form_text_24" defVal="复制菜单"
												fileName="wzgl" />" class="nav_opr_btn nav_opr_add_btn"
					href="javascript:;"> <i class="opr_icon16 add_icon"></i>
				</a>
		   </li>           
		   <li class="nav_opr_item">
			   <a title="<emp:message key="wzgl_qywx_form_text_25" defVal="修改菜单名称"
												fileName="wzgl" />" class="nav_opr_btn nav_opr_edit_btn" href="javascript:;">
			      <i class="opr_icon16 edit_icon"></i>
			   </a>
		   </li>           
		   <li class="nav_opr_item"><a title="<emp:message key="wzgl_qywx_form_text_26" defVal="删除菜单"
												fileName="wzgl" />" sortid="1" leavel="2" menus="0" class="nav_opr_btn nav_opr_edit_btn" href="javascript:;">
			    <i class="opr_icon16 del_icon"></i></a>
		   </li>             
		</ul>         
		<h5 class="nav_t">
		    <a class="level2name nav_btn" data-domid="1" title="<%=otSitpage.getName() %>" href="javascript:;"><%=otSitpage.getName() %></a>
		</h5>       
	</div>            
   </div>   
</div>