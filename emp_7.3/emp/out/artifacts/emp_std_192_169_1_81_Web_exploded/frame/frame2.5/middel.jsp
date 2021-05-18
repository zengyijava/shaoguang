<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath()+session.getAttribute("empRoot");
String bpath = request.getContextPath();
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

String openMenuCode = request.getParameter("openMenuCode");
String priMenus = request.getParameter("priMenus");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><%@ include file="/common/common.jsp"%>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<jsp:include page="/common/commonfile.jsp"></jsp:include>
<link href="<%=iPath %>/css/middel.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/middel.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<style type="text/css">
  #menuFrame{position:relative;}
  #load-bg{
    display: none;
    position: absolute;
    top: 0%;
    left: 0%;
    width: 100%;
    height: 100%;
    z-index:99999;
    -moz-opacity: 0.4;
    opacity:.40;
    filter: alpha(opacity=40);
    background:#eee url('<%=commonPath %>/common/img/loading-bg.gif') no-repeat center
  }
</style>
</head>
<body  >
<input type="hidden" id="isMiddel" value="1"/>
<input type="hidden" id="path" value="<%=request.getContextPath() %>"/>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" style="table-layout:fixed;">
  <tr>
    <td width="190" id=frmTitle noWrap name="fmTitle" align="center" valign="top" >
	   <div style="height:100%;display:block;width:100%;">
	        <iframe name="I1" height="100%" width="190" id="leftIframe" allowtransparency="true" 
	        	src="<%=bpath %>/thirdMenu.htm?method=getAllPriList&priMenus=<%=priMenus %>&openMenuCode=<%=openMenuCode %>"
	         	border="0" frameborder="0" scrolling="no"> <emp:message key="common_frame2_middle_1" defVal="浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。" fileName="common"></emp:message></iframe>
		</div>		
    </td>
   
    <div class="middle_title_bg"></div>
    <div  class="middle_toggle" onclick="javascript:switchSysBar($(this))"></div>
    
    <td align="center" valign="top" style="">
     <div id="menuFrame" style="height:100%;display:block;width:100%;">
         <%-- 共享模板 DIV --%>
         <div id="shareTmpDiv" title="<emp:message key='xtgl_cswh_dxmbgl_mbgx' defVal='模板共享' fileName='xtgl'/>" class="shareTmpDiv">
             <input type="hidden" id="templType" value="4">
             <center>
                 <iframe id="flowFrame" name="flowFrame" src=""  attrid="" tmpname="" class="flowFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
             </center>
             <table>
                 <tr>
                     <td class="shlcgl_qd_td" align="center">
                         <input type="button"  value="<emp:message key='common_spgl_shlcgl_qd' defVal='确定' fileName='common'/>" id="updateShareTemp" class="btnClass5 mr23" onclick="javascript:updateShareTemp();" />
                         <input type="button"  value="<emp:message key='common_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/> " class="btnClass6" onclick="javascript:closeShare();" />
                         <%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
                         <br/>
                     </td>
                 </tr>
             </table>
         </div>
        <div id="load-bg"></div>
    	<iframe name="cont" id="cont" height="100%" width="100%" border="0" frameborder="0" allowtransparency="true"> <emp:message key="common_frame2_middle_1" defVal="浏览器不支持嵌入式框架，或被配置为不显示嵌入式框架。" fileName="common"></emp:message></iframe>
    	</div>
    </td>
  </tr>
</table>
<script type="text/javascript" src="<%=iPath %>/js/middel.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript">
var urlRouter={};
$(function(){
	 urlRouter={
		  modId:20,//在线客服模块ID
		  flag:1,//1表示开启跳转
		  userid:getField('#userid')||getLoginparams('#lguserid'),
		  tkn:getField('#appTkn')
	};
 function getField(obj){
 	return $(obj).val();
 }
 function getLoginparams(obj){
 	var $pa = $(window.parent.document);
 	var pahtm = $pa.find("#loginparams").children(obj).val();
 	return pahtm;
 }
    //新增共享模板DIV
    $("#shareTmpDiv").dialog({
        autoOpen: false,
        height:500,
        width: 530,
        resizable:false,
        modal: true
    });
})
function showLeftMenu1(idstr) {
	$('#leftIframe').attr('src', $('#leftIframe').attr('src'));
	$("#languageDiv",window.parent.parent.document).dialog('close');
	$('#menu', window.parent.document).html(idstr);
	if (!$("#" + idstr).hasClass("cursel")) {
		$(".cursel").removeClass("cursel");
		$("#" + idstr).addClass("cursel");
		urlRouter.tkn=0;
		if(urlRouter.tkn){
			ajaxOnline(urlRouter,idstr);
		}else{
			urlRouter.tkn=$('#appTkn').val();
			ajaxOnline(urlRouter,idstr);
		}
		
	}
}

function ajaxOnline(urlRouter,idstr){
	if(urlRouter['flag'] && ('mod'+urlRouter['modId'])==idstr){
	//在线坐席 对不兼容浏览器的处理
	if(!isBrowserOk()){
		return;
	}
	$.ajax({
		type:'GET',
		url:path+'/customChat.htm?method=checkUser',
		data:'userid='+urlRouter.userid+'&tkn='+urlRouter.tkn+'&isAsync=yes',
		success:function(data){
		    if(data){
		    	if(data == "outOfLogin")
				{
					location.href=path+"/common/logoutEmp.jsp";
					return;
				}
		    	data=eval('('+data+')');
				if(data['aid']==0){
					alert(data['msg']);
					return false;
				}else{
					window.open(path+'/'+data['url']);
				}
		    }
			
		}
	});
	}else{
		$(window.parent.document).find("#mainFrame").contents().find("#leftIframe")[0].contentWindow.showMenuTable(idstr);
	}
}


</script>
</body>
</html>
