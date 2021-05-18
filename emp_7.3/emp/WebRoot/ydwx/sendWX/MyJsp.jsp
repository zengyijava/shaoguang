<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<meta charset="UTF-8">
	<title></title>
	<link rel="stylesheet" href="css/weiEditor.css"/>
	<script type="text/javascript" src="js/myjquery-d.js"></script>
	 <%--[if IE 6]><script type="text/javascript" src="js/DD_belatedPNG.js"></script>
	 <script type="text/javascript">
	 DD_belatedPNG.fix(".content a");
	 </script>
	 <![endif]--%>
</head>
<body>
	<div id="container_wrapper">
	  <div id="main" class="container">
	  <div class="container_inner">
	    <div class="boxHeader">
		  <h2>图文消息</h2>
		</div>
		<div class="content clearfix">
		  <div class="msg-edit over clearfix">
		     <div class="left msg-preview">
			    <div class="msg-item-wrapper">
				  <div id="appmsgItem1" class="appmsgItem" data-rid="1" onmouseover="appmsgItemOver(this)" onmouseout="appmsgItemOut(this)">
				    <p class="msg-meta">
					  <span class="msg-date">2013-08-09</span>
					</p>
					<div class="cover">
					  <p class="default-tip">封面图片</p>
					  <h4 class="msg-t"><span class="i-title">标题</span></h4>
					  <div class="sub-msg-opa tc ">
					   <div class="icostyle">
						 <a href="javascript:;" class="tc icon18 iconEdit first" onclick="iconeditClick(this)"></a>
					   </div>	 
					  </div>
					</div>
				  </div>
				  <div id="appmsgItem2" class="appmsgItem sub-msg-item rel" data-rid="2"  onmouseover="appmsgItemOver(this)" onmouseout="appmsgItemOut(this)">
				    <span class="thumb">
					  <span class="default-tip">缩略图</span>
					  <img src="" class="i-img" style="display:none;"/>
					</span>
				    <h4 class="msg-t"><span class="i-title">标题</span></h4>
					<div class="sub-msg-opa tc ">
					   <div class="icostyle">
						 <a href="javascript:;" class="tc icon18 iconEdit first" onclick="iconeditClick(this)"></a>
						 <a href="javascript:;" class="tc icon18 iconDel" onclick="appmsgItemRemove(this)"></a>
					   </div>	 
					  </div>
				  </div>
				  <div id="appmsgItem3" class="appmsgItem sub-msg-item rel" data-rid="3"  onmouseover="appmsgItemOver(this)" onmouseout="appmsgItemOut(this)">
				    <span class="thumb">
					  <span class="default-tip">缩略图</span>
					  <img src="" class="i-img" style="display:none;"/>
					</span>
				    <h4 class="msg-t"><span class="i-title">标题</span></h4>
					<div class="sub-msg-opa tc ">
					   <div class="icostyle">
						 <a href="javascript:;" class="tc icon18 iconEdit first" onclick="iconeditClick(this)"></a>
						 <a href="javascript:;" class="tc icon18 iconDel" onclick="appmsgItemRemove(this)"></a>
					   </div>	 
					  </div>
					
				  </div>
				  <div class="sub-add">
				    <a href="javascript:;" class="sub-btn-add block tc">
					  <span class="sub-add-icon vm dib"></span>
					  增加一条
					</a>
				  </div>
				
				</div>
			 </div>
			 <div class="msg-edit-area" id="msg-edit-area" style="margin-top:33px;">
			   <div class="rel msg-editer-wrapper">
			     <div class="msg-editer">
			       <label for="" class="block">标题</label>
				   <input type="text" class="msg-input" id="title" value="">
				   <label for="" class="block">作者<em class="mp_desc">（选填）</em></label>
				   <input type="text" class="msg-input" id="author" value="">
				   <label for="" class="block"><span id="upload-tip" class="upload-tip r">大图片建议尺寸：720像素 * 400像素</span>封面</label>
				   <div class="cover-area">
				     <div class="cover-hd">
				       <a href="javascript:;" class="icon28C upload-btn">上传</a>
					 </div>  
				   </div>
				   <label for="" class="block">正文</label>
				   <a id="url-block-link" style="padding-top: 10px; display: none;" href="javascript:(function(){jQuery('#url-block').show();jQuery('#url-block-link').hide()})()" class="url-link block">添加原文链接</a>
				   <div id="url-block" class="none" style="display: block;">       
				     <label for="" class="block">原文链接</label>         
				     <input type="text" class="msg-input" id="url" value="">       
				   </div>
				 </div>  
			     <span class="abs msg-arrow a-out" style="margin-top: 0px;"></span>
				 <span class="abs msg-arrow a-in" style="margin-top: 0px;"></span>
			   </div>
			 </div>
		  </div>
		  <p class="tc msg-btn">
		    <a href="javascript:;" id="previewAppMsg" class="btnGreen">发送预览</a>
			<a href="javascript:;" id="save" class="btnGreen">完成</a>
		  </p>
		</div>
	  </div>
	 </div> 
	</div>
	<script type="text/javascript" src="js/weieditor.js"></script>
</body>
</html>
