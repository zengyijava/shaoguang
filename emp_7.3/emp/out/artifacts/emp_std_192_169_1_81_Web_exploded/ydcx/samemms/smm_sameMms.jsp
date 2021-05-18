<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.entity.pasroute.LfMmsAccbind"%>
<%@page import="com.montnets.emp.entity.template.LfTemplate"%>
<%@page import="com.montnets.emp.entity.ydcx.LfDfadvanced"%>
<%@ page import="com.montnets.emp.servmodule.ydcx.constant.ServerInof"%>
<%@ page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%

	String path = request.getContextPath();
	String isTrue = (String) request.getAttribute("isTrue");
	String context = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = request.getRequestURI().substring(0,context.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session
			.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String menuCode = titleMap.get("sameMms");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	
	@ SuppressWarnings("unchecked")
	List<LfTemplate> mmsList = (List<LfTemplate>)request.getAttribute("mmsTempList");
	
	Integer templateSize = mmsList.size();
	
	//String isFlow = request.getAttribute("isFlow").toString();
	
	@ SuppressWarnings("unchecked")
	List<LfMmsAccbind> mmsAccbinds = (List<LfMmsAccbind>)request.getAttribute("mmsAccbinds");
	
	String taskId = request.getAttribute("taskId").toString();
	
	String lguserid = request.getParameter("lguserid");
	
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");    	
    String lgcorpcode = request.getParameter("lgcorpcode");
    String guid = request.getAttribute("guId").toString();
    //服务器名称
	String serverName = ServerInof.getServerName();
	
	//高级默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
  
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title>静态彩信发送页面</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/css/stasendmms.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/sendBatchSms.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=iPath%>/css/smmsamenms.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/ydcx_applicationMms.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	</head>
	<body id="ydcx_smm_sameMms">
		<div id="container" class="container">
			<%-- header开始 --%>
			<%--<%=ViewParams.getPosition(menuCode) %>
			--%><%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
					<input id="pathUrl" value="<%=path%>" type="hidden" />
					<input type="hidden" id="ipathUrl" value="<%=iPath %>"/>
					<input type="hidden" id="skinUrl" value="<%=skin %>"/>
					<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
					<input type="hidden" id="lgcorpcode" value="<%=lgcorpcode %>"/>
					<input type="hidden" name="lguserid" value="<%=lguserid %>">
					<input title="" style="display:none;" id="rightSelectedUserOption"/>
					<%--初始化操作员信息 --%>
					<div id="batchFileSend"  class="block ydcx_batchFileSend">
						<form class="ydcx_form2" id="form2" name="form2"
							action="smm_sameMms.htm?method=upNumber&langName=<%=langName %>" method="post"
							enctype="multipart/form-data" target="hidden_iframe">
								<%--新增单个手机号码 的号码集合  --%>
								<input type="hidden" id="phoneStr" name="phoneStr" value=""/>
								<%--批量输入手机号码 的号码集合  --%>
								<input type="hidden" id="inputphone" name="inputphone" value=""/>			
								<%--批量输入和手工输入手机号码 的号码集合  --%>
								<input type="hidden" id="allinputphone" name="allinputphone" value=""/>						
								<%--员工机构IDS --%>
								<input type="hidden" id="empDepIds"  name="empDepIds" value=""/>
								<%--客户机构IDS  --%>
								<input type="hidden" id="cliDepIds"  name="cliDepIds" value=""/>
								<%--群组机构IDS  --%>
								<input type="hidden" id="groupIds"  name="groupIds" value=""/>
								<%--员工IDS  --%>
								<input type="hidden" id="empIds"  name="empIds" value=""/>
								<%--客户IDS  --%>
								<input type="hidden" id="cliIds"  name="cliIds" value=""/>
								<%--外部人员IDS --%>
								<input type="hidden" id="malIds"  name="malIds" value=""/>
								<%--表示是否有选择对象的那一行      1表示有  2表示没有 --%>
								<input type="hidden" id="havOne"  name="havOne" value="2"/>
								<%--是否是计费用户 --%>
								<input type="hidden" id="isChargings"  name="isChargings" value=""/>
								<%--选择对象中，选择用户时的号码串 --%>
								<input type="hidden" id="userMoblieStr"  name="userMoblieStr" value=""/>
								<input type="hidden" id="error" name="error" value="" />
								<%--彩信任务ID--%>
								<input type="hidden" id="taskId" name="taskId" value="<%=taskId %>" />
								<%--彩信发送成功的任务ID--%>
								<input type="hidden" id="oldTaskId" name="oldTaskId" value="" />
								<%--彩信预览后的号码文件--%>
								<input type="hidden" id="upNumberPhoneUrl" name="upNumberPhoneUrl" value="" />
								<div style="display:none" id="hiddenValueDiv"></div>
								<%--上传 号码文件的文件名称集合--%>
								<div style="display: none;" id="allfilename" ></div>
								<%--皮肤类型--%>
								<input type="hidden" id="skinType" name="skinType" value="<%=skin %>" />
								<%--员工机构Ids--%>
								<input type="hidden" id="depIdStr" name="depIdStr" value="" />
								<%--群组Ids--%>
								<input type="hidden" id="groupStr" name="groupStr" value="" />
								
								<%--是选择 彩信模板 1  还是上传彩信文件  2--%>
								<input type="hidden" id="teplortms" name="teplortms" value="" />
								
								<div id="eq_sendDiv">
								<span  class="righttitle"><emp:message key="ydcx_cxyy_jtcxfs_text_1" 
										defVal="彩信主题：" fileName="ydcx"/></span>
										<input id="mmstaskname" name="mmstaskname" type="text" value="<emp:message key="ydcx_cxyy_jtcxfs_text_2" 
											defVal="不作为彩信内容发送" fileName="ydcx"/>" class="graytext input_bd div_bd ydcx_mmstaskname"  maxlength="20" />
								</div>
								
								<div id="eq_sendDiv">
								<span  class="righttitle"><emp:message key="ydcx_cxyy_jtcxfs_text_3" 
										defVal="彩信标题：" fileName="ydcx"/></span>
										<input id="tmName" name="tmName" type="text"  class="graytext input_bd div_bd ydcx_tmName"  maxlength="20" />
								</div>
								
								
								<div class="clear2"></div>
								<div id="eq_sendDiv">
									<span  class="righttitle"><emp:message key="ydcx_cxyy_jtcxfs_text_4" 
										defVal="发送号码：" fileName="ydcx"/></span>
									<div id="getObject" class="div_bd">
										<table id="infomaTable" width="100%" class="ydcx_infomaTable">
											<tr id="first" class="title_bg">
												<td width="82"  class="div_bd ydcx_td1" align="center"><emp:message key="ydcx_cxyy_jtcxfs_text_5" 
										defVal="类型" fileName="ydcx"/></td>
												<td   class="div_bd ydcx_td2" align="center"><emp:message key="ydcx_cxyy_jtcxfs_text_6" 
										defVal="号码" fileName="ydcx"/></td>
												<td width="82"  class="div_bd ydcx_td3" align="center"><emp:message key="ydcx_cxyy_jtcxfs_text_7" 
										defVal="操作" fileName="ydcx"/></td>
											</tr>
										</table>
									</div>
									<div class="emp_btn_table" id="emp_btn_table" class="ydcx_emp_btn_dv">
									<table width="100%" class="ydcx_emp_btn_table">
											<tr class="ydcx_picTab"  id="picTab">
												<td align="center"  class="div_bd  div_bg" onclick="javascript:showInfo();">
												
												<div class="ydcx_employee_dv">
										<a class="selectEmp"></a>
										<div class="mt10"><emp:message key="ydcx_cxyy_jtcxfs_text_8" 
										defVal="选择人员" fileName="ydcx"/></div>
										</div>
												</td>
											
												<td  class="div_bd  div_bg"  align="center" style='cursor:pointer;' id="import" >
			                                         
													 <div style="position:relative;width:81px;">
													 <a class="importFile"></a>
													 <div class="mt10"><emp:message key="ydcx_cxyy_jtcxfs_text_9" 
										defVal="导入文件" fileName="ydcx"/></div>
			                                         <div id="filesdiv" class="ydcx_filesdiv">
													 <input id='numFile1' name='numFile1'  type=file onchange="addFilesNoModel()" class="numfileclass ydcx_numFile1" />										
													 </div></div>
												</td>
											
											
											<%--新增批量导入start--%>
									
									<td  class="div_bd div_bg" align="center" style='cursor:pointer;width:85px;' onclick="bulkImport();"> 
									
                                         
										 <a class="bulkImport"/></a><div class="mt10"><emp:message key="ydcx_cxyy_jtcxfs_text_10" 
										defVal="批量输入" fileName="ydcx"/></div>
										 							
                                         
										</td>
									
									<%--新增批量导入end--%>
									</tr>
									</tr>
											<tr>
												<td colspan="3" align="center" class="div_bd  div_bg">
													<table width="235px">
													<tr>
													<td width="190px" class="div_bd">
													<input value="<emp:message key="ydcx_cxyy_jtcxfs_text_11" 
										defVal="请输入手机号" fileName="ydcx"/>"  type="text" class="graytext input_bd ydcx_tph"
													onkeyup="if(this.value!=this.value.replace(/\D/g,'')) this.value=this.value.replace(/\D/g,'')"
													onpaste="return !clipboardData.getData('text').match(/\D/)"
													maxlength="11" name="tph" id="tph" onpropertychange="addphone('1');"/>
													</td>
													<td align="center" onclick="addphone('2');" style="cursor:pointer" class="div_bd"><emp:message key="ydcx_cxyy_jtcxfs_text_12" 
										defVal="添加" fileName="ydcx"/></td>
													</tr>
													</table>
												</td>
											</tr>
										</table>
											<%--onclick="javascript:openTemp('smsTem0.txt');" --%>
									</div>
									
									
									
								</div>
								<div style="clear:both;"></div>
								<div class="ydcx_downlinks_dv" align="right">
							<a id="downlinks" class="ydcx_downlinks"><emp:message key="ydcx_cxyy_jtcxfs_text_13" 
										defVal="格式提示" fileName="ydcx"/></a>
						    </div>
								
							    <div id="eq_sendDiv" class="ydcx_eq_sendDiv">
									<span  class="righttitle ydcx_span1"><emp:message key="ydcx_cxyy_jtcxfs_text_14" 
										defVal="彩信内容：" fileName="ydcx"/></span>
									<%--上传的彩信TMS文件名称 --%>
									<input type="hidden" id="mmsFileName" name="mmsFileName" value=""/>
									<%--选择的彩信TMS文件信息 --%>
									<%--<input type="hidden" id="mmsTemplateFile" name="mmsTemplateFile" value=""/>
									--%>
									<input type="hidden" id="mmstemplateurl" name="mmstemplateurl" value=""/>
									<%-- 彩信TMS文件 的界面    1是选择彩信模板  2是选择上传TMS彩信文件--%>
									<%--<input type="hidden" id="mmsFileType" name="mmsFileType" value="1"/>
									--%>
									<div class="divMmsTable" >
										<%--<input type="radio" id="chioceMMS" name="mmsTms" checked="checked" onclick="javascript:changeTemplate('1')" value="1"/><label for="chioceMMS"> 彩信模板</label>
										<input type="radio" id="upMMS" name="mmsTms" onclick="javascript:changeTemplate('2')" value="2"/> <label for="upMMS">上传彩信文件</label>
										--%>
										<input type="button" class="btnClass4 ydwx_btnClass4" value="<emp:message key="ydcx_cxyy_jtcxfs_text_15" 
										defVal="选择模板" fileName="ydcx"/>" onclick="chooseMmsTpl()"/>
										&nbsp;
										<a class="ydcx_ljxj" onclick="openTemDiv('<%=ViewParams.MMSTEMPCODE%>')"><emp:message key="ydcx_cxyy_jtcxfs_text_16" defVal="[立即新建]" fileName="ydcx"/></a>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

										<%if(StaticValue.ZH_HK.equals(langName)){%>
											<input type="file" id='smmFile' name='smmFile' class="ydcx_smmFile_HK" onchange="upMmsFile()" />
											<div  class="btnClass4 ydwx_btnClass4 ydcx_fileBtn_HK" id="fileBtn" >
										<%}else{%>
											<input type="file" id='smmFile' name='smmFile' class="ydcx_smmFile"  onchange="upMmsFile()" />
											<div  class="btnClass4 ydwx_btnClass4 ydcx_fileBtn" id="fileBtn">
										<%}%>
										<emp:message key="ydcx_cxyy_jtcxfs_text_17" 
										defVal="上传彩信文件" fileName="ydcx"/></div><font class="ydcx_uploadtmsfile" ><emp:message key="ydcx_cxyy_jtcxfs_text_18" 
										defVal="只支持.tms文件格式" fileName="ydcx"/></font>
										
										
										
									</div>
									<div id="templateDiv" class="ydcx_templateDiv">
										<%--选择的模板ID--%>
										<input type="hidden" id="mmstemplateid" name="mmstemplateid" value=""/>
										<div id="templatepre" class='div_bg ydcx_templatepre'>
										</div>	
									
										<%--<div id="chioceMmsDiv" style="height: 75px;float:left;width:242px;display: none;"> 
										</div>
										--%>
									</div>
									<%--<div id="mmsFileDiv" style="display: none;padding-left:240px !important;padding-left:260px;height: 70px;">
										  <a id="upfile" name="upfile" class="afileclass1" >上传附件</a>
										  <label style="color:#ACACAC;">&nbsp;&nbsp;&nbsp;只支持.tms文件格式</label>
										  <div id="filesdiv" style="position:absolute; height: 30px;margin-bottom: 0px;top: 295px !important;top: 288px;left: 280px !important;left: 300px;">
					                          <input id='mmsFile' name='mmsFile'  type=file onchange="javascript:upMmsFile();" class="numfileclass" value=""/>	  	 
										  </div>
										<div id="upTip" style="display: none;width: 290px;"  class='div_bg'>
										</div>
					                </div>
								--%></div>
							
							    <div id="eq_sendDiv" class="eq_sendDiv_c">
						  			<span><emp:message key="ydcx_cxyy_jtcxfs_text_19" 
										defVal="发送时间：" fileName="ydcx"/></span>
									  <div class="ydcx_sendtm">
									    <input type="radio" name="sendType" value="0" id="sendType" 
											checked="checked" onclick="javascript:$('#time2').hide()" />&nbsp;<emp:message key="ydcx_cxyy_jtcxfs_text_20" 
										defVal="立即发送" fileName="ydcx"/>
										<input type="radio" name="sendType" value="1" class="ydcx_sendType1" id="sendType1"
											onclick="javascript:$('#time2').show()"/>&nbsp;<emp:message key="ydcx_cxyy_jtcxfs_text_21" 
										defVal="定时发送" fileName="ydcx"/>
										<label id="time2" class="ydcx_time2">
											<input type="text" class="Wdate div_bd" readonly="readonly"
												onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false})"
												id="sendtime" name="sendtime" value="">
										</label>
									  </div>
								</div>
								<div id="eq_sendDiv2" class="ydcx_eq_sendDiv2" >
									<span id="u_o_c_explain" class="div_bg ydcx_u_o_c_explain"><b><emp:message key="ydcx_cxyy_jtcxfs_text_22" 
										defVal="高级设置" fileName="ydcx"/></b>&nbsp;&nbsp;&nbsp;&nbsp;<a id="foldIcon" class="unfold" style="text-decoration: none">&nbsp;&nbsp;&nbsp;&nbsp;</a></span>
									<div id="moreSelect"  class="div_bg ydcx_moreSelect">
									<%--
									<div style="width:580px" align="right">
										<a id="setdefualt" href="javascript:setDefault()"  class="alink" >选项存为默认</a>
									</div>
									--%>
									<table class="ydcx_moreselect_tabl">
										<%--<tr><td style="width:108px;"><font style="padding-left:35px"><%=StaticValue.SMSACCOUNT%>：</font></td>
										--%><tr><td style="width:115px;"><font style="padding-left:35px"><emp:message key="ydcx_cxyy_jtcxfs_text_73" defVal="SP账号：" fileName="ydcx"/></font></td>
											<td>
												<div id="selfSelect">
												<select id="mmsUser" name="mmsUser" class="ydcx_mmsUser">
												<%
												if(mmsAccbinds != null && mmsAccbinds.size() > 0){
													String spUserId = lfDfadvanced != null?lfDfadvanced.getSpuserid():"";
													LfMmsAccbind acc = null;
													for(int i=0;i<mmsAccbinds.size();i++)
													{
														acc = mmsAccbinds.get(i);
													%>
														<option value="<%=acc.getMmsUser()%>" 
														<%=spUserId != null && !"".equals(spUserId) && spUserId.equals(acc.getMmsUser())?"selected":"" %>>
														<%=acc.getMmsUser()%></option>
													<% 
														}	
													}
												 %>
												</select>
												</div>
											</td>
											<td><a id="setdefualt" href="javascript:setDefault()"  class="alink" ><emp:message key="ydcx_cxyy_jtcxfs_text_23" 
										defVal="选项存为默认" fileName="ydcx"/></a></td>
										</tr>
										
									</table>
									</div>
								</div>
							
							
						      <div class="clear2"></div>
							<div class="b_F_btn ydcx_b_F_btn">
								<input type="hidden" value="1" id="subState" name="subState" />
								<input id="subSend" class="btnClass5 mr23 ydwx_borderRadius" type="button" value="<emp:message key="ydcx_cxyy_common_btn_22" 
										defVal="提交" fileName="ydcx"/>"/>
								<input id="qingkong" onclick="javascript:reloadPage();" class="btnClass6 ydwx_borderRadius" type="button" value="<emp:message key="ydcx_cxyy_common_btn_9" 
										defVal="重置" fileName="ydcx"/>" />
							</div>
							
						</form>
					</div>	
			</div>
			
			
				
			
				<%-- 选择人员的弹出框 --%>
				<div id="infoDiv" title="<emp:message key="ydcx_cxyy_jtcxfs_text_55" 
										defVal="选择发送对象" fileName="ydcx"/>"  style="">
						<iframe id="flowFrame" name="flowFrame" class="ydcx_flowFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
						<center>
							<table>
								<tr>
									<td style="width:530px;" align="center">
										<input type="button"  value="<emp:message key="ydcx_cxyy_common_btn_7" 
											defVal="确定" fileName="ydcx"/>" class="btnClass5 mr23 ydwx_borderRadius" onclick="javascript:doOk()" />
										<input type="button"  value="<emp:message key="ydcx_cxyy_common_btn_20" 
											defVal="清空" fileName="ydcx"/>" class="btnClass6 ydwx_borderRadius" onclick="javascript:doNo()" />
										<br/>
									</td>
								</tr>
							</table>
						</center>
				</div>
				<%--彩信预览DIV --%>
					<div id="tempView" class="ydcx_tempView" title="<emp:message key="ydcx_cxyy_jtcxfs_text_24" 
										defVal="彩信内容" fileName="ydcx"/>" style="display:none">
						<input type="hidden" id="tmmsId" value=""/>
						<div class="ydcx_tempView_sub">
							<div id="mobile" class="ydcx_mobile">
							<center>
							<div id="pers" class='mmsPersDiv'>
							<div id="showtime" class="mmsShowtimeDiv"></div>
							<div id='chart' class='mmsChartDiv'>
							</div>
							</div>
							</center>
							<div id="screen" class="mmsScreenDiv">
							</div>
							<center>
								<table>
									<tr>
									  <td>
									     <label id="pointer" style="vertical-align: bottom"></label>
									     <label id="nextpage" style="vertical-align: bottom"></label>
									  </td>
									</tr>
									<tr align="center">
										<td>
										   <label id="currentpage" style="vertical-align: bottom"></label>
										</td>
									</tr>
								</table>				
							</center>
							</div>
						</div>
						<div id="inputParamCnt1" class="ydcx_inputParamCnt1">
						</div>			
					</div>	
				
				
				<%--预览 --%>
				<div id="detail_Info" class="ydcx_detail_Info">
					<table>
							<tr>
								<td >
									<div id="tempView1" title="<emp:message key="ydcx_cxyy_jtcxfs_text_24" 
										defVal="彩信内容" fileName="ydcx"/>">
									<div class="ydcx_tempView1_sub">
										<div id="mobile1" class="ydcx_mobile1">
										<center>
										<div id="pers1" class='mmsPersDiv'>
										<div id="showtime1" class="mmsShowtimeDiv"></div>
										<div id='chart1' class='mmsChartDiv'>
										</div>
										</div>
										</center>
										<div id="screen1" class="mmsScreenDiv">
										</div>
										<center>
											<table>
												<tr>
												  <td>
												     <label id="pointer1" style="vertical-align: bottom"></label>
												     <label id="nextpage1" style="vertical-align: bottom"></label>
												  </td>
												</tr>
												<tr align="center">
													<td>
													   <label id="currentpage1" style="vertical-align: bottom"></label>
													</td>
												</tr>
											</table>				
										</center>
										</div>
									</div>
								</div>	
								</td>
								<td width="300px;">
										<div>
												<table>
													<tr>
														<td class="ydcx_sendcount_td" align="left">
															<b class="ydcx_b"><emp:message key="ydcx_cxyy_jtcxfs_text_25" 
										defVal="发送条数：" fileName="ydcx"/></b>	<label id="yct"></label>
														</td>
														<td class="ydcx_orgbalance" align="left" id="lesscount">
															&nbsp;&nbsp;<b class="ydcx_b"><emp:message key="ydcx_cxyy_jtcxfs_text_26" 
										defVal="机构余额：" fileName="ydcx"/></b><label id="ct"></label>
														</td>
													</tr>
													<tr id="notsend">
														<td class="ydcx_nosendReason" align="left" colspan="2" id="nosendReason">
														</td>
													</tr>
												</table>
										</div>
										<div>
												<table>
													<tr>
														<td class="ydcx_effs_td" >
															<emp:message key="ydcx_cxyy_jtcxfs_text_27" 
										defVal="有效号码数：" fileName="ydcx"/>&nbsp;<span id="effs"></span>
														</td>
													</tr>
												</table>
										</div>
										<div>
												<table>
													<tr>
														<td calss="ydcx_counts_td">
															<emp:message key="ydcx_cxyy_jtcxfs_text_28" 
										defVal="提交号码数：" fileName="ydcx"/>&nbsp;<span id="counts"></span>
														</td>
													</tr>
												</table>
										</div>
									
										<div class="ydcx_explain1_parent">
												<span id="u_o_c_explain1" class="div_bg ydcx_u_o_c_explain1">
												<emp:message key="ydcx_cxyy_jtcxfs_text_29" 
										defVal="无效号码" fileName="ydcx"/>&nbsp;&nbsp;(<span id="errerCount" style="color: red;"></span>)
												&nbsp;&nbsp;&nbsp;&nbsp;<a id="foldIcon1" class="unfold" style="text-decoration: none">&nbsp;&nbsp;&nbsp;&nbsp;</a>
												</span>
													<a href="javascript:uploadbadFiles()" class="ydcx_downlinkinfo"  id="downlinkinfo"><emp:message key="ydcx_cxyy_jtcxfs_text_30" 
										defVal="详情下载" fileName="ydcx"/></a>
																 <input type="hidden" id="badurl" name="badurl"  value=""></input>
												<div  id="errerDiv"  class="div_bg ydcx_errerDiv">
													<table id="moreSelect1">
														<tr>
															<td>
																<font class="ydcx_blacks_font"><emp:message key="ydcx_cxyy_jtcxfs_text_31" 
										defVal="黑名单号码：" fileName="ydcx"/></font>
																 <span id="blacks"></span>
																 <%--
																 <a href="javascript:uploadbadFiles()" style="padding-right: 2px;" id="downlinkinfo">详情下载</a>
																 <input type="hidden" id="badurl" name="badurl"  value=""></input> --%>
															</td>
														</tr>
													     <tr>
															<td>
																<font class="ydcx_sames_font"><emp:message key="ydcx_cxyy_jtcxfs_text_32" 
										defVal="重复号码：" fileName="ydcx"/></font>
																<span id="sames"></span>
															</td>
														</tr>
														<tr>
															<td>
																<font class="ydcx_legers_font"><emp:message key="ydcx_cxyy_jtcxfs_text_33" 
										defVal="格式非法：" fileName="ydcx"/></font>
																 <span id="legers"></span>
															</td>
														</tr>
													</table>
												</div>
										</div>
										<div class="ydcx_sendquit_dv">
											<table>
												<tr>
													<td style="padding-top: 15px;" align="center">
															<input id="subSend1"  type="button" class="btnClass5 mr23 left" onclick="javascript:previewSub();" value="<emp:message key="ydcx_cxyy_jtcxfs_text_56" 
										defVal="发送" fileName="ydcx"/>"/>
															
															<input id="qingkong1"  type="button" class="btnClass6 left"  onclick="javascript:previewReset();" value="<emp:message key="ydcx_cxyy_common_btn_16" 
										defVal="取消" fileName="ydcx"/>" />
													</td>
												</tr>
											</table>
										</div>
								</td>
							</tr>
					</table>
				
				
				
				
			
				</div>

					<%--模板--%>
				<div id="infomodel" class="infomodelclass" style="display: none;">
					<ul>
						<li></li>
						<li>
							13800138000
						</li>
						<li>
							13200132000
						</li>
						<li>
							13600000000
						</li>
						<li>
							......
						</li>
						<li></li>
						<li>
							<emp:message key="ydcx_cxyy_jtcxfs_text_34" 
										defVal="说明：" fileName="ydcx"/>
						</li>

						<li style="margin-left: 60px; margin-top: 5px;">
							<emp:message key="ydcx_cxyy_jtcxfs_text_35" 
										defVal="1.每个号码单独为一行，11位数字号码后带其他内容，则上传时将被过滤" fileName="ydcx"/>
						</li>
						<li style="margin-left: 60px; margin-top: 5px;">
							<emp:message key="ydcx_cxyy_jtcxfs_text_36" 
										defVal="2.手机号码格式不正确，在上传时将由EMP平台过滤" fileName="ydcx"/>
						</li>
						<li style="margin-left: 60px; margin-top: 5px;">
							<emp:message key="ydcx_cxyy_jtcxfs_text_37" 
										defVal="3.号码包含于黑名单内，在上传时将由EMP平台过滤" fileName="ydcx"/>
						</li>
						<li></li>
						<li>
						</li>
					</ul>
				</div>
				
					<%--格式提示--%>
					<%if(StaticValue.ZH_HK.equals(langName)){%>
				        <div id="infomodel1" class="infomodelclass ydcx_infomodelclass_HK" >
					<%}else{%>
						<div id="infomodel1" class="infomodelclass ydcx_infomodelclass">
					<%}%>
                        <center>
                        <table class="ydcx_tab2">
                          <tr>
                            <td colspan="2" class="ydcx_td1" align="left"><emp:message key="ydcx_cxyy_jtcxfs_text_38" 
										defVal="文件格式如图:" fileName="ydcx"/></td>
                          </tr>
                          <tr>
                           <td valign="top" align="left" ><emp:message key="ydcx_cxyy_jtcxfs_text_39" 
										defVal="txt格式：" fileName="ydcx"/></td>
                           <td valign="top">
                           <%--<div id="txtstyle" style="width: 155px;height:69px;background : url('<%=skin %>/images/mmsphone.png') no-repeat; "></div>
                           --%>
                           <%if(StaticValue.ZH_HK.equals(langName)){%>
                           		<div id="txtstyle" style="width: 155px;height:69px;background : url('<%=commonPath %>/common/img/mmsphone-zh_HK.png') no-repeat; "></div>
                           <%}else if(StaticValue.ZH_TW.equals(langName)){%>
                           		<div id="txtstyle" style="width: 155px;height:69px;background : url('<%=commonPath %>/common/img/mmsphone-zh_TW.png') no-repeat; "></div>
                           <%}else{%>
                           		<div id="txtstyle" style="width: 155px;height:69px;background : url('<%=skin %>/images/mmsphone.png') no-repeat; "></div>
                           <%}%>
                           </td>
                          </tr>
                          <tr height="8px;">
                          <td colspan="2"></td>
                          </tr>
                           <tr>
                           <td valign="top" align="left" width="55px;"><emp:message key="ydcx_cxyy_jtcxfs_text_40" 
										defVal="xls,xlsx,et格式：" fileName="ydcx"/></td>
                           <td valign="top">
                           <%--<div id="xlsstyle" style="width: 155px;height: 85px;background: url('<%=skin %>/images/mmsphone.png') no-repeat 0px -80px;"></div>
                           --%>
                           <%if(StaticValue.ZH_HK.equals(langName)){%>
                           		<div id="xlsstyle" class="ydcx_xlsstyle_HK"></div>
                           <%}else if(StaticValue.ZH_TW.equals(langName)){%>
                           		<div id="xlsstyle" class="ydcx_xlsstyle_TW"></div>
                           <%}else{%>
                           		<div id="xlsstyle" class="ydcx_xlsstyle"></div>
                           <%}%>
                           </td>
                          </tr>
                          <tr>
                          <td colspan="2" align="left">
                           <span style="font-weight: bold;font-size: 14px;"><emp:message key="ydcx_cxyy_jtcxfs_text_41" 
										defVal="注意：" fileName="ydcx"/></span><br/>
                         <emp:message key="ydcx_cxyy_jtcxfs_text_42" 
										defVal="1.每个号码为一行，11位号码后带其他内容，上传时将被过滤；" fileName="ydcx"/><br/>
                          <emp:message key="ydcx_cxyy_jtcxfs_text_44" 
										defVal="2.号码格式不正确，上传时由EMP过滤；" fileName="ydcx"/> <br/>
                          <emp:message key="ydcx_cxyy_jtcxfs_text_45" 
										defVal="3.号码包含于黑名单内，上传时由EMP过滤。" fileName="ydcx"/> 
                          </td>
                          </tr>
                        </table>
                       </center>
                    </div>
				
				
				
				
				
				
				
					<%--等待旋转--%>
				<div id="probar" style="display: none">
					<p class="ydcx_probar_p">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="ydcx_cxyy_jtcxfs_text_46" 
										defVal="处理中,请稍等....." fileName="ydcx"/>
					</p>
					<div id="shows">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<img class="ydcx_shows_img" src="<%=iPath%>/img/loader.gif" />
					</div>
				</div>
			
				<iframe name="hidden_iframe" id="hidden_iframe"
					style="display: none"></iframe>
				
				
			<div id="tmplDiv" title="<emp:message key="ydcx_cxyy_jtcxfs_text_47" 
										defVal="彩信模板选择" fileName="ydcx"/>">
				<iframe id="tempFrame" name="tempFrame" class="ydcx_tempFrame" marginwidth="0" scrolling="no" frameborder="no" src ="<%=commonPath%>/common/blank.jsp " ></iframe>
			</div>
			
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</div>
			<div id="message" title="<emp:message key="ydcx_cxyy_jtcxfs_text_48" 
										defVal="提示" fileName="ydcx"/>" class="ydcx_message">
				<div class="ydcx_dv1"></div>
		 		<center>
				<label ><emp:message key="ydcx_cxyy_jtcxfs_text_49" 
										defVal="发送至网关成功！" fileName="ydcx"/></label>
				<a href="javascript:sendRecord('1100-1800',<%=guid%>)"  class="alink" ><emp:message key="ydcx_cxyy_jtcxfs_text_50" 
										defVal="查看发送记录" fileName="ydcx"/></a>

				</center>
				<br>
				<div class="ydcx_dv2" ></div>
				 <center>
				    <input id="close" onclick="javascript:closemessage();" class="btnClass6" type="button" value="<emp:message key="ydcx_cxyy_jtcxfs_text_51" 
										defVal="关闭" fileName="ydcx"/>" />
				</center> 
			</div>
		<div class="clear2"></div>
		<%--批量导入--%>
		<style>
			.imporInner{width: 485px;height: 240px;margin:20px auto 10px;}
			.bultMark{width: 485px;margin:0 auto 20px;position: relative;overflow: hidden;}
			.imporInner textarea{border: 0;width:477px;height:220px;}
			textarea:focus{outline: none;resize:none;}
			.bultBtn{text-align: center;}
			.numSplit{text-align: left;}
			#bultNum{position: absolute;left: 0;top: 0;}
		</style>
		<div id="bulkImport_box" title="<emp:message key="ydcx_cxyy_jtcxfs_text_52" 
										defVal="批量号码输入" fileName="ydcx"/>" style="display:none;">
			   <div class="imporInner div_bd">
			   		<textarea name="importArea" id="importArea" cols="30" rows="5" onkeyup="formatTelNum('#bNum')" onblur="formatTelNum('#bNum')"></textarea>
			   		<textarea name="importAreaTemp" id="importAreaTemp" style="display:none"></textarea>
			   </div>
			   <div class="bultMark">
			   		<span id="bultNum"><emp:message key="ydcx_cxyy_jtcxfs_text_76" 
										defVal="当前共" fileName="ydcx"/><font color='blue'><b id="bNum"></b></font><emp:message key="ydcx_cxyy_jtcxfs_text_53" 
										defVal="/20000个号码" fileName="ydcx"/></span>
			   		<div class="ydcx_bultMark_dv1"></div>
			   		<div class="numSplit">
			   			<emp:message key="ydcx_cxyy_jtcxfs_text_54" 
										defVal="多个手机号码以逗号、空格、顿号、回车换行其中一种格式隔开" fileName="ydcx"/>
			   		</div>
			   </div>
			   <div class="bultBtn">
			   	<input onclick="previewTel()" id="telSub" class="btnClass5 mr23 ydwx_borderRadius" type="button" value="<emp:message key="ydcx_cxyy_common_btn_7" 
										defVal="确认" fileName="ydcx"/>">
			   	<input onclick="bultCancel()" id="telRet" class="btnClass6 ydwx_borderRadius" type="button" value="<emp:message key="ydcx_cxyy_common_btn_10" 
										defVal="返回" fileName="ydcx"/>">
			   	<br/>
			   </div>
		</div>
		<script>
			var base_path = "<%=path%>";
			var serverName = "<%=serverName%>";
		</script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>"  ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/staMms.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/mmsPreview1.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
 		<script type="text/javascript">
			$(function(){
			  $("#mmsUser").isSearchSelect({'width':'180','isInput':false,'zindex':0});
		   });
		</script>
	</body>
</html>
