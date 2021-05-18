<%@ page language="java" import="java.util.List" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.entity.engine.LfService"%>
<%@ page import="com.montnets.emp.engine.vo.LfServiceVo"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
    LfSysuser curSysuser=(LfSysuser)session.getAttribute("loginSysuser");
	
	LfServiceVo lsv = (LfServiceVo)request.getAttribute("LfSerVo");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("mtService");
	menuCode=menuCode==null?"0-0-0":menuCode;
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.1 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode) %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link href="<%=iPath%>/css/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/mtservice.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>	
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
	</head>
	<body id="eng_mtService" class="eng_mtService">
		<div id="container">
			<%-- 当前位置 --%>
			<%--<%=com.montnets.emp.common.constant.ViewParams.getPosition(menuCode) %>
			--%><%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<div class="rContent">
				<%              		
					if(btnMap.get(menuCode+"-0")!=null)                       		
					{                        	
				%>
				<%--
				<div id="u_o_c_explain">
					<p>
						说明：下行业务项目管理
					</p>
					<ul>
						<li>
							下行业务指由企业信息机系统主动发起的业务,如通知等类型业务
						</li>
						<li>
							状态可以控制业务是运行还是禁用
						</li>
						<li>
							点击【新建】按钮可以添加一个新的下行短信业务信息
						</li>
						<li>
							点击【业务信息】链接可以修改选中的下行短信业务信息
						</li>
						<li>
							点击【触发条件】链接可以定义下行短信业务运行的规则
						</li>
						<!--
							<li>
								点击【接收群组】链接可以定义接收短信的手机用户
							</li>
						--!>
						<li>
							点击【步骤管理】链接可以定义下发给手机用户的短信来源、格式
						</li>
						<li>
							点击【删除】链接可以删除选中的下行短息业务
						</li>
					</ul>
				</div>
				--%>
				<form name="pageForm" id="pageForm" action="<%=path %>/eng_mtService.htm" method="post">
				<div id="hiddenValueDiv"></div>
				<div class="buttons">
					<div id="toggleDiv">
					</div>
					<%              		
						if(btnMap.get(menuCode+"-1")!=null)                       		
						{                        	
					%>
					<a id="add" onclick="window.location.href='eng_mtService.htm?method=toAdd&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val()"><emp:message key="znyq_ywgl_common_btn_4" defVal="新建" fileName="znyq"></emp:message></a>
					<%
						}
					%>
				</div>
				<div id="condition">
						<table>
							<tr>
								<td><emp:message key="znyq_ywgl_xhywgl_ywmc_mh" defVal="业务名称：" fileName="znyq"></emp:message></td>
								<td>
									<input type="text" name="serName" id="serName"  value="<%=lsv.getSerName()==null?"":lsv.getSerName() %>" />
								</td>
								<td><emp:message key="znyq_ywgl_xhywgl_cjzmc_mh" defVal="创建者名称：" fileName="znyq"></emp:message></td>
								<td>
									<input type="text" name="sOpName" id="sOpName" value="<%=lsv.getName()==null?"":lsv.getName() %>" maxlength="20"/>
								</td>
							
								<%--<td>拥有者名称：</td>
								<td>
									<input type="text" name="ownerName" id="ownerName" style="width:180px" value="<%=lsv.getOwnerName()==null?"":lsv.getOwnerName() %>" maxlength="20"/>
								</td>
								--%>
								<td><emp:message key="znyq_ywgl_xhywgl_zt_mh" defVal="状态：" fileName="znyq"></emp:message></td>
								<td>
									<select id="serState" name="serState" isInput="false">
									<%int ss = lsv.getRunState() == null?2:lsv.getRunState(); %>
									    <option value=""><emp:message key="znyq_ywgl_common_text_16" defVal="全部" fileName="znyq"></emp:message></option>
									    <option value="1" <%=ss==1?"selected":"" %>><emp:message key="znyq_ywgl_xhywgl_qy" defVal="启用" fileName="znyq"></emp:message></option>
									    <option value="0" <%=ss==0?"selected":"" %>><emp:message key="znyq_ywgl_xhywgl_ty" defVal="停用" fileName="znyq"></emp:message></option>
									</select>
								</td>
								<td class="tdSer">
										<center><a id="search"></a></center>
								</td>
							</tr>
						</table>
					</div>
				<table id="content" class="content_table">
					<thead>
						<tr>
							<th>
								<emp:message key="znyq_ywgl_xhywgl_bh" defVal="编号" fileName="znyq"></emp:message>
							</th>
							<th>
								<emp:message key="znyq_ywgl_xhywgl_ywmc" defVal="业务名称" fileName="znyq"></emp:message>
							</th>
							<th>
								<emp:message key="znyq_ywgl_xhywgl_cjzmc" defVal="创建者名称" fileName="znyq"></emp:message>
							</th>
							<%-- <th>
								拥有者名称
							</th> --%>
							<th>
								<emp:message key="znyq_ywgl_xhywgl_cjsj" defVal="创建时间" fileName="znyq"></emp:message>
							</th>
							<th>
								<emp:message key="znyq_ywgl_common_yxzt" defVal="运行状态" fileName="znyq"></emp:message>
							</th>
							<%              		
								if(btnMap.get(menuCode+"-24")!=null
										|| btnMap.get(menuCode+"-29")!=null
										|| btnMap.get(menuCode+"-2")!=null
										|| btnMap.get(menuCode+"-30")!=null)                       		
								{                        	
							%>
							<th colspan="5">
								<emp:message key="znyq_ywgl_common_text_14" defVal="操作" fileName="znyq"></emp:message>
							</th>
							<%} %>
						</tr>
					</thead>
					<%
						@ SuppressWarnings("unchecked")
						List<LfServiceVo> serList=(List<LfServiceVo>)request.getAttribute("serList");
						java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						if(serList!= null && serList.size() > 0)
						{
						for(LfServiceVo service : serList)
						{
					%>
					<tr>
						<td>
							<%=service.getSerId() %>
						</td> 
						<td class="textalign" >
							<xmp id="firstXmp">
							<%=service.getSerName() %>
							</xmp>
						</td>
						<td class="textalign" >
							<%=service.getName()==null?"-":service.getName() %>
							<%--(<%=service.getUserName()==null?"-":service.getUserName()%><%if(service.getUserState() !=null && service.getUserState()==2){out.print("<font color='red'>已注销</font>");} %>)--%>
							(<%=service.getUserName()==null?"-":service.getUserName()%><%if(service.getUserState() !=null && service.getUserState()==2){out.print("<font color='red'>"+MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_yzx",request)+"</font>");} %>)
							
						</td>
						<%-- <td class="textalign" >
							<%=service.getOwnerName()==null?"-":service.getOwnerName() %>
							(<%=service.getOwnerUserName()==null?"-":service.getOwnerUserName()%>)
						</td> --%>
						<td>
						<%
						if (service.getCreateTime() != null && !"-".equals(service.getCreateTime().toString()))
						{
						 %>
						<%=df.format(service.getCreateTime()) %>
						<%}%>
						</td>
						<%              		
						if(btnMap.get(menuCode+"-24")!=null)                       		
						{                        	
						%>
						<td class="ztalign" >
						<center>
						<div class="setControl"></div>
							<%
							 if (!curSysuser.getUserName().equals(service.getUserName()) && !curSysuser.getUserName().equals(service.getOwnerUserName()))
				             {
				                    if(service.getRunState()==1)
									{
										//out.print("已启用");
										out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_yqy",request));
									}
				                    else if(service.getRunState()==0)
									{
										//out.print("已停用");
										out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_yty",request));
									}
							 }
							 else
							 {
							 %>
								<%if(service.getRunState()==1){
								%>

									<select  name="runState<%=service.getSerId()%>" id="runState<%=service.getSerId()%>" idx="<%=service.getSerId()%>" serName="<%=service.getSerName() %>" class="input_bd" onchange="javascript:changestate('<%=service.getSerId()%>','<%=service.getSerName()%>>')">
							          <option value="1" selected="selected"><emp:message key="znyq_ywgl_xhywgl_yqy" defVal="已启用" fileName="znyq"></emp:message></option>
							          <option value="0" ><emp:message key="znyq_ywgl_xhywgl_ty" defVal="停用" fileName="znyq"></emp:message></option>
							        </select>
								<%
								}else{
								%>
									<select  name="runState<%=service.getSerId() %>" id="runState<%=service.getSerId()%>" class="input_bd" idx="<%=service.getSerId()%>" serName="<%=service.getSerName() %>" onchange="javascript:changestate('<%=service.getSerId()%>','<%=service.getSerName()%>>')">
							          <option value="0" selected="selected"><emp:message key="znyq_ywgl_xhywgl_yty" defVal="已停用" fileName="znyq"></emp:message></option>
							          <option value="1" ><emp:message key="znyq_ywgl_xhywgl_qy" defVal="启用" fileName="znyq"></emp:message></option>
							        </select>
								<%	
								}
								%>
							<%	
							}
							%>
						</center>	
						</td>
						<% 
						}
						else
						{
						%>
						<td class="ztalign" >
							<%
			                    if(service.getRunState()==1)
								{
									//out.print("已启用");
									out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_yqy",request));
								}
			                    else if(service.getRunState()==0)
								{
									//out.print("已禁用");
									out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_xhywgl_yjy",request));
								}
							%>
						</td>
						<%	} %>
						
						<%   
							
							if(btnMap.get(menuCode+"-29")!=null)                       		
							{                        	
						%>
						<td>
							<a onclick="toProcess('<%=service.getSerIdCipher() %>')"><emp:message key="znyq_ywgl_xhywgl_bzgl" defVal="步骤管理" fileName="znyq"></emp:message></a>
						</td>
						
						<%   
							}
							if(btnMap.get(menuCode+"-30")!=null)                       		
							{                        	
						%>
						<td>
							<a onclick="showSerTimeInfo('<%=service.getSerIdCipher() %>')"><emp:message key="znyq_ywgl_xhywgl_zxsj" defVal="执行时间" fileName="znyq"></emp:message></a>
						</td>
						
						<%   
							}
							if(btnMap.get(menuCode+"-24")!=null)                       		
							{                        	
						%>
						<td>
							<a onclick="showSerInfo('<%=service.getSerIdCipher() %>')"><emp:message key="znyq_ywgl_xhywgl_ywxx" defVal="业务信息" fileName="znyq"></emp:message></a>
						</td>
						<%
							}
							if(btnMap.get(menuCode+"-2")!=null)                       		
							{                        	
						%>
						<td>
						<label id="del<%=service.getSerId() %>">
						<%
							 if (!curSysuser.getUserName().equals(service.getUserName()) && !curSysuser.getUserName().equals(service.getOwnerUserName()))
				             {
								 //out.print("删除");
								 out.print(MessageUtils.extractMessage("znyq","znyq_ywgl_common_text_8",request));
							 }
							 else
							 {
						 %>
							<a href="javascript:deleteSer('<%=service.getSerIdCipher() %>','<%=service.getSerName() %>',<%=service.getRunState() %>)"><emp:message key="znyq_ywgl_common_text_8" defVal="删除" fileName="znyq"></emp:message></a>
						<%} %>
						</label>
						</td>
						<%
							}
						%>
					</tr>
					<%
						}
						}
						else
						{
					%>
					<tr><td colspan="11" align="center"><emp:message key="znyq_ywgl_common_text_1" defVal="无记录" fileName="znyq"></emp:message></td></tr>
					<%} %>
					<tfoot>
					<tr >
						<td colspan="11">
						<div id="pageInfo"></div>
						</td>
					</tr>
					</tfoot>
				</table>
				</form>
				<div class="clear"></div>
				<%
						}
				%>
				
				<div id="serInfoDiv" name="serInfoDiv" title="<emp:message key="znyq_ywgl_xhywgl_ywxx" defVal="业务信息" fileName="znyq"></emp:message>" >
					<iframe id="serInfoflowFrame" name="serInfoflowFrame"  marginwidth="0" scrolling="no" frameborder="no"></iframe>
					<table>
						<tr><td>
						<input type="button" id="serEditok" 	 value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" class="btnClass5 mr23" onclick="javascript:doOk()" />
						<input type="button" id="serEditCancel"  value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>" class="btnClass6" onclick="javascript:closeSerInfodiv();" />
						<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
						<br/>
						</td>
						</tr>
					</table>
				</div>
				
				<div id="exctimeInfoDiv" title="<emp:message key="znyq_ywgl_xhywgl_zxsj" defVal="执行时间" fileName="znyq"></emp:message>">
					<iframe id="exctimeInfoFrame" name="exctimeInfoFrame"  marginwidth="0" scrolling="no" frameborder="no"></iframe>
					<table>
						<tr><td>
						<input type="button" id="serTimeEditok"  value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" class="btnClass5 mr23" onclick="javascript:doTimeOk()" />
						<input type="button"  id="serTimeEditCancel" value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>" class="btnClass6" onclick="javascript:closeTimeInfoDiv();" />
						<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
						<br/>
						</td>
						</tr>
					</table>
				</div>
			</div>
			<%--end rContent--%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
		
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script language="javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/eng_mtService.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript">
		

		$(document).ready(function() {
			
			getLoginInfo("#hiddenValueDiv");
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$(this).addClass("collapse");
			}, function() {
				$("#condition").show();
				$(this).removeClass("collapse");
			});
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});
			$("#content tbody tr").hover(function() {
				$(this).addClass("hoverColor");
				}, function() {
				$(this).removeClass("hoverColor");
			});

			$('#u_o_c_explain').find('> p').next().hide();
			$('#u_o_c_explain').click(function(){$(this).find('> p').next().toggle();});
			
			$("#serInfoDiv").dialog({
				autoOpen: false,
				height:300,
				width: 600,
				resizable:false,
				modal: true,
				open:function(){
					//$(".ui-dialog-titlebar-close").hide();
					//alert("open");
				},
				close:function(){
					$("#serInfoflowFrame").attr("src","");
				}
			});
			
			$("#exctimeInfoDiv").dialog({
				autoOpen: false,
				height:310,
				width: 500,
				resizable:false,
				modal: true,
				open:function(){
					//$(".ui-dialog-titlebar-close").hide();
					//alert("open");
				},
				close:function(){
					$("#exctimeInfoFrame").attr("src","");
				}
			});
			show();
			$('#content select').isSearchSelectNew({'width':'60','isInput':false,'zindex':0,'isHideBorder':true,'isHide':true},function(data){
			  var idx=$(data.box.self).attr("idx");
			  var serName=$(data.box.self).attr("serName");
			  changestate(idx,serName);
		    },function(data){
		    	 var self=$(data.box.self);
	  				self.siblings('.setControl').html(self.find("option:selected").text()).css({'width':'60px'});
		    });
		    $('#serState').isSearchSelect({'width':'183','isInput':false,'zindex':0});
		});

		function showSerInfo(serId)
		{
			//修改
            $("#serInfoflowFrame").attr("src","<%=path %>/eng_mtService.htm?method=toEdit&serId="+serId);
            
			$("#serInfoDiv").dialog("open");
			$("#serInfoDiv").height("310px");
		}
		
		function showSerTimeInfo(serId)
		{
			//修改
            $("#exctimeInfoFrame").attr("src","<%=path %>/eng_mtService.htm?method=toAddTrigger&serId="+serId);
            
			$("#exctimeInfoDiv").dialog("open");
		}
		
		
		function show()
		{
			<% 
				Object result=session.getAttribute("addserResult");
				if(result!=null && result.toString().equals("1"))
				{
			%>
					//alert("新建业务成功！");
					var chenggong=getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_xjyecg");
					alert(chenggong);
			<%
				}
				else if(result!=null && result.toString().equals("0"))
				{
			%>
					//alert("操作失败！");	
					var shibai=getJsLocaleMessage("znyq","znyq_ywgl_xhywgl_czsb");
					alert(shibai);
			<%
				}
				session.removeAttribute("addserResult");
				//request.removeAttribute("addserResult");
				if (result != null)
			    {
			%>
			    	//location.href="ser_mtService.htm?method=find&lguserid="+$('#lguserid').val();
			<%
				}
			%>
		}
		
		function toProcess(serId)
		{
			window.location.href="eng_mtProcess.htm?serId="+serId+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&lgusername="+$('#lgusername').val();
		}
		
</script>
		
	</body>
</html>
