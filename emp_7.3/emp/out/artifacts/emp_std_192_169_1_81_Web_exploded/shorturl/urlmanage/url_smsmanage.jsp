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
<%@page import="com.montnets.emp.shorturl.surlmanage.vo.LfNeturlVo"%>
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
	
	
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String menuCode = titleMap.get("urlSendBatchSMS");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	
	@SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String,String>)request.getAttribute("conditionMap");
	String urlname, srcurl, urlstate,ispass,creatuser,startTime,recvtime,lguserid;
	urlname = conditionMap.get("urlname")==null?"":conditionMap.get("urlname");
	srcurl = conditionMap.get("srcurl")==null?"":conditionMap.get("srcurl");
	urlstate = conditionMap.get("urlstate")==null?"":conditionMap.get("urlstate");
	ispass = conditionMap.get("ispass")==null?"":conditionMap.get("ispass");
	creatuser = conditionMap.get("creatuser")==null?"":conditionMap.get("creatuser");
	startTime = conditionMap.get("startTime")==null?"":conditionMap.get("startTime");
	recvtime = conditionMap.get("recvtime")==null?"":conditionMap.get("recvtime");
	lguserid = conditionMap.get("lguserid")==null?"":conditionMap.get("lguserid");
	
	String deptid = conditionMap.get("deptid")!=null?conditionMap.get("deptid"):null;
	String depNam = conditionMap.get("depNam")!=null?conditionMap.get("depNam"):null;
	
	//是否包含子机构
	String isContainsSun=(String)request.getAttribute("isContainsSun");
	if(isContainsSun==null||"".equals(isContainsSun)){
		isContainsSun="1";
	}
	
	@SuppressWarnings("unchecked")
	List<LfNeturlVo> neturlVos = (List<LfNeturlVo>) request.getAttribute("urlList");
	
	String netUrl = (String)request.getAttribute("netUrl");
	String netUrlId = (String)request.getAttribute("netUrlId");
	String domainId = (String)request.getAttribute("domainId");
	String vaildays = (String)request.getAttribute("vaildays");
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
	//@SuppressWarnings("unchecked")
	//Map<String, String> usersMap  = (Map<String, String>) request.getAttribute("usersMap");
	
	
	
    String findResult= (String)request.getAttribute("findresult");
    CommonVariables  CV = new CommonVariables();
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
	</head>
	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>--%>
			
			<%-- 内容开始 --%>
			
			<div id="rContent" class="rContent">
				<input type="hidden" value="<%=inheritPath %>" id="inheritPath"/>
				<form name="pageForm" action="urlcommonSMS.htm?method=getNetUrl" method="post"
					id="pageForm">
					<input type="hidden" id="vaildays" name="vaildays" value="<%=vaildays %>">
					<input type="hidden" id="lguserid" name="lguserid" value="<%=lguserid %>">
					<input type="hidden" id="domainId" name="domainId" value="<%=domainId %>">
					<div id="r_sysMoRparams" class="hidden"></div>
					
					<div id="condition">
						<table>
							<tbody>
								<tr>
									
									<td>
										链接名称：
									</td>
									<td>
										<input type="text" style="width: 181px;" value="<%=urlname==null?"":urlname %>" id="urlname"
											name="urlname" />
									</td>
									<td>
										链接地址：
									</td>
									<td>
										<input type="text" style="width: 181px;" value="<%=srcurl==null?"":srcurl %>" id="srcurl"
											name="srcurl" />
									</td>
									
									<td class="tdSer">
									             <center><a id="search"></a></center>
								    </td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
							  <th></th>
							  <th>链接名称</th>
							  <th>链接地址</th>
							  <th>链接内容描述</th>
							  <th>创建时间</th>
							</tr>
						</thead>
						<tbody>
							<%if(neturlVos != null && neturlVos.size()>0){
								for(LfNeturlVo lfVo :neturlVos){
								%>
								<tr>
									<td>
										<input type="radio" name="netUrlId" id="Id" value="<%=lfVo.getId() %>">
										<xmp style="display:none" class="pre-wrap"><%=lfVo.getSrcurl() %></xmp>
									</td>
									<td>
										<label><%=lfVo.getUrlname().length()>25?lfVo.getUrlname().substring(0,25)+"...":lfVo.getUrlname() %></label>
									</td>
									<td>
										<label><a href="javascript:showUrl(<%=lfVo.getId()%>);"><%=lfVo.getSrcurl().length()>25?lfVo.getSrcurl().substring(0,25)+"...":lfVo.getSrcurl() %></a></label>
										<input type="hidden" name="toUrl<%=lfVo.getId() %>" id="toUrl<%=lfVo.getId() %>" value="<%=lfVo.getSrcurl() %>"/>
									</td>
									<td>
										<a title="<%= lfVo.getUrlmsg() %>" >
											<%
											String xmessage = lfVo.getUrlmsg()  == null?"" : lfVo.getUrlmsg();
											%>
								  			<label style="display:none"><xmp><%=xmessage %></xmp></label>
											<xmp><%=xmessage.length()>5?xmessage.substring(0,5)+"...":xmessage %></xmp>
										</a> 
									</td>
									
									<td>
										<label><%=df.format(lfVo.getCreatetm()) %></label>
									</td>
									
									
											
								</tr>
								<%
								}
							}else {
								%>
								<tr><td colspan="10" align="center">无记录</td></tr>
								<%	
							}
								%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="10">
								    <input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>					
				</form>
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
			<div style="text-align: center">
				<input name="confirm" class="btnClass5 mr23"  type="button" onclick="chooseConfirm();" value="确认"/>
				<input name="cancel"  class="btnClass6" type="button" onclick="chooseCancel();" value="取消"/>
			</div>
		</div>
		<%-- foot结束 --%>
		<div id="singledetail" style="padding:5px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
		</div>
		
		<div id="showUrl" style="display:none;" >
			<iframe  id ="kkuu" name="kkuu" style="padding:5px;height: 337px;overflow: auto;" src="" ></iframe>
		</div>
		  <div id="modify" title="长链接内容描述"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
				<table width="240px">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td>
								<span style="display:block;width:240px;"><label id="msg2" style="width:100%;height:100%;"></label></span>
								 
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/frame/frame2.5/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript">
		
		

		
		$(document).ready(function() {

            //window.document.getElementById("ui-dialog-title-shortUrlDiv").innerText="test";
            window.parent.document.getElementById("ui-dialog-title-shortUrlDiv").innerHTML="选择长链接" ;
            //var res = $("#ui-dialog-title-shortUrlDiv",window.opener.document).val();
            //console.log(res);
			
			$('#condition input[type="text"]').unbind('keyup blur');
			//要改
			//noquot($("#busNameEdit"));
			//noquot($("#busNameAdd"));

			
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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
			
			
			$("#singledetail").dialog({
				autoOpen: false,
				maxWidth: 350,
				maxHeight: 170,
				modal: true
			});

			
			$("#showUrl").dialog({
				autoOpen: false,
				width:314,
			    height:398,
			    title:"链接预览",
				modal: true
			});


		
		
		});

		function modify2(t)
		{
			$('#modify').dialog({
			autoOpen: false,
			width:250,
		    height:200
		});
		$("#msg2").empty();
		$("#msg2").text($(t).children("label").children("xmp").text());
		$('#modify').dialog('open');
		}
		function showMenu() {
			//hideMenu2();
			var sortSel = $("#depNam");
			var sortOffset = $("#depNam").offset();
			$("#dropMenu").toggle();
		}
		function chooseConfirm(){
		    if($(":checked").length === 0){
		        alert("请"+window.parent.document.getElementById("ui-dialog-title-shortUrlDiv").innerHTML);
		        return
			}
			$("form[name='pageForm']").attr("action", "urlcommonSMS.htm?method=getDomain");
			$("#pageForm").submit();
		}
		function chooseCancel(){
			//parent.contents().find("#shortUrlDiv").dialog("close");
			//$("#shortUrlDiv",window.parent.document).css("display","none");
            window.parent.document.getElementById("ui-dialog-title-shortUrlDiv").innerHTML="选择短链接" ;
			parent.urlCancel();
            parent.insertUrl();
		}
		</script>
	<script type="text/javascript" src="<%=iPath %>/js/url_manage.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
