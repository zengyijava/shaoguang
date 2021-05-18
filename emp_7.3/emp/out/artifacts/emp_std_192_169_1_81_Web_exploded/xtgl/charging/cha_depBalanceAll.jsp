<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.charging.vo.LfDepBalanceVo"%>
<%@page import="com.montnets.emp.charging.vo.LfDepBalanceDefVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,
			request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("balanceMgr");
	menuCode = menuCode==null?"0-0-0":menuCode;
	boolean addcode = false;
	if(btnMap.get(menuCode+"-1")!=null)
	{
		addcode = true;
	}
	boolean delcode = false;
	if(btnMap.get(menuCode+"-2")!=null)
	{
		delcode = true;
	}
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	
	List<LfDepBalanceDefVo> lfDepslist = (List<LfDepBalanceDefVo>)request.getAttribute("lfDeplist");
	LfDepBalanceVo depBalance = (LfDepBalanceVo)request.getAttribute("balance");
	Integer sonDepSum = 0;
	if(lfDepslist != null && lfDepslist.size()>0)
	{
		sonDepSum = Integer.valueOf(lfDepslist.size());
	}
%>
<HTML>
	<HEAD>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/cha_depBalanceAll.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/cha_depBalanceAll.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</HEAD>
	<BODY id="cha_depBalanceAll">
		<div id="rContent" class="rContent">
			<%-- 批量充值 --%>
			<div id="addBalanceAll"  align="center">
			<center>
			    <table cellpadding="0" cellspacing="0" class="supDepId_table">
			    	<input type="hidden" id= "supDepId" name = "supDepId"  value="<%=depBalance.getDepId()%>"/>
			        <tr align="left">
			           <td >部门名称 :</td>
			           <td class="bmmc_td" colspan="3"><%=depBalance.getDepName() %><font color="red">（注：有<%=sonDepSum%>个子机构）</font></td>
			        </tr>
			        <tr><td class="jgye_up_tr_td"></td></tr>
			        <tr class="jgye_tr" align="left">
			           <td class="jgye_tr_td1">机构短信余额:</td>
			           <td class="jgye_tr_td2"><%=depBalance.getSmsBalance() %>&nbsp;条</td>
			           <td class="jgye_tr_td3">机构彩信余额:</td>
			           <td class="jgye_tr_td4"><%=depBalance.getMmsBalance() %>&nbsp;条</td>
			        </tr>
			        <tr><td class="jgye_down_tr_td"></td></tr>
			        <tr class="plc_tr" align="left">
			           <td>短信批量充 :</td>
			           <td><input type="text" id="addsmsCountAll" name="addsmsCountAll" class="addsmsCountAll" maxlength="9" onkeyup="checkSmsCount()" onblur="checkSmsCount()"/>&nbsp;条</td>
			           <td >彩信批量充:</td>
			           <td><input type="text" id="addmmsCountAll" name="addmmsCountAll" class="addmmsCountAll" maxlength="9" onkeyup="checkMmsCount()" onblur="checkMmsCount()"/>&nbsp;条</td>
			        </tr>
			        <tr><td class="plc_down_tr_td"></td></tr>
			        <tr><td colspan="4">
			        		<table class="jgmc_table">
			        		<thead class="jgmc_thead">
			        			<tr class="jgmc_tr">
				        			<th>机构名称</th>
				        			<th class="jgmc_dxcz">短信充值</th>
				        			<th class="jgmc_cxcz">彩信充值</th>
			        			</tr>
			        		</thead>
			        		</table>
			        	<div id="resultSet" class="mt10" style="margin-top:0px;height: <%=sonDepSum > 7?"202px":"auto"%>">
			        	<table id = "content" class="content">
			        		<%
			        		if(lfDepslist != null && lfDepslist.size() > 0)
			        		{
			        			for(LfDepBalanceDefVo dep:lfDepslist)
			        			{
			        		%>
				        		<tr align="center">
				        			<td calss="textalign"><input type="hidden" id="balanceDepId" name="balanceDepId" value="<%=dep.getDepId()%>"/>
				        				<%
					        				if(dep.getDepName().length() >12) {
					        					out.print((dep.getDepName().substring(0,12))+"...");
					        				}else{
					        					out.print(dep.getDepName());
					        				}
				        				%>
				        			</td>
				        			<td calss="textalign" class="smsCount_td"><input type="text" id="smsCount" name="smsCount" class="smsCount" maxlength="9" value="<%=dep.getSmsCount()!= null?dep.getSmsCount():0 %>" onkeyup="sumSmsCount()" onblur="sumSmsCount()"/>&nbsp;条</td>
				        			<td calss="textalign" class="mmsCount_td"><input type="text" id="mmsCount" name="mmsCount" class="mmsCount" maxlength="9" value="<%=dep.getMmsCount()!= null?dep.getMmsCount():0 %>" onkeyup="mumSmsCount()" onblur="mumSmsCount()"/>&nbsp;条</td>
				        		</tr>
			        		<%
			        			}
			        		}else{
			        		%>
			        		<tr><td colspan="3" align="center">此机构无子机构!</td> </tr>
			        		<%} %>
			        	</table>
			        	</div>
			       	</td></tr>
			       	<tr>
			       	<td colspan="4">
			       		<table class="zj_table">
			       			<tr align="center" class="zj_tr">
					       		<td class="zj_td">总计</td>
			        			<td class="smsSum"> <div id="smsSum"></div>
			        			</td>
			        			<td class="mmsSum"">
			        			<div id="mmsSum"></div>
			        			</td>
			       			</tr>
			       		</table>
			       	</td>
			       	</tr>
			       	<tr><td class="isDefault_up_tr_td"></td></tr>
			       	<tr><td colspan="4" align="left">
			       		<input type="checkbox" id="isDefault" name = "isDefault" value class="isDefault"/>设置为默认，下次充值初始显示默认值，否则显示为0
			     		</td>
			       	</tr>
			       	<tr><td class="chongz_up_tr_td"></td></tr>
		       		<tr>
			           <td colspan="6" class="chongz_td">
			           	 <center>
				           	<input type="button" class="btnClass5 mr23" value="充值" onclick="javascript:plchongZhi();" <%=lfDepslist != null && lfDepslist.size() > 0?"":"disabled"%>>
				           	<input type="button" class="btnClass6" value="取消" onclick="javascript:plquXiao();">
			           	 </center>
			           </td>
			       	</tr>
			   	</table>
			    </center>
			</div>
		</div>
		<script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/cha_depBalanceAll.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				sumSmsCount();
				mumSmsCount();
			});
		</script>
	</BODY>
</HTML>
