<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.client.LfCustField"%>
<%@page import="com.montnets.emp.entity.client.LfCustFieldValue"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.entity.client.LfClient"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.common.vo.LfCustFieldValueVo"%>
<%@page import="com.montnets.emp.common.constant.CommonVariables"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String shortInheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("addrBook");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

    String lguserid = (String)request.getAttribute("lguserid");
 	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
    @ SuppressWarnings("unchecked")
    Map<LfCustField, List<LfCustFieldValue>> map1=(Map<LfCustField, List<LfCustFieldValue>>)request.getAttribute("map1");
    @ SuppressWarnings("unchecked")
    Map<LfCustField, List<LfCustFieldValue>> map2=(Map<LfCustField, List<LfCustFieldValue>>)request.getAttribute("map2");
    LfClient client = (LfClient)request.getAttribute("client");
    @ SuppressWarnings("unchecked")
    List<DynaBean> beans = (List<DynaBean>)request.getAttribute("beans");
    
    PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");
    @ SuppressWarnings("unchecked")
    LinkedHashMap<Long, String> clientDepNameMap = (LinkedHashMap<Long, String>)request.getAttribute("clientDepNameMap");
    @ SuppressWarnings("unchecked")
    LinkedHashMap<Long, String> clientValueMap = (LinkedHashMap<Long, String>)request.getAttribute("clientValueMap");
    @ SuppressWarnings("unchecked")
    List<DynaBean> areaBeans = (List<DynaBean>)request.getAttribute("areaBeans");
    
    String conditionsql = (String)request.getAttribute("conditionsql");
    List <LfCustFieldValueVo> custFieldList = (ArrayList<LfCustFieldValueVo>)request.getAttribute("custFieldList");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    
 %>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css"/>
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin%>/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>		


		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/kfs_searchPage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_KeFuDuanXin.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body onload="pageLoad()" id="searchPage">
		<input type="hidden" id="pathUrl" value="<%=path %>" />
		<input type="hidden" id="conditionsql" value="<%=conditionsql %>" />
		<input type="hidden" id="totalcount" value="<%=pageInfo.getTotalRec() %>" />
		<div id="container" class="container">
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="" method="post" id="pageForm">
					<div class="dxkf_display_none" id="hiddenValueDiv"></div>
					<div class="buttons btx_line">
					    <div class="bb_line left" id="bb_line"></div>
						<div id="toggleDiv">
						</div>
					</div>
					<div id="condition">
					   
						<table id="condition_table">
							<tbody>
							    
								<tr><td colspan="6"><b><emp:message key="dxkf_ydkf_gjss_text_bjxx" defVal="基本信息" fileName="dxkf"></emp:message></b></td>
								<td class="tdSer" align="right" colspan="2">
								<div class="dxkf_div1">
								<a id="search" class="right"  onclick="javascript:doSearch1()"></a>
								<input type="button" name="button" id="reset" class="btnClass1 btn_small right" value="<emp:message key="dxkf_common_opt_chognzhi" defVal="重置" fileName="dxkf"></emp:message>" onclick="javascript:doReload()" >
								</div>
								
								
								</td>
								</tr>
								<tr>
									<td>
										<emp:message key="dxkf_ydkf_gjss_text_ssjg" defVal="所属机构" fileName="dxkf"></emp:message>：
									</td>
									<td width="180" class="condi_f_l">	
										<input type="hidden" name="depId" id="depId" value='<%=(client == null || client.getDepId()==null)?"":client.getDepId()%>'/>
										<% 
										String depNam = request.getParameter("depNam");
										if(null==depNam||"".equals(depNam)){
											depNam = MessageUtils.extractMessage("dxkf","dxkf_common_opt_qingxuanze",request) ;
										}
										%>
								        <div class="dxkf_div2">
										    <input id="depNam" onclick="javascript:showMenu('pageForm');"  
										    name="depNam" type="text" readonly value="<%=depNam %>" class="input_book1 input_bd treeInput"/>
								            <a id="ssdep" onclick="javascript:showMenu('pageForm');" 
								            ></a>
										</div>
										<div id="dropMenu">
												<div class="dxkf_div3">
													<input type="button" value="<emp:message key="dxkf_common_opt_queding" defVal="确定" fileName="dxkf"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK2();" />&nbsp;&nbsp;
													<input type="button" value="<emp:message key="dxkf_common_opt_qingkong" defVal="清空" fileName="dxkf"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect_dep2();" />
												</div>
												<div class="dxkf_div4"><font class="zhu"><emp:message key="dxkf_ydkf_gjss_text_deptandselect" defVal="注：请选择所属机构进行查询" fileName="dxkf"></emp:message></font></div>	
											<ul id="dropdownMenu" class="tree">
											</ul>
										</div> 	
									</td>
									<td>
											<emp:message key="dxkf_ydkf_gjss_text_clientnumber" defVal="客户号" fileName="dxkf"></emp:message>：
									</td>
									<td>	
									<input type="text" name="clientCode" id="clientCode" onkeyup="this.value=this.value.replace(/[']+/img,'')"  
											value='<%=(client == null || client.getClientCode()==null) ? "": client.getClientCode()%>' 
											maxlength="20" class="input_book input_bd" />
									</td>
									<td ><span><emp:message key="dxkf_common_text_name" defVal="姓名" fileName="dxkf"></emp:message>：</span></td>
									<td >
										<label>
										<input type="text" name="cName" id="cName" onkeyup="this.value=this.value.replace(/[']+/img,'')" 
												value='<%=(client == null || client.getName() == null) ? "" : client.getName()%>' 
												maxlength="20" class="input_book input_bd "/>
										</label>
									</td>
									<td ><span><emp:message key="dxkf_common_text_sex" defVal="性别" fileName="dxkf"></emp:message>：</span></td>
									<td >
										<label>
										<select name="sex" id="sex" class="input_book input_bd">
											<% 
												String sexindex = "";
												if(client != null){
													sexindex = String.valueOf(client.getSex());
												}
											%>
										
											<option value=""><emp:message key="dxkf_common_text_all" defVal="全部" fileName="dxkf"></emp:message></option>
											<option value="2" <%= sexindex.equals("2") ? "selected='selected'" : "" %>><emp:message key="dxkf_common_text_unknown" defVal="未知" fileName="dxkf"></emp:message></option>
											<option value="1" <%= sexindex.equals("1") ? "selected='selected'" : "" %>><emp:message key="dxkf_common_text_man" defVal="男" fileName="dxkf"></emp:message></option>
											<option value="0" <%= sexindex.equals("0") ? "selected='selected'" : "" %>><emp:message key="dxkf_common_text_woman" defVal="女" fileName="dxkf"></emp:message></option>
										</select>
										</label>
									</td>
								</tr>
								<tr>
									<td ><span><emp:message key="dxkf_common_text_phone" defVal="手机" fileName="dxkf"></emp:message>：</span></td>
									<td >
										<label>	
										<input type="text" name="mobile" id="mobile" onkeyup="javascript:phoneInputCtrl($(this))" 
												value='<%=client == null || client.getMobile()==null?"":client.getMobile() %>' 
												maxlength="21" class="input_book input_bd"/>
										</label>
									</td>
									<td ><span><emp:message key="dxkf_ydkf_gjss_text_area" defVal="区域" fileName="dxkf"></emp:message>：</span></td>
									<td >
										<label>	
											<select name="area" id="area" class="input_book input_bd">
												<option value=""><emp:message key="dxkf_common_text_all" defVal="全部" fileName="dxkf"></emp:message></option>
												<%
													if(areaBeans != null && areaBeans.size()>0){
														String area = "";
														if(client != null){
															area = client.getArea();
														}
														for(DynaBean be:areaBeans){
															Object o = be.get("area");
															if(o != null && !"".equals(o)){
																String areaname = String.valueOf(o);
																boolean flag = false;
																if(areaname.equals(area)){
																	flag = true;
																}
																%>
																
																	<option value="<%=areaname %>" <% if(flag){out.print("selected='selected'");}%> >
																		<%=areaname %>
																	</option>
																<%
															}
														}
													}
												%>
											</select>
										</label>
									</td>
									<td ><span><emp:message key="dxkf_ydkf_gjss_text_birthday" defVal="生日" fileName="dxkf"></emp:message>：</span></td>
										<td ><label>
											<%
												String birth1 = request.getParameter("birth1");
												String birth2 = request.getParameter("birth2");
												if(null==birth1||"".equals(birth1)){
													birth1 = "";
												}
												if(null==birth2||"".equals(birth2)){
													birth2 = "";
												}
											%>
											<input id="d1" type="text" value='<%=birth1%>' name="birth1"
													class="Wdate input_bd" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'d2\')||\'%yyyy-%MM-%dd\'}'})" 
														class="input_book">
										</label></td>
									<td ><span><emp:message key="dxkf_ydkf_gjss_text_zhi" defVal="至" fileName="dxkf"></emp:message></span></td>
									<td ><label>
											<input id="d2" type="text" value='<%=birth2%>' name="birth2"
												class="Wdate input_bd" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'d1\')}',maxDate:'%yyyy-%MM-%dd'})" 
												class="input_book">
										</label>
									</td>
						    	<%
						    		if((map1 != null && map1.size()>0) || (map2 != null && map2.size()> 0)){
						    	%>
						    		<tr><td colspan="8"><b><emp:message key="dxkf_ydkf_gjss_text_customerattributes" defVal="客户属性" fileName="dxkf"></emp:message></b></td></tr>
						    	<%	
						    		}
						    	%>
								<%              		
									if(map1 != null && map1.size()>0)                       		
									{ 
										int i=1;
										for (Iterator it =  map1.keySet().iterator();it.hasNext();)
										{
											if(i == 1 || i%2 == 1){
												%>
													<tr align="left" class="dxkf_span1">
												<%
											}
										    LfCustField key1 = (LfCustField)it.next();
										    List<LfCustFieldValue> value1=map1.get(key1);
									%>
											<td colspan="3">
												<%--<span><%=key1.getField_Name().replace("<","&lt;").replace(">","&gt;")%>：
												</span>--%>
												<%if("客户性质".equals(key1.getField_Name().replace("<","&lt;").replace(">","&gt;"))){%>
													<span>
													<emp:message key='dxkf_ydkf_khqzqf_text_khxz' defVal='客户性质：' fileName='dxkf'></emp:message>
													</span>
												<%}else{%>
													<span>
													<%=key1.getField_Name().replace("<","&lt;").replace(">","&gt;")%>：
													</span>
												<%}%>
													<label>
														<select name="<%=key1.getField_Ref()%>" id="<%=key1.getField_Name()%>" class="input_bd dxkf_select"
															>
															<option value=""><emp:message key='dxkf_common_opt_qingxuanze' defVal='请选择' fileName='dxkf'></emp:message></option>
															<%
																for(int s=0;s<value1.size();s++){
																	LfCustFieldValue fc=value1.get(s);
																	boolean isSelected = false;
																	for(LfCustFieldValueVo filevaluevo : custFieldList){
																		if(fc.getId().equals(filevaluevo.getId())){
																			isSelected = true;
																			break;
																		}
																	}
															%>
															<option value="<%=fc.getId()%>" <%= isSelected ? "selected='selected'" : "" %>>
																<%=fc.getField_Value().replace("<","&lt;").replace(">","&gt;")%>
															</option>
															<%} %>
														</select>
													</label>
											</td>
									<%		
												if( i > 0  && i%2 == 0){
													%>
														</tr>
													<%
												}
												i = i + 1;
											}
											i = i -1;
											if(i < 2 || i%2 < 2){
												%>
														<td colspan="<%=8-(i%2)*3 %>"></td>
													</tr>
												<%
											}
										}
									%>
									
									
									
									
									<%
									if(map2!=null && map2.size()>0){
								   	 	for(Iterator it =  map2.keySet().iterator();it.hasNext();)
										{
										    LfCustField key2 = (LfCustField)it.next();
										    List<LfCustFieldValue> value2=map2.get(key2);
									%>
											<tr align="left" class="dxkf_span1">
												<td>
													<span><%=key2.getField_Name().replace("<","&lt;").replace(">","&gt;")%>：
													</span>
												</td>
												<td colspan="7">
													<%for(int sk=0;sk<value2.size();sk++)
													  { 
													      LfCustFieldValue fc=value2.get(sk);
													      boolean isSelected = false;
															for(LfCustFieldValueVo filevaluevo : custFieldList){
																if(fc.getId().equals(filevaluevo.getId())){
																	isSelected = true;
																	break;
																}
															}
													%>
														 <div id="check_div">
														 <input type="checkbox" style="padding-left: 0px!important;width: 14px!important;height: 14px!important;" class="dxkf_input" name="<%=key2.getField_Ref()%>" value="<%=fc.getId()%>"  <%= isSelected ? "checked='checked'" : "" %>/>&nbsp;<%=fc.getField_Value().replace("<","&lt;").replace(">","&gt;")%>
														 &nbsp;&nbsp;</div> 
													<%
													  }
													%>
												</td>
											</tr>
									<%
									  }
									}
									%>
									
									<tr align="right" class="dxkf_display_none">
									<td colspan="8" align="right" height="30px">
										<input type="button" name="button" id="doSearch" class="btnClass1 btn_small" value="<emp:message key="dxkf_common_opt_chaxun" defVal="查询" fileName="dxkf"></emp:message>" onclick="javascript:doSearch1()">
										
                                        <input type="hidden" name="fieldNames" value="">
									    <input type="button" name="button" id="reset" class="btnClass1 btn_small" value="<emp:message key="dxkf_common_opt_chongzhi" defVal="重置" fileName="dxkf"></emp:message>" onclick="javascript:doReload()" >
									   	
									    <input type="button" name="button" id="back" class="btnClass1 btn_small" value="<emp:message key="dxkf_common_opt_fanhui" defVal="返回" fileName="dxkf"></emp:message>"  onclick="javascript:window.parent.closeSearchBox();">
									</td>
									</tr>
							</tbody>
						</table>
					   
					</div>
            <div class="tx_line "></div>
				
				<div id="resultSet" class="mt10">
                    <table id="content">
                        <thead>
                        <tr>
                            <th>
                                <input type="checkbox" name="selectAll" id="selectAll">
                            </th>
                            <th>
                                 <emp:message key="dxkf_ydkf_gjss_text_ssjg" defVal="所属机构" fileName="dxkf"></emp:message>
                            </th>
                            <th>
                              	 <emp:message key="dxkf_ydkf_gjss_text_clientnumber" defVal="客户号" fileName="dxkf"></emp:message>
                            </th>
                            <th>
                               	 <emp:message key="dxkf_common_text_name" defVal="姓名" fileName="dxkf"></emp:message>
                            </th>
                            <th>
                                 <emp:message key="dxkf_ydkf_gjss_text_phonenumber" defVal="手机号码" fileName="dxkf"></emp:message>	
                            </th>
                            <th>
                                 <emp:message key="dxkf_common_text_sex" defVal="性别" fileName="dxkf"></emp:message>
                            </th>
                              <th>
                                 <emp:message key="dxkf_ydkf_gjss_text_area" defVal="区域" fileName="dxkf"></emp:message>
                            </th>
                            <th>
                               	 <emp:message key="dxkf_ydkf_gjss_text_birthday" defVal="生日" fileName="dxkf"></emp:message>
                            </th>
                            <th colspan="4">
                              	  <emp:message key="dxkf_ydkf_gjss_text_attributevalue" defVal="属性值" fileName="dxkf"></emp:message>	
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            if(beans != null && beans.size()>0){
                            	boolean isShow = btnMap.get(StaticValue.PHONE_LOOK_CODE) == null?true:false;  
                            	CommonVariables commonVariables = new CommonVariables();
                                for(DynaBean bean : beans){
                                	Long clientid = Long.valueOf(bean.get("client_id")+"");
                        %>
                        <tr>
                            <td>
                                <input type="checkbox" name="clientIds" value="<%= bean.get("client_id")%>" mobile="<%=bean.get("mobile")%>" onclick="javascript:onclickClient(this);checkSelected();">
                            </td>
                            <td class="textalign">
            	 				<%=clientDepNameMap.get(clientid) == null?"":clientDepNameMap.get(clientid)%>
                            </td>
                            <td class="textalign">
                               <%=bean.get("client_code")==null?"-":bean.get("client_code")%>
                            </td>
                            <td class="textalign">
                                <%=bean.get("name")%>
                            </td>

                            <td class="textalign">
                            <%
                            	if(isShow)
                           		{
                            		out.print(commonVariables.replacePhoneNumber(bean.get("mobile").toString()));
                           		}
                            	else
                            	{
                            		out.print(bean.get("mobile").toString());
                            	}
                            %>
                            </td>
                            <td class="center"> 
                            	 <% String sex = String.valueOf(bean.get("sex"));
                             	 if("1".equals(sex)) {
                             	     sex = MessageUtils.extractMessage("dxkf", "dxkf_common_text_man", request);
//                             		sex = "<emp:message key='' defVal='男' fileName='dxkf'></emp:message>";
                            	 } else if("0".equals(sex)){
                                     sex = MessageUtils.extractMessage("dxkf", "dxkf_common_text_woman", request);
//                             	     sex = "<emp:message key='dxkf_common_text_woman' defVal='女' fileName='dxkf'></emp:message>";
                            	 } else {
                                     sex = MessageUtils.extractMessage("dxkf", "dxkf_common_text_unknown", request);
//                             		sex = "<emp:message key='dxkf_common_text_unknown' defVal='未知' fileName='dxkf'></emp:message>";
                           	 	}%> 
	                        	 <%=sex %>
                            </td>
                            <td align="center">
                                <%
                                	Object object = bean.get("area");
                                	if(object== null || "".equals(object)){
                                		out.print("-");
                                	}else{
                                		out.print(String.valueOf(object));
                                	}
                                %>
                            </td>
                            <td class="textalign">
                                <%
                                    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
                                %>
                                <% if(bean.get("birthday")==null||"".equals(bean.get("birthday"))){ %>
                                	-
                                <% } else {%>
                                <%=formatter.format(bean.get("birthday"))%>
                                <% }%>
                            </td>
                            <td class="textalign">
                                <%=clientValueMap.get(clientid) == null?"":clientValueMap.get(clientid)%>
                            </td>
                        </tr>
                        <%
                            }
                        }else{
                        %>
                        <tr><td align="center" colspan="12"><emp:message key='dxkf_common_text_nodata' defVal='无记录' fileName='dxkf'></emp:message></td></tr>
                        <%} %>
                        </tbody>
                        <tfoot>
                        <tr height="40px;">
                            <td colspan="12">
                                <div id="pageInfo"></div>
                            </td>
                        </tr>
                        </tfoot>
                    </table>
                    
                </div>
                
            </form>
			</div>
			<div class="dxkf_div5">
                <input id="subSend" class="btnClass5 mr23 left" type="button" onclick="javascript:doOk1()" value="<emp:message key="dxkf_common_opt_queding" defVal="确定" fileName="dxkf"></emp:message>">
                <input id="qingkong" onclick="javascript:window.parent.closeSearchBox();" class="btnClass6 left" type="button" value="<emp:message key="dxkf_common_opt_fanhui" defVal="返回" fileName="dxkf"></emp:message>">
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
    
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=116"></script>				
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=116"></script>
	    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=116"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/dxkf_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=116"></script>
        <script src="<%=iPath %>/js/clientSend.js?V=116.1" type="text/javascript"></script>
        <script type="text/javascript" src="<%=commonPath%>/frame/frame4.0/js/dialogui.js"></script>
        <script type="text/javascript">
		/*机构选择弹出框js --start--*/
		var zTree1;
		var zTree2;
		var setting;
		var formName = "pageForm";
		setting = {
				async : true,
				//asyncUrl :"<%=path%>/kfs_sendClientSMS.htm?method=getClientSecondDepJson", //获取节点数据的URL地址
				asyncUrl:"",
				isSimpleData: true,
				rootPID : -1,
				treeNodeKey: "id",
				treeNodeParentKey: "pId",
				callback: {
					click: zTreeOnClick,
					asyncSuccess:function(event, treeId, treeNode, msg){
					if(!treeNode){	//判断是 顶级机构就展开,其余的收缩+	
						if(formName == "pageForm"){
							 var rootNode2 = zTree2.getNodeByParam("level", 0);
				             zTree2.expandNode(rootNode2, true, false);
						}			  
	     			}
					},
					beforeAsync:function(treeId,treeNode){
						if(formName == "pageForm"){
							zTree2.setting.asyncUrl="<%=request.getContextPath()%>/kfs_sendClientSMS.htm?method=getClientSecondDepJson&depId="+treeNode.id+"&lguserid=<%=lguserid%>";
						}
					}
				}
		};

		var zNodes =[];

		function showMenu(fname) {
			var isIE = (document.all) ? true : false;
       		 var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
       		if(isIE6){
			 	$("select ").hide();
			 	$("#sex ").show();
			 }
			 	
			$("form[name='"+fname+"']").find("#dropMenu").focus();
			var sortSel = $("form[name='"+fname+"']").find("#depNam");
			var sortOffset = $("form[name='"+fname+"']").find("#depNam").offset();
			$("form[name='"+fname+"']").find("#dropMenu").toggle();
			setFormName(fname);
			if(formName == "pageForm")
			{
				setting.asyncUrl="<%=request.getContextPath()%>/kfs_sendClientSMS.htm?method=getClientSecondDepJson"+"&lguserid=<%=lguserid%>";
				zTree2 = $("form[name='pageForm']").find("#dropdownMenu").zTree(setting, zNodes);
				$("form[name='uploadForm']").find("#dropMenu").hide();
			}
	
		}

		function hideMenu() {
			var isIE = (document.all) ? true : false;
       		 var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
       		if(isIE6){
			 	$("select").show();
			 }
			$("form[name='"+formName+"']").find("#dropMenu").hide();
		}
		
		function zTreeOnClick(event, treeId, treeNode) {
			if (treeNode.isParent) {
				if(formName == "pageForm"){
					zTree2.setting.asyncUrl="<%=request.getContextPath()%>/kfs_sendClientSMS.htm?method=getClientSecondDepJson&depId="+treeNode.id+"&lguserid=<%=lguserid%>";
				}
			}
			if (treeNode) {
				var sortObj = $("form[name='"+formName+"']").find("#depNam");
				sortObj.attr("value", treeNode.name);
				$("form[name='"+formName+"']").find("#depId").attr("value",treeNode.id);
				//hideMenu();
			}
		}
		function setFormName(name){
			formName = name;
		}
		function reloadTree() {
			hideMenu();
			zTree2 = $("form[name='pageForm']").find("#dropdownMenu").zTree(setting, zNodes);
		}

		function cleanSelect_dep2()
		{
			var checkNodes = zTree2.getCheckedNodes();
		    for(var i=0;i<checkNodes.length;i++){
		     checkNodes[i].checked=false;
		    }
		    zTree2.refresh();
		    $("#depNam").attr("value", getJsLocaleMessage('dxkf','dxkf_dxqf_page3_qingxuanze'));	
			$("#depId").attr("value","");	
		}
		
        /*机构选择弹出框js --end--*/

		//高级搜索-确认搜索
		function doSearch1(){
			 clearHiddenArea();
			 var form = $("form[name='pageForm']");
			 //高级搜索必须选择机构，很重要
			 var depId=$("form[name='pageForm']").find("#depId").val();
			 if(depId==""){
				 alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_choosedept'));
				 return;
			 }
			 var url = "<%=request.getContextPath()%>/kfs_sendClientSMS.htm?method=advancedSearch&lguserid=<%=lguserid%>"+"&lgcorpcode=<%=lgcorpcode%>";
			 var fieldNames = new Array();
			 $(form).find("select[name*=FIELD],input:checked[name*=FIELD]").each(function(index,element){
				 if(this.value!=null&&this.value!=""){
					 fieldNames.push(this.name);
				 }
			 });
			var fieldNameStr = $.unique(fieldNames).join(",");
            $(form).find("input[name='fieldNames']").attr("value",fieldNameStr);
            $("#pageForm").attr("action",url);
            document.forms['pageForm'].elements["pageIndex"].value =1;	// 回到第一页
            document.forms['pageForm'].submit();
		}

		//高级搜索-添加选中人员
		function doOk1(){	
			var manCount = 0;
			var status = $(window.parent.document).find("#selectAllStatusTemp").val();
			if(status==null||""==status||status=="false"){
   			    var tempStr = $(window.parent.document).find("#phoneStr12Temp").val();
   				var tempArray = new Array();
				tempArray = tempStr.split(",");
					
   				unique(tempArray);
   				manCount = tempArray.length;
   				
   				if(parseInt(manCount) < 1){
					// alert("您没有选择任何人员！");
					alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_nopepolechoose'));
					return;
			    }
			
   				$(window.parent.document).find("#phoneStr12").val(tempStr);
   			}else{
   			    var tempStr = $(window.parent.document).find("#unChioceUserIdsTemp").val();
   				var tempArray = new Array();
				tempArray = tempStr.split(",");
   				unique(tempArray);
   				var count = $("#totalcount").val() - tempArray.length;
   				manCount = (count > 0)? (count) : 0;
   				
   				if(parseInt(manCount) < 1){
					// alert("您没有选择任何人员！");
					alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page_nopepolechoose'));
					return;
			    }
			    
   				$(window.parent.document).find("#conditionsqlTemp").val($("#conditionsql").val());
   				$(window.parent.document).find("#selectAllStatus").val($(window.parent.document).find("#selectAllStatusTemp").val());
   				$(window.parent.document).find("#unChioceUserIds").val(tempStr);
   				
   			}
			
			$(window.parent.document).find("#ygtxl2").remove();
			/*高级搜索 详情 人*/
			$(window.parent.document).find("#vss").append("<tr class='div_bd' id='ygtxl2' style='background-color:#ffffff'><td style='border-left:0;border-right:0' align='center' name='ConR'  valign='middle'>"+getJsLocaleMessage("dxkf","dxkf_common_advancedSearch")+"</td><td style='border-left:0;border-right:0'  align='center' valign='middle' ><a onclick='javascript:showSearchBox();' style='cursor:pointer'><font style='color:#0e5ad1'>"+getJsLocaleMessage("common","common_details")+"(<label style='color:#0e5ad1' id='manCountTemp12'></label>"+getJsLocaleMessage("common","common_person")+")</font> </a></td><td  style='border-left:0;border-right:0' align='center' valign='middle' height='20px'><a  onclick='javascript:delAddr12()'><img border='0' src='<%=shortInheritPath%>/common/widget/zTreeStyle/img/delete.gif' style='cursor:pointer'></img></a></td></tr>");
            window.parent.closeSearchBox();
			$(window.parent.document).find("#manCountTemp12").html(manCount);
			$(window.parent.document).find("#selectAllStatus").val(status);
		}

		$(document).ready(function(){
			closeTreeFun(["dropMenu"]);
			reloadTree();
			//分页
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
            //高级搜索-全选中
            $("#selectAll").live("click",function(){
                $("input[name='clientIds']").attr("checked",$(this).attr("checked"));
                
                $(window.parent.document).find("#selectAllStatusTemp").val($(this).attr("checked"));
	
                var status = $(window.parent.document).find("#selectAllStatusTemp").val();
                if(status==null||""==status||status=="false"){
                	$(window.parent.document).find("#phoneStr12Temp").val('');     
                }else{
                	$(window.parent.document).find("#unChioceUserIdsTemp").val('');     
                }
            }); 
            $("#toggleDiv").toggle(function() {
				$("#condition,#bb_line,.tx_line").hide();
				
				$(this).addClass("collapse");
			}, function() {
				$("#condition,#bb_line,.tx_line").show();
				$(this).removeClass("collapse");
			});
		});

		function checkSelected(){
            if ($("input[name='clientIds']").length == $("input[name='clientIds']:checked").length) {
                $('#selectAll').attr('checked', true);
            } else {
                $('#selectAll').attr('checked', false);
            }
		}

		</script>
	</body>
</html>