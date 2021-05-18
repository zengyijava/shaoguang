<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%
    String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	Object appArrayList = request.getAttribute("listGroup");
	Object notAtGroup=request.getAttribute("notAtGroup");
		
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title></title>
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	<link href="<%=skin %>/role.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
  </head>
  <body style="margin:0;padding:0;">
  	<input type="hidden" id="pathUrl" value="<%=path %>" />
  	<input type="hidden" id="lgcorpcode" value="<%=request.getParameter("lgcorpcode")%>" />
  					<div class="rContent" id="rContent">
				
           			<div id="roleSetup">
               			<div id="r_s_sider">
  			<div id="list" class="list" style="padding:5px;width:180px;height:auto;">
			<p onclick="javascript:setValue('-1','-1');" title="<emp:message key="appmage_jcsj_yhgl_text_usernogroup" defVal="未分组用户" fileName="appmage"></emp:message>"><emp:message key="appmage_jcsj_yhgl_text_usernogroup" defVal="未分组用户" fileName="appmage"></emp:message>(<%=notAtGroup%><emp:message key="appmage_jcsj_yhgl_text_peploe" defVal="人" fileName="appmage"></emp:message>)</p>
				<% 
				String requestId=(String)request.getAttribute("groupid")==null?"":request.getAttribute("groupid")+"";
					if(appArrayList!=null){
					List<DynaBean> list=(List<DynaBean>)appArrayList;
					for(int i=0;i<list.size();i++){
					String gp_name=list.get(i).get("name")+"";
					String gp_id=list.get(i).get("g_id")+"";
					String num="0";

					String gpid=gp_id+"";
					if(list.get(i).get("countper")!=null){
					num=list.get(i).get("countper")+"";

					}			
				%>
				  <p  onclick="javascript:setValue('<%=gp_id %>','<%=gp_name%>');" title="<%=gp_name%>"><%=
					  gp_name.length()>10?gp_name.substring(0,10)+"...":gp_name 
					     %>(<%=num%><emp:message key="appmage_jcsj_yhgl_text_peploe" defVal="人" fileName="appmage"></emp:message>)</p>
			 <%		} 
			 	}
			 %>
			 	
			 </div>
			  </div>
			   </div>
			    </div>
			   	<div id="addDep" style="padding:5px;display:none;">
 			 		<input type="hidden" id="sdepcode" value="" />
				  <table  style="margin:15px auto 0;">
				  <tr><td align="right"><emp:message key="appmage_jcsj_yhgl_text_groupname" defVal="群组名称" fileName="appmage"></emp:message>：</td><td><input type="text" id="depName" maxlength="15" value="" /></td></tr>
				  <tr><td height="20px;"></td></tr>
				  
				  <tr><td colspan="2" align="center"><input type="button" value="<emp:message key="appmage_common_opt_queding" defVal="确定" fileName="appmage"></emp:message>" id="depsub" onfocus="this.blur();" class="btnClass5 mr23"  onclick="javascript:doOk();"/>
				  <input type="button" value="<emp:message key="appmage_common_opt_quxiao" defVal="取消" fileName="appmage"></emp:message>"  class="btnClass6" id="depcancel" onclick="javascript:doNo();"/></td></tr>
				  </table>
			 </div>
			 	<div id="updateDep" style="padding:5px;display:none;">
 			 	
				  <table>
				  <tr><td height="5px;"></td></tr>
				  <tr>	<td width="90px" align="right"><emp:message key="appmage_jcsj_yhgl_text_groupname" defVal="群组名称" fileName="appmage"></emp:message>：</td>
				  		<td>
				  			<input type="hidden" id="updateCode" value="" />
				  			<input type="hidden" id="depOldName" value="" />
				  			<input type="hidden" id="groupid" value="<%=request.getAttribute("groupid")==null?"":request.getAttribute("groupid") %>" />
					  		<input type="text" id="depNewName" maxlength="15" value="" />
				  		</td>
				  </tr>
				  <tr><td height="5px;"></td></tr>
				  
				  <tr><td height="5px;"></td></tr>
				  <tr><td colspan="2" align="center"><input type="button" value="<emp:message key="appmage_common_opt_queding" defVal="确定" fileName="appmage"></emp:message>"  class="btnClass5 mr23" id="updateSubmit"  onclick="javascript:updateDepName();"/>
				  <input type="button" value="<emp:message key="appmage_common_opt_quxiao" defVal="取消" fileName="appmage"></emp:message>"  class="btnClass6" id="updateCancle"  onclick="javascript:noUpdate();"/></td></tr>
				  </table>
			 </div>
			 
			 <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
			 <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
			 <script>
				 	$(document).ready(function(){
					 $("#addDep").dialog({
					autoOpen: false,
					//height:150,
					title:getJsLocaleMessage('appmage','appmage_page_webexlist_addnewgroup'), //"新增用户群组",
					width: 280,
					modal: true,
					resizable :false,
					close:function(){
					var name = $("#depName").val("");
		            //var scode = $("#sdepcode").val("");
		            var scode = $("#depcodethird").val("");
						}
					});
					 $('#list p').click(function() {
							//如果点击的元素重新被点中 不作任何操作
							if($(this).hasClass("roleNameColor")){
								return;
							}
							//选中元素样式
							$(this).addClass("roleNameColor").siblings().removeClass("roleNameColor"); 
					 });
				$("#updateDep").dialog({
				autoOpen: false,
				//height:150,
				title:getJsLocaleMessage('appmage','appmage_smstask_text_6'),
				width: 280,
				modal: true,
				resizable :false,
				close:function()
				{
				
				}
		});

				});
			var lguserid='<%=request.getAttribute("lguserid")%>';	

			  </script>
  </body>
</html>
