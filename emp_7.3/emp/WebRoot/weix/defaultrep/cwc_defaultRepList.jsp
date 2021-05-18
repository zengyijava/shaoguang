<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.entity.weix.LfWcAccount"%>
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
	String shortInheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("defaultrep");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String lguserid = (String)request.getParameter("lguserid");
 	String lgcorpcode = (String)request.getParameter("lgcorpcode");
 	
 	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
 	
    @ SuppressWarnings("unchecked")
	List<LfWcAccount> lfWcAccList=(List<LfWcAccount>)request.getAttribute("lfWcAccList");
    @ SuppressWarnings("unchecked")
    List<DynaBean> beans = (List<DynaBean>)request.getAttribute("replyBeans");
    
    PageInfo pageInfo = new PageInfo();
    pageInfo=(PageInfo)request.getAttribute("pageInfo");
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    String a_id=request.getParameter("a_id");
    if(a_id==null || "".equals(a_id))
    {
    	a_id="-1";
    }
    
    String title = request.getParameter("title");
 %>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/frame.css"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/table.css"/>
		
		<link href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css" type="text/css" >	
		<style>
			#dropdownMenu span {
				float:none !important;
			}
		</style>
	</head>
	<body>
		<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="cwc_defaultrep.htm" method="post" id="pageForm">
			
					<div style="display:none" id="hiddenValueDiv"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1")!=null) { %>
						<a id="add" href="javascript:showAddAccountTmp(0)"><emp:message key="common_btn_4" defVal="新建"
											fileName="common"></emp:message></a>
						<%} %>
						<%if(btnMap.get(menuCode+"-2")!=null) { %>
						<a id="delete" onclick="javascript:del('all')"><emp:message key="common_btn_5" defVal="删除"
											fileName="common"></emp:message></a>
						<%} %>
					</div>
				<div id="condition" style="display: block;">
					<table>
						<tbody>
							<tr>
								<td><emp:message key="wexi_qywx_mrhf_text_1" defVal="公众帐号："
											fileName="weix"></emp:message></td>
								<td>
								<select id="a_id" name="a_id" class="input_bd" style="width:225px">
									<option value=""><emp:message key="wexi_qywx_mrhf_text_2" defVal="全部"
											fileName="weix"></emp:message></option>
									<%
			                            if(lfWcAccList != null && lfWcAccList.size()>0){
			                                for(LfWcAccount acct : lfWcAccList){
			                                String aId  = String.valueOf(acct.getAId());
			                        %>
									<option value="<%=acct.getAId() %>" <%=(a_id.equals(aId))?"selected":"" %>> <%=acct.getName() %></option>
									<%
										}
									}
									%>
								</select>
								</td>
								<td><emp:message key="wexi_qywx_mrhf_text_3" defVal="回复标题："
											fileName="weix"></emp:message></td>
								<td><input id="title" name="title" value="<%=title!=null?title:"" %>"></td>
								<td class="tdSer">
								    <center><a id="search" ></a></center>
								</td>	
							</tr>
						</tbody>
					</table>
				</div>
				<div id="resultSet">
                    <table id="content">
                        <thead>
                        <tr>
                                <th>
                              	<input type="checkbox" name="checkall" id="selectAll" onclick="checkAlls(this,'checklist')" />
                            </th>
                            <th>
                            	<emp:message key="wexi_qywx_mrhf_text_4" defVal="公众帐号"
											fileName="weix"></emp:message>
                            </th>
                            <th>
                            	<emp:message key="wexi_qywx_mrhf_text_5" defVal="回复标题"
											fileName="weix"></emp:message>
                            </th>
                            <th>
                              	<emp:message key="wexi_qywx_mrhf_text_6" defVal="回复内容"
											fileName="weix"></emp:message>
                            </th>
                            <th>
                              	<emp:message key="wexi_qywx_mrhf_text_7" defVal="创建时间"
											fileName="weix"></emp:message>
                            </th>
                            <th colspan="4">
                              	<emp:message key="wexi_qywx_mrhf_text_8" defVal="操作"
											fileName="weix"></emp:message>  
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            if(beans != null && beans.size()>0){
                                for(DynaBean bean : beans){
                        %>
                        <tr>
                        <td> 
							<input type="checkbox" name="checklist" value="<%=bean.get("tet_id") %>" />
                           </td>
                           <td>
                           		<%
								String accoutname=(String)bean.get("accoutname");
								if(accoutname==null || "".equals(accoutname)){out.print(MessageUtils.extractMessage("weix","wexi_qywx_mrhf_text_10",request));}else{ 
								%>
									<a onclick="javascript:modify(this,3)">
									<label style="display:none"><xmp><%=accoutname %></xmp></label>
									<xmp><%=accoutname.length()>8?accoutname.substring(0,8)+"...":accoutname %></xmp>
									</a> 
								<%} %>
                           </td>
                            <td>
                           		<%
								String titleStr=(String)bean.get("title");
								if(titleStr==null || "".equals(titleStr)){out.print("-");}else{ 
								%>
									<a onclick="javascript:modify(this,2)">
									<label style="display:none"><xmp><%=titleStr %></xmp></label>
									<xmp><%=titleStr.length()>8?titleStr.substring(0,8)+"...":titleStr %></xmp>
									</a> 
								<%} %>
                           </td>
                            <td>
                              	<%
								String msg_text=(String)bean.get("msg_text");
								if(msg_text==null || "".equals(msg_text)){out.print("-");}else{ 
								%>
									<a onclick="javascript:modify(this,1)">
									<label style="display:none"><xmp><%=msg_text %></xmp></label>
									<xmp><%=msg_text.length()>8?msg_text.substring(0,8)+"...":msg_text %></xmp>
									</a> 
								<%} %>
                            </td>
                            <td>
 								<%=bean.get("createtime")!=null?sdf.format(bean.get("createtime")):"" %>
                            </td>
                            <td>
                            	<a  onclick='preview(<%=bean.get("tet_id")%>)'><emp:message key="common_text_6" defVal="预览"
											fileName="common"></emp:message></a>
                            	<%if(btnMap.get(menuCode+"-3")!=null) { %>
                            	&nbsp;&nbsp;
								<a href="javascript:toUpdate(<%=bean.get("tet_id") %>)"><emp:message key="common_text_9" defVal="修改"	fileName="common"></emp:message></a>
								<% }%>
                               <%if(btnMap.get(menuCode+"-2")!=null) { %>
                               &nbsp;&nbsp;
                               <a onclick="javascript:del(<%=bean.get("tet_id") %>)"><emp:message key="common_text_8" defVal="删除"
											fileName="common"></emp:message></a>
                               <% }%>
                            </td>
                        </tr>
                        <%
                            }
                        }else{
                        %>
                        <tr><td align="center" colspan="9"><emp:message key="wexi_qywx_mrhf_text_9" defVal="无记录"
											fileName="weix"></emp:message>  </td></tr>
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
                </div>
            </form>
			</div>
			<%-- 内容结束 --%>
			<div id="divBox" style="display:none" title="<emp:message key="common_text_6" defVal="预览"
											fileName="common"></emp:message>">
				<div style="width:240px;height:460px;margin-left:30px; margin-top:-3px; background:url(<%=commonPath %>/common/img/iphone5.jpg);">
					<iframe style="background: white;width:198px;height:325px;margin-top: 65px;margin-left: 22px;" id="msgPreviewFrame" src=""></iframe>	
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
			<div id="pageStuff" style="display:none;">
				<input type="hidden" id="pathUrl" value="<%=path %>" />
				<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
				<input type="hidden" id="currentTotalPage" value="<%=pageInfo.getTotalPage()%>"/>
				<input type="hidden" id="currentPageIndex" value="<%=pageInfo.getPageIndex()%>"/>
				<input type="hidden" id="currentPageSize" value="<%=pageInfo.getPageSize()%>"/>
				<input type="hidden" id="currentTotalRec" value="<%=pageInfo.getTotalRec()%>"/>
			</div>
		</div>
    <div class="clear"></div>
    <div id="modify" title="<emp:message key="wexi_qywx_mrhf_text_11" defVal="信息内容"
											fileName="weix"></emp:message>"  style="padding:5px;width:300px;height:160px;display:none">
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
	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js"></script>				
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js"></script>
	<script type="text/javascript" src="<%=iPath%>/js/defaultRepList.js"></script>
	</body>
</html>