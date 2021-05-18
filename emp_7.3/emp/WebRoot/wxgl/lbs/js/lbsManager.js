//新增位置采集点
		function handleLbs(type,pid){
			var lgcorpcode = $("#lgcorpcode").val();
			var pathUrl = $("#pathUrl").val();
			if(type == "add"){
				location.href = pathUrl+"/weix_lbsManager.htm?method=toAddUpdateLbs&skiptype=add&lgcorpcode="+lgcorpcode;
			}else if(type == "update"){
				doGo(pathUrl+"/weix_lbsManager.htm?method=toAddUpdateLbs&skiptype=update&lgcorpcode="+lgcorpcode+"&pid="+pid);
			}else if(type == "del"){
				if(confirm(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_28"))){
					$.post(pathUrl+"/weix_lbsManager.htm",{
					    method: "delLbs",
					    pid :pid,
					    isAsync: "yes"
						},function(returnmsg){
							if (returnmsg == "outOfLogin") {
								window.location.href = pathUrl + "/common/logoutEmp.html";
								return;
							}else if(returnmsg == "success"){
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_29"));
								setTimeout('back()',1500); 
								return;
							}else if(returnmsg == "fail"){
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_30"));
								return;
							}else if(returnmsg == "error"){
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_31"));
								return;
							}else{
								alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_30"));
								return;
							}
				});
				}
				else {
					alert(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_32"));
				}
/*//				art.dialog.confirm(getJsLocaleMessage("wxgl","wxgl_qywx_dlwzcj_text_28"), function () {
//						
//				}, function () {
//				    
//				});
*/			}
		}
		$(function() {
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

		
		function back(){
			var lgcorpcode = $("#lgcorpcode").val();
			var pathUrl = $("#pathUrl").val();
			doGo(pathUrl + "/weix_lbsManager.htm?method=find&lgcorpcode="+lgcorpcode);
			return;
		}
		//展示信息的弹出框
		function openShowDialog(showtype){
			var throughBox = art.dialog.through;
				throughBox({
			    content:document.getElementById(showtype),
			    lock: true,
			    id: showtype,
			    title: getJsLocaleMessage("wxgl","wxgl_qywx_yhgl_text_10"),
			    width:300
			});
		}
		
		function toPushSet(){
			var pathUrl = $("#pathUrl").val();
			var lgcorpcode = $("#lgcorpcode").val();
			doGo(pathUrl+"/weix_lbsManager.htm?method=toPushSetLayout&lgcorpcode="+lgcorpcode);
		}
		
		function doGo(url)
		{
			location.href = url;
		}
