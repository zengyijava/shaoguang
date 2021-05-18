<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.montnets.emp.common.constant.ViewParams" %>
<%@ page import="com.montnets.emp.entity.biztype.LfBusManager" %>
<%@ page import="com.montnets.emp.entity.template.LfTemplate" %>
<%@ page import="com.montnets.emp.rms.entity.LfDfadvanced" %>
<%@ page import="com.montnets.emp.rms.servmodule.constant.ServerInof" %>
<%@ page import="com.montnets.emp.rms.vo.UserDataVO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
    String path = request.getContextPath();
    String context = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    String inheritPath = request.getRequestURI().substring(0,context.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0,inheritPath.lastIndexOf("/"));
    String iPath=request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
    //操作员Id
    String lguserid = request.getParameter("lguserid");
    //皮肤
    String skin = session.getAttribute("stlyeSkin")==null?"default":(String)session.getAttribute("stlyeSkin");
    //选择模板
    LfTemplate lfTemplate =  request.getAttribute("lfTemplate") == null ? new LfTemplate():(LfTemplate)request.getAttribute("lfTemplate");
    //任务ID
    String taskId = request.getAttribute("taskId").toString();
    //sp账号
    @SuppressWarnings("unchecked")
    List<UserDataVO> spUserList = (List<UserDataVO>)request.getAttribute("spUserList");
    //默认设置
    LfDfadvanced lfDfadvanced = (LfDfadvanced)request.getAttribute("lfDfadvanced");
    //业务类型
    @SuppressWarnings("unchecked")
    List<LfBusManager> busList = (List<LfBusManager>) request.getAttribute("busList");
    @ SuppressWarnings("unchecked")
    Map<String,String> btnMap=(Map<String,String>)session.getAttribute("btnMap");//按钮权限Map
    //服务器名称
    String serverName = ServerInof.getServerName();
    //是否通过快捷方式跳转
    String isShortCut = request.getAttribute("isShortCut") == null ? "": request.getAttribute("isShortCut").toString();
	@ SuppressWarnings("unchecked")
	Map<String,String> titleMap = (Map<String,String>)session.getAttribute("titleMap");
	String menuCode = titleMap.get("rmsSameMms");
	menuCode = menuCode==null?"0-0-0":menuCode;
	String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
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
    <link rel="stylesheet" href="<%=commonPath %>/common/widget/jqueryui/css/jquery.ui.all.css?V=<%=StaticValue.getJspImpVersion() %>">
    <link rel="stylesheet" href="<%=commonPath %>/rms/samemms/css/uploadFile.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
    <link rel="stylesheet" href="<%=commonPath %>/rms/samemms/css/preview.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css" >
    <link rel="stylesheet" href="<%=skin%>/table.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
    <link rel="stylesheet" href="<%=skin%>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
    <link rel="stylesheet" href="<%=commonPath%>/common/css/select.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css"/>
    <link rel="stylesheet" href="<%=commonPath%>/rms/samemms/css/send.css?V=<%=StaticValue.getJspImpVersion() %>" type="text/css">
</head>
<body class="rms_sameMms">
    <%-- fx-fxsend-wrap --%>
    <div class="fx-fxsend-wrap">
        <input type="hidden" id="pathUrl" value="<%=path%>" />
        <input type="hidden" value="<%=commonPath%>" id="commonPath"/>
        <%-- fxsend-crumbs --%>
  		<%--当前位置  --%>
  		<%=ViewParams.getPosition(langName,menuCode) %>
  		<%--当前位置结束  --%>
        <%-- /fxsend-crumbs --%>
        <%-- fx-send-container --%>
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
                <%-- SP账号计费类型--%>
                <input type="hidden" id="feeFlag" name="feeFlag" value=""/>
                <%--内容时效--%>
                <input type="hidden" id="validtm" name="validtm" value=""/>
                <%-- SP账号计费类型 --%>
                <input type="hidden" value="" id="feeFlag" name="feeFlag"/>
                <%-- container-left --%>
                <div class="container-left fxsend-fl">
                    <%-- container-li --%>
                    <div class="container-li fxsend-clear">
                        <%if(!"true".equals(isShortCut)){%>
                        <p class="li-desc fxsend-fl">富信内容：</p>
                        <div class="content-select gray-btn fxsend-fl" onclick="chooseRmsTemplate()">选择富信</div>
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
                            <%--模板类型 0代表相同 1代表不同--%>
                            <input type="hidden" name="tempType" id="tempType" value="<%=lfTemplate.getDsflag() == null ? 0L:lfTemplate.getDsflag()%>"/>
                        </div>
                    </div>
                    <%-- /container-li --%>
                    <%-- container-li --%>
                    <div class="container-li fxsend-clear">
                        <p class="li-desc line-height40 fxsend-fl">发送主题：</p>
                        <input id="tempInputName" class="full-input fxsend-fl" type="text" placeholder="不作为富信内容发送" maxlength="20" value="<%=lfTemplate.getTmName() == null || "".equals(lfTemplate.getTmName()) ? "":lfTemplate.getTmName()%>">
                    </div>
                    <%-- /container-li --%>
                    <%-- container-li --%>
                    <div class="container-li fxsend-clear" style="margin-bottom: 0;">
                        <p class="li-desc fxsend-fl">发送号码：</p>
                        <div class="send-number-cont fxsend-fl">
                            <div class="choose-list fxsend-clear" id="choose-list" style="display: none;">
                                <div class="choose-btn fxsend-fl a-upload" id="fileUploadInput">
                                    <i class="choose-btn-icon file-icon"></i>
                                    <span class="choose-btn-desc" id="fileInput">
                                        <input type="file" name="uploadFile1" id="uploadFile1" value='quose' maxlength="11" onchange="addFilesNoModel();"/>
                                        导入文件
                                    </span>
                                </div>
                                <div class="choose-btn fxsend-fl" onclick="showSelectPerson()" id="choosePerson">
                                    <i class="choose-btn-icon user-icon"></i>
                                    <span class="choose-btn-desc">选择人员</span>
                                </div>
                                <div class="choose-btn fxsend-fl" onclick="bulkImport()" id="bulkInput">
                                    <i class="choose-btn-icon edit-icon"></i>
                                    <span class="choose-btn-desc">批量输入</span>
                                </div>
                                <div class="choose-input-cont fxsend-fr" id="phoneInput">
                                    <input onfocus="changeInputCss()" onblur="lostInputCss()" class="choose-input lostFocus" type="text" placeholder="请输入手机号码" maxlength="21"
                                           onkeyup="controlPhoneInput(this)"
                                           onpaste="return !clipboardData.getData('text').match(/\D/)"
                                           name="tph" id="tph">
                                    <input class="add-btn green-btn" type="button" value="添 加" onclick="addphone()">
                                </div>
                            </div>
                            <ul class="number-list" id="infomaTable">
                                <%--第一次进入的提示语--%>
                                <li id="firstInInfo">请您选择富信内容，再输入或上传手机号码。</li>
                            </ul>
                            <div class="format-hint" id="downlinks">格式提示</div>
                            <div class="downExcel" id="downExcel" onclick="downExcel()" style="display: none;">下载号码文件示例</div>
                        </div>
                    </div>
                    <%-- /container-li --%>
                    <%-- container-li --%>
                    <div class="container-li fxsend-clear" onmouseover="showInfoMsg()" onmouseout="hideInfoMsg()">
                        <p class="li-desc fxsend-fl">内容时效性：</p>
                        <div class="fxsend-fl">
                            <input id="validHourNum" class="input-w100 fxsend-fl" type="text" placeholder="48" onblur="validHour(this.value);" defaultVal="48">
                            <span class="input-desc fxsend-fl">小时</span>
                            &nbsp;&nbsp;
                            <span id="InfoMsg">超出有效时间，手机用户将看不到信息内容</span>
                        </div>
                    </div>
                    <%-- /container-li --%>
                    <%-- container-li --%>
                    <div class="container-li fxsend-clear">
                        <p class="li-desc fxsend-fl">发送时间：</p>
                        <div class="fxsend-fl">
                            <div class="check-li fxsend-fl">
                                <input name="sendType" value="0" id="sendType" type="radio" checked="checked" onclick="$('#time2').hide()">
                                <label for="sendType">立即发送</label>
                            </div>
                            <div class="check-li fxsend-fl">
                                <input name="sendType" value="1" id="sendType1" type="radio" onclick="$('#time2').show()">
                                <label for="sendType1">定时发送</label>
                                <label id="time2" style="display:none;">
                                    <input type="text" class="Wdate div_bd" readonly="readonly"
                                       onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d %H:%m',isShowToday:false,enableInputMask:false})"
                                           id="sendtime" name="sendtime" value="" style="margin-left: 10px;margin-top: 8px;line-height: 20px;">
                                </label>
                            </div>
                        </div>
                    </div>
                    <%-- /container-li --%>
                    <%-- container-li --%>
                    <%--高级设置--%>
                    <div class="container-li fxsend-clear">
                        <p class="li-desc fxsend-fl">高级设置：</p>
                        <div class="fxsend-fl">
                            <div class="table-hd" id="advancedSetting">
                                <span>高级设置</span>
                                <i class="pull-icon send_fold" id="foldIcon"></i>
                            </div>
                            <div class="table-cont" id="advancedSettingContent" style="display: none;">
                                <div id="selfSelect">
                                    <div class="cont-li fxsend-clear">
                                        <p class="cont-li-desc fxsend-fl">业务类型：</p>
                                        <select id="busCode" name="busCode" class="cont-li-select fxsend-fl" isInput="false">
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
                                        <p class="cont-li-desc fxsend-fl">SP账号：</p>
                                        <select id="mmsUser" name="mmsUser" class="cont-li-select fxsend-fl">
                                            <%
                                                if(spUserList != null && spUserList.size() > 0){
                                                    String spUserId = lfDfadvanced != null?lfDfadvanced.getSpuserid():"";
                                                    for (UserDataVO spUser : spUserList) {
                                            %>
                                            <option value="<%=spUser.getUserId()%>"
                                                    <%=spUserId != null && !"".equals(spUserId) && spUserId.equals(spUser.getUserId()) ? "selected" : "" %>>
                                                <%=spUser.getUserId()%>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                                <div class="cont-li fxsend-clear" id="checkRepeat">
                                    <p class="cont-li-desc fxsend-fl">重号过滤：</p>
                                    <div class="fxsend-fl">
                                        <div class="check-li fxsend-fl">
                                            <input id="check-number1" name="checkRepeat" type="radio" value="1" checked>
                                            <label for="check-number1">是</label>
                                        </div>
                                        <div class="check-li fxsend-fl">
                                            <input id="check-number2" name="checkRepeat" type="radio" value="0">
                                            <label for="check-number2">否</label>
                                        </div>
                                    </div>
                                </div>
                                <a href="javaScript:setDefault()" class="table-link">选存为默认</a>
                            </div>
                        </div>
                    </div>
                    <%-- /container-li --%>
                    <%-- container-li --%>
                    <div class="container-li fxsend-clear">
                        <p class="li-desc fxsend-fl"></p>
                        <div class="fxsend-fl">
                            <%--<input class="btn-w80 green-btn" type="button" value="暂存草稿">--%>
                            <input id="subSend" class="btn-w80 green-btn" type="button" onclick="preview()" value="提 交">
                            <input class="btn-w80 gray-btn" type="button" onclick="reloadPage();" value="重 置">
                        </div>
                    </div>
                    <%-- /container-li --%>
                </div>
                <%-- /container-left --%>
                <%-- container-right --%>
                <div class="container-right fxsend-fl" id="tempview" style="display: none">
                    <p class="preview-title">手机端预览效果：</p>
                    <div class="preview-cont">
                        <div id="cust_preview_outer" class="preview_wrapper rollBak preview_f">
                            <div id="cust_preview"></div>
                        </div>
                    </div>
                    <p class="preview-desc">手机预览仅供参考，具体以手机显示为准</p>
                </div>
                <%-- /container-right --%>
            </form>
        </div>
        <%-- /fx-send-container --%>
    </div>
    <%--选择富信模板DIV--%>
    <div id="tmplDiv" title="">
        <iframe id="tempFrame" name="tempFrame" frameborder="0" src ="" ></iframe>
    </div>
    <%--批量输入--%>
    <div id="bulkImport_box" title="批量号码输入" style="display:none;">
        <div class="imporInner div_bd">
            <textarea name="importArea" id="importArea" cols="30" rows="5" onkeyup="formatTelNum('#bNum')" onblur="formatTelNum('#bNum')"></textarea>
            <textarea name="importAreaTemp" id="importAreaTemp" style="display:none"></textarea>
        </div>
        <div class="bultMark">
            <span id="bultNum">当前共<font color='blue'><b id="bNum"></b></font>/20000个号码</span>
            <div style="height:25px"></div>
            <div class="numSplit">多个手机号码以逗号、空格、顿号、回车换行其中一种格式隔开</div>
        </div>
        <div class="bultBtn">
            <input onclick="bulkInputConfirm()" id="telSub" class="btn-w80 green-btn" type="button" value="确认">
            <input onclick="bulkInputCancel()" id="telRet" class="btn-w80 gray-btn" type="button" value="返回">
        </div>
    </div>
    <%-- 选择人员的弹出框 --%>
    <div id="SelectPerson" title="选择发送对象" style="display: none;">
        <iframe id="flowFrame" name="flowFrame" src="" marginwidth="0" scrolling="no" frameborder="no"></iframe>
        <table class="SelectPersonTable">
            <tr>
                <td>
                    <div class="select-btn">
                        <input type="button" id="doOk" value="确 定" class="btn-w80 green-btn" onclick="doOk()"/>
                        <input type="button" id="doNo" value="清 空" class="btn-w80 gray-btn" onclick="doNo()"/>
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <%--发送格式提示--%>
    <div id="reminder" style="display: none;">
        <div id="withArrow" style="display: none;">
            <div id="reminder_arrow"></div>
            <div id="reminder_div">
                <table id="sameInfo" style="display: none;">
                    <tr>
                        <td colspan="2">文件格式如图:</td>
                    </tr>
                    <tr>
                        <td valign="top" align="left" >txt格式：</td>
                        <td valign="top">
                            <div id="same_txtStyle"></div>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" align="left" width="80px;">Excel格式：</td>
                        <td valign="top">
                            <div id="same_xlsStyle"></div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="left">
                            <span class="reminder_span">注意：</span><br/>
                            1.手机号码格式不正确，在上传时将由EMP平台过滤；<br/>
                            2.号码包含于黑名单内，在上传时将由EMP平台过滤；<br/>
                            3.文件需小于100M，有效号码需少于5000000；<br/>
                            4.文件仅支持txt、zip、rar、xls、xlsx、csv、et格式。
                        </td>
                    </tr>
                </table>
                <table id="diffInfo_V1" style="display: none;">
                    <tr>
                        <td colspan="2">文件格式如图:</td>
                    </tr>
                    <tr>
                        <td valign="top" align="left">txt格式：</td>
                        <td valign="top"><div id="txtStyle"></div></td>
                    </tr>
                    <tr>
                        <td valign="top" align="left" width="80px;">Excel 格式：</td>
                        <td valign="top"><div id="xlsStyle"></div></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="left">
                            <span class="reminder_span">注意：</span><br/>
                            1.txt格式分隔符号是英文半角“,”；<br />
                            2.文件需小于100M，有效号码需少于5000000；<br />
                            3.文件仅支持txt、zip、rar、xls、xlsx、csv、et格式。
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <%--V2.0模板的格式提示--%>
        <div id="diffInfo" style="display: none;">
            <img id="diffInfoImg" src="<%=commonPath%>/rms/samemms/img/diffInfo_<%=langName%>.png">
        </div>
    </div>
    <%--等待旋转--%>
    <div id="probar" style="display: none">
        <p style="padding-top: 8px;">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            处理中,请稍等.....
        </p>
        <div id="shows">
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <img style="padding-left: 25px;" src="<%=iPath%>/img/loader.gif"/>
        </div>
    </div>
    <%--相同内容发送预览页面 --%>
    <div id="detail_Info_same" title="预览效果">
        <div id="notsend_same">
            <p id="nosendReason_same"></p>
        </div>
        <div id="mobileDiv">
            <div class="preview_same rollBak">
                <div id="cust_preview_same"></div>
            </div>
        </div>
        <div id="preview_same_Table">
            <table style="width: 200px;">
                <tr>
                    <td>
                        <span>计费档位：</span>
                    </td>
                    <td>
                        <span id="tempFeeDegree"></span>&nbsp;档
                    </td>
                </tr>
                <tr>
                    <td>
                        <span>有效号码数：</span>
                    </td>
                    <td><span id="effs"></span>个</td>
                </tr>
                <tr>
                    <td>
                        <span>提交号码数：</span>
                    </td>
                    <td><span id="counts"></span>个</td>
                </tr>
                <tr>
                    <td><span>无效号码数：</span></td>
                    <td class="invalidPhone">
                        <span id="errerCount"></span>个
                        <img id="arrow" src="<%=commonPath%>/rms/samemms/img/arrow_down.png">
                    </td>
                </tr>
            </table>
            <div class="errorDiv" style="display: none;">
                <div class="arrowUp" style="display: none;right: 15px;border-bottom-color: #eaeaea;"></div>
                <table id="moreSelect_same">
                    <tr>
                        <td align="left">
                            黑名单号码：
                        </td>
                        <td><span id="blacks"></span></td>
                    </tr>
                    <tr>
                        <td align="left">
                            重复号码：
                        </td>
                        <td><span id="sames"></span></td>
                    </tr>
                    <tr>
                        <td align="left">
                            格式非法：
                        </td>
                        <td><span id="legers"></span></td>
                    </tr>
                </table>
                <div id="downlink_same">
                    <%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null ){ %>
                    <a href="javascript:uploadbadFiles('_same')" id="downlinkinfo">详情下载</a>
                    <input type="hidden" id="badUrl_same" name="badUrl_same"  value=""/>
                    <%} %>
                </div>
            </div>
        </div>
        <div id="preview_same_foot">
            <input id="qingkong"  type="button" class="gray-btn btn-w80 left"  onclick="previewReset('same');" value="取 消"/>
            <input id="sendConfirm"  type="button" class="green-btn btn-w80 left" onclick="previewSubSame();" value="发 送"/>
        </div>
    </div>
    <%--不同内容发送预览页面 --%>
    <div id="detail_Info_diff" title="预览效果" style="display: none;">
        <table id="infos">
            <tr >
                <td>计费档位：<span id="tempFeeDegree_diff"></span>&nbsp;档</td>
                <td colspan="2" style="text-align: center;">
                    <div id="notsend_diff">
                        <span id="nosendReason_diff">ffasfas</span>
                    </div>
                </td>
            </tr>
            <tr>
                <td>有效号码数：<span id="effs_diff"></span></td>
                <td style="text-align: center;">提交号码数：<span id="counts_diff"></span></td>
                <td class="invalidPhone" id="invalidPhone_diff">无效号码数：<span id="errerCount_diff"></span>
                    <img style="width: 15px;padding-left: 20px;" class="foldIcon" src="<%=commonPath%>/rms/samemms/img/down_icon.png"/>
                </td>
            </tr>
        </table>
        <div id="errorDiv_diff" style="display: none;">
            <div class="arrowUp" style="display: none;"></div>
            <table id="moreSelect_diff">
                <tr>
                    <td align="left">黑名单号码：</td>
                    <td><span id="blacks_diff"></span></td>
                </tr>
                <tr>
                    <td align="left">重复号码：</td>
                    <td><span id="sames_diff"></span></td>
                </tr>
                <tr>
                    <td align="left">格式非法：</td>
                    <td><span id="legers_diff"></span></td>
                </tr>
                <tr>
                    <td align="left">包含关键字：</td>
                    <td><span id="keyW_diff"></span></td>
                </tr>
                <tr>
                    <td align="left">无效文件数：</td>
                    <td><span id="invalidFile"></span></td>
                </tr>
            </table>
            <div id="downlink">
                <%if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null ){ %>
                <a href="javascript:uploadbadFiles('_diff')" id="downlinkinfo">详情下载</a>
                <input type="hidden" id="badUrl_diff" name="badUrl_diff"  value=""/>
                <%} %>
            </div>
        </div>
        <div id="maindiv">
            <div id="ContentTitle">
                <table id="ContentTitleTable">
                    <tr>
                        <td width="20%">手机号码</td>
                        <td width="60%">文件内参数</td>
                        <td>参数个数</td>
                        <td>富信内容</td>
                    </tr>
                </table>
            </div>
            <div class="rollBak content-wrapper">
                <table id="diffContent"></table>
            </div>
        </div>
        <div id="footer">
            <input id="btsend" class="green-btn btn-w80" type="button" onclick="previewSubDiff();" value="发 送"/>
            <input id="btcancel" class="gray-btn btn-w80" type="button" onclick="previewReset('diff');" value="取 消"/>
        </div>
    </div>
    <%--接收表单的iframe--%>
    <iframe name="hidden_iframe" id="hidden_iframe" style="display: none"></iframe>
    <%--不同内容发送预览弹出框--%>
    <div id="myView" style="display: none;" title="预览">
        <div id="phone_background">
            <div class="cust_preview_diff_outer rollBak">
                <div id="cust_diff_preview"></div>
            </div>
        </div>
    </div>
    <%-- /fx-fxsend-wrap --%>
    <script type="text/javascript" src="<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>" ></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.ztree-myjquery-b.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/myjquery-j.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/widget/jqueryui/jquery.bgiframe-myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/floating.js?V=<%=StaticValue.getJspImpVersion() %>"  ></script>
    <script type="text/javascript" src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/widget/datepicker/WdatePicker.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/rms/samemms/js/send.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
    <script type="text/javascript" src="<%=commonPath%>/common/js/jquery.selectnew.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
</body>
</html>
