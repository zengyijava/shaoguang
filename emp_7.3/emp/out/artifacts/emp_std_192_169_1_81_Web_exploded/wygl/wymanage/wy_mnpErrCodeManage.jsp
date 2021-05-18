<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.montnets.emp.entity.wy.AMnperrcode"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

MessageUtils messageUtils = new MessageUtils();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("mnpErrCodeManage");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<AMnperrcode> list = (List<AMnperrcode>) request
		.getAttribute("list");
String errCode = StringUtils.defaultIfEmpty(request.getParameter("errCode"),"");
String errCodeType = StringUtils.defaultIfEmpty(request.getParameter("errCodeType"),"0");
boolean edit = btnMap.get(menuCode+"-1")!=null;
boolean add = btnMap.get(menuCode+"-3")!=null;
boolean del = btnMap.get(menuCode+"-4")!=null;

//手动添加
String sdtj = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwcwdm_sdtj", request);
//保 存
String bc = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwcwdm_bc", request);
//取  消
String qx = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwcwdm_qx", request);

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  	<%@include file="/common/common.jsp" %>
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
	
	<%if(StaticValue.ZH_HK.equals(langName)){%>	
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
  </head>
  
  <body>
    <div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="wy_mnpErrCodeManage.htm" method="post" id="pageForm">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="errcode_tip" style="display: none;">
					<div style="float: left;margin-top: 0px;">
					<emp:message key="txgl_wygl_xhzwcwdm_ts" defVal="提示" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message>
					</div>
					<div style="float: left;margin-top: -2px;">
					<emp:message key="txgl_wygl_xhzwcwdm_1cwdmszyyyhsjh" defVal="1、错误代码是指由于用户手机号为携号转网号码导致信息接收失败，运营商返回的错误代码；" fileName="txgl"></emp:message><br/>
					<emp:message key="txgl_wygl_xhzwcwdm_2dxtjcdmsjhmfh" defVal="2、当系统检测到某手机号码返回的状态报告包含列表中的错误代码，系统将此号码自动加入携号转网号码库中。" fileName="txgl"></emp:message>
					</div>
				</div>
				<div id="toggleDiv"></div>
				<%if(add) {%><a id="addgr" onclick="javascript:add()"><emp:message key="txgl_wygl_xhzwcwdm_sdtj" defVal="手动添加" fileName="txgl"></emp:message></a><%} %>
				<%if(del) {%><a id="delete"><emp:message key="txgl_wygl_xhzwcwdm_sc" defVal="删除" fileName="txgl"></emp:message></a><%} %>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td>
								<emp:message key="txgl_wygl_xhzwcwdm_lx" defVal="类型" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message>
							</td>
							<td >	
								<select id="errCodeType" name="errCodeType" class="newSelect">
									<option value="0"><emp:message key="txgl_wygl_xhzwcwdm_qb" defVal="全部" fileName="txgl"></emp:message></option>
									<option value="0001"><emp:message key="txgl_wygl_xhzwcwdm_ydzlt" defVal="移动转联通" fileName="txgl"></emp:message></option>
										<option value="0021"><emp:message key="txgl_wygl_xhzwcwdm_ydzdx" defVal="移动转电信" fileName="txgl"></emp:message></option>
										<option value="0100"><emp:message key="txgl_wygl_xhzwcwdm_ltzyd" defVal="联通转移动" fileName="txgl"></emp:message></option>
										<option value="0121"><emp:message key="txgl_wygl_xhzwcwdm_ltzdx" defVal="联通转电信" fileName="txgl"></emp:message></option>
										<option value="2100"><emp:message key="txgl_wygl_xhzwcwdm_dxzyd" defVal="电信转移动" fileName="txgl"></emp:message></option>
										<option value="2101"><emp:message key="txgl_wygl_xhzwcwdm_dxzlt" defVal="电信转联通" fileName="txgl"></emp:message></option>
								</select>
							</td>
							<td>
								<emp:message key="txgl_wygl_xhzwcwdm_cwdm" defVal="错误代码" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message>
							</td>
							<td >
								<input type="text" value="<%=errCode%>" name="errCode" id="errCode"/>
							</td>
							<td class="tdSer">
							       <center><a id="search"></a></center>
						    </td>		
						</tr>
					</tbody>
				</table>
			</div>
			<table id="content">
				<thead>
					<tr>
						<th width="10%;">
							<input type="checkbox" name="checkall" id="checkall" onclick="checkAlls(this,'checklist')" />
						</th>
						<th width="25%;">
							<emp:message key="txgl_wygl_xhzwcwdm_lx" defVal="类型" fileName="txgl"></emp:message>
						</th>
						<th width="25%;">
						    <emp:message key="txgl_wygl_xhzwcwdm_cwdm" defVal="错误代码" fileName="txgl"></emp:message>
						</th>
						<th colspan="2">
							<emp:message key="txgl_wygl_xhzwcwdm_cz" defVal="操作" fileName="txgl"></emp:message>
						</th>
					</tr>
				</thead>
				<tbody>
				<%
					if(list!=null && list.size()>0)
					{
						for(AMnperrcode item : list)
						{
				%>
				<tr class="data_tr">
					<td>
						<input type="checkbox" name="checklist" value="<%=item.getId()%>"/>
					</td>
					<td><%--<%=item.getTypeStr()%>--%>
					<%
					if("移动转联通".equals(item.getTypeStr())){%>
						<emp:message key="txgl_wygl_xhzwcwdm_ydzlt" defVal="移动转联通" fileName="txgl"></emp:message>
					<%}else if("移动转电信".equals(item.getTypeStr())){%>
						<emp:message key="txgl_wygl_xhzwcwdm_ydzdx" defVal="移动转电信" fileName="txgl"></emp:message>
					<%}else if("联通转移动".equals(item.getTypeStr())){%>
						<emp:message key="txgl_wygl_xhzwcwdm_ltzyd" defVal="联通转移动" fileName="txgl"></emp:message>
					<%}else if("联通转电信".equals(item.getTypeStr())){%>
						<emp:message key="txgl_wygl_xhzwcwdm_ltzdx" defVal="联通转电信" fileName="txgl"></emp:message>
					<%}else if("电信转移动".equals(item.getTypeStr())){%>
						<emp:message key="txgl_wygl_xhzwcwdm_dxzyd" defVal="电信转移动" fileName="txgl"></emp:message>
					<%}else if("电信转联通".equals(item.getTypeStr())){%>
						<emp:message key="txgl_wygl_xhzwcwdm_dxzlt" defVal="电信转联通" fileName="txgl"></emp:message>
					<%}%>
					</td>
					<td><%=item.getErrorcode() %></td>
					<td>
					<%if(edit){ %><a class="edit"><emp:message key="txgl_wygl_xhzwcwdm_xg" defVal="修改" fileName="txgl"></emp:message></a><%}else{out.print("-");} %>
					</td>
					<td>
					<%if(del){ %><a class="delete"><emp:message key="txgl_wygl_xhzwcwdm_sc" defVal="删除" fileName="txgl"></emp:message></a><%}else{out.print("-");} %>
					</td>
				</tr>
				<%
						}
					}else{
				%>
				<tr><td align="center" colspan="5"><emp:message key="txgl_wygl_xhzwcwdm_wjl" defVal="无记录" fileName="txgl"></emp:message></td></tr>
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
		</form>
		</div>
		<div class="bottom">
			<div id="bottom_right">
				<div id="bottom_left"></div>
				<div id="bottom_main">
				</div>
			</div>
		</div>
	</div>
	<%-- 手动添加 --%>
	<div id="toAdd" title="<%=sdtj %>" style="display:none;">
		<form id="">
		<div style="height:20px"></div>
			<table width="450" style="margin-left: 20px;margin-right:20px; line-height: 30px;" >
				<tr>
					<td width="90"><emp:message key="txgl_wygl_xhzwcwdm_l" defVal="类" fileName="txgl"></emp:message><span style="width:0.2em;display:inline-block;"></span><emp:message key="txgl_wygl_xhzwcwdm_x" defVal="型" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></td>
					<td width="375">
					<select name="phoneType" class="newSelect">
										<option value=""><emp:message key="txgl_wygl_xhzwcwdm_qxz" defVal="请选择" fileName="txgl"></emp:message></option>
										<option value="0001"><emp:message key="txgl_wygl_xhzwcwdm_ydzlt" defVal="移动转联通" fileName="txgl"></emp:message></option>
										<option value="0021"><emp:message key="txgl_wygl_xhzwcwdm_ydzdx" defVal="移动转电信" fileName="txgl"></emp:message></option>
										<option value="0100"><emp:message key="txgl_wygl_xhzwcwdm_ltzyd" defVal="联通转移动" fileName="txgl"></emp:message></option>
										<option value="0121"><emp:message key="txgl_wygl_xhzwcwdm_ltzdx" defVal="联通转电信" fileName="txgl"></emp:message></option>
										<option value="2100"><emp:message key="txgl_wygl_xhzwcwdm_dxzyd" defVal="电信转移动" fileName="txgl"></emp:message></option>
										<option value="2101"><emp:message key="txgl_wygl_xhzwcwdm_dxzlt" defVal="电信转联通" fileName="txgl"></emp:message></option>
					</select>
					</td>
				</tr>
				<tr><td colspan="2"><emp:message key="txgl_wygl_xhzwcwdm_cwdm" defVal="错误代码" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></td></tr>
				<tr><td colspan="2">
					<textarea name="importArea" id="importArea" style="height: 160px;width: 99%;resize:none;"></textarea>
				</td></tr>
				<tr><td colspan="2">
					<span id="count"  style="color: blue;font-weight: bold;">0</span>/<emp:message key="txgl_wygl_xhzwcwdm_dgcwdmydhkgdh" defVal="1000 多个错误代码以逗号、空格、顿号、回车换行其中一种格式隔开" fileName="txgl"></emp:message>
				</td></tr>
			</table>
			<div style="height: 60px;line-height: 60px;margin-top: 5px;">
			    <center>
	    		<input class="btnClass5 mr23" type="button" value="<%=bc %>" onclick="javascript:sure_add(this);"/>
			    <input onclick="javascript:cancel_add();" class="btnClass6" type="button" value="<%=qx %>" /><br/>
			    </center>
	    	</div>
	    </form>
		</div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=iPath%>/js/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	<script src="<%=iPath%>/js/mnpErrCodeManage.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>

	<script type="text/javascript">
		var path='<%=path%>';
		$(document).ready(function() {
			var findresult="<%=(String)request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
		       alert(getJsLocaleMessage("txgl","txgl_wygl_wytdgl_text_7"));
		       return;			       
		    }
			getLoginInfo("#loginInfo");
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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){
				submitForm();
			});
			var errCodeType = "<%=errCodeType%>";
			$("#errCodeType option[value='"+errCodeType+"']").attr('selected',true);
			$(".newSelect").isSearchSelect({'width':'158','isInput':false,'zindex':0});
		});
	</script>
  </body>
</html>
