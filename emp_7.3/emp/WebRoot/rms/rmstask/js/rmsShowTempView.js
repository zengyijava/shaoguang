
$(document).ready(function() {
    //0只给一个主数据  1给所有数据
    var previewType = 0;
    //需要预览的模板id
    //1显示标题，0不显示
    var title = 0;
    //1显示提示信息，0不显示
    var hint = 0;
    var lang = $("#langName").val();
    var param = 'previewType=' + previewType + '&id=' + $('#tmId').val() + '&title=' + title + '&hint=' + hint + '&lang=' + lang;
    $("#cont").addClass("J-vue-cont");
    $("#cont").attr('data-param', param);
    var frameSrc = 'toPreviewIndex.meditorPage';
    $("#cont").attr("src", frameSrc);
});