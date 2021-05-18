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
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%//清除页面缓存
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires", 0);

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
	/* if(null != PTIP &&!"".equals(PTIP.trim())){
		String[] ptips = PTIP.split("\\.");
		if(ptips.length>=4){
			ip1=ptips[0];
			ip2=ptips[1];
			ip3=ptips[2];
			ip4=ptips[3];
		}
	} */
		if(null != PTIP &&!"".equals(PTIP.trim())){
			ip1=PTIP;
		}
		PTPORT = account.getPtPort().toString();
		SPIP = account.getSpIp();
	/* if(null != SPIP &&!"".equals(SPIP.trim())){
		String[] spips = SPIP.split("\\.");
		if(spips.length>=4){
			ip5=spips[0];
			ip6=spips[1];
			ip7=spips[2];
			ip8=spips[3];
		}
	} */
		if(null != SPIP &&!"".equals(SPIP.trim())){
			ip5=SPIP;
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
//String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = "2000-1400";
	menuCode = menuCode == null ? "0-0-0" : menuCode;
//String menuCode = titleMap.get(rTitle);
//判断是否网优通道
	String wy=(String)request.getParameter("wy");
	String txglFrame = skin.replace(commonPath, inheritPath);
//备运营商余额查看URL
	List<String> bakUrlList = (List<String>)request.getAttribute("bakUrlList");


	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
//修改通道账户
	String tdhm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdyxcsgl_xgtdzh", request);
	if(tdhm!=null&&tdhm.length()>1){
		tdhm = tdhm.substring(0,tdhm.length()-1);
	}
//端口号
	String dkh = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdyxcsgl_dkh", request);
//保存
	String bc = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdyxcsgl_bc", request);
//返回
	String fh = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdyxcsgl_fh", request);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN" >
<html>
<head>
	<%@include file="/common/common.jsp" %>
	<title><emp:message key="txgl_wghdpz_tdyxcsgl_xgtdzh" defVal="修改通道账户" fileName="txgl"></emp:message></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin%>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/gat_editUserdata.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin%>/gat_editUserdata.css?V=<%=StaticValue.getJspImpVersion() %>"/>
</head>
<body id="gat_editUserdata">
<input type="hidden" value="<%=skin%>" id="skin" name="skin"/>
<div id="container">
	<%-- 当前位置 --%>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdyxcsgl_xgtdzh", request)) %>
	<div class="rContent">
		<div class="titletop">
			<table class="titletop_table">
				<tr>
					<td class="titletop_td">
						<emp:message key="txgl_wghdpz_tdyxcsgl_xgtdzh" defVal="修改通道账户" fileName="txgl"></emp:message>
					</td>
					<td align="right">
						<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="txgl_wghdpz_tdyxcsgl_fhsyj" defVal="返回上一级" fileName="txgl"></emp:message></font>
					</td>
				</tr>
			</table>
		</div>
		<div id="detail_Info" class="detail_Info">
			<form action="<%=path %>/addUserdata.htm" name="form1"
				  method="post">
				<input type="hidden" id="path" value="<%=path %>" />
				<%-- 发送速率不起作用，，默认填0 --%>
				<input type="hidden" id="speedLimit"  class="input_bd speedLimit"  name="speedLimit" maxlength="8" value ="0" />
				<table class="accountType_table">
					<tr>
						<td class="zhlx_td">
							<span><emp:message key="txgl_wghdpz_tdyxcsgl_zhlx" defVal="账户类型：" fileName="txgl"></emp:message></span>
						</td>
						<td>
							<input class="accability" type="checkbox" <%="1".equals(userdata.getAccouttype().toString()) && (0==userdata.getAccability() || 1 == (int)(0x00000001&userdata.getAccability()))?"checked":"" %> <%="2".equals(userdata.getAccouttype().toString())?"disabled":"" %> name="dxval" value="1" ><label>短信通道账户</label>&nbsp;&nbsp;
							<input class="accability" type="checkbox" <%="2".equals(userdata.getAccouttype().toString()) && (2 == (int)(0x00000002&userdata.getAccability()))?"checked":"" %> disabled="disabled" name="cxval" value="2" ><label>彩信通道账户</label>&nbsp;&nbsp;
							<input class="accability" type="checkbox" <%="1".equals(userdata.getAccouttype().toString()) && (4 == (int)(0x00000004&userdata.getAccability()))?"checked":"" %> <%="2".equals(userdata.getAccouttype().toString())?"disabled":"" %> name="fxval" value="4" > <label>富信通道账户</label>&nbsp;&nbsp;

							<%-- <label ><select name="bbddd" id="bbddd"  disabled="disabled"  class="input_bd bbddd"  >
                                   <option value="1" <%="1".equals(user.getAccouttype().toString())?"selected":"" %> ><emp:message key="txgl_wgqdpz_dcspzh_dxspzh" defVal="短信SP账户" fileName="txgl"></emp:message></option>
                                   <option value="2" <%="2".equals(user.getAccouttype().toString())?"selected":"" %>><emp:message key="txgl_wgqdpz_dcspzh_cxspzh" defVal="彩信SP账户" fileName="txgl"></emp:message></option>
                            </select></label> --%>


							<input type="hidden" name="accouttype" id="accouttype" value="<%=userdata.getAccouttype() %>" />
							<input type="hidden" name="accability" id="accability" value="<%=userdata.getAccability() %>" />
							<input type="hidden" value="0" name="usertype" id="usertype"/>
							<%--								<label id="daili2">
                                                                <select name="bbcc" id="bbcc" disabled="disabled"
                                                                    class="input_bd bbcc"  >
                                                                    <option value="1" <%if("1".equals(accouttype)){ %>
                                                                        selected="selected" <%} %>>
                                                                        <emp:message key="txgl_wghdpz_wgyxcspz_dxtdzh" defVal="短信通道账户" fileName="txgl"></emp:message>
                                                                    </option>
                                                                    <option value="2" <%if("2".equals(accouttype)){ %>
                                                                        selected="selected" <%} %>>
                                                                        <emp:message key="txgl_wghdpz_wgyxcspz_cxtdzh" defVal="彩信通道账户" fileName="txgl"></emp:message>
                                                                    </option>
                                                                </select>
                                                            </label>
                                                            <input type="hidden" name="accouttype" id="accouttype"
                                                                value="<%=accouttype %>" />--%>
						</td>
					</tr>
				</table>
				<div class="div_bg tabCont">
					<label  class="div_label">
						<emp:message key="txgl_wghdpz_tdyxcsgl_empnbzhjbxx" defVal="EMP内部账户基本信息" fileName="txgl"></emp:message>
					</label>
					<a class="unfold">&nbsp;</a>
				</div>
				<div id="inner-ac-info" class="tabContent">
					<table>
						<tr>
							<td>
								<span><emp:message key="txgl_wghdpz_wgyxcspz_tdzhmc" defVal="通道账户名称：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<input maxlength="12" type="text" id="staffname"
										   class="input_bd staffname"  name="staffname"
										   value="<%=staffname.replace("&","&amp;").replace("\"","&quot;") %>"
									 	   <%--20190909 标准版EMP_LINUX_V7.3.SP3(build 560)，
									 	   通信管理-网关后端配置-通道账户管理，新建通道账户，通道账户名称无法使用上方键盘输入数字3，小键盘可以输入--%>
										   <%--onkeydown="if(event.keyCode==51)event.returnValue = false;" --%>
									/>
									&nbsp;
									<font color="red">*</font>
								</label>
								<span class="tips"><emp:message key="txgl_wghdpz_tdyxcsgl_zdytdzhmcpysb" defVal="自定义通道账户名称，便于识别" fileName="txgl"></emp:message></span>
							</td>
						</tr>
						<tr>
							<td class="tdzh_td">
								<span><emp:message key="txgl_wghdpz_wgyxcspz_tdzh" defVal="通道账号：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label><%=userid %><input type="hidden"
														  value="<%=userid %>" id="userid" />
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_zhmm" defVal="账户密码：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<input type="password" maxlength="32" id="userpassword"
										   class="input_bd userpassword"  name="userpassword"
										   value="<%=userpassword.replace("&","&amp;").replace("\"","&quot;") %>"
										   onkeypress="if(event.keyCode==32) return false;" />
									&nbsp;
									<font color="red">*</font>
								</label>
							</td>
						</tr>
						<tr>
							<td><span><emp:message key="txgl_wghdpz_wgyxcspz_zhzt" defVal="账户状态：" fileName="txgl"></emp:message></span></td>
							<td><label>
								<select name="status" id="status"  class="input_bd status">
									<option value="0" ><emp:message key="txgl_wghdpz_wgyxcspz_yjh" defVal="已激活" fileName="txgl"></emp:message></option>
									<option value="1" <%="1".equals(status)?"selected":"" %>><emp:message key="txgl_wghdpz_wgyxcspz_ysx" defVal="已失效" fileName="txgl"></emp:message></option>
								</select></label></td>
						</tr>
						<tr>
							<td>
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_empwgipdzjdk" defVal="EMP网关地址及端口：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<div id="ptIp">
									<input type="text" name="ip1" id="ip1" onchange="checkDataIp(this.value)" class="aip" value="<%=ip1 %>" maxlength="32" />
									<%-- .
                                    <input type=text name=ip2 id="ip2" maxlength=3 class=a3
                                        value="<%=ip2 %>" onkeyup="mask(this,event)"
                                        onbeforepaste=mask_c()>
                                    .
                                    <input type=text name=ip3 id="ip3" maxlength=3 class=a3
                                        value="<%=ip3 %>" onkeyup="mask(this,event)"
                                        onbeforepaste=mask_c()>
                                    .
                                    <input type=text name=ip4 id="ip4" maxlength=3 class=a3
                                        value="<%=ip4 %>" onkeyup="mask(this,event)"
                                        onbeforepaste=mask_c()> --%>
								</div>
								&nbsp; ：
								<input type="text" class="input_bd ptPort"
									   id="ptPort" name="ptPort" placeholder='<emp:message key="txgl_wghdpz_tdyxcsgl_dkh" defVal="端口号" fileName="txgl"></emp:message>' maxlength="5"
									   value="<%=PTPORT.replace("&","&amp;").replace("\"","&quot;") %>"
									   onblur="validatePort(this)"
									   onkeyup="if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')" />
								&nbsp;
								<font color="red" class="xing_font">*</font>
								<span class="tips"><emp:message key="txgl_wghdpz_tdyxcsgl_empwgszfwqipj" defVal="EMP网关所在服务器地址及通讯端口" fileName="txgl"></emp:message></span>
								<a class="cluster-add"><emp:message key="txgl_wghdpz_tdyxcsgl_tjjqjd" defVal="添加集群节点" fileName="txgl"></emp:message></a>
								<input type="hidden" id="cluster-info" value="<%=ptnode %>"/>
							</td>
						</tr>
					</table>
				</div>

				<div class="div_bg tabCont">
					<label  class="div_label">
						<emp:message key="txgl_wghdpz_tdyxcsgl_yysjrzhjbxx" defVal="运营商接入账户基本信息" fileName="txgl"></emp:message>
					</label>
					<a class="unfold">&nbsp;</a>
				</div>
				<div class="tabContent">
					<div id="yysCont">
						<table id="yysIP" class="temp_yys">
							<tr>
								<td class="yyszhid_td">
									<span><emp:message key="txgl_wghdpz_wgyxcspz_yyszhid" defVal="运营商账户ID：" fileName="txgl"></emp:message></span>
								</td>
								<td>
									<label>
										<input
												value="<%=SPACCID.replace("&","&amp;").replace("\"","&quot;") %>"
												type="text" id="SPACCID" name="SPACCID" maxlength="32"
												class="input_bd SPACCID"
												onkeyup="if(value != value.replace(/[^\a-\z\A-\Z0-9\_\-\*\#\(\)\@]/g,'')) value = value.replace(/[^\a-\z\A-\Z0-9\_\-\*\#\(\)\@]/g,'')" />
										&nbsp;
										<font color="red">*</font>
									</label>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="txgl_wghdpz_tdyxcsgl_yyszhmm" defVal="运营商账户密码：" fileName="txgl"></emp:message></span>
								</td>
								<td>
									<label>
										<input type="password" id="SPACCPWD" name="SPACCPWD"
											   class="input_bd SPACCPWD"  maxlength="32"
											   value="<%=SPACCPWD.replace("&","&amp;").replace("\"","&quot;") %>"
											   onkeypress="if(event.keyCode==32) return false;" />
										&nbsp;
										<font color="red">*</font>
									</label>
								</td>
							</tr>
							<tr>
								<td>
									<span><emp:message key="txgl_wghdpz_tdyxcsgl_jshzs" defVal="技术合作商：" fileName="txgl"></emp:message></span>
								</td>
								<td>
									<label>
										<select name="spType" id="spType" class="input_bd spType" onchange="chty()" >
											<option value="0">
												<emp:message key="txgl_wghdpz_tdyxcsgl_mwkj" defVal="梦网科技" fileName="txgl"></emp:message>
											</option>
											<option value="10" <%="10".equals(SPTYPE)?"selected":"" %>>
												<emp:message key="txgl_wghdpz_tdyxcsgl_qt" defVal="其他" fileName="txgl"></emp:message>
											</option>
										</select>
									</label>
								</td>
							</tr>
							<tr>
								<td>
									<span class="iptext" id="iptext-ip5"><emp:message key="txgl_wghdpz_tdyxcsgl_yysipdzjdk1" defVal="运营商地址及端口1：" fileName="txgl"></emp:message></span>
								</td>
								<td>
									<select id="linklevel" name="linklevel" class="input_bd">
										<option value="0"><emp:message key="txgl_wghdpz_tdyxcsgl_zy" defVal="主用" fileName="txgl"></emp:message></option>
									</select>
									<div id="spip">
										<input type="text" name="ip5" id="ip5"  class="aip" value="<%=ip5 %>"  onchange="checkDataIp2(this)" placeholder="填写IP地址或域名" maxlength="32">
										<%-- <input type=text name=ip5 id="ip5" maxlength=3 class=a3 value="<%=ip5 %>"
                                        onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
                                        <input type=text name=ip6 id="ip6"  maxlength=3 class=a3 value="<%=ip6 %>"
                                        onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
                                        <input type=text name=ip7  id="ip7" maxlength=3 class=a3 value="<%=ip7 %>"
                                        onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
                                        <input type=text name=ip8 id="ip8" maxlength=3 class=a3 value="<%=ip8 %>"
                                        onkeyup="mask(this,event)" onbeforepaste=mask_c()> --%>
									</div>
									&nbsp;：
									<input type="text" class="input_bd spPort"  id="spPort" placeholder="<%=dkh %>" name="spPort"  value="<%=SPPORT.replace("&","&amp;").replace("\"","&quot;") %>" maxlength="5"  onblur="validatePort(this)"
										   onkeyup= "if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')" />
									&nbsp;
									<input type="hidden" name="linkstatus" value="0" />
									<font color="red" class="xing_font">*</font>
									<a href="javascript:void(0)" onclick="addBackIp()" class="addbackup"><emp:message key="txgl_wghdpz_tdyxcsgl_tj" defVal="添加" fileName="txgl"></emp:message></a>
									<span class="tips"><emp:message key="txgl_wghdpz_tdyxcsgl_yyswgszzfwqip" defVal="运营商网关所在主用服务器地址及通讯端口" fileName="txgl"></emp:message></span>
								</td>
							</tr>
						</table>

						<%
							//Pattern p=Pattern.compile("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5]):\\d{1,5}$");
							//Pattern p2=Pattern.compile("(?<=http://|\\.)[^.]*?\\.(?:com\\.cn|net\\.cn|org\\.cn|com|net|org|cn|biz|info|cc|tv:\\d{1,5})",Pattern.CASE_INSENSITIVE);
							String singmutitype="";
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
										String ipportstr=gwgcinfo.getIp()+":"+gwgcinfo.getPort();
										//Matcher m = p.matcher(ipportstr);
										//Matcher m2 = p2.matcher(ipportstr);
										//判断是否匹配ip+端口的模式
										//if(m.matches()||m2.matches())
										//{
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
								<td class="yysipdzjdk_td">
									<span class="iptext" id="iptext-aip-<%=nonum%>"><emp:message key="txgl_wghdpz_tdyxcsgl_yysipdzjdk" defVal="运营商地址及端口" fileName="txgl"></emp:message><%=nonum %>：</span>
								</td>
								<td>
									<select name="linklevel<%=c %>" class="input_bd bklevel llever">
										<option value="0" <%if("0".equals(gwgcinfo.getLinklevel()+"")){ %> selected="selected" <%} %>><emp:message key="txgl_wghdpz_tdyxcsgl_zy" defVal="主用" fileName="txgl"></emp:message></option>
										<option value="1" <%if("1".equals(gwgcinfo.getLinklevel()+"")){ %> selected="selected" <%} %>><emp:message key="txgl_wghdpz_tdyxcsgl_by" defVal="备用" fileName="txgl"></emp:message></option>
									</select>
									<div class="backup">

										<input type="text" name="ip<%=index %>[]"  class="aip" value="<%=ip %>" id="aip-<%=nonum %>" onchange="checkDataIp2(this)" placeholder="填写IP地址或域名" maxlength="32" />
										<%-- <input type=text name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[0] %>"
                                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
                                        .
                                        <input type=text name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[1] %>"
                                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
                                        .
                                        <input type=text name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[2] %>"
                                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
                                        .
                                        <input type=text name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[3] %>"
                                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()> --%>
									</div>
									&nbsp;：
									<input type="text" name="spPort<%=c %>" placeholder="<%=dkh %>" maxlength="5"
										   value="<%=port %>" class="input_bd prot_by dkh_input"
										   onkeyup="if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')"
										   onblur="validatePort(this)" />
									&nbsp;
									<input type="hidden" name="linkstatus<%=c %>" class="lstatus" value="<%=gwgcinfo.getLinkstatus() %>" />
									<font color="red" class="xing_font">*</font>
									<span class="handle"> <a class="addbackup"
															 onclick="addNextBackIp(this)" href="javascript:void(0)"><emp:message key="txgl_wghdpz_tdyxcsgl_tj" defVal="添加" fileName="txgl"></emp:message></a> | <a
											class="remove_handle" onclick="removeHandle(this)"
											href="javascript:void(0)"><emp:message key="common_delete" defVal="删除" fileName="common"></emp:message></a> </span>
								</td>
							</tr>
						</table>
						<%
									}
									//}
								}
							}
						}else{
							//如果数据库未存主备  则默认是单链路
							singmutitype="10";
							String [] ipArray = byId.split(",");
							for(int c=0;c<ipArray.length;c++)
							{
								//Matcher m = p.matcher(ipArray[c]);
								//判断是否匹配ip+端口的模式
								//if(m.matches())
								//{
								if(!"".equals(ipArray[c])&&ipArray[c].contains(":"))
								{
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
								<td width="140">
									<span class="iptext" id="iptext-aip-<%=c+2%>"><emp:message key="txgl_wghdpz_tdyxcsgl_yysipdzjdk" defVal="运营商地址及端口" fileName="txgl"></emp:message><%=c+2 %>：</span>
								</td>
								<td>
									<select name="linklevel<%=c %>" class="input_bd bklevel llever">
										<option value="0"><emp:message key="txgl_wghdpz_tdyxcsgl_zy" defVal="主用" fileName="txgl"></emp:message></option>
										<option value="1" selected="selected" ><emp:message key="txgl_wghdpz_tdyxcsgl_by" defVal="备用" fileName="txgl"></emp:message></option>
									</select>
									<div class="backup">
										<input type=text name="ip<%=index %>[]"  class="aip" value="<%=ip %>" id="aip-<%=c+2 %>" onchange="checkDataIp2(this)" placeholder="填写IP地址或域名" maxlength="32">

										<%-- <input type=text name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[0] %>"
                                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
                                        .
                                        <input type=text name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[1] %>"
                                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
                                        .
                                        <input type=text name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[2] %>"
                                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
                                        .
                                        <input type=text name="ip<%=index %>[]" maxlength=3 class="a3" value="<%=ipStrArr[3] %>"
                                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()> --%>
									</div>
									&nbsp;：
									<input type="text" name="spPort<%=c %>" placeholder="<%=dkh %>" maxlength="5"
										   value="<%=port %>" class="input_bd prot_by dkh_input"
										   onkeyup="if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')"
										   onblur="validatePort(this)" />
									&nbsp;
									<input type="hidden" name="linkstatus<%=c %>" class="lstatus" value="2" />
									<font color="red" class="xing_font">*</font>
									<span class="handle"> <a class="addbackup"
															 onclick="addNextBackIp(this)" href="javascript:void(0)"><emp:message key="txgl_wghdpz_tdyxcsgl_tj" defVal="添加" fileName="txgl"></emp:message></a> | <a
											class="remove_handle" onclick="removeHandle(this)"
											href="javascript:void(0)"><emp:message key="txgl_wghdpz_wgyxcspz_sc" defVal="删除" fileName="txgl"></emp:message></a> </span>
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
							<td class="ywlx_td">
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_ywlx" defVal="业务类型：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<input
											value="<%=SERVICETYPE.replace("&","&amp;").replace("\"","&quot;") %>"
											class="input_bd serviceType"  type="text"
											id="serviceType" name="serviceType" maxlength="32"
											onkeyup="if(value != value.replace(/[^\w]/g,'')) value = value.replace(/[^\w]/g,'')" />
								</label>
								<input type="hidden" id="singmutitype" name="singmutitype" value="<%=singmutitype %>"  />
							</td>
						</tr>
						<tr>
							<td>
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_spqydm" defVal="SP企业代码：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<input type="text" id="spid" name="spid" maxlength="32"
										   onkeyup="if(value != value.replace(/[^\w]/g,'')) value = value.replace(/[^\w]/g,'')"
										   class="input_bd spid"
										   value="<%=SPID.replace("&","&amp;").replace("\"","&quot;") %>" />
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_jfyhlx" defVal="计费用户类型：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<select name="feeUserType" id="feeUserType" class="input_bd feeUserType" >
										<option value="0">
											<emp:message key="txgl_wghdpz_tdyxcsgl_dmdzdmjf" defVal="对目的终端MSISDN计费" fileName="txgl"></emp:message>
										</option>
										<option value="1"
												<%="1".equals(FEEUSERTYPE)?"selected":"" %>>
											<emp:message key="txgl_wghdpz_tdyxcsgl_dyzdmjf" defVal="对源终端MSISDN计费" fileName="txgl"></emp:message>
										</option>
										<option value="2"
												<%="2".equals(FEEUSERTYPE)?"selected":"" %>>
											<emp:message key="txgl_wghdpz_tdyxcsgl_dspjf" defVal="对SP计费" fileName="txgl"></emp:message>
										</option>
										<option value="3"
												<%="3".equals(FEEUSERTYPE)?"selected":"" %>>
											<emp:message key="txgl_wghdpz_tdyxcsgl_gzdwx" defVal="该字段无效" fileName="txgl"></emp:message>
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
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_txxy" defVal="通讯协议：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<select name="protocolCode" id="protocolCode"
											class="input_bd protocolCode"  >
									</select>
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_txxycs" defVal="通讯协议参数：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<input class="input_bd protocolParam"  type="text"
									   maxlength="512"
									   value="<%=PROTOCOLPARAM.replace("&","&amp;").replace("\"","&quot;") %>"
									   id="protocolParam" />
							</td>
						</tr>
						<tr class="ch_ty">
							<td>
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_zhjflx" defVal="账户计费类型：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<select name="spFeeFlag" id="spFeeFlag" class="input_bd spFeeFlag" >
										<option value="2">
											<emp:message key="txgl_wghdpz_tdyxcsgl_hff" defVal="后付费" fileName="txgl"></emp:message>
										</option>
										<option value="1" <%="1".equals(SpFeeFlag)?"selected":"" %>>
											<emp:message key="txgl_wghdpz_tdyxcsgl_yff" defVal="预付费" fileName="txgl"></emp:message>
										</option>
									</select>
								</label>
								&nbsp;
								<span class="tips"><emp:message key="txgl_wghdpz_tdyxcsgl_yyszhjflx" defVal="运营商账户计费类型" fileName="txgl"></emp:message></span>
							</td>
						</tr>
						<tr class="ch_ty">
							<td>
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_yeckurl" defVal="余额查看URL：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<input class="input_bd feeUrl"  type="text"
									   value="<%=FeeUrl==null?"":FeeUrl.trim() %>" id="feeUrl"
									   maxlength="512" />
								&nbsp;
								<span class="tips"><emp:message key="txgl_wghdpz_tdyxcsgl_zyysyeckuljdz" defVal="主运营商余额查看URL链接地址" fileName="txgl"></emp:message></span>
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
								<input class="input_bd byysyeckuljdz_input"  type="text"
									   value="<%=value%>" id="<%=id%>" maxlength="512" />
								&nbsp;
								<span class="tips"><emp:message key="txgl_wghdpz_tdyxcsgl_byysyeckuljdz" defVal="备运营商余额查看URL链接地址" fileName="txgl"></emp:message><%=i%></span>
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
								   value="<%=bc %>" class="btnClass5 mr23" />
							<%} %>
							<input type="button" id="btnSca" onclick="javascript:back()"
								   value="<%=fh %>" class="btnClass6" />
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
				<td class="yysipdzjdk1_td">
					<span class="iptext"><emp:message key="txgl_wghdpz_tdyxcsgl_yysipdzjdk1" defVal="运营商地址及端口1：" fileName="txgl"></emp:message></span>
				</td>
				<td>
					<select name="linklevel0" class="input_bd bklevel llever">
						<option value="0"><emp:message key="txgl_wghdpz_tdyxcsgl_zy" defVal="主用" fileName="txgl"></emp:message></option>
						<option value="1"><emp:message key="txgl_wghdpz_tdyxcsgl_by" defVal="备用" fileName="txgl"></emp:message></option>
					</select>
					<div class="backup">
						<input type="text" name="ip[]"  class="aip"  onchange="checkDataIp2(this)" placeholder="填写IP地址或域名" maxlength="32"/>
						<!-- <input type=text name="ip[]" maxlength=3 class="a3"
                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
                        .
                        <input type=text name="ip[]" maxlength=3 class="a3"
                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
                        .
                        <input type=text name="ip[]" maxlength=3 class="a3"
                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()>
                        .
                        <input type=text name="ip[]" maxlength=3 class="a3"
                            onkeyup="mask_dy(this,event)" onbeforepaste=mask_c()> -->
					</div>
					&nbsp;：
					<input type="text" name="spPort0" placeholder="<%=dkh %>" maxlength="5"
						   value="" class="input_bd prot_by dkh_input"
						   onkeyup="if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')"
						   onblur="validatePort(this)" />
					&nbsp;
					<input type="hidden" name="linkstatus0" class="lstatus" value="<%="0".equals(singmutitype)?0:2 %>" />
					<font color="red" class="xing_font">*</font>
					<span class="handle"> <a class="addbackup"
											 onclick="addNextBackIp(this)" href="javascript:void(0)"><emp:message key="txgl_wghdpz_tdyxcsgl_tj" defVal="添加" fileName="txgl"></emp:message></a> | <a
							class="remove_handle" onclick="removeHandle(this)"
							href="javascript:void(0)"><emp:message key="common_delete" defVal="删除" fileName="common"></emp:message></a> </span>
				</td>
			</tr>
		</table>
	</div>
	<%--end round_content--%>
	<div class="bottom">
		<div id="bottom_right">
			<div id="bottom_left"></div>
		</div>
	</div>
</div>
<%--end warp--%>
<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=iPath%>/js/ip.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=iPath%>/js/gat_userdata.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
<script>
    var times = <%=System.currentTimeMillis()%>;
    $(document).ready(function(){
        noquotUserData("#SPACCPWD");
        $("#protocolCode").load("<%=path%>/gat_userdata.htm?method=getPrTmplOption&accouttype="+$("#accouttype").val()+"&times="+times,{},
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
                $.post("<%=path%>/gat_userdata.htm?method=getPrTmplContent&times="+times,{protocolCode : protocolCode},
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
        chty();
    });
    function submitForm(){
        // var idArray = ["userpassword","staffname","SPACCID","SPACCPWD","serviceType","spid","ptIp","ptPort","spIp","spPort","speedLimit"];
        //var idArray = ["userpassword","staffname","accouttype","SPACCID","SPACCPWD","ip1","ip2","ip3","ip4","ptPort","ip5","ip6","ip7","ip8","spPort"];
        var accability = 0;
        var arr_v = new Array();
        $(".accability[type='checkbox']:checked").each(function(){
            arr_v.push($(this).val());
        });
        if (arr_v.length == 0){
            alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_1"));
            return;
        }else if (arr_v.length == 2){
            accability = parseInt(arr_v[0])+parseInt(arr_v[1]);
        } else if (arr_v.length == 1){
            accability = arr_v[0];
        }

        var idArray = ["userpassword","staffname","accouttype","SPACCID","SPACCPWD","ip1","ptPort","ip5","spPort"];
        var b=0;
        for(var a=0;a<idArray.length && b==0;a++)
        {
            if($.trim($("#"+idArray[a]).val())=="")
            {
                //alert("存在未填项！");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_1"));
                $("#"+idArray[a]).select();
                b=1;
            }
        }
        if(b==0 && $("#protocolCode").val()=="")
        {
            //alert("请选择通讯协议代码！");
            alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_2"));
            b=1;
        }
        if(b==1)
        {
            return;
        }

        /*  //校验网关集群地址的合法性
         if(!checkClusterAddr()){
             return;
         } */

        //校验网关集群地址的合法性
        if(!checkClusterAddrNew()){
            return;
        }

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
                //alert("运营商IP地址及端口"+(i+1)+"主用/备用必须选！");
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
            if(!checkIP2New(ip)){
                //alert("运营商地址/端口"+(i+1)+"地址不合法！");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_5")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_6"));
                return;
            }
            var port=$tr.find("input[name^='spPort']").val();
            if(port=="")
            {
                //alert("运营商地址/端口"+(i+1)+"端口不能为空！");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_5")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_7"));
                return;
            }
            if(!isPort(port)){
                //alert("运营商地址/端口"+(i+1)+"端口不合法！");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_5")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_8"));
                return;
            }

            var linkstatus=$tr.find("input[name^='linkstatus']").val();
            if(linkstatus=="")
            {
                //alert("运营商地址及端口"+(i+1)+"状态必须要有值！");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_9")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_10"));
                return;
            }

            if(linklevel=="0")
            {
                zindex++;
                if(zindex>1&&$("#singmutitype").val()!=0){
                    //alert("运营商地址及端口是单链路多连接，主用只能有一个！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_11"));
                    return;
                }
                if(zindex>5&&$("#singmutitype").val()==0){
                    //alert("运营商地址及端口是多链路多连接，主用不能超过五个！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_12"));
                    return;
                }
            }else if(linklevel=="1"){
                bindex++;
                if(bindex>4&&$("#singmutitype").val()!=0){
                    //alert("运营商地址及端口是单链路多连接，备用只能有四个！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_13"));
                    return;
                }
                if(bindex>5&&$("#singmutitype").val()==0){
                    //alert("运营商地址及端口是多链路多连接，备用不能超过五个！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_14"));
                    return;
                }
            }else{
                //alert("运营商地址及端口"+(i+1)+"主用/备用必须选！");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_15")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_16"));
                return;
            }

            if(linklevel=="1"){
                byIp=byIp+ip+":"+port+",";
            }
            allipport=allipport+ip+":"+port+",";
            zbIP=zbIP+linklevel+":"+ip+":"+port+":"+linkstatus+",";
        }

        if(bindex==0&&$("#spType").val()==0)
        {
            //if(!confirm("您未添加运营商网关备用地址/端口，确定要保存吗？"))
            if(!confirm(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_17")))
            {
                return;
            }
        }

        if(b==0)
        {
            var userid = $("#userid").val();
            var userpassword  = $.trim($("#userpassword").val());
            var staffname  = $.trim($("#staffname").val());
            var accouttype=$.trim($("#accouttype").val());
            var accability=accability;
            var status  = $.trim($("#status").val());
            var SPACCID  = $.trim($("#SPACCID").val());
            var SPACCPWD  = $.trim($("#SPACCPWD").val());
            var serviceType  = $.trim($("#serviceType").val());
            var feeUserType  = $.trim($("#feeUserType").val());
            //var ptIp  = $.trim($("#ip1").val())+'.'+$.trim($("#ip2").val())+'.'+
            //$.trim($("#ip3").val())+'.'+$.trim($("#ip4").val());
            var ptIp  = $.trim($("#ip1").val());
            var ptPort  = $.trim($("#ptPort").val());
            var speedLimit  = $.trim($("#speedLimit").val());
            //var spIp  = $.trim($("#ip5").val())+'.'+$.trim($("#ip6").val())+'.'+
            //$.trim($("#ip7").val())+'.'+$.trim($("#ip8").val());
            var spIp  = $.trim($("#ip5").val());
            var spPort  = $.trim($("#spPort").val());
            var protocolCode  = $.trim($("#protocolCode").val());
            var protocolParam  = $.trim($("#protocolParam").val());
            var spid  = $.trim($("#spid").val());
            var spType = $("#spType").val();
            var feeUrl = $.trim($("#feeUrl").val());
            var bakFeeUrl1 = $.trim($("#bakFeeUrl1").val());
            var bakFeeUrl2 = $.trim($("#bakFeeUrl2").val());
            var bakFeeUrl3 = $.trim($("#bakFeeUrl3").val());
            var bakFeeUrl4 = $.trim($("#bakFeeUrl4").val());
            var bakFeeUrl5 = $.trim($("#bakFeeUrl5").val());
            var spFeeFlag = $.trim($("#spFeeFlag").val());
            //var accountType = $.trim($("#bbcc").val());
            //解决账户类型选择问题
            var accountType = accability;
            /* var reg = /[^\w]/;
            if(reg.test(SPACCID))
            {
                //alert("运营商账户ID只能是字母或数字组成，请重新输入！");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_18"));
                return;
            } */

            var regpwd=/^[\u4e00-\u9fa5]+$/;
            if(regpwd.test(SPACCPWD)){
                alert("运营商密码不能为中文!");
                return;
            }
            if(SPACCPWD.length<6||SPACCPWD.length>32)
            {
                //alert("运营商账户密码长度为6-32位！");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_38"));
                return;
            }
            //cmpp2x/2xx/3x/3xx 6字节
            /* 	if(protocolCode=="5"||protocolCode=="7"||protocolCode=="40"||protocolCode=="41")
                {
                    if(SPACCID.length>6)
                    {
                        //alert("运营商账户ID长度最大为6位，请重新输入！");
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_20"));
                        return;
                    }
                }
                //sgip 16字节
                else if(protocolCode=="4")
                {
                    if(SPACCID.length>16)
                    {
                        //alert("运营商账户ID长度最大为16位，请重新输入！");
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_21"));
                        return;
                    }
                }
                //smgp 8字节
                else if(protocolCode=="6")
                {
                    if(SPACCID.length>8)
                    {
                        //alert("运营商账户ID长度最大为8位，请重新输入！");
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_22"));
                        return;
                    }
                }
                //其他32字节
                else if(protocolCode=="50"||protocolCode=="102")
                {
                    if(SPACCID.length>32)
                    {
                        //alert("运营商账户ID长度最大为32位，请重新输入！");
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_23"));
                        return;
                    }
                }
                //mwmms 6字节
                else if(protocolCode=="101")
                {
                    if(SPACCID.length>6)
                    {
                        //alert("运营商账户ID长度最大为6位，请重新输入！");
                        alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_24"));
                        return;
                    }
                } */

            if(SPACCID.length<1||SPACCID.length>32)
            {
                //alert("运营商账户ID长度为6-32位！");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_39"));
                return;
            }

            if(serviceType=="")
            {
                serviceType=" ";
            }
            if(spid=="")
            {
                spid=" ";
            }

            if(!checkIPAddr(spIp+':'+spPort,allipport))
            {
                return;
            }

            //网关集群地址数组
            var clusterAddrs = getClusterAddr();
            $.post("<%=path%>/gat_userdata.htm",{
                method : "edit",
                userid : userid,
                uid : <%=uid%>,
                keyId : '<%=keyId%>',
                userpassword : userpassword,
                staffname : staffname,
                accouttype:accouttype,
                accability:accability,
                status : status,
                SPACCID : SPACCID,
                SPACCPWD : SPACCPWD,
                serviceType : serviceType,
                feeUserType : feeUserType,
                ptIp : ptIp,
                ptPort : ptPort,
                spPort : spPort,
                spIp : spIp,
                speedLimit : speedLimit,
                protocolCode : protocolCode,
                protocolParam : protocolParam,
                spType : spType,
                spid : spid,
                spFeeFlag : spFeeFlag,
                feeUrl : feeUrl,
                bakFeeUrl1 : bakFeeUrl1,
                bakFeeUrl2 : bakFeeUrl2,
                bakFeeUrl3 : bakFeeUrl3,
                bakFeeUrl4 : bakFeeUrl4,
                bakFeeUrl5 : bakFeeUrl5,
                accountType:accountType,
                byIp:byIp,
                zbIP:zbIP,
                sfID:<%=sfID%>,
                clusterAddr:clusterAddrs
            },function(result){
                if(result == "true"){
                    /*保存成功！*/
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_25"));
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
    }

</script>
</body>
</html>