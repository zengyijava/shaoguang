document.onkeydown=keydown;
var zTree1;
var setting;
setting = {
		async : true,
		asyncUrl :"cha_balanceMgr.htm?method=createTree", //获取节点数据的URL地址
		isSimpleData: true,
		rootPID : -1,
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		callback: {
			beforeExpand: function(){return false;},
			beforeCollapse: function(){return false;},
			click: zTreeOnClick,
			asyncSuccess:function(event, treeId, treeNode, msg){
				zTree1.expandAll(true);
			}
		}
};
var zNodes =[];

function showMenu() {
	var sortSel = $("#sortSel");
	var sortOffset = $("#sortSel").offset();
	$("#dropMenu").toggle();
}
function hideMenu() {
	$("#dropMenu").hide();
}
function zTreeOnClick(event, treeId, treeNode) {
	if (treeNode) {
		var sortObj = $("#sortSel");
		sortObj.attr("value", treeNode.name);
		$("#depId").attr("value",treeNode.id);
		hideMenu();
	}
}

function reloadTree() {
	hideMenu();
	//zTree1 = $("#dropdownMenu").zTree(setting, zNodes);
}	

function chongZhi(){
	var addCount = $('#addCount').val();
			   if(addCount == '' ){
			      alert("充值条数不能为空！");
			       $('#addCount').focus();
			      return;
			   }
			   if(addCount == 0 ){
			      alert("充值条数不能为0！");
			       $('#addCount').select();
			      return;
			   }
			    var re = /^[0-9]+.?[0-9]*$/;    
			     if (!re.test(addCount))
			    {
			        alert("充值条数必须为数值!");
			        $('#addCount').select();
			        return ;
			     }
			   	var addMark = $('#addMark').val();
			   	if(addMark.length > 256 ){
			      alert("备注长度不能超过256个字");
			      return;
			   }
			   
			   var depId = $('#depId').val();
			   var msgType = $("#addSms").attr("checked");
			   var infomation = "";
			   if(msgType){
			      msgType = 1;
			      infomation = "短信";
			   }else{
			      msgType = 2;
			      infomation = "彩信";
			   }
			   var addMark = $('#addMark').val();
			   var path = 'cha_balanceMgr.htm';
			   var $buttons = $("#addBalance input:button");
			   $buttons.attr("disabled",true);
			   $.post(path,{method:"addBalance",count:addCount,depId:depId,msgType:msgType,addMark:addMark,lgguid:$("#lgguid").val()},function(result){
				   if(result.indexOf("html") > 0){
		    			window.location.href=location;
		    		    return;
		    	   }else{
				       if(result == "0"){
				           getCt();
				           alert(infomation+"充值成功！ ");
				           $('#addBalance').dialog('close');  
				           //原有的备份
				           //location.href = "cha_balanceMgr.htm?method=find&lgguid="+$("#lgguid").val()+"&operatePageReturn=true";
				           // EMP5.7新需求：增加对操作员充值和回收权限   by pengj
				           //这里加个判断，主要是判断是否是默认的充值权限
				           if(GlobalVars.lgusername == 'admin'){
				        	   //如果是admin用户，还是使用原来的方法
				        	   location.href = "cha_balanceMgr.htm?method=find&lgguid="+$("#lgguid").val()+"&operatePageReturn=true";
				           }else{
				        	   if($("#operatorBalancePri").val() == 'notDefaultBalancePri'){
					        	   if($('#depName').val() == "输入名称"){
					        			document.getElementById('depName').value="";
					        		}
					        	    var time = new Date();
					        		$('#tableInfo').load($('#servletUrl').val(),
					        			{
					        				method:'getTable',
					        				time:time,
					        				depId:$('#depId').val(),
					        				depTreeId:$('#depTreeId').val(),
					        				pageIndex:$('#txtPage').val(),
					        				pageSize:$('#pageSize').val(),
					        				depName:$('#depName').val(),
					        				lgguid:$("#lgguid").val(),
					        				//判断是否是默认充值回收权限
					        				operatorBalancePri:$("#operatorBalancePri").val()
					        			}
					        		);
					           }else{
					        	   showDepTable();
					           } 
				           }
				           // end
				       }else{
				    	   var str = getBalanceMsg(result,msgType,1);
				           if(str == "-1"){
				        	   alert(str);
					       }else{
					    	   alert("充值失败 ："+str);
						   }
				           
				       }
				       $buttons.attr("disabled",false);
				    }
			   });
}

function quXiao(flag){
	if(flag==1){
		$('#addCount').val("");
	   $('#addMark').val("");
	   $('#addBalance').dialog('close'); 
	 }else{
	      $('#delCount').val("");
			   $('#recMark').val("");
			   $('#delBalance').dialog('close');
	 }
}

function huiShou(){
	var recCount = $('#recCount').val();
			   if(recCount == '' ){
			      alert("回收条数不能为空！");
			      $('#recCount').focus();
			      return;
			   }
			   if(recCount == 0 ){
			      alert("回收条数不能为0！");
			      $('#recCount').select();
			      return;
			   }
			   	var re = /^[0-9]+.?[0-9]*$/;    
			     if (!re.test(recCount))
			    {
			        alert("回收条数必须为数值!");
			        $('#recCount').select();
			        return ;
			     }
			    var recMark = $('#recMark').val();
			   	if(recMark.length > 256 ){
			      alert("备注长度不能超过256个字");
			      return;
			   }
			   var depId = $('#depId').val();
			   var msgType = $("#addMms").attr("checked");
			   var infomation = "";
			   if(msgType){
			      msgType = 1;
			      infomation = "短信";
			   }else{
			      msgType = 2;
			      infomation = "彩信";
			   }
			   var recMark = $('#recMark').val();
			   var path = 'cha_balanceMgr.htm';
			   var $buttons = $("#delBalance input:button");
			   $buttons.attr("disabled",true);
			   $.post(path,{method:"recBalance",count:recCount,depId:depId,msgType:msgType,recMark:recMark,lgguid:$("#lgguid").val()},function(result){
			       if(result.indexOf("html") > 0){
		    			window.location.href=location;
		    		    return;
		    	   }else{
				       if(result == "0"){
				           getCt();
				           alert(infomation+"回收成功！ ");
				           $('#delBalance').dialog('close');  
				           //原有的备份
				     	   //window.location.href = "cha_balanceMgr.htm?method=find&lgguid="+$("#lgguid").val()+"&operatePageReturn=true";
				           // EMP5.7新需求：增加对操作员充值和回收权限   by pengj
				           //这里加个判断，主要是判断是否是默认的充值权限
				           if(GlobalVars.lgusername == 'admin'){
				        	   //如果是admin用户，还是使用原来的方法
				        	   window.location.href = "cha_balanceMgr.htm?method=find&lgguid="+$("#lgguid").val()+"&operatePageReturn=true";
				           }else{
				        	   if($("#operatorBalancePri").val() == 'notDefaultBalancePri'){
					        	   if($('#depName').val() == "输入名称"){
					        			document.getElementById('depName').value="";
					        		}
					        	    var time = new Date();
					        		$('#tableInfo').load($('#servletUrl').val(),
					        			{
					        				method:'getTable',
					        				time:time,
					        				depId:$('#depId').val(),
					        				depTreeId:$('#depTreeId').val(),
					        				pageIndex:$('#txtPage').val(),
					        				pageSize:$('#pageSize').val(),
					        				depName:$('#depName').val(),
					        				lgguid:$("#lgguid").val(),
					        				//判断是否是默认充值回收权限
					        				operatorBalancePri:$("#operatorBalancePri").val()
					        			}
					        		);
					           }else{
					        	   showDepTable();
					           }
				           }
				        // end
				       }else{
					       var str = getBalanceMsg(result,msgType,2);
				           if(str == "-1"){
				        	   alert(str);
					       }else{
					    	   alert("回收失败 ："+str);
						   }
				           $('#delBalance').dialog('close');  
				       }
				       $buttons.attr("disabled",false);
				    }
			   });
}

//num返回值 ,type 短1彩2,baltype充值1 回首2
function getBalanceMsg(num,type,baltype){
	  // -1:短信充值失败
	  // -2:短信充值数目不能为空
	//   -4:机构下没有可用短信余额
	 //   -5:充值数目大于短信可分配余额
	//	-9999:短信充值接口调用异常0:短信充值成功
	// -7:机构还没有进行充值
	// -3:获取操作员上级机构失败
	//-6:用户短信余额记录失败
	var msg = "";
	if(type == 1){
		msg = "短信";
	}else{
		msg = "彩信";
	}
	if(baltype == 1){
		if(num == "-1"){
			msg = msg + "充值失败！";
		}else if(num == "-2"){
			msg = msg + "充值数目不能为空！";
		}else if(num == "-3"){
			msg = "获取操作员上级机构失败！";
		}else if(num == "-4"){
			msg =  "机构下没有可用" + msg + "余额！";
		}else if(num == "-5"){
			msg = "充值数目大于" + msg +"可分配余额！";
		}else if(num == "-6"){
			msg = "用户短信余额记录失败！";
		}else if(num == "-9999"){
			msg = msg + "充值接口调用异常！";
		}else{
			msg = msg + "充值失败！";
		}
	}else if(baltype == 2){
		if(num == "-1"){
			msg = msg + "回收失败！";
		}else if(num == "-2"){
			msg = msg + "回收数目不能为空！";
		}else if(num == "-3"){
			msg = "获取操作员上级机构失败！";
		}else if(num == "-4"){
			msg =  "机构下没有可用" + msg + "余额！";
		}else if(num == "-5"){
			msg = "回收" + msg +"数大于机构可分配数目！";
		}else if(num == "-9999"){
			msg = msg + "回收接口调用异常！";
		}else if(num == "-6"){
			msg = "用户短信余额记录失败！";
		}else if(num == "-7"){
			msg = "机构还没有进行充值！";
		}else{
			msg = msg + "充值失败！";
		}
	}
	return msg;
}


function addBalance(depId,depName){
   $('#depId').val(depId);
   $('#smsType').attr("selected",true);
   $('#addBalance').dialog("option","title","机构余额充值-"+depName);
   $('#addBalance').dialog('option', 'height', '240');
   $('#addBalance').dialog('option', 'width', '350');
   $('#addBalance').dialog('open');
}
function delBalance(depId,depName){
   $('#depId').val(depId);
   $('#smsType').attr("selected",true);
   $('#delBalance').dialog("option","title","机构余额回收-"+depName);
    $('#delBalance').dialog('option', 'height', '240');
   $('#delBalance').dialog('option', 'width', '350');
   $('#delBalance').dialog('open');
}

function addBalanceAll(){
	var depId = $("#balanceAllDepId").val();
	var lguserid = GlobalVars.lguserid;
	var lgguid =  GlobalVars.lgguid;
	if(depId == null || depId == "")
	{
		alert("机构信息不能为空，请选择批量充值的机构!");
		return false;
	}
	var path = 'cha_balanceMgr.htm';
	$.get(path,
		{method:"checkLguser",
		depId:depId,
		lgguid:lgguid
		},function(result){
			if(result == "-1")
			{
				alert("你对此机构无批量充值权限，请联系管理员!");
				return false;
			}
			if(result == "0")
			{
				$("#addBalanceAllDiv").css("display","block");
				$("#addBalanceAllFrame").css("display","block");
				$("#addBalanceAllDiv").dialog({
					autoOpen: false,
					height:500,
					width: 480,
					resizable:false,
					modal: true,
					success:function(){
						$("#addBalanceAllDiv").css("display","none");
						$("#addBalanceAllFrame").css("display","none");
					}
				});
			    $("#addBalanceAllFrame").attr("src","cha_balanceMgr.htm?method=toAddBalanceAll&lgguid="+lgguid+"&depId="+depId+"");
			          
				$("#addBalanceAllDiv").dialog("open");
			}
	   });
}

function noyinhao(obj)
{  
	var isIE = false;
	var isFF = false;
	var isSa = false;
	if ((navigator.userAgent.indexOf("MSIE") > 0)
			&& (parseInt(navigator.appVersion) >= 4))
		isIE = true;
	if (navigator.userAgent.indexOf("Firefox") > 0)
		isFF = true;
	if (navigator.userAgent.indexOf("Safari") > 0)
		isSa = true;
	$(obj).keypress(function(e) {
		var iKeyCode = window.event ? e.keyCode
				: e.which;
		var vv = (!(iKeyCode >=48 && iKeyCode<=57));
		if (vv) {
			if (isIE) {
				event.returnValue = false;
			} else {
				e.preventDefault();
			}
		}
	});
}

function setAlarm(depId,depName,smsCount,mmsCount,name,phone){
	   $('#depId').val(depId);
	   $('#smsAlarm').val(smsCount);
       $('#smsAlarm').attr('lastVal',smsCount);
	   $('#mmsAlarm').val(mmsCount);
       $('#mmsAlarm').attr('lastVal',mmsCount);
	   $('#setAlarm').dialog("option","title","告警阀值设置-"+depName);
	   $('#alarmName').val(name);
	   $('#alarmPhone').val(phone);
	   $('#setAlarm').dialog('open');
	}
function closeAlarm(){
	   $('#depId').val('');
	   $('#smsAlarm').val('');
       $('#smsAlarm').attr('lastVal','');
	   $('#mmsAlarm').val('');
       $('#mmsAlarm').attr('lastVal','');
	   $('#alarmName').val('');
	   $('#alarmPhone').val('');
	   $('#setAlarm').dialog('close');
	}



function alarm(){
    var depId = $.trim($('#depId').val());
    if(depId==''){
    	return;
    }
	var smsAlarm = $.trim($('#smsAlarm').val());
	var mmsAlarm = $.trim($('#mmsAlarm').val());
	var name = $.trim($('#alarmName').val());
	var phone = $.trim($('#alarmPhone').val());
	var re = /^[0-9]+.?[0-9]*$/; 
	var reg=/^1[3458][0-9]{9}$/;
	   if(smsAlarm==''){
		   smsAlarm=0;
	   }
	   if(mmsAlarm==''){
		   mmsAlarm=0;
	   }
	   if(smsAlarm+mmsAlarm==0 ){
	      alert("短彩信阀值至少设置一项！");
	       $('#smsAlarm').focus();
	      return;
	   }
	   if(phone.length<1){
		   alert("手机号码不能为空！");
		   $("#alarmPhone").focus();
		   return;
	   }
	   if(!asyncCheckPhone(phone)){
		   alert("手机号码格式不正确！");
		   $("#alarmPhone").focus();
		   return;   
	   }
	   var depId = $('#depId').val();
        //若阀值未修改 则传递空值
        if(smsAlarm == $('#smsAlarm').attr('lastVal')){
            smsAlarm = '';
        }
        if(mmsAlarm == $('#mmsAlarm').attr('lastVal')){
            mmsAlarm = '';
        }
	   var path = 'cha_balanceMgr.htm';
	   $("#alarmButton").attr("disabled",true);
	   $.post(path,{method:"setAlarm",depId:depId,
		   smsAlarm:smsAlarm,mmsAlarm:mmsAlarm,name:name,phone:phone,
		   lgcorpcode:$("#lgcorpcode").val()},function(result){
		       if(result.indexOf("html") > 0){
	    			window.location.href=location;
	    		    return;
	    	   }else{
	    		   if(result>0){
	    			   alert("设置阀值成功！");
	    		   }else{
	    			   alert("设置阀值失败！");
	    		   }
			       $('#delBalance').dialog('close'); 
			       $('#setAlarm').dialog('close'); 
			       //这个是原有的方法，备份
			       //window.location.href = "cha_balanceMgr.htm?method=find&lgguid="+$("#lgguid").val()+"&operatePageReturn=true";
			       // EMP5.7新需求：增加对操作员充值和回收权限   by pengj
		           //这里加个判断，主要是判断是否是默认的充值权限
		           if(GlobalVars.lgusername == 'admin'){
		        	   //如果是admin用户，还是使用原来的方法
		        	   window.location.href = "cha_balanceMgr.htm?method=find&lgguid="+$("#lgguid").val()+"&operatePageReturn=true";
		           }else{
		        	   if($("#operatorBalancePri").val() == 'notDefaultBalancePri'){
			        	   if($('#depName').val() == "输入名称"){
			        			document.getElementById('depName').value="";
			        		}
			        	    var time = new Date();
			        		$('#tableInfo').load($('#servletUrl').val(),
			        			{
			        				method:'getTable',
			        				time:time,
			        				depId:$('#depId').val(),
			        				depTreeId:$('#depTreeId').val(),
			        				pageIndex:$('#txtPage').val(),
			        				pageSize:$('#pageSize').val(),
			        				depName:$('#depName').val(),
			        				lgguid:$("#lgguid").val(),
			        				//判断是否是默认充值回收权限
			        				operatorBalancePri:$("#operatorBalancePri").val()
			        			}
			        		);
			           }else{
			        	   showDepTable();
			           }
		           }
		           
		           // end
	    	   }
	   });
}
//撤销短彩告警阀值
function deleteAlarm(){
	var depId = $.trim($('#depId').val());
    if(depId==''){
    	return;
    }
    var path = 'cha_balanceMgr.htm';
    $("#deleteAlarmButton").attr("disabled",true);
	 $.post(path,{
		 method:"deleteAlarm",
		 depId:depId
		 },function(result){
       if(result.indexOf("html") > 0){
			window.location.href=location;
		    return;
	   }else{
		   if(result=="0"){
			   alert("撤销阀值成功！");
		   }else{
			   alert("撤销阀值失败！");
		   }
	       $('#delBalance').dialog('close'); 
	       $('#setAlarm').dialog('close'); 
	       //window.location.href = "cha_balanceMgr.htm?method=find&lgguid="+$("#lgguid").val()+"&operatePageReturn=true&operatorBalancePri=notDefaultBalancePri";
   		   if(GlobalVars.lgusername == 'admin'){
	    	   window.location.href = "cha_balanceMgr.htm?method=find&lgguid="+$("#lgguid").val()+"&operatePageReturn=true";
	       }else{
	    	   if($("#operatorBalancePri").val() == 'notDefaultBalancePri'){
	        	   if($('#depName').val() == "输入名称"){
	        			document.getElementById('depName').value="";
	        		}
	        	    var time = new Date();
	        		$('#tableInfo').load($('#servletUrl').val(),
	        			{
	        				method:'getTable',
	        				time:time,
	        				depId:$('#depId').val(),
	        				depTreeId:$('#depTreeId').val(),
	        				pageIndex:$('#txtPage').val(),
	        				pageSize:$('#pageSize').val(),
	        				depName:$('#depName').val(),
	        				lgguid:$("#lgguid").val(),
	        				operatorBalancePri:$("#operatorBalancePri").val()
	        			}
	        		);
	           }else{
	        	   showDepTable();
	           }
	       }
	   }
	 });
	 $("#deleteAlarmButton").attr("disabled",false);
}

$(function(){
	$("#smsAlarm,#mmsAlarm").bind('keyup blur',function(){
		var reg=/[^0-9]/g;
		var val=$(this).val();
		if(reg.test(val)){
			$(this).val($(this).val().replace(reg,''));
					}
  });
	$("#alarmPhone").bind('keyup blur',function(){
		phoneInputCtrl($(this));
  });
})


