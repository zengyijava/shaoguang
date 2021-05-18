<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String inheritPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    inheritPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String isSysmanager = String.valueOf(request.getAttribute("isSysmanager"));
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<html>
<head>
    <%@include file="/common/common.jsp" %>
    <title>Title</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet"
          href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
    <link href="<%=commonPath%>/common/css/empSelect.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath %>/rms/samemms/css/tempChoose.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
</head>
<body>
<div class="fx-module-pop"style="width:100%; height:100%;">
    <input type="hidden" id="isSysmanager" value="<%=isSysmanager%>" />
    <input type="hidden" id="langName" value="<%=langName%>" />
    <%-- module-pop-hd --%>
    <div class="module-pop-hd">
        <ul class="hd-table">
            <li id="myTemp" class="hd-table-li active" onclick="selectTemp('myTemp')">我的场景</li>
            <%--<li hidden id="publicTemp" class="hd-table-li " onclick="selectTemp('publicTemp')">模板库</li>--%>
        </ul>
    </div>
    <div id="tempList" title=""style="width:100%; height:100%;">
        <iframe id="cont" name="cont" class="J-vue-cont" frameborder="0" src="" width="100%" height="100%"></iframe>
    </div>
</div>


<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/rms/meditor/js/meditor_tempChoose.js?V=<%=StaticValue.getJspImpVersion() %>"></script>

</body>
</html>
