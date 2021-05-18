<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.qyll.vo.LlOperatorReportVo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo = null;
	if(request.getAttribute("pageInfo")!=null){
		pageInfo = (PageInfo) request.getAttribute("pageInfo");
	}else{
		pageInfo=new PageInfo();
	}
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("packageOrderQuest");
	menuCode = menuCode==null?"0-0-0":menuCode;
	LlOperatorReportVo llOperatorReport=(LlOperatorReportVo)request.getAttribute("llOperatorReport");
	LlOperatorReportVo reportSum=(LlOperatorReportVo)request.getAttribute("llOperatorReportSum");
	List<LlOperatorReportVo> resultList = (List<LlOperatorReportVo>) request.getAttribute("resultList");
	String findResult = (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute("emp_lang");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
<title>ll_orderReport.htm</title>
<%@include file="/common/common.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/table.css" rel="stylesheet" type="text/css" />
<link href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css">
<%
	if (StaticValue.ZH_HK.equals(langName)) {
%>
<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>" />
<link rel="stylesheet" type="text/css" href="<%=skin%>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>" />
<%
	}
%>
<style type="text/css">
body div#tooltip {
	position: absolute;
	z-index: 1000;
	max-width: 435px;
	_width: expression(this.scrollWidth > 435 ? "435px" : "auto");
	width: auto;
	background: #A8CFF6;
	border: #FEFFD4 solid 1px;
	text-align: left;
	padding: 6px;
}

body div#tooltip p {
	margin: 0;
	padding: 6;
	color: #FFFFFE;
	font: 12px verdana, arial, sans-serif;
}

body div#tooltip p em {
	display: block;
	margin-top: 3px;
	color: #f60;
	font-style: normal;
	font-weight: bold;
}

#content tr.peachpuff,#content tr.peachpuff td {
	background: #FFDAB9;
}

#msgcont {
	white-space: pre-wrap;
	*white-space: pre;
	*word-wrap: break-word;
	word-break: break-all;
}

#batchDetail_box thead th {
	background-color: #ccdef8;
	border: #bdc7d7 1px solid;
}

#batchDetail_box thead th.th_l_b {
	border-left: #bdc7d7 1px solid;
}

#batchDetail_box thead th {
	font-size: 12px;
	text-align: center;
	height: 26px;
	font-weight: normal;
	white-space: nowrap;
}

#batchDetail_box tbody td {
	font-size: 12px;
	text-align: center;
	height: 24px;
	line-height: 24px;
	padding: 0 4px;
	word-break: break-all;
	border: #b3caee 1px solid;
}
</style>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/qyll_<%=langName%>.js"></script>
</head>
<body>
	<div id="batchDetail_box" title="<emp:message key="qyll_common_107" defVal="流量套餐详情" fileName="qyll" />" style="display:none;width: 100%;">
		<div id="maindiv" style="overflow: auto;height: auto;">
			<center>
				<table id="batchDetailTb" style="width:350px;margin-top: 2px;">
					<thead>
						<tr>
							<th><emp:message key="qyll_common_77" defVal="套餐名称" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_43" defVal="运营商" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_108" defVal="号码数" fileName="qyll" /></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</center>
		</div>
	</div>


	<div id="container" class="container">
		<%-- header开始 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode)%>
		<%-- header结束 --%>
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
			<form name="pageForm" action="ll_packageOrderQuest.htm?method=find" method="post" id="pageForm">
				<div class="buttons">
					<a id="exportCondition"><emp:message key="qyll_common_92" defVal="导出" fileName="qyll" /></a>
					<div id="toggleDiv"></div>
				</div>
				<div id="condition">
					<table>
						<tbody>
							<tr>
								<td><emp:message key="qyll_common_99" defVal="订购编号" fileName="qyll" />：</td>
								<td id="myType"><input type="text" id="orderNo" name="orderNo" style="width: 180px" value="${param.orderNo}"></td>
								<td><emp:message key="qyll_common_77" defVal="套餐名称" fileName="qyll" />：</td>
								<td id="myType"><select id="theme" name="theme" style="width: 184px;">
										<c:choose>
											<c:when test="${fn:length(productList) != 0}">
												<option value=""><emp:message key="qyll_common_101" defVal="请选择" fileName="qyll" /></option>
												<c:forEach items="${productList}" var="product">
													<option value="${product.productId}" <c:if test="${param.theme eq product.productId}">selected="selected"</c:if>>${product.productName}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value=""><emp:message key="qyll_common_204" defVal="暂无套餐可选择" fileName="qyll" /></option>
											</c:otherwise>
										</c:choose>
								</select></td>
								<td><emp:message key="qyll_common_43" defVal="运营商" fileName="qyll" />：</td>
								<td id="myType"><select id="isp" name="isp" style="width: 184px;">
										<option value=""><emp:message key="qyll_common_78" defVal="全部" fileName="qyll" /></option>
										<option value="1" <c:if test="${param.isp=='1'}">selected="selected"</c:if>><emp:message key="qyll_common_185" defVal="移动"
												fileName="qyll" /></option>
										<option value="2" <c:if test="${param.isp=='2'}">selected="selected"</c:if>><emp:message key="qyll_common_187" defVal="电信"
												fileName="qyll" /></option>
										<option value="3" <c:if test="${param.isp=='3'}">selected="selected"</c:if>><emp:message key="qyll_common_186" defVal="联通"
												fileName="qyll" /></option>
								</select></td>
								<td class="tdSer">
									<center>
										<a id="search"></a>
									</center>
								</td>
							</tr>

							<tr>
								<td><emp:message key="qyll_common_94" defVal="订购状态" fileName="qyll" />：</td>
								<td id="myType"><select id="state" name="state" style="width: 184px;">
										<option value=""><emp:message key="qyll_common_78" defVal="全部" fileName="qyll" /></option>
										<option value="-1" <c:if test="${param.state=='-1'}">selected="selected"</c:if>><emp:message key="qyll_common_207" defVal="提交中"
												fileName="qyll" /></option>
										<option value="1" <c:if test="${param.state=='1'}">selected="selected"</c:if>><emp:message key="qyll_common_119" defVal="待审核"
												fileName="qyll" /></option>
										<option value="2" <c:if test="${param.state=='2'}">selected="selected"</c:if>><emp:message key="qyll_common_121" defVal="定时中"
												fileName="qyll" /></option>
										<option value="3" <c:if test="${param.state=='3'}">selected="selected"</c:if>><emp:message key="qyll_common_110" defVal="已撤销"
												fileName="qyll" /></option>
										<option value="4" <c:if test="${param.state=='4'}">selected="selected"</c:if>><emp:message key="qyll_common_202" defVal="已冻结"
												fileName="qyll" /></option>
										<option value="5" <c:if test="${param.state=='5'}">selected="selected"</c:if>><emp:message key="qyll_common_203" defVal="超时未发送"
												fileName="qyll" /></option>
										<option value="6" <c:if test="${param.state=='6'}">selected="selected"</c:if>><emp:message key="qyll_common_215" defVal="审核未通过"
												fileName="qyll" /></option>
								</select></td>
								<td><emp:message key="qyll_common_100" defVal="隶属机构" fileName="qyll" />：</td>
								<td class="condi_f_l">
									<div style="width:220px;">
										<input type="text" id="depNam" name="organization"
											value="<c:if test="${empty param.organization}"> <emp:message key="qyll_common_101" defVal="请选择" fileName="qyll" /></c:if><c:if test="${!empty param.organization}">${param.organization}</c:if>"
											readonly style="width:160px;cursor: pointer;" class="treeInput" onclick="javascript:showMenu();" />
									</div>
									<div id="dropMenu">
										<div style="margin-top: 3px;margin-right:10px;text-align:right">
											<input type="button" value="<emp:message key="qyll_common_28" defVal="确定" fileName="qyll" />" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" style="width:50px;" />&nbsp;&nbsp; <input
												type="button" value="<emp:message key="qyll_common_102" defVal="清空" fileName="qyll" />" class="btnClass1" onclick="javascript:cleanSelect_dep();" style="width:50px;" />
										</div>
										<ul id="dropdownMenu" class="tree"></ul>
									</div>
								</td>
								<td><emp:message key="qyll_common_103" defVal="操作员" fileName="qyll" />：</td>
								<td class="condi_f_l">
									<div style="width:220px;">
										<input type="text" id="userName" name="operator"
										value="<c:if test="${empty param.operator}"> <emp:message key="qyll_common_101" defVal="请选择" fileName="qyll" /></c:if><c:if test="${!empty param.operator}">${param.operator}</c:if>"
										readonly style="width:160px;cursor: pointer;" class="treeInput" onclick="javascript:showMenu2();" />
									</div>
									<div id="dropMenu2">
										<div style="margin-top: 3px;margin-right:10px;text-align:right">
											<input type="button" value="<emp:message key="qyll_common_28" defVal="确定" fileName="qyll" />" class="btnClass1" onclick="javascript:zTreeOnClickOK2();" style="width:50px;" />&nbsp;&nbsp; <input
												type="button" value="<emp:message key="qyll_common_102" defVal="清空" fileName="qyll" />" class="btnClass1" onclick="javascript:cleanSelect_user();" style="width:50px;" />
										</div>
										<div style="margin-top:3px;padding-left:4px;">
											<font class="zhu"><emp:message key="qyll_common_104" defVal="注：请勾选操作员进行查询" fileName="qyll" /></font>
										</div>
										<ul id="dropdownMenu2" class="tree"></ul>
									</div>
								</td>
							</tr>

							<tr>
								<td><emp:message key="qyll_common_6" defVal="订购主题" fileName="qyll" />：</td>
								<td id="myType"><input type="text" id="topic" name="topic" style="width: 180px" value="${param.topic}"></td>
								<td><emp:message key="qyll_common_19" defVal="订购时间" fileName="qyll" />：</td>
								<td><input type="text" style="cursor: pointer; width: 180px; background-color: white;" class="Wdate"
									onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="startTime" name="startTime" value="${param.startTime}" /></td>
								<td><emp:message key="qyll_common_105" defVal="至" fileName="qyll" />：</td>
								<td><input type="text" style="cursor: pointer; width: 180px; background-color: white;" class="Wdate"
									onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="endTime" name="endTime" value="${param.endTime}" /></td>
							</tr>
						</tbody>
					</table>
				</div>
				<table id="content">
					<thead>
						<tr>
							<th><emp:message key="qyll_common_103" defVal="操作员" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_100" defVal="隶属机构" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_6" defVal="订购主题" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_99" defVal="订购编号" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_77" defVal="套餐名称" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_94" defVal="订购状态" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_111" defVal="短信提醒状态" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_112" defVal="短信内容" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_130" defVal="创建时间" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_19" defVal="订购时间" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_108" defVal="号码数" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_7" defVal="订购号码" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_116" defVal="操作" fileName="qyll" /></th>

						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(resultList) != 0}">
								<c:forEach items="${resultList}" var="orderTask">
									<tr>
										<td><c:if test="${fn:length(orderTask.operator) != 0}">${orderTask.operator}</c:if> <c:if test="${fn:length(orderTask.operator) == 0}">-</c:if></td>
										<td><c:if test="${fn:length(orderTask.organization) != 0}">${orderTask.organization}</c:if> <c:if test="${fn:length(orderTask.organization) == 0}">-</c:if></td>
										<td title="${fn:escapeXml(orderTask.topic)}"><c:if test="${fn:length(orderTask.topic)>=12 }">${fn:escapeXml(fn:substring(orderTask.topic ,0,12))}...</c:if>
											<c:if test="${fn:length(orderTask.topic)<12 && fn:length(orderTask.topic)>0}">${fn:escapeXml(orderTask.topic)}</c:if> <c:if
												test="${orderTask.topic eq ' '}">-</c:if></td>
										<td>${orderTask.orderNo}</td>
										<td><a href="javascript:void(0); onclick=showBatchDetail('${orderTask.orderNo}');"><emp:message key="qyll_common_200" defVal="详情" fileName="qyll" /></a></td>
										<td><c:if test="${orderTask.orderState == '-1'}">
												<emp:message key="qyll_common_207" defVal="提交中" fileName="qyll" />
											</c:if> <c:if test="${orderTask.orderState == '1'}">
												<emp:message key="qyll_common_119" defVal="待审核" fileName="qyll" />
											</c:if> <c:if test="${orderTask.orderState == '2'}">
												<emp:message key="qyll_common_121" defVal="定时中" fileName="qyll" />
											</c:if> <c:if test="${orderTask.orderState == '3'}">
												<emp:message key="qyll_common_110" defVal="已撤销" fileName="qyll" />
											</c:if> <c:if test="${orderTask.orderState == '4'}">
												<emp:message key="qyll_common_202" defVal="已冻结" fileName="qyll" />
											</c:if> <c:if test="${orderTask.orderState == '5'}">
												<emp:message key="qyll_common_203" defVal="超时未发送" fileName="qyll" />
											</c:if> <c:if test="${orderTask.orderState == '6'}">
												<emp:message key="qyll_common_215" defVal="审核未通过" fileName="qyll" />
											</c:if></td>
										<td><c:if test="${orderTask.smsState == -1}">
												<emp:message key="qyll_common_118" defVal="未设定" fileName="qyll" />
											</c:if> <c:if test="${orderTask.smsState == 0}">
												<emp:message key="qyll_common_120" defVal="已发送" fileName="qyll" />
											</c:if> <c:if test="${orderTask.smsState == 1}">
												<emp:message key="qyll_common_119" defVal="待审核" fileName="qyll" />
											</c:if> <c:if test="${orderTask.smsState == 2}">
												<emp:message key="qyll_common_121" defVal="定时中" fileName="qyll" />
											</c:if> <c:if test="${orderTask.smsState == 3}">
												<emp:message key="qyll_common_110" defVal="已撤销" fileName="qyll" />
											</c:if> <c:if test="${orderTask.smsState == '4'}">
												<emp:message key="qyll_common_202" defVal="已冻结" fileName="qyll" />
											</c:if> <c:if test="${orderTask.smsState == '5'}">
												<emp:message key="qyll_common_203" defVal="超时未发送" fileName="qyll" />
											</c:if><c:if test="${orderTask.smsState == '6'}">
												<emp:message key="qyll_common_215" defVal="审核未通过" fileName="qyll" />
											</c:if></td>
										<td title="${fn:escapeXml(orderTask.msg)}"><c:if test="${fn:length(orderTask.msg)>=12 }">${fn:escapeXml(fn:substring( orderTask.msg ,0,12))}...</c:if>
											<c:if test="${fn:length(orderTask.msg)<12 && fn:length(orderTask.msg)>0}">${fn:escapeXml(orderTask.msg)}</c:if> <c:if
												test="${orderTask.msg eq ' '}">-</c:if></td>
										<td><fmt:formatDate value="${orderTask.createtm}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<td><fmt:formatDate value="${orderTask.ordertm}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
										<td>${orderTask.subCount}</td>
										<td><a href="javascript:void(0); onclick=downloadNumber('${orderTask.orderNo}');"><emp:message key="qyll_common_131" defVal="下载"
													fileName="qyll" /></a></td>
										<td><c:if test="${orderTask.orderState == '2' or orderTask.orderState == '1'}">
												<a onclick="cancel('${orderTask.id}')"><emp:message key="qyll_common_122" defVal="撤销" fileName="qyll" /></a>
											</c:if></td>
									</tr>
								</c:forEach>
							</c:when>
							<c:when test="${isFirstEnter != null}">
								<tr>
									<td align="center" colspan="13"><font color="blue"><emp:message key="qyll_common_81" defVal="请点击查询获取数据" fileName="qyll" /></font></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="13"><emp:message key="qyll_common_82" defVal="无记录" fileName="qyll" /></td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="13">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
				<%-- foot开始 --%>
				<div class="bottom">
					<div id="bottom_right">
						<div id="bottom_left"></div>
						<div id="bottom_main"></div>
					</div>
				</div>
				<div id="orderCode" class="hidden"></div>
			</form>
		</div>

	</div>
	<div class="clear"></div>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js"></script>
	<script type="text/javascript" src="<%=iPath %>/js/reportTime.js"></script>
	<script type="text/javascript">
			$(function(){
				closeTreeFun(["dropMenu"],[""]);
				closeTreeFun(["dropMenu2"],[""]);
			    setting2.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
			    setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
				reloadTree();
				var findresult="<%=findResult%>";
				if(findresult=="-1")
			    {
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_76"));
			       return;			       
			    }
				
				$("#toggleDiv").toggle(function() {
					$("#condition").hide();
					$(this).addClass("collapse");
				}, function() {
					$("#condition").show();
					$(this).removeClass("collapse");
				});
				
			});
			var zTree3;
			var zTree2;					
			var setting3;
			var setting2;			
			var deptArray=[];
			var userArray=[];
	
			//获取机构代码
			setting3 = {									
					async : true,				
					asyncUrl : "ll_mechanismReport.htm?method=createDeptTree", //获取节点数据的URL地址
				    isSimpleData : true,
					rootPID : 0,
					treeNodeKey : "id",
					treeNodeParentKey : "pId",
					asyncParam: ["depId"],	
					callback: {
						click: zTreeOnClick3,					
						asyncSuccess:function(event, treeId, treeNode, msg){
							if(!treeNode){
							   var rootNode = zTree3.getNodeByParam("level", 0);
							   zTree3.expandNode(rootNode, true, false);
							}
						}
					}
			};
	
			//获取人员代码
			setting2 = {    			    
					async : true,
					asyncUrl :"ll_operatorReport.htm?method=createUserTree", //获取节点数据的URL地址
					checkable : true,
				    checkStyle : "checkbox",
				    checkType : { "Y": "s", "N": "s" },	
					isSimpleData: true,
					rootPID : -1,
					treeNodeKey: "id",
					treeNodeParentKey: "pId",
					asyncParam: ["depId"],
					callback: {				  
						change: zTreeOnClick2,
						asyncSuccess:function(event, treeId, treeNode, msg){
							if(!treeNode){
							   var rootNode = zTree2.getNodeByParam("level", 0);
							   zTree2.expandNode(rootNode, true, false);
							}
						}
					}
			};
			
			var zNodes3 =[];
			var zNodes2 =[];
	
			//隐藏人员树形控件
			function showMenu() {
				hideMenu2();
				var sortSel = $("#depNam");
				var sortOffset = $("#depNam").offset();
				$("#dropMenu").toggle();
			}
			//隐藏机构树形控件
			function showMenu2() {
				hideMenu();
				var sortSel = $("#userName");
				var sortOffset = $("#userName").offset();
				$("#dropMenu2").toggle();
			}
			//隐藏机构树形控件
			function hideMenu() {
				$("#dropMenu").hide();
			}
			//隐藏人员树形控件
			function hideMenu2() {
				$("#dropMenu2").hide();
			}
			//选中的机构显示文本框
			function zTreeOnClick3(event, treeId, treeNode) {
				if (treeNode) {
					$("#depNam").attr("value", treeNode.name); //设置机构属性
					$("#deptString").attr("value", treeNode.id); //设置机构代码	
				}				
			}	
			//选中的机构显示文本框
			function zTreeOnClickOK3() {
					hideMenu();
					cleanSelect_user();
			}	
			//选中的人员显示文本框
			function zTreeOnClick2(event, treeId, treeNode) {
				if (treeNode) {				
					var zTreeNodes2=zTree2.getChangeCheckedNodes();
					
					var pops="";
					var params=[]; //获取人员字符串     				
					for(var i=0; i<zTreeNodes2.length; i++){				
						pops+=zTreeNodes2[i].name+";";
						params[i]=zTreeNodes2[i].id;	//人员编号     								
					}					
					$("#userName").attr("value", pops); //设置人员属性
					$("#userString").attr("value", params);	//设置人员代码
					
					if(zTreeNodes2.length==0){
					 cleanSelect_user();
					}
				}				
			}
			//选中的人员显示文本框
			function zTreeOnClickOK2() {
			    hideMenu2();
				var zTreeNodes2=zTree2.getChangeCheckedNodes();
				var pops="";				
				for(var i=0; i<zTreeNodes2.length; i++){
					pops+=zTreeNodes2[i].name+";";					
				}					
				$("#userName").attr("value", pops);
				if(zTreeNodes2.length==0){
				 cleanSelect_user();
				}
			 }
			
	   		// 加载人员/机构树形控件
			function reloadTree() {
				hideMenu();
				hideMenu2();
				zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
				zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);		
			}
			function numberControl(va)
			{
				var pat=/^\d*$/;
				if(!pat.test(va.val()))
				{
					va.val(va.val().replace(/[^\d]/g,''));
				}
			}
	
			function cleanSelect_user()
			{
			    var checkNodes = zTree2.getCheckedNodes();
			    for(var i=0;i<checkNodes.length;i++){
			    	checkNodes[i].checked=false;
			    }
			    zTree2.refresh();
				$('#userName').attr('value', '');
				$('#userName').attr('value', getJsLocaleMessage("qyll","qyll_lldg_alter_136"));
				$('#userString').attr('value', '');				
			}
	
			function cleanSelect_dep()
			{
				$('#depNam').attr('value', '');
				$('#depNam').attr('value', getJsLocaleMessage("qyll","qyll_lldg_alter_136"));
				$('#deptString').attr('value', '');
			}
			$(document).ready(function(){
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				$("#search").click(function(){
					submitForm();
				});
			});
			
			function cancel(id){
				window.location.href="ll_packageOrderQuest.htm?method=updateHistorieById&id="+id;
			}

			$("#exportCondition").click(function(){
				if(confirm(getJsLocaleMessage("qyll","qyll_lldg_alter_137"))){
					var orderNo = $("#orderNo").val();
					var theme = $("#theme").val();
					var state = $("#state").val();
					var isp = $("#isp").val();
					var topic = $("#topic").val();
					var startTime = $("#startTime").val();
					var endTime = $("#endTime").val();
					var organization = $("#depNam").val();
					var operator = $("#userName").val();
					<% if(resultList != null && resultList.size() > 0) {%>
						$.ajax({
							type: "POST",
							url: "ll_packageOrderQuest.htm?method=exportExcel",
							data:{
								orderNo:orderNo,
								theme:theme,
								state:state,
								isp:isp,
								topic:topic,
								startTime:startTime,
								endTime:endTime,
								organization:organization,
								operator:operator
							},
							 beforeSend:function () {
				                    page_loading();
				                },
				                complete:function () {
				               	  	page_complete();
				                },
				                success:function (result) {
					                if (result == 'true') {
				                        download_href("ll_packageOrderQuest.htm?method=downFile");
				                    } else {
				                    	alert(getJsLocaleMessage("qyll","qyll_lldg_alter_138"));
				                    }
				                }
						});
					<%}else{%>
					alert(getJsLocaleMessage("qyll","qyll_lldg_alter_139"));
					<%}%>
		}
						});

		function showBatchDetail(orderNo) {
			$.ajax({
				type : "POST",
				url : "ll_orderHistoryQuest.htm?method=findDetailByIds&orderNo=" + orderNo,
				success : function(object) {
					var data = $.parseJSON(object);
					$("#batchDetailTb tbody").html("");
					if (data.length == 0) {
						$("#batchDetailTb tbody").append(
								"<tr><td colspan='3'><emp:message key="qyll_common_127" defVal="无相关记录" fileName="qyll" /></td></tr>");
					} else {
						$.each(data, function(i) {
							$("#batchDetailTb tbody").append(
									"<tr>" + "<td>" + data[i].productName
											+ "</td>" + "<td>" + data[i].isp
											+ "</td>" + "<td>" + data[i].count
											+ "</td>" + "</tr>");
						});
					}
				}
			});
			$('#batchDetail_box').dialog({
				autoOpen : false,
				height : 180,
				width : 380,
				resizable : false,
				modal : true
			});
			$('#batchDetail_box').dialog('open');
		}

		function downloadNumber(orderNo) {
			if(confirm(getJsLocaleMessage("qyll","qyll_lldg_alter_137"))){
				$
						.ajax({
							type : "POST",
							url : "ll_packageOrderQuest.htm?method=exportNumExcel",
							data : {
								orderNo : orderNo
							},
							beforeSend : function() {
								page_loading();
							},
							complete : function() {
								page_complete();
							},
							success : function(result) {
								if (result == 'true') {
									download_href("ll_packageOrderQuest.htm?method=downNumber");
								} else {
									alert(getJsLocaleMessage("qyll","qyll_lldg_alter_138"));
								}
							}
						});
			}
		}
	</script>
</body>
</html>
