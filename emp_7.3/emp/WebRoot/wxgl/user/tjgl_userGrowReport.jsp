<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
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
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    @SuppressWarnings("unchecked")
    List<DynaBean> userbeans = (List<DynaBean>)request.getAttribute("userbeans");
    @SuppressWarnings("unchecked")
	List<LfWeiAccount> otWeiAccList=(List<LfWeiAccount>)request.getAttribute("otWeiAccList");
    PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");
    if(null == pageInfo)
    {
        pageInfo = new PageInfo();
    }
    
    String aid= (String)request.getParameter("aid");
    if(null == aid || "".equals(aid))
    {
    	aid="-1";
    }
    
    String weixBasePath = request.getScheme() + "://" + request.getServerName() + path + "/";
    
    //使用集群，文件服务器的地址
    String filePath = GlobalMethods.getWeixFilePath();
    
    //起始时间
    String startdate = request.getParameter("startdate");
    //结束时间
    String enddate = request.getParameter("enddate");
    
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css"
			href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
              type="text/css" />
		<link rel="stylesheet" href="<%=iPath%>/css/tjgl_userStatistics.css?V=<%=StaticValue.getJspImpVersion() %>">
		<style type="text/css">
#content thead th.le {
	text-align: left;
	padding-left: 4px;
}

#timediv a#search {
	cursor: pointer;
}
</style>
	<%if(StaticValue.ZH_HK.equals(langName)){%>
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
	<link rel="stylesheet" href="<%=iPath%>/css/user_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>">
	<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	</head>
	<body>
		<%-- header结束 --%>
		<%-- 内容开始 --%>

		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName, menuCode) %>
			<div id="rContent" class="rContent">
				<form name="userGrouwForm" action="" method="post" id="pageForm">
					<div style="display: none" id="hiddenValueDiv"></div>
					<input type="hidden" name="tp" value="follow" />
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
										<emp:message key="wxgl_yhgl_title_1" defVal="公众帐号:" fileName="wxgl"/>
									</td><td>
										<select id="aid" name="aid" class="input_bd">
											<%
					    if(null != otWeiAccList && otWeiAccList.size()>0){
                              for(LfWeiAccount acct : otWeiAccList){
                              String aId  = String.valueOf(acct.getAId());
					%>
											<option value="<%=acct.getAId() %>"
												<%=(aid.equals(aId))?"selected":"" %>>
												<%=acct.getName() %></option>
											<%
						}
					}
					%>
										</select>
								</tr>
							</tbody>
						</table>
						<table>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th colspan="4" class="le">
									<emp:message key="wxgl_yhgl_title_2" defVal="关注指标" fileName="wxgl"/>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>
									<span><emp:message key="wxgl_yhgl_title_3" defVal="新增关注人数" fileName="wxgl"/></span>
									<span><br /> <emp:message key="wxgl_yhgl_title_4" defVal="日:" fileName="wxgl"/><label id="todaySubCount"></label> <br />
										<emp:message key="wxgl_yhgl_title_5" defVal="周:" fileName="wxgl"/><label id="weekSubCount"></label> <br /> 
										<emp:message key="wxgl_yhgl_title_6" defVal="月:" fileName="wxgl"/><label
											id="monthSubCount"></label> </span>
								</td>
								<td>
									<span><emp:message key="wxgl_yhgl_title_7" defVal="取消关注人数" fileName="wxgl"/></span>
									<span><br /><emp:message key="wxgl_yhgl_title_4" defVal="日:" fileName="wxgl"/><label id="todayCancelCount"></label> <br />
										 <emp:message key="wxgl_yhgl_title_5" defVal="周:" fileName="wxgl"/><label id="weekCancelCount"></label> <br />
										<emp:message key="wxgl_yhgl_title_6" defVal="月:" fileName="wxgl"/> <label
											id="monthCancelCount"></label> </span>
								</td>
								<td>
									<span><emp:message key="wxgl_yhgl_title_8" defVal="净增关注人数" fileName="wxgl"/></span>
									<span><br> <emp:message key="wxgl_yhgl_title_4" defVal="日:" fileName="wxgl"/><label id="todayNetGrowthCount"></label>
										<br /> <emp:message key="wxgl_yhgl_title_5" defVal="周:" fileName="wxgl"/><label id="weekNetGrowthCount"></label> <br />
										<emp:message key="wxgl_yhgl_title_6" defVal="月:" fileName="wxgl"/> <label
											id="monthNetGrowthCount"></label> </span>
								</td>
								<td>
									<span><emp:message key="wxgl_yhgl_title_9" defVal="累积关注人数" fileName="wxgl"/></span>
									<span><br /> <label id="totalSubscriberCount"></label>
										<br /> <label></label> <br /> <label></label> <br /> </span>
								</td>
							</tr>
						</tbody>
					</table>

					<div class="bd">
						<div class="contentkey tab1">
							<div class="tab">
								<ul>
									<li class="key"
										onclick="javascript:findUserGrowthInfo('growthdiv');">
										<a class="a-tab" data-tp="follow"><emp:message key="wxgl_yhgl_title_3" defVal="新增关注人数" fileName="wxgl"/></a>
									</li>
									<li onclick="javascript:findUserGrowthInfo('growthdiv');">
										<a class="a-tab" data-tp="unfollow"><emp:message key="wxgl_yhgl_title_7" defVal="取消关注人数" fileName="wxgl"/></a>
									</li>
									<li onclick="javascript:findUserGrowthInfo('growthdiv');">
										<a class="a-tab" data-tp="income"><emp:message key="wxgl_yhgl_title_8" defVal="净增关注人数" fileName="wxgl"/></a>
									</li>
								</ul>
							</div>
							<div class="itemList">
								<div class="item tab1">
									<div style="float: left; width:<%=StaticValue.ZH_HK.equals(langName)?"440px":"250px" %> 250px; height: 40px; line-height: 40px;">
										<input name="newGrowth" type="radio" value="2"
											checked="checked"
											onclick="javascript:findUserGrowthInfo('growthdiv');">
										<emp:message key="wxgl_yhgl_title_10" defVal="本周关注数" fileName="wxgl"/>
										<input name="newGrowth" type="radio" value="3"
											onclick="javascript:findUserGrowthInfo('growthdiv');">
										<emp:message key="wxgl_yhgl_title_11" defVal="本月关注数" fileName="wxgl"/>
										<input name="newGrowth" type="radio" value="1">
										<emp:message key="wxgl_yhgl_title_12" defVal="日期" fileName="wxgl"/>
									</div>
									<div id="timediv" class="timedivclass">
										<emp:message key="wxgl_yhgl_title_13" defVal="开始时间：" fileName="wxgl"/>
										<input type="text" id="submitSartTime" name="startdate"
											style="cursor: pointer; background-color: white;"
											class="Wdate" readonly="readonly" onclick="stime()"
											value='<%=startdate == null ? "" : startdate%>'>
										<emp:message key="wxgl_yhgl_title_14" defVal="至" fileName="wxgl"/>
										<input type="text" id="submitEndTime" name="enddate"
											style="cursor: pointer; background-color: white;"
											class="Wdate" readonly="readonly" onclick="rtime()"
											value='<%=enddate == null ? "" : enddate%>'>
										&nbsp;&nbsp;

										<a id="search"><emp:message key="wxgl_yhgl_title_15" defVal="查询" fileName="wxgl"/></a>

									</div>
									<br />
									<br />
									<br />
									<div id="growthdiv" align="center"></div>
								</div>
								<div class="item tab2">
									tab2<emp:message key="wxgl_yhgl_title_16" defVal="内容" fileName="wxgl"/>
								</div>
								<div class="item tab3">
									tab3<emp:message key="wxgl_yhgl_title_16" defVal="内容" fileName="wxgl"/>
								</div>
								<div class="item tab4">
									tab4<emp:message key="wxgl_yhgl_title_16" defVal="内容" fileName="wxgl"/>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
		<div id="pageStuff" style="display: none;">
			<input type="hidden" id="pathUrl" value="<%=path%>" />
			<input type="hidden" id="iPathUrl" value="<%=iPath%>" />
			<input type="hidden" id="lguserid" value="<%=lguserid%>" />
			<input type="hidden" id="lgcorpcode" value="<%=lgcorpcode%>" />
		</div>
		<div class="clear"></div>
		<div id="updateUserTmpDiv" title='<emp:message key="wxgl_yhgl_title_17" defVal="详细信息" fileName="wxgl"/>' style="padding: 5px; display: none">
			<iframe id="updateUserTmpFrame" name="updateUserTmpFrame"
				style="width: 650px; height: 540px; border: 0;" marginwidth="0"
				scrolling="no" frameborder="no"></iframe>
		</div>
		
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script src="<%=commonPath%>/wxcommon/js/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath%>/wxcommon/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=path %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
		<script type="text/javascript"
			src="<%=path %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/wxcommon/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=path%>/wxcommon/widget/graphical/FusionCharts.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=path%>/wxcommon/widget/graphical/table3.0.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=path%>/wxcommon/widget/graphical/Chart.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=iPath%>/js/tjgl_userStatistics.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
	$('#aid').isSearchSelect({'width':'145','isInput':false,'zindex':0},function(){
		findUserGrowthInfo('growthdiv');
	});
    var pathUrl="<%=path%>",
        iPathUrl="<%=iPath%>",
        currentTotalPage="<%=pageInfo.getTotalPage()%>",
        currentPageIndex="<%=pageInfo.getPageIndex()%>",
        currentPageSize="<%=pageInfo.getPageSize()%>",
        currentTotalRec="<%=pageInfo.getTotalRec()%>",
        lguserid=<%=lguserid%>,
        lgcorpcode="<%=lgcorpcode%>";
    $(document).ready(function() {          
        getLoginInfo("#hiddenValueDiv");  
    	findUserGrowthInfo('growthdiv');
        $('#search').click(function(){            
        	findUserGrowthInfo('growthdiv');
        });
    });

	//切换选项卡
    $(".a-tab").click(function () {
         $(this).parent().siblings().removeClass("key").end().addClass("key"); 
         $("input[name='tp']").val($(this).attr('data-tp'));
    });
    
	$("input[name='newGrowth']").bind("click",function(){
        var newGrowth = $("input[name='newGrowth']:checked").val();
        if(newGrowth=="1"){
        	$("#timediv").css({display:"block"});
        }
        else
        {
        	$("#timediv").css({display:"none"});
        }
	});
	</script>
	</body>
</html>