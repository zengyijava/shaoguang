<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="org.apache.commons.beanutils.DynaBean" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.servmodule.txgl.biz.ViewParams" %>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

	
	@ SuppressWarnings("unchecked")
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
 	List<DynaBean> routeList=(List<DynaBean>)request.getAttribute("enterpList");
	String txglFrame = skin.replace(commonPath, inheritPath);
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("apiBaseMage");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	<%@include file="/common/common.jsp" %>
		<title>API个性配置管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style type="text/css">
			#clientIntName,#InterfaceName,#req_type,#resp_type,#methodNames{
				width: 230px;
			}

		</style>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/wg_apiBaseMulit.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="wg_apiBaseMulit">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<div class="buttons toggleDiv_up_div" >
			<div id="toggleDiv">
			</div>
			<%if(btnMap.get(menuCode+"-1")!=null) { %>
			 <input type="button" class="btnClass4 btnHover toggleDiv_down_input" value="<emp:message key='txgl_apimanage_text_1' defVal='新增方法类型' fileName='mwadmin'/>"  onclick="toAdd()">
			 <%} %>
			</div>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<input type="hidden" id="ecid" name="ecid" value=""/>
			<input type="hidden" id="pathUrl" value="<%=path %>"/>
			<input type="hidden" id="funtype" value=""/>
			<%-- 内容开始 --%>
			<form name="pageForm" action="wg_apiBaseMage.htm?method=find" method="post" id="pageForm">
			<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
			<div id="rContent" class="rContent">
				<div id="addmethod" title="<emp:message key='txgl_apimanage_text_2' defVal='新增方法' fileName='mwadmin'/>" class="addmethod">
				<div class="addmethod_down_div">
					<center>
					<table>
					   <tr>
                      <td align="left" class="ffmcmh_td"><span><emp:message key='txgl_apimanage_text_3' defVal='方法名称：' fileName='mwadmin'/></span></td>
                        <td align="left">
                        <select  id="methodNames" name="methodNames" >
							<option value="single_send"><emp:message key='txgl_apimanage_text_4' defVal='单条发送' fileName='mwadmin'/>(single_send)</option>
						 	<option value="send_single"><emp:message key='txgl_apimanage_text_4' defVal='单条发送' fileName='mwadmin'/>(send_single)</option>
							<option value="batch_send" ><emp:message key='txgl_apimanage_text_5' defVal='相同内容群发' fileName='mwadmin'/>(batch_send)</option>
							<option value="send_batch" ><emp:message key='txgl_apimanage_text_5' defVal='相同内容群发' fileName='mwadmin'/>(send_batch)</option>
							<option value="multi_send" ><emp:message key='txgl_apimanage_text_6' defVal='个性化群发' fileName='mwadmin'/>(multi_send)</option>
							<option value="send_multi" ><emp:message key='txgl_apimanage_text_6' defVal='个性化群发' fileName='mwadmin'/>(send_multi)</option>
							<option value="send_mixed" ><emp:message key='txgl_apimanage_text_6' defVal='个性化群发' fileName='mwadmin'/>(send_mixed)</option>
							<option value="template_send" ><emp:message key='txgl_apimanage_text_7' defVal='模板发送' fileName='mwadmin'/>(template_send)</option>
							<option value="send_template" ><emp:message key='txgl_apimanage_text_7' defVal='模板发送' fileName='mwadmin'/>(send_template)</option>
							<option value="get_mo"><emp:message key='txgl_apimanage_text_8' defVal='获取上行' fileName='mwadmin'/>(get_mo)</option>
							<option value="get_rpt"><emp:message key='txgl_apimanage_text_9' defVal='获取状态报告' fileName='mwadmin'/>(get_rpt)</option>
							<option value="get_balance"><emp:message key='txgl_apimanage_text_10' defVal='查询余额' fileName='mwadmin'/>(get_balance)</option>
							<option value="MO"><emp:message key='txgl_apimanage_text_11' defVal='推送上行' fileName='mwadmin'/>(MO)</option>
							<option value="RPT"><emp:message key='txgl_apimanage_text_12' defVal='推送状态报告' fileName='mwadmin'/>(RPT)</option>
							</select>
                        </td><td></td>
                      </tr>
                      <tr class="addmethod_table_tr"><td colspan="2"></td></tr>
  					  <tr>
   				 		 <td align="left"><emp:message key='txgl_apimanage_text_13' defVal='客户接口名称：' fileName='mwadmin'/></td>   				 		 
   						 <td align="left">
   						 	<input type="text" name="clientIntName" id="clientIntName" maxlength="21"  class="input_bd"/>
   	   					 </td>
   	   					 <td><font color="gray;">&nbsp;&nbsp;<emp:message key='txgl_apimanage_text_14' defVal='输入客户接口中文名' fileName='mwadmin'/></font></td>  	   					 	
 					  </tr>
 					   <tr class="addmethod_table_tr"><td colspan="2"></td></tr>
  					  <tr>
   				 		 <td align="left"><emp:message key='txgl_apimanage_text_15' defVal='客户接口：' fileName='mwadmin'/></td>   				 		 
   						 <td align="left">
   						 	<input type="text" name="InterfaceName" id="InterfaceName" maxlength="21"  class="input_bd"/>
   	   					 </td> 
   	   					 <td><font color="gray;">&nbsp;&nbsp;<emp:message key='txgl_apimanage_text_16' defVal='例如：' fileName='mwadmin'/>csingle_send</font></td>  	   					 	
 					  </tr>
 					    <tr class="addmethod_table_tr"><td colspan="2"></td></tr>
  					  <tr>
   				 		 <td align="left"><emp:message key='txgl_apimanage_text_17' defVal='请求格式：' fileName='mwadmin'/></td>   				 		 
   						 <td align="left">
   						 	<select  id="req_type" name="req_type" >
														<option value="1" >XML</option>
														<option value="2" >JSON</option>
														<option value="4">URLENCODE</option>
							</select>
   	   					 </td>
   	   					 <td></td>   	   					 	
 					  </tr>
 					   <tr class="addmethod_table_tr"><td colspan="2"></td></tr>
  					  <tr>
   				 		 <td align="left"><emp:message key='txgl_apimanage_text_18' defVal='回应格式：' fileName='mwadmin'/></td>   				 		 
   						 <td align="left">
   						 <select  id="resp_type" name="resp_type" >
							<option value="0"><emp:message key='txgl_apimanage_text_19' defVal='无' fileName='mwadmin'/></option>
							<option value="1" >XML</option>
							<option value="2" >JSON</option>
							<option value="4">URLENCODE</option>
						 </select>
   	   					 </td> 
   	   					 <td></td>  	   					 	
 					  </tr>
					</table>
					</center>
				</div>
				<div class="blok_div">
				    <center>
		    		<input id="blok" class="btnClass5 mr23" type="button" value="<emp:message key='txgl_apimanage_text_20' defVal='提  交' fileName='mwadmin'/>" onclick="javascript:save()"/>
				    <input id="blc" onclick="javascript:btcancel();" class="btnClass6" type="button" value="<emp:message key='txgl_apimanage_text_21' defVal='取  消' fileName='mwadmin'/>" />
				    <br/>
				    </center>
		    	</div>
			</div>
			
				<div id="addtype" title="<emp:message key='txgl_apimanage_text_22' defVal='新增方法类型' fileName='mwadmin'/>" class="addtype">
				<div class="addtype_down_div">
					<center>
					<table class="qymc_table">
						<tr>
   				 		 <td align="left"><emp:message key='txgl_apimanage_text_23' defVal='企业名称：' fileName='mwadmin'/></td>   				 		 
   						 <td align="left">
   						<input type="text"  name="corp_name" id="corp_name" maxlength="30"  class="input_bd corp_name" disabled="disabled">
   						<input type="button" class="btnClass2 btnHover xzqy_input" value="<emp:message key='txgl_apimanage_text_24' defVal='选择企业' fileName='mwadmin'/>"  onclick="choice()">
   	   					 </td>
   	   					 </tr> 
   	   					 <tr>
   				 		 <td align="left"><emp:message key='txgl_apimanage_text_25' defVal='企业编号：' fileName='mwadmin'/></td>   				 		 
   						 <td align="left">
   							<input type="text"  name="corp_code" id="corp_code" maxlength="30"  class="input_bd corp_code" disabled="disabled">
   	   					 </td>
   	   					 </tr> 
					   <tr>
   				 		 <td align="left"><emp:message key='txgl_apimanage_text_33' defVal='方法类型：' fileName='mwadmin'/></td>   				 		 
   						 <td align="left">
   						<input type="text"  name="functiontype" id="functiontype" maxlength="30"  class="input_bd functiontype">&nbsp;&nbsp;<emp:message key='txgl_apimanage_text_26' defVal='系统自动加上' fileName='mwadmin'/>:cstd/
   	   					 </td>
   	   					 </tr> 
   	   					 <tr>  				 		 				 	
 					  </tr>
 					   <tr>  				 		 				 	
 					  </tr>
  					  <tr>
   				 		 <td align="left" colspan="2">1.&nbsp;&nbsp;<emp:message key='txgl_apimanage_text_27' defVal='方法类型用于区分不同企业，建议输入企业字母简称+序号。' fileName='mwadmin'/>&nbsp;<emp:message key='txgl_apimanage_text_28' defVal='如：京东 JD001' fileName='mwadmin'/></td>
 					  </tr>
 					   <tr>
   				 		 <td align="left" colspan="2">2.&nbsp;&nbsp;<emp:message key='txgl_apimanage_text_29' defVal='方法类型保存后，不允许修改。' fileName='mwadmin'/></td>   				 		 				 	
 					  </tr>
					</table>
					</center>
				</div>
				<div class="typesave_div">
				    <center>
		    		<input id="typesave" class="btnClass5 mr23" type="button" value="<emp:message key='txgl_apimanage_text_30' defVal='保  存' fileName='mwadmin'/>" onclick="javascript:savetype()" />
				    <input id="blc" onclick="javascript:canceltype();" class="btnClass6" type="button" value="<emp:message key='txgl_apimanage_text_21' defVal='取  消' fileName='mwadmin'/>" />
				    <br/>
				    </center>
		    	</div>
			</div>
			
					<div id="condition">
						<table>
									<%
									LinkedHashMap<String, String> conditionMap =(LinkedHashMap<String,String>)request.getAttribute("conditionMap");
									%>
										<tr>
											<td >
												<emp:message key='txgl_apimanage_text_31' defVal='企业编号：' fileName='mwadmin'/>
											</td>
											<td>
											<input type="text"  class="etccode" name="etccode" id="etccode" size="20" onkeyup="value=value.replace(/\D/g,'')" 
											value="<%=null==conditionMap.get("etccode")?"":conditionMap.get("etccode") %>"/>
											</td>
											<td >
												<emp:message key='txgl_apimanage_text_32' defVal='企业名称：' fileName='mwadmin'/>
											</td>
											<td>
											<input type="text"  class="etcname" name="etcname" id="etcname" size="20" 
											value="<%=null==conditionMap.get("etcname")?"":conditionMap.get("etcname") %>"/>
											</td>
											<td >
												<emp:message key='txgl_apimanage_text_33' defVal='方法类型：' fileName='mwadmin'/>
											</td>
											<td>
											<input type="text"  class="funtype" name="funtype" id="funtype" size="20" 
											value="<%=null==conditionMap.get("funtype")?"":conditionMap.get("funtype") %>"/>
											</td>
											<td >
												<emp:message key='txgl_apimanage_text_34' defVal='请求格式：' fileName='mwadmin'/>
											</td>
											<td>
												<label>
													<select  class="reqfmt"  name="reqfmt" id="reqfmt" >
				                                    <option value="" selected="selected"><emp:message key='txgl_apimanage_text_35' defVal='全部' fileName='mwadmin'/></option>
				                                    <option value="1" <%if(null!=conditionMap.get("reqfmt")&&"1".equals(conditionMap.get("reqfmt"))){%> selected="selected" <%}%>>XML</option>
				                                    <option value="2" <%if(null!=conditionMap.get("reqfmt")&&"2".equals(conditionMap.get("reqfmt"))){%> selected="selected" <%}%>>JSON</option>
				                                    <option value="4" <%if(null!=conditionMap.get("reqfmt")&&"4".equals(conditionMap.get("reqfmt"))){%> selected="selected" <%}%>>URLENCODE</option>
				                                    <option value="3"<%if(null!=conditionMap.get("reqfmt")&&"3".equals(conditionMap.get("reqfmt"))){%> selected="selected" <%}%> >XML,JSON</option>
				                                    <option value="5" <%if(null!=conditionMap.get("reqfmt")&&"5".equals(conditionMap.get("reqfmt"))){%> selected="selected" <%}%>>XML,URLENCODE</option>
				                                    <option value="6" <%if(null!=conditionMap.get("reqfmt")&&"6".equals(conditionMap.get("reqfmt"))){%> selected="selected" <%}%>>JSON,URLENCODE</option>
				                                    <option value="7" <%if(null!=conditionMap.get("reqfmt")&&"7".equals(conditionMap.get("reqfmt"))){%> selected="selected" <%}%>>XML,JSON,URLENCODE</option>
													</select>
												</label>
											</td>
											<td class="tdSer"><center><a id="search"></a></center></td>
										</tr>
										<tr>

											<td >
												<emp:message key='txgl_apimanage_text_36' defVal='回应格式：' fileName='mwadmin'/>
											</td>
											<td>
												<label>
													<select  class="respfmt"  name="respfmt" id="respfmt" >
	                                    			<option value="" selected="selected"><emp:message key='txgl_apimanage_text_35' defVal='全部' fileName='mwadmin'/></option>
				                                    <option value="0" <%if(null!=conditionMap.get("respfmt")&&"0".equals(conditionMap.get("respfmt"))){%> selected="selected" <%}%>><emp:message key='text_null' defVal='无' fileName='common'/></option>
				                                    <option value="1" <%if(null!=conditionMap.get("respfmt")&&"1".equals(conditionMap.get("respfmt"))){%> selected="selected" <%}%>>XML</option>
				                                    <option value="2" <%if(null!=conditionMap.get("respfmt")&&"2".equals(conditionMap.get("respfmt"))){%> selected="selected" <%}%>>JSON</option>
				                                    <option value="3" <%if(null!=conditionMap.get("respfmt")&&"3".equals(conditionMap.get("respfmt"))){%> selected="selected" <%}%>>XML,JSON</option>
													</select>
												</label>
											</td>
											<td >
												<emp:message key='txgl_apimanage_text_37' defVal='创建时间：' fileName='mwadmin'/>
											</td>
											<td>
												<input type="text" name="startdate" id="startdate"
												class="Wdate startdate" readonly="readonly"
												onclick="stime()"
												value="<%=conditionMap.get("startdate") != null ? conditionMap.get("startdate") : ""%>"/>
											</td>
											<td >
												&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key='txgl_apimanage_text_38' defVal='至：' fileName='mwadmin'/>
											</td>
											<td>
												<input type="text" name="enddate" id="enddate"
												class="Wdate enddate" readonly="readonly"
												onclick="rtime()"
												value="<%=conditionMap.get("enddate") != null ? conditionMap.get("enddate") : ""%>"/>
											</td>
											<td colspan="3"></td>
										</tr>
							</table>
						</div>
						<table id="content" class="content_table">
							<thead>
								<tr>
									<th><emp:message key='txgl_apimanage_text_39' defVal='企业名称' fileName='mwadmin'/></th>
									<th class="fflx_th">
										<emp:message key='txgl_apimanage_text_40' defVal='方法类型' fileName='mwadmin'/>
									</th>
									<th>
										<emp:message key='txgl_apimanage_text_41' defVal='请求格式' fileName='mwadmin'/>
									</th>
									<th>
										<emp:message key='txgl_apimanage_text_42' defVal='回应格式' fileName='mwadmin'/>
									</th> 
									<th class="ffs_th">
										<emp:message key='txgl_apimanage_text_43' defVal='方法数' fileName='mwadmin'/>
									</th>
									<th class="qyzt_th">
										<emp:message key='txgl_apimanage_text_44' defVal='企业状态' fileName='mwadmin'/>
									</th>
									<th class="cjsj_th">
										<emp:message key='txgl_apimanage_text_45' defVal='创建时间' fileName='mwadmin'/>
									</th>
									<th class="xgsj_th">
										<emp:message key='txgl_apimanage_text_46' defVal='修改时间' fileName='mwadmin'/>
									</th>
									<th class="cz_th">
										<emp:message key='txgl_apimanage_text_47' defVal='操作' fileName='mwadmin'/>
									</th>
								</tr>
							</thead>
							<tbody>
							<%

			                	if(routeList != null&&routeList.size()>0)                       		
			                	{       
			                		 String keyId;
									for(int i=0;i<routeList.size();i++ )
									{
										DynaBean gt=routeList.get(i);
			                 %>
								<tr>
								<td>
									<%out.print(gt.get("corp_name")+"("+gt.get("ecid")+")"); %>
									</td>
									
									<td>
									<%=gt.get("funtype")==null?"":gt.get("funtype").toString()%>
									</td>
									<td>
									<%if(gt.get("reqfmt")!=null){ 
									String reqfmt=gt.get("reqfmt").toString();
									%>
									<%if("1".equals(reqfmt)){
										out.print("XML");
									}else if("2".equals(reqfmt)){
										out.print("JSON");
									}else if("4".equals(reqfmt)){
										out.print("URLENCODE");
									}else if("3".equals(reqfmt)){
										out.print("XML,JSON");
									}else if("5".equals(reqfmt)){
										out.print("XML,URLENCODE");
									}else if("6".equals(reqfmt)){
										out.print("JSON,URLENCODE");
									}else if("7".equals(reqfmt)){
										out.print("XML,JSON,URLENCODE");
									}else{
										out.print("-");
									} 
									}else{
										out.print("-");
									}%>
									</td>
									<td>
									<%if(gt.get("respfmt")!=null){ 
									String respfmt=gt.get("respfmt").toString();
									%>
									<%if("1".equals(respfmt)){
										out.print("XML");
									}else if("2".equals(respfmt)){
										out.print("JSON");
									}else if("4".equals(respfmt)){
										out.print("URLENCODE");
									}else if("3".equals(respfmt)){
										out.print("XML,JSON");
									}else if("5".equals(respfmt)){
										out.print("XML,URLENCODE");
									}else if("6".equals(respfmt)){
										out.print("JSON,URLENCODE");
									}else if("7".equals(respfmt)){
										out.print("XML,JSON,URLENCODE");
									}else{
										out.print("-");
									}
									}else{
										out.print("-");
									}
										%>
									
									</td>
									<td>
									<%=gt.get("bookcnt")==null?"":gt.get("bookcnt").toString() %>	
									</td>
									<td class="status_td">
									
									<%
									String st="";
									if(gt.get("status")!=null){
									if("0".equals(gt.get("status").toString())){
										st = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_48",request);
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_49",request));
									}else if("1".equals(gt.get("status").toString())){
										st=MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_49",request);
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_48",request));
									}else{
										out.print("-");
									} 
									}else{
										out.print("-");
									}
									%>
									</td> 
									<td >
									<%=gt.get("createtm")!=null?df.format(Timestamp.valueOf(gt.get("createtm").toString())):"-"%>	
									</td>
									<td >
									<%=gt.get("modiytm")!=null?df.format(Timestamp.valueOf(gt.get("modiytm").toString())):"-"%>
									</td>
									<td >
									<%if(btnMap.get(menuCode+"-1")!=null) { %>
									<a id="add" onclick="javascript:doadd('<%=gt.get("ecid")%>','<%=gt.get("funtype")%>')"><emp:message key='txgl_apimanage_text_50' defVal='新增方法' fileName='mwadmin'/></a>&nbsp;&nbsp;&nbsp;&nbsp;
									<%} %>
									<%if(btnMap.get(menuCode+"-2")!=null) { %>
									<a id="work" onclick="javascript:setStaut('<%=gt.get("ecid")%>','<%=gt.get("status")%>')"><%=st%></a>&nbsp;&nbsp;&nbsp;&nbsp;
									<%} %>
									<%if(btnMap.get(menuCode+"-0")!=null) { %>
									<a id="add" onclick="javascript:funList('<%=gt.get("ecid")%>','<%=gt.get("funtype")%>')"><emp:message key='txgl_apimanage_text_51' defVal='方法详情' fileName='mwadmin'/></a>
									<%} %>
									</td>

								</tr>
								<%
				                	}
				                } else{%>
				                <tr>
								<td colspan="10">
                                      <emp:message key='txgl_apimanage_text_52' defVal='无记录' fileName='mwadmin'/>
								</td>
							    </tr>
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
			</div>
			<%-- 内容结束 --%>
			<div id="tmplDiv" title="<emp:message key='txgl_apimanage_text_53' defVal='API接口方法列表' fileName='mwadmin'/>" class="tmplDiv">
				<iframe id="tempFrame" name="tempFrame" class="tempFrame" marginwidth="0" scrolling="no" frameborder="no" src ="<%=commonPath%>/common/blank.jsp " ></iframe>
			</div>
			<div id="custDiv" title="<emp:message key='txgl_apimanage_text_54' defVal='新增企业信息' fileName='mwadmin'/>" class="custDiv">
				<iframe id="custFrame" name="custFrame" class="<%="zh_HK".equals(empLangName)?"custFrame_1":"custFrame_2"%>"  marginwidth="0" scrolling="yes" frameborder="no" src ="<%=commonPath%>/common/blank.jsp " ></iframe>
			</div>
			
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</form>

		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/apicommon.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		var findresult="<%=(String)request.getAttribute("findresult")%>";
		var total=<%=pageInfo.getTotalPage()%>;// 总页数
		var pageIndex=<%=pageInfo.getPageIndex()%>;// 当前页数
		var pageSize=<%=pageInfo.getPageSize()%>;// 每页记录数
		var totalRec=<%=pageInfo.getTotalRec()%>;// 总记录数
		var inheritPath="<%=inheritPath%>";
		//请求删除路由信息地址
		</script>
		<script type="text/javascript" src="<%=iPath %>/js/wg_baseapi.js" ></script>
	</body>
</html>
