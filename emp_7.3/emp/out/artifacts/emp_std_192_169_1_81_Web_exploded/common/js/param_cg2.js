;(function(){
	$.fn.gotoParam=function(opt){
		opt=$.extend({
			box:'.paraContent',
			textarea:'#edit_area',
			panel:'.showParams',
			addPara:'.addParams',
			panel_ul:'.showParams ul',
			panel_li:'.showParams li',
			width:495
		},opt || {});
		return this.each(function(){
			var self=this,oBox=$(opt.box),oTextarea=$(opt.textarea),panel=$(opt.panel),scrollStr="";
			var boxW=$(opt.box).width();
			//var singleLineCount=Math.floor(boxW/55);
			if(boxW<=400){
				scrollStr=' overflow:auto;';
			}
			var str="";
			for(var i=1;i<16;i++){
				str+='<li><a href="javascript:void(0);">'+
						    '<em>'+getJsLocaleMessage('common','common_parameter')+i+'</em>'+
						  '</a>'+
					 '</li>';
			}
			var sParam='<div class="showParams div_bd" style="'+scrollStr+'">'+
							'<ul>'+str+
								'<li><a href="javascript:void(0);" class="addParams">'+getJsLocaleMessage('common','common_newlyIncrease')+'</a></li>'+
							'</ul>'+
							'<b class="arrowX div_bd"></b>'+
							'<b class="arrowX arrowX-out div_bd"></b>'+
						'</div>';
			if($(opt.panel).size()==0){
					$(opt.box).find('.tit_panel').after(sParam);
				}
			$(opt.panel).show();
			$(self).click(function(){
				
				if($(opt.panel).is(':hidden')){
					$(self).html(getJsLocaleMessage('common','common_closeParameter'));
					$(opt.panel).slideDown();
					
				}else{
					$(self).html(getJsLocaleMessage('common','common_openParameter'));
					$(opt.panel).slideUp();
				}
			})
			$(opt.addPara).live('click',function(){
				var liNum=$(opt.panel_li).size()-1;
				if(liNum==15 && boxW>=400){
					$(opt.panel).css({'height':'90px'});
				}
				if(liNum==23 && boxW>=400){
					$(opt.panel).css({'height':'120px'});
				}
				if(liNum<31){
					/*var copyLi=$(opt.panel_li).eq(0).clone(true);
					copyLi.find('em').html('参数'+(liNum+1));
					copyLi.insertBefore($(this).parent());*/
					//每次增加一行
					var str="";
					for(var i=liNum+1;i<liNum+9;i++){
						str+='<li><a href="javascript:void(0);">'+
								    '<em>'+getJsLocaleMessage('common','common_parameter')+i+'</em>'+
								  '</a>'+
							 '</li>';
					}
					$(this).parent().before(str);
				}
				
			})

			$(opt.panel_li).live('click',function(){
				var spa1=$(this).find('em').text();
				if(spa1){
					var spa='{#'+spa1+'#}';
				}
				    sText=oTextarea.val();
				if(spa){
					oTextarea.selection('insert', {text: spa, mode: 'before'});
					//if(navigator.userAgent.indexOf('Chrome')>-1)moveEnd(oTextarea[0]);
					//oTextarea.val($.trim(oTextarea.val()));
				}
				 

			})


		})
		
	}

})(jQuery)

function moveEnd(obj){
    obj.focus();
    var len = obj.value.length;
    if (document.selection) {
        var sel = obj.createTextRange();
        sel.moveStart('character',len);
        sel.collapse();
        sel.select();
    } else if (typeof obj.selectionStart == 'number' && typeof obj.selectionEnd == 'number') {
        obj.selectionStart = obj.selectionEnd = len;
    }
}

//IE下 文本域插入参数光标定位修复（在IE下当选择参数前后光标定位不到上次的正确位置 所以采用先保存位置）
if (!!window.ActiveXObject || "ActiveXObject" in window){
    $(document).ready(function(){

        //鼠标移动到参数项时 保存光标位置
        $('.showParams li').live('hover',function(){
            $('.contents_textarea').each(function(){
                var pos = $(this).selection('getPos');
                $(this).attr('start',pos.start);
            })
        })

        //键盘向上事件时 保存光标位置
        $('.contents_textarea').bind('keyup focus',function(){
            var pos = $(this).selection('getPos');
            $(this).attr('start',pos.start);
        });

        //文本域重新获取到焦点时 根据保存的值设置光标位置
        $('.contents_textarea').focusin(function(){
            var element = $(this)[0];
            if ($(this).attr('start') != undefined && element.createTextRange) {
                var range = element.createTextRange();
                var start = $(this).attr('start');
                start = element.value.substr(0, start).replace(/\r/g, '').length;
                range.moveStart('character', start);
                range.collapse(true);
                range.select();
            }
        });
    })
}
