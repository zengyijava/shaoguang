$(function(){
	  var explorer = window.navigator.userAgent.toLowerCase();
  	//版本号
      var ver=null;
      //如果是ie 
      if (explorer.indexOf("msie") >= 0) {
      	//版本号
          ver = explorer.match(/msie ([\d.]+)/)[1];
      }
      if(window.ActiveXObject&&ver<9) {
         $("#edit_area").css("width","auto")
      }
	/**可编辑区域-start**/
	//可编辑区域-展开与收起功能
    $(".page_item_plant_view ul.item > li.lv1").live("click",function(){
    	$(this).next("li").children().toggle();
    	var isvisible = $(this).next("li").children().css("display");
    	if (isvisible == "none") {
    		$(this).find('span.st').text(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_1"));
		}
    	else if (isvisible == "block") {
    		$(this).find('span.st').html(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_2"));
		}
    	$(this).parent().siblings().find('ul').hide();
    	$(this).parent().siblings().children('li.lv1').find('span.st').text(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_1"));
    });
    
	//可编辑区域-删除一行功能
	$(".page_item_plant_view .delNoti").live("click",function(){
		var $page_item_plant_view = $(this).parents(".page_item_plant_view");
		if($page_item_plant_view.find("ul.item").size()==1){
			alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_3"));
			return;
		}else {
			$(this).parent().parent().remove();
			resetName($page_item_plant_view.find(".buttons"));
		}
		//scrollinit();
	});
	
	//可编辑区域-重置表单标签名称功能
	function resetName(obj){
		$(obj).parents(".page_item_plant_view").find("ul.item").each(function(i){
			$(this).find("input,select,textarea").each(function(){
				$(this).attr('name',$(this).attr('name').replace(/\d+$/,'') + i);
			});
		});
	}
	
	//可编辑区域-重置"收起与展开"功能
	function resetListStatus(obj){
		$(obj).parent().siblings("ul.item").children('li.lv1').find('span.st').text(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_1"));
		$(obj).parent().siblings("ul.item").find("ul").hide();
		$(obj).parent().siblings("ul.item:last").find("ul").show();
		$(obj).parent().siblings("ul.item:last").children('li.lv1').find('span.st').text(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_2"));
		scrollinit();
	}
	
	//可编辑区域-列表-新增一行
	$("#add_normal_banner_list").live("click",function(){
		if($(this).parent().parent().find("ul.item").size()<6){
			var $item = $("#normal_plant_form_hidden ul.normal_list").html();
			$(this).parent().before($("<ul class='item normal_list'></ul>").append($item));
			resetListStatus(this);
			resetName(this);
			scrollinit();
		}else{
			alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_4"));
		}
		return false;
	});
	
	//可编辑区域-底部菜单-新增一行
	$("#add_normal_banner_menu").live("click",function(){
		if($(this).parent().parent().find("ul.item").size()<4){
			var $item = $("#normal_plant_form_hidden ul.normal_menu").html();
			$(this).parent().before($("<ul class='item normal_menu'></ul>").append($item));
			resetListStatus(this);
			resetName(this);
			
			var inputMenus = $(".page_item_plant_view ul.normal_menu:last");
			inputMenus.find('.colorpickerField').ColorPicker({
				onSubmit: function(hsb, hex, rgb, el) {
					$(el).val(hex);
					$(el).ColorPickerHide();
				},
				onBeforeShow: function () {
					$(this).ColorPickerSetColor(this.value);
				}
			})
			.bind('keyup', function(){
				$(this).ColorPickerSetColor(this.value);
			});
		}else{
			alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_5"));
		}
		return false;
	});
	
	//可编辑区域-广告图片-新增一行
	$("#add_normal_banner_img").live("click",function(){
		if($(this).parent().parent().find("ul.item").size()<3){
			var $item = $("#normal_plant_form_hidden ul.normal_head").html();
			$(this).parent().before($("<ul class='item normal_head'></ul>").append($item));
			resetListStatus(this);
			resetName(this);
		}else{
			alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_6"));
		}
		return false;
	});
	
	//可编辑区域-多标签页-新增一行
	$("#add_normal_tab_img").live("click",function(){
		if($(this).parent().parent().find("ul.item").size()<3){
			var $item = $("#normal_plant_form_hidden ul.normal_tab").html();
			$(this).parent().before($("<ul class='item normal_tab'></ul>").append($item));
			resetListStatus(this);
			resetName(this);
			var textarea = $("textara[maxlength]:last");
			textarea.blur(function() {
				checkAreaLen($(this));
			});
			textarea.keydown(function() {
				checkAreaLen($(this));
			});
			textarea.keyup(function() {
				checkAreaLen($(this));
			});
		}else{
			alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_7"));
		}
		return false;
	});
	/**可编辑区域 end**/
	
	/**手机预览效果-start**/
	//手机预览区-点击"编辑"
	$(".area_content_show .plant_edit span").live("click",function(){
		var plantId = $(this).parent().parent().attr("plantId");
		var plantType = $(this).parent().parent().attr("planttype");
		
		$("#edit_content .hidden_inputs").find("input[name='plantId']").val(plantId);
		$("#edit_content .hidden_inputs").find("input[name='plantType']").val(plantType);
		$("#edit_content .page_item_plant_view").hide();
		$("#edit_content #default_edit").hide();
		$("#plant_form_" + $.trim(plantType)).show();
		$(".editContent").nanoScroller();
	});
	
	//手机预览区-点击"tabs"
	$(".area_content_show ul.mytabs li").live("click",function(){
		$("ul.mytabs li.active").removeClass("active");
		$(this).addClass("active");
		$("#mytabs_c").next().children().hide();
		$("#mytabs_c").next().find("#"+$(this).find("a").attr('data-name')).show();
	});
	/**手机预览效果-点击"编辑"-end**/
	
	//可编辑区域-页面保存
	$("#add_save_btn").click(function(){
		//当前控件参数
		var plantId = $("#plantId").val();
		var plantType = $("#plantType").val();
		var lgcorpcode = $("#lgcorpcode").val();
		var count = "1";
		var pathUrl = $("#pathUrl").val();
		
		//当前页面参数
		var nav_item = $("#page_list div.level2.c_selectedBg");
		var pageId = nav_item.attr("pageid");
		var ptype= nav_item.attr("ptype");
		var stype = $("#stype").val();
		
		if(null == plantId || "" == plantId)
		{
			alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_8"));
			return;
		}
		if(null == lgcorpcode || "" == lgcorpcode)
		{
			alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_9"));
			return;
		}
		if(null == plantType || "" == plantType)
		{
			alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_8"));
			return;
		}
		else
		{
			count = $(".page_item_plant_view[planttype='"+ plantType+"'] ul.item").length;
			
			var data = $("#plant_form_"+plantType).serializeArray();
			data.push({name:'plantId',value : plantId});
			data.push({name:'plantType',value : plantType});
			data.push({name:'lgcorpcode',value : lgcorpcode});
			data.push({name:'count',value : count});
			data.push({name:'isAsync',value : 'yes'});
			
			var url = pathUrl + "/wzgl_siteManager.htm?method=createPlantValues";
			
		    $.post(url,data,function(data){
		    	if(data == "outOfLogin"){
		 			window.location.href = pathUrl + "/common/logoutEmp.html";
		 			return false;
			 	}
		    	if(data=="success")
				{		    		
		    		//保存后刷新手机预览区
		    		 url = pathUrl + "/wzgl_siteManager.htm?method=getPageInfo" ;
					 $.post(url,{
						 pageId:pageId,
						 ptype:ptype,
						 lgcorpcode:lgcorpcode,
						 stype:stype
					 },function(data){
						 if(data=="fail"){
							 alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_10"));
						 }else{
							 alert(getJsLocaleMessage("common","common_text_1"));
							 $("#page_item_"+ptype).replaceWith(data);
							 scrollinit();
						 }
					 });
					 $("#page_item_" + $.trim(ptype)).show().siblings().hide();
		    		return;
				}
				else
				{
					alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_11"));
		    		return;
				}
		    	
		    	
		    });
		}
	
	});
	
	//可编辑区域-图片上传1
	$(".page_item_plant_view input[name*=uploadFile]").live("change",function(){
		var name = $(this).attr("name");
		var form = $(this.form);
		var url = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=sendPic&fileName=" + name;
		var pathurl = $("#pathUrl").val();
		form.ajaxSubmit(
				{					
					url: url, 
					type: 'post',
					data:{fileName:name,isAsync:"yes"},
					dataType:'text',
					success:function(msg){
						if(msg == "outOfLogin")
		                {
		                    window.parent.showLogin(0);
		                    return;
		                }
						if (-1 != msg.indexOf('>'))
						{
							msg = msg.substring(msg.indexOf(">")+1);
						}
						if (-1 != msg.indexOf('<')) 
						{
							msg = msg.substring(0,msg.indexOf("<"));
						}
						// 转换成json对象   
			            var jsonresult = $.parseJSON(msg);
			            var isok = jsonresult.result;
			            var filepath = pathurl + "/" + jsonresult.filepath;
			            
				    	if(isok=="success")
						{
				    		//改变页面图片的src和隐藏域的值（用来保存图片路径）
				    		form.siblings('img').attr("src",filepath);
				    		form.siblings('input').val(jsonresult.filepath);
				    		
				    		return false;
						}
				    	if(isok=="oversize")
						{
				    		alertDialog(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_12"));
				    		return false;
						}
						else
						{
							alertDialog(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_13"));
				    		return false;
						}
					}
				}
		);
	});	
	

	//可编辑区域-图片上传2
	$(".page_item_plant_view input[name*=uploadSpecialFile]").live("change",function(){
		var name = $(this).attr("name");
		var form = $(this.form);
		var url = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=sendPic&fileName=" + name;
		var pathurl = $("#pathUrl").val();
		
		form.ajaxSubmit(
				{					
					url: url, 
					type: 'post',
					data:{fileName:name,isAsync:"yes"},	
					dataType:'text',
					success:function(msg){
						if(msg == "outOfLogin")
		                {
		                    window.parent.showLogin(0);
		                    return;
		                }
						if (-1 != msg.indexOf('>'))
						{
							msg = msg.substring(msg.indexOf(">")+1);
						}
						if (-1 != msg.indexOf('<')) 
						{
							msg = msg.substring(0,msg.indexOf("<"));
						}
						// 转换成json对象   
			            var jsonresult = $.parseJSON(msg);
			            var isok = jsonresult.result;
			            var filepath = pathurl + "/" + jsonresult.filepath;
			            
				    	if(isok=="success")
						{
				    		//改变页面图片的src和隐藏域的值（用来保存图片路径）
				    		form.find('ul.item li').find('img').attr("src",filepath);
				    		form.find('ul.item li').find('img').siblings('input').val(jsonresult.filepath);
				    		
							return false;
						}
				    	if(isok=="oversize")
						{
				    		alertDialog(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_12"));
				    		return false;
						}
						else
						{
							alertDialog(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_13"));
				    		return false;
						}
					}
				}
		);
	});	
	
	//可编辑区域-关联类型选择("外链"，"微页面"，"表单")
	$(".page_item_plant_view select.changeTp").live("change",function(){
		var tp = $(this).val();
		$(this).parent().siblings(".tp_linked").hide().has("input[name^='tp"+tp+"_']").show();
	});
	
	//可编辑区域-选择("外链"，"微页面")
	$(".page_item_plant_view a.tpLinkTilte").live("click",function(){
		
		var tp = $(this).parents("div.input_item").find("select.changeTp").val();
		var tp_value = $(this).parents("div.input_item").find("input[name^='tp"+tp+"_value']");
		var tp_note =  $(this).parents("div.input_item").find("input[name^='tp"+tp+"_note']");
		var nav_item = $("#page_list div.level2.c_selectedBg");
		var sId = nav_item.attr("sid");
		var lgcorpcode = $("#lgcorpcode").val();
		var list_tpvalue = $(this).parents(".input_item").find("input[name='list_tpvalue']");
		
		if(tp=="1"){
			var src = $("#pathUrl").val() + "/wzgl_siteManager.htm?method=getSitePages&from=pc&sId="+sId +"&lgcorpcode="+lgcorpcode;
			var aboutConfig = {
					content:getIframe(src,640,370,'choosePage'),
					title:getJsLocaleMessage("wzgl","wzgl_qywx_site_text_14") ,
					lock: true,
					background: "#000",
				    opacity: 0.5,
				    ok: function(){
						var iframe = $("#choosePage")[0].contentWindow;
						if(!iframe.document.body){
							alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_15"));
				        	return false;
				        };
				        var form = iframe.document.getElementById('pageForm');
						var checked = $(form).find("input[name='checklist']:checked");
						if(checked&&checked.size()>0){
							var url = checked.next().val();
							var name = checked.parent().siblings(":eq(0)").find("a").text();
							tp_value.val(url);
							tp_note.val($.trim(name).substr(0,10));
							
							if(tp_value.siblings("a.tpLinkNote").size()>0){
								if($.trim(name).length>0){
									tp_value.siblings("a.tpLinkNote").text($.trim(name).substr(0,10));
								}
							}else{
								tp_value.after(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_16")+ $.trim(name).substr(0,10)+"</a>");
							}
							return true;
						}else{
							alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_17"));
							return false;
						}
					},
					cancel: true
				};
			dlog = art.dialog(aboutConfig);
			setTimeout(function(){$(".aui_content").css("padding","0");},200);
		}else if(tp=="2"){
			var url = $("#pathUrl").val() + "/wzgl_formManager.htm?isArtDialog=true&lgcorpcode="+lgcorpcode;
			art.dialog.open(url,{
				title: getJsLocaleMessage("wzgl","wzgl_qywx_site_text_18"),
				width: "720px",
				height: "450px",
				lock: true,
				background: "#000",
			    opacity: 0.5,
			    ok: function(){
					var iframe = this.iframe.contentWindow;
					if(!iframe.document.body){
						alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_19"));
			        	return false;
			        };
			        var form = iframe.document.getElementById('pageForm');
			        var checked = $(form).find("input[name='checklist']:checked");
			        if(checked&&checked.size()>0){
			        	var value = checked.val();
			        	var name = checked.parent().siblings(":eq(1)").find("a").text();
						tp_value.val(value);
						tp_note.val($.trim(name).substr(0,10));
						
						if(tp_value.siblings("a.tpLinkNote").size()>0){
							if($.trim(name).length>0){
								tp_value.siblings("a.tpLinkNote").text($.trim(name).substr(0,10));
							}
						}else{
							tp_value.after(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_16")+ $.trim(name).substr(0,10)+"</a>");
						}
						return true;
			        }else{
			        	alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_17"));
			        	return false;
			        }
					
				},
				cancel: true
			});
		}else{
			return false;
		}
	});
	
	//可编辑区域-选择("外链"，"微页面")-预览效果
	$(".page_item_plant_view a.tpLinkNote").live("click",function(){
		var input_item = $(this).parents(".input_item");
		var tp = input_item.find("select.changeTp").val();
		var tp_value = $(this).siblings(".tpLinkValue").val();
		if("1"==tp){
			//微站页面
			url = $("#pathUrl").val() + "/wzgl_sitePreview.hts?urlToken="+tp_value;
			showbox({src:url,mode:1});
// 			$("#msgPreviewFrame").attr("src",url);
// 			 dlog = art.dialog.through({
// 					title: "预览",
// 				    content: document.getElementById('divBox'),
// 				    id: 'divBox',
// 				    lock: true
// 			  });
		}else if("2"==tp){
			//表单
			var tp_value = $(this).siblings(".tpLinkValue").val();
			var url = $("#pathUrl").val() +"/wzgl_formManager.hts?method=toAccessForm&formid=" +tp_value;
			
//			$("#msgPreviewFrame").attr("src",url);
//			 dlog = art.dialog.through({
//					title: "预览",
//				    content: document.getElementById('divBox'),
//				    id: 'divBox',
//				    lock: true
//			  });
			showbox({src:url,mode:1});
		}
	});
});

function scrollinit(){
    setTimeout(function(){
        $(".nano.showContent").nanoScroller({
            alwaysVisible: true
        });
        setTimeout(function(){
            if($(".nano.showContent").find(".nano-pane").is(":visible"))
            {
                $(".nano.showContent").find(".nano-content").css("right","-10px");
            }else
            {
                $(".nano.showContent").find(".nano-content").css("right","-15px");
            }
        },300);
    },300);
}

function getIframe(src,width,height,frameid)
{
	return '<iframe id="'+frameid+'" src = "'+src+'" frameborder="0" allowtransparency="true" style="width: '
		+width+'px; height: '+height+'px; border: 0px none;"></iframe>';
}

function alertDialog(content,width){
	var w = width||240;
	var d = art.dialog({
		title:getJsLocaleMessage("common","common_message"),
		time: 2,
	    width: w,
	    content: content
	});
	//IE下 宽高不生效 手动设置
	var ow = $('.aui_state_focus').width();
	if(ow < w){
		$('.aui_state_focus').css({'width':w+6,'height':104});
		$('.aui_state_focus .aui_border').css('width','100%');
	}
}