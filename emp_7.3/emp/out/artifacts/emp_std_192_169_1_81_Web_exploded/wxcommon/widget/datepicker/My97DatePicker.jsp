<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String language=(String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
<head>
<meta http-equiv="content-type" content="text/xml; charset=utf-8" />
<title>My97DatePicker</title>
<script type="text/javascript" src="config.js"></script>
<script>
if(parent==window)
	location.href = 'http://www.my97.net';
var $d, $dp, $pdp = parent.$dp, $dt, $tdt, $sdt, $IE=$pdp.ie, $FF = $pdp.ff,$OPERA=$pdp.opera, $ny, $cMark = false;
if ($pdp.eCont) {
	$dp = {};
	for (var p in $pdp) {
		$dp[p] = $pdp[p];
	}
}
else
	$dp = $pdp;
	
$dp.getLangIndex = function(name){
	var arr = langList;
	for (var i = 0; i < arr.length; i++) {
		if (arr[i].name == name) {
			return i;
		}
	}
	return -1;
}

$dp.getLang = function(name){
	var index = $dp.getLangIndex(name);
	if (index == -1) {
		index = 0;
	}
	return langList[index];
}

$dp.lang='<%=language%>';
$dp.realLang = $dp.getLang($dp.lang);
document.write("<script src='lang/" + $dp.realLang.name + ".js' charset='" + $dp.realLang.charset + "'><\/script>");
document.write('<link rel="stylesheet" type="text/css" href="<%=skin%>/datePicker/datepicker.css" title="' + skinList[0].name + '" charset="' + skinList[0].charset + '" />');
function keydown(e) 
{        
	var currKey=0,e=e||event;
	if(e.keyCode==13) return false;  
}
 document.onkeydown=keydown;
</script>
<script type="text/javascript" src="calendar.js"></script>
</head>
<body leftmargin="0" topmargin="0" onload="$c.autoSize()" tabindex=0>
</body>
</html>
<script>new My97DP();</script>