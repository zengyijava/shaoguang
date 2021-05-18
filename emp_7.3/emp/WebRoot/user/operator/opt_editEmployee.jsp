<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="java.util.List"%>
<%@page import="com.montnets.emp.entity.sysuser.LfSysuser"%>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.employee.vo.LfEmployeeTypeVo"%>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ taglib prefix="emp"
	uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path=request.getContextPath();
	String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
	String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
	inheritPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	//Jsp页面中获取session中的语言设置
 	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("sysuser");
	LfSysuser lc = (LfSysuser)request.getAttribute("user");
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
	if (lc == null)
	{
		lc = new LfSysuser();
	}
	@ SuppressWarnings("unchecked")
	List<LfEmployeeTypeVo> zwList = (List<LfEmployeeTypeVo>)request.getAttribute("zwList");
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
 %>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%=inheritPath %>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=inheritPath %>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="<%=inheritPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=empBasePath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
			<link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%}%>
		
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/opt_editEmployee.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/user.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		
	</head>
	<body id="opt_editEmployee" onload="show()">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="inheritPath" value="<%=inheritPath %>" />
	<input type="hidden" id="bookType"  value="employee"/>
	<input type="hidden" id="hidOpType"  value="add"/>
	<input type="hidden" id="checkUrl" value="<%=path %>/epl_employeeBook.htm?method=checkBook" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=ViewParams.getPosition(langName,menuCode) %>
			<%-- header结束 --%>
			<%-- 内容开始 --%>
			<div id="rContent" class="rContent">
			<%              		
				if(btnMap.get(menuCode+"-1")!=null)                       		
				{                        	
					%>
				<div class="titletop">
					<table class="titletop_table">
						<tr>
							<td class="titletop_td">
								<emp:message key="user_xtgl_czygl_text_63" 
										defVal="转为员工" fileName="user" />
							</td>
							<td align="right">
								<span class="titletop_font"  onclick="javascript:back()">&larr;&nbsp;<emp:message key="user_xtgl_czygl_text_3" 
										defVal="返回上一级" fileName="user" /></span>
							</td>
						</tr>
					</table>
				</div>
					<div id="detail_Info" class="detail_Info">
						<form action="<%=path %>/epl_employeeBook.htm?method=addBook" method="post" id="addForm" name="addForm" >
						<div id="loginUser" class="hidden"></div>
						<input type="hidden" name="guId"  value="<%=lc.getGuId() %>"/>
						<input type="hidden" name="depId"  value="" id="depId"/>
						<input type="hidden" id="userId" name="userId" value='<%=lc.getUserId() %>'/>
						<table id="sysTable" width="100%" height="100%">
						<thead>
						<tr>
						<td><span><emp:message key="user_xtgl_czygl_text_47" 
										defVal="工号：" fileName="user" /></span></td>
										<td>
											<label>
											<input type="text" name="employeeNo" id="employeeNo"
												value='<%=lc.getUserCode()==null?"":lc.getUserCode() %>' maxlength="20" class="input_bd input_book"/>
											</label><font class="font_red">&nbsp;*</font>
										</td>
                        </tr>
                        <tr>						
						<td><span><emp:message key="user_xtgl_czygl_text_64" 
										defVal="姓名：" fileName="user" /></span></td><td><label>
											<input type="text" name="cName" id="cName"
												value='<%=lc.getName()==null?"":lc.getName() %>' maxlength="20" class="input_bd input_book"/>
										</label><font class="font_red">&nbsp;*</font></td>
						</tr>
						<tr>
						<td><span><emp:message key="user_xtgl_czygl_text_12" 
										defVal="生日：" fileName="user" /></span></td><td><label>
								<%
								String s = "";
								if(lc.getBirthday()!=null)
								{
									s = df.format(lc.getBirthday());
								}
								%>
									<input type="text" value='<%=s %>' id="birth" name="birth"  class="Wdate birth" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%yyyy-%MM-%dd'})">
								</label></td>
						</tr>
						<tr>
						<td><span><emp:message key="user_xtgl_czygl_text_9" 
										defVal="性别：" fileName="user" /></span></td><td><label>
										<select name="sex" id="sex" class="input_bd sex"   >
											<option value="2"><emp:message key="user_xtgl_czygl_text_65" 
										defVal="未知" fileName="user" /></option>
											<option value="1" <%="1".equals(lc.getSex().toString())?"selected":"" %>><emp:message key="user_xtgl_czygl_text_10" 
										defVal="男" fileName="user" /></option>
											<option value="0" <%="0".equals(lc.getSex().toString())?"selected":"" %>><emp:message key="user_xtgl_czygl_text_11" 
										defVal="女" fileName="user" /></option>
										</select><font class="font_red">&nbsp;*</font>
									</label></td>
						</tr>
						<%--<tr>
							<td colspan="2">
								<div class="allDiv"></div>
							</td>
						</tr>  --%>
						<tr>				
						<td><span><emp:message key="user_xtgl_czygl_text_62" 
										defVal="<emp:message key="user_xtgl_czygl_text_62" 
										defVal="添加子机构" fileName="user" />" fileName="user" /><emp:message key="user_xtgl_czygl_text_66" 
										defVal="所属机构：" fileName="user" /></span></td><td>
									        <div  class="depNam_div">
												<input id="depNam"  onclick="javascript:showMenu();"  name="depNam" type="text" readonly value='<emp:message key="user_xtgl_czygl_text_49" 
										defVal="点击选择机构" fileName="user" />' class="treeInput depNam"  />
											   <a id="ssdep" onclick="javascript:showMenu();" class="ssdep" ></a><font class="font_red">&nbsp;*</font>
										 
										</div>
										<div id="dropMenu" class="dropMenu">
										<div id='closeDepTreeClass' class='closeDepTreeClassHover'></div>
										<ul id="dropdownMenu" class="tree">
										</ul>
										</div>
										</td>
						</tr>
						<tr>
						<td><span><emp:message key="user_xtgl_czygl_text_67" 
										defVal="职位：" fileName="user" /></span></td><td><label>
									        <select name="job" id="job" class="input_bd job"  >
									        		<option value=""><emp:message key="user_xtgl_czygl_text_15" 
										defVal="请选择" fileName="user" /></option>
									        		<% 
									        			if(zwList != null && zwList.size()>0){
									        				LfEmployeeTypeVo zw = null;
									        				for(int i=0;i<zwList.size();i++){
									        					zw = zwList.get(i);
									        		%>
									        			<option value="<%=zw.getName()%>"><%=zw.getName().replace("<","&lt;").replace(">","&gt;")%></option>
									        		<% 
									        				}
									        			}
									        		%>
									        		</select>
										</label></td>
						</tr>
						<tr>				
							<td><span><emp:message key="user_xtgl_czygl_text_68" 
										defVal="员工描述：" fileName="user" /></span></td><td><label>
						        <input type="text" name="comm" id="comm" value='' class="input_bd input_book"/>
							</label></td>
						</tr>
						<%--<tr>
							<td colspan="2">
								<div class="leftDiv" style="float: left;"></div>
								<div style="width: 50px;float: left;">联系方式</div>
								<div class="rightDiv" style="float: left;"></div>
							</td>
						</tr>  --%>
						<tr>
						<td><span><emp:message key="user_xtgl_czygl_text_69" 
										defVal="手机：" fileName="user" /></span></td><td><label>	
							<%
									String mobile = lc.getMobile()!=null?lc.getMobile():"";
									if(btnMap.get(StaticValue.PHONE_LOOK_CODE) == null && !"".equals(mobile)){
										//无号码的查看权限，需替换手机号码的星号
										mobile = mobile.substring(0,3)+"*****"+mobile.substring(8,11);
									}
								 %>
								 <input type="hidden" name="mobile" id="mobile" value="<%=lc.getMobile()!=null?lc.getMobile():"" %>"/>
											<input type="text" name="tempMobile" id="tempMobile"
												 onkeyup="numberControl($(this))" value='<%=mobile %>' maxlength="11" class="input_bd input_book"/>
										</label><font class="font_red">&nbsp;*</font></td>
						</tr>


						<tr>			
						<td><span>QQ：</span></td><td><label>
											<input type="text" name="qq" id="qq"
												 onkeyup="numberControl($(this))" value='<%=lc.getQq()==null?"":lc.getQq() %>' maxlength="12" class="input_bd input_book"/>
										</label></td>
						</tr>

						<tr>				
						<td><span>E-mail：</span></td><td><label>
											<input type="text" name="EMail" id="EMail" value='<%=lc.getEMail()==null?"":lc.getEMail() %>' class="input_bd input_book"/>
										</label></td>
						</tr>
						<tr>
						
						<td><span>MSN：</span></td><td><label>
											<input type="text" name="msn" id="msn" value='<%=lc.getMsn()==null?"":lc.getMsn() %>' class="input_bd input_book"/>
										</label></td>
						</tr>
						<tr>				
						<td><span><emp:message key="user_xtgl_czygl_text_42" 
										defVal="座机：" fileName="user" /></span></td><td><label>
											<input type="text" name="oph" id="oph" onkeyup="numberControl($(this))" value='<%=lc.getOph()==null?"":lc.getOph() %>' class="input_bd input_book"/>
										</label></td>
						</tr>

						<tr>
							<td><span><emp:message key="user_xtgl_czygl_text_43" 
										defVal="传真：" fileName="user" /></span></td><td>
							<label>
							<input type="text" name="chuanz" id="chuanz" onkeyup="numberControl($(this))" value='' maxlength="30" class="input_bd input_book"/>
							</label>
							</td>
						</tr>
						<tr>
						<td colspan="2" align="right" ><input type="button" name="button" id="button" value="<emp:message key="common_btn_7" 
										defVal="确定" fileName="common" />" onclick="javascript:doSub()" class="btnClass5 mr23"/>
										<input type="button" name="button2" id="button2" value="<emp:message key="common_btn_10" 
										defVal="返回" fileName="common" />" class="btnClass6" onclick="javascript:back()"/></td>
						</tr>
						</thead>
						</table>
								</form>
							</div>
							<div class="clear"></div>
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
		</div>
    <div class="clear"></div>
    	<script type="text/javascript" src="<%=empBasePath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/user_<%=langName%>.js"></script>
		<script type="text/javascript" src="<%=empBasePath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    	<script language="javascript" src="<%=inheritPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=inheritPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script language="javascript" src="<%=inheritPath %>/common/js/book.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=inheritPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=inheritPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript">
		function show(){
			<% String result=(String)request.getAttribute("result");
				if(result!=null && result.equals("true")){%>
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_25"));
				location.href="<%=path%>/opt_sysuser.htm?method=find&lguserid="+$("#lguserid").val()+
						"&lgcorpcode="+$("#lgcorpcode").val();
			<%}else if(result!=null && result.equals("false")){%>
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_26"));
			<%}%>
		}
		var zTree1;
		var setting;
		setting = {
				async : true,
				asyncUrl :"<%=request.getContextPath()%>/opt_sysuser.htm?method=getEmpSecondDepJson", //获取节点数据的URL地址
				isSimpleData: true,
				rootPID : -1,
				treeNodeKey: "id",
				treeNodeParentKey: "pId",
				callback: {
					//beforeExpand: function(){return false;},
					//beforeCollapse: function(){return false;},
					beforeAsync : function(treeId, treeNode) {
						zTree1.setting.asyncUrl="<%=request.getContextPath()%>/opt_sysuser.htm?method=getEmpSecondDepJson&depId="+treeNode.id;
					},
					click: zTreeOnClick,
					asyncSuccess:function(event, treeId, treeNode, msg){
						//zTree1.expandAll(true);
						if(!treeNode){	//判断是 顶级机构就展开,其余的收缩+
						   var rootNode = zTree1.getNodeByParam("level", 0);
						   zTree1.expandNode(rootNode, true, false);
						}
					}
				}
		};

		var zNodes =[];

		function showMenu() {
			$("#dropMenu").focus();
			var sortSel = $("#depNam");
			var sortOffset = $("#depNam").offset();
			$("#dropMenu").toggle();
			
			var disstate = $("#dropMenu").css("display");
			if(disstate == 'block'){			
			   $("#job").css("visibility","hidden");
			}else if(disstate == 'none'){
			   $("#job").css("visibility","visible");
			}
		}
		function hideMenu() {
		    $("#job").css("visibility","visible");
			$("#dropMenu").hide();
		}
		
		function zTreeOnClick(event, treeId, treeNode) {
			if (treeNode.isParent) {
				zTree1.setting.asyncUrl =  "<%=request.getContextPath()%>/opt_sysuser.htm?method=getEmpSecondDepJson&depId="+treeNode.id;
			}
			if (treeNode) {
				var sortObj = $("#depNam");
				sortObj.attr("value", treeNode.name);
				$("#depId").attr("value",treeNode.id);
				hideMenu();
			}
		}

		function reloadTree() {
			hideMenu();
			setting.asyncUrl="<%=request.getContextPath()%>/opt_sysuser.htm?method=getEmpSecondDepJson";
			
			zTree1 = $("#dropdownMenu").zTree(setting, zNodes);
		}	
		$(document).ready(function(){
			getLoginInfo("#loginUser");
		    setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
			reloadTree();
			//$("#dropMenu").blur(function(){hideMenu();})
			CloseTreePlugIN();
		});
		function back(){
			location.href="<%=path%>/opt_sysuser.htm?method=find&lguserid="+$("#lguserid").val()+
						"&lgcorpcode="+$("#lgcorpcode").val();
		}
		</script>
	</body>
</html>
