<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lguserid = request.getParameter("lguserid");
	String corpCode = request.getParameter("lgcorpcode");
	String chooseType = request.getParameter("chooseType");
	String tmId = request.getParameter("tmId");
	String tmName = request.getParameter("tmName");
	String groupName = request.getParameter("groupName");
	if(tmName != null){
		tmName=StringEscapeUtils.unescapeHtml(tmName);
		for(int x=0;x<5;x++){
			tmName=URLDecoder.decode(tmName,"UTF-8");
		}
		//tmName = new String(tmName .getBytes("iso8859-1"),"utf-8");
	}
	if(groupName != null){
		groupName=StringEscapeUtils.unescapeHtml(groupName);
		for(int x=0;x<5;x++){
			groupName = URLDecoder.decode(groupName,"UTF-8");
		}
	}
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	int plook = (btnMap.get(StaticValue.PHONE_LOOK_CODE) == null) ? 0 : 1;
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title>选择人员</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=path%>/common/css/selectUserInfo.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" />
		  <style type="text/css">
            input, textarea { font-family: Helvetica, Arial; color: #000;}
            .placeholder {color: #aaa;}
            <%if(StaticValue.ZH_HK.equals(langName)){%>
                #button span{
                    letter-spacing: 0.5px;
                }
            <%}%>
        </style>
	</head>
	<body class="selectUserInfo" id="selectUserInfo">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<center>
			<div class="selectContent">
			<form method="post" id="selectForm">
				<input type="hidden" id="chooseType" value="<%=chooseType%>"/>
				<input type="hidden" id="ipath" value="<%=request.getContextPath()%>"/>
				<input type="hidden" id="pathUrl" value="<%=path%>">
				<input type="hidden" id="lguserid" value="<%=lguserid%>"/>
				<input type="hidden" id="corpCode" value="<%=corpCode%>"/>
				<input type="hidden" id="tmId" value="<%=tmId%>"/>
				<input type="hidden" id="tmName" value="<%=tmName%>"/>
				<input type="hidden" id="groupName" value="<%=groupName%>"/>
				<%-- 这里是选择的是  1是员工 2客户3群组 --%>
				<input type="hidden" id="chooseType" value="1"/>
				<%-- 是否通过群组选择或修改接口进入 1为是 0为否--%>
				<input type="hidden" id="isGroupEdit" value="0"/>
				<%-- 这里是点击的  1是员工 2客户 3群组  的ID --%>
				<input type="hidden" id="choiceId" value=""/>
				<%-- 这里是点击的  1是员工 2客户  3群组  的名称 --%>
				<input type="hidden" id="choiceName" value=""/>
				<%--操作员的GUID--%>
				<input type="hidden" id="userGuid" value=""/>
				<%-- 员工机构IDS --%>
				<input type="hidden" id="empDepIdsStrs" value=""/>
				<%-- 客户机构IDS --%>
				<input type="hidden" id="cliDepIdsStrs" value=""/>
				<%-- 群组IDS --%>
				<input type="hidden" id="groupIdsStrs" value=""/>
				<%-- 默认是获取分页信息 中第一页 --%>
				<input type="hidden" id="pageIndex" value="1"/>
				<%--选择的是 1员工群组 2客户群组 --%>
				<input type="hidden" id="qztype" value="1"/>
				<%--选择的是 1共享群组 0个人群组 --%>
				<input type="hidden" id="shareType" value="0"/>
				<%--代表的是员工IDS --%>
				<input type="hidden" id="employeeIds" value=""/>
				<%--代表的是客户IDS--%>
				<input type="hidden" id="clientIds" value=""/>
				<%--代表的是外部人员IDS --%>
				<input type="hidden" id="malistIds" value=""/>
				<%--代表的是套餐IDS --%>
				<input type="hidden" id="taocanIds" value=""/>
				<%--代表的是签约用户IDS --%>
				<input type="hidden" id="signClientIds" value=""/>
				<%--代表的是客户属性的属性值的IDS --%>
				<input type="hidden" id="cusFieldValueIds" value=""/>
				<%--代表的是客户属性值的IDS --%>
				<input type="hidden" id="cusFieldIds" value=""/>
				<%--号码字符串(除开签约用户) --%>
				<input type="hidden" id="moblieStrs" value=""/>
				<%--签约用户手机号集合 --%>
				<input type="hidden" id="signClientPhoneStr" value=""/>
				<%--群组修改---群组自建--%>
				<input type="hidden" id="qzStr" name="qzStr" value=","/>
				<%--群组修改---手动添加--%>
				<input type="hidden" id="zjStr" name="zjStr" value=","/>
				<%--群组修改---员工/客户--%>
				<input type="hidden" id="ygStr" name="ygStr" value=","/>
				<%--群组修改---共享--%>
				<input type="hidden" id="gxStr" name="gxStr" value=","/>
				<%--手机预览权限--%>
				<input type="hidden" id="plook" name="plook" value="<%=plook%>"/>
				<%--用于文件上传时的回显--%>
				<input type="hidden" id="fileInputTemp" value=""/>
                <%--用于记录原本的群组名字--%>
                <input type="hidden" id="curName" value=""/>
				<%--用于记录群组加密Id--%>
				<input type="hidden" id="curId" value=""/>
				<%--套餐Id--%>
				<input type="hidden" id="tcCode" value=""/>
				<%--界面提示 --%>
				<input type="hidden" id="viewDepNoBody" value="继续导入"/>
				<input type="hidden" id="viewDepIsExist" value="号码文件"/>
				<%--选择 输入姓名 总人数--%>
				<%--输入群组名称--%>
				<div class="groupNameInput div_bg">
					<span><emp:message fileName="common" key="common_group_name" defVal="群组名称：" /></span><input placeholder='  <emp:message fileName="common" key="common_placeholder_groupname" defVal="请输入群组名称" />' type="text" id="addGpName" class="addGpName" value="" maxlength="18"/>
				</div>
				<%--模板Id 与 模板名字--%>
				<div id="tempInfo">
					<span><emp:message fileName="common" key="common_template_name" defVal="模板名称：" /></span><input type="text" id="tempName" class="tempName" value="" readonly/>
					<span><emp:message fileName="common" key="common_template_id" defVal="模板Id：" /></span><input type="text" id="tempId" class="tempId" value="" readonly/>
				</div>
				<div class="chooseBox">
					<select name="choose_Type" id="choose_Type" onchange="choiceTree()"></select>
                    <div class="searchNameWrapper">
                        <input type="text" name="searchName" class="lostFocus" id="searchName" placeholder='  <emp:message fileName="common" key="common_placeholder_username" defVal="请输入姓名" />' maxlength="20">
                        <div onclick="searchName()" id="searchNameButton" class="green-btn">
                            <img class="search_img" src="<%=path%>/common/img/search_icon.png" alt=""/>
                        </div>
                    </div>
                    <div id="fileUploadInput" class="fileUploadInput a-upload">
                        <i class="file-icon"></i>
                        <span id="fileInput">
							<input type="file" name="uploadFile" id="uploadFile" value="" onchange="uploadGroupFile()"/>
							<emp:message fileName="common" key="common_import_file" defVal="导入文件" />
						</span>
                    </div>
                    <div id="manCountOuter"><emp:message fileName="common" key="common_selected_total2" defVal="所选总人数："/>(<label id="manCount">0</label>)</div>
				</div>
				<%--树形图显示--%>
				<div id="etree"  class="dept rollBak">
					<iframe id="sonFrame" frameborder="0" src=""></iframe>
				</div>
				<%--成员列表--%>
				<div class="showUserName div_bd" id="showName">
					<div class="shownameDiv div_bd">
						<span id="showUserName" class="title_bg"><emp:message fileName="common" key="common_member_list2" defVal="成员列表：" /><a id="selectAllMem" val="1" onclick="selectAllMem(this)"><emp:message fileName="common" key="common_select_all" defVal="全选" /></a></span>
					</div>
					<div id="left" onfocus="treeLoseFocus()" class="left_select_choose rollBak"></div>
					<div id="pageInfo">
						<input class="btnClass1" type="button" id="prepage" value="<emp:message fileName="common" key="common_previous" defVal="上一页" />"  onclick="pageInfo('pre');">
						<input class="btnClass1" type="button" id="nextpage" value="<emp:message fileName="common" key="common_next" defVal="下一页" />"  onclick="pageInfo('next');">
						&nbsp;&nbsp;&nbsp;&nbsp;
						<label id="pagecode"></label>
					</div>
				</div>
				<%--选择 删除按钮--%>
				<div id="button">
					<div class="btnClass1 toLeft" id="toLeft" onclick="router();"><span><emp:message fileName="common" key="common_option" defVal="选择" /></span></div>
					<br/>
					<div class="btnClass1 toRight" id="toRight" onclick="moveOut();"><span><emp:message fileName="common" key="common_delete" defVal="删除" /></span></div>
				</div>
				<%--phoneAndNameInput--%>
				<div class="phoneAndNameInputWrapper">
					<div class="phoneAndNameInputOutter">
						<div class="phoneAndNameInput">
							<input type="text" value="" placeholder="   姓名" class="addName" id="addName" maxlength="20"/>
							<img class="separate_icon" src="<%=path%>/common/img/separate_icon.png"/>
							<input type="text" value="" placeholder="   手机号" class="addPhone" id="addPhone" maxlength="21" onkeyup="phoneInputCtrl(this)"/>
						</div>
						<img class="addIcon" src="<%=path%>/common/img/addMember_icon.png" alt="添加" onclick="addGroupMember()"/>
					</div>
					<div class="phoneAndNameTable">
						<table>
							<tr>
								<td class="border_right" width="35%"><emp:message fileName="common" key="common_name" defVal="名称" /></td>
								<td class="border_right"><emp:message fileName="common" key="common_number" defVal="号码" /></td>
								<td width="20%"><emp:message fileName="common" key="common_source" defVal="来源" /></td>
							</tr>
						</table>
					</div>
				</div>
				<%--已选择人员名单--%>
				<div class="showchoiceDiv div_bd rollBak">
					<select multiple name="right" id="right" size="27" style="display:none;"></select>
					<div id="getChooseMan"></div>
				</div>
			</form>
		</div>
		</center>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=path%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path%>/common/js/jquery_Ul_Send_cx.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=path%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=path%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=path%>/common/js/jquery.placeholder.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=path%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
    <script type="text/javascript" src="<%=path%>/common/js/selectUserInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript">
	 $(function() {
                $('input, textarea').placeholder();
            });
	</script>
	</body>
</html>