<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.notice.vo.LfNoticeVo"%>
<%@page import="com.montnets.emp.entity.notice.LfNotice"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%
String path = request.getContextPath();
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath=iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath=inheritPath.substring(0,inheritPath.lastIndexOf("/"));
PageInfo pageInfo = new PageInfo();
pageInfo = (PageInfo) request.getAttribute("pageInfo");
java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
List<LfNoticeVo> lnList = (List<LfNoticeVo>)request.getAttribute("allList");
LfNotice notice = (LfNotice)request.getAttribute("notice");
//LfSysuser user = (LfSysuser)session.getAttribute("loginSysuser");
String username = (String)request.getAttribute("lgusername");
String type = (String)request.getAttribute("type");
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("sysNotice");
String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=iPath%>/css/notice.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.dialog.new.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<link rel="stylesheet" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<link rel="stylesheet" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/not_sysNotice.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/not_sysNotice.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	
	<script>
	var iPath="<%=iPath%>";
	</script>
	</head>

		<body id="not_sysNotice">
		
		<div id="addNotice" title="发布公告" class="addNotice">
			<div>
			<center>
			<input type="hidden" id="nid" value=""/>
			<table>
			<tr><td >标题：</td><td ><input type="text" id="title" value="<%=notice.getTitle()==null?"":notice.getTitle() %>"  class="input_bd title" maxlength="20"></td>
			<td><font class="font_red">&nbsp;*</font></td></tr>
			<tr><td height="5px;"></td></tr>
			<tr><td>内容：</td><td><textarea id="cont"  class="input_bd cont"><%=notice.getContext()==null?"":notice.getContext() %></textarea></td>
			<td><font class="font_red">&nbsp;*</font></td></tr>
			<tr><td height="5px;"></td></tr>
			<tr><td>注尾：</td><td><input type="text" id="noteTail" name="noteTail" value="<%=notice.getNoteTail()==null?"":notice.getNoteTail() %>"  class="input_bd noteTail" maxlength="30"></td>
			<td><font class="font_red">&nbsp;*</font></td></tr>
			<tr><td height="5px;"></td></tr>
			<%
				String noteState1 = "checked";
				String noteState2 = "";
				if(notice.getNoteState()!=null && notice.getNoteState()==1){
					noteState1 = "checked";
					noteState2 = "";
				}else if(notice.getNoteState()!=null && notice.getNoteState()==2){
					noteState1 = "";
					noteState2 = "checked";
				}
			%>
			<tr><td>公告状态：</td><td><input type="radio" id="noteState1" name="noteState" value="1" <%=noteState1 %> >开启<input type="radio" id="noteState2" name="noteState" value="2" <%=noteState2 %> class="noteState1" >关闭</td>
			<td><font class="font_red">&nbsp;*</font></td></tr>
			<tr><td height="5px;"></td></tr>
			<tr><td>有效期：</td><td><input type="text" id="noteValid" name="noteValid" value="<%=notice.getNoteValid()==null?"":notice.getNoteValid() %>"  class="input_bd noteValid" maxlength="8" onkeyup=numberControl($(this)) >&nbsp;&nbsp;天</td></tr>
			<tr><td height="5px;"></td></tr>
			<tr><td colspan="3" class="font_red">温馨提示：系统公告面向所有企业客户；请合理发布。</td></tr>
			
			</table>
			</center>
			</div>
			<div class="yl_div">
			    <center>
			<input type="button" value="预览"  class="btnClass6 mr23" onclick="previewNotice();"/>
			<input type="button" value="发布" onclick="doOk()" class="btnClass5 mr23"/>
			<input type="button" value="取消"  class="btnClass6" onclick="doCancel()"/><br/>
             </center>
	    	</div>
			</div>
		<div id="Notices" title="公告内容" class="Notices">
			      <table>
			        <tr><td>发  布  人：</td><td><input type="text" id="user" value=""  readonly="readonly" onfocus="this.blur()"  class="input_bd user"/></td></tr>
					<tr><td class="Notices_td"></td></tr>
					<tr><td>发布时间：</td><td><input type="text" id="ttime" value=""  readonly="readonly" onfocus="this.blur()"  class="input_bd ttime"/></td></tr>
					<tr><td class="Notices_td"></td></tr>
					<tr><td>标　　题：</td><td><input type="text" id="ttitle" value=""  readonly="readonly" onfocus="this.blur()"  class="input_bd ttitle"/></td></tr>
					<tr><td class="Notices_td"></td></tr>
					<tr><td valign="top">内　　容：</td><td><textarea readonly="readonly" id="tcont"  class="input_bd tcont"></textarea></td></tr>
					<tr><td class="Notices_td"></td></tr>
					</table>
			</div>
		<div id="container" class="container">
			<%
			if("1".equals(type))
			{
				out.print(com.montnets.emp.common.constant.ViewParams.getPositionWhitIn("首页","公告查看"));
			}
			else
			{
				out.print(com.montnets.emp.common.constant.ViewParams.getPosition(menuCode));
			} %>
				<div id="rContent" class="rContent">
						<div class="buttons rContent_div"  >
							<a class="addNoti" onclick="javascript:doAdd();">发布公告</a>
						</div>
				<form name="pageForm" action="not_sysNotice.htm?method=find" method="post"
					id="pageForm">
					<%-- 表示是在首页还是系统管理下的公告 --%>
					<input type="hidden" name="type" id="type" value="<%=type!=null?type:"" %>"/>
					<%-- contextPath--%>
					<input type="hidden" name="contextPath" id="contextPath" value="<%=request.getContextPath()%>"/>
					<div id="loginparams" class="hidden"></div>
					<table id="content">
						<thead>
							<tr>
								<th>
									标题
								</th>
								<th>
									内容
								</th>
								<th>
									发布人
								</th>
								<th>
									发布时间
								</th>
								<%if("admin".equals(username) || "sysadmin".equals(username)){ %>
								<%-- <th colspan="2">
									操作
								</th> --%>
								<%} %>
							</tr>
						</thead>
						<tbody>
								<%
								if (lnList != null && lnList.size() > 0)
								{
									for (LfNoticeVo ln : lnList)
									{
								%>	
								<tr>
								<td>
								<%=ln.getTitle().length()>20?ln.getTitle().toString().substring(0,20)+"...":ln.getTitle() %>
								 <%-- <a onclick="showDetail('<%=ln.getNoticeID() %>')">
								    <code><%=ln.getTitle().length()>20?ln.getTitle().toString().substring(0,20)+"...":ln.getTitle() %></code>
								</a> --%>
								</td>
								<td>
								<a onclick="javascript:modify(this)">
								  <label class="getContext_label">
								  <xmp><%=ln.getContext()==null?"":ln.getContext().replaceAll("\\\\n"," ")%>
								  </xmp></label>

								<xmp><%=ln.getContext()!=null?ln.getContext().replaceAll("\\\\n"," ").length()>20?ln.getContext().replaceAll("\\\\n"," ").substring(0,20)+"...":ln.getContext().replaceAll("\\\\n"," "):"-" %></xmp>
								  </a>
								</td>
								<td> 
								<xmp><%=ln.getName()!=null?ln.getName():"" %></xmp>
								</td>
								<td><%=df.format(ln.getPublishTime()) %></td>
								<%if("admin".equals(username) || "sysadmin".equals(username)){ %>
								<%-- <td>
								<a onclick="javascript:doEdit('<%=ln.getNoticeID() %>')">修改</a>
								</td>
								<td>
								<a onclick="javascript:doDel('<%=ln.getNoticeID() %>')">删除</a>
								</td> --%>
								<%} %>
								</tr>
								<%}
								}
								else
								{
								%>
								<tr><td colspan="4" align="center">无记录</td></tr>
								<%} %>
								</tbody>
								<tfoot>
							<tr>
								<td colspan="4">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					</form>
					</div>
				</div>
		<div id="modify" title="内容"  class="modify">
				<div id="msg" class="msg"><xmp></xmp></div>
		</div>
			<%-- 内容结束 --%>
			<div class="clear"></div>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=iPath %>/js/not_sysNotice.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript">
    $(document).ready(function(){
        getLoginInfo("#loginparams");
        $("#content tbody tr").hover(function() {
			$(this).addClass("hoverColor");
		}, function() {
			$(this).removeClass("hoverColor");
		});
    	initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
    });
	//显示列表状态下，模板名字详细信息
	function modify(t)
	{
		$('#modify').dialog({
			autoOpen: false,
			resizable: false,
			width:250,
		    height:200
		});
		$("#msg").children("xmp").empty();
		$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
		$('#modify').dialog('open');
	}
    </script>
	</body>
</html>
