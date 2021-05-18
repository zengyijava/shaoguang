<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.netnews.entity.LfWXDataType"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path=request.getContextPath();
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	//按钮权限
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@ SuppressWarnings("unchecked")
	List<DynaBean> trustDatas = (List<DynaBean>)request.getAttribute("wxDatas");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String,String> conditionMap = (LinkedHashMap)request.getAttribute("conditionMap");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	List<LfWXDataType> dataTypes=(List<LfWXDataType>)request.getAttribute("dataTypes");
	String rTitle = (String)request.getAttribute("rTitle");
	String menuCode = titleMap.get(rTitle);
	String result = (String)session.getAttribute("trustdataResult");
	String skin = session.getAttribute("stlyeSkin")==null?"default": (String)session.getAttribute("stlyeSkin");
	String langName = (String)session.getAttribute("emp_lang");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><%=titleMap.get(menuCode)%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=inheritPath %>/common/widget/jqueryui/css/jquery.ui.dialog.new.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin%>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" href="<%=iPath%>/css/trustData.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydwx_netManager.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>	
		<script type="text/javascript" src="<%=inheritPath%>/common/i18n/<%=langName%>/ydwx_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript">
	
	function show(){
		var res = <%=result%>;
		var czchg = getJsLocaleMessage("ydwx","ydwx_common_czchg");
		var czshb = getJsLocaleMessage("ydwx","ydwx_common_czshb");
		if(res != null && res !=""){
			<%session.removeAttribute("trustdataResult");%>
			if(res){
				alert(czchg);
			}else {
				alert(czshb);
			}
		}
		
	}
</script>

</head>
<body onload="show();" id="ydwx_trustData">
<%
	if(CstlyeSkin.contains("frame4.0")){
%>
	<input id='hasBeenBind' value='1' type='hidden'/>
<%
	}
 %>
<div id="container" class="container">
	<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
	<%-- header结束 --%>
		<div id="rContent" class="rContent">
		<%if(btnMap.get(menuCode+"-0")!=null){%>
		<form name="pageForm" action="<%=path%>/wx_trustdata.htm" method="post" id="pageForm">
		<div id="loginUser" class="hidden"></div>
			<input type="hidden" id="pathUrl" value="<%=path%>" />
				<div class="buttons">
				<div id="toggleDiv"> </div>
				<%if(btnMap.get(menuCode+"-1")!=null){%>
				<a id="add" onclick="addTrustData();"><font><emp:message key="ydwx_common_btn_xinjian" defVal="新建" fileName="ydwx"></emp:message></font></a>
				<%} %>
			</div>
			
	<div id="condition">
		<table width="100%">
			<tr>
				<td>
					<span><emp:message key="ydwx_wxgl_hdxgl_bianhao" defVal="编号：" fileName="ydwx"></emp:message></span>
				</td>
				<td>
					<input type="text" name="code" onkeyup="numberControl($(this))" onblur="numberControl($(this))" id="code" class="ydwx_code" value="<%=conditionMap.get("code") == null?"":conditionMap.get("code") %>" />
				</td>
				<td>
					<span><emp:message key="ydwx_wxgl_hdxgl_hdxmc" defVal="互动项名称：" fileName="ydwx"></emp:message></span>
				</td>
				<td>
					<input type="text" name="name" id="name" class="ydwx_name"  value="<%=conditionMap.get("name") == null?"":conditionMap.get("name") %>" />
				</td>
				
				<td><emp:message key="ydwx_wxgl_hdxgl_cjr" defVal="创建人：" fileName="ydwx"></emp:message></td>
				<td>
				<input type="text" id="chUser" name="chUser" class="ydwx_chUser" value="<%=conditionMap.get("chUser")== null?"":conditionMap.get("chUser") %>">
				</td>
					<td class="tdSer">
					<center><a id="search"></a></center>
					</td>
			</tr>
			<tr>
				<td>
					<span><emp:message key="ydwx_wxgl_hdxgl_leibei" defVal="类别：" fileName="ydwx"></emp:message></span>
				</td>
				<td>
					<select name="dataType" id="dataType" class="ydwx_dataType">
						<option value=""><emp:message key="ydwx_wxgl_quanbu" defVal="全部" fileName="ydwx"></emp:message></option>
						<%for(LfWXDataType dataType:dataTypes){%>
								<option value="<%=dataType.getId() %>" <%if(String.valueOf(dataType.getId()).equals(conditionMap.get("dataType"))) {%>selected="selected"<%}%>><xmp><%=dataType.getName() %></xmp></option>
								<% } %>
					</select>
				</td>
				 <td><emp:message key="ydwx_common_time_chuangjianshijians" defVal="创建时间：" fileName="ydwx"></emp:message></td>
				 <td>
					<input type="text" name="chDate" id="chDate"
						class="Wdate ydwx_chDate" readonly="readonly" onclick="stime()"
						value="<%=conditionMap.get("chDate")== null?"":conditionMap.get("chDate") %>" />
						
				 </td>	
				 <td><emp:message key="ydwx_common_time_zhi" defVal="至：" fileName="ydwx"></emp:message></td>
				 <td><input type="text" name="chEndDate" id="chEndDate"
						class="Wdate ydwx_chEndDate" readonly="readonly" onclick="rtime()"
						value="<%=conditionMap.get("chEndDate")== null?"":conditionMap.get("chEndDate") %>"/></td>		    
				 <td></td>		    
			</tr>
		</table>
	</div>
			<table id="content">
				<thead>
				  <tr>
				  		<th><emp:message key="ydwx_common_bianhao" defVal="编号" fileName="ydwx"></emp:message></th>
						<th><emp:message key="ydwx_wxgl_hdxgl_hdxmcs" defVal="互动项名称" fileName="ydwx"></emp:message></th>
						<th><emp:message key="ydwx_wxgl_lx" defVal="类型" fileName="ydwx"></emp:message></th>
						<th><emp:message key="ydwx_wxgl_hdxgl_neirong" defVal="内容" fileName="ydwx"></emp:message></th>
						<th><emp:message key="ydwx_wxgl_hdxgl_leibeis" defVal="类别" fileName="ydwx"></emp:message></th>
						<th><emp:message key="ydwx_wxgl_cjr" defVal="创建人" fileName="ydwx"></emp:message></th>
						<th><emp:message key="ydwx_wxgl_hdxgl_ssjg" defVal="所属机构" fileName="ydwx"></emp:message></th>
						<th width="140px"><emp:message key="ydwx_common_time_chuangjianshijian" defVal="创建时间" fileName="ydwx"></emp:message></th>
						<%
						int colspan = 0;
						if(btnMap.get(menuCode+"-1") != null){
							colspan ++;
						}
						if(btnMap.get(menuCode+"-2") != null){
							colspan ++;											
						}
						if(btnMap.get(menuCode+"-3") != null){
							colspan ++;
						}
						if(colspan > 0){%>
						<th  <%out.print(" colspan="+String.valueOf(colspan));%>>
							<emp:message key="ydwx_common_caozuo" defVal="操作" fileName="ydwx"></emp:message>
						</th>
						<%}%>
				  </tr>
				</thead>
					<tbody>
					<%
					DynaBean data = null;
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(trustDatas != null && trustDatas.size()>0){
						for(int v=0;v<trustDatas.size();v++){
							 data=trustDatas.get(v);
									%>
								<tr>
									<td><%=data.get("code")==null?"":data.get("code") %></td>
									<td><%=data.get("name")==null?"":data.get("name") %></td>
									<td><%
									String strQuestype = data.get("questype").toString();
									Short questype = Short.valueOf(strQuestype);
									if(questype == 1){%>
									<emp:message key="ydwx_wxgl_hdxgl_huida" defVal="回答类" fileName="ydwx"></emp:message>
									<%}else if(questype == 2){%>
									<emp:message key="ydwx_wxgl_hdxgl_danxuan" defVal="单选类" fileName="ydwx"></emp:message>
									<%}else if(questype == 3){%>
									<emp:message key="ydwx_wxgl_hdxgl_duoxuan" defVal="多选类" fileName="ydwx"></emp:message>
									<%}else{%>
									<emp:message key="ydwx_wxgl_hdxgl_xialakuang" defVal="下拉框类" fileName="ydwx"></emp:message>
									<% }%></td>
									<td>
									<a onclick="javascript:Look(<%=data.get("did") %>)"><emp:message key="ydwx_common_btn_chakan" defVal="查看" fileName="ydwx"></emp:message>
								  	</a> 
									</td>
									<td><%=data.get("typename")==null?"":data.get("typename") %></td>
									<td><%=data.get("username")==null?"":data.get("username") %></td>
									<td><%=data.get("depname")==null?"":data.get("depname") %></td>
									<td>
									<%if(data.get("creatdate")==null||"".equals(data.get("creatdate"))){ %>
									- <%	}else{ %>
									 <%=df.format(data.get("creatdate"))%><%
									} %></td>
									<%if(btnMap.get(menuCode+"-3")!=null){%>
									<td nowrap="nowrap">
									
									
									<%--  已经被引用，那么就不能修改，否则，可以被修改  may --%>
									<% if(data.get("binddid")!=null){%>
									<emp:message key="ydwx_common_btn_xiugai" defVal="修改" fileName="ydwx"></emp:message>
									<%}else{
									%>
									<a onclick="javascript:editTrustData(<%=data.get("did") %>,'edit')"><emp:message key="ydwx_common_btn_xiugai" defVal="修改" fileName="ydwx"></emp:message></a>
									<% }%>
									
									
									</td>
									<%} %>
									<%if(btnMap.get(menuCode+"-1")!=null){%>
									<td nowrap="nowrap">
										<a onclick="javascript:editTrustData(<%=data.get("did") %>,'copy')"><emp:message key="ydwx_common_btn_fuzhi" defVal="复制" fileName="ydwx"></emp:message></a>
									</td>
									<%} %>
									<%if(btnMap.get(menuCode+"-2")!=null){%>
									<td nowrap="nowrap" >
										<a href="javascript:trustDel(<%=data.get("did") %>);"><emp:message key="ydwx_common_btn_shanqu" defVal="删除" fileName="ydwx"></emp:message></a>
									</td>
									<%} %>
								</tr>
						<%} 
						}else {%>
						
						<tr><td colspan="<%=colspan+8 %>"><emp:message key="common_norecord" defVal="无记录" fileName="common"></emp:message></td></tr>
					<%} %>
				</tbody>
				<tfoot>
						<tr>
						<td colspan="<%=colspan+8 %>">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
		</table>
		</form>
		<%} %>
		</div>
		<div id="divBox" class="ydwx_divBox" style="display:none" title="<emp:message key="ydwx_wxgl_hdxgl_neirong" defVal="内容" fileName="ydwx"></emp:message>">
			<div class="ydwx_divBox_sub">
				<iframe class="ydwx_iframe" id="nm_preview_common" src=""></iframe>	
			</div>
		</div> 
			<%-- 内容结束 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
					<div id="bottom_main">
					</div>
				</div>
			</div>
			<%-- foot结束 --%>
		<div class="clear"></div>
		</div>
<%-- 加载JS 文件 --%>
<script type="text/javascript">
	$(document).ready(function() {
		getLoginInfo("#loginUser");
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		
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
		
		$('#search').click(function(){submitForm();});
		
		 $("#divBox").dialog({
			autoOpen: false,
			height:510,
			width: 300,
			modal: true,
			close:function(){
			}
		});
	});

	function Look(dId){	
	    $.post('wx_trustdata.htm?method=previewContent',{dId:dId},function(data){
	    	$(document.getElementById('nm_preview_common').contentWindow.document.body).html(data);
	      $("#divBox").dialog("open");
       });
    }
    
	function trustDel(trustID){
		var nqdshchgjl=getJsLocaleMessage("ydwx","ydwx_common_nqdshchgjl");
		var ghdxybyybyxshch=getJsLocaleMessage("ydwx","ydwx_hdxgl_ghdxybyybyxshch");
		var shchchg=getJsLocaleMessage("ydwx","ydwx_common_shchchg");
		var shchshb=getJsLocaleMessage("ydwx","ydwx_common_shchshb");
		if(confirm(nqdshchgjl)){
			$.post("<%=path%>/wx_trustdata.htm?method=trustDel",{trustID:trustID},function(result){
				if(result == "dataBind"){
					alert(ghdxybyybyxshch);
				}else if(result == "true"){
					alert(shchchg);
					submitForm();
				}else{
					alert(shchshb);
				}
			});
		}
	}
	
  	function addTrustData(){
  		var lgcorpcode = $("#lgcorpcode").val();
        window.location.href="<%=path%>/wx_trustdata.htm?method=toEdit&lgcorpcode="+lgcorpcode;
    } 
    
    function editTrustData(dId,optype){
  		var lgcorpcode = $("#lgcorpcode").val();
        window.location.href="<%=path%>/wx_trustdata.htm?method=toEdit&lgcorpcode="+lgcorpcode+
        "&optype="+optype+"&dId="+dId;
    } 
	
	function rtime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#chDate").attr("value");
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
				max = year+"-"+mon+"-"+day+" 23:59:59"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
		
		};

	function stime(){
	    var max = "2099-12-31 23:59:59";
	    var v = $("#chEndDate").attr("value");
	    var min = "1900-01-01 00:00:00";
		if(v.length != 0)
		{
		    max = v;
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
			min = year+"-"+mon+"-01 00:00:00"
		}
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
	
	};	
	$(function(){
	  $('#dataType').isSearchSelect({'width':'180','isInput':false,'zindex':0});
   })
</script>	
</body>
</html>