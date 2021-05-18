<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.corp.LfCorp"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageinfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	List<LfCorp> list=(List<LfCorp>)request.getAttribute("list");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("manager");
	menuCode = menuCode==null?"0-0-0":menuCode;
	LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
	conditionMap=(LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	String code=conditionMap.get("corpCode&like");
	String name=conditionMap.get("corpName&like");
	String addr=conditionMap.get("address&like");
	String corpState=conditionMap.get("corpState");
	LfSysuser lfSysuser = (LfSysuser)(request.getSession(false)!=null?request.getSession(false).getAttribute("loginSysuser"):null);
	String userName=lfSysuser!=null?lfSysuser.getUserName():"";
	
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
	<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/depsp.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/department.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cor_manager.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
        
	</head>

	<body id="cor_manager" onload="show()">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode) %>
			
			<%-- 内容开始 --%>
				<%//if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="cor_manager.htm?method=find" method="post" id="pageForm">
					<div class="buttons">
					<div id="toggleDiv">
						         </div>
				
    			<a id="add" href="javascript:toAdd()"><emp:message key="xtgl_qygl_xj" defVal="新建" fileName="xtgl"></emp:message></a>
    	     	</div>
    	     	<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
										<emp:message key="xtgl_qygl_qybm_mh" defVal="企业编码：" fileName="xtgl"></emp:message>
									</td>
									<td>
										<input type="text"  id="code" name="code" value="<%=null==code?"":code %>">
									</td>
									
									<td>
										<emp:message key="xtgl_qygl_qymc_mh" defVal="企业名称：" fileName="xtgl"></emp:message>
									</td>
									<td>
										<input type="text"  id="name" name="condcorpname" value="<%=null==name?"":name %>">
									</td>
									<td>
										<emp:message key="xtgl_qygl_qydz_mh" defVal="企业地址：" fileName="xtgl"></emp:message>
									</td>
									<td>
									   <input type="text"  id="addr" name="addr" value="<%=addr==null?"":addr%>">
									</td>
									
									<td>
										<emp:message key="xtgl_qygl_qyzt_mh" defVal="企业状态：" fileName="xtgl"></emp:message>
									</td>
									<td>
										<%--
										<input type="text"  id="corpState" name="corpState" value="<%=null==corpState?"":corpState %>">
										 --%>
										<select name="corpState" id="corpState" class="input_bd">
											<option value="-1" <%="-1".equals(corpState) ? "selected" : ""%>><emp:message key="xtgl_qygl_qb" defVal="全部" fileName="xtgl"></emp:message></option>
											<option value="1" <%="1".equals(corpState) ? "selected" : ""%>><emp:message key="xtgl_qygl_qy" defVal="启用" fileName="xtgl"></emp:message></option>
											<option value="0" <%="0".equals(corpState) ? "selected" : ""%>><emp:message key="xtgl_qygl_jy" defVal="禁用" fileName="xtgl"></emp:message></option>
										</select>
									</td>
									
									<td class="tdSer">
											<center><a id="search"></a></center>
										</td>
								</tr>
								
							</tbody>
						</table>
					</div>
				<table id="content">
				<thead>
				    <tr>
						<th><emp:message key="xtgl_qygl_qybm" defVal="企业编码" fileName="xtgl"></emp:message></th>
						<th><emp:message key="xtgl_qygl_qymc" defVal="企业名称" fileName="xtgl"></emp:message></th>
						<th><emp:message key="xtgl_qygl_zt" defVal="状态" fileName="xtgl"></emp:message></th>
						<th><emp:message key="xtgl_qygl_qydz" defVal="企业地址" fileName="xtgl"></emp:message></th>
						<th><emp:message key="xtgl_qygl_qylxdh" defVal="企业联系电话" fileName="xtgl"></emp:message></th>
						<th><emp:message key="xtgl_qygl_lxr" defVal="联系人" fileName="xtgl"></emp:message></th>
						<th><emp:message key="xtgl_qygl_lxrdh" defVal="联系人电话" fileName="xtgl"></emp:message></th>
						<th><emp:message key="xtgl_qygl_lxremail" defVal="联系人E-mail" fileName="xtgl"></emp:message></th>
						<th><emp:message key="xtgl_qygl_sfqyjf" defVal="是否启用计费" fileName="xtgl"></emp:message></th>
						<th colspan="3"><emp:message key="xtgl_qygl_cz" defVal="操作" fileName="xtgl"></emp:message></th>
			        </tr>
				</thead>
				<tbody>
				<%
			if(list.size()>0){	 
				for (int i=0;i<list.size();i++){
				    LfCorp  corp= list.get(i); %>
				<tr>
				<td><%=corp.getCorpCode() %>
				</td>
				<td><%=corp.getCorpName().replace("<","&lt;").replace(">","&gt;")%>
				</td>
				<%--<td><%=corp.getCorpState()==1?"启用":(corp.getCorpState()==0?"禁用":"")%>
				--%><td><%=corp.getCorpState()==1?MessageUtils.extractMessage("xtgl","xtgl_qygl_qy",request):(corp.getCorpState()==0?MessageUtils.extractMessage("xtgl","xtgl_qygl_jy",request):"")%>
				</td>
				<td><%=corp.getAddress()==null?"": corp.getAddress().replace("<","&lt;").replace(">","&gt;") %>
				</td>
				<td><%=corp.getPhone()==null?"": corp.getPhone()%>
				</td>
				<td><%=corp.getLinkman()==null?"": corp.getLinkman().replace("<","&lt;").replace(">","&gt;") %>
				</td>
				<td><%=corp.getMobile()==null?"":corp.getMobile() %>
				</td>
				<td><%=corp.getEmails() ==null?"":corp.getEmails()%>
				</td>
				<%--<td><%=corp.getIsBalance()==1?"是":"否" %></td>
				--%><td><%=corp.getIsBalance()==1?MessageUtils.extractMessage("xtgl","xtgl_qygl_s",request):MessageUtils.extractMessage("xtgl","xtgl_qygl_f",request) %></td>
				
				<%
				if(corp.getCorpCode()!=null&&"100000".equals(corp.getCorpCode())){ 
				  if("sysadmin".equals(userName)){%>
				<td>
				    <a href="javascript:toEdit('<%=corp.getCorpCode()%>')"><emp:message key="xtgl_qygl_xg" defVal="修改" fileName="xtgl"></emp:message></a>
				</td>
				<td>-</td>
				<%}else{%>
                     <td>
                        <emp:message key="xtgl_qygl_xg" defVal="修改" fileName="xtgl"></emp:message>
                    </td>
				    <td>-</td>
				<% } %>
				
				 <td><a onclick="javascript:authConfig('<%=corp.getCorpCode()%>')"><emp:message key="xtgl_rms_config_text9" defVal="权限设置" fileName="xtgl"></emp:message></a></td>
                        
                    <%-- <%if(btnMap.get(menuCode + "-" + 5) == null) { %>
                    		<td><a onclick="javascript:authConfig('<%=corp.getCorpCode()%>')"><emp:message key="xtgl_rms_config_text9" defVal="权限设置" fileName="xtgl"></emp:message></a></td>
                           <% } else { %>
                        <td>-</td>
                    <%} %> --%>

				<% }else{ %>
                    <td>
                        <a href="javascript:toEdit('<%=corp.getCorpCode()%>')"><emp:message key="xtgl_qygl_xg" defVal="修改" fileName="xtgl"></emp:message></a>
                    </td>
				    <td><a onclick="javascript:doAdm('<%=corp.getUserName() %>','<%=corp.getCorpCode() %>')"><emp:message key="xtgl_qygl_gl" defVal="管理" fileName="xtgl"></emp:message></a></Td>
                    <%-- <td>-</td> --%>
				 	<td><a onclick="javascript:authConfig('<%=corp.getCorpCode()%>')"><emp:message key="xtgl_rms_config_text9" defVal="权限设置" fileName="xtgl"></emp:message></a></td>
                 
                 <% } %>

				</tr>
				<%} }else{%>
				    <tr><td colspan="12"><emp:message key="xtgl_qygl_wjl" defVal="无记录" fileName="xtgl"></emp:message></td></tr>				  
				<%} %>
				</tbody>
				<tfoot>
						<tr>
							<td colspan="12">
							   <div id="pageInfo"></div>
							</td>
						</tr>
				</tfoot>
				</table>
				</form>
				</div>
			<%--按添加按钮的HTML--%>
		
			</div>
		
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main"> 
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		
		<script>
            function authConfig(corpCode){
                var url = "<%=path%>/rms_authManage.htm?method=rmsAuthManage&corpCode="+corpCode+"&pageIndex="+<%=pageInfo.getPageIndex()%>;
                url = encodeURI(encodeURI(url));
                location.href = url;
            }

			$(document).ready(function() {
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
			});
				var inheritPath=$('#inheritPath').val();
				$("#btnOK").click(function(e){
					butg("#btnOK","#btnCancel");
					var corpCode = $('#corpCode').val();
					var corpName=$("#corpName").attr("value");
					if($.trim(corpCode)=="" || $.trim(corpCode)==null ){// 非空判断
						//alert("企业编码不能为空！");$("#corpCode").focus();
						alert(getJsLocaleMessage("xtgl","xtgl_qygl_qybmbnwk"));$("#corpCode").focus();
		                butk("#btnOK","#btnCancel");
						return;
					}
					if($.trim(corpName)=="" || $.trim(corpName)==null ){
						//alert("企业名称不能为空！");$("#corpName").focus();
						alert(getJsLocaleMessage("xtgl","xtgl_qygl_qymcbnwk"));$("#corpName").focus();
						butk("#btnOK","#btnCancel");
						return;
					}
					});
			function show(){
			     var addRs=<%=request.getAttribute("add")%>;
			     var updateRs=<%=request.getAttribute("update")%>;
			     var addAdmin=<%=request.getParameter("addAdmin")%>;
			     var updateAdmin=<%=request.getAttribute("updateAdmin")%>;
			     if(addRs==true){
			         //if(confirm("操作成功！是否立即添加管理员？"))
			         if(confirm(getJsLocaleMessage("xtgl","xtgl_qygl_czcgsfljtjgly")))
			         {
			           location.href='<%=path%>/cor_manager.htm?method=toAdmin&action=add&corpCode=<%=request.getAttribute("corpCode")%>';
			         }
			    }else if(addRs==false){
			       //alert("操作失败！");
			       alert(getJsLocaleMessage("xtgl","xtgl_qygl_czsb"));
			       location.href='<%=path%>/cor_manager.htm?method=find';
			    }
			    if(updateRs==true){
			          //alert("操作成功！");
			          alert(getJsLocaleMessage("xtgl","xtgl_qygl_czcg"));
			          location.href='<%=path%>/cor_manager.htm?method=find';
			    }else if(updateRs==false){
			          //alert("操作失败！");
			          alert(getJsLocaleMessage("xtgl","xtgl_qygl_czsb"));
			          location.href='<%=path%>/cor_manager.htm?method=find';
			    }
			     if(addAdmin==true){
			         //alert("操作成功！");
			         alert(getJsLocaleMessage("xtgl","xtgl_qygl_czcg"));
			         location.href='<%=path%>/cor_manager.htm?method=find';
			         
			    }else if(addAdmin==false){
			       //alert("操作失败！");
			       alert(getJsLocaleMessage("xtgl","xtgl_qygl_czsb"));
			       location.href='<%=path%>/cor_manager.htm?method=find';
			    }
			    if(updateAdmin==true){
			          //alert("操作成功！");
			          alert(getJsLocaleMessage("xtgl","xtgl_qygl_czcg"));
			          location.href='<%=path%>/cor_manager.htm?method=find';
			    }else if(updateAdmin==false){
			          //alert("操作失败！");
			          alert(getJsLocaleMessage("xtgl","xtgl_qygl_czsb"));
			          location.href='<%=path%>/cor_manager.htm?method=find';
			    }
			}

			function doAdm(user,code)
			{
				var url = "<%=path%>/cor_manager.htm?method=toAdmin&corpCode="+code+
				          "&condcode=<%=null==code?"":code %>&condname=<%=null==name?"":name %>&condaddr=<%=addr==null?"":addr%>";
                if(user == null || user == "null")
                {
                     //if(confirm("该企业还未添加管理员，是否现在添加？"))
                     if(confirm(getJsLocaleMessage("xtgl","xtgl_qygl_gqyhwtjgly")))
                     {
                    	 url = url + "&action=add";
                    	 //location.href="<%=path%>/cor_manager.htm?method=toAdmin&action=add&corpCode="+code;
                    	 url = encodeURI(encodeURI(url));
						location.href = url;
                     }
                }
                else
                {
                	url = url + "&action=update";
                     //location.href = "<%=path%>/cor_manager.htm?corpCode="+code+"&method=toAdmin&action=update";
                     url = encodeURI(encodeURI(url));
                	location.href = url;
                }
                
			}
			
			function toEdit(code)
			{
				var url = encodeURI(encodeURI("<%=path%>/cor_manager.htm?method=toEdit&code="+code+
								"&condcode=<%=null==code?"":code %>&condname=<%=null==name?"":name %>&condaddr=<%=addr==null?"":addr%>"));

				location.href=url;
			}
			
			function toAdd()
			{
				var url = encodeURI(encodeURI("<%=path%>/cor_manager.htm?method=add" +
								"&condcode=<%=null==code?"":code %>&condname=<%=null==name?"":name %>&condaddr=<%=addr==null?"":addr%>"));

				location.href=url;
			}
			
		</script>
	</body>
</html>