/*用户管理-列表页面*/

//搜索
//--关注者信息页面-start--
function showupdateUserTmp(wcid,type)
{
	//保存当前操作所在的页数
	var pagenum = $("#pageInfo span.current",window.document).text();
	var src = pathUrl+"/user_contactManager.htm?method=findFollowerById&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&wcid="+wcid + "&type="+type;
    var h = 260;
    var aboutConfig,canval;
    if(type == 2)
    {
        h = 355;

        aboutConfig={
             title: getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_10"),
             content:getIframe(src,660,355,'updateUserFrame'),
             id: 'updateUserTmpDiv',
             lock: true,
             padding:10,
             opacity: 0.3,
 	         init: function(){
				 //var iframe = this.iframe.contentWindow;
 	         	 var iframe = $("#updateUserFrame")[0].contentWindow;
				 var form = iframe.document.getElementById('userDescForm');
			 },
             ok:function(){
				//var iframe = this.iframe.contentWindow;
             	var iframe = $("#updateUserFrame")[0].contentWindow;
				if(!iframe.document.body){
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_13"))
		        	return false;
		        };
		        var form = iframe.document.getElementById('userDescForm');
				var okbutton = $("button:first",this.button());
				okbutton.attr("disabled",true);
				
				var desc = $("#userInfo",form).val();
				if(desc==null|| desc=="")
				{
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_11"));
					okbutton.attr("disabled",false);
					return false;
				}
				if(desc.length>512)
				{
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_12"));
					okbutton.attr("disabled",false);
					return false;
				}
				
				var url = pathUrl+"/user_contactManager.htm?method=find&lguserid="
				+ lguserid+"&lgcorpcode="+lgcorpcode+"&wcid="+$("#wcid").val();
				
				$.post("user_contactManager.htm",
						{
				            method:"updateFollowerById",
				            desc:desc,
				            wcid:wcid,
				            gid:$("#gid",form).val(),
				            lgcorpcode : lgcorpcode,
				            userid : lguserid,
				            isAsync:"yes"
				        },
						function(result){
						    if(result == "outOfLogin")
						    {
						        window.parent.showLogin(0);
						        return false;
						    }
					    	if(result=="success")
							{
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_47"));
					    		submitForm(pagenum);
			                    return false;
							}
							else //if(result=="fail")
							{
					    		/*art.dialog({
					    		    time: 1,
					    		    content: '保存失败！'
					    		});*/
					    		alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_10"));
			    		        return false;
							}
						}
				);
     	    },
     	    cancel: true
         };
    }
    else if (type == 1) {
		canval = getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_13");

	    aboutConfig={
	        title: getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_10"),
	        content:getIframe(src,660,355,'updateUserFrame'),
	        id: 'updateUserTmpDiv',
	        lock: true,
	        padding:10,
	        opacity: 0.3,
		    cancel: true,
		    cancelVal:canval
	    };
	}
    //var url = pathUrl+"/user_contactManager.htm?method=findFollowerById&lguserid="
    //+lguserid+"&lgcorpcode="+lgcorpcode+"&wcid="+wcid + "&type="+type;
    //dlog = art.dialog.open(url,aboutConfig);
	dlog = art.dialog(aboutConfig);
}
function getIframe(src,width,height,frameId){
	return '<iframe id="'+frameId+'" src = "'+src+'" frameborder="0" allowtransparency="true" style="width: '
		+width+'px; height: '+height+'px; border: 0px none;"></iframe>';
}
//保存表单提交数据（修改用户信息）
function save()
{
	var desc=$.trim($("#userInfo").val());
	if(desc==null|| desc=="")
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_11"));
		return;
	}
	if(desc.length>512)
	{
		alert(getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_12"));
		return;
	}

	var url = pathUrl+"/user_contactManager.htm?method=find&lguserid="
	+ lguserid+"&lgcorpcode="+lgcorpcode+"&wcid="+$("#wcid").val();
	
	$.post("user_contactManager.htm",
			{
	            method:"updateFollowerById",
	            desc:desc,
	            wcid:$("#wcid").val(),
	            gid:$("#gid").val(),
	            lgcorpcode : lgcorpcode,
	            userid : lguserid,
	            isAsync:"yes"
	        },
			function(result){
			    if(result == "outOfLogin")
			    {
			        window.parent.showLogin(0);
			        return;
			    }
		    	if(result=="success")
				{
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_47"));
		    		contReponse(url);
                    return false;
				}
				else //if(result=="fail")
				{
		    		/*art.dialog({
		    		    time: 1,
		    		    content: '保存失败！'
		    		});*/
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_10"));
    		        return false;
				}
			}
	);
}

//根据用户选择公共账号，加载公共账号对应的群组
function selGroupOfAId()
{
	$.post("user_contactManager.htm",
			{method:"findGroupByAId",a_id:$("#accountId").val(),lgcorpcode:lgcorpcode,isAsync:"yes"},
			function(result){
			    if(result == "outOfLogin")
                {
                    window.parent.showLogin(0);
                    return;
                }
				var message=result.substr(0,result.indexOf("@"));
				result = result.substr(result.indexOf("@")+1);
				
		    	if(message=="success")
				{
	    			var gid = $("#gid").val();
	    			
	    			$("#gid").empty(); 
	    			
	    			// 转换成json对象   
		            var data = JSON.parse(result);   
		               
		            var option = "<option value=''>"+getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_14")+ "</option>";   
		               
		            // 循环组装下拉框选项   
		            $.each(data, function(k, v)   
		            {   
		                option += "<option value=\"" + v['gid'] + "\">" + v['gname'] + "(" + v['gcount'] + ")" + "</option>";   
		            }); 
		            $("#gid").append(option); 
		            $("#gid").next(".c_selectBox").remove();
		            $('#gid').isSearchSelect({'width':'145','isInput':false,'zindex':0});
	    		    return false;
				}else{
					$("#gid").empty(); 
					var option = "<option value=''>"+getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_14")+ "</option>";   
					$("#gid").append(option);
					$("#gid").next(".c_selectBox").remove();
		            $('#gid').isSearchSelect({'width':'145','isInput':false,'zindex':0});
					return false;
				}
			}
	);
}

//全选中
function checkAlls(e,str)    
{  
	var a = document.getElementsByName(str);    
	var n = a.length;    
	for (var i=0; i<n; i=i+1)    
		a[i].checked =e.checked;    
}

//移动用户群组
function moveUser(userId)
{
	if(userId=="all")
	{
		if($("input[type='checkbox'][id!='checkall']:checked").size()==0)
		{
			alert(getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_15"));
			return false;
		}
	}
	
	var userIds = "";
	var gids = "";
	var ids = [];
	var ids2 = [];
	
	//移动多条
	if(userId=="all")
	{
		$("input[type='checkbox']:checked").each(function(){
			if($(this).attr("id")!="selectAll"){
				ids.push($(this).attr("value"));
				ids2.push($(this).attr("gid"));
			}
		});
		ids2.push($("#usergid").val());
		userIds = ids.join(","); 
		gids = ids2.join(","); 
	}else {
		userIds = userId;
	}
	
	$.post("user_contactManager.htm",
		{method:"moveUserGroup",usergid:$("#usergid").val(),userIds: userIds,isAsync:"yes",gids:gids},
    	function(result)
    	{
			if(result == "outOfLogin")
		    {
		        window.parent.showLogin(0);
		        return;
		    }
			if(result=="fail")
			{
	    		/*art.dialog({
	    		    time: 1,
	    		    content: '操作失败！'
	    		});*/
	    		alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_11"));
		        return false;
			}
			if(result=="success"){
				alert(getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_16"));	
			}
	    	submitForm();
	});
}
