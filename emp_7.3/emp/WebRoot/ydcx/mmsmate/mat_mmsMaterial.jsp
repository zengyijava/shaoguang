<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.servmodule.ydcx.constant.ServerInof" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE XHTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mmsMaterial");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String fileSize =  null==request.getParameter("FILE_SIZE")?"":request.getParameter("FILE_SIZE");
	
	String lgcorpcode = request.getParameter("lgcorpcode");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	//服务器名称
	String serverName = ServerInof.getServerName();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
 %>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
  		<link href="<%=skin %>/addrBook.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
  		<link href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" href="<%=iPath %>/css/mmsmaterial.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=skin %>/ydcx_applicationMms.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	</head>

	<body id="ydcx_mmsMaterial">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<%
		if(CstlyeSkin.contains("frame4.0")){
	%>
		<input id='hasBeenBind' value='1' type='hidden'/>
	<%
		}
	%>
		<div id="container" class="container">
			<%-- header开始 --%>
			<%--<%=ViewParams.getPosition(menuCode) %>
			--%><%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			 
			<%-- 内容开始 --%>
			<div id="container" class="container">
				<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			
				<div class="left_dep div_bd" align="left" >
					<input type="hidden" id="id" />
					 <input type="hidden" id="pathUrl" value="<%=path %>" />
					  <input type="hidden" id="iPath" value="<%=iPath %>" />
					   <input type="hidden" id="fileServerUrl" value="<%=StaticValue.getFileServerUrl() %>" />
					   	<input type="hidden" name="httpUrl" id="httpUrl" value="<%=StaticValue.getFileServerViewurl() %>">
						<input type="hidden" name="isCluster" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
					     <input type="hidden" id="mmsMaterialtableUrl" value="mat_mmsMaterial.htm?tttt=<%=System.currentTimeMillis()%>" />
					<h3 class="div_bd title_bg">
		         		<emp:message key="ydcx_cxyy_cxsc_cxscfl" defVal="彩信素材分类" fileName="ydcx"></emp:message>
					</h3>
					<div id="depOperate" class="depOperate">
							<span id="delDepNew" class="depOperateButton3" onclick="menuDelete()"></span>
							<span id="updateDepNew" class="depOperateButton2" onclick="doEditSort('update')"></span>
							<span id="addDepNew" class="depOperateButton1" onclick="doEditSort('add')"></span>
					</div>
					<div id="materialTree" class="list ydcx_materialTree">
					</div>
				</div>
				<div class="right_info">
				<form name="pageForm" action="" method="post">
					<div class="buttons ydcx_buttons">
					<%if(btnMap.get(menuCode+"-1")!=null){ %>
						<a id="add" onclick="javascript:doEdit('','','','','add')"><font class="ydcx_addFont"><emp:message key="ydcx_cxyy_common_btn_4" defVal="新建" fileName="ydcx"></emp:message></font></a>
					<%}%>
					<%if(btnMap.get(menuCode+"-2")!=null){ %>
						<a id="delete" onclick="javascript:delAll()"><font class="ydcx_deleteFont"><emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message></font></a>
					<%} %>
			
 						<input type="hidden" id="childCode" name="childCode"/>
  						<input type="hidden" id="inheritPath" name="inheritPath" value="<%=inheritPath %>"/>
 						<input type="hidden" name = "parentCodetemp" id="parentCodetemp"/>
						<input type="hidden" id="servletUrl" value="mat_mmsMaterial.htm"/>
						<input type="hidden" id="addrTemp" name="addrTemp"/>
						<input type="hidden" id="addrTemp2" name="addrTemp2" />
  					</div>
  					
  					<div id="getloginUser" class="ydcx_getloginUser">
					</div>
  					
					<div id="tableInfo">
					</div>
					</form>
				</div>
			 	<%--按添加按钮的HTML--%>
			<div id="com_add_part"   title="<emp:message key="ydcx_cxyy_cxsc_cxsc" defVal="彩信素材" fileName="ydcx"></emp:message>" class="ydcx_com_add_part">
  					 <table width="330px;">
							<tr>
								<td width="50px" align="right" class="ydcx_mtalName_labl_td">
									<emp:message key="ydcx_cxyy_cxsc_mc_mh" defVal="名称：" fileName="ydcx"></emp:message>
								</td>
								<td colspan="2" class="ydcx_mtalName_td">
									<input type="text" name="mtalName" id="mtalName" maxlength="32" class="input_bd"/>
								</td>
							</tr>
							<tr><td height="3px" colspan="3"></td></tr>
							<%--
							<tr>
								<td width="50px" align="right">
									   备注：
								</td>
								<td ><textarea  rows="4" cols="19" name="comments" id="comments" style="width:250px"></textarea></td>
							</tr> --%>
							<tr>
								<td width="50px" align="right" class="ydcx_material_td1">
									 <emp:message key="ydcx_cxyy_cxsc_sc_mh" defVal="素材：" fileName="ydcx"></emp:message>
								</td>
								<td class="ydcx_material_td2">
									    <a id="doUp" name="doUp" class="afileclass1 ydcx_afileclass1"><emp:message key="ydcx_cxyy_cxsc_scfj" defVal="上传附件" fileName="ydcx"></emp:message></a>
			                          <div id="filesdiv" class="ydcx_filesdiv">
			                           	<input id='mmsMaterialUpload' name='mmsMaterialUpload'  type='file' onchange="javascript:upfile();" class="numfileclass ydcx_numfileclass" value=""/>						  
									  </div>
 								</td>
 							    <td class="ydcx_material_td3" align="left">
									  	 <label class="ydcx_labe1">
								  	 			<emp:message key="ydcx_cxyy_cxsc_tp_mh" defVal="图片：" fileName="ydcx"></emp:message><span class="ydcx_span">jpg,jpeg,gif</span><br/>
												<emp:message key="ydcx_cxyy_cxsc_yp_mh" defVal="音频：" fileName="ydcx"></emp:message><span class="ydcx_span">mid,midi,amr</span>
									  	 </label>
								</td>
							</tr>
							<tr><td height="3px"  colspan="3"></td></tr>
							<tr>
								<td class="ydcx_preview_label_td" align="right">
									<emp:message key="ydcx_cxyy_cxsc_yl_mh" defVal="预览：" fileName="ydcx"></emp:message>
								</td>
								<td colspan="2" class="ydcx_soundpic_td">
									<div id="soundpic" class="div_bg ydcx_soundpic">
	 								</div>
								</td>
							</tr>
							
							<tr>
								<td colspan="3" align="center" class="ydcx_confirm_cancel">
									<input id="queren" class="btnClass5 mr23 ydwx_borderRadius" type="button" onclick="javascript:addOrUpdateMaterial();" value="<emp:message key="ydcx_cxyy_common_btn_7" defVal="确认" fileName="ydcx"></emp:message>"/>
									<input id="quxiao" onclick="javascript:$('#com_add_part').dialog('close');" class="btnClass6 ydwx_borderRadius" type="button" value="<emp:message key="ydcx_cxyy_common_btn_16" defVal="取消" fileName="ydcx"></emp:message>" />
									<br/>
								</td>
							</tr>
							
							</table>
							<input type="hidden" name="parentCode" id="parentCode" value=""/>					 
					</div>
					
					
					
					
					
					
					
			<%-- 彩信分类按添加按钮的HTML--%>
			<div id="com_add_part2" class="ydcx_com_add_part2"  title="">
				<table width="370px;" class="ydcx_part2_tabl">
					<tr>
						<td class="ydcx_td1">
							<emp:message key="ydcx_cxyy_cxsc_scflmc_mh" defVal="素材分类名称：" fileName="ydcx"></emp:message>
						</td>
						<td class="ydcx_td2"><input type="text" name="sortName2" id="sortName2" maxlength="16" class="input_bd ydcx_sortName2"/>
						<input type="hidden" name="childCode2" id="childCode2"/>
						<input type="hidden" name="parentCode2" id="parentCode2"/>
						<input type="hidden" name="sortNametemp" id="sortNametemp"/>
						<input type="hidden" name="addOrRename" id="addOrRename"/>
					</tr>
					<tr>
						<td colspan="2" align="center" calss="ydcx_confirm_cancle1">
							<input id="queren1" class="btnClass5 mr23 ydwx_borderRadius" type="button" onclick="javascript:addRename();" value="<emp:message key="ydcx_cxyy_cxsc_qr" defVal="确认" fileName="ydcx"></emp:message>"/>
							<input id="quxiao1" onclick="javascript:$('#com_add_part2').dialog('close');" class="btnClass6 ydwx_borderRadius" type="button" value="<emp:message key="ydcx_cxyy_common_btn_16" defVal="取消" fileName="ydcx"></emp:message>" />
							<br/>
						</td>
					</tr>
				</table>
				
 			</div>
 			</div>
 			       <div id= "trid"   title="<emp:message key="ydcx_cxyy_cxsc_cxscfl" defVal="彩信素材分类" fileName="ydcx"></emp:message>" class="ydcx_trid">
						<div id="transferTree">
							<table>
								<tr>
									<td><emp:message key="ydcx_cxyy_cxsc_scflmc_mh" defVal="素材分类名称:" fileName="ydcx"></emp:message><input type="text" name="sortName" id="sortName3"/></td>
									<td><input type="button" id="seleSort" name="seleSort" value="<emp:message key="ydcx_cxyy_cxsc_qxzfl" defVal="请选择分类" fileName="ydcx"></emp:message>" onclick="javascript:getSortTree()"/></td>
								</tr>
							</table>
						</div>	
					</div>
			<%} %>
			
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
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<script>
			var serverName = "<%=serverName%>";
			var langName = "<%=langName%>";
			var langEng = "<%=StaticValue.ZH_HK%>";
		</script>
		<script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		 <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 	    <script type="text/javascript" src="<%=commonPath %>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script language="javascript" type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 	    <script language="javascript" type="text/javascript" src="<%=iPath%>/js/mmsMaterial.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
	</body>
</html>