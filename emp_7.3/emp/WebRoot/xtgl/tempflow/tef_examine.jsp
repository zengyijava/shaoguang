<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.sql.Timestamp"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.entity.approveflow.LfFlowRecord"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.entity.template.LfTemplate"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page
        import="org.apache.commons.lang.StringEscapeUtils" %>

<%
	String path = request.getContextPath();
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	@SuppressWarnings("unchecked")
	List<LfFlowRecord> recordList= (List<LfFlowRecord>)request.getAttribute("recordList");
	LfTemplate temp = (LfTemplate)request.getAttribute("temp");
	@SuppressWarnings("unchecked")
	LinkedHashMap<Long,String> usernameMap=(LinkedHashMap<Long,String>)request.getAttribute("usernameMap");
	//当前审核信息
	LfFlowRecord curRecord = (LfFlowRecord)request.getAttribute("curRecord");
	
	LfSysuser lfsysuser = (LfSysuser)request.getAttribute("lfsysuser");

	
	boolean isLastCheck = true;
	
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
	String nowtime = df.format(new Date());
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String httpUrl = StaticValue.getFileServerViewurl();
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/common/common.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.dialog.new.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/tef_examine.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/tef_examine.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	<script language="javascript">
	
	
	</script>
	</head>
	<body id="tef_examine">
	<div id="container" class="container">
	<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_mbsp",request)) %>
		<div id="rContent" class="rContent">
		<div class="titletop" >
					<table class="titletop_table mbsp_mbsp_table"  >
						<tr>
							<td class="titletop_td">
								<emp:message key="xtgl_spgl_mbsp_mbsp" defVal="模板审批" fileName="xtgl"/>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="xtgl_spgl_xxsp_fhsyj" defVal="返回上一级" fileName="xtgl"/></font>
							</td>
						</tr>
					</table>
				</div>
			  <form method="post" action="" name="form2">
			  <div  id="loginparams" class="loginparams"></div>
					<input type="hidden" id="pathUrl" value="<%=path %>">
					<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
					<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
					<%-- 预发送条数
					<div  id="PconfigDiv" style="padding-top:0px;">
						<div id="tabContainer1" onclick="changflod('a')"  style="width:520px;display: block;padding-left:22px;font-size: 13px;font-weight: normal;" class="div_bg div_bd">	
	         			    <label>模板信息</label>
	         			    <a id="imga" class="fold" style="text-decoration: none;margin-left:427px">&nbsp;&nbsp;&nbsp;&nbsp;</a>
						</div>
	        		</div>
	        		--%>
					<div class="itemDiv msgdiva"  id="msgdiva">
					<table class="table_temp" border="0" cellspacing="0" cellpadding="0" id="table_temp">
  <tr>
    <td  class="div_bd div_bg tjr_mh_td" align="right"><emp:message key="xtgl_spgl_xxsp_tjr_mh" defVal="提交人：" fileName="xtgl"/></td>
    <td colspan="3"  class="div_bd ">&nbsp;&nbsp;<%=usernameMap.get(temp.getUserId()) %></td>
  </tr>
  <tr>
    <td  class="div_bd div_bg" align="right"><emp:message key="xtgl_spgl_mbsp_mbmc_mh" defVal="模板名称：" fileName="xtgl"/></td>
    <td colspan="3"  class="div_bd ">&nbsp;&nbsp;<%=temp.getTmName() %></td>
  </tr>
  <tr>
    <td  class="div_bd div_bg" align="right"><emp:message key="xtgl_spgl_mbsp_cjsj_mh" defVal="创建时间：" fileName="xtgl"/></td>
    <td  class="div_bd ">&nbsp;&nbsp;<%=df.format(temp.getAddtime()) %></td>
    <td  class="mblx_mh_td"  class="div_bd div_bg" align="right"><emp:message key="xtgl_spgl_mbsp_mblx_mh" defVal="模板类型：" fileName="xtgl"/></td>
    <td  class="div_bd">&nbsp;&nbsp;<%
										if(temp.getTmpType() == 3){
											out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_dxmb",request));
										}else if(temp.getTmpType() == 4){
											out.print(MessageUtils.extractMessage("xtgl","xtgl_spgl_shlcgl_cxmb",request));
										}else{
											out.print("-");
										}
									%></td>
  </tr>
  <tr>
    <td  class="div_bd div_bg" align="right"><emp:message key="xtgl_spgl_mbsp_mbnr_mh" defVal="模板内容：" fileName="xtgl"/></td>
    <td colspan="3"  class="div_bd">
        <%
        String tmMsg = (temp.getTmMsg()==null?"":temp.getTmMsg().replaceAll("#[pP]_([1-9][0-9]*)#","{#"+MessageUtils.extractMessage("xtgl","xtgl_spgl_mbsp_cs",request)+"$1#}"));
        if(temp.getTmpType() == 3){%>
            <textarea class="TmpType_text"
            readonly class="input_bd"><%=StringEscapeUtils.escapeHtml(tmMsg) %></textarea>
            <%} else{%>
            <a href="javascript:doPreview('<%=tmMsg%>',<%=temp.getDsflag() %>)" class="djck_a"><emp:message key="xtgl_spgl_mbsp_djck" defVal="点击查看" fileName="xtgl"/></a>
        <%} %>
    </td>
  </tr>
</table>
	
					</div>	
					
					<% 
						Integer level = curRecord.getRLevel();
						if(recordList != null && recordList.size()>0)
						{
							List<LfFlowRecord> records = new ArrayList<LfFlowRecord>();
							for(int i=1;i<=level;i++)
							{
								records.clear();
								for(int k=0;k<recordList.size();k++)
								{
									LfFlowRecord recordk = recordList.get(k);
									 if(recordk.getRLevel()- 0== i ){
										 records.add(recordk);
									 }
								}
								if(records.size() == 0)
								{
									continue;
								}
								%>
								<div  id="PconfigDiv"  class="PconfigDiv" >
									<div id="tabContainer1" onclick="changflod(<%=i%>)"  class="div_bg div_bd tabContainer1" >	
				         			    <label class="shlcgl_d_label"><emp:message key="xtgl_spgl_shlcgl_d" defVal="第" fileName="xtgl"/><%=i%><emp:message key="xtgl_spgl_shlcgl_jsp" defVal="级审批" fileName="xtgl"/></label>
				         			    <a id="img<%=i%>" class="fold img_a"  >&nbsp;&nbsp;&nbsp;&nbsp;</a>
									</div>
				        		</div>
				        		<div class="itemDiv msgdiv"  id="msgdiv<%=i%>">
									<table class="msgdiv_table" >
										<% 
											for(int j=0;j<records.size();j++){
												LfFlowRecord record = records.get(j);
										 %>
												<tr>
													<td  align="center"  class="div_bd div_bg UserCode_td">
															<% if(usernameMap.get(record.getUserCode()) != null)
														{
															out.print(usernameMap.get(record.getUserCode()).replaceAll("<","&lt;").replaceAll(">","&gt;"));
														}
														else{out.print("-");}%>
													</td>
													<td align="left"  class="div_bd RTime_td">
														<%=df.format(record.getRTime())%>
													</td>
												</tr>
												<tr>
													<td align="left"  class="div_bd Comments_td" colspan="2">
														<%
															if(record.getComments() != null && !"".equals(record.getComments())){
																out.print(record.getComments().replaceAll("<","&lt;").replaceAll(">","&gt;"));
															}else{
																out.print("");
															}
														%>
													</td>
												</tr>
											<% 
											}
										 %>
									</table>	
								</div>
							 <% 	
							}
						}
					%>
					<%if(curRecord.getIsComplete() == 2 && curRecord.getRState() == -1) {%>
					<div class="itemDiv div_bd dqspr_mh_div"  >
						<table class="dqspr_mh_table">
					   		<tr>
								<td  align="right"  class="div_bd div_bg dqspr_mh_td">
									<emp:message key="xtgl_spgl_xxsp_dqspr_mh" defVal="当前审批人：" fileName="xtgl"/>
								</td>
								<td align="left"  class="div_bd getName_td" >
									<%=lfsysuser.getName().replaceAll("<","&lt;").replaceAll(">","&gt;") %>
								</td>
					   			<td  align="right"  class="div_bd div_bg spjb_mh_td">
									<emp:message key="xtgl_spgl_xxsp_spjb_mh" defVal="审批级别：" fileName="xtgl"/>
								</td>
								<td align="left"  class="div_bd RLevel" >
									<%=curRecord.getRLevel() %>/<%=curRecord.getRLevelAmount() %>
								</td>
					   		</tr>	
							<tr>
								<td  align="right"  class="div_bd div_bg spyj_mh_td">
									<emp:message key="xtgl_spgl_xxsp_spyj_mh" defVal="审批意见：" fileName="xtgl"/>
								</td>
								<td align="left"  class="div_bd content_td" colspan="3">
									<textarea id="content" name="content" class="input_bd content" ></textarea>
								</td>
							</tr>
							
						</table>	
					</div>
					
			
					<%} %>
					
					<div  class="itemDiv xxsp_ty_div"  >	
						<%if(curRecord.getIsComplete() == 2 && curRecord.getRState() == -1) {%> 
							<input type="button" value=" <emp:message key='xtgl_spgl_xxsp_ty' defVal='同意' fileName='xtgl'/> " id="oks" 
							onclick="javascript:review(<%=curRecord.getFrId() %>,1);" class="btnClass5 mr23"/>
							
							<input type="button" value=" <emp:message key='xtgl_spgl_xxsp_jj' defVal='同意' fileName='xtgl'/>" id="rjs"
							onclick="javascript:review(<%=curRecord.getFrId() %>,2);" class="btnClass6 mr23"/>
						<%} %>
						<input type="button" value=" <emp:message key='xtgl_spgl_shlcgl_fh' defVal='返回' fileName='xtgl'/> " onclick="javascript:back()" class="btnClass6"/>
						<br/>
					</div>	
					
					
					
			 </form>
		 </div>
	</div> 
	<div id="mmsExamine" title="<emp:message key='xtgl_spgl_shlcgl_cxmb' defVal='彩信模板' fileName='xtgl'/>" class="mmsExamine">
		<div id="mobileDiv" class="mobileDiv">
			<div id="mobile" class="mobile" >
			<center>
			<div id="pers" class="pers">
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
			     <label id="pointer" class="pointer"></label>
			     <label id="nextpage" class="nextpage"></label>
			  </td>
			</tr>
			<tr align="center">
				<td>
				   <label id="currentpage" class="currentpage"></label>
				</td>
			</tr>
			</table>				
			</center>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/examinetpl.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
</body>
</html>

