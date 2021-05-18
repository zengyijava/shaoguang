<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.servmodule.txgl.vo.AreaPhNoVo" %>
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
	String menuCode = titleMap.get("areaPhoneNo");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	List<AreaPhNoVo> areaList = (List<AreaPhNoVo>)request.getAttribute("areaList");
	@ SuppressWarnings("unchecked")
	List<String> provinceList = (List<String>)request.getAttribute("provinceList");
	@ SuppressWarnings("unchecked")
	Map<String,List<String>> provinceAndCityMap=(Map<String,List<String>>)request.getAttribute("provinceAndCityMap");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String txglFrame = skin.replace(commonPath, inheritPath);
    
    //移动
	String yd = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_yd", request);
    //联通
    String lt = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_lt", request);
    //电信
    String dx = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_dx", request);
    //未知
	String unknown = MessageUtils.extractMessage("txgl", "txgl_wygl_xhzwhmgl_wz", request);
    //区域代码:
    String qydm = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_qyhdgl_qydm", request);
    if(qydm!=null&&qydm.length()>1){
    	qydm = qydm.substring(0,qydm.length()-1);
    }
	//省份
	String sf = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_qyhdgl_sf", request);
    if(sf!=null&&sf.length()>1){
    	sf = sf.substring(0,sf.length()-1);
    }
	//城市
	String cs = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_qyhdgl_cs", request);
    if(cs!=null&&cs.length()>1){
    	cs = cs.substring(0,cs.length()-1);
    }
	//运营商
	String yys = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_yys", request);
    if(yys!=null&&yys.length()>1){
    	yys = yys.substring(0,yys.length()-1);
    }
	//号段
	String hd = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_qyhdgl_hd", request);
    if(hd!=null&&hd.length()>1){
    	hd = hd.substring(0,hd.length()-1);
    }
	//操作
	String cz = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_cz", request);

    //新建
    String xj = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_qyhdgl_xj", request);
	String mobile=request.getParameter("mobile");
	String areaCode = request.getParameter("areaCode");
	String province = request.getParameter("province");
	String servePro = request.getParameter("servePro");
	String city=request.getParameter("city");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_qyhdgl_yyshdgl" defVal="运营商号段管理" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/areaphno.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="areaphno">
	<input type="hidden" id="pathUrl" value="<%=path %>"/>
	<input type="hidden" id="provinceStr" value="<%=province%>"/>
	<input type="hidden" id="skin" value="<%=skin%>"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<form name="pageForm" action="seg_areaPhoneNo.htm?method=find" method="post" id="pageForm">
			<div id="corpCode" class="hidden"></div>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
			<div id="rContent" class="rContent">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1")!=null) { %>
						<a id="add" href="javascript:showAreaPhNo()"><emp:message key="txgl_wgqdpz_qyhdgl_xj" defVal="新建" fileName="txgl"></emp:message></a>
						<% }%>
					</div>
					<div id="condition">
						<table>
									<input type="hidden" id="selectedType" name="selectedType" value=""/>
										<tr>
											<td > 
												 <emp:message key="txgl_wgqdpz_qyhdgl_qydm" defVal="区域代码：" fileName="txgl"></emp:message>
											</td>
											<td>
												<input type="text"  class="areaCode" 
													name="areaCode" id="areaCode" size="20" value="<%=areaCode==null?"":areaCode %>"/>
											</td>
											<td >
												<emp:message key="txgl_wgqdpz_qyhdgl_sf" defVal="省份：" fileName="txgl"></emp:message>
											</td>
											<td>
												<select  class="province"  id="province" name="province" onchange="getcity()" isInput="false">
													   <option value=""><emp:message key="txgl_wgqdpz_qyhdgl_qxz" defVal="请选择" fileName="txgl"></emp:message></option>
													   <%if (provinceList != null&&provinceList.size()>0){
													   		for(int i=0;i<provinceList.size();i++){
													   		String proStr=provinceList.get(i);
													   %>
													   			<option value="<%=proStr %>" <%=proStr.equals(province) ?"selected" :""%> ><%=proStr %></option>
													   		<% }%>
													  	
													   <%} %>
											    </select>
											</td>
											<td >
												<emp:message key="txgl_wgqdpz_qyhdgl_cs" defVal="城市：" fileName="txgl"></emp:message>
											</td>
											<td >
												 <select class="city" id="city" name="city" isInput="false" >
    												<option id="cityOption" value=""><emp:message key="txgl_wgqdpz_qyhdgl_qxz" defVal="请选择" fileName="txgl"></emp:message></option>
    												<%if(city!=null&&!city.equals("")) {%>
    												<option value="<%=city %>" selected="selected"><%=city %></option>
    												<% }%>
   												 </select>
											</td>
											<td class="tdSer"><center><a id="search"></a></center></td>
										</tr>
										<tr>
											<td>
												<emp:message key="txgl_wgqdpz_yyshdgl_yys" defVal="运营商：" fileName="txgl"></emp:message>
											</td>
											<td>
												<label>
													<select  class="servePro"  name="servePro" id="servePro" isInput="false" >
														<option value=""><emp:message key="txgl_wgqdpz_qyhdgl_qb" defVal="全部" fileName="txgl"></emp:message></option>
														<option value="移动" <%="移动".equals(servePro)?"selected":"" %>>
															<emp:message key="txgl_wgqdpz_yyshdgl_yd" defVal="移动" fileName="txgl"></emp:message></option>
														<option value="联通" <%="联通".equals(servePro)?"selected":"" %>>
															<emp:message key="txgl_wgqdpz_yyshdgl_lt" defVal="联通" fileName="txgl"></emp:message></option>
														<option value="电信" <%="电信".equals(servePro)?"selected":"" %>>
															<emp:message key="txgl_wgqdpz_yyshdgl_dx" defVal="电信" fileName="txgl"></emp:message></option>
													</select>
												</label>
											</td>
											<td>
												<emp:message key="txgl_wgqdpz_qyhdgl_hd" defVal="号段：" fileName="txgl"></emp:message>
											</td>
											<td>
												<input type="text"
													   onkeyup="value=value.replace(/\D/g,'')"
													   class="mobile" name="mobile" id="mobile" value="<%=mobile==null?"":mobile %>">
											</td>
										</tr>
							</table>
						</div>
						<table id="content" class="content_table">
							<thead>
							<tr>
									<th>
										<%=qydm %>
									</th>
									<th>
										<%=sf %>
									</th> 
									<th>
										<%=cs %>
									</th>
									<th>
										<%=yys %>
									</th>
									<th>
										<%=hd %>
									</th>
								<%if(btnMap.get(menuCode+"-2")!=null )                       		
										{ 
										%>
									<th <%if(btnMap.get(menuCode+"-2")!=null)                       		
										{ %> <%} %>>
										<%=cz %>
									</th>
									<%	} %>
								</tr>
							</thead>
							<tbody>
							<%
								@ SuppressWarnings("unchecked")
			                 	AreaPhNoVo ap;
			                	if(areaList != null&&areaList.size()>0)                       		
			                	{                        	
									for(int i=0;i<areaList.size();i++ )
									{
										ap=areaList.get(i);
										String operatorName = ap.getServePro();
										if("未知".equals(operatorName)){
											operatorName = unknown;
										}else{
											operatorName = "移动".equals(operatorName)?yd:"联通".equals(operatorName)?lt:dx;
										}
			                 %>
								<tr>
									<td class="content_td1">
										<%=ap.getAreaCode() %>
									</td>
									<td class="content_td2"><%=ap.getProvince() %></td>
									<td class="content_td3"><%=ap.getCity() %></td>
									<td class="content_td4"><%= operatorName %></td>
									<td class="content_td5"><%=ap.getMoblie() %></td>
									<td class="content_td6">
									<%
									if (btnMap.get(menuCode + "-2") != null) {
										%>
											<%if(!"".equals(ap.getId())){ %>
											<a href="javascript:doDel('<%=ap.getId() %>','<%=ap.getKeyId() %>')"><emp:message key="txgl_wgqdpz_qyhdgl_sc" defVal="删除" fileName="txgl"></emp:message></a>
									</td>
											<%} %>	
									<% }%>	
								</tr>
								<% }} else{%>
				                <tr>
								<td colspan="6">
                                   <emp:message key="txgl_wgqdpz_qyhdgl_wjl" defVal="无记录" fileName="txgl"></emp:message>
								</td>
							    </tr>
							    <%} %>
				               
								
						</tbody>
						<tfoot>
							<tr>
								<td colspan="6">
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
		
		<div id="addAreaPhNoDiv" title="<%=xj %>" class="addAreaPhNoDiv">
			<iframe id="addAreaPhNoDivFrame" name="addAreaPhNoDivFrame" class="addAreaPhNoDivFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
		</div>
		<div class="clear"></div>
			<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>	 
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript">
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		function doDel(id,keyId){
		//if(confirm("你确定要删除此条记录吗？")==true)
		if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_2"))==true)
			{
	  			$.post("<%=path%>/seg_areaPhoneNo.htm?method=deleteAreaPhoneNo",{id:id,keyId:keyId},function(result){
					if(result>0){
                        /*删除成功！*/
                        alert(getJsLocaleMessage("common","common_deleteSucceed"));
						submitForm();
		
					}else if(result<=0){
                        /*删除失败！*/
                        alert(getJsLocaleMessage("common","common_deleteFailed"));
					}
				});
			 }
		}
		    function showAreaPhNo()
		    {
		        $("#addAreaPhNoDivFrame").attr("src","<%=path %>/seg_areaPhoneNo.htm?method=doAdd");
		              
		    	$("#addAreaPhNoDiv").dialog("open");
		    };

   			 function closeAddAreaPhNodiv()
		    {
		    	$("#addAreaPhNoDiv").dialog("close");
		    	$("#addAreaPhNoDivFrame").attr("src","");
		    	
		    };
		    	$("#addAreaPhNoDiv").dialog({	    
				autoOpen: false,
				height:290,
				width: 540,
				resizable:false,
				modal: true,
				open:function(){
					
				},
				close:function(){
					$("#addAreaPhNoDiv").attr("src","");
				}
			});
			
		</script>
		<script type="text/javascript" src="<%=iPath%>/js/areaphno.js" ></script>
	</body>
</html>
