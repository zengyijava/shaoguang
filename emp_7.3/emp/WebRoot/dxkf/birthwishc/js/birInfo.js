$(document).ready(function(){
    getLoginInfo("#hiddenValueDiv");
    // floatingRemind("addrType","灰色表示已绑定的机构，仍可选择");
    floatingRemind("addrType",getJsLocaleMessage('dxkf','dxkf_srzf_page6_bindedchoose'));
    // $("#addrType").html("客户通讯录");
    $("#addrType").html(getJsLocaleMessage('dxkf','dxkf_srzf_page6_khtxl'));
    $("#sonFrame").attr("src",$("#iPath").val() + "/bir_addrbookDepTree2.jsp?treemethod=getClientSecondDepJson1&url=bir_birthdaySendClient.htm");
    $("#left option:disabled").css('color', '#fffff');
    $("#left").dblclick(function(){
        moveLeft1();
        $("#treeFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
    });
    //初始化赋值
    $("#cliDepIdsStrs").val($(window.parent.document).find("#cliDepIds").val());
    $("#clientIds").val($(window.parent.document).find("#cliIds").val());
    var selectedUserOption = $(window.parent.document).find("#rightSelectedUserOption").val();
    $("#right").html(selectedUserOption);
    if(selectedUserOption !== undefined){
        selectedUserOption = selectedUserOption.replace(/option/gi, "li").replace(/value/g, "dataval");
    }
    $("#getChooseMan").html(selectedUserOption);
    var manCount = $(window.parent.document).find("#choiceNum").html();
    $("#manCount").html(manCount === undefined || manCount === "null" || manCount == null ? 0 : manCount);
    //分页
    var totalPage = Math.ceil($("#right option").length/100);
    if(totalPage > 0){
        $("#pageIndex2").val(1);
        $("#totalPage2").val(totalPage);
        $("#showPage2").html(1+"/"+totalPage);
    }
});

function handlePage(pageIndex) {
    //遍历right option，取pageIndex  pageSize固定为100
    var pageSize = 100;
    var pageindex = parseInt(pageIndex);
    $("#getChooseMan").empty();
    $("#right option").each(function(index,value) {
        if((index+1) <= pageSize * pageindex && (index+1) >= pageSize * (pageindex - 1) + 1){
            $("#getChooseMan").append("<li membertype=\""+ $(this).attr("membertype") +"\" dataval=\""+ $(this).val() +"\">"+ $(this).text() +"</li>");
        }
    });
    var totalPage = Math.ceil($("#right option").length/100);
    if(totalPage > 0){
        $("#pageIndex2").val(pageIndex);
        $("#totalPage2").val(totalPage);
        $("#showPage2").html(pageindex+"/"+totalPage);
    }else {
        $("#totalPage2").val("0");
        $("#pageIndex2").val("0");
        $("#showPage2").empty();
    }
}

//针对id对象提示str信息
function floatingRemind(id,str)
{
			$("#"+id).hover(
					function(){
						var top=$(this).offset().top+$(this).height();
						var left=$(this).offset().left+$(this).width();

						$(".ifr").css("top",top+"px");
						$(".ifr").css("left",left+"px");
						
						$(".remindMessage").css("top",top+"px");
						$(".remindMessage").css("left",left+"px");

						$(".remindMessage").html(str);

						$(".ifr").css("height","23px");
						$(".ifr").css("width",$(".remindMessage").width()+3+"px");
						
						$(this).css("cursor", "pointer");
						$(this).css("text-decoration","underline");
						
						$(".ifr").show();
						$(".remindMessage").show();
					},
					function(){
						$(this).css("background","none");
						$(this).css("text-decoration","none");
						$(".ifr").hide();
						$(".remindMessage").hide();
					}
			);
}

function router() {
    if($("#left").val() !== null && $("#left").val() !== "") {
        moveLeft1();
    }else{
        selectDepMemeber();
    }
}

function moveLeft1() {
    //新建一个数组
    var newMemberArray = [];
    if ($("#left option:selected").length > 0) {
        $("#left option:selected").each(function() {
            var newMember = {};
            newMember.id = $(this).val();
            newMember.val = $(this).text();
            newMember.membertype = 1;
            newMemberArray.push(newMember);
        });
        var hasExistMember = addNewMemberArray(newMemberArray);

        //实现分页
        var pageIndex = Math.ceil($("#right option").length/100);
        if(pageIndex === 0){
            pageIndex = pageIndex + 1;
        }
        if(hasExistMember){
            alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_198'));
        }
        handlePage(pageIndex);
    }
}

function treeLoseFocus() {
    $("#sonFrame").contents().find(".curSelectedNode").removeClass("curSelectedNode");
}

function fixWidth() {
    var len = $("#right option").length ;
    if( len != 0 ){
        $('#right').css("width","525");
        $("#rightDiv").css("overflow-x","scroll");
    }else{
        $('#right').css("width","204");
    }
}

function fixWidth2() {
		$("#rightDiv").css("overflow-x","hidden");    
}

function addNewMemberArray(newMemberArray) {
    var memberArray = [];
    var showName = "个人";
    var newMemberIds = "";
    var hasExistMember = false;
    var memberArrayLength = 0;
    if($("#right option").length > 0){
        //判断此次选择的人员是否已经存在
        $.each(newMemberArray,function(index,value){
            var newMemberId = value.id;
            var newMemberType = value.membertype;
            var flag = false;
            $("#right option").each(function(index,value) {
                var memberId = $(this).val();
                var memberType = $(this).attr("membertype");
                if(newMemberId === memberId && memberType == newMemberType){
                    hasExistMember = true;
                    flag = true;
                }
            });
            if(!flag){
                memberArray.push(value);
            }
        });
        if(memberArray){
            $.each(memberArray,function(index,value){
                newMemberIds += value.id + ",";
                $("#right").append("<option membertype=\""+ value.membertype +"\" value=\""+ value.id +"\">["+ showName +"] "+ value.val +"</option>");
            });
        }
        memberArrayLength = memberArray.length;
    }else {
        $.each(newMemberArray,function(index,value){
            newMemberIds += value.id + ",";
            $("#right").append("<option membertype=\""+ value.membertype +"\" value=\""+ value.id +"\">["+ showName +"] "+ value.val +"</option>");
        });
        memberArrayLength = newMemberArray.length;
    }
    //记录Id
    $("#clientIds").val($("#clientIds").val() + newMemberIds);
    //已选择人数
    var mamCount = parseInt($("#manCount").html()) + memberArrayLength;
    $("#manCount").html(mamCount);

    return hasExistMember;
}

function moveRight1() {
    var cliDepIdsStrs = $("#cliDepIdsStrs").val();
    var clientIds = $("#clientIds").val();
    var manCount = parseInt($("#manCount").html());
    if ($("#right option:selected").size() > 0) {
        $("#right option:selected").each(function() {
            if($(this).attr("membertype")==="1") {
                manCount--;
                clientIds = clientIds.replace($(this).val()+",","");
            }else if($(this).attr("membertype")==="3") {
                manCount -= parseInt($(this).attr("mcount"));
                cliDepIdsStrs = cliDepIdsStrs.replace("e"+$(this).val()+",","");
            }else if($(this).attr("membertype")==="2") {
                manCount -= parseInt($(this).attr("mcount"));
                cliDepIdsStrs = cliDepIdsStrs.replace($(this).val()+",","");
            }
            $(this).remove();
        });
        $("#manCount").html(manCount);
        var pageIndex = Math.ceil($("#right option").length/100);
        handlePage(pageIndex);
        $("#clientIds").val(clientIds);
        $("#cliDepIdsStrs").val(cliDepIdsStrs);
    }
}
function goLastPage2() {
    var pageIndex2 = $("#pageIndex2").val();
    if(pageIndex2 === "1") {
        alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_noprepage'));
        return;
    }
    $("#pageIndex2").val(parseInt(pageIndex2)-1);
    handlePage(parseInt(pageIndex2)-1);
}

function goNextPage2() {
    var pageIndex2 = $("#pageIndex2").val();
    var totalPage2 = $("#totalPage2").val();
    if(totalPage2 === "0" || totalPage2 === ""){
        alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page3_qingxuanzeperson'));
        return;
    }
    if(pageIndex2 === totalPage2) {
        alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_nonextpage'));
        return;
    }
    $("#pageIndex2").val(parseInt(pageIndex2)+1);
    handlePage(parseInt(pageIndex2)+1);
}

function goLastPage1() {
    var depId = $("#depId").val();
    var lgcorpcode = $("#lgcorpcode").val();
    var lguserid = $("#lguserid").val();
    var setupid = $(window.parent.document).find("#setupid").val();
    if(depId ==="") {
        // alert("请先选择机构！");
        alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_choosedept'));
        return;
    }
    var pageIndex1 = $("#pageIndex1").val();
    if(pageIndex1==="1") {
        // alert("第一页，没有上一页了！");
        alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_noprepage'));
        return;
    }
    $.post(pathUrl + "/bir_birthdaySendClient.htm",
        {method:"getDepAndEmpTree1"
            ,lguserid:lguserid,
            lgcorpcode:lgcorpcode,
            pageIndex1:pageIndex1,
            depId:depId,
            setupid: setupid,
            opType:"goLast"
        }, function(result) {
        $("#left").html(result);
        $("#pageIndex1").val(parseInt(pageIndex1)-1);
        $("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
    });
}

function goNextPage1() {
    var pathUrl = $("#pathUrl").val();
    var depId=$("#depId").val();
    var lgcorpcode = $("#lgcorpcode").val();
    var lguserid = $("#lguserid").val();
    var setupid = $(window.parent.document).find("#setupid").val();
    if(depId === "") {
        // alert("请先选择机构！");
        alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_choosedept'));
        return;
    }
    var pageIndex1 = $("#pageIndex1").val();
    var totalPage1 = $("#totalPage1").val();
    if(pageIndex1 === totalPage1) {
        // alert("已经最后一页了！");
        alert(getJsLocaleMessage('dxkf','dxkf_dxqf_page2_nonextpage'));
        return;
    }
    $.post(pathUrl + "/bir_birthdaySendClient.htm",
        {
            method:"getDepAndEmpTree1",
            lguserid:lguserid,
            lgcorpcode:lgcorpcode,
            pageIndex1:pageIndex1,
            depId:depId,
            setupid:setupid,
            opType:"goNext"
        }, function(result) {
        $("#left").html(result);
        $("#pageIndex1").val(parseInt(pageIndex1)+1);
        $("#showPage1").html($("#pageIndex1").val()+"/"+$("#totalPage1").val());
    });
}

function selectDepMemeber() {
    var pathUrl = $("#pathUrl").val();
    $("#selectDep").attr("disabled",true);
    var depId=$("#depId").val();
    var depname = $("#depName").val();
    if(depId === "") {
        alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_107'));
        $("#selectDep").attr("disabled",false);
        return;
    }
    var ismut = 0;
    if(confirm(getJsLocaleMessage('common','common_alert_0010'))){
        ismut=1;
    }
    //已经选择人数
    var manCount = parseInt($("#manCount").html());
    // 员工机构ID
    var cliDepIdsStrs = $("#cliDepIdsStrs").val();
    if(cliDepIdsStrs !== "" && cliDepIdsStrs.length > 0){
        //处理客户机构/
        $.post(pathUrl +"/selectUserInfo.htm",
            {
                method:"isClientDepContained",
                depId:depId,
                cliDepIds:cliDepIdsStrs
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
                if(result==="depExist") {
                    alert(getJsLocaleMessage('common','common_alert_0009'));
                    $("#selectDep").attr("disabled","");
                    return;
                }
                //处理该机构包含的子机构 操作, 并将选择好的子机构进行删除操作 /
                $.post(pathUrl+"/selectUserInfo.htm",
                    {
                        method : "isClientDepContaineDeps",
                        depId : depId,
                        cliDepIds : cliDepIdsStrs,
                        ismut : ismut
                    }, function(result2){
                        var depidstr = cliDepIdsStrs;
                        if(result2 === "nobody"){
                            //alert("该员工机构下没有员工！");
                            alert(getJsLocaleMessage('common','common_alert_0018'));
                            $("#selectDep").attr("disabled",false);
                            return;
                        }else if(result2 === "errer"){
                            alert(getJsLocaleMessage('common',''));
                            $("#selectDep").attr("disabled",false);
                            return;
                        }
                        var index = $("#right option").length;
                        //机构不包含子机构的操作
                        if(ismut === 0){
                            index++;
                            $("#cliDepIdsStrs").val(cliDepIdsStrs + depId +",");
                            $("#right").append("<option membertype=\"2\" value=\""+ depId +"\" mcount=\""+ result2 +"\">[机构] "+ depname +" ["+ result2 +"人]</option>");
                            $("#selectDep").attr("disabled",false);
                            manCount+=parseInt(result2);
                        }else if(result2.indexOf("notContains")===0){
                            index++;
                            //机构包含子机构但是右边没有其要删除的子机构的操作
                            $("#cliDepIdsStrs").val(cliDepIdsStrs+"e"+depId+",")
                            $("#right").append("<option membertype=\"3\" value=\""+ depId +"\" mcount=\""+ result2.substr(12) +"\">[机构] "+ depname +" (包含子机构)["+ result2.substr(12) +"人]</option>");
                            $("#selectDep").attr("disabled",false);
                            manCount+=parseInt(result2.substr(12));
                        }else {
                            index++;
                            var strArr = result2.split(",");
                            $("#cliDepIdsStrs").val(cliDepIdsStrs+"e"+depId+",");
                            $("#right").append("<option membertype=\"3\" value=\""+ depId +"\" mcount=\""+ strArr[0] +"\">[机构] "+ depname +" (包含子机构)["+ strArr[0] +"人]</option>");
                            $("#selectDep").attr("disabled",false);
                            manCount += parseInt(strArr[0]);
                            for(var i=1;i < strArr.length;i=i+1) {
                                var containId = $.trim(strArr[i]);
                                var $aaa = $("#right").find("option[value='"+containId+"']");
                                if($aaa.attr("membertype")==="3") {
                                    depidstr = depidstr.replace("e"+$aaa.val()+",","");
                                }else if($aaa.attr("membertype")==="2") {
                                    depidstr = depidstr.replace($aaa.val()+",","")
                                }
                                manCount = manCount - $aaa.attr("mcount");
                                $aaa.remove();
                                index--;
                            }
                            $("#cliDepIdsStrs").val(depidstr);
                        }
                        $("#manCount").html(manCount);
                        var pageIndex = Math.ceil(index/100);
                        if(pageIndex === 0) {
                            pageIndex = pageIndex+1;
                        }
                        handlePage(pageIndex);
                    });
            });
    }else {
        //直接添加员工机构
        $.post(pathUrl + "/selectUserInfo.htm",{
            method : "getClientDepCount",
            depId : depId,
            ismut : ismut
        },function (result2) {
            var index = $("#right option").length;
            if(result2 === "nobody" || result2 === "0"){
                //alert("该员工机构下没有员工！");
                alert(getJsLocaleMessage('common','common_alert_0018'));
                $("#selectDep").attr("disabled","");
                return;
            }else if(result2 === "errer"){
                alert(getJsLocaleMessage('common','common_alert_0021'));
                $("#selectDep").attr("disabled",false);
                return;
            }
            if(ismut === 0){
                $("#cliDepIdsStrs").val(depId+",");
                $("#right").append("<option membertype=\"2\" value=\""+ depId +"\" mcount=\""+ result2 +"\">[机构] "+ depname +" ["+ result2 +"人]</option>");
                $("#selectDep").attr("disabled",false);
                manCount += parseInt(result2);
                index++;
            }else{
                var strArr = result2.split(",");
                $("#cliDepIdsStrs").val("e"+depId+",");
                $("#right").append("<option membertype=\"3\" value=\""+ depId +"\" mcount=\""+ strArr[0] +"\">[机构] "+ depname +" (包含子机构)["+ strArr[0] +"人]</option>");
                $("#selectDep").attr("disabled",false);
                manCount += parseInt(strArr[0]);
                index++;
            }
            $("#manCount").html(manCount);
            var pageIndex = Math.ceil(index/100);
            if(pageIndex === 0) {
                pageIndex = pageIndex+1;
            }
            handlePage(pageIndex);
        });
    }
}