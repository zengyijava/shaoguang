$(document).ready(function()
{
    $('#appmsgItem1').find('.sub-msg-opa').css( {
        'height' : $('.cover').outerHeight() + 'px'
    });
    $('#appmsgItem1').find('.sub-msg-opa').find('.icostyle').css( {
        'margin-top' : '80px'
    });
    $('.sub-btn-add').bind('click', function()
    {
        cloneprevappmsgItem();
    })
    if('true' == $('#isMuitil').val())
    {
        $('.msg-editer-wrapper').css('height','397px');
    }
});
function createEditor(editorId)
{
    UM.getEditor(
        editorId,
        {
            toolbar : [ 'bold italic underline removeformat',
                    ' | fontsize forecolor backcolor | insertorderedlist insertunorderedlist image'],
            autoHeightEnabled : false,
            initialFrameWidth:350, //初始化编辑器宽度,默认500
            initialFrameHeight:150  //初始化编辑器高度,默认500
        }
    );
}
function iconeditClick(obj){
    // 左边区块高度
    var height=parseInt($('#msg-preview').css('height'));
    
   // $('.msg-editer-wrapper').css({'height':height-2+'px'});
    var content=$('.content').offset().top;
    var dest = $(obj).offset().top;
    var mt=dest-content;
    $(".msg-edit").find(".msg-edit-area").hide();
    var datarid = $(obj).parents(".appmsgItem").attr('data-rid');
    //$(".msg-edit").find(".msg-edit-area").hide();
    if(!$('#msg-edit-area'+datarid).length){
        currentEditDiv = clonemsgeditarea(datarid);
    }
    $('#msg-edit-area'+datarid).show();
    
    
    //$('.msg-edit-area').animate({'marginTop':mt+'px'});
    if(mt > 300)
    {
        $('.msg-arrow').css("top", '232px');
        $('#msg-edit-area' + datarid).css('margin-top',(mt-251)+"px");
        
    }else
    {
        $('.msg-arrow').css("top",mt+'px');
        $('#msg-edit-area' + datarid).css('margin-top','0');
    }
    // 重新计算外围iframe的高度
    setTimeout("window.parent.reinitHeight()", 200);

}

function appmsgItemOver(obj)
{
    $(obj).addClass('sub-msg-opa-show').siblings().removeClass(
            'sub-msg-opa-show');
}
function appmsgItemOut(obj)
{
    $(obj).removeClass('sub-msg-opa-show');
}
function appmsgItemRemove(obj)
{
    if ($('.msg-item-wrapper').children().length <= 4)
    {
        alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_56"));
        return false;
    }
    $(obj).parent().parent().remove();
    window.IE_UNLOAD = true;
    var datarid = $(obj).parents(".appmsgItem").attr('data-rid');
    var currentEditDiv = $(".msg-edit").find("#msg-edit-area" + datarid);
    $(currentEditDiv).remove();
    hideditother(datarid);
    $("#msg-edit-area1").show();
    $('.msg-arrow').css("top", '129px');
}
function hideditother(datarid)
{
    var itemIds = "";
    $('.msg-edit').find('.msg-edit-area').each(function()
    {
        var idStr = $(this).attr('id');
        itemIds += (idStr.replace("msg-edit-area", '') + ",");
    });
    var s = itemIds.split(",");
    for ( var i in s)
    {
        if (s[i] == datarid)
        {
            continue;
        }
        if (s[i] == 1)
        {
            continue;
        } else
        {
            $("#msg-edit-area" + s[i]).hide();
        }

    }
}
function renameIdandName(currentEditDiv, datarid)
{
    var $newDiv = $(currentEditDiv);
    $newDiv.find("#item-title").attr("id", "item-title" + datarid)
            .attr("name", "item-title" + datarid);
    $newDiv.find("#item-url").attr("id", "item-url" + datarid).attr(
            "name", "item-url" + datarid);
    $newDiv.find("#item-content")
            .attr("id", "item-content" + datarid).attr("name",
                    "item-content" + datarid);
    $newDiv.find("#item-source")
    .attr("id", "item-source" + datarid).attr("name",
            "item-source" + datarid);
    $newDiv.find("#item-link").attr("id", "item-link" + datarid)
            .attr("name", "item-link" + datarid);
    $newDiv.find("#item-img").attr("id", "item-img" + datarid).attr(
            "name", "item-img" + datarid);
    $newDiv.find("#eidtorDiv").html(
            '<script type="text/plain" id="myEditor' + 
            datarid + '" style="width:350px;height:200px;"></script>');
    createEditor('myEditor' + datarid);
    
    return currentEditDiv; 
}

function cloneprevappmsgItem()
{
    if ($('.msg-item-wrapper').children().length >= 10)
    {
        alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_57"));
        return false;
    }
    div = $('.msg-item-wrapper').children(':last').prev('div');
    var newdiv = div.clone();
    var dataid = parseInt(div.attr('data-rid')) + 1;
    newdiv.attr('data-rid', dataid);
    newdiv.find("span.default-tip").css( {
        height : 72,
        width : 72,
        display : 'block'
    }).show();
    newdiv.find("img.default-tip").attr( {
        src : ''
    }).hide();
    newdiv.find(".i-title").html(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_19"));
    newdiv.attr('id', 'appmsgItem' + dataid);
    div.after(newdiv);
    setTimeout("window.parent.reinitHeight()", 200);
}

function clonemsgeditarea(datarid)
{
    div = $("#msg-edit-area-hidden").children(':last');
  
    if ($(".msg-edit").find("#msg-edit-area" + datarid).size() < 1)
    {
        var newarea = div.clone();
        newarea.attr("id", "msg-edit-area" + datarid);
        $(".msg-edit").append(newarea);
    }
    var currentEditDiv = $(".msg-edit").find("#msg-edit-area" + datarid);
    currentEditDiv = renameIdandName(currentEditDiv, datarid);
    return currentEditDiv;
}
function showTitle(obj)
{
    var currentareaId = $(obj).parents(".msg-edit-area").attr('id');
    var datarid = currentareaId.replace("msg-edit-area", "");
    if (obj.value.length > 0)
    {
        var a = trun(obj.value);
        $("#appmsgItem" + datarid).find(".i-title").html(a);
    } else
    {
        $("#appmsgItem" + datarid).find(".i-title").html(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_19"));
    }

}
function setIputname(obj)
{
    // $(obj).prev().prev().val(obj.value);
    uploadImage(obj);
}
function trun(str)
{
    if (str == null)
        return "";
    var html = str;
    html = html.replace(new RegExp("&", "gm"), "&amp;");
    html = html.replace(new RegExp(" ", "gm"), "&nbsp;");
    html = html.replace(new RegExp("<", "gm"), "&lt;");
    html = html.replace(new RegExp(">", "gm"), "&gt;");

    return html;
}
