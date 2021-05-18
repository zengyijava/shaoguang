<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.sysconf.vo.ParamConfVo" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");		
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);

	List<ParamConfVo> corpConfVoList = (List<ParamConfVo>)request.getAttribute("corpConfVoList");
	List<ParamConfVo> sysParamList = (List<ParamConfVo>)request.getAttribute("sysParamList");
	List<ParamConfVo> globalVariableList = (List<ParamConfVo>)request.getAttribute("globalVariableList");
	Map<String,String> dataConfigMap = (Map<String,String>)request.getAttribute("dataSourceConfList");
	Map<String,String> webConfigMap = (Map<String,String>)request.getAttribute("webConfList");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/common/common.jsp" %>
<title></title>
	
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
<style>
#paramValues table {
	width : <%=StaticValue.ZH_HK.equals(empLangName)?140:100 %>%;
	/*background-color: #EFEFEF;*/
}
</style>
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style>
		/* 避免英文状态下，单词被换行*/
			.content tbody td {
				word-break:break-word;
			}
		</style>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/configManage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
</head>
	<body id="config_manage">
    <div id="container">
    	<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
    	<div class="rContent container_down_div" id="paramValues" >
			<div>
				<table class="paramValues_table">
					<tr>
						<td colspan="3" class="div_bg">
							<div id="corpConfigDiv" class="generalParamDiv paramDiv_set"><b>机构配置参数</b>
								<a id="generalParamIcon"  class="unfold <%="zh_HK".equals(empLangName)?"generalParamIcon1":"generalParamIcon2"%>"  ></a>
							</div>
						</td>
					</tr>

					<tr>
						<td  colspan="3">
							<div id="corpConfigParam" >
								<table id="content" class="tableCss">
									<tbody>
									<%
										if (corpConfVoList != null && corpConfVoList.size() > 0) {
											int paramType;
											for (ParamConfVo param : corpConfVoList) {
												paramType = param.getDataType();
									%>
									<tr>
										<td class="tdtxt"><%=param.getParamName()%></td>
										<td class="tdinput"><%
											if ( paramType == 1){
										%><select class="valueinput"
												  id="<%=param.getParamKey()%>"
												  datatype="<%=paramType%>"
												  paramname="<%=param.getParamName()%>"
												  valuekey="<%=param.getParamKey()%>"
										>
												<option value="0" <%="0".equals(param.getParamValue())?"selected":"" %>>是</option>
												<option value="1" <%="1".equals(param.getParamValue())?"selected":"" %>>否</option>
											</select>
											<%
											}else
											{%><input class="valueinput"
													  olddata="<%=param.getParamValue()%>"
													  datatype="<%=paramType%>"
													  paramname="<%=param.getParamName()%>"
													  valuekey="<%=param.getParamKey()%>"
													  value="<%=param.getParamValue() == null?"":param.getParamValue()%>"
													  onkeyup="checkParamNum($(this))"
											/><%
											}
										%>
										</td>
										<td><%=param.getParamMemo()%></td>
									</tr>
									<%
												}}
									%>
									<tr class="paramDiv_up_tr">
										<td  colspan="3" class="td-no-border"></td>
									</tr>

									<tr class="paramDiv_up_tr">
										<td  colspan="3" class="td-no-border">
											<center><input type="button" onclick="saveConfig(0)" value="保存" class="btnClass4"/>
											</center>
										</td>
									</tr>
									</tbody>
								</table>
							</div>
						</td>
					</tr>
					<tr class="paramDiv_up_tr">
							<td  colspan="3"></td>
						</tr>
				</table>
		    </div>

			<div>
				<table class="paramValues_table">
						<tr>
							<td colspan="3" class="div_bg">
								<div id="sysConfigDiv" class="generalParamDiv paramDiv_set"><b>应用配置参数</b>
									<a id="sysParamIcon"  class="unfold <%="zh_HK".equals(empLangName)?"generalParamIcon1":"generalParamIcon2"%>"  >&nbsp;&nbsp;&nbsp;&nbsp;</a>
								</div>
							</td>
						</tr>
						<tr>
							<td  colspan="3">
								<div id="sysConfigParam" >
								<table id="content" class="tableCss">
									<tbody>
									<%
										if (sysParamList != null && sysParamList.size() > 0) {
											int paramType;
											String value;
											for (ParamConfVo param : sysParamList) {
												paramType = param.getDataType();
												value = param.getParamValue();
												// 下行记录导出页面数据显示要以万为单位
												if("cxtjMtExportLimit".equals(param.getParamKey())){
													value = String.valueOf(Integer.parseInt(value) / 10000);
												}
									%>
									<tr>
										<td class="tdtxt"><%=param.getParamName()%></td>
										<td class="tdinput"><%
											if ( paramType == 1){
										%><select class="valueinput"
												  id="<%=param.getParamKey()%>"
												  datatype="<%=paramType%>"
												  paramname="<%=param.getParamName()%>"
												  valuekey="<%=param.getParamKey()%>"
										>  <%
											if ("loginYanZhengMa".equals(param.getParamKey())){
												%>
											<option value="true" <%="true".equals(param.getParamValue())?"selected":"" %>>是</option>
											<option value="false" <%="false".equals(param.getParamValue())?"selected":"" %>>否</option>
											<%
												// 系统配置参数中只有SPUSER_ISLWR该参数是0代表否，1代表是
											}else if("SPUSER_ISLWR".equals(param.getParamKey())){
											%>
											<option value="0" <%="0".equals(param.getParamValue())?"selected":"" %>>否</option>
											<option value="1" <%="1".equals(param.getParamValue())?"selected":"" %>>是</option>
											<%
											}else{
											%>
											<option value="0" <%="0".equals(param.getParamValue())?"selected":"" %>>是</option>
											<option value="1" <%="1".equals(param.getParamValue())?"selected":"" %>>否</option>
											<%}%>
										</select>
											<%
											}else
											{%><input class="valueinput"
													  datatype="<%=paramType%>"
													  paramname="<%=param.getParamName()%>"
													  valuekey="<%=param.getParamKey()%>"
													  value="<%=value == null?"":value%>"
													  onkeyup="checkParamNum($(this))"
											/><%
												}
											%>
										</td>
										<td><%=param.getParamMemo()%></td>
									</tr>
									<%
												}}
									%>
									<tr class="paramDiv_up_tr">
										<td  colspan="3" class="td-no-border"></td>
									</tr>

									<tr class="paramDiv_up_tr">
										<td  colspan="3" class="td-no-border">
											<center><input type="button" onclick="saveConfig(1)" value="保存" class="btnClass4"/></center>
										</td>
									</tr>
									</tbody>
								</table>
								</div>
							</td>
						</tr>
						<tr class="paramDiv_up_tr">
							<td  colspan="3"></td>
						</tr>
				</table>
			</div>

			<div>
					<table class="paramValues_table">
						<tr>
							<td colspan="3" class="div_bg">
								<div id="globalConfigDiv" class="generalParamDiv paramDiv_set"><b>系统全局配置参数</b>
									<a id="globalParamIcon"  class="unfold <%="zh_HK".equals(empLangName)?"generalParamIcon1":"generalParamIcon2"%>"  >&nbsp;&nbsp;&nbsp;&nbsp;</a>
								</div>
							</td>
						</tr>
						<tr><td  colspan="3">
							<div id="globalConfigParam" >
								<table id="content" class="tableCss">
									<tbody>
									<%
										if (globalVariableList != null && globalVariableList.size() > 0) {
											int paramType;
											String paramValue;
											String key;
											for (ParamConfVo param : globalVariableList) {
												paramType = param.getDataType();
												paramValue = param.getParamValue();
												key = param.getParamKey();
												// HTTP请求超时时间、HTTP响应超时时间、运营商余额请求间隔、日志写文件时间间隔 页面数据展示以秒为单位
												if ("HTTP_REQUEST_TIMEOUT".equals(key) || "HTTP_RESPONSE_TIMEOUT".equals(key)
														|| "BALANCE_REQ_INTERVAL".equals(key) || "LOG_PRINT_INTERVAL".equals(key)) {
													paramValue = String.valueOf(Integer.parseInt(paramValue) / 1000);
												}
												//黑名单支持最大数量页面数据展示以万为单位
												if("BLACK_MAXCOUNT".equals(key)){
													paramValue = String.valueOf(Integer.parseInt(paramValue) / 10000);
												}
									%>
									<tr>
										<td class="tdtxt"><%=param.getParamName()%></td>
										<td class="tdinput"><%
											if ( paramType == 1){
										%><select class="valueinput"
												  id="<%=param.getParamKey()%>"
												  datatype="<%=paramType%>"
												  paramname="<%=param.getParamName()%>"
												  valuekey="<%=param.getParamKey()%>"
										>
											<option value="0" <%="0".equals(paramValue)?"selected":"" %>>是</option>
											<option value="1" <%="1".equals(paramValue)?"selected":"" %>>否</option>
										</select>
											<%
											}else
											{%><input class="valueinput"
													  <%if ("BLACK_MAXCOUNT".equals(param.getParamKey())){%> olddata = "<%=paramValue%>" <%}%>
													  datatype="<%=paramType%>"
													  paramname="<%=param.getParamName()%>"
													  valuekey="<%=param.getParamKey()%>"
													  value="<%=paramValue == null?"":paramValue%>"
													  onkeyup="checkParamNum($(this))"
											/><%
												}
											%>
										</td>
										<td><%=param.getParamMemo()%></td>
									</tr>
									<%
											}}
									%>
									<tr class="paramDiv_up_tr">
										<td  colspan="3" class="td-no-border"></td>
									</tr>

									<tr class="paramDiv_up_tr">
										<td  colspan="3" class="td-no-border">
											<center><input type="button" onclick="saveConfig(2)" value="保存" class="btnClass4"/></center>
										</td>
									</tr>
									</tbody>
								</table>
							</div>
						</td>
						</tr>
						<tr class="paramDiv_up_tr">
							<td  colspan="3"></td>
						</tr>
					</table>
			</div>

			<div >
				<table class="paramValues_table">
					<tr>
						<td colspan="3" class="div_bg">
							<div id="dataConfigDiv" class="generalParamDiv paramDiv_set"><b>数据库配置参数</b>
								<a id="dataParamIcon"  class="unfold <%="zh_HK".equals(empLangName)?"generalParamIcon1":"generalParamIcon2"%>"  >&nbsp;&nbsp;&nbsp;&nbsp;</a>
							</div>
						</td>
					</tr>
					<tr><td  colspan="3">
						<div id="dataConfigParam" >
							<table id="content" class="tableCss fixconfig">
								<tbody>
								<tr>
									<td>数据库类型</td>
									<%
										String type = dataConfigMap.get("DBType");
										String typeStr="";
										if ("1".equals(type)){
											typeStr = "Oracle";
										}
										if ("2".equals(type)){
											typeStr = "SQL SERVER";
										}
										if ("3".equals(type)){
											typeStr = "MYSQL";
										}
										if ("4".equals(type)){
											typeStr = "DB2";
										}
									%>
									<td><%=typeStr%></td>
								</tr>
								<tr>
									<td>数据库连接池</td>
									<%
										String typePool = dataConfigMap.get("poolType");
										String typePoolStr="";
										if ("1".equals(typePool)){
											typePoolStr = "C3P0";
										}
										if ("2".equals(typePool)){
											typePoolStr = "BONECP";
										}
										if ("3".equals(typePool)){
											typePoolStr = "DBCP";
										}
									%>
									<td><%=typePoolStr%></td>
								</tr>
								<%if ("1".equals(type)){
									%>
								<tr>
									<td>连接类型</td>
									<%
										String typeConn = dataConfigMap.get("connType");
										String typeConnStr="";
										if ("1".equals(typeConn)){
											typeConnStr = "SID";
										}
										if ("2".equals(typePool)){
											typeConnStr = "Service Name";
										}
									%>
									<td><%=typeConnStr%></td>
								</tr>
								<%
								}%>
								<tr>
									<td>数据库地址</td>
									<td><%=dataConfigMap.get("databaseIp")%></td>
								</tr>
								<tr>
									<td>数据库端口</td>
									<td><%=dataConfigMap.get("databasePort")%></td>
								</tr>
								<tr>
									<td>数据库名称</td>
									<td><%=dataConfigMap.get("databaseName")%></td>
								</tr>
								<tr>
									<td>数据库用户名</td>
									<td><%=dataConfigMap.get("user")%></td>
								</tr>
								<% if ("0".equals(dataConfigMap.get("use_backup_server"))){
									%>
								<tr>
									<td>备用数据库开关</td>
									<td>关闭</td>
								</tr><%
								}else {
								%>
								<tr>
									<td>备用数据库开关</td>
									<td>开启</td>
								</tr>
								<tr>
									<td>备用数据库地址</td>
									<td><%=dataConfigMap.get("databaseIp2")%></td>
								</tr>
								<tr>
									<td>备用数据库端口</td>
									<td><%=dataConfigMap.get("databasePort2")%></td>
								</tr>
								<tr>
									<td>备用数据库名称</td>
									<td><%=dataConfigMap.get("databaseName2")%></td>
								</tr>
								<tr>
									<td>备用数据库用户名</td>
									<td><%=dataConfigMap.get("user2")%></td>
								</tr>
								<%}%>
								<tr>
									<td>数据连接池最大连接数</td>
									<td ><%=dataConfigMap.get("maxPoolSize")%></td>
								</tr>
								<tr>
									<td>数据连接池最小连接数</td>
									<td><%=dataConfigMap.get("minPoolSize")%></td>
								</tr>
								<tr>
									<td>数据连接池初始连接数</td>
									<td><%=dataConfigMap.get("InitialPoolSize")%></td>
								</tr>
								</tbody>
							</table>
						</div>
					</td>
					</tr>
					<tr class="paramDiv_up_tr">
						<td  colspan="3"></td>
					</tr>
				</table>
			</div>

			<div >
				<table class="paramValues_table">
					<tr>
						<td colspan="3" class="div_bg">
							<div id="webConfigDiv" class="generalParamDiv paramDiv_set"><b>WEB配置参数</b>
								<a id="webParamIcon"  class="unfold <%="zh_HK".equals(empLangName)?"generalParamIcon1":"generalParamIcon2"%>"  >&nbsp;&nbsp;&nbsp;&nbsp;</a>
							</div>
						</td>
					</tr>
					<tr><td  colspan="3">
						<div id="webConfigParam" >
							<table id="content" class="tableCss fixconfig">
								<tbody>
								<%
									String isCluster = webConfigMap.get("isCluster");
									if ("0".equals(isCluster)){
										%>
								<tr>
									<td>集群开关</td>
									<td>关闭</td>
								</tr><%
									}else {
								%>
								<tr>
									<td>集群开关</td>
									<td>开启</td>
								</tr>
								<tr>
									<td>集群节点</td>
									<td><%=webConfigMap.get("serverNumber")%></td>
								</tr>
								<%}%>
								<tr>
									<td>文件内网地址</td>
									<td><%=webConfigMap.get("innerUrl")%></td>
								</tr>
								<tr>
									<td>文件外网地址</td>
									<td><%=webConfigMap.get("outerUrl")%></td>
								</tr>
								<% if ("1".equals(isCluster)){
									%>
								<tr>
									<td>备用内网地址</td>
									<td><%=webConfigMap.get("bakinnerUrl")%></td>
								</tr>
								<tr>
									<td>备用外网地址</td>
									<td><%=webConfigMap.get("bakouterUrl")%></td>
								</tr>
								<%}%>
								<tr>
									<td>EMP访问地址</td>
									<td><%=webConfigMap.get("EMPaddress")%></td>
								</tr>
								<tr>
									<td>EMP外网访问地址</td>
									<td><%=webConfigMap.get("EMPOuterAddress")%></td>
								</tr>
								<tr>
									<td>网关通讯地址</td>
									<td><%=webConfigMap.get("webgate")%></td>
								</tr>
								<tr>
									<td>网讯站点访问地址</td>
									<td><%=webConfigMap.get("EMPwxaddress")%></td>
								</tr>
								<tr>
									<td>EMP日志文件存储地址</td>
									<td><%=webConfigMap.get("loggeraddress")%></td>
								</tr>
								<tr>
									<td>默认页面大小</td>
									<td><%=webConfigMap.get("defaultPageSize")%></td>
								</tr>

								<tr>
									<td>肤色版本</td>
									<td><%=webConfigMap.get("frame")%></td>
								</tr>
								<%
									boolean lang;
									String langStr="简体";
									if (webConfigMap.get("multiLanguageEnable")=="No"){
										lang = false;
									}else {
										lang = true;
										if ("zh_CN".equals(webConfigMap.get("selectedLanguage"))){
											langStr = "简体";
										}
										if ("zh_TW".equals(webConfigMap.get("selectedLanguage"))){
											langStr = "繁体";
										}
										if ("zh_HK".equals(webConfigMap.get("selectedLanguage"))){
											langStr = "英语";
										}
									}
								%>
								<tr>
									<td>启用多语言</td>
									<td><%=lang == false? "否" : "是"%></td>
								</tr>
								<tr>
									<td>启用分批设置</td>
									<td><%="0".equals(webConfigMap.get("smsSplit"))?"否":"是"%></td>
								</tr>
								<%
									if (lang){%>
								<tr>
									<td>语言</td>
									<td><%=langStr%></td>
								</tr>
								<%
									}
								%>
								</tbody>
							</table>
						</div>
					</td>
					</tr>
					<tr class="paramDiv_up_tr">
						<td  colspan="3"></td>
					</tr>
				</table>
			</div>

    	</div>
    	<div class="bottom"><div id="bottom_right"><div id="bottom_left"></div></div></div>
	</div><%--end round_content--%>
	<script language="javascript" type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" type="text/javascript" src="<%=iPath%>/js/configManage.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	
	<script>
		var times= <%=System.currentTimeMillis()%>;
		String.prototype.replaceAll  = function(s1,s2){    
		    return this.replace(new RegExp(s1,"gm"),s2); 
		 }
		$(document).ready(function(){
			var findresult="<%=(String)request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
		       alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_1"));	
		       return;			       
		    }
			$(".tableCss tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});

		    // 企业参数
			$("#corpConfigParam").show();
			$("#corpConfigDiv").toggle(function() {
				$("#corpConfigParam").hide();
				$('#generalParamIcon').addClass("fold");
				$('#generalParamIcon').removeClass("unfold");
			}, function() {
				$("#corpConfigParam").show();
				$('#generalParamIcon').addClass("unfold");
				$('#generalParamIcon').removeClass("fold");
			});
			// 系统参数
			$("#sysConfigParam").show();
			$("#sysConfigDiv").toggle(function() {
				$("#sysConfigParam").hide();
				$('#sysParamIcon').addClass("fold");
				$('#sysParamIcon').removeClass("unfold");
			}, function() {
				$("#sysConfigParam").show();
				$('#sysParamIcon').addClass("unfold");
				$('#sysParamIcon').removeClass("fold");
			});

			// 全局参数
			$("#globalConfigParam").show();
			$("#globalConfigDiv").toggle(function() {
				$("#globalConfigParam").hide();
				$('#globalParamIcon').addClass("fold");
				$('#globalParamIcon').removeClass("unfold");
			}, function() {
				$("#globalConfigParam").show();
				$('#globalParamIcon').addClass("unfold");
				$('#globalParamIcon').removeClass("fold");
			});

			// 配置文件file
			$("#fileConfigParam").show();
			$("#fileConfigDiv").toggle(function() {
				$("#fileConfigParam").hide();
				$('#fileParamIcon').addClass("fold");
				$('#fileParamIcon').removeClass("unfold");
			}, function() {
				$("#fileConfigParam").show();
				$('#fileParamIcon').addClass("unfold");
				$('#fileParamIcon').removeClass("fold");
			});

			// 数据库参数
			$("#dataConfigParam").show();
			$("#dataConfigDiv").toggle(function() {
				$("#dataConfigParam").hide();
				$('#dataParamIcon').addClass("fold");
				$('#dataParamIcon').removeClass("unfold");
			}, function() {
				$("#dataConfigParam").show();
				$('#dataParamIcon').addClass("unfold");
				$('#dataParamIcon').removeClass("fold");
			});

			// web参数
			$("#webConfigParam").show();
			$("#webConfigDiv").toggle(function() {
				$("#webConfigParam").hide();
				$('#webParamIcon').addClass("fold");
				$('#webParamIcon').removeClass("unfold");
			}, function() {
				$("#webConfigParam").show();
				$('#webParamIcon').addClass("unfold");
				$('#webParamIcon').removeClass("fold");
			});
		});

	</script>
</body>
</html>
