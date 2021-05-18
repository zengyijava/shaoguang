<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.entity.monitoronline.LfMonOnlcfg"%>
<%@page import="com.montnets.emp.common.biz.SmsBiz"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	LfMonOnlcfg onlUser = (LfMonOnlcfg) request.getAttribute("onlUser");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
    @ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("onlUserNumCfg");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String maxonline = "";
	String monfreq = "";
	String monphone = "";
	String monemail = "";
	Integer monstatus = 1;
	String id = "";
	if(onlUser!=null){
		maxonline = String.valueOf(onlUser.getMaxonline());
		monfreq = String.valueOf(onlUser.getMonfreq());
		if(onlUser.getMonphone()!=null){
			monphone = onlUser.getMonphone();
		}
		if(onlUser.getMonemail()!=null){
			monemail = onlUser.getMonemail().trim();
		}
		id = String.valueOf(onlUser.getId());
		monstatus = onlUser.getMonstatus();
		if(monstatus==null){monstatus=1;}
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
   	<head>
	<title><emp:message key="ptjk_jkgl_zxyhs_1" defVal="在线用户数监控设置" fileName="ptjk"/></title>
	<%@include file="/common/common.jsp" %>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<style>
	#toEdit table{
		line-height:35px;
		font-size: 12px;
	}
	</style>
  </head>
  
  <body>
   		<div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<%-- 内容开始 --%>
		<input type="hidden" id="dialog_flag" value="1"/>
		<div id="toEdit" title="<emp:message key='ptjk_jkgl_zxyhs_2' defVal='设置在线用户信息' fileName='ptjk'/>" style="display:none;font-size: 12px;">
		<form id="set_online">
		<div style="height:20px"></div>
			<center>
			<table>
				<tr>
					<td align=left ><emp:message key="ptjk_jkgl_zxyhs_3" defVal="在线用户数告警阀值：" fileName="ptjk"/></td>
					<td align="left">
					<input type="text" id="maxOnline" value="<%=maxonline%>" style="width:150px" class="input_bd int" maxlength="8"/>
					</td>
					<td align="right">
					<span style="color: #cccccc;" ><emp:message key="ptjk_jkgl_zxyhs_5" defVal="(单位：人)" fileName="ptjk"/></span>
					</td>
				</tr>
				<tr>
					<td align="left"><emp:message key="ptjk_jkgl_zxyhs_4" defVal="刷新频率：" fileName="ptjk"/></td>
					<td align="left">
					<input type="text" id="monFreq" value="<%=monfreq %>" style="width:150px" class="input_bd int" maxlength="6"  disabled='disabled'/>
					<input type="hidden" id="id" value="<%=id %>" />
					</td>
					<td align="right">
					<span style="color: #cccccc;" ><emp:message key="ptjk_jkgl_zxyhs_6" defVal="(单位：秒)" fileName="ptjk"/></span>
					</td>
				</tr>
				<tr>
					<td align="left"><emp:message key="ptjk_common_gjsjh_mh" defVal="告警手机号：" fileName="ptjk"/></td>
					<td align="left">
					<textarea name="monphone" id="monphone" class="input_bd" style="width:150px;"><%=monphone%></textarea>
					</td>
					<td align="right" width="132px" style="line-height: 18px;">
						<span style="color: #cccccc;" ><emp:message key="ptjk_jkgl_tdzh_19" defVal="可以设置至多十个手机号，号码间用逗号(,)分隔" fileName="ptjk"/></span>
					</td>
				</tr>
				<tr>
					<td align="left"><emp:message key="ptjk_common_gjyx_mh" defVal="告警邮箱：" fileName="ptjk"/></td>
					<td align="left">
					<textarea name="monemail" id="monemail" class="input_bd" style="width:150px;"><%=monemail%></textarea>
					</td>
					<td align="right" width="132px" style="line-height: 18px;">
						<span style="color: #cccccc;" ><emp:message key="ptjk_common_znszygyx" defVal="只能设置一个邮箱" fileName="ptjk"/></span>
					</td>
				</tr>
				<tr>
					<td align="left"><emp:message key="ptjk_common_jkzt_mh" defVal="监控状态：" fileName="ptjk"/></td>
					<td align="left">
						<select id="monstatus" style="width:154px" class="input_bd">
							<option value="1"><emp:message key="ptjk_common_jk" defVal="监控" fileName="ptjk"/></option>
							<option value="0" <%if(monstatus==0){%>selected="selected"<%}%>><emp:message key="ptjk_common_wjk" defVal="未监控" fileName="ptjk"/></option>
						</select>
					</td>
					<td align="right">
					</td>
				</tr>
			</table>
			</center>
			<div style="height: 60px;line-height: 60px;margin-top: 10px;">
			    <center>
	    		<input id="ok" class="btnClass5" type="button" value="<emp:message key='ptjk_common_qd' defVal='确  定' fileName='ptjk'/>" onclick="javascript:btok()"/>
			    <input id="cancel" onclick="javascript:btcancel();" class="btnClass6" type="button" value="<emp:message key='ptjk_common_qx' defVal='取  消' fileName='ptjk'/>" />
			    <br/>
			    </center>
	    	</div>
	    </form>
		</div>
			
		<div id="rContent" class="rContent">
		<form name="pageForm" id="pageForm">
				<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="ptjk_jkgl_zxyhs_7" defVal="在线用户数告警阀值(人)" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_jkgl_zxyhs_8" defVal="刷新频率(秒)" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_common_gjsjh" defVal="告警手机号" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_common_gjyx" defVal="告警邮箱" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_common_jkzt" defVal="监控状态" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_common_xgsj" defVal="修改时间" fileName="ptjk"/>
								</th>
								<th>
									<emp:message key="ptjk_common_cz" defVal="操作" fileName="ptjk"/>
								</th>
							</tr>
						</thead>
						<tbody>
						<%
						if (onlUser != null)
							{
						%>
							<tr>
								<td>
									<%=onlUser.getMaxonline() %>
								</td>
								<td>
									<%=onlUser.getMonfreq() %>
								</td>
								<td>
									<%=onlUser.getMonphone()==null?"":onlUser.getMonphone()%>
								</td>
								<td>
									<%=onlUser.getMonemail()==null?"":onlUser.getMonemail().trim()%>
								</td>
								<td>
									<%if(onlUser.getMonstatus()!=null&&onlUser.getMonstatus()==0){
										out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wjk",request));
									} else{
										out.print(MessageUtils.extractMessage("ptjk","ptjk_common_jk",request));
									}%>
								</td>
								<td>
									<%=onlUser.getModifytime()!=null?df.format(onlUser.getModifytime()):"-" %>
								</td>
								<td>
								<%if(btnMap.get(menuCode+"-1")!=null && new SmsBiz().isWyModule("16")) { %>
									<a href="javascript:toEeit()"><emp:message key="ptjk_common_sz" defVal="设置" fileName="ptjk"/></a>
								<%}else{out.print("-");} %>
								</td>
							</tr>
						<%
							}else{
						%>
								<tr><td align="center" colspan="10"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr>
							<%} %>
						</tbody>
				</table>
		</form>
		</div>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
	    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script src="<%=iPath%>/js/text.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
		<script type="text/javascript">
		$(document).ready(function() {
			var findresult="<%=(String)request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
		       alert(getJsLocaleMessage("ptjk","ptjk_jkxq_zxyhs_1"));
		       return;			       
		    }
			$("#content tbody tr").hover(function() {
						$(this).addClass("hoverColor");
			}, function() {
						$(this).removeClass("hoverColor");
			});
		});
		
		function toEeit(){
			$('#toEdit').dialog({
				autoOpen: false,
				width: <%=StaticValue.ZH_HK.equals(langName)?570:500%>,
				height:320,
				def_height:320,
				resizable:false,
				modal:true,
				open: function(){
				$("#monphone").keydown();
				},
				close:function(){
				$('#set_online')[0].reset();
				}
			});
			$('#toEdit').dialog('open');
		}
		function btcancel(){
			$('#toEdit').dialog('close');
		}
		
		//验证邮箱是否有效
		function checkEmail(email_str)
		{
			var arr = [ "ac", "com", "net", "org", "edu", "gov", "mil", "ac\.cn",
				"com\.cn", "net\.cn", "org\.cn", "edu\.cn" ];
			var temp_arr = arr.join("|");
			// reg
			var reg_str = "^[0-9a-zA-Z](\\w|-)*@\\w+\\.(" + temp_arr + ")$";
			var reg = new RegExp(reg_str);
			if (reg.test(email_str)) {
				return true;
			}else{
				return false;
			}
		}
		
		function btok(){
			var maxOnline = $.trim($('#maxOnline').val());
			var monFreq =  $.trim($('#monFreq').val());
			var id = $.trim($('#id').val());
			if(maxOnline.length==0){
					alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zxyhs_1"));
					return false;
				}
			if(monFreq.length==0){
				alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zxyhs_2"));
				return false;
			}
			//判断手机号码格式
			var phone=$("#monphone").val();
			var monstatus = $("#monstatus").val();
			//如果手机号码有值 则必须满足格式验证
			if(!validatePhones($("#monphone"))){
				return false;
			}
			//告警手机号为空 则 进行确认
			if(phone.length==0&&!confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_9"))){
				return false;
			}
			
			//判断邮箱格式
			var monemail=$("#monemail").val();
			if(monemail==" "){monemail="";}
			//告警邮箱为空 则 进行确认
			if((monemail==null || monemail.length==0) && !confirm(getJsLocaleMessage("ptjk","ptjk_jkgl_tdzh_3"))){
				return false;
			}
			if(monemail!=null){

				if(!monemail.length==0 && !checkEmail(monemail)){
						alert(getJsLocaleMessage("ptjk","ptjk_wljk_web_2"));
						return false;
				}
			}			
			var lguserid = $("#lguserid").val();
			var lgcorpcode = $("#lgcorpcode").val();
			var monphone=$("#monphone").val();
			$("#toEdit").find("input[type='button']").attr("disabled","disabled");
			$.ajax({
				url:"mon_gateAcctMonCfg.htm?method=checkPhone",
				method:"POST",
				async:true,
				data:{
					lgcorpcode:lgcorpcode,
					lguserid: lguserid,
					monphone:monphone,
					isAsync:"yes"
				},
				success:function(result){
					if(result == "outOfLogin")
					{
						$("#logoutalert").val(1);
						location.href=pathUrl+"/common/logoutEmp.html";
						return;
					}
					else if(result=="error"||result=="phoneError")
					{
						alert(getJsLocaleMessage("ptjk","ptjk_wljk_web_1"));
						$("#toEdit").find("input[type='button']").attr("disabled","");
					}
					else if(!monemail.length==0 &&!checkEmail(monemail)){
						alert(getJsLocaleMessage("ptjk","ptjk_wljk_web_2"));
						$("#rContent").find("input[type='button']").attr("disabled","");
					}
					else
					{
						$.post("mon_onlUserNumCfg.htm?method=toEdit",{maxonline:maxOnline,monFreq:monFreq,id:id,monphone:phone,monemail:monemail,monstatus:monstatus},function(result){
							if(result == "success"){
								alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zxyhs_3"));
								window.location.href = "mon_onlUserNumCfg.htm?method=find";
							}
							else{
								$('#toEdit').dialog('close');
								alert(getJsLocaleMessage("ptjk","ptjk_jkgl_zxyhs_4"));
								$("#toEdit").find("input[type='button']").attr("disabled","");
							}
						});	
					}
				}
				
			});
			
		}
		
		</script>
  </body>
</html>
