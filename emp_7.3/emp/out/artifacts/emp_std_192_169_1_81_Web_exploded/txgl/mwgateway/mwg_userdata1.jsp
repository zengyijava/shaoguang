<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Iterator"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.Userdata"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.XtGateQueue"%>
<%@page import="com.montnets.emp.servmodule.txgl.entity.LfPageField"%>
<%@ page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="com.montnets.emp.servmodule.txgl.entity.AgwAccount"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = new PageInfo();
	pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, String> spBindMap = (LinkedHashMap<String, String>)request.getAttribute("spBindMap");
	@ SuppressWarnings("unchecked")
	LinkedHashMap<String, AgwAccount> accountMap = (LinkedHashMap<String, AgwAccount>)request.getAttribute("accountMap");
	
	//Jsp页面中获取session中的语言设置
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	


	@ SuppressWarnings("unchecked")
	List<DynaBean> userList = (List<DynaBean>) request.getAttribute("userList");
	@ SuppressWarnings("unchecked")
	Map<String, String> keyIdMap = (Map<String, String>)request.getAttribute("keyIdMap");
	@ SuppressWarnings("unchecked")
	List<Userdata> userListCon = (List<Userdata>) request.getAttribute("userListCon");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	@SuppressWarnings("unchecked")
	List<LfPageField> pagefileds=(List<LfPageField>)session.getAttribute("mwg_pagefileds");
	@SuppressWarnings("unchecked")
	Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
	String menuCode = titleMap.get("userdata");
	menuCode = menuCode==null?"0-0-0":menuCode;
	@ SuppressWarnings("unchecked")
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	String gt_userid = request.getParameter("gt_userid");
	String staffName = request.getParameter("staffName");
	String spaccid = request.getParameter("spaccid");
	String gt_sptype = request.getParameter("sptype");
	String gt_keepconn = request.getParameter("keepconn");
	String accouttype = request.getParameter("accouttype");
	boolean isBack = request.getParameter("isback") != null;//是否返回操作
	if(isBack){
		gt_userid = (String)request.getAttribute("gt_userid");
		staffName = (String)request.getAttribute("staffName");
		spaccid = (String)request.getAttribute("spaccid");
		gt_sptype = (String)request.getAttribute("sptype");
		gt_keepconn = (String)request.getAttribute("keepconn");
		accouttype = (String)request.getAttribute("accouttype");
	}
	String txglFrame = skin.replace(commonPath, inheritPath);
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head><%@include file="/common/common.jsp" %>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/empSelect.css?V=<%=StaticValue.getJspImpVersion()%>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion()%>" type="text/css" >


		<%if(StaticValue.ZH_HK.equals(empLangName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/mwg_userdata1.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/txgl.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="mwg_userdata1">
		<div id="container" class="container">
			<%-- 当前位置 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>
			<%-- 内容开始 --%>
			<form name="pageForm" action="mwg_userdata.htm" method="post"
				id="pageForm">
			<%
			if(btnMap.get(menuCode+"-0")!=null) {
			%>
				<div id="rContent" class="rContent">
					<div class="buttons rContent_down_div"  >
						<div id="toggleDiv"></div>
					</div>
					<div id="condition">
						<table>
							<tr>
								<td>
									<emp:message key='txgl_mwgateway_text_66' defVal='通道账号：' fileName='mwadmin'/>
								</td>
								<td>
									<select name="gt_userid" id="gt_userid">
										<option value="">
											<emp:message key='txgl_mwgateway_text_67' defVal='全部' fileName='mwadmin'/>
										</option>
										<%
										if (userListCon != null && userListCon.size() > 0)
										{
											for(Userdata userdata : userListCon){
											 %>
										<option value="<%=userdata.getUserId() %>"
											<%=gt_userid!=null&&gt_userid.equals(userdata.getUserId())?"selected":"" %>><%=userdata.getUserId() %></option>
										<% }} %>
									</select>

									<input type="hidden" id="gt_userid" name="gt_userid"
										value="<%=gt_userid!=null?gt_userid:"" %>" />
								</td>
								<td>
									<emp:message key='txgl_mwgateway_text_68' defVal='通道账户名称：' fileName='mwadmin'/>
								</td>
								<td>
									<input type="text" class="staffName"
										value="<%=staffName!=null?staffName:"" %>" id="staffName"
										name="staffName" />
								</td>
								<td>
									<emp:message key='txgl_mwgateway_text_69' defVal='运营商账户ID：' fileName='mwadmin'/>
								</td>
								<td>
									<input type="text" class="spaccid" id="spaccid"
										name="spaccid" value="<%=spaccid!=null?spaccid:"" %>" />
								</td>

								<td class="tdSer">
									<center>
										<a id="search"></a>
									</center>
								</td>
							</tr>
							<tr>
								<td>
									<emp:message key='txgl_mwgateway_text_70' defVal='合作商：' fileName='mwadmin'/>
								</td>
								<td>
									<select name="sptype" id="sptype"  
										class="input_bd sptype" isInput="false">
										<option value="">
											<emp:message key='txgl_mwgateway_text_67' defVal='全部' fileName='mwadmin'/>
										</option>
										<option value="0" <%="0".equals(gt_sptype)?"selected":"" %>>
											<emp:message key='txgl_mwgateway_text_39' defVal='梦网科技' fileName='mwadmin'/>
										</option>
										<option value="10" <%="10".equals(gt_sptype)?"selected":"" %>>
											<emp:message key='txgl_mwgateway_text_40' defVal='其他' fileName='mwadmin'/>
										</option>
									</select>
								</td>
								<td>
									<emp:message key='txgl_mwgateway_text_71' defVal='连接方式：' fileName='mwadmin'/>
								</td>
								<td>
									<select name="keepconn" id="keepconn"  
										class="input_bd keepconn" isInput="false">
										<option value="">
											<emp:message key='txgl_mwgateway_text_67' defVal='全部' fileName='mwadmin'/>
										</option>
										<option value="1" <%="1".equals(gt_keepconn)?"selected":"" %>>
											<emp:message key='txgl_mwgateway_text_55' defVal='多链路多连接' fileName='mwadmin'/>
										</option>
										<option value="0" <%="0".equals(gt_keepconn)?"selected":"" %>>
											<emp:message key='txgl_mwgateway_text_56' defVal='单链路多连接' fileName='mwadmin'/>
										</option>
									</select>
								</td>
								<%
									String typename="";
									if(pagefileds!=null&&pagefileds.size()>0){
										LfPageField first=pagefileds.get(0);
										typename=first.getField()+"：";
									} 
									%>
								<td>
									<emp:message key='txgl_mwgateway_text_3' defVal='账户类型：' fileName='mwadmin'/>
								</td>
								<td>
									<select name="accouttype" id="accouttype" class="input_bd accouttype" isInput="false">
										<option value="">
											<emp:message key='txgl_mwgateway_text_67' defVal='全部' fileName='mwadmin'/>
										</option>
										<%
											if(pagefileds!=null&&pagefileds.size()>1){
												for(int i=1;i<pagefileds.size();i++){
												LfPageField pagefid=pagefileds.get(i);
												String accountType = pagefid.getSubFieldName();
												if("短信通道账户".equals(accountType)){
													accountType = "zh_HK".equals(empLangName)?"SMS channel account":"zh_TW".equals(empLangName)?"短信通道賬戶":"短信通道账户";
												}
												if("彩信通道账户".equals(accountType)){
													accountType = "zh_HK".equals(empLangName)?"MMS channel account":"zh_TW".equals(empLangName)?"彩信通道賬戶":"彩信通道账户";
												}
											%>
										<option value="<%=pagefid.getSubFieldValue() %>"
											<%=accouttype!=null&&accouttype.equals(pagefid.getSubFieldValue())?"selected":"" %>><%=accountType %></option>
										<% 
												}
											}
											
											%>
									</select>
								</td>
								<td></td>
							</tr>
						</table>
					</div>
					<table id="content" class="content_table">
						<thead>
							<tr>
							<th>
									<emp:message key='txgl_mwgateway_text_72' defVal='网关编号' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwgateway_text_73' defVal='通道账号' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwgateway_text_74' defVal='通道账户名称' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwgateway_text_75' defVal='运营商账户ID' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwgateway_text_76' defVal='账户类型' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwgateway_text_77' defVal='合作商' fileName='mwadmin'/>	
								</th>
								<th>
									<emp:message key='txgl_mwgateway_text_78' defVal='连接方式' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwgateway_text_79' defVal='连接数' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwgateway_text_80' defVal='创建时间' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwgateway_text_81' defVal='修改时间' fileName='mwadmin'/>
								</th>
								<th>
									<emp:message key='txgl_mwgateway_text_82' defVal='操作' fileName='mwadmin'/>
								</th>
							</tr>
						</thead>
						<tbody>
							<% 
						if(userList != null && userList.size() > 0)
						{
							for(int i=0 ;i< userList.size();i++)
							{
								DynaBean user = userList.get(i);
								String uid = user.get("uid").toString();
								String keyId = keyIdMap.get(uid);
								String userid = user.get("userid").toString();
								String accounttype = user.get("accounttype").toString();
								boolean isWy=(userid.length()>=3 && "WY0".equals(userid.substring(0,3)));
								String gtName = user.get("staffname").toString();
                                AgwAccount agwAccount = accountMap.get(uid);
                                String keepconn = user.get("keepconn")!=null?user.get("keepconn").toString():"0";
                                String linkcnt = user.get("linkcnt")!=null?user.get("linkcnt").toString():"1";
                                String createtm = user.get("createtm")!=null?sf.format(Timestamp.valueOf(user.get("createtm").toString())):"--";
                                String updatetm = user.get("updatetm")!=null?sf.format(Timestamp.valueOf(user.get("updatetm").toString())):"--";
                                String gtNo;
								
                                if(user.get("gw_no") == null)
                                {
                                    gtNo = agwAccount == null?"":agwAccount.getGwNo()+"";
                                }else{
                                    gtNo = user.get("gw_no").toString();
                                }
								if(gtName!=null)
								{
									gtName = gtName.replaceAll("<","&lt").replaceAll(">","&gt").replaceAll("'","&acute;");
								}
						%>
							<tr>
								<td><%=gtNo%></td>
								<td><%=userid%></td>
								<td class="textalign">
									<xmp><%=user.get("staffname").toString()%></xmp>
								</td>
								<td>
									<%
									  if(agwAccount == null || "".equals(agwAccount.getSpAccid()))
									  {
									     out.print("<font color='red'>" + MessageUtils.extractMessage("mwadmin","txgl_mwgateway_text_83",request) + "</font>");
									  }
									  else{
									    out.print(agwAccount.getSpAccid());
									  }									
									%>
								</td>
								<td class="ztalign">
									<% if ("1".equals(accounttype)){ %>
									<font color='#159800'><emp:message key='txgl_mwgateway_text_84' defVal='短信通道账户' fileName='mwadmin'/></font>
									<% } else { %>
									<font color='#f1913c'><emp:message key='txgl_mwgateway_text_85' defVal='彩信通道账户' fileName='mwadmin'/></font>
									<% } %>
								</td>
								<td>
								<%
								if("0".equals(agwAccount.getSpType()+"")){
								%>
									<emp:message key='txgl_mwgateway_text_39' defVal='梦网科技' fileName='mwadmin'/>
								<%}else{ %>
									<emp:message key='txgl_mwgateway_text_40' defVal='其他' fileName='mwadmin'/>
								<%} %>
								</td>
								<td>
								<%
								if(isWy||!"1".equals(accounttype))
								{
								%>
								--
								<%
								}else{
									if("1".equals(keepconn)){
										%>
										<emp:message key='txgl_mwgateway_text_55' defVal='多链路多连接' fileName='mwadmin'/>
										<%
										}else{
										%>
										<emp:message key='txgl_mwgateway_text_56' defVal='单链路多连接' fileName='mwadmin'/>
										<%
										}
								} %>
								</td>
								<td>
								<%
								if(isWy||!"1".equals(accounttype))
								{
								%>
								--
								<%
								}else{
								out.print(linkcnt);
								} %>
								
								</td>
								<td>
								<%
								if(isWy||!"1".equals(accounttype))
								{
								%>
								--
								<%
								}else{
									out.print(updatetm);
								} %>
								</td>
								<td><%
								if(isWy||!"1".equals(accounttype))
								{
								%>
								--
								<%
								}else{
								out.print(createtm);
								} %>
								</td>
								<td>
									<%if(isWy||!"1".equals(accounttype))
									{
									%>
									<%
									}else
									{
										if(btnMap.get(menuCode+"-1")!=null) {
									%>
									<a
										href='<%=path %>/mwg_userdata.htm?method=toEdit&uid=<%=uid %>&accouTtype=<%=accounttype%>&keyId=<%=keyId%>'><emp:message key='txgl_mwgateway_text_86' defVal='编辑' fileName='mwadmin'/></a>
									<%
										}
									}
									%>
									<a href="javascript:toEditByWy('<%=uid %>','<%=accounttype %>','<%=keyId%>');"><emp:message key='txgl_mwgateway_text_87' defVal='查看' fileName='mwadmin'/></a>
								</td>
							</tr>
							<%
						 	}
						 }else
						 {
							 out.print("<tr><td colspan='12'>" + MessageUtils.extractMessage("mwadmin","txgl_apimanage_text_52",request) + "</td></tr>");
						 }
						%>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="12">
									<div id="pageInfo"></div>
								</td>
							</tr>
						</tfoot>
					</table>
				</div>
				<%} %>
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
		<div class="clear"></div>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/txgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/jquery.placeholder.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript"
			src="<%=iPath%>/js/mwg_userdata.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
		<script type="text/javascript">
		$(document).ready(function() {
			var findresult="<%=(String)request.getAttribute("findresult")%>";
		    if(findresult=="-1")
		    {
                //alert("加载页面失败,请检查网络是否正常!");
                alert(getJsLocaleMessage("txgl","txgl_wgqdpz_zhtdpz_text_7"));
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
			$('#tdhm_key').isSearchSelect({'width':'150','zindex':0},function(data){
				//keyup click触发事件
					if(data.value=='全部'){
						$("#gt_userid").val("");
					}else{
						$("#gt_userid").val(data.value);
					}
			},function(data){
				//初始化加载
				var val=$("#gt_userid").val();
				if(val){
					data.box.input.val(val);
				}
				
			});
			$('#accouttype,#keepconn,#sptype').isSearchSelect({'width':'150','isInput':false,'zindex':0},function(data){
			});
			$('#search').click(function(){submitForm();});

		});
		

		function toEditByWy(uid,accouTtype,keyId)
		{
			location.href='<%=path %>/mwg_userdata.htm?method=toEdit&wy=1&uid='+uid+'&accouTtype='+accouTtype+'&keyId='+keyId;
		}
		</script>
	</body>
</html>
