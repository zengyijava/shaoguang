<%@ page language="java" import="com.montnets.emp.common.constant.ViewParams" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.netnews.vo.VisitDATAvo"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path=request.getContextPath();
String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String rTitle = (String)request.getAttribute("rTitle");
String menuCode = titleMap.get(rTitle);
String recvtime = request.getParameter("recvtime");
String sendtime = request.getParameter("sendtime");
@ SuppressWarnings("unchecked")
List<VisitDATAvo> vos = (List<VisitDATAvo>)request.getAttribute("netVisitList");
@ SuppressWarnings("unchecked")
List<LfSysuser> lisuser = (List<LfSysuser>)request.getAttribute("userlist");
String langName = (String)session.getAttribute("emp_lang");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode)%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link type="text/css" rel="stylesheet" href="<%=commonPath%>/ydwx/css/style.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link href="<%=commonPath %>/common/css/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/reviewsend.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/netVisitCount.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
	</head>
	
	<body id="ydwx_netVisitCount">
	<%=ViewParams.getPosition(empLangName,menuCode) %>
	<div id="rContent" class="rContent">	
	<%
		if(CstlyeSkin.contains("frame4.0")){
		%>
			<input id='hasBeenBind' value='1' type='hidden'/>
		<%
			}
	 %>		
	<form name="pageForm" action="<%=path%>/wx_visitreport.htm?method=find&type=2" method="post" id="pageForm">
	<div style="display:none" id="hiddenValueDiv"></div>
	<div id="overlay2"></div>
	
			<%--<div id="aprocitys" onclick="javascript:bycheck(1)"  class="title_bg"  style="float: left;width: 300px;cursor: pointer;height: 40px;margin-left: 5px;margin-top: 20px;text-align: center;vertical-align: middle;line-height: 40px;"">
		         	<label style="font-size: 14px;"><b>按网讯统计</b></label>
			</div>
			<div id="apromobile" onclick="javascript:bycheck(2)" style="float: left;width: 300px;cursor: pointer;height: 40px;margin-left: 10px;margin-top: 20px;text-align: center;vertical-align: middle;line-height: 40px;" >
	                <label style="font-size: 14px;"><b>按批次统计</b></label>
	        </div>
	        --%>
	        <div id="batchFileSend" class="ydwx_batchFileSend">
	        <div id="dtsend" onclick="javascript:bycheck(1)" class="wjsendclss div_bg ydwx_dtsend">
				<div class="ydwx_dtsend_sub">
						<font class="send_ac2 ydwx_send_ac2">
						<emp:message key="ydwx_wxcxtj_fwtj_anwxtj" defVal="按网讯统计" fileName="ydwx"></emp:message></font>
				</div>
			</div>
	        <div id="wjsend" onclick="javascript:bycheck(2)" class="dtsendclass div_bg ydwx_wjsend">
	             <div class="ydwx_wjsend_sub">
	                 <font class="send_ac1"><emp:message key="ydwx_wxcxtj_fwtj_anpctj" defVal="按批次统计" fileName="ydwx"></emp:message></font>
	             </div>
	       </div> 
			</div>
			<div class="buttons" >
				<div id="toggleDiv">
				</div>
				<a id="exportCondition" onclick="javascript:importExcel()"><emp:message key='ydwx_common_btn_daochu' defVal='导出' fileName='ydwx'></emp:message></a>
			</div>
			<div id="condition">
				<table>
					<tr>
						<td>
							<span><emp:message key="ydwx_wxcxtj_fstj_czys" defVal="操作员:" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<select id="option" name="douserd" class="ydwx_douserd">
								<option value=""><emp:message key="ydwx_wxgl_quanbu" defVal="全部" fileName="ydwx"></emp:message></option>
								<%
	
									if(lisuser!=null&&lisuser.size()>0){
										for(LfSysuser lfsysuser:lisuser){
								%>
											<option value="<%=lfsysuser.getUserId() %>" <%if(request.getParameter("douserd")!=null&&request.getParameter("douserd").toString().equals(lfsysuser.getUserId()+"")){%> selected="selected" <%} %> ><%=lfsysuser.getName() %></option>
								<%
										}
									}
	
								 %>
							</select>
						</td>
						<td>
							<span><emp:message key="ydwx_wxgl_wxbh" defVal="网讯编号:" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<input type="text" id="busid" name="busid" onkeyup="numberControl($(this))" onblur="numberControl($(this))" value="<%=request.getParameter("busid")==null?"":request.getParameter("busid") %>" class="ydwx_wxno">
						</td>
						<td>
							<span><emp:message key="ydwx_wxgl_wxmc" defVal="网讯名称:" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<input id="busname"  name = "busname" type="text" value="<%=request.getParameter("busname")==null?"":request.getParameter("busname") %>" class="ydwx_busname">
						</td>
						
						<td  class="tdSer">
							<center><a id="search"></a></center>
						</td>
					</tr>
					<tr>
						<td>
							<span><emp:message key="ydwx_common_time_fasongshijians" defVal="发送时间：" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<input type="text" readonly="readonly" class="Wdate ydwx_sendtime" onclick="stime()" 
												value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime"
													 <%--onchange="onblourSendTime('end')" --%>>
						</td>
						<td>
							<span><emp:message key="ydwx_common_time_zhi" defVal="至：" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<input type="text" readonly="readonly" class="Wdate ydwx_recvtime"  onclick="rtime()"
												value="<%=recvtime==null?"":recvtime %>" 
												 id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
						
					</tr>
					
				</table>
			
			</div>
			<div id ="tablelist" >
				<table id="content" class="ydwx_content">
					<thead>
						<tr>
						   <th>
								<emp:message key="ydwx_wxcxtj_fstj_czy" defVal="操作员" fileName="ydwx"></emp:message>
						   </th>
						   <th>
								<emp:message key="ydwx_wxcxtj_fstj_lshjg" defVal="隶属机构" fileName="ydwx"></emp:message>
						   </th>
						   <th>
								<emp:message key="ydwx_wxgl_wxbhs" defVal="网讯编号" fileName="ydwx"></emp:message>
						   </th>
					　		<th>
								<emp:message key="ydwx_wxgl_wxmcs" defVal="网讯名称" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_fwtj_dxxx" defVal="短信消息" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_common_time_fasongshijian" defVal="发送时间" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_common_time_youxiaoshijian" defVal="有效时间" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_fwtj_fwcsh" defVal="访问次数" fileName="ydwx"></emp:message>
							</th>
							<%--<th>
								访问成功数
							</th>
							<th>
								访问失败数
							</th>
							--%><th>
								<emp:message key="ydwx_wxcxtj_fwtj_fwrsh" defVal="访问人数" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_fwtj_hmxq" defVal="号码详情" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_fwtj_ymxq" defVal="页面详情" fileName="ydwx"></emp:message>
							</th>
						</tr>
					</thead>
					<tbody>
					<%
					if(vos!=null && vos.size()>0)
					{
						for(VisitDATAvo vo:vos)
						{
					%>
					<tr>
					<td><%=vo.getUNAME()!=null?vo.getUNAME():"" %></td>
					<td><%=vo.getUDEP()!=null?vo.getUDEP():"" %></td>
					<td><%=vo.getNETID()!=null?vo.getNETID():"" %></td>
					<td><a href="javascript:Look(<%=vo.getNETID()!=null?vo.getNETID():"" %>);"><%=vo.getNETNAME()!=null?vo.getNETNAME():"" %></a></td>
					<td>
						<%
							//当使用String中的replaceAll方法时，如果替换的值中包含有$符号时，在进行替换操作时会出现如下错误。
							//替换操作前后分别对替换值中的$符号进行encode和decode操作
							String replacement = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_32",request);
							//replacement = replacement.replaceAll("\\$", "RDS_CHAR_DOLLAR");// encode replacement;
							String temp = vo.getNETMSG().replaceAll("#[pP]_(\\d+)#",replacement);
							//temp = temp.replaceAll("RDS_CHAR_DOLLAR", "\\$");// decode replacement;
						%>
						<a onclick=javascript:modify(this)>
							<label style="display:none"><xmp><%=vo.getNETMSG()!=null?temp:"" %></xmp></label>
							<xmp><%=vo.getNETMSG()!=null?temp.length()>5?temp.substring(0,5)+"...":temp:"" %></xmp>
						</a>
					</td>
					<td>
					<%
					java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									String sendedTime = "-";
									if(vo.getReState()==2){//审批不通过 （发送时间为空）
										out.print("-");
									}else if(vo.getSubState()==3){//撤销任务（空）
										out.print("-");
									}else if(vo.getSendstate()==5){//超时未发送（空）
										out.print("-");
									}else if(vo.getTimerStatus()==0 && vo.getReState()==-1){//未定时未审批（待审批）（空）
										out.print("-");
									}else if(vo.getTimerStatus()==1){//定时了
										out.print(df.format(vo.getCREATDATE()));
									    if(vo.getSendstate()==0){out.print("("+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_dingshizhong",request)+")");}
									}else if(vo.getSendstate()==1 || vo.getSendstate()==2){//发送成功或者发送失败
										out.print(df.format(vo.getCREATDATE()));
									}else{
										out.print(vo.getCREATDATE()==null?"-":df.format(vo.getCREATDATE()));//这里面的情况就是sendstate=4(发送中)
									}
									%>
					
					</td>
					<td><%=vo.getTIMEOUT()==null?"":df.format(vo.getTIMEOUT()) %></td>
					<td><%=vo.getVisitcount() %></td>
					<%--<td><%=vo.getVisitsucc() %></td>
					<td><%=vo.getVisitfail() %></td>
					--%><td><%=vo.getVisitpelple() %></td>
					<td><a onclick="seeVisitInfo('<%=vo.getID()!=null?vo.getID():"" %>','<%=vo.getTaskid()!=null?vo.getTaskid():"" %>')"><emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message></a></td>
					<td><a onclick="seeVisitPageList('<%=vo.getID()!=null?vo.getID():"" %>','<%=vo.getTaskid()!=null?vo.getTaskid():"" %>')"><emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message></a></td>
					</tr>
					<%
						}
					}else
					{
					%>
					<tr><td colspan="13"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
					<%
					}
					%>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="13">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
			</table>
		</div>
		
		<div id="divBox" class="hideDlg" style="display:none" title="<emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message>">
								<div  align="center" class="ydwx_divBox_sub">
					          	<emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message>
					          	 <select class="ym ydwx_ym"></select>
					        	</div>
								<div class="ydwx_iframe_dv">
									<iframe class="ydwx_preview_common" id="nm_preview_common" src=""></iframe>	
								</div>
							</div>
							
							 <div id="modify" title="<emp:message key="ydwx_wxcxtj_fwtj_dxnr" defVal="短信内容" fileName="ydwx"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
					<table width="240px">
						<thead>
							<tr style="padding-top:2px;margin-bottom: 2px;">
								<td>
									<span style="display:block;width:240px;"><label id="msg" style="width:100%;height:100%;word-break: break-all;white-space:normal;"></label></span>
									 
								</td>
								 
							</tr>
						   <tr style="padding-top:2px;">
								<td>
								</td>
								</tr>
							 
						</thead>
					</table>
				</div>
		
	</form>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydwx_<%=empLangName%>.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/scripts/netManger.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/scripts/function.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
				  //页码改变时  div层内容变化的方法
				  
		getLoginInfo("#hiddenValueDiv");
		$('#search').click(function(){submitForm();});
	
		$("#toggleDiv").toggle(function() {
			$("#condition").hide();
			$(this).addClass("collapse");
		}, function() {
			$("#condition").show();
			$(this).removeClass("collapse");
		});
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			 
			 $("#busname").live("keyup blur",function(){
						var value=$(this).val();
						if(value!=filterString(value)){
							$(this).val(filterString(value));
						}
			 });
			 
			 $("#divBox").dialog({
						autoOpen: false,
						height:520,
						width: 300,
						modal: true,
						close:function(){
						}
					});
	        //页码改变时  div层内容变化的方法
	        $(".ym").change(function(){
	            var id=$(this).val();
	            for(var i=0;i<listpage.length;i++){
	                if(id==listpage[i].id){
	                    // 此处为显示错误页面，避免进入登录页面
	                    if(listpage[i].content=="notexists"){
	                        $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
	                    }else{
	                        $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[i].id+".jsp");
	                    }
	                }
	            }
	        });
	        $("#searchIcon").click(function() {
		
					$("#condition").toggle();
					if($(this).attr("checked")){
					    $("#toggle").html(getJsLocaleMessage("ydwx","ydwx_common_chxtj"));
					}
					else{
					    $("#toggle").html(getJsLocaleMessage("ydwx","ydwx_common_chxtj"));
					  }
				});
		});
	
	
	 
	  function importExcel(){
	
		  if(confirm(getJsLocaleMessage("ydwx","ydwx_common_qrdchshjd")))
		   {
			   <%
			   if(vos!=null && vos.size()>0)
			   {
			   %>
				   var lguserid = $("#lguserid").val();
				   var lgcorpcode = $("#lgcorpcode").val();
				   var busid= '<%=request.getParameter("busid")==null?"":request.getParameter("busid") %>';
				   var busname= '<%=request.getParameter("busname")==null?"":request.getParameter("busname") %>';
				   var begintime='<%=sendtime==null?"":sendtime %>'; 
				   var endtime= '<%=recvtime==null?"":recvtime %>';
					$.ajax({
						type: "POST",
						url: "wx_visitreport.htm?method=getvisitExport",
						data: {
						type:"2",lguserid:lguserid,
						lgcorpcode:lgcorpcode,busid:busid,
						busname:busname,begintime:begintime,
						endtime:endtime
						},
						beforeSend: function(){
							page_loading();
						},
		                complete:function () {
							page_complete();
		                },
						success: function(result){
							if(result=='true'){
								download_href("wx_visitreport.htm?method=downloadFile&down_session=getvisitExport2");
		                    }else{
		                        alert(getJsLocaleMessage("ydwx","ydwx_common_dcshb"));
		                    }
			   			}
					});
				   //window.location.href="<%=path %>/wx_visitreport.htm?method=getvisitExport&type=2&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&busid="+busid+"&busname="+busname+"&begintime="+begintime+"&endtime="+endtime;
			   <%  
			   }else
			   {
				%>
					alert(getJsLocaleMessage("ydwx","ydwx_common_wshjdch"));
				<%	   
			   }
			   %>
		   }
	  
	  } 
		
	</script>
	<script type="text/javascript" src="<%=iPath %>/js/netVisitCount2.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
		$(function(){
	  		$('#option').isSearchSelect({'width':'180','isInput':false,'zindex':0});
   		});
	</script>
	</body>
</html>
