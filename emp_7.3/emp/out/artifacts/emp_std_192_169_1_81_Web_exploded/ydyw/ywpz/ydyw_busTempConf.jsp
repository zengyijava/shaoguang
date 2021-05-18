<%@ page language="java" import="com.montnets.emp.common.constant.StaticValue" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.ydyw.ywpz.vo.LfBusTailTmpVo"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	if(null != request.getAttribute("pageInfo")){
		pageInfo = (PageInfo) request.getAttribute("pageInfo");
	}

	//当前用户权限
	int permissionType = (Integer) request.getAttribute("permissionType");

	//数据结果集
	List<LfBusTailTmpVo> busTmpList = (List<LfBusTailTmpVo>)request.getAttribute("busTmpList");

	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("busTempConf");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@include file="/common/common.jsp"%>
		<title><emp:message key="ydyw_common_text_4" defVal="业务模板配置" fileName="ydyw"></emp:message></title>
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>"
			rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
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
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">

				<form name="pageForm" action="ydyw_busTempConf.htm" method="post" id="pageForm">
					<div class="buttons">
					<div id="toggleDiv">
					</div>
					<% if(btnMap.get(menuCode+"-1")!=null) {  %>
						<a id="add"><%=empLangName.equals("zh_HK")?"&nbsp;&nbsp;":""%><emp:message key="ydyw_ywgl_ywmbpz_text_53" defVal="配置" fileName="ydyw"></emp:message></a>
					<% } %>
					<% if(btnMap.get(menuCode+"-3")!=null) {  %>
						<a id="delete"><%=empLangName.equals("zh_HK")?"&nbsp;&nbsp;":""%><emp:message key="common_delete" defVal="刪除" fileName="common"></emp:message></a>
					<% } %>
				</div>
					<div id="condition">
					<table>
						<tr>
							<td>
								<span><emp:message key="ydyw_ywgl_ywbgl_text_21" defVal="业务名称：" fileName="ydyw"></emp:message></span>
							</td>
							<td>
								<label>
									<%
									String busName = request.getAttribute("busName") == null? "":(String)request.getAttribute("busName");
									%>
									<input type="text" name="busName" id="busName" value="<%=busName %>"  style="width: 181px" maxlength="20"/>
								</label>
							</td>

							<td>
								<span><emp:message key="ydyw_ywgl_ywbgl_text_22" defVal="业务编码：" fileName="ydyw"></emp:message></span>
							</td>
							<td>
								<label>
									<%
									String busCode = request.getAttribute("busCode") == null? "":(String)request.getAttribute("busCode");
									%>
									<input type="text" name="busCode" id="busCode" maxlength="20" value="<%=busCode %>"  style="width: 181px"/>
								</label>
							</td>

							<td>
								<span><emp:message key="ydyw_ywgl_ywbgl_text_24" defVal="操作员：" fileName="ydyw"></emp:message></span>
							</td>
							<td>
								<label>
									<%
									String userName = request.getAttribute("userName") == null? "":(String)request.getAttribute("userName");
									%>
									<input type="text" name="userName" id="userName" maxlength="24" value="<%=userName %>"  style="width: 181px"/>
								</label>
							</td>

							<td class="tdSer">
								<center><a id="search"></a></center>
							</td>
						</tr>

						<tr>
							<td>
								<span><emp:message key="ydyw_ywgl_ywmbpz_text_1_p" defVal="配置时间：" fileName="ydyw"></emp:message></span>
							</td>
							<td>
								<label>
									<%
									String startTime = request.getAttribute("startTime") == null? "":(String)request.getAttribute("startTime");
									%>
									<input type="text" style="cursor: background-color: white;width: 181px;"
											class="Wdate" readonly="readonly" onclick="setime()"
											value="<%=startTime %>" id="startTime" name="startTime">
								</label>
							</td>

							<td>
								<span><emp:message key="common_to" defVal="至：" fileName="common"></emp:message></span>
							</td>
							<td>
								<label>
									<%
									String endTime = request.getAttribute("endTime") == null? "":(String)request.getAttribute("endTime");
									%>
									<input type="text" style="cursor: pointerund-color: white;width: 181px;"
											class="Wdate" readonly="readonly" onclick="retime()"
											value="<%=endTime %>" id="endTime" name="endTime">
								</label>
							</td>

							<%
							if (permissionType == 2) {
							%>
							<td>
								<span><emp:message key="ydyw_ywgl_ywbgl_text_26" defVal="机构：" fileName="ydyw"></emp:message></span>
							</td>
							<td class="condi_f_l">
								<%--<label>--%>
									<%
									String depId = request.getAttribute("depId") == null? "":(String)request.getAttribute("depId");
									%>
									<input type="hidden" name="depId" id="depId" value="<%=depId %>" />
									<div style="width: 220px;">
										<%
										String depName = request.getAttribute("depName") == null? MessageUtils.extractMessage("common","common_pleaseSelect",request):(String)request.getAttribute("depName");
										%>
										<input type="text" id="depNam" name="depNam" value="<%=depName %>"
												onclick="javascript:showMenu();"
												readonly style="width: 181px; cursor: pointer;" class="treeInput" />
											&nbsp;
										</div>
										<div id="dropMenu">
											<div style="margin-top: 3px; margin-right: 10px; text-align: right">
												<%
												String isCheck = "1";
												if(request.getAttribute("isCheck") != null) {
													isCheck = (String)request.getAttribute("isCheck");
												}
												%>
												<input id="isCheck" name="isCheck" type="checkbox"
													<% if(isCheck.equals("1")) { %>
													checked="checked" <% } %> value="1"
													style="width:15px;height:15px;vertical-align:middle;margin-right:3px;"/><emp:message key="ydyw_ywgl_ywmbpz_text_3" defVal="是否包含子机构" fileName="ydyw"></emp:message>
												<input type="button" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" class="btnClass1"	onclick="javascript:zTreeOnClickOK3();" style="width: 50px;" />&nbsp;&nbsp;
												<input type="button" value="<emp:message key="common_clean" defVal="清空" fileName="common"></emp:message>" class="btnClass1"	onclick="javascript: cleanSelect_dep();" style="width: 50px;" />
											</div>
											<ul id="dropdownMenu" class="tree"></ul>
										</div>
								<%--</label>--%>
							</td>
							<%
							} else {
							%>
							<td colspan="2">&nbsp;</td>
							<%
							}
							%>
							<td>&nbsp;</td>
						</tr>
					</table>
				</div>
				<table id="content">
					<thead>
						<tr>
							<th width="5%"><input type="checkbox" id="cks" value="<emp:message key="ydyw_qyjfcx_khjfcx_text_2" defVal="全选" fileName="ydyw"></emp:message>"/></th>
							<th width="13%">
								<emp:message key="ydyw_ywgl_ywmbpz_text_51" defVal="业务名称" fileName="ydyw"></emp:message>
							</th>
							<th width="13%">
								<emp:message key="ydyw_ywgl_ywmbpz_text_52" defVal="业务编码" fileName="ydyw"></emp:message>
							</th>
							<th width="10%">
								<emp:message key="ydyw_ywgl_ywmbpz_text_2" defVal="配置状态" fileName="ydyw"></emp:message>
							</th>
							<th width="14%">
								<emp:message key="ydyw_ywgl_ywmbpz_text_1" defVal="配置时间" fileName="ydyw"></emp:message>
							</th>
							<th width="15%">
								<emp:message key="ydyw_ywgl_ywbgl_text_23" defVal="操作员" fileName="ydyw"></emp:message>
							</th>
							<th width="15%">
								<emp:message key="ydyw_ywgl_ywbgl_text_25" defVal="机构" fileName="ydyw"></emp:message>
							</th>
							<%
							if(btnMap.get(menuCode+"-2") == null
									&& btnMap.get(menuCode+"-3") == null) {
							%>

							<%
							} else {
							%>
							<th width="15%">
								<emp:message key="common_operation" defVal="操作" fileName="common"></emp:message>
							</th>
							<%
							}
							%>
						</tr>
					</thead>
					<tbody>
					<%
					if(busTmpList != null && busTmpList.size() > 0) {
						for (LfBusTailTmpVo bus : busTmpList) {
					%>
						<tr>
							<td><input type="checkbox" name="cks" id="cks_<%=bus.getBusId() %>" value="<%=bus.getBusId() %>"/></td>
							<td>
							<%
							String bName = bus.getBusName();

							if(bName.length() > 6) {
								bName = bName.substring(0, 6) + "...";
							}

							String state = "";

							if(bus.getState() == 1) {
								state = "（"+MessageUtils.extractMessage("ydyw","ydyw_ywgl_ywbgl_text_5",request)+"）";
							}
							%>
							<input type="hidden" id="name_<%=bus.getBusId() %>" value="<%=bus.getBusName() %>"/>
							<a href="javascript:showName(<%=bus.getBusId() %>);" title="<%=bus.getBusName() %>"><%=bName %></a><%=state %>
							</td>
							<td>
							<%
							String bCode = bus.getBusCode();

							if(bCode.length() > 8) {
								bCode = bCode.substring(0, 8) + "...";
							}
							%>
							<input type="hidden" id="code_<%=bus.getBusId() %>" value="<%=bus.getBusCode() %>"/>
							<a href="javascript:showCode(<%=bus.getBusId() %>);" title="<%=bus.getBusCode() %>"><%=bCode %></a>
							</td>
							<td><emp:message key="ydyw_ywgl_ywmbpz_text_55" defVal="已配置" fileName="ydyw"></emp:message>（<%=bus.getIcount() %>）</td>
							<td>
							<%
							//配置时间
							String cfgTime = bus.getUpdateTime().toString();
							cfgTime = cfgTime.substring(0, cfgTime.lastIndexOf("."));
							%>
							<%=cfgTime %>
							</td>
							<td><%=bus.getName()==null?"":bus.getName() %>（<%=bus.getUserName()==null?"":bus.getUserName() %>）</td>
							<td><%=bus.getDepName()==null?"":bus.getDepName() %></td>

							<%
							if(btnMap.get(menuCode+"-2") != null || btnMap.get(menuCode+"-3") != null) {
							%>
								<td>
								<%
								if(btnMap.get(menuCode+"-2")!=null) {
								%>
									<a href="javascript:updateTail(<%=bus.getBusId() %>);"><emp:message key="ydyw_ywgl_ywmbpz_text_54" defVal="修改配置" fileName="ydyw"></emp:message></a>
								<%
								}

								if(btnMap.get(menuCode+"-3")!=null) {
								%>
									<a href="javascript:del(<%=bus.getBusId() %>);" style="margin-left: 5px;"><emp:message key="common_btn_5" defVal="刪除" fileName="common"></emp:message></a>
								<%
								}
								%>
								</td>
							<%
							}
							%>
						</tr>
					<%
						}
					} else {
					%>
						<tr><td colspan="8"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td>
					<%} %>

					</tbody>
					<tfoot>
						<tr>
							<td colspan="8">
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

			<%-- 完整的业务名称 --%>
			<div id="showBusName" title="<emp:message key="ydyw_ywgl_ywmbpz_text_51" defVal="业务名称" fileName="ydyw"></emp:message>">
				<div style="width: 92%;margin: 10px 0px 10px 10px;font-size: 13px;" align="left">
	    		<span id="allName"></span>
	    		</div>
			</div>

			<%-- 完整的业务编码 --%>
			<div id="showBusCode" title="<emp:message key="ydyw_ywgl_ywmbpz_text_52" defVal="业务编码" fileName="common"></emp:message>">
				<div style="width: 92%;margin: 10px 0px 10px 10px;font-size: 13px;" align="left">
	    		<span id="allCode"></span>
	    		</div>
			</div>

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
		<script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"
			type="text/javascript"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<%-- 为了解决ie8空白双击导致浏览器崩溃，增加日期控件的引用，可解决这问题 --%>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		var zTree3;
		var setting3;
		var deptArray=[];
		var zNodes3 =[];

		//获取机构代码
		setting3 = {
				async : true,
				asyncUrl : "<%=path %>/ydyw_busTempConf.htm?method=createDeptTree&lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lguserid=<%=request.getParameter("lguserid")%>", //获取节点数据的URL地址
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
			$("#depId").attr("value", treeNode.id); //设置机构代码
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

		$(document).ready(function() {
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

			$("#showBusName").dialog({
				autoOpen: false,
			    width:260,
			    height:180,
			    title:getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_51"),
			    modal:true,
			    resizable:false
			 });

			$("#showBusCode").dialog({
				autoOpen: false,
			    width:260,
			    height:180,
			    title:getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_52"),
			    modal:true,
			    resizable:false
			 });

			reloadTree();
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});

			$('#add').click(function(){

				var url = "<%=path %>/ydyw_busTempConf.htm?method=toConfig";
				//$.post(url,{method:'toConfig'},function() {});
				location.href = url;
			});

			//全选
			$('#cks').click(function() {
				if ($('#cks').is(':checked')) {
					$('[name=cks]:checkbox').attr("checked", true);
				} else {
					$('[name=cks]:checkbox').attr("checked", false);
				}
			});

			//去掉全选勾
			$('[name=cks]:checkbox').click(function() {
				$('[name=cks]:checkbox').each(function() {
					if(!this.checked) {
						$('#cks').attr("checked", false);
					}
				});
			});

			//批量删除
			$('#delete').click(function(result) {

				var ids = "";
				var count = 0;

				$('[name=cks]:checkbox').each(function() {
					if(this.checked) {
						count++;
						ids += this.value;
						ids += ",";
					}
				});

				if(count == 0) {
					alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_31"));
				} else {
					if(confirm(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_32"))) {
						var url = "<%=path %>/ydyw_busTempConf.htm";

						$.post(url,{method:'delete',delType:'2',busId:ids},
								function(result) {
							if(result > 0) {
								alert(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_33") +  result  + getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_34"));
							} else {
                                /*删除失败！*/
                                alert(getJsLocaleMessage("common","common_deleteFailed"));
							}

							var busName = $('#busName').val();
							var busCode = $('#busCode').val();
							var userName = $('#userName').val();
							var startTime = $('#startTime').val();
							var endTime = $('#endTime').val();
							var depId = $('#depId').val();

							var path = url + "?method=find&busName=" + busName + "&busCode=" + busCode + "&userName=" +
								userName + "&startTime=" + startTime + "&endTime=" + endTime + "&depId=" +
								depId + "&pageSize=<%=pageInfo.getPageSize() %>&pageIndex=<%=pageInfo.getPageIndex() %>";

							//页面跳转
							window.location.href=path;
						});
					}
				}
			});
		});

		//修改模板配置
		function updateTail(busId) {
			var url = "<%=path %>/ydyw_busTempConf.htm?method=toEdit&type=2&busId=" + busId;

			window.location.href = url;
		}

		//清除所选机构
		function cleanSelect_dep() {

			$('#depNam').attr('value', '');
			$('#depNam').attr('value', getJsLocaleMessage("common","common_pleaseSelect"));
			$('#deptString').attr('value', '');
			$("#depId").attr("value", '');
		}

		//单条记录删除
		function del(busId) {
			var url = "<%=path %>/ydyw_busTempConf.htm";
			if(confirm(getJsLocaleMessage("ydyw","ydyw_ywgl_ywmbpz_text_35"))) {
				$.post(url,{method:'delete',busId:busId,delType:'1'},
						function(result) {

					if (result == "-1" || result == "0") {
						alert(getJsLocaleMessage("common","common_deleteFailed"));
					} else {
						alert(getJsLocaleMessage("common","common_deleteSucceed"));
					}

					submitForm();
				});
			}
		}

		function showName(id) {
			var busName = $('#name_' + id).val();
			$('#allName').html(busName);
			$('#showBusName').dialog("open");
		}

		function showCode(id) {
			var busName = $('#code_' + id).val();
			$('#allCode').html(busName);
			$('#showBusCode').dialog("open");
		}

		//开始时间
		function setime(){
			var v = $("#endTime").attr("value");
			var min = "1900-01-01 00:00:00";

			WdatePicker({dateFmt:'yyyy-MM-dd  HH:mm:ss',minDate:min,maxDate:v});

		};

		//结束时间
		function retime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#startTime").attr("value");

			WdatePicker({dateFmt:'yyyy-MM-dd  HH:mm:ss',minDate:v,maxDate:max});
		};
		</script>
	</body>
</html>
