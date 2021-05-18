<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="org.apache.commons.beanutils.DynaBean" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import= "java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String depNam = request.getParameter("depNam");
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<DynaBean> recordList = (List<DynaBean>)request.getAttribute("recordList");
	@ SuppressWarnings("unchecked")
	List<DynaBean> busList = (List<DynaBean>)request.getAttribute("busList");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String,String> conditionMap = (LinkedHashMap<String,String>) request.getAttribute("conditionMap");
	
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, LinkedHashMap> tailList = (LinkedHashMap<String, LinkedHashMap>) request.getAttribute("tailList");
	//是否包含子机构
	String isContainsSun=(String)request.getAttribute("isContainsSun");
	//按钮权限处理
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String reTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(reTitle);
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	//需要的基本参数
	String userid = request.getParameter("userid");
	String userName = request.getParameter("userName");
	@ SuppressWarnings("unchecked")
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<html>
	<head><%@include file="/common/common.jsp"%>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/smsTailManage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
	</head>

	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=ViewParams.getPosition(empLangName,menuCode) %>

			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="<%=path %>/ydyw_postTailMgr.htm" method="post"
					id="pageForm">
			<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
			<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
			<%-- 选中id值 --%>
			<input type="hidden" id="selectid" name="selectid" value=""/>
			<%-- 修改单条记录时候需要记录的id--%>
			<input type="hidden" id="updateid" name="updateid" value=""/>
			
			<select style="display:none" id='rightSelectTemp'></select>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1") != null){%>
						<a id="add" href="javascript:showAddSmsTmp('<emp:message key="ydyw_ywgl_ywtwgl_text_1" fileName="ydyw" defVal="添加业务贴尾内容"></emp:message>')"><emp:message key="common_establish" fileName="common" defVal="新建"></emp:message></a>
						<%} %>
						<%if(btnMap.get(menuCode+"-3") != null){%>
						<a id="delete" href="javascript:deleteSelect()"><emp:message key="btn_sc" fileName="common_btn_5" defVal="删除"></emp:message></a>
						<%} %>
					</div>
					<input type="hidden" id="pathUrl" value="<%=path %>"/>
					<div id="condition">
						<table>
								<tbody>
								<tr>
									<td>
										<emp:message key="ydyw_ywgl_ywtwgl_text_2" fileName="ydyw" defVal="贴尾名称："></emp:message>
									</td>
									<td>
									<input type="text" id="tailname" name="tailname" maxlength="25" value="<%=null==conditionMap.get("tailname")?"":conditionMap.get("tailname") %>" style="width: 154px;">						
									</td>
									<td>
										<emp:message key="ydyw_ywgl_ywtwgl_text_6" fileName="ydyw" defVal="适用业务："></emp:message>
									</td>
									<td>
										<input type="text" id="buss" name="buss" value="<%=null==conditionMap.get("buss")?"":conditionMap.get("buss") %>"  style="width: 154px;">
									</td>
									
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_26" defVal="机构：" fileName="ydyw"></emp:message>
									</td>
									<td class="condi_f_l">											
									  		<div style="width:180px;">	
									  		
									  		 <input type="hidden" id="deptid" name="deptid" value="<%=conditionMap.get("deptid")==null?"":conditionMap.get("deptid")%>"/> 		
									  		<input type="text" class="treeInput" id="depNam" name="depNam" value="<%=depNam==null?MessageUtils.extractMessage("common","common_pleaseSelect",request):depNam%>"
									  			onclick="javascript:showMenu();"  readonly style="width:133px;cursor: pointer;"/>&nbsp;
											</div>														
											
											<div id="dropMenu" >
												<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>	
												<div style="margin-top: 3px;margin-right:10px;text-align:center">
												 <input type="checkbox" id="isContainsSun" name="isContainsSun" <%if(isContainsSun.equals("1")){%> checked="checked" <%}%> value="1" style="width:15px;height:15px;"/><emp:message key="ydyw_ywgl_ywmbpz_text_3" defVal="是否包含子机构" fileName="ydyw"></emp:message>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" style=""/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key="common_clean" defVal="清空" fileName="common"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep3();" style=""/>
												</div>	
												<ul  id="dropdownMenu" class="tree"></ul>	
											</div>	
									</td>										   										
									<td class="tdSer">
									<center><a id="search"></a></center>
									</td>
								</tr>
								<tr>
								<td>
									<emp:message key="ydyw_ywgl_ywtwgl_text_8" fileName="ydyw" defVal="创建人："></emp:message>
									</td>
									<td>
										<input type="text" id="userName"  name="userName" style="width: 154px;" value="<%=null==conditionMap.get("userName")?"":conditionMap.get("userName") %>" />
									</td>								
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_28" defVal="创建时间：" fileName="ydyw"></emp:message>
									</td>
									<td>
										<input type="text" value="<%=null==conditionMap.get("startSubmitTime")?"":conditionMap.get("startSubmitTime") %>" id="startSubmitTime" name="startSubmitTime" style="cursor: pointer; width: 154px;background-color: white;" class="Wdate" readonly="readonly" onclick="stime()">
									</td>
									<td align="left">
										<emp:message key="common_to" defVal="至：" fileName="common"></emp:message>
									</td>
									<td>
										<input type="text" value="<%=null==conditionMap.get("endSubmitTime")?"":conditionMap.get("endSubmitTime") %>" id="endSubmitTime" name="endSubmitTime" style="cursor: pointer; width: 154px;background-color: white;" class="Wdate" readonly="readonly" onclick="rtime()">
									</td>
									<td></td>
									</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								
								<th>
									<input type="checkbox" name="checkall" id="checkall" onclick="checkAlls(this,'checklist')" />
								</th>

								<th style="width: 15%">
									<emp:message key="ydyw_ywgl_ywtwgl_text_2" fileName="ydyw" defVal="贴尾名称"></emp:message>
								</th>
								<th style="width: 15%">
									<emp:message key="ydyw_ywgl_ywtwgl_text_4" fileName="ydyw" defVal="贴尾内容"></emp:message>
								</th>
								<th style="width: 15%">
									<emp:message key="ydyw_ywgl_ywtwgl_text_6" fileName="ydyw" defVal="适用业务"></emp:message>
								</th>
								<th style="width: 15%">
									<emp:message key="ydyw_ywgl_ywbgl_text_27" defVal="创建时间" fileName="ydyw"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywtwgl_text_10" fileName="ydyw" defVal="创建机构"></emp:message>
								</th>
								<th style="width: 14%">
									<emp:message key="ydyw_ywgl_ywtwgl_text_9" fileName="ydyw" defVal="创建人"></emp:message>
								</th>
								<th>
									<emp:message key="common_operation" defVal="操作" fileName="common"></emp:message>
								</th>
							</tr>
						</thead>
						<tbody>
							<%
							if(recordList != null && recordList.size()>0)
							{
								for(int k=0;k<recordList.size();k++)
								{
									DynaBean db=recordList.get(k);
									String bustail_id= db.get("bustail_id")==null?"":db.get("bustail_id").toString();
									String str="";
									if(tailList!=null){
											LinkedHashMap nameList= tailList.get(bustail_id);
											if(nameList!=null){
												Iterator it=nameList.entrySet().iterator();
												while(it.hasNext()){
													Map.Entry entity = (Entry) it.next();
													str=str+entity.getValue()+"("+entity.getKey()+")"+",";
												}
											}
										} 

						%>
						<tr align="center">
						<td>
								<input type="checkbox" name="checklist" id="checklist"  value="<%=bustail_id%>" onclick="selectIt(this)"/>
						</td>

						<td>
							<a onclick=javascript:showName(this)>
							<%
								String bustail_name=db.get("bustail_name")==null?"":db.get("bustail_name").toString();
							%>
							<label style="display:none"><xmp><%=bustail_name %></xmp></label>
							<xmp><%=bustail_name.length()>10?bustail_name.substring(0,10)+"...":bustail_name %></xmp>
						</a> 
								
						</td>
						<td>
						<a onclick=javascript:modify(this)>
							<%
								String xmessage=db.get("content")==null?"":db.get("content").toString();
							%>
							<label style="display:none"><xmp><%=xmessage %></xmp></label>
							<xmp><%=xmessage.length()>10?xmessage.substring(0,10)+"...":xmessage %></xmp>
						</a> 
						</td>
						<td>
						
						<% 
								if(!"".equals(str)){
									str=str.substring(0,str.length()-1);
								}
							
							%>
							<a onclick=javascript:showBus(this)>
							<label style="display:none"><xmp><%=str %></xmp></label>
							<xmp><%=str.length()>10?str.substring(0,10)+"...":str %></xmp>
							</a>
						</td>
						<td>
						<%
						if(db.get("create_time")!=null){
							String time=db.get("create_time").toString();
							if(time.length()>0&&time.indexOf(".")>0){
								out.print(time.substring(0,time.lastIndexOf(".")));
							}
						}
						%>
						</td>
						<td>
								<%=db.get("dep_name")%>
						</td>
						<td>
								<%=db.get("name")%>(<%=db.get("user_name")%>)
						</td>

						<td>
						<%if(btnMap.get(menuCode+"-2") != null){%>
						<a id="modif" href="javascript:modifSmsTmp('<%=bustail_id%>','<emp:message key="ydyw_ywgl_ywtwgl_text_1" fileName="ydyw" defVal="添加业务贴尾内容"></emp:message>')"><emp:message key="common_modify" fileName="common" defVal="修改"></emp:message></a>
						<%} %>
						<%if(btnMap.get(menuCode+"-3") != null){%>
						<a href="javascript:deleteMsg('<%=bustail_id%>')"><emp:message key="common_btn_5" defVal="删除" fileName="common"></emp:message></a>
						<%} %>
						</td>
						</tr>
						<%
								}
							}else{
						%>
						<tr><td colspan="8"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
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
					
				</form>
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
		
<div class="clear"></div>
	<div id="modify" title="<emp:message key="ydyw_ywgl_ywtwgl_text_4" fileName="ydyw" defVal="贴尾内容"></emp:message>"  style="padding:5px;width:150px;height:160px;display:none;word-wrap: break-word;word-break:break-all;overflow-x:hidden;overflow-y:auto;">
		<table width="240px">
			<thead>
				<tr style="padding-top:2px;margin-bottom: 2px;">
					<td>
						<span style="display:block;width:240px;"><label id="msgshow" style="width:100%;height:100%;word-break: break-all;white-space:normal;"></label></span>								
					</td>
				</tr>
			   <tr style="padding-top:2px;">
				<td>
				</td>
				</tr>
			</thead>
		</table>
	</div>
    
    <div class="clear"></div>
		<div id="buslist" title="<emp:message key="ydyw_ywgl_ywtwgl_text_6" fileName="ydyw" defVal="适用业务"></emp:message>"  style="padding:5px;width:200px;height:160px;display:none;word-wrap: break-word;word-break:break-all;overflow-x:hidden;overflow-y:auto;">
		<table width="240px">
			<thead>
				<tr style="padding-top:2px;margin-bottom: 2px;">
					<td>
						<span style="display:block;width:240px;"><label id="msgshow2" style="width:100%;height:100%;word-break: break-all;white-space:normal;"></label></span>
					</td>
				</tr>
			   <tr style="padding-top:2px;">
					<td>
					</td>
					</tr>
			</thead>
		</table>
	</div>
	
	    <div class="clear"></div>
		<div id="tailName" title="<emp:message key="ydyw_ywgl_ywtwgl_text_2" fileName="ydyw" defVal="贴尾名称"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
		<table width="240px">
			<thead>
				<tr style="padding-top:2px;margin-bottom: 2px;">
					<td>
						<span style="display:block;width:240px;"><label id="msgshow3" style="width:100%;height:100%;word-break: break-all;white-space:normal;"></label></span>
					</td>
				</tr>
			   <tr style="padding-top:2px;">
					<td>
					</td>
					</tr>
			</thead>
		</table>
	</div>
	
    <div id="msgtep"  style="padding:5px;width:950px;display:none;overflow-y:auto;overflow-x:auto;">
	    <div id="msg">
	    <table cellpadding="0" cellspacing="0" border="0" width="95%" style="margin-top: 20px;margin-left: 25px;">
		    <tbody>
		    <tr style="height: 55px;">
		    	<td width="75"><emp:message key="ydyw_ywgl_ywtwgl_text_2" fileName="ydyw" defVal="贴尾名称："></emp:message></td><td align="left"><input type="text" name="tmTitle"  style="width:520px;"  maxlength="25" class="input_bd div_bd" id="tmTitle">&nbsp;&nbsp;<font style="color: red;">*</font></td>
		    </tr>
			<tr>
			    <td width="75"><emp:message key="ydyw_ywgl_ywtwgl_text_5" fileName="ydyw" defVal="贴尾内容："></emp:message></td>
				<td align="left">
					<textarea id="tmMsg" name="tmMsg" class="input_bd div_bd contents_textarea" style="width:520px;height: 100px;" ></textarea>
					<font style="color: red;">*</font>
				</td>
		    </tr>
		    <tr>
			    <td colspan="2" style="text-align: right;padding-right: 50px;height: 10px"><label><b id="strlen" style="color: #868686;">0</b><b style="color: #868686;">/500</b></label></td>
		 	</tr>
					    
		    </tbody>
	    </table>
	    <table cellpadding="0" cellspacing="0" border="0" width="92%" style="margin-left: 25px;">
    	<tr style="height: 45px;" >
		    <td  width="75"><emp:message key="ydyw_ywgl_ywtwgl_text_7" fileName="ydyw" defVal="适用业务："></emp:message></td>
		    <td valign="middle" align="left" style="background-color: #f1f1f9;padding-left: 20px;">
				<input id="epname" name="epname" type="text" maxlength="16" class="graytext" value="" style="font-size:12px;border:0px;width:185px;height:22px;line-height:26px; background-color:#F3FAEE;border:#c9d9f2 1px solid; float: left;"  onkeypress="if(event.keyCode==13) {searchName();event.returnValue=false;}"/>
			 	<a onclick="searchName();" style="cursor: pointer;	display: block;width:28px;height: 23px;float: left;"><img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>
			 	<span style="margin-left:170px;"> <emp:message key="ydyw_ywgl_ywbgl_text_7" defVal="已选择业务数：" fileName="common"></emp:message><label id="manCount">0</label></span>
			</td>
		</tr>
	    </table>
	    <table cellpadding="0" cellspacing="0" border="0" width="80%"  style="margin-left: 100px;background-color: #f1f1f9;height: 245px;width:523px">
			<tr valign="top">
				<td style="width:215px;">
					<div class="dept div_bd" style="height: 220px;width:215px;margin-left: 20px;">
						<select  multiple name="left" id="left" size="15" class="left_select_choose" >
						<% 
						if(busList!=null&&busList.size()>0){
						for(int i=0;i<busList.size();i++){
							DynaBean db=busList.get(i);
							Object link=db.get("tablelink");
							Object state=db.get("state");
							String forbid="";
							if(state!=null){
								String strst=state.toString();
								if("1".equals(strst)){
									forbid="forbid='yes'";
								}
							}
							if(link==null){
						%>
							<option value="<%=db.get("bus_id")%>" <%=forbid%> title="<%=db.get("bus_name")%>(<%=db.get("bus_code")%>)" style="height: 22px;"><%=db.get("bus_name")%>(<%=db.get("bus_code")%>)</option>
						<%}else{%>
							<option value="<%=db.get("bus_id")%>" <%=forbid%> style="color:#CDCDCD;height: 22px;"  title="<emp:message key="ydyw_ywgl_ywtwgl_text_12" fileName="ydyw" defVal="已绑定"></emp:message>(<%=db.get("bustail_name")%>)<emp:message key="ydyw_ywgl_ywtwgl_text_13" fileName="ydyw" defVal="贴尾"></emp:message>" ismove="not"><%=db.get("bus_name")%>(<%=db.get("bus_code")%>)</option>
						<%}
						}
							
						}
						%>
						
						</select>
					</div>
				</td>
				<td width="60" align="center" valign="middle">
				<br>
					<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="common_option" defVal="选择" fileName="common"></emp:message>"  onclick="javascript:router();">
					<br/>
					<br/>
					<input class="btnClass1" type="button" id="toRight" value="<emp:message key="common_delete" defVal="刪除" fileName="common"></emp:message>" onclick="javascript:moveRight();">
				</td>
				<td>
					<div id="rightDiv"   class="dept div_bd" >
						<select multiple name="right" id="right" size="20" 
							style='width:104px;height: 318px;display:none; border:0;float:left;color: black;font-size: 12px;padding:4px;vertical-align:middle;margin:-6px -10px;' >
						</select>
						
						<ul id="getChooseMan">
						</ul>
					</div>
					<span style="display:block;width:5px;height:5px;color: red;margin-top:105px; float: left;">*</span>
				</td>
			</tr>
		</table>
	    </div>
	    <div>
	    <table cellpadding="0" cellspacing="0" border="0" width="80%"  style="margin-left: 90px;">
	    			<tr>
				<td id="btn" colspan="3" style="text-align: center;padding:15px;">
					<input name="subBut" type="button" id="subBut" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" onclick="save()" class="btnClass5 mr23"/>
					<input name="cancelwid" type="button" onclick="closeDiv()" value="<emp:message key="common_btn_16" defVal="取消" fileName="common"></emp:message>" class="btnClass6"/>
				</td>
		  	</tr>
	    </table>
	    </div>
	    
    </div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/smsTailManage.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/jquery_Ul_Send.js?V=116"></script>
	<script type="text/javascript" src="<%=iPath%>/js/chooseInfo_new.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=iPath%>/js/manhuaInputLetter.1.0.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
	<script>

	var zTree3;
	var setting3;
	var zNodes3 = [];
	var lguserid =$("#lguserid").val(); 
		$(document).ready(function() {
		    closeTreeFun(["dropMenu"]);
	   		 getLoginInfo("#hiddenValueDiv");
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
			
			setting3.asyncUrl ="ydyw_postTailMgr.htm?method=createDeptTree&lguserid="+lguserid;
			reloadTree(zNodes3);
			
			//翻页
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			//查询
			$('#search').click(function(){submitForm();});
			
			$(function (){
			    $("#tmMsg").manhuaInputLetter({                          
			        len : 500,//限制输入的字符个数                     
			        showId : "strlen"//显示剩余字符文本标签的ID
			    });
			});

		});
	</script>
	</body>
</html>
