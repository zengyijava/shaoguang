<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.montnets.emp.entity.pasroute.LfSpDepBind"%>
<%@page import="com.ibm.db2.jcc.b.s"%>
<%@page import="com.montnets.emp.shorturl.surlmanage.vo.LfUrlTaskVo"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
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
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String menuCode = titleMap.get("/surlTaskRecord");
	menuCode = menuCode == null ? "0-0-0" : menuCode;
	
	@SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String,String>)request.getAttribute("conditionMap");
	String userid, depid, spUser, taskState, taskType, taskID, startSubmitTime,endSubmitTime, msg,userId,depNam,userName;
	
	userid = conditionMap.get("userid")==null?"":conditionMap.get("userid");
	depid = conditionMap.get("depid")==null?"":conditionMap.get("depid");
	spUser = conditionMap.get("spUser")==null?"":conditionMap.get("spUser");
	taskState = conditionMap.get("taskState")==null?"":conditionMap.get("taskState");
	taskType = conditionMap.get("taskType")==null?"":conditionMap.get("taskType");
	taskID = conditionMap.get("taskID")==null?"":conditionMap.get("taskID");
	startSubmitTime = conditionMap.get("startSubmitTime")==null?"":conditionMap.get("startSubmitTime");
	endSubmitTime = conditionMap.get("endSubmitTime")==null?"":conditionMap.get("endSubmitTime");
	msg = conditionMap.get("msg")==null?"":conditionMap.get("msg");
	depNam = conditionMap.get("depNam")!=null?conditionMap.get("depNam"):null;
    //登录操作员信息
    LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
    String lgcorpcode = loginSysuser != null ? loginSysuser.getCorpCode() : "";

	userId = (String)request.getAttribute("userId");
	userName = (String)request.getAttribute("userName");

	//是否包含子机构
	String isContainsSun=(String)request.getAttribute("isContainsSun");
	if(isContainsSun==null||"".equals(isContainsSun)){
		isContainsSun="1";
	}
	
	@SuppressWarnings("unchecked")
	Map<String, String> usersMap = (HashMap<String,String>)request.getAttribute("usersMap");
	@SuppressWarnings("unchecked")
	List<LfSpDepBind> userList = (List<LfSpDepBind>)request.getAttribute("userList");
	@SuppressWarnings("unchecked")
	List<LfUrlTaskVo> mtVoList = (List<LfUrlTaskVo>)request.getAttribute("mtVoList");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>群发任务查看</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
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
			<%--存储常用数据--%>
				<div style="display:none" id="hiddenValueDiv"></div>
			<form name="pageForm" action="surlTaskRecord.htm" method="post" id="pageForm">
				<%-- <input type="hidden" id="lguserid" name="lguserid" value="<%=userId %>">
                <input type="hidden" id="lgcorpcode" name="lgcorpcode" value="<%=lgcorpcode %>"> --%>
				<div id="r_sysMoRparams" class="hidden"></div>
				<div class="buttons">
					<div id="toggleDiv">
					</div>
					<%--<a id="exportCondition">导出</a>--%>
				</div>
				<div id="condition">
					<table>
						<tbody>
							<tr>
								<td>
									机构:
								</td>
								<td class="condi_f_l">
									<div style="width:220px;">
							  		 	<input type="hidden" id="deptid" name="depid" value="<%=depid==null?"":depid%>"/>
							  			<input type="text" id="depNam" class="treeInput" name="depNam" value="<%=depNam==null?MessageUtils.extractMessage("xtgl","xtgl_cswh_twgl_qxz",request):depNam%>"  onclick="showMenu();"  readonly
							  			style="cursor: pointer;width: 181px;"/>
									</div>
									<div id="dropMenu" >
										<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>
										<div style="margin-top: 3px;margin-right:10px;text-align:right;">
										     <input type="checkbox" id="isContainsSun" name="isContainsSun" <%if("1".equals(isContainsSun)){%>checked="checked" <%}%> value="1" style="width:15px;height:15px;vertical-align:middle;margin-right:3px;"/>包含子机构&nbsp;&nbsp;
											<input type="button" value="确定" class="btnClass1" onclick="zTreeOnClickOK3();" style=""/>&nbsp;&nbsp;
											<input type="button" value="清空" class="btnClass1" onclick="cleanSelect_dep3();" style=""/>
										</div>
										<ul id="dropdownMenu"  class="tree"></ul>
									</div>
								</td>
								<td>
									操作员:
								</td>
								<td class="condi_f_l">
									<div style="width:220px;">
										<input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
										<input type="text" class="treeInput" id="userName" name="userName" value="<%=userName==null?MessageUtils.extractMessage("xtgl","xtgl_cswh_twgl_qxz",request):userName%>" readonly onclick="showMenu2();" style="cursor: pointer;width: 181px;"/>&nbsp;
									</div>
									<div id="dropMenu2">
										<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>
										<div style="margin-top: 3px;margin-right:10px;text-align:right">
											<input type="button" value="确定" class="btnClass1" onclick="zTreeOnClickOK2();" style=""/>&nbsp;&nbsp;
											<input type="button" value="清空" class="btnClass1" onclick="cleanSelect_dep();" style=""/>
										</div>
										<div style="margin-top:3px;padding-left:4px;"><font class="zhu"><emp:message key="dxzs_xtnrqf_title_147" defVal="注：请勾选操作员进行查询" fileName="dxzs"/></font></div>
										<ul id="dropdownMenu2" class="tree"></ul>
									</div>
								<%--<select id=userid name="userid" style="width: 181px;" isInput="false">--%>
										<%--<option value="">全部</option>--%>
										<%--<%--%>
										<%--if(usersMap!=null&&usersMap.size()>0){--%>
											<%--for(Map.Entry<String,String> entry:usersMap.entrySet()){--%>
												<%----%>
												<%--if(!"".equals(userid) && entry.getKey().equals(userid)){--%>
													<%--%>--%>
													<%--<option value="<%=entry.getKey() %>" selected="selected"><%=entry.getValue() %></option>--%>
													<%--<%--%>
												<%--}else{--%>
													<%--%>--%>
													<%--<option value="<%=entry.getKey() %>"><%=entry.getValue() %></option>--%>
													<%--<%	--%>
												<%--}--%>
											<%--}--%>
										<%--}--%>
										<%--%>--%>
									<%--</select>--%>
								</td>
								<td>
									SP账号:
								</td>
								<td>
									<select id="spUser" name="spUser" style="width: 181px;">
										<option value="">全部</option>
										<%
										if(userList!=null && userList.size()>0){
											for(LfSpDepBind lfSpDepBind:userList){
												if(!"".equals(spUser) && spUser.equals(lfSpDepBind.getSpUser())){
													%>
													<option value="<%=lfSpDepBind.getSpUser() %>" selected="selected"><%=lfSpDepBind.getSpUser() %></option>
													<%
												}else{
													%>
													<option value="<%=lfSpDepBind.getSpUser() %>"><%=lfSpDepBind.getSpUser() %></option>
													<%
												}
											}
										}
										%>
									</select>
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
							    </td>
							</tr>
							<tr>
								<td>
									生成状态:
								</td>
								<td>
									<select id="taskState" name="taskState" style="width: 181px;" isInput="false">
										<option value="">全部</option>
										<option value="0" <%="0".equals(taskState)?"selected":"" %>>未处理</option>
										<option value="1" <%="1".equals(taskState)?"selected":"" %>>处理中</option>
										<option value="2" <%="2".equals(taskState)?"selected":"" %>>处理完成</option>
										<option value="3" <%="3".equals(taskState)?"selected":"" %>>处理失败</option>
									</select>
								</td>
								<td>
									发送类型:
								</td>
								<td>
									<select id="taskType" name="taskType" style="width: 181px;" isInput="false">
										<option value="">全部</option>
										<option value="1" <%="1".equals(taskType)?"selected":"" %>>相同内容发送</option>
										<option value="2" <%="2".equals(taskType)?"selected":"" %>>不同内容发送</option>
									</select>
								</td>
								<td>
									任务批次:
								</td>
								<td>
									<input type="text" style="width: 181px;" value="<%=taskID==null?"":taskID %>" id="taskID" name="taskID" />
								</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>
									创建时间:
								</td>
								<td class="tableTime">
									<input type="text"
										style="cursor: pointer; width: 181px; background-color: white;"
										class="Wdate" readonly="readonly" onclick="sedtime()"
										value="<%=startSubmitTime == null ? "" : startSubmitTime%>" id="startSubmitTime"
										name="startSubmitTime">
								</td>
								<td>
									至：
								</td>
								<td>
									<input type="text"
										style="cursor: pointer; width: 181px; background-color: white;"
										class="Wdate" readonly="readonly" onclick="revtime()"
										value="<%=endSubmitTime == null ? "" : endSubmitTime%>" id="endSubmitTime"
										name="endSubmitTime">
								</td>
								<td>
									信息内容:
								</td>
								<td>
									<input type="text" style="width: 181px;" value="<%=msg==null?"":msg %>" id="msg" name="msg" />
								</td>
								<td>&nbsp;</td>
							</tr>
						</tbody>
					</table>
				</div>
				<table id="content">
					<thead>
						<tr>
							<th>
								操作员
							</th>
							<th>
								隶属机构
							</th>
							<th>
								SP账号
							</th>
							<th>
								发送类型
							</th>
							<th>
								发送主题
							</th>
							<th>
								任务批次
							</th>
							<th>
								创建时间
							</th>
							<th>
								发送时间
							</th>
							<th>
								有效号码数
							</th>
							<th>
								短信内容
							</th>
							<th>
								发送文件
							</th>
							<th>
								生成状态
							</th>
							<th>
								操作
							</th>
						</tr>
					</thead>
					<tbody>
						<%
						if(mtVoList!=null && mtVoList.size()>0){
							for(LfUrlTaskVo lfUrlTaskVo : mtVoList){
							%>
							<tr>
								<td>
									<label><%=lfUrlTaskVo.getName() %></label>
								</td>
								<td>
									<label><%=lfUrlTaskVo.getDepname() %></label>
								</td>
								<td>
									<label><%=lfUrlTaskVo.getSpUser() %></label>
								</td>
								<td>
									<label>
									<%if(lfUrlTaskVo.getUtype()==1){
										out.print("相同内容发送");
									}else{
										out.print("不同内容发送");
									}%>
									</label>
								</td>
								<td>
									<label><%=lfUrlTaskVo.getTitle() %></label>
								</td>
								<td>
									<label><%=lfUrlTaskVo.getTaskId() %></label>
								</td>
								<td>
									<label><%=sdf.format(lfUrlTaskVo.getCreatetm())  %></label>
								</td>
								<td>
									<label><%=sdf.format(lfUrlTaskVo.getSendtm())  %></label>
								</td>
								<td>
									<label><%=lfUrlTaskVo.getSubCount()%></label>
								</td>
								<td>
									<%
									if(lfUrlTaskVo.getMsg()!=null && !"".equals(lfUrlTaskVo.getMsg())){
										%>
										<a onclick=modify(this)>
										<xmp>
										<%
										String st = "";
										String temp = lfUrlTaskVo.getMsg().replaceAll("#[pP]_(\\d+)#","{#参数$1#}");
										if(temp.length()>7)
										{
											st = temp.substring(0,7)+"...";
										}else
										{
											st = temp;
										}
										out.print(st);
										%>
										</xmp>
										<textarea style="display:none"><%=temp %></textarea>
										<input type="hidden" value="<%=lfUrlTaskVo.getNetUrl() %>"/>
										</a>
										<%
									}else{
										out.print("--");
									}
									%>
								</td>
								<td>
									<a href="javascript:checkFileOuter('<%=lfUrlTaskVo.getSrcfile()%>','<%=path%>')">查看</a>
									&nbsp;
									<a href="javascript:downloadFilesOuter('<%=lfUrlTaskVo.getSrcfile()%>','<%=path%>')">下载</a>
								</td>
								<td>
									<label>
									<%
									if(lfUrlTaskVo.getStatus()==10||lfUrlTaskVo.getStatus()==20){
										out.print("未处理");
									}else if(lfUrlTaskVo.getStatus()==11||lfUrlTaskVo.getStatus()==21){
										out.print("处理中");
									}else if(lfUrlTaskVo.getStatus()==12||lfUrlTaskVo.getStatus()==22){
										out.print("处理完成");
									}else if(lfUrlTaskVo.getStatus()==13||lfUrlTaskVo.getStatus()==23){
										out.print("处理失败");
									}else{
										out.print("--");
									}
									%>
									</label>
								</td>
								<td>
									<%
									//添加操作
									if(lfUrlTaskVo.getStatus()==13||lfUrlTaskVo.getStatus()==23){%>
                                        <a href="javascript:void(0)" onclick="checkFails('<%=lfUrlTaskVo.getId()%>')" >查看</a>
										<a name="rsend"  href="javascript:reSend('<%=lfUrlTaskVo.getId()%>')">失败补发</a>
									<% }else{
										out.print("--");
									}
									%>
								</td>
							</tr>
							<%
							}
						}else{
							%>
							<tr><td colspan="13" align="center">无记录</td></tr>
							<%
						}
						%>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="13">
							    <input type="hidden" name="pageTotalRec" id="pageTotalRec" value="<%=pageInfo.getTotalRec() %>"/>
								<div id="pageInfo"></div>
							</td>
						</tr>
					</tfoot>
				</table>
			</form>
		</div>

		<div id="modify" title="信息内容"  style="padding:5px;width:300px;height:160px;display:none">
			<table width="100%">
				<thead>
					<tr style="padding-top:2px;margin-bottom: 2px;">
						<td style='word-break: break-all;'>
							<span>
							<textarea id="msgcont" style="width:100%;height:90%;border:0px;" rows="15" readonly="readonly"></textarea>
							</span>
							<a id="orUrl" href="" style="color: blue;font-size: 15px;" target="_blank"  rel="noopener noreferrer nofollow" onclick="">原始连接</a>
						</td>
						<td>

						</td>
					</tr>
				</thead>
			</table>
		</div>

        <div id="failMessage" style="padding: 5px;width: 300px;height: 160px;display:none">
            <span id="failDetails"></span>
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
  	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
       <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	 <script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/frame/frame2.5/js/showbox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  	<script type="text/javascript">
		$(document).ready(function() {
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		});
  	</script>
  	<script type="text/javascript" src="<%=iPath %>/js/url_taskRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
  </body>
</html>
