<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.servmodule.ydcx.constant.ServerInof"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mmsTemplate");
	menuCode = menuCode==null?"0-0-0":menuCode;
	//获得信息回显
	String skin = session.getAttribute("stlyeSkin")==null?"default": (String)session.getAttribute("stlyeSkin");
	String acc = "true";
	if(StaticValue.getCORPTYPE() ==1 && "false".equals((String)request.getAttribute("mmsacc"))) {
		acc = "false";
	}
	//1静态彩信  2动态彩信  3新增
	String pathtype = request.getAttribute("type").toString();
	//服务器名称
	String serverName = ServerInof.getServerName();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=iPath%>/css/mmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/mmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link href="<%=iPath%>/css/addmmstemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/addmmstemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/ydcx_applicationMms.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<style type="text/css">
		    .table01 td{padding:6px 0;}
		    .titletop{padding-top:0;}
			.tree li button.ico_open{ background:url(<%=commonPath %>/common/widget/zTreeStyle/img/sorts.gif) no-repeat;}
	        .tree li button.ico_close{ background:url(<%=commonPath %>/common/widget/zTreeStyle/img/sorts.gif) no-repeat;}
			.tree li button.ico_docu{ background:url(<%=commonPath %>/common/widget/zTreeStyle/img/sorts.gif) no-repeat;}
		</style>
	</head>

	<body id="ydcx_addMmsTemplate">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
	<input type="hidden" id="pathtype" value="<%=pathtype%>"/>
	<input type="hidden" id="opType" value="add" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode,"新建模板") %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_xjmb",request)) %>
			<%-- header结束 --%>
			<div id="previewTemp" title="<emp:message key="ydcx_cxyy_mbbj_cxyl" defVal="彩信预览" fileName="ydcx"/>" style="display:none;overflow: auto;">		
			<input type="hidden" id="tmmsId" value=""/>
			<div class="ydcx_container_sub2">
				<div id="mobile" class="ydcx_mobile">
				<center>				
				<div id="pers" class="ydcx_pers">
				<div id="showtime" class="ydcx_showtime" ></div>
				<div id='chart' class="ydcx_chart" >
				</div>
				</div>
				</center>				
				<div id="screen" class="ydcx_screen">
				</div>				
                <center>
				<table>
				<tr>
				  <td>
				     <label id="pointer" class="ydcx_pointer" style="vertical-align: bottom"></label>
				     <label id="nextpage" class="ydcx_nextpage" style="vertical-align: bottom"></label>
				  </td>
				</tr>
				<tr align="center">
					<td>
					   <label id="currentpage" class="ydcx_currentpage" style="vertical-align: bottom"></label>
					</td>
				</tr>
				</table>				
				</center>
				</div>
			</div>
			<div id="inputParamCnt1" class="ydcx_inputParamCnt1">
			</div>			
		</div>
			<div id="getloginUser" class="ydcx_getloginUser"></div>
			<div id="mmsMat" title="<emp:message key="ydcx_cxyy_mbbj_tysc" defVal="调用素材" fileName="ydcx"/>" style="display:none">
			<input type="hidden" name="address" id="address"/>
			<input type="hidden" name="size" id="size"/>
			<input type="hidden" name="height" id="height"/>
			<input type="hidden" name="width" id="width"/>
			<input type="hidden" name="type" id="type"/>
			<input type = "hidden" name ="mmsacc" id="mmsacc" value="<%=acc %>"/>
			<input type = "hidden" name ="fileServerUrl" id="fileServerUrl" value="<%=StaticValue.getFileServerViewurl()%>"/>
			<input type = "hidden" name ="skinType" id="skinType" value="<%=skin %>"/>
			<table style="width: 100%;">
				<tr>
					<td>
						<div class="ydcx_dv1">
						<div id="dropMenu" class="ydcx_dropMenu">
							<ul id="materialTree" class="tree"></ul>
						</div>
						</div>
					</td>
					<td>
					<table class="ydcx_tabl1">
					<tr>
						<td class="ydcx_tabl1_td1"><div id="matList" class="ydcx_matList">
						</div><emp:message key="ydcx_cxyy_common_text_6" defVal="预览" fileName="ydcx"/></td>
					</tr>
					
					<tr>
						<td class="ydcx_tabl1_td2">
							<input id="preSend" type="button" value="<emp:message key="ydcx_cxyy_mbbj_qr" defVal="确认" fileName="ydcx"/>" class="btnClass5 mr23 left" onclick="confirmMatrial();"/>
							<input id="qingkong" type="button" value="<emp:message key="ydcx_cxyy_common_btn_16" defVal="取消" fileName="ydcx"/>" onclick="$('#mmsMat').dialog('close');" class="btnClass6 left" />
						</td>
					</tr>
					</table>
					</td>
				</tr>
			</table>
			</div>

				<div id="model" class="model" style="display: none;">
						<div class="main">
						<br/>
						<div id="upImg">

							<table class="table01">
						   <tr>
						     <td width="105" align="right"><emp:message key="ydcx_cxyy_mbbj_xztp_mh" defVal="选择图片：" fileName="ydcx"/></td>
						     <td class="ydcx_table01_td1">
						     <%--<input id="chooseImg" name="chooseImg" type="file" style="width:220px;"/>
						     --%>
								<input class="ydcx_filepath" type="textfield" id="filepath" readonly>
								<span class="ydcx_span1">&nbsp;&nbsp;</span>
								<span>
									<span class="ydcx_uploadtms">
										<emp:message key="ydcx_cxyy_dtcxfs_scwj" defVal="上传文件" fileName="ydcx"/>
										<input class="ydcx_chooseImg" type="file" id="chooseImg" name="chooseImg"
												onchange="document.getElementById('filepath').value=this.value;document.getElementById('filepath').title=this.value;"/>
									</span>
								</span>
						     </td>
						   </tr>
						   <tr>
						     <td></td>
						     <td><label class="ydcx_imgtype_labe1"><emp:message key="ydcx_cxyy_mbbj_tpgsw" defVal="图片格式为：jpg、jpeg、gif" fileName="ydcx"/></label></td>
						   </tr>
						  <tr>
						     <td></td>
						     <td><input type="button" id="uploadImg" value="<emp:message key="ydcx_cxyy_mbbj_ljsc" defVal="立即上传" fileName="ydcx"/>" onclick="doUp('chooseImg')" class="btnClass2"/></td>
						   </tr>
						  </table>
							</div>
						</div>
						<div class="foot">
							<div class="foot-right"></div>
						</div>
				</div>
				<div id="model2" class="model" style="display: none;">
					<div class="main">
					<br/>
						<div id="upMus">
						  <table class="table01">
						   <tr>
						     <td width="105" align="right"><emp:message key="ydcx_cxyy_mbbj_xzsy_mh" defVal="选择声音：" fileName="ydcx"/></td>
						     <td>
						     <%--<input id="chooseMusic" name="chooseMusic" type="file"  style="width:220px;"/>--%>
								<input class="ydcx_filepath2" type="textfield" id="filepath2" readonly>
								<span style="width:2px;float:left;">&nbsp;&nbsp;</span>
								<span>
									<span class="ydcx_uploadmusic">
										<emp:message key="ydcx_cxyy_dtcxfs_scwj" defVal="上传文件" fileName="ydcx"/>
										<input class="ydcx_chooseMusic" type="file" id="chooseMusic" name="chooseMusic"
												onchange="document.getElementById('filepath2').value=this.value;document.getElementById('filepath2').title=this.value;"/>
									</span>
								</span>
						     </td>
						   </tr>
						   <tr>
						     <td></td>
						     <td><label class="ydcx_music_label"><emp:message key="ydcx_cxyy_mbbj_ypgsw" defVal="音频格式为：mid、midi、amr" fileName="ydcx"/></label></td>
						   </tr>
						  <tr>
						     <td></td>
						     <td><input type="button" id="uploadMusic" value="<emp:message key="ydcx_cxyy_mbbj_ljsc" defVal="立即上传" fileName="ydcx"/>" onclick="doUp('chooseMusic')" class="btnClass2"/></td>
						   </tr>
						  </table>
						</div>
						<div id="curMus" style="display:none">															</div>
					</div>
					<div class="foot">
						<div class="foot-right"></div>
					</div>
				</div>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent ydcx_rContent">
					<div class="titletop ydcx_titletop">
						<table class="titletop_table ydcx_titletop_table">
							<tr>
								<td class="titletop_td" align="left">
									<emp:message key="ydcx_cxyy_mbbj_xjmb" defVal="新建模板" fileName="ydcx"/>
								</td>
								<td align="right">
									<font class="titletop_font" onclick="backPage('<%=path %>','<%=pathtype %>')">&larr;&nbsp;<emp:message key="ydcx_cxyy_mbbj_fhsyj" defVal="返回上一级" fileName="ydcx"/></font>
								</td>
							</tr>
						</table>
					</div>
					<table class="ydcx_table1">
						<tr class="ydcx_table1_tr1">
							<td >
								<span class="righttitle"><emp:message key="ydcx_cxyy_mbbj_mblx_mh" defVal="模板类型：" fileName="ydcx"/></span></td>
								<td colspan="3">
								<input type="radio" name="templateType" id="tempType" value="0" checked="checked" onclick="selectType();"/><label class="ydcx_templateType_static"><emp:message key="ydcx_cxyy_mbbj_jt" defVal="静态" fileName="ydcx"/> </label>
								<input type="radio" name="templateType" id="tempType1" value="1" style="margin-left: 15px;" onclick="selectType();"/><label class="ydcx_templateType_dynamic"><emp:message key="ydcx_cxyy_mbbj_dt" defVal="动态" fileName="ydcx"/></label>
								</td>
						</tr>
						<tr >
							<td class="ydcx_table1_tr2_td2">
								<span class="righttitle"><emp:message key="ydcx_cxyy_mbbj_mbmc_mh" defVal="模板名称：" fileName="ydcx"/></span></td>
							<td colspan="3" class="ydcx_table1_tr2_td2">
								<input type="text" id="mmsTitle" maxlength="32" class="graytext input_bd ydcx_mmsTitle"/>
							</td>
						</tr>
						<tr >
						<td class="ydcx_table1_tr2_td2">
								<span class="righttitle"><emp:message key="ydcx_cxyy_mbbj_mbbh_mh" defVal="模板编号：" fileName="ydcx"/></span></td>
							<td colspan="3" class="ydcx_table1_tr2_td2">
								<input type="text" id="mmsCode" maxlength="16" class="graytext input_bd ydcx_mmsCode"/>
							</td>
						</tr>
						<tr>
							<td class="ydcx_table1_tr2_td2" valign="top">
								<span class="righttitle"><emp:message key="ydcx_cxyy_mbbj_mbnr_mh" defVal="模板内容：" fileName="ydcx"/></span>
							</td>
									<td colspan="3" class="ydcx_mmsFrame_td">
									<table>
									<tr>
										<td>
										<input type="hidden" id="framesize" value="1" />
										<div id="mmsFrame">
											<div class="ydcx_mmsFrame_sub"onclick="toUp();" ></div>
											<div id="mframes" class="ydcx_mframes">
												<div id="1" class="mmsFrame">
													<div class="textFrame">1
													</div>
													<div class="floatDel"><input type="button" class="deleteFrame" onclick="delFrame(this)"></div>
													<div class="floatFrame">
	            									<input type="button" class="moveUp" onclick="moveUp(this);" align="left"/>
	            									<input type="button" class="moveDown" align="right" onclick="moveDown(this)"/>

            										</div>
            										<center></center>
												</div>
												<div class="ydcx_addFrame" id="addFrame" onclick="addFrame();"></div>
											</div>
											<div class="ydcx_todown" onclick="toDown();" ></div>
										</div>

										</td>
										<td id="rightFrame" class="ydcx_rightFrame" colspan="2" >
											<div id="frame_1">
											  <table class="ydcx_frame_1_tabl">
											  <tr>
											  <td colspan="4" class="ydcx_lasttime_td" >
											<emp:message key="ydcx_cxyy_mbbj_bzcxsj_mh" defVal="本帧持续时间：" fileName="ydcx"/><input type="text" name="lasttime" id="lasttime" value="10" maxlength="2" size="2"  onkeyup="value=value.replace(/[^\d]/g,'')"
											  oncontextmenu="window.event.returnvalue=false "/><emp:message key="ydcx_cxyy_mbbj_m" defVal="秒" fileName="ydcx"/>
											  <emp:message key="ydcx_cxyy_common_text_2" defVal="共" fileName="ydcx"/><span id="frameTotalSize">0.00</span>KB
											  </td>
											  </tr>
											  <tr height="32px;">
											    <td id="addMat" class="ydcx_addMat">
												<%if(StaticValue.ZH_HK.equals(langName)){%>
												<input type="button" class="btnMatel ydcx_btnMatel" value="<emp:message key="ydcx_cxyy_mbbj_dysc" defVal="调用素材" fileName="ydcx"/>" />
												<%}else{%>
												<input type="button" class="btnMatel" value="<emp:message key="ydcx_cxyy_mbbj_dysc" defVal="调用素材" fileName="ydcx"/>" />
												<%}%>
												</td>
												<td id="addImg" class="ydcx_addImg">
												<%if(StaticValue.ZH_HK.equals(langName)){%>
												<input  type="button" class="btnImage ydcx_btnImage" value="<emp:message key="ydcx_cxyy_mbbj_tjtp" defVal="添加图片" fileName="ydcx"/>"/>
												<%}else{%>
												<input type="button" class="btnImage" value="<emp:message key="ydcx_cxyy_mbbj_tjtp" defVal="添加图片" fileName="ydcx"/>"/>
												<%}%>
												</td>
												<td id="addMusic" class="ydcx_addMusic">
												<%if(StaticValue.ZH_HK.equals(langName)){%>
												<input type="button" class="btnMusic ydcx_btnMusic" value="<emp:message key="ydcx_cxyy_mbbj_tjsy" defVal="添加声音" fileName="ydcx"/>"/>
												<%}else{%>
												<input type="button" class="btnMusic" value="<emp:message key="ydcx_cxyy_mbbj_tjsy" defVal="添加声音" fileName="ydcx"/>"/>
												<%}%>
												</td>
												<td rowspan="4" class="ydcx_preview_td">
													  <div id="preview" class="ydcx_preview"></div>
												  </td>

												</tr>
												<tr >
												<td colspan="2" class="ydcx_delImg_td">
												<div style="display: none;" id="delImg" class="div_bg">
												 <input type="hidden" id="ImgUrl" value="" />
												 <table><tr><td><a class="hasImage" style="text-decoration:none;">&nbsp;&nbsp;</a></td>
												 <td><label id="isize">0.00</label>KB
												 <a class="ydcx_delatt" onclick="delatt('ImgUrl')"><emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"/></a></td>
												 </tr></table>
												</div>
												</td>
												</tr>
												<tr>
												<td colspan="2" class="ydcx_delMus_td">
												<div style="display: none" id="delMus" class="div_bg">
												<input type="hidden" id="MusUrl" value="" />
												<table><tr><td><a class="hasMusic" style="text-decoration:none;">&nbsp;&nbsp;</a></td>
												<td> <label id="msize">0.00</label>KB<a class="ydcx_delMus_a" onclick="delatt('MusUrl')">
												<emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"/></a></td></tr></table>
												</div>
												</td>
												</tr>
												<tr>
													  <td colspan="4" class="ydcx_paramTxt_td">
													  &nbsp;
													  <p id="paramTxt" class="ydcx_paramTxt">
													 	 <font color="#7f7f7f"><emp:message key="ydcx_cxyy_mbbj_csgsw" defVal="参数格式为：“{#参数1#}”(如：我和{#参数1#}去{#参数2#})" fileName="ydcx"/></font>
													  </p>
													  </td>
												 </tr>
												 <tr>
													  <td colspan="4" class="ydcx_textContent_td">
													 	 <textarea name="textContent" id="textContent" class="ydcx_textContent"></textarea>
													  </td>
												 </tr>
											  </table>
											</div>
										</td>
									</tr>
									<tr height="15px;">
									   <td colspan="2">  <emp:message key="ydcx_cxyy_common_text_2" defVal="共" fileName="ydcx"/><span id="totalSize">0.00</span><emp:message key="ydcx_cxyy_mbbj_sctmswjh" defVal="KB/80KB，生成tms文件后会略大于当前值" fileName="ydcx"/></td>
									</tr>
									</table>
									</td>
						</tr>

						<tr>
						    <td class="ydcx_templstate_td">
								<span class="righttitle"><emp:message key="ydcx_cxyy_mbbj_mbzt_mh" defVal="模板状态：" fileName="ydcx"/></span>
							</td>
							<td colspan="3" class="ydcx_state_td">
								<input type="radio" id="state" name="state" value="1" checked="checked" /><emp:message key="ydcx_cxyy_mbbj_wc" defVal="完成" fileName="ydcx"/>
								<input type="radio" id="state" name="state" value="2" class="ydcx_state_draft" /><emp:message key="ydcx_cxyy_mbbj_cg" defVal="草稿" fileName="ydcx"/>
							</td>
						</tr>
						</table>
						<div id="footer" class="ydcx_footer">
						        <input type="button" class="btnClass6 mr23 left" id="mscan" value="<emp:message key="ydcx_cxyy_common_text_6" defVal="预览" fileName="ydcx"/>"/>
								<input type="button" id="save" value="<emp:message key="ydcx_cxyy_mbbj_tj" defVal="提交" fileName="ydcx"/>" onclick="doSubmit()" class="btnClass5 mr23 left" />
								<input type="button" id="cancel" onclick="backPage('<%=path %>','<%=pathtype %>')" value="<emp:message key="ydcx_cxyy_common_btn_10" defVal="返回" fileName="ydcx"/>" class="btnClass6 left"/>
						</div>
			</div>
			<%-- 内容结束 --%>
			<div class="clear"></div>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<script>
			var serverName = "<%=serverName%>";
		</script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=iPath%>/js/addMMS.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script src="<%=iPath%>/js/addMmsTpl.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		<script type="text/javascript">
		function backPage(path,pathtype){
			var lgguid = $("#lgguid").val();
			var lguserid= $('#lguserid').val();
			var lgcorpcode= $('#lgcorpcode').val();
			<%
				String frame = (String) session.getAttribute(StaticValue.EMP_WEB_FRAME);
				if("frame3.0".equals(frame)){
					%>
						window.location.href = path+'/tem_mmsTemplate.htm?method=find&lgguid='+lgguid;
					<%
				}else{
			%>
				//1静态彩信  2动态彩信  3新增
				if(pathtype == "1"){
					//window.location.href = path+'/smm_sameMms.htm?method=find&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode;
					window.parent.openNewTab("1100-1000",path+'/smm_sameMms.htm?method=find&lguserid='+lguserid+'&lgcorpcode='+lgcorpcode);
				}else if(pathtype == "2"){
					//window.location.href = path+'/dmm_sendDynMMS.htm?method=find&lgguid='+lgguid;
					window.parent.openNewTab("1100-1100",path+'/dmm_sendDynMMS.htm?method=find&lgguid='+lgguid);
				}else{
					window.location.href = path+'/tem_mmsTemplate.htm?method=find&lgguid='+lgguid;
				}
			<%
				}
			%>
		}
        </script>
	</body>
</html>
