<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.ottbase.util.StringUtils"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiGroup"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>)session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>)session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String)session.getAttribute("stlyeSkin");
    String wxskin = skin.indexOf("/frame") == -1 ? "default" : skin.substring(skin.indexOf("/frame")+1, skin.length());
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    @SuppressWarnings("unchecked")
    List<DynaBean> groupBeans = (List<DynaBean>)request.getAttribute("groupbeans");
	List<LfWeiAccount> otWeiAccList=(List<LfWeiAccount>)request.getAttribute("otWeiAccList");
    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo)request.getAttribute("pageInfo");
    String aid= String.valueOf(request.getParameter("aid"));
    if(null == aid || "".equals(aid)|| "null".equals(aid))
    {
    	aid="-1";
    }
    String gid= String.valueOf(request.getParameter("gid"));
    if(null == gid || "".equals(gid)|| "null".equals(gid))
    {
    	gid="-1";
    }
    //为了页面保存用户查询条件用
    String gname = String.valueOf(request.getParameter("gname"));
    
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    String weixBasePath = request.getScheme() + "://" + request.getServerName() + path + "/";
    
    //使用集群，文件服务器的地址
    String filePath = GlobalMethods.getWeixFilePath();
    //MP企业微信服务协议
    String approve = (String)session.getAttribute("approve");
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>">
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
		<script type="text/javascript">
		$(document).ready(function(){
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$('#searchIcon').attr('src', '<%=inheritPath%>/images/toggle_expand.png');
				$('#searchIcon').attr('title', getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_66"));
			}, function() {
				$("#condition").show();
				$('#searchIcon').attr('src', '<%=inheritPath%>/images/toggle_collapse.png');
				$('#searchIcon').attr('title', getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_67"));
			});
		});
		</script>
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
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<div id="rContent" class="rContent">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<a id="add" href="javascript:showAddGroupTmp('1')"><emp:message key="wxgl_gzhgl_title_84" defVal="新增" fileName="wxgl"/></a>
						<input type="hidden" id="menucode" value="21" name="menucode" />
						<input type="hidden" name="pageTotalRec" id="pageTotalRec" value="" />

					</div>
			<form name="pageForm" action="user_groupManager.htm?method=findGroupInfoList" method="post" id="pageForm">
				<div style="display:none" id="hiddenValueDiv">
				</div>
				<div id="condition" style="display: block;">
			
					<div class="hd cc">
					<table>
						<tr>
							<td><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/></td>
							<td><select id="aid" name="aid" class="input_bd">
								<option value=""><emp:message key="wxgl_gzhgl_title_63" defVal="全部" fileName="wxgl"/></option>
								<%
								if(null != otWeiAccList && otWeiAccList.size()>0){
	                                for(LfWeiAccount acct : otWeiAccList){
	                                String aId  = String.valueOf(acct.getAId());
								%>
								<option value="<%=acct.getAId() %>" <%=(aid.equals(aId))?"selected":"" %>> <%=acct.getName() %></option>
								<%
									}
								}
								%>
							</select></td>
							<td><emp:message key="wxgl_gzhgl_title_80" defVal="群组名称：" fileName="wxgl"/></td>
							<td><input id="gname" name="gname" value="<%=(null!=gname&&""!=gname&&"null"!=gname)?gname:"" %>"></td>
							<td class="tdSer">
												<center><a id="search"></a></center>
											</td>			
					
						</tr>
						</table>
					</div>
				</div>
				<div id="container" class="container">
					<div class="bd">
	                    <table id="content">
	                        <thead>
	                        <tr>
	                            <%-- th>
	                              	<input type="checkbox" name="checkall" id="selectAll" onclick="checkAlls(this,'checklist')" />
	                            </th --%>
	                            <th>
	                              	<emp:message key="wxgl_gzhgl_title_85" defVal="群组名称" fileName="wxgl"/>
	                            </th>
	                            <th>
	                              	<emp:message key="wxgl_gzhgl_title_86" defVal="群组人数" fileName="wxgl"/>
	                            </th>
	                            <th>
	                              	<emp:message key="wxgl_gzhgl_title_87" defVal="所属公众帐号" fileName="wxgl"/>
	                            </th>
	                            <th>
	                              	<emp:message key="wxgl_gzhgl_title_6" defVal="创建时间" fileName="wxgl"/>
	                            </th>
	                            <th >
	                              	  <emp:message key="wxgl_gzhgl_title_7" defVal="操作" fileName="wxgl"/>
	                            </th>
	                        </tr>
	                        </thead>
	                        <tbody>
	                        <%
	                            if(null != groupBeans && groupBeans.size()>0){
	                                for(DynaBean bean : groupBeans){
	                        %>
	                        <tr>
	                        	<%-- td>
								<input type="checkbox" name="checklist" value="<%=bean.get("gid") %>" />
	                           </td --%>
	                            <td>
	                           		<%
									String groupname=String.valueOf(bean.get("gname"));
									if(null == groupname || "".equals(groupname)||"null".equals(groupname))
									{
									    out.print("-");
									}else{ 
									%>
										<xmp><%=groupname.length()>8?groupname.substring(0,8)+"...":groupname %></xmp>
									<%} %>
	                           </td>
	                            <td>
	                           		<%
									String gcount=String.valueOf(bean.get("gcount"));
									if(null == gcount || "".equals(gcount)|| "null".equals(gcount))
									{
									    out.print("0");
									}else{ 
									%>
										<xmp><%=gcount.length()>8?gcount.substring(0,8)+"...":gcount %></xmp>
									<%} %>
	                           </td>
	                            <td>
	                                <%
	                               		String acctName = "";
										String groupAId=String.valueOf(bean.get("aid"));
										if(null != otWeiAccList && otWeiAccList.size()>0)
										{
			                                for(LfWeiAccount acct : otWeiAccList)
			                                {
				                                String aId  = String.valueOf(acct.getAId());
				                                if(groupAId.equals(aId))
				                                {
				                                    acctName = acct.getName();
				                                    break;
				                                }
											}
										}
									%>
									<%=acctName %>
	                            </td>
	                            <td>
	                                <%=formatter.format(bean.get("createtime"))%>
	                            </td>
	                            <td>
	                            	<%-- a href="javascript:showupdateGroupTmp('<%=bean.get("gid") %>','1');">查看详情</a --%>
	                            	<a href="javascript:showupdateGroupTmp('<%=bean.get("gid") %>','2');"><emp:message key="wxgl_gzhgl_title_88" defVal="修改信息" fileName="wxgl"/></a>&nbsp;
	                            	<a href="javascript:showMember(<%=bean.get("gid") %>,<%=bean.get("aid") %>);"><emp:message key="wxgl_gzhgl_title_89" defVal="成员管理" fileName="wxgl"/></a>
	                            </td>
	                        </tr>
	                        <%
	                            }
	                        }else{
	                        %>
	                        <tr><td align="center" colspan="5"><emp:message key="wxgl_gzhgl_title_11" defVal="无记录" fileName="wxgl"/></td></tr>
	                        <%} %>
	                        </tbody>
	                        <tfoot>
		                        <tr>
		                            <td colspan="5">
		                                <div id="pageInfo"></div>
		                            </td>
		                        </tr>
	                        </tfoot>
	                    </table>
                	</div>
                </div>
            </form>
			</div>
			<div id="pageStuff" style="display:none;">
				<input type="hidden" id="pathUrl" value="<%=path%>" />
				<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
			</div>
			<%-- 内容结束 --%>
    	<div class="clear"></div>
    	<div id="updateGroupTmpDiv" title="<emp:message key='wxgl_gzhgl_title_90' defVal='群组信息' fileName='wxgl'/>" style="padding:5px;display:none">
			<iframe id="updateGroupTmpFrame" name="updateGroupTmpFrame" style="width:600px;height:200px;border:0;" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
		</div>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<link href="<%=path %>/wxcommon/css/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    	<link href="wxcommon/<%=wxskin %>/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    	 <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link href="<%=path %>/wxcommon/css/artDialogCss_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%}%>
		<script type="text/javascript">
		$('#aid').isSearchSelect({'width':'145','isInput':false,'zindex':0});		
		var pathUrl="<%=path%>",
        iPathUrl="<%=iPath%>",
        currentTotalPage="<%=pageInfo.getTotalPage()%>",
        currentPageIndex="<%=pageInfo.getPageIndex()%>",
        currentPageSize="<%=pageInfo.getPageSize()%>",
        currentTotalRec="<%=pageInfo.getTotalRec()%>",
        lguserid=<%=lguserid%>,
        lgcorpcode="<%=lgcorpcode%>";
		$(function() {			
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
			
			//分页
			initPage(currentTotalPage,currentPageIndex,currentPageSize,currentTotalRec);
			//搜索
			$('#search').click(function(){
				//如何用户选择了群组未选择公共账号，这需要提示用户必须选择公共账号
				var gname = $("#gname").val();
				var acctid = $("#aid").val();
				if((gname!=null && gname!="") && (acctid==null || acctid==""))
				{
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_38"));
					return;
				}
				submitForm();
			});

		});			
		</script>
		<script type="text/javascript" src="<%=iPath%>/js/yhgl_groupManager.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>