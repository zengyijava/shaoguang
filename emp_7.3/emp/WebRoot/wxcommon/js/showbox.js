var showbox=(function(options){
    var src = options.src;
    var mode = options.mode;
    
    var paddd=window.parent;
    var docUrl = window.location.href;
    var indexp=0;
    while(paddd.location.href!=docUrl && indexp<10)
    {
        docUrl=paddd.location.href;
        paddd=paddd.parent;
        indexp=indexp+1;
    }
    var html = "<div class='sb_bg_div'>" +
    		"</div><div class='sb_box_div'>" +
    		"<span class='sb_close'></span>" +
    		"<iframe id='sb_frame' frameborder=0 src=''></iframe>" +
    		"</div>";
    var $ob = $(paddd.document).find('body');
    var posc = $ob.css("position");
    //$ob.css("position","relative");
    $ob.append(html);
    
    
    
    var $box = $ob.find("> .sb_box_div");
    var wh = $box.width();
    $box.css("left",($ob.width()-wh)/2+"px");
    
    // 计算弹出框的层级，需大于原先已有的层
    var zinde =11,z_in,z_in_num;
    $ob.children().each(function(){
        z_in = $(this).css("z-index");
        if(z_in != null && z_in != "auto" && z_in != "undefined")
        {
            z_in_num = parseInt(z_in);
            if(z_in_num > zinde)
            {
                zinde = z_in_num;
            }
        }
    });
    $ob.find(".sb_bg_div").css("z-index",zinde);
    $box.css("z-index",zinde+2);
    $box.find("> .sb_close").click(function(){
        $box.remove();
        $ob.find(".sb_bg_div").remove();
        //$ob.css("position",posc);
    });
    
    if(mode == 1 || mode == "1")
    {
        $box.addClass("mode1");
    }
    
    // 绑定鼠标移动事件
    var is_box_move=0;
    $box.mousedown(  
        function (event) { 
            is_box_move = 1;
            var abs_x = event.pageX - $(this).offset().left;  
            var abs_y = event.pageY - $(this).offset().top;  
            $(this).mousemove(function (event) {  
                    if (is_box_move == 1) {  
                        $box.css({'left':event.pageX - abs_x, 'top':event.pageY - abs_y});  
                    }  
                }  
            ).mouseup(  
                function () {  
                    is_box_move = 0;  
                }  
            );  
        }  
    ).mouseout(function(){
        is_box_move = 0;
    });  
    
    $box.find("#sb_frame")[0].contentWindow.location.href=src;
});