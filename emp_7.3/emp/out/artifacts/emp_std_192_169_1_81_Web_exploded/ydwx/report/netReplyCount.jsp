<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.netnews.vo.TRUSTDATAvo"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
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
List<TRUSTDATAvo> vos = (List<TRUSTDATAvo>)request.getAttribute("netReplyList");
@ SuppressWarnings("unchecked")
List<DynaBean> wdts =(List<DynaBean>)request.getAttribute("wdts");
@ SuppressWarnings("unchecked")
List<LfSysuser> lisuser = (List<LfSysuser>)request.getAttribute("userlist");


String busid=request.getParameter("busid");        //网讯编号
String busname=request.getParameter("busname");		//网讯名称
String title= request.getParameter("title");  	   //网讯主题
String hudcode=request.getParameter("hudcode");    //互动编号
String hudname=request.getParameter("hudname");    //互动名称
String skip =(String)request.getAttribute("skip");
String langName = (String)session.getAttribute("emp_lang");
if("true".equals(skip)){
	busid =(String)request.getAttribute("n_busid");
	busname =(String)request.getAttribute("n_busname");
	title =(String)request.getAttribute("n_title");
	hudcode =(String)request.getAttribute("n_hudcode");
	hudname =(String)request.getAttribute("n_hudname");
	sendtime =(String)request.getAttribute("n_begintime");
	recvtime =(String)request.getAttribute("n_endtime");
	
}
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
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<style type="text/css">
			#msg {
				white-space: pre-wrap;
				*white-space: pre;
				*word-wrap: break-word;
				word-break: break-all;
			}
			#content > thead > tr > th:nth-child(10){
				width: 10%;
			}
		</style>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<style type="text/css">
			#tbody1 > tr:nth-child(1) > td:nth-child(11),
			#tbody1 > tr:nth-child(1) > td:nth-child(12),
			#tbody1 > tr:nth-child(1) > td:nth-child(13),
			#tbody1 > tr:nth-child(1) > td:nth-child(14){
				width: 6%;
			}
		</style>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/netReplyCount.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
	</head>
	
	<body id="ydwx_netReplyCount">
	<%
			if(CstlyeSkin.contains("frame4.0")){
		%>
			<input id='hasBeenBind' value='1' type='hidden'/>
		<%
			}
		 %>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName,menuCode) %>
	<div id="rContent" class="rContent">			
	<form name="pageForm" action="<%=path%>/wx_count.htm?method=find" method="post" id="pageForm">
	<div style="display:none" id="hiddenValueDiv"></div>
	<div id="overlay2"></div>
	<div id="divBox" class="ydwx_divBox">
				<div class="ydwx_divBoxtop"  onmouseover="Move_obj2('divBoxtop')" id="divBoxtop">
					<b class="ydwx_divBoxtop_b1"><emp:message key="ydwx_wxgl_add_wxchk" defVal="网讯查看" fileName="ydwx"></emp:message></b>
					<b class="ydwx_divBoxtop_b2" onclick="cancel()"></b>
				</div>
				<hr>
				<div align="center" class="ydwx_nav_dv">
					<b class="ydwx_nav_dv_b"><emp:message key="ydwx_common_yemiandaohang" defVal="页面导航：" fileName="ydwx"></emp:message></b>
					<select class="ym ydwx_ym"></select>
				</div>
				<hr align="center" width="80%">
				<div align="center" class="ydwx_iphone_dv">
					<br />
					<br />
					<iframe width="214" height="363" id="nm_preview_common" src=""></iframe>
				</div>
				<hr>
			</div>
			<div class="buttons">
				<div id="toggleDiv">
				</div>
				<a id="exportCondition" onclick="javascript:importExcel()"><emp:message key='ydwx_common_btn_daochu' defVal='导出' fileName='ydwx'></emp:message></a>
			</div>
			<div id="condition">
				<table>
					<tr>
						<td>
							<span><emp:message key="ydwx_wxgl_wxbh" defVal="网讯编号：" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<input type="text" id="busid" onkeyup="numberControl($(this))" onblur="numberControl($(this))" name="busid" value="<%=busid==null?"":busid %>" class="ydwx_busid" >
						</td>
						<td>
							<span><emp:message key="ydwx_wxgl_wxmc" defVal="网讯名称：" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<input id="busname"  name = "busname" type="text" value="<%=busname==null?"":busname %>" class="ydwx_busname">
						</td>
						<td>
							<span><emp:message key="ydwx_wxcxtj_hftj_wxzhts" defVal="网讯主题：" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<input id="title"  name = "title" type="text" value="<%=title==null?"":title %>" class="ydwx_title">
						</td>
						<td  class="tdSer">
							<center><a id="search"></a></center>
						</td>
						</tr>
						<tr>
						<td>
							<span><emp:message key="ydwx_wxcxtj_hftj_hdlbs" defVal="互动类别：" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<select id="hudtype" name="hudtype" isInput="false">
								<option value=""><emp:message key="ydwx_wxgl_quanbu" defVal="全部" fileName="ydwx"></emp:message></option>
								<%
									if(wdts!=null&&wdts.size()>0){
										for(DynaBean wdt:wdts){
											String id="";
											String name ="";
											if(wdt.get("id")!=null){
												id=wdt.get("id").toString();
											}
											if(wdt.get("name")!=null){
												name=wdt.get("name").toString();
											}
											
								%>	
											<option value="<%=id %>" <%if(request.getParameter("hudtype")!=null&&request.getParameter("hudtype").toString().equals(id)){%> selected="selected" <%} %> ><%=name %></option>
								<%
										}
									}
								 %>
							</select>
						</td>
						<td>
							<span><emp:message key="ydwx_wxcxtj_hftj_hdbhs" defVal="互动编号：" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<input id="hudcode"  name = "hudcode" type="text" value="<%=hudcode==null?"":hudcode %>" class="ydwx_hudcode">
						</td>
						<td>
							<span><emp:message key="ydwx_wxcxtj_hftj_hdmchs" defVal="互动名称：" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<input id="hudname"  name = "hudname" type="text" value="<%=request.getParameter("hudname")==null?"":request.getParameter("hudname") %>" class="ydwx_hudname">
						</td>
						<td></td>
					</tr>
					
					<tr>
						<td>
							<span><emp:message key="ydwx_wxcxtj_fstj_czys" defVal="操作员：" fileName="ydwx"></emp:message></span>
						</td>
						<td>
							<select id="douserd" name="douserd" isInput="false">
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
						<td></td>
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
								<emp:message key="ydwx_wxcxtj_hftj_wxzht" defVal="网讯主题" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxgl_wxbhs" defVal="网讯编号" fileName="ydwx"></emp:message>
							</th>
						   <th>
								<emp:message key="ydwx_wxgl_wxmcs" defVal="网讯名称" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_fwtj_dxnr" defVal="短信内容" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_hftj_hdlb" defVal="互动类别" fileName="ydwx"></emp:message>
							</th>
					　　　	<th>
								<emp:message key="ydwx_wxcxtj_hftj_hdbh" defVal="互动编号" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_hftj_hdmch" defVal="互动名称" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_common_time_fasongshijian" defVal="发送时间" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_fwtj_jshrsh" defVal="接收人数" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_fwtj_fwrsh" defVal="访问人数" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_hftj_hfrsh" defVal="回复人数" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_hftj_hfcsh" defVal="回复次数" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_common_btn_xingqing" defVal="详情" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxcxtj_hftj_tjjg" defVal="统计结果" fileName="ydwx"></emp:message>
							</th>
						</tr>
					</thead>
					<tbody id="tbody1" align="center">
					<%
					if(vos!=null && vos.size()>0)
					{
						for(TRUSTDATAvo vo:vos)
						{
					%>
					<tr>
					<td><%=vo.getUNAME()!=null?vo.getUNAME():"" %></td>
					<td><%=vo.getUDEP()!=null?vo.getUDEP():"" %></td>
					<td><%=vo.getTitle()!=null?vo.getTitle():"" %></td>
					<td><%=vo.getNETID()!=null?vo.getNETID():"" %></td>
					<td><%=vo.getNETNAME()!=null?vo.getNETNAME():"" %></td>
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
					<td><%=vo.getNAMETYPE()!=null?vo.getNAMETYPE():"" %></td>
					<td><%=vo.getCODE()!=null?vo.getCODE():"" %></td>
					<td><%=vo.getNAME()!=null?vo.getNAME():"" %></td>
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
										out.print(df.format(vo.getSENDDATE()));
									    if(vo.getSendstate()==0){out.print("("+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_dingshizhong",request)+")");}
									}else if(vo.getSendstate()==1 || vo.getSendstate()==2){//发送成功或者发送失败
										out.print(df.format(vo.getSENDDATE()));
									}else{
										out.print(vo.getSENDDATE()==null?"-":df.format(vo.getSENDDATE()));//这里面的情况就是sendstate=4(发送中)
									}
									%>
					</td>
					<td><%=vo.getSpcount()!=null?vo.getSpcount():"0" %></td>
					<td><%=vo.getScount() %></td>
					<td><%=vo.getPcount() %></td>
					<td><%=vo.getCount() %></td>
					<td><a onclick="ToReplyPageList('<%=vo.getID()==null?"":vo.getID() %>','<%=vo.getNETID()==null?"":vo.getNETID() %>','<%=vo.getTaskid()==null?"":vo.getTaskid() %>')"><emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message></a></td>
					<td><a onclick="ToReplyInfo('<%=vo.getID()==null?"":vo.getID() %>','<%=vo.getNETID()==null?"":vo.getNETID() %>','<%=vo.getTitle()==null?"":vo.getTitle() %>','<%=vo.getSENDDATE()==null?"":vo.getSENDDATE() %>','<%=vo.getSpcount()==null?"":vo.getSpcount() %>','<%=vo.getMtid()==null?"":vo.getMtid() %>')">
					<emp:message key='ydwx_common_btn_chakan' defVal='查看' fileName='ydwx'></emp:message></a></td>
					</tr>
					<%
						}
					}else
					{
					%>
					<tr><td colspan="16"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
					<%
					}
					%>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="16">
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
			</table>
		</div>
		
			 <div id="modify" title="<emp:message key="ydwx_wxcxtj_fwtj_dxnr" defVal="短信内容" fileName="ydwx"></emp:message>" class="ydwx_modify">
					<table width="240px">
						<thead>
							<tr class="ydwx_modify_table_tr1">
								<td>
									<span class="ydwx_modify_span"><label id="msg" class="ydwx_msg"></label></span>
								</td>
							</tr>
						   <tr class="ydwx_modify_table_tr2">
								<td>
								</td>
						  </tr>
						</thead>
					</table>
				</div>
				<input id="path" name="path" type="hidden" value="<%=path %>" /> 
	</form>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydwx_<%=empLangName%>.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/scripts/netManger.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/scripts/function.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	
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
	
	        $("#busname,#title,#hudcode,#hudname").live("keyup blur",function(){
						var value=$(this).val();
						if(value!=filterString(value)){
							$(this).val(filterString(value));
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
				   var busid ='<%=request.getParameter("busid")==null?"":request.getParameter("busid") %>';
				   var hudtype='<%=request.getParameter("hudtype")==null?"":request.getParameter("hudtype") %>';
				   var serchuserid='<%=request.getParameter("douserd")==null?"":request.getParameter("douserd") %>';
				   var begintime ='<%=sendtime==null?"":sendtime %>';
				   var endtime ='<%=recvtime==null?"":recvtime %>';
				   
					$.ajax({
						type: "POST",
						url: "wx_count.htm?method=getReplyExport",
						data: {
						lguserid:lguserid,
						lgcorpcode:lgcorpcode,busid:busid,
						hudtype:hudtype,douserd:serchuserid,
						begintime:begintime,
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
								download_href("wx_count.htm?method=downloadFile&down_session=getReplyExport");
		                    }else{
		                        alert(getJsLocaleMessage("ydwx","ydwx_common_dcshb"));
		                    }
			   			}
					});
				   
				 //  window.location.href="<%=path %>/wx_count.htm?method=getReplyExport&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&busid="+busid+"&hudtype="+hudtype+"&douserd="+serchuserid+"&begintime="+begintime+"&endtime="+endtime;
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
	
	<script type="text/javascript" src="<%=iPath %>/js/netReplyCount.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	</body>
</html>