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
						    '<em>参数'+i+'</em>'+
						  '</a>'+
					 '</li>';
			}
			var sParam='<div class="showParams div_bd" style="'+scrollStr+'">'+
							'<ul>'+str+
								'<li><a href="javascript:void(0);" class="addParams">新增</a></li>'+
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
					$(self).html('关闭参数');
					$(opt.panel).slideDown();
					
				}else{
					$(self).html('打开参数');
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
								    '<em>参数'+i+'</em>'+
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
					oTextarea.selection('insert', {text: spa, mode: 'after'});
					//moveEnd(oTextarea[0]);
					oTextarea.val($.trim(oTextarea.val()));
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
