
$(document).ready(function(){
    $('.div-panel-content #selectAll').click(function(){
       if($(this).attr('checked') == true || $(this).attr('checked') == 'checked') {
           $('.div-panel-content #field-div input[type="checkbox"]').attr('checked', true);
       } else {
           $('.div-panel-content #field-div input[type="checkbox"]').attr('checked', false);
           // 机构，操作员，SP账号默认显示，不可隐藏
           $('.div-panel-content #field-div input[type="checkbox"]').eq(0).attr('checked', true);
           $('.div-panel-content #field-div input[type="checkbox"]').eq(1).attr('checked', true);
           $('.div-panel-content #field-div input[type="checkbox"]').eq(2).attr('checked', true);
       }
    });

    var cookieName = $('#cookieName').val();

    generateHtmlByTh('#field-div', '#content thead tr');
    handleCookie(getCookie(cookieName));
    registerListeners();

});

/**
 * @func 动态添加html元素
 * @param containerId 添加html元素到该容器ID
 * @param thHtmlContent 基元素 '#content thead tr'
 */
function generateHtmlByTh(containerId, thHtmlContent) {
    if(null != thHtmlContent && "" != thHtmlContent && undefined != thHtmlContent) {
        var index = 0;
        var _html = '';
        $(thHtmlContent).find('th').each(function () {
            _html = _html + '<div class="chk-module"><input type="checkbox" id="chklist' + index + '" name="chklist'
                + index + '" value="' + index + '" checked="checked"/><span>' + $(this).text() + '</span></div>';
            index++;
        });
        if(null != containerId && "" != containerId && undefined != containerId) {
            $(containerId).html(_html);
        }

        // 机构，操作员，SP账号默认显示，不可隐藏
        $('#chklist0').attr('disabled', 'disabled');
        $('#chklist1').attr('disabled', 'disabled');
        $('#chklist2').attr('disabled', 'disabled');

    }
}

function registerListeners() {

    if($('#field-div div input[type="checkbox"]:checked').length == $('#field-div div input[type="checkbox"]').length) {
        $('.div-panel-content #selectAll').attr('checked', true);
    } else {
        $('.div-panel-content #selectAll').attr('checked', false);
    }

    $('#field-div div input[type="checkbox"]').each(function () {
        $(this).click(function () {
            if($('#field-div div input[type="checkbox"]:checked').length == $('#field-div div input[type="checkbox"]').length) {
                $('.div-panel-content #selectAll').attr('checked', true);
            } else {
                $('.div-panel-content #selectAll').attr('checked', false);
            }
        });
    });
}

function customShowPanel() {
    if($('.div-panel').css('display')=='none') {
        $("#foldIcon").addClass("pull-icon-fold");
        $("#foldIcon").removeClass("pull-icon");
    } else {
        $("#foldIcon").addClass("pull-icon");
        $("#foldIcon").removeClass("pull-icon-fold");
    }
    $(".div-panel").toggle();
}

function controlHide() {
    $(".div-panel").hide();
}

function controlDisplay(isShow) {
    var selectedVals = new Array();
    $('#field-div div input[type="checkbox"]').each(function () {
        var index = $(this).val();
        if($(this).attr('checked') == true || $(this).attr('checked') == 'checked') {
            $('#content th:eq('+index+')').css('display', 'table-cell');
            $('#content tbody tr').each(function() {
                $(this).find('td:eq('+index+')').css('display', 'table-cell');
            });
            selectedVals.push(index);
        } else {
            // console.log("选中的id: "+index + $('#content th:eq('+index+')').text());
            $('#content th:eq('+index+')').css('display', 'none');
            $('#content tbody tr').each(function() {
                $(this).find('td:eq('+index+')').css('display', 'none');
            });
        }
    });

    var cookieName = $('#cookieName').val();
    setCookie(cookieName, selectedVals, 30);
    if(isShow) {
        $(".div-panel").toggle();
    }
}

function setCookie(cookie_name, cookie_val, expire_day) {
    var nowDate = new Date();
    if(expire_day == undefined || expire_day == null && expire_day == "") {
        expire_day = 30;
    }
    nowDate.setTime(nowDate.getTime() + 24 * 60 * 60 * 1000 * expire_day);
    document.cookie = cookie_name + "=" + escape(cookie_val) + ";expires=" + nowDate.toGMTString();
}

function getCookie(cookie_name) {
    if (document.cookie.length > 0)
    {
        c_start = document.cookie.indexOf(cookie_name + "=")
        if (c_start != -1) {
            c_start = c_start + cookie_name.length + 1;
            c_end = document.cookie.indexOf(";",c_start);
            if (c_end == -1)
                c_end = document.cookie.length;
            return unescape(document.cookie.substring(c_start, c_end))
        }
    }
    return ""
}

function handleCookie(valStr) {
    if(null == valStr || undefined == valStr || "" == valStr) {
        return;
    }

    $('#field-div div input[type="checkbox"]').attr('checked', false);

    var vals = new Array();
    vals = valStr.split(',');

    $('#field-div div input[type="checkbox"]').each(function () {
        var index = $(this).val();
        for(var i = 0; i < vals.length; i++) {
            if(index == vals[i]) {
                $(this).attr('checked', true);
            }
        }
    });
    controlDisplay(false);
}