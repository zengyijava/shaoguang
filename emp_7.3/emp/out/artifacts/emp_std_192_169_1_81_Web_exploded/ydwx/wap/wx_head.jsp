<%@page language="java" import="java.util.*" %>
<%@ page import="com.montnets.emp.common.constant.SystemGlobals" %>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String wxPath = basePath;
%>
<head>
<title> </title>
<meta name="viewport" content="width=device-width; initial-scale=1.0; minimum-scale=1.0; maximum-scale=1.0"/>
<meta name="MobileOptimized" content="240"/>
<meta name="apple-mobile-web-app-capable" content="yes" /> 
<meta name="apple-mobile-web-app-status-bar-style" content="black" /> 
<meta name="format-detection" content="telephone=no" />  
 <script type="text/javascript" src="<%=path%>/ydwx/wap/video/video.js"  charset="utf-8"></script> 
  	<script src="<%=path %>/common/js/myjquery-b.js"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/jqueryui/myjquery-j.js"></script>
	<script type="text/javascript" src="<%=path %>/common/js/common.js"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/video/swfobject.js"></script>
	<style type="text/css">
    #menuFrame{position:relative;}
    #bubbleMedia {
        display: none;
        position: absolute;
        top: 0%;
        left: 0%;
        width: 100%;
        height: 100%;
        z-index:99999;
        -moz-opacity: 1.0;
        opacity:.99;
        filter: alpha(opacity=100);
        background-color: black;
    }
</style>
  <script type="text/javascript">
    VideoJS.setupAllWhenReady();
    function showAudio(videoPath){
    	var bubbleMedia=document.getElementById('bubbleMedia');
    	var videoParams={
    		'route':'<%=path %>/common/widget/video/',
    		'width':180,
    		'height':360,
    		'fileUrl':videoPath,
    		'wrap':'bubbleMedia'
    	};
    	createVideoPreview(videoParams);
    	$('#video').remove();
    	$('p').remove();
    	$('video').remove();
    	$('#bubbleMedia').show();
        }
    
    function showSWF(path){
		var swfdiv="<embed isfakedvideo='' type='application/x-shockwave-flash' pluginspage='http://www.macromedia.com/go/getflashplayer' src="+path+" width='180' height='350' wmode='transparent' play='true' loop='false' menu='false' allowscriptaccess='never'>";
		$('#bubbleMedia').append(swfdiv);
    	$('#video').remove();
    	$('p').remove();
    	$('video').remove();
    	$('.video-js-box').remove();
		$('#bubbleMedia').show();
      }
  </script>

  <%-- Include the VideoJS Stylesheet --%>
  <link rel="stylesheet" href="<%=path%>/ydwx/wap/video/video-js.css" type="text/css" media="screen" title="Video JS"></link>

</head>  
	<body>
	
	<div class="bubbleContent" id="bubbleMedia">
	</div>
	
						 