<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.util.StringUtils"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.site.LfSitType"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
    
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo) request.getAttribute("pageInfo");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressWarnings("unchecked")
    List<LfSitType> otSitTypeList = (List<LfSitType>)request.getAttribute("otSitTypeList");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/frame.css"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/table.css"/>
		<%--<link rel="stylesheet" href="<%=path%>/wxcommon/css/flexslider.css" /> --%>
		
		<link href="<%=commonPath%>/wxcommon/skitter/css/skitter.styles.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" media="all" rel="stylesheet" />
		<link href="<%=commonPath%>/wxcommon/skitter/css/styles.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" media="all" rel="stylesheet" />
		<link href="<%=iPath%>/css/new.skitter.styles2.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" media="all" rel="stylesheet" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
	<%-- 当前位置 --%>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<div id="container" class="container">
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			
			<div id="rContent" class="rContent">
					<div style="display: none" id="hiddenValueDiv">
					</div>
					<div class="buttons">
						<input type="button" id="cancel" onclick="javascript:doreturn();" value="<emp:message key="common_btn_10" defVal="返回"
												fileName="common" />" class="btnClass2" style="float:right;margin:0 0 5px 0;">
					</div>					
					<div style="margin: 0 auto;">
								<%
							    if(null != otSitTypeList && otSitTypeList.size() > 0)
							    {
							        for (LfSitType info : otSitTypeList)
							        {
								%>
									<div style="width:210px;height:300px;float:left;padding:5px;text-align: center;margin: 0px 8px 6px;" class=" div_bd">
									<h3 class="div_bg div_bd" style="padding:5px 0;margin-bottom:5px;">
										<emp:message key="wzgl_qywx_site_text_57" defVal="返回"
												fileName="wzgl" />
									</h3>
										 <%
										 String imgurl = info.getImgUrl0();
										 if(imgurl!=null&&!"".equals(imgurl))
										 {
										 %>
										 	<div class="box_skitter box_skitter_large">
											  <ul class="slides">
											    <li>
											      <img src="<%= inheritPath + info.getImgUrl0()%>" style="width:200px;height:242px;"/>
											    </li>
											    <li>
											      <img src="<%=inheritPath + info.getImgUrl1()%>" style="width:200px;height:242px;"/>
											    </li>
											    <li>
											      <img src="<%=inheritPath + info.getImgUrl2()%>" style="width:200px;height:242px;"/>
											    </li>
											    <li>
											      <img src="<%=inheritPath + info.getImgUrl3()%>" style="width:200px;height:242px;"/>
											    </li>
											  </ul>
											</div>
		                           		<%} else { %>
		                           		    <div class="box_skitter box_skitter_large">
		                           		     	<ul class="slides">
			                           		     	<li>
			                           		     		<img src="<%= inheritPath + info.getImgUrl()%>" style="width:200px;height:242px;"/>
			                           		     	</li>
	                           	    	  		</ul>
	                           	    	  	</div>
			                            <% } 
			                            int isDefault = info.getIsDefault();
			                            if(isDefault==1){ %>
			                           		<a style="position:relative;top:5px;" href="javascript:nextPage(<%=info.getTypeId() %>)"><emp:message key="wzgl_qywx_site_text_30" defVal="应用风格"
												fileName="wzgl" /></a>
			                            <%}else{ %>
			                            <%} %>&nbsp;&nbsp;&nbsp;
			                            <%if(info.getImgUrl0()==null||"".equals(info.getImgUrl0())){%>
			                            	<a style="position:relative;top:5px;" href="#1"><emp:message key="wzgl_qywx_site_text_31" defVal="暂无预览"
												fileName="wzgl" /></a>
			                            <%}%>
			                            	
		                            </div>
								<%
								    }
							    }
							    else
							    {out.print(MessageUtils.extractMessage("wzgl","wzgl_qywx_form_text_8",request));} %>					
					</div></div>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<div id="pageStuff" style="display: none;">
				<input type="hidden" id="pathUrl" value="<%=path%>" />
				<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
			</div>
			<%-- foot结束 --%>
		
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wzgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/siteList.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<%--<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/jquery.flexslider-min.js"></script> --%>
		
		<script type="text/javascript" language="javascript" src="<%=commonPath%>/wxcommon/skitter/js/jquery.easing.myjquery-p.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" language="javascript" src="<%=commonPath%>/wxcommon/skitter/js/jquery.skitter.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script>
		  $(function(){
			  $("#content td").find("input[name='siteType']:gt(0)").attr("disabled",true);
			  //图片轮询的全局变量
				/*$(function(){
					$('.flexslider').flexslider({
						 animation: "slide",
						 controlNav: false
					  });
				});*/

				$('.box_skitter_large').skitter({
					theme: 'default',
					//numbers_align: 'center',
					progressbar: false, 
					dots: false, 
					preview: true,
					animation:'fade',
					hideTools:true,
					numbers:false,
					label:false		
				});
				  
		   });
		  //返回上一级
		  function doreturn()
		  {
		  	var url = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=find&isOperateReturn=true&lguserid="
		  	    +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
		  	window.location.href = url;
		  }
		</script>
	</body>
</html>