	$(document).ready(function(){
		$("#addNotice").dialog({
	    	autoOpen: false,
	    	height:465,
	    	width: 400,
	    	modal: true,
	    	close:function(){
				//$("#title").val("");
				//$("#cont").val("");
				//$("#nid").val("");
	 		}
	    });
	    $("#Notices").dialog({
	    	autoOpen: false,
	    	height:350,
	    	width: 400,
	    	modal: true
	    });
	});
     function showDiv()
     {
           $("#tableDiv").toggle();
     }
     function doAdd()
     {
           $("#addNotice").dialog("open");
     }
     function doEdit(id)
     {
     	$.post("not_sysNotice.htm?method=showDetail",{id:id,isAsync:"yes"},function(data){
     		var path=$("#contextPath").val();
     		if(result == "outOfLogin")
			{
				$("#logoutalert").val(1);
				location.href=contextPath+"/common/logoutEmp.html";
				return;
			}
    		if(data == "false"){
 		     	alert("加载失败，请稍后重试！");
 		     	return ;
 		     }
			var result=eval("("+data+")");//转换为json对象   
  			$("#nid").attr("value",id);
	    	$("#title").attr("value",result.title);
	        $("#cont").val(result.content);
	        $("#addNotice").dialog("open");
    	});
     }
     function doCancel()
     {
    	 $("#addNotice").dialog("close");
	     submitForm();
     }
     //打开公告提示
	function openNoticeTips(txt){
		window.parent.parent.notice_tips_dialog(txt);
	}
	function previewNotice(){
		
		if(!checkInput()){
    		 return;
		}
		var title = $("#title").attr("value");
		var cont = $("#cont").val();
		var noteTail = $("#noteTail").val();
		var d = new Date();
		var vYear = d.getFullYear();
		var vMon = d.getMonth() + 1
		var vDay = d.getDate();
		
		var txt='<h2 style="font-weight:bold;font-size:22px;font-family:微软雅黑;text-align:center;">'+title+'</h2>'+
			'<p style="text-indent:2em;margin-top:40px;">'+cont+'</p>'+
			'<p class="p3"  style="margin-top:60px;text-align:right;font-family:微软雅黑;font-size:14px;">'+noteTail+'</p>'+
			'<p class="p3" style="text-align:right;font-family:微软雅黑;font-size:14px;">'+vYear+'年'+vMon+'月'+vDay+'日</p>';
		
		window.parent.parent.notice_tips_dialog(txt);
	}
	
	function checkInput(){
		var tt = $("#title").attr("value");
         var cont = $("#cont").val();
         var noteTail = $("#noteTail").val();
         var noteState = $('input[name="noteState"]:checked').val();
         var noteValid = $("#noteValid").val();
        
         if (tt==null || $.trim(tt) == "")
         {
             alert("请输入公告标题！");
             return false;
         }
         else if (cont==null || $.trim(cont) == "")
         {
             alert("请输入公告内容！");
             return false;
         }
         else if(cont.length>650){
             alert("公告内容长度限制最大字数为650字,您输入的内容长度超出此限制！");
             return false; 
          }
         else if (noteTail==null || $.trim(noteTail) == "")
         {
             alert("请输入注尾！");
             return false;
         }
         else if (noteState==null || $.trim(noteState) == "")
         {
             alert("请选择公告状态！");
             return false;
         }
         return true;
	}
	
     function doOk()
     {
    	 if(!checkInput()){
    		 return;
    	 }
         var id = $("#nid").attr("value");
         var tt = $("#title").attr("value");
         var cont = $("#cont").val();
         var noteTail = $("#noteTail").val();
         var noteState = $('input[name="noteState"]:checked').val();
         var noteValid = $("#noteValid").val();
         //cont = $.trim(cont);
         var lguserid = $("#lguserid").val();
         var lgcorpcode =$("#lgcorpcode").val();
         
         $.post("not_sysNotice.htm?method=update",{id:id,tt:tt,cont:cont,lguserid:lguserid,lgcorpcode:lgcorpcode,isAsync:"yes",noteTail:noteTail,noteState:noteState,noteValid:noteValid},function(result){
        	var path=$("#contextPath").val();
      		if(result == "outOfLogin")
 			{
      			alert("已退出系统，请记录好当前操作数据，并重新登录。");
 				$("#logoutalert").val(1);
 				//location.href=contextPath+"/common/logoutEmp.html";
 				return;
 			}
        	 if (result != null && result == "true")
             {
                  alert("发布成功！");
                  $("#addNotice").dialog("close");
                  //location.href = location;
                  submitForm();
             }
             else
             {
            	 alert("发布失败！");
             }
         });
     }
     function doDel(id){
    		if(confirm("你确定要删除此条记录吗？")==true)
    		{
    			$.post("not_sysNotice.htm?method=delete",{ids:id,isAsync:"yes"},function(result){
    				var path=$("#contextPath").val();
    	     		if(result == "outOfLogin")
    				{
    					$("#logoutalert").val(1);
    					location.href=contextPath+"/common/logoutEmp.html";
    					return;
    				}
    				if(result>0){
    					alert("删除成功！");
    					//window.location = location;
    					back();

    				}else alert("删除失败！");
    			});
    		}
    	}
 	function showDetail(id)
 	{
 		$.post("not_sysNotice.htm?method=showDetail",{id:id,isAsync:"yes"},function(data){
 			var path=$("#contextPath").val();
     		if(result == "outOfLogin")
			{
				$("#logoutalert").val(1);
				location.href=contextPath+"/common/logoutEmp.html";
				return;
			}
			if(data == "false"){
 		     	alert("加载失败，请稍后重试！");
 		     	return ;
 		     }
				var result=eval("("+data+")");//转换为json对象   
  				$("#user").attr("value",result.name);
	 		$("#ttime").attr("value",result.publishTime);
	   	    $("#ttitle").attr("value",result.title);
	        $("#tcont").val(result.content);
	        $("#Notices").dialog("open");
    	});
 	}
 	
	function back()
	{
			var type=$("#type").val();
	        var url = 'not_sysNotice.htm?method=find&type='+type;
			var conditionUrl = "";
			if(url.indexOf("?")>-1)
			{
				conditionUrl="&";
			}else
			{
				conditionUrl="?";
			}
			$("#loginparams").find(" input").each(function(){
				conditionUrl = conditionUrl + $(this).attr("id")+"="+$(this).val()+"&";
			});
			location.href=url+conditionUrl;
	}