var drag_ = false
var D = new Function('obj', 'return document.getElementById(obj);')
var oevent = new Function('e', 'if (!e) e = window.event;return e')
var td = function() {
	D("pres").style.left = (document.documentElement.clientWidth - D("pres").clientWidth)
			/ 2 - 400 + "px";
	D("pres").style.top = (document.body.clientHeight - D("pres").clientHeight)
			/ 2 + document.documentElement.scrollTop - 100 + "px";
}
function Move_obj(obj) {
	var x, y;
	D(obj).onmousedown = function(e) {
		drag_ = true;
		with (this) {
			x = oevent(e).clientX - parseInt(D("pres").style.left);
			y = oevent(e).clientY - parseInt(D("pres").style.top);
			document.onmousemove = function(e) {
				if (!drag_)
					return false;
				with (D("pres")) {
					D("pres").style.left = oevent(e).clientX - x + "px";
					D("pres").style.top = oevent(e).clientY - y + "px";
				}
			}
		}
		document.onmouseup = new Function("drag_=false");
	}
}
var tuod = function() {
	D("pres").style.left = (document.documentElement.clientWidth - D("divBox").clientWidth)
			/ 2 - 200 + "px";
	D("pres").style.top = (document.body.clientHeight - D("divBox").clientHeight)
			/ 2 + document.documentElement.scrollTop - 100 + "px";
}
function Move_obj2(obj) {
	var x, y;
	D(obj).onmousedown = function(e) {
		drag_ = true;
		with (this) {
			x = oevent(e).clientX - parseInt(D("divBox").style.left);
			y = oevent(e).clientY - parseInt(D("divBox").style.top);
			document.onmousemove = function(e) {
				if (!drag_)
					return false;
				with (D("divBox")) {
					D("divBox").style.left = oevent(e).clientX - x + "px";
					D("divBox").style.top = oevent(e).clientY - y + "px";
				}
			}
		}
		document.onmouseup = new Function("drag_=false");
	}
}

function getpagesize() {
	var number = $("#pagesize").val();
	return number;
}

function getpageno() {
	var pages = $("#pages").val();
	return pages;
}

function rtime() {
	var max = "2099-12-31 23:59:59";
	var v = $("#sendtime").attr("value");
	// if(v.length != 0)
	// {
	// var year = v.substring(0,4);
	// var mon = v.substring(5,7);
	// var day = 31;
	// if (mon != "12")
	// {
	// mon = String(parseInt(mon,10)+1);
	// if (mon.length == 1)
	// {
	// mon = "0"+mon;
	// }
	// switch(mon){
	// case "01":day = 31;break;
	// case "02":day = 28;break;
	// case "03":day = 31;break;
	// case "04":day = 30;break;
	// case "05":day = 31;break;
	// case "06":day = 30;break;
	// case "07":day = 31;break;
	// case "08":day = 31;break;
	// case "09":day = 30;break;
	// case "10":day = 31;break;
	// case "11":day = 30;break;
	// }
	// }
	// else
	// {
	// year = String((parseInt(year,10)+1));
	// mon = "01";
	// }
	// max = year+"-"+mon+"-"+day+" 23:59:59"
	// }
	WdatePicker( {
		dateFmt : 'yyyy-MM-dd HH:mm:ss'
	});
}

function stime() {
	var max = "2099-12-31 23:59:59";
	var v = $("#recvtime").attr("value");
	var min = "1900-01-01 00:00:00";
	// if(v.length != 0)
	// {
	// max = v;
	// var year = v.substring(0,4);
	// var mon = v.substring(5,7);
	// if (mon != "01")
	// {
	// mon = String(parseInt(mon,10)-1);
	// if (mon.length == 1)
	// {
	// mon = "0"+mon;
	// }
	// }
	// else
	// {
	// year = String((parseInt(year,10)-1));
	// mon = "12";
	// }
	// min = year+"-"+mon+"-01 00:00:00"
	// }
	WdatePicker( {
		dateFmt : 'yyyy-MM-dd HH:mm:ss'
	});
}

// 查看发送网讯详细信息
function ToReplyPageList(did, busid, taskid) {
	var lguserid = $("#lguserid").val();
	window.open("wx_count.htm?method=getReplyQueList&pageno=1&busId=" + busid
			+ "&did=" + did + "&lguserid=" + lguserid + "&taskid=" + taskid,
			"_self");
}

// 查看发送网讯详细信息
function ToReplyInfo(did, busid, title, senddate, jspcount, mtid) {
	var lguserid = $("#lguserid").val();
	window.open("wx_count.htm?method=getTjDetailQueList&pageno=1&did=" + did
			+ "&busId=" + busid  + "&senddate=" + senddate
			+ "&lguserid=" + lguserid + "&jspcount=" + jspcount + "&mtid="
			+ mtid, "_self");
}

function modify(t) {
	$('#modify').dialog( {
		autoOpen : false,
		width : 250,
		height : 200
	});
	$("#msg").empty();
	$("#msg").text($(t).children("label").children("xmp").text());
	$('#modify').dialog('open');
}

$(function() {
	$('#hudtype,#douserd').isSearchSelect( {
		'width' : '154',
		'isInput' : false,
		'zindex' : 0
	});
})


   function findNet(){
            var netname = document.getElementById("nname").value;
            var netid = document.getElementById("nid2").value;
            var path = document.getElementById("path").value;
            window.location.href=path+"/wx_count.htm?method=find&netId="+netid+"&netName="+netname;
   } 
            
   function netSendOut(){
	   		var path = document.getElementById("path").value;
             window.location.href=path+"/wx_count.htm?method=wxSendExport";
   } 

  
  