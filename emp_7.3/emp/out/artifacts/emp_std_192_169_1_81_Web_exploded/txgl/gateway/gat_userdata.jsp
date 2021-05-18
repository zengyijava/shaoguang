<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.Userdata"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.XtGateQueue"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfPageField"%>
<%@ page
		import="org.apache.commons.beanutils.DynaBean" %>
<%@ page
		import="com.montnets.emp.servmodule.txgl.entity.AgwAccount" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));


	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> spBindMap = (LinkedHashMap<String, String>)request.getAttribute("spBindMap");

	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, AgwAccount> accountMap = (LinkedHashMap<String, AgwAccount>)request.getAttribute("accountMap");

	@ SuppressWarnings("unchecked")
	List<DynaBean> userList = (List<DynaBean>) request.getAttribute("userList");
	@ SuppressWarnings("unchecked")
	Map<String, String> keyIdMap = (Map<String, String>)request.getAttribute("keyIdMap");
	@ SuppressWarnings("unchecked")
	List<Userdata> userListCon = (List<Userdata>) request.getAttribute("userListCon");

	@ SuppressWarnings("unchecked")
	List<XtGateQueue> gateList = (List<XtGateQueue>)request.getAttribute("gateList") ;

	Map<Integer,String> clusMap = (Map<Integer,String>)request.getAttribute("clusMap") ;

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("gat_pagefileds");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = "2000-1400";
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String skin = session.getAttribute("stlyeSkin")==null?"default":
			(String)session.getAttribute("stlyeSkin");
	String status = request.getParameter("status");
	String accouttype = request.getParameter("accouttype");
	String gate_id = request.getParameter("gate_id");
	String gt_userid = request.getParameter("gt_userid");
	String gt_spgate = request.getParameter("gt_spgate");
	String staffName = request.getParameter("staffName");
	String spaccid = request.getParameter("spaccid");
	String gwno = request.getParameter("gwno");
	String gwstate = request.getParameter("gwstate");
	String gwbak = request.getParameter("gwbak");
	boolean isBack = request.getParameter("isback") != null;//是否返回操作
	if(isBack){
		gt_userid = (String)request.getAttribute("gt_userid");
		accouttype = (String)request.getAttribute("accouttype");
		status = (String)request.getAttribute("status");
		gate_id = (String)request.getAttribute("gate_id");
		staffName = (String)request.getAttribute("staffName");
		spaccid = (String)request.getAttribute("spaccid");
		gt_spgate = (String)request.getAttribute("gt_spgate");
		gwno = (String)request.getAttribute("gwno");
		gwstate = (String)request.getAttribute("gwstate");
		gwbak = (String)request.getAttribute("gwbak");
	}
	String txglFrame = skin.replace(commonPath, inheritPath);

	//通道账号
	String tdzh = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_tdzh", request);
	if(tdzh!=null&&tdzh.length()>1){
		tdzh = tdzh.substring(0,tdzh.length()-1);
	}
	//通道账户名称
	String tdzhmc = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_tdzhmc", request);
	if(tdzhmc!=null&&tdzhmc.length()>1){
		tdzhmc = tdzhmc.substring(0,tdzhmc.length()-1);
	}
	// 网关编号
	String wgbh = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_wgbh", request);
	// 网关节点
	String wgjd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_wgjd", request);
	if(wgjd!=null&&wgjd.length()>1){
		wgjd = wgjd.substring(0,wgjd.length()-1);
	}
	//账户类型
	String zhlx = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_zhlx", request);
	//运营商账户ID
	String yyszhid = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_yyszhid", request);
	if(yyszhid!=null&&yyszhid.length()>1){
		yyszhid = yyszhid.substring(0,yyszhid.length()-1);
	}
	//账户状态
	String zhzt = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_zhzt", request);
	if(zhzt!=null&&zhzt.length()>1){
		zhzt = zhzt.substring(0,zhzt.length()-1);
	}
	//已绑通道
	String ybtd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_ybtd", request);
	if(ybtd!=null&&ybtd.length()>1){
		ybtd = ybtd.substring(0,ybtd.length()-1);
	}
	// 网关状态
	String wgzt = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_wgzt", request);
	if(wgzt!=null&&wgzt.length()>1){
		wgzt = wgzt.substring(0,wgzt.length()-1);
	}
	//通道绑定
	String tdbd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_tdbd", request);
	//添加节点
	String tjjd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_tjjd", request);
	if(tjjd!=null&&tjjd.length()>1){
		tjjd = tjjd.substring(0,tjjd.length()-1);
	}
	//修改
	String xg = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_xg", request);
	//查看
	String ck = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_ck", request);
	//通道账户绑定
	String tdzhbd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_tdzhbd", request);
	//请输入通道号码
	String qsrtdhm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_qsrtdhm", request);
	//选择
	String xz = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_xz", request);
	//删除
	String sc = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_sc", request);
	//确定
	String qd = MessageUtils.extractMessage("common", "common_confirm", request);
	//取消
	String qx = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_qx", request);
	//添加节点网关
	String tjwgjd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_tjwgjd", request);
	String findResult = (String)request.getAttribute("findresult");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/common/common.jsp" %>
	<title></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	<style type="text/css">
		.span_gt {
			float:left;
			display: block;
			width: <%="zh_HK".equals(empLangName)?130:160%>px;
		}
		.span_sp {
			float:left;
			display: block;
			width: <%="zh_HK".equals(empLangName)?60:40%>px;
		}
	</style>
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/gat_userdata.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
</head>
<body id="gat_userdata">
<div id="container" class="container">
	<input type="hidden" value="<%=findResult%>" id="findResult" name="findResult"/>
	<input type="hidden" value="<%=skin%>" id="skin" name="skin"/>
	<%-- 当前位置 --%>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
	<%-- 内容开始 --%>
	<form name="pageForm" action="gat_userdata.htm" method="post" id="pageForm">
		<%
			if(btnMap.get(menuCode+"-0")!=null) {
		%>
		<div id="rContent" class="rContent">
			<div class="buttons toggleDiv_up_div" >
				<div id="toggleDiv"></div>
				<a id="add" href="<%=iPath %>/gat_addUserdata.jsp"><emp:message key="txgl_wgqdpz_qyhdgl_xj" defVal="新建" fileName="txgl"></emp:message></a>
			</div>
			<div id="condition">
				<table>
					<tr>
						<td><emp:message key="txgl_wghdpz_wgyxcspz_tdzhmc" defVal="通道账户名称：" fileName="txgl"></emp:message></td>
						<td>
							<input type="text" class="condition_table_td" value="<%=staffName!=null?staffName:"" %>" id="staffName" name="staffName"/>
						</td>
						<td >
							<emp:message key="txgl_wghdpz_wgyxcspz_tdzh" defVal="通道账号：" fileName="txgl"></emp:message>
						</td>
						<td>
							<select name="tdhm_key" id="tdhm_key">
								<option value=""><emp:message key="txgl_wghdpz_wgyxcspz_qb" defVal="全部" fileName="txgl"></emp:message> </option>
								<%
									if (userListCon != null && userListCon.size() > 0)
									{
										for(Userdata userdata : userListCon){
								%>
								<option value="<%=userdata.getUserId() %>" <%=gt_userid!=null&&gt_userid.equals(userdata.getUserId())?"selected":"" %>><%=userdata.getUserId() %></option>
								<% }} %>
							</select>

							<input type="hidden" id="gt_userid" name="gt_userid" value="<%=gt_userid!=null?gt_userid:"" %>" />
						</td>
						<%
							String typename="";
							if(pagefileds!=null&&pagefileds.size()>0){
								LfPageField first=pagefileds.get(0);
								typename=first.getField()+"：";
							}
						%>
						<td >
							<emp:message key="txgl_wghdpz_tdyxcsgl_zhlx" defVal="账户类型：" fileName="txgl"></emp:message>
						</td>
						<td>
							<select name="accouttype" id="accouttype" class="input_bd condition_table_td" isInput="false"  >
								<option value=""><emp:message key="txgl_wghdpz_wgyxcspz_qb" defVal="全部" fileName="txgl"></emp:message></option>
								<%
									if(pagefileds!=null&&pagefileds.size()>1){
										for(int i=1;i<pagefileds.size();i++){
											LfPageField pagefid=pagefileds.get(i);
											String accountType = pagefid.getSubFieldName();
											if("短信通道账户".equals(accountType)){
												accountType = "zh_HK".equals(empLangName)?"SMS channel account":"zh_TW".equals(empLangName)?"短信通道賬戶":"短信通道账户";
											}
											if("彩信通道账户".equals(accountType)){
												accountType = "zh_HK".equals(empLangName)?"MMS channel account":"zh_TW".equals(empLangName)?"彩信通道賬戶":"彩信通道账户";
											}
								%>
								<option value="<%=pagefid.getSubFieldValue() %>" <%=accouttype!=null&&accouttype.equals(pagefid.getSubFieldValue())?"selected":"" %>><%=accountType %></option>
								<%
										}
									}
								%>
							</select>
						</td>
						<td class="tdSer"><center><a id="search"></a></center></td>
					</tr>
					<tr>
						<td>
							<emp:message key="txgl_wghdpz_wgyxcspz_yyszhid" defVal="运营商账户ID：" fileName="txgl"></emp:message>
						</td>
						<td>
							<input type="text" class="condition_table_td" id="spaccid" name="spaccid" value="<%=spaccid!=null?spaccid:"" %>"/>
						</td>
						<td >
							<emp:message key="txgl_wgqdpz_dcspzh_zhzt" defVal="账户状态：" fileName="txgl"></emp:message>
						</td>
						<td>
							<select name="status" id="status"  class="input_bd condition_table_td" isInput="false">
								<option value="" ><emp:message key="txgl_wghdpz_wgyxcspz_qb" defVal="全部" fileName="txgl"></emp:message></option>
								<option value="0" <%="0".equals(status)?"selected":"" %>><emp:message key="txgl_wghdpz_wgyxcspz_yjh" defVal="已激活" fileName="txgl"></emp:message></option>
								<option value="1" <%="1".equals(status)?"selected":"" %>><emp:message key="txgl_wghdpz_wgyxcspz_ysx" defVal="已失效" fileName="txgl"></emp:message></option>
							</select>
						</td>
						<td >
							<emp:message key="txgl_wghdpz_wgyxcspz_ybtd" defVal="已绑通道：" fileName="txgl"></emp:message>
						</td>
						<td>
							<select name="tdhm_key_spgate" id="tdhm_key_spgate">
								<option  gate_id="" value=""><emp:message key="txgl_wghdpz_wgyxcspz_qb" defVal="全部" fileName="txgl"></emp:message></option>
								<%
									if (gateList != null && gateList.size() > 0)
									{
										for(XtGateQueue xt : gateList){
								%>
								<option gate_id="<%=xt.getId() %>" value="<%=xt.getSpgate() %>" <%=gt_spgate!=null&&gt_spgate.equals(xt.getSpgate())?"selected":"" %> ><%=xt.getSpgate() %>(<%=xt.getSpisuncm()-1==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_lt", request):xt.getSpisuncm()-0==0?
										MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_yd", request):xt.getSpisuncm()-21==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_dx", request):xt.getSpisuncm()-5==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_gw", request):""%><%=xt.getGateType()-1==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_dux", request):xt.getGateType()-2==0?
										MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_cx", request):""%>)</option>
								<% }} %>
							</select>
							<input type="hidden" id="gt_spgate" name="gt_spgate" value="<%=gt_spgate!=null?gt_spgate:"" %>" />
							<input type="hidden" id="gate_id" name="gate_id" value="<%=gate_id!=null?gate_id:"" %>" />
						</td>
						<td></td>
					</tr>
					<tr>
						<td><emp:message key="txgl_wghdpz_wgyxcspz_wgbhh" defVal="网关编号：" fileName="txgl"></emp:message></td>
						<td><input type="text" class="condition_table_td" id="gwno" name="gwno" value="<%=gwno!=null?gwno:"" %>"/></td>
						<td><emp:message key="txgl_wghdpz_wgyxcspz_wgzt" defVal="网关状态：" fileName="txgl"></emp:message></td>
						<td>
							<select name="gwstate" id="gwstate"  class="input_bd condition_table_td" isInput="false">
								<option value="" ><emp:message key="txgl_wghdpz_wgyxcspz_qb" defVal="全部" fileName="txgl"></emp:message></option>
								<option value="1" <%="1".equals(gwstate)?"selected":"" %>><emp:message key="txgl_wghdpz_wgyxcspz_yx" defVal="运行" fileName="txgl"></emp:message></option>
								<option value="0" <%="0".equals(gwstate)?"selected":"" %>><emp:message key="txgl_wghdpz_wgyxcspz_jy" defVal="禁用" fileName="txgl"></emp:message></option>
							</select>
						</td>
						<td><emp:message key="txgl_wghdpz_wgyxcspz_wgjd" defVal="网关节点：" fileName="txgl"></emp:message></td>
						<td>
							<select name="gwbak" id="gwbak"  class="input_bd condition_table_td" isInput="false">
								<option value="" ><emp:message key="txgl_wghdpz_wgyxcspz_qb" defVal="全部" fileName="txgl"></emp:message></option>
								<option value="1" <%="1".equals(gwbak)?"selected":"" %>><emp:message key="txgl_wghdpz_wgyxcspz_jd1" defVal="节点1" fileName="txgl"></emp:message></option>
								<option value="0" <%="0".equals(gwbak)?"selected":"" %>><emp:message key="txgl_wghdpz_wgyxcspz_jd2" defVal="节点2" fileName="txgl"></emp:message></option>
							</select>
						</td>
						<td>&nbsp;</td>
					</tr>
				</table>
			</div>
			<table id="content"  class="content_table">
				<thead>
				<tr>
					<th>
						<%=tdzh %>
					</th>
					<th>
						<%=tdzhmc %>
					</th>
					<th>
						<%=wgbh %>
					</th>
					<th>
						<%=wgjd %>
					</th>
					<th>
						<%=zhlx %>
					</th>
					<th>
						<%=yyszhid %>
					</th>
					<th>
						<%=zhzt %>
					</th>
					<th>
						<%=ybtd %>
					</th>
					<th>
						<%=wgzt %>
					</th>
					<th>
						<%=tdbd %>
					</th>
					<th>
						<%=tjjd %>
					</th>
					<th>
						<%=xg %>
					</th>
				</tr>
				</thead>
				<tbody>
				<%
					if(userList != null && userList.size() > 0)
					{
						for(int i=0 ;i< userList.size();i++)
						{
							DynaBean user = userList.get(i);
							String uid = user.get("uid").toString();
							String keyId = keyIdMap.get(uid);
							String userid = user.get("userid").toString();
							String accounttype = user.get("accounttype").toString();
							int accability = Integer.parseInt(user.get("accability").toString());
							boolean isWy=(userid.length()>=3 && "WY0".equals(userid.substring(0,3)));
							String gtName = user.get("staffname").toString();
							AgwAccount agwAccount = accountMap.get(uid);
							//权重 0为主 其他为备
							String gweight = user.get("gweight")==null?null:user.get("gweight").toString();
							String gtNo;

							if(user.get("gw_no") == null)
							{
								gtNo = agwAccount == null?"":agwAccount.getGwNo()+"";
							}else{
								gtNo = user.get("gw_no").toString();
							}
							if(gtName!=null)
							{
								gtName = gtName.replaceAll("<","&lt").replaceAll(">","&gt").replaceAll("'","&acute;");
							}
							//运行状态
							String runState = user.get("runstatus") == null?"":user.get("runstatus").toString();
							String runStateStr = "-";
							if("1".equals(runState))
							{
								// runStateStr = "运行";
								runStateStr = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_yx", request);
							}
							else if("0".equals(runState))
							{
								//runStateStr = "禁用";
								runStateStr = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_jy", request);
							}
				%>
				<tr>
					<td><%=userid%></td>
					<td class="textalign"><xmp><%=user.get("staffname").toString()%></xmp></td>
					<td><%=gtNo%></td>
					<td><%="0".equals(gweight)? MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_jd1", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_jd2", request)%></td>
					<td class="ztalign">
						<%
							if ("1".equals(accounttype)) {
								if(1 == (int)(accability&0x00000001) && 4 == (int)(accability&0x00000004)){
						%>
						<font color="#159800">短信/富信通道账户</font>
						<%
						}else if(1 == (int)(accability&0x00000001) && 4 != (int)(accability&0x00000004)){
						%>
						<font color="#159800"><emp:message key="txgl_wghdpz_wgyxcspz_dxtdzh" defVal="短信通道账户" fileName="txgl"></emp:message></font>
						<%}else if(4 == (int)(accability&0x00000004) && 1 != (int)(accability&0x00000001)){ %>
						<font color="#159800">富信通道账户</font>
						<%
							}
						} else {
						%>
						<font color="#f1913c"><emp:message key="txgl_wghdpz_wgyxcspz_cxtdzh" defVal="彩信通道账户" fileName="txgl"></emp:message></font>
						<%
							}
						%>
						<%--									<% if ("1".equals(accounttype)){ %>
                                                                <font color='#159800'><emp:message key="txgl_wghdpz_wgyxcspz_dxtdzh" defVal="短信通道账户" fileName="txgl"></emp:message></font>
                                                            <% } else { %>
                                                                <font color='#f1913c'><emp:message key="txgl_wghdpz_wgyxcspz_cxtdzh" defVal="彩信通道账户" fileName="txgl"></emp:message></font>
                                                            <% } %>--%>
					</td>
					<td>
						<%
							if(agwAccount == null || "".equals(agwAccount.getSpAccid()))
							{
								out.print("<font color='red'>"+MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_yysxxbwz", request)+"</font>");
							}
							else{
								out.print(agwAccount.getSpAccid());
							}
						%>
					</td>

					<td class="ztalign">
						<center>
							<div class="setControl"></div>
							<%
								if ("0".equals(user.get("status").toString())) {
							%>
							<select  name="userState<%=uid %>" idx="<%=uid %>" class="input_bd">
								<option value="0" selected="selected"><emp:message key="txgl_wghdpz_wgyxcspz_yjh" defVal="已激活" fileName="txgl"></emp:message></option>
								<option value="1" ><emp:message key="txgl_wghdpz_wgyxcspz_sx" defVal="失效" fileName="txgl"></emp:message></option>
							</select>
							<%--<%
                                if ((user.getStatus() == 0) && (accountMapByspaccid.get(uid) == null || spBindMap.get(uid) == null)|| "".equals(accountMapByspaccid.get(uid)))
                                {
                                   out.print("<font color='red'>(信息不完整，请补充)</font>");
                                }
                            %>
                            --%><%
								}else{
								%>
							<select  name="userState<%=uid %>" idx="<%=uid %>" class="input_bd">
								<option value="1" selected="selected"><emp:message key="txgl_wghdpz_wgyxcspz_ysx" defVal="已失效" fileName="txgl"></emp:message></option>
								<option value="0" ><emp:message key="txgl_wghdpz_wgyxcspz_jh" defVal="激活" fileName="txgl"></emp:message></option>
							</select>
							<%
								}
							%>
						</center>
					</td>
					<td>
						<%
							String htmlStr = spBindMap.get(uid);
							if(htmlStr!=null && !"".equals(htmlStr))
							{
								String info = "";
								String[] htmlStrS = htmlStr.split("；");
								if(htmlStrS.length > 2)
								{
									info = htmlStrS[0]+"；"+htmlStrS[1]+"...";
								}
								else
								{
									info = htmlStr.substring(0,htmlStr.length()-1);
								}
								out.print("<a onclick=javascript:modify(this)>"+info+"</a>");
							}
							else
							{
								out.print("<font color='red'>"+MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_wbdtdhqbd", request)+"</font>");
							}
						%>
						<input type="hidden" value="<%=htmlStr%>" class="htmlStr">
					</td>
					<%
						String tmpInfo = clusMap.get(gtNo);
						if(tmpInfo == null || gweight == null)
						{
							tmpInfo = "-";
						}
					%>
					<td><%=runStateStr%></td>
					<td>
						<%if(isWy)
						{
							out.print("-");
						}else
						{%>
						<a href="javascript:doSpBind('<%=userid %>','<%=uid %>','<%=accounttype %>','<%=gtName %>');"><emp:message key="txgl_wghdpz_wgyxcspz_bdtd" defVal="绑定通道" fileName="txgl"></emp:message></a>
						<%}%>
					</td>
					<td>
						<%
							if("0".equals(gweight))
							{
						%>
						<a class="bak-add" uid="<%=uid%>" bak="<%=tmpInfo%>"><emp:message key="txgl_wghdpz_wgyxcspz_tjjd" defVal="添加节点" fileName="txgl"></emp:message></a>
						<%
						}
						else
						{%>-<%}
					%>

					</td>
					<td>
						<%if(isWy)
						{
						%>
						<a href="javascript:toEditByWy('<%=uid %>','<%=accounttype%>','<%=keyId%>');"><emp:message key="txgl_wghdpz_wgyxcspz_ck" defVal="查看" fileName="txgl"></emp:message></a>
						<%
						}else
						{%>
						<a href='<%=path %>/gat_userdata.htm?method=toEdit&uid=<%=uid %>&accouTtype=<%=accounttype%>&keyId=<%=keyId%>'><emp:message key="txgl_wghdpz_wgyxcspz_xg" defVal="修改" fileName="txgl"></emp:message></a>
						<%}%>
					</td>
				</tr>
				<%
						}
					}else
					{
						out.print("<tr><td colspan='12'>"+MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_wjl", request)+"</td></tr>");
					}
				%>
				</tbody>
				<tfoot>
				<tr>
					<td colspan="12">
						<div id="pageInfo"></div>
					</td>
				</tr>
				</tfoot>
			</table>
		</div>
		<%} %>

		<div id="modify" title="<emp:message key="txgl_wghdpz_wgyxcspz_ybtd" defVal="已绑通道" fileName="txgl"></emp:message>"  class="modify">
			<table class="modify_table">
				<thead>
				<tr class="msgcont_tr">
					<td class="msgcont_td">
						<span><xmp id="msgcont" class="msgcont" ></xmp></span>
					</td>
				</tr>
				<tr class="msgcont_down_tr">
					<td>
					</td>
				</tr>

				</thead>
			</table>
		</div>

		<div  id="gwspbind" title="<%=tdzhbd %>" class="gwspbind">
			<table class="gwspbind_table">
				<tr align="left">
					<td colspan="3">
						<div class="tdzh_up_div ">
							<div class="tdzh_div"><emp:message key="txgl_wghdpz_wgyxcspz_tdzh" defVal="通道账户：" fileName="txgl"></emp:message>&nbsp;<span id="spbinduser" name="spbinduser"></span></div>
							<div class="yxtd_div "><emp:message key="txgl_wghdpz_wgyxcspz_yxtd" defVal="已选通道" fileName="txgl"></emp:message>&nbsp;<span id="bind_num">0</span><emp:message key="txgl_wghdpz_wgyxcspz_t" defVal="条" fileName="txgl"></emp:message></div>
							<input type="hidden" id="spuseruid" name="spuseruid" />
							<input type="hidden" id="spgatetype" name="spgatetype" />
						</div>
					</td>
				</tr>

				<tr>
					<td class="bind_spisuncm_td" >
						<div class="left_dep div_bd " align="left" >
							<div class="title_bg div_bd bind_spisuncm_div" >
									<span class="bind_spisuncm_span">
										<select id="bind_spisuncm" name="bind_spisuncm">
											<option value=""><emp:message key="txgl_wghdpz_wgyxcspz_qb" defVal="全部" fileName="txgl"></emp:message></option>
											<option value="0" ><emp:message key="txgl_wghdpz_wgyxcspz_yd" defVal="移动" fileName="txgl"></emp:message></option>
											<option value="1"><emp:message key="txgl_wghdpz_wgyxcspz_lt" defVal="联通" fileName="txgl"></emp:message></option>
											<option value="21"><emp:message key="txgl_wghdpz_wgyxcspz_dx" defVal="电信" fileName="txgl"></emp:message></option>
											<option value="5"><emp:message key="txgl_wghdpz_wgyxcspz_gw" defVal="国外" fileName="txgl"></emp:message></option>
										</select>
									</span>
								<div  class="div_bd <%="zh_HK".equals(empLangName)?"gatenum_div1":"gatenum_div2"%>"  >
									<input type="text" name="gatenum" id="gatenum" size="20"  class="div_db <%="zh_HK".equals(empLangName)?"gatenum1":"gatenum2"%>"  placeholder="<%=qsrtdhm %>" />
									<a id="switch" class="switch"  onclick="switchclick()">
										<img id="searchIcon" src="<%=skin %>/images/query.png" border="0">
									</a>
								</div>
							</div>
							<div class="clear"></div>
							<div id="left" class=""  align="left">

							</div>
						</div>
					</td>
					<td class="toLeft_td" align="center">
						<div class="cen_btn">
							<input class="btnClass1" type="button" id="toLeft" value="<%=xz %>" onclick="javascript:router();">
						</div>
						<div class="cen_btn">
							<input class="btnClass1" type="button" id="toRight" value="<%=sc %>" onclick="javascript:moveRight();">
						</div>
					</td>
					<td class="right_td">
						<div class="left_dep div_bd right_div"  >
							<%--
                            <h3 class="div_bd title_bg" style="text-align: left;font-weight: normal;font-size: 14px;padding-left: 5px;">
                                已选通道<span id="bind_num">0</span>条
                            </h3>
                            --%>
							<div id="right" class="right">
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td id="btn" align="center" colspan="3" class="btn">
						<input type="button" onclick="bindSure()" id="btnSsu"  value="<%=qd %>" class="btnClass5 mr23"/>
						<input type="button" id="btnSca" onclick="btcancel();"  value="<%=qx %>" class="btnClass6"/>
						<br/>
					</td>
				</tr>

			</table>
		</div>

		<%-- 内容结束 --%>
		<%-- foot开始 --%>
		<div class="bottom">
			<div id="bottom_right">
				<div id="bottom_left"></div>
			</div>
		</div>
		<%-- foot结束 --%>
	</form>
</div>
<div id="des-info" title="<%=tjwgjd %>" uid="">
	<ul>
		<li>
			<emp:message key="txgl_wghdpz_wgyxcspz_wgjd2" defVal="网关节点2：" fileName="txgl"></emp:message><label name="td-bak"></label>
		</li>
		<li>
			<emp:message key="txgl_wghdpz_wgyxcspz_tdzh" defVal="通道账号：" fileName="txgl"></emp:message><label name="td-0"></label>
		</li>
		<li>
			<emp:message key="txgl_wghdpz_wgyxcspz_tdzhmc" defVal="通道账号名称：" fileName="txgl"></emp:message><label name="td-1"></label>
		</li>
		<li>
			<emp:message key="txgl_wghdpz_tdyxcsgl_zhlx" defVal="账户类型：" fileName="txgl"></emp:message><label name="td-4"></label>
		</li>
		<li>
			<emp:message key="txgl_wghdpz_wgyxcspz_yyszhid" defVal="运营商账户ID：" fileName="txgl"></emp:message><label name="td-5"></label>
		</li>
		<li>
			<emp:message key="txgl_wghdpz_wgyxcspz_wgjd1" defVal="网关节点1：" fileName="txgl"></emp:message><label name="td-2"></label>
		</li>
	</ul>
	<div class="bak-ok_div">
		<input type="button" id="bak-ok" value="<%=qd %>" class="btnClass5 mr23" />
		<input type="button" id="bak-cancel" value="<%=qx %>" class="btnClass6" />
	</div>
</div>
<div class="clear"></div>
<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.placeholder.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=iPath%>/js/gat_userdata.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
        $('#accouttype,#status,#gwstate,#gwbak').isSearchSelect({'width':'150','isInput':false,'zindex':0},function(data){});
    });
    //赋值传入外部js文件
    var elNameWidth = <%="zh_HK".equals(empLangName)?85:110%>;
</script>
</body>
</html>
