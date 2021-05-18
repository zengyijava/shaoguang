var curname = "";
$(document).ready(function(){
	  $('#aid').isSearchSelect({'width':'262','select_height':'24','isInput':false,'zindex':0},function(o){
		  showMenu();
	  });
		  getLoginInfo("#logininfos");
		  getLoginInfo("#hiddenValueDiv");
		  //添加菜单
		  $('#add_nav_btn').click(function(){
			  var aid = $("#aid").val();
				if(aid == "")
				{
					alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_1"));
					return;
				}
				var size = $(".lev1menu").length;
				if(size >= 3)
				{
					alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_2"));
					return;
				}
			  $('#addMenu').dialog({
				autoOpen: false,
				height: 200,
				width: 400,
				resizable:false,
				modal:true
			});
			  $('#addMenu').dialog('open');
			  
		  });
		  //预览窗口
		  $("#divBox2").dialog({
				autoOpen: false,
				height:510,
				width: 300,
				modal: true,
				bgiframe: true ,
				overlay: {opacity: 1.0, background: "white" ,overflow:'hidden'},
				open:function(){
					$("#divBox2").dialog("option",{"title":getJsLocaleMessage("weix","weix_qywx_zdycd_text_3")});
				},
				close:function(){
				}
		 });
		  //选择图文
		  $("#tempDiv").dialog({
				autoOpen: false,
				height: 520,
				width: 750,
				resizable:false,
				modal: true,
				open:function(){
			  		$("#tempDiv").dialog("option",{"title":getJsLocaleMessage("weix","weix_qywx_zdycd_text_4")});
				},
				close:function(){
					 if(($("#tid").val() == "" || $("#tid").val()==0))
					  {
						 $('#default_show').show().siblings().hide();
						 $('#backgo').hide();
					  }
				}
			});
  //设置url
  $('#url_setting').click(function(){
	  if($(".c_selectedBg").length==0){
		  alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_5"));
		  return false;
	  }
	  $('#link_transform').show().siblings().hide();
	  $('#backgo').show();
	 
  })
  //设置图文
  $('#pic_article').click(function(){
	  if($(".c_selectedBg").length==0){
		  alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_5"));
		  return false;
	  }
	  if($(".c_selectedBg").attr("tid")=="" || $(".c_selectedBg").attr("tid") == 0)
	  {
		  chooseTemp();
	  }
	  $('#picDiv').show().siblings().hide();
	  $('#default_show').hide();
	  $('#backgo').show();
	  
  })
  
  //返回
  $('#backgo').click(function(){
	  $('#default_show').show().siblings().hide();
	  $(this).hide();
  })
  
});
function bindNavItem($ob)
{
	  //经过变色
	  $ob.hover(function(){
	    $(this).addClass('c_hoverBg');
	  },function(){
	    $(this).removeClass('c_hoverBg');
	  });
	  $ob.click(function(){
		  $(".c_selectedBg").removeClass("c_selectedBg");
		  $ob.addClass("c_selectedBg");
		  menuClick(this);
	  });
	  
}
function bindAction($ob)
{
	  //删除按钮
	  $ob.find('.del_icon').click(function(){
		  if(confirm(getJsLocaleMessage("weix","weix_qywx_zdycd_text_6")))
		  {
			   var $item = $(this).parents(".nav_item");
			   var mid = $item.attr("mid");
			   $.post("dfm_definemenu.htm",{mid:mid,method:"delMenu"},function(result){
				   if(result == "nomenu")
				   {
					   alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_7"));
					   return;
				   }
				   if(result == "true")
				   {
					   alert(getJsLocaleMessage("common","common_text_4"));
					   $item.parent().remove();
				   }else
				   {
					   alert(getJsLocaleMessage("common","common_text_5"));
				   }
			   });
			   
		  }
	  });
	  //添加子菜单
	  $ob.find('.add_icon').click(function(){
		  //添加子菜单前
		  var p=$(this).parents(".nav_item");
		  if(p.attr("pid")==0&&p.attr("mtype")!=0){
			  if(!confirm(getJsLocaleMessage("weix","weix_qywx_zdycd_text_8"))){
				  return false;
			  }
		  }
		  $('#add_sub_Menu').dialog({
				autoOpen: false,
				height: 200,
				width: 400,
				resizable:false,
				modal:true
			});
			  var pid = $(this).parents('.nav_item').attr('mid');
			  var size = $(".lev2menu[pid="+pid+"] > .l2son").length;
			  if(size >= 5)
				{
					alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_9"));
					return;
				}
			  $('#add_sub_Menu').dialog('open');
			  $('#level2pid').val(pid);
	  });
	  //改名
	  $ob.find('.edit_icon').click(function(){
		  $('#edit_sub_Menu').dialog({
			autoOpen: false,
			height: 200,
			width: 400,
			resizable:false,
			modal:true
		});
		  $item = $(this).parents('.nav_item');
		  var pid = $item.attr('pid');
		  var mid = $item.attr('mid');
		  var mname ;
		  if(pid == 0 || pid== "0")
		  {
			  mname = $item.find("h4 > a").text();
			  $("#p1").show();
			  $("#p2").hide();
			  $("#updateMname").attr("maxlength","8");
		  }else
		  {
			  mname = $item.find("h5 > a").text();
			  $("#p2").show();
			  $("#p1").hide();
			  $("#updateMname").attr("maxlength","16");
		  }
		  curname = mname;
		  $("#updateMid").val(mid);
		  $("#updateMname").val(mname);
		  var mname = $(this).parents('.nav_item').find('')
		  $('#edit_sub_Menu').dialog('open');
		  
	  });
	  
	  //上移
	  $ob.find('.arrow_up').click(function(){
		  uporder(this);
	  });
	  
	  //下移
	  $ob.find('.arrow_down').click(function(){
		  downorder(this);
	  });
}
//添加一级菜单
function addNewTab()
{
	var size = $(".lev1menu").length;
	var mname =  $.trim($("#l1menuname").val());
	$("#l1menuname").val(mname);
	if(mname=="")
	{
		alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_10"));
		return;
	}
	var lgcorpcode = $("#lgcorpcode").val();
	var pid=0;
	var aid = $("#aid").val();
	//按钮不可点 
	$("#subButL1").attr("disabled",true);
	$.post("dfm_definemenu.htm",{method: "addMenu",size:size,mname:mname,pid:pid,lgcorpcode:lgcorpcode,aid:aid},
		function(result){
			$("#subButL1").attr("disabled",false);
			if(result == "overlength")
			{
				alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_11"));
				return;
			}
			if(result.indexOf("true")==0)
			{
				alert(getJsLocaleMessage("common","common_text_6"));
				var pid = result.substr(4);
				appendNewTab(mname, size, pid,"","",0,true);
				$("#l1menuname").val("");
			}else
			{
				alert(getJsLocaleMessage("common","common_text_7"));
			}
			btcancel();
		}
	);
}
function addL2Menu()
{
	var pid=$("#level2pid").val();
	var size = $(".lev2menu[pid="+pid+"] > .l2son").length;

	var mname =  $.trim($("#level2name").val());

	 $("#level2name").val(mname);
	if(mname=="")
	{
		alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_10"));
		return;
	}
	var lgcorpcode = $("#lgcorpcode").val();
	var aid = $("#aid").val();
	//按钮不可点 
	$("#subButL2").attr("disabled",true);
	$.post("dfm_definemenu.htm",{method: "addMenu",size:size,mname:mname,pid:pid,lgcorpcode:lgcorpcode,aid:aid},
		function(result){
			$("#subButL2").attr("disabled",false);
			if(result == "overlength")
			{
				alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_12"));
				return;
			}
			if(result.indexOf("true")==0)
			{
				alert(getJsLocaleMessage("common","common_text_6"));
				var mid=$('#level2pid').val();
				var p=$(".level1[mid="+mid+"]");
				p.attr('tid','');
				p.attr('mtype',0);
				p.attr('murl','');
				var mid = result.substr(4);
				appendNewL2Tab(mname, size, pid,mid,"","",0,true);
				$("#level2name").val("");
			}else
			{
				alert(getJsLocaleMessage("common","common_text_7"));
			}
			btcancel();
		}
	);
}

function appendNewTab(mname,index,mid,tid,url,type,isclick)
{
	mname = escapeString(mname);
	var newmnue="<div class='nav_item_wrapper lev1menu' id='level0_1'>" +
	$("#level1menu").html() +
	"<div class='sub_nav_list  lev2menu ui-sortable ui-sortable-disabled' pid='"+mid+"'></div>" +
	"</div>";
	$("#bizmenu").append(newmnue);
	$("#bizmenu > .lev1menu").eq(index).find("h4 > a").attr("title",mname);
	$("#bizmenu > .lev1menu").eq(index).find("h4 > a").html(mname);
	//$(".c_selectedBg").removeClass("c_selectedBg");
	//$("#bizmenu > .lev1menu").eq(index).find(".nav_item").addClass("c_selectedBg");
	$("#bizmenu > .lev1menu").eq(index).find(".nav_item").attr("mid",mid);
	$("#bizmenu > .lev1menu").eq(index).find(".nav_item").attr("pid","0");
	$("#bizmenu > .lev1menu").eq(index).find(".nav_item").attr("tid",tid);
	$("#bizmenu > .lev1menu").eq(index).find(".nav_item").attr("murl",url);
	$("#bizmenu > .lev1menu").eq(index).find(".nav_item").attr("mtype",type);
	bindAction($("#bizmenu > .lev1menu").eq(index));
	bindNavItem($("#bizmenu > .lev1menu").eq(index).find(".nav_item"));
	if(isclick)
	{
		$("#bizmenu > .lev1menu").eq(index).find(".nav_item").trigger("click");
	}
}
function appendNewL2Tab(mname,index,pid,mid,tid,url,type,isclick)
{
	mname = escapeString(mname);
	$(".lev2menu[pid="+pid+"]").append($("#level2menu").html());
	$(".lev2menu[pid="+pid+"]").find("h5 > a").eq(index).attr("title",mname);
	$(".lev2menu[pid="+pid+"]").find("h5 > a").eq(index).html(mname);
	//$(".c_selectedBg").removeClass("c_selectedBg");
	//$(".lev2menu[pid="+pid+"]").find(".nav_item").eq(index).addClass("c_selectedBg");
	$(".lev2menu[pid="+pid+"]").find(".nav_item").eq(index).attr("mid",mid);
	$(".lev2menu[pid="+pid+"]").find(".nav_item").eq(index).attr("pid",pid);
	$(".lev2menu[pid="+pid+"]").find(".nav_item").eq(index).attr("tid",tid);
	$(".lev2menu[pid="+pid+"]").find(".nav_item").eq(index).attr("murl",url);
	$(".lev2menu[pid="+pid+"]").find(".nav_item").eq(index).attr("mtype",type);
	bindAction($(".lev2menu[pid="+pid+"]").find(".nav_item").eq(index));
	bindNavItem($(".lev2menu[pid="+pid+"]").find(".nav_item").eq(index));
	if(isclick)
	{
		$(".lev2menu[pid="+pid+"]").find(".nav_item").eq(index).trigger("click");
	}
}

//通过公众帐号查找对应菜单
function showMenu()
{
	$('.default_content').show().siblings().hide();
	var aid = $("#aid").val();
	$("#bizmenu").html("");

	if(aid=="")
	{
		$(".default_content").html(getJsLocaleMessage("weix","weix_qywx_zdycd_text_13"));
		$("#appid").val("");
		$("#secret").val("");
		return;
	}
	$(".default_content").html(getJsLocaleMessage("weix","weix_qywx_zdycd_text_14"));
	//显示loading
	$("#pos_load").show();
	
	//新增p 添加前台日志---------------------------------------
	EmpExecutionContext.log("dfm_definemenu.htm",{method: "getMenuByAId",aid:aid});
	//--------------------------------------------------------
	
	$.post("dfm_definemenu.htm",{method: "getMenuByAId",aid:aid},function(result)
	{
		//隐藏loading
		$("#pos_load").hide();
		if(result=="@")
		{
			$("#bizmenu").html("");
			return;
		}
		var indexx = result.indexOf("@");
		
		var appStr = "";
		if(indexx > 0)
		{
			appStr = result.substr(0,indexx);
		}
		if(appStr != "" && appStr.indexOf("&")>=0)
		{
			var appArr = appStr.split("&");
			$("#appid").val(appArr[0]);
			if(appArr.length > 1)
			{
				$("#secret").val(appArr[1]);
			}
		}else
		{
			$("#appid").val("");
			$("#secret").val("");
		}
		result = result.substr(indexx + 1);
		var array=eval("("+result+")");
		if(array.length==0)
		{
			$("#bizmenu").html("");
			return;
		}
		for(var i=0;i<array.length;i=i+1)
		{
			var menuArray = array[i];
			for(var j=0;j<menuArray.length;j=j+1)
			{
				var menuJson = menuArray[j];
				if(menuJson.pid==0)
				{
					appendNewTab(menuJson.mname,i,menuJson.mid,
							menuJson.tid,menuJson.murl,menuJson.mtype,false);
				}else
				{
					appendNewL2Tab(menuJson.mname,j-1,menuJson.pid,
							menuJson.mid,menuJson.tid,menuJson.murl,menuJson.mtype,false);
				}
			}
		}
	});
}

function updateMenuName()
{
	var pid = $item.attr("pid");
	var mid = $("#updateMid").val();
	var mname = $.trim($("#updateMname").val());
	$("#updateMname").val(mname);
	if(mname == curname)
	{
		alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_15"));
		return;
	}
	if(mname=="")
	{
		alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_10"));
		return;
	}
	//按钮不可点 
	$("#updateBtn").attr("disabled",true);
	$.post("dfm_definemenu.htm",{mid:mid,mname:mname,method:"updateMenuName",pid:pid},function(result){
		$("#updateBtn").attr("disabled",false);
		if(result == "overlength")
		{
			if(pid == 0)
			{
				alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_11"));
			}else
			{
				alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_12"));
			}
			return;
		}
		if(result == "true")
		{
			alert(getJsLocaleMessage("common","common_text_8"));
			var $item = $(".nav_item[mid="+mid+"]");
			if(pid == 0)
			{
				$item.find("h4 > a").attr("title",mname);
				$item.find("h4 > a").text(mname);
			}else
			{
				$item.find("h5 > a").attr("title",mname);
				$item.find("h5 > a").text(mname);
			}
		}else
		{
			alert(getJsLocaleMessage("common","common_text_9"));
		}
		$('#edit_sub_Menu').dialog("close");
	});
}
//点击选择模板
function chooseTemp()
{
	$("#tempDiv").dialog("open");
	if($("#tempFrame").attr("src") == "")
	{
		$("#tempFrame").attr("src","cwc_defaultrep.htm?method=getLfTemplateByWeix&" +
				"dsflag=1&lguserid="+$("#lguserid").val()+"&lgcorpcode="+$("#lgcorpcode").val());
	}
}
//选择模板
function tempSure()
{
	var $fo = $("#tempFrame").contents();
	var $ro = $fo.find("input[type='radio']:checked");
	
	if($ro.val() == undefined)
	{
		alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_17"));
		return;
	}else
	{
		//将模板id赋值给隐藏域
		$("#tid").val($ro.val());
		var message = $ro.next("xmp").text();
		$("#radioDiv2").empty();
		var content = "\""+ message +"\"" + getJsLocaleMessage("weix","weix_qywx_zdycd_text_18");
		var html = getJsLocaleMessage("weix","weix_qywx_zdycd_text_19");
		html = html +content;
		$("#selDiv").html(html);
	}
	$("#tempDiv").dialog("close");
}
function tempCancel()
{
	$("#tempDiv").dialog("close");
}
function btcancel(){
	  $('#addMenu').dialog('close');	
	  $('#add_sub_Menu').dialog('close');
	   $('#edit_sub_Menu').dialog('close');
  }


//浏览效果
function toPreview(){
	var tempId = $("#tid").val();
	url = "cwc_replymanger.htm?method=preview&lgcorpcode=" + $("#lgcorpcode").val() + "&tempId=" + tempId;
	$("#msgPreviewFrame2").attr("src",url);
	$("#divBox2").dialog("open");
}
function secapp(obj,reg){
	var val=$(obj).val();
	if(reg.test(val)){
		$(obj).val($(obj).val().replace(reg,''));
		
	}
}
		function publishMenu(path){
			var aid = $.trim($("#aid").val());
			var appid = $.trim($("input[name='appid']").val());
			var secret = $.trim($("input[name='secret']").val());
			$('#subBut1').attr("disabled",true);
			//公众帐号
			if(aid==null||aid==""){
				alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_16"));
				$('#subBut1').attr("disabled",false);
				return;
			}
			//Appid
			if(appid==null||appid==""){
				alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_20"));
				$('#subBut1').attr("disabled",false);
				return;
			}
			if(!/^[a-zA-Z0-9]\w+$/.test(appid)){
				alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_21"));
				$('#subBut1').attr("disabled",false);
				return;
			}
			//AppSecret
			if(secret==null||secret==""){
				alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_22"));
				$('#subBut1').attr("disabled",false);
				return;
			}
			if(!/^[a-zA-Z0-9]\w+$/.test(secret)){
				alert(getJsLocaleMessage("weix","weix_qywx_zdycd_text_23"));
				$('#subBut1').attr("disabled",false);
				return;
			}
			$.post(path+"/dfm_definemenu.htm",{
        	    method: "release",
        	    aid: aid,
        	    appid: appid,
        	    secret: secret,
        		isAsync:"yes"
        	},function(message){
        		returnResult(message);
				$('#subBut1').attr("disabled",false);
        });
			
		}