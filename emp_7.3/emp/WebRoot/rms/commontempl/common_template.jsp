<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.rms.vo.LfTemplateVo"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	 
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);
	//当前登录企业编号
	String loginOrgcode = lfSysuser.getCorpCode();
    request.setAttribute("loginOrgcode", loginOrgcode);
	LfTemplateVo tmpl = (LfTemplateVo)request.getAttribute("lfTemplate");
	long auditstatus =  tmpl.getAuditstatus()==null?-2:tmpl.getAuditstatus();
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("commTpl");
	menuCode = menuCode==null?"0-0-0":menuCode;
	// 行业-用途类型
	String type = request.getParameter("type");
	// 行业ID
	String InduOrUseId = request.getParameter("InduOrUseId");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>公共场景管理界面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/template.css?V=<%=StaticValue.getJspImpVersion()%>"/>
<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
<%if(StaticValue.ZH_HK.equals(langName)){%>	
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
<%}%>

<style type="text/css">
	html{
		margin-bottom:150px;
	}
	#content tfoot tr td #p_goto{
		display: none;
	}
	.sendBt-hover{
		width: 70px;
		height: 30px;
		border: 0px;
		cursor: pointer;
		color: white;
		font-family: "微软雅黑";
		font-size: 12px;
		background-color: #15c58b !important;
		border-radius:2px;
	}
</style>
</head>
 <%@include file="/common/common.jsp" %>
<body>
<%-- ---<%=skin %> --%>
	<input type="hidden" id="skin" value="<%=skin%>" />  
	<%-- 当前位置 --%>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
	<%-- <%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %> --%>
    <%-- <div class="nav-bar">
         <a href="javascript:void(0);">企业富信 ></a>
         <a href="javascript:void(0);">富信应用 ></a>
         <span>公共场景</span>
     </div>
     <div class="top-line"></div> --%>
	<div id="container" class="container" >
	<input type="hidden" id="pathUrl" value="<%=path%>" />
	<input type="hidden" id="type" value="<%=type%>" />
	<input type="hidden" id="InduOrUseId" value="<%=InduOrUseId%>" />
	<form name="pageForm" action="rms_commTpl.htm?method=find" method="post" id="pageForm">
		<div class="header">
			<div class="shop">
				<%--<c:if test="${loginSysuser.userCode eq '100000' }" >--%>
					<div class="status">
                        <input type="hidden" id="hiddenStatus" value="${templStatus}"/>
						<select id="templStatus" name="templStatus" class="pulldown">
							<option value="-2" ><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
							<option value="-1" ><emp:message key="rms_fxapp_myscene_noapproval" defVal="未审批" fileName="rms"/></option>
							<option value="1"   ><emp:message key="rms_fxapp_myscene_approvaled" defVal="审批通过" fileName="rms"/></option>
							<option value="2"   ><emp:message key="rms_fxapp_myscene_nopass" defVal="审批不通过" fileName="rms"/></option>
							<option value="3"   ><emp:message key="rms_fxapp_myscene_approvaling" defVal="审批中" fileName="rms"/></option>
							<option value="4"   ><emp:message key="rms_fxapp_myscene_disabled" defVal="已禁用" fileName="rms"/></option>
						</select>
					</div>
					<div class="search">
						<input id="templName" placeholder="<emp:message key="rms_fxapp_pubscene_condition" defVal="输入场景名称/场景ID" fileName="rms"/>" value="${lfTemplate.tmName}">
						<span  id="mysearch" class ="mysearch" onclick="searchTempl()" ></span>
					</div>
				<%--</c:if>--%>
				<%--<c:if test="${loginSysuser.userCode != '100000' }" >
				<div class="search search1">
						<input id="templName" placeholder="<emp:message key="rms_fxapp_pubscene_condition" defVal="输入场景名称/场景ID" fileName="rms"/>" value="${lfTemplate.tmName}">
						<span  id="mysearch" class ="mysearch" onclick="searchTempl()" ></span>
					</div>
				</c:if>--%>
				<%--<c:if test="${loginSysuser.userCode eq '100000' }" >--%>
					<div class="service" onclick="doAdd()"><emp:message key="rms_fxapp_myscene_newscene" defVal="创建新场景" fileName="rms"/></div>
				<%--</c:if>--%>
				<br/>
			</div>
			<div class="nav-all">
				<div class="industry-dv">
     	    	 	<div class= "type"><emp:message key="rms_fxapp_pubscene_trades" defVal="行业" fileName="rms"/></div>
     	    	 	<div class="vertical-line"></div>
     	    	 	<div class="indu-ul-dv">
	     	    	 	<ul id="industry-ul" class="industry-ul">
	     	    	 		<li id="-1">
	     	    	 			 <a href="javascript:void(0);" class="active"  mytype="0" value="-1" onclick="searchByInduOrUse(this)"><emp:message key="rms_fxapp_pubscene_alltrades" defVal="全部行业" fileName="rms"/></a>
	     	    	 		</li>
	     	    	 		 <c:forEach items="${industryList}" var="industry" varStatus="status">
	     	    	 		 <li id="${industry.id}">
								 <a href="javascript:void(0);"  mytype="0"  value="${industry.id}" onclick="searchByInduOrUse(this)">${industry.name}</a>
							 </li>
	     	    	 		 </c:forEach>
	     	    	 		 
	     	    	 	</ul>
		     	   		 <div class="more-btn" style="cursor: pointer;" onclick="showHideCode()"><label style="font-size: 14px;font-family: '微软雅黑';"><emp:message key="common_more" defVal="更多" fileName="common"/></label><span id="industry_more" class="down-arrow-default"></span></div>
     	    	 	</div>
     	    	 </div>
     	    	 <div class="use-dv">
     	    	 	<div class= "type"><emp:message key="rms_fxapp_pubscene_use" defVal="用途" fileName="rms"/></div>
     	    	 	<div class="vertical-line"></div>
     	    	 	<div class="use-ul-dv">
	     	    	 	<ul id="use-ul" class="use-ul">
	     	    	 		<li id="-2">
	     	    	 			<a href="javascript:void(0);" class="active"  mytype="1" value="-2" onclick="searchByInduOrUse(this)"><emp:message key="rms_fxapp_pubscene_alluse" defVal="全部用途" fileName="rms"/></a>
	     	    	 		</li>
	     	    	 		  <c:forEach items="${useList}" var="use" varStatus="status">
	     	    	 		  <li id="${use.id}">
								 <a  href="javascript:void(0);" mytype="1"  value="${use.id}" onclick="searchByInduOrUse(this)">${use.name}</a>
							  </li>
	     	    	 		  </c:forEach>
	     	    	 	</ul>
	     	    	<div class="more-btn" onclick="showHideUse()" style="cursor: pointer;" ><label style="font-size: 14px;font-family: '微软雅黑';"><emp:message key="common_more" defVal="更多" fileName="common"/></label><span id="use_more" class="down-arrow-default"></span></div>
     	    	 	</div>
     	    	 </div>
			</div>
         	</div>
			<div></div>
		<%--                              content    --%>
		<div id="content" class="content rContent">
			<table align="right" style="margin-right: 365px;">
				<tbody>
					<ul class="tagul" style="width: 100%;padding-left: 0px;" >
						<div class="con_li">
							<c:set var="colNum" value="4"></c:set>
							<%--<c:if test="${loginSysuser.userCode eq '100000' }" >--%>
							<c:set var="colNum" value="3"></c:set>
								<li>
									<div class="tag" onclick="doAdd()">
										<a href="javascript:void(0);" id="add" class="append"></a>
										<p class="txt_add"><emp:message key="rms_fxapp_myscene_newscene" defVal="创建新场景" fileName="rms"/></p>
									</div>
								</li>		 
							<%--</c:if>--%>
							<c:forEach items="${commonTemList}" var="bean" varStatus="status">
								 
								<c:if test="${status.index % 5 == colNum}">
									<li style="margin-right: 0px">
								</c:if>
								<c:if test="${status.index % 5 != colNum}">
									<li>
								</c:if>
								<div class="tag">
									<div class="bkimg">
										<div class="first-frame">
											<jsp:include page="${basePath }/${fn:replace(bean.tmMsg, 'fuxin.rms', 'firstframe.jsp')}"></jsp:include> 
										</div>
										<div class="mennu" style="float: right;display: none;">
											<ul class="menu_ul" >
												<li title="<emp:message key="common_preview" defVal="预览" fileName="common"/>">
													<a><img tmid="${bean.tmid}" urlMsg="${bean.tmMsg}" tmName="${bean.tmName}" onclick="doPreview(this)" src="<%=basePath%>/rms/mbgl/image/preview_icon.png"></a>
												</li>
												<li>
												<a><img src="<%=basePath%>/rms/mbgl/image/more_icon.png"  onmouseover="showDetail(this,'${bean.tmid}');" onmouseout="hideDetail(this,'${bean.tmid}');"></a>
													<%--详情  --%>
													<div id="templDetail_${bean.tmid}" class="templDetail">
													    <div class="detail_inner">
														    <div>
														    	<font ><emp:message key="rms_fxapp_myscene_fxtheme" defVal="富信主题：" fileName="rms"/>${bean.tmName}</font>
														    </div>
														    <div clss="line1">
														     <font clss="line1"></font>
														    </div>
													    	<div class="fontcol">
														    	<font >ID:${bean.sptemplid == 0 ?"-":bean.sptemplid}</font>
														    </div>
													    	<div class="fontcol">
														    	<font ><emp:message key="rms_fxapp_myscene_usecounts" defVal="使用次数：" fileName="rms"/>${bean.usecount}次</font>
														    </div>
													    	<div class="fontcol">
														    	<font ><emp:message key="rms_fxapp_myscene_capacity" defVal="容量：" fileName="rms"/>${bean.degreeSize}KB</font>
														    </div>
													    	<div class="fontcol">
														    	<font ><emp:message key="rms_fxapp_myscene_level" defVal="档位：" fileName="rms"/>${bean.degree}</font>
														    </div>
													    	<div class="fontcol">
														    	<font >
														    	<emp:message key="rms_fxapp_myscene_scenetype" defVal="场景类型：" fileName="rms"/>
														    	<c:if test="${bean.dsflag ==1}">
														    		<emp:message key="rms_fxapp_myscene_commds" defVal="通用动态场景" fileName="rms"/>
														    	</c:if>
														    	<c:if test="${bean.dsflag ==0}">
														    		<emp:message key="rms_fxapp_myscene_commss" defVal="通用静态场景" fileName="rms"/>
														    	</c:if>
														    	
														    	</font>
														    </div>
													    	<div class="fontcol">
														    	<font ><emp:message key="rms_fxapp_myscene_creator" defVal="创建人：" fileName="rms"/>${bean.name}</font>
														    </div>
													    	<div class="fontcol">
														    	<font ><emp:message key="rms_fxapp_myscene_organ" defVal="所属机构：" fileName="rms"/>${bean.depName}</font>
														    </div>
													    	<div class="fontcol">
																<font ><emp:message key="rms_fxapp_myscene_createtime" defVal="创建时间：" fileName="rms"/>${fn:substring(bean.addtime, 0, 19)}</font>
														    </div>
													    	<div class="fontcol">
														    	<font >
															    	<emp:message key="rms_fxapp_myscene_scenestate" defVal="场景状态：" fileName="rms"/>
															    	<c:if test="${bean.tmState ==1}">
															    		<emp:message key="rms_fxapp_pubscene_ysx" defVal="已上线" fileName="rms"/>
															    	</c:if>
															    	<c:if test="${bean.tmState ==0}">
															    		<emp:message key="rms_fxapp_pubscene_yxx" defVal="已下线" fileName="rms"/>
															    	</c:if>
															    	<c:if test="${bean.tmState ==2}">
															    		<emp:message key="rms_fxapp_myscene_draft" defVal="草稿" fileName="rms"/>
															    	</c:if>
														    	</font>
														    </div>
													    	<div class="fontcol">
														    	<font >
														    	<emp:message key="rms_fxapp_myscene_optstate" defVal="运营商状态：" fileName="rms"/>
														    	<c:if test="${bean.auditstatus == -1}">
															    		<emp:message key="rms_fxapp_myscene_noapproval" defVal="未审批" fileName="rms"/>
														    	</c:if>
														    	<c:if test="${bean.auditstatus == 0}">
														    			<emp:message key="rms_fxapp_pubscene_wxsp" defVal="无需审批" fileName="rms"/>
														    	</c:if>
														    	<c:if test="${bean.auditstatus == 1}">
														    			<emp:message key="rms_fxapp_myscene_approvaled" defVal="审批通过" fileName="rms"/>
														    	</c:if>
														    	<c:if test="${bean.auditstatus == 2}">
														    			<emp:message key="rms_fxapp_myscene_nopass" defVal="审批未通过" fileName="rms"/>
														    	</c:if>
														    	<c:if test="${bean.auditstatus == 3}">
														    			<emp:message key="rms_fxapp_myscene_approvaling" defVal="审批中" fileName="rms"/>
														    	</c:if>
														    	<c:if test="${bean.auditstatus == 4}">
														    			<emp:message key="rms_fxapp_myscene_disabled" defVal="已禁用" fileName="rms"/>
														    	</c:if>
														    	</font>
														    </div>
													    </div>
													   
													    
													</div>
												</li>
												<%-- <li><a><img onclick="del('<%=path %>','${bean.tmid}');" src="<%=basePath%>/rms/mbgl/image/delete_icon.png"></a>
												</li> --%>
											</ul>
											<c:if test="${bean.tmState ==1 && bean.auditstatus == 1}">
												<c:choose>
													<c:when test="${bean.isShortTemp==0}">
														<%-- <div class="shortScene" onclick="toShortScene('${bean.tmid}','${bean.tmName}');">
															<font>设置为快捷场景</font>
														</div> --%>
														<input type="button" class="shortScene" onclick="toShortScene('${bean.tmid}','${bean.tmName}');" value="<emp:message key="rms_fxapp_myscene_setshortscene" defVal="设置为快捷场景" fileName="rms"/>">
													</c:when>
													<c:otherwise>
														<%-- <div class="deleteshortScene" onclick="deleteShortScene('${bean.tmid}','${bean.tmName}');">
															<font>取消快捷场景</font>
														</div> --%>
														<input type="button" class="deleteshortScene" id="deleteshortScene_${bean.tmid}" onclick="deleteShortScene('${bean.tmid}','${bean.tmName}');" value="<emp:message key="rms_fxapp_myscene_cancelshortscene" defVal="取消快捷场景" fileName="rms"/>">
													</c:otherwise>
												</c:choose>
												
											</c:if>
										</div>
									</div>
									<div class="theme">
										<c:choose>
											<c:when test="${fn:length(bean.tmName) > 16}"><font>${fn:substring(bean.tmName, 0, 16)}...</font></c:when>
											<c:otherwise><font>${bean.tmName}</font></c:otherwise>
										</c:choose>
									</div>
									<div class="theme_phone">
										<div class="tmid"><font>ID:${bean.sptemplid == 0 ?"-":bean.sptemplid}</font></div>
									    <div class="download"> 
										    <c:if test="${bean.dsflag==1}">
												<a title="<emp:message key="rms_fxapp_myscene_downloadtelfile" defVal="手机号码文件下载" fileName="rms"/>" href="javascript:void(0);" onclick="downLoadPhoneFile('${bean.tmMsg}','${bean.tmid}');"><img src="<%=commonPath%>/rms/mbgl/image/download_icon.png"></a>
											</c:if> 
										</div>
									</div>	
									<div class="line"></div>	
									<div class="carrier_state">
										<%-- TM_STATE 模板状态：0-已禁用、1-已启用、2-草稿 --%>
										<c:if test="${bean.tmState ==2}">
										 <%-- 模板状态 =2：草稿 --%>
										 	<div class="carrier"> 
												<input class="editBt" onclick="doEdit('${bean.tmid}','${bean.tmMsg}','edit','${bean.industryid}','${bean.useid}')" type="button" value="<emp:message key="common_edit" defVal="编辑" fileName="common"/>"> 
											</div>
										</c:if>
										<%-- 模板状态  tmState ==1： 已启用；运营商审核状态 auditstatus ==1：审核通过 --%>
										<c:if test="${bean.tmState ==1}">
										 	<div class="carrier"> 
											<c:choose>
												<%--未审批 --%>
												<c:when  test="${bean.auditstatus == -1}">
													<input class="unSendBt"  type="button" value="<emp:message key="rms_fxapp_myscene_noapproval" defVal="未审批" fileName="rms"/>">
												</c:when>
												<%--审批通过 --%>
												<c:when  test="${bean.auditstatus == 1}">
													<input class="sendBt" onclick="doCopy('${bean.tmid}','${bean.tmMsg}','copy','${bean.industryid}','${bean.useid}','${bean.usecount}');" type="button" value="<emp:message key="rms_fxapp_pubscene_ljbj" defVal="立即编辑" fileName="rms"/>"> 
												</c:when >
												<%--审批未通过  --%>
												<c:when  test="${bean.auditstatus == 2}">
													<input class="unSendBt"  type="button" value="<emp:message key="rms_fxapp_myscene_nopass" defVal="审批未通过" fileName="rms"/>">
												</c:when>
												<%--审批中  --%>
												<c:when  test="${bean.auditstatus == 3}">
													<input class="unSendBt"  type="button" value="<emp:message key="rms_fxapp_myscene_approvaling" defVal="审批中" fileName="rms"/>">
												</c:when>
												<%--已禁用--%>
												<c:when  test="${bean.auditstatus == 4}">
													<input class="unSendBt"  type="button" value="<emp:message key="rms_fxapp_myscene_disabled" defVal="已禁用" fileName="rms"/> ">
												</c:when>
											</c:choose>
											</div>
											<c:if test="${loginSysuser.userCode eq '100001' }" >
											<div class="state">
													<div class="open-close">
														 <c:if test="${bean.tmState == 1}">
															<a title="<emp:message key="rms_fxapp_myscene_enabled" defVal="已启用" fileName="rms"/>" href="javascript:void(0);"onclick="chState(this,'${bean.tmid}',0);"><img src="<%=commonPath%>/rms/mbgl/image/close.png"/></a>
														 </c:if>
														 <c:if test="${bean.tmState == 0}">
															<a title="<emp:message key="rms_fxapp_myscene_disabled" defVal="已禁用" fileName="rms"/>"  href="javascript:void(0);"onclick="chState(this,'${bean.tmid}',1);"><img src="<%=commonPath%>/rms/mbgl/image/open.png"/></a>
														 </c:if>
													</div>
											</div>	
										</c:if>
										</c:if>
										
										<%-- 模板状态  tmState ==0： 已禁用 --%>
										<c:if test="${bean.tmState ==0}">
										 	<div class="carrier"> 
											<c:choose>
												<%--未审批 --%>
												<c:when  test="${bean.auditstatus == -1}">
													<input class="forbidden"  type="button" value="<emp:message key="rms_fxapp_myscene_noapproval" defVal="未审批" fileName="rms"/>">
												</c:when>
												<%--审批通过 --%>
												<c:when  test="${bean.auditstatus == 1}">
													<input class="forbidden" type="button" value="<emp:message key="rms_fxapp_pubscene_ljbj" defVal="立即编辑" fileName="rms"/>"> 
												</c:when >
												<%--审批未通过  --%>
												<c:when  test="${bean.auditstatus == 2}">
													<input class="forbidden"  type="button" value="<emp:message key="rms_fxapp_myscene_nopass" defVal="审批未通过" fileName="rms"/>">
												</c:when>
												<%--审批中  --%>
												<c:when  test="${bean.auditstatus == 3}">
													<input class="forbidden"  type="button" value="<emp:message key="rms_fxapp_myscene_approvaling" defVal="审批中" fileName="rms"/>">
												</c:when>
												<%--已禁用--%>
												<c:when  test="${bean.auditstatus == 4}">
													<input class="forbidden"  type="button" value="<emp:message key="rms_fxapp_myscene_disabled" defVal="已禁用" fileName="rms"/>">
												</c:when>
											</c:choose>
											</div>
											<div class="state">
													<div class="open-close">
														 <c:if test="${bean.tmState == 1}">
															<a title="<emp:message key="rms_fxapp_myscene_close" defVal="禁用" fileName="rms"/>"  href="javascript:void(0);"onclick="chState(this,'${bean.tmid}',0);"><img src="<%=commonPath%>/rms/mbgl/image/close.png"/></a>
														 </c:if>
														 <c:if test="${bean.tmState == 0}">
															<a title="<emp:message key="rms_fxapp_myscene_open" defVal="启用" fileName="rms"/>"  href="javascript:void(0);"onclick="chState(this,'${bean.tmid}',1);"><img src="<%=commonPath%>/rms/mbgl/image/open.png"/></a>
														 </c:if>
													</div>
												</div>	
										</c:if>
										
									</div>
								</div>
							</li>
						<c:if test="${status.index % 5 == colNum}">
						<div style="clear: both;"></div>
						</div>
						<div class="con_li">
						</c:if>
						</c:forEach>
						</div>
					</ul>
					<div style="clear: both;"></div>
				</tbody>
					<%--tfoot start  --%>
					<tfoot>
						<tr>
							<td style="width:100%" align="right">
								<div id="pageInfo"  align="right"></div>
							</td>
						</tr>
					</tfoot>
					<%--tfoot end  --%>
			</table>
		</div>
		</form>
		</div>
	</div>
	
	<%-- 预览弹层  --%>
	<div id="fullbg"></div>
		<div id="dialog">
			<p class="close">
				<a href="javascript:void(0);" onclick="closeBg();" class="close-dailog"></a>
			</p>
			<div class ="preview">
				<div id ="cust_preview" class="cust_preview"></div>
				<%-- <div class="pre-next-btn">
					<div class="previous"></div>
					<div class="next"></div>
				</div> --%>
			</div>
		</div>

	
	<div id="industryUse" style="display: none;">
		<div>
			<span><font><emp:message key="rms_fxapp_myscene_tradesname" defVal="行业名称：" fileName="rms"/></font><input type="text" id="InUseName">
			</span> <span><a href="javascript:void(0);" onclick="addInduUse()"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></a>
			</span>
		</div>
		<div>
			<table id="induStryUseTb">
				<thead>
					<tr>
						<th><emp:message key="rms_fxapp_myscene_tradesname2" defVal="行业名称" fileName="rms"/></th>
						<th><emp:message key="rms_fxapp_dwtjbb_operation" defVal="操作" fileName="rms"/></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><emp:message key="rms_fxapp_pubscene_yhjr" defVal="银行金融" fileName="rms"/></td>
						<td><a href="javascript:void(0);" onclick="rename()"><emp:message key="rms_fxapp_pubscene_rename" defVal="重命名" fileName="rms"/></a>
							<a href="javascript:void(0);" onclick="deleteInUse()"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<%--script 脚本文件开始  --%>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/rms/commontempl/js/jquery-cookie.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	
	<%--script 脚本文件结束  --%>



	<script type="text/javascript">
	   //全局url
	   var url = '<%=basePath%>';
	   
	   $(document).ready(function() {
			//公共翻页初始化
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		    //根据li个数来给div 增加高度
		    var industryCount = $(".list li").length;
		    var useCount = $(".list_0 li").length;
		    //li 个数%8==0 ，div 高度增加30px;
		    $(".header").height($(".header").height() +Math.ceil(industryCount/8)*10 +  Math.ceil(useCount/8)*10);
		    
		    //第一帧等比缩放
		    //IE版本小于9
		    if(navigator.appName == "Microsoft Internet Explorer" && parseInt(navigator.appVersion.split(";")[1].replace(/[ ]/g, "").replace("MSIE","")) < 9){
	            	zoomIE();
				}else{//
					zoom();
				}
				
		 	//修改 行业 -用途 div 高度
			var indudv_height = '${indudvHeight}';
			var usedv_height = '${usedvHeight}';
			
			var $indudv_height = $(".indu-ul-dv").height();
			var $usedv_height =  $(".use-ul-dv").height();

			if(parseInt(indudv_height) != $indudv_height ){
				$(".indu-ul-dv").height(indudv_height);
			}
			if(parseInt(usedv_height) != $usedv_height ){
				$(".use-ul-dv").height(usedv_height);
			}

			// 新增审核状态选择之后保持选择
			$("#templStatus option").each(function(){
			    if($(this).val() == $("#hiddenStatus").val()) {
			        $(this).attr("selected", "selected");
                }
            });

				
		});

       var induOrUseType = $("#type").val();
       var induOrUseId = $("#InduOrUseId").val();
       if(induOrUseType === "0"){
           //行业
           $(".use-ul").find("li").find("a").removeClass("selected");
           $(".industry-ul").find("li").find("a[value='"+ induOrUseId +"']").addClass("selected");
       }else{
           //用途
           $(".industry-ul").find("li").find("a").removeClass("selected");
           $(".use-ul").find("li").find("a[value='"+ induOrUseId +"']").addClass("selected");
       }

		//根据模板名称或模板ID查询
		function searchTempl(){
			$("#industryUse").dialog('option','title',getJsLocaleMessage("rms","rms_pubscene_preview"));
			$("#industryUse").dialog("open");
			//模板类型
			var templStatus = $("#templStatus option:selected").val();
			if(typeof(templStatus)=="undefined"){//解决非10000账号为空
				templStatus ="";
			}
			//模板名称
			var templName = $("#templName").val();
			
			window.location.href = url + "/rms_commTpl.htm?method=find&templName="+templName+"&templStatus="+templStatus;
		}
		
		//根据行业-用途查询
		function searchByInduOrUse(obj){
			//行业或者用途
			var type = $(obj).attr("mytype");
			//行业或者用途 ID
			var InduOrUseId = $(obj).attr("value");
			//模板类型
			var templStatus = $("#templStatus option:selected").val();
			if(typeof(templStatus)=="undefined"){//解决非10000账号为空
				templStatus ="";
			}
			//模板名称
			var templName = $("#templName").val();
			
		//	$(".current1").removeClass();
		//	$(obj).addClass("current1");
			var indudv_height =$(".indu-ul-dv").height();
			var usedv_height = $(".use-ul-dv").height();
			
			window.location.href = url + "/rms_commTpl.htm?method=find&type="+type+"&InduOrUseId="+InduOrUseId
			+"&templName="+templName+"&templStatus="+templStatus+"&indudv_height="+indudv_height+"&usedv_height="+usedv_height;
			//设置 行业-用途 div 高度
			$(".indu-ul-dv").height(indudv_height);
			$(".use-ul-dv").height(usedv_height);
 		
		}
		
		//新建模板
		function doAdd(){
		 //1静态彩信  2动态彩信  3新增
		 var lgcorpcode = '${loginOrgcode}';
		 window.location.href = url+"/mbgl_mytemplate.htm?method=doAdd&type=3&lgcorpcode="+lgcorpcode+"&source=commontemplate";
		}
		
		//编辑
		function doEdit(tmId,tmUrl,opType,industryid,useid){
		 	 var pathUrl = $("#pathUrl").val();
   		     window.location.href=pathUrl+"/mbgl_mytemplate.htm?method=doCopy&tmId="+tmId+"&opType="+opType+"&tmUrl="+tmUrl+"&industryId="+industryid+"&useId="+useid+"&source=commontemplate";
		}
		//立即使用-复制
		function doCopy(tmId,tmUrl,opType,industryid,useid,usecount){
		 	 var pathUrl = $("#pathUrl").val();
		 	 usecount = parseInt(usecount) + 1; //点击立即使用就增加1次
             $.post(pathUrl+"/rms_commTpl.htm?method=updateUseCount",{tmid:tmId,usecount:usecount},function(result){
	            if(result != null && result == "success")
	            {
					 window.location.href=pathUrl+"/mbgl_mytemplate.htm?method=doCopy&tmId="+tmId+"&opType="+opType+"&tmUrl="+tmUrl+"&industryId="+industryid+"&useId="+useid;
	            }
	            else
	            {
	            	 alert(getJsLocaleMessage("rms","rms_myscene_runexception"));
	            }
	        });
		 	 
		}
		//删除
		function del(path,i){
			var flag = confirm(getJsLocaleMessage("rms","rms_myscene_confirmdel1"));
			if(flag){
				    $.post("mbgl_mytemplate.htm?method=delete",
				    {ids:i,lgcorpcode:$("#lgcorpcode").val()},
				    function(result){
						if(result>0)
						{
							alert(getJsLocaleMessage("rms","rms_myscene_successdel"));
							var url = path+'/mbgl_mytemplate.htm';
							var conditionUrl = "";
							if(url.indexOf("?")>-1)
							{
								conditionUrl="&";
							}else
							{
								conditionUrl="?";
							}
							$("#hiddenValueDiv").find(" input").each(function(){
								conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
							});
							location.href=url+conditionUrl;	
						}else{
							alert(getJsLocaleMessage("rms","rms_myscene_faildel"));
						}
				});
			}
		}
		//启用-禁用
		function chState(obj,tmid,t)
		{
			
			var skin = $("#skin").val();
			//模板状态
			//var t =$(obj).val();
			//模板自增ID
			//var tmid2 = $(obj).attr("tmid");
		    //if(confirm("确定修改状态吗？"))
		    var tempId = tmid;
		    if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qdxgztm")))
		    {
		    	 var pathUrl = $("#pathUrl").val();
		         $.post(pathUrl+"/mbgl_mytemplate.htm?method=changeState",{id:tmid,t:t},function(result){
		            if(result != null && result == "true")
		            {
		               //alert("修改成功！");
		             alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_xgcg"));
		             if(t == 0){
		             //location.href=url+conditionUrl;	
						 if(skin.indexOf("frame4.0") != -1){
                     	 var _secondMenu =$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tempId).parent().parent();
	                        var len = _secondMenu.children('li').length;
                        	if(len<2){
	                       	 	var width = _secondMenu.parent().parent().width()-164;
	                      	 	_secondMenu.parent().parent().css({"width":width});
	                        	_secondMenu.parent().remove();
	                        }else{
	                        	$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tempId).parent().remove();
	                        }
	                    }else if(skin.indexOf("frame2.5") != -1){
	                    	var _secondMenu =$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tempId).parent().parent();
                        	var len = _secondMenu.children('li').length;
	                        if(len<2){
                        		_secondMenu.parent().remove();
	                        }else{
	                        	$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tempId).parent().remove();
	                        }
	                    }else{
	                        $(window.parent.document).find("#m"+tempId).remove();
	                        window.parent.document.getElementById('leftIframe').contentWindow.location.reload(true);
	                    }
		             }
		              	 //$("#pageForm").find("#search").click();
		                 window.location.reload();
		            }
		            else
		            {
		            	 //alert("修改失败！");
		            	 alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_xgsb"));
		            }
		        });
		    }
		}
		
		//预览
		function doPreview(obj)
		{		
			    var msg = $(obj).attr("urlMsg");
			    var tmName = $(obj).attr("tmName");
			    var tmId = $(obj).attr("tmid");
			    $.post(url+"/rms_commTpl.htm?method=getTmMsg",{tmUrl:msg,tmName:tmName,tmId:tmId},function(result){
				if (result != null && result != "null" && result != "")
				{
					 document.getElementById("cust_preview").innerHTML = result;
					  //计算图参位置进行比例缩放
						 var _widthProportion = 236/385,
						 		_addModuleEL = $("#cust_preview").find(".J-add-module");
						 		
						 $.each(_addModuleEL, function(key){
						 	var __addModuleELTop = _addModuleEL.eq(key).css("top"),
				    			 __addModuleELLeft = _addModuleEL.eq(key).css("left"),
				    			 __addModuleELWidth = _addModuleEL.eq(key).find(".editor-resize-text").css("width"),
				    			 __addModuleELFontsize = _addModuleEL.eq(key).find(".editor-resize-text").css("font-size"),
				    			 __addModuleELLineHeight = _addModuleEL.eq(key).find(".editor-resize-text").css("line-height");
			    			 
			    			  __addModuleELTop = parseInt(__addModuleELTop);
			    			 __addModuleELLeft = parseInt(__addModuleELLeft);
			    			 __addModuleELWidth= parseInt(__addModuleELWidth);
			    			 __addModuleELFontsize = parseInt(__addModuleELFontsize);
			    			 __addModuleELLineHeight = parseInt(__addModuleELLineHeight);
			    			 
			    			 _addModuleEL.eq(key).css({
			    				 "top": (__addModuleELTop*_widthProportion)+1+"px",
			    				 "left": (__addModuleELLeft*_widthProportion)+1+"px",
			    			 });
			    			 
			    			 _addModuleEL.eq(key).find(".editor-resize-text").css({
			    				 "width": (__addModuleELWidth*_widthProportion)+2+"px",
			    				 "font-size": (__addModuleELFontsize*_widthProportion)+1+"px",
			    				 "line-height": (__addModuleELLineHeight*_widthProportion)+1+"px",
			    			 });
						 });
						  //禁用滚动条
						  $("body").css({
						   "overflow-x":"hidden",
						   "overflow-y":"hidden"
						 });
						 //显示预览框
					     showBg();
				}
				else
				{
		             alert(getJsLocaleMessage("rms","rms_myscene_nofile"));
				}
			});
		}
		//立即发送
		function rmsSend(tmid,sptemplid,usecount){
			var path = $("#pathUrl").val();
			usecount = parseInt(usecount) + 1; //点击立即使用就增加1次
            $.post(path+"/rms_commTpl.htm?method=updateUseCount",{tmid:tmid,usecount:usecount},function(result){
	            if(result != null && result == "success")
	            {
					//window.location.href = path+ "/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmid;
                    window.parent.openNewTab("5100-1000",path+ "/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmid);
	            }
	            else
	            {
	            	 alert(getJsLocaleMessage("rms","rms_myscene_runexception"));
	            }
	        });
			
		}
		//手机号码文件下载
		function downLoadPhoneFile(tmMsg,tmId){
		var path = $("#pathUrl").val();
			download_href(path+"/rms_commTpl.htm?method=downPhoneFile&tmMsg="+tmMsg+"&tmId="+tmId);
		}
	
		//隐藏菜单
		$(".bkimg").mouseover(function() {
			$(this).find(".mennu").show();
		});

		//显示菜单
		$(".bkimg").mouseout(function() {
			$(this).find(".mennu").hide();
		});
		//显示详情
		function showDetail(obj,id){
			$("#templDetail_"+id).show();
		}
		//隐藏详情
		function hideDetail(obj,id){
			$("#templDetail_"+id).hide();
		}
		//设置为快捷场景
		function toShortScene(tmId,tmName){
			var name = tmName;
			var skin =  $("#skin").val();
			var pathUrl = $("#pathUrl").val();
			tmName = encodeURIComponent(tmName);
			tmName = encodeURIComponent(tmName);
			$.ajax({
					type:"POST",
					async: false,
					url :"rms_templateMana.htm?method=addShortTemp&tempId="+tmId+"&tempName="+tmName,
					beforeSend:function () {
	                    page_loading();
	                },
	                complete:function () {
	               	  	page_complete();
	                }, 
	                success:function (result) {
		                if (result == 'true') {
	                        alert(getJsLocaleMessage("rms","rms_myscene_addsuccess"));
	                        if(skin.indexOf("frame4.0") != -1||skin.indexOf("frame2.5") != -1){
	                        	var oneMenu = $(window.parent.document).find("#leftIframe").contents().find("#mod25");
	                        	var secondMenu = oneMenu.children('ul').eq(0).children('li').eq(0).children('p');
	                        	if(secondMenu.text() != getJsLocaleMessage("rms","rms_myscene_myshortscene")){
	                        		var width=oneMenu.children('ul').eq(0).width()+164;
	                        		oneMenu.children('ul').eq(0).css({"width":width});
	                        		var liprependTo = "<li><p class=\"second-nav-title\">"+ getJsLocaleMessage("rms","rms_myscene_myshortscene") +"</p><ul class=\"third-nav-menu\">"+
	                        		"<li onmouseover=\"mouseOver('"+tmId+"')\" onmouseout=\"mouseOut('"+tmId+"')\" title='"+name+"'><a id='ak"+tmId+"' onclick=\"doOpen('/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmId+"','"+tmId+"','"+name+"')\">"+name+"</a>"+
	                        		"<input type=\"button\" id=\"closeLi"+tmId+"\" value=\"x\" style=\"border: 0;width: 20px;height:20px;position: absolute;left: 130px;cursor: pointer;background-color: #f1f1f9;display:none;\" onclick=\"deleteLi('"+tmId+"','"+name+"');\"></li></ul></li>";
                        		 	oneMenu.children('ul').eq(0).prepend(liprependTo);
	                        	}else{
	                        		var liprepend = "<li  onmouseover=\"mouseOver('"+tmId+"')\" onmouseout=\"mouseOut('"+tmId+"')\" title='"+name+"'><a id='ak"+tmId+"' onclick=\"doOpen('/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmId+"','"+tmId+"','"+name+"')\">"+name+"</a>"+
	                        		"<input type=\"button\" id=\"closeLi"+tmId+"\" value=\"x\" style=\"border: 0;width: 20px;height:20px;position: absolute;left: 130px;cursor: pointer;background-color: #f1f1f9;display:none;\" onclick=\"deleteLi('"+tmId+"','"+name+"');\"></li>";
	                        		oneMenu.children('ul').eq(0).children('li').eq(0).children('ul').eq(0).prepend(liprepend);
	                        	}
	                        }else{
		                        window.parent.document.getElementById('leftIframe').contentWindow.location.reload(true);
	                        }
                        	window.location.reload();
	                    }else if(result=='num'){
	                    	alert(getJsLocaleMessage("rms","rms_myscene_scenelimit15"));
	                    }else if(result == 'exist'){
	                    	alert(getJsLocaleMessage("rms","rms_myscene_sceneexist"));
	                    }else{
	                        alert(getJsLocaleMessage("rms","rms_myscene_addfail"));
	                    }
	                },
	                error:function(){
	                	alert(getJsLocaleMessage("rms","rms_myscene_runexception"));
	                }
				});
		}
		//取消快捷场景
		function deleteShortScene(tempId,tmName){
			var skin =  $("#skin").val();
			if(confirm(getJsLocaleMessage("rms","rms_myscene_confirmdel2")+tmName+"?")){
			$.ajax({
				type:"POST",
				url :"rms_templateMana.htm?method=deleteShortTemp&tempId="+tempId,
                success:function (result) {
	                if (result=='true') {
                        alert(getJsLocaleMessage("rms","rms_myscene_successdel"));
                        if(skin.indexOf("frame4.0") != -1||skin.indexOf("frame2.5") != -1){
                       		var _secondMenu =$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tempId).parent().parent();
	                        var len = _secondMenu.children('li').length;
	                        if(len<2){
	                       	 	var width = _secondMenu.parent().parent().width()-164;
	                      	 	_secondMenu.parent().parent().css({"width":width});
	                        	_secondMenu.parent().remove();
	                        }else{
	                        	$(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#ak"+tempId).parent().remove();
	                        }
	                    }else{
	                        $(window.parent.document).find("#m"+tempId).remove();
	                        window.parent.document.getElementById('leftIframe').contentWindow.location.reload(true);
	                    }
                    	window.location.reload();
                    } else {
                        alert(getJsLocaleMessage("rms","rms_myscene_faildel"));
                    }
                }
			});
		}
		}
		
		//IE9以下浏览器等比缩放
		function zoomIE(){
			 //计算图参位置进行比例缩放
			 var _widthProportion = 235/385,
			 		_addModuleEL = $(".bkimg").find(".J-add-module");
			 		
			 $.each(_addModuleEL, function(key){
			 	var __addModuleELTop = _addModuleEL.eq(key).css("top"),
	    			 __addModuleELLeft = _addModuleEL.eq(key).css("left"),
	    			 __addModuleELWidth = _addModuleEL.eq(key).find(".editor-resize-text").css("width"),
	    			 __addModuleELFontsize = _addModuleEL.eq(key).find(".editor-resize-text").css("font-size"),
	    			 __addModuleELLineHeight = _addModuleEL.eq(key).find(".editor-resize-text").css("line-height");
    			 
    			  __addModuleELTop = parseInt(__addModuleELTop);
    			 __addModuleELLeft = parseInt(__addModuleELLeft);
    			 __addModuleELWidth= parseInt(__addModuleELWidth);
    			 __addModuleELFontsize = parseInt(__addModuleELFontsize);
    			 __addModuleELLineHeight = parseInt(__addModuleELLineHeight);
    			 _addModuleEL.eq(key).css({
    				 "top": (__addModuleELTop*_widthProportion)+1+"px",
    				 "left": (__addModuleELLeft*_widthProportion)+1+"px",
    			 });
    			 
    			 _addModuleEL.eq(key).find(".editor-resize-text").css({
    				 "width": (__addModuleELWidth*_widthProportion)+2+"px",
    				 "font-size": (__addModuleELFontsize*_widthProportion)+1+"px",
    				 "line-height": (__addModuleELLineHeight*_widthProportion)+1+"px",
    			 });
			 });
		}
		//非IE9一下浏览器等比缩放
		function zoom(){
			 //计算图参位置进行比例缩放
			 var _widthProportion = 235/385,
			 		_addModuleEL = $(".bkimg").find(".J-add-module");
			 		
			 $.each(_addModuleEL, function(key){
			 	var __addModuleELTop = _addModuleEL.eq(key).css("top"),
	    			 __addModuleELLeft = _addModuleEL.eq(key).css("left");
    			 
    			  __addModuleELTop = parseInt(__addModuleELTop);
    			 __addModuleELLeft = parseInt(__addModuleELLeft);
    			 _addModuleEL.eq(key).css({
    				 "top": parseInt(__addModuleELTop*_widthProportion)+1+"px",
    				 "left": parseInt(__addModuleELLeft*_widthProportion)+1+"px",
    				 "transform-origin": "left top",
    				 "-ms-transform-origin": "left top",
    				 "-moz-transform-origin": "left top",
    				 "-webkit-transform-origin": "left top",
    				 "-o-transform-origin": "left top",
    				 "transform": "scale("+_widthProportion+")",
	    			 "-ms-transform": "scale("+_widthProportion+")",
	    			 "-moz-transform": "scale("+_widthProportion+")",
	    			 "-webkit-transform": "scale("+_widthProportion+")",
	    			 "-o-transform": "scale("+_widthProportion+")"
    			 });
			 });
		}
		
		//显示灰色 jQuery 遮罩层 
		function showBg() {
			var bh = $("body").height()+1000;
			var bw = $("body").width();
			$("#fullbg").css({
				height: bh,
				width:  bw,
				display: "block"
			});
			$("#dialog").show();
		}
		//关闭灰色 jQuery 遮罩 
		function closeBg() {
			$("body").css({
				"overflow-y":"auto"
				});
			$("#fullbg,#dialog").hide();
		}
		
		  function showHideCode(){
		     var Object =	document.getElementById("industry-ul").getElementsByTagName("li");
		     var row = Math.ceil(Object.length / 10);
		     var indu_height = row * 35 ;
		     var temp_height = $(".indu-ul-dv").height();
		     if(temp_height == 30){
		     	$(".indu-ul-dv").height(indu_height);
		     }else{
		     	$(".indu-ul-dv").height(30);
		     }
        }
            function showHideUse(){
			     var Object =	document.getElementById("use-ul").getElementsByTagName("li");
			     var row = Math.ceil(Object.length / 10);
			     var indu_height = row * 35 ;
			     var temp_height = $(".use-ul-dv").height();
			     if(temp_height == 30){
			     	$(".use-ul-dv").height(indu_height);
			     }else{
			     	$(".use-ul-dv").height(30);
			     }
        }
		
		//cookie 记录当前点击行业-用途 类别
		$(function(){
			var pathUrl = "/";
			if(!isIE()){
				pathUrl = "<%=basePath%>/rms/commontempl/";
			}
			
			$(".industry-ul").find("li").click(function(){
				var index = $(this).attr("id");
				$.cookie("current_indu_use", index, {path: pathUrl});
			});
			
			$(".use-ul").find("li").click(function() {
				var index = $(this).attr("id");
				$.cookie("current_indu_use", index, { path:pathUrl});
			});
			
			if ($.cookie("current_indu_use")!= null){
				//获取记录的状态
				$(".industry-ul").find("li").find("a").removeClass("selected");
				$(".use-ul").find("li").find("a").removeClass("selected");
			  	//获取记录的状态
			    var num = $.cookie("current_indu_use");
				//当前下标的元素添加样式
			  	$("#"+num+"").find("a").addClass("selected");
			  }
		 
			 $(".more-btn").bind({
			  	click:function(){
				  	$(this).addClass("selected");
 					var $imgcss = $(this).find("span").attr("class");
 					if($imgcss.indexOf("down") != -1){
 						$(this).find("label").text(getJsLocaleMessage("rms","rms_pubscene_fold"));
 						$(this).find("span").removeClass("down-arrow-default");
 						$(this).find("span").removeClass("down-arrow-selected");
 						$(this).find("span").addClass("up-arrow-selected");
 					}else{
 						 $(this).find("label").text(getJsLocaleMessage("rms","rms_pubscene_more"));
 						$(this).find("span").removeClass("up-arrow-selected");
 						$(this).find("span").addClass("down-arrow-selected");
 					} 
 
				  	var $indudv_height = $(".indu-ul-dv").height();
				  	var $usedv_height = $(".use-ul-dv").height();
				  	
				  	$.cookie("indudv_height",$indudv_height,{ path:pathUrl});
				  	$.cookie("usedv_height",$usedv_height,{ path:pathUrl});
				  	
			  	},
			  	 mouseover:function(){
			  	  var $imgcss1 = $(this).find("span").attr("class");
			  	  if($imgcss1.indexOf("down") != -1){
				  		$(this).addClass("selected");
				  		$(this).find("span").removeClass("down-arrow-default");
				  		$(this).find("span").removeClass("up-arrow-selected");
				  		$(this).find("span").addClass("down-arrow-selected");
			  	  }else{
			  	  		$(this).addClass("selected");
				  		$(this).find("span").removeClass("up-arrow-default");
				  		$(this).find("span").removeClass("down-arrow-selected");
				  		$(this).find("span").addClass("up-arrow-selected");
			  	  }
			  	}, 
			 	
			 	
			 });
			 $(".more-btn").mouseout(function(){
			 var $imgcss1 = $(this).find("span").attr("class");
				if($imgcss1.indexOf("down") != -1){
					$(this).removeClass("selected");
				 	$(this).find("span").removeClass("up-arrow-default");
				 	$(this).find("span").removeClass("down-arrow-selected");
				 	$(this).find("span").addClass("down-arrow-default");		
				}else{
					$(this).removeClass("selected");
				 	$(this).find("span").removeClass("up-arrow-selected");
				 	$(this).find("span").removeClass("down-arrow-selected");
				 	$(this).find("span").addClass("up-arrow-default");				
				}		 
			 	
			 }); 
			    
		 });
		 
		function isIE() { //ie?
			 if (!!window.ActiveXObject || "ActiveXObject" in window){
			    return true;
			 }else{
			    return false;
			 }
		 }
		 
		 //----鼠标移进移出样式修改
		  $(".mysearch").mouseover(function(){
		 	 $(this).addClass("mysearch-hover");
		 });
		 
		 $(".mysearch").mouseout(function(){
		 	 $(this).removeClass("mysearch-hover");
		 });
		  $(".service").mouseover(function(){
		 	 $(this).addClass("mysearch-hover");
		 });
		 
		 $(".service").mouseout(function(){
		 	 $(this).removeClass("mysearch-hover");
		 });
		 
		  $(".sendBt").mouseover(function(){
		 	 $(this).addClass("sendBt-hover");
		 });
		 
		 $(".sendBt").mouseout(function(){
		 	 $(this).removeClass("sendBt-hover");
		 });
		  $(".editBt").mouseover(function(){
		 	 $(this).addClass("mysearch-hover");
		 });
		 
		 $(".editBt").mouseout(function(){
		 	 $(this).removeClass("mysearch-hover");
		 });


	</script>
</body>
</html>
