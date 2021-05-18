$(document).ready(function() {
	$("#tempType option[value='"+$("#tempTypeValue").val()+"']").attr('selected','selected');
	$('#tempType').isSearchSelect({'width':'152','isInput':false});
	$('#accuntnum').isSearchSelect({'width':'152','width_ul':'270','isInput':false});
	getLoginInfo("#getloginUser");
	$("#toggleDiv").toggle(function() {
		$("#condition").hide();
		$(this).addClass("collapse");
	}, function() {
		$("#condition").show();
		$(this).removeClass("collapse");
	});
	$("#content tbody tr").hover(function() {
		$(this).addClass("hoverColor");
	}, function() {
		$(this).removeClass("hoverColor");
	});
		
	initPage($("#_totalPage").val(),$("#_pageIndex").val(),$("#_pageSize").val(),$("#_totalRec").val());
	$("#search").click(function(){ submitForm(); });
	
	$("#condition div.c_selectBox").css("width","152px").children("input").css("width","148px");
	$("#content td").hover(function(){
		if($(this).find(".titleTip").size()>0){
			$(this).attr("title",$.trim($(this).find(".titleTip").text()));
		}
		},function(){
			$(this).removeAttr("title");
		}
	);
});
var path=$("#path").val();
function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)   
	{
		a[i].checked =e.checked; 
	} 
}
function rtime(){
    //var max = "2099-12-31 23:59:59";
    var max = "2099-12-31";
    var v = $("#submitSartTime").attr("value");
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
		//max = year+"-"+mon+"-"+day+" 23:59:59"
		max = year+"-"+mon+"-"+day+"";
	}
	//WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});

};

function stime(){
   // var max = "2099-12-31 23:59:59";
   var max = "2099-12-31";
    var v = $("#submitEndTime").attr("value");
  //  var min = "1900-01-01 00:00:00";
   var min = "1900-01-01";
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
	//	min = year+"-"+mon+"-01 00:00:00"
		min = year+"-"+mon+"-01";
	}
	//WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

};
function addNewText(){
    location.href="weix_keywordReply.htm?method=newText&pageSize="+$("#pageSize").val()+"&pageIndex="+$("#txtPage").val()+"&lgcorpcode="+$("#lgcorpcode").val();
}
function addNexImg(type){
	location.href="weix_keywordReply.htm?method=addImgTmp&type="+type+"&pageSize="+$("#pageSize").val()+"&pageIndex="+$("#txtPage").val()+"&lgcorpcode="+$("#lgcorpcode").val();
}

//删除单个模板
function deltemple(tempid){
		//判断单个模板是否被关联了，关联弹出提示，否则调用删除方法
		$.get(path+"/weix_keywordReply.htm?method=checkRelated&ran="+getRandomNum(),
			{tempid:tempid},
			function(data){
		        	if(data!=null&&data=="true")
                     {
		        		alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_45"));
                     }else{
         				if(confirm(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_46"))){
        					$.post(path+"/weix_keywordReply.htm?method=delTemple&ran="+getRandomNum(),
        						{tempIds:tempid},
        						function(r){
        					        	if(r!=null&&r=="sccuess")
        			                     {
        			                     	alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_29"));
        			                     	document.forms['pageForm'].submit();
        			                     }
        			                     else if(r!=null && r=="fail")
        			                     {
        			                       alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_47"));
        			                     }
        			                     else
        			                     {
        			                         alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_47"));
        			                     }
        			               });
        		       }	
	                }            
             });
}

//批量删除
function delBk()
{

	var selected=document.getElementsByName("checklist");
	var lguserid=$("#lguserid").val();
	var lgcorpcode=$("#lgcorpcode").val();
	var n=0;		//统计勾选中的个数
	var id="";
	for(var i=0;i<selected.length;i=i+1){
		if(selected[i].checked==true){
			id=id+selected[i].value;
			id=id+",";
			n=n+1;
		}
	}
	id=id.substring(0,id.lastIndexOf(','));
	if(n<1){alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_48"));return;}

	$.get(path+"/weix_keywordReply.htm?method=getRelatedTemples&ran="+getRandomNum(),
			{tempid:id},
			function(r){
				var wms = getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_49");
		        var juid = r.split("-");
		        var a = juid[0].replace("k",""); 
		        var b = juid[1].replace("k","");
		        if(a!=""&&a.split(",").length>0){
			        wms = getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_60");
			        var arr = a.split(",");
					var msg = getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_50");
					for(var i in arr){
							msg = msg +($("#"+arr[i]).val()+",");
						}
					msg = msg +getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_51");
					alert(msg);
			    }
		        if(b!=""&&b.split(",").length>0){
		        	if(confirm(wms)){
						$.post(path+"/weix_keywordReply.htm?method=delTemple&ran="+getRandomNum(),
								{tempIds:b},
								function(r){
						            	if(r!=null&&r=="sccuess")
					                     {
					                     	alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_29"));
					                     	document.forms['pageForm'].submit();
					                     }
					                     else if(r!=null && r=="fail")
					                     {
					                       alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_47"));
					                     }
					                     else
					                     {
					                         alert(getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_47"));
					                     }
					               });
					}  
			    }         
               });

}
//回复预览
function preview(tempId){
	var url = path+"/weix_keywordReply.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
	showbox({src:url});
}	
//页面上的弹出框
function modify(t,i)
{
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	/*dlog = art.dialog.through({
	    content: document.getElementById('modify'),
	    id: 'modify',
	    lock: false
	});*/
	var title;
	if(i==1)
	{
		title = getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_52");
		//art.dialog.through({"id":"modify"}).title("回复名称");
	}
	else if(i==2)
	{
		title = getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_53");
		//art.dialog.through({"id":"modify"}).title("关键字");
	}
	else if(i==3){
		title = getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_54");
		//art.dialog.through({"id":"modify"}).title("回复内容");
	} else if(i==4){
		title = getJsLocaleMessage("wxgl","wxgl_qywx_nrhfgl_text_55");
		//art.dialog.through({"id":"modify"}).title("所属帐号");
	}

	dlog = art.dialog({
	    content: document.getElementById('modify'),
	    id: 'modify',
	    lock: false,
	    title: title
	});
	dlog.dialog('open');
}
function getRandomNum() {
    	var ran = parseInt(Math.random() * 10000);
    	return ran;
    }
function go(tid,msgtype,replname){
	replname= encodeURI(replname);
	replname= encodeURI(replname);
	var action=path+'/weix_keywordReply.htm?method=doEditemp&tid='+tid+'&msgtype='+msgtype+'&replname='+replname+"&lgcorpcode=" + $("#lgcorpcode").val();
	$("#pageForm").attr('action',action);
	$("#pageForm").submit();
	}