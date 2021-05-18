<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import= "com.montnets.emp.common.constant.ViewParams"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ page import="org.apache.commons.beanutils.DynaBean" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	//************按钮权限处理**************
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String reTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(reTitle);
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	Object  packageList=request.getAttribute("packageList");
	Date date =new Date();
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
	String today=df.format(date);
	//修改结束年份为下一年的
	Date endDate =new Date();
	endDate.setYear(date.getYear()+10);
	String endDay=df.format(endDate);
%>
<html>
	<head><%@ include file="/common/common.jsp"%>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />	
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/floating.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addPage.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/ydyw/busTaoCan/css/addTaoCan_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}else{%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/ydyw/busTaoCan/css/addTaoCan.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<style>
			.tabCont {
				height: 26px;
				font-size: 14px;
				padding-left: 10px;
				line-height: 26px;
			}
			.tabBody{
				margin:5px 0;
			}
			.tabBody td{
				padding:5px 0;
			}
			.txtwidth{
				width: 200px;
			}
		</style>
	</head>
		<body>
		<div id="container" class="container" style="width:1200px; min-height: 265px;">
			<%-- 当前位置 --%>
			<%=ViewParams.getPosition(empLangName,menuCode) %>
				<input id="lguserid" name="lguserid" type="hidden" value="<%=request.getParameter("lguserid") %>"/>
				<input id="lgcorpcode" name="lgcorpcode" type="hidden" value="<%=request.getParameter("lgcorpcode") %>"/>
				<input type="hidden" id="pathUrl" value="<%=path %>"/>
				<input type="hidden" id="packagecodes" value=""/>
				
			<%-- 内容开始 --%>
			<div id="rContent"   style="margin-left: 10px;">
                  <table id="sysTable" style="width: 800px;margin:5px 15px;"> 
                  <tr>
                  	  <td style="width: 95px;"><emp:message key="ydyw_ywgl_ywbgl_text_33" defVal="套餐名称" fileName="ydyw"></emp:message></td>
					  <td><input type="text" name="name" style="width: 200px;" id="name" maxlength="25"/>&nbsp;&nbsp;<font style="color: red;">*</font></td>
					  <td><emp:message key="ydyw_ywgl_ywbgl_text_35" defVal="套餐编号" fileName="ydyw"></emp:message></td>
					  <td><input type="text" name="code" style="width: 200px;" id="code"  maxlength="25"/>&nbsp;&nbsp;<font style="color: red;">*</font></td>
                  </tr>
                  <tr>
					  <td style="width: 95px;"><emp:message key="ydyw_ywgl_ywtcgl_text_8" defVal="套餐有效期：" fileName="ydyw"></emp:message></td>
					  <td><input type="text" value="<%=today %>" id="startSubmitTime" name="startSubmitTime" style="cursor: pointer; width: 200px;" class="Wdate div_bd" readonly="readonly" onclick="setime()">&nbsp;&nbsp;<font style="color: red;">*</font></td>
					  <td><emp:message key="common_to" defVal="至：" fileName="common"></emp:message></td>
					  <td><input type="text" value="<%=endDay %>" id="endSubmitTime" name="endSubmitTime" style="cursor: pointer; width: 200px;" class="Wdate div_bd" readonly="readonly" onclick="retime()">&nbsp;&nbsp;<font style="color: red;">*</font></td>
                  </tr> 
                  </table>
	          
			<div id="tab1"  style="margin-left: 10px;margin-top: 10px;">
	           <label style="float: left;"><font style="font-weight:bold;"><emp:message key="ydyw_ywgl_ywtcgl_text_9" defVal="业务包配置:" fileName="ydyw"></emp:message></font></label>
	           	<div style=" float: left; margin: 8px 5px;width: 1040px;height: 1px;border-top: solid 1px #ccc;"></div>
                 </div>
 			</div>
	              <div>
		              <div style="width:495px;margin-top: 10px;float: left;margin-left: 120px;_margin-left:60px;">
		             	<table id="pkgTitle" style="_width:498px;">
							<tr class="title_bd" style="height: 25px;">
								<td class="div_bd title_bg" style="width: 246px;"><emp:message key="ydyw_ywgl_ywbgl_text_1" fileName="ydyw" defVal="业务包名称"></emp:message></td>
								<td class="div_bd title_bg" style="width: 246px;"><emp:message key="ydyw_ywgl_ywbgl_text_3" fileName="ydyw" defVal="业务包编号"></emp:message></td>
							</tr>
							
						</table>
		            	<div style="width: 496px; height: 98px ;overflow-y:auto;overflow-x:hidden; border: 1px solid #ccdef8;">
							<table id="packageTable" style="border: solid 1px #ccdef8; width: 495px;_width:498px; ">
							<tr id="nopag"><td colspan="2" class="trcss"><emp:message key="ydyw_ywgl_ywtcgl_text_10" fileName="ydyw" defVal="没有业务包信息"></emp:message></td></tr>
							</table>
						</div>					
		              </div>
		              <div id="tabBody3" style="float: left;height: 124px;border: solid 1px #ccdef8;margin-top: 10px;position: relative;background-color: #f1f1f9;">
		               	<input type="reset" value="" id="showTC" style="background: url(<%=iPath%>/images/button_01.png) no-repeat center center; height:120px; border:0;cursor: pointer;"  onclick="javascript:showtaocan()" />
		               	<span style="display:block;height: 20px;position: absolute;top:80px;"><emp:message key="ydyw_ywgl_ywtcgl_text_11" fileName="ydyw" defVal="添加业务包"></emp:message></span>
		              </div>
		              <span style="display:block;width:5px;height:5px;color: red;margin-top:75px; float: left;">&nbsp;&nbsp;*</span>
		              <div class="clear"></div>
				</div>
					<div id="tab2"  style="margin-left: 10px;margin-top: 20px;">
			           <label style="float: left;"><font style="font-weight:bold;margin-left: 10px;"><emp:message key="ydyw_ywgl_ywtcgl_text_12" fileName="ydyw" defVal="订购计费设置:"></emp:message></font></label>
			           <div style=" float: left; margin: 8px 5px;width: 1030px;height: 1px;border-top: solid 1px #ccc;"></div>
	                 </div>
                </div>
                  <div id="tabBody3" style="margin-left: 10px;">
                  <table id="sysTable">
                  <tr>
                  <td><emp:message key="ydyw_ywgl_ywbgl_text_37" defVal="计费类型：" fileName="ydyw"></emp:message></td>
                  <td>
                  <select name="taocan_type" id="taocan_type" onchange="javascript:changestate()">
                  <option value="" ><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"></emp:message></option>
                  <% 
                  
                  if(request.getAttribute("typeMap")!=null){
                	  Map<String, String> map= (Map<String, String>)request.getAttribute("typeMap");
                	  Iterator it=map.entrySet().iterator();
                	  while(it.hasNext()){
                		  Map.Entry<String, String>  ent= (Map.Entry<String, String>)it.next();
                		  String key=ent.getKey();
                		  String value=ent.getValue();
						  if("VIP免费".equals(value)){
							  value = MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_5",request);
						  }else if("包月".equals(value)){
							  value = MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_by",request);
						  }else if("包季".equals(value)){
							  value = MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_bj",request);
						  }else if("包年".equals(value)){
							  value = MessageUtils.extractMessage("ydyw","ydyw_ywgl_tczlsz_text_bn",request);
						  }
                		  if("-1".equals(key)){
                			  continue;
                		  }
                		  %>
                		  <option value="<%=key %>" ><%=value %></option>
                		  <% 
                	  }
                  }
                  %>
                   </select>
                   <input type="text" name="fee" id="fee" onkeyup="value=value.replace(/[^\d]/g,'')" style="width: 40px;height: 18px;" maxlength="5"/>&nbsp;&nbsp;<emp:message key="ydyw_qyjfcx_khjfcx_text_9" fileName="ydyw" defVal="元"></emp:message><emp:message key="text_per" fileName="common" defVal="/"></emp:message><span id="strlen" ><emp:message key="text_month" fileName="common" defVal="月"></emp:message></span><font style="color: red;">*</font>
				 </td>
                  <td><emp:message key="ydyw_ywgl_ywbgl_text_43" defVal="扣费时间：" fileName="ydyw"></emp:message></td>
                  <td>
                  <select name="selectdate" id="selectdate" onchange="javascript:changeType()"; class="txtwidth">
                  <option value="" ><emp:message key="common_pleaseSelect" defVal="请选择" fileName="common"></emp:message></option>
                  <option value="2" ><emp:message key="ydyw_ywgl_ywtcgl_text_13" fileName="ydyw" defVal="订购生效当天"></emp:message></option>
					  <%if("zh_HK".equals(empLangName)){%>
					  <option value="1">Next month of order take effect, date</option>
					  <option value="3">The month of order take effect, date</option>
					  <%}else{%>
					  <option value="1"><emp:message key="ydyw_ywgl_ywtcgl_text_4" fileName="ydyw" defVal="订购生效次月"></emp:message></option>
					  <option value="3"><emp:message key="ydyw_ywgl_ywtcgl_text_7" fileName="ydyw" defVal="订购生效当月"></emp:message></option>
					  <%}%>
                   </select>
                   
                   <span id="fee_div">
                   <input type="text" name="buckle_date" id="buckle_date" onkeyup="value=value.replace(/[^\d]/g,'')" style="width: 40px;height: 18px;" maxlength="2" disabled/>
					   <%if(!"zh_HK".equals(empLangName)){%>
						   <emp:message key="ydyw_qyjfcx_khjfcx_text_11" fileName="ydyw" defVal="号"></emp:message>
					   <%}%>
				   </span>
					  <emp:message key="ydyw_ywgl_ywtcgl_text_14" fileName="ydyw" defVal="开始扣费"></emp:message>
                  </td>
                  </tr>
                  <tr>
                  <td><emp:message key="ydyw_ywgl_ywtcgl_text_15" fileName="ydyw" defVal="补扣次数："></emp:message></td><td> <input type="text" name="backup_max" id="backup_max" class="txtwidth" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="1"/>&nbsp;&nbsp;<emp:message key="text_times" fileName="common" defVal="次"></emp:message></td>
                  <td><emp:message key="ydyw_ywgl_ywtcgl_text_16" fileName="ydyw" defVal="补扣间隔："></emp:message></td><td><input type="text" name="backup_day" id="backup_day"  class="txtwidth" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="3"/>&nbsp;&nbsp;<emp:message key="text_sky" fileName="common" defVal="天"></emp:message></td>
                  </tr>
                  <tr>
                  <td><emp:message key="ydyw_ywgl_ywtcgl_text_17" fileName="ydyw" defVal="免费试用："></emp:message></td><td><input type="text" name="free_days" id="free_days" class="txtwidth" onkeyup="value=value.replace(/[^\d]/g,'')" maxlength="3"/>&nbsp;&nbsp;<emp:message key="text_sky" fileName="common" defVal="天"></emp:message></td>
                  <td><emp:message key="ydyw_ywgl_ywtcgl_text_18" fileName="ydyw" defVal="试用时间："></emp:message></td>
                  <td>
                  <input type="radio" name="timerStatus" value="0" style="vertical-align: middle;" id="sendNow" checked="checked" onclick="javascript:$('#time2').hide()" />&nbsp;<emp:message key="ydyw_ywgl_ywtcgl_text_19" fileName="ydyw" defVal="订购之日起"></emp:message>
                  <input type="radio" name="timerStatus" value="1" style="vertical-align: middle;" id="sendOther" style="margin-left: 10px;"
					onclick="javascript:$('#time2').show()"/>&nbsp;<emp:message key="ydyw_ywgl_ywtcgl_text_20" fileName="ydyw" defVal="自定义时间"></emp:message>
				<label id="time2" style="display: none;">
					<input type="text" class="Wdate div_bd" style="width: 100px; vertical-align: middle;height: 18px; " readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',isShowToday:false})" id="timerTime" name="timerTime" value="<%=today %>">
				</label>
                  </td>
                  </tr>
                  <tr>
					</tr>	
                  </table>
                   </div>
                      <div  style="margin-left: 300px;padding-top: 20px;margin-top: 40px;">
                      		<input type="button" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" class="btnClass5 mr23" onclick="javascript:save('add')" />
					 		<input type="reset" value="<emp:message key="common_btn_16" defVal="取消" fileName="common"></emp:message>"  class="btnClass6" onclick="javascript:toList()" />
                      </div>
                
			<%-- 弹出框 --%>
			
			<div id="msgtep" title="<emp:message key="ydyw_ywgl_ywtcgl_text_11" defVal="添加业务包" fileName="ydyw"></emp:message>" style="padding:5px;width:280px;display:none;word-wrap: break-word;word-break:break-all;overflow-y:auto;">
			       <table border="0"  style="margin: 0 auto;">
			       <tr>
			       <td colspan="3" style="height: 30px;"><emp:message key="ydyw_ywgl_ywbgl_text_33" defVal="套餐名称：" fileName="ydyw"></emp:message><font id="titlename"></font></td>
			       </tr>
					<tr>
				    <td valign="top">
					<input id="epname" name="epname" type="text" maxlength="16" class="graytext" value="" style="font-size:12px;border:0px;width:190px;height:21px;line-height:28px; border:#c9d9f2 1px solid; background-color:#F3FAEE;float: left;"  onkeypress="if(event.keyCode==13) {searchName();event.returnValue=false;}"/>
					 <a onclick="searchName();" style="cursor: pointer;">
					 <img id="searchIcon" src="<%=skin %>/images/query.png" border="0"/></a>
					</td>
					<td colspan="2" style="font-size:12px;color:#666666;text-align:right;"><emp:message key="ydyw_ywgl_ywtcgl_text_21" defVal="已选择业务包数：" fileName="ydyw"></emp:message><label id="manCount">0</label></td>
			    	</tr>
						<tr>
							<td>
								<table>
								
									<tr>
										<td>
											<div class="dept div_bd" style="height: 215px; width: 212px;overflow-x:hidden;overflow-y:hidden;padding-left:5px;">
												<select  multiple name="left" id="left" size="15" class="left_select_choose">
											<% 
											if(packageList!=null){
											List<DynaBean> pack=(List<DynaBean>)packageList;
												if(pack.size()>0){
													for(int i=0;i<pack.size();i++){
														DynaBean db=pack.get(i);
														String showAll=db.get("package_name")+"("+db.get("package_code")+")";
													%>
														<option value="<%=db.get("package_id")%>" title="<%=showAll%>" taocan_name="<%=db.get("package_name")%>" taocan_code="<%=db.get("package_code")%>"  ><%=showAll%></option>
													<%
													}
												}
											}
											%>
												</select>
											 
											</div>
										</td>
									</tr>
								</table>
							</td>
							<td width="60" align="center">
							<br>
								<input class="btnClass1" type="button" id="toLeft" value="<emp:message key="common_option" defVal="选择" fileName="common"></emp:message>"  onclick="javascript:router();">
								<br/>
								<br/>
								<input class="btnClass1" type="button" id="toRight" value="<emp:message key="common_delete" defVal="删除" fileName="common"></emp:message>" onclick="javascript:moveRight();">
							</td>
							<td>
								<div id="rightDiv"   class="dept div_bd" >
									<select multiple name="right" id="right" size="20" 
										style='width:104px;height: 318px;display:none; border:0;float:left;color: black;font-size: 12px;padding:4px;vertical-align:middle;margin:-6px -10px;' >
									</select>
									
									<ul id="getChooseMan">
									</ul>
								</div>
								
							</td>
						</tr>
						<tr>
							<td id="btn" colspan="3" style="text-align: center;padding:15px;">
								<input name="subBut" type="button" id="subBut" value="<emp:message key="common_confirm" defVal="确定" fileName="common"></emp:message>" onclick="bindTaoCan()" class="btnClass5 mr23"/>
								<input name="cancelwid" type="button" onclick="closeDiv()" value="<emp:message key="common_btn_16" defVal="取消" fileName="common"></emp:message>" class="btnClass6"/>
							</td>
						  </tr>
				   </table>
				</div>
			
			
			    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    			<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
        		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
				<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	    			<script type="text/javascript" src="<%=commonPath %>/common/js/jquery_Ul_Send.js?V=116"></script>
	    		<script type="text/javascript" src="<%=iPath%>/js/taocanList.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
	    		<script type="text/javascript" src="<%=iPath%>/js/chooseInfo.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
				<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
				<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/ydyw_<%=empLangName%>.js"></script>
	    		<script type="text/javascript" >
	    		var today="<%=today%>";
	    		$(document).ready(function() {
			    	//初始化的时候，文本框隐藏
			    	$("#fee_div").hide();
	    		});
	    		</script>
	    	</body>
</html>