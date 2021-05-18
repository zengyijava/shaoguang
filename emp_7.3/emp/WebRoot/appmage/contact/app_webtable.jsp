<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ page import="com.montnets.emp.util.PageInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.beanutils.DynaBean"%>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.entity.appmage.LfAppOlGroup"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@include file="/common/common.jsp" %>
<%
	String path=request.getContextPath();
	String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String rTitle = (String)request.getAttribute("rTitle");
	LinkedHashMap<String,String> conditionMap = (LinkedHashMap)request.getAttribute("conditionMap");
	
	Object selectAll=request.getAttribute("selectAll");
	String menuCode = titleMap.get(rTitle);
	@ SuppressWarnings("unchecked")
	List<DynaBean> trustDatas = (List<DynaBean>)request.getAttribute("clientList");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
	(String)session.getAttribute("stlyeSkin");
	String showSex="";
	if(conditionMap.get("sex")!=null&&!"".equals(conditionMap.get("sex"))){
		showSex=conditionMap.get("sex")+"";
	}
	//处理是否被选中的(防止刷新后，节点上的值没有了)
	String groupid="";
	if(conditionMap.get("groupid")!=null&&!"".equals(conditionMap.get("groupid"))){
		groupid=conditionMap.get("groupid")+"";
	}
	
		
%>
	<table id="content" style="margin:8px 0px;">
				<thead>
				  <tr>
						<th><input type='checkbox' name='checkall' id='checkall' class="select_check" onclick='checkAlls(this)'/> </th>
				  		<th><emp:message key="appmage_jcsj_yhgl_text_headimage" defVal="头像" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_useraccount" defVal="用户账户" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_nichen" defVal="昵称" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_phone" defVal="手机号码" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_sex" defVal="性别" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_age" defVal="年龄" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_ssqz" defVal="所属群组" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_jcsj_yhgl_text_regtime" defVal="注册时间" fileName="appmage"></emp:message></th>
						<th><emp:message key="appmage_common_opt_caozuo" defVal="操作" fileName="appmage"></emp:message><th>
				  </tr>
				</thead>
					<tbody>
					<%
					DynaBean data = null;
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(trustDatas != null && trustDatas.size()>0){
						for(int v=0;v<trustDatas.size();v++){
							 data=trustDatas.get(v);
							String groupSelect= data.get("wc_id")+";"+data.get("gid");
									%>
								<tr>
								<td>
								<input type='checkbox' name='checklist' id='checklist' class="select_check" value="<%=groupSelect%>" onclick="checkCh(this,'<%=groupSelect%>')"/>
								 <input type="hidden" id="checkBoxsel" name="checkBoxsel" value=""><%-- 记录人员id值 --%>
								 <input type="hidden" id="checkGroup" name="checkGroup" value=""><%-- 记录群组ID值 --%>
								</td>
								<td>
									 <%
	                                if(data.get("local_img_url") != null && !"".equals(data.get("local_img_url")))
	                                        {
	                            %>
	                                <img src='<%=inheritPath %>/account/img/default_acct.png'  title="<emp:message key="appmage_jcsj_yhgl_text_headimage" defVal="头像" fileName="appmage"></emp:message>" class="avatar" onerror="this.src='<%=inheritPath %>/account/img/default_acct.png'"/>
	                            <%
	                                }
	                                else
	                                {
	                            %>
	                                <img src='<%=inheritPath + "/account/img/default_acct.png" %>' title="<emp:message key="appmage_jcsj_yhgl_text_headimage" defVal="头像" fileName="appmage"></emp:message>" class="avatar">
	                            <%
	                                }
	                            %>
								</td>
								<td><%=data.get("app_code")==null?"":data.get("app_code") %></td>
								<td><%=data.get("nick_name")==null?"":data.get("nick_name") %></td>
								<td><%=data.get("phone")==null?"":data.get("phone") %></td>
								<td><%
								String sex="";
								if(data.get("sex")!=null){
								 String s=data.get("sex")+"";
								 if("1".equals(s)){
								 sex= MessageUtils.extractMessage("appmage","appmage_jcsj_yhgl_text_man",request);  //"男";
								 }else if("2".equals(s)){
								 sex= MessageUtils.extractMessage("appmage","appmage_jcsj_yhgl_text_women",request);  //"女";
								 }else if("0".equals(s)){
								 sex= MessageUtils.extractMessage("appmage","appmage_xxfb_appfsjl_text_unknow",request);  //"未知";
								 }
								}
								out.print(sex);
								%></td>
								<td><%=data.get("age")==null?"":data.get("age") %></td>
								<td><%=data.get("mwgroupname")==null?"":data.get("mwgroupname") %></td>
								
																	<td>
									<%if(data.get("verifytime")==null||"".equals(data.get("verifytime"))){ %>
									- <%	}else{ %>
									 <%=df.format(data.get("verifytime"))%><%
									} %></td>
								
								<td>
								<% if(data.get("mwgroupname")==null||"".equals(data.get("mwgroupname"))) {%>
								--
								<%}else {%>
								<a id="delLink" onclick="javascript:delLink('<%=data.get("wc_id")==null?"":data.get("wc_id") %>','<%=data.get("gid")==null?"":data.get("gid") %>');"><emp:message key="appmage_jcsj_yhgl_text_preexitgroup" defVal="退群" fileName="appmage"></emp:message></a>
								<%}%>
								</td>
								</tr>
						<%} 
						}else {%>
						
						<tr><td colspan="10"><emp:message key="appmage_common_text_nodata" defVal="无记录" fileName="appmage"></emp:message></td></tr>
					<%} %>
				</tbody>
				<tfoot>
						<tr>
						<td colspan="10">
							<div id="pageInfo"></div>
						</td>
					</tr>
				</tfoot>
		</table>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/appmage_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath %>/appmage/contact/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script>

		function exportExcel(){
			if (confirm(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportexcel'))) {
				var nickname = $("#nickname").val();
				var sex = $("#sex").val();
				var app_code = $("#app_code").val();
				var phone = $("#phone").val();
				var createtime = $("#createtime").val();
				var endtime = $("#endtime").val();
				var age = $("#age").val();
				groupid = $("#groupid").val();
				var lguserid = '<%=request.getParameter("lguserid")%>';
				<%if (trustDatas!=null&&trustDatas.size()>0||trustDatas!=null&&trustDatas.size()>0)  {
				   if(pageInfo.getTotalRec()<=500000){
				   %>
	   				 $.ajax({
						type:"POST",
						url: "<%=path%>/app_climanager.htm",
						data: {method: "exportExcel",
							nickname:nickname,
							sex:sex,
							app_code:app_code,
							phone:phone,
							createtime:createtime,
							endtime:endtime,
							lgcorpcode:lgcorpcode,
							lguserid:lguserid,
							age:age,
							groupid:groupid
		   					},
		   				beforeSend: function(){
		   					page_loading();
						},
						complete:function(){
							page_complete();
						},
						success: function(result){
							if(result)
							{
								window.parent.complete();
								download_href("<%=path%>/app_climanager.htm?method=downloadFile");
							}else
							{
								//alert("导出失败！");
								alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_exportfalse'));
							}
						}
					});
				   
				<%}else{%>
				//alert("数据量超过导出的范围50万，请从数据库中导出！");
				alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_datatoobig'));
				<%}
		   } else {%>
				//alert("无数据可导出！");
				alert(getJsLocaleMessage('appmage','appmage_page_mttaskselect_nodatatoexport'));
				<%}
				   %>
			}
		}
		initPage(<%=pageInfo.getTotalPage()%>,<%=pageInfo.getPageIndex()%>,<%=pageInfo.getPageSize()%>,<%=pageInfo.getTotalRec()%>);
		</script>