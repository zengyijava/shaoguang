<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@ page import="com.montnets.emp.entity.pasroute.LfSpDepBind" %>
<%@ page import="com.montnets.emp.entity.pasgrpbind.LfAccountBind" %>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mtService");
	
	@ SuppressWarnings("unchecked")
	List<LfSysuser> sysUserList = (List<LfSysuser>)request.getAttribute("sysuserList");
	LfSysuser curSysuser = (LfSysuser)request.getAttribute("curSysuser");
	
	@ SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");
	
	@ SuppressWarnings("unchecked")
    List<Userdata> spUserList = (List<Userdata>)request.getAttribute("sendUserList");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><emp:message key="znyq_ywgl_xhywgl_xzxxyw" defVal="新增下行业务" fileName="znyq"></emp:message></title>
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/downserAdd.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script>
		function show(){
			<% Object result=request.getAttribute("mtSerResult");
					if(result!=null && result.toString().equals("1")){%>
					//alert("新增下行业务项目成功！");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xzxxywxmcg"));
				<%}else if(result!=null && result.toString().equals("0")){%>
					//alert("操作失败！");	
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb"));	
				<%}request.removeAttribute("mtSerResult");%>
				<%if(result!=null){%>
					location.href="<%=path%>/eng_mtService.htm?method=find&lguserid="+$('#lguserid').val();
				<%}%>
			}
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			noquot("#serName");
			noquot("#comments");
			$('#u_o_c_explain').find('> p').next().hide();
			$('#u_o_c_explain').click(function(){$(this).find('> p').next().toggle();});
			show();
		});
		</script>
	</head>

	<body id="eng_addMtService" class="eng_addMtService">
			<div id="container">
				<div class="top">
					<div id="top_right">
						<div id="top_left"></div>
							<b><emp:message key="znyq_ywgl_xhywgl_dqwz_mh" defVal="当前位置:" fileName="znyq"></emp:message></b>[<%=titleMap.get(menuCode.substring(0,menuCode.indexOf("-"))) %>]-[<%=titleMap.get(menuCode) %><emp:message key="znyq_ywgl_xhywgl_xzxxyw2" defVal="]-[新建下行业务]" fileName="znyq"></emp:message>
					</div>
				</div>
				<div class="rContent">
				<center>
					 <div id="u_o_c_explain">
						<p>
							<emp:message key="znyq_ywgl_xhywgl_smxxyw" defVal="说明：下行业务" fileName="znyq"></emp:message>
						</p>
						<ul>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_ywmcygywmcdyygywlj" defVal="业务名称：一个业务名称对应一个业务逻辑；" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_ywmsygywdmsgywpzrysmyw" defVal="业务描述：一个业务的描述供业务配置人员说明业务；" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_yxztbsgywdqszsyxhsjy" defVal="运行状态：表示该业务当前设置是运行还是禁用" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_djqdanbcywjbxx" defVal="点击【确定】按钮保存业务基本信息" fileName="znyq"></emp:message>
							</li>
							<li>
								<emp:message key="znyq_ywgl_xhywgl_djfhanfqbcbfhxxywlb" defVal="点击【返回】按钮放弃保存并返回下行业务列表" fileName="znyq"></emp:message>
							</li>

						</ul>
					</div>
					<div id="detail_Info">
					<form action="<%=path %>/eng_mtService.htm?method=add" method="post" name="serForm" id="serForm">
					<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
					<table>
					<thead>
						<tr>
							<td class="ywName">
								<span><emp:message key="znyq_ywgl_xhywgl_ywmc_mh" defVal="业务名称：" fileName="znyq"></emp:message></span>
								<input type="hidden" name="hidOpType" value="add"/>
							</td>
							<td>
								<input type="text" name="serName"
									id="serName"  maxlength="20"/>
								<font class="tipColor">&nbsp;*</font>
							</td>
						</tr>
						<tr>
							<td >
								<span><emp:message key="znyq_ywgl_xhywgl_ywms_mh" defVal="业务描述：" fileName="znyq"></emp:message></span>
							</td>
							<td>
								<input type="text" name="comments"
									id="comments" />
							</td>
						</tr>
                           <tr>
							<td >
								<span><emp:message key="znyq_ywgl_xhywgl_yyz_mh" defVal="拥有者：" fileName="znyq"></emp:message></span>
							</td>
							<td>
							<select id="ownerList" name="ownerList" onchange="getUserDate()">
							<option value="<%=curSysuser.getUserId()%>"><%=curSysuser.getName()%>（<%=curSysuser.getUserName() %>）</option>
							<% if (sysUserList != null && sysUserList.size() > 0)
							   {
							       for (LfSysuser lfu : sysUserList)
							       {
							           if (!curSysuser.getUserName().equals(lfu.getUserName()))
							           {
							 %>
			            		 <option value="<%=lfu.getUserId() %>"><%=lfu.getName()%>（<%=lfu.getUserName() %>）</option>
			            	 <%
			            	         }
			            	     }
			            	 }
			            	  %>
			            		</select>
							</td>
						</tr>
						<tr>
							<td >
								<span><emp:message key="znyq_ywgl_xhywgl_ywlx_mh" defVal="业务类型：" fileName="znyq"></emp:message></span>
							</td>
							<td>
								<select id="busCode" name="busCode">
									<%
									if (busList != null && busList.size() > 0)
									{
										for(LfBusManager busManager : busList)
										{
									%>
									<option value="<%=busManager.getBusCode() %>"  >
										<%=busManager.getBusName().replace("<","&lt;").replace(">","&gt;") %>
									</option>
									<%}
									}													
									%>
			            		</select>
			            		<font class="tipColor">&nbsp;*</font>
							</td>
						</tr>
						<tr>
							<td >
								<span>
								<%--<%=StaticValue.SMSACCOUNT %>：--%>
								<emp:message key="znyq_ywgl_common_spzh_mh" defVal="SP账号：" fileName="znyq"></emp:message>
								</span>
							</td>
							<td>
								<select id="spUser" name="spUser">
									<%
									if (spUserList != null && spUserList.size() > 0) 
									{
										for (Userdata spUser : spUserList) 
										{
									%>
									<option value="<%=spUser.getUserId()%>"  >
									<%=spUser.getUserId()+"("+spUser.getStaffName()+")"%>
									</option>
									<%  }
									}
									%>
			            		</select>
			            		<font class="tipColor">&nbsp;*</font>
								<input type="hidden" name="staffName" id="staffName" value=""/>
							</td>
						</tr>
						<%--<tr>
							<td >
								<span>账户密码：</span>
							</td>
							<td>
								<input style="width: 200px" type="password" name="spPwd"
									id="password" />
								<font class="xingRed">*</font>
							</td>
						</tr> --%>
						<tr>
							<td >
								<span><emp:message key="znyq_ywgl_xhywgl_yxzt_mh" defVal="运行状态：" fileName="znyq"></emp:message></span>
							</td>
							<td>
								<input name="runState" type="radio" value="1"
									checked="checked" />
								<emp:message key="znyq_ywgl_xhywgl_yx" defVal="运行" fileName="znyq"></emp:message>
								<input name="runState" type="radio" value="0" />
								<emp:message key="znyq_ywgl_xhywgl_jy" defVal="禁用" fileName="znyq"></emp:message>
							</td>

						</tr>
						<tr>
							<td colspan="2" id="btn">
								<input type="hidden" name="serType" id="serType" value="2" />
								<input name="" type="button" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" id="formSubmit" class="btnClass1"/>
								<input  type="button" value="<emp:message key="znyq_ywgl_common_btn_10" defVal="返回" fileName="znyq"></emp:message>" onclick="javascript:location.href='eng_mtService.htm?method=find&lguserid='+$('#lguserid').val()" class="btnClass1"/>
							</td>
						</tr>
						</thead>
					</table>
					</form>
			</div></center>
		</div>
		<%-- 这是每个界面相应的DIV --%>
		<div class="bottom">
			<div id="bottom_right">
				<div id="bottom_left"></div>
			</div>
		</div>
	</div>
	</body>
</html>
