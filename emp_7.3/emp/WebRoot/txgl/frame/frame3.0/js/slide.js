;(function(){
	$.fn.empSlide=function(opt){
		opt=$.extend({
			_box  : "div.banner_box",
			_prev : $('#prev'),
			_next : $('#next'),
			_index: 0
		},opt || {});
		return this.each(function(){
			var len=$(opt._box).length,intervalTimer=null
			$(opt._box).stop(true,false).fadeOut(1000).eq(opt._index).stop(true,false).fadeIn(1000);
			showIndex(opt._index);
			opt._prev.bind('click',function(){
				if(!$(opt._box).is(':animated')){
					clearTimeout(intervalTimer);
					intervalTimer = setTimeout(function() {
			            opt._index-=1;
						if(opt._index==-1){
							opt._index=len-1;
						}
						showBanner(opt._index);
	       			 }, 300);
				}
				
				
			})
			opt._next.bind('click',function(){
				if(!$(opt._box).is(':animated')){
					clearTimeout(intervalTimer);
					intervalTimer = setTimeout(function() {
						opt._index+=1;
						if(opt._index==len){
							opt._index=0;
						}
						showBanner(opt._index);
					}, 300);
				}
			})
			function showBanner(_index){
				//依赖cookieUtil.js获取cookie
				//if(!$(opt._box).is(':animated')){
					cookieUtil.setCookie('banner',_index,365);
					showIndex(_index);
					$(opt._box).stop(true,false).fadeOut(1000);
					$(opt._box).removeClass('on').eq(_index).addClass('on').fadeIn(1000);
			//	}
					
				
				
			}
			function showIndex(_index){
				$('*').removeClass('start');
				if(_index==0){
					showAnimate(".an0");
				}
				if(_index==1){
					showAnimate(".an1");
				}
				if(_index==2){
					showAnimate(".an2");
				}
			}

			function showAnimate(dom,cname){
				var cname=cname ? cname : 'start';
				$(dom).addClass(cname);
			}

		})
	}
})(jQuery);

