<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.shorturl.report.vo.BatchVisitVo" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

	PageInfo pageInfo = new PageInfo();
	pageInfo=(PageInfo)request.getAttribute("pageInfo");

	@ SuppressWarnings("unchecked")
	List<BatchVisitVo> mtList =(List<BatchVisitVo>) request.getAttribute("viewBatchVisits");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
//	String titlePath = (String)request.getAttribute("titlePath");
	String titlePath = "/surlBatchVisit";
    String menuCode = titleMap.get(titlePath);

	String actionPath = (String)request.getAttribute("actionPath");
	menuCode = menuCode==null?"0-0-0":menuCode;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	BatchVisitVo vo = (BatchVisitVo) request.getAttribute("BatchVisitVo");

	String currUserid = (String)request.getAttribute("currUserid");

	if(vo.getIsContainsSun()==null || "".equals(vo.getIsContainsSun())){
		vo.setIsContainsSun("1");
	}

	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

	boolean isFirstEnter = (Boolean) request.getAttribute("isFirstEnter");
	String findResult= (String)request.getAttribute("findresult");

	String depid= (String)request.getAttribute("depid");
	String depNam= (String)request.getAttribute("depNam");
	String userid= (String)request.getAttribute("userid");
	String userName= (String)request.getAttribute("userName");
	String isContainsSun= (String)request.getAttribute("isContainsSun");

	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String httpUrl = StaticValue.getFileServerViewurl();
	String qxz = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_144", request);
	String cs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_223", request);

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<title>sendSMS.html</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
        <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	    <%--<link href="<%=inheritPath %>/styles/easyui.css?V=<%=StaticValue.JSP_IMP_VERSION %>" rel="stylesheet" type="text/css" />--%>
		
		<style type="text/css">
			body div#tooltip { position:absolute; z-index:1000; max-width:435px; _width:expression(this.scrollWidth > 435 ? "435px" : "auto");  width:auto; background:#A8CFF6; border:#FEFFD4 solid 1px; text-align:left; padding:6px;}
			body div#tooltip p { margin:0;padding:6; color:#FFFFFE;font:12px verdana,arial,sans-serif;}
			body div#tooltip p em { display:block;margin-top:3px; color:#f60;font-style:normal; font-weight:bold;}
			.container {
				float: left;
				min-width: 1220px;
				_width: 1220px;
			}
			.maxwidth{
				margin: 0 auto;
				width: 80px;
				display: block;
				white-space: nowrap;
				overflow: hidden;
				-o-text-overflow: ellipsis;
				text-overflow: ellipsis;
			}
		</style>
	</head>

	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<input type="hidden" name="httpUrl" id="httpUrl" value="<%=httpUrl %>">
			<input type="hidden" name="ISCLUSTER" id="isCluster" value="<%=StaticValue.getISCLUSTER() %>">
			<%--复用批次发送统计的加载树的方法，设置值为surlBatchRecord.htm--%>
			<input type="hidden" name="htmName" id="htmName" value="surlBatchRecord.htm">
			<%-- 短信账号--%>
			<input type="hidden" name="smsAccount" id="smsAccount" value="<%=StaticValue.SMSACCOUNT %>">
			<form name="pageForm" action="<%=actionPath %>?method=find" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						<a id="exportCondition"><emp:message key="dxzs_xtnrqf_button_12" defVal="导出" fileName="dxzs"/></a>
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
								<%--发送主题--%>
								<td><emp:message key="dxzs_xtnrqf_title_2" defVal="发送主题" fileName="dxzs"/>：</td>
								<td><input type="text" id="title" name ="title" style="width: 158px" value="<%=vo.getTitle()== null?"":vo.getTitle() %>"></td>
								<%--任务批次--%>
								<td><emp:message key="dxzs_xtnrqf_title_159" defVal="任务批次" fileName="dxzs"/>：</td>
								<td>
									<input type="text" id="taskID" name ="taskID" style="width: 158px" onkeyup="numberControl($(this))" value="<%=vo.getTaskId()==null?"":vo.getTaskId()%>" maxlength="19">
								</td>
								<%--长地址--%>
								<td>长链接：</td>
								<td>
									<input type="text" id="longUrl" name ="longUrl" style="width: 158px" value="<%=vo.getNetUrl()==null?"":vo.getNetUrl()%>">
								</td>
								<td class="tdSer">
									<center><a id="search"></a></center>
								</td>
							</tr>
								<tr>
									<%--隶属机构--%>
									<td>
										<emp:message key="dxzs_xtnrqf_title_143" defVal="隶属机构" fileName="dxzs"/>：
									</td>									
									<td class="condi_f_l">											
									  		<div style="width:180px;">
									  		 <input type="hidden" id="deptid" name="depid" value="<%=depid==null?"":depid%>"/>
									  		<input type="text" class="treeInput" id="depNam" name="depNam" value="<%=depNam==null?qxz:depNam%>"
									  			onclick="javascript:showMenu();"  readonly style="width:138px;cursor: pointer;"/>&nbsp;
											</div>
											<div id="dropMenu" >
												<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>	
												<div style="margin-top: 3px;margin-right:10px;text-align:center">
												 <input type="checkbox" id="isContainsSun" name="isContainsSun" <%if("1".equals(isContainsSun)){%>checked="checked" <%}%> value="1" style="width:15px;height:15px;"/><emp:message key="dxzs_xtnrqf_title_145" defVal="包含子机构" fileName="dxzs"/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='dxzs_xtnrqf_button_4' defVal='确定' fileName='dxzs'/>" class="btnClass1" onclick="zTreeOnClickOK3();" style=""/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='dxzs_xtnrqf_button_14' defVal='清空' fileName='dxzs'/>" class="btnClass1" onclick="cleanSelect_dep3();" style=""/>
												</div>	
												<ul  id="dropdownMenu" class="tree"></ul>	
											</div>	
									</td>
									<%--操作员--%>
									<td>
										<emp:message key="dxzs_xtnrqf_title_146" defVal="操作员" fileName="dxzs"/>：
									</td>
									<td class="condi_f_l">											
											<div style="width:180px;">											
											 <input type="hidden" id="userid" name="userid" value="<%=userid==null?"":userid %>"/>
											<input type="text" id="userName" class="treeInput" name="userName" value="<%=userName==null?qxz:userName%>" onclick="showMenu2();" readonly
											style="width:138px;cursor: pointer;"/>&nbsp;
											</div>											
											<div id="dropMenu2">
												<iframe style="position: absolute; z-index: -1; width: 100%; height: 100%; top: 0; left: 0; scrolling: no;" frameborder="0" src="about:blank"></iframe>	
												<div style="margin-top: 3px;margin-right:10px;text-align:right">
													<input type="button" value="<emp:message key='dxzs_xtnrqf_button_4' defVal='确定' fileName='dxzs'/>" class="btnClass1" onclick="zTreeOnClickOK2();" style=""/>&nbsp;&nbsp;
													<input type="button" value="<emp:message key='dxzs_xtnrqf_button_14' defVal='清空' fileName='dxzs'/>" class="btnClass1" onclick="cleanSelect_dep();" style=""/>
												</div>	
												<div style="margin-top:3px;padding-left:4px;"><font class="zhu"><emp:message key="dxzs_xtnrqf_title_147" defVal="注：请勾选操作员进行查询" fileName="dxzs"/></font></div>
												<ul  id="dropdownMenu2" class="tree"></ul>	
											</div>										   										
									</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<%--发送时间--%>
									<td>
								        <emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/>：
								   </td>
								   <td>
								    	<input type="text" style="cursor: pointer; width: 158px; background-color: white;" readonly="readonly" class="Wdate" onclick="stime()" value="<%=vo.getStartSendTime()== null?"":vo.getStartSendTime() %>"  id="sendtime" name="sendtime"/>
								   </td>
									<%--至--%>
								   <td><emp:message key="dxzs_xtnrqf_title_135" defVal="至" fileName="dxzs"/>：</td>
								   <td>
								      	<input type="text" style="cursor: pointer; width: 158px; background-color: white;" readonly="readonly" class="Wdate"  onclick="rtime()" value="<%=vo.getEndSendTime()==null?"":vo.getEndSendTime() %>" id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
								    </td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table id="content">
						<thead>
							<tr>
								<th>
									<emp:message key="dxzs_xtnrqf_title_146" defVal="操作员" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_143" defVal="隶属机构" fileName="dxzs"/>
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_2" defVal="发送主题" fileName="dxzs"/>
								</th>
								<th style="width: 5%;">
									<emp:message key="dxzs_xtnrqf_title_159" defVal="任务批次" fileName="dxzs"/>
								</th>
								<th>
									信息内容
								</th>
								<th>
									长链接
								</th>
								<th style="width: 10%;">
									<emp:message key="dxzs_xtnrqf_title_25" defVal="发送时间" fileName="dxzs"/>
								</th>
								<th style="width: 10%;">
									有效时间
								</th>
								<th>
									<emp:message key="dxzs_xtnrqf_title_191" defVal="号码个数" fileName="dxzs"/>
								</th>
								<th>
									访问人数
								</th>
								<th>
									访问次数
								</th>
								<th>
									号码详情
								</th>
							</tr>
						</thead>
						<tbody>
							<%
							if(isFirstEnter)
							{
							%>
							<tr><td colspan="19" align="center"><emp:message key="dxzs_xtnrqf_title_164" defVal="请点击查询获取数据" fileName="dxzs"/></td></tr>
							<%
							} else if(mtList != null && mtList.size()>0)
							{
								for(BatchVisitVo bv : mtList)
								{
							%>
							<tr align="center">
							<td>
								<%=bv.getUserName() %><%if(bv.getUserState()==2){out.print("<font color='red'>(已注销)</font>");} %>
							</td>
							<td>
								<xmp><%=bv.getDepName() %></xmp>
							</td>
							<td>
								<span ><%=bv.getTitle()==null?"":(bv.getTitle().replaceAll("<","&lt;").replaceAll(">","&gt;")) %></span>
							</td>
							<td>
								<%=bv.getTaskId()==0?"-":bv.getTaskId()%>
							</td>
							<td>
                                <%if(bv.getMsg()==null || "".equals(bv.getMsg())){
                                    out.print("-");
                                }else{ %>
                                          <a onclick = "modify(this,'信息内容')">
                                        <xmp><%
                                        String st = "";
                                        String temp = bv.getMsg().replaceAll("#[pP]_(\\d+)#","{#"+cs+"$1#}");
                                        if(temp.length()>5) {
                                            st = temp.substring(0,5)+"...";
                                        }else {
                                            st = temp;
                                        }
                                        out.print(st);
                                        %></xmp>
											  <textarea style="display:none"><%=temp.replaceAll(bv.getDomainUrl(),"<a href='"+ bv.getNetUrl() +"' target='_blank' title='" + bv.getNetUrl() + "' style='color: blue;text-decoration: none'>"+ bv.getDomainUrl() +"</a>") %></textarea>
										  </a>
                                <%} %>
							</td>
							<td>
								<%if(bv.getNetUrl()==null || "".equals(bv.getNetUrl())){
									out.print("-");
								}else{ %>
								<a onclick = "modify(this,'长链接')" title="<%=bv.getNetUrl()%>">
									<xmp><%
										String st = "";
										String temp = bv.getNetUrl();
										if(temp.length()>25) {
											st = temp.substring(0,25)+"...";
										}else {
											st = temp;
										}
										out.print(st);
									%></xmp>
									<textarea style="display:none"><%=temp %></textarea>
								</a>
								<%} %>
								<%--长地址 --%>
							</td>
							<td>
								<%=bv.getPlanTime()==null?"":sdf.format(bv.getPlanTime()) %><%--发送时间 --%>
							</td>
							<td>
								<%=bv.getInvalidTm()==null?"":sdf.format(bv.getInvalidTm()) %><%--有效时间 --%>
							</td>
							<td>
								<%=bv.getEffCount()==null?"":bv.getEffCount()%><%--号码个数--%>
							</td>
							<td>
								<%=bv.getVisitorCount()==null?"":bv.getVisitorCount()%><%--访问人数--%>
							</td>
							<td>
								<%=bv.getVisitCount()==null?"":bv.getVisitCount()%><%--访问次数--%>
							</td>
							<td>
								<a href="javascript:changeHisInfo('<%=bv.getTaskId()%>')">查看</a><%--号码详情--%>
							</td>
							<%
								}
							}else{
						%>
						<tr><td align="center" colspan="19"><emp:message key="dxzs_xtnrqf_title_87" defVal="无记录" fileName="dxzs"/></td></tr>
						<%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="12">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					<div id="smssendparams" class="hidden"></div>
				</form>
			</div>
			<div id="detailDialog" style="padding: 0px; display: none; width: 550px;">
			</div>
			<%-- 内容结束 --%>
			<%--信息内容弹出框--%>
			<div id="modify" title=""  style="padding:5px;width:300px;height:160px;display:none">
				<span><div id="msgcont" style="width:100%;height:100%;"></div></span>
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
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/checkFile.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/smt_smsSendedBox.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath %>/js/smt_smsTaskRecord.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
            $('#condition input[type="text"]').unbind('keyup blur');
		    // getLoginInfo("#smssendparams");
		    //参数是要隐藏的下拉框的div的id数组，
		    closeTreeFun(["dropMenu2","dropMenu"]);
			var findresult="<%=findResult%>";
		    if(findresult=="-1")
		    {
		       alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_77'));	
		       return;			       
		    }

			$("#title").live("keyup blur",function(){
					var value=$(this).val();
					if(value!=filterString(value)){
						$(this).val(filterString(value));
					}
			});
			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
            $('#spUser').isSearchSelect({'width':'160','isInput':true,'zindex':0});
			//导出全部数据到excel
			$("#exportCondition").click(
			function() {
				if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_210'))) {
			   	    var userid = $("#userid").val();
			   	    var deptid = $("#deptid").val();
			   	    var title = $("#title").val();
                    var taskId = $("#taskID").val();
                    var longUrl = $("#longUrl").val();
                    var sendtime = $("#sendtime").val();
                    var recvtime = $("#recvtime").val();
                    var isContainsSun =$("#isContainsSun").val();
			   		<%
			   		if(mtList!=null && mtList.size()>0){
			   		  if(pageInfo.getTotalRec()<=500000){
			   		%>	
		   				$.ajax({
								type: "POST",
								url: "<%=path%>/surlBatchVisit.htm?method=batchVisitReportExcel",
								data: {
                                    userid:userid,
                                    deptid:deptid,
                                    title:title,
                                    taskId:taskId,
                                    longUrl:longUrl,
                                    sendtime:sendtime,
                                    recvtime:recvtime,
                                    isContainsSun:isContainsSun
									  },
				                beforeSend:function () {
									page_loading();
				                },
				                complete:function () {
							    	page_complete();
				                },
								success: function(result){
				                        if(result==='true'){
				                            //复用批次发送统计的下载方法
				                           download_href("<%=path%>/surlBatchRecord.htm?method=downloadFile&type=BatchVisit");
				                        }else{
				                            alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_211'));
				                        }
					   			}
							});	
			   		<%	
			   		}else{%>
			   		    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_272'));
			   		<%}
			   		}else{
			   		%>
			   		    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_212'));
			   		<%
			   		}%>
				}				 
			});
			
		});
        function changeHisInfo(taskId){
            page_loading();
            var path = '<%=path%>';
            var lgguid = '<%=currUserid%>';
            //记录当前页的分页信息
            var pageSize = $("#pageSize").val();
            var pageIndex = $("#txtPage").val();
            var totalPage = $("#totalPage").val();
            var totalRec = $("#totalRec").val();
            window.location.href = path+"/surlBatchVisit.htm?method=getPhoneNumDetail" + "&taskId="+taskId+ "&lgguid="+lgguid+ "&pageIndex="+pageIndex+ "&totalPage="+totalPage+ "&totalRec="+totalRec+ "&pageSize="+pageSize;
        }
	</script>
	</body>
</html>
