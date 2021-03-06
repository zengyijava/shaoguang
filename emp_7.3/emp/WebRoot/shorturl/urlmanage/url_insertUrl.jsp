<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Calendar"%>
<%@page import="java.util.LinkedHashMap"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.shorturl.surlmanage.entity.LfDomain"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	String langName = (String)session.getAttribute("emp_lang");
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.indexOf("/",1));
	
	String netUrl = (String)request.getAttribute("netUrl");
	String netUrlId = (String)request.getAttribute("netUrlId");
	String domainId = (String)request.getAttribute("domainId");
	String vaildays = (String)request.getAttribute("vaildays");
	List<LfDomain> domainList = (List<LfDomain>)request.getAttribute("domainlist");
	if(StringUtils.isBlank(netUrl)){
		netUrl = "";
	}
	if(StringUtils.isBlank(netUrlId)){
		netUrlId = "";
	}
	if(StringUtils.isBlank(domainId)){
		domainId = "";
	}
	if(StringUtils.isBlank(vaildays)){
		vaildays = "7";
	}
	
	
	
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link
			href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link
			href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link
			href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link rel="stylesheet"
			href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet"
			href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>"
			type="text/css">
		<link rel="stylesheet"
			href="<%=iPath %>/css/url_insertUrl.css?V=<%=StaticValue.getJspImpVersion()%>"
			type="text/css">
			
		<script language="javascript"
			src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"
			type="text/javascript"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>

		
		
	</head>
	<body>
		<div id="container" class="container">
			
			<%-- ???????????? --%>
			
			<div id="rContent" class="rContent">
				<input type="hidden" value="<%=inheritPath %>" id="inheritPath"/>
				<form name="pageForm" action="urlcommonSMS.htm?method=getNetUrl" method="post"
					id="pageForm">
					<div class="shorurl_div shorurl_div_first">
						<span class="shorurl_span">????????????</span>
						
						<% if(domainList!=null && !domainList.isEmpty()){
							for(int i=0;i<domainList.size();i++){
							
								if(i>1){
									break;
								}%>
							<div class="shorurl_domain_radio" >
								<input id="domainId" name="domainId" type="radio"  onclick="javascript:doLen();"
									value="<%= domainList.get(i).getId()%>"  <%=domainId.equals(domainList.get(i).getId().toString())?"checked":"" %>/>
								<span><%= domainList.get(i).getDomain()%></span>
								<input id="domainvDay<%= domainList.get(i).getId()%>" type="hidden" value="<%=domainList.get(i).getValidDays() %>"/>
							</div>
						<% }
							}%>
							<span>?????????<a id="domainLength">0</a>??????</span><span class="shorurl_div_span">  ????????????????????????????????????</span>
					</div>
					
					
						
						<% if(domainList!=null && domainList.size()>2){
							for(int i=2;i<domainList.size();i++){
								if((i-2)%2==0){
								%>
						<div class="shorurl_bbq">
							<div class="shorurl_domain_radio">
								<input id="domainId" name="domainId" type="radio"  onclick="doLen();"
									value="<%= domainList.get(i).getId()%>" <%=domainId.equals(domainList.get(i).getId().toString())?"checked":"" %>/>
								<span><%= domainList.get(i).getDomain()%></span>
								<input id="domainvDay<%= domainList.get(i).getId()%>" type="hidden" value="<%=domainList.get(i).getValidDays() %>"/>
							</div>
							<%}else{ %>
							<div class="shorurl_domain_radio">
								<input id="domainId" name="domainId" type="radio"  onclick="doLen();"
								value="<%= domainList.get(i).getId()%>" <%=domainId.equals(domainList.get(i).getId().toString())?"checked":"" %>/>
								<span><%= domainList.get(i).getDomain()%></span>
								<input id="domainvDay<%= domainList.get(i).getId()%>" type="hidden" value="<%=domainList.get(i).getValidDays() %>"/>
							</div>
							<%} 
							if((i-1)%2==0 || (i+1)==domainList.size()){%>
							
						</div>
						
						<%}
							 }
							}%>
					
					<div class="shorurl_div">
						<span class="shorurl_span">????????????</span>
						<input type="hidden" value="<%=netUrlId %>" id="netUrlId" name="netUrlId"/>
						<input id="netUrl" name="netUrl" class="input_bd"
							style="width: 300px;" type="text" maxlength="20"
							value="<%=netUrl %>" readonly="readonly" />
						<a id="choose"   style="cursor: pointer;margin-left:14px;">??????</a>
						<a id="createUrl"   style="cursor: pointer;margin-left:24px;">???????????????</a>
					</div>
					<div class="shorurl_div">
						<span class="shorurl_span">???????????????</span>
						<input id="vaildays" name="vaildays" class="input_bd"
							style="width: 100px;" type="text" maxlength="2"
							value="<%=vaildays %>"  onkeyup="phoneInputCtrl($(this))"/>
						???
					</div>
					<div >
				<input name="insert" id="insertBtn"  class="btnClass5 shoturl_insert"  type="button" value="??????"/>
				<input name="cancel"  class="btnClass6 shoturl_cancel"  type="button" onclick="chooseCancel();" value="??????"/>
				</div>		
				</form>
			
			
			<div class="short_desc" style="margin-top: 20px;">
				<span>?????????</span><br/>
				<span>1????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</span><br/>

				<span>2??? ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</span><br/>
				
				<span>3??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????<a id="createUrl2"  style="cursor: pointer">????????????</a>???????????????</span><br/>
				
			</div>
			
			</div>
			<%-- ???????????? --%>
			<%-- foot?????? --%>
			
		</div>
		<%-- foot?????? --%>
		

		<div id="addDiv" style="display:none">
				<table style="height:100%;font-size: 12px;margin-left: 72px">
					<tr style="height: 45px;">
						<td align="right" style="padding-right: 15px;width: 25%;">
							??????????????????
						</td>
						<td style="width: 50%;">
							<input type="text" style="width: 266px;height: 23px;line-height: 23px;" class="input_bd" name="urlNameAdd" id="urlNameAdd" maxlength="50"/>
						</td>
						<td>
							<font color="red">*</font>
						</td>
					</tr>
					<tr style="height: 35px;">
						<td align="right" style="padding-right: 15px;">
							????????????
						</td>
						<td>
							<input type="text"  style="width: 266px;" class="input_bd" name="urlCodeAdd" id="urlCodeAdd"  maxlength="1024"/>
						</td>
						<td style="width: 200px;">
							<font color="red">*</font>
						</td>
					</tr>
					<tr style="height: 170px;" >
						<td align="right" style="padding-right: 15px;">
							????????????????????????
						</td>
						<td>
							<textarea style="height:150px;width: 266px; line-height: 18px;" class="input_bd" name="urlDescriptionAdd" id="urlDescriptionAdd" maxlength="500"></textarea>
						 	<div id="url_desc_cover" style="color:gray;position:relative;width:265px;height:0px;margin-left:5px;bottom:146px;" >????????????????????????????????????????????????????????????????????????????????????</div>
						 </td>
						<td>
							<font color="red">*</font>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align:center">
							<input name="addsubmit" id="addsubmit" class="btnClass5 mr23" type="button" value="??????" onclick="addUrl1();"/>
							<input name="addcancel" id="addcancel" class="btnClass6" type="button" value="??????" onclick="doCancel();"/>
							<%-- ????????????ie8??????????????????????????????????????????????????????????????????????????? --%>
							<br/>
						</td>
					</tr>
				</table>
			</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/frame/frame2.5/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		//js???????????????
           var path="<%=path%>";
		$(document).ready(function() {
            window.parent.document.getElementById("ui-dialog-title-shortUrlDiv").innerHTML="???????????????" ;
			doLen();
			$("#addDiv").dialog({
				autoOpen: false,
			    width:620,
			    height:400,
			    title:"???????????????",
			    modal:true,
			    resizable:false
			 });
			
			$("#addDiv").dialog({
				autoOpen: false,
			    width:620,
			    height:400,
			    title:"???????????????",
			    modal:true,
			    resizable:false
			 });
			
			$('#choose').click(function(){$("#pageForm").submit();});
			$('#createUrl,#createUrl2').click(function(){
				$("#addDiv").dialog("open");
			});
			
			
			$("#url_desc_cover").click(function(){
				var txt = $.trim($(this).val());
				if(txt == "") {
					$("#url_desc_cover").hide();
				}
			});
			
			
			
			$("#urlDescriptionAdd").focus(function(){
				var txt = $.trim($(this).val());
				if(txt == "") {
					$("#url_desc_cover").hide();
				}
			});
			
			$("#urlDescriptionAdd").blur(function(){
				var txt = $.trim($(this).val());
				if(txt == "") {
					$("#url_desc_cover").show();
				}
			});

			$("#insertBtn").click(function() {
                insertInfo();
            });
		});

		  function doLen(){
		  	var domainLength =  $("input[type='radio']:checked").next("span").text().length;
						$("#domainLength").text(domainLength);
		  }
          //??????
          function chooseCancel(){
          	parent.urlCancel();
          	
          }
          //??????
          function insertInfo(){
          	//??????Id
          	var domainId = $("input[type='radio']:checked").val();
          	if(domainId==null||domainId==""){
          		alert("??????????????????");
          		return;
          	}
          	var domain =  $("input[type='radio']:checked").next("span").text();
          	//?????????Id
          	var netUrlId = $("#netUrlId").val();
          	var netUrl =  $("#netUrl").val()
          	if(netUrlId==null || netUrlId==""||netUrl==null || netUrl==""){
          		alert("??????????????????");
          		return;
          	}
          	//????????????
          	var vaildays = $("#vaildays").val()
          	if(vaildays==null || vaildays==""){
          		alert("?????????????????????");
          		return;
          	}
          	//?????????????????????
          	var domainvDay =  $("#domainvDay"+domainId).val();
          	
          	// console.log(vaildays + " : " + domainvDay);
          	
          	if(parseInt(vaildays)>parseInt(domainvDay)){
          		alert("?????????????????????"+domainvDay+"???");
          		return;
          	}
          	$("#netUrl",window.parent.document).val(netUrl);
          	$("#netUrlId",window.parent.document).val(netUrlId);
          	$("#vaildays",window.parent.document).val(vaildays);
          	$("#domainId",window.parent.document).val(domainId);
          	parent.urlSure();
          	 //$("#shortUrlDiv",window.parent.document).dialog("close");
          }


		
		</script>
	<script type="text/javascript" src="<%=iPath %>/js/url_manage.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
