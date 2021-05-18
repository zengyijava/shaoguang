<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.GwProtomtch"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.GwPushprotomtch"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String lguserid = request.getParameter("lguserid");
	String ecid  ="";
	if(request.getAttribute("ecid")!=null){
		ecid=request.getAttribute("ecid").toString();
	}
	Object mwCodeList=request.getAttribute("mwCodeList");
	
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

	String list="";
	if(mwCodeList!=null){
		List<DynaBean> beanlist=(List<DynaBean>)mwCodeList;
		for(int k=0;k<beanlist.size();k++){
			DynaBean db=beanlist.get(k);
			String argname=db.get("argname").toString();
			list=list+"<option value="+argname+">"+argname+"</option>";
		}
	}

	String retMsg="";
	if(request.getAttribute("retMsg")!=null){
		retMsg=(String)request.getAttribute("retMsg");
	}
String number="";
if(request.getAttribute("protoList")!=null){
	List<GwProtomtch> prolist=(List<GwProtomtch>)request.getAttribute("protoList");
	if(prolist.size()!=0){
		number=prolist.size()+"";
	}
}

Object morptListobj=request.getAttribute("morptList");
List<GwProtomtch> morptList =null;
if(morptListobj!=null){
	morptList=(List<GwProtomtch>)morptListobj;
	if(morptList.size()!=0){
		number=morptList.size()+"";
	}
}
String  sflage="false";
if(request.getAttribute("saveValue")!=null&&!"".equals(request.getAttribute("saveValue"))){
	int intsflage=Integer.parseInt(request.getAttribute("saveValue").toString());
	if(intsflage==0){
		sflage="true";
	}else if(intsflage>0){
		sflage="saveSuc";
	}
}

String html="";
if(request.getAttribute("mtmsg")!=null){
	html=request.getAttribute("mtmsg").toString();
    html = html.replaceAll("&", "&amp;");
}

String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

%>
<html>
	<head><%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/frame/frame3.0/skin/default/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/frame/frame3.0/skin/default/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
			<%if(StaticValue.ZH_HK.equals(empLangName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<style>
				.btnClass3 {
					    background: url(frame/frame3.0/skin/default/images/but-bg1.jpg) 0px -168px no-repeat;
					    width: 95px;
				}
		</style>
		<%}%>
		
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/wg_mapping.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/wg_mapping.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="wg_mapping" onload="init();">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<form name="pageForm" action="wg_apiBaseMage.htm?method=saveContext" method="post"
					id="pageForm">
			<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
			<input type="hidden" id="ipathUrl" value="<%=iPath %>"/>
			<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
			<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
			<input id="pathUrl" value="<%=path%>" type="hidden" />
			<input id="ecid" name="ecid" value="<%=ecid%>" type="hidden" />
			<input id="cmdtype" name="cmdtype" value="<%=request.getAttribute("cmdtype")%>" type="hidden" />	
			<input id="funname" name="funname" value="<%=request.getAttribute("funname")%>" type="hidden" />	
			<input id="req" name="req" value="<%=request.getAttribute("req")%>" type="hidden" />
			<input id="resp" name="resp" value="<%=request.getAttribute("resp")%>" type="hidden" />
			<input id="number" name="number" value="<%=number%>" type="hidden" />
			<input id="funtype" name="funtype" value="<%=request.getAttribute("funtype")%>" type="hidden" />
			<%-- 状态 --%>
			<input id="status" name="status" value="<%=request.getAttribute("status")%>" type="hidden" />
			
					
			<div id="rContent" class="rContent rContent2"  >
			<div class="buttons rContent_down_div"  >
			<input type="button" class="btnClass3"  onclick="javascript:toList('1')" id="btn1" value="<emp:message key='txgl_apimanage_text_117' defVal='客户请求' fileName='mwadmin'/>">
			<input type="button" class="btnClass3"  onclick="javascript:toList('2')" id="btn2" value="<emp:message key='txgl_apimanage_text_61' defVal='全成功回应' fileName='mwadmin'/>">
			<input type="button" class="btnClass3"  onclick="javascript:toList('3')" id="btn3" value="<emp:message key='txgl_apimanage_text_62' defVal='全失败回应' fileName='mwadmin'/>">
			<input type="button" class="btnClass3"  onclick="javascript:toList('4')" id="btn4" value="<emp:message key='txgl_apimanage_text_63' defVal='部分成功回应' fileName='mwadmin'/>">
			<input type="button" class="btnClass3"  onclick="javascript:toList('5')" id="btn5" value="<emp:message key='txgl_apimanage_text_114' defVal='详细信息回应' fileName='mwadmin'/>">
			</div>
		<div class="bottom show_div"  >
			<div class="same bottom-left">
				<p><span id="show"><emp:message key='txgl_apimanage_text_90' defVal='请求格式示例' fileName='mwadmin'/></span></p>
				<textarea name="oldStyle" id="oldStyle" rows="" cols=""></textarea>
			</div>
			<div class="same bottom-right">
				<p><emp:message key='txgl_apimanage_text_91' defVal='预览' fileName='mwadmin'/></p>
				<textarea name="newStyle" id="newStyle" rows="" cols=""><%=html%></textarea>
			</div>
			<div class="same bottom-right content_div" >
				<p><emp:message key='txgl_apimanage_text_92' defVal='字段映射关系' fileName='mwadmin'/></p>
				<table id="content">
				<tr class="content_tale_tr" >
				<td  class="tablebord content_tale_tr_td1"><emp:message key='txgl_apimanage_text_93' defVal='客户字段' fileName='mwadmin'/></td>
				<td  class="tablebord content_tale_tr_td2"><emp:message key='txgl_apimanage_text_94' defVal='梦网字段' fileName='mwadmin'/></td>
				<td  class="tablebord content_tale_tr_td3"><emp:message key='txgl_apimanage_text_95' defVal='字段属性' fileName='mwadmin'/></td>
				<td  class="tablebord content_tale_tr_td4"><emp:message key='txgl_apimanage_text_96' defVal='固定值' fileName='mwadmin'/></td>
				</tr>
				
				</table>
			</div>
		</div>

			</div>
				<div class="buttons blok_div"  >
				    <center>
		    		<input id="blok" class="btnClass5 mr23" type="button" value="<emp:message key='txgl_apimanage_text_97' defVal='解 析' fileName='mwadmin'/>" onclick="javascript:analysis()"/>
				    <input id="blc" onclick="javascript:save();" class="btnClass6" type="button" value="<emp:message key='txgl_apimanage_text_98' defVal='保 存' fileName='mwadmin'/>" />
				    <br/>
				    </center>
		    	</div>
			</form>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/apicommon.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/wg_mapping.js" ></script>
		<script type="text/javascript">
		var mwcode='<%=list%>';
		var retmsg="<%=retMsg%>";
		var sflage="<%=sflage%>";
		function init() {
		$(".listAll").remove();
		$("#content").append(retmsg);
		var selectdSytle=$("#cmdtype").val();
		if($.trim(selectdSytle)=='')
		{
			$("#cmdtype").val('1');
		}
		if($("#cmdtype").val()=='1'){
			$("#show").text(getJsLocaleMessage("txgl","txgl_js_mapping_2"));
		}else{
			$("#show").text(getJsLocaleMessage("txgl","txgl_js_mapping_3"));
		}
	if($("#cmdtype").val()=='1'){
		$("#btn1").css("background-position","0% <%=StaticValue.ZH_HK.equals(empLangName)?-168:0%>px");
		$("#btn2").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
		$("#btn3").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
		$("#btn4").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
		$("#btn5").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
	}else{
		if($("#cmdtype").val()=='2'){
			$("#btn1").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn2").css("background-position","0% <%=StaticValue.ZH_HK.equals(empLangName)?-168:0%>px");
			$("#btn3").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn4").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn5").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
		}else if($("#cmdtype").val()=='3'){
			$("#btn1").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn2").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn3").css("background-position","0% <%=StaticValue.ZH_HK.equals(empLangName)?-168:0%>px");
			$("#btn4").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn5").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
		}else if($("#cmdtype").val()=='4'){
			$("#btn1").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn2").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn3").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn4").css("background-position","0% <%=StaticValue.ZH_HK.equals(empLangName)?-168:0%>px");
			$("#btn5").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
		}else if($("#cmdtype").val()=='5'){
			$("#btn1").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn2").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn3").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn4").css("background-position","0% -<%=StaticValue.ZH_HK.equals(empLangName)?168:26 %>px");
			$("#btn5").css("background-position","0% <%=StaticValue.ZH_HK.equals(empLangName)?-168:0%>px");
		}
	}
	}
	function analysis(){
	var status=$("#status").val();
	if(status=="1"){
		alert(getJsLocaleMessage("txgl","txgl_js_mapping_4"));
			return;
	}
	var req=$("#req").val();
	var resp=$("#resp").val();
	var cmdtype=$("#cmdtype").val();
	var oldStyle=$("#oldStyle").val();
	
	if($.trim(oldStyle)==''){
		alert(getJsLocaleMessage("txgl","txgl_js_mwedithttps_6"));
		return;
	}
			$.post('wg_apiBaseMage.htm?method=checkStyle', {
				req:req,
				resp:resp,
				oldStyle:oldStyle,
				cmdtype:cmdtype
				},function(msg)
			{
				// 转换成json对象   
			  var jsonresult = $.parseJSON(msg);
			  var result = jsonresult.style;
			  var nodeList=jsonresult.nodeList;
			  	var flag="false";
	if(cmdtype=='1'&&req=='1'){
		flag="true";
	}else if(cmdtype=='1'&&req=='2'){
		flag="true";
	}else if(cmdtype=='2'&&resp=='1'){
		flag="true";
	}else if(cmdtype=='2'&&resp=='2'){
		flag="true";
	}else if(cmdtype=='3'&&resp=='1'){
		flag="true";
	}else if(cmdtype=='3'&&resp=='2'){
		flag="true";
	}else if(cmdtype=='4'&&resp=='1'){
		flag="true";
	}else if(cmdtype=='4'&&resp=='2'){
		flag="true";
	}else if(cmdtype=='5'&&resp=='1'){
		flag="true";
	}else if(cmdtype=='5'&&resp=='2'){
		flag="true";
	}
				if(result!=""){
					$('#newStyle').val(result);
					if(nodeList!=null&&nodeList!=''){
					var nodes= nodeList.split(',');
					$(".listAll").remove();
					$('#number').val(nodes.length);
					if(flag=='true'){
						for(var i=0;i<nodes.length;i++){
							var client_node=nodes[i].split(':');
						$("#content").append("<tr style='background:#dfe9f9' class='listAll'><td>"+client_node[0]+"<input name='cargname"+i+"' value='"+client_node[0]+"' type='hidden' /><input name='mainNode"+i+"' value='"+client_node[1]+"' type='hidden' /></td>" +
						"<td> <select name='mwfield"+i+"' id='mwfield"+i+"' onchange='change(this)'>" +
						/*固定值  忽略值*/
						"<option value='0'>"+getJsLocaleMessage("txgl","txgl_js_mapping_9")+"</option>" +
						"<option value='-1'>"+getJsLocaleMessage("txgl","txgl_js_mapping_10")+"</option>" +mwcode+
						"</select></td><td>" +
						"<select name='mwattr"+i+"' onchange='propertySelect(this,8)'>" +
						"<option value='8'>string</option>" +
						"<option value='1'>xml</option>" +
						"<option value='2'>json</option>" +
						"<option value='4'>urlencode</option>" +
						"<option value='16'>int</option>" +
						"<option value='32'>time</option>" +
						"<option value='64'>other</option>" +
						"</select>" +
						"</td><td><input type='text' name='fixed"+i+"' value='' id='fixed"+i+"' style='width: 30px;' maxlength='30'></td></tr>");
					}
					}else{
						for(var i=0;i<nodes.length;i++){
						$("#content").append("<tr style='background:#dfe9f9' class='listAll'><td>"+nodes[i]+"<input name='cargname"+i+"' value='"+nodes[i]+"' type='hidden' /></td>" +
						"<td> <select name='mwfield"+i+"' id='mwfield"+i+"' onchange='change(this)' >" +
						/*固定值  忽略值*/
						"<option value='0'>"+getJsLocaleMessage("txgl","txgl_js_mapping_9")+"</option>" +
						"<option value='-1'>"+getJsLocaleMessage("txgl","txgl_js_mapping_10")+"</option>" +mwcode+
						"</select></td><td>" +
						"<select name='mwattr"+i+"' onchange='propertySelect(this,8)' >" +
						"<option value='8'>string</option>" +
						"<option value='1'>xml</option>" +
						"<option value='2'>json</option>" +
						"<option value='4'>urlencode</option>" +
						"<option value='16'>int</option>" +
						"<option value='32'>time</option>" +
						"<option value='64'>other</option>" +
						"</select>" +
						"</td><td><input type='text' name='fixed"+i+"' id='fixed"+i+"' value='' style='width: 30px;' maxlength='30'></td></tr>");
					}
					}

				}
				}else{
				    /*解析失败，请检查格式是否正确！*/
					alert(getJsLocaleMessage("txgl","txgl_js_userdata_18"));
				}

			});
}
		</script>
	</body>
</html>
