<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.entity.gateway.AgwParamConf"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.GwCluStatus"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	List<AgwParamConf> paramList = (List<AgwParamConf>) request
			.getAttribute("paramList");
	@ SuppressWarnings("unchecked")
	Map<String, String> valueMap = (Map<String, String>) request
			.getAttribute("valueMap");
    String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");		
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	String txglFrame = skin.replace(commonPath, inheritPath);
	
	@ SuppressWarnings("unchecked")
List<GwCluStatus> gwCluStatusList = (List<GwCluStatus>) request.getAttribute("gwCluStatusList");
String gwNo = (String) request.getAttribute("gwNo");
String keyId = (String)request.getAttribute("keyId");


String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

//保存
String bc = MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_bc", request);

%>
<%!
  	
	public String listColumn(AgwParamConf param,
			Map<String, String> valueMap) {
		String controltype = param.getControltyp().toString();
		String tableHtml = "";
		String defaultValue = param.getDefaultValue();
		String paramItem = param.getParamItem();
		String valueRange = param.getValueRange();

		if (valueMap.get(paramItem) != null) {
			defaultValue = valueMap.get(paramItem).trim();
		} else {
			defaultValue = defaultValue == null ? "" : defaultValue.trim();
		}
		if ("0".equals(controltype)) {
			tableHtml += "<input maxlength='20' name='" + paramItem + "' id='" + paramItem
					+ "' class='input' type='text' value='" + defaultValue
					+ "' /></td>";
		} else if ("1".equals(controltype)) {
			tableHtml += "<select class='input1' id='" + paramItem + "' name='"
					+ paramItem + "'>";
			if (valueRange != null && valueRange != "") {
				String[] items = valueRange.split(",");
				for (int j = 0; j < items.length; j++) {
					tableHtml += "<option value='" + items[j] + "'"
							+ (items[j].equals(defaultValue) ? "selected" : "")
							+ ">" + items[j] + "</option>";
				}
			}
			tableHtml += "</select></td>";
		} else if ("2".equals(controltype)) {
			if (valueRange != null && valueRange != "") {
				String[] items = valueRange.split(",");
				String[] items2 = null;
				items2 = defaultValue.split("-");
			
				for (int j = 0; j < items.length; j++) {
					String checked = "";
					if (items2.length >= j + 1
							&& items2[j].equals("1")) {
						checked = "checked";
					} 
					tableHtml += "&nbsp;<input type='checkbox' id='"
							+ paramItem + "' name='" + paramItem + "' "
							+ checked + " value='" + items[j] + "'/>"
							+ items[j];
				}
			}
			tableHtml += "</td>";
		} else if ("3".equals(controltype)) {
			tableHtml += "<input name='" + paramItem + "' id='" + paramItem
					+ "' class='numInput' type='text' value='" + defaultValue
					+ "' />";
			if (valueRange != null && !"".equals(valueRange.trim())) {
				tableHtml += "</td><td><label>" + valueRange + "</label></td>";
			} else {
				tableHtml += "</td>";
			}
		}
		return tableHtml;
	}%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/common/common.jsp" %>
<title></title>
	
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
<style>
#paramValues table {
	width : <%=StaticValue.ZH_HK.equals(empLangName)?140:100 %>%;
	/*background-color: #EFEFEF;*/
}
</style>
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style>
		/* 避免英文状态下，单词被换行*/
			#content tbody td {
				word-break:break-word;
			}
		</style>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/gat_webgate.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
</head>
	<body id="gat_webgate">
    <div id="container">
    	<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
    	<div class="rContent container_down_div"  >
            	<form action="" name="form1"  method="post">
<%--            	<input type="hidden" value="99" name="gwNo" id="gwNo"/>--%>
            	<input type="hidden" value="4000" name="gwType" id="gwType"/>
            	    <div id="paramValues">
            		<table class="paramValues_table">
            		
						<tr>
            				<td  colspan="3">
            				<table >
            				<tr><td>
            				<div class="buttons add_div"   >
            				<a id="add" href="javascript:addGW()"><emp:message key="txgl_wgqdpz_qyhdgl_xj" defVal="新建" fileName="txgl"></emp:message></a>
            				</div>
            				</td></tr>
            				
              				<tr class="wgcx_class"><td>
            					<emp:message key="txgl_wghdpz_wgyxcspz_wgcx" defVal="网关程序：" fileName="txgl"></emp:message>
            					<select name="gwNo" id="gwNo" class="gwNo">
            						<%
            							if (gwCluStatusList != null && gwCluStatusList.size() > 0) {
            								for (GwCluStatus gwCluStatus : gwCluStatusList) {
            						%>
            						<option value="<%=gwCluStatus.getGwNo()%>" <%=gwNo.equals(gwCluStatus.getGwNo().toString()) ? "selected"
									: ""%>><emp:message key="txgl_wghdpz_wgyxcspz_wgbh" defVal="网关编号" fileName="txgl"></emp:message>
            							<%=gwCluStatus.getGwNo()%>
            						</option>
            						<%}	}%>
            					</select>
            					<input type='hidden' id='keyId' value='<%=keyId%>'/>
            					</td></tr></table>
            				</td>
            			</tr>
            			
               	  		<tr>
               	  				<td colspan="3" class="div_bg">
               	  				<div id="generalParamDiv" class="generalParamDiv paramDiv_set"><b><emp:message key="txgl_wghdpz_wgyxcspz_ptcssz" defVal="普通参数设置" fileName="txgl"></emp:message></b>
               	  				<a id="generalParamIcon"  class="unfold <%="zh_HK".equals(empLangName)?"generalParamIcon1":"generalParamIcon2"%>"  >&nbsp;&nbsp;&nbsp;&nbsp;</a></div></td>
               	  		</tr>
               	  				<tr><td  colspan="3">
               	  				<div id="generalParam" >
               	  				<table id="content" class="tableCss">
               	  				<tbody>
									<%
										String allParamItems="";
										if (paramList != null && paramList.size() > 0) {
											for (AgwParamConf param : paramList) {
												int paramAttribute = param.getParamAttribute();
												if (paramAttribute == 0) {
												allParamItems += param.getParamItem() + ",";
									%>
									<tr>
										<%--<td class="tdtxt"><%="内部端口".equals(param.getParamName())?"EMP网关端口":param.getParamName()%></td>
										--%>
										<td class="tdtxt"><%="内部端口".equals(param.getParamName())?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_empwgdk", request):(StaticValue.ZH_HK.equals(langName)?param.getENParamName():(StaticValue.ZH_TW.equals(langName)?param.getHKParamName():param.getParamName()))%></td>
										<td class="tdinput">
											<%
												out.print(this.listColumn(param,valueMap));
											%>
										</td>
										<%--<td><%=param.getParamMemo()%></td>
										--%>
										<td><%=(StaticValue.ZH_HK.equals(langName)?param.getENParamMemo():(StaticValue.ZH_TW.equals(langName)?param.getHKParamMemo():param.getParamMemo()))%></td>
									</tr>
									<%
										}}}
									%>
									</tbody>
									</table></div></td></tr>
								<tr>
								 <tr class="advancedParamDiv_up_tr">
								 <td  colspan="3"></td>
								 </tr>
               	  				<td colspan="3" class="div_bg">
               	  				<div id="advancedParamDiv" class="advancedParamDiv paramDiv_set"><b><emp:message key="txgl_wghdpz_wgyxcspz_gjcssz" defVal="高级参数设置" fileName="txgl"></emp:message></b>
               	  				<a id="advancedParamIcon"  class="fold <%="zh_HK".equals(empLangName)?"advancedParamIcon1":"advancedParamIcon2"%>"  >&nbsp;&nbsp;&nbsp;&nbsp;</a></div></td>
               	  				</tr>
               	  				<tr><td  colspan="3">
               	  				<div id=advancedParam>
               	  				<table id="content" class="tableCss">
               	  				<tbody>
               	  				<%
										if (paramList != null && paramList.size() > 0) {
											for (AgwParamConf param : paramList) {
												int paramAttribute = param.getParamAttribute();
												if (paramAttribute == 1) {
												allParamItems += param.getParamItem()+ ",";
												
									%>
									
									<tr>
										<%--<td class="tdtxt"><%=param.getParamName()%></td>--%>
										<td class="tdtxt"><%=(StaticValue.ZH_HK.equals(langName)?param.getENParamName():(StaticValue.ZH_TW.equals(langName)?param.getHKParamName():param.getParamName()))%></td>
										<td class="tdinput">
											<%
												out.print(this.listColumn(param,valueMap));
											%>
										</td>
										<%--<td><%=param.getParamMemo()%></td>--%>
										<td><%=(StaticValue.ZH_HK.equals(langName)?param.getENParamMemo():(StaticValue.ZH_TW.equals(langName)?param.getHKParamMemo():param.getParamMemo()))%></td>
	
									</tr>
									<%}	
									}
									}
									%>
									</tbody>
									</table></div></td></tr>
               	  			</table>
               	  		<table class="allParamItems_table">
               	  		<tr  class="allParamItems_tr">
               	  			<td id="btn"><input type='hidden' value='<%=allParamItems%>' id='allParamItems'/>
               	  				<center><input type="button" onclick="submitForm()" value="<%=bc %>" class="btnClass4"/></center>
               	  			</td>
               	  		</tr>
               	  	</table>
               	  	</div>
        	  	</form>
    	</div>
    	<div class="bottom"><div id="bottom_right"><div id="bottom_left"></div></div></div>
	</div><%--end round_content--%>
	<script language="javascript" type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" type="text/javascript" src="<%=iPath%>/js/gat_webgate.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	
	<script>
		var times= <%=System.currentTimeMillis()%>;
		String.prototype.replaceAll  = function(s1,s2){    
		    return this.replace(new RegExp(s1,"gm"),s2); 
		 }
		$(document).ready(function(){
			var findresult="<%=(String)request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
		       alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_1"));	
		       return;			       
		    }
			$(".tableCss tbody tr").hover(function() {
				$(this).addClass("hoverColor");
			}, function() {
				$(this).removeClass("hoverColor");
			});
			
			$("#generalParam").show();
			$("#generalParamDiv").toggle(function() {
				$("#generalParam").hide();
				$('#generalParamIcon').addClass("fold");
				$('#generalParamIcon').removeClass("unfold");
			}, function() {
				$("#generalParam").show();
				$('#generalParamIcon').addClass("unfold");
				$('#generalParamIcon').removeClass("fold");
			});
			$("#advancedParam").hide();
			$("#advancedParamDiv").toggle(function() {
				$("#advancedParam").show();
				$('#advancedParamIcon').addClass("unfold");
				$('#advancedParamIcon').removeClass("fold");
			}, function() {
				$("#advancedParam").hide();
				$('#advancedParamIcon').addClass("fold");
				$('#advancedParamIcon').removeClass("unfold");
			});
			
			$('#gwNo').change(function(){
			location.href = "<%=path%>/gat_webgate.htm?gwNo="+$('#gwNo').val();
		    });
		});
		function addGW()
		{
			//if(confirm("是否新建一个网关程序？"))
			if(confirm(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_2")))
			{
				$.post("gat_webgate.htm?method=add",
				{
				},
				function(result){
					if(result == "true"){
						//alert("新建网关程序成功！");
						alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_3"));
						location.href = "<%=path%>/gat_webgate.htm?gwNo=99";
					}else if(result == "error"){
						//alert("新建网关程序失败！");
						alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_4"));
					}else if(result == "false"){
						//alert("新建网关程序失败！");
						alert(getJsLocaleMessage("txgl","txgl_wghdpz_wgyxcspz_text_4"));
					}
				}
			);
			}
		}
		
	</script>
</body>
</html>
