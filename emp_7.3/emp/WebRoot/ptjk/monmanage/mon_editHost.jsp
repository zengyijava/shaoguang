<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.monitor.LfMonSproce"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
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
String menuCode = titleMap.get("hostMonCfg");
menuCode = menuCode==null?"0-0-0":menuCode;

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
DynaBean hostBean = (DynaBean)request.getAttribute("dhostBean");
String ip1="",ip2="",ip3="",ip4="",ip5="",ip6="",ip7="",ip8="";
String oupip = hostBean.get("oupip")!=null?hostBean.get("oupip").toString():"";
String adapter1 = hostBean.get("adapter1")!=null?hostBean.get("adapter1").toString():"";
if(oupip.indexOf(".")!=-1)
{
	String [] ip=oupip.split("\\.");
	if(ip.length==4)
	{
		ip1=ip[0];
		ip2=ip[1];
		ip3=ip[2];
		ip4=ip[3];
	}
}

if(adapter1.indexOf(".")!=-1)
{
	String [] ip=adapter1.split("\\.");
	if(ip.length==4)
	{
		ip5=ip[0];
		ip6=ip[1];
		ip7=ip[2];
		ip8=ip[3];
	}
}

Boolean result = (Boolean)request.getAttribute("result");
@ SuppressWarnings("unchecked")
List<LfMonSproce> sprcList = (List<LfMonSproce>)request.getAttribute("sprcList");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'mon_editHost.jsp' starting page</title>
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
	<style type="text/css">
		table
		{
			font-size:12px;
			line-height: 40px !important;
			line-height: 30px;
		}
		table input
		{
			width: 260px;
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
		.a3 {
			width: 60px;
			height:17px;
			 border: 0;
			text-align: center;
			background: white;
		}
	</style>
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
  </head>
  
  <body>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("ptjk","ptjk_jkgl_zj_14",request)) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" style="padding-left: 40px;">
			<form action="mon_hostMonCfg.htm?method=editHost" method="post" id="editForm" name="editForm" autocomplete="off">
				<div id="loginUser" class="hidden"></div>
				<div class="div_bg" style="height: 30px;line-height: 30px;"><strong>&nbsp;<emp:message key="ptjk_jkgl_zj_jbxx" defVal="基本信息" fileName="ptjk"/></strong></div>
				<table>
					<tr>
						<td width="140px;"><emp:message key="ptjk_common_zjbh_mh" defVal="主机编号：" fileName="ptjk"/></td>
						<td>
							<label><%=hostBean.get("hostid")!=null?hostBean.get("hostid"):"" %></label>
							<input type="hidden" name="hostid" id="hostid" class="input_bd" value="<%=hostBean.get("hostid")!=null?hostBean.get("hostid"):"" %>"/>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_common_zjmc_mh" defVal="主机名称：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="hostname" maxlength="20" id="hostname" class="input_bd" value="<%=hostBean.get("hostname")!=null?hostBean.get("hostname"):"" %>"/>
							<font class="xinhao">*</font><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_5" defVal="(1~20位字符)" fileName="ptjk"/></span>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_czxt_mh" defVal="操作系统：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="opersys" maxlength="20" id="opersys" class="input_bd" value="<%=hostBean.get("opersys")!=null?hostBean.get("opersys"):"" %>"/>
							<span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_5" defVal="(1~20位字符)" fileName="ptjk"/></span>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_zjnc_mh" defVal="主机内存：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="hostmem" id="hostmem" maxlength="9" class="input_bd int" value="<%=hostBean.get("hostmem")!=null?hostBean.get("hostmem"):"" %>"/>
							<span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_dwm" defVal="(单位：M)" fileName="ptjk"/></span>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_cpdx_mh" defVal="磁盘大小：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="hosthd" id="hosthd" maxlength="9" class="input_bd int" value="<%=hostBean.get("hosthd")!=null?hostBean.get("hosthd"):"" %>"/>
							<span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_dwm" defVal="(单位：M)" fileName="ptjk"/></span>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_3" defVal="CPU信息：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="hostcpu" id="hostcpu" maxlength="32" class="input_bd" value="<%=hostBean.get("hostcpu")!=null?hostBean.get("hostcpu"):"" %>"/>
							<span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_6" defVal="(1~32位字符)" fileName="ptjk"/></span>
						</td>
					</tr>
					<tr>
						<td>
							<emp:message key="ptjk_common_jkzt_mh" defVal="监控状态：" fileName="ptjk"/>
						</td>
						<td>
							<select name="monstatus" id="monstatus" style="width: 264px;" class="input_bd">
								<option value="0" <%="0".equals(hostBean.get("monstatus").toString())?"selected":"" %>><emp:message key="ptjk_common_wjk" defVal="未监控" fileName="ptjk"/></option>
								<option value="1" <%="1".equals(hostBean.get("monstatus").toString())?"selected":"" %>><emp:message key="ptjk_common_jk" defVal="监控" fileName="ptjk"/></option>
							</select>
							<font class="xinhao">*</font>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_wwip_mh" defVal="外网IP：" fileName="ptjk"/></td>
						<td>
							<div id="ptIp" class="input_bd"
							style="width:260px; background: white;font-size: 8pt;float: left;">
							<input type=text name=ip1 id="ip1" maxlength=3 value="<%=ip1 %>" class=a3
							onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
							<input type=text name=ip2 id="ip2"  maxlength=3 value="<%=ip2 %>" class=a3
							onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
							<input type=text name=ip3  id="ip3" maxlength=3 value="<%=ip3 %>" class=a3 
							onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
							<input type=text name=ip4 id="ip4" maxlength=3 value="<%=ip4 %>" class=a3
							onkeyup="mask(this,event)" onbeforepaste=mask_c()> </div>
							<input type="hidden" id="oupip" name="oupip" value="<%=hostBean.get("oupip")!=null?hostBean.get("oupip"):"" %>"/>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkxq_zj_nwip_mh" defVal="内网IP：" fileName="ptjk"/></td>
						<td>
							<div id="spip" class="input_bd"
							style="background:white;width:260px; font-size: 8pt;float: left;" >
							<input type=text name=ip5 id="ip5" maxlength=3 value="<%=ip5 %>" class=a3
							onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
							<input type=text name=ip6 id="ip6"  maxlength=3 value="<%=ip6 %>" class=a3
							onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
							<input type=text name=ip7  id="ip7" maxlength=3 value="<%=ip7 %>" class=a3
							onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
							<input type=text name=ip8 id="ip8" maxlength=3 value="<%=ip8 %>" class=a3
							onkeyup="mask(this,event)" onbeforepaste=mask_c()>
							</div>
							<input type="hidden" id="adapter1" name="adapter1" value="<%=hostBean.get("adapter1")!=null?hostBean.get("adapter1"):"" %>"/>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_common_gjsjh_mh" defVal="告警手机号：" fileName="ptjk"/></td>
						<td>
							<textarea name="monphone" id="monphone" class="input_bd" style="width:260px;"><%=hostBean.get("monphone")!=null?hostBean.get("monphone"):"" %></textarea>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkxq_sjk_7" defVal="(可以设置至多十个手机号，号码间用逗号“,”分隔)" fileName="ptjk"/></span></font> 
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_common_gjyx_mh" defVal="告警邮箱：" fileName="ptjk"/></td>
						<td>
							<textarea name="monemail" id="monemail" class="input_bd" style="width:260px;"><%=hostBean.get("monemail")!=null?hostBean.get("monemail").toString().trim():"" %></textarea>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_common_znszygyx" defVal="只能设置一个邮箱" fileName="ptjk"/></span></font> 
						</td>
					</tr>
				</table>
				<div class="div_bg" style="height: 30px;line-height: 30px;"><strong>&nbsp;<emp:message key="ptjk_jkgl_zj_13" defVal="告警阀值设置" fileName="ptjk"/></strong></div>
				<table>
					<tr>
						<td width="156px;"><emp:message key="ptjk_jkgl_zj_8" defVal="CPU占用比率告警阀值：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="cpuusage" id="cpuusage" class="input_bd int" maxlength="3" value="<%=hostBean.get("cpuusage")!=null?hostBean.get("cpuusage"):"" %>"/>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" >(<emp:message key="ptjk_jkgl_zj_dw_mh" defVal="单位：" fileName="ptjk"/>%&nbsp;<emp:message key="ptjk_jkgl_zj_qzfw" defVal="取值范围" fileName="ptjk"/> 0 - 100)</span></font>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_9" defVal="物理内存使用量告警阀值：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="memusage" id="memusage" class="input_bd int" maxlength="9" value="<%=hostBean.get("memusage")!=null?hostBean.get("memusage"):"" %>"/>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_dwm" defVal="(单位：M)" fileName="ptjk"/></span></font>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_10" defVal="虚拟内存使用量告警阀值：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="vmemusage" id="vmemusage" class="input_bd int" maxlength="9" value="<%=hostBean.get("vmemusage")!=null?hostBean.get("vmemusage"):"" %>"/>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_dwm" defVal="(单位：M)" fileName="ptjk"/></span></font>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_11" defVal="磁盘剩余空间告警阀值：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="diskfreespace" id="diskfreespace" class="input_bd int" maxlength="9" value="<%=hostBean.get("diskfreespace")!=null?hostBean.get("diskfreespace"):"" %>"/>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_dwm" defVal="(单位：M)" fileName="ptjk"/></span></font>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_zj_12" defVal="进程数告警阀值：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="processcnt" id="processcnt" maxlength="9" class="input_bd int" value="<%=hostBean.get("processcnt")!=null?hostBean.get("processcnt"):"" %>"/>
							<font class="til"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_zj_dwg" defVal="(单位：个)" fileName="ptjk"/></span></font>
						</td>
					</tr>
				</table>
				<table style="margin-left:130px;margin-top: 20px;">
					<tr>
						<td>
							<input type="button" name="sub" id="sub" value="<emp:message key='ptjk_common_qd' defVal='确定' fileName='ptjk'/>" onclick="editHost()" class="btnClass5 mr23"/>
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
    <script language="javascript" type="text/javascript" src="<%=iPath%>/js/ip.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script src="<%=iPath%>/js/hostMonCfg.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script src="<%=iPath%>/js/text.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript">
		var pathUrl = '<%=path%>';
		$(document).ready(function() {
			getLoginInfo("#loginUser");
			<%
			if(result!=null){
				if(result)
				{
			%>
					alert(getJsLocaleMessage("ptjk","ptjk_common_xgcg"));
					doreturn();
			<%
				}
				else
				{
			%>
					alert(getJsLocaleMessage("ptjk","ptjk_common_xgsb"));
			<%
				}
			}
			%>
		});
	</script>
	</body>
</html>
