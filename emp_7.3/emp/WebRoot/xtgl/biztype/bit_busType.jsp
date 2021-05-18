<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.biztype.vo.LfBusManagerVo"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.LinkedHashMap"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	List<LfBusManagerVo> busVoList = (List<LfBusManagerVo>) request.getAttribute("busVoList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	LfBusManagerVo busVo = (LfBusManagerVo)request.getAttribute("busVo");
	String state = busVo.getState()!=null?busVo.getState().toString():"";
	String riseLevel = busVo.getRiseLevel()!=null?busVo.getRiseLevel().toString():null;
	String busType = busVo.getBusType()!=null?busVo.getBusType().toString():null;
	String recvtime = busVo.getEndSubmitTime()!=null?busVo.getEndSubmitTime():null;
	String sendtime = busVo.getStartSubmitTime()!=null?busVo.getStartSubmitTime():null;
	String deptid = busVo.getDepIds()!=null?busVo.getDepIds():null;
	String depNam = busVo.getDepName()!=null?busVo.getDepName():null;
	String busName = busVo.getBusName()!=null?busVo.getBusName():null;
	String busCode = busVo.getBusCode()!=null?busVo.getBusCode():null;
	String name = busVo.getName()!=null?busVo.getName():null;
	//String isContainsSun = busVo.getIsContainsSun()!=null?busVo.getIsContainsSun():null;
	//是否机构权限 1个人 2机构
	String isPermissionType=(String)request.getAttribute("isPermissionType");
	//是否包含子机构
	String isContainsSun=(String)request.getAttribute("isContainsSun");
	if(isContainsSun==null||"".equals(isContainsSun)){
		isContainsSun="1";
	}
//	System.out.println("isContainsSun="+isContainsSun);
	String zNodes3 = (String)request.getAttribute("departmentTree");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("busType");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    //String result= (String)request.getAttribute("result");
    String upmax=(String)request.getAttribute("upmax");
    String resultPath =(String)request.getAttribute("path");
    Integer successNum = request.getAttribute("successNum") == null ? 0 : (Integer)request.getAttribute("successNum");
    Integer errorNum =request.getAttribute("errorNum") == null ? 0 : (Integer)request.getAttribute("errorNum");;
    
    
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="xtgl_cswh_twgl_ywlx" defVal="业务类型" fileName="xtgl"/></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/bit_busType.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	
	<%if(upmax!=null&&!"".equals(upmax)){ %>
	<script type="text/javascript">
	alert("<%=upmax%>"); 
	</script>
	<%} %>
	
	<script type="text/javascript">
	var errorNum="<%=errorNum%>";
	var successNum="<%=successNum%>";
	/* alert("errorNum"+errorNum);
	alert("successNum"+successNum); */
   </script>
	<body id="bit_busType">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<form name="pageForm" action="bit_busType.htm" method="post" id="pageForm">
			<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
			<input type="hidden" value="" id="busCodeTemp" />
			<input type="hidden" value="" id="menuCodeTemp" />

			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent" >
				<div class="buttons">
					<div id="toggleDiv">
					</div>
					<% if(btnMap.get(menuCode+"-1")!=null) {  %>
					<a id="add"><emp:message key="xtgl_spgl_shlcgl_xj" defVal="新建" fileName="xtgl"/></a>
					<a class="addNoti" id="downLodeTemple" href="javascript:download_href('<%=path %>/down.htm?filepath=xtgl/biztype/file/temp/lf_bizType_<%=langName %>.xlsx')" >
					<emp:message key="xtgl_cswh_ywlxgl_down_mb" defVal="下载导入模板" fileName="xtgl"/></a>
					
					<a id="upload" href="javascript:fileBind()">
		
					<emp:message key="xtgl_czygl_gjaqsz_dr" defVal="导入" fileName="xtgl"/>
					
					</a>
					
					<% } %>
				</div>
				<div id="condition">
					<table>
						<tr>
							<td>
								<emp:message key="xtgl_cswh_ywlxgl_ywmc_mh" defVal="业务名称：" fileName="xtgl"/>
							</td>
							<td>
								<label>
									<input type="text" class="busName" name="busName" id="busName" value="<%=busName==null?"":busName %>" maxlength="32"/>
								</label>
							</td>
							<td>
								<emp:message key="xtgl_cswh_ywlxgl_ywbm_mh" defVal="业务编码：" fileName="xtgl"/>
							</td>
							<td>
								<label>
									<input type="text" class="busCode" name="busCode" id="busCode" value="<%=busCode==null?"":busCode %>" maxlength="32" />
								</label>
							</td>
							<td>
										<emp:message key="xtgl_cswh_ywlxgl_zt_mh" defVal="状态：" fileName="xtgl"/>
									</td>
									<td>
<%--										//状态（0启用；1禁用） --%>
										<select id="state" name="state" class="state">
											<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/></option>
											<option value="0"  <%="0".equals(state)?"selected":"" %>><emp:message key="xtgl_spgl_shlcgl_qy" defVal="启用" fileName="xtgl"/></option>
											<option value="1"  <%="1".equals(state)?"selected":"" %>><emp:message key="xtgl_spgl_shlcgl_jy" defVal="禁用" fileName="xtgl"/></option>
										</select>
									</td>
							<td class="tdSer">
												<center><a id="search"></a></center>
											</td>
						</tr>
						<tr>
							<td>
								<emp:message key="xtgl_cswh_ywlxgl_yxj_mh" defVal="优先级：" fileName="xtgl"/>
							</td>
							<td>
								<select id="riseLevel" name="riseLevel" class="riseLevel">
											<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/></option>
											<option value="1"  <%="1".equals(riseLevel)?"selected":"" %>>1<emp:message key="xtgl_cswh_ywlxgl_zgyxj" defVal="（最高优先级）" fileName="xtgl"/></option>
											<option value="2"  <%="2".equals(riseLevel)?"selected":"" %>>2</option>
											<option value="3"  <%="3".equals(riseLevel)?"selected":"" %>>3</option>
											<option value="4"  <%="4".equals(riseLevel)?"selected":"" %>>4</option>
											<option value="5"  <%="5".equals(riseLevel)?"selected":"" %>>5</option>
											<option value="6"  <%="6".equals(riseLevel)?"selected":"" %>>6</option>
											<option value="7"  <%="7".equals(riseLevel)?"selected":"" %>>7</option>
											<option value="8"  <%="8".equals(riseLevel)?"selected":"" %>>8</option>
											<option value="9"  <%="9".equals(riseLevel)?"selected":"" %>>9<emp:message key="xtgl_cswh_ywlxgl_zdyxj" defVal="（最低优先级）" fileName="xtgl"/></option>
											<option value="-99"  <%="-99".equals(riseLevel)?"selected":"" %>><emp:message key="xtgl_cswh_ywlxgl_wyxj" defVal="无优先级" fileName="xtgl"/></option>
										</select>
							</td>
							<td>
								<emp:message key="xtgl_cswh_twgl_ywlx_mh" defVal="业务类型：" fileName="xtgl"/>
							</td>
							<td>
								<select id="busType" name="busType" class="busType">
											<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/></option>
											<option value="0"  <%="0".equals(busType)?"selected":"" %>><emp:message key="xtgl_cswh_ywlxgl_sd" defVal="手动" fileName="xtgl"/></option>
											<option value="1"  <%="1".equals(busType)?"selected":"" %>><emp:message key="xtgl_cswh_ywlxgl_cf" defVal="触发" fileName="xtgl"/></option>
											<option value="2"  <%="2".equals(busType)?"selected":"" %>><emp:message key="xtgl_cswh_ywlxgl_sd_cf" defVal="手动+触发" fileName="xtgl"/></option>
										</select>
							</td>
							<td>
										<emp:message key="xtgl_cswh_ywlxgl_czy_mh" defVal="操作员：" fileName="xtgl"/>
									</td>
									<td>
										<label>
										<input type="text" class="name" name="name" id="name" value="<%=name==null?"":name %>" maxlength="16" />
								</label>
									</td>
									<td>
									</td>
						</tr>
						<tr>
<%--							<%--%>
<%--							if("2".equals(isPermissionType)){--%>
<%--								%>--%>
								<td>
								<emp:message key="xtgl_spgl_shlcgl_jg_mh" defVal="机构：" fileName="xtgl"/>
							</td>
							<td class="condi_f_l">
								<div class="deptid_div">	 
									  		 <input type="hidden" id="deptid" name="depid" value="<%=deptid==null?"":deptid%>"/> 		
									  		<input type="text" id="depNam" class="treeInput depNam" name="depNam" value="<%=depNam==null?MessageUtils.extractMessage("xtgl","xtgl_cswh_twgl_qxz",request):depNam%>"  onclick="javascript:showMenu();"  readonly />
											</div>														
											<div id="dropMenu" >
											<iframe class="dropMenu_iframe" frameborder="0" src="about:blank"></iframe>	
												<div class="dropMenu_iframe_div">
												     <input type="checkbox" id="isContainsSun" name="isContainsSun" <%if("1".equals(isContainsSun)){%>checked="checked" <%}%> value="1" class="isContainsSun"/><emp:message key="xtgl_cswh_twgl_bhzjg" defVal="包含子机构" fileName="xtgl"/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" />&nbsp;&nbsp;
													<input type="button" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='清空' fileName='xtgl'/>" class="btnClass1" onclick="cleanSelect_dep3();" />
												</div>	
												<ul id="dropdownMenu"  class="tree"></ul>	
											</div>
							</td>
							<td>
										<emp:message key="xtgl_spgl_mbsp_cjsj_mh" defVal="创建时间：" fileName="xtgl"/>
									</td>
									<td>
										<input type="text"
											readonly="readonly" class="Wdate sendtime" onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime">
									</td>
									<td align="left">
										<emp:message key="xtgl_spgl_xxsp_z_mh" defVal="至：" fileName="xtgl"/>
									</td>
									<td>
										<input type="text"
											readonly="readonly" class="Wdate recvtime"  onclick="rtime()"
											value="<%=recvtime==null?"":recvtime %>" 
											 id="recvtime" name="recvtime">
									</td>
											<td>
											</td>
<%--							<%--%>
<%--							}else{--%>
<%--							%>--%>
<%--							--%>
<%--							<td>--%>
<%--										创建时间：--%>
<%--									</td>--%>
<%--									<td>--%>
<%--										<input type="text"--%>
<%--											style="cursor: pointer; width: 178px; background-color: white;"--%>
<%--											readonly="readonly" class="Wdate" onclick="stime()" --%>
<%--											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime">--%>
<%--									</td>--%>
<%--									<td align="left">--%>
<%--										至：--%>
<%--									</td>--%>
<%--									<td>--%>
<%--										<input type="text"--%>
<%--											style="cursor: pointer; width: 178px; background-color: white;"--%>
<%--											readonly="readonly" class="Wdate"  onclick="rtime()"--%>
<%--											value="<%=recvtime==null?"":recvtime %>" --%>
<%--											 id="recvtime" name="recvtime">--%>
<%--									</td>--%>
<%--											<td>--%>
<%--											</td>--%>
<%--											<td>--%>
<%--								--%>
<%--							</td>--%>
<%--							<td class="condi_f_l">--%>
<%--							</td>--%>
<%--							<%} %>--%>
						</tr>
						
					</table>
				</div>
				<table id="content">
					<thead>
						<tr >
							<th class="th1">
								<emp:message key="xtgl_cswh_ywlxgl_ywmc" defVal="业务名称" fileName="xtgl"/>
							</th>
							<th class="th2">
								<emp:message key="xtgl_cswh_ywlxgl_ywbm" defVal="业务编码" fileName="xtgl"/>
							</th>
							<th class="th3">
								<emp:message key="xtgl_cswh_twgl_ywlx" defVal="业务类型" fileName="xtgl"/>
							</th>
							<th class="th4">
								<emp:message key="xtgl_cswh_ywlxgl_fsyxj" defVal="发送优先级" fileName="xtgl"/>
							</th>
							<th class="th5">
								<emp:message key="xtgl_cswh_ywlxgl_zt" defVal="状态" fileName="xtgl"/>
							</th>
							<th class="th6">
								<emp:message key="xtgl_cswh_twgl_cjsj" defVal="创建时间" fileName="xtgl"/>
							</th>
							<th class="th7">
								<emp:message key="xtgl_cswh_ywlxgl_xgsj" defVal="修改时间" fileName="xtgl"/>
							</th>
							<th class="th8">
								<emp:message key="xtgl_spgl_shlcgl_jg" defVal="机构" fileName="xtgl"/>
							</th>
							<th class="th9">
								<emp:message key="xtgl_spgl_shlcgl_czy" defVal="操作员" fileName="xtgl"/>
							</th>
							<th class="th10">
								<emp:message key="xtgl_cswh_ywlxgl_ywms" defVal="业务描述" fileName="xtgl"/>
							</th>
							<%if(btnMap.get(menuCode+"-3") != null || btnMap.get(menuCode+"-2") != null)  {  %>
							<th <%if(btnMap.get(menuCode+"-3") != null && btnMap.get(menuCode+"-2") != null)  {  out.print(" colspan=2");}%> class="cz_th">
								<emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
							</th>
							<%} %>
						</tr>
					</thead>
					<tbody>
						<%
						if(busVoList != null && busVoList.size()>0)
						{
							for(int i=0;i<busVoList.size();i++)
							{
								LfBusManagerVo bus = busVoList.get(i); 
						%>
						<tr>
							<td class="" >
							<label id="lbName<%=bus.getBusId() %>"><xmp><%=StringEscapeUtils.unescapeHtml(bus.getBusName())%></xmp></label>
							</td>
							<td>
							<label id="lbCode<%=bus.getBusId() %>"><%=bus.getBusCode()%></label>
							</td>
							<td>
							<input type="hidden" id="lbType<%=bus.getBusId() %>" value="<%=bus.getBusType()%>" />
							<%
								String s = "";
								boolean flag = false;
								 if (bus.getBusType()== 0)
								 {
								     s = MessageUtils.extractMessage("xtgl","xtgl_cswh_ywlxgl_sd",request);
								     flag = true;
							     }else if(bus.getBusType()== 1)
								 {
								     s = MessageUtils.extractMessage("xtgl","xtgl_cswh_ywlxgl_cf",request);
								     flag = true;
							     }else if(bus.getBusType()== 2)
								 {
								     s = MessageUtils.extractMessage("xtgl","xtgl_cswh_ywlxgl_sd_cf",request);
								     flag = true;
							     }
							%>
							<%
								if(flag){
									out.print(s);
								}
							%>
							</td>
							<td>
							<input type="hidden" id="lbRiseLevel<%=bus.getBusId() %>" value="<%=bus.getRiseLevel()%>" />
							<%if(bus.getRiseLevel()==-99){out.print(MessageUtils.extractMessage("xtgl","xtgl_cswh_ywlxgl_wyxj",request));}else{out.print(bus.getRiseLevel());}%>
							</td>
							<td>
							<input type="hidden" id="lbState<%=bus.getBusId() %>" value="<%=bus.getState()%>" />
							<%
								if(bus.getState()!=null){
									if(bus.getState()==0){
										out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_qy",request));
									}else if(bus.getState()==1)
									{
										out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_jy",request));
									}
								}
							%>
							</td>
							<td>
							<%=bus.getCreateTime()!=null?df.format(bus.getCreateTime()):"" %>
							</td>
							<td><%=bus.getUpdateTime()!=null?df.format(bus.getUpdateTime()):"" %>
							</td>
							<td>
							<%=bus.getDepName()!=null?bus.getDepName():""%>
							</td>
							<td>
							<%
								if(bus.getName()!=null||bus.getUserName()!=null){
									if(bus.getName()!=null){
										out.print(bus.getName());
									}
									if(bus.getUserName()!=null){
										out.print("("+bus.getUserName()+")");
										
									}
								}
							%>
							</td>
							<td class="" >
										<%
										 if(!"".equals(bus.getBusDescription())&&bus.getBusDescription()!=null){
											String st = "";
											if(bus.getBusDescription().length()>8)
											{
												st = bus.getBusDescription().substring(0,8)+"...";
											}else
											{
												st = bus.getBusDescription();
											}
										%>
										<a onclick="javascript:modify(this)">
										  <label id="lbDesc<%=bus.getBusId() %>" class="lbDesc"><xmp><%=bus.getBusDescription()%></xmp></label>
										  <xmp><%=st%></xmp>
										  </a> <%}else{ %>		<%} %>
							</td>
							<%  if(btnMap.get(menuCode+"-3")!=null)  {  %>
							<td><%if(!bus.getBusCode().equals("M00000")) 
								{
								%>
								<a href="javascript:update(<%=bus.getBusId()%>)"><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a>
								<%} %>
							</td>
							<%} %>
						</tr>
						<%
						 	}
						 }else{
						%>
						<tr><td align="center" colspan="11"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/></td></tr>
						<%} %>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="11">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
			
			<div id="addDiv" class="addDiv">
				<table class="addDiv_table">
					<tr class="ywmc_mh_tr">
						<td align="right" class="ywmc_mh_td">
							<emp:message key="xtgl_cswh_ywlxgl_ywmc_mh" defVal="业务名称：" fileName="xtgl"/>
						</td>
						<td class="busNameAdd_td">
							<input type="text"  class="input_bd busNameAdd" name="busNameAdd" id="busNameAdd" maxlength="32"/>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					
					<tr class="ywbm_mh_tr">
						<td align="right" class="ywbm_mh_td">
							<emp:message key="xtgl_cswh_ywlxgl_ywbm_mh" defVal="业务编码：" fileName="xtgl"/>
						</td>
						<td class="busCodeAdd_td">
							<input type="text"  class="input_bd busCodeAdd" name="busCodeAdd" id="busCodeAdd"  maxlength="32"/>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					
					<tr class="ywlx_mh_tr">
						<td align="right" class="ywlx_mh_td">
							<emp:message key="xtgl_cswh_twgl_ywlx_mh" defVal="业务类型：" fileName="xtgl"/>
						</td>
						<td class="busTypeAdd_td">
<%--						<input type="text"  class="input_bd" name="busCode" id="busCode"  maxlength="16"/>--%>
						<select name="busTypeAdd" id="busTypeAdd" class="input_bd busTypeAdd"  >
								<option selected="selected" value=""><emp:message key="xtgl_cswh_twgl_qxz" defVal="请选择" fileName="xtgl"/></option>
								<option value="0"><emp:message key="xtgl_cswh_ywlxgl_sd" defVal="手动" fileName="xtgl"/></option>
								<option value="1"><emp:message key="xtgl_cswh_ywlxgl_cf" defVal="触发" fileName="xtgl"/></option>
								<option value="2"><emp:message key="xtgl_cswh_ywlxgl_sd_cf" defVal="手动+触发" fileName="xtgl"/></option>
							</select>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					
					<tr class="yxjb_mh_tr">
						<td align="right" class="yxjb_mh_td">
							<emp:message key="xtgl_cswh_ywlxgl_yxjb_mh" defVal="优先级别：" fileName="xtgl"/>
						</td>
						<td class="riseLevelAdd_td">
<%--							<input type="text"  class="input_bd" name="busCode" id="busCode"  maxlength="16"/>--%>
							<select  name="riseLevelAdd" id="riseLevelAdd"  class="input_bd riseLevelAdd">
								<option value="-99" selected="selected"><emp:message key="xtgl_cswh_ywlxgl_wyxj" defVal="无优先级" fileName="xtgl"/></option>
								<option value="1">1<emp:message key="xtgl_cswh_ywlxgl_zgyxj" defVal="（最高优先级）" fileName="xtgl"/></option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
								<option value="6">6</option>
								<option value="7">7</option>
								<option value="8">8</option>
								<option value="9">9<emp:message key="xtgl_cswh_ywlxgl_zdyxj" defVal="（最低优先级）" fileName="xtgl"/></option>
							</select>
<%--							<font color="red">*</font>--%>
						</td>
					</tr>
					<tr class="ywms_mh_tr" >
						<td align="right" class="ywms_mh_td">
							<emp:message key="xtgl_cswh_ywlxgl_ywms_mh" defVal="业务描述：" fileName="xtgl"/>
						</td>
						<td class="busDescriptionAdd_td"><textarea  class="input_bd busDescriptionAdd"  name="busDescriptionAdd" id="busDescriptionAdd"></textarea></td>
					</tr>
					<tr>
					<td colspan="2" class="addsubmit_td">
					<input name="addsubmit" id="addsubmit" class="btnClass5 mr23" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" onclick="javascript: addBusType();"/>
					<input name="addcancel" id="addcancel" class="btnClass6" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" onclick="javascript:doCancel();"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td>
					</tr>
				</table>
			</div>
			<div id="bindDiv" class="bindDiv">
				<table class="bindDiv_table">
					<tr>
						<td class="ywlxgl_ywmc_mh_td" align="right">
							<emp:message key="xtgl_cswh_ywlxgl_ywmc_mh" defVal="业务名称 ：" fileName="xtgl"/>
						</td>
						<td class="bindDivBusName_td"><label id="bindDivBusName"></label></td>
					</tr>
					<tr><td class="bindDiv_tr_td" ></td></tr>
					<tr>
						<td align="right">
							<emp:message key="xtgl_cswh_ywlxgl_fszh_mh" defVal="发送账号 ：" fileName="xtgl"/>
						</td>
						<td class="sp_td">
						    <select name="sp" id="sp" class="sp" onchange="">
							    <option value="">--<emp:message key="xtgl_cswh_twgl_qxz" defVal="请选择" fileName="xtgl"/>--</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="ms_mh_td" align="right">
							<emp:message key="xtgl_cswh_ywlxgl_ms_mh" defVal="描述 ：" fileName="xtgl"/>
						</td>
						<td><textarea  rows="3" cols="20" name="busDescriptionBind" id="busDescriptionBind"></textarea></td>
					</tr>
				</table>
			</div>
			<div id="editDiv" class="editDiv">
				<table class="editDiv_table">
					<tr class="editDiv_ywmc_mh_tr">
						<td class="editDiv_ywmc_mh_td" align="right" >
							<emp:message key="xtgl_cswh_ywlxgl_ywmc_mh" defVal="业务名称：" fileName="xtgl"/>
						</td>
						<td class="busNameEdit_td">
							<input class="input_bd busNameEdit" type="text" name="busNameEdit" id="busNameEdit" maxlength="32"/>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
					
					<tr class="editDiv_ywbm_mh_tr">
						<td class="editDiv_ywbm_mh_td" align="right" >
							<emp:message key="xtgl_cswh_ywlxgl_ywbm_mh" defVal="业务编码：" fileName="xtgl"/>
						</td>
						<td class="busCodeEdit_td"><label id="busCodeEdit" class="busCodeEdit"></label></td>
					</tr>
					
					<tr class="editDiv_ywlx_mh_tr">
						<td align="right" class="editDiv_ywlx_mh_td">
							<emp:message key="xtgl_cswh_twgl_ywlx_mh" defVal="业务类型：" fileName="xtgl"/>
						</td>
						<td class="busTypeEdit_td">
<%--							<input class="input_bd" type="text" name="busNameEdit" id="busNameEdit" maxlength="16"/>--%>
							<select name="busTypeEdit" id="busTypeEdit"  class="input_bd busTypeEdit">
								<option value=""><emp:message key="xtgl_cswh_twgl_qxz" defVal="请选择" fileName="xtgl"/></option>
								<option value="0"><emp:message key="xtgl_cswh_ywlxgl_sd" defVal="手动" fileName="xtgl"/></option>
								<option value="1"><emp:message key="xtgl_cswh_ywlxgl_cf" defVal="触发" fileName="xtgl"/></option>
								<option value="2"><emp:message key="xtgl_cswh_ywlxgl_sd_cf" defVal="手动+触发" fileName="xtgl"/></option>
							</select>
							&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>
						</td>
					</tr>
<%--					<tr><td height="3px"></td></tr>--%>
					<tr class="editDiv_yxjb_mh_tr">
						<td class="editDiv_yxjb_mh_td" align="right" >
							<emp:message key="xtgl_cswh_ywlxgl_yxjb_mh" defVal="优先级别：" fileName="xtgl"/>
						</td>
						<td class="riseLevelEdit_td">
							<select name="riseLevelEdit" id="riseLevelEdit" class="input_bd riseLevelEdit">
								<option value="-99" selected="selected"><emp:message key="xtgl_cswh_ywlxgl_wyxj" defVal="无优先级" fileName="xtgl"/></option>
								<option value="1">1<emp:message key="xtgl_cswh_ywlxgl_zgyxj" defVal="（最高优先级）" fileName="xtgl"/></option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
								<option value="6">6</option>
								<option value="7">7</option>
								<option value="8">8</option>
								<option value="9">9<emp:message key="xtgl_cswh_ywlxgl_zdyxj" defVal="（最低优先级）" fileName="xtgl"/></option>
							</select>
<%--							<font color="red">*</font>--%>
						</td>
					</tr>
<%--					<tr><td height="3px"></td></tr>--%>
					<tr class="editDiv_zt_mh_tr">
						<td class="editDiv_zt_mh_td" align="right" >
							<emp:message key="xtgl_cswh_ywlxgl_zt_mh" defVal="状态：" fileName="xtgl"/>
						</td>
						<td class="editDiv_qy_td">
							<input name="stateEdit" type="radio" value="0" />
											<emp:message key="xtgl_spgl_shlcgl_qy" defVal="启用" fileName="xtgl"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="stateEdit" type="radio" value="1" />
											<emp:message key="xtgl_spgl_shlcgl_jy" defVal="禁用" fileName="xtgl"/>
						</td>
					</tr>
					<tr class="editDiv_ywms_mh_tr">
						<td class="editDiv_ywms_mh_td" align="right" >
							<emp:message key="xtgl_cswh_ywlxgl_ywms_mh" defVal="业务描述：" fileName="xtgl"/>
						</td>
						<td ><textarea  class="input_bd busDescriptionEdit" name="busDescriptionEdit" id="busDescriptionEdit"></textarea></td>
					</tr>
					<tr><td colspan="2" class="editsubmit_td">
					<input name="editsubmit" id="editsubmit"  class="btnClass5 mr23" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" onclick="javascript:editBusType()"/>
					<input name="editcancel" id="editcancel"  class="btnClass6" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" onclick="javascript:$('#editDiv').dialog('close')"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td></tr>
				</table>
				
			</div>
        	<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div id="singledetail" class="singledetail">
				<div id="msg" class="msg"><xmp class="msg_xmp"></xmp></div>
			</div>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</form>
		</div>
			
		    <div id="fileUploadDiv" class="bindDiv">
		    <form id="uploadForm"  method="post"  action="<%=path%>/bit_busType.htm?method=uploadBusManage"  enctype="multipart/form-data">
				<table class="addDiv_table">
					<tr class="ywmc_mh_tr">
						<td align="right" class="ywmc_mh_td">
							<emp:message key="xtgl_czygl_gjaqsz_xzwj_mh" defVal="上传文件" fileName="xtgl"/>
						</td>
						<td class="busNameAdd_td">
							<input name="file" type="file" id="uploadFile"  maxlength="32" />
						</td>
					</tr>
					<tr>
					<td colspan="2" class="addsubmit_td">
					<input   id="kwsok" class="btnClass5 mr23" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" onclick="javascript:checkUpload()"/>
					<input  id="kwsc" class="btnClass6" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" onclick="javascript:back();"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td>
					</tr>
				</table>
				</form>
			</div>
			
			
				
		    <div id="resultUploadDiv" class="bindDiv">
				<table class="uploadResult_table">
					<tr class="ywmc_mh_tr">
						<td align="center" class="ywmc_mh_tr_font">
							<emp:message key="xtgl_cswh_ywlxgl_text7" defVal="导入完成，共导入" fileName="xtgl"/><span class="font_green"><%=successNum %></span>
							 <emp:message key="xtgl_cswh_ywlxgl_text8" defVal="条，失败" fileName="xtgl"/><span class="font_red"><%=errorNum %></span>
							 <emp:message key="xtgl_cswh_ywlxgl_text9" defVal="条" fileName="xtgl"/>
						</td>
					</tr>
			    <%if(errorNum!=null&&errorNum>0){ %>
					<tr class="ywmc_mh_tr">
						<td align="center" class="ywmc_mh_tr_font">
							<emp:message key="xtgl_cswh_ywlxgl_text10" defVal="下载导入报告，查看失败原因" fileName="xtgl"></emp:message>
						</td>
					</tr>
					
					<tr >
						<td align="center">
							<a class="ff" href="javascript:download_href('<%=path %>/down.htm?filepath=<%=resultPath%>')">
							<span class="ywmc_mh_tr_a" ><%=resultPath.substring(resultPath.lastIndexOf("/")).substring(1) %></span>
							</a>
						</td>
						
					</tr>
					<%}%>
					<tr class="ywmc_mh_tr_a">
						<td align="center">
						</td>
					</tr>
					<tr class="ywmc_mh_tr">
						<td align="center">
						</td>
					</tr>
				</table>
				<input   id="uploadResult" class="btnClass5 mr23" type="button" value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" onclick="javascript:resultBack()"/>
				
			</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"
			type="text/javascript"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<%-- 为了解决ie8空白双击导致浏览器崩溃，增加日期控件的引用，可解决这问题 --%>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/bit_busType.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		
		var zTree3;
		var setting3;
		var zNodes3 = [];
		//获取机构代码
		setting3 = {									
				async : true,				
				asyncUrl : "bit_busType.htm?method=createDeptTree", //获取节点数据的URL地址
				
				//checkable : true,
			    //checkStyle : "radio",
			    //checkType : { "Y": "s", "N": "s" },
			    isSimpleData : true,
				rootPID : 0,
				treeNodeKey : "id",
				treeNodeParentKey : "pId",
				asyncParam: ["depId"],	
				
				callback: {
						
					click: zTreeOnClick3,					
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree3.getNodeByParam("level", 0);
						   zTree3.expandNode(rootNode, true, false);
						}
					//zTree3.expandAll(false);
					}
				}
		};
		//选中的机构显示文本框
		function zTreeOnClick3(event, treeId, treeNode) {
			if (treeNode) {				
				var pops="";
				var depts ="";
				$("#depNam").attr("value", treeNode.name);	
				$("#deptid").attr("value",treeNode.id);
				
			}
		}	
		$(document).ready(function() {
			closeTreeFun(["dropMenu"]);
			getLoginInfo("#hiddenValueDiv");
			noquot($("#busNameEdit"));
			noquot($("#busNameAdd"));
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
			    title:getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_57"),
			    modal:true,
			    resizable:false
			 });
			 var sure = getJsLocaleMessage("xtgl","xtgl_cswh_whgl_11");
			 var cancle = getJsLocaleMessage("xtgl","xtgl_cswh_whgl_12");
			$("#bindDiv").dialog({
				autoOpen: false,
			    width:300,
			    height:220,
			    title:getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_58"),
			    
			    buttons:{
					sure:function(){
				       bindBusSP();
					},
					cancle:function(){
						 $("#busNameAdd").val("");
			             $("#busCode").val("");
			             $("#busDescriptionAdd").val("");
			             $("#bindDiv").dialog("close");
					}
				},
			    modal:true,
			    resizable:false
			    
			 });
			 $("#editDiv").dialog({
				autoOpen: false,
			    width:500,
			    height:360,
			    title:getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_59"),
			    modal:true,
			    resizable:false
			 });
			$('#add').click(function(){
				$("#addDiv").dialog("open");
			});
			$("#singledetail").dialog({
				autoOpen: false,
				maxWidth: 350,
				maxHeight: 170,
				modal: true
			});
			//机构树
			var lguserid =$("#lguserid").val();
//		    setting3.asyncUrl = "bit_busType.htm?method=createDeptTree&lguserid="+lguserid;
		    setting3.asyncUrl = "bit_busType.htm?method=createDeptTree&lguserid=2";
		    reloadTree(zNodes3);
		    
		    	 
	        $("#resultUploadDiv").dialog({
			autoOpen: false,
		    width:300,
		    height:300,
		    title:getJsLocaleMessage("xtgl","xtgl_ywlx_tishi"),
		    modal:true,
		    resizable:false
		 });
		    if(successNum>0||errorNum>0){
		     $("#resultUploadDiv").dialog("open");
		    }
		  
		});
		// 加载人员/机构树形控件
		function reloadTree(zNodes3) {
			setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
			zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
			zTree3.expandAll(true);
		}
		//选中的机构显示文本框
		function zTreeOnClickOK3() {
			hideMenu();
		}
		//隐藏人员树形控件
		function hideMenu() {
			$("#dropMenu").hide();
		}
		function cleanSelect_dep3()
		{
			var checkNodes = zTree3.getCheckedNodes();
		    for(var i=0;i<checkNodes.length;i++){
		     checkNodes[i].checked=false;
		    }
		    zTree3.refresh();
		    $("#depNam").attr("value", getJsLocaleMessage("xtgl","xtgl_cswh_ywlxgl_60"));	
			$("#deptid").attr("value","");	
		}
		
		</script>
	</body>
</html>
