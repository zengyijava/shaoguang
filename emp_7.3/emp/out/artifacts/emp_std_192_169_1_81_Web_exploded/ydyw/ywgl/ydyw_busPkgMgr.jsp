<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));

	PageInfo pageInfo = new PageInfo();
	if(request.getAttribute("pageInfo") != null)
	{
		pageInfo = (PageInfo) request.getAttribute("pageInfo");
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//Date date = new Date();
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH, -1);
	//String beginTime = df.format(c.getTime()).substring(0, 8) + "01";
	//String endTime = df.format(c.getTime()).substring(0, 11); //change by dj

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");

	@SuppressWarnings("unchecked")
	String menuCode = titleMap.get("busPkgMgr");

	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String findResult = null;
	findResult = (String) request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");

	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	
	List<DynaBean> recordList = (List<DynaBean>)request.getAttribute("recordList");
	Map busMap = (Map)request.getAttribute("busMap");
	LinkedHashMap<String,String> conditionMap = (LinkedHashMap<String,String>) request.getAttribute("conditionMap");
	
	String depNam = request.getParameter("depNam");
	
	//是否包含子机构
	String isContainsSun=conditionMap.get("isContainsSun")==null?"":conditionMap.get("isContainsSun").toString();	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@include file="/common/common.jsp"%>
		<title><%=titleMap.get(menuCode)%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<style rel="stylesheet" type="text/css">
			#condition .c_selectBox {
				width:208px!important;
			}
			#condition .c_selectBox ul {
				width:208px!important;
			}
			#condition .c_selectBox ul li{
				width:208px!important;
			}
		</style>
	</head>
	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>

			<%=ViewParams.getPosition(empLangName,menuCode) %>
			<%-- 内容开始 --%>
			<%
				if(btnMap.get(menuCode + "-0") != null)
				{
			%>
			<div id="rContent" class="rContent">
				<input type="hidden" value="<%=inheritPath%>" id="inheritPath" />
				<form name="pageForm" action="ydyw_busPkgMgr.htm" method="post" id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%
						if(btnMap.get(menuCode + "-5") != null){
						%>
						<a id="exportCurrent"></a>
						<a id="exportCondition"></a>
						<input type="hidden" name="menucode" id="menucode" value="17" />
						<%}%>
						<%
							if(btnMap.get(menuCode + "-1") != null){
						%>						
							<a id="add" href="javascript:showAddBusPkg()"><emp:message key="common_establish" fileName="common" defVal="新建"></emp:message></a>
						<%
							}
						%>
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_1_p" fileName="ydyw" defVal="业务包名称："></emp:message>
									</td>
									<td>
										<input type="text" style="width: 177px;" value="<%=null==conditionMap.get("packageName")?"":conditionMap.get("packageName") %>" id="packageName" name="packageName" maxlength="32"/>
									</td>
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_3_p" fileName="ydyw" defVal="业务包编号："></emp:message>
									</td>
									<td>
										<input type="text" style="width: 177px;" value="<%=null==conditionMap.get("packageCode")?"":conditionMap.get("packageCode") %>" id="packageCode" name="packageCode" maxlength="32"/>
									</td>
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_31" fileName="ydyw" defVal="状态："></emp:message>
									</td>
									<td>
										<select name="packageState" id="packageState" style="width: 182px" isInput="false">
											<option value="" ><emp:message key="common_whole" defVal="全部" fileName="common"></emp:message></option>
											<option value="0" <%if(null!=conditionMap.get("packageState")&&"0".equals(conditionMap.get("packageState"))){%> selected="selected" <%}%>><emp:message key="ydyw_ywgl_ywbgl_text_4" fileName="ydyw" defVal="已启用"></emp:message></option>
											<option value="1" <%if(null!=conditionMap.get("packageState")&&"1".equals(conditionMap.get("packageState"))){%> selected="selected" <%}%>><emp:message key="ydyw_ywgl_ywbgl_text_5" fileName="ydyw" defVal="已禁用"></emp:message></option>
										</select>										
									</td>
									<td class="tdSer">
										<center>
											<a id="search" name="research"></a>
										</center>
									</td>
								</tr>
								<tr>
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_21" defVal="业务名称：" fileName="ydyw"></emp:message>
									</td>
									<td>
										<input type="text" style="width: 177px;" value="<%=null==conditionMap.get("busName")?"":conditionMap.get("busName") %>" id="busName" name="busName" maxlength="32"/>
									</td>
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_22" defVal="业务编码：" fileName="ydyw"></emp:message>
									</td>
									<td>
										<input type="text" style="width: 177px;" value="<%=null==conditionMap.get("busCode")?"":conditionMap.get("busCode") %>" id="busCode" name="busCode" maxlength="32"/>
									</td>
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_24" defVal="操作员：" fileName="ydyw"></emp:message>
									</td>
									<td>
										<input type="text" style="width: 177px;" value="<%=null==conditionMap.get("userName")?"":conditionMap.get("userName") %>" id="userName" name="userName"/>
									</td>
									<td></td>
								</tr>
								<tr>
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_26" defVal="机构：" fileName="ydyw"></emp:message>
									</td>
									<td class="condi_f_l">
										<div style="width: 220px;">
											<input type="hidden" id="deptString" name="deptString" value="<%=conditionMap.get("deptString")==null?"":conditionMap.get("deptString")%>"/>
											<input type="text" class="treeInput" id="depNam" name="depNam" value="<%=depNam==null? MessageUtils.extractMessage("common","common_pleaseSelect",request):depNam%>"
											onclick="javascript:showMenu();" readonly style="width: 160px; cursor: pointer;" class="treeInput" />
											&nbsp;
										</div>
										<div id="dropMenu">
											<div style="margin-top: 3px; margin-right: 10px; text-align: right">
												<input type="checkbox" id="isContainsSun" name="isContainsSun" <%if("1".equals(isContainsSun)){%>checked="checked" <%}else if("0".equals(isContainsSun)){}else{%>checked="checked" <%}%> value="1" style="width:15px;height:15px;vertical-align: text-bottom;"/><emp:message key="ydyw_qyjfcx_khtfgl_text_9" defVal="包含子机构" fileName="ydyw"></emp:message>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="button" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" class="btnClass1"	onclick="javascript:zTreeOnClickOK3();" style="width: 50px;" />&nbsp;&nbsp;
												<input type="button" value="<emp:message key="common_clean" defVal="清空" fileName="common"></emp:message>" class="btnClass1"	onclick="javascript: cleanSelect_dep();" style="width: 50px;" />
											</div>
											<ul id="dropdownMenu" class="tree"></ul>
										</div>
									</td>
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_28" defVal="创建时间：" fileName="ydyw"></emp:message>
									</td>
									<td class="tableTime">
										<input type="text" style="cursor: background-color: white;width: 181px;" class="Wdate" readonly="readonly" onclick="setime()"
											value="<%=conditionMap.get("begintime") != null ? conditionMap.get("begintime") : ""%>"
											id="sendtime" name="begintime">
									</td>
									<td>
										<emp:message key="common_to" defVal="至：" fileName="common"></emp:message>
									</td>
									<td>
										<input type="text" style="cursor: pointerund-color: white;width: 180px;"	class="Wdate" readonly="readonly" onclick="retime()"
											value="<%=conditionMap.get("endtime") != null ? conditionMap.get("endtime") : ""%>"
											id="recvtime" name="endtime">
									</td>
									<td></td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_1" fileName="ydyw" defVal="业务包名称"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_3" fileName="ydyw" defVal="业务包编号"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_2" fileName="ydyw" defVal="包含业务"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_30" fileName="ydyw" defVal="状态"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_27" defVal="创建时间" fileName="ydyw"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_29" defVal="修改时间" fileName="ydyw"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_25" defVal="机构" fileName="ydyw"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_23" defVal="操作员" fileName="ydyw"></emp:message>
								</th>
								<%
									if(btnMap.get(menuCode + "-2") != null){
								%>									
								<th>
									<emp:message key="common_operation" defVal="操作" fileName="common"></emp:message>
								</th>
								<%
									}
								%>									
							</tr>
						</thead>
						<tbody>
							<%
							if(recordList != null && recordList.size()>0)
							{
								for (int i = 0; i < recordList.size(); i++)
								{
									DynaBean bean=recordList.get(i);
							%>
							<tr>
								<td>
									<a onclick=javascript:showDetial(this,getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_1"))>
									<%
										String pkgNameStr = null!=bean.get("package_name")?bean.get("package_name").toString():"";
									%>
									<label style="display:none"><xmp><%=pkgNameStr %></xmp></label>
									<xmp><%=pkgNameStr.length()>10?pkgNameStr.substring(0,10)+"...":pkgNameStr %></xmp>
									</a> 									
								</td>
								<td>
									<a onclick=javascript:showDetial(this,getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_3"))>
									<%
										String pkgCodeStr = null!=bean.get("package_code")?bean.get("package_code").toString():"";
									%>
									<label style="display:none"><xmp><%=pkgCodeStr %></xmp></label>
									<xmp><%=pkgCodeStr.length()>10?pkgCodeStr.substring(0,10)+"...":pkgCodeStr %></xmp>
									</a> 									
								</td>
								<td>
									<a onclick=javascript:showDetial(this,getJsLocaleMessage("ydyw","ydyw_ywgl_ywbgl_text_2"))>
									<%
										String busstr = busMap.get(null!=bean.get("package_code")?bean.get("package_code"):"").toString();
									%>
									<label style="display:none"><xmp><%=busstr %></xmp></label>
									<xmp><%=busstr.length()>10?busstr.substring(0,10)+"...":busstr %></xmp>
									</a> 									
								</td>
								<td id="stateTd">
									<%
										String state = bean.get("package_state").toString();
									%>
									<select id="selState<%=bean.get("package_id") %>" state="<%=state %>" pkgId="<%=bean.get("package_id") %>" >
										<%
											if("0".equals(state)){
										%>
												<option value="0-<%=state %>-<%=bean.get("package_id") %>" selected="selected"><emp:message key="ydyw_ywgl_ywbgl_text_4" fileName="ydyw" defVal="已启用"></emp:message></option>
												<option value="1-<%=state %>-<%=bean.get("package_id") %>"><emp:message key="ydyw_qyjfcx_khjfcx_text_6" fileName="ydyw" defVal="禁用"></emp:message></option>
										<%
											}else if("1".equals(state)){
										%>
												<option value="1-<%=state %>-<%=bean.get("package_id") %>" selected="selected"><emp:message key="ydyw_ywgl_ywbgl_text_5" fileName="ydyw" defVal="已禁用"></emp:message></option>
												<option value="0-<%=state %>-<%=bean.get("package_id") %>"><emp:message key="ydyw_qyjfcx_khjfcx_text_7" fileName="ydyw" defVal="启用"></emp:message></option>
										<%		
											}
										%>
									</select>
								</td>
								<td><%=df.format(bean.get("create_time")) %></td>
								<td><%=df.format(bean.get("update_time"))  %></td>
								<td><%=bean.get("dep_name") %></td>
								<td><%=bean.get("name") %>(<%=bean.get("user_name") %>)</td>
								<%
									if(btnMap.get(menuCode + "-2") != null){
								%>									
								<td>
									<a href="javascript:toEdit('<%=bean.get("package_id") %>')"><emp:message key="common_modify" fileName="common" defVal="修改"></emp:message></a>
								</td>
								<%
									}
								%>									
							</tr>
							<%
								}
							}else{
							%>
								<tr>
									<td colspan="9" align="center">
										<emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message>
									</td>
								</tr>											
							<%
								}
							%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="9">
									<input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec()%>" />
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="corpCode" class="hidden"></div>
				</form>
			</div>
			<%
				}
			%>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
		</div>
		<%-- foot结束 --%>
		<div class="clear"></div>
		<div id="singledetail" style="padding:5px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
			<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
		</div>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
		<script type="text/javascript">
		
		var zTree3;
		var setting3;
		var deptArray=[];
		var zNodes3 =[];

		//获取机构代码
		setting3 = {									
				async : true,				
				asyncUrl : "<%=path%>/ydyw_crmBillQuery.htm?method=createDeptTree&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lguserid=<%=request.getParameter("lguserid")%>", //获取节点数据的URL地址
			    isSimpleData : true,
				rootPID : 0,
				treeNodeKey : "id",
				treeNodeParentKey : "pId",
				asyncParam: ["depId"],	
				callback: {
					click: zTreeOnClick3,					
					asyncSuccess:function(event, treeId, treeNode, msg){
						if(!treeNode){
						   var rootNode = zTree3.getNodeByParam("level", 0);
						   zTree3.expandNode(rootNode, true, false);
						}
					}
				}
		};


		//隐藏机构树形控件
		function showMenu() {
			hideMenu();
			var sortSel = $("#depNam");
			var sortOffset = $("#depNam").offset();
			$("#dropMenu").toggle();
		}
		
		//隐藏机构树形控件
		function hideMenu() {
			$("#dropMenu").hide();
		}
		
		//选中的机构显示文本框
		function zTreeOnClick3(event, treeId, treeNode) {
			if (treeNode) {
				$("#depNam").attr("value", treeNode.name); //设置机构属性
				$("#deptString").attr("value", treeNode.id); //设置机构代码	
				
			}
			
		}		
		
		//选中的机构显示文本框
		function zTreeOnClickOK3() {
				hideMenu();
		}

		// 加载机构树形控件
		function reloadTree() {
			hideMenu();
			zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
		}
   		
		//开始时间
		function setime(){
		    var max = "2099-12-31";
		    var v = $("#recvtime").attr("value");
		    var min = "1900-01-01";

			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:v});

		}
		//发送起止时间控制
		function retime(){
		    var max = "2099-12-31";
		    var v = $("#sendtime").attr("value");
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});

		}
		
		$(document).ready(function(){
			getLoginInfo("#corpCode");
			var findresult="<%=findResult%>";
			closeTreeFun(["dropMenu"],[""]);
		    if(findresult=="-1")
		    {
		       alert(getJsLocaleMessage("ydyw","ydyw_text_error_1"));
		       return;			       
		    }
		    
			reloadTree();
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
			
			var checkSubmitFlag = true;
			$('#search').click(function(){
			
			   checkSubmit();
			
			});			
			
			function checkSubmit(){
			    if(checkSubmitFlag ==true){
			         $("a[name='research']").attr("title",getJsLocaleMessage("ydyw","ydyw_text_error_3"));
			         submitForm();//选择好时间段，才允许查询 		        
			         checkSubmitFlag = false;			         
			    }
			    else{
			      // $("a[name='research']").attr("title","您已经点过了查询按钮，请稍等!");		      
			    }
			}

			$('#packageState').isSearchSelect({'width':'178','select_height':'20','isInput':false});
			$('#content select').isSearchSelectNew({'width':'80','select_height':'34','isInput':false},function(o){
				var str = o.value;
				var strArr = str.split("-");
				//alert(strArr[0]+"//"+strArr[1]+"//"+strArr[2])
				changeState(strArr[0],strArr[1],strArr[2]);
			});
			$("#singledetail").dialog({
				autoOpen: false,
				//modal: true,
				width: 250,
				height: 200
			});

			
		});
		
		function numberControl(va)
		{
			var pat=/^\d*$/;
			if(!pat.test(va.val()))
			{
				va.val(va.val().replace(/[^\d]/g,''));
			}
		}

		function cleanSelect_dep()
		{
			$('#depNam').attr('value', '');
			$('#depNam').attr('value', getJsLocaleMessage("common","common_pleaseSelect"));
			$('#deptString').attr('value', '');
		}

		function showAddBusPkg(){
			
			location.href='<%=path%>/ydyw_busPkgMgr.htm?method=toAdd&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>';
		}
		function toEdit(pkgId){
			location.href='<%=path%>/ydyw_busPkgMgr.htm?method=toEdit&pkgId='+pkgId+'&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>';
		}

		function changeState(state,stated,pkgId){//要改的状态，本来的状态，业务包ID
			var lguserid = $("#lguserid").val();
			var lgcorpcode = $("#lgcorpcode").val();
			if(state==stated){
				return;
			}else{
				$.ajax({
					type:"POST",
					url: "ydyw_busPkgMgr.htm",
					data: {method: "changeState",pkgId:pkgId,pkgState:state,isAsync:"yes"},
					success: function(result){
						if(result=='success'){
							alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_49"));
							submitForm();
						}else{
							alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_50"));
						}
					},
					error: function(){alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywtcgl_text_51"));}
				})
			}
		}
		//包含业务显示
		function showDetial(t,title)
		{
			$("#msg").children("xmp").empty();
			$("#msg").children("xmp").text($(t).children("label").children("xmp").text());
			$("#singledetail").dialog("option","title", title);
  			$("#singledetail").dialog("open");
		}		
		</script>
	</body>
</html>
