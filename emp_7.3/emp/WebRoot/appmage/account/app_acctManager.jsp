<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%
	String path=request.getContextPath();
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	//按钮权限
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	List<DynaBean> trustDatas = (List<DynaBean>)request.getAttribute("beans");
	int count=0;
	Object countObject=request.getAttribute("count");
	if(countObject!=null){
		count=((Integer)countObject).intValue();
	}
	LinkedHashMap<String,String> conditionMap = (LinkedHashMap)request.getAttribute("conditionMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	String result = (String)session.getAttribute("trustdataResult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
<title><%=titleMap.get(menuCode)%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %> " type="text/css" >
		<style>
			.avatar{
				width:40px;
				vertical-align: middle;
				padding: 3px 0;
			}
		</style>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
</head>
<body>
<div id="container" class="container">
	<%--<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
	<%--<%-- header结束 --%>
		<div id="rContent" class="rContent">

		<form name="pageForm" action="<%=path%>/app_acctmanager.htm" method="post" id="pageForm">
		<div id="loginUser" class="hidden"></div>
			<input type="hidden" id="pathUrl" value="<%=path%>" />
				<div class="buttons">
				<div id="toggleDiv"> </div>
			<div>
		<%-- 添加一条记录之后，不能再添加记录了  --%>
		<% if(count>0){ %>
		<input type="button" class="btnClass4 btnHover" value="<emp:message key="appmage_jcsj_yhgl_text_addappaccount" defVal="添加APP账号" fileName="appmage"></emp:message>" style="margin-bottom:8px;" disabled>
		<%}else { %>
		<input type="button" class="btnClass4 btnHover" value="<emp:message key="appmage_jcsj_yhgl_text_addappaccount" defVal="添加APP账号" fileName="appmage"></emp:message>" style="margin-bottom:8px;" onclick="showAddAccountTmp()">
		<%}%>
		</div>
			</div>
			
	<div id="condition">
		<table>
			<tr>
				<td>
					<span><emp:message key="appmage_jcsj_yhgl_text_busname" defVal="企业名称" fileName="appmage"></emp:message>：</span>
				</td>
				<td>
					<input type="text" name="name" id="name" style="width:180px" maxlength="20" value="<%=conditionMap.get("name") == null?"":conditionMap.get("name") %>" />
				</td>
				<td>
					<span><emp:message key="appmage_jcsj_yhgl_text_buscode" defVal="企业编码" fileName="appmage"></emp:message>：</span>
				</td>
				<td>
					<input type="text" name="code"  id="code"maxlength="20"  style="width:180px" value="<%=conditionMap.get("code") == null?"":conditionMap.get("code") %>" />
				</td>
				 <td><emp:message key="appmage_jcsj_yhgl_text_appaccount" defVal="APP帐号" fileName="appmage"></emp:message>：</td>
				 <td><input type="text" name="account" id="account" maxlength="20" style="width:180px" value="<%=conditionMap.get("account") == null?"":conditionMap.get("account") %>" />
				 </td>	

				<td class="tdSer">
					<center><a id="search"></a></center>
					</td>	 
					   
			</tr>
			<tr>
				<td><emp:message key="appmage_xxfb_appsyfb_text_createpeploe" defVal="创建人" fileName="appmage"></emp:message>：</td>
				<td>
				<input type="text" id="username" name="username" style="width: 180px;" maxlength="20" value="<%=conditionMap.get("username") == null?"":conditionMap.get("username") %>">
				</td>
				<td></td>
				<td></td>
				 <td></td>
				 <td></td>	
				 <td></td>	    
			</tr>
		</table>

	</div>
			<table id="content">
				<thead>
				  <tr>
				  		<th><emp:message key="appmage_jcsj_yhgl_text_headimage" defVal="头像" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_busname" defVal="企业名称" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_busnum" defVal="企业编号" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_appaccount" defVal="APP帐号" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_ipandport" defVal="通讯IP及端口" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_xxfb_appsyfb_text_createpeploe" defVal="创建人" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_xxfb_dxqf_text_createtime" defVal="创建时间" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_remark" defVal="备注" fileName="appmage"></emp:message></th>
						<th>
							<emp:message key="appmage_common_opt_caozuo" defVal="操作" fileName="appmage"></emp:message>
						</th>

				  </tr>
				</thead>
					<tbody>
					<%
					DynaBean data = null;
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(trustDatas != null && trustDatas.size()>0){
						for(int v=0;v<trustDatas.size();v++){
							 data=trustDatas.get(v);
									%>
								<tr>
									<td>	
			                            <%
	                                if(data.get("img") != null && !"".equals(data.get("img")))
	                                        {
	                            %>
	                                <img src='<%= inheritPath + "/"+data.get("img")%>'  title="<emp:message key="appmage_jcsj_yhgl_text_headimage" defVal="头像" fileName="appmage"></emp:message>" class="avatar" onerror="this.src='<%= inheritPath + "/"+data.get("img")%>'"/>
	                            <%
	                                }
	                                else
	                                {
	                            %>
	                                <img src='<%=inheritPath + "/appmage/account/img/default_acct.png" %>' title="<emp:message key="appmage_jcsj_yhgl_text_headimage" defVal="头像" fileName="appmage"></emp:message>" class="avatar">
	                            <%
	                                }
	                            %>
									
									</td>
									<td><%=data.get("name")==null?"":data.get("name") %></td>
									
									<td><%=data.get("fake_id")==null?"":data.get("fake_id") %></td>
									<td><%=data.get("code")==null?"":data.get("code") %></td>
									<td>
									
									<%
									String url=data.get("url")==null?"":data.get("url")+"";
									String port=data.get("port")==null?"":data.get("port")+"";
										out.print(url+":"+port);
									%>
								
									</td>
									<td>
									<%
									String username=data.get("username")==null?"":data.get("username")+"";
									String lfname=data.get("lfname")==null?"":data.get("lfname")+"";
										out.print(username+"("+lfname+")");
									%>
									</td>
									<td>
									<%if(data.get("createtime")==null||"".equals(data.get("createtime"))){ %>
									- <%	}else{ %>
									 <%=df.format(data.get("createtime"))%><%
									} %></td>
									<td>
									<a onclick=javascript:modify(this)>
									<%
									String rmessage=String.valueOf(data.get("info")==null?"":data.get("info"));
									 %>
								  <label style="display:none"><xmp><%=rmessage %></xmp></label>
											<xmp><%=rmessage.length()>5?rmessage.substring(0,5)+"...":rmessage %></xmp>
								  </a> 
									
									</td>
									<td nowrap="nowrap">
									<a href="javascript:showEditAcctTmp(<%=data.get("a_id")%>)" class="mr10"><emp:message key="appmage_jcsj_yhgl_text_modify" defVal="修改" fileName="appmage"></emp:message></a>
									</td>
									
								</tr>
						<%} 
						}else {%>
						
						<tr><td colspan="9"><emp:message key="appmage_common_text_nodata" defVal="无记录" fileName="appmage"></emp:message></td></tr>
					<%} %>
				</tbody>
				<tfoot>
						<tr>
						<td colspan="9">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
		</table>
		</form>
		</div>
					<%-- foot结束 --%>
			<div id="modify" title="<emp:message key="appmage_jcsj_yhgl_text_remarkcontent" defVal="备注内容" fileName="appmage"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
				<table width="100%">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td>
								<label id="msg" style="width:100%;height:100%"></label>
								 
							</td>
							 
						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
			<%-- 内容结束 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		<div class="clear"></div>
		</div>
<%-- 加载JS 文件 --%>
<script type="text/javascript" src="<%=inheritPath%>/common/js/myjquery-a.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/myjquery-j.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/pageInfo.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=path%>/appmage/account/js/accountList.js"></script>	
<script type="text/javascript">

	$(document).ready(function() {
		getLoginInfo("#loginUser");
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		
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
		
		$('#search').click(function(){submitForm();});
		

	});
	var lguserid=<%=request.getParameter("lguserid")%>
	var lgcorpcode=<%=request.getParameter("lgcorpcode")%>
</script>

</body>
</html>