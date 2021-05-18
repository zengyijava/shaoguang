<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.sms.LfBigFile"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");

	String actionPath = (String)request.getAttribute("actionPath");
	String menuCode = titleMap.get(actionPath.substring(4, 8) + "BigFileExport");
	menuCode = menuCode==null?"0-0-0":menuCode;

	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String httpUrl = StaticValue.getFileServerViewurl();

	@ SuppressWarnings("unchecked")
	List<LfBigFile> lfBigFiles = (List<LfBigFile>)request.getAttribute("lfBigFiles");
	String busIds = (String)request.getAttribute("busIds");
	//文件批次
	String pcid = request.getParameter("pcid");
	//操作员
	String userName = request.getParameter("userName");
	//处理状态  1.上传中 2.上传成功(有效号码、黑名单、重复号码、格式错误号码已处理完成) 3.上传失败
	String handleStatus = request.getParameter("handleStatus");
	//文件状态
	String fileStatus = request.getParameter("fileStatus");
	//开始时间
	String sendtime = request.getParameter("sendtime");
	//结束时间
	String recvtime = request.getParameter("recvtime");

	if(pcid==null){
		pcid = "";
	}
	if(userName==null){
		userName = "";
	}
	if(handleStatus==null){
		handleStatus = "";
	}
	if(fileStatus==null){
		fileStatus = "";
	}
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>sendSMS.html</title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<style type="text/css">
			body div#tooltip { position:absolute; z-index:1000; max-width:435px; _width:expression(this.scrollWidth > 435 ? "435px" : "auto"); width:auto; background:#A8CFF6; border:#FEFFD4 solid 1px; text-align:left; padding:6px;}
			body div#tooltip p { margin:0;padding:6; color:#FFFFFE;font:12px verdana,arial,sans-serif;}
			body div#tooltip p em { display:block;margin-top:3px; color:#f60;font-style:normal; font-weight:bold;}
			#content tr.peachpuff,#content tr.peachpuff td{background:#FFDAB9;}
		</style>
	</head>

	<body id="ssm_bigFileExport" class="ssm_bigFileExport">
		<div id="container" class="container">
			<!-- 当前位置 -->
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<!-- 内容开始 -->
			<div id="rContent" class="rContent">
			<!-- 表示请求的名称 -->
			<input type="hidden" name="path" id="path" value="<%=path %>">
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<input type="hidden" name="htmName" id="htmName" value="<%=actionPath %>">
			<input type="hidden" name="busIds" id="busIds" value="<%=busIds %>">
			<form name="pageForm" action="<%=actionPath%>?method=find" method="post" id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
                         <a id="exportCondition"><emp:message key="dxzs_xtnrqf_button_12bak" defVal="上传" fileName="dxzs"/></a>
					</div>
					<div id="condition">
						<table>

							<tbody>
								<tr>
								   <td><emp:message key="dxzs_xtnrqf_title_159bak" defVal="文件批次" fileName="dxzs" />：</td>
								   <td>
								      <input type="text" id="pcid" name ="pcid" style="width: 178px"  value="<%="".equals(pcid)?"":pcid%>" onkeyup="phoneInputCtrl($(this))" onpaste="phoneInputCtrl($(this))" >
								   </td>

									<td>
										<emp:message key="dxzs_xtnrqf_title_146bak" defVal="操作员" fileName="dxzs"/>：
									</td>
									<td>
								      <input type="text" id="userName" name ="userName" style="width: 178px"  value="<%="".equals(userName)?"":userName%>" maxlength="19">
								   </td>

									<td><emp:message key="dxzs_xtnrqf_title_148bak" defVal="处理状态" fileName="dxzs"/>：</td>
								    <td>
								    	<select name="handleStatus" id="handleStatus" style="width: 178px;">
								    		<option value=""><emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/></option>
								    		<option value="1" <%="1".equals(handleStatus)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_149bak" defVal="上传中" fileName="dxzs"/></option>
								    		<option value="2" <%="2".equals(handleStatus)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_150bak" defVal="上传成功" fileName="dxzs"/></option>
								    		<option value="3" <%="3".equals(handleStatus)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_151bak" defVal="上传失败  " fileName="dxzs"/></option>
											<option value="4" <%="4".equals(handleStatus)?"selected":"" %>>已提交发送</option>
								    	</select>
								    </td>
								    <td class="tdSer">
									       <center><a id="search"></a></center>
								    </td>
								</tr>
								<tr>
								    <td><emp:message key="dxzs_xtnrqf_title_148bak" defVal="文件状态" fileName="dxzs"/>：</td>
								    <td>
								    	<select name="fileStatus" id="fileStatus" style="width: 178px;">
								    		<option value=""><emp:message key="dxzs_xtnrqf_title_100bak" defVal="全部" fileName="dxzs"/></option>
								    		<option value="1" <%="1".equals(fileStatus)?"selected":"" %>>已启用</option>
								    		<option value="2" <%="2".equals(fileStatus)?"selected":"" %>>已禁用 </option>
								    	</select>
								    </td>

								    <td>
								      <emp:message key="dxzs_xtnrqf_title_160bak" defVal="上传时间" fileName="dxzs"/>：
								   </td>
								   <td>
								    	<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()"
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime">
								   </td>
								   <td><emp:message key="dxzs_xtnrqf_title_135bak" defVal="至" fileName="dxzs"/>：</td>
								   <td>
								      	<input type="text"
											style="cursor: pointer; width: 178px; background-color: white;"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=recvtime==null?"":recvtime %>"
											 id="recvtime" name="recvtime" >
								    </td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="dxzs_xtnrqf_title_146bak" defVal="文件批次" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_146bak" defVal="文件名称" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_143bak" defVal="文件个数" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_143bak" defVal="业务类型" fileName="dxzs"/>
								</th>

								<th>
									<emp:message key="dxzs_xtnrqf_title_99bak" defVal="提交号码数" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_158bak" defVal="有效号码数" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_2bak" defVal="黑名单号码数" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_159bak" defVal="重复号码数" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_160bak" defVal="格式非法数" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_25bak" defVal="上传时间" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_45bak" defVal="处理完成时间" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_45bak" defVal="操作员" fileName="dxzs"/>
								</th>
								 <th>
								         <emp:message key="dxzs_xtnrqf_title_12bak" defVal="处理状态" fileName="dxzs"/>
								 </th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_163bak" defVal="文件状态" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_163bak" defVal="无效号码" fileName="dxzs"/>
								</th>

							</tr>
						</thead>
						<tbody>
							<%
							if(lfBigFiles != null && lfBigFiles.size()>0)
							{
								for(LfBigFile lfBigFile : lfBigFiles)
								{
							%>
									<td style="text-align: center;">
										<%=lfBigFile.getId()%>
									</td>
									<td class="text-align: center;">
										<%=lfBigFile.getFileName() %>
									</td>
									<td style="text-align: center;">
										<xmp><%=lfBigFile.getUploadNum() %></xmp>
									</td>
									<td class="text-align: center;">
										<%=lfBigFile.getBusName()%>(<%=lfBigFile.getBusCode()%>)
									</td>
									<td>
										<%=lfBigFile.getHandleStatus()==1?"--":lfBigFile.getSubCount() %>
									</td>
									<td>
										<%=lfBigFile.getHandleStatus()==1?"--":lfBigFile.getEffCount() %>
									</td>
									<td>
										<%
											if(lfBigFile.getHandleStatus()==1){
										%>
										--
										<%
											}else if(lfBigFile.getHandleStatus()==2){
										%>
										<%=lfBigFile.getBlaCount() %>
										<%
											}else{
										%>
										--
										<%
											}
										 %>
									</td>
									<td>
										<%
											if(lfBigFile.getHandleStatus()==1){
										%>
										--
										<%
											}else if(lfBigFile.getHandleStatus()==2){
										%>
											<%=lfBigFile.getRepCount()%>
										<%
											}else{
										%>
										--
										<%
											}
										 %>
									</td>
									<td>
										<%
											if(lfBigFile.getHandleStatus()==1){
										%>
										--
										<%
											}else if(lfBigFile.getHandleStatus()==2){
										%>
											<%=lfBigFile.getErrCount() %>
										<%
											}else{
										 %>
										--
										<%
											}
										 %>
									</td>
									<td>
										<%=lfBigFile.getCreateTime().toString().substring(0, 19)%>
									</td>
									<td>
										<%
											if(lfBigFile.getHandleStatus()==1){
										%>
										--
										<%
											}else{
										%>
										<%=lfBigFile.getUpdateTime().toString().substring(0, 19)%>
										<%
											}
										 %>
									</td>
									<td>
										<%=lfBigFile.getUserName()%>
									</td>
									<td>
											<%
											if(lfBigFile.getHandleStatus()!=null&&lfBigFile.getHandleStatus()==1){
											%>
												上传中
											<%
												}else if(lfBigFile.getHandleStatus()==2){
											%>
												上传成功
											<%
												}else if(lfBigFile.getHandleStatus()==3){
											%>
										<%
											String xmessage = lfBigFile.getRemark() == null?"" : lfBigFile.getRemark();
										%>
										<a   onclick="modify(this)" title="<%=xmessage%>">

											<label style="display:none"><xmp><%=xmessage %></xmp></label>
											<xmp>上传失败</xmp></a>
											<%
												}else{
											 %>
												已提交发送
											<%
												}
											%>
									</td>
									<td>
											<%
											if(lfBigFile.getFileStatus()!=null&&lfBigFile.getFileStatus()==1){
											%>

												 <a id="fileStatus<%=lfBigFile.getId()%>"  onclick="changeFileStatus('<%=lfBigFile.getId()%>','<%=lfBigFile.getFileStatus()%>')">已启用</a>
											<%
												}else if(lfBigFile.getFileStatus()==2){
											%>
											 <a id="fileStatus<%=lfBigFile.getId()%>" onclick="changeFileStatus('<%=lfBigFile.getId()%>','<%=lfBigFile.getFileStatus()%>')">已禁用</a>
											<%
												}else{
											%>
												--
											<%
												}
											 %>

									</td>
									<td>
										<% if(lfBigFile.getHandleStatus()!=null&&lfBigFile.getHandleStatus()==2&&!lfBigFile.getSubCount().equals(lfBigFile.getEffCount())){%>
										<a href="javascript:downloadFilesOuter('<%=lfBigFile.getBadUrl()==null?"":lfBigFile.getBadUrl()%>','<%=path%>')">下载</a>
										<%
												}else{
											%>
												--
											<%
												}
											 %>
									</td>

						</tr>
						<%
								}
							}else{
						%>
						<tr><td align="center" colspan="16"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="16">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="smssendparams" class="hidden"></div>
				</form>
			</div>

			<!-- foot开始 -->
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
				</div>
		</div>
		<div id="modify" title="错误原因">
			<table>
				<thead>
				<tr class="modify_tr1">
					<td>
						<label id="msgss"></label>
					</td>
				</tr>
				<tr class="modify_tr2">
					<td></td>
				</tr>
				</thead>
			</table>
		</div>
    <div class="clear"></div>

    <%--导入模板--%>
	<div id="tempDiv" class="dxzs_tempDiv" title="<emp:message key='dxzs_xtnrqf_title_64bak' defVal='模板导入' fileName='dxzs'/>" style="display: none;">
		<iframe id="tempFrame" name="tempFrame" width="100%" height="360px" class="dxzs_tempFrame" marginwidth="0" scrolling="auto" frameborder="no" src ="<%=actionPath%>?method=toExport&busIds=<%=busIds%>"></iframe>
		<div class="dxzs_div7"  align="center" id="tempButton" style="margin-bottom: 10px;">
			<input id="subSend" type="button" onclick="tempSure()" value="上传" class="btnClass5 mr23"/>
			<input id="cancel" type="button" onclick="tempCancel()" value="关闭" class="btnClass6"/>
		</div>
	</div>

    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		 <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		 <script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/ssm_bigFileExport.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
			$(document).ready(function(){

			    closeTreeFun(["dropMenu2","dropMenu"]);

				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
                $('#handleStatus').isSearchSelect({'width':'180','isInput':true,'zindex':0});
                $('#fileStatus').isSearchSelect({'width':'180','isInput':true,'zindex':0});

				$("#exportCondition").click(function()
				{
					$("#subSend").attr("disabled","");
					$("#tempFrame").attr("src","ssm_dxzsBigFileExport.htm?method=toExport");
				 	$("#tempDiv").dialog("open");
				});

				$("#subSend").attr("disabled","");
		});
		</script>
	</body>
</html>

