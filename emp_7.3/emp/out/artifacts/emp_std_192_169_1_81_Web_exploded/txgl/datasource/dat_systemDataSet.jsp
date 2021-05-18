<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>
<%@ page import="com.montnets.emp.common.constant.SystemGlobals" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String DBType=request.getAttribute("DBType")==null?"":request.getAttribute("DBType").toString();
	String poolType=request.getAttribute("poolType")==null?"":request.getAttribute("poolType").toString();
	
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	//连接类型  对Oracle数据库有用
	String connType=request.getAttribute("connType")==null?"":request.getAttribute("connType").toString();
	String isCluster=request.getAttribute("isCluster")==null?"":request.getAttribute("isCluster").toString();
	String serverNumber=request.getAttribute("serverNumber")==null?"":request.getAttribute("serverNumber").toString();
	
	String bakinnerUrl=request.getAttribute("bakinnerUrl")==null?"":request.getAttribute("bakinnerUrl").toString();
	String bakouterUrl=request.getAttribute("bakouterUrl")==null?"":request.getAttribute("bakouterUrl").toString();
	String use_backup_server=request.getAttribute("use_backup_server")==null?"":request.getAttribute("use_backup_server").toString();
	String multiLanguageEnable = request.getAttribute("multiLanguageEnable")==null?"":request.getAttribute("multiLanguageEnable").toString();
	String[] selectedLanguage = request.getAttribute("selectedLanguage")==null?new String[]{}:request.getAttribute("selectedLanguage").toString().split(",");
	String defaultLanguage = request.getAttribute("defaultLanguage")==null?"":request.getAttribute("defaultLanguage").toString();
	String smsSplit = request.getAttribute("smsSplit")==null?"":request.getAttribute("smsSplit").toString();

	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

%>
<!doctype html>
<html lang="en">
<head >
<%@include file="/common/common.jsp" %>
	<meta charset="UTF-8">
	<title>欢迎使用梦网科技EMP移动信息平台</title>
	<link rel="stylesheet" href="<%=iPath %>/css/install.css?V=<%=StaticValue.getJspImpVersion() %>">
	
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/dat_systemDataSet.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dat_systemDataSet.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
</head>
<body id="dat_systemDataSet" onload="">
<input type="hidden" id="pathUrl" value="<%=path %>"/>
	<div class="header">
		<div class="inner">
			<h2 class="logo"></h2>
			<a href="javascript:void(0);" class="edit_pwd"><emp:message key='txgl_datasource_text_1' defVal='修改密码' fileName='mwadmin'/></a>
			<%if("Yes".equals(multiLanguageEnable)  && selectedLanguage != null && selectedLanguage.length > 1 ){%>
			<a href="javascript:void(0);" class="switchLang">
				<select id="switchLang" onchange="javascript:switchLang();">
					<%
						for(String str : selectedLanguage){
							String langname = "zh_HK".equals(str)?"English":"zh_TW".equals(str)?"繁體":"简体";
					%>
					<option value="<%=str%>"><%=langname%></option>
					<%}%>
				</select>
			</a>
			<%}%>
			<a href="javascript:logout();" class="loginOut"><emp:message key='txgl_datasource_text_2' defVal='退出' fileName='mwadmin'/></a>
		</div>
		
	</div>
	<div class="mod_top radius">
		<h2><emp:message key='txgl_datasource_text_3' defVal='EMP参数配置' fileName='mwadmin'/></h2>
	</div>
	<div class="wrapper">
	<form id="form" name="form" action="<%=path %>/systemManage.htm?method=update" method="post">
		<%--数据库配置start--%>
		<div class="db_config">
			<div class="cg_tags radius">
					<b>1</b>
			</div>
			<div class="config_detail radius">
				<h2 class="c1"><emp:message key='txgl_datasource_text_4' defVal='数据库配置' fileName='mwadmin'/></h2>
				<div class="inner">
					<table class="cg_tab">
						<tbody>
							<tr>
								<td class="sjklx_td"><emp:message key='txgl_datasource_text_5' defVal='数据库类型：' fileName='mwadmin'/></td>
								<td>
								<select name="DBType" id="DBType" class="inpSel"  onchange="changeDbType(this.value)">
								<option value="1" <%if("1".equals(DBType)){ %>selected="selected"<%} %> >Oracle</option>
								<option value="2" <%if("2".equals(DBType)){ %>selected="selected"<%} %> >SQL Server</option>
								<option value="3" <%if("3".equals(DBType)){ %>selected="selected"<%} %> >MySQL</option>
								<option value="4" <%if("4".equals(DBType)){ %>selected="selected"<%} %> >DB2</option>
								</select>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_6' defVal='数据库连接池：' fileName='mwadmin'/></td>
								<td>
									<input type="radio" name="dataPooltype" value="1" <%if("1".equals(poolType)){ %>  checked <%} %> class="inpra" id="r1">
									<label for="r1" class="lab">1.C3P0</label>
									<input type="radio" name="dataPooltype"value="2" <%if("2".equals(poolType)){ %>  checked <%} %> class="inpra" id="r2">
									<label for="r2" class="lab">2.BONECP</label>
									<input type="radio" name="dataPooltype"  value="3" <%if("3".equals(poolType)){ %>  checked <%} %> class="inpra" id="r3">
									<label for="r3" class="lab">3.DBCP</label>
<%--									<input type="radio" name="dataPooltype"  value="4" <%//if("4".equals(poolType)){ %>  checked <%//} %> class="inpra" id="r4">--%>
<%--									<label for="r4" class="lab">4.JDBC-POOL</label>--%>
								</td>
								<%-- <td>SQL Server数据库（当SQL Server 2005在Windows 2008组合环境上使用时选择类型3）其他SQL Server组合环境请务必选择类型2；其余数据库类型选择类型1。</td> --%>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_7' defVal='连接类型：' fileName='mwadmin'/></td>
								<td>
									<select id="connType" name="connType" class="inpSel">
									    <option value="1" <%if("1".equals(connType)){ %>selected="selected"<%} %> >SID</option>
										<option value="0" <%if("0".equals(connType)){ %>selected="selected"<%} %> >Service Name</option>
									</select>
								</td>
							</tr>
							<input type="hidden" id="type" value="">
							<tr>
								<td><emp:message key='txgl_datasource_text_8' defVal='数据库地址：' fileName='mwadmin'/></td>
								<td><input type="text" name="dbconIp" id="dbconIp" value="<%=request.getAttribute("databaseIp")==null?"":request.getAttribute("databaseIp")%>" class="inpStyle" placeholder="<emp:message key='txgl_datasource_text_13' defVal='·请输入IP地址' fileName='mwadmin'/>"></td>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_9' defVal='数据库端口号：' fileName='mwadmin'/></td>
								<td><input type="text" name="port" id="port" value="<%=request.getAttribute("databasePort")==null?"":request.getAttribute("databasePort")%>" class="inpStyle" placeholder="<emp:message key='txgl_datasource_text_14' defVal='·请输入端口号' fileName='mwadmin'/>"></td>
							</tr>
							<tr>
								<td id="tdDbName"><span><emp:message key='txgl_datasource_text_71' defVal='数据库名称：' fileName='mwadmin'/></span></td>
								<td><input type="text" name="dbName" id="dbName" value="<%=request.getAttribute("databaseName")==null?"":request.getAttribute("databaseName")%>" class="inpStyle" placeholder="<emp:message key='txgl_datasource_text_15' defVal='·请输入名称' fileName='mwadmin'/>"></td>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_11' defVal='数据库用户名：' fileName='mwadmin'/></td>
								<td><input type="text" name="dbUser" id="dbUser" value="<%=request.getAttribute("user")==null?"":request.getAttribute("user")%>" class="inpStyle" placeholder="<emp:message key='txgl_datasource_text_16' defVal='·请输入用户名' fileName='mwadmin'/>"></td>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_12' defVal='数据库密码：' fileName='mwadmin'/></td>
								<td><input type="password" name="dbPwd"  id="dbPwd" value="<%=request.getAttribute("password")==null?"":request.getAttribute("password")%>" class="inpStyle" placeholder="<emp:message key='txgl_datasource_text_17' defVal='·请输入密码' fileName='mwadmin'/>"></td>
							</tr>
							
						</tbody>
					</table>
					<div class="cg_btn">
						<a href="javascript:testConnection()" class="btn01" id="testDBConnBtn" ><emp:message key='txgl_datasource_text_18' defVal='测试连接' fileName='mwadmin'/></a>
						<span id="waitTextConnection" class=""waitTextConnection""></span>
						<span id="rightTextConnection" class="rightTextConnection"></span>
						<span id="errorTextConnection" class="errorTextConnection"></span>
						<%--  <span id="success" style="display:none;" class="tips_success">连接成功</span>
						<span id="error" style="display:none;" class="tips_error">连接失败</span>
						--%>
					</div>
					<table class="cg_tab sjkkg_table"  >
							<tr>
								<td class="sjkkg_td"><emp:message key='txgl_datasource_text_19' defVal='备用数据库开关：' fileName='mwadmin'/></td>
								<td >
									<select name="use_backup_server" id="use_backup_server" class="inpSel use_backup_server"   onchange="isOPen(this.value)">
										<option value="0" <%if("0".equals(use_backup_server)){ %>selected="selected"<%} %> ><emp:message key='txgl_datasource_text_20' defVal='关闭' fileName='mwadmin'/></option>
										<option value="1" <%if("1".equals(use_backup_server)){ %>selected="selected"<%} %> ><emp:message key='txgl_datasource_text_21' defVal='开启' fileName='mwadmin'/></option>
									</select>
								</td>
								<td class="tips01"></td>
							</tr>
							</table>
						<table id="backConn" class="cg_tab backConn"  >
								<tr>
								<td class="sjkdz_td"><emp:message key='txgl_datasource_text_22' defVal='备用数据库地址：' fileName='mwadmin'/></td>
								<td><input type="text" name="dbconIp2" id="dbconIp2" value="<%=request.getAttribute("databaseIp2")==null?"":request.getAttribute("databaseIp2")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_13' defVal='·请输入IP地址' fileName='mwadmin'/>"></td>
								<td class="tips01"></td>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_23' defVal='备用数据库端口号：' fileName='mwadmin'/></td>
								<td><input type="text" name="port2" id="port2" value="<%=request.getAttribute("databasePort2")==null?"":request.getAttribute("databasePort2")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_14' defVal='·请输入端口号' fileName='mwadmin'/>"></td>
								<td class="tips01"></td>
							</tr>
							<tr>
								<td id="tdDbName2"><span><emp:message key='txgl_datasource_text_72' defVal='备用数据库名称：' fileName='mwadmin'/></span></td>
								<td><input type="text" name="dbName2" id="dbName2" value="<%=request.getAttribute("databaseName2")==null?"":request.getAttribute("databaseName2")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_15' defVal='·请输入名称' fileName='mwadmin'/>"></td>
								<td class="tips01"></td>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_25' defVal='备用数据库用户名：' fileName='mwadmin'/></td>
								<td><input type="text" name="dbUser2" id="dbUser2" value="<%=request.getAttribute("user2")==null?"":request.getAttribute("user2")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_16' defVal='·请输入用户名' fileName='mwadmin'/>"></td>
								<td class="tips01"></td>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_26' defVal='备用数据库密码：' fileName='mwadmin'/></td>
								<td><input type="password" name="dbPwd2"  id="dbPwd2" value="<%=request.getAttribute("password2")==null?"":request.getAttribute("password2")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_17' defVal='·请输入密码' fileName='mwadmin'/>"></td>
								<td class="tips01"></td>
							</tr>
							</table>
					
					<div class="cg_btn backTest"  id="backTest">
						<a href="javascript:testBackConnection()" class="btn01" id="testBackConnBtn" ><emp:message key='txgl_datasource_text_27' defVal='测试连接' fileName='mwadmin'/></a>
						<span id="waitTextConnection2" class="waitTextConnection2"></span>
						<span id="rightTextConnection2" class="rightTextConnection2"></span>
						<span id="errorTextConnection2" class="errorTextConnection2"></span>
						<%--  <span id="success" style="display:none;" class="tips_success">连接成功</span>
						<span id="error" style="display:none;" class="tips_error">连接失败</span>
						--%>
					</div>
					<span class="cg_more" id="db_cg_event"><emp:message key='txgl_datasource_text_28' defVal='更多参数配置' fileName='mwadmin'/></span>

				</div>
				
			</div>
		</div>
		
		<%--数据库配置end--%>
		<%--web信息start--%>
		<div class="db_config">
			<div class="cg_tags radius">
					<b>2</b>
			</div>
			<div class="config_detail radius">
				<h2 class="c1"><emp:message key='txgl_datasource_text_29' defVal='WEB信息' fileName='mwadmin'/></h2>
				<div class="inner">
					<table class="cg_tab">
						<tbody>
							<tr>
								<td id="clusterSwitch"><emp:message key='txgl_datasource_text_30' defVal='集群开关：' fileName='mwadmin'/></td>
								<td >
								<select name="isCluster" id="isCluster" class="inpSel isCluster"  onchange="changeType(this.value)">
								<option value="0" <%if("0".equals(isCluster)){ %>selected="selected"<%} %> ><emp:message key='txgl_datasource_text_20' defVal='关闭' fileName='mwadmin'/></option>
								<option value="1" <%if("1".equals(isCluster)){ %>selected="selected"<%} %>  ><emp:message key='txgl_datasource_text_21' defVal='开启' fileName='mwadmin'/></option>
								</select>
								</td>
								<td class="tips01"></td>
							</tr>
							<tr id="servershow" >
								<td colspan="3">
							<table id="serverNum" class="serverNum">
							<tr>
								<td id="server1"><emp:message key='txgl_datasource_text_31' defVal='集群节点：' fileName='mwadmin'/></td>
								<td  id="server2">
									<select name="serverNumber" id="serverNumber" class="inpSel serverNumber"  >
										<option value="1001" <%if("1001".equals(serverNumber)){ %>selected="selected"<%} %>>1001</option>
										<option value="1002" <%if("1002".equals(serverNumber)){ %>selected="selected"<%} %>>1002</option>
										<option value="1003" <%if("1003".equals(serverNumber)){ %>selected="selected"<%} %>>1003</option>
										<option value="1004" <%if("1004".equals(serverNumber)){ %>selected="selected"<%} %>>1004</option>
										<option value="1005" <%if("1005".equals(serverNumber)){ %>selected="selected"<%} %>>1005</option>
										<option value="1006" <%if("1006".equals(serverNumber)){ %>selected="selected"<%} %>>1006</option>
										<option value="1007" <%if("1007".equals(serverNumber)){ %>selected="selected"<%} %>>1007</option>
										<option value="1008" <%if("1008".equals(serverNumber)){ %>selected="selected"<%} %>>1008</option>
										<option value="1009" <%if("1009".equals(serverNumber)){ %>selected="selected"<%} %>>1009</option>
										<option value="1010" <%if("1010".equals(serverNumber)){ %>selected="selected"<%} %>>1010</option>
										<option value="1011" <%if("1011".equals(serverNumber)){ %>selected="selected"<%} %>>1011</option>
										<option value="1012" <%if("1012".equals(serverNumber)){ %>selected="selected"<%} %>>1012</option>
										<option value="1013" <%if("1013".equals(serverNumber)){ %>selected="selected"<%} %>>1013</option>
										<option value="1014" <%if("1014".equals(serverNumber)){ %>selected="selected"<%} %>>1014</option>
										<option value="1015" <%if("1015".equals(serverNumber)){ %>selected="selected"<%} %>>1015</option>
										<option value="1016" <%if("1016".equals(serverNumber)){ %>selected="selected"<%} %>>1016</option>
										<option value="1017" <%if("1017".equals(serverNumber)){ %>selected="selected"<%} %>>1017</option>
										<option value="1018" <%if("1018".equals(serverNumber)){ %>selected="selected"<%} %>>1018</option>
										<option value="1019" <%if("1019".equals(serverNumber)){ %>selected="selected"<%} %>>1019</option>
										<option value="1020" <%if("1020".equals(serverNumber)){ %>selected="selected"<%} %>>1020</option>
									</select>
								</td>
								<td class="tips01"></td>
							</tr>
							</table>
							</td>
							</tr>	
							<tr>
								<td><emp:message key='txgl_datasource_text_32' defVal='文件内网地址：' fileName='mwadmin'/></td>
								<td><input type="text" name="innerUrl" id="innerUrl" value="<%=request.getAttribute("innerUrl")==null?"":request.getAttribute("innerUrl")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_33' defVal='·请输入内网地址' fileName='mwadmin'/>"></td>
								<td class="tips01"><emp:message key='txgl_datasource_text_34' defVal='例：http://192.168.2.234:8610/emp/(注:末尾"/"要加上' fileName='mwadmin'/>)</td>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_35' defVal='文件外网地址：' fileName='mwadmin'/></td>
								<td><input type="text" name="outerUrl" id="outerUrl" value="<%=request.getAttribute("outerUrl")==null?"":request.getAttribute("outerUrl")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_36' defVal='·请输入外网地址' fileName='mwadmin'/>"></td>
								<td class="tips01"><emp:message key='txgl_datasource_text_34' defVal='例：http://121.15.130.218:8610/emp/(注:末尾"/"要加上)' fileName='mwadmin'/></td>
							</tr>

							
						<tr id="initCols_show">
							<td colspan="3">
								<table id="initCols" class="initCols">
									<tr id="listinner1">
										<td id="StandbyInterURL"><emp:message key='txgl_datasource_text_37' defVal='备用内网地址1：' fileName='mwadmin'/></td>
										<td><input type="text" name="bakinnerUrl1" id="bakinnerUrl1" value="" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_38' defVal='·备用内网地址1' fileName='mwadmin'/>"></td>
										<td class="tips01"></td>
									</tr>
									<tr id="listout1">
										<td id="StandbyOuterURL"><emp:message key='txgl_datasource_text_39' defVal='备用外网地址1：' fileName='mwadmin'/></td>
										<td><input type="text" name="bakouterUrl1" id="bakouterUrl1" value="" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_40' defVal='·备用外网地址1' fileName='mwadmin'/>"></td>
										<td><a onclick="javascript:addCols()" class="tjby_a"><emp:message key='txgl_datasource_text_41' defVal='添加备用' fileName='mwadmin'/></a>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td id="EMPAccessURL"><emp:message key='txgl_datasource_text_42' defVal='EMP访问地址：' fileName='mwadmin'/></td>
							<td ><input type="text" name="EMPaddress" id="EMPaddress" value="<%=request.getAttribute("EMPaddress")==null?"":request.getAttribute("EMPaddress")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_43' defVal='·请输入EMP访问地址' fileName='mwadmin'/>"></td>
							<td class="tips01"><emp:message key='txgl_datasource_text_34' defVal='例：http://192.168.2.234:8610/emp/(注:末尾"/"要加上)' fileName='mwadmin'/></td>
						</tr>
						<tr>
							<td id="EMPOuterAccessURL"><emp:message key='txgl_datasource_text_outer_42' defVal='EMP外网访问地址：' fileName='mwadmin'/></td>
							<td ><input type="text" name="EMPOuterAddress" id="EMPOuterAddress" value="<%=request.getAttribute("EMPOuterAddress")==null?"":request.getAttribute("EMPOuterAddress")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_outer_43' defVal='·请输入EMP外网访问地址' fileName='mwadmin'/>"></td>
							<td class="tips01"><emp:message key='txgl_datasource_text_34' defVal='例：http://192.168.2.234:8610/emp/(注:末尾"/"要加上)' fileName='mwadmin'/></td>
						</tr>
						<tr>
							<td><emp:message key='txgl_datasource_text_44' defVal='网关通讯地址：' fileName='mwadmin'/></td>
							<td><input type="text" name="webgate" id="webgate"  value="<%=request.getAttribute("webgate")==null?"":request.getAttribute("webgate")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_45' defVal='·请输入网关能讯地址' fileName='mwadmin'/>"></td>
							<td class="tips01"><emp:message key='txgl_datasource_text_50' defVal='例：' fileName='mwadmin'/>http://192.168.2.234:8610/sms/mt</td>
						</tr>
						<tr>
							<td><emp:message key='txgl_datasource_text_46' defVal='网讯站点访问地址：' fileName='mwadmin'/></td>
							<td><input type="text" name="EMPwxaddress" id="EMPwxaddress" value="<%=request.getAttribute("EMPwxaddress")==null?"":request.getAttribute("EMPwxaddress")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_47' defVal='·请输入网讯站点' fileName='mwadmin'/>"></td>
							<td class="tips01"><emp:message key='txgl_datasource_text_50' defVal='例：' fileName='mwadmin'/>http://121.15.130.218:8610/emp</td>
						</tr>
						<tr>
							<td><emp:message key='txgl_datasource_text_48' defVal='EMP日志文件存储地址：' fileName='mwadmin'/></td>
							<td><input type="text" name="loggeraddress" id="loggeraddress" value="<%=request.getAttribute("loggeraddress")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_49' defVal='·请输入日志文件存储地址' fileName='mwadmin'/>">
								<input type="hidden" name="defaultloggeraddress" id="defaultloggeraddress" value="<%=request.getAttribute("defaultloggeraddress")%>">
							</td>
							<td class="tips01"><emp:message key='txgl_datasource_text_50' defVal='例：' fileName='mwadmin'/>D:\Program Files\Tomcat 6.0\webapps\emp\</td>
						</tr>

						<tr>

						</tbody>
					</table>
					<span class="cg_more" id="web_cg_event"><emp:message key='txgl_datasource_text_51' defVal='更多参数配置' fileName='mwadmin'/></span>
					<div class="cg_btn">
						<a href="javascript:submit();" class="btn01"><emp:message key='txgl_datasource_text_52' defVal='完成' fileName='mwadmin'/></a>
						<font id="errorIP" color="red"></font>
					</div>
				</div>
				
			</div>
		</div>
		</form>
		<%--web信息end--%>
		<%--弹出框**修改密码--%>
		<div id="modfiy_pwd" class="modfiy_pwd">
			<form action="<%=path %>/systemManage.htm?method=modifPwd" id="form_pwd" method="post">
				<table class="tab_pwd">
					<tr>
						<td><emp:message key='txgl_datasource_text_53' defVal='旧密码：' fileName='mwadmin'/></td>
						<td><input type="password" name="oldPwd" id="oldPwd" class="inp02" onblur="checkPass(this.value)"></td>
					</tr>
					<tr>
						<td><emp:message key='txgl_datasource_text_54' defVal='新密码：' fileName='mwadmin'/></td>
						<td><input type="password" name="newPwd" id="newPwd" class="inp02"></td>
					</tr>
					<tr>
						<td><emp:message key='txgl_datasource_text_55' defVal='确认密码：' fileName='mwadmin'/></td>
						<td><input type="password" name="confirmPwd" id="confirmPwd" class="inp02"></td>
					</tr>
				</table>
			</form>
			<font id="errorPWD" color="red"></font>
			<font id="corrPWD" color="blue"></font>
		</div>


	</div>
	<%--弹出框**修改密码end--%>
	
	<%--弹出框**切换语言start--%>
	<%-- <div id="switchLang" style="display:none;">
	
		   	<!-- 语言切换 -->
	<div id="lang"  >
		<input type="radio" name="lang" value="zh_CN"/><emp:message  key="common_zh_cn" defVal="简体" fileName="common"/>
		<input type="radio" name="lang" value="zh_TW"/><emp:message  key="common_zh_tw" defVal="繁体" fileName="common"/>
		<input type="radio" name="lang" value="zh_HK"/><emp:message  key="common_zh_hk" defVal="English" fileName="common"/>
   	</div>
	</div> --%>
	<%--弹出框**切换语言end--%>
	<%--弹出框**数据库更多配置参数start--%>
	<div id="db_more_cg" class="cg_box">
		<form action="<%=path %>/systemManage.htm?method=dataSubmit" id="form_more_db" method="post">
					<table class="cg_tab">
						<tbody>
							<tr>
								<td id="more_db_param"><emp:message key='txgl_datasource_text_56' defVal='数据连接池最大连接数：' fileName='mwadmin'/></td>
								<td ><input type="text" name="maxPoolSize" id="maxPoolSize"  onkeyup="numberControl($(this))"  maxlength="5" value="<%=request.getAttribute("maxPoolSize")==null?"":request.getAttribute("maxPoolSize")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_67' defVal='·请输入连接池最大连接数' fileName='mwadmin'/>"></td>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_57' defVal='数据连接池最小连接数：' fileName='mwadmin'/></td>
								<td><input type="text" name="minPoolSize" id="minPoolSize" onkeyup="numberControl($(this))" maxlength="5" value="<%=request.getAttribute("minPoolSize")==null?"":request.getAttribute("minPoolSize") %>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_68' defVal='·请输入连接池最小连接数' fileName='mwadmin'/>"></td>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_58' defVal='数据连接池初始连接数：' fileName='mwadmin'/></td>
								<td><input type="text" name="InitialPoolSize" id="InitialPoolSize" onkeyup="numberControl($(this))" maxlength="5" value="<%=request.getAttribute("InitialPoolSize")==null?"":request.getAttribute("InitialPoolSize") %>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_69' defVal='·请输入连接池初始连接数' fileName='mwadmin'/>"></td>
							</tr>
							
						</tbody>
					</table>
			
		</form>
	</div>	
	<%--弹出框**WEB信息更多配置参数end--%>
	<div id="web_more_cg" class="cg_box">
		<form action="<%=path %>/systemManage.htm?method=webSubmit" id="form_more_web" method="post">
			<table class="cg_tab">
						<tbody>
							<tr>
								<td class="mrymdx_td"><emp:message key='txgl_datasource_text_59' defVal='默认页面大小：' fileName='mwadmin'/></td>
								<td ><input type="text" name="defaultPageSize" id="defaultPageSize" onkeyup="numberControl($(this))" maxlength="5"  value="<%=request.getAttribute("defaultPageSize")==null?"":request.getAttribute("defaultPageSize")%>" class="inpStyle w335" placeholder="<emp:message key='txgl_datasource_text_70' defVal='·请输入默认页面大小' fileName='mwadmin'/>"></td>
							</tr>
							<tr>
								<td><emp:message key='txgl_datasource_text_60' defVal='肤色版本：' fileName='mwadmin'/></td>
								<%  
									String frame=request.getAttribute("frame")==null?"":request.getAttribute("frame").toString();
								%>
								
								<td>
								<select name="frame" id="frame" class="inpSel">
								<%--
								<option value="frame2.5" <%if("frame2.5".equals(frame)){ %>selected="selected"<%} %> >frame2.5</option>
								--%>
								<option value="frame3.0" <%if("frame3.0".equals(frame)){ %>selected="selected"<%} %> >frame3.0</option>
								</select>
							
								</td>
							</tr>
							<tr>
								<td class="langEnable_td"><emp:message key='txgl_mwadmin_langEnable' defVal='启用多语言：' fileName='mwadmin'/></td>
								<td>
									<input type="radio" name="langSelect" value="Yes" <%="Yes".equals(multiLanguageEnable)?"checked":""%>>&nbsp;
									<emp:message key='txgl_wgqdpz_zhtdpz_s' defVal='是' fileName='txgl'/>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="langSelect" value="No" <%="No".equals(multiLanguageEnable)?"checked":""%>>&nbsp;
									<emp:message key='txgl_wgqdpz_zhtdpz_f' defVal='否' fileName='txgl'/>
								</td>
							</tr>
							<tr>
								<td class="mrymdx_td">启用分批设置：</td>
								<td >
									<input type="radio" name="smsSplit" id="smsSplitY" <%="1".equals(smsSplit)?"checked":""%> value="1" /><label for="smsSplitY">是</label>
									<input type="radio" name="smsSplit" id="smsSplitN" <%="0".equals(smsSplit)?"checked":""%> value="0" /><label for="smsSplitN">否</label>
								</td>
							</tr>
						</tbody>
					</table>
			<div id="langSelect">
				<table>
					<tr>
						<td class="langSelect_td"><emp:message key='txgl_mwadmin_langSelect' defVal='选择语言：' fileName='mwadmin'/></td>
						<td >
							<%
								List<String> langs = new ArrayList<String>();
								if(selectedLanguage!=null||selectedLanguage.length!=0){
									langs = Arrays.asList(selectedLanguage);
								}
							%>
							<input type="hidden" id="defaultLanguage" value="<%=defaultLanguage%>"/>
							<input type="checkbox" name="langName" value="zh_CN" <%=langs.contains("zh_CN")?"checked":""%>>&nbsp;<emp:message key='common_zh_cn' defVal='简体' fileName='common'/>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="checkbox" name="langName" value="zh_TW" <%=langs.contains("zh_TW")?"checked":""%>>&nbsp;<emp:message key='common_zh_tw' defVal='繁体' fileName='common'/>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="checkbox" name="langName" value="zh_HK" <%=langs.contains("zh_HK")?"checked":""%>>&nbsp;<emp:message key='common_zh_hk' defVal='英语' fileName='common'/>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</div>	
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/txgl/datasource/js/selectLang.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
	 (EMP_Cookie = {
	 			add:function(key,value){document.cookie = key + "="+ escape (value) + ";expires=Tue, 19 Jan 2038 00:01:11 GMT";},
	  			del:function(key){document.cookie=key+"= ; expires=Sat, 01 Sep 2001 00:00:00 GMT";},
	  			get:function(key){var arr,reg=new RegExp("(^| )"+key+"=([^;]*)(;|$)");if(arr=document.cookie.match(reg))return unescape(arr[2]);else return null;}
	   	});
	   var langKeyJs = "<%=StaticValue.LANG_KEY%>";
	   var langName = EMP_Cookie.get(langKeyJs)||"zh_CN";
	   $("#switchLang").val(langName);
    </script>
	<script language="javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script language="javascript" src="<%=commonPath%>/common/widget/artDialog/artDialog.js?skin=default"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script  language="javascript" src="<%=iPath %>/js/install.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
	//	$("#form table:nth-child(3) tr > td:nth-child(1)").css("width","140px");
	//	$("#backConn > tbody > tr > td:nth-child(1)").css("width","140px");
	</script>

	<script type="text/javascript">
	var langBox = $("#lang input[name=lang]")
	for(var i = 0;i < langBox.length; i++) {
		if($(langBox[i]).val() == '<%=langName %>') {
			$(langBox[i]).attr("checked","true");
		}
		
	}
	
	var executResult="<%=request.getAttribute("executResult")%>";
	var path="<%=path%>";
	var bakinnerUrl="<%=bakinnerUrl%>";
	var bakouterUrl="<%=bakouterUrl%>";
	var isCluster="<%=isCluster%>";
	var show_backup_server="<%=use_backup_server%>";
	if(executResult!="null"){
	if(executResult=="modifPwdsuccess"){
        /*修改密码成功！*/
        alert(getJsLocaleMessage("txgl","txgl_js_install_23"));
	}
		if(executResult=="differ"){
            /*旧密码不对！*/
            alert(getJsLocaleMessage("txgl","txgl_js_install_24"));
	}
		if(executResult=="confsucc"){
            /*修改配置项成功！*/
            alert(getJsLocaleMessage("txgl","txgl_js_install_25"));
	}
	if(executResult=="conffail"){
			/*修改配置项失败！*/
			alert(getJsLocaleMessage("txgl","txgl_js_install_26"));
	}
	if(executResult=="defalutSucc"){
	    /*恢复默认值成功！*/
		alert(getJsLocaleMessage("txgl","txgl_js_install_27"));
	}
		if(executResult=="savesucess"){
		$("#errorIP").html("");
            /*保存成功！*/
            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_10"));
	}
		if(executResult=="defalutFail"){
            /*恢复默认值失败！*/
            alert(getJsLocaleMessage("txgl","txgl_js_install_29"));
	}
	}
	function logout() {
    	/*是否退出登录？*/
        if (confirm(getJsLocaleMessage("common","common_frame2_top_5"))) {
		parent.window.location.replace(path + '/logout');
		return;
	}
}
function changeDbType(dbType) {
		if(dbType=="1") {
            /*实例名/服务名：*/
            $("#tdDbName span").text(getJsLocaleMessage("txgl","txgl_js_install_1"));
            /*备用实例名/服务名：*/
            $("#tdDbName2 span").text(getJsLocaleMessage("txgl","txgl_js_install_2"));
			$("#connType").attr("disabled","");
        } else {
            /*数据库名称：*/
            $("#tdDbName span").text(getJsLocaleMessage("txgl","txgl_js_install_3"));
            /*备用数据库名称：*/
            $("#tdDbName2 span").text(getJsLocaleMessage("txgl","txgl_js_install_4"));
			$("#connType").attr("disabled","disabled");
		}
	}
	</script>
</body>
</html>