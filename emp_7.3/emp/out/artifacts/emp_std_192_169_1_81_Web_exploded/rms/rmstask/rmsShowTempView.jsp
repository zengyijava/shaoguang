<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
        String inheritPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
        inheritPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
        String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
        String skin = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
        String tmId = String.valueOf(request.getAttribute("tmId"));
        String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
        %>
<html>
<body>
<input type="hidden" id="tmId" value="<%=tmId%>" />
<input type="hidden" id="langName" value="<%=langName%>" />
<div class="fx-module-pop" style="width:425px;height:525px;">
    <iframe id="cont" name="cont" class="J-vue-cont" frameborder="0" src="" width="100%" height="120%"></iframe>
</div>


<script type="text/javascript"
        src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script type="text/javascript"
        src="<%=commonPath%>/rms/rmstask/js/rmsShowTempView.js?V=<%=StaticValue.getJspImpVersion() %>"></script>

</body>
</html>
