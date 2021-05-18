/*用户管理-群组列表页面*/
//--修改群组信息弹出框-start--
function showupdateGroupTmp(gid,type)
{
	var pagenum = $("#pageInfo span.current",window.document).text();
	
	var src = pathUrl+"/user_groupManager.htm?method=findGroupById&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&gid="+gid + "&type="+type;
	var aboutConfig={
            title: getJsLocaleMessage("wxgl","wxgl_qywx_qfgl_text_1"),
            content:getIframe(src,380,200,'updateGroupFrame'),
            id: 'updateGroupTmpDiv',
            lock: true,
            opacity: 0.3,
	         init: function(){
				 var iframe =$("#updateGroupFrame")[0].contentWindow;
				 var form = iframe.document.getElementById('groupForm');
			 },
            ok:function(){
				var iframe = $("#updateGroupFrame")[0].contentWindow;
				if(!iframe.document.body){
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_2"));
		        	return false;
		        };
		        var form = iframe.document.getElementById('groupForm');
				var okbutton = $("button:first",this.button());
				okbutton.attr("disabled",true);
				
				var aid=$.trim($("#aid",iframe.document).val());
				var gid=$.trim($("#gid",iframe.document).val());
				var gname=$.trim($("#gname",form).val());
				var lgcorpcode=$.trim($("#lgcorpcode",iframe.document).val());
				if(gname==null|| gname=="")
				{
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_3"));
					okbutton.attr("disabled",false);
					return false;
				}
				
				var url = $("#pathUrl").val()+"/user_groupManager.htm?&lgcorpcode="+lgcorpcode;
				$.post("user_groupManager.htm",
						{method:"updateGroupById",aid:aid,gid:gid,gname:gname,lgcorpcode:lgcorpcode,userid:$("#lguserid",iframe.document).val(),isAsync:"yes"},
						function(result){
							if(result=="success")
							{		
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_zdycd_text_47"));
					    		submitForm(pagenum);
			    		        return false;
							}
							else
							{
					    	/*	art.dialog({
					    		    time: 1,
					    		    content: '更新失败！'
					    		});*/
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_qfgl_text_3"));
			    		        return false;
							}
						}
				);
    	    },
    	    cancel: true
        };
	
	//var url = pathUrl+"/user_groupManager.htm?method=findGroupById&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&gid="+gid + "&type="+type;
     dlog = art.dialog(aboutConfig);
}

//新增群组页面
function showAddGroupTmp(type)
{	
	var src=pathUrl+"/user_groupManager.htm?method=addGroup&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&aid="+$('#aid').val() + "&type="+type;
	var aboutConfig={
            title: getJsLocaleMessage("wxgl","wxgl_qywx_qfgl_text_4"),
            content:getIframe(src,310,150,'addGroupFrame'),
            id: 'addGroupTmpDiv',
            lock: true,
            opacity: 0.3,
	         init: function(){
				 var iframe = $("#addGroupFrame")[0].contentWindow;
				 var form = iframe.document.getElementById('groupDescForm');
			 },
            ok:function(){
				var iframe = $("#addGroupFrame")[0].contentWindow;
				if(!iframe.document.body){
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_gzhf_text_13"))
		        	return false;
		        };
		        var form = iframe.document.getElementById('groupDescForm');
				var okbutton = $("button:first",this.button());
				okbutton.attr("disabled",true);
				
				var aid=$.trim($("#aid",iframe.document).val());
				var gname=$.trim($("#gname",form).val());
				var lgcorpcode=$.trim($("#lgcorpcode",iframe.document).val());
				if(aid==null|| aid=="")
				{
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_qfgl_text_5"));
					okbutton.attr("disabled",false);
					return false;
				}
				if(gname==null|| gname=="")
				{
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_qfgl_text_2"),1.0);
					okbutton.attr("disabled",false);
					return false;
				}
				
				$.post("user_groupManager.htm",
						{method:"addGroup",aid:aid,gname:gname,lgcorpcode:lgcorpcode,userid:$("#lguserid",iframe.document).val(),isAsync:"yes"},
						function(result){
						    if(result == "outOfLogin")
						    {
						        window.parent.showLogin(0);
						        return false;
						    }
					    	if(result=="success")
							{
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_qfgl_text_10"));
					    		submitForm();
			    		        return false;
							}
							else
							{
					    	/*	art.dialog({
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
	
	//var url = pathUrl+"/user_groupManager.htm?method=addGroup&lguserid="+$('#lguserid').val()+"&lgcorpcode="+$('#lgcorpcode').val()+"&aid="+$('#aid').val() + "&type="+type;
    dlog = art.dialog(aboutConfig);
}
function getIframe(src,width,height,frameId){
	return '<iframe id="'+frameId+'" src = "'+src+'" frameborder="0" allowtransparency="true" style="width: '
		+width+'px; height: '+height+'px; border: 0px none;"></iframe>';
}
//判断公众账号是否已经进行过信息同步操作
function judgeAccIsSynched() 
{
	$.post("user_groupManager.htm",
			{method:"judgeAccIsSynched",aid:$("#aid").val(),lgcorpcode:$("#lgcorpcode").val(),isAsync:"yes"},
			function(result){
				var message=result.substr(0,result.indexOf("@"));
				result = result.substr(result.indexOf("@")+1);
		    	if(message=="success")
				{
		    		//同步(默认值为0，为空或者0表示未同步，1表示已同步)
	    			if (null == result || result == "" ||result == "0")
	    			{
	    				alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_7"));
	    				$('#subBut').attr("class","btnClass6");
	    				$('#subBut').attr("disabled",true);
	    		        return false;
					}
	    			else if (result == "1")
	    			{
	    				$('#subBut').attr("class","btnClass5 mr23");
	    				$('#subBut').attr("disabled",false);
					}
				}
				else if(message=="fail")
				{
					return false;
				}
				else
				{
					return false;
				}
			}
	);
}
function showMember(gid,aid)
{
	 var src = pathUrl + '/user_groupManager.htm?method=getMembers&gpId='+gid+'&aId='+aid+'&'+new Date().getTime();
    var aboutConfig={
            title: getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_8"),
            content:getIframe(src,550,400),
            lock: true,
            background: "#000",
            opacity: 0.3,
            ok:function(){
                var addUids=art.dialog.data('addUids');//增加的成员id
                var delUids = art.dialog.data('delUids');//删除的成员id
                var gpids = art.dialog.data('gpids');//需要重新统计人数的群组id
                updateGroupMember(addUids,delUids,gid,aid,gpids);
            },
            cancel: true
        };
    var url = pathUrl + '/user_groupManager.htm?method=getMembers&gpId='+gid+'&aId='+aid+'&'+new Date().getTime();
     dlog = art.dialog(aboutConfig);
}
//保存表单提交数据（修改群组）
function updateGroupMember(addUids,delUids,gid,aid,gpids)
{
	$.post("user_groupManager.htm",
			{method:"managerGroupMember",aid:aid,addToGid:gid,addUserIds:addUids,delUserIds:delUids,gpids:gpids,isAsync:"yes"},
			function(result){
				if(result=="success")
				{
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_mrhf_text_9"));
					submitForm();
    		        return false;
				}
				else
				{
		    /*		art.dialog({
		    		    time: 1,
		    		    content: '更新失败！'
		    		});*/
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_qfgl_text_3"));
    		        return false;
				}
			}
	);
}