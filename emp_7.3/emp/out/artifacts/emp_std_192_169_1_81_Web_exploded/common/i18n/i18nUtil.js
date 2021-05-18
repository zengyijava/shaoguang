/**
 * 获取多语言资源 
 * @param fileName 资源名称
 * @param key 键
 * @returns
 */
function getJsLocaleMessage(fileName, key) {
	try {
		return eval(fileName)[key];
	} catch (e) {
		return undefined;
	}
};


/**
 * 修改jquery ui弹出框宽高
 * @param obj jquery弹出框对象
 * @param jsonStr 
 * @param dialogJson 页面宽高json字符串配置,比如"tem_mmsTemplate_detail":{width:300,height:200}
 * @param key
 */
function resizeDialog(obj,dialogJson,key){
	if(CstlyeSkin && CstlyeSkin.indexOf("frame4.0") != -1&&dialogJson&&eval(dialogJson)[key]){
		obj.dialog("option",eval(dialogJson)[key]);
	}
}


var EMP_Dialog = {
	doShadow : function() {
		var alertShadow = document.createElement("DIV");
		alertShadow.id = "alertShadow";
		alertShadow.className = 'alertShadow';
		document.body.appendChild(alertShadow);
	},
	cancelShadow : function() {
		document.body.removeChild(alertShadow);
	},
	getCss : function(dom, key) {
		return dom.currentStyle ? dom.currentStyle[key] : document.defaultView
				.getComputedStyle(dom, false)[key];
	},
	showAlert : function(e) {
		var left = 0;
		var ttop = 0;
		var alertDiv = document.createElement("DIV");
		alertDiv.className = "alertDiv";
		alertDiv.id = "alertDiv";
		var msg;
		var btnConfirm;
		try {
			msg = getJsLocaleMessage("common", "common_txt_alert");
			btnConfirm = getJsLocaleMessage("common", "common_btn_confirm");
			if (!msg) {
				msg = "系统提示";
			}
			if (!btnConfirm) {
				btnConfirm = "确定";
			}
		} catch (err) {
			msg = "系统提示";
			btnConfirm = "确定";
		}
		var html = '<div id="alertDivHead" class="alertDivHead"><span>【'
				+ msg
				+ '】</span><a style="width:10px" onclick="closeDiv()">X</a></div><div id="alertDiv" class="alertDivBody"><div>'
				+ e
				+ '</div><div><input type="button" onclick="closeDiv()" value="'
				+ btnConfirm + '" class="alertDivConfirm" /></div></div>';
		alertDiv.innerHTML = html;
		document.body.appendChild(alertDiv);

		doShadow();

		alertDiv.focus();
		this.closeDiv = function() {
			document.body.removeChild(alertDiv);
			cancelShadow();
		};
		var target = document.getElementById("alertDiv");
		var alertDivHead = document.getElementById("alertDivHead");

		left = getCss(target, "left");
		ttop = getCss(target, "top");

		alertDivHead.onmousedown = function(event) {
			if (!event) {
				event = window.event;
				// 防止IE文字选中
				alertDivHead.onselectstart = function() {
					return false;
				};
			}
			var e = event;
			var currentX = e.clientX;
			var currentY = e.clientY;
			document.onmousemove = function(event) {
				var e = event ? event : window.event;
				var nowX = e.clientX, nowY = e.clientY;
				var disX = nowX - currentX, disY = nowY - currentY;
				target.style.left = parseInt(left) + disX + "px";
				target.style.top = parseInt(ttop) + disY + "px";
				if (event && event.preventDefault) {
					event.preventDefault();
				}
			};
		};
		document.onmouseup = function() {
			left = getCss(target, "left");
			ttop = getCss(target, "top");
			document.onmousemove = null;
		};
		return true;
	}
};



