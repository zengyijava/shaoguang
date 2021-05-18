<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Calendar"%>
<%@page import="java.util.LinkedHashMap"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.shorturl.surlmanage.vo.LfNeturlVo"%>
<%
	String langName = (String)session.getAttribute("emp_lang");
	
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.indexOf("/",1));

	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String menuCode = titleMap.get("record");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	
	@SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String,String>)request.getAttribute("conditionMap");
	String urlname, srcurl, urlstate,ispass,creatuser,startTime,recvtime,lguserid;
	urlname = conditionMap.get("urlname")==null?"":conditionMap.get("urlname");
	srcurl = conditionMap.get("srcurl")==null?"":conditionMap.get("srcurl");
	urlstate = conditionMap.get("urlstate")==null?"":conditionMap.get("urlstate");
	ispass = conditionMap.get("ispass")==null?"":conditionMap.get("ispass");
	creatuser = conditionMap.get("creatuser")==null?"":conditionMap.get("creatuser");
	startTime = conditionMap.get("startTime")==null?"":conditionMap.get("startTime");
	recvtime = conditionMap.get("recvtime")==null?"":conditionMap.get("recvtime");
	lguserid = conditionMap.get("lguserid")==null?"":conditionMap.get("lguserid");
	
	String deptid = conditionMap.get("depId")!=null?conditionMap.get("depId"):null;
	String depNam = conditionMap.get("depNam")!=null?conditionMap.get("depNam"):null;
	
	//是否包含子机构
	String isContainsSun=(String)request.getAttribute("isContainsSun");
	if(isContainsSun==null||"".equals(isContainsSun)){
		isContainsSun="1";
	}
	
	@SuppressWarnings("unchecked")
	List<LfNeturlVo> neturlVos = (List<LfNeturlVo>) request.getAttribute("urlList");
	
	//@SuppressWarnings("unchecked")
	//Map<String, String> usersMap  = (Map<String, String>) request.getAttribute("usersMap");
	
	
	
    String findResult= (String)request.getAttribute("findresult");
    CommonVariables  CV = new CommonVariables();
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>短链地址报备</title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>

	</head>
	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>

			<%-- 内容开始 --%>
			<%
				if (btnMap.get(menuCode + "-0") != null)
				{
			%>
			<div id="rContent" class="rContent">
				<input type="hidden" value="<%=inheritPath %>" id="inheritPath"/>
				<form name="pageForm" action="keep_record.htm" method="post"
					id="pageForm">
					<input type="hidden" id="deptString" name="deptString"
						value="<%=request.getParameter("deptString") == null ? "" : request.getParameter("deptString")%>" />
					<input type="hidden" id="lguserid" name="lguserid" value="<%=lguserid %>">
					<div id="r_sysMoRparams" class="hidden"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1")!=null) { %>
							<a id="add" >新建</a>
						<%}%>
						<%--<%//if(btnMap.get(menuCode+"-2")!=null) { %>
							<a id="delete" onclick="javascript:delAll('<%//=path %>');">删除</a>
						<%//}%>--%>
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>

									<td>
										长链接名称：
									</td>
									<td>
										<input type="text" style="width: 181px;" value="<%=urlname==null?"":urlname %>" id="urlname"
											name="urlname" />
									</td>
									<td>
										长链接：
									</td>
									<td>
										<input type="text" style="width: 181px;" value="<%=srcurl==null?"":srcurl %>" id="srcurl"
											name="srcurl" />
									</td>
									<td>
										EMP启用状态：
									</td>
									<td>
										<label>
										<select id="urlstate" name="urlstate" style="width: 181px;" isInput="false">
											<option value="">全部</option>
											<option value="0" <%=urlstate.equals("0")?"selected":"" %> >已启用</option>
											<option value="-1" <%=urlstate.equals("-1")?"selected":"" %>>已禁用</option>
										</select>
										</label>
									</td>
									<td class="tdSer">
									             <center><a id="search"></a></center>
								    </td>
								</tr>
								<tr>
									<td>
										机构：
									</td>

									<td class="condi_f_l">
										<div style="width:220px;">
								  		 	<input type="hidden" id="deptid" name="depid" value="<%=deptid==null?"":deptid%>"/>
								  			<input type="text" id="depNam" class="treeInput" name="depNam" value="<%=depNam==null?MessageUtils.extractMessage("xtgl","xtgl_cswh_twgl_qxz",request):depNam%>"  onclick="javascript:showMenu();"  readonly
								  			style="cursor: pointer;width:160px;"/>
										</div>
										<div id="dropMenu" >
											<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>
											<div style="margin-top: 3px;margin-right:10px;text-align:right;">
											     <input type="checkbox" id="isContainsSun" name="isContainsSun" <%if("1".equals(isContainsSun)){%>checked="checked" <%}%> value="1" style="width:15px;height:15px;vertical-align:middle;margin-right:3px;"/>包含子机构&nbsp;&nbsp;
												<input type="button" value="确定" class="btnClass1" onclick="javascript:zTreeOnClickOK3();" style=""/>&nbsp;&nbsp;
												<input type="button" value="清空" class="btnClass1" onclick="cleanSelect_dep3();" style=""/>
											</div>
											<ul id="dropdownMenu"  class="tree"></ul>
										</div>
									</td>
 									<td>
										运营商审批状态：
									</td>
									<td>
										<label>
										<select id="ispass" name="ispass" style="width: 181px;" isInput="false">
											<option value="">全部</option>
											<option value="-2" <%=ispass.equals("-2")?"selected":"" %>>已禁用</option>
											<option value="0" <%=ispass.equals("0")?"selected":"" %> >待审批</option>
											<option value="1" <%=ispass.equals("1")?"selected":"" %>>无需审批</option>
											<option value="2" <%=ispass.equals("2")?"selected":"" %>>审批通过</option>
											<option value="3" <%=ispass.equals("3")?"selected":"" %>>审批不通过</option>
										</select>
										</label>
									</td>
									<td>
										创建人：
									</td>
									<td>
										<input type="text" style="width: 181px;" value="<%=creatuser == null ? "" : creatuser%>" id="creatuser"
										name="creatuser" />
									</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
								<td>
										创建时间：
									</td>
									<td class="tableTime">

										<input type="text"
											style="cursor: pointer; width: 181px; background-color: white;"
											class="Wdate" readonly="readonly" onclick="sedtime()"
											value="<%=startTime == null ? "" : startTime%>" id="startTime"
											name="startTime">
									</td>
									<td>
										至：
									</td>
									<td>
										<input type="text"
											style="cursor: pointer; width: 181px; background-color: white;"
											class="Wdate" readonly="readonly" onclick="revtime()"
											value="<%=recvtime == null ? "" : recvtime%>" id="recvtime"
											name="recvtime">
									</td>

									<td colspan=2>&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
							  <%--<th><input type="checkbox" name="checkall" id="checkall" onclick="checkAlls(this,'Id')"/></th>--%>
							  <th>长链接名称</th>
							  <th>长链接</th>
							  <th>长链接内容描述</th>
							  <th>创建人</th>
							  <th>所属机构</th>
							  <th>创建时间</th>
							  <th>运营商审批状态</th>
							  <th>EMP启用状态</th>
							</tr>
						</thead>
						<tbody>
							<%if(neturlVos != null && neturlVos.size()>0){
								for(LfNeturlVo lfVo :neturlVos){
								%>
								<tr>
									<%--<td>--%>
										<%--<input type="checkbox" name="Id" id="Id" value="<%=lfVo.getId() %>">--%>
									<%--</td>--%>
									<td width="400">
										<label><%=lfVo.getUrlname().length()>50?lfVo.getUrlname().substring(0,50)+"...":lfVo.getUrlname() %></label>
									</td>
									<td width="400">
										<label><a href="javascript:showUrl(<%=lfVo.getId()%>);"><%=lfVo.getSrcurl().length()>50?lfVo.getSrcurl().substring(0,50)+"...":lfVo.getSrcurl()  %></a></label>
										<input type="hidden" name="toUrl<%=lfVo.getId() %>" id="toUrl<%=lfVo.getId() %>" value="<%=lfVo.getSrcurl() %>"/>
									</td>
									<td>
										<a onclick=javascript:modify2(this)>
											<%
											String xmessage = lfVo.getUrlmsg()  == null?"" : lfVo.getUrlmsg();
											%>
								  			<label style="display:none"><xmp><%=xmessage %></xmp></label>
											<xmp><%=xmessage.length()>5?xmessage.substring(0,5)+"...":xmessage %></xmp>
										</a>
									</td>
									<td>
										<label>
										<%=lfVo.getName()%>
										</label>
									</td>
									<td>
										<label><%=lfVo.getDepname()%></label>
									</td>
									<td>
										<label><%=df.format(lfVo.getCreatetm()) %></label>
									</td>
									<td>
										<%
										String isstr = "";
											if(lfVo.getIspass()==-2|lfVo.getIspass()==-3){
												isstr = "已禁用";
											}else if(lfVo.getIspass()==0){
												isstr = "待审批";
											}else if(lfVo.getIspass()==1){
												isstr = "无需审批";
											}else if(lfVo.getIspass()==2){
												isstr = "审批通过";
											}else if(lfVo.getIspass()==3){
												isstr = "审批不通过 ";
											}

											if(lfVo.getIspass()==3){
												%>
												<a onclick=javascript:modify('<%=lfVo.getId()%>')><%=isstr %></a>
												<%
											}else if(lfVo.getIspass()==-2||lfVo.getIspass()==-3){
												%>
												<a onclick=javascript:showstop('<%=lfVo.getId()%>')><%=isstr%></a>
												<%
											}else{
												out.print(isstr);
											}
										%>
										<input type="hidden" value="<%=lfVo.getRemarks()%>" id="remark<%=lfVo.getId() %>" name="remark<%=lfVo.getId() %>"/>
										<input type="hidden" value="<%=lfVo.getRemarks1()%>" id="remark1<%=lfVo.getId() %>" name="remark1<%=lfVo.getId() %>"/>
									</td>
									<td>
										<center>
											<div class="evestatue_div" style="width:100%;height:24px;">
                                                <input type = "hidden" value="<%=lfVo.getId()%>">
												<select name="evestatue<%=lfVo.getId()%>" id="evestatue<%=lfVo.getId()%>" style="display:none" class="input_bd"
												onchange="javascript:changestate('<%=lfVo.getId()%>')">
												<option value="0" <%=lfVo.getUrlstate()==0?"selected":""%>>已启用 </option>
												<option value="-1"  <%=lfVo.getUrlstate()!=0?"selected":""%>>已禁用</option>
												</select>
												<span  id="evestatue<%=lfVo.getId()%>_txt"><%=lfVo.getUrlstate()==0?"已启用":"已禁用"%></span>
											</div>
										</center>
									</td>
								</tr>
								<%
								}
							}else {
								%>
								<tr><td colspan="9" align="center">无记录</td></tr>
								<%
							}
								%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="9">
								    <input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
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
		<div id="singledetail" style="padding:5px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
		</div>

		<div id="stopdetail" style="padding:5px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
				<div id="msg1" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
		</div>

		<div id="showUrl" style="display:none;" >
			<iframe  id ="kkuu" name="kkuu" style="padding:5px;height: 337px;overflow: auto;" src="" ></iframe>
		</div>
		 <div id="modify" title="长链接内容描述"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;">
				<table width="240px">
					<thead>
						<tr style="padding-top:2px;margin-bottom: 2px;">
							<td>
								<span style="display:block;width:240px;"><label id="msg2" style="width:100%;height:100%;"></label></span>

							</td>

						</tr>
					   <tr style="padding-top:2px;">
							<td>
							</td>
							</tr>

					</thead>
				</table>
			</div>
		<div id="addDiv" style="display:none">
				<table style="height:100%;font-size: 12px;margin-left: 15px;">
					<tr style="height: 45px;">
						<td align="right" style="padding-right: 15px;">
							长链接名称：
						</td>
						<td>
							<input type="text" style="width: 266px;height: 23px;line-height: 23px;" class="input_bd" name="urlNameAdd" id="urlNameAdd" maxlength="50"/>
						</td>
						<td>
							<font color="red">*</font>
						</td>
					</tr>
					<tr style="height: 35px;">
						<td align="right" style="padding-right: 15px;">
							长链接：
						</td>
						<td>
							<input type="text"  style="width: 266px;" class="input_bd" name="urlCodeAdd" id="urlCodeAdd"  maxlength="1024"/>
						</td>
						<td>
							<font color="red">*</font>
						</td>
					</tr>
					<tr style="height: 170px;">
						<td align="right" style="padding-right: 15px;">
							长链接内容描述：
						</td>
						<td width="72%">
						<textarea style="height:150px;width: 266px; line-height: 18px;" class="input_bd"
						  name="urlDescriptionAdd" id="urlDescriptionAdd" maxlength="200"></textarea>
						 <div id="url_desc_cover" style="color:gray;position:relative;width:265px;height:0px;margin-left:5px;bottom:146px;" >为保证您的链接顺利通过运营商审核，请如实完整地描述链接。</div>
						 </td>
						<td>
							<font color="red">*</font>
						</td>
					</tr>
					<tr>
					<td colspan="2" style="text-align:center">
					<input name="addsubmit" id="addsubmit" class="btnClass5 mr23" type="button" value="确定" onclick="addUrl();"/>
					<input name="addcancel" id="addcancel" class="btnClass6" type="button" value="取消" onclick="doCancel();"/>
					<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
					<br/>
					</td>
					</tr>
				</table>
			</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/frame/frame2.5/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		var zTree3;
		var setting3;
		var zNodes3 = [];
		//获取机构代码
		setting3 = {
				async : true,
				asyncUrl : "keep_record.htm?method=createDeptTree", //获取节点数据的URL地址

				//checkable : true,
			    //checkStyle : "radio",
			    //checkType : { "Y": "s", "N": "s" },
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
					//zTree3.expandAll(false);
					}
				}
		};
		//选中的机构显示文本框
		function zTreeOnClick3(event, treeId, treeNode) {
			if (treeNode) {
				var pops="";
				var depts ="";
				$("#depNam").attr("value", treeNode.name);
				$("#deptid").attr("value",treeNode.id);

			}
		}


		$(document).ready(function() {
            $('#condition input[type="text"]').unbind('keyup blur');
			closeTreeFun(["dropMenu"]);
			getLoginInfo("#hiddenValueDiv");

			$(".evestatue_div").hover(function() {
			    var _this = $(this);
			    var id = _this.find("input").val();

                    evestatue_in(id);
            }, function() {
                var _this = $(this);
                var id = _this.find("input").val();
                    evestatue_out(id);
            })

            //IE浏览器下拉框消失未解之谜的终极解决办法
            $("select").mouseleave(function() {
                return false;
            });

			//要改
			//noquot($("#busNameEdit"));
			//noquot($("#busNameAdd"));


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
			$('#search').click(function(){submitForm();});
			$("#addDiv").dialog({
				autoOpen: false,
			    width:455,
			    height:336,
			    title:"长链接报备",
			    modal:true,
			    resizable:false
			 });

			$('#add').click(function(){
				$("#addDiv").dialog("open");
			});
			$("#singledetail").dialog({
				autoOpen: false,
				maxWidth: 350,
				maxHeight: 170,
				modal: true
			});
			$("#stopdetail").dialog({
				autoOpen: false,
				maxWidth: 350,
				maxHeight: 170,
				modal: true
			});

			$("#url_desc_cover").click(function(){
				var txt = $.trim($(this).val());
				if(txt == "") {
					$("#url_desc_cover").hide();
				}
			});



			$("#urlDescriptionAdd").focus(function(){
				var txt = $.trim($(this).val());
				if(txt == "") {
					$("#url_desc_cover").hide();
				}
			});

			$("#urlDescriptionAdd").blur(function(){
				var txt = $.trim($(this).val());
				if(txt == "") {
					$("#url_desc_cover").show();
				}
			});

			$("#showUrl").dialog({
				autoOpen: false,
				width:314,
			    height:398,
			    title:"链接预览",
				modal: true
			});
			//机构树
			var lguserid =$("#lguserid").val();
		    setting3.asyncUrl = "keep_record.htm?method=createDeptTree&lguserid="+lguserid;
//		    setting3.asyncUrl = "bit_busType.htm?method=createDeptTree&lguserid=2";
		    reloadTree(zNodes3);
		});

		function evestatue_in(id) {
                $("#evestatue" + id ).show();
                $("#evestatue" + id + "_txt").hide();
		}
		function evestatue_out(id) {
                $("#evestatue" + id ).hide();
                $("#evestatue" + id + "_txt").show();
		}

		// 加载人员/机构树形控件
		function reloadTree(zNodes3) {
			setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
			zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
			zTree3.expandAll(true);
		}
		//选中的机构显示文本框
		function zTreeOnClickOK3() {
			hideMenu();
		}
		//隐藏人员树形控件
		function hideMenu() {
			$("#dropMenu").hide();
		}
		function cleanSelect_dep3()
		{
			var checkNodes = zTree3.getCheckedNodes();
		    for(var i=0;i<checkNodes.length;i++){
		     checkNodes[i].checked=false;
		    }
		    zTree3.refresh();
		    $("#depNam").attr("value","请选择");
			$("#deptid").attr("value","");
		}

		function showMenu() {
			//hideMenu2();
			var sortSel = $("#depNam");
			var sortOffset = $("#depNam").offset();
			$("#dropMenu").toggle();
		}
	</script>
	<script type="text/javascript" src="<%=iPath %>/js/url_manage.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>
