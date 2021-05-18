<%@ page language="java" import="com.montnets.emp.common.constant.ErrorCodeInfo" pageEncoding="UTF-8"%>
<%@page import="com.montnets.emp.entity.gateway.LfSpFee"%>
<%@page import="com.montnets.emp.i18n.util.MessageUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
String path = request.getContextPath();
String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/")); 
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
@ SuppressWarnings("unchecked")
List<LfSpFee> feeList = (List<LfSpFee>)request.getAttribute("feeList");
String fResult = (String)request.getAttribute("fResult");
Long mmsBalance = (Long)request.getAttribute("maxMmsConunt");
Long yeCount = (Long)request.getAttribute("maxcount");
String nodepfee =  (String)request.getAttribute("nodepfee");
String isShowYe =  (String)request.getAttribute("isShowYe");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
  <head>
    <%@ include file="/common/common.jsp"%>
    <title><emp:message key="common_frame2_checkFee_1" defVal="运营商余额查看" fileName="common"></emp:message></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <style type="text/css">
    #lyout{
    	width:596px;
    	margin-left: 5px;
    	padding:20px;
    	margin-bottom:15px;
    	display: block;
    }
    .tabyys {
    	display:block;
    	width:<%="zh_HK".equals(empLangName)?220:130%>px;
    	height:20px;
    	line-height:20px;
    	margin-left:20px;
    	margin-bottom:-10px;
    	position: relative;
    	background-color: #FFF;
    	font-weight: 800;
    	text-align: center;
    }
    </style>
  	<%if(StaticValue.ZH_HK.equals(empLangName)){%>
  	<link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
  	<%}%>
  </head>
  
  <body>
  <%if("1".equals(nodepfee) && "1".equals(isShowYe)) 
  {
	  out.print("<br/><center><font size='+1' class='zhu'>"+ MessageUtils.extractMessage("common","common_frame2_spaceFee_1",request)+"</font></center>");
  }else
  {
  %>
  <input style="margin: 10px 5px;" value="<emp:message key="common_frame2_spaceFee_2" defVal="重新获取余额" fileName="common"></emp:message>" class="btnClass4" onclick="reLoadFee()" type="button"/>
  <%if(!"1".equals(nodepfee)){ %>
  <div class="tabyys" style="width:94px;"><emp:message key="common_frame2_spaceFee_3" defVal="机构余额信息" fileName="common"></emp:message></div>
  <div id="lyout" class="div_bd" style="padding-top:20px" >
  	<div style="height:20px;line-height:20px;"><emp:message key="common_frame2_spaceFee_4" defVal="所属机构：" fileName="common"></emp:message><%=request.getAttribute("depName") %></div>
  	<div style="height:20px;line-height:20px;">
  		<div style="height:20px;line-height:20px;float:left;width:50%"><emp:message key="common_frame2_main_8" defVal="机构短信余额：" fileName="common"></emp:message><%=yeCount %><%="zh_HK".equals(empLangName)?"":MessageUtils.extractMessage("common","common_item",request)%></div>
  		<div style="height:20px;line-height:20px;float:right;width:50%"><emp:message key="common_frame2_main_9" defVal="机构彩信余额：" fileName="common"></emp:message><%=mmsBalance %><%="zh_HK".equals(empLangName)?"":MessageUtils.extractMessage("common","common_item",request)%></div>
  	</div>
  </div>
  <%} else
  {
	  yeCount = 0l;
	  mmsBalance = 0l;
  }%>
  <%if(!"1".equals(isShowYe)){ %>
  <div class="tabyys"><emp:message key="common_frame2_spaceFee_5" defVal="运营商账户余额信息" fileName="common"></emp:message></div>
  <div id="lyout" class="div_bd" >
  	<table id="content"  >
<thead>
	<tr >
	<th><emp:message key="common_serialNumber" defVal="序号" fileName="common"></emp:message></th>
		<th>
			<emp:message key="common_frame2_checkFee_3" defVal="运营商账户" fileName="common"></emp:message>
		</th>
		<th><emp:message key="common_InfoType" defVal="信息类型" fileName="common"></emp:message></th>
		<th><emp:message key="common_balance" defVal="余额" fileName="common"></emp:message></th>
	<%--	<th>状态</th> --%>
	</tr>
	</thead>
	<tbody>
	<%
		HashMap<String,String> errorInfoMap = new HashMap<String, String>();
		errorInfoMap.put("A40000",MessageUtils.extractMessage("common","common_ErrorCodeInfo_A40000",request));
		errorInfoMap.put("B20001",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20001",request));
		errorInfoMap.put("B20002",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20002",request));
		errorInfoMap.put("B20003",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20003",request));
		errorInfoMap.put("B20004",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20004",request));
		errorInfoMap.put("B20005",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20005",request));
		errorInfoMap.put("B20006",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20006",request));
		errorInfoMap.put("B20007",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20007",request));
		errorInfoMap.put("B20008",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20008",request));
		errorInfoMap.put("B20009",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20009",request));
		errorInfoMap.put("B20010",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20010",request));
		errorInfoMap.put("B20011",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20011",request));
		errorInfoMap.put("B20012",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20012",request));
		errorInfoMap.put("B20013",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20013",request));
		errorInfoMap.put("B20014",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20014",request));
		errorInfoMap.put("B20015",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20015",request));
		errorInfoMap.put("B20016",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20016",request));
		errorInfoMap.put("B20017",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20017",request));
		errorInfoMap.put("B20018",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20018",request));
		errorInfoMap.put("B20019",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20019",request));
		errorInfoMap.put("B20020",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20020",request));
		errorInfoMap.put("B20021",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20021",request));
		errorInfoMap.put("B20022",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20022",request));
		errorInfoMap.put("B20023",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20023",request));
		errorInfoMap.put("B20024",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20024",request));
		errorInfoMap.put("B20025",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20025",request));
		errorInfoMap.put("B20026",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20026",request));
		errorInfoMap.put("B20027",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20027",request));
		errorInfoMap.put("B20028",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20028",request));
		errorInfoMap.put("B20029",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20029",request));
		errorInfoMap.put("B20030",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20030",request));
		errorInfoMap.put("B20031",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20031",request));
		errorInfoMap.put("B20032",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20032",request));
		errorInfoMap.put("B20033",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20033",request));
		errorInfoMap.put("B20034",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20034",request));
		errorInfoMap.put("B20038",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20038",request));
		errorInfoMap.put("B20039",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20039",request));
		errorInfoMap.put("B20040",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20040",request));
		errorInfoMap.put("B20041",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20041",request));
		errorInfoMap.put("B20042",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20042",request));
		errorInfoMap.put("B20043",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20043",request));
		errorInfoMap.put("B20044",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20044",request));
		errorInfoMap.put("B20045",MessageUtils.extractMessage("common","common_ErrorCodeInfo_B20045",request));
		errorInfoMap.put("6001",MessageUtils.extractMessage("common","common_ErrorCodeInfo_6001",request));
		errorInfoMap.put("6002",MessageUtils.extractMessage("common","common_ErrorCodeInfo_6002",request));
		errorInfoMap.put("6003",MessageUtils.extractMessage("common","common_ErrorCodeInfo_6003",request));
		errorInfoMap.put("V10000",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10000",request));
		errorInfoMap.put("V10001",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10001",request));
		errorInfoMap.put("V10002",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10002",request));
		errorInfoMap.put("V10003",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10003",request));
		errorInfoMap.put("V10004",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10004",request));
		errorInfoMap.put("V10005",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10005",request));
		errorInfoMap.put("V10006",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10006",request));
		errorInfoMap.put("V10007",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10007",request));
		errorInfoMap.put("V10008",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10008",request));
		errorInfoMap.put("V10009",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10009",request));
		errorInfoMap.put("V10010",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10010",request));
		errorInfoMap.put("V10011",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10011",request));
		errorInfoMap.put("V10012",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10012",request));
		errorInfoMap.put("V10013",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10013",request));
		errorInfoMap.put("V10014",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10014",request));
		errorInfoMap.put("V10015",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10015",request));
		errorInfoMap.put("V10016",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10016",request));
		errorInfoMap.put("V10017",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10017",request));
		errorInfoMap.put("V10018",MessageUtils.extractMessage("common","common_ErrorCodeInfo_V10018",request));
	%>
	<%
	if(feeList != null  ) {
		int index = 0;
		for(;index < feeList.size();index++)
		{
			LfSpFee fee = feeList.get(index);
			String feeuser = fee.getSpUser();
			if(feeuser==null || "".equals(feeuser.trim()))
			{
				continue;
			}

	    if(fee.getAccountType() == 1){
	        boolean isNull = false;
	        String spResult = fee.getSpResult();
	        
	        int count = fee.getBalance();
	        long rmsCount=fee.getRmsBalance();
	        %>
	        <tr <%=!"ok".equals(spResult)?"":(count<yeCount&&count>=0? "style= \"color:red \"":"")%>>
			<td><%=index+1 %></td>
			<td><%=feeuser %></td>
			<%
				String errorInfo;
				String errorDes = errorInfoMap.get(spResult);
				if("".equals(errorDes) || errorDes == null){
					errorInfo = "["+spResult+"]";
				}else{
					errorInfo = errorDes+"["+spResult+"]";
				}
			%>
	        <td><emp:message key="common_SMS" defVal="短信/富信" fileName="common"></emp:message></td>
	        <td><%="".equals(spResult) || spResult == null? MessageUtils.extractMessage("common","common_frame2_spaceFee_6",request):(!"ok".equals(spResult)?MessageUtils.extractMessage("common","common_frame2_checkFee_10",request)+ errorInfo:(count+MessageUtils.extractMessage("common","common_text_3",request)+"/"+rmsCount+MessageUtils.extractMessage("common","common_text_3",request))) %></td>
	         </tr>
	         <%
	    }else if(fee.getAccountType() == 2){
	        String result = null;
	        String mmsFeeState = fee.getSpResult();
	        String trcolor = "";
	        if("ok".equals(mmsFeeState))
	        {
            	int tempMmsBalance = fee.getBalance();
	            if(tempMmsBalance < mmsBalance.intValue()){
	               trcolor = "style= \"color:red \"";
	            }
	            mmsFeeState = fee.getBalance().toString();
	        }
	        else
	        {
	            result = fee.getSpResult();
	            if("".equals(result) || result == null)
	            {
	            	/*"未获取余额，请点击重新获取余额按钮"*/
					mmsFeeState = MessageUtils.extractMessage("common","common_frame2_spaceFee_6",request);
	            }
	            else
	            {
					String errorDes = errorInfoMap.get(result);
					if("".equals(errorDes) || errorDes == null){
						mmsFeeState = MessageUtils.extractMessage("common","common_frame2_checkFee_10",request)+"["+result+"]";
					}else{
						mmsFeeState = MessageUtils.extractMessage("common","common_frame2_checkFee_10",request)+errorDes+"["+result+"]";
					}
	            }
	        }
	     %>
	        <tr <%=trcolor%>>
	        <td><%=index+1 %></td>
			<td><%=fee.getSpUser() %></td>
	        <td><emp:message key="common_MMS" defVal="彩信" fileName="common"></emp:message></td>
	        <td><%=mmsFeeState %></td>
	        </tr>
	     <%
	    }
	
	 %>
	<%}}else{out.print("<tr><td colspan='4' ><font color='red'>"+MessageUtils.extractMessage("common","common_frame2_spaceFee_7",request)+"</font></td></tr>");}
	if("smserror".equals(fResult))
	{
		out.print("<tr><td colspan='4' ><font color='red'>"+MessageUtils.extractMessage("common","common_frame2_checkFee_12",request)+"</font></td></tr>");
	}else if("mmserror".equals(fResult))
	{
		out.print("<tr><td colspan='4' ><font color='red'>"+MessageUtils.extractMessage("common","common_frame2_checkFee_13",request)+"</font></td></tr>");
	}
	%>
	</tbody>
	</table>
	<%if(!"1".equals(nodepfee)){ %>
	<br>
	  <font color="red"><emp:message key="common_MMS" defVal="彩信" fileName="common"></emp:message></font><emp:message key="common_frame2_checkFee_15" defVal="当运营商账户显示为红色时，表示运营商账户余额少于前台计费账户余额，当您发送时，将可能导致信息发送" fileName="common"></emp:message><font color="red"><emp:message key="common_fail" defVal="失败" fileName="common"></emp:message></font><emp:message key="common_frame2_checkFee_16" defVal="，请及时联系运营商" fileName="common"></emp:message><font color="red"><emp:message key="common_topUp" defVal="充值" fileName="common"></emp:message></font>。
	</div>
	<%}}} %>
	<script language="javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>" type="text/javascript"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
  </body>
</html>
