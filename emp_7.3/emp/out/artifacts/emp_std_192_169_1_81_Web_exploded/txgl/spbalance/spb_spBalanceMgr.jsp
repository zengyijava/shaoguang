<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.spbalance.vo.LfSpFeeAlarmVo"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfPageField"%>
<%@page import="com.montnets.emp.spbalance.vo.UserfeeVo"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.beanutils.BasicDynaBean" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>

<%
	SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
	String path = request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@SuppressWarnings("unchecked")
	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>) request.getAttribute("conditionMap");
	@SuppressWarnings("unchecked")
	List<DynaBean> ufdynlist = (List<DynaBean>) request.getAttribute("ufdynlist");

	@SuppressWarnings("unchecked")
	Map<String,List<LfSpFeeAlarmVo>> userAlarmMap=(Map<String,List<LfSpFeeAlarmVo>>)request.getAttribute("userAlarmMap");
	@SuppressWarnings("unchecked")
	Map<String, String> btnMap = (Map<String, String>) session
			.getAttribute("btnMap");//按钮权限Map

	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session
			.getAttribute("titleMap");
	String menuCode = titleMap.get("spBalanceMgr");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String skin = session.getAttribute("stlyeSkin") == null ? "default"
			: (String) session.getAttribute("stlyeSkin");
				@SuppressWarnings("unchecked")
	List<LfPageField> pagefileds=(List<LfPageField>)request.getAttribute("pagefileds");
	String txglFrame = skin.replace(commonPath, inheritPath);
	UserfeeVo ufvorq=(UserfeeVo)request.getAttribute("ufvorq");
	if(ufvorq==null){
		ufvorq=new UserfeeVo();
	}
	HashMap<String,String> encryptmap=   (HashMap)request.getAttribute("encryptmap");
	
	//充值
	String cz = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_cz", request);
    //if(tdhm!=null&&tdhm.length()>1){
    //	tdhm = tdhm.substring(0,tdhm.length()-1);
    // }
	
	//取消
	String qx = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_qx", request);
	//回收
	String hs = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_hs", request);
	//告警阀值设置
	String gjfzsz = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_gjfzsz", request);
	//批量充值/回收
	String plczhs = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_plczhs", request);
	//日志查看
	String rzck = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_rzck", request);
	//输入SP账号
	String srspzzh = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_srspzzh", request);
	//应用类型：
	String yylx = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_yylx", request);
    if(yylx!=null&&yylx.length()>1){
    	yylx = yylx.substring(0,yylx.length()-1);
    }
	//通知人详情
	String tzrxq = MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_tzrxq", request);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<title><emp:message key="txgl_wgqdpz_spzhczhs_spzhczhs" defVal="SP账号充值/回收" fileName="txgl"></emp:message></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
	
		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=iPath%>/css/spbalance_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/spb_spBalanceMgr.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="spb_spBalanceMgr">
		<input type="hidden" id="useridstr"/>
		<div id="container" class="container">
			<%-- 当前位置 --%>
				<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName,menuCode) %>
			
			<%-- 内容开始 --%>
			<form name="pageForm" action="spb_spBalanceMgr.htm" method="post"
					id="pageForm">
					<div id="corpCode" class="hidden"></div>
			<%
				if (btnMap.get(menuCode + "-0") != null) {
			%>
			<div id="addBalance" title="<%=cz %>" class="hidden addBalance"  >
				<center>
			    <table>
			    <tr><td class="spzzh_up_tr_td"></td></tr>
			        <tr>
			           <td class="<%="zh_HK".equals(empLangName)?"spzzh_td1":"spzzh_td2"%>"  ><emp:message key="txgl_wgqdpz_spzhczhs_spzzh" defVal="SP账号：" fileName="txgl"></emp:message></td>
			           <td align="left" >
			              <span id="addspuser" class="addspuser"><xml></xml></span>
			            </td>
			        </tr>
			        <tr><td class="addBalance_tr_td"></td></tr>
			        <tr>
			           <td  align="left"><emp:message key="txgl_wgqdpz_spzhczhs_czts" defVal="充值条数：" fileName="txgl"></emp:message></td>
			           <td align="left"><input type="text" id="addCount" class="input_bd addCount"  maxlength="9" onkeyup="if(value!=value.replace(/[^\w\/]/g,''))value=value.replace(/[^\w\/]/g,'')"/>&nbsp;<emp:message key="txgl_wgqdpz_spzhczhs_t" defVal="条" fileName="txgl"></emp:message></td>
			        </tr>
			        <tr><td class="addBalance_tr_td"></td></tr>
			         <tr>
			           <td  align="left"><emp:message key="txgl_wgqdpz_spzhczhs_b" defVal="备&nbsp;&nbsp;&nbsp;&nbsp;注：" fileName="txgl"></emp:message></td>
			           <td align="left"><textarea  id="addMark" cols="21" rows="5" class="addMark"></textarea></td>
			        </tr>
			        <tr><td class="addBalance_tr_td"></td></tr>
			         <tr>
			           <td colspan="2" class="cz_td"><center><input type="button" class="btnClass5 mr23" value="<%=cz %>" onclick="javascript:chongZhi();">
			           <input type="button" class="btnClass6" value="<%=qx %>" onclick="javascript:quXiao('1');"></center></td>
			        </tr>
			    </table>
			    </center>
			</div>
			<div id="delBalance" title="<%=hs %>" class="hidden delBalance"  >
			<center>
			    <table>
			    <tr><td class="delBalance_tr_td"></td></tr>
			        <tr>
			           <td class="<%="zh_HK".equals(empLangName)?"spzzh_td1":"spzzh_td2"%>" ><emp:message key="txgl_wgqdpz_spzhczhs_spzzh" defVal="SP账号：" fileName="txgl"></emp:message></td>
			           <td align="left" >
			              <span id="delspuser" class="delspuser"><xml></xml></span>
			            </td>
			        </tr>
			        <tr><td class="delBalance_tr_td"></td></tr>
			        <tr>
			           <td  align="left"><emp:message key="txgl_wgqdpz_spzhczhs_hsts" defVal="回收条数：" fileName="txgl"></emp:message></td>
			           <td><input type="text" id="recCount" class="input_bd recCount"   maxlength="9" onkeyup="if(value!=value.replace(/[^\w\/]/g,''))value=value.replace(/[^\w\/]/g,'')"/>&nbsp;<emp:message key="txgl_wgqdpz_spzhczhs_t" defVal="条" fileName="txgl"></emp:message></td>
			        </tr>
			        <tr><td class="delBalance_tr_td"></td></tr>
			        <tr>
			           <td  align="left"><emp:message key="txgl_wgqdpz_spzhczhs_b" defVal="备&nbsp;&nbsp;&nbsp;&nbsp;注：" fileName="txgl"></emp:message></td>
			           <td align="left"><textarea id="recMark" class="recMark"  cols="21" rows="5"></textarea></td>
			        </tr>
			         <tr><td class="delBalance_tr_td"></td></tr>
			        <tr>
			           <td colspan="2" class="hs_td"><center><input type="button" class="btnClass5 mr23" value="<%=hs %>" onclick="javascript:huiShou();">
			           <input type="button" class="btnClass6" value="<%=qx %>" onclick="javascript:quXiao('2');"></center></td>
			        </tr>
			    </table>
			    </center>
			</div>
			<%-- 设置阀值 --%>
			<div id="setAlarm" title="<%=gjfzsz %>" class="setAlarm">
				<iframe id="editAlarmFrame" name="editAlarmFrame" class="<%="zh_HK".equals(empLangName)?"editAlarmFrame1":"editAlarmFrame2"%>"  marginwidth="0"  frameborder="no"  ></iframe>
			</div>
			
			<%-- 批量充值/回收 --%>
			<div id="addAllSpuserDiv" title="<%=plczhs %>" class="addAllSpuserDiv">
				<iframe id="addAllSpuserFrame" name="addAllSpuserFrame" class="<%="zh_HK".equals(empLangName)?"addAllSpuserFrame1":"addAllSpuserFrame2"%>" marginwidth="0" frameborder="no"  ></iframe>
			</div>
			
			<div id="rContent" class="rContent">
					<table class="rContent_table">
					<tbody>
						<tr >
							<td class="rzck_td">
							<input type="button" class="btnClass4" onclick="javascript:toLog()" value="<%=rzck %>"/>
							<% if( btnMap.get(menuCode + "-4") != null) {%>
							<input type="button" class="btnClass4" onclick="javascript:addBalanceAll()" value="<%=plczhs %>"/>
							<%} %>
							</td>
							<td class="plczhs_down_td">
							</td>
							</tr>	
					</tbody>
					</table>
					<div id="condition">
						<table>
							<tr>
							<td>
								<span><%=MessageUtils.extractMessage("txgl","txgl_mwgateway_text_3",request)%></span>
							</td>
							<td>
								<label>
								<%
									String spuser = ufvorq.getSpuser();
								%>
									<input type="text" name="spuser" class="spuser" id="spuser" value="<%=spuser == null ? "" : spuser.replace("&", "&amp;")
						.replace("\"", "&quot;")%>"
										onkeyup="if(value!=value.replace(/[^\w\/]/g,''))value=value.replace(/[^\w\/]/g,'')" placeholder="<%=srspzzh %>"/>
								</label>
							</td>
							<%--
							<%
							String typename="";
							if(pagefileds!=null&&pagefileds.size()>0){
								LfPageField first=pagefileds.get(0);
								typename=first.getField()+"：";
								if(0==first.getFiledShow() &&pagefileds.size()>2 )
								{
							%>
								<td>
									<%=typename %>
								</td>
								<td>
								<%
									String accountType = ufvorq.getAccounttype();
								%>
									<select name="accounttype" id="accounttype"
										style="width: 158px;">
										<option value="">
											全部
										</option>
										<%
											for(int i=1;i<pagefileds.size();i++)
											{
											LfPageField pagefid=pagefileds.get(i);
										%>
										     <option value="<%=pagefid.getSubFieldValue() %>" <%=pagefid.getSubFieldValue().equals(accountType)?"selected":"" %>><%=pagefid.getSubFieldName() %></option>
										<% 
											}
										%>
									</select>
								</td>
								<%
									}
									else{out.print("<td></td><td></td>");
									}
								}
								else
								{
									out.print("<td></td><td></td>");
								}
								%>
								 --%>
							<td><emp:message key="txgl_wgqdpz_spzhczhs_yylx" defVal="应用类型：" fileName="txgl"></emp:message></td>
							<td>
							<%
								String sptype = ufvorq.getSptype();
							%>
							<select name="sptype" id="sptype" class="sptype" isInput="false">
										<option value="">
											<emp:message key="txgl_wgqdpz_spzhczhs_qb" defVal="全部" fileName="txgl"></emp:message>
										</option>
										<option value="1" <%="1".equals(sptype) ? "selected" : ""%>>
											<emp:message key="txgl_wgqdpz_spzhczhs_empyyzh" defVal="EMP应用账户" fileName="txgl"></emp:message>
										</option>
										<%
											int corptype = StaticValue.getCORPTYPE();
											int spytpeflag = StaticValue.getSPTYPEFLAG();
												if (corptype - 0 == 0 || spytpeflag-0==0) {
										%>
										<option value="2" <%="2".equals(sptype) ? "selected" : ""%>>
											<emp:message key="txgl_wgqdpz_spzhczhs_empjrzh" defVal="EMP接入账户" fileName="txgl"></emp:message>
										</option>
										<option value="3" <%="3".equals(sptype) ? "selected" : ""%>>
											<emp:message key="txgl_wgqdpz_spzhczhs_zlzh" defVal="直连账户" fileName="txgl"></emp:message>
										</option>
								<%
									}
								%> 
									</select>
									</td>
							<td class="tdSer"><center><a id="search"></a></center></td>
						</tr>
						</table>
					</div>
					<table id="content" class="content_table">
						<thead>
							<tr >
								<th>
									<input type="checkbox" name="checkall" id="checkall" onclick="checkAlls(this,'checklist')" />
								</th>
								<th>
									<emp:message key="txgl_mwgateway_text_1" defVal="SP账号" fileName="txgl"></emp:message>
								</th>
								<th>
									<emp:message key="txgl_wgqdpz_spzhczhs_zhmc" defVal="账户名称" fileName="txgl"></emp:message>
								</th>
								<th>
									<%=yylx %>
								</th>								
								<th>
									<emp:message key="txgl_wgqdpz_spzhczhs_xxlx" defVal="信息类型" fileName="txgl"></emp:message>
								</th>
								<th>
									<emp:message key="txgl_wgqdpz_spzhczhs_yet" defVal="余额（条）" fileName="txgl"></emp:message>
								</th>
								<th>
									<emp:message key="txgl_wgqdpz_spzhczhs_gjfz" defVal="告警阀值" fileName="txgl"></emp:message>
								</th>
								<th>
									<emp:message key="txgl_wgqdpz_spzhczhs_tzr" defVal="通知人" fileName="txgl"></emp:message>
								</th>
								<%
									if (btnMap.get(menuCode + "-1") != null
												|| btnMap.get(menuCode + "-2") != null
												|| btnMap.get(menuCode + "-3") != null) {
								%>
								<th>
									<emp:message key="txgl_wgqdpz_yyshdgl_cz" defVal="操作" fileName="txgl"></emp:message>
								</th>
								<%
									}
								%>
							</tr>
						</thead>
						<tbody>
						<%
							if (ufdynlist != null && ufdynlist.size() > 0) {
									for (int i = 0; i < ufdynlist.size(); i++) {
										DynaBean user = ufdynlist.get(i);
										if(user!=null){
										String dynuseridstr=user.get("userid")!=null?user.get("userid").toString():"";
										String dynstaffname=user.get("staffname")!=null?user.get("staffname").toString():"";
										String dynthreshold=user.get("threshold")!=null?user.get("threshold").toString():"";
						%>
							<tr>
								<td>
									<input type="checkbox" name="checklist" value="<%=encryptmap.get(dynuseridstr) %>"/>
								</td>
								<td><xmp><%=dynuseridstr %></xmp></td>
								<td><xmp><%=dynstaffname %></xmp></td>
								<td class="ztalign">
									<%
										String sptypestr=user.get("sptype")!=null?user.get("sptype").toString():"";
										if ("1".equals(sptypestr)) {
									%>
										<emp:message key="txgl_wgqdpz_spzhczhs_empyyzh" defVal="EMP应用账户" fileName="txgl"></emp:message>
									<%
										} else if ("2".equals(sptypestr)) {
									%>
										<emp:message key="txgl_wgqdpz_spzhczhs_empjrzh" defVal="EMP接入账户" fileName="txgl"></emp:message>
									<%
										} else if ("3".equals(sptypestr)) {
										out.print(MessageUtils.extractMessage("txgl", "txgl_wgqdpz_spzhczhs_zlzh", request));
									}
									%>
								</td>
								<td class="ztalign">
									<%
										String accounttypestr=user.get("accounttype")!=null?user.get("accounttype").toString():"";
										if ("1".equals(accounttypestr)) {
									%>
										<font color="#159800"><emp:message key="txgl_wgqdpz_dcspzh_dxspzh" defVal="短信SP账户" fileName="txgl"></emp:message></font>
									<%
										} else {
									%>
										<font color="#f1913c"><emp:message key="txgl_wgqdpz_dcspzh_cxspzh" defVal="彩信SP账户" fileName="txgl"></emp:message></font>
									<%
										}
									%>
								</td>
								<td>
								<%=user.get("sendnum")!=null?user.get("sendnum").toString():"" %>
								</td>
								<td>
								<%=dynthreshold %>
								</td>
								<td>
								<%
									if(userAlarmMap!=null){
										List<LfSpFeeAlarmVo> spfvos=userAlarmMap.get(user.get("userid")!=null?user.get("userid").toString():"");
										if(spfvos!=null){
											String threerow="";
											String allrow="";
											int row=0;
											for(LfSpFeeAlarmVo spvo:spfvos){
												if(spvo!=null){
													row++;
													if(row<2){
														threerow=threerow+"&nbsp;&nbsp;&nbsp;&nbsp;"+spvo.getNoticename()+"("+spvo.getAlarmphone()+")</br>";
													}
													allrow=allrow+"&nbsp;&nbsp;&nbsp;&nbsp;"+spvo.getNoticename()+"("+spvo.getAlarmphone()+")</br>";
												}
											}
											if(!"".equals(threerow)&&threerow!=null&&!"".equals(allrow)&&allrow!=null){
												%>
													<%=threerow%>
												<%
												if(allrow.length()>threerow.length()){
													%>
													<a class="allrow_a" onclick=javascript:modify(this)>
													<label class="allrow_label"><xmp><%=allrow %></xmp></label>
													<xmp>------</xmp> 
													</a> 
													<%
												}	
											}
										}else{
											out.print("-");
										}
									}else{
										out.print("-");
									}
								 %>
								</td>
								
								<%
									if (btnMap.get(menuCode + "-1") != null) {
								%>
								<td>
									<a href="javascript:addBalance('<%=dynuseridstr %>','<%=encryptmap.get(dynuseridstr) %>','<%=dynstaffname %>')"><emp:message key="txgl_wgqdpz_spzhczhs_cz" defVal="充值" fileName="txgl"></emp:message></a>&nbsp;
								<%
									}
									if (btnMap.get(menuCode + "-2") != null) {
								%>
									<a href="javascript:delBalance('<%=dynuseridstr %>','<%=encryptmap.get(dynuseridstr) %>','<%=dynstaffname %>')"><emp:message key="txgl_wgqdpz_spzhczhs_hs" defVal="回收" fileName="txgl"></emp:message></a>&nbsp;
								<%
 									}
 									if (btnMap.get(menuCode + "-3") != null) {
								%>
									<a href="javascript:setAlarm('<%=dynuseridstr %>','<%=encryptmap.get(dynuseridstr) %>','<%=dynstaffname %>','<%=dynthreshold %>')"><emp:message key="txgl_wgqdpz_spzhczhs_fz" defVal="阀值" fileName="txgl"></emp:message></a>&nbsp;
								<%
 									}
 								%>
									
								</td>
							</tr>
						<%
								}
							}
						} else {
						%>
						<tr>
							<Td colspan="9">
								<emp:message key="txgl_wgqdpz_qyhdgl_wjl" defVal="无记录" fileName="txgl"></emp:message>
							</Td>
						</tr><%
						}
						%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="9">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
			</div>
			<%
				}
			%>
			<%-- 内容结束 --%>
			<%-- foot开始 --%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
			<%-- foot结束 --%>
			</form>
		</div>
		<div id="modify" title="<%=tzrxq %>"  class="modify">
				<table class="modify_table">
					<thead>
						<tr class="msg_tr">
							<td>
								<label id="msg" class="msg"></label>
							</td>
						</tr>
					   <tr class="msg_down_tr">
							<td></td>
					   </tr>
					</thead>
				</table>
			</div>
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
        <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.placeholder.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=iPath %>/js/spb_spBalanceMgr.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/txgl_<%=empLangName%>.js"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			getLoginInfo("#corpCode");
			var findresult="<%=(String) request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
		      // alert("加载页面失败,请检查网络是否正常!");	
		       alert(getJsLocaleMessage("txgl","txgl_wgqdpz_yyshdgl_text_1"));
		       return;			       
		    }
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
			$('#search').click(function(){submitForm();});
		    $('#sptype').isSearchSelect({'width':'156','isInput':false,'zindex':0});
		    
		    $('#condition input[placeholder]').each(function(){
				$(this).placeholder({'labelMode':true});	
			})
		    
		    
			$("#addBalance").dialog({
				autoOpen: false,
				width: 350,
				height: 200,
				modal: true,
				open:function(){
					$("#addSms").attr("checked","checked");
				},
				close:function(){
				    $('#addCount').val("");
				   	$('#addMark').val("");
				    $(".ui-dialog-buttonpane button").attr("disabled",false);
				}
			});
			$("#delBalance").dialog({
				autoOpen: false,
				width: 350,
				height: 200,
				modal: true,
				open:function(){
					$("#addMms").attr("checked","checked");
				},
				close:function(){
				    $('#recCount').val("");
				    $('#recMark').val("");
				    $(".ui-dialog-buttonpane button").attr("disabled",false);
				}
			});
			$("#setAlarm").dialog({
				autoOpen: false,
				width: <%="zh_HK".equals(empLangName)?540:500%>,
				height: <%="zh_HK".equals(empLangName)?320:300%>,
				modal: true,
				open:function(){
				}
			});
			
			
			$("#addAllSpuserDiv").dialog({
				autoOpen: false,
                width: <%="zh_HK".equals(empLangName)?550:500%>,
                height: <%="zh_HK".equals(empLangName)?580:300%>,
				modal: true,
				open:function(){
				}
			});
			
			$("#recCount,#addCount").bind('keyup blur',function(){
				var reg=/[^0-9]/g;
				var val=$(this).val();
				if(reg.test(val)){
					$(this).val($(this).val().replace(reg,''));
				}
		 	});

		$("#recMark,#addMark").live('keyup blur',function(){
			var value=$(this).val();
			if(value!=filterString(value)){
				$(this).val(filterString(value));
			}
		});
			
		    
			});
			
			function setAlarm(useridstr,jmspuser){
	   			$("#editAlarmFrame").attr("src","<%=path %>/spb_spBalanceMgr.htm?method=toSetAlarm&useridstr="+jmspuser+"&lgcorpcode="+$("#lgcorpcode").val()+"&lguserid="+$("#lguserid").val());
	   			$("#setAlarm").dialog("open");
			}

			function addBalanceAll(){
				var items = "";
				$('input[name="checklist"]:checked').each(function(){	
					items += $(this).val()+",";
				});
				if (items != "")
				{
					items = items.toString().substring(0, items.lastIndexOf(','));
				}
				if(items==null || items.length == 0){
					//alert("请选择您要批量充值/回收的SP账号！");
					alert(getJsLocaleMessage("txgl","txgl_wgqdpz_spzhczhs_text_1"));
					return;
				}else{
					$("#addAllSpuserFrame").attr("src","<%=path %>/spb_spBalanceMgr.htm?method=toAddBalanceAll&useridstrs="+items+"&lgcorpcode="+$("#lgcorpcode").val()+"&lguserid="+$("#lguserid").val());
					$("#addAllSpuserDiv").dialog("open");
				}
			}
			
			function modify(t)
			{
			$('#modify').dialog({
				autoOpen: false,
				width:300,
			    height:200
			});
			$("#msg").empty();
			$("#msg").html($(t).children("label").children("xmp").html());
			$('#modify').dialog('open');
			}
			
			function checkAlls(e,str)    
			{  
				var a = document.getElementsByName(str);    
				var n = a.length;    
				for (var i=0; i<n; i=i+1)    
					a[i].checked =e.checked;    
			}
			function toLog(){
				location.href="sp_balanceLog.htm?method=find&lgguid="+$("#lgguid").val();
			}
			
			function doCancel(obj){
			$(obj).dialog('close');
		}
		</script>
	</body>
</html>
