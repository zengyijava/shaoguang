<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.entity.pasroute.LfMmsAccbind"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.template.LfTemplate"%>
<%@page import="com.montnets.emp.entity.ydcx.LfDfadvanced"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.servmodule.ydcx.constant.ServerInof"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String commonPath = iPath.substring(0,iPath.lastIndexOf("/"));
	commonPath = commonPath.substring(0,commonPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	List<LfMmsAccbind> mmsAccbinds = (List<LfMmsAccbind>)request.getAttribute("mmsAccbinds");
	
	LfSysuser lfSysuser = (LfSysuser)session.getAttribute("loginSysuser");
	
	@ SuppressWarnings("unchecked")
	List<LfTemplate> mmsList = (List<LfTemplate>)request.getAttribute("mmsTempList");
	Integer templateSize = mmsList.size();
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("sendDynMMS");
	menuCode = menuCode==null?"0-0-0":menuCode;
	//String isFlow = (String)request.getAttribute("isFlow");
	String result = session.getAttribute("eq_diffResult")==null?"-1":
		(String)session.getAttribute("eq_diffResult");
	
    Long taskId=(Long)request.getAttribute("taskId");
    
    String skin = session.getAttribute("stlyeSkin")==null?"default":
			(String)session.getAttribute("stlyeSkin");
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
		<title><emp:message key="ydcx_cxyy_dtcxfs_dtcxfs" defVal="动态彩信发送" fileName="ydcx"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/css/senddynamicmms.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/ydcx_applicationMms.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	</head>
	
<style> 
i{
    display: inline-block;
    height: 100%;
    vertical-align: middle;
}


<%if(StaticValue.ZH_HK.equals(langName)){%>
	#batchFileSend {
	    width: 700px;
	}
	
	#batchFileSend span {
    	width: 90px;
	}
<%}%>

</style>
	<body onload="show()" id="ydcx_dmm_senddynamicmms">
	    <input type="hidden" name="checkCount" id="checkCount" value="0"/>
		<input type="hidden" id="pathUrl" value="<%=request.getContextPath() %>" />
		<div id="container" class="container">
			<%--<%=ViewParams.getPosition(menuCode) %>
			--%><%=ViewParams.getPosition(langName,menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
					<input type="hidden" id="ipathUrl" value="<%=commonPath %>"/>
					<input type="hidden" id="iPath" value="<%=iPath %>"/>
					<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
					<div id="batchFileSend" class="block">
						<form class="ydcx_dynmmssend" name="dynMmsSend" id="dynMmsSend"
							action="dmm_sendDynMMS.htm?method=preview&langName=<%=langName %>" method="post"
							enctype="multipart/form-data" target="hidden_iframe">
							<iframe name="hidden_iframe" id="hidden_iframe"
								class="ydcx_hidden_iframe"></iframe>
							<div class="hidden" id="loginparam"></div>	
	               			<input type="hidden" value ="<%=taskId %>" id="taskId" name="taskId"/>
	               			<input type="hidden" value ="" id="tmUrl" name="tmUrl"/>
							<input type="hidden" id="mobileUrl" name="mobileUrl" value=""/>
							<input type="hidden" id="effCount" name="effCount" value=""/>
							<input type="hidden" id="subCount" name="subCount" value=""/>
							<input type="hidden" value="" id="isCharg" name="isCharg"/>
							<input type="hidden" id="error" name="error" value="" />
							<%--彩信发送成功的任务ID--%>
							<input type="hidden" id="oldTaskId" name="oldTaskId" value="" />
               				<div id="eq_sendDiv">
							<span class="righttitle"><emp:message key="ydcx_cxyy_dtcxfs_cxzt_mh" defVal="彩信主题：" fileName="ydcx"></emp:message></span>
								<table>
									<tr>
										<td class="ydcx_td">
											<input id="taskName" name="taskName" type="text" value="<emp:message key="ydcx_cxyy_dtcxfs_bzwcxnrfs" defVal="不作为彩信内容发送" fileName="ydcx"></emp:message>" maxlength="20" class="graytext input_bd div_bd yddcx_taskname" />
										</td>
									</tr>
								</table>
							</div>
	               			
	               			
	               			<div id="eq_sendDiv">
							<span class="righttitle"><emp:message key="ydcx_cxyy_dtcxfs_cxbt_mh" defVal="彩信标题：" fileName="ydcx"></emp:message></span>
								<table>
									<tr>
										<td class="ydcx_td">
											<input id="tmName" name="tmName" type="text" value="" class="graytext input_bd div_bd yddcx_taskname"  maxlength="20" />
										</td>
									</tr>
								</table>
							</div>
	               			
	               			
	               			<div id="eq_sendDiv"  class="ydcx_eq_senddiv">
								<span class="righttitle"><emp:message key="ydcx_cxyy_dtcxfs_wjdr" defVal="文件导入：" fileName="ydcx"></emp:message></span>
               					<div id="mmsFileDiv" class="ydcx_mmsfilediv">
									  <a id="upfile" name="upfile" class="afileclass1 ydcx_cursor_pointer" ><emp:message key="ydcx_cxyy_dtcxfs_scwj" defVal="上传文件" fileName="ydcx"></emp:message></a>
									  	 <label class="ydcx_lable1">&nbsp;&nbsp;<emp:message key="ydcx_cxyy_dtcxfs_zd100m" defVal="(最大100M)" fileName="ydcx"></emp:message></label>
									 <label class="ydcx_lable2">
									 <a  id="downlinks" class="ydcx_downlinks"><emp:message key="ydcx_cxyy_dtcxfs_gsts" defVal="格式提示" fileName="ydcx"></emp:message></a></label>
									 <%-- 	 id="filesdiv" style="width: 450px;position:absolute; height: 30px;margin-bottom: 0px;top:122px !important;top: 118px;left: 113px !important;left: 105px;
									  --%>
									  <div id="filesdiv" class="ydcx_filesdiv">
				                          <input id='numFile' name='numFile'  type='file' onchange="javascript:upFile();" class="numfileclass ydcx_numfile" value=""/>
									  </div>
								  
									<div id="upTipDiv" class="div_bg ydcx_uptipdiv"></div>
				                </div>
	               		   </div>
	               			
	               			
	               		<div id="eq_sendDiv" class="ydcx_eq_senddiv">
							        <span class="righttitle" ><emp:message key="ydcx_cxyy_dtcxfs_fsmb_mh" defVal="发送模板：" fileName="ydcx"></emp:message></span>
								<div class="ydcx_dv1">
								<div class="ydcx_dv2">
										<input type="button" class="btnClass4 ydcx_dv2_btnClass4" value="<emp:message key="ydcx_cxyy_dtcxfs_djxzmb" defVal="点击选择模板" fileName="ydcx"></emp:message>" onclick="chooseMmsTpl()"/>
										<%--选择的模板ID--%>
										<input type="hidden" id="mmstemplateid" name="mmstemplateid" value=""/>
										<input type="hidden" id="mmstemplateurl" name="mmstemplateurl" value=""/>
									  	<a class="ydcx_cursor_pointer" onclick="openTemDiv('<%=ViewParams.MMSTEMPCODE%>')"><emp:message key="ydcx_cxyy_dtcxfs_ljxj" defVal="[立即新建]" fileName="ydcx"></emp:message></a>
								</div>
								
								</div>
								<div id="templatepre" class='div_bg ydcx_templatepre'>
								</div>	
								
              					<div id="mmsContent" calss="ydcx_mmsContent"> 
              					<%--
              					<% if(templateSize == 0) {%> style="height: 10px;width:242px;"<%}
										else if((templateSize == 1)){
											%> style="width:242px;height: 40px;float:left;"<%
										}else if((templateSize == 2)){
											%> style="width:242px;height: 70px;float:left;"<%
										}else if((templateSize == 3)){
											%> style="width:242px;height: 100px;float:left;"<%
										}else if((templateSize == 4)){
											%> style="width:242px;overflow-y:auto;height: 130px;float:left;"<%
										}else if((templateSize >= 5)){
											%> style="width:242px;overflow-y:auto;height: 160px;float:left;"<%
										}
									%> 
              					
              					 <table id="modelTable" width="225px" style="font-size:12px;display: none;"  class="div_bd">
											 <%
												if (mmsList != null && mmsList.size() > 0)
												{
													for(LfTemplate lv : mmsList)
													{
												%>
												<tr>
												<td align="center" class="div_bd" style="height:30px;width: 45px;1;border-right: 0;border-bottom: 1px">
													<input type="radio" id ="tmMsg" name="tmMsg" value="<%=lv.getTmid()%>" tmMsg="<%=lv.getTmMsg() %>" /></td>
												<td style="border-left: 0;" class="div_bd">
													<a style='cursor:pointer;color:#2a6fbe;' onclick='doPreview("<%=lv.getTmMsg() %>")'>
														<%=lv.getTmName() %>(ID:<%=lv.getTmid() %>)
													</a>
												</td>
												</tr>
												<%}
												}													
											%>
								    </table>--%>
								</div>
								
								
	               		  </div>
	               		  <div id="eq_sendDiv" class="eq_sendDiv_c">
						  <span><emp:message key="ydcx_cxyy_dtcxfs_fssj_mh" defVal="发送时间：" fileName="ydcx"></emp:message></span>
						  <div class="ydcx_sendtmdv">
						    <input type="radio" name="sendType" value="0" id="sendNow" 
								checked="checked" onclick="javascript:$('#time2').hide()" />&nbsp;<emp:message key="ydcx_cxyy_dtcxfs_ljfs" defVal="立即发送" fileName="ydcx"></emp:message>
							<input type="radio" name="sendType" value="1" class="ydcx_sendType"
								onclick="javascript:$('#time2').show()"/>&nbsp;<emp:message key="ydcx_cxyy_dtcxfs_dsfs" defVal="定时发送" fileName="ydcx"></emp:message>
							<label id="time2" class="ydcx_time2">
								<input type="text" class="Wdate div_bd" readonly="readonly"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false})"
									id="sendtime" name="sendtime" value="">
							</label>
						  </div>
						</div>
								<div id="eq_sendDiv2" class="ydcx_eq_senddiv2">
								<span id="u_o_c_explain" class="div_bg ydcx_u_o_c_explain"><b><emp:message key="ydcx_cxyy_dtcxfs_gjxz" defVal="高级设置" fileName="ydcx"></emp:message></b>&nbsp;&nbsp;&nbsp;&nbsp;<a id="foldIcon" class="unfold ydcx_foldIcon">&nbsp;&nbsp;&nbsp;&nbsp;</a></span>
								<div id="moreSelect" class="div_bg ydcx_moreSelect">
								<div style="width:580px" align="right">
									<a id="setdefualt" href="javascript:setDefault()"  class="alink" ><emp:message key="ydcx_cxyy_dtcxfs_xxcwmr" defVal="选项存为默认" fileName="ydcx"></emp:message></a>
								</div>
								<table class="ydcx_tb1">
									<%--<tr><td style="width:108px;" align="right"><font style="padding-left:35px"><%=StaticValue.SMSACCOUNT%>：</font></td>
									--%><tr><td class="ydcx_tb1_td1" align="right"><font class="ydcx_tb1_font" ><emp:message key="ydcx_cxyy_dtcxfs_spzh_mh" defVal="SP账号： " fileName="ydcx"></emp:message></font></td>
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
													<option value="<%=acc.getMmsUser()%>" <%=spUserId != null && !"".equals(spUserId) && spUserId.equals(acc.getMmsUser())?"selected":"" %>>
													<%=acc.getMmsUser()%></option>
												<% 
													}	
												}
											 %>
											</select>
											</div>
										</td>
									</tr>
									
								</table>
								</div>
							</div>
	               			
							<div class="b_F_btn ydcx_b_F_btn">
								<input type="hidden" value="1" id="subState" name="subState" />
								<input id="preSend" class="btnClass5 mr23 ydwx_borderRadius" type="button" value="<emp:message key="ydcx_cxyy_common_btn_22" defVal="提交" fileName="ydcx"></emp:message>" />
								<input id="qingkong" onclick="javascript:location.href=location.href;" class="btnClass6 ydwx_borderRadius" type="button" value="<emp:message key="ydcx_cxyy_common_btn_9" defVal="重置" fileName="ydcx"></emp:message>" />
							<input type = "hidden" name="preStr" id="preStr" value="" />
							</div>
							
							
							
							<div id="detail_Info" class="ydcx_detail_Info">
									<input type="hidden" value="" id="spFeeResult" name="spFeeResult"/>
								   <table id="infos" width="98%">
								    <tr>
								       <td class="infostd"><emp:message key="ydcx_cxyy_dtcxfs_fsts_mh" defVal="发送条数:" fileName="ydcx"></emp:message><span id="yct"></span></td>
								      <td class="infostd"  id="showyct"> <emp:message key="ydcx_cxyy_dtcxfs_jgye_mh" defVal="机构余额:" fileName="ydcx"></emp:message><span id="ct"></span></td>
								       </tr>
								       <tr>
								       <td colspan="2">
								        <label id="messages1"></label>
								       </td>
								    </tr>
									<tr>
										<td><emp:message key="ydcx_cxyy_dtcxfs_tjhms_mh" defVal="提交号码数:" fileName="ydcx"></emp:message><span id="counts"></span></td>
										<td><emp:message key="ydcx_cxyy_dtcxfs_yxhms_mh" defVal="有效号码数:" fileName="ydcx"></emp:message><span id="effs"></span></td>
										<td align="center">
										   <div id="showeffinfo" class="div_bg">
										       <p class="ydcx_showeffinfo-font"><emp:message key="ydcx_cxyy_dtcxfs_wxhm" defVal="无效号码" fileName="ydcx"></emp:message>(<span id="alleff" class="ydcx_alleff"></span>)&nbsp;&nbsp;
										   <a id="arrowhead" class="unfold">&nbsp;&nbsp;&nbsp;&nbsp;</a></p>
										   </div>
										</td>
									</tr>
									</table>
									<div id="effinfo" class="div_bg">
								   <center>
								    <table id="effinfotable">
                                      <tr height="10px;">
                                        <td colspan="2"></td>
                                      </tr>
 								      <tr>
									      <td align="left"><emp:message key="ydcx_cxyy_dtcxfs_hmdhm_mh" defVal="黑名单号码：" fileName="ydcx"></emp:message><span id="blacks"></span></td>
									      <td id="baddownload"><a href="javascript:uploadbadFiles()"><emp:message key="ydcx_cxyy_dtcxfs_xqxz" defVal="详情下载" fileName="ydcx"></emp:message></a><input type="hidden" id="badurl" name="badurl"  value=""></input></td>
								      </tr>
								      <tr>
									      <td align="left"><emp:message key="ydcx_cxyy_dtcxfs_cfhm_mh" defVal="重复号码：" fileName="ydcx"></emp:message><span id="sames"></span></td>
									      <td></td>
								      </tr>
								      <tr>
								          <td align="left"><emp:message key="ydcx_cxyy_dtcxfs_gsff_mh" defVal="格式非法：" fileName="ydcx"></emp:message><span id="legers"></span></td>
								          <td></td>
								      </tr>
								      <tr>
								          <td align="left"><emp:message key="ydcx_cxyy_dtcxfs_hgjzhm_mh" defVal="含关键字号码：" fileName="ydcx"></emp:message><span id="keyW"></span></td>
								          <td></td>
								      </tr>
								    </table>
								    </center>
							    </div>
							    <div class="ydcx_bfyldv"></div>
								<p class="ydcx_bfyldv_p">
									<emp:message key="ydcx_cxyy_dtcxfs_bfyl_mh" defVal="部分预览：" fileName="ydcx"></emp:message>
								</p>
								<table id="content" class="ydcx_content">
									<thead>
									<tr align='center'>
									<th ><emp:message key="ydcx_cxyy_dtcxfs_bh" defVal="编号" fileName="ydcx"></emp:message></th>
									<th ><emp:message key="ydcx_cxyy_dtcxfs_sjhm" defVal="手机号码" fileName="ydcx"></emp:message></th>
									<th ><emp:message key="ydcx_cxyy_dtcxfs_cxnr" defVal="彩信内容" fileName="ydcx"></emp:message></th>
									</tr>
																		</thead>
									<tbody id="newtable">
									</tbody>
								</table>
								<div id="footer" class="ydcx_footer">
						    	  <center>
						    		<input id="btsend" class="btnClass5 mr23" type="button" value="<emp:message key="ydcx_cxyy_dtcxfs_fs" defVal="发  送" fileName="ydcx"></emp:message>" onclick="javascript:btsend()"/>
								    <input id="btcancel" onclick="javascript:$('#detail_Info').dialog('close');" class="btnClass6" type="button" value="<emp:message key="ydcx_cxyy_dtcxfs_qx" defVal="取  消" fileName="ydcx"></emp:message>" />
								    <br/>
								  </center> 
						    	</div>	
							</div>
							
						</form>
					</div>
                     <%--格式提示--%>
                     
					<%if(StaticValue.ZH_HK.equals(langName)){%>
				        <div id="infomodel1" class="infomodelclass ydcx_infomodel1_dv1">
					<%}else if(StaticValue.ZH_TW.equals(langName)){%>
						<div id="infomodel1" class="infomodelclass ydcx_infomodel1_dv2">
					<%}else {%>
						<div id="infomodel1" class="infomodelclass ydcx_infomodel1_dv3">
					<%}%>
                        <center>
                        <table class="ydcx_tb2">
                          <tr>
                            <td colspan="2" class="ydcx_tb2_td1" align="left"><emp:message key="ydcx_cxyy_dtcxfs_wjgsrt_mh" defVal="文件格式如图:" fileName="ydcx"></emp:message></td>
                          </tr>
                           <tr>
			                 <td valign="top" align="left"><emp:message key="ydcx_cxyy_dtcxfs_txtgs_mh" defVal="txt格式：" fileName="ydcx"></emp:message></td>
			                 <td valign="top">
			                 <%--<div id="txtstyle" style="width: 155px;height: 69px;background : url('<%=skin %>/images/dtsend.png') no-repeat; "></div></td>--%>
	                           <%if(StaticValue.ZH_HK.equals(langName)){%>
	                           		<div id="txtstyle" style="width: 157px;height:69px;background : url('<%=commonPath %>/common/img/dtsend_zh_HK.png') no-repeat; "></div>
	                           <%}else if(StaticValue.ZH_TW.equals(langName)){%>
	                           		<div id="txtstyle" style="width: 155px;height:71px;background : url('<%=commonPath %>/common/img/dtsend_zh_TW.png') no-repeat; "></div>
	                           <%}else{%>
	                           		<div id="txtstyle" style="width: 155px;height:69px;background : url('<%=skin %>/images/dtsend.png') no-repeat; "></div>
	                           <%}%>
			                </tr>
			                <tr height="12px;">
			                <td colspan="2"></td>
			                </tr>
			                <tr>
			                 <td valign="top" align="left" width="55px;"><emp:message key="ydcx_cxyy_dtcxfs_xlsgs_mh" defVal="xls格式：" fileName="ydcx"></emp:message></td>
			                 <td valign="top">
			                 <%--<div id="xlsstyle" style="width: 155px;height: 85px;background: url('<%=skin %>/images/dtsend.png') no-repeat 0px -80px;"></div></td>--%>
	                           <%if(StaticValue.ZH_HK.equals(langName)){%>
	                           		<div id="xlsstyle" style="width: 157px;height: 82px;background: url('<%=commonPath %>/common/img/dtsend_zh_HK.png') no-repeat 0px -82px;"></div>
	                           <%}else if(StaticValue.ZH_TW.equals(langName)){%>
	                           		<div id="xlsstyle" style="width: 155px;height: 84px;background: url('<%=commonPath %>/common/img/dtsend_zh_TW.png') no-repeat 0px -83px;"></div>
	                           <%}else{%>
	                           		<div id="xlsstyle" style="width: 155px;height: 85px;background: url('<%=skin %>/images/mmsphone.png') no-repeat 0px -80px;"></div>
	                           <%}%>
			                </tr>
                          <tr>
                          <td colspan="2" align="left">
                           <span  style="font-weight: bold;font-size: 14px;"><emp:message key="ydcx_cxyy_dtcxfs_zy_mh" defVal="注意：" fileName="ydcx"></emp:message></span><br/>
                           <emp:message key="ydcx_cxyy_dtcxfs_1mhznyyghm" defVal="1.每行只能有一个号码及一条短信内容的参数；" fileName="ydcx"></emp:message><br/>
                           <emp:message key="ydcx_cxyy_dtcxfs_2hmgsbzq" defVal="2.号码格式不正确，上传时由EMP过滤；" fileName="ydcx"></emp:message><br/>
                           <emp:message key="ydcx_cxyy_dtcxfs_3hmbhyhmdn" defVal="3.号码包含于黑名单内，上传时由EMP过滤。" fileName="ydcx"></emp:message>
                          </td>
                          </tr>
                        </table>
                       </center>
                    </div>
			</div>
			<div id="tempView" class="ydcx_tempView" title="<emp:message key="ydcx_cxyy_dtcxfs_cxnr" defVal="彩信内容" fileName="ydcx"></emp:message>">
		
			<input type="hidden" id="tmmsId" value=""/>
			<div class="ydcx_mobile_parent">
				<div id="mobile" class="ydcx_mobile" style="width:240px;height:470px;position: absolute;background: url('<%=commonPath %>/common/img/iphone5.jpg') no-repeat">
					<center>
						<div id="pers" class="ydcx_pers">
							<div id="showtime" class="ydcx_showtime"></div>
							<div id='chart' class="ydcx_chart">
							</div>
						</div>
					</center>
					<div id="screen" class="ydcx_screen">
					</div>
					<center>
					<table>
					<tr>
					  <td>
					     <label id="pointer" class="ydcx_pointer"></label>
					     <label id="nextpage" class="ydcx_nextpage"></label>
					  </td>
					</tr>
					<tr align="center">
						<td>
						   <label id="currentpage" class="ydcx_currentpage"></label>
						</td>
					</tr>
						</table>				
					</center>
				</div>
			</div>
			<div id="inputParamCnt1" class="ydcx_inputParamCnt1">
			</div>			
		</div>	
		<%--等待旋转--%>
				<div id="probar" class="ydcx_probar">
					<p class="ydcx_probar_p">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="ydcx_cxyy_dtcxfs_clzqsd" defVal="处理中,请稍等....." fileName="ydcx"></emp:message>
					</p>
					<div id="shows">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<img class="ydcx_shows_img" src="<%=commonPath%>/common/img/loader.gif" />
					</div>
				</div>
				
			<div id="tmplDiv" title="<emp:message key="ydcx_cxyy_dtcxfs_cxmbxz" defVal="彩信模板选择" fileName="ydcx"></emp:message>"  >
				<iframe id="tempFrame" name="tempFrame" class="ydcx_tempFrame"  marginwidth="0" scrolling="no" frameborder="no" src ="<%=commonPath%>/common/blank.jsp " ></iframe>
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
		<div id="message" title="<emp:message key="ydcx_cxyy_dtcxfs_ts" defVal="提示" fileName="ydcx"></emp:message>" class="ydcx_message" >
			<div class="ydcx_message_dv1"></div>
	 		<center>
			<label ><emp:message key="ydcx_cxyy_dtcxfs_fszwgcg" defVal="发送至网关成功！" fileName="ydcx"></emp:message></label>
				<a href="javascript:sendRecord('1100-1800')"  class="alink" ><emp:message key="ydcx_cxyy_dtcxfs_ckfsjl" defVal="查看发送记录" fileName="ydcx"></emp:message></a>
			</center>
			<br>
			<div class="ydcx_message_dv2" ></div>
			 <center>
			    <input id="close" onclick="javascript:closemessage();" class="btnClass6" type="button" value="<emp:message key="ydcx_cxyy_dtcxfs_gb" defVal="关闭" fileName="ydcx"></emp:message>" />
			</center> 
		</div>
		<div class="clear2"></div>
				<script>
			var serverName = "<%=serverName%>";
			var base_path = "<%=path%>";
		</script>
		<script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
		<script src="<%=commonPath%>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
		<script src="<%=iPath%>/js/dynMmsSend.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>
		
			var langName ="<%=langName %>";	
			function show()
			{
				var pathUrl = $("#pathUrl").val();
				var lgguid = $("#lgguid").val();
				<% 
					String mmsResult=(String)request.getAttribute("mmsresult");
					if(mmsResult != null && mmsResult != ""){
						if("nogwfee"==mmsResult || "feefail"==mmsResult || "feeerror"==mmsResult){
							 	//mmsResult = "获取运营商余额失败！";
							 	mmsResult = MessageUtils.extractMessage("ydcx","ydcx_cxyy_dtcxfs_hqyysyesb",request);
						}else if(mmsResult.indexOf("lessgwfee")==0){
								//mmsResult ="运营商余额不足！";
								mmsResult =MessageUtils.extractMessage("ydcx","ydcx_cxyy_dtcxfs_yysyebz",request);
						}
					if("000".equals(mmsResult.substring(0,3)))
					{
				%>
						$("#oldTaskId").attr("value","<%=mmsResult.substring(4)%>");
						$("#message").dialog({width:300,height:180});
						$("#message").dialog("open");
						getCt();
				<%
					}
					else
					{
				%>
						alert("<%=mmsResult%>");
						getCt();
						window.location.href = "<%=path%>/dmm_sendDynMMS.htm?lgguid="+lgguid;
				<%
					}
				}
				%>
			}
			
			$(function(){
			  $("#mmsUser").isSearchSelect({'width':'180','isInput':false,'zindex':0});
		   });
		</script>
	</body>
</html>
