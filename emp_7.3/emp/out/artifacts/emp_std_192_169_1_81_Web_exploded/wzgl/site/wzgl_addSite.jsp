<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="com.montnets.emp.entity.site.LfSitInfo"%>
<%@page import="com.montnets.emp.entity.site.LfSitPage"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
@SuppressWarnings("unchecked")
Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
@SuppressWarnings("unchecked")
Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
String menuCode = titleMap.get(request.getAttribute("rTitle"));
String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
String lguserid = (String) request.getParameter("lguserid");
String lgcorpcode = (String) request.getParameter("lgcorpcode");

//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

//当前微站
LfSitInfo otSitInfo = (LfSitInfo)request.getAttribute("otSitInfo");
//当前微站的pages
@SuppressWarnings("unchecked")
List<LfSitPage> otPageList = (List<LfSitPage>)request.getAttribute("otPageList");
String weixBashPath = GlobalMethods.getWeixBasePath();
String wxskin = skin.indexOf("/frame") == -1 ? "default" : skin.substring(skin.indexOf("/frame")+1, skin.length());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
<title>创建微站</title>
<meta http-equiv="X-UA-Compatible" content="IE=8">
<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=commonPath%>/wxcommon/css/global.css?V=<%=StaticValue.getJspImpVersion() %>">
<link rel="stylesheet" href="<%=commonPath%>/wxcommon/css/colorpicker.css?V=<%=StaticValue.getJspImpVersion() %>">
<%--<link rel="stylesheet" href="<%=commonPath%>/wxcommon/css/flexslider.css"> --%>
<link rel="stylesheet" href="<%=commonPath%>/wxcommon/widget/nanoscroller/nanoscroller.css?V=<%=StaticValue.getJspImpVersion() %>">
<link rel="stylesheet" href="<%=iPath%>/css/site.css?V=<%=StaticValue.getJspImpVersion() %>">
<link rel="stylesheet" href="<%=iPath%>/normal/css/normal.css?V=<%=StaticValue.getJspImpVersion() %>">

<link href="<%=commonPath%>/wxcommon/skitter/css/skitter.styles.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" media="all" rel="stylesheet" />
<link href="<%=commonPath%>/wxcommon/skitter/css/styles.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" media="all" rel="stylesheet" />
<link href="<%=iPath%>/css/new.skitter.styles.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" media="all" rel="stylesheet" />
<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
</head>
<style>
#load-bg {
	position:absolute;
}
</style>

<body>
	<%-- 当前位置 --%>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
	<%--当前页面菜单 --%>
    <%--当前页面菜单 end --%>
    <%--当前页面的隐藏值 --%>
    <div id="logininfos" style="display:none"></div>
	<div id="hiddenValueDiv" style="display:none"></div>
	<%--当前页面的隐藏值  end--%>
	<div class="hd">
	     <div class="copyinfo">
          <p><span style="float:left"><emp:message key="wzgl_qywx_site_text_1" defVal="页面地址："
											fileName="wzgl" /></span><span id="accessUrl"><%=weixBashPath%>wzgl_sitePreview.hts?urlToken=<%=otSitInfo.getUrl()%></span>
           <a class="return" href="javascript:doreturn()"><emp:message key="common_btn_10" defVal="返回"
											fileName="common" /></a>
          </p>
          <%-- <input type="button" id="cancel" onclick="javascript:doreturn();"
          value="返回" class="btnClass6 left" style="background-position:-84px 0px;float:right;margin-top:10px;"> --%>
        </div>
	</div>
	<%-- 页面结构 --%>
	<div id="site_wrap" class="ok" style="height:520px;">
	<%--左侧自定义菜单start--%>
	<div id="pages" class="left div_bd bb_none">
	    <div class="area_hd title_bg">
		    <h4 class="area_t oh ">
			  <span class="area_text"><emp:message key="wzgl_qywx_site_text_2" defVal="页面结构"
											fileName="wzgl" /></span>
			</h4>
		</div> 
	    <%--菜单模块start--%>
		<div class="area_bd">
		   <div id="page_list">
			   <div id="level0_1" class="nav_item_wrapper lev1menu">
				<%--一级菜单start--%>
				<div sid="<%=otSitInfo.getSId()%>" stype="<%=otSitInfo.getTypeId()%>" pageid="0" ptype="0" url="<%=otSitInfo.getUrl()%>"  class="nav_item level1">
					<div class="nav_bd">
						<%-- <span class="nav_icon">
						<i class="opr_icon18 switch_icon"></i> </span> --%>
						<ul class="nav_opr_list">
							<li class="nav_opr_item">
								<a class="nav_opr_btn nav_opr_edit_btn"> <i class="opr_icon16 edit_icon"></i>
								</a>
							</li>
						</ul>
						<h4 class="nav_t">
							<a class="level1name nav_btn" title="test2"
								href="javascript:;"><%=otSitInfo.getName()%></a>
						</h4>
					</div>
				</div>
				<%--一级菜单end--%>
		   		<div class="sub_nav_list lev2menu ui-sortable ui-sortable-disabled" style="height:410px ">
	   				<div class="nano menuContent" style="height:100%;_overflow:auto;">
						<div class="nano-content" >
				   		<%--二级子菜单start--%>
				   		<%if(otPageList!=null) {%>
				   		<%for(LfSitPage otSitpage : otPageList){ %>
						   <div style="" class="nav_item_wrapper l2son nav_item_level2_wrapper" id="level0_2_<%=otSitpage.getPageId()%>">
							  <div sid="<%=otSitpage.getSId()%>" pageid="<%=otSitpage.getPageId()%>" ptype="<%= otSitpage.getPageType() %>" class="nav_item level2" url="<%=otSitpage.getUrl()%>">       
							     <div class="nav_bd">         
								     <span class="nav_icon">
									     <i class="add_on_icon10 circle"></i>
									 </span>         
									 <ul class="nav_opr_list">
									   <li class="nav_opr_item">
											<a title="<emp:message key="wzgl_qywx_site_text_4" defVal="复制菜单"
											fileName="wzgl" />" class="nav_opr_btn nav_opr_add_btn"
												href="javascript:;"> <i class="opr_icon16 add_icon"></i>
											</a>
									   </li>           
									   <li class="nav_opr_item">
										   <a title="<emp:message key="wzgl_qywx_site_text_5" defVal="修改菜单名称"
											fileName="wzgl" />" class="nav_opr_btn nav_opr_edit_btn" >
										      <i class="opr_icon16 edit_icon"></i>
										   </a>
									   </li>           
									   <li class="nav_opr_item"><a title="<emp:message key="wzgl_qywx_site_text_6" defVal="删除菜单"
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
						     <% } %>
						 <% } %>
				  	 	 <%--二级子菜单end--%>
		  	 	 		</div>
		  	 	 	</div>
			  	 </div>
			</div>
		</div>
		 <div id="pos_load"><div id="loading"><img src="<%=iPath%>/img/loading.gif"></div></div>  
	   	</div>
		<%--菜单模块end--%>
	   </div>
       <%--左侧自定义菜单end--%>
       
       <%-- 右侧预览界面  --%>
       <div class="right div_bd bb_none" id="show_content" style="float:left;margin-left:10px;">
		  <div class="area_hd title_bg">
		    <h4 class="area_t oh ">
			  <span class="area_text"><emp:message key="wzgl_qywx_site_text_7" defVal="手机预览效果"
											fileName="wzgl" /></span>
			</h4>
		  </div>
		  <div class="area_content_show ">
			  <div class="nano showContent" style="height:100%;_overflow:auto;">
				  <div class="nano-content" >
				    <%--预览页面 start--%>
				  	<div id="default_show" style="display:none;">
				      <div class="tips">
				       <p><emp:message key="wzgl_qywx_site_text_8" defVal="选择左侧页面，这里可以看到预览效果。"
											fileName="wzgl" /></p>
				     </div>
				    </div>
					   <div id="page_item_normal_page1" ptype="normal_page1" class="page_item_view" style="display: none;"></div>
					   <div id="page_item_normal_page2" ptype="normal_page2" class="page_item_view" style="display: none;"></div>
					   <div id="page_item_normal_page3" ptype="normal_page3" class="page_item_view" style="display: none;"></div>
					   <div id="page_item_normal_page4" ptype="normal_page4" class="page_item_view" style="display: none;"></div>
				    <%--预览页面  end--%>
			    </div>
		    </div>
		  </div>
	  </div>
	  <%-- 右侧预览界面 end --%>
	  
	  <%-- 可编辑区域 start --%>
      <div class="right left div_bd bb_none edit_content"  id="edit_content">
		  <div class="area_hd title_bg">
		    <h4 class="area_t oh " id="edit_area">
			  <span class="area_text"><emp:message key="wzgl_qywx_site_text_9" defVal="可编辑区域"
											fileName="wzgl" /></span>
			  <span class="area_t_opr buttons">
			     <a href="#1" class="addNoti" id="add_save_btn" style="background-position: 0px 0px; line-height: 2.2em;" title='<emp:message key="common_btn_8" defVal="保存"
											fileName="common" />'><emp:message key="common_btn_8" defVal="保存"
											fileName="common" /></a>
			  </span>
			  <span class="hidden_inputs">
			  	<input id="count" name="count" value="0" type="hidden"/>
			  	<input id="plantId" name="plantId" value="" type="hidden"/>
			  	<input id="stype" name="stype" value="<%=otSitInfo.getTypeId()%>" type="hidden"/>
			  	<input id="plantType" name="plantType" value="" type="hidden"/>
			  	<input id="lgcorpcode" name="lgcorpcode" value="<%=lgcorpcode %>" type="hidden"/>
			  	<input type="hidden" id="pathUrl" value="<%=path%>" />
			  </span>
			</h4>
		  </div>
		  <div class="area_content_edit">
		  <div class="nano editContent">
		  <div class="nano-content">
		  <%--预览页面 start--%>
	 		<div id="default_edit" style="display:none;">
		      <div class="tips">
		       <p><emp:message key="wzgl_qywx_site_text_11" defVal="亲，请在左边选择需要编辑的部分，鼠标经过处会有编辑控件显示，点击进入编辑状态。"
											fileName="wzgl" /></p>
		     </div>
		  	</div>
		    <%--预览页面  end--%>
		  </div>
		  </div>
		  </div>
	  </div>
	  <%-- 可编辑区域  end --%>
	  
	</div>
	<%-- 页面结构 end --%>
	<%--发布 --%>
	  <div class="moni" >
		<div class="div_bd bb_none bl_none br_none bd1"></div>
		<div class="div_bd bb_none bl_none br_none bd2"></div>
		<div class="div_bd bb_none bl_none br_none bd3"></div>
	  </div>
	  <div class="wx_wrap oh">
		<div class="tips"><h2><emp:message key="wzgl_qywx_site_text_12" defVal="提示"
											fileName="wzgl" /></h2>
						<emp:message key="wzgl_qywx_site_text_13" defVal="请在切换和关闭页面前先保存页面数据，否则你填写的信息可能丢失！"
											fileName="wzgl" /></div>
		  <div align="center" class="mt29">
		 	 <input type="button" style="background-position: 0px 0px;display:none;" onclick="publishMenu('/ott');" name="" id="subBut1" value="发布" class="btnClass5 ">
		  </div>
	  </div>
	<%--发布 end --%>
	<%-- foot --%>
	<div id="pageStuff" style="display:none;">
		<input type="hidden" id="pathUrl" value="<%=path%>" />
		<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
		<div id="editing_name" title="<emp:message key="wzgl_qywx_site_text_14" defVal="编辑站点名称"
											fileName="wzgl" />">
		  <div class="edit_inner">
		    <div class="box">
		       <p id="editing_name_p1"><emp:message key="wzgl_qywx_site_text_16" defVal="名称不多于6个汉字或12个字母:"
											fileName="wzgl" /></p>
		       <input name= "objname" class="input_inp div_bd" type="text" maxlength="12" value="">
		    </div>
		  </div>
		</div>
	</div>
	<div id="divBox" style="display:none" title='<emp:message key="common_text_6" defVal="预览"
											fileName="common" />'>
		<div style="width:240px;height:460px;margin-left:30px; margin-top:-3px; background:url(<%=commonPath%>/common/img/iphone5.jpg);">
			<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="msgPreviewFrame" src=""></iframe>	
		</div>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wzgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<%-- foot结束 --%>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath%>/wxcommon/widget/nanoscroller/jquery.nanoscroller.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/artDialog.js?skin=default"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/jquery.form.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/colorpicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<%--<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/jquery.flexslider-min.js"></script> --%>
	<script type="text/javascript" src="<%=iPath%>/js/jquery.zclip.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/normal/js/normal.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/addSite.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	
	<script type="text/javascript" language="javascript" src="<%=commonPath%>/wxcommon/skitter/js/jquery.easing.myjquery-p.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" language="javascript" src="<%=commonPath%>/wxcommon/skitter/js/jquery.skitter.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
	<link href="<%=commonPath %>/wxcommon/css/artDialogCss.css" rel="stylesheet" type="text/css" />
   	<link href="<%=commonPath %>/wxcommon/<%=wxskin %>/artDialogCss.css" rel="stylesheet" type="text/css" />
   	 <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link href="<%=path %>/wxcommon/css/artDialogCss_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%}%>
	<script type="text/javascript">
		 //返回上一级
		 var isedit = "<%=request.getAttribute("edit")%>";
		 function doreturn()
		  {
			  if('true'==isedit)
			  {
	  			var url = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=find&isOperateReturn=true&lguserid="
	  	   		 +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
			  }
			  else
			  {
				  var url = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=find&lguserid="
		  	   		 +$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val();
			  }
		  	window.location.href = url;
		  }
		$(document).ready(function(){
		    setTimeout(function(){
		        $(".nano.menuContent").nanoScroller({
		            alwaysVisible: true
		        });
		        setTimeout(function(){
		            if($(".nano.menuContent").find(".nano-pane").is(":visible"))
		            {
		                $(".nano.menuContent").find(".nano-content").css("right","-10px");
		            }else
		            {
		                $(".nano.menuContent").find(".nano-content").css("right","-15px");
		            }
		        },300);
		    },300);
		});

	</script>
</body>

</html>