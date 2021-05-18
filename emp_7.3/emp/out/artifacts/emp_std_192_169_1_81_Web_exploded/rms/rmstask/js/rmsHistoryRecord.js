$(document).ready(function() {
    $("#content tbody tr").hover(function() {
        $(this).addClass("hoverColor");
    }, function() {
        $(this).removeClass("hoverColor");
    });
    $("#toggleDiv").toggle(function() {
        $("#condition").hide();
        $(this).addClass("collapse");
    }, function() {
        $("#condition").show();
        $(this).removeClass("collapse");
    });
    $('#search').click(function(){submitForm();});

    $('#sendstate').isSearchSelect({'width':'152','zindex':1,'isInput':false});
    $('#spUser').isSearchSelect({'width':'152','isInput':false,'zindex':0});
});
function showTempView(id) {
    var param = '?&id=' + id;
    layer.open({
        type: 2,
        title:getJsLocaleMessage("rms","rms_previewshow"),
        maxmin: true,
        shadeClose: true, //点击遮罩关闭层
        area : ['445px' , '700px'],
        content: 'toShowTemp.meditorPage'+param
    });
}