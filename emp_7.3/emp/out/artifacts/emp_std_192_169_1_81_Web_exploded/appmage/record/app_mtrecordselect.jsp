<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%
	String path=request.getContextPath();
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	//按钮权限
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	List<DynaBean> trustDatas = (List<DynaBean>)request.getAttribute("mtRecordList");
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String,String> conditionMap = (LinkedHashMap)request.getAttribute("conditionMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	//操作员
	String userid = request.getParameter("userid");
	String menuCode = titleMap.get(rTitle);
	String result = (String)session.getAttribute("trustdataResult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		String msg_type=conditionMap.get("msg_type");
		
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
<title><%=titleMap.get(menuCode)%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=inheritPath%>/common/css/mobilePreview.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<style type="text/css">
	.emo{
		width:24px;
	}
	#bubbleContent .img{
		width:115px;
	}
	</style>
<script type="text/javascript">
	
	function show(){
		var res = <%=result%>;
		if(res != null && res !=""){
			<%session.removeAttribute("trustdataResult");%>
			if(res){
				// alert("操作成功！");
				alert(getJsLocaleMessage('appmage','appmage_page_mtrecordselect_optsuc'));	
			}else {
				//alert("操作失败！");
				alert(getJsLocaleMessage('appmage','appmage_page_mtrecordselect_optfalse'));	
			}
		}
		
	}
</script>
<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
</head>
<body onload="show();">
<div id="container" class="container">
	<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
	<%-- header结束 --%>
		<div id="rContent" class="rContent">
		<%if(btnMap.get(menuCode+"-0")!=null){%>
		<form name="pageForm" action="<%=path%>/app_mtrecordselect.htm" method="post" id="pageForm">
		<div id="loginUser" class="hidden"></div>
			<input type="hidden" id="pathUrl" value="<%=path%>" />
				<div class="buttons">
				<div id="toggleDiv"> </div>
				<a id="exportCondition"><emp:message key="appmage_common_opt_daochu" defVal="导出" fileName="appmage"></emp:message></a>
			</div>
			
	<div id="condition">
		<table width="100%">
			<tr>
				<td>
					<span><emp:message key="appmage_xxfb_appfsrw_text_caozuoyuan" defVal="操作员" fileName="appmage"></emp:message>：</span>
				</td>
				
					<td class="condi_f_l">											
			<div >											
			 <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
			<input type="text" id="userName" class="treeInput" style="width:160px;" name="userName" value="<%=conditionMap.get("userName")==null?MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_qingxuanze",request):conditionMap.get("userName")%>" onclick="javascript:showMenu();" readonly 	style="width:138px;cursor: pointer;"/>&nbsp;
			</div>											
			<div id="dropMenu">
				<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>	
				<div style="margin-top: 3px;margin-right:10px;text-align:right">
					<input type="button" value="<emp:message key="appmage_common_opt_queding" defVal="确定" fileName="appmage"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK();" style=""/>&nbsp;&nbsp;
					<input type="button" value="<emp:message key="appmage_common_opt_qingkong" defVal="清空" fileName="appmage"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep();" style=""/>
				</div>	
				<div style="margin-top:3px;padding-left:4px;"><font class="zhu"><emp:message key="appmage_xxfb_appfsrw_text_tips" defVal="注：请勾选操作员进行查询" fileName="appmage"></emp:message></font></div>
				<ul  id="dropdownMenu" class="tree"></ul>	
			</div>										   										
		</td>
				
				
				<td>
					<span><emp:message key="appmage_xxfb_appfsrw_text_contenttype" defVal="内容类型" fileName="appmage"></emp:message>：</span>
				</td>
				<td>
				<select name="msg_type" id="msg_type" style="width:185px">
				<option value=""><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
				<option value="0" <%if(("0").equals(msg_type)) {%>selected="selected" <%}%> ><emp:message key="appmage_xxfb_appxxfs_text_word" defVal="文字" fileName="appmage"></emp:message></option>
				<option value="1" <%if(("1").equals(msg_type)) {%>selected="selected" <%}%> ><emp:message key="appmage_xxfb_appfsrw_text_image" defVal="图片" fileName="appmage"></emp:message></option>
				<option value="2" <%if(("2").equals(msg_type)) {%>selected="selected" <%}%>><emp:message key="appmage_xxfb_appfsrw_text_video" defVal="视频" fileName="appmage"></emp:message></option>
				<option value="3" <%if(("3").equals(msg_type)) {%>selected="selected" <%}%>><emp:message key="appmage_xxfb_appfsrw_text_audio" defVal="音频" fileName="appmage"></emp:message></option>
				</select>
				</td>
							<td><emp:message key="appmage_xxfb_appfsrw_text_subject" defVal="发送主题" fileName="appmage"></emp:message>：</td>
				<td>
				<input type="text" id="title" name="title" style="width: 180px;" value="<%=conditionMap.get("title")== null?"":conditionMap.get("title") %>">
				</td>
				<td>

					<td class="tdSer">
					<center><a id="search"></a></center>
					</td>
			</tr>
			<tr>
			<td><emp:message key="appmage_xxfb_appfsjl_text_reseveobject" defVal="接收对象" fileName="appmage"></emp:message>：</td>
				<td>
				<input type="text" id="tousername" name="tousername" style="width: 180px;" value="<%=conditionMap.get("tousername")== null?"":conditionMap.get("tousername") %>">
				</td>
				<td>
					<span><emp:message key="appmage_xxfb_appfsjl_text_sendstatus" defVal="发送状态" fileName="appmage"></emp:message>：</span>
				</td>
				<td>
					<select name="sendstate" id="sendstate" style="width:184px">
					<%String sendst=conditionMap.get("sendstate");
					 %>
					 <option value=""><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
						<option value="1" <%="1".equals(sendst)?"selected":"" %>><emp:message key="appmage_xxfb_appfsrw_text_suc" defVal="成功" fileName="appmage"></emp:message></option>
						<option value="2" <%="2".equals(sendst)?"selected":"" %>><emp:message key="appmage_xxfb_appfsrw_text_false" defVal="失败" fileName="appmage"></emp:message></option>
						<option value="3" <%="3".equals(sendst)?"selected":"" %>><emp:message key="appmage_xxfb_appfsjl_text_noreturn" defVal="未返" fileName="appmage"></emp:message></option>
					</select>
				</td>
					<td>
					<span><emp:message key="appmage_xxfb_appfsjl_text_returnstatus" defVal="回执状态" fileName="appmage"></emp:message>：</span>
				</td>
	    			<td>
					<select name="rptstate" id="rptstate" style="width:184px">
					<%String rptstate=conditionMap.get("rptstate")==null?"":conditionMap.get("rptstate");
					 %>
					 <option value=""><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
					 <option value="-1" <%="-1".equals(rptstate)?"selected":"" %>><emp:message key="appmage_xxfb_appfsjl_text_unknow" defVal="未知" fileName="appmage"></emp:message></option>
					 <option value="0" <%="0".equals(rptstate)?"selected":"" %>><emp:message key="appmage_xxfb_appfsjl_text_read" defVal="已读" fileName="appmage"></emp:message></option>
					<option value="1" <%="1".equals(rptstate)?"selected":"" %>><emp:message key="appmage_xxfb_appfsjl_text_resavefalse" defVal="接收失败" fileName="appmage"></emp:message></option>
					</select>
				</td>
				<td></td>
				<td></td>
			</tr>
			<tr>
			 <td><emp:message key="appmage_xxfb_appfsrw_text_sendtime" defVal="发送时间" fileName="appmage"></emp:message>：</td>
				 <td>
					<input type="text" name="createtime" id="createtime"
						style="cursor: pointer; width: 180px;background-color: white;" 
						class="Wdate" readonly="readonly" onclick="stime()"
						value="<%=conditionMap.get("createtime")== null?"":conditionMap.get("createtime") %>" />
						
				 </td>	
				 <td><emp:message key="appmage_common_text_zhi" defVal="至：" fileName="appmage"></emp:message></td>
				 <td><input type="text" name="endtime" id="endtime"
						style="cursor: pointer; width: 180px;background-color: white;" 
						class="Wdate" readonly="readonly" onclick="rtime()"
						value="<%=conditionMap.get("endtime")== null?"":conditionMap.get("endtime") %>"/></td>		    
				 <td></td>	
				 <td></td>
				 <td></td>
				 <td></td>
			</tr>
			
		</table>
	</div>
			<table id="content">
				<thead>
				  <tr>
				  		<th><emp:message key="appmage_xxfb_appfsrw_text_caozuoyuan" defVal="操作员" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_xxfb_appfsrw_text_subject" defVal="发送主题" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_xxfb_appfsrw_text_content" defVal="发送内容" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_xxfb_appfsrw_text_contenttype" defVal="内容类型" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_xxfb_appfsjl_text_reseveobject" defVal="接收对象" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_xxfb_appfsjl_text_sendstatus" defVal="发送状态" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_xxfb_appfsrw_text_sendtime" defVal="发送时间" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_xxfb_appfsjl_text_returnstatus" defVal="回执状态" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_xxfb_appfsjl_text_returntime" defVal="回执时间" fileName="appmage"></emp:message></th>
				  </tr>
				</thead>
					<tbody>
							<%
							DynaBean data = null;
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="9" align="center"><emp:message key="appmage_xxfb_appfsrw_text_chilkdata" defVal="请点击查询获取数据" fileName="appmage"></emp:message></td></tr>
					<%
							} else if(trustDatas != null && trustDatas.size()>0){
						for(int v=0;v<trustDatas.size();v++){
							 data=trustDatas.get(v);
							 String msgtype=data.get("msg_type")+"";
							 String id=data.get("id")+"";
							 String msgtext=data.get("content")+"";
					%>
								<tr>
									<td><%=data.get("name")==null?"":data.get("name") %></td>
									<td><xmp><%=data.get("title")==null?"":data.get("title") %></xmp></td>
									<td>
									<%if("4".equals(msgtype)){out.print("-");}else{ %>
								<a href="javascript:prev(<%=id%>);"><xmp><%if("0".equals(msgtype)){out.print(StringUtils.abbreviate(msgtext,20));}else{out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appsyfb_opt_liulan",request));} %></xmp></a>
								<%}%>
									</td>
									<td>
									<%
									if("0".equals(msgtype)){
									out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appxxfs_text_word",request));
									}else if("1".equals(msgtype)){
									out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_image",request));
									}else if("2".equals(msgtype)){
									out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_video",request));
									}else if("3".equals(msgtype)){
									out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_audio",request));
									}else if("4".equals(msgtype)){
									out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_tuisong",request));
									}else {
									out.print("-");
									}
									 %>
									</td>
									<td><%=data.get("tousername")==null?"":data.get("tousername") %></td>
									<td>
									<%

										String showstate=data.get("rpt_state")==null?"":data.get("rpt_state")+"";
										if("0".equals(showstate)){ 
										         out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_suc",request));
										}else if("1".equals(showstate)){
										        out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_false",request));
										}else{ 
												out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appfsjl_text_noreturn",request));
										}
										
										String sendtime="-";
										if(data.get("createtime")!=null){
											sendtime=data.get("createtime")+"";
											if(sendtime.lastIndexOf(".")>0){
												sendtime=sendtime.substring(0,sendtime.lastIndexOf("."));
											}
										}
										
										String recrpttime="-";
										if(data.get("recrpttime")!=null){ 
											recrpttime=data.get("recrpttime")+"";
											if(recrpttime.lastIndexOf(".")>0){
												recrpttime=recrpttime.substring(0,recrpttime.lastIndexOf("."));
											}
										}

									%>
									</td>
									<td><%=sendtime %></td>
									<td>
									<%
										if("0".equals(showstate)){
											out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appfsjl_text_read",request));
										}else if("1".equals(showstate)){
											out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appfsjl_text_resavefalse",request));
										}else {
											out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appfsjl_text_unknow",request));
										}
									%>
									</td>
									<td><%=recrpttime%></td>
									
								</tr>
						<%} 
						}else {%>
						
						<tr><td colspan="<%=9 %>"><emp:message key="appmage_common_text_nodata" defVal="无记录" fileName="appmage"></emp:message></td></tr>
					<%} %>
				</tbody>
				<tfoot>
						<tr>
						<td colspan="<%=9 %>">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
		</table>
		</form>
		<%} %>
		</div>
			<%-- 内容结束 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
					<div id="modify" title="<emp:message key="appmage_xxfb_appfsrw_text_smscontent" defVal="信息内容" fileName="appmage"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
				<table width="100%">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td>
								<label id="msg" style="width:100%;height:100%"></label>
								 
							</td>
							 
						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
		<div class="clear"></div>
		
		</div>
		    <%--手机模拟预览 --%>
		    <%--去掉背景<div id="previewBg"></div> --%>
    <div id="mobilePreview">
    	<div id="appDrag" class="appDrag"></div>
		<div class="effectArea">
		    <div class="appClose"></div>
				<div class="mobileModel">
					<div class="effectBox">
					 <%--去掉手机标题
						<div class="effect_hd" id="subTitle"></div> --%>
						<div class="effect_bd">
							<div class="tabsContent">
								<div class="effect_dialog" data-tab="txt">
									<div class="avitor">
										<img src="<%=inheritPath%>/common/img/mobilePreview/avitor.gif" alt="">
									</div>
									<div class="bubbleArea">
										<span class="triangle"></span>
										<span class="triangleContour"></span>
										<div class="bubbleContent" id="bubbleContent">
											<emp:message key="appmage_xxfb_appfsjl_text_text" defVal="文本内容!" fileName="appmage"></emp:message>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
	</div>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=iPath %>/js/app_mtrecordselect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/appDrap.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath %>/common/widget/video/swfobject.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<%-- 加载JS 文件 --%>
<script type="text/javascript">

	$(document).ready(function() {
		getLoginInfo("#loginUser");
	    //参数是要隐藏的下拉框的div的id数组，
		   closeTreeFun(["dropMenu"]);

           getLoginInfo("#smssendparams");//加载头文件内容 
		     var lgcorpcode =$("#lgcorpcode").val();
			 var lguserid =$("#lguserid").val();
			//请求名称
			htmName="app_mttaskselect.htm";
			//操作员树
		    setting.asyncUrl = htmName+"?method=createUserTree&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
			setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
			zTree = $("#dropdownMenu").zTree(setting, zNodes);
			zTree.expandAll(true);
			 
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		showPreview();
		$('.appClose').click(function(){
			$('#mobilePreview,#previewBg').hide();
			$('#bubbleContent').html('');
		})
		$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
		$("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
		}, function() {
			$(this).removeClass("hoverColor");
		});
		//导出全部数据到excel
		$("#exportCondition").click(
		 function(){
			  if(confirm(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportexcel')))
			   {
			   		var userid = $("#userid").val();
			   		var msgtype = $("#msg_type").val();
			   		var tousername= $("#tousername").val();
			   		var sendstate = $("#sendstate").val();
			   		var createtime = $("#createtime").val();
			   		var endtime = $("#endtime").val();
			   		var rptstate=$("#rptstate").val();
			   		var title=$("#title").val();
			   		var lgcorpcode =<%=request.getParameter("lgcorpcode")%>;
		            var lguserid =<%=request.getParameter("lguserid")%>;
			   		<%if (trustDatas!=null&&trustDatas.size()>0||trustDatas!=null&&trustDatas.size()>0)  {
			   		if(pageInfo.getTotalRec()<=500000){
			   		%>			
   					$.ajax({
						type:"POST",
						url: "<%=path%>/app_mtrecordselect.htm",
						data: {method: "exportExcel",
			   				userid:userid,
			   				msg_type:msgtype,
			   				tousername:tousername,
			   				sendstate:sendstate,
			   				createtime:createtime,
			   				endtime:endtime,
			   				lgcorpcode:lgcorpcode,
			   				lguserid:lguserid,
			   				title:title,
			   				rptstate:rptstate
		   					},
		   				beforeSend: function(){
		   					page_loading();
						},
						complete:function(){
							page_complete();
						},
						success: function(result){
							if(result)
							{
								download_href("<%=path%>/app_mtrecordselect.htm?method=downloadFile");
							}else
							{
								//alert("导出失败！");
								alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportfalse'));	
							}
						}
					});
			   		
			   		<%}else{%>
	   		    //alert("数据量超过导出的范围50万，请从数据库中导出！");
	   		    alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_datatoobig'));	
	   				<%}
			   		} else {%>
			   			//alert("无数据可导出！");
			   			alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_nodatatoexport'));	
			   		<%}
			   		%>
			   	}
		  });
		$('#search').click(function(){submitForm();});
		
	});
	var videoPath="<%=inheritPath%>/common/widget/video/";
	var path="<%=path %>";
	function prev(id){
		$.ajax({
			type:"POST",
			url: "app_mtrecordselect.htm",
			data: {method: "prev",id:id},
			beforeSend: function(){
				showPreview();
				$('#mobilePreview,#previewBg').show();
				$('#bubbleContent').html("<img class='img' src='<%=inheritPath%>/common/img/mobilePreview/load.gif'/>");
			},
			success: function(result){
				var data = eval('('+result+')');
				if(data.errcode == -1){
					$('#bubbleContent').html('<font color="red">'+data.errmsg+'</font>');
				}else{
					if(data.msgtype == 0){
						//$('#subTitle').html(data.title);
						$('#bubbleContent').html(data.msgtext);
					}else if(data.msgtype == 1){
						if(data.msgurl==null||data.msgurl==""){
							$('#mobilePreview,#previewBg').hide();
							// alert("图片不存在或者图片路径不正确!");
							alert(getJsLocaleMessage('appmage','appmage_page_mtrecordselect_imagepatherror'));	
							return;
						}
						//$('#subTitle').html(data.title);
						$('#bubbleContent').html('<img src="'+data.msgurl+'" width="110"/>');
					}else if(data.msgtype == 2){
						if(data.msgurl==null||data.msgurl==""){
							$('#mobilePreview,#previewBg').hide();
							// alert("视频不存在或者视频路径不正确!");
							alert(getJsLocaleMessage('appmage','appmage_page_mtrecordselect_videopatherror'));	
							return;
						}
						$('#bubbleContent').html('');
						var videoParams={
							'route':videoPath,
							'width':115,
							'height':80,
							'fileUrl':path+"/"+data.msgurl,
							'wrap':'bubbleContent'
						};
						createVideoPreview(videoParams);
					}else if(data.msgtype == 3){//音频
						if(data.msgurl==null||data.msgurl==""){
							$('#mobilePreview,#previewBg').hide();
							// alert("音频不存在或者音频路径不正确!");
							alert(getJsLocaleMessage('appmage','appmage_page_mtrecordselect_audiopatherror'));	
							return;
						}
						var mp3Array=[],temp;
						var dataurl = videoPath+'/mp3/dewplayer-mini.swf?mp3='+data.msgurl;
						mp3Array.push('<object style="width:20px" type="application/x-shockwave-flash" data="'+dataurl+'" width="160" height="20" id="dewplayer-mini">');
						mp3Array.push('<param name="wmode" value="transparent" />');
						mp3Array.push('<param name="movie" value="'+dataurl+'" />');
						mp3Array.push('</object>');
						temp=mp3Array.join('');
						$('#bubbleContent').html(temp);
					}else{
						//$('#subTitle').html(data.title);
						$('#bubbleContent').html('');
					}
				}
				//showPreview();
				//$('#mobilePreview,#previewBg').show();
			},
			error: function(){
				//alert('请求异常！');
				alert(getJsLocaleMessage('appmage','appmage_common_requesterror'));	
			}
		})
	
	}
	
	function showPreview(){
		var bg=$('#previewBg');
		if(bg.is(':visible')){
			var clientHeight=$(window).height();
			$('#previewBg').css({'height':clientHeight});
		}
	}
	$(window).resize(function(){
		showPreview();
	})
	function rtime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#createtime").attr("value");
		    var min="1900-01-01 00:00:00";
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
				//max = year+"-"+mon+"-"+day+" 23:59:59"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
		
		};

	function stime(){
	    var max = "2099-12-31 23:59:59";
	    var v = $("#endtime").attr("value");
	    var min = "1900-01-01 00:00:00";
		if(v.length != 0)
		{
		    //max = v;
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
		}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
	
	};	
</script>		
</body>
</html>