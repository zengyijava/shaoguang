<%@ page language="java" import="java.util.List" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.pasroute.GtPortUsed" %>
<%@ page import="com.montnets.emp.servmodule.txgl.entity.XtGateQueue" %>
<%@ page import="java.util.ArrayList" %>
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
	
	
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	Long id=Long.valueOf(request.getParameter("id"));
	String keyId=request.getParameter("keyId");
	GtPortUsed gt= (GtPortUsed) request.getAttribute("gtPort");
	XtGateQueue xt = (XtGateQueue) request.getAttribute("xtGate");
	int privilege = xt.getGateprivilege();
	boolean isSupportEn = (privilege&2)==2;
	int signMode = xt.getSignDropType()-0==0?1:0;
	int signlen = signMode==1?xt.getSignlen():gt.getSignstr().trim().length();
	int ensignlen = signMode==1?xt.getEnsignlen():gt.getEnsignstr().trim().replaceAll("[\\[\\]\\|\\^\\{\\}\\~\\\\]", "**").length();
    int signMaxLen = signMode == 1? xt.getSignlen():20;
    int ensignMaxLen = signMode == 1 ? xt.getEnsignlen() : 20;
	String maxWordStr = isSupportEn?getContent(xt.getMaxWords()+"",xt.getEnmaxwords()+""):xt.getMaxWords()+"";
	String singleLenStr = isSupportEn?getContent(xt.getSingleLen()+"",xt.getEnsinglelen()+""):xt.getSingleLen()+"";
	String multilen1Str = isSupportEn?getContent(xt.getMultilen1()+"",xt.getEnmultilen1()+""):xt.getMultilen1()+"";
	String multilen2Str = isSupportEn?getContent(gt.getMultilen2()+"",gt.getEnmultilen2()+""):gt.getMultilen2()+"";
	String signlenStr = isSupportEn?getContent(signlen+"",ensignlen+""):signlen+"";
	String signStr = isSupportEn?getContent(gt.getSignstr()+"",gt.getEnsignstr()+""):gt.getSignstr()+"";
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("route");
	Integer corpType = StaticValue.getCORPTYPE();
	String result = request.getAttribute("w_routeResult")==null?"-1"
			:(String)request.getAttribute("w_routeResult");
	request.removeAttribute("w_routeResult");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    String txglFrame = skin.replace(commonPath, inheritPath);
    
    //确定：
	String qd = MessageUtils.extractMessage("common", "common_confirm", request);
   //返回
    String fh = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_dcspzh_fh", request);
    
%>
<%!
public String getContent(String c1,String c2){
	return "中文："+c1+"  "+"英文："+c2;
	//return MessageUtils1.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_zw", request)+c1+"  "+MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_yw", request)+c2;
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_zhtdpz_xgly" defVal="修改路由" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
        
        <%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/par_editRoute.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/par_editRoute.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="par_editRoute" onload="show(<%=result %>,<%=request.getParameter("lgcorpcode") %>,<%=request.getParameter("lguserid") %>)">
		<div id="container" class="container">
			<%--  当前位置
			<%=com.montnets.emp.servmodule.txgl.biz.ViewParams.getPosition(menuCode,"修改路由") %>
			 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode,MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_xgly", request)) %>
			
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent rContent2"  >
			<%if(btnMap.get(menuCode+"-3")!=null){%>
			<div class="titletop">
					<table class="titletop_table xgdcspzhly_table"  >
						<tr>
							<td class="titletop_td">
								<emp:message key="txgl_wgqdpz_zhtdpz_xgdcspzhly" defVal="修改短彩SP账号路由" fileName="txgl"></emp:message>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="txgl_wgqdpz_dcspzh_fhsyj" defVal="返回上一级" fileName="txgl"></emp:message></font>
							</td>
						</tr>
					</table>
				</div>
            <div id="detail_Info" class="detail_Info"> 
            
                <form name="updateInfo" method="post" action="<%=path %>/par_route.htm">
                <div id="corpCode" class="hidden"></div>
                	<input type="hidden" value="edit" id="hidOpType" name="hidOpType" />
                	<input type="hidden" value="update" id="hidOpType" name="method" />
                	<input type="hidden" value="<%=gt.getFeeFlag() %>" id="feeFlag" name="feeFlag" /> 
            		<input type="hidden" value="<%=id %>" name="id" /> 
            		<input type="hidden" value="<%=keyId %>" name="keyId" />       
            		<input type="hidden" value="<%=xt.getId() %>" name="gtId" id="gtId" /> <%-- 修改时 存储通道账号ID --%>
                	<table class="userType_table">         	
       			 	<thead>
               	  		 <tr>
               	  		 <td  class="userType_up_td"><span><%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_1",request) %>：</span></td>
               	  		  <td>
               	  		    <label><input type="hidden" id="userType" value="<%=gt.getUserId().equals(gt.getLoginId())?1:0 %>" />
               	  		    	<%=gt.getUserId() %><input type="hidden" value="<%=gt.getUserId() %>" name="userId" id="userId"/>
               	  		    	<input type="hidden" value="<%=gt.getLoginId() %>" name="loginId"/>
                     		</label>
                     		</td> 
                     		</tr><tr>  
                             <td><span><emp:message key="txgl_wgqdpz_zhtdpz_lyzt" defVal="路由状态：" fileName="txgl"></emp:message></span></td>
                            <td>
                            <label>
	                            <select name="status" id="select" class="input_bd select_wd" >
	                            	<option value="0"><emp:message key="txgl_wgqdpz_zhtdpz_qy" defVal="启用" fileName="txgl"></emp:message></option>
	                            	<option value="1" <%=gt.getStatus()-1==0?"selected":"" %>><emp:message key="txgl_wgqdpz_zhtdpz_jy" defVal="禁用" fileName="txgl"></emp:message></option>
	                            </select>
                            </label>
                            </td>
                         </tr>
                         <tr>
                         	<td><span><emp:message key="txgl_wgqdpz_zhtdpz_lylx" defVal="路由类型：" fileName="txgl"></emp:message></span></td>
                         	<td><%=gt.getRouteFlag()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_xxly", request):"" %>
                         	<%=gt.getRouteFlag()-2==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_sxly", request):"" %>
                         	<%=gt.getRouteFlag()-0==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_sxxly", request):"" %>
                         	<input type="hidden" value="<%=gt.getRouteFlag() %>" id="routeflag" name="routeflag"/>
                             </td>
                         </tr>
                         <tr>
                            <td><span><emp:message key="txgl_wgqdpz_zhtdpz_tdhm" defVal="通道号码：" fileName="txgl"></emp:message></span></td>
                            <td>
                            	<%-- 
                            	<input type="hidden" value="<%=gt.getSpgate() %>" id="spgate" name="spgate"/><%=gt.getSpgate() %>(<%=gt.getSpisuncm()-1==0?"联通":gt.getSpisuncm()-0==0?
												"移动":gt.getSpisuncm()-21==0?"电信":gt.getSpisuncm()-5==0?"国外":""%><%=gt.getGateType()-1==0?"短信":gt.getGateType()-2==0?
												"彩信":""%>)
								--%>				
								<input type="hidden" value="<%=gt.getSpgate() %>" id="spgate" name="spgate"/><%=gt.getSpgate() %>(<%=gt.getSpisuncm()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_lt", request):gt.getSpisuncm()-0==0?
												MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_yd", request):gt.getSpisuncm()-21==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_yyshdgl_dx", request):gt.getSpisuncm()-5==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_gw", request):""%><%=gt.getGateType()-1==0?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_dx", request):gt.getGateType()-2==0?
												MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_cx", request):""%>)			
								 <input name="gatetype" id="gatetype" type="hidden" value="<%=gt.getGateType() %>" />
                            </td>
                            </tr><tr>
                            <td><span><emp:message key="txgl_wgqdpz_zhtdpz_tdmc" defVal="通道名称：" fileName="txgl"></emp:message></span></td>
                         	<td>
                         		<label>
                         			<input  type="text" name="spgateName" class="input_bd userType_table_input" value="<%=xt.getGateName()%>" id="spgateName" readonly="readonly" onfocus="this.blur()"/>
                         			<input type="hidden" name="spisuncm" id="spisuncm" value="<%=xt.getSpisuncm() %>" readonly="readonly"/>
                         		</label>
                         	</td>
                         </tr>
                         <%  if(gt.getGateType()-1==0){ %>
                                              	<tr class="smsattr"> 
						                            <td><span><emp:message key="txgl_wgqdpz_zhtdpz_zcywdx" defVal="支持英文短信：" fileName="txgl"></emp:message></span></td>
						                         	<td>
						                         		<label>
						                         			<input type="text" id="isSupportEn" value="<%=isSupportEn?MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_s", request):MessageUtils.extractMessage("txgl", "txgl_wgqdpz_zhtdpz_f", request) %>" readonly="readonly" onfocus="this.blur()" class="input_bd userType_table_input" />
						                         		</label>
						                         	</td>
						                         </tr>
												<tr>
												<td>
													<span><emp:message key="txgl_wgqdpz_zhtdpz_tdkfszdcd" defVal="通道可发送最大长度：" fileName="txgl"></emp:message></span>
												</td>

												<td>
													<label>
														<input   type="text" class="input_bd userType_table_input" name="maxwords" id="maxwords"
															readonly="readonly" value="<%=maxWordStr %>" onfocus="this.blur()"/>
													</label>
												</td>
												</tr><tr>
												<td>
													<span><emp:message key="txgl_wgqdpz_zhtdpz_dtdxcd" defVal="单条短信长度：" fileName="txgl"></emp:message></span>
												</td>
												<td>
													<label>
														<input  class="input_bd userType_table_input" type="text" name="singlelen" id="singlelens"
															readonly="readonly" value="<%=singleLenStr %>" onfocus="this.blur()"/>
													</label>
												</td>
											</tr>
											<tr>
												<td>
													<span><emp:message key="txgl_wgqdpz_zhtdpz_dytcdxcd" defVal="第一条长短信长度：" fileName="txgl"></emp:message></span>
												</td>
												<td><label>
													<input  class="input_bd userType_table_input" type="text" id="multilen1" name="multilen1"
														readonly="readonly" value="<%=multilen1Str%>" onfocus="this.blur()"/>
													</label>
												</td>
												</tr>
												<tr>
												<td>
													<span><emp:message key="txgl_wgqdpz_zhtdpz_zhytcdxcd" defVal="最后一条长短信长度：" fileName="txgl"></emp:message></span>
												</td>
												<td>
												<label>
													<input  class="input_bd userType_table_input" type="text" id="multilen2" name="multilen2"
														readonly="readonly" value="<%=multilen2Str %>" onfocus="this.blur()"/>
													</label>
												</td>
											</tr>
												<tr>
											 <td>
													<span><emp:message key="txgl_wgqdpz_zhtdpz_dxqmcd" defVal="短信签名长度：" fileName="txgl"></emp:message></span>
												</td>
												<td><label id="smslen"><%=signlenStr %></label>
                     								<label><input name="signlen" value="<%=gt.getSignlen() %>" type="hidden" id="signlen2" /></label>
					                     	</td>  
					                     	</tr>
					                     	<tr>
											<td><span><emp:message key="txgl_wgqdpz_zhtdpz_dxqmcdms" defVal="短信签名长度模式：" fileName="txgl"></emp:message></span></td>
					                        <td>
					                        <label>
					                        <select name="signMode2" disabled="disabled" id="signMode" class="input_bd select_wd"  onchange="changeSignMode(this.value)">
					                        	<option value="0"><emp:message key="txgl_wgqdpz_zhtdpz_zdjs" defVal="自动计算" fileName="txgl"></emp:message></option>
					                        	<option value="1" <%=signMode==0?"":"selected" %>><emp:message key="txgl_wgqdpz_zhtdpz_gdcd" defVal="固定长度" fileName="txgl"></emp:message></option>
					                        </select>
					                        <input type="hidden" id="signMode2" name="signMode" value="<%=gt.getSignlen()==0?0:1%>"/>
					                        </label>
					                        </td>         
											</tr>
								<%} %>
								 <tr>
                         	<td><span><emp:message key="txgl_wgqdpz_zhtdpz_zdsptdkzws" defVal="最大SP通道扩展位数：" fileName="txgl"></emp:message></span></td>
                         	<td><label><input   class="input_bd userType_table_input" type="text" name="sublen" value="<%=xt.getSublen() %>" id="sublen" readonly="readonly" onfocus="this.blur()"/></label></td>
                         </tr>
						<tr>
                              <td>
                                  <span class="signlabel"></span><span><emp:message key="txgl_wgqdpz_zhtdpz_dxqmm" defVal="短信签名：" fileName="txgl"></emp:message></span>
                              </td>
                              <td><label>
                                  <input type="text" name="signstr" id="sms" value="<%=gt.getSignstr()==null?"":gt.getSignstr().trim()%>" maxlength="<%=signMaxLen%>"
                                         <%if(StaticValue.getCORPTYPE() == 0){%>disabled="disabled" style="background-color: #E8E8E8;" <%}%> class="input_bd sign" />
                              </label>
                              </td>
                              </tr>
                              <tr class="dxqmm_tr">
                                  <td>
                                      <span class="signlabel"></span><span><emp:message key="txgl_wgqdpz_zhtdpz_dxqmm" defVal="短信签名：" fileName="txgl"></emp:message></span>
                                  </td>
                                  <td><label>
                                      <input type="text" name="ensignstr" id="ensms" value="<%=gt.getEnsignstr()==null?"":gt.getEnsignstr().trim()%>" maxlength="<%=ensignMaxLen%>"
                                             <%if(StaticValue.getCORPTYPE() == 0){%>disabled="disabled" style="background-color: #E8E8E8;" <%}%> class="input_bd sign"/>
                                  </label>
                                  </td>
                              </tr>
                          <tr>
                         	<td><span><emp:message key="txgl_wgqdpz_zhtdpz_zhsd" defVal="子号设定：" fileName="txgl"></emp:message></span></td>
                         	<td><label><input type="text" name="cpno" id="cpno" class="input_bd table_input2"  value="<%=gt.getCpno().trim() %>"  onkeyup="value=value.replace(/[^\d]/g,'')"/></label>
                         		 <input type="hidden" value="<%=gt.getCpno() %>" id="oldCpno" name="oldCpno"/>
                         		 </td>
                         </tr>
                        
<%--                          <tr>--%>
<%--                         	<td><span>起始发送时间：</span></td>--%>
<%--                            <td><label><input type="text" id="sendtimebegin"  name="sendtimebegin" value="<%=gt.getSendTimeBegin() %>" readonly="readonly"  style="cursor: pointer; background-color: #E8E8E8;width: 260px;"   value="00:00:00" onfocus="this.blur()"/></label></td>--%>
<%--                          </tr>--%>
<%--                          <tr>--%>
<%--                            <td><span>结束发送时间：</span></td>--%>
<%--                            <td><label><input type="text"  id="sendtimeend"  name="sendtimeend" value="<%=gt.getSendTimeEnd() %>" readonly="readonly" style="cursor: pointer; background-color: #E8E8E8;width: 260px;" value="23:59:59" onfocus="this.blur()"/></label></td>--%>
<%--                         </tr>--%>
                         <tr>
                         	<td><span><emp:message key="txgl_wgqdpz_zhtdpz_lyms" defVal="路由描述：" fileName="txgl"></emp:message></span></td>
                            <td  ><label><input type="text" class="input_bd table_input2"  maxlength="33" name="memo" id="memo" value="<%=gt.getMemo()==null?"":gt.getMemo().replace("&","&amp;").replace("\"","&quot;") %>"/></label></td>
                         </tr>
                         <%if(gt.getRouteFlag()-1!=0) {%>
                         <tr>
                            <td><div id="usercodediv"><span><emp:message key="txgl_wgqdpz_zhtdpz_zldm" defVal="指令代码：" fileName="txgl"></emp:message></span></div></td>
                            <td><div id="usercodediv2"><label>
                            	<input type="text" id="usercode" class="input_bd table_input2"   name="usercode" maxlength="10" value="<%=gt.getRouteFlag()!=1?gt.getUserCode().trim():"" %>"  onkeyup="value=value.replace(/[^\w]/g,'')"/></label></div></td>
                         </tr>
                         <%} %>
                         <tr align="left">
                         	<td id="btn" colspan="2">
                         	  <div  class="mt10 btn_div">
                         		<input type="button" onclick="checkEditNull()" id ="arOk" value="<%=qd %>" class="btnClass5 mr23"/>
                         		<input type="button"  id="arNo" value="<%=fh %>" class="btnClass6" onclick="javascript:back()"/>
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
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
			  getLoginInfo("#corpCode");
			  });
			  <%if(isSupportEn){%>
                    $('#ensms').closest('tr').show();
                    //$('.signlabel:eq(0)').text('中文');
                    $('.signlabel:eq(0)').text(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_90"));
                    $('.signlabel:eq(1)').text(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_91"));
			<%  }%>
		</script>
	</body>
</html>
