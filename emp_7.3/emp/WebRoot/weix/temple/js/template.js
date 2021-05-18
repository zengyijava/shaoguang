$(document).ready(function() {
	$("#tempType option[value='"+$("#type").val()+"']").attr('selected','selected');
	$('#tempType').isSearchSelect({'width':'180','isInput':false});
	$('#accuntnum').isSearchSelect({'width':'180','width_ul':'270','isInput':false});
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
	
	 $("#divBox").dialog({
			autoOpen: false,
			height:510,
			width: 300,
			modal: true,
			close:function(){
		  $("#msgPreviewFrame").attr("src","");
			}
		});
	

	$('#modify').dialog({
		autoOpen: false,
		width:250,
	    height:200
	});
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
		max = year+"-"+mon+"-"+day+""
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
		min = year+"-"+mon+"-01"
	}
	//WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
	WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});

};
function addNewText(){
    location.href="cwc_replymanger.htm?method=newText&pageSize="+$("#pageSize").val()+"&pageIndex="+$("#txtPage").val()+"&lgcorpcode="+$("#lgcorpcode").val();
}
function addNexImg(type){
	location.href="cwc_replymanger.htm?method=addImgTmp&type="+type+"&pageSize="+$("#pageSize").val()+"&pageIndex="+$("#txtPage").val()+"&lgcorpcode="+$("#lgcorpcode").val();
}

//删除单个模板
function deltemple(tempid){
		//判断单个模板是否被关联了，关联弹出提示，否则调用删除方法
		$.get(path+"/cwc_replymanger.htm?method=checkRelated&ran="+getRandomNum(),
			{tempid:tempid},
			function(data){
		        	if(data!=null&&data=="true")
                     {
		        		alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_42"));
                     }else{
         				if(confirm(getJsLocaleMessage("weix","weix_qywx_hfgl_text_43"))){
        					$.post(path+"/cwc_replymanger.htm?method=delTemple&ran="+getRandomNum(),
        						{tempIds:tempid},
        						function(r){
        					        	if(r!=null&&r=="sccuess")
        			                     {
        			                     	alert(getJsLocaleMessage("common","common_text_4"));
        			                     	document.forms['pageForm'].submit();
        			                     }
        			                     else if(r!=null && r=="fail")
        			                     {
        			                       alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_44"));
        			                     }
        			                     else
        			                     {
        			                         alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_44"))
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
	for(i=0;i<selected.length;i=i+1){
		if(selected[i].checked==true){
			id=id+selected[i].value;
			id=id+","
			n=n+1;
		}
	}
	id=id.substring(0,id.lastIndexOf(','));
	if(n<1){alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_45"));return;}

	$.get(path+"/cwc_replymanger.htm?method=getRelatedTemples&ran="+getRandomNum(),
			{tempid:id},
			function(r){
				var wms = getJsLocaleMessage("weix","weix_qywx_hfgl_text_46");
		        var juid = r.split("-");
		        var a = juid[0].replace("k",""); 
		        var b = juid[1].replace("k","");
		        if(a!=""&&a.split(",").length>0){
			        wms = getJsLocaleMessage("weix","weix_qywx_hfgl_text_46");
			        var arr = a.split(",");
					var msg = getJsLocaleMessage("weix","weix_qywx_hfgl_text_47");
					for(var i in arr){
							msg = msg +($("#"+arr[i]).val()+",");
						}
					msg = msg + getJsLocaleMessage("weix","weix_qywx_hfgl_text_48");
					alert(msg);
			    }
		        if(b!=""&&b.split(",").length>0){
		        	if(confirm(wms)){
						$.post(path+"/cwc_replymanger.htm?method=delTemple&ran="+getRandomNum(),
								{tempIds:b},
								function(r){
						            	if(r!=null&&r=="sccuess")
					                     {
					                     	alert(getJsLocaleMessage("common","common_text_4"));
					                     	document.forms['pageForm'].submit();
					                     }
					                     else if(r!=null && r=="fail")
					                     {
					                       alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_44"));
					                     }
					                     else
					                     {
					                         alert(getJsLocaleMessage("weix","weix_qywx_hfgl_text_44"))
					                     }
					               });
					}  
			    }         
               });

}
//回复预览
function preview(tempId){
	url = path+"/cwc_replymanger.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
	$("#msgPreviewFrame").attr("src",url);
	$("#divBox").dialog("open");
}	
//页面上的弹出框
function modify(t,i)
{
	$("#msgcont").empty();
	$("#msgcont").text($(t).children("label").children("xmp").text());
	if(i==1)
	{
		$('#modify').dialog('option','title',getJsLocaleMessage("weix","weix_qywx_hfgl_text_49"));
	}
	else if(i==2)
	{
		$('#modify').dialog('option','title',getJsLocaleMessage("weix","weix_qywx_mrhf_text_2"));
	}
	else if(i==3){
		$('#modify').dialog('option','title',getJsLocaleMessage("weix","weix_qywx_mrhf_text_1"));
	} else if(i==4){
		$('#modify').dialog('option','title',getJsLocaleMessage("weix","weix_qywx_hfgl_text_50"));
	}
	$('#modify').dialog('open');
}
function getRandomNum() {
    	var ran = parseInt(Math.random() * 10000);
    	return ran;
    }
function go(tid,msgtype,replname){
	replname= encodeURI(replname);
	replname= encodeURI(replname);
	var action=path+'/cwc_replymanger.htm?method=doEditemp&tid='+tid+'&msgtype='+msgtype+'&replname='+replname+"&lgcorpcode=" + $("#lgcorpcode").val();
	$("#pageForm").attr('action',action);
	$("#pageForm").submit();
	}