	$(document).ready(function() {
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
			//分页
			initPage(currentTotalPage,currentPageIndex,currentPageSize,currentTotalRec);
			deleteleftline1();
			$('#search').click(function(){submitForm();});
		});


         // 新增add   修改edit    删除del   复制 copy
		function handleForm(type,formid){
			var pathUrl = $("#pathUrl").val();
			var corpCode = $("#lgcorpcode").val();
			if(type == "add" || type == "edit" || type == "preview" || type == "statistics"){ 
				doGo(pathUrl+"/wzgl_formManager.htm?method=toSavePageForm&handletype="+type+"&formid="+formid+"&lgcorpcode="+corpCode);
			}else if(type == "link"){
				doGo(pathUrl+"/wzgl_formManager.htm?method=toAccessFormAtest&formid="+formid);
			}else if(type == "del"){
				if(confirm(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_1"))) {
					$.post(pathUrl+"/wzgl_formManager.htm",{
						method:"delForm",
						fid:formid,
						lgcorpcode:corpCode,
						isAsync: "yes"
						},function(returnmsg){
							 prompt(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_2"),returnmsg);
					});
				}else {
					 alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_3"));
				}
				
				/*art.dialog.confirm(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_1"), function () {
					
				}, function () {
				   
				});*/
			}else if(type == "copy"){
				$.post(pathUrl+"/wzgl_formManager.htm",{
					method:"copyForm",
					fid:formid,
					path:pathUrl,
					isAsync: "yes"
					},function(returnmsg){
						 prompt(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_4"),returnmsg);
					});
			}
		}

		//跳转回查询页面 
		function back(){
			var lgcorpcode = $("#lgcorpcode").val();
			var pathUrl = $("#pathUrl").val();
			doGo(pathUrl + "/wzgl_formManager.htm?method=find&lgcorpcode="+lgcorpcode);
			return;
		}

		//type  删除    复制    
		function prompt(type,returnmsg){
			if (returnmsg == "outOfLogin") {
				window.location.href = pathUrl + "/common/logoutEmp.html";
				return;
			}else if(returnmsg == "success"){
				alert(type+getJsLocaleMessage("wzgl","wzgl_qywx_form_text_5"));
				setTimeout('back()',1500); 
				return;
			}else if(returnmsg == "fail"){
				alert(type+getJsLocaleMessage("wzgl","wzgl_qywx_form_text_6"));
				return;
			}else if(returnmsg == "error"){
				alert(type+getJsLocaleMessage("wzgl","wzgl_qywx_form_text_7"));
				return;
			}else if(returnmsg == "paramisnull"){
				alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_8"));
				return;
			}else if(returnmsg == "objisnull"){
				alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_9"));
				return;
			}else{
				alert(type+getJsLocaleMessage("wzgl","wzgl_qywx_form_text_10"));
				return;
			}
		}

		//展示信息的弹出框
		function openShowDialog(showtype,title){
			var throughBox = art.dialog.through;
				throughBox({
			    content:document.getElementById(showtype),
			    lock: true,
			    id: showtype,
			    title: title,
			    width:300
			});
		}
		
		
		function rtime(){
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
				max = year+"-"+mon+"-"+day+""
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:v,maxDate:max});
		};

		function stime(){
		   var max = "2099-12-31";
		    var v = $("#submitEndTime").attr("value");
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
				min = year+"-"+mon+"-01"
			}
			WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:min,maxDate:max});
		};
		
		
		
		
		
		
		
		
		
		