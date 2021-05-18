<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.wy.AMnp"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
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
String menuCode = titleMap.get("mnpManage");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo=(PageInfo)request.getAttribute("pageInfo");
if(pageInfo == null){pageInfo = new PageInfo();}
String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<AMnp> list = (List<AMnp>) request
		.getAttribute("list");
String phoneStr = StringUtils.defaultIfEmpty(request.getParameter("phone"),"");
String addType = StringUtils.defaultIfEmpty(request.getParameter("addType"),"");
String phoneType = StringUtils.defaultIfEmpty(request.getParameter("phoneType"),"0");
//按钮权限
boolean add = btnMap.get(menuCode+"-1")!=null;
boolean ipt = btnMap.get(menuCode+"-2")!=null;
boolean edit = btnMap.get(menuCode+"-3")!=null;
boolean del = btnMap.get(menuCode+"-4")!=null;

//手动添加
String sdtj = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwcwdm_sdtj", request);
//保 存
String bc = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwcwdm_bc", request);
//取  消
String qx = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwcwdm_qx", request);
//文件导入
String wjdr = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwhmgl_wjdr", request);
//浏览
String ll = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwhmgl_ll", request);

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
    <%@include file="/common/common.jsp" %>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.dialog.new.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
	
	<%if(StaticValue.ZH_HK.equals(langName)){%>	
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<style>
			.choose_file{
				position: relative;
			}
			.files{
				position: absolute;
				left: 0;
				top: 0;
				heigth: 28px;
				cursor: pointer;
				filter: Alpha(opacity=0);
				-moz-opacity: 0;
				opacity: 0;
				width: 300px;
			}
			.w_input{
				width:218px;
				height: 20px;
				line-height: 20px;
				padding: 0 0 0 2px;
				outline: 0;
			}
			.table_import{
				margin-left: 20px;margin-right:20px;
				width:450px;
			}
			.table_import td{
				padding:0 0 8px 0;
			}
			.table_import p{
				line-height:20px;
			}
			.explain{
				color:#999;
			}
		</style>
		
  </head>
  
  <body>
    <div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="wy_mnpManage.htm" method="post" id="pageForm">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv"></div>
				<%if(add) {%><a id="addgr" onclick="javascript:add()"><emp:message key="txgl_wygl_xhzwcwdm_sdtj" defVal="手动添加" fileName="txgl"></emp:message></a><%} %>
				<%if(ipt) {%><a id="addgr" onclick="javascript:importData()"><emp:message key="txgl_wygl_xhzwhmgl_wjdr" defVal="文件导入" fileName="txgl"></emp:message></a><%} %>
				<%if(del) {%><a id="delete" ><emp:message key="txgl_wygl_xhzwcwdm_sc" defVal="删除" fileName="txgl"></emp:message></a><%} %>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td>
								<emp:message key="txgl_wygl_xhzwhmgl_sjhm" defVal="手机号码" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message>
							</td>
							<td >
								<input type="text" value="<%=phoneStr %>" name="phone" id="phone" class="int"/>
							</td>
							<td>
								<emp:message key="txgl_wygl_xhzwhmgl_hmlx" defVal="号码类型" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message>
							</td>
							<td >	
								<select id="phoneType" name="phoneType" class="newSelect">
									<option value="0"><emp:message key="txgl_wygl_xhzwcwdm_qb" defVal="全部" fileName="txgl"></emp:message></option>
									<option value="0001"><emp:message key="txgl_wygl_xhzwcwdm_ydzlt" defVal="移动转联通" fileName="txgl"></emp:message></option>
									<option value="0021"><emp:message key="txgl_wygl_xhzwcwdm_ydzdx" defVal="移动转电信" fileName="txgl"></emp:message></option>
									<option value="0100"><emp:message key="txgl_wygl_xhzwcwdm_ltzyd" defVal="联通转移动" fileName="txgl"></emp:message></option>
									<option value="0121"><emp:message key="txgl_wygl_xhzwcwdm_ltzdx" defVal="联通转电信" fileName="txgl"></emp:message></option>
									<option value="2100"><emp:message key="txgl_wygl_xhzwcwdm_dxzyd" defVal="电信转移动" fileName="txgl"></emp:message></option>
									<option value="2101"><emp:message key="txgl_wygl_xhzwcwdm_dxzlt" defVal="电信转联通" fileName="txgl"></emp:message></option>
									<option value="-1"><emp:message key="txgl_wygl_xhzwhmgl_wz" defVal="未知" fileName="txgl"></emp:message></option>
								</select>
							</td>
							<td><emp:message key="txgl_wygl_xhzwhmgl_lrlx" defVal="录入类型" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></td>
							<td>
								<select id="addType" name="addType" class="newSelect">
									<option value=""><emp:message key="txgl_wygl_xhzwcwdm_qb" defVal="全部" fileName="txgl"></emp:message></option>
									<option value="0"><emp:message key="txgl_wygl_xhzwhmgl_yhtj" defVal="用户添加" fileName="txgl"></emp:message></option>
									<option value="1"><emp:message key="txgl_wygl_xhzwhmgl_xtlr" defVal="系统录入" fileName="txgl"></emp:message></option>
								</select>
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
						<th width="5%;">
							<input type="checkbox" name="checkall" id="checkall" onclick="checkAlls(this,'checklist')" />
						</th>
						<th width="20%;">
							<emp:message key="txgl_wygl_xhzwhmgl_sjhm" defVal="手机号码" fileName="txgl"></emp:message>
						</th>
						<th width="20%;">
							<emp:message key="txgl_wygl_xhzwhmgl_hmlx" defVal="号码类型" fileName="txgl"></emp:message>
						</th>
						<th>
						    <emp:message key="txgl_wygl_xhzwhmgl_lrlx" defVal="录入类型" fileName="txgl"></emp:message>
						</th>
						<th>
							<emp:message key="txgl_wygl_xhzwhmgl_lrsj" defVal="录入时间" fileName="txgl"></emp:message>
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
						for(AMnp item : list)
						{
				%>
				<tr class="data_tr">
					<td>
						<input type="checkbox" name="checklist" value="<%=item.getId()%>"/>
					</td>
					<td><%=item.getPhone() %></td>
					<td>
					<%--<%=item.getPhoneTypeStr() %>--%>
					<%if("移动转联通".equals(item.getPhoneTypeStr())){ %>
						<emp:message key="txgl_wygl_xhzwcwdm_ydzlt" defVal="移动转联通" fileName="txgl"></emp:message>
					<%}else if("移动转电信".equals(item.getPhoneTypeStr())){ %>
						<emp:message key="txgl_wygl_xhzwcwdm_ydzdx" defVal="移动转电信" fileName="txgl"></emp:message>
					<%}else if("联通转移动".equals(item.getPhoneTypeStr())){ %>
						<emp:message key="txgl_wygl_xhzwcwdm_ltzyd" defVal="联通转移动" fileName="txgl"></emp:message>
					<%}else if("联通转电信".equals(item.getPhoneTypeStr())){ %>
						<emp:message key="txgl_wygl_xhzwcwdm_ltzdx" defVal="联通转电信" fileName="txgl"></emp:message>
					<%}else if("电信转移动".equals(item.getPhoneTypeStr())){ %>
						<emp:message key="txgl_wygl_xhzwcwdm_dxzyd" defVal="电信转移动" fileName="txgl"></emp:message>
					<%}else if("电信转联通".equals(item.getPhoneTypeStr())){ %>
						<emp:message key="txgl_wygl_xhzwcwdm_dxzlt" defVal="电信转联通" fileName="txgl"></emp:message>
					<%}%>
					</td>
					<td><% if(item.getAddType()==0){out.print(messageUtils.extractMessage("txgl", "txgl_wygl_xhzwhmgl_yhtj", request));}else out.print(messageUtils.extractMessage("txgl", "txgl_wygl_xhzwhmgl_xtlr", request)); %></td>
					<td><%=item.getCreateTimeStr() %></td>
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
				<tr><td align="center" colspan="7"><emp:message key="txgl_wygl_xhzwcwdm_wjl" defVal="无记录" fileName="txgl"></emp:message></td></tr>
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
			<table width="450px;" style="margin-left: 20px;margin-right:20px; line-height: 30px;" >
				<tr>
					<td width="100"><emp:message key="txgl_wygl_xhzwhmgl_hmlx" defVal="号码类型" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></td>
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
				<tr><td colspan="2"><emp:message key="txgl_wygl_xhzwhmgl_sjhm" defVal="手机号码" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></td></tr>
				<tr><td colspan="2">
					<textarea name="importArea" id="importArea" style="height: 150px;width: 450px;resize:none;"></textarea>
				</td></tr>
				<tr><td colspan="2">
					<span id="count" style="color: blue;font-weight: bold;">0</span>/1000     <emp:message key="txgl_wygl_xhzwhmgl_dgsjhmydhkgdh" defVal=" 多个手机号码以逗号、空格、顿号、回车换行其中一种格式隔开" fileName="txgl"></emp:message>
				</td></tr>
			</table>
			<div style="height: 50px;line-height: 50px;margin-top: 5px;">
			    <center>
	    		<input class="btnClass5 mr23" type="button" value="<%=bc %>" onclick="javascript:sure_add(this);"/>
			    <input onclick="javascript:cancel_add();" class="btnClass6" type="button" value="<%=qx %>" /><br/>
			    </center>
	    	</div>
	    </form>
		</div>
		<%-- 文件导入 --%>
		
		<div id="toImport" title="<%=wjdr %>" style="display:none;">
		<form id="" enctype="multipart/form-data" action="<%=path %>/wy_mnpManage.htm?method=importFile">
		<div style="height:12px"></div>
			<table class="table_import">
				<tr>
					<td width="110"><emp:message key="txgl_wygl_xhzwhmgl_xzwj" defVal="选择文件" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></td>
					<td>
						<div class="choose_file">
			              <input type="text" id="txt" name="txt" class="w_input div_bd" style="margin-right:5px;">
						  <input type="button" value="<%=ll %>" size="30" class="btnClass2">
			              <input type="file"  onchange="setIputname(this)" name="file" style="height:26px;" class="files" size="1" hidefocus="">
						 
			    		</div>
					</td>
				</tr>
				<tr>
				<td></td>
				<td>
				<%-- 解决下载后出现等待滚动层的bug 备份
				<a href="javascript:location.href='<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/mnpPhone.txt'" style="color: #2a6fbe;">txt文件模板 </a>   
				<a href="javascript:location.href='<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/mnpPhone.xls'" style="margin:0 10px;color: #2a6fbe;">excel文件模板 </a>
				 --%>
				 <%-- 解决下载后出现等待滚动层的bug 修改 --%>
				 
				 <%--
				 <a href="javascript:download_href('<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/mnpPhone.txt')" style="color: #2a6fbe;">txt <emp:message key="txgl_wygl_xhzwhmgl_wjmb" defVal="文件模板" fileName="txgl"></emp:message></a>   
				<a href="javascript:download_href('<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/mnpPhone.xls')" style="margin:0 10px;color: #2a6fbe;">excel<emp:message key="txgl_wygl_xhzwhmgl_wjmb" defVal="文件模板" fileName="txgl"></emp:message> </a>
				--%>
				<%if(StaticValue.ZH_HK.equals(langName)){%>
				 	<a href="javascript:download_href('<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/mnpPhone-zh_HK.txt')" style="color: #2a6fbe;">txt <emp:message key="txgl_wygl_xhzwhmgl_wjmb" defVal="文件模板" fileName="txgl"></emp:message></a>   
					<a href="javascript:download_href('<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/mnpPhone-zh_HK.xls')" style="margin:0 10px;color: #2a6fbe;">excel<emp:message key="txgl_wygl_xhzwhmgl_wjmb" defVal="文件模板" fileName="txgl"></emp:message> </a>
				<%}else if(StaticValue.ZH_TW.equals(langName)){%>
				 	<a href="javascript:download_href('<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/mnpPhone-zh_TW.txt')" style="color: #2a6fbe;">txt <emp:message key="txgl_wygl_xhzwhmgl_wjmb" defVal="文件模板" fileName="txgl"></emp:message></a>   
					<a href="javascript:download_href('<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/mnpPhone-zh_TW.xls')" style="margin:0 10px;color: #2a6fbe;">excel<emp:message key="txgl_wygl_xhzwhmgl_wjmb" defVal="文件模板" fileName="txgl"></emp:message> </a>
				<%}else{%>
				 	<a href="javascript:download_href('<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/mnpPhone.txt')" style="color: #2a6fbe;">txt <emp:message key="txgl_wygl_xhzwhmgl_wjmb" defVal="文件模板" fileName="txgl"></emp:message></a>   
					<a href="javascript:download_href('<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/mnpPhone.xls')" style="margin:0 10px;color: #2a6fbe;">excel<emp:message key="txgl_wygl_xhzwhmgl_wjmb" defVal="文件模板" fileName="txgl"></emp:message> </a>
				<%}%>
				
				<span class="explain"><emp:message key="txgl_wygl_xhzwhmgl_zc" defVal="支持" fileName="txgl"></emp:message>txt、zip、xls、xlsx、et<emp:message key="txgl_wygl_xhzwhmgl_gs" defVal="格式" fileName="txgl"></emp:message></span>
				<br/>
				</td></tr>
				<tr>
				<td align="right" valign="top"><emp:message key="txgl_wygl_xhzwhmgl_z" defVal="注" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></td>
				<td width="375">
       				<p><emp:message key="txgl_wygl_xhzwhmgl_1dcschmxyg" defVal="1、单次上传号码小于20000个；" fileName="txgl"></emp:message></p>
       				<p><emp:message key="txgl_wygl_xhzwhmgl_2hmlxyzwqhzwh" defVal="2、号码类型由转网前和转网后两个运营商代码组成，各运营商代码分别为：移动－00，联通－01，电信－21。" fileName="txgl"></emp:message><span class="explain"><emp:message key="txgl_wygl_xhzwhmgl_r0001bsydlt" defVal="（如：0001表示移动转联通，前两位00表示转网前的归属运营商，后两位01表示转网后的归属运营商）" fileName="txgl"></emp:message></span></p>
       				
				</td></tr>
			</table>
			<div style="margin-top: 5px;">
			    <center>
	    		<input class="btnClass5 mr23" type="button" value="<%=bc %>" onclick="javascript:sure_import(this);"/>
			    <input onclick="javascript:cancel_import();" class="btnClass6" type="button" value="<%=qx %>" /><br/>
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
	<script src="<%=iPath%>/js/mpnManage.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	<script src="<%=iPath%>/js/jquery.form.min.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
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
			var addType = "<%=addType%>";
			var phoneType = "<%=phoneType%>";
			$("#addType option[value='"+addType+"']").attr('selected',true);
			$("#phoneType option[value='"+phoneType+"']").attr('selected',true);
			$(".newSelect").isSearchSelect({'width':'158','isInput':false,'zindex':0});
		});
		function setIputname(obj){
		    $(obj).prev().prev().val(obj.value);
		 }
	</script>
  </body>
</html>
