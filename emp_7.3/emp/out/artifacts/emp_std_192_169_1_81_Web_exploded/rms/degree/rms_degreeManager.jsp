<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.rms.degree.vo.LfDegreeManageVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="java.sql.Timestamp" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	//环境路径
	String path = request.getContextPath();
	//计划+服务名+服务端口
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	//URL路径.substring(从0开始，截至索引位置)
	String iPath = request.getRequestURI().substring(0,
	request.getRequestURI().lastIndexOf("/"));
	//从0开始，截至索引位置
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	//从0开始，截至索引位置
	String commonPath = inheritPath.substring(0, inheritPath
	.lastIndexOf("/"));
	//分页页数
	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	if(pageInfo==null){
		pageInfo = new PageInfo();
	}
	//设置时间
	//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//是否第一次访问
	Boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	//获取计费档位管理集合
	@ SuppressWarnings("unchecked")
	LfDegreeManageVo lfdegree = (LfDegreeManageVo)request.getAttribute("LfDegreeManageVo");
	if(lfdegree==null){
		lfdegree=new LfDegreeManageVo();	
	}
	List<LfDegreeManageVo> chaVoList=(List<LfDegreeManageVo>)request.getAttribute("chaVoList");
	//获取档位数
	List<Integer> chaDegree = (List<Integer>)request.getAttribute("chaDegree");
	//档位
	String degree = "";
	//状态
	String state = "";
	//创建人
	String userid = "";
	
	if (lfdegree.getDegree() != null) {
		degree = lfdegree.getDegree().toString();
	} else {
		degree = "";
	}

	if (lfdegree.getStatus() != null) {
		state = lfdegree.getStatus().toString();
	} else {
		state = "";
	}

	if (lfdegree.getUserName() != null) {
		userid = lfdegree.getUserName();
	} else {
		userid = "";
	}

	//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");

	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	String menuCode = titleMap.get("degreeManager");
	//获取值
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><emp:message key="rms_fxapp_dwtjbb_xjjfdw" defVal="新建计费档位" fileName="rms"/></title>
<%@ include file="/common/common.jsp"%>
	<link href="<%=commonPath%>/common/css/frame.css" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css" rel="stylesheet" type="text/css" />
	<link href="<%=skin%>/frame.css" rel="stylesheet" type="text/css" />
	<link href="<%=skin%>/table.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css" />
	<link rel="stylesheet" href="<%=skin%>/newjqueryui.css" type="text/css">
	<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />
	<style rel="stylesheet" type="text/css">
		.c_input{
			padding-left: 0px!important;
		}
	</style>
</head>
<body>
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>

	<div id="container" class="container">
		<div id="rContent" class="rContent">
			<form name="pageForm" action="rms_degreeManager.htm?method=find"
				method="post" id="pageForm">
				<%--新建和删除--%>
				<div class="buttons">
					<%--
								if(chaVoList != null && chaVoList.size() >0)
								{
									for(LfChannelManagerVo channel : chaVoList)
									{
							--%>
					<%--隐藏搜索--%>
					<div id="toggleDiv"></div>
					<%if(btnMap.get(menuCode + "-1") != null){%>
						<a id="add" href="javascript:add();"><emp:message key="rms_fxapp_dwtjbb_create" defVal="新建" fileName="rms"/></a>
					<%}%>
					<%--'<%=channel.getId() %>' --%>
					<%--
									}
								}
						--%>
				</div>
				<%--查询--%>
				<div id="condition">
					<table>
						<tbody>
							<tr>
								<td><emp:message key="rms_fxapp_fxsend_chargerange" defVal="计费档位：" fileName="rms"/></td>
								<td><label> <select style="width: 180px;"
										name="degree" id="degree" isInput="false">
											<option value=""><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
										<%
												if ( chaDegree != null &&  chaDegree.size() > 0) {
														for (int i = 0; i <  chaDegree.size(); i++) {
											%>
										<option value="<%=chaDegree.get(i).toString()%>"
										<%=chaDegree.get(i).toString().equals(request.getParameter("degree"))? "selected": ""%>><%=chaDegree.get(i).toString()%><emp:message key="rms_fxapp_myscene_levelunit" defVal="档" fileName="rms"/></option>
										<%} %>
										<%} %>
									</select> </label>
								</td>
									
<%-- 								<td><select style="width:180px;" id="degree" name="degree"> --%>
<%-- 										<option value="">全部</option> --%>
<%-- 										<option value="1" <%="1".equals(degree)? "selected"  : ""%>>1档</option> --%>
<%-- 										<option value="2" <%="2".equals(degree) ? "selected" : ""%>>2档</option> --%>
<%-- 										<option value="3" <%="3".equals(degree) ? "selected" : ""%>>3档</option> --%>
<%-- 										<option value="4" <%="4".equals(degree) ? "selected" : ""%>>4档</option> --%>
<%-- 										<option value="5" <%="5".equals(degree) ? "selected" : ""%>>5档</option> --%>
<%-- 								</select> --%>
<%-- 								</td> --%>

								<td><emp:message key="rms_fxapp_dwtjbb_status" defVal="状态：" fileName="rms"/></td>

								<td><select style="width:180px;" id="status" name="status" isInput="false">
										<option value=""><emp:message key="common_whole" defVal="全部" fileName="common"/></option>
										<option value="0" <%="0".equals(state) ? "selected" : ""%>><emp:message key="rms_fxapp_myscene_enabled" defVal="已启用" fileName="rms"/></option>
										<option value="1" <%="1".equals(state) ? "selected" : ""%>><emp:message key="rms_fxapp_myscene_disabled" defVal="已禁用" fileName="rms"/></option>
										<option value="2" <%="2".equals(state) ? "selected" : ""%>><emp:message key="rms_fxapp_dwtjbb_outdate" defVal="已过期" fileName="rms"/></option>
								</select>
								<td><emp:message key="rms_fxapp_myscene_creator" defVal="创建人：" fileName="rms"/></td>
								<td><input type="text" id="userName" name="userName"
									maxlength="25" style="width: 154px;" value="<%=userid%>"></td>
								<td class="tdSer">
									<center>
										<a id="search"></a>
									</center></td>
							</tr>

						</tbody>
					</table>
				</div>
				<%--查询显示--%>
				<table id="content">
					<thead>
						<tr>
							<th style="width: 10%"><emp:message key="rms_fxapp_dwtjbb_chargerange2" defVal="计费档位" fileName="rms"/></th>
							<th style="width: 10%"><emp:message key="rms_fxapp_dwtjbb_rlfw" defVal="容量范围" fileName="rms"/></th>
							<th><emp:message key="rms_fxapp_dwtjbb_cjr" defVal="创建人" fileName="rms"/></th>
							<th><emp:message key="rms_fxapp_dwtjbb_cjsj" defVal="创建时间" fileName="rms"/></th>
							<th style="width: 15%"><emp:message key="rms_fxapp_dwtjbb_yxkssj" defVal="有效开始时间" fileName="rms"/></th>
							<th style="width: 15%"><emp:message key="rms_fxapp_dwtjbb_yxjzsj" defVal="有效截止时间" fileName="rms"/></th>
							<th style="width: 10%"><emp:message key="rms_fxapp_dwtjbb_status2" defVal="状态" fileName="rms"/></th>
							<%if(btnMap.get(menuCode + "-3") != null){%>
							<th style="width: 10%"><emp:message key="rms_fxapp_dwtjbb_operation" defVal="操作" fileName="rms"/></th>
							<%}%>
						</tr>
					</thead>
					<tbody>
						<%
							if (chaVoList != null && chaVoList.size() > 0) {
								for (LfDegreeManageVo degreevo : chaVoList) {
						%>

						<tr>
							<%-- 计费档位 --%>
							<td><%=degreevo.getDegree() != null ? degreevo
							.getDegree() : 0%></td>
							<%
								String getDegreeBegin = degreevo.getDegreeBegin() != null ? degreevo
												.getDegreeBegin() : "";
							%>
							<%
								String getDegreeEnd = degreevo.getDegreeEnd() != null ? degreevo
												.getDegreeEnd() : "";
							%>
							<%-- 容量范围 --%>
							<td><%=getDegreeBegin%>KB-<%=getDegreeEnd%>KB</td>
							<%-- 操作员名称 --%>
							<td><%=degreevo.getUserName()!=null?degreevo.getUserName():""%></td>
							<%-- 时间格式处理 --%>
								<%
									Timestamp creatTime = degreevo.getCreateTime();
									Timestamp validDateBegin = degreevo.getValidDateBegin();
									Timestamp validDateEnd = degreevo.getValidDateEnd();
								 %>
							
							<%-- 创建时间 --%>
							<td><%=creatTime!=null?creatTime.toString().substring(0,creatTime.toString().lastIndexOf(".")):0%></td>
							<%-- 有效开始时间 --%>
							<td><%=validDateBegin!=null?validDateBegin.toString().substring(0,validDateBegin.toString().lastIndexOf(".")):0%></td>
							<%-- 有效截止时间 --%>
							<td><%=validDateEnd!=null?validDateEnd.toString().substring(0,validDateEnd.toString().lastIndexOf(".")):0%></td>
							<%-- 状态 --%>
							<%
								if(degreevo.getStatus()!= null && degreevo.getStatus() == 0 ){
							%>
							<td>已启用</td>
							<%
								}else if(degreevo.getStatus()!= null && degreevo.getStatus() == 1){
							%>
							<td>已禁用</td>
							<%
								}else if(degreevo.getStatus()!= null && degreevo.getStatus() == 2){
							%>
							<td>已过期</td>
							<%
								}else{
							%>
							<td><%=""%></td>
							<%
								}
							%>

							<%-- 修改 --%>
							<%if(btnMap.get(menuCode + "-3") != null){%>
							<td><a href="javascript:update('<%=degreevo.getId()%>','<%=degreevo.getDegree()%>','<%=degreevo.getDegreeBegin()%>',
							'<%=degreevo.getDegreeEnd()%>','<%=degreevo.getValidDateBegin()%>','<%=degreevo.getValidDateEnd()%>','<%=degreevo.getStatus()%>'
							);"><emp:message key="rms_fxapp_dwtjbb_modify" defVal="修改" fileName="rms"/></a>
							</td>
							<%}%>
						</tr>
						<%
							}
																				}else
																				{
						%>
						
						<tr>
							<td colspan="8">
								<%if(isFirstEnter!=null&&isFirstEnter){%> <emp:message key="rms_fxapp_fsmx_clickquery" defVal="请点击查询获取数据" fileName="rms"/> <%}else{%> <emp:message key="rms_fxapp_fsmx_norecord" defVal="无记录" fileName="rms"/> <%}%>
							</td>
						</tr>
						<%
							}
						%>
					</tbody>

					<tfoot>
						<tr>
							<td colspan="8">
								<div id="pageInfo"></div></td>
						</tr>
					</tfoot>
				</table>
			</form>
		</div>
	</div>
	<%-- 内容结束 --%>
	<%-- foot开始 --%>
	<div class="bottom">
		<div id="bottom_right">
			<div id="bottom_left"></div>
			<div id="bottom_main"></div>
		</div>
	</div>

	<div class="clear"></div>

	<%-- 新建计费档位 --%>
			 	<div id="addDiv"  
		style="text-align: center;padding: 5px; width: 100px; display: none; overflow-y: auto; overflow-x: auto;"> 
		<div>
			<table   cellpadding="0" cellspacing="0" border="0" width="90%"
				style="margin-top: 20px; margin-left: 25px;"> 
				<tr style="height: 49px;">
					<td><emp:message key="rms_fxapp_fxsend_chargerange" defVal="计费档位：" fileName="rms"/></td>
					<td ><input id="addDegree" name="addDegree" type="text" maxlength="4" style="width: 350px ;margin-left:-65px; "> <emp:message key="rms_fxapp_myscene_levelunit" defVal="档" fileName="rms"/></td>
				</tr>
				<tr style="height: 49px;">
					<td><emp:message key="rms_fxapp_dwtjbb_rlfw2" defVal="容量范围：" fileName="rms"/></td>
					<td><input id="addDegreeBegin" name="addDegreeBegin" type="text" maxlength="4"  style="width: 150px;margin-left:-60px"> KB
					<emp:message key="rms_fxapp_dwtjbb_dao" defVal="到" fileName="rms"/>
					<input id="addDegreeEnd" name="addDegreeEnd" type="text" maxlength="4" style="width: 150px;"> KB</td>
				</tr>
				<tr style="height: 49px;">
					<td><emp:message key="rms_fxapp_dwtjbb_validatetime" defVal="有效时间：" fileName="rms"/></td>
					<td>
					<input id="addValidDateBegin" name="addValidDateBegin" type="text" style="width: 170px;margin-left:-50px;cursor: pointer;"
					class="Wdate" onclick="sedtime()"> 
					<emp:message key="rms_fxapp_dwtjbb_dao" defVal="到" fileName="rms"/>
					<input id="addValidDateEnd" name="addValidDateEnd" type="text" style="width: 170px;cursor: pointer;"
					class="Wdate" onclick="revtime()">&nbsp;&nbsp;&nbsp;&nbsp;
					
				</tr>
				<tr style="height: 100px;">
						<td id="btn" colspan="5"
							style="text-align: center; padding: 15px;"><input
							name="addsubBut" type="button" id="addsubBut" value="<emp:message key="common_confirm" defVal="确定" fileName="common"/>"
							class="btnClass5 mr23" onclick="javascript:addType();" /> <input
							name="addcancelwid" id="addcancelwid" type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"/>"
							class="btnClass6"
							onclick="javascript:$('#addDiv').dialog('close')" />
						</td>
					</tr>
			</table>
		</div>
	</div>



	<%-- 修改计费档位 --%>
	<div id="editDiv"  
		style="text-align: center;padding: 5px; width: 100px; display: none; overflow-y: auto; overflow-x: auto;"> 
		<div>
			<table   cellpadding="0" cellspacing="0" border="0" width="90%"
				style="margin-top: 20px; margin-left: 25px;"> 
				<tr style="height: 49px;">
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="rms_fxapp_myscene_level" defVal="档位：" fileName="rms"/></td>
					<td><input id="editDegree" name="editDegree" type="text" maxlength="4" style="width: 446px;margin-left:-30px;" /></td>
				</tr>
				<tr style="height: 49px;">
					<td><emp:message key="rms_fxapp_dwtjbb_rlfw2" defVal="容量范围：" fileName="rms"/></td>
					<td><input id="editDegreeBegin" name="editDegreeBegin" type="text" maxlength="4" style="width: 200px;margin-left:-10px;"> KB
					<emp:message key="rms_fxapp_dwtjbb_dao" defVal="到" fileName="rms"/>
					<input id="editDegreeEnd" name="editDegreeEnd" type="text" maxlength="4" style="width: 200px;"> KB</td>
				</tr>
				<tr style="height: 49px;">
					<td><emp:message key="rms_fxapp_dwtjbb_validatetime" defVal="有效时间：" fileName="rms"/></td>
					<td>
					<input id="editValidDateBegin" name="editValidDateBegin" type="text" style="width: 200px;margin-left:-15px;cursor: pointer;"
					class="Wdate" onclick="sedtimeUpdate()"> &nbsp;&nbsp;&nbsp; &nbsp;  
					<emp:message key="rms_fxapp_dwtjbb_dao" defVal="到" fileName="rms"/>
					<input id="editValidDateEnd" name="editValidDateEnd" type="text" style="width: 200px;cursor: pointer;"
					class="Wdate" onclick="revtimeUpdate()">&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<tr style="height: 49px;">
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key="rms_fxapp_dwtjbb_status" defVal="状态:" fileName="rms"/></td>
					<td >
						<select id="editStatus" name="editStatus" style="width: 450px;margin-left:-30px;">
							<option value="0"><emp:message key="rms_fxapp_myscene_enabled" defVal="已启用" fileName="rms"/></option>
							<option value="1"><emp:message key="rms_fxapp_myscene_disabled" defVal="已禁用" fileName="rms"/></option>
							<option value="2"><emp:message key="rms_fxapp_dwtjbb_outdate" defVal="已过期" fileName="rms"/></option>
						</select>
					</td>
				</tr>
				<tr>
					<td id="btn" colspan="5"
						style="text-align: center; padding: 15px;">
						<input name="editSubBut" type="button" id="editSubBut" value="<emp:message key="common_confirm" defVal="确定" fileName="common"/>"
						class="btnClass5 mr23" onclick="javascript:editDegreeManager();" /> 
						<input name="editcancelwid" id="editcancelwid" type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="rms"/>"
						class="btnClass6" onclick="javascript:$('#editDiv').dialog('close')" />
					</td>
				</tr>
			</table>
		</div>
	</div>




	<script type="text/javascript"
		src="<%=commonPath%>/common/js/myjquery-a.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/pageInfo.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/common.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/jquery.selectnew.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js"></script>
	<script type="text/javascript"
		src="<%=commonPath%>/common/js/jquery_Ul_Send.js?V=116"></script>
	<script type="text/javascript" src="<%=iPath%>/js/channelManage.js"></script>
	
	<script type="text/javascript">
		$(document).ready(
				function() {
					closeTreeFun([ "dropMenu" ]);
					getLoginInfo("#hiddenValueDiv");
					noquot($("#busNameEdit"));
					noquot($("#busNameAdd"));
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
					initPage(
					<%=pageInfo.getTotalPage()%>
						,
					<%=pageInfo.getPageIndex()%>
						,
					<%=pageInfo.getPageSize()%>
						,
					<%=pageInfo.getTotalRec()%>
						);
					$('#search').click(function() {
						submitForm();
					});

					$("#addDiv").dialog({
						autoOpen : false,
						width : 650,
						height : 350,
						title : getJsLocaleMessage("rms","rms_rangemng_createrange"),
						modal : true,
						resizable : false
					});
					
					 $("#editDiv").dialog({
				autoOpen: false,
			    width:650,
			    height:350,
			    title:getJsLocaleMessage("rms","rms_rangemng_modifyrange"),
			    modal:true,
			    resizable:false
			 });
					
				});
		function add() {
			var date = new Date();
    		var seperator1 = "-";
    		var seperator2 = ":";
   			var year = date.getFullYear();
   			var year2 = date.getFullYear() + 50;
   			var month = date.getMonth() + 1;
    		var strDate = date.getDate();
    		var hour = date.getHours();
    		var minutes = date.getMinutes();
    		var seconds = date.getSeconds();
    		if (month >= 1 && month <= 9) {
        		month = "0" + month;
    		}
    		if (strDate >= 1 && strDate <= 9) {
        		strDate = "0" + strDate;
    		}
    		if (hour >= 0 && hour <= 9) {
    			hour = "0" + hour;
    		}
    		if (minutes >= 0 && minutes <= 9) {
    			minutes = "0" + minutes;
    		}
    		if (seconds >=0 && seconds <= 9) {
    			seconds = "0" + seconds;
    		}
    		var validDateBegin = year + seperator1 + month + seperator1 + strDate
         	   + " " + hour + seperator2 + minutes + seperator2 + seconds;
    		var validDateEnd =  year2 + seperator1 + month + seperator1 + strDate
         	   + " " + hour + seperator2 + minutes + seperator2 + seconds;
         	$("#addValidDateBegin").val(validDateBegin);
         	$("#addValidDateEnd").val(validDateEnd);
         	$("#addDiv").dialog("open");
		};
		
		
		
	//发送起止时间控制
	//有效时间结束
	function revtime(){
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    
	    var max = date.getFullYear()+50 + seperator1 + month + seperator1 + strDate
	              +" 23:59:59";
	    var v = $("#addValidDateBegin").attr("value");
	    var min = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes()
	            + seperator2 + date.getSeconds();
	    
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max,enableInputMask:false});
	
	}

	//有效时间开始
	function sedtime(){
	    
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    var hour = date.getHours();
	    var minute = date.getMinutes();
	    var second = date.getSeconds();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    
	    var max = date.getFullYear()+50 + seperator1 + month + seperator1 + strDate
	              +" 23:59:59";
	   
	    var min = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes()
	            + seperator2 + date.getSeconds();
	            
   
		 WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:max,enableInputMask:false});
	}
		
	function revtimeUpdate(){
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    
	    var max = date.getFullYear()+50 + seperator1 + month + seperator1 + strDate
	              +" 23:59:59";
	    var v = $("#editValidDateBegin").attr("value");
	    var min = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes()
	            + seperator2 + date.getSeconds();
	    
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max,enableInputMask:false});
	
	}

	//有效时间开始
	function sedtimeUpdate(){
	    
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    
	    var max = date.getFullYear()+50 + seperator1 + month + seperator1 + strDate
	              +" 23:59:59";
	   
	    var min = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + date.getMinutes()
	            + seperator2 + date.getSeconds();
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:max});
	}
	
	$('#degree').isSearchSelect({'width':'180','zindex':1,'isInput':false});
	$('#status').isSearchSelect({'width':'180','zindex':1,'isInput':false});
	</script>
</body>
</html>









