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
			       alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_77'));	
			       return;			       
			    }

				if(message=="file"){
				     parent.$("#probar").dialog("close");
			         parent.$("body").css("background-color","white");
				     alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_308'));
				     parent.$("#subSend").attr("disabled",false);
		             parent.$("#qingkong").attr("disabled",false);
				     return;
		 		 }
				$("#detail_Info").dialog({
					autoOpen: false,
					modal:true,
					title:getJsLocaleMessage('dxzs','dxzs_ssend_alert_78'), 
					height:520,
					width:680,
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
										$("#messages2 font").text(getJsLocaleMessage('dxzs','dxzs_ssend_alert_79'));
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
                var skin = $("#skin").val();
            	if(skin.indexOf("frame4.0") != -1) {
            		$("#infomodel").hide();
            		floatings("downlinks", "dsm_descriptInfo");
            	}else{
            		floating("downlinks", "infomodel");
            	}
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
			if($("#splitFlag")&&$("#splitFlag").val()){
				
				subcount = subcount+1;
				if(subcount > 1)
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_80'));
					$("form[name='" + formName + "']").attr("action","");
					return;
				}
				$("#btsend").attr("disabled",true);
				$("#btcancel").attr("disabled",true);
				var nomsg = getJsLocaleMessage('dxzs','dxzs_ssend_alert_36');
			    if($("#taskname").val()==nomsg)
				{
					$("#taskname").val("");
				}
			    
				var sumLimit = $("#effs").text();
				var sum = 0;
				// 遍历分批设置，验证分批数量是否合理
				var bn = $(".custBatchNode");
				var len = bn.length;
				// 发送短信分批验证失败，清空之前添加到的from表单的分批设置
				$(".hiddenBatchNode").remove();
				for (var i = 0; i < len; i++) {
					var batchNodeValue = $(bn[i]).find(".batchNodeValue").val();
					var batchNodeTimeValue = $(bn[i]).find(".batchNodeTimeValue").val();
					if(undefined != batchNodeValue){
						if(""==$.trim(batchNodeValue)||parseInt(batchNodeValue) == 0){
							alert("分批设置不合理！");
							subcount = 0;
							$("#btsend").attr("disabled","");
							$("#btcancel").attr("disabled","");
							return;
						}
                        //bug 在预览效果页将发送时间清空，点击‘发送’，提示‘任务创建失败：提交短信任务失败
                        if("" == $.trim(batchNodeTimeValue)){
                            alert("分批时间设置不合理！");
                            subcount = 0;
                            $("#btsend").attr("disabled", "");
                            $("#btcancel").attr("disabled", "");
                            return;
                        }
						// if(isNaN(Date.parse(batchNodeTimeValue))){
						// 	alert("分批发送时间设置不合理！");
						// 	subcount = 0;
						// 	$("#btsend").attr("disabled", "");
						// 	$("#btcancel").attr("disabled", "");
						// 	$(bn[i]).find(".batchNodeTimeValue").addClass("WdateFmtErr");
						// 	return;
						// }
						sum += parseInt(batchNodeValue);
						// 将分批设置添加到form表单，便于提交到后台
						$("form[name='" + formName + "']").append("<input class='hiddenBatchNode' name='batchNode" + i + "' type='hidden' value='" + batchNodeValue + "'/>");
						$("form[name='" + formName + "']").append("<input class='hiddenBatchNode' name='batchNodeTimeValue" + i + "' type='hidden' value='" + batchNodeTimeValue + "'/>");
					}
				}
				if(sum>sumLimit){
					alert("分批总数不能超过有效号码数！");
					subcount = 0;
					$("#btsend").attr("disabled","");
					$("#btcancel").attr("disabled","");
					return;
				}
				if(sum!=sumLimit){
					alert("分批总数与有效号码数不一致！");
					subcount = 0;
					$("#btsend").attr("disabled","");
					$("#btcancel").attr("disabled","");
					return;
				}
				
				$("form[name='" + formName + "']").append("<input  class='hiddenBatchNode' name='batchNodeNum' type='hidden' value='"+ len +"' />");
				$.post("common.htm?method=getServerTime", {
				
				}, function(msg) {
					var serverTime = msg;
					var date2 = new Date(Date.parse(serverTime.replaceAll("-", "/")));
					var isExpire = false;
					if($("#splitFlag")&&$("#splitFlag").val()){
						var bn = $(".custBatchNode");
						var len = bn.length;
						var timingDate;
						for (var i = 0; i < len; i++) {
							var bntv = $(bn[i]).find(".batchNodeTimeValue");
							var batchNodeTimeValue = bntv.val();
							timingDate = new Date(Date.parse(batchNodeTimeValue.replaceAll("-", "/")));
							// 定时时间应该在当前时间一分钟之后，将服务器时间减去1分钟之后与定时时间比较做判断
							timingDate.setMinutes(timingDate.getMinutes()-1, 59, 0);
							if(timingDate<=date2){
								isExpire = true;
								bntv.addClass("WdateFmtErr");
							}
						}
					}
					
					if (isExpire) {
						alert("定时时间不能小于服务器时间，且应在当前时间1分钟之后！");
						subcount = 0;
						showSelect();
						$("#subSend").attr("disabled","");
						$("#qingkong").attr("disabled","");
						$("#btsend").attr("disabled","");
						$("#btcancel").attr("disabled","");
						return;
					}
					else
					{
						$.post("common.htm?method=getSpUserTimePeriod", {
							spUser:$("#spUser").val()
						}, function(spUserTimePeriod) {
							
							if(spUserTimePeriod.length>0 && ("00:00:00-23:59:59" == spUserTimePeriod)){
								
							}else{
								var startSpUserTimeDate = new Date();
								var endSpUserTimeDate = new Date();
								var startSpUserTime = spUserTimePeriod.split("-")[0];
								startSpUserTimeDate.setHours(startSpUserTime.split(":")[0], startSpUserTime.split(":")[1], startSpUserTime.split(":")[2], 0);
								var endSpUserTime = spUserTimePeriod.split("-")[1];
								endSpUserTimeDate.setHours(endSpUserTime.split(":")[0], endSpUserTime.split(":")[1], endSpUserTime.split(":")[2], 0);
								
								if($("#splitFlag")&&$("#splitFlag").val()){
									var bn = $(".custBatchNode");
									var len = bn.length;
									var timingDate;
									for (var i = 0; i < len; i++) {
										var bntv = $(bn[i]).find(".batchNodeTimeValue");
										var batchNodeTimeValue = bntv.val();
										timingDate = new Date(Date.parse(batchNodeTimeValue.replaceAll("-", "/")));
										if(startSpUserTimeDate>timingDate || timingDate>endSpUserTimeDate){
											isExpire = true;
											bntv.addClass("WdateFmtErr");
										}
									}
								}
							}
							
							if (isExpire) {
								alert("定时时间需要在sp账号发送时间("+spUserTimePeriod+")之内，请重新设置！");
								subcount = 0;
								showSelect();
								$("#subSend").attr("disabled", "");
								$("#qingkong").attr("disabled", "");
								$("#btsend").attr("disabled", "");
								$("#btcancel").attr("disabled", "");
								return;
							} else {
								if($("#effs").text()=="0")
								{
									alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_128'));
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
									$("form[name='" + formName + "']").attr("action","dsm_"+base_reTitle+".htm?method=send&lguserid="+lguserid+"&lgusername="+lgusername+"&lgcorpcode="+lgcorpcode);
									if($("form[name='" + formName + "']").attr("encoding"))
									{
										$("form[name='" + formName + "']").attr("encoding","application/x-www-form-urlencoded");
									}else
									{
										$("form[name='" + formName + "']").attr("enctype","application/x-www-form-urlencoded");
									}
								$.post('common.htm?method=frontLog',{
									info:getJsLocaleMessage('dxzs','dxzs_ssend_alert_299')+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
									+'，taskId:'+$("#taskId").val()+'，sendType:'+$("#sendType").val()+'，bmtType:'+$("#bmtType").val()
									+'，spUser:'+$("#spUser").val()
								});
									isSend = 1;
									$("form[name='" + formName + "']").submit();
								}
							}
						});
					}
				});
			}else{
				subcount = subcount+1;
				if(subcount > 1)
				{
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_80'));
					$("form[name='" + formName + "']").attr("action","");
					return;
				}
				$("#btsend").attr("disabled",true);
				$("#btcancel").attr("disabled",true);
				var nomsg = getJsLocaleMessage('dxzs','dxzs_ssend_alert_36');
			    if($("#taskname").val()==nomsg)
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
					if (date1 <= date2  && $("input[name=timerStatus]:checked").val() == 1) {
						alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_20'));
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
							alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_128'));
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
							$("form[name='" + formName + "']").attr("action","dsm_"+base_reTitle+".htm?method=send&lguserid="+lguserid+"&lgusername="+lgusername+"&lgcorpcode="+lgcorpcode);
							if($("form[name='" + formName + "']").attr("encoding"))
							{
								$("form[name='" + formName + "']").attr("encoding","application/x-www-form-urlencoded");
							}else
							{
								$("form[name='" + formName + "']").attr("enctype","application/x-www-form-urlencoded");
							}
						$.post('common.htm?method=frontLog',{
							info:getJsLocaleMessage('dxzs','dxzs_ssend_alert_299')+ $("#lguserid").val() +'，lgcorpcode:'+ $("#lgcorpcode").val() 
							+'，taskId:'+$("#taskId").val()+'，sendType:'+$("#sendType").val()+'，bmtType:'+$("#bmtType").val()
							+'，spUser:'+$("#spUser").val()
						});
							isSend = 1;
							$("form[name='" + formName + "']").submit();
						}
					}
				});
			}
			
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
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_95'));
			else
				alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_96'));
			});
		}

		
		function bycheck(type)
		{
			//清楚草稿箱信息
			$('#draftFile').val('');
            $('#draftId').val('');
            $('#draftFileTemp').val('');

		    var sendTypevalue = $("#sendType").val();
            //不同内容群发，切换tab显示不同的提示图片
            var oldSrc = $("#dsm_descriptInfo img").attr('src');
            var index = oldSrc.lastIndexOf('/');
            var oldFileName = oldSrc.substring(index + 1, oldSrc.length);
		   if(type == '1')
		   {
		       //不同内容群发，切换tab显示不同的提示图片
               var newFileName = oldFileName.substring(0, 6) + 'diffdynamic_desc.png';
               var newSrc = oldSrc.substring(0, index + 1) + newFileName;
               $("#dsm_descriptInfo img").attr('src',newSrc);

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
               //不同内容群发，切换tab显示不同的提示图片
               var newFileName = oldFileName.substring(0, 6) + 'difffile_desc.png';
               var newSrc = oldSrc.substring(0, index + 1) + newFileName;
               $("#dsm_descriptInfo img").attr('src',newSrc);

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
	   			 $('#upfilediv').append("<li id='tr"+fileCount+"'><i class='x-icon "+icon+"'></i>"+name+" <a href='javascript:delfile("+fileCount+")' class='x-del'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_127')+" </a></li>");
	   			 
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
		if(menuCode == "1050-1050")
		 {
		 	menuCode = "1050-1200";
		 }
		 else
		 {
		 	menuCode = "2300-1400";
		 }
		closemessage();
		window.parent.openNewTab(menuCode,base_path+"/smt_smsSendedBox.htm?method=find&taskid="+taskid+"&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&pageIndex="+1+"&pageSize="+15);
	 }
	 function closemessage()
	 {
		 $("#message").dialog("close");
	 }
	 
	function setDefault()
	{
		if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_98'))) {
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
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_99'));
					return;
				} 
				else if(result == "fail"){
					alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_100'));
					return;
				}
			});
		}
	}
	
	//选择草稿箱
	function showDraft(draftstype)
	{
	    var tpath = $("#cpath").val();
	    var frameSrc = tpath+"/common.htm?method=getDrafts&draftstype="+draftstype+"&timee=" + new Date().getTime();
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
	        alert(getJsLocaleMessage('dxzs','dxzs_ssend_alert_101'));
	        return;
	    }
	    //已存在草稿箱 提示覆盖
	    if($('#containDraft').val()){
	        if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_102'))){
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
    		trhtml.push("<a href='javascript:delRow("+fileCount+")' class='x-del'>"+getJsLocaleMessage('dxzs','dxzs_ssend_alert_127')+"</a>");
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
	    if(confirm(getJsLocaleMessage('dxzs','dxzs_ssend_alert_76')))
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
	//调用当前页的悬浮框
	function floatings(id,id2){
		$("#"+id).hover(
				function(){
					$(this).css("text-decoration","underline");
					//$(".ifr").show();
					$("#"+id2).show();
				},function(){
					$("#"+id2).hide();
					//$(".ifr").hide();
					$(this).css("text-decoration","none");
				}
		);
		$("#"+id2).hover(
				function(){
					//$(".ifr").show();
					$("#"+id2).show();
				},function(){
					$("#"+id2).hide();
					//$(".ifr").hide();
				}	
		);
	}
	
	