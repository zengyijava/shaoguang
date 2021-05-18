<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.entity.client.LfClient"%>
<%@page import="com.montnets.emp.client.vo.LfClientVo"%>
<%@page import="com.montnets.emp.entity.client.LfCustField"%>
<%@page import="com.montnets.emp.entity.client.LfCustFieldValue"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("addrBook");
	String company=(String)request.getAttribute("company");
	@ SuppressWarnings("unchecked")
	Map<LfCustField, List<LfCustFieldValue>> map1=(Map<LfCustField, List<LfCustFieldValue>>)request.getAttribute("map1");
	@ SuppressWarnings("unchecked")
	Map<LfCustField, List<LfCustFieldValue>> map2=(Map<LfCustField, List<LfCustFieldValue>>)request.getAttribute("map2");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
 	String filepath = (String)request.getAttribute("filepath");
 	
 	String lguserid = (String)request.getAttribute("lguserid");
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
 %>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=commonPath %>/client/climan/css/uploadFile.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/cli_addClient.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_TongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<style type="text/css">
			div#tab{width: 680px;padding-left: 16px;}
			#sysTable  tr  td:nth-child(1){width: 135px;}
		</style>
		<%}%>
	</head>
	<body id="cli_addClient" onload="show('<%=request.getAttribute("result") %>','<%=request.getAttribute("path") %>','<%=request.getAttribute("isexiststr") %>')" >
		<input type="hidden" id="pathUrl" value="<%=path %>" />
		<input type="hidden" id="inheritPath" value="<%=inheritPath %>" />
		<input type="hidden" id="bookType"  value="client"/>
		<input type="hidden" id="checkUrl" value="<%=path %>/cli_addrBook.htm?method=checkBook&add=a" />
		<%--处理 客户手机号码是否合法 --%>
		<input type="hidden" id="checkClient" value="<%=path %>/cli_addrBook.htm?method=checkClient" />
		<input type="hidden" id="actionType"  value="add"/>
		<div class="client_display_none" id="hiddenValueDiv"></div>
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%              		
				if(btnMap.get(menuCode+"-1")!=null)                       		
				{                        	
			%>
				<div class="titletop">
					<table class="titletop_table">
						<tr>
							<td class="titletop_td">
								<emp:message key="client_khtxlgl_kftxl_text_addclient" defVal="添加客户" fileName="client"></emp:message>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:location.href='<%=path %>/cli_addrBook.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val()">&larr;&nbsp;<emp:message key="client_common_opt_gobak" defVal="返回上一级" fileName="client"></emp:message></font>
							</td>
						</tr>
					</table>
				</div>
				
				<div id="tab">
				    <table class="client_table1">  
				      <tr align="center">
				      <td id="addone"  class="infotd1" onclick="javascript:setFormName('addForm');changeinfo(1);"><emp:message key="client_khtxlgl_kftxl_text_addsingle" defVal="单个添加" fileName="client"></emp:message></td>
				      <td id="addall" class="infotd2" onclick="javascript:setFormName('uploadForm');changeinfo(2);"><emp:message key="client_khtxlgl_kftxl_text_addbatch" defVal="批量导入" fileName="client"></emp:message></td>
				      </tr>
				    </table> 
				     <div id="addoneDiv" class="block">
	                   <center>
						<form action="<%=path %>/cli_addrBook.htm?method=addBookcd" method="post" id="addForm" name="addForm" >
						<input type="hidden" id="bookId" name="bookId" value=''/>
							<table id="sysTable">
									<tr align="left">
										<td><span><emp:message key="client_khtxlgl_kftxl_text_clientnumber" defVal="客户号" fileName="client"></emp:message>：</span></td>
										<td>
											<label>
											<input type="text" name="clientCode" id="clientCode" onkeyup="this.value=this.value.replace(/[']+/img,'')"  value='' maxlength="20" class="input_book input_bd" />
											</label>
										</td>
									</tr>
									<tr align="left">	
										<td ><span><emp:message key="client_khtxlgl_kftxl_text_name" defVal="姓名" fileName="client"></emp:message>：</span></td>
										<td >
											<label>
											<input type="text" name="cName" id="cName" onkeyup="validateInput(this)" value='' maxlength="60" class="input_book input_bd "/>
											</label>
											<font class="client_color_red">&nbsp;*</font>
										</td>
									</tr>
									<tr align="left">
										<td ><span><emp:message key="client_khtxlgl_kftxl_text_phone" defVal="手机" fileName="client"></emp:message>：</span></td>
										<td >
											<label>
												<input type="text" name="mobile" id="mobile" onkeyup="phoneInputCtrl($(this))" value='' maxlength="21" class="input_book input_bd"/>
											</label><font class="client_color_red">&nbsp;*</font>
										</td>
									</tr>
									<tr align="left">
										<td><span><emp:message key="client_khtxlgl_kftxl_text_duties" defVal="职务" fileName="client"></emp:message>：</span></td>
										<td>
											<label>
												<input type="text" name="job" id="job"  value='' maxlength="20" class="input_book input_bd"/>
											</label>
										</td>
									</tr>
									<tr align="left">
										<td ><span><emp:message key="client_khtxlgl_kftxl_text_affiliation" defVal="所属机构" fileName="client"></emp:message>：</span></td>
										<td >
											<input type="hidden" name="depId" id="depId" value=''/>
											<div class="client_div1">
												<input id="depNam" onclick="javascript:showMenu('addForm');"  name="depNam" type="text" readonly value='<emp:message key="client_khtxlgl_kftxl_text_clickselectorg" defVal="点击选择机构" fileName="client"></emp:message>' class="input_book1 input_bd treeInput"/>
												<a id="ssdep" onclick="javascript:showMenu('addForm');"  ></a><font class="client_color_red">&nbsp;*</font>
											</div>
											<div id="dropMenu" >
												<div class="client_div2">
													<input type="button" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" class="btnClass1 mr23" onclick="javascript:zTreeOnClickOK();"/>
													<input type="button" value="<emp:message key="client_common_opt_emptiy" defVal="清空" fileName="client"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect();"/>
												</div>
												<ul id="dropdownMenu" class="tree">
												</ul>
											</div>
										</td>
									</tr>
									<tr align="left">
										<td><span><emp:message key="client_khtxlgl_kftxl_text_industry" defVal="行业" fileName="client"></emp:message>：</span></td><td>
										<label>
											<input type="text" name="pro" maxlength="20"  id="pro" value="" class="input_book input_bd"/>
										</label>
										</td>
									</tr>
									<tr align="left">
										<td><span><emp:message key="client_khtxlgl_kftxl_text_accountmanager" defVal="客户经理" fileName="client"></emp:message>：</span></td><td>
											<label>
												<input type="text" name="eName" id="eName" maxlength="20"  value="" class="input_book input_bd"/>
											</label>
										</td>
									</tr>
									<tr align="left">
										<td ><span><emp:message key="client_khtxlgl_kftxl_text_sex" defVal="性别" fileName="client"></emp:message>：</span></td>
										<td >
											<label>
												<select name="sex" id="sex" class="input_book input_bd">
													<option value="2"><emp:message key="client_khtxlgl_kftxl_text_unknown" defVal="未知" fileName="client"></emp:message></option>
													<option value="1" ><emp:message key="client_khtxlgl_kftxl_text_man" defVal="男" fileName="client"></emp:message></option>
													<option value="0" ><emp:message key="client_khtxlgl_kftxl_text_woman" defVal="女" fileName="client"></emp:message></option>
												</select>
											</label>
										</td>
									</tr>
									<tr align="left">
										<td ><span><emp:message key="client_khtxlgl_kftxl_text_birthday" defVal="生日" fileName="client"></emp:message>：</span></td>
										<td ><label>
											<input type="text" value='' id="birth" name="birth" class="Wdate input_bd" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%yyyy-%MM-%dd'})" class="input_book">
										</label></td>
									</tr>
									<tr align="left">
										<td ><span>QQ：</span></td>
										<td ><label>
											<input type="text" name="qq" id="qq"
												 onkeyup="numberControl($(this))" value='' maxlength="12" class="input_book input_bd"/>
											</label>
										</td>
								   </tr>
									<tr align="left">
										<td ><span>E-mail：</span></td>
										<td ><label>
											<input type="text" name="EMail" id="EMail" value='' class="input_book input_bd"  maxlength="32"/>
											</label>
										</td>
								    </tr>

									<tr align="left">
										<td ><span>MSN：</span></td>
										<td ><label>
											<input type="text" name="msn" id="msn" value='' maxlength="21" class="input_book input_bd"/>
										</label></td>
									</tr>
									<tr align="left">	
										<td ><span><emp:message key="client_khtxlgl_kftxl_text_landline" defVal="座机" fileName="client"></emp:message>：</span></td>
										<td ><label>
											<input type="text" name="oph" id="oph" onkeyup="numberControl($(this))" value='' maxlength="64" class="input_book input_bd"/>
										</label></td>
									</tr>
									<tr align="left">
										<td><span><emp:message key="client_khtxlgl_kftxl_text_belongarea" defVal="所属区域" fileName="client"></emp:message>：</span></td><td>
											<label>
												<input type="text" name="area" id="area" value="" maxlength="20" class="input_book input_bd"/>
											</label>
										</td>
									</tr>
									<tr align="left">
										<td ><span><emp:message key="client_khtxlgl_kftxl_text_clientdes" defVal="客户描述" fileName="client"></emp:message>：</span></td>
										<td >
											<label>
												<input type="text" name="comm" id="comm" value='' class="input_book input_bd" maxlength="20" maxlength="64" />
											</label>
										</td>
									</tr>
									<%              		
									if(map1!=null && map1.size()>0)                       		
									{ 
										int i=0;                  
										for (Iterator it =  map1.keySet().iterator();it.hasNext();)
										{
										    LfCustField key1 = (LfCustField)it.next();
										    List<LfCustFieldValue> value1=map1.get(key1);
									%>
									<tr align="left" class="dxkf_tr1">
										<td>
											<span>
												<%=key1.getField_Name().replace("<","&lt;").replace(">","&gt;")%>：
											</span>
										</td>
										<td>
											<label>
												<select name="<%=key1.getField_Name()%>" id="<%=key1.getField_Name()%>" class="input_bd client_select">
													<option value=""><emp:message key="client_common_text_select" defVal="请选择" fileName="client"></emp:message></option>
													<% 	for(int s=0;s<value1.size();s++){ 
															LfCustFieldValue fc=value1.get(s);
													%>
														<option value="<%=fc.getId()%>">
															<%=fc.getField_Value().replace("<","&lt;").replace(">","&gt;")%>
														</option>
													<%
														} 
													%>
												</select>
											</label>
										</td>
									</tr>
									<%
										}
									}
									%>
									<%
										if(map2!=null && map2.size()>0){
									    for(Iterator it =  map2.keySet().iterator();it.hasNext();)
										{
										  
										    LfCustField key2 = (LfCustField)it.next();
										    List<LfCustFieldValue> value2=map2.get(key2);
									%>
										<tr align="left" class="dxkf_tr1">
											<td>
												<span>
													<%=key2.getField_Name().replace("<","&lt;").replace(">","&gt;")%>：
												</span>
											</td>
											<td>
												<%
													for(int sk=0;sk<value2.size();sk++){ 
												      LfCustFieldValue fc=value2.get(sk);
												%>
													  <input type="checkbox" name="<%=key2.getField_Name()%>" value="<%=fc.getId()%>"/>
													  	&nbsp;<%=fc.getField_Value().replace("<","&lt;").replace(">","&gt;")%>
													  	&nbsp;&nbsp;
												<%
													}
												%>
											</td>
										</tr>
									<%
									  }
									}
									%>
									
							</table>
							<div align="right" class="client_div3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<input type="button" name="button" id="button" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" class="btnClass5 mr23"  onclick="javascript:doSubClient()"/>
											<input type="button" onclick="javascript:location.href='<%=path %>/cli_addrBook.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val()+'&skip=ture'"	name="button2"  class="btnClass6" id="button2" value="<emp:message key="client_common_opt_back" defVal="返回" fileName="client"></emp:message>" />
	                         		&nbsp;&nbsp;&nbsp;&nbsp;
									</div>
						</form>
						</center>
					</div>
					<div id="addallDiv">
					  
							<form action="<%=path %>/cli_addrBook.htm?method=uploadBook" method="post" enctype="multipart/form-data" id="uploadForm" name="uploadForm">
								<center>
								<iframe name="hidden_iframe" id="hidden_iframe" class="client_display_none"></iframe>
								<input type="hidden" id="bookId" name="bookId" value=''/>
								<table id="sysTable" >
									<thead>
										<tr align="left">
											<td width="20%"><span><emp:message key="client_khtxlgl_kftxl_text_affiliation" defVal="所属机构" fileName="client"></emp:message>：</span></td>
											<td>
												<input type="hidden" name="depId" id="depId" value=''/>
										        <div  class="client_div1">
												    <input id="depNam" class="input_bd treeInput"  onclick="javascript:showMenu('uploadForm');"  name="depNam" type="text" readonly value='<emp:message key="client_khtxlgl_kftxl_text_clickselectorg" defVal="点击选择机构" fileName="client"></emp:message>'/>
									                <a id="ssdep" onclick="javascript:showMenu('uploadForm');"  ></a><font class="client_color_red">&nbsp;*</font>
												</div>
												<div id="dropMenu">
													<div class="client_div2">
														<input type="button" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK();"/>&nbsp;&nbsp;
														<input type="button" value="<emp:message key="client_common_opt_emptiy" defVal="清空" fileName="client"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect();"/>
													</div>	
													<ul id="dropdownMenu" class="tree">
													</ul>
												</div>
											</td>
										</tr>
										<tr align="left">
											<td><span><emp:message key="client_common_opt_uploadfile" defVal="选择上传文件" fileName="client"></emp:message>：</span></td>
											<td>
												<a href="javascript:" class="a-upload">
													<input type="file" name="uploadFile" id="uploadFile" onkeyup="numberControl($(this))" value='quose' maxlength="11" <%--onchange="showFileName();"--%>/>
													<emp:message key="ydwx_wxfs_dtwxfs_schwj" defVal="上传文件" fileName="ydwx"></emp:message>
												</a>
												<span class="showFileName"></span>
												<font class="client_color_red">&nbsp;*</font>
											</td>
										</tr>
										<tr align="left">
										<td></td>
										<td><a id="file" href="javascript:exportAll();"><emp:message key="client_khtxlgl_kftxl_text_downmodel" defVal="支持Excel文件的上传。点击下载上传模板" fileName="client"></emp:message></a></td>
										</tr>
										<tr align="left" class="client_display_none">
											<td><span><emp:message key="client_khtxlgl_kftxl_text_filterheavy" defVal="过滤重号" fileName="client"></emp:message>：</span></td>
											<td>	
												<input type="radio" value="1" name="checkFlag" checked/><emp:message key="client_khtxlgl_kftxl_text_filter" defVal="过滤" fileName="client"></emp:message><input type="radio" value="2" name="checkFlag" ><emp:message key="client_khtxlgl_kftxl_text_notfilter" defVal="不过滤" fileName="client"></emp:message>
												<font class="client_color_red">&nbsp;*</font><font class="zhu"><emp:message key="client_khtxlgl_kftxl_text_selectfilter" defVal="选择过滤，则电话号码相同的记录会被过滤掉" fileName="client"></emp:message></font>
											</td>
										</tr>
										<tr align="left">
											<td colspan="2">	
												<font class="client_color_red">&nbsp;*</font><font class="zhu"><emp:message key="client_khtxlgl_kftxl_text_automaticupdates" defVal="同一机构中姓名和手机号码相同的记录系统将自动更新" fileName="client"></emp:message></font><br/>
												<font class="client_color_red">&nbsp;*</font><font class="zhu"><emp:message key="client_khtxlgl_kftxl_text_filemaxsize" defVal="07版excel文件单次最多上传20万数据" fileName="client"></emp:message></font><br/>
												<font class="zhu"><emp:message key="client_khtxlgl_kftxl_text_usermax" defVal="客户通讯录适用于企业管理会员、客户资料，无设上限，建议控制在300万个客户以内。" fileName="client"></emp:message></font>
											</td>
										</tr>
										
									</thead>
								</table>
								
							
							</center>
							<div class="client_div4" align="right">
								  <input type="button" name="button1" id="button1"  class="btnClass5 mr23" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" onclick="javascript:doUpload()" />
								  <input type="button" onclick="javascript:location.href='<%=path %>/cli_addrBook.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val()" class="btnClass6" value="<emp:message key="client_common_opt_back" defVal="返回" fileName="client"></emp:message>" />
<%--								  <input type="reset"	name="button2" id="button2"  class="btnClass6" value="重置" />--%>
								</div>
								</form>
					</div>
				</div>
			<div class="clear"></div>
		<%
		}
	%>
			</div>
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
    
    <div id="exportDiv" title="<emp:message key="client_khtxlgl_kftxl_text_exportmodel" defVal="导出excel模板" fileName="client"></emp:message>"><span class="client_span1">
			<input id="excelBut" class="btnClass4" type="button" value="<emp:message key="client_khtxlgl_kftxl_text_2003model" defVal="2003格式模板" fileName="client"></emp:message>" onclick="javascript:export2003Excel('<%=langName%>')"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="txtBut" class="btnClass4" type="button" value="<emp:message key="client_khtxlgl_kftxl_text_2007model" defVal="2007格式模板" fileName="client"></emp:message>" onclick="javascript:export2007Excel('<%=langName%>')"/>
			</span>
	</div>
    
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/client_<%=empLangName%>.js"></script>
		<script language="javascript" src="<%=iPath %>/js/addClient.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script language="javascript" src="<%=iPath %>/js/book.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript">
            var str_val = "";
            function validateInput(obj) {
                obj.value=obj.value.replace(/[']+/img,'');
                str = obj.value;
                var Expression=/^[\u4e00-\u9fa5]+$/;
                var poiseision=/^[a-zA-Z0-9]+$/;
                var objExp=new RegExp(Expression);
                var pinExp=new RegExp(poiseision);
                if(objExp.test(str)){
                    if (str.length > 30) {
                        $(obj).val(str_val);
                        alert("客户姓名长度英文字符不得超过60个字符，中文字符不得超过30字符");
                        return;
                    }
                }else if (pinExp.test(str)){
                    if (str.length > 60) {
                        $(obj).val(str_val);
                        alert("客户姓名长度英文字符不得超过60个字符，中文字符不得超过30字符");
                        return;
                    }
                } else {
                    if (str.length > 30) {
                        $(obj).val(str_val);
                        alert("客户姓名长度英文字符不得超过60个字符，中文字符不得超过30字符");
                        return;
                    }
                }
                str_val = str;
                $(obj).val(str_val);
            }
            $(document).ready(function(){
                // 解决 IE9 及以下 onChange 事件失效问题
                $("#uploadFile").change(function(){
                    alert(getJsLocaleMessage('common','common_uploadSucceed'));
                    showFileName();
                });
            });
            function showFileName(){
                var file=$("#uploadFile").val();
                var arr=file.split('\\');
                var fileName=arr[arr.length-1];
                $(".showFileName").text(fileName);
                $("#uploadFile").attr("title",fileName);
			}
		</script>
	</body>
</html>
