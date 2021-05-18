<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

MessageUtils messageUtils = new MessageUtils();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("routeManage");
menuCode = menuCode==null?"0-0-0":menuCode;

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
String jsonStr = (String)request.getAttribute("json"); 

//保存
String bc = messageUtils.extractMessage("txgl", "txgl_wygl_wylybd_bc", request);


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  	<%@include file="/common/common.jsp" %>
    <title>My JSP 'wy_routeManage.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css?V=<%=StaticValue.JSP_IMP_VERSION %>">
	--%>
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<style><%--
		#systab
		{
			margin-left:20px;
			font-size: 12px; 
		}
		#tabyys {
			display: block;
			
			<%if(StaticValue.ZH_HK.equals(langName)){%>
				width: 140px;
			<%}else{%>
				width: 95px;
			<%}%>
			height: 20px;
			line-height: 20px;
			margin-left: 20px;
			margin-bottom: -10px;
			position: relative;
			background-color: #FFF;
			font-weight: 800;
			text-align: left;
			padding: 0 3px;
		}
		#lyout {
			width: 450px;
			padding: 20px;
			margin-bottom: 15px;
			display: block;
		}
		#td_height_fir
		{
			height: 40px;
		}
		.con_span
		{
			padding:5px 10px; 
		}
		.gn_width
		{
		}
		.ly_div_con_sty
		{
			padding: 5px;
		}
		.div_che
		{
			padding-bottom: 5px;
		}
	--%></style>
  </head>
  
  <body>
    <div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<form name="pageForm" action="p_busBindSp.htm" method="post" id="pageForm">
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent" style="">
			<div id="loginInfo" class="hidden"></div>
				<table id="systab">
					<tr>
						<td id="td_height_fir" ><emp:message key="txgl_wygl_wylybd_lylx" defVal="路由类型" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></td>
						<td width="350px">
						<select id="type" name="type" onchange="routeChange()" >
								<option value="0"><emp:message key="txgl_wygl_wylybd_xhzw" defVal="携号转网" fileName="txgl"></emp:message></option>
								<option value="1"><emp:message key="txgl_wygl_wylybd_tshm" defVal="特殊号码" fileName="txgl"></emp:message></option>
								<%-- 由于V5.3版本中增加了国际短信的支持，所以去掉
								<option value="2">国际号码</option> --%>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<div id="tabyys"><emp:message key="txgl_wygl_wylybd_bdlytd" defVal="绑定路由通道" fileName="txgl"></emp:message></div>
							<div id="lyout" class="div_bd">
							
								<div id="lg_div">
									<div class="div_che">
										<label>
											<input type="checkbox" name="checkAll" id="checkAll" onclick="checkAlls(this,'checklist')">
											<span><emp:message key="txgl_wygl_wylybd_qx" defVal="全选" fileName="txgl"></emp:message></span>
										</label>
									</div>
								</div>
								<div id="lg_div_con">
								</div>
								<%if(btnMap.get(menuCode+"-1")!=null) { %>
								<div style="text-align: center;padding-top: 20px;">
									<input type="button" id="btnOK" value="<%=bc %>" onclick="bind()" class="btnClass5"/>
								</div>
								<%} %>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<emp:message key="txgl_wygl_wylybd_tsxhzwlytshmly" defVal="提示：携号转网路由、特殊号码路由绑定哪个通道，决定了该路由类型发送" fileName="txgl"></emp:message><br/><emp:message key="txgl_wygl_wylybd_dxxynngtdjxznfs" defVal="的信息由哪个通道进行智能发送。" fileName="txgl"></emp:message>
						</td>
					</tr>
				</table>
			</div>
			<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</form>
		</div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=iPath%>/js/spePhoneManage.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>

	<script type="text/javascript">
		var path='<%=path%>';
		$(document).ready(function() {
			getLoginInfo("#loginInfo");
			$("#type").isSearchSelect({'width':'158','isInput':false,'zindex':0},function(){routeChange();});
			var jsonStr='<%=jsonStr%>';
			json(jsonStr);
			jsonStr=null;
		});
		
		function json(jsonStr)
		{
			if(jsonStr=="error")
			{
				//alert("网优绑定查询错误！");
				alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_3"));
				return;
			}
			var data =eval('('+jsonStr+')');
			var tempArr = [];
			$("#lg_div_con").html("");
			var htmlStr="";
			if(data.length==0)
			{
				//$("#lyout").html("<div id='no_con_div'>未存在网优通道！</div>");
				$("#lyout").html("<div id='no_con_div'>"+getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_4")+"</div>");
				return;
			}
			tempArr.push("<table>");
			for(var i=0;i<data.length;i++)
			{
				tempArr.push("<tr><td><input type='checkbox' name='checklist' value='"+data[i].ip_gateid+"'"+(data[i].is_bind=="yes"?" checked":"")+"></td>");
				tempArr.push("<td class='con_span' >"+data[i].spgate+"</td>");
				tempArr.push("<td class='con_span gn_width'>"+data[i].gatename+"</span>");
				tempArr.push("<td class='con_span'>"+data[i].corpsign+"</td></tr>");
				//tempArr.push("<div class='ly_div_con_sty'>");
				//tempArr.push("<div  style='width:440px;float:left;'><span class='con_span'><input type='checkbox' name='checklist' value='"+data[i].ip_gateid+"'"+(data[i].is_bind=="yes"?" checked":"")+"></span>");
				//tempArr.push("<span class='con_span' style='width:80px'>"+data[i].spgate+"</span>");
				//tempArr.push("<span class='con_span gn_width'>"+data[i].gatename+"</span>");
				//tempArr.push("<span class='con_span' style='width:100px'>"+data[i].corpsign+"</span></lable></div></div>");
			}
			tempArr.push("</table>");
			$("#lg_div_con").html(tempArr.join(''));
			tempArr=null;
		}
		
		function checkAlls(e,str)
		{  
			var a = document.getElementsByName(str);    
			var n = a.length;    
			for (var i=0; i<n; i=i+1)
			{
				a[i].checked =e.checked;
			}
		}
		
		//路由类型改变
		function routeChange()
		{
			$("#checkAll").attr("checked",false);
			var lguserid = $("#lguserid").val();
			var lgcorpcode = $("#lgcorpcode").val();
			var lgusername = $("#lgusername").val();
			var type=$("#type").val();
			$.post("wy_routeManage.htm",
					{method:"getJsonByType",lgcorpcode:lgcorpcode,lguserid: lguserid,type:type,lgusername:lgusername,time:new Date(),isAsync:"yes"},
					function(result){
						if(result == "outOfLogin")
						{
							$("#logoutalert").val(1);
							location.href=path+"/common/logoutEmp.html";
							return;
						}
						else if(result!="error")
						{
							json(result);
						}
						else
						{
							//alert("删除失败！");
						}
					});
		}
		function bind()
		{
			var id=""
			$("input[name='checklist']").each(function(){
				if($(this).attr("checked"))
				{
					id=id+$(this).val()+",";
				}
			});
			/*if(id=="")
			{
				alert("请选择路由通道！");
				return;
			}*/
			
			var type = $("#type").val();
			var lguserid = $("#lguserid").val();
			var lgcorpcode = $("#lgcorpcode").val();
			var lgusername = $("#lgusername").val();
			$("#btnOK").attr("disabled","disabled");
			$.post("wy_routeManage.htm",
					{method:"bind",lgcorpcode:lgcorpcode,lguserid: lguserid,id:id,type:type,lgusername:lgusername,time:new Date(),isAsync:"yes"},
					function(result){
						$("#btnOK").attr("disabled","");
						if(result == "outOfLogin")
						{
							$("#logoutalert").val(1);
							location.href=path+"/common/logoutEmp.html";
							return;
						}
						else if(result!="error")
						{
							//alert("保存成功！");
							alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_5"));
							routeChange();
						}
						else
						{
							//alert("保存失败！");
							alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_6"));
						}
					});
		}
	</script>
  </body>
</html>
