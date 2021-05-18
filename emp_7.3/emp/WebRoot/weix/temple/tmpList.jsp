<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.entity.weix.LfWcAccount" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>

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
	String menuCode = titleMap.get("replymanger");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String lguserid = (String)request.getAttribute("lguserid");
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
 	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	
 	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
 	
	//新增
	boolean addcode = false;
	if(btnMap.get(menuCode+"-1")!=null)
	{
		addcode = true;
	}
	//删除
	boolean delcode = false;
	if(btnMap.get(menuCode+"-2")!=null)
	{
		delcode = true;
	}
	//修改
	boolean upcode = false;
	if(btnMap.get(menuCode+"-3")!=null)
	{
		upcode = true;
	}
	PageInfo pageInfo = null;
	pageInfo = (PageInfo)request.getAttribute("pageInfo");
	if(pageInfo==null){
		pageInfo = new PageInfo();	
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css">	
	</head>
	<body onload="">
		<div id="container" class="container">
			<input type="hidden" id="pathUrl" value="<%=path %>" />
			<input type="hidden" id="type" value="<%=tempType %>" />
			<input type="hidden" id="path" value="<%=path %>" />
			<input type="hidden" id="_pageIndex" value="<%=pageInfo.getPageIndex() %>" />
			<input type="hidden" id="_totalPage" value="<%=pageInfo.getTotalPage() %>" />
			<input type="hidden" id="_pageSize" value="<%=pageInfo.getPageSize() %>" />
			<input type="hidden" id="_totalRec" value="<%=pageInfo.getTotalRec() %>" />
		<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
				
				
				<div>
				<form name="pageForm" action="<%=path%>/cwc_replymanger.htm?method=find" method="post" id="pageForm">
				<div id="getloginUser" style="display:none;"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if (addcode){%>
							<a id="addeployee" onclick="javascript:addNewText()"><emp:message key="wexi_qywx_hfgl_btn_1" defVal="新增文本"
											fileName="weix"></emp:message></a>
						  	<a id="addeployee" onclick="javascript:addNexImg(0)"><emp:message key="wexi_qywx_hfgl_btn_2" defVal="新增单图"
											fileName="weix"></emp:message></a>	
						  	<a id="addeployee" onclick="javascript:addNexImg(1)"><emp:message key="wexi_qywx_hfgl_btn_3" defVal="新增多图"
											fileName="weix"></emp:message></a>	
						<%} %>
					  	<%if (delcode){%>
							<a id="delepl" onclick="javascript:delBk()"><emp:message key="wexi_qywx_hfgl_btn_4" defVal="删除回复"
											fileName="weix"></emp:message></a>
						<%} %>			
						
					</div>
				
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
									<emp:message key="wexi_qywx_hfgl_text_1" defVal="回复类型："
											fileName="weix"></emp:message>
									</td>
											<td>
												<label>
													<select  style="width:180px;"  name="tempType" id="tempType" >
														<option value="0"><emp:message key="wexi_qywx_hfgl_text_2" defVal="全部"
											fileName="weix"></emp:message></option>
														<option value="1" ><emp:message key="wexi_qywx_hfgl_text_3" defVal="文本"
											fileName="weix"></emp:message></option>
														<option value="2" ><emp:message key="wexi_qywx_hfgl_text_4" defVal="单图文"
											fileName="weix"></emp:message></option>
														<option value="3" ><emp:message key="wexi_qywx_hfgl_text_5" defVal="多图文"
											fileName="weix"></emp:message></option>
													</select>
												</label>
											</td>
									<td><emp:message key="wexi_qywx_hfgl_text_6" defVal="公众帐号："
											fileName="weix"></emp:message></td>
									<td>
										<label>
										<%
											String AId=request.getParameter("accoutid");
										%>
	                     				<select name="accoutid" id="accuntnum" class="input_bd" style="width:180px;" >
	                     				<option value=""><emp:message key="wexi_qywx_hfgl_text_2" defVal="全部"
											fileName="weix"></emp:message></option>
	                     				<option value="0"  <%= (AId!=null&&"0".equals(AId)) ? "selected='selected'" : "" %>>
	                     				<emp:message key="wexi_qywx_hfgl_text_7" defVal="所有公众帐号"
											fileName="weix"></emp:message></option>
	                     				<%List<LfWcAccount> list = (List<LfWcAccount>)request.getAttribute("accoutlist");
	                     					if(list!=null && list.size()>0){
	                     						for(LfWcAccount li:list){
	                     							String id=String.valueOf(li.getAId());
	                     							String select="";
	                     							if(AId!=null&&AId.toString().equals(id)){
	                     								select="selected=\"selected\"";
	                     							}
	                     						%>
	                     							<option value="<%=id%>" <%=select %>><%=li.getName()%></option>
	                     					<% 	}%>
	                     					<% }%>
	                     				
	                  
	                     		</select></label>
									</td>
									<td><emp:message key="wexi_qywx_hfgl_text_8" defVal="关键字："
											fileName="weix"></emp:message></td>
									<td>
									<input id="serkey" name="serkey"  type="text" value='<%=serkey==null?"":serkey %>'/>
									</td>
									<td class="tdSer">
										<a id="search"></a>
									</td>
								</tr>
								<tr>
									
									<td><emp:message key="wexi_qywx_hfgl_text_9" defVal="创建时间："
											fileName="weix"></emp:message></td>
									<td>
										<input type="text" id="submitSartTime" name="startdate" 
										style="cursor: pointer; width: 180px;background-color: white;" 
										class="Wdate" readonly="readonly" onclick="stime()" value='<%=startdate==null?"":startdate %>'>
									</td>
									<td><emp:message key="wexi_qywx_hfgl_text_10" defVal="至："
											fileName="weix"></emp:message></td>
									<td>
										<input type="text" id="submitEndTime" name="enddate" 
										style="cursor: pointer; width: 180px;background-color: white;" 
										class="Wdate" readonly="readonly" onclick="rtime()" value='<%=enddate==null?"":enddate %>'>
									</td>
									
									<td><emp:message key="wexi_qywx_hfgl_text_11" defVal="回复内容："
											fileName="weix"></emp:message></td>
									<td>
									<input id="serReply"  name="serReply" type="text" value='<%=serReply==null?"":serReply %>'/>
									</td>
									<td >
										
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
							<emp:message key="wexi_qywx_hfgl_text_12" defVal="回复名称"
											fileName="weix"></emp:message>
						</th>
						<th width="13%">
							<emp:message key="wexi_qywx_hfgl_text_13" defVal="关键字"
											fileName="weix"></emp:message>
						</th>
						<th width="17%">
							<emp:message key="wexi_qywx_hfgl_text_14" defVal="回复内容"
											fileName="weix"></emp:message>
						</th>
						<th width="6%">
							<emp:message key="wexi_qywx_hfgl_text_15" defVal="回复类型"
											fileName="weix"></emp:message>
						</th>
						<th width="12%">
							<emp:message key="wexi_qywx_hfgl_text_16" defVal="公众帐号"
											fileName="weix"></emp:message>
						</th>
						<th width="15%">
							<emp:message key="wexi_qywx_hfgl_text_17" defVal="创建时间"
											fileName="weix"></emp:message>
						</th>
						<th colspan="3" width="18%">
							<emp:message key="wexi_qywx_hfgl_text_18" defVal="操作"
											fileName="weix"></emp:message>
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
										value='<%=bean.get("t_id") %>' />
									<input type="hidden" id="<%=bean.get("t_id") %>" value="<%=bean.get("t_name") %>"/>	
								</td>
								<td>
									<a onclick='modify(this,"1")'>
										<label style="display:none"><xmp><%=bean.get("t_name").toString() %></xmp></label>
										<xmp><%=(bean.get("t_name")==null)?"":bean.get("t_name").toString().length()>6?(bean.get("t_name").toString().substring(0,6)+"..."):bean.get("t_name").toString()%></xmp>
									</a>	
								</td>
								<td >
									<a onclick='modify(this,"2")'>
										<label style="display:none"><xmp><%=bean.get("wordnames").toString() %></xmp></label>
										<xmp><%=(bean.get("wordnames")==null)?"":bean.get("wordnames").toString().length()>6?(bean.get("wordnames").toString().substring(0,6)+"..."):bean.get("wordnames").toString()%></xmp>
									</a>	
								</td>
								<td>
								 <%if(bean.get("msg_text")!=null&&!"".equals(bean.get("msg_text"))) { 
									 String msg_text=bean.get("msg_text").toString();
								 %>
										<a onclick="javascript:modify(this,3)">
										<label style="display:none"><xmp><%=msg_text %></xmp></label>
										<xmp><%=msg_text.length()>8?msg_text.substring(0,8)+"...":msg_text %></xmp>
										</a> 
								 <% } else { %>
								 		-
								 <% } %>
								</td>
								<td>
								<%
								Object sta =bean.get("msg_type");
								String s = sta.toString();
								if("0".equals(s))
								{
									out.print(MessageUtils.extractMessage("weix","wexi_qywx_hfgl_text_3",request));
								}else if("1".equals(s))
								{
									out.print(MessageUtils.extractMessage("weix","wexi_qywx_hfgl_text_4",request));
								}else if("2".equals(s))
								{
									out.print(MessageUtils.extractMessage("weix","wexi_qywx_hfgl_text_5",request));
								}
								%>
								</td>
								<td >
									<% 
										String accoutname = (String)bean.get("accoutname");
										if(accoutname==null||"".equals(accoutname)){
											accoutname = MessageUtils.extractMessage("weix","wexi_qywx_hfgl_text_6",request);
										}
									%>
									
										<label ><xmp><%=accoutname %></xmp></label>

								</td>
								<td  >
									<%=bean.get("createtime").toString().replaceAll("\\.\\d*", "") %>
								</td>
								<td nowrap="nowrap">
								    <a  onclick='preview(<%=bean.get("t_id")%>)'><emp:message key="common_text_6" defVal="预览"
											fileName="common"></emp:message></a>
									<%if(upcode){ %>
									&nbsp;&nbsp;
									<a href='javascript:go(<%=bean.get("t_id") %>,<%=bean.get("msg_type") %>,"<%=bean.get("t_name")%>");'>
										<emp:message key="common_text_7" defVal="编辑"	fileName="common"></emp:message></a>
									<%} %>
									<%if (delcode){%>
									&nbsp;&nbsp;
									<a  onclick='deltemple(<%=bean.get("t_id")%>)'><emp:message key="common_text_8" defVal="删除"
											fileName="common"></emp:message></a>
									<%} %>
								</td>
								
							</tr>
							<%
							}
						}else{
							%>
						<tr>
						<td colspan="10">
							<emp:message key="wexi_qywx_hfgl_text_19" defVal="无记录"
											fileName="weix"></emp:message>
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
			<div id="divBox" style="display:none" title="预览">
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
	  <div id="modify" title="信息内容"  style="padding:5px;width:300px;height:160px;display:none">
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/weix_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js" ></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.selectnew.js"/></script>
   	    <script type="text/javascript" src="<%=iPath %>/js/template.js"></script>
	</body>
	
</html>
