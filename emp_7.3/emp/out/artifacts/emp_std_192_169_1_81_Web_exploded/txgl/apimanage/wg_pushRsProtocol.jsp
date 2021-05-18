<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.servmodule.txgl.biz.ViewParams" %>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.GwBasePara"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.GwPushRsProtocol"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

	//@ SuppressWarnings("unchecked")
	//Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//@ SuppressWarnings("unchecked")
   // String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
   // String txglFrame = skin.replace(commonPath, inheritPath);
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@include file="/common/common.jsp" %>
		<title>API推送用户回应管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
			<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
			<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
			<link href="<%=commonPath%>/frame/frame3.0/skin/default/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
			<link href="<%=commonPath%>/frame/frame3.0/skin/default/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
			<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
			<link href="<%=commonPath%>/frame/frame3.0/skin/default/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
			<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
			<link rel="stylesheet" href="<%=commonPath%>/frame/frame3.0/skin/default/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css">
			<%if(StaticValue.ZH_HK.equals(empLangName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<%}%>
			
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/wg_pushRsProtocol.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/wg_pushRsProtocol.css?V=<%=StaticValue.getJspImpVersion() %>"/>
			
	</head>
	<body id="wg_pushRsProtocol">
		<div id="container" class="container">
			<input type="hidden" id="ecid" name="ecid" value=""/>
			<input type="hidden" id="pathUrl" value="<%=path %>"/>
			<input type="hidden" id="funtype" value=""/>
			<%-- 内容开始 --%>
			<form name="pageForm" action="wg_pushRsProtocol.htm?method=find" method="post" id="pageForm">
			<input type="hidden" id="lguserid" name="lguserid" value="abc"/>
			<div id="rContent" class="rContent">	
				<div class="buttons toggleDiv_up_div"  >
						<div id="toggleDiv"></div>
						<a id="add"><emp:message key='txgl_apimanage_text_99' defVal='新建' fileName='mwadmin'/></a>
					</div>
					<div id="getloginUser" class="getloginUser">
					</div>
					<div id="condition">
						<table>
									<%
									LinkedHashMap<String, String> conditionMap =(LinkedHashMap<String,String>)request.getAttribute("conditionMap");
									%>
										<tr>
											<td >
												<emp:message key='txgl_apimanage_text_36' defVal='回应格式：' fileName='mwadmin'/>
											</td>
											<td>
												<label>
													<select  class="crspfmt"  name="crspfmt" id="crspfmt" >
				                                    <option value="" selected="selected"><emp:message key='txgl_apimanage_text_35' defVal='全部' fileName='mwadmin'/></option>
				                                    <option value="1" <%if(null!=conditionMap.get("crspfmt")&&"1".equals(conditionMap.get("crspfmt"))){%> selected="selected" <%}%>>XML</option>
				                                    <option value="2" <%if(null!=conditionMap.get("crspfmt")&&"2".equals(conditionMap.get("crspfmt"))){%> selected="selected" <%}%>>JSON</option>
				                                    <option value="3" <%if(null!=conditionMap.get("crspfmt")&&"4".equals(conditionMap.get("crspfmt"))){%> selected="selected" <%}%>>URLENCODE</option>
													</select>
												</label>
											</td>
											<td >
												<emp:message key='txgl_apimanage_text_119' defVal='回应状态：' fileName='mwadmin'/>
											</td>
											<td>
											<label>
													<select  class="rspStatus"  name="rspStatus" id="rspStatus" >
	                                    			<option value="" selected="selected"><emp:message key='txgl_apimanage_text_35' defVal='全部' fileName='mwadmin'/></option>
				                                    <option value="2" <%if(null!=conditionMap.get("rspStatus")&&"2".equals(conditionMap.get("rspStatus"))){%> selected="selected" <%}%>><emp:message key='txgl_apimanage_text_61' defVal='全成功回应' fileName='mwadmin'/></option>
				                                    <option value="3" <%if(null!=conditionMap.get("rspStatus")&&"3".equals(conditionMap.get("rspStatus"))){%> selected="selected" <%}%>><emp:message key='txgl_apimanage_text_62' defVal='全失败回应' fileName='mwadmin'/></option>
				                                    <option value="4" <%if(null!=conditionMap.get("rspStatus")&&"4".equals(conditionMap.get("rspStatus"))){%> selected="selected" <%}%>><emp:message key='txgl_apimanage_text_100' defVal='部分成功部分失败回应' fileName='mwadmin'/></option>
				                                    <option value="5" <%if(null!=conditionMap.get("rspStatus")&&"5".equals(conditionMap.get("rspStatus"))){%> selected="selected" <%}%>><emp:message key='txgl_apimanage_text_64' defVal='回应详细信息' fileName='mwadmin'/></option>
													</select>
												</label>
											</td>
											<td >
												<emp:message key='txgl_apimanage_text_101' defVal='返回命令：' fileName='mwadmin'/>
											</td>
											<td>
												<label>
													<select  class="rspCmd"  name="rspCmd" id="rspCmd" >
	                                    			<option value="" selected="selected"><emp:message key='txgl_apimanage_text_35' defVal='全部' fileName='mwadmin'/></option>
				                                    <option value="1" <%if(null!=conditionMap.get("rspCmd")&&"1".equals(conditionMap.get("rspCmd"))){%> selected="selected" <%}%>><emp:message key='txgl_apimanage_text_102' defVal='MO回应' fileName='mwadmin'/></option>
				                                    <option value="2" <%if(null!=conditionMap.get("rspCmd")&&"2".equals(conditionMap.get("rspCmd"))){%> selected="selected" <%}%>><emp:message key='txgl_apimanage_text_103' defVal='RPT回应' fileName='mwadmin'/></option>
													</select>
												</label>
											</td>
											<td class="tdSer"><center><a id="search"></a></center></td>
										</tr>
										<tr>
										<td >
												<emp:message key='txgl_apimanage_text_104' defVal='客户参数名：' fileName='mwadmin'/>
											</td>
											<td>
											<input type="text"  class="cargName" name="cargName" id="cargName" size="20"  maxlength="50"
											value="<%=null==conditionMap.get("cargName")?"":conditionMap.get("cargName") %>"/>
											</td>
											<td >
												<emp:message key='txgl_apimanage_text_105' defVal='客户字段值：' fileName='mwadmin'/>
											</td>
											<td>
											<input type="text"  class="cargValue" name="cargValue" id="cargValue" size="20"  maxlength="50" 
											value="<%=null==conditionMap.get("cargValue")?"":conditionMap.get("cargValue") %>"/>
											</td>
											<td colspan="3"></td>
										</tr>
							</table>
						</div>
						<table id="content" class="content_table">
							<thead>
							<tr>
									<th>
										<emp:message key='txgl_apimanage_text_106' defVal='企业' fileName='mwadmin'/>
									</th>
									<th >
										<emp:message key='txgl_apimanage_text_107' defVal='返回命令' fileName='mwadmin'/>
									</th>
									<th >
										<emp:message key='txgl_apimanage_text_108' defVal='客户参数名' fileName='mwadmin'/>
									</th> 
									<th >
										<emp:message key='txgl_apimanage_text_109' defVal='回应状态' fileName='mwadmin'/>
									</th>
									<th >
										<emp:message key='txgl_apimanage_text_110' defVal='回应格式' fileName='mwadmin'/>
									</th>
									<th >
										<emp:message key='txgl_apimanage_text_111' defVal='客户字段值' fileName='mwadmin'/>
									</th>
									<th>
										<emp:message key='txgl_apimanage_text_112' defVal='修改' fileName='mwadmin'/>
									</th>
									<th>
										<emp:message key='txgl_apimanage_text_83' defVal='删除' fileName='mwadmin'/>
									</th>
								</tr>
							</thead>
							<tbody>
							<%
								@ SuppressWarnings("unchecked")
			                 	List<GwPushRsProtocol> gwPushRsProtocolList=(List<GwPushRsProtocol>)request.getAttribute("gwPushRsProtocolList");
			                	if(gwPushRsProtocolList != null&&gwPushRsProtocolList.size()>0)                       		
			                	{       
			                		 String keyId;
									for(int i=0;i<gwPushRsProtocolList.size();i++ )
									{
										GwPushRsProtocol gprp=gwPushRsProtocolList.get(i);
			                 %>
								<tr>
									<td>
									<%=(String)request.getAttribute("corpInfo")%>
									</td>
									<td>
								      <%
								        String rspCmd="";
								        if(gprp.getRspCmd()==1)
								        {
								        	//out.print("MO回应");
								        	rspCmd = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_102",request);
								        }else if(gprp.getRspCmd()==2)
								        {
								        	//out.print("RPT回应");
								        	rspCmd = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_103",request);
								        }
								       %>
								    <label id="lbRspCmd<%=gprp.getId()%>"><%=rspCmd%></label>
								    <input type="hidden"   name="HidRspCmd<%=gprp.getId()%>" id="HidRspCmd<%=gprp.getId()%>" value="<%=gprp.getRspCmd() %>"/>
									</td>
									<td>
									<label id="lbCargName<%=gprp.getId()%>"><%=gprp.getCargName()%></label>
									</td>
									<td>
									<%
									   String rspStatus="";
									if(gprp.getRspStatus()==2){
										rspStatus = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_61",request);
										//out.print("全成功回应");
									}else if(gprp.getRspStatus()==3){
										//out.print("全失败回应");
										rspStatus = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_62",request);
									}else if(gprp.getRspStatus()==4){
										//out.print("部分成功部分失败回应");
										rspStatus = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_100",request);
									}else if(gprp.getRspStatus()==5){
										//out.print("回应详细信息");
										rspStatus = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_64",request);
									}
									 %>	
									 <label id="lbRspStatus<%=gprp.getId()%>"><%=rspStatus%></label>	
									  <input type="hidden"   name="HidRspStatus<%=gprp.getId()%>" id="HidRspStatus<%=gprp.getId()%>" value="<%=gprp.getRspStatus() %>"/>
									</td>
									<td>
									<%
									String crspfmt="";
									if(gprp.getCrspfmt()==0){
										//out.print("未知");
										crspfmt = MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_116",request);
									}else if(gprp.getCrspfmt()==1){
										//out.print("xml");
										crspfmt="xml";
									}else if(gprp.getCrspfmt()==2){
										//out.print("json");
										crspfmt="json";
									}else if(gprp.getCrspfmt()==4){
										//out.print("urlencode");
										crspfmt="urlencode";
									}
									 %>
									 <label id="lbCrspfmt<%=gprp.getId()%>"><%=crspfmt%></label>	
									</td> 
									<td>
									<label id="lbCargValue<%=gprp.getId()%>"><%=gprp.getCargValue()%></label>	
									</td>
									<td >
									<a href="javascript:update(<%=gprp.getId()%>)"><emp:message key='txgl_apimanage_text_112' defVal='修改' fileName='mwadmin'/></a>
									</td>
									<td >
									<a href="javascript:del('<%=gprp.getId()%>','<%=gprp.getRspCmd()%>','<%=gprp.getCargName()%>','<%=gprp.getRspStatus() %>','<%=gprp.getEcid()%>')">
									<emp:message key='txgl_apimanage_text_83' defVal='删除' fileName='mwadmin'/></a>
									</td>
								</tr>
								<%}
				                } else{%>
				                <tr>
								<td colspan="8">
                                       <emp:message key='txgl_apimanage_text_52' defVal='无记录' fileName='mwadmin'/>
								</td>
							    </tr>
				                <%} %>
								
						</tbody>
						<tfoot>
							<tr>
								<td colspan="8">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
			</div>
			
			
			   <div id="addDiv" class="addDiv">
				<table class="addDiv_table">
					<tr class="addDiv_table_tr">
						<td class="addDiv_table_tr_td1"  >
							<emp:message key='txgl_apimanage_text_101' defVal='返回命令：' fileName='mwadmin'/>
						</td>
						<td class="addDiv_table_tr_td2">
						<select name="newRspCmd" id="newRspCmd" class="input_bd newRspCmd"  >
								<option value="1"><emp:message key='txgl_apimanage_text_102' defVal='MO回应' fileName='mwadmin'/></option>
								<option value="2"><emp:message key='txgl_apimanage_text_103' defVal='RPT回应' fileName='mwadmin'/></option>
							</select>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					
					<tr class="addDiv_table_tr">
						<td class="addDiv_table_tr_td1"  >
							<emp:message key='txgl_apimanage_text_104' defVal='客户参数名：' fileName='mwadmin'/>
						</td>
						<td class="addDiv_table_tr_td2">
							<input type="text"   class="input_bd newCargName" name="newCargName" id="newCargName"  maxlength="25"/>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					
					<tr class="addDiv_table_tr">
						<td class="hyzt_td">
							<emp:message key='txgl_apimanage_text_119' defVal='回应状态：' fileName='mwadmin'/>
						</td>
						<td class="addDiv_table_tr_td2">
						<select name="newRspStatus" id="newRspStatus" class="input_bd newRspStatus"  >
								<option value="2"><emp:message key='txgl_apimanage_text_61' defVal='全成功回应' fileName='mwadmin'/></option>
								<option value="3"><emp:message key='txgl_apimanage_text_62' defVal='全失败回应' fileName='mwadmin'/></option>
								<option value="4"><emp:message key='txgl_apimanage_text_100' defVal='部分成功部分失败回应' fileName='mwadmin'/></option>
								<option value="5"><emp:message key='txgl_apimanage_text_64' defVal='回应详细信息' fileName='mwadmin'/></option>
							</select>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					
					<tr class="addDiv_table_tr">
						<td class="addDiv_table_tr_td1" >
							<emp:message key='txgl_apimanage_text_111' defVal='客户字段值：' fileName='mwadmin'/>
						</td>
						<td class="addDiv_table_tr_td2">
							<input type="text"   class="input_bd newCargValue" name="newCargValue" id="newCargValue"  maxlength="32" value="<emp:message key='txgl_apimanage_text_113' defVal='多个值中间请用,分隔' fileName='mwadmin'/>"/>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					<tr>
					<td colspan="2" class="addsubmit_td">
					<input name="addsubmit" id="addsubmit" class="btnClass5 mr23" type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'/>" onclick="javascript: addPushRsProtocol();"/>
					<input name="addcancel" id="addcancel" class="btnClass6" type="button" value="<emp:message key='common_cancel' defVal='取消' fileName='common'/>" onclick="javascript:doCancel();"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td>
					</tr>
				</table>
			</div>
			
			
			
			    <div id="editDiv" class="editDiv">
				<table class="editDiv_table ">
					<tr class="editDiv_table_tr">
						<td class="editDiv_table_tr_td1"  >
							<emp:message key='txgl_apimanage_text_101' defVal='返回命令：' fileName='mwadmin'/>
						</td>
						<td class="editDiv_table_tr_td2">
						<input type="hidden"   name="idEdit" id="idEdit" value=""/>
							<input type="text" disabled="disabled"  class="input_bd rspCmdEdit" name="rspCmdEdit" id="rspCmdEdit"  maxlength="32"/>
							<input type="hidden"   name="rspCmdHid" id="rspCmdHid" value=""/>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					
					<tr class="editDiv_table_tr">
						<td class="editDiv_table_tr_td1"  >
							<emp:message key='txgl_apimanage_text_104' defVal='客户参数名：' fileName='mwadmin'/>
						</td>
						<td class="editDiv_table_tr_td2">
							<input type="text"  disabled="disabled"  class="input_bd cargNameEdit" name="cargNameEdit" id="cargNameEdit"  maxlength="25"/>
							<input type="hidden"   name="cargNameHid" id="cargNameHid" value=""/>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					
					<tr class="editDiv_table_tr">
						<td align="right" class="editDiv_hyzt_td">
							<emp:message key='txgl_apimanage_text_119' defVal='回应状态：' fileName='mwadmin'/>
						</td>
						<td class="editDiv_table_tr_td2">
							<input type="text"  disabled="disabled"  class="input_bd rspStatusEdit" name="rspStatusEdit" id="rspStatusEdit"  maxlength="32"/>
							<input type="hidden"   name="rspStatusHid" id="rspStatusHid" value=""/>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					
					<tr class="editDiv_table_tr">
						<td class="editDiv_table_tr_td1"  >
							<emp:message key='txgl_apimanage_text_105' defVal='客户字段值：' fileName='mwadmin'/>
						</td>
						<td class="editDiv_table_tr_td2">
							<input type="text"   class="input_bd cargValueEdit" name="cargValueEdit" id="cargValueEdit"  maxlength="32"/>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					<tr>
					<td colspan="2" class="editsubmit_td">
					<input name="editsubmit" id="editsubmit" class="btnClass5 mr23" type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'/>" onclick="javascript: editPushRsProtocol();"/>
					<input name="editcancel" id="editcancel" class="btnClass6" type="button" value="<emp:message key='common_cancel' defVal='取消' fileName='common'/>" onclick="javascript:doCancelEdit();"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td>
					</tr>
				</table>
			</div>
			<%-- 内容结束 --%>
			
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker3/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/txgl/apimanage/js/pushRsProtocol.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
				<script type="text/javascript">
		$(document).ready(function() {
			var findresult="<%=(String)request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
                //alert("加载页面失败,请检查网络是否正常!");
                alert(getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_1"));
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
			
			$("#addDiv").dialog({
				autoOpen: false,
			    width:500,
			    height:318,
				/*新增推送用户回应协议*/
			    title:getJsLocaleMessage("txgl","txgl_js_baseApi_13"),
			    modal:true,
			    resizable:false
			 });
			$("#editDiv").dialog({
				autoOpen: false,
			    width:500,
			    height:360,
				/*修改推送用户回应协议*/
			    title:getJsLocaleMessage("txgl","txgl_js_baseApi_14"),
			    modal:true,
			    resizable:false
			 });
			$('#add').click(function(){
				$("#addDiv").dialog("open");
			});
		});
		
	function rtime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#enddate").attr("value");
	if(v.length != 0)
	{
	    var year = v.substring(0,4);
		var mon = v.substring(5,7);
		var day = 31;
		if (mon != "12")
		{
		    mon = String(parseInt(mon,10)+1);
		    if (mon.length == 1)
		    {
		        mon = "0"+mon;
		    }
		    switch(mon){
		    case "01":day = 31;break;
		    case "02":day = 28;break;
		    case "03":day = 31;break;
		    case "04":day = 30;break;
		    case "05":day = 31;break;
		    case "06":day = 30;break;
		    case "07":day = 31;break;
		    case "08":day = 31;break;
		    case "09":day = 30;break;
		    case "10":day = 31;break;
		    case "11":day = 30;break;
		    }
		}
		else
		{
		    year = String((parseInt(year,10)+1));
		    mon = "01";
		}
		max = year+"-"+mon+"-"+day+" 23:59:59"
	}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});

}

function stime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#startdate").attr("value");
    var min = "1900-01-01 00:00:00";
	if(v.length != 0)
	{
	    max = v;
	    var year = v.substring(0,4);
		var mon = v.substring(5,7);
		if (mon != "01")
		{
		    mon = String(parseInt(mon,10)-1);
		    if (mon.length == 1)
		    {
		        mon = "0"+mon;
		    }
		}
		else
		{
		    year = String((parseInt(year,10)-1));
		    mon = "12";
		}
		min = year+"-"+mon+"-01 00:00:00"
	}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

}
	</script>
	</body>
</html>
