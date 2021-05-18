<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.vo.LfMaterialVo"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
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
	//获得信息回显
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String acc = "true";
	if(StaticValue.getCORPTYPE() ==1 && "false".equals((String)request.getAttribute("mmsacc")))
	{
		acc = "false";
	}
	String templateType = (String)request.getAttribute("templateType");
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
		<link href="<%=commonPath%>/common/css/mmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
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
	</head>
	<body>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
	<input type="hidden" id="tmpltype" value="<%=templateType %>"/>
	<input type="hidden" id="opType" value="add" />
		<div id="container" class="container">
			<%-- header结束 --%>
			<div id="previewTemp" title="<emp:message key="ydcx_cxyy_dtcxfs_dxyl" defVal="彩信预览" fileName="ydcx"></emp:message>" style="display:none;overflow: auto;">		
			<input type="hidden" id="tmmsId" value=""/>
			<div style="float: left;width:250px;margin-left: 30px;display: inline;">
				<div id="mobile" style="width:240px;height:470px;position: absolute;background: url('<%=commonPath %>/common/img/iphone5.jpg') no-repeat">
				<center>				
				<div id="pers" style='position:relative;margin-top:90px;border:1px #09F solid;text-align:left;display:none;width:180px;height:13px;background-color:#FFFFFF'>
				
				<div id="showtime" style="position:absolute;width:140px;height:13px;text-align: center;"></div>
				
				<div id='chart' style='position:absolute;left:0px; background-color:#00ffdd;width:0px;height:13px;text-align:right;padding-top:0px;'>
				</div>
				</div>
				</center>				
				<div id="screen" style="position:relative;width:189px;height:240px;background:#ffffff;margin-left:23px;margin-top:95px;overflow: auto;padding:2px;word-break: break-all;">
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
			<div id="inputParamCnt1" style="margin-top: 10px;margin-left: 280px; ">
			</div>			
		</div>
			
			<div id="getloginUser" style="padding:5px;display:none;">
					</div>
			<div id="mmsMat" title="<emp:message key="ydcx_cxyy_dtcxfs_dysc" defVal="调用素材" fileName="ydcx"></emp:message>" style="display:none">
			<input type="hidden" name="address" id="address"/>
			<input type="hidden" name="size" id="size"/>
			<input type="hidden" name="height" id="height"/>
			<input type="hidden" name="width" id="width"/>
			<input type="hidden" name="type" id="type"/>
			<input type = "hidden" name ="mmsacc" id="mmsacc" value="<%=acc %>"/>
			<table style="width: 100%;">
				<tr>
					<td>
						<div style="height:80%;margin-left:6px;margin-top: 3px;">
						<div id="dropMenu" style="height:330px; width:200px; background-color:white;border:1px solid;border-color:#eff1f3;overflow-y:scroll;overflow-x:auto;z-index:999;">
							<ul id="materialTree" class="tree"></ul>
						</div>
						</div>
					</td>
					<td>
					<table  style="width: 100%;height: 330px;">
					<tr>
						<td style="vertical-align: top;"><div id="matList" Style="height:150px;width:200px;margin-top: 0px;margin-right:5px;border:1px solid;border-color:#eff1f3;background:#d5d6d7;">
						</div><emp:message key="ydcx_cxyy_common_text_6" defVal="预览" fileName="ydcx"></emp:message></td>
					</tr>
					
					<tr>
						<td  style="vertical-align: bottom;">
							<input id="preSend" type="button" value="<emp:message key="ydcx_cxyy_dtcxfs_qr" defVal="确认" fileName="ydcx"></emp:message>" class="btnClass5 mr23" onclick="javascript:confirmMat();"/>
							
							<input id="qingkong" type="button" value="<emp:message key="ydcx_cxyy_common_btn_16" defVal="取消" fileName="ydcx"></emp:message>" onclick="javascript:$('#mmsMat').dialog('close');" class="btnClass6" />
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
						  <table>
						   <tr>
						     <td><emp:message key="ydcx_cxyy_dtcxfs_xztp_mh" defVal="选择图片：" fileName="ydcx"></emp:message></td>
						     <td><input id="chooseImg" name="chooseImg" type="file"/></td>
						   </tr>
						   <tr>
						     <td></td>
						     <td><label style="color: #2970c0"><emp:message key="ydcx_cxyy_dtcxfs_tpgsw" defVal="图片格式为：jpg、jpeg、gif" fileName="ydcx"></emp:message></label></td>
						   </tr>
						  <tr>
						     <td></td>
						     <td><input type="button" id="uploadImg" value="<emp:message key="ydcx_cxyy_dtcxfs_ljsc" defVal="立即上传" fileName="ydcx"></emp:message>" onclick="javascript:doUp('chooseImg')" class="btnClass2"/></td>
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
							<emp:message key="ydcx_cxyy_dtcxfs_xzsy_mh" defVal="选择声音：" fileName="ydcx"></emp:message><input id="chooseMusic" name="chooseMusic" type="file" />
							<p><label style="color: #2970c0"><emp:message key="ydcx_cxyy_dtcxfs_ypgsw" defVal="音频格式为：mid、midi、amr" fileName="ydcx"></emp:message></label></p>
							<p style="float: right;padding-top:10px;padding-right:10px;">
							<input type="button" id="uploadMusic" value="<emp:message key="ydcx_cxyy_dtcxfs_ljsc" defVal="立即上传" fileName="ydcx"></emp:message>" onclick="javascript:doUp('chooseMusic')" class="btnClass2"/></p>
						</div>
						<div id="curMus" style="display:none">															</div>
					</div>
					<div class="foot">
						<div class="foot-right"></div>
					</div>
				</div>
				
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" style="margin-left:36px;margin-top: 16px;">
					<table style="text-align:left; width: 700px; height: 420px;border: 0;">
						<tr style="width: 80px">
							<td >
								<span class="righttitle"><emp:message key="ydcx_cxyy_dtcxfs_mblx_mh" defVal="模板类型：" fileName="ydcx"></emp:message></span></td>
								<td colspan="3">
								<%
								if("0".equals(templateType)){
								%>
								<input type="radio" name="templateType" value="0" checked="checked" /><label style="margin-left: 5px;"><emp:message key="ydcx_cxyy_dtcxfs_jt" defVal="静态" fileName="ydcx"></emp:message> </label>
								<%}else{ %>
								<input type="radio" name="templateType" value="1" checked="checked"/><label style="margin-left: 5px;"><emp:message key="ydcx_cxyy_dtcxfs_dt" defVal="动态" fileName="ydcx"></emp:message></label>
								<%}
								 %>
							
								</td>
						</tr>
						<tr >
							<td style="padding-top: 20px;">
								<span class="righttitle"><emp:message key="ydcx_cxyy_dtcxfs_mbmc_mh" defVal="模板名称：" fileName="ydcx"></emp:message></span></td>
							<td colspan="3" style="padding-top: 20px;">
								<input type="text" id="mmsTitle" maxlength="32" class="graytext input_bd" 
								style="width: 586px;" />											
							</td>
						</tr>
						<tr>
							<td style="padding-top: 20px;" valign="top">
								<span class="righttitle"><emp:message key="ydcx_cxyy_dtcxfs_mbnr_mh" defVal="模板内容：" fileName="ydcx"></emp:message></span>											
							</td>
									<td colspan="3" style="height:295px;padding-top: 20px;" > 
									<table>
									<tr>
										<td>
										<input type="hidden" id="framesize" value="1" />
										<div id="mmsFrame" style="float: left;background: #ededed;text-align: center;
										border: 1px solid #c5c5c5; border-bottom: 0;border-top:0;">
											<div style="width: 76px;height:16px;display: inline-block;cursor: pointer;
											 background:url('<%=skin %>/images/toUp.png');border-right:0;"
												onclick="toUp();" >
													  </div>
											<div id="mframes" style="width: 76px; overflow: auto; height: 261px;">
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
												
												<div style="width:58px;height:52px;margin-top:8px;visibility:visible;cursor: pointer;
												 display: inline-block; background:url('<%=skin %>/images/addFrame.png');"
												id="addFrame" onclick="javascript:addFrame();">
												</div>
											</div>
											<div style="width: 76px;height:16px;display: inline-block; cursor: pointer;
											background:url('<%=skin %>/images/toDown.png');margin-bottom:-2px !important;margin-bottom:0;"
											onclick="toDown();" >
											</div>
										</div>
										
										</td>
										<td id="rightFrame" style="width: 492px;padding-left:8px;" colspan="2" >
											<div id="frame_1">
											  <table style="border: 1px solid #e1e1e1;">
											  <tr>
											  <td colspan="4" style="height: 30px;background: #eff1f3;padding-left: 5px;">
											 <emp:message key="ydcx_cxyy_dtcxfs_bzcxsj_mh" defVal="本帧持续时间：" fileName="ydcx"></emp:message><input type="text" name="lasttime" id="lasttime" value="10" maxlength="2" size="2"  onkeyup="value=value.replace(/[^\d]/g,'')" 
											  oncontextmenu="window.event.returnvalue=false "/><emp:message key="ydcx_cxyy_dtcxfs_m" defVal="秒" fileName="ydcx"></emp:message>
											  <emp:message key="ydcx_cxyy_common_text_2" defVal="共" fileName="ydcx"></emp:message><span id="frameTotalSize">0.00</span>KB
											  </td>
											  </tr>
											  <tr height="32px;">
											    <td id="addMat" style="padding-left:10px;width: 75px;">
												<input type="button" class="btnMatel" value="<emp:message key="ydcx_cxyy_dtcxfs_dysc" defVal="调用素材" fileName="ydcx"></emp:message>" />
												</td>
												<td id="addImg" style="width: 75px;">
												<input type="button" class="btnImage" value="<emp:message key="ydcx_cxyy_dtcxfs_tjtp" defVal="添加图片" fileName="ydcx"></emp:message>"/>
												</td>
												<td id="addMusic" style="width: 75px;">
												<input type="button" class="btnMusic" value="<emp:message key="ydcx_cxyy_dtcxfs_tjsy" defVal="添加声音" fileName="ydcx"></emp:message>"/>
												</td>
												<td rowspan="4" style="padding-left:10px;padding-top:4px;width: 150px;height: 150px;" >
													  <div id="preview" style="width:170px;height:150px;background:#d5d6d7;margin-right:-45px;
													  display: inline-block;overflow:hidden;" >
													  </div>
												  </td>
												  
												</tr>
												<tr >
												<td colspan="2" style="padding-left:5px;height: 33px;">
												<div style="display: none;" id="delImg" class="div_bg">
												 <input type="hidden" id="ImgUrl" value="" />
												 <table><tr><td><a class="hasImage" style="text-decoration:none;">&nbsp;&nbsp;</a></td>
												 <td><label id="isize">0.00</label>KB
												 <a style="cursor:pointer;margin-left: 60px;margin-top: 10px;color: #2970c0;" onclick="delatt('ImgUrl')"><emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message></a></td>
												 </tr></table>
												</div>
												</td>
												</tr>
												<tr>
												<td colspan="2" style="padding-left:5px; height: 33px;">
												<div style="display: none" id="delMus" class="div_bg">
												<input type="hidden" id="MusUrl" value="" />
												<table><tr><td><a class="hasMusic" style="text-decoration:none;">&nbsp;&nbsp;</a></td>
												<td> <label id="msize">0.00</label>KB<a style="cursor:pointer;margin-left: 60px;margin-bottom: 10px;color: #2970c0" onclick="delatt('MusUrl')">
												<emp:message key="ydcx_cxyy_common_text_8" defVal="删除" fileName="ydcx"></emp:message></a></td></tr></table>
												</div>
												</td>
												</tr>
												<tr>
												  <td colspan="4" style="height:15px;">
												  &nbsp;<p id="paramTxt" style="margin-left: 5px;display: none"><font color="#7f7f7f"><emp:message key="ydcx_cxyy_dtcxfs_csgsw" defVal="参数格式为：“#P_1#”(如：我和#P_1#去#P_2#)" fileName="ydcx"></emp:message></font></p>
												  </td>
												  </tr>
												  <tr>
												  <td colspan="4" style="padding-left:5px;padding-bottom: 5px;padding-right:8px; ">
												  <textarea name="textContent" id="textContent" 
												  style="background:white;width:486px;height:100px;word-break:break-all;border:1.5px solid #e1e1e1;"></textarea></td>
												</tr>
											  </table>
											</div>
										</td>
									</tr>
									<tr height="15px;">
									   <td colspan="2">  <emp:message key="ydcx_cxyy_dtcxfs_cxzt_mh" defVal="共" fileName="ydcx"></emp:message><span id="totalSize">0.00</span><emp:message key="ydcx_cxyy_dtcxfs_sctmswjh" defVal="KB/80KB，生成tms文件后会略大于当前值" fileName="ydcx"></emp:message></td>
									</tr>
									</table>											
									</td>
						</tr>
						
						<tr>
						   <td style="padding-top: 20px;">
								<span class="righttitle"><emp:message key="ydcx_cxyy_dtcxfs_mbzt_mh" defVal="模板状态：" fileName="ydcx"></emp:message></span></td>
								<td colspan="3" style="padding-top: 20px;">
								<input type="radio" id="state" name="state" value="1" checked="checked" /><emp:message key="ydcx_cxyy_dtcxfs_wc" defVal="完成" fileName="ydcx"></emp:message>
								<input type="radio" id="state" name="state" value="2" style="margin-left: 10px;" /><emp:message key="ydcx_cxyy_dtcxfs_cg" defVal="草稿" fileName="ydcx"></emp:message>
								</td>
						</tr>
						</table>
						<div id="footer" style="margin-top: 10px;margin-left: 375px;" >
						        <input type="button" class="btnClass6 mr10" id="mscan" value="<emp:message key="ydcx_cxyy_common_text_6" defVal="预览" fileName="ydcx"></emp:message>"/> 
								<input type="button" id="save" value="<emp:message key="ydcx_cxyy_common_btn_22" defVal="提交" fileName="ydcx"></emp:message>" onclick="javascript:submitTemp()" class="btnClass5 mr10" />
								<input type="button" id="cancel" onclick="javascript:backPage('<%=path %>')" value="<emp:message key="ydcx_cxyy_common_btn_10" defVal="返回" fileName="ydcx"></emp:message>" class="btnClass6" />
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
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=iPath%>/js/addMMS.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		<script type="text/javascript">
		function doApp(img,width,height,size)
		{
              if (img != null && img != "")
              {
            	  setImg(img,width,height,size*1024);
            	  addApp(img,"null","",width+";"+height+";"+(size*1024),"");
              }
		}
		function doApp2(addr,size)
		{
              if (addr != null && addr != "")
              {
            	  addApp("null",addr,"","",size*1024);
              }
		}
		var zTree1;
		var setting;

		setting = {
			async : true,
			asyncUrl :"<%=path%>/dmm_sendDynMMS.htm?method=getMatelTree", //获取节点数据的URL地址
			isSimpleData: true,
			rootPID : 0,
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			callback: {
				beforeAsync : function(treeId, treeNode) {
				return false;
			    },
				click: zTreeOnClick,
				asyncSuccess:function(event, treeId, treeNode, msg){
				//zTree1.expandAll(true);
				}
			}
		};

		var zNodes =[];

		$(document).ready(function(){
			getLoginInfo("#getloginUser");
			var lgcorpcode = $("#lgcorpcode").val();
			selectType();
			setting.asyncUrl = setting.asyncUrl+"&lgcorpcode="+lgcorpcode;
			reloadTree();
		});

		function showMenu() {
			var sortSel = $("#sortSel");
			var sortOffset = $("#sortSel").offset();
			$("#dropMenu").toggle();
		}
		function hideMenu() {
			$("#dropMenu").hide();
		}
		
		function zTreeOnClick(event, treeId, treeNode) {
			if (treeNode && !treeNode.isParent) {
				//var sortObj = $("#sortSel");
				//sortObj.attr("value", treeNode.name);
				//hideMenu();
				var id = (treeNode.id).substring(1);
				var type = treeNode.type;
				var address = treeNode.address;
				var width = parseFloat(treeNode.width);
				var height = parseFloat(treeNode.height);
				var size = parseFloat(treeNode.size);
				var name = treeNode.name;
				$("#type").val(type);
				$("#address").val(address);
				$("#size").val(size);
				$("#width").val(width);
				$("#height").val(height);
				if (width-200>=0){
			    	width = width*((190/width));
			    	height = height*(190/height);
			    }
			    if (height-150>=0) {
			    	width = width*(140/width);
			    	height = height*(140/height);
			    }
			    var  padding = parseFloat((140-height)/2);
			    
			     var fileServerUrl="<%=StaticValue.getFileServerUrl()%>";
			    if(fileServerUrl!=null&&fileServerUrl!=""){
			    	$.post("<%=path%>/dmm_sendDynMMS.htm?method=checkMmsFile", {
					url : address
					},
					function(result) {
						 if(result == "true"){
						 }else if (result == "false"){
						    //alert("素材不存在！");
						    alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_scbcz"));
						    return;
						}else{
							//alert("素材不存在！");
							alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_scbcz"));
							return;
						}
					});
			    }
			    
			    var result ;
				if ("AMR" != type && "MID" != type && "MIDI" != type){
					result = "<img src=\'"+address+ "\' style=\'width:"+width+"px;height:"+height+"px;margin-top:"+padding+"px;\'/>";
				}else{
					result = "<img src=\'"+"<%=skin %>/images/hasMusic.png"+"\'/>"+name+"(ID:"+id+")";
				}
				 $("#matList").empty();
                 $("#matList").html("<center>"+result+"</center>");
			}
		}

		function reloadTree() {
			showMenu();
			zTree1 = $("#materialTree").zTree(setting, zNodes);
		}	
		
	
		
		$(document).ready(function() {
				var isIE = false;
				var isFF = false;
				var isSa = false;
				if ((navigator.userAgent.indexOf("MSIE") > 0)
						&& (parseInt(navigator.appVersion) >= 4))
					isIE = true;
				if (navigator.userAgent.indexOf("Firefox") > 0)
					isFF = true;
				if (navigator.userAgent.indexOf("Safari") > 0)
					isSa = true;
					//现需支持斜杠跟反斜杠
				/*$('#textContent').keypress(function(e) {
					var iKeyCode = window.event ? e.keyCode
							: e.which;
					if (iKeyCode == 47 || iKeyCode == 92) {
						if (isIE) {
							event.returnValue = false;
						} else {
							e.preventDefault();
						}
					}
				}
				);*/
			
		})
		
		function backPage(path){
		    var templateType = '<%=templateType%>';
		    var url ;
		    if(templateType == "0"){
		        url="/smm_sameMms.htm";
		    }else if(templateType == "1"){
		    	url = "/dmm_sendDynMMS.htm";
		    }
			var lgguid = $("#lgguid").val();
			window.location.href = path+url+'?method=find&lgguid='+lgguid+"&lguserid="+$("#lguserid").val();
		}
		
		function submitTemp()
{
	var acc = $("#mmsacc").val();
	var id = $(".mmsFrame").attr("id");		//获取显示帧框的样式中的ID
	saveframe(id)
	var pathUrl = $("#pathUrl").val();
	var mmsTitle = $.trim($("#mmsTitle").attr("value"));	//模板名称
	var st = $("input:radio[name='state']:checked").attr("value");//模板状态
	
	if(acc=="false" && st=="1")
	{
		//alert("没有彩信发送账号，只能保存为草稿！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_mycxfszh"));
		return;
	}
	var contents = new Array();
	var index = 0;
	for (var i=0;i<arr.length;i++)
	{
		if ($.trim(arr[i]) != "")
		{
			var as = arr[i];
			as=as.replace(/\r\n/g,"<BR/>")   
		    as=as.replace(/\n/g,"<BR/>"); 
			var data = $.parseJSON(as);  
		    if (data != null && data != "")
		    {
		    	if(data.ImgUrl == "" && data.MusUrl=="" && data.textContent=="")
		    	{
		    		//alert("第"+(parseInt(i)+1)+"帧为空白帧，请修改！");
		    		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_d")+(parseInt(i)+1)+getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_zwkbzqxg"));
					return;
		    	}
		    }
			contents[index] = arr[i];
			index++;
		}
	}
	var si = $.trim($("#totalSize").html());
	if(mmsTitle == "")
	{
		//alert("模板名称不能为空！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_mbmcbnwk"));
		return;
	}
	else if(/(\<{1,})|(\>{1,})|(\'{1,})|(\"{1,})|(\\{1,})/.test(mmsTitle)){
		//alert("模板名称中包含不允许的特殊字符(\",\',\<,\>,\\)！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_mbmczbhbyxdtszf"));
	    return;
	}
		
	if(contents.length <= 0)
	{
		//alert("模板内容不能为空！");
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_mbnrbnwk"));
		return;
	}
	if(parseFloat(si)-parseFloat(80)>0.00)
	{
		//alert("模板内容不能大于80KB！")
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_mbnrbndy80kb"))
		return;
	}
    else
	{
    	$("#save").attr("disabled","disabled");
    	$("#mscan").attr("disabled","disabled");
    	$("#cancel").attr("disabled","disabled");
    	var templateType = $("input[name='templateType']:checked").val();
    	var tmId = $("#tmId").val();
    	var opType = $("#opType").val();
    
    	var lgguid = $("#lgguid").val();
    	var lguserid = $("#lguserid").val();
    	var lgcorpcode = $("#lgcorpcode").val();
    	var tmpltype = $("#tmpltype").val();
    	var url;
	   if(tmpltype == "0"){
	        url="/smm_sameMms.htm";
	    }else if(tmpltype == "1"){
	    	url = "/dmm_sendDynMMS.htm";
	    }
    	$.post(pathUrl+"/dmm_sendDynMMS.htm?method=checkBadWord",{content:contents,lgcorpcode:lgcorpcode,templateType:templateType},function(result){
    		if (result != null && result == "")
    		{
    			$.post(pathUrl+"/dmm_sendDynMMS.htm?method=update",{tmId:tmId,mt:mmsTitle,cont:contents,s:st,opType:opType,
    				templateType:templateType,lguserid:lguserid,lgcorpcode:lgcorpcode},function(result){
    				if(result != null && result == "overSize"){
    					//alert("tms文件不能大于80KB！");
    					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_tmswjbndy80kb"));
    					$("#save").attr("disabled",false);
    					$("#mscan").attr("disabled",false);
    					$("#cancel").attr("disabled",false);
    		    		return;
    				}else if(result != null && result == "caogaotrue"){
    					//alert("保存草稿成功！");
    					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_bccgcg"));
    					//location.href = pathUrl+"/dmm_sendDynMMS.htm?method=doAdd&lgguid="+lgguid;
   						window.location.href = pathUrl+url+'?method=find&lgguid='+lgguid+"&lguserid="+lguserid;
    				}else if(result != null && result == "true")
    				{
    					//alert("模板新建成功！请等待系统审核！");
    					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_mbxjcg"));
    					//location.href = pathUrl+"/dmm_sendDynMMS.htm?method=doAdd&lgguid="+lgguid;
       					window.location.href = pathUrl+url+'?method=find&lgguid='+lgguid+"&lguserid="+lguserid;
    				}
    				else
    				{
    					//alert("操作失败！");
    					alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_czsb"));
    					$("#save").attr("disabled",false);
    					$("#mscan").attr("disabled",false);
    					$("#cancel").attr("disabled",false);
    		    		return;
    				}
    			});
    		}
    		else if(result !=null && result =="Pfalse" && templateType == 1)
    		{
    			//alert("内容中动态参数填写不合法!");
    			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_nrzdtcstxbhf"));
    			$("#save").attr("disabled",false);
				$("#mscan").attr("disabled",false);
				$("#cancel").attr("disabled",false);
    			return;
    		}else if(result != null && result == "noParam"  && templateType == 1)
    		{
    			//alert("请输入参数！");
    			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_qsrcs"));
    			$("#save").attr("disabled",false);
				$("#mscan").attr("disabled",false);
				$("#cancel").attr("disabled",false);
    	    	return ;
    		}
    		else if(result != null && result == "moreParam"  && templateType == 1)
    		{
    			//alert("模板参数最多20个！");
    			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_mbcszd20g"));
    			$("#save").attr("disabled",false);
				$("#mscan").attr("disabled",false);
				$("#cancel").attr("disabled",false);
    	    	return ;moreParam
    		}
    		else if(result != null && result != "")
    		{
    			//alert("内容包含关键字："+result);
    			alert(getJsLocaleMessage("ydcx","ydcx_cxyy_dtcxfs_nrbhgjz_mh")+result);
    			$("#save").attr("disabled",false);
				$("#mscan").attr("disabled",false);
				$("#cancel").attr("disabled",false);
	    		return;
    		}
    	});
	}
}
        </script>
	</body>
</html>
