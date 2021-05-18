<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.template.LfTemplate"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.vo.LfMaterialVo"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	List<LfMaterialVo> lfMaterialVoList = (List<LfMaterialVo>)request.getAttribute("lfMaterialVoList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mmsTemplate");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String opType =(String) request.getAttribute("opType");
	//String opContent = "edit".equals(opType)?"编辑彩信":"复制彩信";
	String opContent = "edit".equals(opType)?MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_bjcx",request):MessageUtils.extractMessage("ydcx","ydcx_cxyy_mbbj_fzcx",request);
	LfTemplate template =(LfTemplate) request.getAttribute("template");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
		String acc = "true";
		if(StaticValue.getCORPTYPE() ==1 && "false".equals((String)request.getAttribute("mmsacc")))
		{
			acc = "false";
		}
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
		<style type="text/css">
			.tree li button.ico_open{ background:url(<%=commonPath %>/common/widget/zTreeStyle/img/sorts.gif) no-repeat;}
	        .tree li button.ico_close{ background:url(<%=commonPath %>/common/widget/zTreeStyle/img/sorts.gif) no-repeat;}
			.tree li button.ico_docu{ background:url(<%=commonPath %>/common/widget/zTreeStyle/img/sorts.gif) no-repeat;}
		</style>
		<link href="<%=iPath %>/css/editMmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/editMmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/ydcx_applicationMms.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	</head>

	<body id="ydcx_editMmsTemplate">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
	<input type="hidden" id="opType" value="<%=opType %>" />
	<input type="hidden" id="tmId" value="<%=template.getTmid() %>" />
	<input type="hidden" id="hiddenCode" value="<%=template.getTmCode() %>" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode,opContent) %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,opContent) %>

			<%-- header结束 --%>
		<div id="previewTemp" class="ydcx_previewTemp" title="<emp:message key="ydcx_cxyy_mbbj_cxyl" defVal="彩信预览" fileName="ydcx"></emp:message>">		
			<input type="hidden" id="tmmsId" value=""/>
			<div class="ydcx_container_sub2">
				<div id="mobile" class="ydcx_mobile">
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
			
			<div id="getloginUser" class="ydcx_getloginUser">
			</div>
			
			
			<div id="mmsMat" title="<emp:message key="ydcx_cxyy_mbbj_tysc" defVal="调用素材" fileName="ydcx"></emp:message>" style="display:none">
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
							<ul id="dropdownMenu" class="tree"></ul>
						</div>
						</div>
					</td>
					<td>
					<table class="ydcx_tabl1">
					<tr>
						<td class="ydcx_tabl1_td1"><div id="matList" class="ydcx_matList">
						</div><emp:message key="ydcx_cxyy_common_text_6" defVal="预览" fileName="ydcx"></emp:message></td>
					</tr>
					
					<tr>
						<td class="ydcx_tabl1_td2">
							<input id="preSend" type="button" value="<emp:message key="ydcx_cxyy_mbbj_qr" defVal="确认" fileName="ydcx"></emp:message>" class="btnClass5 mr23 left" onclick="javascript:confirmMat();"/>
							
							<input id="qingkong" type="button" value="<emp:message key="ydcx_cxyy_common_btn_16" defVal="取消" fileName="ydcx"></emp:message>" onclick="javascript:$('#mmsMat').dialog('close');" class="btnClass6 left" />
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
								
								<%--<emp:message key="ydcx_cxyy_mbbj_xztp_mh" defVal="选择图片：" fileName="ydcx"></emp:message>
								<input id="chooseImg" name="chooseImg" type="file" style="width: 220px"/>
								<p><label style="color: #2970c0"><emp:message key="ydcx_cxyy_mbbj_tpgsw" defVal="图片格式为：jpg、jpeg、gif" fileName="ydcx"></emp:message></label></p>
								<p style="float: right;padding-top:10px;padding-right:10px;"><input type="button" id="uploadImg" value="<emp:message key="ydcx_cxyy_mbbj_ljsc" defVal="立即上传" fileName="ydcx"></emp:message>" onclick="javascript:doUp('chooseImg')" class="btnClass2"/></p>
						     --%>
						     <p>
						     <span class="ydcx_upImg_span1"><emp:message key="ydcx_cxyy_mbbj_xztp_mh" defVal="选择图片：" fileName="ydcx"></emp:message></span>
						     <span class="ydcx_upImg_span2">
								<input class="ydcx_filepath" type="textfield" id="filepath" readonly>
								<span  class="ydcx_span1">&nbsp;&nbsp;</span>
								<span>
									<span class="ydcx_uploadmusic">
										<emp:message key="ydcx_cxyy_dtcxfs_scwj" defVal="上传文件" fileName="ydcx"></emp:message>
										<input class="ydcx_chooseImg" style="position:absolute;left:0;top:0;width:82px;height:20px;opacity:0;filter:alpha(opacity=0);" type="file" id="chooseImg" name="chooseImg" 
												onchange="document.getElementById('filepath').value=this.value;document.getElementById('filepath').title=this.value;"/>			
									</span>
								</span>
						     </span>
						     <br/>
						     <br/>
						     </p>
						     <p><label class="ydcx_imgtype_labe1"><emp:message key="ydcx_cxyy_mbbj_tpgsw" defVal="图片格式为：jpg、jpeg、gif" fileName="ydcx"></emp:message></label></p>
						     <p class="ydcx_uploadImg_p"><input type="button" id="uploadImg" value="<emp:message key="ydcx_cxyy_mbbj_ljsc" defVal="立即上传" fileName="ydcx"></emp:message>" onclick="javascript:doUp('chooseImg')" class="btnClass2"/></p>
							
							
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
							<%--<emp:message key="ydcx_cxyy_mbbj_xzsy_mh" defVal="选择声音：" fileName="ydcx"></emp:message>
							<input id="chooseMusic" name="chooseMusic" type="file" style="width: 220px"/>
							<p><label style="color: #2970c0"><emp:message key="ydcx_cxyy_mbbj_ypgsw" defVal="音频格式为：mid、midi、amr" fileName="ydcx"></emp:message></label></p>
							<p style="float: right;padding-top:10px;padding-right:10px;">
							<input type="button" id="uploadMusic" value="<emp:message key="ydcx_cxyy_mbbj_ljsc" defVal="立即上传" fileName="ydcx"></emp:message>" onclick="javascript:doUp('chooseMusic')" class="btnClass2"/></p>
							--%>
							<p>
						     <span class="ydcx_chooseMusic_span1"><emp:message key="ydcx_cxyy_mbbj_xzsy_mh" defVal="选择声音：" fileName="ydcx"></emp:message></span>
						     <span class="ydcx_chooseMusic_span2">
								<input class="ydcx_filepathmusic" type="textfield" id="filepathmusic" readonly>
								<span class="ydcx_span3">&nbsp;&nbsp;</span>
								<span>
									<span class="ydcx_uploadmusic">
										<emp:message key="ydcx_cxyy_dtcxfs_scwj" defVal="上传文件" fileName="ydcx"></emp:message>
										<input class="ydcx_chooseMusic" type="file" id="chooseMusic" name="chooseMusic" 
												onchange="document.getElementById('filepathmusic').value=this.value;document.getElementById('filepathmusic').title=this.value;"/>			
									</span>
								</span>
						     </span>
						     <br/>
						     <br/>
						     </p>
						    <p><label class="ydcx_music_label"><emp:message key="ydcx_cxyy_mbbj_ypgsw" defVal="音频格式为：mid、midi、amr" fileName="ydcx"></emp:message></label></p>
							<p class="ydcx_music_p">
							<input type="button" id="uploadMusic" value="<emp:message key="ydcx_cxyy_mbbj_ljsc" defVal="立即上传" fileName="ydcx"></emp:message>" onclick="javascript:doUp('chooseMusic')" class="btnClass2"/></p>
						
						
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
									<%=opContent %>
								</td>
								<td align="right">
									<font class="titletop_font" onclick="javascript:backPage('<%=path %>')">&larr;&nbsp;<emp:message key="ydcx_cxyy_mbbj_fhsyj" defVal="返回上一级" fileName="ydcx"></emp:message></font>
								</td>
							</tr>
						</table>
					</div>
			
			
			
			
					<table class="ydcx_table1">
						<tr class="ydcx_table1_tr1">
							<td>
								<emp:message key="ydcx_cxyy_mbbj_mblx_mh" defVal="模板类型：" fileName="ydcx"></emp:message>
							</td>
							<td colspan="3">
								<input type="radio" name="templateType" value="0" onclick="selectType();"/><label class="ydcx_templateType_static" ><emp:message key="ydcx_cxyy_mbbj_jt" defVal="静态" fileName="ydcx"></emp:message> </label>
								<input type="radio" name="templateType" value="1" onclick="selectType();" style="margin-left: 15px;"/><label class="ydcx_templateType_dynamic"><emp:message key="ydcx_cxyy_mbbj_dt" defVal="动态" fileName="ydcx"></emp:message></label>
							</td>
						</tr>
						<tr>
							<td class="ydcx_table1_tr2_td2">
								<emp:message key="ydcx_cxyy_mbbj_mbmc_mh" defVal="模板名称：" fileName="ydcx"></emp:message></td>
							<td colspan="3" class="ydcx_table1_tr2_td2">
								<input type="text" id="mmsTitle" maxlength="32" class="graytext input_bd ydcx_mmsTitle"/>											
							</td>
						</tr>
						<tr>
						<td class="ydcx_table1_tr2_td2">
								<span class="righttitle"><emp:message key="ydcx_cxyy_mbbj_mbbh_mh" defVal="模板编号：" fileName="ydcx"></emp:message></span></td>
							<td colspan="3"  class="ydcx_table1_tr2_td2">
								<input type="text" id="mmsCode" maxlength="16" class="graytext input_bd ydcx_mmsCode"/>											
							</td>
						</tr>
						<tr>
							<td class="ydcx_table1_tr2_td2" valign="top">
								<emp:message key="ydcx_cxyy_mbbj_mbnr_mh" defVal="模板内容：" fileName="ydcx"></emp:message>								
							</td>
									<td colspan="3" class="ydcx_mmsFrame_td" valign="top"> 
									<table>
									<tr>
										<td>
										<input type="hidden" id="framesize" value="1" />
										<div id="mmsFrame">
											<div class="ydcx_mmsFrame_sub" onclick="toUp();" ></div>
											<div id="mframes" class="ydcx_mframes">
												<div id="1" class="mmsFrame">
													<div class="textFrame">1
													</div>
													<div class="floatDel"><input type="button" class="deleteFrame" onclick="javascript:delFrame(this)"></div>
													<div class="floatFrame">
	            									<input type="button" class="moveUp" onclick="javascript:moveUp(this);" align="left"/>
	            									<input type="button" class="moveDown" align="right" onclick="javascript:moveDown(this)"/>
            										
            										</div>	
            										<center></center>												
												</div>
												
												<div class="ydcx_addFrame" id="addFrame" onclick="javascript:addFrame();"></div>
											</div>
											<div class="ydcx_todown" onclick="toDown();" ></div>
										</div>
										
										</td>
										<td id="rightFrame" class="ydcx_rightFrame" colspan="2" >
											<div id="frame_1">
											  <table width="492px" class="ydcx_frame_1_tabl">
											  <tr>
											  <td colspan="4"  class="ydcx_lasttime_td">
											  <emp:message key="ydcx_cxyy_mbbj_bzcxsj_mh" defVal="本帧持续时间：" fileName="ydcx"></emp:message><input type="text" name="lasttime" id="lasttime" value="10" maxlength="2" size="2"  onkeyup="value=value.replace(/[^\d]/g,'')" 
											  oncontextmenu="window.event.returnvalue=false "/><emp:message key="ydcx_cxyy_mbbj_m" defVal="秒" fileName="ydcx"></emp:message>
											 <emp:message key="ydcx_cxyy_common_text_2" defVal="共" fileName="ydcx"></emp:message><span id="frameTotalSize">0.00</span>KB
											  </td>
											  </tr>
											  <tr height="32px;">
											    <td id="addMat" class="ydcx_addMat">
												<input type="button" class="btnMatel" value="<emp:message key="ydcx_cxyy_mbbj_dysc" defVal="调用素材" fileName="ydcx"></emp:message>" />
												</td>
												<td id="addImg" class="ydcx_addImg">
												<input type="button" class="btnImage" value="<emp:message key="ydcx_cxyy_mbbj_tjtp" defVal="添加图片" fileName="ydcx"></emp:message>"/>
												</td>
												<td id="addMusic" class="ydcx_addMusic">
												<input type="button" class="btnMusic" value="<emp:message key="ydcx_cxyy_mbbj_tjsy" defVal="添加声音" fileName="ydcx"></emp:message>"/>
												</td>
												<td rowspan="4" class="ydcx_preview_td">
													  <div id="preview" class="ydcx_preview">
													  </div>
												  </td>
												  
												</tr>
												<tr>
												<td colspan="2" class="ydcx_delImg_td">
												<div style="display: none" id="delImg" class="div_bg">
												 <input type="hidden" id="ImgUrl" value="" />
												<table><tr><td><a class="hasImage" style="text-decoration:none;">&nbsp;&nbsp;</a></td>
												 <td><label id="isize">0.00</label>KB
												 <a class="ydcx_delatt" onclick="delatt('ImgUrl')"><emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message></a></td>
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
												<emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message></a></td></tr></table>
												</div>
												</td>
												</tr>
												<tr>
												  <td colspan="4"  class="ydcx_paramTxt_td">
												  <p id="paramTxt" class="ydcx_paramTxt"><font color="#7f7f7f"><emp:message key="ydcx_cxyy_mbbj_csgsw" defVal="参数格式为：“{#参数1#}”(如：我和{#参数1#}去{#参数2#})" fileName="ydcx"></emp:message></font></p>
												  </td>
												  </tr>
												  <tr>
												  <td colspan="4" class="ydcx_textContent_td">
												  <textarea name="textContent" id="textContent" class="ydcx_textContent" ></textarea></td>
												</tr>
											  </table>
											</div>
										</td>
									</tr>
									</table>											
									</td>
						</tr>
						<tr>
						   <td style="padding-top: 5px;"></td>
						   <td>  
						   	<emp:message key="ydcx_cxyy_common_text_2" defVal="共" fileName="ydcx"></emp:message><span id="totalSize">0.00</span><emp:message key="ydcx_cxyy_mbbj_sctmswjh" defVal="KB/80KB	，生成tms文件后会略大于当前值" fileName="ydcx"></emp:message>
						   </td>
						   
						</tr>
						<tr>
						   <td class="ydcx_templstate_td">
								<emp:message key="ydcx_cxyy_mbbj_mbzt_mh" defVal="模板状态：" fileName="ydcx"></emp:message></td>
								<td colspan="3" class="ydcx_state_td">
								<input type="radio" id="state" name="state" value="1" /><emp:message key="ydcx_cxyy_mbbj_wc" defVal="完成" fileName="ydcx"></emp:message>
								<input type="radio" id="state" name="state" value="2" checked="checked" class="ydcx_state_draft" /><emp:message key="ydcx_cxyy_mbbj_cg" defVal="草稿" fileName="ydcx"></emp:message>
								
								</td>
						</tr>
						</table>
						<div id="footer" class="ydcx_footer">
						        <input type="button" class="btnClass6 mr23 left" id="mscan" value="<emp:message key="ydcx_cxyy_common_text_6" defVal="预览" fileName="ydcx"></emp:message>"/> 
								<input type="button" id="save" value="<emp:message key="ydcx_cxyy_mbbj_tj" defVal="提交" fileName="ydcx"></emp:message>" onclick="javascript:doSubmit()" class="btnClass5 mr23 left"/>
								<input type="button" id="cancel" onclick="javascript:backPage('<%=path %>')" value="<emp:message key="ydcx_cxyy_common_btn_10" defVal="返回" fileName="ydcx"></emp:message>" class="btnClass6 left" />
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datePicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=iPath%>/js/addMMS.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=iPath%>/js/editMmsTpl.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		<script type="text/javascript">
	
	function init(){
		$("input[name='templateType'][value='<%=template.getDsflag() %>']").attr("checked","checked");
		if('<%=template.getDsflag() %>' == 0){
			$("#paramTxt").hide();
		}else{
			$("#paramTxt").show();
		}
		$("#mmsTitle").val("<%=template.getTmName() %>");
		$("#mmsCode").val("<%=template.getTmCode() %>");
	    var msg = "<%=template.getTmMsg() %>";
	    var lgcorpcode = "<%=template.getCorpCode() %>";
		$.post("<%=path%>/tem_mmsTemplate.htm?method=edit",{tmUrl:msg,lgcorpcode:lgcorpcode},function(result){
		if (result != null && result !="null" && result != ""){
			var array = eval("("+result+")");
			var data;
			var ImgUrl;
			var MusUrl;
			var isize;
			var msize;
			var allsize="0.00";
			var textContent;
			var pathUrl = $("#pathUrl").val();
			for(var i = 0 ;i<array.length;i++){
				data = array[i];
				ImgUrl = data.ImgUrl?data.ImgUrl:"";
				MusUrl = data.MusUrl?data.MusUrl:"";
				isize = data.isize?new Number(parseFloat(data.isize)/1024).toFixed(2):"0.00";
				msize = data.msize?new Number(parseFloat(data.msize)/1024).toFixed(2):"0.00";
				textContent = data.textContent?data.textContent:"";
				allsize = parseFloat(allsize)+parseFloat(isize)+parseFloat(msize);
				var img = "";
				if(ImgUrl != ""){
					img = $.trim($("#preview").html())+"";  //预览模板中的彩信图片	
					var temp = $("#preview").children("center").children("img").attr("src")+"";			//获取服务器上的url
					temp = temp.substring(1,temp.length);
					img = temp.substring(temp.indexOf("/")+1);
				}
				var w = parseFloat(data.width);						//宽
				var h = parseFloat(data.height);						//高
				var padding = parseFloat("0");
				if (w-220>=0){
			    	w = w*(210/w);
			    	h = h*(210/h);
			    }
			    if (h-160>=0){
			    	w = w*(150/h);
			    	h = h*(150/h);
			    }
			    padding = (160-h)/2;
			    // 反斜杠转义
				textContent =textContent.replace(/\\/g,"\\\\");
				//英文双引号转义
			    textContent = textContent.replace(/"/g,"\\\"");
				style="width:"+w+"px;height:"+h+"px;margin-top:"+padding+"px;";
				content  = '{\"img\":\"'+img+'\",\"style":\"'+style+'\",\"ImgUrl\":\"'+ImgUrl+'\",\"MusUrl\":\"'+MusUrl+'\",\"isize\":\"'
				+isize+'\",\"msize\":\"'+msize+'\",\"lasttime\":\"'+data.lasttime+'\",\"textContent\":\"'+textContent+'\"}';
				arr[i] = content;	
				arrs[i] = parseFloat(isize)+parseFloat(msize);
				if(i==0){
					if (ImgUrl != ""){
				    	setImg(ImgUrl,data.width,data.height,data.isize);			//将该值在页面中设置出该图片文件的属性
			    	}
				    if (textContent != "")
				    {
				    	var ts=content.replace(/\r\n/g,"<brrn/>");   
				    	ts=ts.replace(/\n/g,"<brn/>");
						var newContent =  $.parseJSON(ts);//eval('(' + ts + ')');
						var nr = newContent.textContent;
						nr=nr.replaceAll("<brrn/>","\r\n");   
					    nr=nr.replaceAll("<brn/>","\n");
					    
					    //将 {#参数 支持多语言显示
						<%if(StaticValue.ZH_HK.equals(langName)){%>
							nr=nr.replaceAll("{#参数","{#Param");
						<%}else if(StaticValue.ZH_TW.equals(langName)){%>
							nr=nr.replaceAll("{#参数","{#參數");
						<%}%>
						
				    	$("#textContent").val(nr);
					}
					if (MusUrl != ""){
				    	setMus(MusUrl,msize);
				    }
				    $("#lasttime").val(data.lasttime);
				    $("#frameTotalSize").html(parseFloat(arrs[0]).toFixed(2));
				}else{
					var frameid = $("#framesize").attr("value");
					var id = $(".mmsFrame").attr("id")==null?"1":$(".mmsFrame").attr("id");				//获取当前选中的贞   默认是 第一个贞
			       
			        var delFrame = '<div class="floatDel"><input type="button" class="deleteFrame" onclick="javascript:delFrame(this)"></div>';
			    	var floatDiv = '<div class="floatFrame">'+
			            '<input type="button" width="20px" class="moveUp" onclick="javascript:moveUp(this);" align="left"/>'+
			            '<input type="button" width="20px" class="moveDown" align="right" onclick="javascript:moveDown(this)"/></div>';
			        var textDiv = "<div class='textFrame'>"+(parseInt(frameid)+1)+"</div>";
			        var div = '<div id="'+(parseInt(frameid)+1)+'" class="mmsFrames">'+
			        textDiv+delFrame+floatDiv+'<center></center></div>';	
			        $("#"+frameid).after($.trim(div));		
			        if((parseInt(frameid)+1) == 4){
			        	$("#addFrame").css("display","none");
			        }else if((parseInt(frameid)+1) > 4){
			        	$("#"+(parseInt(frameid)+1)).css("display","none");
			        }											//将当前的贞加入到大模板中								
			        //$("#curf").empty();
			    	//$("#curf").html("当前"+$.trim(id)+"/"+$.trim((parseInt(frameid)+1)+""));
			    	$("#framesize").attr("value",(parseInt(frameid)+1));	
					if (ImgUrl != ""){
						var w2 = parseFloat(data.width);						//宽
						var h2 = parseFloat(data.height);	
						if (w2-50>=0){
					    	w2 = w2*(47/w2);
					    	h2 = h2*(47/h2);
					    }
					    if (h2-50>=0){
					    	w2 = w2*(47/h2);
					    	h2 = h2*(47/h2);
					    }
					    var padding2 =  (50-h2)/2;
						$("#"+parseInt(parseInt(i)+1)+" center").html("<img src=\'"+pathUrl+"/"+ImgUrl+"\' style=\'width:"+w2+"px;height:"+h2+"px;margin-top:"+padding2+"px;\' />");
					}
			    	$('#'+(parseInt(frameid)+1)).bind('click', function() {								//为该元素绑定一个CLICK事件
			    		fClick($(this).attr("id"));
					});
					$('#'+(parseInt(frameid)+1)).hover(function() {
			    		$(this).children("div .floatDel")[0].style.display="inline-block";
			    		$(this).children("div .floatFrame")[0].style.display="inline-block";
					},function(){
						$(this).children("div .floatDel")[0].style.display="none";
						$(this).children("div .floatFrame")[0].style.display="none";
					});
				}
			}
			//获取所有图片 的最大KB量
			$("#totalSize").html(parseFloat(allsize).toFixed(2));
		}else{
             //alert("内容文件不存在，无法预览！");
             alert(getJsLocaleMessage("ydcx","ydcx_cxyy_mbbj_nrwjbcz"));
		}
	});
	}
	
	
        </script>
	</body>
</html>