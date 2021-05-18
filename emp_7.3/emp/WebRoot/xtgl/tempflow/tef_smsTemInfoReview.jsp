<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.tempflow.vo.LfFlowRecordTemplateVo"%>
<%@page import="com.montnets.emp.entity.approveflow.LfFlowRecord"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.entity.system.LfPageField"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	List<LfFlowRecord> recordList = (List<LfFlowRecord>)request.getAttribute("recordList");
	//Long dsflag = mt.getDsflag()==null?3l:mt.getDsflag();


	@ SuppressWarnings("unchecked")
	LinkedHashMap<Long,String> usernameMap = (LinkedHashMap<Long,String>) request.getAttribute("usernameMap");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<Long,String> tempMap = (LinkedHashMap<Long,String>) request.getAttribute("tempMap");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String,String>) request.getAttribute("conditionMap");
	String dsflag = conditionMap.get("reviewType");
	String infoType = conditionMap.get("infoType");
	String tmName = conditionMap.get("RContent&like");
	
	String findResult= (String)request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	@ SuppressWarnings("unchecked")
	//状态报告类
	List<LfPageField> pagefileds=(List<LfPageField>)request.getAttribute("pagefileds");
%>
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
        <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/tef_smsTemInfoReview.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>

	</head>
	<body id="tef_smsTemInfoReview">
	<input type="hidden" id="ipathUrl" value="<%=commonPath %>"/>
	<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
	<div id="smsExamine" title="<emp:message key='xtgl_spgl_mbsp_dxmbsp' defVal='短信模板审批' fileName='xtgl'/>" class="smsExamine">
	</div>
	<input type="hidden" id="fmmsId" value=""/>
	<div id="mmsExamine" title="<emp:message key='xtgl_spgl_mbsp_cxmbsp' defVal='彩信模板审批' fileName='xtgl'/>" class="mmsExamine">
	
			<div id="mobileDiv" class="mobileDiv">
				<div id="mobile" class="mobile"  >
				<center>
				
				<div id="pers" class='pers'>
				
				<div id="showtime" class="showtime"></div>
				
				<div id='chart' class='chart'>
				</div>
				</div>
				</center>
				
				<div id="screen" class="screen">
				</div>
				
				<center>
				<table>
				<tr>
				  <td>
				     <label id="pointer" class="align_bottom"></label>
				     <label id="nextpage" class="align_bottom"></label>
				  </td>
				</tr>
				<tr align="center">
					<td>
					   <label id="currentpage" class="align_bottom"></label>
					</td>
				</tr>
				</table>				
				</center>
				</div>
			</div>
	<table id="shenpi" class="shenpi">
	  <tr>
	  		<td colspan="2"><emp:message key="xtgl_spgl_xxsp_spyj_mh" defVal="审批意见：" fileName="xtgl"/>
	  		</td>
	  </tr>
	  <tr>
			<td colspan="2">
			<textarea  id="cont" class="input_bd cont"></textarea></td>
	  </tr>
	  <tr><td colspan="2" class="showParam" id="showParam"><div id="inputParamCnt1"></div></td></tr>	
	  <tr>
	  		<td><input type="button" class="btnClass5 mr23" id="oks" value="<emp:message key='xtgl_spgl_xxsp_ty' defVal='同意' fileName='xtgl'/>" onclick="javascript:review(1,4)"/>
	  		</td>
			<td>
			<input type="button"  class="btnClass6" id="rjs"  value="<emp:message key='xtgl_spgl_xxsp_jj' defVal='拒绝' fileName='xtgl'/>" onclick="javascript:review(2,4)"/>
			</td>
	  </tr>
	  
	</table>
	</div>
	
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<form name="pageForm" action="<%=path %>/tef_smsTemInfoReview.htm?method=find" method="post"
					id="pageForm">
					<div id="ir_smsTRparams" class="hidden"></div>
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
					</div>
					<div id="condition">
						<table>
							<tr>
								<td>
									<span><emp:message key="xtgl_spgl_mbsp_mbmc_mh" defVal="模板名称：" fileName="xtgl"/></span>
								</td>
								<td>
									<input type="text" name="tmName" 
										id="tmName" value="<%=null!=tmName?tmName:"" %>" class="tmName"  />
								</td>
								
									
							<td>
									<span><emp:message key="xtgl_spgl_mbsp_mblx_mh" defVal="模板类型：" fileName="xtgl"/></span>
								</td>
								<td>
									<select name="dsflag" id="dsflag" class="dsflag" >
										<option value="">
											<emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/>
										</option>
										<option value="0" <%="0".equals(dsflag)?"selected":"" %>>
											<emp:message key="xtgl_spgl_mbsp_tyjtmk" defVal="通用静态模块" fileName="xtgl"/>
										</option>
										<option value="1" <%="1".equals(dsflag)?"selected":"" %>>
											<emp:message key="xtgl_spgl_mbsp_tydtmk" defVal="通用动态模块" fileName="xtgl"/>
										</option>
										<option value="2" <%="2".equals(dsflag)?"selected":"" %>>
											<emp:message key="xtgl_spgl_mbsp_znzqmk" defVal="智能抓取模块" fileName="xtgl"/>
										</option>
										<option value="3" <%="3".equals(dsflag)?"selected":"" %>>
											<emp:message key="xtgl_spgl_mbsp_ydcwmk" defVal="移动财务模块" fileName="xtgl"/>
										</option>
									</select>
								</td>
								<td class="tdSer">
									             <center><a id="search"></a></center>
								</td>
							</tr>
							<tr>
							<td><span><emp:message key="xtgl_spgl_mbsp_xxlx_mh" defVal="信息类型：" fileName="xtgl"/></span></td>
									<td>
										<select id="infoType" name="infoType" class="infoType">
											<option value="" ><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/></option>
											
											<option value="3" <%="3".equals(infoType)?"selected":"" %>><emp:message key="xtgl_spgl_xxsp_dx" defVal="短信" fileName="xtgl"/></option>
											<option value="4" <%="4".equals(infoType)?"selected":"" %>><emp:message key="xtgl_spgl_xxsp_cx" defVal="彩信" fileName="xtgl"/></option>	
											
											

								</select>
									</td>
								<td>
									<%--<span>模板内容：</span> --%>
								</td>
								<td>
									<%--<input type="text" name="tmMsg" id="tmMsg"  value="<%=null!=mt.getTmMsg()?mt.getTmMsg():"" %>" style="width:180px" /> --%>
								</td>
							<td></td>
							</tr>
						</table>
					</div>
					<table id="content">
						<thead>
					  <tr>
										
										<th>
											<emp:message key="xtgl_spgl_mbsp_mbid" defVal="模板ID" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_spgl_mbsp_mbmc" defVal="模板名称" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_spgl_mbsp_mbnr" defVal="模板内容" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_spgl_mbsp_mblx" defVal="模板类型" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_spgl_shlcgl_cjr" defVal="创建人" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_spgl_mbsp_cjrq" defVal="创建日期" fileName="xtgl"/>
										</th>
										<th>
											<emp:message key="xtgl_spgl_xxsp_spjb" defVal="审批级别" fileName="xtgl"/>
										</th>
										<th><emp:message key="xtgl_spgl_mbsp_shzt" defVal="审核状态" fileName="xtgl"/></th>
										<th>
											<emp:message key="xtgl_spgl_shlcgl_cz" defVal="审批" fileName="xtgl"/>
										</th>
									</tr>
								</thead>
								<tbody>
								<%
								if (recordList != null && recordList.size() > 0)
								{
									for (LfFlowRecord record : recordList)
									{
								%>
									<tr>
										
										<td>
											<%=record.getMtId() %>
										</td>
										<td class="textalign">
											<%=record.getRContent().replaceAll("<","&lt;").replaceAll(">","&gt;") %>
										</td>
										<td class="textalign">
										<div class="mbysc_div">
										<%
										String msg = tempMap.get(record.getMtId());
										String st = "";
										if(msg == null)
										 {
											 out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_mbysc",request));
										 }
										else if(record.getInfoType()-4==0){
											msg = msg.replaceAll("#[pP]_([1-9][0-9]*)#","{#"+MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_cs",request)+"$1#}");
											%>
												<a onclick="javascript:modify('<%=msg%>',0,<%=record.getReviewType() %>)">
										  			<label class="msg_label"><xmp><%=msg%></xmp></label>
										 			<xmp ><emp:message key="xtgl_spgl_xxsp_ck" defVal="查看" fileName="xtgl"/></xmp>
										  		</a>
											 <%}else{
												 msg = msg.replaceAll("#[pP]_([1-9][0-9]*)#","{#"+MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_cs",request)+"$1#}");
													if( msg.length()>20)
													{
														st = msg.substring(0,20)+"...";
													}else
													{
														st = msg;
													}
											}
										%>
										<a onclick="javascript:modify(this,1,0)">
										  			<label class="msg_label"><xmp><%=msg==null?"":msg%></xmp></label>
										 			<xmp ><%=st==null?"":st%></xmp>
										  		</a>
										</div>											
										</td>
										<td class="ztalign">
												<%
												String type ="";
												if(record.getInfoType()-3==0){
													type="<font color='#159800'>"+MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_dx",request)+"-";
												}
												if(record.getInfoType()-4==0){
													type="<font color='#f1913c'>"+MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_cx",request)+"-";
												}
												if(!"".equals(type)){
													if(record.getReviewType()-1==0)
													{
														out.print(type+MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_tydtmk",request)+"</font>");
													}else if(record.getReviewType()-0==0)
													{
														out.print(type+MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_tyjtmk",request)+"</font>");
													}else if(record.getReviewType()-2==0)
													{
														out.print(type+MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_znzqmk",request)+"</font>");
													}else if(record.getReviewType()-3==0)
													{
														out.print(type+MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_ydcwmk",request)+"</font>");
													}
												}
											%>
										</td>
										<td>
											<%=usernameMap.get(record.getProUserCode())==null?"":usernameMap.get(record.getProUserCode()) %>
										</td>
										<td>
											<%=record.getSubmitTime()==null?"":df.format(record.getSubmitTime()) %>
										</td>
										<td><%=record.getRLevel() %>/<%=record.getRLevelAmount() %></td>
										<td>
										<%
										if(msg == null)
										 {
											 out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_ysc",request));
										 }else
										 {
										//审批状态（-1代表未审核；0代表无需审核；1代表审核通过；2代表审核不通过）
								    		int state=record.getRState();
									    	if(state == -1){
									    		if(record.getIsComplete() == 1){
									    			out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_spwc",request));
									    		}else{
									    			out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_wsp",request));	
									    		}
									    	}else if(state==1)
									    	{
									    		out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_sptg",request));	
									    	}else if(state==2)
									    	{
												out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_spbtg",request));				    		
									    	}else if(state==0){
									    		out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_wxsp",request));		
									    	}
										 }
								    %> 
										</td>
										<td>
										<%if(msg!=null){ %>
											<a onclick="javascript:examineMsg('<%=record.getFrId()%>')">
												<img alt="<emp:message key="xtgl_spgl_xxsp_sp" defVal="审批" fileName="xtgl"/>" src="<%=commonPath%>/common/img/find.png">
													<%=record.getRState()==-1 && record.getIsComplete() == 2?MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_sp",request):MessageUtils.extractMessage("xtgl","xtgl_spgl_xxsp_ck",request) %>
											</a>
											<%}else{out.print("-");} %>
										</td>
									</tr>
									<%
									}
								}else{
									%>
									<tr><td colspan="9"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/></td></tr>
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
			<%-- foot结束 --%>
		</div>
		<div id="modify" title="<emp:message key='xtgl_spgl_mbsp_mbnr' defVal='模板内容' fileName='xtgl'/>"  class="modify">
			<table class="modify_table">
				<thead>
					<tr class="msg_tr">
						<td>
							<span><label id="msg" class="msg"></label></span>
							 
						</td>
						 
					</tr>
				   <tr class="msg_down_tr">
						<td>
						</td>
						</tr>
					 
				</thead>
			</table>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/examinetplmgr.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript">
			$(document).ready(function() {
			    getLoginInfo("#ir_smsTRparams");
				var findresult="<%=findResult%>";
			    if(findresult=="-1")
			    {
			       alert(getJsLocaleMessage("xtgl","xtgl_spgl_mbsp_98"));	
			       return;			       
			    }
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
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,
					<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				$('#search').click(function(){submitForm();});
			    var h = 380;
				if (navigator.appName == "Netscape")
			    {
			    	h = 370;
			    }
				$("#smsExamine").dialog({
					autoOpen: false,
					height:h,
					width: 400,
					resizable: false,
					modal: true
				});
				var he = 500;
				if (navigator.appName == "Netscape")
			    {
			    	he = 520;
			    }
				$("#mmsExamine").dialog({
					autoOpen: false,
					height:he,
					width: 480,
					modal: true,
					resizable:false,
					open:function(){
				        $("#userId").css("visibility","hidden");
					}
				});
				$("#mmsExamine").dialog({
				    close: function(event, ui){
				    $("#userId").css("visibility","visible");
				    $("#screen").empty();
				    inits();
				    }
				});
			} );
	</script>
	</body>
</html>
