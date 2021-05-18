<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="com.montnets.emp.entity.wxgl.LfWeiAccount" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("keywordReply");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String lguserid = (String)request.getAttribute("lguserid");
 	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	
	PageInfo pageInfo = null;
	pageInfo = (PageInfo)request.getAttribute("pageInfo");
	if(pageInfo==null){
		pageInfo = new PageInfo();	
	}
	
    String tempType = request.getParameter("tempType");
    //起始时间
    String startdate = request.getParameter("startdate");
    //结束时间
    String enddate = request.getParameter("enddate");
    //公众账号ID
    String accoutid = request.getParameter("accoutid");
    //关键字
    String serkey = request.getParameter("serkey");
    //回复内容
    String serReply = request.getParameter("serReply");
    
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    String dantw = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_75", request);
	String duotw = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_76", request);
	String wb = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_74", request);
	String sygzh = com.montnets.emp.i18n.util.MessageUtils.extractMessage("wxgl", "wxgl_gzhgl_title_77", request);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title><emp:message key="wxgl_gzhgl_title_384" defVal="回复列表" fileName="wxgl"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>">
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
			<link rel="stylesheet" href="<%=iPath%>/css/temple_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body onload="">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="tempTypeValue" value="<%=tempType%>" />
	<input type="hidden" id="path" value="<%=path%>" />
	<input type="hidden" id="_pageIndex" value="<%=pageInfo.getPageIndex()%>" />
	<input type="hidden" id="_totalPage" value="<%=pageInfo.getTotalPage()%>" />
	<input type="hidden" id="_pageSize" value="<%=pageInfo.getPageSize()%>" />
	<input type="hidden" id="_totalRec" value="<%=pageInfo.getTotalRec()%>" />
		<div id="container" class="container">
		<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
				<div>
				<form name="pageForm" action="<%=path%>/weix_keywordReply.htm?method=find" method="post" id="pageForm">
				<div id="getloginUser" style="display:none;"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
							<a id="addeployee" onclick="javascript:addNewText()"><emp:message key="wxgl_gzhgl_title_385" defVal="新增文本" fileName="wxgl"/></a>
						  	<a id="addeployee" onclick="javascript:addNexImg(0)"><emp:message key="wxgl_gzhgl_title_386" defVal="新增单图" fileName="wxgl"/></a>	
						  	<a id="addeployee" onclick="javascript:addNexImg(1)"><emp:message key="wxgl_gzhgl_title_387" defVal="新增多图" fileName="wxgl"/></a>	
							<a id="delepl" onclick="javascript:delBk()"><emp:message key="wxgl_gzhgl_title_388" defVal="删除回复" fileName="wxgl"/></a>
					</div>
				
					<div id="condition">
						<table>
							<tbody>
								<tr>
									
									<td><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/></td>
									<td>
										<label>
										<%
										    String AId = request.getParameter("accoutid");
										%>
	                     				<select name="accoutid" id="accuntnum" class="input_bd" style="width:225px;">
	                     				<option value=""><emp:message key="wxgl_gzhgl_title_63" defVal="全部" fileName="wxgl"/></option>
	                     				<option value="0"  <%=(AId != null && "0".equals(AId)) ? "selected='selected'" : ""%>><emp:message key="wxgl_gzhgl_title_77" defVal="所有公众帐号" fileName="wxgl"/></option>
	                     				<%
	                     				    List<LfWeiAccount> list = (List<LfWeiAccount>) request.getAttribute("accoutlist");
	                     				    if(list != null && list.size() > 0)
	                     				    {
	                     				        for (LfWeiAccount li : list)
	                     				        {
	                     				            String id = String.valueOf(li.getAId());
	                     				            String select = "";
	                     				            if(AId != null && AId.toString().equals(id))
	                     				            {
	                     				                select = "selected=\"selected\"";
	                     				            }
	                     				%>
	                     							<option value="<%=id%>" <%=select%>><%=li.getName()%></option>
	                     					<%
	                     					    }
	                     					%>
	                     					<%
	                     					    }
	                     					%>
	                     		</select></label>
									</td>
									<td><emp:message key="wxgl_gzhgl_title_62" defVal="回复内容：" fileName="wxgl"/></td>
									<td>
									<input id="serReply"  name="serReply" type="text" value='<%=serReply == null ? "" : serReply%>'/>
									</td>
									<td><emp:message key="wxgl_gzhgl_title_126" defVal="关键字：" fileName="wxgl"/></td>
									<td>
									<input id="serkey"  name="serkey"  type="text" value='<%=serkey == null ? "" : serkey%>'/>
									</td>
									<td></td>
									<td class="tdSer">
										<a id="search"></a>
									</td>
								</tr>
								<tr>
									<td ><emp:message key="wxgl_gzhgl_title_58" defVal="回复类型：" fileName="wxgl"/></td>
											<td>
												<label>
													<select  style="width:225px;"  name="tempType" id="tempType" >
														<option value="0"><emp:message key="wxgl_gzhgl_title_63" defVal="全部" fileName="wxgl"/></option>
														<option value="1" ><emp:message key="wxgl_gzhgl_title_74" defVal="文本" fileName="wxgl"/></option>
														<option value="2" ><emp:message key="wxgl_gzhgl_title_75" defVal="单图文" fileName="wxgl"/></option>
														<option value="3" ><emp:message key="wxgl_gzhgl_title_76" defVal="多图文" fileName="wxgl"/></option>
													</select>
												</label>
											</td>
									<td><emp:message key="wxgl_yhgl_title_53" defVal="创建时间：" fileName="wxgl"/></td>
									<td>
										<input type="text" id="submitSartTime" name="startdate" 
										style="cursor: pointer;background-color: white;" 
										class="Wdate" readonly="readonly" onclick="stime()" value='<%=startdate == null ? "" : startdate%>'>
									</td>
									<td><emp:message key="wxgl_gzhgl_title_69" defVal="至：" fileName="wxgl"/></td>
									<td>
										<input type="text" id="submitEndTime" name="enddate" 
										style="cursor: pointer; background-color: white;" 
										class="Wdate" readonly="readonly" onclick="rtime()" value='<%=enddate == null ? "" : enddate%>'>
									</td>
									<td colspan="2">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
		
		<div id="resultSet">
			<table id="content">
				<thead>
					<tr>
						<th width="5%" >
							<input type="checkbox" name="checkall" id="checkall"
								value="checkbox" onclick="checkAlls(this,'checklist');" />
						</th>
						<th width="13%">
							<emp:message key="wxgl_gzhgl_title_389" defVal="回复名称" fileName="wxgl"/>
						</th>
						<th width="13%">
							<emp:message key="wxgl_gzhgl_title_72" defVal="关键字" fileName="wxgl"/>
						</th>
						<th width="17%">
							<emp:message key="wxgl_gzhgl_title_65" defVal="回复内容" fileName="wxgl"/>
						</th>
						<th width="6%">
							<emp:message key="wxgl_gzhgl_title_73" defVal="回复类型" fileName="wxgl"/>
						</th>
						<th width="12%">
							<emp:message key="wxgl_gzhgl_title_66" defVal="公众帐号" fileName="wxgl"/>
						</th>
						<th width="15%">
							<emp:message key="wxgl_gzhgl_title_6" defVal="创建时间" fileName="wxgl"/>
						</th>
						<th colspan="3" width="18%">
							<emp:message key="wxgl_gzhgl_title_7" defVal="操作" fileName="wxgl"/>
						</th>
					</tr>
				</thead>
				<tbody id="tbs">
				<%
				@SuppressWarnings("unchecked")
				List<DynaBean> beans = (List<DynaBean>)request.getAttribute("templelist");
				if(beans!=null && beans.size()>0)
				{
					for(DynaBean bean:beans)
					{
					
				%>
							<tr>
								<td >
									<input type="checkbox" name="checklist" id="checklist"
										value='<%=bean.get("t_id")%>' />
									<input type="hidden" id="<%=bean.get("t_id")%>" value="<%=bean.get("t_name")%>"/>	
								</td>
								<td>
									<xmp><%=(bean.get("t_name") == null) ? "" : bean.get("t_name").toString().length() > 6 ? (bean.get("t_name").toString().substring(0, 6) + "...") : bean.get("t_name").toString()%></xmp>
									<div class="titleTip" style="display:none"><%=bean.get("t_name").toString()%></div>
								</td>
								<td >
										<xmp><%=(bean.get("wordnames") == null) ? "" : bean.get("wordnames").toString().length() > 6 ? (bean.get("wordnames").toString().substring(0, 6) + "...") : bean.get("wordnames").toString()%></xmp>
										<div class="titleTip" style="display:none"><%=bean.get("wordnames").toString()%></div>
								</td>
								<td>
								 <%
									 //System.out.println(bean.get("msg_text"));
								 		
								 
								     if(bean.get("msg_text") != null && !"".equals(bean.get("msg_text")))
								    	 
								             {
								                 String msg_text = bean.get("msg_text").toString();
								 %>
										<xmp><%=msg_text.length() > 8 ? msg_text.substring(0, 8) + "..." : msg_text%></xmp>
										<div class="titleTip" style="display:none"><xmp><%=msg_text%></xmp></div>
								 <%
 								     }
						             else
						             {
 								 %>
								 		-
								 <%
 								     }
 								 %>
								</td>
								<td>
								<%
								Object sta =bean.get("msg_type");
								String s = sta.toString();
								if(s.equals("0"))
								{
									out.print(wb);
								}else if(s.equals("1"))
								{
									out.print(dantw);
								}else if(s.equals("2"))
								{
									out.print(duotw);
								}
								%>
								</td>
								<td >
									<% 
										String accoutname = (String)bean.get("accoutname");
										if(accoutname==null||"".equals(accoutname)){
											accoutname = sygzh;
										}
									%>
									
										<label ><xmp><%=accoutname %></xmp></label>

								</td>
								<td  >
									<%=bean.get("createtime").toString().replaceAll("\\.\\d*", "") %>
								</td>
								<td nowrap="nowrap">
								    <a  onclick='preview(<%=bean.get("t_id")%>)'><emp:message key='wxgl_button_11' defVal='预览' fileName='wxgl'/></a>
									&nbsp;&nbsp;
									<a href='javascript:go(<%=bean.get("t_id") %>,<%=bean.get("msg_type") %>,"<%=bean.get("t_name")%>");'><emp:message key='wxgl_button_12' defVal='编辑' fileName='wxgl'/></a>
									&nbsp;&nbsp;
									<a  onclick='deltemple(<%=bean.get("t_id")%>)'><emp:message key="wxgl_button_10" defVal="删除" fileName="wxgl"/></a>
								</td>
							</tr>
							<%
							}
						}else{
							%>
						<tr>
						<td colspan="10">
							<emp:message key="wxgl_gzhgl_title_11" defVal="无记录" fileName="wxgl"/>
						</td>
						</tr>
					<%} %>
				</tbody>
				<tfoot>
				<tr>
				<td colspan="11">
					<div id="pageInfo"></div>
				</td>
				</tr>
				</tfoot>
			</table>						
		</div>
					
		</form>
		</div>
				
			</div>
			<%-- 内容结束 --%>
			<div id="divBox" style="display:none" title="<emp:message key='wxgl_button_11' defVal='预览' fileName='wxgl'/>">
				<div style="width:240px;height:460px;margin-left:30px; margin-top:-3px; background:url(<%=commonPath %>/common/img/iphone5.jpg);">
					<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="msgPreviewFrame" src=""></iframe>	
				</div>
			</div> 
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
	  <div id="modify" title="<emp:message key='wxgl_gzhgl_title_390' defVal='信息内容' fileName='wxgl'/>"  style="padding:5px;width:300px;height:160px;display:none">
		<table width="100%">
			<thead>
				<tr style="padding-top:2px;margin-bottom: 2px;">
					<td style='word-break: break-all;'>
						<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>
					</td>
					 
				</tr>
			   <tr style="padding-top:2px;">
					<td>
					</td>
					</tr>
				 
			</thead>
		</table>
	</div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	 	<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"/></script>
   	    <script type="text/javascript" src="<%=iPath%>/js/template.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
	
</html>