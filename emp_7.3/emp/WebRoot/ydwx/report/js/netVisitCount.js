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



function getpagesize(){
    var number=$("#pagesize").val();
    return number;
}

  function getpageno(){
    var pages=$("#pages").val();
    return pages;
}
	function seeVisitPageList(busid){
		var lguserid = $("#lguserid").val();
        window.open("wx_visitreport.htm?method=getVisitQueList&type=1&busId="+busid+"&lguserid="+lguserid,"_self");
	}


	function bycheck(type){
		var lguserid = $("#lguserid").val();
		var lgcorpcode = $("#lgcorpcode").val();
		window.open("wx_visitreport.htm?method=find&type="+type+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&time="+new Date().getTime(),"_self");
	}
	
	function seeVisitInfo(obj){
      window.open("wx_visitreport.htm?method=visittrustView&type=1&busId="+obj+"","_self");
	}
	
		//??????
	function Look(netId){	
	    //$("#netid").val(netId);
	    $.post('wx_manger.htm?method=showNetById',{netId:netId},function(data){
	       data=eval("("+data+")");
	       listpage=data;
	       $(".ym").children().remove();
	       for(var i=0;i<listpage.length;i++)	{
	           $("<option>").val(listpage[i].id).html(listpage[i].name).appendTo($(".ym"));
	      }
            // ??????????????????????????????????????????????????????
            if(listpage[0].content=="notexists"){
                $("#nm_preview_common").attr("src","ydwx/wap/404.jsp");
            }else{
                $("#nm_preview_common").attr("src","file/wx/PAGE/wx_"+listpage[0].id+".jsp");
            }
	      $("#divBox").dialog("open");
     });
  }
	
	
    function findNet(){
        var netname = document.getElementById("nname").value;
        var netid = document.getElementById("nid2").value;
        window.location.href=$("#path").val()+"/wx_count.htm?method=find&netId="+netid+"&netName="+netname;
    } 
        
    function netSendOut(){
       window.location.href=$("#path").val()+"/wx_count.htm?method=wxSendExport";
    } 
	

	