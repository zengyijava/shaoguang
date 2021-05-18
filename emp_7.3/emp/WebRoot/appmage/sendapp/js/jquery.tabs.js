(function($){

	$.fn.tabs=function(control){
		var element=$(this);
		control=$(control);

		element.find('li').live('click',function(){
			//遍历选项卡名称
			var tabName=$(this).attr('data-tab');
			//在点击选项卡时触发自定义事件
			element.trigger('change.tabs',tabName);
		});

		//绑定自定义事件
		element.bind('change.tabs',function(e,tabName){
            if(!tabName){
                return;
            }
			element.find('li').removeClass('cur');
			element.find('>[data-tab="'+tabName+'"]').addClass('cur');
			element.find('>[data-tab="'+tabName+'"]').find('input').attr('checked','checked');
            control.find('>[data-tab]').removeClass('cur');
            control.find('>[data-tab="'+tabName+'"]').addClass('cur');

		});

		//激活第一个选项卡
		var firstName=element.find('li:first').attr('data-tab');
		element.trigger('change.tabs',firstName);

		return this;
	};
})(jQuery);