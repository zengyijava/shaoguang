<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.rms.servmodule.constant.ServerInof" %>
<%@ page import="com.montnets.emp.rms.vo.LfTemplateVo" %>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("template");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	List<LfTemplateVo> mmsList = (List<LfTemplateVo>)request.getAttribute("mmsList");
	//@ SuppressWarnings("unchecked")
	//List<LfSysuser> sysList = (List<LfSysuser>)session.getAttribute("sysList");
	LfTemplateVo mt = (LfTemplateVo)request.getAttribute("mmsVo");
	int rState = mt.getIsPass()==null?-2:mt.getIsPass();
	long state = mt.getTmState()==null?-2:mt.getTmState();
	int auditStatus = mt.getAuditstatus()==null?-2:mt.getAuditstatus();
	int submitstatus = mt.getSubmitstatus() == null?-2:mt.getSubmitstatus();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	//服务器名称
	String serverName = ServerInof.getServerName();
    String flowId = request.getParameter("flowId");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<script src="<%=inheritPath%>/ueditor/ueditor.config.js" type="text/javascript"></script>
		<script src="<%=inheritPath%>/ueditor/ueditor.all.js" type="text/javascript"></script>
		<script src="<%=inheritPath%>/ueditor/cust_params.js" type="text/javascript"></script>
	
		<style type="text/css">
			#phone_background {
			    background: url(rms/ueditor/themes/default/images/phone.png) no-repeat;
			    width: 232px;
			    height: 465px;
			    margin-left: 5px;
			    margin-top:8px;
			}
			#cust_preview_outer {
				width: 210px;
				height: 380px;
				margin-top: 50px;
			    margin-left: 10px;
				display: inline-block;
				vertical-align: top;
				overflow: hidden;
			}
			#cust_preview {
				overflow-y: scroll;
				width: 226px;
			    height: 380px;
				overflow-x: hidden;
			}
			#cust_preview div,#cust_preview p{
				width: 210px;
				word-wrap: break-word;
			    margin: 0;
			}
			#cust_preview video{
			    width: 206px;
			    height: 150px;
			}
			#cust_preview img{
			   width: 205px;
			}
			
		</style>
	</head>
	<body>
		<input type="hidden" id="pathUrl" value="<%=path %>" />
	    <input type="hidden" id="ipathUrl" value="<%=inheritPath%>"/>
	    <input type="hidden" id="commonPath" value="<%=commonPath %>"/>
	    <input type="hidden" id="iPath" value="<%=iPath %>" />
		<input type="hidden" id="templType" value="2">
		<div id="container" class="container">
			<%-- header开始 --%>
		<%--<%=ViewParams.getPosition(menuCode) %>
		--%><%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
		<div id="modify" title="<emp:message key="ydcx_cxyy_mbbj_shyj" defVal="审核意见" fileName="ydcx"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
		</div>
		<%-- 预览弹层  --%>
		<div id="myView" style="display: none;">
		<div id="phone_background">
			<div id="cust_preview_outer">
				<div id="cust_preview"></div>
			</div>
		</div>
		</div> 
			<%-- 内容开始 --%>
			<%
				if(btnMap.get(menuCode+"-0")!=null) {
			%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="mbgl_mytemplate.htm?method=find" method="post"
					id="pageForm">
					<div style="display:none" id="hiddenValueDiv"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%
							if(btnMap.get(menuCode+"-1")!=null) {
						%>
						<a id="add" onclick="doAdd()"><emp:message key="ydcx_cxyy_common_btn_4" defVal="新建" fileName="ydcx"></emp:message></a>
							<%if(StaticValue.ZH_HK.equals(langName)){%>
								<a id="upload" onclick="javascript:$('#modelTms').dialog('open')"><emp:message key="ydcx_cxyy_mbbj_dr" defVal="导入" fileName="ydcx"></emp:message></a>
							<%}else{%>
								<a id="upload" onclick="javascript:$('#modelTms').dialog('open')"><emp:message key="ydcx_cxyy_mbbj_dr" defVal="导入" fileName="ydcx"></emp:message></a>
							<%}%>
						<%
							}
						%>
						<%
							if(btnMap.get(menuCode+"-2")!=null) {
						%>
						<a id="delete" onclick="javascript:delAll('<%=path %>')"><emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message></a>
						<%
							}
						%>
					</div>
					<div id="getloginUser" style="padding:5px;display:none;">
					</div>
					<div id="condition">
						<table>
						<tbody>
							<tr>
								<td align="left">
									<%-- <emp:message key="ydcx_cxyy_mbbj_mbbh_mh" defVal="模板编号：" fileName="ydcx"></emp:message> --%>
									<emp:message key="rms_fxapp_fsmx_tempid2" defVal="模板ID" fileName="rms"/>
								</td>
								<td >
									<input type="text" name="tmCode" 
										id="tmCode" value="<%=mt.getSptemplid()==null?"":mt.getSptemplid()%>" style="width:178px"  />
								</td>
								<td align="left">
									<%-- <emp:message key="ydcx_cxyy_mbbj_mbmc_mh" defVal="模板名称：" fileName="ydcx"></emp:message> --%>
									<emp:message key="rms_fxapp_tempchoose_fxtopic" defVal="富信主题" fileName="rms"/>
								</td>
								<td >
									<input type="text" name="theme" 
										id="tmName" value="<%=mt.getTmName()==null?"":mt.getTmName()%>" style="width:178px"  />
								</td>
								<%-- <td>
									<emp:message key="ydcx_cxyy_mbbj_empspzt_mh" defVal="EMP审批状态：" fileName="ydcx"></emp:message>
								</td>
								<td>
									<select id="rState" name="rState" style="width:178px">
											<option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
											<option value="-1" <%=rState+1==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_wsp" defVal="未审批" fileName="ydcx"></emp:message></option>
											<option value="0" <%=rState+0==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_wxsp" defVal="无需审批" fileName="ydcx"></emp:message></option>
											<option value="1" <%=rState-1==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_sptg" defVal="审批通过" fileName="ydcx"></emp:message></option>
											<option value="2" <%=rState-2==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_spwtg" defVal="审批未通过" fileName="ydcx"></emp:message></option>
									</select>
								</td> --%>
								<td></td>
								<td></td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
							<tr>
						    	<td>
						    		<emp:message key="ydcx_cxyy_mbbj_mbzt_mh" defVal="模板状态：" fileName="ydcx"></emp:message>
						    	</td>
							    <td>
							    	<select id="state" name="state" Style="width:178px">
									    <option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
										<option value="1" <%=state-1==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_qy" defVal="启用" fileName="ydcx"></emp:message></option>
										<option value="0" <%=state+0==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_jy" defVal="禁用" fileName="ydcx"></emp:message></option>
										<option value="2" <%=state-2==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_cg" defVal="草稿" fileName="ydcx"></emp:message></option>
									</select>
								</td>
								<td>
									<emp:message key="ydcx_cxyy_mbbj_mblx_mh" defVal="模板类型：" fileName="ydcx"></emp:message>
								</td>
								<td>
									<select id="dsFlag" name="dsFlag" Style="width:178px">
										<option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
										<option value="0" <%=mt.getDsflag()!=null && mt.getDsflag()==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_tyjtmb" defVal="通用静态模板" fileName="ydcx"></emp:message></option>
										<option value="1" <%=mt.getDsflag()!=null && mt.getDsflag()==1?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_tydtmb" defVal="通用动态模板" fileName="ydcx"></emp:message></option>
									</select>
								</td>
								<td>
									<emp:message key="ydcx_cxyy_mbbj_yystjzt_mh" defVal="运营商提交状态：" fileName="ydcx"></emp:message>
								</td>
								<td>
									<select id="submitstatus" name="submitstatus" style="width:178px">
										<option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
										<option value="0" <%=submitstatus==0?"selected":""%>><emp:message key="ydcx_cxyy_mbbj_wtj" defVal="未提交" fileName="ydcx"></emp:message></option>
										<option value="1" <%=submitstatus==1?"selected":""%>><emp:message key="ydcx_cxyy_common_text_11" defVal="成功" fileName="ydcx"></emp:message></option>
										<option value="2" <%=submitstatus==2?"selected":""%>><emp:message key="ydcx_cxyy_common_text_12" defVal="失败" fileName="ydcx"></emp:message></option>
									</select>
								</td>
								<td></td>
						   </tr>
						   <tr>
						   		<td>
						   			<emp:message key="rms_fxapp_dwtjbb_cjsj" defVal="创建时间" fileName="rms"/>
						   		</td>
								<td>
									<input type="text" value="<%=mt.getAddStartm()==null?"":mt.getAddStartm()%>" id="submitSartTime" name="submitSartTime" style="cursor: pointer; width: 178px;background-color: white;" class="Wdate" readonly="readonly" onclick="stime()">
								</td>
								<td >
									<emp:message key="rms_fxapp_degreerep_to" defVal="至" fileName="rms"/>
                                </td>
                                <td>
               						<input type="text" value="<%=mt.getAddEndtm()==null?"":mt.getAddEndtm()%>" id="submitEndTime" name="submitEndTime" style="cursor: pointer; width: 178px;background-color: white;" class="Wdate" readonly="readonly" onclick="rtime()">
                                </td>
                                <td>
                                	<emp:message key="ydcx_cxyy_mbbj_yysspzt_mh" defVal="运营商审批状态：" fileName="ydcx"></emp:message>
                                </td>
								<td>
									<select id="auditStatus" name="auditStatus" style="width:178px">
										<option value=""><emp:message key="ydcx_cxyy_common_text_16" defVal="全部" fileName="ydcx"></emp:message></option>
										<option value="-1" <%=auditStatus==-1?"selected":""%>><emp:message key="rms_fxapp_myscene_noapproval" defVal="未审批" fileName="rms"/></option>
										<option value="3" <%=auditStatus==3?"selected":""%>><emp:message key="rms_fxapp_myscene_approvaling" defVal="审批中" fileName="rms"/></option>
										<option value="4" <%=auditStatus==4?"selected":""%>><emp:message key="rms_fxapp_myscene_disabled" defVal="已禁用" fileName="rms"/></option>
										<option value="1" <%=auditStatus==1?"selected":""%>><emp:message key="rms_fxapp_myscene_approvaled" defVal="审批通过" fileName="rms"/></option>
										<option value="2" <%=auditStatus==2?"selected":""%>><emp:message key="rms_fxapp_myscene_nopass" defVal="审批未通过" fileName="rms"/></option>
									</select>
								</td>
								<td></td>
							</tr>
							</tbody>
						</table>
					</div>
					<table id="content" style="width: 100%;">
						<thead>
				 	 		<tr>
								<th width="1.9%">
									<input type="checkbox" name="checkall" id="checkall" onclick="checkAlls(this,'checklist')" />
								</th>
								<th width="6%">
									<emp:message key="ydcx_cxyy_mbbj_mbid" defVal="模板ID" fileName="ydcx"></emp:message>
								</th>
								<%-- <th width="5%">
									<emp:message key="ydcx_cxyy_mbbj_mbbh" defVal="模板编号" fileName="ydcx"></emp:message>
								</th> --%>
								<th width="12%">
									<%-- <emp:message key="ydcx_cxyy_mbbj_mbmc" defVal="模板名称" fileName="ydcx"></emp:message> --%>
									<emp:message key="rms_fxapp_tempchoose_fxtopic" defVal="富信主题" fileName="rms"/>
								</th>
								<th width="5%">
									<%-- <emp:message key="ydcx_cxyy_mbbj_mbmc" defVal="模板名称" fileName="ydcx"></emp:message> --%>
									<emp:message key="rms_fxapp_mbgl_rlkb" defVal="容量(KB)" fileName="rms"/>
								</th>
								<th width="5%">
									<%-- <emp:message key="ydcx_cxyy_mbbj_mbmc" defVal="模板名称" fileName="ydcx"></emp:message> --%>
									<emp:message key="rms_fxapp_mbgl_jfdw" defVal="计费档位(档)" fileName="rms"/>
								</th>
								<th width="7%">
								    <emp:message key="ydcx_cxyy_mbbj_mblx" defVal="模板类型" fileName="ydcx"></emp:message>
								</th>
								<th width="4.6%">
									<emp:message key="ydcx_cxyy_mbbj_mbnr" defVal="模板内容" fileName="ydcx"></emp:message>
								</th>
								<th width="4%">
									<%-- <emp:message key="ydcx_cxyy_mbbj_cjrxm" defVal="创建人姓名" fileName="ydcx"></emp:message> --%>
									<emp:message key="rms_fxapp_dwtjbb_cjr" defVal="创建人" fileName="rms"/>
								</th>
								<th width="5%">
									<emp:message key="ydcx_cxyy_mbbj_ssjg" defVal="所属机构" fileName="ydcx"></emp:message>
								</th>
								<th width="10%">
									<emp:message key="ydcx_cxyy_mbbj_cjrq" defVal="创建日期" fileName="ydcx"></emp:message>
								</th>
								<%-- <th width="8.5%">
								    <emp:message key="ydcx_cxyy_mbbj_empspzt" defVal="EMP审批状态" fileName="ydcx"></emp:message>
								</th> --%>
								<th width="8%">
								    <emp:message key="ydcx_cxyy_mbbj_yystjzt" defVal="运营商提交状态" fileName="ydcx"></emp:message>
								</th>
								<th width="8%">
								    <emp:message key="ydcx_cxyy_mbbj_yysspzt" defVal="运营商审批状态" fileName="ydcx"></emp:message>
								</th>
								<th width="4.5%">
								    <emp:message key="ydcx_cxyy_mbbj_mbzt" defVal="模板状态" fileName="ydcx"></emp:message>
								</th>
								<th width="22%" colspan="4" >
									<emp:message key="ydcx_cxyy_common_text_14" defVal="操作" fileName="ydcx"></emp:message>
								</th>
							</tr>
						</thead>
						<tbody>
							<%
								if (mmsList != null && mmsList.size() > 0)
								{
									for (LfTemplateVo mv : mmsList)
								{
									boolean isEdit=false;
									if(mv.getShareType()==0){
									isEdit=true;
								}
							%>
							<tr>
								<td>
									<% if(btnMap.get(menuCode+"-2")!=null&&isEdit){%>
									<input type="checkbox" name="checklist" value="<%=mv.getTmid() %>" />
									<% }else{%>
									<input type="checkbox" disabled="disabled""/>
									<%} %>
								</td>
								<td>
									<%
									    if(!"".equals(mv.getSptemplid()) && mv.getSptemplid() != null && (mv.getSptemplid()-0 != 0)){
								    %>
								    <a onclick="javascript:detail(this,1)" title="<%=mv.getSptemplid() %>">
								  		<label style="display:none"><xmp><%=mv.getSptemplid()%></xmp></label>
									  	<xmp><%=mv.getSptemplid()%></xmp>
									</a>
								   <% }else{
									    	out.print("-");
								    }
									%>
									</td>
									<%-- <td>
										<%=mv.getTmCode()==null?"-": mv.getTmCode()%>
									</td> --%>
									<td class="textalign">
										<a onclick="javascript:detail(this,2)" title="<%=mv.getTmName().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;") %>">
									  		<label style="display:none"><xmp><%=mv.getTmName()%></xmp></label>
											<xmp><%=mv.getTmName().length()>10?mv.getTmName().substring(0,10)+"...":mv.getTmName() %></xmp>
										</a> 
									</td>
									<td>
										<%=mv.getDegreeSize()%>
									</td>
									<td>
										<%=mv.getDegree() %>
									</td>
									<td>
									   	<%
											if(mv.getDsflag()-1==0)
											{
												out.print(StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_mbgl_tydtmb", request), "通用动态模版"));
												//out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_tydtmk",request));
											}else if(mv.getDsflag()-0==0)
											{
												out.print(StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_mbgl_tyjtmb", request), "通用静态模板"));
												//out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_tyjtmk",request));
											}else if(mv.getDsflag()-2==0)
											{
												//out.print("智能抓取模块");
												out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_znzqmk",request));
											}else if(mv.getDsflag()-3==0)
											{
												//out.print("移动财务模块");
												out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_ydcwmk",request));
											}
										%>
									</td>
									<td>
										<%
										 if (mv.getTmMsg() != null)
										 {
										%>
										<a  urlMsg="<%=mv.getTmMsg() %>" tmName="<%=StringEscapeUtils.escapeHtml(mv.getTmName()) %>"  onclick="doPreview(this)"> <emp:message key="ydcx_cxyy_common_text_6" defVal="预览" fileName="ydcx"></emp:message></a>
										<%} %>
									</td>
									<td class="textalign">
										<%if(mv.getUserState() !=null && mv.getUserState() ==2)
										    {
										      //out.print(mv.getName()+"<font color='red'>(已注销)</font>");
										      out.print(mv.getName()+"<font color='red'>("+MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_yzx",request)+")</font>");
										    }
										    else
										    {
										       out.print(mv.getName());
										    }
									  	%>
									</td>
									<td>
										<a onclick="javascript:detail(this,3)" title="<%=mv.getDepName().replace("&","&amp;").replace("<","&lt;").replace(">","&gt;") %>">
									  		<label style="display:none"><xmp><%=mv.getDepName()%></xmp></label>
											<xmp><%=mv.getDepName().length()>5?mv.getDepName().substring(0,5)+"...":mv.getDepName() %></xmp>
										</a> 
									</td>
									<td>
										<%=df.format(mv.getAddtime()) %>
									</td>
									<td class="ztalign">
										<%
											if(mv.getSubmitstatus() == 1){
												//out.print("成功");
												out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_common_text_11",request));
											}else if(mv.getSubmitstatus() == 2){
												//out.print("失败["+mv.getErrorcode()+"]");
												out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_common_text_12",request)+"["+mv.getErrorcode()+"]");
										    }else if(mv.getSubmitstatus() == 0){
										    	//out.print("未提交");
										    	out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_wtj",request));
										    }else{
										    	out.print("-");
										    }
										%>
									</td>
									<td class="ztalign">
										<%
										String s = "";
										//boolean flag = false;
										int intFlag=-2;
										if (mv.getAuditstatus()==-1)
											 {
											    // s = "未审批";
											     s = StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms","rms_fxapp_myscene_noapproval",request), "未审批");
											    // s = MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_wsp",request);
											     //flag = true;
											    // intFlag=-1;
										     }else if(mv.getAuditstatus()== 0)
											 {
											     //s = "无需审批";
											     s = StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms","rms_fxapp_pubscene_wxsp",request), "无需审批");
											     //s = MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_wxsp",request);
										     }else if(mv.getAuditstatus()== 1)
											 {
											    // s = "审批通过";
											     s = StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms","rms_fxapp_myscene_approvaled",request), "审批通过");
											    // s = MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_sptg",request);
											     //flag = true;
										     }else if(mv.getAuditstatus()== 2)
										     {
										    	 //s = "审批未通过";
										    	 s = StringUtils.defaultIfEmpty(MessageUtils.extractMessage("ydcx","rms_fxapp_myscene_nopass",request), "审批未通过");
										    	 //flag = true;
										    	 //intFlag=2;
										     }
										     else if(mv.getAuditstatus()== 3)
										     {
										    	 //s = "审核中";
										    	 //s = "审批中";
										    	 s = StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms","rms_fxapp_myscene_approvaling",request), "审批中");
										    	 //flag = true;
										     }
										     else if(mv.getAuditstatus()== 4)
										     {
										    	// s = "已禁用";
										    	 s = StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms","rms_fxapp_myscene_disabled",request), "已禁用");
										    	 //flag = true;
										     }
										%>
										<%
											if(intFlag==1||intFlag==2){
												%>
													<a href="javascript:opentmpAudmsg('<%=mv.getTmid() %>')"><%=s%></a>
												<%
											}else if(intFlag==-1){
												%>
													<a href="javascript:openReviewFlow('<%=mv.getTmid() %>','<%=mv.getUserId()%>','4')"><%=s%></a>
												<%
											}else{
												out.print(s);
											}
										%>
									</td>
									<%-- <td class="ztalign">
									    <%
										    if(mv.getTmState()==0L||mv.getTmState()==1L){
										    	 if(mv.getAuditstatus()-1 == 0){
													//out.print("审批通过");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_sptg",request));
												}else if(mv.getAuditstatus()-2 == 0){
													//out.print("审批未通过");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_spwtg",request));
												}else if(mv.getAuditstatus()- 0 == 0){
													//out.print("未审批");
													out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_wsp",request));
												}else{
													out.print("-");
												}
											}else{
											    out.print("-");
											}
										%>
									</td> --%>
									<td class="ztalign">
										<%if(mv.getTmState()-1 == 0){
											//out.print("启用");
											out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_qy",request));
										}else if(mv.getTmState() == 0){
											//out.print("禁用");
											out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_jy",request));
										}else{
											//out.print("草稿");
											out.print(MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_cg",request));
										}
										%>
									</td>
									<%if(btnMap.get(menuCode+"-1")!=null) { %>
									<td>
										<%
										if(isEdit){
											if(mv.getTmState()-1 == 0){
												%>
												<a onclick="javascript:chState('<%=mv.getTmid() %>','0')"><emp:message key="ydcx_cxyy_mbbj_jy" defVal="禁用" fileName="ydcx"></emp:message></a>
												<%
											}else if(mv.getTmState() == 0){
												%>
												<a onclick="javascript:chState('<%=mv.getTmid() %>','1')"><emp:message key="ydcx_cxyy_mbbj_qy" defVal="启用" fileName="ydcx"></emp:message></a>
												<%
											}else if(btnMap.get(menuCode+"-1")!=null){%>
												<a onclick="javascript:doCopy('<%=mv.getTmid() %>','<%=mv.getTmMsg() %>','edit')"><emp:message key="ydcx_cxyy_common_text_7" defVal="编辑" fileName="ydcx"></emp:message></a>
											<%}else{%>
											-
											<%}
											}else{ %>
											-
											<%} %>
									</td>
									<td>
										<a onclick="javascript:doCopy('<%=mv.getTmid() %>','<%=mv.getTmMsg() %>','copy')"><emp:message key="ydcx_cxyy_mbbj_fz" defVal="复制" fileName="ydcx"></emp:message></a>
									</td>
									<%}%>
									<%-- <td>
										<%if(btnMap.get(menuCode+"-1")!=null&&isEdit){%>
										<a href="javascript:showShareTmp(<%=mv.getTmid() %>,'<%=mv.getTmName() %>',<%=mv.getUserId() %>)"><emp:message key="ydcx_cxyy_mbbj_gx" defVal="共享" fileName="ydcx"></emp:message></a>
										<% } else{%>
										<emp:message key="ydcx_cxyy_mbbj_gx" defVal="共享" fileName="ydcx"></emp:message>
										<% } %>
									</td> --%>
									<td>
										<%if(btnMap.get(menuCode+"-2")!=null&&isEdit) { %>
										<a href="javascript:del('<%=path %>',<%=mv.getTmid() %>)"><emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message></a>
										<%} else{%>
										<emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message>
										<%} %>
									</td>
									<td>
										<%if(isEdit){
											if(mv.getTmState() == 0){ %>
										    -
										<%} else if(mv.getIsPass()== 0) { %>
										  <a onclick="javascript:doExport('<%=mv.getTmMsg() %>')"><emp:message key="ydcx_cxyy_mbbj_dcmb" defVal="导出模板" fileName="ydcx"></emp:message></a>
										<%} else if(mv.getIsPass() == 1) {%>
										      <a onclick="javascript:doExport('<%=mv.getTmMsg() %>')"><emp:message key="ydcx_cxyy_mbbj_dcmb" defVal="导出模板" fileName="ydcx"></emp:message></a>
										<%} else{ %>
										    -
										<%}}else{ %>
										-
										<%} %>
									</td>
								</tr>
								<%
									}
								}else{
									%>
									<tr><td colspan="18"><emp:message key="ydcx_cxyy_common_text_1" defVal="无记录" fileName="ydcx"></emp:message></td></tr>
								<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="18">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
				</form>
			</div>
			<%} %>
			<div id="modelTms" class="model" style="display: none;width:395px;">
				<div class="main" style="width:365px;">
					<div style="padding:6px; width:340px;">
						<div id="upTms">
							<p>
							<span style="width:70px;float:left;"><emp:message key="ydcx_cxyy_mbbj_xzwj_mh" defVal="选择文件：" fileName="ydcx"></emp:message>
							</span>
							<input style="width:138px;float:left;" type="textfield" id="filepath" readonly>
							<span style="width:10px;float:left;">&nbsp;&nbsp;&nbsp;&nbsp;</span>
							<span>
								<span style="position:relative;float:left;display:block;width:80px;height:18px;border:1px solid #999;background: #eee;text-align:center;line-height:18px;">
									<emp:message key="ydcx_cxyy_mbbj_xzwj" defVal="上传文件" fileName="ydcx"></emp:message>
									<input style="position:absolute;left:0;top:0;width:82px;height:20px;opacity:0;filter:alpha(opacity=0);" type="file" id="chooseRms" name="chooseRms" 
											onchange="document.getElementById('filepath').value=this.value;document.getElementById('filepath').title=this.value;"/>			
								</span>
							</span>
							</p>
							<br/>
							
							<br/>
							<%-- <p style="margin-top:5px; color:blue;"><emp:message key="ydcx_cxyy_mbbj_cxwjgsw" defVal="富信文件格式为：rms" fileName="ydcx"></emp:message></p> --%>
							<p style="margin-top:5px; color:blue;"><emp:message key="rms_fxapp_myscene_fxformat" defVal="富信文件格式为：rms" fileName="rms"/></p> 
							<p style="float: right;padding-top:10px;">
							<input type="button" id="uploadTms" value="<emp:message key="ydcx_cxyy_mbbj_ljsc" defVal="立即上传" fileName="ydcx"></emp:message>" onclick="javascript:doUp()" class="btnClass2"/></p>
						</div>
						<div id="curTms" style="display:none">															</div>
					</div>
				</div>
				<div class="foot">
					<div class="foot-right"></div>
				</div>
			</div>
			<div id="detail" style="padding:5px;width:300px;height:160px;display:none">
				<table width="100%">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td style='word-break: break-all;'>
								<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>
							</td>
						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
						</tr>
					</thead>
				</table>
			</div>
			
			<%-- 共享模板 DIV --%>
			<div id="shareTmpDiv" title="<emp:message key="ydcx_cxyy_mbbj_mbgx" defVal="模板共享" fileName="ydcx"></emp:message>" style="display: none;">
				<center>
					<iframe id="flowFrame" name="flowFrame" src=""  attrid="" tmpname="" style="width:510px;height:410px;border: 0;" marginwidth="0" scrolling="no" frameborder="no"></iframe>
				</center>
				<table>
					<tr>
						<td style="width:510px;" align="center">
							<input type="button"  value="<emp:message key="ydcx_cxyy_common_btn_7" defVal="确定" fileName="ydcx"></emp:message>" id="updateShareTemp" class="btnClass5 mr23" onclick="javascript:updateShareTemp();" />
							<input type="button"  value="<emp:message key="ydcx_cxyy_common_btn_16" defVal="取消" fileName="ydcx"></emp:message> " class="btnClass6" onclick="javascript:closeShare();" />
							<br/>
						</td>
					</tr>
				</table>
			</div>
				
			<%-- 富信信息详情--%>
			<div id="mmsdetailinfo" title="<emp:message key="ydcx_cxyy_mbbj_xxxx" defVal="详细信息" fileName="ydcx"></emp:message>" style="display:none;overflow: auto;">
				<div style="width: 100%;padding:15px 0;display:none;" id="recordTableDiv" align="center">
					<table width="95%" id="recordTable"  style='text-align:center'>
					</table>
				</div>
				<div style="width: 95%;padding-bottom:15px;padding-top:10px;display:none;font-size: 12px;color: blue;padding-left: 25px;" id="nextrecordmgs" align="left">
				</div>
			</div>	
				
				
			<div id="reviewflowinfo" title="<emp:message key="ydcx_cxyy_mbbj_dsp" defVal="待审批" fileName="ydcx"></emp:message>" style="display:none;overflow: auto;">
				<div style="width: 100%;padding:15px 0;display:none;" id="reviewTableDiv" align="center">
					<table width="95%" id="reviewTable"  style='text-align:center'>
					</table>
				</div>
				<div style="width: 95%;padding-bottom:15px;padding-top:10px;display:none;font-size: 12px;color: blue;padding-left: 25px;" id="nextreviewmgs" align="left">
				</div>
			</div>	
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<script>
			var serverName = "<%=serverName%>";
		</script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/mmsTemplate.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script type="text/javascript">
			    $(document).ready(function() {
			    	$('#myView').dialog({
						autoOpen: false,
						width:240,
					    height:510
					});
			    
			    
					var findresult="<%=request.getAttribute("findresult")%>";
				    if(findresult != null && findresult !="" && findresult=="-1")
				    {
				       //alert("加载页面失败，请检查网络是否正常！");	
				       alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_jzyesb"));	
				       return;			       
				    }
				    getLoginInfo("#hiddenValueDiv");
					initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
					$('#search').click(function(){submitForm();});
			    });
			    function stime(){
				    var max = "2099-12-31 23:59:59";
				    var v = $("#submitEndTime").attr("value");
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
					WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max,enableInputMask:false});
				
				};
				function rtime(){
				    var max = "2099-12-31 23:59:59";
				    var v = $("#submitSartTime").attr("value");
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
				
				};
				
 			//预览
			function doPreview(obj)
			{	
				  var msg = $(obj).attr("urlMsg");
				  var tmName = $(obj).attr("tmName");
				  // tmName = encodeURI(tmName); 
				   var pathUrl = $("#pathUrl").val();
				    $.post(pathUrl+"/mbgl_mytemplate.htm?method=getTmMsg",{tmUrl:msg,tmName:tmName},function(result){
					if (result != null && result != "null" && result != "")
					{
						 document.getElementById("cust_preview").innerHTML = result;
						 $("#myView").dialog('option','title','预览');
						 $("#myView").dialog("open");
					}
					else
					{
			             alert(getJsLocaleMessage("rms","rms_myscene_nofile"));
					}
				});
			}	
		//编辑
		function doCopy(tmId,tmUrl,opType){
		 	 var pathUrl = $("#pathUrl").val();
   		     window.location.href=pathUrl+"/mbgl_mytemplate.htm?method=doCopy&tmId="+tmId+"&opType="+opType+"&tmUrl="+tmUrl;
		}
		
		//导入富信模板
		function doUp(){
			    var tms = $.trim($("#chooseRms").val());
				if (tms == ""){
					alert(getJsLocaleMessage("rms","rms_myscene_uploadfile"));
					//alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_qxzyscdcxwj"));
					return ;
				}else {
					tms = tms.substring(tms.lastIndexOf(".")+1,tms.length).toUpperCase();
			    	if(tms != "RMS"){
			    		 alert(getJsLocaleMessage("rms","rms_myscene_unsupportfmt"));
			    		//alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_bzcdcxgs"));
				        return ;
			    	}
			    }
			   var pathUrl = $("#pathUrl").val();
			   var lguserid= $("#lguserid").val();
			   var lgcorpcode= $("#lgcorpcode").val();
				   $.ajaxFileUpload ({ 
				    url:pathUrl+"/mbgl_mytemplate.htm?method=importRms&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid, //处理上传文件的服务端 
				    secureuri:false, //是否启用安全提交，默认为false
				    fileElementId:'chooseRms', //与页面处理代码中file相对应的ID值
				    dataType: 'text', //返回数据类型:text，xml，json，html,scritp,jsonp五种
				    success: function (result) { 
				    	if(result == "true"){
				    		var path = '<%=path%>';
			 				window.location.href = path+"/mbgl_mytemplate.htm?method=find";
				    	}else{
				        	alert(getJsLocaleMessage("rms","rms_mbgl_importfail")+result);
				        	 
				        }
				    }
				});
			}
		</script>
	</body>
</html>
