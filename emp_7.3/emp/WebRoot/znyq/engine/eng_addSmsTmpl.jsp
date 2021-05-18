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
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=commonPath%>/common/css/table_detail.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/params.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
		<style type='text/css'>
			<%if(StaticValue.ZH_HK.equals(langName)){%>
			.showParams li {
				width: 90px;
			}
			<%}%>
		</style>
	</head>

	<body onload="showOfAdd('<%=request.getAttribute("tmresult") %>','<%=fromState %>')" id="eng_addSmsTmpl" class="eng_addSmsTmpl">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<center>
					<div >
						<form action="<%=path %>/eng_mtProcess.htm?method=addSmsTmpl" method="post" name="tmform" id="tmform">
						<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
						<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
						<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
						<%--判断从哪发出的请求，如果为1是发送模块,其余说明是从短信模板管理--%>
						<input type="hidden" id="fromState" name="fromState" value="<%=fromState!=null&&!"".equals(fromState)?fromState:"2" %>"/>
						
							<table id="editSmstmpTable">
							<thead>
								<tr class="opTyepTr">
									<td colspan="2">
										<emp:message key="znyq_ywgl_xhywgl_rglrdtmbhcwmb" defVal="如果录入动态模板或财务模板，参数格式为\"#P_1#\"（如：我们#P_1#去#P_2#。）" fileName="znyq"></emp:message>
										<input type="hidden" id="OpType" name="OpType"
											value="add" />
									</td>
								</tr>
								<tr>
									<td class="SmsTmpNameTd">
										<span id="spanSmsTmpName"><emp:message key="znyq_ywgl_xhywgl_mbmc_mh" defVal="模板名称：" fileName="znyq"></emp:message></span>
									</td>
									<td>
									<div id="modelNameDiv" class="input_bd div_bd">
										<label>
											<input name="tmName" id="tmName"
												type="text" onkeypress="if(event.keyCode==13) return false;" maxlength="20"/>
										</label><font class="tmNameFt"><emp:message key="znyq_ywgl_xhywgl_cdbndy20" defVal="长度不能大于20" fileName="znyq"></emp:message></font>
									</div>
									</td>
								</tr>
								
								<tr>
									<td class="SmsTmpCodeTd">
										<span id="spanSmsTmpCode"><emp:message key="znyq_ywgl_xhywgl_mbbh_mh" defVal="模板编号：" fileName="znyq"></emp:message></span>
									</td>
									<td>
									<div id="tmCodeDiv" class="input_bd div_bd">
										<label>
											<input name="tmCode" id="tmCode" class="filter"
												type="text"  maxlength="16"/>
												<input type="hidden" name="hiddenCode" id="hiddenCode" value=""/>
										</label><font class="tmCodeFt"></font>
									</div>
									</td>
								</tr>
								
								<tr id="trSmsTmpType">
									<td>
										<span><emp:message key="znyq_ywgl_xhywgl_mblx_mh" defVal="模板类型：" fileName="znyq"></emp:message></span>
									</td>
									<td>
									<div class="modelTypeDiv">
										<label>
											<select name="dsflag" id="dsflag" onchange="changeMb(this.value)">
												<option value="0">
													<emp:message key="znyq_ywgl_xhywgl_tyjtmk" defVal="通用静态模块" fileName="znyq"></emp:message>
												</option>
												<option value="1">
													<emp:message key="znyq_ywgl_xhywgl_tydtmk" defVal="通用动态模块" fileName="znyq"></emp:message>
												</option>
												<option value="2">
													<emp:message key="znyq_ywgl_xhywgl_znzqmk" defVal="智能抓取模块" fileName="znyq"></emp:message>
												</option>
											</select>
										</label><input type="button" value="<emp:message key="znyq_ywgl_xhywgl_tjcs" defVal="添加参数" fileName="znyq"></emp:message>" id="butt" class="hidden"/>
										</div>
										<div id="subModule" class="hidden">
										<div class="childModelDiv"><emp:message key="znyq_ywgl_xhywgl_zmk" defVal="子模块" fileName="znyq"></emp:message><font color="red"><emp:message key="znyq_ywgl_xhywgl_gmkzzcdtmbfs" defVal="(该模块只支持动态模板发送)" fileName="znyq"></emp:message></font>：</div>
										<div class="cwcodeDiv">
										<select id="cwcode" name="cwcode">
											<option value="MF0001"><emp:message key="znyq_ywgl_xhywgl_dzgzd" defVal="电子工资单" fileName="znyq"></emp:message></option>
											<option value="MF0002"><emp:message key="znyq_ywgl_xhywgl_bxtx" defVal="报销提醒" fileName="znyq"></emp:message></option>
											<option value="MF0003"><emp:message key="znyq_ywgl_xhywgl_hktz" defVal="回款通知" fileName="znyq"></emp:message></option>
										</select>
										</div>
											
										</div>
										
									</td>
								</tr>
								<tr>
									<td>
										<span id="spanSmsTmpCont"><emp:message key="znyq_ywgl_xhywgl_mbnr" defVal="模板内容：" fileName="znyq"></emp:message></span>
									</td>
									<td class="SmsTmpContTd">
									<div class="paraContent div_bd">
									<div class="tit_panel div_bd">
										<a href="javascript:void(0)" class="para_cg"><emp:message key="znyq_ywgl_xhywgl_gbcs_mh" defVal="关闭参数" fileName="znyq"></emp:message></a>
									</div>	
										<label id="showEditor">
											<textarea id="tmMsg" name="tmMsg" class="input_bd div_bd contents_textarea"  onblur="cut()"></textarea>
										</label>
									</div>	
										<div id="fontFmTip"><emp:message key="znyq_ywgl_xhywgl_csgsw" defVal="参数格式为：“#参数1#”(如：我和#参数1#去#参数2#)" fileName="znyq"></emp:message></div>




									</td>
								</tr>
								<tr >
									<td class="temp_style">
										<span><emp:message key="znyq_ywgl_xhywgl_mbzt_mh" defVal="模板状态：" fileName="znyq"></emp:message></span>
									</td>
									<td class="temp_style">
											<input checked="checked" name="tmState" checked="checked"
												type="radio" value="1" />
											<emp:message key="znyq_ywgl_xhywgl_qy" defVal="启用" fileName="znyq"></emp:message>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input name="tmState" type="radio" value="0" />
											<emp:message key="znyq_ywgl_xhywgl_jy" defVal="禁用" fileName="znyq"></emp:message>
									</td>
								</tr>
								<tr>
									<td id="btn" colspan="2">
										<input name="" type="button" id="subBut" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" class="btnClass5 mr23"/>
										<input name="" type="button" onclick="javascript:window.parent.closeAddSmsTmpdiv()" value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>" class="btnClass6"/>
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
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"/></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_addAsmsTmpl.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script src="<%=commonPath %>/common/js/jquery.selection.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/param_cg2.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">

		$(document).ready(function() {
			$('.para_cg').gotoParam({'width':369,'textarea':'#tmMsg'});
			getLoginInfo("#hiddenValueDiv");
			noquot("#tmName");
			changeMb(<%=showType%>);
			$("#dsflag").find("option[value=<%=showType%>]").attr("selected",true);
			$('#dsflag').isSearchSelect({'width':'367','select_height':'24','isInput':false},function(o){
				changeMb(o.value)
			});
			$('#cwcode').isSearchSelect({'width':'367','select_height':'24','isInput':false});
		});

		</script>
	</body>
</html>
