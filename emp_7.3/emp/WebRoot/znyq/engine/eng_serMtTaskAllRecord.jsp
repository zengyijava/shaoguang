<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@page import="com.montnets.emp.common.vo.SendedMttaskVo"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.sms.LfMttask" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%

	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat sdf_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//LfSysuser sysuser=((LfSysuser)session.getAttribute("loginSysuser"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");
			
	@ SuppressWarnings("unchecked")
	List<SendedMttaskVo> mtList =(List<SendedMttaskVo>) request.getAttribute("mtList");
	
	LfMttask Lfmttask = (LfMttask)session.getAttribute("serMttask");
	if(Lfmttask == null)
	{
		Lfmttask = new LfMttask();
	}
	String title = Lfmttask.getTitle();
	String message = Lfmttask.getMsg();
	if(Lfmttask.getBmtType() == null || Lfmttask.getBmtType() == 3)
	{
		//message = "详见明细";
		message =MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_xjmx",request);
	}
	String sendtime = Lfmttask.getTimerTime()==null?"":sdf_yyyyMMddHHmmss.format(Lfmttask.getTimerTime());
	String sendinfo;
	if(Lfmttask.getIcount2() == null)
	{
		//sendinfo="待汇总";
		sendinfo=MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_dhz",request);
	}
	else
	{
		//接收失败总数
		String r_count=String.valueOf((Lfmttask.getRfail2()==null?"0":Lfmttask.getRfail2()));
		//提交失败总数
		long fail_count = Lfmttask.getFaiCount()==null?0l:Long.parseLong(Lfmttask.getFaiCount());
		long icount = Lfmttask.getIcount2()==null?0l:Long.parseLong(Lfmttask.getIcount2());
		//发送成功数
		long suc = icount - fail_count;
		//sendinfo ="发送总数为"+icount+"条，其中发送成功数为"+suc+"条，提交失败数为"+fail_count+"条，接收失败数为"+r_count+"条。";
		sendinfo =MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_fszsw",request)+icount+MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_tqzfscgsw",request)
		+suc+MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_ttjsbsw",request)+fail_count+MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_tjssbsw",request)
		+r_count+MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_t",request);
	}
	String mtid = request.getParameter("mtid");
	String type = request.getParameter("type");
	String phone = request.getParameter("phone");
	String spisuncm = request.getParameter("spisuncm");
	String taskId = Lfmttask.getTaskId().toString();

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");

	String menuCode = titleMap.get("serMtTask");
	menuCode=menuCode==null?"0-0-0":menuCode;
	CommonVariables  CV = new CommonVariables();
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	boolean isFirstEnter = (Boolean)request.getAttribute("isFirstEnter");
	String loadPageUrl = path+"/eng_serMtTask.htm?method=getDetailPageInfo";
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >

		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
	</head>
	<body class="eng_serMtTaskAllRecord" id="eng_serMtTaskAllRecord">
		<div id="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode,"发送详情查看") %>--%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("znyq","znyq_ywgl_xxywjl_fsxqck",request)) %>
				<div class="rContent">
					<%              		
						if(btnMap.get(menuCode+"-0")!=null)                       		
						{                        	
					%>
					<div class="titletop">
						<table class="titletop_table">
								<tr>
								    <td class="titletop_td">
								           <emp:message key="znyq_ywgl_xxywjl_fsxqck" defVal="发送详情查看" fileName="znyq"></emp:message>     
						        	</td>
									<td align="right">
										<span class="titletop_font"    onclick="javascript:showback()">&larr;&nbsp;<emp:message key="znyq_ywgl_xxywjl_fhsyj" defVal="返回上一级" fileName="znyq"></emp:message></span>
									</td>
								</tr>
							</table>
					   </div>
						<div id="u_o_c_explain" class="div_bg">
							<p>
								<emp:message key="znyq_ywgl_xxywjl_jbzt" defVal="基本信息" fileName="znyq"></emp:message>
							</p>
                            <table>
                              <tr>
                                 <td><b><emp:message key="znyq_ywgl_xxywjl_zt_mh" defVal="主题：" fileName="znyq"></emp:message></b></td>
                              </tr>
                              <tr>
                                 <td><%=title==null?"":(title.replaceAll("<","&lt;").replaceAll(">","&gt;")) %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="znyq_ywgl_xxywjl_nr_mh" defVal="内容：" fileName="znyq"></emp:message></b></td>
                              </tr>
                              <tr>
                                 <td class="wordWrap"><%=message==null||"".equals(message)?"":message.replaceAll("<","&lt;").replaceAll(">","&gt;") %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="znyq_ywgl_xxywjl_fssj_mh" defVal="发送时间：" fileName="znyq"></emp:message></b></td>
                              </tr>
                              <tr>
                                 <td><%=sendtime %></td>
                              </tr>
                              <tr>
                                 <td><b><emp:message key="znyq_ywgl_xxywjl_fsqk_mh" defVal="发送情况：" fileName="znyq"></emp:message></b></td>
                              </tr>
                              <tr>
                                <td><%=sendinfo %>
                                <br/>
                                <%--<font class="zhu">注：以上数据<%=StaticValue.CORPTYPE==0?"6小时":"每晚"%>汇总一次,可能与下面的实时数据有一定误差。</font> --%>
                                <font class="zhu"><emp:message key="znyq_ywgl_xxywjl_zyssj" defVal="注：以上数据" fileName="znyq"></emp:message><%=StaticValue.getCORPTYPE() ==0?"6小时":"每晚"%><emp:message key="znyq_ywgl_xxywjl_hzyc" defVal="汇总一次,可能与下面的实时数据有一定误差。" fileName="znyq"></emp:message></font>
                                </td>
                              </tr>
                            </table>
						</div>
						<form name="pageForm" action="eng_serMtTask.htm?method=findAllSendInfo" method="post">
						<input type="hidden" name="taskId" id="taskId" value="<%=taskId %>">
						<div class="buttons">
						  <div id="toggleDiv">								
						  </div> 
						  <span id="backgo" class="right mr5" onclick="javascript:showback()">&nbsp;<emp:message key="znyq_ywgl_common_btn_10" defVal="返回" fileName="znyq"></emp:message></span>
						  <a id="exportCondition"><emp:message key="znyq_ywgl_common_btn_18" defVal="导出" fileName="znyq"></emp:message></a>				  					 			          
						</div>
						<div id="condition">
						 <input type="hidden" id="mtid" value="<%=mtid %>" name="mtid">
						 <table>
						    <tr>
						      <td><emp:message key="znyq_ywgl_xxywjl_yys_mh" defVal="运营商：" fileName="znyq"></emp:message></td>
						      <td>
						           <select  id="spisuncm" name="spisuncm" class="commonWidth1">
										<option value=""><emp:message key="znyq_ywgl_common_text_16" defVal="全部" fileName="znyq"></emp:message></option>
										<option value="0" <%="0".equals(spisuncm)?"selected":"" %>><emp:message key="znyq_ywgl_xxywjl_yd" defVal="移动" fileName="znyq"></emp:message></option>
										<option value="1" <%="1".equals(spisuncm)?"selected":"" %> ><emp:message key="znyq_ywgl_xxywjl_lt" defVal="联通" fileName="znyq"></emp:message></option>
										<option value="21" <%="21".equals(spisuncm)?"selected":"" %>><emp:message key="znyq_ywgl_xxywjl_dx" defVal="电信" fileName="znyq"></emp:message></option>
										<option value="5" <%="5".equals(spisuncm)?"selected":"" %>><emp:message key="znyq_ywgl_xxywjl_gw" defVal="国外" fileName="znyq"></emp:message></option>
									</select>
						      </td>
						      <td><emp:message key="znyq_ywgl_xxywjl_sjhm_mh" defVal="手机号码：" fileName="znyq"></emp:message></td>
						      <td>
						         <input type="text" value='<%=phone==null?"":phone %>' id="phone" name="phone" maxlength="21" onkeyup="javascript:phoneInputCtrl($(this))"/>
						      </td>
						      <td><emp:message key="znyq_ywgl_xxywjl_zt_mh2" defVal="状态：" fileName="znyq"></emp:message></td>
						      <td>
						          <select  id="type" name="type" class="commonWidth1">
										<option value="1" <%="1".equals(type)?"selected":"" %>><emp:message key="znyq_ywgl_xxywjl_qbhm" defVal="全部号码" fileName="znyq"></emp:message></option>
										<%--
										<option value="5" <%="5".equals(type)?"selected":"" %> >发送成功</option>
										<option value="6" <%="6".equals(type)?"selected":"" %> >状态未返</option>										
										--%>
										<option value="2" <%="2".equals(type)?"selected":"" %> ><emp:message key="znyq_ywgl_xxywjl_jssb" defVal="接收失败" fileName="znyq"></emp:message></option>
										<option value="3" <%="3".equals(type)?"selected":"" %>><emp:message key="znyq_ywgl_xxywjl_tjsb" defVal="提交失败" fileName="znyq"></emp:message></option>
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
										<emp:message key="znyq_ywgl_xxywjl_xh" defVal="序号" fileName="znyq"></emp:message>
									</th>
									<th>
									    <emp:message key="znyq_ywgl_xxywjl_yys" defVal="运营商" fileName="znyq"></emp:message>
									</th>
									<th>
										<emp:message key="znyq_ywgl_xxywjl_sjh" defVal="手机号" fileName="znyq"></emp:message>
									</th>
									<th>
										<emp:message key="znyq_ywgl_xxywjl_nr" defVal="内容" fileName="znyq"></emp:message>
									</th>
									<th>
										<emp:message key="znyq_ywgl_xxywjl_ft" defVal="分条" fileName="znyq"></emp:message>
									</th>
									<th>
										<emp:message key="znyq_ywgl_common_zt" defVal="状态" fileName="znyq"></emp:message>
									</th>
								</tr>
							</thead>
							<%
							if(type.equals("0"))
							{
							%>
							<tr><td colspan="6" align="center"><emp:message key="znyq_ywgl_xxywjl_qdjanhqsj" defVal="请点击按钮获取数据" fileName="znyq"></emp:message></td></tr>
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
								<td>
				  					<%
									if(vo.getUnicom()-0==0){%>
									<emp:message key="znyq_ywgl_xxywjl_yd" defVal="移动" fileName="znyq"></emp:message>
									<%}else if(vo.getUnicom()-1==0){%>
									<emp:message key="znyq_ywgl_xxywjl_lt" defVal="联通" fileName="znyq"></emp:message>
									<%}else if(vo.getUnicom()-21==0){ %>
									<emp:message key="znyq_ywgl_xxywjl_dx" defVal="电信" fileName="znyq"></emp:message>
									<%}else if(vo.getUnicom()-5==0){ %>
									<emp:message key="znyq_ywgl_xxywjl_gw" defVal="国外" fileName="znyq"></emp:message>
									<%} %>
								</td>
								<td>
									<%
									   String phones = CV.replacePhoneNumber(btnMap,vo.getPhone());
									   out.print(phones);									   
									 %>
								</td>
								<td class="textalign">									
									<%=vo.getMessage()==null||"".equals(vo.getMessage())?"":vo.getMessage().replaceAll("<","&lt;").replaceAll(">","&gt;") %>
								</td>
								<td>
									<%=vo.getPknumber() %>/<%=vo.getPktotal() %>
								</td>
								<td class="textalign">
								<%if(vo.getErrorcode() != null){ %>
								    <%if(vo.getErrorcode().equals("DELIVRD")||vo.getErrorcode().trim().equals("0")){  %>
								         <label><emp:message key="znyq_ywgl_xxywjl_fscg" defVal="发送成功" fileName="znyq"></emp:message></label>  
								    <%}else if(vo.getErrorcode().toString().trim().length()>0){%>
								         <label><emp:message key="znyq_ywgl_common_text_12" defVal="失败" fileName="znyq"></emp:message>[<%=vo.getErrorcode() %>]</label>
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
							<tr><td colspan="6" align="center"><emp:message key="znyq_ywgl_common_text_1" defVal="无记录" fileName="znyq"></emp:message></td></tr>
							<%} %>
							<tfoot>
							<tr >
								<td colspan="6">
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
						
					 <div id="probar">
							<p >
								<emp:message key="znyq_ywgl_xxywjl_qnxdd" defVal="请耐心等待....." fileName="znyq"></emp:message>
							</p>
							<div id="shows">
								<img src="<%=commonPath%>/common/img/loader.gif" />
							</div>							
					</div>					
					</div>
			<%--end rContent--%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
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
			initPageSyn(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,<%=pageInfo.getNeedNewData() %>, <%=isFirstEnter %>, "<%=loadPageUrl %>");
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
				 	if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_qdydcsjde")))
				 	{
				 	var a=getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_qdydcsjde");
				   		<%
				   		if(mtList!=null && mtList.size()>0 ){
				   		%>  
				   		    var IsexportAll = true;
				   		    //if(confirm("是否只需导出手机号(点“确定”则只导出手机号字段，点“取消”则导出所有页面显示字段)?"))
				   		    
				 		var b=getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_sfzxdcsjh");
				   		    if(confirm(getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_sfzxdcsjh")))
				   		    {
				   		        IsexportAll = false;
				   		    }  
	   	
				   		<%		     
				   		  	if(pageInfo.getTotalRec()<=500000){
				   		%>
				   		
				   			$.ajax({
								type: "POST",
								url: "eng_serMtTask.htm?method=ReportMtSerDetailExcel",
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
								beforeSend: function(){
									page_loading();
								},
				                complete:function () {
									page_complete();
				                },
								success: function(result){
									if(result=='true'){		
										download_href("eng_serMtTask.htm?method=downloadFile&down_session=ReportMtSerDetailExcel");	                    	
				                    }else{
				                        //alert('导出失败！');
				                        alert(getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_dcsb"));
				                    }
					   			}
							});
				   		
					   		   
					   		<%}else{
					   		%>				   		    
					   		     //alert("数据量超过导出的范围50万，请从数据库中导出！");
					   		     alert(getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_sjlcgdcdfw50w"));
					   		<%}
				   		}else{
				   		%>
				   			//alert("无数据可导出！");
				   			alert(getJsLocaleMessage("znyq","znyq_ywgl_xxywjl_wsjkdc"));
				   		<%
				   		}
				   		%>
				   	}				 
				});
			
			});
			
			function showback()
			{
			   var lgcorpcode =$("#lgcorpcode").val();
			   var lguserid =$("#lguserid").val();
			   location.href="eng_serMtTask.htm?method=find&isback=1&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid;
			}
			//关闭加载窗口的方法		
			function closelog(){
			    var lguserid =$("#lguserid").val();
	            $.post("<%=path%>/eng_serMtTask.htm?method=checkResult",{lguserid:lguserid},function(result){
	        	    if(result != null && result =="over" )
	        	    {
	        	          $("#probar").dialog("close");
			              window.clearInterval(dd);
	        	    }
	        	});	
			}
</script>
	</body>
</html>
