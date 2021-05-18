<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.qyll.entity.LlProduct"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Calendar" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="static com.montnets.emp.common.constant.ViewParams.getPosition" %>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
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
	String menuCode = titleMap.get("flowSynchronize");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
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
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<style type="text/css">
			#content tr.peachpuff,#content tr.peachpuff td{background:#FFDAB9;}
			#msgcont {
                white-space: pre-wrap;
                *white-space: pre;
                *word-wrap: break-word;
                word-break: break-all;
            }
            .handbuttons{
				width:90px;
				text-indent: 32px;
				font-size: 12px;
				height: 24px;
				line-height: 24px;
				cursor: pointer;
			}
		</style>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/qyll_<%=langName%>.js"></script>
	</head>

	<body>
	<%=ViewParams.getPosition(langName,menuCode) %>
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="ll_flowSynchronize.htm?method=find" method="post" id="pageForm">
					<div class="buttons">
						<div id="toggleDiv" >
						</div>
						<div class="handbuttons">
							<div id="handSyn" name="handSyn" ><emp:message key='qyll_common_75' defVal='手动同步' fileName='qyll'></emp:message></div>
						</div>
					</div>
					<div id="condition">
						<table>
						   
							<tbody>
							
								<tr>
									<td>
										<emp:message key="qyll_common_149" defVal="套餐编号" fileName="qyll"></emp:message>：
									</td>									
									<td>	
									  	<input type="text" id="productid"  name="productid" value="${llProduct.productid==null?'':llProduct.productid}" style="width: 180px">
									</td>
									<td>
										<emp:message key="qyll_common_150" defVal="套餐名称" fileName="qyll"></emp:message>：
									</td>									
									<td>	
									  	<input type="text" id="productname"  name="productname" value="${llProduct.productname==null?'':llProduct.productname}" style="width: 180px">
									</td>
									<td>
										<emp:message key="qyll_common_43" defVal="运营商" fileName="qyll"></emp:message>：
									</td>
									<td id="myType">
										<select id="isp" name="isp" style="width: 184px;">
											<option value="9999" ><emp:message key="qyll_common_78" defVal="全部" fileName="qyll"></emp:message></option>
											<option value="1" <c:if test="${llProduct.isp==1}">selected='selected'</c:if>><emp:message key="qyll_common_185" defVal="移动" fileName="qyll"></emp:message></option>
											<option value="2" <c:if test="${llProduct.isp==2}">selected='selected'</c:if>><emp:message key="qyll_common_187" defVal="电信" fileName="qyll"></emp:message></option>
											<option value="3" <c:if test="${llProduct.isp==3}">selected='selected'</c:if>><emp:message key="qyll_common_186" defVal="联通" fileName="qyll"></emp:message></option>
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
								<th>
									<emp:message key="qyll_common_149" defVal="套餐编号" fileName="qyll"></emp:message>
								</th>
								<th>
									<emp:message key="qyll_common_150" defVal="套餐名称" fileName="qyll"></emp:message>
								</th>
								<th>
									<emp:message key="qyll_common_79" defVal="流量包大小（M）" fileName="qyll"></emp:message>
								</th>
								<th>
									<emp:message key="qyll_common_80" defVal="套餐价格（元）" fileName="qyll"></emp:message>
								</th>
								<th>
									<emp:message key="qyll_common_43" defVal="运营商" fileName="qyll"></emp:message>
								</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${llProduct.isFirstEnter==0}">
									<tr>
										<td colspan="8" align="center"><emp:message key="qyll_common_81" defVal="请点击查询获取数据" fileName="qyll"></emp:message></td>
									</tr>
								</c:when>
								<c:when test="${fn:length(resultList) != 0}">
									<c:forEach items="${resultList}" var="list" varStatus="i">
										<tr>
											<td>${list.productid}</td>
											<td>${list.productname}</td>
											<td>${list.volume}</td>
											<td>${list.price}</td>
											<c:choose>
												<c:when test="${list.isp==1}"><td><emp:message key="qyll_common_185" defVal="移动" fileName="qyll"></emp:message></td></c:when>
												<c:when test="${list.isp==2}"><td><emp:message key="qyll_common_187" defVal="电信" fileName="qyll"></emp:message></td></c:when>
												<c:when test="${list.isp==3}"><td><emp:message key="qyll_common_186" defVal="联通" fileName="qyll"></emp:message></td></c:when>
												<c:otherwise><td>--</td></c:otherwise>
											</c:choose>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr><td align="center" colspan="5"><emp:message key="qyll_common_82" defVal="无记录" fileName="qyll"></emp:message></td></tr>
								</c:otherwise>
							</c:choose>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="9">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<%-- foot开始 --%>
					
					<div class="bottom">
						<div id="bottom_right">
							<div id="bottom_left"></div>
							<div id="bottom_main">
								<div id="pageInfo"></div>
							</div>
						</div>
					</div>
					<div id="orderCode" class="hidden"></div>
				</form>
			</div>
			
		</div>
   		<div class="clear"></div>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script> 
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				$("#search").click(function(){
					submitForm();
				});
				$(".handbuttons").css({"background":"url(<%=iPath%>/img/handSyn.png) no-repeat","background-size":"100% 100%"});
			});
			$(function(){
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
			$("#handSyn").click(function(){
				$.ajax({
					type: "POST",
					url: "ll_flowSynchronize.htm?method=handSyn",
					beforeSend:function () {
	                    page_loading();
	                },
		                complete:function () {
	               	  	page_complete()
	                },
	                success:function (result) {
                	 	if (result == 'true') {
                	 		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_140"));	
	                        location.reload();
	                    }else if(result =='falses') {
                       		alert(getJsLocaleMessage("qyll","qyll_lldg_alter_141"));
	                    }else{
                    	 	alert(getJsLocaleMessage("qyll","qyll_lldg_alter_142"));
	                    }
	                }
				});
			});
		</script>
	</body>
</html>
