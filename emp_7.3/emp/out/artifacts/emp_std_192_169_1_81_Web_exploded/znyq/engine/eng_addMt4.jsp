<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
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
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mtService");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	

	LfService service = new LfService();
	if (request.getAttribute("service") != null)
	{
		service = (LfService)request.getAttribute("service");
	}
	
	String serId = request.getParameter("serId");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
		
		
		<style type="text/css">
		</style>
		
	</head>
	<body id="eng_addMt4" class="eng_addMt4">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode,"步骤管理") %>--%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_bzgl",request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="eng_mtService.htm" method="post" id="proform">
			
				<input type="hidden" id="serId" name="serId" value='<%=serId %>' />
				
				<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
				<div class="linediv">
					<table class="linebgimg4">
						<tr>
						<td class="linebgth1"><b>1.</b><emp:message key="znyq_ywgl_xhywgl_xjyw" defVal="新建业务" fileName="znyq"></emp:message></td>
						<td class="linebgth2"><b>2.</b><emp:message key="znyq_ywgl_xhywgl_xjsleletbz" defVal="新建select步骤" fileName="znyq"></emp:message></td>
						<td class="linebgth3"><b>3.</b><emp:message key="znyq_ywgl_xhywgl_xjreplybz" defVal="新建reply步骤" fileName="znyq"></emp:message></td>
						<td class="linebgth4"><b>4.</b><emp:message key="znyq_ywgl_xhywgl_szfssj" defVal="设置发送时间" fileName="znyq"></emp:message></td>
						<td class="linebgth5"><b>5.</b><emp:message key="znyq_ywgl_xhywgl_wc" defVal="完成" fileName="znyq"></emp:message></td>
						</tr>
					</table>
				</div>
				<div class="itemMtDiv" id="item1">
				<span class="righttitle"><emp:message key="znyq_ywgl_xhywgl_rwmc_mh" defVal="任务名称：" fileName="znyq"></emp:message></span>
				<table><tr><td>
				<xmp class="commonXmp"><%=service.getSerName() %></xmp>
				</td></tr></table>
				</div>
				
				<div class="itemMtDiv">
				<span id="spanBeginTime" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_kssj_mh" defVal="开始时间：" fileName="znyq"></emp:message></span>
				<table class="sendtimeTable"><tr><td class="input_bd">
				<input name="sendtime"  value="" id="sendtime"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m'})" readonly="readonly" class="Wdate" />
				</td>
				<td class="zeroBorder"><font class="tipColor">&nbsp;*</font></td></tr></table>
				</div>
				
				<div class="itemMtDiv">
				<span id="spanSendCount" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_fspl_mh" defVal="发送频率：" fileName="znyq"></emp:message></span>
				<table class="sendcountTable"><tr><td class="input_bd">
				<select name="runway" class="runway" onchange="showAlert(this)" >
				<option value="0"><emp:message key="znyq_ywgl_xhywgl_ycx" defVal="一次性" fileName="znyq"></emp:message></option>
				<option value="1"><emp:message key="znyq_ywgl_xhywgl_mt" defVal="每天" fileName="znyq"></emp:message></option>
				<option value="4"><emp:message key="znyq_ywgl_xhywgl_mz" defVal="每周" fileName="znyq"></emp:message></option>
				<option value="2"><emp:message key="znyq_ywgl_xhywgl_my" defVal="每月" fileName="znyq"></emp:message></option>
				</select>
				</td></tr></table>
				</div>
				
				<div class="itemMtDiv" id="validateDiv">
				<span id="spanValiddate" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_yxq_mh" defVal="有效期：" fileName="znyq"></emp:message></span>
				<table class="validateTable"><tr>
				<td id="hoursTd" class="input_bd">
				<input type="text" value="1" id="hours" name="hours" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="3"/>
				&nbsp;<emp:message key="znyq_ywgl_xhywgl_xs" defVal="小时" fileName="znyq"></emp:message>
				</td>
				<td id="minitesTd" class="input_bd">
				<input type="text" name="minites" value="" id="minites" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="3"/>
				&nbsp;<emp:message key="znyq_ywgl_xhywgl_fz" defVal="分钟" fileName="znyq"></emp:message>
				</td></tr></table>
				</div>
				
				<div class="itemMtDiv">
				<span id="spanSendCount" class="righttitle"></span>
				<table class="sendCountDiv"><tr><td >
				<font id="fontZhu"><emp:message key="znyq_ywgl_xhywgl_z_mh_dyfm" defVal="注：当月份没29、30、31号时，本业务当月将不执行。" fileName="znyq"></emp:message></font>
				</td></tr></table>
				</div>
				
				<div class="nextstepDiv">
				<input type="button" id="btnNextstup"  value="<emp:message key="znyq_ywgl_xhywgl_wc" defVal="完 成" fileName="znyq"></emp:message>" class="btnClass5 mr23" onclick="checkServerTime()" />
				<input type="button"  value="<emp:message key="znyq_ywgl_common_btn_23" defVal="上一步" fileName="znyq"></emp:message>" class="btnClass6 indent_none" onclick="goLast()" />
				<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
				<br/>
				</div>
			</form>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
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
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			//show();
		});
		function goLast()
		{
			var lguserid =$('#lguserid').val();
			var lgcorpcode=$('#lgcorpcode').val();
			location.href="<%=path%>/eng_mtProcess.htm?method=toAddReply&serId=<%=serId %>&repPrId=<%=request.getAttribute("repPrId")==null?"":request.getAttribute("repPrId") %>";
		}

		function form()
		{
			var time=$("#sendtime").val();
			if(time=="")
			{
				//alert("请选择开始时间！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qxzkssj"));
			}
			else if($('#exit').val()==1)
			{
				//alert("已在该业务定时,不能重复设置！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_yzgywds"));
			}
			else
			{
				$.post("eng_mtService.htm",{method:"getRunState",serId:$("#serId").val()},
						function(result)
						{
							if(result=="0")
							{
								//alert("该业务已禁用，无法定时！");
								alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_gywyjy"));
							}
							else if(result=="error")
							{
								//alert("验证业务状态是否运行失败，请确认网络是否正常！");
								alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_yzywztsfyxsb"));
							}
							//else if(confirm('请检查所填时间格式是否正确，正确请点击“确定”，否则点击“取消”进行修改！')==true)
							else if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qjcstsjgesfzq"))==true)
							{
								$("#btnNextstup").attr("disabled","disabled");
	
								$('#proform').attr("action",$('#proform').attr('action')+'?method=addTime&serId=<%=serId %>'+'&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val());
					
								$("#proform").submit();
							}
						}
				);
			}
		}
		
		//当前时间与服务器时间对比
		function checkServerTime()
		{
			var sendTime = $('#sendtime').val();
			var serverTime ;
			$.post("eng_mtService.htm", {
				method : "getServerTime"
			}, function(msg) {
				if(msg == null || msg.length==0)
				{
					//alert("获取服务器时间失败！");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_hqfwqsjsb"));
					return;
				}
				serverTime = msg;
				var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
				var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
                date1.setMinutes(date1.getMinutes() - 1, 0, 0);
				if (date1 <= date2)
				{
					//alert("预发送时间小于服务器当前时间！请合理预定发送时间");
					alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_yfssjxyfwqdqsj"));
					$("#sendtime").val("");
					return;
				}
				form();
			});
		}
		
		function showAlert(obj){
			if(obj.value == '2'){
				$("#fontZhu").css("display","");
			}else{
				$("#fontZhu").css("display","none");
			}
		}
		
    
	</script>
		
	</body>
</html>