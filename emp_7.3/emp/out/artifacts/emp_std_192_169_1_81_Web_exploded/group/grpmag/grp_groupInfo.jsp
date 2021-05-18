<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	String menuCode = "1700-1500";
	
	String lguserid =request.getParameter("lguserid");

	String skin = session.getAttribute("stlyeSkin")==null?"default":
			(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="group_ydbg_xzqz_text_selectsendobj" defVal="选择发送对象" fileName="group"></emp:message></title>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7"/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/group.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/group.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
	</head>
	<body>
	<div id="gpName_div" class="div_bg">
		<input type=text" name="addGpName" id="addGpName" value="" maxlength="18"  onkeyup = "checkText($(this))"/>
		<label id="gpName_label" for="addGpName"><emp:message key="group_ydbg_xzqz_text_entergroupname" defVal="请输入群组名称" fileName="group"></emp:message></label>	
	</div>
			<input type="hidden" id="strUser" name="strUser" value=""/>
			<input type="hidden" id="ipath" value="<%=path%>"/>	
			<select style="display:none" id="tempOptions"></select>
			<select style="display:none" id="rightSelectTemp"></select>
			<select style="display:none" id="rightSelectTempAll"></select>
			<input type="hidden" id="depId" name="depId" value=""/>
			<input type="hidden" id="plook" name="plook" value="<%=plook %>"/>
			<input type="hidden" id="depName" name="depName" value=""/>
			<input type="hidden" id="epno" name="epno" value=""/>
			<input type="hidden" id="user" name="user" value=""/>
			<input type="hidden" id="addType" name="addType" value="3"/>
			<%-- 默认是新增分页信息 中第一页 --%>
			<input type="hidden" id="pageIndexAdd" value="1"/>
			<div style="height:5px;"></div>		
			<table border="0"  cellspacing="0" cellpadding="0" style="margin:0 auto;">
				<tr>
					<td style="font-size:12px; line-height:22px;height:24px; " align="left">
						<%--<input type="radio" value="1" name="chooseType" id="chooseType1" checked onclick="changeChooseType(1)"/>
						<label for="chooseType1">员工通讯录</label>&nbsp;&nbsp;&nbsp;
						<input type="radio" value="2" name="chooseType" id="chooseType2"  onclick="changeChooseType(2)"/>
						<label for="chooseType2">共享群组</label>&nbsp;&nbsp;&nbsp;
						<input type="radio" value="3" name="chooseType" id="chooseType3" onclick="changeChooseType(3)"/>
						<label for="chooseType3">文件导入</label> --%>
						<center><select id="chooseType" style="float:left;margin-left:5px;height:23px;margin-right: 2px;" onchange="changeChooseType(this.value)">
							<option value="1"><emp:message key="group_ydbg_xzqz_text_memberaddbook" defVal="员工通讯录" fileName="group"></emp:message></option>
							<option value="2"><emp:message key="group_ydbg_xzqz_text_personalgroup" defVal="个人群组" fileName="group"></emp:message></option>
							<% if(btnMap.get(menuCode+"-4")!=null) {  %><option value="3"><emp:message key="group_ydbg_xzqz_text_importfile" defVal="文件导入" fileName="group"></emp:message></option><%} %>
						</select>
						<input style="float:left;width:120px;height:20px;border-right:-1px; " type=text" name="epname" id="epname" class="input_bd;" value='<emp:message key="group_ydbg_xzqz_text_entername" defVal="请输入姓名" fileName="group"></emp:message>' maxlength="20" onkeyup = "checkText($(this))"/>
						
						<a  id="btnSearch"  href="javascript:dosearch()" style="margin-left:-1px;"></a>
						</center>
					</td>	
					<td></td>
					<td>&nbsp;&nbsp;<emp:message key="group_ydbg_xzqz_text_added" defVal="已添加" fileName="group"></emp:message><b id="numcount">0</b><emp:message key="group_ydbg_xzqz_text_peploe" defVal="人" fileName="group"></emp:message></td>
				</tr>
				<tr>
					<td width="267" height="410px;" valign="top">
						<center>
							<div id="etree" class="div_bd" style="height: 224px;width:257px;display:block">
								<iframe id="sonFrame" style="width: 257px; height: 224px;" frameborder="0" src="<%=iPath %>/grp_addrbookDepTree1.jsp?lguserid=<%=lguserid %>"></iframe>
							</div>
							<div id="egroup" class="div_bd" style="display:none;height: 224px;width:257px;overflow:hidden;">
							</div>
							<div id="efile" class="div_bd">
								<div style="height:10px;"></div>
								<input type="file" name="drfile" id="drfile" onchange="doupload()" />
								<div  class="btnClass3" id="fileBtn" ><emp:message key="group_ydbg_xzqz_text_uploadfile" defVal="上传文件" fileName="group"></emp:message></div>
								<!-- 
								<span id="sileSpan" >
									<emp:message key="group_ydbg_xzqz_text_fileformat" defVal="只支持上传.txt和.xls文件" fileName="group"/>
								</span> 
								-->
								<div class='remindMsg'>
									<p align="left"><emp:message key="group_ydbg_xzqz_text_fileformat1" defVal="1.批量添加只支持上传.txt/.xls/.xlsx文件。" fileName="group"/></p>
									<p align="left"><emp:message key="group_ydbg_xzqz_text_fileformat2" defVal="2.单个文件上传有效数据不能超过2000条。" fileName="group"/></p>
									<p align="left"><emp:message key="group_ydbg_xzqz_text_fileformat3" defVal="3.最多上传五个文件。" fileName="group"/></p>
									<p align="left"><emp:message key="group_ydbg_xzqz_text_fileformat4" defVal="4.错误格式数据(名字包含特殊符号等)将会被自动过滤。" fileName="group"/></p>
									<p align="left"><emp:message key="group_ydbg_xzqz_text_fileformat5" defVal="5.具体填写见格式图例(左为.txt文件，右为.xls/.xlsx文件)。" fileName="group"/></p>
								</div>
								<div id="upts"></div>
								<%--<input class="btnClass1" type="button" value="导入" onclick="doupload()"><br/> --%>
								<div class="formatText"><emp:message key="group_ydbg_xzqz_text_formatdes" defVal="格式说明" fileName="group"></emp:message></div>
								<div  class="formatInfo">
								<%--注：批量添加只支持上传.txt和.xls文件，.txt文档内容的格式为：姓名 号码<br/>
		 					   	姓名和号码以空格隔开，每个通讯录信息为一行。
		 					   	.xls文档内第一个单元格为姓名，第二个单元格为号码，每行为一条记录。
		 					   	错误格式数据将会被过滤 --%>
		 					   	<div class="pic_div">
		 					   		<div>.txt<emp:message key="group_ydbg_xzqz_text_document" defVal="文档" fileName="group"></emp:message></div>
		 					   		<img src="<%=skin %>/images/gp_upload_intro1.png"/>
		 					   	</div>
		 					   	<div class="pic_div">
		 					   		<div>.xls<emp:message key="group_ydbg_xzqz_text_document" defVal="文档" fileName="group"></emp:message></div>
		 					   		<img src="<%=skin %>/images/gp_upload_intro2.png"/>
		 					   	</div>
		 					   	<div style="float: left;text-align:center;width:99%"><emp:message key="group_ydbg_xzqz_text_filemaxsize" defVal="单个文件上传有效数据不能超过2000条" fileName="group"></emp:message></div>
		 					   	</div>
							</div>
							<div id="div_sel"  class="div_bd div_bg">
								&nbsp;&nbsp;<emp:message key="group_ydbg_ygtxlgl_text_memberlist" defVal="成员列表" fileName="group"></emp:message>：
							</div>
							<span style="display:none" id="showUserName"></span>
							<div class="div_bd" id="left_div">
								<select  multiple name="left" id="left" ondblclick="" size="9"
									style=" width: 263px; font-size: 12px;padding:2px;vertical-align:middle;margin:-4px -6px;">
								</select>
							</div>
							<div style="padding-top: 5px;">
								<input class="btnClass1" type="button" id="prepage"  style="visibility:hidden;" value="<emp:message key="group_common_text_prepage" defVal="上一页" fileName="group"></emp:message>"  onclick="javascript:prePage();">
								<input class="btnClass1" type="button" id="nextpage" style="visibility:hidden;" value="<emp:message key="group_common_text_nextpage" defVal="下一页" fileName="group"></emp:message>"  onclick="javascript:nextPage();">
								&nbsp;&nbsp;&nbsp;&nbsp;<label style="color: blue;font-size: 12px;"  id="pagecode">
								</label>
							</div>
						</center>	
					</td>
					<td width="60" align="center"  valign="middle">
						<%--<input class="btnClass1" type="button" id="toLefts" value="&gt;&gt;" onclick="javascript:moveallLeft();">
						<div style="height:15px;"></div> --%>
						<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="group_common_opt_add" defVal="添加" fileName="group"></emp:message>" onclick="javascript:moveLeft();">
						<div style="height:15px;"></div>
						<%-- <input class="btnClass1" type="button" id="toRight" value="&lt;" onclick="javascript:moveRight();"> --%>
						<input class="btnClass1" type="button" id="toRights" value="<emp:message key="group_common_opt_delete" defVal="删除" fileName="group"></emp:message>" onclick="javascript:moveRight()">
					</td>
					<td width="226" style="" valign="top" >
					<center>
					<%
					int width = 378;
					if(btnMap.get(menuCode+"-5")!=null) {
						width = 350;
						%>
						<div  class="div_bd div_bg" id="right_search">
							<input type=text" class="input_bd" name="adName" id="adName" value='<emp:message key="group_ydbg_ygtxlgl_text_name" defVal="姓名" fileName="group"></emp:message>' maxlength="20" onkeyup = "checkText($(this))"
									style="height:20px;line-height:20px;width:60px;border-right: 0px;" />
							
							<input type=text" class="input_bd" name="adPhone" id="adPhone" value='<emp:message key="group_ydbg_xzqz_text_phonenumber" defVal="手机号" fileName="group"></emp:message>' maxlength="11" onkeyup = "checkText($(this))"
									style="height:20px;line-height:20px;width:165px;" />
							
								<a id="btnAdd" href="javascript:doAdd()"></a>
						</div>
						<div  class="div_bd" style="height: 25px;line-height: 25px;width:286px;display:block;border-top:none">
							<span class="ld div_bd" style="height: 25px;line-height: 25px;border-bottom: none"><emp:message key="group_ydbg_xzqz_text_nikename" defVal="名称" fileName="group"></emp:message></span>
							<span class="md div_bd" style="height: 25px;line-height: 25px;border-bottom: none"><emp:message key="group_ydbg_xzqz_text_number" defVal="号码" fileName="group"></emp:message></span>
							<span class="ld div_bd" style="height: 25px;line-height: 25px;width:45px;_width:44px;border-bottom: none"><emp:message key="group_ydbg_xzqz_text_src" defVal="来源" fileName="group"></emp:message></span>
						</div>
						<%}else{%>
							<div  class="div_bd" style="height: 25px;line-height: 25px;width:286px;display:block">
							<span class="ld div_bd" style="height: 25px;line-height: 25px;border-bottom: none"><emp:message key="group_ydbg_xzqz_text_nikename" defVal="名称" fileName="group"></emp:message></span>
							<span class="md div_bd" style="height: 25px;line-height: 25px;border-bottom: none"><emp:message key="group_ydbg_xzqz_text_number" defVal="号码" fileName="group"></emp:message></span>
							<span class="ld div_bd" style="height: 25px;line-height: 25px;width:45px;_width:44px;border-bottom: none"><emp:message key="group_ydbg_xzqz_text_src" defVal="来源" fileName="group"></emp:message></span>
						</div>
						<%} %>
						
						<div id="right" class="div_bd" style="	height: <%=width+4 %>px !important;height: <%=width %>px;border-top: none">
						</div>
					</center>
					</td>
					</tr>
		</table>
		<script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/group_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=iPath%>/js/addGroupInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
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