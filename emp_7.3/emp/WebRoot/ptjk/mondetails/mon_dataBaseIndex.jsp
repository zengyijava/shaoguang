<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.monitor.constant.MonitorStaticValue"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
String path = request.getContextPath();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("dataBaseMon");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
		
String procename = StringUtils.defaultIfEmpty(request.getParameter("procename"),"");
String procenode = StringUtils.defaultIfEmpty(request.getParameter("procenode"),"");
String evttype = StringUtils.defaultIfEmpty(request.getParameter("evttype"),"");
String dbconnectstate = StringUtils.defaultIfEmpty(request.getParameter("dbconnectstate"),"");
String addopr = StringUtils.defaultIfEmpty(request.getParameter("addopr"),"");
String procetype = StringUtils.defaultIfEmpty(request.getParameter("procetype"),"");
String dispopr = StringUtils.defaultIfEmpty(request.getParameter("dispopr"),"");
String delopr = StringUtils.defaultIfEmpty(request.getParameter("delopr"),"");
String modiopr = StringUtils.defaultIfEmpty(request.getParameter("modiopr"),"");
String monstatus = StringUtils.defaultIfEmpty(request.getParameter("monstatus"),"");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title></title>
    <%@include file="/common/common.jsp" %>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	<link rel="stylesheet" href="<%=iPath%>/css/condition.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" />
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
		<form name="pageForm" action="mon_dataBaseMon.htm" method="post" id="pageForm">
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv">
				</div>
                <a id="add" onclick="javascript:toSet()" style="line-height: 30px; /*background-position: -635px 0px;*/"><emp:message key="ptjk_common_sz" defVal="设置" fileName="ptjk"/></a>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td><emp:message key="ptjk_jkxq_sjk_cxm_mh" defVal="程序名：" fileName="ptjk"/></td>
							<td>
								<input type="text" name="procename" id="procename" value="<%=procename%>"/>
							</td>
							<td><emp:message key="ptjk_jkxq_sjk_ssjd_mh" defVal="所属节点：" fileName="ptjk"/></td>
							<td>
								<input type="text" name="procenode" id="procenode" value="<%=procenode%>"/>
							</td>
							<td><emp:message key="ptjk_common_gjjb_mh" defVal="告警级别：" fileName="ptjk"/></td>
							<td>
								<select id="evttype" name="evttype" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="0"><emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></option>
                                    <option value="2"><emp:message key="ptjk_common_yz" defVal="严重" fileName="ptjk"/></option>
								</select>
							</td>
							<td class="tdSer">
							     <center><a id="search"></a></center>
						    </td>	
						<tr>	
							<td>
								<emp:message key="ptjk_jkxq_sjk_1" defVal="数据库连接状态：" fileName="ptjk"/>
							</td>
							<td>
								<select id="dbconnectstate" name="dbconnectstate" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="0"><emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></option>
									<option value="1"><emp:message key="ptjk_common_dk" defVal="断开" fileName="ptjk"/></option>
								</select>
							</td>
							<td><emp:message key="ptjk_jkxq_sjk_2" defVal="增加操作状态：" fileName="ptjk"/></td>
							<td>
								<select id="addopr" name="addopr" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="0"><emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></option>
									<option value="1"><emp:message key="ptjk_common_sb" defVal="失败" fileName="ptjk"/></option>
								</select>
							</td>
							<td><emp:message key="ptjk_common_cxlx_mh" defVal="程序类型：" fileName="ptjk"/></td>
							<td>
								<select id="procetype"  name="procetype" isInput="false">
									<option value="" selected="selected"><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="5000"><emp:message key="ptjk_common_webcx" defVal="WEB程序" fileName="ptjk"/></option>
									<option value="5200"><emp:message key="ptjk_common_empwg" defVal="EMP网关" fileName="ptjk"/>(EMP_GW)</option>
									<option value="5300"><emp:message key="ptjk_common_yysjk" defVal="运营商接口" fileName="ptjk"/>(SPGATE)</option>
									<%--<option value="5800">文件服务器</option>--%>
								</select>
							</td>
							<td></td>
						</tr>
						<tr>	
							<td ><emp:message key="ptjk_jkxq_sjk_4" defVal="查询操作状态：" fileName="ptjk"/></td>
							<td>
								<select id="dispopr" name="dispopr" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="0"><emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></option>
									<option value="1"><emp:message key="ptjk_common_sb" defVal="失败" fileName="ptjk"/></option>
								</select>
							</td>
							<td ><emp:message key="ptjk_jkxq_sjk_5" defVal="删除操作状态：" fileName="ptjk"/></td>
							<td>
								<select id="delopr" name="delopr" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="0"><emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></option>
									<option value="1"><emp:message key="ptjk_common_sb" defVal="失败" fileName="ptjk"/></option>
								</select>
							</td>
							<td ><emp:message key="ptjk_jkxq_sjk_3" defVal="修改操作状态：" fileName="ptjk"/></td>
							<td>
								<select id="modiopr" name="modiopr" isInput="false">
									<option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
									<option value="0"><emp:message key="ptjk_common_zc" defVal="正常" fileName="ptjk"/></option>
									<option value="1"><emp:message key="ptjk_common_sb" defVal="失败" fileName="ptjk"/></option>
								</select>
							</td>
							<td></td>
						</tr>
                    <tr>
                        <td><emp:message key="ptjk_common_jkzt_mh" defVal="监控状态：" fileName="ptjk"/></td>
                        <td>
                            <select id="monstatus" name="monstatus" isInput="false">
                                <option value=""><emp:message key="ptjk_common_qb" defVal="全部" fileName="ptjk"/></option>
                                <option value="0"><emp:message key="ptjk_common_wjk" defVal="未监控" fileName="ptjk"/></option>
                                <option value="1"><emp:message key="ptjk_common_jk" defVal="监控" fileName="ptjk"/></option>
                            </select>
                        </td>
                        <td colspan="5"></td>
                    </tr>
					</tbody>
				</table>
			</div>
			<div id="info"></div>
		</form>
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
	<div id="setDataBaseDiv" title="<emp:message key='ptjk_jkxq_sjk_6' defVal='设置数据库监控信息' fileName='ptjk'/>" style="padding:5px;display:none">

        <table style="margin: 12px 12px" cellpadding="6px">
            <tr>
                <td width="80px"><emp:message key="ptjk_common_gjsjh_mh" defVal="告警手机号：" fileName="ptjk"/></td>
                <td>
                    <textarea name="monphone" id="monphone" class="input_bd" style="width: 200px;"></textarea>
                </td>
                <td style="padding-left: 12px;">
                    <font class="til"><span style="color: #cccccc;font-size: 14px;"><emp:message key="ptjk_jkxq_sjk_7" defVal="(可以设置至多十个手机号，号码间用逗号“,”分隔)" fileName="ptjk"/></span></font>
                </td>
            </tr>
            <tr>
                <td><emp:message key="ptjk_common_gjyx_mh" defVal="告警邮箱：" fileName="ptjk"/></td>
                <td>
                    <input name="monemail" id="monemail" class="input_bd" style="width:200px;" maxlength="128" />
                </td>
                <td style="padding-left: 12px;">
                    <font class="til"><span style="color: #cccccc;font-size: 14px;"><emp:message key="ptjk_common_znszygyx" defVal="只能设置一个邮箱" fileName="ptjk"/></span></font>
                </td>
            </tr>
            <tr>
                <td colspan="3" style="line-height: 30px;">
                    <font class="til"><span style="color: #cccccc;font-size: 14px;"><emp:message key="ptjk_jkxq_sjk_8" defVal="注：告警手机号和邮箱通用于所有数据库监控" fileName="ptjk"/></span>
                    </font></td>
            </tr>
        </table>
        <div style="text-align: center;margin-top: 20px;margin-bottom: 12px;">
            <input type="button" name="sub" id="sub" value="<emp:message key='ptjk_common_qd' defVal='确定' fileName='ptjk'/>" onclick="addSetting()" class="btnClass5 mr23" />
            <input type="button" name="button" id="button" value="<emp:message key='ptjk_common_fh' defVal='返回' fileName='ptjk'/>" onclick="javascript:doreturn()" class="btnClass6" /><br>
        </div>
    </div>
    <div class="clear"></div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/ptjk_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/monPageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=inheritPath%>/monmanage/js/text.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript">
	$(document).ready(function() {
        var evttype = "<%=evttype%>";
        var dbconnectstate = "<%=dbconnectstate%>";
        var addopr = "<%=addopr%>";
        var procetype = "<%=procetype%>";
        var dispopr = "<%=dispopr%>";
        var delopr = "<%=delopr%>";
        var modiopr = "<%=modiopr%>";
        var monstatus = "<%=monstatus%>";
        $('#evttype').children('option[value="'+evttype+'"]').attr('selected',true);
        $('#dbconnectstate').children('option[value="'+dbconnectstate+'"]').attr('selected',true);
        $('#addopr').children('option[value="'+addopr+'"]').attr('selected',true);
        $('#procetype').children('option[value="'+procetype+'"]').attr('selected',true);
        $('#dispopr').children('option[value="'+dispopr+'"]').attr('selected',true);
        $('#delopr').children('option[value="'+delopr+'"]').attr('selected',true);
        $('#modiopr').children('option[value="'+modiopr+'"]').attr('selected',true);
        $('#monstatus').children('option[value="'+monstatus+'"]').attr('selected',true);
		getLoginInfo("#loginInfo");
		$('#search').click(function(){
			//点击查询显示第一页
			$("#txtPage").val('1');
			submitForm();
		});
		//定时刷新时间
		refreshTime = <%=MonitorStaticValue.getRefreshTime()%>;
		//window.clearInterval();
		//定时刷新
		reTimer=window.setInterval("submitForm()",refreshTime);
		$("#evttype,#dbconnectstate,#addopr,#procetype,#dispopr,#delopr,#modiopr,#monstatus").isSearchSelect({'width':'152','isInput':false,'zindex':0});
		submitForm();
		setInterval("refresh()",15*60*1000);

        $("#setDataBaseDiv").dialog({
            title: getJsLocaleMessage("ptjk","ptjk_jkxq_sjk_1"),
            autoOpen: false,
            width: 540,
            resizable: false,
            closeOnEscape: false,
            modal: true,
            close: function() {}
        });
    });
	function submitForm()
	{
		var search = document.getElementById('search');
		if(search)
		{
			search.isClick = true;
		}
		window.parent.loading();

        var data = $('#pageForm').serializeObject();
        $.extend(data,{method:'getInfo'},{time:new Date()});
        $('#p_jump_menu').remove();
		$('#info').load("mon_dataBaseMon.htm",data,function(){});
	}

	function refresh(){
			$("#pageForm").submit();
		}

	function toSet(){
        $.ajax({
            type: "POST",
            url: "mon_dataBaseMon.htm",
            async:false,
            data: {method: 'getMon',isAsync:"yes"},
            success: function (data) {
                if(data == "outOfLogin")
                {
                    location.href="common/logoutEmp.html";
                    return;
                }

                if(data.length > 0)
                {
                    data = eval('('+data+')');
                    $("#monphone").val(data.monphone);
                    $("#monphone").keyup();
                    $("#monemail").val(data.monemail);
                }else {
                    $("#monphone").val('');
                    $("#monemail").val('');
                }

            }
        });
        $("#setDataBaseDiv").dialog("open");
    };

    function addSetting(){
        if(!validatePhones($('#monphone'))){
            return;
        }
        var monemail = $.trim($('#monemail').val());
        var monphone = $.trim($('#monphone').val());

        if(monemail.length != 0 && !checkEmail(monemail)){
            alert(getJsLocaleMessage("ptjk","ptjk_jkxq_sjk_2"));
            return;
        }

        if((monemail+monphone).length == 0){
            if(!confirm(getJsLocaleMessage("ptjk","ptjk_jkxq_sjk_3"))){
                return;
            }
        }

        $.ajax({
            type: "POST",
            url: "mon_dataBaseMon.htm",
            data: {method: 'setMon',monphone:monphone,monemail:monemail,isAsync:"yes"},
            beforeSend: function () {
                page_loading();
            },
            complete: function () {
                page_complete();
            },
            success: function (data) {
                if(data == "outOfLogin")
                {
                    location.href="common/logoutEmp.html";
                    return;
                }

                if(data == 'true')
                {
                    $("#setDataBaseDiv").dialog('close');
                    alert(getJsLocaleMessage("ptjk","ptjk_jkxq_sjk_4"));
                }else{
                    alert(getJsLocaleMessage("ptjk","ptjk_jkxq_sjk_5"));
                }

            }
        });
    }
    function doreturn()
    {
        $("#setDataBaseDiv").dialog("close");
    }
	</script>
  </body>
</html>
