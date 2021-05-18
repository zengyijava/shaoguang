<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.monitor.LfMonBusbase"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@page import="com.montnets.emp.entity.monitor.LfMonBusdata"%>
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
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("busCfg");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<LfMonBusbase> baseList = (List<LfMonBusbase>) request.getAttribute("baseList");
//获得初始的业务编号与名称
@ SuppressWarnings("unchecked")
List<LfBusManager> busList = (List<LfBusManager>) request.getAttribute("busList");
@ SuppressWarnings("unchecked")
List<LfMonBusdata> dataList = (List<LfMonBusdata>) request.getAttribute("dataList");
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
    
    <title><emp:message key="ptjk_jkgl_yw_1" defVal="业务监控设置" fileName="ptjk"/></title>
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
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<style type="text/css">
		.c_selectBox{
			width: 208px!important;
		}
		.c_selectBox ul {
			width: 208px!important;
		}
		.c_selectBox ul li{
			width: 208px!important;
		}
	</style>
  </head>
  
  <body>
    <div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="mon_busCfg.htm" method="post" id="pageForm">
		<input type="hidden" id="code" name="code" value="" />
		<input type="hidden" id="lgcorpcode" name="lgcorpcode" value="" />
			<div class="buttons">
				<div id="toggleDiv">
				</div>
				<a id="add" onclick="javascript:toAdd()"><emp:message key="ptjk_common_xj" defVal="新建" fileName="ptjk"/></a>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td><emp:message key="ptjk_jkxq_yw_ywmc_mh" defVal="业务名称：" fileName="ptjk"/></td>
							<td>
							<select class="select input_bd" style="width:208px;" id="busName" name="busName" isInput="false">
							<option value="" ><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
							<%if(busList!=null&&busList.size()>0){
								for(int i=0;i<busList.size();i++){
									LfBusManager bus=busList.get(i);
									if(conditionMap!=null&&conditionMap.get("busname")!=null&&bus.getBusName().equals(conditionMap.get("busname"))){
									%>
										<option value="<%=bus.getBusName()%>" selected="selected" ><%=bus.getBusName()%></option>
									<%}else{
								%>
								
									<option value="<%=bus.getBusName()%>" ><%=bus.getBusName()%></option>
									<%}
									}
							}%>
							</select>
							</td>
							<td><emp:message key="ptjk_jkxq_yw_ywbm_mh" defVal="业务编码：" fileName="ptjk"/></td>
							<td>
							<select class="select input_bd" style="width:208px;" id="busCode" name="busCode" isInput="false">
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
								<emp:message key="ptjk_common_jkzt_mh" defVal="监控状态：" fileName="ptjk"/>
							</td>
							<td >	
							<select class="select input_bd" style="width:208px;" id="status" name="status" isInput="false">
							<option value="" ><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
							<% 
							String status="";
							if(conditionMap!=null&&conditionMap.get("status")!=null){
								status=conditionMap.get("status");
							}
							%>
							<option value="1" <% if("1".equals(status)){ %> selected="selected" <%} %>><emp:message key="ptjk_common_yqy" defVal="已启用" fileName="ptjk"/></option>
							<option value="0" <% if("0".equals(status)){ %> selected="selected" <%} %>><emp:message key="ptjk_common_ygb" defVal="已关闭" fileName="ptjk"/></option>
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
							
							
							<td><emp:message key="ptjk_jkxq_yw_jksj_mh" defVal="监控时间：" fileName="ptjk"/></td>
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
							<emp:message key="ptjk_common_jkzt" defVal="监控状态" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_yw_jksj" defVal="监控时间" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_yw_6" defVal="告警条件" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_gjsjh" defVal="告警手机号" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_yw_jkqy" defVal="监控区域" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_cz" defVal="操作" fileName="ptjk"/>
						</th>
					</tr>
				</thead>
				<tbody>
				<%
				if(baseList!=null&&baseList.size()>0)
				{
				for(int i=0;i<baseList.size();i++){
					LfMonBusbase base=baseList.get(i);
					%>
						<tr>
							<td><%=base.getBusname() %></td>
							<td><%=base.getBuscode() %></td>
						<td>
						<% if("1".equals(base.getMonstate()+"")){
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_yqy",request));
						}else{
							out.print(MessageUtils.extractMessage("ptjk","ptjk_common_ygb",request));
						}
						%>
						</td>
						<td style="width: 180px;"><%=df.format(base.getBegintime())%>&nbsp;<emp:message key="ptjk_common_z" defVal="至" fileName="ptjk"/>&nbsp;<%=df.format(base.getEndtime()) %></td>
						<%
						String info="";
						String divMsg="";
						if(dataList!=null&&dataList.size()>0)
						{
							String br="";
							int tmp=0;
							for(int k=0;k<dataList.size();k++){
								LfMonBusdata data=dataList.get(k);
								if(base.getId().equals(data.getBusbaseid())){
									info=info+MessageUtils.extractMessage("ptjk","ptjk_jkxq_yw_sjd_mh",request)+data.getBeginhour()+":00~"+data.getEndhour()+":00   "+MessageUtils.extractMessage("ptjk","ptjk_jkgl_yw_2",request)+data.getMthavesnd()+MessageUtils.extractMessage("ptjk","ptjk_jkgl_yw_3",request)+data.getDeviathigh()+MessageUtils.extractMessage("ptjk","ptjk_jkgl_yw_4",request)+data.getDeviatlow()+"%";
									if(tmp!=0){
										br="<br/>";
									}
									divMsg=divMsg+"<div>"+br+MessageUtils.extractMessage("ptjk","ptjk_jkxq_yw_sjd_mh",request)+data.getBeginhour()+":00~"+data.getEndhour()+":00  <br/>  "+MessageUtils.extractMessage("ptjk","ptjk_jkgl_yw_2",request)+data.getMthavesnd()+MessageUtils.extractMessage("ptjk","ptjk_jkgl_yw_5",request)+data.getDeviathigh()+MessageUtils.extractMessage("ptjk","ptjk_jkgl_yw_4",request)+data.getDeviatlow()+"% <br/> </div>";
									tmp=tmp+1;
								}
							}
						}
						String info_li="";
						if(info.length()>15){
							info_li = info.substring(0,15)+"...";
						}else{
							info_li=info;
						}
						
						%>
						<td style="width: 150px;">
						<a onclick=javascript:show_notice(this)>
						  <label style="display:none"><%=divMsg %>
						  </label>
						  <xmp><%=info_li %></xmp>
						  </a>
						
						</td>
					<% 
					String phone_info="";
					if(base.getMonphone()!=null&&base.getMonphone().length()>12){
						phone_info = base.getMonphone().substring(0,12)+"...";
					}else{
						phone_info=base.getMonphone();
					}
					%>
					<td style="width: 120px;">
					<% if("".equals(phone_info.trim())){
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
						<td>
						<a onclick=javascript:showmsg(this,1)>
						  <label style="display:none"><xmp><%="-1".equals(base.getAreacode())?MessageUtils.extractMessage("ptjk","ptjk_jkxq_yw_qbqy",request):nameMap.get(base.getId()) %>
						  </xmp></label>
						  <xmp><%="-1".equals(base.getAreacode())?MessageUtils.extractMessage("ptjk","ptjk_jkxq_yw_qbqy",request):showNameMap.get(base.getId()) %></xmp>
						  </a> 
						  </td>
						<td style="width: 240px;">
						<input type="button"   value="<emp:message key='ptjk_jkgl_yw_qy' defVal='启用' fileName='ptjk'/>" onclick="ope('<%=base.getId() %>')" 
						<% if(base.getMonstate()==1){%> disabled="disabled" <%} %> class="btnClass1"/>&nbsp;&nbsp;
						<input type="button"   value="<emp:message key='ptjk_jkgl_yw_gb' defVal='关闭' fileName='ptjk'/>" onclick="clo('<%=base.getId() %>')" <% if(base.getMonstate()==0){%>disabled="disabled" <%} %> class="btnClass1"/>&nbsp;&nbsp;
						<input type="button"   value="<emp:message key='ptjk_jkgl_yw_xg' defVal='修改' fileName='ptjk'/>" onclick="modif('<%=base.getId() %>')" class="btnClass1"/>&nbsp;&nbsp;
						<input type="button"   value="<emp:message key='ptjk_common_sc' defVal='删除' fileName='ptjk'/>" onclick="del('<%=base.getId() %>')" class="btnClass1"/>
						</td>
						</tr>
					<% 
				}
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
		</form>
		</div>

		<div id="arealist" title="<emp:message key='ptjk_jkxq_yw_jkqy' defVal='监控区域' fileName='ptjk'/>"  style="padding:5px;width:300px;height:160px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
		<div id="msg" style="width:100%;height:100%;overflow-y:auto;"><xmp></xmp></div>
		</div>
		
		<div id="notice" title="<emp:message key='ptjk_jkgl_yw_6' defVal='告警条件' fileName='ptjk'/>"  style="padding:5px;width:300px;height:160px;display:none;overflow-y:auto;">
		<div id="notice_msg" style="width:100%;height:100%;overflow-y:auto;"></div>
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
				}
				if(tempID.indexOf(city.get("provincecode").toString()+",")==-1){%>
						<input type="checkbox" name="roleId" id="cityId:<%=city.get("provincecode") %>" value="<%=city.get("provincecode") %>" />
						<label for="cityId:<%=city.get("provincecode") %>"><%=city.get("province").toString().replace("<","&lt;").replace(">","&gt;")%></label><br/>
			<% }
				}%>
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
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/mon_busCfg.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			var lgcorpcode = GlobalVars.lgcorpcode;
			$('#lgcorpcode').val(lgcorpcode);
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
				title : getJsLocaleMessage("ptjk","ptjk_jkgl_yw_1"),
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
