<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	//连接类型  对Oracle数据库有用
	String connType=request.getAttribute("connType")==null?"":request.getAttribute("connType").toString();
	String isCluster=request.getAttribute("isCluster")==null?"":request.getAttribute("isCluster").toString();
	String serverNumber=request.getAttribute("serverNumber")==null?"":request.getAttribute("serverNumber").toString();
	
	String bakinnerUrl=request.getAttribute("bakinnerUrl")==null?"":request.getAttribute("bakinnerUrl").toString();
	String bakouterUrl=request.getAttribute("bakouterUrl")==null?"":request.getAttribute("bakouterUrl").toString();
	String use_backup_server=request.getAttribute("use_backup_server")==null?"":request.getAttribute("use_backup_server").toString();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

%>
<!doctype html>
<html lang="en">
<head >
	<meta charset="UTF-8">
	<title>欢迎欢使用梦网科技EMP移动信息平台</title>
	<link rel="stylesheet" href="<%=iPath %>/css/install.css?V=<%=StaticValue.getJspImpVersion() %>">
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/dat_systemDataSet.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/dat_systemDataSet.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
</head>
<body id="dat_systemDataSet" onload="">
	<div class="header">
		<div class="inner">
			<h2 class="logo"></h2>
			<a href="javascript:void(0);" class="edit_pwd">修改密码</a>
			<a href="javascript:logout();" class="loginOut">退出</a>
		</div>
		
	</div>
	<div class="mod_top radius">
		<h2>EMP参数配置</h2>
	</div>
	<div class="wrapper">
	<form id="form" name="form" action="<%=path %>/systemManage.htm?method=update" method="post">
		<%--数据库配置start--%>
		<div class="db_config">
			<div class="cg_tags radius">
					<b>1</b>
			</div>
			<div class="config_detail radius">
				<h2 class="c1">数据库配置</h2>
				<div class="inner">
					<table class="cg_tab">
						<tbody>
							<tr>
								<td class="sjklx_td">数据库类型：</td>
								
								<td>
								<select name="DBType" id="DBType" class="inpSel"  onchange="changeDbType(this.value)">
								<option value="1" <%if("1".equals(DBType)){ %>selected="selected"<%} %> >Oracle</option>
								<option value="2" <%if("2".equals(DBType)){ %>selected="selected"<%} %> >SQL Server</option>
								<option value="3" <%if("3".equals(DBType)){ %>selected="selected"<%} %> >MySQL</option>
								<option value="4" <%if("4".equals(DBType)){ %>selected="selected"<%} %> >DB2</option>
								</select>
							</tr>
							<tr>
								<td>数据库连接池：</td>
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
								<td>连接类型：</td>
								<td>
									<select id="connType" name="connType" class="inpSel">
									    <option value="1" <%if("1".equals(connType)){ %>selected="selected"<%} %> >SID</option>
										<option value="0" <%if("0".equals(connType)){ %>selected="selected"<%} %> >Service Name</option>
									</select>
								</td>
							</tr>
							<input type="hidden" id="type" value="">
							<tr>
								<td>数据库地址：</td>
								<td><input type="text" name="dbconIp" id="dbconIp" value="<%=request.getAttribute("databaseIp")==null?"":request.getAttribute("databaseIp")%>" class="inpStyle" placeholder="·请输入IP地址"></td>
							</tr>
							<tr>
								<td>数据库端口号：</td>
								<td><input type="text" name="port" id="port" value="<%=request.getAttribute("databasePort")==null?"":request.getAttribute("databasePort")%>" class="inpStyle" placeholder="·请输入端口号"></td>
							</tr>
							<tr>
								<td id="tdDbName"><span>实例名/服务名：</span></td>
								<td><input type="text" name="dbName" id="dbName" value="<%=request.getAttribute("databaseName")==null?"":request.getAttribute("databaseName")%>" class="inpStyle" placeholder="·请输入名称"></td>
							</tr>
							<tr>
								<td>数据库用户名：</td>
								<td><input type="text" name="dbUser" id="dbUser" value="<%=request.getAttribute("user")==null?"":request.getAttribute("user")%>" class="inpStyle" placeholder="·请输入用户名"></td>
							</tr>
							<tr>
								<td>数据库密码：</td>
								<td><input type="password" name="dbPwd"  id="dbPwd" value="<%=request.getAttribute("password")==null?"":request.getAttribute("password")%>" class="inpStyle" placeholder="·请输入密码"></td>
							</tr>
							
						</tbody>
					</table>
					<div class="cg_btn">
						<a href="javascript:testConnection()" class="btn01" id="testDBConnBtn" >测试连接</a>
						<font id="waitTextConnection" color="#808080"></font>
						<%--  <span id="success" style="display:none;" class="tips_success">连接成功</span>
						<span id="error" style="display:none;" class="tips_error">连接失败</span>
						--%>
								<font id="rightTextConnection" color="green"></font>
								<font id="errorTextConnection" color="red"></font>
					</div>
					<table class="cg_tab use_backup_server_table"  >
							<tr>
								<td class="bysjkg_td">备用数据库开关：</td>
								<td class="use_backup_server_td">
								<select name="use_backup_server" id="use_backup_server" class="inpSel use_backup_server"  onchange="isOPen(this.value)">
								<option value="0" <%if("0".equals(use_backup_server)){ %>selected="selected"<%} %> >关闭</option>
								<option value="1" <%if("1".equals(use_backup_server)){ %>selected="selected"<%} %>  >开启</option>
								</select>
								</td>
								<td class="tips01"></td>
							</tr>
							</table>
						<table id="backConn" class="cg_tab backConn"  >
								<tr>
								<td class="dbconIp2_up_td">备用数据库地址：</td>
								<td><input type="text" name="dbconIp2" id="dbconIp2" value="<%=request.getAttribute("databaseIp2")==null?"":request.getAttribute("databaseIp2")%>" class="inpStyle w335" placeholder="·请输入IP地址"></td>
								<td class="tips01"></td>
							</tr>
							<tr>
								<td class="port2_up_td">备用数据库端口号：</td>
								<td><input type="text" name="port2" id="port2" value="<%=request.getAttribute("databasePort2")==null?"":request.getAttribute("databasePort2")%>" class="inpStyle w335" placeholder="·请输入端口号"></td>
							<td class="tips01"></td>
							</tr>
							<tr>
								<td class="dbName2_up_td" id="tdDbName2"><span>备用实例名/服务名：</span></td>
								<td><input type="text" name="dbName2" id="dbName2" value="<%=request.getAttribute("databaseName2")==null?"":request.getAttribute("databaseName2")%>" class="inpStyle w335" placeholder="·请输入名称"></td>
							<td class="tips01"></td>
							</tr>
							<tr>
								<td class="dbUser2_up_td">备用数据库用户名：</td>
								<td><input type="text" name="dbUser2" id="dbUser2" value="<%=request.getAttribute("user2")==null?"":request.getAttribute("user2")%>" class="inpStyle w335" placeholder="·请输入用户名"></td>
							<td class="tips01"></td>
							</tr>
							<tr>
								<td class="dbPwd2_up_td">备用数据库密码：</td>
								<td><input type="password" name="dbPwd2"  id="dbPwd2" value="<%=request.getAttribute("password2")==null?"":request.getAttribute("password2")%>" class="inpStyle w335" placeholder="·请输入密码"></td>
							<td class="tips01"></td>
							</tr>
							</table>
					
					<div class="cg_btn" style="display:none" id="backTest">
						<a href="javascript:testBackConnection()" class="btn01" id="testBackConnBtn" >测试连接</a>
						<font id="waitTextConnection2" color="#808080"></font>
						<%--  <span id="success" style="display:none;" class="tips_success">连接成功</span>
						<span id="error" style="display:none;" class="tips_error">连接失败</span>
						--%>
								<font id="rightTextConnection2" color="green"></font>
								<font id="errorTextConnection2" color="red"></font>
					</div>
					<span class="cg_more" id="db_cg_event">更多参数配置</span>

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
				<h2 class="c1">WEB信息</h2>
				<div class="inner">
					<table class="cg_tab">
						<tbody>
								<tr>
								<td class="jqkg_td">集群开关：</td>
								<td class="isCluster_td">
								<select name="isCluster" id="isCluster" class="inpSel isCluster"   onchange="changeType(this.value)">
								<option value="0" <%if("0".equals(isCluster)){ %>selected="selected"<%} %> >关闭</option>
								<option value="1" <%if("1".equals(isCluster)){ %>selected="selected"<%} %>  >开启</option>
								</select>
								</td>
								<td class="tips01"></td>
							</tr>
					<tr id="servershow" >
						<td colspan="3">
						<table id="serverNum" class="serverNum">
						<tr>
								<td class="server1" id="server1" >集群节点：</td>
								<td class="server2" id="server2">
								<select name="serverNumber" id="serverNumber" class="inpSel serverNumber" >
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
								<td>文件内网地址：</td>
								<td><input type="text" name="innerUrl" id="innerUrl" value="<%=request.getAttribute("innerUrl")==null?"":request.getAttribute("innerUrl")%>" class="inpStyle w335" placeholder="·请输入内网地址"></td>
								<td class="tips01">例：http://192.168.2.234:8610/emp/(注:末尾"/"要加上)</td>
							</tr>
							<tr>
								<td>文件外网地址：</td>
								<td><input type="text" name="outerUrl" id="outerUrl" value="<%=request.getAttribute("outerUrl")==null?"":request.getAttribute("outerUrl")%>" class="inpStyle w335" placeholder="·请输入外网地址"></td>
								<td class="tips01">例：http://121.15.130.218:8610/emp/(注:末尾"/"要加上)</td>
							</tr>

							
						<tr id="initCols_show"  >
						<td colspan="3">
						<table id="initCols" class="initCols">
						<tr id="listinner1">
						<td class="listinner1_td">备用内网地址1：</td>
						<td><input type="text" name="bakinnerUrl1" id="bakinnerUrl1" value="" class="inpStyle w335" placeholder="·备用内网地址1"></td>
						<td class="tips01"></td>
						</tr>
						<tr id="listout1">
							<td>备用外网地址1：</td>
							<td><input type="text" name="bakouterUrl1" id="bakouterUrl1" value="" class="inpStyle w335" placeholder="·备用外网地址1"></td>
							<td><a onclick="javascript:addCols()" class="tjby_a">添加备用</a>
							</td>
						</tr>
						</table></td>
						</tr>
							
							<tr>
								<td class="empfwdz_td">EMP访问地址：</td>
								<td class="EMPaddress_td"><input type="text" name="EMPaddress" id="EMPaddress" value="<%=request.getAttribute("EMPaddress")==null?"":request.getAttribute("EMPaddress")%>" class="inpStyle w335" placeholder="·请输入EMP访问地址"></td>
								<td class="tips01">例：http://192.168.2.234:8610/emp/(注:末尾"/"要加上)</td>
							</tr>
							<tr>
								<td>网关通讯地址：</td>
								<td><input type="text" name="webgate" id="webgate"  value="<%=request.getAttribute("webgate")==null?"":request.getAttribute("webgate")%>" class="inpStyle w335" placeholder="·请输入网关能讯地址"></td>
								<td class="tips01">例：http://192.168.2.234:8610/sms/mt</td>
							</tr>
							<tr>
								<td>网讯站点访问地址：</td>
								<td><input type="text" name="EMPwxaddress" id="EMPwxaddress" value="<%=request.getAttribute("EMPwxaddress")==null?"":request.getAttribute("EMPwxaddress")%>" class="inpStyle w335" placeholder="·请输入网讯站点"></td>
								<td class="tips01">例：http://121.15.130.218:8610/emp</td>
							</tr>
							<tr>
								<td>EMP日志文件存储地址：</td>
								<td><input type="text" name="loggeraddress" id="loggeraddress" value="<%=request.getAttribute("loggeraddress")%>" class="inpStyle w335" placeholder="·请输入日志文件存储地址">
								    <input type="hidden" name="defaultloggeraddress" id="defaultloggeraddress" value="<%=request.getAttribute("defaultloggeraddress")%>">
								</td>
								<td class="tips01">例：D:\Program Files\Tomcat 6.0\webapps\emp\</td>
							</tr>

						<tr>

						</tbody>
					</table>
					<span class="cg_more" id="web_cg_event">更多参数配置</span>
					<div class="cg_btn">
						<a href="javascript:submit();" class="btn01">完成</a>
						<font id="errorIP" color="red"></font>
					</div>
				</div>
				
			</div>
		</div>
		</form>
		<%--web信息end--%>
		<%--弹出框**修改密码--%>
		<div id="modfiy_pwd" style="display:none;">
			<form action="<%=path %>/systemManage.htm?method=modifPwd" id="form_pwd" method="post">
				<table class="tab_pwd">
					<tr>
						<td>旧密码：</td>
						<td><input type="password" name="oldPwd" id="oldPwd" class="inp02" onblur="checkPass(this.value)"></td>
					</tr>
					<tr>
						<td>新密码：</td>
						<td><input type="password" name="newPwd" id="newPwd" class="inp02"></td>
					</tr>
					<tr>
						<td>确认密码：</td>
						<td><input type="password" name="confirmPwd" id="confirmPwd" class="inp02"></td>
					</tr>
				</table>
			</form>
			<font id="errorPWD" color="red"></font>
			<font id="corrPWD" color="blue"></font>
		</div>


	</div>
	<%--弹出框**修改密码end--%>
	<%--弹出框**数据库更多配置参数start--%>
	<div id="db_more_cg" class="cg_box">
		<form action="<%=path %>/systemManage.htm?method=dataSubmit" id="form_more_db" method="post">
					<table class="cg_tab">
						<tbody>
							<tr>
								<td class="sjljc_td">数据连接池最大连接数：</td>
								<td class="maxPoolSize_td"><input type="text" name="maxPoolSize" id="maxPoolSize"  onkeyup="numberControl($(this))"  maxlength="5" value="<%=request.getAttribute("maxPoolSize")==null?"":request.getAttribute("maxPoolSize")%>" class="inpStyle w335" placeholder="·请输入连接池最大连接数"></td>
							</tr>
							<tr>
								<td>数据连接池最小连接数：</td>
								<td><input type="text" name="minPoolSize" id="minPoolSize" onkeyup="numberControl($(this))" maxlength="5" value="<%=request.getAttribute("minPoolSize")==null?"":request.getAttribute("minPoolSize") %>" class="inpStyle w335" placeholder="·请输入连接池最小连接数"></td>
							</tr>
							<tr>
								<td>数据连接池初始连接数：</td>
								<td><input type="text" name="InitialPoolSize" id="InitialPoolSize" onkeyup="numberControl($(this))" maxlength="5" value="<%=request.getAttribute("InitialPoolSize")==null?"":request.getAttribute("InitialPoolSize") %>" class="inpStyle w335" placeholder="·请输入连接池初始连接数"></td>
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
								<td class="mrymdx_td">默认页面大小：</td>
								<td class="defaultPageSize_td"><input type="text" name="defaultPageSize" id="defaultPageSize" onkeyup="numberControl($(this))" maxlength="5"  value="<%=request.getAttribute("defaultPageSize")==null?"":request.getAttribute("defaultPageSize")%>" class="inpStyle w335" placeholder="·请输入默认页面大小"></td>
							</tr>
							<tr>
								<td>肤色版本：</td>
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
							
						</tbody>
					</table>
			
		</form>
	</div>	
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script language="javascript" src="<%=commonPath%>/common/widget/artDialog/artDialog.js?skin=default"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	
	<script  language="javascript" src="<%=iPath %>/js/install.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
	var executResult="<%=request.getAttribute("executResult")%>";
	var path="<%=path%>";
	var bakinnerUrl="<%=bakinnerUrl%>";
	var bakouterUrl="<%=bakouterUrl%>";
	var isCluster="<%=isCluster%>";
	var show_backup_server="<%=use_backup_server%>";
	if(executResult!="null"){
	if(executResult=="modifPwdsuccess"){
		alert("修改密码成功！");
	}
		if(executResult=="differ"){
		alert("旧密码不对！");
	}
		if(executResult=="confsucc"){
		alert("修改配置项成功！");
	}
	if(executResult=="conffail"){
		alert("修改配置项失败！");
	}
	if(executResult=="defalutSucc"){
		alert("恢复默认值成功！");
	}
		if(executResult=="savesucess"){
		$("#errorIP").html("");
		alert("保存成功！");
	}
		if(executResult=="defalutFail"){
		alert("恢复默认值失败！");
	}
	}
	function logout() {
	if (confirm("是否退出登录？")) {
			window.history.back(-1);
	}
}
function changeDbType(dbType)
	{
		if(dbType=="1")
		{
			$("#tdDbName span").text("实例名/服务名：");
			$("#tdDbName2 span").text("备用实例名/服务名：");
			$("#connType").attr("disabled","");
		}else if(dbType=="2")
		{
			$("#tdDbName span").text("数据库名称：");
			$("#tdDbName2 span").text("备用数据库名称：");
			$("#connType").attr("disabled","disabled");
		}else if(dbType=="3")
		{
			$("#tdDbName span").text("数据库名称：");
			$("#tdDbName2 span").text("备用数据库名称：");
			$("#connType").attr("disabled","disabled");
		}else
		{
			$("#tdDbName span").text("数据库名称：");
			$("#tdDbName2 span").text("备用数据库名称：");
			$("#connType").attr("disabled","disabled");
		}
	}
	</script>
</body>
</html>