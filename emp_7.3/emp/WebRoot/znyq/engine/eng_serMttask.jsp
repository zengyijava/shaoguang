<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.engine.vo.LfMttaskVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//@ SuppressWarnings("unchecked")
	//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
	
	@ SuppressWarnings("unchecked")
	List<LfSpDepBind> userList = (List<LfSpDepBind>)request.getAttribute("sendUserList");
	
	@ SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");
	
	@ SuppressWarnings("unchecked")
	List<LfMttaskVo> mtList =(List<LfMttaskVo>) request.getAttribute("mtList");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String titlePath = (String)request.getAttribute("titlePath");
	String menuCode = titleMap.get("serMtTask");
	
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	String recvtime = request.getParameter("recvtime");
	String sendtime = request.getParameter("sendtime");
	String deptid = request.getParameter("depid");
	String busCode = request.getParameter("busCode");
	String userid = request.getParameter("userid");
	String userName = request.getParameter("userName");
	String depNam = request.getParameter("depNam");
	String spUser = request.getParameter("spUser");
	String serId = request.getParameter("serId");
	String serName = request.getParameter("serName");
	String currUserid = request.getParameter("lguserid");
	String runway = request.getParameter("runway");
	String serSendState = request.getParameter("serSendState");
	boolean isBack = request.getParameter("isback")==null?false:true;//是否返回操作
	if(isBack){
		recvtime = (String)request.getAttribute("recvtime");
		sendtime = (String)request.getAttribute("sendtime");
		deptid = (String)request.getAttribute("depid");
		busCode = (String)request.getAttribute("busCode");
		userid = (String)request.getAttribute("userid");
		userName = request.getParameter("userName");
		depNam = request.getParameter("depNam");
		spUser = (String)request.getAttribute("spUser");
		serId = (String)request.getAttribute("serId");
		serName = (String)request.getAttribute("serName");
		serSendState = (String)request.getAttribute("serSendState");
		userName = (String)request.getAttribute("userName");
		depNam = (String)request.getAttribute("depNam");
	}
	int corpType = StaticValue.getCORPTYPE();
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	//String zNodes3 = (String)request.getAttribute("departmentTree");
	//String zNodes2 = (String)request.getAttribute("deptUserTree");
	
	String findResult= (String)request.getAttribute("findresult");
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String httpUrl = StaticValue.getFileServerViewurl();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title>sendSMS.html</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
					
	    <%--<link href="<%=inheritPath %>/styles/easyui.css" rel="stylesheet" type="text/css" />--%>
				
	<%if(StaticValue.ZH_HK.equals(langName)){%>	
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
		
		<style type="text/css">
			div #tooltip{
				_width:expression(this.scrollWidth > 435 ? 435px : auto);
			}
		</style>
		
	</head>

	<body class="eng_serMttask" id="eng_serMttask">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<%-- 表示请求的名称 --%>
			<input type="hidden" name="htmName" id="htmName" value="eng_serMtTask.htm">
			<%-- 短信账号--%>
			<input type="hidden" name="smsAccount" id="smsAccount" value="<%=StaticValue.SMSACCOUNT %>">
			<form name="pageForm" action="eng_serMtTask.htm" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						<a id="exportCondition"><emp:message key="znyq_ywgl_common_btn_18" defVal="导出" fileName="znyq"></emp:message></a>
					</div>
					<div id="condition">
						<table>
						   
							<tbody>
								<tr>
									<td>
										<emp:message key="znyq_ywgl_xxywjl_ywbh_mh" defVal="业务编号：" fileName="znyq"></emp:message>
									</td>									
									<td>											
									  	<input type="text" id="serId" name ="serId" class="commomWidth" value="<%=serId== null?"":serId %>" onkeyup="numberControl($(this))" />
									</td>
									<td>
										<emp:message key="znyq_ywgl_xxywjl_ywmc_mh" defVal="业务名称：" fileName="znyq"></emp:message>
									</td>
									<td>											
										<input type="text" id="serName" name ="serName" class="commomWidth" value="<%=serName== null?"":serName %>" />									   										
									</td>
									<td><emp:message key="znyq_ywgl_xxywjl_fszt_mh" defVal="发送状态：" fileName="znyq"></emp:message></td>
								    <td>
										<select name="serSendState" id="serSendState" isInput="false">
											<option value="" ><emp:message key="znyq_ywgl_common_text_16" defVal="全部" fileName="znyq"></emp:message></option>
								    		<option value="1" <%="1".equals(serSendState)?"selected":"" %> ><emp:message key="znyq_ywgl_xxywjl_wgjscg" defVal="网关接收成功" fileName="znyq"></emp:message></option>
								    		<option value="2" <%="2".equals(serSendState)?"selected":"" %> ><emp:message key="znyq_ywgl_common_text_12" defVal="失败" fileName="znyq"></emp:message></option>
								    		<option value="3" <%="3".equals(serSendState)?"selected":"" %> ><emp:message key="znyq_ywgl_xxywjl_wgclwc" defVal="网关处理完成" fileName="znyq"></emp:message></option>
								    		<%--  <option value="0" <%="0".equals(serSendState)?"selected":"" %> >未发送</option>
											<option value="4" <%="4".equals(serSendState)?"selected":"" %> >EMP发送中</option>
											<option value="6" <%="6".equals(serSendState)?"selected":"" %> >终止发送</option>--%>
								    	</select>
								    </td>	
									<td class="tdSer">
									    <center><a id="search"></a></center>
								    </td>		
								</tr>
								<tr>
									<td>
										<emp:message key="znyq_ywgl_xxywjl_dsjg_mh" defVal="隶属机构：" fileName="znyq"></emp:message>
									</td>									
									<td class="condi_f_l">											
									  		<div class="commonWidth1">	
									  		
									  		 <input type="hidden" id="deptid" name="depid" value="<%=deptid==null?"":deptid%>"/> 		
									  		<%--<input type="text" class="treeInput" id="depNam" name="depNam" value="<%=depNam==null?"请选择":depNam%>" 
									  			onclick="javascript:showMenu();"  readonly style="width:138px;cursor: pointer;"/>&nbsp;--%>
									  		<input type="text" class="treeInput" id="depNam" name="depNam" value="<%=depNam==null?MessageUtils.extractMessage("znyq","znyq_ywgl_common_text_13",request):depNam%>" 
									  			onclick="javascript:showMenu();"  readonly/>&nbsp;
											</div>														
											
											<div id="dropMenu">
												<iframe frameborder="0" scrolling="no" src="about:blank"></iframe>	
												<div>
													<input type="button" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" />&nbsp;&nbsp;
													<input type="button" value="<emp:message key="znyq_ywgl_common_btn_20" defVal="清空" fileName="znyq"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep3();"/>
												</div>	
												<ul  id="dropdownMenu" class="tree"></ul>	
											</div>	
									</td>
									<td>
										<emp:message key="znyq_ywgl_xxywjl__mh" defVal="操作员：" fileName="znyq"></emp:message>
									</td>
									<td class="condi_f_l">											
											<div class="commonWidth1">											
											 <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
											<%--<input type="text" id="userName" class="treeInput" name="userName" value="<%=userName==null?"请选择":userName%>" onclick="javascript:showMenu2();" readonly 
											style="width:138px;cursor: pointer;"/>&nbsp; --%>
											<input type="text" id="userName" class="treeInput" name="userName" value="<%=userName==null?MessageUtils.extractMessage("znyq","znyq_ywgl_common_text_13",request):userName%>" onclick="javascript:showMenu2();" readonly />&nbsp;
											</div>											
											<div id="dropMenu2">
												<iframe frameborder="0" scrolling="no" src="about:blank"></iframe>	
												<div class="firstDiv">
													<input type="button" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK2();"/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key="znyq_ywgl_common_btn_20" defVal="清空" fileName="znyq"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep();"/>
												</div>	
												<div class="secondDiv"><font class="zhu"><emp:message key="znyq_ywgl_xxywjl_z_mh_qgxczyjxcx" defVal="注：请勾选操作员进行查询" fileName="znyq"></emp:message></font></div>
												<ul  id="dropdownMenu2" class="tree"></ul>	
											</div>										   										
									</td>
									<td>
									<%--<%=StaticValue.SMSACCOUNT %>：--%>
									<emp:message key="znyq_ywgl_common_spzh_mh" defVal="SP账号：" fileName="znyq"></emp:message>
									</td>
									  
									<td>	
  		                                 	<label>
											<select name="spUser" id="spUser">
												<option value="">
													<emp:message key="znyq_ywgl_common_text_16" defVal="全部" fileName="znyq"></emp:message>
												</option>
												<%
													  if(userList!=null && userList.size()>0){
													     for(LfSpDepBind userdata:userList){
													      
													 %>
												<option value="<%=userdata.getSpUser() %>"
													<%=userdata.getSpUser().equals(spUser)?"selected":"" %>>
													<%=userdata.getSpUser()%>
												</option>
												<%
													   }
													  }
													 %>
											</select>
										</label>		
									</td>	
									<td ></td>		
								</tr>
								<tr>
							   	   <td>
								        <emp:message key="znyq_ywgl_xxywjl_fssj_mh" defVal="发送时间：" fileName="znyq"></emp:message>
								   </td>
								   <td>
								    	<input type="text"
											readonly="readonly" class="Wdate" onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime"
												 <%--onchange="onblourSendTime('end')" --%>>
								   </td>
								   <td><emp:message key="znyq_ywgl_common_text_15" defVal="至：" fileName="znyq"></emp:message></td>
								   <td>
								      	<input type="text"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=recvtime==null?"":recvtime %>" 
											 id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
								    </td>
								    <td></td>
								    <td></td>
								    <td>&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr align="center">
								<th class="sixTh"><emp:message key="znyq_ywgl_xxywjl_ywbh" defVal="业务编号" fileName="znyq"></emp:message></th>
								<th class="fourTh"><emp:message key="znyq_ywgl_xxywjl_ywmc" defVal="业务名称" fileName="znyq"></emp:message></th>
								<th class="fiveTh">
									<emp:message key="znyq_ywgl_xxywjl_czy" defVal="操作员" fileName="znyq"></emp:message>
								</th>
								<th class="sixTh">
									<emp:message key="znyq_ywgl_xxywjl_dsjg" defVal="隶属机构" fileName="znyq"></emp:message>
								</th>
								<th class="seven1Th">
									<%--<%=StaticValue.SMSACCOUNT %>--%>
									<emp:message key="znyq_ywgl_common_spzh" defVal="SP账号" fileName="znyq"></emp:message>
								</th>
								<th class="tenTh">
									<emp:message key="znyq_ywgl_xxywjl_fssj" defVal="发送时间" fileName="znyq"></emp:message>
								</th>
								<th class="eightTh">
								    <emp:message key="znyq_ywgl_xxywjl_fszt" defVal="发送状态" fileName="znyq"></emp:message>
								</th>
								<th class="sixTh">
									<emp:message key="znyq_ywgl_xxywjl_hmgs" defVal="号码个数" fileName="znyq"></emp:message>
								</th>
								<th class="sixTh">
									<emp:message key="znyq_ywgl_xxywjl_tjxxs" defVal="提交信息数" fileName="znyq"></emp:message>
								</th>
								<th class="eightTh">
									<emp:message key="znyq_ywgl_xxywjl_tjcgs" defVal="发送成功数" fileName="znyq"></emp:message>
								</th>
								<th class="sevenTh">
									<emp:message key="znyq_ywgl_xxywjl_tjsbs" defVal="提交失败数" fileName="znyq"></emp:message>
								</th>
								<th class="eightTh">
									<emp:message key="znyq_ywgl_xxywjl_jssbs" defVal="接收失败数" fileName="znyq"></emp:message>
								</th>
								<th class="sixTh">
								    <emp:message key="znyq_ywgl_xxywjl_xxnr" defVal="信息内容" fileName="znyq"></emp:message>
								</th>
								<th class="nineTh">
								    &nbsp;&nbsp;<emp:message key="znyq_ywgl_xxywjl_fswj" defVal="发送文件" fileName="znyq"></emp:message>&nbsp;&nbsp;
								</th>
								<th class="sevenTh">
								    <emp:message key="znyq_ywgl_common_xq" defVal="详情" fileName="znyq"></emp:message>
								</th>
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="18" align="center"><emp:message key="znyq_ywgl_common_qdjcxhqsj" defVal="请点击查询获取数据" fileName="znyq"></emp:message></td></tr>
							<%
							} else if(mtList != null && mtList.size()>0)
							{
								for(LfMttaskVo mt : mtList)
								{
						%>
						<tr align="center">
							<td>
								<%=mt.getSerId()==null?"-":mt.getSerId() %>
							</td>
							<td>
								<%=mt.getSerName()==null?"-":mt.getSerName() %>
							</td>
							<td class="textalign">
								<%-- <%=mt.getName() %><%if(mt.getUserState()==2){out.print("<font color='red'>(已注销)</font>");} %>--%>
								<%=mt.getName() %><%if(mt.getUserState()==2){out.print("<font color='red'>("+MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_yzx",request)+")</font>");} %>
							</td>
							<td class="textalign">
								<xmp><%=mt.getDepName() %></xmp>
							</td>
							<td>
								<%=mt.getSpUser() %>
							</td>
							<td>
								<%=mt.getTimerTime()==null?"":df.format(mt.getTimerTime()) %><%--发送时间 --%>
							</td>
							<td class="ztalign">
						 	<%
								    String sendState = mt.getSendstate().toString();
						            String error = "";
								    if(sendState.equals("0")){
								       //sendState = "未发送";
								       sendState = MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_wfs",request);
								    }else if(sendState.equals("1")){
								       //sendState = "网关接收成功";
								       sendState = MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_wgjscg",request);
								    }else if(sendState.equals("2")){
								       //sendState = "失败";
								       sendState = MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_sb",request);
								       if(mt.getErrorCodes()!=null && !"".equals(mt.getErrorCodes())){
								    	   error="["+mt.getErrorCodes()+"]";
								       }
								    }else if(sendState.equals("4")){
								       //sendState = "EMP发送中";
								       sendState = MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_empfsz",request);
								    }else if(sendState.equals("6")){
								    	//sendState = "终止发送";
								    	sendState = MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_zzfs",request);
								    }else if(sendState.equals("3"))
								    	{
								    	//sendState = "网关处理完成";
								    	sendState = MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_wgclwc",request);
								    	}else{
								       //sendState = "未使用";
								       sendState = MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_wsy",request);
								    }
								 %>
								 <%=sendState %><%=error %>	
							 </td>
							<td>
								<%=mt.getEffCount() %>
							</td>
							<td>
								<%=mt.getSendstate()==2?(mt.getIcount()==null?"0":mt.getIcount()):(mt.getIcount2()==null?"-":mt.getIcount2()) %>
							</td>
							<td>
								<%
								String fail_count=(mt.getFaiCount()==null?"0":mt.getFaiCount());
								String icount=(mt.getIcount2()==null?"0":mt.getIcount2());
								
								//提交总数
								Long icount1=Long.parseLong(icount);
								//提交失败总数
								Long fail=Long.parseLong(fail_count);
								long suc=icount1-fail;
								if(mt.getSendstate()==2)
								{
									suc=0;
									out.print(suc);
								}
								else if(mt.getIcount2()==null)
								{
									out.print("-");
								}
								else
								{
									out.print(suc);
								}
								%>
							</td>
							<td>
								<%=mt.getSendstate()==2?(mt.getIcount()==null?0:mt.getIcount()):(mt.getIcount2()==null?"-":mt.getFaiCount()) %>
							</td>
							<td>
								<%=mt.getSendstate()==2?"0":(mt.getIcount2()==null?"-":(mt.getRFail2()==null?"-":mt.getRFail2()))%>
							</td>
							<td  class="textalign">
							<%--<%if(mt.getMsg()==null || "".equals(mt.getMsg())){out.print("详见文件");}else{ --%>
							<%if(mt.getMsg()==null || "".equals(mt.getMsg())){out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_xjwj",request));}else{ %>
									<a onclick="javascript:modify(this)">
								  <label class="hiddenValueDiv"><xmp><%=mt.getMsg()%></xmp></label>
											<xmp><%=mt.getMsg().length()>5?mt.getMsg().substring(0,5)+"...":mt.getMsg() %></xmp>
								  </a> 
							<%} %>
							</td>
							<td>
								<%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) == null ){ %>	
								 <label>-</label>
								<%}else{ %>									
								 <a href="javascript:checkFileOuter('<%=mt.getMobileUrl()%>','<%=request.getContextPath()%>')"><emp:message key="znyq_ywgl_common_ck" defVal="查看" fileName="znyq"></emp:message></a>
									&nbsp;
								  <a href="javascript:downloadFilesOuter('<%=mt.getMobileUrl()%>','<%=request.getContextPath()%>')"><emp:message key="znyq_ywgl_common_btn_19" defVal="下载" fileName="znyq"></emp:message></a>
								<%} %>								
							</td>

												

							<td>
							
							  <%if(mt.getSendstate().toString().equals("2")){ %>
							     -
							  <%} else{%>
							  <a onclick="javascript:location.href='eng_serMtTask.htm?method=findAllSendInfo&mtid=<%=mt.getMtIdCipher() %>&type=0'"><emp:message key="znyq_ywgl_xxywjl_fs" defVal="详情" fileName="znyq"></emp:message></a>
							  <%} %>
							</td>			
						</tr>
						<%
								}
							}else{
						%>
						<tr><td align="center" colspan="18"><emp:message key="znyq_ywgl_common_text_1" defVal="无记录" fileName="znyq"></emp:message></td></tr>
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
					<div id="smssendparams" class="hidden"></div>
				</form>
			</div>
			<div id="detailDialog">
			</div>
			<%-- 内容结束 --%>
			<div id="modify" title="<emp:message key="znyq_ywgl_xxywjl_xxnr" defVal="信息内容" fileName="znyq"></emp:message>">
				<table>
					<thead>
						<tr class="firstTr">
							<td id="msgcontTd">
								<span><xmp id="msgcont"></xmp></span>
							</td>
						</tr>
					   <tr class="secondTr">
							<td>
							</td>
							</tr>
						 
					</thead>
				</table>
			</div>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
				</div>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/eng_serMttaskTree.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/eng_serMttask.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
		    // getLoginInfo("#smssendparams");
		    //参数是要隐藏的下拉框的div的id数组，
		    closeTreeFun(["dropMenu2","dropMenu"]);
			var findresult="<%=findResult%>";
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
		       alert(getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_jzymsb"));	
		       return;			       
		    }
			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			
			//导出全部数据到excel
			$("#exportCondition").click(
			function()
			{
				
				//if(confirm("确定要导出数据到excel?"))
				if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_qdydcsjde")))
				 {
			   	    var langName ='<%=langName %>';	
			   	    var spuser = '<%=spUser!=null?spUser:""%>';			   	    
			   	   	var lgcorpcode =$("#lgcorpcode").val();
					var lguserid =$("#lguserid").val(); 			
			   		<%
			   		if(mtList!=null && mtList.size()>0){
			   		  if(pageInfo.getTotalRec()<=500000){
			   		%>			
			   		      $.ajax({
								type: "POST",
								url: "eng_serMtTask.htm?method=ReportMtSerExcel",
								data: {
								langName:langName,
								spuser:spuser,
								lgcorpcode:lgcorpcode,lguserid:lguserid
								},
								beforeSend: function(){
									page_loading();
								},
				                complete:function () {
									page_complete();
				                },
								success: function(result){
									if(result=='true'){		
										download_href("eng_serMtTask.htm?method=downloadFile&down_session=ReportMtSerExcel");	                    	
				                    }else{
				                        //alert('导出失败！');
				                        alert(getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_dcsb"));
				                    }
					   			}
							});
			   		      
			   		
			   		<%	
			   		}else{%>
			   		    //alert("数据量超过导出的范围50万，请从数据库中导出！");
			   		    alert(getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_sjlcgdcdfw50w"));
			   		<%}
			   		}else{
			   		%>
			   		    //alert("无数据可导出！");
			   		    alert(getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_wsjkdc"));
			   		<%
			   		}%>
				}				 
			});
			
		});
	</script>
	</body>
</html>
