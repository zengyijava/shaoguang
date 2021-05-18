<%@ page language="java" import="java.util.List" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.common.constant.OperatorsFee"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>
<%@ page import="com.montnets.emp.i18n.util.MessageUtils" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
@ SuppressWarnings("unchecked")
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
List<OperatorsFee> feeList = (List<OperatorsFee>)request.getAttribute("feeList");
String fResult = (String)request.getAttribute("fResult");
String ye = request.getParameter("ye");
Long yeCount = 0L;
if(ye != null && !"".equals(ye))
{
	yeCount = Long.valueOf(ye);
}
String strMmsBalance = request.getParameter("mmsBanalce");
Long mmsBalance = 0L;
if(strMmsBalance != null && !"".equals(strMmsBalance))
{
	mmsBalance = Long.valueOf(strMmsBalance);
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
  		<%@ include file="/common/common.jsp"%>
    <title><emp:message key="common_frame2_checkFee_1" defVal="运营商余额查看" fileName="common"></emp:message></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <style type="text/css">
    #lyout{
    	width:496px;
    	margin-left: 5px;
    }
    </style>
  	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
  	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
  	<%}%>
  </head>
  
  <body>
  <%if(!"allerror".equals(fResult)){ %>
  <div id="lyout">
  	<table id="content" >
<thead>
	<tr >
		<th>
			<emp:message key="common_serialNumber" defVal="序号" fileName="common"></emp:message>
		</th>
		<th>
			<emp:message key="common_frame2_checkFee_3" defVal="运营商账户" fileName="common"></emp:message>
		</th>
		<th>
			<emp:message key="common_InfoType" defVal="信息类型" fileName="common"></emp:message>
		</th>
		<th>
			<emp:message key="common_balance" defVal="余额" fileName="common"></emp:message>
		</th>
	<%--	<th>状态</th> --%>
	</tr>
	</thead>
	<tbody>
	<%if(feeList != null && feeList.size()>0 ) {
		int index = 0;
		for(OperatorsFee fee : feeList)
		{
			String feeuser = fee.getUserId();
			if(feeuser==null || "".equals(feeuser.trim()))
			{
				continue;
			}
			index ++;

	    if(fee.getMsType() == 1){
	        Long feeCount = 0L;
	        boolean isNull = false;
			if(fee.getCount() != null && !"".equals(fee.getCount()))
			{
				feeCount = Long.valueOf(fee.getCount());
			}else
			{
				isNull = true;
			}
	        %>
	        <tr <%=isNull?"":(feeCount<yeCount&&feeCount>=0? "style= \"color:red \"":"")%>>
			<td><%=index %></td>
			<td><%=fee.getUserId() %></td>
	        <td><emp:message key="common_SMS" defVal="短信" fileName="common"></emp:message></td>
	        <td><%=isNull? MessageUtils.extractMessage("common","common_frame2_checkFee_9",request):(feeCount<0?MessageUtils.extractMessage("common","common_frame2_checkFee_9",request)+"["+feeCount+"]":feeCount) %></td>
	        <%--<td><%= (feeCount<0?"错误编码["+feeCount+"]":(feeCount<yeCount?"运营商短信余额小于平台短信余额，请及时充值，以免影响发送！":"余额正常！"))%></td>
	         --%>
	         </tr>
	         <%
	    }else if(fee.getMsType() == 2){
	        String result = null;
	        String mmsFeeState = fee.getCount();
	        String trcolor = "";
	        try{
	            Long tempMmsBalance = Long.parseLong(fee.getCount());
	            if(tempMmsBalance < mmsBalance){
	               result = MessageUtils.extractMessage("common","common_frame2_checkFee_6",request);
	               trcolor = "style= \"color:red \"";
	            }else{
	               result = MessageUtils.extractMessage("common","common_frame2_checkFee_7",request);
	            }
	        }catch(Exception exception){
	        	EmpExecutionContext.error(exception,MessageUtils.extractMessage("common","common_frame2_checkFee_8",request));
	            result = fee.getCount();
	            if(result == null)
	            {
	            	mmsFeeState = MessageUtils.extractMessage("common","common_frame2_checkFee_9",request);
	            }else
	            {
	            	mmsFeeState = MessageUtils.extractMessage("common","common_frame2_checkFee_10",request)+result;
	            }
	        }
	     %>
	        <tr <%=trcolor%>>
	        <td><%=index %></td>
			<td><%=fee.getUserId() %></td>
	        <td><emp:message key="common_MMS" defVal="彩信" fileName="common"></emp:message></td>
	        <td><%=mmsFeeState %></td>
	        <%--<td><%=result %></td> --%>
	        </tr>
	     <%
	    }
	
	 %>
	<%}}
	if("smserror".equals(fResult))
	{
		out.print("<tr><td colspan='4' ><font color='red'>"+MessageUtils.extractMessage("common","common_frame2_checkFee_12",request)+"</font></td></tr>");
	}else if("mmserror".equals(fResult))
	{
		out.print("<tr><td colspan='4' ><font color='red'>"+MessageUtils.extractMessage("common","common_frame2_checkFee_13",request)+"</font></td></tr>");
	}
	%>
	</tbody>
	</table><br>
<%--	<font color="red">提醒：</font>当运营商账户显示为红色时，表示运营商账户余额少于前台计费账户余额，--%>
<%--	当您选用该运营商账户发送时，将导致部分信息发送失败，请及时联系运营商充值。--%>
<%--	<%}else{out.print("<center><font size='+1' color='red'>查询余额请求失败！</font></center>");} %>--%>
	  <font color="red"><emp:message key="common_MMS" defVal="彩信" fileName="common"></emp:message></font><emp:message key="common_frame2_checkFee_15" defVal="当运营商账户显示为红色时，表示运营商账户余额少于前台计费账户余额，
	当您发送时，将可能导致信息发送" fileName="common"></emp:message><font color="red"><emp:message key="common_fail" defVal="失败" fileName="common"></emp:message></font><emp:message key="common_frame2_checkFee_16" defVal="，请及时联系运营商" fileName="common"></emp:message><font color="red"><emp:message key="common_topUp" defVal="充值" fileName="common"></emp:message></font>。
	</div>
	<%}else{out.print("<center><font size='+1' color='red'>"+MessageUtils.extractMessage("common","common_frame2_checkFee_18",request)+"</font></center>");} %>
  <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
  </body>
</html>
