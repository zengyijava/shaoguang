//操作员树
var zTree2;
var setting2;
var zNodes2 = [];
//机构树
var zTree;
var setting;
var zNodes = [];
$(document).ready(function(){
    //加载头文件内容
    getLoginInfo("#hiddenValueDiv");
    var lgcorpcode = GlobalVars.lgcorpcode;
    var lguserid = GlobalVars.lguserid;
    var reportType = $("#reportTypeStr").val();
    var findResult = $("#findResult").val();
    var sendType = $("#sendType").val();
    if(findResult === "-1") {
        // alert("加载页面失败,请检查网络是否正常!");
        alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_1"));
        return;
    }

    if (reportType === "1") {
        $("#countTime").attr("value",$("#tTime").val().substring(0,4));
        $("#titleYear1").css("display", "");
        $("#titleYear2").css("display", "");
        $("#titleYear3").css("display", "");
        $("#titleYear4").css("display", "");
        $("#titleTime1").css("display", "none");
        $("#titleTime2").css("display", "none");
        $("#titleTime3").css("display", "none");
        $("#titleTime4").css("display", "none");
    } else if(reportType === "0") {
        $("#countTime").attr("value",$("#tTime").val());
        $("#titleYear1").css("display", "");
        $("#titleYear2").css("display", "");
        $("#titleYear3").css("display", "");
        $("#titleYear4").css("display", "");

        $("#titleTime1").css("display", "none");
        $("#titleTime2").css("display", "none");
        $("#titleTime3").css("display", "none");
        $("#titleTime4").css("display", "none");
    }else{
        $("#titleYear1").css("display", "none");
        $("#titleYear2").css("display", "none");
        $("#titleYear3").css("display", "none");
        $("#titleYear4").css("display", "none");

        $("#titleTime1").css("display", "");
        $("#titleTime2").css("display", "");
        $("#titleTime3").css("display", "");
        $("#titleTime4").css("display", "");
    }

    $('#reportType').isSearchSelectNew(
        {'width':'180','zindex':0,'isInput':false},
        function(){$("#reportType").change();}
    );
    $("#reportType").change();
    $("#timeSelect div:nth-child(3)").hide();


    getLoginInfo("#corpCode");
    $("#toggleDiv").toggle(function() {
        $("#condition").hide();
        $(this).addClass("collapse");
    }, function() {
        $("#condition").show();
        $(this).removeClass("collapse");
    });
    $("#content tbody tr").hover(function() {
        $(this).addClass("hoverColor");
    }, function() {
        $(this).removeClass("hoverColor");
    });
    $('#userIds').isSearchSelect({'width':'180','zindex':0},function(data){
        //keyup click触发事件
        //if(data.value!='全部'){
        if(data.value!= getJsLocaleMessage("cxtj","cxtj_sjcx_yystjbb_text_5")){
            $("#userId").val(data.value);
        }else{
            $("#userId").val('');
        }

    },function(data){
        //初始化加载
        var val=$("#userId").val();
        if(val){
            data.box.input.val(val);
        }
    });

    $("#staffname").live("keyup blur",function(){
        var value=$(this).val();
        if(value!=filterString(value)){
            $(this).val(filterString(value));
        }
    });

    $("#reportType").change(function(){
        if ($("#reportType").attr("value") === "1") {
            $("#countTime").attr("value",$("#tTime").val().substring(0,4));
            $("#titleYear1").css("display", "");
            $("#titleYear2").css("display", "");
            $("#titleYear3").css("display", "");
            $("#titleYear4").css("display", "");
            $("#titleTime1").css("display", "none");
            $("#titleTime2").css("display", "none");
            $("#titleTime3").css("display", "none");
            $("#titleTime4").css("display", "none");
            $("#spisuncm").val("100");
        }
        else if($("#reportType").attr("value") === "0")
        {
            $("#countTime").attr("value",$("#tTime").val());
            $("#titleYear1").css("display", "");
            $("#titleYear2").css("display", "");
            $("#titleYear3").css("display", "");
            $("#titleYear4").css("display", "");
            $("#titleTime1").css("display", "none");
            $("#titleTime2").css("display", "none");
            $("#titleTime3").css("display", "none");
            $("#titleTime4").css("display", "none");
            $("#spisuncm").val("100");
            // $("#spisuncm2").find("option[text='100']").attr("selected",true);
        }else{
            $("#titleYear1").css("display", "none");
            $("#titleYear2").css("display", "none");
            $("#titleYear3").css("display", "none");
            $("#titleYear4").css("display", "none");
            $("#titleTime1").css("display", "");
            $("#titleTime2").css("display", "");
            $("#titleTime3").css("display", "");
            $("#titleTime4").css("display", "");
    /*            $("#sendtime").val('<%=beginTime%>');
                $("#recvtime").val('<%=endTime%>');*/
            $("#spisuncm").val("100");
        }
    });

    $('#search').click(function(){

        var sptype = $("#sptype").val();//数据源

        if(sptype==null||""==sptype)
        {
            //alert("您当前的角色未赋予查看[EMP应用、EMP接入]的数据权限");
            alert(getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_6"));
            return;
        }

        submitForm();
    });
    $("#tempDiv").dialog({
        autoOpen: false,
        height:460,
        width: 820,
        resizable:false,
        modal: true,
        open:function(){

        },
        close:function(){

        }
    });

    $("#sendInfoDiv").dialog({
        autoOpen: false,
        height:460,
        width: 820,
        resizable:false,
        modal: true,
        open:function(){

        },
        close:function(){
        }
    });

    var sendType1 = "";
    var sendType2 = "";
    var sendType3 = "";
    var sendType4 = "";
    switch (sendType) {
        case "1":
            sendType1 = "selected='selected'";break;
        case "2":
            sendType2 = "selected='selected'";break;
        case "3":
            sendType3 = "selected='selected'";break;
        case "4":
            sendType4 = "selected='selected'";break;
    }

    //账号类型
    $("#sptype").change(function(){
        if ($("#sptype").attr("value") === "0") {
            $("#sendtype").empty();
            $("#sendtype").append("<option value=''>"+getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_1")+"</option>");
            $("#sendtype").append("<option value='1' "+ sendType1 +">"+getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_2")+"</option>");
            $("#sendtype").append("<option value='2' "+ sendType2 +">"+getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_3")+"</option>");
            $("#sendtype").append("<option value='3' "+ sendType3 +">"+getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_4")+"</option>");
            $("#sendtype").append("<option value='4' "+ sendType4 +">"+getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_5")+"</option>");
        } else if($("#sptype").attr("value") === "1") {
            $("#sendtype").empty();
            $("#sendtype").append("<option value=''>"+getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_1")+"</option>");
            $("#sendtype").append("<option value='1' "+ sendType1 +">"+getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_2")+"</option>");
        }else if($("#sptype").attr("value") === "2"){
            $("#sendtype").empty();
            $("#sendtype").append("<option value=''>"+getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_1")+"</option>");
            $("#sendtype").append("<option value='2' "+ sendType2 +">"+getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_3")+"</option>");
            $("#sendtype").append("<option value='3' "+ sendType3 +">"+getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_4")+"</option>");
            $("#sendtype").append("<option value='4' "+ sendType4 +">"+getJsLocaleMessage("cxtj","cxtj_sjcx_zhtjbb_text_5")+"</option>");
        }
    });
    //操作员树
    setting2.asyncUrl = "rmsTaskUserAndDep.htm?method=createUserTree2&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
    setting2.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
    zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);
    zTree2.expandAll(true);

    //机构树
    setting.asyncUrl = "rmsTaskUserAndDep.htm?method=createDeptTree&lguserid="+lguserid;
    setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
    zTree = $("#dropdownMenu").zTree(setting, zNodes);
    zTree.expandAll(true);
});

function showDate() {
	var r = $("#reportType").attr("value");
	if (r == 0) {
		//WdatePicker({skin:'simple',dateFmt:'yyyy-MM',isShowClear:true});
		WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});
	} else if (r == 1) {
		//WdatePicker({skin:'simple',dateFmt:'yyyy',isShowClear:true});
		WdatePicker({dateFmt:'yyyy',isShowClear:false});
	}else{
		WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});
	}
}

function exportCondition(recordSize) {
    //if(confirm("确定要导出数据到excel?"))
    if(confirm(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_2"))) {
        if(recordSize !=null && recordSize > 0){
            var reportType = $("#reportType").val();
            $.ajax({
                type: "POST",
                url: "rep_spMtReport.htm?method=r_smRptSPExcel",
                data: {
                    reportType:reportType
                },
                beforeSend:function () {
                    page_loading();
                },
                complete:function () {
                    page_complete()
                },
                success:function (result) {
                    if (result === 'true') {
                        download_href("rep_spMtReport.htm?method=downloadFile");
                    } else {
                        //alert('导出失败！');
                        alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_3"));
                    }
                }
            });
        }else{
            //alert("无数据可导出！");
            alert(getJsLocaleMessage("cxtj","cxtj_sjcx_xtsxjl_text_4"));
        }
    }
}

var moreDimension;
var selectUser;
var selectDep;
/*更多维度*/
function showMoreDimension() {
    moreDimension = layer.open({
        type: 1,
        //skin: 'layui-layer-rim', //加上边框
        area: ['420px', '380px'], //宽高
        title: null,//不要标题
        offset: ['25%', '35%'], //居中
        closeBtn: 0, //不显示关闭按钮
        content: $('#moreDimensionDiv')
    });
}
function confirmMoreDimension() {
    layer.close(moreDimension);
}
function closeMoreDimension() {
    $("#userIdStr").val("");
    $("#userName").val("");
    $("#depIdStr").val("");
    $("#depName").val("");
    $("#svrType").val("-1");
    $("#araCode").val("-1");
    $("#DomesticOperator").val("-1");
    layer.close(moreDimension);
}

//获取操作员
setting2 = {
    checkable : true,
    checkStyle : "checkbox",
    checkType : { "Y": "s", "N": "s" },
    async : true,
    asyncUrl : "rmsTaskUserAndDep.htm?method=createUserTree2", //获取节点数据的URL地址
    isSimpleData: true,
    rootPID : 0,
    treeNodeKey: "id",
    treeNodeParentKey: "pId",
    asyncParam: ["depId"],
    callback: {
        change: showUserName,
        asyncSuccess:function(event, treeId, treeNode, msg){
            if(!treeNode){
                var rootNode = zTree2.getNodeByParam("level", 0);
                zTree2.expandNode(rootNode, true, false);
            }
        }
    }
};

//获取机构
setting = {
    async : true,
    asyncUrl : "rmsTaskUserAndDep.htm?method=createDeptTree", //获取节点数据的URL地址
    isSimpleData : true,
    rootPID : 0,
    treeNodeKey : "id",
    treeNodeParentKey : "pId",
    asyncParam: ["depId"],
    callback: {
        click: showDepName,
        asyncSuccess:function(event, treeId, treeNode, msg){
            if(!treeNode){
                var rootNode = zTree.getNodeByParam("level", 0);
                zTree.expandNode(rootNode, true, false);
            }
        }
    }
};

//选中的人员显示文本框
function showUserName(event, treeId, treeNode) {
    if (treeNode) {
        var zTreeNodes2=zTree2.getChangeCheckedNodes();
        var pops="";
        var userids = "";
        for(var i=0; i<zTreeNodes2.length; i++){
            pops+=zTreeNodes2[i].name+";";
            userids+=zTreeNodes2[i].id.replace("u","")+",";
        }
        $("#userName").attr("value", pops);
        $("#userIdStr").attr("value",userids);
        if(zTreeNodes2.length===0){
            $("#userIdStr").attr("value","");
            $("#userName").attr("value", "");
        }
    }
}

function showUserMenu() {
    selectUser = layer.open({
        type: 1,
        area: ['420px', '380px'], //宽高
        title: null,//不要标题
        offset: ['38%', '45%'], //居中
        content: $('#selectUser')
    });
}

//操作员->点击确定
function zTreeUserOnClickOK() {
    layer.close(selectUser);
    var zTreeNodes2=zTree2.getChangeCheckedNodes();
    var pops="";
    var userids ="";
    for(var i=0; i<zTreeNodes2.length; i++){
        pops+=zTreeNodes2[i].name+";";
        userids+=zTreeNodes2[i].id.replace("u","")+",";
    }
    $("#userName").attr("value", pops);
    $("#userIdStr").attr("value",userids);
    if(zTreeNodes2.length === 0){
        $("#userIdStr").attr("value","");
        $("#userName").attr("value", getJsLocaleMessage('common','common_pleaseSelect'));
    }
}
//操作员->点击清空
function zTreeUserOnClickClean() {
    var checkNodes = zTree2.getCheckedNodes();
    for(var i=0;i<checkNodes.length;i++){
        checkNodes[i].checked=false;
    }
    zTree2.refresh();
    $("#userIdStr").val("");
    var text = getJsLocaleMessage('common', 'common_pleaseSelect');
    $("#userName").val(text);
}

//选中的机构显示文本框
function showDepName(event, treeId, treeNode) {
    if (treeNode) {
        $("#depName").attr("value", treeNode.name);
        $("#depIdStr").attr("value",treeNode.id);
    }
}

//机构树形控件
function showDepMenu() {
    selectDep = layer.open({
        type: 1,
        area: ['420px', '380px'], //宽高
        title: null,//不要标题
        offset: ['44%', '45%'], //居中
        content: $('#selectDep')
    });
}
//机构->点击确定
function zTreeDepOnClickOK() {
    layer.close(selectDep);
}

//机构->点击清空
function zTreeDepOnClickClean() {
    var checkNodes = zTree.getCheckedNodes();
    for(var i=0;i<checkNodes.length;i++){
        checkNodes[i].checked=false;
    }
    zTree.refresh();
    $("#depName").attr("value", getJsLocaleMessage('common','common_pleaseSelect'));
    $("#depIdStr").attr("value","");
}
function setContain() {
    $("#containSubDep").val($("#isContainsSun").attr("checked") ? 1:0);
}