<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));

MessageUtils messageUtils = new MessageUtils();
String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
@ SuppressWarnings("unchecked")
Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
@ SuppressWarnings("unchecked")
Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
String menuCode = titleMap.get("spePhoneManage");
menuCode = menuCode==null?"0-0-0":menuCode;

PageInfo pageInfo = new PageInfo();
pageInfo=(PageInfo)request.getAttribute("pageInfo");

String skin = session.getAttribute("stlyeSkin")==null?
		"default":(String)session.getAttribute("stlyeSkin");
@SuppressWarnings("unchecked")
List<DynaBean> beanList = (List<DynaBean>) request
		.getAttribute("beanList");

String phone = request.getParameter("phone");
String unicom = request.getParameter("unicom");


//保存
String bc = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwcwdm_bc", request);
//取消
String qx = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwcwdm_qx", request);
//手动添加
String sdtj = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwcwdm_sdtj", request);
//文件导入
String wjdr = messageUtils.extractMessage("txgl", "txgl_wygl_xhzwhmgl_wjdr", request);
//信息内容
String xxnr = messageUtils.extractMessage("txgl", "txgl_wygl_tshmgl_xxnr", request);


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  	<%@include file="/common/common.jsp" %>
    <title>My JSP 'wy_spePhoneManage.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.dialog.new.css?V=<%=StaticValue.getJspImpVersion() %>" />
	<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" />
	
	<%if(StaticValue.ZH_HK.equals(langName)){%>	
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
	<%}%>
	<style type="text/css">
		.imporInner{width: 485px;height: 240px;margin:20px auto 10px;}
		.bultMark{width: 485px;margin:0 auto 20px;position: relative;overflow: hidden;}
		.imporInner textarea{background: transparent;border: 0;width:477px;height:220px;}
		textarea:focus{outline: none;resize:none;}
		.bultBtn{text-align: center;}
		.numSplit{text-align: left;}
		#bultNum{position: absolute;left: 0;top: 0;}
		.bultMark_top
		{
		
			width: 485px;top:30px;position: absolute;overflow: hidden;left:40px;
			color: #BABDC0;
		}
		#uploadPhone table
		{
			margin-top: 20px;
		}
		#uploadPhone table tr td
		{
			height: 30px;
		}
		#uploadPhone .wy_a_color
		{
			color: #0e5ad1;
			text-decoration: underline;
		}
		.td_right
		{
			padding-left: 5px; 
		}
	</style>
  </head>
  
  <body>
    <div id="container" class="container">
		<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
		
		<%-- 内容开始 --%>
		<div id="rContent" class="rContent">
		<%-- 表示请求的名称 --%>
		<form name="pageForm" action="wy_spePhoneManage.htm" method="post" id="pageForm">
			<%-- 隐藏id，表示是否双击修改 1没有 2,修改中 --%>
			<input type="hidden" name="hid_state" id="hid_state" value="1"/>
			<input type="hidden" name="hid_id" id="hid_id" value=""/>
			<div id="loginInfo" class="hidden"></div>
			<div class="buttons">
				<div id="toggleDiv">
				</div>
				<%if(btnMap.get(menuCode+"-1")!=null){%>
				<a id="addeployee" onclick="add()"><emp:message key="txgl_wygl_xhzwcwdm_sdtj" defVal="手动添加" fileName="txgl"></emp:message></a>
				<%} %>
				<%if(btnMap.get(menuCode+"-2")!=null){%>
				<a id="addgr" onclick="openUpload()"><emp:message key="txgl_wygl_xhzwhmgl_wjdr" defVal="文件导入" fileName="txgl"></emp:message></a>
				<%} %>
				<%if(btnMap.get(menuCode+"-4")!=null){%>
				<a id="delete" onclick="delAll()"><emp:message key="txgl_wygl_xhzwcwdm_sc" defVal="删除" fileName="txgl"></emp:message></a>
				<%} %>
			</div>
			<div id="condition">
				<table>
					<tbody>
						<tr>
							<td>
							<emp:message key="txgl_wygl_xhzwhmgl_sjhm" defVal="手机号码" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message>
							</td>
							<td >
								<input type="text" name="phone" id="phone" onkeyup="phoneInputCtrl($(this))" value='<%=phone!=null?phone:"" %>'/>
							</td>
							<td>
								<emp:message key="txgl_wygl_tshmgl_ssyyb" defVal="所属运营商" fileName="txgl"></emp:message>：
							</td>
							<td >	
								<select name="unicom" id="unicom">
									<option value=""><emp:message key="txgl_wygl_xhzwcwdm_qb" defVal="全部" fileName="txgl"></emp:message></option>
									<option value="0" <%="0".equals(unicom)?"selected":"" %>><emp:message key="txgl_wygl_wytdgl_yd" defVal="移动" fileName="txgl"></emp:message></option>
									<option value="1" <%="1".equals(unicom)?"selected":"" %>><emp:message key="txgl_wygl_wytdgl_lt" defVal="联通" fileName="txgl"></emp:message></option>
									<option value="21" <%="21".equals(unicom)?"selected":"" %>><emp:message key="txgl_wygl_wytdgl_dx" defVal="电信" fileName="txgl"></emp:message></option>
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
							<input type="checkbox" name="dels" value="" onclick="checkAlls(this,'checklist')" />
						</th>
						<th>
							<emp:message key="txgl_wygl_xhzwhmgl_sjhm" defVal="手机号码" fileName="txgl"></emp:message>
						</th>
						<th>
							<emp:message key="txgl_wygl_tshmgl_ssyyb" defVal="所属运营商" fileName="txgl"></emp:message>
						</th>
						<th>
							<emp:message key="txgl_wygl_xhzwhmgl_lrsj" defVal="录入时间" fileName="txgl"></emp:message>
						</th>
						<%if(btnMap.get(menuCode+"-3")!=null&&btnMap.get(menuCode+"-4")!=null ){%>
							<th colspan="2">
								<emp:message key="txgl_wygl_xhzwcwdm_cz" defVal="操作" fileName="txgl"></emp:message>
							</th>
						<%}else if(btnMap.get(menuCode+"-3")!=null){%>
							<th colspan="2">
								<emp:message key="txgl_wygl_xhzwcwdm_cz" defVal="操作" fileName="txgl"></emp:message>
							</th>
						<%}else if(btnMap.get(menuCode+"-4")!=null){%>
							<th colspan="2">
								<emp:message key="txgl_wygl_xhzwcwdm_cz" defVal="操作" fileName="txgl"></emp:message>
							</th>
						<%}%>
					</tr>
				</thead>
				<tbody>
				<%
					if(beanList!=null && beanList.size()>0)
					{
						for(DynaBean bean : beanList)
						{
				%>
				<tr>
					<td>
						<input type="checkbox" name="checklist" value="<%=bean.get("id") %>" />
					</td>
					<td width="290px">
					<center>
						<%
						String phoneStr=bean.get("phone")!=null?bean.get("phone").toString():"-";
						 %>
						<span id="span<%=bean.get("id") %>">
							<%=phoneStr %>
						</span>
						<input type="text" name="phoneText" onkeyup="numberControl($(this))" maxlength="11" id="phone<%=bean.get("id") %>" value="" style="display: none;width: 153px;"/>
						<input type="hidden" name="hidden_id" value="<%=bean.get("id") %>" style=""/>
					</center>
					</td>
					<td>
					<%
						String type = bean.get("unicom").toString();
						//String typeStr="移动";
						String typeStr=messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_yd", request);
						if("0".equals(type)){
							//typeStr="移动";
							typeStr=messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_yd", request);
						}else if("21".equals(type)){
							//typeStr="电信";
							typeStr=messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_dx", request);
						}else if("1".equals(type)){
							//typeStr="联通";
							typeStr=messageUtils.extractMessage("txgl", "txgl_wygl_wytdgl_lt", request);
						}
					%>
					<span id="unicom<%=bean.get("id") %>"><%=typeStr %></span>
					</td>
					<td>
						<%=bean.get("createtime")!=null?df.format(bean.get("createtime")):"" %>
					</td>
					<%if(btnMap.get(menuCode+"-3")!=null&&btnMap.get(menuCode+"-4")!=null ){%>
						<td>
							<a id="edit<%=bean.get("id") %>" onclick="toEdit('<%=bean.get("id") %>')" ><emp:message key="txgl_wygl_xhzwcwdm_xg" defVal="修改" fileName="txgl"></emp:message></a>
						</td>
						<td>
							<a onclick="javascript:del('<%=bean.get("id") %>')"><emp:message key="txgl_wygl_xhzwcwdm_sc" defVal="删除" fileName="txgl"></emp:message></a>
						</td>
					<%}else if(btnMap.get(menuCode+"-3")!=null){%>
						<td>
							<a id="edit<%=bean.get("id") %>" onclick="javascript:toEdit('<%=bean.get("id") %>')"><emp:message key="txgl_wygl_xhzwcwdm_xg" defVal="修改" fileName="txgl"></emp:message></a>
						</td>
					<%}else if(btnMap.get(menuCode+"-4")!=null){%>
						<td>
							<a onclick="javascript:del('<%=bean.get("id") %>')"><emp:message key="txgl_wygl_xhzwcwdm_sc" defVal="删除" fileName="txgl"></emp:message></a>
						</td>
					<%}%>
				</tr>
				<%
						}
					}else{
				%>
				<tr><td align="center" colspan="6"><emp:message key="txgl_wygl_xhzwcwdm_wjl" defVal="无记录" fileName="txgl"></emp:message></td></tr>
				<%} %>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="13">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
			</table>
		</form>
		</div>
			
		<%-- 内容结束 --%>
		<div id="bulkImport_box" title="<%=sdtj %>" style="display:none;">
				<input type="hidden" value="" id="inputphone" name="inputphone"/>
					<div class="bultMark_top" id="bultMark_top" onclick="hideDiv()">
				   		<emp:message key="txgl_wygl_tshmgl_qsrsjhm" defVal="请输入手机号码" fileName="txgl"></emp:message>
				   </div>
			   <div class="imporInner div_bd">
			   		<textarea name="importArea" id="importArea" cols="30" rows="5" onkeyup="formatTelNum('#bNum')" onchange="formatTelNum('#bNum')"  onclick="hideDiv()" onblur="showDiv()"></textarea>
			   		<textarea name="importAreaTemp" id="importAreaTemp" style="display:none"></textarea>
			   </div>
			   <div class="bultMark">
			   		<span id="bultNum"><emp:message key="txgl_wygl_tshmgl_dqg" defVal="当前共" fileName="txgl"></emp:message><font color='blue'><b id="bNum"></b></font>/<emp:message key="txgl_wygl_tshmgl_yqghm" defVal="1000个号码" fileName="txgl"></emp:message></span>
			   		<div style="height:25px"></div>
			   		<div class="numSplit">
			   			<emp:message key="txgl_wygl_xhzwhmgl_dgsjhmydhkgdh" defVal="多个手机号码以逗号、空格、顿号、回车换行其中一种格式隔开" fileName="txgl"></emp:message>
			   		</div>
			   </div>
			   <div class="bultBtn">
			   	<input onclick="previewTel()" class="btnClass5 mr23" type="button" value="<%=bc %>">
			   	<input onclick="bultCancel()"  class="btnClass6" type="button" value="<%=qx %>"><br/>
			   </div>
		</div>
		
		<%-- 内容结束 --%>
		<div id="uploadPhone" title="<%=wjdr %>" style="display:none;">
		<iframe name="hidden_iframe" id="hidden_iframe" style="display: none"></iframe>
		<form name="updateInfo" id="updateInfo" method="post" action="wy_spePhoneManage.htm?method=uploadPhone"  enctype="multipart/form-data" target="hidden_iframe">
				<center>
				<table>
					<tr>
						<td><emp:message key="txgl_wygl_xhzwhmgl_xzwj" defVal="选择文件" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message></td>
						<td class="td_right">
						    <%--<input type="file" value="" id="uplfile" name="uplfile"/>
							--%>
							 <%--
							<span style="position:relative;float:left;display:block;width:140px;height:18px;border:1px solid #999;background: #eee;text-align:center;">
								<input style="width:138px;border:0px;" type="text" id="filepath" readonly>
								<input style="position:absolute;left:0;top:0;width:138px;height:18px;border:0px;opacity:0;filter:alpha(opacity=0);" type="file" id="uplfile" name="uplfile" onchange="filepath.value=this.value;this.title=this.value"/>			
							</span>
							--%>
							<input style="width:138px;float:left;" type="text" id="filepath" readonly>
							<span style="width:10px;float:left;">&nbsp;&nbsp;&nbsp;&nbsp;</span>
							<span>
								<span style="position:relative;float:left;display:block;width:80px;height:18px;border:1px solid #999;background: #eee;text-align:center;line-height:18px;">
									<emp:message key="txgl_wygl_tshmgl_scwj" defVal="上传文件" fileName="txgl"></emp:message>
									<input style="position:absolute;left:0;top:0;width:82px;height:20px;opacity:0;filter:alpha(opacity=0);" type="file" id="uplfile" name="uplfile" 
											onchange="document.getElementById('filepath').value=this.value;document.getElementById('filepath').title=this.value;"/>			
								</span>
							</span>
						</td>
					</tr>
					<tr>
						<td></td>
						<td style="" class="td_right" align="left">
							<%-- 解决下载后出现等待滚动层的bug 备份
						    <a class="wy_a_color" href="javascript:location.href='<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/spPhone.txt'">txt文件模板</a>&nbsp;&nbsp;&nbsp;&nbsp;
						    <a class="wy_a_color" href="javascript:location.href='<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/spPhone.xls'">excel文件模板</a>
						    --%>
						    <%-- 解决下载后出现等待滚动层的bug 修改 --%>
						    <a class="wy_a_color" href="javascript:download_href('<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/spPhone.txt')">txt<emp:message key="txgl_wygl_xhzwhmgl_wjmb" defVal="文件模板" fileName="txgl"></emp:message></a>&nbsp;&nbsp;&nbsp;&nbsp;
						    <a class="wy_a_color" href="javascript:download_href('<%=path %>/down.htm?filepath=/wygl/wymanage/file/temp/spPhone.xls')">excel<emp:message key="txgl_wygl_xhzwhmgl_wjmb" defVal="文件模板" fileName="txgl"></emp:message></a>
						    <br/>
						</td>
					</tr>
					<tr>
						<td align="right">
							<emp:message key="txgl_wygl_tshmgl_z" defVal="注" fileName="txgl"></emp:message><emp:message key="txgl_wygl_wytdgl_fhfh" defVal="：" fileName="txgl"></emp:message>
						</td>
						<td align="left" class="td_right">
							<emp:message key="txgl_wygl_tshmgl_1zctzxxgs" defVal="1、支持txt、zip、xls、xlsx、et格式；" fileName="txgl"></emp:message>
						</td>
					</tr>
					<tr>
						<td align="right">
						</td>
						<td align="left" class="td_right">
							<emp:message key="txgl_wygl_tshmgl_2dcschmxy5g" defVal="2、单次上传号码小于50000个。" fileName="txgl"></emp:message>
						</td>
					</tr>
				</table>
				</center>
			   <div class="uplBtn" style="text-align: center;margin-top: 10px;">
			   	<input onclick="upload()" class="btnClass5 mr23" type="button" value="<%=bc %>">
			   	<input onclick="back()"  class="btnClass6" type="button" value="<%=qx %>"><br/>
			   </div>
			</form>
		</div>
		
		<div id="modify" title="<%=xxnr %>"  style="padding:5px;width:300px;height:160px;display:none">
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
	</div>
	<div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script src="<%=iPath%>/js/spePhoneManage.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript" ></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>

	<script type="text/javascript">
		var path='<%=path%>';
		var srcpath='<%=StaticValue.BASEURL %>'
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
			$('#bulkImport_box').dialog({
				autoOpen: false,
				height: 420,
				width: 542,
				resizable:false,
				modal:true
			});
			$('#uploadPhone').dialog({
				autoOpen: false,
				height: 260,
				width: 460,
				resizable:false,
				modal:true
			});
			$("#unicom").isSearchSelect({'width':'158','isInput':false,'zindex':0});
			//表格选中变色
			$('#content table tbody tr,#content tbody tr').bind("dblclick",function(){
				var id=$(this).find("input[name='hidden_id']").val();
				$("#hid_id").val(id);
				$("#hid_state").val("2");
				edit_js(id);
			})

			//解除绑定事件，使表格选中不变色
			$('#content table tbody tr,#content tbody tr').unbind("click");
			//表格选中变色
			$('#content table tbody tr,#content tbody tr').click(function(e){
				$(this).addClass('c_selectedBg').siblings().removeClass('c_selectedBg');
				var hidden_id=$(this).find("input[name='hidden_id']").val();
				var hid_id = $("#hid_id").val();
				var hid_state = $("#hid_state").val();
				if(hidden_id!=hid_id && hid_state==2)
				{
					$("#hid_state").val("1");
		         	var id=$("#hid_id").val();
	         		clearEdit(id);
				}
				e.stopPropagation();
			})
			
			$('html,body').click(function(e){
				//var htmlStr = $.trim($(e.target).html());
				//var hidden_id=$(e.target).parent().find("input[name='hidden_id']").val();
				//var hid_id = $("#hid_id").val();
				//var hid_state = $("#hid_state").val();
				//alert(hidden_id+"xx"+hid_id+"cc"+hid_state);
	         	//if(htmlStr!="保存" && ( hidden_id!=hid_id && hid_state==2)){
	       		//}
	         	$("#hid_state").val("1");
	         	var id=$("#hid_id").val();
         		clearEdit(id);
			});
		});

		function hideDiv()
		{
			$("#bultMark_top").hide();
			$("#importArea").focus();
		}

		function showDiv()
		{
			if($("#importArea").val()=="")
			{
				$("#bultMark_top").show();
			}
		}
	</script>
  </body>
</html>
