(function($){
	var TableSlide = {
		timer: null,
		intWaitTime: 1000,	// 初始等待时间
		currentIndex: 0,	// 当前聚集的index
		clickCount: 0,		// 点击次数统计，第一次点击才清除第一个上面的动画样式
		clickLock: false,	// 点击锁，防止频繁无意义操作
		// 初始化
		init: function(){
			this.childrenList = this.getAllListChildren();
			this.childrenListShowStart = 1;
			this.childrenListShowEnd = 5;
			this.childrenListEnd = this.childrenList.length;
			this.allInitRender();
			this.bindUI();
		},

		// 全部新闻首次渲染5条数据
		allInitRender: function(){
			var _listChildren = this.childrenList;

			$.each(_listChildren, function (key) {
				if(key < 5){
					if(key === 2){_listChildren[key].addClass("active")};
					if(key === 4){_listChildren[key].addClass("mar-right0")};
					$(".list-items").eq(0).append(_listChildren[key]);
				}
			});
		},

		// UI绑定操作事件
		bindUI: function(){
			var _self = this,
				_initTimer = null;
			// 第一个全部分类图片不能聚集
			$("#list-cont").on("mouseenter", ".items-li", function(){
				if($(this).parent().index() != 0){
					$(this).addClass("active").siblings().removeClass("active");
				}
			});

			// 主动切换Tab
			$("#list-table").on("click", "li", function(){
				var _clickTabIndex = $(this).index();

				if(_self.clickCount == 0){
					// 移除第一个上面所有css动画Class
					$(".list-items").eq(_self.clickCount).removeClass("delay5 animated bounceInRight");
				}
				_self.clickCount++;

				// 防止频繁点击
				if(!_self.clickLock && !$(this).hasClass("active")){
					_self.clickLock = true;

					// 是否显示前后切换按钮
					if(_clickTabIndex == 0){
						$(".list-check-icon").show();
					}else{
						$(".list-check-icon").hide();
					}

					// 根据当前点击元素下标和上一次点击元素下标对比判断动画方向
					if(_clickTabIndex > _self.currentIndex){
						_self.sliderLeft(_clickTabIndex);
					}else if(_clickTabIndex < _self.currentIndex){
						_self.sliderRight(_clickTabIndex);
					}
					// 为当前元素聚集
					$(this).addClass("active").siblings().removeClass("active");
				}
			});

			// 第一个tab下的前后按钮操作
			$(".J-list-check").on("click", function () {
				var _type = $(this).data("type"),
					_parentEl = $(".list-items").eq(0),
					_activeLi= _parentEl.find(".items-li.active"),
					_currentLiIndex = _activeLi.index();

				// 判断列表内列切换
				if(_type === "prev" && _self.currentIndex === 0){
					if(_self.childrenListShowStart > 1){
						_activeLi.removeClass("active");
						_parentEl.find(".items-li").eq(_currentLiIndex+2).remove();
						_parentEl.find(".items-li").eq(_currentLiIndex+1).addClass("mar-right0");
						_parentEl.find(".items-li").eq(_currentLiIndex-1).addClass("active");
						_parentEl.prepend(_self.childrenList[_self.childrenListShowStart-2]);
						_self.childrenListShowStart--;
						_self.childrenListShowEnd--;
						if(_self.childrenListShowStart === 1){
							$(this).addClass("disable");
						}else{
							$(".J-list-check").removeClass("disable");
						}
					}
				}else if(_type === "next" && _self.currentIndex === 0){
					if(_self.childrenListShowEnd < _self.childrenListEnd){
						_activeLi.removeClass("active");
						_parentEl.find(".items-li").eq(_currentLiIndex+1).addClass("active");
						_parentEl.find(".items-li").eq(_currentLiIndex+2).removeClass("mar-right0");
						_parentEl.find(".items-li").eq(_currentLiIndex-2).remove();
						_parentEl.append(_self.childrenList[_self.childrenListShowEnd].addClass("mar-right0"));
						_self.childrenListShowStart++;
						_self.childrenListShowEnd++;
						if(_self.childrenListShowEnd === _self.childrenListEnd){
							$(this).addClass("disable");
						}else{
							$(".J-list-check").removeClass("disable");
						}
					}
				}
			});

			// 开启和停止定时任务
			$(".info-cont,.list-check-ft").on("mouseenter", function () {
				clearTimeout(_self.timer);
			}).on("mouseleave", function(){
				// 执行tab定时切换
				_self.setTimeCheckTable();
			});

			// 定时任务切换
			_initTimer = setTimeout(function(){
				clearTimeout(_initTimer);
				// 执行tab定时切换
				_self.setTimeCheckTable();
			}, this.intWaitTime);
		},

		// 定时切换Tab
		setTimeCheckTable: function(){
			var _self = this;
			if(_self.currentIndex == 0){
				$(".list-check-icon").show();
			}else{
				$(".list-check-icon").hide();
			}
			clearTimeout(_self.timer);

			// 6秒切换一个列表
			_self.timer = setTimeout(function(){
				if(_self.clickCount == 0){
					// 移除第一个上面所有css动画Class
					$(".list-items").eq(_self.clickCount).removeClass("delay5 animated bounceInRight");
				}
				_self.clickCount++;

				if(_self.currentIndex < $("#list-table").find("li").length-1){
					_self.sliderLeft(_self.currentIndex+1);
					$("#list-table").find("li").removeClass("active");
					$("#list-table").find("li").eq(_self.currentIndex).addClass("active");
					_self.setTimeCheckTable();
				}else{
					_self.sliderRight(0);
					_self.currentIndex = 0;
					$("#list-table").find("li").removeClass("active");
					$("#list-table").find("li").eq(_self.currentIndex).addClass("active");
					_self.setTimeCheckTable();
				}
			}, 6000);
		},

		// 右滑操作
		sliderRight: function(index){
			var _self = this,
				_prevIndex = this.currentIndex,
				_currentIndex = index,
				_listCurrentClassName = "current";

			// 上一个移除动画和当前移入动画
			$(".list-items").eq(_prevIndex).animate({
				"left": "100%"
			},800,function(){
				$(this).removeClass(_listCurrentClassName).css("left","74px");
				_self.clickLock = false;
			});
			$(".list-items").eq(_currentIndex).css("left","-100%").addClass(_listCurrentClassName).animate({
				"left": "74px"
			},800);

			this.currentIndex = _currentIndex;
		},

		// 左滑操作
		sliderLeft: function(index){
			var _self = this,
				_prevIndex = this.currentIndex,
				_currentIndex = index,
				_listCurrentClassName = "current";

			// 上一个移除动画和当前移入动画
			$(".list-items").eq(_prevIndex).animate({
				"left": "-100%"
			},800,function(){
				$(this).removeClass(_listCurrentClassName).css("left","74px");
				_self.clickLock = false;
			});
			$(".list-items").eq(_currentIndex).css("left","100%").addClass(_listCurrentClassName).animate({
				"left": "74px"
			},800);

			this.currentIndex = _currentIndex;
		},

		// 获取所有列表下面子元素
		getAllListChildren: function(){
			var _childrenElArr = [],
				_parentEL = $("#list-cont").find(".list-items");

			$.each(_parentEL, function(key){
				var _childrenList = _parentEL.eq(key).find(".items-li");
				if(key != 0){
					$.each(_childrenList, function(key2){
						var _childrenLi = _childrenList.eq(key2).clone().removeClass("active mar-right0");

						_childrenElArr.push(_childrenLi);
					});
				}
			});

			return _childrenElArr;
		}
	};
	TableSlide.init();
}(window.jQuery));