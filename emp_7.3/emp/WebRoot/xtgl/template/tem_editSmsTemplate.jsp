<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
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
if(request.getAttribute("temp")==null){
	out.println(MessageUtils.extractMessage("xtgl","xtgl_cswh_dxmbgl_cmbknybsc",request));
	return;
}
LfTemplate tem = (LfTemplate)request.getAttribute("temp");

@SuppressWarnings("unchecked")
List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("smsTemplate");
menuCode = menuCode==null?"0-0-0":menuCode;
%>
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table_detail.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
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
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/tem_editSmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/tem_editSmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>

	<body id="tem_editSmsTemplate">
		<div id="container" class="container">
			<%-- ???????????? --%>
			<div id="rContent" class="rContent">
				<center>
					<div>
						<form action="tem_smsTemplate.htm?method=update" method="post" name="tmform" id="tmform">
						<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
						<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
						<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
						<input type="hidden" id="cztype" name="cztype" value="1" />
							<table id="editSmstmpTable" class="editSmstmpTable">
							<thead>
								<tr class="only_tr">
									<td colspan="2" >
										<emp:message key="xtgl_cswh_dxmbgl_rglrdtmb_only" defVal='?????????????????????????????????????????????????????????"#P_1#"???????????????#P_1#???#P_2#??????' fileName="xtgl"/>
										<input type="hidden" id="OpType" name="OpType"
											value="edit" />
										<input type="hidden" id="tmId" name="tmId" value="<%=tem.getTmid() %>" />
									</td>
								</tr>
								<tr>
									<%if(StaticValue.ZH_HK.equals(langName)){ %>
									
									<td class="spanSmsTmpName_td1">
									<%}else{ %>
									<td class="spanSmsTmpName_td2">
									<%} %>
										<span id="spanSmsTmpName"><emp:message key="xtgl_spgl_mbsp_mbmc_mh" defVal="???????????????" fileName="xtgl"/></span>
									</td>
									<td class="tmName_td">
									<div  class="input_bd div_bd tmName_div">
										<label>
											<input name="tmName" id="tmName"  class="filter tmName"  
												type="text" value="<%=tem.getTmName() %>"
												onkeypress="javascript:if(window.event.keyCode==13) return false;" />
										</label>
									</div>
									</td>
									<td><font class="font_red">&nbsp;*</font></td>
								</tr>
								<tr id="tmcodetr" <%if(tem.getDsflag()-4==0){out.print("class='hidden'");}%>>
									<td class="spanSmsTmpCode_td">
										<span id="spanSmsTmpCode"><emp:message key="xtgl_cswh_dxmbgl_mbbh_mh" defVal="???????????????" fileName="xtgl"/></span>
									</td>
									<td class="tmCode_td">
									<div  class="input_bd div_bd tmCode_div">
										<label>
											<input name="tmCode" id="tmCode" class="filter tmCode"  
												type="text"  maxlength="16" value="<%=tem.getTmCode() %>"/>
												<input type="hidden" name="hiddenCode" id="hiddenCode" value="<%=tem.getTmCode() %>"/>
										</label><font  class="tmCode_down_font"></font>
									</div>
									</td>
									<td><font class="font_red">&nbsp;*</font><font class="yszhzmzc_font">&nbsp;&nbsp;<emp:message key="xtgl_cswh_dxmbgl_yszhzmzc" defVal="????????????????????????" fileName="xtgl"/></font></td>
								</tr>
								<%--
								<tr>
									<td>
										<span>?????????????????????</span>
									</td>
									<td>
										<input name="codeState" type="radio" value="1" onclick="switchState(this);"
											<%if(tem.getBizCode()!=null){ %>
												checked="checked" 
											<%} %>
										/>
										??????????????????
										<input name="codeState" type="radio" value="0" onclick="switchState(this);"
											<%if(tem.getBizCode()==null){ %>
												checked="checked" 
											<%} %>
										/>
										??????????????????
									</td>
								</tr>
								<tr>
									<td>
										<span>???????????????</span>
									</td>
									<td>
										<label>
											<select name="bizCode" id="bizCode"
												<%if(tem.getBizCode()==null) { %>
											 		disabled="disabled"
											 	<%} %>
											 >
												<%
												if(busList!=null && busList.size()>0) {
													for(LfBusManager bus : busList) {
														String code = bus.getBusCode();
												%>
												<option 
													<%if(code.equals(tem.getBizCode())){ %> 
														selected="selected"
													 <%} %>
													value="<%=bus.getBusCode() %>"><%=bus.getBusName()%></option>
												<%
													}
												} 
												%>
											</select>
										</label>
									</td>
								</tr> --%>
								<tr id="trSmsTmpType" <%if(tem.getDsflag()-3!=0){}
											else {out.print("class='trSmsTmpType'");}%>>
									<td>
										<span><emp:message key="xtgl_spgl_mbsp_mblx_mh" defVal="???????????????" fileName="xtgl"/></span>
									</td>
									<td class="dsflag_td">
									<div class="dsflag_div">
										<label>
											<select name="dsflag" id="dsflag" onchange="changeMb(this.value)">
												<option value="0">
													<emp:message key="xtgl_spgl_mbsp_tyjtmk" defVal="??????????????????" fileName="xtgl"/>
												</option>
												<option value="1" <%if(tem.getDsflag()-1==0){out.print("selected=\"selected\"");} %>>
													<emp:message key="xtgl_spgl_mbsp_tydtmk" defVal="??????????????????" fileName="xtgl"/>
												</option>
												<option value="2" <%if(tem.getDsflag()-2==0){out.print("selected=\"selected\"");} %>>
													<emp:message key="xtgl_spgl_mbsp_znzqmk" defVal="??????????????????" fileName="xtgl"/>
												</option>
												<option value="3" <%if(tem.getDsflag()-3==0){out.print("selected=\"selected\"");} %>>
													<emp:message key="xtgl_spgl_mbsp_ydcwmk" defVal="??????????????????" fileName="xtgl"/>
												</option>
												<option value="4" <%if(tem.getDsflag()-4==0){out.print("selected=\"selected\"");} %>>
													<emp:message key="xtgl_cswh_dxmbgl_xtjrmb" defVal="??????????????????" fileName="xtgl"/>
												</option>
											</select>
										</label><input type="button" value="<emp:message key='xtgl_cswh_dxmbgl_tjcs' defVal='????????????' fileName='xtgl'/>" id="butt" class="hidden"/>
										</div>
										<div id="subModule" class="subModule" <%if(tem.getDsflag()-3!=0){out.print("class='hidden'");}
											else {out.print("class='inline'");}%>>
										<div class="zmk_div"><emp:message key="xtgl_cswh_dxmbgl_zmk" defVal="?????????" fileName="xtgl"/><font color="red"><emp:message key="xtgl_cswh_dxmbgl_gmkz" defVal="(????????????????????????????????????)" fileName="xtgl"/></font>???</div>
										<div class="cwcode_div">
										<select id="cwcode" name="cwcode" >
											<option value="MF0001" <%="MF0001".equals(tem.getBizCode())? "selected": "" %>><emp:message key="xtgl_cswh_dxmbgl_dzgzd" defVal="???????????????" fileName="xtgl"/></option>
											<option value="MF0002" <%="MF0002".equals(tem.getBizCode())? "selected": "" %>><emp:message key="xtgl_cswh_dxmbgl_bxtx" defVal="????????????" fileName="xtgl"/></option>
											<option value="MF0003" <%="MF0003".equals(tem.getBizCode())? "selected": "" %>><emp:message key="xtgl_cswh_dxmbgl_hktz" defVal="????????????" fileName="xtgl"/></option>
										</select>
										</div>
										</div>
										
									</td>
								</tr>
								<tr>
									<td>
										<span id="spanSmsTmpCont"><emp:message key="xtgl_spgl_mbsp_mbnr_mh" defVal="???????????????" fileName="xtgl"/></span>
									</td>
									<td class="gbcs_td">
									
									<div class="paraContent div_bd gbcs_div"  >
										<div class="tit_panel div_bd gbcs_div_div"  >
											<a href="javascript:void(0)" class="para_cg"><emp:message key="xtgl_cswh_dxmbgl_gbcs" defVal="????????????" fileName="xtgl"/></a>
										</div>
										<label id="showEditor">
											<textarea id="tmMsg" name="tmMsg" class="input_bd div_bd contents_textarea tmMsg"  onblur="cut()"><%=StringEscapeUtils.escapeHtml(tem.getTmMsg().replaceAll("#[pP]_([1-9][0-9]*)#","{#"+MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_cs",request)+"$1#}")) %></textarea>
										</label>
										<td><font class="font_red">&nbsp;*</font></td>
									</div>
									<div id="fontFmTip" class="fontFmTip" style="<%if(tem.getDsflag()-0==0 || tem.getDsflag()-2==0){out.print("display:none");} %>;"><emp:message key="xtgl_cswh_dxmbgl_csgsw" defVal='?????????????????????{#??????1#}???(????????????{#??????1#}???{#??????2#})' fileName="xtgl"/></div>
									</td>
								</tr>
								<tr class="mbzt_mh_tr">
									<td class="temp_style">
										<span><emp:message key="xtgl_cswh_dxmbgl_mbzt_mh" defVal="???????????????" fileName="xtgl"/></span>
									</td>
									<td class="temp_style">
											<input checked="checked" name="tmState" <%if(tem.getTmState()-1==0){out.print("checked=\"checked\"");} %> 
												type="radio" value="1" />
											<emp:message key="xtgl_spgl_shlcgl_qy" defVal="??????" fileName="xtgl"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input name="tmState" type="radio" value="0" <%if(tem.getTmState()-0==0){out.print("checked=\"checked\"");} %>  />
											<emp:message key="xtgl_spgl_shlcgl_jy" defVal="??????" fileName="xtgl"/>
									</td>
								</tr>
								<tr>
									<td id="btn" colspan="2" class="btn">
										<input name="" type="button" id="subBut" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='??????' fileName='xtgl'/>" class="btnClass5 mr23"/>
										<input name="" type="button" onclick="javascript:window.parent.closeEditSmsTmpdiv()" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='??????' fileName='xtgl'/>" class="btnClass6"/>
										<%-- ????????????ie8??????????????????????????????????????????????????????????????????????????? --%>
										<br/>
									</td>
								</tr>
								</thead>
							</table>
						</form>
					</div>
				</center>
			</div>
			<%-- ???????????? --%>
			<%-- foot?????? --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot?????? --%>
		</div>
    <div class="clear"></div>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
        <script language="javascript" type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script language="javascript" type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script language="javascript" type="text/javascript" src="<%=commonPath %>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"/></script>
		<script language="javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script language="javascript" src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script language="javascript" type="text/javascript" src="<%=iPath%>/js/template.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath %>/common/js/jquery.selection.js?V=116.1" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/param_cg2.js?V=116.1" type="text/javascript"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			$('.para_cg').gotoParam({'width':369,'textarea':'#tmMsg'});
			changeMb($("#dsflag").val());
			getLoginInfo("#hiddenValueDiv");
			noquot("#tmName");
			$('#dsflag').isSearchSelect({'width':'367','select_height':'24','isInput':false},function(o){
				changeMb(o.value)
			});
			$('#cwcode').isSearchSelect({'width':'367','select_height':'24','isInput':false});
			showOfEdit('<%=request.getAttribute("tmresult") %>');

			//ie????????????????????????
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
			
			});
		</script>
	</body>
</html>
