<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.entity.pasroute.GtPortUsed" %>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="com.montnets.emp.servmodule.txgl.entity.XtGateQueue" %>
<%@ page import="com.montnets.emp.servmodule.txgl.entity.Userdata" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.montnets.emp.servmodule.txgl.biz.ViewParams" %>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("route");
	@ SuppressWarnings("unchecked")
	List<XtGateQueue> gates = (List<XtGateQueue>)request.getAttribute("mrXtList");
	@ SuppressWarnings("unchecked")
	List<Userdata> userList = (List<Userdata>)request.getAttribute("userdataList");
	@ SuppressWarnings("unchecked")
	Map<Long, String> keyIdMap = (Map<Long, String>)request.getAttribute("keyIdMap");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String txglFrame = skin.replace(commonPath, inheritPath);
    
    //通道号码
    String tdhm = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_tdhm", request);
    if(tdhm!=null&&tdhm.length()>1){
    	tdhm = tdhm.substring(0,tdhm.length()-1);
    }
	//通道子号
	String tdzh = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_tdzh", request);
    if(tdzh!=null&&tdzh.length()>1){
    	tdzh = tdzh.substring(0,tdzh.length()-1);
    }
	//运营商
	String yys = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_yys", request);
    if(yys!=null&&yys.length()>1){
    	yys = yys.substring(0,yys.length()-1);
    }
	//路由状态
	String lyzt = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_lyzt", request);
    if(lyzt!=null&&lyzt.length()>1){
    	lyzt = lyzt.substring(0,lyzt.length()-1);
    }
	//短信签名（中文/英文）
	String dxqm = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_dxqm", request);
	//路由类型
	String lylx = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_lylx", request);
    if(lylx!=null&&lylx.length()>1){
    	lylx = lylx.substring(0,lylx.length()-1);
    }
	//操作
	String cz = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_cz", request);
										
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_zhtdpz_lypzcx" defVal="路由配置查询" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/par_route.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="par_route">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<form name="pageForm" action="par_route.htm?method=find" method="post" id="pageForm">
			<div id="corpCode" class="hidden"></div>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1")!=null) { %>
						<a id="add" href="javascript:toAdd()"><emp:message key="txgl_wgqdpz_qyhdgl_xj" defVal="新建" fileName="txgl"></emp:message></a>
						<%}if(btnMap.get(menuCode+"-2")!=null) { %>
						<a id="delete" ><emp:message key="txgl_wgqdpz_zhtdpz_sc" defVal="删除" fileName="txgl"></emp:message></a>
						<%} %>
					</div>
					<div id="condition">
						<table>
									<%
										String spgate=request.getParameter("spgate");
										String spisuncm=request.getParameter("spisuncm");
										String userId=request.getParameter("userId");
										String status=request.getParameter("status");
										String routeFlag=request.getParameter("routeFlag");
										String cpno=request.getParameter("cpno");
										String spgatetype=request.getParameter("spgatetype");
										String accouttype=request.getParameter("accouttype");
										boolean isBack = request.getParameter("isback") != null;//是否返回操作
										if(isBack){
											spgate=(String)request.getAttribute("spgate");
											spisuncm=(String)request.getAttribute("spisuncm");
											userId=(String)request.getAttribute("userId");
											status=(String)request.getAttribute("status");
											routeFlag=(String)request.getAttribute("routeFlag");
											cpno=(String)request.getAttribute("cpno");
											spgatetype=(String)request.getAttribute("spgatetype");
											accouttype=(String)request.getAttribute("accouttype");
										}
									%>
										<tr>
											<td > 
												<emp:message key="txgl_wgqdpz_zhtdpz_tdhm" defVal="通道号码：" fileName="txgl"></emp:message> 
											</td>
											<td>
												<select name="tdhm_key" class="select_st"  id="tdhm_key">
													<option gateid="0"  gatetype="" value=""><emp:message key="txgl_wygl_xhzwcwdm_qb" defVal="全部" fileName="txgl"></emp:message> </option>
													<%
													if (gates != null && gates.size() > 0)
													{
						                            	for(XtGateQueue xgq : gates){
						                            	 %>
														<option gateid="<%=xgq.getId() %>" gatetype="<%=xgq.getGateType() %>" value="<%=xgq.getSpgate() %>" 
														<%=xgq.getSpgate().equals(spgate)&&xgq.getGateType().toString().equals(spgatetype)?"selected":""
																%>><%=xgq.getSpgate() %>(<%=xgq.getSpisuncm()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_lt", request):xgq.getSpisuncm()-0==0?
																MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_yd", request):xgq.getSpisuncm()-21==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_dx", request):xgq.getSpisuncm()-5==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_gw", request):""%><%=xgq.getGateType()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_dx", request):xgq.getGateType()-2==0?
																MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_cx", request):""%>)</option>                            
						                            <% }} %>
												</select>
												<input type="hidden" id="spgatetype" name="spgatetype" value="<%=spgatetype==null?"":spgatetype %>" />
												<input type="hidden" id="spgate" value="<%=spgate!=null?spgate:"" %>" name="spgate">
												
											</td>
											<td >
												<emp:message key="txgl_wgqdpz_yyshdgl_yys" defVal="运营商：" fileName="txgl"></emp:message>
											</td>
											<td>
												<select  class="select_wi"  id="spisuncm" name="spisuncm" isInput="false" >
														<option value=""><emp:message key="txgl_wgqdpz_qyhdgl_qb" defVal="全部" fileName="txgl"></emp:message></option>
														<option value="0" <%="0".equals(spisuncm)?"selected":"" %>><emp:message key="txgl_wgqdpz_yyshdgl_yd" defVal="移动" fileName="txgl"></emp:message></option>
														<option value="1" <%="1".equals(spisuncm)?"selected":"" %>><emp:message key="txgl_wgqdpz_yyshdgl_lt" defVal="联通" fileName="txgl"></emp:message></option>
														<option value="21" <%="21".equals(spisuncm)?"selected":"" %>><emp:message key="txgl_wgqdpz_yyshdgl_dx" defVal="电信" fileName="txgl"></emp:message></option>
														<option value="5" <%="5".equals(spisuncm)?"selected":"" %>><emp:message key="txgl_wgqdpz_zhtdpz_gw" defVal="国外" fileName="txgl"></emp:message></option>
												</select>
											</td>
											<td >
												<%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_3",request)%>
											</td>
											<td>
												<select  class="select_st"  name="spCard" id="spCard">
													<option uid="0" gatetype="" value=""><emp:message key="txgl_wgqdpz_qyhdgl_qb" defVal="全部" fileName="txgl"></emp:message></option>
													 <%
													 if (userList != null && userList.size() > 0)
													 {
									                 	for(Userdata user : userList){
							                		 %>
							                		 <option uid="<%=user.getUid() %>" gatetype="<%=user.getAccouttype() %>" value="<%=user.getUserId() %>" 
							                		 <%=user.getUserId().equals(userId)&&user.getAccouttype().toString().equals(accouttype)?"selected":"" %>
							                		 ><%=user.getUserId() %>(<%=user.getAccouttype()==1?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_dxspzh", request):user.getAccouttype()==2?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_cxspzh", request):"" %>)</option>
							                     	<% } }%>
												</select>
												<input type="hidden" id="accouttype" name="accouttype" value="<%=accouttype==null?"":accouttype %>" />
												<input type="hidden" id="userId" value="<%=userId!=null?userId:"" %>" name="userId">
												
											</td>
											<td class="tdSer"><center><a id="search"></a></center></td>
										</tr>
										<tr>
											<td >
												<emp:message key="txgl_wgqdpz_zhtdpz_lyzt" defVal="路由状态：" fileName="txgl"></emp:message>
											</td>
											<td>
												<label>
													<select  class="select_wi"  name="status" id="status" isInput="false" >
														<option value=""><emp:message key="txgl_wgqdpz_qyhdgl_qb" defVal="全部" fileName="txgl"></emp:message></option>
														<option value="0" <%="0".equals(status)?"selected":"" %>><emp:message key="txgl_wgqdpz_zhtdpz_qy" defVal="启用" fileName="txgl"></emp:message></option>
														<option value="1" <%="1".equals(status)?"selected":"" %>><emp:message key="txgl_wgqdpz_zhtdpz_jy" defVal="禁用" fileName="txgl"></emp:message></option>
													</select>
												</label>
											</td>
											<td >
												<emp:message key="txgl_wgqdpz_zhtdpz_lylx" defVal="路由类型：" fileName="txgl"></emp:message>
											</td>
											<td>
												<select name="routeFlag" id="routeFlag"  class="select_wi" isInput="false" >
													<option value=""><emp:message key="txgl_wgqdpz_qyhdgl_qb" defVal="全部" fileName="txgl"></emp:message></option>
													<option value="2" <%="2".equals(routeFlag)?"selected":"" %>><emp:message key="txgl_wgqdpz_zhtdpz_sxly" defVal="上行路由" fileName="txgl"></emp:message></option>
													<option value="1" <%="1".equals(routeFlag)?"selected":"" %>><emp:message key="txgl_wgqdpz_zhtdpz_xxly" defVal="下行路由" fileName="txgl"></emp:message></option>
													<option value="0" <%="0".equals(routeFlag)?"selected":"" %>><emp:message key="txgl_wgqdpz_zhtdpz_sxxly" defVal="上/下行路由" fileName="txgl"></emp:message></option>
												</select> 
											</td>

											<td > 
												 <emp:message key="txgl_wgqdpz_zhtdpz_tdzh" defVal="通道子号：" fileName="txgl"></emp:message>
											</td>
											<td>
												<input type="text"  class="cpno" 
													name="cpno" id="cpno" size="20" value="<%=cpno==null?"":cpno.replace("&","&amp;").replace("\"","&quot;") %>"/>
											</td>
											<td>&nbsp;</td>
										</tr>
							</table>
						</div>
						<table id="content" class="content_table">
							<thead>
							<tr>
									 <th>
										<input type="checkbox" name="dels" value="" onclick="checkAlls(this,'delRouteId')" />
									</th>
									<th>
										<%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_1",request)%>
									</th>
									<th>
										<%=tdhm %>
									</th>
									<th>
										<%=tdzh %>
									</th> 
									<th>
										<%=yys %>
									</th>
									<th>
										<%=lyzt %>
									</th>
									<th>
										<%=dxqm %>
									</th>
									<th>
										<%=lylx %>
									</th>
									
								<%if(btnMap.get(menuCode+"-2")!=null || btnMap.get(menuCode+"-3")!=null)                       		
										{ 
										%>
									<th <%if(btnMap.get(menuCode+"-2")!=null && btnMap.get(menuCode+"-3")!=null)                       		
										{ %>colspan="2" <%} %>>
										<%=cz %>
									</th>
									<%	} %>

									
								</tr>
							</thead>
							<tbody>
							<%
								@ SuppressWarnings("unchecked")
			                 	List<GtPortUsed> routeList=(List<GtPortUsed>)request.getAttribute("routeList");
			                 	GtPortUsed gt=null;
			                	if(routeList != null&&routeList.size()>0)                       		
			                	{       
			                		 String keyId;
									for(int i=0;i<routeList.size();i++ )
									{
										gt=routeList.get(i);
										 keyId = keyIdMap.get(gt.getId());
			                 %>
								<tr>
									<td> <input type="checkbox" name="delRouteId" id="" value="<%=keyId %>" /> </td> 
									<td><%=gt.getUserId() %>
									</td>
									<td><%=gt.getSpgate() %>(<% if(gt.getGateType()-1==0){ %><font color='#159800'><% }else{%><font color='#f1913c'><%}%><%=gt.getSpisuncm()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_lt", request):gt.getSpisuncm()-0==0?
												MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_yd", request):gt.getSpisuncm()-21==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_dx", request):gt.getSpisuncm()-5==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_gw", request):""%><%=gt.getGateType()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_dx", request):gt.getGateType()-2==0?
												MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_cx", request):""%></font>)
									</td>
									<td class="getCpno_td"><%=gt.getCpno() %>
									</td> 
									<td class="ztalign">
										<%if(gt.getSpisuncm()==0) {%><emp:message key="txgl_wgqdpz_yyshdgl_yd" defVal="移动" fileName="txgl"></emp:message><%}else if(gt.getSpisuncm()==1) {%> <emp:message key="txgl_wgqdpz_yyshdgl_lt" defVal="联通" fileName="txgl"></emp:message> <%}else if(gt.getSpisuncm()==21){ %><emp:message key="txgl_wgqdpz_yyshdgl_dx" defVal="电信" fileName="txgl"></emp:message><%}else{ %><emp:message key="txgl_wgqdpz_zhtdpz_gw" defVal="国外" fileName="txgl"></emp:message><%} %>
									</td>
									<%if(btnMap.get(menuCode+"-3")!=null) { %>
									<td class="ztalign">
									<center>	
									<div class="setControl"></div>								
										<%
										if (gt.getStatus()==0) {
										%>
											<select  name="gtState<%=gt.getId() %>" id="gtState<%=gt.getId() %>" idx="<%=gt.getId() %>" keyidx="<%=keyId %>" class="input_bd" onchange="javascript:changestate('<%=gt.getId() %>','<%=keyId %>')">
									          <option value="0" selected="selected"><emp:message key="txgl_wgqdpz_zhtdpz_yqy" defVal="已启用" fileName="txgl"></emp:message></option>
									          <option value="1" ><emp:message key="txgl_wgqdpz_zhtdpz_jy" defVal="禁用" fileName="txgl"></emp:message></option>
									        </select>
										<%		
										}else{
										%>
										   <select  name="gtState<%=gt.getId() %>" id="gtState<%=gt.getId() %>" idx="<%=gt.getId() %>" keyidx="<%=keyId %>" class="input_bd" onchange="javascript:changestate('<%=gt.getId() %>','<%=keyId %>')">
									          <option value="1" selected="selected"><emp:message key="txgl_wgqdpz_zhtdpz_yjy" defVal="已禁用" fileName="txgl"></emp:message></option>
									          <option value="0" ><emp:message key="txgl_wgqdpz_zhtdpz_qy" defVal="启用" fileName="txgl"></emp:message></option>									          
									        </select>
										<%		
										}
										 %>	
										 </center>								
									</td>
									<%}else{ %>
									<td class="ztalign">
										<%if(gt.getStatus()==0) {%><emp:message key="txgl_wgqdpz_zhtdpz_qy" defVal="启用" fileName="txgl"></emp:message><%}else{ %> <emp:message key="txgl_wgqdpz_zhtdpz_jy" defVal="禁用" fileName="txgl"></emp:message><%} %>
									</td>
									<%} %>
									
									
									<td class="textalign"><center><xmp><%=StringUtils.defaultIfEmpty(gt.getSignstr().trim(), "--")%>/<%out.print(StringUtils.defaultIfEmpty(gt.getEnsignstr().trim(), "--")); %></xmp></center>
									</td>
									<td class="ztalign">
										<%if(gt.getRouteFlag()==1){%><emp:message key="txgl_wgqdpz_zhtdpz_xxly" defVal="下行路由" fileName="txgl"></emp:message><%} %>
										<%if(gt.getRouteFlag()==2){ %><emp:message key="txgl_wgqdpz_zhtdpz_sxly" defVal="上行路由" fileName="txgl"></emp:message><%} %>
										<%if(gt.getRouteFlag()==0){ %><emp:message key="txgl_wgqdpz_zhtdpz_sxxly" defVal="上/下行路由" fileName="txgl"></emp:message><%} %>
									</td>
									<%              		
										if(btnMap.get(menuCode+"-3")!=null)                       		
										{                        	
									%>
									<td>
										<a href="par_route.htm?method=toEdit&id=<%=gt.getId() %>&lguserid=<%=request.getParameter("lguserid") %>&lgcorpcode=<%=request.getParameter("lgcorpcode") %>&keyId=<%=keyId %>"><emp:message key="txgl_wgqdpz_yyshdgl_xg" defVal="修改" fileName="txgl"></emp:message></a>
									</td>
									<%
										}
										if(btnMap.get(menuCode+"-2")!=null)                       		
										{                        	
									%>
									<td>
										
										<a href="javascript:del('<%=gt.getId() %>','<%=gt.getGateType() %>','<%=keyId %>')"><emp:message key="txgl_wgqdpz_zhtdpz_sc" defVal="删除" fileName="txgl"></emp:message></a>
									</td>
									<%
										}
									%>
								</tr>
								<%
				                	}
				                } else{%>
				                <tr>
								<td colspan="10">
                                    <emp:message key="txgl_wgqdpz_zhtdpz_wjl" defVal="无记录" fileName="txgl"></emp:message>
								</td>
							    </tr>
				                <%} %>
								
						</tbody>
						<tfoot>
							<tr>
								<td colspan="10">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
			</div>
			<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</form>
		</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript">
		var findresult="<%=(String)request.getAttribute("findresult")%>";
		var total=<%=pageInfo.getTotalPage()%>;// 总页数
		var pageIndex=<%=pageInfo.getPageIndex()%>;// 当前页数
		var pageSize=<%=pageInfo.getPageSize()%>;// 每页记录数
		var totalRec=<%=pageInfo.getTotalRec()%>;// 总记录数
		var inheritPath="<%=inheritPath%>";
		//请求删除路由信息地址
		var par_route_path="par_route.htm?lgcorpcode=<%=request.getParameter("lgcorpcode")%>&lguserid=<%=request.getParameter("lguserid")%>";
		</script>
		<script type="text/javascript" src="<%=iPath %>/js/par_route.js" ></script>
	</body>
</html>
