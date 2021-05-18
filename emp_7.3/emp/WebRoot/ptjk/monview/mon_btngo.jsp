<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.monitor.constant.MonitorStaticValue"%>
<%
String path = request.getContextPath();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'mon_btngo.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<style>
		*{margin:0;padding:0;}
		.btngo{
			width: 253px;
			height: 64px;
			display: block;
			position: absolute;
			left: 50%;
			top: 50%;
			margin: -32px 0 0 -126px;
			text-decoration: underline;
		}
		div img{border: none;}

		.mon_font
		{
			color:black;
			font-size: 16px;
		}
		

	</style>
  </head>
  
	<body>

		<%--<a href="javascript:void();" class="btngo"><img src="<%=iPath %>/images/btngo.gif" alt=""></a> --%>
		<span class="btngo">
			<img src="<%=commonPath %>/common/img/ico_loading.gif" style="width: 16px;height:16px;" alt="">
		</span>

		<div id="loginInfo" class="hidden"></div>
		<input type="hidden" name="openTimer" id="openTimer" value="1">
	</body>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
<script src="<%=path%>/common/widget/artDialog/artDialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script src="<%=path%>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script>
var timers=null;
var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false)
	$(document).ready(function(){
		getLoginInfo("#loginInfo");
			var time = new Date().getTime();
			warning_dialog();
			//console.log($('.aui_outer',window.parent.parent.document).parent().size());
			$('.aui_close',window.parent.parent.document).click(function(e){
				$(window.parent.parent.document).find("#close_mon_State").val("2");
				dialogClose();
				e.preventDefault();
			})
			$('#openTimer').click(function(){
				dialogClose();
			})
			var ie=!!window.ActiveXObject;
			if(ie){
				$('.fullScreen',window.parent.parent.document).hide();
			}
			$('.fullScreen',window.parent.parent.document).click(function(){
			   if(!ie){
			   	 window.parent.parent.toggleFullScreen();
			   }
			  
			})
			
	})
	//报警信息弹出框
	function warning_dialog(){
			var lguserid = $("#lguserid").val();
			var openTimer=$('#openTimer').val();
			
			var artDialog=art.dialog;
					//$('.aui_content',window.parent.parent.document).html('');
					//$('.aui_outer:eq(0)',window.parent.parent.document).parent().hide();
					//console.log($(window.parent.parent.document).contents().find('.aui_outer').parent().size());
					art.dialog({id:'x1354'}).close();
						var aboutConfig={
							id:'x1354',
							width: '100%',
			    			height: '100%',
					        title: getJsLocaleMessage("ptjk","ptjk_jkst_jkst_19")+'<a class="fullScreen" href="javascript:void(0)">'+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_20")+'</a>',
					        lock: false,
					        background: "#CCCCCC",
					        opacity: 0,
					        drag:false,
					        close:function(){
					        	if(isIE6)
					        	{
									setTimeout("window.parent.parent.openMonDialog()",300);
					        	}
					        	else
					        	{
									window.parent.parent.openMonDialog();
					        	}
					        }
					    };
					    artDialog.open("<%=path%>/mon_mainMon.htm?method=getMonInfo&lguserid="+lguserid, aboutConfig);
				$(window.parent.parent.document).find("#close_mon_State").val("1");
				//timers=setTimeout('warning_dialog()',<%=MonitorStaticValue.getRefreshTime()%>);
			
		//},5000);
		//timers=setTimeout('warning_dialog()',5000);
	}
	function dialogClose(){
		art.dialog({id:'x1354'}).close();
		clearTimeout(timers);
	}
	
</script>
</html>
