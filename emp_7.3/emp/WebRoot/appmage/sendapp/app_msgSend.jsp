<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List" %>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.app.LfDfadvanced"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String absolutePath = request.getRealPath("/").replaceAll("\\\\","/");
if(!absolutePath.endsWith("/"))
{
	absolutePath += "/";
}
@SuppressWarnings("unchecked")
Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
@SuppressWarnings("unchecked")
Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
String menuCode = titleMap.get("msgsend");

String result = session.getAttribute("app_sendResult")==null?"-1":
	(String)session.getAttribute("app_sendResult");

List<Userdata> userData = (List<Userdata>) request.getAttribute("userData");
List<LfBusManager> busList = (List<LfBusManager>) request.getAttribute("busList");

String lguserid = request.getParameter("lguserid");
String lgcorpcode = request.getParameter("lgcorpcode");
String lgguid = request.getParameter("lgguid");

String appAccount = (String)request.getAttribute("appAccount");
String baseAppAccount = (String)request.getAttribute("baseAppAccount");
String taskId = (String)request.getAttribute("taskId");
//高级默认设置
LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
String specialChar = StaticValue.getSmscontentSpecialcharStr();
    
%>

<!DOCTYPE html>
<html>
  <head>
 	<meta charset="UTF-8">
    <title><emp:message key="appmage_xxfb_appxxfs_text_title" defVal="App信息发送" fileName="appmage"></emp:message></title>
	<link rel="stylesheet" href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link rel="stylesheet" href="<%=iPath %>/css/pageEdit.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" href="<%=commonPath %>/common/css/tipsy.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" type="text/css" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link rel="stylesheet" type="text/css" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" type="text/css" href="<%=commonPath %>/frame/frame3.0/skin/default/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
	<style>
	#detail_Info .infos{
		margin: 1px 1%;
		line-height: 20px;
	}
	#detail_Info .infos td{
		padding: 0;
	}
	#effinfotable{
		width: 480px;
	}
	</style>
	<script>
		var specialChar = "<%=specialChar%>";
		var special = specialChar.split(",");
	</script>
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=iPath %>/css/sendapp_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>">
	<%}%>
  </head>
  
  <body>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
    <div class="appWrap">
		<%--内容编辑区--%>
		<div class="pageEditArea">
<%--			<h2>内容编辑区</h2>--%>
			<p><span style="font-weight:bold;"><emp:message key="appmage_xxfb_appxxfs_text_zhiying" defVal="指引：" fileName="appmage"></emp:message></span><emp:message key="appmage_xxfb_appxxfs_text_tips" defVal="本页面是用于发送手机APP广播类信息及短信，即选中的发送对象都能看到本页发送的信息。" fileName="appmage"></emp:message></p>
			<div class="formArea">
				<form id="appForm" action="app_msgsend.htm?method=send" method="post">
				<input type="hidden" id="gt1" name="gt1" value=""/>
				<input type="hidden" id="gt2" name="gt2" value=""/>
				<input type="hidden" id="gt3" name="gt3" value=""/>
				<input type="hidden" id="gt4" name="gt4" value=""/>
				<input type="hidden" value="<%=appAccount%>" id="appAccount" name="appAccount" />
				<input type="hidden" value="<%=baseAppAccount%>" id="baseAppAccount" name="baseAppAccount" />
				<input type="hidden" value ="<%=taskId%>" name="taskId" id="taskId"/>
				<input type="hidden" value="<%=lguserid%>" id="lguserid" name="lguserid" />
				<input type="hidden" value ="<%=lgcorpcode%>" name="lgcorpcode" id="lgcorpcode"/>
				<input type="hidden" value ="<%=lgguid%>" name="lgguid" id="lgguid"/>
				<input type="hidden" value ="," name="appcode" id="appcode"/>
				<input type="hidden" value ="," name="group" id="group"/>
				<input type="hidden" name="groupStrTemp" id="groupStrTemp" value=",">
				<input type="hidden" value ="," name="appcorpcode" id="appcorpcode"/>
				<input type="hidden" name="appCorpCodeTemp" id="appCorpCodeTemp" value=",">
				<input type="hidden" value="0" id ="manCountTemp" name ="manCountTemp"/>
				<input type="hidden" value="0" id="hidIsDoOk" name="hidIsDoOk"/><%-- 控制选择人员弹出框关闭时清空。0为关闭操作；1为确定操作 --%>
				
				<input type="hidden" value="0" id="hidisSmsSend" name="hidisSmsSend"/>
				<input type="hidden" value="" id="hidSubCount" name="hidSubCount" />
				<input type="hidden" value="" id="hidEffCount" name="hidEffCount" />
				<input type="hidden" value="" id="hidMobileUrl" name="hidMobileUrl" />
				<input type="hidden" value="" id="hidPreSendCount" name="hidPreSendCount" />
				<input type="hidden" value="" id="isCharg" name="isCharg"/><%-- 机构扣费标识 --%>
				<input type="hidden" value="" id="gwFee" name="gwFee"/><%-- 运营商扣费状态 --%>
				<div class="fieldArea">
					<div class="listArea">
						<span class="field_tit"><emp:message key="appmage_xxfb_appxxfs_text_sendsubject" defVal="发送主题：" fileName="appmage"></emp:message></span>
						<div class="field_bd" style="position:relative;">
							<input type="text" name="title" class="input_bd" id="sendSub" maxlength="32">
							<span class="app_placeholder"></span>
						</div>
					</div>
					<div class="listArea">
						<span class="field_tit"><emp:message key="appmage_xxfb_appxxfs_text_sendobject" defVal="发送对象：" fileName="appmage"></emp:message></span>
						<div class="field_bd" style="position:relative;height:24px;">
							<span id="selectedPerson">
								<input onclick="javascript:showInfo();" type="button" class="btnClass2" value="<emp:message key="appmage_xxfb_appxxfs_opt_choosepeople" defVal="选择人员" fileName="appmage"></emp:message>" id="selectWorkerButton">
								<div class="chooseWrap">
								<emp:message key="appmage_xxfb_appxxfs_text_yixuanze" defVal="已选择 " fileName="appmage"></emp:message>
								<b id="selectedTotal">0</b>
								<emp:message key="appmage_xxfb_appxxfs_text_usernum" defVal="个用户 " fileName="appmage"></emp:message>
								</div>
							</span>
						</div>
					</div>
					<div class="listArea">
						<span class="field_tit"><emp:message key="appmage_xxfb_appxxfs_text_sendcontent" defVal="发送内容：" fileName="appmage"></emp:message></span>
						<div class="field_bd">
							<div id="sendsel-div" style="float:right;margin-right: 20px;height: 26px;line-height: 26px;"><label><input type="checkbox" name="isSmsSend" id="isSmsSend" value="" style="vertical-align: middle;"/>&nbsp;&nbsp;<emp:message key="appmage_xxfb_appxxfs_opt_synsendmsms" defVal="同时发送短信" fileName="appmage"></emp:message></label></div>
							<ul id="sendTabs">
								<li data-tab="txt">
									<input type="radio" id="sendTxt" name="sendType" value="0">
									<label for="sendTxt"><emp:message key="appmage_xxfb_appxxfs_text_word" defVal="文字" fileName="appmage"></emp:message></label>
								</li>
								<li data-tab="media">
									<input type="radio" id="sendMedia" name="sendType" value="1">
									<label for="sendMedia"><emp:message key="appmage_xxfb_appxxfs_text_multimedia" defVal="多媒体" fileName="appmage"></emp:message></label>
								</li>
							</ul>
							<div class="tabsContent">
								<div class="hide" data-tab="txt">
									<dl>
										<dd><textarea name="msg" id="sendContent" class="sendContent" cols="30" rows="12" onblur="showInputInfo()"></textarea></dd>
										<dd>
											<b id="countLimit">0</b><b id="maxLen">/990<emp:message key="appmage_xxfb_appxxfs_text_zi" defVal="字" fileName="appmage"></emp:message></b>
											<font id="ft-count" style="line-height:15px;float:right;margin-right:32px; color: #656565;">
											<emp:message key="appmage_xxfb_appxxfs_text_mobile" defVal="移动" fileName="appmage"></emp:message>(<b id="ft1">0</b>)&nbsp;&nbsp;
											<emp:message key="appmage_xxfb_appxxfs_text_unicom" defVal="联通" fileName="appmage"></emp:message>(<b id="ft2">0</b>)&nbsp;&nbsp;
											<emp:message key="appmage_xxfb_appxxfs_text_telecommunications" defVal="电信" fileName="appmage"></emp:message>(<b id="ft3">0</b>)&nbsp;&nbsp;
											<emp:message key="appmage_xxfb_appxxfs_text_foreign" defVal="国外" fileName="appmage"></emp:message>(<b id="ft4">0</b>)
											</font>
										</dd>
									</dl>
								</div>

								<div class="hide" data-tab="media">
									<dl class="uploadFile">
										<dt>
											<input type="hidden">
										</dt>
										<dt>
											<input type="text" id="txt_input" name="txt" class="w_input inputSpec" readonly="readonly">
											<input type="hidden" value ="" name="fileUrl" id="fileUrl"/>
											<input type="button" value='<emp:message key="appmage_xxfb_appsyfb_opt_liulan" defVal="浏览" fileName="appmage"></emp:message>' size="30" class="btnClass2">
											<input type="file" id="uploadFile" class="files" name="uploadFile" onchange="upload(this)">
										</dt>
										<dt class="formatIntro">
										<emp:message key="appmage_xxfb_appxxfs_text_format" defVal="格式：" fileName="appmage"></emp:message><br>
										<emp:message key="appmage_xxfb_appxxfs_text_formatcontent" defVal="支持jpg、jpeg、png、bmp图片格式；amr音频格式；3gp、mp4视频格式。容量不超过5M，图片大小建议360像素*200像素。" fileName="appmage"></emp:message>
										</dt>
									</dl>
								</div>
							<div id="configDiv" style="margin-top:10px;" data-tab="checkbox" class="hide">
									<span id="u_o_c_explain" class="div_bg" style="cursor: pointer;"><b><emp:message key="appmage_xxfb_appxxfs_opt_advancedsettings" defVal="高级设置" fileName="appmage"></emp:message></b>&nbsp;&nbsp;&nbsp;&nbsp;<a id="foldIcon" class="unfold" style="text-decoration: none">&nbsp;&nbsp;&nbsp;&nbsp;</a></span>
										<div id="moreSelect" style="height: 80px" class="div_bg">
										<table style="width:100%;height:100%;border:0px">
											<tr><td style="width:108px;"><font style="padding-left:<%=StaticValue.ZH_HK.equals(empLangName)?0:35 %>px"><%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_3",request) %></font></td>
												<td>
													<select id="spUser" name="spUser"  class="input_bd" style="width:240px;vertical-align:middle;height: 22px;" onchange="setGtInfo()">
				                 					<% 
				                 						if(userData != null && userData.size()>0){
				                 							Userdata user= null;
				                 							String spUserId = lfDfadvanced != null?lfDfadvanced.getSpuserid():"";
				                 							for(int i=0;i<userData.size();i++){
				                 							user = userData.get(i);
				                 					%>
				                 							<option value="<%=user.getUserId()%>"
				                 							<%=spUserId != null && !"".equals(spUserId) && spUserId.equals(user.getUserId())?"selected":"" %>>
				                 							<%=user.getUserId() %>(<%=user.getStaffName() %>)</option>
				                 					<% 
				                 							}
				                 						}
				                 					%>
				                 					</select>
												</td>
												<td>
													<a id="setdefualt" href="javascript:setDefault()"  class="alink" ><emp:message key="appmage_xxfb_appxxfs_opt_saveddefault" defVal="选项存为默认" fileName="appmage"></emp:message></a>
												</td>
											</tr>
											<tr><td style="width:108px;"><font style="padding-left:<%=StaticValue.ZH_HK.equals(empLangName)?0:35 %>px"><emp:message key="appmage_xxfb_appxxfs_text_businesstype" defVal="业务类型：" fileName="appmage"></emp:message></font></td>
												<td>
													<select id="busCode"  name="busCode"  class="input_bd" style="width:240px;vertical-align:middle;height: 22px;">
												<%
													if (busList != null && busList.size() > 0) {
														String busCode = lfDfadvanced != null?lfDfadvanced.getBuscode():"";
														for (LfBusManager busManager : busList) {
												%>
														<option value="<%=busManager.getBusCode()%>"
																<%=busCode != null && !"".equals(busCode) && busCode.equals(busManager.getBusCode())?"selected":"" %>>
																<%=busManager.getBusName().replace("<","&lt;").replace(">","&gt;")%>(<%=busManager.getBusCode()%>)
														</option>
												<%
														}
													}
												%>
													</select>
												</td>
											</tr>
										</table>
								</div>
								</div>
							</div>
						</div>
					</div>
					<div class="listArea">
						<div class="field_bd">
							<p class="appTips"><emp:message key="appmage_xxfb_appxxfs_text_apptips" defVal="发送之后，系统将信息推送至已选定的用户手机中，请仔细核对发送的内容信息。" fileName="appmage"></emp:message></p>
<%--							<input id="preview" class="btnClass5 mr23" type="button" value="提交"/>--%>
<%--							<input id="sendReset" class="btnClass6" type="button" value="重置 " onclick="javascript:reloadPage();" />--%>
						</div>
					</div>
				</div>
				<div id="detail_Info" style="width: 100%; padding: 5px; display: none;position: relative;">
					<fieldset style="border: 1px solid #bec3d1;">
						<legend style="font-size: 12px;font-weight: bold;margin-left: 1%;margin-top:12px;">
							<emp:message key="appmage_xxfb_appxxfs_text_appyulan" defVal="APP消息发送预览：" fileName="appmage"></emp:message>
						</legend>
						<table id="apppinfos" width="98%" style="border:0;line-height: 20px;">
							  <tr>
							  	 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="appmage_xxfb_appxxfs_text_totalnumber" defVal="提交总数：" fileName="appmage"></emp:message><span id="subCount"></span></td>
							  	 <td><emp:message key="appmage_xxfb_appxxfs_text_activeusernum" defVal="有效用户数：" fileName="appmage"></emp:message><span id="effCount"></span></td>	
							  </tr>
							  <tr>
							  	 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="appmage_xxfb_appxxfs_text_repeatuser" defVal="重复用户数：" fileName="appmage"></emp:message><span id="repeat"></span></td>
							  	 <td><emp:message key="appmage_xxfb_appxxfs_text_illegalformat" defVal="格式非法：" fileName="appmage"></emp:message><span id="illegal"></span></td>	
							  </tr>
						</table>
					</fieldset>
					<div style="height: 15px;"></div>	
					<fieldset id="fieldset_info" style="border: 1px solid #bec3d1;">
						<legend style="font-size: 12px;font-weight: bold;margin-left: 1%;margin-top:10px;">
							<emp:message key="appmage_xxfb_appxxfs_text_smsyulan" defVal="短信发送预览：" fileName="appmage"></emp:message>
						</legend>							  
							<table id="infos" class="infos" width="98%" style="border:0;">
							  <tr>
							    <td class="infostd">&nbsp;&nbsp;&nbsp;<emp:message key="appmage_xxfb_appxxfs_text_sendquantity" defVal="发送条数：" fileName="appmage"></emp:message><span id="yct"></span></td>								    
							    <td class="infostd" id="showyct">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="appmage_xxfb_appxxfs_text_jgye" defVal="机构余额：" fileName="appmage"></emp:message><span id="ct"></span></td>
							    <td class="infostd" id="shospfee">&nbsp;&nbsp;<emp:message key="appmage_xxfb_appxxfs_text_spzhye" defVal="SP账号余额：" fileName="appmage"></emp:message><span id="spfee"></span></td>								    
							  </tr>
							   <tr>
								  <td colspan="2">
								    <label id="messages2"><font style="color: red;"></font></label>
								  </td>
							  </tr>
							  <tr>
								  <td colspan="2">
								    <label id="messages1"><font style="color: red;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="appmage_xxfb_appxxfs_text_yebzbfs" defVal="机构余额不足，不允许进行发送!" fileName="appmage"></emp:message></font></label>
								  </td>
							  </tr>
							  <tr>
								  <td>&nbsp;&nbsp;&nbsp;<emp:message key="appmage_xxfb_appxxfs_text_yxhms" defVal="有效号码数：" fileName="appmage"></emp:message><span id="effs"></span></td>
								  <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="appmage_xxfb_appxxfs_text_tjhms" defVal="提交号码数：" fileName="appmage"></emp:message><span id="counts"></span></td>								  
								  <td align="center">
								   <div id="showeffinfo" class="div_bg" style="cursor: pointer;">
								       <p style="font-size: 12px;"><emp:message key="appmage_xxfb_appxxfs_text_wxhm" defVal="无效号码" fileName="appmage"></emp:message>(<span id="alleff" style="color: red;"></span>)&nbsp;&nbsp;
								   <a id="arrowhead" style="text-decoration:none"  class="fold">&nbsp;&nbsp;&nbsp;&nbsp;</a></p> 
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
								      <td align="left"><emp:message key="appmage_xxfb_appxxfs_text_hmdhm" defVal="黑名单号码：" fileName="appmage"></emp:message><span id="blacks"></span></td>
								      <td id="preinfonum">
								      	<a href="javascript:uploadbadFiles()" style="color: #2a6fbe"><emp:message key="appmage_xxfb_appxxfs_opt_download" defVal="详情下载" fileName="appmage"></emp:message></a>
								      <input type="hidden" id="badurl" name="badurl"  value=""></input>
								      </td>
							      </tr>
							      <tr>
								      <td align="left"><emp:message key="appmage_xxfb_appxxfs_text_repeatnumber" defVal="重复号码：" fileName="appmage"></emp:message><span id="sames"></span></td>
								      <td></td>
							      </tr>
							      <tr>
							          <td align="left"><emp:message key="appmage_xxfb_appxxfs_text_illegalformat" defVal="格式非法：" fileName="appmage"></emp:message><span id="legers"></span></td>
							          <td></td>
							      </tr>
							    </table>
							    </center>
						    </div>
						    </fieldset>
						    <div style="height: 15px;"></div>	

					    	<div id="footer" style="margin-top: 30px;position: absolute;top:215px;width:480px;">
					    	  <center>
								<input type="button" id="sendSubmit" class="btnClass5 mr23" value="<emp:message key="appmage_common_opt_fasong" defVal="发送" fileName="appmage"></emp:message>">
							    <input id="btcancel" class="btnClass6" type="button" value="<emp:message key="appmage_common_opt_quxiao" defVal="取消" fileName="appmage"></emp:message>" />
								<br/>
							  </center> 
					    	</div>	
						</div>
				</form>
			</div>	
		</div>
		<%--效果展示区--%>
		<div class="effectArea">
			<div class="mobileModel">
				<div class="effectBox">
<%--					<div class="effect_hd" id="subTitle">发送主题</div>--%>
					<div class="effect_bd">
						<div class="tabsContent">
							<div class="effect_dialog" data-tab="txt">
								<div class="avitor">
									<img src="<%=iPath %>/img/avitor.gif" alt="">
								</div>
								<div class="bubbleArea">
									<span class="triangle"></span>
									<span class="triangleContour"></span>
									<div class="bubbleContent" id="bubbleContent">
										<emp:message key="appmage_xxfb_appxxfs_opt_choosewordcontent" defVal="请选择文本内容!" fileName="appmage"></emp:message>
									</div>
								</div>
							</div>
							<div class="effect_dialog" data-tab="media">
								<div class="avitor">
									<img src="<%=iPath %>/img/avitor.gif" alt="">
								</div>
								<div class="bubbleArea">
									<span class="triangle"></span>
									<span class="triangleContour"></span>
									<div class="bubbleContent" id="bubbleMedia">
										<emp:message key="appmage_xxfb_appxxfs_opt_qxzdmtnr" defVal="请选择多媒体内容!" fileName="appmage"></emp:message>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="subArea">
			<input id="preview" class="btnClass5 mr23" type="button" value="<emp:message key="appmage_common_opt_tijiao" defVal="提交" fileName="appmage"></emp:message>" style="margin-left: 590px;""/>
			<input id="sendReset" class="btnClass6" type="button" value="<emp:message key="appmage_common_opt_chongzhi" defVal="重置" fileName="appmage"></emp:message>" onclick="javascript:reloadPage();"/>
			<br/>
		</div>
</div>
		<div class="clear2"></div>
			<div id="infoDiv" title="<emp:message key="appmage_xxfb_appxxfs_opt_selectobject" defVal="选择发送对象" fileName="appmage"></emp:message>" style="padding:5px;display:none">
				<input id="flowNames" type="hidden" name="flowNames" value="" />
				<iframe id="flowFrame" name="flowFrame" src="<%=iPath%>/app_chooseSendInfo.jsp?lguserid=<%=lguserid %>" style="width:525px;height:455px;border: 0;" marginwidth="0" scrolling="no" frameborder="no"></iframe>
				<table style="border:0">
					<tr><td style="width:530px;border:0px" align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button"  value="<emp:message key="appmage_common_opt_queding" defVal="确定" fileName="appmage"></emp:message>" class="btnClass5 mr23" onclick="javascript:doOk()" />
						<input type="button"  value="<emp:message key="appmage_common_opt_quxiao" defVal="取消" fileName="appmage"></emp:message>" class="btnClass6" onclick="javascript:doSelectEClose()" />
					</td>
					</tr>
				</table> 
		</div>
		<div id="probar" style="display: none">
			<p style="padding-top: 8px;">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="appmage_xxfb_appxxfs_text_wait" defVal="处理中,
				请稍等....." fileName="appmage"></emp:message>
			</p>
			<div id="shows">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<img style="padding-left: 25px;"
					src="<%=commonPath%>/common/img/loader.gif" />
			</div>
		</div>
	</div>
	<script>
		var path="<%=path%>";
		var iPath="<%=iPath %>";
		var base_menuCode = "<%=menuCode%>";
		var absolutePath = "<%=absolutePath%>";
		var videoPath="<%=commonPath%>/common/widget/video/";
	</script>
	<script src="<%=commonPath %>/common/js/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/jquery.ui.widget.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/jquery.fileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/video/swfobject.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=iPath %>/js/jquery.tabs.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath %>/common/js/jquery.tipsy.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=iPath %>/js/jquery.InputLetter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
	<script src="<%=iPath %>/js/appPageEdit.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=iPath %>/js/app_msgSend.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script>
		 $(document).ready(function(){
		   var result = "<%=result%>";
			if(result!="-1")
			{
				var messageInfo = "";
				var data = result.split("@@");
				if(data[0] == "000")
				{
					messageInfo = getJsLocaleMessage('appmage','appmage_page_msgSend_sendsuc') ; //"APP消息发送成功！";
				}
				else if(data[0] == "error")
				{
					messageInfo = getJsLocaleMessage('appmage','appmage_page_msgSend_sendfalse') ; //"APP消息发送失败！";
				}
				else
				{
					messageInfo = data[0];
				}
				//短信发送返回状态
				var smsResult = data[1];
				//短信发送
				if(smsResult != "noSendSms")
				{
					messageInfo += "\n";
					if(smsResult.indexOf("empex") == 0)
					{
						//messageInfo += "短信发送任务创建失败："+smsResult.substr(5);
						messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_smscreatefalse') +smsResult.substr(5);
					}else if(smsResult=="uploadFileFail")
					{
						messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_smsfileerror') ; //"短信发送上传号码文件失败，取消任务创建！";
					}else if(smsResult=="createSuccess")
					{ 
						messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_taskcreatesuc') ; //"创建短信任务及提交到审批流成功！";
					}else if(smsResult=="000")
					{
						messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_sendwgsuc') ; //"创建短信任务及发送到网关成功！";
					}else if(smsResult=="error")
					{
						messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_timeout') ; // "短信发送请求响应超时，创建短信任务失败！";
					}else if(smsResult=="nospnumber")
					{
						messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_noweihao') ; //"短信发送失败，SP账号未设置尾号！";
					}else if(smsResult=="depfee:-2")
					{
					    messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_yuebuzu') ; //"短信发送机构余额不足，创建短信任务失败！";
					}else if(smsResult=="depfee:-1")
					{
					    messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_jfsb') ; //"短信发送创建短信任务时，修改计费信息失败！";
					}
					else if(smsResult=="spuserfee:-2")
					{
				    	messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_spyuebuzu') ; //"SP账号余额不足,创建短信任务失败！";
					}
					else if(smsResult=="spuserfee:-1"||smsResult=="spuserfee:-3")
					{
				    	messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_spcostfalse') ; //"创建短信任务时,检查SP账号费用失败！";
					}
					else if(smsResult == "subnoFailed"){
						messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_smsweihaoerror') ; //"短信发送拓展尾号处理失败！";
					}else if("nogwfee"==smsResult || "feefail"==smsResult){
						messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_yysyuebuzu') ; //"短信发送获取运营商余额失败，取消任务创建！";
					}else if(smsResult.indexOf("lessgwfee")==0){
						messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_yysyebz') ; //"短信发送运营商余额不足，取消任务创建！";
					}else
					{
						// messageInfo += "短信发送向网关发送请求失败:"+smsResult;
						messageInfo += getJsLocaleMessage('appmage','appmage_page_msgSend_sendwgfalse') +smsResult;
					}
				}
				alert(messageInfo);
				getCt();
				<%session.removeAttribute("app_sendResult");%>
			}
		 })
		</script>
  </body>
</html>
