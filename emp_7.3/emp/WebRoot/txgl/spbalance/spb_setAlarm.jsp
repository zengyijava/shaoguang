<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userfee"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	inheritPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	Userfee userfee = (Userfee) request.getAttribute("userfee");
	if(userfee==null){
		//out.println("此sp账号还未有过充值记录，请先充值");
		out.println(MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_cspzhhwygjl", request));
		return;
	}
	@SuppressWarnings("unchecked")
	List<DynaBean> spfeedynlist = (List<DynaBean>) request.getAttribute("spfeedynlist");
	
	//请输入通知人姓名
	 String qsrtzrxm = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_qsrtzrxm", request);
	 //请输入通知人手机号
	 String qsrtzrsjh = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_qsrtzrsjh", request);
    //确定
	String qd = MessageUtils.extractMessage("common", "common_confirm", request);
    //取消
	String qx = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_qx", request);
	
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_spzhczhs_gjfzsz" defVal="告警阀值设置" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=iPath%>/css/setAlarm.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		
	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<style type="text/css">
		label#alarmThreshold,label#receiver{
			width: 110px;
		}
		.channel_item{
			width: 520px;
		}
		input#phoneInput{
			width: 150px;
		}
		input#threshold{
			width: 293px;
		}
	</style>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/spb_setAlarm.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/spb_setAlarm.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	</head>
	<body id="spb_setAlarm">
					<div >
						<input type="hidden" id="useridstr" name="useridstr" value="<%=request.getParameter("useridstr") %>"/>
						<input type="hidden" id="lguserid" name="lguserid" value="<%=request.getParameter("lguserid") %>"/>
						<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
						<div id="addDiv">
				<div class="channel_manage nano">
					<div class="nano-content">
						<div class="channel_item clearfix">
							<label class="mod_t1" id="alarmThreshold"><emp:message key="txgl_wgqdpz_spzhczhs_gjfz" defVal="告警阀值：" fileName="txgl"></emp:message></label>
							<input type="text"  class="input_bd div_bd inpstyle"   id="threshold" name="threshold" value="<%=userfee.getThresHold()!=null?userfee.getThresHold():"" %>" maxlength="9" class="inpstyle"/>&nbsp;<emp:message key="txgl_wgqdpz_spzhczhs_t" defVal="条" fileName="txgl"></emp:message></td>
						</div>
					
						<%
							int zIndex=999;
									if(spfeedynlist != null && spfeedynlist.size() > 0)
									{
									//行标识
									int i =0;
										for (DynaBean dynspfee : spfeedynlist)
										{
											--zIndex;
											i++;
								%>
						<div class="channel_item sim_detail clearfix" data="<%=i %>" style="z-index:<%=zIndex %>">
								<label class="mod_t1" id="receiver"><emp:message key="txgl_wgqdpz_spzhczhs_tzr" defVal="通知人" fileName="txgl"></emp:message><%=i %>：</label>
								<input type="text" class="input_bd div_bd inpstyle1" name="noticename" value="<%=dynspfee.get("noticename")!=null?dynspfee.get("noticename").toString():"" %>" maxlength="21" placeholder="<%=qsrtzrxm %>"/>
								<input type="text" id="phoneInput" class="input_bd div_bd inpstyle1" name="alarmphone" value="<%=dynspfee.get("alarmphone")!=null?dynspfee.get("alarmphone").toString():"" %>"  onblur="phoneInputCtrl($(this));"  onkeyup="phoneInputCtrl($(this));"   maxlength="21" placeholder="<%=qsrtzrsjh %>"/>
								<span class="handle">
									<a class="add_handle" href="#"><emp:message key="txgl_wgqdpz_spzhczhs_tj" defVal="添加" fileName="txgl"></emp:message></a>
								| 	<a class="remove_handle" href="#"><emp:message key="common_delete" defVal="删除" fileName="common"></emp:message></a>
									<br/>
								</span>
						</div>
						<%
										}
									}else{
						%>
						<div class="channel_item sim_detail clearfix" data="1" style="z-index:<%=zIndex %>" id="channel_item">
								<label class="mod_t1" id="receiver"><emp:message key="txgl_wgqdpz_spzhczhs_tzro" defVal="通知人1：" fileName="txgl"></emp:message></label>
								<input type="text" class="input_bd div_bd inpstyle1" name="noticename" value="" maxlength="21" placeholder="<%=qsrtzrxm %>"/>
								<input type="text" id="phoneInput" class="input_bd div_bd inpstyle1" name="alarmphone" value=""  onblur="phoneInputCtrl($(this));"  onkeyup="phoneInputCtrl($(this));"   maxlength="21" placeholder="<%=qsrtzrsjh %>"/>
								<span class="handle"> <a class="add_handle" href="#"><emp:message key="txgl_wgqdpz_spzhczhs_tj" defVal="添加" fileName="txgl"></emp:message></a>
										| <a class="remove_handle" href="#"><emp:message key="common_delete" defVal="删除" fileName="common"></emp:message></a><br/></span>
						</div>
						<%
									}
						%>
							
						<div  align="center">
							<input class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="javascript: setAlarm(arguments[0]);"/>
							<input class="btnClass6" type="button" value="<%=qx %>" onclick="javascript:doCancelEdit('#setAlarm');"/>
							<br/>
						</div>
					</div>
					</div>
					</div>
					</div>
    <div class="clear"></div>
    <div id="templates" class="templates">
			<div class="channel_item sim_detail clearfix" data="1">
				<label class="mod_t1" id="receiver" id="channel_item"><emp:message key="txgl_wgqdpz_spzhczhs_tzro" defVal="通知人1：" fileName="txgl"></emp:message></label>
				<input type="text" class="input_bd div_bd inpstyle1" name="noticename" value="" maxlength="21" placeholder="<%=qsrtzrxm %>"/>
				<input type="text" id="phoneInput" class="input_bd div_bd inpstyle1" name="alarmphone" value=""  onblur="phoneInputCtrl($(this));"  onkeyup="phoneInputCtrl($(this));"   maxlength="21" placeholder="<%=qsrtzrsjh %>"/>
				<span class="handle"> <a class="add_handle" href="#"><emp:message key="txgl_wgqdpz_spzhczhs_tj" defVal="添加" fileName="txgl"></emp:message></a>
				| 	<a class="remove_handle" href="#">
						<emp:message key="common_delete" defVal="删除" fileName="common"></emp:message>
					</a>
						<br/>
				</span>
			</div>
		
		</div>
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.placeholder.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/setAlarm.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
 	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
	<script type="text/javascript">

	$(document).ready(function() {
		$('.channel_manage input[placeholder]').each(function(){
				$(this).placeholder({'labelMode':true});	
			})
			var add_handle=$('.add_handle'),
				remove_handle=$('.remove_handle'),
				sim_template=$('.sim_template:first'),
				sim_detail=$('.sim_detail');
			add_handle.live('click',function(e){
				e.preventDefault();
				if($('.sim_detail').size()>=6){
					//alert('最多添加5组通知人信息');
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_55"));
					return;
				}
				var clone=$('#templates').children().clone();
				var index=Number($('.channel_manage .sim_detail:last').attr('data'));
				var newIndex=++index;
				clone.attr('data',newIndex);
				//clone.find('label').html('通知人'+newIndex+'：');
				clone.find('label').html(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_56")+' '+newIndex+'：');
				clone.insertAfter('.channel_manage .sim_detail:last');
				//clone.find('.c_selectBox').remove();
				clone.find('input[placeholder]').placeholder({'labelMode':true});	
			});
			
			remove_handle.live('click',function(e){
				e.preventDefault();
				var fa=$(this).parent().parent();
				if($('.sim_detail').size()>2){
					fa.remove();
					sortHandle();
				}else{
					//alert('至少保留一组通知人信息');
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_57"));
				}
				
			})
			
			$("#threshold").bind('keyup blur',function(){
				var reg=/[^0-9]/g;
				var val=$(this).val();
				if(reg.test(val)){
					$(this).val($(this).val().replace(reg,''));
				}
 			});
			
			function reconstruct(o){
				if(o.value==1 && $(o.box.self).parent().parent().find('select[name="sim_server"]').size()==0){
					 var operaClone=operators_template.children().clone();
					 operaClone.insertAfter($(o.box.self).parent());
					$(o.box.self).parent().parent().find('select[name="sim_server"]').isSearchSelect({'width':'60','isInput':false,'zindex':0});
				 }else if(o.value!=1){
					 $(o.box.self).parent().parent().find('.choose_operators').remove();
				 }
			}
		});
	


	</script>
	</body>
</html>
