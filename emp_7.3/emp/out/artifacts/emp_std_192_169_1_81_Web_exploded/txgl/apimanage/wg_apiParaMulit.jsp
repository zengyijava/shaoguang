<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.servmodule.txgl.biz.ViewParams" %>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.GwBasePara"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("apiPara");
	@ SuppressWarnings("unchecked")
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String txglFrame = skin.replace(commonPath, inheritPath);
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@include file="/common/common.jsp" %>
		<title>API接口参数管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<link
			href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link
			href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link
			href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>"
			rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet"
			href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/wg_apiParaMulit.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="wg_apiParaMulit">
		<div id="container" class="container">
			<input type="hidden" id="ecid" name="ecid" value=""/>
			<input type="hidden" id="pathUrl" value="<%=path %>"/>
			<input type="hidden" id="funtype" value=""/>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- 内容开始 --%>
			<form name="pageForm" action="wg_apiPara.htm?method=find" method="post" id="pageForm">
			<input type="hidden" id="lguserid" name="lguserid" value="abc"/>
			<div id="rContent" class="rContent">	
				<div class="buttons toggleDiv_up_div"  >
						<div id="toggleDiv"></div>
					</div>
					<div id="getloginUser" class="getloginUser">
					</div>
					<div id="condition">
						<table>
									<%
									LinkedHashMap<String, String> conditionMap =(LinkedHashMap<String,String>)request.getAttribute("conditionMap");
									%>
										<tr>
											<td >
												<emp:message key='txgl_apimanage_text_3' defVal='方法名称：' fileName='mwadmin'/>
											</td>
											<td>
												<label>
													<select  class="funName"  name="funName" id="funName" >
				                                  	<option value="" selected="selected"><emp:message key='txgl_apimanage_text_35' defVal='全部' fileName='mwadmin'/></option>
				                                   <option value="single_send" <%if(null!=conditionMap.get("funName")&&"single_send".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_4' defVal='单条发送' fileName='mwadmin'/>(single_send)
				                                    </option>
				                                     <option value="send_single" <%if(null!=conditionMap.get("funName")&&"send_single".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_4' defVal='单条发送' fileName='mwadmin'/>(send_single)
				                                    </option>
				                                    <option value="batch_send" <%if(null!=conditionMap.get("funName")&&"batch_send".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_5' defVal='相同内容群发' fileName='mwadmin'/>(batch_send)
				                                    </option>
				                                      <option value="send_batch" <%if(null!=conditionMap.get("funName")&&"send_batch".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_5' defVal='相同内容群发' fileName='mwadmin'/>(send_batch)
				                                    </option>
				                                    <option value="multi_send" <%if(null!=conditionMap.get("funName")&&"multi_send".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_6' defVal='个性化群发' fileName='mwadmin'/>(multi_send)
				                                    </option>
				                                    <option value="send_multi" <%if(null!=conditionMap.get("funName")&&"send_multi".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_6' defVal='个性化群发' fileName='mwadmin'/>(send_multi)
				                                    </option>
				                                    <option value="send_mixed" <%if(null!=conditionMap.get("funName")&&"send_mixed".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_6' defVal='个性化群发' fileName='mwadmin'/>(send_mixed)
				                                    </option>
				                                    <option value="template_send" <%if(null!=conditionMap.get("funName")&&"template_send".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_7' defVal='模板发送' fileName='mwadmin'/>(template_send)
				                                    </option>
				                                     <option value="send_template" <%if(null!=conditionMap.get("funName")&&"send_template".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_7' defVal='模板发送' fileName='mwadmin'/>(send_template)
				                                    </option>
				                                    <option value="get_mo"<%if(null!=conditionMap.get("funName")&&"get_mo".equals(conditionMap.get("funName"))){%> selected="selected" <%}%> >
				                                   		 <emp:message key='txgl_apimanage_text_8' defVal='获取上行' fileName='mwadmin'/>(get_mo)
				                                    </option>
				                                    <option value="get_rpt" <%if(null!=conditionMap.get("funName")&&"get_rpt".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_9' defVal='获取状态报告' fileName='mwadmin'/>(get_rpt)
				                                    </option>
				                                    <option value="get_balance" <%if(null!=conditionMap.get("funName")&&"get_balance".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_10' defVal='查询余额' fileName='mwadmin'/>(get_balance)
				                                    </option>
				                                     <option value="MO" <%if(null!=conditionMap.get("funName")&&"MO".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                     	<emp:message key='txgl_apimanage_text_11' defVal='推送上行' fileName='mwadmin'/>(MO)
				                                     </option>
				                                    <option value="RPT" <%if(null!=conditionMap.get("funName")&&"RPT".equals(conditionMap.get("funName"))){%> selected="selected" <%}%>>
				                                    	<emp:message key='txgl_apimanage_text_12' defVal='推送状态报告' fileName='mwadmin'/>(RPT)
				                                    </option>
													</select>
												</label>
											</td>
											<td >
												<emp:message key='txgl_apimanage_text_58' defVal='参数名称：' fileName='mwadmin'/>
											</td>
											<td>
											<input type="text"  class="argName" name="argName" id="argName" size="20"  maxlength="50"
											value="<%=null==conditionMap.get("argName&like")?"":conditionMap.get("argName&like") %>"/>
											</td>
											<td >
												<emp:message key='txgl_apimanage_text_59' defVal='请求类型：' fileName='mwadmin'/>
											</td>
											<td>
												<label>
													<select  class="cmdType"  name="cmdType" id="cmdType" >
	                                    			<option value="" selected="selected"><emp:message key='txgl_apimanage_text_35' defVal='全部' fileName='mwadmin'/></option>
				                                    <option value="1" <%if(null!=conditionMap.get("cmdType")&&"1".equals(conditionMap.get("cmdType"))){%> selected="selected" <%}%>><emp:message key='txgl_apimanage_text_60' defVal='请求' fileName='mwadmin'/></option>
				                                    <option value="2" <%if(null!=conditionMap.get("cmdType")&&"2".equals(conditionMap.get("cmdType"))){%> selected="selected" <%}%>><emp:message key='txgl_apimanage_text_61' defVal='全成功回应' fileName='mwadmin'/></option>
				                                    <option value="3" <%if(null!=conditionMap.get("cmdType")&&"3".equals(conditionMap.get("cmdType"))){%> selected="selected" <%}%>><emp:message key='txgl_apimanage_text_62' defVal='全失败回应' fileName='mwadmin'/></option>
				                                    <option value="4" <%if(null!=conditionMap.get("cmdType")&&"4".equals(conditionMap.get("cmdType"))){%> selected="selected" <%}%>><emp:message key='txgl_apimanage_text_63' defVal='部分成功回应' fileName='mwadmin'/></option>
				                                    <option value="5" <%if(null!=conditionMap.get("cmdType")&&"5".equals(conditionMap.get("cmdType"))){%> selected="selected" <%}%>><emp:message key='txgl_apimanage_text_64' defVal='回应详细信息' fileName='mwadmin'/></option>
													</select>
												</label>
											</td>
											<td class="tdSer"><center><a id="search"></a></center></td>
										</tr>
										<tr>
											<td >
												<emp:message key='txgl_apimanage_text_37' defVal='创建时间：' fileName='mwadmin'/>
											</td>
											<td>
												<input type="text" name="startdate" id="startdate"
												class="Wdate startdate" readonly="readonly"
												onclick="stime()"
												value="<%=conditionMap.get("startdate") != null ? conditionMap.get("startdate") : ""%>"/>
											</td>
											<td >
												&nbsp;&nbsp;&nbsp;&nbsp;<emp:message key='txgl_apimanage_text_38' defVal='至：' fileName='mwadmin'/>
											</td>
											<td>
												<input type="text" name="enddate" id="enddate"
												class="Wdate enddate" readonly="readonly"
												onclick="rtime()"
												value="<%=conditionMap.get("enddate") != null ? conditionMap.get("enddate") : ""%>"/>
											</td>
											<td colspan="3"></td>
										</tr>
							</table>
						</div>
						<table id="content" class="content_table">
							<thead>
							<tr>
									<th>
										<emp:message key='txgl_apimanage_text_65' defVal='方法名称' fileName='mwadmin'/>
									</th>
									<th >
										<emp:message key='txgl_apimanage_text_66' defVal='参数名称' fileName='mwadmin'/>
									</th>
									<th >
										<emp:message key='txgl_apimanage_text_67' defVal='请求类型' fileName='mwadmin'/>
									</th> 
									<th >
										<emp:message key='txgl_apimanage_text_68' defVal='参数长度' fileName='mwadmin'/>
									</th>
									<th >
										<emp:message key='txgl_apimanage_text_69' defVal='参数名描述' fileName='mwadmin'/>
									</th>
									<th >
										<emp:message key='txgl_apimanage_text_70' defVal='参数类型' fileName='mwadmin'/>
									</th>
									<th class="cjsj_th">
										<emp:message key='txgl_apimanage_text_45' defVal='创建时间' fileName='mwadmin'/>
									</th>
									<th class="xgsj_th">
										<emp:message key='txgl_apimanage_text_46' defVal='修改时间' fileName='mwadmin'/>
									</th>
								</tr>
							</thead>
							<tbody>
							<%
								@ SuppressWarnings("unchecked")
			                 	List<GwBasePara> gwBaseParaList=(List<GwBasePara>)request.getAttribute("gwBaseParaList");
			                	if(gwBaseParaList != null&&gwBaseParaList.size()>0)                       		
			                	{       
			                		 String keyId;
									for(int i=0;i<gwBaseParaList.size();i++ )
									{
										GwBasePara gbp=gwBaseParaList.get(i);
			                 %>
								<tr>
									<td>
									<%
									if("single_send".equals(gbp.getFunName()))
									{
									 	out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_4",request) + "(single_send)");
									}else if("send_single".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_4",request) + "(send_single)");
									}else if("batch_send".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_5",request) + "(batch_send)");
									}else if("send_batch".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_5",request) + "(send_batch)");
									}else if("multi_send".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_6",request) + "(multi_send)");
									}else if("send_multi".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_6",request) + "(send_multi)");
									}
									else if("send_mixed".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_6",request) + "(send_mixed)");
									}else if("template_send".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_7",request) + "(template_send)");
									}else if("send_template".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_7",request) + "(send_template)");
									}else if("get_mo".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_8",request) + "(get_mo)");
									}else if("get_rpt".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_9",request) + "(get_rpt)");
									}else if("get_balance".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_10",request) + "(get_balance)");
									}else if("MO".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_11",request) + "(MO)");
									}else if("RPT".equals(gbp.getFunName()))
									{
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_12",request) + "(RPT)");
									}
									 %>
									</td>
									<td>
								       <%=gbp.getArgName()%>
									</td>
									<td>

									<%if(gbp.getCmdType()==1){
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_60",request));
									}else if(gbp.getCmdType()==2){
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_61",request));
									}else if(gbp.getCmdType()==3){
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_62",request));
									}else if(gbp.getCmdType()==4){
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_63",request));
									}else if(gbp.getCmdType()==5){
										out.print(MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_64",request));
									 }%>
									</td>
									<td>
									<%=gbp.getArgValueLen() %>	
									</td>
									<td>
									<%=gbp.getArgDes()%>	
									</td> 
									<td>
									<%
										if(gbp.getArgType()==1)
										{
											out.print("String");
										}else if(gbp.getArgType()==2)
										{
											out.print("int");
										}else if(gbp.getArgType()==3)
										{
											out.print("tinyint");
										}else if(gbp.getArgType()==4)
										{
											out.print("bigint");
										}
									%>	
									</td>
									<td >
									<%=gbp.getCreateTime()!=null?df.format(gbp.getCreateTime()):"-"%>
									</td>
									<td >
									<%=gbp.getModifTime()!=null?df.format(gbp.getModifTime()):"-"%>
									</td>
								</tr>
								<%}
				                } else{%>
				                <tr>
								<td colspan="8">
                                         <emp:message key='txgl_apimanage_text_52' defVal='无记录' fileName='mwadmin'/> 
								</td>
							    </tr>
				                <%} %>
								
						</tbody>
						<tfoot>
							<tr>
								<td colspan="8">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
			</div>
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		
				<script type="text/javascript">
		$(document).ready(function() {
			var findresult="<%=(String)request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
                //alert("加载页面失败,请检查网络是否正常!");
                alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_1"));
                return;
		    }

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
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
			$('#search').click(function(){submitForm();});

		});

			   //结束时间
			function rtime(){
				
			    var max = "2099-12-31 23:59:59";
			    var v = $("#startdate").attr("value");
			    var min = "1900-01-01 00:00:00";
				if(v.length != 0)
				{
				    var year = v.substring(0,4);
					var mon = v.substring(5,7);
					var day = 31;
					if (mon != "12")
					{
					    mon = String(parseInt(mon,10)+1);
					    if (mon.length == 1)
					    {
					        mon = "0"+mon;
					    }
					    switch(mon){
					    case "01":day = 31;break;
					    case "02":day = 28;break;
					    case "03":day = 31;break;
					    case "04":day = 30;break;
					    case "05":day = 31;break;
					    case "06":day = 30;break;
					    case "07":day = 31;break;
					    case "08":day = 31;break;
					    case "09":day = 30;break;
					    case "10":day = 31;break;
					    case "11":day = 30;break;
					    }
					}
					else
					{
					    year = String((parseInt(year,10)+1));
					    mon = "01";
					}
				}
				WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
			
			};
			
		//开始时间	
		function stime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#enddate").attr("value");
		    var min = "1900-01-01 00:00:00";
			if(v.length != 0)
			{
//			    max = v;
			    var year = v.substring(0,4);
				var mon = v.substring(5,7);
				if (mon != "01")
				{
				    mon = String(parseInt(mon,10)-1);
				    if (mon.length == 1)
				    {
				        mon = "0"+mon;
				    }
				}
				else
				{
				    year = String((parseInt(year,10)-1));
				    mon = "12";
				}
//				min = year+"-"+mon+"-01 00:00:00"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

		};
	</script>
	</body>
</html>
