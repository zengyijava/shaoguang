<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.montnets.emp.entity.sysuser.LfSysuser" %>
<%@ page import="com.montnets.emp.entity.engine.LfProCon" %>
<%@ page import="com.montnets.emp.entity.engine.LfProcess" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
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
	LfSysuser curSysuser = (LfSysuser) session.getAttribute("loginSysuser");
			
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("moService");
	
	@ SuppressWarnings("unchecked")
	Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
	
	String skin = session.getAttribute("stlyeSkin")==null?"default":
		(String)session.getAttribute("stlyeSkin");
	
	@ SuppressWarnings("unchecked")
	List<LfProCon> proConList = (List<LfProCon>)request.getAttribute("proConList");
	int tableSize = 0;
	if(request.getAttribute("tableSize") != null)
	{
		tableSize = Integer.parseInt(request.getAttribute("tableSize").toString());
	}
	@ SuppressWarnings("unchecked")
	List<LfProcess> proList = (List<LfProcess>)request.getAttribute("proList");
	String prId = (String)request.getAttribute("prId");
	String serId = (String)request.getAttribute("serId");
		
	String lguserid = (String)request.getAttribute("lguserid");
	String lgcorpcode = (String)request.getAttribute("lgcorpcode");
	
	StringBuffer str=new StringBuffer("<option value=''></option>");
	for(int i=0;i<proList.size();i++)
	{
		LfProcess process=proList.get(i);
		str.append("<option  value='"+process.getPrId()+"'>"+process.getPrName().replace("<","&lt;").replace(">","&gt;")+"</option>");
	}
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
	<head>
		<%@include file="/common/common.jsp" %>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<%if(StaticValue.ZH_HK.equals(langName)){%>
			<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<%}%>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=iPath%>/css/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=skin %>/addMo.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion()%>" />
		<link rel="stylesheet" type="text/css" href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion()%>"/>
		<link rel="stylesheet" type="text/css" href="<%=commonPath%>/znyq/engine/css/eng_common.css?V=<%=StaticValue.getJspImpVersion()%>" />
		
		
		
		
	</head>

	<body onload="show()" id="eng_editProCon" class="eng_editProCon">
	<input type="hidden" id="pathUrl" value="<%=path %>" />
		<div id="container" class="container">
						<%--
							<div id="u_o_c_explain">
								<p>
									说明：编辑执行条件
								</p>
								<ul>
									<li>
										用于设置步骤的执行条件，仅当条件表达式的值与条件值比较的结果为真时，步骤才允许执行，多个执行条件的逻辑关系为与
									</li>
									<li>
										一条执行条件记录包含条件表达式、操作符、条件值三个字段，分别说明如下：
									</li>
									<li>
										条件表达式：提供下拉选择框以选择处理步骤，用于提供步骤输出结果；提供输入框以输入用户参数、系统变量、处理步骤输出等可选
										项，作为条件的表达式
									</li>
									<li>
										条件表达式输入框说明：
									</li>
									<li>可输入用户参数变量，如userpara_0或userpara_1，userpara_2，...，userpara_n，其中userpara_0表示上行信息的手机号，userpara_1表示第一个用户参数，如此类推</li>
									<li>可以输入select步骤中要查询的列名或别名，例如select步骤（select name from xxx）,那么如果条件表达式输入框输入name，则表示会对比字段name的所有记录的值是否符合条件值，只要select步骤的结果集有一个不符合条件，那么都不符合条件</li>
									<li>如果步骤下拉选择框所选的步骤类型为select步骤，则可以输入“resultcount”，该变量存放了select步骤执行后所得结果集的总行数</li>
									<li>如果步骤下拉选择框所选的步骤类型为delete/update/insert步骤，则可以输入“infcounts”，该变量存放了delete/update/insert步骤执行后的受影响行数</li>
									<li>	
										操作符：指定条件表达式与条件值之间的关系，如=(相等)，&lt;(小于)，&gt;(大于)，!=(不等于)等；
									</li>
									<li>
										条件值：用于与条件表达式进行比较的值。
									</li>
								</ul>
							</div>
						 --%>
						<form method="post" action="<%=path %>/eng_moService.htm?method=upProCon" id="proform">
						<div class="hiddenValueDiv" id="hiddenValueDiv"></div>
							<input type="hidden" name="prId" value="<%=prId %>"/>
							<input type="hidden" name="serId" value="<%=serId %>"/>
							<input type="hidden" name="lguserid" id="lguserid" value="<%=lguserid %>"/>
							<input type="hidden" name="lgcorpcode" id="lgcorpcode" value="<%=lgcorpcode %>"/>
							<input type="hidden" id="hidStr" value="<%=str.toString() %>"/>
							<div id="detail_Info" class="div_bg">
							<input type="hidden" value="<%=tableSize %>" id="tableSize"/>
							<div class="addConditionDiv">
							<a id="addCondition" onclick="addTable()"/><emp:message key="znyq_ywgl_sxywgl_zjtj" defVal="增加条件" fileName="znyq"></emp:message></a>
							</div>
							<%
								if(tableSize>0)
								{
									for(int i=0;i<tableSize;i++)
									{
										LfProCon proCon=proConList.get(i);
							%>
							<table id="tableIndex<%=i %>">
							<thead>
							<tr>
							<td class="tjbdsTd">
							<emp:message key="znyq_ywgl_sxywgl_tjbds_mh" defVal="条件表达式：" fileName="znyq"></emp:message>
							<select name="UsedPrId" class="UsedPrId">
							<option value=""></option>
							<%
								Long prid=proCon.getUsedPrId();
								for(int j=0;j<proList.size();j++)
								{
									LfProcess process=proList.get(j);
							%>
									<option value="<%=process.getPrId() %>" <%=prid-process.getPrId()==0?"selected":"" %>>
									<%=process.getPrName().replace("<","&lt;").replace(">","&gt;") %>
									 </option>
							<%
								}
							%>
							</select>
							<input type="text"  id="conExpress"  name="conExpress" value="<%=proCon.getConExpress() %>" maxlength="32" />
							</td>
							<td>
							<emp:message key="znyq_ywgl_sxywgl_czf_mh" defVal="操作符：" fileName="znyq"></emp:message>
							<select name="conOperate" id="conOperate">
								<option value="=">=</option>
								<option value="&lt;" <%=proCon.getConOperate().equals("<")?"selected":"" %>>&lt;</option>
								<option value="&gt;" <%=proCon.getConOperate().equals(">")?"selected":"" %>>&gt;</option>
								<option value="&lt;=" <%=proCon.getConOperate().equals("<=")?"selected":"" %>>&lt;=</option>
								<option value="&gt;=" <%=proCon.getConOperate().equals(">=")?"selected":"" %>>&gt;=</option>
								<option value="!="  <%=proCon.getConOperate().equals("!=")?"selected":"" %>>!=</option>
							</select>
							</td>
							<td>
							<emp:message key="znyq_ywgl_sxywgl_tjz_mh" defVal="条件值：" fileName="znyq"></emp:message><input id="conValue" width="80px" name="conValue" value="<%=proCon.getConValue()==null?"":proCon.getConValue() %>" maxlength="32" />
							</td>
							<td>
							<a onclick="delTable(<%=i %>)"><img src="<%=skin %>/images/x.png"/></a>
							</td>
							</tr>
							</thead>
							</table>
							<%
									}
								}
							%>
							</div>
							
							<div class="btnDiv">
							<br/>
							<font class="tipColor"><emp:message key="znyq_ywgl_sxywgl_ztjbdssrkdzwks" defVal="*注：条件表达式输入框的值为空时，则该执行条件不会被创建" fileName="znyq"></emp:message></font>
							<br/><br/>
							<input type="hidden" name="serId" value="<%=request.getAttribute("serId") %>"/>
							<input type="button" id="addBasicInfoBtn" value="<emp:message key="znyq_ywgl_common_btn_7" defVal="确定" fileName="znyq"></emp:message>" onclick="submitForm()" class="btnClass5 mr23"/>
							<input type="button"  name="previous" id="previous" value="<emp:message key="znyq_ywgl_common_btn_16" defVal="取消" fileName="znyq"></emp:message>" onclick="javascript:window.parent.closeConditionDiv()" class="btnClass6"/>
							<%-- 为了兼容ie8下取消按钮右下角旁边能点击到确定按钮，增加这个换行 --%>
							<br/>
							</div>	
						</form>	
						<div class="clear"></div>
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
    <div class="clear"></div>
    
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
    <script type="text/javascript" src="<%=iPath%>/js/quotes.js?V=<%=StaticValue.getJspImpVersion()%>"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/znyq_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    <script type="text/javascript">
		//var operateStr="操作符：<select name=\"conOperate\">"+
		var operateStr=getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czf_mh")+"<select name=\"conOperate\">"+
		"<option value=\"=\">=</option>"+
		"<option value=\"&lt;\">&lt;</option>"+
		"<option value=\"&gt;\">&gt;</option>"+
		"<option value=\"&lt;=\">&lt;=</option>"+
		"<option value=\"&gt;=\">&gt;=</option>"+
		"<option value=\"!=\">!=</option></select>";
		var expressStr;
	    $(document).ready(function(){
	    	getLoginInfo("#hiddenValueDiv");
	    	//expressStr="条件表达式：<select style='width:100px' name=\"UsedPrId\">"+"<%=str %>"+
	    	expressStr=getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tjbds_mh")+"<select class='UsedPrId' name=\"UsedPrId\">"+"<%=str %>"+
   			"</select><input name=\"conExpress\" id=\"conExpress\" size=\"8\" maxlength='32' value=\"\"/>";
	
	    	$("#detail_Info img").hover(
			  function(){
				  $(this).attr("src","<%=skin %>/images/x.png");
			  },
			  function(){
				  $(this).attr("src","<%=skin %>/images/x2.png");
			  }
			);
	    });
	    function delTable(size){
	         $("#detail_Info").find("> table").each(
				function(index){
					if(this.id==("tableIndex"+size))
					{
						$(this).remove();
					}
				}
	     	);
	         if( $("#detail_Info").find("> table").length==0)
	         {
	      	   var tableStr="<table id='tableIndex0'><thead><tr>"+
	            	"<td  class='tableIndex0Td'>"+expressStr+"</td><td>"+operateStr+"</td>"+
	             	//"<td>条件值：<input name=\"conValue\" maxlength='32' value=\"\"/></td>"+
	             	"<td>"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tjz_mh")+"<input name=\"conValue\" maxlength='32' value=\"\"/></td>"+
	             	"<td><a onclick=\"delTable(0)\"><img src=\"<%=skin %>/images/x.png\"/></a></td></tr></thead></table>";
	             	"</tr></thead></table>";
	            $('#detail_Info').append(tableStr);  
	         }
	     }
	    function addTable(){
	         var tableSize=$('#tableSize').val();
	         tableSize=tableSize-0+1;
	         var tableStr="<table id='tableIndex"+tableSize+"'><thead><tr>"+
	             	"<td  class='tableIndexTd'>"+expressStr+"</td><td>"+operateStr+"</td>"+
	             	//"<td>条件值：<input name=\"conValue\" maxlength='32' value=\"\"/></td>"+
	             	"<td>"+getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_tjz_mh")+"<input name=\"conValue\" maxlength='32' value=\"\"/></td>"+
	             	"<td><a onclick=\"delTable("+tableSize+")\"><img src=\"<%=skin %>/images/x.png\"/></a></td></tr></thead></table>";
	   	     $('#detail_Info').append(tableStr);  
	  	   	 $('#tableSize').val(tableSize);
	      }
	     function submitForm(){
	        //if (confirm("确定提交吗？"))
	        if (confirm(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_qdtjm")))
	        {
	        	 $('#proform').attr("action",$('#proform').attr("action")+"&lgcorpcode="+$("#lgcorpcode").val());
	            $('#proform').submit();
	        }
	     }
			function show(){
			<%  String result=(String)request.getAttribute("result");
				if(result!=null && "true" == result){%>
				//alert("操作成功！");
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czcg"));
				$("#previous").trigger("click");
			<%}else if(result!=null && "false" == result){%>
				//alert("操作失败！");	
				alert(getJsLocaleMessage("znyq","znyq_ywgl_sxywgl_czsb"));	
		    <%  }%>
		    delTable(-1);
			}
	    </script>
    
	</body>
</html>
