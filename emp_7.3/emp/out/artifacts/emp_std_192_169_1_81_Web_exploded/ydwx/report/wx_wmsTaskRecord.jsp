<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.netnews.vo.LfMttaskVo"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="com.montnets.emp.common.constant.*" %>
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
	String menuCode = titleMap.get(titlePath);
	
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	String recvtime = request.getParameter("recvtime");
	String sendtime = request.getParameter("sendtime");
	String deptid = request.getParameter("depid");
	String busCode = request.getParameter("busCode");
	String userid = request.getParameter("userid");
	String userName = request.getParameter("userName");
	String depNam = request.getParameter("depNam");
	String spUser = request.getParameter("spUser");
	String title = request.getParameter("title");
	
	String netid = request.getParameter("netid");
	String netname = request.getParameter("netname");
	
	String staskid =request.getParameter("taskid");
	
	String currUserid = request.getParameter("lguserid");
	String skip=(String)request.getAttribute("skip");
	if("true".equals(skip)){
		recvtime=(String)request.getAttribute("recvtime");
		sendtime=(String)request.getAttribute("sendtime");
		deptid=(String)request.getAttribute("depid");
		busCode=(String)request.getAttribute("busCode");
		userid=(String)request.getAttribute("userid");
		userName=(String)request.getAttribute("userName");
		depNam=(String)request.getAttribute("depNam");
		spUser=(String)request.getAttribute("spUser");
		title=(String)request.getAttribute("title");
		netid=(String)request.getAttribute("netid");
		netname=(String)request.getAttribute("netname");
		staskid=(String)request.getAttribute("taskid");
	}
	
	
	//是否包含子机构
	String isContainsSun=(String)request.getAttribute("isContainsSun");
	if(isContainsSun==null||"".equals(isContainsSun)){
		isContainsSun="1";
	}
	
	int corpType = StaticValue.getCORPTYPE();
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	String zNodes3 = (String)request.getAttribute("departmentTree");
	//String zNodes2 = (String)request.getAttribute("deptUserTree");
	
	String findResult= (String)request.getAttribute("findresult");
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String httpUrl = StaticValue.getFileServerViewurl();
	String langName = (String)session.getAttribute("emp_lang");
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
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
		<style type="text/css">
			body div#tooltip { position:absolute; z-index:1000; max-width:435px; _width:expression(this.scrollWidth > 435 ? "435px" : "auto");  width:auto; background:#A8CFF6; border:#FEFFD4 solid 1px; text-align:left; padding:6px;}
			body div#tooltip p { margin:0;padding:6; color:#FFFFFE;font:12px verdana,arial,sans-serif;}
			body div#tooltip p em { display:block;margin-top:3px; color:#f60;font-style:normal; font-weight:bold;}
			#msgcont {
                white-space: pre-wrap;
                *white-space: pre;
                *word-wrap: break-word;
                word-break: break-all;
            }
			#content > tbody > tr > td{
				width: 5%;
			}
			#content > tbody > tr > > td:nth-child(7),#content > tbody > tr > td:nth-child(16){
				width: 7%;
			}
			#content > tbody > tr > td:nth-child(8){
				width: 11%;
			}
		</style>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style type="text/css">
			#content > tbody > tr > td:nth-child(4),
			#content > tbody > tr > td:nth-child(5),
			#content > tbody > tr > td:nth-child(7),
			#content > tbody > tr > td:nth-child(15){
				width: 4%;
			}
			#content > tbody > tr > td:nth-child(9),
			#content > tbody > tr > td:nth-child(16){
				width: 7%;
			}
			#content > tbody > tr > td:nth-child(8),
			#content > tbody > tr > td:nth-child(17){
				width: 9%;
			}
			#content > tbody > tr > td:nth-child(1){
				width: 3%;
			}
		</style>
		<%}%>
		<link href="<%=iPath%>/css/wmsTaskRecord.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin%>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
	</head>

	<body id="ydwx_wmsTaskRecord">
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=ViewParams.getPosition(empLangName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<%-- 表示请求的名称 --%>
			<input type="hidden" name="htmName" id="htmName" value="wx_<%=titlePath %>.htm">
			<%
				if(CstlyeSkin.contains("frame4.0")){
			%>
				<input id='hasBeenBind' value='1' type='hidden'/>
			<%
				}
			 %>
			<form name="pageForm" action="wx_<%=titlePath %>.htm" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						<a id="exportCondition"><emp:message key="ydwx_common_btn_daochu" defVal="导出" fileName="ydwx"></emp:message></a>
					</div>
					<div id="condition">
						<table>
						   
							<tbody>
							
								<tr>
								  <td><emp:message key="ydwx_wxgl_wxbh" defVal="网讯编号：" fileName="ydwx"></emp:message></td>
								    <td><input type="text" id="netid" name ="netid" class="ydwx_netid" onkeyup="numberControl($(this))" onblur="numberControl($(this))" value="<%=netid== null?"":netid %>"></td>
								     <td><emp:message key="ydwx_wxgl_wxmc" defVal="网讯名称：" fileName="ydwx"></emp:message></td>
								    <td><input type="text" id="netname" name ="netname" class="ydwx_netname" value="<%=netname== null?"":netname %>"></td>
								  	<td><emp:message key="ydwx_wxcxtj_fstj_fszts" defVal="发送主题：" fileName="ydwx"></emp:message></td>
								    <td><input type="text" id="title" name ="title" class="ydwx_title" value="<%=title== null?"":title %>"></td>
										<td class="tdSer">
									    <center><a id="search"></a></center>
								    </td>		
									</tr>
									<tr>

								    
								    <td>
										<emp:message key="ydwx_wxcxtj_fstj_lshjgs" defVal="隶属机构：" fileName="ydwx"></emp:message>
									</td>									
									<td class="condi_f_l">											
									  		<div class="ydwx_dep_dv">	
									  		
									  		 <input type="hidden" id="deptid" name="depid" value="<%=deptid==null?"":deptid%>"/> 		
									  		<input type="text" class="treeInput ydwx_depNam" id="depNam" name="depNam" value="<%=depNam==null?MessageUtils.extractMessage("ydwx","ydwx_jsp_out_qingxuanze",request):depNam%>" 
									  			onclick="javascript:showMenu();" readonly/>&nbsp;
											</div>														
											
											<div id="dropMenu" >
												<iframe class="ydwx_dropMenu_iframe" frameborder="0" src="about:blank"></iframe>	
												<div class="ydwx_dropMenu_sub">
													<input type="checkbox" id="isContainsSun" name="isContainsSun" <%if(isContainsSun.equals("1")){%>checked="checked" <%}%> value="1" class="isContainsSun"/>
													<emp:message key="ydwx_wxcxtj_fstj_bhzjg" defVal="包含子机构" fileName="ydwx"></emp:message>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK3();"/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='common_clean' defVal='清空' fileName='common'></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep3();"/>
												</div>	
												<ul  id="dropdownMenu" class="tree"></ul>	
											</div>	
									</td>
									  <td>
										<emp:message key="ydwx_wxcxtj_fstj_czys" defVal="操作员：" fileName="ydwx"></emp:message>
									</td>
									<td class="condi_f_l">											
											<div class="ydwx_dep_dv1">											
											 <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
											<input type="text" id="userName" class="treeInput ydwx_userName" name="userName" value="<%=userName==null?MessageUtils.extractMessage("ydwx","ydwx_jsp_out_qingxuanze",request):userName%>" onclick="javascript:showMenu2();" readonly />&nbsp;
											</div>											
											<div id="dropMenu2">
												<iframe class="ydwx_dropMenu2_iframe" frameborder="0" src="about:blank"></iframe>	
												<div class="ydwx_dropMenu2_sub">
													<input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK2();"/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='common_clean' defVal='清空' fileName='common'></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep();"/>
												</div>	
												<div class="ydwx_note_dv"><font class="zhu">
												<emp:message key="ydwx_wxcxtj_fstj_czytsh" defVal="注：请勾选操作员进行查询" fileName="ydwx"></emp:message></font></div>
												<ul  id="dropdownMenu2" class="tree"></ul>	
											</div>										   										
									</td>
									<td><emp:message key="ydwx_common_SPzhanghaos" defVal="SP账号：" fileName="ydwx"></emp:message></td>
									  
									<td>	
  		                                 	<label>
											<select name="spUser"id="spUser" class="ydwx_spUser">
												<option value="">
													<emp:message key="ydwx_wxgl_quanbu" defVal="全部" fileName="ydwx"></emp:message>
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
									<td></td>
									</tr>
								<tr>
								<td>
										<emp:message key="ydwx_wxcxtj_fstj_rwpcs" defVal="任务批次：" fileName="ydwx"></emp:message>
									</td>
									<td>
										<input type="text" class="ydwx_taskid"  value='<%=staskid==null?"":staskid %>' id="taskid" name="taskid" onkeyup="javascript:numberControl($(this))" onblur="javascript:numberControl($(this))"/>
									</td>
							   	   <td>
								       <emp:message key="ydwx_common_time_fasongshijian" defVal="发送时间：" fileName="ydwx"></emp:message> 
								   </td>
								   <td>
								    	<input type="text"
											readonly="readonly" class="Wdate ydwx_sendtime" onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime"
												 <%--onchange="onblourSendTime('end')" --%>>
								   </td>
								   <td><emp:message key="ydwx_common_time_zhi" defVal="至：" fileName="ydwx"></emp:message></td>
								   <td>
								      	<input type="text"
											readonly="readonly" class="Wdate ydwx_recvtime"  onclick="rtime()"
											value="<%=recvtime==null?"":recvtime %>" 
											 id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
								    </td>
									
									
								    <td>&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr align="center">
								<th>
									<emp:message key="ydwx_wxcxtj_fstj_czy" defVal="操作员" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxcxtj_fstj_lshjg" defVal="隶属机构" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_common_SPzhanghao" defVal="SP账号" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxcxtj_fstj_fszt" defVal="发送主题" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxcxtj_fstj_rwpc" defVal="任务批次" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxgl_wxbhs" defVal="网讯编号" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxgl_wxmcs" defVal="网讯名称" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_common_time_fasongshijian" defVal="发送时间" fileName="ydwx"></emp:message>
								</th>
								<th>
								   <emp:message key="ydwx_wxcxtj_fstj_fszht" defVal="发送状态" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxcxtj_fstj_hmgsh" defVal="号码个数" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxcxtj_fstj_tjxxsh" defVal="提交信息数" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxcxtj_fstj_fschgsh" defVal="发送成功数" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxcxtj_fstj_tjshbsh" defVal="提交失败数" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxcxtj_fstj_jshshbsh" defVal="接收失败数" fileName="ydwx"></emp:message>
								</th>
								<th>
								   <emp:message key="ydwx_wxcxtj_fstj_xxnr" defVal="信息内容" fileName="ydwx"></emp:message>
								</th>
								<th>
								   <emp:message key="ydwx_wxcxtj_fstj_fswj" defVal="发送文件" fileName="ydwx"></emp:message>
								</th>
								<th>
					          		<emp:message key="ydwx_common_caozuo" defVal="操作" fileName="ydwx"></emp:message>
								</th>
								<th>
							   		<emp:message key="ydwx_wxcxtj_fstj_hfxq" defVal="回复详情" fileName="ydwx"></emp:message>
								</th>
								<th>
							    	<emp:message key="ydwx_wxcxtj_fstj_fsxq" defVal="发送详情" fileName="ydwx"></emp:message>
								</th>
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="19" align="center"><emp:message key="ydwx_common_qdjcxhqsj" defVal="请点击查询获取数据" fileName="ydwx"></emp:message></td></tr>
							<%
							} else if(mtList != null && mtList.size()>0)
							{
								for(LfMttaskVo mt : mtList)
								{
						%>
						<tr align="center">
							<td class="textalign">
								<%=mt.getName() %><%if(mt.getUserState()==2){out.print("<font color='red'>("+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_yizhuxiao",request)+")</font>");} %>
							</td>
							<td class="textalign" >
								<xmp><%=mt.getDepName() %></xmp>
							</td>
							<td >
								<%=mt.getSpUser() %>
							</td>
							<td class="textalign" >
								<%=mt.getTitle()==null?"":(mt.getTitle().replaceAll("<","&lt;").replaceAll(">","&gt;")) %>
							</td>
							<td class="textalign" >
								<%
									Long taskid=mt.getTaskId()!=null?mt.getTaskId():0l;
									if(taskid-0!=0){
										out.print(taskid+"");
									}else{
										out.print("--");
									}
								%>
							</td>
							<td class="textalign">
								<%=mt.getNetid()==null?"":(mt.getNetid().replaceAll("<","&lt;").replaceAll(">","&gt;")) %>
							</td>
							<td class="textalign">
							<%
								String networkName = mt.getNetname()==null?"":(mt.getNetname().replaceAll("<","&lt;").replaceAll(">","&gt;"));
								String _networkName = mt.getNetname()==null?"":(mt.getNetname().replaceAll("<","&lt;").replaceAll(">","&gt;"));
								if(networkName.length()>6)
								{
									networkName = networkName.substring(0,6)+"...";
								}
							%>
							<a href="javascript:Look(<%=mt.getNetid()==null?"":(mt.getNetid().replaceAll("<","&lt;").replaceAll(">","&gt;")) %>);" title="<%= _networkName%>"><%=networkName %></a>
							</td>
							<td>
								<%=mt.getTimerTime()==null?"":df.format(mt.getTimerTime()) %><%--发送时间 --%>
							</td>
							<td class="ztalign" >
						 	<%
								    String sendState = mt.getSendstate().toString();
						            String error = "";
								    if(sendState.equals("0")){
								       sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_weifasong",request);
								    }else if(sendState.equals("1")){
								       sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_wgjshchg",request);
								    }else if(sendState.equals("2")){
								       sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_shibai",request);
								       if(mt.getErrorCodes()!=null && !"".equals(mt.getErrorCodes())){
								    	   error="["+mt.getErrorCodes()+"]";
								       }
								    }else if(sendState.equals("4")){
								      	sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_EMPfszh",request);
								    }else if(sendState.equals("6")){
								    	sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_zhzhfs",request);
								    }else if(sendState.equals("3"))
								    	{
								    	sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_wgchlwc",request);
								    	}else{
								       	sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_weishiyong",request);
								    }
								 %>
								 <%=sendState %><%=error %>	
							 </td>
							<td >
								<%=mt.getEffCount() %>
							</td>
							<td >
								<%=mt.getSendstate()==2?(mt.getIcount()==null?"0":mt.getIcount()):(mt.getIcount2()==null?"-":mt.getIcount2()) %>
							</td>
							<td >
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
							<td >
								<%//mt.getSendstate()==2?(mt.getIcount()==null?0:mt.getIcount()):(mt.getIcount2()==null?"-":mt.getFaiCount()) %>
								<%if((  mt.getSendstate()==2?(mt.getIcount()==null?0:mt.getIcount()):(mt.getIcount2()==null?"-":mt.getFaiCount()) )==null){
									out.print("0");
								}else{
									out.print(mt.getSendstate()==2?(mt.getIcount()==null?0:mt.getIcount()):(mt.getIcount2()==null?"-":mt.getFaiCount()));
								}%>
							</td>
							<td >
								<%=mt.getSendstate()==2?"0":(mt.getIcount2()==null?"-":(mt.getRFail2()==null?"-":mt.getRFail2()))%>
							</td>
							<td  class="textalign">
							<%if(mt.getMsg()==null || "".equals(mt.getMsg())){out.print(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_xiangjianwenjian",request));}else{ %>
									<a onclick=javascript:modify(this,1)>
										<xmp><%
											String st = "";
											//当使用String中的replaceAll方法时，如果替换的值中包含有$符号时，在进行替换操作时会出现如下错误。
											//替换操作前后分别对替换值中的$符号进行encode和decode操作
											String replacement = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_32",request);
											//replacement = replacement.replaceAll("\\$", "RDS_CHAR_DOLLAR");// encode replacement;
											String temp = mt.getMsg().replaceAll("#[pP]_(\\d+)#",replacement);
											//temp = temp.replaceAll("RDS_CHAR_DOLLAR", "\\$");// decode replacement;
											if(temp.length()>5)
											{
												st = temp.substring(0,5)+"...";
											}else
											{
												st = temp;
											}
											out.print(st);
										%></xmp>
								  		<label style="display:none"><xmp><%=temp%></xmp></label>
								  </a> 
							<%} %>
							</td>
							<td>
								<%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) == null ){ %>	
								 <label>-</label>
								<%}else{ %>									
								 <a href="javascript:checkFileOuter('<%=mt.getMobileUrl()%>','<%=request.getContextPath()%>')"><emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message></a>
									&nbsp;
								  <a href="javascript:downloadFilesOuter('<%=mt.getMobileUrl()%>','<%=request.getContextPath()%>')"><emp:message key='ydwx_common_xiazai' defVal='下载' fileName='ydwx'></emp:message></a>
								<%} %>								
							</td>

							<td>
							   <%if(mt.getIsRetry() == 1){ %>
							      <label><emp:message key="ydwx_wxcxtj_fstj_ychf" defVal="已重发" fileName="ydwx"></emp:message></label>
							   <%}else if(mt.getSendstate().toString().equals("2") && currUserid.toString().equals(mt.getUserId().toString())){%>
							     <a name="rsend"  href="javascript:reSend('<%=mt.getStrMtID() %>',<%=mt.getMsType() %>)">
							     <emp:message key="ydwx_wxcxtj_fstj_shbchf" defVal="失败重发" fileName="ydwx"></emp:message></a>
							     <%}else{ %>
							     <label>-</label>
							     <%} %>
							     <%
							     //网关正在发送中
							     boolean icountok = (mt.getIcount2()==null)?false:(Long.parseLong(mt.getIcount())-Long.parseLong(mt.getIcount2())<=0L);

							     if((mt.getSendstate()==1||mt.getSendstate()==3) && !icountok)
							     {
						    	 %>
						    	 <%--
							    	<a name=stopTask  href="javascript:stopTask('<%=mt.getMtId() %>')">终止</a>
							     --%>
						    	 <% 
							     }
							     %>
							</td>					

							<td>
							<%if(mt.getIsReply()==1&&"3".equals( mt.getSendstate().toString())){ %>
								<a href="javascript:searchNoticeDetail(1,'<%=mt.getStrMtID()%>')">
								<emp:message key='ydwx_common_btn_huifu' defVal='回复' fileName='ydwx'></emp:message></a>
							<%}else {%>
								-
							<% }%>
							</td>
							<td>
							  <%if(mt.getSendstate().toString().equals("2")){ %>
							     -
							  <%} else{%>
								  <a onclick="javascript:location.href='wx_wmsTaskRecord.htm?method=findAllSendInfo&mtid=<%=mt.getStrMtID() %>&type=0'">
								  <emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message></a>
							  <%} %>
							</td>			
						</tr>
						<%
								}
							}else{
						%>
						<tr><td align="center" colspan="19"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="19">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="smssendparams" class="hidden"></div>
				</form>
			</div>
			<div id="detailDialog" class="ydwx_detailDialog">
			</div>
			<%-- 内容结束 --%>
			<div id="modify" title="<emp:message key="ydwx_wxcxtj_fstj_xxnr" defVal="信息内容" fileName="ydwx"></emp:message>" class="ydwx_modify">
				<table width="100%">
					<thead>
						<tr class="ydwx_modify_table_tr1">
							<td class="ydcx_td1">
								<span><xmp id="msgcont" class="ydwx_msgcont"></xmp></span>
							</td>
						</tr>
					   <tr class="ydwx_modify_table_tr2" >
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
		<div id="divBox" class="hideDlg" style="display:none" title="<emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message>">
							<div  align="center" class="ydwx_divBox_sub">
				          	<emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message>
				          	 <select class="ym ydwx_ym"></select>
				        	</div>
							<div class="ydwx_divBox_sub2">
								<iframe class="ydwx_preview_common" id="nm_preview_common" src=""></iframe>	
							</div>
						</div>
    <div class="clear"></div>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydwx_<%=empLangName%>.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/smt_smsSendedBox.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript">
			$(document).ready(function() {
			     getLoginInfo("#smssendparams");
			    //参数是要隐藏的下拉框的div的id数组，
			    closeTreeFun(["dropMenu2","dropMenu"]);
				var findresult="<%=findResult%>";
			    if(findresult=="-1")
			    {
			       alert(getJsLocaleMessage("ydwx","ydwx_common_jzymshbqjchwl"));	
			       return;			       
			    }
				//reloadTree(<%=zNodes3 %>);
				$("#toggleDiv").toggle(function() {
					$("#condition").hide();
					$(this).addClass("collapse");
				}, function() {
					$("#condition").show();
					$(this).removeClass("collapse");
				});
                $('#spUser').isSearchSelect({'width':'160','isInput':true,'zindex':0});
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				
					$("#divBox").dialog({
					autoOpen: false,
					height:520,
					width: 300,
					modal: true,
					close:function(){
					}
				});

                //页码改变时  div层内容变化的方法
                $(".ym").change(function(){
                    var id=$(this).val();
                    for(var i=0;i<listpage.length;i++){
                        if(id==listpage[i].id){
                            // 此处为显示错误页面，避免进入登录页面
                            if(listpage[i].content=="notexists"){
                                $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
                            }else{
                                $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
                            }
                        }
                    }
                });

                $("#netname,#title").live("keyup blur",function(){
					var value=$(this).val();
					if(value!=filterString(value)){
						$(this).val(filterString(value));
					}
				});
				
				$('#search').click(function(){submitForm();});
			    $("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
				}, function() {
					$(this).removeClass("hoverColor");
				});		

				$("#detailDialog").dialog({
					autoOpen: false,
					height:450,
					width: 650,
					modal: true,
					resizable:false,
					close:function(){
					}
				});
				
			//导出全部数据到excel
			$("#exportCondition").click(
			function()
			 {
				  if(confirm(getJsLocaleMessage("ydwx","ydwx_common_qrdchshjd")))
				   {
				   		/*var sendtime = $("#sendtime").val();
				   		
				   		var recvtime = $("#recvtime").val();
				   		
				   		var userName = $("#userid").val();*/
				   		
				   	    var mTitle = '<%=title==null?"":title%>';
				   	    
				   	    var spuser = '<%=spUser==null?"":spUser%>';			   	    
				   	   	var lgcorpcode =$("#lgcorpcode").val();
						var lguserid =$("#lguserid").val(); 	
						var netid ='<%=netid==null?"":netid %>'; 
						var isContainsSun='<%=isContainsSun%>';
						var taskid='<%=staskid%>';
						
				   		<%
				   		if(mtList!=null && mtList.size()>0){
				   		  if(pageInfo.getTotalRec()<=500000){
				   		%>		
						$.ajax({
							type: "POST",
							url: "wx_wmsTaskRecord.htm?method=ReportCurrPageExcel",
							data: {
							spuser:spuser,mTitle:mTitle,
							pageIndex:'<%=pageInfo.getPageIndex()%>',
							pageSize:'<%=pageInfo.getPageSize()%>',
							lguserid:lguserid,netid:netid,
							lgcorpcode:lgcorpcode,isContainsSun:isContainsSun,
							taskid:taskid,
							empLangName:"<%=empLangName%>"
							},
							beforeSend: function(){
								page_loading();
							},
			                complete:function () {
								page_complete();
			                },
							success: function(result){
								if(result=='true'){		
									download_href("wx_wmsTaskRecord.htm?method=downloadFile&down_session=ReportCurrPageExcel");	                    	
			                    }else{
			                        alert(getJsLocaleMessage("ydwx","ydwx_common_dcshb"));
			                    }
				   			}
						});
				   			
				   		     // location.href="<%=path%>/wx_wmsTaskRecord.htm?method=ReportCurrPageExcel&spuser="+spuser+"&mTitle="+mTitle+"&pageIndex="+<%=pageInfo.getPageIndex()%>+"&pageSize="+<%=pageInfo.getPageSize()%>+"&lguserid="+lguserid+"&netid="+netid+"&lgcorpcode="+lgcorpcode+"&isContainsSun="+isContainsSun+"&taskid="+taskid;
				   		<%	
				   		}else{%>
				   		    alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_3"));
				   		<%}
				   		}else{
				   		%>
				   		   alert(getJsLocaleMessage("ydwx","ydwx_common_wshjdch"));
				   		<%
				   		}%>
				   }				 
			  });
				$('#modify').dialog({
					autoOpen: false,
					width:250,
				    height:200
				});
				
				//if(screen.width >= 1024){       
	            //     $("#sztdsize").css("font-size", 10.5);                  
	            //}
			});

			function reSend(mtid,msType)
			{
               if(confirm(getJsLocaleMessage("ydwx","ydwx_wxfstj_1"))){
			    $("a[name='rsend']").attr("disabled","disabled");
			    var lgcorpcode =$("#lgcorpcode").val();
				var lguserid =$("#lguserid").val();
				var lgusername =$("#lgusername").val();
				var lgguid =$("#lgguid").val();
				if(msType == 5){// 移动财务短信
					$.post("<%=path %>/ycw_electronicPayroll.htm?method=retry", {
							mtid : mtid,
							lgcorpcode : lgcorpcode,
							lguserid : lguserid,
							lgusername:lgusername,
							lgguid:lgguid
						},
							function(result) {
		                        if(result=="success")
								{ 
									alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_2"));
								}else if(result=="error")
								{
									alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_3"));
								}else if(result=="noMoney")
								{
								    alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_4"));
								}else if(result=="payFailure")
								{
								    alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_5"));
								}
								else if(result=="nofindfile")
								{
									alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_6"));
								}
								else if(result=="isretry")
							    {
							       alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_7"));
							    }
								else if(result=="nospnumber")
								{
							        alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_8")+getJsLocaleMessage("ydwx","ydwx_common_SPzhanghao")+getJsLocaleMessage("ydwx","ydwx_wxfstj_9"));
						        }else if(result == "noSubNo"){
						        	alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_10"));
						        }else if(result == "noUsedSubNo"){
									alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_11"));
								}else
								{
									alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_12")+result);
								}
								 $("a[name='rsend']").attr("disabled","");
								document.pageForm.submit();
						});
				}else{
				  $.post("<%=path %>/wx_wmsTaskRecord.htm?method=reSendSMS", {
						mtid : mtid,
						lgcorpcode : lgcorpcode,
						lguserid : lguserid,
						lgusername:lgusername,
						lgguid:lgguid
					},
						function(result) {
	                        if(result=="createSuccess")
							{ 
								alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_13"));
							}else if(result=="000")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_14"));
							}else if(result=="saveSuccess")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_15"));
							}else if(result=="error")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_3"));
							}else if(result=="nomoney")
							{
							    alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_4"));
							}else if(result=="false")
							{
							    alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_5"));
							}
							else if(result=="nofindfile")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_6"));
							}
							else if(result=="isretry")
							{
							    alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_7"));
							}
							else if(result=="uploadFileFail")
							{
								alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_16"));
							}
							else if(result=="nospnumber")
						    {
							    alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_8")+getJsLocaleMessage("ydwx","ydwx_common_SPzhanghao")+getJsLocaleMessage("ydwx","ydwx_wxfstj_9"));
						    }else if(result == "noSubNo"){
					        	alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_10"));
					        }else if(result == "noUsedSubNo"){
								alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_11"));
							}
							else
							{
								alert(getJsLocaleMessage("ydwx","ydwx_wxfstj_17")+result);
							}
							//window.location.href = window.location.href;
							 $("a[name='rsend']").attr("disabled","");
							document.pageForm.submit();
					});
				}
				}
			}
			function searchNoticeDetail(pageIndex,mtId){
				 $.post("<%=request.getContextPath()%>/wx_wmsTaskRecord.htm?method=getSinglePerNoticDetail&mtId="+mtId+"&pageIndex="+pageIndex,null,function call(data){   
					if(data!="error"&&data!="noList")
					{
						
			   		  var member = eval("("+data+")");
		        		  var moblie;
		        		  var time;
		        		  var content;
			        	  var pageMsg = "";
			        	  $("#detailDialog").text("");
			        	  var maxSendCount = member.maxSendCount;
			        	  var sendInterval = member.sendInterval;
			        	  var sendContent = member.sendContent;
			        	  var arySendCount = member.arySendCount;
			        	 var recordCount = member.count; 
			        	 var pageSize = member.pageSize;
			        	 var mtId = member.preNoticeId;
			        	 var index = member.index;

						 //var title = "<span style='float:right;'><input type='button' value='刷新'  class='btnClass1' onClick='searchNoticeDetail("+index+","+preNoticeId+")'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></div></br>";
						 var title = "";



						  var msg = "<br><center id='ccontent1'><table id='content' style='width:97%;' >"                 
		        + "<thead> <tr>  <th align='center'  width='20%'>"+getJsLocaleMessage("ydwx","ydwx_wxfstj_18")+"</th><th align='center' width='50%'>"+getJsLocaleMessage("ydwx","ydwx_wxfstj_19")+"</th> <th align='center' width='30%'>"+getJsLocaleMessage("ydwx","ydwx_wxfstj_20")+"</th> </tr> </thead>";

						if(member.jobs.length>0){
							 for(var i= 0;i<member.jobs.length;i++){
				        			moblie =  member.jobs[i].moblie;
				        			time =  member.jobs[i].time;
				        			content =  member.jobs[i].content;
					        		msg = msg +" <tr> <td width='20%' align='center'>"+moblie+"</td> "
					        		+"  <td align='center' width='50%' style='word-break : break-all;'>"+content.replaceAll("<","&lt;").replaceAll(">","&gt;")+"</td>"          
							        +"  <td align='center' width='30%'>"+time+"</td>";
							      
					         }
							
				        		if(recordCount%pageSize == 0){
				        			pageCount = recordCount/pageSize;
				        		}else{
				        			pageCount = parseInt(recordCount/pageSize) + 1;
				        		}
				        		if(pageCount == 0){
				        			pageCount = 1;
				        		}
				        		var start = 1;
				        		var end = pageCount; 
				        		if(pageCount>5){
				        			start = pageIndex - 2;
				        			if(start <= 0){
				        				start = 1;
				        			}
				        			end = parseInt(pageIndex) + 2;
				        			if(end > pageCount){
				        				end = pageCount;
				        			}
				        		}  
				        		if(pageIndex != 1){
				        			pageMsg = pageMsg +"<a href='javascript:searchNoticeDetail("+(pageIndex-1)+","+mtId+")'>"+getJsLocaleMessage("ydwx","ydwx_wxfstj_21")+" </a>&nbsp;&nbsp;";
				        		}
				        		if(end - 5 >= 1){
				        			pageMsg = pageMsg + "<a href='javascript:searchNoticeDetail(1,"+mtId+")'>"+1+"</a>&nbsp;&nbsp;";
				        		}
				        		if(start >= 3 ){
				        			pageMsg = pageMsg +"<span>...</span>";
				        		}
				        		for(var i=start ; i <= end ; i++){
				        			if(i == pageIndex){
				        				pageMsg = pageMsg + "<span class='current'>"+i+"</span>&nbsp;&nbsp;";
				        			}else{
				        				pageMsg = pageMsg + "<a href='javascript:searchNoticeDetail("+i+","+mtId+")'>"+i+"</a>&nbsp;&nbsp;";
				        			}
				        		}
				        		if(pageCount - end >= 2 ){
				        			pageMsg = pageMsg + "<span>...</span>";
				        		}
				        		if(end < pageCount ){
				        			pageMsg = pageMsg + "<a href='javascript:searchNoticeDetail("+pageCount+","+mtId+")'>"+pageCount+"</a>&nbsp;&nbsp;";
				        		}
				        		if(pageIndex != pageCount){
				        			pageMsg = pageMsg + "<a href='javascript:searchNoticeDetail("+(pageIndex+1)+","+mtId+")'>"+getJsLocaleMessage("ydwx","ydwx_wxfstj_22")+"</a>";
				        		}
						}else{
							msg =msg + " <tr><td align='center' colspan='7'>"+getJsLocaleMessage("ydwx","ydwx_wxfstj_30")+"</td></tr>";
						}
						var foot = "<div>&nbsp;</div>";
		        		$("#detailDialog").append(title+msg+"</table></center><br><div style='text-align:center';>"+pageMsg+"</div>"+foot); 
		        			$(".btnClass1").hover(function() {
								var img=$(this).css('background-image');
								$(this).css('background-image',img.replace('but-bg1','but-bg2'))
							}, function() {
								var img=$(this).css('background-image');
								$(this).css('background-image',img.replace('but-bg2','but-bg1'))
							});
		        		$("#detailDialog").dialog("option","title", getJsLocaleMessage("ydwx","ydwx_wxfstj_23"));
		      			$("#detailDialog").dialog("open");
		      			deleteleftline2();
					}
					else if(data=="error"){
						alert( getJsLocaleMessage("ydwx","ydwx_wxfstj_24"));
					}
					else {
						alert( getJsLocaleMessage("ydwx","ydwx_wxfstj_25"));
					}
		        });
			}	
			

			function stopTask(mtid)
			{
               if(confirm( getJsLocaleMessage("ydwx","ydwx_wxfstj_26")))
               {
					$.post("<%=path %>/wx_<%=titlePath %>.htm?method=stopTask", {
						mtId : mtid
					},
					function(result) {
                        if(result=="true")
                        {
							alert( getJsLocaleMessage("ydwx","ydwx_wxfstj_27"));
		                }else if(result=="false"){
							alert( getJsLocaleMessage("ydwx","ydwx_wxfstj_28"));
			            }else if(result=="AllSended")
			            {
			            	alert( getJsLocaleMessage("ydwx","ydwx_wxfstj_29"));
				        }
						document.pageForm.submit();
					});
               }	
			}
			
	
		</script>
		<script type="text/javascript" src="<%=iPath %>/js/wx_wmsTaskRecord.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	</body>
</html>
