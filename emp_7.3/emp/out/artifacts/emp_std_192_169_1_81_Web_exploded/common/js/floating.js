

//调用父层的悬浮框
function floatingOnParent(id,id2)
{
	var isIE = (document.all) ? true : false;
    var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
    var topNum=0;
    if(isIE6)
    {
    	topNum=5;
    }
	$("#"+id).hover(
			function(){
				var top=$("#"+id).offset().top+$(this).height()-topNum;
				//18是设的padding,padding是不会算在宽度内的所以要加上
				var left=$("#"+id).offset().left+$(this).width()-parent.$("#"+id2).width()+18;
				
				$("#"+id).addClass("onSys");
				parent.$(".ifr").css("top",top+"px");
				parent.$(".ifr").css("left",left+"px");
				parent.$("#"+id2).css("top",top+"px");
				parent.$("#"+id2).css("left",left+"px");
				parent.$(".ifr").css("height",parent.$("#"+id2).height()+"px");
				parent.$(".ifr").css("width",parent.$("#"+id2).width()+"px");
				$(this).css("cursor", "pointer");
				parent.$(".ifr").show();
				parent.$("#"+id2).show();
			},
			function(){
				$("#"+id).removeClass("onSys");
				parent.$("#"+id2).hide();
				parent.$(".ifr").hide();
			}
	);
	
	parent.$("#"+id2).hover(
		
			function()
			{
				$("#"+id).addClass("onSys");
				parent.$(".ifr").show();
				parent.$("#"+id2).show();
			},
			function()
			{
				$("#"+id).removeClass("onSys");
				setTimeout(parent.$("#"+id2).hide(), 1000);
				setTimeout(parent.$(".ifr").hide(), 1000);
				//parent.$("#"+id2).hide();
				//parent.$(".ifr").hide();
			}	
	);
}

//余额下拉框专用（因为要实时显示余额）--------------延迟版
function floatingOnParentGetCt(id,id2)
{
	t=null;
	//鼠标进入余额范围
	$("#"+id).mouseenter(
			function(){
				//余额查询及显示，1为余额查询完毕。2为查询中
				var isEnd=$("#isEnd").val();
				//鼠标停留这为1
				$("#onTime").val("1");
				$("#"+id).css("cursor", "pointer");
				if(isEnd==1)
				{
					//设置为查询余额中
					$("#isEnd").val("2");
					//延迟请求，向后台发送查询余额的请求。
					setTimeout("getCtFloating('"+id+"','"+id2+"')", 2000);
				}
				else
				{
					//getCtHover(id,id2);
					setTimeout("getCtHover('"+id+"','"+id2+"')", 2000);
				}
				
			});
	//鼠标离开余额范围
	$("#"+id).mouseleave(function(){
				$("#"+id).removeClass("onSys");
				//鼠标停留则为2
				$("#onTime").val("2");
				//设置为不在查询中
				$("#isEnd").val("1");
				parent.$("#"+id2).hide();
				parent.$(".ifr").hide();
			}
	);
	
	parent.$("#"+id2).mouseenter(
		
			function()
			{
				$("#"+id).addClass("onSys");
				parent.$(".ifr").show();
				parent.$("#"+id2).show();
			});
			parent.$("#"+id2).mouseleave(function()
			{
				clearTimeout(t);
				t=setTimeout("sidedown('"+id2+"','"+id+"')", 300);
				//parent.$("#"+id2).hide();
				//parent.$(".ifr").hide();
			}	
	);
}

//延迟消失
function sidedown(id2,id)
{
	var onTime=$("#onTime").val();
	if(onTime==2)
	{
		$("#"+id).removeClass("onSys");
		parent.$("#"+id2).hide();
		parent.$(".ifr").hide();
	}
}

//延迟版
function getCtHover(id,id2)
{
	var onTime=$("#onTime").val();
	if(onTime==1)
	{
	
		var isIE = (document.all) ? true : false;
	    var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
	    var topNum=0;
	    if(isIE6)
	    {
	    	topNum=5;
	    }
		var top=$("#"+id).offset().top+$("#"+id).height()-topNum;
		var left=$("#"+id).offset().left+$("#"+id).width()-parent.$("#"+id2).width()+7;
		
		$("#"+id).addClass("onSys");
		parent.$(".ifr").css("top",top+"px");
		parent.$(".ifr").css("left",left+"px");
		parent.$("#"+id2).css("top",top+"px");
		parent.$("#"+id2).css("left",left+"px");
		parent.$(".ifr").css("height",parent.$("#"+id2).height()+"px");
		parent.$(".ifr").css("width",parent.$("#"+id2).width()+"px");
		$("#"+id).css("cursor", "pointer");
		if(t!=null)
		{
			clearTimeout(t)
		}
		parent.$(".ifr").show();
		parent.$("#"+id2).show();
		t=null;
	}
}

//调用当前页的悬浮框
function floating(id,id2)
{
	$("#"+id).hover(
			function(){
				var top=($("#"+id).offset().top+$(this).height())/2;
				var left=$("#"+id).offset().left+$("#"+id).width();
				//当窗口变小时则弹出框出现在右边
				var a = $(window).width();
				if(a < 835)
				{
					left = $("#"+id).offset().left - $("#"+id2).width();
				}
				$(".ifr").css("top",top+"px");
				$(".ifr").css("left",left+"px");
				$("#"+id2).css("top",top+"px");
				$("#"+id2).css("left",left+"px");
				$(".ifr").css("height",$("#"+id2).height()+"px");
				$(".ifr").css("width",$("#"+id2).width()+"px");
				//$(this).css("cursor", "pointer");
				$(this).css("text-decoration","underline");
				$(".ifr").show();
				$("#"+id2).show();
				
			},
			function(){
				$("#"+id2).hide();
				$(".ifr").hide();
				$(this).css("text-decoration","none");
			}
	);
	
	$("#"+id2).hover(
		
			function()
			{
				$(".ifr").show();
				$("#"+id2).show();
			},
			function()
			{
				$("#"+id2).hide();
				$(".ifr").hide();
			}	
	);
}

//调用当前页的悬浮框
function floating1(id,id2)
{
	
	$("#"+id).hover(
			function(){
				var top=$(this).offset().top+$(this).height()-2;
				var left=$(this).offset().left;
				
				
				$(".ifr").css("top",top+"px");
				$(".ifr").css("left",left+"px");
				$("#"+id2).css("top",top+"px");
				$("#"+id2).css("left",left+"px");
				$(".ifr").css("height",$("#"+id2).height()+"px");
				$(".ifr").css("width","98px");
				$(this).css("cursor", "pointer");
				$(".ifr").show();
				$("#"+id2).show();
			},
			function(){
				$(this).css("background","none");
				$("#"+id2).hide();
				$(".ifr").hide();
			}
	);
	
	$("#"+id2).hover(
			function()
			{
				$("#"+id).css("background","red");
				$(".ifr").show();
				$("#"+id2).show();
			},
			function()
			{
				$("#"+id).removeClass("onSys");
				$("#"+id2).hide();
				$(".ifr").hide();
			}	
	);
	
	
}

//调用父层的悬浮框插件写法
(function ($) {
	$.fn.myFloatingParent = function(options) { //定义插件的名称，这里为userCp
		var dft = {
			//以下为该插件的属性及其默认值
			divId: "uChildren", //悬浮框内容的div的id
			width: "98", //宽度
			align: "left",//显示的位置 1.靠左  2.靠右
			textAlign:"center",//内容的位置，left || center || right
			backColor:"#0B3E75",
			onBackcolor:"#8ad8fd"
			
		};
		var ops = $.extend(dft,options);
		var id=$(this).attr("id");
		
		$("#"+id).hover(
				function(){
					//框的宽度
					parent.$("#"+ops.divId).css("width",ops.width+"px");
					//框的背景色
					parent.$("#"+ops.divId).css("background",ops.backColor);
					//内容的位置
					parent.$("#"+ops.divId+" li a").css("text-align",ops.textAlign);
					
					$(this).css("background",ops.backColor);
					//距离头部位置
					var top=$(this).offset().top+$(this).height()-2;
					//距离左边的位置
					var left=$(this).offset().left;
					
					//靠右显示
					if(ops.align=="left")
					{
						left=$(this).offset().left+$(this).width()-ops.width;
					}
					
					//iframe的距头部的位置
					parent.$(".ifr").css("top",top+"px");
					//iframe的距左边的位置
					parent.$(".ifr").css("left",left+"px");
					//实体框距头部的位置
					parent.$("#"+ops.divId).css("top",top+"px");
					//实体框距左边的位置
					parent.$("#"+ops.divId).css("left",left+"px");
					parent.$(".ifr").css("height",parent.$("#"+ops.divId).height()+"px");
					parent.$(".ifr").css("width",ops.width+"px");
					$(this).css("cursor", "pointer");
					parent.$(".ifr").show();
					parent.$("#"+ops.divId).show();
				},
				function(){
					$(this).css("background","none");
					parent.$("#"+ops.divId).hide();
					parent.$(".ifr").hide();
				}
		);
		
		parent.$("#"+ops.divId).hover(
				function()
				{
					$("#"+id).css("background",ops.backColor);
					parent.$(".ifr").show();
					parent.$("#"+ops.divId).show();
				},
				function()
				{
					$("#"+id).removeClass("onSys");
					parent.$("#"+ops.divId).hide();
					parent.$(".ifr").hide();
				}	
		);
		
		//a的hover背景色改变，（用了会产生闪烁问题，但可以用延迟解决）
		/*parent.$("#"+ops.divId+" li a").hover(
				function()
				{
					$(this).css("background",ops.onBackcolor);
				},
				function()
				{
					$(this).css("background",ops.backColor);
				}
		);*/
		
	}
})(jQuery);

//调用当前页面的悬浮框插件写法
(function ($) {
	$.fn.myFloating = function(options) { //定义插件的名称，这里为userCp
		var dft = {
			//以下为该插件的属性及其默认值
			divId: "uChildren", //悬浮框内容的div的id
			width: "0", //宽度
			align: "left",//显示的位置，left || right
			textAlign:"center",//内容的位置，left || center || right
			backColor:"none",//背景色
			onBackcolor:"none",//鼠标经过a标签的颜色
			onTime: false,//是否鼠标悬停一定时间后才显示
			timeValue: "1000",//悬停时间
			idDelay: false,//是否能延迟
			delayTime: "1000",//延迟时间
			title: "",//提示
			paddingSize: 0,
			beforeShow: function(){}//显示之前执行的方法
		};
		var ops = $.extend(dft,options);
		var id=$(this).attr("id");
		if(ops.backColor!="none")
		{
			$("#"+ops.divId+" li a").css("background",ops.backColor);
		}
		var onTimeId="";
		//是否鼠标悬停一定时间后才显示
		if(ops.onTime)
		{
			onTimeId=id+"OnTimeId"
			var htmlStr="<input type='hidden' value='2' id='"+onTimeId+"'/> ";
			$("body").append(htmlStr);
		}
		
		t=null;
		$("#"+id).hover(
				function(){
					dft.beforeShow.apply();
					if(ops.onTime)
					{
						$("#"+onTimeId).val("1");
					}
					//提示
					if(ops.title!="")
					{
						$(this).attr("title",ops.title);
					}
					//若是配置了颜色
					if(ops.backColor!="none")
					{
						$("#"+ops.divId).css("background",ops.backColor);
						$("#"+ops.divId+" li a").css("text-align",ops.textAlign);
					}
					var width=$("#"+ops.divId).width();
					if(ops.width!=0)
					{
						$("#"+ops.divId).css("width",ops.width+"px");
						width=ops.width;
					}	
					
					var top=$(this).offset().top+$(this).height()-2;
					var left=$(this).offset().left;
					
					if(ops.align=="left")
					{
						left=$(this).offset().left+$(this).width()-width;
					}
					//如果设置了padding，就额外加上padding的值
					width=width+ops.paddingSize;
					$(".ifr").css("top",top+"px");
					$(".ifr").css("left",left+"px");
					$("#"+ops.divId).css("top",top+"px");
					$("#"+ops.divId).css("left",left+"px");
					$(".ifr").css("height",$("#"+ops.divId).height()+"px");
					$(".ifr").css("width",width+"px");
					$(this).css("cursor", "pointer");
					//如果设置了延迟
					if(ops.idDelay)
					{
						if(t!=null)
						{
							clearTimeout(t)
						}
						t=null;
					}
					
					//如果设置了鼠标悬停一定时间后才显示
					if(ops.onTime)
					{
						setTimeout("showFloating('"+id+"','"+ops.divId+"','"+onTimeId+"','"+ops.backColor+"')",ops.timeValue);
					}
					else
					{
						if(ops.backColor!="none")
						{
							$(this).css("background",ops.backColor);
						}
						$(".ifr").show();
						$("#"+ops.divId).show();
					}
					
				},
				function(){
					if(ops.onTime)
					{
						$("#"+onTimeId).val("2");
					}
					if(ops.backColor!="none")
					{
						$(this).css("background","none");
					}
					$("#"+ops.divId).hide();
					$(".ifr").hide();
				}
		);
		
		$("#"+ops.divId).hover(
				function()
				{
					if(ops.backColor!="none")
					{
						$("#"+id).css("background",ops.backColor);
					}
					$(".ifr").show();
					$("#"+ops.divId).show();
				},
				function()
				{
					//如果设置了延迟
					if(ops.idDelay)
					{
						clearTimeout(t);
						t=setTimeout("hideFra('"+id+"','"+ops.divId+"','"+onTimeId+"','"+ops.backColor+"')", ops.delayTime);
					}
					else
					{
						if(ops.onTime)
						{
								//$("#"+id).removeClass("onSys");
								//$("#"+ops.divId).hide();
								//$(".ifr").hide();
								clearTimeout(t);
								t=setTimeout("hideFra('"+id+"','"+ops.divId+"','"+onTimeId+"','"+ops.backColor+"')", 50);
						}
						else
						{
							if(ops.backColor!="none")
							{
								$("#"+id).removeClass("onSys");
							}
							$("#"+ops.divId).hide();
							$(".ifr").hide();
						}
					}
				}	
		);
		
		//如果设置了改变鼠标经过的背景色
		if(ops.backColor!="none")
		{
			$("#"+ops.divId+" li a").hover(
					function()
					{
						$(this).css("background",ops.onBackcolor);
					},
					function()
					{
						$(this).css("background",ops.backColor);
					}
			);
		}
		
	}
})(jQuery);

//延迟隐藏下拉框
function hideFra(id,id2,onTimeId,bacColor)
{
	if(onTimeId!="")
	{
		if($("#"+onTimeId).val()=="2")
		{
			if(bacColor!="none")
			{
				
				$("#"+id).removeClass("onSys");
			}
			$("#"+id2).hide();
			$(".ifr").hide();
		}
	}
	else
	{
		if(bacColor!="none")
		{
			
			$("#"+id).removeClass("onSys");
		}
		$("#"+id2).hide();
		$(".ifr").hide();
	}
}

//如果设置了鼠标悬停一定时间后才显示,先判断鼠标是否停留了指定的时间，才去决定是否显示
function showFloating(id,id2,onTimeId,backColor)
{
	var value=$("#"+onTimeId).val();
	if(value==1)
	{
		if(backColor!="none")
		{
			$("#"+id).css("background",backColor);
		}
		$(".ifr").show();
		$("#"+id2).show();
	}
}

//余额查询
function getCtFloating(id,id2)
{
	var onTime=$("#onTime").val();
	//如果鼠标停留在范围内，则进行后台查询
	if(onTime==1)
	{
		var path=$("#path").val();
		var userid = $("#userid").val();
		//若是1则表示查询结束那鼠标放上再次去后台查询，若是2表示查询还未返回值那鼠标放上则不去后台查询
		$.post(
			path+"/emp_tz.htm",{method:"getCt",lguserid:userid,isAsync:"yes",time: new Date().getTime()},
			function(result){
				
			//设置为查询完毕	
			$("#isEnd").val("1");
			if(result == "outOfLogin")
			{
				//登录超时，返回登录界面
				parent.document.getElementById("logoutalert").value = 1
	    		window.location.href=path+"/common/logoutEmp.jsp";
				return;
			}
			else{
				result=result.substr(result.indexOf("ye")+2);
				if(result.indexOf("ye")!=-1)
				{
					result=result.substr(result.indexOf("ye")+2);
				}
				var count=result.split(",");
				var regNum=/^[0-9]*$/; 
				//如果是数字返回true，其它返回false
				if(regNum.test(count[0])&&regNum.test(count[1]))
				{
					parent.$("#ctSms").html(count[0]);
					parent.$("#ctMms").html(count[1]);
					$("#hiddenYe").val(count[0]);
					$("#mmsBalance").val(count[1]);
				}
			}
			//展示余额下拉框
			getCtHover(id,id2);
		});			
	}
}
//针对id对象提示str信息
function floatingRemind(id,str)
{
	$("#"+id).hover(
			function(){
				var top=$(this).offset().top-$(this).height();
				var left=$(this).offset().left+$(this).width();

				$(".ifr").css("top",top+"px");
				$(".ifr").css("left",left+"px");
				
				$(".remindMessage").css("top",top+"px");
				$(".remindMessage").css("left",left+"px");

				$(".remindMessage").html(str);

				//$(this).css("cursor", "pointer");
				//$(this).css("text-decoration","underline");
				$(".remindMessage").show();
				$(".ifr").css("height",$(".remindMessage").height());
				$(".ifr").css("width",$(".remindMessage").width()+3+"px");
				
				$(".ifr").show();
			},
			function(){
				$(this).css("background","none");
				$(this).css("text-decoration","none");
				$(".ifr").hide();
				$(".remindMessage").hide();
			}
	);
}
