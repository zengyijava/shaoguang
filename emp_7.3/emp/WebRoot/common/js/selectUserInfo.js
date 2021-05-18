//判断成员类型(isdep)  是 1员工机构  2客户机构 3群组 4员工 5客户 6外部人员 7签约用户 8签约套餐 9属性客户 10客户属性 11操作员 12操作员机构 13共享
var pathUrl = $("#pathUrl").val();
var corpCode = $("#corpCode").val();
var lguserid = $("#lguserid").val();
$(document).ready(function(){

    //IE下手动修改样式
    if(isIE()){
        $(".addName").css("height","24px");
        $(".addName").css("top","2px");
        $(".addPhone").css("height","24px");
        $(".addPhone").css("bottom","150px");
        $(".separate_icon").css("bottom","26px");
    }

    $("#selectAllMem").toggle(function () {
        $(this).attr("val","0");
        $(this).html(getJsLocaleMessage('common','common_cancel_selectall'));
    },function () {
        $(this).attr("val","1");
        $(this).html(getJsLocaleMessage('common','common_select_all'));
    });

	$("#searchName").focus(function () {
		$(this).removeClass("lostFocus");
		$(this).addClass("focus");
	});
	$("#searchName").blur(function () {
	   $(this).removeClass("focus");
	   $(this).addClass("lostFocus");
	});
	hideBtn();
		//<%=path%>/common/selectUserEmpTree.jsp
	var chooseType = $("#chooseType").val();
	var $typeList = $(chooseType.split(","));
	var flag = true;
	$typeList.each(function (index,element) {
		if(element === "1"){
			$("#choose_Type").append("<option value='1' id='deptTree'>"+getJsLocaleMessage('common','common_employee_contact')+"</option>");
			$("#sonFrame").attr("src",pathUrl + "/common/selectUserEmpTree.jsp");
			flag = false;
		}else if(element === "2"){
			$("#choose_Type").append("<option value='2' id='clientTree'>"+getJsLocaleMessage('common','common_customer_contact')+"</option>");
			//如果同时有员工通讯录与客户通讯录则显示员工通讯录
			if(flag){
				$("#sonFrame").attr("src",pathUrl + "/common/selectUserCliTree.jsp");
			}
		}else if(element === "3"){
			$("#choose_Type").append("<option value='3' id='groupTree'>"+getJsLocaleMessage('common','common_group')+"<!--群组--></option>");
		}else if(element === "4"){
			$("#choose_Type").append("<option value='4' id='groupTree'>"+getJsLocaleMessage('common','common_employee_group')+"<!--员工群组--></option>");
		}else if(element === "5"){
			$("#choose_Type").append("<option value='5' id='groupTree'>"+getJsLocaleMessage('common','common_customer_group')+"<!--客户群组--></option>");
		} else if(element === "6"){
			$("#choose_Type").append("<option value='6' id='clientFieldTree'>"+getJsLocaleMessage('common','common_customer_attr')+"<!--客户属性--></option>");
		}else if(element === "7"){
			$("#choose_Type").append("<option value='7' id='groupTree'>"+getJsLocaleMessage('common','common_sign_user')+"<!--签约用户--></option>");
		}else if(element === "8"){
			$("#choose_Type").append("<option value='8' id='operatorTree'>"+getJsLocaleMessage('common','common_operator_organize')+"<!--操作员机构--></option>");
			$("#sonFrame").attr("src",pathUrl + "/common/selectUserDepTree.jsp");
			var tmName = $("#tmName").val();
			var tmId = $("#tmId").val();
			if(tmName !== "" && tmName !== undefined && tmName !== "null"
				&& tmId !== "" && tmId !== undefined && tmId !== "null"){
				$("#tempInfo").css("display","block");
				$(".chooseBox").css("margin-top","15px");
                tmName = decodeURIComponent(tmName);
				$("#tempName").val(tmName);
                $("#tempId").val(tmId);
			}
			$("#searchName").attr("placeholder",$("#searchName").attr("placeholder").replace(getJsLocaleMessage('common','common_input_name')/*"请输入姓名"*/,getJsLocaleMessage('common','common_name_phone')));
		}else if(element === "9"){
            var groupName = $("#groupName").val();
            $("#curName").val(groupName);
		    //处理群组增加或修改
			$("#isGroupEdit").val("1");
            $("#choose_Type").append("<option value='9' id='importFile'>"+getJsLocaleMessage('common','common_file_import')+"<!--文件导入--></option>");
            $(".groupNameInput").css("display","block");
            $(".phoneAndNameInputWrapper").css("display","block");
            $(".chooseBox").css("margin-top","15px");
            $(".showchoiceDiv").css("height","275px");
            $(".showchoiceDiv").css("bottom","200px");
            $("#manCountOuter").html($("#manCountOuter").html().replace(getJsLocaleMessage('common','common_selected_total')/*"所选总人数"*/,getJsLocaleMessage('common','common_added_total')));
            if(groupName !== "" && groupName !== undefined && groupName !== "null"){
                $("#addGpName").val(groupName);
            }
        }
	});
	//数据回显
	//员工机构ids
	$("#empDepIdsStrs").val($(window.parent.document).find("#empDepIds").val());
	//客户机构ids
	$("#cliDepIdsStrs").val($(window.parent.document).find("#cliDepIds").val());
	//签约套餐Ids
	$("#taocanIds").val($(window.parent.document).find("#ydywGroupStr").val());
	//群组ids
	$("#groupIdsStrs").val($(window.parent.document).find("#groupIds").val());
	//客户ids
	$("#clientIds").val($(window.parent.document).find("#cliIds").val());
	//员工ids
	$("#employeeIds").val($(window.parent.document).find("#empIds").val());
	//自建ids
	$("#malistIds").val($(window.parent.document).find("#malIds").val());
	//签约用户Ids
	$("#signClientIds").val($(window.parent.document).find("#signClientIds").val());
	//客户属性Ids
	$("#cusFieldIds").val($(window.parent.document).find("#proIdStr").val());
	//客户属性值Ids
	$("#cusFieldValueIds").val($(window.parent.document).find("#proValueIdStr").val());
	//选择单个对象的手机号集合(签约用户除开)
	$("#signClientPhoneStr").val($(window.parent.document).find("#ydywPhoneStr").val());
	//签约用户的手机号集合
	$("#moblieStrs").val($(window.parent.document).find("#userMoblieStr").val());
    //群组新增或修改--群组自建
    $("#qzStr").val($(window.parent.document).find("#qzStr").val());
    //群组新增或修改--客户或员工
    $("#ygStr").val($(window.parent.document).find("#ygStr").val());
    //群组新增或修改--共享
    $("#gxStr").val($(window.parent.document).find("#gxStr").val());
    //群组新增或修改--群组加密后Id
    $("#curId").val($(window.parent.document).find("#curId").val());

	var selectedUserOption = $(window.parent.document).find("#rightSelectedUserOption").val();
	$("#right").html(selectedUserOption);
	if(selectedUserOption !== undefined){
		selectedUserOption = selectedUserOption.replace(/option/gi, "div").replace(/value/g, " onclick='changeChooseDiv(this)' class='chooseMan' dataval");
	}
	$("#getChooseMan").html(selectedUserOption);
    var manCount = $(window.parent.document).find("#choiceNum").html();
	$("#manCount").html(manCount === undefined || manCount === "null" || manCount == null ? 0 : manCount);
});
//这里是选择按钮后的操作    1.员工通讯录   2.客户通讯录  3.群组(员工+客户) 4.员工群组 5.客户群组 6.客户属性 7.签约用户 8.操作员机构 9.文件导入
function choiceTree(){
    var showUserName = getJsLocaleMessage('common','common_member_list') + "<a id='selectAllMem' val='1' onclick='selectAllMem(this)'>"+getJsLocaleMessage('common','common_select_all')+"<!--全选--></a>";
	var treeType = $("#choose_Type").val();
	var path = $("#pathUrl").val();
	//树的DIV
	$("#etree").empty();
	//下放DIV
	$("#left").empty();
	//当前选择的名称
	$("#choiceName").val("");
	//当前选择的ID
	$("#choiceId").val("");
	//初始化索引指向第一页
	$("#pageIndex").val(1);
	//隐藏按钮
	hideBtn();
	if(treeType === "1"){	//员工
        $("#showUserName").html(showUserName);
        $(".searchNameWrapper").show();
        $("#fileUploadInput").hide();
		$("#etree").show();
		$("#etree").html("<iframe id='sonFrame' src='"+path+"/common/selectUserEmpTree.jsp' frameborder='0'></iframe>");
	}else if(treeType === "2"){	//客户
        $("#showUserName").html(showUserName);
        $(".searchNameWrapper").show();
        $("#fileUploadInput").hide();
		$("#etree").show();
		$("#etree").html("<iframe id='sonFrame' src='"+path+"/common/selectUserCliTree.jsp' frameborder='0'></iframe>");
	}else if(treeType === "6"){//客户属性
		$.post(path+"/selectUserInfo.htm",{
			method:"getClientAttrList"
		},function (clientFieldList) {
			if(clientFieldList != null && clientFieldList !== "" && clientFieldList !== undefined && clientFieldList !== "null") {
				$("#etree").show();
				var list = eval("("+clientFieldList+")");
				for (var i=0;i<list.length;i++) {
					$("#etree").append("<div class='groupList' onclick='clientFieldChange(this)' mcount='"+list[i].clientFieldCount+"' fieldid='"+list[i].clientFieldId+"' dataValue='"+ list[i].clientFieldRef +"'>"+ list[i].clientFieldName +"</div>");
				}
			}
		});
	} else if(treeType === "7"){//签约用户
		$.post(path+"/selectUserInfo.htm",{
			method:"getSignClientList"
		},function (signClientList) {
			if(signClientList != null && signClientList !== "" && signClientList !== undefined && signClientList !== "null") {
				$("#etree").show();
				var list = eval("("+signClientList+")");
				for (var i=0;i<list.length;i++) {
					$("#etree").append("<div class='groupList' onclick='signClientChange(this)' mcount='"+list[i].memberCount+"' dataValue='"+ list[i].taocanCode +"'>"+ list[i].taocanName +"("+ list[i].taocanCode +")</div>");
				}
			}
		});
	}else if(treeType === "9"){
        $(".searchNameWrapper").hide();
        $("#fileUploadInput").css("display","inline-block");
        $("#showUserName").html(getJsLocaleMessage('common','common_format_sample')/*"格式图例："*/);
        $("#left").append("<img class='txt_img' src='"+pathUrl+"/common/img/gp_upload_intro1_zh_CN.png' title='.txt文件' alt='.txt文件'/>" +
            "<img class='xls_img' src='"+pathUrl+"/common/img/gp_upload_intro2_zh_CN.png' title='.xls文件' alt='.xls文件'/>");
        var data = $("#fileInputTemp").val();
        if(data !== "" && data !== "null" && data !== undefined){
            $("#etree").html(data);
            if($(".showFile").length === 0){
                $(".remindMsg").show();
            }
        }else {
            $("#etree").append("<div class='remindMsg'>" +
                "<p>"+getJsLocaleMessage('common','common_remind_label')+"<!--注：--></p>" +
                "<p>"+getJsLocaleMessage('common','common_remind_1')+"<!--1.批量添加只支持上传.txt/.xls/.xlsx文件。--></p>" +
                "<p>"+getJsLocaleMessage('common','common_remind_2')+"<!--2.单个文件上传有效数据不能超过2000条。--></p>" +
                "<p>"+getJsLocaleMessage('common','common_remind_3')+"<!--3.最多上传五个文件。--></p>" +
                "<p>"+getJsLocaleMessage('common','common_remind_4')+"<!--4.错误格式数据(名字包含特殊符号等)将会被自动过滤。--></p>" +
                "<p>"+getJsLocaleMessage('common','common_remind_5')+"<!--5.具体填写见格式图例(左为.txt文件，右为.xls/.xlsx文件)。--></p>" +
                "</div>");
            $(".remindMsg").show();
        }
    }else {	//群组
        $("#showUserName").html(showUserName);
        $(".searchNameWrapper").show();
        $("#fileUploadInput").hide();
		$("#etree").show();
		$.post(path+"/selectUserInfo.htm",{
			method:"getRmsGroup",
			groupType:treeType
			},
			function(groupListRes) {
				if(groupListRes != null && groupListRes !== "" && groupListRes !== undefined && groupListRes !== "null"){
					$("#etree").show();
					var groupList = eval("("+groupListRes+")");
					for (var i=0;i<groupList.length;i++) {
						var attrName;
						//groupType  1 员工 2 客户
						//shareType  0 个人 1 共享
						if(groupList[i].groupType === 1){
							attrName = groupList[i].shareType === "0" ? getJsLocaleMessage('common','common_employee_person')/*"[员工/个人]"*/:getJsLocaleMessage('common','common_employee_share')/*"[员工/共享]"*/;
						}else if(groupList[i].groupType === 2){
							attrName = groupList[i].shareType === "0" ? getJsLocaleMessage('common','common_customer_person')/*"[客户/个人]"*/:getJsLocaleMessage('common','common_customer_share')/*"[客户/共享]"*/;
						}
						$("#etree").append("<div class='groupList' " +
							"onclick='groupChange(this)' gcount='"+ groupList[i].groupCount +"' " +
							"isdep='"+ groupList[i].isDep +"' sharetype='"+ groupList[i].shareType +"' " +
							"gtype='"+ groupList[i].groupType +"' dataValue='"+ groupList[i].groupId +"' " +
							"udgid='"+ groupList[i].udgId +"'>"+ groupList[i].groupName + attrName +"</div>");
					}
				}
			}
		);
	}
}

//选择左边员工的操作
function moveIn(obj){
    var $selected;
    if(obj !== null && obj !== "" && obj !== undefined){
        $selected = $(obj);
    }else {
        $selected =  $("#left").find("div.selectedNode");
    }
    if($selected.size() === 0){
        alert(getJsLocaleMessage('common','common_alert_0001'));
        return;
    }
    var signClientIds = $("#signClientIds").val();
    var employeeIds = $("#employeeIds").val();
    var clientIds = $("#clientIds").val();
    var malistIds = $("#malistIds").val();
    var moblieStrs = $("#moblieStrs").val();
    var signClientPhoneStr = $("#signClientPhoneStr").val();
    var cusFieldValueIds = $("#cusFieldValueIds").val();

    var kongMoblieStr = "";
    var havExistStr = "";
    var manCountStr = $("#manCount").html();
    manCountStr = "" === manCountStr || undefined === manCountStr || "NaN" === manCountStr ?  "0" : manCountStr;
    var manCount = parseInt(manCountStr);
    $selected.each(function(){
	//成员GUID
	var guid = $(this).attr("dataValue");
	//这里判断成员类型  是 4员工 5客户 6自建 7签约用户 8签约套餐 9属性值用户 10客户属性 11操作员
	var isdep = $(this).attr("isdep");
	//手机号码
	var mobile = $(this).attr("mobile");
	//成员名称
	var name = $(this).text();
	//去掉带上的后缀[]
    var reg = /^(.*?)\[.*]$/g;
    if(name.match(reg)){
        name = RegExp.$1;
    }
	//如果是通过群组修改或编辑接口进入则另外处理
    if($("#isGroupEdit").val()==="1"){
    	//检查是否重复
        var $getChooseMan = $("#getChooseMan").find("div[dataval='"+guid+"']");
        if($getChooseMan.html() === null || $getChooseMan.html() === ""){
        	//获取类型
            var typeStr = getTypeStr(isdep,guid);
            //手机号是否需要加密
            var phoneStr = getPhoneStr(isdep,mobile);
            $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='groupEdit' " +
				"dataval='"+guid+"' isdep='"+isdep+"' et='' mobile='"+mobile+"'>" +
				"<span class='selectedName' title='"+name+"'>"+ name +"</span>" +
				"<span class='selectedMobile'>"+ phoneStr +"</span>" +
				"<span class='selectedType'>"+ typeStr +"</span>" +
				"</div>");
            manCount += 1;
            $("#manCount").html(manCount);
		}else {
        	//重复添加
            havExistStr = havExistStr + name + "，";
		}
    }else {
        if (isdep === "4") {
            //处理员工
            if (employeeIds === "" || employeeIds.length === 0) {
                if (mobile === "" || mobile.length === 0) {
                    kongMoblieStr = kongMoblieStr + name + ",";
                } else {
                    manCount++;
                    $("#right").append("<option value=\'" + guid + "\' isdep='4'  et='' mobile='" + mobile + "'>" + getJsLocaleMessage("common","common_employee") + name + "</option>");
                    $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + guid + "' isdep='4' et='' mobile='" + mobile + "'>" + getJsLocaleMessage("common","common_employee") + name + "</div>");
                    employeeIds = employeeIds + guid + ",";
                    moblieStrs = moblieStrs + mobile + ",";
                }
            } else {
                if (ListContainObject(employeeIds, guid)) {
                    havExistStr = havExistStr + name + "，";
                } else {
                    manCount++;
                    employeeIds = employeeIds + guid + ",";
                    moblieStrs = moblieStrs + mobile + ",";
                    $("#right").append("<option value=\'" + guid + "\' isdep='4'  et='' mobile='" + mobile + "'>" + getJsLocaleMessage("common","common_employee") + name + "</option>");
                    $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + guid + "' isdep='4' et=''  mobile='" + mobile + "'>" + getJsLocaleMessage("common","common_employee") + name + "</div>");
                }
            }
        } else if (isdep === "5") {
            //处理客户
            if (clientIds === "" || clientIds.length === 0) {
                if (mobile === "" || mobile.length === 0) {
                    kongMoblieStr = kongMoblieStr + name + ",";
                } else {
                    manCount++;
                    $("#right").append("<option value=\'" + guid + "\' isdep='5'  et='' mobile='" + mobile + "'>" + getJsLocaleMessage("common","common_customer") + name + "</option>");
                    $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + guid + "' isdep='5' et=''  mobile='" + mobile + "'>" + getJsLocaleMessage("common","common_customer") + name + "</div>");
                    clientIds = clientIds + guid + ",";
                    moblieStrs = moblieStrs + mobile + ",";
                }
            } else {
                if (ListContainObject(clientIds, guid)) {
                    havExistStr = havExistStr + name + "，";
                } else {
                    manCount++;
                    clientIds = clientIds + guid + ",";
                    moblieStrs = moblieStrs + mobile + ",";
                    $("#right").append("<option value=\'" + guid + "\' isdep='5'  et='' mobile='" + mobile + "'>" + getJsLocaleMessage("common","common_customer") + name + "</option>");
                    $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + guid + "' isdep='5' et=''  mobile='" + mobile + "'>" + getJsLocaleMessage("common","common_customer") + name + "</div>");
                }
            }
        } else if (isdep === "6" || isdep === "13") {
            var type = isdep === "6" ? getJsLocaleMessage('common','common_buildby_myself') : getJsLocaleMessage('common','common_share');
            //处理外部人员
            if (malistIds === "" || malistIds.length === 0) {
                if (mobile === "" || mobile.length === 0) {
                    kongMoblieStr = kongMoblieStr + name + ",";
                } else {
                    manCount++;
                    $("#right").append("<option value=\'" + guid + "\' isdep='6'  et='' mobile='" + mobile + "'>" + type + name + "</option>");
                    $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + guid + "' isdep='6' et=''  mobile='" + mobile + "'>" + type + name + "</div>");
                    malistIds = malistIds + guid + ",";
                    moblieStrs = moblieStrs + mobile + ",";
                }
            } else {
                if (ListContainObject(malistIds, guid)) {
                    havExistStr = havExistStr + name + "，";
                } else {
                    manCount++;
                    malistIds = malistIds + guid + ",";
                    moblieStrs = moblieStrs + mobile + ",";
                    $("#right").append("<option value=\'" + guid + "\' isdep='6'  et='' mobile='" + mobile + "'>"+ type + name + "</option>");
                    $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + guid + "' isdep='6' et=''  mobile='" + mobile + "'>" + type + name + "</div>");
                }
            }
        } else if (isdep === "7") {
            var subscriber = getJsLocaleMessage('common', 'common_sign_user');
            //签约用户
            if (signClientIds === "" || signClientIds.length === 0) {
                if (mobile === "" || mobile.length === 0) {
                    kongMoblieStr += name + ",";
                } else {
                    manCount++;
                    $("#right").append("<option value=\'" + guid + "\' isdep='7'  et='8' mobile='" + mobile + "'>"+ subscriber + name + "</option>");
                    $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + guid + "' isdep='7' et='8'  mobile='" + mobile + "'>" + subscriber + name + "</div>");
                    signClientIds += guid + ",";
                    signClientPhoneStr += mobile + ",";
                }
            } else {
                if (ListContainObject(signClientIds, guid)) {
                    havExistStr += name + "，";
                } else {
                    manCount++;
                    signClientIds += guid + ",";
                    signClientPhoneStr += mobile + ",";
                    $("#right").append("<option value=\'" + guid + "\' isdep='7'  et='8' mobile='" + mobile + "'>" + subscriber + name + "</option>");
                    $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + guid + "' isdep='7' et='8'  mobile='" + mobile + "'>" + subscriber + name + "</div>");
                }
            }
        } else if (isdep === "9") {
            var $proValueIdStr = $(window.parent.document).find("#proValueIdStr");
            //处理属性值用户
            if (cusFieldValueIds === "" || cusFieldValueIds.length === 0) {
                $.post(pathUrl + "/selectUserInfo.htm", {
                    method: 'getCustFieldMemberCount',
                    corpCode: corpCode,
                    fieldValue: guid
                }, function (count) {
                    if (count === "0") {
                        alert(getJsLocaleMessage('common', 'common_errorInfo_text_4'));
                    } else {
                        cusFieldValueIds += guid + ",";
                        manCount += parseInt(count);
                        $("#manCount").html(manCount);
                        $("#cusFieldValueIds").val(cusFieldValueIds);
                        $proValueIdStr.val($proValueIdStr.val() + guid + ",");
                        $("#right").append("<option value=\'" + guid + "\' isdep='9'  et='6' mobile='' mcount='" + count + "'>" + getJsLocaleMessage("common","common_attribute") + name + "[" + count + getJsLocaleMessage("common","common_person")+"]</option>");
                        $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + guid + "' isdep='9' et='6'  mobile='' mcount='" + count + "'>" + getJsLocaleMessage("common","common_attribute") + name + "[" + count + getJsLocaleMessage("common","common_person")+"]</div>");
                    }
                });
            } else {
                if (ListContainObject(cusFieldValueIds, guid)) {
                    havExistStr += name + "，";
                } else {
                    $.post(pathUrl + "/selectUserInfo.htm", {
                        method: 'getCustFieldMemberCount',
                        corpCode: corpCode,
                        fieldValue: guid
                    }, function (count) {
                        if (count === "0") {
                            alert(getJsLocaleMessage('common', 'common_errorInfo_text_4'));
                        } else {
                            cusFieldValueIds += guid + ",";
                            manCount += parseInt(count);
                            $("#manCount").html(manCount);
                            $("#cusFieldValueIds").val(cusFieldValueIds);
                            $proValueIdStr.val($proValueIdStr.val() + guid + ",");
                            $("#right").append("<option value=\'" + guid + "\' isdep='9'  et='6' mobile='' mcount='" + count + "'>" + getJsLocaleMessage("common","common_attribute") + name + "[" + count + getJsLocaleMessage("common","common_person")+"]</option>");
                            $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + guid + "' isdep='9' et='6'  mobile='' mcount='" + count + "'>" + getJsLocaleMessage("common","common_attribute") + name + "[" + count + getJsLocaleMessage("common","common_person")+"]</div>");
                        }
                    });
                }
            }
        } else if (isdep === "11") {
            //处理操作员
            var flag = true;
            var $rightOption = $("#right option");
            $rightOption.each(function () {
                if (this.value === guid) {
                    havExistStr = havExistStr + name + "，";
                    flag = false;
                    return false;
                }
            });
            if (flag) {
                manCount++;
                $("#right").append("<option value=\'" + guid + "\' isdep='11'  et='1' mobile='" + mobile + "'>" + getJsLocaleMessage("common","common_operator") + name + "</option>");
                $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + guid + "' isdep='11' et='1'  mobile='" + mobile + "'>" + getJsLocaleMessage("common","common_operator") + name + "</div>");
            }
        }
    }
});
    var msg = "";
    if(kongMoblieStr !== "" && kongMoblieStr.length>0){
	kongMoblieStr = kongMoblieStr.substring(0,kongMoblieStr.length-1);
	//msg = "以下成员手机号码为空："+kongMoblieStr+"；\n";
}
    if(havExistStr !== "" && havExistStr.length>0){
	havExistStr = havExistStr.substring(0,havExistStr.length-1);
	//msg = msg + "以下成员已经存在："+havExistStr+"；";
	msg = getJsLocaleMessage("common","common_errorInfo_text_1");
}
    if(msg.length>0){
		alert(msg);
	}
    $("#manCount").html(manCount);
    $("#employeeIds").val(employeeIds);
    $("#clientIds").val(clientIds);
    $("#malistIds").val(malistIds);
    $("#signClientIds").val(signClientIds);
    $("#moblieStrs").val(moblieStrs);
    $("#signClientPhoneStr").val(signClientPhoneStr);
    $("#cusFieldValueIds").val(cusFieldValueIds);
}
	 
	 
//移出右边的单个或者多个选择的对象
function moveOut(){
	if($("#right option:selected").size() === 0 && $("#isGroupEdit").val() !== "1"){
		alert(getJsLocaleMessage('common','common_alert_0002'));
		return;
	}
	//1员工机构  2客户机构 3群组 4员工 5客户 6外部人员
    // 7签约用户 8签约套餐 9属性客户
    // 10客户属性 11操作员 12操作员机构 13共享
	//员工IDS
	var employeeIds = $("#employeeIds").val();
	//客户IDS
	var clientIds = $("#clientIds").val();
	//外部人员IDS
	var malistIds = $("#malistIds").val();
	//员工机构IDS
	var empDepIdsStrs = $("#empDepIdsStrs").val();
	//客户机构IDS
	var cliDepIdsStrs = $("#cliDepIdsStrs").val();
	//群组IDS
	var groupIdsStrs = $("#groupIdsStrs").val();
	//套餐Ids
	var taocanIds = $("#taocanIds").val();
	//签约用户Ids
	var signClientIds = $("#signClientIds").val();
	//属性值客户Ids
	var cusFieldValueIds = $("#cusFieldValueIds").val();
	//客户属性Ids
    var cusFieldIds = $("#cusFieldIds").val();
	//用户的手机号码集合
	var moblieStrs = $("#moblieStrs").val();
	//签约用户的手机号集合
    var signClientPhoneStr = $("#signClientPhoneStr").val();
    //群组增改---共享Id
    var gxStr = $("#gxStr").val();
    //群组增改---群组自建Id
    var qzStr = $("#qzStr").val();
    //群组增改---手动输入Id
    var zjStr = $("#zjStr").val();
    //群组增改---客户或员工Id
    var ygStr = $("#ygStr").val();
	$("#right option:selected").each(function(){
		//成员GUID
		var id = $(this).val();
		//这里判断成员类型  是 1员工机构  2客户机构 3群组 4员工 5客户 6外部人员 7签约用户 8签约套餐 9属性客户 10客户属性 11操作员 12操作员机构 13共享
		var isdep = $(this).attr("isdep");
		var mobile = $(this).attr("mobile");
		var name = $(this).text();
		//1表示机构包含关系    2表示不包含关系
		var et =  $(this).attr("et");
		if(isdep === "1"){
			//处理员工机构
			//alert("机构ID1："+empDepIdsStrs);
			//alert("类型："+et);
			if(et === "2") {
				empDepIdsStrs = empDepIdsStrs.replace("e"+id+"," , "");
			}else if(et === "1") {
				empDepIdsStrs = empDepIdsStrs.replace(id+"," , "");
			}
			$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
			//alert("机构ID2："+empDepIdsStrs);
		}else if(isdep === "2"){
			//处理客户机构
			if(et === "2") {
				cliDepIdsStrs = cliDepIdsStrs.replace("e"+id+"," , "");
			}else if(et === "1") {
				cliDepIdsStrs = cliDepIdsStrs.replace(id+"," , "");
			}
			$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
		}else if(isdep === "3"){
			//处理外群组
			if(ListContainObject(groupIdsStrs, id)) {
				groupIdsStrs = groupIdsStrs.replace(id+"," , "");
				$("#manCount").html(parseInt($("#manCount").html())-parseInt($(this).attr("mcount")));
			}
		}else if(isdep === "4"){
			//处理员工
			if(ListContainObject(employeeIds, id)) {
				employeeIds = employeeIds.replace(id+"," , "");
				moblieStrs = moblieStrs.replace(mobile+"," , "");
				$("#manCount").html(parseInt($("#manCount").html())-1);
			}
		}else if(isdep === "5"){
			//处理客户
			if(ListContainObject(clientIds, id)) {
				clientIds = clientIds.replace(id+"," , "");
				moblieStrs = moblieStrs.replace(mobile+"," , "");
				$("#manCount").html(parseInt($("#manCount").html())-1);
			}
		}else if(isdep === "6"){
			//处理外部人员
			if(ListContainObject(malistIds, id)) {
				malistIds = malistIds.replace(id+"," , "");
				moblieStrs = moblieStrs.replace(mobile+"," , "");
				$("#manCount").html(parseInt($("#manCount").html())-1);
			}
		}else if(isdep === "7"){
			//处理签约用户
			if(ListContainObject(signClientIds, id)) {
				signClientIds = signClientIds.replace(id+"," , "");
                signClientPhoneStr = signClientPhoneStr.replace(mobile+"," , "");
				$("#manCount").html(parseInt($("#manCount").html())-1);
			}
		}else if(isdep === "8"){
			//处理签约套餐
			if(ListContainObject(taocanIds, id)) {
				taocanIds = taocanIds.replace(id+"," , "");
				$("#manCount").html(parseInt($("#manCount").html()) - parseInt($(this).attr("mcount")));
			}
		}else if(isdep === "9"){
			//处理属性值用户
			if(ListContainObject(cusFieldValueIds, id)) {
				cusFieldValueIds = cusFieldValueIds.replace(id+"," , "");
				$("#manCount").html(parseInt($("#manCount").html()) - parseInt($(this).attr("mcount")));
			}
		}else if(isdep === "10"){
            //处理客户属性
            if(ListContainObject(cusFieldIds, id)) {
                cusFieldIds = cusFieldIds.replace(id+"," , "");
                $("#manCount").html(parseInt($("#manCount").html()) - parseInt($(this).attr("mcount")));
            }
        } else if(isdep === "11"){
            //处理操作员
            var $rightOption = $("#right option");
            $rightOption.each(function () {
                if(this.value === id){
                    $("#manCount").html(parseInt($("#manCount").html()) - 1);
                    return false;
                }
            });
        }else if(isdep === "12"){
            var depIdStr;
            if(et === "3"){
                depIdStr = $(window.parent.document).find("#depIdStr").val();
                $(window.parent.document).find("#depIdStr").val(depIdStr.replace("e"+id+",",""));
                $("#manCount").html(parseInt($("#manCount").html()) - parseInt($(this).attr("mcount")));
            }else if(et === "2") {
                depIdStr = $(window.parent.document).find("#depIdStr").val();
                $(window.parent.document).find("#depIdStr").val(depIdStr.replace(","+id+",",","));
                $("#manCount").html(parseInt($("#manCount").html()) - parseInt($(this).attr("mcount")));
            }
        }
		$(this).remove();
		if($('#getChooseMan div').size()>0){
			$('#getChooseMan > div').each(function(){
				if($(this).hasClass('cur')){
					$(this).remove();
				}
			})
		}
	});
	//通过群组增加或修改接口进入
	if($("#isGroupEdit").val()==="1"){
        var manNum = parseInt($("#manCount").html());
        //删除已选择的群组成员
        $("#getChooseMan .cur").each(function () {
            var guid = $(this).attr("dataval");
            var idDep = $(this).attr("isdep");
            if(idDep === "4"){
                ygStr = ygStr.replace(guid + ",","");
                manNum -= 1;
            }else if(idDep === "5"){
                ygStr = ygStr.replace(guid + ",","");
                manNum -= 1;
            }else if(idDep === "6"){
                qzStr = qzStr.replace(guid + ",","");
                manNum -= 1;
            }else if(idDep === "14"){
                zjStr = zjStr.replace(guid + ",","");
                manNum -= 1;
            }else if(idDep === "13"){
                gxStr = gxStr.replace(guid + ",","");
                manNum -= 1;
            }
            $(this).remove();
            $("#manCount").html(parseInt($("#manCount").html())- 1);
        });
    }
    $("#gxStr").val(gxStr);
    $("#qzStr").val(qzStr);
    $("#zjStr").val(zjStr);
    $("#ygStr").val(ygStr);
	$("#employeeIds").val(employeeIds);
	$("#clientIds").val(clientIds);
	$("#malistIds").val(malistIds);
	$("#empDepIdsStrs").val(empDepIdsStrs);
	$("#cliDepIdsStrs").val(cliDepIdsStrs);
	$("#groupIdsStrs").val(groupIdsStrs);
	$("#moblieStrs").val(moblieStrs);
    $("#signClientPhoneStr").val(signClientPhoneStr);
	$("#taocanIds").val(taocanIds);
	$("#signClientIds").val(signClientIds);
	$("#cusFieldValueIds").val(cusFieldValueIds);
    $("#cusFieldIds").val(cusFieldIds);
}

function getMemberType(isDep) {
    var memberType = "";
    if(isDep === "" || isDep === "null" || isDep === undefined){
        return memberType;
    }
    //判断成员类型(isdep)  4员工 5客户 6自建 13共享
    switch (isDep){
        case 4:memberType = getJsLocaleMessage('common','common_employee');break;
        case 5:memberType = getJsLocaleMessage('common','common_customer');break;
        case 6:memberType = getJsLocaleMessage('common','common_buildby_myself');break;
        case 13:memberType = getJsLocaleMessage('common','common_share');break;
    }
    return memberType;
}

//点击群组展示群组成员
function groupChange(obj) {
    var $obj = $(obj);
    var path = $("#pathUrl").val();
    //先删除之前所有的selectedGroup
    $(".selectedGroup").each(function () {
       $(this).removeClass("selectedGroup");
    });
    $obj.addClass("selectedGroup");
    $("#left").empty();
    $("#prepage").attr("disabled",false);
    $("#nextpage").attr("disabled",false);
    var pageIndex = 1;
    $("#pageIndex").val(pageIndex);
    // 当前群组ID
    var groupVal = $obj.attr("dataValue");
    var groupName = $obj.text();
    groupName = groupName.substring(0,groupName.lastIndexOf("["));
    // 当前群组类型  1是员工群组  2是客户群组/
    var groupType = $obj.attr("gtype");
    $("#qztype").val(groupType);
    var shareType = $obj.attr("shareType");//群组共享还是个人
    $("#shareType").val(shareType);
    var gcount = parseInt($obj.attr("gcount"));
    $("#choiceId").val(groupVal);
    $("#choiceName").val(groupName);
    $("#pagecode").empty();
    if(gcount === 0){
       $("#prepage").css("visibility","hidden");
       $("#nextpage").css("visibility","hidden");
       return;
    }
$.post(path+"/selectUserInfo.htm",
   {
		method:"getGroupUserByGroupId",
		depId:groupVal,
		pageIndex:pageIndex,
		type:groupType,
       shareType:shareType
   },
   function(result){
   if(result !== "" ){
       $("#prepage").css("visibility","visible");
       $("#nextpage").css("visibility","visible");
	   // 获取分页信息 /
	   var arr = result.substring(0,result.indexOf("#")).split(",");
	   //总页数 /
	   var pageTotal = parseInt(arr[0]);
	   //总记录数 /
	   var pageRec = arr[1];
	   // 添加记录 /
	   var memberList = eval("("+result.substring(result.indexOf("#")+1)+")");
	   for (var i=0;i<memberList.length;i++) {
			if(memberList[i].userName == null || memberList[i].userName === undefined){
				continue;
			}
			var memberType = getMemberType(parseInt(memberList[i].isDep));
		   $("#left").append("<div class='memberList' onclick='move2RightByClick(this)' " +
			   "ondblclick='move2Right(this)' dataValue='"+ memberList[i].udgId +"' isdep='"+ memberList[i].isDep +"'" +
			   " et='' mobile='"+ memberList[i].mobile +"'>"+ $.trim(memberList[i].userName) + memberType +"</div>");
	   }
	   $("#pagecode").html(pageIndex+"/"+pageTotal);
	   $("#prepage").attr("disabled",true);
	   //如果是只有一页记录的话,隐藏翻页按钮
	   if(pageTotal === 1){
		   $("#prepage").css("visibility","hidden");
		   $("#nextpage").css("visibility","hidden");
	   }
   }
});
}

// 隐藏按钮/
function hideBtn(){
	$("#prepage").css("visibility","hidden");
	$("#nextpage").css("visibility","hidden");
	$("#pagecode").empty();
	$("#userTotal").empty();
}
		
// 选择  员工  客户 群组  客户属性 员工机构 客户机构
function choiceBtn(){
// 将选择按钮置灰/
$("#selectDep").attr("disabled","disabled");
	// 选择的类型 /
	var chooseType = $("#choose_Type").val();
	// emp路径/
	var path = $("#pathUrl").val();
	// 群组ID
	var groupIds = $("#groupIdsStrs").val();
	//套餐Id
	var taocanIds = $("#taocanIds").val();
	//客户属性的Ids
	var cusFieldIds = $("#cusFieldIds").val();
	//已选择人数 /
	var manCount = parseInt($("#manCount").html());
	// 群组
	if($("#isGroupEdit").val()==="1"){
		alert(getJsLocaleMessage('common','common_alert_0003'));
		return false;
	}
	if(chooseType === "3"||chooseType === "4"||chooseType === "5"){
		// 没有群组人员的群组名称 /
		var noBodyGroup = "";
		// 已经选择了的群组名称 /
		var havExistGroup = "";
		var groupSize = $(".selectedGroup").size();
		if(groupSize === 0){
			//alert("请选择群组！");
			alert(getJsLocaleMessage('common','common_alert_0004'));
			$("#selectDep").attr("disabled","");
			return;
		}
		$(".selectedGroup").each(function(){
				// 群组VAL /
				var groupVal = $(this).attr("datavalue");
				// 群组 名称/
				var groupName = $(this).text();
				// 群组类型  员工还是客户群组  1 是员工群组  2是客户群组  /
				var groupType = $(this).attr("gtype");
				// 群组   0个人 1共享/
				var groupShare = $(this).attr("sharetype");
				// 群组 数量/
				var groupCount = $(this).attr("gcount");
				if(groupCount === "0"){
					noBodyGroup = noBodyGroup+groupName+"，";
				}else{
					var bool = false;
					if(groupIds !== ""&&groupIds.length>0){
						var groupIdsArr = groupIds.split(",");
						for(var i=0;i<groupIdsArr.length;i++){
							if(groupVal === groupIdsArr[i]){
								bool = true;
								break;
							}
						}
					}
					if(bool) {
						havExistGroup = havExistGroup+groupName+"，";
					}else{
						//人
						//groupName = groupName +" ["+ groupCount+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+ "]";
						groupName = groupName +" ["+ groupCount + getJsLocaleMessage("common","common_person")+"]";
						groupIds = groupIds+groupVal+",";
						$("#right").append("<option value=\'"+groupVal+"\' isdep='3'  et='' mobile='' mcount='"+$(this).attr("gcount")+"'>"+getJsLocaleMessage("common","common_group2")+groupName+"</option>");
						$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval=\'"+groupVal+"\' isdep='3'  et='' mobile='' mcount='"+$(this).attr("gcount")+"'>"+getJsLocaleMessage("common","common_group2")+groupName+"</d>");
						manCount +=parseInt($(this).attr("gcount"));
					}
				}
			}
		);
		$("#manCount").html(manCount);
		$("#groupIdsStrs").val(groupIds);
		var msg = "";
		if(noBodyGroup !== "" && noBodyGroup.length>0){
			noBodyGroup = noBodyGroup.substring(0,noBodyGroup.length-1);
			msg = getJsLocaleMessage('common','common_alert_0005')+noBodyGroup+"；\n";
			//msg = getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_86");
		}
		if(havExistGroup !== "" && havExistGroup.length>0){
			havExistGroup = havExistGroup.substring(0,havExistGroup.length-1);
			msg = msg + getJsLocaleMessage('common','common_alert_0006')+havExistGroup+"；";
			//msg = getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_87");
		}
		if(msg.length>0){
			alert(msg);
		}
		$("#selectDep").attr("disabled","");
		return;
	}else if(chooseType === "8"){
        //处理操作员机构
        var depId = $("#choiceId").val();
        var depName = $("#choiceName").val();
        if(depId == null || depId === undefined || depId === "") {
            alert(getJsLocaleMessage('common','common_alert_0007'));
            return;
        }
        var $rightOps = $("#right option");
        var depIdsExist= $(window.parent.document).find("#depIdStr").val();
        if($rightOps.length>0) {
            for(var i = 0;i<$rightOps.length;i=i+1) {
                if(depId === $rightOps.eq(i).attr("value") && $rightOps.eq(i).attr("isdep") === 12) {
                    alert($rightOps[i].text + getJsLocaleMessage('common','common_alert_0008'));
                    return;
                }
            }
        }
        $.post(path + "/selectUserInfo.htm",
            {
                method:"checkDepIsExist",
                lgcorpcode:corpCode,
                depId:depId,
                depIdsExist:depIdsExist,
                depName:depName
            },
            function(result) {
                if(result === "depExist") {
                    alert(getJsLocaleMessage('common','common_alert_0009'));
                    return;
                }
                var ismut = 0;
                if(confirm(getJsLocaleMessage('common','common_alert_0010'))){
                    ismut=1;
                }
                //检查要添加的机构是不是包含已经添加的机构，如果是则删除已经添加的子机构，如果不是则生成"[机构]...机构 (包含子机构)"
                $.post(path + "/selectUserInfo.htm",
                    {
                        method : "isDepsContainedSubDep",
                        depId : depId,
                        lgcorpcode:corpCode,
                        depIdsExist : depIdsExist,
                        depName : depName,
                        ismut : ismut
                    }, function(result2) {
                        var per = getJsLocaleMessage('common','common_person');
                        var org = getJsLocaleMessage('common','common_organization');
                        var containsub = getJsLocaleMessage('common','common_contain_suborgan');
                        if(ismut === 0) {
                            if(result2 === 0){
                                alert("[ "+depName+" ] " + getJsLocaleMessage('common','common_alert_0011'));
                                return ;
                            }
                            $(window.parent.document).find("#depIdStr").val(depIdsExist+depId+",");
                            $("#right").append("<option value='"+depId+"' isdep='12'  et='2' mobile='' mcount='"+result2+"'>"+getJsLocaleMessage("common","common_organization")+depName+" ("+result2+ per+")</option>");
                            $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='"+depId+"' isdep='12'  et='2' mobile='' mcount='"+result2+"'>"+org+depName+" ("+result2+ per+")</div>");
                            manCount += parseInt(result2);
                        }else {
                        	var $depIdStr = $(window.parent.document).find("#depIdStr");
                            if (result2.indexOf("notContains") === 0) {
                                if (result2.substr(12) === 0) {
                                    alert("[ " + depName + "]" + getJsLocaleMessage('common','common_alert_0011'));
                                    return;
                                }
                                $depIdStr.val(depIdsExist + "e" + depId + ",");
                                $("#right").append("<option value='" + depId + "' isdep='12'  et='3' mobile='' mcount='" + result2.substr(12) + "'>"+org + depName + "(" + containsub + ")(" + result2.substr(12) + per+")</option>");
                                $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + depId + "' isdep='12'  et='3' mobile='' mcount='" + result2.substr(12) + "'>"+org + depName + "(" + containsub + ")(" + result2.substr(12) + per+")</div>");
                                manCount += parseInt(result2.substr(12));
                            } else {
                                var depIdStr;
                                var strArr = result2.split(",");
                                if (strArr[0] === 0) {
                                    alert("[ " + depName + "]" + getJsLocaleMessage('common','common_alert_0011'));
                                    return;
                                }
                                $depIdStr.val(depIdsExist + "e" + depId + ",");
                                $("#right").append("<option value='" + depId + "' isdep='12'  et='3' mobile='' mcount='" + strArr[0] + "'>"+org + depName + "(" + containsub + ")(" + strArr[0] + per+")</option>");
                                $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='" + depId + "' isdep='12'  et='3' mobile='' mcount='" + strArr[0] + "'>" + org + depName + "("+containsub+")(" + strArr[0] + per+")</div>");
                                manCount = parseInt(strArr[0]);
                                for (var i = 1; i < strArr.length; i = i + 1) {
                                    var $selectedOps = $("#right").find("option[isdep='12'][value='" + strArr[i] + "']");
                                    var $chooseMan = $("#getChooseMan").find("div[isdep='12'][dataval='" + strArr[i] + "']");
                                    if ($selectedOps.attr("et") === "3") {
                                        depIdStr = $depIdStr.val().replace("e" + $selectedOps.val() + ",", "");
                                        $depIdStr.val(depIdStr);
                                    } else if ($selectedOps.attr("et") === "2") {
                                        depIdStr = $depIdStr.val().replace("," + $selectedOps.val() + ",", ",");
                                        $depIdStr.val(depIdStr);
                                    }
                                    $selectedOps.remove();
                                    $chooseMan.remove();
                                }
                            }
                        }
                        $("#manCount").html(manCount);
                    }
                );
            });
    } else if(chooseType === "7"){
        //处理签约用户
        // 没有用户的套餐
		var noBodyTaocan = "";
		// 已经选择了的套餐名称 /
		var havExistTaocan = "";
		var taocanSize = $(".selectedGroup").size();
		if(taocanSize === 0){
			alert(getJsLocaleMessage('common','common_alert_0012'));
			$("#selectDep").attr("disabled","");
			return;
		}
		$(".selectedGroup").each(function(){
			// 套餐val
			var taocanVal = $(this).attr("datavalue");
			// 套餐 名称
			var taocanName = $(this).text();
			// 套餐 数量
			var taocanCount = $(this).attr("mcount");
			if(taocanCount === "0"){
				noBodyTaocan += taocanName+"，";
			}else {
				var bool = false;
				if(taocanIds !== "" && taocanIds.length>0){
					var taocanIdArrs = taocanIds.split(",");
					for(var i=0;i<taocanIdArrs.length;i++){
						if(taocanVal === taocanIdArrs[i]){
							bool = true;
							break;
						}
					}
				}
				if(bool) {
					havExistTaocan += taocanName +"，";
				}else{
					taocanName = taocanName +" ["+ taocanCount + "人]";
					taocanIds += taocanVal + ",";
					$("#right").append("<option value=\'"+taocanVal+"\' isdep='8'  et='9' mobile='' mcount='"+$(this).attr("mcount")+"'>"+getJsLocaleMessage('common','common_subscrib_combo')+taocanName+"</option>");
					$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval=\'"+taocanVal+"\' isdep='8'  et='9' mobile='' mcount='"+$(this).attr("mcount")+"'>"+getJsLocaleMessage('common','common_subscrib_combo')+taocanName+"</d>");
					manCount += parseInt($(this).attr("mcount"));
				}
			}
		});
		$("#manCount").html(manCount);
		$("#taocanIds").val(taocanIds);
		var msg = "";
		if(noBodyTaocan !== "" && noBodyTaocan.length>0){
			noBodyTaocan = noBodyTaocan.substring(0,noBodyTaocan.length-1);
			msg = getJsLocaleMessage('common','common_alert_0013') + noBodyTaocan+"；\n";
			//msg = getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_86");
		}
		if(havExistTaocan !== "" && havExistTaocan.length>0){
			havExistTaocan = havExistTaocan.substring(0,havExistTaocan.length-1);
			msg = msg + getJsLocaleMessage('common','common_alert_0014')+havExistTaocan+"；";
			//msg = getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_87");
		}
		if(msg.length>0){
			alert(msg);
		}
		$("#selectDep").attr("disabled","");
		return;
	}else if(chooseType === "6"){
		//处理客户属性
		// 没有用户的客户属性
		var noBodyClientField = "";
		// 已经选择了的客户属性名称 /
		var havExistClientField = "";
		var fieldSize = $(".selectedGroup").size();
		if(fieldSize === 0){
			alert(getJsLocaleMessage('common','common_alert_0034'));
			$("#selectDep").attr("disabled","");
			return;
		}
		$(".selectedGroup").each(function(){
			var $proIdStr = $(window.parent.document).find("#proIdStr");
			// 属性值
			var fieldVal = $(this).attr("datavalue");
			// 属性名称
			var fieldName = $(this).text();
			// 包含人数
			var fieldCount = $(this).attr("mcount");
			if(fieldCount === "0"){
				noBodyClientField += fieldName+"，";
			}else {
				var bool = false;
				if(cusFieldIds !== "" && cusFieldIds.length>0){
					var fieldIdArrs = cusFieldIds.split(",");
					for(var i=0;i<fieldIdArrs.length;i++){
						if(fieldVal === fieldIdArrs[i]){
							bool = true;
							break;
						}
					}
				}
				if(bool) {
					havExistClientField += fieldName +"，";
				}else{
					fieldName = fieldName +" ["+ fieldCount + getJsLocaleMessage('common','common_person')+"]";
					cusFieldIds += fieldVal + ",";
					//将值存在父页面
					$proIdStr.val($proIdStr.val() + fieldVal + ",");
					$("#right").append("<option value='"+fieldVal+"' isdep='10'  et='5' mobile='' mcount='"+$(this).attr("mcount")+"'>"+getJsLocaleMessage('common','common_customer_attr')+fieldName+"</option>");
					$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='"+fieldVal+"' isdep='10'  et='5' mobile='' mcount='"+$(this).attr("mcount")+"'>"+getJsLocaleMessage('common','common_customer_attr')+ fieldName +"</d>");
					manCount += parseInt($(this).attr("mcount"));
				}
			}
		});
		$("#manCount").html(manCount);
		$("#cusFieldIds").val(cusFieldIds);
		var msg = "";
		if(noBodyClientField !== "" && noBodyClientField.length>0){
			noBodyClientField = noBodyClientField.substring(0,noBodyClientField.length-1);
			msg = getJsLocaleMessage('common','common_alert_0015') + noBodyClientField+"；\n";
		}
		if(havExistClientField !== "" && havExistClientField.length>0){
			havExistClientField = havExistClientField.substring(0,havExistClientField.length-1);
			msg = msg + getJsLocaleMessage('common','common_alert_0016')+havExistClientField+"；";
		}
		if(msg.length>0){
			alert(msg);
		}
		$("#selectDep").attr("disabled","");
		return;
	}
	// 选择的名称 /
	var choiceName = $("#choiceName").val();
	// 选择的ID /
	var choiceId = $("#choiceId").val();
	// 处理员工/客户机构的选择操作/
	if(choiceId === ""){
		alert(getJsLocaleMessage('common','common_alert_0017'));
		$("#selectDep").attr("disabled","");
		return;
	}
	// 员工机构ID /
	var empDepIds = $("#empDepIdsStrs").val();
	// 客户机构ID /
	var cliDepIds = $("#cliDepIdsStrs").val();

	//提示/
	var viewDepNoBody = getJsLocaleMessage('common','common_alert_0018');
	var viewDepIsExist = getJsLocaleMessage('common','common_alert_0019');

	//获取右边选择好的对象
	var rops = $("#right option");
	if(rops.length>0) {
	// 员工机构 /
	if(chooseType === "1"){
		//处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组/
		for(var i = 0;i<rops.length;i=i+1)
		{
			if(choiceId === rops.eq(i).attr("value") && rops.eq(i).attr("isDep") === 1)
			{
				//alert(rops[i].text + " 记录已添加！");
				alert(viewDepIsExist);
				$("#selectDep").attr("disabled","");
				return;
			}
		}
	// 客户机构 /
	}else if(chooseType === "2"){
		for(var i = 0;i<rops.length;i=i+1)
		{
			if(choiceId === rops.eq(i).attr("value") && rops.eq(i).attr("isDep") === 2)
			{
				//alert(rops[i].text + " 记录已添加！");
				alert(viewDepIsExist);
				$("#selectDep").attr("disabled","");
				return;
			}
		}
	}
}
	//处理员工/客户机构是否被包含或者包含子机构/
	if(chooseType === "1"){
	if(empDepIds !== "" && empDepIds.length>0){
		//处理员工机构/
		$.post(path+"/selectUserInfo.htm",
			{
				method:"isEmpDepContained",
				depId:choiceId,
				empDepIds:empDepIds
			},
			function(result) {
			if("" === result){
				alert(getJsLocaleMessage('common','common_alert_0020'));
				$("#selectDep").attr("disabled","");
				return;
			}else if(result.indexOf("html") > 0){
				alert(getJsLocaleMessage('common','common_alert_0021'));
				$("#selectDep").attr("disabled","");
				window.location.href = window.location.href;
				return;
			}
			if(result=="depExist")
			{
				alert(getJsLocaleMessage('common','common_alert_0009'));
				$("#selectDep").attr("disabled","");
				return;
			}
			var ismut = 0;
			if(confirm(getJsLocaleMessage('common','common_alert_0010'))){
				ismut=1;
			}
			//处理该机构包含的子机构 操作, 并将选择好的子机构进行删除操作 /
			$.post(path+"/selectUserInfo.htm",
				{
					method : "isDepContaineDeps",
					depId : choiceId,
					empDepIds : empDepIds,
					ismut : ismut
				}, function(result2){
			            var employeeOrg = getJsLocaleMessage('common','common_employee_organization');
			            var per = getJsLocaleMessage('common','common_person');
			            var contain = getJsLocaleMessage('common','common_contain');
						if(result2 == "nobody"){
							//alert("该员工机构下没有员工！");
							alert(viewDepNoBody);
							$("#selectDep").attr("disabled","");
							return;
						}else if(result2 == "errer"){
							alert(getJsLocaleMessage('common',''));
							$("#selectDep").attr("disabled","");
							return;
						}
						//机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组      et 1表示不包含    2表示包含/
						if(ismut == 0){
							$("#empDepIdsStrs").val(empDepIds+choiceId+",");
							$("#right").append("<option value=\'"+choiceId+"\' isdep='1'  et='1' mobile='' mcount='"+result2+"'>"+employeeOrg + choiceName+" [" + result2+ per+"]</option>");
							$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval=\'"+choiceId+"\' isdep='1'  et='1' mobile='' mcount='"+result2+"'>"+employeeOrg + choiceName+" ["+result2 + per+"]</d>");
							$("#selectDep").attr("disabled","");
							manCount+=parseInt(result2);
						}
						//机构包含子机构但是右边没有其要删除的子机构的操作 /
						else if(result2.indexOf("notContains")==0)
						{
							$("#empDepIdsStrs").val(empDepIds+"e"+choiceId+",")
							$("#right").append("<option value='"+choiceId+"' isdep='1'  et='2' mobile='' mcount='"+result2.substr(12)+"'>"+employeeOrg+ choiceName + contain+ result2.substr(12)+per+"]</option>");
							$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='"+choiceId+"' isdep='1'  et='2' mobile='' mcount='"+result2.substr(12)+"'>"+employeeOrg + choiceName + contain + result2.substr(12)+ getJsLocaleMessage("common","common_person")+"]</d>");
							$("#selectDep").attr("disabled","");
							manCount+=parseInt(result2.substr(12));
						}else{
								var strArr = result2.split(",");
								$("#empDepIdsStrs").val(empDepIds+"e"+choiceId+",")
								$("#right").append("<option value='"+choiceId+"' isdep='1'  et='2' mobile='' mcount='"+strArr[0]+"'>"+employeeOrg+ choiceName+ contain+strArr[0] + per+"]</option>");
								$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='"+choiceId+"' isdep='1'  et='2' mobile='' mcount='"+strArr[0]+"'>"+employeeOrg+ choiceName + contain+strArr[0] + per+"</d>");
								$("#selectDep").attr("disabled","");
								manCount+=parseInt(strArr[0]);
								for(var i=1;i<strArr.length;i=i+1)
								{
									var containId = $.trim(strArr[i]);
									var $aaa = $("#right").find("option[isdep=1][value='"+containId+"']");
									var $bbb=$("#getChooseMan").find("div[isdep=1][dataval='"+containId+"']");
									// et1表示包含子机构关系,2表示不包含子机构/
									var depidstr = $("#empDepIdsStrs").val();
									if($aaa.attr("et")===2)
									{
										$("#empDepIdsStrs").val(depidstr.replace("e"+$aaa.val()+",",""));
									}else if($aaa.attr("et")===1)
									{
										$("#empDepIdsStrs").val(depidstr.replace($aaa.val()+",",""));
									}
									manCount=manCount-$aaa.attr("mcount");
									$aaa.remove();
									$bbb.remove();
								}
						}
						$("#manCount").html(manCount);
				});
		});
	}else{
        var employeeOrg = getJsLocaleMessage('common','common_employee_organization');
        var per = getJsLocaleMessage('common','common_person');
        var contain = getJsLocaleMessage('common','common_contain');
		//直接添加员工机构/
		var ismut = 0;
		if(confirm(getJsLocaleMessage('common','common_alert_0010'))){
			ismut=1;
		}
		$.post(path+"/selectUserInfo.htm",{
			method : "getEmpDepCount",
			depId : choiceId,
			ismut : ismut
		}, function(result2){
			if(result2 === "nobody" || result2 === "0"){
				//alert("该员工机构下没有员工！");
				alert(viewDepNoBody);
				$("#selectDep").attr("disabled","");
				return;
			}else if(result2 === "errer"){
				alert(getJsLocaleMessage('common','common_alert_0021'));
				$("#selectDep").attr("disabled","");
				return;
			}
			//机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组   4代表员工 5代表客户 6代表外部人员   et 1表示不包含    2表示包含/
			if(ismut==0){
				$("#empDepIdsStrs").val(choiceId+",");
				$("#right").append("<option value='"+choiceId+"' isdep='1'  et='1' mobile='' mcount='"+result2+"'>"+employeeOrg+choiceName+" ["+result2+per+"]</option>");
				$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='"+choiceId+"' isdep='1'  et='1' mobile='' mcount='"+result2+"'>"+employeeOrg+choiceName+" ["+result2+per+"]</d>");

				//$("#right").append("<option value='"+choiceId+"' isdep='1'  et='1' mobile='' mcount='"+result2+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+" "+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</option>");
				//$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='1'  et='1' mobile='' mcount='"+result2+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+" "+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</li>");

				$("#selectDep").attr("disabled","");
				manCount+=parseInt(result2);
			}else{
				var strArr = result2.split(",");
				$("#empDepIdsStrs").val("e"+choiceId+",")
				$("#right").append("<option value='"+choiceId+"' isdep='1'  et='2' mobile=''  mcount='"+strArr[0]+"'>"+employeeOrg+choiceName+contain+result2+per+"]</option>");
				$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='"+choiceId+"' isdep='1'  et='2' mobile=''  mcount='"+strArr[0]+"'>"+employeeOrg+choiceName+contain+result2+per+"]</d>");

				//$("#right").append("<option value='"+choiceId+"' isdep='1'  et='2' mobile=''  mcount='"+strArr[0]+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</option>");
				//$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='1'  et='2' mobile=''  mcount='"+strArr[0]+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_92")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</li>");

				$("#selectDep").attr("disabled","");
				manCount+=parseInt(strArr[0]);
			}
			$("#manCount").html(manCount);
		});
	}
}else if(chooseType === "2"){
        var customerOrg = getJsLocaleMessage('common','common_customer_organization');
        var per = getJsLocaleMessage('common','common_person');
        var contain = getJsLocaleMessage('common','common_contain');
	//等下处理
	//处理客户机构/
	if(cliDepIds !== "" && cliDepIds.length>0){
		//处理客户机构/
		$.post(path+"/selectUserInfo.htm", {method:"isClientDepContained",depId:choiceId,cliDepIds:cliDepIds}, function(result)
		{
			if("" === result){
				alert(getJsLocaleMessage('common','common_alert_0020'));
				//alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
				$("#selectDep").attr("disabled","");
				return;
			}else if(result.indexOf("html") > 0){
				alert(getJsLocaleMessage('common','common_alert_0021'));
				//alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_89"));
				$("#selectDep").attr("disabled","");
				window.location.href = window.location.href;
				return;
			}
			if(result==="depExist") {
				alert(getJsLocaleMessage('common','common_alert_0009'));
				//alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_90"));
				$("#selectDep").attr("disabled","");
				return;
			}
			var ismut = 0;
			if(confirm(getJsLocaleMessage('common','common_alert_0010'))){
			//if(confirm(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_91"))){
				ismut=1;
			}
			//处理该机构包含的子机构操作, 并将选择好的子机构进行删除操作 /
			$.post(path+"/selectUserInfo.htm",
				{
					method : "isClientDepContaineDeps",
					depId : choiceId,
					cliDepIds : cliDepIds,
					ismut : ismut
				}, function(result2){
						if(result2 === "nobody"){
							//alert("该员工机构下没有客户！");
							alert(viewDepNoBody);
							$("#selectDep").attr("disabled","");
							return;
						}else if(result2 === "errer"){
							alert(getJsLocaleMessage('common','common_alert_0021'));
							//alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_16"));
							$("#selectDep").attr("disabled","");
							return;
						}
						//机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组      et 1表示不包含    2表示包含/
						if(ismut===0){
							$("#cliDepIdsStrs").val(cliDepIds+choiceId+",");
							$("#right").append("<option value=\'"+choiceId+"\' isdep='2'  et='1' mobile='' mcount='"+result2+"'>"+customerOrg+choiceName+" ["+result2+per+"]</option>");
							$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval=\'"+choiceId+"\' isdep='2'  et='1' mobile='' mcount='"+result2+"'>"+customerOrg+choiceName+" ["+result2+per+"]</d>");

							//$("#right").append("<option value=\'"+choiceId+"\' isdep='2'  et='1' mobile='' mcount='"+result2+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</option>");
							//$("#getChooseMan").append("<li dataval=\'"+choiceId+"\' isdep='2'  et='1' mobile='' mcount='"+result2+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</li>");
							$("#selectDep").attr("disabled","");
							manCount+=parseInt(result2);
						}
						//机构包含子机构但是右边没有其要删除的子机构的操作 /
						else if(result2.indexOf("notContains")===0)
						{
							$("#cliDepIdsStrs").val(cliDepIds+"e"+choiceId+",")
							$("#right").append("<option value='"+choiceId+"' isdep='2'  et='2' mobile='' mcount='"+result2.substr(12)+"'>"+customerOrg+choiceName+contain+result2.substr(12)+per+"]</option>");
							$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='"+choiceId+"' isdep='2'  et='2' mobile='' mcount='"+result2.substr(12)+"'>"+customerOrg+choiceName+contain+result2.substr(12)+per+"]</d>");

							//$("#right").append("<option value='"+choiceId+"' isdep='2'  et='2' mobile='' mcount='"+result2.substr(12)+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+result2.substr(12)+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</option>");
							//$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='2'  et='2' mobile='' mcount='"+result2.substr(12)+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+result2.substr(12)+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</li>");
							//$("#selectDep").attr("disabled","");
							manCount+=parseInt(result2.substr(12));
						}else{
								var strArr = result2.split(",");
								$("#cliDepIdsStrs").val(cliDepIds+"e"+choiceId+",")
								//$("#right").append("<option value='"+choiceId+"' isdep='2'  et='2' mobile='' mcount='"+strArr[0]+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+strArr[0]+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</option>");
								//$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='2'  et='2' mobile='' mcount='"+strArr[0]+"'>"+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+" "+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+strArr[0]+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+"]</li>");
								$("#right").append("<option value='"+choiceId+"' isdep='2'  et='2' mobile='' mcount='"+strArr[0]+"'>"+ customerOrg+choiceName + contain + strArr[0] + per+"" +"]</option>");
								$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='"+choiceId+"' isdep='2'  et='2' mobile='' mcount='"+strArr[0]+"'>"+ customerOrg+choiceName + contain + strArr[0] + per + "]</d>");
								$("#selectDep").attr("disabled","");
								manCount+=parseInt(strArr[0]);
								for(var i=1;i<strArr.length;i=i+1)
								{
									var containId = $.trim(strArr[i]);
									var $aaa = $("#right").find("option[isdep=2][value='"+containId+"']");
									var $bbb = $("#getChooseMan").find("div[isdep=2][dataval='"+containId+"']");
									// et1表示包含子机构关系,2表示不包含子机构/
									var depidstr = $("#cliDepIdsStrs").val();
									if($aaa.attr("et")==2)
									{
										$("#cliDepIdsStrs").val(depidstr.replace("e"+$aaa.val()+",",""));
									}else if($aaa.attr("et")==1)
									{
										$("#cliDepIdsStrs").val(depidstr.replace($aaa.val()+",",""));
									}
									manCount=manCount-$aaa.attr("mcount");
									$aaa.remove();
									$bbb.remove();
								}
						}
						$("#manCount").html(manCount);
				});
		});
	}else{
		//直接添加客户机构/
		var ismut = 0;
		if(confirm(getJsLocaleMessage('common','common_alert_0010'))){
			ismut=1;
		}
		$.post(path+"/selectUserInfo.htm",{
			method : "getClientDepCount",
			depId : choiceId,
			ismut : ismut
		}, function(result2){
			if(result2 == "nobody" || result2 == "0"){
			//	alert("该客户机构下没有客户！");
				alert(viewDepNoBody);
				$("#selectDep").attr("disabled","");
				return;
			}else if(result2 == "errer"){
				alert(getJsLocaleMessage('common','common_alert_0021'));
				$("#selectDep").attr("disabled","");
				return;
			}
			//机构不包含子机构的操作   isDep表示是1 员工机构 2表示客户 3 表示群组      et 1表示不包含    2表示包含/
			if(ismut==0){
				$("#cliDepIdsStrs").val(choiceId+",");
				//$("#right").append("<option value='"+choiceId+"' isdep='2'  et='1' mobile=''  mcount='"+result2+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95") + choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93") + "]</option>");
				//$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='2'  et='1' mobile=''  mcount='"+result2+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+choiceName+" ["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93")+ "]</li>");
				$("#right").append("<option value='"+choiceId+"' isdep='2'  et='1' mobile=''  mcount='"+result2+"'>"+customerOrg +  choiceName+" ["+ result2 + per+"]</option>");
				$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='"+choiceId+"' isdep='2'  et='1' mobile=''  mcount='"+result2+"'>"+customerOrg + choiceName+" ["+ result2 + per+"]</d>");
				$("#selectDep").attr("disabled","");
				manCount+=parseInt(result2);
			}else{
				var strArr = result2.split(",");
				$("#cliDepIdsStrs").val("e"+choiceId+",")
				$("#right").append("<option value='"+choiceId+"' isdep='2'  et='2' mobile=''  mcount='"+strArr[0]+"'>"+customerOrg + choiceName + contain + result2 + per+"]</option>");
				$("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='chooseMan' dataval='"+choiceId+"' isdep='2'  et='2' mobile=''  mcount='"+strArr[0]+"'>"+customerOrg + choiceName + contain + result2 + per+"]</d>");
				//$("#right").append("<option value='"+choiceId+"' isdep='2'  et='2' mobile=''  mcount='"+strArr[0]+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+ "["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93") + "]</option>");
				//$("#getChooseMan").append("<li dataval='"+choiceId+"' isdep='2'  et='2' mobile=''  mcount='"+strArr[0]+"'>" + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_95")+choiceName+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_94")+ "["+result2+getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_93") + "]</li>");
				$("#selectDep").attr("disabled","");
				manCount+=parseInt(strArr[0]);
			}
			$("#manCount").html(manCount);
		});
	}
	}
}

function treeLoseFocus() {
	//window.frames['sonFrame'].returnZTree().cancelSelectedNode();
	$("#sonFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
	$("#groupList").val("");
}

	function router() {
		if($("#left").find("div.selectedNode").size() > 0) {
			moveIn();
			$("div.selectedNode").removeClass("selectedNode");
		}else{
			choiceBtn();
		}
        $("#selectAllMem").html(getJsLocaleMessage('common','common_select_all'));
        $("#selectAllMem").attr("val","1");
	}

	function changeChooseDiv(obj) {
	 	var $selected = $(obj);
		var dataVal = $selected.attr("dataval");
		var isDep = $selected.attr("isdep");
		if($selected.hasClass("cur")){
            $selected.css("background-color","");
            $selected.removeClass("cur");
			$("#right").find("option[value='"+dataVal+"'][isdep='"+ isDep +"']").attr("selected","");
		}else {
            $selected.css("background-color","#ebf2ff");
            $selected.addClass("cur");
			$("#right").find("option[value='"+dataVal+"'][isdep='"+ isDep +"']").attr("selected","selected");
		}
	}

	//右边的选框
	var initSelectBox = function(selector, selectCallback) {
		function clearBubble(e) {
		if (e.stopPropagation) {
		   e.stopPropagation();
		} else {
		   e.cancelBubble = true;
		}

		if (e.preventDefault) {
		   e.preventDefault();
		} else {
		   e.returnValue = false;
		}
		}
		var $container = $(selector);
		//  框选事件
		$container.mousedown(function(eventDown) {
			//滚动距离
			var scrollTop = $(".showchoiceDiv").attr('scrollTop');
		   //  创建选框节点
		   var $selectBoxDashed = $('<div class="select-box-dashed"></div>');
		   $('body').append($selectBoxDashed);
		   //  设置选框的初始位置(选框右下角坐标)
		   var startX = eventDown.x || eventDown.clientX;
		   var startY = eventDown.y || eventDown.clientY;
		   $selectBoxDashed.css({
			   left: startX,
			   top : startY
		   });
		   //  根据鼠标移动，设置选框宽高
		   var _x = null;
		   var _y = null;
		   //  清除事件冒泡、捕获
		   clearBubble(eventDown);
		   //  监听鼠标移动事件
		   $(selector).mousemove(function(eventMove) {
			   //  设置选框可见
			   $selectBoxDashed.css('display', 'block');
			   //  根据鼠标移动，设置选框的位置、宽高(左上角的点的坐标)
			   _x = eventMove.x || eventMove.clientX;
			   _y = eventMove.y || eventMove.clientY;
			   //  暂存选框的位置及宽高，用于将 select-item 选中
			   var _left   = Math.min(_x, startX);
			   var _top    = Math.min(_y, startY);
			   var _width  = Math.abs(_x - startX);
			   var _height = Math.abs(_y - startY);
			   $selectBoxDashed.css({
				   left  : _left,
				   top   : _top,
				   width : _width,
				   height: _height
			   });
			   //  遍历容器中的选项，进行选中操作
			   $(selector).find('.chooseMan').each(function() {
				   var $item = $(this);
				   //var itemX_pos = $item.attr('offsetWidth') + $item.attr('offsetLeft');
				   //左下角位置坐标(相对于外部div)
				   var itemY_pos = $item.attr('offsetHeight') + $item.attr('offsetTop') + 60 - scrollTop;
				   //  判断 每个div 是否与选框有交集，添加选中的效果（ temp-selected ，在事件 mouseup 之后将 temp-selected 替换为 selected）
				   var condition1 = _top > ($item.attr('offsetTop') - scrollTop + 60 - _height);
				   var condition2 = _top < itemY_pos;
				   if (condition1 && condition2) {
					   $item.addClass('temp-selected');
				   } else {
					   $item.removeClass('temp-selected');
				   }
			   });
			   //  清除事件冒泡、捕获
			   clearBubble(eventMove);
		   });

		   $(document).mouseup(function() {
		       var $tempSelect = $(selector).find('.chooseMan.temp-selected');
			   $(selector).unbind('mousemove');
               $tempSelect.removeClass('temp-selected').addClass('cur').css("background-color","#ebf2ff");
               //清除掉选框
			   $selectBoxDashed.remove();
			   //处理选中的div
               $tempSelect.each(function () {
                   var $itemDiv = $(this);
                   var dataVal = $itemDiv.attr("dataval");
                   var isDep = $itemDiv.attr("isdep");
                   $("#right").find("option[value='"+dataVal+"'][isdep='"+ isDep +"']").attr("selected","selected");
               });
			   if (selectCallback) {
				   selectCallback();
			   }
		   });
		})
		};

	initSelectBox('.showchoiceDiv');

	//双击操作
	function move2Right(obj) {
		var $item = $(obj);
		$item.addClass("selectedNode");
		moveIn(obj);
		$("#sonFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
		$item.removeClass("selectedNode");
        $("#choiceId").val("");
        $("#choiceName").val("");
	}

	//单击操作
	function move2RightByClick(obj) {
		var $item = $(obj);
		if($item.hasClass("selectedNode")){
			$item.removeClass("selectedNode");
		}else {
			$item.addClass("selectedNode");
		}
        $("#choiceId").val("");
        $("#choiceName").val("");
		$("#sonFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
	}

   // 处理按纽 下一页 /
   function pageInfo(val){
       $("#prepage").attr("disabled",false);
       $("#nextpage").attr("disabled",false);
       var path = $("#pathUrl").val();
       var pageIndex =  parseInt($("#pageIndex").val());
       $("#prepage").attr("disabled",false);
       // 选择的类型 /
       var chooseType = $("#choose_Type").val();
       var choiceId = $("#choiceId").val();
       pageIndex = val === "pre" ? pageIndex - 1:pageIndex + 1;
       $("#pageIndex").val(pageIndex);
       $("#left").empty();
       // 方法名  /
       var methodName ;
       //用户查询群组的时候 表识 1 员工群组  2客户群组
       var type = $("#qztype").val();
       //用户查询群组的时候 表识 1 共享群组  0个人群组
       var shareType = $("#shareType").val();
       //套餐Id
       var tcCode = $("#tcCode").val();

       if(chooseType === "1"){
           methodName = "getEmployeeByDepId";
       }else if(chooseType === "2"){
           methodName = "getClientByDepId";
       }else if(chooseType === "3" || chooseType === "4" || chooseType === "5"){
           methodName = "getGroupUserByGroupId";
       }else if(chooseType === "7"){
           methodName = "getSignClientMember";
	   }
       //处理机构是否重复       isDep  1表示是员工机构      2表示是客户机构     3表示是群组/
       $.post(path+"/selectUserInfo.htm",
           {
               method:methodName,
               depId:choiceId,
               pageIndex:pageIndex,
               type:type,
               shareType:shareType,
               tcCode:tcCode
           },
           function(result){
           $("#pagecode").empty();
           if(result !== "" ){
               // 获取分页信息
               var arr = result.substring(0,result.indexOf("#")).split(",");
               //总页数
               var pageTotal = arr[0] === "" || arr[0] === undefined || arr[0] === "null" ? 0 : parseInt(arr[0]);
               //总记录数
               var pageRec = arr[1] === "" || arr[1] === undefined || arr[1] === "null" ? 0 : parseInt(arr[1]);
               //添加记录
               var memberList = eval("("+result.substring(result.indexOf("#")+1)+")");
               for (var i=0;i < memberList.length;i++) {

                   var memberType = chooseType === "3" ? getMemberType(parseInt(memberList[i].isDep)):"";
                   var hasPrefix = chooseType === "7" ? "c_" + memberList[i].udgId : memberList[i].udgId;

                   $("#left").append("<div class='memberList' onclick='move2RightByClick(this)' " +
                       "ondblclick='move2Right(this)' dataValue='"+ hasPrefix +"' isdep='"+ memberList[i].isDep +"'" +
                       " et='' mobile='"+ memberList[i].mobile +"'>"+ $.trim(memberList[i].userName) + memberType +"</div>");
               }
               $("#pagecode").html(pageIndex+"/"+pageTotal);
               // 当总页数等于当前页时 ，下一页置灰/
               if(pageTotal === pageIndex){
                   $("#nextpage").attr("disabled",true);
                   $("#nextpage").removeClass("btnHover");
               }
               // 当总页数等于首页时 ，前一页置灰/
               if(1 === pageIndex){
                   $("#prepage").attr("disabled",true);
                   $("#prepage").removeClass("btnHover");
               }
           }
       });
   }

   /*根据名字搜索 */
   function searchName() {
   		var pathUrl = $("#pathUrl").val();
       //选择类型查询  1.员工通讯录   2.客户通讯录  3.群组(员工+客户) 4.员工群组 5.客户群组 6.客户属性 7.签约用户
       var chooseType = $("#choose_Type").val();
       var searchName = $("#searchName").val();
	   $("#left").empty();
	   if(searchName == null || $.trim(searchName).length === 0) {
	   		if(chooseType === "8"){
                alert(getJsLocaleMessage('common','common_alert_0024'));
			}else {
                alert(getJsLocaleMessage('common','common_input_name'));
            }
		   return;
	   }
	   if(chooseType === "6"){
	   		clientFieldChange();
	   		return;
	   }
	   if(chooseType === "7"){
           signClientChange();
           return;
	   }
       if(chooseType === "8"){
           getOperatorByNameOrPhone(searchName);
           return;
       }
	   $.post(pathUrl+"/selectUserInfo.htm?method=getClientOrEmployeeByName", {
			   chooseType : chooseType,
			   searchName : searchName
		   }, function(result){
	   			hideBtn();
				$("#left").empty();
           var memberList = eval("("+result+")");
           for (var i=0;i<memberList.length;i++) {
               $("#left").append("<div class='memberList' onclick='move2RightByClick(this)' " +
                   "ondblclick='move2Right(this)' dataValue='"+ memberList[i].udgId +"' isdep='"+ memberList[i].isDep +"'" +
                   " et='' mobile='"+ memberList[i].mobile +"'>"+ $.trim(memberList[i].userName) +"</div>");
           }
	   });
   }

   function changeCss() {
   		$("#searchName").css("border","1px solid #06a972");
   }
   function clientFieldChange(obj) {
       var $obj = $(obj);
       //先删除之前所有的selectedGroup
       $(".selectedGroup").each(function () {
           $(this).removeClass("selectedGroup");
       });
       $obj.addClass("selectedGroup");
       if(obj !== undefined){
           var fieldId = $(obj).attr('fieldid');
       }else {
           var searchName = $("#searchName").val();
	   }
       $.post(pathUrl + "/selectUserInfo.htm?method=getCustFieldMember",{
				searchName : searchName,
				fieldId:fieldId,
				corpCode:corpCode
           },function(groupMember){
           $("#left").empty();
           var memberList = eval("("+groupMember+")");
			   for (var i=0;i<memberList.length;i++) {
                   $("#left").append("<div class='memberList' onclick='move2RightByClick(this)' " +
                       "ondblclick='move2Right(this)' dataValue='"+ memberList[i].field_Ref + "&" + memberList[i].id +"' isdep='9'>"+
					   $.trim(memberList[i].field_Value) +"</div>");
			   }
           }
       );
   }

   function signClientChange(obj) {
       var $obj = $(obj);
       //先删除之前所有的selectedGroup
       $(".selectedGroup").each(function () {
           $(this).removeClass("selectedGroup");
       });
       $obj.addClass("selectedGroup");
       var pageIndex = 1;
       if(obj !== undefined){
           var tcCode = $(obj).attr('datavalue');
           $("#tcCode").val(tcCode);
       }else {
           var searchName = $("#searchName").val();
       }
       $.post(pathUrl + "/selectUserInfo.htm?method=getSignClientMember",
		   {
		   	searchName : searchName,
		    tcCode:tcCode,
		    pageIndex:pageIndex
		   },
		   function (result) {
               $("#left").empty();
               if(result === ""){
       				return;
				}
               var memberList = eval("("+result.substring(result.indexOf("#")+1)+")");
               //获取分页信息
               var arr = result.substring(0,result.indexOf("#")).split(",");
               //总页数
               var pageTotal = parseInt(arr[0]);
               for (var i=0;i<memberList.length;i++) {
                   $("#left").append("<div class='memberList' onclick='move2RightByClick(this)' " +
                       "ondblclick='move2Right(this)' dataValue='c_"+ memberList[i].udgId +"' isdep='"+memberList[i].isDep+"' mobile='"+memberList[i].mobile+"'>"+
                       $.trim(memberList[i].userName) +"</div>");
               }
               $("#pagecode").html(pageIndex+"/"+pageTotal);
               $("#prepage").attr("disabled",true);
               if(pageTotal === 1){
                   $("#prepage").css("visibility","hidden");
                   $("#nextpage").css("visibility","hidden");
               }
       });
   }

   function getOperatorByNameOrPhone(nameOrPhone) {
	   $.post(pathUrl+"/selectUserInfo.htm?method=getOperatorByNameOrPhone",
		   {
               nameOrPhone:nameOrPhone,
               corpCode:corpCode
	   		},function (result) {
               $("#left").empty();
               var memberList = eval("("+result+")");
               for (var i=0;i<memberList.length;i++) {
                   $("#left").append("<div class='memberList' onclick='move2RightByClick(this)' " +
                       "ondblclick='move2Right(this)' dataValue='"+ memberList[i].userId + "' isdep='11' et='' mobile='"+memberList[i].mobile+"'>"+
                       $.trim(memberList[i].name) +"</div>");
               }
           }
	   )
   }

	/**
	 * 点击添加按钮
	 */
	function addGroupMember() {
	   var addName = $("#addName").val().trim();
	   var addPhone = $("#addPhone").val();
	   if(addName === ""){
	   		alert(getJsLocaleMessage('common','common_alert_0025'));
	   		return;
	   }
	   if(addPhone === ""){
           alert(getJsLocaleMessage('common','common_alert_0026'));
           return;
	   }
	   if(addName.length > 0 && outSpecialChar(addName)){
           alert(getJsLocaleMessage('common','common_alert_0035'));
           $("#addName").val("");
           $("#addName").focus();
           return;
	   }
		if(addPhone.length<7 || addPhone.length>21) {
			alert(getJsLocaleMessage('common','common_alert_0027')) ;
            $("#addPhone").val("");
			$("#addPhone").focus();
            return;
		}
		$.post(pathUrl + "/common.htm",
			{
                method:"filterPh",
                tmp:addPhone
			},
			function (returnMsg) {
                if(returnMsg === "false") {
                    alert(getJsLocaleMessage('common','common_alert_0028'));
                    $("#addPhone").val("");
                    $("#addPhone").focus();
                }else {
                	//判断重复
					var $chooseMan = $("#getChooseMan").find("div[mobile='"+ addPhone +"']");
					if($chooseMan.html() !== "" && $chooseMan.html() !== null && $chooseMan.html() !== undefined){
                        alert(getJsLocaleMessage('common','common_alert_0029'));
                        $("#addPhone").val("");
                        $("#addPhone").focus();
					}else {
                        $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='groupEdit' " +
                            "dataval='"+ addName + "|" + addPhone +"' isdep='14' et='3' mobile='"+addPhone+"'>" +
                            "<span class='selectedName' title='"+addName+"'>"+ addName +"</span>" +
                            "<span class='selectedMobile'>"+ addPhone +"</span>" +
                            "<span class='selectedType'>自建</span>" +
                            "</div>");
                        var manCountStr = $("#manCount").html();
                        manCountStr = manCountStr === "" || manCountStr === undefined ? "0":manCountStr;
                        $("#manCount").html(parseInt(manCountStr) + 1);
                        var zjStr = $("#zjStr").val();
                        $("#zjStr").val(zjStr + addName+"|" + addPhone + ",");
                        $("#addPhone").val("");
                        $("#addName").val("");
					}
				}
        })
   }
   
   function getTypeStr(isdep,guid) {
       if(isdep === "4") {
           var ygStr = $("#ygStr").val();
           $("#ygStr").val(ygStr + guid + ",");
		   return "员工";
       }if(isdep === "5") {
           var ygStr = $("#ygStr").val();
           $("#ygStr").val(ygStr + guid + ",");
           return "客户";
       }if(isdep === "6") {
           var qzStr = $("#qzStr").val();
           $("#qzStr").val(qzStr + guid + ",");
		   return "自建";
       }if(isdep === "13") {
           var gxStr = $("#gxStr").val();
           $("#gxStr").val(gxStr + guid + ",");
           return "共享";
       }
       return "";
   }
   function getPhoneStr(isdep,mobile) {
       var hasRight = $("#plook").val();
       if(hasRight === "0") {
           if(isdep !== "6") {
               if(mobile.length === 11) {
                   return mobile.substr(0,3)+"*****"+mobile.substr(8,11);
               }else {
                   return mobile;
               }
           }
       }
       return mobile;
   }

//手机号码输入控制
function phoneInputCtrl(obj) {
	var $obj = $(obj);
    var val = $obj.val();
    var reg=/^(\+?)(\d*)$/g;
    if(!reg.test(val)){
        val = val.replace(/(\+?)(\d*)([^\d]*)/g,'$1$2');
        $obj.val(val);
    }
    else{
        if(val.length>21){
            val = val.substr(0,21);
            $obj.val(val);
        }
    }
}
function outSpecialChar(str){
    var reg = /[*_#:?<>|&.+^'";,!\-\]\\/@$%]+/;
    if(reg.test(str)){
        return true;
    }else{
        return false;
    }
}

var fileArray = new Array();
/*用于文件计数*/
var fileIndex = -1;
var fileNum = 1;
function uploadGroupFile() {
    if(fileIndex === 4) {
    	$("input [id=uploadFile]").val("");
        alert(getJsLocaleMessage('common','common_alert_0036'));
        return;
    }
    var pathValue = $("#uploadFile").val();
    var index = pathValue.lastIndexOf("\\");
    var fileName = pathValue.substring(index + 1);
    var fileType= fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
	if(fileArray.toString().indexOf(fileName)>-1){
		alert(getJsLocaleMessage('common','common_alert_0037'));
		$("input [id=uploadFile]").val("");
		return;
	}
    if (fileType != "txt" && fileType != "xls"&& fileType != "xlsx") {
        alert(getJsLocaleMessage('common','common_alert_0033')/*"上传文件格式错误，请选择txt或xls格式的文件。"*/);
        $("input [id=uploadFile]").val("");
        return;
    }
    var origName = "";
    if(fileName.length > 15){
        origName = fileName.substring(0,7)+"...";
    }else {
        origName = fileName;
    }
    $.ajaxFileUpload({
        url: pathUrl+'/selectUserInfo.htm?method=addGroupMemberByFile',
        secureuri:false,
        fileElementId: 'uploadFile',
        type:'POST',
        dataType: 'text',
        success: function (data) {
    	 var count = 0;
    	var dataValue="";
    	 if (!!window.ActiveXObject || "ActiveXObject" in window){ 
    		 dataValue=data;
         }else{ 
			 
		     /* var reg = /<pre.+?>(.+)<\/pre>/g;
		      data.match(reg);
		      data = RegExp.$1;
		      dataValue= $.trim(data);*/
		      dataValue=data;
         }
    	 
            if(dataValue === "" || dataValue === "[]" || dataValue === "null") {
                alert(getJsLocaleMessage('common','common_alert_0030')/*"上传文件内没有有效的数据！"*/);
            }else {
                var memberList = eval("("+dataValue+")");
                //校验是否超过2000条
                if(memberList.length > 2000){
                    alert(getJsLocaleMessage('common','common_alert_0031')/*"单个文件上传有效记录超过2000条，请重新选择文件上传！"*/);
                    return;
                }
                var zjStr = $("#zjStr").val();
                $(".remindMsg").hide();
                $("#etree").append("<div id='fileNum" + fileNum +"' class='showFile'>" +
                    "<span class='fileFlag'>[文件]</span>" +
                    "<span class='fileName' title='"+fileName+"'>"+ origName +"</span>" +
                    "<i class='delete_icon' onclick='deleteFileByIndex(this)'></i></div>");
                var str = "";
                for(var i = 0;i < memberList.length;i++){
                    var phoneAndName = memberList[i].name + "|" + memberList[i].mobile;
                    //校验是否存在
                    if(zjStr.indexOf(phoneAndName) > -1){
                        continue;
                    }
                    $("#getChooseMan").append("<div onclick='changeChooseDiv(this)' class='groupEdit fileNum" + fileNum + "' " +
                        "dataval='"+ phoneAndName +"' isdep='14' et='' mobile='"+memberList[i].mobile+"'>" +
                        "<span class='selectedName' title='"+memberList[i].name+"'>"+ memberList[i].name +"</span>" +
                        "<span class='selectedMobile'>"+ memberList[i].mobile +"</span>" +
                        "<span class='selectedType'>文件</span>" +
                        "</div>");
                    str += phoneAndName + ",";
                    count++;
                }
                $("#fileInputTemp").val($("#etree").html());
                fileArray[fileNum] = fileName;
                $("#zjStr").val(zjStr + str);
                var manCountStr = $("#manCount").html();
                manCountStr = manCountStr === "" || manCountStr === undefined ? "0":manCountStr;
                $("#manCount").html(parseInt(manCountStr) + count);
                fileIndex++;
                fileNum++;
                if(fileIndex === 4){
                    $("#etree").find("div:last").css("border-bottom","0");
                }
            }
            $("#uploadFile").val("");
        }
    });
}
function deleteFileByIndex(obj) {
    var count = 0;
    var zjStr = $("#zjStr").val();
    var $fileObj = $(obj);
    var manCount = $("#manCount").html();
    if(confirm(getJsLocaleMessage('common','common_alert_0032')/*"确认删除文件内所有的成员信息？"*/)){
        var fileFlag = $fileObj.parent("div").attr("id");
        var index = fileFlag.replace("fileNum","");
        fileArray[index] = "";
        fileIndex -= 1;
        $("#getChooseMan").find("div." + fileFlag).each(function () {
            var guid = $(this).attr("dataval");
            zjStr = zjStr.replace(guid + ",","");
            count++;
            $(this).remove();
        });
    }
    $fileObj.parent("div").remove();
    $("#manCount").html(parseInt(manCount) - count);
    $("#zjStr").val(zjStr);
    $("#fileInputTemp").val($("#etree").html());
    var len = $(".showFile").length;
    if(len === 0){
        $(".remindMsg").show();
    }
    if(len < 5 && len > 0){
        $("#etree").find("div:last").css("border-bottom","1px solid #e4dada");
    }
}
//全选所有的人员
function selectAllMem(obj) {
    var flag = $(obj).attr("val");
    $("#left .memberList").each(function () {
        //flag 为 1 则全选，0 则取消全选
        if(flag === "1") {
            //先删除后增加
            $(this).removeClass("selectedNode");
            $(this).addClass("selectedNode");
        }else if(flag === "0"){
            $(this).removeClass("selectedNode");
        }
    });
}
function isIE() {
    return !!window.ActiveXObject || "ActiveXObject" in window;
}