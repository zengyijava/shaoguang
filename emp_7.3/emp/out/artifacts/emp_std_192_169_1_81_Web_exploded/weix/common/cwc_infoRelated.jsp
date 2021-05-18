<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	

	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	String wcId = request.getParameter("wcId");
	String lgcorpcode = request.getParameter("lgcorpcode");
	String result = (String) request.getAttribute("result");
	if(result==null || "".equals(result))
	{
		result="-1";
	}
%>

<!DOCTYPE HTML>
<html lang="en-US">
  <head>
	<meta name="viewport" content="width=device-width,initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title><emp:message key="wexi_qywx_common_text_1" defVal="填写微信与emp关联信息"
											fileName="weix"></emp:message></title>
    <link href="<%=iPath %>/css/cwc_infoRelated.css" rel="stylesheet" type="text/css" />
    

  </head>
  
  <body>
   <section id="wrap-login">
    <div class="top-tips"><emp:message key="wexi_qywx_common_text_2" defVal="完善您的基本信息，可以获得更多的服务。"
											fileName="weix"></emp:message></div>
	<form id="pangeForm" name="pangeForm" action="cwc_infoRelated.hts?method=saveInfo" method="post">
	<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=lgcorpcode!=null?lgcorpcode:"" %>">
	<input type="hidden" name="wcId" id="wcId" value="<%=wcId!=null?wcId:""%>"/>
	<%-- 代表是否成功获取过验证码  0.没有 1.有--%>
	<input type="hidden" name="isGetCode" id="isGetCode" value="1"/>
	  <div class="inp_list controls">
	      <em><emp:message key="wexi_qywx_common_text_3" defVal="手机号："
											fileName="weix"></emp:message></em>
		  <input type="text" onblur="checkPhone()" name="phone" id="phone" maxlength="11" class="inp login_mobile" placeholder="" required />
		  <input type="button"  id="getCode" class="btn btn-primary right" value="<emp:message key="wexi_qywx_common_text_6" defVal="获取验证码"
											fileName="weix"></emp:message>" onClick="getVerifyCode(this)"/>
	  </div>
	  <div class="inp_list controls">
	      <em><emp:message key="wexi_qywx_common_text_4" defVal="验证码："
											fileName="weix"></emp:message></em>
		  <input type="text" maxlength="5" onkeyup="checkCode()" class="inp" id="verifyCode" name="verifyCode" placeholder="" required/>
	  </div>
	  <div class="inp_list controls">
	      <em><emp:message key="wexi_qywx_common_text_5" defVal="其&nbsp;&nbsp;他："
											fileName="weix"></emp:message></em>
		  <textarea name="other" id="other" cols="30" rows="3" onkeyup="checkOther()" class="inp"></textarea>
	  </div>
	  <div class="inp_list">
		  <button type="button" class="btn btn-submit btn-large" onclick="save()"><emp:message key="common_btn_22" defVal="提 交"
											fileName="common"></emp:message></button>
	  </div>
	  <div class="inp_list">
	    <div class="tips" style="color: red;" ></div>
	  </div>
	</form>
  </section>
  
<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/weix_<%=langName%>.js"></script>
<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
  <script src="<%=commonPath %>/common/js/myjquery-a.js" type="text/javascript"></script>
  <script type="text/javascript">
    $(document).ready(function(){
    	$('#wrap-login .inp').each(function(){
    		if($(this).val()!=''){
    			$(this).parent().find('em').hide();
    		}
    	})
		$('#wrap-login .inp').focus(function(){
			$(this).parent().find('em').hide();
		}).blur(function(){
			if($(this).val()==''){
				$(this).parent().find('em').show();
			}
		})
		var result='<%=result%>';
		if(result!="-1")
		{
			if(result=="success")
	    	{
	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_1"));
	    	}
	    	else if(result=="fail")
	    	{
	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_2"));
	    	}
	    	else if(result=="phoneError")
	    	{
	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_3"));
	    	}
	    	else if(result=="notUserInfo")
	    	{
	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_4"));
	    	}
	    	else if(result=="error")
	    	{
	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_5"));
	    	}
	    	else
	    	{
	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_6"));
	    	}
		}
		
	})
    
    function getVerifyCode(val)
    {
    	var phone=$("#phone").val();
		var patrnMobile=/^[0-9]{11}$/;
		
		if(phone.length==0)
		{
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_7"));
			return;
		}
		else if(!patrnMobile.exec(phone) && phone.length != 0)
		{
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_8"));
			return;
		}

		//settime(val) 
		
		$.post("cwc_infoRelated.hts?method=sendYzm",
			{lgcorpcode:$("#lgcorpcode").val(),wcId:'<%=wcId!=null?wcId:""%>',phone:phone,isAsync:"yes"},
   	    	function(result){
    	    	if(result == "outOfLogin")
    	    	{
    	    		$("#logoutalert").val(1);
    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_9"))
    	    		return;
    	    	}
    	    	else if(result== "success")
    	    	{
    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_10"));
    	    	}
    	    	else if(result=="infoError")
    	    	{
    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_11"));
    	    		return;
    	    	}
    	    	else if(result=="codeTiemInFive")
    	    	{
    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_12"));
    	    		return;
    	    	}
    	    	else if(result=="errorphone")
    	    	{
    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_13"));
    	    	}
    	    	else if(result=="error")
    	    	{
    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_14"));
    	    	}
    	    	else if(result=="NoMoney")
    	    	{
    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_15"));
    	    	}
    	    	else if(result=="Moneyerror")
    	    	{
    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_16"));
    	    	}
    	    	else if(result=="sendfalse")
    	    	{
    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_17"));
    	    	}
    	    	else
    	    	{
    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_18"));
    	    	}
			});
    }
    
	var countdown=60;
	//获取验证码
	function settime(val) 
	{

		var t=setTimeout(function(){settime(val)},1000);
		if (countdown == 0) 
		{
			val.removeAttribute("disabled");
			val.value=getJsLocaleMessage("weix","weix_qywx_common_text_19");
			countdown = 60;
			clearTimeout(t);
		} 
		else 
		{
			val.setAttribute("disabled", true);
			val.value=getJsLocaleMessage("weix","weix_qywx_common_text_20") + countdown + ")";
			countdown--;
		}
		
	}

	//手机号输入框输入过滤
	function checkPhoneIput(str)
	{
		if(str.value!=str.value.replace(/\D/g,''))
		{
			str.value=str.value.replace(/\D/g,'');
		}
	}
	
	//点击提交
	function save()
	{
		var phone=$("#phone").val();
		var patrnMobile=/^[0-9]{11}$/;
		var verifyCode = $("#verifyCode").val();
		var verifyCodeStr=/^[0-9]{5}$/;
		var other=$("#other").val();
		if(phone.length==0)
		{
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_7"));
			return;
		}
		else if(!patrnMobile.exec(phone) && phone.length != 0)
		{
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_8"));
			return;
		}
		if(verifyCode.length==0)
		{
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_21"));
			return;
		}
		if(!verifyCodeStr.exec(verifyCode))
		{
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_22"));
			return;
		}

		if(other.length>150)
		{
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_23"));
			return;
		}
		$(".inp_list").find("button").attr("disabled","disabled");

		$.post("cwc_infoRelated.hts?method=checkPC",
				{lgcorpcode:$("#lgcorpcode").val(),verifyCode:verifyCode,wcId:$("#wcId").val(),phone:phone,isAsync:"yes"},
	   	    	function(result){
					$(".inp_list").find("button").attr("disabled","");
	    	    	if(result == "outOfLogin")
	    	    	{
	    	    		$("#logoutalert").val(1);
	    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_24"))
	    	    		return;
	    	    	}
	    	    	else if(result=="phoneError")
	    	    	{
	    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_13"));
	    	    		return;
	    	    	}
	    	    	else if(result=="codeNull")
	    	    	{
	    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_25"));
	    	    		return;
	    	    	}
	    	    	else if(result=="false")
	    	    	{
		    	    	// "验证码错误，请重新输入 ！"
	    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_22"));
	    	    		return;
	    	    	}
	    	    	else if(result=="codeOutTime")
	    	    	{
	    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_28"));
	    	    		return;
	    	    	}
	    	    	else if(result=="countOut")
	    	    	{
	    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_26"));
	    	    		return;
	    	    	}
	    	    	else if(result=="infoError")
	    	    	{
	    	    		// "页面信息出错！"
	    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_11"));
	    	    		return;
	    	    	}
	    	    	else if(result=="true")
	    	    	{
	    	    		$("#pangeForm").submit();
	    	    	}
	    	    	else
	    	    	{
	    	    		// "验证信息失败！"
	    	    		$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_1"));
	    	    		return;
	    	    	}
	    	    		
				});
		
		
		
	}

	function checkPhone()
	{
		var phone=$("#phone").val();
		var patrnMobile=/^[0-9]{11}$/;
		var verifyCode = $("#verifyCode").val();
		var verifyCodeStr=/^[0-9]{5}$/;
		var other=$("#other").val();
		if(phone.length==0)
		{
			// "请输入手机号！"
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_1"));
			return;
		}
		else if(!patrnMobile.exec(phone) && phone.length != 0)
		{
			// "手机号码格式不正确，必须是11位数字！"
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_1"));
			return;
		}
		
		$(".tips").html("");
	}

	function checkCode()
	{
		
		var verifyCode = $("#verifyCode").val();
		var verifyCodeStr=/^[0-9]{5}$/;
		if(verifyCode.length==0)
		{
			// "请输入验证码！"
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_1"));
			return;
		}
		if(!verifyCodeStr.exec(verifyCode))
		{
			// "验证码错误，请重新输入 ！"
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_1"));
			return;
		}
		$(".tips").html("");
	}

	function checkOther()
	{
		var other=$("#other").val();
		if(other.length>150)
		{
			// "其他输入内容太长，最长允许150字！"
			$(".tips").html(getJsLocaleMessage("weix","weix_qywx_common_text_1"));
			return;
		}
		$(".tips").html("");
	}

	function clear()
	{
		var val=$("#getCode").
		$("#phone").val("");
		$("#verifyCode").val("");
		$("#other").val("");
		$("em").show();
		$("#getCode").attr("disabled","");
		//"获取验证码"
		$("#getCode").val(getJsLocaleMessage("weix","weix_qywx_common_text_1"));
		countdown = 60;
		clearTimeout(t);
	}
  </script>
</body>
</html>
