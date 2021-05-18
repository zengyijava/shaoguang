<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.entity.template.LfTemplate"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath=iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath=inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/"; 
			//获取业务类型
	String businessType = "";
	if (request.getAttribute("businessType") != null) {
		businessType = (String) request.getAttribute("businessType");
	}
	//短信模板读取
	@SuppressWarnings("unchecked")
	List<LfTemplate> tempsList = (List<LfTemplate>) request.getAttribute("tempsList");
	@SuppressWarnings("unchecked")
	List<Userdata> spUserList = (List<Userdata>) request.getAttribute("spUserList");
	@SuppressWarnings("unchecked")
	List<LfBusManager> busList = (List<LfBusManager>) request.getAttribute("busList");
	LfSysuser lfSysuser = (LfSysuser)session.getAttribute("loginSysuser");
	String taskId = (String)request.getAttribute("taskId");
	LfTemplate lf = null;
	//显示提示消息
	String repMsg = "";
	if (session.getAttribute("ycw_result") != null) {
		repMsg = (String) session.getAttribute("ycw_result");
		session.removeAttribute("ycw_result");
	}
	//获得信息回显
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session
			.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("electronicPayroll");
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
	</head>
	<body onload="showMsgElec();">
		<%-- 预览发送时用于标识是手工录入还是导入数据 --%>
		<input type="hidden" id="repMsg" name="repMsg" value="<%=repMsg%>" />
		<input type="hidden" id="imgPath" name="imgPath" value="<%=skin %>" />
		<input type="hidden" id="ipathUrl" value="<%=iPath %>"/>
		
		<%-- 预览层用于标识是那个业务 --%>
		<input type="hidden" id="buscode" name="buscode" value="MF0001"/>
		<div id="container" class="container">
			<%-- header开始 #faf0be--%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>

			<div id="rContent" class="rContent">
			<div id="batchFileSend" style="padding-left:0 !important;padding-left:16px; padding-top: 0 !important;padding-top: 16px;">
				<form id="ydcwForm" name="ydcwForm" method="post"
				action="<%=path%>/ycw_electronicPayroll.htm?method=manualPreview"
					target="hidden_iframe"	enctype="multipart/form-data"				>
										  <iframe name="hidden_iframe" id="hidden_iframe"
								style="display: none"></iframe>
										<div id="loginUser" class="hidden"></div>
										<%-- 定时时间 --%>
										<input type="hidden" id="path" name="path" value="<%=path %>"/>
										<input type="hidden" id="deterTime" name="deterTime"/>
										<input type="hidden" id="isCheckTime" name="isCheckTime" />
										<input type="hidden" id="spAccount" name="spAccount" />
										<input type="hidden" id="busCode1" name="busCode1" />
										<input type="hidden" id="templateIds" name="templateIds" value=""/>
										<input type="hidden" id="isReply" name="isReply"/>
										<input type="hidden" value ="" id="sendFlag" name="sendFlag"/><%--  发送帐号主通道号+循环尾号的位数超过２０位个数 --%>
										<input type="hidden" value ="" id="circleSubNo" name="circleSubNo"/> <%-- 循环尾号 --%>
						                <input type="hidden" value ="" id="subNo" name="subNo"/> <%-- 尾号 --%>
						                <input type="hidden" value ="<%=taskId%>" id="taskId" name="taskId"/>
						                <%-- 参数字符串 --%>
										<input type="hidden" id="tempArr" name="tempArr"  value=""/>
										<%-- 表格的列 --%>
										<input type="hidden" id="textCell" name="textCell"  value=""/>
										<%-- 表格的行 --%>
										<input type="hidden" id="textRow" name="textRow" value=""/>
										<%-- 参数值字符串,临时参数数据保存于此 --%>
										<input type="hidden" id="paraValue" name="paraValue"  value=""/>
										<input type="hidden" id="subType" name="subType" value="0"/>
										<input type="hidden" id="error" name="error" value="" />
										<div id="eq_sendDiv">
											<span><b><emp:message key="ydcw_title_1" defVal="内容模板：" fileName="ydcw"/></b></span>
											<table style="width:498px" class="div_bd">
											<tr style="height:25px">
											<td style="padding-left:3px;" class="div_bd">
													<select name="select" id="select" style="width:490px;border: 0px;azimuth: center;"
														onChange="getTempMsg($(this));">
														<option value="" class="optionFirst"><emp:message key="ydcw_title_2" defVal="请选择模板" fileName="ydcw"/></option>
														<%
															for (int i = 0; i < tempsList.size(); i++) {
																lf = new LfTemplate();
																lf = (LfTemplate) tempsList.get(i);
														%>
															<option value="<%=lf.getTmid()%>" ><%=lf.getTmName()%></option>
														<%}%>
													</select>
													</td>
													</tr>
													<tr style="min-height:90px">
													<td>
													    <div id="text_show" style="width:478px;min-height:90px;padding:4px 6px;"></div>
														<textarea style="width:498px;height:90px;border:0px;display:none;" class="msg2" name="textarea" rows="5" id="textarea" onfocus="this.blur();"></textarea>
													</td>
												</tr>
												</table>
												</div>
												<div class="clear2"></div>
												<div class="mt10 eq_sendDiv">
												<span><b><emp:message key="ydcw_title_3" defVal="数据导入：" fileName="ydcw"/></b></span>
												<div id="mmsFileDiv" style="margin-top:-5px; width:495px; float:left;">
												<table>
							                         <tr>  <td width="120px;" style="padding-top: 14px !important;">
													   <div  style="position:relative;width:80px;height:15px;">
													  		<a id="upfile" name="upfile" style="" ><emp:message key="ydcw_title_4" defVal="上传附件" fileName="ydcw"/></a>
								                          <div id="filesdiv" style="margin-bottom: 0px;width: 50px;overflow:hidden;position:absolute;top:0px; height: 15px;left:0;">
									                             <input id='numFile' name='numFile'  type='file' onchange="javascript:upFile();" class="numfileclass" value="" style="width:50px;height:15px;" />						  
														  </div>
													  </div>
													  </td>
							                         <td width="120px;"  style="padding-top: 14px !important;"><a onclick="javascript:handInput()" id="input"
							                         style="cursor: hand;"><emp:message key="ydcw_title_5" defVal="手工录入" fileName="ydcw"/></a>
													  </td>
							                         <td align="right" style="padding-top: 16px !important;">
							                         <a  style="position: relative;margin-left: 268px;margin-top: 10px;"
													 id="downlinks"
													><emp:message key="ydcw_title_6" defVal="格式提示" fileName="ydcw"/></a></td>
							                         </tr>
							                         </table>
												 
													
											</div>
											</div>
											<div id="eq_sendDiv">
											<div id="upTipDiv" style="display: none;margin-left: 70px;height:35px;width:245px;" class="div_bg"></div>
													<div id="handInput" style="display:none;height:35px;width:260px;margin-top: 10px;margin-left: 70px;" class="div_bg">
												<img id="dataDiv" src="<%=skin %>/images/handInput.png" style="margin-top: 10px;margin-left: 7px;"/>
												<emp:message key="ydcw_title_5" defVal="手工录入" fileName="ydcw"/>(<label id="paraNumber">0</label><emp:message key="ydcw_title_7" defVal="条" fileName="ydcw"/>)
												<a style="margin-left: 50px;cursor: hand;" onclick="javascript:modify($(this))" ><emp:message key="ydcw_title_8" defVal="编辑" fileName="ydcw"/></a>
												&nbsp;<a style="margin-left: 20px;cursor: hand;" onclick="deleteInput();" ><emp:message key="ydcw_title_9" defVal="删除" fileName="ydcw"/></a>
												</div>
											</div>
											<div id="eq_sendDiv" class="eq_sendDiv_c">
						  <span><emp:message key="ydcw_title_10" defVal="发送时间：" fileName="ydcw"/></span>
						  <div style="padding:3px 5px 0; width: 493px;line-height: 26px;">
						    <input type="radio" name="timerStatus" value="0" id="sendNow" 
								checked="checked" onclick="javascript:$('#time2').hide()" />&nbsp;<emp:message key="ydcw_title_11" defVal="立即发送" fileName="ydcw"/>
							<input type="radio" name="timerStatus" value="1" style="margin-left: 10px;"
								onclick="javascript:$('#time2').show()"/>&nbsp;<emp:message key="ydcw_title_12" defVal="定时发送" fileName="ydcw"/>
							<label id="time2" style="display: none;">
								<input type="text" class="Wdate div_bd" readonly="readonly"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false})"
									id="timerTime" name="timerTime" value="">
							</label>
						  </div>
						</div>
											<div id="eq_sendDiv2" >
												<span id="u_o_c_explain" class="div_bg"><b><emp:message key="ydcw_title_13" defVal="高级设置" fileName="ydcw"/></b>&nbsp;&nbsp;&nbsp;&nbsp;<a id="foldIcon" class="unfold" style="text-decoration: none">&nbsp;&nbsp;&nbsp;&nbsp;</a></span>
												<div id="moreSelect" class="div_bg">
													<table style="width:100%;height:100%;border:0px">
														<tr style="height:20%;">
															<td style="width:108px;"><font style="padding-left:35px"><emp:message key="ydcw_title_14" defVal="业务类型：" fileName="ydcw"/></font></td>
															<td>
																<select id="busCode" name="busCode" style="width:380px;">
										               				<%
																	if (busList != null && busList.size() > 0)
																	{
																		for(LfBusManager busManager : busList)
																		{
																	%>
																	<option value="<%=busManager.getBusCode() %>"  >
																		<%=busManager.getBusName().replace("<","&lt;").replace(">","&gt;") %>
																	</option>
																	<%}
																	}													
																	%>
					                							</select>
						                					</td>
														</tr>
														<tr  style="height:20%">
															<td><font style="padding-left:35px"><emp:message key="dxzs_xtnrqf_title_99" defVal="SP账号" fileName="dxzs"/>:</font></td>
															<td>
																	<select name="spUser" id="spUser" style="width:380px" onchange="getGateNumber()">
																		<%
																			if(spUserList != null && spUserList.size()>0){
																				for(Userdata userdata:spUserList){%>
																				<option value="<%=userdata.getUserId()%>" >
																				<%=userdata.getUserId()+"("+userdata.getStaffName()+")"%></option>
																			<%}}
																			%>
																	</select>
															</td>
														</tr>
														<tr  style="height:20%">
															<td><font style="padding-left:35px"><emp:message key="ydcw_title_15" defVal="回复设置：" fileName="ydcw"/></font></td>
															<td>
																	<input type="radio"  name="reply" value="0" onclick="getGateNumber()" 
														<%if(lfSysuser.getIsExistSubNo() != 1){ %> checked="checked" <%} %>/><emp:message key="ydcw_title_16" defVal="不用回复" fileName="ydcw"/>
														<input type="radio"  name="reply" value="1" onclick="getGateNumber()" style="margin-left: 10px;"/><emp:message key="ydcw_title_17" defVal="本次任务" fileName="ydcw"/>
														<input type="radio"  name="reply" value="2" onclick="getGateNumber()" 
														<%if(lfSysuser.getIsExistSubNo() != 1){ %> disabled="disabled" <%}else{ %>
														checked="checked"<%} %>
														style="margin-left: 10px;"/><label <%if(lfSysuser.getIsExistSubNo() != 1){ %> style="color:gray;" <%}else{%>
														style="color:#000000;"
														 <%}%>><emp:message key="ydcw_title_18" defVal="我的尾号" fileName="ydcw"/></label>
														 <label style="margin-left: 20px;color:#000000;display: none" id="curSubNo"><emp:message key="ydcw_title_19" defVal="当前尾号：" fileName="ydcw"/></label>
														 <label id="subno" ></label>
																	</td>
												</tr>
												
												</table>
												</div>
									</div>
							<div class="b_F_btn" style="padding-right:40px">
								<input type="hidden" value="1" id="subState" name="subState" />
								<input id="subButt" name="subButt" class="btnClass5 mr23" type="button" value="<emp:message key='ydcw_button_1' defVal='提交' fileName='ydcw'/>"/>
								<input id="qingkong" onclick="javascript:location.href=location.href;" class="btnClass6"
									type="button" value="<emp:message key='ydcw_button_3' defVal='重置' fileName='ydcw'/>" />
							</div>
									</form>
						</div>
						<div class="modBottom">
							<span class="modABL">&nbsp;</span><span class="modABR">&nbsp;</span>
						</div>
			</div>
			<%-- 内容结束 --%>
			<div class="clear">

			</div>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</div>
		<%-- 手工录入模板 overflow-y:auto;overflow-x:auto;--%>
		<div id="modify" title="<emp:message key='ydcw_title_5' defVal='手工录入' fileName='ydcw'/>" style="display: none;margin-top:10px;">
				<div style="overflow: auto;height: 400px;width:425px;margin:0 auto;">
				<table id="tabletId"  width="400px;"></table>
				</div>
				<div align="center" >
				<input style="margin-top: 10px;letter-spacing: 0px;" type="button" class="btnClass5 mr23" value="<emp:message key='ydcw_button_5' defVal='保存' fileName='ydcw'/>" onclick="javascript:save();">
				<input  style="margin-top: 10px;letter-spacing: 0px;" type="button" class="btnClass6" value="<emp:message key='ydcw_button_3' defVal='重置' fileName='ydcw'/>" onclick="javascript:cleardb();">
				 </div>
		</div>
		<div id="infomodel" class="infomodelclass" style="display: none;position: absolute;z-index: 5000;border: 1px solid #dae1cf;background-color: #f7fff0;width: 250px;height: 277px;padding-bottom: 8px;">
		<center>
              <table style="line-height: 30px;">
                <tr>
                  <td colspan="2" style="font-weight: bold;font-size: 14px;" align="left"><emp:message key="ydcw_title_20" defVal="文件格式如图:" fileName="ydcw"/></td>
                </tr>
                <tr>
                 <td valign="top" align="left"><emp:message key="ydcw_title_21" defVal="txt格式：" fileName="ydcw"/></td>
                 <td valign="top"><div id="txtstyle" style="width: 155px;height: 69px;background : url('<%=skin %>/images/dtsend.png') no-repeat; "></div></td>
                </tr>
                <tr height="12px;">
                <td colspan="2"></td>
                </tr>
                <tr>
                 <td valign="top" align="left" width="55px;"><emp:message key="ydcw_title_22" defVal="xls格式：" fileName="ydcw"/></td>
                 <td valign="top"><div id="xlsstyle" style="width: 155px;height: 85px;background: url('<%=skin %>/images/dtsend.png') no-repeat 0px -80px;"></div></td>
                </tr>
                <tr>
                <td colspan="2" align="left">
                 <span style="font-weight: bold;font-size: 14px;"><emp:message key="ydcw_title_23" defVal="注意：" fileName="ydcw"/></span><br/>
                 <emp:message key="ydcw_title_24" defVal="1.txt格式分隔符号是英文半角“,”；" fileName="ydcw"/><br/>
                 <emp:message key="ydcw_title_25" defVal="2.有效号码个数不能超过50万。" fileName="ydcw"/>
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>
                 
                </td>
                </tr>
              </table>
             </center>
		</div>
		<%--等待旋转--%>
				<div id="probar" style="display: none">
					<p style="padding-top: 8px;">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="ydcw_title_26" defVal="处理中,请稍等....." fileName="ydcw"/>
					</p>
					<div id="shows">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<img style="padding-left: 25px;"
							src="<%=commonPath%>/common/img/loader.gif" />
					</div>
				</div>
		<%-- 预览 --%>
		<div  id="preview" title="<emp:message key='ydcw_title_27' defVal='预览效果' fileName='ydcw'/>" style="width: 100%; padding: 5px; display: none;font-size: 12px;">
			<table width="98%">
				<tr>
			        <td class="infostd"><emp:message key="ydcw_title_28" defVal="发送条数:" fileName="ydcw"/><span id="yct"></span></td>
					<td class="infostd" id="showyct"><emp:message key="ydcw_title_29" defVal="余额：" fileName="ydcw"/><span id="ct"></span></td>	
			    </tr>
			    <tr>
				  <td colspan="2">
				    <label id="messages1"><font style="color: red;"><emp:message key="ydcw_title_45" defVal="余额不足不允许进行发送!" fileName="ydcw"/></font></label>
				  </td>
			   </tr>
				<tr>
				<td>
					<emp:message key="ydcw_title_30" defVal="有效号码数:" fileName="ydcw"/>
					<span id="effs"></span>
					</td>
					<td><emp:message key="ydcw_title_31" defVal="提交号码数:" fileName="ydcw"/>
					<span id="counts"></span>
					</td>
				    
					<td align="center">
					 <div id="showeffinfo" class="div_bg">
					       <p style="padding-top: 7px;font-size: 12px;"><emp:message key="ydcw_title_32" defVal="无效号码" fileName="ydcw"/>(<span id="alleff" style="color: red;"></span>)&nbsp;&nbsp;
					   <a id="arrowhead" class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a></p>
					   </div>
					  </td>
					 </tr>
					 </table>
				<div id="effinfo" style="margin-right: 5px;" class="div_bg">
				   <center>
				    <table id="effinfotable">
                                <tr height="10px;">
                                  <td colspan="2"></td>
                                </tr>
                     <tr>
					 <td align="left"><emp:message key="ydcw_title_33" defVal="黑名单号码:" fileName="ydcw"/>
						<span id="blacks"></span>
						</td>
						<td id="preinfonum">
							<%
							if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) 
							{
							 %>
								<a href="javascript:uploadbadFiles()"><emp:message key="ydcw_title_34" defVal="详情下载" fileName="ydcw"/></a>
							<%} %>
							<input type="hidden" id="badurl" name="badurl"  value=""></input>
						</td>
					</tr>
					 <tr>
					  <td align="left">	 
						<emp:message key="ydcw_title_35" defVal="重复号码:" fileName="ydcw"/>
						<span id="sames"></span>
						</td>
						<td></td>
					</tr>
						<tr><td align="left">
							<emp:message key="ydcw_title_36" defVal="格式非法:" fileName="ydcw"/>
							<span id="legers"></span>
						</td>
						<td></td>
						</tr>
						<tr>
						 	<td align="left">
						<emp:message key="ydcw_title_37" defVal="含关键字号码:" fileName="ydcw"/>
						<span id="keyW"></span>
						</td>
						<td></td>
					</tr>
				</table>
			</center>
			</div>
			<div style="height: 20px;"></div>
			<p align="left" style="font-size: 12px;font-weight: bold;">
				<emp:message key="ydcw_title_38" defVal="部分预览：" fileName="ydcw"/>
			</p>
			<form id="form3" name="form3" method="post" > 
			<div id="maindiv" style="overflow: auto;height: 300px;">
			<center>
			<table id="content" style="width: 470px;">
				<thead id="head">
				<tr align='center'>
				<th style='height: 25px;width:30px;'><emp:message key="ydcw_title_39" defVal="编号" fileName="ydcw"/></th>
				<th style='height: 25px;width:120px;'><emp:message key="ydcw_title_40" defVal="手机号码" fileName="ydcw"/></th>
				<th style='height: 25px;width:320px'><emp:message key="ydcw_title_41" defVal="短信内容" fileName="ydcw"/></th>
				</tr>
				</thead>
				<tbody id="newtable">
				</tbody>
			</table>
			</center>
			</div>
			<div id="footer" style="margin-top: 20px;" >
	    	  <center>
	    		<input class="btnClass5 mr23" type="button" id="subSend" value="<emp:message key='ydcw_button_4' defVal='发  送' fileName='ydcw'/>" onclick="javascript:btsend();"/>
			    <input onclick="javascript:btcancel();" class="btnClass6" type="button" value="<emp:message key='ydcw_button_2' defVal='取  消' fileName='ydcw'/>" id="cancelSend"/>
			     <br/>
			  </center> 
	    	</div>
			</form>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcw_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.blockUI.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/jquery.hotkeys.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/ydcw.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript">
		$(document).ready(function(){
			$('#u_o_c_explain').toggle(function(){
					$("#foldIcon").removeClass("unfold");
					$("#foldIcon").addClass("fold");
					$('#moreSelect').show();
				},function(){
					$("#foldIcon").removeClass("fold");
					$("#foldIcon").addClass("unfold");
					$('#moreSelect').hide();
			});
			floating("downlinks","infomodel");
			$("#showeffinfo").click(function() {
			    if($("#effinfo").is(":hidden")){
					$("#effinfo").show();
					$("#arrowhead").removeClass("unfold");
					$("#arrowhead").addClass("fold");
					if($("#messages1").is(":hidden")){
                        $("#effinfotable").css("top","62px");
                    }
                    else
                    {
                       $("#effinfotable").css("top","90px");
                    }
			    }else{
					$("#effinfo").hide();
					$("#arrowhead").removeClass("fold");
					$("#arrowhead").addClass("unfold");
				}
			    $("#subSend").hide();
				$("#subSend").show();
			});
		});
		
		</script>
	</body>
</html>
