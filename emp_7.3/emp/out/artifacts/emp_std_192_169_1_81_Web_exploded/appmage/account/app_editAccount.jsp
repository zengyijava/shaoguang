<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="java.util.List"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@page import="com.montnets.emp.entity.appmage.LfAppAccount"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@include file="/common/common.jsp" %>
<%
	String path = request.getContextPath();
   String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map	
	LfAppAccount account = (LfAppAccount)request.getAttribute("account");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	String result=(String)request.getAttribute("result");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	
%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=inheritPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=iPath %>/css/par_indexConfig.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
<%-- header结束 --%>
<body onload="show();">
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
			<input type="hidden" name="pathUrl" id="pathUrl" value="<%=path %>">
			<form action="<%=path%>/app_acctmanager.htm?method=Add" method="post" id="trustForm" enctype="multipart/form-data">
				<input id="path" name="path" type="hidden" value="<%=path%>"/>
				<input type="hidden" name="tmId" id="tmId" value="<%=request.getAttribute("tmId") %>">
				<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getAttribute("lguserid") %>"/>
				<input type="hidden" name="lgcorpcode" id="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>">
				<table border="0" cellspacing="0" id="sysTable" style="margin-top:10px;" >
					<tbody>
						<tr >
							<td align="right"><emp:message key="appmage_jcsj_yhgl_text_busname" defVal="企业名称" fileName="appmage"></emp:message>：</td>
							<td align="left" id="left">
								<input id="name" name="name" class="input_bd" style="width:200px;"
									type="text" maxlength="20" value="<%=account.getName()==null?"":account.getName()%>"/>
									<font color="red">*</font><span style="color:gray;vertical-align:middle;">&nbsp;&nbsp;<emp:message key="appmage_jcsj_yhgl_text_enterbusname" defVal="请与APP提供商提供的企业名称录入。" fileName="appmage"></emp:message></span>
							</td>
						</tr>
						<tr >
							<td align="right"><emp:message key="appmage_jcsj_yhgl_text_buscode" defVal="企业编码" fileName="appmage"></emp:message>：</td>
							<td align="left" id="left">
								<input id="fakeid" name="fakeid" class="input_bd" style="width:200px;"
									type="text" maxlength="15"  value="<%=account.getFakeid()==null?"":account.getFakeid()%>"/>
							<font color="red">*</font><span style="color:gray;vertical-align:middle;">&nbsp;&nbsp;<emp:message key="appmage_jcsj_yhgl_text_enterbuscode" defVal="请按APP提供商提供的企业编码录入。" fileName="appmage"></emp:message></span>
							</td>
						</tr>

						<tr >
							<td align="right"><emp:message key="appmage_jcsj_yhgl_text_appaccount" defVal="APP帐号" fileName="appmage"></emp:message>：</td>
							<td align="left" id="left">
								<input id="code" name="code" class="input_bd" style="width:200px;"
									type="text" maxlength="20" value="<%=account.getCode()==null?"":account.getCode()%>"/>
									<font color="red">*</font><span style="color:gray;vertical-align:middle;">&nbsp;&nbsp;<emp:message key="appmage_jcsj_yhgl_text_enteraccount" defVal="请按APP提供商提供的APP帐号录入。" fileName="appmage"></emp:message></span>
							</td>
						</tr>
						
							<tr >
							<td align="right"><emp:message key="appmage_jcsj_yhgl_text_accandpwd" defVal="账号密码" fileName="appmage"></emp:message>：</td>
							<td align="left" id="left">
								<input id="password" name="password" class="input_bd" style="width:200px;"
									type="password" maxlength="20" value="<%=account.getPwd()==null?"":account.getPwd()%>"/>
									<font color="red">*</font><span style="color:gray;vertical-align:middle;">&nbsp;&nbsp;<emp:message key="appmage_jcsj_yhgl_text_enterpwd" defVal="请按APP提供商提供的帐号密码录入。" fileName="appmage"></emp:message></span>
							</td>
						</tr>
							<tr >
							<td align="right"><emp:message key="appmage_jcsj_yhgl_text_serverpath" defVal="文件服务器地址" fileName="appmage"></emp:message>：</td>
							<td align="left" id="left">
								<input id="fileSvrUrl" name="fileSvrUrl" class="input_bd" style="width:200px;"
									type="text"  maxlength="200" value="<%=account.getFileSvrUrl()==null?"":account.getFileSvrUrl()%>"/>
									<font color="red">*</font><span style="color:gray;vertical-align:middle;">&nbsp;&nbsp;<emp:message key="appmage_jcsj_yhgl_text_enterserverpath" defVal="请按APP提供商提供的文件服务器地址录入。" fileName="appmage"></emp:message></span>
							</td>
						</tr>
						<tr >
										<td align="right">
											<span><emp:message key="appmage_jcsj_yhgl_text_ipandport" defVal="通讯IP及端口" fileName="appmage"></emp:message>：</span>
										</td>
										<td>
											<div id="spip">
												<input type=text name="ip" id="ip" class="input_bd" style="width:140px;" value="<%=account.getUrl()==null?"":account.getUrl()%>">：
												<input type=text name="port" id="port" class="input_bd" style="width:40px;" onkeyup= "numberControl($(this))"  onblur="numberControl($(this))" maxlength="4" value="<%=account.getPort()==null?"":account.getPort()%>">
											<font color="red">*</font><span style="color:gray;vertical-align:middle;">&nbsp;&nbsp;<emp:message key="appmage_jcsj_yhgl_text_enteripandport" defVal="请按APP提供商提供的通讯IP及端口录入。" fileName="appmage"></emp:message></span>
											</div>
										</td>

						</tr>
						<tr>
						<td align="right"> <emp:message key="appmage_jcsj_yhgl_text_uploadheadimage" defVal="上传头像" fileName="appmage"></emp:message>：</td>
                              <td>
                              <div class="showImg  div_bd div_bg">
			    			<div id="main_pre">
			    				<div class="img"><span>LOGO</span><img style="vertical-align:middle;" src="<%=inheritPath + "/appmage/account/img/default_acct.png" %>" /></div>
			    			</div>
			    		</div>
                            <div class="uploadLogo">
			    			<input type="hidden" id="txt" name="txt" class="w_input inputSpec" style="margin-right:5px;">
			    			<input type="button" value="<emp:message key="appmage_xxfb_appsyfb_opt_liulan" defVal="浏览" fileName="appmage"></emp:message>" size="30" class="btnClass2">
			    			<input type="file" id="logo_img"  name="logo_img" style="height:26px;" class="files">
			    			<input type="hidden" name="company_logo" value=""/>
			    			<p class="file_tips"><emp:message key="appmage_jcsj_yhgl_text_imageformat" defVal="图片格式：jpg、jpeg、gif、bmp、png" fileName="appmage"></emp:message><br/><emp:message key="appmage_jcsj_yhgl_text_imagesize" defVal="图片尺寸：50像素*50像素" fileName="appmage"></emp:message></p>
			    		</div>
                       </td>         
						</tr>
						<tr >
							<td align="right"><emp:message key="appmage_jcsj_yhgl_text_remark" defVal="备注" fileName="appmage"></emp:message>：</td>
							<td align="left" id="left">
								<textarea rows="3" cols="10" style="width:200px;margin-top:5px;" name="contents" id="contents" onblur="eblur($(this))" ><%=account.getInfo()==null?"":account.getInfo()%></textarea>
							
							</td>
						</tr>
						<tr>
						<td>
						</td>
							<td  align="left">
								<font id="waitTextConnection" color="#808080"></font>
								<font id="rightTextConnection" color="green"></font>
								<font id="errorTextConnection" color="red"></font>
						</td>
					</tr>
					</tbody>
				</table>
				<div style="margin:10px 0 0 300px;">
				<input type="button"  value="<emp:message key="appmage_jcsj_yhgl_text_testconn" defVal="测试连接" fileName="appmage"></emp:message>" class="btnClass5 btnLetter4 indent_none"  onclick="javascript:testConn();"/>&nbsp;&nbsp;
				<input type="button" id="save" class="btnClass5"  value="<emp:message key="appmage_common_opt_baocun" defVal="保存" fileName="appmage"></emp:message>" onclick='saveData()'>&nbsp;&nbsp;
				<input type="button" class="btnClass5"  value="<emp:message key="appmage_common_opt_fanhui" defVal="返回" fileName="appmage"></emp:message>" onclick="javascript:showMain();" >
			</div>
			</form>

		</div>
			<%-- 内容结束 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
<%-- 加载JS 文件 --%>
		<script src="<%=inheritPath%>/common/js/myjquery-c.js"></script>
		<script src="<%=inheritPath%>/common/js/common.js"></script>
		<script src="<%=inheritPath%>/common/js/ajaxfileupload.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
		<script src="<%=path%>/appmage/account/js/accountList.js"></script>	
		<script>
		$(document).ready(function() {
            var url='<%=account.getImg()==null?"":account.getImg()%>';
            $('img','#main_pre').attr('src',url);
            $('span','#main_pre').css('display','none'); 
            
			$('.files').live('change',function(){
				var id = $(this).attr('name');
				upload(id);
			})
	    });
	var lguserid=<%=request.getParameter("lguserid")%>
	var lgcorpcode=<%=request.getParameter("lgcorpcode")%>
		var lgcorpcode=<%=request.getParameter("lgcorpcode")%>
		function show(){
		var res = <%=result%>;
		res=res+"";
		if(res != "null" && res !=""){
			 $("#waitTextConnection").text("");
			 $("#save").attr("disabled",false);
			if(res=="true"){
				//alert("操作成功,并且已经登录到梦网APP平台！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_loginapp'));
			}else if(res=="100"){
				//alert("企业编号重复！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_repeatbuscode'));
			}else if(res=="101"){
				//alert("APP账号重复！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_repeataccount'));
			}else if(res=="false") {
				//alert("保存失败，请检查网络！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_checknet'));
			}else if(res=="-1"){
				//alert("未配置企业账户！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_nobusaccount'));
			}else if(res=="-2"){
				//alert("登录APP平台失败！[错误编码:-2]");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_loginerror') + "-2]");
			}else if(res=="-3"){
				//alert("登录APP平台失败！[错误编码:-3]");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_loginerror') + "-3]");
			}else if(res=="-4"){
				//alert("登录APP平台失败！[错误编码:-4]");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_loginerror') + "-4]");
			}else if(res=="-5"){
				//alert("登录APP平台失败！[错误编码 :-5]");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_loginerror') + "-5]");
			}else if(res=="401"){
				//alert("用户名密码无效！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_nonameandpwd'));
			}else if(res=="408"){
				//alert("用户请求超时！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_requesttimeout'));
			}else if(res=="409"){
				//alert("冲突错误！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_chongtu'));
			}else if(res=="501"){
				//alert("服务器不提供此功能！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_noserversuport'));
			}else if(res=="502"){
				//alert("服务器内部错误！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_servererror'));
			}else if(res=="504"){
				//alert("服务器内部错误！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_servererror'));
			}else{
				//alert("发生不可恢复错误，请联系管理员！");
				alert(getJsLocaleMessage('appmage','appmage_js_accountList_lxgly'));
			}
		}
		
	}
		$(document).ready(function() {
            var url='<%=account.getImg()==null?"":account.getImg()%>';
            $('img','#main_pre').attr('src',url);
            $('span','#main_pre').css('display','none'); 
	    });
		// 屏蔽刷新代码
		document.onkeydown=function()
		{
			if ((window.event.keyCode==116) || //屏蔽 F5 
				(window.event.keyCode==122) || //屏蔽 F11 
				(window.event.shiftKey && window.event.keyCode==121) //shift+F10 
			   )
			{
				window.event.keyCode=0; 
				window.event.returnValue=false; 
			}
		}
		function nocontextmenu() {
			event.cancelBubble = true;
			event.returnValue = false; 
			return false; 
		}
		function norightclick(e) {
			if (window.Event) {
			}
			else
				if (event.button == 2 || event.button == 3) {
					event.cancelBubble = true;
					event.returnValue = false; 
					return false;
				}
		}
		document.oncontextmenu = nocontextmenu; // for IE5+ 
		document.onmousedown = norightclick; // for all others 
		
		</script>


</body>
</html>