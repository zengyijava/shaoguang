<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="java.util.ArrayList"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.pasgroup.Userdata"%>
<%@page import="com.montnets.emp.entity.blacklist.PbListBlackDelrecord"%>
<%@ page import="com.montnets.emp.util.PhoneUtil" %>
<%@ page import="com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map

@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("blacklistSvt");
menuCode = menuCode==null?"0-0-0":menuCode;
@ SuppressWarnings("unchecked")
//List<PbListBlack> pbList = (List<PbListBlack>)request.getAttribute("blackList");
List<PbListBlackDelrecord> pbList = (List<PbListBlackDelrecord>)request.getAttribute("blackList");
@ SuppressWarnings("unchecked")
Map<Long, String> keyIdMap = (Map<Long, String>)request.getAttribute("keyIdMap");
@ SuppressWarnings("unchecked")
LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");
//@ SuppressWarnings("unchecked")
//List<XtGateQueue> xtList =(List<XtGateQueue>)session.getAttribute("mrXtList");
@ SuppressWarnings("unchecked")
List<Userdata> userList = (List<Userdata>)session.getAttribute("mrUserList");

String spgate = conditionMap.get("spgate");
@ SuppressWarnings("unchecked")
Map<String,String> busMap = (LinkedHashMap<String,String>)request.getAttribute("busMap");

String findResult= (String)request.getAttribute("findresult");
String uploadresult = (String)session.getAttribute("uploadresult");	
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
String txglFrame = skin.replace(commonPath, inheritPath);


String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
//修改号码
 String xghm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_xghm", request);
//新建短信黑名单
 String xjdxhmd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_xjdxhmd", request);
//确定
String qd = MessageUtils.extractMessage("common", "common_confirm", request);
//取消
 String qx = MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_qx", request);
//导入短信黑名单
 String drdxhmd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_drdxhmd", request);
//导出短信黑名单
 String dcdxhmd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_dcdxhmd", request);
//EXCEL格式
 String egs = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_egs", request);
 //TXT格式
 String tgs = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_tgs", request);

//添加显示列(删除时间、手机号码、用户id)
//删除时间
java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String scsj = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dlzhgl_scsj", request);
//操作用户id  txgl_wghdpz_dxhmd_czyhid
String czyhid = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_czyhid", request);

//手机号码
String sjhm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_sjhm", request);
if(sjhm!=null&&sjhm.length()>1){
	sjhm = sjhm.substring(0,sjhm.length()-1);
}
String recvtime = request.getParameter("recvtime");
String sendtime = request.getParameter("sendtime");
String username=request.getParameter("username");

//删除记录，用于定位
String scjl =  MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_247", request);
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/file.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/bla_blacklist.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="bla_blacklist">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,scjl) %>
			
			<div id="modify" title="<%=xghm %>" class="modify">
					<emp:message key="txgl_wghdpz_dxhmd_sjhm" defVal="手机号码：" fileName="txgl"></emp:message><input type="text" value=""  id="mobile1" maxlength="11" onkeyup="numberControl($(this))" onblur="numberControl($(this))"/>
					<input type="hidden" value="" id="blId"/>
					<input type="hidden" value="" id="spisuncm"/>
					<input type="hidden" value="" id="blBusCode"/>
					<input type="hidden" value="" id="blSpgate"/>
					<input type="hidden" id="pnoticeSize" name="pnoticeSize" value="<%=pbList != null ? pbList.size():0%>"/>
					<input type="hidden" id="totalSize" name="totalSize" value="<%=pageInfo != null ? pageInfo.getTotalRec():0%>"/>
			</div>
			<div id="uploadmmsBl" title="<%=drdxhmd %>" class="uploadmmsBl">
			<form name="updateInfo" method="post" action="bla_blacklistSvt.htm?method=update"  enctype="multipart/form-data">
			    <div id="blloginparams1" class="hidden"></div>   
				<div class="blloginparams1_down_div">
					<center>	 
					<table class="blloginparams1_down_div_table" >
					  <tr>
                         	<td>
								<span class="ywmc_span">
									<emp:message key="txgl_wghdpz_dxhmd_ywmc" defVal="业务名称：" fileName="txgl"></emp:message>
								</span>
							</td>
                         	<td>
                         		<select id="uploadbusCode" name="uploadbusCode" class="input_bd uploadbusCode" >
                         		<option value=""><emp:message key="txgl_wghdpz_dxhmd_qxzywmc" defVal="请选择业务名称" fileName="txgl"></emp:message></option>
                         		 <%
                         		if(busMap != null && busMap.size() > 0)
                         		{
                         		  Object s[] = busMap.keySet().toArray();
                         		
                         		 for(int i=0;i<busMap.size();i++)
                         		 {
                         		 %>
                         			<option value="<%=s[i]%>" ><%=busMap.get(s[i]).toString().replace("<","&lt;").replace(">","&gt;") %>(<%=s[i] %>)</option>
                         		<%} }%>
                         		</select>
                         	</td>
                      </tr>
                     <tr class="xzwj_up_tr"><td colspan="3"></td></tr>
  					 <tr>
	   				 	 <td><span class="xzwj_span"><emp:message key="txgl_wghdpz_dxhmd_xzwj" defVal="选择文件：" fileName="txgl"></emp:message></span></td>
	   					 <td>
	   					 <div>
	                        <a href="javascript:;" class="file"><emp:message key='wxgl_button_14' defVal='浏览' fileName='wxgl'/>
								<input type="file" name="phone1" id="phone1">
							</a>
	                        <p id="filename"></p>
                         </div> 
	 				 </tr>
					 <tr class="zmh_up_tr"><td colspan="3"></td></tr>
					 <tr>
						<td class="<%="zh_HK".equals(empLangName)?"zmh_td1":"zmh_td2"%>"  ><emp:message key="txgl_wgqdpz_spzhczhs_z" defVal="注：" fileName="txgl"></emp:message></td>
						<td class="z1zcetwjgssc_td"><emp:message key="txgl_wghdpz_dxhmd_z1zcetwjgssc" defVal="1、支持Excel、txt文件格式上传，单次上传不超过20万；" fileName="txgl"></emp:message></td>
					 </tr>
					 <tr>
						<td></td>
						<td class="2xtzdglgsbzq_td"><emp:message key="txgl_wghdpz_dxhmd_2xtzdglgsbzq" defVal="2、系统自动过滤格式不正确或不属于运营商号段的手机号码；" fileName="txgl"></emp:message></td>
					 </tr>
					 <tr>
						<td></td>
						<td class="3xtzdglxtywlx_td"><emp:message key="txgl_wghdpz_dxhmd_3xtzdglxtywlx" defVal="3、系统自动过滤相同业务类型中重复的手机号码。" fileName="txgl"></emp:message></td>
					 </tr>
					</table>
					</center>
				</div>
				<div class="impfont_div">
				    <center>
				    <font id="impfont" color="#F1F1F9;" class="impfont"><emp:message key="txgl_wghdpz_dxhmd_zzdrzqsh" defVal="正在导入中,请稍后..." fileName="txgl"></emp:message></font>
		    		<input id="bloks" class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="javascript:checkVal('upload')"/>
				    <input id="blcs" onclick="javascript:btcancel();" class="btnClass6" type="button" value="<%=qx %>" />
				    <br/>
				    </center>
		    	</div>
		    	</form>
			</div>
			<div id="exportDiv" title="<%=dcdxhmd %>" class="exportDiv">
				<span class="<%="zh_HK".equals(empLangName)?"excelBut_span1":"excelBut_span2"%>"  >
					<input id="excelBut" class="btnClass4" type="button" value="<%=egs %>" onclick="javascript:exportExcel()"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input id="txtBut" class="btnClass4" type="button" value="<%=tgs %>" onclick="javascript:exportTxt()"/>
				</span>
			<span >
			<font id="outfont" color="#F1F1F9;" class="<%="zh_HK".equals(empLangName)?"outfont1":"outfont2"%>"  ><emp:message key="txgl_wghdpz_dxhmd_zzdczqsh" defVal="正在导出中,请稍后..." fileName="txgl"></emp:message></font>
			</span>
			<span>
			<font class="zhu qxzdcgscg"  ><emp:message key="txgl_wghdpz_dxhmd_qxzdcgscg" defVal="请选择导出格式，超过50万以上数据，请您选择TXT格式。" fileName="txgl"></emp:message></font>
			</span>
			</div>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent" >
			<form name="pageForm" action="bla_blacklistSvt.htm?method=findAllDelete" method="post"
					id="pageForm">
					<div class="buttons">
					    <div id="toggleDiv">							
						</div>
						<span id="backgo" class="right mr5" onclick="javascript:showback()">&nbsp;<emp:message key="dxzs_xtnrqf_button_9" defVal="返回" fileName="dxzs"/></span>
					</div>
					<div id="condition">
						<table>
							<tr>
							   <td>
							        <emp:message key="dxzs_xtnrqf_title_248" defVal="操作时间" fileName="dxzs"/>：
							   </td>
							   <td>
							    	<input type="text"
										readonly="readonly" class="Wdate" onclick="stime()" 
										value="<%= sendtime== null?"":sendtime %>"  id="sendtime" name="sendtime"
											 <%--onchange="onblourSendTime('end')" --%>>
							   </td>
							   <td><emp:message key="dxzs_xtnrqf_title_135" defVal="至" fileName="dxzs"/>：</td>
							   <td>
							      	<input type="text"
										readonly="readonly" class="Wdate"  onclick="rtime()"
										value="<%=recvtime==null?"":recvtime %>" 
										 id="recvtime" name="recvtime" <%--onchange="onblourSendTime('end')" --%>>
							    </td>
	                         	<td>
									 <emp:message key="txgl_wghdpz_dxhmd_czyhid" defVal="操作用户ID：" fileName="txgl"/>：
	                         	</td>
	                         	<td><input type="text" id="username" name ="username" value="<%=username== null?"":username %>"></td>
								<td> 
									 <emp:message key="txgl_wghdpz_dxhmd_sjhm" defVal="手机号码：" fileName="txgl"></emp:message>
								</td>
								<td>

									<input name="phone" id="phone" type="text"  class="phone" onkeyup="phoneInputCtrl($(this))" onblur="phoneInputCtrl($(this))"
										 maxlength="21" value="<%=request.getParameter("phone")==null?"":request.getParameter("phone") %>"/>
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
						           
									<th>
									    <%-- 操作用户id --%>
										<%=czyhid %>
									</th>
									<th>
									    <%-- 手机号码 --%>
										<%=sjhm %>
									</th>
									<th>
									    <%-- 删除时间 --%>
										<%=scsj %>
									</th>
						        </tr>
							</thead>
							<tbody>
							<%
							if (pbList != null && pbList.size() > 0)
							{
                                PhoneUtil phoneUtil = new PhoneUtil();
                                String[] haoduan=new WgMsgConfigBiz().getHaoduan();
                                String keyId;
								for (PbListBlackDelrecord pb : pbList)
								{
	                                keyId = keyIdMap.get(pb.getId());
							%>
							<tr>
								<td>
									<%-- <input type="checkbox" name="checklist" value="<%=keyId %>" /> --%>
									<%=pb.getOperateId() %><%-- 操作用户id --%>
								</td>

								<td>
       								<%-- 手机号 去掉 class="textalign" --%>
                                    <%
                                    String phone =    String.valueOf(pb.getPhone()) ;
                                    if(phoneUtil.getPhoneType(phone, haoduan)+1==0){
                                        phone = "00"+phone;
                                    }
                                    %>
									<label id="la<%=pb.getId() %>"><%=phone%></label>
								</td>
								<%-- 删除时间 --%>
								<td >
								   <%=df.format(pb.getOptTime()) %>
								</td>
							</tr>
							<%     }
								}else{
							%>
							<tr><Td colspan="4"><emp:message key="txgl_wghdpz_gjzsz_wjl" defVal="无记录" fileName="txgl"></emp:message></Td></tr><%} %>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="4">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
					 <div id="blloginparams" class="hidden"></div>
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
        <script type="text/javascript">
            var cond_phone = '<%=request.getParameter("phone")==null?"":request.getParameter("phone") %>';
            var cond_buscode = '<%=conditionMap.get("svrType")==null?"":conditionMap.get("svrType")%>';
        </script>
        <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/empSelect.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<%-- 	<script type="text/javascript" src="<%=iPath %>/js/blackdetelelist.js?V=<%=StaticValue.JSP_IMP_VERSION%>"></script> --%>
        <%-- 引入插件 --%>
		<script type="text/javascript">
	    var maxCount = "<%=StaticValue.getBlackMaxcount()%>";
	    //结束时间设置
	    function rtime() {
	        var min = "1900-01-01 00:00:00";
	        var max = "2099-12-31 23:59:59";
	        var sendtime = $("#sendtime").val();
	        WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:sendtime||min,maxDate:max,enableInputMask:false});
	    }

	    //起始时间设置
	    function stime(){
	    var max = "2099-12-31 23:59:59";
	    var v = $("#recvtime").attr("value");
	    var min = "1900-01-01 00:00:00";
		//if(v.length != 0)
		//{
		//    max = v;
		//    var year = v.substring(0,4);
		//	var mon = v.substring(5,7);
		//	if (mon != "01")
		//	{
		//	    mon = String(parseInt(mon,10)-1);
		//	    if (mon.length == 1)
		//	    {
		//	        mon = "0"+mon;
		//	    }
		//	}
		//	else
		//	{
		//	    year = String((parseInt(year,10)-1));
		//	    mon = "12";
		//	}
		//	min = year+"-"+mon+"-01 00:00:00"
		//}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});
	    }
	    function showback()
		{
		   var lgcorpcode =$("#lgcorpcode").val();
		   var lguserid =$("#lguserid").val();
		   location.href="bla_blacklistSvt.htm?method=find&isback=1&lgcorpcode="+lgcorpcode+"&lguserid="+lguserid;
		}

		$(document).ready(function() {
		    getLoginInfo("#blloginparams");
		    getLoginInfo("#blloginparams1");
		    var findresult="<%=findResult%>";
		    
		    if(findresult=="-1")
		    {
		       //alert("加载页面失败,请检查网络是否正常!");	
		       alert(getJsLocaleMessage("txgl","txgl_wghdpz_gjzsz_text_1"));
		       return;			       
		    }
			noquot("#phone");
			
			 var uploadresult = "<%=uploadresult%>";
			
			if(uploadresult.indexOf("true") == 0)
			{
				//alert("新建成功,有效黑名单号码个数为："+uploadresult.substr(4)+"！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_1")+uploadresult.substr(4)+"！");
				black();
			}
			else if( uploadresult =="noPhone")
			{
				//alert("没有有效的黑名单记录可以添加！");
				 alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_2"));
			}else if( uploadresult =="overCount")
			{
				//alert("上传号码个数超过20万，请分批次重新上传！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_3"));
			}else if(uploadresult == "false"){
				//alert("新建失败！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_4"));
			}else if(uploadresult == "outCount")
			{
				//alert("企业黑名单总数超过"+maxCount+"个，不允许再添加！");
				alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_5")+maxCount+getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_6"));
			}
		    <%session.removeAttribute("uploadresult");%>
			
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
				var phone = $.trim($("#phone").val());
				if(phone.length>0&&!checkPhone(phone) )
				{
					//alert("请输入合法的手机号码进行查询！");
					alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_7"));
					return;
				}
				submitForm();
			});
		});
	
		</script>
	</body>
</html>
