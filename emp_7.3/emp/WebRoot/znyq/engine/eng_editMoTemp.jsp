<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.engine.LfProcess" %>
<%@ page import="com.montnets.emp.entity.engine.LfReply" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String iPath = request.getRequestURI().substring(0,
		request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");

	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("moService");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	Long prId =Long.valueOf(request.getParameter("prId"));
	String serId=request.getParameter("serId");
	Long tempId=(Long)request.getAttribute("tempId");
	LfReply reply = (LfReply)request.getAttribute("reply");
	if(reply==null)
	{
		reply=new LfReply();
		reply.setMsgLoopId(0l);
	}
	@ SuppressWarnings("unchecked")
	List<LfProcess> proList = (List<LfProcess>)request.getAttribute("proList");
	@ SuppressWarnings("unchecked")
	List<LfTemplate> temList = (List<LfTemplate>)request.getAttribute("temList");
		
	String lguserid = (String)request.getAttribute("lguserid");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link rel="stylesheet" type="text/css" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>">
		<link href="<%=commonPath %>/common/css/params.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
	</head>
	<body onload="show()" id="eng_editMoTemp" class="eng_editMoTemp">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container">
			<%--
			<div id="u_o_c_explain">
					<p>
						说明：回复短信设置
					</p>
					<ul>
						<li>
							回复短信设置用来定义发送给用户的短信的具体内容
						</li>
						<li>
							步骤名称：选择用于提供动态短信模板参数值的select步骤处理结果
						</li>
						<li>
							信息体：选择短信模板，可以是动态模板或静态模板，如果是动态模板，则需要选择步骤名称下拉框的值
						</li>
						<li>
							动态模板说明：
						</li>
						<li>
							动态模板由短信内容和插入符组成，例如：你好，#P_1#先生/小姐。其中#P_1#为插入符，用于把动态结果插入到短信内容中，它代表所选select类型步骤执行结果集第一列的值，插入符也可以是#P_2#，代表第二列的值，如此类推
						</li>
					</ul>
				</div>
				
			 --%>
						<div id="detail">
						<form method="post" action="eng_moService.htm?method=upTemp" id="reply">
						<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
									<input type="hidden" name="prId" value='<%=prId %>' id="prId" />
									<input type="hidden" name="serId" value='<%=serId %>' id="serId" />
									<input type="hidden" name="lguserid" id="lguserid" value="<%=lguserid %>"/>
									<input type="hidden" name="lgcorpcode" id="lgcorpcode" value="<%=lgcorpcode %>"/>
									<table>
									<thead>
										 <tr class="stepNameTr" >
											<td>
												<span><emp:message key="znyq_ywgl_sxywgl_bzmc_mh" defVal="步骤名称:" fileName="znyq"></emp:message></span>
											</td>
											<td>
											<label>
												<select class="input_bd" name="msgLoopId" id="msgLoopId">
													<option value=" ">
														<emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message>
													</option>
												<%
													for(int i=0;i<proList.size();i++)
													{
														LfProcess process=proList.get(i);
												%>
													<option value="<%=process.getPrId() %>"
													<%
														if(reply.getMsgLoopId()-process.getPrId()==0)
														{
															out.print("selected=\"selected\"");
														}
													%>>
														<%=process.getPrName().replace("<","&lt;").replace(">","&gt;") %>(<emp:message key="znyq_ywgl_sxywgl_ywmc_mh" defVal="序号" fileName="znyq"></emp:message>：<%=process.getPrNo()%>)
													</option>
												<%
													}
												%>
												</select></label>
												<font color="red"><emp:message key="znyq_ywgl_sxywgl_xzbzlxwselectdbz" defVal="选择步骤类型为Select的步骤" fileName="znyq"></emp:message></font>
											</td>
										</tr> 
										<tr class="sendInfoTr">
											<td>
												<span><emp:message key="znyq_ywgl_sxywgl_fsnr_mh" defVal="发送内容:" fileName="znyq"></emp:message></span>
											</td><td><label>
												<select id="selectInfoBody" class="input_bd" name="tempId" class="input_bd">
													<option value="" class="slvColor">
														<emp:message key="znyq_ywgl_common_text_13" defVal="请选择" fileName="znyq"></emp:message>
													</option>
													<%
														for(int t=0;t<temList.size();t++)
														{
															LfTemplate tem=temList.get(t);
													%>
													<option value="<%=tem.getTmid() %>" <% if(tem.getTmid().equals(tempId)){ %>selected="selected"<%} %>><%=tem.getTmName().replace("<","&lt;").replace(">","&gt;") %>(ID:<%=tem.getTmid() %>)</option>
													<%} %>
												</select></label>
												<font class="tipColor"><emp:message key="znyq_ywgl_sxywgl_kxzdxmbsxksbj" defVal="可选择短信模板实现快速编辑" fileName="znyq"></emp:message></font>
											</td>
										</tr>
										<tr>
										<td></td>
										<td class="closeParTd">
												<div class="paraContent div_bd">
												<div class="tit_panel div_bd">
											
													<a href="javascript:void(0)" class="para_cg" ><emp:message key="znyq_ywgl_sxywgl_gbcs" defVal="关闭参数" fileName="znyq"></emp:message></a>
													<%
													if(btnMap.get(titleMap.get("manger")+"-0")!=null){
													%>
													<a class="chooseNetTpl" onclick="window.parent.chooseNetTpl()"><emp:message key="znyq_ywgl_sxywgl_crwx" defVal="插入网讯" fileName="znyq"></emp:message></a>
													<%} %>
												</div>	
													<label id="showEditor">
														<textarea class="textarea_limit input_bd div_bd contents_textarea" id="msgMain" name="msgMain"  
														onKeydown="saveTextareaPos(this)"
							                            onKeyup="saveTextareaPos(this)"
							                            onmousedown="saveTextareaPos(this)"
							                            onmouseup="saveTextareaPos(this);"
							                            onfocus="saveTextareaPos(this)"
														><%=reply.getMsgMain()!=null? reply.getMsgMain():""%></textarea>
														<xmp id="msgXmp" name="msgXmp" style="display:none"><%=(reply.getMsgMain()==null?"":reply.getMsgMain())%></xmp>
													</label><br>
													<div id="fontFmTip"><emp:message key="znyq_ywgl_sxywgl_csgsw" defVal="参数格式为：“#P_1#”(如：我和#P_1#去#P_2#)" fileName="znyq"></emp:message></div>
													<input type="hidden" id="start" />
													<input type="hidden" id="end" />
												</div>
											
										</td>
										</tr>
										
										<tr> 
											<td colspan="2" id="btn">
												<input type="button" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" id="subReply" class="btnClass5 mr23"/>
												<input type="button" name="previous" id="previous" value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>"  onclick="javascript:window.parent.closeTemplateDiv()" class="btnClass6" />
												<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
												<br/>
											</td>
										</tr>
										</thead>
									</table>
								</form>			
				</div>
						<div class="clear"></div>
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
    <div class="clear"></div>
    
    	<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    	<script type="text/javascript" src="<%=iPath%>/js/replyAdd.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/ldg/lhgdialog.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
 		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath%>/js/textarea_limit.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
 		<script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
 		<script src="<%=commonPath %>/common/js/jquery.selection.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
		<script src="<%=commonPath %>/common/js/param_cg2.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
 		<script type="text/javascript" src="<%=iPath%>/js/insertpos.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
 		<script type="text/javascript" src="<%=iPath%>/js/eng_smsContentLength.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
 		<script type="text/javascript">
 		
 		$(document).ready(function(){
			$('.para_cg').gotoParam({'width':369,'textarea':'#msgMain'});

            //用textarea显示短信内容
            $("#msgMain").empty();
            $("#msgMain").text($("#msgXmp").text());

		    repMsg();
		    getSmsContentMaxLen('<%=serId %>');
		});
 		
 		function repMsg(){
			var repMsg = getChangeValX($("#msgMain"));
			$("#msgMain").val(repMsg);
		}
 		
		function show(){
		<% String result=(String)request.getAttribute("result");
				if(result!=null && "true" == result){%>
				//alert("操作成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czcg"));
				$("#previous").trigger("click");
			<%}else if(result!=null && "false" == result){%>
				//alert("操作失败！");	
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czsb"));	
		    <%  }%>
		}
		
		
    </script>
 		
	</body>
</html>
