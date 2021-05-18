<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import= "java.util.LinkedHashMap"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.weix.LfWcAccount" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	String menuCode = titleMap.get("usermanger");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String lguserid = (String)request.getParameter("lguserid");
 	String lgcorpcode = (String)request.getParameter("lgcorpcode");
    List<LfWcAccount> acctList = (List<LfWcAccount>)request.getAttribute("acctList");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
    LinkedHashMap<String,String> statusList1 = new LinkedHashMap<String,String>();
    statusList1.put("","全部");
    statusList1.put("0","未验证");
    statusList1.put("1","已 验证");
    
    LinkedHashMap<String,String> statusList2 = new LinkedHashMap<String,String>();
    statusList2.put("","全部");
    statusList2.put("0","未关联");
    statusList2.put("1","已关联");
	//新增
	boolean addCode = false;
	if(btnMap.get(menuCode+"-1")!=null)
	{
		addCode = true;
	}
	//删除回复
	boolean delRep = false;
	if(btnMap.get(menuCode+"-1")!=null)
	{
		delRep = true;
	}
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo)request.getAttribute("pageInfo");
	
		//手机号
	 String phone=request.getParameter("phone");
	    if(phone==null || "".equals(phone))
	    {
	    	phone="";
	    }
	    //是否验证
	    String status1=request.getParameter("status1");
	    if(status1==null || "".equals(status1))
	    {
	    	status1="-1";
	    }
	    //客户名称
	    String uname=request.getParameter("uname");
	    if(uname==null || "".equals(uname))
	    {
	    	uname="";
	    }
	    //微信编号
	    String wcid=request.getParameter("wcid");
	    if(wcid==null || "".equals(wcid))
	    {
	    	wcid="";
	    }
	    //认证开始时间
	    String beginTime=request.getParameter("beginTime");
	    if(beginTime==null || "".equals(beginTime))
	    {
	    	beginTime="";
	    }
	    //认证结束时间
	    String endTime=request.getParameter("endTime");
	    if(endTime==null || "".equals(endTime))
	    {
	    	endTime="";
	    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>回复列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css">	
	</head>
	<body onload="">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="_pageIndex" value="<%=pageInfo.getPageIndex() %>" />
	<input type="hidden" id="_totalPage" value="<%=pageInfo.getTotalPage() %>" />
	<input type="hidden" id="_pageSize" value="<%=pageInfo.getPageSize() %>" />
	<input type="hidden" id="_totalRec" value="<%=pageInfo.getTotalRec() %>" />
		<div id="container" class="container">
		<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" >
			<div>
				<form name="pageForm" action="<%=path%>/cwc_usermanger.htm?method=find" method="post" id="pageForm">
				<div id="getloginUser" style="display:none;"></div>
				<div class="buttons">
						<div id="toggleDiv"></div>
				</div>		
				<%-- 查询条件开始--%>
				<div id="condition" style="display: block;">
					<table>
						<tbody>
							<tr>
								<td><emp:message key="wexi_qywx_wxgzlb_text_1" defVal="微信编号："
											fileName="weix"></emp:message></td>
								<td>
									<input name="wcid" value="<%= ("".equals(wcid)? "":wcid) %>" style="width:220px"/>
								</td>
								<td><emp:message key="wexi_qywx_wxgzlb_text_2" defVal="手机号："
											fileName="weix"></emp:message></td>
								<td>
									<input name="phone" value="<%= ("".equals(phone)? "":phone) %>" style="width:220px"/>
								</td>
								<td></td>
								<td>
								</td>
								<td class="tdSer">
								    <center><a id="search" ></a></center>
								</td>	
							</tr>
							<tr>
								<td><emp:message key="wexi_qywx_wxgzlb_text_3" defVal="客户名称："
											fileName="weix"></emp:message></td>
								<td>
									<input name="uname" value="<%= ("".equals(uname)? "":uname) %>" style="width:220px"/>
								</td>
								<td ><span><emp:message key="wexi_qywx_wxgzlb_text_4" defVal="认证时间："
											fileName="weix"></emp:message></span></td>
								<td >
						            <label>
                                      <input id="submitSartTime" type="text" value='<%= "".equals(beginTime) ? "":beginTime %>' name="beginTime" style="cursor: pointer; background-color: white;width: 220px;" class="Wdate input_bd" readonly="readonly" onclick="stime()" class="input_book">
                                    </label>
                                    <span><emp:message key="wexi_qywx_wxgzlb_text_5" defVal="至"
											fileName="weix"></emp:message></span>
                                    <label> <input id="submitEndTime" name="endTime"  type="text" value='<%= "".equals(endTime) ? "":endTime %>' style="cursor: pointer; background-color: white;width: 220px;" class="Wdate input_bd" readonly="readonly" onclick="rtime()" class="input_book">
                                    </label>
								</td>
								<td></td>	
								<td></td>
								<td></td>
							</tr>
						</tbody>
					</table>
				</div>
		<%-- 查询条件结束 --%>
		
		<div id="resultSet">
			<table id="content">
				<thead>
					<tr>
						<th>
							<emp:message key="wexi_qywx_wxgzlb_text_6" defVal="微信编号"
											fileName="weix"></emp:message>
						</th>
						<th>
							<emp:message key="wexi_qywx_wxgzlb_text_7" defVal="关注ID"
											fileName="weix"></emp:message>
						</th>
						<th>
							<emp:message key="wexi_qywx_wxgzlb_text_8" defVal="微信名称"
											fileName="weix"></emp:message>
						</th>
						<th>
							<emp:message key="wexi_qywx_wxgzlb_text_9" defVal="手机号"
											fileName="weix"></emp:message>
						</th>
						<th>
							<emp:message key="wexi_qywx_wxgzlb_text_10" defVal="客户编号"
											fileName="weix"></emp:message>
						</th>
						<th>
							<emp:message key="wexi_qywx_wxgzlb_text_11" defVal="客户名称"
											fileName="weix"></emp:message>
						</th>
						<th>
							<emp:message key="wexi_qywx_wxgzlb_text_12" defVal="关注时间"
											fileName="weix"></emp:message>
						</th>
						<th>
							<emp:message key="wexi_qywx_wxgzlb_text_13" defVal="认证时间"
											fileName="weix"></emp:message>
						</th>
					</tr>
				</thead>
				<tbody id="tbs">
				<%
				@SuppressWarnings("unchecked")
				List<DynaBean> beans = (List<DynaBean>)request.getAttribute("userlist");
				if(beans!=null && beans.size()>0)
				{
					for(DynaBean bean:beans)
					{
				%>
							<tr>
								<td>
							    <% if(bean.get("wcid")!=null&&!"".equals(String.valueOf(bean.get("wcid")).trim())){ %>
                            		 <%= bean.get("wcid")%>
	                             <%} else {%>
	                             	 	-
	                             <%}%>
								</td>
								<td>
							    <% if(bean.get("openid")!=null&&!"".equals(String.valueOf(bean.get("openid")).trim())){ %>
                            		 <%= bean.get("openid")%>
	                             <%} else {%>
	                             	 	-
	                             <%}%>
								</td>
								<td>
							    <% if(bean.get("username")!=null&&!"".equals(String.valueOf(bean.get("username")).trim())){ %>
                            		 <%= bean.get("username")%>
	                             <%} else {%>
	                             	 	-
	                             <%}%>
								</td>
								<td >
								<% if(bean.get("phone")!=null&&!"".equals(String.valueOf(bean.get("phone")).trim())){ %>
                            	 	<%= bean.get("phone")%>
	                             <%} else {%>
	                             	 	-
	                             <%}%>
								</td>
								<td >
								 <% if(bean.get("client_code")!=null&&!"".equals(String.valueOf(bean.get("client_code")).trim())){ %>
                            	 	<%= bean.get("client_code")%>
	                             <%} else {%>
	                             	 	-
	                             <%}%>
								</td>
								<td >
								 <% if(bean.get("uname")!=null&&!"".equals(String.valueOf(bean.get("uname")).trim())){ %>
                            	 	<%= bean.get("uname")%>
	                             <%} else {%>
	                             	 	-
	                             <%}%>
								</td>
								<td >
								     <%if(bean.get("createtime")!=null&&!"".equals(bean.get("createtime"))){ %>
	                             	 	<%= formatter.format(bean.get("createtime"))%>
	                             	 <%} else {%>
	                             	 	-
	                             	<%}%>
								</td>
								<td >
								     <%if(bean.get("verifytime")!=null&&!"".equals(bean.get("verifytime"))){ %>
	                             	 	<%= formatter.format(bean.get("verifytime"))%>
	                             	 <%} else {%>
	                             	 	-
	                             	<%}%>
								</td>
							</tr>
							<%
							}
						}else{
							%>
						<tr>
						<td colspan="13">
							<emp:message key="weix_qywx_gzzhgl_text_10" defVal="无记录"
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
			
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=iPath %>/js/userm.js"></script>
	</body>
	
</html>
