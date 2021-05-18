<%@ page language="java" import="com.montnets.emp.entity.sysuser.LfSysuser" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.rms.servmodule.constant.ServerInof"%>
<%@ page import="com.montnets.emp.rms.vo.LfTemplateVo"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mytemplate");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	List<LfTemplateVo> mmsList = (List<LfTemplateVo>)request.getAttribute("mmsList");
	//@ SuppressWarnings("unchecked")
	//List<LfSysuser> sysList = (List<LfSysuser>)session.getAttribute("sysList");
	LfTemplateVo mt = (LfTemplateVo)request.getAttribute("mmsVo");
	int rState = mt.getIsPass()==null?-2:mt.getIsPass();
	long state = mt.getTmState()==null?-2:mt.getTmState();
	int auditStatus = mt.getAuditstatus()==null?-2:mt.getAuditstatus();
	int submitstatus = mt.getSubmitstatus() == null?-2:mt.getSubmitstatus();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	//服务器名称
	String serverName = ServerInof.getServerName();
    String flowId = request.getParameter("flowId");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);
	//当前登录企业编号
	String loginOrgcode = lfSysuser.getCorpCode();
	request.setAttribute("loginOrgcode", loginOrgcode);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>我的场景</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=commonPath%>/rms/mbgl/css/myScene.css?V=<%=StaticValue.getJspImpVersion()%>" />
<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
<link rel="stylesheet" href="<%=inheritPath%>/plugin/fx.editor.css" />
<style type="text/css">
html{
	margin-bottom:150px;
}
#content tfoot tr td #p_goto {
	display: none;
}

#cont{
    padding-bottom: 0px;
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
.Wdate{
	background: url("<%=skin%>/images/datePicker.gif") no-repeat right;
}
</style>
</head>
<%@include file="/common/common.jsp" %>
<body>
	<input type="hidden" id="skin" value="<%=skin%>" />
	<input type="hidden" id="pathUrl" value="<%=path%>" />
	<input type="hidden" id="ipathUrl" value="<%=inheritPath%>" />
	<input type="hidden" id="commonPath" value="<%=commonPath%>" />
	<input type="hidden" id="iPath" value="<%=iPath%>" />
	<input type="hidden" id="templType" value="2">
	<%-- 当前位置 --%>
    <%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
    <%-- <div class="fxsend-crumbs">
         <a href="javascript:void(0);">企业富信 ></a>
         <a href="javascript:void(0);">富信应用 ></a>
         <span>我的场景</span>
      </div>
      <div class="top-line"></div> --%>
	<div id="container" class="container">
		<form name="pageForm" action="mbgl_mytemplate.htm?method=find" method="post" id="pageForm">
			<div style="display:none" id="hiddenValueDiv"></div>
			<div id="condition" class="condition">
				<table class="condition-tabl" cellspacing="10">
					<thead>
						<tr>
							<td align="left" width="8%"><font><emp:message key="rms_fxapp_myscene_sceneid" defVal="场景ID：" fileName="rms"/></font></td>
							<td align="left" width="17%"><input style="width:187px" type="text" name="tmCode" id="tmCode"
								 value="<%=mt.getSptemplid()==null?"":mt.getSptemplid()%>"
								 onkeyup="javascript:numberControl($(this))" maxlength="10"/></td>
							<td align="left"><font><emp:message key="rms_fxapp_myscene_fxtheme" defVal="富信主题：" fileName="rms"/></font></td>
							<td width="8%"><input type="text" name="theme" id="tmName"
								value="<%=mt.getTmName()==null?"":mt.getTmName()%>"
								style="width:187px"  maxlength="20"/></td>
							<td align="left" width="8%"><font><emp:message key="rms_fxapp_myscene_scenestate" defVal="场景状态：" fileName="rms"/></font></td>
							<td width="17%"><select id="state" name="state" Style="width:187px">
									<option value=""><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
									<option value="1" <%=state-1==0?"selected":""%>><emp:message key="rms_fxapp_myscene_enabled" defVal="已启用" fileName="rms"/></option>
									<option value="0" <%=state+0==0?"selected":""%>><emp:message key="rms_fxapp_myscene_disabled" defVal="已禁用" fileName="rms"/></option>
									<option value="2" <%=state-2==0?"selected":""%>><emp:message key="rms_fxapp_myscene_draft" defVal="草稿" fileName="rms"/></option>
							</select></td>
							<td align="right" width="12%" style="padding-right:10px;"><font><emp:message key="rms_fxapp_myscene_scenetype" defVal="场景类型：" fileName="rms"/></font></td>
							<td width="13%"><select id="dsFlag" name="dsFlag" Style="width:187px">
									<option value="">
										<emp:message key="ydcx_cxyy_common_text_16" defVal="全部"
											fileName="ydcx"></emp:message>
									</option>
									<option value="0"
										<%=mt.getDsflag()!=null && mt.getDsflag()==0?"selected":""%>>
										<emp:message key="rms_fxapp_myscene_commss" defVal="通用静态场景" fileName="rms"/>
									</option>
									<option value="1"
										<%=mt.getDsflag()!=null && mt.getDsflag()==1?"selected":""%>>
										<emp:message key="rms_fxapp_myscene_commds" defVal="通用动态场景" fileName="rms"/>
									</option>
							</select></td>
						</tr>
						<tr>
							<td align="left" width="8%"><font><emp:message key="rms_fxapp_myscene_optstate" defVal="运营商状态：" fileName="rms"/></font></td>
							<td align="left" width="17%"><select id="auditStatus" name="auditStatus"
								style="width:187px">
									<option value="">
										<emp:message key="ydcx_cxyy_common_text_16" defVal="全部"
											fileName="ydcx"></emp:message>
									</option>
									<option value="-1" <%=auditStatus==-1?"selected":""%>><emp:message key="rms_fxapp_myscene_noapproval" defVal="未审批" fileName="rms"/></option>
									<option value="3" <%=auditStatus==3?"selected":""%>><emp:message key="rms_fxapp_myscene_approvaling" defVal="审批中" fileName="rms"/></option>
									<option value="4" <%=auditStatus==4?"selected":""%>><emp:message key="rms_fxapp_myscene_disabled" defVal="已禁用" fileName="rms"/></option>
									<option value="1" <%=auditStatus==1?"selected":""%>><emp:message key="rms_fxapp_myscene_approvaled" defVal="审批通过" fileName="rms"/></option>
									<option value="2" <%=auditStatus==2?"selected":""%>><emp:message key="rms_fxapp_myscene_nopass" defVal="审批未通过" fileName="rms"/></option>
							</select></td>
							<td align="left" width="8%"><font><emp:message key="rms_fxapp_myscene_createtime" defVal="创建时间：" fileName="rms"/></font></td>
							<td width="17%"><input type="text"
								value="<%=mt.getAddStartm()==null?"":mt.getAddStartm()%>"
								id="submitSartTime" name="submitSartTime"
								style="cursor: pointer; width: 187px;background-color: white;"
								class="Wdate" readonly="readonly" onclick="stime()"></td>
							<td width="8%" align="right"><font style="margin-right: 50px;"><emp:message key="rms_fxapp_degreerep_to" defVal="至" fileName="rms"/></font></td>
							<td width="17%"><input type="text"
								value="<%=mt.getAddEndtm()==null?"":mt.getAddEndtm()%>"
								id="submitEndTime" name="submitEndTime"
								style="cursor: pointer; width: 187px;background-color: white;"
								class="Wdate" readonly="readonly" onclick="rtime()"></td>

							<c:if test="${loginOrgcode eq '100000' }" >
								<td align="right" width="8%">
									<font><emp:message key="rms_fxapp_myscene_ecid" defVal="企业编码：" fileName="rms"/></font>
								</td>
								<td align="left" width="17%">
									<input type="text" style="cursor: pointer; width: 187px;background-color: white;" id="corpCode" name="corpCode" value="<%=mt.getCorpCode()==null?"":mt.getCorpCode()%>">
								</td>
							</c:if>
							
							<c:if test="${loginOrgcode != '100000' }" >
								<td align="left" width="25%" colspan="2">
									<div align="right">
										<span><input class="search mysearch" id="search" type="button" value="<emp:message key="common_query" defVal="查询" fileName="common"/>"></span>
										<%-- <span><input class="import" id="upload" onclick="javascript:$('#modelTms').dialog('open')" type="button" value="导入"> </span> --%>
									</div>
								</td>
							</c:if>
						</tr>
						<c:if test="${loginOrgcode eq '100000' }" >
							<tr>
								<td colspan="8">
									<div align="right">
										<span><input class="search mysearch" id="search" type="button" value="<emp:message key="common_query" defVal="查询" fileName="common"/>"></span>
										<%-- <span><input class="import" id="upload" onclick="javascript:$('#modelTms').dialog('open')" type="button" value="导入"> </span> --%>
									</div>
								</td>
							</tr>
						</c:if>
					</thead>
				</table>
			</div>
		    <div id="content" class="content rContent">
		    	<%--<table align="right">--%>
					<tbody style="width: 100%">
		    	
						<ul class="tagul" style="width: 100%;padding-left: 0px;" >
							<div class="con_li">
								<li>
									<div class="tag" onclick="doAdd()">
										<a id="add" href="javascript:void(0);" class="append"></a>
										<p class="txt_add"><emp:message key="rms_fxapp_myscene_newscene" defVal="创建新场景" fileName="rms"/></p>
									</div>
								</li>
								<c:forEach items="${mmsList}" var="bean" varStatus="status">
									<c:if test="${status.index % 5 == 3}">
										<li style="margin-right: 0px">
									</c:if>
									<c:if test="${status.index % 5 != 3}">
										<li>
									</c:if>
									<div class="tag">
										<div class="bkimg">
											<div class="first-frame">
												<jsp:include page="${basePath }/${fn:replace(bean.tmMsg, 'fuxin.rms', 'firstframe.jsp')}"></jsp:include> 
											</div>
											<div class="mennu" style="float: right;display: none;">
												<ul class="menu_ul">
													<li title="<emp:message key="common_preview" defVal="预览" fileName="common"/>">
													<a><img tmid="${bean.tmid}" urlMsg="${bean.tmMsg}" tmName="${bean.tmName}" ver="${bean.ver}" onclick="doPreview(this)" src="<%=iPath%>/image/preview_icon.png"></a>
													</li>
													<li title="<emp:message key="common_copy" defVal="复制" fileName="common"/>"><a><img onclick="javascript:doCopy('${bean.tmid}','${bean.tmMsg}','copy','${bean.industryid}','${bean.useid}')" src="<%=iPath%>/image/copy_icon.png">
													</a>
													</li>
													<li><a title="<emp:message key="common_export" defVal="导出" fileName="common"/>"><img  onclick="javascript:doExport('${bean.tmMsg}','${bean.tmid}')" src="<%=iPath%>/image/export.png">
													</a>
													</li>
													<li>
														<a><img src="<%=iPath%>/image/more_icon.png"  onmouseover="showDetail(this,'${bean.tmid}');" onmouseout="hideDetail(this,'${bean.tmid}');"></a>
													</li>
														<%--详情  --%>
														<div id="templDetail_${bean.tmid}" class="templDetail">
														    <div class="detail_inner">
															    <div>
															    	<font ><emp:message key="rms_fxapp_myscene_fxtheme" defVal="富信主题：" fileName="rms"/>${bean.tmName}</font>
															    </div>
															    <div clss="line1" style="margin-top: 10px;width: 200px;border-bottom:1px solid #666666;">
															     <font clss="line1"></font>
															    </div>
														    	<div class="fontcol">
															    	<font >
															    		ID：${bean.sptemplid==0?'-':(bean.sptemplid)}
															    	</font>
															    </div>
															    <div class="fontcol">
															    	<font ><emp:message key="rms_fxapp_myscene_usecounts" defVal="使用次数：" fileName="rms"/>${bean.usecount}<emp:message key="text_times" defVal="次" fileName="common"/></font>
															    </div>
														    	<div class="fontcol">
															    	<font ><emp:message key="rms_fxapp_myscene_capacity" defVal="容量：" fileName="rms"/>${bean.degreeSize}</font>
															    </div>
														    	<div class="fontcol">
															    	<font ><emp:message key="rms_fxapp_myscene_level" defVal="档位：" fileName="rms"/>${bean.degree}</font>
															    </div>
														    	<div class="fontcol">
															    	<font >
															    	<emp:message key="rms_fxapp_myscene_scenetype" defVal="场景类型：" fileName="rms"/>
															    	<c:if test="${bean.dsflag==1}">
															    		<emp:message key="rms_fxapp_myscene_commds" defVal="通用动态场景" fileName="rms"/>
															    	</c:if>
															    	<c:if test="${bean.dsflag==0}">
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
															    	<font ><emp:message key="rms_fxapp_myscene_ecid" defVal="企业编码：" fileName="rms"/>${bean.corpCode}</font>
															    </div>
														    	<div class="fontcol">
															    	<font ><emp:message key="rms_fxapp_myscene_createtime" defVal="创建时间：" fileName="rms"/>${fn:substring(bean.addtime, 0, 19)}</font>
															    </div>
														    	<div class="fontcol">
															    	<font >
																    	<emp:message key="rms_fxapp_myscene_scenestate" defVal="场景状态：" fileName="rms"/>
																    	<c:if test="${bean.tmState ==1}">
																    		<emp:message key="rms_fxapp_myscene_enabled" defVal="已启用" fileName="rms"/>
																    	</c:if>
																    	<c:if test="${bean.tmState ==0}">
																    		<emp:message key="rms_fxapp_myscene_disabled" defVal="已禁用" fileName="rms"/>
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
															    			<emp:message key="rms_fxapp_myscene_noneedexam" defVal="无需审批" fileName="rms"/>
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
													<c:if test="${bean.corpCode ==loginOrgcode}">
													<li><a title="<emp:message key="common_delete" defVal="删除" fileName="common"/>"><img onclick="del('<%=path %>','${bean.tmid}');" src="<%=iPath%>/image/delete_icon.png"></a>
													</li>
													</c:if>
												</ul>
												<c:if test="${bean.tmState ==1 && bean.auditstatus == 1}">
												<c:choose>
													<c:when test="${bean.isShortTemp==0}">
														<%-- <div class="shortScene" onclick="toShortScene('${bean.tmid}','${bean.tmName}');">
															<font>设置为快捷场景</font>
														</div> --%>
														<c:if test="${bean.corpCode==loginOrgcode}">
															<input type="button" class="shortScene" id="shortScene_${bean.tmid}" onclick="toShortScene('${bean.tmid}','${bean.tmName}');" value="<emp:message key="rms_fxapp_myscene_setshortscene" defVal="设置为快捷场景" fileName="rms"/>">
														</c:if>
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
												<c:when test="${fn:length(bean.tmName) > 16}">${fn:substring(bean.tmName, 0, 16)}...</c:when>
												<c:otherwise>${bean.tmName}</c:otherwise>
											</c:choose>
										</div>
										<div class="theme_phone">
											<div class="tmid">ID：${bean.sptemplid==0?'-':(bean.sptemplid)}</div>
										    <div class="download"> <c:if
													test="${bean.dsflag==1}">
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
													<input class="editBt" onclick="doCopy('${bean.tmid}','${bean.tmMsg}','edit','${bean.industryid}','${bean.useid}')" type="button" value="<emp:message key="common_edit" defVal="编辑" fileName="common"/>"> 
												</div>
											</c:if>
											<%-- 模板状态  tmState ==1： 已启用 --%>
											<c:if test="${bean.tmState ==1}">
											 	<div class="carrier"> 
												<c:choose>
													<%--未审批 --%>
													<c:when  test="${bean.auditstatus == -1}">
														<input class="unSendBt"  type="button" value="<emp:message key="rms_fxapp_myscene_noapproval" defVal="未审批" fileName="rms"/>">
													</c:when>
													<%--审批通过 --%>
													<c:when  test="${bean.auditstatus == 1}">
														<c:if test="${bean.corpCode==loginOrgcode}">
															<input class="sendBt" onclick="rmsSend('${bean.tmid}','${bean.sptemplid}','${bean.usecount}');" type="button" value="<emp:message key="rms_fxapp_myscene_sendnow" defVal="立即发送" fileName="rms"/>">
														</c:if>
														<c:if test="${bean.corpCode !=loginOrgcode}">
															<input class="sendBt forbidden" type="button" disabled="disabled" value="<emp:message key="rms_fxapp_myscene_sendnow" defVal="立即发送" fileName="rms"/>">
														</c:if>
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
												<c:if test="${bean.corpCode == loginOrgcode}">
												<div class="state">
													<div class="open-close">
														 <c:if test="${bean.tmState == 1}">
															<a title="<emp:message key="rms_fxapp_myscene_enabled" defVal="已启用" fileName="rms"/>"  href="javascript:void(0);"onclick="chState(this,'${bean.tmid}',0);"><img src="<%=commonPath%>/rms/mbgl/image/close.png"/></a>
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
														<input class="forbidden" type="button" value="<emp:message key="rms_fxapp_myscene_sendnow" defVal="立即发送" fileName="rms"/>"> 
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
							<c:if test="${status.index % 5 == 3}">
							<div style="clear: both;"></div>
							</div>
							<div class="con_li">
							</c:if>
							</c:forEach>
							</div>
						</ul>
						<div style="clear: both;"></div>
						</tbody>
						<tfoot>
							<tr>
								<td>
									<div id="pageInfo" style="clear: both;" calss="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
		 			<%--</table>--%>
			</div>
		</form>
	</div>
	

	<div id="modelTms" class="model" style="display: none;width:395px;">
		<div class="main" style="width:365px;">
			<div style="padding:6px; width:340px;">
				<div id="upTms">
					<p>
						<span style="width:70px;float:left;"><emp:message
								key="ydcx_cxyy_mbbj_xzwj_mh" defVal="选择文件：" fileName="ydcx"></emp:message>
						</span> <input style="width:138px;float:left;" type="textfield"
							id="filepath" readonly> <span
							style="width:10px;float:left;">&nbsp;&nbsp;&nbsp;&nbsp;</span> <span>
							<span
							style="position:relative;float:left;display:block;width:80px;height:18px;border:1px solid #999;background: #eee;text-align:center;line-height:18px;">
								<emp:message key="ydcx_cxyy_mbbj_xzwj" defVal="上传文件"
									fileName="ydcx"></emp:message> <input
								style="position:absolute;left:0;top:0;width:82px;height:20px;opacity:0;filter:alpha(opacity=0);"
								type="file" id="chooseRms" name="chooseRms"
								onchange="document.getElementById('filepath').value=this.value;document.getElementById('filepath').title=this.value;" />
						</span> </span>
					</p>
					<br /> <br />
					<%-- <p style="margin-top:5px; color:blue;"><emp:message key="ydcx_cxyy_mbbj_cxwjgsw" defVal="富信文件格式为：rms" fileName="ydcx"></emp:message></p> --%>
					<p style="margin-top:5px; color:blue;"><emp:message key="rms_fxapp_myscene_fxformat" defVal="富信文件格式为：rms" fileName="rms"/></p>
					<p style="float: right;padding-top:10px;">
						<input type="button" id="uploadTms"
							value="<emp:message key="ydcx_cxyy_mbbj_ljsc" defVal="立即上传" fileName="ydcx"></emp:message>"
							onclick="javascript:doUp()" class="btnClass2" />
					</p>
				</div>
				<div id="curTms" style="display:none"></div>
			</div>
		</div>
		<div class="foot">
			<div class="foot-right"></div>
		</div>
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

	<script type="text/javascript"
		src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	
	<script type="text/javascript">
    	 //初始化加载
    	 $(document).ready(function() {
				//rms文件弹框初始化
		        $("#modelTms").dialog({
					autoOpen: false,
					//title:"导入富信文件",
					title:getJsLocaleMessage("rms","rms_myscene_importtpl"),
					height:200,
					width:365
				});
		    
				var findresult="<%=request.getAttribute("findresult")%>";
			    if(findresult != null && findresult !="" && findresult=="-1")
			    {
			       //alert("加载页面失败，请检查网络是否正常！");	
			       alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_jzyesb"));	
			       return;			       
			    }
				 getLoginInfo("#hiddenValueDiv");
	    		//公共翻页初始化	
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				
				//点击查询
				$('#search').click(function(){submitForm();});
				
				//第一帧等比缩放
			    //IE版本小于9
			    if(navigator.appName == "Microsoft Internet Explorer" && parseInt(navigator.appVersion.split(";")[1].replace(/[ ]/g, "").replace("MSIE","")) < 9){
		            	zoomIE();
					}else{//
						zoom();
					}
			    });
    	
    	//开始时间
    	function stime(){
			    var max = "2099-12-31 23:59:59";
			    var v = $("#submitEndTime").attr("value");
			    var min = "1900-01-01 00:00:00";
				if(v.length != 0)
				{
				    max = v;
				    var year = v.substring(0,4);
					var mon = v.substring(5,7);
					if (mon != "01")
					{
					    mon = String(parseInt(mon,10)-1);
					    if (mon.length == 1)
					    {
					        mon = "0"+mon;
					    }
					}
					else
					{
					    year = String((parseInt(year,10)-1));
					    mon = "12";
					}
					min = year+"-"+mon+"-01 00:00:00"
				}
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max,enableInputMask:false});
			
			};
			
			//结束时间
			function rtime(){
			    var max = "2099-12-31 23:59:59";
			    var v = $("#submitSartTime").attr("value");
				if(v.length != 0)
				{
				    var year = v.substring(0,4);
					var mon = v.substring(5,7);
					var day = 31;
					if (mon != "12")
					{
					    mon = String(parseInt(mon,10)+1);
					    if (mon.length == 1)
					    {
					        mon = "0"+mon;
					    }
					    switch(mon){
					    case "01":day = 31;break;
					    case "02":day = 28;break;
					    case "03":day = 31;break;
					    case "04":day = 30;break;
					    case "05":day = 31;break;
					    case "06":day = 30;break;
					    case "07":day = 31;break;
					    case "08":day = 31;break;
					    case "09":day = 30;break;
					    case "10":day = 31;break;
					    case "11":day = 30;break;
					    }
					}
					else
					{
					    year = String((parseInt(year,10)+1));
					    mon = "01";
					}
					max = year+"-"+mon+"-"+day+" 23:59:59"
				}
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
			};
			
			//创建模板
			function doAdd()
			{
				var btn = '${btnMap}';
				if(!(btn.indexOf("5100-1300-1") > -1)){//5100-1300-1：我的场景新建权限
					alert("您不具备此权限，请与管理员联系！");
					return false;
				}
			   //1静态彩信  2动态彩信  3新增
			   var pathUrl = $("#pathUrl").val();
			   window.location.href=pathUrl+'/mbgl_mytemplate.htm?method=doAdd&type=3&lgcorpcode='+$("#lgcorpcode").val();
			}
			
			
			//预览
			function doPreview(obj)
			{	
				  //rms 文件相对路径
				  var msg = $(obj).attr("urlMsg");
				  //模板名称
				  var tmName = $(obj).attr("tmName");
				  //模板自增ID
				  var tmid = $(obj).attr("tmid");
				  //rms 版本
				  var ver = $(obj).attr("ver");
				  // tmName = encodeURI(tmName); 
				  //项目相对路径
				   var pathUrl = $("#pathUrl").val();
				    $.post(pathUrl+"/mbgl_mytemplate.htm?method=getTmMsg",{tmUrl:msg,tmName:tmName,tmid:tmid,ver:ver},function(result){
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
						  showBg();
					}
					else
					{
			             //alert("内容文件不存在，无法预览！");
						alert(getJsLocaleMessage("rms","rms_myscene_nofile"));
					}
				});
			}	
		//编辑
		function doCopy(tmId,tmUrl,opType,industryid,useid){
		 	 var pathUrl = $("#pathUrl").val();
   		     window.location.href=pathUrl+"/mbgl_mytemplate.htm?method=doCopy&tmId="+tmId+"&opType="+opType+"&tmUrl="+tmUrl+"&industryId="+industryid+"&useId="+useid;
		}
		//删除
		function del(path,i){
			var btn = '${btnMap}';
			if(!(btn.indexOf("5100-1300-2") > -1)){//5100-1300-2：我的场景删除权限
				alert("您不具备此权限，请与管理员联系！");
				return false;
			}
			var tempId = i;
			var skin = $("#skin").val();
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
		                    window.location.reload();
						}else{
							alert(getJsLocaleMessage("rms","rms_myscene_faildel"));
						}
				});
			}
		}
		
		//导入富信模板
		function doUp(){
			    var tms = $.trim($("#chooseRms").val());
				if (tms == ""){
					alert(getJsLocaleMessage("rms","rms_myscene_uploadfile"));
					//alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qxzyscdcxwj"));
					return ;
				}else {
					tms = tms.substring(tms.lastIndexOf(".")+1,tms.length).toUpperCase();
			    	if(tms != "RMS"){
			    		 alert(getJsLocaleMessage("rms","rms_myscene_unsupportfmt"));
			    		//alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_bzcdcxgs"));
				        return ;
			    	}
			    }
			   var pathUrl = $("#pathUrl").val();
			   var lguserid= $("#lguserid").val();
			   var lgcorpcode= $("#lgcorpcode").val();
				   $.ajaxFileUpload ({ 
				    url:pathUrl+"/mbgl_mytemplate.htm?method=importRms&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid, //处理上传文件的服务端 
				    secureuri:false, //是否启用安全提交，默认为false
				    fileElementId:'chooseRms', //与页面处理代码中file相对应的ID值
				    dataType: 'text', //返回数据类型:text，xml，json，html,scritp,jsonp五种
				    success: function (result) { 
				    	if(result == "true"){
				    		var path = '<%=path%>';
						window.location.href = path+ "/mbgl_mytemplate.htm?method=find";
					} else {
						alert(getJsLocaleMessage("rms","rms_myscene_failimport") + result);

					}
				}
			});
		}
		//导出
		function doExport(u,tmid)
		{
			 var pathUrl = $("#pathUrl").val();
			 $.post(pathUrl+"/mbgl_mytemplate.htm?method=checkMmsFile", 
			 {url : u,tmid:tmid},
			 function(result){
				if (result == "true") {
					download_href(pathUrl+"/mbgl_mytemplate.htm?method=exportRms&u="+u);
				}else if (result == "false"){
					alert(getJsLocaleMessage("rms","rms_myscene_nofile"));
				}else{
					alert(getJsLocaleMessage("rms","rms_myscene_errnoredirect"));
				}
			});
		}
		
		//设置为快捷场景
		function toShortScene(tmId,tmName){
			var name = tmName;
			var skin = $("#skin").val();
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
	                        	if(skin.indexOf("frame2.5") != -1){
		                        	var oneMenu = $(window.parent.document).find("#leftIframe").contents().find("#mod25").find("#sider");
		                        	var secondMenu = oneMenu.children('ul').eq(0).children('li').eq(0).children('span');
		                        	if(secondMenu.text() != "我的快捷场景"){
		                        		var liprependTo = "<li class=\"mune_show\"><span>"+getJsLocaleMessage("rms","rms_myscene_myshortscene")+"</span><ul class=\"mune_hidden div_bd\">"+
		                        		"<li  title='"+name+"' class=\" \" onmouseover=\"mouseOver1('"+tmId+"')\" onmouseout=\"mouseOut1('"+tmId+"')\"><a id='ak"+tmId+"' class=\" \" onmouseover=\"mouseOver('"+tmId+"')\" onmouseout=\"mouseOut('"+tmId+"')\" onclick=\"doOpen('/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmId+"','"+tmId+"','"+name+"')\">"+name+
		                        		"<input type=\"button\" id=\"closeLi"+tmId+"\" value=\"x\" style=\"border: 0;width: 20px;height:30px;position: absolute;left: 170px;cursor: pointer;background-color: #f1f1f9;display:none;\" onclick=\"deleteLi('"+tmId+"','"+name+"');\"></a></li></ul></li>";
	                        		 	oneMenu.children('ul').eq(0).prepend(liprependTo);
		                        	}else{
		                        		var liprepend = "<li title='"+name+"' class=\" \" onmouseover=\"mouseOver1('"+tmId+"')\" onmouseout=\"mouseOut1('"+tmId+"')\"><a id='ak"+tmId+"' class=\" \"  onmouseover=\"mouseOver('"+tmId+"')\" onmouseout=\"mouseOut('"+tmId+"')\" onclick=\"doOpen('/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmId+"','"+tmId+"','"+name+"')\">"+name+
		                        		"<input type=\"button\" id=\"closeLi"+tmId+"\" value=\"x\" style=\"border: 0;width: 20px;height:30px;position: absolute;left: 170px;cursor: pointer;background-color: #f1f1f9;display:none;\" onclick=\"deleteLi('"+tmId+"','"+name+"');\"></a></li>";
		                        		oneMenu.children('ul').eq(0).children('li').eq(0).children('ul').eq(0).prepend(liprepend);
		                        	}
	                        	}else{
		                        	var oneMenu = $(window.parent.document).find("#leftIframe").contents().find("#mod25");
		                        	var secondMenu = oneMenu.children('ul').eq(0).children('li').eq(0).children('p');
		                        	if(secondMenu.text() != "我的快捷场景"){
		                        		var width=oneMenu.children('ul').eq(0).width()+165;
		                        		oneMenu.children('ul').eq(0).css({"width":width});
		                        		var liprependTo = "<li><p class=\"second-nav-title\">"+getJsLocaleMessage("rms","rms_myscene_myshortscene")+"</p><ul class=\"third-nav-menu\">"+
		                        		"<li onmouseover=\"mouseOver('"+tmId+"')\" onmouseout=\"mouseOut('"+tmId+"')\" title='"+name+"' onclick=\"doOpen('/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmId+"','"+tmId+"','"+name+"')\"><a id='ak"+tmId+"'>"+name+"</a>"+
		                        		"<input type=\"button\" id=\"closeLi"+tmId+"\" value=\"x\" style=\"border: 0;width: 20px;height:20px;position: absolute;left: 130px;cursor: pointer;background-color: #f1f1f9;display:none;\" onclick=\"deleteLi('"+tmId+"','"+name+"');\"></li></ul></li>";
	                        		 	oneMenu.children('ul').eq(0).prepend(liprependTo);
		                        	}else{
		                        		var liprepend = "<li  onmouseover=\"mouseOver('"+tmId+"')\" onmouseout=\"mouseOut('"+tmId+"')\" title='"+name+"' onclick=\"doOpen('/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmId+"','"+tmId+"','"+name+"')\"><a id='ak"+tmId+"'>"+name+"</a>"+
		                        		"<input type=\"button\" id=\"closeLi"+tmId+"\" value=\"x\" style=\"border: 0;width: 20px;height:20px;position: absolute;left: 130px;cursor: pointer;background-color: #f1f1f9;display:none;\" onclick=\"deleteLi('"+tmId+"','"+name+"');\"></li>";
		                        		oneMenu.children('ul').eq(0).children('li').eq(0).children('ul').eq(0).prepend(liprepend);
		                        	}
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
	                	alert(getJsLocaleMessage("rms","rms_myscene_occurerr"));
	                }
				});
		}
		function deleteShortScene(tempId,tmName){
			var skin = $("#skin").val();
			if(confirm(getJsLocaleMessage("rms","rms_myscene_confirmdel2")+tmName+"?")){
			$.ajax({
				type:"POST",
				url :"rms_templateMana.htm?method=deleteShortTemp&tempId="+tempId,
                success:function (result) {
	                if (result=='true') {
                        alert(getJsLocaleMessage("rms","rms_myscene_successdel"));
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
	                    window.location.reload();
                    } else {
                        alert(getJsLocaleMessage("rms","rms_myscene_faildel"));
                    }
                }
			});
		}
		}
		////启用-禁用
		//t:0禁用，1启用
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
		              	// $("#pageForm").find("#search").click();
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
		
		//立即发送
		function rmsSend(tmid,sptemplid,usecount){
			var path = $("#pathUrl").val();
			usecount = parseInt(usecount) + 1; //点击立即使用就增加1次
            $.post(path+"/rms_commTpl.htm?method=updateUseCount",{tmid:tmid,usecount:usecount},function(result){
	            if(result != null && result == "success")
	            {
                    window.parent.openNewTab("5100-1000",path+ "/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmid);
					//window.location.href = path+ "/rms_rmsSameMms.htm?method=shortCut2Send&tempId="+tmid;
	            }
	            else
	            {
	            	 alert(getJsLocaleMessage("ydcx","rms_myscene_runexception"));
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
		
		
			//IE9以下浏览器等比缩放
		function zoomIE(){
			 //计算图参位置进行比例缩放
			 var _widthProportion = 236/385,
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
			 var _widthProportion = 236/385,
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
		 
		 $(".mysearch").mouseover(function(){
		 	 $(this).addClass("mysearch-hover");
		 });
		 
		 $(".mysearch").mouseout(function(){
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
