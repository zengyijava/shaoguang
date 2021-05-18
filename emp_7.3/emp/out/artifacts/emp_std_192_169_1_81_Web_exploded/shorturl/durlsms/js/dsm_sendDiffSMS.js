		function initContents(){
		    var manualWriter = base_manualWriter;
		    var msgob = $("form[name='form2']").find("textarea[name='msg']");
			if(manualWriter == "true"){
				msgob.attr("readonly","readonly");
				msgob.css("background-color","#E8E8E8");
				msgob.val("");
			}else{
				msgob.css("background-color","");
				msgob.val("");
				msgob.attr("readonly","");
			}
		}
		var subcount = 0;
		$(document).ready(
			function(){
				getLoginInfo("#loginparam");
				initContents();
				setFormName('form2');
				var sendType=base_sendType;
			    var message=base_message;
			    var findresult=base_findresult;

			    if(findresult=="-1")
			    {
			       alert("加载页面失败,请检查网络是否正常!");	
			       return;			       
			    }

				if(message=="file"){
				     parent.$("#probar").dialog("close");
			         parent.$("body").css("background-color","white");
				     alert("文件过大！");
				     parent.$("#subSend").attr("disabled",false);
		             parent.$("#qingkong").attr("disabled",false);
				     return;
		 		 }
				$("#detail_Info").dialog({
					autoOpen: false,
					modal:true,
					title:'预览效果', 
					height:520,
					width:500,
					resizable:false,
					closeOnEscape: false,
					open:function(){
						
						$("#detail_Info").parent().css("top","20px");
						$("#detail_Info").css("height","481px");//兼容websphere
					    var  hhh=$("#effinfo").height();
						hideSelect();
						$(".ui-dialog-titlebar-close").hide();												 		 
				 		 var ct = $.trim($("#ct").text());
				 		 var yct =$.trim($("#yct").text());
				 		 var isCharg = $("#isCharg").val();
				 		 var spbalance = $.trim($("#spbalance").text());
						 var feeFlag = $("#feeFlag").val();
	 		 			var gwFee = $("#gwFee").val();
						if(gwFee.substring(0, 9) != "lessgwfee" && gwFee != "feefail" && gwFee != "nogwfee")
						{
							 //机构扣费状态，true:正常；false:费用不足
							 var depState = true;
					 		 if(isCharg == "true")
					 		 {
						 		 if(eval(ct)<eval(yct))
						 		 {
						 			 depState = false;
						 		     $("#messages1").show(); 
						 		     $("#btsend").attr("disabled","disabled");
						 		 }				 		 
						 		 else
						 		 {
						 		    $("#messages1").hide(); 
						 		 }
						 		 $("#showyct").show();
					 		 }
					 		 else{
					 		     $("#showyct").hide();
					 		     $("#messages1").hide(); 
					 		 }
					 		 //机构扣费正常且SP账号为预付费
							if(feeFlag == 1)
							{
								if(depState){
									//显示SP账号余额
									$("#showyspbalance").show();
									//余额小于发送条新
									if (eval(spbalance) < eval(yct)){
										$("#messages2 font").text("SP账号余额不足，不允许发送，请联系管理员充值。");
										$("#btsend").attr("disabled", "disabled");
									}
								}
							}
							else
							{
								//隐藏SP账号余额
								$("#showyspbalance").hide();
							}
						}
						else
						{
							$("#showyct").hide();
				 		    $("#messages1").hide(); 
				 		    //隐藏SP账号余额
							$("#showyspbalance").hide();
						}
				 		 //如果预发送条数为0，则也不允许发送，因为有可能此号码未绑定通道。				 		 
				 		 if(eval(yct) == 0)
				 		 {
				 		    $("#btsend").attr("disabled","disabled");
				 		 }

					},
					 close:function(){
						showSelect();
						$("#effinfo").hide();
						$("#subSend").attr("disabled","");
						$("#qingkong").attr("disabled","");
						$("#SendReq").val("0");
						$(".ui-dialog-titlebar-close").show();
						$("#btsend").attr("disabled","");
						//$('#contents').val(getChangeVal());
					}
				});	
				
				$("#showeffinfo").click(function() {
				    if($("#effinfo").is(":hidden"))
				    {
						$("#effinfo").show();
						$(this).find("> p ").removeClass("unfold");
						$(this).find("> p ").addClass("fold");
	                    if($("#messages1").is(":hidden")){
	                        $("#effinfotable").css("top","62px");
	                    }
	                    else
	                    {
	                       $("#effinfotable").css("top","90px");
	                    }
				    }else
				    {
						$("#effinfo").hide();
						$(this).find("> p ").removeClass("fold");
						$(this).find("> p ").addClass("unfold");
					}
				});
				
				$('#moreSelect').hide();
				$('#u_o_c_explain').toggle(function(){
						$("#foldIcon").removeClass("unfold");
						$("#foldIcon").addClass("fold");
						$('#moreSelect').show();
					},function(){
						$("#foldIcon").removeClass("fold");
						$("#foldIcon").addClass("unfold");
						$('#moreSelect').hide();
				});
				
				//加载上传附件鼠标移动的事件
                filedivHover();				
				//加载格式提示处的悬浮框
				floating("downlinks","infomodel");
				//触发账户change事件
				$("#spUser").trigger("change");
				
				$("#draftDiv").dialog({
			        autoOpen: false,
			        height:500,
			        width: 900,
			        resizable:false,
			        modal: true,
			        open:function(){
						//那个等待滚动条会把这个标题栏关掉
						$(".ui-dialog-titlebar").show();
			        },
			        close:function(){
			        }
			    });
			}
			
		);
		//发送
		function btsend()
		{
			subcount = subcount+1;
			if(subcount > 1)
			{
				alert("提交信息中，请勿重复提交！");
				$("form[name='" + formName + "']").attr("action","");
				return;
			}
			$("#btsend").attr("disabled",true);
			$("#btcancel").attr("disabled",true);
		    if($("#taskname").val()=="不作为短信内容发送")
			{
				$("#taskname").val("");
			}
			var sendTime = $('#timerTime').val();
			var serverTime ;
			$.post("common.htm?method=getServerTime", {
			
			}, function(msg) {
				serverTime = msg;
				var date1 = new Date(Date.parse(sendTime.replaceAll("-","/")));
				var date2 = new Date(Date.parse(serverTime.replaceAll("-","/")));
                date1.setMinutes(date1.getMinutes() - 1, 0, 0);
				if (date1 <= date2) {
					alert("预发送时间小于服务器当前时间！请合理预定发送时间:[EBFV009]");
					$("#timerTime").val("");
					showSelect();
					$("#subSend").attr("disabled","");
					$("#qingkong").attr("disabled","");
					$("#detail_Info").dialog("close");
					$("#btsend").attr("disabled","");
					$("#btcancel").attr("disabled","");
					return;
				}
				else
				{
					if($("#effs").text()=="0")
					{
						alert("无有效发送号码，无法提交。");
						$("#btsend").attr("disabled","");
						$("#btcancel").attr("disabled","");
					}else
					{
						$("#btsend").attr("disabled",true);
						$("#btcancel").attr("disabled",true);
						$("form[name='" + formName + "']").attr("target","_self");
						var lguserid =$("#lguserid").val();
                                 var lgcorpcode =$("#lgcorpcode").val();
                                 var lgusername =$("#lgusername").val();
						$("form[name='" + formName + "']").attr("action","url_"+base_reTitle+".htm?method=send&lguserid="+lguserid+"&lgusername="+lgusername+"&lgcorpcode="+lgcorpcode);
						if($("form[name='" + formName + "']").attr("encoding"))
						{
							$("form[name='" + formName + "']").attr("encoding","application/x-www-form-urlencoded");
						}else
						{
							$("form[name='" + formName + "']").attr("enctype","application/x-www-form-urlencoded");
						}
					$.post('common.htm?method=frontLog',{
						info:'不同内容群发，发送提交参数。userId:'+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
						+'，taskId:'+$("#taskId").val()+'，sendType:'+$("#sendType").val()+'，bmtType:'+$("#bmtType").val()
						+'，spUser:'+$("#spUser").val()
					});
						isSend = 1;
						$("form[name='" + formName + "']").submit();
					}
				}
			});
		}	
		//取消
		function btcancel1()
		{
		    showSelect();
		    $('#contents').val(getChangeVal());
			$("#subSend").attr("disabled","");
			$("#qingkong").attr("disabled","");
			$("#detail_Info").dialog("close");

		}
        //错误上传内容下载
		function uploadbadFiles()
		{
		    var badurl = $("#badurl").val();
		    badurl = badurl.replace("_view.txt","_bad.txt");
		   	$.post("common.htm?method=checkFile", {
				url : badurl,
				upload : "0"
			},
			function(result) {
			if (result == "true") {
                //来源emp节点
                download_href("doExport.hts?u="+badurl);
			} else if (result == "false")
				alert("文件不存在或该文件无访问权限");
			else
				alert("出现异常,无法跳转");
			});
		}

		
		function bycheck(type)
		{
			//清楚草稿箱信息
			$('#draftFile').val('');
            $('#draftId').val('');
            $('#draftFileTemp').val('');
	            
		   var sendTypevalue = $("#sendType").val();
		   if(type == '1')
		   {
		      $("#dtsend").removeClass("wjsendclss");
		      $("#wjsend").removeClass("dtsendclass");
		      $("#dtsend").addClass("dtsendclass");
		      $("#wjsend").addClass("wjsendclss");
		      $("#dtsend font").removeClass();
		      $("#wjsend font").removeClass();
		      $("#dtsend font").addClass("send_ac1");
		      $("#wjsend font").addClass("send_ac2");
		      $("#showmessage").show();
		     // $("#showdtsend").css("height","300px");
		      $("#sendType").attr("value","2");
		      if(sendTypevalue != "2")
		       {
			       fileCount=1;
			      // $("#filesdiv").html("<input id='numFile1' name='numFile1'  type=file onchange='ch();' class='numfileclass' value=''/><a id='afile' name='afile' class='afileclass'>上传附件</a>");
			      //$("#filesdiv").find("> input").remove();
			       $('.x-fileUpload input').remove();
			       lastname = '';
			       $(".x-uploadButton").append("<input type='file' id='numFile1' name='numFile1' onchange='ch();' class='x-numfileclass'/>");
			       $("#upfilediv").hide();
			       $("#upfilediv").html("");
			       $("#allfilename").empty();
		       }
		       $("#txtstyle").css("background",'url('+base_commonPath+'/common/img/dtsend.png) no-repeat');
		       $("#xlsstyle").css("background",'url('+base_commonPath+'/common/img/dtsend.png) no-repeat 0px -80px');
		       $("#errormessage").empty();
		       tempNo();
		       $("#wtailcontents").hide();
		       if($('#tailstate').val() == 1)
	    	   {
			       $("#dtailcontents").show();
	    	   }
		   }
		   else
		   {
		       $("#dtsend").removeClass("dtsendclass");
		       $("#wjsend").removeClass("wjsendclss");
		       $("#wjsend").addClass("dtsendclass");
		       $("#dtsend").addClass("wjsendclss");
		       $("#dtsend font").removeClass();
		       $("#wjsend font").removeClass();
		       $("#dtsend font").addClass("send_ac2");
		       $("#wjsend font").addClass("send_ac1");
		       $("#showmessage").hide();
		       //$("#showdtsend").css("height","150px");		      
		       $("#sendType").attr("value","3");
		       if(sendTypevalue != "3")
		       {
			       fileCount=1;
			      // $("#filesdiv").html("<input id='numFile1' name='numFile1'  type='file' onchange='ch();' class='numfileclass' value=''/><a id='afile' name='afile' class='afileclass'>上传附件</a>");
			        //$("#filesdiv").find("> input").remove();
			        $('.x-fileUpload input').remove();
			         lastname = '';
			        $(".x-uploadButton").append("<input type='file' id='numFile1' name='numFile1' onchange='ch();' class='x-numfileclass'/>");
			       $("#upfilediv").hide();
			       $("#upfilediv").html("");
			       $("#allfilename").empty();
		       }
		       $("#txtstyle").css("background",'url('+base_skin+'/images/wjsend.png) no-repeat');
		       $("#xlsstyle").css("background",'url('+base_skin+'/images/wjsend.png) no-repeat 0px -80px');
		       $("#errormessage").empty();
		       $("#contents").attr("value","");
		       $("#tempSelect").val("");
		       //changeEmpSelect( $("#tempSelect"),494);
		       $("#wtailcontents").show();
		       if($('#tailstate').val() == 1)
	    	   {
			       $("#tailContendDiv").show();
	    	   }
		   }		   
           filedivHover();
		}
		
		function filedivHover()
		{
		   	$("#filesdiv input").hover(function(e){
               $("#afile").css("text-decoration","underline");
			},function(e){
                  $("#afile").css("text-decoration","none");
			});
		}
		function inputTipText(){
			//所有样式名中含有graytext的input
			$("input[class*=graytext]").each(function(){
				var oldVal=$(this).val(); //默认的提示性文本
				$(this)
				.css({'color':'#ccc'}) //灰色
				.focus(function(){
				if($(this).val()!=oldVal)
					{$(this).css({'color':'#000'})}
				else
					{$(this).val('').css({'color':'#ccc'})}
				})
				.blur(function(){
				if($(this).val()=="")
					{$(this).val(oldVal).css({'color':'#ccc'})}
				})
				.keydown(function(){
					$(this).css({'color':'#000'})
				})
			})
		}
		$(function(){
			inputTipText(); //直接调用就OK了
		});
	
	//计算文件列表高度
	function setFileListHeight(){
		 var count=$('.x-fileUpload li').size(),
			 li_H=$('.x-fileUpload li').outerHeight()+5,
			 setting_H=count*li_H+10;
		if(setting_H>128){
			$('.x-fileUpload').css({'height':setting_H+'px'});
		}	
	}	
	//浏览按纽事件
	function ch(){
 		var obj=$("#numFile"+fileCount);
 		var pathValue="";
 		pathValue = $("#numFile"+fileCount).val();
		var index = pathValue.lastIndexOf("\\");
		var name = pathValue.substring(index + 1);
		
  		if(checkFile("numFile"+fileCount)){   		     
  		     if ($("#tr"+fileCount).length == 0){ 
  		     	 var fileType=getFileType(name),icon;
  		     	 if(fileType=='txt'){
  		     	 	icon='x-txt';
  		     	 }else if(fileType=='xls' || fileType=='xlsx' || fileType=='et'){
  		     	 	icon='x-xls';
  		     	 }else{
  		     	 	icon='x-fileimg';
  		     	 }
		  		if(name.length >24)
				{
				   name = name.substring(0,24)+'...'+fileType;
				}
  		        /* $("#upfilediv").append("<div id='tr"+fileCount+"' style='margin-bottom:0px;height:35px;line-height:35px;'><a style='background : url("+base_commonPath+"/common/img/"+icon+") no-repeat left center;padding: 2px 0;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;&nbsp;<label >"+name+"</label>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:delfile("+fileCount+")' style='text-decoration: none;color: #2970c0;'>删除</a></div>");  		     
  		          */      		
  		           $("#upfilediv").show();	 
	   			 $("#numFile"+fileCount).hide();
	   			 $('#upfilediv').append('<li id="tr'+fileCount+'"><i class="x-icon '+icon+'"></i>'+name+'<a href="javascript:delfile('+fileCount+')" class="x-del">删除</a></li>');
	   			 fileCount++; 
	   			 var newStr = "<input type='file' id='numFile"+fileCount+"' name='numFile"+fileCount+"' onchange='ch();' class='x-numfileclass'/>";
	   			 $('.x-uploadButton').append(newStr);
	   			 setFileListHeight();
				
	   			/* var ss = $("#filesdiv").html();      			
	   			 var ss1 = "<input type='file' id='numFile"+fileCount+"' name='numFile"+fileCount+"' value='' onchange='ch();' class='numfileclass2'/>";
	   			 $("#filesdiv").find("> a:eq(0)").after(ss1);*/
	   			 $("#allfilename").append(pathValue+";");
	   			 filedivHover();
   			  }
  		}
	}
	//发送成功跳转群发任务查看界面
	 function sendRecord(menuCode, taskid, lguserid, lgcorpcode)
	 {
		
		closemessage();
		window.parent.openNewTab(menuCode,base_path+"/surlTaskRecord.htm?method=find&taskID="+taskid+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&pageIndex="+1+"&pageSize="+15);
	 }
	 function closemessage()
	 {
		 $("#message").dialog("close");
	 }
	 
	function setDefault()
	{
		if(confirm("确认是否将当前选项设置为默认？ ")) {
			var lguserid = $('#lguserid').val();
			var lgcorpcode = $('#lgcorpcode').val();
			var busCode = $("#busCode").val();
			var priority = $("#priority").val();
			var spUser = $("#spUser").val();
			var isReply = $("input:radio[name='isReply']:checked").attr("value");
			var repeat = $("input:radio[name='checkrepeat']:checked").attr("value");
			$.post("ssm_comm.htm?method=setDefault", {
				lguserid: lguserid,
				lgcorpcode:lgcorpcode,
				busCode: busCode,
				priority: priority,
				spUser: spUser,
				isReply: isReply,
				repeat:repeat,
				flag: "2"			
				}, function(result) {
				if (result == "seccuss") {
					alert("当前选项设置为默认成功！");
					return;
				} 
				else if(result == "fail"){
					alert("当前选项设置为默认失败！");
					return;
				}
			});
		}
	}
	
	//选择草稿箱
	function showDraft(draftstype)
	{
	    var tpath = $("#cpath").val();
	    var frameSrc = tpath+"/common.htm?method=getDrafts&draftstype=" + draftstype + "&shorturl=shorturl&timee="+new Date().getTime();
	    $("#draftFrame").attr("src",frameSrc);
	    $("#draftDiv").dialog("open");
	}
	
	function draftCancel()
	{
	    $("#draftDiv").dialog("close");
	}

	function draftSure()
	{
	    var $fo = $("#draftFrame").contents();
	    var $ro = $fo.find("input[type='radio']:checked");
	    if(!$ro.val())
	    {
	        alert("未选择草稿箱！");
	        return;
	    }
	    //已存在草稿箱 提示覆盖
	    if($('#containDraft').val()){
	        if(confirm("是否覆盖已有的草稿？")){
	            $('#containDraft').parents('li').remove();
	            //删除草稿文件后 清空草稿信息
	            $('#draftFile').val('');
	            $('#draftId').val('');
	            $('#draftFileTemp').val('');
	        }else{
	            $("#draftDiv").dialog("close");
	            return false;
	        }
	    }
	    var $tr = $ro.parents('tr');
	    //草稿箱发送文件相对路径
	    var filePath = $tr.find('td:eq(0)').attr('path');
	    var draftId = $ro.val();
	    var taskname = $tr.find('td:eq(2)').find('label').text();
	    var msg = $tr.find('td:eq(3)').find('label').attr('msg');
        var domainId = $tr.find('td:eq(0)').find('input[id="domainId"]').attr('value');
        var netUrlId = $tr.find('td:eq(0)').find('input[id="netUrlId"]').attr('value');
        var validays = $tr.find('td:eq(0)').find('input[id="validays"]').attr('value');
	    
	    if(filePath != null && $.trim(filePath).length > 0)
	    {
			var index = filePath.lastIndexOf("\\");
			var name = filePath.substring(index + 1);
			var fileType=getFileType(name);
			var icon;
			if(fileType=='txt'){
				icon='x-txt';
			}else if(fileType=='xls' || fileType=='xlsx' || fileType=='et'){
				icon='x-xls';
			}else{
				icon='x-fileimg';
			}
	  		if(name.length >24)
			{
			   name = name.substring(0,24)+'...'+fileType;
			}
	  		
	  		var mobilefileName;
		  	if(taskname == null || $.trim(taskname).length == 0)
			{
			    mobilefileName = ""+draftId;
			}
		  	else
		  	{
		  		mobilefileName = draftId + "&nbsp;" + taskname;
		  	}
	  		
	  		var trhtml = [];
    		trhtml.push('<li id="tr'+fileCount+'">');
    		trhtml.push('<i class="x-icon '+icon+'"></i>');
    		trhtml.push(mobilefileName);
    		trhtml.push('<a href="javascript:delRow('+fileCount+')" class="x-del">删除</a>');
    		trhtml.push('<input type="hidden" id="containDraft" name="containDraft" value="1">');
    		trhtml.push('</li>');
    		
    		$("#upfilediv").append(trhtml.join(''));
	  		
			//$('#upfilediv').append('<li id="tr'+fileCount+'"><i class="x-icon '+icon+'"></i>'+taskname+'<a href="javascript:delRow('+fileCount+')" class="x-del">删除</a></li>');
			fileCount++;
			var newStr = "<input type='file' id='numFile"+fileCount+"' name='numFile"+fileCount+"' onchange='ch();' class='x-numfileclass'/>";
	   		$('.x-uploadButton').append(newStr);
			//var newStr = '<input type="hidden" id="containDraft" name="containDraft" value="1">';
			//$('.x-uploadButton').append(newStr);
			
	   		$("#upfilediv").show();	 
	   		//$("#numFile"+fileCount).hide();
	   		
	   		setFileListHeight();//重新计算上传文件列表高度
			
		}
	    
	    $('#draftFile').val(filePath);
	    $('#draftFileTemp').val(filePath);
	    $('#draftId').val(draftId);
        $('#netUrlId').val(netUrlId);
        $('#domainId').val(domainId);
        $('#vaildays').val(validays);

	    $('#taskname').css('color','').val(taskname);
	    if(msg != null && msg.length > 0)
	    {
	    	//回填草稿箱内容前 如若存在已选择的模板 则需取消掉模板
    		tempNo();
		    
		    $('#contents').val(msg);
	    }
	    
	    len($('#contents'));
	    $("#draftDiv").dialog("close");
	}

	/**
	 * 删除草稿箱文件行
	 * @param obj
	 */
	function delRow(filename)
	{
	    if(confirm( "确认要删除吗? "))
		{
		   $("#tr"+filename).remove();
		   //清楚文件
		   $('#draftFile').val('');
		   setFileListHeight();//重新计算上传文件列表高度
		   if($("#upfilediv").html() == "")
		   {
		      $("#upfilediv").hide();
		   }
		}
	}
	$(function() {  
	    /*  在textarea处插入文本--Start */  
	    (function($) {  
	        $.fn  
	                .extend({  
	                    insertContent : function(myValue, t) {  
	                        var $t = $(this)[0];  
	                        if (document.selection) { // ie  
	                            this.focus();  
	                            var sel = document.selection.createRange();  
	                            sel.text = myValue;  
	                            this.focus();  
	                            sel.moveStart('character', -l);  
	                            var wee = sel.text.length;  
	                            if (arguments.length == 2) {  
	                                var l = $t.value.length;  
	                                sel.moveEnd("character", wee + t);  
	                                t <= 0 ? sel.moveStart("character", wee - 2 * t  
	                                        - myValue.length) : sel.moveStart(  
	                                        "character", wee - t - myValue.length);  
	                                sel.select();  
	                            }  
	                        } else if ($t.selectionStart  
	                                || $t.selectionStart == '0') {  
	                            var startPos = $t.selectionStart;  
	                            var endPos = $t.selectionEnd;  
	                            var scrollTop = $t.scrollTop;  
	                            $t.value = $t.value.substring(0, startPos)  
	                                    + myValue  
	                                    + $t.value.substring(endPos,  
	                                            $t.value.length);  
	                            this.focus();  
	                            $t.selectionStart = startPos + myValue.length;  
	                            $t.selectionEnd = startPos + myValue.length;  
	                            $t.scrollTop = scrollTop;  
	                            if (arguments.length == 2) {  
	                                $t.setSelectionRange(startPos - t,  
	                                        $t.selectionEnd + t);  
	                                this.focus();  
	                            }  
	                        } else {  
	                            this.value += myValue;  
	                            this.focus();  
	                        }  
	                    }  
	                })  
	    })(jQuery);  
	    /* 在textarea处插入文本--Ending */  
	});  
	var cacheUrl
	$(document).ready(function() {
		$("#insUrl").click(function() {
			var sendType = $("#sendType").val();
			if(sendType == "3"){
				return;
			}
			var srcUrl = $("#srcUrl").val();
			if(srcUrl==null || srcUrl=="活动页面地址，如：http://www.wal-martchina.com/"){
				alert("没有可插入地址");
				return;
			}
			var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
			var objExp= srcUrl.match(Expression);
			var contents = $("#contents").val(); 
			var objExp2 = contents.match(Expression);
			
			if(objExp==null){
				alert("插入地址格式不对");
				return;
			}
			var text = srcUrl.replace(Expression,"");
			if(text.length>0){
				alert("插入信息包含地址以外内容");
				return;
			}
			if(objExp2 != null && objExp2.length>=4){
				alert("短信内容中包含多个地址");
				return;
			}
			$("#contents").insertContent(" "+srcUrl+" ",0);
		});
	})


	//点击插入短链接触发方法
function insertUrl(){
	var tpath = $("#cpath").val();
	var frameSrc = $("#shortUrlFrame").attr("src");
//	var lguserid = $("#lguserid").val();
//	var lgcorpcode = $("#lgcorpcode").val();
	var netUrlId = $("#netUrlId").val();
	var domainId = $("#domainId").val();
	var vaildays = $("#vaildays").val();

	frameSrc = tpath+"/urlcommonSMS.htm?method=getDomain&netUrlId="+netUrlId+"&domainId="
		+domainId+"&vaildays="+vaildays+"&timee="+new Date().getTime();
	$("#shortUrlFrame").attr("src",frameSrc);
	if(isFrame4_0()) {
        $("#shortUrlDiv").dialog({
        	autoOpen: false,
        	modal: true, 
        	width:1000,
        	height:550,
        	close:function(){
        	eblur($("#contents"));
        	$.ajax({
				  type: "post",
				  url: tpath+"/urlcommonSMS.htm",
				  data: {"method":"getLfDomain"},
				  success:function(data){
					  var domains = eval('(' + data + ')');
					  var str="";
					  for(var i in domains){
						  str+="<input type=\"hidden\" value=\""+domains[i].domain+"\" />";
					  }
					  $("#urllist").html(str);
				  }
			  });
        	}
        });
    } else {
        $("#shortUrlDiv").dialog({
        	autoOpen: false,
        	modal: true, 
        	width:827,
        	height:550,
        	close:function(){
        	eblur($("#contents"));
        	$.ajax({
				  type: "post",
				  url: tpath+"/urlcommonSMS.htm",
				  data: {"method":"getLfDomain"},
				  success:function(data){
					  var domains = eval('(' + data + ')');
					  var str="";
					  for(var i in domains){
						  str+="<input type=\"hidden\" value=\""+domains[i].domain+"\" />";
					  }
					  $("#urllist").html(str);
				  }
			  });
        	}
        });
    }

	$("#shortUrlDiv").dialog("open");
}
	function urlSure()
{
		var $fo = $("#shortUrlFrame").contents();
		var srcUrl =  $fo.find("input[type='radio']:checked").next("span").text();
		$("#contents").focus();
		$("#contents").selection('insert', {text: " "+srcUrl+" ", mode: 'before'});
		//$("#contents").insertContent(" "+srcUrl+" ",0);
		$("#shortUrlDiv").dialog("close");

}
	function showUrl()
	{
		var ul = $("#netUrl").val();
		showbox({src:ul , mode:1});

	}
function urlCancel(){
	$("#shortUrlDiv").dialog("close");
}

function isFrame4_0() {
    var skin = $("#skin").val();
    if(skin.indexOf('frame4.0') != -1) {
        return true;
    } else {
        return false;
    }
}