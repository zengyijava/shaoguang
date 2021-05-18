<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfDep"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.accountpower.LfMtPri"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="java.util.Iterator"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	String uri = (String)request.getAttribute("uri");
	String uriCode = uri.substring(uri.lastIndexOf("_")+1,uri.lastIndexOf("."));

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get(uriCode);
	@ SuppressWarnings("unchecked")
	List<LfSpDepBind> userdataList = (List<LfSpDepBind>) request.getAttribute("userdataList");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	@ SuppressWarnings("unchecked")
	List<LfMtPri> accountList = (List<LfMtPri>)request.getAttribute("accountList");
	@ SuppressWarnings("unchecked")
	Map<Long, String> keyIdMap = (Map<Long, String>)request.getAttribute("keyIdMap");
	@ SuppressWarnings("unchecked")
	Map<String,LfSysuser> sysUserMap=(Map<String,LfSysuser>)request.getAttribute("sysUserMap");
	@ SuppressWarnings("unchecked")
	Map<String,Userdata> userMap=(Map<String,Userdata>)request.getAttribute("userMap");
	if(sysUserMap == null)
	{
		sysUserMap = new LinkedHashMap<String,LfSysuser>();
	}
	if(userMap == null)
	{
		userMap = new LinkedHashMap<String,Userdata>();
	}
	
	String userid = request.getParameter("userid");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String txglFrame = skin.replace(commonPath, inheritPath);
	
	//关系绑定
	String gxbd = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_gxbd", request);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
 		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/pgb_access.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/pgb_access.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="pgb_access">
		<div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<form name="pageForm" action="<%=uri %>" method="post"
					id="pageForm">
					<div id="corpCode" class="hidden"></div>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent rContent"  >
			<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1")!=null) { %>
						<a id="bind"><emp:message key="txgl_wgqdpz_spzhczhs_gxbd" defVal="关系绑定" fileName="txgl"></emp:message></a>
						<%} %>
					</div>
					<div id="condition">
					<table>
						<tr>
							<td>
								<span><%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_3",request)%></span>
							</td>
							<td>
							<label>
								<%String userId = request.getParameter("userid");%>
									<input type="text" name="userid" id="userid" value="<%=userId==null?"":userId %>"
										onkeyup="if(value!=value.replace(/[^\w\/]/g,''))value=value.replace(/[^\w\/]/g,'')" />
								</label>
							</td>
							<td class="tdSer"><center><a id="search"></a></center></td>
						</tr>
					</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_1",request)%>
								</th>
								<th>
									<emp:message key="txgl_wgqdpz_spzhczhs_zhmc" defVal="账户名称" fileName="txgl"></emp:message>
								</th>
								<th>
									<emp:message key="txgl_wgqdpz_spzhczhs_bdczy" defVal="绑定操作员" fileName="txgl"></emp:message>
								</th>
								 <th>
									<emp:message key="txgl_wgqdpz_spzhbd_zhlx" defVal="账户类型" fileName="txgl"></emp:message>
								</th>
								<th>
									<emp:message key="txgl_wgqdpz_spzhbd_zhzt" defVal="账户状态" fileName="txgl"></emp:message>
								</th>
								<th>
									<emp:message key="txgl_wgqdpz_yyshdgl_cz" defVal="操作" fileName="txgl"></emp:message>
								</th>
							</tr>
						</thead>
						<tbody>
						<%
							if(accountList != null && accountList.size()>0)
							{
								for(LfMtPri account : accountList)
								{
									String spUser = account.getSpuserid();
									String keyId =  keyId = keyIdMap.get(account.getId());
									Userdata userdata = userMap.get(spUser);
									if(userdata == null)
									{
										userdata = new Userdata();
									}
						%>
							<tr>
								<td>
									<%=spUser %>
								</td>
								
								<td class="textalign"><%=userdata.getStaffName()==null?"":userdata.getStaffName() %></td>
								<td class="textalign"> 
								<%
								String userName =sysUserMap.get(account.getUserid().toString())==null?"":sysUserMap.get(account.getUserid().toString()).getName();
								 %>
									<%=userName%>
								</td>
								<td class="ztalign">
									<emp:message key="txgl_wgqdpz_dcspzh_empjrzh" defVal="EMP接入账户" fileName="txgl"></emp:message>
								</td>
								<td class="ztalign">
									<% 
									if (userdata.getStatus()!=null && userdata.getStatus() == 0) { %>
									<emp:message key="txgl_wgqdpz_dcspzh_yjh" defVal="已激活" fileName="txgl"></emp:message>
									<% } else  { %>
									<emp:message key="txgl_wgqdpz_dcspzh_ysx" defVal="已失效" fileName="txgl"></emp:message>
									<% }%>
								</td>
								
								
								<td>
									<a href="javascript:del(<%=account.getId() %>,'<%=keyId %>')"><emp:message key="txgl_wgqdpz_spzhczhs_cxbd" defVal="撤销绑定" fileName="txgl"></emp:message></a>
								</td>
							</tr>
						<%			
								}
							}else{out.print("<tr><td colspan='7' align='center'>"+MessageUtils.extractMessage("txgl", "txgl_wgqdpz_qyhdgl_wjl", request)+"</td></tr>");}
						%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="7">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					</div>
					</form>
					</div>
		<div class="clear"></div>
		
	<div id="binddiv" title="<%=gxbd %>" class="binddiv">
		<center>
		<table class="binddiv_table">
			<tr >
				<td class="binddiv_table_tr" >
					<div class="binddiv_table_tr_div"  >
						<h3 class="binddiv_table_tr_div_h3 ">
<%--						<img alt="" style="vertical-align:middle"  src="<%=iPath %>/img/jt.gif">--%>
						&nbsp;<%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_3",request)%></h3>
					</div>
				</td>
				<td class="czy_td">
					<div class="czy_div"  >
					<h3 class="czy_div_h3 ">
					&nbsp;<emp:message key="txgl_wgqdpz_spzhczhs_czy" defVal="操作员：" fileName="txgl"></emp:message>
					</h3>
					</div>
				</td>
			</tr>
			<tr>
				<td class="SpUser_td" >
					<div class="div_bd SpUser_div"  >
<%--					<br>--%>
						<%
						if(userdataList != null && userdataList.size()>0)
						{
							for(LfSpDepBind lfspbind : userdataList)
							{
						 %>
						 <input type="checkbox" name="checkSpUser" id="s<%=lfspbind.getSpUser() %>" value="<%=lfspbind.getSpUser() %>"/>
						 <label for="s<%=lfspbind.getSpUser() %>"><%=lfspbind.getSpUser() %></label><br/>
						 <%}} %>
					</div>
				</td>
				<td class="userName_td">
					<div id="userDiv"  class="div_bd userDiv" >
						<input type="text" id="userName" name="userName" class="hidden"/>
						<ul id="dropdownMenu2" class="tree"></ul>
					</div>
				</td>
			</tr>
			<tr >
			<td colspan="2" class="dropdownMenu2_down_tr_td">
			
			</td>
			</tr>
			<tr align="center">
				<td colspan="2"> 
				</td>
			</tr>
			<tr align="center">
				<td align="center" colspan="2" > 
				<br/> <input type="button" class="btnClass5 indent_none bind"   value="<%=gxbd %>" id="bind" onclick="bineSure()" >
				</td>
			</tr>
		</table></center>
	</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>	 
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
	<script type="text/javascript" src="<%=iPath %>/js/pgb_access.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>

	<script type="text/javascript">
	var zTree3;
	var zTree2;		
	
	var setting3;
	var setting2;
	
	var deptArray=[];
	var userArray=[];
	
	var zNodes3 =[];
	var zNodes2 =[];
	
	$(document).ready(function() {
	getLoginInfo("#corpCode");
		var time = new Date();
			
		$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$(this).addClass("collapse");
			}, function() {
				$("#condition").show();
				$(this).removeClass("collapse");
			});
		$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});
			
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		$('#search').click(function(){submitForm();});
		$("#binddiv").dialog({
			autoOpen: false,
			height: 510,
			width: 520,
			modal: true,
			//buttons :{
					///"确定":function(){
				    //bineSure();
				///},
				//"取消":function(){
					//$('#binddiv').dialog('close');
				//}
			//},
			close:function(){
			},
			open:function(){
			}
		});
		$("#bind").click(
			function(){
				$("#binddiv").dialog("open");
			}
		);
		var lgcorpcode =$("#lgcorpcode").val();
		setting2.asyncUrl="<%=path%>/pgb_accessPri.htm?method=createUserTree&lgcorpcode="+lgcorpcode;
		reloadTree();
	}
	);
	
	function bineSure()
	{
		var spUserStr = "";
		var idorcodes="";
		//操作员 
		var checkNodes2 = zTree2.getCheckedNodes();
		if($("input[name='checkSpUser']:checked").length==0)
		{
			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_1")+"<%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_1",request)%>！");
			return;
		}
		
		$("input[name='checkSpUser']:checked").each(function(){
			spUserStr = spUserStr + $(this).val()+",";
		});

		if(checkNodes2.length==0)
		{
			//alert("请选择操作员！");
			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_64"));
			return;
		}
		for(var i=0;i<checkNodes2.length;i++){
    		idorcodes+=checkNodes2[i].id.substr(1)+",";
    	}
		$.post("<%=uri%>",{method:"doBind",spUserStr : spUserStr,idorcodes : idorcodes,lgcorpcode:<%=request.getParameter("lgcorpcode")%>},
			function(result){
				if(result=="true")
				{
					//alert("新增绑定关系成功！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_65"));
					//location.href = location.href;
					black();
				}else if(result == "error")
				{
					//alert("绑定关系失败！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_66"));
				}else if(result == "noCount")
				{
					//alert("所选择绑定关系皆已存在，无需重复添加绑定关系！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_67"));
				}
			}
		);
	}
	
	function del(id, keyId)
	{
		//if(confirm("是否确定撤销此绑定关系？"))
		if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_68")))
		{
			$.post("<%=uri%>",{method:"delete",id:id,lgcorpcode:<%=request.getParameter("lgcorpcode")%>,keyId:keyId},function(result){
				if(result == "true")
				{
					//alert("撤销绑定关系成功！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_69"));
					//location.href = location.href;
					black();
				}else// if(result == "false")
				{
					//alert("撤销绑定关系失败！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_70"));
				}
			});
		}
	}
	</script>
	</body>
</html>