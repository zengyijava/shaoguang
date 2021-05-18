<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.entity.sms.LfBigFile"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
String path = request.getContextPath();
java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
PageInfo pageInfo = new PageInfo();
pageInfo = (PageInfo) request.getAttribute("pageInfo");
String id = request.getParameter("id");
String startTime = request.getParameter("sendtime");
String endTime = request.getParameter("recvtime");
@ SuppressWarnings("unchecked")
List<LfBigFile> bigfileList = (List<LfBigFile>) request.getAttribute("bigfileList");
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

String lguserid = request.getParameter("lguserid");
%>
<html>
	<head><%@ include file="/common/common.jsp"%>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=path%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=path%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=path%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		
	</head>
	<body>
		<div id="container" class="container">
			<!-- header结束 -->
			<!-- 内容开始 -->
			<form name="pageForm" action="common.htm?method=getLfBigFileBySms&lguserid=<%=request.getParameter("lguserid") %>" method="post"
					id="pageForm">
					
					<input type="hidden" name="lgcorpcode" value="<%=request.getParameter("lgcorpcode") %>"/>
					
					<div style="display:none" id="hiddenValueDiv"></div>
			<div id="rContent" class="rContent" style="padding:5px">
					<div id="condition">
						<table>
							<tr>
								<td>
									<span>文件批次：</span>
								</td>
								<td>
									<input type="text" name="id" 
										id="id" value="<%=null!=id?id:"" %>" style="width:147px" onkeyup="javascript:numberControl($(this))" />
								</td>
								<td>
									<span>上传时间：</span>
								</td>
								<td class="tableTime">
										<input type="text" style="cursor: pointer; width: 178px;background-color: white;" class="Wdate" readonly="readonly" onclick="sedtime()"
											 value="<%=startTime==null?"":startTime %>" id="sendtime" name="sendtime">
								</td>
									<td>
										至：
								</td>
								<td>
										<input type="text" style="cursor: pointer; width: 178px;background-color: white;" class="Wdate" readonly="readonly" onclick="revtime()"
											 value="<%=endTime==null?"":endTime %>" id="recvtime" name="recvtime">
											 
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
					  <tr>
							<th>
								<emp:message key="common_option" defVal="选择" fileName="common"></emp:message>
							</th>
							<th>
								文件批次
							</th>
							<th>
								文件名称
							</th>
							<th>
								提交号码数
							</th>
							<th>
								有效号码数
							</th>
							<th>
								无效号码数
							</th>
							<th>
								上传时间
							</th>			
									</tr>
								</thead>
								<tbody>
								<%
								if (bigfileList != null && bigfileList.size() > 0)
								{
									for (LfBigFile tem : bigfileList)
									{
								%>
									<tr>
										<td>
											<input type="radio" name="checklist" value="<%=tem.getId()%>" />
											<xmp style="display:none" class="pre-wrap"><%=tem.getBusCode()%></xmp>
											<xmp style="display:none" class="pre-wrap"><%=tem.getEffNum()%></xmp>
										</td>
										<td>
											<%=tem.getId() %>
										</td>
										<td>
											<%=tem.getFileName()==null?"-":tem.getFileName() %>
										</td>
										<td  >
									    <%=tem.getSubCount()==null?"-":tem.getSubCount() %>
										</td>
										<td  >
										<%=tem.getEffCount()==null?"-":tem.getEffCount() %>
										</td>
										<td  >
										<%=tem.getErrCount().intValue()+tem.getRepCount().intValue()+tem.getBlaCount().intValue() %>
										</td>
										<td >
										<%
									String sendtime;
									if(tem.getCreateTime() != null)
									{
										Date date = df.parse(tem.getCreateTime().toString());
										sendtime = df.format(date);
									}
									else
									{
										sendtime = "-";
									}
									out.print(sendtime);
									%>
										
										</td>
									</tr>
								<%
									}
								}else{
									%>
									<tr><td colspan="7"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
								<%} %>
						</tbody>
						<tfoot>

							<tr>
								<td><span style="color: #7f8b99">已经提交发送过的超大文件，不支持二次发送，需要重新上传</span></td>
								<td colspan="6">

									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					
			</div>
			<!-- 内容结束 -->
			<!-- foot开始 -->
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			</form>
			<!-- foot结束 -->
		</div>
		<div id="modify" title="<emp:message key="common_smsTemplate_1" defVal="模板内容" fileName="common"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=path%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript">
		var total=<%=pageInfo.getTotalPage()%>;
		var pageIndex=<%=pageInfo.getPageIndex()%>;
		var pageSize=<%=pageInfo.getPageSize()%>;
		var totalRec=<%=pageInfo.getTotalRec()%>;

		$(document).ready(function() {
			//showPageInfo2(total,pageIndex,pageSize,totalRec,[10]);
            showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[5, 10, 15]);
			$('#search').click(function(){
				
				var val = $("#id").val();
				if(isNaN(val)&&val!=""){
					alert("文件批次："+val +"不是数字");
					return;
				}
				submitForm();});
		});



		//发送起止时间控制
		function revtime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#sendtime").attr("value");
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
				max = year+"-"+mon+"-"+day+" 23:59:59"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
		
		};

			function sedtime(){
			       var max = "2099-12-31 23:59:59";
			    var v = $("#recvtime").attr("value");
			    var min = "1900-01-01 00:00:00";
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
					min = year+"-"+mon+"-01 00:00:00"
				}
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
			
			};

			function numberControl(va)
			{
				var pat=/^\d*$/;
				if(!pat.test(va.val()))
				{
					va.val(va.val().replace(/[^\d]/g,''));
				}
			}	
		</script>
		</body>
</html>
