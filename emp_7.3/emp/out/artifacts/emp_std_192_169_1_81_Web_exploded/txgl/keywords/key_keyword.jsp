<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.entity.keywords.LfKeywords"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	//分页对象	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	//查询条件
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
	@ SuppressWarnings("unchecked")
	List<LfKeywords> kwList = (List<LfKeywords>)request.getAttribute("kwList");
	@ SuppressWarnings("unchecked")
	Map<Long, String> keyIdMap = (Map<Long, String>)request.getAttribute("keyIdMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("keywordSvt");
	
	String findResult= (String)request.getAttribute("findresult");
	String kwcount = (String)session.getAttribute("kwcount");	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String CurrCorpcode= (String)request.getAttribute("lgcorpcode");
	String kwStype = (String)request.getAttribute("kwStype");
	
	int corpType = StaticValue.getCORPTYPE();
	String txglFrame = skin.replace(commonPath, inheritPath);
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	//修改关键字
	String xggjz = MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_xggjz", request);
    //确定
    String qd = MessageUtils.extractMessage("common", "common_confirm", request);
    //取消
	String qx = MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_qx", request);
	//新建关键字
	String xjgjz = MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_xjgjz", request);
	//导入关键字
	String drgjz = MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_drgjz", request);
	//关键字
	String gjz = MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_gjz", request);
    if(gjz!=null&&gjz.length()>1){
    	gjz = gjz.substring(0,gjz.length()-1);
    }
	//状态
	String zt = MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_zt", request);
    if(zt!=null&&zt.length()>1){
    	zt = zt.substring(0,zt.length()-1);
    }
	//类型
	String lx = MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_lx", request);
    if(lx!=null&&lx.length()>1){
    	lx = lx.substring(0,lx.length()-1);
    }
    
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/file.css?V=<%=StaticValue.getJspImpVersion() %>" />
			<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/key_keyword.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="key_keyword">
		<div id="container" class="container">
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<div id="editKey" title="<%=xggjz %>" class="editKey">
			<div class="editKey_down_div"></div>
					<center>
					<table>
						<tr><td align="center"><emp:message key="txgl_wghdpz_gjzsz_gjz" defVal="关键字：" fileName="txgl"></emp:message><input type="text" id="bkw" value=""  class="input_bd bkw"/>
								<input type="hidden" id="fkw" value=""/>
								<input type="hidden" id="fkwid" value=""/>
								<input type="hidden" id="keyId" value=""/>
							</td>
						</tr>
						<tr>
							<td  class="zhu zhumh_td" align="left">
								<emp:message key="txgl_wgqdpz_spzhczhs_z" defVal="注：" fileName="txgl"></emp:message>
								<p>
									<emp:message key="txgl_wghdpz_gjzsz_zgjzcdbndy" defVal="关键字长度不能大于10。" fileName="txgl"></emp:message>
								</p>
								<p>
									<emp:message key="txgl_wghdpz_gjzsz_bnbhkhz" defVal="不能包含括号中" fileName="txgl"></emp:message>(#=$%@*<>\/^?'_&,")<emp:message key="txgl_wghdpz_gjzsz_zxzf" defVal="这些字符！" fileName="txgl"></emp:message>
								</p>
							</td>
					    </tr>
					</table>
					</center>
				<div class="qd_div">
				    <center>
		    		<input id="bt1" class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="javascript:btok()"/>
				    <input id="bt2" onclick="javascript:btcancel();" class="btnClass6" type="button" value="<%=qx %>" />
				    <br/>
				    </center>
		    	</div>
			</div>
			<div id="addKey" title="<%=xjgjz %>" class="addKey">
				<div class="gjz_div">
					<center>
					<table>
  					  <tr>
   				 		 <td align="center"><emp:message key="txgl_wghdpz_gjzsz_gjz" defVal="关键字：" fileName="txgl"></emp:message>
   						 	<input type="text"   name="keywoed" id="keywored"
   						 	 maxlength="10" onkeypress="if(event.keyCode==13) return false;" class="input_bd keywored"/> 
   						 	 <input type="hidden" id="kwState" name="kwState" value="1"/>   	
   	   					 </td>   	   					 	
 					  </tr>
 					 <tr>
 					  <td  class="zhu zhs_z_td" align="left">
					  <emp:message key="txgl_wgqdpz_spzhczhs_z" defVal="注：" fileName="txgl"></emp:message>
 					  <p>
						  <emp:message key="txgl_wghdpz_gjzsz_zgjzcdbndy" defVal="关键字长度不能大于10。" fileName="txgl"></emp:message>
					  </p>
 					  <p>
						  <emp:message key="txgl_wghdpz_gjzsz_bnbhkhz" defVal="不能包含括号中" fileName="txgl"></emp:message>(#=$%@*<>\/^?'_&,")<emp:message key="txgl_wghdpz_gjzsz_zxzf" defVal="这些字符！" fileName="txgl"></emp:message>
					  </p>
					  </td>
 					  </tr>
					</table>
					</center>
				</div>
				<div class="kwok_div">
				    <center>
		    		<input id="kwok" class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="javascript:doClick()"/>
				    <input id="kwc" onclick="javascript:btcancel();" class="btnClass6" type="button" value="<%=qx %>" />
				    <br/>
				    </center>
		    	</div>
			</div>
			<div id="uploadKey" title="<%=drgjz %>" class="uploadKey">
				<div class="uploadForm_div">
					<center>
					<form id="uploadForm" action="key_keywordSvt.htm?method=update" method="post" enctype="multipart/form-data">
					<div id="kwparams1" class="hidden"></div>
					<table class="xzwj_table" >
  					  <tr class="xzwj_tr">
	   				 	 <td><span class="xzwj_span"><emp:message key="txgl_wghdpz_dxhmd_xzwj" defVal="选择文件：" fileName="txgl"></emp:message></span></td>
	   					 <td>
	                         <div>
		                        <a href="javascript:;" class="file"><emp:message key='wxgl_button_14' defVal='浏览' fileName='wxgl'/>
									<input type="file" name="numfile" id="numfile">
								</a>
		                        <p id="filename"></p>
	                         </div> 
	                      </td>
	 				 </tr>
 					  <tr> 					  
 					  <td colspan="3" align="left" class="zhu spzhczhs_z_td"  >
					  <emp:message key="txgl_wgqdpz_spzhczhs_z" defVal="注：" fileName="txgl"></emp:message>
 					  <p><emp:message key="txgl_wghdpz_gjzsz_zpltjzzcscwd" defVal="1.批量添加只支持上传.txt文档。" fileName="txgl"></emp:message></p>
 					  <p><emp:message key="txgl_wghdpz_gjzsz_wdnrdgswmggjzwyh" defVal="2.文档内容的格式为：每个过滤关键字为一行。" fileName="txgl"></emp:message></p>
 					  <p><emp:message key="txgl_wghdpz_gjzsz_gjzcddydbjtj" defVal="3.关键字长度大于10的将不添加。" fileName="txgl"></emp:message></p>
 					  <p><emp:message key="txgl_wghdpz_gjzsz_bhkhzzxzfdbtj" defVal="4.包含括号中(#=$%@*<>\/^?'_&,\")这些字符的不添加。" fileName="txgl"></emp:message></p>
 					  <p><emp:message key="txgl_wghdpz_gjzsz_gjzslsxwgcg" defVal="5.关键字数量上限为5000个，超过5000个则不能添加。" fileName="txgl"></emp:message></p>
	 				  </td>
 					  </tr>
					</table>
					</form>
					</center>
				</div>
				<div class="kwsok_div">
				    <center>
		    		<input id="kwsok" class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="javascript:checkUpload()"/>
				    <input id="kwsc" onclick="javascript:btcancel();" class="btnClass6" type="button" value="<%=qx %>" />
				    <br/>
				    </center>
		    	</div>
			</div>
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="key_keywordSvt.htm?method=find" method="post"
					id="pageForm">
					<div id="kwparams" class="hidden"></div>
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1")!=null) { %>
						<a id="add" onclick="javascript:doadd()"><emp:message key="txgl_wgqdpz_qyhdgl_xj" defVal="新建" fileName="txgl"></emp:message></a>
						<a id="upload" onclick="javascript:doupload()"><emp:message key="txgl_wghdpz_gjzsz_dr" defVal="导入" fileName="txgl"></emp:message></a>
						<%} %>
						<%if(btnMap.get(menuCode+"-2")!=null) { %>
						<a id="delete" onclick="javascript:delKw()"><emp:message key="txgl_wghdpz_gjzsz_sc" defVal="删除" fileName="txgl"></emp:message></a>
						<%} %>
					</div>
					<div id="condition">
						<table>
							<tr>
											<td>
												<span><emp:message key="txgl_wghdpz_gjzsz_gjz" defVal="关键字：" fileName="txgl"></emp:message></span>
											</td>
											<td>
												<label>
													<input type="text" name="keyWord" id="keyWord" class="keyWord"
														value="<%=conditionMap.get("keyWord&like")==null?"":conditionMap.get("keyWord&like")%>"/>
												</label>
											</td>
											<td>
												<span><emp:message key="txgl_wghdpz_gjzsz_zt" defVal="状态：" fileName="txgl"></emp:message></span>
											</td>
											<td>
												<label>
													<select name="keyState" id="keyState" class="keyState" isInput="false">
														<option value=""><emp:message key="txgl_wghdpz_gjzsz_qb" defVal="全部" fileName="txgl"></emp:message></option>
														<option value="1" 
															<%=conditionMap.get("kwState")!=null &&
																conditionMap.get("kwState").equals("1")?"selected":""%>>
															<emp:message key="txgl_wghdpz_gjzsz_qy" defVal="启用" fileName="txgl"></emp:message>
														</option>
														<option value="0" 
															<%=conditionMap.get("kwState")!=null &&
																conditionMap.get("kwState").equals("0")?"selected":""%>>
															<emp:message key="txgl_wghdpz_gjzsz_ty" defVal="停用" fileName="txgl"></emp:message>
														</option>
													</select>
												</label>
											</td>
										
											<% if(!"100000".equals(CurrCorpcode) && corpType==1){ %>
											<td><span><emp:message key="txgl_wghdpz_gjzsz_lx" defVal="类型：" fileName="txgl"></emp:message></span></td>
											<td>
											  	<label>
													<select name="keyStype" id="keyState2" class="keyState2">
														<option value=""><emp:message key="txgl_wghdpz_gjzsz_qb" defVal="全部" fileName="txgl"></emp:message></option>
														<option value="00" 
															<%=kwStype!=null &&	kwStype.equals("00")?"selected":""%>>
															<emp:message key="txgl_wghdpz_gjzsz_ttj" defVal="系统级" fileName="txgl"></emp:message>
														</option>
														<option value="01" 
															<%=kwStype!=null &&	kwStype.equals("01")?"selected":""%>>
															<emp:message key="txgl_wghdpz_gjzsz_bqy" defVal="本企业" fileName="txgl"></emp:message>
														</option>
													</select>
												</label>
											</td>
											<%} %>
												<td class="tdSer">
												<center><a id="search"></a></center>
											</td>
										</tr>
						</table>
					</div>
					<table id="content">
					<thead>
					 <tr>
							        <th>
										<input type="checkbox" name="dels" value=""
											onclick="checkAlls(this,'delKwId')" />
									</th>
									 <th>
										<%=gjz %>
									</th>
									<%if(btnMap.get(menuCode+"-3")!=null) { %>
									<th class="zt_th">
										<%=zt %>
									</th>
									<%}
									if(corpType == 1){ %>
									<th>
									    <%=lx %>
									</th>
									<%}if(btnMap.get(menuCode+"-3")!=null || btnMap.get(menuCode+"-2")!=null) { %>
									<th colspan=<%=((btnMap.get(menuCode+"-3")!=null && btnMap.get(menuCode+"-2")!=null))?"2":"1" %>>
										<emp:message key="txgl_wghdpz_gjzsz_cz" defVal="操作" fileName="txgl"></emp:message>
									</th>
									<%} %>
						        </tr>
							</thead>
							<tbody>
							<%
							if (kwList != null && kwList.size() > 0)
							{
								String keyId = "";
								int kindex = 0;
								for(LfKeywords keyword : kwList)
								{
									kindex ++;
									keyId = keyIdMap.get(keyword.getKwId());
							 %>
								<tr>
									<td>
										<input type="checkbox" name="delKwId" value="<%=keyId %>" />
									</td>
									<td class="textalign">
										<label><xmp><%=keyword.getKeyWord() %></xmp></label>
									</td>
									<%if(btnMap.get(menuCode+"-3")!=null) { 
									if(CurrCorpcode.equals(keyword.getCorpCode())){%>
									<td class="ztalign">
									<center>
										<%
										if(keyword.getKwState()-1==0)
										{
										%>
											<select  name="bkwState<%=keyword.getKwId() %>" data="<%=keyword.getKwId() %>" keyid="<%=keyId %>" id="bkwState<%=keyword.getKwId() %>"  class="input_bd" onchange="javascript:changestate('<%=keyword.getKwId() %>','<%=keyId%>')">
									          <option value="1" selected="selected"><emp:message key="txgl_wghdpz_gjzsz_yqy" defVal="已启用" fileName="txgl"></emp:message></option>
									          <option value="0" ><emp:message key="txgl_wghdpz_gjzsz_ty" defVal="停用" fileName="txgl"></emp:message></option>
									        </select>
										<%		
											}else if(keyword.getKwState()-0==0)
											{
										%>
										   <select  name="bkwState<%=keyword.getKwId() %>" data="<%=keyword.getKwId() %>" keyid="<%=keyId %>" id="bkwState<%=keyword.getKwId() %>"  class="input_bd" onchange="javascript:changestate('<%=keyword.getKwId() %>','<%=keyId%>')">
									          <option value="0" selected="selected"><emp:message key="txgl_wghdpz_gjzsz_yty" defVal="已停用" fileName="txgl"></emp:message></option>
									          <option value="1" ><emp:message key="txgl_wghdpz_gjzsz_qy" defVal="启用" fileName="txgl"></emp:message></option>									          
									        </select>
										<%		
											}
										 %>									
									</center>									

									<input type="hidden"  name="keyName" id="keyName" value="<%=keyword.getKeyWord() %>"/>
									</td>
									<%}else{ %>
									<td class="ztalign">
								   		<%
										if(keyword.getKwState()-1==0)
										{
											//out.print("已启用");	
											out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_yqy", request));	
										}else if(keyword.getKwState()-0==0)
										{
											//out.print("已停用");
											out.print(MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_yty", request));	
										}
									 %>									
									</td>
									<%}} %>
									<%if(corpType == 1){ %>
									<td>
									  <%if(keyword.getCorpCode().equals("100000")){ %>
									           <emp:message key="txgl_wghdpz_gjzsz_ttj" defVal="系统级" fileName="txgl"></emp:message>
									  <%}else{ %>
									          <emp:message key="txgl_wghdpz_gjzsz_bqy" defVal="本企业" fileName="txgl"></emp:message>
									  <%} %>
									</td>
									<%} %>							
									
									<%if(btnMap.get(menuCode+"-3")!=null) { 
									if(CurrCorpcode.equals(keyword.getCorpCode())){%>									
									<td>
										<a onclick="javascript:doEdit('<%=keyword.getKwId() %>','<%=keyword.getKeyWord() %>','<%=keyId %>')" ><emp:message key="txgl_wghdpz_gjzsz_xg" defVal="修改" fileName="txgl"></emp:message></a>
									</td>
									<%}else{ %>
									<td>-</td>
									<%}} %>
									
								    <%if(btnMap.get(menuCode+"-2")!=null) { %>
								    <td>
								    <% if(CurrCorpcode.equals(keyword.getCorpCode())){%>
									<a onclick="doCmd(<%=keyword.getKwId() %>, '<%=keyId %>')"><emp:message key="common_delete" defVal="删除" fileName="common"></emp:message></a>
									<%}else {%>
									-
									<%} %>
									</td>
									<%} %>
								</tr>
								
								<%} }else{%>
					<tr><td colspan="6" align="center"><emp:message key="txgl_wghdpz_gjzsz_wjl" defVal="无记录" fileName="txgl"></emp:message></td></tr>
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
					</form>
			</div>
			<%} %>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
		<div class="clear"></div>
		<input type="hidden" value="<%=StaticValue.getCORPTYPE() %>" id="corpType"/>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-b.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/keyword.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>	    

		<script type="text/javascript">
			$(document).ready(function() {
			    getLoginInfo("#kwparams");
			    getLoginInfo("#kwparams1");
			    var findresult="<%=findResult%>";
			    if(findresult=="-1")
			    {
			       //alert("加载页面失败,请检查网络是否正常!");	
			       alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_1"));	
			       return;			       
			    }
			    
                var kwcount1 = "<%=kwcount%>";
				var corpType = <%=StaticValue.getCORPTYPE()%>;
                if(kwcount1 != null && kwcount1 !="null" && parseInt(kwcount1)>=0)
			    {
			        //alert("共添加"+kwcount1+"条记录！");		
			         alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_2")+kwcount1+getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_3"));	    
			    }
			    else if(kwcount1 !=null && kwcount1 != "null" && parseInt(kwcount1)<0){
			    
				    //alert("共创建"+(0-eval(kwcount1))+"条记录,"+(corpType==1?"本企业":"")+"关键字记录已达上限5000条！");
				    alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_4")+(0-eval(kwcount1))+getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_5")+(corpType==1?getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_6"):"")+getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_7"));	
				} 
                <%session.removeAttribute("kwcount");%>
				
				$("#toggleDiv").toggle(function() {
					$("#condition").hide();
					$(this).addClass("collapse");
				}, function() {
					$("#condition").show();
					$(this).removeClass("collapse");
				});
				$("#content tbody tr").hover(function() {
					$(this).addClass("hoverColor");
					$(this).find('select').next().show().siblings().hide();
				}, function() {
					$(this).removeClass("hoverColor");
					var $select = $(this).find('select');
					$select.next().hide();
					$select.prev().show();
					
				});
				initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
				$("#search").click(function(){submitForm();});

				//$("#content select").empSelect({
					//width:80//显示的宽度，不配就是select框本身自定义的宽度
				//});	
				 $('#content select').isSearchOldSelect({'width':'100','isInput':false,'zindex':0},function(o){
					  changestate(o.obj.attr('data'),o.obj.attr('keyid'));
				  });
				  $('#content select').each(function(){
					    $(this).next().hide();
						$(this).before('<div>'+$(this).find('option:selected').text()+'</div>');
				  });
				  $('#keyState,#keyState2').isSearchSelect({'width':'182','isInput':false,'zindex':0});
				  
				  $("input:text").bind("blur keyup",function(){
					var value=$(this).val();
					if(/['"]/g.test(value)){
						value=value.replace(new RegExp("\"","gm"), "")
						.replace(new RegExp("\'","gm"), "");
						$(this).val(value);
					}
				});
				  
			});
	</script>
	</body>
</html>
