<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path=request.getContextPath();
String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
String langName = (String)session.getAttribute("emp_lang");	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=path%>/common/css/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=path%>/common/css/table.css" rel="stylesheet" type="text/css" />
		<link href="<%=path%>/common/css/empSelect.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/empSelect.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=path%>/common/widget/jqueryui/css/jquery.ui.all.css" />
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/jqueryui/myjquery-j.js"></script>
		<script type="text/javascript" src="<%=path%>/common/js/common.js"></script>
		<script type="text/javascript" src="<%=path%>/common/js/jquery.ztree-myjquery-b.js"></script>
		<script type="text/javascript" src="../internal.js"></script>
		<script type="text/javascript" src="<%=path%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=path%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=path%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
		<style type="text/css">
			.aparam {
				margin-left:10px;
				cursor: pointer;
			}
			#tabParam tr{
				height:25px;
			}
		</style>
		
		<script type="text/javascript">
			$(document).ready(function(){
		
				$("#divBox").dialog({
					autoOpen: false,
					height:120,
					width: <%="zh_HK".equals(empLangName)?280:250%>,
					modal: true,
					close:function(){
					}
				});
				
				initParams();
					
			});
			
			function initParams(){
				
				var params = $("#params",parent.document).val();
				if(params == null ||params == "null" ||params.length==0){
					params ="1,2,3,4,5";
					$("#params",parent.document).val(params);
				}
				$("#params").val(params);

				var paramsArray= new Array(); //定义一数组

				paramsArray=params.split(",");
				for (i=0;i<paramsArray.length ;i++ )
				{
					addOneParam(paramsArray[i]);
				}
			}
			
			function openParam(){	
	  			
	  			var paramCount = parseInt($("#paramCount").val());
	  			var paramname = paramCount + 1;
				$("#paramSe").val(paramname);
				$("#paramCount").val(paramCount);
				$("#divBox").dialog("open");
			}
			
			function cancelParam(){
				$("#divBox").dialog("close");
			}
			
			function addParam(){
				
				var paramSe = $("#paramSe").val();
				var params = $("#params").val();
				
				if(paramSe == null || paramSe.length==0){
					alert(getJsLocaleMessage("ydwx","ydwx_wxbj_134"));
					return;
				}
				
				var repeatParam = false;
				$("input[name='cbParam']").each(function(){
					var tmpval = $(this).val();
					if(paramSe == tmpval){
						repeatParam = true;
						return false;
					}
				});
				
				if(repeatParam){
					alert(getJsLocaleMessage("ydwx","ydwx_wxbj_135"));
					return;
				}
				
				addOneParam(paramSe);
				
				if(params == null || params.length==0){
					params ="";
				}else{
					params +=",";
				}
				var paramsS = params+paramSe;
				$("#params").val(paramsS);
				$("#params",parent.document).val(paramsS);
				
				if(!isNaN(paramSe)){
					var paramCount = parseInt(paramSe) + 1;
					$("#paramSe").val(paramCount);
				}
			}
			
			function addOneParam(paramSe){
				//一行显示数量
				var count = 5;
				var paramCount = parseInt($("#paramCount").val());
				var trCount = parseInt($("#trCount").val());
				var trHtml = "";

				if(paramCount/count >= trCount){
					trCount += 1; 
					trHtml="<tr id='tr"+trCount+"' ></tr>";
					
					$("#trCount").val(trCount);
					$("#tabParam").append(trHtml);
				}
				var trId = "tr"+trCount;
				
				$("#paramCount").val(paramCount+1);
				
				//var param = "#P_"+paramSe+"#";
				var paramNa = getJsLocaleMessage("common","common_parameter");
				var strHtml = "<td><input type='checkbox' name='cbParam' value='"+paramSe+"' class='aparam' />"+paramNa+""+paramSe+"</td>";
				var trParam=$("#"+trId);
				trParam.append(strHtml);
				
			}
			
			dialog.onok = function (){
				
				var useParam = $("#useParam",parent.document).val();
				if(useParam==null || useParam.length==0){
					useParam = "";
				}else{
					useParam += ",";
				}
				var strHtml = "";
				var parFontSize = "16";
				var parFontFam = "";
				var empLangName = $("#empLangName").val();
				$("input[name='cbParam']:checkbox:checked").each(function(){
					if($(this).val() != null){
						useParam += $(this).val() + ",";
						var num = $(this).val().length*9;
						var widthLength = ("zh_HK" === empLangName?47:33)+num;

						var parStyle = 'style="BORDER-TOP:0px;BORDER-RIGHT:0px;BORDER-BOTTOM:0px;BORDER-LEFT:0px;WIDTH:'+widthLength+'px;FONT-SIZE:'+parFontSize+'px;'+parFontFam+'"';

						var param = '<input name="#P_'+$(this).val()+'#" value="'+getJsLocaleMessage("ydwx","ydwx_common_canshu")+$(this).val()+'" '+parStyle+' readonly="readonly" typeend="#P_'+$(this).val()+'#" />';
						strHtml += param;
						
					}
				});
				
				if(strHtml == null || useParam.length==0){
					alert(getJsLocaleMessage("ydwx","ydwx_wxbj_136"));
					return;
				}
				
				$("#useParam",parent.document).val(useParam.substring(0,useParam.length-1));
				
				editor.execCommand("insertactividata",strHtml);
				dialog.close();
			    <%-- 校验元素类型 --%>
				if($("#wx_tempType1",parent.document).attr("checked")){
					//alert("当前您选择插入对象为动态模板元素！");
					$("#wx_tempType2",parent.document).attr("checked","true");
				}
			}
			
		</script>
		
	</head>
	<body>
	

	
	<div id="rContent" class="rContent">
		<div id="divParam" style="font-size: 14px;"><table id="tabParam"><tr id='tr1'></tr></table></div>
	<%--  <div style="margin-left:10px;margin-top: 20px;" >
	<span >参数字号：</span>
	<select id="parFontSize" name="parFontSize" >
		<option value="10">10px</option>
		<option value="11">11px</option>
		<option value="12">12px</option>
		<option value="14">14px</option>
		<option value="16" selected >16px</option>
		<option value="18">18px</option>
		<option value="20">20px</option>
		<option value="24">24px</option>
		<option value="36">36px</option>
	</select>
	<span>参数字体：</span>
	<select id="parFontFam" name="parFontFam">
		<option value="FONT-FAMILY: &#39;宋体&#39;, &#39;SimSun&#39;" selected >宋体</option>
		<option value="FONT-FAMILY: &#39;楷体&#39;, &#39;楷体_GB2312&#39;, &#39;SimKai&#39;">楷体</option>
		<option value="FONT-FAMILY: '隶书', 'SimLi'">隶书</option>
		<option value="14">黑体</option>
		<option value="16">andale mono</option>
		<option value="18">arial</option>
		<option value="20">arial black</option>
		<option value="24">comic sans ms</option>
		<option value="36">impact</option>
		<option value="36">times new roman</option>
	</select>
	</div>--%>
	
	<input type="button" value="<emp:message key="ydwx_wxbj_add_8" defVal="新增参数" fileName="ydwx"></emp:message>" onclick="openParam()" style="margin-left:10px;margin-top: 20px;height: 30px;width: 90px;" />
	<input type="hidden" value="0" name="paramCount" id="paramCount" />
	<input type="hidden" value="1" name="trCount" id="trCount" />
	<input type="hidden" value="" name="params" id="params" />
	<input type="hidden" value="<%=empLangName%>" id="empLangName"/>
	</div>
	

	
	<div id="divBox" style="display:none;padding-left: 5px;padding-top: 10px;" title="<emp:message key="ydwx_wxbj_add_10" defVal="添加参数" fileName="ydwx"></emp:message>" >
		<table style="text-align:right;">
			<tr><td style="width: <%="zh_HK".equals(empLangName)?120:80%>px;"><emp:message key="ydwx_wxbj_add_9" defVal="参数序号：" fileName="ydwx"></emp:message></td><td><input type="text" name="paramSe" id="paramSe" value="" style="width: 120px;"/></td></tr>
			<%-- <tr><td>参数名：</td><td><input type="text" name="paramNa" id="paramNa" value=""/></td></tr> --%>
			<tr style="text-align:center;height: 50px;">
			<td colspan="2">
				<input type="button" style="width: 60px;height: 25px;" value="<emp:message key='common_confirm' defVal='确定' fileName='common'></emp:message>"  onclick="addParam()" />
				<input type="button" style="width: 60px;height: 25px;margin-left: 10px;" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" onclick="cancelParam()" />
			</td>
			</tr>
			
		</table>
	</div> 
	
	</body>
</html>