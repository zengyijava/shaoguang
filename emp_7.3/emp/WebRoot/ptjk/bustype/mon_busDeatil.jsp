<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.entity.monitor.LfMonBusinfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
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

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("busDeatil");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<LfMonBusinfo> busDetailList = (List<LfMonBusinfo>) request.getAttribute("infoList");
//获得初始的业务编号与名称
@ SuppressWarnings("unchecked")
List<LfBusManager> busList = (List<LfBusManager>) request.getAttribute("busList");
//查询条件
@ SuppressWarnings("unchecked")
LinkedHashMap<String, String> conditionMap=(LinkedHashMap<String, String>)request.getAttribute("conditionMap");

//用于初始化 查询条件的区域
@ SuppressWarnings("unchecked")
List<DynaBean>	cityList=(List<DynaBean>)request.getAttribute("citys");

//处理查询结果的区域名称
@ SuppressWarnings("unchecked")
Map<Long,String>	nameMap=(Map<Long,String>)request.getAttribute("nameMap");
@ SuppressWarnings("unchecked")
Map<Long,String>	showNameMap=(Map<Long,String>)request.getAttribute("showNameMap");

//用于保存用户选择区域
String areaName=(String)request.getAttribute("areaName");
@ SuppressWarnings("unchecked")
String[] selectNames=(String[])request.getAttribute("selectNames");
String areaCodes=(String)request.getAttribute("areaCodes");
%>

<!doctype html>
<html>
  <head>
    
    <title><emp:message key="ptjk_jkgl_yw_0" defVal="业务监控详情" fileName="ptjk"/></title>
    <%@include file="/common/common.jsp" %>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	<link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
  	<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
  </head>
  
  <body>
    <div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="mon_busDeatil.htm" method="post" id="pageForm" autocomplete="off">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv">
				</div>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td><emp:message key="ptjk_jkxq_yw_ywmc_mh" defVal="业务名称：" fileName="ptjk"/></td>
							<td>
							<input type="text" style="width:203px;" id="busName" name="busName" value="<%=conditionMap.get("busname")==null?"":conditionMap.get("busname") %>">
							</td>
							<td><emp:message key="ptjk_jkxq_yw_ywbm_mh" defVal="业务编码：" fileName="ptjk"/></td>
							<td>
							<select class="select input_bd" style="width:203px;" id="busCode" name="busCode">
							<option value="" ><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
								<%if(busList!=null&&busList.size()>0){
								for(int i=0;i<busList.size();i++){
									LfBusManager bus=busList.get(i);
									if(conditionMap!=null&&conditionMap.get("buscode")!=null&&bus.getBusCode().equals(conditionMap.get("buscode"))){
								%>
									<option value="<%=bus.getBusCode()%>" selected="selected" ><%=bus.getBusCode()%></option>
									<%}else{%>
									<option value="<%=bus.getBusCode()%>" ><%=bus.getBusCode()%></option>
									<%}
								}
							}%>	
							</select>
							</td>
							<td>
								<emp:message key="ptjk_common_gjjb_mh" defVal="告警级别：" fileName="ptjk"/>
							</td>
							<td>	
							<% 
							String level="";
							if(conditionMap!=null&&conditionMap.get("level")!=null){
								level=conditionMap.get("level");
							}
							%>
							<select class="select input_bd" style="width:203px;" id="level" name="level">
							<option value="" ><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
							<option value="0" <% if("0".equals(level)){ %> selected="selected" <%} %>><emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></option>
							<option value="1" <% if("1".equals(level)){ %> selected="selected" <%} %>><emp:message key="ptjk_common_gj" defVal="告警" fileName="ptjk"/></option>
							</select>
							</td>
							<td class="tdSer">
							     <center><a id="search"></a></center>
						    </td>		
						</tr>
						
						<tr>
							<td><emp:message key="ptjk_jkxq_yw_qy_mh" defVal="区域：" fileName="ptjk"/></td>
							<td>
							<label>
							<input id="areaName"   name="areaName" type="text"  onclick="javascript:openAreaChoose();"
							readonly value='<%=areaName%>'  allAreaName="" 
							 style="width:200px;height:20px;cursor: pointer;"/>
							<div id="allAreaName" style="display: none;z-index: 4000px;" class="allAreaName"></div>
						</label>
						<input type="hidden" id="allAreas" name="allAreas" value="<%=areaCodes%>"/>
							</td>
							<% 
							String sendtime="";
							if(conditionMap!=null&&conditionMap.get("sendtime")!=null){
								sendtime=conditionMap.get("sendtime");
							}
							String recvtime="";
							if(conditionMap!=null&&conditionMap.get("recvtime")!=null){
								recvtime=conditionMap.get("recvtime");
							}
							%>
							<td><emp:message key="ptjk_common_sj_mh" defVal="时间：" fileName="ptjk"/></td>
							<td>
								<input type="text"
											style="cursor: pointer; width: 200px; background-color: white;"
											readonly="readonly" class="Wdate" onclick="stime()" 
											value="<%=sendtime %>"  id="sendtime" name="sendtime">
							</td>
							<td>
								<emp:message key="ptjk_common_z_mh" defVal="至：" fileName="ptjk"/>
							</td>
							<td >	
							<input type="text"
							style="cursor: pointer; width: 200px; background-color: white;"
							readonly="readonly" class="Wdate"  onclick="rtime()"
							value="<%=recvtime %>" 
							 id="recvtime" name="recvtime" >
							</td>
							<td >
						    </td>		
						</tr>
						
						
					</tbody>
				</table>
			</div>
			<table id="content">
				<thead>
					<tr>
						<th>
							<emp:message key="ptjk_jkxq_yw_ywmc" defVal="业务名称" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_yw_ywbm" defVal="业务编码" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_yw_jkqy" defVal="监控区域" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_yw_jksj" defVal="监控时间" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_yw_sjd" defVal="时间段" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_yw_1" defVal="告警偏离率" fileName="ptjk"/>
						</th>

						<th>
							<emp:message key="ptjk_jkxq_yw_2" defVal="MT告警值(条)" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_tdzh_21" defVal="MT已发(条)" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_gjjb" defVal="告警级别" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_yw_gjsm" defVal="告警说明" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_yw_gjr" defVal="告警人" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_gxsj" defVal="更新时间" fileName="ptjk"/>
						</th>
					</tr>
				</thead>
				<tbody>
				<%
				if(busDetailList!=null&&busDetailList.size()>0)
				{				
					for(int i=0;i<busDetailList.size();i++){
					LfMonBusinfo base=busDetailList.get(i);
				%>
						<tr>
						<td><%=base.getBusname() %></td>
						<td><%=base.getBuscode() %></td>
						<td><a onclick=javascript:showmsg(this,1)>
						  <label style="display:none"><xmp><%="-1".equals(base.getAreacode())?MessageUtils.extractMessage("ptjk","ptjk_jkxq_yw_qbqy",request):nameMap.get(base.getId()) %>
						  </xmp></label>
						  <xmp><%="-1".equals(base.getAreacode())?MessageUtils.extractMessage("ptjk","ptjk_jkxq_yw_qbqy",request):showNameMap.get(base.getId()) %></xmp>
						  </a> 
						</td>
						<td style="width: 150px;"><%=df.format(base.getBegintime())%>~<%=df.format(base.getEndtime()) %></td>
						<td><%=base.getBeginhour()%>:00~<%=base.getEndhour()%>:00</td>
						<td><%=base.getMondeviat() %></td>
						<td><%=base.getMthavesnd()%></td>
						<td><%=base.getMtsendcount() %> </td>						

						<% if("0".equals(base.getEvttype()+"")){
							out.print("<td class='natural'>"+MessageUtils.extractMessage("ptjk","ptjk_common_zc",request)+"</td>");
						}else if("1".equals(base.getEvttype()+"")){
							out.print("<td class='warn'>"+MessageUtils.extractMessage("ptjk","ptjk_common_jg",request)+"</td>");
						}else{
							out.print("-");
						}
						
						%>
						<td>	
						<%
						String desc=base.getMondes();
						if(desc==null){
							out.print("-");
						}else if(desc!=null&&"".equals(desc.trim())){
							out.print("-");
						}else if(desc!=null){
							out.print(desc);
						}	
						%> </td>
					<td>	
					<% 
					String phone_info="";
					if(base.getMonphone()!=null&&base.getMonphone().length()>12){
						phone_info = base.getMonphone().substring(0,12)+"...";
					}else{
						phone_info=base.getMonphone();
					}
					if("".equals(phone_info.trim())){
						
						out.print("-");
					}else{
					%>
					<a onclick=javascript:showmsg(this,2)>
						  <label style="display:none"><xmp><%=base.getMonphone() %>
						  </xmp></label>
						  <xmp><%=phone_info%></xmp>
						  </a>
					<% }%>
					</td>
					<td><%=format.format(base.getCreatetime())%></td>
					</tr>
					<%} 
					}else{%>
				<tr><td align="center" colspan="13"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr>
				<%} %>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="13">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
			</table>
			
	<%-- 弹出区域--%>
	<div id="areaDiv" style="display:none;">
	<div style="width:300px;height:200px;padding:10px 0 10px 10px;overflow: auto">
			<%
				if (cityList != null) {
			%>
		<div id="areaDiv" style="width:140px;">
		<%
			String tempID="";
			for (int i = 0; i < cityList.size(); i++) {
				DynaBean city = cityList.get(i);
				if(selectNames!=null&&selectNames.length>0){
					for(int k=0;k<selectNames.length;k++){
						if(selectNames[k].equals(city.get("provincecode").toString())){
							tempID=tempID+selectNames[k]+",";
							%>
							<input type="checkbox" name="roleId" id="cityId:<%=city.get("provincecode") %>" value="<%=city.get("provincecode") %>"  checked/>
							<label for="cityId:<%=city.get("provincecode") %>"><%=city.get("province").toString().replace("<","&lt;").replace(">","&gt;")%></label><br/>
						<%}
					}
				}if(tempID.indexOf(city.get("provincecode").toString()+",")==-1){%>
					<input type="checkbox" name="roleId" id="cityId:<%=city.get("provincecode") %>" value="<%=city.get("provincecode") %>" />
					<label for="cityId:<%=city.get("provincecode") %>"><%=city.get("province").toString().replace("<","&lt;").replace(">","&gt;")%></label><br/>
				<%}
		%>

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
			
		</form>
		</div>

		<div id="arealist" title="<emp:message key='ptjk_jkxq_yw_gjr' defVal='告警人' fileName='ptjk'/>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
		<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
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
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/mon_busCfg.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#loginInfo");
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
			
			$('#areaDiv').dialog( {
				autoOpen : false,
				height : 300,
				title : getJsLocaleMessage("ptjk","ptjk_jkxq_yw_1"),
				width : 320,
				close : function() {
					fillAreaName();
				},
				modal : true
			});
			
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){
				submitForm();
			});
		});
	</script>
  </body>
</html>
