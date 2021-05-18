<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.entity.lbs.LfLbsPios"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.ottbase.constant.WXStaticValue"%>
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
	String menuCode = titleMap.get("lbsManager");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @ SuppressWarnings("unchecked")
    List<LfLbsPios> lbsPiosList = (List<LfLbsPios>)request.getAttribute("lbsPiosList");
    @ SuppressWarnings("unchecked")
    List<LfWeiAccount> accountList = (List<LfWeiAccount>)request.getAttribute("accountList");
    @ SuppressWarnings("unchecked")
    LinkedHashMap<Long,LfWeiAccount> accountMap = (LinkedHashMap<Long,LfWeiAccount>)request.getAttribute("accountMap");
    @ SuppressWarnings("unchecked")
    LinkedHashMap<String,String> conditionMap = (LinkedHashMap<String,String>)request.getAttribute("conditionMap");
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>

		<%}%>
		<style type="text/css">
			.c_selectBox, #condition .c_selectBox ul, #condition .c_selectBox ul li{
				width:208px!important;
			}
		</style>
	</head>
	<body>
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<div id="container" class="container">
	<%-- 当前位置 --%>
	<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		<%if(btnMap.get(menuCode+"-0")!=null) { %>
		<div id="rContent" class="rContent">
			<input id="pathUrl" value="<%=path %>" type="hidden" />
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=WXStaticValue.ISCLUSTER %>">
			<form name="pageForm" action="weix_lbsManager.htm?method=find" method="post" id="pageForm">
				<div  style="display:none" id="getloginUser"></div>
				<div class="buttons">
					<div id="toggleDiv">
					</div>
					<a href="javascript:toPushSet()" id="addeployee"><emp:message key="wxgl_gzhgl_title_133" defVal="推送配置" fileName="wxgl"/></a>
					<a href="javascript:handleLbs('add','')" class="ml10" id="addeployee"><emp:message key="wxgl_gzhgl_title_134" defVal="添加位置" fileName="wxgl"/></a>
				</div>
				<div id="condition">
					<table >
						<tr>
						   <td ><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/></td>
						   <td>
						   <select name="pubAccounts" id="pubAccounts">
								<option value=""><emp:message key="wxgl_gzhgl_title_135" defVal="请选择公众帐号" fileName="wxgl"/></option>
										<% 
											if(accountList != null && accountList.size()>0){
											    LfWeiAccount account = null;
											    for(int j=0;j<accountList.size();j++){
											        account = accountList.get(j);
										%>
											<option value="<%=account.getAId() %>"
												<% if(conditionMap != null && conditionMap.get("AId") != null && account.getAId().toString().equals(conditionMap.get("AId"))){
													    out.print("selected='selected'");}
												%>><%=account.getName() %></option>
										<% 
											    }
											}
										%>
							</select>
						   </td>
				   			<td>
									<emp:message key="wxgl_gzhgl_title_126" defVal="关键字：" fileName="wxgl"/>
								</td>
								<td>
									<input type="text" id="keyword" name="keyword" maxlength="16" size="16"
									value="<%if(conditionMap != null && conditionMap.get("keyword&like") != null){ out.print(conditionMap.get("keyword&like"));} %>" >
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
                            <th><emp:message key="wxgl_gzhgl_title_136" defVal="名称（标题/主题）" fileName="wxgl"/></th>
                            <th><emp:message key="wxgl_gzhgl_title_137" defVal="关键字" fileName="wxgl"/></th>
                            <th>  <emp:message key="wxgl_gzhgl_title_138" defVal="联系方式" fileName="wxgl"/></th>
                            <th><emp:message key="wxgl_gzhgl_title_139" defVal="简介" fileName="wxgl"/></th>
                            <th>  <emp:message key="wxgl_gzhgl_title_66" defVal="公众帐号" fileName="wxgl"/></th>
                            <th colspan="4"> <emp:message key="wxgl_gzhgl_title_7" defVal="操作 " fileName="wxgl"/></th>
                        </tr>
                    </thead>
                    <tbody>
                    	<% 
                        		if(lbsPiosList != null && lbsPiosList.size()>0){
                        		    LfLbsPios lbsPios = null;
                        		    for(int i=0;i<lbsPiosList.size();i++){
                        		        lbsPios = lbsPiosList.get(i);
                        	%>
                        		<tr>
                        		   <td>
                        				<%
                        					String title = lbsPios.getTitle();
                        					if(title != null && title.length()>6){
                        					    title = title.substring(0,6)+"...";
                        					}else if(title == null || title.length() == 0){
                        					    title = "-";
                        					}
                        					out.print(title);
                        				%>
                        				<div class="titleTip" style="display:none;width:300px;">
                        					<%=lbsPios.getTitle()%>
                        				</div>
                        			</td>
                        			<td>
                        				<%
                        					String keyword = lbsPios.getKeyword();
                        					if(keyword != null && keyword.length()>6){
                        					    keyword = keyword.substring(0,6)+"...";
                        					}else if(keyword == null || keyword.length() == 0){
                        					    keyword = "-";
                        					}
                        					out.print(keyword);
                        				%>
                        				<div class="titleTip" id="lbskeyword<%=lbsPios.getPid()%>" style="display: none;width:300px;">
                        					<xmp><%=lbsPios.getKeyword() %></xmp>
                        				</div>
                        			</td>
                        			<td>
                        				<div class="titleTip"><%=lbsPios.getTelephone() %></div>
                        			</td>
                        			<td>
                        				<%
                        					String note = lbsPios.getNote();
                        					if(note != null && note.length()>6){
                        					    note = note.substring(0,6)+"...";
                        					}else if(note == null || note.length() == 0){
                        					    note = "-";
                        					}
                        					out.print(note);
                        				%>
                        				<div class="titleTip" id="lbsnote<%=lbsPios.getPid()%>" style="display: none;width:360px;height:160px;">
                        				 <textarea style="word-break: break-all;width:100%;height:100%;" readonly="readonly"><%=lbsPios.getNote() %></textarea>
                        				 </div>
                        			</td>
                        			 <td>
                        			 	<div class="titleTip">
                        			 	<%
                        			 		if(null != accountMap && accountMap.size() > 0 && null!=accountMap.get(Long.valueOf(lbsPios.getAId())) ){
                        			 		    out.print(accountMap.get(Long.valueOf(lbsPios.getAId())).getName());
                        			 		}else{
                        			 		    out.print("-");
                        			 		}
                        			 	%>
                        			 	</div>
		                            </td>
                        			
                        			<td colspan="4">
                        				<a href="javascript:handleLbs('update','<%=lbsPios.getPid() %>')"  class="ml10"> <emp:message key="wxgl_button_12" defVal="编辑" fileName="wxgl"/></a>
                        				&nbsp;&nbsp;&nbsp;
                        				<a href="javascript:handleLbs('del','<%=lbsPios.getPid() %>')"  class="ml10">  <emp:message key="wxgl_button_10" defVal="删除" fileName="wxgl"/></a>
                        			</td>
                        		</tr>
                        	<% 
                        		    }
                        		}else{
                        		    
                        	%>
                            	<tr><td align="center" colspan="9"><emp:message key="wxgl_gzhgl_title_11" defVal="无记录" fileName="wxgl"/></td></tr>
                        	<% 
                        		    
                        		}
                        	%>
                    </tbody>
                     <tfoot>
                        <tr>
                            <td colspan="9">
                                <div id="pageInfo">
                                </div>
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
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
	<script src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
  	<script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?v=<%=WXStaticValue.OTT_VERSION %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?v=<%=WXStaticValue.OTT_VERSION %>"></script>
	<script src="<%=iPath%>/js/lbsManager.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script>
    	$('#pubAccounts,#catalogue').isSearchSelect({'width':'145','isInput':false,'zindex':0});
         currentTotalPage="<%=pageInfo.getTotalPage()%>";
         currentPageIndex="<%=pageInfo.getPageIndex()%>";
         currentPageSize="<%=pageInfo.getPageSize()%>";
         currentTotalRec="<%=pageInfo.getTotalRec()%>";
    </script>
	</body>
</html>
