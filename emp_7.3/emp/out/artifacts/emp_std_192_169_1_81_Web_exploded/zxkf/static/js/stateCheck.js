define(['jquery'],function($){
	var stateCheck=function(){

	};

	/**
	* 轮询检测消息
	* @return
	*/
	stateCheck.prototype.checkStatus=function(){
		var _this=this;
		$.ajax({
			method:'POST',
			url:"customChat.htm?method=checkState&time="+new Date().getTime(),
			data:{
    		    customId : userCustomeId,
    	        aId : aId,
    	        isAsync : 'yes'
		    },
		    cache:false,
		    success:function(result){
		    	//console.log(result);
		    	if(typeof result=='string'){
		    		if(result == "outOfLogin")
		    		{
		    			doOut();
		    			return;
		    		}
		    	    if (result.indexOf("status:") == 0 && result != "status:")
		            {
		                //这里面存放了所有客服人员的状态
		                var statusjson=eval("("+result.substr(7)+")");
		                
		                for(var key in statusjson)
		                {
		                    var status = statusjson[key].status;
		                    var labeltext = "";
		                    if (status=="1") {
		                        labeltext = "[在线]";
		                    }else if (status=="2") {
		                        labeltext = "[忙碌]";
		                    }else if (status=="3") {
		                        labeltext = "[离开]";
		                    }else if (status=="4") {
		                        labeltext = "[离线]";
		                    }else {
		                        labeltext = "[未知]";
		                    }
		                    // 执行状态更改
		                }
		                setTimeout(function(){
	                        _this.checkStatus();
	                    },10000);
		            }
		    	}
		        
		    }
			
		})
	}


	return {
	    stateCheck:stateCheck
	}
});

