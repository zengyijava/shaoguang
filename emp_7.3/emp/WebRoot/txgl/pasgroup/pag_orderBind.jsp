<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.servmodule.txgl.entity.AcmdRoute"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.Userdata"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>

<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<%
    String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
		
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//List<LfServiceVo> serList=(List<LfServiceVo>)request.getAttribute("serList");
	//java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("orderBind");
	menuCode = menuCode==null?"0-0-0":menuCode;

	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	
	@ SuppressWarnings("unchecked")
	List<Userdata> userList = (List<Userdata>)request.getAttribute("spUserList");
	@ SuppressWarnings("unchecked")
	List<AcmdRoute> LfMoTructVoList = (List<AcmdRoute>)request.getAttribute("LfMoTructVoList");
	@ SuppressWarnings("unchecked")
	Map<Long, String> keyIdMap = (Map<Long, String>)request.getAttribute("keyIdMap");
	String structcode = (String)request.getAttribute("structcode");
	String bussysname = (String)request.getAttribute("bussysname");
	if ( structcode == null ) structcode = "";
	if ( bussysname == null ) bussysname = "";
	String txglFrame = skin.replace(commonPath, inheritPath);
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	//指令代码
    String zldm = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zllypz_zldm", request);	
    if(zldm!=null&&zldm.length()>1){
    	zldm = zldm.substring(0,zldm.length()-1);
    }
	//业务系统名称
	String xtywmc = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zllypz_xtywmc", request);	
    if(xtywmc!=null&&xtywmc.length()>1){
    	xtywmc = xtywmc.substring(0,xtywmc.length()-1);
    }
    //编辑业务指令绑定
    String bjywzlbd = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zllypz_bjywzlbd", request);
    //新增业务指令绑定
    String xzywzlbd = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zllypz_xzywzlbd", request);
    
%>
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_zllypz_sxywzlbd" defVal="上行业务指令绑定" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />

		<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/pag_orderBind.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/pag_orderBind.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="pag_orderBind">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<%-- 内容开始 --%>
			<form name="pageForm" action="pag_orderBind.htm?method=find" method="post"
					id="pageForm">
					<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
			<div id="rContent" class="rContent">
					<div class="buttons">
						<div id="toggleDiv">
						</div>
						<a id="add" href="javascript:showAddServiceBind()"><emp:message key="txgl_wgqdpz_qyhdgl_xj" defVal="新增" fileName="txgl"></emp:message></a>
						
					</div>
					<div id="condition">
						<table>
							<tbody>
								<tr>
									<td>
										<emp:message key="txgl_wgqdpz_zllypz_zldm" defVal="指令代码：" fileName="txgl"></emp:message>
									</td>
									<td>
									 	<input type="text" id = "structcode" name="structcode" value="<%=structcode %>"/>
									 
									</td>
									<td><emp:message key="txgl_wgqdpz_zllypz_xtywmc" defVal="业务系统名称：" fileName="txgl"></emp:message></td>
									<td>
									   	<input type="text" id = "bussysname" name = "bussysname" value="<%=bussysname %>"/>
									</td>
									<td class="tdSer">
									       <center><a id="search"></a></center>
								    </td>
								</tr>
							</tbody>
						</table>
					
				</div>
					<table id="content" cellspacing="0" cellpadding="0">
					<thead>
						<tr>
						    <th><emp:message key="txgl_wgqdpz_zllypz_zlmc" defVal="指令名称" fileName="txgl"></emp:message></th>
							<th><%=zldm %></th>
							<th><%=xtywmc %></th>
							<th><emp:message key="txgl_wgqdpz_zllypz_spzh" defVal="SP账号" fileName="txgl"></emp:message></th>
							<th><emp:message key="txgl_wgqdpz_yyshdgl_cz" defVal="操作" fileName="txgl"></emp:message></th>
						</tr>
					</thead>
					<tbody>
					<%
					   if(LfMoTructVoList!=null && LfMoTructVoList.size()>0){
					     for(int i=0;i<LfMoTructVoList.size();i++){
					       AcmdRoute moTructVo = LfMoTructVoList.get(i);
					       String keyId = keyIdMap.get(moTructVo.getId());
					 %>
						<tr>
						    <td><%=moTructVo.getName() %></td>
							<td><%=moTructVo.getStructcode() %></td>
							<td><%=moTructVo.getBussysname() %></td>
							<td>
							<%
								if(userList!=null && userList.size()>0){
								for(Userdata spuser :userList){
									if((spuser.getUid() - moTructVo.getSpid()) == 0){
							 %>
									<%=spuser.getUserId() %>
							<%
										break;
									}
								}
								}
							 %>
							 &nbsp;
							 </td>
							<td>
							<%if(!"01".equals(moTructVo.getTructtype())){ %>
							<a href="javascript:showEditServiceBind('<%=moTructVo.getId()%>', '<%=keyId %>','<%
								if(userList!=null && userList.size()>0){
								for(Userdata spuser :userList){
									if((spuser.getUid() - moTructVo.getSpid()) == 0){
							 %><%=spuser.getUserId() %><%
										break;
									}
								}
								}
							 %>');"><emp:message key="txgl_wgqdpz_yyshdgl_xg" defVal="修改" fileName="txgl"></emp:message></a>&nbsp;&nbsp;
							<a href="javascript:doDel('<%=moTructVo.getId() %>', '<%=keyId %>')"><emp:message key="txgl_wgqdpz_qyhdgl_sc" defVal="删除" fileName="txgl"></emp:message></a></td>
							<%} %>
						</tr>
						<%
						 }
						}else{
						 %>
						 <tr>
						 <td algin="center" colspan="8"><emp:message key="txgl_wgqdpz_qyhdgl_wjl" defVal="无记录" fileName="txgl"></emp:message></td>
						 </tr>
						 <%
						  }
						  %>
						
					</tbody>
					
					
					<tfoot>
							<tr>
								<td colspan="16">
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
					<div id="bottom_main">
					</div>
				</div>
			</div>
			</form>
			<%-- foot结束 --%>
		</div>
		
		<div id="editMoServiceBindDiv" title="<%=bjywzlbd %>" class="editMoServiceBindDiv">
			<iframe id="editMoServiceFrame" name="editMoServiceFrame" class="<%="zh_HK".equals(empLangName)?"editMoServiceFrame1":"editMoServiceFrame2"%>" marginwidth="0" scrolling="no" frameborder="no"></iframe>
		</div>
		
		<div id="addMoServiceBindDiv" title="<%=xzywzlbd %>" class="addMoServiceBindDiv">
			<iframe id="addMoServiceBindFrame" name="addMoServiceBindFrame" class="<%="zh_HK".equals(empLangName)?"addMoServiceBindFrame1":"addMoServiceBindFrame2"%>"  marginwidth="0" scrolling="no" frameborder="no"></iframe>
		</div>
		
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script language="javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
 		<script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/txgl/pasgroup/js/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/orderbind.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		
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
				}, function() {
				$(this).removeClass("hoverColor");
			});
			initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		
			$('#search').click(function(){
				submitForm();
		
				});
		
			$("#addMoServiceBindDiv").dialog({	    
				autoOpen: false,
				height:290,
				width: <%="zh_HK".equals(empLangName)?630:590%>,
				resizable:false,
				modal: true,
				open:function(){
					
				},
				close:function(){
					$("#addMoServiceBindDiv").attr("src","");
				}
			});
			$("#editMoServiceBindDiv").dialog({
				autoOpen: false,
				height:310,
				width: <%="zh_HK".equals(empLangName)?630:590%>,
				resizable:false,
				modal: true,
				open:function(){
					
				},
				close:function(){
					$("#editMoServiceBindDiv").attr("src","");
				}
			});
			
		});

		function doDel(id, keyId){
		    
		 	var userid =<%= session.getAttribute("userId")%>;
			//if(confirm("你确定要删除此条记录吗？")==true)
			if(confirm(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_2"))==true)
			{
			  $.post("<%=path%>/pag_orderBind.htm?method=checkBinding",{id:id,userid:userid},function(data){
			  		if(data == "1"){
			  			//alert("此条指令已绑定智能引擎上行业务，请先解绑上行！");
			  			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_1"));

			  		}else{
			  			$.post("<%=path%>/pag_orderBind.htm?method=delete",{id:id,userid:userid,keyId:keyId},function(result){
						if(result>0){
							//alert("删除成功！");
							alert(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_3"));
							submitForm();

						}else if(result<=0){
						
						 //alert("删除失败！");
						 alert(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_4"));
						}
					});
			  		}
			  });
				
			}
		}
		//打开修改上行业务指令绑定页面
		function showEditServiceBind(moServiceid, keyId,spId)
		{
			var userid =<%= session.getAttribute("userId")%>;
			$.post("<%=path%>/pag_orderBind.htm?method=checkBinding",{id:moServiceid,userid:userid},function(data){
				if(data == "1"){
		  			//alert("此条指令已绑定智能引擎上行业务，无法修改！");
		  			alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zllypz_text_2"));

		  		}else{
		  			$("#editMoServiceFrame").attr("src","<%=path %>/pag_orderBind.htm?method=doEdit&moServiceid="+moServiceid+"&keyId="+keyId+"&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&spId="+spId);
		          
					$("#editMoServiceBindDiv").dialog("open");
		  		}
			});
			
		    
		}
		</script>
	</body>
</html>
