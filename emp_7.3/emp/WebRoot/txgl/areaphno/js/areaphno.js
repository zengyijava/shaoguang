var skin = $("#skin").val();
function getcity(provinceName){
    var $c_result = $("#city").next(".c_selectBox").find("ul.c_result");
    $("#selectedType").val("");
    if(skin.indexOf("frame4.0") < 0){
        provinceName = $("#province").val();
    }
    if(provinceName === undefined || provinceName === "" || provinceName === "null"){
        return;
    }
    var selectedType=$("#selectedType").val();
    var citySelected=$("#city").val();
    $("#city").empty();
    $c_result.empty();
    $("#city").next(".c_selectBox").find(".c_input").val(getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_1"));
    $("#city").append("<option id='cityOption' value=''>"+getJsLocaleMessage("txgl","txgl_wgqdpz_qyhdgl_text_1")+"</option>");
    $.post("seg_areaPhoneNo.htm?method=addCity",
        {
            "province":provinceName,
            "selectedType":selectedType,
            "city":citySelected
        },
        function(data){
            if(data !== "" && data !== "null"){
                var dataList = data.split("&")[0];
                var selectedCity = data.split("&")[1];
                var citys = eval("("+dataList+")");
                for(var i = 0;i < citys.length;i++){
                    //c_result的li 用onmouseenter与onmouseout模拟hover事件
                    if(citys[i] === selectedCity){
                        $("#city").append("<option id='cityOption' selected value='"+citys[i]+"'>"+citys[i]+"</option>");
                        $c_result.append("<li class='' onmouseenter='addSelected(this)' onmouseout='removeSelected(this)' onclick='showCityName(this)'>"+citys[i]+"</li>");
                        $("#city").next(".c_selectBox").find(".c_input").val(citys[i]);
                    }else {
                        $("#city").append("<option id='cityOption' value='"+citys[i]+"'>"+citys[i]+"</option>");
                        //增加c_result的li 用onmouseenter与onmouseout模拟hover事件
                        $c_result.append("<li class='' onmouseenter='addSelected(this)' onmouseout='removeSelected(this)' onclick='showCityName(this)'>"+citys[i]+"</li>");
                    }
                }
                //c_result的高度根据内容自适应，最大300px
                var heightStr = $c_result.find("li").length * 24;
                $c_result.css("height",heightStr > 300 ? 300:heightStr + "px");
            }
        });
}
$(document).ready(function() {
    getLoginInfo("#hiddenValueDiv");
    $("#toggleDiv").toggle(function() {
            $("#condition").hide();
            $(this).addClass("collapse");
        },
        function() {
            $("#condition").show();
            $(this).removeClass("collapse");
        });
    getcity($("#provinceStr").val());
    $('#search').click(function(){
        $("#selectedType").val("find");
        submitForm();
    });
    $("#province").next(".c_selectBox").find("ul.c_result li").each(function () {
        var name = $(this).html();
        $(this).click(function () {
            getcity(name);
        });
    });
});

function addSelected(obj) {
    var $obj = $(obj);
    $obj.addClass("selected");
    $obj.addClass("div_bg");
}
function removeSelected(obj) {
    var $obj = $(obj);
    $obj.removeClass("selected");
    $obj.removeClass("div_bg");
}

function showCityName(obj) {
    var $obj = $(obj);
    $("#city").next(".c_selectBox").find(".c_input").val($obj.html());
    $("#city").next(".c_selectBox").find(".c_result ").hide();
    $("#city").val($obj.html());
}