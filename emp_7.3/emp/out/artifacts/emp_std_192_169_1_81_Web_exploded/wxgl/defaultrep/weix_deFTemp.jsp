<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get(request.getAttribute("rTitle"));
	PageInfo pageInfo = new PageInfo();

	pageInfo = (PageInfo)request.getAttribute("pageInfo");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
	
 	String startdate=request.getParameter("startdate");
    if(startdate==null || "".equals(startdate))
    {
    	startdate="";
    }
    
	String enddate=request.getParameter("enddate");
    if(enddate==null || "".equals(enddate))
    {
    	enddate="";
    }
    
	String tempType=request.getParameter("tempType");
	if(tempType ==null||"".equals(tempType)){
	    tempType = "";
	}
	
	String onlyImgText=request.getParameter("onlyImgText");
	if(onlyImgText ==null||"".equals(onlyImgText)){
		onlyImgText = "";
	}
	
	String nogif=request.getParameter("nogif");
	if(nogif ==null){
		nogif = "";
	}
	
	LinkedHashMap tempTypeMap = new LinkedHashMap();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String qb = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_63", request);
	String dantw = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_75", request);
	String duotw = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_76", request);
	String wb = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_74", request);
	String sygzh = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_77", request);
	
	if("1".equals(onlyImgText)){
		tempTypeMap.put("23",qb);
		tempTypeMap.put("2",dantw);
		tempTypeMap.put("3",duotw);
	}else{
		tempTypeMap.put("0",qb);
		tempTypeMap.put("1",wb);
		tempTypeMap.put("2",dantw);
		tempTypeMap.put("3",duotw);
	}
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="wxgl_gzhgl_title_78" defVal="选择回复模板" fileName="wxgl"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body onload="">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<div id="container" class="container">
			<div id="rContent" class="rContent" style="padding-top: 5px; padding-bottom:5px;padding-left: 5px;padding-right: 5px;">
				<form name="pageForm" action="<%=path%>/weix_defaultReply.htm?method=getTemplates" method="post" id="pageForm">
				<div id="hiddenValueDiv" class="hidden"></div>
					<input type="hidden" id="depId" value="<%=request.getAttribute("connDepId")==null?"":request.getAttribute("connDepId") %>" />
					<input type="hidden" name="onlyImgText" id="onlyImgText" value="<%=onlyImgText%>"/>
					<input type="hidden" name="nogif" id="nogif" value="<%=nogif%>"/>
					<div id="condition">
						<table>
						<tr>
								<td><emp:message key="wxgl_gzhgl_title_58" defVal="回复类型：" fileName="wxgl"/></td>
							    <td>
							    <select name="tempType" id="tempType">
							    				<%for (Iterator it = tempTypeMap.keySet().iterator();it.hasNext();){
							    				    Object key = it.next();
							    				%>
							    				<option value="<%=String.valueOf(key)%>" <%=(tempType.equals(String.valueOf(key))) ? "selected" : ""%>> <%=tempTypeMap.get(String.valueOf(key))%></option>
												<% } %>
											</select>
							    </td>
							    <td class="tdSer" colspan="2" align="center"><a class="btn-search fr" id="search"></a></td>
							</tr>
							<tr>
							    <td><emp:message key="wxgl_gzhgl_title_68" defVal="创建时间：" fileName="wxgl"/></td>
								<td><input type="text" value='<%= "".equals(startdate) ? "":startdate %>' id="submitSartTime" name="startdate" 
										style="cursor: pointer; background-color: white;width:140px;" 
										class="Wdate" readonly="readonly" onclick="stime()"></td>
							    <td><emp:message key="wxgl_gzhgl_title_69" defVal="至：" fileName="wxgl"/></td>
							    <td>
							    <input type="text" value='<%= "".equals(enddate) ? "":enddate %>' id="submitEndTime" name="enddate" 
										style="cursor: pointer; background-color: white;width:140px;" 
										class="Wdate" readonly="readonly" onclick="rtime()">
							    </td>
							</tr>
							
						</table>
					</div>
					
					<div class="bd">
						<table id="content" >
							<thead>
								<tr>
									<th >
										<emp:message key="wxgl_gzhgl_title_70" defVal="选择" fileName="wxgl"/>
									</th>
									<th>
										<emp:message key="wxgl_gzhgl_title_71" defVal="模板名称" fileName="wxgl"/>
									</th>
									<th>
										<emp:message key="wxgl_gzhgl_title_72" defVal="关键字" fileName="wxgl"/>
									</th>
									<th>
										<emp:message key="wxgl_gzhgl_title_65" defVal="回复内容" fileName="wxgl"/>
									</th>
									<th>
										<emp:message key="wxgl_gzhgl_title_73" defVal="回复类型" fileName="wxgl"/>
									</th>
									<th>
										<emp:message key="wxgl_gzhgl_title_66" defVal="公众帐号" fileName="wxgl"/>
									</th>
									<th>
										<emp:message key="wxgl_gzhgl_title_6" defVal="创建时间" fileName="wxgl"/>
									</th>
									<th>
										<emp:message key="wxgl_gzhgl_title_7" defVal="操作" fileName="wxgl"/>
									</th>
								</tr>
							</thead>
							<tbody id="tbs">
							<%
							@SuppressWarnings("unchecked")
							List<DynaBean> beans = (List<DynaBean>)request.getAttribute("templelist");
							if(beans!=null && beans.size()>0)
							{
								for(DynaBean bean:beans)
								{
							%>
										<tr>
											<td >
												<input type="radio" name="checklist" value="<%=bean.get("t_id")%>" />
												<xmp style="display:none">
												<%
												String t_name=(String)bean.get("t_name");
												if(t_name==null || "".equals(t_name)){out.print("-");}else{%>
													<%=(String)bean.get("t_name")%>
												<%} %>
												</xmp>
											</td>
											<td>
												<%
												if(t_name==null || "".equals(t_name)){out.print("-");}else{ 
												%>
													<span id="tempname<%=bean.get("t_id")%>" style="display:none"><xmp><%=t_name %></xmp></span>
													<xmp><%=t_name.length()>5?t_name.substring(0,5)+"...":t_name %></xmp>
													<label class="titleTip" style="display:none"><xmp><%=t_name %></xmp></label>
												<%} %>
											</td>
											<td >
											<%
												String key_wordsvo= String.valueOf(bean.get("key_wordsvo"));
												if(key_wordsvo==null || "".equals(key_wordsvo)){out.print("-");}else{ 
												%>
													<label class="titleTip" style="display:none"><xmp><%=key_wordsvo %></xmp></label>
													<xmp><%=key_wordsvo.length()>5?key_wordsvo.substring(0,5)+"...":key_wordsvo %></xmp>
												<%} %>
											</td>
											<td>
												<%
												String msg_text=(String)bean.get("msg_text");
												if(msg_text==null || "".equals(msg_text)){out.print("-");}else{ 
												%>
													<label class="titleTip" style="display:none"><xmp><%=msg_text %></xmp></label>
													<xmp><%=msg_text.length()>5?msg_text.substring(0,5)+"...":msg_text %></xmp>
												<%} %>
											</td>
											<td>
											<%
											Object sta =bean.get("msg_type");
											String s = sta.toString();
											if("0".equals(s))
											{
												out.print(wb);
											}else if("1".equals(s))
											{
												out.print(dantw);
											}else if("2".equals(s))
											{
												out.print(duotw);
											}
											%>
											</td>
											<td >
												<%
												String accoutname=String.valueOf(bean.get("accoutname"));
												if(bean.get("accoutname")==null||accoutname==null||"".equals(accoutname)){
													accoutname = sygzh;
												}%>
													<label class="titleTip" style="display:none"><xmp><%=accoutname %></xmp></label>
													<xmp><%=accoutname.length()>5?accoutname.substring(0,5)+"...":accoutname %></xmp>
											</td>
											<td  >
												<%=bean.get("createtime")!=null?sdf.format(bean.get("createtime")):"-" %>
											</td>
											<td >
												 <a  onclick='preview(<%=bean.get("t_id")%>)'><emp:message key="wxgl_button_11" defVal="预览" fileName="wxgl"/></a>
											</td>
										</tr>
										<%
										}
									}else{
										%>
									<tr>
									<td colspan="13">
										<emp:message key="wxgl_gzhgl_title_11" defVal="无记录" fileName="wxgl"/>
									</td>
									</tr>
								<%} %>
							</tbody>
							<tfoot>
	                        <tr>
	                            <td colspan="11">
	                                <div id="pageInfo"></div>
	                            </td>
	                        </tr>
	                        </tfoot>
						</table>
					</div>	
										
				</form>
				</div>
		</div>
    <div class="clear"></div>
	<%--预览--%>
	<div id="divBox" style="display:none" title="<emp:message key='wxgl_button_11' defVal='预览' fileName='wxgl'/>">
		<div style="width:240px;height:460px;margin-top:-3px; background:url(<%=commonPath %>/wxcommon/img/iphone5.jpg);">
			<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="msgPreviewFrame" src=""></iframe>	
		</div>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/wxcommon/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
	<script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">

	
		$(document).ready(function() {
		$("#tempType").isSearchSelect({'width':'165','select_height':'23','isInput':false,'zindex':0,"class":"div_bd"});
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
			
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$("#search").click(function(){ submitForm(); });
		});
		
		function modify(t,i)
		{
			$("#msgcont").empty();
			$("#msgcont").text($(t).children("label").children("xmp").text());
			dlog = art.dialog.through({
				    content: document.getElementById('modify'),
				    id: 'modify',
				    lock: false
			});
			if(i==1)
			{
				art.dialog.through({"id":"modify"}).title(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_52"));
			}
			else if(i==2)
			{
				art.dialog.through({"id":"modify"}).title(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_53"));
			}
			else if(i==3){
				art.dialog.through({"id":"modify"}).title(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_53"));
			}
			else if(i==4){
				art.dialog.through({"id":"modify"}).title(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_37"));
			}
		}

		//iframe嵌套的层里面这种方式特殊处理，拿父层的信息
		function getLoginInfo1(ids)
		{
			var $pa = $(window.parent.document);
			var pahtm = $pa.find("#hiddenValueDiv").html();
			$(ids).html(pahtm);
		}

		//全选中
		function checkAlls(e,str)    
		{  
			var a = document.getElementsByName(str);    
			var n = a.length;    
			for (var i=0; i<n; i=i+1)   
			{
				a[i].checked =e.checked; 
			} 
		}

		//开始时间选择控制
		function rtime(){
		    //var max = "2099-12-31 23:59:59";
		    var max = "2099-12-31";
		    var v = $("#submitSartTime").attr("value");
			if(v.length != 0)
			{
			    var year = v.substring(0,4);
				var mon = v.substring(5,7);
				var day = 31;
				if (mon != "12")
				{
				    mon = String(parseInt(mon,10)+1);
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
				    year = String((parseInt(year,10)+1));
				    mon = "01";
				}
				//max = year+"-"+mon+"-"+day+" 23:59:59"
				max = year+"-"+mon+"-"+day+""
			}
			//WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
		
		};

		//结束时间选择控制
		function stime(){
		   // var max = "2099-12-31 23:59:59";
		   var max = "2099-12-31";
		    var v = $("#submitEndTime").attr("value");
		  //  var min = "1900-01-01 00:00:00";
		   var min = "1900-01-01";
			if(v.length != 0)
			{
			    max = v;
			    var year = v.substring(0,4);
				var mon = v.substring(5,7);
				if (mon != "01")
				{
				    mon = String(parseInt(mon,10)-1);
				    if (mon.length == 1)
				    {
				        mon = "0"+mon;
				    }
				}
				else
				{
				    year = String((parseInt(year,10)-1));
				    mon = "12";
				}
			//	min = year+"-"+mon+"-01 00:00:00"
				min = year+"-"+mon+"-01"
			}
			//WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
		
		};

		//回复预览
		function preview(tempId){
			var url = "<%=path%>/weix_keywordReply.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
			showbox({src:url});
			/*$("#msgPreviewFrame").attr("src",url);
			//$("#divBox").dialog("open");
			dlog = art.dialog.through({
				title: "预览",
			    content: document.getElementById('divBox'),
			    id: 'divBox',
			    lock: true
			});
			$('.bgiframe2').show();*/
	    }
		
		function doGo(url)
		{
			location.href = url;
		}
		
		$("#content td").hover(function(){
			if($(this).find(".titleTip").size()>0){
				$(this).attr("title",$.trim($(this).find(".titleTip").text()));
			}
			},function(){
				$(this).removeAttr("title");
			}
		);
	</script>
	</body>
</html>
