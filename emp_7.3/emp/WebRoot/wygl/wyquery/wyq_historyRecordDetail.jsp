<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.wyquery.vo.SendedMttaskVo"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
			
	@ SuppressWarnings("unchecked")
	List<SendedMttaskVo> mtList =(List<SendedMttaskVo>) request.getAttribute("mtList");
	Map<String,String> spgatesMap = (Map<String,String>)request.getAttribute("spgatesMap");
	String title = (String)request.getAttribute("title");
	String message = (String)request.getAttribute("message");
	String sendtime = (String)request.getAttribute("sendtime");
	String sendinfo = (String)request.getAttribute("sendinfo");
	String mtid = (String)request.getAttribute("mtid");
	String type=(String)request.getAttribute("type");
	String phone = (String)request.getAttribute("phone");
	String spisuncm = (String)request.getAttribute("spisuncm");
	String taskId = (String)request.getAttribute("taskId");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");

	String menuCode = titleMap.get("historyRecord");
	menuCode=menuCode==null?"0-0-0":menuCode;
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<%@include file="/common/common.jsp" %>
	<head>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
	</head>
	<body>
		<div id="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode,"发送详情查看") %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("wygl","wygl_common_text58",request)) %>
				<div class="rContent">
					<%              		
						if(btnMap.get(menuCode+"-0")!=null)                       		
						{                        	
					%>
					<div class="titletop" style="padding-left: 0;padding-top: 0">
						<table class="titletop_table" style="width: 100%">
								<tr>
								    <td class="titletop_td">
								     <emp:message key="wygl_common_text58" defVal="发送详情查看" fileName="wygl"></emp:message> 
						        	</td>
									<td align="right">
										<span class="titletop_font"    onclick="javascript:showback()">&larr;&nbsp;<emp:message key="wygl_common_text59" defVal="返回上一级" fileName="wygl"></emp:message></span>
									</td>
								</tr>
							</table>
					   </div>
						<div id="u_o_c_explain" class="div_bg">
							<p>
								<emp:message key="wygl_common_text60" defVal="基本信息" fileName="wygl"></emp:message>
							</p>
                            <table>
                              <tr>
                                 <td><b><emp:message key="wygl_common_text61" defVal="主题：" fileName="wygl"></emp:message></b></td>
                              </tr>
                              <tr>
                                 <td><%=title==null?"":(title.replaceAll("<","&lt;").replaceAll(">","&gt;")) %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="wygl_common_text62" defVal="内容：" fileName="wygl"></emp:message></b></td>
                              </tr>
                              <tr>
                                 <td style="word-wrap: break-word;word-break:break-all;"><%=message==null||"".equals(message)?"":message.replaceAll("<","&lt;").replaceAll(">","&gt;") %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="wygl_common_text11" defVal="发送时间：" fileName="wygl"></emp:message></b></td>
                              </tr>
                              <tr>
                                 <td><%=sendtime %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="wygl_common_text63" defVal="发送情况：" fileName="wygl"></emp:message></b></td>
                              </tr>
                              <tr>
                                <td><%=sendinfo %>
                                <br/>
                                <%--<font class="zhu">注：以上数据<%=StaticValue.CORPTYPE==0?"6小时":"每晚"%>汇总一次,可能与下面的实时数据有一定误差。</font>
                                --%>
                                <font class="zhu"><emp:message key="wygl_common_text64" defVal="注：以上数据" fileName="wygl"></emp:message><%=StaticValue.getCORPTYPE() ==0?MessageUtils.extractMessage("wygl","wygl_common_text65",request):MessageUtils.extractMessage("wygl","wygl_common_text66",request)%><emp:message key="wygl_common_text67" defVal="汇总一次,可能与下面的实时数据有一定误差。" fileName="wygl"></emp:message></font>
                                </td>
                              </tr>
                            </table>
						</div>
						<form name="pageForm" action="wyq_historyRecord.htm?method=findAllSendInfo" method="post">
						<input type="hidden" name="taskId" id="taskId" value="<%=taskId %>">
						<div class="buttons" style="padding-top: 10px;">
						  <div id="toggleDiv">								
						  </div> 
						  <span id="backgo" class="right mr5" onclick="javascript:showback()">&nbsp;<emp:message key="wygl_common_text68" defVal="返回" fileName="wygl"></emp:message></span>
						  <a id="exportCondition" ><emp:message key="wygl_common_text69" defVal="导出" fileName="wygl"></emp:message></a>				  					 			          
						</div>
						<div id="condition">
						 <input type="hidden" id="mtid" value="<%=mtid %>" name="mtid">
						 <table>
						    <tr>
						      <td style="display: none;"><emp:message key="wygl_common_text70" defVal="运营商：" fileName="wygl"></emp:message></td>
						      <td style="display: none;">
						           <select style="width:180px;"  id="spisuncm" name="spisuncm">
										<option value=""><emp:message key="wygl_common_text71" defVal="全部" fileName="wygl"></emp:message></option>
										<option value="0" <%="0".equals(spisuncm)?"selected":"" %>><emp:message key="wygl_common_text72" defVal="移动" fileName="wygl"></emp:message></option>
										<option value="1" <%="1".equals(spisuncm)?"selected":"" %> ><emp:message key="wygl_common_text73" defVal="联通" fileName="wygl"></emp:message></option>
										<option value="21" <%="21".equals(spisuncm)?"selected":"" %>><emp:message key="wygl_common_text74" defVal="电信" fileName="wygl"></emp:message></option>
									</select>
						      </td>
						      <td><emp:message key="wygl_common_text75" defVal="手机号码：" fileName="wygl"></emp:message></td>
						      <td>
						         <input type="text" style="width: 160px;" value='<%=phone==null?"":phone %>' id="phone" name="phone" maxlength="21" onkeyup="javascript:phoneInputCtrl($(this))"/>
						      </td>
						      <td><emp:message key="wygl_common_text76" defVal="状态：" fileName="wygl"></emp:message></td>
						      <td>
						          <select style="width:180px;"  id="type" name="type">
										<option value="1" <%="1".equals(type)?"selected":"" %>><emp:message key="wygl_common_text77" defVal="全部号码" fileName="wygl"></emp:message></option>
										<%--
										<option value="5" <%="5".equals(type)?"selected":"" %> >发送成功</option>
										<option value="6" <%="6".equals(type)?"selected":"" %> >状态未返</option>										
										--%>
										<option value="2" <%="2".equals(type)?"selected":"" %> ><emp:message key="wygl_common_text78" defVal="接收失败" fileName="wygl"></emp:message></option>
										<option value="3" <%="3".equals(type)?"selected":"" %>><emp:message key="wygl_common_text79" defVal="提交失败" fileName="wygl"></emp:message></option>
								  </select>
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
										<emp:message key="wygl_common_text80" defVal="序号" fileName="wygl"></emp:message>
									</th>
									<%-- <th>
									          运营商
									</th> --%>
									<th>
										<emp:message key="wygl_common_text81" defVal="手机号" fileName="wygl"></emp:message>
									</th>
									<th>
										<emp:message key="wygl_common_text47" defVal="通道号码" fileName="wygl"></emp:message>
									</th>
									<th>
										<emp:message key="wygl_common_text48" defVal="通道名称" fileName="wygl"></emp:message>
									</th>
									<th>
										<emp:message key="wygl_common_text22" defVal="发送时间" fileName="wygl"></emp:message>
									</th>
									<th>
										<emp:message key="wygl_common_text44" defVal="接收时间" fileName="wygl"></emp:message>
									</th>
									<th>
										<emp:message key="wygl_common_text82" defVal="内容" fileName="wygl"></emp:message>
									</th>
									<%-- <th>
										分条
									</th> --%>
									<th>
										<emp:message key="wygl_common_text83" defVal="状态" fileName="wygl"></emp:message>
									</th>
								</tr>
							</thead>
							<tbody>
							<%
							if(type.equals("0"))
							{
							%>
							<tr><td colspan="8" align="center"><emp:message key="wygl_common_text84" defVal="请点击按钮获取数据" fileName="wygl"></emp:message></td></tr>
							<%
							}else if(mtList!= null && mtList.size() > 0)
								{
								for(int i=0;i<mtList.size();i++)
								{
									SendedMttaskVo vo = mtList.get(i);
							%>
							<tr>
								<td>
									<%=i+1 %>
								</td> 
								<%-- <td>
				  					<%
									if(vo.getUnicom()-0==0){%>
									移动
									<%}else if(vo.getUnicom()-1==0){%>
									联通
									<%}else if(vo.getUnicom()-21==0){ %>
									电信
									<%} %>
								</td> --%>
								<td>
									<%
									   String phones = CV.replacePhoneNumber(btnMap,vo.getPhone());
									   out.print(phones);									   
									 %>
								</td>
								<td>
									<%=vo.getSpgate()==null?"-":vo.getSpgate() %>
								</td>
								<td>
									<%=spgatesMap.get(vo.getSpgate())==null?"-":spgatesMap.get(vo.getSpgate()) %>
								</td>
								<td>
									<%
										String format="--";
										if(vo.getSendtime()!=null){
											format=df.format(vo.getSendtime());
										}
									 %>
											<%=format %>
								</td>
								<td><%=vo.getRecvtime()==null?"":df.format(vo.getRecvtime()) %></td>
								<td  style="text-align:left;">
									<a onclick="javascript:modify(this)">
										<%
											String xmessage=vo.getMessage()==null?"":vo.getMessage();
										%>
										<label style="display:none"><xmp><%=xmessage %></xmp></label>
										<xmp><%=xmessage.length()>5?xmessage.substring(0,5)+"...":xmessage %></xmp>
									</a> 
								</td>
								
								<%-- <td>
									<%=vo.getPknumber() %>/<%=vo.getPktotal() %>
								</td> --%>
								<td class="textalign">
								<%if(vo.getErrorcode() != null){ %>
								    <%if(vo.getErrorcode().equals("DELIVRD")||vo.getErrorcode().trim().equals("0")){  %>
								         <label><emp:message key="wygl_common_text85" defVal="发送成功" fileName="wygl"></emp:message></label>  
								    <%}else if(vo.getErrorcode().toString().trim().length()>0){%>
								         <label><emp:message key="wygl_common_text86" defVal="失败" fileName="wygl"></emp:message>[<%=vo.getErrorcode() %>]</label>
								    <%}else { %>
								         <label>-</label>
								    <% }%>
								<%}else { %>
								    <label>-</label>
								<%} %>
								</td>

							</tr>
							<%
								}
								}
								else
								{
							%>
							<tr><td colspan="8" align="center"><emp:message key="wygl_common_text87" defVal="无记录" fileName="wygl"></emp:message></td></tr>
							<%} %>
							</tbody>
							<tfoot>
							<tr >
								<td colspan="8">
								<div id="pageInfo"></div>
								</td>
							</tr>
							</tfoot>
						</table>
						<div id="smssendparams" class="hidden"></div>
						</form>
						<div class="clear"></div>
						<%
								}
						%>
						
					 <div id="probar" style="display: none;text-align: center;">
							<p >
								<emp:message key="wygl_common_text88" defVal="请耐心等待....." fileName="wygl"></emp:message>
							</p>
							<div id="shows">
								<img src="<%=commonPath%>/common/img/loader.gif" />
							</div>							
					</div>
					<div id="modify" title="<emp:message key="wygl_common_text89" defVal="短信内容" fileName="wygl"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
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
					</div>
			<%--end rContent--%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wygl_<%=langName%>.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
			    getLoginInfo("#smssendparams");
				$("#toggleDiv").toggle(function() {
					$("#condition").hide();
					$(this).addClass("collapse");
				}, function() {
					$("#condition").show();
					$(this).removeClass("collapse");
				});
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				$('#search').click(function(){submitForm();});
			    $("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
				}, function() {
					$(this).removeClass("hoverColor");
				});	
	
				//导出全部数据到excel
				$("#exportCondition").click(
					function()
					{
						var langName ='<%=langName %>';	
			   			var mtid=$.trim($("#mtid").val());
			   			var type=$.trim($("#type").val());
			   			var lgcorpcode =$("#lgcorpcode").val();
						var lguserid =$("#lguserid").val();
						var phone = $("#phone").val();
						var spisuncm = $("#spisuncm").val();
					 	//if(confirm("确定要导出数据到excel?"))
					 	if(confirm(getJsLocaleMessage("wygl","wygl_common_text8")))
					 	{
					   		<%
					   		if(mtList!=null && mtList.size()>0 ){
					   		%>  
					   		    var IsexportAll = true;
					   		    //if(confirm("是否只需导出手机号(点“确定”则只导出手机号字段，点“取消”则导出所有页面显示字段)?"))
					   		    if(confirm(getJsLocaleMessage("wygl","wygl_common_text9")))
					   		    {
					   		        IsexportAll = false;
					   		    }  
		   	
					   		<%		     
					   		  	if(pageInfo.getTotalRec()<=500000){
					   		%>	
						   		   $.ajax({
										type: "POST",
										url: "<%=path%>/wyq_historyRecord.htm?method=smsReportAllExcel",
										data: {
											langName:langName,
											mtid:mtid,
											type:type,
											lgcorpcode:lgcorpcode,
											lguserid:lguserid,
											spisuncm:spisuncm,
											phone:phone,
											IsexportAll:IsexportAll
										},
							            beforeSend:function () {
							                page_loading();
							            },
							            complete:function () {
							           	  	page_complete()
							            },
							            success:function (result) {
							                  if (result == 'true') {
							                       download_href("<%=path%>/wyq_historyRecord.htm?method=wydetaildownloadFile");
							                  } else {
							                       //alert('导出失败！');
							                       alert(getJsLocaleMessage("wygl","wygl_common_text10"));
							                  }
				           				}
								 	});				
						   			//location.href="<%=path%>/wyq_historyRecord.htm?method=smsReportAllExcel&mtid="+mtid+"&type="+type+"&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid+"&spisuncm="+spisuncm+"&phone="+phone+"&IsexportAll="+IsexportAll;
						   		    
						   		<%}else{
						   		%>				   		    
						   		     //alert("数据量超过导出的范围50万，请从数据库中导出！");
						   		     alert(getJsLocaleMessage("wygl","wygl_common_text11"));
						   		<%}
					   		}else{
					   		%>
					   			//alert("无数据可导出！");
					   			alert(getJsLocaleMessage("wygl","wygl_common_text12"));
					   		<%
					   		}
					   		%>
					   	}				 
					});
				
				});
			
			$(function(){
			  $('#spisuncm').isSearchSelect({'width':'160','isInput':false,'zindex':0});
			  $('#type').isSearchSelect({'width':'160','isInput':false,'zindex':0});
			})
			
				function showback()
				{
				   var lgcorpcode =$("#lgcorpcode").val();
				   var lguserid =$("#lguserid").val();
				   location.href="wyq_historyRecord.htm?method=findallLfMttask&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid;
				}
				//关闭加载窗口的方法		
				function closelog(){
				    var lguserid =$("#lguserid").val();
		            $.post("<%=path%>/wyq_historyRecord.htm?method=checkResult",{lguserid:lguserid},function(result){
		        	    if(result != null && result =="over" )
		        	    {
		        	          $("#probar").dialog("close");
				              window.clearInterval(dd);
		        	    }
		        	});	
				}
				function modify(t)
				{
					$('#modify').dialog({
					autoOpen: false,
					width:250,
				    height:200
				});
				$("#msg").empty();
				$("#msg").text($(t).children("label").children("xmp").text());
				$('#modify').dialog('open');
				}
</script>
	</body>
</html>
