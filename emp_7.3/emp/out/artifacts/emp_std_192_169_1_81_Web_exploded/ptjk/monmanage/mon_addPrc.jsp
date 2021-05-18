<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.monitor.LfMonShost"%>
<%@page import="com.montnets.emp.entity.gateway.AgwAccount"%>
<%@page import="com.montnets.emp.entity.monitor.LfMonSproce"%>
<%@ page
        import="com.montnets.emp.common.constant.StaticValue" %>
        
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
String path = request.getContextPath();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("prcMonCfg");
menuCode = menuCode==null?"0-0-0":menuCode;

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");

Boolean result = (Boolean)request.getAttribute("result");
Long prcId = (Long)request.getAttribute("prcId");
@ SuppressWarnings("unchecked")
List<LfMonShost> shostList = (List<LfMonShost>)request.getAttribute("shostList");
@ SuppressWarnings("unchecked")
List<AgwAccount> accountList = (List<AgwAccount>)request.getAttribute("accountList");
%>
<!doctype html>
<html>
  <head>
    
    <title></title>
    <%@include file="/common/common.jsp" %>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<style type="text/css">
		table
		{
			font-size:12px;
			line-height: 40px !important;
			line-height: 30px;
		}
		table input
		{
			width: 200px;
			height: 24px;
		}
		.xinhao
		{
			color: red;
			margin-left: 3px;
			margin-right: 3px;
		}
		.til
		{
			margin-left: 3px;
		}
		.select
		{
			width: 200px;
		}
	</style>
  </head>
  
  <body>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("ptjk","ptjk_jkgl_cx_8",request)) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" style="padding-left: 40px;">
			<form action="mon_prcMonCfg.htm?method=addPrc" method="post" id="addForm" name="addForm" autocomplete="off">
				<div id="loginUser" class="hidden"></div>
				<div class="div_bg" style="height: 30px;line-height: 30px;"><strong>&nbsp;<emp:message key="ptjk_jkgl_zj_jbxx" defVal="基本信息" fileName="ptjk"/></strong></div>
				<table>
					<tr>
						<td><emp:message key="ptjk_common_cxlx_mh" defVal="程序类型：" fileName="ptjk"/></td>
						<td>
							<select class="select input_bd" id="procetype" style="width:204px;" name="procetype" onchange="getGateWayid()">
								<option value="5000" selected="selected"><emp:message key="ptjk_common_webcx" defVal="WEB程序" fileName="ptjk"/></option>
								<option value="5200"><emp:message key="ptjk_common_empwg" defVal="EMP网关" fileName="ptjk"/>(EMP_GW)</option>
								<option value="5300"><emp:message key="ptjk_common_yysjk" defVal="运营商接口" fileName="ptjk"/>(SPGATE)</option>
								<option value="5800"><emp:message key="ptjk_common_wjfwq" defVal="文件服务器" fileName="ptjk"/></option>
							</select>
							<font class="xinhao">*</font>
						</td>
					</tr>
					<tr id="gate_id_tr" >
						<td><emp:message key="ptjk_jkgl_cx_wgbh_mh" defVal="网关编号：" fileName="ptjk"/></td>
						<td>
							<select class="select input_bd" style="width:204px;" id="gatewayid" name="gatewayid">
							<%
							if(accountList!=null && accountList.size()>0)
							{
								for(AgwAccount aa:accountList)
								{
							%>
								<option value="<%=aa.getGwNo() %>"><%=aa.getGwNo() %></option>
							<%		
								}
							}
							%>
							</select>
							<font class="xinhao">*</font>
						</td>
					</tr>
					<tr>
						<td width="140px;"><emp:message key="ptjk_jkxq_cx_cxmc_mh" defVal="程序名称：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="procename" id="procename" maxlength="20" class="input_bd" value=""/>
							<font class="xinhao">*</font><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_5" defVal="(1~20位字符)" fileName="ptjk"/></span>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_cx_sszj_mh" defVal="所属主机：" fileName="ptjk"/></td>
						<td>
							<select id="hostid" name="hostid" style="width:204px;" class="select input_bd">
								<option value="-1"></option>
							<%if(shostList!=null && shostList.size()>0){
								for(LfMonShost shost : shostList){
								%>
								<option value="<%=shost.getHostid() %>"><%=shost.getHostname().replaceAll("<","&lt").replaceAll(">","&gt")%></option>
							<%}} %>
							</select>
<%--							<font class="xinhao">*</font>--%>
						</td>
					</tr>
                    <tr id="server-tr">
                        <td><emp:message key="ptjk_jkxq_sjk_ssjd_mh" defVal="所属节点：" fileName="ptjk"/></td>
                        <td>
                            <input type="text" name="servernum" id="servernum" maxlength="8" class="input_bd" value=""/>
                            <font class="xinhao">*</font><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_cx_4" defVal="(1~8位字符)" fileName="ptjk"/></span>
                        </td>
                    </tr>

					<tr>
						<td><emp:message key="ptjk_common_jkzt_mh" defVal="监控状态：" fileName="ptjk"/></td>
						<td>
							
							<select name="monstatus" id="monstatus" style="width:204px;" class="select input_bd">
								<option value="1"><emp:message key="ptjk_common_jk" defVal="监控" fileName="ptjk"/></option>
								<option value="0"><emp:message key="ptjk_common_wjk" defVal="未监控" fileName="ptjk"/></option>
							</select>
							<font class="xinhao">*</font>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_common_bbh_mh" defVal="版本号：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="version" id="version" maxlength="20" class="input_bd" value=""/>
							<font class="xinhao">*</font><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_5" defVal="(1~20位字符)" fileName="ptjk"/></span>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_common_gjsjh_mh" defVal="告警手机号：" fileName="ptjk"/></td>
						<td>
							<textarea name="monphone" id="monphone" class="input_bd" style="width:200px;"></textarea>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkxq_sjk_7" defVal="(可以设置至多十个手机号，号码间用逗号“,”分隔)" fileName="ptjk"/></span></font>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_common_gjyx_mh" defVal="告警邮箱：" fileName="ptjk"/></td>
						<td><textarea name="monemail" id="monemail" class="input_bd"
								style="width:200px;"></textarea> <font class="til"><span
								style="color: #cccccc;font-size: 14px;"><emp:message key="ptjk_common_znszygyx" defVal="只能设置一个邮箱" fileName="ptjk"/></span>
						</font></td>
					</tr>
				</table> 
				<div class="div_bg" style="height: 30px;line-height: 30px;"><strong>&nbsp;<emp:message key="ptjk_jkgl_zj_13" defVal="告警阀值设置" fileName="ptjk"/></strong></div>
				<table >
					<tr>
						<td width="156px;"><emp:message key="ptjk_jkgl_zj_8" defVal="CPU占用比率告警阀值：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="cpubl" id="cpubl" class="input_bd int" value="90" maxlength="3"/>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" >(<emp:message key="ptjk_jkgl_zj_dw_mh" defVal="单位：" fileName="ptjk"/>%&nbsp;<emp:message key="ptjk_jkgl_zj_qzfw" defVal="取值范围" fileName="ptjk"/> 0 - 100)</span></font>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_9" defVal="物理内存使用量告警阀值：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="memuse" id="memuse" maxlength="9" class="input_bd int" value=""/>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_dwm" defVal="(单位：M)" fileName="ptjk"/></span></font>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_10" defVal="虚拟内存使用量告警阀值：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="vmemuse" id="vmemuse" maxlength="9" class="input_bd int" value=""/>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_dwm" defVal="(单位：M)" fileName="ptjk"/></span></font>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_11" defVal="磁盘剩余空间告警阀值：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="harddiskspace" maxlength="9" id="harddiskspace" class="input_bd int" value=""/>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_dwm" defVal="(单位：M)" fileName="ptjk"/></span></font>
						</td>
					</tr>

				</table>
				<table style="margin-left:130px;margin-top: 20px;">
					<tr>
						<td>
							<input type="button" name="sub" id="sub" value="<emp:message key='ptjk_common_qd' defVal='确定' fileName='ptjk'/>" onclick="addPrc()" class="btnClass5 mr23"/>
							<input type="button" name="button" id="button" value="<emp:message key='ptjk_common_fh' defVal='返回' fileName='ptjk'/>" onclick="javascript:doreturn()" 
							class="btnClass6"/><br/>
						</td>
					</tr>
				</table>
			</form>
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
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script src="<%=iPath%>/js/prcMonCfg.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script src="<%=iPath%>/js/text.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript">
		var pathUrl = '<%=path%>';
		var prcId = '<%=prcId%>';
		$(document).ready(function() {
			getLoginInfo("#loginUser");
	        $('#gate_id_tr').hide();
			<%
			if(result!=null){
				if(result)
				{
			%>
					if(confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_cx_1")))
					{
						 toHostMonCfg("3000-1300");
					}
					else
					{
						doreturn();
					}
			<%
				}
				else
				{
			%>
					alert(getJsLocaleMessage("ptjk","ptjk_common_xjsb"));
			<%
				}
			}
			%>
			$("select[name=procename] option").each(function(){
					$(this).text($(this).text()+$("#gatewayid").val());
					$(this).val($(this).val()+$("#gatewayid").val());
				});
			$("#gatewayid").change(function(){
				$("select[name=procename] option").each(function(){
					$(this).text($(this).text()+$("#gatewayid").val());
					$(this).val($(this).val()+$("#gatewayid").val());
				});
				});
		});
	</script>
	</body>
</html>
