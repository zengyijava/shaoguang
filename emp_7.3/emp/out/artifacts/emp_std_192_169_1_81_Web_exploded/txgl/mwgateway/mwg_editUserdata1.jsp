<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.Userdata"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.AgwAccount"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfSpFee"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.GwGateconninfo"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%//清除页面缓存
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", 0);
//Jsp页面中获取session中的语言设置
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

Userdata userdata = (Userdata)request.getAttribute("userdata");
AgwAccount account = (AgwAccount)request.getAttribute("account");
LfSpFee spFee=null;
String sfID="0";
if(request.getAttribute("spFee")!=null){
	spFee=(LfSpFee)request.getAttribute("spFee");
	sfID=String.valueOf(spFee.getSfId());
} 
String uid = request.getParameter("uid");
String keyId = request.getParameter("keyId");

String staffname="", status="",userpassword="",userid="",accouttype="";
staffname = userdata.getStaffName();
status = userdata.getStatus().toString();
userpassword = userdata.getUserPassword();
userid = userdata.getUserId();
accouttype=userdata.getAccouttype().toString();

String SPACCID="",SPACCPWD="",SERVICETYPE="",FEEUSERTYPE="",SPID="",SPTYPE="",
	PTIP="",PTPORT="",SPIP="",SPPORT="",SPEEDLIMIT="",PROTOCOLCODE="",PROTOCOLPARAM="",
	ip1="",ip2="",ip3="",ip4="",ip5="",ip6="",ip7="",ip8="",FeeUrl="",SpFeeFlag="",ptnode="";

List<GwGateconninfo> gwgcinfos=null;
//如果是短信SP账号则取主备ip
if("1".equals(accouttype)){
	if(request.getAttribute("gwgcinfos")!=null){
		gwgcinfos=(List<GwGateconninfo>)request.getAttribute("gwgcinfos");
	}
}
//备用ip
String byId="";
if(account != null)
{
	SPACCID = account.getSpAccid();
	SPACCPWD = account.getSpAccPwd();
	SERVICETYPE = account.getServiceType();
	FEEUSERTYPE = account.getFeeUserType().toString();
	PTIP = account.getPtIp();
	if(null != PTIP &&!"".equals(PTIP.trim())){
		String[] ptips = PTIP.split("\\.");
		if(ptips.length>=4){
			ip1=ptips[0];
			ip2=ptips[1];
			ip3=ptips[2];
			ip4=ptips[3];
		}
	}
	PTPORT = account.getPtPort().toString();
	SPIP = account.getSpIp();
	if(null != SPIP &&!"".equals(SPIP.trim())){
		String[] spips = SPIP.split("\\.");
		if(spips.length>=4){
			ip5=spips[0];
			ip6=spips[1];
			ip7=spips[2];
			ip8=spips[3];
		}
	}
	SPPORT = account.getSpPort().toString();
	SPEEDLIMIT = account.getSpeedLimit().toString();
	PROTOCOLCODE = account.getProtocolCode().toString();
	PROTOCOLPARAM = account.getProtocolParam();
	String [] array = PROTOCOLPARAM.split(";");
	//备用ip获取
	for(int i=0;i<array.length;i++)
	{
		if(array[i].indexOf("backupip=")!=-1)
		{
			byId=array[i].replace("backupip=","");
		}
	}
	SPID = account.getSpId();
	SPTYPE = account.getSpType().toString();
	if(StaticValue.getCORPTYPE() ==0){
		if(spFee!=null){
			FeeUrl = spFee.getSpFeeUrl();
		}
	}else{
		FeeUrl=account.getFeeUrl();
	}
	SpFeeFlag = account.getSpFeeFlag().toString();
	ptnode=account.getPtNode();
}
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String rTitle = (String)request.getAttribute("rTitle");
String menuCode = titleMap.get(rTitle);
//判断是否网优通道
String wy=(String)request.getParameter("wy");
String txglFrame = skin.replace(commonPath, inheritPath);
//备运营商余额查看URL
List<String> bakUrlList = (List<String>)request.getAttribute("bakUrlList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN" >
<html>
	<head><%@include file="/common/common.jsp" %>
		<title>修改通道账户</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link
                href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link
                href="<%=commonPath%>/frame/frame3.0/skin/default/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">

	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/mwg_editUserdata1.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/mwg_editUserdata1.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	</head>
	<body id="mwg_editUserdata1">
		<div id="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.servmodule.txgl.biz.ViewParams.getPosition(langName, menuCode) %>
			<div class="rContent">
				<div class="titletop">
					<table class="titletop_table">
						<tr>
							<td class="titletop_td">
								<emp:message key='txgl_mwgateway_text_1' defVal='修改通道账户' fileName='mwadmin'/>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key='txgl_mwgateway_text_2' defVal='返回上一级' fileName='mwadmin'/></font>
							</td>
						</tr>
					</table>
				</div>
				<div id="detail_Info" class="detail_Info">
					<form action="<%=path %>/addUserdata.htm" name="form1"
						method="post">
						<input type="hidden" id="path" value="<%=path %>" />
						<%-- 发送速率不起作用，，默认填0 --%>
						<input type="hidden" id="speedLimit" class="input_bd speedLimit"
							 name="speedLimit" maxlength="8" value="0" />
						<table class="accountType_table">
							<tr>
								<td class="detail_Info_table_td">
									<span><emp:message key='txgl_mwgateway_text_3' defVal='账户类型：' fileName='mwadmin'/></span>
								</td>
								<td>
									<label id="daili2">
										<select name="bbcc" id="bbcc" disabled="disabled" class="input_bd bbcc"  >
											<option value="1" <%if("1".equals(accouttype)){ %> selected="selected" <%} %>>
												<emp:message key='txgl_mwgateway_text_4' defVal='短信通道账户' fileName='mwadmin'/>
											</option>
											<option value="2" <%if("2".equals(accouttype)){ %>
												selected="selected" <%} %>>
												<emp:message key='txgl_mwgateway_text_5' defVal='彩信通道账户' fileName='mwadmin'/>
											</option>
										</select>
									</label>
									<input type="hidden" name="accouttype" id="accouttype"
										value="<%=accouttype %>" />
									<%--  --%>
								</td>
							</tr>
						</table>
						<div class="div_bg tabCont">
							<label class="div_label">
								<emp:message key='txgl_mwgateway_text_6' defVal='EMP内部账户基本信息' fileName='mwadmin'/>
							</label>
							<a class="unfold">&nbsp;</a>
						</div>
						<div id="inner-ac-info" class="tabContent">
							<table>
								<tr>
									<td>
										<span><emp:message key='txgl_mwgateway_text_7' defVal='通道账户名称：' fileName='mwadmin'/></span>
									</td>
									<td>
										<label>
											<input maxlength="12" type="text" id="staffname"
												class="input_bd staffname"   name="staffname"
												value="<%=staffname.replace("&","&amp;").replace("\"","&quot;") %>"
												onkeydown="if(event.keyCode==51)event.returnValue = false;"
												disabled="disabled" />
											&nbsp;
											<font color="red">*</font>
										</label>
										<span class="tips"><emp:message key='txgl_mwgateway_text_8' defVal='自定义通道账户名称，便于识别' fileName='mwadmin'/></span>
									</td>
								</tr>
								<tr>
									<td class="detail_Info_table_td">
										<span><emp:message key='txgl_mwgateway_text_9' defVal='通道账号：' fileName='mwadmin'/></span>
									</td>
									<td>
										<label><%=userid %><input type="hidden"
												value="<%=userid %>" id="userid" />
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<span><emp:message key='txgl_mwgateway_text_10' defVal='账户密码：' fileName='mwadmin'/></span>
									</td>
									<td>
										<label>
											<input type="password" maxlength="32" id="userpassword"
												class="input_bd userpassword"  name="userpassword"
												value="<%=userpassword.replace("&","&amp;").replace("\"","&quot;") %>"
												onkeypress="if(event.keyCode==32) return false;"
												disabled="disabled" />
											&nbsp;
											<font color="red">*</font>
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<span><emp:message key='txgl_mwgateway_text_11' defVal='账户状态：' fileName='mwadmin'/></span>
									</td>
									<td>
										<label>
											<select name="status" id="status"  
												class="input_bd status" disabled="disabled">
												<option value="0">
													<emp:message key='txgl_mwgateway_text_12' defVal='已激活' fileName='mwadmin'/>
												</option>
												<option value="1" <%="1".equals(status)?"selected":"" %>>
													<emp:message key='txgl_mwgateway_text_13' defVal='已失效' fileName='mwadmin'/>
												</option>
											</select>
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<span><emp:message key='txgl_mwgateway_text_14' defVal='EMP网关IP地址及端口：' fileName='mwadmin'/></span>
									</td>
									<td>
										<div id="ptIp">
											<input type=text name=ip1 id="ip1" maxlength=3 class=a3 value="<%=ip1 %>" onkeyup="mask(this,event)" onbeforepaste=mask_c() disabled="disabled">
											.
											<input type=text name=ip2 id="ip2" maxlength=3 class=a3 value="<%=ip2 %>" onkeyup="mask(this,event)" onbeforepaste=mask_c() disabled="disabled">
											.
											<input type=text name=ip3 id="ip3" maxlength=3 class=a3 value="<%=ip3 %>" onkeyup="mask(this,event)" onbeforepaste=mask_c() disabled="disabled">
											.
											<input type=text name=ip4 id="ip4" maxlength=3 class=a3 value="<%=ip4 %>" onkeyup="mask(this,event)" onbeforepaste=mask_c() disabled="disabled">
										</div>
										&nbsp; ：
										<input type="text" class="input_bd ptPort"  
											id="ptPort" name="ptPort" placeholder="<emp:message key='txgl_mwgateway_text_15' defVal='端口号' fileName='mwadmin'/>" maxlength="5"
											value="<%=PTPORT.replace("&","&amp;").replace("\"","&quot;") %>"
											onblur="validatePort(this)"
											onkeyup="if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')"
											disabled="disabled" />
										&nbsp;
										<font color="red" class="xing_font">*</font>
										<span class="tips"><emp:message key='txgl_mwgateway_text_16' defVal='EMP网关所在服务器IP及通讯端口' fileName='mwadmin'/></span>
										<%--<a class="cluster-add" id="notadd">添加集群节点</a>
										--%><input type="hidden" id="cluster-info" value="<%=ptnode %>" />
									</td>
								</tr>
							</table>
						</div>

						<div class="div_bg tabCont">
							<label class="div_label">
								<emp:message key='txgl_mwgateway_text_17' defVal='运营商接入账户基本信息' fileName='mwadmin'/>
							</label>
							<a class="unfold">&nbsp;</a>
						</div>
						<div class="tabContent">
							<div id="yysCont">
								<table id="yysIP" class="temp_yys">
									<tr>
										<td class="detail_Info_table_td">
											<span><emp:message key='txgl_mwgateway_text_18' defVal='运营商账户ID：' fileName='mwadmin'/></span>
										</td>
										<td>
											<label>
												<input
													value="<%=SPACCID.replace("&","&amp;").replace("\"","&quot;") %>"
													type="text" id="SPACCID" name="SPACCID" maxlength="32"
													class="input_bd SPACCID"  
													onkeyup="if(value != value.replace(/[^\w]/g,'')) value = value.replace(/[^\w]/g,'')"
													disabled="disabled" />
												&nbsp;
												<font color="red">*</font>
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<span><emp:message key='txgl_mwgateway_text_19' defVal='运营商账户密码：' fileName='mwadmin'/></span>
										</td>
										<td>
											<label>
												<input type="password" id="SPACCPWD" name="SPACCPWD"
													class="input_bd SPACCPWD"   maxlength="6"
													value="<%=SPACCPWD.replace("&","&amp;").replace("\"","&quot;") %>"
													onkeypress="if(event.keyCode==32) return false;"
													disabled="disabled" />
												&nbsp;
												<font color="red">*</font>
											</label>
										</td>
									</tr>
									<tr>
										<td>
											<span class="iptext"><emp:message key='txgl_mwgateway_text_20' defVal='运营商IP地址及端口1：' fileName='mwadmin'/></span>
										</td>
										<td>
											<select id="linklevel" <%if("1".equals(wy)){ %> disabled="disabled" <%} %> name="linklevel" class="input_bd">
												<option value="0">
													<emp:message key='txgl_mwgateway_text_21' defVal='主用' fileName='mwadmin'/>
												</option>
											</select>
											<div id="spip">
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name=ip5 id="ip5" maxlength=3 class=a3 value="<%=ip5 %>" onkeyup="mask(this,event)" onbeforepaste=mask_c()>
												.
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name=ip6 id="ip6" maxlength=3 class=a3 value="<%=ip6 %>" onkeyup="mask(this,event)" onbeforepaste=mask_c()>
												.
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name=ip7 id="ip7" maxlength=3 class=a3 value="<%=ip7 %>" onkeyup="mask(this,event)" onbeforepaste=mask_c()>
												.
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name=ip8 id="ip8" maxlength=3 class=a3 value="<%=ip8 %>" onkeyup="mask(this,event)" onbeforepaste=mask_c()>
											</div>
											&nbsp;：
											<input type="text" <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  class="input_bd spPort"  
												id="spPort" placeholder="<emp:message key='txgl_mwgateway_text_15' defVal='端口号' fileName='mwadmin'/>" name="spPort"
												value="<%=SPPORT.replace("&","&amp;").replace("\"","&quot;") %>"
												maxlength="5" onblur="validatePort(this)"
												onkeyup="if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')" />
											&nbsp;
											<select name="linkstatus" <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  class="input_bd">
												<option value="0">
													<emp:message key='txgl_mwgateway_text_22' defVal='已启用' fileName='mwadmin'/>
												</option>
											</select>
											<font color="red" class="xing_font">*</font>
											<%if(!"1".equals(wy)){ %>
											<a href="javascript:void(0)" onclick="addBackIp()"
												class="addbackup"><emp:message key='txgl_mwgateway_text_23' defVal='添加' fileName='mwadmin'/></a>
											<%} %>
												&nbsp;&nbsp;
											<a href="javascript:void(0)" onclick="doadd()"><emp:message key='txgl_mwgateway_text_24' defVal='高级配置' fileName='mwadmin'/></a>
											<span class="tips"><emp:message key='txgl_mwgateway_text_25' defVal='运营商网关所在主用服务器IP及通讯端口' fileName='mwadmin'/></span>
										</td>
									</tr>
								</table>

								<%
								Pattern p=Pattern.compile("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5]):\\d{1,5}$");
								String singmutitype="";
								String keepconn="";
								String linkcnt="";
								String reconncnt="";
								String relogincnt="";
								String switchmainip="";
								int zynum=0;
								int bynum=0;
								//短信SP账号
								if("1".equals(accouttype)&&gwgcinfos!=null&&gwgcinfos.size()>0){
									int nonum=1;
									for(int c=0;c<gwgcinfos.size();c++)
									{
										GwGateconninfo gwgcinfo=gwgcinfos.get(c);
										if(gwgcinfo!=null){
											if(singmutitype.length()==0){
												if(gwgcinfo.getKeepconn()==1){
													singmutitype="0";
												}else{
													singmutitype="10";
												}
											}
											if(keepconn.length()==0){
												keepconn=gwgcinfo.getKeepconn()+"";
											}
											if(linkcnt.length()==0){
												linkcnt=gwgcinfo.getLinkcnt()+"";
											}
											if(reconncnt.length()==0){
												reconncnt=gwgcinfo.getReconncnt()+"";
											}
											if(relogincnt.length()==0){
												relogincnt=gwgcinfo.getRelogincnt()+"";
											}
											if(switchmainip.length()==0){
												switchmainip=gwgcinfo.getSwitchmainip()+"";
											}
											
											
											String ipportstr=gwgcinfo.getIp()+":"+gwgcinfo.getPort();
											Matcher m = p.matcher(ipportstr);
											//判断是否匹配ip+端口的模式
											if(m.matches())
											{
												if("0".equals(gwgcinfo.getLinklevel()+"")){
													zynum++;
												}else if("1".equals(gwgcinfo.getLinklevel()+"")){
													bynum++;
												}
												String [] ipPortArr=ipportstr.split(":");
												String port= ipPortArr[1];
												String ip=ipPortArr[0];
												//过滤调已显示出来的主用IP端口
												if(SPIP.equals(ip)&&SPPORT.equals(port)){
												}else{
													String [] ipStrArr=ip.split("\\.");
													String index="";
													if(c!=0)
													{
														index = c+"";
													}
													nonum++;
										%>
								<table class="temp_yys">
									<tr>
										<td class="detail_Info_table_td">
											<span class="iptext"><emp:message key='txgl_mwgateway_text_26' defVal='运营商IP地址及端口' fileName='mwadmin'/><%=nonum %>：</span>
										</td>
										<td>
											<select name="linklevel<%=c %>" <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  class="input_bd bklevel llever">
												<option value="0"
													<%if("0".equals(gwgcinfo.getLinklevel()+"")){ %>
													selected="selected" <%} %>>
													<emp:message key='txgl_mwgateway_text_21' defVal='主用' fileName='mwadmin'/>
												</option>
												<option value="1"
													<%if("1".equals(gwgcinfo.getLinklevel()+"")){ %>
													selected="selected" <%} %>>
													<emp:message key='txgl_mwgateway_text_27' defVal='备用' fileName='mwadmin'/>
												</option>
											</select>
											<div class="backup">
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[0] %>" onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
												.
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[1] %>" onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
												.
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[2] %>" onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
												.
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[3] %>" onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
											</div>
											&nbsp;：
											<input type="text" <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="spPort<%=c %>" placeholder="<emp:message key='txgl_mwgateway_text_15' defVal='端口号' fileName='mwadmin'/>"
												maxlength="5" value="<%=port %>" class="input_bd prot_by port_input"
												onkeyup="if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')"
												onblur="validatePort(this)" />
											&nbsp;
											<select name="linkstatus<%=c %>" <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  class="input_bd lstatus">
												<option value="0"
													<%if("0".equals(gwgcinfo.getLinkstatus()+"")){ %>
													selected="selected" <%} %>>
													<emp:message key='txgl_mwgateway_text_29' defVal='已启用' fileName='mwadmin'/>
												</option>
												<option value="2"
													<%if("2".equals(gwgcinfo.getLinkstatus()+"")){ %>
													selected="selected" <%} %>>
													<emp:message key='txgl_mwgateway_text_30' defVal='已停用' fileName='mwadmin'/>
												</option>
											</select>
											<font color="red" class="xing_font">*</font>
											<%if(!"1".equals(wy)){ %>
											<span class="handle"> <a class="addbackup"
												onclick="addNextBackIp(this)" href="javascript:void(0)"><emp:message key='txgl_mwgateway_text_23' defVal='添加' fileName='mwadmin'/></a>
												| <a class="remove_handle" onclick="removeHandle(this)"
												href="javascript:void(0)"><emp:message key='txgl_mwgateway_text_28' defVal='删除' fileName='mwadmin'/></a> </span>
											<%} %>
										</td>
									</tr>
								</table>
								<%
												}
											}
										}
									}
								}else{
									//如果数据库未存主备  则默认是单链路
									singmutitype="10";
									keepconn="0";
									linkcnt="1";
									reconncnt="5";
									relogincnt="5";
									switchmainip="1";
									zynum=1;
									String [] ipArray = byId.split(",");
									for(int c=0;c<ipArray.length;c++)
									{
										Matcher m = p.matcher(ipArray[c]);
										//判断是否匹配ip+端口的模式
										if(m.matches())
										{
											bynum++;
											String [] ipPortArr=ipArray[c].split(":");
											String port= ipPortArr[1];
											String ip=ipPortArr[0];
											String [] ipStrArr=ip.split("\\.");
											String index="";
											if(c!=0)
											{
												index = c+"";
											}
								%>
								<table class="temp_yys">
									<tr>
										<td class="detail_Info_table_td">
											<span class="iptext"><emp:message key='txgl_mwgateway_text_26' defVal='运营商IP地址及端口' fileName='mwadmin'/><%=c+2 %>：</span>
										</td>
										<td>
											<select name="linklevel<%=c %>" <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  class="input_bd bklevel llever">
												<option value="0">
													<emp:message key='txgl_mwgateway_text_21' defVal='主用' fileName='mwadmin'/>
												</option>
												<option value="1" selected="selected">
													<emp:message key='txgl_mwgateway_text_27' defVal='备用' fileName='mwadmin'/>
												</option>
											</select>
											<div class="backup">
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="ip<%=index %>[]" maxlength=3
													class="a3" value="<%=ipStrArr[0] %>"
													onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
												.
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="ip<%=index %>[]" maxlength=3
													class="a3" value="<%=ipStrArr[1] %>"
													onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
												.
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="ip<%=index %>[]" maxlength=3
													class="a3" value="<%=ipStrArr[2] %>"
													onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
												.
												<input type=text <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="ip<%=index %>[]" maxlength=3
													class="a3" value="<%=ipStrArr[3] %>"
													onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
											</div>
											&nbsp;：
											<input type="text" <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="spPort<%=c %>" placeholder="<emp:message key='txgl_mwgateway_text_15' defVal='端口号' fileName='mwadmin'/>"
												maxlength="5" value="<%=port %>" class="input_bd prot_by port_input"
												onkeyup="if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')"
												onblur="validatePort(this)" />
											&nbsp;
											<select name="linkstatus<%=c %>" <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  class="input_bd lstatus">
												<option value="0">
													<emp:message key='txgl_mwgateway_text_29' defVal='已启用' fileName='mwadmin'/>
												</option>
												<option value="2">
													<emp:message key='txgl_mwgateway_text_30' defVal='已停用' fileName='mwadmin'/>
												</option>
											</select>
											<font color="red" class="xing_font">*</font>
											 <%if(!"1".equals(wy)){ %>
											<span class="handle"> <a class="addbackup"
												onclick="addNextBackIp(this)" href="javascript:void(0)"><emp:message key='txgl_mwgateway_text_23' defVal='添加' fileName='mwadmin'/></a>
												| <a class="remove_handle" onclick="removeHandle(this)"
												href="javascript:void(0)"><emp:message key='txgl_mwgateway_text_28' defVal='删除' fileName='mwadmin'/></a> </span>
											<%} %>
										</td>
									</tr>
								</table>
								<%
										}
									}
								}
								%>
							</div>
							<table>
								<tr>
									<td class="detail_Info_table_td">
										<input type="hidden" name="zynum" id="zynum" value="<%=zynum %>"/>
										<input type="hidden" name="bynum" id="bynum" value="<%=bynum %>"/>
										<span><emp:message key='txgl_mwgateway_text_31' defVal='业务类型：' fileName='mwadmin'/></span>
									</td>
									<td>
										<label>
											<input
												value="<%=SERVICETYPE.replace("&","&amp;").replace("\"","&quot;") %>"
												class="input_bd serviceType"  type="text"
												id="serviceType" name="serviceType" maxlength="32"
												onkeyup="if(value != value.replace(/[^\w]/g,'')) value = value.replace(/[^\w]/g,'')"
												disabled="disabled" />
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<span><emp:message key='txgl_mwgateway_text_32' defVal='SP企业代码：' fileName='mwadmin'/></span>
									</td>
									<td>
										<label>
											<input type="text" id="spid" name="spid" maxlength="32"
												onkeyup="if(value != value.replace(/[^\w]/g,'')) value = value.replace(/[^\w]/g,'')"
												class="input_bd spid"  
												value="<%=SPID.replace("&","&amp;").replace("\"","&quot;") %>"
												disabled="disabled" />
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<span><emp:message key='txgl_mwgateway_text_33' defVal='计费用户类型：' fileName='mwadmin'/></span>
									</td>
									<td>
										<label>
											<select name="feeUserType" id="feeUserType" class="input_bd feeUserType"
												 disabled="disabled">
												<option value="0">
													<emp:message key='txgl_mwgateway_text_34' defVal='对目的终端MSISDN计费' fileName='mwadmin'/>
												</option>
												<option value="1"
													<%="1".equals(FEEUSERTYPE)?"selected":"" %>>
													<emp:message key='txgl_mwgateway_text_35' defVal='对源终端MSISDN计费' fileName='mwadmin'/>
												</option>
												<option value="2"
													<%="2".equals(FEEUSERTYPE)?"selected":"" %>>
													<emp:message key='txgl_mwgateway_text_36' defVal='对SP计费' fileName='mwadmin'/>
												</option>
												<option value="3"
													<%="3".equals(FEEUSERTYPE)?"selected":"" %>>
													<emp:message key='txgl_mwgateway_text_37' defVal='该字段无效' fileName='mwadmin'/>
												</option>
											</select>
										</label>
									</td>
								</tr>


								<%--
	                      <tr>
	                        <td><span>发送速率：</span></td>
	                        <td>
	                        <label><input type="hidden" id="speedLimit"  class="input_bd" style="width:260px;" name="speedLimit" maxlength="8" value ="0" />&nbsp;<font color="red">*</font></label>
	                        </td>
	                     </tr>
                         --%>
								<tr>
									<td>
										<span><emp:message key='txgl_mwgateway_text_38' defVal='技术合作商：' fileName='mwadmin'/></span>
									</td>
									<td>
										<label>
											<select name="spType" id="spType" class="input_bd spType"
												onchange="chty()"  disabled="disabled">
												<option value="0">
													<emp:message key='txgl_mwgateway_text_39' defVal='梦网科技' fileName='mwadmin'/>
												</option>
												<option value="10" <%="10".equals(SPTYPE)?"selected":"" %>>
													<emp:message key='txgl_mwgateway_text_40' defVal='其他' fileName='mwadmin'/>
												</option>
											</select>
											<input type="hidden" id="singmutitype" name="singmutitype"
												value="<%=singmutitype %>" />
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<span><emp:message key='txgl_mwgateway_text_41' defVal='通讯协议：' fileName='mwadmin'/></span>
									</td>
									<td>
										<label>
											<select name="protocolCode" id="protocolCode"
												class="input_bd protocolCode"  disabled="disabled">
											</select>
										</label>
									</td>
								</tr>
								<tr>
									<td>
										<span><emp:message key='txgl_mwgateway_text_42' defVal='通讯协议参数：' fileName='mwadmin'/></span>
									</td>
									<td>
										<input class="input_bd protocolParam"  type="text"
											maxlength="512"
											value="<%=PROTOCOLPARAM.replace("&","&amp;").replace("\"","&quot;") %>"
											id="protocolParam" disabled="disabled" />
									</td>
								</tr>
								<tr class="ch_ty">
									<td>
										<span><emp:message key='txgl_mwgateway_text_43' defVal='账户计费类型：' fileName='mwadmin'/></span>
									</td>
									<td>
										<label>
											<select name="spFeeFlag" id="spFeeFlag" class="input_bd spFeeFlag"
												 disabled="disabled">
												<option value="2">
													<emp:message key='txgl_mwgateway_text_44' defVal='后付费' fileName='mwadmin'/>
												</option>
												<option value="1" <%="1".equals(SpFeeFlag)?"selected":"" %>>
													<emp:message key='txgl_mwgateway_text_45' defVal='预付费' fileName='mwadmin'/>
												</option>
											</select>
										</label>
										&nbsp;
										<span class="tips"><emp:message key='txgl_mwgateway_text_46' defVal='运营商账户计费类型' fileName='mwadmin'/></span>
									</td>
								</tr>
								<tr class="ch_ty">
									<td>
										<span><emp:message key='txgl_mwgateway_text_47' defVal='余额查看URL：' fileName='mwadmin'/></span>
									</td>
									<td>
										<input class="input_bd feeUrl"  type="text"
											value="<%=FeeUrl==null?"":FeeUrl.trim() %>" id="feeUrl"
											maxlength="512" disabled="disabled" />
										&nbsp;
										<span class="tips"><emp:message key='txgl_mwgateway_text_48' defVal='主运营商余额查看URL链接地址' fileName='mwadmin'/></span>
									</td>
								</tr>

								<%
									int count = bakUrlList.size();
									for(int i=1; i<5; i++)
									{
										String id = "bakFeeUrl"+i;
										String value = "";
										//if(FeeUrl != null && FeeUrl.trim().length() > 0)
										//{
											if(i - 1 < count)
											{
												value = bakUrlList.get(i-1);
											}
										//}
								%>
								<tr class="ch_ty">
									<td>
										<span></span>
									</td>
									<td>
										<input class="input_bd id_input"  type="text"
											value="<%=value%>" id="<%=id%>" maxlength="512"
											disabled="disabled" />
										&nbsp;
										<span class="tips"><emp:message key='txgl_mwgateway_text_49' defVal='备运营商余额查看URL链接地址' fileName='mwadmin'/><%=" "+i%></span>
									</td>
								</tr>
								<%
									}
								%>
							</table>
						</div>

						<table class="passgaeInfo_table">
							<thead>
								<tr id="passgaeInfo"></tr>
								<tr align="center">
									<td colspan="4" id="btn">
										<%if(!"1".equals(wy)){ %>
										<input type="button" onclick="submitForm()" id="btnSsu"
											value="<emp:message key='txgl_mwgateway_text_50' defVal='保存' fileName='mwadmin'/>" class="btnClass5 mr23" />
										<%} %>
										<input type="button" id="btnSca" onclick="javascript:back()"
											value="<emp:message key='txgl_mwgateway_text_51' defVal='返回' fileName='mwadmin'/>" class="btnClass6" />
									</td>
								</tr>
							</thead>
						</table>
					</form>
				</div>
			</div>
			<div id="template" class="template">
				<table class="temp_yys">
					<tr>
						<td class="yysipdz_td">
							<span class="iptext"><emp:message key='txgl_mwgateway_text_52' defVal='运营商IP地址及端口1：' fileName='mwadmin'/></span>
						</td>
						<td>
							<select name="linklevel0" class="input_bd bklevel llever">
								<option value="0">
									<emp:message key='txgl_mwgateway_text_21' defVal='主用' fileName='mwadmin'/>
								</option>
								<option value="1">
									<emp:message key='txgl_mwgateway_text_27' defVal='备用' fileName='mwadmin'/>
								</option>
							</select>
							<div class="backup">
								<input type=text name="ip[]" maxlength=3 class="a3"
									onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
								.
								<input type=text name="ip[]" maxlength=3 class="a3"
									onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
								.
								<input type=text name="ip[]" maxlength=3 class="a3"
									onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
								.
								<input type=text name="ip[]" maxlength=3 class="a3"
									onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
							</div>
							&nbsp;：
							<input type="text" name="spPort0" placeholder="<emp:message key='txgl_mwgateway_text_15' defVal='端口号' fileName='mwadmin'/>" maxlength="5"
								value="" class="input_bd prot_by spPort0_input"  
								onkeyup="if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')"
								onblur="validatePort(this)" />
							&nbsp;
							<select name="linkstatus0" class="input_bd lstatus">
								<option value="0">
									<emp:message key='txgl_mwgateway_text_29' defVal='已启用' fileName='mwadmin'/>
								</option>
								<option value="2">
									<emp:message key='txgl_mwgateway_text_30' defVal='已停用' fileName='mwadmin'/>
								</option>
							</select>
							<font color="red" class="xing_font">*</font>
							<%if(!"1".equals(wy)){ %>
							<span class="handle"> <a class="addbackup"
								onclick="addNextBackIp(this)" href="javascript:void(0)"><emp:message key='txgl_mwgateway_text_23' defVal='添加' fileName='mwadmin'/></a> |
								<a class="remove_handle" onclick="removeHandle(this)"
								href="javascript:void(0)"><emp:message key='txgl_mwgateway_text_28' defVal='删除' fileName='mwadmin'/></a> </span>
							<%} %>
						</td>
					</tr>
				</table>
			</div>

			<div id="advanceconf" title="<emp:message key='txgl_mwgateway_text_53' defVal='高级配置' fileName='mwadmin'/>"
				class="advanceconf">
				<div class="advanceconf_down_div">
					<table class="ljfs_table">
						<tr>
							<td class="ljfs_td">
								<emp:message key='txgl_mwgateway_text_54' defVal='连接方式：' fileName='mwadmin'/>
								<input type="hidden" id="keepconnbak" value="<%=keepconn %>" />
							</td>
							<td align="left">
								&nbsp;
								<input type="radio" <%if("1".equals(wy)){ %> disabled="disabled" <%} %> name="keepconn" value="1"
									<% if("1".equals(keepconn)){ %> checked="checked" <%} %>
									onclick="changeKeepConn(this.value)"  />
								<emp:message key='txgl_mwgateway_text_55' defVal='多链路多连接 ' fileName='mwadmin'/>&nbsp;&nbsp;&nbsp;
								<input type="radio" <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="keepconn" value="0"
									<% if("0".equals(keepconn)){ %> checked="checked" <%} %>
									onclick="changeKeepConn(this.value)" />
								<emp:message key='txgl_mwgateway_text_56' defVal='单链路多连接' fileName='mwadmin'/>
							</td>
						</tr>
						<tr>
							<td class="linkcntbak_td">
								<emp:message key='txgl_mwgateway_text_57' defVal='连接数：' fileName='mwadmin'/>
								<input type="hidden" id="linkcntbak" value="<%=linkcnt %>" />
							</td>
							<td align="left">
								<input type="text" <%if("1".equals(wy)){ %> disabled="disabled" <%} %>  name="linkcnt" id="linkcnt" class="input_bd"
									value="<%=linkcnt %>" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d]/g,'')"/>
							</td>
						</tr>
						<tr id="trmemostart" <%if("1".equals(keepconn)){ %>
							class="trmemostart" <%} %>>
							<td colspan="2" class="trmemostart_td">
								<p>
									————————<emp:message key='txgl_mwgateway_text_58' defVal='主备切换条件' fileName='mwadmin'/>————————
								</p>
							</td>
						</tr>
						<tr id="trreconncnt" <%if("1".equals(keepconn)){ %>
							class="trreconncnt" <%} %>>
							<td class="reconncntbak_td">
								<emp:message key='txgl_mwgateway_text_59' defVal='重连失败(次)：' fileName='mwadmin'/>
								<input type="hidden" id="reconncntbak" value="<%=reconncnt %>" />
							</td>
							<td align="left">
								<input type="text" <%if("1".equals(wy)){ %> disabled="disabled" <%} %> name="reconncnt" id="reconncnt"
									class="input_bd" value="<%=reconncnt %>" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d]/g,'')" />
							</td>
						</tr>
						<tr id="trrelogincnt" <%if("1".equals(keepconn)){ %>
							class="trrelogincnt" <%} %>>
							<td class="relogincntbak_td">
								<emp:message key='txgl_mwgateway_text_60' defVal='登录失败(次)：' fileName='mwadmin'/>
								<input type="hidden" id="relogincntbak" value="<%=relogincnt %>" />
							</td>
							<td align="left">
								<input type="text" <%if("1".equals(wy)){ %> disabled="disabled" <%} %> name="relogincnt" id="relogincnt"
									class="input_bd" value="<%=relogincnt %>" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d]/g,'')" />
							</td>
						</tr>
						<tr id="trmemoend" <%if("1".equals(keepconn)){ %>
							class="trmemoend" <%} %>>
							<td colspan="2" class="trmemoend_td">
								<p>
									—————————————————————————————
								</p>
							</td>
						</tr>
						<tr id="trswitchmainip" <%if("1".equals(keepconn)){ %>
							class="trswitchmainip" <%} %>>
							<td class="switchmainipbak_td">
								<emp:message key='txgl_mwgateway_text_61' defVal='备切主条件：' fileName='mwadmin'/>
								<input type="hidden" id="switchmainipbak" value="<%=switchmainip %>" />
							</td>
							<td align="left">
								&nbsp;
								<input type="radio" <%if("1".equals(wy)){ %> disabled="disabled" <%} %> name="switchmainip" value="1"
									<% if("1".equals(switchmainip)){ %> checked="checked" <%} %> />
								<emp:message key='txgl_mwgateway_text_62' defVal='主用链路恢复立即切回主用' fileName='mwadmin'/> &nbsp;&nbsp;&nbsp;
								<input type="radio" <%if("1".equals(wy)){ %> disabled="disabled" <%} %> name="switchmainip" value="0"
									<% if("0".equals(switchmainip)){ %> checked="checked" <%} %> />
								<emp:message key='txgl_mwgateway_text_63' defVal='备用链路正常不切回主用' fileName='mwadmin'/>
							</td>
						</tr>
					</table>
				</div>
				<div class="qd_div">
					<center>
						<%if(!"1".equals(wy)){ %>
						<input id="kwok" class="btnClass5 mr23" type="button" value="<emp:message key='txgl_mwgateway_text_64' defVal='确  定' fileName='mwadmin'/>"
							onclick="javascript:btok();" />
						<%}%>
						<input id="kwc" onclick="javascript:btcancel();" class="btnClass6"
							type="button" value="<emp:message key='txgl_mwgateway_text_65' defVal='取  消' fileName='mwadmin'/>" />
						<br />
					</center>
				</div>
			</div>

			<%--end round_content--%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
		<%--end warp--%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/widget/jqueryui/myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script language="javascript"
			src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"
			type="text/javascript"></script>
		<script language="javascript" type="text/javascript"
			src="<%=iPath%>/js/mwip.js?V=<%=StaticValue.getJspImpVersion()%>"
			type="text/javascript"></script>
		<script language="javascript" type="text/javascript"
			src="<%=iPath%>/js/mwg_userdata.js?V=<%=StaticValue.getJspImpVersion()%>"
			type="text/javascript"></script>
		<script>
		var times = <%=System.currentTimeMillis()%>;
		$(document).ready(function(){
			var h=$(document.body).width()-$(document.body).width()*0.2;
			$("#mwg_editUserdata1").css("height",h);
			$("#notadd").click(function(){
					return false;
			});
			
			$("#protocolCode").load("<%=path%>/mwg_userdata.htm?method=getPrTmplOption&accouttype="+$("#accouttype").val()+"&times="+times,{},
				function(){
					setTimeout(function() {
					$("#protocolCode").find(" >option[value='<%=PROTOCOLCODE%>']").attr("selected",true);
					}, 1);
					//$("#protocolCode").val('<%=PROTOCOLCODE%>");
				}
			);
			$("#protocolCode").change(function(){
				var protocolCode  = $.trim($("#protocolCode").val()); 
				if(protocolCode != ""){
					$.post("<%=path%>/mwg_userdata.htm?method=getPrTmplContent&times="+times,{protocolCode : protocolCode},
						function(result){
							$("#protocolParam").val(result);			
						}
					);
				}else{
					$("#protocolParam").val("");
				}
			});
			$('.tabCont').click(function(){
				var oFold=$(this).find('a'),
					oTabContent=$(this).next('.tabContent');
				if(oTabContent.is(':visible')){
					oTabContent.hide();
					oFold.attr('class','fold');
				}else{
					oTabContent.show();
					oFold.attr('class','unfold');
				}
				
			});
		});
		function submitForm(){
            var yysSize=$('#yysCont .temp_yys').size();
			//备用ip，格式192.169.1.130:8080,192.169.1.131:8080
			var byIp=""
			//包含主备信息的ip端口字符串 格式 0:192.169.1.30:8080,1:192.169.1.131:8080
			var zbIP="";
			//包含主备信息的ip端口字符串 格式192.169.1.30:8080,192.169.1.131:8080
			var allipport="";
			//主用个数
			var zindex=0;
			//备用个数
			var bindex=0;
			for(var i=0;i<yysSize;i++)
			{
				var index=i+"";
				if(i==0){index=""};
				
				var $tr = $("#yysCont .temp_yys:eq("+i+")");
				var linklevel=$tr.find("select[name^='linklevel']").val();
				if(linklevel=="")
				{
                    /*运营商IP地址及端口  主用/备用必须选！*/
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_3")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_4"));
					return;
				}
				var ip="";
				var count=0;
				$tr.find("input[name^='ip']").each(function(){
					if(count<4)
					{
						ip=ip+$(this).val()+".";
					}
					count++;
				});
				ip=ip.substr(0,ip.lastIndexOf("."));
				//验证端口号是否合法
				if(checkIP2(ip)){
                    /*运营商IP地址/端口  IP不合法！*/
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_5")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_6"));
					 return;	
				}
				var port=$tr.find("input[name^='spPort']").val();
				if(port=="")
				{
                    /*运营商IP地址/端口  IP不合法！*/
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_5")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_6"));
					return;
				}
				if(!isPort(port)){
                    /*运营商IP地址/端口  端口不合法！*/
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_5")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_8"));
					return;
				}

				var linkstatus=$tr.find("select[name^='linkstatus']").val();
				if(linkstatus=="")
				{
				    /*运营商IP地址及端口   状态必须选！*/
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_5")+(i+1)+getJsLocaleMessage("txgl","txgl_js_userdata_6"));
					return;
				}
				
				if(linklevel=="0")
				{
					zindex++;
					if(zindex>1&&$("input[name='keepconn']:checked").val()!=1){
                        //alert("运营商IP地址及端口是单链路多连接，主用只能有一个！");
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_11"));
						return;
					}
					if(zindex>5&&$("input[name='keepconn']:checked").val()==1){
                        //alert("运营商IP地址及端口是多链路多连接，主用不能超过五个！");
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_12"));
						return;
					}
				}else if(linklevel=="1"){
					bindex++;					
					if(bindex>4&&$("input[name='keepconn']:checked").val()!=1){
                        //alert("运营商IP地址及端口是单链路多连接，备用只能有四个！");
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_13"));
						return;
					}
					if(bindex>5&&$("input[name='keepconn']:checked").val()==1){
                        //alert("运营商IP地址及端口是多链路多连接，备用不能超过五个！");
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_14"));
						return;
					}
				}else{
                    //alert("运营商IP地址及端口"+(i+1)+"主用/备用必须选！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_3")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_4"));
					return;
				}
				
				if(linklevel=="1"){
					byIp=byIp+ip+":"+port+",";
				}
				allipport=allipport+ip+":"+port+",";
				zbIP=zbIP+linklevel+":"+ip+":"+port+":"+linkstatus+",";
			}
			
			var userid = $("#userid").val();
			var accouttype=$.trim($("#accouttype").val());
			var spIp  = $.trim($("#ip5").val())+'.'+$.trim($("#ip6").val())+'.'+
				$.trim($("#ip7").val())+'.'+$.trim($("#ip8").val()); 
			var spPort  = $.trim($("#spPort").val());
			var protocolParam  = $.trim($("#protocolParam").val());
			var spid  = $.trim($("#spid").val());
			var spType = $("#spType").val();
			var feeUrl = $.trim($("#feeUrl").val());
			var spFeeFlag = $.trim($("#spFeeFlag").val());
			var accountType = $.trim($("#bbcc").val());
            if(!checkIPAddr(spIp+':'+spPort,allipport))
            {
               return;
            }
            //网关集群地址数组
			$.post("<%=path%>/mwg_userdata.htm",{
				method : "edit",
				userid : userid,
				uid : <%=uid%>,
				keyId : '<%=keyId%>',
				accouttype:accouttype,
				spPort : spPort,
				spIp : spIp,
				protocolParam : protocolParam,
				spType : spType,
				accountType:accountType,
				byIp:byIp,
				zbIP:zbIP,
				sfID:<%=sfID%>
			},function(result){
					if(result == "true"){
                        /*保存成功！*/
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_10"));
						location=location;
					}else if(result == "error"){
                        /*保存失败！*/
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_11"));
						location=location;
					}else if(result == "false"){
                        /*保存失败！*/
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_11"));
						location=location;
					}
				});
		}

		function doadd()
		{
			//$('#keywored').val('');
			$("#advanceconf").css("display","block");
			$('#advanceconf').dialog({
				autoOpen: false,
				height: 420,
				width: 500,
				resizable:false,
				modal:true
			});
			$('#advanceconf').dialog('open');	
		}


		function btok(){
			var keepconn=$("input[name='keepconn']:checked").val();
			if(keepconn==null||keepconn==''){
                /*连接方式必选*/
                alert(getJsLocaleMessage("txgl","txgl_js_userdata_7"));
				return;
			}
			if(($("#zynum").val()>1||$("#bynum").val()>4)&&keepconn!=1){
                /*运营商IP地址及端口是单链路多连接，主用最多一个，备用最多四个，请先按要求保存好主备ip端口信息！*/
                alert(getJsLocaleMessage("txgl","txgl_js_userdata_8"));
				return;
			}
			if(($("#zynum").val()>5||$("#bynum").val()>5)&&keepconn==1){
                /*运营商IP地址及端口是多链路多连接，主用最多五个,备用最多五个，请先按要求保存好主备ip端口信息！*/
                alert(getJsLocaleMessage("txgl","txgl_js_userdata_9"));
				return;
			}

			var linkcnt=$("#linkcnt").val();
			if(!isNumber(linkcnt)){
                /*连接数必须是数字*/
                alert(getJsLocaleMessage("txgl","txgl_js_userdata_10"));
				return;
			}
			if(linkcnt>10){
                /*连接数不能超过10*/
                alert(getJsLocaleMessage("txgl","txgl_js_userdata_11"));
				return;
			}
			var userid = $("#userid").val();
			var accouttype=$.trim($("#accouttype").val());
			var switchmainip=$("input[name='switchmainip']:checked").val();
			if(switchmainip==null||switchmainip==''){
                /*备切主条件必选*/
                alert(getJsLocaleMessage("txgl","txgl_js_userdata_12"));
				return;
			}
			if(keepconn=='0'){
				var reconncnt=$("#reconncnt").val();
				if(!isNumber(reconncnt)){
                    /*重连失败数必须是数字*/
                    alert(getJsLocaleMessage("txgl","txgl_js_userdata_13"));
					return;
				}
				if(reconncnt>100){
                    /*重连失败数不能超过100*/
                    alert(getJsLocaleMessage("txgl","txgl_js_userdata_14"));
					return;
				}
				var relogincnt=$("#relogincnt").val();
				if(!isNumber(relogincnt)){
                    /*登录失败数必须是数字*/
                    alert(getJsLocaleMessage("txgl","txgl_js_userdata_15"));
					return;
				}
				if(relogincnt>100){
                    /*登录失败数不能超过100*/
                    alert(getJsLocaleMessage("txgl","txgl_js_userdata_16"));
					return;
				}
				$.post("<%=path%>/mwg_userdata.htm",{
					method : "editAdvanceConf",
					userid : userid,
					keyId : '<%=keyId%>',
					accouttype:accouttype,
					keepconn : keepconn,
					linkcnt : linkcnt,
					switchmainip : switchmainip,
					reconncnt:reconncnt,
					relogincnt:relogincnt
				},function(result){
						if(result == "true"){
                            /*保存成功！*/
                            alert(getJsLocaleMessage("txgl","txgl_js_install_28"));
							$("#keepconnbak").val(keepconn); 
							$("#linkcntbak").val(linkcnt); 
							$("#reconncntbak").val(reconncnt);
							$("#relogincntbak").val(relogincnt);  
							$("#switchmainipbak").val(switchmainip);  
							if(!$('#advanceconf').is(":hidden"))
							{
							    $('#advanceconf').dialog('close');	
							}	
						}else if(result == "error"){
                            /*保存失败！*/
                            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_11"));
						}else if(result == "false"){
                            /*保存失败！*/
                            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_11"));
						}
					});
			}else{
			
			
				$.post("<%=path%>/mwg_userdata.htm",{
					method : "editAdvanceConf",
					userid : userid,
					keyId : '<%=keyId%>',
					accouttype:accouttype,
					keepconn : keepconn,
					linkcnt : linkcnt,
					switchmainip : switchmainip
				},function(result){
						if(result == "true"){
                            /*保存成功！*/
                            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_10"));
							$("#keepconnbak").val(keepconn); 
							$("#linkcntbak").val(linkcnt); 
							$("#switchmainipbak").val(switchmainip);  
							if(!$('#advanceconf').is(":hidden"))
							{
							    $('#advanceconf').dialog('close');	
							}			
						}else if(result == "error"){
                            /*保存失败！*/
                            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_11"));
						}else if(result == "false"){
                            /*保存失败！*/
                            alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_11"));
						}
					});
			}
			
		}

		
		//取消
		function btcancel()
		{
			//连接方式
			var keepconn=$("#keepconnbak").val();
			//连接数
			var linkcnt=$("#linkcntbak").val();
			//重连失败(次)
			var reconncnt=$("#reconncntbak").val();
			//登录失败(次)
			var relogincnt=$("#relogincntbak").val();
			//备切主条件
			var switchmainip=$("#switchmainipbak").val();
			$("input:radio[name='keepconn'][value='"+keepconn+"']").attr("checked","true");
			$("#linkcnt").val(linkcnt); 
			$("#reconncnt").val(reconncnt);
			$("#relogincnt").val(relogincnt);  
			$("input:radio[name='switchmainip'][value='"+switchmainip+"']").attr("checked","true");
			
			if(keepconn==1)
			{	
				$('#trmemostart').hide();
				$('#trreconncnt').hide();
				$('#trrelogincnt').hide();
				$('#trmemoend').hide();
				$('#trswitchmainip').hide();
			}else
			{
				$('#trmemostart').show();
				$('#trreconncnt').show();
				$('#trrelogincnt').show();
				$('#trmemoend').show();
				$('#trswitchmainip').show();
			}
			
			if(!$('#advanceconf').is(":hidden"))
			{
			    $('#advanceconf').dialog('close');	
			}
		}


		//短信签名长度模式改变时
		function changeKeepConn(modeValue){
			if(modeValue==1)
			{	
				$('#trmemostart').hide();
				$('#trreconncnt').hide();
				$('#trrelogincnt').hide();
				$('#trmemoend').hide();
				$('#trswitchmainip').hide();				
			}else
			{
				$('#trmemostart').show();
				$('#trreconncnt').show();
				$('#trrelogincnt').show();
				$('#trmemoend').show();
				$('#trswitchmainip').show();
			}
		}
		</script>
	</body>
</html>