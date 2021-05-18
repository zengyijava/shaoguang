<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.appmage.vo.MttaskSelectVo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
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
	@ SuppressWarnings("unchecked")
	List<MttaskSelectVo> mtList =(List<MttaskSelectVo>) request.getAttribute("mtList");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mttaskselect");
	menuCode = menuCode==null?"0-0-0":menuCode;
	//操作员
	String userid = request.getParameter("userid");
	String userName = request.getParameter("userName");
	//内容类型 
	String msgtype = request.getParameter("msgtype");
	//发送主题
    String title = request.getParameter("title");
    //发送状态
    String sendstate=request.getParameter("sendstate");
	//开始时间
	String starttime = request.getParameter("sendtime");
	//结束时间
	String endtime = request.getParameter("recvtime");
	//当前操作员ID
	String currUserid = request.getParameter("lguserid");
	String skip=(String)request.getAttribute("skip");
	if("true".equals(skip)){
		MttaskSelectVo msVo=(MttaskSelectVo)request.getAttribute("msVo");
		userName=msVo.getUsername();
		msgtype=String.valueOf(msVo.getMsgtype()!=null?msVo.getMsgtype():"");
		title=msVo.getTitle();
		sendstate=String.valueOf(msVo.getSendstate()!=null?msVo.getSendstate():"");
		starttime=String.valueOf(msVo.getBigintimestr()!=null?msVo.getBigintimestr():"");
		endtime=String.valueOf(msVo.getEndtimestr()!=null?msVo.getEndtimestr():"");
		if("null".equals(starttime)){
			starttime=null;
		}
		if("null".equals(endtime)){
			endtime=null;
		}
	}
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	String findResult= (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><%=titleMap.get(menuCode)%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/css/mobilePreview.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<style type="text/css">
			body div#tooltip { position:absolute; z-index:1000; max-width:435px; _width:expression(this.scrollWidth > 435 ? "435px" : "auto");  width:auto; background:#A8CFF6; border:#FEFFD4 solid 1px; text-align:left; padding:6px;}
			body div#tooltip p { margin:0;padding:6; color:#FFFFFE;font:12px verdana,arial,sans-serif;}
			body div#tooltip p em { display:block;margin-top:3px; color:#f60;font-style:normal; font-weight:bold;}
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
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%-- 表示请求的名称 --%>
			<form name="pageForm" action="app_mttaskselect.htm" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						<a id="exportCondition"><emp:message key="appmage_common_opt_daochu" defVal="导出" fileName="appmage"></emp:message></a>
					</div>
					<div id="condition">
						<table>
						   
							<tbody>
							
								<tr>
								 <td>
										<emp:message key="appmage_xxfb_appfsrw_text_caozuoyuan" defVal="操作员" fileName="appmage"></emp:message>：
									</td>
									<td class="condi_f_l">											
											<div style="width:180px;">											
											 <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
											<input type="text" id="userName" class="treeInput" name="userName" value="<%=userName==null?MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_qingxuanze",request):userName%>" onclick="javascript:showMenu();" readonly 	style="width:138px;cursor: pointer;"/>&nbsp;
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
								  <td><emp:message key="appmage_xxfb_appfsrw_text_contenttype" defVal="内容类型" fileName="appmage"></emp:message>：</td>
								  <td>
									<label>
									<select name="msgtype" id="msgtype"  style="width: 162px">
												<option value="" <% if("".equals(msgtype)){ %> selected="selected" <%} %>><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
												<option value="0" <% if("0".equals(msgtype)){ %> selected="selected" <%} %>><emp:message key="appmage_xxfb_appxxfs_text_word" defVal="文字" fileName="appmage"></emp:message></option>
												<option value="1" <% if("1".equals(msgtype)){ %> selected="selected" <%} %>><emp:message key="appmage_xxfb_appfsrw_text_image" defVal="图片" fileName="appmage"></emp:message></option>
												<option value="2" <% if("2".equals(msgtype)){ %> selected="selected" <%} %>><emp:message key="appmage_xxfb_appfsrw_text_video" defVal="视频" fileName="appmage"></emp:message></option>
												<option value="3" <% if("3".equals(msgtype)){ %> selected="selected" <%} %>><emp:message key="appmage_xxfb_appfsrw_text_audio" defVal="音频" fileName="appmage"></emp:message></option>
												<%--<option value="4" <% if("4".equals(msgtype)){ %> selected="selected" <%} %>>事件推送</option>
											--%></select>
									</label>
								  </td>
									<td><emp:message key="appmage_xxfb_appfsrw_text_subject" defVal="发送主题" fileName="appmage"></emp:message>：</td>
								    <td><input type="text" id="title" name ="title" style="width: 158px" value="<%=title== null?"":title %>"></td>
								  	<td class="tdSer">
									    <center><a id="search"></a></center>
								  	</td>		
									</tr>
								<tr>
								 <td><emp:message key="appmage_xxfb_appfsrw_text_submitstatus" defVal="提交状态" fileName="appmage"></emp:message>：</td>
								  <td>
									<label>
									<select name="sendstate" id="sendstate"  style="width: 162px">
												<option value="" <% if("".equals(sendstate)){ %> selected="selected" <%} %>><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
												<option value="1" <% if("1".equals(sendstate)){ %> selected="selected" <%} %>><emp:message key="appmage_xxfb_appfsrw_text_suc" defVal="成功" fileName="appmage"></emp:message></option>
												<option value="2" <% if("2".equals(sendstate)){ %> selected="selected" <%} %>><emp:message key="appmage_xxfb_appfsrw_text_false" defVal="失败" fileName="appmage"></emp:message></option>
											</select>
									</label>
								  </td>
								   <td>
								        <emp:message key="appmage_xxfb_appfsrw_text_sendtime" defVal="发送时间" fileName="appmage"></emp:message>：
								   </td>
								   <td>
								    	<input type="text"
											style="cursor: pointer; width: 158px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()" 
											value="<%= starttime== null?"":starttime %>"  id="sendtime" name="sendtime" >
								   </td>
								   <td><emp:message key="appmage_common_text_zhi" defVal="至：" fileName="appmage"></emp:message></td>
								   <td>
								      	<input type="text"
											style="cursor: pointer; width: 158px; background-color: white;"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=endtime==null?"":endtime %>"  id="recvtime" name="recvtime" >
								    </td>
								      <td>&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr align="center">
								<th>
									<emp:message key="appmage_xxfb_appfsrw_text_sendtime" defVal="发送时间" fileName="appmage"></emp:message>
								</th>
								<th>
									<emp:message key="appmage_xxfb_appfsrw_text_subject" defVal="发送主题" fileName="appmage"></emp:message>
								</th>
								<th>
								     <emp:message key="appmage_xxfb_appfsrw_text_content" defVal="发送内容" fileName="appmage"></emp:message>
								</th>
								<th>
								     <emp:message key="appmage_xxfb_appfsrw_text_contenttype" defVal="内容类型" fileName="appmage"></emp:message>
								</th>
								<th>
								     <emp:message key="appmage_xxfb_appfsrw_text_status" defVal="状态" fileName="appmage"></emp:message>
								</th>
								<th>
								     <emp:message key="appmage_xxfb_appfsrw_text_totalnum" defVal="发送总数" fileName="appmage"></emp:message>
								</th>
								<th>
								    <emp:message key="appmage_xxfb_appfsrw_text_sucnum" defVal="发送成功数" fileName="appmage"></emp:message>
								</th>
								<th>
								    <emp:message key="appmage_xxfb_appfsrw_text_falsenum" defVal="发送失败数" fileName="appmage"></emp:message>  
								</th>
								<th>
								   <emp:message key="appmage_xxfb_appfsrw_text_readnum" defVal="已读用户数" fileName="appmage"></emp:message>   
								</th>
								<th>
								   <emp:message key="appmage_xxfb_appfsrw_text_unreadnum" defVal="未读用户数" fileName="appmage"></emp:message> 
								</th>
								<th>
									<emp:message key="appmage_xxfb_appfsrw_text_caozuoyuan" defVal="操作员" fileName="appmage"></emp:message> 
								</th>
								<th>
								    <emp:message key="appmage_common_opt_caozuo" defVal="操作" fileName="appmage"></emp:message> 
								</th>
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="18" align="center"><emp:message key="appmage_xxfb_appfsrw_text_chilkdata" defVal="请点击查询获取数据" fileName="appmage"></emp:message></td></tr>
							<%
							} else if(mtList != null && mtList.size()>0)
							{
								for(MttaskSelectVo mt : mtList)
								{
						%>
						<tr align="center">
							<td width="120px;">
								<%=mt.getBigintime()==null?"":df.format(mt.getBigintime()) %><%--发送时间 --%>
							</td>
							<td>
								<%=mt.getTitle()==null?"":(mt.getTitle().replaceAll("<","&lt;").replaceAll(">","&gt;")) %>
							</td>
							<td>
							<%
							String msgtypen = mt.getMsgtype()!=null?mt.getMsgtype().toString():"";
							if(!msgtypen.equals("0")){
								if(!msgtypen.equals("4")){
								%>
									<a href="javascript:prev(<%=mt.getTaskid() %>);"><emp:message key="appmage_common_opt_chakan" defVal="查看" fileName="appmage"></emp:message></a>
								<%
								}
							}else{ 
							%>
							<a href="javascript:prev(<%=mt.getTaskid() %>);"> <xmp><%=StringUtils.abbreviate(mt.getMsg(),20) %></xmp></a>
							<%--<a onclick=javascript:modify(this)>
								  <label style="display:none"><xmp><%=mt.getMsg()%></xmp></label>
								  <xmp><%=mt.getMsg().length()>5?mt.getMsg().substring(0,5)+"...":mt.getMsg() %></xmp>
								</a> 
							--%>
							<%} %>
							</td>
							<td width="50px;">
								<%
								    
								    if(msgtypen.equals("0")){
								       msgtypen = MessageUtils.extractMessage("appmage","appmage_xxfb_appxxfs_text_word",request); //"文字";
								    }else if(msgtypen.equals("1")){
								       msgtypen = MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_image",request); //"图片";
								    }else if(msgtypen.equals("2")){
								       msgtypen = MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_video",request); //"视频";
								    }else if(msgtypen.equals("3")){
								       msgtypen = MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_audio",request); //"音频";
								    }else if(msgtypen.equals("4")){
								       msgtypen = MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_tuisong",request); //"事件推送";
								    }else{
								       msgtypen = "--";
								    }
								 %>
								 <%=msgtypen %>
							</td>
							<td class="ztalign" width="30px;">
						 	<%
								    String sendState = mt.getSendstate().toString();
								   // if(sendState.equals("0")){
								   //   sendState = "新消息（未发送）";
								   //}else 
								    if(sendState.equals("1")){
								       sendState = MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_suc",request); //"成功";
								    }else if(sendState.equals("2")){
								       sendState = MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_false",request); //"失败";
								    //}else if(sendState.equals("4")){
								    //   sendState = "发送中";
								    //}else if(sendState.equals("5")){
								    //	sendState = "超时未发送";
								    //}else if(sendState.equals("3")){
								    //	sendState = "APP网关处理完成";
								    }else{
								       sendState = "--";
								    }
								 %>
								 <%=sendState %>
							 </td>
							 <td>
								 <%=mt.getSubcount()!=null?mt.getSubcount():"0" %>
							 </td>
							 <td>
								 <%=mt.getSuccount()!=null?mt.getSuccount():"0" %>
							 </td>
							 <td>
								  <%=mt.getFaicount()!=null?mt.getFaicount():"0" %>
							 </td>
							 <td>
								  <%=mt.getReadcount()!=null?mt.getReadcount():"0" %>
							 </td>
							 <td>
								  <%=mt.getUnreadcount()!=null?mt.getUnreadcount():"0" %>
							 </td>
							<td width="60px;">
								<%=mt.getUsername() %><%if(mt.getUserstate()==2){out.print("<font color='red'>(" + MessageUtils.extractMessage("appmage","appmage_xxfb_appfsrw_text_becanceled",request) + ")</font>");} %>
							</td>
							<td width="50px;">
							  <%if(mt.getSendstate().toString().equals("0")){ %>
							     -
							  <%} else{%>
							  	<a onclick="javascript:location.href='app_mttaskselect.htm?method=findAllSendInfo&taskid=<%=mt.getTaskid() %>&msgtype=<%=mt.getMsgtype()!=null?mt.getMsgtype().toString():"" %>&lgcorpcode=<%=request.getParameter("lgcorpcode") %>'"><emp:message key="appmage_xxfb_appfsrw_text_xqck" defVal="详情查看" fileName="appmage"></emp:message></a>
							  <%} %>
							</td>			
						</tr>
						<%
								}
							}else{
						%>
						<tr><td align="center" colspan="12"><emp:message key="appmage_common_text_nodata" defVal="无记录" fileName="appmage"></emp:message></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="12">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="smssendparams" class="hidden"></div>
				</form>
			</div>
			<div id="detailDialog" style="padding: 0px; display: none; width: 550px;">
			</div>
			<%-- 内容结束 --%>
			<div id="modify" title="<td align="center" colspan="12"><emp:message key="appmage_xxfb_appfsrw_text_smscontent" defVal="信息内容" fileName="appmage"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none">
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
			<%-- foot开始 --%>
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
						<%--<div class="effect_hd" id="subTitle"></div>
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath%>/common/js/common.js" type="text/javascript?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/video/swfobject.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/app_mttaskselect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
			$(document).ready(function() {
			    //参数是要隐藏的下拉框的div的id数组，
			   closeTreeFun(["dropMenu"]);
			   showPreview();
				$('.appClose').click(function(){
					$('#bubbleContent').html('');
					$('#mobilePreview,#previewBg').hide();
				})
				var findresult="<%=findResult%>";
			    if(findresult=="-1")
			    {
			       //alert("加载页面失败,请检查网络是否正常!");
			       alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_loadpagefalse'));	
			       return;			       
			    }
				$("#toggleDiv").toggle(function() {
					$("#condition").hide();
					$(this).addClass("collapse");
				}, function() {
					$("#condition").show();
					$(this).removeClass("collapse");
				});
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				
				$("#divBox").dialog({
					autoOpen: false,
					height:520,
					width: 300,
					modal: true,
					close:function(){
					}
				});
				
				$(".ym").change(function(){
		            var id=$(this).val();
		            for(var i=0;i<listpage.length;i++){
		                  if(id==listpage[i].id){
		                      $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
		                  }
		            }
				});
				
				$("#title").live("keyup blur",function(){
					var value=$(this).val();
					if(value!=filterString(value)){
						$(this).val(filterString(value));
					}
				});
				
				$('#search').click(function(){submitForm();});
			    $("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
				}, function() {
					$(this).removeClass("hoverColor");
				});		

				$("#detailDialog").dialog({
					autoOpen: false,
					height:450,
					width: 650,
					modal: true,
					resizable:false,
					close:function(){
					}
				});
				
			//导出全部数据到excel
			$("#exportCondition").click(
				function()
				 {
					  if(confirm(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportexcel')))
					   {
							//内容类型 
							var msgtype='<%=msgtype!=null?msgtype:"" %>';
							//发送状态
							var sendstate ='<%=sendstate!=null?sendstate:"" %>'
					   	   	var lgcorpcode =$("#lgcorpcode").val();
							var lguserid =$("#lguserid").val(); 
					   		<%
					   		if(mtList!=null && mtList.size()>0){
					   		  if(pageInfo.getTotalRec()<=500000){
					   		%>	
				                $.ajax({
									type:"POST",
									url: "<%=path%>/app_mttaskselect.htm",
									data: {method: "ReportCurrPageExcel",
										msgtype:msgtype,
					   					sendstate:sendstate,
					   					lguserid:lguserid,
					   					lgcorpcode:lgcorpcode},
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
											// alert("导出失败！");
											alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportfalse'));	
										}
									}
								});
					   		<%	
					   		}else{%>
					   		    //alert("数据量超过导出的范围50万，请从数据库中导出！");
					   		     alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_datatoobig'));	
					   		<%}
					   		}else{
					   		%>
					   		    //alert("无数据可导出！");
					   		     alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_nodatatoexport'));	
					   		<%
					   		}%>
					   }				 
			  });
			
				$('#modify').dialog({
						autoOpen: false,
						width:250,
					    height:200
				});
				
				
			});
			
			$(window).resize(function(){
			showPreview();
			})
			
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
