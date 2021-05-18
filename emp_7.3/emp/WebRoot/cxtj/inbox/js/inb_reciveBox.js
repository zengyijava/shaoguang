var contentLen = 990;
var baseContentLen = 990;
// 短信内容键盘事件监听统计字数
		function synlen() {
			$("#msg").keyup(function() {
				var content = $(this).val();
				var huiche = content.length - content.replaceAll("\n", "").length;
				if (content.length + huiche > contentLen) {
					$(this).val(content.substring(0, contentLen - huiche));
				}
				len($(this));
			});
		}
		
		function modify(t)
		{
		$('#modify').dialog({
			autoOpen: false,
			width:250,
		    height:200
		});
		$("#msgss").empty();
		$("#msgss").html($(t).children("label").children("xmp").html());
		$('#modify').dialog('open');
		}

		function numberControl(va)
		{
			var pat=/^\d*$/;
			if(!pat.test(va.val()))
			{
				va.val(va.val().replace(/[^\d]/g,''));
			}
		}
		//发送起止时间控制
		function rtime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#sendtime").attr("value");
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
		}

		function stime(){
		    var max = "2099-12-31 23:59:59";
		    var v = $("#recvtime").attr("value");
		    var min = "1900-01-01 00:00:00";
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:v});

		}
		//快捷回复
		var moId = null;
		function ksreplay(moid,spUser)
		{
		    setGtInfo(spUser);
		    moId = moid;
		    $("#replayCon").css("display","block");
			$('#replayCon').dialog({
				autoOpen: false,
				height: 300,
				width: 500,
				resizable:false,
				modal:true,
				close:function(){
                    hideselect();   
				}
			});
			$('#replayCon').dialog('open');
		}
		//发送
		function dook()
		{
  			$("#ok").attr("disabled",true);
		    var msg = $("#msg").val();
		    if(msg.length <=0)
		    {
		      // alert("发送内容不允许为空！");
		    	alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_1"));
		       $("#ok").attr("disabled","");
		       return;
		    }
		    if(eval($("#strlen").text())>contentLen){
				//alert("字数超过"+contentLen+"个，请重新输入");
				alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_2")+contentLen+getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_3"));
				$("#ok").attr("disabled","");
				return;
			}
		    var signStr =$("#signStr").val();
		    var busCode=$("#busCode").val();
		    var priority=$("#priority").val();
		    var lgcorpcode=$("#lgcorpcode").val();
		    var lguserid=$("#lguserid").val();
		   	$.post("inb_reciveBox.htm", {
		   	   moId : moId,
		   	   msg : msg ,
		   	   signStr : signStr,
		   	   busCode : busCode,
		   	   priority : priority,
		   	   lgcorpcode : lgcorpcode,
		   	   lguserid : lguserid,
			   method : "ksReplay"
			}, function(result) {
			     if(result == "sendfalse")
			     {
			         //alert("发送网关失败！");
			         alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_4"));
			     }
			     else if(result == "NoMoney")
			     {
			        //alert("开启了记费功能，但余额不足！");
			    	 alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_5"));
			     }
			     else if(result=="Moneyerror")
			     {
			        //alert("开启了记费功能，但扣费失败！");
			    	 alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_6"));
			     }
			     else if(result=="true")
			     {
			        hideselect();
			        $('#replayCon').dialog('close');
			       // alert("发送网关成功！");
			        alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_7"));
			     }
			     else if(result=="Nospid")
			     {
			        //alert("没有可用的发送账号！");
			    	 alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_8"));
			     }
			     else if(result=="error")
			     {
			       // alert("后台异常！");
			    	 alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_9"));
			     }
			     else if(result=="noSubNo")
			     {
			        //alert("获取尾号失败！");
			    	 alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_10"));
			     }
			     else if(result=="noUsedSubNo")
			     {
			       //alert("没有可用的尾号！");
			    	 alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_11"));
			     }
			     else if(result=="uploadFileFail")
				 {
					//alert("上传号码文件失败，取消任务创建！");
			    	 alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_12"));
				 }
			     else if(result=="subnoFailed")
			     {
			        //alert("尾号处理失败！");
			    	 alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_13"));
			     }
			     else if(result=="noSpInfo")
			     {
			        //alert("查询不到sp账号信息！");
			    	 alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_14"));
			     }
			     else if(result=="noSuffiSpFee")
				 {
					//alert("sp账号余额不足,不允许发送,请联系管理员充值");
			    	 alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_15"));
				 }
			     else if(result=="spFail")
			     {
			        //alert("查询sp账号信息异常！");
			    	 alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_16"));
			     }
			    $("#ok").attr("disabled","");
			});
		}
		
		function docancel()
		{
		    hideselect();
			$('#replayCon').dialog('close');
		}
		
		function hideselect()
		{
		    $("#msg").empty(); 
		    $("#inputContent").empty();
		    $("#msg").attr("readonly","");
			$("#msg").focus();
			$("#msg").css("background","#FFFFFF");
			$("#msg").unbind("focus");
			len($("#msg"));
		    $("#tempSelect").attr('value','');
		    $("#isSign").attr("checked","");
		    $("#signStr").css("display","none");
			$("#signStr").css("background-color","");
			$("#signStr").val("");
			$("#priority").attr('value','0');
			$(".ui-dialog-buttonpane button").first().attr("disabled","");
		}
		
		// 失去焦点时判断
		function eblur(ob) {
			if (ob.is("#msg")) {
				var content = $("#msg").val();
				var huiche = content.length - content.replaceAll("\n", "").length;
				if (content.length + huiche > contentLen) {
					$("#msg").val(content.substring(0, contentLen - huiche));
				}
				if($("#tempSelect").val()=="")
				{
					$("#inputContent").val($("#msg").val());
				}
				len(ob);
			}
		}		
		// 统计短信内容字数
		function len(ob) {
			var content = $.trim(ob.val());
			var signLen = 0;
			var sign = "";
			if(ob.is("#msg") && $("#isSign").attr("checked")){
				sign = $.trim($("#signStr").val());
				signLen = $.trim($("#signStr").val()).length;
				$("#msg").val(content.substring(0,contentLen-signLen));
			}
			var len = contentLen;
			var huiche = content.length - content.replaceAll("\n", "").length;
			countSMS($("#msg").val().length + huiche + signLen, content, sign);
			content = $("#msg").val();
			if(len != contentLen)
			{
				if($("#msg").val().length>contentLen)
				{
					$("#msg").val(content.substring(0,contentLen-signLen));
					content = $("#msg").val();
				}
				$('#maxLen').html("/"+contentLen);
			}
			huiche = huiche = content.length - content.replaceAll("\n", "").length;
			$("#strlen").html(($("#msg").val().length + huiche + signLen));
		}
		
		
		//根据账户获取账户绑定的路由信息拆分规则
		function setGtInfo(spUser)
		{
			$.post("inb_reciveBox.htm",{method : "getSpGateConfig",spUser : spUser},
				function(infoStr){
					if(infoStr !="error" && infoStr.startWith("infos:"))
					{
						var infos = infoStr.replace("infos:","").split("&");
						$("#gt1").val(infos[0]);
						$("#gt2").val(infos[1]);
						$("#gt3").val(infos[2]);
						$("#gt4").val(infos[3]);
						contentLen = infos[4];
						baseContentLen = contentLen;
						$('#maxLen').html("/"+contentLen);
					}
					len($("#msg"));
				}
			);
		}
		
		//定义startWith方法
		String.prototype.startWith=function(str){
			if(str==null||str==""||this.length==0||str.length>this.length)
			return false;
			if(this.substr(0,str.length)==str)
			return true;
			else
			return false;
			return true;
		}
		
		// 统计条数-短信拆分规则js版
		function countTS(s) {
			
			var len ;
			var maxLen;
			var totalLen;
			var lastLen;
			var signLen;
			var count;
			for(var i=1;i<4;i=i+1)
			{
				count = 0;
				if(s>0)
				{
					var gtinfo = $("#gt"+i).val();
					if(gtinfo != "")
					{
						var gtinfos = gtinfo.split(",");
						
						maxLen = gtinfos[0];
						totalLen = gtinfos[1];
						lastLen = gtinfos[2];
						signLen = gtinfos[3];
						len = s*2;
						if (len <= (totalLen - signLen + 3)*2)
							count = 1;
						else
							count = 1 + Math.floor((len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2));
						
					}
				}
				$("#ft"+i).text(count);
			}
		}
		
		// 统计条数-短信拆分规则js版(支持英文短信)
		function countSMS(s, content, sign) {
			var len ;
			var enLen;
			var reset;
			var maxLen;
			var totalLen;
			var lastLen;
			var signLen;
			var enmaxLen;
			var entotalLen;
			var enlastLen;
			var ensignLen;
			var gateprivilege;
			var ensinglelen;
			var count;
			var enMsgShortLen;
			var signLocation;
			var longSmsFirstLen;
			//短信内容是否为中文短信,false:英文;true:中文
			var isChinese = false;
			//存在签名
			if(sign.length > 0)
			{
			  content = sign + content;
			}
			for(var i=1;i<5;i=i+1)
			{
				count = 0;
				if(s>0)
				{
					enLen = 0;
					enMsgShortLen = 0;
					reset = false;
					var gtinfo = $("#gt"+i).val();
					if(gtinfo != "")
					{
						var gtinfos = gtinfo.split(",");
						
						maxLen = gtinfos[0];
						totalLen = gtinfos[1];
						lastLen = gtinfos[2];
						signLen = gtinfos[3];
						enmaxLen = gtinfos[4];
						entotalLen = gtinfos[5];
						enlastLen = gtinfos[6];
						ensignLen = gtinfos[7];
						gateprivilege = gtinfos[8];
						ensinglelen = gtinfos[9];
						signLocation = gtinfos[10];
						//签名前置
						if(signLocation == 1)
						{
							longSmsFirstLen = entotalLen - ensignLen;
						}
						else
						{
							longSmsFirstLen = entotalLen;
						}
						//支持英文短信
						if(!isChinese && gateprivilege == 1){
							//字符ASCII码
							var charAscii;
							//是否中文短信
							for(var j=0;j<content.length;j++)
							{
								enLen += 1;
								enMsgShortLen += 1;
								charAscii = content.charAt(j).charCodeAt();
								if(charAscii > 127)
								{
									isChinese = true;
								}
								for(var k=0; k<special.length; k++)
								{
									if(special[k] == charAscii)
									{
										//长短信边界值
										if(enLen % longSmsFirstLen == 0)
										{
											//条数加2
											enLen += 2;
										}
										else
										{
											enLen += 1;
										}
										enMsgShortLen += 1;
										break;
									}
								}
								
								//英文短信长度超过最大短信长度
								if(enLen > contentLen)
								{
									content = content.substring(0,j);
									//存在签名,将内容减去签名的
									if(sign.length > 0)
									{
										$("#msg").val(content.substring(sign.length));
									}
									else
									{
										$("#msg").val(content);
									}
									s = content.length;
									//重新计算条数
									reset = true;
									break;
								}
								if(isChinese)
								{
									break;
								}
							}
							//重新计算条数,重新遍历
							if(reset)
							{
								i = 0;
								continue;
							}
							//如果为短短信
							if(enMsgShortLen <= (ensinglelen - ensignLen))
							{
								enLen = enMsgShortLen;
							}
						}
						//短信内容为英文并且支持英文短信，使用英文拆分规则
						if(!isChinese && gateprivilege == 1){
							//国内通道英文短信,特殊字符按1个计算
							if(i != 4)
							{
								enLen = s;
							}
							//条数计算
							if (enLen <= (ensinglelen - ensignLen)){
								count = 1;
							}
							else{
								count = 1 + Math.floor((enLen - enlastLen +  parseInt(entotalLen) -1) / entotalLen);
							}
						}
						//中文短信
						else{
							//处理模板导入,短信内容条数计算
							if(isChinese && contentLen == 700)
							{
								contentLen = 350;
								if(s > contentLen)
								{
									content = content.substring(0,350);
									//存在签名,将内容减去签名的
									if(sign.length > 0)
									{
										$("#msg").val(content.substring(sign.length));
									}
									else
									{
										$("#msg").val(content);
									}
									
									//content = $("#msg").val();
									s = content.length;
									//重新计算条数
									i = 0;
									continue;
								}
							}
							len = s*2;
							if (len <= (totalLen - signLen + 3)*2)
								count = 1;
							else
								count = 1 + Math.floor((len - lastLen * 2 + totalLen * 2 - 1) / (totalLen * 2));
						}
					}
				}
				$("#ft"+i).text(count);
			}
			if(isChinese && contentLen == 700)
			{
				contentLen = 350;
			}
			else if(!isChinese && contentLen == 350 && baseContentLen == 700)
			{
				contentLen = 700;
			}
		}

		// 获取短信模板内容
		function getTempMsg(tmpOb) {
			var msgob = $("#msg");
			if (tmpOb.val() != "") {
				$.ajax({
					url:"inb_reciveBox.htm",
					data:{method : "getTmMsg1",tmId : tmpOb.val()},
					type:"post",
					success:function(result){
						if(result.indexOf("@")==-1){
							window.location.href='inb_reciveBox.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
							return;
						}
						result=result.substr(result.indexOf("@")+1);
						if(result=="error")
						{
							//alert("获取模板失败！");
							alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_17"));
							return;
						}
						msgob.val(result);
						msgob.attr("readonly","readonly");
						msgob.css("background","#E8E8E8");
						msgob.bind("focus",function(){
							this.blur();
						});
						//还原初始值
						contentLen = baseContentLen;
						$('#maxLen').html("/"+contentLen);
						len(msgob);
					},
					error:function(xrq,textStatus,errorThrown){
						//alert("网络或服务器无法连接，请稍后重试！[EXFV010]");
						alert(getJsLocaleMessage("cxtj","cxtj_sjcx_grsjx_text_18"));
						window.location.href='inb_reciveBox.htm?method=find&lguserid='+$('#lguserid').val()+'&lgcorpcode='+$('#lgcorpcode').val();
					}
				});
			} else {
				msgob.val($("#inputContent").val());
				msgob.attr("readonly","");
				msgob.focus();
				msgob.css("background","#FFFFFF");
				msgob.unbind("focus");
				len(msgob);
			}
		}