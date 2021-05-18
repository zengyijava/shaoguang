<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import= "org.apache.commons.beanutils.DynaBean"%>
<%@page import="com.montnets.emp.ottbase.util.GlobalMethods"%>
<%@page import="com.montnets.emp.ottbase.util.StringUtils"%>
<%@page import="com.montnets.emp.util.PageInfo"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiAccount"%>
<%@page import="com.montnets.emp.entity.wxgl.LfWeiGroup"%>
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
    String wxskin = skin.indexOf("/frame") == -1 ? "default" : skin.substring(skin.indexOf("/frame")+1, skin.length());
    String lguserid = (String) request.getParameter("lguserid");
    String lgcorpcode = (String) request.getParameter("lgcorpcode");
    @SuppressWarnings("unchecked")
    List<DynaBean> userbeans = (List<DynaBean>)request.getAttribute("userbeans");
	List<LfWeiAccount> otWeiAccList=(List<LfWeiAccount>)request.getAttribute("otWeiAccList");
	List<LfWeiGroup> otWeiGroupList=(List<LfWeiGroup>)request.getAttribute("otWeiGroupList");
    PageInfo pageInfo = new PageInfo();
    pageInfo = (PageInfo)request.getAttribute("pageInfo");
    String aid= (String)request.getParameter("a_id");
    if(null == aid || "".equals(aid))
    {
    	aid="-1";
    }
    String gid= (String)request.getParameter("gid");
    if(null == gid || "".equals(gid))
    {
    	gid="-1";
    }
    //为了页面保存用户查询条件用
    String nickname = String.valueOf(request.getParameter("nickname"));
    if(null == nickname || "".equals(nickname) || "null".equals(nickname))
    {
        nickname = "";
    }
    
    String sex = String.valueOf(request.getParameter("sex"));
    if(null == sex || "".equals(sex) || "null".equals(sex))
    {
        sex = "";
    }
    
    String place = String.valueOf(request.getParameter("place"));
    if(null == place || "".equals(place) || "null".equals(place))
    {
        place = "";
    }
    
    String subscribe = String.valueOf(request.getParameter("subscribe"));
    if(null == subscribe || "".equals(subscribe) || "null".equals(subscribe))
    {
        subscribe = "";
    }
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    String weixBasePath = request.getScheme() + "://" + request.getServerName() + path + "/";
    
    //使用集群，文件服务器的地址
    //String filePath = GlobalMethods.getWeixFilePath();
    //MP企业微信服务协议
    String approve = (String)session.getAttribute("approve");
    
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
		<link rel="stylesheet" href="<%=iPath%>/css/contactManager.css?V=<%=StaticValue.getJspImpVersion() %>">
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/wxgl_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/wxcommon/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		$(document).ready(function(){
			$("#toggleDiv").toggle(function() {
				$("#condition").hide();
				$('#searchIcon').attr('src', '<%=inheritPath%>/images/toggle_expand.png');
				$('#searchIcon').attr('title', getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_66"));
			}, function() {
				$("#condition").show();
				$('#searchIcon').attr('src', '<%=inheritPath%>/images/toggle_collapse.png');
				$('#searchIcon').attr('title', getJsLocaleMessage("wxgl","wxgl_qywx_jsp_text_67"));
			});
		});
		</script>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
	</head>
	<body>
	<%-- header结束 --%>
	<%-- 内容开始 --%>
	<%-- 当前位置 --%>
		<%=com.montnets.emp.common.constant.ViewParams.getPosition(langName,menuCode) %>	
	<div id="rContent" class="rContent">
			
		<form name="pageForm" action="user_contactManager.htm" method="post" id="pageForm">
			<div style="display:none" id="hiddenValueDiv"></div>
			<div class="buttons">
						<div id="toggleDiv">
						</div>
					</div>
			<div id="condition">
			<div class="hd cc">
			<table>
			<tbody>
				<tr class="dl-list">
					<td>
					<emp:message key="wxgl_yhgl_title_1" defVal="公众帐号:" fileName="wxgl"/>
					</td>
					<td>
					<select id="accountId" name="a_id" class="input_bd" onchange="selGroupOfAId()">
						
						<%
					    if(null != otWeiAccList && otWeiAccList.size()>0){
                              for(LfWeiAccount acct : otWeiAccList){
                              String aId  = String.valueOf(acct.getAId());
					%>
					<option value="<%=acct.getAId() %>" <%=(aid.equals(aId))?"selected":"" %>> <%=acct.getName() %></option>
					<%
						}
					}
					%>
					</select>
					</td>
					<td>
					<emp:message key="wxgl_yhgl_title_22" defVal="所属群组：" fileName="wxgl"/>
					</td>
					<td>
					<select id="gid" name="gid" class="input_bd">
						<option value=""><emp:message key="wxgl_yhgl_title_23" defVal="全部" fileName="wxgl"/></option>
						<%
						if(otWeiGroupList != null && otWeiGroupList.size()>0){
	                             for(LfWeiGroup group : otWeiGroupList){
	                              String gId  = String.valueOf(group.getGId());
						%>
						<option value="<%=group.getGId() %>" <%=(gid.equals(gId))?"selected":"" %>> <%=group.getName()%>(<%=group.getCount()==null?0:group.getCount()%>)</option>
						<%
							}
						}
						%>
					</select>
					</td>
					<td>
					<emp:message key="wxgl_yhgl_title_24" defVal="用户昵称：" fileName="wxgl"/>
					</td>
					<td>
					<input id="nickname" name="nickname" value="<%=nickname%>">
					</td>
					<td class="tdSer">						
					<center><a id="search"></a></center>
					</td>
				</tr>	
							
				<tr class="dl-list">	
					<td>
					<emp:message key="wxgl_yhgl_title_25" defVal="用户性别：" fileName="wxgl"/>
					</td>
					<td><select id="sex" name="sex" class="input_bd">
							<option value=""><emp:message key="wxgl_yhgl_title_23" defVal="全部" fileName="wxgl"/></option>
							<option value="1"  <%=("1".equals(sex))?"selected":"" %>><emp:message key="wxgl_yhgl_title_26" defVal="男" fileName="wxgl"/></option>
							<option value="2"  <%=("2".equals(sex))?"selected":"" %>><emp:message key="wxgl_yhgl_title_27" defVal="女" fileName="wxgl"/></option>
							<option value="0"  <%=("0".equals(sex))?"selected":"" %>><emp:message key="wxgl_yhgl_title_28" defVal="未知" fileName="wxgl"/></option>
						</select>
					</td>
					
					<td>
					<emp:message key="wxgl_yhgl_title_29" defVal="用户状态：" fileName="wxgl"/>
					</td>
					<td>
					<select id="subscribe" name="subscribe" class="input_bd">
						<option value=""><emp:message key="wxgl_yhgl_title_23" defVal="全部" fileName="wxgl"/></option>
						<option value="0" <%=("0".equals(subscribe))?"selected":"" %>><emp:message key="wxgl_yhgl_title_30" defVal="未关注" fileName="wxgl"/></option>
						<option value="1" <%=("1".equals(subscribe))?"selected":"" %>> <emp:message key="wxgl_yhgl_title_31" defVal="已关注" fileName="wxgl"/></option>
					</select>
					</td>
					<td><emp:message key="wxgl_yhgl_title_44" defVal="所在地区：" fileName="wxgl"/>
					</td>
					<td>
					<input id="place" name="place" value="<%=place%>">
					</td>
					<td ></td>			
					
				</tr>
				</tbody>
				</table>
			</div>
		</div>

       	<div id="container" class="container">
       		<div >
       			<table>
       			<tr>
				<td><emp:message key="wxgl_yhgl_title_32" defVal="移动到：" fileName="wxgl"/></td><td><select id="usergid" name="usergid" class="input_bd">
					<%
					if(otWeiGroupList != null && otWeiGroupList.size()>0){
                             for(LfWeiGroup group : otWeiGroupList){
                              String gId  = String.valueOf(group.getGId());
					%>
					<option value="<%=group.getGId() %>" <%=(gid.equals(gId))?"selected":"" %>> <%=group.getName()%>(<%=group.getCount()==null?0:group.getCount()%>)</option>
					<%
						}
					}
					%>
					</select></td>
					</tr>
					</table>
			</div>
			<div class="bd" style="margin-top:5px;">
                <table id="content">
                    <thead>
                    <tr>
                        <th>
                          	 <input type="checkbox" name="checkall" id="selectAll" onclick="checkAlls(this,'checklist')" >
                        </th>
                        <th>
                          	<emp:message key="wxgl_yhgl_title_33" defVal="头像" fileName="wxgl"/> 
                        </th>
                        <th>
                          	<emp:message key="wxgl_yhgl_title_34" defVal="用户昵称" fileName="wxgl"/>
                        </th>
                        <th>
                          	<emp:message key="wxgl_yhgl_title_35" defVal="性别" fileName="wxgl"/>
                        </th>
                        <th>
                          	<emp:message key="wxgl_yhgl_title_36" defVal="地区" fileName="wxgl"/>
                        </th>
                        <th>
                          	<emp:message key="wxgl_yhgl_title_37" defVal="所属群组" fileName="wxgl"/>
                        </th>
                        <th>
                          	<emp:message key="wxgl_yhgl_title_38" defVal="所属公众帐号" fileName="wxgl"/>
                        </th>
                        <th colspan="4">
                          	<emp:message key="wxgl_yhgl_title_39" defVal="操作" fileName="wxgl"/>  
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        if(null != userbeans && userbeans.size()>0){
                            for(DynaBean bean : userbeans){
                    %>
                    <tr>
                    	<td> 
							<input type="checkbox" name="checklist" value="<%=bean.get("wcid") %>" gid="<%=bean.get("gid") %>"/>
                       </td>
                       <td valign="middle">
                        <%if(bean.get("localimgurl")!=null&&!"".equals(bean.get("localimgurl"))) {%>
                        	<img class="img-circle headimg5" alt="img" src='<%= bean.get("localimgurl")%>' title="<emp:message key="wxgl_yhgl_title_40" defVal="头像" fileName="wxgl"/>" onerror="this.src='<%= bean.get("headimgurl") %>'">
                        <% } else { %>
                       	    <img class="img-circle headimg5" alt="img" src="<%= bean.get("headimgurl") %>" title="<emp:message key="wxgl_yhgl_title_40" defVal="头像" fileName="wxgl"/>">
                        <% } %>
                        </td>
                        <td>
                       		<%
				String nickName=String.valueOf( bean.get("nickname"));
				if(null == nickName || "".equals(nickName)){out.print("-");}else{ 
				%>
					<xmp><%=nickName.length()>8?nickName.substring(0,8)+"...":nickName %></xmp>
				<%} %>
                       </td>
                        <td>
                       		<%
				String usex=String.valueOf( bean.get("sex"));
				if(null == usex || "".equals(usex)){out.print("-");}else{ 
				    if("1".equals(usex))
				    {
				        usex = MessageUtils.extractMessage("wxgl","wxgl_yhgl_title_26",request);
				    }
				    else if("2".equals(usex))
				    {
				        usex =  MessageUtils.extractMessage("wxgl","wxgl_yhgl_title_27",request);
				    }
				    else if("0".equals(usex))
				    {
				        usex =  MessageUtils.extractMessage("wxgl","wxgl_yhgl_title_28",request);
				    }
				%>
					<xmp><%=usex%></xmp>
				<%} %>
                       </td>
                        <td>
                       		<%
                       		String uplace= "-";
                       		if(bean.get("country")==null || bean.get("province")==null || bean.get("city")==null){
                       		 	uplace= "-";
                       		}else if(bean.get("country").equals("") && bean.get("province").equals("") && bean.get("city").equals("")){
                       			uplace= "-";
                       		}else{
                       			uplace=String.valueOf(bean.get("country")).concat(String.valueOf( bean.get("province"))).concat(String.valueOf( bean.get("city")));
                       		}
				%>
					<xmp><%=uplace%></xmp>
                       </td>
                        <td>
                       		<%
                       		String gname = "";
                       		try{
                       		 	gname  = bean.get("groupname").toString();
                       		}catch(Exception e){
                       		 	gname = "";
                       		}
                       		
							if("".equals(gname))
							{
							    out.print( MessageUtils.extractMessage("wxgl","wxgl_yhgl_title_41",request));
							}
							else
							{ 		
							    out.print(gname);		
							}
							%>
                       </td>
                        <td>
                        	<%
                        	Long uaid = Long.valueOf(bean.get("aid").toString());
                        	String accname = "";
                       		if(null != otWeiAccList && otWeiAccList.size()>0)
                       		{
                             for(LfWeiAccount acct : otWeiAccList)
                             {
                                 Long aId  = acct.getAId();
                             	if(null!=uaid && 0L!=uaid && null!=aId && 0L!=aId && uaid.longValue()==aId.longValue())
                             	{
                             	   accname = acct.getName();
                             	   break;
                             	}
                             }
                             out.print(accname);
                       		}
                       		else
                       		{
                       		 out.print("-");
                       		}
				%>
                       </td>                       
                        <td  colspan="4">
                        	<a onclick="showupdateUserTmp(<%=bean.get("wcid") %>,1)"><emp:message key="wxgl_yhgl_title_42" defVal="查看详情" fileName="wxgl"/></a>&nbsp;
                        	<a onclick="showupdateUserTmp(<%=bean.get("wcid") %>,2)"><emp:message key="wxgl_yhgl_title_43" defVal="修改信息" fileName="wxgl"/></a>
                        </td>
                    </tr>
                    <%
                        }
                    }else{
                    %>
                    <tr><td align="center" colspan="11"><emp:message key="wxgl_yhgl_title_21" defVal="无记录" fileName="wxgl"/></td></tr>
                    <%} %>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td colspan="11">
                            <div id="pageInfo"></div>
                        </td>
                    </tr>
                    </tfoot>
                </table>
            </div>
        </div>
        </form>
    </div>
  	<div class="clear"></div>
   	<div id="updateUserTmpDiv" title='<emp:message key="wxgl_yhgl_title_17" defVal="详细信息" fileName="wxgl"/>' style="padding:5px;display:none">
		<iframe id="updateUserTmpFrame" name="updateUserTmpFrame" style="width:650px;height:540px;border:0;" marginwidth="0" scrolling="no" frameborder="no"></iframe>				
	</div>
	<script src="<%=commonPath%>/common/js/myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/pageInfo.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/artDialog.js?skin=default"></script>
	<script type="text/javascript" src="<%=path %>/wxcommon/widget/artDialog/iframeTools.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	<link href="<%=path %>/wxcommon/css/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=path %>/wxcommon/<%=wxskin %>/artDialogCss.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
     <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link href="<%=path %>/wxcommon/css/artDialogCss_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<%}%>
	<script type="text/javascript">
	$('#accountId').isSearchSelect({'width':'145','isInput':false,'zindex':0},function(){
	   selGroupOfAId();
	});
	$('#gid,#sex,#subscribe').isSearchSelect({'width':'145','isInput':false,'zindex':0});

	$('#usergid').isSearchSelect({'width':'145','isInput':false,'zindex':0},function(){
		moveUser('all');
	});
    var pathUrl="<%=path%>",
        iPathUrl="<%=iPath%>",
        currentTotalPage="<%=pageInfo.getTotalPage()%>",
        currentPageIndex="<%=pageInfo.getPageIndex()%>",
        currentPageSize="<%=pageInfo.getPageSize()%>",
        currentTotalRec="<%=pageInfo.getTotalRec()%>",
        lguserid=<%=lguserid%>,
        lgcorpcode="<%=lgcorpcode%>";
    	$(function() {			
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
        
        //分页
        initPage(currentTotalPage,currentPageIndex,currentPageSize,currentTotalRec);
        $('#search').click(function(){
            submitForm();
        });
    });
	</script>
	<script type="text/javascript" src="<%=iPath%>/js/yhgl_contactManager.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
	</body>
</html>