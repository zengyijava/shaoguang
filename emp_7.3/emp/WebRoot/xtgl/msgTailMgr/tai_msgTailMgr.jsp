<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.Map.Entry" %>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
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
	List<DynaBean> spList = (List<DynaBean>)request.getAttribute("spList");
	
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String,String> conditionMap = (LinkedHashMap<String,String>) request.getAttribute("conditionMap");
	
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, LinkedHashMap> tailListByBus = (LinkedHashMap<String, LinkedHashMap>) request.getAttribute("tailListByBus");
	LinkedHashMap<String, LinkedHashMap> tailListBySp = (LinkedHashMap<String, LinkedHashMap>) request.getAttribute("tailListBySp");
	//是否包含子机构
	String isContainsSun=(String)request.getAttribute("isContainsSun");
	//按钮权限处理
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("msgTailMgr");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	//需要的基本参数
	String userid = request.getParameter("userid");
	String userName = request.getParameter("userName");
	@ SuppressWarnings("unchecked")
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");


%>
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/smsTailManage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/tai_msgTailMgr.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>

	<body id="tai_msgTailMgr">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>

			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="<%=path %>/tai_msgTailMgr.htm" method="post"
					id="pageForm">
			<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
			<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
			<%-- 选中id值 --%>
			<input type="hidden" id="selectid" name="selectid" value=""/>
			<%-- 修改单条记录时候需要记录的id--%>
			<input type="hidden" id="updateid" name="updateid" value=""/>
			
			<select class="rightSelectTemp" id='rightSelectTemp'></select>
			<select class="rightSelectTempSp" id='rightSelectTempSp'></select>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1")!=null) { %>
						<a id="add" href="javascript:showAddSmsTmp('<emp:message key="xtgl_cswh_twgl_xjtwnr" defVal="新建贴尾内容" fileName="xtgl"/>')"><emp:message key="xtgl_spgl_shlcgl_xj" defVal="新建" fileName="xtgl"/></a>
						<%} %>
						<%if(btnMap.get(menuCode+"-2")!=null) { %>
						<a id="delete" href="javascript:deleteSelect()"><emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/></a>
						<%} %>
					</div>
					<input type="hidden" id="pathUrl" value="<%=path %>"/>
					<div id="condition">
						<table>
								<tbody>
								<tr>
									<td>
										<emp:message key="xtgl_cswh_twgl_twmc_mh" defVal="贴尾名称：" fileName="xtgl"/>
									</td>
									<td>
									<input type="text" id="tailname" name="tailname" maxlength="25" value="<%=null==conditionMap.get("tailname")?"":conditionMap.get("tailname") %>" class="tailname">						
									</td>
									<td>
										<emp:message key="xtgl_cswh_twgl_twnr_mh" defVal="贴尾内容：" fileName="xtgl"/>
									</td>
									<td>
									<input type="text" id="content" name="content" maxlength="25" value="<%=null==conditionMap.get("content")?"":conditionMap.get("content") %>" class="content">						
									</td>
									<td>
										<emp:message key="xtgl_cswh_twgl_jg_mh" defVal="机构：" fileName="xtgl"/>
									</td>
									<td class="condi_f_l">											
									  		<div class="deptid_div">	
									  		
									  		 <input type="hidden" id="deptid" name="deptid" value="<%=conditionMap.get("deptid")==null?"":conditionMap.get("deptid")%>"/> 		
									  		<input type="text" class="treeInput depNam" id="depNam" name="depNam" value="<%=depNam==null?MessageUtils.extractMessage("xtgl","xtgl_cswh_twgl_qxz",request):depNam%>" 
									  			onclick="javascript:showMenu();"  readonly  />&nbsp;
											</div>														
											
											<div id="dropMenu" >
												<iframe class="dropMenu_frame" frameborder="0" src="about:blank"></iframe>	
												<div class="isContainsSun_div">
												 <input type="checkbox" id="isContainsSun" name="isContainsSun" <%if(isContainsSun.equals("1")){%> checked="checked" <%}%> value="1" class="isContainsSun"/><emp:message key="xtgl_cswh_twgl_bhzjg" defVal="包含子机构" fileName="xtgl"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" style=""/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='xtgl_cswh_twgl_qk' defVal='清空' fileName='xtgl'/>" class="btnClass1" onclick="javascript:cleanSelect_dep3();" style=""/>
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
										<emp:message key="xtgl_cswh_twgl_twlx_mh" defVal="贴尾类型：" fileName="xtgl"/>
									</td>
									<td>				
										<select name="tailtype" id="tailtype" class="tailtype" >
										<option value="">
											<emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/>
										</option>
										<option value="0" <%="0".equals(conditionMap.get("tailtype"))?"selected":"" %>>
											<emp:message key="xtgl_cswh_twgl_qjtw" defVal="全局贴尾" fileName="xtgl"/>
										</option>
										<option value="1" <%="1".equals(conditionMap.get("tailtype"))?"selected":"" %>>
											<emp:message key="xtgl_cswh_twgl_ywtw" defVal="业务贴尾" fileName="xtgl"/>
										</option>
										<option value="2" <%="2".equals(conditionMap.get("tailtype"))?"selected":"" %>>
											<emp:message key="xtgl_cswh_twgl_spzhtw" defVal="SP账号贴尾" fileName="xtgl"/>
										</option>
										</select>
									</td>
									
									<td>
										<emp:message key="xtgl_cswh_twgl_spzh_mh" defVal="SP账号：" fileName="xtgl"/>
									</td>
									<td>
									<input type="text" id="spuserid" name="spuserid" maxlength="25" value="<%=null==conditionMap.get("spuserid")?"":conditionMap.get("spuserid") %>" class="spuserid">						
									</td>
									<td>
										<emp:message key="xtgl_cswh_twgl_ywlx_mh" defVal="业务类型：" fileName="xtgl"/>
									</td>
									<td>
									<input type="text" id="buscode" name="buscode" maxlength="25" value="<%=null==conditionMap.get("buscode")?"":conditionMap.get("buscode") %>" class="buscode">						
									</td>
								</tr>
								<tr>
								<td>
										<emp:message key="xtgl_spgl_shlcgl_cjr_mh" defVal="创建人：" fileName="xtgl"/>
									</td>
									<td>
										<input type="text" id="userName"  name="userName" class="userName" value="<%=null==conditionMap.get("userName")?"":conditionMap.get("userName") %>" />
									</td>								
									<td>
										<emp:message key="xtgl_spgl_mbsp_cjsj_mh" defVal="创建时间：" fileName="xtgl"/>
									</td>
									<td>
										<input type="text" value="<%=null==conditionMap.get("startSubmitTime")?"":conditionMap.get("startSubmitTime") %>" id="startSubmitTime" name="startSubmitTime"   class="Wdate startSubmitTime" readonly="readonly" onclick="stime()">
									</td>
									<td align="left">
										<emp:message key="xtgl_spgl_xxsp_z_mh" defVal="至：" fileName="xtgl"/>
									</td>
									<td>
										<input type="text" value="<%=null==conditionMap.get("endSubmitTime")?"":conditionMap.get("endSubmitTime") %>" id="endSubmitTime" name="endSubmitTime"  class="Wdate endSubmitTime" readonly="readonly" onclick="rtime()">
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

								<th class="twmc_th">
									<emp:message key="xtgl_cswh_twgl_twmc" defVal="贴尾名称" fileName="xtgl"/>
								</th>
								<th class="twlx_th">
									<emp:message key="xtgl_cswh_twgl_twlx" defVal="贴尾类型" fileName="xtgl"/>
								</th>
								<th>
									<emp:message key="xtgl_cswh_twgl_twnr" defVal="贴尾内容" fileName="xtgl"/>
								</th>
								<th class="syyw_th">
									 <emp:message key="xtgl_cswh_twgl_syyw" defVal="适用业务" fileName="xtgl"/>
								</th>
								<th class="cjsj_th">
									<emp:message key="xtgl_cswh_twgl_cjsj" defVal="创建时间" fileName="xtgl"/>
								</th>
								<th class="cjjg_th">
									<emp:message key="xtgl_cswh_twgl_cjjg" defVal="创建机构" fileName="xtgl"/>
								</th>
								<th class="cjr_th">
									<emp:message key="xtgl_spgl_shlcgl_cjr" defVal="创建人" fileName="xtgl"/>
								</th>
								<th class="cz_th">
									<emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
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
									
									String tail_id= db.get("tail_id")==null?"":db.get("tail_id").toString();
									
									String strByBus="";
									if(tailListByBus!=null){
											LinkedHashMap nameListByBus= tailListByBus.get(tail_id);
											if(nameListByBus!=null){
												Iterator it1=nameListByBus.entrySet().iterator();
												while(it1.hasNext()){
													Map.Entry entity1 = (Entry) it1.next();
													strByBus=strByBus+entity1.getValue()+"("+entity1.getKey()+")"+",";
													//System.out.println("bus"+strByBus+"");
												}
											}
										} 
									String strBySp="";
									if(tailListBySp!=null){
										LinkedHashMap nameListBySp= tailListBySp.get(tail_id);
										if(nameListBySp!=null){
											Iterator it2=nameListBySp.entrySet().iterator();
											while(it2.hasNext()){
												Map.Entry entity2 = (Entry) it2.next();
												strBySp=strBySp+entity2.getValue()+"("+entity2.getKey()+")"+",";
												//System.out.println("sp"+strBySp);
											}
										}
									} 
									
						%>
						<tr align="center">
						<td>
							<%
							String tail_type=db.get("tail_type")==null?"":db.get("tail_type").toString();
							String type="";
							if(!"0".equals(tail_type)){%>
								<input type="checkbox" name="checklist" id="checklist"  value="<%=tail_id%>" onclick="selectIt(this)"/>
							<%}else{ %>
								<font>-</font>
							<%} %>
						</td>

						<td>
							<a onclick="javascript:showName(this)">
							<%
								String tail_name=db.get("tail_name")==null?"":db.get("tail_name").toString();
							%>
							<label class="tail_name_label"><xmp class="tail_name_xmp"><%=tail_name %></xmp></label>
							<xmp class="tail_name_xmp_2"><%=tail_name.length()>10?tail_name.substring(0,10)+"...":tail_name %></xmp>
						</a> 
								
						</td>
						
						<td>
							
							<%
							
							if("0".equals(tail_type)){
								type=MessageUtils.extractMessage("xtgl","xtgl_cswh_twgl_qjtw",request);
							}else if("1".equals(tail_type)){
								type=MessageUtils.extractMessage("xtgl","xtgl_cswh_twgl_ywtw",request);
							}else{
								type=MessageUtils.extractMessage("xtgl","xtgl_cswh_twgl_spzhtw",request);
							}
							%>
							<label class="type_label"><xmp><%=type %></xmp></label>
							
							<xmp><%=type%></xmp>
						
								
						</td>
						
						<td>
						<a onclick="javascript:modify(this)">
							<%
								String xmessage=db.get("content")==null?"":db.get("content").toString();
								//xmessage=StringEscapeUtils.unescapeHtml(xmessage);
							%>
							<label class="xmessage_label"><xmp class="xmessage_xmp"><%=xmessage %></xmp></label>
							<xmp class="xmessage_xmp_2"><%=xmessage.length()>10?xmessage.substring(0,10)+"...":xmessage %></xmp>
						</a> 
						</td>

						
						<td>
					
						<% 
							String str = "";
							if("0".equals(tail_type)){
								str = MessageUtils.extractMessage("xtgl","xtgl_cswh_twgl_qjtw",request);
						%>	
							<a onclick="javascript:showBus(this)">	
							<label class="str_label"><xmp><%=str %></xmp></label>
							<xmp><%=str.length()>10?str.substring(0,10)+"...":str %></xmp>	
							</a>	
						<%	}
							if("1".equals(tail_type)){
								if(!"".equals(strByBus)){
									strByBus=strByBus.substring(0,strByBus.length()-1);
									str = strByBus;
								}
						%>		
							<a onclick="javascript:showBus(this)">
							<label class="str_label"><xmp><%=str %></xmp></label>
							<xmp><%=str.length()>10?str.substring(0,10)+"...":str %></xmp>
							</a>	
								
								
						<%	}
							if("2".equals(tail_type)){
								if(!"".equals(strBySp)){
									strBySp=strBySp.substring(0,strBySp.length()-1);
									str = strBySp;
								}
						%>	
							<a onclick="javascript:showBus(this)">
							<label class="str_label"><xmp><%=str %></xmp></label>
							<xmp><%=str.length()>10?str.substring(0,10)+"...":str %></xmp>
							</a>	
						
						<%	}%>
							
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
								<%=db.get("dep_name")!=null?db.get("dep_name").toString():""%>
						</td>
						<td>
								<%=db.get("name")!=null?db.get("name").toString():""%>(<%=db.get("user_name")!=null?db.get("user_name").toString():"" %>)
						</td>

						<td>
						<%if(!"0".equals(tail_type)){%>
						<%if(btnMap.get(menuCode+"-3")!=null) { %>
						<a id="modif" href="javascript:modifSmsTmp('<%=tail_id%>','<emp:message key="xtgl_cswh_twgl_xgywtwlr" defVal="修改业务贴尾内容" fileName="xtgl"/>','<%=tail_type %>')"><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a>
						<%} %>
						<%if(btnMap.get(menuCode+"-2")!=null) { %>
						<a href="javascript:deleteMsg('<%=tail_id%>')"><emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/></a>
						<%} %>
						<%}else{ %>
						<font>-</font>
						<%} %>
						</td>
						</tr>
						<%
								}
							}else{
						%>
						<tr><td colspan="9"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/></td></tr>
						<%} %>

						</tbody>
						<tfoot>
							<tr>
								<td colspan="9">
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
	<div id="modify" title="<emp:message key='xtgl_cswh_twgl_twnr' defVal='贴尾内容' fileName='xtgl'/>"  class="modify">
		<table class="modify_table">
			<thead>
				<tr class="msgshow_tr">
					<td>
						<span class="msgshow_span"><label id="msgshow" class="msgshow"></label></span>		
					</td>
				</tr>
			   <tr class="msgshow_down_tr">
				<td>
				</td>
				</tr>
			</thead>
		</table>
	</div>
    
    <div class="clear"></div>
		<div id="buslist" title="<emp:message key='xtgl_cswh_twgl_syyw' defVal='适用业务' fileName='xtgl'/>"  class="buslist">
		<table class="buslist_table">
			<thead>
				<tr class="msgshow2_tr">
					<td>
						<span class="msgshow2_span"><label id="msgshow2" class="msgshow2"></label></span>
					</td>
				</tr>
			   <tr class="msgshow2_down_tr">
					<td>
					</td>
					</tr>
			</thead>
		</table>
	</div>
	
	    <div class="clear"></div>
		<div id="tailName" title="<emp:message key='xtgl_cswh_twgl_twmc' defVal='贴尾名称' fileName='xtgl'/>"  class="tailName">
		<table class="tailName_table">
			<thead>
				<tr class="msgshow3_tr">
					<td>
						<span class="msgshow3_span"><label id="msgshow3" class="msgshow3"></label></span>
					</td>
				</tr>
			   <tr class="msgshow3_down_tr">
					<td>
					</td>
					</tr>
			</thead>
		</table>
	</div>
	
    <div id="msgtep"  class="msgtep">
	    <div id="msg">
	    <table cellpadding="0" cellspacing="0" border="0"  class="msg_table">
		    <tbody>
		    <tr class="twlx_mh_tr">
		    	<td class="twlx_mh_td"><emp:message key="xtgl_cswh_twgl_twlx_mh" defVal="贴尾类型：" fileName="xtgl"/></td><td align="left">
		    	
		    		<select id="tmTailtype" name="tmTailtype"  class="input_bd div_bd tmTailtype" onchange="javascript:changeBusAndSp();">
						<option value="2"><emp:message key="xtgl_cswh_twgl_spzh" defVal="SP账号" fileName="xtgl"/></option>
						<option value="1"><emp:message key="xtgl_cswh_twgl_ywlx" defVal="业务类型" fileName="xtgl"/></option>
					</select>
		    	</td>
		    </tr>
		    <tr class="tmTitle_tr">
		    	<td class="tmTitle_td"><emp:message key="xtgl_cswh_twgl_twmc_mh" defVal="贴尾名称：" fileName="xtgl"/></td><td align="left"><input type="text" name="tmTitle"   maxlength="25" class="input_bd div_bd tmTitle" id="tmTitle">&nbsp;&nbsp;<font class="font_red">*</font></td>
		    </tr>
			<tr>
			    <td class="twnr_mh_td"><emp:message key="xtgl_cswh_twgl_twnr_mh" defVal="贴尾内容：" fileName="xtgl"/></td>
				<td align="left">
					<textarea id="tmMsg" name="tmMsg" class="input_bd div_bd contents_textarea tmMsg"   maxlength="128"></textarea>
					<font class="font_red">*</font>
				</td>
		    </tr>
		    <tr>
			    <td colspan="2" class="strlen_td"><label><b id="strlen" class="strlen">0</b><b class="strlen_next_b">/128</b></label></td>
		 	</tr>
					    
		    </tbody>
	    </table>
	    <div id="busTableDiv">
	    <table cellpadding="0" cellspacing="0" border="0"  class="busTableDiv_table">
    	<tr class="syfw_mh_tr" >
		    <td  class="syfw_mh_td"><emp:message key="xtgl_cswh_twgl_syfw_mh" defVal="适用范围：" fileName="xtgl"/></td>
		    <td valign="middle" align="left" class="epname_td">
				<input id="epname" name="epname" type="text" maxlength="16" class="graytext epname" value=""  onkeypress="if(event.keyCode==13) {searchName();event.returnValue=false;}"/>
			 	<a onclick="searchName();" class="epname_a"><img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>
			 	<span class="<%=StaticValue.ZH_HK.equals(langName)?"yxzyws_mh_span_1":"yxzyws_mh_span_2"%>"  > <emp:message key="xtgl_cswh_twgl_yxzyws_mh" defVal="已选择业务数：" fileName="xtgl"/><label id="manCount">0</label></span>
			</td>
		</tr>
	    </table>
	    <table cellpadding="0" cellspacing="0" border="0" width="80%"  class="left_table">
			<tr valign="top">
				<td class="left_td">
					<div class="dept div_bd left_div"  >
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
							<option value="<%=db.get("bus_id")%>" <%=forbid%> title="<%=db.get("bus_name")%>(<%=db.get("bus_code")%>)" class="bus_name_option"><%=db.get("bus_name")%>(<%=db.get("bus_code")%>)</option>
						<%}else{%>
							<option value="<%=db.get("bus_id")%>" <%=forbid%> class="ybd_option"  title="<emp:message key='xtgl_cswh_twgl_ybd' defVal='已绑定' fileName='xtgl'/>(<%=db.get("tail_name")%>)<emp:message key='xtgl_cswh_twgl_tw' defVal='贴尾' fileName='xtgl'/>" ismove="not"><%=db.get("bus_name")%>(<%=db.get("bus_code")%>)</option>
						<%}
						}
							
						}
						%>
						
						</select>
					</div>
				</td>
				<td class="toLeft_td" align="center" valign="middle">
				<br>
					<input class="btnClass1" type="button" id="toLeft" value="<emp:message key='xtgl_spgl_shlcgl_xz' defVal='选择' fileName='xtgl'/>"  onclick="javascript:router();">
					<br/>
					<br/>
					<input class="btnClass1" type="button" id="toRight" value="<emp:message key='xtgl_spgl_shlcgl_sc' defVal='删除' fileName='xtgl'/>" onclick="javascript:moveRight();">
				</td>
				<td>
					<div id="rightDiv"   class="dept div_bd" >
						<select multiple name="right" id="right" size="20" 
							class='right' >
						</select>
						
						<ul id="getChooseMan">
						</ul>
					</div>
					<span class="getChooseMan_down_span">*</span>
				</td>
			</tr>
		</table>
		</div>
		<div id="spTableDiv">
	    <table cellpadding="0" cellspacing="0" border="0" class="spTableDiv_table">
    	<tr class="syfw_mh_tr" >
		    <td  class="syfw_mh_td"><emp:message key="xtgl_cswh_twgl_syfw_mh" defVal="适用范围：" fileName="xtgl"/></td>
		    <td valign="middle" align="left" class="epnameSp_td">
				<input id="epnameSp" name="epnameSp" type="text" maxlength="16" class="graytext epnameSp" value=""  onkeypress="if(event.keyCode==13) {searchName();event.returnValue=false;}"/>
			 	<a onclick="searchNameSp();" class="searchIcon_a"><img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>
			 	<span class="<%=StaticValue.ZH_HK.equals(langName)?"yxzspzhs_mh_span_1":"yxzspzhs_mh_span_2"%>" > <emp:message key="xtgl_cswh_twgl_yxzspzhs_mh" defVal="已选择SP账号数：" fileName="xtgl"/><label id="manCountSp">0</label></span>
			</td>
		</tr>
	    </table>
	    <table cellpadding="0" cellspacing="0" border="0"  class="leftSp_table">
			<tr valign="top">
				<td class="leftSp_td">
					<div class="dept div_bd leftSp_div"  >
						<select  multiple name="leftSp" id="leftSp" size="15" class="left_select_choose" >
						<% 
						if(spList!=null&&spList.size()>0){
						for(int i=0;i<spList.size();i++){
							DynaBean db=spList.get(i);
							Object link=db.get("tablelink");
							
							if(link==null){
						%>
							<option value="<%=db.get("userid")%>" title="<%=db.get("userid")%>(<%=db.get("staffname")==null?"":db.get("staffname")%>)" class="userid_option"><%=db.get("userid")%>(<%=db.get("staffname")==null?"":db.get("staffname")%>)</option>
						<%}else{%>
							<option value="<%=db.get("userid")%>" class="ybd_option"  title="<emp:message key='xtgl_cswh_twgl_ybd' defVal='已绑定' fileName='xtgl'/>(<%=db.get("staffname")==null?"":db.get("staffname")%>)<emp:message key='xtgl_cswh_twgl_tw' defVal='贴尾' fileName='xtgl'/>" ismove="not"><%=db.get("userid")%>(<%=db.get("staffname")==null?"":db.get("staffname")%>)</option>
						<%}
						}
							
						}
						%>
						
						</select>
					</div>
				</td>
				<td class="xz_td" align="center" valign="middle">
				<br>
					<input class="btnClass1" type="button" id="toLeftSp" value="<emp:message key='xtgl_spgl_shlcgl_xz' defVal='选择' fileName='xtgl'/>"  onclick="javascript:routerSp();">
					<br/>
					<br/>
					<input class="btnClass1" type="button" id="toRightSp" value="<emp:message key='xtgl_spgl_shlcgl_sc' defVal='删除' fileName='xtgl'/>" onclick="javascript:moveRightSp();">
				</td>
				<td>
					<div id="rightDiv"   class="dept div_bd" >
						<select multiple name="rightSp" id="rightSp" size="20" 
							class='rightSp' >
						</select>
						
						<ul id="getChooseManSp">
						</ul>
					</div>
					<span class="getChooseManSp_down_span">*</span>
				</td>
			</tr>
		</table>
		</div>
	    </div>
	    <div>
	    <table cellpadding="0" cellspacing="0" border="0"  class="subBut_table">
	    			<tr>
				<td id="btn" colspan="3" class="subBut_td">
					<input name="subBut" type="button" id="subBut" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" onclick="save()" class="btnClass5 mr23"/>
					<input name="cancelwid" type="button" onclick="closeDiv()" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" class="btnClass6"/>
				</td>
		  	</tr>
	    </table>
	    </div>
	    
    </div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<%-- <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.JSP_IMP_VERSION %>"></script> --%>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/smsTailManage.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/jquery_Ul_Send.js?V=116"></script>
	<script type="text/javascript" src="<%=iPath%>/js/chooseInfo_new.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=iPath%>/js/manhuaInputLetter.1.0.js?V=<%=StaticValue.getJspImpVersion() %>"></script>

	<script>

	var zTree3;
	var setting3;
	var zNodes3 = [];
	var lguserid =$("#lguserid").val(); 
		$(document).ready(function() {

			var theTailtype = $('#tmTailtype').val();
			if(theTailtype == 2){
				$("#spTableDiv").show();
			   	$("#busTableDiv").hide();
			}else{
				$("#spTableDiv").hide();
			   	$("#busTableDiv").show();
			}
			
			
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
			
			setting3.asyncUrl ="tai_msgTailMgr.htm?method=createDeptTree&lguserid="+lguserid;
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

		function changeBusAndSp(){
			
			var theTailtype = $('#tmTailtype').val();
			if(theTailtype == 2){
				$("#spTableDiv").show();
			   	$("#busTableDiv").hide();
			}else{
				$("#spTableDiv").hide();
			   	$("#busTableDiv").show();
			}
			
			
		}
	</script>
	</body>
</html>
