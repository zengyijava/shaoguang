$(document).ready(function () {
	/*//此方法已经在点击权限设置按钮是查询出了数据 ,此方法加载有问题，不用了
    $.ajax({
        url: "cor_manager.htm?method=getMultimedia_orig",
        type: "POST",
        dataType: "JSON",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({"corpCode": $("#corpCode").val()}),
        success: function (json) {
            handleJson(json);
        },
        error: function () {

        }
    });*/

    var value = $("#control input[type='radio']:checked").val();
    if(0 == value) {
    	rmsAuthclick();
    }
});

$("#control input[type='radio']").click(function () {
    var value = $("#control input[type='radio']:checked").val();
    if(1 == value) {

        $(".auth-list-div input[type='checkbox']").each(function () {
            $(this).show();
        });

        $('input[type="checkbox"]').each(function () {
            $(this).attr("checked", 'true');
        });
    } else {

        $(".auth-list-div input[type='checkbox']").each(function () {
            $(this).hide();
        });

        $('input[type="checkbox"]').each(function () {
            $(this).removeAttr("checked");
        });
    }
});

function rmsAuthclick(){
	var value = $("#control input[type='radio']:checked").val();
	if(1 == value) {
	
	    $(".auth-list-div input[type='checkbox']").each(function () {
	        $(this).show();
	    });
	
	    $('input[type="checkbox"]').each(function () {
	        $(this).attr("checked", 'true');
	    });
	} else {
	
	    $(".auth-list-div input[type='checkbox']").each(function () {
	        $(this).hide();
	    });
	
	    $('input[type="checkbox"]').each(function () {
	        $(this).removeAttr("checked");
	    });
	}
}

$(".auth-list-div > div > input[type='checkbox']").each(function () {

//    if($(this).attr('checked')) {
//        $(this).siblings(".supply-method-groups").find('input').each(function () {
//            // console.log($(this));
//            $(this).attr("checked", 'true');
//        });
//    }

    $(this).click(function () {

        if($(this).attr('checked')) {
            $(this).siblings(".supply-method-groups").find('input').each(function () {
                //console.log($(this));
                //console.log($(this).attr("id"));
            	//富媒体消息勾选时，不默认选择补充方式(短信)
            	if($(this).attr("id")!=="mediaTextCheckBox"){
                    $(this).attr("checked", 'true');
            	}
            });
        }else if(!$(this).attr('checked')) {
            $(this).siblings(".supply-method-groups").find('input').each(function () {
                //console.log($(this));
                $(this).removeAttr("checked");
            });
        }
    });
});

$(".auth-list-div > div > div > input[type='checkbox']").each(function () {

    // if($(this).attr('checked')) {
    //     $(this).siblings(".supply-method-groups").find('input').each(function () {
    //         // console.log($(this));
    //         $(this).attr("checked", 'true');
    //     });
    // }

    $(this).click(function () {
        if($(this).parent().siblings("input[type='checkbox']").attr("checked") == "false" ||
            $(this).parent().siblings("input[type='checkbox']").attr("checked") != "selected") {
            $(this).parent().siblings("input[type='checkbox']").attr("checked", 'true');
        }
    });



});


function sub() {
    //获取checkbox选中的值
//    var textCheckedArr = getCheckedVals("text");
//    var mediaCheckedArr = getCheckedVals("media");
//    var sceneCheckedArr = getCheckedVals("scene");
//    var h5CheckedArr = getCheckedVals("h5");


    var checkArr = new Array();

    var jsonArr = "";
    jsonArr += "[";
    
    if($("input[name='rmsTextMessage']").attr("checked")){
    	var textValue=$("input[name='rmsTextMessage']").val();
        var textCheckedArr = getCheckedVals("text");
        jsonArr+='{"type":'+textValue+',"suppStyle":['+textCheckedArr+']},';
    };
    if($("input[name='rmsMediaMessage']").attr("checked")){
    	var mediaValue=$("input[name='rmsMediaMessage']").val();
        var mediaCheckedArr = getCheckedVals("media");
        jsonArr+='{"type":'+mediaValue+',"suppStyle":['+mediaCheckedArr+']},';
    	
    };

    if($("input[name='rmsSceneMessage']").attr("checked")){
    	var sceneValue=$("input[name='rmsSceneMessage']").val();
        var sceneCheckedArr = getCheckedVals("scene");
        jsonArr+='{"type":'+sceneValue+',"suppStyle":['+sceneCheckedArr+']},';
    	
    };

    if($("input[name='rmsH5Message']").attr("checked")){
    	var h5Value=$("input[name='rmsH5Message']").val();
        var h5CheckedArr = getCheckedVals("h5");
        jsonArr+='{"type":'+h5Value+',"suppStyle":['+h5CheckedArr+']},';
    };
    
    if(","==jsonArr.substring(jsonArr.length-1, jsonArr.length)){
        jsonArr=jsonArr.substring(0, jsonArr.length-1);
    }
    jsonArr+= ']';

    var sceneObj =
        // console.log("sceneCheckedStr: " + sceneCheckedStr + "|mediaCheckedStr: " + mediaCheckedStr + "|textCheckedStr: " + textCheckedStr + "|h5CheckedStr: " + h5CheckedStr);
        //setMultimedia
        $.ajax({
            url: "cor_manager.htm?method=setMultimedia",
            type: "POST",
            dataType: "JSON",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                "corpCode": $("#corpCode").val(),
                "multimedia":$(".switch-module input[name='rmsAuth']:checked").val(),
                "modulePer":jsonArr
            }),
            success: function(data){
            	if(data=="true"){
                    //alert("配置成功！");
    				alert(getJsLocaleMessage("xtgl","xtgl_qygl_xjcg"));
    				//$('#buttonOk').attr("disabled",false);
    				//$('#buttonBack').attr("disabled",false);
    			
            	}else{
    				alert(getJsLocaleMessage("xtgl","xtgl_qygl_xjsb"));
            		
            	}
            	goBack();
            },
            error: function () {
				alert(getJsLocaleMessage("xtgl","xtgl_qygl_xjsb"));
				goBack();
            }
        });
    
    
}

function getCheckedVals(id) {
    //获取checkbox选中的值
    var checkedArr = new Array();
    $("#" + id + " input[type='checkbox']:checked").each(function() {
        checkedArr.push($(this).val());
    });
    return checkedArr;
}

function goBack() {
    var path = $("#path").val();
    var url = path + "/cor_manager.htm?method=find&pageIndex="+pageNum;
    url = encodeURI(encodeURI(url));
    location.href = url;
}

function getDataType(o) {
    if(o===null){
        return 'null';
    }
    else if(typeof o == 'object'){
        if( typeof o.length == 'number' ){
            return 'Array';
        }else{
            return 'Object';
        }
    }else{
        return 'param is no object type';
    }
}

function handleJson(result) {
    if(typeof(result) != "undefined") {
        var json = JSON.parse(result)
        if(json.code == 200) {
            var isauth = json.data.multimedia;
            // var sceneArr = json.data.modulePer.scene;
            // var mediaArr = json.data.modulePer.media;
            // var textArr = json.data.modulePer.text;
            // var h5Arr = json.data.modulePer.h5;

            if(1 == isauth) {
                $('#hasAuth').attr('checked', true);
            } else {
                $('#noAuth').attr('checked', true);
            }

            var modulePer = json.data.modulePer;
            if(getDataType(modulePer) == "Array" && modulePer.length > 0) {
                for(var i = 0; i < modulePer.length; i++) {
                    var module = modulePer[i];
                	//此处解析出错了，就不用了，还没解析到type
                    if(typeof (module.type) != "undefined" && module.type != "") {
                        if(module.type == 11) {
                            var mediaArr = module.suppStyle;
                            if(typeof (mediaArr) == Array && mediaArr.length > 0) {
                                if(-1 != $.inArray(14, mediaArr)) {
                                    $('#mediaTextCheckBox').attr('checked', true);
                                }
                            }

                        } else if(module.type == 12) {
                            var sceneArr = module.suppStyle;
                            if(getDataType(sceneArr) == "Array" && sceneArr.length > 0) {
                                if(-1 != $.inArray(11, sceneArr)) {
                                    $('#sceneMediaCheckBox').attr('checked', true);
                                }
                                if(-1 != $.inArray(14, sceneArr)) {
                                    $('#sceneTextCheckBox').attr('checked', true);
                                }
                            }

                        } else if(module.type == 13) {
                            var textArr = module.suppStyle;
                            if(getDataType(textArr) == "Array" && textArr.length > 0) {
                                if(-1 != $.inArray(14, textArr)) {
                                    $('#textCheckBox').attr('checked', true);
                                }
                            }
                        }

                    }
                }
            }

            /*
            if(typeof (sceneArr) != "undefined" && -1 != $.inArray('media', sceneArr)) {
                $('#textCheckBox').attr('checked', true);
            } else {
                // $('#textCheckBox').removeAttr('checked');
            }
            if(typeof (sceneArr) != "undefined" && -1 != $.inArray('shortMessage', sceneArr)) {
                $('#sceneTextCheckBox').attr('checked', true);
            } else {

            }
            if(typeof (mediaArr) != "undefined" && -1 != $.inArray('shortMessage', mediaArr)) {
                $('#mediaTextCheckBox').attr('checked', true);
            } else {

            }
            if(typeof (textArr) != "undefined" && -1 != $.inArray('shortMessage', textArr)) {
                $('#textCheckBox').attr('checked', true);
            } else {

            }
*/
        } else {

        }
    }

}