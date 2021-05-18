<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.form.LfFomType"%>
<%@page import="com.montnets.emp.entity.form.LfFomInfo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("formManager");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
		
	@ SuppressWarnings("unchecked")
    List<LfFomType> otfomTypeList = (List<LfFomType>)request.getAttribute("fomTypeList");	
	//表单类型
	String typeId = (String)request.getAttribute("typeId");
	//表单类型的父ID
	String parentId = (String)request.getAttribute("parentId");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>表单模板</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/formtype.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
			<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body onload="clickFormType('first','','0');">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
				<input type="hidden" id="pathUrl" value="<%=path %>" />
				<input type="hidden" id="servletUrl" value="<%=path %>/wzgl_formManager.htm?method=toFormTempalte"/>
				<input type="hidden" id="typeId" value="<%=typeId %>" />
				<input type="hidden" id="parentId" value="<%=parentId %>" />
				<div style="display:none" id="getloginUser"></div>
					<div id="roleSetup">
               			<div id="r_s_sider">
                       		<div id="siderList_con" class="div_bd"  style="height:480px;">
                      		 	<h5 class="div_bd title_bg" style="border-left:0;border-right: 0;border-top: 0;background: #52a6e7;">
                      		 		<span style="padding-left:10px;"><emp:message key="wzgl_qywx_form_text_13" defVal="分类"
											fileName="wzgl" /></span>
                      		 		<span style="float: right;padding-right: 10px;">
                      		 			<a href="javascript:optForm('','defined')"><emp:message key="wzgl_qywx_form_text_14" defVal="自定义"
											fileName="wzgl" /></a>
                      		 		</span>
                      		 	</h5>
                       			<div id="siderList" class="list">
                       				    <%
		                     				if(otfomTypeList != null && otfomTypeList.size() > 0)
		                    			{
		                     				LfFomType fomtype = null;
			                     			for(int i=0;i<otfomTypeList.size();i++)
			                     			{
			                     			   fomtype = otfomTypeList.get(i);
		                    			%>
						                	<p id="p_<%=fomtype.getTypeId()%>" 
						                		<% if(i==0){%> class="open" <%}%> 
						                	  onclick="javascript:clickFormType('nofirst','<%=fomtype.getTypeId()%>','<%=fomtype.getParentId()%>');"
						                	 >
						                		<%=fomtype.getName().replaceAll("<","&lt;").replaceAll(">","&gt;") %>
				                            </p>
				                         <%	
				                        	}
		                      			}
		                     			%> 
                            	</div>  
                       		</div>
       				</div>
	             	<div id="r_s_manager"> 
					</div>
				</div>
			</div>
				<%-- 内容结束  --%>
				<%-- foot开始 --%>
				<div class="bottom">
					<div id="bottom_right">
						<div id="bottom_left"></div>
					</div>
				</div>
					<%-- foot结束 --%>
					</div>
		   		 <div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wzgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
   		 <script src="<%=commonPath%>/common/js/myjquery-a.js"></script>
	    <script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/artDialog.js?skin=default"></script>
	  	<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script type="text/javascript">
			$(document).ready(function() {
				getLoginInfo("#getloginUser");
			});
			//type  套用模板 opt 自定义 类型defined    formid模板ID
			function optForm(formid,type){
				var pathUrl = $("#pathUrl").val();
				var corpCode = $("#lgcorpcode").val();
				if(type == "apply"){
					doGo(pathUrl+"/wzgl_formManager.htm?method=toSavePageForm&handletype=apply&formid="+formid+"&lgcorpcode="+corpCode);
				}else if(type == "defined"){
					doGo(pathUrl+"/wzgl_formManager.htm?method=toSavePageForm&handletype=defined&lgcorpcode="+corpCode);
				}
			}

			//点左侧的表单类型 以及  父ID  
			function clickFormType(entertype,typeId,parentId){
				if(entertype == "first"){

				}else{
					$(".open").removeClass("open");
					$("#p_"+typeId).addClass("open");
				}
				var pageIndex=$('#txtPage').val();
				var pageSize=$('#pageSize').val();
				var url=$('#servletUrl').val();
				$('#typeId').val(typeId);
				$('#parentId').val(parentId);
				$('#r_s_manager').load(url,{
					pageIndex:pageIndex,
					pageSize:pageSize,
					typeId:typeId,
					parentId:parentId,
					type:'click'
				},function(){
					
				});
			}

			//分页处理
			function submitForm(pageIndex){
				var url=$('#servletUrl').val();
				var typeId=$('#typeId').val();
				var parentId=$('#parentId').val();
				$('#r_s_manager').load(url,{
					pageIndex:pageIndex,
					typeId:typeId,
					parentId:parentId,
					type:'page'
				},function(){
					
				});
			}
			
		</script>	
	</body>
	
</html>
