
<%@page import="com.montnets.emp.common.constant.StaticValue"%><%
String empWebPath = request.getContextPath();
String empBasePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+empWebPath+"/";
String empLangName = (String) session.getAttribute(StaticValue.LANG_KEY);
empLangName = empLangName == null?StaticValue.ZH_CN:empLangName;

String CstlyeSkin = session.getAttribute("stlyeSkin")==null?empWebPath+"/frame/frame3.0/skin/default":(String)session.getAttribute("stlyeSkin");
String empFramePath = CstlyeSkin.substring(0,CstlyeSkin.indexOf("skin"));
%>
<script>var CstlyeSkin = "<%=CstlyeSkin %>";</script>


<script type="text/javascript" src="<%=empWebPath%>/common/i18n/<%=empLangName%>/common_<%=empLangName%>.js"></script>

