<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.form.LfFomType"%>
<%@page import="com.montnets.emp.entity.form.LfFomInfo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
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
	List<LfFomInfo> otfomInfoList = (List<LfFomInfo>)request.getAttribute("otFomInfoList");
	
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
		
%>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wzgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>

<script>
	$(document).ready(function() {
		 initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
	});


	$("div.abc").on({
		  mouseenter: function(){
			  $(this).css("border-bottom-color","red");
		  },
		  mouseleave: function(){
			  $(this).css("border-bottom-color","#52a6e7");
		  }
	});
	  
</script>
	
	<form  name="pageForm" action="wzgl_formManager.htm?method=toFormTempalte" method="post" id="pageForm">
	 	 <div id="m_container">
	 			    <%
	 			    		boolean isData = false;
	                		if(otfomInfoList != null && otfomInfoList.size() > 0)
	               			{
	                		    isData = true;
	                			LfFomInfo fomInfo = null;
	                 			for(int i=0;i<otfomInfoList.size();i++)
	                 			{
	                  			   fomInfo = otfomInfoList.get(i);
	                		%>
		  						<div id="m_c_list" onclick="optForm('<%=fomInfo.getFId() %>','apply');" 
		  							class="abc">
		            				<div id="channel_management" class="l_m_c_left div_bd ">
		            				   <img src="<%=iPath %><%=fomInfo.getUrl() %>">
		         					</div>
		         					<div class="l_m_c_right">
		          					<p><%=fomInfo.getTitle() %>   &nbsp;&nbsp;&nbsp;  <%=fomInfo.getSubmitCount() %> <emp:message key="wzgl_qywx_form_text_12" defVal="人用过"
											fileName="wzgl" /> </p>
			            			<p><%=fomInfo.getNote() %></p>
		         					</div>
		       					</div>
	 		    	<%	
	                      	}
	                 }else{
	                     %>
	                     		<div id="build_div" style="margin-left: 20px;padding-top:120px;">
		            				   <img src="<%=iPath %>/img/build.png">
		         				</div>
	                     
	                    <%	 
	                 }
	             %> 
	     </div>
	     
	       <%
             if(isData){
           %>
			     <div id="pageInfo">
			     </div>
	     	<%	
	            }
	       %> 
	     <%--
	     <div id="buttonDiv">
	       		<input type="button" id="btn" class="btnClass6" value="自定义" onclick="optForm('','defined')"/>
	     </div>
	     --%>
	</form>
