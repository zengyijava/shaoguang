<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.entity.selfparam.LfWgParmDefinition"%>
<%@page import="com.montnets.emp.entity.selfparam.LfWgParamConfig"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path=request.getContextPath();
    String langName = (String)session.getAttribute("emp_lang");
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath=iPath.substring(0,iPath.lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	@ SuppressWarnings("unchecked")
	List<LfWgParamConfig> lfwgparamconfigList=(List<LfWgParamConfig>)request.getAttribute("LfWgParamConfigList");
	//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
   // Long curId = sysuser.getUserId();
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	
	LfWgParmDefinition lfdefinition = (LfWgParmDefinition)request.getAttribute("lfdefinition");
	String params="";
	if(lfdefinition !=null)
	{
		if(lfdefinition.getParam().equals("Param2"))
		{
		   params="2";
		}
		else if(lfdefinition.getParam().equals("Param3"))
		{
		  params="3";
		}
		else if(lfdefinition.getParam().equals("Param4"))
		{
		  params="4";
		}
	}
	
	String findResult= (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
        <title><%=titleMap.get(menuCode) %></title>
        <%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
  	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/par_selParamConfig.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/par_selParamConfig.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
  </head>
  
  <body id="par_selParamConfig">
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("xtgl","xtgl_cswh_zdcsgl_cszck",request)) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			
					<div class="titletop cszck_div"  >
						<table class="titletop_table cszck_table" >
								<tr>
								    <td class="titletop_td">
								            <emp:message key="xtgl_cswh_zdcsgl_cszck" defVal="参数值查看" fileName="xtgl"/>    
						        	</td>
									<td align="right">
										<span class="titletop_font"  onclick="javascript:location.href='<%=path%>/par_selParam.htm?method=find'">&larr;&nbsp;<emp:message key="xtgl_spgl_xxsp_fhsyj" defVal="返回上一级" fileName="xtgl"/></span>
									</td>
								</tr>
							</table>
					   </div>
					<form name="pageForm" action="<%=path %>/par_selParam.htm?method=findConfig" method="post" id="pageForm">					
								
						<input type="hidden" id="dpid" name="dpid" value="<%=lfdefinition!=null?lfdefinition.getPid():"" %>"/>
							<div class="buttons">
								<div id="toggleDiv" >
								</div>
								<span id="backgo" class="right mr5" onclick="javascript:location.href='<%=path%>/par_selParam.htm?method=find'">&nbsp;<emp:message key="xtgl_spgl_shlcgl_fh" defVal="返回" fileName="xtgl"/></span>
								 <%if(btnMap.get(menuCode+"-5")!=null) { %>		
								<a id="add" href="javascript:doAddConfig('<%=params %>','<%=conditionMap.get("paramSubNum")==null?"":conditionMap.get("paramSubNum") %>')"><emp:message key="xtgl_spgl_shlcgl_xj" defVal="新建" fileName="xtgl"/></a>
								<%} %>	
								<%-- <a id="back" href="par_selParam.htm?method=find"></a>	 --%>
											
							</div>
							<div id="condition">
								<table>
								    <tr>
								      <td>
								          <emp:message key="xtgl_cswh_zdcsgl_csmc_mh" defVal="参数名称:" fileName="xtgl"/>
								      </td>
								      <td>
								           <input type="text" name="ParamSubName" id="ParamSubName" value="<%=lfdefinition!=null?lfdefinition.getParamSubName():""%>" disabled="disabled" />
								      </td>
							         <td><emp:message key="xtgl_cswh_zdcsgl_csz_mh" defVal="参数值:" fileName="xtgl"/></td>
									 <td><input type="text" name="paramValue" id="paramValue" value="<%=conditionMap.get("paramValue&like")==null?"":conditionMap.get("paramValue&like")%>" /></td>
		                              <td><emp:message key="xtgl_cswh_zdcsgl_cszhy_mh" defVal="参数值含义:" fileName="xtgl"/></td>
									  <td><input type="text" name="paramName" id="paramName" value="<%=conditionMap.get("paramName&like")==null?"":conditionMap.get("paramName&like")%>" /></td>
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
										        <emp:message key="xtgl_cswh_zdcsgl_csz" defVal=" 参数值 " fileName="xtgl"/>
										</th>
										<th >
										       <emp:message key="xtgl_cswh_zdcsgl_cszhy" defVal="参数值含义" fileName="xtgl"/>
									    </th>	
									    <th>
									           <emp:message key="xtgl_spgl_shlcgl_bz" defVal="备注" fileName="xtgl"/>
									    </th>	
									     <%if(btnMap.get(menuCode+"-3")!=null && btnMap.get(menuCode+"-4")!=null) { %>		
									    <th colspan="2">
									           <emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
									    </th>	
									    <%}else if(btnMap.get(menuCode+"-3")!=null || btnMap.get(menuCode+"-4")!=null){ %>	
									    <th >
									           <emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
									    </th>	
									    <%} %>		
									</tr>
								</thead>
						<tbody>
                        <%
                         if (lfwgparamconfigList != null && lfwgparamconfigList.size() > 0)
						 {
							for(int v=0;v<lfwgparamconfigList.size();v++)
							{
								LfWgParamConfig wgparamConfig=lfwgparamconfigList.get(v);
						%>
						<tr>							
						<td class="textalign"><xmp id="paramValue<%=wgparamConfig.getPid() %>"><%=wgparamConfig.getParamValue() %></xmp></td>					
						<td class="textalign"><xmp id="paramName<%=wgparamConfig.getPid() %>"><%=wgparamConfig.getParamName() %></xmp></td>				   
						<td class="textalign">
						   	<%
							 if(!"".equals(wgparamConfig.getMemo())&&wgparamConfig.getMemo()!=null){
								String st = "";
								if(wgparamConfig.getMemo().length()>15)
								{
									st = wgparamConfig.getMemo().substring(0,15)+"...";
								}else
								{
									st = wgparamConfig.getMemo();
								}
							%>
							<a onclick="javascript:modify(this)">
							<label class="memo_label"><xmp id="memo<%=wgparamConfig.getPid() %>"><%=wgparamConfig.getMemo()%></xmp></label>
							<xmp><%=st %></xmp>
							</a> 	<%}else{ %>		<%} %>
													
						</td>	
						 <%if(btnMap.get(menuCode+"-3")!=null) { %>	
						<td><a href="javascript:doEdit('<%=wgparamConfig.getPid() %>')"><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a></td>
						<%} %>
						 <%if(btnMap.get(menuCode+"-4")!=null) { %>	
						<td><a href="javascript:doDel('<%=wgparamConfig.getPid() %>')"><emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/></a></td>	
						<%} %>		
                        </tr>
                       
						<%} }else{%>
					<tr><td colspan="10" align="center"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/></td></tr>
					<%} %>	
						</tbody>
								<tfoot>
							<tr>
								<td colspan="10">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
							</table>
							<div id="wgpconfigReparams" class="hidden"></div>
							</form>
						
		        </div>
				<div id="modify" title="<emp:message key='xtgl_spgl_shlcgl_bz' defVal='备注' fileName='xtgl'/>"  class="modify">
				<div id="msg" class="msg"><xmp></xmp></div>
				</div>
				
				 <div id="configDiv" title="<emp:message key='xtgl_cswh_zdcsgl_xjwgdtcsz' defVal='新建网关动态参数值' fileName='xtgl'/> "  class="configDiv" >
				<table class="configDiv_table">
				<tr><td class="csmc_mh_up_tr_td"></td></tr>
					<tr>
						<td class="csmc_mh_td">
							<emp:message key="xtgl_cswh_zdcsgl_csmc_mh" defVal="参数名称:" fileName="xtgl"/>
						</td>
						<td><input type="text" id="ParamSubName" name="ParamSubName" disabled="disabled" value="<%=lfdefinition!=null?lfdefinition.getParamSubName():""%>" class="input_bd ParamSubName" /></td>
					</tr>
					<tr><td class="csz_mh_up_up_tr_td" colspan="2"></td></tr>				
					<tr><td class="csz_mh_up_tr_td"></td></tr>
					<tr>								
						<td class="csz_mh_td">
							<emp:message key="xtgl_cswh_zdcsgl_csz_mh" defVal="参数值:" fileName="xtgl"/>
						</td>
						<td><input type="text" name="paramValues" id="paramValues" maxlength="20"  class="input_bd paramValues"/><font class="zhu"><emp:message key="xtgl_cswh_zdcsgl_cdbcg" defVal="长度不超过20个字符" fileName="xtgl"/></font></td>
					</tr>
					<tr><td class="cszhy_mh_up_tr_td"></td></tr>	
					<tr>								
						<td class="cszhy_mh_td">
							<emp:message key="xtgl_cswh_zdcsgl_cszhy_mh" defVal="参数值含义:" fileName="xtgl"/>
						</td>
						<td><input type="text" name="paramNames" id="paramNames" maxlength="20"  class="input_bd paramNames"/><font class="zhu"><emp:message key="xtgl_cswh_zdcsgl_cdbcg" defVal="长度不超过20个字符" fileName="xtgl"/></font></td>
					</tr>	
					<tr><td class="bz_mh_up_tr_td"></td></tr>	
					<tr>
					    <td class="bz_mh_td">
					                     <emp:message key="xtgl_spgl_shlcgl_bz_mh" defVal="备注:" fileName="xtgl"/>
					    </td>
					    <td>
					        <textarea id="memo" name="memo" class="input_bd memo"  ></textarea>
					        <input type="hidden" id="dpidh" name="dpidh" value="<%=lfdefinition!=null?lfdefinition.getPid():"" %>"></input>
					    </td>
					</tr>
					<tr><td class="dpidh_down_tr_td"></td></tr>						
			     </table>	
			     <div class="ok_div">
				    <center>
					   <input id="ok" class="btnClass5 mr23" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确  定' fileName='xtgl'/>" onclick="javascript:dook()"/>
				       <input id="sc" onclick="javascript:docancel();" class="btnClass6" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取  消' fileName='xtgl'/>" />
				    </center>
		    	</div>						
	         </div>
			<%-- 内容结束 --%>
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
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
    <script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"
			type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/par_selParamConfig.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
			$(document).ready(function() {
			    getLoginInfo("#wgpconfigReparams");
			    var findresult="<%=findResult%>";
			    if(findresult=="-1")
			    {
			       alert(getJsLocaleMessage("xtgl","xtgl_spgl_mbsp_98"));	
			       return;			       
			    }
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
			    $('#search').click(function(){submitForm();});
			});			
		
		</script>
  </body>
</html>
