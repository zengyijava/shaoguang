var queryConstantType = {myTemp: 1, publicTemp: 2};
var isSysmanager = document.getElementById("isSysmanager").value;
var lang = document.getElementById("langName").value;

$(document).ready(function () {
    var isActive = $("#myTemp").hasClass('active');
    var frameSrc = 'toPoplist.meditorPage';
    if (!isActive) {
        $("#publicTemp").removeClass("active");
        $("#myTemp").addClass("active");
    }
    $("#cont").attr('data-param', 'type=my&auditStatus=1'+'&isSysmanager='+isSysmanager + '&lang=' + lang);
    $("#cont").attr("src", frameSrc);
});


function getTmpId(id) {
    alert(id);





    //预览
}

function selectTemp(id) {
    var frameSrc = 'toPoplist.meditorPage';
    if (id == "myTemp") {
        var isActive = $("#myTemp").hasClass('active');
        if (!isActive) {
            $("#publicTemp").removeClass("active");
            $("#myTemp").addClass("active");
        }
        $("#cont").attr('data-param', 'type=my&auditStatus=1'+'&isSysmanager='+isSysmanager + '&lang=' + lang);
    } else {
        var isActive = $("#myTemp").hasClass('active');
        if (isActive) {
            $("#myTemp").removeClass("active");
            $("#publicTemp").addClass("active");
        }
        $("#cont").attr('data-param', 'type=common&auditStatus=1'+'&isSysmanager='+isSysmanager + '&lang=' + lang)
    }
    $("#cont").attr("src", frameSrc);
}
