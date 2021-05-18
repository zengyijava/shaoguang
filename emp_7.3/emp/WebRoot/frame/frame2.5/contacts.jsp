<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String version = StaticValue.getEmpVersion();
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head><%@ include file="/common/common.jsp"%>
	 <meta http-equiv="X-UA-Compatible" content="IE=8">
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><emp:message key="common_frame2_contacts_1" defVal="企业移动信息平台" fileName="common"></emp:message></title>
	<jsp:include page="/common/commonfile.jsp"></jsp:include>
	<link href="<%=iPath %>/css/main.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/main.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <style type="text/css">
		  body{
			  background-color: white;
		  }
		  #complain{
			  width:500px;overflow: auto;
			  padding-top:10px
		  }
		  #complainTab > tbody > tr > td:nth-child(1){
			  text-align:right;
		  }
		  #complainDiv tr > td:nth-child(1){
			  text-align: right;
			  width:85px;
		  }
		  #complainDiv > center > div.feedback > div:nth-child(1) > table > tbody > tr > td:nth-child(2){
			  text-align: left;
		  }
		  #complain input#ctitle,#complain textarea#ccontent,#complain input#cemail,#complain input#cphone{
			  width: 400px;
		  }
		  textarea#ccontent{
			  height:140px;
			  overflow-y:hidden;
		  }
    </style>
  	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
  	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<style type="text/css">
		div#complain{
			width: 600px;
		}
		#complainTab > tbody > tr > td:nth-child(1){
			width: 100px;
		}
		input.btnClass5.mr23,input.btnClass6{
			letter-spacing: 0;
			text-indent: 0px;
			text-align: center;
		}
		#complainTab > tbody > tr > td:nth-child(1){
			text-align:left;
		}
		#complainDiv tr > td:nth-child(1){
			text-align: left;
			width:160px;
		}
		#complainDiv > center > div.feedback > div:nth-child(1) > table > tbody > tr > td:nth-child(2){
			text-align: left;
		}
		#complain input#ctitle,#complain textarea#ccontent,#complain input#cemail,#complain input#cphone{
			width: 470px;
		}
		#complainTab > tbody > tr:nth-child(4) > td:nth-child(2) > div > div{
			width: 465px;
		}
		div#ccontent_label{
			top: 85px;
			width: 450px;
			left: 150px;
		}
		textarea#ccontent{
			height:180px;
		}
	</style>
  	<%}%>
  <script type="text/javascript">
      var emppatch = "<%=path%>";
      var version="<%=version%>";
    </script>
</head>
<body>
    <div id="complainDiv" class="container">
    <center>
    <div id="complain">
    	<table style="text-align:left;" id="complainTab">
    	<tr>
			<td><emp:message key="common_title" defVal="标题：" fileName="common"></emp:message></td>
			<td style="height:22px;"><input maxlength="16" id="ctitle" name="Ctitle" class="input_bd"/></td>
		</tr>
    	<tr>
			<td><emp:message key="common_type" defVal="类型：" fileName="common"></emp:message></td>
			<td><select id="ctype" name="ctype" class="input_bd"><option value="1"><emp:message key="common_suggest" defVal="建议" fileName="common"></emp:message></option><option value="2"><emp:message key="common_complain" defVal="投诉" fileName="common"></emp:message></option><option value="3"><emp:message key="common_consult" defVal="咨询" fileName="common"></emp:message></option></select></td>
		</tr>
    	<tr>
			<td valign="top"><emp:message key="common_description" defVal="描述：" fileName="common"></emp:message></td>
			<td style="padding-bottom:10px;">
				 <textarea id="ccontent" name="ccontent" class="input_bd" onblur="blurHide($(this))" onfocus ="focusShow($(this))" onkeyup="countRestrict($(this))" ></textarea>
				 <div id="ccontent_label" onclick="focusShow_label($(this))">
					 <p><emp:message key="common_frame2_contacts_8" defVal="感谢您的关注和反馈，产品的改善和优化有您的一份力量；" fileName="common"></emp:message></p>
					 <p><emp:message key="common_frame2_contacts_9" defVal="我们有专人处理您的咨询和投诉，请在下方留下联系方式" fileName="common"></emp:message></p>
					 <p><emp:message key="common_frame2_contacts_10" defVal="我们将对所有建议进行梳理和分析，请原谅不能逐一回复；" fileName="common"></emp:message></p>
					 <p><emp:message key="common_frame2_contacts_11" defVal="我们会据此改进和优化产品及服务；" fileName="common"></emp:message></p>
					 <p><emp:message key="common_frame2_contacts_12" defVal="请至少输入5个字符。" fileName="common"></emp:message></p>
    	 		</div>
    	 	</td>
		 </tr>
    	 <tr>
			<td><emp:message key="common_frame2_contacts_13" defVal="联系邮箱：" fileName="common"></emp:message></td>
			<td>
				<div class="parentInp">
					<input  maxlength="50"  id="cemail" name="cemail"  onblur="blurHide($(this))" onfocus ="focusShow($(this))" class="input_bd"/>
					<div class="input_tip" onclick ="focusShow_label($(this))"><emp:message key="common_frame2_contacts_14" defVal="请输入您的常用邮箱，以便我们联系您(只能写一个邮箱)" fileName="common"></emp:message></div>
				</div>
			</td>
		 </tr>
    	 <tr>
			<td><emp:message key="common_frame2_contacts_15" defVal="联系电话：" fileName="common"></emp:message></td>
    		<td>
				<div class="parentInp">
					<input id="cphone" name="cphone" class="input_bd"  maxlength="15"  onblur="blurHide($(this))" onfocus ="focusShow($(this))" onkeyup="numberControl($(this))" />
					<div class="input_tip" onclick ="focusShow_label($(this))"><emp:message key="common_frame2_contacts_16" defVal="请输入您的手机或座机" fileName="common"></emp:message></div>
				</div>
    		</td>
    	 </tr>
    	</table>
    </div>
    <div style="text-align: center;margin-bottom:20px;margin-top:10px">
    	<input type="button" class="btnClass5 mr23" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>"  id="com_but_ok" onclick="complainSure()" />
    	<input type="button" class="btnClass6" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" id="com_but_no"  onclick="complainNoGood()"/>
    	<br/>
    </div>
	<div class="bd" style="border-top:1px dashed #ccc;height:20px;width:500px;"></div>
    <div id="u20_rtf" class="utips"><span style="font-family:Arial;font-size:14px;color:#6FD36F;text-align:left;">
     <emp:message key="common_frame2_contacts_17" defVal="提示： 您也可以通过以下方式进行反馈" fileName="common"></emp:message>
    </span>
    </div>
    <br>
	
    <div class="feedback">
<div style="float:left;width:280px;">
<table width="100%">
    <tr >
    	<td><emp:message key="common_frame2_contacts_18" defVal="Q Q 客服：" fileName="common"></emp:message></td>
    	<td>306950241</td>
    </tr>
     <tr>
    	<td><emp:message key="common_frame2_contacts_19" defVal="客服热线：" fileName="common"></emp:message></td>
    	<td>4007-001-009</td>
    </tr>
    <tr >
    	<td><emp:message key="common_frame2_contacts_20" defVal="客服传真：" fileName="common"></emp:message></td>
    	<td>0755－86017706</td>
    </tr>
     <tr>
    	<td><emp:message key="common_frame2_contacts_21" defVal="客服邮箱：" fileName="common"></emp:message></td>
    	<td>service@montnets.com</td>
    </tr>
    </table>
    </div>
	<div style="float:right;width:150px;">
		<img  src="<%=commonPath %>/common/img/qrcode.jpg" width="110"/><br/>
		<%if("zh_HK".equals(empLangName)){%>
			<p>Scan the QR code</p>
			<p>Feedback online</p>
		<%}else{%>
			<emp:message key="common_frame2_contacts_22" defVal="扫一扫，手机在线反馈" fileName="common"></emp:message>
		<%}%>
	</div>
    </div>
	</center>
    </div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=iPath %>/js/contacts.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
</body>
</html>
