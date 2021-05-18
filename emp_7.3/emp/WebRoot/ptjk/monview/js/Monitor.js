var Monitor = (function(self, $) {
	var timeArr=[];
	self.config_init=function(){
		return {
			ball:'.mon-line-ball',
			warning_info:'.warning_info',
			server_info:'.server_info',
			sound:'.mp3',
			canvas:'#myCanvas',
			showVoice:'.showVoice',
			closeVoice:'closeVoice',
			mp3:'.mp3',
			mp3Url:ipath+'/media/flash/warning.mp3',
			errGif:ipath+'/images/err.gif',
			interface_monitor:'.interface-data .monitor',
			abnormalBgColor:'#ffbc07',
			malfunctionBgColor:'#ff4c4c',
			normalBgColor:'#67bb0b',
			actual:'.histogram .actual',
			actual_p:'.histogram .actual p',
			havesend:'.havesend',
			havesend_p:'.histogram .havesend p',
			nano:'.nano',
			slogan_l:'.slogan-l',
			slogan_m:'.slogan-m',
			slogan_r:'.slogan-r',
			chart_left_column:{
				count:8,
				monitor_H:46,
				type:'solid',
				container:'.chart-left-column .inner'
			},
			chart_main_column:{
				count:9,
				monitor_H:40,
				type:'dashed',
				container:'.chart-main-column .interface-data'
			},
			chart_right_column:{
				count:9,
				monitor_H:40,
				type:'solid',
				container:'.chart-right-column .inner'
			},
			soundOption:{
				"backcolor": '',
		        "forecolor": "ffffff",
		        "width": "25",
		        "repeat": "false",
		        "volume": "100",
		        "autoplay": "true",
		        "showdownload": "false",
		        "showfilename": "false"
			}
		}

	};
	
	
	self.init=function(lineBgColor){
		self.lineBgColor=lineBgColor;
		self.config=self.config_init();
		//去除中间部分 填充数据的速度显示
		$(self.config.interface_monitor).each(function(){
			if($(this).hasClass("noData")){
				$(this).find(".mon-rate").remove();
			}
		})
		self.startSound();	
		//初始化色块高度自适应
		self.adaptive(self.config.chart_left_column);
		self.adaptive(Monitor.config.chart_main_column);
		self.adaptive(Monitor.config.chart_right_column);
		//初始化警告信息滚动
		self.msgRoll(self.config.warning_info);
		//判断待发数量
		self.isHistogram();
		//事件初始化
		self.Events.init();
		//停止速度为0的通道球 并且不显示速度为0的发送速度
		self.noRateState(".chart-left-column .monitor");
		self.noRateState(".chart-right-column .monitor");
		
		
		$(self.config.server_info).find('dl:last').addClass('bb_none');
		
	};

	/******JQ事件绑定******/
	self.Events={
		init:function(){
			//点击播放与关闭警报声音
			this.showVoice();
			//初始化加载虚线
			this.drawLine();
			//初始化加载动画
			this.clearBallAnimate();
			this.ballAnimate(self.config.ball);
			//鼠标滑过报警信息暂停动画
			this.stopWarningAnmite();
			//模拟滚动条
			this.scrollfn();
			this.slogan();
			this.lineColorSetting();
			this.wordSetting();
		},
		//模拟滚动条
		scrollfn:function(){
			
			if(!ie6){
				$(document).find(self.config.nano).nanoScroller({ preventPageScrolling: true });
					setTimeout(function(){
					   $(document).find(self.config.nano).nanoScroller({ 
					    	preventPageScrolling: true,
				   			flash:true,
				    		flashDelay:3000});
				}, 30);
			}else{
				$('#rightcolumn .bd').removeClass('nano').addClass('ieSix');
			}
			
		},
		//文字设置
		wordSetting:function(){
			var textCount=$('.total_count').text().length;
			if(textCount>10){
				$('.total_count').css({'left':'7px'});
			}else{
				$('.total_count').css({'left':'15px'});
			}
		},
		//业务系统、EMP、运营商箭头初始化
		slogan:function(){
			$('.slogan .inner').each(function(){
				var slogan_width=$(this).width()-120,
					arrow_count=Math.floor(slogan_width/100),
					arrow_width=Math.floor(slogan_width/arrow_count)-1,
					str="";
				for(var i=0;i<arrow_count;i++){
					str+='<li class="slogan-arrow" style=width:'+arrow_width+'px></li>';
				}
				$('ul',this).html(str);
			})
		},
		//点击播放声音方法 
		showVoice:function(){
			//去掉由于多次异步调用导致图标出错
			$(document).undelegate(self.config.showVoice,'click');
			$(document).delegate(self.config.showVoice,'click',function(){
				if($(this).hasClass(self.config.closeVoice)){
					changeVoice(0);
					$(this).removeClass(self.config.closeVoice);
					self.startSound();	
				}else{
					changeVoice(1);
					self.startSound('off');
					$(self.config.mp3).html('');
					$(this).addClass(self.config.closeVoice);
				}
			})
		},
		//初始化虚线方法
		drawLine:function(){
			var myCanvas = $(self.config.canvas)[0];
			myCanvas.width = 32;
      		myCanvas.height = 380;
      		$(self.config.interface_monitor).each(function(i){
	      		var _height=parseInt($(this).height()),
	      			mon_height=(_height+2)*(i+1)-_height/2,
	      			bgColor;
	      		if($(this).hasClass('noData')){
	      			return false;
	      		}
	      		if($(".titleBar").children().eq(0).hasClass("malfunction")){
	      			bgColor=self.config.malfunctionBgColor;
	      		}else if($(this).hasClass('malfunction')){
	      			bgColor=self.config.malfunctionBgColor;
	      		}else{
	      			bgColor=self.config.normalBgColor;
	      		}
	      		var options={
	      			lineToX:32,
	      			lineToY:mon_height,
	      			strokeStyle:bgColor
	      		};
	      		self.createdashed(myCanvas,options);
	      	})
		},
		//圆点滚动动画
		ballAnimate:function(element){
			$(element).each(function(i){
				var bwidth=$(this).parent().width();
				
				var _this=this,speed,mon_rate=$(this).parent().parent().find('.mon-rate').attr('speed');
				mon_rate=(typeof mon_rate=='undefined') ? 0 : mon_rate;
				if(mon_rate==0){
					speed=5000;
				}else if(mon_rate<=500 && mon_rate>0){
					speed=3000;
				}else if(mon_rate<1000 && mon_rate>500){
					speed=2600;
				}else{
					speed=2000;
				}
				if(speed>0){
					var t=setInterval(function(){
						var left=typeof $(_this).css('left')=='undefined' ? 0 : parseInt($(_this).css('left'));
						
						if(left==0){
							$(_this).show().animate({'left':bwidth+'px'},speed);
						}else if(left==bwidth){
							$(_this).fadeOut();
							if($(_this).is(':hidden')){
								$(_this).css({'left':0});
							}
						}
					},200);
				}
				
				ballTimerArr.push(t);
			})
			
			
		},
		clearBallAnimate:function(){
			for(var i=0;i<ballTimerArr.length;i++){
				clearInterval(ballTimerArr[i]);
			}
			ballTimerArr=[];
		},
		//鼠标滑过报警信息暂停动画
		stopWarningAnmite:function(){
			$(self.config.warning_info).hover(function(){
				$(this).addClass('stopAnmite');
			},function(){
				$(this).removeClass('stopAnmite');
			})
		},
		
		//中间虚线随右侧实线同色
		lineColorSetting:function(){
			$('.chart-main-column .monitor').each(function(i){
				$('.mon-dashed',this).addClass(self.lineBgColor[i]);
			})
		}


	};

	/******公用方法******/
	 /*  Function:绘制虚线方法
	 *  Params:element-canvas元素  
	 *  Params:options-{strokeStyle:虚线色值,lineToX:终点x坐标,lineToY:终点y坐标}
     */  
	self.createdashed=function(element,options){
		var context =  element.getContext("2d");
        context.strokeStyle =options.strokeStyle;
        context.lineWidth = 1; 
        context.dashedLineTo(0, 179, options.lineToX, options.lineToY, 3);
        context.stroke();
	}
    
	/*  Function:播放声音方法
	 *  Params:element-div.mp3  
	 *  Params:options:config.soundOption
     */ 
	self.soundPlayer=function(element,options){
		var _options=self.config.soundOption;
		$.extend(true,{}, _options, options);
		element.html(self.config.mp3Url);
		element.jmp3(_options);
	};
	/*  Function:色块自适应方法
	 *  Params:element [chart_left_column.container]
     */ 
    self.adaptive=function(element){
    	var oMonitor=$(element.container).find('.monitor');
		var noDataCount=element.count-oMonitor.size();
		if(noDataCount>0){
			for(var i=0;i<noDataCount;i++){
				var template=self.monitorTemplate(element.type);
				if($('.monitor',element.container).size()==0){
					$(template).prependTo($(element.container));
				}else{
					$(template).insertAfter($(element.container).children('.monitor:last'));
				}
				
			}
		}
	}; 
	self.monitorTemplate=function(type){
		var lineType,hasNbsp;
		if(type=='dashed'){
			lineType='mon-dashed';
			hasNbsp='';
		}else{
			lineType='mon-line';
			hasNbsp='&nbsp;';
		}
		var template='<div class="monitor noData">'+
			'<div class="mon-body">'+
				'<i></i>'+
				'<ul>'+
					'<li class="weight"></li>'+
					'<li></li>'+
				'</ul>'+
			'</div>'+
			'<div class="mon-side">'+
				'<div class="mon-rate">'+hasNbsp+'</div>'+
				'<div class="lineBar">'+
					'<div class="'+lineType+'"></div>'+
				'</div>'+
			'</div>'+
		'</div>';
		return template;
	};
	/*  Function:警报信息滚动
	 *  
    */ 
    self.msgRoll=function(element){
    	var _content=$(element).find('.content');
    	if(!$(element).hasClass('stopAnmite')){
    		var oDl=$(element).find('dl:first'),
	    		dl_H=oDl.height();
    			oDl.animate({marginTop:-dl_H+'px'},600,function(){
					oDl.css('marginTop',0).appendTo(_content);
				})
    		
	    		var dl_H=$(element).find('dl:last').height();
		    	
			
		}
    	clearTimeout(rollTime);
		rollTime=setTimeout(function(){
			self.msgRoll(element);
			//self.Events.scrollfn();
		},8000);
    };
    //判断待高度 是否隐藏
	self.isHistogram=function(){
		
	};
	//开启警告播放
	self.startSound=function(option){
		var ismonvoice = $("#ismonvoice").val();
		var isHaveMon = $("#isHaveMon").val();

		if(option=='off'){
			clearInterval(timer);
		}else if( ismonvoice==0 && isHaveMon==1){
			//初始化加载警报声音
			
			timer=setInterval(function(){
				self.soundPlayer($(self.config.mp3));
			},5000);
		}
	};
	self.noRateState=function(element){
		$(element).each(function(){
			if($(this).find('i').hasClass('err')){
				$(this).find('i').remove();
				$('.lineBar',this).append('<i style="left:50%;" class="mon-line-ball err"><img src="'+self.config.errGif+'"></i>');
				$('.mon-rate',this).html('&nbsp;');
				
			}else{
				/*if($(this).find(".mon-rate").html()=='&nbsp;'){
					$(this).find("i").html('').removeClass("mon-line-ball");
				}*/
			}
		})
		
	};
	return self;
}(Monitor || {}, jQuery));

//声音告警切换
function changeVoice(state)
{
	var lguserid = $("#lguserid").val();
	var lgcorpcode = $("#lgcorpcode").val();
	
	$.ajax({
		url:"mon_mainMon.htm?method=changeVoice",
		method:"POST",
		async:false,
		data:{
			lgcorpcode:lgcorpcode,
			lguserid: lguserid,
			state:state,
			isAsync:"yes"
		},
		success:function(result){
			if(result == "outOfLogin")
			{
				$("#logoutalert").val(1);
				location.href=pathUrl+"/common/logoutEmp.html";
				return;
			}
			else if(result=="error"||result=="fail")
			{
				//alert("声音告警开关错误！");
			}
			else
			{
				$("#ismonvoice").val(state);
			}
		}
		
	});
	
}