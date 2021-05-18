<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@page import="com.montnets.emp.sysuser.vo.LfRolesVo"%>
<%@page import="com.montnets.emp.common.vo.LfPrivilegeAndMenuVo"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	//@ SuppressWarnings("unchecked")
	//List<LfRoles> roleList=(List<LfRoles>)request.getAttribute("roleList");
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	//角色集合
	@ SuppressWarnings("unchecked")
	List<LfRolesVo> roleVoList=(List<LfRolesVo>)request.getAttribute("roleVoList");
	
	@ SuppressWarnings("unchecked")
	List<LfPrivilegeAndMenuVo> prList=(List<LfPrivilegeAndMenuVo>)request.getAttribute("privilegeList");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");
		@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("role");
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
	<title><%=titleMap.get(menuCode) %></title>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 		<link href="<%=inheritPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=iPath %>/css/role.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/role.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
          
          <%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
			<style type="text/css">
			.l_pLeft{width: auto;text-indent: 40px;}
			.l_mana_son{padding-left: 20px;}
			</style>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opt_role.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="opt_role">
	<input type="hidden" id="pathUrl" value="<%=path %>">
			<div id="container" class="container">
				<%=ViewParams.getPosition(langName,menuCode) %>
				<div id="loginUser" class="hidden"></div>
				<%if(btnMap.get(menuCode+"-0")!=null) { %>
				<div class="rContent" id="rContent" style="">
				
           			<div id="roleSetup">
               			<div id="r_s_sider">
                       		<div id="siderList_con" class="div_bd">
                      		 	<h5 class="div_bd title_bg jslbh5"  ><emp:message key="user_xtgl_czygl_text_73" 
										defVal="角色列表" fileName="user" /></h5>
                      		 	<div id="depOperate" class="depOperate">
								    <%if(btnMap.get(menuCode+"-2")!=null){ %>
										<span id="delDepNew" class="depOperateButton3" onclick="deleteFirm()"></span>
								    <%} %>
									<%if(btnMap.get(menuCode+"-3")!=null){ %>
										<span id="updateDepNew" class="depOperateButton2" onclick="updateDepFun()"></span>
									<%} %>
								    <%if(btnMap.get(menuCode+"-1")!=null){ %>
										<span id="addDepNew" class="depOperateButton1" onclick="addDepFun()"></span>
								    <%} %>
								</div>
								<div id="roleNameHideenDiv"  class="roleNameHideenDiv">
									<input type="text" class="graytext" name="roleName" id="textfield" maxlength="32" />
									<label class="placeholder" style=""><emp:message key="user_xtgl_czygl_text_74" 
										defVal="请输入" fileName="user" /></label>
									<span id="roleNameHideenSpan"></span>
								</div>
                       			<div id="siderList" class="list">
                     			    <%
                     				if(roleVoList!=null)
                    			{
	                     			for(int i=0;i<roleVoList.size();i++)
	                     			{
	                     				LfRolesVo role=roleVoList.get(i);
                    			%>
				                	<p title="<%=role.getRoleName().replace("<","&lt;").replace(">","&gt;")%>">
		                            	<%
											String roleName = role.getRoleName().replace("<","&lt;").replace(">","&gt;");
										%>
										<%=roleName.length() > 8 ? roleName.substring(0,8) + "..." : roleName%>
		                            	<input type="hidden" class="Id" value="<%=role.getRoleId() %>"/>
       			                        <input type="hidden" class="roleName" value="<%=role.getRoleName()%>"/>
		                            	<input type="hidden" id="comments" value="<%if(role.getComments()!=null){out.print(role.getComments());} %>"/>
		                            </p>
		                        <%	
		                        	}
                      			}
                     			%> 
                            </div>  
                        </div>
					    <%-- 标示是添加还是修改操作 --%>
					    <input type="hidden" id="state" name="state" value="0"/>
       				</div><%--end r_s_sider--%>
             	<div id="r_s_manager"> 
			  		<div id="m_container" class="m_container">
				     	<div id="m_c_list">
				     		<%
				     			String modName="";
				     		String showModName = "";
				     			String menuName="";
				     		String showMenuName = "";
				     		String showComments="";
				     			//大模块名称
				     			String bigMenName="";
				     			String showBigMenuName="";
				     			String MenuClassName="";
				     			String showMenuClassName="";
				     			String className="";
				     			for(int i=0;i<prList.size();i++)
				     			{
				     				LfPrivilegeAndMenuVo pri=prList.get(i);
				     				showComments = pri.getComments();
				     				if(StaticValue.ZH_TW.equals(langName)){
				     					showComments = pri.getZhTwComments();
			     					}else if(StaticValue.ZH_HK.equals(langName)){
			     						showComments = pri.getZhHkComments();
			     					}
				     				
				     				className=" index_roleMemu_"+pri.getMenuNum();
				     				if(!bigMenName.equals(pri.getTitle())){
				     					bigMenName=pri.getTitle();
				     					showBigMenuName = pri.getTitle();
				     					if(StaticValue.ZH_TW.equals(langName)){
				     						showBigMenuName = pri.getZhTwTitle();
				     					}else if(StaticValue.ZH_HK.equals(langName)){
				     						showBigMenuName = pri.getZhHkTitle();
				     					}
				     					if(i>0){
				     						out.print("</div>");	
				     					}
				     					%>
				     					
				     					<div class='juli'></div>
				     				<div class="mod1">
				     				<div id="channel_management" class="l_m_c_list div_bd title_bg">
				     					<p class="l_pLeft2<%=className %>"><a><strong class="showBigMenuName_strong"><%=showBigMenuName%></strong></a>  </p>
				     					<p class="l_pRight">
			                  		<input type="checkbox" name="checkboxs" id="checkboxs" class="checkboxs"/>
			                  		&nbsp;<label class="checkboxs_label" ><emp:message key="user_xtgl_czygl_text_75" 
										defVal="全选" fileName="user" /></label> &nbsp;&nbsp;
			                  	</p>  
			                  	</div>
				     					<%
				     				}
				     				if(!modName.equals(pri.getModName()))
				     				{
				     					modName=pri.getModName();
				     					showModName = pri.getModName();
				     					if(StaticValue.ZH_TW.equals(langName)){
				     						showModName = pri.getZhTwModName();
				     					}else if(StaticValue.ZH_HK.equals(langName)){
				     						showModName = pri.getZhHkModName();
				     					}
				     		%>
			               		                      	 
            					
            					<div class="div_bd mod2">
            					<span class="unfold fico"></span>
            					<%=showModName %>
            					
            					</div>
            					<div class="chan_manag_son  div_bd" id="manag_son">
					            	<%
						     				}
						     				if(!menuName.equals(pri.getMenuCode()))
						     				{
						     					menuName=pri.getMenuCode();
						     					showMenuName = pri.getMenuName();
						     					if(StaticValue.ZH_TW.equals(langName)){
						     						showMenuName = pri.getZhTwMenuName();
						     					}else if(StaticValue.ZH_HK.equals(langName)){
						     						showMenuName = pri.getZhHkMenuName();
						     					}
					            	%>
				                	<div class="l_mana_son">
				                  		<p class="l_pLeft"><%=showMenuName %>：</p>
				                    	<p class="l_pRightCon">
					                    <%
						     				}
					                    %>
					                   			<label id="<%=pri.getPrivilegeId() %>"><span class=""><%=showComments %></span><span class="fackCheckbox"></span> </label>
					       		            	<%
			     				
					       		        	if(i==prList.size()-1)
					       		        	{
		       		        			%>
       		        
				                     	</p>
					            	</div>
					            	
				              	</div>
				          		
				          		<%	
				          				}else{
				          					if(!pri.getMenuCode().equals(prList.get(i+1).getMenuCode()))
					          				{
					          					out.print("</p></div>");
					          					if(i<prList.size()-1 && !pri.getModName().equals(prList.get(i+1).getModName()))
					           		        	{
					           		        		out.print("</div>");	
					           		        	}
					          				}
				          					
				          				}
				          			
					     			}
				     			if(prList.size()>0){
				     				out.print("</div>");
				     			}
					     		%> 
					     		
          					</div>
         				</div>
						<div id="remark" class="remark">
							<span><emp:message key="user_xtgl_czygl_text_76" 
										defVal="备注" fileName="user" /></span>
							<textarea name="comments" id="textarea" cols="45" rows="5" ></textarea>
						</div>
             		<div id="buttonDiv">
             		<%if(btnMap.get(menuCode+"-3")!=null||btnMap.get(menuCode+"-1")!=null){ %>
             			<input type="button" class="btnClass5 mr23" value="<emp:message key="common_btn_8" 
										defVal="保存" fileName="common" />" onclick="saveRole()"/>
             		<%} %>
             			<input type="button" id="btnCancel" class="btnClass6" value="<emp:message key="common_btn_9" 
										defVal="重置" fileName="common" />"/>
             		</div>
             		</div><%--end r_s_manager--%>
    			</div>
			</div>
			<%
				}
			%>
			<div class="clear"></div>
			<%--end round_content--%>
			<div class="bottom">
				<div id="bottom_right">
					<div id="bottom_left"></div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
		<script language="javascript" src="<%=inheritPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script src="<%=iPath %>/js/role.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		 <script type="text/javascript">
			$(document).ready(function() {
				$("#addDepNew").hover(function() {
					$(this).removeClass("depOperateButton1");
					$(this).addClass("depOperateButton1On");
				}, function() {
					$(this).addClass("depOperateButton1");
					$(this).removeClass("depOperateButton1On");
				});

				$("#updateDepNew").hover(function() {
					$(this).removeClass("depOperateButton2");
					$(this).addClass("depOperateButton2On");
				}, function() {
					$(this).addClass("depOperateButton2");
					$(this).removeClass("depOperateButton2On");
				});

				$("#delDepNew").hover(function() {
					$(this).removeClass("depOperateButton3");
					$(this).addClass("depOperateButton3On");
				}, function() {
					$(this).addClass("depOperateButton3");
					$(this).removeClass("depOperateButton3On");
				});
				getLoginInfo("#loginUser");
	    			//var menuHeight = $("#r_s_manager").height();
	                //$("#siderList").height(menuHeight-25);	
	            setLeftHeight();
	            //inputTipText();	                
			})
          </script>
	</body>
</html>
