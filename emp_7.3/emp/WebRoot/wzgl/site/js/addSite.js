$(function(){
	
	//登录后页面初始值
	getLoginInfo("#hiddenValueDiv");
	
	/** 页面结构区开始 start **/
	//页面结构区-页面结构鼠标hover事件
	
	$("#pages .nav_item").live({
		mouseenter:
		function(){
				$(this).addClass("c_hoverBg");
		},
		mouseleave:		
		function(){
			$(this).removeClass("c_hoverBg");
		}
	});
	
	$("#page_list .nav_item_level2_wrapper:first .del_icon").remove();
	
	//页面结构区-页面结构鼠标click事件
	$("#pages .nav_item").live("click",function(){
		 $(".c_selectedBg").removeClass("c_selectedBg");
		 $(this).addClass("c_selectedBg");
		 $("#plantId").val("");
		 $("#plantType").val("");
		 var pageId = $(this).attr("pageid");
		 var ptype = $(this).attr("ptype");
		 var stype = $("#stype").val();
		 var pathUrl = $("#pathUrl").val();
		 var lgcorpcode = $("#lgcorpcode").val();
		 var url = $(this).attr("url");
		 var accessUrl = $.trim($("#accessUrl").html());
		 $("#accessUrl").html(accessUrl.replace(/urlToken\=(\w+)/,"urlToken="+url));
		 
		 if(pageId == "0"){
			 reset_content();
		 }else{
			 url = pathUrl + "/wzgl_siteManager.htm?method=getPageInfo";
			 $.post(url,{
				 pageId:pageId,
				 ptype:ptype,
				 lgcorpcode:lgcorpcode,
				 stype:stype,
				 isAsync: "yes"
			 },function(data){
				 if(data == "outOfLogin"){
			 			window.location.href = pathUrl + "/common/logoutEmp.html";
			 	 }
				 if(data=="fail"){
					 alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_20"));
				 }else{
					 $("#page_item_"+ptype).replaceWith(data);
					 scrollinit();
				 }
			 });
			 $("#page_item_" + $.trim(ptype)).show().siblings().hide();
			 
			 //加载当前页面的控件
			 url =  pathUrl + "/wzgl_siteManager.htm?method=getPlantFormInfo";
			 $.post(url,{
				 pageId:pageId,
				 ptype:ptype,
				 lgcorpcode:lgcorpcode,
				 stype:stype,
				 isAsync:"yes"
			 },function(data){
				 if(data=="fail"){
					 alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_21"));
				 }else{
					 reset_edit_content();
					 $("#edit_content .area_content_edit .nano-content").append(data);
					 
					 $("#default_edit").show().siblings().hide();
				 }
				 scrollinit();
			 });
		 }
	});
	
	
	//页面结构区-编辑站点名称和微站页面名称
	$("#pages .nav_item i.edit_icon").live("click",function(e){
	    e.stopPropagation();
	    var $nameo = $(this).parent().parent().parent().next();
	    
	    var $nav_item = $(this).parents(".nav_item");
        var sid = $nav_item.attr("sid");
        var pageid = $nav_item.attr("pageid");
        var ptype = $nav_item.attr("ptype");
	    var nameh = $nameo.html();
	    var namet = $nameo.find("a").text();
        var url = "";
        var options = {isAync:"yes"};
        var pathUrl = $("#pathUrl").val();
        $nameo.children().hide();
	    $nameo.append("<input class='rname' value='"+namet+"' length='16' title='" + getJsLocaleMessage("wzgl","wzgl_qywx_site_text_22")+ "'/>");
	    $nameo.find("input").select().click(function(e)
        {
	        e.stopPropagation();
        }).blur(function(e)
        {
            e.stopPropagation();
            var text = $.trim($(this).val());
            if(text!="" && text != namet)
            {
                options.name = text;
                options.lgcorpcode = $("#lgcorpcode").val();
                
                if("0" == pageid && "0" == ptype){
                    title = getJsLocaleMessage("wzgl","wzgl_qywx_site_text_23");
                    url = pathUrl + "/wzgl_siteManager.htm?method=updateSiteName";
                    options.sid = sid;
                }else{
                    title = getJsLocaleMessage("wzgl","wzgl_qywx_site_text_24");
                    url = pathUrl + "/wzgl_siteManager.htm?method=updatePageName";
                    options.pageid = pageid;
                }
              //编辑请求
                $.post(url,options,function(data){
                	 if(data == "outOfLogin"){
 			 			window.location.href = pathUrl + "/common/logoutEmp.html";
                	 }
                    if("success"==data){
                    	$nameo.find("a").text(text).attr("title",text).show().siblings().remove();
                        alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_25"));
                    }else if("overlength"==data){
                    	 $nameo.html(nameh);
                    	alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_26")); 
                    	return false;
                    }else{
                    	alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_27"));
                    	 $nameo.html(nameh);
                    	return false;
                    }
                });
            }else
            {
                $nameo.html(nameh);
            }
        });
	    
	});
	
	//页面结构区-删除微站页面
	$("#pages .nav_item i.del_icon").live("click",function(e){
		e.preventDefault();
		e.stopPropagation();
		var $nav_item = $(this).parents(".nav_item");
		var pageid = $nav_item.attr("pageid");
		var pathUrl = $("#pathUrl").val();
		var lgcorpcode = $("#lgcorpcode").val();
		
		if(confirm(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_28"))){
			//删除页面请求
			var url = pathUrl + "/wzgl_siteManager.htm?method=deletePageInfo";
	  		$.post(url,{pageid:pageid,lgcorpcode:lgcorpcode},function(data){
	  			if("success"==data){
	  				$nav_item.remove();
	  				reset_content();
	  			    alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_29"));	
	  			}else{
	  				alert(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_30"));	
	  			}
	  		});
		}
	});
	
	//页面结构区-复制微站页面
	$("#pages .nav_item i.add_icon").live("click",function(){
		//var $nav_item_wrapper = $(this).parents(".nav_item_wrapper");
		var $nav_item = $(this).parents(".nav_item");
		var pageid = $nav_item.attr("pageid");
		var pathUrl = $("#pathUrl").val();
		var lgcorpcode = $("#lgcorpcode").val();
		if(confirm(getJsLocaleMessage("wzgl","wzgl_qywx_site_text_31"))){
			//复制页面请求
			var url = pathUrl + "/wzgl_siteManager.htm?method=copyPageInfo";
	  		$.post(url,{pageid:pageid,lgcorpcode:lgcorpcode,isAsync:"yes"},function(data){
	  			 if(data == "outOfLogin"){
			 			window.location.href = pathUrl + "/common/logoutEmp.html";
			 	 }
	  			$("#page_list .sub_nav_list .menuContent .nano-content").append(data);
	  			reset_content();
	  		});
		};
	});
	
	//编辑器和预览区默认显示
	$("#default_show").show().siblings().hide();
	$("#default_edit").show().siblings().hide();
	
	/** 页面结构区开始 end **/
	
	//手机预览区-鼠标经过事件(显示"编辑")
	$(".area_content_show .page_item_view > div.plant").live({
		mouseenter:
		function(){
			//$(this).addClass("plant_selected");
			var container_h=$(this).height(),
				container_w=$(this).width()-2;
			$(this).prepend('<div class="plant_edit" style=""><span style="float:right">' + getJsLocaleMessage("wzgl","wzgl_qywx_site_text_32")+'</span></div>');
			$('.plant_edit',this).css({'width':container_w+'px','height':container_h+'px'});
		},
		mouseleave:
		function(){
			//$(this).removeClass("plant_selected");
			$(this).find('.plant_edit').remove();
		}
	});
	
	//手机预览效果-初始化手机预览效果
	function reset_show_content(){
		 $("#show_content .page_item_view").html("");
	     $("#default_show").show().siblings().hide();
	}
	
	//可编辑区域-初始化可编辑区域
	function reset_edit_content(){
		$("#edit_content .area_content_edit .nano-content").children(":not('#default_edit')").remove();
		$("#default_edit").show().siblings().hide();
	}
	
	//初始化手机预览效果&初始化可编辑区域
	function reset_content(){
		reset_show_content();
		reset_edit_content();
	}
});