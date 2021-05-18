<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@ page import="com.montnets.emp.rms.servmodule.constant.ServerInof" %>
<%@ page import="com.montnets.emp.rms.vo.LfTemplateVo" %>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<LfTemplateVo> temList = (List<LfTemplateVo>) request.getAttribute("temList");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

	LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
	String lguserid = String.valueOf(loginSysuser.getUserId());

	String choosePublic = request.getAttribute("choosePublic").toString();

	//数据回显
	String tempName = request.getAttribute("tempName")==null?"":request.getAttribute("tempName").toString();
	String tempId = request.getAttribute("tempId")==null?"":request.getAttribute("tempId").toString();
	String tempType = request.getAttribute("tempType")==null?"":request.getAttribute("tempType").toString();
	String pageSource = request.getAttribute("pageSource")==null?"":request.getAttribute("pageSource").toString();
	
	//服务器名称
	String serverName = ServerInof.getServerName();
	//语言
	//String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/rms/samemms/css/tempChoose.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>
	<body class="rms_tempChoose" id="rms_tempChoose">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<form name="pageForm" action="rms_rmsSameMms.htm?method=getLfTemplateByMms&lguserid=<%=lguserid%>" method="post" id="pageForm">
					<div style="display:none" id="hiddenValueDiv"></div>
					<input type="hidden" id="pathUrl" value="<%=path%>" />
					<input type="hidden" id="ipathUrl" value="<%=iPath %>"/>
					<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
					<%--表示查询结果是否为查询结果--%>
					<input type="hidden" id="isSearch" value="0"/>
					<input type="hidden" id="skinType" value="<%=skin %>"/>
					<input id="pathUrl" value="<%=path%>" type="hidden" />
					<input type="hidden" id="lguserid" value ="<%=lguserid%>"/>
					<input type="hidden" id="pageSource" value="<%=pageSource %>">
					<input type="hidden" id="lgcorpcode" value="<%=loginSysuser.getCorpCode()%>"/>
                    <%--公共模板标记--%>
                    <input id="isPublic" name="isPublic" value="<%=choosePublic%>" type="hidden"/>
                    <input id="source" name="source" value="${source}" type="hidden"/>
				
				<div class="fx-module-pop">
			        <%-- module-pop-hd --%>
			        <div class="module-pop-hd">
			            <ul class="hd-table">
			                <li id="myTemp" class="hd-table-li active" onclick="selectTemp('myTemp')">我的场景</li>
			                <li id="publicTemp" class="hd-table-li " onclick="selectTemp('publicTemp')">公共场景</li>
			            </ul>
			            <div class="module-pop-close">
			                <i class="close-icon "></i>
			            </div>
			        </div>
			        <%-- /module-pop-hd --%>
			        <%-- module-pop-container --%>
			        <div class="module-pop-container module-pop-clear" id="selfSelect">
			            <%-- container-left --%>
			            <div id="resultDiv" class="container-left module-pop-fl">
			                <ul id="myResult" class="module-list">
			                    
			                </ul>
			                <ul id="publicResult" class="module-list" style="display:none">
			                    
			                </ul>
			                <div class="module-list-none" id="noResultDiv" style="display:none">
			                    <div class="none-img"></div>
			                    <p>没有找到相符合的场景~~</p>
			                </div>
			            </div>
			            <%-- /container-left --%>
			            <%-- container-right --%>
			            <div id="publicCondition" class="container-right module-pop-fl" style="display:none">
			                <div class="right-wrap">
			                    <div class="form-li">
			                        <input type="text"  maxlength="10" placeholder="请输入场景ID" id="pub_tempId" onkeyup="javascript:numberControl($(this))">
			                    </div>
			                    <div class="form-li">
			                        <input type="text" maxlength="20" placeholder="请输入场景名称" id="pub_tempName">
			                    </div>
			                    <div class="form-li">
			                        <select class="" id="pub_tempType" name="pub_tempType" title="">
			                            <option value="">全部</option>
			                            <option value="0">静态场景</option>
			                            <option value="1">动态场景</option>
			                        </select>
<%--
			                        <i class="pull-icon"></i>
--%>
			                    </div>
			                    <div class="btn-li">
			                        <div class="query-btn green-btn" onclick="publicTempQuery()">
			                            <i class="search-icon"></i>
			                            <span class="btn-text">查询</span>
			                        </div>
			                        <a class="add-btn green-border-btn" href="" id="addPublicTemp">新建</a>
			                    </div>
			                    <div class="screen-items">
			                        <h4 class="items-title">行业</h4>
									<div id="industry_wrapper" class="useAndIndustry rollBak">
										<ul class="items" id="industry">
											<li class="active" number="all"><span>全部行业</span></li>
										</ul>
									</div>
			                    </div>
			                    <div class="screen-items">
			                        <h4 class="items-title">用途</h4>
									<div id="use_wrapper" class="useAndIndustry rollBak">
										<ul class="items" id="use">
											<li class="active" number="all"><span>全部行业</span></li>
										</ul>
									</div>
			                    </div>
			                    
			                </div>
			            </div>
			            <%-- /container-right --%>
			            
			            <%-- container-right --%>
			            <div id="myCondition" class="container-right module-pop-fl">
			                <div class="right-wrap">
			                    <div class="form-li">
			                        <input type="text" maxlength="10" placeholder="请输入场景ID" id="my_tempId" onkeyup="javascript:numberControl($(this))">
			                    </div>
			                    <div class="form-li">
			                        <input type="text" maxlength="20" placeholder="请输入场景名称" id="my_tempName">
			                    </div>
			                    <div class="form-li">
			                        <select class="" id="my_tempType" name="my_tempType" title="">
			                            <option value="">全部</option>
			                            <option value="0">静态场景</option>
			                            <option value="1">动态场景</option>
			                        </select>
<%--
			                        <i class="pull-icon"></i>
--%>
			                    </div>
			                    <div class="btn-li">
			                        <div class="query-btn green-btn" onclick="myTempQuery()">
			                            <i class="search-icon"></i>
			                            <span class="btn-text">查询</span>
			                        </div>
			                        <a class="add-btn green-border-btn" href="" id="addMyTemp">新建</a>
			                    </div>
			                </div>
			            </div>
			            <%-- /container-right --%>
			            
			        </div>
			        <%-- /module-pop-container --%>
			    </div>
			    <%-- /fx-module-pop --%>
							
			</form>
			<%-- foot结束 --%>
		</div>
		<div id="modify" title="富信主题"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
		</div>
		
		<%-- 预览弹层  --%>
		<div id="myView" title="预览" style="display: none;">
		<div id="phone_background">
			<div id="cust_preview_outer" class="rollBak">
				<div id="cust_preview"></div>
			</div>
		</div>
		</div>
		
		<%--富信内容预览DIV --%>
		<div id="tempView" title="富信内容" style="display:none;overflow: auto;">
			<input type="hidden" id="tmmsId" value=""/>
			<div style="float: left;width:250px;margin-left: 30px;display: inline;">
				<div id="mobile" style="width:240px;height:470px;position: absolute;background: url('<%=commonPath %>/common/img/iphone5.jpg') no-repeat">
				<center>
				<div id="pers" style='position:relative;margin-top:90px;border:1px #09F solid;text-align:left;display:none;width:180px;height:13px;background-color:#FFFFFF'>
				
				<div id="showtime" style="position:absolute;width:140px;height:13px;text-align: center;"></div>
				
				<div id='chart' style='position:absolute;left:0; background-color:#00ffdd;width:0;height:13px;text-align:right;padding-top:0px;'>
				</div>
				</div>
				</center>
				
				<div id="screen" style="position:relative;width:189px;height:240px;background:#ffffff;margin-left:23px;margin-top:95px;overflow: auto;padding:2px;word-break: break-all;">
				</div>
				<center>
				<table>
				<tr>
				  <td>
				     <label id="pointer" style="vertical-align: bottom"></label>
				     <label id="nextpage" style="vertical-align: bottom"></label>
				  </td>
				</tr>
				<tr align="center">
					<td>
					   <label id="currentpage" style="vertical-align: bottom"></label>
					</td>
				</tr>
				</table>				
				</center>
				</div>
			</div>
			<div id="inputParamCnt1" style="margin-top: 10px;margin-left: 280px; ">
			</div>			
		</div>
		<div class="clear"></div>
		<%--手动加的遮罩层--%>
		<div id="mask" class="mask"></div>
		<script>
			var serverName = "<%=serverName%>";
		</script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/rms/samemms/js/template.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/rms/samemms/js/tempChoose.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
			var pageSource = <%=pageSource %>;
			if(pageSource==1){
				$(".close-icon").click(function(){
					$(window.parent.document).find("#popup_div").css({"display":"none"});
	 				$(window.parent.document).find("#tempFrames").css({"display":"none"});
				});
			}else{
				$(".close-icon").click(function(){
					parent.closeDialog();
				});
			}
		});
		</script>
	</body>
</html>
