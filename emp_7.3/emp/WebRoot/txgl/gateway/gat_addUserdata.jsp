<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfPageField"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String txglFrame = skin.replace(commonPath, inheritPath);

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = "2000-1400";
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	//String menuCode = titleMap.get("userdata");
	@SuppressWarnings("unchecked")
	List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("gat_pagefileds");


	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

	//端口号
	String dkh = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdyxcsgl_dkh", request);
	//确定
	String qd = MessageUtils.extractMessage("common", "common_confirm", request);
	//返回
	String fh = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdyxcsgl_fh", request);

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/common/common.jsp" %>
	<title><emp:message key="txgl_wghdpz_tdyxcsgl_xjtdzh" defVal="新建通道账户" fileName="txgl"></emp:message></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/gat_addUserdata.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/gat_addUserdata.css?V=<%=StaticValue.getJspImpVersion() %>"/>
</head>
<body id="gat_addUserdata">
<input type="hidden" value="<%=skin%>" id="skin" name="skin"/>
<div id="container">
	<%-- 当前位置 --%>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdyxcsgl_xjzhxx", request)) %>
	<div class="rContent">
		<div class="titletop">
			<table class="titletop_table">
				<tr>
					<td class="titletop_td">
						<emp:message key="txgl_wghdpz_tdyxcsgl_xjzhxx" defVal="新建账户信息" fileName="txgl"></emp:message>
					</td>
					<td align="right">
						<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="txgl_wghdpz_tdyxcsgl_fhsyj" defVal="返回上一级" fileName="txgl"></emp:message></font>
					</td>
				</tr>
			</table>
		</div>
		<div id="detail_Info" class="detail_Info">
			<form action="addUserdata.htm" name="form1" method="post">
				<input type="hidden" id="path" value="<%=path %>" />
				<input type="hidden" id="status" name="status" value="0" />
				<%-- 发送速率不起作用，，默认填0 --%>
				<input type="hidden" id="speedLimit" class="input_bd speedLimit"
					   name="speedLimit" maxlength="8" value="0" />
				<%
					//String typename="";
					if(pagefileds!=null&&pagefileds.size()>0){
						LfPageField first=pagefileds.get(0);
						//typename=first.getField()+"：";
					}
				%>
				<table class="accountType_table">
					<tr>
						<td class="zhlx_td">
							<span><emp:message key="txgl_wghdpz_tdyxcsgl_zhlx" defVal="账户类型：" fileName="txgl"></emp:message></span>
						</td>
						<%--								<td>
                                                            <label id="daili2">
                                                                <select name="accouttype" id="accouttype" class="input_bd accouttype" >
                                                                    <option value="1"><emp:message key="txgl_wgqdpz_dcspzh_dxspzh" defVal="短信SP账户" fileName="txgl"></emp:message></option>

                                                             <option value="2"><emp:message key="txgl_wgqdpz_dcspzh_cxspzh" defVal="彩信SP账户" fileName="txgl"></emp:message></option>
                                                                    &lt;%&ndash;
                                                                    <%
                                                        if(pagefileds!=null&&pagefileds.size()>1){
                                                            for(int i=1;i<pagefileds.size();i++){
                                                            LfPageField pagefid=pagefileds.get(i);
                                                            String accountType = pagefid.getSubFieldName();
                                                            if("短信通道账户".equals(accountType)){
                                                                accountType = "zh_HK".equals(empLangName)?"SMS channel account":"zh_TW".equals(empLangName)?"短信通道賬戶":"短信通道账户";
                                                            }
                                                            else if("彩信通道账户".equals(accountType)){
                                                                accountType = "zh_HK".equals(empLangName)?"MMS channel account":"zh_TW".equals(empLangName)?"彩信通道賬戶":"彩信通道账户";
                                                            }
                                                        %>
                                                            <option value="<%=pagefid.getSubFieldValue() %>"><%= accountType %></option>
                                                        <%
                                                            }
                                                        }
                                                        %>
                                                                &ndash;%&gt;</select>
                                                            </label>
                                                        </td>--%>
						<td>
							<input class="accability" type="checkbox" checked="checked" name="dxval" value="1" id="dxval"><label>短信通道账户</label>&nbsp;&nbsp;
							<input class="accability" type="checkbox" disabled="disabled" name="cxval" value="2" id="cxval"><label>彩信通道账户</label>&nbsp;&nbsp;
							<input class="accability" type="checkbox" name="fxval" value="4" id="fxval"> <label>富信通道账户</label>&nbsp;&nbsp;&nbsp;&nbsp;

							<font color="red">&nbsp;*&nbsp;</font><span class="tips">短信或富信不能和彩信同时勾选</span>

							<input type="hidden" value="0" name="usertype" id="usertype"/>
							<label><input type=text id="lbUserType" name="loginid" value="WBS00A" readonly="readonly" class="hidden"/></label>
						</td>
					</tr>
				</table>
				<div class="div_bg tabCont">
					<label class="div_label">
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
										   value=""
										   <%--20190909 标准版EMP_LINUX_V7.3.SP3(build 560)，
										   通信管理-网关后端配置-通道账户管理，新建通道账户，通道账户名称无法使用上方键盘输入数字3，小键盘可以输入--%>
										   <%--onkeydown="if(event.keyCode==51)event.returnValue = false;"--%>
									/>
									&nbsp;
									<font color="red">*</font>
								</label>
								<span class="tips"><emp:message key="txgl_wghdpz_tdyxcsgl_zdytdzhmcpysb" defVal="自定义通道账户名称，便于识别" fileName="txgl"></emp:message></span>
							</td>
						</tr>
						<tr>
							<td width="140">
								<span><emp:message key="txgl_wghdpz_wgyxcspz_tdzh" defVal="通道账号：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<input type="text" id="userid" name="userid" maxlength="6" value="" class="input_bd userid"  onblur="spCard(this)" onkeyup="spCard(this)" />
									&nbsp;
									<font color="red">*</font>
								</label>
								<input type="hidden" name="hidOpType" value="add" />
								<span class="tips"><emp:message key="txgl_wghdpz_tdyxcsgl_empnbtxjqzh" defVal="EMP内部通讯鉴权帐号(不能以WY0开头)" fileName="txgl"></emp:message></span>
							</td>
						</tr>
						<tr>
							<td>
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_zhmm" defVal="账户密码：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<input type="password" maxlength="32" id="userpassword" name="userpassword" class="input_bd userpassword"  value="" onkeypress="if(event.keyCode==32) return false;" />
									&nbsp;
									<font color="red">*</font>
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_empwgipdzjdk" defVal="EMP网关地址及端口：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<div id="ptIp" class="div_bd">
									<input type="text" name="ip1" id="ip1" onchange="checkDataIp(this.value)" class="aip" placeholder="填写IP地址或域名" maxlength="32"/>
									<!-- .
                                    <input type=text name=ip2 id="ip2" maxlength=3 class=a3 onkeyup="mask(this,event)" onbeforepaste=mask_c()>
                                    .
                                    <input type=text name=ip3 id="ip3" maxlength=3 class=a3 onkeyup="mask(this,event)" onbeforepaste=mask_c()>
                                    .
                                    <input type=text name=ip4 id="ip4" maxlength=3 class=a3 onkeyup="mask(this,event)" onbeforepaste=mask_c()> -->
								</div>
								&nbsp; ：
								<input type="text" id="ptPort" name="ptPort" placeholder="<%=dkh %>" maxlength="5" value="" onblur="validatePort(this)" class="input_bd ptPort"  onkeyup="if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')" />
								&nbsp;
								<font color="red" class="ptPort_down_font">*</font>
								<span class="tips"><emp:message key="txgl_wghdpz_tdyxcsgl_empwgszfwqipj" defVal="EMP网关所在服务器地址及通讯端口" fileName="txgl"></emp:message></span>
								<a class="cluster-add"><emp:message key="txgl_wghdpz_tdyxcsgl_tjjqjd" defVal="添加集群节点" fileName="txgl"></emp:message></a>
							</td>
						</tr>


					</table>

				</div>
				<div class="div_bg tabCont">
					<label class="div_label">
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
										<input type="text" id="SPACCID" name="SPACCID"
											   class="input_bd SPACCID"  value=""
											   maxlength="32" onkeyup="if(value != value.replace(/[^\a-\z\A-\Z0-9\_\-\*\#\(\)\@]/g,'')) value = value.replace(/[^\a-\z\A-\Z0-9\_\-\*\#\(\)\@]/g,'')"  &nbsp; />
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
											   class="input_bd SPACCPWD"  value=""
											   maxlength="32"
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
										<select name="spType" id="spType" class="input_bd spType" >
											<option value="0" selected="selected">
												<emp:message key="txgl_wghdpz_tdyxcsgl_mwkj" defVal="梦网科技" fileName="txgl"></emp:message>
											</option>
											<option value="10">
												<emp:message key="txgl_wghdpz_tdyxcsgl_qt" defVal="其他" fileName="txgl"></emp:message>
											</option>
										</select>
										<input type="hidden" id="singmutitype" name="singmutitype" value="0"  />
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
										<input type=text name="ip5" id="ip5"  class="aip" onchange="checkDataIp2(this)" placeholder="填写IP地址或域名" maxlength="32">


										<!-- <input type=text name="ip5" id="ip5" maxlength=3 class="a3"
                                            onkeyup="mask(this,event)" onbeforepaste=mask_c()>
                                        .
                                        <input type=text name="ip6" id="ip6" maxlength=3 class="a3"
                                            onkeyup="mask(this,event)" onbeforepaste=mask_c()>
                                        .
                                        <input type=text name="ip7" id="ip7" maxlength=3 class="a3"
                                            onkeyup="mask(this,event)" onbeforepaste=mask_c()>
                                        .
                                        <input type=text name="ip8" id="ip8" maxlength=3 class="a3"
                                            onkeyup="mask(this,event)" onbeforepaste=mask_c()> -->
									</div>
									&nbsp;：
									<input type="text" id="spPort" name="spPort"
										   placeholder="<%=dkh %>" maxlength="5" value="" class="input_bd spPort"
										   onkeyup="if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')"
										   onblur="validatePort(this)" />
									&nbsp;
									<input type="hidden" name="linkstatus" value="" />
									<font color="red" class="tj_up_font">*</font>
									<a href="javascript:void(0)" class="addbackup"><emp:message key="txgl_wghdpz_tdyxcsgl_tj" defVal="添加" fileName="txgl"></emp:message></a>
									&nbsp;
									<span class="tips"><emp:message key="txgl_wghdpz_tdyxcsgl_yyswgszzfwqip" defVal="运营商网关所在主用服务器地址及通讯端口" fileName="txgl"></emp:message></span>

								</td>
							</tr>
						</table>
					</div>
					<table>
						<tr>
							<td class="ywlx_td">
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_ywlx" defVal="业务类型：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<input type="text" id="serviceType" name="serviceType"
										   maxlength="32" class="input_bd serviceType"
										   onkeyup="if(value != value.replace(/[^\w]/g,'')) value = value.replace(/[^\w]/g,'')" />
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_spqydm" defVal="SP企业代码：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<input type="text" id="spid" name="spid" maxlength="32"
										   value="" class="input_bd spid"
										   onkeyup="if(value != value.replace(/[^\w]/g,'')) value = value.replace(/[^\w]/g,'')" />
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
										<option value="2">
											<emp:message key="txgl_wghdpz_tdyxcsgl_dspjf" defVal="对SP计费" fileName="txgl"></emp:message>
										</option>
										<option value="0">
											<emp:message key="txgl_wghdpz_tdyxcsgl_dmdzdmjf" defVal="对目的终端MSISDN计费" fileName="txgl"></emp:message>
										</option>
										<option value="1">
											<emp:message key="txgl_wghdpz_tdyxcsgl_dyzdmjf" defVal="对源终端MSISDN计费" fileName="txgl"></emp:message>
										</option>
										<option value="3">
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
									   value="" id="protocolParam" maxlength="512" />
							</td>
						</tr>
						<tr class="ch_ty">
							<td>
								<span><emp:message key="txgl_wghdpz_tdyxcsgl_zhjflx" defVal="账户计费类型：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<select name="spFeeFlag" id="spFeeFlag" class="input_bd spFeeFlag" >
										<option value="2" <% if(StaticValue.getCORPTYPE() ==1){ %> selected="selected" <%} %>>
											<emp:message key="txgl_wghdpz_tdyxcsgl_hff" defVal="后付费" fileName="txgl"></emp:message>
										</option>
										<option value="1" <% if(StaticValue.getCORPTYPE() ==0){ %> selected="selected" <%} %>>
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
								<input class="input_bd feeUrl" type="text"
									   value="<%=StaticValue.getMbossWebservicesUrl()%>" id="feeUrl" maxlength="512" />
								&nbsp;
								<span class="tips"><emp:message key="txgl_wghdpz_tdyxcsgl_yysyeckuljdz" defVal="运营商余额查看URL链接地址" fileName="txgl"></emp:message></span>
							</td>
						</tr>
					</table>
				</div>

				<table class="passgaeInfo_table" >
					<thead>
					<tr id="passgaeInfo"></tr>
					<tr align="center">
						<td colspan="4" id="btn">
							<input type="button" onclick="submitForm()" id="btnSsu"
								   value="<%=qd %>" class="btnClass5 mr23" />
							<input type="button" id="btnSca" onclick="javascript:back()"
								   value="<%=fh %>" class="btnClass6" />
						</td>
					</tr>
					</thead>
				</table>
			</form>
		</div>
	</div>

	<%--end round_content--%>
	<div class="bottom">
		<div id="bottom_right">
			<div id="bottom_left"></div>
		</div>
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
					<input type="text" name="ip[]"  class="aip" onchange="checkDataIp2(this)" placeholder="填写IP地址或域名" maxlength="32" />
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
				<input type="hidden" name="linkstatus0" value="" class="lstatus" />
				<font color="red" class="xing_font">*</font>
				<span class="handle"> <a class="addbackup"
										 onclick="addNextBackIp(this)" href="javascript:void(0)"><emp:message key="txgl_wghdpz_tdyxcsgl_tj" defVal="添加" fileName="txgl"></emp:message></a> | <a
						class="remove_handle" onclick="removeHandle(this)"
						href="javascript:void(0)"><emp:message key="common_delete" defVal="删除" fileName="common"></emp:message></a> </span>
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script language="javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
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
        $("#protocolCode").load("<%=path%>/gat_userdata.htm?method=getPrTmplOption&accouttype="+$("#accouttype").val()+"&times="+times,function(){
            setTimeout(function() {
                $("#protocolCode").val(40);
                $.post("<%=path%>/gat_userdata.htm?method=getPrTmplContent&times="+times,{protocolCode : 40},
                    function(result){
                        $("#protocolParam").val(result);
                    }
                );
            },1);
        });
        $(".accability").change(function(){
            //声明账户类型
            var accouttype=1;
            var _currentName = $(this).attr('name');
            var _cxChecked = $(".accability[name='cxval']").attr('name');
            var	_cxChecked = $(".accability[name='cxval']").prop('checked');
            var	_dfChecked = $(".accability[name='dxval']").prop('checked')||$(".accability[name='fxval']").prop('checked');
            if (_currentName === 'cxval' && _cxChecked) {
                $(".accability").attr('disabled', true);
                $(this).attr('disabled', false);
                //当前选中的是彩信账户类型
                accouttype=2;
            } else if(_dfChecked){
                $(".accability[name='cxval']").attr('disabled', true);
                //当前选中的是短信或彩信账户类型
                accouttype=1;
            }else{
                $(".accability").attr('disabled', false);
                //默认走的短信账户类型
                accouttype=1;
            }
            //根据选择的账户类型动态改变通讯协议的内容
            $.post("<%=path%>/gat_userdata.htm?method=getPrTmplOption&times="+times,{accouttype : accouttype},
                function(result){
                    $("#protocolCode").empty();
                    $("#protocolCode").append(result);
                    if(accouttype==1)
                    {
                        $("#protocolCode").val("40");
                    }
                    else
                    {
                        $("#protocolCode").val("101");
                    }
                }
            );
        });

        $("#accouttype").change(function(){
            var accouttype = $("#accouttype").val();
            $.post("<%=path%>/gat_userdata.htm?method=getPrTmplOption&times="+times,{accouttype : accouttype},
                function(result){
                    $("#protocolCode").empty();
                    $("#protocolCode").append(result);
                    if(accouttype==1)
                    {
                        $("#protocolCode").val("40");
                    }
                    else
                    {
                        $("#protocolCode").val("101");
                    }
                }
            );
        });
        $("#spType").change(function(){
            chty();
            $("#singmutitype").val($("#spType").val());
            if($("#spType").val() == 0){
                $("#protocolCode").val(40);
                $.post("<%=path%>/gat_userdata.htm?method=getPrTmplContent&times="+times,{protocolCode : 40},
                    function(result){
                        $("#protocolParam").val(result);
                    }
                );
            }
        });
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

        })
        $('.addbackup').click(function(){
            addBackIp();
        });
    });

    function submitForm(){
        // var idArray = ["userid","userpassword","staffname","SPACCID","SPACCPWD","serviceType","spid","ptIp","ptPort","spIp","spPort","speedLimit"];
        //var idArray = ["userid","userpassword","staffname","accouttype","SPACCID","SPACCPWD","ip1","ip2","ip3","ip4","ptPort","ip5","ip6","ip7","ip8","spPort"];
        //var idArray = ["userid","userpassword","staffname","accouttype","SPACCID","SPACCPWD","ip1","ptPort","ip5","spPort"];
        var accouttype = 0;
        var dxval = 0;
        var cxval = 0;
        var fxval = 0;
        var arr_v = new Array();
        $(".accability[type='checkbox']:checked").each(function(){
            arr_v.push($(this).val());
        });
        if (arr_v.length == 0){
            alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_1"));
            return;
        }else if (arr_v.length == 2){
            accouttype = 1;
            dxval = 1;
            fxval = 4;
        } else if (arr_v.length == 1){
            var tempType = arr_v[0];
            if (tempType == 1){
                accouttype = 1;
                dxval = tempType;
            } else if (tempType == 2){
                accouttype = 2;
                cxval = tempType;
            }else if (tempType == 4){
                accouttype = 1;
                fxval = tempType;
            }
        }

        var idArray = ["userid","userpassword","staffname","SPACCID","SPACCPWD","ip1","ptPort","ip5","spPort"];
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
        var userid  = $.trim($("#userid").val());
        if(userid.length>=3&&userid.substr(0,3)=="WY0")
        {
            //alert("通道账号不能以WY0开头！");
            alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_27"));
            return;
        }

        //校验EMP网关IP地址的合法性
        var tempIp=$('div[id=ptIp] input[id=ip1]').val();
        if(!checkDataIp(tempIp)){
            return;
        }
        //校验网关集群地址的合法性
        /* if(!checkClusterAddr()){
            return;
        } */
        //校验网关集群地址的合法性
        if(!checkClusterAddrNew()){
            return;
        }
        var yysSize=$('#yysCont .temp_yys').size();
        //备用ip，格式192.169.1.130:8080,192.169.1.131:8080
        var byIp="";
        //包含主备信息的主备以及ip端口字符串 格式 0:192.169.1.30:8080,1:192.169.1.131:8080
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
                //alert("运营商IP地址及端口"+(i+1)+"IP不合法！");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_3")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_6"));
                return;
            }
            var port=$tr.find("input[name^='spPort']").val();
            if(port=="")
            {
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_3")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_7"));
                return;
            }
            if(!isPort(port)){
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_3")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_8"));
                return;
            }

            if(linklevel=="0")
            {
                zindex++;
                if(zindex>1&&$("#spType").val()!=0){
                    //alert("技术合作商选择其他时，默认是单链路多连接,运营商IP地址及端口主用只能有一个！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_28"));
                    return;
                }
                if(zindex>5&&$("#spType").val()==0){
                    //alert("技术合作商选择梦网科技时，默认是多链路多连接，运营商IP地址及端口主用不能超过五个！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_29"));
                    return;
                }
            }else if(linklevel=="1"){
                bindex++;
                if(bindex>4&&$("#spType").val()!=0){
                    //alert("技术合作商选择其他时，默认是单链路多连接，运营商IP地址及端口备用只能有四个！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_30"));
                    return;
                }
                if(bindex>5&&$("#spType").val()==0){
                    //alert("技术合作商选择梦网科技时，默认是多链路多连接，运营商IP地址及端口备用不能超过五个！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_31"));
                    return;
                }

            }else{
                //alert("运营商IP地址及端口"+(i+1)+"主用/备用必须选！");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_15")+(i+1)+getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_16"));
                return;
            }

            if(linklevel=="1"){
                byIp=byIp+ip+":"+port+",";
            }
            allipport=allipport+ip+":"+port+",";
            zbIP=zbIP+linklevel+":"+ip+":"+port+",";
        }

        if(bindex==0&&$("#spType").val()==0)
        {
            //if(!confirm("您未添加运营商网关备用IP及端口，确定要保存吗？"))
            if(!confirm(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_17")))
            {
                return;
            }
        }

        if(b==0)
        {
            var staffname  = $.trim($("#staffname").val());
            var userpassword  = $.trim($("#userpassword").val());
            //var accouttype=$.trim($("#accouttype").val());
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
            var spid = $.trim($("#spid").val());
            var spType = $.trim($("#spType").val());
            var feeUrl = $.trim($("#feeUrl").val());
            var spFeeFlag = $.trim($("#spFeeFlag").val());


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
            /* if(protocolCode=="5"||protocolCode=="7"||protocolCode=="40"||protocolCode=="41")
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
            else if(protocolCode=="50" ||protocolCode=="102")
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
                //alert("运营商账户ID长度为1-32位！");
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

            $.post("<%=request.getContextPath()%>/gat_userdata.htm",{
                method : "add",
                dxval:dxval,
                cxval:cxval,
                fxval:fxval,
                userid : userid,
                userpassword : userpassword,
                staffname : staffname,
                accouttype:accouttype,
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
                byIp:byIp,
                zbIP:zbIP,
                clusterAddr:clusterAddrs
            },function(result){
                if(result == "itemExists"){
                    //alert("该通道发送账号已存在，请重新输入！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_32"));
                    $("#userid").select()
                }else if(result == "dbError"){
                    //alert("创建失败，通道发送账号数据保存失败！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_33"));
                }else if(result == "nobyip"){
                    //alert("创建失败，通道发送账号未填运营商地址及端口为空或者参数不合法！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_34"));
                }else if(result == "error"){
                    //alert("创建失败！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_35"));
                }else if(result == "false"){
                    //alert("创建失败，网关后端账号数据保存失败！");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_36"));
                }else if(result-99>0){
                    //alert("创建成功！网关编号为："+result+"。");
                    alert(getJsLocaleMessage("txgl","txgl_wghdpz_tdyxcsgl_text_37")+result);
                    location.href=location.href;
                }
            });
        }
    }

</script>
</body>
</html>