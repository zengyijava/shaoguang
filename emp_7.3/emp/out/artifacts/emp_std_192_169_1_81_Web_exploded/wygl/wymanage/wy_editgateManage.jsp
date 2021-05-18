<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="com.montnets.emp.entity.wy.AIpcominfo"%>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.wymanage.vo.ASiminfoVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	AIpcominfo ipcom = (AIpcominfo) request.getAttribute("aipcominfo");
	if(ipcom == null)
	{
		ipcom = new AIpcominfo();
	}
	@SuppressWarnings("unchecked")
	List<ASiminfoVo> simvolist = (List<ASiminfoVo>) request.getAttribute("simvolist");
	
	MessageUtils messageUtils = new MessageUtils();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	//请输入SIM卡号
	 String qsrsimkh = messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_qsrsimkh", request);
   
	//请输入国家
	String qsrgj = messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_qsrgj", request);
	
	//确认
	String qd = messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_qd", request);
	
	//取消
	String qx = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwcwdm_qx", request);
	
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wygl_wytdgl_xgwytd" defVal="修改网优通道" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=iPath%>/css/gateManage.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<style type="text/css">
	
	.a3 {
			width: 35px;
			height:21px;
			 border: 0;
			text-align: center;
			background: white;
		}
		
	.operators{
		width: 200px;
	}
	
	</style>
	</head>

	<body >
			
					<div >
						<div style="display:none" id="hiddenValueDiv"></div>
						<input type="hidden" id="ipcommid" name="ipcommid" value="<%=ipcom.getId() %>"/>
						<div id="addDiv">
				<div class="channel_manage nano">
					<div class="nano-content">
					
						<div class="channel_item clearfix">
							<label class="mod_t1"><emp:message key="txgl_wygl_wytdgl_tdmc" defVal="通道名称" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></label>
							<input type="text" class="input_bd div_bd" name="agatename" id="agatename" value="<%=ipcom.getGatename()!=null?ipcom.getGatename():"" %>" maxlength="16"/>
							<span class="channelTip"><em>*</em></span>
						</div>
						<div class="channel_item clearfix" style="width:600px;">
							<div style="width:131px;float:left;padding-right:2px;">
								<span class="mod_t1"><emp:message key="txgl_wygl_wytdgl_empwgipjdk" defVal="EMP网关IP及端口" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></span>
							</div>
							<%--<input type="text"  class="input_bd div_bd" name="aipadress" id="aipadress" value="<%=ipcom.getIp()%>"  maxlength="16"/>
							--%><%
								String ainnseripadress = ipcom.getPtip()!=null?ipcom.getPtip():"";
								String ip1="";
								String ip2="";
								String ip3="";
								String ip4="";
								if(null != ainnseripadress &&!"".equals(ainnseripadress.trim())){
									String[] ainnseripadresss = ainnseripadress.split("\\.");
									if(ainnseripadresss.length>=4){
										ip1=ainnseripadresss[0];
										ip2=ainnseripadresss[1];
										ip3=ainnseripadresss[2];
										ip4=ainnseripadresss[3];
									}
								}
							 %>
							<div id="ainneripadress" class="div_bd" style="width:172px; background: white;font-size: 8pt;float: left;_margin-left:2px;">
								<input type=text name=ip1 id="ip1" maxlength=3 class=a3 value="<%=ip1 %>"
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip2 id="ip2"  maxlength=3 class=a3 value="<%=ip2 %>"
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip3  id="ip3" maxlength=3 class=a3  value="<%=ip3 %>"
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip4 id="ip4" maxlength=3 class=a3 value="<%=ip4 %>"
								onkeyup="mask(this,event)" onbeforepaste=mask_c()> 
							</div>
							<div style="float:left;">
								<span style="padding-left: 2px;padding-right:2px;"><strong>:</strong></span>
							</div>
							<div style="float:left;">
								<input type="text"  class="input_bd div_bd" name="ainnerportnum" id="ainnerportnum" onkeyup= "if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')" onblur="validatePort(this)" style="width:70px;height: 21px;" placeholder="<emp:message key="txgl_wygl_wytdgl_dkh" defVal="端口号" fileName="txgl"></emp:message>" value="<%=ipcom.getPtport()!=null?ipcom.getPtport():""%>"  maxlength="5"/>
							</div>
							<div style="width:200px;float:left;padding-left:4px;">
								<span class="channelTip zhu"><em>*&nbsp;</em><emp:message key="txgl_wygl_wytdgl_empwgszfwqipjtxdk" defVal="EMP网关所在服务器IP及通讯端口" fileName="txgl"></emp:message></span>
							</div>
						</div>
						<%--<div class="channel_item clearfix">
							<label class="mod_t1">EMP网关端口：</label>
							<input type="text"  class="input_bd div_bd" name="ainnerportnum" id="ainnerportnum"  value="<%=ipcom.getPtport()%>"  maxlength="16"/>
							<span class="channelTip"><em>*</em>EMP网关通讯端口</span>
						</div>
						
						--%>
						<div class="channel_item clearfix" style="width:600px;">
							<div style="width:131px;float:left;padding-right:2px;">
								<span class="mod_t1"><emp:message key="txgl_wygl_wytdgl_empwgipjdk" defVal="运营商网关IP及端口" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></span>
							</div>
							<%--<input type="text"  class="input_bd div_bd" name="aipadress" id="aipadress" value="<%=ipcom.getIp()%>"  maxlength="16"/>
							--%><%
								String aipadress = ipcom.getIp()!=null?ipcom.getIp():"";
								String ip5="";
								String ip6="";
								String ip7="";
								String ip8="";
								if(null != aipadress &&!"".equals(aipadress.trim())){
									String[] aipadresss = aipadress.split("\\.");
									if(aipadresss.length>=4){
										ip5=aipadresss[0];
										ip6=aipadresss[1];
										ip7=aipadresss[2];
										ip8=aipadresss[3];
									}
								}
							 %>
							<div id="aipadress" class="div_bd"	style="width:172px; background: white;font-size: 8pt;float: left;_margin-left:2px;">
								<input type=text name=ip5 id="ip5" maxlength=3 class=a3 value="<%=ip5 %>"
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip6 id="ip6"  maxlength=3 class=a3 value="<%=ip6 %>"
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip7  id="ip7" maxlength=3 class=a3  value="<%=ip7 %>"
								onkeyup="mask(this,event)" onbeforepaste=mask_c()>.
								<input type=text name=ip8 id="ip8" maxlength=3 class=a3 value="<%=ip8 %>"
								onkeyup="mask(this,event)" onbeforepaste=mask_c()> 
							</div>
							<div style="float:left;">
								<span style="padding-left:2px;padding-right:2px;"><strong>:</strong></span>
							</div>
							<div style="float:left;">
								<input type="text"  class="input_bd div_bd" style="width:70px;height: 21px;" name="aportnum" id="aportnum" onblur="validatePort(this)" onkeyup= "if(value != value.replace(/[^\d]/g,'')) value = value.replace(/[^\d]/g,'')"  value="<%=ipcom.getPort()!=null?ipcom.getPort():"" %>"  placeholder="<emp:message key="txgl_wygl_wytdgl_dkh" defVal="端口号" fileName="txgl"></emp:message>" maxlength="5"/>
							</div>
							<div style="width:200px;float:left;padding-left:4px;">
								<span class="channelTip zhu"><em>*&nbsp;</em><emp:message key="txgl_wygl_wytdgl_empwgszfwqipjtxdk" defVal="IPCOM设备的IP地址及通讯端口" fileName="txgl"></emp:message></span>
							</div>
						</div>
						<%--<div class="channel_item clearfix">
							<label class="mod_t1">运营商网关端口：</label>
							<input type="text"  class="input_bd div_bd" name="aportnum" id="aportnum"  value="<%=ipcom.getPort()%>"  maxlength="16"/>
							<span class="channelTip"><em>*</em>IPCOM设备的监听端口号</span>
						</div>
						
						--%>
						<div class="channel_item clearfix">
							<div style="width:131px;float:left;padding-right:2px;">
								<span class="mod_t1"><emp:message key="txgl_wygl_wytdgl_dxqm" defVal="短信签名" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></span>
							</div>
							<div style="float:left;">
								<input type="text"  class="input_bd div_bd" name="corpsign" id="corpsign"  value="<%=ipcom.getCorpsign()!=null?ipcom.getCorpsign():"" %>"  maxlength="10"/>
							</div>
							<div style="width:240px;float:left;padding-left:4px;">
								<span class="channelTip zhu"><emp:message key="txgl_wygl_wytdgl_gsbxbhbjzkhrmwkj" defVal="格式必须包含半角中括号，如：[梦网科技]" fileName="txgl"></emp:message></span>
							</div>
						</div>
						
						<%
							int zIndex=999;
									if(simvolist != null && simvolist.size() > 0)
									{
										for (ASiminfoVo simvo : simvolist)
										{
											--zIndex;
								%>
						<div class="channel_item sim_detail clearfix" data="<%=simvo.getSimno() %>" style="width:590px;z-index:<%=zIndex %>">
								
								<label class="mod_t1">
									<emp:message key="txgl_wygl_wytdgl_simk" defVal="SIM卡" fileName="txgl"></emp:message><%=simvo.getSimno() %><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message>
								</label>
								<input type="text" name="sim_number" class="input_bd div_bd" value="<%=simvo.getPhoneno()!=null?simvo.getPhoneno():0 %>" onblur="phoneInputCtrl($(this));"  onkeyup="phoneInputCtrl($(this));"  maxlength="21" placeholder="<%=qsrsimkh %>"/><em style="color: red;">*</em>
								<input type="text" class="input_bd div_bd" style="width: 90px"	name="country" value="<%=simvo.getAreaname()!=null?simvo.getAreaname():"" %>" maxlength="16" placeholder="<%=qsrgj %>"/>
								<div class="operators" style="width:200px;z-index:<%=zIndex %>">
									<div class="choose_operators">
										<select name="sim_server">
											<option value="0" <%if("0".equals(simvo.getUnicom()!=null?simvo.getUnicom().toString():"")){ %> selected="selected" <%} %>>
												<emp:message key="txgl_wygl_wytdgl_yd" defVal="移动" fileName="txgl"></emp:message>
											</option>
											<option value="1" <%if("1".equals(simvo.getUnicom()!=null?simvo.getUnicom().toString():"")){ %> selected="selected" <%} %>>
												<emp:message key="txgl_wygl_wytdgl_lt" defVal="联通" fileName="txgl"></emp:message>
											</option>
											<option value="21" <%if("21".equals(simvo.getUnicom()!=null?simvo.getUnicom().toString():"")){ %> selected="selected" <%} %>>
												<emp:message key="txgl_wygl_wytdgl_dx" defVal="电信" fileName="txgl"></emp:message>
											</option>
											<option value="5" <%if("5".equals(simvo.getUnicom()!=null?simvo.getUnicom().toString():"")){ %> selected="selected" <%} %>>
												<emp:message key="txgl_wygl_wytdgl_gj" defVal="国际" fileName="txgl"></emp:message>
											</option>
										</select>
									</div>
									<span class="handle"> <a class="add_handle" href="#"><emp:message key="txgl_wygl_gjdmgl_tj" defVal="添加" fileName="txgl"></emp:message></a>
										| <a class="remove_handle" href="#"><emp:message key="txgl_wygl_gjdmgl_sc" defVal="删除" fileName="txgl"></emp:message></a><br/></span>
									
								</div>

									
							</div>
						<%
										}
									%>
									<% 
									}
									%>
							
						<div class="channel_item mb20 bz clearfix" style="position:static;">
							<label class="mod_t1"><emp:message key="txgl_wygl_wytdgl_bz" defVal="备注" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></label>
							<textarea class="input_bd div_bd"  name="acommon" id="acommon" onblur="if(this.value.length > 100)this.value=this.value.substring(0,100)"  onkeyup="if(this.value.length > 100)this.value=this.value.substring(0,100)" ><%=ipcom.getCommon()!=null?ipcom.getCommon():"" %></textarea>
						</div>
						<div  align="center">
							<input class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="javascript: updateGateManage(arguments[0]);"/>
							<input class="btnClass6" type="button" value="<%=qx %>" onclick="javascript:doCancelEdit('#editWyGateDiv');"/>
							<br/>
						</div>
					</div>
					</div>
					</div>
					</div>
    <div class="clear"></div>
    <div id="templates" style="display:none;">
			<div class="channel_item sim_detail clearfix" data="1" style="width:590px;">
							<label class="mod_t1"><emp:message key="txgl_wygl_wytdgl_simk" defVal="SIM卡" fileName="txgl"></emp:message>1<emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></label>
							<input type="text" name="sim_number" class="input_bd div_bd" value="" onblur="phoneInputCtrl($(this));"  onkeyup="phoneInputCtrl($(this));" maxlength="21" placeholder="<%=qsrsimkh %>" /><em style="color: red;">*</em>
							<input type="text"  class="input_bd div_bd" style="width:90px" name="country"  maxlength="16" placeholder="<%=qsrgj %>"/>
							<div class="operators">
							<div class="choose_operators">
								<select name="sim_server">
									<option value="0"><emp:message key="txgl_wygl_wytdgl_yd" defVal="移动" fileName="txgl"></emp:message></option>
									<option value="1"><emp:message key="txgl_wygl_wytdgl_lt" defVal="联通" fileName="txgl"></emp:message></option>
									<option value="21"><emp:message key="txgl_wygl_wytdgl_dx" defVal="电信" fileName="txgl"></emp:message></option>
									<option value="5"><emp:message key="txgl_wygl_wytdgl_gj" defVal="国际" fileName="txgl"></emp:message></option>
								</select>
							</div>
							<span class="handle">
								<a class="add_handle" href="#"><emp:message key="txgl_wygl_gjdmgl_tj" defVal="添加" fileName="txgl"></emp:message></a>
						  	  	|
						  	  	<a class="remove_handle" href="#"><emp:message key="txgl_wygl_gjdmgl_sc" defVal="删除" fileName="txgl"></emp:message></a><br/>
							</span>
							
							</div>
							
							
						</div>
		
		</div>
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script language="javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.placeholder.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    	<script type="text/javascript" src="<%=iPath%>/js/gateManage.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    	<script type="text/javascript" src="<%=iPath%>/js/ip.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>

	<script type="text/javascript">

	$(document).ready(function() {
		$('.channel_manage input[placeholder]').each(function(){
				$(this).placeholder({'labelMode':true});	
			})
			$('.channel_manage').click(function(){
					$('.channel_manage input[name="sim_number"]').removeClass('no-unique');
			})
			var add_handle=$('.add_handle'),
				remove_handle=$('.remove_handle'),
				sim_template=$('.sim_template:first'),
				sim_detail=$('.sim_detail');
			createOpera();
			add_handle.live('click',function(e){
				e.preventDefault();
				var clone=$('#templates').children().clone();
				var index=Number($('.channel_manage .sim_detail:last').attr('data'));
				var newIndex=++index;
				var zIndex=$('.channel_manage .operators:last').css('zIndex');
				clone.attr('data',newIndex);
				clone.find('.operators').css({'z-index':--zIndex});
				//clone.find('label').html('SIM卡'+newIndex+'：');
				<%if(StaticValue.ZH_HK.equals(langName)){%>
					clone.find('label').html('SIM Card'+newIndex+'：');
				<%}else{%>
					clone.find('label').html('SIM卡'+newIndex+'：');
				<%}%>
				clone.insertAfter('.channel_manage .sim_detail:last');
				clone.find('.c_selectBox').remove();
				clone.find('select').isSearchSelect({'width':'90','isInput':false,'zindex':1});
				clone.find('input[placeholder]').placeholder({'labelMode':true});	
			});
			
			remove_handle.live('click',function(e){
				e.preventDefault();
				var fa=$(this).parent().parent().parent();
				if($('.sim_detail').size()>2){
					fa.remove();
					sortHandle();
				}else{
					//alert('至少保留一组SIM卡');
					alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_1"));
				}
				
			})
			function createOpera(){
				$('.channel_item select').isSearchSelect({'width':'90','isInput':false,'zindex':1});
			}
			
			function reconstruct(o){
				if(o.value==1 && $(o.box.self).parent().parent().find('select[name="sim_server"]').size()==0){
					 var operaClone=operators_template.children().clone();
					 operaClone.insertAfter($(o.box.self).parent());
					$(o.box.self).parent().parent().find('select[name="sim_server"]').isSearchSelect({'width':'60','isInput':false,'zindex':0});
				 }else if(o.value!=1){
					 $(o.box.self).parent().parent().find('.choose_operators').remove();
				 }
			}
			 
			getLoginInfo("#hiddenValueDiv");
			
			
			
		});
	


	</script>
	</body>
</html>
