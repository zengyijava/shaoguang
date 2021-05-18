<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("homeedit");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
		
List<DynaBean> list = null;
if(request.getAttribute("list") != null){
	list = (List<DynaBean>) request.getAttribute("list");
}

String startTime = StringUtils.defaultString(request.getParameter("starttime"));
String endTime = StringUtils.defaultString(request.getParameter("endtime"));
String sid = StringUtils.defaultString(request.getParameter("sid"));
String name = StringUtils.defaultString(request.getParameter("name"));
String creater = StringUtils.defaultString(request.getParameter("creater"));
String status = StringUtils.defaultString(request.getParameter("status"));

//按钮权限
boolean add = btnMap.get(menuCode+"-1")!=null;
add = true;
Map<String,String> statusMap = new HashMap<String,String>();
statusMap.put("1",MessageUtils.extractMessage("appmage","appmage_xxfb_appsyfb_text_released",request));
statusMap.put("2",MessageUtils.extractMessage("appmage","appmage_xxfb_appsyfb_text_fbcg",request));
statusMap.put("3",MessageUtils.extractMessage("appmage","appmage_xxfb_appsyfb_text_xjcg",request));
statusMap.put("-1",MessageUtils.extractMessage("appmage","appmage_xxfb_appsyfb_text_unknown",request));
statusMap.put("-2",MessageUtils.extractMessage("appmage","appmage_xxfb_appsyfb_text_fbsb",request));
statusMap.put("-3",MessageUtils.extractMessage("appmage","appmage_xxfb_appsyfb_text_xjsb",request));
statusMap.put("-9",MessageUtils.extractMessage("appmage","appmage_xxfb_appsyfb_text_ygq",request));
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>

    <title></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/mobilePreview.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
  	<style type="text/css">
  	.slider-text,.artContent,.artTitle,.pictit{
      white-space: nowrap;
      /*width:120px;*/
      overflow: hidden;             
      -o-text-overflow: ellipsis;    /* Opera */
      text-overflow:    ellipsis;    /* IE, Safari (WebKit) */
	}
  	</style>
  	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
  </head>
  
  <body>
    <div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="app_homeedit.htm" method="post" id="pageForm">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv"></div>
				<%if(add) {%><a id="add" href="javascript:opp(0);"><emp:message key="appmage_common_opt_xinjian" defVal="新建" fileName="appmage"></emp:message></a><%} %>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td>
								<emp:message key="appmage_xxfb_appsyfb_text_numbering" defVal="编号" fileName="appmage"></emp:message>：
							</td>
							<td >
								<input style="width: 178px" type="text" value="<%=sid %>" name="sid" id="sid" class="int"/>
							</td>
							<td>
								<emp:message key="appmage_xxfb_appsyfb_text_indexpagename" defVal="首页名称" fileName="appmage"></emp:message>：
							</td>
							<td >	
								<input style="width: 178px" type="text" id="name" name="name" value="<%=name %>"/>
							</td>
							<td><emp:message key="appmage_xxfb_appsyfb_text_createpeploe" defVal="创建人" fileName="appmage"></emp:message>：</td>
							<td>
								<input style="width: 178px" type="text" value="<%=creater %>" name="creater" id="creater"/>
							</td>
							<td class="tdSer">
							       <center><a id="search"></a></center>
						    </td>		
						</tr>
						<tr>
							<td><emp:message key="appmage_xxfb_appsyfb_text_status" defVal="状态" fileName="appmage"></emp:message>：</td>
							<td>
								<select id="status" name="status" class="newSelect" isInput="false">
									<option value=""><emp:message key="appmage_common_text_all" defVal="全部" fileName="appmage"></emp:message></option>
									<%--<option value="0">草稿</option>  --%>
									<option value="1" <%=status!=null && status.equals("1")?"selected":""%>>
									<emp:message key="appmage_xxfb_appsyfb_text_released" defVal="待发布" fileName="appmage"></emp:message></option>
									<option value="3" <%=status!=null && status.equals("3")?"selected":""%>>
									<emp:message key="appmage_xxfb_appsyfb_text_xjcg" defVal="下架成功" fileName="appmage"></emp:message></option>
									<option value="-3" <%=status!=null && status.equals("-3")?"selected":""%>>
									<emp:message key="appmage_xxfb_appsyfb_text_xjsb" defVal="下架失败" fileName="appmage"></emp:message></option>
									<option value="2" <%=status!=null && status.equals("2")?"selected":""%>>
									<emp:message key="appmage_xxfb_appsyfb_text_fbcg" defVal="发布成功" fileName="appmage"></emp:message></option>
									<option value="-2" <%=status!=null && status.equals("-2")?"selected":""%>>
									<emp:message key="appmage_xxfb_appsyfb_text_fbsb" defVal="发布失败" fileName="appmage"></emp:message></option>
									<option value="-9" <%=status!=null && status.equals("-9")?"selected":""%>>
									<emp:message key="appmage_xxfb_appsyfb_text_ygq" defVal="已过期" fileName="appmage"></emp:message></option>
									<option value="-1" <%=status!=null && status.equals("-1")?"selected":""%>>
									<emp:message key="appmage_xxfb_appsyfb_text_unknown" defVal="未知" fileName="appmage"></emp:message></option>
								</select>
							</td>
							<td><emp:message key="appmage_xxfb_appsyfb_text_createtime" defVal="创建时间" fileName="appmage"></emp:message>：</td>
							<td>
								<input type="text"
									style="cursor: pointer; width: 178px; background-color: white;"
									class="Wdate startdate" readonly="readonly"
									value="<%=startTime == null ? "" : startTime%>"  id="starttime"
									name="starttime" />
							</td>
							<td><emp:message key="appmage_common_text_zhi" defVal="至：" fileName="appmage"></emp:message></td>
							<td>
								<input type="text"
										style="cursor: pointer; width: 178px; background-color: white;"
										class="Wdate enddate" readonly="readonly"
										value="<%=endTime == null ? "" : endTime%>"  id="endtime"
										name="endtime" />
							</td>
							<td colspan="1"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<table id="content">
				<thead>
					<tr>
						<th>
							<emp:message key="appmage_xxfb_appsyfb_text_numbering" defVal="编号" fileName="appmage"></emp:message>
						</th>
						<th width="230px">
							<emp:message key="appmage_xxfb_appsyfb_text_indexpagename" defVal="首页名称" fileName="appmage"></emp:message>
						</th>
						<th>
							<emp:message key="appmage_xxfb_appsyfb_text_indexpagecontent" defVal="首页内容" fileName="appmage"></emp:message>
						</th>
						<th>
							<emp:message key="appmage_xxfb_appsyfb_text_status" defVal="状态" fileName="appmage"></emp:message>
						</th>
						<th>
							<emp:message key="appmage_xxfb_appsyfb_text_publictime" defVal="发布时间" fileName="appmage"></emp:message>
						</th>
						<th>
							<emp:message key="appmage_xxfb_appsyfb_text_downtime" defVal="下架时间" fileName="appmage"></emp:message>
						</th>
						<th>
							<emp:message key="appmage_xxfb_appsyfb_text_validperiod" defVal="有效期" fileName="appmage"></emp:message>
						</th>
						<th>
						    <emp:message key="appmage_xxfb_appsyfb_text_createtime" defVal="创建时间" fileName="appmage"></emp:message>
						</th>
						<th>
							<emp:message key="appmage_xxfb_appsyfb_text_createpeploe" defVal="创建人" fileName="appmage"></emp:message>
						</th>
						<th colspan="4">
							<emp:message key="appmage_common_opt_caozuo" defVal="操作" fileName="appmage"></emp:message>
						</th>
					</tr>
				</thead>
				<tbody>
				<%
					if(list!=null && list.size()>0)
					{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						long currTime = System.currentTimeMillis();
						for(DynaBean item : list)
						{
							String s_id = item.get("s_id").toString();
							String s_name = item.get("name")==null?"-":item.get("name").toString();
							String old_name = s_name.replaceAll("\"","#quot;").replaceAll("&","#amp;");
							s_name = StringEscapeUtils.escapeHtml(StringUtils.abbreviate(s_name,18));
							String s_createtime =  item.get("createtime")==null?"-":String.valueOf(item.get("createtime")).substring(0,19);
							String s_creater = item.get("creater")==null?"-":item.get("creater").toString();
							String s_publishtime = item.get("publishtime")==null?"-":String.valueOf(item.get("publishtime")).substring(0,19);
							String s_canceltime = item.get("canceltime")==null?"-":String.valueOf(item.get("canceltime")).substring(0,19);
							String s_validate = item.get("validity")==null?null:String.valueOf(item.get("validity"));
							String s_sendstate = item.get("sendstate")==null?"":String.valueOf(item.get("sendstate"));
							String s_status = null;
							//修正状态的值 当查询条件指定状态时
							if(!"".equals(status)){
								s_status = status;
							}else{
								s_status = item.get("status")==null?"1":item.get("status").toString();
								if("2".equals(s_status)){
									if("0".equals(s_sendstate)){
										s_status = "2";//发布成功
									}else if("1".equals(s_sendstate)){
										s_status = "-2";//发布失败
									}else{
										s_status = "-1";//未知
									}
								}
								if("-3".equals(s_status)){
									if("1".equals(s_sendstate)){
										s_status = "-3";//下架失败
									}else{
										s_status = "3";//下架成功
									}
								}
							}
							String s_username = item.get("user_name")==null?"-":item.get("user_name").toString();
							if(s_validate!=null){
								//在不指状态查询时 处理状态为2的已发布状态中的过期数据 状态设置为已过期
								if("".equals(status)&&"2".equals(s_status)&&currTime>Long.valueOf(s_validate)){
									s_status = "-9";
								}
								s_validate = sdf.format(new Date(Long.valueOf(s_validate)));
							}else{
								s_validate = "-";
							}
							
				%>
				<tr class="data_tr">
					<td><%=s_id %></td>
					<td old="<%=old_name%>" <%if(!"2".equals(s_status)){ %>class="edit_td qtip"<%}else{ %>class="qtip"<%} %>><%=s_name%></td>
					<td><a href="javascript:prev(<%=s_id%>);"><emp:message key="appmage_common_opt_yulan" defVal="预览" fileName="appmage"></emp:message></a></td>
					<td><%=statusMap.get(s_status) %></td>
					<td><%="-9".equals(s_status)||"-3".equals(s_status)||"2".equals(s_status)||"3".equals(s_status)?s_publishtime:"-" %></td>
					<td><%="3".equals(s_status)?s_canceltime:"-" %></td>
					<td><%="-9".equals(s_status)||"-3".equals(s_status)||"2".equals(s_status)||"3".equals(s_status)?s_validate:"-" %></td>
					<td><%=s_createtime%></td>
					<td><%=s_creater%>(<%=s_username %>)</td>
					<td>
						<%if("1".equals(s_status)||"-2".equals(s_status)){ %>
							<a href="javascript:opp(1,<%=s_id%>);"><emp:message key="appmage_common_opt_bianji" defVal="编辑" fileName="appmage"></emp:message></a>
						<%}else{ %>
							<emp:message key="appmage_common_opt_bianji" defVal="编辑" fileName="appmage"></emp:message>
						<%} %>
					</td>
					<td>
						<%if("0".equals(s_status)){ %>
							<emp:message key="appmage_common_opt_fuzhi" defVal="复制" fileName="appmage"></emp:message>
						<%}else{%>
							<a href="javascript:opp(2,<%=s_id%>);"><emp:message key="appmage_common_opt_fuzhi" defVal="复制" fileName="appmage"></emp:message></a>
						<%} %>
					</td>
					<td>
						<%if("2".equals(s_status)||"-3".equals(s_status)){ %>
							<emp:message key="appmage_common_opt_shanchu" defVal="删除" fileName="appmage"></emp:message>
						<%}else{ %>
						<a href="javascript:del(<%=s_id%>);"><emp:message key="appmage_common_opt_shanchu" defVal="删除" fileName="appmage"></emp:message></a>
						<%} %>
					</td>
					<td>
						<%if("1".equals(s_status)||"-2".equals(s_status)){ %>
							<a href="javascript:publish(<%=s_id%>);"><emp:message key="appmage_xxfb_appsyfb_opt_fabu" defVal="发布" fileName="appmage"></emp:message></a>
						<%}else if("2".equals(s_status)||"-3".equals(s_status)){ %>
							<a href="javascript:unpublish(<%=s_id%>);"><emp:message key="appmage_xxfb_appsyfb_opt_qxfb" defVal="取消发布" fileName="appmage"></emp:message></a>
						<%}else{%>
							<emp:message key="appmage_xxfb_appsyfb_opt_fabu" defVal="发布" fileName="appmage"></emp:message>
						<% } %>
					</td>
				</tr>
				<%
						}
					}else{
				%>
				<tr><td align="center" colspan="13"><emp:message key="appmage_common_text_nodata" defVal="无记录" fileName="appmage"></emp:message></td></tr>
				<%} %>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="13">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
			</table>
		</form>
		</div>
		<div class="bottom">
			<div id="bottom_right">
				<div id="bottom_left"></div>
				<div id="bottom_main">
				</div>
			</div>
		</div>
	</div>
    <div class="clear"></div>
     <%--手机模拟预览 --%>
    <div id="previewBg"></div>
    <div id="mobilePreview">
    <div class="effectArea effectIndex" id="effectIndex">
    	<div class="appClose"></div>
			<div class="mobileModel">
				<div class="effectBox">
					<%--新闻列表--%>
					<div id="heads" class="effInner">
						<div class="sliderBox" id="sliderBox">
							<ul>
							</ul>
    						<div class="slider-text" id="slider-text"></div>
    						<div class="promo-nav" id="promo-nav"></div>
						</div>
						
						<div class="bd_line"></div>
						<ul id="newsList">
						</ul>
					</div>
				</div>
			</div>
		</div>
		</div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-b.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js"></script>
	<script src="<%=commonPath%>/common/js/common.js" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/wdate_extend.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
	<script src="<%=commonPath%>/common/js/Mslider.js"></script>
	<script type="text/javascript" src="<%=iPath%>/js/homelist.js"></script>
	<script type="text/javascript" src="<%=iPath%>/js/jquery.qtip-myjquery-q.js"></script>
	<script type="text/javascript">
		var path='<%=path%>';
		$(document).ready(function() {
			getLoginInfo("#loginInfo");
			showPreview();
			
			$('.appClose').click(function(){
				$('#mobilePreview,#previewBg').hide();
			})
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
		    $(window).resize(function(){
				showPreview();
			})
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){
				submitForm();
			});
			$("#status option[value='<%=status%>']").attr('selected',true);
			$(".newSelect").isSearchSelect({'width':'180','isInput':false,'zindex':0});
			
		});
	</script>
  </body>
</html>
