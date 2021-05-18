<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Calendar"%>
<%@page import="java.util.LinkedHashMap"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.shorturl.surlmanage.vo.LfNeturlVo"%>
<%@page import="com.montnets.emp.shorturl.surlmanage.vo.LfOperNeturlVo"%>
<%@page import="java.util.Date"%>
<%
	String langName = (String)session.getAttribute("emp_lang");
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.indexOf("/",1));
	
	
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String menuCode = titleMap.get("audit");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	
	@SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String,String>)request.getAttribute("conditionMap");
	//    连接地址名 ,连接地址,审核状态,企业编号,企业名称  , 开始时间    ,结束时间
	String urlname, srcurl,ispass,corpnum,cropname,startTime,recvtime;
	urlname = conditionMap.get("urlname")==null?"":conditionMap.get("urlname");
	srcurl = conditionMap.get("srcurl")==null?"":conditionMap.get("srcurl");
	ispass = conditionMap.get("ispass")==null?"":conditionMap.get("ispass");
	corpnum = conditionMap.get("corpnum")==null?"":conditionMap.get("corpnum");
	cropname = conditionMap.get("cropname")==null?"":conditionMap.get("cropname");
	startTime = conditionMap.get("startTime")==null?"":conditionMap.get("startTime");
	recvtime = conditionMap.get("recvtime")==null?"":conditionMap.get("recvtime");
	
	
	@SuppressWarnings("unchecked")
	List<LfOperNeturlVo> neturlVos = (List<LfOperNeturlVo>) request.getAttribute("urlList");
	
	//审核人map
	@SuppressWarnings("unchecked")
	Map<String, String> usersMap  = (Map<String, String>) request.getAttribute("usersMap");
	
	@SuppressWarnings("unchecked")
	List<String> urlNameList = (List<String>) request.getAttribute("urlNameList");
	
	
    String findResult= (String)request.getAttribute("findresult");
    CommonVariables  CV = new CommonVariables();
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>短链地址审核</title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		
	</head>
	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
			
			<%-- 内容开始 --%>
			<%
				if (btnMap.get(menuCode + "-0") != null)
				{
			%>
			<div id="rContent" class="rContent">
				<input type="hidden" value="<%=inheritPath %>" id="inheritPath"/>
				<form name="pageForm" action="surl_audit.htm?method=find" method="post"
					id="pageForm">
					<input type="hidden" id="deptString" name="deptString"
						value="<%=request.getParameter("deptString") == null ? "" : request.getParameter("deptString")%>" />
					<div id="r_sysMoRparams" class="hidden"></div>
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									
									<td>
										长链接名称：
									</td>
									<td>
									<input type="text" id="urlname" name ="urlname" style="width: 180px" value="<%= (urlname != null && !"".equals(urlname))?urlname:""%>" maxlength="250">
									<%-- 
										<select id="urlname" name="urlname">
											<option value="">全部</option>
											<%
											if(urlNameList.size()>0){
												for(String str : urlNameList ){
													if(!"".equals(urlname)&&urlname.equals(str)){
													%>
											<option value="<%=str %>" selected="selected"><%=str %></option>													
													<%
													}else{
													%>
											<option value="<%=str %>"><%=str %></option>													
													<%
													}
												}
											}
											%>
										
										</select> --%>
									</td>
									<td>
										长链接：
									</td>
									<td>
										<input type="text" value="<%=srcurl==null?"":srcurl %>" id="srcurl"
											name="srcurl" />
									</td>
									<td>
										审核状态：
									</td>
									<td>
										<label>
										<select id="ispass" name="ispass" isInput="false">
											<option value="">全部</option>
											<option value="0" <%=ispass.equals("0")?"selected":"" %> >待审批</option>
											<option value="2" <%=ispass.equals("2")?"selected":"" %>>审批通过</option>
											<option value="3" <%=ispass.equals("3")?"selected":"" %>>审批未通过 </option>
											<option value="-2" <%=ispass.equals("-2")?"selected":"" %> >已禁用</option>
										</select>
										</label>
									</td>
									<td class="tdSer">
									             <center><a id="search"></a></center>
								    </td>
								</tr>
								<tr>
									<td>
										企业编号：
									</td>
									
									<td>
										<input type="text" value="<%=corpnum==null?"":corpnum %>" id="corpnum" name="corpnum" />
									</td>
 									<td>
										企业名称：
									</td>
									<td>
										<input type="text" value="<%=cropname==null?"":cropname %>" id="cropname" name="cropname" />
									</td>
									<td>
										
									</td>
									<td>
									</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
								<td>
										创建时间：
									</td>
									<td class="tableTime">

										<input type="text"
											style="cursor: pointer; width: 181px; background-color: white;"
											class="Wdate" readonly="readonly" onclick="sedtime()"
											value="<%=startTime == null ? "" : startTime%>" id="startTime"
											name="startTime">
									</td>
									<td>
										至：
									</td>
									<td>
										<input type="text"
											style="cursor: pointer; width: 181px; background-color: white;"
											class="Wdate" readonly="readonly" onclick="revtime()"
											value="<%=recvtime == null ? "" : recvtime%>" id="recvtime"
											name="recvtime">
									</td>
									
									<td colspan=2>&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
							  <th>企业名称</th>
							  <th>企业编号</th>
							  <th>长链接名称</th>
							  <th>长链接</th>
							  <th>长链接内容描述</th>
							  <th>创建人</th>
							  <th>创建时间</th>
							  <th>运营商审批状态</th>
							  <th>审核人</th>
							  <th>操作</th>
							</tr>
						</thead>
						<tbody>
							<%if(neturlVos != null && neturlVos.size()>0){
								for(LfOperNeturlVo lfVo :neturlVos){
								%>
								<tr>
									<td>
										<%=lfVo.getCorpname() %>
										<input type="hidden" name="cppname<%=lfVo.getId()%>" id="cppname<%=lfVo.getId()%>" value="<%=lfVo.getCorpname()%>"/>
									</td>
									<td>
										<%=lfVo.getCorpcode() %>
										<input type="hidden" name="cppnum<%=lfVo.getId()%>" id="cppnum<%=lfVo.getId()%>" value="<%=lfVo.getCorpcode()%>"/>
									</td>
									<td>
										<%=lfVo.getUrlname() %>
										<input type="hidden" name="cppurlnam<%=lfVo.getId()%>" id="cppurlnam<%=lfVo.getId()%>" value="<%=lfVo.getUrlname()%>"/>
									</td>
									<td>
										<label><a href="javascript:showUrl(<%=lfVo.getId()%>);"><%=lfVo.getSrcurl().length()>50?lfVo.getSrcurl().substring(0,50)+"...":lfVo.getSrcurl()  %></a></label>
										<input type="hidden" name="toUrl<%=lfVo.getId() %>" id="toUrl<%=lfVo.getId() %>" value="<%=lfVo.getSrcurl() %>"/>
									</td>
									<td>
									<a onclick=javascript:modify(this)>
											<%
											String xmessage = lfVo.getUrlmsg()  == null?"" : lfVo.getUrlmsg();
											%>
								  			<label style="display:none"><xmp><%=xmessage %></xmp></label>
											<xmp><%=xmessage.length()>5?xmessage.substring(0,5)+"...":xmessage %></xmp>
										</a> 
										<input type="hidden" name="cppurlmsg<%=lfVo.getId()%>" id="cppurlmsg<%=lfVo.getId()%>" value="<%=lfVo.getUrlmsg()%>"/>
									</td>
									<td>
										<%=lfVo.getCreateuser() %>
										<input type="hidden" name="cppurlcu<%=lfVo.getId()%>" id="cppurlcu<%=lfVo.getId()%>" value="<%=lfVo.getCreateuser()%>"/>
									</td>
									<td>
										<%=df.format(lfVo.getCreatetm()) %>
										<input type="hidden" name="cppurltm<%=lfVo.getId()%>" id="cppurltm<%=lfVo.getId()%>" value="<%=df.format(lfVo.getCreatetm())%>"/>
									</td>
									
									<td>
										<%
										String isstr = "";
											if(lfVo.getIspass()==0){
												isstr = "待审批";
											}else if(lfVo.getIspass()==1){
												isstr = "无需审批";
											}else if(lfVo.getIspass()==2){
												isstr = "审批通过";
											}else if(lfVo.getIspass()==3){
												isstr = "审批未通过 ";
											}else if(lfVo.getIspass()==-2||lfVo.getIspass()==-3){
												isstr = "已禁用";
											}
											out.print(isstr);
										%>
										<input type="hidden" value="<%=lfVo.getRemarks()%>" id="remark<%=lfVo.getId() %>" name="remark<%=lfVo.getId() %>"/>
										<input type="hidden" value="<%=lfVo.getRemarks1()%>" id="remark1<%=lfVo.getId() %>" name="remark1<%=lfVo.getId() %>"/>
										<input type="hidden" value="<%=lfVo.getIspass() %>" id="urlispass<%=lfVo.getId() %>" name="urlispass<%=lfVo.getId() %>">
									</td>
									<td>
										<%
										if(usersMap!=null&&usersMap.size()>0){
											String aud = lfVo.getAudituid().toString();
											String name = usersMap.get(aud);
											if(name!=null&&!"".equals(name)){
												out.print(name);
												
											}else{
												out.print("");
											}
										}
										%>
									</td>
									
									<td>
										<%if(lfVo.getIspass()==0 && lfVo.getUrlstate() !=-2 && lfVo.getUrlstate() !=-1){//待审批，不可禁用，可审核 %>
											<a id="check" onclick="javascript:verify(<%=lfVo.getId()%>);">审核</a>
											<font>禁用</font>
										<%}else if(lfVo.getIspass()==2) {//审批通过，可禁用
											%>
											<a id="check" onclick="javascript:check(<%=lfVo.getId()%>);">查看</a>
											<a id="stop" onclick="javascript:toStop(<%=lfVo.getId() %>)">禁用</a>
											<%
										}else if(lfVo.getIspass()==3){//审批不通过,不可禁用，可查看
											%>
											<a id="check" onclick="javascript:check(<%=lfVo.getId()%>);">查看</a>
											<font>禁用</font>
											<%
										}else if(lfVo.getIspass()==-2||lfVo.getIspass()==-3){//禁用， 不可重复禁用，可查看
											%>
											<a id="check" onclick="javascript:check(<%=lfVo.getId()%>);">查看</a>
											<font>禁用</font>
											<%
										}else{
											out.print("--");
										}%>
										
									</td>		
								</tr>
								<%
								}
							}else {
								%>
								<tr><td colspan="10" align="center">无记录</td></tr>
								<%	
							}
								%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="10">
								    <input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>					
				</form>
			</div>
			<%
				}
			%>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
		</div>
		<%-- foot结束 --%>
		<div id="singledetail" style="padding:5px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
		</div>
		
		<div id="showUrl" style="display:none;" >
			<iframe  id ="kkuu" name="kkuu" style="padding:5px;height: 337px;overflow: auto;" src="" ></iframe>
		</div>
		 
		 
		<div id="stopDiv" style="display:none">
			<table style="width:100%;height:100%;font-size: 12px; ">
				<tr>
					<td align="right" >
						长链接名称：
					</td>
					<td colspan="3">
						<input type="text" readonly="readonly" id="stopurlname" value="" style="border: none; width: 370px;"/>
					</td>
				</tr>
				<tr >
					<td  align="right" >
						长链接：
					</td>
					<td  colspan="3" >
						<a href="javascript:showStopurl();" id="stopsrcu" style="color: #0e5ad1"></a>
						<input type="hidden"  id="stopsrcurl" value=""/>
					</td>
				</tr>
				<tr >
					<td align="right" style="padding-left: 15px;">
						长链接内容描述：
					</td>
					<td  colspan="3" >
						<textarea rows="4" readonly="readonly" style="width: 370px;" id="stopurlmsg"></textarea>
						<%-- <input type="text"  readonly="readonly" style="border: none;" id="stopurlmsg" value=""/> --%>
					</td>
				</tr>
				<tr>
					<td align="right" >
						状态：
					</td>
					<td  colspan="3">
						<input type="radio" id="stoptg1" name="stoptg1" value="-3"> 禁用
					</td>
				</tr>
				<tr>
					<td align="right" >禁用意见：</td>
					<td  colspan="3">
						<textarea style="height:100px;width: 370px; line-height: 18px;" class="input_bd"  name="urlDescriptionStop" id="urlDescriptionStop"></textarea>
					</td>
				</tr >
				<tr>
					<td colspan="4" style="text-align:center">
						<input name="stopcancel" id="stopcancel" class="btnClass6" type="button" value="取消" onclick="javascript:doCancelStop();"/>
						<input name="stopysubmit" id="stopysubmit" class="btnClass5 mr23" type="button" value="确定" onclick="javascript:stopUrl();"/>
						<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
						<br/>
					</td>
				</tr>
				
				
			</table>
		</div> 
		<div id="checkDiv" style="display:none">
				<table style="width:100%;height:100%;font-size: 12px; ">
					<tr >
						<td  align="right" >
							长链接名称：
						</td>
						<td colspan="3">
							<input type="text" readonly="readonly" id="checkurlname" value="" style="border: none; width: 370px;"/>
						</td>
					</tr>
					<tr >
						<td  align="right" >
							长链接地址：
						</td>
						<td  colspan="3" >
							<a href="javascript:showresurl();" id="checksrcu" style="color: #0e5ad1"></a>
							<input type="hidden"  id="checksrcurl" value=""/>
						</td>
					</tr>
					<tr >
						<td align="right" style="padding-left: 15px;">
							长链接内容描述：
						</td>
						<td  colspan="3" >
						<textarea rows="4" readonly="readonly" style="width: 370px;" id="checkurlmsg"></textarea>
						<%-- 	<input type="text"  readonly="readonly" style="border: none;" id="checkurlmsg" value=""/> --%>
						</td>
					</tr>
					<tr >
						<td align="right" >
							企业名称：
						</td>
						<td >
							<input type="text" readonly="readonly" style="border: none;" id="checkcorpname" value=""/>
						</td>
						<td align="right" >
							企业编号：
						</td>
						<td>
							<input type="text" readonly="readonly" style="border: none;" id="checkcorpcode" value="">
						</td>
					</tr>
					<tr >
						<td align="right" >
							创建人：
						</td>
						<td>
							<input type="text" readonly="readonly" style="border: none;" id="checkcrusr" value="">
						</td>
						<td align="right" >
							创建时间：
						</td>
						<td>
							<input type="text" readonly="readonly" style="border: none;" id="checkurlcreatime" value="">
						</td>
					</tr>
					<tr>
						<td align="right" >
							审批结果：
						</td>
						<td  colspan="3">
							<input type="radio" onclick="return false;" id="checjtg1" name="tongguo" value="2"> 通过
							<input type="radio" onclick="return false;" id="checjtg2" name="tongguo" value="3"/> 不通过
						</td>
						
					</tr>
					<tr>
						<td align="right" >审批意见：</td>
						<td  colspan="3">
							<textarea readonly="readonly" style="height:100px;width: 370px; line-height: 18px;" class="input_bd"  name="urlDescriptionAdd" id="urlDescriptionAdd"></textarea>
						</td>
						
					</tr >
					<tr>
					<td colspan="4" style="text-align:center">
					<input name="checkcancel" id="checkcancel" class="btnClass6" type="button" value="返回" onclick="javascript:doCancelCheck();"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td>
					</tr>
				</table>
			</div>
			 <div id="modify" title="长链接内容描述"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
				<table width="240px">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td>
								<span style="display:block;width:240px;"><label id="msg2" style="width:100%;height:100%;"></label></span>
								 
							</td>
							 
						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
			<div id="verifyDiv" style="display:none">
				<table style="width:100%;height:100%;font-size: 12px; ">
					<tr>
						<td colspan="4" align="center">
							<font color="red">请审核人员务必打开链接核对内容是否与描述一致！</font>
						</td>
					</tr>
					<tr >
						<td  align="right" >
							长链接名称：
						</td>
						<td colspan="3">
							<input type="text" readonly="readonly" id="verifyurlname" value="" style="border: none; width: 370px;"/>
						</td>
					</tr>
					<tr >
						<td  align="right" >
							长链接：
						</td>
						<td  colspan="3" >
							
							<a href="javascript:showCUrl();" id="verysrcu" style="color: #0e5ad1"></a>
							<input type="hidden"  id="verifysrcurl" value=""/>
						</td>
					</tr>
					<tr >
						<td align="right" style="padding-left: 15px;">
							长链接内容描述：
						</td>
						<td  colspan="3" >
						<textarea rows="4" readonly="readonly" style=" width: 370px;" id="verifyurlmsg"></textarea>
						<%-- 	<input type="text"  readonly="readonly" style="border: none; width: 370px;" id="verifyurlmsg" value=""/> --%>
						</td>
					</tr>
					<tr >
						<td align="right" >
							企业名称：
						</td>
						<td >
							<input type="text" readonly="readonly" style="border: none;" id="verifycorpname" value=""/>
						</td>
						<td align="right" >
							企业编号：
						</td>
						<td>
							<input type="text" readonly="readonly" style="border: none;" id="verifycorpcode" value="">
						</td>
					</tr>
					<tr >
						<td align="right" >
							创建人：
						</td>
						<td>
							<input type="text" readonly="readonly" style="border: none;" id="verifycrusr" value="">
						</td>
						<td align="right" >
							创建时间：
						</td>
						<td>
							<input type="text" readonly="readonly" style="border: none;" id="verifyurlcreatime" value="">
						</td>
					</tr>
					<tr>
						<td align="right" >
							审批结果：
						</td>
						<td  colspan="3">
							<input type="radio"  id="verifytg1" checked="checked" name="verifytg" value="2"> 通过
							<input type="radio"  id="verifytg2"  name="verifytg" value="3"/> 不通过
						</td>
						
					</tr>
					<tr>
						<td align="right" >审批意见：</td>
						<td  colspan="3">
							<textarea style="height:100px;width: 370px; line-height: 18px;" class="input_bd"  name="verifyDescription" id="verifyDescription"></textarea>
						</td>
						
					</tr >
					<tr>
					<td colspan="4" style="text-align:center">
					<input name="verifycancel" id="verifycancel" class="btnClass6" type="button" value="取消" onclick="javascript:doCancelVerify();"/>
					<input name="verifysubmit" id="verifysubmit" class="btnClass5 mr23" type="button" value="确定" onclick="javascript: verifyUrl();"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td>
					</tr>
				</table>
			</div>
			
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/frame/frame2.5/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {

		    //处理掉input框keyup与blur事件
            $('#condition input[type="text"]').unbind('keyup blur');

			getLoginInfo("#hiddenValueDiv");

			//要改
			//noquot($("#busNameEdit"));
			//noquot($("#busNameAdd"));

			
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
			
			
			$("#singledetail").dialog({
				autoOpen: false,
				maxWidth: 350,
				maxHeight: 170,
				modal: true
			});

			
			$("#checkDiv").dialog({
				autoOpen: false,
			    width:560,
			    height:428,
			    title:"长链接内容审核查看",
			    modal:true,
			    resizable:false
			 });
			 
			$("#verifyDiv").dialog({
				autoOpen: false,
			    width:560,
			    height:418,
			    title:"长链接内容审核",
			    modal:true,
			    resizable:false
			 });
			
			$("#stopDiv").dialog({
				autoOpen: false,
			    width:500,
			    height:318,
			    title:"长链接禁用",
			    modal:true,
			    resizable:false
			 });

		});


		</script>
	<script type="text/javascript" src="<%=iPath %>/js/url_operUrlAudit.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
