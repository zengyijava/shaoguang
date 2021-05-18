<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List" %>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	PageInfo pageInfo  = (PageInfo) request.getAttribute("pageInfo");
	List<DynaBean> list = (List<DynaBean>)request.getAttribute("list");
	//主题名称
    String taskname = StringUtils.defaultString(request.getParameter("taskname"));
    //发送内容
    String msg = StringUtils.defaultString(request.getParameter("msg"));
    //起始时间
    String starttime = StringUtils.defaultString(request.getParameter("starttime"));
    //结束时间
    String endtime = StringUtils.defaultString(request.getParameter("endtime"));
    //草稿箱类型
    String draftstype = StringUtils.defaultString(request.getParameter("draftstype"));
    String httpUrl = StaticValue.getFileServerViewurl();
    String langName = (String)session.getAttribute("emp_lang");
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=path%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=path%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=path %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link href="<%=path%>/ydwx/sendWX/css/wx_draftlist.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		<link href="<%=skin%>/wx_send.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
	</head>
	<body id="wx_draftlist">
		<div id="container" class="container">
			<%-- 内容开始 --%>
			<form name="pageForm" action="wx_send.htm?method=getDrafts" method="post" id="pageForm" autocomplete="off">
			<div id="loginInfo"></div>
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
            <input type="hidden" name="draftstype" value="<%=draftstype%>"/>
			<div id="rContent" class="rContent">
					<div id="condition">
						<table>
							<tr>
								<td>
									<span><emp:message key="ydwx_wxfs_jtwxfs_dxnr_cgx_td2" defVal="发送主题：" fileName="ydwx"></emp:message></span>
								</td>
								<td>
									<input type="text" name="taskname" id="taskname" value="<%=taskname%>"/>
								</td>
								
								<td>
									<span><emp:message key="ydwx_wxfs_jtwxfs_dxnr_cgx_td3" defVal="发送内容：" fileName="ydwx"></emp:message></span>
								</td>
								<td>
									<input type="text" name="msg" id="msg" value="<%=msg%>"/>
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
											<tr>
					<td>
						<span><emp:message key="ydwx_common_time_shijian" defVal="时间：" fileName="ydwx"></emp:message></span>
					</td>
					<td>
						<input type="text" readonly="readonly" class="Wdate" onclick="stime()" value="<%=starttime %>"  id="starttime" name="starttime" />
					</td>
					<td>
						<span><emp:message key="ydwx_common_time_zhi" defVal="至：" fileName="ydwx"></emp:message></span>
					</td>
					<td>
						<input type="text" readonly="readonly" class="Wdate"  onclick="rtime()" value="<%=endtime %>" id="endtime" name="endtime" />
					</td>
					<td></td>
				</tr>
						</table>
					</div>
					<table id="content">
						<thead>
					  <tr>
							<th>
								<emp:message key="ydwx_common_btn_xuanze" defVal="选择" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_common_bianhao" defVal="编号" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_jtwxfs_dxnr_cgx_td4" defVal="发送主题" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_jtwxfs_dxnr_cgx_td5" defVal="发送内容" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_wxfs_jtwxfs_dxnr_cgx_td6" defVal="接收号码" fileName="ydwx"></emp:message>
							</th>

							<th class="updateTime">
							      <emp:message key="ydwx_common_time_gengxinshijian" defVal="更新时间" fileName="ydwx"></emp:message>
							</th>
							<th>
								<emp:message key="ydwx_common_caozuo" defVal="操作" fileName="ydwx"></emp:message>
							</th>		
                            </tr>
                            </thead>
								<tbody>
								<%
								if (list != null && list.size() > 0)
								{
									for (DynaBean info : list)
									{
                                        String id = info.get("id").toString();
                                        String _taskname = info.get("title").toString().trim();
                                        String _msg = info.get("msg").toString().trim();
                                        String _filePath = info.get("mobile_url").toString();
                                        String time = info.get("update_time").toString();
                                        if(time.length()>19){
                                            time = time.substring(0,19);
                                        }
								%>
									<tr>
										<td path="<%=_filePath%>">
											<input type="radio" name="checklist" value="<%=id %>" />
										</td>
										<td><%=id%></td>
										<td><label class="ellipsis ellipsis1"><%=StringEscapeUtils.escapeHtml(_taskname)%></label></td>
										<td><label class="ellipsis ellipsis2" msg="<%=StringEscapeUtils.escapeHtml(_msg)%>"><%=StringEscapeUtils.escapeHtml(_msg)%></label></td>
										<td>
                                            <a href="javascript:checkFile('<%=_filePath%>','<%=path%>')"><emp:message key="ydwx_common_chakanhaoma" defVal="查看号码" fileName="ydwx"></emp:message></a>
                                            <a class="downLoad" href="javascript:uploadFiles('<%=_filePath%>','<%=path%>')"><emp:message key="ydwx_common_xiazai" defVal="下载" fileName="ydwx"></emp:message></a>
                                        </td>
										<td><%=time%></td>
										<td>
										<a onclick="javascript:del(<%=id%>,<%=draftstype%>)"><emp:message key="ydwx_common_btn_shanqu" defVal="删除" fileName="ydwx"></emp:message></a>
										</td>
									</tr>
								<%
									}
								}else{
									%>
									<tr><td colspan="7"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
								<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="7">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
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
        <%--发送主题及内容详情查看--%>
        <div id="des-info"></div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=path%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=path%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=path %>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
		<script type="text/javascript" src="<%=path%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path%>/common/js/draft.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=path%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=path%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>	
		<script type="text/javascript">
		//页面加载，初始化相关数据
		$(document).ready(function() {
            getLoginInfo("#loginInfo");
            showPageInfo2(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>,[5]);
            //不显示跳转操作
            $('#p_goto').hide();
			$('#search').click(function(){submitForm();});
		});

		</script>
	</body>
</html>
