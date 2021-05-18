<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
String path = request.getContextPath();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("busCfg");
menuCode = menuCode==null?"0-0-0":menuCode;
String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
Boolean result = (Boolean)request.getAttribute("result");
Object list=request.getAttribute("busList");
@SuppressWarnings("unchecked")
List<LfBusManager>	busList=(List<LfBusManager>)request.getAttribute("busList");
@SuppressWarnings("unchecked")
List<DynaBean>	cityList=(List<DynaBean>)request.getAttribute("citys");
%>
<!doctype html>
<html>
  <head>
    <title></title>
    <%@include file="/common/common.jsp" %>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	<link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link href="<%=iPath%>/css/mon_bus.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
  	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
  </head>
  
  <body>
	<input type="hidden" id="pathUrl" value="<%=path %>" />

		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("ptjk","ptjk_jkgl_cx_8",request)) %>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent" style="padding-left: 40px;">
			
			<form action="mon_busCfg.htm?method=addBusType" method="post" id="addForm" name="addForm" autocomplete="off">
				<%-- 后台需要的隐藏信息 --%>
				<input type="hidden" id="name" name="name" value="" />
				<input type="hidden" id="code" name="code" value="" />
				<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="" />
				<input type="hidden" id="lguserid" name="lguserid" value="" />
				<input type="hidden" id="optype" value="add" />
				<div class="div_bg" style="height: 30px;line-height: 30px;"><strong>&nbsp;<emp:message key="ptjk_jkgl_zj_jbxx" defVal="基本信息" fileName="ptjk"/></strong></div>
				<table>
					<tr>
						<td><emp:message key="ptjk_jkxq_yw_ywmc_mh" defVal="业务名称：" fileName="ptjk"/></td>
						<td>
							<select class="select input_bd" id="busName"  style="width:204px;" name="busName" onchange="name_change()">
							<option value="" selected="selected"><emp:message key="ptjk_jkgl_yw_qxz" defVal="请选择" fileName="ptjk"/></option>
								<% if(busList!=null&&busList.size()>0){
								for(int i=0;i<busList.size();i++){
									LfBusManager vo=busList.get(i);
								%>
									<option value="<%=vo.getBusCode() %>"><%=vo.getBusName() %></option>
									<%}
									}%>
							</select>
						</td>
					</tr>
					<tr id="gate_id_tr">
						<td><emp:message key="ptjk_jkxq_yw_ywbm_mh" defVal="业务编码：" fileName="ptjk"/></td>
						<td>
							<select class="select input_bd" style="width:204px;" id="busCode" name="busCode" onchange="code_change()">
								<option value="" selected="selected"><emp:message key="ptjk_jkgl_yw_qxz" defVal="请选择" fileName="ptjk"/></option>
								<% if(busList!=null&&busList.size()>0){
								for(int i=0;i<busList.size();i++){
									LfBusManager vo=busList.get(i);
								%>
									<option value="<%=vo.getBusCode() %>"><%=vo.getBusCode() %></option>
									<%}
									}%>
							</select>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkxq_yw_qy_mh" defVal="区域：" fileName="ptjk"/></td>
						<td>							
							<label>
							<input id="areaName"   name="areaName" type="text"  onclick="javascript:openAreaChoose();"
							readonly value='<emp:message key='ptjk_jkxq_yw_qbqy' defVal='全部区域' fileName='ptjk'/>'  allAreaName="" 
							 style="width:200px;height:20px;cursor: pointer;"/>
							<div id="allAreaName" style="display: none;z-index: 4000px;" class="allAreaName"></div>
						</label>
						<input type="hidden" id="allAreas" name="allAreas" value=""/>
							
							<font class="til"><emp:message key="ptjk_jkgl_yw_7" defVal="默认全区域" fileName="ptjk"/></font> 
						</td>
					</tr>

					<tr>
						<td><emp:message key="ptjk_common_gjsjh_mh" defVal="告警手机号：" fileName="ptjk"/></td>
						<td>
							<textarea name="monphone" id="monphone" class="input_bd" style="width:200px;vertical-align: middle;"></textarea>
							<font class="til"><emp:message key="ptjk_jkgl_yw_8" defVal="手机号以逗号“,”分隔，最多十个号码" fileName="ptjk"/></font> 
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_common_gjyx_mh" defVal="告警邮箱：" fileName="ptjk"/></td>
						<td><textarea name="monemail" id="monemail" class="input_bd"
								style="width:200px;vertical-align: middle;"></textarea> <font
							class="til"><emp:message key="ptjk_common_znszygyx" defVal="只能设置一个邮箱" fileName="ptjk"/></font></td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_jkgl_yw_9" defVal="监控开始时间：" fileName="ptjk"/></td>
						<td>
								<input type="text" style="cursor: pointer; width: 200px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()" value=""  id="begintime" name="begintime">
						</td>
					</tr>
						<tr>
						<td><emp:message key="ptjk_jkgl_yw_10" defVal="监控截止时间：" fileName="ptjk"/></td>
						<td>
							<input type="text" style="cursor: pointer; width: 200px; background-color: white;"
									readonly="readonly" class="Wdate"  onclick="rtime()" value=""  id="endtime" name="endtime" >
						</td>
					</tr>
					
				</table> 
				<div class="div_bg" style="height: 28px;line-height: 28px;margin-top: 20px;"><strong>&nbsp;<emp:message key="ptjk_jkgl_yw_11" defVal="告警时间段阀值设置" fileName="ptjk"/></strong></div>
				<div style="height: 80px;" id="busTypeTable">
				<div  style="height: 30px;line-height: 30px;width: 900px;text-align: right;">
				<input type="button" name="addcol" id="addcol" value="<emp:message key='ptjk_jkgl_spzh_tjh' defVal='添加行' fileName='ptjk'/>" onclick="addLine()" class="btnClass3"/></div>
				<table id="content" style="width: 900px;">
						<thead style="line-height: 30px;">
					  <tr>
					  		<th style="width: 149px;">
								<emp:message key="ptjk_jkgl_yw_12" defVal="行数" fileName="ptjk"/>
							</th>
							<th style="width: 154px;">
								<emp:message key="ptjk_jkxq_yw_sjd" defVal="时间段" fileName="ptjk"/>
							</th>
							<th style="width: 154px;">
								<emp:message key="ptjk_jkgl_yw_13" defVal="MT已发告警值(条)" fileName="ptjk"/>
							</th>
							<th style="width: 154px;">
								<emp:message key="ptjk_jkgl_yw_14" defVal="偏离率(高于%)" fileName="ptjk"/>
							</th>
							<th style="width: 154px;">
								<emp:message key="ptjk_jkgl_yw_15" defVal="偏离率(低于%)" fileName="ptjk"/>
							</th>														
							<th style="width: 152px;" id="opreate">
							           <emp:message key="ptjk_common_cz" defVal="操作" fileName="ptjk"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</th>
						</tr>
						</thead>
						</table>
						<div  style="overflow-y: auto;overflow-x:hidden;height: 220px;width: 900px;">
						<table id="content">
						<tbody  id="tbody">
						<tr id="tr1" class="trClass" style="height: 24px;">
						<td style="border-left:#a0b2ca 1px solid; width: 150px;" >
						1
						</td>
						<td style="width: 154px;">
						<select id="selectfrom1" name="selectfrom1" style="width: 40px;" onchange="from_change(this)" >
						<option>0</option>
						<option>1</option>
						<option>2</option>
						<option>3</option>
						<option>4</option>
						<option>5</option>
						<option>6</option>
						<option>7</option>
						<option>8</option>
						<option>9</option>
						<option>10</option>
						<option>11</option>	
						<option>12</option>
						<option>13</option>
						<option>14</option>
						<option>15</option>
						<option>16</option>
						<option>17</option>
						<option>18</option>
						<option>19</option>
						<option>20</option>
						<option>21</option>
						<option>22</option>										
						</select>	<emp:message key="ptjk_common_z" defVal="至" fileName="ptjk"/>
						<select id="selectto1" name="selectto1" style="width: 40px;" onchange="to_change(this)">
						<option>1</option>
						<option>2</option>
						<option>3</option>
						<option>4</option>
						<option>5</option>
						<option>6</option>
						<option>7</option>
						<option>8</option>
						<option>9</option>
						<option>10</option>
						<option>11</option>	
						<option>12</option>
						<option>13</option>
						<option>14</option>
						<option>15</option>
						<option>16</option>
						<option>17</option>
						<option>18</option>
						<option>19</option>
						<option>20</option>
						<option>21</option>
						<option>22</option>
						<option>23</option>											
						</select>
						</td>
						<td style="width: 152px;">
						<input type="text" id="alarmCount1" name="alarmCount1" style="width:80px;height:15px;" maxlength="9" onkeyup="numberControl($(this))" value="" onblur="numberControl($(this))" onchange="parseNumber($(this))">
						</td>
						<td style="width: 154px;">
						<input type="text" id="biger1" name="biger1" style="width:30px;height:15px;"  maxlength="3" onkeyup="numberControl($(this))" value="" onblur="numberControl($(this))" onchange="checkNumber($(this))">&nbsp;%
						</td>
						<td style="width: 154px;">
						<input type="text" id="smaller1" name="smaller1" style="width:30px;height:15px;"  maxlength="3" onkeyup="numberControl($(this))" value="" onblur="numberControl($(this))"onchange="checkNumber($(this))" >&nbsp;%
						</td>
						<td style="border-right:#a0b2ca 1px solid;width:155px" id="del">
						<a   onclick="javascript:del(1)"><emp:message key="ptjk_common_sc" defVal="删除" fileName="ptjk"/></a>
						</td>
						</tr>
						
						</tbody>
						
						
				</table>
				</div>
				</div>
				<table style="margin-left:300px;margin-top: 220px;">
					<tr>
						<td>
							<input type="button" name="sub" id="sub" value="<emp:message key='ptjk_common_qd' defVal='确定' fileName='ptjk'/>" onclick="finishIntercept()" class="btnClass5 mr23"/>
							<input type="button" name="button" id="button" value="<emp:message key='ptjk_common_qx' defVal='取消' fileName='ptjk'/>" onclick="javascript:doreturn()" 
							class="btnClass6"/><br/>
						</td>
					</tr>
				</table>
			</form>
			</div>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    
	<%-- 弹出区域--%>
	<div id="areaDiv" style="display:none;">

		<div style="width:300px;height:200px;padding:10px 0 10px 10px;overflow: auto">
				<%
					if (cityList != null) {
				%>
			<div id="areaDiv" style="width:140px;">
			<%
				for (int i = 0; i < cityList.size(); i++) {
					DynaBean city = cityList.get(i);
			%>
				<input type="checkbox" name="roleId" id="cityId:<%=city.get("provincecode") %>" value="<%=city.get("provincecode") %>" />
				<label for="cityId:<%=city.get("provincecode") %>"><%=city.get("province").toString().replace("<","&lt;").replace(">","&gt;")%></label><br/>
				<% } %>
			 </div>
			 <% } %>
		</div>
		<center>
		<div style="" id="" class="roleBut">
			<input type="button" class="btnClass5" value="<emp:message key='ptjk_common_qd' defVal='确定' fileName='ptjk'/>" onclick="doArea()"/>
			</div>
		</center>
		</div>
    
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/busType.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript">
		var pathUrl = '<%=path%>';
		var repeatArr=new Array();
		var arr =new Array();
		//初始化 默认第一个值，数组为0的没有使用
		arr[1]="0:1";
		$(document).ready(function() {		
		var lgcorpcode = GlobalVars.lgcorpcode;
		$('#lgcorpcode').val(lgcorpcode);
		var lguserid=GlobalVars.lguserid;
		$('#lguserid').val(lguserid);
		$("#monphone").css({
			'height':'24px',
			'line-height': '16px',
			'vertical-align': 'middle',
			'margin-bottom': '2px',
			'margin-top': '2px',
			'resize': 'none',
			'overflow':'hidden'	
		});
		$("#monphone").bind('keydown keyup focus',function(){
			//处理内容
			subText($(this));
			//处理高度
			$(this).height('24px');
			if($(this)[0].scrollHeight>24){
				$(this).height($(this)[0].scrollHeight);
			}		
		});	
		//失焦事件只处理内容 不处理高度
		$("#monphone").bind('blur',function(){
			subText($(this));
		});	
		$("#monphone").keydown();

		$('#areaDiv').dialog( {
			autoOpen : false,
			height : 300,
			title : getJsLocaleMessage("ptjk","ptjk_jkgl_yw_1"),
			width : 320,
			close : function() {
				fillAreaName();
			},
			modal : true
		});
		});
	</script>
	</body>
</html>
