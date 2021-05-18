<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
String path = request.getContextPath();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("spAcctMonCfg");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<DynaBean> moniList = (List<DynaBean>) request
		.getAttribute("monitorList");

String accountname = request.getParameter("accountname");
String spaccountid = request.getParameter("spaccountid");
String monstatus = request.getParameter("monstatus");
@SuppressWarnings("unchecked")
List<DynaBean> spList = (List<DynaBean>) request.getAttribute("spList");
String spaccountType = request.getParameter("spaccountType");
if(!"1".equals(spaccountType)){
	spaccountType="0";
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'mon_hostMoConfig.jsp' starting page</title>
    <%@include file="/common/common.jsp" %>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<style type="text/css">
		.edit_spacct 
		{
			margin-left: 10px;
		}
		.edit_spacct tr td
		{
			height: 30px;
			line-height: 30px;
			font-size: 12px; 
			
		}
		.zhushi
		{
			padding-left:3px; 
		}
        .container {
            float: left;
            min-width: 1220px;
            _width: 1220px;
        }
        .maxwidth-td{
            width:135px;
        }
        #content .maxwidth-td .maxwidth{
            margin: 0 auto;
            width: 130px;
            display: block;
            white-space: nowrap;
            overflow: hidden;
            -o-text-overflow: ellipsis;
            text-overflow: ellipsis;
        }
        .c_selectBox{
			width: 208px!important;
		}
		.c_selectBox ul {
			width: 208px!important;
		}
		.c_selectBox ul li{
			width: 208px!important;
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
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="mon_spAcctMonCfg.htm" method="post" id="pageForm">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv">
				</div>
				<%--<a id="exportCondition">导出</a>--%>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td>
							<emp:message key="ptjk_common_spzh_mh" defVal="SP账号：" fileName="ptjk"/>
							</td>
							<td >
								<select name="tdhm_key" style="width:158px;display:none"  id="tdhm_key">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<%for(int i=0;i<spList.size();i++){ 
										String account=String.valueOf(spList.get(i).get("spaccountid"));
									%>
										<option value="<%=account %>"><%=account %></option>
									<%} %>
								</select>
								<input type="hidden" name="spaccountid" id="spaccountid" value="<%=spaccountid!=null?spaccountid:"" %>"/>
								<input type="hidden" name="spaccountType" id="spaccountType" value="<%=spaccountType %>"/><%-- 0表示下拉框取值 1表示文本框 --%>
							</td>
							<td>
								<emp:message key="ptjk_common_zhmc_mh" defVal="账号名称：" fileName="ptjk"/>
							</td>
							<td >	
								<input type="text" name="accountname" id="accountname" value="<%=accountname!=null?accountname:"" %>"/>
							</td>
							
							<td>
							<emp:message key="ptjk_common_jkzt_mh" defVal="监控状态：" fileName="ptjk"/>
							</td>
							<td>
								<select name="monstatus" id="monstatus" style="" class="input_bd" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="0" <%="0".equals(monstatus)?"selected":"" %>><emp:message key="ptjk_common_wjk" defVal="未监控" fileName="ptjk"/></option>
									<option value="1" <%="1".equals(monstatus)?"selected":"" %>><emp:message key="ptjk_common_jk" defVal="监控" fileName="ptjk"/></option>
								</select>
							</td>
							<td><emp:message key="ptjk_jkxq_spzh_dllx_mh" defVal="登录类型：" fileName="ptjk"/></td>
							<td>
								<select id="logintype" name="logintype" class="input_bd" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="1" <%if("1".equals(request.getParameter("logintype"))){%>selected="selected"<%}%>><emp:message key="ptjk_jkxq_spzh_wbsdl" defVal="WBS登录" fileName="ptjk"/></option>
									<option value="2" <%if("2".equals(request.getParameter("logintype"))){%>selected="selected"<%}%>><emp:message key="ptjk_jkxq_spzh_zldl" defVal="直连登录" fileName="ptjk"/></option>
									<option value="0" <%if("0".equals(request.getParameter("logintype"))){%>selected="selected"<%}%>><emp:message key="ptjk_common_wz" defVal="未知" fileName="ptjk"/></option>
								</select>
							</td>
							
							<td class="tdSer">
							     <center><a id="search"></a></center>
						    </td>		
						</tr>
					</tbody>
				</table>
			</div>
			<table id="content">
				<thead>
					<tr>
						<th>
							<emp:message key="ptjk_common_spzh" defVal="SP账号" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_zhmc" defVal="账号名称" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_spzh_zhlx" defVal="账号类型" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_spzh_dllx" defVal="登录类型" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkxq_spzh_fsjb" defVal="发送级别" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_jkzt" defVal="监控状态" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_tdzh_1" defVal="MT待发告警阀值(条)" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_tdzh_2" defVal="MO滞留告警阀值(条)" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_spzh_14" defVal="MO最低接收率" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_tdzh_4" defVal="RPT滞留告警阀值(条)" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_spzh_1" defVal="RPT最低接收率" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_jkgl_spzh_2" defVal="账号离线阀值(分钟)" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_xgsj" defVal="修改时间" fileName="ptjk"/>
						</th>
						<th>
							<emp:message key="ptjk_common_cz" defVal="操作" fileName="ptjk"/>
						</th>
					</tr>
				</thead>
				<tbody>
				<%
					if(moniList!=null && moniList.size()>0)
					{
						for(DynaBean host : moniList)
						{
				%>
				<tr>
					<td >
						<xmp><%=host.get("spaccountid") %></xmp>
					</td>
					<td class="maxwidth-td">
						<xmp class="maxwidth"><%=host.get("accountname") %></xmp>
					</td>
					<td>
					<%
						String type = host.get("spaccounttype").toString();
						if(type==null){
							out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_zlzh",request));
						}else if("1".equals(type)){
							out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_1",request));
						}else if("2".equals(type)){
							out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_3",request));
						}else{
							out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_zlzh",request));
						}
					%>
<%--						<xmp><%="1".equals(host.get("spaccounttype").toString())?"预付费":"后付费" %></xmp>--%>
					</td>
					<td>
						<%
							Object loginTypeObj=host.get("login_type");
							if(loginTypeObj==null)
							{
								out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wz",request));
							}else
							{
							    //登录类型
								String loginType=loginTypeObj.toString();
								if("1".equals(loginType))
								{
									out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_wbsdl",request));
								}else if("2".equals(loginType))
								{
									out.print(MessageUtils.extractMessage("ptjk","ptjk_jkxq_spzh_zldl",request));
								}else
								{
									out.print(MessageUtils.extractMessage("ptjk","ptjk_common_wz",request));
								}
								
							}
						 %>
					</td>
					<td >
						<%=(host.get("sendlevel")!=null&&!"".equals(host.get("sendlevel")))?Integer.valueOf(""+host.get("sendlevel"))+5:"-"%>
					</td>
					<td>
					<%
						String status = "0";
						if(host.get("monstatus")!=null){
							status = String.valueOf(host.get("monstatus"));
						}
					%>
						<%="1".equals(status)?MessageUtils.extractMessage("ptjk","ptjk_common_jk",request):MessageUtils.extractMessage("ptjk","ptjk_common_wjk",request) %>
					</td>
					<td>
						<%=host.get("mtremained")%>
					</td>
					<td>
						<%=host.get("moremained")%>
					</td>
					<td>
						<%=host.get("morecvratio") %>%
					</td>
					<td>
						<%=host.get("rptremained")%>
					</td>
					<td>
						<%=host.get("rptrecvratio") %>%
					</td>
					<td>
					    <%=host.get("offlinethreshd")%>
					</td>
					<td>
						<%=host.get("modifytime")!=null?df.format(host.get("modifytime")):"" %>
					</td>
					<td>
						<%if(btnMap.get(menuCode+"-1")!=null) { %>
						<a href="javascript:toEdit('<%=host.get("spaccountid") %>')"><emp:message key="ptjk_common_sz" defVal="设置" fileName="ptjk"/></a>
						<%}else{out.print("-");} %>
					</td>
				</tr>
				<%
						}
					}else{
				%>
				<tr><td align="center" colspan="14"><emp:message key="ptjk_common_wjl" defVal="无记录" fileName="ptjk"/></td></tr>
				<%} %>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="14">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
			</table>
		</form>
		</div>
			
		<%-- 内容结束 --%>
		
		<div id="modify" title="<emp:message key='ptjk_common_xxnr' defVal='信息内容' fileName='ptjk'/>"  style="padding:5px;width:300px;height:160px;display:none">
			<table width="100%">
				<thead>
					<tr style="padding-top:2px;margin-bottom: 2px;">
						<td style='word-break: break-all;'>
							<span><label id="msgcont" style="width:100%;height:100%"><xmp style='word-break: break-all;white-space:normal;'></xmp></label></span>
							 
						</td>
						 
					</tr>
				   <tr style="padding-top:2px;">
						<td>
						</td>
						</tr>
					 
				</thead>
			</table>
		</div>
		<input type="hidden" id="dialog_flag" value="1"/>
		<div id="editSpAcct" title="<emp:message key='ptjk_jkgl_tdzh_gjsz' defVal='告警设置' fileName='ptjk'/>"  style="display:none">
			<input type="hidden" name="spaccountid" value="spaccountid"/>
			<table class="edit_spacct">
				<tr>
					<td colspan="2"><emp:message key="ptjk_jkgl_tdzh_7" defVal="注：0或为空，代表不监控" fileName="ptjk"/></td>
				</tr>
					<tr>
						<td><emp:message key="ptjk_common_jkzt_mh" defVal="监控状态：" fileName="ptjk"/></td>
						<td>
							
							<select name="monstatus" id="monstatus" style="width: 163px;" class="select input_bd">
								<option value="1" ><emp:message key="ptjk_common_jk" defVal="监控" fileName="ptjk"/></option>
								<option value="0" ><emp:message key="ptjk_common_wjk" defVal="未监控" fileName="ptjk"/></option>
							</select>
						</td>
						<td class="zhushi">
							<span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_tdzh_sfjk" defVal="是否监控" fileName="ptjk"/></span>
						</td>
					</tr>
					<tr>
						<td><emp:message key="ptjk_common_gjsjh_mh" defVal="告警手机号：" fileName="ptjk"/></td>
						<td>
							<textarea name="monphone" id="monphone" class="input_bd" style="width: 159px;"></textarea>
						</td>
						<td class="zhushi">
							<span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_tdzh_19" defVal="可以设置至多十个手机号，号码间用逗号(,)分隔" fileName="ptjk"/></span>
						</td>
					</tr>
					
					<tr>
						<td><emp:message key="ptjk_common_gjyx_mh" defVal="告警邮箱：" fileName="ptjk"/></td>
						<td>
							<textarea name="monemail" id="monemail" class="input_bd" style="width: 159px;"></textarea>
						</td>
						<td class="zhushi">
							<span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_common_znszygyx" defVal="只能设置一个邮箱" fileName="ptjk"/></span>
						</td>
					</tr>
					
					<tr>
						<td><emp:message key="ptjk_jkgl_spzh_3" defVal="账号离线阀值：" fileName="ptjk"/></td>
						<td>
							<input type="text" name="offlinethreshd" id="offlinethreshd" maxlength="4" class="input_bd int" value=""/>
						</td>
						<td class="zhushi">
							<span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_spzh_4" defVal="(单位：分钟)，离线时长超过阀值告警，直连账号登录时，阀值才生效" fileName="ptjk"/></span>
						</td>
					</tr>
				<tr>
					<td><emp:message key="ptjk_jkgl_tdzh_8" defVal="MT待发告警阀值：" fileName="ptjk"/></td>
					<td>
						<input type="text" name="mtremained" maxlength="9" id="mtremained" class="input_bd int" value=""/>
					</td>
					<td class="zhushi"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_spzh_5" defVal="(单位：条)，监控SP账号下行最大滞留量" fileName="ptjk"/></span></td>
				</tr>
				<tr>
					<td><emp:message key="ptjk_jkgl_tdzh_10" defVal="MO滞留告警阀值：" fileName="ptjk"/></td>
					<td>
						<input type="text" name="moremained" maxlength="9" id="moremained" class="input_bd int" value=""/>
					</td>
					<td class="zhushi"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_spzh_6" defVal="(单位：条)，监控用户上行未转发至该SP账号业务系统最大滞留量" fileName="ptjk"/></span></td>
				</tr>
				<tr>
					<td><emp:message key="ptjk_jkgl_spzh_7" defVal="MO最低接收率：" fileName="ptjk"/></td>
					<td>
						<input type="text" name="morecvratio" maxlength="3" id="morecvratio" class="input_bd int percent" value=""/>
					</td>
					<td class="zhushi"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_spzh_8" defVal="(单位：%)，监控SP账号用户上行从EMP接收到的比率,取值范围(0~100)" fileName="ptjk"/></span></td>
				</tr>
				<tr>
					<td><emp:message key="ptjk_jkgl_tdzh_14" defVal="RPT滞留告警阀值：" fileName="ptjk"/></td>
					<td>
						<input type="text" name="rptremained" maxlength="9" id="rptremained" value="" class="input_bd int"/>
					</td>
					<td class="zhushi"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_spzh_9" defVal="(单位：条)，监控状态报告未转发至该SP账号业务系统最大滞留量" fileName="ptjk"/></span></td>
				</tr>
				<tr>
					<td><emp:message key="ptjk_jkgl_spzh_10" defVal="RPT最低接收率：" fileName="ptjk"/></td>
					<td>
						<input type="text" name="rptrecvratio" maxlength="3" id="rptrecvratio" class="input_bd int percent" value=""/>
						
					</td>
					<td class="zhushi"><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_spzh_11" defVal="(单位：%)，监控SP账号状态报告从EMP接收到的比率,取值范围(0~100)" fileName="ptjk"/></span></td>
				</tr>
			</table>
			<table  class="edit_spacct">
				<tr>
					<td colspan="4"><emp:message key="ptjk_jkgl_spzh_12" defVal="长时间未提交阀值：" fileName="ptjk"/><span style="color: #cccccc;font-size: 14px;" ><emp:message key="ptjk_jkgl_spzh_13" defVal="(规定时间段内超过设定的时长未接收到业务系统提交的信息则告警)" fileName="ptjk"/></span><input type="button" name="addcol" id="addcol" value="<emp:message key='ptjk_jkgl_spzh_tjh' defVal='添加行' fileName='ptjk'/>" onclick="addLine()" class="btnClass3"/></td>
				</tr>
				<tbody  id="tbody">
<%--				<tr>--%>
<%--					<td>时间段：--%>
<%--					<select id="selectfrom1" name="selectfrom1" style="width: 40px;" onchange="from_change(this)" >--%>
<%--						<option>0</option>--%>
<%--						<option>1</option>--%>
<%--						<option>2</option>--%>
<%--						<option>3</option>--%>
<%--						<option>4</option>--%>
<%--						<option>5</option>--%>
<%--						<option>6</option>--%>
<%--						<option>7</option>--%>
<%--						<option>8</option>--%>
<%--						<option>9</option>--%>
<%--						<option>10</option>--%>
<%--						<option>11</option>	--%>
<%--						<option>12</option>--%>
<%--						<option>13</option>--%>
<%--						<option>14</option>--%>
<%--						<option>15</option>--%>
<%--						<option>16</option>--%>
<%--						<option>17</option>--%>
<%--						<option>18</option>--%>
<%--						<option>19</option>--%>
<%--						<option>20</option>--%>
<%--						<option>21</option>--%>
<%--						<option>22</option>										--%>
<%--						</select>至	--%>
<%--						<select id="selectto1" name="selectto1" style="width: 40px;" onchange="to_change(this)">--%>
<%--						<option>1</option>--%>
<%--						<option>2</option>--%>
<%--						<option>3</option>--%>
<%--						<option>4</option>--%>
<%--						<option>5</option>--%>
<%--						<option>6</option>--%>
<%--						<option>7</option>--%>
<%--						<option>8</option>--%>
<%--						<option>9</option>--%>
<%--						<option>10</option>--%>
<%--						<option>11</option>	--%>
<%--						<option>12</option>--%>
<%--						<option>13</option>--%>
<%--						<option>14</option>--%>
<%--						<option>15</option>--%>
<%--						<option>16</option>--%>
<%--						<option>17</option>--%>
<%--						<option>18</option>--%>
<%--						<option>19</option>--%>
<%--						<option>20</option>--%>
<%--						<option>21</option>--%>
<%--						<option>22</option>--%>
<%--						<option>23</option>											--%>
<%--						</select>--%>
<%--						时长：--%>
<%--						<input type="text" name="duration1" maxlength="4" id="duration1" value="" class="input_bd int"/>--%>
<%--						<span style="color: #cccccc;font-size: 14px;" >(单位：分钟)</span>--%>
<%--					</td>--%>
<%--				</tr>--%>
				</tbody>
			</table>
			<table width="100%" style="text-align: center;margin-top: 20px;">
					<tr>
						<td>
							<input type="button" name="sub" id="sub" value="<emp:message key='ptjk_common_qd' defVal='确定' fileName='ptjk'/>" onclick="edit()" class="btnClass5 mr23"/>
							<input type="button" name="button" id="button" value="<emp:message key='ptjk_common_fh' defVal='返回' fileName='ptjk'/>" onclick="javascript:doreturn()" 
							class="btnClass6"/><br/>
						</td>
					</tr>
				</table>
		</div>
		
			
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
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=iPath%>/js/spAcctMon.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script src="<%=iPath%>/js/spAcctTime.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script src="<%=iPath%>/js/text.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript">
		var path='<%=path%>';
		$(document).ready(function() {
			getLoginInfo("#loginInfo");
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
			$('#search').click(function(){
				submitForm();
			});
			$("#editSpAcct").dialog({
				modal:true,
				autoOpen: false,
				width:750,
				height:520,
				def_height:520,
				resizable :false,
				open: function(){
					$("#monphone").keydown();
				},
				close:function()
				{
				}
			});
			$("#logintype").isSearchSelect({'width':'158','isInput':false,'zindex':0});
			$("#monstatus").isSearchSelect({'width':'158','isInput':false,'zindex':0});
			$('#tdhm_key').isSearchSelect({'width':'158','zindex':0},function(data){
				//keyup click触发事件
					$("#spaccountid").val(data.value);
			},function(data){
				//初始化加载
				var val=$("#spaccountid").val();
				if(val){
					data.box.input.val(val);
				}
				
			});
			$('#tdhm_key').next().find('.c_input').bind('click blur',function(){
				$("#spaccountType").val("1");
				});
			$('#tdhm_key').next().find('.c_result li').click(function(){
				$("#spaccountType").val("0");
				});
		});
	</script>
  </body>
</html>
