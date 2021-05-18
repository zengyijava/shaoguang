<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.servmodule.txgl.entity.XtGateQueue"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfPageField"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="java.util.Set"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	@ SuppressWarnings("unchecked")
	List<XtGateQueue> gateList = (List<XtGateQueue>) request.getAttribute("gateList");
	@ SuppressWarnings("unchecked")
	Map<Long, String> keyIdMap = (Map<Long, String>)request.getAttribute("keyIdMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	Set<String> gates = (Set<String>)request.getAttribute("mrXtList");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("passage");
	menuCode = menuCode==null?"0-0-0":menuCode;
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    @SuppressWarnings("unchecked")
	List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("pas_pagefileds");
    //运营商类型
    String spisuncm=request.getParameter("spisuncm");
    //通道类型
    String gateType=request.getParameter("routeFlag");
    //通道名称
    String gateName=request.getParameter("getename");
    //签名
    String cardname=request.getParameter("cardname");
    //通道号码
	String spgete = request.getParameter("spgate");
	boolean isBack = request.getParameter("isback") != null;//是否返回操作
	if(isBack){
	    spisuncm=(String)request.getAttribute("spisuncm");
	    gateType=(String)request.getAttribute("routeFlag");
	    gateName=(String)request.getAttribute("gatename");
	    cardname=(String)request.getAttribute("cardname");
		spgete = (String)request.getAttribute("spgate");
	}
	String txglFrame = skin.replace(commonPath, inheritPath);
	
	//通道号码
	String tdhm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_tdhm", request);
    if(tdhm!=null&&tdhm.length()>1){
    	tdhm = tdhm.substring(0,tdhm.length()-1);
    }
	//通道名称
	String tdmc = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_tdmc", request);
    if(tdmc!=null&&tdmc.length()>1){
    	tdmc = tdmc.substring(0,tdmc.length()-1);
    }
    //通道类型
	String tdlx = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_tdlxx", request);
	if(tdlx!=null&&tdlx.length()>1){
		tdlx = tdlx.substring(0,tdlx.length()-1);
	}
    if("zh_HK".equals(langName)){
		tdhm = tdhm.substring(8,tdhm.length()).replaceFirst("n","N");
		tdmc = tdmc.substring(8,tdmc.length()).replaceFirst("n","N");
		tdlx = tdlx.substring(8,tdlx.length()).replaceFirst("n","N");
	}
	//运营商
	String yys = MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_yys", request);
    if(yys!=null&&yys.length()>1){
    	yys = yys.substring(0,yys.length()-1);
    }
	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
	
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/pas_passage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="pas_passage">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<form name="pageForm" action="pas_passage.htm" method="post"
					id="pageForm">
					<div id="corpCode" class="hidden"></div>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent" >
					<div class="buttons rContent_div"  >
					<div id="toggleDiv">
						</div>
					<%if(btnMap.get(menuCode+"-1")!=null) { %>
						<a id="add" href="<%=iPath %>/pas_addPassage.jsp?lgcorpcode=<%=request.getParameter("lgcorpcode") %>"><emp:message key="txgl_wgqdpz_qyhdgl_xj" defVal="新建" fileName="txgl"></emp:message></a>
					<%} %>
					</div>
					<div id="condition">
						<table>
										<tr>
											<td > 
												 <emp:message key="txgl_wghdpz_tdgl_tdhm" defVal="通道号码：" fileName="txgl"></emp:message>
											</td>
											<td>
											  <select name="spgate" id="spgate">
											  <option value=""><emp:message key="txgl_wghdpz_wgyxcspz_qb" defVal="全部" fileName="txgl"></emp:message></option>
											  <%
												if (gates != null && gates.size() > 0)
												{
													for(String xgq : gates){
													 	%>
											<option  value="<%=xgq %>" <%if(xgq.equals(spgete)){%>selected='selected'<%}%>><%=xgq %></option>
											   <% }} %>
											  </select>
												
												<input type="hidden" id="spgatetype" name="spgatetype" value="" />
												<input type="hidden" name="spgate" id="spgate" value="<%=spgete!=null?spgete:"" %>"/>
											</td>
											<td >
												<emp:message key="txgl_wghdpz_tdgl_yys" defVal="运营商：" fileName="txgl"></emp:message>
											</td>
											<td>
												<select  class="select_wid"  id="spisuncm" name="spisuncm" isInput="false" >
														<option value=""><emp:message key="txgl_wghdpz_wgyxcspz_qb" defVal="全部" fileName="txgl"></emp:message></option>
														<option value="0" <%=spisuncm!=null?"0".equals(spisuncm)?"selected":"":"" %>><emp:message key="txgl_wghdpz_wgyxcspz_yd" defVal="移动" fileName="txgl"></emp:message></option>
														<option value="1" <%=spisuncm!=null?"1".equals(spisuncm)?"selected":"":"" %>><emp:message key="txgl_wghdpz_wgyxcspz_lt" defVal="联通" fileName="txgl"></emp:message></option>
														<option value="21" <%=spisuncm!=null?"21".equals(spisuncm)?"selected":"":"" %>><emp:message key="txgl_wghdpz_wgyxcspz_dx" defVal="电信" fileName="txgl"></emp:message></option>
														<option value="5" <%=spisuncm!=null?"5".equals(spisuncm)?"selected":"":"" %>><emp:message key="txgl_wghdpz_wgyxcspz_gw" defVal="国外" fileName="txgl"></emp:message></option>
												</select>
											</td>
											<td >
												<emp:message key="txgl_wghdpz_tdgl_qm" defVal="签名：" fileName="txgl"></emp:message>
											</td>
											<td>
												<input type="text"  class="cardname_input" 
													name="cardname"  size="20" value="<%=cardname!=null?StringEscapeUtils.escapeHtml(cardname):"" %>"/>
											</td>
											<td class="tdSer"><center><a id="search"></a></center></td>
										</tr>
										<tr>
											<td >
												<emp:message key="txgl_wghdpz_tdgl_tdmc" defVal="通道名称：" fileName="txgl"></emp:message>
											</td>
											<td>
												<input type="text"  class="getename_input" 
													name="getename"  size="20" value="<%=gateName!=null?StringEscapeUtils.escapeHtml(gateName):"" %>"/>
											</td>
											 <%
											//String typename="";
											if(pagefileds!=null&&pagefileds.size()>0)
											{
												LfPageField first=pagefileds.get(0);
												//typename=first.getField()+"：";
												if(0==first.getFiledShow() && pagefileds.size()>2 )
												{
											%>
											<td >
												<emp:message key="txgl_wghdpz_tdgl_tdlxx" defVal="通道类型：" fileName="txgl"></emp:message>
											</td>
											<td>
												<select name="routeFlag" id="routeFlag" class="select_wid" isInput="false" >
													<option value=""><emp:message key="txgl_wghdpz_wgyxcspz_qb" defVal="全部" fileName="txgl"></emp:message></option>
													<%
														for(int i=1;i<pagefileds.size();i++)
														{
														LfPageField pagefid=pagefileds.get(i);
														String channelType = pagefid.getSubFieldName();
														if("短信".equals(channelType)){
															channelType = "zh_HK".equals(empLangName)?"SMS":"短信";
														}
														if("彩信".equals(channelType)){
															channelType = "zh_HK".equals(empLangName)?"MMS":"彩信";
														}
													%>
													     <option value="<%=pagefid.getSubFieldValue() %>" <%=pagefid.getSubFieldValue()!=null?(pagefid.getSubFieldValue()).equals(gateType)?"selected":"":"" %>><%=channelType %></option>
													<% 
														}
													%>
												</select> 
											</td>
												<%
													}
													else{out.print("<td></td><td></td>");
													}
												}
												else
												{
													out.print("<td></td><td></td>");
												}
												%>

											<td > 
												
											</td>
											<td>
												
											</td>
											<td>&nbsp;</td>
										</tr>
							</table>
						</div>
					<table id="content"  class="content_table">
						<thead>
							<tr >
								<th><%=tdhm %></th> 
								<th><%=tdmc %></th> 
								<th><%=tdlx %></th> </th>
								<th><%=yys %></th> 
								<%-- <th>是否支持英文短信</th> --%>
								<th><emp:message key="txgl_wghdpz_tdgl_sfzccdx" defVal="是否支持长短信" fileName="txgl"></emp:message></th> 
								<th><emp:message key="txgl_wghdpz_tdgl_dxqmzyw" defVal="短信签名（中文/英文）" fileName="txgl"></emp:message></th>
								<th><emp:message key="txgl_wghdpz_tdgl_qmwz" defVal="签名位置" fileName="txgl"></emp:message></th>
								<th><emp:message key="txgl_wghdpz_tdgl_dtdxzs" defVal="单条短信字数（中文/英文）" fileName="txgl"></emp:message></th>
							    <th><emp:message key="txgl_wghdpz_tdgl_dxzdzs" defVal="短信最大字数（中文/英文）" fileName="txgl"></emp:message></th>
								<th><emp:message key="txgl_wghdpz_tdgl_zt" defVal="状态" fileName="txgl"></emp:message></th>
								<th colspan="2"><emp:message key="txgl_wghdpz_tdgl_xx" defVal="详细" fileName="txgl"></emp:message></th>
							</tr>
						</thead>
						<tbody>
						<% 
						if(gateList != null && gateList.size() > 0)
						{
							String keyId;
							for(int i=0 ;i< gateList.size();i++)
							{
								XtGateQueue gate = gateList.get(i);
								boolean isGW = gate.getSpisuncm()-5 == 0;
								int gpvelege=gate.getGateprivilege();
								boolean isSupportEn = (gpvelege&2)==2;
								keyId = keyIdMap.get(gate.getId());
						%>
							<tr>
								<td><%=gate.getSpgate() %></td>
								<td class="ztalign"><span class="maxWidth"><%=gate.getGateName()%></span></td>
								<td class="ztalign">
									<%=gate.getGateType()-1==0?"<font color='#159800'>"+MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_dux", request)+"</font>":
										gate.getGateType()-2==0?"<font color='#f1913c'>"+MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_cx", request)+"</font>":"--"%>
								</td>
								<td class="ztalign">
									<%=gate.getSpisuncm()-0==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_yd", request):
										gate.getSpisuncm()-1==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_lt", request):
										gate.getSpisuncm()-21==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_dx", request):
										gate.getSpisuncm()-5==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_gw", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_wxdyysbs", request)%>
								</td>
								<td class="ztalign">
									<%=gate.getGateType()-1==0?gate.getLongSms()-1==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_s", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_f", request):"--" %>
								</td>
								<td class="ztalign">
									<xmp><%=StringUtils.defaultIfEmpty(gate.getSignstr().trim(),"--")%>/<%if(isGW&&isSupportEn){out.print(StringUtils.defaultIfEmpty(gate.getEnsignstr().trim(),"--"));}else{out.print("--");} %></xmp>
								</td>
								<td class="ztalign">
								<%
									//String bytestr=Integer.toBinaryString(gpvelege);
									//String zerostr="";
									//for(int j=0;j<32-bytestr.length();j++){
									//	zerostr=zerostr+"0";
									//}
									//String gpstr=zerostr+bytestr;
									//String gpchar=gpstr.charAt(29)+"";
								 %>
									<%=gate.getGateType()-1==0?(gpvelege&4)==4?MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_qz", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_tdgl_hz", request):"--"%>
								</td>
								<td>
									<%
									if(gate.getGateType()-1==0){
										if(isGW&&isSupportEn){
											out.print(gate.getSingleLen()+"/"+gate.getEnsinglelen());
										}else{
											out.print(gate.getSingleLen()+"/--");
										}
									}else{
										out.print("--");
									}
									%>
								</td>
								<td>
									<%
									if(gate.getGateType()-1==0){
										if(isGW&&isSupportEn){
											out.print(gate.getMaxWords()+"/"+gate.getEnmaxwords());
										}else{
											out.print(gate.getMaxWords()+"/--");
										}
									}else{
										out.print("--");
									}
									%>
								</td>
								
								<%if(btnMap.get(menuCode+"-3")!=null) { %>
								<td class="ztalign">	
								<center>
								<div class="setControl"></div>
								<%
								if (gate.getStatus() == 0) {
								%>
								<select  name="gateState<%=gate.getId() %>" id="gateState<%=gate.getId() %>" idx="<%=gate.getId() %>" keyidx='<%=keyId %>' class="input_bd" onchange="javascript:changeStatu('<%=gate.getId() %>','<%=keyId %>')">
								<option value="0" selected="selected"><emp:message key="txgl_wghdpz_wgyxcspz_yjh" defVal="已激活" fileName="txgl"></emp:message></option>
								<option value="1" ><emp:message key="txgl_wghdpz_wgyxcspz_sx" defVal="失效" fileName="txgl"></emp:message></option>
								</select>
								<%		
								}else{
								%>
								<select  name="gateState<%=gate.getId() %>" id="gateState<%=gate.getId() %>" idx="<%=gate.getId() %>" keyidx='<%=keyId %>' class="input_bd" onchange="javascript:changeStatu('<%=gate.getId() %>','<%=keyId %>')">
								<option value="1" selected="selected"><emp:message key="txgl_wghdpz_wgyxcspz_ysx" defVal="已失效" fileName="txgl"></emp:message></option>
								<option value="0" ><emp:message key="txgl_wghdpz_wgyxcspz_jh" defVal="激活" fileName="txgl"></emp:message></option>									          
								</select>
								<%		
								}
								%>	
								</center>								
								</td>
								<%}else{ %>
								<td class="ztalign">
									<%=gate.getStatus()-0==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_jh", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_sx", request) %>
								</td>
								<%} %>


								<%-- <td>
								<%if(btnMap.get(menuCode+"-3")!=null) { %>
									<a onclick='changeStatu(<%=gate.getId() %>)' id='a<%=gate.getId() %>'><%=gate.getStatus()-1==0?MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_jh", request):MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_sx", request) %></a>
									<%} %>
								</td> --%>
								<td>
									<%
									if(StaticValue.getCORPTYPE() ==1&&!"100000".equals(request.getParameter("lgcorpcode"))){
									%>
									<%
										}else{
									 %>
									<a href="pas_passage.htm?method=toEdit&id=<%=gate.getId() %>&lguserid=<%=request.getParameter("lguserid") %>&lgcorpcode=<%=request.getParameter("lgcorpcode") %>&keyId=<%=keyId %>"><emp:message key="txgl_wghdpz_wgyxcspz_xg" defVal="修改" fileName="txgl"></emp:message></a>&nbsp;&nbsp;&nbsp;
									<%
										}
									%>
								</td>
								<td>
									<a href = "pas_passage.htm?method=getDetail&id=<%=gate.getId() %>&keyId=<%=keyId %>"><emp:message key="txgl_wghdpz_tdgl_xx" defVal="详细" fileName="txgl"></emp:message></a>
								</td> 
							</tr>
						<%
						 	}
						 }else
						 {
							 out.print("<tr><td colspan='12'>"+MessageUtils.extractMessage("txgl", "txgl_wghdpz_wgyxcspz_wjl", request)+"</td></tr>");
						 }
						%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="12">
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
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
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
		</script>
		<script type="text/javascript" src="<%=iPath %>/js/pas_passage.js?V=<%=StaticValue.getJspImpVersion()%>" ></script>
</body>
</html>
