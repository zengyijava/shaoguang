	function auestionnaire(){
			$("#submitbtn").attr("disabled","disabled");
			var pathUrl = $("#pathUrl").val();
			//提交 问题信息msg
			var questionmsg = "";
			var isok = "isok";
			//提交 表单的时候处理   表单ID
			var formid = $("#formid").val();
			//公众帐号
			var aid = $("#aid").val();
			//普通用户ID
			var wcid = $("#wcid").val();
			var formid = $("#formid").val();
			//循环页面所产生的问题
			$("div[sign='question']").each(function(){
				if(isok == "isfail"){
					return;
				}
				//获取该问题的num唯一序列号
				var num = $(this).attr("questionnum");
				
				//该问题的类型，单选还是多选
				var widget_type_num = $("#widget_type_"+num).val();
				//问题ID
				var question_id_num = $("#question_id_"+num).val();
				//选项
				var items = "";
				if(widget_type_num == "1"){
					 items = $("input:checked[name='radio_option_name_"+num+"']").val();
				}else if(widget_type_num == "2"){
					 items = checkboxOption(num);
				}
				if(items == "" || items == undefined || items == "undefined"){
					alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_15")+num+getJsLocaleMessage("wzgl","wzgl_qywx_form_text_36"));
					isok = "isfail";
					questionmsg = "";
					return;
				}
				//问题之间的分割  &ott& ，   一个问题中的值 分割  #ott#  ，选项之间的分割 ,
				//选项类型   + 问题ID + 选项ID
				questionmsg = questionmsg  + widget_type_num + "#ott#" + question_id_num + "#ott#" + items + "&ott&";
			});
			if(isok == "isok"){
				var corpCode = $("#lgcorpcode").val();
				$.post(pathUrl+"/wzgl_formManager.hts",{
					method:"accessFormQuestionnaire",
					questionmsg:questionmsg,
					corpCode:corpCode,
					formid:formid,
					aid:aid,
					wcid:wcid,
					isAsync: "yes"
					},function(returnmsg){
						 $("#submitbtn").attr("disabled",false);
						if(returnmsg == "success"){
							alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_27"),2);
							//setTimeout('back()',1500); 
							$("#delwarndiv").show();
							$("#submitbtn").removeClass("btnClass5");
							$("#submitbtn").removeClass("mr10");
							$("#submitbtn").addClass("btnClass6");
							$("#submitbtn").attr("disabled","disabled");
							$("#warninfo").empty();
							$("#warninfo").append(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_28"));
							return;
						}else if(returnmsg == "error"){
							alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_29"), 1.5);
							return;
						}else if(returnmsg == "paramisnull"){
							alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_37"), 1.5);
							return;
						}else{
							alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_30"), 1.5);
							return;
						}
				});
			}else{
				 $("#submitbtn").attr("disabled",false);
			}
		}


		//多选处理
		function checkboxOption(num){
			var items = "";
			$("input[name='checkbox_option_"+num+"']:checked").each(function(){	
				items += $(this).val()+",";
			});
			if (items != "")
			{
				items = items.toString().substring(0, items.lastIndexOf(','));
			}
			return items;
		}
		
		$(document).ready(function() {
			var publicstate = $("#publicstate").val();
			if(publicstate == "3"){
				 $("#delwarndiv").show();
				 $("#submitbtn").removeClass("btnClass5");
				 $("#submitbtn").removeClass("mr10");
				 $("#submitbtn").addClass("btnClass6");
				 $("#submitbtn").attr("disabled","disabled");
				 $("#warninfo").empty();
				 $("#warninfo").append(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_31"));
				 $("#submitbtn").hide();
			}else if(publicstate == "4"){
				 $("#delwarndiv").show();
				 $("#submitbtn").removeClass("btnClass5");
				 $("#submitbtn").removeClass("mr10");
				 $("#submitbtn").addClass("btnClass6");
				 $("#submitbtn").attr("disabled","disabled");
				 $("#warninfo").empty();
				 $("#warninfo").append(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_28"));
				 $("#submitbtn").hide();
			}
		});
		
		
		
		
		
		