<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.samemms.vo.LfTemplateVo"%>
<%@ page import="com.montnets.emp.servmodule.ydcx.constant.ServerInof" %>
<%@ page import="com.montnets.emp.util.PageInfo" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	LfTemplateVo temVo = (LfTemplateVo)request.getAttribute("lfTemplateVo");
	Long dsflag = temVo.getDsflag()==null?-1l:temVo.getDsflag();
	Long tmState = temVo.getIsPass()==null?3l:temVo.getIsPass();
	long state = temVo.getTmState()==null?-2:temVo.getTmState();
	@ SuppressWarnings("unchecked")
	List<LfTemplateVo> temList = (List<LfTemplateVo>) request.getAttribute("temList");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String lguserid = request.getParameter("lguserid");
	
	//区分是静态还是动态
	String tmpltype = request.getParameter("tmpltype");
	//服务器名称
	String serverName = ServerInof.getServerName();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	   <link href="<%=iPath%>/css/smmsamenms.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	   <link href="<%=skin%>/ydcx_applicationMms.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	</head>
	<body id="ydwx_sameMmsTmpl">
		<div id="container" class="container">
			<%-- header开始 --%>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<form name="pageForm" action="smm_sameMms.htm?method=getLfTemplateByMms&lguserid=<%=lguserid %>&type=<%=tmpltype %>" method="post"
					id="pageForm">
					<div style="display:none" id="hiddenValueDiv"></div>
					<input type="hidden" id="ipathUrl" value="<%=iPath %>"/>
					<input type="hidden" id="commonPath" value="<%=commonPath %>"/>
					<input type="hidden" id="skinType" value="<%=skin %>"/>
					<input id="pathUrl" value="<%=path%>" type="hidden" />
					<input type="hidden" id="tmpltype" name="tmpltype" value="<%=tmpltype %>"/>
					
			<div id="rContent" class="rContent ydcx_rContent">
					<div id="condition">
						<table>
							<tr>
								<td>
									<span><emp:message key="ydcx_cxyy_jtcxfs_text_64" 
										defVal="模板名称：" fileName="ydcx"></emp:message></span>
								</td>
								<td>
									<input type="text" name="tmName" class="ydcx_tmName"
										id="tmName" value="<%=null!=temVo.getTmName()?temVo.getTmName():"" %>" maxlength="16"/>
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
								<emp:message key="ydcx_cxyy_jtcxfs_text_65" 
										defVal="选择" fileName="ydcx"></emp:message>
							</th>
							<th>
								<emp:message key="ydcx_cxyy_jtcxfs_text_66" 
										defVal="模板编号" fileName="ydcx"></emp:message>
							</th>
							<th>
								<emp:message key="ydcx_cxyy_jtcxfs_text_67" 
										defVal="模板名称" fileName="ydcx"></emp:message>
							</th>
							<th>
								<emp:message key="ydcx_cxyy_jtcxfs_text_68" 
										defVal="模板内容" fileName="ydcx"></emp:message>
							</th>
							<th>
								<emp:message key="ydcx_cxyy_jtcxfs_text_69" 
										defVal="创建人名称" fileName="ydcx"></emp:message>
							</th>
							<th>
							    <emp:message key="ydcx_cxyy_jtcxfs_text_70" 
										defVal="创建日期" fileName="ydcx"></emp:message>
							</th>
										
									</tr>
								</thead>
								<tbody>
								<%
								if (temList != null && temList.size() > 0)
								{
									for (LfTemplateVo tem : temList)
									{
								%>
									<tr>
										<td>
											<input type="radio" name="checklist" id="checklist" value="<%=tem.getTmid() %>" />
											<xmp style="display:none"><%=tem.getTmMsg()%></xmp>
											<input type="hidden" value="<%=tem.getTmName().replace("<","&lt;").replace(">","&gt;") %>" id="templatename"/>
										</td>
										<td>
											<%=tem.getTmCode()==null?"":tem.getTmCode() %>
										</td>
										<td class="textalign" >
											<%
											 if(!"".equals(tem.getTmName())&&tem.getTmName()!=null){
												String st1 = "";
												if(tem.getTmName().length()>8)
												{
													st1 = tem.getTmName().substring(0,8)+"...";
												}else
												{
													st1 = tem.getTmName();
												}
											%>
											<a onclick="javascript:modify(this,1)">
											 	 <label style="display:none"><xmp><%=tem.getTmName()%></xmp></label>
											  	<xmp><%=st1 %></xmp>
											  </a> 				
										  	<%}else{ %>		
										  	<%} %>
										</td>
										<td>
											<a onclick="javascript:doPreview('<%=tem.getTmMsg() %>')">
												<emp:message key="ydcx_cxyy_jtcxfs_text_71" 
										defVal="预览" fileName="ydcx"></emp:message>
											</a> 		
										</td>
										<td class="textalign" >
											<%
											    if(tem.getUserState() !=null && tem.getUserState() ==2)
											    {
											      out.print(tem.getName()+"<font color='red'></font>");
											    }
											    else
											    {
											       out.print(tem.getName());
											    }
											%>
										</td>
										<td>
											<%
												if(tem.getAddtime()!=null)
												{
													out.print(df.format(tem.getAddtime()));
												}
											%>
										</td>
									</tr>
								<%
									}
								}else{
									%>
									<tr><td colspan="12"><emp:message key="ydcx_cxyy_jtcxfs_text_72" 
										defVal="无记录" fileName="ydcx"></emp:message></td></tr>
								<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="12">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					
					<div class="ydcx_btns">
						<input class="btnClass5 mr23 ydwx_borderRadius" onclick="tempSure()" value="<emp:message key="ydcx_cxyy_jtcxfs_text_65" 
										defVal="选择" fileName="ydcx"></emp:message>" type="button"/>
						<input class="btnClass6 ydwx_borderRadius" onclick="tempCancel()" value="<emp:message key="ydcx_cxyy_common_btn_16" 
										defVal="取消" fileName="ydcx"></emp:message>" type="button"/>
						<br/>
					</div>
					
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
			</form>
			<%-- foot结束 --%>
		</div>
		
		<div id="modify" title="<emp:message key="ydcx_cxyy_jtcxfs_text_68" defVal="模板内容" fileName="ydcx"></emp:message>" class="ydwx_modify">
				<div id="msg" class="ydwx_msg"><xmp></xmp></div>
		</div>
		
		<%--彩信预览DIV --%>
		<div id="tempView" title="<emp:message key="ydcx_cxyy_jtcxfs_text_24" 
										defVal="彩信内容" fileName="ydcx"></emp:message>" style="display:none;overflow: auto;">
			<input type="hidden" id="tmmsId" value=""/>
			<div class="ydcx_tempView_sub">
				<div id="mobile" class="ydcx_mobile">
				<center>
				<div id="pers" class="ydcx_pers">
				
				<div id="showtime" class="ydcx_showtime"></div>
				
				<div id='chart' class="ydcx_chart">
				</div>
				</div>
				</center>
				
				<div id="screen" class="ydcx_screen">
				</div>
				<center>
				<table>
				<tr>
				  <td>
				     <label id="pointer" style="vertical-align: bottom"></label>
				     <label id="nextpage" style="vertical-align: bottom"></label>
				  </td>
				</tr>
				<tr align="center">
					<td>
					   <label id="currentpage" style="vertical-align: bottom"></label>
					</td>
				</tr>
				</table>				
				</center>
				</div>
			</div>
			<div id="inputParamCnt1" class="ydcx_inputParamCnt1">
			</div>			
		</div>	
		<div class="clear"></div>
		<script>
			var serverName = "<%=serverName%>";
		</script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/mmsPreview.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/template.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ydcx_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[10]);//[10]表示每次只查询10条数据，不能更改
			$('#search').click(function(){submitForm();});
		});
		</script>
	</body>
</html>
