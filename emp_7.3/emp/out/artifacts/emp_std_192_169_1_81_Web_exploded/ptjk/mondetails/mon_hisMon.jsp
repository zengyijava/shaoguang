<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
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
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("hisMon");
menuCode = menuCode==null?"0-0-0":menuCode;
//用户名称map
@ SuppressWarnings("unchecked")
Map<String,String> userMap=(Map<String,String>)request.getAttribute("userMap");//按钮权限Map
PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<DynaBean> moniList = (List<DynaBean>) request
		.getAttribute("monitorList");

String msg = request.getParameter("msg");
String evttype = request.getParameter("evttype");
String recvtime = request.getParameter("recvtime");
String sendtime = request.getParameter("sendtime");
String apptype = request.getParameter("apptype");
String dealflag = request.getParameter("dealflag");
String dealpeople = request.getParameter("dealpeople");
String monname = request.getParameter("monname");
monname=monname!=null?( java.net.URLDecoder.decode(monname,"UTF-8")):monname;

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'mon_hostMoConfig.jsp' starting page</title>
    <%@include file="/common/common.jsp" %>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<style type="text/css">
	.deal_tab tr td
	{
		height: 25px;
	}
	.c_selectBox{
			width: 208px!important;
	}
	.c_selectBox ul {
		width: 208px!important;
	}
	.c_selectBox ul li{
		width: 208px!important;
	}
	</style>
  </head>
  
  <body>
    <div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="mon_hisMon.htm" method="post" id="pageForm">
		<input type="hidden" id="pathUrl" name="pathUrl" value="<%=path %>">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv">
				</div>
				<%--<a id="exportCondition">导出</a>--%>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td>
								<emp:message key="ptjk_jkxq_ssgj_jklx_mh" defVal="监控类型：" fileName="ptjk"/>
							</td>
							<td >	
								<select name="apptype" id="apptype" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="3000" <%="3000".equals(apptype)?"selected":"" %>><emp:message key="ptjk_common_zj" defVal="主机" fileName="ptjk"/></option>
									<option value="3100" <%="3100".equals(apptype)?"selected":"" %>><emp:message key="ptjk_common_zjwl" defVal="主机网络" fileName="ptjk"/></option>
									<option value="5000" <%="5000".equals(apptype)?"selected":"" %>><emp:message key="ptjk_common_webcx" defVal="WEB程序" fileName="ptjk"/></option>
									<option value="5200" <%="5200".equals(apptype)?"selected":"" %>><emp:message key="ptjk_common_empwg" defVal="EMP网关" fileName="ptjk"/>(EMP_GW)</option>
									<option value="5300" <%="5300".equals(apptype)?"selected":"" %>><emp:message key="ptjk_common_yysjk" defVal="运营商接口" fileName="ptjk"/>(SPGATE)</option>
									<option value="5400" <%="5400".equals(apptype)?"selected":"" %>><emp:message key="ptjk_common_spzh_mh" defVal="SP账号" fileName="ptjk"/></option>
									<option value="5500" <%="5500".equals(apptype)?"selected":"" %>><emp:message key="ptjk_common_tdzh" defVal="通道账号" fileName="ptjk"/></option>
									<option value="5800" <%="5800".equals(apptype)?"selected":"" %>><emp:message key="ptjk_common_wjfwq" defVal="文件服务器" fileName="ptjk"/></option>
									<option value="5100" <%="5100".equals(apptype)?"selected":"" %>><emp:message key="ptjk_common_sjk" defVal="数据库" fileName="ptjk"/></option>
								</select>
							</td>
							<td>
								<emp:message key="ptjk_common_gjjb_mh" defVal="告警级别：" fileName="ptjk"/>
							</td>
							<td >
								<select id="evttype" name="evttype" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
								<%--<option value="0" <%="0".equals(evttype)?"selected":"" %>>正常</option>--%>
									<option value="1" <%="1".equals(evttype)?"selected":"" %>><emp:message key="ptjk_common_jg" defVal="警告" fileName="ptjk"/></option>
									<option value="2" <%="2".equals(evttype)?"selected":"" %>><emp:message key="ptjk_common_yz" defVal="严重" fileName="ptjk"/></option>
								</select>
							</td>
							<td><emp:message key="ptjk_common_mc_mh" defVal="名称：" fileName="ptjk"/></td>
							<td><input type="text" name="monname" id="monname" value="<%=monname==null?"":monname %>"></td>
							<td class="tdSer">
							       <center><a id="search"></a></center>
						    </td>	
						</tr>
						<tr>
							<%--<td>处理者：</td>
							<td>
								<input type="text" name="dealpeople" id="dealpeople" value="<%=dealpeople!=null?dealpeople:"" %>"/>
							</td>
							--%>
							<td>
								<emp:message key="ptjk_jkxq_ssgj_3" defVal="首次告警时间：" fileName="ptjk"/>
							</td>
							<td >
								<input type="text"
											style="cursor: pointer; width: 150px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()" 
											value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime">
							</td>
							<td><emp:message key="ptjk_common_z_mh" defVal="至：" fileName="ptjk"/></td>
							<td>
								<input type="text"
											style="cursor: pointer; width: 150px; background-color: white;"
											readonly="readonly" class="Wdate"  onclick="rtime()"
											value="<%=recvtime==null?"":recvtime %>" 
											 id="recvtime" name="recvtime" >
							</td>
							<td colspan="3"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<table id="content">
				<thead>
					<tr>
						<th>
							<emp:message key="ptjk_jkxq_ssgj_jklx" defVal="监控类型" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_mc" defVal="名称" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_gjjb" defVal="告警级别" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_ssgj_sjms" defVal="事件描述" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_ssgj_3" defVal="首次告警时间" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_ssgj_4" defVal="末次告警时间" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_gjcs" defVal="告警次数" fileName="ptjk"/>
						</th>
<%--						<th>
							抛出者
						</th>--%>
						<th>
							<emp:message key="ptjk_common_clzt" defVal="处理状态" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_clz" defVal="处理者" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_clms" defVal="处理描述" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_clsj" defVal="处理时间" fileName="ptjk"/>
						</th>
					</tr>
				</thead>
				<tbody>
				<%
					if(moniList!=null && moniList.size()>0)
					{
						for(DynaBean mon : moniList)
						{
				%>
				<tr>
					<td >
						<%
						String appType = mon.get("apptype").toString();
						String monName="";
						if("3000".equals(appType) )
						{
							monName = mon.get("hostname")!=null?mon.get("hostname").toString():"";
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_zj",request));
						}
						else if("3100".equals(appType))
						{
							monName = mon.get("hostnetname")!=null?mon.get("hostnetname").toString():"";
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_zjwl",request));
						}
						else if("5000".equals(appType))
						{
							monName = mon.get("procename")!=null?mon.get("procename").toString():"";
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_webcx",request));
						}
						else if("5200".equals(appType))
						{
							monName = mon.get("procename")!=null?mon.get("procename").toString():"";
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_empwg",request)+"(EMP_GW)");
						}
						else if("5300".equals(appType))
						{
							monName = mon.get("procename")!=null?mon.get("procename").toString():"";
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_yysjk",request)+"(SPGATE)");
						}
						else if("5400".equals(appType))
						{
							monName = mon.get("accountname")!=null?mon.get("accountname").toString():"";
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_spzh",request));
						}
						else if("5500".equals(appType))
						{
							monName = mon.get("gatename")!=null?mon.get("gatename").toString():"";
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_tdzh",request));
						}
						else if("5600".equals(appType))
						{
							monName = "";
							out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_ssgj_5",request));
						}
						else if("5700".equals(appType))
						{
							monName = mon.get("procename")!=null?mon.get("procename").toString():"";
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_webcx",request));
						}
						else if("5800".equals(appType))
						{
							monName = mon.get("procename")!=null?mon.get("procename").toString():"";
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wjfwq",request));
						}
						else if("5100".equals(appType))
						{
							monName = mon.get("dbprocename")!=null?mon.get("dbprocename").toString():"";
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_sjk",request));
						}
						else
						{
							out.print("-");
						}
						%>
					</td>
					<td>
					<xmp>
						<%=(monName!=null&&!"".equals(monName)?monName:"-")%>
					</xmp>
					</td>
					<%
					String evttypeStr = mon.get("evttype")!=null?mon.get("evttype").toString():"30";
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
						<%String msgStr = mon.get("msg")!=null?mon.get("msg").toString():"-"; %>
						<a onclick='javascript:modify(this,1)' title="<%=(msgStr.length()==0?"-":msgStr) %>">
								  <label style="display:none"><xmp><%=(msgStr.length()==0?"-":msgStr)%></xmp></label>
								<xmp><%=msgStr.length()>8?msgStr.substring(0,8)+"...":(msgStr.length()==0?"-":msgStr) %></xmp>
						</a> 
					</td>
					<td>
						<%=mon.get("rcvtime")!=null?df.format(mon.get("rcvtime")):"" %>
					</td>
					<td>
						<%=mon.get("evttime")!=null?df.format(mon.get("evttime")):"" %>
					</td>
					<td>
						<%=mon.get("montimer")!=null?mon.get("montimer"):"" %>
					</td>
<%--					<td>
						<%=mon.get("who")!=null?mon.get("who"):"没有事件抛出者"%>
					</td>--%>
					<td>
						<%
						String dealflagStr = mon.get("dealflag").toString();
						if("0".equals(dealflagStr))
						{
							out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_ssgj_zdcl",request));
						}
						else if("1".equals(dealflagStr))
						{
							out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_ssgj_xsj",request));
						}
						else if("2".equals(dealflagStr))
						{
							out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_ssgj_clz",request));
						}
						else if("3".equals(dealflagStr))
						{
							out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_ssgj_ycl",request));
						}
						else
						{
							out.print("-");
						}
						%>
					</td>
					<td>
						<xmp><%=mon.get("dealpeople")!=null?userMap.get(mon.get("dealpeople").toString()):"-" %></xmp>
					</td>
					<td>
						<%String dealdescStr = mon.get("dealdesc")!=null?mon.get("dealdesc").toString():"";%>
						<% if(dealdescStr.length()>0){%>
						<a onclick='javascript:modify(this,2)' >
								  <label style="display:none"><xmp><%=dealdescStr%></xmp></label>
								  <xmp><%=dealdescStr.length()>8?dealdescStr.substring(0,8)+"...":dealdescStr %></xmp>
						</a> 
						<%}else{out.print("-");} %>
					</td>
					<td>
						<%=mon.get("crttime")!=null?df.format(mon.get("crttime")):"-" %>
					</td>
				</tr>
				<%
						}
					}else{
				%>
				<tr><td align="center" colspan="15"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr>
				<%} %>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="15">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
			</table>
		</form>
		</div>
			
		<%-- 内容结束 --%>
		
		<div id="modify" title="<emp:message key='ptjk_common_xxnr' defVal='信息内容' fileName='ptjk'/>"  style="padding:5px;width:300px;height:160px;display:none">
			<table width="100%">
				<thead>
					<tr style="padding-top:2px;margin-bottom: 2px;">
						<td style='word-break: break-all;'>
							<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>
						</td>
					</tr>
				   <tr style="padding-top:2px;">
						<td>
						</td>
						</tr>
				</thead>
			</table>
		</div>
		
		<div id="dealMon" title="<emp:message key='ptjk_jkxq_ssgj_gjcl' defVal='告警处理' fileName='ptjk'/>"  style="padding:10px;padding-top:20px;display:none">
			<input type="hidden" id="id" name="id"/>
			<table class="deal_tab">
				<tr>
					<td></td>
					<td>
						<label>
						<input type="radio" name="dealflag" id="dealflag" value="2"> 
						<emp:message key="ptjk_jkxq_ssgj_clz" defVal="处理中" fileName="ptjk"/>
						</label>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<label>
						<input type="radio" name="dealflag" id="dealflag" value="3"> 
						<emp:message key="ptjk_jkxq_ssgj_ycl" defVal="已处理" fileName="ptjk"/>
						</label>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<label>
						<input type="radio" name="dealflag" id="dealflag" value="1"> 
						<emp:message key="ptjk_jkxq_ssgj_xsj" defVal="新事件" fileName="ptjk"/>
						</label>
					</td>
				</tr>
				<tr>
					<td style="vertical-align: top; width: 50px;">
					<emp:message key="ptjk_jkxq_ssgj_ms_mh" defVal="描述：" fileName="ptjk"/>
					</td>
					<td>
						<textarea rows="" cols="" style="width: 300px;height: 100px;" name="dealdesc" id="dealdesc">
						</textarea>
					</td>
				</tr>
			</table>
			<table width="100%" style="text-align: center;margin-top: 20px;">
				<tr>
					<td>
						<input type="button" name="sub" id="sub" value="<emp:message key='ptjk_common_qd' defVal='确定' fileName='ptjk'/>" onclick="dealMon()" class="btnClass5 mr23"/>
						<input type="button" name="button" id="button" value="<emp:message key='ptjk_common_qx' defVal='取消' fileName='ptjk'/>" onclick="javascript:doreturn()" 
						class="btnClass6"/>
						<br />
					</td>
				</tr>
			</table>
		</div>
		
			
		<%-- foot开始 --%>
		<div class="bottom">
			<div id="bottom_right">
				<div id="bottom_left"></div>
				<div id="bottom_main">
				</div>
			</div>
		</div>
	</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script src="<%=iPath%>/js/errMon.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#loginInfo");
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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){
				submitForm();
			});

			$("#dealMon").dialog({
				modal:true,
				autoOpen: false,
				height:320,
				width:400,
				resizable :false,
				open: function(){
				},
				close:function()
				{
					$("#dealMon").find("input type='text'").val("");
					$("#id").val("");
					$("#dealdesc").val("");
				}
			});

			$('#modify').dialog({
				autoOpen: false,
				width:250,
			    height:200
			});

			$("#apptype,#evttype,#dealflag").isSearchSelect({'width':'152','isInput':false,'zindex':0});
		});
		//返回实时监控详情页面
		function toErr()
		{
			var lgcorpcode = $("#lgcorpcode").val();
			var pathUrl = $("#pathUrl").val();
			window.parent.openNewTab("2900-1000",pathUrl+"/mon_realTimeAlarm.htm?method=find&lgcorpcode="+lgcorpcode);
		}
		function modify(t,titleTyp)
		{
			if(titleTyp==1)
			{
				$("#modify").dialog('option','title',getJsLocaleMessage("ptjk","ptjk_jkxq_ssgj_4"));
			}
			else if(titleTyp==2)
			{
				$("#modify").dialog("option","title",getJsLocaleMessage("ptjk","ptjk_jkxq_ssgj_5"));
			}	
			$("#msgcont").empty();
			$("#msgcont").text($(t).children("label").children("xmp").text());
			$('#modify').dialog('open');
		}
	</script>
  </body>
</html>
