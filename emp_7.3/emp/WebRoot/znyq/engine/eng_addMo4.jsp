<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.engine.LfProcess"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@ page import="com.montnets.emp.entity.pasroute.LfSpDepBind" %>
<%@ page import="com.montnets.emp.entity.pasgroup.Userdata" %>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.entity.engine.LfService" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@ page import="com.montnets.emp.entity.engine.LfProCon" %>
<%@ page import="com.montnets.emp.entity.engine.LfReply" %>
<%@page import="com.montnets.emp.entity.datasource.LfDBConnect"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

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
	List<LfProcess> proList=(List<LfProcess>)request.getAttribute("proList");
	
	String serId = request.getParameter("serId");
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("moService");
	menuCode = menuCode==null?"0-0-0":menuCode;
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
	@ SuppressWarnings("unchecked")
	List<LfDBConnect> dbList =(List<LfDBConnect>)request.getAttribute("dbList");
	
	@ SuppressWarnings("unchecked")
	List<LfTemplate> tmpList=(List<LfTemplate>)request.getAttribute("tmpList");
	
	Long prId =Long.valueOf(request.getParameter("prId"));
	
	int tableSize = 0;
	if(request.getAttribute("tableSize") != null)
	{
		tableSize = Integer.parseInt(request.getAttribute("tableSize").toString());
	}
	
	@ SuppressWarnings("unchecked")
	List<LfProCon> proConList = (List<LfProCon>)request.getAttribute("proConList");
	
	LfReply reply = (LfReply)request.getAttribute("reply");
	Long tempId = (Long)request.getAttribute("tempId");
	if(reply==null)
	{
		reply=new LfReply();
		reply.setMsgLoopId(0l);
	}
	@ SuppressWarnings("unchecked")
	List<LfTemplate> temList = (List<LfTemplate>)request.getAttribute("temList");
	
	
	StringBuffer str=new StringBuffer("<option value=''></option>");
	for(int i=0;i<proList.size();i++)
	{
		LfProcess process=proList.get(i);
		str.append("<option  value='"+process.getPrId()+"'>"+process.getPrName().replace("<","&lt;").replace(">","&gt;")+"</option>");
	}
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/floating.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link href="<%=commonPath %>/common/css/params.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
<%-- 		<style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		#detail_Info table {
	    width: 660px;
	    padding-left: 5px;
	    padding-right: 5px;
		}
		<%}%>
		
		#detail_Info table {
	    width: 660px;
	    padding-left: 5px;
	    padding-right: 5px;
		}
		
		.showParams li{
		width: 65px;
		}
		
		.itemDiv span {
		    width: 85px;
		}
		
		.itemDiv {
		    width: 730px;
		}
		</style>
		 --%>
		
		
	</head>
	<body onload="delTable(-1)" id="eng_addMo4" class="eng_addMo4">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
		<div id="container" class="container">
			<div id="rContent" class="rContent">
			<form action="eng_moService.htm?method=upTemp" method="post" id="reply">
			<input type="hidden" value="<%=tableSize %>" id="tableSize"/>
			<input type="hidden" name="workType" id="workType" value="waterLine"/>
			<input type="hidden" name="serId" id="serId" value="<%=serId %>"/>
			<input type="hidden" name="prId" id="prId" value="<%=prId %>"/>
				<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
				<div class="firstDiv">
					<table id="location" class="linebgimg3">
						<tr><td><b>1.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_xjsxyw" defVal="新建上行业务" fileName="znyq"></emp:message></td><td>
						<b>2.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_xjbz" defVal="新建步骤" fileName="znyq"></emp:message></td><td>
						<b>3.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_sdpzbz" defVal="手动配置步骤" fileName="znyq"></emp:message></td><td>
						<b>4.</b>&nbsp;<emp:message key="znyq_ywgl_sxywgl_wc" defVal="完成" fileName="znyq"></emp:message></td></tr>
					</table>
				</div>
				
				<div class="itemDiv title_bg div_bd first" >
				<b id="replyMessage"><emp:message key="znyq_ywgl_sxywgl_hfdx" defVal="回复短信" fileName="znyq"></emp:message></b>
				</div>
				
				<div class="itemDiv" id="firstItem">
				<span id="buzhou"><emp:message key="znyq_ywgl_sxywgl_bzmc_mh" defVal="步骤名称：" fileName="znyq"></emp:message></span>
				<table><tr><td>
				<select name="msgLoopId" id="msgLoopId" class="input_bd">
					<option value=" "><emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message></option>
						<%
						for(int i=0;i<proList.size();i++)
						{
							LfProcess process=proList.get(i);
						%>
							<option value="<%=process.getPrId() %>"
						<%
						if(reply.getMsgLoopId()-process.getPrId()==0)
						{
							out.print("selected=\"selected\"");
						}
						%>>
						
						<%=process.getPrName().replace("<","&lt;").replace(">","&gt;") %>(<emp:message key="znyq_ywgl_sxywgl_xh" defVal="序号" fileName="znyq"></emp:message>：<%=process.getPrNo()%>)
						</option>
						<%
						}
						%>
				</select>
				</td></tr></table>
				</div>
				
				
				<div class="itemDiv">
				<span id="contentTip"><emp:message key="znyq_ywgl_sxywgl_fsnr_mh" defVal="发送内容：" fileName="znyq"></emp:message></span>
				<table class="firstTable">
				<tr>
				<td class="firstTd">
				<select id="selectInfoBody" name="tempId" class="input_bd">
				<option class="firstOp" value=""><emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message></option>
						<%
						for(int t=0;t<temList.size();t++)
						{
						LfTemplate tem=temList.get(t);
						%>
						<option value="<%=tem.getTmid() %>" <% if(tem.getTmid().equals(tempId)){ %>selected="selected"<%} %>><%=tem.getTmName().replace("<","&lt;").replace(">","&gt;") %>(ID:<%=tem.getTmid() %>)</option>
						<%} %>
				</select>
				</td>
				<%if(btnMap.get(titleMap.get("smsTemplate")+"-1")!=null) { %>
				<td><a class="firstA" onclick="showAddSmsTmp(0)"><emp:message key="znyq_ywgl_sxywgl_xz" defVal="新增" fileName="znyq"></emp:message></a></td>
				<%} %>
				
				</tr>
				<tr>
				<td class="secondTd">
				
					
						<div class="paraContent div_bd">
						<div class="tit_panel div_bd">
							<a href="javascript:void(0)" class="para_cg" ><emp:message key="znyq_ywgl_sxywgl_gbcs" defVal="关闭参数" fileName="znyq"></emp:message></a>
							<%
							if(btnMap.get(titleMap.get("manger")+"-0")!=null){
							%>
							<a class="secondA" onclick="chooseNetTpl()"><emp:message key="znyq_ywgl_sxywgl_crwx" defVal="插入网讯" fileName="znyq"></emp:message></a>
							<%} %>
						</div>	
							<label id="showEditor">
								<textarea  id="msgMain" name="msgMain"  class="input_bd textarea_limit contents_textarea" 
								onKeydown="saveTextareaPos(this)"
								onKeyup="saveTextareaPos(this)"
								onmousedown="saveTextareaPos(this)"
								onmouseup="saveTextareaPos(this);"
								onfocus="saveTextareaPos(this)"
							  rows="5" ><%=reply.getMsgMain()!=null? reply.getMsgMain():""%></textarea>
							</label><br>
							<div id="fontFmTip"><emp:message key="znyq_ywgl_sxywgl_csgsw" defVal="参数格式为：“#P_1#”(如：我和#P_1#去#P_2#)" fileName="znyq"></emp:message></div>
							<input type="hidden" id="start" />
							<input type="hidden" id="end" />
						</div>
					
				
				</td><td ><font class="tipColor">*</font></td>
				</tr>
				</table>
				</div>
				
				<div class="itemDiv title_bg div_bd second">
				<b id="conditionTip"><emp:message key="znyq_ywgl_sxywgl_zxtj" defVal="执行条件" fileName="znyq"></emp:message></b>
				<div class="secondDiv">
					<a id="addCondition" onclick="addTable()"><emp:message key="znyq_ywgl_sxywgl_zjtj" defVal="增加条件" fileName="znyq"></emp:message></a>
				</div>
				</div>
				
				<div id="detail_Info" class="itemDiv div_bg">
				<%
				if(tableSize>0)
				{
					for(int i=0;i<tableSize;i++)
					{
						LfProCon proCon=proConList.get(i);
				%>
				<table id="tableIndex<%=i %>" class='div_bg'>
				<thead>
				<tr>
				<td>
				<emp:message key="znyq_ywgl_sxywgl_tjbds_mh" defVal="条件表达式：" fileName="znyq"></emp:message>
				<select  class="input_bd" id="firstSE" name="UsedPrId">
				<option value=""></option>
				<%
					Long prid=proCon.getUsedPrId();
					for(int j=0;j<proList.size();j++)
					{
						LfProcess process=proList.get(j);
				%>
						<option value="<%=process.getPrId() %>" <%=prid-process.getPrId()==0?"selected":"" %>>
						<%=process.getPrName().replace("<","&lt;").replace(">","&gt;") %>
						 </option>
				<%
					}
				%>
				</select>
				<input type="text"  class="input_bd" id="conExpress"  name="conExpress" value="<%=proCon.getConExpress() %>" maxlength="32" />
				</td>
				<td>
				<emp:message key="znyq_ywgl_sxywgl_czf_mh" defVal="操作符：" fileName="znyq"></emp:message>
				<select  class="input_bd" name="conOperate" id="conOperate">
					<option value="=">=</option>
					<option value="&lt;" <%=proCon.getConOperate().equals("<")?"selected":"" %>>&lt;</option>
					<option value="&gt;" <%=proCon.getConOperate().equals(">")?"selected":"" %>>&gt;</option>
					<option value="&lt;=" <%=proCon.getConOperate().equals("<=")?"selected":"" %>>&lt;=</option>
					<option value="&gt;=" <%=proCon.getConOperate().equals(">=")?"selected":"" %>>&gt;=</option>
					<option value="!="  <%=proCon.getConOperate().equals("!=")?"selected":"" %>>!=</option>
				</select>
				</td>
				<td>
				<emp:message key="znyq_ywgl_sxywgl_tjz_mh" defVal="条件值：" fileName="znyq"></emp:message><input  class="input_bd" id="conValue" width="80px" name="conValue" value="<%=proCon.getConValue()==null?"":proCon.getConValue() %>" maxlength="32" />
				</td>
				<td>
				<a onclick="delTable(<%=i %>)"><img src="<%=skin %>/images/x.png"/></a>
				</td>
				</tr>
				</thead>
				</table>
				<%
					}
				}
				%>
				</div>
				
				<div class="itemDiv">
				<table id="secondTable">
				<tr>
				<td class="fengexian">
					<div class="leftDiv"></div>
					<div class="conCent"> <emp:message key="znyq_ywgl_sxywgl_bbzywc" defVal="本步骤已完成" fileName="znyq"></emp:message> </div>
					<div class="rightDiv"></div>
				</td>
				</tr>
				</table>
				</div>
				
				<div class="itemDiv div_bg" id="thirdDiv">
				<table id="moreItem">
				<tr>
				<td class="thirdTd"><emp:message key="znyq_ywgl_sxywgl_jxcjbz_mh" defVal="继续创建步骤：" fileName="znyq"></emp:message></td>
				<td>
				<input name="morePro" onclick="javascript:$('#addBasicInfoBtn').val('<emp:message key="znyq_ywgl_common_btn_17" defVal="下一步" fileName="znyq"></emp:message>').addClass('indent_none').css({'padding-left':'14px'})" type="radio" value="1"/>&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_s" defVal="是" fileName="znyq"></emp:message>
				<input checked  onclick="javascript:$('#addBasicInfoBtn').val('<emp:message key="znyq_ywgl_sxywgl_wc" defVal="完成" fileName="znyq"></emp:message>').removeClass('indent_none').css({'padding-left':'0'})" style="margin-left:60px" name="morePro" type="radio" value="0"/>&nbsp;&nbsp;<emp:message key="znyq_ywgl_sxywgl_f" defVal="否" fileName="znyq"></emp:message>
				</td>
				</tr>
				</table>
				</div>
				
				<div class="fourDiv">
				<input type="button" id="addBasicInfoBtn" value="<emp:message key="znyq_ywgl_sxywgl_wc" defVal="完成" fileName="znyq"></emp:message>" class="btnClass5 mr23" onclick="submitForm()" />
				<input type="button"  name="previous" id="previous" value="<emp:message key="znyq_ywgl_common_btn_23" defVal="上一步" fileName="znyq"></emp:message>" class="btnClass6 indent_none"  onclick="javascript:goLast()"  />
				<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
				<br/>
				</div>
			</form>
			</div>
			<iframe id="ifr" class="ifr"></iframe>
			<div id="id2" class="remindMessage"></div>
			<div id="addSmsTmpDiv" title="<emp:message key="znyq_ywgl_sxywgl_dxmb" defVal="短信模板" fileName="znyq"></emp:message>">
				<iframe id="addSmsTmpFrame" name="addSmsTmpFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
			</div>
			<div id="tmplDiv" title="<emp:message key="znyq_ywgl_sxywgl_jtwxnrxz" defVal="静态网讯内容选择" fileName="znyq"></emp:message>">
				<iframe id="tempFrame" name="tempFrame" marginwidth="0" scrolling="no" frameborder="no"  ></iframe>
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
		<div class="clear"></div>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/moService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/textarea_limit.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script src="<%=commonPath %>/common/js/jquery.selection.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/param_cg2.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=iPath%>/js/insertpos.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_smsContentLength.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		
		<script type="text/javascript">
		<%--
		var operateStr="操作符：<select  class='input_bd' name=\"conOperate\">"+
		"<option value=\"=\">=</option>"+
		"<option value=\"&lt;\">&lt;</option>"+
		"<option value=\"&gt;\">&gt;</option>"+
		"<option value=\"&lt;=\">&lt;=</option>"+
		"<option value=\"&gt;=\">&gt;=</option>"+
		"<option value=\"!=\">!=</option></select>";
		var tipStr = "说明：编辑执行条件<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用于设置步骤的执行条件，仅当条件表达式的值与条件值比较的结果为真时，步骤才允许执行，多个执行条件的逻辑关系为与<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;一条执行条件记录包含条件表达式、操作符、条件值三个字段，分别说明如下：<br/>"+
						"条件表达式：提供下拉选择框以选择处理步骤，用于提供步骤输出结果；提供输入框以输入用户参数、系统变量、处理步骤输出等可选项，作为条件的表达式<br/>"+
						"条件表达式输入框说明：<br/>"+
					    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;可输入用户参数变量，如userpara_0或userpara_1，userpara_2，...，userpara_n，其中userpara_0表示上行信息的手机号，userpara_1表示第一个用户参数，如此类推<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;可以输入select步骤中要查询的列名或别名，例如select步骤（select name from xxx）,那么如果条件表达式输入框输入name，<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;则表示会对比字段name的所有记录的值是否符合条件值，只要select步骤的结果集有一个不符合条件，那么都不符合条件<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如果步骤下拉选择框所选的步骤类型为select步骤，则可以输入“resultcount”，该变量存放了select步骤执行后所得结果集的总行数<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如果步骤下拉选择框所选的步骤类型为delete/update/insert步骤，则可以输入“infcounts”，该变量存放了delete/update/insert步骤执行后的受影响行数<br/>"+
						"操作符：指定条件表达式与条件值之间的关系，如=(相等)，&lt;(小于)，&gt;(大于)，!=(不等于)等；"+
						"条件值：用于与条件表达式进行比较的值。";
		var tipStr2 = "选择用于提供动态短信模板参数值的select步骤处理结果";
		var tipStr3 = "回复短信设置用来定义发送给用户的短信的具体内容";
		var tipStr4 = "发送内容：选择短信模板，可以是动态模板或静态模板，如果是动态模板，则需要选择步骤名称下拉框的值<br/>"+
					  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;动态模板由短信内容和插入符组成，例如：你好，#P_1#先生/小姐。其中#P_1#为插入符，用于把动态结果插入到短信内容中，<br/>"+
					  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;它代表所选select类型步骤执行结果集第一列的值，插入符也可以是#P_2#，代表第二列的值，如此类推"
					  ;
					  --%>
					  
					  
		var operateStr=getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czf_mh")+"<select  class='input_bd' name=\"conOperate\">"+
		"<option value=\"=\">=</option>"+
		"<option value=\"&lt;\">&lt;</option>"+
		"<option value=\"&gt;\">&gt;</option>"+
		"<option value=\"&lt;=\">&lt;=</option>"+
		"<option value=\"&gt;=\">&gt;=</option>"+
		"<option value=\"!=\">!=</option></select>";
		var tipStr = getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_smbjzxtj")+"<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_yyszbzdzxtj")+"<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_ytzxtjjlbhtjbds")+"<br/>"+
						getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tjbdstgxlxzkyxzclbz")+"<br/>"+
						getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tjbdssrksm")+"<br/>"+
					    "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_ksryhcsbl")+"<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_kysrselectbzzycxdlm")+"<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_zbshdbzd")+"<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_rgbzxlxzksxdbzlxwselect")+"<br/>"+
						"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_rgbzxlxzksxdbzlxwdelete")+"<br/>"+
						getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czfzdtjbds")+
						getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tjzyyytjbds");
		var tipStr2 = getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_xzyytgdtdxmb");
		var tipStr3 = getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_hfdxsz");
		var tipStr4 = getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_fsnrxzdxmb")+"<br/>"+
					  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_dtmbydxnr")+"<br/>"+
					  "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tdbsxselect")
					  ;
		var expressStr;
		$(document).ready(function() {
			$('.para_cg').gotoParam({'width':369,'textarea':'#msgMain'});
			floatingRemind("conditionTip",tipStr);
			floatingRemind("replyMessage",tipStr3);
			floatingRemind("buzhou",tipStr2);
			floatingRemind("contentTip",tipStr4);
			//expressStr="条件表达式：<select  class='input_bd' style='width:100px' name=\"UsedPrId\">"+"<%=str %>"+
			expressStr=getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tjbds_mh")+"<select  class='input_bd' style='width:100px' name=\"UsedPrId\">"+"<%=str %>"+
   			"</select><input style='margin-left:10px;height:22px' class='input_bd' name=\"conExpress\" id=\"conExpress\" size=\"8\" maxlength='32' value=\"\"/>";
			getLoginInfo("#hiddenValueDiv");
			getSmsContentMaxLen('<%=serId %>');
			$("#selectInfoBody").change(choiceInfoBody);  

			$("#detail_Info img").hover(
					  function(){
						  $(this).attr("src","<%=skin %>/images/x.png");
					  },
					  function(){
						  $(this).attr("src","<%=skin %>/images/x2.png");
					  }
			);
			
			$("#tmplDiv").dialog({
				autoOpen: false,
				height:520,
				width: 690,
				resizable:false,
				modal: true
			});
			
		});
		 function submitForm(){
			 
			  var msgBody = $.trim($("#msgMain").val());       //获取信息体文本域的值
              var regStr =/#W_(\d+)#/g;
              if(msgBody == "" ){                //判断信息体
                   //alert("发送内容不能为空，请输入发送内容！");
                   alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_fsnrbnwk"));
                   return false;
              }
              if(msgBody.length > smsContentMaxLen)
				{
					//alert("发送内容长度大于短信最大长度限制，最大长度限制为："+smsContentMaxLen);
					alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_fsnrcddydxzdcdxz")+smsContentMaxLen);
					return false;
				}
               if(smsContentMaxLen == 700)
				{
					if(!checkSmsContentLen(msgBody,smsContentMaxLen))
					{
						return false;
					}
				}
              if(regStr.test(msgBody) && msgBody.match(regStr).length > 1){
            	   //插入且插入了多个网讯，则不允许
            	   //alert("发送内容插入了多个网讯，只能插入一个网讯！");
            	   alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_fsnrcrldgwx"));
            	   return false;
              }
			
            var repedMsg = getContentValX($("#msgMain"));
			$("#msgMain").val(repedMsg);
			
		    $('#reply').attr("action",$('#reply').attr("action")+"&lgcorpcode="+$("#lgcorpcode").val());
		    $('#reply').submit();
		 }

		 function getRepMsg(msg){
			var reg=/#p_(.*?)#/gi;
			msg=msg.replace(reg,replacerChange);
			return msg;
		}
		 
		 function choiceInfoBody(){
	        	var pathUrl = $("#pathUrl").val();
	                $.post(pathUrl+"/tem_smsTemplate.htm?method=getTmMsg",
	            		{tmId:$('#selectInfoBody').val()},
	            		function(result)
	            		{
	            			$("#msgMain").val(getRepMsg(result));
	            		}
	            	);             //获取信息体的值
	            }

		 function delTable(size){
	         $("#detail_Info").find("> table").each(
				function(index){
					if(this.id==("tableIndex"+size))
					{
						$(this).remove();
					}
				}
	     	);
	         if( $("#detail_Info").find("> table").length==0)
	         {
	      	   var tableStr="<table  class='div_bg' id='tableIndex0'><thead><tr>"+
	            	"<td>"+expressStr+"</td><td>"+operateStr+"</td>"+
	             	//"<td>条件值：<input class='input_bd' name=\"conValue\" maxlength='32' value=\"\"/></td>"+
	             	"<td>"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tjz_mh")+"<input class='input_bd' name=\"conValue\" maxlength='32' value=\"\"/></td>"+
	             	"<td><a onclick=\"delTable(0)\"><img src=\"<%=skin %>/images/x.png\"/></a></td></tr></thead></table>";
	             	"</tr></thead></table>";
	            $('#detail_Info').append(tableStr);  
	         }
	     }
	     
	     function addTable(){
	         var tableSize=$('#tableSize').val();
	         tableSize=tableSize-0+1;
	         var tableStr="<table  class='div_bg' id='tableIndex"+tableSize+"'><thead><tr>"+
	             	"<td>"+expressStr+"</td><td>"+operateStr+"</td>"+
	             	//"<td>条件值：<input class='input_bd' name=\"conValue\" maxlength='32' value=\"\"/></td>"+
	             	"<td>"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tjz_mh")+"<input class='input_bd' name=\"conValue\" maxlength='32' value=\"\"/></td>"+
	             	"<td><a onclick=\"delTable("+tableSize+")\"><img src=\"<%=skin %>/images/x.png\"/></a></td></tr></thead></table>";
	   	     $('#detail_Info').append(tableStr);  
	  	   	 $('#tableSize').val(tableSize);
	      }
	      function goLast()
	      {
		      var prId = $("#prId").val();
	    	  location.href="<%=path%>/eng_moService.htm?method=doProEdit&workType=waterLine&serId=<%=serId %>&prId="+prId;
		  }
	      //获取新模板
		function getTmplInfo() {
			
			var lguserid=$("#lguserid").val();
			$.post("eng_mtProcess.htm?method=getSmsTmpl&dsflag=0,1&lguserid="+lguserid,function(msg)
					{
						if(msg== "" || msg=="errtmpl"){
							//alert("获取模板信息失败。");
							alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_hqmbxxsb"));
							return;
						}
						if(msg=="nodata"){
							return;
						}
						msg = eval("("+msg+")");
						//var selStr = '<option value="" style="color:#666666">请选择</option>';
						var selStr = '<option value="" style="color:#666666">'+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_qxz")+'</option>';
						$.each(msg, function(idx,item){
							selStr += '<option value="'+item.tmid+'" >'+item.tmname+'</option>';
						});
						$("#selectInfoBody").find("option").remove();
						$("#selectInfoBody").append(selStr);
					}
			);
		}
	      
	    function chooseNetTpl()
		{
			$(".ui-dialog-titlebar-close").show();
			var frameSrc = $("#tempFrame").attr("src");
			var lgcorpcode = $("#lgcorpcode").val();
			
				var lguserid = $("#lguserid").val();
				frameSrc = "eng_mtProcess.htm?method=toWxTmpl&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
				$("#tempFrame").attr("src",frameSrc);
			
			$("#tmplDiv").dialog("open");
		}
	    
	    //隐藏层
		function closeDialog(){
			$("#tmplDiv").dialog("close");
			$("#tempFrame").attr("src","");
		}
	    
	    function setWxInfo(wxId){
	    	var newStr = '#W_'+wxId+'#';
	    	add(newStr);
	    	//var oldCon = $("#msgMain").val();
	    	//$("#msgMain").val(oldCon+newStr);
	    }
	    
</script>
		
	</body>
</html>