define(['jquery','util'],function($,util){
	
	//显示隐藏头像区域
	$('.show-others').delegate('.btnToggle','click',function(e){
		e.preventDefault();
		e.stopPropagation();
		$(this).addClass('ico-down').next('.dropdown-menu').toggle();
		
		$("#slide-other-list").nanoScroller({ preventPageScrolling: true });
		setTimeout(function(){$("#slide-other-list").nanoScroller();}, 300);

	});	

	//隐藏头像区域列表滑过状态
	$('#slide-others-group').delegate('li',{
		mouseenter:function(){
			$(this).addClass('hover').siblings().removeClass('hover');
		},
		mouseleave:function(){
			$(this).removeClass('hover')
		}
		
	});	
	
	//点击隐藏的任务头像,显示在顶部
	$('#slide-others-group').delegate('li','click',function(e){
		e.stopPropagation();
		var u=new util.util();
		u.choosePerson(this);
	})




});