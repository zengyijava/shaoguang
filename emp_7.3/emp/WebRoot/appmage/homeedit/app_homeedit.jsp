<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("homeedit");
menuCode = menuCode==null?"0-0-0":menuCode;
String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
String sid = StringUtils.defaultString(request.getParameter("sid"),"");
// 0 新增  1 修改  2 复制
String type = StringUtils.defaultString(request.getParameter("type"),"0");
String validity = (String)request.getAttribute("validity");
Object appObject = request.getAttribute("appCode");
String appCode = appObject==null?"":appObject.toString();

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=iPath %>/css/pageIndexEdit.css?V=<%=StaticValue.getJspImpVersion() %>">
	<%if(StaticValue.ZH_HK.equals(empLangName)){
	%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" href="<%=iPath %>/css/homeedit_ch_HK.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%} else if(StaticValue.ZH_TW.equals(empLangName)) {
	%>		
		<link rel="stylesheet" href="<%=iPath %>/css/homeedit_ch_TW.css?V=<%=StaticValue.getJspImpVersion() %>">
	<%} %>
  </head>
  
  <body>
  <div id="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
   				<div class="appIndexWrap">
	  <form action="">
	  	<div id="loginInfo" class="hidden"></div>
	  	<input id="sid" type="hidden" value="<%=sid %>"/>
	  	<input id="oppType" type="hidden" value="<%=type %>"/>
	  	<input id="appCode" type="hidden" value="<%=appCode %>"/>
	  	<div>
	  		<div style="width: 400px;margin-left: 20px;float:left;">
	  			<emp:message key="appmage_xxfb_appsyfb_text_indexname" defVal="首页名称：" fileName="appmage"></emp:message>
	  			<input id="infoname" placeholder="<emp:message key="appmage_xxfb_appsyfb_text_tishiyu" defVal="便于识别，不作为首页内容发送" fileName="appmage"></emp:message>" class="input_bd div_bd" type="text" style="width: 300px;" maxlength="25">
	  		</div>
	  		<div style="margin-right: 20px;text-align: right;">
	  			<emp:message key="appmage_xxfb_appsyfb_text_validperiod" defVal="有效期" fileName="appmage"></emp:message>：
	  		<input type="text" style="cursor: pointer; width: 178px; background-color: white;"
			 id="validity" name="validity" class="Wdate validity input_bd div_bd" readonly="readonly" value=""/>
	  		</div>
	  	</div>
		<%--内容编辑区--%>
		<div class="pageEditArea div_bd" data-editArea="{pos:'0',role:'largeArticle'}">
			<div class="formArea">
				<div class="fieldArea">
					<div class="largePic" id="largePic">
						<ul>
							<li class="cur"><emp:message key="appmage_xxfb_appsyfb_text_image1" defVal="大图一" fileName="appmage"></emp:message></li>
						</ul>
						<a href="javascript:void(0)" id="addPic"></a>
						<div class="bd_line2 div_bd"></div>
					</div>
					<div class="listArea">
						<span class="field_tit" id="newsTitle"><emp:message key="appmage_xxfb_appsyfb_text_bigtitle" defVal="大标题：" fileName="appmage"></emp:message></span>
						<div class="field_bd">
							<input type="text" name="" class="input_bd div_bd" id="sendTitle">
							<span id="countTit">0</span>/25
						</div>
					</div>
					
					<div class="largeContent">
						<div class="listArea">
							<span class="field_tit" id="newsPic"><emp:message key="appmage_xxfb_appsyfb_text_bigimage" defVal="大图片：" fileName="appmage"></emp:message></span>
							<div class="field_bd">
								<div class="picPreview" id="thumbView">
									<img src="" alt="">
								</div>
								<div class="uploadbtn">
									<input type="button" value="<emp:message key="appmage_xxfb_appsyfb_opt_liulan" defVal="浏览" fileName="appmage"></emp:message>" size="30" class="btnClass2" id="uploadPic">
									<input type="file" id="uploadFile" name="uploadfile" class="files" onchange="upload(this);">
								</div>
								<p class="picDesc"><emp:message key="appmage_xxfb_appsyfb_text_format" defVal="格式：jpg、jpeg、bmp、png" fileName="appmage"></emp:message><br /><emp:message key="appmage_xxfb_appsyfb_text_size" defVal="大小：建议360像素*200像素" fileName="appmage"></emp:message></p>
							</div>
						</div>
						<div class="listArea" style="display: none;">
							<span class="field_tit"><emp:message key="appmage_xxfb_appsyfb_text_content" defVal="正文内容" fileName="appmage"></emp:message>：</span>
							<div class="field_bd">
								<textarea name="" id="sendContent" cols="30" rows="10"></textarea>
								<p><span id="countCont">0</span>/1000</p>
							</div>
						</div>
					</div>
					<div class="listArea">
						<span class="field_tit"><emp:message key="appmage_xxfb_appsyfb_text_url" defVal="链接URL：" fileName="appmage"></emp:message></span>
						<div class="field_bd">
							<input type="text" name="" class="input_bd div_bd" id="linkUrl" maxlength="150">
							<span style="color: #cccccc;display: block;line-height: 20px;"><emp:message key="appmage_xxfb_appsyfb_text_urlformat" defVal="格式如http://192.169.1.155:8080/emp/details.html" fileName="appmage"></emp:message></span>
						</div>
					</div>
					
					<input type="hidden" id="newsId" value="">
				</div>
			</div>
			<b class="appArrow appArrowOut div_bd" data-left="-13"></b>
			<b class="appArrow appArrowinner div_bd" data-left="-11"></b>
		</div>
		<%--效果展示区--%>
		<div class="effectArea effectIndex" id="effectIndex">
			<div class="mobileModel">
				<div class="effectBox">
					<%--新闻列表--%>
					<div id="heads" class="effInner">
						<div class="sliderBox" id="sliderBox" data-config="{role:'largeArticle'}">
							<div class="piclist cur" data-config="{'title':'','content':'','order':0,
							'url':'','newsId':'','thumb':'','pic':'','role':'largeArticle'}">
								<span class="alt"><emp:message key="appmage_xxfb_appsyfb_text_bigimage1" defVal="封面大图一" fileName="appmage"></emp:message></span>
								<img src="" alt="" class="thumb" style="display:none;">
								<div class="pictit"><emp:message key="appmage_xxfb_appsyfb_text_title" defVal="标题" fileName="appmage"></emp:message></div>
								<div class="hoverStatus">
									<div class="statusInner">
										<b class="edit" title="<emp:message key="appmage_common_opt_bianji" defVal="编辑" fileName="appmage"></emp:message>"></b>
										<b class="delete" title="<emp:message key="appmage_common_opt_shanchu" defVal="删除" fileName="appmage"></emp:message>"></b>
									</div>
								</div>
							</div>
						</div>
						<div class="bd_line"></div>
						<ul id="newsList">
							<li data-config="{'newsId':'','title':'','content':'','order':0,'thumb':'','pic':'','url':'','role':'listArticle'}" class="modList">
								<p class="artTitle"><emp:message key="appmage_xxfb_appsyfb_text_title" defVal="标题" fileName="appmage"></emp:message></p>
								<p class="artContent"><emp:message key="appmage_xxfb_appsyfb_text_content" defVal="正文内容" fileName="appmage"></emp:message></p>
								<img width="50" src="<%=iPath %>/img/thumb_<%=empLangName %>.gif"  class="thumb">
								<div class="hoverStatus">
									<div class="statusInner">
										<b class="edit" title="<emp:message key="appmage_common_opt_bianji" defVal="编辑" fileName="appmage"></emp:message>"></b>
										<b class="delete" title="<emp:message key="appmage_common_opt_shanchu" defVal="删除" fileName="appmage"></emp:message>"></b>
										<b class="moveDown" title="<emp:message key="appmage_common_opt_up" defVal="下移" fileName="appmage"></emp:message>"></b>
										<b class="moveUp" title="<emp:message key="appmage_common_opt_down" defVal="上移" fileName="appmage"></emp:message>"></b>
									</div>
								</div>
							</li>
							<li data-config="{'newsId':'','title':'','content':'','order':1,'thumb':'','pic':'','url':'','role':'listArticle'}" class="modList">
								<p class="artTitle"><emp:message key="appmage_xxfb_appsyfb_text_title" defVal="标题" fileName="appmage"></emp:message></p>
								<p class="artContent"><emp:message key="appmage_xxfb_appsyfb_text_content" defVal="正文内容" fileName="appmage"></emp:message></p>
								<img width="50" src="<%=iPath %>/img/thumb_<%=empLangName %>.gif" class="thumb">
								<div class="hoverStatus">
									<div class="statusInner">
										<b class="edit" title="<emp:message key="appmage_common_opt_bianji" defVal="编辑" fileName="appmage"></emp:message>"></b>
										<b class="delete" title="<emp:message key="appmage_common_opt_shanchu" defVal="删除" fileName="appmage"></emp:message>"></b>
										<b class="moveDown" title="<emp:message key="appmage_common_opt_up" defVal="下移" fileName="appmage"></emp:message>"></b>
										<b class="moveUp" title="<emp:message key="appmage_common_opt_down" defVal="上移" fileName="appmage"></emp:message>"></b>
									</div>
								</div>
							</li>
							<li id="addNewsMod">
								<b class="addNews" title="<emp:message key="appmage_xxfb_appsyfb_opt_addimage" defVal="添加新图文" fileName="appmage"></emp:message>"></b>
							</li>

						</ul>
					</div>
				</div>
			</div>
		</div>

		<div class="btnGroup">
			<input style="margin-left: 260px;" type="button" value='<emp:message key="appmage_common_opt_zancun" defVal="暂存" fileName="appmage"></emp:message>' class="btnClass5 mr5 cache">
			<input type="button" value='<emp:message key="appmage_common_opt_baocun" defVal="保存" fileName="appmage"></emp:message>' class="btnClass5 mr5 save">
			<input type="button" value='<emp:message key="appmage_common_opt_fabu" defVal="发布" fileName="appmage"></emp:message>' class="btnClass5 mr5">
			<input type="button" value='<emp:message key="appmage_common_opt_fanhui" defVal="返回" fileName="appmage"></emp:message>' onclick="back();" class="btnClass6"><br/>
		</div>
		</form>
	</div>
	
			</div>
	</div>
	<img id="uptip" alt="<emp:message key="appmage_xxfb_appsyfb_text_isupload" defVal="正在上传中..." fileName="appmage"></emp:message>" style="display:none;position: absolute;top:50px;left:460px;" src="common/img/mobilePreview/load.gif">
	<script>
		var iPath="<%=iPath %>";
	</script>
	<script src="<%=commonPath %>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=iPath %>/js/uploadify/jquery.uploadify.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/jquery.ui.widget.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/jquery.fileupload.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/json2.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/wdate_extend.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=iPath %>/js/jquery.InputLetter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
	<script src="<%=iPath %>/js/appEditPic.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=iPath %>/js/jquery.placeholder.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=iPath %>/js/home.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$('#validity').val("<%=validity%>");
			//解决IE下不支持placeholder
			if($.browser.msie) { 
			$(":input[placeholder]").each(function(){
				$(this).placeholder();
			});
			};
			});
	</script>
  </body>
</html>
