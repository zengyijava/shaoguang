<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.qyll.vo.LlOperatorReportVo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
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
	String titlePath = (String)request.getAttribute("titlePath");
	String menuCode = titleMap.get(titlePath);
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
<link href="<%=skin%>/frame.css" rel="stylesheet" type="text/css" />
<link href="<%=skin%>/table.css" rel="stylesheet" type="text/css" />
<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css">
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
</style>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/qyll_<%=langName%>.js"></script>
</head>
<body>
	<div id="container" class="container">
		<%-- header开始 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode)%>
		<%-- header结束 --%>
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">

			<div class="text" id="u872" style="visibility: visible;">
				<p>
					<span style='font-family: "Arial Negreta", "Arial"; font-weight: 700;'><emp:message key="qyll_common_6" defVal="订购主题" fileName="qyll" />：</span>
					<span style='font-family: "Arial Normal", "Arial"; font-weight: 400;'>&nbsp;${topic}</span>
				</p>
				<p>
					<span style='font-family: "Arial Negreta", "Arial"; font-weight: 700;'><emp:message key="qyll_common_86" defVal="订购套餐" fileName="qyll" />：</span>
					<c:if test="${fn:length(productList) != 0}">
						<c:forEach items="${productList}" var="product">
							<span style='font-family: "Arial Normal", "Arial"; font-weight: 400;'>&nbsp;${product.productName}&nbsp;&nbsp;</span>
						</c:forEach>
					</c:if>
				</p>
				<p>
					<span style='font-family: "Arial Negreta", "Arial"; font-weight: 700;'><emp:message key="qyll_common_19" defVal="订购时间" fileName="qyll" />：
					</span> <span style='font-family: "Arial Normal", "Arial"; font-weight: 400;'>&nbsp;${orderTM}</span>
				</p>
				<p>
					<span style='font-family: "Arial Negreta", "Arial"; font-weight: 700;'><emp:message key="qyll_common_87" defVal="订购情况" fileName="qyll" />：</span>
					<span style='font-family: "Arial Normal", "Arial"; font-weight: 400;'>&nbsp;<emp:message key="qyll_common_88" defVal="提交总数"
							fileName="qyll" />：${subCount}<emp:message key="qyll_common_89" defVal="个， 订购成功数" fileName="qyll" />：${sucCount}<emp:message
							key="qyll_common_90" defVal="个 ，订购失败数" fileName="qyll" />：${subCount-sucCount} <emp:message key="qyll_common_91" defVal="个。" fileName="qyll" /></span>
				</p>
			</div>
			<form name="pageForm" action="ll_orderHistoryQuest.htm?method=findProductsById" method="post" id="pageForm">
				<input type="hidden" id="ids" name="ids" value="${ids}"> <input type="hidden" name="topic" value="${topic}"> <input type="hidden"
					id="orderno" name="orderNo" value="${orderNo}"> <input type="hidden" name="orderTM" value="${orderTM}"> <input type="hidden"
					name="subCount" value="${subCount}"> <input type="hidden" name="sucCount" value="${sucCount}"> <input type="hidden"
					name="faiCount" value="${faiCount}"> <input type="hidden" name="effCount" value="${effCount}">
				<div class="buttons">
					<a id="exportCondition"><emp:message key="qyll_common_92" defVal="导出" fileName="qyll" /></a><span id="backgo" class="right mr5"
						onclick="javascript:history.back();">返回</span>
					<div id="toggleDiv"></div>
				</div>
				<div id="condition">
					<table>
						<tbody>
							<tr>
								<td><emp:message key="qyll_common_93" defVal="手机号码" fileName="qyll" />：</td>
								<td id="myType"><input type="text" id="mobile" name="mobile" style="width: 180px" value="${param.mobile}"></td>
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
								<td><emp:message key="qyll_common_94" defVal="订购状态" fileName="qyll" />：</td>
								<td id="myType"><select id="state" name="state" style="width: 184px;">
										<option value=""><emp:message key="qyll_common_78" defVal="全部" fileName="qyll" /></option>
										<option value="0" <c:if test="${param.state=='0'}">selected="selected"</c:if>><emp:message key="qyll_common_205" defVal="提交成功"
												fileName="qyll" /></option>
										<option value="4" <c:if test="${param.state=='4'}">selected="selected"</c:if>><emp:message key="qyll_common_206" defVal="提交失败"
												fileName="qyll" /></option>
										<option value="1" <c:if test="${param.state=='1'}">selected="selected"</c:if>><emp:message key="qyll_common_96" defVal="订购成功"
												fileName="qyll" /></option>
										<option value="2" <c:if test="${param.state=='2'}">selected="selected"</c:if>><emp:message key="qyll_common_97" defVal="订购失败"
												fileName="qyll" /></option>
								</select></td>
								<td class="tdSer">
									<center>
										<a id="search"></a>
									</center>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<table id="content">
					<thead>
						<tr>
							<th><emp:message key="qyll_common_93" defVal="手机号码" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_77" defVal="套餐名称" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_43" defVal="运营商" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_144" defVal="状态报告" fileName="qyll" /></th>
							<th><emp:message key="qyll_common_99" defVal="订购编号" fileName="qyll" /></th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${fn:length(resultList) != 0}">
								<c:forEach items="${resultList}" var="orderDetail">
									<tr>
										<td>${orderDetail.mobile}</td>
										<td>${orderDetail.theme}</td>
										<td><c:if test="${orderDetail.isp=='1'}">
												<emp:message key="qyll_common_185" defVal="移动" fileName="qyll" />
											</c:if> <c:if test="${orderDetail.isp=='2'}">
												<emp:message key="qyll_common_187" defVal="电信" fileName="qyll" />
											</c:if> <c:if test="${orderDetail.isp=='3'}">
												<emp:message key="qyll_common_186" defVal="联通" fileName="qyll" />
											</c:if></td>
										<td><c:if test="${orderDetail.state=='0'}">
												<emp:message key="qyll_common_205" defVal="提交成功" fileName="qyll" />
											</c:if> <c:if test="${orderDetail.state=='4'}">
												<emp:message key="qyll_common_206" defVal="提交失败" fileName="qyll" />
											</c:if> <c:if test="${orderDetail.state=='1'}">
												<emp:message key="qyll_common_96" defVal="订购成功" fileName="qyll" />
											</c:if> <c:if test="${orderDetail.state=='2'}">
												<emp:message key="qyll_common_97" defVal="订购失败" fileName="qyll" />(${orderDetail.errcode})</c:if></td>
										<td>${orderDetail.orderno}</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:when test="${isFirstEnter != null}">
								<tr>
									<td align="center" colspan="5"><font color="blue"><emp:message key="qyll_common_81" defVal="请点击查询获取数据" fileName="qyll" /></font></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td align="center" colspan="5"><emp:message key="qyll_common_82" defVal="无记录" fileName="qyll" /></td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="5">
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
	<script type="text/javascript" src="<%=iPath%>/js/reportTime.js"></script>
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
					asyncUrl : "ll_operatorReport.htm?method=createDeptTree&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lguserid=<%=request.getParameter("lguserid")%>", //获取节点数据的URL地址
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
					asyncUrl :"ll_operatorReport.htm?method=createUserTree&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lguserid=<%=request.getParameter("lguserid")%>", //获取节点数据的URL地址
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
			

			$("#exportCondition").click(function(){
				if(confirm(getJsLocaleMessage("qyll","qyll_lldg_alter_137"))){
					var orderno = $("#orderno").val();
					var mobile = $("#mobile").val();
					var state = $("#state").val();
					var isp = $("#isp").val();
					<% if(resultList != null && resultList.size() > 0) {%>
						$.ajax({
							type: "POST",
							url: "ll_orderHistoryQuest.htm?method=exportDetailExcel",
							data:{
								orderno:orderno,
								mobile:mobile,
								state:state,
								isp:isp,
							},
							 beforeSend:function () {
				                    page_loading();
				                },
				                complete:function () {
				               	  	page_complete();
				                },
				                success:function (result) {
					                if (result == 'true') {
				                        download_href("ll_orderHistoryQuest.htm?method=downDetailFile");
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
	</script>
</body>
</html>
