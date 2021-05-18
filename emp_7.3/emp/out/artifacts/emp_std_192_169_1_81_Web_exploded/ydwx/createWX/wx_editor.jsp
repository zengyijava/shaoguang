<%@ page language="java" import="com.montnets.emp.common.biz.CommonBiz" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.ViewParams" %>
<%@page import="com.montnets.emp.netnews.common.AllUtil"%>
<%@page import="com.montnets.emp.netnews.entity.LfWXBASEINFO"%>
<%@page import="com.montnets.emp.netnews.entity.LfWXSORT"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path=request.getContextPath();
String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
Object netid = request.getAttribute("netid");
Object objDataId = request.getAttribute("dataId");
String dataId = "";
if(objDataId != null){
	dataId = objDataId.toString();
}

Object objHasParams = request.getAttribute("params");
String hasParams = "";
if(objHasParams != null){
	hasParams = objHasParams.toString();
}

Object objUseParam = request.getAttribute("useParam");
String useParam = "";
if(objUseParam != null){
	useParam = objUseParam.toString();
}

Object objTempType = request.getAttribute("wx_tempType");
Integer tempType = 1;
if(objTempType != null){
	tempType = Integer.valueOf(objTempType.toString());
}

String wx_netid = "''";
String startyeardate = AllUtil.addDayDate(0);
String defaultTimeOut = AllUtil.addMonth(1);
String endyeardate = AllUtil.addDayDate(1);

if(netid!=null && !"".equals(netid))
	wx_netid = netid.toString();
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("manger");

@ SuppressWarnings("unchecked")
List<LfWXSORT> wxSortList = (List<LfWXSORT>)request.getAttribute("wxSortList");
@ SuppressWarnings("unchecked")
List<LfWXBASEINFO> wxbaseinfos = (List<LfWXBASEINFO>)request.getAttribute("wxbaseinfos");

String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");
if(pageInfo==null)
{
	pageInfo=new PageInfo();
}
String errormsg = (String)session.getAttribute("errormsg");

String wxpageid =(String)request.getAttribute("wxpageid");

String langName = (String)session.getAttribute("emp_lang");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=inheritPath%>/dhtmlxTree/docsExplorer/codebase/dhtmlxtree.css?V=<%=StaticValue.getJspImpVersion() %>"></link>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=inheritPath%>/ueditor/themes/default/ueditor_<%=empLangName%>.css?V=<%=StaticValue.getJspImpVersion() %>"/>
        <link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
        <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<%--  页面原有的样式优化到下面文件--%>
     	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/wxEditor.css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/ydwx/createWX/css/wx_editor_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}else{%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/ydwx/createWX/css/wx_editor.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
     	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>

	</head>
	<body onload="showNotify()" class="wx_editor" id="ydwx_editor">
	<% 
	// 原来该代码写在最上面，导致页面无法加载样式，所以只好放在BOAY里面，样式没有问题。
	if(errormsg != null && errormsg.length() > 0){
	session.removeAttribute("errormsg");
	out.write("<script type=\"text/javascript\" >alert('" + errormsg + "'); </script>");
}%>
		<div id="container" class="container" >
		<%-- header开始 --%>
		<%if(wx_netid.equals("''")) {%>
			<%=ViewParams.getPosition(langName,menuCode, MessageUtils.extractMessage("ydwx","ydwx_wxgl_add_xjwx",request)) %>
			<%}else{ %>
			<%=ViewParams.getPosition(langName,menuCode, MessageUtils.extractMessage("ydwx","ydwx_wxgl_add_bjwx",request)) %>
		<%} %>
			
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
					<div class="titletop">
					<table class="titletop_table ydwx_titletop_table">
						<tr>
							<td class="titletop_td">
								<%if(wx_netid.equals("''")) {%>
								<emp:message key="ydwx_wxgl_add_xjwx" defVal="新建网讯" fileName="ydwx"></emp:message>
								<%}else{ %>
								<emp:message key="ydwx_wxgl_add_bjwx" defVal="编辑网讯" fileName="ydwx"></emp:message>
								<%} %>
							</td>
							<td align="right">
								<span class="titletop_font" onclick="javascript:back()"><emp:message key="ydwx_wxgl_add_fhshyj" defVal="返回上一级" fileName="ydwx"></emp:message></span>
							</td>
						</tr>
					</table>
					</div>						
					<div id="overlay1"></div>				
					<div id="showdivBox" class="overlay" class="ydwx_showdivBox">
				             <div class="ydwx_ydwx_divBoxtop"  onmouseover="Move_obj2('divBoxtop')" id="divBoxtop">
					          <b  class="ydwx_divBoxtop_b1"><emp:message key="ydwx_wxgl_add_wxchk" defVal="网讯查看" fileName="ydwx"></emp:message></b>
					          <b  class="ydwx_divBoxtop_b2" onclick="cancelDiv()"></b>
							 </div>
							 <hr>
						     
				  			<div id="img" class="ydwx_img">
		            			<div id="netInfoDiv" class="showNet ydwx_netInfoDiv"></div>
		             		</div>
		             		 <hr>
             		</div>
	                                    	
								
					<form  name="Form" action="<%=path %>/wx_ueditor.htm?method=Ueditor" method="post">
						<input type="hidden" name="wxpageid" id="wxpageid" value="<%=wxpageid %>" >
						<input type="hidden" name="wx_id" id="wx_id" value=""  size="60" >
						<div style="display:none" id="hiddenValueDiv"></div>
						<input type="hidden" name="wx_timeType" id="wx_timeType" value="1" >
						<input type="hidden" name="dataId" id="dataId" value="<%=dataId %>" >
						<input type="hidden" name="templateId" id="templateId">
						<input type="hidden" name="params" id="params" value="<%=hasParams %>" >
						<input type="hidden" name="useParam" id="useParam" value="<%=useParam %>" >
						<input type="hidden" name="path" id="path" value="<%=path %>" >
						<input type="hidden" name="saveAndList" id="saveAndList" >
						<%-- 用于追加覆盖获得动态表名等    网讯ID  --%>
						<input type="hidden" name="appendForNetID" id="appendForNetID" value="">
						<div class="div_all">
						<table class="table002">
						  <tr>
						   <td><emp:message key="ydwx_wxgl_wxmc" defVal="网讯名称：" fileName="ydwx"></emp:message></td>
						   <td><input type="text" class="input_bd div_bd ydwx_wx_name" name="wx_name" id="wx_name" size="60" value=""></td>
						   <td><emp:message key="ydwx_wxgl_add_wxlxs" defVal="网讯类型：" fileName="ydwx"></emp:message></td>
						   <td>
							   <input type="radio" class="wx_radio" name="wx_tempType" id="wx_tempType1" value="1" <%=tempType==1?"checked='checked'":"" %> />
								<emp:message key="ydwx_wxgl_mblx_options1" defVal="静态模板" fileName="ydwx"></emp:message>

							   <input type="radio" name="wx_tempType" class="wx_radio" id="wx_tempType2" value="2" <%=tempType==2?"checked='checked'":"" %> />
								<emp:message key="ydwx_wxgl_mblx_options2" defVal="动态模板" fileName="ydwx"></emp:message>
						   </td>
						   <td><emp:message key="ydwx_common_time_youxiaoshijians" defVal="有效时间：" fileName="ydwx"></emp:message></td>
						   <td><input type="text" name="wx_timeOut" id="wx_timeOut"
									class="Wdate input_bd div_bd ydwx_wx_timeOut" readonly="readonly"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'<%=startyeardate %>',isShowToday:true})"
									value="<%=defaultTimeOut %>" /> <%-- <font color="#0000FF">*有效时间最长为1年</font>  --%></td>
						  </tr>
						  
						</table>
						
						
						
						<%--<p style="margin:15px">
						<label class="tit">网讯名称：</label>
						<input type="text" class="input_bd div_bd" name="wx_name" id="wx_name" size="60" value="" style="width:528px;"></p>
						<p style="margin:15px">
						
						<label class="tit">网讯类型：</label>
						<input type="radio" name="wx_tempType" id="wx_tempType1" value="1" <%=tempType==1?"checked='checked'":"" %> >静态模板&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="wx_tempType" id="wx_tempType2" value="2" <%=tempType==2?"checked='checked'":"" %>> 动态模板<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
						有效时间：<input type="text" name="wx_timeOut" id="wx_timeOut"
									style="cursor: pointer; width: 180px; background-color: white;"
									class="Wdate input_bd div_bd" readonly="readonly"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'<%=startyeardate %>',maxDate:'<%=endyeardate %>',isShowToday:true})"
									value="<%=endyeardate %>" />  <font color="#0000FF">*有效时间最长为1年</font>  
						</p>
						--%><p class="ydwx_p" ><emp:message key="ydwx_wxgl_add_xxnrs" defVal="信息内容：" fileName="ydwx"></emp:message><input type="text" class="input_bd div_bd"  name="wx_sms" id="wx_sms"   size="60" value="" /></p>
					</div> 
					<input type="hidden" id="wx_netid" name="wx_netid" value="" />
					<table id="eContent" class="ydwx_eContent">
						<tr>
							<td id="pageSetting" valign="top">
							<div id="pageSetting_title" class="div_bd div_bg ydwx_pageSetting_title">
								<div class="ydwx_pageSetting_title_sub">
									<a class="ydwx_add" onclick="newTree()" ><emp:message key="ydwx_common_btn_xinjian" defVal="新建" fileName="ydwx"></emp:message></a>
									&nbsp;
									<a class="ydwx_rename"  onclick="changeName()" ><emp:message key="ydwx_common_btn_chongmingming" defVal="重命名" fileName="ydwx"></emp:message></a>
									&nbsp;
									<a class="ydwx_delete"  onclick="delNode()" ><emp:message key="common_delete" defVal="删除" fileName="common"></emp:message></a>
								</div>
							</div>
							<div <%="zh_HK".equals(langName)?"class='div_all13 input_bd div_bd ydwx_doctree_box'":"class='div_all12 input_bd div_bd ydwx_doctree_box'"%>
                                    id="doctree_box"  <%="zh_HK".equals(langName)?"style='height: 433px;'":"" %>></div>
							<div id="cname" class="ydwx_cname">
								<br>
								<div align="left" class="ydwx_cname_sub">
									<b><emp:message key="ydwx_wxgl_add_qshrmchs" defVal="请输入名称：" fileName="ydwx"></emp:message></b><br>
									<input type="text" id="pageName" class="ydwx_pageName">
								</div>
								<br>
								<div align="center">
									<input class="bt ydwx_cOK" type="button"
										id="cOK" value="<emp:message key='ydwx_common_btn_queren' defVal='确认' fileName='ydwx'></emp:message>">
									<input  type="button" class="bt ydwx_cNO"
										id="cNO" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>">
								</div>
								<br>
							</div>
							
							<div id="zym" ></div>
	
							</td>
							<td id="ueditorContent" valign="top">
								<textarea id="editor"></textarea>
								<table class="ydydwx_editor_table"><tr align="center"><td>
								<input type="button" value="<emp:message key='ydwx_common_btn_zancun' defVal='暂  存' fileName='ydwx'></emp:message>" class="btnClass5 " name="save" id="save" onclick="submitui('1');"/>
								 <input type="button" value="<emp:message key='ydwx_common_btn_tijiaoshenhe' defVal='提交审核' fileName='ydwx'></emp:message>"  class="btnClass5 btnLetter4 indent_none" name="saveAndList" onclick="submitui('2');"/>
								 <input type="button" value="<emp:message key="ydwx_common_yulan" defVal="预览" fileName="ydwx"></emp:message>"  class="btnClass5 " onclick="Look();"/>  
								 <input type="button" value="<emp:message key="ydwx_common_btn_fanhui" defVal="返  回" fileName="ydwx"></emp:message>"  class="btnClass6" onclick="back();"/>
								 <br/>
								</td></tr>
								</table>
							</td>
							<td id="tempPreview" valign="top">
								<div id="tempSelect" <%="zh_HK".equals(langName)?"class='div_all13 input_bd div_bd ydwx_tempSelect' style='height:448px'":"class='div_all12 input_bd div_bd ydwx_tempSelect'"%>>
									<div class="input_bd div_bd div_bg ydwx_tempSelect_sub" >
										<div id="currentTemp"><emp:message key="ydwx_wxgl_add_xzmbdqmb" defVal="选择模板：当前模板" fileName="ydwx"></emp:message></div>
										<select id="sortId" class="input_bd div_bd ydwx_sortId" onchange="chageTemplate(this)">
										<%if(wxSortList!=null && wxSortList.size()>0)
										{ 
											for(LfWXSORT wxsort:wxSortList)
											{
										%>
											<option value="<%=wxsort.getID() %>"><%=wxsort.getNAME()!=null?wxsort.getNAME().replaceAll("<","&lt").replaceAll(">","&gt"):"" %></option>
										<%}} %>
										</select>
									</div>
									<div class="ydwx_changeTemplateDiv"  id="changeTemplateDiv">
										<%if(wxbaseinfos!=null && wxbaseinfos.size()>0){
											for(LfWXBASEINFO wxbinfo:wxbaseinfos)
											{
										%>
										<div class="ydwx_changeTemplateDiv_sub">
											<div >
											<%--  此处增加了分布式数据处理--%>
											<% 
												String imgSrc="";
												if(StaticValue.getISCLUSTER() ==1){
												String imageUrl=wxbinfo.getIMAGE();
												//如果/file/wxTempImage/ 路径，则要去掉/（多余的） 
												if(wxbinfo.getIMAGE().length()>0&&wxbinfo.getIMAGE().indexOf("/")==0){
													imageUrl=wxbinfo.getIMAGE().substring(1);
												}
												String viewurl="";
												CommonBiz biz=new CommonBiz();
												String[] filePath=biz.getALiveFileServer();
												if(filePath!=null&&filePath.length>1){
													viewurl=filePath[1];
												}
													imgSrc=viewurl+imageUrl;
												}else{
													imgSrc=request.getContextPath()+wxbinfo.getIMAGE();
												}
											 %>
												<img onclick="templateShow('<%=wxbinfo.getNETID() %>')" alt="<%=wxbinfo.getNAME() %>" title="<%=wxbinfo.getNAME() %>" class="ydwx_templateShow_img" src="<%=imgSrc%>"/>
											</div>
											<div class="ydwx_changeTemplateDiv_sub_sub">
												<a onclick="templateShow('<%=wxbinfo.getNETID() %>')" style="cursor: pointer;"><emp:message key="ydwx_common_yulan" defVal="预览" fileName="ydwx"></emp:message></a>
												<span class="ydwx_span">&nbsp;</span>
												<a onclick="templateApplyConfirm('<%=wxbinfo.getNETID() %>')" style="cursor: pointer;"><emp:message key="ydwx_common_yingyong" defVal="应用" fileName="ydwx"></emp:message></a>
											</div>
										</div>
										<%}} %>
									</div>
								</div>
								<div class="ydwx_changePage" id="changePage" >
									<%-- 总页数 --%>
									<input type="hidden" name="wxb_pageCount" id="wxb_pageCount" value="<%=pageInfo.getTotalPage() %>"/>
									<%-- 页数 --%>
									<input type="hidden" name="wxb_pageIndex" id="wxb_pageIndex" value="<%=pageInfo.getPageIndex() %>"/>
									<input type="button" value="<emp:message key='ydwx_common_btn_shangyiye' defVal='上一页' fileName='ydwx'></emp:message>" onclick="doUpPage()" class="btnClass2">
									<input type="button" value="<emp:message key='ydwx_common_btn_xiayiye' defVal='下一页' fileName='ydwx'></emp:message>" onclick="doDownPage()" class="btnClass2">
									<br/>
								</div>
							</td>
						</tr>		
					</table>
				</form> 
			<div id="divBox1" class="hideDlg" style="display:none" title="<emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message>">
				<div class="ydwx_divBox1_sub"  >
					<iframe class="ydwx_nm_preview_common" id="nm_preview_common" src=""></iframe>	
				</div>
			</div>
			<div id="templateDiv" title="<emp:message key="ydwx_wxgl_add_mbyl" defVal="模板预览" fileName="ydwx"></emp:message>" style="display: none">
				<div class="ydwx_templateDiv_sub">
					<img src="" id="templateImage" />
				</div>
				<div style="text-align: center;">
					<input type="button" value="<emp:message key="ydwx_common_yingyong" defVal="应用" fileName="ydwx"></emp:message>" class="btnClass5 mr23" onclick="templateApplyConfirm($('#templateId').val())"/>
					<input type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" class="btnClass6" onclick="cancelTemplate()"/>
				</div>
			</div>
			
			<div id="divBox" style="display:none" title="<emp:message key="ydwx_common_yulan" defVal="预览" fileName="ydwx"></emp:message>">
				<div  align="center" class="ydwx_divBox_sub">
				         <emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message> <select class="ym ydwx_ym"></select>
		        </div>
				<div class="ydwx_preview_shows_dv">
					<iframe class="ydwx_preview_shows" id="nm_preview_shows" src=""></iframe>	
				</div>
			</div>   
			 	
			<div id="muchose" title="<emp:message key="ydwx_wxgl_add_mbyymsh" defVal="模板应用模式" fileName="ydwx"></emp:message>" class="ydwx_muchose" onmouseover="Move_obj('pretop')">
				<div align="left" id="pretop" >
				<b class="ydwx_payorback" id="payorback"></b>
		        </div>	
		        <center>
				<table style="text-align: center;">
					<tr><td colspan="2" align="center" height="30px" >
						<emp:message key="ydwx_wxgl_add_xinxi1" defVal="选择应用模板的方式" fileName="ydwx"></emp:message></td>
					</tr>
					<tr>
						<td><emp:message key="ydwx_common_zhus" defVal="注：" fileName="ydwx"></emp:message></td>
					</tr>
					<tr>
						<td height="25px" style="text-align: left;<%="zh_HK".equals(empLangName)?"width:80px":""%>"><b><emp:message key="ydwx_common_btn_zhuijia" defVal="追加" fileName="ydwx"></emp:message></b></td>
						<td class="ydwx_add_xinxi2"><emp:message key="ydwx_wxgl_add_xinxi2" defVal="选择此项在当前网讯末尾追加模板页面" fileName="ydwx"></emp:message></td>
					</tr>
					<tr>
						<td height="25px" class="ydwx_btn_fugai"><b><emp:message key="ydwx_common_btn_fugai" defVal="覆盖" fileName="ydwx"></emp:message></b></td>
						<td class="ydwx_add_xinxi3"><emp:message key="ydwx_wxgl_add_xinxi3" defVal="选择此项删除当前网讯内容加载模板" fileName="ydwx"></emp:message></td>
					</tr>
					<tr>
						<td colspan="2"  height="10px">
						</td>
					</tr>
					<tr>
						<td colspan="2"  height="30px">
						<input type="button" class="btnClass2" onclick="muConfirm(1);" value="<emp:message key="ydwx_common_btn_zhuijia" defVal="追加" fileName="ydwx"></emp:message>">
						&nbsp;&nbsp;&nbsp;
						<input  type="button" class="btnClass2" onclick="muConfirm(3);" value="<emp:message key="ydwx_common_btn_fugai" defVal="覆盖" fileName="ydwx"></emp:message>">
						&nbsp;&nbsp;&nbsp;
						<input type="button" class="btnClass2" onclick="closemudiv();" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>">
						</td>
					</tr>
				</table>
		        </center>
				</div>
			</div>
			</div>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/ueditor/dialogs/jquery.form.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/ueditor/editor_config.js" charset="utf-8" ></script>
		<script type="text/javascript" src="<%=inheritPath%>/dhtmlxTree/docsExplorer/codebase/dhtmlxcommon.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/dhtmlxTree/docsExplorer/codebase/dhtmlxtree.js"></script>
      	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js"></script>
      	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js"></script>
      	<script type="text/javascript" src="<%=empFramePath%>/js/dialogui.js"></script>
      <%--开发版 加载一些控件脚本--%>
    	<script type="text/javascript" src="<%=inheritPath%>/ueditor/_examples/editor_api.js" charset="utf-8" >
	        paths = [
	            'editor.js',
	            'core/browser.js',
	            'core/utils.js',
	            'core/EventBase.js',
	            'core/dom/dom.js',
	            'core/dom/dtd.js',
	            'core/dom/domUtils.js',
	            'core/dom/Range.js',
	            'core/dom/Selection.js',
	            'core/Editor.js',
	            'commands/inserthtml.js',
	            'commands/image.js',
	            'commands/justify.js',
	            'commands/font.js',
	            'commands/link.js',
	            'commands/map.js',
	            'commands/iframe.js',
	             'commands/actividata.js',
	            'commands/removeformat.js',
	            'commands/blockquote.js',
	            'commands/indent.js',
	            'commands/print.js',
	            'commands/preview.js',
	            'commands/spechars.js',
	            'commands/emotion.js',
	            'commands/selectall.js',
	            'commands/paragraph.js',
	            'commands/directionality.js',
	            'commands/horizontal.js',
	            'commands/time.js',
	            'commands/rowspacing.js',
	            'commands/cleardoc.js',
	            'commands/anchor.js',
	            'commands/delete.js',
	            'commands/wordcount.js',
	            'commands/image.js',
	            'plugins/pagebreak/pagebreak.js',
	            'plugins/undo/undo.js',
	            'plugins/paste/paste.js',
	            'plugins/list/list.js',
	            'plugins/source/source.js',
	            'plugins/shortcutkeys/shortcutkeys.js',
	            'plugins/enterkey/enterkey.js',
	            'plugins/keystrokes/keystrokes.js',
	            'plugins/fiximgclick/fiximgclick.js',
	            'plugins/autolink/autolink.js',
	            'plugins/autoheight/autoheight.js',
	            'plugins/autofloat/autofloat.js',  //依赖UEditor UI,在IE6中，会覆盖掉body的背景图属性
	            'plugins/highlight/highlight.js',
	            'plugins/serialize/serialize.js',
	            'plugins/video/video.js',
	            'plugins/table/table.js',
	            'plugins/Interaction/Interaction.js',
	            'plugins/downfile/downfile.js',
	            'plugins/contextmenu/contextmenu.js',
	            'plugins/pagebreak/pagebreak.js',
	            'plugins/basestyle/basestyle.js',
	            'plugins/elementpath/elementpath.js',
	            'plugins/formatmatch/formatmatch.js',
	            'plugins/searchreplace/searchreplace.js',
	            'ui/ui.js',
	            'ui/uiutils.js',
	            'ui/uibase.js',
	            'ui/separator.js',
	            'ui/mask.js',
	            'ui/popup.js',
	            'ui/colorpicker.js',
	            'ui/tablepicker.js',
	            'ui/stateful.js',
	            'ui/button.js',
	            'ui/splitbutton.js',
	            'ui/colorbutton.js',
	            'ui/tablebutton.js',
	            'ui/toolbar.js',
	            'ui/menu.js',
	            'ui/combox.js',
	            'ui/dialog.js',
	            'ui/menubutton.js',
	            'ui/datebutton.js',
	            'ui/editorui.js',
	            'ui/editor.js',
	            'ui/multiMenu.js'
	        ];
        
   	 	</script>
   	 	<script type="text/javascript">
	   		var subcount = 0;
	   		$(document).ready(
		   		function(){
		   			getLoginInfo("#hiddenValueDiv");
		
		   			var lguserid = $("#lguserid").val();
		   			var lgcorpcode = $("#lgcorpcode").val();
		   			
		   			$("#divBox1").dialog({
		   					autoOpen: false,
		   					height:500,
		   					width: 300,
		   					modal: true,
		   					close:function(){
		   					}
		   				});
		   				
		   			$("#divBox").dialog({
						autoOpen: false,
						height:510,
						width: 300,
						modal: true,
						close:function(){
						}
					});	
						
		   			 $("#templateDiv").dialog({
		   					autoOpen: false,
		   					height:495,
		   					width: 260,
		   					modal: true,
		   					open:function(){
		   						hideOrShowSelect(true);
		   					},
		   					close:function(){
		   						hideOrShowSelect(false);
		   					}
		   				});muchose

	   				$("#muchose").dialog({
	   					autoOpen: false,
	   					height:210,
	   					width: <%="zh_HK".equals(empLangName)?520:320%>,
	   					modal: true,
	   					open:function(){
	   						hideOrShowSelect(true);
   						},
	   					close:function(){
   							hideOrShowSelect(false);
	   					}
	   				});
		   		    
				 	//页码改变时  div层内容变化的方法
                    $(".ym").change(function(){
                        var id=$(this).val();
                        for(var i=0;i<listpage.length;i++){
                            if(id==listpage[i].id){
                                // 此处为显示错误页面，避免进入登录页面
                                if(listpage[i].content=="notexists"){
                                    $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
                                }else{
                                    $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
                                }
                            }
                        }
                    });
		   	    <% if(!wx_netid.equals("''")){ %>
		   			IDbyInfo();
		   		<% }else{%>
		   		var wxpageidstr=getwxpagestr('0');
		   		document.getElementById("zym").innerHTML ="<div id='div0'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input0' value='"+getJsLocaleMessage("ydwx","ydwx_wxbj_38")+"<#!!#>0<#!!#><p></p><#!!#>0<#!!#>01"+wxpageidstr+"'></p></div>"; 
		   		loadTree("<tree id='-1'><item id='0' text='"+getJsLocaleMessage("ydwx","ydwx_wxbj_39")+"' im0='dhtmlxtree_icon.gif' im1='dhtmlxtree_icon.gif' im2='dhtmlxtree_icon.gif'></item></tree>");
		   		<%}%>
	   		});
	 		
	 		// 上页处理
   	 		function doUpPage()
   	 		{
   	   	 		var lgcorpcode=$("#lgcorpcode").val();
   	   	 		var lguserid=$("#lguserid").val();
   	   	 		var pageIndex=$("#wxb_pageIndex").val();
   	   	 		var pageCount=$("#wxb_pageCount").val();
   	   	 		if(pageIndex==1)
   	   	 		{
   	   	   	 		alert(getJsLocaleMessage("ydwx","ydwx_wxbj_40"));
   	   	   	 		return;
   	   	 		}
   	   	 		pageIndex =parseInt(pageIndex)-1;
   	   	 		var sortId=$("#sortId").val();
   	   	 		$("#changePage").find("input[type='button']").attr("disabled","disabled");
   	 			$.post("wx_ueditor.htm",
					{method:"getWxBaseinfoByPage",lgcorpcode:$("#lgcorpcode").val(),sortId:sortId,pageIndex: pageIndex,pageCount:pageCount,lguserid:$("#lguserid").val(),isAsync:"yes"},
	    	    	function(result){
						$("#changePage").find("input[type='button']").attr("disabled","");
						if(result == "outOfLogin")
		    	    	{
		    	    		$("#logoutalert").val(1);
		    	    		location.href="<%=request.getContextPath()%>/common/logoutEmp.jsp";
		    	    		return;
		    	    	}
						else if(result.indexOf("@")!=-1)
						{
							var arrayRes=result.split("@");
							$("#changeTemplateDiv").html(arrayRes[0]);
							$("#wxb_pageIndex").val(arrayRes[1]);
			   	   	 		$("#wxb_pageCount").val(arrayRes[2]);
						}
					}
				);
   	 		}
			//下页处理
	   	 	function doDownPage()
		 	{
	   	 		var lgcorpcode=$("#lgcorpcode").val();
	   	 		var lguserid=$("#lguserid").val();
	   	 		var pageIndex=$("#wxb_pageIndex").val();
	   	 		var pageCount=$("#wxb_pageCount").val();
	   	 		if(pageIndex>=pageCount)
	   	 		{
	   	   	 		alert(getJsLocaleMessage("ydwx","ydwx_wxbj_41"));
	   	   	 		return;
	   	 		}
	   	 		pageIndex =parseInt(pageIndex)+1;
	   	 		var sortId=$("#sortId").val();
	   	 		$("#changePage").find("input[type='button']").attr("disabled","disabled");
	 			$.post("wx_ueditor.htm",
				{method:"getWxBaseinfoByPage",lgcorpcode:$("#lgcorpcode").val(),sortId:sortId,pageIndex: pageIndex,pageCount:pageCount,lguserid:$("#lguserid").val(),isAsync:"yes"},
	    	    	function(result){
						$("#changePage").find("input[type='button']").attr("disabled","");
						if(result == "outOfLogin")
		    	    	{
		    	    		$("#logoutalert").val(1);
		    	    		location.href="<%=request.getContextPath()%>/common/logoutEmp.jsp";
		    	    		return;
		    	    	}
						else if(result.indexOf("@")!=-1)
						{
							var arrayRes=result.split("@");
							$("#changeTemplateDiv").html(arrayRes[0]);
							$("#wxb_pageIndex").val(arrayRes[1]);
			   	   	 		$("#wxb_pageCount").val(arrayRes[2]);
						}
					}
				);
		 	}

	   	 	//切换网讯模板
		 	function chageTemplate(objiect)
		 	{
			 	var sortId=$(objiect).val();
			 	pageIndex =1;
	   	 		var sortId=$("#sortId").val();
	 			$.post("wx_ueditor.htm",
				{method:"getWxBaseinfoByPage",lgcorpcode:$("#lgcorpcode").val(),sortId:sortId,pageIndex: pageIndex,lguserid:$("#lguserid").val(),isAsync:"yes"},
	    	    	function(result){
						if(result == "outOfLogin")
		    	    	{
		    	    		$("#logoutalert").val(1);
		    	    		location.href="<%=request.getContextPath()%>/common/logoutEmp.jsp";
		    	    		return;
		    	    	}
						else if(result.indexOf("@")!=-1)
						{
							var arrayRes=result.split("@");
							$("#changeTemplateDiv").html(arrayRes[0]);
							$("#wxb_pageIndex").val(arrayRes[1]);
			   	   	 		$("#wxb_pageCount").val(arrayRes[2]);
						}
		    	    	
					}
				);
		 	}
	
	   		//点击应用
	   	 	function templateApplyConfirm(str)
	   	 	{
	   	    	$("#muchose").dialog("open");
	   	    	modid=str;
	   	    	$.post("<%=path %>/wx_manger.htm?method=findCreatid",{"netid":str},function(data){
	   	        	creatid=data;
	   	    	});
	   	 	}
	
	   		//点击预览
	   	 	function templateShow(netId)
	   	 	{
	    	   $.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
			       data=eval("("+data+")");
			       listpage=data;
			       $(".ym").children().remove();
			       for(var i=0;i<listpage.length;i++)	{   
			           $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
			      }
                   // 此处为显示错误页面，避免进入登录页面
                   if(listpage[0].content=="notexists"){
                       $("#nm_preview_shows").attr("src","ydwx/wap/404.jsp");
                   }else{
                       $("#nm_preview_shows").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
                   }
                  resizeDialog($("#divBox"),"ydwxDialogJson","ydwx_editor_divBox");
			      $("#divBox").dialog("open");
		       });
	   	 	}
	   	 	//取消
		   	function cancelTemplate()
		    {
		        $("#templateDiv").dialog("close");
		    }
	
	   	 	//取消
		   	function closemudiv(){
		   		$("#muchose").dialog("close");
		    }
			
			
	
		    //点击追加或覆盖
		   	function muConfirm(s){
				$("#muchose").dialog("close");
				$("#templateDiv").dialog("close");
			    var str= modid;
				var loadzym =document.getElementById("zym").innerHTML;
		        var mycars=new Array();
		        var d ="";
		        var nodeid=tree._globalIdStorageFind("0");
		        var count=tree._getLeafCount(nodeid);   //得到节点下子节点数
		        var lguserid=$("#lgyuserid").val();
		        var wxpageid= $("#wxpageid").val();
		        $("#appendForNetID").val(modid);
				if(s=='3')
				{
					//遮盖层
					if(document.getElementById("deck")!="" && document.getElementById("deck")!=null)
						cancel();
					xmlHttp=GetXmlHttpObject()
		 	    	if (xmlHttp==null)
		    		{
		    			alert (getJsLocaleMessage("ydwx","ydwx_wxbj_42"));
		    			return;
		    		}
			    	//发送请求，并将在onComplete选项中调用回调函数
					$.post('<%=path %>/wx_ueditor.htm?method=getParentUdeitor',{Action:"post",netid:str,creteid:creatid,lguserid:lguserid,showcount:-1,wxpageid:wxpageid},
						function(result){
							result = eval("("+result+")");
							if(result.success){
					       
					            var s=$("#input0").val();
					            openPathDocs("0");
					            showcount=0;
					            tree.deleteChildItems(0);
							    loadzym="";
								var dataObj=result.msg;//转换为json对象 
								var baseObj=result.base;//转换为json对象 
								$.each(baseObj.root, function(idx,item){
								    var sst;
								    var wxpageidstr=getwxpagestr('0');
									sst= s.split("<#!!#>")[0]+ "<#!!#>"+s.split("<#!!#>")[1]+"<#!!#>"+item.page.split("<#!!#>")[2]+"<#!!#>"+ s.split("<#!!#>")[3]+"<#!!#>01"+wxpageidstr;
								    document.getElementById("zym").innerHTML="<div id='div0'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input0' value='"+sst+"'></p></div>";
								    editor.setContent(item.page.split("<#!!#>")[2]);
								    pageid=s.split("<#!!#>")[3];
									//新增覆盖操作 根节点名称没有被替换
								    tree._globalIdStorageFind(0).span.innerHTML=item.name;
								    var Nodevalue = document.getElementById("input0").value;
									var namelist = Nodevalue.split("<#!!#>"); 
									document.getElementById("input0").value = item.name+"<#!!#>"+namelist[1]+"<#!!#>"+namelist[2]+"<#!!#>"+namelist[3]+"<#!!#>"+namelist[4] ; 
									//以上参照重命名方法
								});
								if(dataObj)
								{	
									$.each(dataObj.root, function(idx,item){
										var ss;
										showcount= showcount+1;
										//获取pageid页面编号
		   						 		var wxpageidstr=getwxpagestr(showcount);
								    	if(item.page.split("<#!!#>")[0]=='null')
									    {
								    		ss='page'+showcount;
							    		}
									    else
										{
									    	ss=item.page.split("<#!!#>")[0];
									    }
									
										//输出每个root子对象的名称和值 
										loadzym=document.getElementById("zym").innerHTML+"<div id='div"+showcount+"'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value='"+ss+"<#!!#>1<#!!#>"+item.page.split("<#!!#>")[2]+"<#!!#>-1<#!!#>01"+wxpageidstr+"'></p></div>";  
										tree.insertNewItem("0",showcount,ss);   //增加子节点
			                    		document.getElementById("zym").innerHTML=loadzym;
									});
								
								}
							}else{
								alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));
							}
						});	
				}
				else{
					if(document.getElementById("deck")!="" && document.getElementById("deck")!=null)
						cancel();
					xmlHttp=GetXmlHttpObject()
		 	    	if (xmlHttp==null)
		    		{
		    			alert (getJsLocaleMessage("ydwx","ydwx_wxbj_42"));
		    			return;
		    		}
		    		$.post('<%=path %>/wx_ueditor.htm?method=getParentUdeitor',
			    		{Action:"post",netid:str,creteid:creatid,lguserid:lguserid,showcount:showcount,wxpageid:wxpageid},
			    		function(result){
			    		result = eval("("+result+")");
						if(result.success){
							var dataObj=result.msg;//转换为json对象 
							var baseObj=result.base;//转换为json对象 
							if(tree._getLeafCount(nodeid)<9){
							$.each(baseObj.root, function(idx,item){
							   var ss;
							    showcount= showcount+1;
							    //获取pageid页面编号
		   						 var wxpageidstr=getwxpagestr(showcount);
							  if(item.page.split("<#!!#>")[0]=='null'){
							    ss='page'+showcount;
							  }else{
							    ss=item.page.split("<#!!#>")[0];
							  }
							 
										//输出每个root子对象的名称和值 
								loadzym=loadzym+"<div id='div"+showcount+"'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value='"+ss+"<#!!#>1<#!!#>"+item.page.split("<#!!#>")[2]+"<#!!#>-1<#!!#>01"+wxpageidstr+"'></p></div>"; 
								tree.insertNewItem("0",showcount,ss);   //增加子节点
			                    document.getElementById("zym").innerHTML=loadzym;
							});
							if(tree._getLeafCount(nodeid)<9){
							if(dataObj){	
								$.each(dataObj.root, function(idx,item){
								count=tree._getLeafCount(nodeid);
								if(count>8){
							       alert(getJsLocaleMessage("ydwx","ydwx_wxbj_43"));
							       return false;
								   }
								 var ss;
								 showcount= showcount+1;
								 //获取pageid页面编号
		   						 var wxpageidstr=getwxpagestr(showcount);
							  if(item.page.split("<#!!#>")[0]=='null'){
							    ss='page'+showcount;
							  }else{
							    ss=item.page.split("<#!!#>")[0];
							  }
									//输出每个root子对象的名称和值 
									loadzym=loadzym+"<div id='div"+showcount+"'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value='"+ss+"<#!!#>1<#!!#>"+item.page.split("<#!!#>")[2]+"<#!!#>-1<#!!#>01"+wxpageidstr+"'></p></div>";
								 	tree.insertNewItem("0",showcount,ss);   //增加子节点
			                      	document.getElementById("zym").innerHTML=loadzym;
								
								});
								
							 }
							 }else{
								alert(getJsLocaleMessage("ydwx","ydwx_wxbj_43"));
							 }
							 }else{
								alert(getJsLocaleMessage("ydwx","ydwx_wxbj_127"));
							 }
						}else{
							alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));
						}
					});	
		    		
				}
			}
			
			//提示操作状态，在保存后，跳转到该页面就会提示
			function showNotify(){
				var res = <%=(String)session.getAttribute("callbackResult")%>;
				if(res != null && res !=""){
					<%session.removeAttribute("callbackResult");%>
					if(res){
						alert(getJsLocaleMessage("ydwx","ydwx_common_czchg"));
					}else {
						alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));
					}
				}
			}	
			
   	 	</script>
   	 	<script type="text/javascript">
			var editor = new baidu.editor.ui.Editor({
		        autoHeightEnabled:false 
		        
		    });
		    editor.render('editor');
		</script>
			
<%--  将鼠标放置于图片之上显示大图 begin --%>
<script type="text/javascript">
			//加载控件
			function enableTooltips(id){
				
				var links,i,h;
				if(!document.getElementById || !document.getElementsByTagName) return;
				
				h=document.createElement("span");
				h.id="btc";
				h.setAttribute("id","btc");
				h.style.position="absolute";
				document.getElementsByTagName("body")[0].appendChild(h);
				if(id==null) links=document.getElementsByTagName("img");
				else links=document.getElementById(id).getElementsByTagName("img");
				for(i=0;i<links.length;i++){
					PrepareImg(links[i]);
				}
			}
			
			//主要是处理返回按钮，返回到制定的页面
			function back()
			{
				var lguserid=GlobalVars.lguserid;
				var lgcorpcode=GlobalVars.lgcorpcode;
				<%
				String frame = (String) session.getAttribute(StaticValue.EMP_WEB_FRAME);
				String pageI=(String)session.getAttribute("pageI");
				String pageS=(String)session.getAttribute("pageS");
				if("1".equals(request.getParameter("type")))
				{
						if("frame2.5".equals(frame)){
				%>
					window.parent.openNewTab("2400-1000","<%=path%>/wx_send.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode);
					<%}else{%>
					location.href="<%=path%>/wx_manger.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
					
				<%
				}
				}else if("2".equals(request.getParameter("type")))
				{
					if("frame2.5".equals(frame)){
				%>
					window.parent.openNewTab("2400-1080","<%=path%>/wx_senddsm.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode);
					<%}else{%>
					location.href="<%=path%>/wx_manger.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
					
				<%
				}
				}else{
					if("frame2.5".equals(frame)){
				%>
					window.parent.openNewTab("2600-1010","<%=path%>/wx_manger.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode);
				<%}else{%>		
					location.href="<%=path%>/wx_manger.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&pageIndex="+<%=pageI%>+"&pagesize="+<%=pageS%>;
				<%	
				}
				}
				%>
			}
			//预览功能（控件右边）
			function PrepareImg(el){
				var tooltip,t,b,s,l;
				t=el.getAttribute("message");
				if(t==null || t.length==0) return ;
				el.removeAttribute("message");
				tooltip=CreateEl("span","tooltip");
				s=CreateEl("span","top");
				s.innerHTML = t;
				tooltip.appendChild(s);
				l=el.getAttribute("src");
				if(l.length>30) l=l.substr(0,27)+"...";
				setOpacity(tooltip);
				el.tooltip=tooltip;
				el.onmouseover=showTooltip;
				el.onmouseout=hideTooltip;
				el.onmousemove=Locate;
			}

			//用于预览功能的部分事件  onmouseover
			function showTooltip(e){
				document.getElementById("btc").appendChild(this.tooltip);
			}

			//用于预览功能的部分事件 onmouseout
			function hideTooltip(e){
				var d=document.getElementById("btc");
				if(d.childNodes.length>0) d.removeChild(d.firstChild);
				//alert("mouse leave!!");
			}
			// 用于预览功能的部分事件 设置滤镜效果
			function setOpacity(el){
				el.style.filter="alpha(opacity:95)";
				el.style.KHTMLOpacity="0.95";
				el.style.MozOpacity="0.95";
				el.style.opacity="0.95";
			}

			//创建新的节点
			function CreateEl(t,c){
				var x=document.createElement(t);
				x.className=c;
				x.style.display="block";
				return(x);
			}
			
			//节点定位
			function Locate(e){	
				var posx=0,posy=0;
				var myVarX,myVarY;
				if(e==null) e=window.event;
				if(e.pageX || e.pageY){
					posx=e.pageX; posy=e.pageY;
					myVarX=document.documentElement.scrollLeft;
					myVarY=document.documentElement.scrollTop;
				}
				else if(e.clientX || e.clientY){
					if(document.documentElement.scrollTop){
						posx=e.clientX+document.documentElement.scrollLeft;
						posy=e.clientY+document.documentElement.scrollTop;
						myVarX=document.documentElement.scrollLeft;
						myVarY=document.documentElement.scrollTop;
					}
					else{
						posx=e.clientX+document.body.scrollLeft;
						posy=e.clientY+document.body.scrollTop;
						myVarX=document.body.scrollLeft;
						myVarY=document.body.scrollTop;
					}
				}
					posx=posx-270;
				if(document.body.offsetHeight<posy-myVarY+550){
					posy=posy-(posy-myVarY+500-document.body.offsetHeight)-100;
				}
				document.getElementById("btc").style.top=posy+"px";
				document.getElementById("btc").style.left=posx+"px";//(posx)
				
			}

</script>

<script language='javascript'>
	enableTooltips(null);
</script>
			<script type="text/javascript">
			//保存按钮是否可以点击
			function is_save(da){
					if(da==1){
						document.getElementById("save").disabled="disabled"; 
					}else{
						document.getElementById("save").disabled="";
					}
				}
			
			var drag_=false
			var D=new Function('obj','return document.getElementById(obj);')
			var oevent=new Function('e','if (!e) e = window.event;return e')
					
          //onmouseover触发时候， 编辑对象
           function Move_obj(obj){
			     var x,y;
				D(obj).onmousedown=function(e){
					drag_=true;
					with(this){
						   x = oevent(e).clientX - parseInt(D("muchose").style.left); 
			               y = oevent(e).clientY - parseInt(D("muchose").style.top); 
						 document.onmousemove=function(e){
							if(!drag_)return false;
							with(D("muchose")){
							   
								D("muchose").style.left=oevent(e).clientX-x+"px";
								D("muchose").style.top=oevent(e).clientY-y+"px";
							}
						}
					}
					document.onmouseup=new Function("drag_=false");
				}
				
			}
           //onmouseover触发时候， 编辑对象
           function Move_obj2(obj){
			     var x,y;
				D(obj).onmousedown=function(e){
					drag_=true;
					with(this){
						   x = oevent(e).clientX - parseInt(D("showdivBox").style.left); 
			               y = oevent(e).clientY - parseInt(D("showdivBox").style.top); 
						 document.onmousemove=function(e){
							if(!drag_)return false;
							with(D("showdivBox")){
								D("showdivBox").style.left=oevent(e).clientX-x+"px";
								D("showdivBox").style.top=oevent(e).clientY-y+"px";
							}
						}
					}
					document.onmouseup=new Function("drag_=false");
				}
				
			}
			//onmouseover触发时候， 编辑对象
			function Move_obj3(obj){
			     var x,y;
				D(obj).onmousedown=function(e){
					drag_=true;
					with(this){
						   x = oevent(e).clientX - parseInt(D("divBox").style.left); 
			               y = oevent(e).clientY - parseInt(D("divBox").style.top); 
						 document.onmousemove=function(e){
							if(!drag_)return false;
							with(D("divBox")){
								D("divBox").style.left=oevent(e).clientX-x+"px";
								D("divBox").style.top=oevent(e).clientY-y+"px";
							}
						}
					}
					document.onmouseup=new Function("drag_=false");
				}
				
			}
								// may**********************暂存与提交审核按钮事件**********************************************************
								   var   data = editor.getContent();
								    var pagexml = "";
									var oldvalue ="input0";
									var oldlink="10";  
									var olddiv ="div0";
									
									var modid=null;
									var creatid="";
									var parentID = 0 ;
									var pageid=0;
						    		 var showcount = 0;
									function submitui(type){ 
										var contnet = editor.getContent();
										var wx_name = document.getElementById("wx_name").value;
										
										var wx_sms = document.getElementById("wx_sms").value;
									   var pattern = "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）—|{}【】‘；：”“'。，、？]"; 
										if(wx_name==""){ 
											alert(getJsLocaleMessage("ydwx","ydwx_wxbj_44"));
											return false; 
										}else if(wx_name.length>50){
											alert(getJsLocaleMessage("ydwx","ydwx_wxbj_128"));
											return false; 
										}else{
											
											 for(var i = 0 ; i< pattern.length; i++){
	  	                                      	if(wx_name.replace(/(\s*$)/g, "").indexOf(pattern.charAt(i))>-1){
				                               alert(getJsLocaleMessage("ydwx","ydwx_wxbj_45"));
		    	                              $("#wx_name").select();
	    	                                 	return false;
			                                            }		
										}
											 }
										var wx_timeOut = document.getElementById("wx_timeOut").value;
											if(wx_timeOut==""){
												alert(getJsLocaleMessage("ydwx","ydwx_wxbj_129"));
												return false;
											}
											var a=new Date();
											a=new Date(a.getFullYear()+parseInt(1),a.getMonth(),a.getDate()+parseInt(1));
											var b=wx_timeOut.replace(/-/g, "/");//2010-04-29  2010/04/29
									    	var dt1=new Date(Date.parse(b));
						             		if(dt1>a){//比较日期
						              			alert(getJsLocaleMessage("ydwx","ydwx_wxbj_46"));
						              			return false; 
											}
										
										if(contnet==""){
											alert(getJsLocaleMessage("ydwx","ydwx_wxbj_47"));
											return false; 
										}
										var fontshow=contnet.replace(/<[^>]*>/g, "");
										fontshow=fontshow.replaceAll("&nbsp;", "");
										if(fontshow.length>20000){
											alert(getJsLocaleMessage("ydwx","ydwx_wxbj_48"));
											return false; 
										}
										var chevalue = document.getElementById(oldvalue).value.split("<#!!#>"); 
										var	content = editor.getContent();
										//保存最后操作的值
										document.getElementById(oldvalue).value=chevalue[0]+"<#!!#>"+chevalue[1]+"<#!!#>"+content+"<#!!#>"+chevalue[3]+"<#!!#>"+chevalue[4];  //保存上次的网讯名称,值 
										
										var inp = document.getElementById("input0").value;
									
										var invalue = inp.split("<#!!#>")[2];
											//判断默认页面内容不能为空
										invalue = invalue.replace(new RegExp(/(&nbsp;)/g),'')
										if(invalue=="" || invalue=="<p></p>"){ 
											alert(getJsLocaleMessage("ydwx","ydwx_wxbj_49"));
											return false; 
										}
										//当为暂存的时候直接提交不判断是否动态			
										if(type=="1"){
											var contentPage=$("input[name='checkboxzym']");
											var flag=true;
											contentPage.each(function(){
												var aCon=$(this).val().split("<#!!#>");
												var oContent=aCon[2];
												
												if(oContent==""){
													alert(aCon[0]+getJsLocaleMessage("ydwx","ydwx_wxbj_50"));
													flag=false;
												 }
												var fontsh=oContent.replace(/<[^>]*>/g, "");
												fontsh=fontsh.replaceAll("&nbsp;", "");
												if(fontsh.length>20000){
													alert(aCon[0]+getJsLocaleMessage("ydwx","ydwx_wxbj_51"));
													flag=false;
													
												}
											})
											if(!flag){return false;} 
											//alert("1");
											//document.forms['Form'].action="<%=path%>/wx_manger.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val()";
											document.forms['Form'].submit();
										}	
										//当为提交审核的时候判断是否动态								
										if(type=="2"){
										
										$("#saveAndList").val("2");
										if($("#wx_tempType1").attr("checked")){
										//<%--如果选了静态模板，却有参数，则自动选择为动态模板保存，并提示 --%>
										// checkboxzym
										var val=document.getElementById("zym").innerHTML; 
										if(val.indexOf("#P_") >= 0 || val.indexOf("#p_") >= 0){										
										     //<%-- 校验元素类型 --%>
										     var choice=confirm(getJsLocaleMessage("ydwx","ydwx_wxbj_52"));
											if($("#wx_tempType1").attr("checked") && choice){
												$("#wx_tempType2").attr("checked","true");
											}else{
												return;
											}
											//alert("静态模板页面包含变量参数，请修改网讯类型或删除变量参数!");
											//return;
										}else{
											//document.forms['Form'].action="<%=path%>/wx_manger.htm?method=find&lguserid="+$("#lguserid").val()";
												document.forms['Form'].submit();
										}
										//<%--如果选了动态模板，却没参数，则不能保存并提示 --%>

										}
										if($("#wx_tempType2").attr("checked") ){
										var val=document.getElementById("zym").innerHTML; 
										   if(val.indexOf("#P_") == -1 && val.indexOf("#p_") == -1){
										   
												alert(getJsLocaleMessage("ydwx","ydwx_wxbj_53"));
												return;
											}
										  // alert("2");
											//document.forms['Form'].action="<%=path%>/wx_manger.htm?method=find&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val()";
											document.forms['Form'].submit();
										}
										}
									}
									
									//删除节点时候 ，去掉隐藏域中该节点值
									function removezym(){
										var d1=document.getElementById(olddiv);
										d1.parentNode.removeChild(d1);
										//删除完，显示默认页面，，，同时初始化数据
										oldvalue ="input0";
										olddiv ="div0";
										var wxpageidstr=getwxpagestr('0');
										oldlink="01"+wxpageidstr;
										
										var valueandname = document.getElementById(oldvalue).value;
										pageid = valueandname.split("<#!!#>")[3];
										editor.setContent(valueandname.split("<#!!#>")[2]); //获得当前网讯的值
										
										 
										
									}
						    		//存在的网讯（暂存，修改等情况），加载时候一些值得初始化
							     function IDbyInfo(){
								     	var loadzym = "";
										var mycars=new Array();
										var main_va ;
										var lguserid = $("#lguserid").val();
										//发送请求，并将在onComplete选项中调用回调函数
										$.post('<%=path %>/wx_ueditor.htm?method=getParentUdeitor',{Action:"post",lguserid:lguserid,netid:<%=wx_netid %>},function(result){
											result = eval("("+result+")");
											if(result.success){
														var dataObj=result.msg;//转换为json对象 
														var baseObj=result.base;//转换为json对象 
														$.each(baseObj.root, function(idx,item){
																$("#wx_timeOut").val(item.timeout);
																loadzym += "<div id='div0'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input0' value=''></p></div>";   
																main_va = item.page ;
																 document.getElementById("wx_sms").value=item.wx_sms; 
																 document.getElementById("wx_name").value=item.wx_name;
																 document.getElementById("wx_netid").value=item.netid;
																 document.getElementById("useParam").value=item.useparams;
																 document.getElementById("params").value=item.hasparams;
																$("#wx_tempType"+item.temptype).attr("checked","checked");
																 editor.setContent(item.page.split("<#!!#>")[2]);
																 pageid = item.page.split("<#!!#>")[3];
																 pagexml += "<tree id='-1' ><item id='0' text='"+item.name+"' im0='dhtmlxtree_icon.gif' im1='dhtmlxtree_icon.gif' im2='dhtmlxtree_icon.gif'>"; 
														});
														 document.getElementById("zym").innerHTML=loadzym;
														if(dataObj){	
															$.each(dataObj.root, function(idx,item){
																showcount= showcount+1;
																 //获取pageid页面编号
		   														var wxpageidstr=getwxpagestr(showcount);
																	//输出每个root子对象的名称和值 
																loadzym=loadzym+"<div id='div"+showcount+"'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value=''></p></div>";
															//	document.getElementById('input1').value=item.page;    
																pagexml =pagexml +"<item id='"+showcount+"' text='"+item.name+"'></item>"; 
																mycars[idx]="input"+showcount+"<#>"+item.page+"<#!!#>01"+wxpageidstr;  
															});
															document.getElementById("zym").innerHTML=loadzym; 
															var i=0;
															for (i=0;i<mycars.length;i++)
															{
															 	var temp = mycars[i];
															 	document.getElementById(temp.split("<#>")[0]).value=temp.split("<#>")[1];
															}
														 }
														//到这里才来保存 input0 的原因是，如果在上面保存，里面的&quot;会被替换成“号，会影响到预览，所以放到 这里来
														var wxpageidstr=getwxpagestr('0');
														 document.getElementById("input0").value = main_va+"<#!!#>01"+wxpageidstr; 
														 pagexml = pagexml+"</item></tree>";
														 loadTree(pagexml);
												}else{
													alert(getJsLocaleMessage("ydwx","ydwx_common_czshb"));
												}
											});	
									}
									//修改网讯
									function UpdateUeditor(value,name,divrm,link){
										var edContent = editor.getContent(); 
										var parentli = document.getElementById(oldvalue).value.split("<#!!#>");  //取出旧的数据
										if(oldvalue!=value ){ //&& oldname!=name 不是input0 
											var valueandname = document.getElementById(value).value;  //取出当前的数据
											//保存上次的网讯名称,父结点0 子结点为父结点ID 新增子结点-1 ，内容，pageid 新增 父结点0 子结点-1  ，1+showcount	 
											document.getElementById(oldvalue).value=parentli[0]+"<#!!#>"+parentli[1]+"<#!!#>"+edContent+"<#!!#>"+parentli[3]+"<#!!#>"+parentli[4];    
											editor.setContent(valueandname.split("<#!!#>")[2]); //获得当前网讯的值  
											oldvalue = value;
											oldlink = link;
											olddiv=divrm;		
										}
									}
									
									
								</script>
	<script>
		var type=null;
		var node=0;
		//设置tabbarconteiner的高度
		function correctSizes(){
			document.getElementById('tabbarconteiner').style.height = (document.body.offsetHeight - 70)+'px'
		}
		
		/* init tree */
		var tree;
		  $("#muchose").hide();
		var tree_smpl
		
		//加载树状结构
		function loadTree(xmlstring){
			tree=new dhtmlXTreeObject("doctree_box","100%","100%",-1);
			tree.setImagePath("<%=inheritPath%>/dhtmlxTree/docsExplorer/codebase/imgs/");
			tree.setOnClickHandler(function(id){openPathDocs(id);});
			tree.attachEvent("onOpenEnd",updateTreeSize);
			//tree.loadXML("<%=path%>/dhtmlxTree/docsExplorer/xml/a.xml",autoselectNode);
			tree.loadXMLString(xmlstring,autoselectNode);
		}
		//给元素赋予样式，校验页面名字输入长度
	  function changeName(){
		      $("#cname").toggle();   
		      $("#doctree_box").css({"background-color":"#DDDDDD",width:"200px","height":'402px',"filter":"alpha(opacity=10)"
		     });
		      
		}
		$("#pageName").keyup(function(){
		         if($(this).val().length>20){
		           alert(getJsLocaleMessage("ydwx","ydwx_wxbj_54"));
		           $(this).val("");
		           return;
		         }
		});  
		
		//cOK 点击事件
		$("#cOK").click(function(){
		       var nodeid=tree.getSelectedItemId();
		        if(nodeid==""){
		         alert(getJsLocaleMessage("ydwx","ydwx_wxbj_55"));
		         return;
		      }
		      else{
		      var pageName=$("#pageName").val();
		       if($.trim(pageName)==''){
			    alert(getJsLocaleMessage("ydwx","ydwx_wxbj_56"));
             	return;
			  }
			   if(pageName.indexOf("'")!=-1  || outSpecialChar(pageName)  ){
             	alert(getJsLocaleMessage("ydwx","ydwx_wxbj_57"));
             	return;
             }
		      var node=tree._globalIdStorageFind(nodeid);
		      node.span.innerHTML=pageName;
		      var Nodevalue = document.getElementById("input"+nodeid).value;
			  var namelist = Nodevalue.split("<#!!#>"); 


			  document.getElementById("input"+nodeid).value = $("#pageName").val()+"<#!!#>"+namelist[1]+"<#!!#>"+namelist[2]+"<#!!#>"+namelist[3]+"<#!!#>"+namelist[4] ; 
		
		      $("#cname").toggle();
		      $("#doctree_box").css({
		           "background-color":"white",width:"200px",height:"402px","filter":"alpha(opacity=100)"
		       });
		      $("#pageName").val("");
		      }
		});
		//cNO 点击事件
		$("#cNO").click(function(){
		     $("#pageName").val("");
		      $("#cname").toggle();
		      $("#doctree_box").css({
		           "background-color":"white",width:"200px","filter":"alpha(opacity=100)"
		     });
		
		});
	    //点击事件
		function openPathDocs(id){
		  
		      UpdateUeditor('input'+id,'a'+id,'div'+id,'01'+id);
			
		}
		//更新树状的的高度
		function updateTreeSize(){
			this.allTree.style.overflow = "visible";
			this.allTree.style.height = this.allTree.scrollHeight+"px";
			
		}
		function autoselectNode(){
				tree.selectItem(node,true);tree.openItem(node)
		}  
			
		//获取page页面编号
		function getwxpagestr(showcount){
			var wxpageid= '';//$("#wxpageid").val();
			return wxpageid+"_"+showcount; 
		}
		
		//添加子节点
		function newTree(){
		   var nodeid=tree._globalIdStorageFind("0");  //根据iD 获取节点
		   var text=nodeid.span.innerHTML;               //得到节点的名称
		   var count=tree._getLeafCount(nodeid);   //得到节点下子节点数
		  
		   if(count>8){
		       alert(getJsLocaleMessage("ydwx","ydwx_wxbj_58"));
		       return;
		   }
		   else{
		   showcount = showcount+1;
		   //获取pageid页面编号
		   var wxpageidstr=getwxpagestr(showcount);
		   tree.insertNewItem("0",showcount,"page"+(showcount));   //增加子节点  第五个参数超链接用，以1+showcount
	       document.getElementById("zym").innerHTML=document.getElementById("zym").innerHTML+"<div id='div"+showcount+"'><p style='display:none;'><input type='checkbox' name='checkboxzym'  checked='checked' id='input"+showcount+"' value='page"+showcount+"<#!!#>-1<#!!#><p></p><#!!#>-1<#!!#>01"+wxpageidstr+"'></p></div>"; 
		   }
		}
		
		//删除子节点
		function delNode(){
			var nodeid=tree.getSelectedItemId();
	        if(nodeid==""){
	            alert(getJsLocaleMessage("ydwx","ydwx_wxbj_55"));
	            return;
	        }
	        if(nodeid==0){
		        alert(getJsLocaleMessage("ydwx","ydwx_wxbj_59"));
		        return;
		    }
	        
			if(confirm(getJsLocaleMessage("ydwx","ydwx_wxbj_60"))){
	            removezym() ;
		        tree.deleteItem(nodeid,true);
			}
		}
		
	// 用来得到网讯预览中有引用子页面  
		function mylink(s,c,Custom){ 
			var chevalue = document.getElementById(oldvalue).value.split("<#!!#>"); 
			var	content = editor.getContent();
			//保存最后操作的值
			document.getElementById(oldvalue).value=chevalue[0]+"<#!!#>"+chevalue[1]+"<#!!#>"+content+"<#!!#>"+chevalue[3]+"<#!!#>"+chevalue[4];  //保存上次的网讯名称,值 
			
			for(li = 0 ;li<=showcount;li++){ 
				if(document.getElementById("input"+li)){
					
					var _v = document.getElementById("input"+li).value;
					
					if(_v.split("<#!!#>")[4].replace(new RegExp("#link#","g"),"")==c || s.indexOf("w="+_v.split("<#!!#>")[3])>-1 ){
						
						document.getElementById("netInfoDiv").innerHTML=_v.split("<#!!#>")[2]; 
						//$(document.getElementById('nm_preview_common').contentWindow.document.body).html(_v.split("<#!!#>")[2]); 
						$(document.getElementById('nm_preview_common').contentWindow.document.body).html(_v.split("<#!!#>")[2].replace(/javascript:mylink/g,'javascript:parent.mylink'));  
					}
				}
				
			}
		}
</script>
		<script language='javascript'>
			var currentPageNum=1;
			var currentSectionNum="";
			//AJAX的调用
			function GetXmlHttpObject()
			{
			  var xmlHttp=null;
			  try
			    {
			    xmlHttp=new XMLHttpRequest();
			    }
			  catch (e)
			    {
			    try
			      {
			      xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
			      }
			    catch (e)
			      {
			      xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
			      }
			    }
			  return xmlHttp;
			}

			//状态修改后相关处理（预览图片变大等）
			function stateChanged() 
			{ 
			  if (xmlHttp.readyState==4)
			  { 
				document.getElementById("mainPageTemplateContainer").innerHTML=xmlHttp.responseText;
				enableTooltips();
				hideTooltip(null);
			  }
			}
			//处理编辑框内容
			function templateStateChanged() 
			{ 
			
			  if (xmlHttp.readyState==4)
			  {  	
				var text = xmlHttp.responseText;
			  	editor.setContent(text);
				//enableTooltips();暂时注释掉，以后可能有用
				
			  }
			}
 	/**function templateApplyConfirm(str)
 	{

 		var templateId=$("#templateId").val();
 		str=templateId;
    	$("#muchose").show();
    	$("#overlay1").show();
    	if(document.getElementById("deck")!="" && document.getElementById("deck")!=null)
        {
     		cancel();
   		}
    	modid=str;
    	$.post("wx_manger.htm?method=findCreatid",{"netid":str},function(data){
        	creatid=data;
    	});
 	}*/

	//选择模板
	
	function templateApply(str){
		document.getElementById('templateID').value=str;
		document.getElementById('templateImage').src="<%=request.getContextPath()%>"+"/ydwx/images/templateImage/"+str+".jpg";
		showDlg();
		
	}
	
	
	//显示选择的类别的选择页面的数据
	function showPage(showPageNum){
		if(showPageNum==currentPageNum) return;
		xmlHttp=GetXmlHttpObject()
 	    if (xmlHttp==null)
    	{
    		alert (getJsLocaleMessage("ydwx","ydwx_wxbj_42"));
    		return;
    	}

		var url="<%=request.getContextPath()%>/ydwx/createWX/template.jsp";
		url=url+"?section="+currentSectionNum;
		url=url+"&pageNum="+showPageNum;
		xmlHttp.onreadystatechange=stateChanged;
		xmlHttp.open("GET",url,true);
		xmlHttp.send(null);
		currentPageNum = showPageNum;//这个时候要将当前页码值进行设置
	}
	//显示第一页
	function showPreviousPage(){
		showPage(currentPageNum-1);//不用考虑最后一页和第一页的问题，由image.jsp处理
	}
	
	//显示下页
	function showNextPage(){
		showPage(currentPageNum+1);
	}
	// 查询模板
	function selectTemplate(showSectionNum){
		showSectionNum =showSectionNum.value;
		if(currentSectionNum==showSectionNum) return;
		xmlHttp=GetXmlHttpObject()
 	    if (xmlHttp==null)
    	{
    		alert (getJsLocaleMessage("ydwx","ydwx_wxbj_42"));
    		return;
    	}

		var url="<%=request.getContextPath()%>/ydwx/createWX/template.jsp";
		url=url+"?section="+showSectionNum;
		url=url+"&pageNum="+1;
		xmlHttp.onreadystatechange=stateChanged;
		xmlHttp.open("GET",url,true);
		xmlHttp.send(null);
		currentSectionNum = showSectionNum;//这个时候要将当前类别进行设置
		currentPageNum=1;
	}
	 
</script>
<%-- ajax end --%>
<%-- popup div begin --%>


<script type="text/javascript">
    function showDlg()
    {
        //显示遮盖的层
        var objDeck = document.getElementById("deck");
        if(!objDeck)
        {
            objDeck = document.createElement("div");
            objDeck.id="deck";
            document.body.appendChild(objDeck);
        }
        objDeck.className="showDeck";
        objDeck.style.filter="alpha(opacity=50)";
        objDeck.style.opacity=40/100;
        objDeck.style.MozOpacity=40/100;
        //显示遮盖的层end
        
        //禁用select
        hideOrShowSelect(true);
        
        //改变样式
        document.getElementById('divBox').className='showDlg';
        
        //调整位置至居中
        adjustLocation();
       $("#overlay1").toggle();
        
    }
    // 隐藏层
    function cancel()
    {
        document.getElementById('divBox').className='hideDlg';
        document.getElementById("deck").className="hideDeck";
        hideOrShowSelect(false);
        $("#overlay1").toggle();
    }
    //隐藏或者显示列表
    function hideOrShowSelect(v)
    {
        var allselect = document.getElementsByTagName("select");
        for (var i=0; i<allselect.length; i++)
        {
            //allselect[i].style.visibility = (v==true)?"hidden":"visible";
            allselect[i].disabled =(v==true)?"disabled":"";
        }
    }
    //调整位置
    function adjustLocation()
    {
        var obox=document.getElementById('divBox');
        if (obox !=null && obox.style.display !="none")
        {
            var w=239;
            var h=450;
            var oLeft,oTop;
            
            if (window.innerWidth)
            {
                oLeft=window.pageXOffset+(window.innerWidth-w)/2 +"px";
                oTop=window.pageYOffset+(window.innerHeight-h)/2 +"px";
            }
            else
            {
                var dde=document.documentElement;
                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2 +"px";
                oTop=dde.scrollTop+(dde.offsetHeight-h)/2 +"px";
            }
            
            obox.style.left=oLeft;
            obox.style.top=oTop;
        }
    }
    
</script>

    <%-- popup div end --%>
<%-- yuxd end --%>
<%--网讯预览 --%>
<script type="text/javascript">

	function Look(){
		var $pre = $('#nm_preview_common');
		$pre.replaceWith($pre.empty().clone());
		var htmltmp=editor.getContent();
		htmltmp=htmltmp.replace(/javascript:mylink/g,'javascript:parent.mylink');
		setTimeout(function(){//ie下不延时 打开页面空白？
			$(document.getElementById('nm_preview_common').contentWindow.document.body).html(htmltmp);
			$("#divBox1").dialog("open");
			},100);
	}
    function showNetDlg()
    {
    
   		document.getElementById("netInfoDiv").innerHTML=editor.getContent();

        //禁用select
     	hideOrShowSelect();
        //改变样式
        document.getElementById('showdivBox').className='showNetbox';
             
        //调整位置至居中
      // Location();
    ShowLocationNet();
   $("#overlay1").toggle();
    }
    
     //20111215调整居中
     function ShowLocationNet()
    {
  
        var netbox=document.getElementById('showdivBox');
        var imgs = document.getElementById('img');
        
        var netInfo = document.getElementById('netInfoDiv');
      
         if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){
    
			if (netbox !=null && netbox.style.display !="none")
	        {
	
	        
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	         
            if (window.innerWidth)
            {
                oLeft=window.pageXOffset+(window.innerWidth-w)/2.5 +"px";
                oTop=window.pageYOffset+(window.innerHeight-h)/2.5 +"px";
            }
            else
            {
           
                var dde=document.documentElement;
                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2.5 +"px";
                oTop=dde.scrollTop+(dde.offsetHeight-h)/2.5 +"px";
            }
            
                netInfo.style.left="0px";
                netInfo.style.top="31px";
                
                imgs.style.left="0px";
	            imgs.style.top="-45px"; 
	            netbox.style.left=oLeft;
	            netbox.style.top=oTop;
	        }
	        
		}else{
	
	        if (netbox !=null && netbox.style.display !="none")
	        {
	 
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	            
	            if (window.innerWidth)
	            {
	      
	                oLeft=window.pageXOffset+(window.innerWidth-w)/2.5 +"px";
	                oTop=window.pageYOffset+(window.innerHeight-h)/2.5 +"px";
	                
	                
	            }
	            else
	            {
	       
	                var dde=document.documentElement;
	                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2.5 +"px";
	                oTop=dde.scrollTop+(dde.offsetHeight-h)/2.5 +"px";
	                
	            }
	            
	           
	           imgs.style.left="0.8px";
	           imgs.style.top="3px"; 
	            netbox.style.left=oLeft;
	            netbox.style.top=oTop;
	        }
	    }
    }
    
    
    function cancelDiv()
    {
        document.getElementById('showdivBox').className='hideDlg';
       
        hideOrShowSelect(false);
        $("#overlay1").toggle();
    }
     //禁用select
    function hideOrShowSelect(v)
    {
        var allselect = document.getElementsByTagName("select");
        for (var i=0; i<allselect.length; i++)
        {
            //allselect[i].style.visibility = (v==true)?"hidden":"visible";
            allselect[i].disabled =(v==true)?"disabled":"";
        }
    }
    //调整位置至居中
    function Location()
    {
        var netbox=document.getElementById('showdivBox');
        var imgs = document.getElementById('img');
         if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){
			if (netbox !=null && netbox.style.display !="none")
	        {
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	            
	            if (window.innerWidth)
	            {
	                oLeft=window.pageXOffset+(window.innerWidth-w)/2 +"px";
	                oTop=window.pageYOffset+(window.innerHeight-h)/2 +"px";
	            }
	            else
	            {
	                var dde=document.documentElement;
	                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2 +"px";
	                oTop=dde.scrollTop+(dde.offsetHeight-h)/2 +"px";
	            }
	            netbox.style.left=oLeft;
	            netbox.style.top=oTop;
	        }
	        
		}else{
	        if (netbox !=null && netbox.style.display !="none")
	        {
	            var w=239;
	            var h=450;
	            var oLeft,oTop;
	            
	            if (window.innerWidth)
	            {
	                oLeft=window.pageXOffset+(window.innerWidth-w)/2 +"px";
	                oTop=window.pageYOffset+(window.innerHeight-h)/2 +"px";
	                
	                
	            }
	            else
	            {
	                var dde=document.documentElement;
	                oLeft=dde.scrollLeft+(dde.offsetWidth-w)/2 +"px";
	                oTop=dde.scrollTop+(dde.offsetHeight-h)/2 +"px";
	                
	            }
	            
	           
	            imgs.style.left="0.8px";
	            imgs.style.top="3px"; 
	            netbox.style.left=oLeft;
	            netbox.style.top=oTop;
	        }
	    }
    }
    
</script>
 <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js"></script>
 <script type="text/javascript">
	$(function(){
	    //解决英文状态下，隐藏不起作用
	  $('#sortId').isSearchSelect({'width':'110','isInput':false,'zindex':0},function(o){
	    chageTemplate(o.value);
	  });
   })
	</script>
		</body>
	</html>

 