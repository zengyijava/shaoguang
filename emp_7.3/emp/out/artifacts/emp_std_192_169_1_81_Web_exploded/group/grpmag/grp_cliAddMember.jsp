<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	int plook = (btnMap.get(StaticValue.PHONE_LOOK_CODE) == null)?0:1;
	String menuCode = "1580-1400";
	
	String lguserid =request.getParameter("lguserid");

	String skin = session.getAttribute("stlyeSkin")==null?"default":
			(String)session.getAttribute("stlyeSkin");
			
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="group_ydkf_xzqz_text_getobj" defVal="获取对象" fileName="group"/></title>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/group.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		 <link rel="stylesheet" type="text/css" href="<%=iPath %>/css/grp_cliAddMember.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_TongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="grp_cliAddMember">
	<input type="hidden" id="pathUrl" value="<%=path %>">
	<div id="gpName_div" class="div_bg">
		<span class="group_span1"><emp:message key="group_ydbg_xzqz_text_groupname" defVal="群组名称" fileName="group"/>：</span>
		<input type="text" name="addGpName" id="addGpName" value="" maxlength="18"  onkeyup = "checkText($(this))"/>
		<label id="gpName_label"  for="addGpName">
			<emp:message key="group_ydbg_xzqz_text_entergroupname" defVal="请输入群组名称" fileName="group"/>
		</label>
	</div>
			<input type="hidden" id="strUser" name="strUser" value=""/>
			<input type="hidden" id="ipath" value="<%=request.getContextPath()%>"/>	
			<select class="group_display_none" id="tempOptions"></select>
			<select class="group_display_none" id="rightSelectTemp"></select>
			<select class="group_display_none" id="rightSelectTempAll"></select>
			<input type="hidden" id="depId" name="depId" value=""/>
			<input type="hidden" id="depName" name="depName" value=""/>
			<input type="hidden" id="epno" name="epno" value=""/>
			<input type="hidden" id="user" name="user" value=""/>
			<input type="hidden" id="addType" name="addType" value="3"/>
			
			<input type="hidden" id="plook" name="plook" value="<%=plook %>"/>
			
			<%-- 默认是新增分页信息 中第一页 --%>
			<input type="hidden" id="pageIndexAdd" value="1"/>
			<div class="group_div7"></div>		
			<table border="0"  cellspacing="0" cellpadding="0">
				<tr>
					<td class="group_td1">
						<center><select id="chooseType" onchange="changeChooseType(this.value)">
							<option value="1"><emp:message key="group_ydbg_xzqz_text_memberaddbookCli" defVal="客户通讯录" fileName="group"/></option>
							<option value="2"><emp:message key="group_ydbg_xzqz_text_personalgroup" defVal="个人群组" fileName="group"/></option>
							<% if(btnMap.get(menuCode+"-4")!=null) {  %><option value="3"><emp:message key="group_ydbg_xzqz_text_importfile" defVal="文件导入" fileName="group"/></option><%} %>
						</select>
						<input type=text" name="epname" id="epname" class="input_bd" value="" maxlength="20" onkeyup = "checkText($(this))"/>
						<label id="enterName" for="epname"><emp:message key="group_ydbg_xzqz_text_entername" defVal="请输入姓名" fileName="group"/></label>
						<%
							if(skin.contains("frame4.0")){
						%>
							<div onclick="dosearch()" id="searchNameButton" class="green-btn">
								<img class="search_img" src="<%=commonPath%>/common/img/search_icon.png" alt="">
							</div>
						<%
							}else{
						%>
							<a  id="btnSearch" href="javascript:dosearch()"></a>
						<%} %>
						</center>
					</td>	
					<td></td>
					<td class="group_font_size">&nbsp;&nbsp;<emp:message key="group_ydbg_xzqz_text_added" defVal="已添加" fileName="group"/><b id="numcount">0</b><emp:message key="group_ydbg_xzqz_text_peploe" defVal="人" fileName="group"/></td>
				</tr>
				<tr>
					<td width="267" height="410px;" valign="top">
						<center>
							<div id="etree" class="div_bd">
								<iframe id="sonFrame" frameborder="0" src="<%=iPath %>/grp_cliAddDepTree.jsp?lguserid=<%=lguserid %>"></iframe>
							</div>
							<div id="egroup" class="div_bd">
							</div>
							<div id="efile" class="div_bd">
								<div class="group_div1"></div>
								<input type="file" name="drfile" id="drfile" onchange="doupload()" />
								<div  class="btnClass3" id="fileBtn" ><emp:message key="group_ydbg_xzqz_text_uploadfile" defVal="上传文件" fileName="group"/></div>
								<!-- 
								<span id="sileSpan" >
									<emp:message key="group_ydbg_xzqz_text_fileformat" defVal="只支持上传.txt和.xls文件" fileName="group"/>
								</span> 
								-->
								<div class='remindMsg'>
									<p><emp:message key="group_ydbg_xzqz_text_fileformat1" defVal="1.批量添加只支持上传.txt/.xls/.xlsx文件。" fileName="group"/></p>
									<p><emp:message key="group_ydbg_xzqz_text_fileformat2" defVal="2.单个文件上传有效数据不能超过2000条。" fileName="group"/></p>
									<p><emp:message key="group_ydbg_xzqz_text_fileformat3" defVal="3.最多上传五个文件。" fileName="group"/></p>
									<p><emp:message key="group_ydbg_xzqz_text_fileformat4" defVal="4.错误格式数据(名字包含特殊符号等)将会被自动过滤。" fileName="group"/></p>
									<p><emp:message key="group_ydbg_xzqz_text_fileformat5" defVal="5.具体填写见格式图例(左为.txt文件，右为.xls/.xlsx文件)。" fileName="group"/></p>
								</div>
								<div id="upts"></div>
								<%--<input class="btnClass1" type="button" value="导入" onclick="doupload()"><br/> --%>
								<div class="formatText"><emp:message key="group_ydbg_xzqz_text_formatdes" defVal="格式说明" fileName="group"/></div>
								<div  class="formatInfo">
								<%--注：批量添加只支持上传.txt和.xls文件，.txt文档内容的格式为：姓名 号码<br/>
		 					   	姓名和号码以空格隔开，每个通讯录信息为一行。
		 					   	.xls文档内第一个单元格为姓名，第二个单元格为号码，每行为一条记录。
		 					   	错误格式数据将会被过滤 --%>
		 					   	<div class="pic_div">
		 					   		<div>.txt&nbsp;<emp:message key="group_ydbg_xzqz_text_document" defVal="文档" fileName="group"/></div>
		 					   		<img src="<%=commonPath%>/common/img/gp_upload_intro1_<%=empLangName%>.png"/>
		 					   	</div>
		 					   	<div class="pic_div">
		 					   		<div>.xls&nbsp;<emp:message key="group_ydbg_xzqz_text_document" defVal="文档" fileName="group"/></div>
		 					   		<img src="<%=commonPath%>/common/img/gp_upload_intro2_<%=empLangName%>.png"/>
		 					   	</div>
		 					   	<div class="group_div2"><emp:message key="group_ydbg_xzqz_text_filemaxsize" defVal="单个文件上传有效数据不能超过2000条" fileName="group"/></div>
		 					   	</div>
							</div>
							<div id="div_sel"  class="div_bd div_bg">
								<emp:message key="group_ydbg_ygtxlgl_text_memberlist" defVal="成员列表" fileName="group"/>：
							</div>
							<span class="group_display_none" id="showUserName"></span>
							<div class="div_bd" id="left_div">
								<select  multiple name="left" id="left" ondblclick="" size="9">
								</select>
							</div>
							<div class="group_div3">
								<input class="btnClass1" type="button" id="prepage" value="<emp:message key="group_common_text_prepage" defVal="上一页" fileName="group"/>"  onclick="javascript:prePage();">
								<input class="btnClass1" type="button" id="nextpage" value="<emp:message key="group_common_text_nextpage" defVal="下一页" fileName="group"/>"  onclick="javascript:nextPage();">
								&nbsp;&nbsp;&nbsp;&nbsp;<label id="pagecode">
								</label>
							</div>
						</center>	
					</td>
					<td width="60" align="center"  valign="middle">
						<%--<input class="btnClass1" type="button" id="toLefts" value="&gt;&gt;" onclick="javascript:moveallLeft();">
						<div style="height:15px;"></div> --%>
						<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="group_common_opt_add" defVal="添加" fileName="group"/>" onclick="javascript:moveLeft();">
						<div class="group_div4"></div>
						<%-- <input class="btnClass1" type="button" id="toRight" value="&lt;" onclick="javascript:moveRight();"> --%>
						<input class="btnClass1" type="button" id="toRights" value="<emp:message key="group_common_opt_delete" defVal="删除" fileName="group"/>" onclick="javascript:moveRight()">
					</td>
					<td width="226" valign="top" >
					<center>
					<%
					int width = 378;
					if(btnMap.get(menuCode+"-5")!=null) {
						width = 350;
						%>
						<div  class="div_bd div_bg" id="right_search">
							<input type=text" class="input_bd" name="adName" id="adName" maxlength="20" onkeyup = "checkText($(this))"
									value='<emp:message key="group_ydbg_ygtxlgl_text_name" defVal="姓名" fileName="group"/>' />
							
							<input type=text" class="input_bd" name="adPhone" id="adPhone" value='<emp:message key="group_ydbg_xzqz_text_phonenumber" defVal="手机号" fileName="group"/>' maxlength="21" onkeyup = "checkText($(this));phoneInputCtrl($(this))"
									/>
									<a id="btnAdd" href="javascript:doAdd()"></a>
						</div>
						<div  class="div_bd group_div5">
							<span class="ld div_bd" ><emp:message key="group_ydbg_xzqz_text_nikename" defVal="名称" fileName="group"/></span>
							<span class="md div_bd" ><emp:message key="group_ydbg_xzqz_text_number" defVal="号码" fileName="group"/></span>
							<span class="rd div_bd" ><emp:message key="group_ydbg_xzqz_text_src" defVal="来源" fileName="group"/></span>
						
						</div>
						<%}else{%>
							<div  class="div_bd group_div6">
							<span class="ld div_bd" ><emp:message key="group_ydbg_xzqz_text_nikename" defVal="名称" fileName="group"/></span>
							<span class="md div_bd" ><emp:message key="group_ydbg_xzqz_text_number" defVal="号码" fileName="group"/></span>
							<span class="rd div_bd" ><emp:message key="group_ydbg_xzqz_text_src" defVal="来源" fileName="group"/></span>
						</div>
						<%} %>
						<%
							if(skin.contains("frame4.0")){
						%>
							<div id="right" class="div_bd">
						<%
							}else{
						%>
							<div id="right" class="div_bd" style="	height: <%=width+4 %>px !important;height: <%=width %>px;border-top:none">
						<%} %>
						</div>
					</center>
					</td>
				</tr>
		</table>
		<script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/group_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=iPath%>/js/cliAddMember.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>
			
			$(":text").each(function(){
				$(this).attr("holder",$(this).val()).css("color","gray");
			}).focus(function(){
				var holder = $(this).attr("holder");
				var value = $.trim($(this).val());
				if(value == holder)
					$(this).val("");
			}).blur(function(){
				var holder = $(this).attr("holder");
				var value = $.trim($(this).val());
				if(value == "")
					$(this).val(holder);
			});
		</script>
	</body>
</html>