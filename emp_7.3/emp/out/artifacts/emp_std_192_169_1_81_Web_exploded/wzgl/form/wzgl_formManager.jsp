<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.form.LfFomInfo"%>
<%@page import="com.montnets.emp.entity.form.LfFomType"%>
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
	String menuCode = titleMap.get("formManager");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
    @ SuppressWarnings("unchecked")
    LinkedHashMap<String,String> conditionMap = (LinkedHashMap<String,String>)request.getAttribute("conditionMap");
    @ SuppressWarnings("unchecked")
    List<LfFomInfo> otfomInfoList = (List<LfFomInfo>)request.getAttribute("otFomInfoList");
    
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
    //查询的表单标题
    String formtitle = "";
    String startdate = "";
    String enddate = "";
    if(conditionMap != null){
        formtitle = conditionMap.get("title&like")==null?"":conditionMap.get("title&like");
        //起始时间
        startdate = conditionMap.get("createtime&>")==null?"":conditionMap.get("createtime&>");;
        //结束时间
         enddate = conditionMap.get("createtime&<")==null?"":conditionMap.get("createtime&<");;
    }
	 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" href="<%=commonPath%>/common/css/global.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>">
			<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<form name="pageForm" action="wzgl_formManager.htm?method=find" method="post" id="pageForm">
		<div id="condition">
				<div class="hd cc">
					<dl class="dl-list">
							<dt>
									<emp:message key="wzgl_qywx_form_text_1" defVal="表单名称："
											fileName="wzgl" />
							</dt>
							<dd>
								<input type="text" id="formtitle" name="formtitle" maxlength="16" size="16"       
								value="<%=formtitle%>" >
							</dd>	
							<dt>
								<emp:message key="wzgl_qywx_form_text_2" defVal="创建时间："
											fileName="wzgl" />
							</dt>
							<dd>
									<input type="text" id="submitSartTime" name="startdate" 
									style="cursor: pointer; width: 150px;background-color: white;" 
									class="Wdate" readonly="readonly" onclick="stime()" value='<%=startdate%>'>
							</dd>
							<dt>
								<emp:message key="wzgl_qywx_form_text_3" defVal="至："
											fileName="wzgl" />
							</dt>
							<dd>
								<input type="text" id="submitEndTime" name="enddate" 
									style="cursor: pointer; width: 150px;background-color: white;" 
									class="Wdate" readonly="readonly" onclick="rtime()" value='<%=enddate%>'>
							</dd>
							
					</dl>
					<a class="btn-search fr" id="search"><emp:message key="common_btn_3" defVal="查询"
											fileName="common" /></a>
				</div>
			</div>
		<div class="container">
			<input id="pathUrl" value="<%=path %>" type="hidden" />
			<div style="display:none" id="getloginUser"></div>
			<div class="hd">
				<a href="javascript:handleForm('add','')"><emp:message key="wzgl_qywx_form_text_9" defVal="创建表单"
											fileName="wzgl" /></a>
            </div>
            <div class="bd">
                <table id="content">
                    <thead>
                        <tr>
                        	<th><emp:message key="wzgl_qywx_form_text_4" defVal="表单编号"
											fileName="wzgl" /></th>
                            <th><emp:message key="wzgl_qywx_form_text_5" defVal="表单名称"
											fileName="wzgl" /></th>
                            <th><emp:message key="wzgl_qywx_form_text_10" defVal="表单描述"
											fileName="wzgl" />  </th>
                            <th><emp:message key="wzgl_qywx_form_text_6" defVal="创建时间"
											fileName="wzgl" />  </th>
                            <th colspan="4"> <emp:message key="common_text_14" defVal="操作"
											fileName="common" /></th>
                        </tr>
                    </thead>
                    <tbody>
                    	<% 
                        		if(otfomInfoList != null && otfomInfoList.size()>0){
                        		    LfFomInfo fomInfo = null;
                        		    for(int i=0;i<otfomInfoList.size();i++){
                        		        fomInfo = otfomInfoList.get(i);
                        	%>
                        		<tr>
                        			<td align="center">
                        				<%=fomInfo.getFId() %>
                        			</td>
                        			<td>
                        			    <div class="titleTip" style="display:none"><%=fomInfo.getTitle().replaceAll("<","&lt;").replaceAll(">","&gt;") %></div>
                        				<%--  <a href="javascript:openShowDialog('formtitle<%=fomInfo.getFId()%>','表单名称')">--%>
                        				<%
                        					String title = fomInfo.getTitle();
                        					if(title != null && title.length()>6){
                        					    title = title.substring(0,6)+"...";
                        					}else if(title == null || title.length() == 0){
                        					    title = "-";
                        					}
                        					out.print(title);
                        				%>
                        				<div id="formtitle<%=fomInfo.getFId()%>" style="display: none;width:300px;">
                        					<%=fomInfo.getTitle().replaceAll("<","&lt;").replaceAll(">","&gt;") %>
                        				 </div>
                        			</td>
                        		
                        			<td>
                        			    <div class="titleTip" style="display:none"><%=fomInfo.getNote().replaceAll("<","&lt;").replaceAll(">","&gt;") %></div>
                        				<%--  <a href="javascript:openShowDialog('formnote<%=fomInfo.getFId()%>','表单描述')">--%>
                        				<%
                        					String note = fomInfo.getNote();
                        					if(note != null && note.length()>6){
                        					    note = note.substring(0,6)+"...";
                        					}else if(note == null || note.length() == 0){
                        					    note = "-";
                        					}
                        					out.print(note);
                        				%>
                        				<div id="formnote<%=fomInfo.getFId()%>" style="display: none;width:360px;height:160px;">
                        				 <textarea style="word-break: break-all;width:100%;height:100%;" readonly="readonly"><%=fomInfo.getNote().replaceAll("<","&lt;").replaceAll(">","&gt;") %></textarea>
                        				 </div>
                        			</td>
                        			 <td>
                        			 	<%=formatter.format(fomInfo.getCreatetime())%>
		                            </td>
                        			<td colspan="4" align="center">
                        				<a href="javascript:handleForm('copy','<%=fomInfo.getFId() %>')"  class="ml10"><emp:message key="common_btn_14" defVal="复制"
											fileName="common" /> </a>
                        				&nbsp;&nbsp;
                        				<a href="javascript:handleForm('edit','<%=fomInfo.getFId() %>')"  class="ml10"><emp:message key="common_text_7" defVal="编辑"
											fileName="common" /> </a>
                        				&nbsp;&nbsp;
                        				<a href="javascript:handleForm('preview','<%=fomInfo.getFId() %>')"  class="ml10"> <emp:message key="common_text_6" defVal="预览"
											fileName="common" /></a>
                        				<%--
                        				 &nbsp;&nbsp;
                        				<a href="javascript:handleForm('link','<%=fomInfo.getFId() %>')"  class="ml10">模拟提交表单</a>
                        				 &nbsp;&nbsp;
                        				<a href="javascript:handleForm('statistics','<%=fomInfo.getFId() %>')"  class="ml10">统计</a> --%>
                        				&nbsp;&nbsp;
                        				<a href="javascript:handleForm('del','<%=fomInfo.getFId() %>')"  class="ml10"><emp:message key="common_text_8" defVal="删除 "
											fileName="common" /> </a>
                        			</td>
                        		</tr>
                        	<% 
                        		    }
                        		}else{
                        		    
                        	%>
                            	<tr><td align="center" colspan="8"><emp:message key="wzgl_qywx_form_text_8" defVal="无记录"
											fileName="wzgl" /></td></tr>
                        	<% 
                        		    
                        		}
                        	%>
                    </tbody>
                     <tfoot>
                        <tr>
                            <td colspan="8">
                                <div id="pageInfo">
                                </div>
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>

		</div>	
		</form>
		
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wzgl_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/artDialog.js?skin=default"></script>
  	<script type="text/javascript" src="<%=commonPath %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/formManager.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	
    <script>
         currentTotalPage="<%=pageInfo.getTotalPage()%>";
         currentPageIndex="<%=pageInfo.getPageIndex()%>";
         currentPageSize="<%=pageInfo.getPageSize()%>";
         currentTotalRec="<%=pageInfo.getTotalRec()%>";
    </script>
	</body>
</html>
