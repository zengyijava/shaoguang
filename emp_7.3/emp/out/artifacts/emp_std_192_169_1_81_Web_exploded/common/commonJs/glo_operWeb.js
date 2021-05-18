function doweb(){
  				//var fullName = $("#webfileurl").val();
  				//var webname = fullName.substring(fullName.lastIndexOf("\\")+1);
  				//if (webname == ""){
  				//	alert("请选择要需要处理的web.xml文件！");
  				//	return;
  				//}else if (webname != "" && webname != "web.xml"){
  			   //     alert("请选择需要处理的web.xml文件！");
  			   //     return;
  			  //  }else{
  			    //	fullName=encodeURI(fullName);
  			  		$.post("glo_operWebSvt.hts",{method:"getweb"},
	  			  	     function(returnMsg){
	  			  	     	if(returnMsg != null && returnMsg != ""){
		  			  	     	var htmlmsg = "" ;
	  			  	     		var menustr = returnMsg.split("#");
	  			  	     		if(menustr != null){
		  			  	     		for(var i=0;i<menustr.length;i++){
			  			  	     		if(menustr[i] == "" || menustr[i] == null){
											continue;
				  			  	     	}
										var arr = menustr[i].split("_");
										/*--模块ID--*/
										var menuid = arr[0];
										/*--模块状态 --*/
										var menuattr = arr[1];
										/*--模块名称--*/
										var menuname = arr[2];
										var useredMenu = "";
										var unuseredMenu = "";
										if(menuattr == "1"){
											useredMenu = "checked='checked'";
										}else if(menuattr == "0"){
											unuseredMenu = "checked='checked'";
										}
										htmlmsg = htmlmsg + " <tr><td align='center'>"+menuid+"</td><td align='center'>" + menuname + "</td>";
										var mname = menuid;
										var mnameA = menuid+"ismenuA";
										var mnameB = menuid+"ismenuB";
										if(menuid == "frame" || menuid == "global")
										{
											htmlmsg = htmlmsg + " <td align='center'>-<input type='radio' style='display:none;' value='1' name='"+mname+"' id='"+mnameA+"' checked='checked'></td></tr>";
										}else{
											/*集成  卸载 */
											htmlmsg = htmlmsg + " <td align='center'>"+getJsLocaleMessage("common","common_integrate")+"<input type='radio' value='1' name='"+mname+"' id='"+mnameA+"' "+useredMenu+">"+getJsLocaleMessage("common","common_uninstall")+"<input type='radio' value='2' name='"+mname+"' id='"+mnameB+"' "+unuseredMenu+"></td></tr>";
										}
			  			  	     	}
									if(htmlmsg.length>0){
										$("#bodymsg").empty();
										$("#bodymsg").html(htmlmsg);
									}
		  			  	     	}
		  			  	     }else{
	  			  	     		/*请检查web.xml文件格式是否正确！*/
								alert(getJsLocaleMessage("common","common_js_operWeb_1"));
								return;
			  			  	 }
	    	  			 }
    	  			 );
  	  		}


  			$(document).ready(
  					function(){
  						doweb();
  						$("#addMenu").dialog({
  							autoOpen: false,
  							height:180,
  							width: 350,
  							modal: true,
  							close:function(){
  							}
  						});

  	  		});


			function menuNamebycode(code){
				var name;
				if(code == "xtgl"){
					/*系统管理*/
					name = getJsLocaleMessage("common","common_module_xtgl");
				}else if(code == "dxkf"){
					/*短信客服*/
					name = getJsLocaleMessage("common","common_module_dxkf");
				}else if(code == "dxzs"){
					/*短信助手*/
					name = getJsLocaleMessage("common","common_module_dxzs");
				}else if(code == "txgl"){
					/*通信管理*/
					name = getJsLocaleMessage("common","common_module_txgl");
				}else if(code == "ydcw"){
					/*移动财务*/
					name = getJsLocaleMessage("common","common_module_ydcw");
				}else if(code == "ydwx"){
					/*移动网讯*/
					name = getJsLocaleMessage("common","common_module_ydwx");
				}else if(code == "ydcx"){
					/*移动彩讯*/
					name = getJsLocaleMessage("common","common_module_ydcx");
				}else if(code == "cxtj"){
					/*查询统计*/
					name = getJsLocaleMessage("common","common_module_cxtj");
				}else if(code == "yhgl"){
					/*用户管理*/
					name = getJsLocaleMessage("common","common_module_yhgl");
				}else if(code == "znyq"){
					/*智能引擎*/
					name = getJsLocaleMessage("common","common_module_znyq");
				}else{
					name = code;
				}
				return name;
			}
			/*--提交所处理的模块--*/
			function doSubmit(){
			  	var ismut = 0;
			  	/*点击“确定”将更新web.xml文件?*/
			    if(confirm(getJsLocaleMessage("common","common_js_operWeb_2"))){
			    	ismut=1;
			    }else{
					return;
				}
				var menustr = "";
				var menunamestr = "";
				$("tbody tr").each(function(){
					var name = $(this).find("td:eq(2)").find("input").attr("name");
					if(name != "" && name != null){
						var tempval = $("input:checked[name='"+name+"']").val();
						menustr = menustr + name+"_"+tempval+ "&";
					}
					var chname = $(this).find("td:eq(1)").text();
					if(chname != "" && chname != null){
						menunamestr = menunamestr + name+"_"+chname+ "&";
					}
				});
				if(menustr != "" && menustr != null){
					menunamestr=encodeURI(menunamestr);
					$.post("glo_operWebSvt.hts",{method:"updateWebXml",menustr:menustr,menunamestr:menunamestr},
		  			  function(returnMsg){
		  			  	    if(returnMsg == "success"){
		  			  	    	/*更新web.xml成功！*/
								alert(getJsLocaleMessage("common","common_js_operWeb_3"));
			  			  	}else if(returnMsg == "fail"){
		  			  	    	/*更新web.xml失败！*/
			  			  		alert(getJsLocaleMessage("common","common_js_operWeb_4"));
				  			}else{
			  			  		/*操作失败！*/
								alert(getJsLocaleMessage("common","common_operateFailed"));
					  		}
				  	});
				}else{
					/*读取页面信息失败或者没有所运行的模块！*/
					alert(getJsLocaleMessage("common","common_js_operWeb_5"));
					return;
				}
				
			}


			/*--刷新页面--*/
			function doRefresh(){
				window.location.href = window.location.href;
			}
			/*--新增--*/
			function addnewMenu(){
				$('#addMenu').dialog('open');
			}

			/*--新增模块到WEB.XML中--*/
			function add(){
				
				var menuid = $.trim($("#menuid").val());
				var menuname = $.trim($("#menuname").val());
				if(menuid == null || menuid == ""){
					/*请输入模块ID！*/
					alert(getJsLocaleMessage("common","common_js_operWeb_6"));
					return;
				}else if(menuid.length < 4){
					/*模块ID由4~8位字母组成！*/
					alert(getJsLocaleMessage("common","common_js_operWeb_7"));
					return;
				}
				if(menuname == null || menuname == ""){
					/*请输入模块名称！*/
					alert(getJsLocaleMessage("common","common_js_operWeb_8"));
					return;
				}

				var isFlag = 1;
				$("tbody tr").each(function(){
					var name = $(this).find("td:eq(2)").find("input").attr("name");
					if(menuid == name){
						/*模块ID已重复,请重新输入！*/
						alert(getJsLocaleMessage("common","common_js_operWeb_9"));
						isFlag = 2;
					}
				});
				if(isFlag == 2){
					return;
				}
				menuname=encodeURI(menuname);
				$.post("glo_operWebSvt.hts",{method:"addMenu",menuid:menuid,menuname:menuname},function(returnMsg){
					if(returnMsg == "success"){
						/*新增成功！*/
						alert(getJsLocaleMessage("common","common_newSucceed"));
	  			  	}else if(returnMsg == "fail"){
						/*新增失败！*/
	  			  		alert(getJsLocaleMessage("common","common_newFailed"));
		  			}else{
	  			  		/*操作失败！*/
						alert(getJsLocaleMessage("common","common_operateFailed"));
			  		}
					cancel();
					doRefresh();
			  	});
			}

			/*--关闭DIV--*/
			function cancel(){
				$("#menuid").val("");
				$("#menuname").val("");
				$('#addMenu').dialog('close');
			}