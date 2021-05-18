<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.commons.beanutils.DynaBean" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();  //:  /p_xtgl
	String langName = (String)session.getAttribute("emp_lang");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  //:  http://localhost:8080/p_xtgl
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));  //:  /p_xtgl/xtgl/auditpro
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));  //:  /p_xtgl/xtgl
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));  //:  /p_xtgl
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	//分页对象	
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("auditpro");
	
	@ SuppressWarnings("unchecked")
	List<DynaBean> beanList = (List<DynaBean>) request.getAttribute("beanList");
	//存放的是key  F_ID value 范围信息
	@ SuppressWarnings("unchecked")
	LinkedHashMap<Long,String> flowTypeMap = (LinkedHashMap<Long,String>) request.getAttribute("flowTypeMap");
	//存放的是条件
	@ SuppressWarnings("unchecked")
	LinkedHashMap<Long,String> conditionMap = (LinkedHashMap<Long,String>) request.getAttribute("conditionMap");
	
	String lguserid = (String)request.getAttribute("lguserid");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	int corpType = StaticValue.getCORPTYPE();
%>
<html>
	<head>
		<title></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/aud_auditpro.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/xtgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	</head>
	<body id="aud_auditpro">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="aud_auditpro.htm?method=find" method="post"
					id="pageForm">
					<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
					<input id="pathUrl" value="<%=path%>" type="hidden" />
					<input id="iPath" value="<%=iPath%>" type="hidden" />
					<%-- 显示信息--%>
					<div id="singlecomment" class="singlecomment">
						<div id="msg" class="msg"></div>
					</div>
					
					
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<%if(btnMap.get(menuCode+"-1")!=null) { %>
								<a id="add" onclick="javascript:addAuditPro()"><emp:message key="xtgl_spgl_shlcgl_xj" defVal="新建" fileName="xtgl"/></a>
						<%} %>
					</div>
					<div id="condition">
						<table>
							<tr>
									<td>
										<span><emp:message key="xtgl_spgl_shlcgl_lcmc_mh" defVal="流程名称：" fileName="xtgl"/></span>
									</td>
									<td>
										<label>
											<input type="text" name="flowtask" id="flowtask" class="flowtask" 
												value="<%=conditionMap.get("flowtask")==null?"":conditionMap.get("flowtask") %>"/>
										</label>
									</td>
									<td>
										<span><emp:message key="xtgl_spgl_shlcgl_shfw_mh" defVal="审核范围：" fileName="xtgl"/></span>
									</td>
									<td>
										<% 
											String [] menu_num=StaticValue.getMenu_num().toString().split(",");
							    			LinkedHashMap<String,String> menMap=new LinkedHashMap<String,String>();
							    			if(menu_num != null)
							    			{
								    			for(int i=0;i<menu_num.length;i++)
								    			{
								    				menMap.put(menu_num[i],"");
								    			}
							    			}
										
										%>
									
										<select name="flowtype" id="flowtype" >
												<option value=""><emp:message key="xtgl_spgl_shlcgl_qb" defVal="全部" fileName="xtgl"/></option>
												<option value="1" <%=conditionMap.get("flowtype")!=null && conditionMap.get("flowtype").equals("1")?"selected":""%>>
														<emp:message key="xtgl_spgl_shlcgl_dxfs" defVal="短信发送" fileName="xtgl"/>
												</option>
												<% 
												if(menMap.containsKey("4"))
													{
												%>
												<option value="2" <%=conditionMap.get("flowtype")!=null && conditionMap.get("flowtype").equals("2")?"selected":""%>>
														<emp:message key="xtgl_spgl_shlcgl_cxfs" defVal="彩信发送" fileName="xtgl"/>
												</option>
												<% 
													}
												%>
												<option value="3" <%=conditionMap.get("flowtype")!=null && conditionMap.get("flowtype").equals("3")?"selected":""%>>
														<emp:message key="xtgl_spgl_shlcgl_dxmb" defVal="短信模板" fileName="xtgl"/>
												</option>
												<% 
												if(menMap.containsKey("4"))
													{
												%>
												<option value="4" <%=conditionMap.get("flowtype")!=null && conditionMap.get("flowtype").equals("4")?"selected":""%>>
														<emp:message key="xtgl_spgl_shlcgl_cxmb" defVal="彩信模板" fileName="xtgl"/>
												</option>
												<% 
													}
												if(menMap.containsKey("14"))
													{
												%>
												<option value="6" <%=conditionMap.get("flowtype")!=null && conditionMap.get("flowtype").equals("6")?"selected":""%>>
														<emp:message key="xtgl_spgl_shlcgl_wxmb" defVal="网讯模板" fileName="xtgl"/>
												</option>
												<% 
													}
												%>
										</select>
									</td>
										<td>
										<span><emp:message key="xtgl_spgl_shlcgl_cjr_mh" defVal="创建人：" fileName="xtgl"/></span>
									</td>
									<td>
										<label>
											<input type="text" name="flowname" id="flowname" class="flowname"
												value="<%=conditionMap.get("flowname")==null?"":conditionMap.get("flowname") %>"/>
										</label>
									</td>
										<td class="tdSer">
										<center><a id="search"></a></center>
									</td>
								</tr>
						</table>
					</div>
					<table id="content">
							<thead>
					 			<tr>
								   <th class="th1" >
										<emp:message key="xtgl_spgl_shlcgl_xh" defVal="序号" fileName="xtgl"/>
									</th>
									<th class="th2">
										<emp:message key="xtgl_spgl_shlcgl_shlcmc" defVal="审核流程名称" fileName="xtgl"/>
									</th>
									<th class="th3">
									          <emp:message key="xtgl_spgl_shlcgl_shjb" defVal="审核级别" fileName="xtgl"/>
									</th>
									<th class="th4">
										<emp:message key="xtgl_spgl_shlcgl_shfw" defVal="审核范围" fileName="xtgl"/>
									</th>
									 <th class="th5">
										<emp:message key="xtgl_spgl_shlcgl_bshdx" defVal="被审核对象" fileName="xtgl"/>
									</th>
									<th class="th6">
										<emp:message key="xtgl_spgl_shlcgl_cjr" defVal="创建人" fileName="xtgl"/>
									</th>
									<th class="th7">
										<emp:message key="xtgl_spgl_shlcgl_zt" defVal="状态" fileName="xtgl"/>
									</th>
									<th class="th8">
									    <emp:message key="xtgl_spgl_shlcgl_cz" defVal="操作" fileName="xtgl"/>
									</th>
									<th class="th9">
										<emp:message key="xtgl_spgl_shlcgl_bz" defVal="备注" fileName="xtgl"/>
									</th>
						        </tr>
							</thead>
							<tbody>
										<%
											if(beanList != null && beanList.size()>0)
											{
												for(int i=0;i<beanList.size();i++)
												{
													DynaBean bean = beanList.get(i); 
											%>
											<tr>
												<td>
													<%=bean.get("f_id") %>
												</td>
												<td>
													<label>
																<%
																	if(bean.get("f_task") != null && !"".equals(bean.get("f_task"))  && !"null".equals(bean.get("f_task"))){
																		String flowtaskname=(String)bean.get("f_task");
																		%>
																			<a href="javascript:getAuditProMsg('<%=bean.get("f_id")%>')"><xmp><%=StringEscapeUtils.unescapeHtml(flowtaskname)%></xmp></a>
																		<% 																	
																		}else{
																			%>
																			<a href="javascript:getAuditProMsg('<%=bean.get("f_id")%>')">-</a>
																		<% 
																		}
																%>
													</label>
												</td>
												<td>
													<%=bean.get("r_levelamount") %>
												</td>
												<td class="textalign" >
													<%
														if(flowTypeMap != null && flowTypeMap.size()>0){
															String infotype = flowTypeMap.get(Long.parseLong(bean.get("f_id").toString()));
															if(infotype != null && !"".equals(infotype)){
																String type = infotype.substring(0,infotype.length()-1);
																out.print(type);
															}else{
																out.print("-");
															}
														}
													%>
												</td>
												<td>
													<a href="javascript:installAuditObj('<%=String.valueOf(bean.get("f_task")).replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("'","\\\\\'").replaceAll("\"","&quot;")%>','<%=bean.get("f_id")%>')"><emp:message key="xtgl_spgl_shlcgl_sz" defVal="设置" fileName="xtgl"/></a>
												</td>
												<td class="textalign" >
													<%=String.valueOf(bean.get("name")).replaceAll("<","&lt;").replaceAll(">","&gt;") %>
												</td>
												<td>
													<center>
														<%
														String flowState= bean.get("flowstate")!=null?bean.get("flowstate").toString():"1";
														if("1".equals(flowState))
														{
														%>
															<select  name="bkwState<%=bean.get("f_id") %>" id="bkwState<%=bean.get("f_id") %>"  class="input_bd" onchange="javascript:changestate('<%=bean.get("f_id") %>')">
													          <option value="1" selected="selected"><emp:message key="xtgl_spgl_shlcgl_yqy" defVal="已启用" fileName="xtgl"/></option>
													          <option value="2" ><emp:message key="xtgl_spgl_shlcgl_ty" defVal="停用" fileName="xtgl"/></option>
													        </select>
														<%		
															}else
															{
														%>
															<select  name="bkwState<%=bean.get("f_id") %>" id="bkwState<%=bean.get("f_id") %>"  class="input_bd" onchange="javascript:changestate('<%=bean.get("f_id") %>')">
													           <option value="2" selected="selected"><emp:message key="xtgl_spgl_shlcgl_yty" defVal="已停用" fileName="xtgl"/></option>
													           <option value="1" ><emp:message key="xtgl_spgl_shlcgl_qy" defVal="启用" fileName="xtgl"/></option>
													        </select>
														<%		
															}
														 %>									
													</center>	
												</td>
												<td>
													
														<%  
															if(btnMap.get(menuCode+"-3")!=null){ %>
																<a href="javascript:updateAuditPro('<%=bean.get("f_id")%>')"><emp:message key="xtgl_spgl_shlcgl_xg" defVal="修改" fileName="xtgl"/></a>
														<%
															} else{
																out.print("-");
															}
														%>
														 &nbsp; &nbsp; &nbsp;
														<%  
															if(btnMap.get(menuCode+"-2")!=null)  {  %>
																<a onclick="javascript:deleteAuditPro('<%=bean.get("f_id")%>')"><emp:message key="xtgl_spgl_shlcgl_sc" defVal="删除" fileName="xtgl"/></a>
														<%
															} else{
																out.print("-");
															}
														%>
												</td>
												<td>
													<label>
																<%
																	if(bean.get("comments") != null && !"".equals(bean.get("comments"))){
																		
																			String comments = (String)bean.get("comments");
																			comments=StringEscapeUtils.unescapeHtml(comments);
																		%>
																				<% 
																					if(comments !=null && !"".equals(comments) && comments.length()>7){
																						out.print("<a onclick='javascript:showcommentdialog(this)' class='comments_cursor'><label class='comments_display'><xmp>"+comments+"</xmp></label>"+comments.substring(0,7)+"...</a>");
																					}else if(comments !=null && !"".equals(comments) && comments.length()>0){
																						out.print("<a onclick='javascript:showcommentdialog(this)' class='comments_cursor'><label class='comments_display'><xmp>"+comments+"</xmp></label>"+comments+"</a>");
																					}else{
																						out.print("-");
																					}
																				%>
																		<% 	
																	}else{
																		out.print("-");
																	}
																%>
													</label>
												</td>
												</tr>
											<%
											 	}
											 }else{
												 %>
													 <tr><td align="center" colspan="9"><emp:message key="xtgl_spgl_shlcgl_wjl" defVal="无记录" fileName="xtgl"/></td></tr>
												 <% 
											 }
											%>
							</tbody>
						<tfoot>
							<tr>
								<td colspan="9">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					</form>
			</div>
			<%} %>
			<%-- 内容结束 --%>
			<div id="auditObj" title="<emp:message key='xtgl_spgl_shlcgl_szbshdx' defVal='设置被审核对象' fileName='xtgl'/>" class="auditObj">
					<center>
					<iframe id="flowFrame" name="flowFrame" src=""  attrid="" class="flowFrame" marginwidth="0" scrolling="no" frameborder="no"></iframe>
					</center>
					<table>
						<tr>
							<td class="auditObj_td" align="center">
								<input type="button"  value="<emp:message key='xtgl_spgl_shlcgl_qd' defVal='确定' fileName='xtgl'/>" id="updateinstallbtn" class="btnClass5 mr23" onclick="javascript:updateinstallObj();" />
								<input type="button"  value=" <emp:message key='xtgl_spgl_shlcgl_qx' defVal='取消' fileName='xtgl'/>" class="btnClass6" onclick="javascript:closeInstall();" />
								<br/>
							</td>
						</tr>
					</table>
			</div>
			
			
			<div id="auditproFlow"  class="auditproFlow">
				<div>
					<center>
							<table class="auditproFlow_table" align="center" >
								<tr>
									<td class="auditproFlow_td"> 
											<emp:message key="xtgl_spgl_shlcgl_lcmc_mh" defVal="流程名称：" fileName="xtgl"/>
											<label id="aud_flowtask"></label>
											<label class="auditproFlow_label"><emp:message key="xtgl_spgl_shlcgl_cjr_mh" defVal="创建人：" fileName="xtgl"/></label>
											<label id="aud_flowuser"></label>
											<label class="auditproFlow_label"><emp:message key="xtgl_spgl_shlcgl_shjb_mh" defVal="审核级别：" fileName="xtgl"/></label>
											<label id="aud_flowlevel"></label>
									</td>
								</tr>
							</table>
						</center>	
					</div>
					<div class="recordTableDiv" id="recordTableDiv" align="center">
						<table  id="recordTable" class="recordTable">
						</table>
					</div>
				
			</div>
            <%--修改 提示弹出框--%>
            <div id="updateTip">
                <div class="warning-icon"></div>
                <div class="content-tip"></div>
                <div class="audit-list">
                    <a><emp:message key="xtgl_spgl_shlcgl_cksplc" defVal="查看审批流程" fileName="xtgl"/></a>
                </div>
            </div>
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/xtgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=commonPath %>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=iPath%>/js/auditpro.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#hiddenValueDiv");
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
			$("#auditObj").dialog({
				autoOpen: false,
				height:500,
				width: 530,
				resizable:false,
				modal: true
			});

			$("#auditproFlow").dialog({
				autoOpen: false,
				modal:true,
				title:'&nbsp;&nbsp;'+getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_70"), 
				width:620,
				height: 'auto',
				minHeight:180,
				maxHeight:850,
				closeOnEscape: false,
				resizable:false,
				open:function(){
				},
				close:function(){
				}
			});	
			$("#singlecomment").dialog({
				autoOpen: false,
				height:250,
				width: 250,
				modal: true,
				close:function(){
				}
			});

            $("#updateTip").dialog({
                autoOpen: false,
                height:150,
                width: 480,
                oheight:150,
                resizable:false,
                modal: true,
                open:function(){
                },
                close:function(){
                }
            });

			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$("#search").click(function(){submitForm();});
			$("#content select").empSelect({width:80});	
			$('#content select').each(function(){
				$(this).next().hide();
				$(this).before('<div class="selectBefore">'+$(this).find('option:selected').text()+'</div>');
		  });
		});
	</script>
	</body>
</html>
