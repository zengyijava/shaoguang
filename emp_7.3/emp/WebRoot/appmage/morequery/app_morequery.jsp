<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("morecordselect");
menuCode = menuCode==null?"0-0-0":menuCode;

boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<DynaBean> list = null;
if(request.getAttribute("list") != null){
	list = (List<DynaBean>) request.getAttribute("list");
}
String startTime= StringUtils.defaultString(request.getParameter("starttime"));
String endTime= StringUtils.defaultString(request.getParameter("endtime"));
String fromName = StringUtils.defaultString(request.getParameter("fromname"));
String msgText = StringUtils.defaultString(request.getParameter("msgtext"));
String msgType = StringUtils.defaultString(request.getParameter("msgtype"));
//查看历史详情时 带有此参数
String fromUser = StringUtils.defaultString(request.getParameter("fromuser"));
Map<String,String> typeMap = new HashMap<String,String>();
typeMap.put("0",MessageUtils.extractMessage("appmage","appmage_xxfb_appxxfs_text_word",request));
typeMap.put("1",MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_image",request));
typeMap.put("2",MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_video",request));
typeMap.put("3",MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_audio",request));
String skip =(String)request.getAttribute("skip");
if("true".equals(skip)){
	LinkedHashMap<String, String> conditionMap=(LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	if(conditionMap.containsKey("fromname")){
		fromName=conditionMap.get("fromname");
	}
	if(conditionMap.containsKey("starttime")){
		startTime=conditionMap.get("starttime");
	}
	if(conditionMap.containsKey("msgtext")){
		msgText=conditionMap.get("msgtext");
	}
	if(conditionMap.containsKey("endtime")){
		endTime=conditionMap.get("endtime");
	}
	if(conditionMap.containsKey("msgtype")){
		msgType=conditionMap.get("msgtype");
	}
}
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/mobilePreview.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<style type="text/css">
	.emo{
		width:24px;
	}
	#bubbleContent .img{
		width:115px;
	}
	</style>
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
  </head>
  
  <body>
    <div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="app_morecordselect.htm" method="post" id="pageForm">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv"></div>
				<%if(fromUser.length()>0) {%>
				<span id="backgo" class="right mr5" onclick="javascript:showback()">&nbsp;<emp:message key="appmage_common_opt_fanhui" defVal="返回" fileName="appmage"></emp:message></span>
				<input type="hidden" name="fromuser" value="<%=fromUser %>"/>
				<%} %>
				<a id="exportCondition"><emp:message key="appmage_common_opt_daochu" defVal="导出" fileName="appmage"></emp:message></a>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td>
								<emp:message key="appmage_xxfb_appsxjl_text_appuser" defVal="APP用户" fileName="appmage"></emp:message>：
							</td>
							<td >
								<input style="width: 178px" type="text" value="<%=fromName %>" name="fromname" id="fromname"/>
							</td>
							<td>
								<emp:message key="appmage_xxfb_appsxjl_text_messagetype" defVal="消息类型" fileName="appmage"></emp:message>：
							</td>
							<td >	
								<select id="msgtype" name="msgtype" class="newSelect">
									<option value=""><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
									<option value="0" <%=msgType!=null&&"0".equals(msgType)?"selected":"" %>><emp:message key="appmage_xxfb_appxxfs_text_word" defVal="文字" fileName="appmage"></emp:message></option>
									<option value="1" <%=msgType!=null&&"1".equals(msgType)?"selected":"" %>><emp:message key="appmage_xxfb_appfsrw_text_image" defVal="图片" fileName="appmage"></emp:message></option>
									<option value="2" <%=msgType!=null&&"2".equals(msgType)?"selected":"" %>><emp:message key="appmage_xxfb_appfsrw_text_video" defVal="视频" fileName="appmage"></emp:message></option>
									<option value="3" <%=msgType!=null&&"3".equals(msgType)?"selected":"" %>><emp:message key="appmage_xxfb_appfsrw_text_audio" defVal="音频" fileName="appmage"></emp:message></option>
								</select>
							</td>
							<td><emp:message key="appmage_xxfb_appsxjl_text_mocontent" defVal="上行内容" fileName="appmage"></emp:message>：</td>
							<td>
								<input style="width: 178px" type="text" id="msgtext" name="msgtext" value="<%=msgText %>"/>
							</td>
							<td class="tdSer">
							       <center><a id="search"></a></center>
						    </td>		
						</tr>
						<tr>
							<td><emp:message key="appmage_xxfb_appsxjl_text_motime" defVal="上行时间" fileName="appmage"></emp:message>：</td>
							<td>
								<input type="text"
									style="cursor: pointer; width: 178px; background-color: white;"
									class="Wdate startdate" readonly="readonly"
									value="<%=startTime%>"  id="starttime"
									name="starttime" />
							</td>
							<td><emp:message key="appmage_common_text_zhi" defVal="至：" fileName="appmage"></emp:message></td>
							<td>
								<input type="text"
										style="cursor: pointer; width: 178px; background-color: white;"
										class="Wdate enddate" readonly="readonly"
										value="<%=endTime%>"  id="endtime"
										name="endtime" />
							</td>
							<td colspan="3"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<table id="content">
				<thead>
					<tr>
						<th width="20%;">
							<emp:message key="appmage_xxfb_appsxjl_text_appuser" defVal="APP用户" fileName="appmage"></emp:message>
						</th>
						<th width="30%;">
						    <emp:message key="appmage_xxfb_appsxjl_text_mocontent" defVal="上行内容" fileName="appmage"></emp:message>
						</th>
						<th width="8%;">
							<emp:message key="appmage_xxfb_appsxjl_text_messagetype" defVal="消息类型" fileName="appmage"></emp:message>
						</th>
						<th>
							<emp:message key="appmage_xxfb_appsxjl_text_motime" defVal="上行时间" fileName="appmage"></emp:message>
						</th>
						<th>
							<emp:message key="appmage_common_opt_caozuo" defVal="操作" fileName="appmage"></emp:message>
						</th>
					</tr>
				</thead>
				<tbody>
				<%
					if(list!=null && list.size()>0)
					{
						for(DynaBean item : list)
						{
							String fromname = item.get("fromname")==null?"-":String.valueOf(item.get("fromname"));
							String msgtype = item.get("msg_type")==null?"0":String.valueOf(item.get("msg_type"));
							String createtime =  item.get("createtime")==null?"-":String.valueOf(item.get("createtime")).substring(0,19);
							String msgid = item.get("msg_id")==null?"0":String.valueOf(item.get("msg_id"));
							String fromuser = item.get("fromuser")==null?"-":String.valueOf(item.get("fromuser"));
							String msgtext = item.get("msg_text")==null?" ":String.valueOf(item.get("msg_text"));
							//文字中是否可能含有系统表情字符 /开头
							boolean isEmo = msgtext.contains("/");
							
				%>
				<tr class="data_tr">
					<td><%=fromname%>(<%=fromuser %>)</td>
					<td><a href="javascript:prev(<%=msgid %>);"><%if("0".equals(msgtype)&&!isEmo){out.print(StringEscapeUtils.escapeHtml(StringUtils.abbreviate(msgtext,18)).replaceAll(" ", "&nbsp;"));}else{out.print(MessageUtils.extractMessage("appmage","appmage_common_opt_yulan",request));} %></a></td>
					<td><%=typeMap.get(msgtype) %></td>
					<td><%=createtime%></td>
					<td>
					<%if(fromUser.length()>0) {%>
						-
					<%}else{ %>
						<a href="javascript:history('<%=fromuser %>')"><emp:message key="appmage_xxfb_appsxjl_text_hisdetail" defVal="历史详情" fileName="appmage"></emp:message></a>
					<%} %>
					</td>
				</tr>
				<%
						}
					}else{
				%>
				<tr><td align="center" colspan="5"><%if(isFirstEnter){out.print(MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_chilkdata",request));}else{out.print(MessageUtils.extractMessage("appmage","appmage_common_text_nodata",request));} %></td></tr>
				<%} %>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="5">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
			</table>
		</form>
		</div>
		<div class="bottom">
			<div id="bottom_right">
				<div id="bottom_left"></div>
				<div id="bottom_main">
				</div>
			</div>
		</div>
	</div>
    <div class="clear"></div>
    <%--手机模拟预览 --%>
    <div id="previewBg"></div>
    <div id="mobilePreview">
    	<div id="appDrag" class="appDrag"></div>
		<div class="effectArea">
			<div class="appClose"></div>
				<div class="mobileModel">
					<div class="effectBox">
						<div class="effect_hd" id="subTitle"><emp:message key="appmage_xxfb_appsxjl_text_moyulan" defVal="上行消息预览" fileName="appmage"></emp:message></div>
						<div class="effect_bd">
							<div class="tabsContent">
								<div class="effect_dialog" data-tab="txt">
									<div class="avitor">
										<img src="<%=commonPath%>/common/img/mobilePreview/avitor.gif" alt="">
									</div>
									<div class="bubbleArea">
										<span class="triangle"></span>
										<span class="triangleContour"></span>
										<div class="bubbleContent" id="bubbleContent">
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
	</div>
<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath %>/common/widget/video/swfobject.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/wdate_extend.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/appDrap.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
	<script type="text/javascript">
		var path='<%=path%>';
		var videoPath="<%=commonPath%>/common/widget/video/";
		$(document).ready(function() {
			getLoginInfo("#loginInfo");
			//new appDrag({'wrapId':'mobilePreview'});
			showPreview();
			$('.appClose').click(function(){
				$('#bubbleContent').html('');
				$('#mobilePreview,#previewBg').hide();
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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){
				submitForm();
			});
			$("#msgtype option[value='<%=msgType%>']").attr('selected',true);
			$(".newSelect").isSearchSelect({'width':'180','isInput':false,'zindex':0});
		
			//导出全部数据到excel
			$("#exportCondition").click(
			 function(){
				  if(confirm(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportexcel')))  
				  {
					  var lgcorpcode =$("#lgcorpcode").val();
					  var lguserid =$("#lguserid").val();
					  var fromuser = '<%=fromUser != null ? fromUser : ""%>';
			  		  var fromname = '<%=fromName != null ? fromName : ""%>';
		   			  var msgtype = '<%=msgType != null ? msgType : ""%>';
		   			  var msgtext = '<%=msgText != null ? msgText : ""%>';
		   			  var starttime = '<%=startTime != null ? startTime : ""%>';
		   			  var endtime = '<%=endTime != null ? endTime : ""%>';
					   <%if (list!=null&&list.size()>0)  {
					   		if(pageInfo.getTotalRec()<=500000){
					   		%>
			                $.ajax({
								type:"POST",
								url: "<%=path%>/app_morecordselect.htm",
								data: {method: "exportData",
					   				lguserid:lguserid,
					   				lgcorpcode:lgcorpcode,
					   				fromuser:fromuser,
					   				fromname:fromname,
					   				msgtype:msgtype,
					   				msgtext:msgtext,
					   				starttime:starttime,
					   				endtime:endtime
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
										window.parent.complete();
										download_href("<%=path%>/app_morecordselect.htm?method=downloadFile");
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
		});
		$(window).resize(function(){
			showPreview();
		})
		function history(fromuser){
			var lgcorpcode = $("#lgcorpcode").val();
			if(!fromuser||!lgcorpcode){
				return;
			}
			window.location.href = "app_morecordselect.htm?method=find&fromuser="+fromuser+"&lgcorpcode="+lgcorpcode+"&pageIndex=1&skip=1";
		};
		function showback()
		{
		   var lgcorpcode =$("#lgcorpcode").val();
		   var lguserid =$("#lguserid").val();
		   location.href="app_morecordselect.htm?method=find&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&pageIndex=1&skip=true";
		}
		function prev(id){
			 var lgcorpcode =$("#lgcorpcode").val();
			$.ajax({
				type:"POST",
				url: "app_morecordselect.htm",
				data: {method: "prev",msgid:id,lgcorpcode:lgcorpcode,time:new Date(),isAsync:"yes"},
				beforeSend: function(){
					showPreview();
					$('#mobilePreview,#previewBg').show();
					$('#bubbleContent').html("<img class='img' src='<%=commonPath%>/common/img/mobilePreview/load.gif'/>");
					},
				success: function(result){
					var data = eval('('+result+')');
					if(data.errcode == -1){
						$('#bubbleContent').html('<font color="red">'+data.errmsg+'</font>');
					}else{
						var msg = data.msg;
						if(msg.msgtype == 0){//文字预览
							$('#bubbleContent').html(data.text);
						}else if(msg.msgtype == 1){//图片预览
							$('#bubbleContent').html('<img class="img" src="'+msg.pic+'"/>');
						}else if(msg.msgtype == 2){//视频
							$('#bubbleContent').html('');
							var videoParams={
								'route':videoPath,
								'width':115,
								'height':80,
								'fileUrl':msg.url,
								'wrap':'bubbleContent'
							};
							createVideoPreview(videoParams);
						}else if(msg.msgtype == 3){//音频
							var mp3Array=[],temp;
							var data = videoPath+'/mp3/dewplayer-mini.swf?mp3='+msg.url;
							mp3Array.push('<object style="width:20px" type="application/x-shockwave-flash" data="'+data+'" width="160" height="20" id="dewplayer-mini">');
							mp3Array.push('<param name="wmode" value="transparent" />');
							mp3Array.push('<param name="movie" value="'+data+'" />');
							mp3Array.push('</object>');
							temp=mp3Array.join('');
							$('#bubbleContent').html(temp);
						}else{
							$('#bubbleContent').html(escapelt(msg.content||''));
						}
					}
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
		
</script>
  </body>
</html>
