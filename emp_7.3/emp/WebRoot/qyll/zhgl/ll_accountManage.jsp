<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Calendar" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
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
	String menuCode = titleMap.get("accountManage");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String langName = (String)session.getAttribute("emp_lang");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title>ll_accountManage.htm</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css" />
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/accountManage.css"/>  
		<style type="text/css">
			<%if(StaticValue.ZH_HK.equals(langName)){%>
				.btnClass5,.btnClass6{
				    letter-spacing: 0px;
				}
				tr td:first-child{
					text-indent:20px;
					letter-spacing:0px;
				}
			<%}%>
		</style>
	</head>
	<body>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName,menuCode) %>
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="ll_accountManage.htm?method=find" method="post" id="pageForm">
					<div class="ll_thead" style="display: none;"><emp:message key="qyll_common_163" defVal="账号配置(*所在行表示必填项)" fileName="qyll"></emp:message></div>
					<table>
						<tr>
							<td><emp:message key="qyll_common_164" defVal="企业名称" fileName="qyll"></emp:message>：</td>
							<td><input type="text" value="${llCompInfoBean.ecName}" id="ecName" name="ecName"></td>
							<td id="ecNameError"><span>*</span><emp:message key="qyll_common_165" defVal="请与流量提供商录入的企业名称保持一致" fileName="qyll"></emp:message></td>
						</tr>
						<tr>
							<td><emp:message key="qyll_common_166" defVal="企业编号" fileName="qyll"></emp:message>：</td>
							<td><input type="text" value="${llCompInfoBean.corpCode}" id="ecId" name="ecId"></td>
							<td id="ecIdError"><span>*</span><emp:message key="qyll_common_167" defVal="请按流量提供商提供的编号录入" fileName="qyll"></emp:message></td>
						</tr>
						<tr>
							<td><emp:message key="qyll_common_168" defVal="通讯密钥" fileName="qyll"></emp:message>：</td>
							<td><input type="password" value="${llCompInfoBean.password}" id="password" name="password"></td>
							<td id="pwdError"><span>*</span><emp:message key="qyll_common_169" defVal="请按流量提供商提供的通讯密钥录入" fileName="qyll"></emp:message></td>
						</tr>
						<tr>
							<td><emp:message key="qyll_common_170" defVal="接口地址" fileName="qyll"></emp:message>：</td>
							<td>
								<div class="ipinput_div">
									<input type="text" id="ipOne" class="ipinput_input" maxlength="3" value="${llCompInfoBean.ipOne}"/>
									<span class="ipinput_separator">.</span>
									<input type="text" id="ipTwo" class="ipinput_input" maxlength="3" value="${llCompInfoBean.ipTwo}"/>
									<span class="ipinput_separator">.</span>
									<input type="text" id="ipThree" class="ipinput_input" maxlength="3" value="${llCompInfoBean.ipThree}"/>
									<span class="ipinput_separator">.</span>
									<input type="text" id="ipFour" class="ipinput_input" maxlength="3" value="${llCompInfoBean.ipFour}"/>
									<input type="button" value="x" id="clean" class="ipinput_input" onclick="clearDiv();"  style="background-color: white;border: 0px;"
								</div>
							</td>
							<td id="ipError"><span>*</span><emp:message key="qyll_common_171" defVal="请按流量提供商提供的信息录入" fileName="qyll"></emp:message></td>
						</tr>
						<tr>
							<td><emp:message key="qyll_common_172" defVal="端口" fileName="qyll"></emp:message>：</td>
							<td><input type="text" id="port" name="port" value="${llCompInfoBean.port}"></td>
							<td id="portError"><span>*</span><emp:message key="qyll_common_171" defVal="请按流量提供商提供的信息录入" fileName="qyll"></emp:message></td>
						</tr>
						<tr>
							<td><emp:message key="qyll_common_173" defVal="推送地址" fileName="qyll"></emp:message>：</td>
							<td><input type="text"  id="pushAddr" name="pushAddr" value="${llCompInfoBean.pushAddr}"></td>
							<td id="pushError"><span>*</span><emp:message key="qyll_common_174" defVal="请按流量提供商提供的推送地址录入" fileName="qyll"></emp:message></td>
						</tr>
						<tr>
							<td><emp:message key="qyll_common_175" defVal="备注" fileName="qyll"></emp:message>：</td>
							<td colspan="2">
								<textarea rows="5" cols="45" id="reMark" name="reMark" style="width:267px;">${llCompInfoBean.reMark=='null'?'':llCompInfoBean.reMark}</textarea>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<input type="button" class="btnClass5 mr24" value="<emp:message key='qyll_common_176' defVal='测试连接' fileName='qyll'></emp:message>"  onclick="Connection(0);">
								<input type="button" class="btnClass5 mr23" value="<emp:message key='qyll_common_177' defVal='保存' fileName='qyll'></emp:message>" onclick="Connection(1);">
								<input type="button" class="btnClass6" value="<emp:message key='qyll_common_26' defVal='取消' fileName='qyll'></emp:message>" onclick="cancel();">
							</td>
						</tr>
					</table>
					
				</form>
			</div>
		</div>
   		<div class="clear"></div>
   		
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script> 
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js"></script>
		<script type="text/javascript" src="<%=iPath %>/js/ipInput.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/qyll_<%=langName%>.js"></script>
		<script type="text/javascript">
		$('input[name=aucIpAddr]').ipinput();
		
		function Connection(ident){
			var ecName = $("#ecName").val();
			var ecId = $("#ecId").val();
			var password = $("#password").val();
			var ip = $.trim($("#ipOne").val())+"."+$.trim($("#ipTwo").val())+"."+$.trim($("#ipThree").val())+"."+$.trim($("#ipFour").val());
			var pushAddr = $("#pushAddr").val();
			var reMark = $("#reMark").val()==""?"null":$("#reMark").val();
			var port = $("#port").val();
			//校验数字
			reg=/^[0-9]*$ /;
			var strRegex='^(http|https|ftp)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]';
			var regs=new RegExp(strRegex); 
			if($.trim(ecName)==""){
				$("#ecNameError").css('color','red');
				return;
			}
			if($.trim(ecId)==""){
				$("#ecIdError").css('color','red');
				return;
			}
			if(password==""){
				$("#pwdError").css('color','red');
				return;
			}
			if($.trim(pushAddr)==""||!regs.test($.trim(pushAddr))){
				$("#pushError").css('color','red');
				return;
			}
			if($.trim(port)==""||reg.test($.trim(port))){
				$("#portError").css('color','red');
				return;
			}
			$.ajax({
				type:"POST",
				url:"ll_accountManage.htm?method=toConnection",
				data:{
					ecName:ecName,
					ecId:ecId,
					password:password,
					ip:ip,
					port:port,
					pushAddr:pushAddr,
					reMark:reMark,
					ident:ident
				},
				beforeSend:function () {
                    page_loading();
                },
                complete:function () {
               	  	page_complete();
                },
                success:function (result) {
                	 if (result == 'true') {
                        alert(getJsLocaleMessage("qyll","qyll_lldg_alter_132"));
                   	 } else if(result == 'false'){
                       	alert(getJsLocaleMessage("qyll","qyll_lldg_alter_133"));
                     }else if(result=="1"){
                     	alert(getJsLocaleMessage("qyll","qyll_lldg_alter_134"));
                     }else if(result=="0"){
                     	alert(getJsLocaleMessage("qyll","qyll_lldg_alter_135"));
                     }
                }
			});
			
		}
		function cancel(){
			$("#ecName").val("");
			$("#ecId").val("");
			$("#password").val("");
			$("#ipOne").val("");
			$("#ipTwo").val("");
			$("#ipThree").val("");
			$("#ipFour").val("");
			$("#pushAddr").val("");
			$("#reMark").val("");
			$("#port").val("");
		}
		$('#ecName').focus(function(){
        	$("#ecNameError").css('color','#cccccc');
        });
        $('#ecId').focus(function(){
        	$("#ecIdError").css('color','#cccccc');
        });
        $('#password').focus(function(){
        	$("#pwdError").css('color','#cccccc');
        });
        $('#pushAddr').focus(function(){
        	$("#pushError").css('color','#cccccc');
        });
        $('#port').focus(function(){
        	$("#portError").css('color','#cccccc');
        });
		</script>
	</body>
</html>
