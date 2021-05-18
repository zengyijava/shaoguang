<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.monitor.constant.MonDspAccountParams"%>
<%@page import="com.montnets.emp.monitor.constant.MonitorStaticValue"%>
<%@page import="java.util.Calendar"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	@SuppressWarnings("unchecked")
	List<MonDspAccountParams> pageList = (List<MonDspAccountParams>)request.getAttribute("pageList");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<style type="text/css">
		#moreDes span{
			position: absolute;
			left:30px;
		}
		#moreDes span font{
			font-size: 12px;
		}
        #container {
            float: left;
            min-width: 1220px;
            _width: 1220px;
        }
        .maxwidth-td{
            width:120px;
        }
        #content .maxwidth-td .maxwidth{
            margin: 0 auto;
            width: 118px;
            display: block;
            white-space: nowrap;
            overflow: hidden;
            -o-text-overflow: ellipsis;
            text-overflow: ellipsis;
        }
		</style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		
		<table id="content">
		<thead>
			<tr>
				<th>
					<emp:message key="ptjk_common_spzh" defVal="SP账号" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_common_zhmc" defVal="账号名称" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_spzh_zhlx" defVal="账号类型" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_common_zhzt" defVal="账号状态" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_spzh_fsjb" defVal="发送级别" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_common_gjjb" defVal="告警级别" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_spzh_dllx" defVal="登录类型" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_spzh_tjzt" defVal="提交状态" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_tdzh_26" defVal="MT待发(条)" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_tdzh_21" defVal="MT已发(条)" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_tdzh_22" defVal="MT总量(条)" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_spzh_6" defVal="MT下发速度(条/秒)" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_spzh_7" defVal="MT提交速度(条/秒)" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_tdzh_24" defVal="MO滞留(条)" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_tdzh_25" defVal="RPT滞留(条)" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_common_gxsj" defVal="更新时间" fileName="ptjk"/>
				</th>
				<th>
					<emp:message key="ptjk_jkxq_tdzh_gdxq" defVal="更多详情" fileName="ptjk"/>
				</th>
			</tr>
		</thead>
		<tbody>
		<%
			if(pageList!=null && pageList.size()>0)
			{
				for(MonDspAccountParams mon : pageList)
				{
		%>
		<tr>
			<td><%=mon.getSpaccountid()!=null?mon.getSpaccountid():"-"%></td>
			<td class="maxwidth-td">
				<xmp class="maxwidth">
				<%=mon.getAccountName()!=null?mon.getAccountName():"-"%>
				</xmp>
			</td>
			<td>
			<%
				Integer type = mon.getSpAccountType();
				if(type==null){
					out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_zlzh",request));
				}else if(type==1){
					out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_1",request));
				}else if(type==2){
					out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_2",request));
				}else{
					out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_zlzh",request));
				}
			%>
			</td>
			<td>
				<%
				Integer onlinestatus=mon.getOnlinestatus();
				if(onlinestatus!=null&&onlinestatus==0)
				{
					out.print(MessageUtils.extractMessage("ptjk","ptjk_common_zx",request));
				}else if(onlinestatus!=null&&onlinestatus==1)
				{
				    out.print(MessageUtils.extractMessage("ptjk","ptjk_common_lx",request));
				}else
				{
					out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wz",request));
				}
				 %>
			</td>
			<td>
				<%=mon.getSendlevel()!=null?mon.getSendlevel()+5:"-" %>
			</td>
			<%
				String evttypeStr = mon.getEvtType()!=null?mon.getEvtType().toString():"30";
				if("1".equals(evttypeStr))
				{
					out.print("<td class='warn'>"+MessageUtils.extractMessage("ptjk","ptjk_common_jg",request)+"</td>");
				}
				else if("2".equals(evttypeStr))
				{
					out.print("<td class='bad'>"+MessageUtils.extractMessage("ptjk","ptjk_common_yz",request)+"</td>");
				}
				else
				{
					out.print("<td class='natural'>"+MessageUtils.extractMessage("ptjk","ptjk_common_zc",request)+"</td>");
				}
			%>
			<td>
				<%
				Integer loginType=mon.getLoginType();
				if(loginType!=null&&loginType==1)
				{
					out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_wbsdl",request));
				}else if(loginType!=null&&loginType==2)
				{
				    out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_zldl",request));
				}else
				{
					out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wz",request));
				}
				 %>
			</td>
			<td>
				<%
				//spid为空，则未知
				if(mon.getSpaccountid()==null||"".equals(mon.getSpaccountid().trim())){
					out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wz",request));
				}else if(MonitorStaticValue.getSpAccountMap().get(mon.getSpaccountid())==null){
					out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wz",request));
				}else{
					 //获取提交状态
					 long submitStatus=MonitorStaticValue.getSpAccountMap().get(mon.getSpaccountid()).getMonThresholdFlag().get(13);
					 //提交状态为0，正常
					 if(submitStatus==0)
					 {
					 	out.print(MessageUtils.extractMessage("ptjk","ptjk_common_zc",request));
					 }
					 //提交状态为1，不正常
					 else if(submitStatus==1)
					 {
					 	//计算未提交数据时间
					 	long min=(Calendar.getInstance().getTime().getTime()- mon.getNoMtHaveSnd())/(1000*60);
					 	out.print(min+MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_8",request));
					 }else{
					 	out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wz",request));
					 }
				}
				 %>
			</td>
			<td>
				<%=mon.getMtremained()!=null?mon.getMtremained():"-" %>
			</td>
			<td>
				<%=mon.getMthavesnd()!=null?mon.getMthavesnd():"-"%>
			</td>
			<td>
				<%=mon.getMtTotalSnd()!=null?mon.getMtTotalSnd():"-"%>
			</td>
			<td>
				<%=mon.getMtissuedspd()!=null?mon.getMtissuedspd():"-"%>
			</td>
			<td>
				<%=mon.getMtsndspd()!=null?mon.getMtsndspd():"-"%>
			</td>
			<td>
				<%=mon.getMoremained()!=null?mon.getMoremained():"-"%>
			</td>
			<td>
				<%=mon.getRptremained()!=null?mon.getRptremained():"-"%>
			</td>
			<td>
				<%=mon.getUpdatetime()!=null?df.format(mon.getUpdatetime()):"-" %>
			</td>
			<td><a onclick="javascript:openmore(this);"><emp:message key="ptjk_common_ck" defVal="查看" fileName="ptjk"/></a></td>
		</tr>
		<%
				}
			}else{
		%>
		<tr><td align="center" colspan="17"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr>
		<%} %>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="17">
					<div id="pageInfo"></div>
				</td>
			</tr>
		</tfoot>
	</table>
	<div class="clear"></div>
	<%-- <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.JSP_IMP_VERSION%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.JSP_IMP_VERSION%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.JSP_IMP_VERSION%>"></script> --%>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script src="<%=iPath%>/js/Date2Str.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
	    $("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
		}, function() {
			$(this).removeClass("hoverColor");
		});
		var findresult="<%=(String)request.getAttribute("findresult")%>";
	    if(findresult=="-1")
	    {
	       alert(getJsLocaleMessage("ptjk","ptjk_jkxq_spzh_1"));	
	       return;			       
	    }
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		$("#moreDes").dialog({
			modal:true,
			autoOpen: false,
			width:600,
			height:300,
			resizable :false,
			open: function(){
				$("#ui-dialog-title-moreDes").html(getJsLocaleMessage("ptjk","ptjk_jkxq_tdzh_3"));
				window.clearInterval(reTimer);
			},
			close:function()
			{
				$("#moreDes").find('font').remove();
				reTimer=window.setInterval("submitForm()",refreshTime);
			}
		});
		//销毁之前的dialog
		$("#moreDes").parent().siblings(".ui-dialog").remove();
		//解除绑定事件，使表格选中不变色
		$('#content table tbody tr,#content tbody tr').unbind("click");
		
	});
	function openmore(obj){
		var brothers = $(obj).parent().siblings();
		var name = brothers.eq(0).html();
		$("#moreDes").dialog('open');
		$.post("mon_spAcctMon.htm",{
    	    method: "getDes",
    	    spaccount: name,
    		isAsync:"yes"
    	},function(message){
        	if(message=="outOfLogin"){
				location.reload();
            	}
        	message = eval('('+message+')');
        	$("#moreDes").children("span").each(function(){
            		var label=$(this).attr('label');
            		var value =message[label];
            		if($(this).hasClass('time')){
            			var date;
                		if(value==null||value==''){
								date = '-';
                   		}else{
								date = formatDate(new Date(value));
                       		}
            			$(this).append('<font>'+date+'</font>');
                	}else{
                    	if(label=='spAccountType'){
                    		if(value==null){value=0;}
                    		var type = value;
                    		var lab = getJsLocaleMessage("ptjk","ptjk_jkxq_spzh_2");
                    		if(type-1==0){
                    			lab = getJsLocaleMessage("ptjk","ptjk_jkxq_spzh_3");
            				}else if(type-2==0){
            					lab = getJsLocaleMessage("ptjk","ptjk_jkxq_spzh_4");
            				}
                    		$(this).append('<font>'+lab+'</font>');
                       	}else{
                       	   if($(this).hasClass('count')){
                       		if(value==null){value=0;}
                           	$(this).append('<font>'+value+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_2")+'</font>');
                           }else if($(this).hasClass('speed')){
                       	   	if(value==null){value=0;}
                           	$(this).append('<font>'+value+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_7")+'</font>');
                           }else{
                        	 if(value==null){value='-';}
                           	$(this).append('<font>'+value+'</font>');
                               }
                       	}
            		}
            	});
            //点击查看详情，不更新页面
        	brothers.eq(0).html($("#moreDes [label='spaccountid'] font").html());
        	brothers.eq(1).html("<xmp class='maxwidth'>"+$("#moreDes [label='accountName'] font").html()+"</xmp>");
        	brothers.eq(2).text($("#moreDes [label='spAccountType'] font").html());
        	
        	brothers.eq(3).text($("#moreDes [label='onlinestatusStr'] font").html());
        	brothers.eq(7).text($("#moreDes [label='submitStatusStr'] font").html());
        	
        	brothers.eq(8).text(''+$("#moreDes [label='mtremained'] font").html().replace(/\(.*\)/gm,''));
        	brothers.eq(9).text(''+$("#moreDes [label='mthavesnd'] font").html().replace(/\(.*\)/gm,''));
        	brothers.eq(10).text(''+$("#moreDes [label='mtTotalSnd'] font").html().replace(/\(.*\)/gm,''));
        	brothers.eq(11).text(''+$("#moreDes [label='mtissuedspd'] font").html().replace(/\(.*\)/gm,''));
        	brothers.eq(12).text(''+$("#moreDes [label='mtsndspd'] font").html().replace(/\(.*\)/gm,''));
        	brothers.eq(13).text(''+$("#moreDes [label='moremained'] font").html().replace(/\(.*\)/gm,''));
        	brothers.eq(14).text(''+$("#moreDes [label='rptremained'] font").html().replace(/\(.*\)/gm,''));
        	brothers.eq(15).text($("#moreDes [label='updatetime'] font").html());
    });
		}
	</script>
	</body>
</html>
