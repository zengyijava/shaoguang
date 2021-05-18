<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@page import="com.montnets.emp.monitor.constant.MonDgateacParams"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,
			iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo  = (PageInfo) request.getAttribute("pageInfo");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	@SuppressWarnings("unchecked")
	List<MonDgateacParams> pageList = (List<MonDgateacParams>)request.getAttribute("pageList");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<style type="text/css">
		#moreDes span{
			position: absolute;
			left:30px;
		}
		#moreDes span font{
			font-size: 12px;
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
						<th><emp:message key="ptjk_common_tdzh" defVal="通道账号" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_common_zhmc" defVal="账号名称" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_common_gjjb" defVal="告警级别" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_tdzh_26" defVal="MT待发(条)" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_tdzh_21" defVal="MT已发(条)" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_tdzh_22" defVal="MT总量(条)" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_tdzh_23" defVal="MT速度(条/秒)" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_tdzh_24" defVal="MO滞留(条)" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_tdzh_25" defVal="RPT滞留(条)" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_tdzh_fflx" defVal="付费类型" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_tdzh_yet" defVal="余额(条)" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_common_zhzt" defVal="账号状态" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_common_gxsj" defVal="更新时间" fileName="ptjk"/></th>
						<th><emp:message key="ptjk_jkxq_tdzh_gdxq" defVal="更多详情" fileName="ptjk"/></th>
					</tr>
				</thead>
				<tbody>
					<%
					if (pageList != null && pageList.size() > 0)
					{
						for (MonDgateacParams gate : pageList) {
					%>
					<tr>
						<td>
							<xmp><%=gate.getGateaccount()%></xmp>
						</td>
						<td class="textalign">
							<xmp><%= null == gate.getGateName()?"-":gate.getGateName()%></xmp>
						</td>
						<%
							String evttypeStr = gate.getEvtType()!=null?gate.getEvtType().toString():"30";
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
						<td><%=gate.getMtremained()!=null?gate.getMtremained():"0" %></td>
						<td><%=gate.getMthavesnd()!=null?gate.getMthavesnd():"0" %></td>
						<td><%=gate.getMttotalsnd()!=null?gate.getMttotalsnd():"0" %></td>
						<td><%=gate.getMtrecvspd()!=null?gate.getMtrecvspd():"0" %></td>
						<td><%=gate.getMoremained()!=null?gate.getMoremained():"0" %></td>
						<td><%=gate.getRptremained()!=null?gate.getRptremained():"0" %></td>
						<td><%=(gate.getFeeflag()!=null&&gate.getFeeflag()==1)?MessageUtils.extractMessage("ptjk","ptjk_jkxq_tdzh_2",request):MessageUtils.extractMessage("ptjk","ptjk_jkxq_tdzh_3",request) %></td>
						<td><%=(gate.getFeeflag()!=null&&gate.getFeeflag()==1)?(gate.getUserfee()!=null?gate.getUserfee():"0"):"-" %></td>
						<td class="ztalign">
						<%
							if(gate.getOnlinestatus()==null){
								out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wz",request));
							}else if(gate.getOnlinestatus()==0){
								out.print(MessageUtils.extractMessage("ptjk","ptjk_common_zx",request));
							}else if(gate.getOnlinestatus()==1){
								if("2".equals(evttypeStr))
								{
									out.print(MessageUtils.extractMessage("ptjk","ptjk_common_lx",request));
								}
								else
								{
									out.print(MessageUtils.extractMessage("ptjk","ptjk_common_zx",request));
								}
							}else if(gate.getOnlinestatus()==2){
								out.print(MessageUtils.extractMessage("ptjk","ptjk_common_qf",request));
							}else{
								out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wz",request));
							}
						%>
						</td>
						<td>
							<%=gate.getModifytime()!=null?df.format(gate.getModifytime()):"-" %>
						</td>
						<td><a onclick="javascript:openmore(this);"><emp:message key="ptjk_common_ck" defVal="查看" fileName="ptjk"/></a></td>
					</tr>
					<%
						}
					}else{
					%>
					<tr><td colspan="15" align="center"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr><%} %>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="15">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
			</table>
	<%-- <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.JSP_IMP_VERSION%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.JSP_IMP_VERSION%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.JSP_IMP_VERSION%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.JSP_IMP_VERSION%>"></script> --%>
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
		       alert(getJsLocaleMessage("ptjk","ptjk_jkxq_tdzh_2"));	
		       return;			       
		    }
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$("#moreDes").dialog({
				modal:true,
				autoOpen: false,
				width:600,
				height:310,
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
			var name = brothers.eq(0).children("xmp").text();
			$("#moreDes").dialog('open');
			$.post("mon_gateAcctMon.htm",{
        	    method: "getDes",
        	    gateaccount: name,
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
                        	if(label=='feeflag'){
                        		if(value==null){
        							value=0;
                        		}
                        		$(this).append('<font>'+(value==1?getJsLocaleMessage("ptjk","ptjk_jkxq_tdzh_4"):getJsLocaleMessage("ptjk","ptjk_jkxq_tdzh_5"))+'</font>');
                           	}else if(label=='onlinestatus'){
                           		if(value==null){
        							value=-1;
                        		}
                               	var status = getJsLocaleMessage("ptjk","ptjk_jkxq_tdzh_6");
                               	if(value===0){
									status = getJsLocaleMessage("ptjk","ptjk_jkxq_tdzh_7");
                                   	}
                               	if(value===1){
									status = getJsLocaleMessage("ptjk","ptjk_jkxq_tdzh_8");
                                   	}
                               	if(value===2){
									status = getJsLocaleMessage("ptjk","ptjk_jkxq_tdzh_9");
                                   	}
                           		$(this).append('<font>'+status+'</font>');
                            }else{
                                if($(this).hasClass('count')){
                                	if(value==null){value=0;}
                                	if(label=='userfee'&&1!=message['feeflag']){
                                		$(this).append('<font>-</font>');
                                   	}else{
                                   		$(this).append('<font>'+value+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_2")+'</font>');
                                    }
                                	
                                }else if($(this).hasClass('speed')){
                                	if(value==null){value=0;}
                                	$(this).append('<font>'+value+getJsLocaleMessage("ptjk","ptjk_jkst_jkst_7")+'</font>');
                                }else if(label=='linknum'){
                                	if(value==null){value=0;}
                                	$(this).append('<font>'+value+'('+getJsLocaleMessage("ptjk","ptjk_jkxq_tdzh_10")+')</font>');
                               	}else{
                                	if(value==null){value='-';}
                                	$(this).append('<font>'+value+'</font>');
                                    }
                				
                           	}
                		}
                	});
            	brothers.eq(0).html("<xmp>"+$("#moreDes [label='gateaccount'] font").html()+"</xmp>");
            	brothers.eq(1).html("<xmp>"+$("#moreDes [label='gateName'] font").html()+"</xmp>");
            	brothers.eq(3).text(''+$("#moreDes [label='mtremained'] font").html().replace(/\(.*\)/gm,''));
            	brothers.eq(4).text(''+$("#moreDes [label='mthavesnd'] font").html().replace(/\(.*\)/gm,''));
            	brothers.eq(5).text(''+$("#moreDes [label='mttotalsnd'] font").html().replace(/\(.*\)/gm,''));
            	brothers.eq(6).text(''+$("#moreDes [label='mtrecvspd'] font").html().replace(/\(.*\)/gm,''));
            	brothers.eq(7).text(''+$("#moreDes [label='moremained'] font").html().replace(/\(.*\)/gm,''));
            	brothers.eq(8).text(''+$("#moreDes [label='rptremained'] font").html().replace(/\(.*\)/gm,''));
            	brothers.eq(9).text($("#moreDes [label='feeflag'] font").html());
            	brothers.eq(10).text(''+$("#moreDes [label='userfee'] font").html().replace(/\(.*\)/gm,''));
            	brothers.eq(11).text($("#moreDes [label='onlinestatus'] font").html());
            	brothers.eq(12).text($("#moreDes [label='modifytime'] font").html());
        });
			}
		</script>
	</body>
</html>
