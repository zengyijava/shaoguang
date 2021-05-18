<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.monitor.constant.MonitorStaticValue"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String findresult = (String)request.getAttribute("findresult");
String keyword = request.getParameter("keyword");
if(keyword==null){keyword="";}
%>
<!doctype html>
<html>
<head>
<meta http-equiv="refresh" content="1800" />
	<title>Document</title>
	<%@include file="/common/common.jsp" %>
	<link rel="stylesheet" href="<%=iPath%>/style/jk.css?V=<%=StaticValue.getJspImpVersion()%>">
	<link rel="stylesheet" href="<%=iPath%>/style/nanoscroller.css?V=<%=StaticValue.getJspImpVersion()%>">
	<!--[if IE]>
		<script type="text/javascript" src="<%=iPath%>/js/html5.js"></script>
   		<script type="text/javascript" src="<%=iPath%>/js/excanvas.js"></script>
	<![endif]-->
	<!--[if IE 6]>
        <script type="text/javascript" src="<%=iPath%>/js/DD_belatedPNG.js"></script>
        <script language="javascript" type="text/javascript">
        DD_belatedPNG.fix(".headBar,.mon-body i,.png");
        </script>
    <![endif]-->
    <script>
    var ipath="<%=iPath%>";
    var pathUrl = "<%=path%>";
    var getTime=new Date().getTime();
    </script>
    <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
	<%if(!(null==langName||langName.equals(""))){%>
	<style type="text/css">
		.JK_wrapper #main .slogan-m{
			width:308px;
			float: left;
			background: url(<%=iPath%>/images/appbarm_<%=langName%>.jpg) no-repeat 0 0;
		}
		.JK_wrapper #main .slogan-l .inner{
			margin:0 0 0 156px;
			background: url(<%=iPath%>/images/appbarl_<%=langName%>.jpg) no-repeat 0 0;
		}
		.JK_wrapper #main .slogan-r .inner{
			margin:0 0 0 156px;
			background: url(<%=iPath%>/images/appbarr_<%=langName%>.jpg) no-repeat 100% 0;
		}
	</style>
	<%}%>
</head>
<body>
	<input type="hidden" name="findresult" id="findresult" value="<%=findresult %>">
	<input type="hidden" name="ismonvoice" id="ismonvoice" value="">
	<input type="hidden" name="keyword" id="keyword" value="<%=keyword %>">
	<%--是否有告警信息  1有，2没有 --%>
	<input type="hidden" name="isHaveMon" id="isHaveMon" value="">
	<input type="hidden" id="ipath" name="ipath" value="<%=iPath %>"/>
	<input type="hidden" id="pathUrl" name="pathUrl" value="<%=path %>"/>
	<div id="loginInfo"></div>

	<div class="JK_wrapper">
		<div id="main">
			
			<div id="mainContainer">
			<div class="mainContent">
				<div class="slogan clearfix">
					<div class="slogan-l">
						<div class="inner">
							<ul class="arrowBox">
								
							</ul>
						</div>
					</div>
					<div class="slogan-m"></div>
					<div class="slogan-r">
						<div class="inner">
							<ul class="arrowBox">
								
							</ul>
						</div>
					</div>
				</div>
				<div class="chart-left-column">
				 <div class="inner"></div>
				</div>
				<div class="chart-main-column png">
								<div class="total_count"><p><emp:message key="ptjk_jkst_jkst_jrzl_mh" defVal="今日总量：" fileName="ptjk"/><span id="volume"></span><emp:message key="ptjk_jkst_jkst_t" defVal="条" fileName="ptjk"/></p></div>
					<div class="titleBar">
						<span class="gateway" id="gateway" style="cursor: pointer;" onclick="toSpPrcMon('')"><img src="" alt=""><emp:message key="ptjk_common_empwg" defVal="EMP网关" fileName="ptjk"/></span>
						<span class="interface png" style="cursor: pointer;" onclick="toGateAcct()"><img src="" alt=""><emp:message key="ptjk_common_yysjk" defVal="运营商接口" fileName="ptjk"/></span>
					</div>
					<div class="middle-content">
						<div class="toBeSend">
							<div class="histogram">
								<div class="actual">
									<p><emp:message key="ptjk_jkst_jkst_df" defVal="待发" fileName="ptjk"/><br /><span id="noSend"></span><emp:message key="ptjk_jkst_jkst_t" defVal="条" fileName="ptjk"/></p>
								</div>
								<div class="havesend">
								<p><emp:message key="ptjk_jkst_jkst_yf" defVal="已发" fileName="ptjk"/><br /><span id="send"></span><emp:message key="ptjk_jkst_jkst_t" defVal="条" fileName="ptjk"/></p>
								</div>
							</div>
							<div class="canvasBox">
								<canvas id="myCanvas"></canvas>
							</div>
							
						</div>
						<div class="interface-data" id="interface-data">
						</div>
					</div>
				
				</div>
				
				<div class="chart-right-column">
				<div class="inner"></div>
				</div>
				</div>
			</div>
			<div class="explain">
				<ul>
					<li><i></i><emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></li>
					<li class="abnormal"><i></i><emp:message key="ptjk_common_jg" defVal="警告" fileName="ptjk"/></li>
					<li class="malfunction"><i></i><emp:message key="ptjk_common_yz" defVal="严重" fileName="ptjk"/></li>
					<li class="voice"><emp:message key="ptjk_jkst_jkst_sy" defVal="声音" fileName="ptjk"/><a href="javascript:void(0)" class="showVoice png "></a></li>
					<li class="ml10"><emp:message key="ptjk_jkst_jkst_zxrs_mh" defVal="在线人数:" fileName="ptjk"/><span id="personTotal" style="cursor:pointer;" onclick="toOnline()"><a id="onlineUser"></a></span></li>
				</ul>
			</div>
		</div>
		<div id="rightcolumn">
			<div class="mod1 warning_info">
				<div class="hd"><span class="png"><emp:message key="ptjk_jkst_jkst_gjxx" defVal="告警消息" fileName="ptjk"/></span></div>
				<div class="bd nano">
					<div class="content"></div>
				</div>
			</div>
			<div class="mod1 server_info">
				<div class="hd"><span class="png"><emp:message key="ptjk_jkst_jkst_fwqzk" defVal="服务器状况" fileName="ptjk"/></span></div>
				<div class="bd nano">
					<div class="content"></div>
				</div>
			</div>
		</div>
	</div>
	<div class="mp3" style="position:absolute;left:-999em;"><%=iPath %>/media/flash/warning.mp3</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=iPath%>/js/myjquery-d.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script src="<%=path%>/common/widget/artDialog/artDialog.js?skin=default"></script>
	<script src="<%=path%>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/lib/nano/jquery.nanoscroller.min.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/mainMon.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/jquery.jmp3.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/fishcomponent.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/Monitor.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript">
	var timer=null,rollTime=null,ballTimerArr=[];
	var ie=!!window.ActiveXObject;
	var ie6=ie&&!window.XMLHttpRequest;
	var tiao = getJsLocaleMessage("ptjk","ptjk_jkst_jkst_2");
	var tiaos = getJsLocaleMessage("ptjk","ptjk_jkst_jkst_7");
		$(document).ready(function() {
			getLoginInfo("#loginInfo");
			getData();
			setInterval("getData()",<%=MonitorStaticValue.getRefreshTime()%>);
			$(window).resize(function(){
				Monitor.Events.slogan();
			})
		});
		function getData(){
			var path = $("#pathUrl").val();
			var ipath = $("#ipath").val();
			var keyword = $("#keyword").val();
			var nosend_h,havesend_h,lineBgColor;
			var histogram_h=$('.histogram').height();
			
			$.ajax({ 
		        type : "post", 
		        url : path+'/mon_mainMon.htm',
		        data : 'method=getJson&keyword='+keyword+'&lguserid=2&isAsync=yes', 
		        async : false,
		        success : function(data){ 
					if(data == "outOfLogin")
					{
						location.href=path+"/common/logoutEmp.html";
						return;
					}
					var data =eval('('+data+')');
					var MonitorObj={
						ismonvoice:$("#ismonvoice"),
						isHaveMon:$("#isHaveMon"),
						onlineUser:$("#onlineUser"),
						titleBar:$(".titleBar"),
						volume:$("#volume"),
						total_count:$(".total_count"),
						voiceA:$(".voice a"),
						actual:$(".actual"),
						havesend:$(".havesend"),
						noSend:$("#noSend"),
						send:$("#send"),
						leftInner:$(".chart-left-column .inner:eq(0)"),
						rightInner:$(".chart-right-column .inner:eq(0)")
					};
					
					MonitorObj.onlineUser.html(data.online);
					MonitorObj.ismonvoice.val(data.ismonvoice);
					MonitorObj.isHaveMon.val(data.isHaveMon);
					MonitorObj.titleBar.children().eq(0).removeClass().addClass("gateway").addClass(data.wbsprcstyle);
					MonitorObj.titleBar.children().eq(0).children("img").attr('src',ipath+"/images/ani-"+data.wbsprcstyle+"-1.gif");
					MonitorObj.titleBar.children().eq(1).removeClass().addClass("interface png").addClass(data.gterrstyle);
					MonitorObj.titleBar.children().eq(1).children("img").attr('src',ipath+"/images/ani-"+data.gterrstyle+"-1.gif");
					MonitorObj.volume.html(countView(data.volume));
					MonitorObj.volume.parent().attr('title',getJsLocaleMessage("ptjk","ptjk_jkst_jkst_1")+data.volume+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_2"));//增加提交总量的提示
					MonitorObj.voiceA.removeClass().addClass("showVoice").addClass("png").addClass(data.showvoice);
					/*
					 * 只有待发：待发填满柱子
					 * 只有已发：已发填满柱子
					 * 待发大于0小于X：待发使用保底
					 * 待发大于X小于已发保底：按正常比率显示
					 * 已发大于0小于X：已发使用保底
					 * 已发大于X小于待发保底：按正常比率显示
					 */
					var _h=data.actualheight*histogram_h*0.01;
					if(_h>0 && _h<=40){
						nosend_h=40;
						
					}else if(_h>40 && data.actualheight<=100){
						nosend_h=_h>=Math.floor(histogram_h-40) ? Math.floor(histogram_h-40) : _h;
						
					}
					if(data.actualheight==0 && data.bufsendcount==0){
						nosend_h= Math.floor(histogram_h*0.5);
					}
					
					if(data.actualheight==0 && data.bufsendcount!=0){
						nosend_h= 0;
					}
					if(data.actualheight!=0 && data.bufsendcount==0){
						nosend_h= histogram_h;
					}
					
					havesend_h=histogram_h-nosend_h;
					MonitorObj.actual.css('height',nosend_h+"px");
					MonitorObj.havesend.css('height',havesend_h+"px");
					MonitorObj.noSend.html(countView(data.waitsendcount));
					MonitorObj.noSend.parent().attr('title',getJsLocaleMessage("ptjk","ptjk_jkst_jkst_3")+data.waitsendcount+tiao);
					MonitorObj.send.html(countView(data.bufsendcount));
					MonitorObj.send.parent().attr('title',getJsLocaleMessage("ptjk","ptjk_jkst_jkst_4")+data.bufsendcount+tiao);
					
					
					hostinfo(data.host);
					warninfo(data.warn);
					leftinfo(data.left);
					var lineBgColor=rightinfo(data.right);
					maininfo(data.main);
					MonitorObj.leftInner.append("<p class=\"mon-total\" title=\""+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_5")+data.spwaitsendcount+tiao+"\" >"+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_5")+countView(data.spwaitsendcount)+tiao+"</p>");
					MonitorObj.rightInner.append("<p class=\"mon-total buf\" title=\""+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_6")+data.bufsendcount+tiao+"\">"+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_6")+countView(data.bufsendcount)+tiao+"</p>")
		        	closeVoice();
					Monitor.config=null;
					if(ie){
						$.getScript("<%=iPath%>/js/html5.js",function(){
			        		$.getScript("<%=iPath%>/js/excanvas.js",function(){
			        			Monitor.init(lineBgColor);
			        		});
			        	});
					}else{
						Monitor.init(lineBgColor);
					}
					MonitorObj=null;
					data=null;
					
		        } 
			})
			
			}
		function closeVoice()
		{
			clearTimeout(rollTime);
			clearInterval(timer);
			$('.mp3').html('');
		}

		function leftinfo(data){
			var $div = $(".chart-left-column >.inner");
			$div.html('');
			var path = $("#ipath").val();
			var tempArr = [];
			var spaccountid,style,ballstyle,mtremained,mtsndspd;
			for(var i=0;i<data.length;i++){
				spaccountid = data[i].spaccountid;
				style = data[i].style;
				ballstyle = data[i].ballstyle;
				mtremained = data[i].mtremained;
				mtsndspd = data[i].mtsndspd;
				linestyle = data[i].linestyle;
				tempArr.push("<div class='monitor "+style+"' >");
				tempArr.push("<div class='mon-body' style='cursor: pointer;' onclick=\"toSpAcctMon('"+spaccountid+"')\">");
				tempArr.push("<span class='light'></span>");
				tempArr.push("<img class='png' src=\""+path+"/images/ani-malfunction.png\">");
				tempArr.push("<ul><li class='weight' title=\""+spaccountid+"\">"+spaccountid+"</li>");
				tempArr.push("<li title=\""+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_3")+mtremained+tiao+"\">"+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_3")+countView(mtremained)+tiao+"</li></ul>");
				tempArr.push("</div><div class='mon-side'><div class='inner'>");
				if(countView(mtsndspd)){
					tempArr.push("<div class='mon-rate' speed=\""+mtsndspd+"\" title=\""+mtsndspd+tiaos+"\">"+countView(mtsndspd)+tiaos+"</div>");
				}else{
					tempArr.push("<div class='mon-rate'>&nbsp;</div>");
				}
				tempArr.push("<div class='lineBar'><div class='"+linestyle+" mon-line'></div><i class='mon-line-ball "+ballstyle+"'></i></div></div></div></div>");
				
			}
			
			$div.prepend(tempArr.join(''));
			tempArr=null;
			$div=null;
			}
		function maininfo(data){
			var $div = $(".chart-main-column").find(".interface-data");
			$div.html('');
			var path = $("#ipath").val();
			var tempArr = [];
			var gatewayid,style,gatename,mtremained,acctname;
			for(var i=0;i<data.length;i++){
				style = data[i].style;
				gatename = data[i].gatename;
				gateaccount = data[i].gateaccount;
				mtremained = data[i].mtremained;
				acctname = data[i].acctname;
				tempArr.push("<div class='monitor "+style+"' style=\"cursor: pointer;\" onclick=\"toGateAcctById('"+gateaccount+"')\">");
				tempArr.push("<div class='mon-body'>");
				tempArr.push("<i class='light'></i>");
				tempArr.push("<img src=\""+path+"/images/ani-"+style+".gif\">");
				tempArr.push("<ul><li class='weight' title=\""+gatename+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_16")+"\">"+gatename+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_16")+"</li>");
				tempArr.push("<li title=\""+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_14")+mtremained+tiao+"\">"+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_14")+countView(mtremained)+tiao+"</li></ul></div><div class='mon-side'>");
				tempArr.push("<div class=\"mon-dashed\"></div></div></div>");
				}
			$div.html(tempArr.join(''));
			tempArr=null;
			$div=null;
			}
		function rightinfo(data){
			var $div = $(".chart-right-column >.inner");
			$div.html('');
			var path =$("#ipath").val();
			var tempArr = [];
			var gatename,acctname,mtsdcnt,mtspd1,lineColor=[];
			//var bufsendcount;
			for(var i=0;i<data.length;i++){
				gatename = data[i].gatename;
				mtsdcnt = data[i].mtsdcnt;
				mtspd1 = data[i].mtspd1;
				style = data[i].style;
				linestyle = data[i].linestyle;
				lineColor.push(linestyle);
				ballstyle = data[i].ballstyle;
				tempArr.push("<div class='monitor'>");
				tempArr.push("<div class='mon-body'>");
				tempArr.push("<span class='light'></span>");
				tempArr.push("<img class='png' src=\""+path+"/images/ani-normal.png\">");
				tempArr.push("<ul><li class='weight' title='"+gatename+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_8")+"'>"+gatename+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_8")+"</li>");
				tempArr.push("<li title=\""+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_15")+mtsdcnt+tiao+"\">"+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_15")+countView(mtsdcnt)+tiao+"</li></ul></div><div class='mon-side'><div class='inner'>");
				if(countView(mtspd1)){
					tempArr.push("<div class='mon-rate' speed=\""+mtspd1+"\" title=\""+mtspd1+tiaos+"\">"+countView(mtspd1)+tiaos+"</div>");
				}else{
					tempArr.push("<div class='mon-rate'>&nbsp;</div>");
				}
				tempArr.push("<div class='lineBar "+style+"'><div class='mon-line "+linestyle+"'></div><i class='mon-line-ball "+ballstyle+"'></i></div></div></div></div>");
				}
			
			$div.prepend(tempArr.join(''));
			tempArr=null;
			$div=null;
			return lineColor;
			}
		function warninfo(data){
			var $div = $(".warning_info >.bd >.content");
			$div.html('');
			var tempArr = [];
			var evttime,id,monname,msg;
			for(var i=0;i<data.length;i++){
				evttime = data[i].evttime;
				id = data[i].id; 
				monname = data[i].monname;
				msg = data[i].msg;
				tempArr.push("<dl><dt>"+evttime+"</dt>");
				tempArr.push("<dd onclick=\"toErrMon("+id+")\" style=\"cursor: pointer;\">"+monname+"："+msg+"</dd></dl>");
				}
			if(data.length==0){
				tempArr.push("<dl><dt>"+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_9")+"</dt></dl>");
				}
			$div.html(tempArr.join(''));
			tempArr=null;
			$div=null;
			}
		function hostinfo(data){
			var $div = $(".server_info .bd .content");
			$div.html('');
			var tempArr = [];
			var hostid,hostname,hostusestatus,cpustyle,dcpuusage,memustyle,memuwidth,dmemusage,diskstyle,diskwidth,ddiskfreespace;
			for(var i=0;i<data.length;i++){
				hostid = data[i].hostid;
				hostname = data[i].hostname; 
				hostusestatus = data[i].hostusestatus;
				cpustyle = data[i].cpustyle;
				dcpuusage = data[i].dcpuusage;
				memuwidth = data[i].memuwidth;
				memustyle = data[i].memustyle;
				dmemusage = data[i].dmemusage;
				diskstyle = data[i].diskstyle;
				diskwidth = data[i].diskwidth;
				ddiskfreespace = data[i].ddiskfreespace;
				tempArr.push("<dl onclick=\"toHostMon('"+hostid+"')\" style=\"cursor: pointer;\">");
				tempArr.push("<dt><code>"+hostname+"</code>"+hostusestatus+ "</dt>");
				tempArr.push("<dd><div class='process-con'>");
				tempArr.push("<div class='process "+cpustyle+"' style=\"width:"+ dcpuusage+"%;\">"+dcpuusage+"%<span></span></div>");
				tempArr.push("</div><div class=\"hardware\">CPU</div></dd>");
				tempArr.push("<dd><div class='process-con'>");
				tempArr.push("<div class='process "+memustyle+"' style=\"width:"+ memuwidth+"%;\">"+dmemusage+"G<span></span></div>");
				tempArr.push("</div><div class=\"hardware\">"+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_10")+"</div></dd>");
				tempArr.push("<dd><div class='process-con'>");
				tempArr.push("<div class='process "+diskstyle+"' style=\"width:"+ diskwidth+"%;\">"+ddiskfreespace+"G<span></span></div>");
				tempArr.push("</div><div class=\"hardware\">"+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_11")+"</div></dd></dl>");
				}
			if(data.length==0){
				tempArr.push(getJsLocaleMessage("ptjk","ptjk_jkst_jkst_12")+"<a href=\"javascript:toAddMon()\">"+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_13")+"</a>");
				}
			
			$div.html(tempArr.join(''));
			tempArr=null;
			$div=null;
			}
		
		
</script>
</body>
</html>
