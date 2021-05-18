<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@ page import="com.montnets.emp.entity.pasroute.LfSpDepBind" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.netnews.vo.LfMttaskVo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
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
	
	int corpType = StaticValue.getCORPTYPE();
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	String zNodes3 = (String)request.getAttribute("departmentTree");
	//String zNodes2 = (String)request.getAttribute("deptUserTree");
	
	String findResult= (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String httpUrl = StaticValue.getFileServerViewurl();
	String taskid = (String)request.getParameter("taskid");
    String flowId = request.getParameter("flowId");
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
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/taskReport.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	    <link rel="stylesheet" type="text/css" href="<%=skin %>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
		<style type="text/css">
			body div#tooltip { position:absolute; z-index:1000; max-width:435px; _width:expression(this.scrollWidth > 435 ? "435px" : "auto"); width:auto; background:#A8CFF6; border:#FEFFD4 solid 1px; text-align:left; padding:6px;}
			body div#tooltip p { margin:0;padding:6; color:#FFFFFE;font:12px verdana,arial,sans-serif;}
			body div#tooltip p em { display:block;margin-top:3px; color:#f60;font-style:normal; font-weight:bold;}
			#content tr.peachpuff,#content tr.peachpuff td{background:#FFDAB9;}
			#msgcont {
                white-space: pre-wrap;
                *white-space: pre;
                *word-wrap: break-word;
                word-break: break-all;
            }
		</style>
	</head>

	<body id="ydwx_taskReport">
		<div id="container" class="container">
			<%
				if(CstlyeSkin.contains("frame4.0")){
			%>
				<input id='hasBeenBind' value='1' type='hidden'/>
			<%
				}
			 %>
			<%-- header开始 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<form name="pageForm" action="<%=path %>/wx_taskreport.htm" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						<a id="exportCondition"><emp:message key='ydwx_common_btn_daochu' defVal='导出' fileName='ydwx'></emp:message></a>						
					</div>
					<div id="condition">
						<table>
						   
							<tbody>
							
								<tr>
									<td>
										<emp:message key="ydwx_wxgl_wxbh" defVal="网讯编号：" fileName="ydwx"></emp:message>
									</td>									
									<td>	
									  		<input type="text" id="netid" onkeyup="numberControl($(this))" onblur="numberControl($(this))" name="netid" value="<%=request.getParameter("netid")==null?"":request.getParameter("netid") %>" class="ydwx_netid">
									</td>
									<td>
										<emp:message key="ydwx_wxgl_wxmc" defVal="网讯名称：" fileName="ydwx"></emp:message>
									</td>									
									<td>	
									  		<input type="text" id="netname" name="netname" value="<%=request.getParameter("netname")==null?"":request.getParameter("netname") %>" class="ydwx_netname">
									</td>
									<td>
										<emp:message key="ydwx_wxcxtj_hftj_wxzhts" defVal="网讯主题：" fileName="ydwx"></emp:message>
									</td>									
									<td>	
									  		<input type="text" id="title" name="title" value="<%=request.getParameter("title")==null?"":request.getParameter("title") %>" class="ydwx_title">
									</td>
									<td class="tdSer">
									       <center><a id="search"></a></center>
								    </td>		
								</tr>	
							
								<tr>
									<td>
										<emp:message key="ydwx_wxcxtj_fstj_czys" defVal="操作员：" fileName="ydwx"></emp:message>
									</td>
									<td class="condi_f_l">	
											<div class="ydwx_operator_dv">
												<input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
												<input type="text" class="treeInput ydwx_userName" id="userName" name="userName" value="<%=userName==null?MessageUtils.extractMessage("ydwx","ydwx_jsp_out_qingxuanze",request):userName%>" readonly onclick="javascript:showMenu2();"/>&nbsp;
<%--											<a onclick="javascript:showMenu2();" style="cursor: pointer;text-decoration: underline">选择</a>--%>
											</div>											
											<div id="dropMenu2">
											<iframe class="ydwx_dropMenu2_iframe" frameborder="0" src="about:blank"></iframe>	
												<div class="ydwx_btns">
													<input type="button" value="<emp:message key='common_confirm' defVal='确定' fileName='common'></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK2();"/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='common_clean' defVal='清空' fileName='common'></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep();"/>
												</div>
												<div class="ydwx_note_dv"><font class="zhu"><emp:message key="ydwx_wxcxtj_fstj_czytsh" defVal="注：请勾选操作员进行查询" fileName="ydwx"></emp:message></font></div>	
												<ul  id="dropdownMenu2" class="tree"></ul>	
											</div>										   										
									</td>
									<td><emp:message key="ydwx_wxcxtj_wxfschx_rwzhts" defVal="审批状态：" fileName="ydwx"></emp:message></td>
								<td>
									<select id="conrstate" name="conrstate" class="ydwx_conrstate" isInput="false">
										<option value=""><emp:message key="ydwx_wxgl_quanbu" defVal="全部" fileName="ydwx"></emp:message></option>
										<option value="1"  <%="1".equals(request.getParameter("conrstate")!=null?request.getParameter("conrstate").toString():"")?"selected":"" %>><emp:message key="ydwx_wxgl_wxshh_daishp" defVal="待审批" fileName="ydwx"></emp:message></option>
										<option value="2"  <%="2".equals(request.getParameter("conrstate")!=null?request.getParameter("conrstate").toString():"")?"selected":"" %>><emp:message key="ydwx_wxgl_wxshh_shptongguo" defVal="审批通过" fileName="ydwx"></emp:message></option>
										<option value="3"  <%="3".equals(request.getParameter("conrstate")!=null?request.getParameter("conrstate").toString():"")?"selected":"" %>><emp:message key="ydwx_wxgl_wxshh_shpbutongguo" defVal="审批不通过" fileName="ydwx"></emp:message></option>
										<option value="4"  <%="4".equals(request.getParameter("conrstate")!=null?request.getParameter("conrstate").toString():"")?"selected":"" %>><emp:message key="ydwx_wxgl_wxshh_shpwancheng" defVal="审批完成" fileName="ydwx"></emp:message></option>
									</select>
								</td>
									<td><emp:message key="ydwx_wxgl_wxshh_zhuangtais" defVal="任务状态：" fileName="ydwx"></emp:message></td>
								<td>
										<select id="taskstate" name="taskstate" class="ydwx_taskstate" isInput="false">
											<option value=""><emp:message key="ydwx_wxgl_quanbu" defVal="全部" fileName="ydwx"></emp:message></option>
											<option value="1"  <%="1".equals(request.getParameter("taskstate")!=null?request.getParameter("taskstate").toString():"")?"selected":"" %>><emp:message key="ydwx_wxcxtj_wxfschx_daifasong" defVal="待发送" fileName="ydwx"></emp:message></option>
											<option value="2"  <%="2".equals(request.getParameter("taskstate")!=null?request.getParameter("taskstate").toString():"")?"selected":"" %>><emp:message key="ydwx_wxcxtj_wxfschx_yifasong" defVal="已发送" fileName="ydwx"></emp:message></option>
											<option value="3"  <%="3".equals(request.getParameter("taskstate")!=null?request.getParameter("taskstate").toString():"")?"selected":"" %>><emp:message key="ydwx_wxcxtj_wxfschx_yidongjie" defVal="已冻结" fileName="ydwx"></emp:message></option>
											<option value="4"  <%="4".equals(request.getParameter("taskstate")!=null?request.getParameter("taskstate").toString():"")?"selected":"" %>><emp:message key="ydwx_wxcxtj_wxfschx_yichexiao" defVal="已撤销" fileName="ydwx"></emp:message></option>
											<option value="5"  <%="5".equals(request.getParameter("taskstate")!=null?request.getParameter("taskstate").toString():"")?"selected":"" %>><emp:message key="ydwx_wxcxtj_wxfschx_weifasong" defVal="超时未发送" fileName="ydwx"></emp:message></option>

										</select>
								</td>
								
								</tr>
								<tr>
								<td><emp:message key="ydwx_common_SPzhanghaos" defVal="SP账号：" fileName="ydwx"></emp:message></td>
									  
									<td>	
  		                                 	<label>
											<select name="spUser" id="spUser" class="ydwx_spUser">
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
							   	   <td>
								       <emp:message key="ydwx_common_time_chuangjianshijians" defVal="创建时间：" fileName="ydwx"></emp:message> 
								   </td>
								   <td>
								    	<input type="text" readonly="readonly" class="Wdate ydwx_sendtime" onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime"
												 <%--onchange="onblourSendTime('end')" --%>>
								   </td>
								   <td><emp:message key="ydwx_common_time_zhi" defVal="至：" fileName="ydwx"></emp:message></td>
								   <td>
								      	<input type="text" readonly="readonly" class="Wdate ydwx_recvtime"  onclick="rtime()"
											value="<%=recvtime==null?"":recvtime %>" 
											 id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
								    </td>
								    <td></td>
								</tr>
                                <%if(flowId != null){%>
                                <tr>
                                    <td><emp:message key="ydwx_wxcxtj_wxfschx_shhlchID" defVal="审核流程ID：" fileName="ydwx"></emp:message></td>
                                    <td colspan="6">
                                        <input type="text" name="flowId" class="ydwx_flowId" value="<%=flowId%>" onkeyup="javascript:numberControl($(this))" maxlength="19"/>
                                    </td>
                                </tr>
                                <%}%>

							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
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
									<emp:message key="ydwx_wxcxtj_hftj_wxzht" defVal="网讯主题" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxgl_wxbhs" defVal="网讯编号" fileName="ydwx"></emp:message>
								</th>
								<th class="ydwx_name">
									<emp:message key="ydwx_wxgl_wxmcs" defVal="网讯名称" fileName="ydwx"></emp:message>
								</th>
								<th class="ydwx_createtm">
									<emp:message key="ydwx_common_time_chuangjianshijian" defVal="创建时间" fileName="ydwx"></emp:message>
								</th>
								<th class="ydwx_sendtm">
									<emp:message key="ydwx_common_time_fasongshijian" defVal="发送时间" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxcxtj_wxfschx_yxhmsh" defVal="有效号码数" fileName="ydwx"></emp:message>
								</th>
								<th>
								    <emp:message key="ydwx_wxcxtj_fstj_xxnr" defVal="信息内容" fileName="ydwx"></emp:message>
								</th>
								<th class="ydwx_sendfile">
									<emp:message key="ydwx_wxcxtj_fstj_fswj" defVal="发送文件" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_wxcxtj_wxfschx_rwzht" defVal="任务状态" fileName="ydwx"></emp:message>
								</th>
								
							    <th>
									<emp:message key="ydwx_wxcxtj_wxfschx_shpxq" defVal="审批详情" fileName="ydwx"></emp:message>
								</th>
								<th>
									<emp:message key="ydwx_common_caozuo" defVal="操作" fileName="ydwx"></emp:message>
								</th>
								
							</tr>
						</thead>
						<tbody>

							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="14" align="center"><emp:message key="ydwx_common_qdjcxhqsj" defVal="请点击查询获取数据" fileName="ydwx"></emp:message></td></tr>
							<%
							} else if(mtList != null && mtList.size()>0)
							{
								for(LfMttaskVo mt : mtList)
								{
									if(taskid != null && !"".equals(taskid) && Long.valueOf(taskid) - mt.getTaskId() == 0)
									{
									%>
										<tr class="peachpuff">
									<%
									}
									else
									{
									%>
										<tr>
									<%} %>
										<td class="textalign">
											<%=mt.getName() %><%if(mt.getUserState()==2){out.print("<font color='red'>("+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_yizhuxiao",request)+")</font>");} %>
										</td>
										<td class="textalign">
											<xmp><%=mt.getDepName() %></xmp>
										</td>
										<td>
											<%=mt.getSpUser() %>
										</td>
										<td   class="textalign">
											<%=mt.getTitle()==null?"-":(mt.getTitle().replaceAll("<","&lt;").replace(">","&gt;")) %>
										</td>
										<td   class="textalign"><%-- 网讯编号--%>
											<%=mt.getNetid()==null?"":mt.getNetid()%>
										</td>
										<td   class="textalign" width="200"><%-- 网讯名称--%>
											<%=mt.getNetname()==null?"":mt.getNetname() %>
										</td>
										<td><%-- 创建时间--%>
											<%=mt.getSubmitTime()==null?"-":df.format(mt.getSubmitTime()) %>
										</td>
										<td>
										<%--发送时间:1、如果定时时间为空（没有定时），且发送状态为0（没有发送），则在等待审批，发送时间未知；
										              2、如果定时时间不为空（定时了），则发送时间为定时时间，定时时间在不断更新中，当审批完毕时也会更新定时时间。
										--%>
											<%
											String sendedTime = "-";
											if(mt.getReState()==2){//审批不通过 （发送时间为空）
												out.print("-");
											}else if(mt.getSubState()==3){//撤销任务（空）
												out.print("-");
											}else if(mt.getSendstate()==5){//超时未发送（空）
												out.print("-");
											}else if(mt.getTimerStatus()==0 && mt.getReState()==-1){//未定时未审批（待审批）（空）
												out.print("-");
											}else if(mt.getTimerStatus()==1){//定时了
												out.print(df.format(mt.getTimerTime()));
											    if(mt.getSendstate()==0){out.print("("+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_dingshizhong",request)+")");}
											}else if(mt.getSendstate()==1 || mt.getSendstate()==2){//发送成功或者发送失败
												out.print(df.format(mt.getTimerTime()));
											}else{
												out.print(mt.getTimerTime()==null?"-":df.format(mt.getTimerTime()));//这里面的情况就是sendstate=4(发送中)
											}
											%>
										</td>
										<td>
											<%=mt.getEffCount() %>
										</td>
							<td style="text-align:left;">
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
											if(temp.length()>7)
											{
												st = temp.substring(0,7)+"...";
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
								 <%
								    Integer sendState = mt.getSendstate();
								    Integer subState = mt.getSubState();
								    String state=new String();
								    if(subState==2&&mt.getReState()==-1){
									    state = "<font color='#FE3E4D'>"+MessageUtils.extractMessage("ydwx","ydwx_wxgl_wxshh_daishp",request)+"</font>";
									}
								    else if(subState==2&&mt.getReState()==2){
									    state = "<font color='#FE3E4D'>"+MessageUtils.extractMessage("ydwx","ydwx_wxgl_wxshh_shpbutongguo",request)+"</font>";
									}
									else if(subState ==4)
									{
									   state = "<font color='#FE3E4D'>"+MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_wxfschx_yidongjie",request)+"</font>";
									}
								    else if(subState==3){
								    	state = "<font color='#FE3E4D'>"+MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_wxfschx_yichexiao",request)+"</font>";
									}
								    else if(sendState==5){
								    	state="<font color='#FE3E4D'>"+MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_wxfschx_weifasong",request)+"</font>";
								    }
								    else if(sendState!=0){
								    	state = "<font color='#088dd2'>"+MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_wxfschx_yifasong",request)+"</font>";
								    }
								    else if(subState==2){
									      if(mt.getReState()==-1)
									    	  state="<font color='#000000'>"+MessageUtils.extractMessage("ydwx","ydwx_wxgl_wxshh_daishp",request)+"</font>";
									      else if(mt.getReState()==2)
									    	  state = "<font color='#FE3E4D'>"+MessageUtils.extractMessage("ydwx","ydwx_wxgl_wxshh_shpbutongguo",request)+"</font>";
									      else
									    	  state = "<font color='#048e79'>"+MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_wxfschx_daifasong",request)+"</font>";
									    }
								 %>	
								 <span id="s<%=mt.getMtId()%>"><%=state %>	</span>					
							</td>
							<td>
							    <%
							   	 if(mt.getReState()==1||mt.getReState()==2){
							    %>
							     <%-- javascript:verify('<mt.getMtId()') --%>
								 <a onclick="javascript:openSmsDetail('<%=mt.getStrMtID() %>','<%=mt.getUserId()%>');" name="querySP"><emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message></a>
								<%
							    }else if(mt.getReState()==-1){
							    %>
							    <a onclick="javascript:openReviewFlow('<%=mt.getMtId() %>','<%=mt.getUserId()%>','5');" name="querySP"><emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message></a>
							    <% 
							    }else{
								out.print("-");
								}
								%>
							</td> 
							<td>
							  <span id="cz<%=mt.getMtId()%>">
							    <%
								   if(mt.getSendstate()!=0 || mt.getSubState()==3 || mt.getReState()==2){
									   out.print("-");
								   }
								   else{
								 %>																			  									 
									<a title="<emp:message key='ydwx_wxcxtj_wxfschx_chxrw' defVal='撤销任务' fileName='ydwx'></emp:message>" href="javascript:cancelTimer('<%=mt.getStrMtID() %>')">
									<emp:message key='ydwx_wxcxtj_wxfschx_chexiao' defVal='撤销' fileName='ydwx'></emp:message></a>
								<%
								   }
								 %>		
								 </span>
							 </td>
						</tr>
						<%
								}
							}else{
						%>
						<tr><td align="center" colspan="15"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="16">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="smssendparams" class="hidden"></div>
				</form>
			</div>
			
			<%-- 审批详情--%>
			<div id="shenhe" title="<emp:message key="ydwx_wxcxtj_wxfschx_shpxq" defVal="审批详情" fileName="ydwx"></emp:message>" class="ydwx_shenhe">
			
			</div>
			<%-- 内容结束 --%>
			
			<div id="modify" title="<emp:message key="ydwx_wxcxtj_fstj_xxnr" defVal="信息内容" fileName="ydwx"></emp:message>" class="ydwx_modify">
				<table width="100%">
					<thead>
						<tr class="ydwx_modify_table_tr1">
							<td class="ydwx_td1">
								<span><xmp id="msgcont" class="ydwx_msgcont"></xmp></span>
							</td>
						</tr>
					   <tr class="ydwx_modify_table_tr2">
							<td>
							</td>
						</tr>
						 
					</thead>
				</table>
			</div>
			
			<div id="smsdetailinfo" title="<emp:message key="ydwx_wxcxtj_wxfschx_shpxq" defVal="审批详情" fileName="ydwx"></emp:message>" style="display:none;overflow: auto;">
				<div class="ydwx_recordTableDiv" id="recordTableDiv" align="center">
					<table id="recordTable" class="ydwx_recordTable">
					</table>
				</div>
				<div class="ydwx_nextrecordmgs" id="nextrecordmgs" align="left">
				</div>
			</div>	
			<div id="reviewflowinfo" title="<emp:message key="ydwx_wxgl_wxshh_daishp" defVal="待审批" fileName="ydwx"></emp:message>" style="display:none;overflow: auto;">
				<div class="ydwx_reviewTableDiv"  id="reviewTableDiv" align="center">
						<table id="reviewTable" class="ydwx_reviewTable"></table>
				</div>
				<div class="ydwx_nextreviewmgs" id="nextreviewmgs" align="left">
				</div>
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
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js"></script>
		<script src="<%=commonPath%>/common/js/common.js" type="text/javascript" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydwx_<%=empLangName%>.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/serach_taskReport.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
			    //getLoginInfo("#smssendparams");
			    var findresult="<%=findResult%>";
			    closeTreeFun(["dropMenu2","dropMenu"]);
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
			    $("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
				}, function() {
					$(this).removeClass("hoverColor");
				});
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				$('#search').click(function(){submitForm();});	
				$('#modify').dialog({
					autoOpen: false,
					width:250,
				    height:200
				});
                $('#spUser').isSearchSelect({'width':'182','isInput':true,'zindex':0});
			$("#netname,#title").live("keyup blur",function(){
						var value=$(this).val();
						if(value!=filterString(value)){
							$(this).val(filterString(value));
						}
			 });

				<%--彩信任务详细信息弹出框 --%>
				$("#smsdetailinfo").dialog({
					autoOpen: false,
					modal:true,
					title:getJsLocaleMessage("ydwx","ydwx_wxfschx_1"), 
					width:680,
					height: 'auto',
					minHeight:170,
					maxHeight:650,
					closeOnEscape: false,
					resizable:false,
					open:function(){
					},
					close:function(){
					}
				});	

				$("#reviewflowinfo").dialog({
					autoOpen: false,
					modal:true,
					title:getJsLocaleMessage("ydwx","ydwx_wxfschx_2"), 
					width:880,
					height: 'auto',
					minHeight:170,
					maxHeight:750,
					closeOnEscape: false,
					resizable:false,
					open:function(){
					},
					close:function(){
					}
				});	
	
			
			//导出全部数据到excel
			$("#exportCondition").click(
			    function()
			    {
				
				  if(confirm(getJsLocaleMessage("ydwx","ydwx_common_qrdchshjd")))
				   {
				   		var sendtime = '<%=sendtime==null?"":sendtime %>';
				   		
				   		var recvtime = '<%=recvtime==null?"":recvtime %>';
				   		
				   		var userName = '<%=userid==null?"":userid %>';
				   		
				   	    var depNam = '<%=deptid==null?"":deptid%>';	
				   	    
				   	    var spuser = '<%=spUser%>';	
				   	    
				   	    var netid='<%=request.getParameter("netid")!=null?request.getParameter("netid").toString():"" %>';
				   	    
				   	    var  conrstate= '<%=request.getParameter("conrstate")!=null?request.getParameter("conrstate").toString():"" %>';
				   	     var  taskstate= '<%=request.getParameter("taskstate")!=null?request.getParameter("taskstate").toString():"" %>';
				   	    
				   	    var lgcorpcode =$("#lgcorpcode").val();
				        var lguserid =$("#lguserid").val();		   	    
				   	    			
				   		<%
				   		if(mtList!=null && mtList.size()>0 && pageInfo.getTotalRec()<=500000){
				   		%>	
				   			
						$.ajax({
							type: "POST",
							url: "wx_taskreport.htm?method=ReportAllPageExcel",
							data: {
							lguserid:lguserid,
							lgcorpcode:lgcorpcode,netid:netid,
							conrstate:conrstate,taskstate:taskstate,
							userid:userName,depid:depNam,
							spuser:spuser,pageIndex:'<%=pageInfo.getPageIndex()%>',
							pageSize:'<%=pageInfo.getPageSize()%>',
							empLangName:"<%=empLangName%>",
                            sendtime:sendtime, recvtime:recvtime
							},
							beforeSend: function(){
								page_loading();
							},
			                complete:function () {
								page_complete();
			                },
							success: function(result){
								if(result=='true'){
									download_href("wx_count.htm?method=downloadFile&down_session=ReportAllPageExcel");
			                    }else{
			                    	alert(getJsLocaleMessage("ydwx","ydwx_common_dcshb"));
			                    }
				   			}
						});
			   			
				   		//      location.href="<%=path%>/wx_taskreport.htm?method=ReportAllPageExcel&netid="+netid+"&conrstate="+conrstate+"&taskstate="+taskstate+"&sendtime="+sendtime+"&recvtime="+recvtime+"&userid="+userName+"&depid="+depNam+"&spuser="+spuser+"&pageIndex="+<%=pageInfo.getPageIndex()%>+"&pageSize="+<%=pageInfo.getPageSize()%>+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				   		
				   		<%	
				   		}
				   		else if(mtList!=null && pageInfo.getTotalRec()>500000)
				   		{
				   		%>
				   			alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_3"));
				   		<%}	else{
				   		%>
				   			alert(getJsLocaleMessage("ydwx","ydwx_common_wshjdch"));
				   		<%
				   		}%>
				   }				 
			  });
			});

			function verify(mtId){
				$("a[name='querySP']").attr("disabled","disabled");
				$("#shenhe").load(
				    	"wx_<%=titlePath %>.htm?method=getVerify",
				    	{mtId:mtId},
				    	function(){
				    		$('#shenhe').dialog({
								autoOpen: false,
								resizable: false,
								width:500,
							    height:300,
							    close:function(){
									$("a[name='querySP']").attr("disabled","");
								}
							});
							$('#shenhe').dialog('open');
						}
				   );
			}
			
			function cancelTimer(mtId)
			{	  
     			if(confirm(getJsLocaleMessage("ydwx","ydwx_wxfschx_4")))
				{
				    var lgcorpcode =$("#lgcorpcode").val();
				    var lguserid =$("#lguserid").val();
					$.post("<%=request.getContextPath()%>/smt_smsSendedBox.htm",
							{
								method : "checkCancel",
								mtId:mtId,
								lgcorpcode:lgcorpcode,
								lguserid:lguserid
							},
							function(result)
							{
							  if(result=="true")
							  {
								$.post("<%=request.getContextPath()%>/smt_smsSendedBox.htm",
								{
									method : "changeState",
									mtId:mtId,
									subState : "3",
									lgcorpcode:lgcorpcode,
								    lguserid:lguserid
								},
								function(result)
								{
								  
									if(result=="cancelSuccess")
									{
										alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_5"));										
										$("#s"+mtId).html("<font color='#FE3E4D'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_6")+"</font>");
										$("#cz"+mtId).html("-");
										getCt();
										//location.reload();
										//location.href = location.href;
										document.forms['pageForm'].submit();
									}else
									{
										alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_7"));
									}
								}
							   );
							  }
							  else
							  {
							    alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_8"));
							  }	
					  		//$('#btnReturn').attr("disabled","");
			        		//$('#btnOk').attr("disabled","");
			        	//	$('#btnConcel').attr("disabled","");							
							}
						);
				}
			}			


			<%-- 点详细，弹出框--%>
			function openSmsDetail(mtId,userId){
				 var pathUrl = $("#pathUrl").val();
				 $('#recordTableDiv').hide();
				 $.post("<%=request.getContextPath()%>/wx_<%=titlePath %>.htm?method=getSmsDetail",{mtId:mtId,userId:userId},function(jsonObject){
					 var json = eval("("+jsonObject+")");
					//是否有 审批记录
					var haveRecord = json.haveRecord;
					var firstshowname = json.firstshowname;
					var firstcondition = json.firstcondition;
					var secondshowname = json.secondshowname;
					var secondcondition = json.secondcondition;
					var isshow = json.isshow;
					$("#recordTable").empty();
					var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_9")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_10")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_11")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_12")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_13")+"</td></tr>";
					if(haveRecord == "1"){
						//审批记录
						var recordList = json.members;
						 for(var i= 0;i<recordList.length;i++){
								var sms_Rlevel = recordList[i].mmsRlevel;
								var sms_Reviname = recordList[i].mmsReviname;
								var sms_Exstate = recordList[i].mmsexstate;
								var sms_Comments = recordList[i].mmsComments;
								var sms_rtime = recordList[i].mmsrtime;
								msg = msg  +" <tr class='div_bd' height='24px'> <td  align='center' width='10%'  class='div_bd'>"+sms_Rlevel+"</td> "
				        		+"  <td align='center'  width='15%'  class='div_bd'>"+sms_Reviname+"</td>"          
						        +"  <td align='center'  width='15%'  class='div_bd'>"+sms_Exstate+"</td> "
						        +"  <td align='left'  width='35%'  style='word-break: break-all;'  class='div_bd'>";

						        var view_sms_Comments=sms_Comments.length>17?(sms_Comments.substr(0,17)+"..."):sms_Comments;
						        msg=msg+"<a onclick='javascript:modify(this,2)' style='cursor: pointer;'><label style='display:none'><xmp>"+sms_Comments+"</xmp></label>"
								+"<xmp>"+view_sms_Comments+"</xmp></a>"
						        +"</td> <td align='center'  width='25%'  class='div_bd'>"+sms_rtime+"</td>  </tr>" ;
						}
					}else{
						msg = msg +	"<tr><td colspan='5' align='center'  class='div_bd' height='24px'>"+getJsLocaleMessage("common","common_norecord")+"</td></tr>";
					}
					$("#recordTable").html(msg);
					
					$('#recordTableDiv').show();	
					$("#nextrecordmgs").empty();
					if(isshow == "1"){
						var recordmsg =" "+ getJsLocaleMessage("ydwx","ydwx_wxfschx_14")+"&nbsp;" + firstshowname;
						if(firstcondition == "1"){
							recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_17");
						}else if(firstcondition == "2"){
							recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_15");
						}
						if(secondshowname != "" && secondcondition != ""){
							recordmsg = recordmsg + "</br>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_16")+"&nbsp;" + secondshowname;
							if(secondcondition == "1"){
								recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_17");
							}else if(secondcondition == "2"){
								recordmsg = recordmsg + "   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_15");
							}
						}
						$("#nextrecordmgs").html(recordmsg);
						$('#nextrecordmgs').show();	
					}
					 $("#smsdetailinfo").dialog("open");
				 });
			}		
			//点详细，弹出框
function openReviewFlow(mtId,userId,reviewType){
	$('#reviewTableDiv').hide();
	$.post("reviewflow.htm?method=getReviewFlow",{mtId:mtId,userId:userId,reviewType:reviewType},function(jsonObject){
		 var json = eval("("+jsonObject+")");
		//是否有 审批记录
		var haveRecord = json.haveRecord;

		var onelevel = json.onelevel;
		var onecondition = json.onecondition;
		var twolevel = json.twolevel;
		var twocondition = json.twocondition;
		var threelevel = json.threelevel;
		var threecondition = json.threecondition;
		var fourlevel = json.fourlevel;
		var fourcondition = json.fourcondition;
		var fivelevel = json.fivelevel;
		var fivecondition = json.fivecondition;
		var isshow = json.isshow;
		$("#reviewTable").empty();
		var msg = "<tr class='div_bd div_bg'  height='29px'  style='font-size:13px;'><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_9")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_10")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_36")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_11")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_12")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_13")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_37")+"</td><td class='div_bd'>"+getJsLocaleMessage("ydwx","ydwx_common_caozuo")+"</td></tr>";
		if(haveRecord == "1"){
			//审批记录
			var recordList = json.members;
			 for(var i= 0;i<recordList.length;i++){
					var Rlevel = recordList[i].Rlevel;
					var Reviname = recordList[i].Reviname;
					var Exstate = recordList[i].exstate;
					var Exresult= recordList[i].exresult;
					var Comments = recordList[i].Comments;
					var rtime = recordList[i].rtime;
					var remindtime=recordList[i].remindtime;
					var allowremind=recordList[i].allowremind;
					var frid=recordList[i].flowid;
					var existreviewer=recordList[i].existreviewer;
					if(existreviewer=="1"){
						msg = msg  +" <tr class='div_bd' height='24px'> <td  align='center' width='10%'  class='div_bd'>"+Rlevel+"</td> "
		        		+"  <td align='center'  width='10%'  class='div_bd'>"+Reviname+"</td>"          
				        +"  <td align='center'  width='10%'  class='div_bd'>"+Exstate+"</td> "
				         +"  <td align='center'  width='10%'  class='div_bd'>"+Exresult+"</td> "
				        +"  <td align='left'  width='20%'  style='word-break: break-all;'  class='div_bd'>";
	
				        var view_Comments=Comments.length>17?(Comments.substr(0,17)+"..."):Comments;
				        msg=msg+"<a onclick='javascript:modify(this,2)' style='cursor: pointer;'><label style='display:none'><xmp>"+Comments+"</xmp></label>"
						+"<xmp>"+view_Comments+"</xmp></a>"
				        +"</td> <td align='center'  width='15%'  class='div_bd'>"+rtime+"</td>" 
				        +"</td> <td align='center'  width='15%'  class='div_bd'>"+remindtime+"</td>" ;
				        if(allowremind=="1"){
				        	msg=msg+"<td align='center'  width='10%'  class='div_bd'><a onclick='javascript:remind("+frid+")' style='cursor: pointer;color:blue;'>"+getJsLocaleMessage("ydwx","ydwx_wxfschx_38")+"</a></td>" +"</tr>" ;
				        }else{
				        	msg=msg+"<td align='center'  width='10%'  class='div_bd'>"+allowremind+"</td>" +"</tr>" ;
				        }
			        }else{
			        	msg = msg  +" <tr class='div_bd' height='24px'> <td  align='center' width='10%'  class='div_bd'>"+Rlevel+"</td> "
		        		+"  <td colspan='6' align='center'  width='80%'  class='div_bd' style='color:red;'>"+Exstate+"</td>";          
				        msg=msg+"<td align='center'  width='10%'  class='div_bd'>-</td>" +"</tr>" ;
				        
			        }
			        
			}
		}else{
			msg = msg +	"<tr><td colspan='8' align='center'  class='div_bd' height='24px'>"+getJsLocaleMessage("ydwx","ydwx_common_wujilu")+"</td></tr>";
		}
		$("#reviewTable").html(msg);
		
		$('#reviewTableDiv').show();	
		$("#nextreviewmgs").empty();
		if(isshow == "1"){
			var recordmsg = "";
				if(onelevel=="1"){
					recordmsg=recordmsg+"1"+getJsLocaleMessage("ydwx","ydwx_wxfschx_18");
					if(onecondition == "1"){
						recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_17");
					}else if(onecondition == "2"){
						recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_15");
					}
				}
				if(twolevel=="2"){
					recordmsg=recordmsg+"</br>2"+getJsLocaleMessage("ydwx","ydwx_wxfschx_18");
					if(twocondition == "1"){
						recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_17");
					}else if(twocondition == "2"){
						recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_15");
					}
				}
				if(threelevel=="3"){
					recordmsg=recordmsg+"</br>3"+getJsLocaleMessage("ydwx","ydwx_wxfschx_18");
					if(threecondition == "1"){
						recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_17");
					}else if(threecondition == "2"){
					recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_15");
					}
				}
				if(fourlevel=="4"){
					recordmsg=recordmsg+"</br>4"+getJsLocaleMessage("ydwx","ydwx_wxfschx_18");
					if(fourcondition == "1"){
						recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_17");
					}else if(fourcondition == "2"){
						recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_15");
					}
				}
				if(fivelevel=="5"){
					recordmsg=recordmsg+"</br>5"+getJsLocaleMessage("ydwx","ydwx_wxfschx_18");
					if(fivecondition == "1"){
						recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_17");
					}else if(fivecondition == "2"){
						recordmsg = recordmsg +"   &nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("ydwx","ydwx_wxfschx_15");
					}
				}
			$("#nextreviewmgs").html(recordmsg);
			$('#nextreviewmgs').show();	
		}
		 $("#reviewflowinfo").dialog("open");
	 });
}

function remind(frid){
		$.post("reviewflow.htm?method=cuibanFlow&frid="+frid,
				{},
				function(result)
				{
				  if(result=="success")
				  {
					alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_19"));
				  }
				  else if(result=="getTaskFail")
				  {
				    alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_20"));
				  }
				  else if(result=="getDcTempFail")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_21"));
				  }
				  else if(result=="getWxTempFail")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_22"));
				  }
				  else if(result=="noPhone")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_23"));
				  }
				  else if(result=="noContent")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_24"));
				  }
				  else if(result=="noAdmin")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_25"));
				  }
				  else if(result=="noAgree")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_26"));
				  }
				  else if(result=="noDisAgree")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_27"));
				  }
				  else if(result=="noSP")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_28"));
				  }
				  else if(result=="noSubNo")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_29"));
				  }
				  else if(result=="noSpNumber")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_30"));
				  }
				  else if(result=="validPhone")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_31"));
				  }
				  else if(result=="wgkoufeiFail")
				  {
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_32"));
				  }else{
					  alert(getJsLocaleMessage("ydwx","ydwx_wxfschx_33"));
				  }
				}
			);
}
		</script>
		<script type="text/javascript" src="<%=iPath %>/js/taskReport.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	</body>
</html>
