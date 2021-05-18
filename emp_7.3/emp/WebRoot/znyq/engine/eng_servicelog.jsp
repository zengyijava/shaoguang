<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.engine.vo.LfServicelogVo"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.SystemGlobals"%>
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
List<LfServicelogVo> serList=(List<LfServicelogVo>)request.getAttribute("serList");
@ SuppressWarnings("unchecked")
List<LfSysuser> sysList = (List<LfSysuser>)request.getAttribute("sysList");
java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("servicelog");
menuCode = menuCode==null?"0-0-0":menuCode;

	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	LfServicelogVo serVo=(LfServicelogVo)request.getAttribute("serVo");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
		
		String httpUrl = StaticValue.getFileServerViewurl();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
	</head>
	<body id="eng_servicelog" class="eng_servicelog">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		
		<div id="container" class="container">
		<%-- 当前位置 --%>
		<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
		--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<form name="pageForm" action="<%=path %>/eng_servicelog.htm?method=find" method="post"
					id="pageForm">
					<div id="hiddenValueDiv" class="hiddenValueDiv"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
					</div>
					<div id="condition">
						<table >
							<tr>
							   <td ><emp:message key="znyq_ywgl_ywfwrz_ywmc_mh" defVal="业务名称：" fileName="znyq"></emp:message></td>
							   <td><input type="text" class="commonWidth1" name="serName" id="serName" value="<%=serVo.getSerName()==null?"":serVo.getSerName() %>" />
							   </td>
							   <td ><emp:message key="znyq_ywgl_ywfwrz_tjr_mh" defVal="提交人：" fileName="znyq"></emp:message></td>
							   <td>
								<select id="subUserName" name="subUserName" class="commonWidth2" isInput="false">
									<option value=""><emp:message key="znyq_ywgl_common_text_16" defVal="全部" fileName="znyq"></emp:message></option>
									<%
									if(sysList != null && sysList.size() > 0){
										for(int u=0;u<sysList.size();u++)
										{
											String name=((LfSysuser)sysList.get(u)).getName();
									%>
										<option value="<%=name %>" <%=(serVo.getName()!=null && serVo.getName().equals(name))?"selected":"" %>>
											<label><xmp><%=name %></xmp></label>
										</option>
									<%
										}}
									%>
								</select>
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
							<tr> 
								<td><emp:message key="znyq_ywgl_ywfwrz_yxsj_mh" defVal="运行时间：" fileName="znyq"></emp:message></td>
								<td>
								<input type="text" onclick="stime()"
									value="<%=serVo.getStartSubmitTime()==null?"":serVo.getStartSubmitTime() %>"
									readonly="readonly" class="Wdate"   id="logStartTime"  name="logStartTime"/>
								</td>
								<td align="left"> <emp:message key="znyq_ywgl_common_text_15" defVal="至：" fileName="znyq"></emp:message></td>
								<td> 
								<input type="text"  name="logEndTime" id="logEndTime" readonly="readonly"
									value="<%=serVo.getEndSubmitTime()==null?"":serVo.getEndSubmitTime() %>"
									onclick="rtime()"  class="Wdate"/>
								</td>
								<td></td>
						  </tr>
						</table>
					</div>
					<table id="content">
						<thead>
								<tr> 
								 <th><emp:message key="znyq_ywgl_ywfwrz_ywmc" defVal="业务名称" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_ywfwrz_ywlx" defVal="业务类型" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_ywfwrz_tjr" defVal="提交人" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_ywfwrz_yxsj" defVal="运行时间" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_ywfwrz_fszt" defVal="发送状态" fileName="znyq"></emp:message></th>
								 <th><emp:message key="znyq_ywgl_common_text_14" defVal="操作" fileName="znyq"></emp:message></th>
								</tr>	
							</thead>
							<tbody>
							<%
							if (serList != null && serList.size() > 0)
							{
											for(LfServicelogVo serlog : serList)
											{
										%>
											<tr>
												<td class="textalign" >
												<xmp class="commonXmp">
													<%=serlog.getSerName() %>(ID:<%=serlog.getSerId() %>)
												</xmp>
												</td>
												<td class="ztalign" >
													<%--<%=(serlog.getSerType()-1==0)?"上行业务":"下行业务" %>--%>
													<%=(serlog.getSerType()-1==0)?MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_sxyw",request):MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_xhyw",request) %>
												</td>
												<td class="textalign" >
													<%=serlog.getName() %>
												</td>
												<td>
													<%=df.format(serlog.getRunTime()) %>
												</td>
												<td class="ztalign" >
												<%
													switch(serlog.getSlState())
													{
														case 0:
															//out.print("成功向网关发送请求");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_cgxwgfsqq",request));
															break;
														case 1:
															//out.print("向网关发送请求失败");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_xwgfsqqsb",request));
															break;
														case 2:
															//out.print("运营商余额不足/获取运营商余额失败");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_yysyebz_hqyysyesb",request));
															break;
														case 3:
															//out.print("数据库操作异常");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_sjkczyc",request));
															break;
														case 4:
															//out.print("服务配置不正确");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_fwpzbzq",request));
															break;
														case 5:
															//out.print("其他异常");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_qtyc",request));
															break;
														case 6:
															//out.print("正在运行中");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_zzyxz",request));
															break;
														case 7:
															//out.print("抓取的内容为空");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_zqdnrwk",request));
															break;
														case 8:
															//out.print("操作员被禁用，取消执行");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_czybjy_wfzx",request));
															break;
														case 9:
															//out.print("业务状态为禁用，无法执行");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_ywztwjy_wfzx",request));
															break;
														case 10:
															//out.print("执行成功");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_zxcg",request));
															break;
														case 11:
															//out.print("上传文件到服务器失败，无法执行");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_scwjdfwqsb_wfzx",request));
															break;
														case 12:
															//out.print("发送内容包含关键字");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_fsnrbhgjz",request));
															break;
														case 13:
															//out.print("发送内容为空");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_fsnrwk",request));
															break;
														case 14:
															//out.print("发送账号余额不足");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_fszhyebz",request));
															break;
														case 15:
															//out.print("扣费失败");
															out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_kfsb",request));
															break;
														default:
															out.print("-");
													}
												%>
												</td>
												<td><input type="hidden" name="hidUrl<%=serlog.getSlId() %>" value="<%=serlog.getUrl() %>"/>
												    <%
												    	if(serlog.getUrl()==null || "".equals(serlog.getUrl().trim()))
												    	{
												    		//out.print("无法查看");
												    		out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_ywfwrz_wfck",request));
												    	}else
												    	{
															if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) {
															%>
													    	<a href="javascript:checkFileOuter('<%=serlog.getUrl() %>','<%=path %>')"><emp:message key="znyq_ywgl_ywfwrz_djck" defVal="点击查看" fileName="znyq"></emp:message></a>
												    	<%
												    		}else{
												    			out.print("-");
												    		}
												    	}
												    %>
												</td>
											</tr>
										<%
											}
							}
							            else{
										%>			
										<tr><td colspan="6" align="center"><emp:message key="znyq_ywgl_common_text_1" defVal="无记录" fileName="znyq"></emp:message></td></tr>
										<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="6">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					</form>
			</div>
			<%} %>
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_servicelog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
			setInterval("refeshPage()", 10000);
		});
		
		
		</script>
		
	</body>
</html>