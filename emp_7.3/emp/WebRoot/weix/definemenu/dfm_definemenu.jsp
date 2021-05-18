<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.weix.LfWcAccount"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	
	@ SuppressWarnings("unchecked")
	List<LfWcAccount> acctList = (List<LfWcAccount>)request.getAttribute("acctList");//微信公众帐号集合 
	
	String skin = session.getAttribute("stlyeSkin")==null?"frame/frame3.0/skin/default":(String)session.getAttribute("stlyeSkin");
%>
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.dialog.new.css" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css" type="text/css" >
		<link rel="stylesheet" href="<%=commonPath %>/common/css/table.css" type="text/css" >
		<link rel="stylesheet" href="<%=skin %>/table.css" type="text/css" >
		<link rel="stylesheet" href="<%=iPath %>/css/defined_menu.css" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css" />
	</head>
	<body>
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<div id="logininfos" style="display:none"></div>
			<div id="hiddenValueDiv" style="display:none"></div>
			<div class="wx_wrap">
	
	<div class="tips"><h2><emp:message key="common_text_7" defVal="编辑"
											fileName="common"></emp:message></h2>
				<emp:message key="wexi_qywx_zdycd_text_1" defVal="你可以创建最多三个一级菜单，每个一级菜单下可以创建最多5个二级菜单，编辑中的菜单不会马上被用户看到，可放心调试。"
											fileName="weix"></emp:message></div>
	<form action="">
	  <table class="table_menu">
	    <tr>
	    	<td align="right" vlign="middle"><emp:message key="wexi_qywx_zdycd_text_2" defVal="公众帐号："
											fileName="weix"></emp:message></td>
	    	
	    	<td  vlign="middle" colspan="4"><select name="" id="aid">
			 <option value=""><emp:message key="wexi_qywx_zdycd_text_3" defVal="请选择"
											fileName="weix"></emp:message></option>
			 <%if(acctList != null && acctList.size()>0)
			 { 
			 	for(LfWcAccount acct : acctList)
			 	{
			 %>
			 <option value="<%=acct.getAId() %>"><%=acct.getName() %></option>
			 <%} }%>
			</select></td>
		</tr>
		<tr>
			<td align="right"  vlign="middle">Appid：</td>
			<td  vlign="middle">
			<input maxlength="64" type="text" name="appid" id="appid" value="" class="div_bd input_bd" onkeyup="secapp(this,/[\u4E00-\u9FA5]/g)" onpaste="value=value.replace(/[\u4E00-\u9FA5]/g,'')" oncontextmenu = "value=value.replace(/[\u4E00-\u9FA5]/g,'')"/>&nbsp;&nbsp;
			</td>
			<td  vlign="middle">
			AppSecret：</td>
			<td>
			<input maxlength="64" type="text" name="secret" id="secret" value="" class="div_bd input_bd" onkeyup="secapp(this,/[\u4E00-\u9FA5]/g)" onpaste="value=value.replace(/[\u4E00-\u9FA5]/g,'')" oncontextmenu = "value=value.replace(/[\u4E00-\u9FA5]/g,'')"/>
			</td>
			<td align="right" style="padding-left:0;padding-right:0;">
			<input type="button" class="btnClass5" value="<emp:message key="common_btn_4" defVal="保存"
											fileName="common"></emp:message>" id="sub_update_app" name="" style="display:block"></td>
	    </tr>
	  </table>
	</form>
	</div>
	<div id="menu_wrap" class="oh">
	  <%--左侧自定义菜单start--%>
	  <div id="defined_menu" class="left div_bd bb_none">
	    <div class="area_hd title_bg">
		    <h4 class="area_t oh ">
			  <span class="area_text"><emp:message key="wexi_qywx_zdycd_text_4" defVal="菜单管理"
											fileName="weix"></emp:message></span>
			  <span class="area_t_opr buttons">
			     <a id="add_nav_btn" class="addNoti" href="javascript:;"><emp:message key="wexi_qywx_zdycd_btn_1" defVal="添加菜单"
											fileName="weix"></emp:message></a>
				 
			  </span>
			</h4>
		</div>
	    <%--菜单模块start--%>
		   <div class="area_bd">
		      <div id="bizmenu">
		   </div>
		 <div id="pos_load"><div id="loading"><img src="<%=iPath %>/img/loading.gif"></div></div>  
	   </div>
		<%--菜单模块end--%>
	   </div>
        <%--左侧自定义菜单end--%>
		<%--右侧展示区start--%>
		<div class="right div_bd bb_none" id="show_content">
		  <div class="area_hd title_bg">
		    <h4 class="area_t oh ">
			  <span class="area_text"><emp:message key="wexi_qywx_zdycd_text_5" defVal="设置动作"
											fileName="weix"></emp:message></span>
			  <span class="area_t_opr">
			   <a id="backgo" href="javascript:;" style="display: none"><emp:message key="common_btn_10" defVal="返回"
											fileName="common"></emp:message></a> 
			  </span>
			</h4>
		  </div>
		  <div class="area_content">
		  <%--default--%>
		    <div id="default_show" style="display:none;">
		      <div class="tips">
		       <p><emp:message key="wexi_qywx_zdycd_text_6" defVal="请选择订阅者点击菜单后，公众帐号做出的相应动作"
											fileName="weix"></emp:message></p>
		     </div>
		     <div class="gl_info oh" align="center">
		         <ul class="mode_list">
		           <li class="mode_item">
		             <a href="javascript:;" class="icon_mode pic_article pngFix" id="pic_article"></a>
		             <p class="p_text"><emp:message key="wexi_qywx_zdycd_text_7" defVal="关联已有图文"
											fileName="weix"></emp:message></p>
		           </li>
		           <li class="mode_item">
		             <a href="javascript:;" class="icon_mode url_setting pngFix" id="url_setting"></a>
		             <p class="p_text"><emp:message key="wexi_qywx_zdycd_text_8" defVal="设置URL"
											fileName="weix"></emp:message></p>
		           </li>
		         </ul>
		     </div>
		    </div>
		  <%-- tip start --%>
		  <div class="nav_tip_box" style="display:none;"><emp:message key="wexi_qywx_zdycd_text_9" defVal="已有子菜单，无法设置动作"
											fileName="weix"></emp:message></div>
		 <%-- tip end --%>
		 <div class="default_content"><emp:message key="wexi_qywx_zdycd_text_10" defVal="选择好公众帐号之后才能添加设置菜单 "
											fileName="weix"></emp:message> </div>
		  <%-- /default --%>
		   <%--链接跳转 --%>
		   <div id="link_transform" style="display:none;">
		    <form method="">
		     <div class="tips">
		       <p><emp:message key="wexi_qywx_zdycd_text_11" defVal="订阅者点击该子菜单会跳转到以下链接"
											fileName="weix"></emp:message></p>
		     </div>
		     <div class="link_input">
		       <input type="text" id="url" name="url" value="" class="div_bd link_inp input_inp"/>
		     </div>
		    <div class="keep_submit" align="center">
		      <input type="button" class="btnClass5" value="<emp:message key="common_btn_4" defVal="保存"
											fileName="common"></emp:message>" id="update_action" name="" >
		    </div>
		  </form>
		  </div>
		  <%--/链接跳转 --%>
		  <div id="picDiv" style="display:none" >
		  	<div id="chooseTemp" >
		  		<input type="button" value="<emp:message key="wexi_qywx_zdycd_text_12" defVal="选择图文"
											fileName="weix"></emp:message>" class="btnClass3 btnHover mb10" onclick="chooseTemp()">
		  		<input type="hidden" id="tid" value=""/>
		  		<div id="selDiv"></div>
		  		 <div class="keep_submit" align="center">
				      <input type="button" class="btnClass5" value="<emp:message key="common_btn_4" defVal="保存"
											fileName="common"></emp:message>" onclick="savePic()" id="savePicBtn" >
				    </div>
		  		<div style="display:none;" id="tempDiv" title="<emp:message key="wexi_qywx_zdycd_text_12" defVal="选择图文"
											fileName="weix"></emp:message>">
		    	<iframe id="tempFrame" name="tempFrame" style="width:100%;height:450px;border:0;overflow:hidden;margin:0;" scrolling="no" 
		    	frameborder="no" src=""></iframe>
					<div style="text-align: center">
						<input class="btnClass5 mr23" onclick="tempSure()" value="<emp:message key="common_btn_4" defVal="选择"
											fileName="common"></emp:message>" type="button">
						<input class="btnClass6" onclick="tempCancel()" value="<emp:message key="common_btn_16" defVal="取消"
											fileName="common"></emp:message>" type="button">
					</div>
		    </div>
		  	</div>
		  </div>
		</div>
		  </div>
		<%--右侧展示区end--%>
	</div>
	<div class="moni">
	<div class="div_bd bb_none bl_none br_none left moni_left"></div>
	<div class="div_bd bb_none bl_none br_none right moni_right"></div>
	</div>
		<div class="wx_wrap oh">
	 
	<div class="tips"><h2><emp:message key="wexi_qywx_zdycd_text_13" defVal="发布："
											fileName="weix"></emp:message></h2>
			<emp:message key="wexi_qywx_zdycd_text_14" defVal="编辑中的菜单不能在手机中生效，你需要进行发布，发布中的24小时内所有的用户都将更新到新的菜单。"
											fileName="weix"></emp:message></div>
	
	  <div align="center" class="mt29">
	 <%if(btnMap.get(menuCode+"-1")!=null) {%>
	  <input type="button" class="btnClass5 " value="<emp:message key="wexi_qywx_zdycd_text_13" defVal="发布："
											fileName="weix"></emp:message>" id="subBut1" name="" onclick="publishMenu('<%=path %>');" style="background-position: 0px 0px;">
	  <%} %>
	  </div>
	</div>
	
	
	</div>
	
	<div id="addMenu" title="<emp:message key="wexi_qywx_zdycd_btn_1" defVal="添加菜单"
											fileName="weix"></emp:message>" style="display:none;">
	  <div class="addmenu_inner">
	    <div class="addmenu_box">
	      <p><emp:message key="wexi_qywx_zdycd_text_17" defVal="菜单名称名字不多于4个汉字或8个字母:"
											fileName="weix"></emp:message></p>
	       <input class="input_inp bizmenu_name div_bd" id="l1menuname" 	maxLength="8" type="text" value="">
	    </div>
	    <div align="center">
	      <input type="button" class="btnClass5 mr23" value="<emp:message key="common_btn_7" defVal="确定"
											fileName="common"></emp:message>" id="subButL1" onclick="addNewTab()" 
	       style="background-position: 0px 0px;">
	      <input type="button" class="btnClass6 " value="<emp:message key="common_btn_16" defVal="取消"
											fileName="common"></emp:message>" name="" onclick="javascript:btcancel();">
	  </div>
	  </div>
	</div>
	
	<div id="add_sub_Menu" title="<emp:message key="wexi_qywx_zdycd_text_18" defVal="添加子菜单"
											fileName="weix"></emp:message>" style="display:none;">
	  <div class="addmenu_inner">
	    <div class="addmenu_box">
	      <p><emp:message key="wexi_qywx_zdycd_text_15" defVal="菜单名称名字不多于8个汉字或16个字母:"
											fileName="weix"></emp:message></p>
	       <input type="hidden" id="level2pid" value="">
	       <input class="input_inp bizmenu_name div_bd"  id="level2name" type="text" value="" 	maxLength="16">
	    </div>
	    <div align="center">
	      <input type="button" class="btnClass5 mr23" value="<emp:message key="common_btn_7" defVal="确定"
											fileName="common"></emp:message>" id="subButL2" name="" onclick="addL2Menu()"
	       style="background-position: 0px 0px;">
	      <input type="button" class="btnClass6 " value="<emp:message key="common_btn_16" defVal="取消"
											fileName="common"></emp:message>" name="" onclick="javascript:btcancel();">
	  </div>
	  </div>
	</div>
	
	<div id="edit_sub_Menu" title="<emp:message key="wexi_qywx_zdycd_text_16" defVal="菜单重命名"
											fileName="weix"></emp:message>" style="display:none;">
	  <div class="addmenu_inner">
	    <div class="addmenu_box">
	    	<p id="p1" class="hidden"><emp:message key="wexi_qywx_zdycd_text_17" defVal="菜单名称名字不多于4个汉字或8个字母:"
											fileName="weix"></emp:message></p>
	      <p id="p2"><emp:message key="wexi_qywx_zdycd_text_15" defVal="菜单名称名字不多于8个汉字或16个字母:"
											fileName="weix"></emp:message></p>
	       <input id="updateMid" type="hidden" value="">
	       <input class="input_inp bizmenu_name div_bd" id="updateMname" type="text" maxLength="16" value="">
	    </div>
	    <div align="center">
	      <input type="button" class="btnClass5 mr23" value="<emp:message key="common_btn_7" defVal="确定"
											fileName="common"></emp:message>" id="updateBtn" name=""
	      	onclick = "updateMenuName()" maxLength="8" 
	       style="background-position: 0px 0px;">
	      <input type="button" class="btnClass6 " value="<emp:message key="common_btn_16" defVal="取消"
											fileName="common"></emp:message>" name="" onclick="javascript:btcancel();">
	  </div>
	  </div>
	</div>
	<div id="divBox2" style="display:none">
			<div style="width:240px;height:460px;margin-left:30px; margin-top:-3px; background:url(<%=path %>/common/img/iphone5.jpg);">
				<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="msgPreviewFrame2" 
				src=""></iframe>	
			</div>
		</div>
	
	<div id="level1menu" style="display:none;">
	 <%--一级菜单start--%>
		  <div class="nav_item level1" pid="" mid="" tid="" murl="" mtype="">
		     <div class="nav_bd">
			   <span class="nav_icon">
			      <i class="opr_icon18 switch_icon"></i>
			   </span>
			   <ul class="nav_opr_list">
			       <li class="nav_opr_item">
				      <a href="javascript:;" class="nav_opr_btn nav_opr_add_btn" title="<emp:message key="wexi_qywx_zdycd_text_18" defVal="添加子菜单"
											fileName="weix"></emp:message>">
					       <i class="opr_icon16 add_icon"></i>
					  </a></li>           
				   <li class="nav_opr_item">
				      <a href="javascript:;" class="nav_opr_btn nav_opr_edit_btn" title="<emp:message key="wexi_qywx_zdycd_text_19" defVal="改名"
											fileName="weix"></emp:message>">
					       <i class="opr_icon16 edit_icon"></i>
					   </a>
				   </li>           
				   <li class="nav_opr_item">
				       <a href="javascript:;" class="nav_opr_btn nav_opr_edit_btn" menus="0" leavel="0" sortid="1" title="<emp:message key="common_text_8" defVal="删除"
											fileName="common"></emp:message>">
					       <i class="opr_icon16 del_icon"></i>
					   </a>
				   </li>           
				   <li class="nav_opr_item">
				       <a href="javascript:;" class="nav_opr_btn nav_opr_sort_btn" title="<emp:message key="wexi_qywx_zdycd_text_20" defVal="上移"
											fileName="weix"></emp:message>">
					       <i class="opr_icon16 arrow_up"></i>
					   </a>
				   </li> 
				   <li class="nav_opr_item">
				       <a href="javascript:;" class="nav_opr_btn nav_opr_sort_btn" title="<emp:message key="wexi_qywx_zdycd_text_21" defVal="下移"
											fileName="weix"></emp:message>">
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
					   <a href="javascript:;" class="nav_opr_btn nav_opr_edit_btn" title="<emp:message key="wexi_qywx_zdycd_text_19" defVal="改名"
											fileName="weix"></emp:message>">
					      <i class="opr_icon16 edit_icon"></i>
					   </a>
					</li>           
					<li class="nav_opr_item"><a href="javascript:;" class="nav_opr_btn nav_opr_edit_btn" menus="0" leavel="2" sortid="1" title="<emp:message key="common_text_8" defVal="删除"
											fileName="common"></emp:message>">
					    <i class="opr_icon16 del_icon"></i></a>
					</li>           
					<li class="nav_opr_item">
		       <a href="javascript:;" class="nav_opr_btn nav_opr_sort_btn" title="<emp:message key="wexi_qywx_zdycd_text_20" defVal="上移"
											fileName="weix"></emp:message>">
			       <i class="opr_icon16 arrow_up"></i>
			   </a>
		   </li> 
		   <li class="nav_opr_item">
		       <a href="javascript:;" class="nav_opr_btn nav_opr_sort_btn" title="<emp:message key="wexi_qywx_zdycd_text_21" defVal="下移"
											fileName="weix"></emp:message>">
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/weix_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-b.js"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js"></script>
		<script type="text/javascript" src="<%=iPath %>/js/settingMenu.js"></script>	
		<script type="text/javascript" src="<%=iPath %>/js/menuz.js"></script>
	</body>
</html>
