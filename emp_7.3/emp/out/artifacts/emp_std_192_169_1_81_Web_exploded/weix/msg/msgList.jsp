<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import= "java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.entity.weix.LfWcAccount" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("msgrep");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String lguserid = (String)request.getParameter("lguserid");
 	String lgcorpcode = (String)request.getParameter("lgcorpcode");
 	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
 	
    @ SuppressWarnings("unchecked")
    List<DynaBean> msgbeans = (List<DynaBean>)request.getAttribute("msgbeans");
    PageInfo pageInfo = new PageInfo();
    pageInfo=(PageInfo)request.getAttribute("pageInfo");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    LinkedHashMap<String,String> msgtpList = (LinkedHashMap<String,String>)request.getAttribute("msgtpList");
    List<LfWcAccount> acctList = (List<LfWcAccount>)request.getAttribute("acctList");
    
    String aId=request.getParameter("aId");
   // System.out.println(aId);
    if(aId==null || "".equals(aId))
    {
    	aId="-1";
    }
    String msgtp=request.getParameter("msgtp");
    if(msgtp==null || "".equals(msgtp))
    {
    	msgtp="-1";
    }
    String wcname=request.getParameter("wcname");
    if(wcname==null || "".equals(wcname))
    {
    	wcname="";
    }
    String beginTime=request.getParameter("beginTime");
    if(beginTime==null || "".equals(beginTime))
    {
    	beginTime="";
    }
    String endTime=request.getParameter("endTime");
    if(endTime==null || "".equals(endTime))
    {
    	endTime="";
    }
 %>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/frame.css"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/table.css"/>
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/empSelect.css"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/empSelect.css"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css">
		<link rel="stylesheet" type="text/css" href="<%=skin%>/newjqueryui.css">	
		
		<style>
			#dropdownMenu span {
				float:none !important;
			}
			.img-circle {
	    		border-radius: 500px 500px 500px 500px;
				}
			.headimg5 {
			    border: 1px solid #CCCCCC;
			    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
			    height: 38px;
			    width: 38px;
			}
		</style>
	</head>
	<body>
		<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="" method="post" id="pageForm">
					<div style="display:none" id="hiddenValueDiv"></div>
					<div class="buttons">
						<div id="toggleDiv"></div>
					</div>	
				<div id="condition" style="display: block;">
					<table>
						<tbody>
							<tr>
								<td><emp:message key="wexi_qywx_sxxx_text_1" defVal="公众帐号："
											fileName="weix"></emp:message></td>
								<td>
								<select id="aId" name="aId" class="input_bd" style="width:225px">
									<option value=""><emp:message key="wexi_qywx_sxxx_text_2" defVal="全部"
											fileName="weix"></emp:message></option>
									<%
			                            if(acctList != null && acctList.size()>0){
			                                for(LfWcAccount acct : acctList){
			                        %>
									<option value="<%=acct.getAId() %>" <%=((Long)acct.getAId()-Long.parseLong(aId)==0)?"selected":"" %>> <%=acct.getName() %></option>
									<%}}%>
								</select></td>
								<td><emp:message key="wexi_qywx_sxxx_text_3" defVal="消息类型："
											fileName="weix"></emp:message></td>
								<td>
									<select id="msgtp" name="msgtp" class="input_bd" style="width:225px">
										<option value=""><emp:message key="wexi_qywx_sxxx_text_1" defVal="全部"
											fileName="weix"></emp:message></option>
										<%
				                         Iterator it = msgtpList.keySet().iterator();
										 while (it.hasNext()){
											 
											    String key = (String)it.next();
									            String value = msgtpList.get(key);
				                        %>
										<option value="<%= key %>" <%=(String.valueOf(key).equals(String.valueOf(msgtp)))?"selected":"" %>> <%=value %></option>
										<%}%>
									</select>
								</td>
								<td class="tdSer">
								    <center><a id="search" onclick="javascript:doSearch()"></a></center>
								</td>	
							</tr>
							<tr>
								<td><emp:message key="wexi_qywx_sxxx_text_4" defVal="微信名称："
											fileName="weix"></emp:message></td>
								<td>
									<input name="wcname" value="<%= ("".equals(wcname)? "":wcname) %>" style="width:220px"/>
								</td>
									<td ><span><emp:message key="wexi_qywx_sxxx_text_5" defVal="发送时间："
											fileName="weix"></emp:message></span></td>
								<td >
						            <label>
                                      <input id="beginTime" type="text" value='<%= "".equals(beginTime) ? "":beginTime %>' name="beginTime" style="cursor: pointer; background-color: white;width: 150px;" class="Wdate input_bd" readonly="readonly" onclick="sedtime()" class="input_book">
                                    </label>
                                    <span><emp:message key="wexi_qywx_sxxx_text_6" defVal="至"
											fileName="weix"></emp:message></span>
                                      <label> <input id="endTime" type="text" value='<%= "".equals(endTime) ? "":endTime %>' name="endTime" style="cursor: pointer; background-color: white;width: 150px;" class="Wdate input_bd" readonly="readonly" onclick="revtime()" class="input_book">
                                    </label>
								</td>
								<td></td>	
							</tr>
						</tbody>
					</table>
				</div>
				<div id="resultSet">
                    <table id="content">
                        <thead>
                        <tr>
                            <th>
                              	<emp:message key="wexi_qywx_sxxx_text_7" defVal="微信名称"
											fileName="weix"></emp:message>
                            </th>
                            <th>
                               <emp:message key="wexi_qywx_sxxx_text_8" defVal="消息类型"
											fileName="weix"></emp:message>	
                            </th>
                            <th>
                              	<emp:message key="wexi_qywx_sxxx_text_9" defVal="消息内容"
											fileName="weix"></emp:message>
                            </th>
                            <th>
                              	<emp:message key="wexi_qywx_sxxx_text_10" defVal="公众帐号"
											fileName="weix"></emp:message>
                            </th>
                            <th>
                              	<emp:message key="wexi_qywx_sxxx_text_11" defVal="发送时间 "
											fileName="weix"></emp:message>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            if(msgbeans != null && msgbeans.size()>0){
                                for(DynaBean bean : msgbeans){
                        %>
                        <tr>
                            <td>
                             <% if(bean.get("username")!=null&&!"".equals(String.valueOf(bean.get("username")).trim())){ %>
                            	 <%= bean.get("username")%>
                             <%} else {%>
                             	<%if(bean.get("openid")!=null&&!"".equals(bean.get("openid"))){ %>
                             	 	<%= bean.get("openid")%>
                             	 <%} else {%>
                             	 	-
                             	 <%}%>
                             <%}%>
                            </td>
                            <td>
                            <%=msgtpList.get(String.valueOf(bean.get("msg_type"))) %>
                            </td>
                           
                            <%if(bean.get("msg_type")!=null&&"0".equals(String.valueOf(bean.get("msg_type")))) {%>
                             <td style="text-align:left;width:320px;">
                            	<%=bean.get("msg_text") %>
                             </td>
                            <%}else{%>
                             <td style="width:320px;">
                            	-
                             </td>
                            <%}%>
                            
                            <td>
                            <%=bean.get("acctname") %>
                            </td>
                            <td>
                      	    <%=formatter.format(bean.get("createtime"))%>
                            </td>
                        </tr>
                        <%
                            }
                        }else{
                        %>
                        <tr><td align="center" colspan="9"><emp:message key="wexi_qywx_sxxx_text_12" defVal="无记录"
											fileName="weix"></emp:message></td></tr>
                        <%} %>
                        </tbody>
                        <tfoot>
                        <tr>
                            <td colspan="9">
                                <div id="pageInfo"></div>
                            </td>
                        </tr>
                        </tfoot>
                    </table>
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
		</div>
    <div class="clear"></div>
    
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js"></script>				
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script>
	<script type="text/javascript">
	//搜索
	function doSearch(){
		 var form = $("form[name='pageForm']");
		 var url = "<%=request.getContextPath()%>/cwc_msgrep.htm?lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val();
          $("#pageForm").attr("action",url);
          document.forms['pageForm'].elements["pageIndex"].value =1;	// 回到第一页
          document.forms['pageForm'].submit();
	}
	
	$(document).ready(function(){
		$("#content select").empSelect({width:80});
		getLoginInfo("#hiddenValueDiv");
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
		
		//分页
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);

		//全选中
		$("#selectAll").click(function(){
			$("input[name=msgIds]").attr("checked", $(this).attr("checked"));
		});
	});


	//发送起止时间控制
	function sedtime(){
	    var max = "2016-07-31 23:59:59";
	    var v = $("#endTime").attr("value");
	    var min = "2013-08-01 00:00:00";
		if(v.length != 0)
		{
		    max = v;
		    var year = v.substring(0,4);
			var mon = v.substring(5,7);
			if (mon != "01")
			{
			    mon = String(parseInt(mon,10));
			    if (mon.length == 1)
			    {
			        mon = "0"+mon;
			    }
			}
			else
			{
			    year = String((parseInt(year,10)));
			    mon = "12";
			}
			min = year+"-"+mon+"-01 00:00:00"
		}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

	};

	//发送结束时间控制
	function revtime(){
	    var max = "2016-07-31 23:59:59";
	    var v = $("#beginTime").attr("value");
		if(v.length != 0)
		{
		    var year = v.substring(0,4);
			var mon = v.substring(5,7);
			var day = 31;
			if (mon != "12")
			{
			    mon = String(parseInt(mon,10));
			    if (mon.length == 1)
			    {
			        mon = "0"+mon;
			    }
		    switch(mon){
			    case "01":day = 31;break;
			    case "02":day = 28;break;
			    case "03":day = 31;break;
			    case "04":day = 30;break;
			    case "05":day = 31;break;
			    case "06":day = 30;break;
			    case "07":day = 31;break;
			    case "08":day = 31;break;
			    case "09":day = 30;break;
			    case "10":day = 31;break;
			    case "11":day = 30;break;
			    }
			}
			else
			{
			    year = String((parseInt(year,10)));
			    mon = "01";
			}
			max = year+"-"+mon+"-"+day+" 23:59:59"
		}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
	};
	</script>
	</body>
</html>