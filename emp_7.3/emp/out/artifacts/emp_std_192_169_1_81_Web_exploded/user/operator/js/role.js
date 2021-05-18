$(function() {
	// 取消按钮
	$('#btnCancel').click(function(){
		init();
	})
	// 页面加载时控制初始配置
	$("#expandAll").click(function() {
		$(".l_pLeft").each(function(index) {
			$(".chan_manag_son").eq(index).css("display", "block");
		});
	});
	
	$(".placeholder").click(function() {
		$('.graytext').focus();
	});
	
	$("#roleNameHideenSpan").click(function() {
		init();
	});
	
	$("#hidAll").click(function() {
		$(".l_pLeft").each(function(index) {
			$(".chan_manag_son").eq(index).css("display", "none");
		});
	});

	$(".l_pLeft2").each(function(index) {
		$(".chan_manag_son").eq(index).addClass("hidden");

	});
	$(".mod2 ").each(function(index) {
		$(this).click(function() {
			
			$(".chan_manag_son").hide().eq(index).toggle();
			$('.fico').addClass('unfold').removeClass('fold');
			if($(this).is(':visible')){
				$(this).find('.fico').addClass('fold').removeClass('unfold');
			}else{
				$(this).find('.fico').addClass('unfold').removeClass('fold');
			}
			
			var menuHeight = $("#r_s_manager").height();
			//$("#siderList").height(menuHeight-25);	
		});
	});

	$("input:checkbox[name='checkboxs']").each(
			function(index) {

				$(this).click(
						function(e) {
							var className="";
							if($(this).attr("checked"))
							{
								className="packCheck";
							}
							$(".mod1").eq(index).find(
									".l_pRightCon label").attr("class",
									className);
							 e.stopPropagation();

						});
			});
	// end 页面加载时初始配置
	
	//点击模拟选中
	$(".l_pRightCon label").click(function(){
		$(this).find("span").first().attr("class","");
		var className=$.trim($(this).attr("class"));
		if("packCheck"==className)
		{
			$(this).attr("class","");
		}
		else
		{
			$(this).attr("class","packCheck");
		}
		var mid=0;
		$(this).parent().parent().parent().find(".l_pRightCon label").each(
			function(){
				if($.trim($(this).attr("class"))!="packCheck")
					mid=1;
			}
		);
		if(mid==0)
			$(this).parent().parent().parent().prev()
				.find("input:checkbox[name='checkboxs']").attr("checked",true);
		else
			$(this).parent().parent().parent().prev()
				.find("input:checkbox[name='checkboxs']").attr("checked",false);
	});
	
	//鼠标经过变色
	$(".l_pRightCon label").hover(
			function(){
				//如果是未选中的鼠标经过变色
				if($.trim($(this).attr("class")).indexOf("packCheck")==-1)
				{
					$(this).addClass("packCheckHover");
					//$(this).find("span").first().attr("class","roleNameClass");
				}
			},
			function(){
				$(this).removeClass("packCheckHover");
				//$(this).find("span").first().attr("class","");
			});

	$('#siderList p').click(function() {
		//如果点击的元素重新被点中 不作任何操作
		if($(this).hasClass("roleNameColor")){
			return;
		}
		//选中元素样式
		$(this).addClass("roleNameColor").siblings().removeClass("roleNameColor");
		//如果其他兄弟元素内含有文本框则移除
		$(this).siblings().children(":text").remove();
		//隐藏添加角色层
		$("#roleNameHideenDiv").css("display","none").children(":text").val('');
		//选中默认对其为修改
		$("#state").val("2");
				var roleId = $(this).find('.Id').val();
				$.get("opt_role.htm?method=getPrivilegeByRole", {
					roleId : roleId,
                    r:Math.random()
				}, function(msg) {
					$(".chan_manag_son").css("display", "none");
					//$("input[name='check']").attr("checked", false);
					$(".l_pRightCon label").attr("class","");
					var pri=new Array();
					var pri=msg.split(",");
					// var jsonStr = msg.replace(/(^\"*)|(\"*$)/g, ""); //
					// 转换成字符串
						// var json = eval("(" + jsonStr + ")"); //
						// 转换成json格式的字符串

					/**--------老方法
						for ( var i = 0; i < pri.length; i=i+1) {
							var id1 = pri[i];
							$('input[name="check"]').each(
									function() {
										var id2 = $(this).val();
										if (id1 == id2) {
											$(this).attr("checked", true);
											$(this).parent().parent().parent()
													.css("display", "block");
										}
									});
							
						}
						$(".chan_manag_son").each(function(){
							var mid=0;
							$(this).find("input:checkbox").each(
								function(){
									if($(this).attr("checked")==false)
										mid=1;
								}
							);
							if(mid==0)
								$(this).prev()
									.find("input:checkbox[name='checkboxs']").attr("checked",true);
							else
								$(this).prev()
									.find("input:checkbox[name='checkboxs']").attr("checked",false);
							
							
							var menuHeight = $("#r_s_manager").height();
							$("#siderList").height(menuHeight-25);	
						}
						--------------*/
						//emp3.0
					//选中权限
						for ( var i = 0; i < pri.length; i=i+1) {
							var id1 = pri[i];
							$(".l_pRightCon label[id="+id1+"]").attr("class","packCheck").parent().parent().parent()
							.css("display", "block");	
						}
						//模块下的全选
						$(".mod1").each(function(){
							//各模块下具体权限数
							var total=$(this).find(".l_pRightCon label").length;
							//各模块下选中数
							var select=$(this).find(".l_pRightCon label[class='packCheck']").length;
							if(total==select)
								$(this).find("input:checkbox[name='checkboxs']").attr("checked",true);
							else
								$(this).find("input:checkbox[name='checkboxs']").attr("checked",false);
							var menuHeight = $("#r_s_manager").height();
							//$("#siderList").height(menuHeight-25);	
						}
					);
				});
		});
	$("#textfield").live('keyup blur',function(){
		var value=$.trim($(this).val());
		if(value=="")
		{
			$(this).next("label").css("display","inline");
		}else
		{
			$(this).next("label").css("display","none");
		}
		var reg=/['<>"\n]/g;
		if(reg.test(value)){
			value=value.replace(reg, "");
			$(this).val(value);
		}
		if(value.length>32){
			$(this).val(value.substring(0,32));
		}
	})
	
	
});
// 删除确认
function deleteFirm() {
	var pathUrl = $("#pathUrl").val();
	var isRoles=$("#siderList p ").hasClass("roleNameColor");
	if(!isRoles){
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_72"));	
	}else{
		if($(".roleNameColor").find(".roleName").val()==getJsLocaleMessage("user","user_xtgl_czygl_text_73")){
			alert(getJsLocaleMessage("user","user_xtgl_czygl_text_74"));
			return;
		}
		var roleId = $(".roleNameColor").children(".Id").val();
		$.post(pathUrl+"/opt_role.htm?method=canDelete",{roleId:roleId,lgusername:$("#lgusername").val()},function(result){
			if (result != null && result == "true") {
				if (confirm(getJsLocaleMessage("user","user_xtgl_czygl_text_75"))) {
					deleteR(roleId);
				} else {
					return;
				}
			}else if(result=="false"){
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_76"));
			}
		});
	}
}

// 删除
function deleteR(id) {
	var pathUrl = $("#pathUrl").val();
	var roleName = $.trim($(".roleNameColor").text()); // 得到文本框上输入的角色 名
	$.post(pathUrl+"/opt_role.htm?method=delete",{roleId:id,roleName:roleName,lgcorpcode:$("#lgcorpcode").val()},function(result){
		if (result != null && result == "true") {
			alert(getJsLocaleMessage("user","user_xtgl_czygl_text_77"));
			location.href = location;
		}else if(result=="mid")
		{
			alert(getJsLocaleMessage("user","user_xtgl_czygl_text_79"));
		}else if(result=="false")
		{
			alert(getJsLocaleMessage("user","user_xtgl_czygl_text_78"));
		}
	});
}

// 增加角色
function add() {
	var Privilege = "";
	$(".l_pRightCon label").each(
			function() {
				var className=$.trim($(this).attr("class"));
				if("packCheck"==className)
				{
					var id2 = $(this).attr("id");
					Privilege += id2 + ",";
				}
			});
	var roleName = $.trim($("#textfield").attr("value")); // 得到文本框上输入的角色 名
	var comments = $("#textarea").attr("value"); // 得到文本框上输入的comments

	if (roleName.length == 0) {
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_80"));
		$("#textfield").focus();
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	} else if (comments.length > 15) {
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_81"));
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	} else if (Privilege == "") {
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_82"));
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	} else {
		$.post("opt_role.htm?method=add", {
			roleName : roleName,
			comments : comments,
			Privilege : Privilege,
			lgguid:$("#lgguid").val(),
			lgcorpcode:$("#lgcorpcode").val()
		}, function(result) {
			if ("true" == result) {
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_83"));
				location.reload();
			}else
			if ("mid" ==result) {
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_85"));
				$("#buttonDiv").find("input[type='button']").attr("disabled","");
			}else
			{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_84"));
				$("#buttonDiv").find("input[type='button']").attr("disabled","");
			}
		});
	}

}
// 修改角色
function updateSelectedRole(roleId) {

	var roleName = $("#textfield").attr("value"); // 得到文本框上输入的角色 名
	if (roleName == null || roleName.length < 1) 
	{
		roleName=$.trim($(".roleNameColor").text());
	}
	var comments = $("#textarea").attr("value"); // 得到文本框上输入的comments
	var Privilege="";
	$(".l_pRightCon label").each(
			function() {
				var className=$.trim($(this).attr("class"));
				if("packCheck"==className)
				{
					var id2 = $(this).attr("id");
					Privilege += id2 + ",";
				}
			});
	if (comments.length > 15) {
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_86"));
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	} else if (roleName == null || roleName.length < 1) {
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_87"));
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	} else {

		$.post("opt_role.htm?method=update", {
			roleId : roleId,
			roleName : roleName,
			comments : comments,
			Privilege:Privilege,
			lgcorpcode:$("#lgcorpcode").val()
		}, function(result) {
			
			if (result=="false") {
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_142"));
				$("#buttonDiv").find("input[type='button']").attr("disabled","");
			}else
			if(result=="mid")
			{
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_88"));
				$("#buttonDiv").find("input[type='button']").attr("disabled","");
			}else {
				alert(getJsLocaleMessage("user","user_xtgl_czygl_text_141"));
				location.reload();
			}
		});
	}
}

var Privilege = "";

// 判断修改权限是否为空
function judgePrivilegeIsNull() {
	var roleId = $(".roleNameColor").children(".Id").val(); // 得到角色名对应的roleId
	if($(".roleNameColor").length==0||roleId=="")
	{
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_89"));
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	}else
	{
		$(".l_pRightCon label").each(
				function() {
					var className=$.trim($(this).attr("class"));
					if("packCheck"==className)
					{
						var id2 = $(this).attr("id");
						Privilege += id2 + ",";
					}
				});
	
		if (Privilege == "") {
			alert(getJsLocaleMessage("user","user_xtgl_czygl_text_90"));
			$("#buttonDiv").find("input[type='button']").attr("disabled","");
			return;
	
		} else if(confirm(getJsLocaleMessage("user","user_xtgl_czygl_text_91"))){
			updateSelectedRole(roleId); // 修改角色
			// updatePrivilegeOfRole(); // 修改角色权限
		}
		else
		{
			$("#buttonDiv").find("input[type='button']").attr("disabled","");
		}
		
	}
}

//emp3.0添加角色
function addDepFun()
{	
	//如果当前正处于添加角色 则不尽兴任何操作
	if($("#roleNameHideenDiv:visible").length>0){
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	}
	init();
	//标记是添加操作
	$("#state").val("1");
	$("#roleNameHideenDiv").css("display","block");
	$("#roleNameHideenDiv").children(":text").attr('id','textfield');
}

//emp3.0修改角色
function updateDepFun()
{
	var selectObjiect=$(".roleNameColor");
	if(selectObjiect.length<=0)
	{
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_92"));
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	}
	if($(".roleNameColor").find(".roleName").val()==getJsLocaleMessage("user","user_xtgl_czygl_text_73")){
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_158"));
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	}
	//已经存在正在编辑的角色则不进行任何操作
	if($(".roleNameColor").children(":text").length>0){
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	}
	$("#state").val("2");
	$("#roleNameHideenDiv").css("display","none");
	$("#textfield").val("");
	$("#textfield").attr("id","");
	var strHtml='<input type="text" id="textfield" value="'+$.trim($(".roleNameColor").find(".roleName").val())+'" />';
	selectObjiect.prepend(strHtml);
	$(".roleNameColor").children(":text").css('margin-right',"4px");
}

//emp3.0保存
function saveRole()
{
	$("#buttonDiv").find("input[type='button']").attr("disabled","disabled");
	var state=$("#state").val();
	if($(".roleNameColor").length>0&&$(".roleNameColor").find(".roleName").val()==getJsLocaleMessage("user","user_xtgl_czygl_text_73")){
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_158"));
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	}
	if(state==0)
	{
		alert(getJsLocaleMessage("user","user_xtgl_czygl_text_92"));
		$("#buttonDiv").find("input[type='button']").attr("disabled","");
		return;
	}
	else if(state==1)
	{
		add();
	}
	else if(state==2)
	{
		judgePrivilegeIsNull();
	}
	$("#buttonDiv").find("input[type='button']").attr("disabled","");
}

var aaaaa = 0 ;
function setLeftHeight()
{
	setLeftHeight2();
	window.onresize = function(){  
		if(aaaaa<1)
		{
			aaaaa=aaaaa+1;
		}else
		{
			aaaaa=0;
			return;
		}
		setLeftHeight2();
		setTimeout('aaaaa=0;',300);
	}
}

function setLeftHeight2()
{
	var isIE = (document.all) ? true : false;
	var isIE6 = isIE && (navigator.appVersion.match(/MSIE 6.0/i)=="MSIE 6.0" ? true : false);
	var hei=$(window).height();
	//alert($('#siderList').css('height'));
	$('#r_s_manager').css('height',hei-50);
	$('#m_container').css('height',hei-100);
	$('#siderList').css('height',hei-75);
}

function inputTipText(){
	//所有样式名中含有graytext的input
	$("input[class*=graytext]").each(function(){
		var oldVal=getJsLocaleMessage("user","user_xtgl_czygl_text_93"); //默认的提示性文本
		$(this)
		.css({'color':'#ccc'}) //灰色
		.focus(function(){
		if($(this).val()!=oldVal)
			{$(this).css({'color':'#000'})}
		else
			{$(this).val('').css({'color':'#ccc'})}
		})
		.blur(function(){
		if($(this).val()=="")
			{$(this).val(oldVal).css({'color':'#ccc'})}
		})
		.keydown(function(){
			$(this).css({'color':'#000'})
		})
	})
}

function checkText(ep)
{
	ep.val($.trim(ep.val()));
	if(ep.val()=="")
	{
		ep.next("label").css("display","inline");
	}else
	{
		ep.next("label").css("display","none");
	}

}

function init(){
	$("#state").val("0");
	//删除选中 删除输入框
	$("#siderList p").removeClass('roleNameColor').children(":text").remove();
	$("#roleNameHideenDiv").css("display","none").children(":text").val('');
	$(".l_pRightCon label").attr("class","");
	$(".chan_manag_son").css("display", "none");
	$("input[name='checkboxs']").attr("checked",false);
}
