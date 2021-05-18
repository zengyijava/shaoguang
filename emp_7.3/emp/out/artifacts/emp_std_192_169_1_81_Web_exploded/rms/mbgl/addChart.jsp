<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String dataJson = (String)request.getParameter("dataJson");
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'Test.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<style type="text/css">
		#chartPopup td{
			text-align:center;
			height:36px;
		}
		#staticStateTable td{
			width:110px;
		}
		/*select {
			-webkit-appearance:menulist;
		}*/
		#chartPopup .c_selectBox{
			/* margin-left: 108px; */
			margin-top: 9px;
			height: 32px!important;
			width: 214px!important;
		}
		#chartPopup .c_result{
			top: 32px!important;
			width: 214px!important;
		}
		#chartPopup .c_input{
			height: 28px!important;
			text-align: left;
		}
	</style>
  </head>
  
  <body>
  	<div style="z-index:100;position: absolute;overflow: hidden;top:0px;left: 0;height: 100%;width: 100%;background-color: #333333;opacity:0.8;margin: 0;padding: 0;display: none;" id="popup_div_1"></div>
   	<div style="position: absolute;z-index:150;width: 876px;height: 455px;background-color: #ffffff;border-radius:5px;left:90px;top:130px;display: none;" id="chartPopup">
		<div style="height: 40px;width:876px;background-color: #f7f9fc;border-bottom: 1px solid #eaeff4; ">
			<div style="height: 40px;width:796px;float: left;line-height: 40px;padding-left: 30px;" id="popup_div_3"><emp:message key="rms_fxapp_myscene_insertgraph" defVal="插入图表" fileName="rms"/></div>
			<div style="height: 40px;width: 40px;float: left;line-height: 40px;text-align: center;cursor: pointer;color:#888888" onclick="displayDiv()">X</div>
		</div>
		<div style="height: 405px;width: 876px;">
			<div style="height: 405px;width:464px;float: left;">
				<div>
					<div style="float: left;margin-left: 34px;margin-top: 8px;font-size: 13px;;"><emp:message key="rms_fxapp_myscene_graphicstype" defVal="图形类型：" fileName="rms"/></div>
					<div id="selfSelect" <%if(StaticValue.ZH_HK.equals(langName)){%>style="margin-left:126px;"<%}else{%>style="margin-left:108px;"<%}%>>
						<select id="chartType" onchange="chartChange();" style="height: 30px;margin-top: 12px;width: 220px;margin-left: 10px;" isInput="false">
							<option value="1"><emp:message key="rms_fxapp_addchart_pinechart" defVal="饼状图" fileName="rms"/></option>
							<option value="2"><emp:message key="rms_fxapp_addchart_barchart" defVal="柱状图" fileName="rms"/></option>
							<option value="3"><emp:message key="rms_fxapp_addchart_linechart" defVal="折线图" fileName="rms"/></option>
							<%-- <option value="4">工资条</option>
							<option value="5">表格</option> --%>
						</select>
					</div>
				</div>
				<%-- <div>${dataJson.chartType}2222</div> --%>
				<div style="margin-top: 12px;">
					<div style="float: left;margin-left: 34px;font-size: 13px;margin-top: 6px;"><emp:message key="rms_fxapp_myscene_graphtitle" defVal="图形标题：" fileName="rms"/></div>
					<div>
						<input type="text" id="chartTitle" value="<emp:message key="rms_fxapp_myscene_title" defVal="标题" fileName="rms"/>" style="height: 26px;width: 150px;text-align: left;border: 1px solid #A9A9A9;margin-left: 10px;" oninput="change()" maxlength="20">
						<input type="button" value="<emp:message key="rms_fxapp_myscene_addparam" defVal="添加参数" fileName="rms"/>" style="height: 20px;width: 60px;text-align: center;border-radius:5px;background-color: #42bafe;color:#fff; " onclick="addParm()">
					</div>
				</div>
			
				<div style="margin-top: 8px;">
					<div style="float: left;margin-left: 34px;font-size: 13px;margin-top: 6px;"><emp:message key="rms_fxapp_myscene_datatype" defVal="数据类型：" fileName="rms"/></div>
					<input  checked="checked" name="ptType" id="type1" type="radio" value="1" onclick="javascript:toAction()" style="display: inline-block;width: 15px;margin-top: 6px;margin-left: 10px;"><label style="display: inline-block;font-size:13px;"><emp:message key="rms_fxapp_myscene_static" defVal="静态" fileName="rms"/></label>
					<input  name="ptType" id="type2" type="radio" value="2" onclick="javascript:toAction()" style="display: inline-block;width: 15px;margin-top: 6px;margin-left: 12px;"><label style="display: inline-block;font-size:13px;"><emp:message key="rms_fxapp_myscene_datadynamic" defVal="数值动态" fileName="rms"/></label>
					<input  name="ptType" id="type3" type="radio" value="3" onclick="javascript:toAction()" style="display: inline-block;width: 15px;margin-top: 6px;margin-left: 12px;"><label style="display: inline-block;font-size:13px;"><emp:message key="rms_fxapp_myscene_dynamic" defVal="全动态" fileName="rms"/></label>
					<div style="float: right;<%if(StaticValue.ZH_HK.equals(langName)){%>margin-right:50px;;<%}else{%>margin-right: 110px;<%}%>font-size: 13px;margin-top: 7px;color:#42bafe;cursor: pointer;" onmouseover="display()" onmouseout="disappear()"><emp:message key="rms_fxapp_myscene_description" defVal="说明" fileName="rms"/></div>
				</div>
				<div class = "box" style="display: none;" onmouseover="display()" onmouseout="disappear()"><emp:message key="rms_fxapp_addchart_descdetail" defVal="说明：数据类型为静态时，每个手机号码看到的图形都是一样的；数据类型为数值动态或全动态时，每个手机号码需上传具体数值，每个手机号码显示的图形根据数值而显示，在我的场景页面提供“下载号码文件格式”。" fileName="rms"/></div>
				<div style="margin-top: 12px;">
					<div style="float: left;margin-left: 34px;font-size: 13px;margin-top: 12px;"><emp:message key="rms_fxapp_myscene_graphdata" defVal="图形数据：" fileName="rms"/></div>
					<div class= "pie_Chart" >
						<div style="display: block;height: 173px;overflow:auto;padding-top: 6px;" id="staticState">
							<table width="340px" style="white-space:nowrap" id ="staticStateTable">
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"></td>
									<td><button onclick="add(this,'1');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'1')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"></td>
									<td><button onclick="add(this,'1');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'1')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;""></td>
									<td><button onclick="add(this,'1');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'1')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"></td>
									<td><button onclick="add(this,'1');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'1')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
							</table>
						</div>
						<div style="display: none;height: 173px;overflow:auto;padding-top: 6px;" id="dynamic">
							<table width="340px" style="white-space:nowrap" id ="dynamicTable">
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle1" defVal="行标题1" fileName="rms"/></td>
									<td style="color:#cccccc">{#1<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>1<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>}</td>
									<td style="width: 72px;"><button onclick="add(this,'2');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'2')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle2" defVal="行标题2" fileName="rms"/></td>
									<td style="color:#cccccc">{#2<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>1<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="width: 72px;"><button onclick="add(this,'2');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'2')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle3" defVal="行标题3" fileName="rms"/></td>
									<td style="color:#cccccc">{#3<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>1<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="width: 72px;"><button onclick="add(this,'2');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'2')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle4" defVal="行标题4" fileName="rms"/></td>
									<td style="color:#cccccc">{#4<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>1<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="width: 72px;"><button onclick="add(this,'2');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'2')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
							</table>
						</div>
					</div>
					<div class="bar_Chart" style="display: none;">
						<div style="display: none;height: 173px;overflow:auto;padding-top: 6px;" id="staticState_bar">
							<table width="340px" style="white-space:nowrap" id ="staticState_bar_table">
								<tr>
									<td></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle1" defVal="列标题1" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle2" defVal="列标题2" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle3" defVal="列标题3" fileName="rms"/></td>
									<td style="width: 72px;"><button onclick="barAddrow('staticState_bar_table','1');" title="添加列"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="barRemoverow('staticState_bar_table','1')" title="删除列"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle1" defVal="行标题1" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >1</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >2</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >3</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'1');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'1')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle2" defVal="行标题2" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >2</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >3</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >4</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'1');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'1')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle3" defVal="行标题3" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >3</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >4</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >5</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'1');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'1')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
							</table>
						</div>
						<div id="dynamic_bar" style="overflow:auto;height: 173px;width:360px;display: none;padding-top: 6px;">
							<table width="340px" style="white-space:nowrap" id ="dynamic_bar_table">
								<tr>
									<td></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle1" defVal="列标题1" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle2" defVal="列标题2" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle3" defVal="列标题3" fileName="rms"/></td>
									<td style="width: 72px;"><button onclick="barAddrow('dynamic_bar_table','2');" title="添加列"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="barRemoverow('dynamic_bar_table','2')" title="删除列"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle1" defVal="行标题1" fileName="rms"/></td>
									<td style="color: #ccc;">{#1<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>1<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>}</td>
									<td style="color: #ccc;">{#1<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>2<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="color: #ccc;" >{#1<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>3<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'2');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'2')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle2" defVal="行标题2" fileName="rms"/></td>
									<td style="color: #ccc;" >{#2<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>1<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="color: #ccc;" >{#2<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>2<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="color: #ccc;" >{#2<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>3<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'2');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'2')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle3" defVal="行标题3" fileName="rms"/></td>
									<td style="color: #ccc;" >{#3<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>1<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="color: #ccc;"  >{#3<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>2<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="color: #ccc;" >{#3<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>3<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'2');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'2')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
							</table>
						</div>
					</div>
					<div class="line_Chart">
						<div id="staticState_line" style="overflow:auto;height: 173px;width:360px;display: none;padding-top: 6px;">
							<table width="340px" style="white-space:nowrap" id ="staticState_line_table">
								<tr>
									<td></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle1" defVal="列标题1" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle2" defVal="列标题2" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle3" defVal="列标题3" fileName="rms"/></td>
									<td style="width: 72px;"><button onclick="barAddrow('staticState_line_table','1');" title="添加列"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="barRemoverow('staticState_line_table','1')" title="删除列"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle1" defVal="行标题1" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >1</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >2</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >3</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'1');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'1')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle2" defVal="行标题2" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >2</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >3</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >4</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'1');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'1')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle3" defVal="行标题3" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >3</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >4</td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" >5</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'1');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'1')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
							</table>
						</div>
						<div id="dynamic_line" style="overflow:auto;height: 173px;width:360px;display: none;padding-top: 6px;">
							<table width="340px" style="white-space:nowrap" id ="dynamic_line_table">
								<tr>
									<td></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle1" defVal="列标题1" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle2" defVal="列标题2" fileName="rms"/></td>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;"><emp:message key="rms_fxapp_addchart_coltitle3" defVal="列标题3" fileName="rms"/></td>
									<td style="width: 72px;"><button onclick="barAddrow('dynamic_line_table','2');"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="barRemoverow('dynamic_line_table','2')"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle1" defVal="行标题1" fileName="rms"/></td>
									<td style="color: #ccc;">{#1<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>1<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>}</td>
									<td style="color: #ccc;">{#1<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>2<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="color: #ccc;" >{#1<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>3<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'2');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'2')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" style="cursor: pointer;" ><emp:message key="rms_fxapp_addchart_rowtitle2" defVal="行标题2" fileName="rms"/></td>
									<td style="color: #ccc;" >{#2<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>1<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="color: #ccc;" >{#2<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>2<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="color: #ccc;" >{#2<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>3<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'2');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'2')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
								<tr>
									<td ondblclick="ShowElement(this)" ><emp:message key="rms_fxapp_addchart_rowtitle3" defVal="行标题3" fileName="rms"/></td>
									<td style="color: #ccc;" >{#3<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>1<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="color: #ccc;"  >{#3<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>2<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="color: #ccc;" >{#3<emp:message key="rms_fxapp_addchart_row" defVal="行" fileName="rms"/>3<emp:message key="rms_fxapp_addchart_column" defVal="列" fileName="rms"/>#}</td>
									<td style="width: 72px;"><button onclick="barAddcol(this,'2');" title="<emp:message key="rms_fxapp_addchart_addrow" defVal="添加行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_add" defVal="添加" fileName="rms"/></button><button onclick="removeRow(this,'2')" title="<emp:message key="rms_fxapp_addchart_delrow" defVal="删除行" fileName="rms"/>"><emp:message key="rms_fxapp_myscene_del" defVal="删除" fileName="rms"/></button></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				<div style="color: #ccc;margin-left: 100px;font-size: 10px;margin-top: 10px;"><emp:message key="rms_fxapp_myscene_tip2" defVal="注：图形数据中黑色字体双击可更改内容，灰色字体不可更改" fileName="rms"/></div>
				<div>
					<input type="button" value="<emp:message key="rms_fxapp_myscene_insert" defVal="插入" fileName="rms"></emp:message>" onclick="insertInfo();" style="margin-top: 20px;height: 30px;width: 74px;margin-left: 100px;background-color: #42bafe;color: white;border-radius:5px;cursor: pointer;"/>
					<input type="button" value="<emp:message key="common_cancel" defVal="取消" fileName="common"></emp:message>" onclick="displayDiv();" style="height: 30px;width: 74px;margin-left: 20px;background-color: #f8f9fb;color: #666666;border: 1px solid #dee2e6;border-radius:5px;cursor: pointer;"/>
				</div>
			</div>
			<div style="float: left;width: 0.1px;height: 350px;border: 0.5px solid #f4f4f4;margin-top: 20px;"></div>
			<div style="height: 405px;width:408px;float: left;" id= "pie_Chart">
				<div style="margin-left: 30px;margin-top: 12px;font-size: 14px;color: #888888;"><emp:message key="rms_fxapp_addchart_preview" defVal="预览：" fileName="rms"/></div>
				<div style="border: 0px solid red;margin-left: 50px;" id="echartPie"></div>
			</div>
			<div style="height: 405px;width:408px;float: left;display: none;" id= "bar_Chart">
				<div style="margin-left: 30px;margin-top: 12px;font-size: 14px;color: #888888;"><emp:message key="rms_fxapp_addchart_preview" defVal="预览：" fileName="rms"/></div>
				<div style="border: 0px solid red;margin-left: 20px;" id="echartBar"></div>
			</div>
			<div style="height: 405px;width:408px;float: left;display: none;" id= "line_Chart">
				<div style="margin-left: 30px;margin-top: 12px;font-size: 14px;color: #888888;"><emp:message key="rms_fxapp_addchart_preview" defVal="预览：" fileName="rms"/></div>
				<div style="border: 0px solid red;margin-left: 20px;" id="echartLine"></div>
			</div>
		</div>
	</div>
  </body>
</html>
