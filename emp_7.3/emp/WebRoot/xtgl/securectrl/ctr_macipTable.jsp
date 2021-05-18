<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.securectrl.vo.LfMacIpVo"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath .substring(0, inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session
			.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session
			.getAttribute("titleMap");
	String menuCode = titleMap.get("loginSafeInfo");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	@SuppressWarnings("unchecked")
	List<LfMacIpVo> macipVoList = (List<LfMacIpVo>) request
			.getAttribute("macIpVoList");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>) request
			.getAttribute("conditionMap");
			
	LfSysuser lfSysuser = (LfSysuser)session.getAttribute("loginSysuser");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	int colspan = 0;

%>
<html>
	<head>
	<%@include file="/common/common.jsp" %>
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/ctr_macipTable.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ctr_macipTable.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	</head>
	<body id="ctr_macipTable">
			<div id="ipsDiv" class="ipsDiv">
				<div id="ipMsg" class="ipMsg"></div>
			</div>
			<div id="macsDiv" class="macsDiv">
				<div id="macMsg" class="macMsg"></div>
			</div>
					<table id="content">
								<thead>
									<tr>
									<th><input type="checkbox" name="macips" value=""
							onclick="checkAlls(this,'macip')" /></th>
										<th>
											<emp:message key="xtgl_czygl_gjaqsz_ssjg" defVal="所属机构" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_cswh_whgl_dlzh" defVal="登录账号" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_cswh_whgl_czymc" defVal="操作员名称" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_czygl_gjaqsz_ipdz" defVal="IP地址" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_czygl_gjaqsz_macdz" defVal="MAC地址" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_czygl_gjaqsz_dtkl" defVal="动态口令" fileName="xtgl"/>
										</th>
									<%-- 	<th>
											创建人
										</th>
										<th>
											创建时间
										</th> --%>
										<%
											if (btnMap.get(menuCode + "-2") != null) {
												colspan++;
											}
											
											if (colspan > 0) {
										%>
										<th  <%out.print(" colspan=" + String.valueOf(colspan));%>>
											<emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
										</th>
										<%
											}
										%>
									</tr>
								</thead>
								<tbody>
									<%
										if (macipVoList != null && macipVoList.size() > 0) {

											for (int v = 0; v < macipVoList.size(); v++) {
												LfMacIpVo macipVo = macipVoList.get(v);
									%>
									<tr>
									<td  class="getDtpwd_td"><input type="checkbox" id='<%=macipVo.getDtpwd()%>' value="<%=macipVo.getGuid()%>" 
									name="macip" isIpNull="<%=macipVo.getIpaddr()!=null?macipVo.getIpaddr():"" %>" 
									isMacNull="<%=macipVo.getMacaddr()!=null?macipVo.getMacaddr():"" %>"/></td>
										<td class="textalign getDepName_td" >
											<%=macipVo.getDepName()%>
										</td>
										<td class="textalign getUserName_td" >
											<%=macipVo.getUserName()%>
										</td>
										<td class="textalign getName_td"  >
											<label><xmp><%=macipVo.getName()%></xmp></label>
										</td>
										
										<td class="textalign getIpaddr_td" >
											 <%
											 	String ipaddr = macipVo.getIpaddr();
								 				String[] ips = null;
								 				String ip = "";
								 				if (ipaddr != null && !"".equals(ipaddr)) {
								 					ips = ipaddr.split(",");
								 				}
								 				if (ips != null && ips.length > 1) {
								 					ip = ipaddr.substring(0,11) + "...";
								 				} else if (ips != null && ips.length == 1) {
								 					ip = ips[0];
								 				}
											 %>
											<%
												if (ip != "") {
											%>
											<a onclick="javascript:allIps(this)">
											 	<label class="ipaddr_label"><%=ipaddr%>
											 	</label>
												<%=ip%>
											</a>
											<%
												} else {
											%>
											-<%
												}
											%>
											
										</td>
										<td class="textalign macaddr_td"  >
											 <%
											 	String macaddr = macipVo.getMacaddr();
											 				String[] macs = null;
											 			 	String mac = "";
											 				if (macaddr != null && !"".equals(macaddr)) {
											 					macs = macaddr.split(",");
											 				}
											 				if (macs != null && macs.length > 1) {
											 					mac = macs[0].substring(0,10) + "...";
											 				} else if (macs != null && macs.length == 1) {
											 					mac = macs[0];
											 				}
											 %>
											<%
												if (mac != "") {
											%>
											<a onclick="javascript:allMacs(this)">
											 	<label class="macaddr_label"><%=macaddr%>
											 	</label>
												<%=mac%>
											</a>
											<%
												} else {
											%>
											-<%
												}
											%>
										</td>
										<td  class="getDtpwd_td">
										<%if(macipVo.getDtpwd() == 1){ %>
										<emp:message key="xtgl_czygl_gjaqsz_qy" defVal="启用" fileName="xtgl"/>
										<%}else{ %>
										<emp:message key="xtgl_czygl_gjaqsz_wqy" defVal="未启用" fileName="xtgl"/>
										<%} %>
										</td>
										<%--
										<td class="textalign">
											<%=macipVo.getCreatorName() != null
							&& !"".equals(macipVo.getCreatorName()) ? macipVo
							.getCreatorName() : "-"%>
										</td>
										<td>
										<%=macipVo.getCreatTime() != null
							&& !"".equals(macipVo.getCreatTime()) ? sdf
							.format(macipVo.getCreatTime()) : "-"%>
										</td>
										--%>
										<% 
											if (btnMap.get(menuCode + "-2") != null ) {
										%>
										<td  class="shlcgl_xg_td">
										 <a href="javascript:update('<%=macipVo.getGuid()%>','<%=macipVo.getIpaddr()%>','<%=macipVo.getMacaddr()%>','<%=macipVo.getDtpwd()%>')"><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a>
										 	</td>
										  <%}  %>
									</tr>
									<%
										}} else {
									%>
										<tr>
											<td <%out.print(" colspan=" + String.valueOf(colspan+7));%> align="center">
												<emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/>
											</td>
										</tr>
									
									<%}%>
									
								</tbody>
								<tfoot>
							<tr>
								<td <%out.print(" colspan=" + String.valueOf(colspan+7));%>>
								<div></div>
									<div id="pageInfo">   </div>
									<div>     </div>
									<div>     </div>
									<div>     </div>
									<div>     </div>
									<div>     </div>
								</td>
							</tr>
						</tfoot>
					</table>
<div id="binddiv" title="<emp:message key='xtgl_czygl_gjaqsz_qxbd' defVal='取消绑定' fileName='xtgl'/>" class="binddiv">
		<center>
		<table class="binddiv_table">
			<tr >
				<td class="ipDiv" id="ipDiv" >
					<div class="ipDiv_div ">
						<h3 class="ipDiv_div_h3 "><img alt="" class="ipdz_img" src="">&nbsp;<emp:message key="xtgl_czygl_gjaqsz_ipdz" defVal="IP地址" fileName="xtgl"/></h3>
					</div>
					<input type="hidden" id="lmiid" name="lmiid">
				</td>
				<td class="macDiv" id="macDiv">
					<div class="macDiv_div">
					<h3 class="macDiv_div_h3 "><img alt="" class="macdz_img" src="">&nbsp;<emp:message key="xtgl_czygl_gjaqsz_macdz" defVal="MAC地址" fileName="xtgl"/>
					</h3>
					</div>
				</td>
			</tr>
			<tr >
				<td colspan="2" class="macdz_down_tr_td">
				</td>
			</tr>
			<tr>
				<td class="ipDiv2" id="ipDiv2">
					<div id="ip" class="ip ">
					</div>
				</td>
				<td class="macDiv2" id="macDiv2">
					<div id="mac" class="mac">
					</div>
				</td>
			</tr>
			<tr >
			<td colspan="2" class="mac_down_tr_td">
			
			</td>
			</tr>
			<tr align="center">
				<td colspan="2"   class="bineSure_up_tr_td "> 
				</td>
			</tr>
			<tr align="center">
				<td align="center" colspan="2" > 
				<br/> <a onclick="bineSure()" class="bineSure_a"></a>
				</td>
			</tr>
		</table></center>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
	<script type="text/javascript"	src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript"	src="<%=iPath%>/js/ctr_macipTable.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script>
		$(document).ready(function(){
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		});
		
		$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});
			
		function cancelBind(lmiid,ipaddr,macaddr){
		      var type = '<%=conditionMap.get("type")%>'; 
		      $("#lmiid").val(lmiid);
		      if("1" == type){
		      	   $("#mac").empty();
		      	   $("#macDiv").css("display","inline");
		      	   $("#macDiv2").css("display","inline");
		      	   var macs = "";
		      	   if(macaddr != "null" && macaddr != ""){
			  	   	    macs = macaddr.split(",");
			 	   }
			 	   for(var i=0;i<macs.length;i++){
			       var str = "<input type='checkbox' name='checkMac' id='s"+macs[i]+"' value='"+macs[i]+"' checked />";
			  	   $("#mac").append(str);
			  	   $("#mac").append("<label>"+macs[i]+"</label><br/>");
			  	   }
		      }else{
				  $("#ip").empty();
				   $("#ipDiv").css("display","inline");
				   $("#ipDiv2").css("display","inline");
				  var ips = "";
				  if(ipaddr != "null" && ipaddr != ""){
				  	   ips = ipaddr.split(",");
				  }
				
				  for(var i=0;i<ips.length;i++){
				       var str = "<input type='checkbox' name='checkIp' id='s"+ips[i]+"' value='"+ips[i]+"' checked />";
				  	   $("#ip").append(str);
				  	   $("#ip").append("<label>"+ips[i]+"</label><br/>");
				  }
			  }
		    $("#binddiv").dialog("open");
		}
	</script>
	</body>
</html>