<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiUserInfo"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.ottbase.util.StringUtils"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiGroup"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    @SuppressWarnings("unchecked")
    Map<String, String> btnMap = (Map<String, String>)session.getAttribute("btnMap");//按钮权限Map
    @SuppressWarnings("unchecked")
    Map<String, String> titleMap = (Map<String, String>)session.getAttribute("titleMap");
    String menuCode = titleMap.get(request.getAttribute("rTitle"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String)session.getAttribute("stlyeSkin");
    String weixBasePath = request.getScheme() + "://" + request.getServerName() + path + "/";
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //使用集群，文件服务器的地址
    String filePath = GlobalMethods.getWeixFilePath();
    //MP企业微信服务协议
    String approve = (String)session.getAttribute("approve");
    
	//用户信息
    LfWeiUserInfo otWeiUserInfo=(LfWeiUserInfo)request.getAttribute("userinfo");
	List<LfWeiGroup> otWeiGroupList=(List<LfWeiGroup>)request.getAttribute("otWeiGroupList");
	//用来表示用户是查看详情，还是修改信息(1：查看详情，2:修改)
	String type = String.valueOf(request.getAttribute("type"));
	if(null == type || "".equals(type) || "null".equals(type))
	{
	    type="";
	}
	  String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=commonPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=commonPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>">
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<style>
		.img-circle {
    		border-radius: 500px 500px 500px 500px;
			}
		.headimg5 {
		    border: 1px solid #CCCCCC;
		    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
		    height: 38px;
		    width: 38px;
		    vertical-align:middle;
		    margin:5px 0;
		    _margin:4px 0 5px;
		}
		.visible_hidden{visibility:hidden;}
		.blueColor{color:#0e5ad1;}
		</style>
		
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/artDialog/artDialog.js?skin=default"></script>
	<script type="text/javascript" src="<%=path %>/common/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=iPath%>/js/yhgl_contactManager.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.InputLetter.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
		<div id="container" class="container">
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="detail_Info" style="padding:10px;"> 
			<form name="userDescForm" action="" method="post" id="userDescForm">
				<div style="display:none" id="hiddenValueDiv">
				</div>
				<div style="display: block;">
					
					<table id="editUserTable" width="100%" height="100%" style="font-size: 12px; line-height: 30px;">
					<tbody>
                              <tr>
                                  <td style="text-align:right;">
                                      <span><emp:message key="wxgl_yhgl_title_45" defVal="头像：" fileName="wxgl"/></span>
                                  </td>
                                  <td>
                                      <div>
                                          <label>
			                             <%if(otWeiUserInfo.getLocalImgUrl()!=null&&!"".equals(otWeiUserInfo.getLocalImgUrl())) {%>
				                        	<img class="img-circle headimg5" alt="img" src='<%= otWeiUserInfo.getLocalImgUrl()%>' title="<emp:message key="wxgl_yhgl_title_40" defVal="头像" fileName="wxgl"/>" onerror="this.src='<%= otWeiUserInfo.getHeadImgUrl() %>'">
				                        <% } else { %>
				                       	    <img class="img-circle headimg5" alt="img" src="<%= otWeiUserInfo.getHeadImgUrl() %>" title="<emp:message key="wxgl_yhgl_title_40" defVal="头像" fileName="wxgl"/>">
				                        <% } %>
                                          </label>
                                      </div>
                                  </td>
							<td style="width:120px;text-align:right;">
								<%-- <span>微信号：</span> --%>
							</td>
							<td><%--
							<div style="float: left;">
								<label><%=otWeiUserInfo.getFakeId()==null?"-": otWeiUserInfo.getFakeId()%></label>
							</div>--%>
							</td>
						</tr>
						<tr>
                                  <td style="text-align:right;">
									<span><emp:message key="wxgl_yhgl_title_46" defVal="姓名：" fileName="wxgl"/></span>
									</td><td> 
									<label><%=otWeiUserInfo.getUname()==null?"-": otWeiUserInfo.getUname()%></label>
                                  </td>
                                  <td style="text-align: right">
                                      <span><emp:message key="wxgl_yhgl_title_47" defVal="昵称：" fileName="wxgl"/></span>
                                  </td><td>
									<label><%=otWeiUserInfo.getNickName()==null?"-":otWeiUserInfo.getNickName() %></label>
                                  </td>
                              </tr>
                             <tr>
                                  
                                  <td style="text-align:right;">
									<span><emp:message key="wxgl_yhgl_title_48" defVal="关注时间：" fileName="wxgl"/></span>
									</td><td>
									<%
										Timestamp subscribeTime = otWeiUserInfo.getSubscribeTime();
										if(null == subscribeTime)
										{
										    out.print("-");
										}else{
									%>
									<label><%=formatter.format(subscribeTime) %></label>
									<%}%>
                                  </td>
                                  <td style="text-align: right">
                                  	<span> <emp:message key="wxgl_yhgl_title_35" defVal="性别：" fileName="wxgl"/></span>
                                  	</td><td>
                                      	<%
                                      		String sex = otWeiUserInfo.getSex();
                                      		if(null==sex || "".equals(sex))
                                      		{
                                      		    sex = "0";
                                      		}else{
                                          		if(sex.equals("0"))
                                          		{ 
                                          		    sex=MessageUtils.extractMessage("wxgl","wxgl_yhgl_title_28",request);
                                          		}
                                          		else if(sex.equals("1"))
                                          		{
                                          		    sex = MessageUtils.extractMessage("wxgl","wxgl_yhgl_title_26",request);
                                          		}
                                          		else if(sex.equals("2"))
                                          		{
                                          		    sex = MessageUtils.extractMessage("wxgl","wxgl_yhgl_title_27",request);
                                          		}
                                      		}
                                      	%>
									<label><%=sex %></label>
                                  </td>
                              </tr>
                             <tr>
                                  <td style="text-align:right;">
                                      <span><emp:message key="wxgl_yhgl_title_50" defVal="所在国家：" fileName="wxgl"/></span>
                                  </td>
                                  <td>
									<label><%=otWeiUserInfo.getCountry()==null||otWeiUserInfo.getCountry().equals("")?"-": otWeiUserInfo.getCountry() %></label>
                                  </td>
                                  <td style="text-align:right;">
                                      <span><emp:message key="wxgl_yhgl_title_51" defVal="所在省份：" fileName="wxgl"/></span>
                                  </td>
                                  <td>
									<label><%=otWeiUserInfo.getProvince()==null||otWeiUserInfo.getProvince().equals("")?"-": otWeiUserInfo.getProvince() %></label>
                                  </td>
                              </tr>
                             <tr>
                                  <td style="text-align:right;">
                                      <span><emp:message key="wxgl_yhgl_title_52" defVal="所在城市：" fileName="wxgl"/></span>
                                  </td>
                                  <td>
									<label><%=otWeiUserInfo.getCity()==null||otWeiUserInfo.getCity().equals("")?"-": otWeiUserInfo.getCity() %></label>
                                  </td>
                                  <td style="text-align:right;">
                                      <span><emp:message key="wxgl_yhgl_title_53" defVal="创建时间：" fileName="wxgl"/></span>
                                  </td>
                                  <td>
									<label><%=formatter.format(otWeiUserInfo.getCreatetime()) %></label>
                                  </td>
                              </tr>
                             <tr>
                                  <td style="text-align:right;">
                                      <span><emp:message key="wxgl_yhgl_title_54" defVal="所属群组：" fileName="wxgl"/></span>
                                  </td>
                                  <td colspan="3">
                                      	<%
		                           		String usergid = String.valueOf(otWeiUserInfo.getGId());
		                           		String groupname = "";
										if(null == usergid || "".equals(usergid) || "null".equals(usergid)){
											out.print("-");
											}
										else{ 
											if(otWeiGroupList != null && otWeiGroupList.size()>0){
					                              for(LfWeiGroup group : otWeiGroupList){
				                                  	  String gId  = String.valueOf(group.getGId());
				                                  	  if(usergid.equals("0")){
					                                      groupname = MessageUtils.extractMessage("wxgl","wxgl_yhgl_title_41",request);
					                                  }  
				                                  	  else if(usergid.equals(gId)){
					                                      groupname = group.getName();
					                                  }  
					                              }
											}     
										%>
										<label><%=groupname %></label>
										<%}%>
                                      <%if("2".equals(type)){ %>    
                                      	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;             	
										<label>
											<select id="gid" name="gid" class="input_bd">
												<%
				                           		String ugid = String.valueOf(otWeiUserInfo.getGId());
												if(otWeiGroupList != null && otWeiGroupList.size()>0){
						                              for(LfWeiGroup group : otWeiGroupList){
					                                String gId  = String.valueOf(group.getGId());
												%>
												<option value="<%=group.getGId() %>" <%=(ugid.equals(gId))?"selected":"" %>> <%=group.getName()%></option>
												<%
													}
												}
												%>
											</select>
											<font color="red">&nbsp;&nbsp;&nbsp;<emp:message key="wxgl_yhgl_title_55" defVal="*请选择需要修改的群组" fileName="wxgl"/></font>
										</label>
									<%} %>
                                  </td>
                              </tr>
                              <tr>
                                  <td style="width:120px;text-align:right;vertical-align: top">
                                      <span><emp:message key="wxgl_yhgl_title_56" defVal="备注：" fileName="wxgl"/></span>
                                  </td>
							 <td colspan="3">
							<%if("2".equals(type)){ %>
							<div style="float:left;width:510px;height: 126px;overflow:hidden;position:relative;">
								<label id="showEditor">
									<textarea id="userInfo" name="userInfo" style="width:500px;height:85px;" class="input_bd"><%=otWeiUserInfo.getDescr()==null?"":otWeiUserInfo.getDescr()%></textarea>
									<b style="bottom:-15px;left:0;color:#656565;display: block;"><span id="sid"></span>/215</b>
								</label>
							</div>
							<%}else if("1".equals(type)){ %>
								<%=otWeiUserInfo.getDescr()==null?"":otWeiUserInfo.getDescr()%>
							<%} %>
							</td>
                              </tr>
						</tbody>
					</table>
				</div>
            </form>
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
			<div id="pageStuff" style="display:none;">
				<input type="hidden" id="pathUrl" value="<%=path%>" />
				<input type="hidden" id="iPathUrl" value="<%=iPath%>"/>
			</div>
				<input type="hidden" id="lgcorpcode" value="<%=lgcorpcode%>" />
				<input type="hidden" id="lguserid" value="<%=lguserid%>" />
				<input type="hidden" id="wcid" value="<%=otWeiUserInfo.getWcId()%>" />
		</div>
		
    <div class="clear"></div>
    <%if("2".equals(type)){ %>
	<script type="text/javascript">
	 var pathUrl="<%=path%>",
     iPathUrl="<%=iPath%>",
     lguserid=<%=lguserid%>,
     lgcorpcode="<%=lgcorpcode%>";
		    $(function(){
		  	  getLoginInfo("#hiddenValueDiv");
		  	  //类型默认选中公众帐号类型
		  	  $("#userInfo").manhuaInputLetter({					       
		  			len : 215,//限制输入的字符个数				       
		  			showId : "sid",//显示剩余字符文本标签的ID
		  			showNum: $("#userInfo").val().length
		  	  });
		    })
    </script>
    <%} %>
	</body>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
</html>