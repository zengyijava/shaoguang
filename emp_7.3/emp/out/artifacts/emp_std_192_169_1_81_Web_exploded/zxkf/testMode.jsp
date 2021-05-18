<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.znly.biz.CustomTestMode"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	boolean isT = CustomTestMode.getTestMode();
	String userJson = (String)request.getAttribute("userJson");
	String appInfoJson = (String)request.getAttribute("appInfoJson");
//语言方面相关
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String isopen =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_58", request);
String noopen =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_59", request);
String open =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_60", request);
String close =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_61", request);	
String online =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_62", request);	
String offline =com.montnets.emp.i18n.util.MessageUtils.extractMessage("zxkf", "zxkf_chat_title_63", request);		
	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<%@include file="/common/common.jsp" %>
 	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<%}%>
    <title><emp:message key="zxkf_chat_title_46" defVal="调试发送页面" fileName="zxkf"/></title>
  </head>
  
  <body onload="checkMess()">
 <emp:message key="zxkf_chat_title_47" defVal=" 调试模式状态" fileName="zxkf"/>：<font id="mtest" color="green"><%=isT?isopen:noopen %></font>
  &nbsp;<input type="button" onclick="change(this);" ist="<%=isT?"0":"1" %>" value="<%=isT?close:open %>"/>
	
	<br/>
	<input type="radio" value="1" name="ptp" checked/><emp:message key="zxkf_chat_title_48" defVal=" 微信上行" fileName="zxkf"/>
	<input type="radio" value="6" name="ptp"/><emp:message key="zxkf_chat_title_49" defVal=" app上行" fileName="zxkf"/>
	<br/>
	fromUser:<input type= "text" id="fromUser" value="linzhhan"/><br/>
	toCusUser:<select id="toCusUser" style="width:154px"></select><br/>
	serverNum:<input type= "text" id="serverNum" value="lin-server"/><br/>
	msg:<input type= "text" id="msg" value="<emp:message key='zxkf_chat_title_50' defVal='发送消息' fileName='zxkf'/>"/><br/>
	name:<input type= "text" id="name" value="<emp:message key='zxkf_chat_title_51' defVal='测试员' fileName='zxkf'/>"/><br/>
	AId:<input type= "text" id="AId" value="10002"/><br/>
	<input type="button" value="<emp:message key='zxkf_chat_title_52' defVal='测试消息发送' fileName='zxkf'/>" onclick="sendwx()"/>
	<br/>
	<br/>
	<b><emp:message key="zxkf_chat_title_53" defVal=" APP客服流程测试" fileName="zxkf"/>-----------</b><br/>
	<emp:message key="zxkf_chat_title_54" defVal=" APP用户" fileName="zxkf"/>：<select id="appUser" style="width:154px"></select><br/>
	<emp:message key="zxkf_chat_title_55" defVal=" 发起APP消息" fileName="zxkf"/>:<input type= "text" id="appMsg" value="<emp:message key='zxkf_chat_title_56' defVal='APP消息' fileName='zxkf'/>"/><br/>
	<input type="button" value="<emp:message key='zxkf_chat_title_57' defVal='测试APP流程' fileName='zxkf'/>" onclick="sendApp()"/>
	
	<div id="messDiv" style="border:solid #999 2px;width:500px;height:250px;overflow:auto;position: absolute;top:20px;left:280px;"></div>
	<script src="<%=request.getContextPath()%>/wxcommon/js/myjquery-d.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/zxkf_<%=langName%>.js"></script>
	<script>
	var istt = <%=isT?"1":"0" %>;
	function sendApp()
	{
		var stype = "APPLC";
		var msg = $("#appMsg").val();
		var from = $("#appUser").val();
		var fromName = $("#appUser > option:selected").text();
		$.post("customChat.hts?method=sendApp",{
			fromUser: from,
			msg: msg,
			fromName:fromName
		},function(result){
			var message ="<span style='color:blue'>"+ (tkn+1)+"."+stype+"&lt;"+$("#appUser > option:selected").text()+"&gt;"+result+"<br/>"+msg;
			$("#messDiv").prepend(message+"<br/></span>");
		});
	}
	function sendwx()
	{
		var fromUser = $("#fromUser").val();
		var toCusUser = $("#toCusUser").val();
		var serverNum = $("#serverNum").val();
		var msg = $("#msg").val();
		var name = $("#name").val();
		var AId = $("#AId").val();
		var pushType = $("input:radio:checked").val();
		var stype = "WEI";
		if(pushType == 6)
		{
			AId = 0;
			stype = "APP";
		}
		$.post("customChat.hts?method=sendwx",{
			fromUser: fromUser,
			toCusUser: toCusUser,
			serverNum: serverNum,
			msg: msg,
			name: name,
			AId: AId,
			pushType: pushType
		},function(result){
			var message ="<span style='color:green'>"+ (tkn+1)+"."+stype+"&lt;"+fromUser+"&gt;-&lt;"+toCusUser+"&gt;"+result+"<br/>"+msg;
			$("#messDiv").prepend(message+"<br/></span>");
		});
	}
	var len=0;
	var tkn = <%=CustomTestMode.index%>;
	function checkMess()
	{
		if(istt == 1)
		{
			var tkn2 = tkn;
			$.post("customChat.hts?method=testMess",{len:len},function(result){
				if(tkn != tkn2)
				{
					return;
				}
				len = 0;
				var array=eval("("+result+")");
				for(var i=0;i<array.length;i=i+1)
				{
					tkn = parseInt(array[i].substring(0,array[i].indexOf(".")));
					$("#messDiv").prepend(array[i]+"<br/>");
					len = len +1;
				}
				checkMess();
			});
		}
	}
	
	function change(obj)
	{
		var ist = $(obj).attr("ist");
		$.post("customChat.hts?method=changeMode",{ist:ist},function(){
			istt = ist;
			if(ist == 1)
			{
				$("#mtest").text(getJsLocaleMessage('zxkf','zxkf_chat_title_58'));
				$(obj).attr("ist",0);
				$(obj).val(getJsLocaleMessage('zxkf','zxkf_chat_title_60'));
			}else
			{
				$("#mtest").text(getJsLocaleMessage('zxkf','zxkf_chat_title_59'));
				$(obj).attr("ist",1);
				$(obj).val(getJsLocaleMessage('zxkf','zxkf_chat_title_61'));
			}
			checkMess();
		});
	}
	$(document).ready(function(){
		var userInfoJson = <%=userJson%>;
		if(userInfoJson){
			for(var key in userInfoJson){//页面初始化加载客户列表
			    var data=userInfoJson[key];
				var customeId=data.customeId,
					name=data.name,
					state=data.state;
				var ht = "<option value='"+customeId+"'>"+name+"["+(state==1?<%=online%>:<%=offline%>)+"]</option>";
				$("#toCusUser").append(ht);
			}
		}
		var appJson = <%=appInfoJson%>;
		if(appJson){
			for(var uk in appJson){//页面初始化加载客户列表
				 var data=appJson[uk];
					for(var key in data){
				var customeId=data[key].appcode,
					name=data[key].name;
				var ht = "<option value='"+customeId+"'>"+name+"</option>";
				$("#appUser").append(ht);
					}
			}
		}
	});
	</script>
  </body>
</html>
