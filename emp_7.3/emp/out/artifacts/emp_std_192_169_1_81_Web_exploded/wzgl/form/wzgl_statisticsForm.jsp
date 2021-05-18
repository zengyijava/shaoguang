<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.form.LfFomField"%>
<%@page import="com.montnets.emp.entity.form.LfFomInfo"%>
<%@page import="com.montnets.emp.entity.form.LfFomQuestion"%>
<%@page import="com.montnets.emp.entity.form.LfFomFieldvalue"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
	String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("formManager");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
	
	//表单信息
	LfFomInfo otFomInfo = (LfFomInfo)request.getAttribute("otFomInfo");
	@ SuppressWarnings("unchecked")
	List<LfFomQuestion> questionList = (List<LfFomQuestion>)request.getAttribute("questionList");
	@ SuppressWarnings("unchecked")
	List<LfFomField> fomFieldList = (List<LfFomField>)request.getAttribute("fomFieldList");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String,List<LfFomField>> fomFieldMap = (LinkedHashMap<String,List<LfFomField>>)request.getAttribute("fomFieldMap");
   @ SuppressWarnings("unchecked")
   LinkedHashMap<String,Integer> fieldCountMap = (LinkedHashMap<String,Integer>)request.getAttribute("fieldCountMap");
   //问题
   LfFomQuestion fomQuestion = null;
   //选项
   LfFomField fomField = null;
	//问题ID
   String questionId = "";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	    <title>表单统计</title>
	   	<link rel="stylesheet" href="<%=commonPath%>/common/css/global.css?V=<%=StaticValue.getJspImpVersion() %>">
	   	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
<body> 
	<div style="height: auto;">
		<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="getloginUser" style="padding:5px;display:none;">
		</div>
		<div class="itemDiv" style="margin-top:30px;padding-left: 30px;" >
			<table class="div_bg div_bd" style="height:23px;width: 700px;">
			   		<tr>
						<td>
							&nbsp;&nbsp;<%=otFomInfo.getTitle().replaceAll("<","&lt;").replaceAll(">","&gt;") %> 
							 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<emp:message key="wzgl_qywx_form_text_20" defVal="参与人数 ："
											fileName="wzgl" />   <%=otFomInfo.getSubmitCount() %>
						</td>
					</tr>
			</table>
		</div>

		<% 
			if(questionList != null && questionList.size() > 0){
			    String label = MessageUtils.extractMessage("wzgl","wzgl_qywx_form_text_21",request);
			    for(int i=0;i<questionList.size();i++){
			        fomQuestion = questionList.get(i);
			        if("1".equals(fomQuestion.getFiledType())){
			            label = MessageUtils.extractMessage("wzgl","wzgl_qywx_form_text_21",request);
			        }else if("2".equals(fomQuestion.getFiledType())){
			            label = MessageUtils.extractMessage("wzgl","wzgl_qywx_form_text_22",request);
			        }
			        questionId = String.valueOf(fomQuestion.getQId());
			        List<LfFomField> fieldList = fomFieldMap.get(questionId);
		%>
					<table  class="div_bg div_bd" style="margin-left:30px;height:23px;width: 700px;margin-top: 5px;">
						<tr>
							<td colspan="2" height="30px;">
								<div style="padding-left: 10px;"> <emp:message key="wzgl_qywx_form_text_17" defVal="问题"
											fileName="wzgl" />    <%=i+1%> : <%=fomQuestion.getTitle().replaceAll("<","&lt;").replaceAll(">","&gt;") %>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div id="div<%=questionId %>" style="width: 300px;float:left;border: 1;"></div>
							</td>
							<td>
								<table  class="div_bg div_bd" style="height:155px;width: 300px;float: right;border: 0;">
										<tr style="border-bottom: 1px;">
											<td align="left" style="width:180px;">&nbsp;&nbsp;<%=label %></td>
											<td align="center"><emp:message key="wzgl_qywx_form_text_23" defVal="数据量"
											fileName="wzgl" /> </td>
										</tr>
											<%
												if(fieldList != null && fieldList.size()>0){
												    String letter = "";
												    for(int m=0;m<fieldList.size();m++){
								   			 			fomField = fieldList.get(m);
								   			 			int count = 0;
								   			 		 	 if(fieldCountMap != null && fieldCountMap.get(fomField.getFieldId().toString()) != null){
								   			 		    	count = fieldCountMap.get(fomField.getFieldId().toString());
								   			 		    }
								   			 		 	 if(m == 0){
								   			 		 		letter = "A";
								   			 		 	 }else if(m == 1){
								   			 				letter = "B";
								   			 			 }else if(m == 2){
								   			 				letter = "C";
								   			 			 }else if(m == 3){
								   			 				letter = "D";
								   			 			 }else if(m == 4){
								   			 				letter = "E";
								   			 			 }else if(m == 5){
								   			 				letter = "F";
								   			 			 }
											
											%>
										<tr>
											<td align="left">
													&nbsp;&nbsp;&nbsp;&nbsp;<%=letter %>:&nbsp;&nbsp;
													<%=fomField.getFieldValue().replaceAll("<","&lt;").replaceAll(">","&gt;") %>
											</td>
											<td  align="center">
													<%=count %>
											</td>
										</tr>
											<%
												    }
												}
											%>
								</table>
							</td>
						</tr>
					</table>
					
		<% 
			    }
			}
		%>
		<div class="itemDiv " style="height: 30px;padding-top:25px;">
			<table style="width: 700px;height: 35px;">
			   		<tr>
						<td align="right">
								<input type="button" id="cancelbtn" value="<emp:message key="common_btn_10" defVal="返回"
											fileName="common" /> " class="btnClass6" onclick="javascript:back();" />
						</td>
					</tr>
			</table>
		</div>
		<div style="height:75px;">
		</div>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wzgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    <script language="javascript" src="<%=commonPath%>/common/widget/graphical/FusionCharts.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
    <script language="javascript" src="<%=commonPath%>/common/widget/graphical/table3.0.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
    <script language="javascript" src="<%=commonPath%>/common/widget/graphical/Chart.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
    <script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script language="javascript" src="<%=iPath%>/js/transformLetter.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
     <script type="text/javascript">
	showAllChart=function(id,width,heigth,url,cates,countArray){
	var chart = new Jon.Chart({
		caption:getJsLocaleMessage("wzgl","wzgl_qywx_form_text_32"),
		subcaption:'',
		decimalPrecision:'0',
		xAxisName:getJsLocaleMessage("wzgl","wzgl_qywx_form_text_33"),
		yAxisName:'',
		numberSuffix:getJsLocaleMessage("wzgl","wzgl_qywx_form_text_34"),
		base:{
			id:"CartLine",
			url:url,
			width:width,
			height:heigth,
			renderTo:id
		}
		});
		chart.loadCategories(cates);
		chart.add({seriesName:getJsLocaleMessage("wzgl","wzgl_qywx_form_text_35"),color:'00cc00',anchorBorderColor:'00cc00', anchorBgColor:'00cc00' },countArray); 
		chart.show();
	}

	$(document).ready(function(){
  			getLoginInfo("#getloginUser");
	  		<% 
			if(questionList != null && questionList.size() > 0){
			   
			    for(int i=0;i<questionList.size();i++){
			        fomQuestion = questionList.get(i);
			        questionId = String.valueOf(fomQuestion.getQId());
			        List<LfFomField> fieldList = fomFieldMap.get(questionId);
			        if(fieldList != null && fieldList.size() > 0){
			%>
			  		//类别
		   			 var cates = new Array();
		   			 var countArray = new Array();
		   			 <%
		   			        //int a = 0;
		   			 		for(int m=0;m<fieldList.size();m++){
		   			 			fomField = fieldList.get(m);
		   			 		   // a = m+1;
		   			 			int count = 0;
		   			 		    if(fieldCountMap != null && fieldCountMap.get(fomField.getFieldId().toString()) != null){
		   			 		    	count = fieldCountMap.get(fomField.getFieldId().toString());
		   			 		    }
		   			 %>
		   					var letter = transLetter(<%=m%>);
		   			 	 	cates.push(letter);
		   			 		countArray.push(<%=count%>);
		   			 <%   
		   			 	  }
		   			 %>
					showAllChart("div<%=questionId %>",400,270,"<%=commonPath%>/common/swf/FCF_MSColumn3D.swf",cates,countArray);
			<% 
			      	  }
				    }
				}
			%>
     	});

		//跳转回查询页面
		function back(){
			var lgcorpcode = $("#lgcorpcode").val();
			var pathUrl = $("#pathUrl").val();
			doGo(pathUrl + "/wzgl_formManager.htm?method=find&lgcorpcode="+lgcorpcode);
			return;
		}
    </script>
</body>
</html>








