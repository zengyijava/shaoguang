var corpCodeList = $("#corpCodeList").val().split(',');
var corpNameList = $("#corpNameList").val().split(',');
var corp = {};

for(var i = 0;i < corpCodeList.length;i ++){
    corp[corpCodeList[i].trim()] = corpNameList[i];
}

function getAddcorpName(){
    var addcorpCode = $("#addcorpCode option:selected").val();
    $("#addcorpName").val("");
    $("#addcorpName").val(corp[addcorpCode]);
}

$(document).ready(function(){
    var pathUrl = $("#pathUrl").val()
    $('#btnSca').click(function() {
            window.location.href=pathUrl + "/surl_bind.htm?method=find";
        }
    );

    //复选框全选操作
    $("#all").click(function(){
        if (this.checked) {
            $(".domainclass :checkbox").attr("checked", true);
        } else {
            $(".domainclass :checkbox").attr("checked", false);
        }
    });

    $(".domainclass :checkbox").click(function(){
        if (!this.checked) {
            $("#all").attr("checked", false);
        }
    });

    $("#addcorpCode").change(function(){
        getAddcorpName()
    });

    getAddcorpName();

    $(".c_selectBox ul.c_result li").each(function () {
       $(this).click(function () {
           getAddcorpName();
       }); 
    });
    $("#btnSsu").click(function () {
        if(check()){
            document.forms["form4"].submit();
        }
    });
});