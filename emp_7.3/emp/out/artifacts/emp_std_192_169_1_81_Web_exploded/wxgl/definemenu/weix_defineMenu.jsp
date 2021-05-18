<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
   // System.out.println(commonPath);
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String rTitle = (String) request.getAttribute("rTitle");
    String menuCode = titleMap.get(rTitle);

    @SuppressWarnings("unchecked")
    List<LfWeiAccount> acctList = (List<LfWeiAccount>) request.getAttribute("acctList");//微信公众帐号集合 
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String wxskin = skin.indexOf("/frame") == -1 ? "default" : skin.substring(skin.indexOf("/frame")+1, skin.length());
    
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=iPath%>/css/defined_menu.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" href="<%=iPath%>/css/definemenu_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
<%--			<jsp:include page="/wxcommon/dqwz.jsp">--%>
<%--				<jsp:param value="<%=menuCode %>" name="menuCode" />--%>
<%--			</jsp:include>--%>
			<div id="logininfos" style="display:none"></div>
			<div id="hiddenValueDiv" style="display:none"></div>
		<div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>	
			<div class="wx_wrap">
	
	<div class="tips"><h2><emp:message key="wxgl_button_12" defVal="编辑" fileName="wxgl"/></h2><emp:message key="wxgl_gzhgl_title_91" defVal="你可以创建最多三个一级菜单，每个一级菜单下可以创建最多5个二级菜单，编辑中的菜单不会马上被用户看到，可放心调试。" fileName="wxgl"/></div>
	<form action="">
	  <table class="table_menu">
	    <tr>
	    	<td align="left" vlign="middle" width="80px;"><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/></td>
	    	
	    	<td  vlign="middle" colspan="4">
	    	<select name="aid" id="aid">
			 <%
			     if(acctList != null && acctList.size() > 0)
			     {
			         for (LfWeiAccount acct : acctList)
			         {
			 %>
			 		<option value="<%=acct.getAId()%>" data-id="<%=acct.getAppId()%>"><%=acct.getName()%></option>
			 <%
			     	}
			     }
			 %>
			</select>
			</td>
		</tr>
	  </table>
	</form>
	</div>
	<div id="menu_wrap" class="oh">
	  <%--左侧自定义菜单start--%>
	  <div id="defined_menu" class="left div_bd bb_none">
	    <div class="area_hd title_bg">
		    <h4 class="area_t oh ">
			  <span class="area_text"><emp:message key="wxgl_gzhgl_title_92" defVal="菜单管理" fileName="wxgl"/></span>
			  <span class="area_t_opr buttons">
			     <a id="add_nav_btn" class="addNoti" href="javascript:;"  ><emp:message key="wxgl_gzhgl_title_93" defVal="添加菜单" fileName="wxgl"/></a>
			  </span>
			</h4>
		</div>
	    <%--菜单模块start--%>
	   <div class="area_bd">
	     <div id="bizmenu">
	      
	  	</div>
	 	<div id="pos_load"><div id="loading_1"><img src="<%=iPath%>/img/loading.gif"></div></div>  
	   </div>
		<%--菜单模块end--%>
	   </div>
        <%--左侧自定义菜单end--%>
		<%--右侧展示区start--%>
		<div class="right div_bd bb_none" id="show_content">
		  <div class="area_hd title_bg">
		    <h4 class="area_t oh ">
			  <span class="area_text"><emp:message key="wxgl_gzhgl_title_94" defVal="设置动作" fileName="wxgl"/></span>
			  <span class="area_t_opr">
			   <a id="backgo" href="javascript:;" style="display: none"><emp:message key="wxgl_button_8" defVal="返回" fileName="wxgl"/></a> 
			  </span>
			</h4>
		  </div>
		  <div class="area_content">
		  <%--default--%>
		    <div id="default_show" style="display:none;">
		      <div class="tips">
		       <p><emp:message key="wxgl_gzhgl_title_95" defVal="请选择订阅者点击菜单后，公众帐号做出的相应动作" fileName="wxgl"/></p>
		     </div>
		     <div class="gl_info oh" align="center">
		         <ul class="mode_list">
		           <li class="mode_item">
		             <a href="javascript:;" class="icon_mode pic_article pngFix" id="article_setting"></a>
		             <p class="p_text"><emp:message key="wxgl_gzhgl_title_96" defVal="关联已有图文" fileName="wxgl"/></p>
		           </li>
		           <li class="mode_item">
		             <a href="javascript:;" class="icon_mode url_setting pngFix" id="url_setting"></a>
		             <p class="p_text"><emp:message key="wxgl_gzhgl_title_97" defVal="设置URL" fileName="wxgl"/></p>
		           </li>
		           <li class="mode_item">
		             <a href="javascript:;" class="icon_mode zxkfModel_setting pngFix" id="zxkfModel_setting"></a>
		             <p class="p_text"><emp:message key="wxgl_gzhgl_title_98" defVal="在线客服" fileName="wxgl"/></p>
		           </li>
		           <li class="mode_item">
		             <a href="javascript:;" class="icon_mode lbsModel_setting pngFix" id="lbsModel_setting"></a>
		             <p class="p_text"><emp:message key="wxgl_gzhgl_title_99" defVal="LBS网点查询" fileName="wxgl"/></p>
		           </li>
		           <li class="mode_item">
		             <a href="javascript:;" class="icon_mode wzglModel_setting pngFix" id="wzglModel_setting"></a>
		             <p class="p_text"><emp:message key="wxgl_gzhgl_title_100" defVal="企业微站" fileName="wxgl"/></p>
		           </li>
		            <li class="mode_item" style="display: none">
		             <a href="javascript:;" class="icon_mode choujiangModel_setting pngFix" id="choujiangModel_setting"></a>
		             <p class="p_text"><emp:message key="wxgl_gzhgl_title_101" defVal="抽奖活动" fileName="wxgl"/></p>
		           </li>
		           <li class="mode_item" style="display: none">
		             <a href="javascript:;" class="icon_mode formModel_setting pngFix" id="formModel_setting"></a>
		             <p class="p_text"><emp:message key="wxgl_gzhgl_title_102" defVal="表单" fileName="wxgl"/></p>
		           </li>
		         </ul>
		     </div>
		    </div>
		  <%-- tip start --%>
		  <div class="nav_tip_box" style="display:none;"><emp:message key="wxgl_gzhgl_title_103" defVal="已有子菜单，无法设置动作" fileName="wxgl"/></div>
		 <%-- tip end --%>
		 <div class="default_content"><emp:message key="wxgl_gzhgl_title_104" defVal="选择好公众帐号之后才能添加设置菜单 " fileName="wxgl"/> </div>
		  <%-- /default --%>
		   <%--链接跳转 --%>
		   <div id="link_transform" style="display:none;">
		    <form method="">
		     <div class="tips">
		       <p><emp:message key="wxgl_gzhgl_title_105" defVal="订阅者点击该子菜单会跳转到以下链接" fileName="wxgl"/></p>
		     </div>
		     <div class="link_input">
		       <input type="text" id="url" name="url" value="" class="div_bd link_inp input_inp"/>
		     </div>
		    <div class="keep_submit" align="center">
		      <input type="button" class="btnClass5" value="<emp:message key='wxgl_button_6' defVal='保存' fileName='wxgl'/>" id="update_action_url" name="update_action_url" >
		    </div>
		  </form>
		  </div>
		  <%--/链接跳转 --%>
		  <%-- 在线客服 --%>
		   <div id="zxkf_transform" style="display:none;">
		    <form method="">
		     <div class="tips">
		       <p><emp:message key="wxgl_gzhgl_title_106" defVal="订阅者点击该子菜单会激活人工客服聊天" fileName="wxgl"/></p>
		     </div>
		     <div class="link_input">
		       <input type="checkbox" id="zxkfModule" value="1" class="div_bd link_inp input_inp" style="width:80px"/>
		                      <span><emp:message key="wxgl_gzhgl_title_107" defVal="人工客服" fileName="wxgl"/></span> 
		     </div>
		    <div class="keep_submit" align="center">
		      <input type="hidden" name="modelName" value="zxkfModule"/>
		      <input type="button" class="btnClass5" value="<emp:message key='wxgl_button_6' defVal='保存' fileName='wxgl'/>" id="update_action_zxkfModule" name="update_action_zxkfModule" >
		    </div>
		  	</form>
		  </div>
		  <%-- /在线客服 --%>
		  <%-- lbs查询 --%>
		   <div id="lbs_transform" style="display:none;">
		    <form method="">
		     <div class="tips">
		       <p><emp:message key="wxgl_gzhgl_title_108" defVal="订阅者点击该子菜单会激活LBS采集点查询" fileName="wxgl"/></p>
		     </div>
		     <div class="link_input">
		       <input type="checkbox" id="lbsModule" value="1" class="div_bd link_inp input_inp" style="width:80px"/>
		                      <span><emp:message key="wxgl_gzhgl_title_109" defVal="LBS采集点查询" fileName="wxgl"/></span> 
		     </div>
		    <div class="keep_submit" align="center">
		      <input type="hidden" name="modelName" value="lbsModule"/>
		      <input type="button" class="btnClass5" value="<emp:message key='wxgl_button_6' defVal='保存' fileName='wxgl'/>" id="update_action_lbsModule" name="update_action_lbsModule" >
		    </div>
		  	</form>
		  </div>
		  <%-- /lbs查询  --%>
		  <%-- 关联已有图文  --%>
		  <div id="picDiv" style="display:none" >
		  	<div id="temp_transform" >
		  		<input type="button" value="<emp:message key='wxgl_gzhgl_title_110' defVal='选择图文' fileName='wxgl'/>" class="btnClass3 btnHover mb10" onclick="chooseTemp()">
		  		<input type="hidden" id="tid" value=""/>
		  		<input type="hidden" id="selTid" value=""/>
		  		<div id="selDiv"></div>
		  		<div class="keep_submit" align="center">
				      <input type="button" class="btnClass5" value="<emp:message key='wxgl_button_6' defVal='保存' fileName='wxgl'/>" name="update_action_Temp" id="update_action_temp" >
				</div>
		  		<div style="display:none;" id="tempDiv" title="<emp:message key='wxgl_gzhgl_title_110' defVal='选择图文' fileName='wxgl'/>">
		    	<iframe id="wxtempFrame" name="wxtempFrame" style="width:720px;height:450px;border:0;overflow:hidden;margin:0;" scrolling="no" 
		    	frameborder="no" src=""></iframe>
		    	</div>
		  	</div>
		  </div>
		  <%-- /关联已有图文  --%>
		  <%-- 抽奖活动  --%>
		  <div id="choujiangDiv" style="display:none" >
		  	<div id="chouJiang_transform" >
		  		<input type="button" value="<emp:message key='wxgl_gzhgl_title_111' defVal='选择抽奖' fileName='wxgl'/>" class="btnClass3 btnHover mb10" onclick="chooseChoujiang()">
		  		<input type="hidden" id="choujiangid" value=""/>
		  		<input type="hidden" id="choujiangSelTid" value=""/>
		  		<div id="choujiangSelDiv"></div>
		  		 <div class="keep_submit" align="center">
				      <input type="button" class="btnClass5" value="<emp:message key='wxgl_button_6' defVal='保存' fileName='wxgl'/>" id="update_action_chouJiang">
				 </div>
		    </div>
		  </div>
		  <%-- /抽奖活动   --%>
		  <%-- 微站  --%>
		  <div id="wzglDiv" style="display:none" >
		  	<div id="wzgl_transform" >
		  		<input type="button" value="<emp:message key='wxgl_gzhgl_title_112' defVal='选择微站' fileName='wxgl'/>" class="btnClass3 btnHover mb10" onclick="chooseSite()">
		  		<input type="hidden" id="siteid" value=""/>
		  		<input type="hidden" id="siteSelTid" value=""/>
		  		<div id="siteSelDiv"></div>
		  		 <div class="keep_submit" align="center">
				      <input type="button" class="btnClass5" value="<emp:message key='wxgl_button_6' defVal='保存' fileName='wxgl'/>" id="update_action_wzgl">
				 </div>
		    </div>
		  </div>
		  <%-- /微站   --%>
		  <%-- 表单  --%>
		  <div id="formDiv" style="display:none" >
		  	<div id="form_transform" >
		  		<input type="button" value="<emp:message key='wxgl_gzhgl_title_113' defVal='选择表单' fileName='wxgl'/>" class="btnClass3 btnHover mb10" onclick="chooseForm()">
		  		<input type="hidden" id="formid" value=""/>
		  		<input type="hidden" id="formSelTid" value=""/>
		  		<div id="formSelDiv"></div>
		  		 <div class="keep_submit" align="center">
				      <input type="button" class="btnClass5" value="<emp:message key='wxgl_button_6' defVal='保存' fileName='wxgl'/>" id="update_action_form">
				 </div>
		    </div>
		  </div>
		  <%-- /表单   --%>
		  </div>
		</div>
		</div>
		<%--右侧展示区end--%>
	<div class="moni">
	<div class="div_bd bb_none bl_none br_none left moni_left"></div>
	<div class="div_bd bb_none bl_none br_none right moni_right"></div>
	</div>
		<div class="wx_wrap oh">
	 
	<div class="tips"><h2><emp:message key="wxgl_gzhgl_title_114" defVal="发布" fileName="wxgl"/></h2><emp:message key="wxgl_gzhgl_title_115" defVal="编辑中的菜单不能在手机中生效，你需要进行发布，发布中的24小时内所有的用户都将更新到新的菜单。" fileName="wxgl"/></div>
	
	  <div align="center" class="mt29">
	 
	  <input type="button" class="btnClass5 " value="<emp:message key='wxgl_gzhgl_title_114' defVal='发布' fileName='wxgl'/>" id="subBut1" name="" onclick="publishMenu('<%=path%>');" style="background-position: 0px 0px;">
	  
	  </div>
	</div>
	</div>
	
	<div id="addMenu" title="<emp:message key='wxgl_gzhgl_title_93' defVal='添加菜单' fileName='wxgl'/>" style="display:none;">
	  <div class="addmenu_inner" style="padding:20px 15px;height:60px;color:#666;">
	    <div class="addmenu_box" style="padding-left:28px;margin-bottom:29px;">
	      <p style="word-wrap:break-word"><emp:message key="wxgl_gzhgl_title_116" defVal="菜单名称名字不多于4个汉字或8个字母:" fileName="wxgl"/></p>
	       <input class="input_inp bizmenu_name inp01" id="l1menuname" maxLength="8" type="text" value="">
	    </div>
	    <div align="center">
	      <input type="button" class="btnClass5 mr23" value="<emp:message key='wxgl_button_3' defVal='确定' fileName='wxgl'/>" id="subButL1"
	       style="background-position: 0px 0px;">
	      <input type="button" class="btnClass6 " value="<emp:message key='wxgl_button_4' defVal='取消' fileName='wxgl'/>" name="" onclick="javascript:closeDialog();">
	  </div>
	  </div>
	</div>
	
	<div id="add_sub_Menu" title="<emp:message key='wxgl_gzhgl_title_117' defVal='添加子菜单' fileName='wxgl'/>" style="display:none;">
	  <div class="addmenu_inner">
	    <div class="addmenu_box">
	      <p><emp:message key="wxgl_gzhgl_title_118" defVal="菜单名称名字不多于8个汉字或16个字母:" fileName="wxgl"/></p>
	       <input type="hidden" id="level2pid" value="">
	       <input class="input_inp bizmenu_name div_bd"  id="level2name" type="text" value="" 	maxLength="16">
	    </div>
	    <div align="center">
	      <input type="button" class="btnClass5 mr23" value="<emp:message key='wxgl_button_3' defVal='确定' fileName='wxgl'/>" id="subButL2" name=""
	       style="background-position: 0px 0px;">
	      <input type="button" class="btnClass6 " value="<emp:message key='wxgl_button_4' defVal='取消' fileName='wxgl'/>" name="" onclick="javascript:closeDialog();">
	  </div>
	  </div>
	</div>
	
	<div id="edit_sub_Menu" title="<emp:message key='wxgl_gzhgl_title_119' defVal='菜单重命名' fileName='wxgl'/>" style="display:none;">
	  <div class="addmenu_inner">
	    <div class="addmenu_box">
	    	<p id="p1" class="hidden"><emp:message key="wxgl_gzhgl_title_116" defVal="菜单名称名字不多于4个汉字或8个字母:" fileName="wxgl"/></p>
	      <p id="p2"><emp:message key="wxgl_gzhgl_title_118" defVal="菜单名称名字不多于8个汉字或16个字母:" fileName="wxgl"/></p>
	       <input id="updateMid" type="hidden" value="">
	       <input class="input_inp bizmenu_name div_bd" id="updateMname" type="text" maxLength="16" value="">
	    </div>
	    <div align="center">
	      <input type="button" class="btnClass5 mr23" value="<emp:message key='wxgl_button_3' defVal='确定' fileName='wxgl'/>" id="updateBtn" name=""
	       maxLength="8" 
	       style="background-position: 0px 0px;">
	      <input type="button" class="btnClass6 " value="<emp:message key='wxgl_button_4' defVal='取消' fileName='wxgl'/>" name="" onclick="javascript:closeDialog();">
	  </div>
	  </div>
	</div>
	<%-- 微站预览 --%>
	<div id="divPreBox" style="display:none" title="<emp:message key='wxgl_button_11' defVal='预览' fileName='wxgl'/>">
		<div style="width:240px;height:460px;margin-top:-3px; background:url(<%=path%>/common/img/iphone5.png);">
			<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="msgPreviewFrame" src=""></iframe>	
		</div>
	</div> 
	<%-- 微站预览 /--%>
	<%--图文预览  --%>
	<div id="divBox2" style="display:none" title="<emp:message key='wxgl_button_11' defVal='预览' fileName='wxgl'/>">
		<div style="width:240px;height:460px;margin-top:-3px; background:url(<%=path%>/common/img/iphone5.png);">
			<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="msgPreviewFrame2" 
			src=""></iframe>	
		</div>
	</div>
	<%-- 图文预览  /--%>
	<%-- 表单预览 --%>
	<div id="divPreBox3" style="display:none" title="<emp:message key='wxgl_button_11' defVal='预览' fileName='wxgl'/>">
		<div style="width:240px;height:460px;margin-top:-3px; background:url(<%=path%>/common/img/iphone5.png);">
			<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="formPreviewFrame3" src=""></iframe>	
		</div>
	</div> 
	<%-- 表单预览 /--%>
	<div id="level1menu" style="display:none;">
	 <%--一级菜单start--%>
		  <div class="nav_item level1" pid="" mid="" tid="" murl="" mtype="">
		     <div class="nav_bd">
			   <span class="nav_icon">
			      <i class="opr_icon18 switch_icon"></i>
			   </span>
			   <ul class="nav_opr_list">
			       <li class="nav_opr_item">
				      <a href="javascript:;" class="nav_opr_btn nav_opr_add_btn" title="<emp:message key='wxgl_gzhgl_title_117' defVal='添加子菜单' fileName='wxgl'/>">
					       <i class="opr_icon16 add_icon"></i>
					  </a></li>           
				   <li class="nav_opr_item">
				      <a href="javascript:;" class="nav_opr_btn nav_opr_edit_btn" title="<emp:message key='wxgl_gzhgl_title_120' defVal='改名' fileName='wxgl'/>">
					       <i class="opr_icon16 edit_icon"></i>
					   </a>
				   </li>           
				   <li class="nav_opr_item">
				       <a href="javascript:;" class="nav_opr_btn nav_opr_edit_btn" menus="0" leavel="0" sortid="1" title="<emp:message key='wxgl_button_10' defVal='删除' fileName='wxgl'/>">
					       <i class="opr_icon16 del_icon"></i>
					   </a>
				   </li>           
				   <li class="nav_opr_item">
				       <a href="javascript:;" class="nav_opr_btn nav_opr_sort_btn" title="<emp:message key='wxgl_gzhgl_title_121' defVal='上移' fileName='wxgl'/>">
					       <i class="opr_icon16 arrow_up"></i>
					   </a>
				   </li> 
				   <li class="nav_opr_item">
				       <a href="javascript:;" class="nav_opr_btn nav_opr_sort_btn" title="<emp:message key='wxgl_gzhgl_title_122' defVal='下移' fileName='wxgl'/>">
					       <i class="opr_icon16 arrow_down"></i>
					   </a>
				   </li>         
				</ul>
				<h4 class="nav_t">
				   <a href="javascript:;" title="" class="level1name nav_btn"></a>
				</h4>
			 </div>
		  </div>
		  <%--一级菜单end--%>
	</div>
	<div id="level2menu" style="display:none;">
	 <%--二级子菜单start--%>
	   <div id="level0_2_1" class="nav_item_wrapper l2son nav_item_level2_wrapper" style="">
			      <div class="nav_item level2" pid="" mid=""  tid="" murl="" mtype="">       
		     <div class="nav_bd">         
			     <span class="nav_icon">
				     <i class="add_on_icon10 circle"></i>
				 </span>         
				 <ul class="nav_opr_list">           
				    <li class="nav_opr_item">
					   <a href="javascript:;" class="nav_opr_btn nav_opr_edit_btn" title="<emp:message key='wxgl_gzhgl_title_120' defVal='改名' fileName='wxgl'/>">
					      <i class="opr_icon16 edit_icon"></i>
					   </a>
					</li>           
					<li class="nav_opr_item"><a href="javascript:;" class="nav_opr_btn nav_opr_edit_btn" menus="0" leavel="2" sortid="1" title="<emp:message key='wxgl_button_10' defVal='删除' fileName='wxgl'/>">
					    <i class="opr_icon16 del_icon"></i></a>
					</li>           
					<li class="nav_opr_item">
		       <a href="javascript:;" class="nav_opr_btn nav_opr_sort_btn" title="<emp:message key='wxgl_gzhgl_title_121' defVal='上移' fileName='wxgl'/>">
			       <i class="opr_icon16 arrow_up"></i>
			   </a>
		   </li> 
		   <li class="nav_opr_item">
		       <a href="javascript:;" class="nav_opr_btn nav_opr_sort_btn" title="<emp:message key='wxgl_gzhgl_title_122' defVal='下移' fileName='wxgl'/>">
			       <i class="opr_icon16 arrow_down"></i>
			   </a>
		   </li>             
				</ul>         
				<h5 class="nav_t">
				    <a href="javascript:;" title="" data-domid="1" class="level2name nav_btn"></a>
				</h5>       
			</div>            
		  </div>   
	    </div>
  <%--二级子菜单end--%>
	</div>
	<div id="pageStuff" style="display:none;">
				<input type="hidden" id="pathUrl" value="<%=path %>" />
				<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
	</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	    <script type="text/javascript" src="<%=commonPath%>/wxcommon/widget/jqueryui/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/wxcommon/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/settingMenu.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/menuz.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<link href="<%=commonPath %>/wxcommon/css/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    	<link href="<%=commonPath %>/wxcommon/<%=wxskin %>/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    	 <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link href="<%=path %>/wxcommon/css/artDialogCss_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%}%>
	</body>
</html>
