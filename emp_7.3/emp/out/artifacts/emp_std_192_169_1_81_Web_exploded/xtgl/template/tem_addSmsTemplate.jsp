<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
String langName = (String)session.getAttribute("emp_lang");
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

@SuppressWarnings("unchecked")
List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");

@SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("smsTemplate");
menuCode = menuCode==null?"0-0-0":menuCode;

String showType = request.getParameter("showType");
String fromState=(String)request.getAttribute("fromState");
String dsflag = request.getParameter("dsflag");
if(dsflag == null){dsflag = "0";}
%>
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table_detail.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/params.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<style type='text/css'>
			<%if(StaticValue.ZH_HK.equals(langName)){%>
				.showParams li{
					margin: 0px 3px 4px 0px;
					width: 65px;
				}
			<%}%>
		</style>
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/tem_addSmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/tem_addSmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>

	<body id="tem_addSmsTemplate">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<center>
					<div>
						<form action="tem_smsTemplate.htm?method=update" method="post" name="tmform" id="tmform">
						<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
						<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
						<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
						<%--判断从哪发出的请求，如果为1是发送模块,其余说明是从短信模板管理--%>
						<input type="hidden" id="fromState" name="fromState" value="<%=fromState!=null&&!"".equals(fromState)?fromState:"2" %>"/>
						<input type="hidden" id="cztype" name="cztype" value="0" />
							<table id="editSmstmpTable" class="editSmstmpTable">
							<thead>
								<tr class="rglrdtmb_tr">
									<td colspan="2">
										<emp:message key="xtgl_cswh_dxmbgl_rglrdtmb" defVal='如果录入动态模板或财务模板，参数格式为"#P_1#"（如：我们#P_1#去#P_2#。）' fileName="xtgl"/>
										<input type="hidden" id="OpType" name="OpType"
											value="add" />
									</td>
								</tr>
								
								<tr>
									<%if(StaticValue.ZH_HK.equals(langName)){ %>
									
									<td class="spanSmsTmpName_td1">
									<%}else{ %>
									<td class="spanSmsTmpName_td2">
									<%} %>
										<span id="spanSmsTmpName"><emp:message key="xtgl_spgl_mbsp_mbmc_mh" defVal="模板名称：" fileName="xtgl"/></span>
									</td>
									<td class="tmName_td">
									<div  class="input_bd div_bd tmName_div">
										<label>
											<input name="tmName" id="tmName" class="filter tmName"  
												type="text" onkeypress="if(event.keyCode==13) return false;" maxlength="20"/>
										</label><font  class="cdbndy_font"><emp:message key="xtgl_cswh_dxmbgl_cdbndy" defVal="长度不能大于20" fileName="xtgl"/></font>
									</div>
									</td>
									<td><font class="font_red">&nbsp;*</font></td>
								</tr>
									<tr id="tmcodetr">
									<td class="spanSmsTmpCode_td">
										<span id="spanSmsTmpCode"><emp:message key="xtgl_cswh_dxmbgl_mbbh_mh" defVal="模板编号：" fileName="xtgl"/></span>
									</td>
									<td class="tmCode_td">
									<div  class="input_bd div_bd tmCode_div">
										<label>
											<input name="tmCode" id="tmCode" class="filter tmCode"  
												type="text"  maxlength="16"/>
												<input type="hidden" name="hiddenCode" id="hiddenCode" value=""/>
										</label><font  class="tmCode_down_font"></font>
									</div>
									</td>
									<td><font id="mbcodexh"  class="font_red">&nbsp;*</font><font id="mbcodems" class="mbcodems">&nbsp;&nbsp;<emp:message key="xtgl_cswh_dxmbgl_yszhzmzc" defVal="由数字或字母组成" fileName="xtgl"/></font></td>
								</tr>
								
								<%--
								<tr>
									<td>
										<span>业务编码状态：</span>
									</td>
									<td>
										<input name="codeState" type="radio" value="1" onclick="switchState(this);"/>
										启用业务编码
										<input name="codeState" type="radio" value="0"  checked="checked"  onclick="switchState(this);"/>
										禁用业务编码
									</td>
								</tr>
								<tr>
									<td>
										<span>业务编码：</span>
									</td>
									<td>
										<label>
											<select name="bizCode" id="bizCode" disabled="disabled">
												<%
												if(busList!=null && busList.size()>0) {
													for(LfBusManager bus : busList) {
												%>
												<option value="<%=bus.getBusCode() %>"><%=bus.getBusName()%></option>
												<%
													}
												} 
												%>
											</select>
										</label>
									</td>
								</tr> --%>
								<tr id="trSmsTmpType">
									<td>
										<span><emp:message key="xtgl_spgl_mbsp_mblx_mh" defVal="模板类型：" fileName="xtgl"/></span>
									</td>
									<td class="dsflag_td">
									<div class="dsflag_div" >
										<label>
											<select name="dsflag" id="dsflag" onchange="changeMb(this.value)">
												<option value="0">
													<emp:message key="xtgl_spgl_mbsp_tyjtmk" defVal="通用静态模块" fileName="xtgl"/>
												</option>
												<option value="1">
													<emp:message key="xtgl_spgl_mbsp_tydtmk" defVal="通用动态模块" fileName="xtgl"/>
												</option>
												<option value="2">
													<emp:message key="xtgl_spgl_mbsp_znzqmk" defVal="智能抓取模块" fileName="xtgl"/>
												</option>
												<option value="3">
													<emp:message key="xtgl_spgl_mbsp_ydcwmk" defVal="移动财务模块" fileName="xtgl"/>
												</option>
												<option value="4">
													<emp:message key="xtgl_cswh_dxmbgl_xtjrmb" defVal="系统接入模板" fileName="xtgl"/>
												</option>
											</select>
										</label><input type="button" value="<emp:message key='xtgl_cswh_dxmbgl_tjcs' defVal='添加参数' fileName='xtgl'/>" id="butt" class="hidden"/>
										</div>
										<div id="subModule"  class="hidden subModule">
										<div class="zmk_div"><emp:message key="xtgl_cswh_dxmbgl_zmk" defVal="子模块" fileName="xtgl"/><font class="font_red"><emp:message key="xtgl_cswh_dxmbgl_gmkz" defVal="(该模块只支持动态模板发送)" fileName="xtgl"/></font>：</div>
										<div class="cwcode_div">
										<select id="cwcode" name="cwcode">
											<option value="MF0001"><emp:message key="xtgl_cswh_dxmbgl_dzgzd" defVal="电子工资单" fileName="xtgl"/></option>
											<option value="MF0002"><emp:message key="xtgl_cswh_dxmbgl_bxtx" defVal="报销提醒" fileName="xtgl"/></option>
											<option value="MF0003"><emp:message key="xtgl_cswh_dxmbgl_hktz" defVal="回款通知" fileName="xtgl"/></option>
										</select>
										</div>											
										</div>										
									</td>
								</tr>
								<tr>
									<td>
										<span id="spanSmsTmpCont"><emp:message key="xtgl_spgl_mbsp_mbnr_mh" defVal="模板内容：" fileName="xtgl"/></span>
									</td>
									<td class="gbcs_td">
									<div class="paraContent div_bd gbcs_div"  >
										<div class="tit_panel div_bd gbcs_div_div"  >
											<a href="javascript:void(0)" class="para_cg"><emp:message key="xtgl_cswh_dxmbgl_gbcs" defVal="关闭参数" fileName="xtgl"/></a>
										</div>
										<label id="showEditor">
										<textarea id="tmMsg" name="tmMsg" class="input_bd div_bd contents_textarea tmMsg"  onblur="cut()"></textarea>
										</label>
										<td><font class="font_red">&nbsp;*</font></td>
									</div>
									<div id="fontFmTip" class="fontFmTip"><emp:message key="xtgl_cswh_dxmbgl_csgsw" defVal='参数格式为：“{#参数1#}”(如：我和{#参数1#}去{#参数2#})' fileName="xtgl"/></div>
									</td>
								</tr>
								<tr class="mbzt_mh_tr">
									<td class="temp_style" >
										<span><emp:message key="xtgl_cswh_dxmbgl_mbzt_mh" defVal="模板状态：" fileName="xtgl"/></span>
									</td>
									<td class="temp_style">
											<input checked="checked" name="tmState" checked="checked"
												type="radio" value="1" />
											<emp:message key="xtgl_spgl_shlcgl_qy" defVal="启用" fileName="xtgl"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input name="tmState" type="radio" value="0" />
											<emp:message key="xtgl_spgl_shlcgl_jy" defVal="禁用" fileName="xtgl"/>
									</td>
								</tr>
								<tr>
									<td id="btn" colspan="2" class="btn">
										<input name="" type="button" id="subBut" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" class="btnClass5 mr23"/>
										<input name="" type="button" onclick="window.parent.closeAddSmsTmpdiv()" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" class="btnClass6"/>
										<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
										<br/>
									</td>
								</tr>
								</thead>
							</table>
						</form>
					</div>
				</center>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"/></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/template.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath %>/common/js/jquery.selection.js?V=116.1" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/param_cg2.js?V=116.1" type="text/javascript"></script>
		<script type="text/javascript">

		$(document).ready(function() {
			$('.para_cg').gotoParam({'width':369,'textarea':'#tmMsg'});
			getLoginInfo("#hiddenValueDiv");
			noquot("#tmName");
			var dsflag = <%=dsflag%>;
			changeMb(dsflag);
			$("#dsflag").find("option[value=<%=dsflag%>]").attr("selected",true);
			$('#dsflag').isSearchSelect({'width':'367','select_height':'24','isInput':false},function(o){
				changeMb(o.value)
			});
			$('#cwcode').isSearchSelect({'width':'367','select_height':'24','isInput':false});
			showOfAdd('<%=request.getAttribute("tmresult") %>','<%=fromState %>');
			});
		
			//ie浏览器兼容性判断
			var isIE = (document.all) ? true : false;
			var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0");
			var isIE7 = isIE && (navigator.appVersion.match(/MSIE 7.0/i)=="MSIE 7.0");
			var isIE8 = isIE && (navigator.appVersion.match(/MSIE 8.0/i)=="MSIE 8.0");
			var isIE9 = isIE && (navigator.appVersion.match(/MSIE 9.0/i)=="MSIE 9.0");
			if(isIE6){
				$("#fontFmTip").css("margin-left","50px");
			}else  if(isIE7||isIE8||isIE9){
				$("#fontFmTip").css("margin-left","90px");
			}else{
				$("#fontFmTip").css("margin-left","120px");
			}
		</script>
	</body>
</html>
