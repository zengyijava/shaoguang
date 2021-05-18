<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.engine.LfProcess"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@ page import="com.montnets.emp.entity.pasroute.LfSpDepBind" %>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@ page import="com.montnets.emp.entity.engine.LfReply" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
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
	
	String serId = (String)request.getAttribute("serId");
	
	String tailcontents = (String)request.getAttribute("tailcontents");

	LfProcess lp = new LfProcess();
	if (request.getAttribute("process") != null)
	{
		lp = (LfProcess)request.getAttribute("process");
	}

	List<LfTemplate> tmpList = new ArrayList<LfTemplate>();
	if(request.getAttribute("tmpList") != null)
	{
		tmpList=(List<LfTemplate>)request.getAttribute("tmpList");
	}
	
	LfProcess selPro = (LfProcess)request.getAttribute("selPro");
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
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
		
	</head>
	<body id="eng_addMt3" class="eng_addMt3">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode,"步骤管理") %>--%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_bzgl",request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="eng_mtProcess.htm" method="post" id="proform">
			
				<input type="hidden" id="repPrId" name="repPrId" value='<%=lp.getPrId()==null?"":lp.getPrId() %>' />
				<input type="hidden" id="serId" name="serId" value='<%=serId%>' />
				<input type="hidden" id="workType" name="workType" value="waterLine"/>
				<input type="hidden" id="finalState" name="finalState" value="1"/><%-- reply步骤默认设为是最终步骤 --%>
				<input type="hidden" id="tailcontents" name="tailcontents" value="<%=tailcontents==null?"":tailcontents %>"/>
				
				<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
				<div class="linediv">
					<table class="linebgimg3">
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
				<span id="spanPrName" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_bzmc_mh" defVal="步骤名称：" fileName="znyq"></emp:message></span>
				<table><tr><td class="input_bd">
				<input type="hidden" id="hprName" name="hprName" value='<%=lp.getPrName()==null?"":lp.getPrName() %>' />
				<input type="text" name="prName" id="prName" value='<%=lp.getPrName()==null?"":lp.getPrName() %>' maxlength="20"/>
				</td><td class="zeroBorder"><font class="tipColor">&nbsp;*</font></td></tr></table>
				</div>
				
				<div class="itemMtDiv" id="stepDiv">
				<span class="righttitle"><emp:message key="znyq_ywgl_xhywgl_bzms_mh" defVal="步骤描述：" fileName="znyq"></emp:message></span>
				<table><tr><td class="input_bd">
				<input type="text" name="comments" id="comments" value='<%=lp.getComments()==null?"":lp.getComments() %>' maxlength="32"/>
				</td></tr></table>
				</div>
				
				<div class="itemMtDiv" id="spanPrDiv">
				<span id="spanPrType" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_bzlx_mh" defVal="步骤类型：" fileName="znyq"></emp:message></span>
				<table><tr><td style="border: 0;">
				<input type="hidden" name="prType" value="5" />
				&nbsp;&nbsp;Reply
				</td></tr></table>
				</div>
				
				<div class="div_bg">
				<table id="addseltable">
					<thead>
					<tr>
						<td class="spanDataSource">
							<span id="spanDataSource" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_sjy_mh" defVal="数据源：" fileName="znyq"></emp:message></span>
						</td>
						<td class="">
							<label>
								<input name="msgLoopId" id="msgLoopId" type="hidden" value="<%=selPro.getPrId() %>" />
								<%=selPro.getPrName() %>
							</label>
						</td>
					</tr>
					<tr>
						<td><span class="righttitle"><emp:message key="znyq_ywgl_xhywgl_dxmb_mh" defVal="短信模板：" fileName="znyq"></emp:message></span></td>
						<td class="tempSelTd">
						<div class="input_bd">
						<label><select name="tempSel" id="tempSel" onchange="getSql(this.value)">
							<option value="" class="slvColor"><emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message></option>
						<%
							if(tmpList != null && tmpList.size()>0)
							{
								for(LfTemplate temp : tmpList)
								{
						%>
									<option value="<%=temp.getTmid() %>" <% if(temp.getTmid().equals(lp.getTemplateId())){ %>selected="selected"<%} %>><%=temp.getTmName().replace("<","&lt;").replace(">","&gt;") %>(ID:<%=temp.getTmid() %>)</option>
						<%
								}
							}
						 %>
						</select></label><%-- &nbsp;<a href="javascript:location.href=location.href">还原</a> --%>
						</div>
						</td>
						<%if(btnMap.get(titleMap.get("smsTemplate")+"-1")!=null) { %>
						<td><a onclick="showAddSmsTmp(1)" class="showAddSms"><emp:message key="znyq_ywgl_xhywgl_xz" defVal="新增" fileName="znyq"></emp:message></a></td>
						<%} %>
					</tr>
					<tr>
						<td class="spanMsgTd">
							<span id="spanMsgCon" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_fsnr_mh" defVal="发送内容：" fileName="znyq"></emp:message></span>
						</td>
						<td class="leftPad">
						<div id="limitDiv" class="input_bd" >
							<textarea class="textarea_limit" name="msgMain" id="msgMain" ><%
							if(request.getAttribute("reply") !=null)
							{
								LfReply reply = (LfReply)request.getAttribute("reply");
								
								out.print(reply.getMsgMain());
							}
							%></textarea>
						</div>
						<div id="tailConDiv" class="<%=tailcontents==null?"hiddenValueDiv":"emptycss" %>">
							<div class="tailContCss input_bd"><emp:message key="znyq_ywgl_xhywgl_twnr_mh" defVal="贴尾内容：" fileName="znyq"></emp:message><label id="showtailcontent"><%=tailcontents==null?"":StringEscapeUtils.escapeHtml(tailcontents) %></label></div>
						</div>
						</td>
						<td><font class="tipColor">&nbsp;*</font></td>
					</tr>
					<tr>
						<td><span id="spanPreview" class="righttitle"><emp:message key="znyq_ywgl_xhywgl_dxyl_mh" defVal="短信预览：" fileName="znyq"></emp:message></span></td>
						<td class="spanPreviewTd">
							<input type="hidden" id="pathUrl" value="<%=path %>" />
							<input id="btnPreview" type="button" value="<emp:message key="znyq_ywgl_common_text_6" defVal="预览" fileName="znyq"></emp:message>" class="btnClass3" onclick="getResult()" />
						</td>
					</tr>
					</thead>
				</table>
				</div>
				
				<div class="nextStepDiv">
				<input type="button" id="btnNextstup"  value="<emp:message key="znyq_ywgl_common_btn_17" defVal="下一步" fileName="znyq"></emp:message>" class="btnClass5 mr23 indent_none" onclick="checkSubBefore()" />
				<input type="button"  value="<emp:message key="znyq_ywgl_common_btn_23" defVal="上一步" fileName="znyq"></emp:message>"  class="btnClass6 indent_none" onclick="goLast()" />
				<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
				<br/>
				</div>
			</form>
			
			<div id="modify" class="modify" title="<emp:message key="znyq_ywgl_xhywgl_dxyl" defVal="短信预览" fileName="znyq"></emp:message>">
				<div id="msg" class="msg"><xmp></xmp></div>
			</div>
			
			</div>
			<div id="addSmsTmpDiv" class="addSmsTmpDiv" title="<emp:message key="znyq_ywgl_xhywgl_dxmb" defVal="短信模板" fileName="znyq"></emp:message>">
			<iframe id="addSmsTmpFrame" name="addSmsTmpFrame" class="addSmsTmpFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
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
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/replyAdd.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/tipcontent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/textarea_limit.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_smsContentLength.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
			getSmsContentMaxLen('<%=serId %>');
		});
		
		function goLast()
		{
			var lguserid =$('#lguserid').val();
			var lgcorpcode=$('#lgcorpcode').val();
			location.href="<%=path%>/eng_mtProcess.htm?method=toAddSelect&serId=<%=serId %>&prId=<%=selPro.getPrId() %>&repPrId=<%=lp.getPrId()==null?"":lp.getPrId() %>";
		}

	    //获取sql模板内容
		function getSql(sqlId) {
			var $sqlContent = $("form[name='proform']").find("textarea[name='msgMain']");
			if(sqlId != "")
			{
				$.post("tem_smsTemplate.htm",{method:"getTmMsg",tmId:sqlId},function(msg)
					{
						$("#msgMain").val(msg);
					}
				);
			}else
			{
				$("#msgMain").val("");
			}
		}
		
	    //表单验证
	function checkSubBefore()
	{	
		var prName = $("#prName").val();         //步骤名称
		//var prType = $("#prType").val();
		prName = prName.replace(/\s+/g,"");         // 清除所有空格
		if(prName == "") 
		{      
			//检查步骤名称
			//alert("步骤名称不能为空，请输入步骤名称");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcbnwk"));
			return false;
		}
		if(!prName.match( /^[\u4E00-\u9FA5a-zA-Z0-9_]+$/))
		{
			//alert("步骤名称只能由汉字、英文字母、数字、下划线组成");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_bzmcznyhz"));
			return false;
		}

		var msgMain = $("#msgMain").val();
		if (msgMain == "" || msgMain == null) 
		{
			//alert("发送内容不能为空");
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_fsnrbnwk"));
			return false;
		}
		var len = msgMain.length;
        if(len > smsContentMaxLen)
        {
        	//alert("发送内容长度大于短信最大长度限制，最大长度限制为："+smsContentMaxLen);
			alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_fsnrcddydxzdcdxz")+smsContentMaxLen);
			return false;
        }
		if(smsContentMaxLen == 700)
		{
			if(!checkSmsContentLen(msgMain,smsContentMaxLen))
			{
				return false;
			}
		}
		$("#btnNextstup").attr("disabled","disabled");
	
		$('#proform').attr("action",$('#proform').attr('action')+'?method=addReply&serId=<%=serId %>'+'&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val());
					
		$("#proform").submit();
		       	  
	}
	
	   
	//获取新模板
	function getTmplInfo() {
		
		var lguserid=$("#lguserid").val();
		$.post("eng_mtProcess.htm?method=getSmsTmpl&dsflag=0,1&lguserid="+lguserid,function(msg)
				{
					if(msg== "" || msg=="errtmpl"){
						//alert("获取模板信息失败。");
						alert(getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_hqmbxxsb"));
						return;
					}
					if(msg=="nodata"){
						return;
					}
					msg = eval("("+msg+")");
					//var selStr = '<option value="" style="color:#666666">请选择</option>';
					var selStr = '<option value="" style="color:#666666">'+getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_qxz")+'</option>';
					$.each(msg, function(idx,item){
						selStr += '<option value="'+item.tmid+'" >'+item.tmname+'</option>';
					});
					$("#tempSel").find("option").remove();
					$("#tempSel").append(selStr);
				}
		);
	}
    
	</script>
		
	</body>
</html>