function appDrag(option){
	this.oTarget = document.getElementById(option.wrapId); //拖动对象
	this.oDrap = false; //判断是否进行拖动
	this.nMax_w = option.max_w || document.documentElement.clientWidth; //X移动的最大宽度
	this.nMax_h = option.max_h || document.documentElement.clientHeight; //Y移动的最大高度
	this.nDisX = 0; 
	this.nDisY = 0;
	this.init();
}
appDrag.prototype = {
	setCss:function(node,val){
		for(var v in val){
			node.style.cssText += ';'+ v +':'+val[v];
		}
	},
	getEvent:function(e){
		return e || window.event;
	},
	fnMousedown:function(){
		var _that = this;
		this.oTarget.onmousedown = function(e){
			var e = _that.getEvent(e);

			_that.oDrap = true;
			_that.nDisX = e.clientX - _that.oTarget.offsetLeft;
			_that.nDisY = e.clientY - _that.oTarget.offsetTop;

			return false;

		}
	},
	fnMousemove:function(){
		var _that = this;
		document.onmousemove = function(e){
			var e = _that.getEvent(e);
			if(!_that.oDrap) return;
			var _moveW = e.clientX - _that.nDisX,
				_moveH = e.clientY - _that.nDisY,
				_maxW = _that.nMax_w - _that.oTarget.offsetWidth, //减去拖动对象的宽
				_maxH = _that.nMax_h - _that.oTarget.offsetHeight; //减去拖动对象的高
			//横向拖动的最小，最大范围
			_moveW = _moveW<0?0:_moveW;
			_moveW = _moveW>_maxW?_maxW:_moveW;
			//竖向拖动的最小，最大范围
			_moveH = _moveH<0?0:_moveH;
			_moveH = _moveH>_maxH?_maxH:_moveH;

			_that.setCss(_that.oTarget,{'left':_moveW + 'px','top':_moveH + 'px'})
			return false;
		}		
	},
	fnMouseup:function(){
		var _that = this;
		document.onmouseup = function(){
			_that.oDrap = false;
			_that.setCss(_that.oTarget)
		}
	},
	init:function(){
		this.fnMousedown();
		this.fnMousemove();
		this.fnMouseup();
	}
}
/*
@[wrapId] - string:拖动的元素id名 --- 必需
@[max_w] - number:X轴最大的移动距离，默认是页面可见的宽度 
@[max_h] - number:Y轴最大的移动距离，默认是页面可见的高度
*/