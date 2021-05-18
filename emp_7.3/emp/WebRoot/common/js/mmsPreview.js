var timer = null;
var arr = [""];
var index = 0;
var ttime = 0;
var ttimer = null;
var first = 0;
var cplaytime = 0;
var nplaytime = -1;
var ipath = null;
var commonPath = null;
var count = 0;
//帧数的全局变量，0：初始值，1上一帧，2下一帧，3无
var nextpage1 = 0;
var parmCount =null;//参数个数

$(document).ready(function() {
	ipath = $('#ipathUrl').val();
	commonPath = $("#commonPath").val();
});


//初使化加载手机图片的方法
function inits()
{
	window.clearInterval(timer);
	window.clearInterval(ttimer);  
	timer = null;
	index = 1;
	ttimer = null;
	first = 0;
	$("#showtime").css("z-index","0");
	$("#showtime").html("");
	document.getElementById("chart").style.width = 0+"%";
	$("#pers").hide();
	$("#screen").css("margin-top","86px");
}

String.prototype.replaceAll  = function(s1,s2){    
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
function guolvXG(text)
{
	var t=text.replace(/\\/g,"\\\\");
	t=t.replace(/\\\"/g,"\"");
	return t;
}
//播放方法
function play(dsFlag){
	//只有第一次时需要加载参数
	if(parmCount == null)
	{	
		if(dsFlag == 1){
			getParmCount();//获取模板中的参数个数
		}
		if(parmCount !=null && parmCount>0)
		{
			$("#tempView").dialog('option', 'width', 500);
			var str;
			var strs="";
			for(var i=0;i<parmCount;i++)
			{
	    		str=getJsLocaleMessage("common","common_parameter")+(i+1)+': &nbsp;<input type="text" style="width:140px" id="pa'+i+'" value=""/></br><br/>';
	    		strs=strs+str;
			}
			$("#inputParamCnt1").html(strs);
		}
		else
		{
				$("#tempView").dialog('option', 'width', 290);
		}
	}
    var showinfo="";
    for (var i = 1; i < arr.length; i++)
    {
       showinfo=showinfo+"<a href='#' style='color:blue;' onclick='javascript:currentPage("+i+");return false'>"+i+"</a>&nbsp;";
    }
    $("#currentpage").html(showinfo);
    
	setScreen();	
    setPer();
}
//播放下一帧
function setPer()
{
	ttimer = setInterval("reTimer()",1000);
}
//时间进度条动态变的方法
function reTimer()
{		   
	first++;
	cplaytime++;
	if(first <= ttime)
	{
		document.getElementById("chart").style.width = (first*100/ttime)+"%";
		var s = (ttime-first)<10?"0"+(ttime-first):(ttime-first);
		$("#showtime").html("0:"+s);
		//alert("first:"+first+",nplaytime:"+nplaytime+",cplaytime:"+cplaytime+",ttime:"+ttime);
		if(nextpage1 == 2){
			nextpage1 = 3;
			//设置上面那个时间条
			first = first + (nplaytime - cplaytime);
			//整改满足下一帧的条件
			cplaytime = nplaytime;

		}
		if(nextpage1 == 1){
			//上一帧
			nextpage1 = 3;					
			if(index == 2){
				alert(getJsLocaleMessage("common","common_js_mmsPreview_1"));
			}else{
				//第一帧播放的内容
			    var textshe = guolvXG(arr[index-2]);
				var datashe = $.parseJSON(textshe);
				//当前帧的上一帧的时间
				nplaytimeshe = datashe.time/1000;

				first = first - (nplaytimeshe + cplaytime);

				index = index - 2;
				
				refresh();
			}
		}else{
			if(cplaytime == nplaytime){
				//过了一帧后执行
				$("#screen").empty();
			    refresh();
			}
		}
	}
}
//播放方法1
function setScreen()
{   
	$("#screen").css("margin-top","0px");
	if(arr[index] != null && arr[index] != "")
    {
	    var textt = arr[index];
		var data = $.parseJSON(textt);
		nplaytime = data.time/1000;
		//帧数+1
		index = index + 1; 
	    var html ="";
	    if (data.img != null && data.img != "")
	    {
	    	html = "<img style='width:170;' src='"+data.img+"' /><p>";
	    }
	    if (data.text!= null)
	    {
    	    var dataText=data.text;
		    var vals=$("#inputParamCnt1").find("input");
			for(var i=0;i<vals.length;i++)
			{
				if(vals.eq(i).val()!="")
				{
					dataText = dataText.replaceAll("#p_"+(i+1)+"#", vals.eq(i).val());
					dataText = dataText.replaceAll("#P_"+(i+1)+"#",vals.eq(i).val());
				}
			}
	    	data.text = dataText.replaceAll("&lt;BR/&gt;","<BR/>");
	    	data.text = getContentValChange(data.text);
	    	html = html+data.text;
		}
		if (data.sound != null && data.sound != "")
	    {
	    	//html = html+"<embed id='swfEmbed' src='"+data.sound+"' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='70' height='40'></embed>";
	    	var soundurl = data.sound;
	    	if(soundurl.length>0){
	  	    	 var soundtype = $.trim(soundurl.substring(soundurl.lastIndexOf(".")+1));
//	  	    	 if(soundtype == "amr" || soundtype == "AMR"){
//	  	    		//html = html+"<object classid='clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95'  width='70' height='40'><param name='FileName' value='"+soundurl+"'/></object>";
//	  		     	html = html+"<embed src='"+soundurl+"' style='HEIGHT: 45px; WIDTH: 180px' type='audio/mpeg' AUTOSTART='1' loop='0'/>";
//	  	    	 }else{
//	  		    	//html = html+"<embed id='swfEmbed' src='"+soundurl+"' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='70' height='40'></embed>";
//	  			 	html = html+"<object classid='clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95'  width='180' height='45' type=application/x-oleobject><param name='FileName' value='"+soundurl+"'/><param name='AutoStart' value='0'/></object>";
//	  	    	 }
	  	    	 if(soundtype == "mid" || soundtype == "MID" || soundtype == "midi" || soundtype == "MIDI")
  	    		 {
	  	    		 //如果是websphere
  	    		 	if(serverName == "WebSphere")
  	    		 		{
	  	    		 		//QuickTime Player
  	    		 			html = html+"<embed src='"+soundurl+"' style='HEIGHT: 45px; WIDTH: 180px' type='audio/mpeg' AUTOSTART='1' loop='0'/>";
  	    		 		}
	  	    		 	else
  	    		 		{
	  	    		 		//media play
						html = html+"<object classid='clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95'  width='180' height='45' type=application/x-oleobject><param name='FileName' value='"+soundurl+"'/><param name='AutoStart' value='0'/></object>";
  	    		 		}
	  	    		 }
		  	    	 else
	  	    		 {
		  	    		 //QuickTime Player
  	    		 	html = html+"<embed src='"+soundurl+"' style='HEIGHT: 45px; WIDTH: 180px' type='audio/mpeg' AUTOSTART='1' loop='0'/>";
  	    		 }
	  		 }
	    }
		
	   $("#screen").empty();
	   $("#screen").html(html);
	   //最上面的时间进度条
	   $("#showtime").css("z-index","999");
	   //中间内容显示
	   $("#pers").show();
	   //下面当前帧/总帧数
	   $("#pointer").html((index-1)+"/"+(arr.length-1));
	   count = arr.length-1;
	   //上一帧，下一帧 
	   $("#nextpage").html("&nbsp;&nbsp;&nbsp;<a href='#' style='TEXT-DECORATION:none;color:blue;' onclick='javascript:advanp("+index+");return false'>"+getJsLocaleMessage("common","common_js_mmsPreview_4")+"</a>&nbsp;&nbsp;<a href='#' style='TEXT-DECORATION:none;color:blue;' onclick='javascript:nextp("+index+");return false'>"+getJsLocaleMessage("common","common_js_mmsPreview_2")+"</a>&nbsp;&nbsp;<a href='#' style='TEXT-DECORATION:none;color:blue;' onclick='javascript:stopp("+index+");return false'>"+getJsLocaleMessage("common","common_stop")+"</a>");
    }
}

//上一帧事件
function advanp(a){
	nextpage1 = 1;
}

//下一帧事件
function nextp(a){
	if(a-1 == count)
	{
		alert(getJsLocaleMessage("common","common_js_mmsPreview_3"));
		return;
	}
	nextpage1 = 2;
}
//停止播放
function stopp(a)
{
   index = arr.length;
   refresh();
}
//当前需查看的帧
function currentPage(a)
{
    index = a;
    var alltime = 0;
    for (var i = 1; i < a; i++)
    {
       var textt1 = arr[i];
       
	   var data1 = $.parseJSON(textt1);			
       alltime = alltime+data1.time/1000;
    }
    first = alltime;
    cplaytime = nplaytime;
    $("#screen").empty();
    refresh();
}
//跳转到下一帧或者结束的方法
function refresh()
{
    //如果还没有播放完成,则跳转到下一帧继续播放
	if (index < arr.length)
	{
	    cplaytime = 0;
		setScreen();
	}
	else
	{
	    cplaytime = 0;
		nplaytime = -1;
		clearInterval(ttimer); 
		inits();
		$("#pointer").empty();
		$("#nextpage").empty();
		$("#screen").empty();
		$("#currentpage").empty();
        $("#screen").html("<center style='padding-top:120px'><img src='"+commonPath+"/common/img/play.png' style='cursor:pointer;' title='"+getJsLocaleMessage("common","common_play")+"' onclick='javascript:play();'/></center>");
	}
   timer = null;
}

function getParmCount()
{
    var eg = /#[pP]_[1-9][0-9]*#/g;
    for (var i = 1; i <arr.length; i++)
    {          
       var textt1 = arr[i];
	   var data1 = $.parseJSON(textt1);
	   if(data1.text != null)
	   {
			var arr1 = data1.text.match(eg);//返回数组 
			if(arr1 != null)
			{
    			for(var j=0;j<arr1.length;j++)
    			{
    				var rstr = arr1[j].toUpperCase(); 
					var pc = rstr.substring(rstr.indexOf("#P_")+3,rstr.lastIndexOf("#"));
					var pci = parseInt(pc);
					if(pci > parmCount)
					{
						parmCount = pci;
					}
    			}
			}
	   }
    }	
}

function getContentValChange(msg){
	var reg=/#[pP]_(.*?)#/g;
	msg=msg.replace(reg,replacerChange);
	return msg;
}