<%@ page language="java" import="com.montnets.emp.common.vo.LfMaterialVo" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.rms.servmodule.constant.ServerInof"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
	//1静态彩信  2动态彩信  3新增
	String pathtype = (String)request.getParameter("type");
	//服务器名称
	String serverName = ServerInof.getServerName();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

	LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
	<%@include file="/common/common.jsp" %>
	<title></title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<%if(StaticValue.ZH_HK.equals(langName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
	<link href="<%=iPath%>/css/mmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
	<link href="<%=skin %>/mmsTemplate.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/batchFileSend.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >

	<link rel="stylesheet" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>

	<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
	<link rel="stylesheet" href="<%=inheritPath%>/plugin/resource/colorpicker/css/colorpicker.css">
	<link rel="stylesheet" href="<%=inheritPath%>/plugin/fx.editor.css"/>
	<style>
		#fxeditor{
			width: 880px;margin: 0px auto;
		}
	</style>

	<style type="text/css">
		.bt1{
			letter-spacing:0px;
			font-size: 12px;
			color: white;
		}
		.bt2{
			letter-spacing:0px;
			font-size: 12px;
		}
		.sp1{
			text-align: center;
			margin-left: 5px;
		}

		.degreeSize{
			color: #ff5400;
		}

		.degree{
			color: #ff5400;
		}

		#load-bg{
			display: none;
			position: absolute;
			top: 0%;
			left: 0%;
			width: 100%;
			height: 100%;
			z-index:99999;
			-moz-opacity: 0.4;
			opacity:.40;
			filter: alpha(opacity=40);
			background:#eee url('<%=path %>/common/img/loading-bg.gif') no-repeat center;
		}
		#chartPopup table {
			border:1px solid #A9A9A9;
			BORDER-LEFT-WIDTH: 0px;
			BORDER-COLLAPSE: collapse;
			BORDER-RIGHT-WIDTH: 0px;
			height: 160px;
		}
		#chartPopup table td {
			border:1px solid #A9A9A9;
			BORDER-RIGHT: medium none;
			BORDER-LEFT: medium none;
			height:40px;
			padding-left: 3px;
			padding-right: 3px;
		}
		#chartPopup input{
			border: 0px;
			text-align: center;
		}
		#chartPopup button{
			color:#008cdb;
			border: 0px;
			background: #fff;
			cursor: pointer;
			padding:2px;
			margin: 0;
		}
		#chartPopup .box{
			width: 260px;
			height: 90px;
			font-size:13px;
			background-color: #FFFFFF;
			z-index: 20;
			left: 370px;
			top:96px;
			box-shadow:0px 0px  5px 5px #ccc;
			position:absolute;
			padding: 3px;
			border-radius: 3px;
		}


		.industryName{
			margin-top:10px;
			margin-left: 10px;

		}

		.induStryTb{
			margin-top:16px;
			width:95%;

		}
		.induStryTb thead tr th{
			height: 40px;
			line-height: 40px;
			background-color:#f3f6fd;
			border-bottom: 1px solid #f3f6fd;
		}
		.induStryTb  tr td{
			padding-left:10px;
			height: 35px;
			line-height: 35px;
			border-bottom: 1px solid #f3f6fd;

		}

		#staticState,#dynamic,#staticState_bar_table,#dynamic_bar_table td {
			font-size: 12px;
			text-align: center;
		}
		.czbtn{
			color: #008cdb;
			padding-left: 14px;
		}
		.nameClass{
			border: 0px;
			height: 30px;
			background-color: white;
			line-height: 30px;
		}
		select::-ms-expand{ display: none; }
		select{
			appearance:none;
			-moz-appearance:none;
			-webkit-appearance:none;
			 background: url("<%=path %>/rms/mbgl/image/select_icon.png") no-repeat scroll 480px transparent;
			padding-left:5px;
		}
		/*.inputTable .c_selectBox{
			vertical-align: bottom;
			display: inline-block;
			height: 28px!important;
			width: 500px!important;
		}
		.inputTable .c_result{
			width: 500px!important;
			margin-top: 8px;
		}
		.inputTable .c_input{
			height: 24px!important;
			width: 450px!important;
		}*/
		.inputTable .showSpan{
			display: inline-block;
			height: 25px;
		}
		.preview-hint{
				display: none;
			}
	</style>

</head>
<body>
<input type="hidden" id="contentSize" value="">
<div id="rContent" class="rContent">
	<table style="margin-top: 10px;" <%--id="selfSelect" --%>class="inputTable">
		<tr style="height: 40px;">
			<td  style="width: 80px;" align="right"><div style="line-height: 25px;font-size: 12px;color: #666666;height: 30px;font-weight: bold;padding-right: 14px;"><emp:message key="rms_fxapp_myscene_fxtheme" defVal="富信主题：" fileName="rms"/></div></td>
			<td><input id="tmName" style="width: 500px;height: 30px;border: 1px solid #e2e4e8;" type="text" maxlength="20"/><span id="countSp">&nbsp;&nbsp;0/20</span></td>
		</tr>
		<c:if test="${source eq 'commontemplate'}" >
			<tr style="height: 40px;">
				<td  style="width: 80px;" align="right"><div style="line-height: 25px;font-size: 12px;color: #666666;height: 30px;font-weight: bold;padding-right: 14px;"><emp:message key="rms_fxapp_pubscene_trades2" defVal="行业：" fileName="rms"/></div></td>
				<td>
					<select id="IndustryList" style="width: 500px;height: 30px;border: 1px solid #e2e4e8;">
						<c:forEach items="${industryList}" var="industry">
							<option id="option_${industry.id}" value="${industry.id}" onclick="searchByInduOrUse(this)">${industry.name}</option>
						</c:forEach>
					</select>
					<div class="showSpan">&nbsp;&nbsp;<a href="javascript:void(0);" onclick="showInduUse(0)" class="myactive"><emp:message key="rms_fxapp_myscene_tradesmng" defVal="行业管理" fileName="rms"/></a></div>
				</td>
			</tr>
			<tr style="height: 40px;">
				<td  style="width: 80px;" align="right"><div style="line-height: 25px;font-size: 12px;color: #666666;height: 30px;font-weight: bold;padding-right: 14px;"><emp:message key="rms_fxapp_myscene_use2" defVal="用途：" fileName="rms"/></div></td>
				<td>
					<select id="useList" style="width: 500px;height: 30px;border: 1px solid #e2e4e8;">
						<c:forEach items="${useList}" var="use">
							<option id="option_${use.id}"  value="${use.id}" onclick="searchByInduOrUse(this)">${use.name}</option>
						</c:forEach>
					</select>
					<div class="showSpan">&nbsp;&nbsp;<a href="javascript:void(0);" onclick="showInduUse(1)" class="myactive"><emp:message key="rms_fxapp_myscene_usemng" defVal="用途管理" fileName="rms"/></a></div>
				</td>
			</tr>
			<br/>
		</c:if>
		<tr  style="height: 550px;">
			<td>&nbsp;</td>
			<td>
				<div id="fxeditor"> </div>
			</td>
		</tr>
		<tr  style="height: 30px;">
			<td>&nbsp;</td>
			<td colspan="2">
				<span><emp:message key="rms_fxapp_myscene_currcapacity" defVal="当前容量：" fileName="rms"/></span><span id="degreeSize" class="degreeSize"></span>KB
				<span style="margin-left: 30px;">&nbsp;&nbsp;<emp:message key="rms_fxapp_myscene_level" defVal="档位：" fileName="rms"/></span><span id="degree" class="degree"></span><emp:message key="rms_fxapp_myscene_levelunit" defVal="档" fileName="rms"/>
			</td>
			<td>
				<p style="margin-top: -12px;margin-left: -280px;color: #888;"><emp:message key="rms_fxapp_myscene_showtip1" defVal="手机预览效仅供参考，具体以手机显示为准。" fileName="rms"/></p>
			</td>
		</tr>
		<tr  style="height: 40px;">
			<td></td>
			<td colspan="2">
				<span><input id="zcBt" type="button" value="<emp:message key="rms_fxapp_myscene_savetemp" defVal="暂存草稿" fileName="rms"/>" class="mybtnClass5  bt1"></span>
				<span class="sp1"><input id="subBt" type="button" value="<emp:message key="rms_fxapp_myscene_submitexam" defVal="提交审核" fileName="rms"/>" class="mybtnClass5  bt1"></span>
				<span class="sp1"><input id="backBt" type="button" value="<emp:message key="common_return" defVal="返回" fileName="common"/>" class="mybtnClass6 bt2" ></span>
			</td>
		</tr>
	</table>
	<jsp:include page="addChart.jsp" flush="true"/>
</div>
<div id="load-bg"></div>


<div style="z-index:5;position: absolute;overflow: hidden;top:0px;left: 0;height: 100%;width: 100%;background-color: #333333;opacity:0.8;margin: 0;padding: 0;display: none;" id="industry_div_1"></div>
<div style="position: absolute;z-index:50;width: 444px;height: 455px;background-color: #ffffff;opacity: 0.9;border-radius:5px;left:400px;top:100px;display: none;" id="industry_div">
	<div style="height: 40px;width:444px;background-color: #f7f9fc;border-bottom: 1px solid #eaeff4; ">
		<div style="height: 40px;width:285px;float: left;line-height: 40px;padding-left: 18px;" id="popup_div_3"><emp:message key="rms_fxapp_myscene_tradesmng" defVal="行业管理" fileName="rms"/></div>
		<div style="height: 40px;width: 40px;float: right;line-height: 40px;text-align: center;cursor: pointer;color:#888888;font-size: 14px;"  onclick="closeDiv()">X</div>
	</div>
	<div style="height: 400px;" >
		<div class="industryName">
			<span><font><emp:message key="rms_fxapp_myscene_tradesname" defVal="行业名称：" fileName="rms"/></font><input type="text" id="InduName" style="height: 24px;border:1px solid #dee2e6;" maxlength="6";>
			</span><span style="margin-left: 10px; color: #008cdb;"><a href="javascript:void(0);" style="color: #008cdb;" onclick="addInduUse(0)"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></a>
			</span>
		</div>
		<div align="center" style="height:380px;overflow-y:auto;">
			<table id="induStryTb" class="induStryTb">
				<thead>
				<tr>
					<th align="left"  style="padding-left: 20px;color: #222222;"><emp:message key="rms_fxapp_myscene_tradesname2" defVal="行业名称" fileName="rms"/></th>
					<th align="right" style="padding-right: 70px;color: #222222;"><emp:message key="common_operation" defVal="操作" fileName="common"/></th>
				</tr>
				</thead>
				<tbody>
				<c:forEach items="${industryList}" var="industry">
					<tr class="hoverClass">
						<td><input id="name_${industry.id}" class="nameClass"  type="text" value="${industry.name}" disabled="disabled" maxlength="6";></td>
						<td>
							<a class="czbtn" id="rename_${industry.id}" href="javascript:void(0);" onclick="rename('${industry.id}',0)"><emp:message key="rms_fxapp_pubscene_rename" defVal="重命名" fileName="rms"/></a>
							<a class="czbtn" href="javascript:void(0);" myid="${industry.id}" onclick="deleteInUse(this)"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></a>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>

<%--用途管理  --%>
<div style="z-index:5;position: absolute;overflow: hidden;top:0px;left: 0;height: 100%;width: 100%;background-color: #333333;opacity:0.8;margin: 0;padding: 0;display: none;" id="use_div_1"></div>
<div style="position: absolute;z-index:50;width: 444px;height: 455px;background-color: #ffffff;opacity: 0.9;border-radius:5px;left:400px;top:100px;display: none;" id="use_div">
	<div style="height: 40px;width:444px;background-color: #f7f9fc;border-bottom: 1px solid #eaeff4; ">
		<div style="height: 40px;width:285px;float: left;line-height: 40px;padding-left: 30px;" id="popup_div_3"><emp:message key="rms_fxapp_myscene_usemng" defVal="用途管理" fileName="rms"/></div>
		<div style="height: 40px;width: 40px;float: right;line-height: 40px;text-align: center;cursor: pointer;color:#888888";font-size: 14px;" onclick="closeDiv()">X</div>
</div>
<div style="height: 400px">
	<div class="industryName">
			<span><font><emp:message key="rms_fxapp_myscene_usename" defVal="用途名称：" fileName="rms"/></font><input type="text" id="useName" style="height: 24px;border:1px solid #dee2e6;" maxlength="6";>
			</span><span style="margin-left: 10px;"><a href="javascript:void(0);" style="color: #008cdb;" onclick="addInduUse(1)"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></a>
			</span>
	</div>
	<div align="center" style="height:380px;overflow:auto;">
		<table id="useTb" class= "induStryTb">
			<thead>
			<tr>
				<th align="left"  style="padding-left: 20px;color: #222222;"><emp:message key="rms_fxapp_myscene_usename2" defVal="用途名称" fileName="rms"/></th>
				<th align="right" style="padding-right: 70px;color: #222222;"><emp:message key="common_operation" defVal="操作" fileName="common"/></th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${useList}" var="use">
				<tr class="hoverClass">
					<td><input id="name_${use.id}"  class="nameClass"  type="text"  value="${use.name}" disabled="disabled" maxlength="6";></td>
					<td>
						<a class="czbtn" id="rename_${use.id}" href="javascript:void(0);" onclick="rename('${use.id}',1)"><emp:message key="rms_fxapp_pubscene_rename" defVal="重命名" fileName="rms"/></a>
						<a class="czbtn" href="javascript:void(0);" myid="${use.id}" onclick="deleteInUse(this)"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</div>
</div>
<div style="z-index:5;position: absolute;overflow: hidden;top:0px;left: 0;height: 100%;width: 100%;background-color: #333333;opacity:0.8;margin: 0;padding: 0;display: none;" id="popup_div"></div>
<div style="position: absolute;z-index:50;width:1038px;height:742px;background-color: #ffffff;border-radius:5px;left:90px;top:30px;display: none;" id="tempFrames">
	<div id="tmplDiv" title="">
		<iframe id="tempFrame" name="tempFrame" style="width:1036px;height:740px;border: 0;" frameborder="0" src ="rms_rmsSameMms.htm?method=getLfTemplateByMms&choosePublic=1&pageSource=1&source=${source}" ></iframe>
	</div>
</div>

<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/ajaxfileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
	src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript"
	src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
<script type="text/javascript"
	src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>

<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>


<script src="<%=inheritPath%>/plugin/test/jquery-1.12.4.min.js"></script>
<script src="<%=inheritPath%>/plugin/resource/webuploader-0.1.5/webuploader.min.js"></script>
<script src="<%=inheritPath%>/plugin/resource/cropper.min.js"></script>
<script src="<%=inheritPath%>/plugin/resource/video6.6.2.js/ion.rangeSlider.js"></script>
<script src="<%=inheritPath%>/plugin/resource/video6.6.2.js/video.min.js"></script>
<script src="<%=inheritPath%>/plugin/resource/colorpicker/js/colorpicker.js" charset="utf-8"></script>
<script src="<%=inheritPath%>/plugin/fx.editor.js"></script>
<script type="text/javascript" src="<%=iPath%>/js/echart-min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/pie_echart.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/bar_echart.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/line_echart.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript" src="<%=iPath%>/js/chartInsert.js?V=<%=StaticValue.getJspImpVersion() %>"></script>

<script type="text/javascript">

	SetLanguage('<%=langName%>');
    window.FXEDITOR_CONFIG.HOME_URL = '${basePath }';
    //初始化富文本编辑器
    FxEditor.getEditor("fxeditor");
    //window.FXEDITOR_CONFIG.audioConfig.size =1887437;//1.8M
    //获取默认配置参数:初始大小2KB
    var paramSize  = parseInt('${paramSize}');
    var maxSize  = parseInt('${maxSize}');
    // 格式：total:par:text:img:audio:vedio
    var smilFileSizes  = '${smilFileSize}'.split(":");

    FxEditor.listenFxEditorChange(function(rst){
        var size = rst.allSize;
        //计算大小和档位
        var size = size+parseInt(smilFileSizes[0]);
        var existParam = false;
        var contenHtml = $(".J-keyframe").html();
       	if(typeof(contenHtml)== "undefined"){//编辑框无内容的情况
       		 size = 0;
       	}else{
      	    $(".J-keyframe").each(function(index,obj){
	            size = parseInt(size)+parseInt(smilFileSizes[1]);
	            $(this).find(".J-edit-text").each(function(i,text){
	                var content =  $(this).text();
	                content = content.replace(reg,replacer);
	                var paramLength = getParamLength(content);
	                if(parseInt(paramLength) > 0){
	                    existParam = true;
	                }
	                if(content.trim() != ""){//有内容才加size
		                size = parseInt(size)+parseInt(smilFileSizes[2]);
	                }
	            });
	
	            $(this).find(".J-editor-img").each(function(i,img){
	                size = parseInt(size)+parseInt(smilFileSizes[3]);
	            });
	
	            $(this).find(".J-audio").each(function(i,audio){
	                size = parseInt(size)+parseInt(smilFileSizes[4]);
	            });
	
	            $(this).find(".J-video").each(function(i,video){
	                size = parseInt(size)+parseInt(smilFileSizes[5]);
	            });
        	});
		}
        if(existParam){//存在参数，加默认参数size
            size += paramSize;
        }

		//富信编辑框内容size
		var contentSize = size;
		$("#contentSize").val(contentSize);
        //标题
        var titleBytesSize = getStringByteSize($("#tmName").val());
        size += titleBytesSize;

        size = size%1024 ==0?parseInt(size/1024):(parseInt(size/1024)+1);
        var degree = getDegree(size);
        document.getElementById("degreeSize").innerHTML=size;
        document.getElementById("degree").innerHTML=degree;

    });

</script>

<script type="text/javascript">

    $("#zcBt").click(function(){

        if(checkTitle()){
            //富信主题
            var tmName = $("#tmName").val();
            $(".J-keyframe-edit-content").remove();
            //富信内容生成的HTML
            var fxHtml=FxEditor.getContentHtml("fxeditor");
            //替换参数 --{#参数N#}--%>{#P_N#}
            //fxHtml = fxHtml.replace(reg,replacer);
            //操作类型：2：暂存草稿，1：提交审核
            var opType = 2;
            //档位
            var degree = $("#degree").text();
            //容量
            var degreeSize = $("#degreeSize").text();

            //模板内容
            var parmLegth =FxEditor.getContentHtml("fxeditor");

            //替换参数--%>#P_N#
            parmLegth = parmLegth.replace(reg,replacer);
            parmLegth = parmLegth.replace(reg_imgParam,replacer);
            //校验是否为空帧
            if(checkEmptyFrame(fxHtml)){
                return;
            }

            //改变同一帧里先报表后文本删除报表后的帧类型
            fxHtml = changeChartToText();

            if(degree <= 0){
                alert(getJsLocaleMessage("rms","rms_myscene_alert2"));
            }
            //行业ID
            var industryID = $("#IndustryList option:selected").val();
            //用途ID
            var useID = $("#useList option:selected").val();
            //我的场景-公共场景判断 ： source == 'commontemplate'为公共场景
            var source ='${source}';
            $.ajax({
                //提交数据的类型 POST GET
                type:"POST",
                //提交的网址
                url:"mbgl_mytemplate.htm?method=update",
                //提交的数据
                data:{tmThame:tmName,fxHtml:fxHtml,opType:opType,
                    degree:degree,degreeSize:degreeSize,parmLegth:parmLegth,industryID:industryID,useID:useID,source:source},
                //成功返回之前调用的函数
                beforeSend:function(){
                    $("#load-bg").show();
                },
                //成功返回之后调用的函数
                success:function(msg){
                    $("#load-bg").hide();
                    if("true" == msg){
                        alert(getJsLocaleMessage("rms","rms_myscene_alert3"));
                        var path = '<%=path%>';
                        if(source =="commontemplate"){
                            window.location.href = path+"/rms_commTpl.htm?method=find";
                        }else{
                            window.location.href = path+"/mbgl_mytemplate.htm?method=find";
                        }
                    }else{
                        alert(getJsLocaleMessage("rms","rms_myscene_alert4")+msg);
                    }
                }
            });
        }
    });


    $("#subBt").click(function(){
        if(checkTitle()){
            //富信主题
            var tmName = $("#tmName").val();
            $(".J-keyframe-edit-content").remove();
            //富信内容生成的HTML
            var fxHtml=FxEditor.getContentHtml("fxeditor");
            //替换参数 --{#参数N#}--%>{#P_N#}
            //fxHtml = fxHtml.replace(reg,replacer);

            //操作类型：1：暂存草稿，2：提交审核
            var opType = 1;
            //档位
            var degree = $("#degree").text();
            //容量
            var degreeSize = $("#degreeSize").text();
            //参数个数--%>改为直接获取内容，后端进行参数个数判断
            var parmLegth =FxEditor.getContentHtml("fxeditor");

            //替换参数--%>#P_N#
            parmLegth = parmLegth.replace(reg,replacer);
            parmLegth = parmLegth.replace(reg_imgParam,replacer);
            if(degree <= 0){
                alert(getJsLocaleMessage("rms","rms_myscene_alert5"));
                return;
            }
            //判断上传容量
            if(parseInt(degreeSize) > maxSize/1024 ){
                alert(getJsLocaleMessage("rms","rms_myscene_alert6")+ parseFloat((maxSize/(1024*1024))).toFixed(2)+getJsLocaleMessage("rms","rms_myscene_alert7"));
                return;
            }
            //校验是否为空帧
            if(checkEmptyFrame(fxHtml)){
                return;
            }
            //改变同一帧里先报表后文本删除报表后的帧类型
            fxHtml = changeChartToText();

            //行业ID
            var industryID = $("#IndustryList option:selected").val();
            //用途ID
            var useID = $("#useList option:selected").val();
            //我的场景-公共场景判断 ： source == 'commontemplate'为公共场景
            var source ='${source}';
            $.ajax({
                //提交数据的类型 POST GET
                type:"POST",
                //提交的网址
                url:"mbgl_mytemplate.htm?method=update",
                //提交的数据
                data:{tmThame:tmName,fxHtml:fxHtml,opType:opType,degree:degree,degreeSize:degreeSize,parmLegth:parmLegth,industryID:industryID,useID:useID,source:source},
                //返回数据的格式
                //成功返回之前调用的函数
                beforeSend:function(){
                    $("#load-bg").show();
                },

                success:function(msg){
                    $("#load-bg").hide();
                    if("true" == msg){
                        alert(getJsLocaleMessage("rms","rms_myscene_alert8"));
                        var path = '<%=path%>';
                        var source ='${source}';
                        //window.location.href = path+"/mbgl_mytemplate.htm?method=find";
                        if(source =="commontemplate"){
                            window.location.href = path+"/rms_commTpl.htm?method=find";
                        }else{
                            window.location.href = path+"/mbgl_mytemplate.htm?method=find";
                        }
                    }else{
                        alert(getJsLocaleMessage("rms","rms_myscene_alert14")+msg);
                    }
                },
                error:function(){
                    alert(getJsLocaleMessage("rms","rms_myscene_alert9"));
                    $('#load-bg').hide();
                }

            });

        }
    });

    $("#backBt").click(function(){
        var path = '<%=path%>';
        var source ='${source}';
        if(source =="commontemplate"){
            window.location.href = path+"/rms_commTpl.htm?method=find";
        }else{
            window.location.href = path+"/mbgl_mytemplate.htm?method=find";
        }
        //window.history.go(-1);
    });
    $("#backBt").mouseover(function(){
        $(this).addClass("back-hover");
    });

    $("#backBt").mouseout(function(){
        $(this).removeClass("back-hover");
    });

    $(".mybtnClass5").mouseover(function(){
        $(this).addClass("save-hover");
    });

    $(".mybtnClass5").mouseout(function(){
        $(this).removeClass("save-hover");
    });


    $("#tmName").keyup(function(){
        var value = $('#tmName').val().length;
        if(value < 20){
            $("#countSp").html("&nbsp;&nbsp;"+value+"/20");
        }else{
            $("#countSp").html("&nbsp;&nbsp;"+20+"/20");
        }

        //预览赋值
        var title = $("#tmName").val().substring(0,20);
        FxEditor.setEditorTitle(title);
        $(".J-preview-content").find(".fxeditor-preview-title").remove();
        $(".J-preview-content").prepend("<h4 class='fxeditor-preview-title'>"+title+"</h4>");
		calcDegreeAndSize();
    });

    //获取档位
    function  getDegree(degreeSize){
        var deg= 0;
        var degreeMap = '${deGreeMap}';
        if(degreeMap == null
            || degreeMap == 'null'
            || degreeMap.length <=0){
            return 0;
        }
        var obj =$.parseJSON(degreeMap);
        $.each(obj,function(degree,value) {
            var degreeBegin =parseInt(value.split("-")[0]);
            var degreeEnd = parseInt(value.split("-")[1]);
            if(parseInt(degreeSize) > degreeBegin && parseInt(degreeSize) <= degreeEnd){
                deg =  degree;
            }
        });
        return deg;
    }

    //匹配参数个数
    function getParamLength(strSource) {
        //统计字符串中包含{}或{xxXX}的个数
        str = " "+strSource+" ";
        var eg = /#[pP]_[1-9][0-9]*#/;
        var arr = str.split(eg);
        return arr.length-1;
    }

    //校验主题
    function checkTitle(){
        var flag = true;
        var  tmName = $("#tmName").val().trim();
        if(tmName ==""){
            alert(getJsLocaleMessage("rms","rms_myscene_alert10"));
            flag  = false;
        }
        return flag;
    }

    //获取文本字节数
    function getStringByteSize(s) {
        var totalLength = 0,
            i,
            charCode;

        for (i = 0; i < s.length; i++) {
            charCode = s.charCodeAt(i);
            if (charCode < 0x007f) {
                totalLength = totalLength + 1;
            } else if ((0x0080 <= charCode) && (charCode <= 0x07ff)) {
                totalLength += 2;
            } else if ((0x0800 <= charCode) && (charCode <= 0xffff)) {
                totalLength += 3;
            } else if ((00010000 <= charCode) && (charCode <= 0x1fffff)) {
                totalLength += 4;
            }
        }
        return totalLength;
    }


    reg=/{#参数(.*?)#}/g;
    //参数格式替换
    function replacer(match, p1, p2, p3, offset, string){
        return '#P_'+ p1+'#';
    }
    reg_imgParam=/{#图参(.*?)#}/g;
    //参数格式替换
    function replacer(match, p1, p2, p3, offset, string){
        return '#P_'+ p1+'#';
    }


    //行业-用途弹框
    function showInduUse(type){
        if(type ==0){
            //$("#industry").dialog('option','title','行业管理');
            //$("#industry").dialog("open");
            $("#industry_div").css({"display":""});
            $("#industry_div_1").css({"display":""});
            // name = $("#InduName").val();
        }else if(type ==1){
            //$("#use").dialog('option','title','用途管理');
            //$("#use").dialog("open");
            $("#use_div").css({"display":""});
            $("#use_div_1").css({"display":""});
            // name = $("#useName").val();
        }
    }

    //行业-用途添加
    function addInduUse(type){//0-行业,1-用途
        var name = "";
        if(type ==0){
            name = $("#InduName").val();
        }else if(type ==1){
            name = $("#useName").val();
        }
        if(""==name){
            alert(getJsLocaleMessage("rms","rms_myscene_alert11"));
            return;
        }

        var baseUrl = '<%=basePath%>';
        $.ajax({
            type:"POST",
            url:baseUrl+"/rms_commTpl.htm?method=add",
            data:{iuName:name,type:type},
            success:function(id){
                if(id !=="fault" && id !=getJsLocaleMessage("rms","rms_myscene_alert12")){
                    alert(getJsLocaleMessage("rms","rms_myscene_addsuccess"));
                    var tbody = "<tr class='hoverClass'>"+
                        "<td><input class='nameClass' id='name_"+id+"' type='text' value="+name+" disabled='disabled' maxlength='6'></td>"+
                        "<td><a class='czbtn' id='rename_"+id+"' href='javascript:void(0);' onclick='rename("+id+","+type+")'>"+getJsLocaleMessage("rms","rms_myscene_rename")+"</a>"+
                        "<a class='czbtn' href='javascript:void(0);' myid="+id+" onclick='deleteInUse(this)'>"+getJsLocaleMessage("rms","rms_myscene_del")+"</a></td>"+
                        "</tr>";

                    var option = "<option value="+id+" onclick='searchByInduOrUse(this)'>"+name+"</option>";
                    if(type ==0){
                        $("#induStryTb tbody").append(tbody);
                        $("#IndustryList").append(option);
                        $("#InduName").val("");
                    }else if(type ==1){
                        $("#useTb tbody").append(tbody);
                        $("#useList").append(option);
                        $("#useName").val("");
                    }

                }else{
                    if(id =="fault"){
                        alert(getJsLocaleMessage("rms","rms_myscene_alert15"));
                    }else{
                        alert(id);
                    }
                }
            }
        });
    }

    //删除行业-用途
    function deleteInUse(obj){
        if(confirm(getJsLocaleMessage("rms","rms_myscene_confimdel2"))){
            var id = $(obj).attr("myid");
            var baseUrl = '<%=basePath%>';
            //window.location.href=url+"/rms_commTpl.htm?method=delete&id="+id;
            $.ajax({
                type:"POST",
                url:baseUrl+"/rms_commTpl.htm?method=delete",
                data:{id:id},
                success:function(idstr){
                    if(idstr !=="fault"){
                        alert(getJsLocaleMessage("rms","rms_myscene_successdel"));
                        //js 删除当前行
                        var tr=obj.parentNode.parentNode;
                        var tbody=tr.parentNode;
                        tbody.removeChild(tr);

                        //下拉宽对应删除
                        $("#option_"+id).remove();
                    }else{
                        alert(getJsLocaleMessage("rms","rms_myscene_faildel2"));
                    }
                }
            });
        }
    }
    //重命名
    function rename(obj,type){
        var baseUrl = '<%=basePath%>';
        var name = $("#name_"+obj).val();
        var renameStatus =  $("#rename_"+obj).text();
        if(""==name){
            alert(getJsLocaleMessage("rms","rms_myscene_alert11"));
            return;
        }
        if(getJsLocaleMessage("rms","rms_myscene_rename") == renameStatus){
            $("#name_"+obj).attr("disabled",false);
            $("#rename_"+obj).text(getJsLocaleMessage("rms","rms_myscene_save"));
            return;
        }

        if(getJsLocaleMessage("rms","rms_myscene_save") == renameStatus){
            $("#name_"+obj).attr("disabled","");
        }

        $.ajax({
            type:"POST",
            url:baseUrl+"/rms_commTpl.htm?method=update",
            data:{id:obj,name:name,type:type},
            success:function(idstr){
                if(idstr !=="fault" && idstr !=getJsLocaleMessage("rms","rms_myscene_alert16")){
                    $("#name_"+obj).attr("disabled","disabled");
                    $("#rename_"+obj).text(getJsLocaleMessage("rms","rms_myscene_rename"));
                    alert(getJsLocaleMessage("rms","rms_myscene_succmodify"));
                    $("#option_"+obj).val(obj);
                    $("#option_"+obj).text(name);
                }else{
                    if(idstr =="fault" ){
                        alert(getJsLocaleMessage("rms","rms_myscene_failmodify"));
                    }else{
                        alert(idstr);
                        $("#name_"+obj).attr("disabled",false);
                    }
                }
            }
        });
    }


    function isShowChart(params){
        var _params = params || "";
        if(_params){
            //编辑
            $("#chartPopup").css({"display":""});
            $("#popup_div_1").css({"display":""});
            var dataJson = $.parseJSON(_params);
            var chartType  = dataJson.chartType;
            var chartTitle = dataJson.chartTitle;
            var ptType     = dataJson.ptType;
            $("#chartType").val(chartType);
            $("#chartTitle").val(chartTitle);
            if(ptType==1){
                $("#type1").prop("checked",true);
                $("#type2").prop("checked",false);
                $("#type3").prop("checked",false);
            }else if(ptType==2){
                $("#type2").prop("checked",true);
                $("#type1").prop("checked",false);
                $("#type3").prop("checked",false);
            }else{
                $("#type3").prop("checked",true);
                $("#type1").prop("checked",false);
                $("#type2").prop("checked",false);
            }
            var color      = dataJson.color;
            var rowValue   = dataJson.rowValue;
            //行名
            var barRowName = dataJson.barRowName;
            //列名
            var barColName = dataJson.barColName;
            //数值(以行为单位，用“,”隔开，换行则用“@”隔开)
            var barValue = dataJson.barValue;
            var barTableVal = dataJson.barTableVal;
            chartChange();
            toAction();
            if(chartType == 1){
                newPie(ptType,rowValue,barRowName,chartTitle);
            }else if(chartType == 2||chartType == 3){
                newBarOrLine(ptType,chartType,barTableVal,chartTitle);
            }

        }else{
            $("#chartType").val("");
            var $selectedLi = $("#chartType").next(".c_selectBox").find("ul.c_result li");
            $selectedLi.each(function () {
                var cont = $(this).html();
                if(cont === getJsLocaleMessage("rms","rms_myscene_piechart")){
                    $("#chartType").next(".c_selectBox").find("input.c_input").val(cont);
                }
            });
            //新增
            var ptType =1;
            $("#chartType").val(ptType);
            $("#chartTitle").val(getJsLocaleMessage("rms","rms_myscene_title"));
            $("#type1").prop("checked",true);
            $("#type2").prop("checked",false);
            $("#type3").prop("checked",false);
            $("#chartPopup").css({"display":""});
            $("#popup_div_1").css({"display":""});
            newPie(ptType,null,null,null);
            chartChange();
            toAction();

        }
    }
    //行业-用途 管理DIV关闭
    function closeDiv(){
        $("#industry_div").css({"display":"none"});
        $("#industry_div_1").css({"display":"none"});

        $("#use_div").css({"display":"none"});
        $("#use_div_1").css({"display":"none"});

    }
<%--     function quoteRmsTemplate(){
        $("#popup_div").css({"display":""});
        $("#tempFrames").css({"display":""});
        	var frameSrc = "<%=request.getContextPath()%>/rms_rmsSameMms.htm?method=getLfTemplateByMms&choosePublic=1";
          $("#tempFrame").attr("src",frameSrc);
          $("#tempFrames").dialog();
        $.ajax({
            type:"POST",
            url:"rms_rmsSameMms.htm?method=getLfTemplateByMms&choosePublic=1",
            async:true,
            success:function(result){
                console.log(result);
                $("#tempFrames").html(result);
            }
        });
    } --%>
    function quoteRmsTemplate(){
        $("#popup_div").css({"display":""});
        $("#tempFrames").css({"display":""});
    }
    $(".hoverClass").mouseover(function(){
        $(this).css("background-color","#f8faff");
        $(this).find(".nameClass").css("background-color","#f8faff");
    });
    $(".hoverClass").mouseout(function(){
        $(this).css("background-color","white");
        $(this).find(".nameClass").css("background-color","white");

    });
   //设置图片、音频、视频支持格式的提示
   $(function(){
   		document.getElementById("imageFile").setAttribute("title",getJsLocaleMessage("rms","rms_myscene_tips1"));
   		document.getElementById("audioFile").setAttribute("title",getJsLocaleMessage("rms","rms_myscene_tips2"));
   		document.getElementById("videoFile").setAttribute("title",getJsLocaleMessage("rms","rms_myscene_tips3"));

   });
   //计算档位和容量
	function calcDegreeAndSize(){
		//计算标题bytes
        var size = 0;
        var tmNameBytesSize = getStringByteSize($("#tmName").val());
		size += tmNameBytesSize;
        
       var contentSize = parseInt($("#contentSize").val()==""?0:$("#contentSize").val());
        size += contentSize;
        
        size = size%1024 ==0?parseInt(size/1024):(parseInt(size/1024)+1);
        var degree = getDegree(size);
        document.getElementById("degreeSize").innerHTML=size;
        document.getElementById("degree").innerHTML=degree;
	}
    //校验是否为空帧
    function checkEmptyFrame(fxHtml) {
        var flag = false;
        if(fxHtml === undefined || fxHtml.length <= 0){
            alert("场景内容不能为空,请重新编辑！");
            flag =  true;
        }
        $(".J-keyframe-content").each(function () {
            var divContent = $(this).find("div").html();
            var lengthOfAudio = $(this).find("audio").length;
            var lengthOfVideo = $(this).find("video").length;
            if((divContent === undefined || divContent === "") && (lengthOfAudio === 0 && lengthOfVideo === 0)){
                alert("模板内容中不允许空帧,请重新编辑！");
                flag = true;
                return false;
            }
        });
        return flag;
    }
    function changeChartToText() {
        $(".J-keyframe").each(function () {
            if($(this).attr("data-type") === "chart" && $(this).find(".J-chart-data").length === 0){
				//data-type == chart 并且没有class=J-chart-data的div则说明该帧为报表但没有报表数据
                $(this).attr("data-type","text");
			}
        });
		return $(".J-editor-content").html().replace(/<scirpt>/g,"&lt;scirpt&gt;").replace(/<\/scirpt>/g,"&lt;/scirpt&gt;");
    }
</script>
</body>
</html>
