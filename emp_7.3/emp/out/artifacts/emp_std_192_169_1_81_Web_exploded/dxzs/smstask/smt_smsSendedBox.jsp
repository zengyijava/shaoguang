<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.common.vo.LfMttaskVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
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
	
	String actionPath = (String)request.getAttribute("actionPath");
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
	String taskState= request.getParameter("taskState");
	if(taskState==null || "".equals(taskState))
	{
		taskState="0";
	}
	
	String taskType= request.getParameter("taskType");
	if(taskType==null || "".equals(taskType))
	{
		taskType="0";
	}
	
	//是否包含子机构
	String isContainsSun=(String)request.getAttribute("isContainsSun");
	if(isContainsSun==null||"".equals(isContainsSun)){
		isContainsSun="1";
	}
	
	//任务批次
	String taskID= request.getParameter("taskID");
	if(taskID==null||"".equals(taskID.trim())){
		taskID="";
	}else{
		try{
			Long.parseLong(taskID.trim());
			taskID=taskID.trim();
		}catch(Exception e){
			EmpExecutionContext.error(e, "任务批次号转换异常!taskID:" + taskID);
			taskID="";
		}
	}
	
	//信息内容
	String msg=request.getParameter("msg");
	//对msg的处理
	if(msg==null||"".equals(msg.trim()))
	{
		msg="";
	}
	
	int corpType = StaticValue.getCORPTYPE();
	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	String zNodes3 = (String)request.getAttribute("departmentTree");
	//String zNodes2 = (String)request.getAttribute("deptUserTree");
	
	String findResult= (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?
			"default":(String)session.getAttribute("stlyeSkin");
			
	String httpUrl = StaticValue.getFileServerViewurl();
	String taskid = (String)request.getParameter("taskid");
    String flowId = request.getParameter("flowId");
    
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    String lguserid = null != request.getAttribute("lguserid")?request.getAttribute("lguserid").toString():"";

    String pserch = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_144", request);
    String empSd = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_156", request);
    String httpSd = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_157", request);
    String intime = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_166", request);
    String lgoff = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_139", request);
    
    String dsp = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_149", request);
    String spbtg = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_150", request);
    String ydj = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_154", request);
    String ycx = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_152", request);
    String cswfs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_155", request);
    String yfs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_153", request);
    String dfs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_151", request);
    String xjwj = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_205", request);
    String cs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_223", request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>sendSMS.html</title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	    <%--<link href="<%=inheritPath %>/styles/easyui.css?V=<%=StaticValue.JSP_IMP_VERSION %>" rel="stylesheet" type="text/css" />--%>
	    <style type="text/css">
	    	.smt_smsSendedBox div#tooltip {
				position:absolute; 
				z-index:1000; 
				max-width:435px; 
				_width:expression(this.scrollWidth > 435 ? "435px" : "auto"); 
				width:auto; 
				background:#A8CFF6; 
				border:#FEFFD4 solid 1px; 
				text-align:left; 
				padding:6px;
			}
	    </style>
	    <link rel="stylesheet" type="text/css" href="<%=iPath %>/css/smt_smsSendedBox.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydzs_DuanXinZhuShou.css?V=<%=StaticValue.getJspImpVersion() %>"/>
        <link rel="stylesheet" type="text/css" href="<%=iPath %>/css/smt_smsCustomField.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>

	<body class="smt_smsSendedBox">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%-- 表示请求的名称 --%>
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<input type="hidden" name="htmName" id="htmName" value="<%=actionPath %>">
			<form name="pageForm" action="<%=actionPath%>" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
                        <%if(flowId == null){%>
                            <a id="exportCondition"><emp:message key="dxzs_xtnrqf_button_12" defVal="导出" fileName="dxzs"/></a>
                        <%}%>
					</div>
					<div id="condition">
						<table>
						   
							<tbody>
								<tr>
									<td>
										<emp:message key="dxzs_xtnrqf_title_143" defVal="隶属机构" fileName="dxzs"/>：
									</td>									
									<td class="condi_f_l">	
											
									  		<div class="dxzs_div">	 
									  		 <input type="hidden" id="deptid" name="depid" value="<%=deptid==null?"":deptid%>"/> 		
									  		<input type="text" id="depNam" class="treeInput dxzs_depNam" name="depNam" value="<%=depNam==null?pserch:depNam%>"  onclick="javascript:showMenu();"  readonly />
											</div>														
											<div id="dropMenu" >
											<iframe class="dxzs_iframe" frameborder="0" src="about:blank"></iframe>	
												<div class="dxzs_div1">
												     <input type="checkbox" id="isContainsSun" name="isContainsSun" class="dxzs_isContainsSun" <%if(isContainsSun.equals("1")){%>checked="checked" <%}%> value="1"/><emp:message key="dxzs_xtnrqf_title_145" defVal="包含子机构" fileName="dxzs"/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='dxzs_xtnrqf_button_4' defVal='确定' fileName='dxzs'/>" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" />&nbsp;&nbsp;
													<input type="button" value="<emp:message key='dxzs_xtnrqf_button_14' defVal='清空' fileName='dxzs'/>" class="btnClass1" onclick="cleanSelect_dep3();" />
												</div>	
												<ul id="dropdownMenu"  class="tree"></ul>	
											</div>			
									</td>
									<td>
										<emp:message key="dxzs_xtnrqf_title_146" defVal="操作员" fileName="dxzs"/>：
									</td>
									<td class="condi_f_l">	
											
											<div class="dxzs_div2">
											
											 <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
											<input type="text" class="treeInput dxzs_input" id="userName" name="userName" value="<%=userName==null?pserch:userName%>" readonly onclick="javascript:showMenu2();"/>&nbsp;
<%--											<a onclick="javascript:showMenu2();" style="cursor: pointer;text-decoration: underline">选择</a>--%>
											</div>											
											<div id="dropMenu2">
											<iframe class="dxzs_iframe1" frameborder="0" src="about:blank"></iframe>	
												<div class="dxzs_div3">
													<input type="button" value="<emp:message key='dxzs_xtnrqf_button_4' defVal='确定' fileName='dxzs'/>" class="btnClass1" onclick="javascript:zTreeOnClickOK2();" />&nbsp;&nbsp;
													<input type="button" value="<emp:message key='dxzs_xtnrqf_button_14' defVal='清空' fileName='dxzs'/>" class="btnClass1" onclick="javascript:cleanSelect_dep();"/>
												</div>
												<div class="dxzs_div4"><font class="zhu"><emp:message key="dxzs_xtnrqf_title_147" defVal="注：请勾选操作员进行查询" fileName="dxzs"/></font></div>	
												<ul  id="dropdownMenu2" class="tree"></ul>	
											</div>										   										
									</td>
									<td><emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/>：</td>
									  
									<td>	
  		                                 	<label>
											<select name="spUser" id="spUser" class="dxzs_spUser">
												<option value="">
													<emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/>
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
									<td class="tdSer">
									       <center><a id="search"></a></center>
								    </td>		
								</tr>
								<tr>
								
								<%--	<td>
										业务类型：
									</td>
									<td>
									   <label>
											<select name="busCode" id="busCode" value="<%=busCode%>" style="width: 180px">
												<option value="">
													全部
												</option>
												<%
														if(busList != null && busList.size()>0){													
														  for(LfBusManager lfbusManager:busList){												    
													 %>
												<option value="<%=lfbusManager.getBusCode() %>"
													<%=lfbusManager.getBusCode().equals(busCode)?"selected":"" %>>
													<%=lfbusManager.getBusName() %>
												</option>
												<%
											            }
											           }
											          %>
											</select>
										</label>
									</td> --%>
							   	   
								    <td><emp:message key="dxzs_xtnrqf_title_148" defVal="任务状态" fileName="dxzs"/>：</td>
								    <td>
								    	<select name="taskState" id="taskState" class="dxzs_spUser" isInput="false">
								    		<option value="0"><emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/></option>
								    		<%if(!"sendTask".equals(titlePath)){%>
								    		<option value="1" <%="1".equals(taskState)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_149" defVal="待审批" fileName="dxzs"/></option>
								    		<option value="2" <%="2".equals(taskState)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_150" defVal="审批不通过" fileName="dxzs"/></option>
								    		<%}%>
								    		<option value="3" <%="3".equals(taskState)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_151" defVal="待发送" fileName="dxzs"/></option>
								    		<option value="4" <%="4".equals(taskState)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_152" defVal="已撤销" fileName="dxzs"/></option>
								    		<option value="5" <%="5".equals(taskState)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_153" defVal="已发送" fileName="dxzs"/></option>
								    		<option value="6" <%="6".equals(taskState)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_154" defVal="已冻结" fileName="dxzs"/></option>
								    		<option value="7" <%="7".equals(taskState)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_155" defVal="超时未发送" fileName="dxzs"/></option>
								    	</select>
								    </td>
								    <td>
								       <emp:message key="dxzs_xtnrqf_title_252" defVal="业务类型" fileName="dxzs"/>：
								   </td>
								   <td>
								    	<select name="taskType" id="taskType" class="dxzs_spUser" isInput="false">
								    		<option value="0"><emp:message key="dxzs_xtnrqf_title_100" defVal="全部" fileName="dxzs"/></option>
								    		<option value="1" <%="1".equals(taskType)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_156" defVal="EMP发送" fileName="dxzs"/></option>
								    		<option value="2" <%="2".equals(taskType)?"selected":"" %>><emp:message key="dxzs_xtnrqf_title_157" defVal="HTTP接入" fileName="dxzs"/></option>
								    	</select>
								   </td>
								   <td><emp:message key="dxzs_xtnrqf_title_159" defVal="任务批次" fileName="dxzs"/>：</td>
								   <td>
								      <input type="text" id="taskID" name ="taskID" class="dxzs_spUser" onkeyup="javascript:numberControl($(this))" value="<%="".equals(taskID)?"":taskID%>" maxlength="19">		
								    </td>
								     <td></td>
								</tr>
								<tr>
								 
								    <td>
								      <emp:message key="dxzs_xtnrqf_title_160" defVal="创建时间" fileName="dxzs"/>：
								   </td>
								   <td>
								    	<input type="text"
											readonly="readonly"  onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime" class="dxzs_input Wdate"
												 <%--onchange="onblourSendTime('end')" --%>>
								   </td>
								   <td><emp:message key="dxzs_xtnrqf_title_135" defVal="至" fileName="dxzs"/>：</td>
								   <td>
								      	<input type="text"
											readonly="readonly"  onclick="rtime()" 
											value="<%=recvtime==null?"":recvtime %>" 
											 id="recvtime" name="recvtime"  class="dxzs_input Wdate"<%--onchange="onblourSendTime('end')" --%>>
								    </td>
								    <td><emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/>：</td>
								    <td>
                                     <input type="text" id="msg" name ="msg" class="dxzs_spUser"  value="<%=msg== null?"":msg %>">
								    </td>
								     <td></td>
								</tr>
								<%if(flowId != null){%>
								<tr>
									<td><emp:message key="dxzs_xtnrqf_title_162" defVal="审核流程ID：" fileName="dxzs"/></td>
									<td><input type="text" name="flowId" class="dxzs_spUser" value="<%=flowId%>" onkeyup="javascript:numberControl($(this))" maxlength="19"/></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								<%}%>
                                <tr>
                                    <td colspan="7">
                                        <input type="hidden" id="cookieName" value="smsSendedBox<%=lguserid%>"/>
                                        <div id="ctlIcon" onclick="customShowPanel()">
                                            <img src="<%=commonPath%>/common/img/<%=skin.contains("frame3.0")?"icon_more_3.0":"icon_more_4.0"%>.png" alt="">
                                            <i class="pull-icon" id="foldIcon"></i>
                                        </div>
                                        <div class="div-panel">
                                            <div class="panel-title">
                                                <span><strong>自定义查询列表展示</strong></span>
                                            </div>
                                            <div class="div-panel-content">
                                                <div>
                                                    <span>列表字段:</span>
                                                    <input type="checkbox" id="selectAll" checked="checked"/><span class="m4">全选</span>
                                                </div>
                                                <div id="field-div">

                                                </div>
                                            </div>
                                            <div class="warm-tip"><p>注：该功能仅代表界面展示，导出数据仍将包含以上全部字段。</p></div>

                                            <div class="btn-group">
                                                <span class="btnClass5 mr23" onclick="controlDisplay(true)">确定</span>
                                                <span class="btnClass6" onclick="controlHide()">取消</span>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th class="align-left">
									<emp:message key="dxzs_xtnrqf_title_146" defVal="操作员" fileName="dxzs"/>
								</th>
								<th class="align-left">
									<emp:message key="dxzs_xtnrqf_title_143" defVal="隶属机构" fileName="dxzs"/>
								</th>
								<th class="align-left">
									<emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_252" defVal="业务类型" fileName="dxzs"/>
								</th>
								<th class="align-left">
									<emp:message key="dxzs_xtnrqf_title_2" defVal="发送主题" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_159" defVal="任务批次" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_160" defVal="创建时间" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/>
								</th>

								<th>
									<emp:message key="dxzs_xtnrqf_title_250" defVal="完成时间" fileName="dxzs"/>
								</th>

								<th>
									<emp:message key="dxzs_xtnrqf_title_251" defVal="发送进度" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_45" defVal="有效号码数" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_192" defVal="提交信息数" fileName="dxzs"/>
								</th>
								 <th class="align-left">
                                     <emp:message key="dxzs_xtnrqf_title_12" defVal="发送内容" fileName="dxzs"/>
								 </th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_163" defVal="发送文件" fileName="dxzs"/>
								</th>
								<%--
								<th>
									审批详情
								</th>
								--%>
							    <th class="align-left">
								    <emp:message key="dxzs_xtnrqf_title_148" defVal="任务状态" fileName="dxzs"/>
								</th>
								<th>
								    <emp:message key="dxzs_xtnrqf_title_6" defVal="操作" fileName="dxzs"/>
								</th>
								
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="16" align="center"><emp:message key="dxzs_xtnrqf_title_164" defVal="请点击查询获取数据" fileName="dxzs"/></td></tr>
							<%
							} else if(mtList != null && mtList.size()>0)
							{
								for(LfMttaskVo mt : mtList)
								{
									if(taskid != null && !"".equals(taskid) && !"null".equals(taskid) && Long.valueOf(taskid) - mt.getTaskId() == 0)
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
											<td class="align-left">
												<%=mt.getName() %><%if(mt.getUserState()==2){out.print("<font color='red'>("+lgoff+")</font>");} %>
											</td>
											<td class="align-left">
												<xmp><%=mt.getDepName() %></xmp>
											</td>
											<td class="align-left">
												<%=mt.getSpUser() %>
											</td>
											<td>
											<% 
											if(mt.getTaskType()==1){
												out.print(empSd);
											}else{
												out.print(httpSd);
											}
											%>
											</td>
											<td class="align-left">
												<%=mt.getTitle()==null?"-":(mt.getTitle().replaceAll("<","&lt;").replace(">","&gt;")) %>
											</td>
											<td>
												<%=mt.getTaskId()==0?"-":mt.getTaskId()%>
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
												    if(mt.getSendstate()==0){out.print("("+intime+")");}
												}else if(mt.getSendstate()==1 || mt.getSendstate()==2){//发送成功或者发送失败
													out.print(df.format(mt.getTimerTime()));
												}else{
													out.print(mt.getTimerTime()==null?"-":df.format(mt.getTimerTime()));//这里面的情况就是sendstate=4(发送中)
												}
												%>
											</td>
								           <%-- 完成时间--%>
											<td id="endSendTime_<%=mt.getTaskId()%>">

											</td>
											<%-- 发送状态--%>
											<td>
                                                <div style="display: none" class="sendPercent" value="<%=mt.getTaskId()%>"></div>
                                                <div class="div0">
                                                    <div id="percentBar_<%=mt.getTaskId()%>" class="div1">

                                                    </div>
                                                    <div id="percentContent_<%=mt.getTaskId()%>" class="div2">

                                                    </div>
                                                </div>

<%--                                                <!--外层容器-->
												<div class="wrapper">
													<!--进度条容器-->
													<div id="<%=mt.getTaskId()%>" class="progressbar">
														<!--用来模仿进度条推进效果的进度条元素-->
														<div  class="fill"></div>
													</div>
												</div>--%>
											</td>
											<td>
													<%
														if(mt.getTaskType()==1){
															out.print(mt.getEffCount());
														}else{
															out.print("-");
														}
							 						%>
											</td>
											<td>
													<%
														if(mt.getTaskType()==1){
															out.print(mt.getIcount());
														}else{
															out.print("-");
														}
							 						%>
											</td>
							<td class="align-left">
							<%
							if(mt.getMsg()==null || "".equals(mt.getMsg())){
								if(mt.getTaskType()==1){
									out.print(xjwj);
								}else{
									out.print("-");
								}
							}else{ %>
								 <a onclick=modifyNew(this,1)>
									<xmp><%
									String st = "";
									String temp = mt.getMsg().replaceAll("#[pP]_(\\d+)#","{#"+cs+"$1#}");
									if(temp.length()>7)
									{
										st = temp.substring(0,7)+"...";
									}else
									{
										st = temp;
									}
									out.print(st);
									%></xmp>
									 <%--<label>--%>
									 <%--<xmp class="dxzs_display_none"><%=temp%></xmp>--%>
									 <%--</label>--%>
									 <textarea style="display:none"><%=temp %></textarea>
									 <xmp name="msgXmp" style="display:none"><%=temp %></xmp>
                                 </a>
								<%} %>
							</td>
							<td>
								<%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) == null||mt.getTaskType()==2 ){ %>	
								 <label>-</label>
								<%}else{ %>
								<a href="javascript:checkFileOuter('<%=mt.getMobileUrl()%>','<%=path%>')"><emp:message key="dxzs_xtnrqf_title_106" defVal="查看" fileName="dxzs"/></a>
								&nbsp;
								<a href="javascript:downloadFilesOuter('<%=mt.getMobileUrl()%>','<%=path%>')"><emp:message key="dxzs_xtnrqf_title_165" defVal="下载" fileName="dxzs"/></a>
								<%} %>
							</td>
							
							<td class="align-left">
								 <%
								    boolean flag = false;
								    Integer sendState = mt.getSendstate();
								    Integer subState = mt.getSubState();
								    String state=new String();
								    if(subState==2&&mt.getReState()==-1){
									    state = "<font color='#FE3E4D'>"+dsp+"</font>";
									    flag =true;
									}
								    else if(subState==2&&mt.getReState()==2){
									    state = "<font color='#FE3E4D'>"+spbtg+"</font>";
									    flag =true;
									}
									else if(subState ==4)
									{
									   state = "<font color='#FE3E4D'>"+ydj+"</font>";
									}
								    else if(subState==3){
								    	state = "<font color='#FE3E4D'>"+ycx+"</font>";
									}
								    else if(sendState==5){
								    	state="<font color='#FE3E4D'>"+cswfs+"</font>";
								    }
								    else if(sendState!=0){
								    	state = "<font color='#088dd2'>"+yfs+"</font>";
								    	 flag =true;
								    }
								    else if(subState==2){
									      if(mt.getReState()==-1){
									    	  state="<font color='#000000'>"+dsp+"</font>";
									    	  flag =true;
									      }else if(mt.getReState()==2){ 
									    	  state = "<font color='#FE3E4D'>"+spbtg+"</font>";
									    	  flag =true;
									      }else{ 
									    	  state = "<font color='#048e79'>"+dfs+"</font>";
									    	  flag =true;
									      }
									 }
								 %>	
								 <span id="s<%=mt.getMtId()%>">
									 <% 
									 	//如果类型为EMP界面发送，则可以看审核详情
									 	if(mt.getTaskType()==1){
									 		if(flag){
									 			if(mt.getReState()== -1){
										 		    %>
												     <%-- javascript:verify('<mt.getMtId()') --%>
													 <a onclick="javascript:openReviewFlow('<%=mt.getMtId() %>','<%=mt.getUserId() %>','1');" name="querySP">
													 	<%=state %>
													 </a>
													<%
									 			}else{
									 				out.print(state);
									 			}
									 		}else{
									 			out.print(state);
									 		}
								 		}else{
								 			out.print(state);
								 		}
									 %>	
								 </span>					
							</td>
							<%-- 
							<td>
							    <%
							   	 if(mt.getReState()-0 != 0){
							    %>
							     <!-- javascript:verify('<mt.getMtId()') -->
								 <a onclick="javascript:openSmsDetail('<%=mt.getMtId() %>','<%=mt.getUserId() %>');" name="querySP">查看</a>
								<%
							    }else{
								out.print("-");
								}
								%>
							</td> 
							--%>
							<td>
							  <span id="cz<%=mt.getMtId()%>">
							    <%
							      if(mt.getTaskType()==1){
										   if(mt.getSendstate()!=0 || mt.getSubState()==3 || mt.getReState()==2){

										   }
										   else{
											   if(mt.getSendstate()==0 && mt.getTimerStatus() == 1){
										 	%>
								 			 <a class="changeTiming " href="javascript:changeTiming('<%=mt.getMtId() %>','<%=mt.getMsgType()%>');"><emp:message key='dxzs_xtnrqf_title_246' defVal='修改' fileName='dxzs'></emp:message> </a>
											&nbsp;<% } %>
								  			<a title="<emp:message key='dxzs_xtnrqf_title_168' defVal='撤销任务' fileName='dxzs'/>" href="javascript:cancelTimer('<%=mt.getMtIdCipher()%>')"><emp:message key="dxzs_xtnrqf_title_167" defVal="撤销" fileName="dxzs"/></a>
											<%
										   }
										   //审批状态为同意和拒绝的时候，代表审批完成
										   if(mt.getReState()==1||mt.getReState()==2){
										   %>
										    <a onclick="javascript:openSmsDetail('<%=mt.getMtId() %>','<%=mt.getUserId() %>');" name="querySP"><emp:message key="dxzs_xtnrqf_title_106" defVal="查看" fileName="dxzs"/></a>
										   <%
										   }else{

										   }
								   }else{
								   			out.print("-");
								   }
								 %>		
								 </span>
							 </td>
						</tr>
						<%
								}
							}else{
						%>
						<tr><td align="center" colspan="16"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/></td></tr>
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
			<div id="shenhe" class="dxzs_shenhe" title="<emp:message key='dxzs_xtnrqf_title_169' defVal='审批详情' fileName='dxzs'/>">
			
			</div>
			<%-- 内容结束 --%>
			
			<div id="modify" class="dxzs_shenhe" title="<emp:message key='dxzs_xtnrqf_title_111' defVal='信息内容' fileName='dxzs'/>">
				<table width="100%">
					<thead>
						<tr class="dxzs_tr">
							<td class="dxzs_word_break">
<%--								用label显示短信内容--%>
								<%--<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>--%>
								<span><textarea id="msgcont" class="dxzs_msgcont" rows="15" readonly="readonly"></textarea></span>
								 
							</td>
							 
						</tr>
<%--					   <tr style="padding-top:2px;">--%>
<%--							<td>--%>
<%--							</td>--%>
<%--							</tr>--%>
						 
					</thead>
				</table>
			</div>

			<%-- 修改定时 --%>
			<div id="changeTimingDiv" class="dxzs_changeTimingDiv" title="<emp:message key='dxzs_xtnrqf_title_249' defVal='定时任务修改' fileName='dxzs'/>">
				<iframe id="changeTimingFrame" class="dxzs_changeTimingFrame" name="changeTimingFrame" marginwidth="0" scrolling="no" frameborder="no" width="100%" height="100%"></iframe>
			</div>
			
			<div id="smsdetailinfo" title="<emp:message key='dxzs_xtnrqf_title_169' defVal='审批详情' fileName='dxzs'/>" class="dxzs_smsdetailinfo">
				<div id="recordTableDiv" class="dxzs_recordTableDiv" align="center">
						<table id="recordTable" class="dxzs_recordTable" >
						</table>
				</div>
				<div id="nextrecordmgs" class="dxzs_nextrecordmgs" align="left">
				</div>
			</div>	
			<div id="reviewflowinfo" title="<emp:message key='dxzs_xtnrqf_title_149' defVal='待审批' fileName='dxzs'/>" clas="dxzs_smsdetailinfo">
				<div class="dxzs_recordTableDiv" id="reviewTableDiv" align="center">
						<table class="dxzs_recordTable" id="reviewTable">
						</table>
				</div>
				<div class="dxzs_nextrecordmgs" id="nextreviewmgs" align="left">
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
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/smt_smsSendedBox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/smsSendedBox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=iPath %>/js/smt_smsCustomField.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
			$(document).ready(function() {
			    //getLoginInfo("#smssendparams");
			    var findresult="<%=findResult%>";
			    closeTreeFun(["dropMenu2","dropMenu"]);
			    if(findresult=="-1")
			    {
			       alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_77'));	
			       return;			       
			    }
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
                $('#spUser').isSearchSelect({'width':'182','isInput':true,'zindex':0});
				//导出全部数据到excel
				$("#exportCondition").click(
				    function()
				    {
					
						if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_210')))
						{
							var sendtime = '<%=sendtime!=null?sendtime:""%>';
							var recvtime = '<%=recvtime!=null?recvtime:""%>';
					   		var userName = '<%=userid!=null?userid:""%>';
					   	    var depNam = '<%=deptid!=null?deptid:""%>';	
					   	    var spuser = '<%=spUser!=null?spUser:""%>';	
					   	    
					   	    var lgcorpcode =$("#lgcorpcode").val();
					        var lguserid =$("#lguserid").val();		   	    
					   	    var taskState='<%=taskState%>';
					   	    var taskType='<%=taskType%>';
					   	    var taskID='<%=taskID%>';
					   	    var msg='<%=msg%>';
					   	    var isContainsSun='<%=isContainsSun%>';
					   		<%
					   		if(mtList!=null && mtList.size()>0 && pageInfo.getTotalRec()<=500000){
					   		%>	
					   				$.ajax({
									type: "POST",
									url: "<%=path%>/<%=actionPath%>?method=ReportAllPageExcel",
									data: {sendtime:sendtime,
											recvtime:recvtime,
											userid:userName,
											depid:depNam,
											spuser:spuser,
											pageIndex:<%=pageInfo.getPageIndex()%>,
											pageSize:<%=pageInfo.getPageSize()%>,
											lguserid:lguserid,
											lgcorpcode:lgcorpcode,
											taskState:taskState,
											taskType:taskType,
											taskID:taskID,
											msg:msg,
											isContainsSun:isContainsSun
											},
					                beforeSend:function () {
										page_loading();
					                },
					                complete:function () {
								    	page_complete();
					                },
									success: function(result){
					                        if(result=='true'){
					                           download_href("<%=path%>/<%=actionPath%>?method=downloadFile&exporttype=smssendedbox_export");
					                        }else{
					                            alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_211'));
					                        }
						   			}
								});		
					   		<%	
					   		}
					   		else if(mtList!=null && pageInfo.getTotalRec()>500000)
					   		{
					   		%>
					   		   alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_272'));
					   		<%}	else{
					   		%>
					   		    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_212'));
					   		<%
					   		}%>
					   }				 
				});
			});
		</script>
	</body>
</html>
