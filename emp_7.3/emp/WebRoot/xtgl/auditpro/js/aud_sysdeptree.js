var path = $('#path').val();
var lguserid = $('#lguserid').val();
var pathtype = $('#pathtype').val();
var auditlevel = $('#auditlevel').val();
var flowid = $('#flowid').val();
var labelidstr = $('#labelidstr').val();


var zTree1;
var setting1;
setting1 = {
    async: true,
    checkable : true,
    checkStyle : "checkbox",
    checkType : { "Y": "", "N": "" },
    isSimpleData: true,
    rootPID : 0,
    treeNodeKey: "id",
    treeNodeParentKey: "pId",
    asyncParam: ["depId"],
    asyncUrl: path + "/aud_auditpro.htm?method=createdeptree&lguserid="+lguserid+"&pathtype="+pathtype+"&auditlevel="+auditlevel+"&flowid="+flowid+"&labelidstr="+labelidstr+"&time="+ new Date().getTime(),
    callback:{
        beforeChange:function(treeId,treeNode){
            if(!treeNode.checked && treeNode.parentNode == null && !treeNode.rootHasReview){
                alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_42"));
                return false;
            }
            //机构不存在审核人员 不允许添加
            if(!treeNode.checked && treeNode.hasReviewer != undefined && !treeNode.hasReviewer){
                alert(getJsLocaleMessage("xtgl","xtgl_spgl_shlcgl_42"));
                return false;
            }
        },
        change: zTreeOnClick,
        asyncSuccess:function(event, treeId, treeNode, msg)
        {
            if(!treeNode)
            {
                var rootNode = zTree1.getNodeByParam("level",0);
                zTree1.expandNode(rootNode,true,false);
            }
        },
        beforeAsync: function(treeId, treeNode) {
            zTree1.setting.asyncUrl=path + "/aud_auditpro.htm?method=createdeptree&depId="+treeNode.id+
                "&lguserid="+lguserid+"&pathtype="+pathtype+"&auditlevel="+auditlevel+"&flowid="+auditlevel+"&labelidstr="+labelidstr+"&labelidstr="+labelidstr+"&time="+ new Date().getTime();
        }
    }
};

//处理 选择的操作员
function zTreeOnClick(event, treeId, treeNode) {
    if(!treeNode.isParent){
        return;
    }
    //var zTreeuserNodes=zTree1.getChangeCheckedNodes();
    var zTreeuserNodes=zTree1.getCheckedNodes(true);
    var nameid= "";
    for(var i=0; i<zTreeuserNodes.length; i++){
        nameid = nameid + zTreeuserNodes[i].name + "_" + zTreeuserNodes[i].id + "#";
    }
    var divcount = $("#divcount").val();
    $(window.parent.document).find("#addsysdepstr"+divcount).val("");
    $(window.parent.document).find("#addsysdepstr"+divcount).val(nameid);
}


function zTreeBeforeAsync(treeId, treeNode) {
    if (treeNode.id == 1) return false;
    return true;
}

var zNodes =[];

$(document).ready(function(){
    setting1.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=7)?"":"fast";
    zTree1 = $("#tree").zTree(setting1, zNodes);
});

function returnZTree()
{
    return zTree1;
}
