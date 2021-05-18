<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
	
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
	String menuCode = titleMap.get("employeeBook");
	PageInfo pageInfo = new PageInfo();
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

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
    
	String aId=request.getParameter("tempType");
	if(aId ==null||"".equals(aId)){
	   aId = "";
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>选择回复模板</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css" rel="stylesheet" type="text/css" />
		
		<link href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css" rel="stylesheet" type="text/css" />
        <link href="<%=iPath %>/css/select.css" rel="stylesheet" type="text/css" />
	</head>
	<body onload="">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
			<%-- header开始 --%>
			<%-- header结束 --%>
		<div id="container" class="container" style="padding-left: 5px;">
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
				
				<div>
				<form name="pageForm" action="<%=path%>/cwc_defaultrep.htm?method=getLfTemplateByWeix" method="post" id="pageForm">
				<div id="hiddenValueDiv" class="hidden"></div>
					<div class="buttons">
					</div>
					<input type="hidden" id="depId" value="<%=request.getAttribute("connDepId")==null?"":request.getAttribute("connDepId") %>" />
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td><emp:message key="wexi_qywx_hfgl_text_9" defVal="创建时间："
											fileName="weix"></emp:message></td>
									<td>
										<input type="text" value='<%= "".equals(startdate) ? "":startdate %>' id="submitSartTime" name="startdate" 
										style="cursor: pointer; width: 157px;background-color: white;" 
										class="Wdate" readonly="readonly" onclick="stime()">
									</td>
									<td><emp:message key="wexi_qywx_hfgl_text_10" defVal="至："
											fileName="weix"></emp:message></td>
									<td>
										<input type="text" value='<%= "".equals(enddate) ? "":enddate %>' id="submitEndTime" name="enddate" 
										style="cursor: pointer; width: 157px;background-color: white;" 
										class="Wdate" readonly="readonly" onclick="rtime()">
									</td>
									
									<td class="tdSer">
										<center><a id="search"></a></center>
									</td>
								</tr>
								<tr>
									<td ><emp:message key="wexi_qywx_hfgl_text_1" defVal="回复类型："
											fileName="weix"></emp:message></td>
									<td>
										<label>
											<select  style="width:157px;"  name="tempType" id="tempType" >
												<option value="0"><emp:message key="wexi_qywx_hfgl_text_2" defVal="全部"
											fileName="weix"></emp:message></option>
												<option value="1"><emp:message key="wexi_qywx_hfgl_text_3" defVal="文本"
											fileName="weix"></emp:message></option>
												<option value="2"><emp:message key="wexi_qywx_hfgl_text_4" defVal="单图文"
											fileName="weix"></emp:message></option>
												<option value="3"><emp:message key="wexi_qywx_hfgl_text_5" defVal="多图文"
											fileName="weix"></emp:message></option>
											</select>
										</label>
									</td>
									<td></td>
									<td></td>
									
									<td></td>
								</tr>
							</tbody>
						</table>
					</div>
		
					<table id="content">
						<thead>
							<tr>
								<th >
									<emp:message key="common_text_10" defVal="选择"
											fileName="common"></emp:message>
								</th>
								<th>
									<emp:message key="wexi_qywx_mrhf_text_21" defVal="模板名称"
											fileName="weix"></emp:message>
								</th>
								<th>
									<emp:message key="wexi_qywx_hfgl_text_13" defVal="关键字"
											fileName="weix"></emp:message>
								</th>
								<th>
									<emp:message key="wexi_qywx_hfgl_text_14" defVal="回复内容"
											fileName="weix"></emp:message>
								</th>
								<th>
									<emp:message key="wexi_qywx_hfgl_text_15" defVal="回复类型"
											fileName="weix"></emp:message>
								</th>
								<th>
									<emp:message key="wexi_qywx_hfgl_text_16" defVal="公众帐号"
											fileName="weix"></emp:message>
								</th>
								<th>
									<emp:message key="wexi_qywx_hfgl_text_17" defVal="创建时间"
											fileName="weix"></emp:message>
								</th>
								<th>
								 	<emp:message key="wexi_qywx_mrhf_text_8" defVal="操作"
										fileName="weix"></emp:message>  
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
												<a onclick="javascript:modify(this,3)">
												<label style="display:none"><xmp><%=t_name %></xmp></label>
												<span id="tempname<%=bean.get("t_id")%>" style="display:none"><xmp><%=t_name %></xmp></span>
												<xmp><%=t_name.length()>5?t_name.substring(0,5)+"...":t_name %></xmp>
												</a> 
											<%} %>
										</td>
										<td >
										<%
											String key_wordsvo= String.valueOf(bean.get("key_wordsvo"));
											if(key_wordsvo==null || "".equals(key_wordsvo)){out.print("-");}else{ 
											%>
												<a onclick="javascript:modify(this,2)">
												<label style="display:none"><xmp><%=key_wordsvo %></xmp></label>
												<xmp><%=key_wordsvo.length()>5?key_wordsvo.substring(0,5)+"...":key_wordsvo %></xmp>
												</a> 
											<%} %>
										</td>
										<td>
											<%
											String msg_text=(String)bean.get("msg_text");
											if(msg_text==null || "".equals(msg_text)){out.print("-");}else{ 
											%>
												<a onclick="javascript:modify(this,1)">
												<label style="display:none"><xmp><%=msg_text %></xmp></label>
												<xmp><%=msg_text.length()>5?msg_text.substring(0,5)+"...":msg_text %></xmp>
												</a> 
											<%} %>
										</td>
										<td>
										<%
										Object sta =bean.get("msg_type");
										String s = sta.toString();
										if("0".equals(s))
										{
											out.print(MessageUtils.extractMessage("weix","wexi_qywx_hfgl_text_3",request));
										}else if("1".equals(s))
										{
											out.print(MessageUtils.extractMessage("weix","wexi_qywx_hfgl_text_4",request));
										}else if("2".equals(s))
										{
											out.print(MessageUtils.extractMessage("weix","wexi_qywx_hfgl_text_5",request));
										}
										%>
										</td>
										<td >
											<%
											String accoutname=String.valueOf(bean.get("accoutname"));
											if(bean.get("accoutname")==null||accoutname==null||"".equals(accoutname)){
												accoutname = MessageUtils.extractMessage("weix","wexi_qywx_hfgl_text_7",request);
											}%>
												<a onclick="javascript:modify(this,4)">
												<label style="display:none"><xmp><%=accoutname %></xmp></label>
												<xmp><%=accoutname.length()>5?accoutname.substring(0,5)+"...":accoutname %></xmp>
												</a>
										</td>
										<td  >
											<%=bean.get("createtime")!=null?sdf.format(bean.get("createtime")):"-" %>
										</td>
										<td >
											 <a  onclick='preview(<%=bean.get("t_id")%>)'><emp:message key="common_text_6" defVal="预览"
											fileName="common"></emp:message></a>
										</td>
									</tr>
									<%
									}
								}else{
									%>
								<tr>
								<td colspan="13">
										<emp:message key="weix_qywx_gzzhgl_text_10" defVal="无记录"
											fileName="weix"></emp:message>
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
				</form>
				</div>
				
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
	
    <div id="modify" title="信息内容"  style="padding:5px;width:300px;height:160px;display:none">
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
	
	<%--预览--%>
	<div id="divBox" style="display:none" title="<emp:message key="common_text_6" defVal="预览"
											fileName="common"></emp:message>">
		<div style="width:240px;height:460px;margin-left:30px; margin-top:-3px; background:url(<%=commonPath %>/common/img/iphone5.jpg);">
			<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="msgPreviewFrame" src=""></iframe>	
		</div>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/weix_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
	
	<script type="text/javascript" src="<%=iPath %>/js/jquery.selectnew.js"/></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#tempType option[value='${tempType}']").attr('selected','selected');
			getLoginInfo1("#hiddenValueDiv");
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
			$('#modify').dialog({
				autoOpen: false,
				width:250,
			    height:200
			});

			 $("#divBox").dialog({
					autoOpen: false,
					height:510,
					width: 300,
					modal: false,
					bgiframe: true ,
					overlay: {opacity: 1.0, background: "white" ,overflow:'hidden'},
					close:function(){
				      $("#msgPreviewFrame").attr("src","");
				      $('.bgiframe2').hide();
					}
			 });
		});
		
		function modify(t,i)
		{
			$("#msgcont").empty();
			$("#msgcont").text($(t).children("label").children("xmp").text());
			if(i==1)
			{
				$('#modify').dialog('option','title',getJsLocaleMessage("weix","weix_qywx_mrhf_text_1"));
			}
			else if(i==2)
			{
				$('#modify').dialog('option','title',getJsLocaleMessage("weix","weix_qywx_mrhf_text_2"));
			}
			else if(i==3){
				$('#modify').dialog('option','title',getJsLocaleMessage("weix","weix_qywx_mrhf_text_3"));
			}
			else if(i==4){
				$('#modify').dialog('option','title',getJsLocaleMessage("weix","weix_qywx_mrhf_text_4"));
			}
			$('#modify').dialog('open');
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
			url = "<%=path%>/cwc_replymanger.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
			$("#msgPreviewFrame").attr("src",url);
			$("#divBox").dialog("open");
			$('.bgiframe2').show();
	    }
	</script>
	</body>
	
</html>
