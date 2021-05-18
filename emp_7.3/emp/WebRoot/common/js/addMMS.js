//**********兼容IE6，IE7，IE8，火狐浏览器模态对话框的宽高
var isIE = (document.all) ? true : false;
var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
var app=navigator.appName;
var ieWidth = 320;
var ieHeight = 520;
if (isIE6) {
	ieWidth = 300;
	ieHeight = 500;
}
if (app == "Netscape") {
	ieWidth = 300;
	ieHeight = 500;
}
//***************

var p2,p3;
var arr = ["","","","","","","","","","","","","","",""];
var arrs = ["0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"];		//这里记录的是所有上传上来的图片以及声音的 KB总量
var timer = null;
var index = 0;
var ttime = 0;					//整个的播放时间 
var ttimer = null;
var first = 0;
var count = 0;					//点击预览时   获取该帧数量
var ide = 1;
var cplaytime = 0;
var nplaytime = -1;
//帧数的全局变量，0：初始值，1上一帧，2下一帧，3无
var nextpage1 = 0;
String.prototype.replaceAll  = function(s1,s2){    
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
$(document).ready(function() {
	 var h = 510;
		if (navigator.appName == "Netscape")
	    {
	    	h = 510;
	    }
	$("#modelType2 li").each(function(index){
		$(this).mouseover(function(){
			$(this).css("cursor","pointer");
		});
	});
	$("#modelType3 li").each(function(index){
		$(this).mouseover(function(){
			$(this).css("cursor","pointer");
		});
	});
	$(".btnMatel").hover(function() {
		var img=$(this).css("background-image");
		if(isIE6){
			$(this).css("background-image",img.replace("matel","matel_hover"));
		}
		$(this).toggleClass("btnHover");
	}, function() {
		var img=$(this).css("background-image");
		if(isIE6){
			$(this).css("background-image",img.replace("matel_hover","matel"));
		}
		$(this).toggleClass("btnHover");
	});
	$(".btnImage").hover(function() {
		var img=$(this).css("background-image");
		if(isIE6){
			$(this).css("background-image",img.replace("addImage","addImage_hover"));
		}
		$(this).toggleClass("btnHover");
	}, function() {
		var img=$(this).css("background-image");
		if(isIE6){
			$(this).css("background-image",img.replace("addImage_hover","addImage"));
		}
		$(this).toggleClass("btnHover");
	});
	$(".btnMusic").hover(function() {
		var img=$(this).css("background-image");
		if(isIE6){
			$(this).css("background-image",img.replace("addMusic","addMusic_hover"));
		}
		$(this).toggleClass("btnHover");
	}, function() {
		var img=$(this).css("background-image");
		if(isIE6){
			$(this).css("background-image",img.replace("addMusic_hover","addMusic"));
		}
		$(this).toggleClass("btnHover");
	});
	$("#1").addClass("mmsFrame");								//这里是被选中的效果
	//这里是处理模板中第一帧 的触发事件
	$("#mframes").children().click(function(){
		var frameid = $("#framesize").attr("value");			//获取帧的数量
		changeFrame($(this).attr("id"));
		$(".mmsFrame").addClass("mmsFrames");
		$(".mmsFrame").removeClass("mmsFrame");
		$(this).removeClass("mmsFrames");
		$(this).addClass("mmsFrame");
		$("#curf").empty();										//下面显示 当前X/X 的内容
		$("#curf").html(getJsLocaleMessage("common","common_current")+$.trim($(this).attr("id"))+"/"+frameid);
	});
	$("#1").hover(function() {
		$(this).children("div .floatDel")[0].style.display="inline-block";
		$(this).children("div .floatFrame")[0].style.display="inline-block";
	},function(){
		$(this).children("div .floatDel")[0].style.display="none";
		$(this).children("div .floatFrame")[0].style.display="none";
	});
	//彩信预览
	$("#previewTemp").dialog({			
		autoOpen: false,
		height:h,
		width: 290,
		resizable:false,
		modal: true,
		close:function(){
	    cplaytime = 0;
		nplaytime = -1;
		clearInterval(ttimer); 
	}
	});
	//调用素材的dialog
	$("#mmsMat").dialog({				
		autoOpen: false,
		height:380,
		resizable:false,
		width: 430
	});
	//添加图片的dialog
	$("#model").dialog({				
		autoOpen: false,
		/*添加图片*/
		title:getJsLocaleMessage("common","common_addPicture"),
		height:200,
		width: 310
	});
	//添加声音的dialog
	$("#model2").dialog({				
		autoOpen: false,
		/*添加声音*/
		title:getJsLocaleMessage("common","common_addSound"),
		height:200,
		width: 310
	});
	//点击调用素材
	$("#addMat").click(function(){		
		var focusFrameId = $(".mmsFrame").attr("id");
		if(focusFrameId == "addFrame"){
			//如果当前选中帧为“添加帧按钮”，则return
			/*请选择帧后再调用素材文件！*/
			alert(getJsLocaleMessage("common","common_js_addMMS_1"));
			return ;
		}
		$("#mmsMat").dialog("open");
	});
	//点击添加图片
	$("#addImg").click(function(){		
		var focusFrameId = $(".mmsFrame").attr("id");
		if(focusFrameId == "addFrame"){
			//如果当前选中帧为“添加帧按钮”，则return
			/*请选择帧后再上传图片！*/
			alert(getJsLocaleMessage("common","common_js_addMMS_2"));
			return ;
		}
		$("#model").dialog("open");
	});
	//点击添加声音
	$("#addMusic").click(function(){		
		var focusFrameId = $(".mmsFrame").attr("id");
		if(focusFrameId == "addFrame"){
			//如果当前选中帧为“添加帧按钮”，则return
			/*请选择帧后再上传声音文件！*/
			alert(getJsLocaleMessage("common","common_js_addMMS_31"));
			return ;
		}
		$("#model2").dialog("open");
	});
	//彩信预览的dialog
	$("#mscan").click(function(){					
		count = 0;
		//初始化dialog里的数据	
		inits();			
		//获取指定的帧的id
		var id = $(".mmsFrame").attr("id");	
		//获取该帧的数据，并且放入数组
		saveframe(id);								
		ttime = 0;
		//循环该数组中的帧内容  获取其每个的播放时间
		for (var i=0;i<arr.length;i++)				
		{
			var as = arr[i];
			as=as.replace(/\r\n/g,"<BR/>");
		    as=as.replace(/\n/g,"<BR/>"); 
			var data = $.parseJSON(as);  
		    if (data != null && data != "")
		    {
		    	if(data.ImgUrl != "" || data.MusUrl!=""|| data.textContent!="")
		    	{
		    		count++;
		    		ttime = parseInt(ttime)+parseInt(data.lasttime);		//播放时间
		    	}
		    }
		}
		index = 0;
		clearInterval(timer);  
		timer = null;
		$("#screen").empty();
		$("#pointer").empty();
		$("#nextpage").empty();
		$("#currentpage").empty();
		$("#inputParamCnt1").empty();
		//$("#screen").html("<center style='padding-top:120px'><img src='"+pu+"/images/play.png' style='cursor:pointer;width:48;height:48' title='播放' onclick='javascript:play();'/></center>");
       // $("#screen").html("<center style='padding-top:80px;'><img src='"+pu+"/images/play.png' style='cursor:pointer;width:48;height:48' title='播放' onclick='javascript:setScreen();setPer();'/></center>");

	    var eg = /#[pP]_[1-9][0-9]*#/g;
	    var paramCnt =0;//参数个数
	    for (var i = 0; i <count; i++)
        {          
           var textt1 = arr[i];
           textt1=textt1.replace(/\r\n/g,"<BR/>")   
		   textt1=textt1.replace(/\n/g,"<BR/>"); 
    	   var data1 = $.parseJSON(textt1);
    	   if(data1.textContent != null)
    	   {
    			var arr1 = data1.textContent.match(eg);//返回数组 
    			if(arr1 != null)
    			{
	    			for(var j=0;j<arr1.length;j++)
	    			{
	    				var rstr = arr1[j].toUpperCase(); 
						var pc = rstr.substring(rstr.indexOf("#P_")+3,rstr.lastIndexOf("#"));
						var pci = parseInt(pc);
						if(pci > paramCnt)
						{
							paramCnt = pci;
						}
	    			}
    			}
    	   }
        }
	   var dsFlag = $("input[name='templateType']:checked").val();
		if(dsFlag == 1 && paramCnt>0)
		{
			$("#previewTemp").dialog('option', 'width', "500");
    		var str;
    		var strs="";
    		for(var i=0;i<paramCnt;i++)
    		{
    			/*参数*/
        		str=getJsLocaleMessage("common","common_parameter")+(i+1)+': &nbsp;<input type="text" id="pa'+i+'" value=""/></br><br/>';
        		strs=strs+str;
    		}
			$("#inputParamCnt1").html(strs);
		}
		else
		{
   			$("#previewTemp").dialog('option', 'width', "290");
		}
	    $("#previewTemp").dialog("open");
		play();
	});
});


function doShowDiv(div)
{   
	$("#"+div).css("display","block");
}
function change(a,b,c,d,e)
{
	if(e==1)
	{
		$("#type"+a).css("background","#85B6E2");
		$("#type"+b).css("background","#FFFFFF");
		$("#"+c).toggle();
		$("#"+d).toggle();
	}
	else
	{
		$("#type"+a).css("background","#85B6E2");
		$("#type"+b).css("background","#FFFFFF");
		$("#"+c).toggle();
		$("#"+d).toggle();
	}
}
//根据上传的字符判断     这边是判断它的上传类型是否合法        先判断是上传图片还是上传声音
function check(type){		
	flag = true;
	if (type == "chooseImg"){
		var imgs = $.trim($("#chooseImg").val());
		if(imgs != ""){
	    	imgs = imgs.toString().substring( imgs.toString().lastIndexOf(".")+1,imgs.toString().length);
	    	imgs = imgs.toUpperCase();
	    }
		if (imgs == "")
		{
			/*请选择要上传的图片文件！*/
			alert(getJsLocaleMessage("common","common_frame2_uploadLogo_6"));
			flag = false;
		}else{
			if(imgs == 'JPG' ||imgs == 'GIF'  || imgs == 'JPEG'){
				
			}
			else
		    {
		    	/*不支持的图片格式！*/
		        alert(getJsLocaleMessage("common","common_frame2_uploadLogo_7"));
		        flag =  false;
		    }
		}
	}else if(type == "chooseMusic"){
		var musics = $.trim($("#chooseMusic").val());
		if (musics == "")
		{
			/*请选择要上传的声音文件！*/
			alert(getJsLocaleMessage("common","common_js_addMMS_3"));
			flag = false;
		}
		else if (musics != "" && !(/^(mid|midi|amr)$/.test(musics.toString().substring((musics.toString().lastIndexOf("."))+1).toLowerCase())))
	    {
	    	/*不支持的声音格式！*/
	        alert(getJsLocaleMessage("common","common_js_addMMS_4"));
	        flag =  false;
	    }
	}else if(type == "chooseTms"){
		var tms = $.trim($("#chooseTms").val());
		if (tms == ""){
			/*请选择要上传的彩信文件！*/
			alert(getJsLocaleMessage("common","common_js_addMMS_5"));
			flag = false;
		}else {
			tms = tms.substring(tms.lastIndexOf(".")+1,tms.length);
	    	if(tms != "tms" && tms != "TMS"){
	    		/*不支持的彩信格式！*/
	    		alert(getJsLocaleMessage("common","common_js_addMMS_6"));
		        flag =  false;
	    	}
	    }
	}
	return flag;
}

function sizecheck(name)
{
       var MAXSIZE = 80 * 1024;
       var obj = document.getElementById(name);
       var image = new Image();
       image.src= obj.value;         
       var filesize=image.fileSize; 
       var flag = false;
       if ($.browser.msie) {//查看是否是IE
       		if(obj.readyState == "complete") {
       			if (filesize <= MAXSIZE) {
       				flag = true;
       			}
       		}
       	} else {
       		var file = $("input:file[name='"+name+"']")[0];

       		if (file.files[0].fileSize <= MAXSIZE) {
       			flag = true;
       		}
       	}
       	return flag
}
//这里是上传图片 和声音
function doUp(type)	{
	var lgcorpcode = $("#lgcorpcode").val();
	var pathUrl = $("#pathUrl").val();
	if (check(type)){		//这里判断上传的 图片或者声音是否合法	
		if(type == "chooseImg"){
			$.ajaxFileUpload ({ 
			    url:pathUrl+'/tem_mmsTemplate.htm?method=upload&lgcorpcode='+lgcorpcode, //处理上传文件的服务端 
			    secureuri:false, 								//是否启用安全提交，默认为false
			    fileElementId:type, 							//与页面处理代码中file相对应的ID值
			    dataType: 'json', 								//返回数据类型:text，xml，json，html,scritp,jsonp五种
			    success: function (data) { 
				    if(data != null && data.url == "false")
				    {
				    	/*上传的文件大小不能超过80KB！*/
				    	alert(getJsLocaleMessage("common","common_js_addMMS_7"));
				    	return;
				    }
				    else if(data != null && data.url != "false")
				    {
				    	 setImg(data.url,data.width,data.height,data.size);			//将该值在页面中设置出该图片文件的属性					//图片url    宽     高      图片大小
					    getToatalSize(new Number(parseFloat(data.size)/1024).toFixed(2)+"","+");	//获取所有图片 的最大KB量
					    getFrameTotalSize();
						divClose("chooseImg");
						$("#model").dialog("close");
				    }
			  } 
			  });
		}else if(type == "chooseMusic"){
			$.ajaxFileUpload ({ 
			    url:pathUrl+'/tem_mmsTemplate.htm?method=upload&lgcorpcode='+lgcorpcode, //处理上传文件的服务端 
			    secureuri:false, //是否启用安全提交，默认为false
			    fileElementId:type, //与页面处理代码中file相对应的ID值
			    dataType: 'json', //返回数据类型:text，xml，json，html,scritp,jsonp五种
			    success: function (data) { 
				 if(data != null && data.url == "false")
				    {
                        /*上传的文件大小不能超过80KB！*/
                        alert(getJsLocaleMessage("common","common_js_addMMS_7"));
				    	return;
				    }
				    else if(data != null && data.url != "false")
				    {
				    setMus(data.url,data.size);			//将该值在页面中设置出该音乐文件的属性
				    getToatalSize(new Number(parseFloat(data.size)/1024).toFixed(2)+"","+");	
				    getFrameTotalSize();
					divClose("chooseMusic");
					$("#model2").dialog("close");
				    }
			  } 
			  });
		}
	}
}
//这里是将图片将上传上来的图片放到摸板中
function setImg(url,width,height,size)				
{
	var pathUrl = $("#pathUrl").val();
	var w = parseFloat(width);						//宽
	var h = parseFloat(height);						//高
	var padding = parseFloat("0");
	if (w-170>=0)
    {
    	w = w*(160/w);
    	h = h*(160/h);
    }
    if (h-150>=0)
    {
    	w = w*(140/h);
    	h = h*(140/h);
    }
    padding = (140-h)/2;
    var w2 = parseFloat(width);						//宽
	var h2 = parseFloat(height);	
	if (w2-58>=0){
    	w2 = w2*(58/w2);
    	h2 = h2*(58/h2);
    }
    if (h2-52>=0){
    	w2 = w2*(58/w2);
    	h2 = h2*(52/h2);
    }
    var padding2 = -2;// (52-h2)/2
    $("#isize").empty();		//当前图片文件大小
    $("#isize").html(new Number(parseFloat(size)/1024).toFixed(2));
    $("#delImg").show();		//显示该删除该图片按钮
    $("#ImgUrl").attr("value",url);	//隐藏域中该图片的url
    $("#preview").empty();			//清空摸板图片里的内容
	$("#preview").append("<center><img src=\'"+pathUrl+"/"+url+"\' style=\'width:"+w+"px;height:"+h+"px;margin-top:"+padding+"px;\' frameStyle=\'width:"+w2+"px;height:"+h2+"px;margin-top:"+padding2+"px;\' /></center>");
	var id = $(".mmsFrame").attr("id");
	$("#"+id+" center").html("<img src=\'"+pathUrl+"/"+url+"\' style=\'width:"+w2+"px;height:"+h2+"px;margin-top:"+padding2+"px;\' align=\'center\' />");
	
	//将上传的图片放到该摸板中
}

function setMus(url,size)
{
	$("#msize").empty();					//当前声音文件大小
    $("#msize").html(new Number(parseFloat(size)/1024).toFixed(2));
    $("#soundPlay").html("<embed id='swfEmbed' src='"+url+"' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='70' height='40'></embed>");
    $("#delMus").show();					//显示[删除当前声音]按钮
    $("#MusUrl").attr("value",url);			//隐藏域中该音乐文件的url
}

function setMsg(msg)
{
	$("#textContent").attr("value","");
	$("#textContent").attr("value",msg);
}

function divClose(p)
{
	if (navigator.appName == "Netscape")
    {
		$("#"+p).attr("value","");
    }
	else
	{
		document.getElementById(p).select();
	    document.selection.clear();
	}
}
//获取图片以及声音的总KB
function getToatalSize(size,method)					
{
	var curframe = $(".mmsFrame").attr("id");		//  当上传图片或者声音的时候,统计该KB量
	var i = $("#isize").html();
	var m = $("#msize").html();
	var newsize = parseFloat(parseFloat(i)+parseFloat(m));
	arrs[curframe-1] = new Number(newsize).toFixed(2);
	var ts = parseFloat("0");
	for (var i=0;i<arrs.length;i++)
	{
		ts += parseFloat(arrs[i]);
	}
	var totalsize = parseFloat(new Number(ts).toFixed(2)+"");
	if (totalsize >parseFloat(80))
	{
		/*当前彩信已经超过最大值80KB了，请删除一些内容！*/
		alert(getJsLocaleMessage("common","common_js_addMMS_8"));
	}
	$("#totalSize").empty();				//当前彩信最大的KB量
	$("#totalSize").html(new Number(totalsize).toFixed(2));
}
//获取当前选中帧的图片以及声音的总KB
function getFrameTotalSize()					
{
	var curframe = $(".mmsFrame").attr("id");		//  当上传图片或者声音的时候,统计该KB量
	var i = $("#isize").html();
	var m = $("#msize").html();
	var newsize = parseFloat(parseFloat(i)+parseFloat(m));
	$("#frameTotalSize").empty();				//当前彩信最大的KB量
	$("#frameTotalSize").html(new Number(newsize).toFixed(2));
}
//删除模板中的图片或者声音
function delatt(d)				
{
	var pathUrl = $("#pathUrl").val();
	var sourceUrl = $("#"+d).attr("value");			//获取的是该因声或者图片的url
	if (sourceUrl!="")
	{
		$.post(pathUrl+"/tem_mmsTemplate.htm?method=delSource",{sourceUrl:sourceUrl},function(result){
	    	if (result != null && result != "")
	    	{
	    		if (d=="ImgUrl")					//图片url
	    		{
	    			var s = $("#isize").html();		//该图片的KB大小
	    			$("#ImgUrl").attr("value","");	//隐藏域的图片url
	    			$("#preview").empty();			//模板里的内容
	    			$("#isize").empty();
	       		 	$("#isize").html("0.00");
	       		    $("#delImg").hide();			//删除文字隐藏
	       		 	getToatalSize(s,"-");
	       			getFrameTotalSize();
		       		var id = $(".mmsFrame").attr("id");
		       		$("#"+id+" center").html("");
	    		}
	    		else								//声音url
	    		{
	    			var s2 = $("#msize").html();
	    			$("#MusUrl").attr("value","");
	    			 $("#msize").empty();
	    			 $("#soundPlay").empty();
					 $("#msize").html("0.00");
					 $("#delMus").hide();
					 getToatalSize(s2,"-");
					 getFrameTotalSize();
	    		}
	    	}
	    });	
	}
}

function setCont(img,sou,msg,imginfo,souinfo)
{
	if (img != "null" && sou != "null")
	{
		var infos = (imginfo+"").split(";");
		setImg(img,infos[0],infos[1],infos[2]);
		setMus(sou,souinfo);
		getToatalSize(new Number((parseFloat(infos[2])+parseFloat(souinfo))/1024).toFixed(2)+"","+")
	}
	else
	{
		if (img != "null" && imginfo != "null")
		{
			var infos = (imginfo+"").split(";");
			setImg(img,infos[0],infos[1],infos[2]);
			getToatalSize(new Number(parseFloat(infos[2])/1024).toFixed(2)+"","+");
		}
		else
		{
			$("#isize").empty();
		    $("#isize").html("0.00");
		    $("#delImg").hide();
		    $("#ImgUrl").attr("value","");
		    $("#preview").empty();
		    getToatalSize("0.00","+");
		}
		if (sou != "null" && souinfo != "null")
		{
			setMus(sou,souinfo);
			getToatalSize(new Number(parseFloat(souinfo)/1024).toFixed(2)+"","+");
		}
		else
		{
			$("#msize").empty();
		    $("#msize").html("0.00");
		    $("#delMus").hide();
		    $("#MusUrl").attr("value","");
		    getToatalSize("0.00","+");
		}
	}
	if (msg != "null")
	{
		setMsg(msg);
	}
	else
	{
		setMsg("");
	}
}

function addApp(img,sou,msg,imginfo,souinfo)
{
	if (img != "null" && imginfo != "null")
	{
		var infos = (imginfo+"").split(";");
		setImg(img,infos[0],infos[1],infos[2]);
		getToatalSize(new Number(parseFloat(infos[2])/1024).toFixed(2)+"","+");
		getFrameTotalSize();
	}
	if (sou != "null" && souinfo != "null")
	{
		setMus(sou,souinfo);
		getToatalSize(new Number(parseFloat(souinfo)/1024).toFixed(2)+"","+");
		getFrameTotalSize();
	}
	/*if (msg != "null")
	{
		setMsg(msg);
	}
	else
	{
		setMsg("");
	}*/
}
//保存点击前的帧，然后展现点击的帧
function changeFrame(frameid)							
{
	var oid = $(".mmsFrame").attr("id");		//获取现在设焦点的贞
	saveframe(oid);								//保存该焦点的属性内容
	showFrame(frameid);							//然后将指定贞的属性内容显示在右边页面
}

//保存帧
function saveframe(oid)
{
	if(oid == "addFrame"){
		return ;
	}
	var img = $.trim($("#preview").html())+"";  //预览模板中的彩信图片	
	var style = "";
	var ImgUrl = $("#ImgUrl").attr("value");	//图片URL
	var MusUrl = $("#MusUrl").attr("value");	//声音URL	
	var isize = $("#isize").html();				//图片KB量
	
	var msize = $("#msize").html();				//声音KB量
	var lasttime = $("#lasttime").attr("value");	//持续时间
	var	textContent = $("#textContent").val();		//文字中的内容
	textContent=textContent.replace(/\\/g,"\\\\");
	textContent=textContent.replaceAll("\"","\\\"");  
	//textContent=textContent.replaceAll("\\","\\\\");   
    //textContent=textContent.replace(/\n/g,"<BR>"); 
	if(img != "")
	{
		//存在隐患。取的是从第5个字符开始的
		//img = ($("#preview").children("center").children("img").attr("src")+"").substring(5);
		var temp = $("#preview").children("center").children("img").attr("src")+"";			//获取服务器上的url
		temp = temp.substring(1,temp.length);
		img = temp.substring(temp.indexOf("/")+1);
		
		
	    style = $("#preview").children("center").children("img").attr("style");
	}
	var content = '{\"img\":\"'+img+'\",\"style":\"'+style+'\",\"ImgUrl\":\"'+ImgUrl+'\",\"MusUrl\":\"'+MusUrl+'\",\"isize\":\"'
	+isize+'\",\"msize\":\"'+msize+'\",\"lasttime\":\"'+lasttime+'\",\"textContent\":\"'+textContent+'\"}';
	arr[parseInt(oid)-1] = content;	
}
//过滤斜杠
function guolvXG(text)
{
	var t=text.replace(/\\/g,"\\\\");
	return t;
}

//显示帧
function showFrame(id)		//将指定贞的属性内容显示在右边页面
{
	//当前帧大小
	$("#frameTotalSize").empty();				
	var pathUrl = $("#pathUrl").val();
	var newContent = "";
	if(id != "addFrame" && (parseInt(id)-1)>=0)
	{
		var ts = arr[parseInt(id)-1];
		ts=ts.replace(/\r\n/g,"<brrn/>");   
	    ts=ts.replace(/\n/g,"<brn/>");
		newContent = $.parseJSON(ts);  
	}
	if (newContent == null || newContent == "")			//如果是空贞的话
	{
		$("#preview").html("");
		$("#ImgUrl").attr("value","");
		$("#MusUrl").attr("value","");
		$("#isize").html("0.00");
		$("#delImg").hide();
		$("#msize").html("0.00");
		$("#delMus").hide();
		$("#lasttime").attr("value","10");
		$("#textContent").val("");
		$("#frameTotalSize").html("0.00");
	}
	else
	{
		$("#frameTotalSize").html(new Number(arrs[id-1]).toFixed(2));
		if(newContent.img !="")
		{
			$("#preview").html("<center><img src=\'"+pathUrl+"/"+newContent.ImgUrl+"\' style=\'"+newContent.style+"\' /></center>");
		}
		else
		{
			$("#preview").html("");
		}
		$("#ImgUrl").attr("value",newContent.ImgUrl);
		$("#MusUrl").attr("value",newContent.MusUrl);
		$("#isize").html(newContent.isize);
		if("0.00"==newContent.isize)
		{
			$("#delImg").hide();
		}
		else
		{
			$("#delImg").show();
		}
		$("#msize").html(newContent.msize);
		if("0.00"==newContent.msize)
		{
			$("#soundPlay").html("");
			$("#delMus").hide();
		}
		else
		{
		    $("#soundPlay").html("<embed id='swfEmbed' src='"+newContent.MusUrl+"' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='70' height='40'></embed>");
			$("#delMus").show();
		}
		$("#lasttime").attr("value",newContent.lasttime);
		var nr = newContent.textContent;
		nr=nr.replaceAll("<brrn/>","\r\n");   
	    nr=nr.replaceAll("<brn/>","\n");
		$("#textContent").val(nr);
	}
	
}
//添加贞
function addFrame()    
{
    var frameid = $("#framesize").attr("value");
    if (parseInt(frameid) >= 15)
    {
    	/*不能超过15帧！*/
    	alert(getJsLocaleMessage("common","common_js_addMMS_9"));
    	return;
    }
    else
    {
    	arr[(parseInt(frameid))] = '{"img":"","style":"","ImgUrl":"","MusUrl":"","isize":"0.00","msize":"0.00","lasttime":"10","textContent":""}';   //新加一个空贞放在数组里
    	var id = $(".mmsFrame").attr("id")==null?"1":$(".mmsFrame").attr("id");				//获取当前选中的贞   默认是 第一个贞
        var delFrame = '<div class="floatDel"><input type="button" class="deleteFrame" onclick="javascript:delFrame(this)"></div>';
    	var floatDiv = '<div class="floatFrame">'+
            '<input type="button" width="20px" class="moveUp" onclick="javascript:moveUp(this);" align="left"/>'+
            '<input type="button" width="20px" class="moveDown" align="right" onclick="javascript:moveDown(this)"/></div>';
        var textDiv = "<div class='textFrame'>"+(parseInt(frameid)+1)+"</div>";
        var div = '<div id="'+(parseInt(frameid)+1)+'" class="mmsFrames">'+
        textDiv+delFrame+floatDiv+'<center></center></div>';		
        if(frameid>=3){
        	$("#"+parseInt(frameid-2)).css("display","none");
        }
        $("#"+frameid).after($.trim(div)); 
        $("#curf").empty();
        /*当前*/
    	$("#curf").html(getJsLocaleMessage("common","common_current")+$.trim(id)+"/"+$.trim((parseInt(frameid)+1)+""));
    	$("#framesize").attr("value",(parseInt(frameid)+1));								//将贞数加1
    	$('#'+(parseInt(frameid)+1)).bind('click', function() {								//为该元素绑定一个CLICK事件
    		fClick((parseInt(frameid)+1));
		});
    	$('#'+(parseInt(frameid)+1)).hover(function() {
    		$(this).children("div .floatDel")[0].style.display="inline-block";
    		$(this).children("div .floatFrame")[0].style.display="inline-block";
		},function(){
			$(this).children("div .floatDel")[0].style.display="none";
			$(this).children("div .floatFrame")[0].style.display="none";
		});

    }
}
//删除贞
function delFrame(obj)				
{
	//获取焦点贞
	var id = $(obj).parent().parent().attr("id");//$(".mmsFrame").attr("id");								
	if(id == 1){
		/*第一帧不允许删除*/
		alert(getJsLocaleMessage("common","common_js_addMMS_10"));
		return;
    }
	//获取贞的数量
	var frameid = $("#framesize").attr("value");					
	if(frameid<1){
		/*没有可供删除的帧！*/
		alert(getJsLocaleMessage("common","common_js_addMMS_11"));
		return;
	}
	/*确定要删除当前帧吗？*/
	if(confirm(getJsLocaleMessage("common","common_js_addMMS_12")))
	{
		saveframe($(".mmsFrame").attr("id"));
		saveframe(id);
		//该贞的KB
		var fsize = 0.00;				
		//获取总KB数量
		var hsize = parseFloat($.trim($("#totalSize").html()));		
		//获取到选定的贞在数组中的位置
		if(arr[(parseInt(id)-1)] != null && arr[(parseInt(id)-1)] != "")		
		{
			var sp = arr[(parseInt(id)-1)];
			sp=sp.replace(/\r\n/g,"<BR/>");  
		    sp=sp.replace(/\n/g,"<BR/>");
			var data = $.parseJSON(sp);
			fsize += parseFloat(data.isize);			
			fsize += parseFloat(data.msize);				
			fsize = parseFloat(new Number(fsize).toFixed(2)+"");			// 该贞图片 和 声音  的总KB数
		}
		if (id == frameid)													//如果是删除最后一个贞
		{
			arr.pop();														//删除并返回数组的最后一个元素
			arr.push("");													//向数组的末尾添加一个或更多元素，并返回新的长度。
			$("#"+parseInt(frameid)).remove();								//将该模板贞移除掉
			$("#framesize").attr("value",(parseInt(frameid)-1));			//将贞的总数量减1
			$(".mmsFrame").addClass("mmsFrames");
			$(".mmsFrame").removeClass("mmsFrame");
			$("#"+(parseInt(id)-1)).addClass("mmsFrame");
			$("#"+(parseInt(id)-1)).removeClass("mmsFrames");
			showFrame((parseInt(id)-1));									//展示它之前的贞
			if($("#addFrame").css("display") == "none"){
	    		$("#addFrame").css("display","inline-block");
		    }
			//$("#curf").empty();
			//$("#curf").html("当前"+(parseInt(id)-1)+"/"+$.trim((parseInt(frameid)-1)+""));	//显示X/X的内容	
			arr[(parseInt(id)-1)]="";										//将数组清空对应的JSON
			arrs[(parseInt(id)-1)]="0.00";									//将该贞的KB清0
		}else{
			var tempHtml;
			for(var i=(parseInt(id)-1);i<(frameid-1);i++)	{			//循环该贞到最后的
				arr.splice(i,1,arr[i+1]);
				//帧图片循环替换
				tempHtml = $("#"+parseInt(parseInt(i)+2)+" center").html();
				tempHtml = tempHtml == null?"":tempHtml;
				$("#"+parseInt(parseInt(i)+1)+" center").html(tempHtml);
			}
			arr[frameid-1]="";											//将删除后的贞提前，并且把最后一个贞设置为""
			arr.push("");
			$("#"+parseInt(frameid)).remove();
			var showFrameSize = 0;
			var obj = $("#mframes").children();
			for(var i=0;i<obj.length;i++){
				if(obj[i].style.display == "" || obj[i].style.display == "inline-block"){
				    showFrameSize = parseInt(showFrameSize+parseInt(1));
				}
			}
			if(showFrameSize <4){
				if(parseInt(id)+parseInt(4) <= frameid){
					$("#"+parseInt(id)+parseInt(4)).css("display","inline-block");
				}else{
					$("#addFrame").css("display","inline-block");
				}
			}
			
			$("#framesize").attr("value",(parseInt(frameid)-1));
			showFrame(id);
			//$("#curf").empty();
			//$("#curf").html("当前"+$.trim(id)+"/"+$.trim((parseInt(frameid)-1)+""));
			arrs[(parseInt(frameid)-1)]="0.00";
		}
		$("#totalSize").html(new Number(hsize-fsize).toFixed(2)+"");
	}
}


function fClick(id)			//点击贞的时候触发这个 事
{
	//alert("*********");
	changeFrame(id);			//改变模板里的图片
	//var frameid = $("#framesize").attr("value");				//贞的数量
	changeFrameClass(id);
	//$("#curf").empty();											//显示哪一贞的
	//$("#curf").html("当前"+id+"/"+$.trim((parseInt(frameid))+""));
}

function doSubmit()
{
	var acc = $("#mmsacc").val();
	var id = $(".mmsFrame").attr("id");		//获取显示帧框的样式中的ID
	saveframe(id)
	var pathUrl = $("#pathUrl").val();
	var mmsTitle = $.trim($("#mmsTitle").attr("value"));	//模板名称
	var st = $("input:radio[name='state']:checked").attr("value");//模板状态
	
	if(acc=="false" && st=="1")
	{
		/*没有彩信发送账号，只能保存为草稿！*/
		alert(getJsLocaleMessage("common","common_js_addMMS_13"));
		return;
	}
	var contents = new Array();
	var index = 0;
	for (var i=0;i<arr.length;i++)
	{
		if ($.trim(arr[i]) != "")
		{
			var as = arr[i];
			as=as.replace(/\r\n/g,"<BR/>")   
		    as=as.replace(/\n/g,"<BR/>"); 
			var data = $.parseJSON(as);  
		    if (data != null && data != "")
		    {
		    	if(data.ImgUrl == "" && data.MusUrl=="" && data.textContent=="")
		    	{
		    		/*第 帧为空白帧，请修改！*/
		    		alert(getJsLocaleMessage("common","common_js_pageInfo_11")+(parseInt(i)+1)+getJsLocaleMessage("common","common_js_addMMS_14"));
					return;
		    	}
		    }
			contents[index] = arr[i];
			index++;
		}
	}
	var si = $.trim($("#totalSize").html());
	if(mmsTitle == "")
	{
		/*模板名称不能为空！*/
		alert(getJsLocaleMessage("common","common_js_addMMS_15"));
		return;
	}
	else if(/(\<{1,})|(\>{1,})|(\'{1,})|(\"{1,})|(\\{1,})/.test(mmsTitle)){
		/*模板名称中包含不允许的特殊字符(",',<,>,\)！*/
		alert(getJsLocaleMessage("common","common_js_addMMS_16"));
	    return;
	}
		
	if(contents.length <= 0)
	{
		/*模板内容不能为空！*/
		alert(getJsLocaleMessage("common","common_js_addMMS_17"));
		return;
	}
	if(parseFloat(si)-parseFloat(80)>0.00)
	{
		/*模板内容不能大于80KB！*/
		alert(getJsLocaleMessage("common","common_js_addMMS_18"))
		return;
	}
    else
	{
    	$("#save").attr("disabled","disabled");
    	$("#mscan").attr("disabled","disabled");
    	$("#cancel").attr("disabled","disabled");
    	var templateType = $("input[name='templateType']:checked").val();
    	var tmId = $("#tmId").val();
    	var opType = $("#opType").val();
    
    	var lgguid = $("#lgguid").val();
    	var lguserid = $("#lguserid").val();
    	var lgcorpcode = $("#lgcorpcode").val();
    	$.post(pathUrl+"/tem_mmsTemplate.htm?method=checkBadWord",{content:contents,lgcorpcode:lgcorpcode,templateType:templateType},function(result){
    		if (result != null && result == "")
    		{
    			$.post(pathUrl+"/tem_mmsTemplate.htm?method=update",{tmId:tmId,mt:mmsTitle,cont:contents,s:st,opType:opType,
    				templateType:templateType,lguserid:lguserid,lgcorpcode:lgcorpcode},function(result){
    				if(result != null && result == "overSize"){
    					/*tms文件不能大于80KB！*/
    					alert(getJsLocaleMessage("common","common_js_addMMS_19"));
    					$("#save").attr("disabled",false);
    					$("#mscan").attr("disabled",false);
    					$("#cancel").attr("disabled",false);
    		    		return;
    				}else if(result != null && result == "caogaotrue"){
    					/*保存草稿成功！*/
    					alert(getJsLocaleMessage("common","common_js_addMMS_20"));
    					location.href = pathUrl+"/tem_mmsTemplate.htm?lgguid="+lgguid;
    				}else if(result != null && result == "true")
    				{
    					if(opType == "add"){
    						/*模板新建成功！请等待运营商审核！*/
    						alert(getJsLocaleMessage("common","common_js_addMMS_21"));
        					//location.href = location;
    					}else if(opType == "edit"){
    						/*模板编辑成功！*/
    						alert(getJsLocaleMessage("common","common_js_addMMS_22"));
    					}else{
    						/*模板复制成功！*/
    						alert(getJsLocaleMessage("common","common_js_addMMS_23"));
    					}
    					location.href = pathUrl+"/tem_mmsTemplate.htm?lgguid="+lgguid;
    				}
    				else
    				{
    					/*操作失败！*/
    					alert(getJsLocaleMessage("common","common_operateFailed"));
    					$("#save").attr("disabled",false);
    					$("#mscan").attr("disabled",false);
    					$("#cancel").attr("disabled",false);
    		    		return;
    				}
    			});
    		}
    		else if(result !=null && result =="Pfalse" && templateType == 1)
    		{
    			/*内容中动态参数填写不合法!*/
    			alert(getJsLocaleMessage("common","common_js_addMMS_24"));
    			$("#save").attr("disabled",false);
				$("#mscan").attr("disabled",false);
				$("#cancel").attr("disabled",false);
    			return;
    		}else if(result != null && result == "noParam"  && templateType == 1)
    		{
    			/*请输入参数！*/
    			alert(getJsLocaleMessage("common","common_js_addMMS_25"));
    			$("#save").attr("disabled",false);
				$("#mscan").attr("disabled",false);
				$("#cancel").attr("disabled",false);
    	    	return ;
    		}
    		else if(result != null && result == "moreParam"  && templateType == 1)
    		{
    			/*模板参数最多20个！*/
    			alert(getJsLocaleMessage("common","common_js_addMMS_26"));
    			$("#save").attr("disabled",false);
				$("#mscan").attr("disabled",false);
				$("#cancel").attr("disabled",false);
    	    	return ;moreParam
    		}
    		else if(result != null && result != "")
    		{
    			/*内容包含关键字：*/
    			alert(getJsLocaleMessage("common","common_js_addMMS_27")+result);
    			$("#save").attr("disabled",false);
				$("#mscan").attr("disabled",false);
				$("#cancel").attr("disabled",false);
	    		return;
    		}
    	});
	}
}

function setScreen()
{  
	var pathUrl = $("#pathUrl").val();
	if (count != 0)			//是否帧  并且帧里面有图片 声音  内容
	{
		var id = $(".mmsFrame").attr("id");
		saveframe(id);
		if ($.trim(arr[index]) != "")
		{
			var as = arr[index];
			as=as.replace(/\r\n/g,"<BR/>");
		    as=as.replace(/\n/g,"<BR/>"); 
			var data = $.parseJSON(as);  
		    if (data != null && data != "")
		    {
		    	if(data.ImgUrl == "" && data.MusUrl==""&& data.textContent=="")
		    	{
		    		 index++;
				     refresh();
					return;
		    	}
		    }
		}
		if(arr[index] == "" || arr[index] == null)
	    {
		     index++;
		     refresh();
	    }
		else
		{
			$("#screen").css("margin-top","0px");
			window.clearInterval(timer); 
			var ars = arr[index];
			ars=ars.replace(/\r\n/g,"<BR/>")   
		    ars=ars.replace(/\n/g,"<BR/>");
			var data = $.parseJSON(ars);  
			index = index + 1; 
		    var html ="";
		    if (data.ImgUrl != null && data.ImgUrl != "")
		    {
		    	html = "<img style='width:170;' src='"+pathUrl+"/"+data.ImgUrl+"' /><p>";
		    }
		    if (data.textContent!= null)
		    {
			    var dataText=data.textContent;
			    var vals=$("#inputParamCnt1").find("input");
				for(var i=0;i<vals.length;i++)
				{
					if(vals.eq(i).val()!="")
					{
						dataText = dataText.replaceAll("#p_"+(i+1)+"#", vals.eq(i).val());
						dataText = dataText.replaceAll("#P_"+(i+1)+"#",vals.eq(i).val());
					}
				}
		    	data.textContent = dataText.replaceAll("<","&lt;").replaceAll(">","&gt;");
		    	data.textContent = data.textContent.replaceAll("&lt;BR/&gt;","<BR/>");
		    	data.textContent = getContentValChange(data.textContent);
		    	html=html+data.textContent;
			}
		    if (data.MusUrl != null && data.MusUrl != "")
		    {
		    	html = html+"<embed id='swfEmbed' src='"+pathUrl+"/"+data.MusUrl+"' quality='high' pluginspage='http://www.macromedia.com/go/getflashplayer' type='application/x-shockwave-flash' width='70' height='40'></embed>";
		    }
		    $("#screen").empty();
		    $("#screen").html(html);
		    $("#showtime").css("z-index","999");
			$("#pers").show();
		    $("#pointer").html(index+"/"+count);
		    //上一帧，下一帧 
		    $("#nextpage").html("&nbsp;&nbsp;&nbsp;<a href='#' style='TEXT-DECORATION:none;color:blue;' onclick='javascript:advanp("+index+");return false'>"+getJsLocaleMessage("common","common_js_mmsPreview_4")+"</a>&nbsp;&nbsp;<a id='next' href='#' style='TEXT-DECORATION:none;color:blue;' onclick='javascript:nextp("+index+");return false'>"+getJsLocaleMessage("common","common_js_mmsPreview_2")+"</a>&nbsp;&nbsp;<a href='#' style='TEXT-DECORATION:none;color:blue;' onclick='javascript:stopp("+index+");return false'>"+getJsLocaleMessage("common","common_stop")+"</a>");
			nplaytime = data.lasttime;  	      
		}
	}
	else
	{
		/*无内容，无法预览！*/
		alert(getJsLocaleMessage("common","common_js_addMMS_28"));
		return;
	}
}
//上一帧事件
function advanp(a){
	nextpage1 = 1;
}

//下一帧事件
function nextp(a){
	if(a == count)
	{
		/*已经是最后一帧了*/
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
    for (var i = 0; i < a; i++)
    {
       var textt1 = arr[i];
       textt1=textt1.replace(/\r\n/g,"<BR/>")   
	   textt1=textt1.replace(/\n/g,"<BR/>"); 
	   var data1 = $.parseJSON(textt1);			
       alltime = alltime+parseInt(data1.lasttime);
    }
    first = alltime;
    cplaytime = nplaytime;
    $("#screen").empty();
    refresh();
}
function refresh()
{
	var commonPath = $("#commonPath").val();
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
		$("#screen").empty();
		$("#nextpage").empty();
		$("#currentpage").empty();
		$("#screen").html("<center style='padding-top:120px'><img src='"+commonPath+"/common/img/play.png' style='cursor:pointer;' title='"+getJsLocaleMessage("common","common_play")+"' onclick='javascript:play();'/></center>");
	}
   timer = null;
}
//播放方法
function play(){
    var showinfo="";
    for (var i = 0; i < count; i++)
    {
       showinfo=showinfo+"<a href='#' style='color:blue;' onclick='javascript:currentPage("+i+");return false'>"+(i+1)+"</a>&nbsp;";
    }
    $("#currentpage").html(showinfo);
	setScreen();
	if (count != 0)
	{
       setPer();
	}
}
function setPer()
{
	ttimer = setInterval("reTimer()",1000);
}
//初始化
function inits()						
{
	window.clearInterval(timer);		//取消由setInterval()方法设置的定时器
	window.clearInterval(ttimer);  
	timer = null;
	index = 0;
	ttimer = null;
	first = 0;
	ide = 1;
	$("#showtime").css("z-index","0");
	$("#showtime").html("");
	document.getElementById("chart").style.width = 0+"%";
	$("#pers").hide();
	$("#screen").css("margin-top","86px");
}

function reTimer()
{
	first++;
	cplaytime++;
	if(first <= ttime)
	{
		document.getElementById("chart").style.width = (first*100/ttime)+"%";
		var s = (ttime-first)<10?"0"+(ttime-first):(ttime-first);
		$("#showtime").html("0:"+s);
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
			if(index == 1){
				/*已经是第一帧了*/
				alert(getJsLocaleMessage("common","common_js_mmsPreview_1"));
			}
			else{
				//第一帧播放的内容
			    var textshe = guolvXG(arr[index-1]);
				var datashe = $.parseJSON(textshe);
				//当前帧的上一帧的时间
				nplaytimeshe = datashe.lasttime;
				first = parseInt(first) - (parseInt(nplaytimeshe) + parseInt(cplaytime));
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
//向上滑动帧
function toUp(){
	var obj = $("#mframes").children();
	var showFrameId;
	var showFrameSize = 0;
	for(var i=0;i<obj.length;i++){
		if(obj[i].style.display == "" || obj[i].style.display == "inline-block"){
		    showFrameSize = parseInt(showFrameSize+parseInt(1));
		}
	}
	for(var i=0;i<obj.length;i++){
		if(obj[i].style.display == "" || obj[i].style.display == "inline-block"){
		//当前显示的第一帧
		    showFrameId = obj[i].id;
		    break;
		}
	}
	var frameSize = $("#framesize").attr("value");
	var nextFocus = showFrameId == "addFrame"?frameSize:showFrameId-1;
	if(nextFocus >=1 ){
		//向上滚动之前先保存当前选中的帧
		var curFrameId = $(".mmsFrame").attr("id");;
    	if(curFrameId !="addFrame"){
    		saveframe(curFrameId);
    	}
    	showFrameId = nextFocus;
    	if(showFrameSize == 3){
    	   //如果当前显示的帧数等于3，填充完整4帧
    	    $("#"+nextFocus).css("display","inline-block");
    	}else if(showFrameSize == 2){
    		$("#"+nextFocus).css("display","inline-block");
    		if(nextFocus-1 >=1){
    			$("#"+parseInt(nextFocus-1)).css("display","inline-block");
    			showFrameId = nextFocus-1;
    		}
    	}else if(showFrameSize == 1){
    		$("#"+nextFocus).css("display","inline-block");
    		if(nextFocus-1 >=1){
    			$("#"+parseInt(nextFocus-1)).css("display","inline-block");
    			showFrameId = nextFocus-1;
    		}
    		if(nextFocus-2 >=1){
    			$("#"+parseInt(nextFocus-2)).css("display","inline-block");
    			showFrameId = nextFocus-2;
    		}
    	}else if(showFrameSize == 4){
    		showFrameId = nextFocus;
    		if($("#addFrame").css("display") == "inline-block"){
	    		$("#addFrame").css("display","none");
		    }else{
		   	     $("#"+parseInt(nextFocus+4)).css("display","none");
		    }
		    $("#"+nextFocus).css("display","inline-block");
    	}
    	
    	changeFrameClass(showFrameId);
	    showFrame(showFrameId);
	}
}
//向下滑动帧
function toDown(){
	var obj = $("#mframes").children();
	var showFrameId;
	for(var i=0;i<obj.length;i++){
		if(obj[i].style.display == "" || obj[i].style.display == "inline-block"){
		//当前显示的最后一帧
		    showFrameId = parseInt(obj[i].id);
		}
	}
	
	var frameid = parseInt($("#framesize").attr("value"));
	var nextFocus = parseInt(showFrameId+1); 
	if(nextFocus <=frameid+1 && frameid > 3 ){
		//向下滚动之前先保存当前帧
    	var curFrameId = $(".mmsFrame").attr("id");
    	if(curFrameId !="addFrame"){
    		saveframe(curFrameId);
    	}
	    if(nextFocus > frameid){
	    	$("#addFrame").css("display","inline-block");
	    	changeFrameClass("addFrame");
	    }else{
	   	     $("#"+nextFocus).css("display","inline-block");
	   	     changeFrameClass(nextFocus);
			 showFrame(nextFocus);
	    }
	    $("#"+parseInt(nextFocus-4)).css("display","none");
	}
}
//上移
function moveUp(obj){
	var curFrameId = $(obj).parent().parent().attr("id");//$(".mmsFrame").attr("id");
	if(curFrameId == 1){
		/*已经是第一帧，无法上移！*/
		alert(getJsLocaleMessage("common","common_js_addMMS_29"));
	}else{
	    var tempContent = arr[parseInt(curFrameId)-2];	
	    //上移之前先保存当前选中帧
	    saveframe($(".mmsFrame").attr("id"));
	    //上下帧交换
		arr[parseInt(curFrameId)-2] = arr[parseInt(curFrameId)-1];	
		arr[parseInt(curFrameId)-1] = tempContent;
		
		 //上下帧大小交换
		var tempSize = arrs[parseInt(curFrameId)-2];	
		arrs[parseInt(curFrameId)-2] = arrs[parseInt(curFrameId)-1];	
		arrs[parseInt(curFrameId)-1] = tempSize;
		
		//显示上一帧
		var nextImg = $("#"+(curFrameId-1)+" center").html();// $("#"+(curFrameId-1)+" img").attr("src");
		var curImg = $("#"+curFrameId+" center").html();//$("#"+curFrameId+" img").attr("src");
		nextImg = nextImg ==null?"":nextImg;
		curImg = curImg == null ?"":curImg;
		$("#"+(curFrameId-1)+" center").html(curImg);
		$("#"+curFrameId+" center").html(nextImg);
		//改变帧样式
		changeFrameClass(curFrameId);
		showFrame(curFrameId);
		//改变选中的帧
		/*
		var frameid = $("#framesize").attr("value");				//贞的数量
		$("#curf").empty();											//显示哪一贞的
		$("#curf").html("当前"+(curFrameId-1)+"/"+$.trim((parseInt(frameid))+""));
		*/
	}
}
//下移
function moveDown(obj){
	var frameSize = $("#framesize").attr("value");
	var curFrameId = $(obj).parent().parent().attr("id");// $(".mmsFrame").attr("id");
	if(frameSize == curFrameId){
		/*已经是最后一帧，无法下移！*/
		alert(getJsLocaleMessage("common","common_js_addMMS_30"));
	}else{
		//上移之前先保存当前选中帧
	    saveframe($(".mmsFrame").attr("id"));
		var tempContent = arr[parseInt(curFrameId)];	
	    //上下帧交换
		arr[parseInt(curFrameId)] = arr[parseInt(curFrameId)-1];	
		arr[parseInt(curFrameId)-1] = tempContent;
		
		//帧大小交换
		var tempSize = arrs[parseInt(curFrameId)];	
		arrs[parseInt(curFrameId)] = arrs[parseInt(curFrameId)-1];	
		arrs[parseInt(curFrameId)-1] = tempSize;
		
		//下一帧
		var nextFrameId = parseInt(parseInt(curFrameId)+1);
		//显示下一帧
		var nextImg =$("#"+nextFrameId+" center").html();// $("#"+nextFrameId+" img").attr("src");
		var curImg = $("#"+curFrameId+" center").html();// $("#"+curFrameId+" img").attr("src")
		nextImg = nextImg ==null?"":nextImg;
		curImg = curImg == null ?"":curImg;
		$("#"+nextFrameId+" center").html(curImg);
		$("#"+curFrameId+" center").html(nextImg);
		//改变帧样式
		changeFrameClass(nextFrameId);
	   // alert("下移之后显示："+nextFrameId);
		showFrame(nextFrameId);
		//改变选中的帧
		//var frameid = $("#framesize").attr("value");				//贞的数量
		//$("#curf").empty();											//显示哪一贞的
		//$("#curf").html("当前"+nextFrameId+"/"+$.trim((parseInt(frameid))+""));
	}
}

function changeFrameClass(frameId){
	$(".mmsFrame").addClass("mmsFrames");
     $(".mmsFrame").removeClass("mmsFrame");	
     //将显示的帧加CSS		
     $("#"+frameId).removeClass("mmsFrames");			
	 $("#"+frameId).addClass("mmsFrame");
}

function confirmMat(){
	var type = $("#type").val();
	var address = $("#address").val();
	var size = $("#size").val();
	if ("MP3" != type &&"MID" != type && "WAV" != type){
	    var width = $("#width").val();
	    var height = $("#height").val();
		doApp(address,width,height,size);
	}else{
		doApp2(address,size);
	}
	$("#mmsMat").dialog("close");
}
function selectType(){
	var templateType = $("input[name='templateType']:checked").val();
	if(templateType == 0){
		$("#paramTxt").hide();
	}else{
		$("#paramTxt").show();
	}
}

function getContentValChange(msg){
	var reg=/#[pP]_(.*?)#/g;
	msg=msg.replace(reg,replacerChange);
	return msg;
}