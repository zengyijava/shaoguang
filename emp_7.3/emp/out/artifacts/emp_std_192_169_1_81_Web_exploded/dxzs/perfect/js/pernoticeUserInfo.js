$(document).ready(function () {
    $("#left").click(function () {
        $("#treeFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
    });
    $("#left").dblclick(function () {
        //moveIn();
        newmoveIn();
        $("#treeFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
    });
    $("#right").dblclick(function () {
        //moveOut();
        newmoveOut();
    });
    hideBtn();
});

//这里是选择按钮后的操作    员工    客户  自定义  群组
function choiceTree() {
    var treeType = $("#choose_Type").val();
    var ipath = $("#iPathUrl").val();
    var path = $("#pathUrl").val();
    var inheritPath = $("#inheritPath").val();
    //1代表的是完美通知中的群组   2代表的是彩信发送中的群组
    var grouptype = $("#modulegrouptype").val();
    var lguserid = $(window.parent.document).find("#lguserid").val();
    //企业编码
    var corpCode = $(window.parent.document).find("#lgcorpcode").val();
    //树的DIV
    $("#etree").empty();
    //下放DIV
    $("#left").empty();
    //群组DIV
    $("#egroup").empty();
    //当前选择的名称
    $("#choiceName").val("");
    //当前选择的ID
    $("#choiceId").val("");
    //显示当前选择目录
    //$("#addrName").empty();
    //初始化索引指向第一页
    $("#pageIndex").val(1);
    var lguserid = $("#lguserid").val();
    //隐藏按钮
    hideBtn();
    if (treeType == "1") {	//员工
        $("#egroup").hide();
        $("#etree").show();
        $("#etree").html("<iframe id='sonFrame' style='width: 238px; height:237px;'    src='" + ipath + "/per_sendNotEmpTree.jsp?lguserid=" + lguserid + "' frameborder='0'></iframe>");
    } else if (treeType == "2") {	//客户
        $("#egroup").hide();
        $("#etree").show();
        //	$("#etree").html("<iframe id='sonFrame' style='width: 238px; height:237px;'   src='"+ipath+"/sta_sendMMSClientTree.jsp' frameborder='0'></iframe>");
    } else if (treeType == "3") {	//群组
        $("#etree").hide();
        $("#egroup").show();
        $.post(path + "/per_sendNot.htm", {
                method: "getStaMMSGroup",
                userId: lguserid,
                grouptype: grouptype
            }, function (GroupList) {
                $("#egroup").append(GroupList);
            }
        );

    }
}


//选择左边员工的操作
function moveIn() {
    if ($("#left option:selected").size() == 0) {
        return;
    }
    var employeeIds = $("#employeeIds").val();
    var malistIds = $("#malistIds").val();
    var moblieStrs = $("#moblieStrs").val();
    var chooseType = $("#choose_Type").val();
    var kongMoblieStr = "";
    var havExistStr = "";
    var manCount = parseInt($("#manCount").html());
    $("#left option:selected").each(function () {
        //成员GUID
        var guid = $(this).val();
        //这里判断成员类型  是4员工5客户6外部人员
        var isdep = $(this).attr("isdep");
        //手机号码
        var moblie = $(this).attr("moblie");
        //成员名称
        var name = $(this).text();
        //把机构去除掉
        if (chooseType == "1") {
            name = name.substring(0, name.lastIndexOf("["));
        }
        if (manCount >= 1000) {
            alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_195'));
            return;
        }
        //处理员工
        if (isdep == "4") {
            if (employeeIds == "" || employeeIds.length == 0) {
                manCount = manCount + 1;
                $("#right").append("<option value=\'" + guid + "\' isdep='4'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_196') + name + "</option>");
                $("#getChooseMan").append("<li dataval=\'" + guid + "\' isdep='4'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_196') + name + "</li>");
                employeeIds = employeeIds + guid + ",";
                moblieStrs = moblieStrs + moblie + ",";
            } else {
                if (employeeIds.indexOf(guid + ",") >= 0) {
                    havExistStr = havExistStr + name + "，";
                } else {
                    manCount = manCount + 1;
                    employeeIds = employeeIds + guid + ",";
                    moblieStrs = moblieStrs + moblie + ",";
                    $("#right").append("<option value=\'" + guid + "\' isdep='4'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_196') + name + "</option>");
                    $("#getChooseMan").append("<li dataval=\'" + guid + "\' isdep='4'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_196') + name + "</li>");
                }
            }
        } else if (isdep == "6") {
            //处理外部人员
            if (malistIds == "" || malistIds.length == 0) {
                manCount = manCount + 1;
                $("#right").append("<option value=\'" + guid + "\' isdep='6'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_197') + name + "</option>");
                $("#getChooseMan").append("<li dataval=\'" + guid + "\' isdep='6'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_197') + name + "</li>");
                malistIds = malistIds + guid + ",";
                moblieStrs = moblieStrs + moblie + ",";
            } else {
                if (malistIds.indexOf(guid + ",") >= 0) {
                    havExistStr = havExistStr + name + "，";
                } else {
                    manCount = manCount + 1;
                    malistIds = malistIds + guid + ",";
                    moblieStrs = moblieStrs + moblie + ",";
                    $("#right").append("<option value=\'" + guid + "\' isdep='6'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_197') + name + "</option>");
                    $("#getChooseMan").append("<li dataval=\'" + guid + "\' isdep='6'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_197') + name + "</li>");
                }
            }
        }
    });
    var msg = "";
    if (havExistStr != "" && havExistStr.length > 0) {
        msg = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_198');
    }
    if (msg.length > 0) {
        alert(msg);
    }
    $("#manCount").html(manCount);
    $("#employeeIds").val(employeeIds);
    $("#malistIds").val(malistIds);
    $("#moblieStrs").val(moblieStrs);
}

//移出右边的单个或者多个选择的对象
function moveOut() {
    //员工IDS
    var employeeIds = $("#employeeIds").val();
    //外部人员IDS
    var malistIds = $("#malistIds").val();
    //员工机构IDS
    var empDepIdsStrs = $("#empDepIdsStrs").val();
    //群组IDS
    var groupIdsStrs = $("#groupIdsStrs").val();
    //用户的 手机号码集合
    var moblieStrs = $("#moblieStrs").val();
    $("#right option:selected").each(function () {
        //成员GUID
        var id = $(this).val();
        //这里判断成员类型  是1员工机构  2客户机构 3群组 4员工5客户6外部人员
        var isdep = $(this).attr("isdep");
        var moblie = $(this).attr("moblie");
        var name = $(this).text();
        //1表示机构包含关系    2表示不包含关系
        var et = $(this).attr("et");
        if (isdep == "1") {
            //处理员工机构
            if (et == "2") {
                empDepIdsStrs = empDepIdsStrs.replace("e" + id + ",", "");
            } else if (et == "1") {
                empDepIdsStrs = empDepIdsStrs.replace(id + ",", "");
            }
            $("#manCount").html(parseInt($("#manCount").html()) - parseInt($(this).attr("mcount")));
        } else if (isdep == "3") {
            //处理外群组
            if (groupIdsStrs.indexOf(id + ",") >= 0) {
                groupIdsStrs = groupIdsStrs.replace(id + ",", "");
                $("#manCount").html(parseInt($("#manCount").html()) - parseInt($(this).attr("mcount")));
            }
        } else if (isdep == "4") {
            //处理员工
            if (employeeIds.indexOf(id + ",") >= 0) {
                employeeIds = employeeIds.replace(id + ",", "");
                moblieStrs = moblieStrs.replace(moblie + ",", "");
                $("#manCount").html(parseInt($("#manCount").html()) - 1);
            }
        } else if (isdep == "6") {
            //处理外部人员
            if (malistIds.indexOf(id + ",") >= 0) {
                malistIds = malistIds.replace(id + ",", "");
                moblieStrs = moblieStrs.replace(moblie + ",", "");
                $("#manCount").html(parseInt($("#manCount").html()) - 1);
            }
        }
        $(this).remove();
    });
    if ($('#getChooseMan li').size() > 0) {
        $('#getChooseMan>li').each(function () {
            if ($(this).hasClass('cur')) {
                //成员GUID
                var id = $(this).attr("value");
                //这里判断成员类型  是1员工机构  2客户机构 3群组 4员工5客户6外部人员
                var isdep = $(this).attr("isdep");
                var moblie = $(this).attr("moblie");
                //1表示机构包含关系    2表示不包含关系
                var et = $(this).attr("et");
                if (isdep == "1") {
                    //处理员工机构
                    if (et == "2") {
                        empDepIdsStrs = empDepIdsStrs.replace("e" + id + ",", "");
                    } else if (et == "1") {
                        empDepIdsStrs = empDepIdsStrs.replace(id + ",", "");
                    }
                    $("#manCount").html(parseInt($("#manCount").html()) - parseInt($(this).attr("mcount")));
                    //alert("机构ID2："+empDepIdsStrs);
                } else if (isdep == "3") {
                    //处理外群组
                    if (groupIdsStrs.indexOf(id + ",") >= 0) {
                        groupIdsStrs = groupIdsStrs.replace(id + ",", "");
                        $("#manCount").html(parseInt($("#manCount").html()) - parseInt($(this).attr("mcount")));
                    }
                } else if (isdep == "4") {
                    //处理员工
                    if (employeeIds.indexOf(id + ",") >= 0) {
                        employeeIds = employeeIds.replace(id + ",", "");
                        moblieStrs = moblieStrs.replace(moblie + ",", "");
                        $("#manCount").html(parseInt($("#manCount").html()) - 1);
                    }
                } else if (isdep == "6") {
                    //处理外部人员
                    if (malistIds.indexOf(id + ",") >= 0) {
                        malistIds = malistIds.replace(id + ",", "");
                        moblieStrs = moblieStrs.replace(moblie + ",", "");
                        $("#manCount").html(parseInt($("#manCount").html()) - 1);
                    }
                }
                $(this).remove();
            }
        })
    }
    $("#employeeIds").val(employeeIds);
    $("#malistIds").val(malistIds);
    $("#empDepIdsStrs").val(empDepIdsStrs);
    $("#groupIdsStrs").val(groupIdsStrs);
    $("#moblieStrs").val(moblieStrs);
}


//移出右边的单个或者多个选择的对象
function newmoveOut() {
    $("#right option:selected").each(function () {
        $(this).remove();
    });
    //员工机构IDS
    var empDepIdsStrs = $("#empDepIdsStrs").val();
    if ($('#getChooseMan li').size() > 0) {
        $('#getChooseMan>li').each(function () {
            if ($(this).hasClass('cur')) {
                //成员GUID
                var id = $(this).attr("dataval");
                //这里判断成员类型  是1员工机构  2客户机构 3群组 4员工5客户6外部人员
                var isdep = $(this).attr("isdep");
                //1表示机构包含关系    2表示不包含关系
                var et = $(this).attr("et");
                if (isdep == "1") {
                    if (et == "2") {
                        empDepIdsStrs = empDepIdsStrs.replace("e" + id + ",", "");
                    } else if (et == "1") {
                        empDepIdsStrs = empDepIdsStrs.replace(id + ",", "");
                    }
                    $("#manCount").html(parseInt($("#manCount").html()) - parseInt($(this).attr("mcount")));
                } else if (isdep == "3") {
                    $("#manCount").html(parseInt($("#manCount").html()) - parseInt($(this).attr("mcount")));
                } else if (isdep == "4") {
                    $("#manCount").html(parseInt($("#manCount").html()) - 1);
                } else if (isdep == "6") {
                    $("#manCount").html(parseInt($("#manCount").html()) - 1);
                }
                $(this).remove();
            }
        })
    }

    $("#empDepIdsStrs").val(empDepIdsStrs);
}


function newmoveIn() {
    if ($("#left option:selected").size() == 0) {
        return;
    }
    var chooseType = $("#choose_Type").val();
    var havExistStr = "";
    var manCount = parseInt($("#manCount").html());
    $("#left option:selected").each(function () {
        //成员GUID
        var guid = $(this).val();
        //这里判断成员类型  是4员工5客户6外部人员
        var isdep = $(this).attr("isdep");
        //成员名称
        var name = $(this).text();
        var moblie = $(this).attr("moblie");
        //把机构去除掉
        if (chooseType == "1") {
            name = name.substring(0, name.lastIndexOf("["));
        }
        if (manCount >= 1000) {
            alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_195'));
            return;
        }
        var isflag = "1";
        if ($("#right option").size() > 0) {
            $("#right option").each(function () {
                var type = $(this).attr("isdep");
                var val = $(this).val();
                if (isdep == type && val == guid) {
                    isflag = "2";
                    havExistStr = "common";
                }
            });
        }
        if (isflag == "1") {
            if (isdep == "4") {
                manCount = manCount + 1;
                $("#right").append("<option style='display: block !important;' value=\'" + guid + "\' isdep='4'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_196') + name + "</option>");
                $("#getChooseMan").append("<li dataval=\'" + guid + "\' isdep='4'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_196') + name + "</li>");
            } else if (isdep == "6") {
                manCount = manCount + 1;
                $("#right").append("<option style='display: block !important;' value=\'" + guid + "\' isdep='6'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_197') + name + "</option>");
                $("#getChooseMan").append("<li dataval=\'" + guid + "\' isdep='6'  et='' moblie='" + moblie + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_197') + name + "</li>");
            }
        }
    });
    var msg = "";
    if (havExistStr != "" && havExistStr.length > 0) {
        msg = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_198');
    }
    if (msg.length > 0) {
        alert(msg);
    }
    $("#manCount").html(manCount);

}

// 隐藏按钮
function hideBtn() {
    $("#prepage").css("visibility", "hidden");
    $("#nextpage").css("visibility", "hidden");
    $("#pagecode").empty();
    $("#userTotal").empty();
}

//选择员工  /客户 /群组
function choiceBtn() {
// 将选择按钮置灰
    $("#selectDep").attr("disabled", "disabled");
//选择的类型 
    var chooseType = $("#choose_Type").val();
//emp路径
    var path = $("#pathUrl").val();
//企业编码
    var corpCode = $(window.parent.document).find("#lgcorpcode").val();
//群组ID 	
    var groupIds = $("#groupIdsStrs").val();
//已选择人数 
    var manCount = parseInt($("#manCount").html());
    var maxNoticeCount = parseInt($("#noticeCount").val());
//群组 
    if (chooseType == "3") {
        // 没有群组人员的群组名称
        var noBodyGroup = "";
        // 已经选择了的群组名称
        var havExistGroup = "";
        var groupSize = $("#groupList option:selected").size();
        if (groupSize == 0) {
            alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_104'));
            $("#selectDep").attr("disabled", "");
            return;
        }
        $("#groupList option:selected").each(function () {
                // 群组VAL
                var groupVal = $(this).val();
                // 群组 名称
                var groupName = $(this).text();
                // 群组类型  员工还是客户群组  1 是员工群组  2是客户群组
                var groupType = $(this).attr("gtype");
                // 群组   0个人 1共享
                var groupShare = $(this).attr("sharetype");
                // 群组 数量
                var groupCount = parseInt($(this).attr("gcount"));
                if (groupCount == "0") {
                    noBodyGroup = "noBodyGroup";
                } else {
                    var a = groupCount + manCount;
                    var isflag = "1";
                    if ($("#right option").size() > 0) {
                        $("#right option").each(function () {
                            var type = $(this).attr("isdep");
                            var val = $(this).val();
                            if ("3" == type && val == groupVal) {
                                isflag = "2";
                                havExistGroup = "common";
                            }
                        });
                    }
                    if (groupCount > maxNoticeCount) {
                        alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_199') + maxNoticeCount + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "！");
                        $("#selectDep").attr("disabled", "");
                        return;
                    } else if (a > maxNoticeCount) {
                        alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_200') + maxNoticeCount + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "！");
                        $("#selectDep").attr("disabled", "");
                        return;
                    } else {
                        if (isflag == "1") {
                            groupName = groupName + " [" + groupCount + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "]";
                            $("#right").append("<option value=\'" + groupVal + "\' isdep='3'  et='' moblie='' mcount='" + $(this).attr("gcount") + "'>[" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_112') + "]" + groupName + "</option>");
                            $("#getChooseMan").append("<li dataval=\'" + groupVal + "\' isdep='3'  et='' moblie='' mcount='" + $(this).attr("gcount") + "'>[" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_112') + "]" + groupName + "</li>");
                            manCount += parseInt($(this).attr("gcount"));
                        }
                    }
                }
            }
        );
        $("#manCount").html(manCount);
        //$("#groupIdsStrs").val(groupIds);
        var msg = "";
        if (noBodyGroup != "" && noBodyGroup.length > 0) {
            msg = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_106');
        }
        if (havExistGroup != "" && havExistGroup.length > 0) {
            msg = getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_105');
        }
        if (msg.length > 0) {
            alert(msg);
        }
        $("#selectDep").attr("disabled", "");
        return;
    }
//选择的名称 
    var choiceName = $("#choiceName").val();
//选择的ID 
    var choiceId = $("#choiceId").val();
//处理员工/客户机构的选择操作
    if (choiceId == "") {
        if (chooseType == "1") {
            alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_107'));
            $("#selectDep").attr("disabled", "");
            return;
        } else if (chooseType == "2") {
            alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_107'));
            $("#selectDep").attr("disabled", "");
            return;
        }
    }
//员工机构ID 
    var empDepIds = $("#empDepIdsStrs").val();
//提示
    var viewDepNoBody = $("#viewDepNoBody").val();
    var viewDepIsExist = $("#viewDepIsExist").val();

//获取右边选择好的对象 			
    var rops = $("#right option");
    if (rops.length > 0) {
// 员工机构 	
        if (chooseType == "1") {
            //处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组
            for (var i = 0; i < rops.length; i = i + 1) {
                if (choiceId == rops.eq(i).attr("value") && rops.eq(i).attr("isDep") == 1) {
                    //alert(rops[i].text + " 记录已添加！");
                    alert(viewDepIsExist);
                    $("#selectDep").attr("disabled", "");
                    return;
                }
            }
        }
    }
//处理员工/客户机构是否被包含或者包含子机构
    if (chooseType == "1") {
        if (empDepIds != "" && empDepIds.length > 0) {
            //处理员工机构
            $.post(path + "/per_sendNot.htm", {
                method: "isEmpDepContained",
                depId: choiceId,
                empDepIds: empDepIds
            }, function (result) {
                if ("" == result) {
                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_194'));
                    $("#selectDep").attr("disabled", "");
                    return;
                } else if (result.indexOf("html") > 0) {
                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_201'));
                    $("#selectDep").attr("disabled", "");
                    window.location.href = window.location.href;
                    return;
                }
                if (result == "depExist") {
                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_109'));
                    $("#selectDep").attr("disabled", "");
                    return;
                }
                var ismut = 0;
                if (confirm(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_110'))) {
                    ismut = 1;
                }
                //处理该机构包含的子机构 操作, 并将选择好的子机构进行删除操作
                $.post(path + "/per_sendNot.htm",
                    {
                        method: "isDepContaineDeps",
                        depId: choiceId,
                        empDepIds: empDepIds,
                        ismut: ismut
                    }, function (result2) {
                        if (result2 == "nobody") {
                            //alert("该员工机构下没有员工！");
                            alert(viewDepNoBody);
                            $("#selectDep").attr("disabled", "");
                            return;
                        } else if (result2 == "errer") {
                            alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_194'));
                            $("#selectDep").attr("disabled", "");
                            return;
                        }
                        //机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组      et 1表示不包含    2表示包含
                        if (ismut == 0) {
                            var h = parseInt(result2);
                            var m = manCount + h;
                            if (h > maxNoticeCount) {
                                alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_202') + maxNoticeCount + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "！");
                                $("#selectDep").attr("disabled", "");
                                return;
                            } else if (m > maxNoticeCount) {
                                alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_200') + maxNoticeCount + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "！");
                                $("#selectDep").attr("disabled", "");
                                return;
                            } else {
                                $("#empDepIdsStrs").val(empDepIds + choiceId + ",");
                                $("#right").append("<option value=\'" + choiceId + "\' isdep='1'  et='1' moblie='' mcount='" + result2 + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_203') + choiceName + " [" + result2 + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "]</option>");
                                $("#getChooseMan").append("<li dataval=\'" + choiceId + "\' isdep='1'  et='1' moblie='' mcount='" + result2 + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_203') + choiceName + " [" + result2 + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "]</li>");
                                $("#selectDep").attr("disabled", "");
                                manCount += parseInt(result2);
                            }
                        }
                        //机构包含子机构但是右边没有其要删除的子机构的操作
                        else if (result2.indexOf("notContains") == 0) {
                            var b = parseInt(result2.substr(12));
                            var c = manCount + b;
                            if (b > maxNoticeCount) {
                                alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_202') + maxNoticeCount + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "！");
                                $("#selectDep").attr("disabled", "");
                                return;
                            } else if (c > maxNoticeCount) {
                                alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_200') + maxNoticeCount + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "！");
                                $("#selectDep").attr("disabled", "");
                                return;
                            } else {
                                $("#empDepIdsStrs").val(empDepIds + "e" + choiceId + ",")
                                $("#right").append("<option value='" + choiceId + "' isdep='1'  et='2' moblie='' mcount='" + result2.substr(12) + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_203') + choiceName + "(" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_86') + ")[" + result2.substr(12) + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "]</option>");
                                $("#getChooseMan").append("<li dataval='" + choiceId + "' isdep='1'  et='2' moblie='' mcount='" + result2.substr(12) + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_203') + choiceName + "(" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_86') + ")[" + result2.substr(12) + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "]</li>");
                                $("#selectDep").attr("disabled", "");
                                manCount += parseInt(result2.substr(12));
                            }
                        } else {
                            var strArr = result2.split(",");
                            var d = manCount + parseInt(strArr[0]);
                            var e = 0;
                            for (var i = 1; i < strArr.length; i = i + 1) {
                                var id = $.trim(strArr[i]);
                                var $testid = $("#right").find("option[isdep=1][value='" + id + "']");
                                e = d - $testid.attr("mcount");
                            }
                            if (parseInt(strArr[0]) > maxNoticeCount) {
                                alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_202') + maxNoticeCount + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "！");
                                $("#selectDep").attr("disabled", "");
                                return;
                            } else if (e > maxNoticeCount) {
                                alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_200') + maxNoticeCount + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "！");
                                $("#selectDep").attr("disabled", "");
                                return;
                            } else {
                                $("#empDepIdsStrs").val(empDepIds + "e" + choiceId + ",")
                                $("#right").append("<option value='" + choiceId + "' isdep='1'  et='2' moblie='' mcount='" + strArr[0] + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_203') + choiceName + "(" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_115') + ")[" + strArr[0] + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "]</option>");
                                $("#getChooseMan").append("<li dataval='" + choiceId + "' isdep='1'  et='2' moblie='' mcount='" + strArr[0] + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_203') + choiceName + "(" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_115') + ")[" + strArr[0] + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "]</li>");
                                $("#selectDep").attr("disabled", "");
                                manCount += parseInt(strArr[0]);
                                for (var i = 1; i < strArr.length; i = i + 1) {
                                    var containId = $.trim(strArr[i]);
                                    var $aaa = $("#right").find("option[isdep=1][value='" + containId + "']");
                                    var $bbb = $("#getChooseMan").find("li[isdep=1][dataval='" + containId + "']");
                                    // et1表示包含子机构关系,2表示不包含子机构
                                    var depidstr = $("#empDepIdsStrs").val();
                                    if ($aaa.attr("et") == 2) {
                                        $("#empDepIdsStrs").val(depidstr.replace("e" + $aaa.val() + ",", ""));
                                    } else if ($aaa.attr("et") == 1) {
                                        $("#empDepIdsStrs").val(depidstr.replace($aaa.val() + ",", ""));
                                    }
                                    manCount = manCount - $aaa.attr("mcount");
                                    $aaa.remove();
                                    $bbb.remove();
                                }
                            }
                        }
                        $("#manCount").html(manCount);
                    });
            });
        } else {
            //直接添加员工机构
            var ismut = 0;
            if (confirm(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_110'))) {
                ismut = 1;
            }
            $.post(path + "/per_sendNot.htm", {
                method: "getEmpDepCount",
                depId: choiceId,
                ismut: ismut
            }, function (result2) {
                if (result2 == "nobody" || result2 == "0") {
                    alert(viewDepNoBody);
                    $("#selectDep").attr("disabled", "");
                    return;
                } else if (result2 == "errer") {
                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_194'));
                    $("#selectDep").attr("disabled", "");
                    return;
                }
                var f = 0;
                var g = 0;
                if (ismut == 0) {
                    f = manCount + parseInt(result2);
                    g = parseInt(result2);
                } else {
                    var strArr = result2.split(",");
                    f = manCount + parseInt(strArr[0]);
                    g = parseInt(strArr[0]);
                }
                if (g > maxNoticeCount) {
                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_202') + maxNoticeCount + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "！");
                    $("#selectDep").attr("disabled", "");
                    return;
                } else if (f > maxNoticeCount) {
                    alert(getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_200') + maxNoticeCount + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "！");
                    $("#selectDep").attr("disabled", "");
                    return;
                } else {
                    //机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组   4代表员工 5代表客户 6代表外部人员   et 1表示不包含    2表示包含
                    if (ismut == 0) {
                        $("#empDepIdsStrs").val(choiceId + ",");
                        $("#right").append("<option value='" + choiceId + "' isdep='1'  et='1' moblie='' mcount='" + result2 + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_203') + choiceName + " [" + result2 + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "]</option>");
                        $("#getChooseMan").append("<li dataval='" + choiceId + "' isdep='1'  et='1' moblie='' mcount='" + result2 + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_203') + choiceName + " [" + result2 + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "]</li>");
                        $("#selectDep").attr("disabled", "");
                        manCount += parseInt(result2);
                    } else {
                        var strArr = result2.split(",");
                        $("#empDepIdsStrs").val("e" + choiceId + ",")
                        $("#right").append("<option value='" + choiceId + "' isdep='1'  et='2' moblie=''  mcount='" + strArr[0] + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_203') + choiceName + "(" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_86') + ")[" + result2 + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "]</option>");
                        $("#getChooseMan").append("<li dataval='" + choiceId + "' isdep='1'  et='2' moblie=''  mcount='" + strArr[0] + "'>" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_203') + choiceName + "(" + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_86') + ")[" + result2 + getJsLocaleMessage('dxzs', 'dxzs_ssend_alert_114') + "]</li>");
                        $("#selectDep").attr("disabled", "");
                        manCount += parseInt(strArr[0]);
                    }
                    $("#manCount").html(manCount);
                }
            });
        }
    }

}

//处理按纽上一页
function prePage() {
    var path = $("#pathUrl").val();
    var searchname = $("#searchname").val();
    searchname = "";
//企业编码
    var lgcorpcode = $("#lgcorpcode").val();
    var pageIndex = parseInt($("#pageIndex").val());
    $("#nextpage").attr("disabled", false);
//选择的类型 
    var chooseType = $("#choose_Type").val();
    var choiceId = $("#choiceId").val();
    var name = $("#choiceName").val();

    pageIndex = pageIndex - 1;
    $("#pageIndex").val(pageIndex);
    $("#left").empty();
//方法名  
    var methodName;
//用户 查询群组的时候 表识 1 员工群组  2客户群组  
    var type = 1;
    if (chooseType == "1") {
        methodName = "getEmployeeByDepId";
    } else if (chooseType == "2") {
        methodName = "getClientByDepId";
    } else if (chooseType == "3") {
        methodName = "getGroupUserByGroupId";
        type = $("#qztype").val();
    }
//处理员工/群组的上一页的操作 
//处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组
    $.post(path + "/per_sendNot.htm", {
        method: methodName,
        depId: choiceId,
        pageIndex: pageIndex,
        type: type,
        searchname: searchname,
        lgcorpcode: lgcorpcode
    }, function (result) {
        $("#pagecode").empty();
        if (result != "") {
            // 获取分页信息
            var arr = result.substring(0, result.indexOf("#")).split(",");
            //总页数
            var pageTotal = parseInt(arr[0]);
            //总记录数
            var pageRec = arr[1];
            // 添加记录
            $("#left").html(result.substring(result.indexOf("#") + 1));
            // 显示当前机构
            // 如果是只有一页记录的话
            $("#pagecode").html(pageIndex + "/" + pageTotal);
            // 当总页数等于当前页时 ，下一页置灰
            if (1 == pageIndex) {
                $("#prepage").attr("disabled", true);
                return;
            }
        }
    });
}

//处理按纽 下一页
function nextPage() {
    var path = $("#pathUrl").val();
    var searchname = $("#searchname").val();
    searchname = "";
//企业编码
    var lgcorpcode = $("#lgcorpcode").val();
    var pageIndex = parseInt($("#pageIndex").val());
    $("#prepage").attr("disabled", false);
//选择的类型 
    var chooseType = $("#choose_Type").val();
    var choiceId = $("#choiceId").val();
    var name = $("#choiceName").val();

    pageIndex = pageIndex + 1;
    $("#pageIndex").val(pageIndex);
    $("#left").empty();
//方法名  
    var methodName;
//用户 查询群组的时候 表识 1 员工群组  2客户群组  
    var type = 1;
    if (chooseType == "1") {
        methodName = "getEmployeeByDepId";
    } else if (chooseType == "2") {
        methodName = "getClientByDepId";
    } else if (chooseType == "3") {
        methodName = "getGroupUserByGroupId";
        type = $("#qztype").val();
    }
//处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组
    $.post(path + "/per_sendNot.htm", {
        method: methodName,
        depId: choiceId,
        pageIndex: pageIndex,
        type: type,
        searchname: searchname,
        lgcorpcode: lgcorpcode
    }, function (result) {
        $("#pagecode").empty();
        if (result != "") {
            // 获取分页信息
            var arr = result.substring(0, result.indexOf("#")).split(",");
            //总页数
            var pageTotal = parseInt(arr[0]);
            //总记录数
            var pageRec = arr[1];
            // 添加记录
            $("#left").html(result.substring(result.indexOf("#") + 1));
            // 显示当前机构
            // 如果是只有一页记录的话
            $("#pagecode").html(pageIndex + "/" + pageTotal);
            // 当总页数等于当前页时 ，下一页置灰
            if (pageTotal == pageIndex) {
                $("#nextpage").attr("disabled", true);
                return;
            }
        }
    });
}


function a() {
    var path = $("#pathUrl").val();
//查询名称
    var searchname = $("#searchname").val();

    if ($("#groupList option:selected").size() == 1) {
        $("#groupList option:selected").each(function () {
            $("#left").empty();
            $("#prepage").attr("disabled", false);
            $("#nextpage").attr("disabled", false);
            var pageIndex = 1;
            $("#pageIndex").val(pageIndex);
            // 当前群组ID
            var groupVal = $(this).val();
            var groupName = $(this).text();
            groupName = groupName.substring(0, groupName.lastIndexOf("["));
            // 当前群组类型  1是员工群组  2是客户群组
            var grouptype = $(this).attr("gtype");
            $("#qztype").val(grouptype);
            var gcount = parseInt($(this).attr("gcount"));
            $("#choiceId").val(groupVal);
            $("#choiceName").val(groupName);
            $("#pagecode").empty();
            if (gcount == 0) {
                $("#prepage").css("visibility", "hidden");
                $("#nextpage").css("visibility", "hidden");
                return;
            }
            $.post(path + "/per_sendNot.htm", {
                method: "getGroupUserByGroupId",
                depId: groupVal,
                pageIndex: pageIndex,
                type: grouptype,
                searchname: searchname
            }, function (result) {
                if (result != "") {
                    // 获取分页信息
                    var arr = result.substring(0, result.indexOf("#")).split(",");
                    //总页数
                    var pageTotal = parseInt(arr[0]);
                    //总记录数
                    var pageRec = arr[1];
                    // 添加记录
                    $("#left").html(result.substring(result.indexOf("#") + 1));
                    // 显示当前机构
                    // 显示该机构总人数
                    // 如果是只有一页记录的话
                    $("#pagecode").html(pageIndex + "/" + pageTotal);
                    $("#prepage").attr("disabled", true);
                    if (pageTotal == 1) {
                        $("#prepage").css("visibility", "hidden");
                        $("#nextpage").css("visibility", "hidden");
                        return;
                    } else if (pageTotal > 1) {
                        $("#prepage").css("visibility", "visible");
                        $("#nextpage").css("visibility", "visible");
                        return;
                    }
                } else {
                    $("#prepage").css("visibility", "hidden");
                    $("#nextpage").css("visibility", "hidden");
                }
            });

        });
    }

}

function treeLoseFocus() {
    $("#sonFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
    $("#groupList").val("");
}

function router() {
    if ($("#left").val() != null && $("#left").val() != "") {
//moveIn();
        newmoveIn();
    } else {
        choiceBtn();
    }
}


//名字查询
function searchbyname() {
// 选择的类型  1员工 2客服 3群组
    var chooseType = $("#choose_Type").val();
//选择的ID
//var choiceId = $("#choiceId").val();
//if(choiceId = null || choiceId ==""){
//	return;
//}
    if (chooseType == "1") {
        selectEmployee();
    } else if (chooseType == "3") {
        a();
    }
}

//处理员工通讯录的查询
function selectEmployee() {

    $("#left").empty();
    $("#prepage").attr("disabled", false);
    $("#nextpage").attr("disabled", false);
    var choiceId = $("#choiceId").val();
//每点机构树的时候，查询的是第一个索引页面的数据 
    var pageIndex = 1;
    $("#pageIndex").val(pageIndex);
//查询条件名称
    var searchname = $("#searchname").val();
    if (searchname == null || searchname == "") {
        $("#prepage").css("visibility", "hidden");
        $("#nextpage").css("visibility", "hidden");
        return;
    }
//企业编码
    var lgcorpcode = $("#lgcorpcode").val();//depId:choiceId,
    var pathUrl = $("#pathUrl").val();

    $.post(pathUrl + "/per_sendNot.htm", {
        method: "getEmployeeByDepId",
        pageIndex: pageIndex,
        searchname: searchname,
        lgcorpcode: lgcorpcode
    }, function (result) {
        $("#pagecode").empty();
        if (result != "") {
            //上一页按钮可见
            $("#prepage").css("visibility", "visible");
            //下一页按钮可见
            $("#nextpage").css("visibility", "visible");
            //获取分页信息
            var arr = result.substring(0, result.indexOf("#")).split(",");
            //总页数
            var pageTotal = parseInt(arr[0]);
            //总记录数
            var pageRec = arr[1];
            //添加记录
            $("#left").html(result.substring(result.indexOf("#") + 1));
            //显示当前机构
            //如果是只有一页记录的话
            $("#pagecode").html(pageIndex + "/" + pageTotal);
            $("#prepage").attr("disabled", true);
            if (pageTotal == 1) {
                $("#prepage").css("visibility", "hidden");
                $("#nextpage").css("visibility", "hidden");
                return;
            }
        } else {
            $("#prepage").css("visibility", "hidden");
            $("#nextpage").css("visibility", "hidden");
            return;
        }
    });
}