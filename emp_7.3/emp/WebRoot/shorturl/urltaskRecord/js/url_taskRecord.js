//机构树
var zTree3;
var setting3;
var zNodes3 = [];
//操作员树
var zTree2;
var setting2;
var zNodes2 = [];

function modify(t)
{
	$('#modify').dialog("option","title","信息内容");
	$("#msgcont").empty();
	//用label显示短信内容
	//$("#msgcont").text($(t).children("label").children("xmp").text());
	//修改成用textarea显示短信内容
	$("#msgcont").val($(t).children("textarea").val());
	$("#orUrl").attr("href",$(t).children("input").val());
	$('#modify').dialog('open');
}

function revtime(){
	var max = "2099-12-31 23:59:59";
    var v = $("#startSubmitTime").attr("value");
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max,enableInputMask:false});
};
	
function sedtime(){
	var max = "2099-12-31 23:59:59";
    var v = $("#endSubmitTime").attr("value");
    var min = "1900-01-01 00:00:00";
    WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:v});	
	
};

function reSend(id){
	$.post("surlTaskRecord.htm?method=isOverTimerTime",
			{id:id},
			function(result){
				
				
				if(result == "true"){
					reSend2(id,0);
				}else if(result == "timeOver"){
					if(confirm("定时时间小于过当前时间加10分钟，无法继续定时，确定要立即发送？")){
						reSend2(id,1);
					}else{
						return;
					}
				}else{
					alert("判断该任务是否是定时状态异常，补发失败");
				}
		});
	
	
}
//type:0，不修改定时时间；1，修改定时时间为立即发送
function reSend2(id,type){
	$.post("surlTaskRecord.htm?method=resend",
			{id:id,
			type:type
			},
			function(result){
				if(result == "true"){
					alert("重发成功");
					submitForm();
				}else if(result == "repeat"){
					alert("任务在处理中");
				}else{
					alert("重发失败");
				}
		});
	
}
$(document).ready(function() {
    closeTreeFun(["dropMenu2","dropMenu"]);
    getLoginInfo("#hiddenValueDiv");
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
    $('#search').click(function(){submitForm();});

    $('#modify').dialog({
        autoOpen: false,
        width:300,
        height:300
    });

    $('#failMessage').dialog({
        autoOpen: false,
        width:300,
        height:260
    });

    var lguserid =$("#lguserid").val();
    var lgcorpcode =$("#lgcorpcode").val();

    //机构树
    setting3.asyncUrl = "keep_record.htm?method=createDeptTree&lguserid="+lguserid;
    reloadTree(zNodes3);

    //操作员树
    //操作员树
    setting2.asyncUrl = "surl_surlSendedBox.htm?method=createUserTree2&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
    setting2.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
    zTree2 = $("#dropdownMenu2").zTree(setting2, zNodes2);
    zTree2.expandAll(true);
});

//获取机构代码
setting3 = {
    async : true,
    asyncUrl : "keep_record.htm?method=createDeptTree", //获取节点数据的URL地址

    //checkable : true,
    //checkStyle : "radio",
    //checkType : { "Y": "s", "N": "s" },
    isSimpleData : true,
    rootPID : 0,
    treeNodeKey : "id",
    treeNodeParentKey : "pId",
    asyncParam: ["depId"],

    callback: {

        click: zTreeOnClick3,
        asyncSuccess:function(event, treeId, treeNode, msg){
            if(!treeNode){
                var rootNode = zTree3.getNodeByParam("level", 0);
                zTree3.expandNode(rootNode, true, false);
            }
            //zTree3.expandAll(false);
        }
    }
};

setting2 = {
    checkable : true,
    checkStyle : "checkbox",
    checkType : { "Y": "s", "N": "s" },
    async : true,
    asyncUrl : "surl_surlSendedBox.htm?method=createUserTree2", //获取节点数据的URL地址
    isSimpleData: true,
    rootPID : 0,
    treeNodeKey: "id",
    treeNodeParentKey: "pId",
    asyncParam: ["depId"],
    callback: {
        change: zTreeOnClick2,
        asyncSuccess:function(event, treeId, treeNode, msg){
            if(!treeNode){
                var rootNode = zTree2.getNodeByParam("level", 0);
                zTree2.expandNode(rootNode, true, false);
            }
        }
    }
};

//选中的人员显示文本框
function zTreeOnClick2(event, treeId, treeNode) {
    if (treeNode) {
        var zTreeNodes2=zTree2.getChangeCheckedNodes();

        var pops="";
        var userids = "";
        for(var i=0; i<zTreeNodes2.length; i++){
            pops+=zTreeNodes2[i].name+";";
            userids+=zTreeNodes2[i].id.replace("u","")+",";
        }
        $("#userName").attr("value", pops);
        $("#userid").attr("value",userids);
        if(zTreeNodes2.length==0){
            $("#userid").attr("value","");
            $("#userName").attr("value", "");
        }
    }
}

//选中的机构显示文本框
function zTreeOnClick3(event, treeId, treeNode) {
    if (treeNode) {
        var pops="";
        var depts ="";
        $("#depNam").attr("value", treeNode.name);
        $("#deptid").attr("value",treeNode.id);

    }
}
// 加载人员/机构树形控件
function reloadTree(zNodes3) {
    setting3.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
    zTree3 = $("#dropdownMenu").zTree(setting3, zNodes3);
    zTree3.expandAll(true);
}
//选中的机构显示文本框
function zTreeOnClickOK3() {
    hideMenu();
}
//隐藏人员树形控件
function hideMenu() {
    $("#dropMenu").hide();
}
function cleanSelect_dep3()
{
    var checkNodes = zTree3.getCheckedNodes();
    for(var i=0;i<checkNodes.length;i++){
        checkNodes[i].checked=false;
    }
    zTree3.refresh();
    $("#depNam").attr("value","请选择");
    $("#deptid").attr("value","");
}

function showMenu() {
    hideMenu2();
    $("#dropMenu").toggle();
}

//隐藏机构树形控件
function showMenu2() {
    hideMenu();
    $("#dropMenu2").toggle();
}
//选中的机构显示文本框
//选中的人员显示文本框
function zTreeOnClickOK2() {

    hideMenu2();
    var zTreeNodes2=zTree2.getChangeCheckedNodes();

    var pops="";
    var userids ="";
    for(var i=0; i<zTreeNodes2.length; i++){

        pops+=zTreeNodes2[i].name+";";
        userids+=zTreeNodes2[i].id.replace("u","")+",";
    }
    $("#userName").attr("value", pops);
    $("#userid").attr("value",userids);
    if(zTreeNodes2.length==0){
        $("#userid").attr("value","");
        $("#userName").attr("value", "请选择");
    }

}

function cleanSelect_dep() {
    var checkNodes = zTree2.getCheckedNodes();
    for(var i=0;i<checkNodes.length;i++){
        checkNodes[i].checked=false;
    }
    zTree2.refresh();
    $("#userid").val("");
    $("#userName").val("请选择");
}

//隐藏人员树形控件
function hideMenu2() {
    $("#dropMenu2").hide();
}

function checkFails(id) {
    $.ajax({
        url: "surlTaskRecord.htm?method=getFailDetails",
        type: "POST",
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({"id": id}),
        success: function (e) {
            handleJson(e);
        },
        error: function () {
            alert('获取详细信息失败！');
        }
    });
}

function handleJson(result) {
    if(result != null && result != "" && result != undefined) {
        var json = $.parseJSON(result);
        if(200 == json.code){
            if(200 == json.data.isOk) {
                $('#failDetails').html(json.data.result);
            }  else if(404 == json.data.isOk){
                $('#failDetails').html('未找到失败详细信息');
            } else if(500 == json.data.isOk) {
                $('#failDetails').html('服务器内部发生错误，请联系管理员！');
            }
            $('#failMessage').dialog('open');
        }
    }
}