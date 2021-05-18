<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.util.StringUtils"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.weix.common.util.GlobalMethods"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	//-> /p_weix
	String path = request.getContextPath();
	//-> http://localhost:8088/p_weix/
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	//-> /p_weix/weix/account
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	//-> /p_weix/weix
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	//-> /p_weix
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	
	
	String menuCode = titleMap.get("acctmanager");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String lguserid = (String)request.getParameter("lguserid");
 	String lgcorpcode = (String)request.getParameter("lgcorpcode");
 	
 	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
 	
    @ SuppressWarnings("unchecked")
    List<DynaBean> beans = (List<DynaBean>)request.getAttribute("beans");
    PageInfo pageInfo = new PageInfo();
    pageInfo=(PageInfo)request.getAttribute("pageInfo");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    String name = (String)request.getParameter("name");
    String ghname = (String)request.getParameter("ghname");
	String weixBasePath = request.getScheme() + "://" + request.getServerName() + path + "/";
	
	//使用集群，文件服务器的地址
	String filePath = GlobalMethods.getWeixFilePath();
	//MP企业微信服务协议
	String approve = (String)session.getAttribute("approve");
	//修改
	boolean upcode = false;
	if(btnMap.get(menuCode+"-3")!=null)
	{
		upcode = true;
	}
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=commonPath%>/common/css/frame.css" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/frame.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=commonPath%>/common/css/table.css" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/table.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css">
		<link rel="stylesheet" type="text/css"
			href="<%=skin%>/newjqueryui.css">
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/tips.css" />
		<style>
			#dropdownMenu span {
				float: none !important;
			}
			
			.img-circle {
				border-radius: 500px 500px 500px 500px;
			}
			
			.headimg5 {
				border: 1px solid #CCCCCC;
				box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
				height: 38px;
				width: 38px;
				vertical-align: middle;
				margin: 5px 0;
				_margin: 4px 0 5px;
			}
			
			.visible_hidden {
				visibility: hidden;
			}
			
			.blueColor {
				color: #0e5ad1;
			}
		</style>
	</head>
	<body>
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="" method="post" id="pageForm">
					<div style="display: none" id="hiddenValueDiv">
					</div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1")!=null) { %>
						<a id="add" href="javascript:showAddAccountTmp(0)">
						<emp:message key="common_btn_4" defVal="新建"
											fileName="common"></emp:message>
						</a>
						<%} %>
					</div>
					<div id="condition" style="display: block;">
						<table>
							<tbody>
								<tr>
									<td id="accounts">
										<emp:message key="weix_qywx_gzzhgl_text_1" defVal="公众帐号名称："
											fileName="weix"></emp:message>
									</td>
									<td>
										<input type="text" id="name" name="name"
											value="<%=name!=null?name:""  %>">
									</td>
									<td>
										<emp:message key="weix_qywx_gzzhgl_text_2" defVal="原始微信号："
											fileName="weix"></emp:message>
									</td>
									<td>
										<input type="text" id="ghname" name="ghname"
											value="<%=ghname!=null?ghname:""%>">
									</td>
									<td class="tdSer">
										<center>
											<a id="search" onclick="javascript:doSearch()"></a>
										</center>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="resultSet">
						<table id="content">
							<thead>
								<tr>
									<th>
										<emp:message key="weix_qywx_gzzhgl_text_3" defVal="头像"
											fileName="weix"></emp:message>
									</th>
									<th>
										<emp:message key="weix_qywx_gzzhgl_text_4" defVal="公众帐号名称"
											fileName="weix"></emp:message>
									</th>
									<th>
										<emp:message key="weix_qywx_gzzhgl_text_5" defVal="微信号"
											fileName="weix"></emp:message>
									</th>
									<th>
										<emp:message key="weix_qywx_gzzhgl_text_6" defVal="原始微信号"
											fileName="weix"></emp:message>
									</th>
									<th>
										<emp:message key="weix_qywx_gzzhgl_text_7" defVal="接入地址"
											fileName="weix"></emp:message>
									</th>
									<th>
										TOKEN
									</th>
									<th>
										<emp:message key="weix_qywx_gzzhgl_text_8" defVal="创建时间"
											fileName="weix"></emp:message>
									</th>
									<th colspan="4">
										<emp:message key="weix_qywx_gzzhgl_text_9" defVal="操作"
											fileName="weix"></emp:message>
									</th>
								</tr>
							</thead>
							<tbody>
								<%
                            if(beans != null && beans.size()>0){
                                for(DynaBean bean : beans){
                        %>
								<tr>
									<td valign="middle">
										<%if(bean.get("img")!=null&&!"".equals(bean.get("img"))) {%>
										<img class="img-circle headimg5" alt="img"
											src='<%= inheritPath + "/account/img/default_acct.png"%>'
											title="<emp:message key="weix_qywx_gzzhgl_text_3" defVal="头像"
											fileName="weix"></emp:message>"
											onerror="this.src='<%= inheritPath + "/account/img/default_acct.png"%>'">
										<% } else { %>
										<img class="img-circle headimg5" alt="img"
											src="<%= inheritPath + "/account/img/default_acct.png"%>"
											title="<emp:message key="weix_qywx_gzzhgl_text_3" defVal="头像"
											fileName="weix"></emp:message>">
										<% } %>
									</td>
									<td>
										<%=StringUtils.escapeString(bean.get("name").toString()) %>
									</td>
									<td>
										<%=bean.get("code") %>
									</td>
									<td>
										<%=bean.get("open_id") %>
									</td>
									<td class="getUrl">
										<div style="position: relative; width: 100%;">
											<div class="url_path zcopy"><%=bean.get("url") == null ? "" : weixBasePath + "api/" + bean.get("url") %></div>
											<div class="sd_popcard visible_hidden"
												style="left: 50%; margin-left: -30px;">
												<div class="pop_layer layer_bottomC">
													<div class="pop_warp angle_bottomC" style="padding: 0 15px">
														<span class="angle"></span>
														<span class="diamond"></span>
														<p>
															<emp:message key="common_btn_14" defVal="复 制"
											fileName="common"></emp:message>
														</p>
													</div>
												</div>
											</div>
										</div>
									</td>
									<td class="getToken">

										<div style="position: relative; width: 100%;">
											<div class="token zcopy"><%=bean.get("token")== null ? "" : bean.get("token") %></div>
											<div class="sd_popcard visible_hidden"
												style="left: 50%; margin-left: -30px;">
												<div class="pop_layer layer_bottomC">
													<div class="pop_warp angle_bottomC" style="padding: 0 15px">
														<span class="angle"></span>
														<span class="diamond"></span>
														<p>
															<emp:message key="common_btn_14" defVal="复 制"
											fileName="common"></emp:message>
														</p>
													</div>
												</div>
											</div>
										</div>
									</td>
									<td>
										<%=formatter.format(bean.get("createtime"))%>
									</td>
									<td>
										<%if(upcode){ %>
										<a href="javascript:showEditAcctTmp(<%=bean.get("a_id") %>)"><emp:message key="common_btn_15" defVal="修改"
											fileName="common"></emp:message></a>
										<%} else{
                            	   out.print("-");
                               }%>
									</td>
								</tr>
								<%
                            }
                        }else{
                        %>
								<tr>
									<td align="center" colspan="11">
										<emp:message key="weix_qywx_gzzhgl_text_10" defVal="无记录"
											fileName="weix"></emp:message>
									</td>
								</tr>
								<%} %>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="8">
										<div id="pageInfo"></div>
									</td>
								</tr>
							</tfoot>
						</table>
					</div>
				</form>
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
			<div id="pageStuff" style="display: none;">
				<input type="hidden" id="pathUrl" value="<%=path %>" />
				<input type="hidden" id="iPathUrl" value="<%=iPath%>" />
				<input type="hidden" id="currentTotalPage"
					value="<%=pageInfo.getTotalPage()%>" />
				<input type="hidden" id="currentPageIndex"
					value="<%=pageInfo.getPageIndex()%>" />
				<input type="hidden" id="currentPageSize"
					value="<%=pageInfo.getPageSize()%>" />
				<input type="hidden" id="currentTotalRec"
					value="<%=pageInfo.getTotalRec()%>" />
				<input type="hidden" id="approve" value="<%=approve%>" />
			</div>
		</div>
		<div id="editAcctTmpDiv" title="<emp:message key="weix_qywx_gzzhgl_text_23" defVal="编辑公众帐号"
											fileName="weix"></emp:message>"
			style="padding: 5px; display: none">
			<iframe id="editAcctTmpFrame" name="editAcctTmpFrame"
				style="display: none; width: 650px; height: 470px; border: 0;"
				marginwidth="0" scrolling="no" frameborder="no"></iframe>

		</div>
		<div id="addAcctTmpDiv" title="<emp:message key="weix_qywx_gzzhgl_text_24" defVal="添加公众帐号"
											fileName="weix"></emp:message>"
			style="padding: 5px; display: none">
			<iframe id="addAcctTmpFrame" name="addAcctTmpFrame"
				style="display: none; width: 650px; height: 430px; border: 0;"
				marginwidth="0" scrolling="no" frameborder="no"></iframe>
		</div>

		<div class="clear"></div>
		<script>
    var iPath="<%=iPath%>";
    </script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/js/myjquery-a.js"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/pageInfo.js"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
		<script type="text/javascript"
			src="<%=commonPath %>/common/js/common.js"></script>
		
		<%-- 国际化js加载 --%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/weix_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
			
		<script type="text/javascript" src="<%=iPath%>/js/accountList.js"></script>
				
		<script type="text/javascript"
			src="<%=iPath %>/js/jquery.zclip.min.js"></script>
			
	</body>
</html>