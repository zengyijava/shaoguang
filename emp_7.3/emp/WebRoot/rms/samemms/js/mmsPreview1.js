var timer1 = null;
var arr2 = [""];
var index1 = 0;
var ttime1 = 0;
var ttimer1 = null;
var first1 = 0;
var cplaytime1 = 0;
var nplaytime1 = -1;
var ipath = null;
//帧数的全局变量，0：初始值，1上一帧，2下一帧，3无
var nextpage11 = 0;

$(document).ready(function() {
	ipath = $('#ipathUrl').val();
});


//初使化加载手机图片的方法
function inits1()
{
	window.clearInterval(timer1);
	window.clearInterval(ttimer1);  
	timer1 = null;
	index1 = 1;
	ttimer1 = null;
	first1 = 0;
	$("#showtime1").css("z-index","0");
	$("#showtime1").html("");
	document.getElementById("chart1").style.width = 0+"%";
	$("#pers1").hide();
	$("#screen1").css("margin-top","60px");
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
function play1(){
    var showinfo="";
    for (var i = 1; i < arr2.length; i++)
    {
       showinfo=showinfo+"<a href='#' style='color:blue;' onclick='javascript:currentPage1("+i+");return false'>"+i+"</a>&nbsp;";
    }
    $("#currentpage1").html(showinfo);
	setScreen1();	
    setPer1();
}
//播放下一帧
function setPer1()
{
	ttimer1 = setInterval("reTimer1()",1000);
}
//时间进度条动态变的方法
function reTimer1()
{		   
	first1++;
	cplaytime1++;
	if(first1 <= ttime1)
	{
		document.getElementById("chart1").style.width = (first1*100/ttime1)+"%";
		var s = (ttime1-first1)<10?"0"+(ttime1-first1):(ttime1-first1);
		$("#showtime1").html("0:"+s);
		if(nextpage11 == 2){
			nextpage11 = 3;
			//设置上面那个时间条
			first1 = first1 + (nplaytime1 - cplaytime1);
			//整改满足下一帧的条件
			cplaytime1 = nplaytime1;

		}
		if(nextpage11 == 1){
			//上一帧
			nextpage11 = 3;					
			if(index1 == 2){
				alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_1"));
			}else{
				//第一帧播放的内容
			    var textshe1 = guolvXG(arr2[index1-2]);
				var datashe1 = $.parseJSON(textshe1);
				//当前帧的上一帧的时间
				nplaytimeshe1 = datashe1.time/1000;

				first1 = first1- (nplaytimeshe1 + cplaytime1);

				index1 = index1 - 2;
				
				refresh1();
			}
		}else{
			if(cplaytime1 == nplaytime1){
				//过了一帧后执行
				$("#screen1").empty();
			    refresh1();
			}
		}
	}
}
//播放方法1
function setScreen1()
{   
	$("#screen1").css("margin-top","60px");
	if(arr2[index1] != null && arr2[index1] != "")
    {
	    var textt = arr2[index1];
		var data = $.parseJSON(textt);
		nplaytime1 = data.time/1000;
		//帧数+1
		index1 = index1 + 1; 
	    var html ="";
	    if (data.img != null && data.img != "")
	    {
	    	html = "<img style='width:170;' src='"+data.img+"' /><p>";
	    }
	    if (data.text!= null)
	    {
    	    var dataText=data.text;
	    	data.text = dataText.replaceAll("&lt;BR/&gt;","<BR/>");
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
//	  		     	html = html+"<EMBED src='"+soundurl+"' style='HEIGHT: 45px; WIDTH: 180px' type='audio/mpeg' AUTOSTART='1' loop='0'/>";
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
		
	   $("#screen1").empty();
	   $("#screen1").html(html);
	   //最上面的时间进度条
	   $("#showtime1").css("z-index","999");
	   //中间内容显示
	   $("#pers1").show();
	   //下面当前帧/总帧数
	   $("#pointer1").html((index1-1)+"/"+(arr2.length-1));
	   //上一帧，下一帧 
	   $("#nextpage1").html("&nbsp;&nbsp;&nbsp;<a href='#' style='TEXT-DECORATION:none;color:blue;' onclick='javascript:advanp1("+index1+");return false'> " +
	   getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_71") + "</a>&nbsp;&nbsp;<a href='#' style='TEXT-DECORATION:none;color:blue;' onclick='javascript:nextp1("+index1+");return false'>" + 
	   getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_72") +"</a>&nbsp;&nbsp;<a href='#' style='TEXT-DECORATION:none;color:blue;' onclick='javascript:stopp1("+index1+");return false'>" + 
	   getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_73") +"</a>");
    }
}

//上一帧事件
function advanp1(a){
	nextpage11 = 1;
}

//下一帧事件
function nextp1(a){
	if(a-1 == count)
	{
		alert(getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_2"));
		return;
	}
	nextpage11 = 2;
}
//停止播放
function stopp1(a)
{
   index1 = arr2.length;
   refresh1();
}
//当前需查看的帧
function currentPage1(a)
{
    index1 = a;
    var alltime = 0;
    for (var i = 1; i < a; i++)
    {
       var textt1 = arr2[i];
       
	   var data1 = $.parseJSON(textt1);			
       alltime = alltime+data1.time/1000;
    }
    first1 = alltime;
    cplaytime1 = nplaytime1;
    $("#screen1").empty();
    refresh1();
}
//跳转到下一帧或者结束的方法
function refresh1()
{
    //如果还没有播放完成,则跳转到下一帧继续播放
	if (index1 < arr2.length)
	{
	    cplaytime1 = 0;
		setScreen1();
	}
	else
	{
	    cplaytime1 = 0;
		nplaytime1 = -1;
		clearInterval(ttimer1); 
		inits1();
		$("#pointer1").empty();
		$("#nextpage1").empty();
		$("#screen1").empty();
		$("#currentpage1").empty();
        $("#screen1").html("<center style='padding-top:120px'><img src='"+ipath+"/img/play.png' style='cursor:pointer;' title=' " + getJsLocaleMessage("ydcx","ydcx_cxyy_jtcxfs_text_3") + "' onclick='javascript:play1();'/></center>");
	}
   timer1 = null;
}

