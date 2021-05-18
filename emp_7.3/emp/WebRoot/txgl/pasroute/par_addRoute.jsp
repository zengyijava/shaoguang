<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.servmodule.txgl.entity.XtGateQueue" %>
<%@ page import="com.montnets.emp.servmodule.txgl.entity.Userdata" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	String path=request.getContextPath();
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	String inheritPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("route");
	
	String result = request.getAttribute("w_routeResult")==null?"-1"
			:(String)request.getAttribute("w_routeResult");
	request.removeAttribute("w_routeResult");
	Integer corpType = StaticValue.getCORPTYPE();
	@ SuppressWarnings("unchecked")
    List<XtGateQueue> gates = (List<XtGateQueue>)request.getAttribute("allXtList");
	@ SuppressWarnings("unchecked")
	List<Userdata> userList = (List<Userdata>)request.getAttribute("userdataList");
	Userdata user=null;
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	String txglFrame = skin.replace(commonPath, inheritPath);
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	//确定
	String qd = MessageUtils.extractMessage("common", "common_confirm", request);
	//返回
	String fh = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_fh", request);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_yyshdgl_lyxz" defVal="路由新增" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath%>/css/selcet.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
        <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />

        <%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/par_addRoute.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/par_addRoute.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="par_addRoute" onload="show(<%=result %>,<%=request.getParameter("lgcorpcode") %>,<%=request.getParameter("lguserid") %>)">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_xjly", request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%if(btnMap.get(menuCode+"-1")!=null){%>
				<div class="titletop">
					<table class="titletop_table tjdcspzhly_table" >
						<tr>
							<td class="titletop_td">
								<emp:message key="txgl_wgqdpz_yyshdgl_tjdcspzhly" defVal="添加短彩SP账号路由" fileName="txgl"></emp:message>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_fhsyj" defVal="返回上一级" fileName="txgl"></emp:message></font>
							</td>
						</tr>
					</table>
				</div>
            	<div id="detail_Info" class="detail_Info"> 
                <form name="updateInfo" method="post" action="par_route.htm">
                <div id="corpCode" class="hidden"></div>
                	<input type="hidden" value="add" name="hidOpType" id="hidOpType" />
                	<input type="hidden" value="update" name="method" id="update" />   
                	<input type="hidden" value="" name="gtId" id="gtId" /> <%-- 新增绑定通道账号时 存储通道账号ID --%>
                	<table class="userid_table">         	
       			 	<thead>
               	  		 <tr>
               	  		 <td class="userType_up_td"><span><%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_3",request)%></span></td>
               	  		  <td><label><input type="hidden" id="userType" value=""/>
                     	<select name="userid" id="userid" class="input_bd userid"  ><%-- onchange="javascript:getUserType()"> --%>
                     	  <%
						 	//@ SuppressWarnings("unchecked")
		                 	//List<Userdata> userList = (List<Userdata>)request.getAttribute("userdataList");
		                 	//Userdata user=null;
		                 	if(userList != null)
		                 	{
		                 	for(int i=0;i<userList.size();i++){
		                 		user=userList.get(i); 
		                 		if(user.getUserType()!=1 && !user.getUserId().equals(user.getLoginId())){
                		 %>
							<option accounttype="<%=user.getAccouttype() %>" value="<%=user.getUserId() %>" id="<%=user.getUserType() %>">
								<%=user.getUserId() %>(<%
							if (user.getAccouttype()-1==0){
								if (1 == (int)(user.getAccability()&0x00000001) && 4 == (int)(user.getAccability()&0x00000004)){
										// 此时SP账号类型为短信/富信
							               %>短信/富信<%
							    }else if (1 == (int)(user.getAccability()&0x00000001) && 4 != (int)(user.getAccability()&0x00000004)){
								// 此时SP账号类型为短信
							              %><%=MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_dx", request)%><%
							    }else if (4 == (int)(user.getAccability()&0x00000004) && 1 != (int)(user.getAccability()&0x00000001)){
								// 此时SP账号类型为富信
							              %>富信<%
								}
							}else if (user.getAccouttype()-2 == 0){
							%><%=MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_cx", request)%><%
							}else {
							%><%=""%><%
								}
							%>)
								<%--(<%=user.getAccouttype()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_dx", request):user.getAccouttype()-2==0?--%>
								<%--MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_cx", request):""%>)--%>
							</option>
                     	<%} } }%>
                     	</select></label></td>  
                     	</tr>
                     	<tr>    
                             <td class="lyzt_td"><span><emp:message key="txgl_wgqdpz_zhtdpz_lyzt" defVal="路由状态：" fileName="txgl"></emp:message></span></td>
                            <td>
                            <label>
	                            <select name="status" id="select" class="input_bd select_wi"  >
	                            	<option value="0"><emp:message key="txgl_wgqdpz_zhtdpz_qy" defVal="启用" fileName="txgl"></emp:message></option>
	                            	<option value="1"><emp:message key="txgl_wgqdpz_zhtdpz_jy" defVal="禁用" fileName="txgl"></emp:message></option>
	                            </select>
                            </label>
                            </td>
                         </tr>
                         <tr>
                         	<td><span><emp:message key="txgl_wgqdpz_zhtdpz_lylx" defVal="路由类型：" fileName="txgl"></emp:message></span></td>
                         	<td ><label>                          
                         	 <select  id="routeflag" name="routeflag" onchange="check(this.value)" class="input_bd select_wi"  >
                         	 		<option value="3"><emp:message key="txgl_wgqdpz_zhtdpz_qxzlylx" defVal="请选择路由类型" fileName="txgl"></emp:message></option>
                     	          <option value="1"><emp:message key="txgl_wgqdpz_zhtdpz_xxly" defVal="下行路由" fileName="txgl"></emp:message></option>
                     	          <option value="2"><emp:message key="txgl_wgqdpz_zhtdpz_sxly" defVal="上行路由" fileName="txgl"></emp:message></option>
                     	          <option value="0"><emp:message key="txgl_wgqdpz_zhtdpz_sxxly" defVal="上/下行路由" fileName="txgl"></emp:message></option>
                            </select>
                             </label>
                             </td>
                         </tr>
                         <tr>
                            <td><span><emp:message key="txgl_wgqdpz_zhtdpz_tdhm" defVal="通道号码：" fileName="txgl"></emp:message></span></td>
                            <td>
	                            <input type="text" class="input_bd gatebut"  name="spgate1" id="gatebut" size="20" />
	                            <label>
	                            <select name="spgate"  id="spgate"  class="input_bd spgate"  >
	                            </select></label>
	                            <input name="gatetype" id="gatetype" type="hidden" />
                            	<div class="ac_conteiner  input_bd birds_div"  >
								  <input  onclick="this.select()"  id="birds" class="ac_input birds"  autocomplete="off"/>
								  <div class="ac_img"></div>
								  <ul class="ac_results div_bd birds_ul"  >
								  </ul>
								</div>
                            </td>
                        </tr>
                     	<tr> 
                            <td><span><emp:message key="txgl_wgqdpz_zhtdpz_tdmc" defVal="通道名称：" fileName="txgl"></emp:message></span></td>
                         	<td>
                         		<label>
                         			<input type="text" name="spgateName" id="spgateName" readonly="readonly" onfocus="this.blur()"  class="input_bd spgateName" />
                         			<input type="hidden" name="spisuncm" id="spisuncm" readonly="readonly"/>
                         		</label>
                         	</td>
                         </tr>
                     	<tr class="smsattr"> 
                            <td><span><emp:message key="txgl_wgqdpz_zhtdpz_zcywdx" defVal="支持英文短信：" fileName="txgl"></emp:message></span></td>
                         	<td>
                         		<label>
                         			<input type="text" id="isSupportEn" value="" readonly="readonly" onfocus="this.blur()" class="input_bd isSupportEn"  />
                         		</label>
                         	</td>
                         </tr>
						<tr id="trone">
							<td>
								<span><emp:message key="txgl_wgqdpz_zhtdpz_tdkfszdcd" defVal="通道可发送最大长度：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<input type="text" name="maxwords" id="maxwords"
										readonly="readonly" value="" onfocus="this.blur()"   class="input_bd maxwords"  />
								</label>
							</td>
                     	</tr>
                     	<tr id="trtwo"> 							
							<td>
								<span><emp:message key="txgl_wgqdpz_zhtdpz_dtdxcd" defVal="单条短信长度：" fileName="txgl"></emp:message></span>
							</td>
							<td>
								<label>
									<input type="text" name="singlelen" id="singlelens"
										readonly="readonly" value="" onfocus="this.blur()"   class="input_bd singlelens"  />
								</label>
							</td>
						</tr>
						<tr id="trthree">
							<td>
								<span><emp:message key="txgl_wgqdpz_zhtdpz_dytcdxcd" defVal="第一条长短信长度：" fileName="txgl"></emp:message></span>
							</td>
							<td><label>
								<input type="text" id="multilen1" name="multilen1"
									readonly="readonly" value=""  onfocus="this.blur()"  class="input_bd multilen1" />
								</label>
							</td>
						</tr>
						<tr id="trfour">
							<td>
								<span><emp:message key="txgl_wgqdpz_zhtdpz_zhytcdxcd" defVal="最后一条长短信长度：" fileName="txgl"></emp:message></span>
							</td>
							<td>
							<label>
								<input type="text" id="multilen2" name="multilen2"
									readonly="readonly" value="" onfocus="this.blur()"  class="input_bd multilen2" />
								</label>
							</td>
						</tr>

						<tr>
							<td>
								<span class="signlabel"></span><span><emp:message key="txgl_wgqdpz_zhtdpz_dxqmm" defVal="短信签名：" fileName="txgl"></emp:message></span>
							</td>
							<td><label>
								<input type="text" name="signstr" id="sms" <%if(StaticValue.getCORPTYPE() == 0){%>disabled="disabled" style="background-color: #E8E8E8;" <%}%> class="input_bd sign" />
								</label>
							</td>
						</tr>
                          <tr class="dxqmm_tr">
                              <td>
                                  <span class="signlabel"></span><span><emp:message key="txgl_wgqdpz_zhtdpz_dxqmm" defVal="短信签名：" fileName="txgl"></emp:message></span>
                              </td>
                              <td><label>
                                  <input type="text" name="ensignstr" id="ensms" <%if(StaticValue.getCORPTYPE() == 0){%>disabled="disabled" style="background-color: #E8E8E8;"<%}%> class="input_bd sign"/>
                              </label>
                              </td>
                          </tr>
						<tr>
                         	<td><span><emp:message key="txgl_wgqdpz_zhtdpz_zdsptdkzws" defVal="最大SP通道扩展位数：" fileName="txgl"></emp:message></span></td>
                         	<td><label><input type="text" name="sublen" id="sublen" onfocus="this.blur()"  class="input_bd sublen" /></label></td>
                         </tr>
						<tr id="trfive">
						<td><span><emp:message key="txgl_wgqdpz_zhtdpz_dxqmcdms" defVal="短信签名长度模式：" fileName="txgl"></emp:message></span></td>
						 <td>
                        <label>
                        <select name="signMode2" id="signMode" disabled="disabled" onchange="changeSignMode(this.value)"  class="input_bd signMode"  >
                        	<option value="0"><emp:message key="txgl_wgqdpz_zhtdpz_zdjs" defVal="自动计算" fileName="txgl"></emp:message></option>
                        	<option value="1"><emp:message key="txgl_wgqdpz_zhtdpz_gdcd" defVal="固定长度" fileName="txgl"></emp:message></option>
                        </select>
                        <input type="hidden" id="signMode2" name="signMode" value=""/>
                        </label>
                        </td>     
                       	</tr>
						<tr id="trsix">
							<td>
								<span><emp:message key="txgl_wgqdpz_zhtdpz_dxqmcd" defVal="短信签名长度：" fileName="txgl"></emp:message></span>
							</td>
							<%--<td><label>
								<input type="text" id="smslen" readonly="readonly"
									name="signlen" value=""  style="background-color: #E8E8E8"/>
								</label>
							</td>
						--%>
						<td><label id="smslen">0</label>
                     	<label><input  name="signlen"  type="hidden"  value ="" id="signlen2" /></label>
                     	</td>  
						</tr>
                          <tr>
                         	<td><span><emp:message key="txgl_wgqdpz_zhtdpz_zhsd" defVal="子号设定：" fileName="txgl"></emp:message></span></td>
                         	<td><label><input type="text" name="cpno" id="cpno" onkeyup="value=value.replace(/[^\d]/g,'')" class="input_bd cpno"  /></label></td>
                         						</tr>

<%--                          <tr>--%>
<%--                         	<td><span>起始发送时间：</span></td>--%>
<%--                            <td><label><input type="text" id="sendtimebegin"  name="sendtimebegin" onclick="WdatePicker({dateFmt:'HH:mm:ss'})" readonly="readonly" style="cursor: pointer; background-color: white;width: 260px;"   class="Wdate" style="cursor:hand;"  value="00:00:00"/></label></td>--%>
<%--                         						</tr>--%>
<%--						<tr>--%>
<%--                            <td><span>结束发送时间：</span></td>--%>
<%--                            <td><label><input type="text" id="sendtimeend"  name="sendtimeend"  onclick="WdatePicker({dateFmt:'HH:mm:ss'})" readonly="readonly" style="cursor: pointer; background-color: white;width: 260px;"   class="Wdate" style="cursor:hand;" value="23:59:59" /></label></td>--%>
<%--                         </tr>--%>
                         <tr>
                         	<td><span><emp:message key="txgl_wgqdpz_zhtdpz_lyms" defVal="路由描述：" fileName="txgl"></emp:message></span></td>
                            <td><label><input type="text" name="memo" maxlength="33" value="" class="input_bd lyms_down_input"  /></label></td>
                        </tr>
						<tr>
                            <td><div id="usercodediv"><span><emp:message key="txgl_wgqdpz_zhtdpz_zldm" defVal="指令代码：" fileName="txgl"></emp:message></span></div></td>
                            <td><div id="usercodediv2"><label><input type="text" id="usercode" name="usercode" maxlength="10" value="" class="input_bd usercode"  onkeyup="value=value.replace(/[^\w]/g,'')" /></label></div></td>                            
                         </tr>
                    
                         <tr align="left">
                            
                         	<td id="btn" colspan="2">
                         	  <div  align="right" class="mt10 btn_div">
                         		<input type="button" onclick="checkNull()" id ="arOk" value="<%=qd %>" class="btnClass5 mr23"/>
                         		<input type="button"  id="arNo" value="<%=fh %>" class="btnClass6" onclick="back()"/>
                         		</div>
                         	</td>
                         </tr>
                  </thead>
                </table>
                </form>
                </div>
                <%} %>
			</div>
			<%-- 内容结束 --%>
			<div class="clear"></div>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
		</div>
    <div class="clear"></div>
    <script language="javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script language="javascript" src="<%=iPath%>/js/route.js?V=<%=StaticValue.getJspImpVersion()%>" type="text/javascript"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
		  getLoginInfo("#corpCode");
		  setSpgate();
		  
		  $("#userid").change(function(){
			  setSpgate();
			});
		  $('#sms').css({'width':'260px'});
         /** $(this).click(function(){
              //处理可输入下拉框在未选择下拉选项时的默认填值
          
              var $selectBox = $('#userid').next('.c_selectBox');
              $selectBox.children(".c_input").val($('#userid option:selected').text());
              $selectBox.children(".c_result").hide();
              $selectBox = $(".ac_conteiner");
              $selectBox.children(".ac_input").val($('#spgate option:selected').text());
              $selectBox.children(".ac_result").hide();

          });*/
            $('#userid').parents("tr").click(function(event){
                event.stopPropagation();
            });
            $("#gatetype").parents("tr").click(function(event){
                event.stopPropagation();
            });

	  });
		  function setSpgate()
		  {
              $('.signlabel').empty();
              //隐藏英文签名行
              $('#ensms').closest('tr').hide();
              $('.sign').empty();
			  	$('#userType').val($('#userid').find('> option[selected]').attr('id'));
				if ($('#userid').find('> option[selected]').attr('accounttype') == "1")
				{
					$("#spgate").empty();
					$("#spgate").append("<option value=\"0\"></option>");
                      <%
                           if(gates != null)
                           {
                            	for(int index = 0;index<gates.size();index++){
                            	 XtGateQueue xgq = gates.get(index);
//                            	 String spateStr=(xgq.getSpgate()!=null&&xgq.getSpgate().length()>=3 ?xgq.getSpgate().substring(0,3):"");
                            	 if(xgq.getGateType()==1){
                            	 %>
                            	 //var lt = getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_83");
                            	 //alert(lt);
								$("#spgate").append("<option title=\"<%=xgq.getId()%>\" value=\"<%=xgq.getSpgate() %>\" id=\"s<%=xgq.getSpisuncm()%>\"><%=xgq.getSpgate() %>(<%=xgq.getSpisuncm()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_lt", request):xgq.getSpisuncm()-0==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_yd", request):xgq.getSpisuncm()-21==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_dx", request):xgq.getSpisuncm()-5==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_gw", request):""%><%=xgq.getGateType()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_dx", request):xgq.getGateType()-2==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_cx", request):xgq.getGateType()-3==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_dchh", request):""%>)</option>");                            
                     <%} } }%>
                     $('.smsattr').show();
                     $("#trone").show();
                     $("#trtwo").show();
                     $("#trthree").show();
                      $("#trfour").show();
                     $("#trfive").show();
                      $("#trsix").show();
				}
				else
				{
					$("#spgate").empty();
					$("#spgate").append("<option value=\"0\"></option>");
                      <%
                           if(gates != null)
                           {
                            	for(int index = 0;index<gates.size();index++){
                            	 XtGateQueue xgq = gates.get(index);
                            	 if(xgq.getGateType()==2){
                            	 %>
								$("#spgate").append("<option title=\"<%=xgq.getId()%>\" value=\"<%=xgq.getSpgate() %>\" id=\"s<%=xgq.getSpisuncm()%>\"><%=xgq.getSpgate() %>(<%=xgq.getSpisuncm()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_lt", request):xgq.getSpisuncm()-0==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_yd", request):xgq.getSpisuncm()-21==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_dx", request):xgq.getSpisuncm()-5==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_gw", request):""%><%=xgq.getGateType()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_dx", request):xgq.getGateType()-2==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_cx", request):xgq.getGateType()-3==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_dchh", request):""%>)</option>");                            
                     <%} } }%>
                     $('.smsattr').hide();
                     $("#trone").hide();
                     $("#trtwo").hide();
                     $("#trthree").hide();
                      $("#trfour").hide();
                     $("#trfive").hide();
                      $("#trsix").hide();
				}
				getSublen("");
				$('.ac_results').hide();
				$("#birds").val($("#spgate>option:selected").text());
		  }

        $('#userid').isSearchSelect({'width':'263','isInput':true,'zindex':0},function(selectObj){
            //回调函数触发时机 下拉框选择 或 输入框点击与键盘向上
            //下拉框选择时
            if(selectObj.isSelect){
                $("#userid").change();
            }

        });
        $('#select,#routeflag').isSearchSelect({'width':'263','isInput':false,'zindex':0});
    </script>
	</body>
</html>
