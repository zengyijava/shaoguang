<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.appmage.vo.MttaskDetailVo"%>
<%@page import="com.montnets.emp.entity.appmage.LfAppMwClient"%>
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
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	//详情结果集
	@ SuppressWarnings("unchecked")
	List<MttaskDetailVo> mtList =(List<MttaskDetailVo>) request.getAttribute("mtList");
	 //任务id
	 String taskid  = request.getParameter("taskid");
	 //任务id
	 String msgtype  = request.getParameter("msgtype");
	 //用户APP账号
	 String appacount =request.getParameter("appacount");
	 //用户昵称
	 String appacountname =request.getParameter("appacountname");
	 //发送状态
     String sendstate = request.getParameter("sendstate");
     //回执状态
     String rptstate = request.getParameter("rptstate");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");

	String menuCode = titleMap.get("mttaskselect");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/mobilePreview.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
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
				<div id="container">
					<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
					<div class="rContent">
						<%              		
							if(btnMap.get(menuCode+"-0")!=null)                       		
							{                        	
						%>
						<form name="pageForm" action="app_mttaskselect.htm?method=findAllSendInfo" method="post">
						<div class="buttons" style="padding-top: 10px;">
						  <div id="toggleDiv">								
						  </div> 
						  <a id="exportCondition" ><emp:message key="appmage_common_opt_daochu" defVal="导出" fileName="appmage"></emp:message></a>	
						  <span id="backgo" class="right mr5" onclick="javascript:showback()"><emp:message key="appmage_common_opt_fanhui" defVal="返回" fileName="appmage"></emp:message></span>			  					 			          
						</div>
						<div id="condition">
						 <input type="hidden" id="taskid" value="<%=taskid %>" name="taskid">
						 <input type="hidden" id="msgtype" value="<%=msgtype %>" name="msgtype">
						 
						 <table>
						    <tr>
						     <td>用户APP账号：</td>
						     <td>
						         <input type="text" style="width: 177px;" value='<%=appacount==null?"":appacount %>' id="appacount" name="appacount" />
						      </td>
						     <td>用户昵称：</td>
						      <td>
						         <input type="text" style="width: 177px;" value='<%=appacountname==null?"":appacountname %>' id="appacountname" name="appacountname" />
						      </td>
						      <td><emp:message key="appmage_xxfb_appfsjl_text_sendstatus" defVal="发送状态" fileName="appmage"></emp:message>：</td>
						      <td>
						          <select style="width:180px;"  id="sendstate" name="sendstate">
										<option value="" <%="".equals(sendstate)?"selected":"" %>><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
										<option value="1" <%="1".equals(sendstate)?"selected":"" %> ><emp:message key="appmage_xxfb_appxxfs_text_suc" defVal="成功" fileName="appmage"></emp:message></option>
										<option value="2" <%="2".equals(sendstate)?"selected":"" %>><emp:message key="appmage_xxfb_appxxfs_text_false" defVal="失败" fileName="appmage"></emp:message></option>
										<option value="3" <%="3".equals(sendstate)?"selected":"" %>><emp:message key="appmage_xxfb_appfsjl_text_noreturn" defVal="未返" fileName="appmage"></emp:message></option>
								  </select>
						       </td>
						       <td><emp:message key="appmage_xxfb_appfsjl_text_returnstatus" defVal="回执状态" fileName="appmage"></emp:message>：</td>
						      <td>
						          <select style="width:180px;"  id="rptstate" name="rptstate">
										<option value="" <%="".equals(rptstate)?"selected":"" %>><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
										 <option value="-1" <%="-1".equals(rptstate)?"selected":"" %>><emp:message key="appmage_xxfb_appfsjl_text_unknow" defVal="未知" fileName="appmage"></emp:message></option>
										 <option value="0" <%="0".equals(rptstate)?"selected":"" %>><emp:message key="appmage_xxfb_appfsjl_text_read" defVal="已读" fileName="appmage"></emp:message></option>
										<option value="1" <%="1".equals(rptstate)?"selected":"" %>><emp:message key="appmage_xxfb_appfsjl_text_resavefalse" defVal="接收失败" fileName="appmage"></emp:message></option>
								  </select>
						       </td>
						      <td class="tdSer">
								<center><a id="search"></a></center>
							  </td>
						    </tr> 
						 </table>
						</div>
						<table id="content">
							<thead>
								<tr>
									<th>
										用户APP账号
									</th>
									<th>
									          昵称
									</th>
									<th>
										<emp:message key="appmage_xxfb_appfsrw_text_subject" defVal="发送主题" fileName="appmage"></emp:message>
									</th>
									<th>
										内容
									</th>
									<th>
										<emp:message key="appmage_xxfb_appfsjl_text_sendstatus" defVal="发送状态" fileName="appmage"></emp:message>
									</th>
									<th>
										<emp:message key="appmage_xxfb_appfsrw_text_sendtime" defVal="发送时间" fileName="appmage"></emp:message>
									</th>
									<th>
										<emp:message key="appmage_xxfb_appfsjl_text_returnstatus" defVal="回执状态" fileName="appmage"></emp:message>
									</th>
									<th>
										<emp:message key="appmage_xxfb_appfsjl_text_returntime" defVal="回执时间" fileName="appmage"></emp:message>
									</th>
								</tr>
							</thead>
							<%
							if(mtList!= null && mtList.size() > 0)
								{
								for(int i=0;i<mtList.size();i++)
								{
									MttaskDetailVo vo = mtList.get(i);
							%>
							<tr>
								<td>
									<xmp><%=vo.getAppuseraccount()!=null?vo.getAppuseraccount():"--" %></xmp>
								</td>
								<td>
									<xmp><%=vo.getAppusername()!=null?vo.getAppusername():"--" %></xmp>
								</td>
								<td>
									<xmp><%=vo.getTitle()!=null?vo.getTitle():"" %></xmp>
								</td>
								
								<td>
								<%							
									if(!"0".equals(msgtype)){
										if(!"4".equals(msgtype)){
								%>
											<a href="javascript:prev(<%=taskid %>);"><emp:message key="appmage_common_opt_chakan" defVal="查看" fileName="appmage"></emp:message></a>
								<%
										}
									}else{
								%>
								 <a href="javascript:prev(<%=taskid %>);"><xmp><%=vo.getContent()==null||"".equals(vo.getContent())?"":vo.getContent().length()>5?vo.getContent().substring(0,5)+"...":vo.getContent() %></xmp></a>
									<%--<a onclick=javascript:modify(this)>
									  <label style="display:none"><xmp><%=vo.getContent()==null||"".equals(vo.getContent())?"":vo.getContent().replaceAll("<","&lt;").replaceAll(">","&gt;") %></xmp></label>
									  <xmp><%=vo.getContent()==null||"".equals(vo.getContent())?"":vo.getContent().length()>5?vo.getContent().replaceAll("<","&lt;").replaceAll(">","&gt;").substring(0,5)+"...":vo.getContent().replaceAll("<","&lt;").replaceAll(">","&gt;") %></xmp>
									</a>
								 --%>
								<%
								 	}
								%>								
								</td>
								<td>
									<%if(vo.getRptstate()!= null&&"0".equals(vo.getRptstate())){ %>
									         <label><emp:message key="appmage_xxfb_appfsrw_text_suc" defVal="成功" fileName="appmage"></emp:message></label>  
									<%}else if(vo.getRptstate()!=null&&"1".equals(vo.getRptstate())){%>
									         <label><emp:message key="appmage_xxfb_appfsrw_text_false" defVal="失败" fileName="appmage"></emp:message></label>
									<%}else{ %>
											 <label><emp:message key="appmage_xxfb_appfsjl_text_noreturn" defVal="未返" fileName="appmage"></emp:message></label>
									<%}%>
								</td>
								<td width="11%">
								<%=vo.getCreatetime()==null?"":df.format(vo.getCreatetime()) %><%--发送时间 --%>
								</td>
								<td>
									<%if(vo.getRptstate()!=null&&"0".equals(vo.getRptstate())){%>
									         <label><emp:message key="appmage_xxfb_appfsjl_text_read" defVal="已读" fileName="appmage"></emp:message></label>
									<%}else if(vo.getRptstate()!=null&&"1".equals(vo.getRptstate())){%>
									         <label><emp:message key="appmage_xxfb_appfsjl_text_resavefalse" defVal="接收失败" fileName="appmage"></emp:message></label> 
									<%}else { %>
									         <label><emp:message key="appmage_xxfb_appfsjl_text_unknow" defVal="未知" fileName="appmage"></emp:message></label>
									<% }%>
								</td>
								<td width="11%">
								<%=vo.getRecrpttime()==null?"":df.format(vo.getRecrpttime()) %><%--回执时间 --%>
								</td>
							</tr>
							<%
								}
								}
								else
								{
							%>
							<tr><td colspan="8" align="center"><emp:message key="appmage_common_text_nodata" defVal="无记录" fileName="appmage"></emp:message></td></tr>
							<%} %>
							<tfoot>
							<tr >
								<td colspan="8">
								<div id="pageInfo"></div>
								</td>
							</tr>
							</tfoot>
						</table>
						<div id="smssendparams" class="hidden"></div>
						</form>
						<div class="clear"></div>
						<%
								}
						%>

					
					<%-- 内容结束 --%>
			<div id="modify" title="<emp:message key="appmage_xxfb_appfsrw_text_smscontent" defVal="信息内容" fileName="appmage"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none">
				<table width="100%">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td style='word-break: break-all;'>
								<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>
							</td>
						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
										
					</div>
			<%--end rContent--%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
		
		  <%--手机模拟预览 --%>
    <div id="previewBg"></div>
    <div id="mobilePreview">
    	
    	<div id="appDrag" class="appDrag"></div>
		<div class="effectArea">
				<div class="appClose"></div>
				<div class="mobileModel">
					<div class="effectBox">
						<%--<div class="effect_hd" id="subTitle">发送主题</div>
						--%><div class="effect_bd">
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
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/video/swfobject.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
		
		<script type="text/javascript">
		$(document).ready(function() {
		    getLoginInfo("#smssendparams");
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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
		    $("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});	

			$("#appacount,#appacountname").live("keyup blur",function(){
					var value=$(this).val();
					if(value!=filterString(value)){
						$(this).val(filterString(value));
					}
			});
			//导出全部数据到excel
			$("#exportCondition").click(
			function()
			{
				//任务id
	   			var taskid=$.trim($("#taskid").val());
	   			//企业编码
	   			var lgcorpcode =$("#lgcorpcode").val();
	   			//操作员
				var lguserid =$("#lguserid").val();
				//内容类型
				var msgtype=$("#msgtype").val();
				//用户APP账号
				var appacount='<%=appacount!=null?appacount:""%>';
				//发送状态
				var sendstate='<%=sendstate!=null?sendstate:""%>';
				//回执状态
				var rptstate='<%=rptstate!=null?rptstate:""%>';

			 	if(confirm(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportexcel')))
			 	{
			   		<%
			   		if(mtList!=null && mtList.size()>0 ){
			   		  	if(pageInfo.getTotalRec()<=500000){
			   		%>	
							$.ajax({
								type:"POST",
								url: "<%=path%>/app_mttaskselect.htm",
								data: {method: "smsReportAllExcel",
									msgtype:msgtype,
				   					sendstate:sendstate,
				   					lguserid:lguserid,
				   					lgcorpcode:lgcorpcode,
				   					taskid:taskid,
				   					appacount:appacount,
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
										window.parent.complete();
										download_href("<%=path%>/app_mttaskselect.htm?method=downloadFile");
									}else
									{
										//alert("导出失败！");
										alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportfalse'));	
									}
								}
							});
				   		<%}else{
				   		%>				   		    
				   		     //alert("数据量超过导出的范围50万，请从数据库中导出！");
				   		     alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_datatoobig'));	
				   		<%}
			   		}else{
			   		%>
			   			//alert("无数据可导出！");
			   			alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_nodatatoexport'));
			   		<%
			   		}
			   		%>
			   	}				 
			});
			
			$('#modify').dialog({
						autoOpen: false,
						width:250,
					    height:200
				});
			
		});

		//关闭加载窗口的方法		
		function closelog(){
		    var lguserid =$("#lguserid").val();
            $.post("<%=path%>/app_mttaskselect.htm?method=checkResult",{lguserid:lguserid},function(result){
        	    if(result != null && result =="over" )
        	    {
        	          $("#probar").dialog("close");
		              window.clearInterval(dd);
        	    }
        	});	
		}
		//返回
		function showback()
		{
		   var lgcorpcode =$("#lgcorpcode").val();
		   var lguserid =$("#lguserid").val();
		   location.href="app_mttaskselect.htm?method=findAllAppMttask&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&skip=true";
		}
		
		function modify(t)
		{
			$('#modify').dialog({
				autoOpen: false,
				width:250,
				   height:200
			});
			$("#msgcont").children("xmp").empty();
			$("#msgcont").children("xmp").text($(t).children("label").children("xmp").html());
			$('#modify').dialog('open');
		}
		
		var videoPath="<%=commonPath%>/common/widget/video/";
		var path="<%=path %>";
			function prev(id){
				$.ajax({
					type:"POST",
					url: "app_mttaskselect.htm",
					data: {method: "prev",taskid:id},
					beforeSend: function(){
						showPreview();
						$('#mobilePreview,#previewBg').show();
						//$('#bubbleContent').html('');
						//var videoParams={
						//		'route':videoPath,
						//		'width':115,
						//		'height':80,
						//		'fileUrl':'',
						//		'wrap':'bubbleContent'
						//};
						//createVideoPreview(videoParams);
						$('#bubbleContent').html("<img class='img' src='<%=commonPath%>/common/img/mobilePreview/load.gif'/>");
					},
					success: function(result){
						var data = eval('('+result+')');
						if(data.errcode == -1){
							$('#bubbleContent').html('<font color="red">'+data.errmsg+'</font>');
						}else{
							if(data.msgtype == 0){
								//$('#subTitle').html(data.title);
								$('#bubbleContent').text(data.msgtext);
							}else if(data.msgtype == 1){
								
								//$('#subTitle').html(data.title);
								$('#bubbleContent').html('<img src="'+data.msgurl+'" width="100%"/>');
							}else if(data.msgtype == 2){
								
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
								
								var mp3Array=[],temp;
							    var datavar = videoPath+'/mp3/dewplayer-mini.swf?mp3='+data.msgurl;
								mp3Array.push('<object style="width:20px" type="application/x-shockwave-flash" data="'+datavar+'" width="160" height="20" id="dewplayer-mini">');
								mp3Array.push('<param name="wmode" value="transparent" />');
								mp3Array.push('<param name="movie" value="'+datavar+'" />');
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
						// alert('请求异常！');
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
