<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.entity.lbs.LfLbsPios"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import="com.montnets.emp.ottbase.constant.WXStaticValue"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
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
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("lbsManager");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	@ SuppressWarnings("unchecked")
	List<LfWeiAccount> weixAccountList = (List<LfWeiAccount>)request.getAttribute("weixAccountList");
	//跳转方式  add /  update
	String skiptype = (String)request.getAttribute("skiptype");
	//地址采集点
	LfLbsPios lbsPios = (LfLbsPios)request.getAttribute("lbsPios");
	//初始化pid值  在add的情况下无效，在update的时候有效
	Long pid = 0L;
	if(lbsPios != null){
	    pid = lbsPios.getPid();
	}
	//百度地图密钥
	String ak = WXStaticValue.BAIDU_MAP_AK;
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/location.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=commonPath%>/wxcommon/css/global.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=commonPath%>/wxcommon/css/select.css?V=<%=StaticValue.getJspImpVersion() %>">
		
		<style type="text/css">
			body,html,#allmap {
				width: 100%;
				height: 100%;
				overflow: hidden;
				margin: 0;
			}
		</style>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>	
	</head>
	<body>
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
				<div id="detail_Info" style="padding-left: 40px;"> 
                <form name="updateInfo" method="post" action="">
                	<input id="pathUrl" value="<%=path %>" type="hidden" />
                	<div id="getloginUser" style="display:none;"></div>
                	<%-- 页面类型  update / add --%>
                	<input type="hidden" value="<%=skiptype %>" name="pagetype" id="pagetype" />
                	<%--采集点pid --%>
                	<input type="hidden" value="<%=pid%>" name="pid" id="pid" />
                	<%--ak--%>
                	<input type="hidden" value="<%=ak%>" name="mapAk" id="mapAk" />
                	<div id="moreSelect">
                	<table  width="100%" height="100%" style="font-size: 12px;line-height: 30px !important; line-height: 20px;">         	
       			 	<thead>
               	  		 <tr>
               	  		 <td width="<%=StaticValue.ZH_HK.equals(langName)?140:80 %>"><span><emp:message key="wxgl_gzhgl_title_54" defVal="公众帐号：" fileName="wxgl"/></span></td>
               	  		  <td>
               	  		  		<label>
	                     		<select name="account" id="account" class="input_bd" >
	                     			<option value=""><emp:message key="wxgl_gzhgl_title_124" defVal="请选择" fileName="wxgl"/></option>
	                     			<% 
	                     				if(weixAccountList != null && weixAccountList.size() > 0 ){
	                     				   LfWeiAccount weiAccount = null;
	                     				    for(int i=0;i<weixAccountList.size();i++){
	                     				       weiAccount = weixAccountList.get(i);
	                     			%>
	                     						<option value="<%=weiAccount.getAId() %>"  <%if(lbsPios != null && weiAccount.getAId().equals(Long.valueOf(lbsPios.getAId()))){%> selected="selected"<%} %>  ><%=weiAccount.getName() %></option>
	                     			<% 
	                     				    }
	                     				}
	                     			%>
	                     		</select>
	                     		</label>
	                     	</td>  
                     	</tr>
                         <tr >
		                    	<td>
									<span><emp:message key="wxgl_gzhgl_title_125" defVal="标题：" fileName="wxgl"/></span>
								</td>
								<td>
									<input type="text"  name="title" id="title"  
										value="<%if(lbsPios != null){out.print(lbsPios.getTitle());} %>"  
										maxlength="32" class="input_bd" style="width:320px;" />
									<font color="red">*</font>
								</td>
	                    	</tr>                                      
						<tr>
							<td>
								<span><emp:message key="wxgl_gzhgl_title_126" defVal="关键字：" fileName="wxgl"/></span>
							</td>
							<td>
								<input type="text"  name="keyword" id="keyword"    
								value="<%if(lbsPios != null){out.print(lbsPios.getKeyword());} %>"  
								class="input_bd" style="width:320px;" />
								<font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
						 <tr>
							<td>
								<span><emp:message key="wxgl_gzhgl_title_127" defVal="简介：" fileName="wxgl"/></span>
							</td>
							<td>
								<textarea id="note" name="note" class="input_bd" style="width:320px;height:60px;"><%if(lbsPios != null){out.print(lbsPios.getNote()==null?"":lbsPios.getNote());} %></textarea>
							</td>
						</tr>
						<tr>
							<td>
								<span><emp:message key="wxgl_gzhgl_title_128" defVal="联系电话：" fileName="wxgl"/></span>
							</td>
							<td>
								<input type="text"  name="telephone" id="telephone"   
								value="<%if(lbsPios != null){out.print(lbsPios.getTelephone());} %>" 
								 class="input_bd" style="width:320px;" />
								<font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<span><emp:message key="wxgl_gzhgl_title_129" defVal="地址：" fileName="wxgl"/></span>
							</td>
							<td>
								<input type="text"  name="address" id="address"   
								value="<%if(lbsPios != null){out.print(lbsPios.getAddress());}else{out.print("");}%>"  
								class="input_bd" style="width:320px;"/>
								<font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;
								<%-- javascript:addressResolution() --%>
								<input type="button" id="searchbtn" onclick="getphyaddress();"  value="<emp:message key='wxgl_gzhgl_title_130' defVal='地址定位' fileName='wxgl'/>" class="btnClass3 mr23"/>
							</td>
						</tr>
						 <tr>
							<td>
								<span><emp:message key="wxgl_gzhgl_title_131" defVal="经度与纬度：" fileName="wxgl"/></span>
							</td>
							<td>
								<input type="text"  name="lng" id="lng" 
								value="<%if(lbsPios != null){out.print(lbsPios.getLng());} %>" 
								class="input_bd" style="width:140px;"/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="text"  name="lat" id="lat" 
								value="<%if(lbsPios != null){out.print(lbsPios.getLat());} %>" 
								class="input_bd" style="width:140px;"/>
								<font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" id="searchbtn" onclick="javascript:searchAddressbylnglat()"  value="<emp:message key='wxgl_gzhgl_title_132' defVal='定位' fileName='wxgl'/>" class="btnClass3 mr23"/>
							</td>
						</tr>
						 <tr style="height: 10px;"><%--
						 	<td>
								<input type="text"  name="city" id="city" class="input_bd" style="width:70px;" value="深圳"/>
							</td>
							<td>
								<input type="text"  name="location" id="location" class="input_bd" style="width:240px;" value="电影院"/>
								<input type="button" id="searchbtn" onclick="javascript:searchLocation('jk')"  value="定位" class="btnClass3 mr23"/>
							</td>
						--%>
						</tr>
						<tr>
							<td colspan="2" style="height: 220px;">
								<div id="allmap" style="display:block;border: 1px #52a6e7 solid;"></div>
							</td>
						</tr>
						<tr style="line-height: 15px !important;">
                         	<td>
                         	  &nbsp;
                         	</td>
                         </tr>
                         <tr>
                         	<td  id="btn" align="center"  colspan="2">
                         		<input type="button" id="addupdatebtn" onclick="addupdateLbs('<%=skiptype %>')"  value="<emp:message key='wxgl_button_6' defVal='保存' fileName='wxgl'/>" class="btnClass5 mr23"/>
                         		<input type="button" onclick="javascript:back()"  value="<emp:message key='wxgl_button_8' defVal='返回' fileName='wxgl'/>" class="btnClass6" style="margin-right:120px"/>
                         	</td>
                         </tr>
                  </thead>
                </table>
                </div>
                </form>
                </div>
			</div>
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
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath %>/wxcommon/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script language="javascript"  src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
  	<script type="text/javascript" src="<%=commonPath %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<%--<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?v=<%=StaticValue.OTT_VERSION %>"></script> --%>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=<%=ak %>"></script>
	<script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/baiduMap.js?v=<%=System.currentTimeMillis()%>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/addupdateLbs.js?v=<%=WXStaticValue.OTT_VERSION %> "></script>
	<%-- 逆地址解析 --%>
	<%-- <script type="text/javascript" id="addressResolution" src=""></script> --%>

	<script>
			$('#account').isSearchSelect({'width':'322','isInput':false,'zindex':0});	
						
	</script>			
	
	</body>
</html>








