<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.entity.biztype.LfBusManager"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
    String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    String findResult= (String)request.getAttribute("findresult");
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    List<LfBusManager> busList = (List<LfBusManager>)request.getAttribute("busList");
    String result = request.getAttribute("result")==null?"":(String)request.getAttribute("result");

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 4.01 Transitional//EN">
<html>
<head>
    <title>importTemplateDetails.html</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="/common/common.jsp" %>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=commonPath%>/common/css/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="<%=commonPath%>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>" />
    <link rel="stylesheet" href="<%=skin %>/newjqueryui.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
    <link href="<%=commonPath%>/common/css/reading.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=skin %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <link href="<%=skin %>/table.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css" />
    <%if(StaticValue.ZH_HK.equals(langName)){%>
    <link rel="stylesheet" type="text/css" href="<%=commonPath%>/common/css/frame_zh_HK.css?V=<%=StaticValue.getJspImpVersion()%>"/>
    <link rel="stylesheet" type="text/css" href="<%=skin %>/table_zh_HK.css?V=<%=StaticValue.getJspImpVersion() %>"/>
    <%}%>

    <style type="text/css">
        .smt_smsTaskRecord div#tooltip {
            position:absolute;
            z-index:1000;
            max-width:435px;
            _width:expression(this.scrollWidth > 435 ? "435px" : "auto");
            width:auto; background:#A8CFF6;
            border:#FEFFD4 solid 1px;
            text-align:left; padding:6px;
        }
    </style>
</head>

<body id="toExport">
<div id="container" class="container">
    <!-- 内容开始 -->
    <div id="rContent" class="rContent">
        <input type="hidden" name="path" id="path" value="<%=path %>">
        <form id="pageForm" name="pageForm" action="ssm_dxzsBigFileExport.htm?method=uploadFile" method="post" enctype="multipart/form-data">
            <div id="condition">
                <table>
                    <tbody>
                    <tr>
                        <td width="30%" style="text-align:right; word-break:break-all;">文件名称：</td>
                        <td>
                            <input id="creFileName" name="creFileName" type="text" maxlength="30" >
                        </td>
                    </tr>
                    <tr>
                        <td width="30%" style="text-align:right; word-break:break-all;">业务类型：</td>
                        <td>
                            <select name="busId" id="busId" style="width: 202px;" isinput="false">
                                <%
                                    if(busList!=null&&busList.size()>0){
                                        for(LfBusManager bus:busList){
                                %>
                                <option  value='<%=bus.getBusId()%>' selected="selected"><%=bus.getBusName()%>(<%=bus.getBusCode()%>)</option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="30%" style="text-align:right; word-break:break-all;">文件：</td>
                        <td style="text-align:center;">
                            <!--
                            <input id="numFile" name="numFile" type="file" >
                             -->
                            <label id="fileBind" for="numFile1"><div style="background-color: #00a0e9;color:white;width:70px;height:20px;line-height: 20px" class=" ">选择文件</div></label>
                            <div id="filesdiv" class=" ">
                                <input id='numFile1' name='numFile1'  type=file onchange="addFiles()" class="numfileclass" style='display:none;'/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" colspan="2">
                            <div>
                                <table id="vss1" >
                                    <div id="vss">
                                    </div>
                                    <div id="vssSum">
                                        <tr  style='background-color:#ffffff' id='trsum'>
                                            <td width="500px;"  style='border-left:0;border-right:0' align='left' valign='middle' >文件总大小:</td>
                                            <td width='30%' style='border-left:0;border-right:0' align='left' id="sumsize" valign='middle'>0</td>
                                            <td width='30%' style='border-left:0;border-right:0;cursor: pointer' align='left' name='Kind' valign='middle'><a onclick="deleteSum('trsum')">全部删除</a></td>
                                        </tr>
                                    </div>
                                </table>
                            </div>
                        </td>

                    </tr>
                    <tr>
                        <td colspan="2">注意：</td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:left;  word-break:break-all;">
                            1、上传文件总大小小于1000M，有效号码数小于5000万;
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:left;  word-break:break-all;">
                            2、可上传多个文件，支持txt、zip、rar、xls、xlsx、csv、et格式，请优先使用zip文件格式;
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:left;  word-break:break-all;">
                            3、手机号码格式不正确，在上传时将由EMP平台过滤，发送提交预览时不再过滤;
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:left;  word-break:break-all;">
                            4、号码在黑名单内的，在上传时将由EMP平台过滤，发送提交预览时不再过滤;
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:left;  word-break:break-all;">
                            5、已经提交发送过的超大文件，不支持二次发送，需要重新上传。
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:left;  word-break:break-all;" >
                            <input type="hidden" id = "upResult" style="color: red" value = "0"/>
                        </td>
                    </tr>
                    <div style="display: none;" id="allfilename" ></div>
                    </tbody>
                </table>
            </div>
        </form>
        <!-- foot开始 -->
        <div class="bottom">
            <div id="bottom_right">
                <div id="bottom_left"></div>
                <div id="bottom_main">
                </div>
            </div>
        </div>
    </div>
    <div class="clear"></div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/dxzs_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/dxzs/samesms/js/ssm_bigFileExport.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript">
        $(document).ready(function(){
                var findresult = "<%=findResult%>";
                if(findresult == "-1") {
                    alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_77'));
                    return;
                }
                $("#title").live("keyup blur",function(){
                    var value=$(this).val();
                    if(value != filterString(value)){
                        $(this).val(filterString(value));
                    }
                });

                var result = "<%=result%>";
                var path = $("#path", parent.documen).val();
                
                var actionPath = $("#htmName", window.parent.document).val();
                if (result != ""){
                    if(result=="fileSuccess") {
                    	alert("文件提交成功");
                    }else if(result=="noBus") {
                        alert("业务类型不正确");
                    }else if(result=="uploadFileFail") {
                        alert("文件上传失败");
                    } else if (result == "noFile") {
                        alert("未选择文件");
                    }else{
                        alert(result);
                    }
                    parent.location.href=path+"/"+actionPath+"?method=find";
                    parent.page_complete();
                }
            }
        );

        var fileCount = 1;
        var sumSize = 0;
        $("#vssSum").hide();
        function addFiles() {
            var pathValue = $("#numFile" + fileCount).val();
            var index = pathValue.lastIndexOf("\\");
            var name = pathValue.substring(index + 1);
            var fileType = name.substring(name.lastIndexOf(".")+1).toLowerCase();
            //判断文件是否已经上传
            var flag = false;
            $(".inputFileName").each(function () {
                if(name === $(this).attr("title")){
                    alert("文件《"+ name + "》已经上传，请勿重复上传。");
                    flag = true;
                }
            });
            if (fileType!==""&&fileType !== "txt" && fileType !== "zip" && fileType !== "xls" && fileType !== "xlsx" && fileType !== "et" && fileType !== "rar" && fileType !== "csv") {
                alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_88'));
                flag = true;
            }
            if(flag){
                $("#numFile" + fileCount).val("");
                return
            }
            //记录全名
            var wholeFileName = name;

            if (name.length > 12) {
                name = name.substring(0, 12) + "....";
            }
            var fileSize = $("#numFile" + fileCount)[0].files[0].size;
            var size = bytesToSize(fileSize);
            sumSize = sumSize + fileSize;
            $("#upResult").val(sumSize);
            if (true) {
                if ($("#tr" + fileCount).length == 0) {
                    $("#vss").prepend("<tr  style='background-color:#ffffff;display: table;width: 100%;' id='tr" + fileCount + "'>" +
                        "<td width='500px' class='inputFileName' title='"+ wholeFileName +"'  style='border-left:0;border-right:0' align='left' valign='middle' >"+name+"</td>" +
                        "<td width='30%'  style='border-left:0;border-right:0' align='left' name='FName' valign='middle'>" + size + "</td>" +
                        "<td  width='30%' style='border-left:0;border-right:0' align='left' name='Kind' valign='middle'>" +
                        "<a style='cursor: pointer' onclick=ddll('tr" + fileCount + "')>删除</a></td>" +
                        "</tr>");
                    $("#numFile" + fileCount).css("display", "none");
                    fileCount++;
                    var ss = $("#filesdiv").html();
                    var ss1 = "<input type='file' id='numFile" + fileCount + "' name='numFile" + fileCount + "' value='' onchange='addFiles();' class='x-numfileclass'";
                    var style = " style='height:23px;width:100px'";
                    ss1 += style + "/>";
                    $("#filesdiv").prepend(ss1);
                    $("#fileBind").attr("for","numFile"+fileCount);
                    $("#allfilename").append(pathValue + ";");
                }
            }
            if(fileCount>1){
                $("#vssSum").show();
                $("#sumsize").html(bytesToSize(sumSize));
            }
        }
    </script>
</body>
</html>
