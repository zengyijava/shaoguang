//定义replaceAll方法
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}


function noticEblur(ob) {
	var $parent = ob.parent();
	if (ob.is("#sendContent")) {
		ob.val($.trim(ob.val()));
		len(ob);
		if ($.trim(ob.val()) == "") {
			//alert("请输入完美通知发送内容！");
			$("#reStartSendDia").find('#isOk').attr("value","0");
		} else if (ob.val().length > 965) {
			//alert("完美通知发送内容过长，应少于965个字符！");
			$("#reStartSendDia").find('#isOk').attr("value","1");
		} else {
			$("#reStartSendDia").find('#isOk').attr("value","2");
		}
	}
}

// 统计短信内容字数
function len(ob) {
	var content = $.trim(ob.val());
	var huiche = content.length - content.replaceAll("\n", "").length;
	$("#reStartSendDia").find('#strlen').html((content.length + huiche));

};

// 短信内容键盘事件监听统计字数
function synlen() {
	$("#reStartSendDia").find("textarea[name='sendContent']").keyup(function() {
		var content = $(this).val();
		var huiche = content.length - content.replaceAll("\n", "").length;
		if (content.length + huiche > 965) {
			$(this).val(content.substring(0, 965 - huiche));
		}
		len($(this));
	});
}
	
			
	function rtime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#sendTime").attr("value");
	if(v.length != 0)
	{
	    var year = v.substring(0,4);
		var mon = v.substring(5,7);
		var day = 31;
		if (mon != "12")
		{
		    mon = String(parseInt(mon,10)+1);
		    if (mon.length == 1)
		    {
		        mon = "0"+mon;
		    }
		    switch(mon){
		    case "01":day = 31;break;
		    case "02":day = 28;break;
		    case "03":day = 31;break;
		    case "04":day = 30;break;
		    case "05":day = 31;break;
		    case "06":day = 30;break;
		    case "07":day = 31;break;
		    case "08":day = 31;break;
		    case "09":day = 30;break;
		    case "10":day = 31;break;
		    case "11":day = 30;break;
		    }
		}
		else
		{
		    year = String((parseInt(year,10)+1));
		    mon = "01";
		}
		max = year+"-"+mon+"-"+day+" 23:59:59"
	}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});

}

function stime(){
    var max = "2099-12-31 23:59:59";
    var v = $("#recvTime").attr("value");
    var min = "1900-01-01 00:00:00";
	if(v.length != 0)
	{
	    max = v;
	    var year = v.substring(0,4);
		var mon = v.substring(5,7);
		if (mon != "01")
		{
		    mon = String(parseInt(mon,10)-1);
		    if (mon.length == 1)
		    {
		        mon = "0"+mon;
		    }
		}
		else
		{
		    year = String((parseInt(year,10)-1));
		    mon = "12";
		}
		min = year+"-"+mon+"-01 00:00:00"
	}
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

}
function modify(t)
{
		$("#msg").empty();
		//用label显示短信内容
		//$("#msg").append($(t).children("label").text().replaceAll("<","&lt;").replaceAll(">","&gt;"));
		//修改成用textarea显示短信内容
		$("#msgcont").val($(t).children("textarea").val());
		$("#singledetail").dialog("option","title", getJsLocaleMessage('dxzs','dxzs_ssend_alert_65'));
		$("#singledetail").dialog("open");
}	
function changeHisInfo(taskid){
	page_loading();
	var path = $("#pathUrl").val();
	var lgguid = $("#lgguid").val();
	window.location.href = path+"/per_queryNotHis.htm?method=getPreNoticeHisInfo&noticeI=1&taskid="+taskid+"&lgguid="+lgguid;
}

function modifyNew(t)
{
    $("#msgcont").empty();
    //用label显示短信内容
    $("#msgcont").text($(t).children("xmp[name='msgXmp']").text());
    //修改成用textarea显示短信内容
    $("#singledetail").dialog("option","title", getJsLocaleMessage('dxzs','dxzs_ssend_alert_65'));
    $("#singledetail").dialog("open");
}