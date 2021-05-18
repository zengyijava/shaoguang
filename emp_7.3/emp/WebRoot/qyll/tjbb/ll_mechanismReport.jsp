<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.qyll.vo.LlMechanismReportVo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Calendar" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
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
	String menuCode = titleMap.get("mechanismReport");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	LlMechanismReportVo llMechanismReport=(LlMechanismReportVo)request.getAttribute("llMechanismReport");
	LlMechanismReportVo reportSum=(LlMechanismReportVo)request.getAttribute("llMechanismReportSum");
	List<LlMechanismReportVo> resultList = (List<LlMechanismReportVo>) request.getAttribute("resultList");
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
		<link href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<style type="text/css">
			body div#tooltip { position:absolute; z-index:1000; max-width:435px; _width:expression(this.scrollWidth > 435 ? "435px" : "auto"); width:auto; background:#A8CFF6; border:#FEFFD4 solid 1px; text-align:left; padding:6px;}
			body div#tooltip p { margin:0;padding:6; color:#FFFFFE;font:12px verdana,arial,sans-serif;}
			body div#tooltip p em { display:block;margin-top:3px; color:#f60;font-style:normal; font-weight:bold;}
			#content tr.peachpuff,#content tr.peachpuff td{background:#FFDAB9;}
			#msgcont {
                white-space: pre-wrap;
                *white-space: pre;
                *word-wrap: break-word;
                word-break: break-all;
            }
            <%if("1".equals(llMechanismReport.getIsDel())){%>
				#condition{display:none;}
			<%}%>
		</style>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/qyll_<%=langName%>.js"></script>
	</head>

	<body>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName,menuCode) %>
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="ll_mechanismReport.htm?method=find" method="post" id="pageForm">
					<div class="buttons">
						<c:choose>
							<c:when test="${llMechanismReport.isDel==1}">
								<span id="backgo" class="right mr5" onclick="javascript:showback()">&nbsp;<emp:message key="qyll_common_69" defVal="返回" fileName="qyll"></emp:message></span>
								<input style="display: none" id="isDel" value="${llMechanismReport.isDel}">
							</c:when>
							<c:otherwise>
								<div id="toggleDiv" >
								</div>
							</c:otherwise>
						</c:choose>
						<a id="exportCondition"><emp:message key="qyll_common_92" defVal="导出" fileName="qyll"></emp:message></a>						
					</div>
					<div id="condition">
						<table>
						   
							<tbody>
							
								<tr>
									<td>
										<emp:message key="qyll_common_100" defVal="隶属机构" fileName="qyll"></emp:message>：
									</td>									
									<td class="condi_f_l">
										<div style="width: 220px;">
											<input type="text" id="depNam" name="depNam" value="<c:if test="${llMechanismReport.depNam==-9999}"><emp:message key="qyll_common_101" defVal="请选择" fileName="qyll" /></c:if><c:if test="${llMechanismReport.depNam != -9999}">${llMechanismReport.depNam}</c:if>"
												onclick="javascript:showMenu();" readonly style="width: 160px; cursor: pointer;" class="treeInput" />
											&nbsp;
										</div>
										<div id="dropMenu">
											<div
												style="margin-top: 3px; margin-right: 10px; text-align: right">
												<input type="button" value="<emp:message key='qyll_common_28' defVal='确定' fileName='qyll'></emp:message>" class="btnClass1"	onclick="javascript:zTreeOnClickOK3();" style="width: 50px;" />&nbsp;&nbsp;
												<input type="button" value="<emp:message key='qyll_common_102' defVal='清空' fileName='qyll'></emp:message>" class="btnClass1"	onclick="javascript: cleanSelect_dep();" style="width: 50px;" />
											</div>
											<ul id="dropdownMenu" class="tree"></ul>
										</div>
									</td>
									<td>
										<emp:message key="qyll_common_149" defVal="套餐编号" fileName="qyll"></emp:message>：
									</td>									
									<td>	
									  	<input type="text" id="productId"  name="productId" value="${llMechanismReport.productId==null?'':llMechanismReport.productId}" style="width: 180px">
									</td>
									<td>
										<emp:message key="qyll_common_150" defVal="套餐名称" fileName="qyll"></emp:message>：
									</td>									
									<td>	
									  	<input type="text" id="productName"  name="productName" value="${llMechanismReport.productName==null?'':llMechanismReport.productName}" style="width: 180px">
									</td>
									
									<td class="tdSer">
									       <center><a id="search"></a></center>
								    </td>		
								</tr>	
								<tr>
									<td>
										<emp:message key="qyll_common_43" defVal="运营商" fileName="qyll"></emp:message>：
									</td>
									<td id="myType">
										<select id="isp" name="isp" style="width: 184px;">
											<option value="9999" ><emp:message key="qyll_common_78" defVal="全部" fileName="qyll"></emp:message></option>
											<option value="1" <c:if test="${llMechanismReport.isp==1}">selected='selected'</c:if>><emp:message key="qyll_common_185" defVal="移动" fileName="qyll"></emp:message></option>
											<option value="2" <c:if test="${llMechanismReport.isp==2}">selected='selected'</c:if>><emp:message key="qyll_common_187" defVal="电信" fileName="qyll"></emp:message></option>
											<option value="3" <c:if test="${llMechanismReport.isp==3}">selected='selected'</c:if>><emp:message key="qyll_common_186" defVal="联通" fileName="qyll"></emp:message></option>
										</select>
									</td>
								</tr>
								<tr>
									<td>
										<emp:message key="qyll_common_151" defVal="报表类型" fileName="qyll"></emp:message>：
									</td>									
									<td>	
										<select id="reportType" name="reportType" style="width: 184px;">
											<option value="1" ><emp:message key="qyll_common_152" defVal="日报表" fileName="qyll"></emp:message></option>
											<option value="2" <c:if test="${llMechanismReport.reportType==2}">selected='selected'</c:if>><emp:message key="qyll_common_153" defVal="月报表" fileName="qyll"></emp:message></option>
											<option value="3" <c:if test="${llMechanismReport.reportType==3}">selected='selected'</c:if>><emp:message key="qyll_common_154" defVal="年报表" fileName="qyll"></emp:message></option>
										</select>
									</td>
									<c:choose>
										<c:when test="${llMechanismReport.reportType==1||llMechanismReport.reportType==9999}">
											<td class="dayTime">
									      	 	<emp:message key="qyll_common_155" defVal="统计时间" fileName="qyll"></emp:message>：
									   		</td>
									   		<td class="dayTime">
									    	<input type="text"
												style="cursor: pointer; width: 180px; background-color: white;"
												readonly="readonly" class="Wdate" onclick="updateTime()" 
												value="${llMechanismReport.sendtime}"  id="sendtime" name="sendtime">
										   </td>
										   <td class="dayTime">
										   		<emp:message key="qyll_common_105" defVal="至" fileName="qyll"></emp:message>：
										   </td>
										   <td class="dayTime">
										      	<input type="text"
													style="cursor: pointer; width: 180px; background-color: white;"
													readonly="readonly" class="Wdate"  onclick="updateTime('1')"
													value="${llMechanismReport.recvtime}"    id="recvtime" name="recvtime" >
										    </td>
									     	<td  class="yearTime" style="display: none"><emp:message key="qyll_common_156" defVal="统计年月" fileName="qyll"></emp:message>：</td>
										    <td  class="yearTime" style="display: none">
										    	<input type="text"
													style="cursor: pointer; width: 180px; background-color: white;"
													readonly="readonly" class="Wdate" onclick="updateTime()" 
													value="${llMechanismReport.statisticsTime}"  id="statisticsTime" name="statisticsTime"
										    <td>
										    <td  class="yearTime" style="display: none"></td>
										    <td  class="yearTime" style="display: none"></td>
										</c:when>
										<c:otherwise>
									 		<td class="dayTime" style="display: none">
									      		 <emp:message key="qyll_common_155" defVal="统计时间" fileName="qyll"></emp:message>：
									   		</td>
									   		<td class="dayTime" style="display: none">
										    	<input type="text"
													style="cursor: pointer; width: 180px; background-color: white;"
													readonly="readonly" class="Wdate" onclick="updateTime()" 
													value="${llMechanismReport.sendtime}"  id="sendtime" name="sendtime">
									   		</td>
											<td class="dayTime" style="display: none">
										   		<emp:message key="qyll_common_105" defVal="至" fileName="qyll"></emp:message>：
										   	</td>
										   	<td class="dayTime" style="display: none">
									      		<input type="text"
													style="cursor: pointer; width: 180px; background-color: white;"
													readonly="readonly" class="Wdate"  onclick="updateTime()"
													value="${llMechanismReport.recvtime}"    id="recvtime" name="recvtime" >
									    	</td>
									     	<td  class="yearTime" ><emp:message key="qyll_common_156" defVal="统计年月" fileName="qyll"></emp:message>：</td>
										    <td  class="yearTime" >
										    	<input type="text"
													style="cursor: pointer; width: 180px; background-color: white;"
													readonly="readonly" class="Wdate" onclick="updateTime()" 
													value="${llMechanismReport.statisticsTime}"  id="statisticsTime" name="statisticsTime">
										    <td>
										    <td  class="yearTime" ></td>
										    <td  class="yearTime" ></td>
										</c:otherwise>
									</c:choose>
								    <td></td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="qyll_common_157" defVal="时间" fileName="qyll"></emp:message>
								</th>
								<th>
									<emp:message key="qyll_common_100" defVal="隶属机构" fileName="qyll"></emp:message>
								</th>
								<th>
									<emp:message key="qyll_common_149" defVal="套餐编号" fileName="qyll"></emp:message>
								</th>
								<th>
									<emp:message key="qyll_common_150" defVal="套餐名称" fileName="qyll"></emp:message>
								</th>
								<th>
									<emp:message key="qyll_common_43" defVal="运营商" fileName="qyll"></emp:message>
								</th>
								<th>
									<emp:message key="qyll_common_158" defVal="提交号码数" fileName="qyll"></emp:message>
								</th>
								<th>
									<emp:message key="qyll_common_113" defVal="订购成功数" fileName="qyll"></emp:message>
								</th>
								<th>
									<emp:message key="qyll_common_114" defVal="订购失败数" fileName="qyll"></emp:message>
								</th>
								<c:choose>
									<c:when test="${llMechanismReport.isDel!=1}">
										<th><emp:message key="qyll_common_200" defVal="详情" fileName="qyll"></emp:message></th>
									</c:when>
								</c:choose>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${llMechanismReport.isFirstEnter==0}">
									<tr>
										<td colspan="9" align="center"><emp:message key="qyll_common_81" defVal="请点击查询获取数据" fileName="qyll"></emp:message></td>
									</tr>
								</c:when>
								<c:when test="${fn:length(resultList) != 0}">
									<c:forEach items="${resultList}" var="report" varStatus="i">
										<tr>
											<c:choose>
												<c:when test="${llMechanismReport.isDel==1}">
													<td>
														<c:choose>
															<c:when test="${llMechanismReport.reportType==3}">
																${fn:substring(report.reportDate, 4 , 6)}<emp:message key="qyll_common_160" defVal="月" fileName="qyll"></emp:message>
															</c:when>
															<c:when test="${llMechanismReport.reportType==1||llMechanismReport.reportType==2||llMechanismReport.reportType==9999}">
																${fn:substring(report.reportDate, 0 , 4)}<emp:message key="qyll_common_159" defVal="年" fileName="qyll"></emp:message>${fn:substring(report.reportDate, 4 , 6)}<emp:message key="qyll_common_160" defVal="月" fileName="qyll"></emp:message>${fn:substring(report.reportDate, 6 , 8)}<emp:message key="qyll_common_161" defVal="日" fileName="qyll"></emp:message>
															</c:when>
															<c:otherwise>-</c:otherwise>
														</c:choose>
													</td>	
												</c:when>
												<c:otherwise>
													<td>${llMechanismReport.showTime}</td>
												</c:otherwise>
											</c:choose>
											<%-- <td>${report.uName==null?"未知":report.uName}</td> --%>
											<td>${report.depNam==null?"-":report.depNam}</td>
											<td>
												${report.productId}
											</td>
											<td>
												${report.productName}
											</td>
											<c:choose>
												<c:when test="${report.isp==1}"><td><emp:message key="qyll_common_185" defVal="移动" fileName="qyll"></emp:message></td></c:when>
												<c:when test="${report.isp==2}"><td><emp:message key="qyll_common_187" defVal="电信" fileName="qyll"></emp:message></td></c:when>
												<c:when test="${report.isp==3}"><td><emp:message key="qyll_common_186" defVal="联通" fileName="qyll"></emp:message></td></c:when>
												<c:otherwise><td>--</td></c:otherwise>
											</c:choose>
											<td>${report.sunMitNum}</td>
											<td>${report.succNum}</td>
											<td>${report.faildNum}</td>
											<c:choose>
												<c:when test="${llMechanismReport.isDel!=1}">
													<td>
														<a onclick="detail('${report.productId}','1')"><emp:message key="qyll_common_123" defVal="查看" fileName="qyll"></emp:message></td></a>
													</td>
												</c:when>
											</c:choose>
										</tr>
									</c:forEach>
									<tr>
										<td colspan="5"><b><emp:message key="qyll_common_162" defVal="合计" fileName="qyll"></emp:message>：</b></td>
										<td><%=reportSum.getSunMitNumSum() %></td>
										<td><%=reportSum.getSuccNumSum() %></td>
										<td><%=reportSum.getFaildNumSum() %></td>
										<c:choose>
											<c:when test="${llMechanismReport.isDel!=1}">
												<td>
													-
												</td>
											</c:when>
										</c:choose>
									</tr>
								</c:when>
								<c:otherwise>
									<tr><td align="center" colspan="9"><emp:message key="qyll_common_82" defVal="无记录" fileName="qyll"></emp:message></td></tr>
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
							</div>
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
				if(${llMechanismReport.depNam==null}){
					$("#depNam").val(getJsLocaleMessage("qyll","qyll_lldg_alter_136"));
				}
			});
			var zTree3;
			var setting3;
			var deptArray=[];
			var zNodes3 =[];
	
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
	
	
			//隐藏机构树形控件
			function showMenu() {
				hideMenu();
				var sortSel = $("#depNam");
				var sortOffset = $("#depNam").offset();
				$("#dropMenu").toggle();
			}
			
			//隐藏机构树形控件
			function hideMenu() {
				$("#dropMenu").hide();
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
			}
			// 加载机构树形控件
			function reloadTree() {
				hideMenu();
				zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
			}
			$(function(){
				closeTreeFun(["dropMenu"],[""]);
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
			function numberControl(va)
			{
				var pat=/^\d*$/;
				if(!pat.test(va.val()))
				{
					va.val(va.val().replace(/[^\d]/g,''));
				}
			}


			function cleanSelect_dep()
			{
				$('#depNam').attr('value', '');
				$('#depNam').attr('value', getJsLocaleMessage("qyll","qyll_lldg_alter_136"));
				$('#deptString').attr('value', '');
			}
			
			function showback(){
				history.back();
			}
			$(document).ready(function(){
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				$("#search").click(function(){
					var sendtime = $("#sendtime").val();
					var recvtime = $("#recvtime").val();
					sendtime =sendtime.split("-")[0];
					recvtime =recvtime.split("-")[0];
					if(sendtime != recvtime){
						alert(getJsLocaleMessage("qyll","qyll_lldg_alter_143"));
						return;
					}
					submitForm();
				});
			});
			function detail(productId,isDel){
				var statisticsTime = $("#statisticsTime").val();
				var sendtime = $("#sendtime").val();
				var productName = $("#productName").val();
				var isp = $("#isp").val();
				var reportType = $("#reportType").val();
				var recvtime = $("#recvtime").val();
				var depNam = $("#depNam").val();
				var userName = $("#userName").val();
				productName = encodeURIComponent(productName);
				productName = encodeURIComponent(productName);
				depNam = encodeURIComponent(depNam);
				depNam = encodeURIComponent(depNam);
				userName = encodeURIComponent(userName);
				userName = encodeURIComponent(userName);
				window.location.href="ll_mechanismReport.htm?method=toDetail"+"&sendtime="+sendtime+"&productName="+productName+"&isp="+isp+"&reportType="+reportType+"&recvtime="+recvtime+"&isDel="+isDel+"&productId="+productId+"&statisticsTime="+statisticsTime+"&userName="+userName+"&depNam="+depNam;
			}
			$("#exportCondition").click(function(){
			  	if(confirm(getJsLocaleMessage("qyll","qyll_lldg_alter_137"))){
					var statisticsTime = $("#statisticsTime").val();
					var sendtime = $("#sendtime").val();
					var productName = $("#productName").val();
					var isp = $("#isp").val();
					var reportType = $("#reportType").val();
					var recvtime = $("#recvtime").val();
					var isDel = $("#isDel").val();
					var productId = $("#productId").val();
					var depNam = $("#depNam").val();
					var userName = $("#userName").val();
					productName = encodeURIComponent(productName);
					depNam = encodeURIComponent(depNam);
					userName = encodeURIComponent(userName);
					<% if(resultList != null && resultList.size() > 0) {%>
						$.ajax({
							type: "POST",
							url: "ll_mechanismReport.htm?method=rep_orderExportExcel",
							data:{
								statisticsTime:statisticsTime,
								sendtime:sendtime,
								productName:productName,
								isp:isp,
								reportType:reportType,
								recvtime:recvtime,
								isDel:isDel,
								productId:productId,
								depNam:depNam,
								userName:userName
							},
							 beforeSend:function () {
				                    page_loading();
				                },
				                complete:function () {
				               	  	page_complete()
				                },
				                success:function (result) {
					                if (result == 'true') {
				                        download_href("ll_mechanismReport.htm?method=downFile");
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
