<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@page import="java.util.ArrayList"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.blacklist.LfMmsBlist"%>
<%@page import="com.montnets.emp.entity.blacklist.PbListBlack"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>

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
String menuCode = titleMap.get("mmsblacklistSvt");
menuCode = menuCode==null?"0-0-0":menuCode;
@ SuppressWarnings("unchecked")
List<PbListBlack> pbList = (List<PbListBlack>)request.getAttribute("mmsblackList");
@ SuppressWarnings("unchecked")
Map<Long, String> keyIdMap = (Map<Long, String>)request.getAttribute("keyIdMap");
@ SuppressWarnings("unchecked")
LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getAttribute("conditionMap");

String findResult= (String)request.getAttribute("findresult");
String uploadresult = (String)session.getAttribute("uploadresult");	
String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");

String blState = request.getParameter("blState");
String txglFrame = skin.replace(commonPath, inheritPath);


String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

//修改彩信黑名单
String xgcxhmd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_xgcxhmd", request);
//新建彩信黑名单
String xjcxhmd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_xjcxhmd", request);
//确定
String qd = MessageUtils.extractMessage("common", "common_confirm", request);
//取消
String qx = MessageUtils.extractMessage("txgl", "txgl_wghdpz_gjzsz_qx", request);
//导入彩信黑名单
String drcxhmd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_drcxhmd", request);
//导出彩信黑名单
String dccxhmd = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_dccxhmd", request);
//EXCEL格式
String egs = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_egs", request);
//TXT格式
String tgs = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_tgs", request);
//手机号码
String sjhm = MessageUtils.extractMessage("txgl", "txgl_wghdpz_dxhmd_sjhm", request);
if(sjhm!=null&&sjhm.length()>1){
   sjhm = sjhm.substring(0,sjhm.length()-1);
}



%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    <%@include file="/common/common.jsp" %>
    <title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/file.css?V=<%=StaticValue.getJspImpVersion() %>" />
        <%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/bla_mmsblacklist.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/bla_mmsblacklist.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
  </head>
  
  <body id="bla_mmsblacklist">
   		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			
			<div id="editmmsBl" title="<%=xgcxhmd %>" class="editmmsBl">
			<table>
			   <tr>
			     <td><emp:message key="txgl_wghdpz_dxhmd_sjhm" defVal="手机号码：" fileName="txgl"></emp:message></td>
			     <td><input type="text" id="umobile" maxlength="11" onkeyup="numberControl($(this))" onblur="numberControl($(this))"/></td>
			   </tr>
			</table>					
			</div>
			
			<div id="addmmsBl" title="<%=xjcxhmd %>" class="addmmsBl">
				<div class="addmmsBl_down_div">
					<center>
					<table>
  					  <tr>
   				 		 <td align="right"><emp:message key="txgl_wghdpz_dxhmd_sjhm" defVal="手机号码：" fileName="txgl"></emp:message></td>
   						 <td align="left">
   						 	<input type="text" onkeyup="numberControl($(this))" onblur="numberControl($(this))"  name="mobile" id="mobile" maxlength="11"  class="input_bd mobile"/>  
   	   					 </td>   	   					 	
 					  </tr>
					</table>
					</center>
				</div>
				<div class="blok_div">
				    <center>
		    		<input id="blok" class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="javascript:checkPhone('add')"/>
				    <input id="blc" onclick="javascript:btcancel();" class="btnClass6" type="button" value="<%=qx %>" />
				    <br/>
				    </center>
		    	</div>
			</div>
			<input type="hidden" id="pnoticeSize" name="pnoticeSize" value="<%=pbList != null ? pbList.size():0%>"/>
			<input type="hidden" id="totalSize" name="totalSize" value="<%=pageInfo != null ? pageInfo.getTotalRec():0%>"/>
			
			<div id="uploadmmsBl" title="<%=drcxhmd %>" class="uploadmmsBl">
			<form name="updateInfo" method="post" action="<%=path%>/bla_mmsblacklistSvt.htm?method=update" enctype="multipart/form-data">
			    <div id="blloginparams1" class="hidden"></div>   
				<div class="blloginparams1_down_div">
					<center>	 
					<table class="xzwj_table" >
  					 <tr class="xzwj_tr">
	   				 	 <td><span class="xzwj_span"><emp:message key="txgl_wghdpz_dxhmd_xzwj" defVal="选择文件：" fileName="txgl"></emp:message></span></td>
	   					 <td>
	                         <div>
		                        <a href="javascript:;" class="file"><emp:message key='wxgl_button_14' defVal='浏览' fileName='wxgl'/>
									<input type="file" name="phone1" id="phone1">
								</a>
		                        <p id="filename"></p>
	                         </div> 
	                      </td>
	 				 </tr>
	 				 <tr class="filename_down_tr"></tr>
				  	 <tr>
						 <td class="<%="zh_HK".equals(empLangName)?"spzhczhs_z1":"spzhczhs_z2"%>" ><emp:message key="txgl_wgqdpz_spzhczhs_z" defVal="注：" fileName="txgl"></emp:message></td>
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
				    <font id="impfont" color="#F1F1F9;" class="<%="zh_HK".equals(empLangName)?"impfont1":"impfont2"%>"  ><emp:message key="txgl_wghdpz_dxhmd_zzdrzqsh" defVal="正在导入中,请稍后..." fileName="txgl"></emp:message></font>
		    		<input id="bloks" class="btnClass5 mr23" type="button" value="<%=qd %>" onclick="javascript:checkPhone('upload')"/>
				    <input id="blcs" onclick="javascript:btcancel();" class="btnClass6" type="button" value="<%=qx %>" />
				    <br/>
				    </center>
		    	</div>
		    	</form>
			</div>
			
			
			<div id="exportDiv" title="<%=dccxhmd %>" class="exportDiv" >
			<span class="<%="zh_HK".equals(empLangName)?"excelBut_span1":"excelBut_span2"%>"  >
				<input id="excelBut" class="btnClass4" type="button" value="<%=egs %>" onclick="javascript:exportExcel()"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="txtBut" class="btnClass4" type="button" value="<%=tgs %>" onclick="javascript:exportTxt()"/>
			</span>
			<span>
			<font id="outfont" color="#F1F1F9;" class="<%="zh_HK".equals(empLangName)?"outfont1":"outfont2"%>"  ><emp:message key="txgl_wghdpz_dxhmd_zzdczqsh" defVal="正在导出中,请稍后..." fileName="txgl"></emp:message></font>
			</span>
			<span>
			<font class="zhu qxzdcgscg_font"  ><emp:message key="txgl_wghdpz_dxhmd_qxzdcgscg" defVal="请选择导出格式，超过50万以上数据，请您选择TXT格式。" fileName="txgl"></emp:message></font>
			</span>
			</div>
			
			<%-- 内容开始 --%>
			<%if(btnMap.get(menuCode+"-0")!=null) { %>
			<div id="rContent" class="rContent">
			<form name="pageForm" action="bla_mmsblacklistSvt.htm?method=find" method="post"
					id="pageForm">
					<div class="buttons">
						<div id="toggleDiv">							
						</div>
						  <%if(btnMap.get(menuCode+"-1")!=null) { %>
						<a id="add" onclick="javascript:doadd()"><emp:message key="txgl_wgqdpz_qyhdgl_xj" defVal="新建" fileName="txgl"></emp:message></a>
						<a id="upload" onclick="javascript:doupload()"><emp:message key="txgl_wghdpz_gjzsz_dr" defVal="导入" fileName="txgl"></emp:message></a>
						<%} %>
						<%if(btnMap.get(menuCode+"-4")!=null) { %>
						<a id="exportCondition" onclick="javascript:exportAll()"><emp:message key="txgl_wghdpz_dxhmd_dc" defVal="导出" fileName="txgl"></emp:message></a>
						<%} %>
						<%if(btnMap.get(menuCode+"-2")!=null) { %>
						<a id="delete" onclick="javascript:delAll()"><emp:message key="txgl_wghdpz_gjzsz_sc" defVal="删除" fileName="txgl"></emp:message></a>
						<%} %>
					</div>
					<div id="condition">
						<table>
							<tr>
									<td class="sjhm_td">
										<emp:message key="txgl_wghdpz_dxhmd_sjhm" defVal="手机号码： " fileName="txgl"></emp:message>
									</td>
									<td align="left">										
										<input name="phone" id="phone" type="text"  class="phone" onkeyup="numberControl($(this))" onblur="numberControl($(this))"
											 maxlength="11" value="<%=conditionMap.get("phone")==null?"":conditionMap.get("phone") %>"/>
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
										<input type="checkbox" name="dels" value=""
											onclick="checkAlls(this,'checklist')" />
									</th>
									<th>
										<%=sjhm %>
									</th>
									<%--<th>
										状态
									</th>
									--%><th>
									<emp:message key="txgl_wghdpz_gjzsz_cz" defVal="操作" fileName="txgl"></emp:message>
									</th>
						        </tr>
							</thead>
						<tbody>
							<%
							if (pbList != null && pbList.size() > 0)
							{
								String keyId;
								for (PbListBlack pb : pbList)
								{
									keyId = keyIdMap.get(pb.getId());
							%>
							<tr>
								<td>
									<input type="checkbox" name="checklist" value="<%=keyId %>" />
								</td>
								<td>
									<label id="la<%=pb.getId() %>"><%=pb.getPhone()%></label>
								</td>
								<%--<td class="ztalign">
										<%
											if(pb.getBlState()==1)
											{
										%>
											<select  name="blState<%=pb.getBlId() %>" id="blState<%=pb.getBlId() %>" class="input_bd" onchange="javascript:changeState(<%=pb.getBlId()%>)">
									          <option value="1" selected="selected">已启用</option>
									          <option value="0" >禁用</option>
									        </select>
										<%		
											}else
											{
										%>
										   <select  name="blState<%=pb.getBlId()%>" id="blState<%=pb.getBlId()%>" class="input_bd" onchange="javascript:changeState(<%=pb.getBlId()%>)">
									          <option value="0" selected="selected">已禁用</option>
									          <option value="1" >启用</option>									          
									        </select>
										<%		
											}
										 %>	
								</td>
								--%><td>
								  <%if(btnMap.get(menuCode+"-2")!=null) { %>
									<a href="javascript:del(<%=pb.getId()%>,'<%=pb.getPhone()%>', '<%=keyId %>')"><emp:message key="common_delete" defVal="删除" fileName="common"></emp:message></a>
									<%} %>
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/mmsblacklist.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		
		
        <script type="text/javascript">
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
						//alert("新建成功,有效彩信黑名单号码个数为："+uploadresult.substr(4)+"！");
						alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_8")+uploadresult.substr(4)+"！");
						black();
					}
					else if( uploadresult =="noPhone")
					{
						//alert("没有有效的彩信黑名单记录可以添加！");
						alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_9"));
					}else if( uploadresult =="overCount")
					{
						//alert("上传号码个数超过20万，请分批次重新上传！");
						alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_3"));
					}else if(uploadresult == "false"){
						//alert("新建失败！");
						alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_4"));
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
					$("#search").click(function(){
						var phone = $("#phone").val();
						if(phone.length > 0 && phone.length < 11 )
						{
							//alert("请输入11位的号码进行查询！");
							alert(getJsLocaleMessage("txgl","txgl_wghdpz_dxhmd_text_10"));
							return;
						}
						submitForm();
					});
				});
	  </script>
  </body>
</html>
