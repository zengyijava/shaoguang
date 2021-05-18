		
		//图片DIV信息
		function adddeldiv(num,count){
			var iPath = $("#iPath").val();
			//用背景图
			var bginfodiv = "&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:delOption("+num+","+count+");' class='nav_opr_btn' title='"+ getJsLocaleMessage("wzgl","wzgl_qywx_form_text_2") +"'>"
				+"<span class='opr_icon16 del_icon'></span></a>"
				+"<a href='javascript:addOption("+num+","+count+");' class='nav_opr_btn' title='" + getJsLocaleMessage("wzgl","wzgl_qywx_form_text_11") + "'>"
				+"<span class='opr_icon16 add_icon'></span></a>";
			//用img形式点击图片
			var imginfodiv = "<img src='"+iPath+"/img/add.png' onclick='addOption("+num+","+count+")'  title='"+getJsLocaleMessage("wzgl","wzgl_qywx_form_text_11") +"' class='addpic'>&nbsp;"
			+"<img src='"+iPath+"/img/del.png' onclick='delOption("+num+","+count+")'  title='"+ getJsLocaleMessage("wzgl","wzgl_qywx_form_text_2") +"' class='delpic'>";
			return bginfodiv;
		}

		//增加一个选项
		function addOption(num,optionSer){
			//循环获取页面支持的选项的数目
			var loop =  "#question_div_"+num + " table tr";
			var optioncount = 0;
			$(loop).each(function(){
				 var tr_count = $(this).attr("tr_count");
				 if(tr_count != "-1"){
					 optioncount = optioncount + 1;
				 }
			});
			if(optioncount > 5){
				alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_12"));
				return;
			}
			
			// 1 单选   2多选 
			var widgetType = $("#widget_type_"+num).val();
			//选项 +1
			count = count + 1;
			//选项行 ID
			var tr_widget_option_count = "tr_widget_option_"+count;
			
			var iPath = $("#iPath").val();
			var widgetcontent = "";
			//单选框
			if(widgetType == "1"){
				// 单选ID
				var radio_option_count = "radio_option_"+count;
				//输入框ID
				var r_input_option_count = "r_input_option_"+count;

				var radio_option_name_num = "radio_option_name_"+num;
				widgetcontent = "<tr id='"+tr_widget_option_count+"' tr_count='"+count+"'><td style='height:18px;' align='center'>"
					//+"<a href='javascript:delOption("+count+")'>删除</a>&nbsp;&nbsp;"
					+"<input name='"+radio_option_name_num+"'  id='"+radio_option_count+"' type='radio' value='' /></td>"
					+"<td width='468px'><input class='graytext input_bd' style='width:240px;border-top: 0;border-bottom: 0;border-right: 0;'" 
					+"name='"+r_input_option_count+"' id='"+r_input_option_count+"'  value='' maxlength='32'/>";
			//多选框
			}else if(widgetType == "2"){
				// 多选ID
				var checkbox_option_name_num = "checkbox_option_name_"+num;
				//输入框ID
				var c_input_option_count = "c_input_option_"+count;
				widgetcontent = "<tr id='"+tr_widget_option_count+"' tr_count='"+count+"'><td style='height:18px;' align='center'>"
					//+"<a href='javascript:delOption("+count+")'>删除</a>&nbsp;&nbsp;"
					+"<input type='checkbox' name='"+checkbox_option_name_num+"' class='select_check' value=''/></td>"
					+"<td width='468px'><input class='graytext input_bd' style='width:240px;border-top: 0;border-bottom: 0;border-right: 0;'" 
					+"name='"+c_input_option_count+"' id='"+c_input_option_count+"'  value='' maxlength='32'/>"
			}
			var info = adddeldiv(num,count);
			widgetcontent = widgetcontent + info +"</td></tr>";
			//新增一个option选项
			//$("#add_btn_tr_"+num).before(widgetcontent);
			$("#tr_widget_option_"+optionSer).after(widgetcontent);
			addtrhover(tr_widget_option_count);
		}

		//删除一个option
		function delOption(num,count){
			var loop =  "#question_div_"+num + " table tr";
			var optioncount = 0;
			$(loop).each(function(){
				 var tr_count = $(this).attr("tr_count");
				 if(tr_count != "-1"){
					 optioncount = optioncount + 1;
				 }
			});
			if(optioncount == 1){
				alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_13"));
				return;
			}
			$("#tr_widget_option_"+count).remove();
		}
		
		//   增加一个问题 
		function addQuestion(){
			//统计问题多少,最多10个 
			var question_count = 0
			$("div[sign='question']").each(function(){
				question_count = question_count + 1;
			});
			
			if(question_count > 9){
				alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_14"));
				return;
			}
			//widgetType  1 单选   2多选
			var widgetType = $("input[name='isCheckForm']:checked").val();
			
			var iPath = $("#iPath").val();
			//问题+1
			num = num + 1;
			//问题的DIV
			var question_num_div = "question_div_"+num;
			//该问题对应的标签类型    1 单选   2多选
			var widget_type_num = "widget_type_"+num;
			var content = "<div class='itemDiv' sign='question' style='padding-top: 15px;height: auto;' id='"+question_num_div+"' questionnum='"+num+"'>";
			content = content + "<input type='hidden' value='"+widgetType+"' id='"+widget_type_num+"'>";
			content = content + "<table class='div_bg div_bd' style='height:120px;width:566px;' border='1'>";
			//问题的html
			var title_num = "title_"+num;
			var questioncontent = "<tr tr_count='-1'><td style='height:35px;'  align='center'> " + getJsLocaleMessage("wzgl","wzgl_qywx_form_text_15") +"</td><td width='468px'><input class='graytext input_bd' style='width:360px;border-top: 0;border-bottom: 0;border-right: 0;'"
				+" name='"+title_num+"' id='"+title_num+"'  value='' maxlength='64'/>"
				//+" <input type='button' value='删除' onclick='delQuestion("+num+")'>"
				+"<img src='"+iPath+"/img/deletex.png' onclick='delQuestion("+num+")'  title='" + getJsLocaleMessage("wzgl","wzgl_qywx_form_text_16") + "' style='float: right;margin-right:20px;cursor: pointer;'>"
				+"</td></tr>";
			content = content + questioncontent;
			//循环标签
			var widgetcontent = "";
			//选项行 ID
			var tr_widget_option_count = "";
			if(widgetType == "1"){
				// 单选ID
				var radio_option_count = "";
				//输入框ID
				var r_input_option_count = "";
				//单选框的name 值
				var radio_option_name_num = "radio_option_name_"+num;
				for(var i=0;i<3;i++){
					count = count + 1;
					radio_option_count = "radio_option_"+count;
					r_input_option_count = "r_input_option_"+count;
					tr_widget_option_count = "tr_widget_option_"+count;
					widgetcontent = widgetcontent + "<tr id='"+tr_widget_option_count+"' tr_count='"+count+"'><td style='height:18px;' align='center'>"
						+"<input name='"+radio_option_name_num+"'  id='"+radio_option_count+"' type='radio' value='' /></td>"
						+"<td width='468px'><input class='graytext input_bd' style='width:240px;border-top: 0;border-bottom: 0;border-right: 0;'" 
						+"name='"+r_input_option_count+"' id='"+r_input_option_count+"'  value='' maxlength='32'/>"
						var info = adddeldiv(num,count);
						widgetcontent = widgetcontent + info +"</td></tr>";
				}
			}else if(widgetType == "2"){
				// 多选ID
				var checkbox_option_name_num = "";
				//输入框ID
				var c_input_option_count = "";
				for(var i=0;i<3;i++){
					count = count + 1;
					checkbox_option_name_num = "checkbox_option_name_"+num;
					c_input_option_count = "c_input_option_"+count;
					tr_widget_option_count = "tr_widget_option_"+count;
					widgetcontent = widgetcontent + "<tr id='"+tr_widget_option_count+"' tr_count='"+count+"'><td style='height:18px;' align='center'>"
						+"<input type='checkbox' name='"+checkbox_option_name_num+"' class='select_check' value=''/></td>"
						+"<td width='468px'><input class='graytext input_bd' style='width:240px;border-top: 0;border-bottom: 0;border-right: 0;'" 
						+"name='"+c_input_option_count+"' id='"+c_input_option_count+"'  value='' maxlength='32'/>"
						var info = adddeldiv(num,count);
						widgetcontent = widgetcontent + info +"</td></tr>";
				}
			}
			content = content + widgetcontent;
			var add_btn_tr_num = "add_btn_tr_"+num;
			//新增   option按钮 
			var btncontent = "<tr id='"+add_btn_tr_num+"'  tr_count='-1'><td  style='height:25px;text-indent:10px;'  align='center' colspan='2'>"
				//+"<a href='javascript:addOption("+num+")'>+</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+"<img src='"+iPath+"/img/add.png' onclick='addOption("+num+","+num+")'  title='"+ getJsLocaleMessage("wzgl","wzgl_qywx_form_text_11") + "'>&nbsp;&nbsp;&nbsp;&nbsp;"
				+"</td></tr>";
		//	content = content  + btncontent + "</table></div>";
			content = content  + "</table></div>";

			$("#add_option").before(content);
			setTimeout("window.parent.reinitHeight()",200);
			
			var loop =  "#"+question_num_div + " table tr";
			$(loop).each(function(){
				 var tr_count = $(this).attr("tr_count");
				 if(tr_count != "-1"){
					 var tr_id = $(this).attr("id");
					 addtrhover(tr_id);
				 }
			});	 
			
		}

		//删除问题
		function delQuestion(num){
			$("#question_div_"+num).remove();
			setTimeout("window.parent.reinitHeight()",200);
		}

		//跳转回查询页面
		function back(handletype){
			var lgcorpcode = $("#lgcorpcode").val();
			var pathUrl = $("#pathUrl").val();
			//处理跳转
			if(handletype == "defined" || handletype == "apply" || handletype == "add"){
				doGo(pathUrl + "/wzgl_formManager.htm?method=toSavePageForm&handletype=add");
			}else{
				doGo(pathUrl + "/wzgl_formManager.htm?method=find&lgcorpcode="+lgcorpcode);
			}
			return;
		}
		
		
		
		//操作类型  handletype: add 自定义创建表单    edit 编辑表单     apply套用模板创建表单
		function saveForm(handletype){
			$("#savebtn").attr("disabled","disabled");
			//表单描述
			var form_note =$("#form_note").val();
			//表单标题
			var form_title =  $.trim($("#form_title").val());
			if(form_title == null || form_title.length == 0){
				alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_17"));
				$("#savebtn").attr("disabled",false);
				return;
			}
			var pathUrl = $("#pathUrl").val();
			//问题信息msg
			var questionmsg = "";
			var isok = "isok";
			//编辑表单的时候处理   表单ID
			var formid = "";
			var warn = "";
			var formtype = "";
			if(handletype == "edit"){
				formid = $("#formid").val();
				warn = getJsLocaleMessage("wzgl","wzgl_qywx_form_text_18");
			}else if(handletype == "add" || handletype == "defined"){
				warn = getJsLocaleMessage("wzgl","wzgl_qywx_form_text_19");
				formtype = $("#selectFormType").val();
				if(formtype == null || formtype == ""){
					alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_20"));
					return;
				}
			}else if(handletype == "apply"){
				formid = $("#formid").val();
				warn = getJsLocaleMessage("wzgl","wzgl_qywx_form_text_19");
			}	
			var ishavequestion = "isno";
			//循环页面所产生的问题
			$("div[sign='question']").each(function(){
				ishavequestion = "isok";
				if(isok == "isnotitle" || isok == "isnooptionvalue"){
					return;
				}
				//获取该问题的num唯一序列号
				var num = $(this).attr("questionnum");
				//该问题的类型，单选还是多选
				var widget_type_num = $("#widget_type_"+num).val();
				var title_num = $.trim($("#title_"+num).val());
				
				if(title_num == "" || title_num.length == 0){
					alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_21"));
					isok = "isnotitle";
					return;
				}
				//转码
				var title_num_Encode = encodeURIComponent(title_num);
				var optionmsg = "";
				//循环这个div中的选项中的内容  "question_div_"+num;      "div[sign='question'] table tr"
				var loop =  "#question_div_"+num + " table tr";
				var optioncount = 0;
				$(loop).each(function(){
					if(isok == "isnooptionvalue"){
						return;
					}
					 var tr_count = $(this).attr("tr_count");
					 if(tr_count != "-1"){
						 optioncount = optioncount + 1;
						// alert(encodeURIComponent($("#r_input_option_"+tr_count).val()));
						//标签类型 1单选  2多选
						 var  input_option_value = "";
						if(widget_type_num == "1"){
							 input_option_value = $.trim($("#r_input_option_"+tr_count).val());
						}else if(widget_type_num == "2"){
							 input_option_value = $.trim($("#c_input_option_"+tr_count).val());
						}
						if(input_option_value == null || input_option_value == ""){
							alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_22")+title_num+ getJsLocaleMessage("wzgl","wzgl_qywx_form_text_23")+optioncount+getJsLocaleMessage("wzgl","wzgl_qywx_form_text_24"));
							isok = "isnooptionvalue";
							return;
						}else{
							optionmsg = optionmsg + encodeURIComponent(input_option_value) + ",";
						}
					 }
				});
				if(isok == "isok"){
					if(optionmsg.length > 0){
						optionmsg = optionmsg.substring(0,optionmsg.length-1);
					}
					//问题之间的分割  &ott& ，   一个问题中的值 分割  #ott#  ，选项之间的分割 ,
					questionmsg = questionmsg  + widget_type_num + "#ott#" + title_num_Encode + "#ott#" + optionmsg + "&ott&";
				}
			});
			
			if(ishavequestion == "isno"){
				alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_25"));
				$("#savebtn").attr("disabled",false);
			}else if(isok == "isnotitle"){
				$("#savebtn").attr("disabled",false);
			}else if(isok == "isneedtwo"){
				$("#savebtn").attr("disabled",false);
			}else if(isok == "isok"){
				var corpCode = $("#lgcorpcode").val();
				$.post(pathUrl+"/wzgl_formManager.htm",{
					method:"saveForm",
					form_note:form_note,
					form_title:form_title,
					questionmsg:questionmsg,
					corpCode:corpCode,
					formid:formid,
					handletype:handletype,
					path:pathUrl,
					formtype:formtype,
					isAsync: "yes"
					},function(returnmsg){
						 $("#savebtn").attr("disabled",false);
						if (returnmsg == "outOfLogin") {
							window.location.href = pathUrl + "/common/logoutEmp.html";
							return;
						}else if(returnmsg == "success"){
							alert(warn+getJsLocaleMessage("wzgl","wzgl_qywx_form_text_5"),2);
							setTimeout('back()',1500); 
							return;
						}else if(returnmsg == "error"){
							alert(warn+getJsLocaleMessage("wzgl","wzgl_qywx_form_text_7"), 1.5);
							return;
						}else if(returnmsg == "paramisnull"){
							alert(getJsLocaleMessage("wzgl","wzgl_qywx_form_text_8"), 1.5);
							return;
						}else{
							alert(warn+getJsLocaleMessage("wzgl","wzgl_qywx_form_text_10"), 1.5);
							return;
						}
				});
			}else{
				$("#savebtn").attr("disabled",false);
			}
		}
		
		//增加删除效果
		function addtrhover(trid){
			  $("#"+trid).hover(function(){
			    $(this).addClass('delpic_hoverBg');
			  },function(){
			    $(this).removeClass('delpic_hoverBg');
			  });
		}
		
		
		
		
		
		
		
		
		
		
		
		
		