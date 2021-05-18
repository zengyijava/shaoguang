$(function() {
		//1.从角标开始
		var mytop = 1;
		$('.main .center ol li').click(function(event) {
			if( $(this).attr('class') == undefined || $(this).attr('class') == '' ){

				mytop++;
			$(this).addClass('current').siblings().removeClass('current');
			//通过角标的编号找对应ul下的li，找到之后，让它定位到右侧 992；
			//然后让这个从右侧运动到左侧 0 
			var index= $(this).index();
			
			var num2 = 1573;
			if(index > num){
				num2 = 1573;
			}else{
				num2 = -1573;
			}
			$('.main .center .carousel ul').eq(index).css({
				left:num2,
				zIndex:mytop
			}).animate({left:0}, 500);
			console.log(mytop);

			num = index;


			}
		});
		//2.自动播放
		var num = 0;
		var timer = null;
		function autoPlay(){
			mytop++;
			console.log(mytop);
			num++;
			if(num>7){
				num=0;
			}
			$('.main .center ol li').eq(num).addClass('current').siblings().removeClass('current');
			//让ul下的li，淡入效果 fadeIn
			$('.main .center .carousel ul').eq(num).css({
				zIndex: mytop
			}).hide().fadeIn(500);
		}
		function prevPlay(){
			mytop++;//这个不能减，层级永远在最上层
			console.log(mytop);
			num--;
			if(num<0){
				num=7;
			}
			$('.main .center ol li').eq(num).addClass('current').siblings().removeClass('current');
			//让ul下的li，淡入效果 fadeIn
			$('.main .center .carousel ul').eq(num).css({
				zIndex: mytop
			}).hide().fadeIn(500);
		}
		timer = setInterval(autoPlay, 9000);
		//鼠标经过
		$('.center').mouseover(function(event) {
			clearInterval(timer);
		}).mouseout(function(event) {
			clearInterval(timer);
			timer = setInterval(autoPlay, 3000);
		});
		//点击向右 ，向左
		$('.right').click(function(event) {
			autoPlay();
		});
		$('.left').click(function(event) {
			prevPlay();
		});
	});
	
