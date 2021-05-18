	 $(document).ready(function() {
		 	$("#infoDiv").dialog({
		 		autoOpen: false,
				//height: 540,
				width: 535,
				resizable: false,
				modal: true,
		 		close: function() {
					//0为取消操作；1：为确定操作
					var hidIsDoOk = $("#hidIsDoOk").val();
					if (hidIsDoOk == "0") {
						doNo();
					}
					$("#hidIsDoOk").val("0");
					$('#group').val($('#groupStrTemp').val());
					$('#appcorpcode').val($('#appCorpCodeTemp').val());
		 		},
		 		open:function(){
		 			$('#groupStrTemp').val($('#group').val());
		 			$('#appCorpCodeTemp').val($('#appcorpcode').val());
		 		}
		 	})
		 });
 		
		//还原
		function doNo() {
			$(window.frames['flowFrame'].document).find("#right").empty();
			$(window.frames['flowFrame'].document).find("#getChooseMan").empty();
			if ($("#manCountTemp").val() == null || $("#manCountTemp").val() == "") {
				$(window.frames['flowFrame'].document).find("#manCount").html("0");
			} else {
				$(window.frames['flowFrame'].document).find("#manCount").html($("#manCountTemp").val());
			}
			$(window.frames['flowFrame'].document).find("#rightSelectTemp").empty();
		}
		
		function showInfo() {
			$(window.frames['flowFrame'].document).find("#right").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html());
			$(window.frames['flowFrame'].document).find("#getChooseMan").html($(window.frames['flowFrame'].document).find("#rightSelectTempAll").html().replace(/option/gi, "li").replace(/value/g, "dataval"));
			if ($("#manCountTemp").val == null || $("#manCountTemp").val == "") {
				$(window.frames['flowFrame'].document).find("#manCount").html("0");
			} else {
				$(window.frames['flowFrame'].document).find("#manCount").html($("#manCountTemp").val());
			}
			$(window.frames['flowFrame'].document).find("#showUserName").html(getJsLocaleMessage('appmage','appmage_js_msgSend_curgroup'));//.html("当前群组：");
			$(window.frames['flowFrame'].document).find("#left").empty();
			$(window.frames['flowFrame'].document).find("#group").val("");
			$(window.frames['flowFrame'].document).find('#pageIndex1,#totalPage1').val(1);
			$(window.frames['flowFrame'].document).find('#showPage1').html('');
			$("#infoDiv").dialog("open");
		}
		
		function doOk() {
			$("#appcode").val("");
			var eIds = "";
			var names = "";
			var appcode = "";
			if ($(window.frames['flowFrame'].document).find("#right option").size() < 1) {
				//alert("您没有选择任何人员！");
				alert(getJsLocaleMessage('appmage','appmage_js_msgSend_selectpeploe'));
				return;
			}
		
			$(window.frames['flowFrame'].document).find("#right option").each(function() {
				eIds = $(this).val();
				names = $(this).text();
				appcode = $(this).attr("appcode");
		
				if (appcode != null && appcode != "") {
					$("#appcode").val($("#appcode").val() + appcode + ",");
				}
			});
			$(window.frames['flowFrame'].document).find("#rightSelectTempAll").html($(window.frames['flowFrame'].document).find("#right").html());
		
			//改变弹出框状态为1
			$("#hidIsDoOk").val("1");
			$("#manCountTemp").val($(window.frames['flowFrame'].document).find("#manCount").html());
			$("#selectedTotal").html($(window.frames['flowFrame'].document).find("#manCount").html());
			$('#groupStrTemp').val($('#group').val());
			$('#appCorpCodeTemp').val($('#appcorpcode').val());
			$("#infoDiv").dialog("close");
		}
		
		function doSelectEClose() {
			$("#infoDiv").dialog("close");
		}

		function reloadPage() {
			var menuCode = base_menuCode;
			if (menuCode == "4700-1100") {
				window.location.href = 'app_msgsend.htm?method=find&lguserid=' + $('#lguserid').val() + '&lgcorpcode=' + $('#lgcorpcode').val() + '&lgguid=' + $('#lgguid').val();
			} else {
				window.location.href = 'app_msgsendd.htm.htm?method=find&lguserid=' + $('#lguserid').val() + '&lgcorpcode=' + $('#lgcorpcode').val() + '&lgguid=' + $('#lgguid').val();
			}
		}
		
		$(document).ready(function(){
			setShowAndHide('#isSmsSend', 'input[name="sendType"]:checked','#configDiv');
			$('#sendsel-div,#sendTabs').bind('click',function(e){
					if($(e.target).is('input')){
						setShowAndHide('#isSmsSend', 'input[name="sendType"]:checked','#configDiv');
					}else if($(e.target).is('label')){
					}else{
						return false;
					}
			});
			$('#u_o_c_explain').click(function(){
				$('#moreSelect').toggle();
				if($('#moreSelect').is(':hidden')){
					$(this).find('#foldIcon').removeClass('fold').addClass('unfold');
				}else{
					$(this).find('#foldIcon').removeClass('unfold').addClass('fold');
				}
			})
		})
		function setShowAndHide(checkbox,radio,div){
			var $check = $(checkbox);
			var $radio = $(radio);
			var $div = $(div);
			if($radio.val()==0){
				$check.parent().show();
			}else{
				$check.parent().hide();
				$check.attr('checked',false);
			}
			if($check.attr('checked')){
				$div.removeClass('hide');
				$('#ft-count').show();
				$('#hidisSmsSend').val(1);
			}else{
				$div.addClass('hide');
				$('#ft-count').hide();
				$div.find('#moreSelect').hide();
				$('#hidisSmsSend').val(0);
			}
			setGtInfo();
		}