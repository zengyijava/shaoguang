<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.entity.selfparam.LfWgParmDefinition"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	List<LfWgParmDefinition> LfWgParmDefinitionList=(List<LfWgParmDefinition>)request.getAttribute("LfWgParamDefinitionList");
	//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
   // Long curId = sysuser.getUserId();
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
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
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/par_selParamDefinition.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/par_selParamDefinition.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
  </head>
  
  <body id="par_selParamDefinition">
      	<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
					<form name="pageForm" action="<%=path %>/par_selParam.htm" method="post" id="pageForm">						
							<div class="buttons pageForm_div" >
								<%if(btnMap.get(menuCode+"-1")!=null) { %>							
								<a id="add" href="javascript:doAdd()"><emp:message key="xtgl_spgl_shlcgl_xj" defVal="新建" fileName="xtgl"/></a>		
								<%} %>						
								<%-- <a href="par_selParam.htm?method=findConfig" >查看值</a>	 --%>
							</div>
							<table id="content">
								<thead>
									<tr>
									    <th>
									           <emp:message key="xtgl_cswh_zdcsgl_csmc" defVal="参数名称" fileName="xtgl"/>
									    </th>										
										<th>
										       <emp:message key="xtgl_cswh_zdcsgl_csx" defVal="参数项" fileName="xtgl"/>
										</th>									
										<th>
										        <emp:message key="xtgl_cswh_zdcsgl_fds" defVal="分段数" fileName="xtgl"/>
										</th>
										<th >
										        <emp:message key="xtgl_cswh_zdcsgl_fdf" defVal="分段符" fileName="xtgl"/>
									    </th>		
									    <th>
									                     <emp:message key="xtgl_spgl_shlcgl_bz" defVal="备注" fileName="xtgl"/>
									    </th>
									    <%if(btnMap.get(menuCode+"-2")!=null) { %>	
									    <th colspan="2">
									                     <emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
									    </th>	
									    <%} else { %>	
									     <th >
									                     <emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
									    </th>	
									    <%} %>			
									</tr>
								</thead>
						<tbody>
                        <%
                         if (LfWgParmDefinitionList != null && LfWgParmDefinitionList.size() > 0)
						 {
							for(int v=0;v<LfWgParmDefinitionList.size();v++)
							{
								LfWgParmDefinition wgparamDefinition=LfWgParmDefinitionList.get(v);
						%>
						<tr>
						<td class="textalign"><xmp id="ParamSubName<%=wgparamDefinition.getPid() %>"><%=wgparamDefinition.getParamSubName()%></xmp></td>					
						<td><xmp id="param<%=wgparamDefinition.getPid()%>"><%=wgparamDefinition.getParam()%></xmp></td>						
						<td><xmp id="ParamSubNum<%=wgparamDefinition.getPid()%>"><%=wgparamDefinition.getParamSubNum() %></xmp></td>					
						<td><xmp id="ParamSubSign<%=wgparamDefinition.getPid()%>"><%=wgparamDefinition.getParamSubSign() %></xmp></td>				   
						
						<td class="textalign">
						 	<%
							 if(!"".equals(wgparamDefinition.getMemo())&&wgparamDefinition.getMemo()!=null){
								String st = "";
								if(wgparamDefinition.getMemo().length()>15)
								{
									st = wgparamDefinition.getMemo().substring(0,15)+"...";
								}else
								{
									st = wgparamDefinition.getMemo();
								}
							%>
							<a onclick="javascript:modify(this)">
							<label class="memo_label"><xmp id="memo<%=wgparamDefinition.getPid() %>"><%=wgparamDefinition.getMemo()%></xmp></label>
							<xmp><%=st %></xmp>
							</a> 	<%}else{ %>		<%} %>
						</td>
						 <%if(btnMap.get(menuCode+"-2")!=null) { %>	
						<td><a  href ="javascript:doEdit(<%= wgparamDefinition.getPid()%>,<%=wgparamDefinition.getParam().substring(5) %>)"><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a></td>
						<%} %>
						<td><a onclick="javascript:location.href='par_selParam.htm?method=findConfig&dpid=<%=wgparamDefinition.getPid() %>'"><emp:message key="xtgl_cswh_zdcsgl_ckz" defVal="查看值" fileName="xtgl"/></a></td>				
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
				<div id="wgpDefinitionparams" class="hidden"></div>
			</form>
						
		  </div>
				<div id="modify" title="<emp:message key='xtgl_spgl_shlcgl_bz' defVal='备注' fileName='xtgl'/>"  class="modify">
				<div id="msg" class="msg"><xmp></xmp></div>
				</div>
					
			 <div id="definitionDiv" title="<emp:message key='xtgl_cswh_zdcsgl_xjwgdtcsdy' defVal='新建网关动态参数定义' fileName='xtgl'/>"  class="definitionDiv" >
				<table class="definitionDiv_table">
				<tr><td class="definitionDiv_table_tr_td"></td></tr>
					<tr>
						<td>
							<emp:message key="xtgl_cswh_zdcsgl_csx_mh" defVal="参数项:" fileName="xtgl"/>
						</td>
						<td>
						    <select name="param" id="param" onchange="javascript:changeparam()"  class="input_bd param">	
						    <%
						    String P_propFile = "SystemGlobals";
							ResourceBundle rb = ResourceBundle.getBundle(P_propFile);
							String depcodethird2p2 = rb.getString("depcodethird2p2");
							if ("false".equals(depcodethird2p2)) {
						    %>					        
							    <option value="2">Param2</option>
							<%
							} 
							%>
							    <option value="3">Param3</option>
							    <option value="4">Param4</option>
							</select>
						</td>
					</tr>
					<tr><td class="definitionDiv_table_tr_td" colspan="2"></td></tr>				
					<tr><td class="definitionDiv_table_tr_td"></td></tr>
					<tr>								
						<td class="definitionDiv_table_td_ver">
							<emp:message key="xtgl_cswh_zdcsgl_fds_mh" defVal="分段数:" fileName="xtgl"/>
						</td>
						<td><input type="text" name="ParamSubNum" id="ParamSubNum" disabled="true" maxlength="20"  class="input_bd ParamSubNum"/></td>
					</tr>
					<tr><td class="definitionDiv_table_tr_td"></td></tr>	
					<tr>								
						<td class="definitionDiv_table_td_ver">
							<emp:message key="xtgl_cswh_zdcsgl_fdf_mh" defVal="分段符:" fileName="xtgl"/>
						</td>
						<td><input type="text" name="ParamSubSign" id="ParamSubSign" value="#" disabled="true" maxlength="20"  class="input_bd ParamSubSign"/></td>
					</tr>	
					<tr><td class="definitionDiv_table_tr_td"></td></tr>	
					<tr>
					    <td class="definitionDiv_table_td_ver">
					                     <emp:message key="xtgl_cswh_zdcsgl_csmc_mh" defVal="参数名称:" fileName="xtgl"/>
					    </td>
					    <td>
					        <input type="text" name="ParamSubName" id="ParamSubName" maxlength="20"  class="input_bd ParamSubName"/><font class="zhu"><emp:message key="xtgl_cswh_zdcsgl_cdbcg" defVal="长度不超过20个字符" fileName="xtgl"/></font>
					    </td>
					</tr>
					<tr><td class="definitionDiv_table_tr_td"></td></tr>	
					<tr>
					    <td class="definitionDiv_table_td_ver">
					                     <emp:message key="xtgl_spgl_shlcgl_bz_mh" defVal="备注:" fileName="xtgl"/>
					    </td>
					    <td>
					        <textarea id="memo" name="memo" class="input_bd memo"  ></textarea>
					    </td>
					</tr>
					<tr><td class="definitionDiv_table_tr_td"></td></tr>	
					
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
		<script type="text/javascript" src="<%=iPath %>/js/par_selParamDefinition.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script type="text/javascript">
			$(document).ready(function() {
			   getLoginInfo("#wgpDefinitionparams");
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
