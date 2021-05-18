;(function(){
	$.fn.gotoParam=function(opt){
		opt=$.extend({
			box:'.paraContent',
			textarea:'#edit_area',
			panel:'.all_param',
			addPara:'.addPara',
			panel_ul:'.all_param ul',
			panel_li:'.all_param li',
			width:495
		},opt || {});
		return this.each(function(){
			var self=this,oBox=$(opt.box),oTextarea=$(opt.textarea),panel=$(opt.panel);
			var sParam='<div class="all_param" style="top: -50px;width:'+opt.width+'px;display:none;">'+
							'<div class="inner" style="width:'+opt.width+'px;">'+
								'<ul style="width:'+(opt.width-10)+'px;">'+
									'<li><a href="javascript:void(0);"><i></i><b>'+getJsLocaleMessage('dxzs','dxzs_ssend_alert_309')+'1</b></a></li>'+
									'<li><i class="addPara">+</i></li>'+
								'</ul>'+
								'<i class="bd1"></i>'+
								'<i class="bd2"></i>'+
							'</div>'+
						'</div>';
			$(self).click(function(){
				if($(opt.panel).size()==0){
					oBox.append(sParam);
				}
				if($(opt.panel).is(':hidden')){
					$(self).html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_297'));
					$(opt.panel).show();
				}else{
					$(self).html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_310'));
					$(opt.panel).hide();
				}
			})
			$(opt.addPara).live('click',function(){
				var liNum=$(opt.panel_li).size()-1,
					vTop=parseInt($(opt.panel).css('top')),
					copyLi=$(opt.panel_li).eq(0).clone(true);
				copyLi.find('b').html(getJsLocaleMessage('dxzs','dxzs_ssend_alert_309')+(liNum+1));
				copyLi.insertBefore($(this).parent());
				var liHeight=$(opt.panel).outerHeight();
				if(liHeight!=vTop){
					$(opt.panel).css({'top':-liHeight-20+'px'});
				}
			})

			$(opt.panel_li).live('click',function(){
				//var spa=$(this).find('b').text().replace(/参数/,'{#p_');
				//if(spa){
					//spa+='#}';  
				//}
				var spa1=$(this).find('b').text();
				if(spa1){
					var spa='{#'+spa1+'#}';
				}
				    sText=oTextarea.val();
				spa && oTextarea.val(sText+' '+spa);
			})


		})
	}

})(jQuery)