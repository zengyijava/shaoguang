<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.rms.entity.LfDfadvanced" %>
<%@ page import="java.util.List" %>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.montnets.emp.common.constant.StaticValue" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@page import="com.montnets.emp.common.constant.ViewParams"%>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple"%>
<%
    String path = request.getContextPath();
    String context = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = request.getRequestURI().substring(0,context.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    //皮肤
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    //选择模板
    LfTemplate lfTemplate =  request.getAttribute("lfTemplate") == null ? new LfTemplate():(LfTemplate)request.getAttribute("lfTemplate");
    //任务ID
    String taskId = request.getAttribute("taskId").toString();
    //sp账号
    @SuppressWarnings("unchecked")
    List<String> spUserList = (List<String>)request.getAttribute("spUserList");
    //默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
    //业务类型
    @SuppressWarnings("unchecked")
    List<LfBusManager> busList = (List<LfBusManager>) request.getAttribute("busList");
    @ SuppressWarnings("unchecked")
    Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
    //是否通过快捷方式跳转
    String isShortCut = request.getAttribute("isShortCut") == null ? "": request.getAttribute("isShortCut").toString();
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("rmsSameMms");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
    String tmIdSend = String.valueOf(request.getAttribute("tmIdSend"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@include file="/common/common.jsp"%>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>富信平台发送页面</title>
    <link rel="stylesheet" href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" />
    <link rel="stylesheet" href="<%=commonPath %>/common/widget/zTreeStyle/zTreeStyle.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" />
    <link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery-ui.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet" href="<%=commonPath %>/rms/samemms/css/uploadFile.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
    <link rel="stylesheet" href="<%=commonPath %>/rms/samemms/css/preview.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
    <link rel="stylesheet" href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
    <link rel="stylesheet" href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
    <link rel="stylesheet" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
    <link rel="stylesheet" href="<%=commonPath%>/rms/samemms/css/send.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
</head>
<body class="rms_sameMms">
    <%-- fx-fxsend-wrap --%>
    <div id="reminder" style="display: none;">
        <div id="withArrow" style="display: none;">
            <div id="reminder_arrow"></div>
            <div id="reminder_div">
                <table id="diffInfo_V1" style="display: none;">
                    <tr>
                        <td colspan="2"><emp:message key="rms_fxapp_fxsend_fmtasimg" defVal="文件格式如图:" fileName="rms"/></td>
                    </tr>
                    <tr>
                        <td valign="top" align="left"><emp:message key="rms_fxapp_fxsend_txtfmt" defVal="txt格式：" fileName="rms"/></td>
                        <td valign="top"><div id="txtStyle"></div></td>
                    </tr>
                    <tr>
                        <td valign="top" align="left" width="80px;"><emp:message key="rms_fxapp_fxsend_exlfmt" defVal="Excel格式：" fileName="rms"/></td>
                        <td valign="top"><div id="xlsStyle"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="left">
                            <span class="reminder_span"><emp:message key="rms_fxapp_fxsend_note" defVal="注意：" fileName="rms"/></span><br/>
                            <emp:message key="rms_fxapp_fxsend_reminder1" defVal="1.txt格式分隔符号是英文半角“,”；" fileName="rms"/><br />
                            <emp:message key="rms_fxapp_fxsend_reminder2" defVal="2.文件需小于100M，有效号码需少于5000000；" fileName="rms"/><br />
                            <emp:message key="rms_fxapp_fxsend_reminder3" defVal="3.文件仅支持txt、zip、rar、xls、xlsx、csv、et格式。" fileName="rms"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <%--V2.0模板的格式提示--%>
        <div id="sameInfo" style="display: none;">
            <img id="sameInfoImg" src="<%=commonPath%>/rms/samemms/img/sameInfo_<%=langName%>.png">
        </div>
        <div id="diffInfo" style="display: none;">
            <img id="diffInfoImg" src="<%=commonPath%>/rms/samemms/img/diffInfo_<%=langName%>.png">
        </div>
    </div>
    <input type="hidden" value="<%=tmIdSend%>" id="tmIdSend" name="tmIdSend"/>
    <%--选择富信模板DIV--%>
    <div class="fx-fxsend-wrap">
        <input type="hidden" id="pathUrl" value="<%=path%>" />
        <input type="hidden" id="langName" value="<%=langName%>" />
        <input type="hidden" value="<%=commonPath%>" id="commonPath"/>
        <%--<input type="hidden" value="<%=tmIdSend%>" id="tmIdSend"/>--%>
        <%-- fxsend-crumbs --%>
  		<%--<%--当前位置  --%>
  		<%=ViewParams.getPosition(langName,menuCode) %>
  		<%--<%--当前位置结束  --%>
        <%--<%-- /fxsend-crumbs --%>
        <%--<%-- fx-send-container --%>
        <div class="fx-send-container fxsend-clear">
            <form id="form2" name="form2" action="" method="post" enctype="multipart/form-data" target="hidden_iframe">
                <div id="sameSendInfo">
                    <%--用于数据回显--%>
                    <input type="hidden" id="rightSelectedUserOption" value=""/>
                    <%--新增单个手机号码的号码集合  --%>
                    <input type="hidden" id="phoneStr" name="phoneStr" value=""/>
                    <%--批量输入手机号码 的号码集合  --%>
                    <input type="hidden" id="inputphone" name="inputphone" value=""/>
                    <%--员工机构IDS --%>
                    <input type="hidden" id="empDepIds"  name="empDepIds" value=""/>
                    <%--客户机构IDS  --%>
                    <input type="hidden" id="cliDepIds"  name="cliDepIds" value=""/>
                    <%--群组机构IDS  --%>
                    <input type="hidden" id="groupIds"  name="groupIds" value=""/>
                    <%--员工IDS  --%>
                    <input type="hidden" id="empIds"  name="empIds" value=""/>
                    <%--客户IDS  --%>
                    <input type="hidden" id="cliIds"  name="cliIds" value=""/>
                    <%--外部人员IDS --%>
                    <input type="hidden" id="malIds"  name="malIds" value=""/>
                    <%--选择对象中，选择用户时的号码串 --%>
                    <input type="hidden" id="userMoblieStr"  name="userMoblieStr" value=""/>
                    <%--表示是否有选择对象的那一行  1表示有  2表示没有 --%>
                    <input type="hidden" id="havOne"  name="havOne" value="2"/>
                </div>
                <%--表示是否通过快捷方式进入--%>
                <input type="hidden" id="isShortCut"  name="isShortCut" value="<%=isShortCut%>"/>
                <%--富信发送任务ID--%>
                <input type="hidden" id="taskId" name="taskId" value="<%=taskId%>"/>
                <%--富信发送成功的任务ID--%>
                <input type="hidden" id="oldTaskId" name="oldTaskId" value=""/>
                <%--富信预览后的号码文件--%>
                <input type="hidden" id="upNumberPhoneUrl" name="upNumberPhoneUrl" value=""/>
                <%--结果文件URL--%>
                <input type="hidden" id="resultUrl" name="resultUrl" value=""/>
                <%--存储常用数据--%>
                <div style="display:none" id="hiddenValueDiv"></div>
                <%--相同内容上传号码文件的文件名称集合--%>
                <div style="display: none;" id="allSameFileName" ></div>
                <%--不同内容上传号码文件的文件名称集合--%>
                <div style="display: none;" id="allDiffFileName" ></div>
                <input type="hidden" style="display: none;" id="allFileNames" name="allFileNames" value="">
                <%--皮肤类型--%>
                <input type="hidden" id="skinType" name="skinType" value="<%=skin%>" />
                <%--<%-- SP账号计费类型--%>
                <input type="hidden" id="feeFlag" name="feeFlag" value=""/>
                <%--内容时效--%>
                <input type="hidden" id="validtm" name="validtm" value=""/>
                <%--<%-- SP账号计费类型 --%>
                <input type="hidden" value="" id="feeFlag" name="feeFlag"/>
                <%--<%-- container-left --%>
                <div class="container-left fxsend-fl">
                    <%--<%-- container-li --%>
                    <div class="container-li fxsend-clear">
                        <%if(!"true".equals(isShortCut)){%>
                        <p class="li-desc fxsend-fl"><emp:message key="rms_fxapp_fxsend_fxcontent" defVal="富信内容：" fileName="rms"/></p>
                        <div class="content-select gray-btn fxsend-fl" onclick="chooseMeditorTemplate()"><emp:message key="rms_fxapp_fxsend_fxchoose" defVal="选择富信" fileName="rms"/></div>
                        <%}%>
                        <%--富信模板文件相关div--%>
                        <div id="tempInfo">
                            <%--模板名字--%>
                            <input type="hidden" id="tempName" name="tempName" value="<%=lfTemplate.getTmName() == null || "".equals(lfTemplate.getTmName()) ? "":lfTemplate.getTmName()%>"/>
                            <%--模板自增ID--%>
                            <input type="hidden" id="tmId" name="tmId" value="<%=lfTemplate.getTmid() == null ? "":lfTemplate.getTmid()%>"/>
                            <%--模板版本--%>
                            <input type="hidden" id="tempVer" name="tempVer" value="<%=lfTemplate.getVer() == null ? "":lfTemplate.getVer()%>"/>
                            <%--模板ID--%>
                            <input type="hidden" id="tempId" name="tempId" value="<%=lfTemplate.getSptemplid() == null ? "":lfTemplate.getSptemplid()%>"/>
                            <%--模板路径--%>
                            <input type="hidden" id="tempUrl" name="tempUrl" value="<%=lfTemplate.getTmMsg() == null || "".equals(lfTemplate.getTmMsg()) ? "":lfTemplate.getTmMsg()%>"/>
                            <%--计费档位--%>
                            <input type="hidden" name="tempDegree" id="tempDegree" value="<%=lfTemplate.getDegree() == null ? "":lfTemplate.getDegree()%>"/>
                            <%--容量大小--%>
                            <input type="hidden" name="tempSize" id="tempSize" value="<%=lfTemplate.getDegreeSize() == null ? "":lfTemplate.getDegreeSize()%>"/>
                            <%--参数个数--%>
                            <input type="hidden" name="paramCount" id="paramCount" value="<%=lfTemplate.getParamcnt() == null ? "":lfTemplate.getParamcnt()%>"/>
                            <%--模板参数类型 0代表相同 1代表不同--%>
                            <input type="hidden" name="tempType" id="tempType" value="<%=lfTemplate.getDsflag() == null ? 0L:lfTemplate.getDsflag()%>"/>
                            <%--模板类型 11富媒体、12卡片和13富文本 14短信--%>
                            <input type="hidden" name="templateType" id="templateType" value="<%=lfTemplate.getTmpType() == null ? 0L:lfTemplate.getTmpType()%>"/>
                        </div>
                    </div>
                    <%--<%-- /container-li --%>
                    <%--<%-- container-li --%>
                    <div class="container-li fxsend-clear">
                        <p class="li-desc line-height40 fxsend-fl"><emp:message key="rms_fxapp_fxsend_sendtopic" defVal="发送主题：" fileName="rms"/></p>
                        <input id="tempInputName" class="full-input fxsend-fl graytext" type="text" value="<emp:message key="rms_fxapp_fxsend_hint1" defVal="不作为富信内容发送" fileName="rms"/>" maxlength="20" value="<%=lfTemplate.getTmName() == null || "".equals(lfTemplate.getTmName()) ? "":lfTemplate.getTmName()%>">
                    </div>
                    <%--<%-- /container-li --%>
                    <%--<%-- container-li --%>
                    <div class="container-li fxsend-clear" style="margin-bottom: 0;">
                        <p class="li-desc fxsend-fl"><emp:message key="rms_fxapp_fxsend_sendnumber" defVal="发送号码：" fileName="rms"/></p>
                        <div class="send-number-cont fxsend-fl">
                            <div class="choose-list fxsend-clear" id="choose-list" style="display: none;">
                                <div class="choose-btn fxsend-fl a-upload" id="fileUploadInput">
                                    <i class="choose-btn-icon file-icon"></i>
                                    <span class="choose-btn-desc" id="fileInput">
                                        <input type="file" name="uploadFile1" id="uploadFile1" value='quose' maxlength="11" onchange="addFilesNoModel();"/>
                                        <emp:message key="rms_fxapp_fxsend_importfile" defVal="导入文件" fileName="rms"/>
                                    </span>
                                </div>
                                <div class="choose-btn fxsend-fl" onclick="showSelectPerson()" id="choosePerson">
                                    <i class="choose-btn-icon user-icon"></i>
                                    <span class="choose-btn-desc"><emp:message key="rms_fxapp_fxsend_xzry" defVal="选择人员" fileName="rms"/></span>
                                </div>
                                <div class="choose-btn fxsend-fl" onclick="bulkImport()" id="bulkInput">
                                    <i class="choose-btn-icon edit-icon"></i>
                                    <span class="choose-btn-desc"><emp:message key="rms_fxapp_fxsend_plsr" defVal="批量输入" fileName="rms"/></span>
                                </div>
                                <div class="choose-input-cont fxsend-fr" id="phoneInput">
                                    <input onfocus="changeInputCss()" onblur="lostInputCss()" class="choose-input lostFocus" type="text" placeholder="<emp:message key="rms_fxapp_fxsend_srsjhm" defVal="请输入手机号码" fileName="rms"/>" maxlength="21"
                                           onkeyup="controlPhoneInput(this)"
                                           onpaste="return !clipboardData.getData('text').match(/\D/)"
                                           name="tph" id="tph">
                                    <input class="add-btn green-btn" type="button" value="<emp:message key="rms_fxapp_myscene_add" defVal="添 加" fileName="rms"/>" onclick="addphone()">
                                </div>
                            </div>
                            <ul class="number-list" id="infomaTable">
                                <%--第一次进入的提示语--%>
                                <li id="firstInInfo"><emp:message key="rms_fxapp_fxsend_hint2" defVal="请您选择富信内容，再输入或上传手机号码。" fileName="rms"/></li>
                            </ul>
                            <div class="format-hint" id="downlinks"><emp:message key="rms_fxapp_fxsend_hint3" defVal="格式提示" fileName="rms"/></div>
                            <div class="downExcel" id="downExcel" onclick="downExcel()" style="display: none;"><emp:message key="rms_fxapp_fxsend_downloadfe" defVal="下载号码文件示例" fileName="rms"/></div>
                        </div>
                    </div>
                    <%--<%-- /container-li --%>
                    <%--<%-- container-li --%>
                    <%--<div class="container-li fxsend-clear" onmouseover="showInfoMsg()" onmouseout="hideInfoMsg()">--%>
                        <%--<p class="li-desc fxsend-fl"><emp:message key="rms_fxapp_fxsend_cnttimely" defVal="内容时效性：" fileName="rms"/></p>--%>
                        <%--<div class="fxsend-fl">--%>
                            <%--<input id="validHourNum" class="input-w100 fxsend-fl" type="text" placeholder="48" onblur="validHour(this.value);" defaultVal="48">--%>
                            <%--<span class="input-desc fxsend-fl"><emp:message key="rms_fxapp_fxsend_hour" defVal="小时" fileName="rms"/></span>--%>
                            <%--&nbsp;&nbsp;--%>
                            <%--<span id="InfoMsg"><emp:message key="rms_fxapp_fxsend_hint4" defVal="超出有效时间，手机用户将看不到信息内容" fileName="rms"/></span>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<%-- /container-li --%>
                    <%--<%-- container-li --%>
                    <div class="container-li fxsend-clear">
                        <p class="li-desc fxsend-fl"><emp:message key="rms_fxapp_fxsend_sendtime" defVal="发送时间：" fileName="rms"/></p>
                        <div class="fxsend-fl">
                            <div class="check-li fxsend-fl">
                                <input name="sendType" value="0" id="sendType" type="radio" checked="checked" onclick="$('#time2').hide()">
                                <label for="sendType"><emp:message key="rms_fxapp_fxsend_sendim" defVal="立即发送" fileName="rms"/></label>
                            </div>
                            <div class="check-li fxsend-fl">
                                <input name="sendType" value="1" id="sendType1" type="radio" onclick="$('#time2').show()">
                                <label for="sendType1"><emp:message key="rms_fxapp_fxsend_sendontime" defVal="定时发送" fileName="rms"/></label>
                                <label id="time2" style="display:none;">
                                    <input type="text" class="Wdate div_bd" readonly="readonly"
                                       onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false,enableInputMask:false})"
                                           id="sendtime" name="sendtime" value="" style="margin-left: 10px;margin-top: 8px;line-height: 20px;">
                                </label>
                            </div>
                        </div>
                    </div>
                    <%--<%-- /container-li --%>
                    <%--<%-- container-li --%>
                    <%--高级设置--%>
                    <div class="container-li fxsend-clear">
                        <p class="li-desc fxsend-fl"><emp:message key="rms_fxapp_fxsend_ssetting1" defVal="高级设置：" fileName="rms"/></p>
                        <div class="fxsend-fl">
                            <div class="table-hd" id="advancedSetting">
                                <span><b><emp:message key="rms_fxapp_fxsend_ssetting2" defVal="高级设置" fileName="rms"/></b></span>
                                <i class="pull-icon send_fold" id="foldIcon"></i>
                            </div>
                            <div class="table-cont" id="advancedSettingContent" style="display: none;">
                                <div id="selfSelect">
                                    <div class="cont-li fxsend-clear">
                                        <p class="cont-li-desc fxsend-fl"><emp:message key="rms_fxapp_fxsend_bstype" defVal="业务类型：" fileName="rms"/></p>
                                        <select id="busCode" name="busCode" class="cont-li-select fxsend-fl" isInput="false" >
                                                <%
                                                    if (busList != null && busList.size() > 0) {
                                                        String busCode = lfDfadvanced != null?lfDfadvanced.getBuscode():"";
                                                        for(LfBusManager busManager:busList){
                                                %>
                                                <option value="<%=busManager.getBusCode()%>"
                                                        <%=busCode != null && !"".equals(busCode) && busCode.equals(busManager.getBusCode())?"selected":"" %>>
                                                    <%=busManager.getBusName().replace("<","&lt;").replace(">","&gt;") + "("+ busManager.getBusCode() +")"%>
                                                </option>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </select>
                                    </div>
                                    <div class="cont-li fxsend-clear">
                                        <p class="cont-li-desc fxsend-fl"><emp:message key="rms_fxapp_fxsend_spact" defVal="SP账号：" fileName="rms"/></p>
                                        <select id="mmsUser" name="mmsUser" class="cont-li-select fxsend-fl">
                                            <%
                                                if(spUserList != null && spUserList.size() > 0){
                                                    String spUserId = lfDfadvanced != null?lfDfadvanced.getSpuserid():"";
                                                    for (String spUser : spUserList) {
                                            %>
                                            <option value="<%=spUser%>"
                                                    <%=spUserId != null && !"".equals(spUserId) && spUserId.equals(spUser) ? "selected" : "" %>>
                                                <%=spUser%>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                                <div class="cont-li fxsend-clear">
                                    <p class="li-desc fxsend-fl"><emp:message key="rms_fxapp_fxsend_cnttimely" defVal="内容时效性：" fileName="rms"/></p>
                                    <input id="validHourNum" class="input-w100 fxsend-fl" type="text" placeholder="48" onblur="validHour(this.value);" defaultVal="48">
                                    <span class="input-desc fxsend-fl"><emp:message key="rms_fxapp_fxsend_hour" defVal="小时" fileName="rms"/></span>
                                </div>
                                <div class="cont-li fxsend-clear" id="checkRepeat">
                                    <p class="cont-li-desc fxsend-fl"><emp:message key="rms_fxapp_fxsend_chgrep" defVal="重号过滤：" fileName="rms"/></p>
                                    <div class="fxsend-fl">
                                        <div class="check-li fxsend-fl">
                                            <input id="check-number1" name="checkRepeat" type="radio" value="1" <%=lfDfadvanced == null || (lfDfadvanced.getRepeatfilter() != null && lfDfadvanced.getRepeatfilter() == 1) ? "checked":""%>>
                                            <label for="check-number1"><emp:message key="rms_fxapp_fxsend_yes" defVal="是" fileName="rms"/></label>
                                        </div>
                                        <div class="check-li fxsend-fl">
                                            <input id="check-number2" name="checkRepeat" type="radio" value="0" <%=lfDfadvanced != null && lfDfadvanced.getRepeatfilter() != null && lfDfadvanced.getRepeatfilter() == 0 ? "checked":""%>>
                                            <label for="check-number2"><emp:message key="rms_fxapp_fxsend_no" defVal="否" fileName="rms"/></label>
                                        </div>
                                    </div>
                                </div>
                                <a href="javaScript:setDefault()" class="table-link"><emp:message key="rms_fxapp_fxsend_hint5" defVal="选存为默认" fileName="rms"/></a>
                            </div>
                        </div>
                    </div>
                    <%--<%-- /container-li --%>
                    <%--<%-- container-li --%>
                    <div class="container-li fxsend-clear">
                        <p class="li-desc fxsend-fl"></p>
                        <div class="fxsend-fl">
                            <%--<input class="btn-w80 green-btn" type="button" value="暂存草稿">--%>
                            <input id="subSend" class="btn-w80 green-btn" type="button" onclick="preview()" value="<emp:message key="rms_fxapp_fxsend_submit" defVal="提 交" fileName="rms"/>">
                            <input class="btn-w80 gray-btn" type="button" onclick="reloadPage();" value="<emp:message key="rms_fxapp_fxsend_reset" defVal="重 置" fileName="rms"/>">
                        </div>
                    </div>
                    <%--<%-- /container-li --%>
                </div>
                <%--<%-- /container-left --%>
                <%--<%-- container-right --%>
                <div class="container-right fxsend-fl" id="tempview" style="display: none">
                    <p class="preview-title"><emp:message key="rms_fxapp_fxsend_sjdylxg" defVal="手机端预览效果：" fileName="rms"/></p>
                    <iframe id="tempview-iframe" name="tempview-iframe" frameborder="0" src="" width="100%" height="635px"></iframe>
                    <p class="preview-desc"><emp:message key="rms_fxapp_myscene_showtip1" defVal="手机预览仅供参考，具体以手机显示为准" fileName="rms"/></p>
                </div>
                <%--<%-- /container-right --%>
            </form>
        </div>
        <%--<%-- /fx-send-container --%>
    </div>
    <%--批量输入--%>
    <div id="tmplDiv" title="" style="display: none;">
        <iframe id="tempFrame" name="tempFrame" frameborder="0" src ="" ></iframe>
    </div>
    <%--<%-- 选择人员的弹出框 --%>
    <div id="bulkImport_box" title="<emp:message key="rms_fxapp_fxsend_plhmsr" defVal="批量号码输入" fileName="rms"/>" style="display:none;">
        <div class="imporInner div_bd">
            <textarea name="importArea" id="importArea" cols="30" rows="5" onkeyup="formatTelNum('#bNum')" onblur="formatTelNum('#bNum')"></textarea>
            <textarea name="importAreaTemp" id="importAreaTemp" style="display:none"></textarea>
        </div>
        <div class="bultMark">
            <span id="bultNum"><emp:message key="rms_fxapp_fxsend_currtotal" defVal="当前共" fileName="rms"/><font color='blue'><b id="bNum"></b></font><emp:message key="rms_fxapp_fxsend_number2000" defVal="/20000个号码" fileName="rms"/></span>
            <div style="height:25px"></div>
            <div class="numSplit"><emp:message key="rms_fxapp_fxsend_hint6" defVal="多个手机号码以逗号、空格、顿号、回车换行其中一种格式隔开" fileName="rms"/></div>
        </div>
        <div class="bultBtn">
            <input onclick="bulkInputConfirm()" id="telSub" class="btn-w80 green-btn" type="button" value="<emp:message key="rms_fxapp_fxsend_confirm" defVal="确认" fileName="rms"/>">
            <input onclick="bulkInputCancel()" id="telRet" class="btn-w80 gray-btn" type="button" value="<emp:message key="rms_fxapp_fxsend_back" defVal="返回" fileName="rms"/>">
        </div>
    </div>
    <%--发送格式提示--%>
    <div id="SelectPerson" title="<emp:message key="rms_fxapp_fxsend_choosetarget" defVal="选择发送对象" fileName="rms"/>" style="display: none;">
        <iframe id="flowFrame" name="flowFrame" src="" marginwidth="0" scrolling="no" frameborder="no"></iframe>
        <table class="SelectPersonTable">
            <tr>
                <td>
                    <div class="select-btn">
                        <input type="button" id="doOk" value="<emp:message key="rms_fxapp_fxsend_confirmb" defVal="确 定" fileName="rms"/>" class="btn-w80 green-btn" onclick="doOk()"/>
                        <input type="button" id="doNo" value="<emp:message key="rms_fxapp_fxsend_clear" defVal="清 空" fileName="rms"/>" class="btn-w80 gray-btn" onclick="doNo()"/>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <%--相同内容发送预览页面 --%>
    <div id="detail_Info_same" title="<emp:message key="rms_fxapp_fxsend_previewshow" defVal="预览效果" fileName="rms"/>" style="width:100%; height:100%">
        <div id="notsend_same">
            <p id="nosendReason_same"></p>
        </div>
        <div class="container-right fxsend-fl" id="tempview1" style="width:400px; height: 640px;display: none">
                    <iframe id="tempview1-iframe" name="tempview1-iframe" frameborder="0" src="" width="100%" height="100%"></iframe>
        </div>
        <div id="preview_same_Table">
            <table style="width: 200px;">
                <tr>
                    <td>
                        <span><emp:message key="rms_fxapp_fxsend_chargerange" defVal="计费档位：" fileName="rms"/></span>
                    </td>
                    <td>
                        <span id="tempFeeDegree"></span>&nbsp;<emp:message key="rms_fxapp_myscene_levelunit" defVal="档" fileName="rms"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <span><emp:message key="rms_fxapp_fxsend_validatenos" defVal="有效号码数：" fileName="rms"/></span>
                    </td>
                    <td><span id="effs"></span>个</td>
                </tr>
                <tr>
                    <td>
                        <span><emp:message key="rms_fxapp_fxsend_submitnos" defVal="提交号码数：" fileName="rms"/></span>
                    </td>
                    <td><span id="counts"></span><emp:message key="common_single" defVal="个" fileName="common"/></td>
                </tr>
                <tr>
                    <td><span><emp:message key="rms_fxapp_fxsend_invalidnos" defVal="无效号码数：" fileName="rms"/></span></td>
                    <td class="invalidPhone">
                        <span id="errerCount"></span><emp:message key="common_single" defVal="个" fileName="common"/>
                        <img id="arrow" src="<%=commonPath%>/rms/samemms/img/arrow_down.png">
                    </td>
                </tr>
            </table>
            <div class="errorDiv" style="display: none;">
                <div class="arrowUp" style="display: none;right: 15px;border-bottom-color: #eaeaea;"></div>
                <table id="moreSelect_same">
                    <tr>
                        <td align="left">
                           <emp:message key="rms_fxapp_fxsend_blacklistno" defVal="黑名单号码：" fileName="rms"/>
                        </td>
                        <td><span id="blacks"></span></td>
                    </tr>
                    <tr>
                        <td align="left">
                            <emp:message key="rms_fxapp_fxsend_duplicateno" defVal="重复号码：" fileName="rms"/>
                        </td>
                        <td><span id="sames"></span></td>
                    </tr>
                    <tr>
                        <td align="left">
                           <emp:message key="rms_fxapp_fxsend_invalidfmt" defVal="格式非法：" fileName="rms"/>
                        </td>
                        <td><span id="legers"></span></td>
                    </tr>
                </table>
                <div id="downlink_same">
                    <%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null ){ %>
                    <a href="javascript:uploadbadFiles('_same')" id="downlinkinfo"><emp:message key="rms_fxapp_fxsend_detaildownload" defVal="详情下载" fileName="rms"/></a>
                    <input type="hidden" id="badUrl_same" name="badUrl_same"  value=""/>
                    <%} %>
                </div>
            </div>
        </div>
        <div id="preview_same_foot">
            <input id="qingkong"  type="button" class="gray-btn btn-w80 left"  onclick="previewReset('same');" value="<emp:message key="rms_fxapp_fxsend_cancel" defVal="取 消" fileName="rms"/>"/>
            <input id="sendConfirm"  type="button" class="green-btn btn-w80 left" onclick="previewSubSame();" value="<emp:message key="rms_fxapp_fxsend_send" defVal="发 送" fileName="rms"/>"/>
        </div>
    </div>
    <%--不同内容发送预览页面 --%>
    <div id="detail_Info_diff" title="<emp:message key="rms_fxapp_fxsend_previewshow" defVal="预览效果" fileName="rms"/>" style="display: none;">
        <table id="infos">
            <tr >
                <td><emp:message key="rms_fxapp_fxsend_chargerange" defVal="计费档位：" fileName="rms"/><span id="tempFeeDegree_diff"></span>&nbsp;<emp:message key="rms_fxapp_myscene_levelunit" defVal="档" fileName="rms"/></td>
                <td colspan="2" style="text-align: center;">
                    <div id="notsend_diff">
                        <span id="nosendReason_diff">ffasfas</span>
                    </div>
                </td>
            </tr>
            <tr>
                <td><emp:message key="rms_fxapp_fxsend_validatenos" defVal="有效号码数：" fileName="rms"/><span id="effs_diff"></span></td>
                <td style="text-align: center;"><emp:message key="rms_fxapp_fxsend_submitnos" defVal="提交号码数：" fileName="rms"/><span id="counts_diff"></span></td>
                <td class="invalidPhone" id="invalidPhone_diff"><emp:message key="rms_fxapp_fxsend_invalidnos" defVal="无效号码数：" fileName="rms"/><span id="errerCount_diff"></span>
                    <img style="width: 15px;padding-left: 20px;" class="foldIcon" src="<%=commonPath%>/rms/samemms/img/down_icon.png"/>
                </td>
            </tr>
        </table>
        <div id="errorDiv_diff" style="display: none;">
            <div class="arrowUp" style="display: none;"></div>
            <table id="moreSelect_diff">
                <tr>
                    <td align="left"><emp:message key="rms_fxapp_fxsend_blacklistno" defVal="黑名单号码：" fileName="rms"/></td>
                    <td><span id="blacks_diff"></span></td>
                </tr>
                <tr>
                    <td align="left"><emp:message key="rms_fxapp_fxsend_duplicateno" defVal="重复号码：" fileName="rms"/></td>
                    <td><span id="sames_diff"></span></td>
                </tr>
                <tr>
                    <td align="left"><emp:message key="rms_fxapp_fxsend_invalidfmt" defVal="格式非法：" fileName="rms"/></td>
                    <td><span id="legers_diff"></span></td>
                </tr>
                <tr>
                    <td align="left"><emp:message key="rms_fxapp_fxsend_containkeyword" defVal="包含关键字：" fileName="rms"/></td>
                    <td><span id="keyW_diff"></span></td>
                </tr>
                <tr>
                    <td align="left"><emp:message key="rms_fxapp_fxsend_invalidfiles" defVal="无效文件数：" fileName="rms"/></td>
                    <td><span id="invalidFile"></span></td>
                </tr>
            </table>
            <div id="downlink">
                <%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null ){ %>
                <a href="javascript:uploadbadFiles('_diff')" id="downlinkinfo"><emp:message key="rms_fxapp_fxsend_detaildownload" defVal="详情下载" fileName="rms"/></a>
                <input type="hidden" id="badUrl_diff" name="badUrl_diff"  value=""/>
                <%} %>
            </div>
        </div>
        <div id="maindiv">
            <div id="ContentTitle">
                <table id="ContentTitleTable">
                    <tr>
                        <td width="20%"><emp:message key="rms_fxapp_fxsend_telphone" defVal="手机号码" fileName="rms"/></td>
                        <td width="60%"><emp:message key="rms_fxapp_fxsend_paraminfile" defVal="文件内参数" fileName="rms"/></td>
                        <td><emp:message key="rms_fxapp_fxsend_paramnumber" defVal="参数个数" fileName="rms"/></td>
                        <td><emp:message key="rms_fxapp_fxsend_fxcontent2" defVal="富信内容" fileName="rms"/></td>
                    </tr>
                </table>
            </div>
            <div class="rollBak content-wrapper">
                <table id="diffContent"></table>
            </div>
        </div>
        <div id="footer">
            <input id="btsend" class="green-btn btn-w80" type="button" onclick="previewSubDiff();" value="<emp:message key="rms_fxapp_fxsend_send" defVal="发 送" fileName="rms"/>"/>
            <input id="btcancel" class="gray-btn btn-w80" type="button" onclick="previewReset('diff');" value="<emp:message key="rms_fxapp_fxsend_cancel" defVal="取 消" fileName="rms"/>"/>
        </div>
    </div>
    <%--接收表单的iframe--%>
    <iframe name="hidden_iframe" id="hidden_iframe" style="display: none"></iframe>
    <div class="container-right fxsend-fl" id="myView" style="display: none;width: 100%;min-height: 0px;max-height: none;padding: 0;overflow: hidden;">
        <iframe id="tempview2-iframe" name="tempview2-iframe" frameborder="0" src="" width="100%" height="100%"></iframe>
    </div>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-c.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery-ui.min.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/common_<%=langName%>.js"></script>
	<script type="text/javascript" src="<%=commonPath%>/common/i18n/<%=langName%>/rms_<%=langName%>.js"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>"  ></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/rms/samemms/js/send.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/layer/layer.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
</body>
</html>
