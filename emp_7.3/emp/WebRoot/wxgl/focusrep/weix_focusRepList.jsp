<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String shortInheritPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
	
    @SuppressWarnings("unchecked")
    List<LfWeiAccount> otWeiAccList = (List<LfWeiAccount>) request.getAttribute("otWeiAccList");
    @SuppressWarnings("unchecked")
    List<DynaBean> beans = (List<DynaBean>) request.getAttribute("replyBeans");

    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo) request.getAttribute("pageInfo");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String a_id = request.getParameter("a_id");
    if(a_id == null || "".equals(a_id))
    {
        a_id = "-1";
    }

    String title = request.getParameter("title");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    String sygzhgy = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_67", request);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css"
			href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css"
			href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
              type="text/css" />

		<style>
#dropdownMenu span {
	float: none !important;
}
	.c_selectBox, #condition .c_selectBox ul, #condition .c_selectBox ul li{
		width:208px!important;
	}
	
</style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
	
	<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<input type="hidden" id="pathUrl" value="<%=path%>" />
			<%-- 内容开始 --%>
		 <div id="rContent" class="rContent">	
			<form name="pageForm" action="weix_focusReply.htm" method="post"
				id="pageForm">
				<div style="display: none" id="hiddenValueDiv"></div>
				<div class="buttons">
					<div id="toggleDiv">
					</div>
					<a id="add"
						href="javascript:addFocusRep(<%=pageInfo.getTotalRec()%>,<%=otWeiAccList.size()%>)"><emp:message key="wxgl_button_9" defVal="新建" fileName="wxgl"/></a>
					<a id="delete" onclick="javascript:del('all')"><emp:message key="wxgl_button_10" defVal="删除" fileName="wxgl"/></a>
				</div>
				<div style="display: none" id="loginpa"></div>

				<div id="condition">
					<table>
						<tbody>
							<tr>
								<td>
									<emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/>
								</td>
								<td>
									<select id="a_id" name="a_id" class="input_bd"
										style="width: 225px">
										<option value="">
											<emp:message key="wxgl_gzhgl_title_63" defVal="全部" fileName="wxgl"/>
										</option>
										<%
									    if(otWeiAccList != null && otWeiAccList.size() > 0)
									    {
									        for (LfWeiAccount acct : otWeiAccList)
									        {
									            String aId = String.valueOf(acct.getAId());
									%>
										<option value="<%=acct.getAId()%>"
											<%=(a_id.equals(aId)) ? "selected" : ""%>>
											<%=acct.getName()%></option>
										<%
									    }
									    }
									%>
									</select>
								</td>
								<td>
									<emp:message key="wxgl_gzhgl_title_56" defVal="回复标题：" fileName="wxgl"/>
								</td>
								<td>
									<input id="title" name="title"
										value="<%=title != null ? title : ""%>">
								</td>

								<td class="tdSer">
									<a id="search"></a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				
				
				
					<div class="bd">
						<table id="content">
							<thead>
								<tr>
									<th>
										<input type="checkbox" name="checkall" id="selectAll"
											onclick="checkAlls(this,'checklist')" />
									</th>
									<th>
										<emp:message key="wxgl_gzhgl_title_64" defVal="回复标题" fileName="wxgl"/>
									</th>
									<th>
										<emp:message key="wxgl_gzhgl_title_65" defVal="回复内容" fileName="wxgl"/>
									</th>
									<th>
										<emp:message key="wxgl_gzhgl_title_66" defVal="公众帐号" fileName="wxgl"/>
									</th>
									<th>
										<emp:message key="wxgl_gzhgl_title_6" defVal="创建时间" fileName="wxgl"/>
									</th>
									<th colspan="4">
										<emp:message key="wxgl_gzhgl_title_7" defVal="操作" fileName="wxgl"/>
									</th>
								</tr>
							</thead>
							<tbody>
								<%
                            if(beans != null && beans.size() > 0)
                            {
                                for (DynaBean bean : beans)
                                {
                        %>
								<tr>
									<td>
										<input type="checkbox" name="checklist"
											value="<%=bean.get("evt_id")%>" />
									</td>
									<td>
										<%
                           		    String titleStr = (String) bean.get("title");
                           		            if(titleStr == null || "".equals(titleStr))
                           		            {
                           		                out.print("-");
                           		            }
                           		            else
                           		            {
                           		%>
										<xmp><%=titleStr.length() > 8 ? titleStr.substring(0, 8) + "..." : titleStr%></xmp>
										<div class="titleTip" style="display: none"><%=titleStr%></div>
										<%
 								    }
 								%>
									</td>
									<td>
										<%
                              	    String msg_text = (String) bean.get("msg_text");
                              	            if(msg_text == null || "".equals(msg_text))
                              	            {
                              	                out.print("-");
                              	            }
                              	            else
                              	            {
                              	%>
										<xmp><%=msg_text.length() > 8 ? msg_text.substring(0, 8) + "..." : msg_text%></xmp>
										<div class="titleTip" style="display: none">
											<xmp><%=msg_text%></xmp>
										</div>
										<%
 								    }
 								%>
									</td>
									<td>
										<%
                           		    String accountname = (String) bean.get("accountname");
                           		            if(accountname == null || "".equals(accountname))
                           		            {
                           		                out.print(sygzhgy);
                           		            }
                           		            else
                           		            {
                           		%>
										<xmp><%=accountname.length() > 8 ? accountname.substring(0, 8) + "..." : accountname%></xmp>
										<div class="titleTip" style="display: none"><%=accountname%></div>
										<%
 								    }
 								%>
									</td>
									<td>
										<%=bean.get("createtime") != null ? sdf.format(bean.get("createtime")) : ""%>
									</td>
									<td>
										<a onclick='preview(<%=bean.get("evt_id")%>)'><emp:message key="wxgl_button_11" defVal="预览" fileName="wxgl"/></a>
										&nbsp;&nbsp;
										<a
											href="javascript:toUpdate(<%=bean.get("a_id")%>,<%=bean.get("evt_id")%>)"><emp:message key="wxgl_button_12" defVal="编辑" fileName="wxgl"/></a>
										&nbsp;&nbsp;
										<a onclick="javascript:del(<%=bean.get("evt_id")%>)"><emp:message key="wxgl_button_10" defVal="删除" fileName="wxgl"/></a>
									</td>
								</tr>
								<%
                            }
                            }
                            else
                            {
                        %>
								<tr>
									<td align="center" colspan="9">
										<emp:message key="wxgl_gzhgl_title_11" defVal="无记录" fileName="wxgl"/>
									</td>
								</tr>
								<%
                            }
                        %>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="9">
										<div id="pageInfo"></div>
									</td>
								</tr>
							</tfoot>
						</table>
					</div>
			</form>
				</div>
		</div>
		<%-- 内容结束 --%>

		<div id="pageStuff" style="display: none;">
			<input type="hidden" id="pathUrl" value="<%=path%>" />
			<input type="hidden" id="iPathUrl" value="<%=iPath%>" />
			<input type="hidden" id="currentTotalPage"
				value="<%=pageInfo.getTotalPage()%>" />
			<input type="hidden" id="currentPageIndex"
				value="<%=pageInfo.getPageIndex()%>" />
			<input type="hidden" id="currentPageSize"
				value="<%=pageInfo.getPageSize()%>" />
			<input type="hidden" id="currentTotalRec"
				value="<%=pageInfo.getTotalRec()%>" />
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
		<script src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath%>/wxcommon/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=path %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript"
			src="<%=path %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/wxcommon/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/focusRepList.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
			$('#a_id').isSearchSelect({'width':'165','isInput':false,'zindex':0});
			function doGo(url)
			{
				location.href = url;
			}
			$("#content td").hover(function(){
				if($(this).find(".titleTip").size()>0){
					$(this).attr("title",$.trim($(this).find(".titleTip").text()));
				}
				},function(){
					$(this).removeAttr("title");
				}
			);
		</script>
	</body>
</html>