<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.netnews.entity.LfWXDataType"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<LfWXDataType> dataTypes = (List<LfWXDataType>) request.getAttribute("dataTypes");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	String langName = (String)session.getAttribute("emp_lang");
	
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/trustData.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
	</head>
	<body id="ydwx_dataType">
		<div id="container" class="container">
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<form name="pageForm" action="<%=path%>/wx_trustdata.htm?method=getDataTypes" method="post" id="pageForm">
			<input type="hidden" name="lgcorpcode" id="lgcorpcode" value="<%=lgcorpcode %>"/>
			<div id="loginUser" class="hidden"></div>
			<div id="rContent" class="rContent ydwx_rContent">
					<div id="condition">
						<emp:message key="ydwx_wxgl_hdxgl_lbmcs" defVal="类别名称：" fileName="ydwx"></emp:message>
						<input type="text" name="name" id="name" class="ydwx_name" maxlength="20">
						<a class="ydwx_addType" onclick="addType();"><emp:message key="ydwx_common_btn_tianjia" defVal="添加" fileName="ydwx"></emp:message></a>
					</div>
					<div class="ydwx_content_dv">
					<table id="content" class="ydwx_content">
						<thead>
					  <tr>
							<th>
								<emp:message key="ydwx_wxgl_hdxgl_lbmc" defVal="类别名称：" fileName="ydwx"></emp:message>
							</th>
							<th >
							    <emp:message key="ydwx_common_caozuo" defVal="操作" fileName="ydwx"></emp:message>
							</th>
							</tr>
						</thead>
								<tbody>
								<%
								if (dataTypes != null && dataTypes.size() > 0)
								{
									for (LfWXDataType dataType : dataTypes)
									{
								%>
									<tr>
										<td width="255px" id="dt<%=dataType.getId() %>" title="<%=dataType.getName() %>">
											<label><xmp><%=dataType.getName().length() > 10 ? dataType.getName().substring(0,10) + ".....":dataType.getName() %></xmp></label>
											<input type="text" value="<%=dataType.getName() %>" style="width:150px; display: none;" maxlength="20" >

										</td>
										<td >
										<a style="cursor: hand;" onclick="javascript:rename(this);"><emp:message key="ydwx_common_btn_chongmingming" defVal="重命名" fileName="ydwx"></emp:message></a>
										<a style="cursor: hand;margin-left: 10px;" onclick="javascript:del('<%=dataType.getId() %>');"><emp:message key="ydwx_common_btn_shanqu" defVal="删除" fileName="ydwx"></emp:message></a>
									
										<a style="cursor: hand;display: none" onclick="javascript:save(this,'<%=dataType.getId() %>','<%=dataType.getName() %>');"><emp:message key="ydwx_common_btn_queren" defVal="确认" fileName="ydwx"></emp:message></a>
										<a style="cursor: hand;display: none;margin-left: 18px;" onclick="javascript:cancelEdit(this,'<%=dataType.getName() %>');"><emp:message key="ydwx_common_btn_quxiao" defVal="取消" fileName="ydwx"></emp:message></a>
										<br/>
										</td>
									</tr>
								<%
									}
								}else{
									%>
									<tr><td colspan="3"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
								<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="3">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					
			</div>
			</form>
			<%-- foot结束 --%>
		</div>
	<div class="clear"></div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
				}, function() {
				$(this).removeClass("hoverColor");
			});
		});
		
		getLoginInfo("#loginUser");
		showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[5]);
		
		function addType(){
			var name = $.trim($("#name").val());
			if(name == ""){
				alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_12"));
				 $("#name").focus();
				return ;
			}
		if(!reg_exp(name)){
			alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_13"));
			return;
		}
			var lgcorpcode = $("#lgcorpcode").val();
			$.post("<%=path%>/wx_trustdata.htm?method=saveDataType",{name:name,lgcorpcode:lgcorpcode},function(result){
				if(result == "exist"){
					alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_14"));
				}else if(result == "true"){
					alert(getJsLocaleMessage("ydwx","ydwx_common_tjchg"));
					$("#name").val("");
					submitForm();
				}else if(result == "false"){
					alert(getJsLocaleMessage("ydwx","ydwx_common_tjshb"));
				}else{
					alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_15"));
				}
			});
		}
		
			//特殊字符串验证。过滤
			function reg_exp(s) { 				
				var pattern = "`~!#$^&*={};',<>?~！#￥……&*\\——|‘；：:”“'\"%～。，、？";
				for (var i = 0; i < pattern.length; i++) {
					if(s.indexOf(pattern.charAt(i)) > -1){
						return false;
					}
				} 
				return true; 
			}
		function rename(obj){
			$(obj).parent().prev().find("label").css("display","none");
			$(obj).parent().prev().find("input").css("display","");
			
			
			$(obj).next().next().css("display","");
			$(obj).next().next().next().css("display","");
			$(obj).next().css("display","none");
			$(obj).css("display","none");
		}
		
		function cancelEdit(obj,name){
			$(obj).parent().prev().find("input").val(name);
			$(obj).parent().prev().find("label").css("display","");
			$(obj).parent().prev().find("input").css("display","none");
			$(obj).prev().prev().css("display","");
			$(obj).prev().prev().prev().css("display","");
			$(obj).prev().css("display","none");
			$(obj).css("display","none");
		}
		
		function del(id){
			if(confirm(getJsLocaleMessage("ydwx","ydwx_common_qdshch"))){
				$.post("<%=path%>/wx_trustdata.htm?method=delDataType",{id:id},function(result){
					if(result == "true"){
						alert(getJsLocaleMessage("ydwx","ydwx_common_shchchg"));
						submitForm();
					}else if(result == "forbidden"){
						alert(getJsLocaleMessage("ydwx","ydwx_common_yjshybyxshch"));
					}else if(result == "false"){
						alert(getJsLocaleMessage("ydwx","ydwx_common_shchshbqjchwl"));
					}
				});
			}
		}
		
		function show(obj,name){
			$(obj).parent().prev().find("xmp").text(name);
			$(obj).parent().prev().find("label").css("display","");
			$(obj).parent().prev().find("input").css("display","none");
			$(obj).prev().prev().css("display","");
			$(obj).prev().css("display","");
			$(obj).next().css("display","none");
			$(obj).css("display","none");
		}
		
		function save(obj,id,oldName){
			var typeId = id;
			var newName = $(obj).parent().prev().find("input").val();
			var subnewName = newName.length > 10 ? newName.substring(0,10) + "....." :newName;
				if(/(\<{1,})|(\>{1,})|(\'{1,})|(\"{1,})/.test(subnewName)){
					alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_13"));
	   	 			return ;
					}
			if(newName == ""){
				alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_12"));
				$("#newName").focus();
			}else if(oldName == newName){
				alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_17"));
				show(obj,subnewName);
			}else{
				$.post("<%=path%>/wx_trustdata.htm?method=saveDataType",{typeId:typeId,name:newName},function(result){
					if(result == "exist"){
						alert(getJsLocaleMessage("ydwx","ydwx_hdxgl_14"));
					}else if(result == "true"){
						alert(getJsLocaleMessage("ydwx","ydwx_common_xgchg"));
						show(obj,subnewName);
						$("#dt" + id).attr("title",newName);
					}else{
						alert(getJsLocaleMessage("ydwx","ydwx_common_xgshb"));
					}
				});
			}
		}
	</script>
  </body>
</html>
