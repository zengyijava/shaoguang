<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="com.montnets.emp.entity.client.LfClient"%>
<%@page import="com.montnets.emp.client.vo.LfClientVo"%>
<%@page import="com.montnets.emp.entity.client.LfCustField"%>
<%@page import="com.montnets.emp.entity.client.LfCustFieldValue"%>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="com.montnets.emp.common.constant.CommonVariables"%>
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
	String company=(String)request.getAttribute("company");
	@ SuppressWarnings("unchecked")
	Map<LfCustField, List<LfCustFieldValue>> map1=(Map<LfCustField, List<LfCustFieldValue>>)request.getAttribute("map1");
	@ SuppressWarnings("unchecked")
	Map<LfCustField, List<LfCustFieldValue>> map2=(Map<LfCustField, List<LfCustFieldValue>>)request.getAttribute("map2");
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("addrBook");
	LfClient lc = (LfClient)request.getAttribute("client");
	String[] lco = (String[])request.getAttribute("depname");
	@ SuppressWarnings("unchecked")
	Class clazz  = Class.forName("com.montnets.emp.entity.client.LfClient");
     Object smgr = (Object)clazz.newInstance();
      Object[]   args   =   {}; 
      @ SuppressWarnings("unchecked")
      Class[]   types   =   {};
     Method mf=null;
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
	
	String lfclientid = String.valueOf(lc.getClientId());
	
	String lguserid = String.valueOf(request.getAttribute("lguserid"));
	
	CommonVariables commonVariables = new CommonVariables();
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);	
 %>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="/common/common.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath %>/css/cli_editClient.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/ydkf_TongXunLuGuanLi.css?V=<%=StaticValue.getJspImpVersion() %>"/>
		<%if(StaticValue.ZH_HK.equals(langName)){%>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<style type="text/css">
			#sysTable  tr  td:nth-child(1){width: 135px;}
		</style>
		<%}%>
	</head>
	<body onload="show(<%=request.getAttribute("addresult") %>)" id="cli_editClient">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
	<input type="hidden" id="inheritPath" value="<%=inheritPath %>" />
	<input type="hidden" id="bookType"  value="client"/>
	<input type="hidden" id="hidOpType"  value="edit"/>
	<input type="hidden" id="lfclientid"  value="<%=lfclientid %>"/>
	<input type="hidden" id="userid"  value="<%=lguserid %>"/>
	<input type="hidden" id="actionType"  value="edit"/>
	<input type="hidden" id="checkUrl" value="<%=path %>/cli_addrBook.htm?method=checkBook" />
	<%--处理 客户手机号码是否合法 --%>
	<input type="hidden" id="checkClient" value="<%=path %>/cli_addrBook.htm?method=checkClient" />
		<div id="container" class="container">
			<%-- header开始 --%>
			<%=com.montnets.emp.common.constant.ViewParams.getPosition(empLangName, menuCode) %>
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
							<td  class="titletop_td">
								<emp:message key="client_khtxlgl_kftxl_text_modifyclient" defVal="修改客户" fileName="client"></emp:message>
							</td>
							<td align="right">
								<font class="titletop_font" onclick="javascript:back()">&larr;&nbsp;<emp:message key="client_common_opt_gobak" defVal="返回上一级" fileName="client"></emp:message></font>
							</td>
						</tr>
					</table>
					</div>


					<div id="detail_Info">
						<form action="<%=path %>/cli_addrBook.htm?method=editBookcd" method="post" id="addForm" name="addForm" >
						<div class="client_display_none" id="hiddenValueDiv"></div>
						<input type="hidden" id="bookId" name="bookId" value="<%=lc.getClientId()==null?"":lc.getClientId() %>"/>
						<table id="sysTable" width="90%" height="100%">
						<thead>

						<tr align="left">
							<td><span><emp:message key="client_khtxlgl_kftxl_text_clientnumber" defVal="客户号" fileName="client"></emp:message>：</span></td>
							<td>
								<label><%=lc.getClientCode()==null?"":lc.getClientCode() %>
									<input class="input_bd" type="hidden" name="clientCode" id="clientCode"
										   value="<%=lc.getClientCode()==null?"":lc.getClientCode() %>" maxlength="20"  readonly="readonly"/>
								</label>
							</td>
						</tr>

						<tr align="left">
							<td class="client_td1"><span><emp:message key="client_khtxlgl_kftxl_text_name" defVal="姓名" fileName="client"></emp:message>：</span></td>
							<td>
								<label>
								<input class="input_bd" type="text" name="cName" id="cName" onkeyup="validateInput(this)" value="<%=lc.getName()==null?"":lc.getName().replace("&","&amp;").replace("\"","&quot;")%>" maxlength="60"/>
								<input class="input_bd" type="hidden" name="cNameTemp" id="cNameTemp" value="<%=lc.getName()==null?"":lc.getName().replace("&","&amp;").replace("\"","&quot;") %>">
								<font class="client_color_red">&nbsp;*</font>
							</label>
							</td>
						</tr>

						<tr align="left">
						<td><span><emp:message key="client_khtxlgl_kftxl_text_phone" defVal="手机" fileName="client"></emp:message>：</span></td>
							<td>
								<label>
								<%
								if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null) {
								%>
									<input  type="text" name="mobile" class="graytext input_bd"
									id="mobile" onkeyup="phoneInputCtrl($(this))" value="<%=lc.getMobile()==null?"":lc.getMobile() %>" maxlength="21"/>
								<%}else{
								%>
								<input type="text" name="mobile" class="graytext input_bd" id="mobile"
								onkeyup="phoneInputCtrl($(this))" value="<%=lc.getMobile()==null?"":commonVariables.replacePhoneNumber(lc.getMobile()) %>" maxlength="21"/>
								<%
								} %>
								<input type="hidden" name="mobileTemp" id="mobileTemp" value="<%=lc.getMobile()==null?"":lc.getMobile() %>"/>
								<font class="client_color_red">&nbsp;*</font>
							</label>
						</td>
						</tr>

						<tr align="left">
							<td><span><emp:message key="client_khtxlgl_kftxl_text_duties" defVal="职务" fileName="client"></emp:message>：</span></td>
							<td>
								<label>
									<input class="input_bd" type="text" name="job"
									id="job" value="<%=lc.getJob()==null?"":lc.getJob().replace("&","&amp;").replace("\"","&quot;")%>"
									 maxlength="20"/>
								</label>
							</td>
						</tr>

						<tr align="left">
						<td><span><emp:message key="client_khtxlgl_kftxl_text_affiliation" defVal="所属机构" fileName="client"></emp:message>：</span></td><td>
							<%--选择的机构ID    #^@;depid;depname#^@ 格式--%>
							<input type="hidden" name="isExistDep" id="isExistDep" value="<%=lco[2]==null?"":lco[2] %>" />
							<input type="hidden" name="depId" id="depId" value="<%=lco[1]==null?"":lco[1] %>" />
							<input type="hidden" name="depIdTemp" id="depIdTemp" value="" />
					    <div class="client_div1">
						<input id="depNam" onfocus="this.blur()" class="input_bd treeInput" name="depNam" type="text"
						onclick="javascript:showMenu();" readonly value="<%=lco[0]==null?"":lco[0]%>"/>
						<font class="client_color_red">&nbsp;*</font>
						</div>
						<div id="dropMenu">
							<div class="client_div2">
									<input type="button" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" class="btnClass1" onclick="javascript:zTreeOnClickOK();"/>&nbsp;&nbsp;
									<input type="button" value="<emp:message key="client_common_opt_emptiy" defVal="清空" fileName="client"></emp:message>" class="btnClass1" onclick="javascript:cleanSelect();"/>
							</div>
							<ul id="dropdownMenu" class="tree">
							</ul>
						</div>
						</td>
						</tr>

						<tr align="left">
							<td><span><emp:message key="client_khtxlgl_kftxl_text_industry" defVal="行业" fileName="client"></emp:message>：</span></td>
							<td>
								<label>
								<input type="text" class="input_bd" name="pro"
									id="pro" value="<%=lc.getProfession()==null?"":lc.getProfession().replace("&","&amp;").replace("\"","&quot;") %>"  maxlength="20" />
								</label>
							</td>
						</tr>

						<tr align="left">
							<td><span><emp:message key="client_khtxlgl_kftxl_text_accountmanager" defVal="客户经理" fileName="client"></emp:message>：</span></td>
							<td>
								<label>
									<input type="text" class="input_bd" name="eName"
									id="eName" value="<%=lc.getEname()==null?"":lc.getEname().replace("&","&amp;").replace("\"","&quot;") %>" maxlength="20" />
								</label>
							</td>
						</tr>

						<tr align="left">
							<td><span><emp:message key="client_khtxlgl_kftxl_text_sex" defVal="性别" fileName="client"></emp:message>：</span></td>
							<td>
								<label>
									<select class="input_bd"  name="sex" id="sex">
										<option value="2"><emp:message key="client_khtxlgl_kftxl_text_unknown" defVal="未知" fileName="client"></emp:message></option>
										<option value="1" <%="1".equals(lc.getSex().toString())?"selected":"" %>><emp:message key="client_khtxlgl_kftxl_text_man" defVal="男" fileName="client"></emp:message></option>
										<option value="0" <%="0".equals(lc.getSex().toString())?"selected":"" %>><emp:message key="client_khtxlgl_kftxl_text_woman" defVal="女" fileName="client"></emp:message></option>
									</select>
								</label>
							</td>
						</tr>
						<tr align="left">
							<td><span><emp:message key="client_khtxlgl_kftxl_text_birthday" defVal="生日" fileName="client"></emp:message>：</span></td>
							<td><label>
								<%
									String s = "";
									if(lc.getBirthday()!=null)
									{
										s = df.format(lc.getBirthday());
									}
								%>
								<input type="text" value="<%=s %>" id="birth" name="birth" 
									   class="Wdate" readonly="readonly" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%yyyy-%MM-%dd'})">
							</label>
							</td>
						</tr>
						<tr align="left">
							<td><span>QQ：</span></td>
							<td>
								<label>
									<input type="text" class="input_bd" name="qq" id="qq"
									onkeyup="numberControl($(this))" value="<%=lc.getQq()==null?"":lc.getQq() %>"
									maxlength="12"/>
								</label>
							</td>
						</tr>
						<tr align="left">
							<td><span>E-mail：</span></td>
							<td>
								<label>
									<input type="text" class="input_bd"  name="EMail" id="EMail"
										   value="<%=lc.getEMail()==null?"":lc.getEMail() %>" maxlength="32"/>
								</label>
							</td>
						</tr>
						<tr align="left">
							<td><span>MSN：</span></td>
							<td>
								<label>
									<input type="text" class="input_bd"  name="msn" id="msn" value="<%=lc.getMsn()==null?"":lc.getMsn() %>"  maxlength="21"/>
								</label>
							</td>
						</tr>
						<tr align="left">
							<td><span><emp:message key="client_khtxlgl_kftxl_text_landline" defVal="座机" fileName="client"></emp:message>：</span></td>
							<td>
								<label>
									<input type="text"  class="input_bd" name="oph" id="oph" onkeyup="numberControl($(this))" value="<%=lc.getOph()==null?"":lc.getOph().replace("&","&amp;").replace("\"","&quot;") %>" />
								</label>
							</td>
						</tr>
						<tr align="left">
							<td><span><emp:message key="client_khtxlgl_kftxl_text_belongarea" defVal="所属区域" fileName="client"></emp:message>：</span></td>
							<td>
								<label>
									<input type="text" class="input_bd" name="area" id="area"
										   value="<%=lc.getArea()==null?"":lc.getArea() %>" maxlength="20"/>
								</label>
							</td>
						</tr>
						<tr align="left">
							<td><span><emp:message key="client_khtxlgl_kftxl_text_clientdes" defVal="客户描述" fileName="client"></emp:message>：</span></td>
							<td><label>
								<input type="text" class="input_bd" name="comm" id="comm"
									   value="<%=lc.getComments()==null?"":lc.getComments().replace("&","&amp;").replace("\"","&quot;") %>" maxlength="64" />
							</label>
							</td>
						</tr>
							<%
							if(map1.size()>0)
							{
							 int i=0;
							 for (Iterator it =  map1.keySet().iterator();it.hasNext();)
							   {
							    LfCustField key1 = (LfCustField)it.next();
							    List<LfCustFieldValue> value1=map1.get(key1);

							%>
								<tr align="left" class="dxkf_tr1">
									<td>
										<span>
											<%=key1.getField_Name().replace("<","&lt;").replace(">","&gt;")%>：
										</span>
									</td>
									<td>
										<label>
											<select class="input_bd client_select"  name="<%=key1.getField_Name()%>" id="<%=key1.getField_Name()%>">
												<option value=""><emp:message key="client_common_text_select" defVal="请选择" fileName="client"></emp:message></option>
												<%  for(int w=0;w<value1.size();w++)
													{
														LfCustFieldValue fc=value1.get(w);
														String sh1="F"+key1.getField_Ref().substring(1).toLowerCase();
							                  			mf= clazz.getDeclaredMethod("get"+sh1,types);
								                		String aa=(String)mf.invoke(lc,args);
												%>
													<option value="<%=fc.getId()%>"  <%=fc.getId().toString().equals(aa)?"selected":"" %>>
														<%=fc.getField_Value().replace("<","&lt;").replace(">","&gt;")%>
													</option>
												<%
													}
												%>
											</select>
										</label>
									</td>
								</tr>
								<%
							 	}
							}

						     if(map2.size()>0){
						     	int m=1;
						     	for (Iterator it =  map2.keySet().iterator();it.hasNext();)
								   {
								      LfCustField key2 = (LfCustField)it.next();
								      List<LfCustFieldValue> value2=map2.get(key2);
							%>
								<tr align="left" class="dxkf_tr1">
									<td>
										<span>
											<%=key2.getField_Name().replace("<","&lt;").replace(">","&gt;")%>：
										</span>
									</td>
									<td>
										<% for(int k=0;k<value2.size();k++){
											LfCustFieldValue fc=value2.get(k);
											boolean blo=false;
									    	clazz  = Class.forName("com.montnets.emp.entity.client.LfClient");
					                    	String sh2="F"+key2.getField_Ref().substring(1).toLowerCase();
					                   		 mf= clazz.getDeclaredMethod("get"+sh2,types);
						                	String aa2=(String)mf.invoke(lc,args);
						                	if(aa2!=null&&!"".equals(aa2))
						               		 {
							                	String[] as=aa2.split(";");
							                	for(int bl=0;bl<as.length;bl++)
							                	{
									                if(as[bl].equals(fc.getId().toString()))
									                {
									                   blo=true;
									                }
							            		 }
						          			 }
											%>
											<input type="checkbox" name="<%=key2.getField_Name()%>"
												value="<%=fc.getId()%>" <%=blo?"checked":"" %>/>
												&nbsp;<%=fc.getField_Value().replace("<","&lt;").replace(">","&gt;")%>
												&nbsp;&nbsp;&nbsp;&nbsp;
										<%
										}
										%>
									</td>
								</tr>
                                 <%
									}
								}
							  %>
						<tr class="client_tr1">
						<td></td>
						<td align="left" height="30px" class="client_tr2">
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" name="button" id="button" value="<emp:message key="client_common_opt_confire" defVal="确定" fileName="client"></emp:message>" class="btnClass5 mr23"  onclick="javascript:doSubClient()" />
					    <input type="button" name="button2" id="button2" onclick="javascript:back()" value="<emp:message key="client_common_opt_back" defVal="返回" fileName="client"></emp:message>" class="btnClass6" />
					    &nbsp;&nbsp;&nbsp;&nbsp;
						</td>
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
    <script language="javascript" src="<%=commonPath %>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>
		<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=empLangName%>/client_<%=empLangName%>.js"></script>
		<script language="javascript" src="<%=iPath %>/js/editClient.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
		<script language="javascript" src="<%=iPath %>/js/book.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
	</body>
    <script type="text/javascript">
        var str_val = "";
        function validateInput(obj) {
            obj.value = obj.value.replace(/[']+/img, '');
            str = obj.value;
            var Expression = /^[\u4e00-\u9fa5]+$/;
            var poiseision = /^[a-zA-Z]+$/;
            var objExp = new RegExp(Expression);
            var pinExp = new RegExp(poiseision);
            if (objExp.test(str)) {
                if (str.length > 30) {
                    $(obj).val(str_val);
                    alert("客户姓名长度英文字符不得超过60个字符，中文字符不得超过30字符");
                    return;
                }
            } else if (pinExp.test(str)) {
                if (str.length > 60) {
                    $(obj).val(str_val);
                    alert("客户姓名长度英文字符不得超过60个字符，中文字符不得超过30字符");
                    return;
                }
            } else {
                if (str.length > 30) {
                    $(obj).val(str_val);
                    alert("客户姓名长度英文字符不得超过60个字符，中文字符不得超过30字符");
                    return;
                }
            }
            str_val = str;
            $(obj).val(str_val);
        }
    </script>
</html>
