<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.entity.system.LfPageField"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="com.montnets.emp.jfcx.vo.CrmBillQueryVo"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));

	PageInfo pageInfo = new PageInfo();
	if(request.getAttribute("pageInfo")!=null){
		pageInfo = (PageInfo) request.getAttribute("pageInfo");
	}

	int succ = 0;
	int rfail2 = 0;
	if(session.getAttribute("succ") != null && session.getAttribute("rfail2") != null)
	{
		succ = Integer.parseInt(session.getAttribute("succ").toString());

		rfail2 = Integer.parseInt(session.getAttribute("rfail2").toString());
	}
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	//Date date = new Date();
	Calendar c = Calendar.getInstance();
	c.add(Calendar.DAY_OF_MONTH, -1);
	String beginTime = df.format(c.getTime()).substring(0, 8) + "01";
	String endTime = df.format(c.getTime()).substring(0, 11); //change by dj

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");

	@SuppressWarnings("unchecked")
	List<CrmBillQueryVo> resultListt = (List<CrmBillQueryVo>) session.getAttribute("resultList");
	List<LfPageField> pagefileds = (List<LfPageField>) session.getAttribute("pagefileddeps");
	String menuCode = titleMap.get("sysDepReport");

	menuCode = menuCode == null ? "0-0-0" : menuCode;
	String findResult = null;
	findResult = (String) request.getAttribute("findresult");
	String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
	String msType = (String) request.getAttribute("msType");
	//电话号码
	String mobile="";
	//姓名
	String custoname="";
	//证件号
	String identno="";
	//扣费帐号
	String debitaccount="";

	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
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
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
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

				<form name="pageForm" action="ydyw_crmBillQuery.htm" method="post"
					id="pageForm">

					<input type="hidden" id="deptString" name="deptString"
						value="<%=request.getAttribute("deptString") == null ? "" : request.getAttribute("deptString")%>" />
					<input type="hidden" id="sp" name="sp"
						value="<%=request.getAttribute("sp") == null ? "" : request.getAttribute("sp")%>" />
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%
							if(btnMap.get(menuCode + "-5") != null)
								{
						%>
						<a id="exportCurrent"></a>
						<a id="exportCondition"></a>

						<input type="hidden" name="menucode" id="menucode" value="17" />
						<%
							}
						%>

						<a id="exportCondition"><emp:message key="common_export" defVal="导出" fileName="common"></emp:message></a>
						<span id="backgo" class="right mr5" onclick="javascript:showback()"><emp:message key="common_btn_10" defVal="返回" fileName="common"></emp:message></span>

					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_45" defVal="扣费状态：" fileName="ydyw"></emp:message>
									</td>
									<td>
										<select name="deductionstype" id="deductionstype" style="width: 182px">
												<option value="0" <%if("0".equals(request.getParameter("deductionstype"))){ %> selected="selected" <%} %>><emp:message key="common_all" defVal="全部" fileName="common"></emp:message></option>
												<option value="1" <%if("1".equals(request.getParameter("deductionstype"))){ %> selected="selected" <%} %>><emp:message key="ydyw_qytjbb_ywtcjftj_text_9" defVal="成功" fileName="ydyw"></emp:message></option>
												<option value="2" <%if("2".equals(request.getParameter("deductionstype"))){ %> selected="selected" <%} %>><emp:message key="ydyw_qytjbb_ywtcjftj_text_10" defVal="失败" fileName="ydyw"></emp:message></option>
										</select>
									</td>
									<td>
										<emp:message key="ydyw_ywgl_ywbgl_text_43" defVal="扣费时间：" fileName="ydyw"></emp:message>
									</td>
									<td class="tableTime">

										<input type="text" style="cursor: background-color: white;width: 181px;"	class="Wdate" readonly="readonly" onclick="setime()"
											value="<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>"
											id="sendtime" name="begintime">
									</td>
									<td>
										<emp:message key="common_to" defVal="至：" fileName="common"></emp:message>
									</td>
									<td>
										<input type="text" style="cursor: pointerund-color: white;width: 180px;"	class="Wdate" readonly="readonly" onclick="retime()"
											value="<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>"
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
									<emp:message key="ydyw_ywgl_ywbgl_text_38" defVal="手机号码" fileName="ydyw"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywmbpz_text_56" defVal="姓名" fileName="ydyw"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_qyjfcx_khqygl_text_10_p" defVal="证件号码" fileName="ydyw"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_36" defVal="计费类型" fileName="ydyw"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_qytjbb_jgjftj_text_6" defVal="资费（元）" fileName="ydyw"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_40" defVal="扣费账号" fileName="ydyw"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_44" defVal="扣费状态" fileName="ydyw"></emp:message>
								</th>
								<th>
									<emp:message key="ydyw_ywgl_ywbgl_text_42" defVal="扣费时间" fileName="ydyw"></emp:message>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>
									18612345678
								</td>
								<td>
									<emp:message key="ydyw_qytjbb_ywtcjftj_text_12" defVal="张三" fileName="ydyw"></emp:message>
								</td>
								<td>
									350627198708165089
								</td>
								<td>
									<emp:message key="ydyw_ywgl_tczlsz_text_by" defVal="包月" fileName="ydyw"></emp:message>
								</td>
								<td>
									3
								</td>
								<td>
									6225 xxxx
								</td>
								<td>
									<emp:message key="ydyw_qytjbb_ywtcjftj_text_9" defVal="成功" fileName="ydyw"></emp:message>
								</td>
								<td>
									2014-11-23 14:33:00
								</td>
							</tr>
							<%
								if(isFirstEnter)
									{
							%>
							<tr>
								<td colspan="8" align="center">
									<emp:message key="ydyw_qyjfcx_khjfcx_text_1" defVal="请点击查询获取数据" fileName="ydyw"></emp:message>
								</td>
							</tr>
							<%
								}
									else if(succ == 0 && rfail2 == 0)
									{
							%>
							<tr>
								<td colspan="8" align="center">
									<emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message>
								</td>
							</tr>
							<%
								}
									else
									{
							%>

							<%
								if(findResult != null)
										{
							%>

							<tr>
								<td colspan="8" align="center">
									<emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message>
								</td>
							</tr>
							<%
								}
										else
										{
							%>
							<%
								for (int i = 0; i < resultListt.size(); i++)
											{
												CrmBillQueryVo mdreportVo = resultListt.get(i);
							%>
							<tr>
								<%
									//时间格式显示处理  eg：2012年12月12日 
													String btime = request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime;
													String etime = request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime;
													String showTime = "";
													if(!"".equals(btime) && null != btime && 0 != btime.length())
													{
														String btemp[] = btime.split("-");

														btime = btemp[0] + MessageUtils.extractMessage("common","text_year",request) + btemp[1] + MessageUtils.extractMessage("common","text_month",request) + btemp[2] + MessageUtils.extractMessage("common","text_day",request);

													}

													if(!"".equals(etime) && null != etime && 0 != etime.length())
													{
														String etemp[] = etime.split("-");

														etime = etemp[0] + MessageUtils.extractMessage("common","text_year",request) + etemp[1] + MessageUtils.extractMessage("common","text_month",request) + etemp[2] + MessageUtils.extractMessage("common","text_day",request);
													}

													showTime = btime + " - " + etime;
													session.setAttribute("showTime", showTime);
								%>
								<td><%=showTime%></td>
								<td class="textalign"><%=mdreportVo.getDepname() == null ?  MessageUtils.extractMessage("ydyw","ydyw_qytjbb_ywtcjftj_text_8",request) : mdreportVo.getDepname().toString().replace("<", "&lt;").replace(">", "&gt;")%>
								</td>
								<td>
									<%=session.getAttribute("datasourcename")!=null?session.getAttribute("datasourcename"):"--" %>		
								</td>
							</tr>
							<%
								}
								}
								}
							%>
						</tbody>

						<tfoot>
							<tr>
								<td colspan="8">
									<input type="hidden" name="pageTotalRec" id="pageTotalRec"
										value="<%=pageInfo.getTotalRec()%>" />
									<input type="hidden" name="queryTime" id="queryTime"
										value="<%=request.getParameter("begintime") == null ? "" : request.getParameter("begintime")%><emp:message key="text_to_p" defVal="至" fileName="common"></emp:message><%=request.getParameter("endtime") == null ? "" : request.getParameter("endtime")%>" />
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
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<link rel="stylesheet"
			href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<script type="text/javascript"
			src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
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

			WdatePicker({dateFmt:'yyyy-MM-dd',minDate:min,maxDate:v});

		}
		//发送起止时间控制
		function retime(){
		    var max = "2099-12-31";
		    var v = $("#sendtime").attr("value");
			WdatePicker({dateFmt:'yyyy-MM-dd',minDate:v,maxDate:max});

		}
		
		
		function showback()
		{
		   var lgcorpcode =$("#lgcorpcode").val();
		   var lguserid =$("#lguserid").val();
		   location.href="ydyw_crmBillQuery.htm?lgcorpcode="+lgcorpcode+"&lguserid="+lguserid;
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
			
				var judgeSendtime = $("#sendtime").val();//开始时间
				
				var judgeendtime = $("#recvtime").val();//结束时间
				
				var datasourcetype = $("#datasourcetype").val();//数据源
				
				if(datasourcetype==null||""==datasourcetype)
				{
					alert(getJsLocaleMessage("ydyw","ydyw_qytjbb_ywtcjftj_text_11"));
					return;
				}
				
				if("" == $.trim(judgeSendtime) || "" == $.trim(judgeendtime) )
				{
					alert(getJsLocaleMessage("ydyw","ydyw_text_error_2"));
				}else
				{
				   checkSubmit();
				}
			
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
			
			
			//导出全部数据到excel
			$("#exportCondition").click(
			 function()
			 {
				
				  if(confirm(getJsLocaleMessage("ydyw","ydyw_text_error_5")))
				   {

				   		var queryTime=encodeURIComponent(encodeURIComponent($("#queryTime").val()));
				   		
				   		var sendtime = '<%=request.getParameter("begintime") != null ? request.getParameter("begintime") : beginTime%>';
				   		
				   		var recvtime = '<%=request.getParameter("endtime") != null ? request.getParameter("endtime") : endTime%>';
				   		
				   	    var depNam = '<%=request.getAttribute("deptString") == null ? "" : request.getAttribute("deptString")%>';
				   	    
				   	    var datasourcetype='<%=request.getParameter("datasourcetype") == null ? "" : request.getParameter("datasourcetype")%>'
				   	    
				   	    var mstype = '<%=msType == null ? "" : msType%>';
				   		<%if(succ == 0 && rfail2 == 0)
			{%>
				   			alert(getJsLocaleMessage("ydyw","ydyw_text_error_4"));
				   		<%}
			else
			{%>
				   		
					   		<%List<CrmBillQueryVo> list = (List<CrmBillQueryVo>) session.getAttribute("resultList");

				if(list != null && list.size() > 0)
				{%>			
							   		location.href="<%=path%>/ydyw_crmBillQuery.htm?method=r_sdRptExportExcel&lguserid=<%=request.getParameter("lguserid")%>&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&queryTime="+queryTime+"&begintime="+sendtime+"&endtime="+recvtime+"&depNam="+depNam+"&mstype="+mstype+"&datasourcetype="+datasourcetype;
					   		<%}
				else
				{%>
					   		alert(getJsLocaleMessage("ydyw","ydyw_text_error_4"));
					   		<%}
			}%>
				   }				 
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
		
		  

		</script>
	</body>
</html>
